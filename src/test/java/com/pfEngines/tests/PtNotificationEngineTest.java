package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.pt.Pt;
import com.mbasys.mars.ejb.entity.ptNotifCntct.PtNotifCntct;
import com.mbasys.mars.ejb.entity.ptNotifDetail.PtNotifDetail;
import com.mbasys.mars.ejb.entity.ptNotifPlan.PtNotifPlan;
import com.overall.accession.patientDemographics.PatientDemographics;
import com.overall.menu.MenuNavigation;
import com.overall.utils.PatientDemographicsUtil;
import com.overall.utils.XifinPortalUtils;
import com.xifin.sso.orgconfigs.constants.OrganizationConfigs;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.xap.utils.XifinAdminUtils;
import domain.accession.patientDemographics.Header;
import domain.accession.patientDemographics.PatientDemographicsRequireFields;
import domain.accession.patientDemographics.PatientInformation;
import domain.accession.patientDemographics.PayorInfo;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class PtNotificationEngineTest extends SeleniumBaseTest
{

    private static final String EXIST_EPI = "Exist EPI";
    private static final String EXIST_SSN = "Exist SSN";

    private PatientDemographics patientDemographics;
    private PatientDemographicsUtil patientDemographicsUtil;
    private XifinPortalUtils xifinPortalUtils;
    private XifinAdminUtils xifinAdminUtils;

    @BeforeSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword,
                            @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running BeforeSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            logIntoSso(ssoUsername, ssoPassword);
            logger.info("Set all patient notification data to inactive!");
            patientDao.deactivatePtNotifRecords();
            ssoDao.updateOrganizationConfigurationDataValue(orgAlias, String.valueOf(OrganizationConfigs.PT_NOTIFICATION.PT_NOTIF_SMS_SENDING_SERVICE.getId()), "TEST");
        }
        catch (Exception e)
        {
            Assert.fail("Error running BeforeSuite", e);
        }
        finally
        {
            quitWebDriver();
        }
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"ssoUsername", "ssoPassword"})
    void beforeMethod(String ssoUsername, String ssoPassword) throws Exception
    {
        xifinPortalUtils = new XifinPortalUtils(driver);
        patientDemographicsUtil = new PatientDemographicsUtil(config);
        patientDemographics = new PatientDemographics(driver, wait);
        try
        {
            xifinAdminUtils = new XifinAdminUtils(driver, config);
        }
        catch (MalformedURLException e)
        {
            logger.error("Malformed url in setup", e);
            throw e;
        }
        logIntoSso(ssoUsername, ssoPassword);
        MenuNavigation menuNavigation = new MenuNavigation(driver, config);
        menuNavigation.navigateToAccessionPatientDemographicsPage();

    }

    @Test(priority = 1, description = "Patient Enrollment Notification Success")
    @Parameters({"phoneNumber", "ssoUsername", "ssoPassword"})
    public void testPatientEnrollmentNotificationSuccess(String phoneNumber, String ssoUsername, String ssoPassword) throws Exception
    {
        logger.info("==== Testing - testPatientEnrollmentNotificationSuccess ==== ");

        logger.info("*** Step 1: User login successful. Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();

        logger.info("*** Step 2: Input an EPI that exists in PT table. Then tab out.");
        Header header = setDataPatientDemographicsSection();
        String epi = header.getEpi();

        logger.info("*** Step 3: The Patient Demographics detail page is displayed with correct header data and empty data in most sections.");
        enterValues(patientDemographics.ptInfoSectionHomePhnInput(), phoneNumber);
        enterValues(patientDemographics.ptInfoSectionPostalCodeInput(), "92130");
        enterValues(patientDemographics.pyrInfoSectionPayoridInput(), "P");

        logger.info("*** Step 4: Click [Save and Clear] button.");
        clickOnSaveAndClearBtn();

        logger.info("*** Step 5: Patient Demographics load page is displayed. Patient record updated.");
        verifyPatientDemographicsLoadPageIsDisplayed();
        PatientInformation patientInfo = createPatientInformation(phoneNumber);
        PayorInfo payorInfo = createPayorInfo();
        verifyPatientDemographicsIsSavedInPTByRequireFields(header, patientInfo, payorInfo);

        logger.info("*** Step 6: Run PatientNotification Engine");
        Thread.sleep(5000);
        xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword, null, "PatientNotificationEngine", "SSO_APP_STAGING", true);
        Thread.sleep(5000);

        logger.info("*** Step 7: Verify notification result");
        Pt pt = patientDao.getPtByEpi(epi);
        PtNotifCntct ptNotifCntct = patientDao.getPtNotifCntctBySeqId(pt.getSeqId());
        assertTrue(StringUtils.startsWith(ptNotifCntct.getPtCntct(), "+"), "Phone number should start with a prefix, observedPhn=" + ptNotifCntct.getPtCntct() + ", expectedSuffix=+");
        assertTrue(StringUtils.endsWith(ptNotifCntct.getPtCntct(), phoneNumber), "Phone number does not have expected ending, observedPhn=" + ptNotifCntct.getPtCntct() + ", expectedPhn=" + phoneNumber);
        assertNull(ptNotifCntct.getOptOutDt());
        assertTrue(ptNotifCntct.getIsVerified());

        PtNotifDetail ptNotifDetail = patientDao.getPtNotifDetailByPtNotifCntctId(ptNotifCntct.getSeqId());
        assertNotNull(ptNotifDetail.getMessageId());
        assertFalse(ptNotifDetail.getIsOptedOut());
        assertFalse(ptNotifDetail.getIsUndeliverable());
        assertNotNull(ptNotifDetail.getSendDt());

        PtNotifPlan ptNotifPlan = patientDao.getPtNotifPlanBySeqId(pt.getSeqId());
        assertTrue(ptNotifPlan.getIsActive());
    }

    @Test(priority = 1, description = "Patient Enrollment Notification Invalid")
    @Parameters({"phoneNumber", "ssoUsername", "ssoPassword"})
    public void testPatientEnrollmentNotificationInvalid(String phoneNumber, String ssoUsername, String ssoPassword) throws Exception
    {
        logger.info("==== Testing - testPatientEnrollmentNotificationInvalid ==== ");

        logger.info("*** Step 1: User login successful. Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();

        logger.info("*** Step 2: Input an EPI that exists in PT table. Then tab out.");
        Header header = setDataPatientDemographicsSection();
        String epi = header.getEpi();

        logger.info("*** Step 3: The Patient Demographics detail page is displayed with correct header data and empty data in most sections.");
        enterValues(patientDemographics.ptInfoSectionHomePhnInput(), phoneNumber);
        enterValues(patientDemographics.ptInfoSectionPostalCodeInput(), "92130");
        enterValues(patientDemographics.pyrInfoSectionPayoridInput(), "P");

        logger.info("*** Step 4: Click [Save and Clear] button.");
        clickOnSaveAndClearBtn();

        logger.info("*** Step 5: Patient Demographics load page is displayed. Patient record updated.");
        verifyPatientDemographicsLoadPageIsDisplayed();
        PatientInformation patientInfo = createPatientInformation(phoneNumber);
        PayorInfo payorInfo = createPayorInfo();
        verifyPatientDemographicsIsSavedInPTByRequireFields(header, patientInfo, payorInfo);

        logger.info("*** Step 6: Run PatientNotification Engine");
        Thread.sleep(5000);
        xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword, null, "PatientNotificationEngine", "SSO_APP_STAGING", true);
        Thread.sleep(5000);

        logger.info("*** Step 7: Verify notification result");
        Pt pt = patientDao.getPtByEpi(epi);
        PtNotifCntct ptNotifCntct = patientDao.getPtNotifCntctBySeqId(pt.getSeqId());
        assertFalse(StringUtils.startsWith(ptNotifCntct.getPtCntct(), "+"), "Phone number should not start with a prefix, observedPhn=" + ptNotifCntct.getPtCntct() + ", expectedSuffix=+");
        assertTrue(StringUtils.endsWith(ptNotifCntct.getPtCntct(), phoneNumber), "Phone number does not have expected ending, observedPhn=" + ptNotifCntct.getPtCntct() + ", expectedPhn=" + phoneNumber);
        assertNull(ptNotifCntct.getOptOutDt());
        assertTrue(ptNotifCntct.getIsUndeliverable());
        assertFalse(ptNotifCntct.getIsVerified());

        PtNotifDetail ptNotifDetail = patientDao.getPtNotifDetailByPtNotifCntctId(ptNotifCntct.getSeqId());
        assertNull(ptNotifDetail);

        PtNotifPlan ptNotifPlan = patientDao.getPtNotifPlanBySeqId(pt.getSeqId());
        assertTrue(ptNotifPlan.getIsActive());
    }

    @Test(priority = 1, description = "Patient Enrollment Notification Opt-Out")
    @Parameters({"phoneNumber", "ssoUsername", "ssoPassword"})
    public void testPatientEnrollmentNotificationOptOut(String phoneNumber, String ssoUsername, String ssoPassword) throws Exception
    {
        logger.info("==== Testing - testPatientEnrollmentNotificationUndeliverable ==== ");

        logger.info("*** Step 1: User login successful. Patient Demographics load page is displayed.");
        verifyPatientDemographicsLoadPageIsDisplayed();

        logger.info("*** Step 2: Input an EPI that exists in PT table. Then tab out.");
        Header header = setDataPatientDemographicsSection();
        String epi = header.getEpi();

        logger.info("*** Step 3: The Patient Demographics detail page is displayed with correct header data and empty data in most sections.");
        enterValues(patientDemographics.ptInfoSectionHomePhnInput(), phoneNumber);
        enterValues(patientDemographics.ptInfoSectionPostalCodeInput(), "92130");
        enterValues(patientDemographics.pyrInfoSectionPayoridInput(), "P");

        logger.info("*** Step 4: Click [Save and Clear] button.");
        clickOnSaveAndClearBtn();

        logger.info("*** Step 5: Patient Demographics load page is displayed. Patient record updated.");
        verifyPatientDemographicsLoadPageIsDisplayed();
        PatientInformation patientInfo = createPatientInformation(phoneNumber);
        PayorInfo payorInfo = createPayorInfo();
        verifyPatientDemographicsIsSavedInPTByRequireFields(header, patientInfo, payorInfo);

        logger.info("*** Step 6: Run PatientNotification Engine");
        Thread.sleep(5000);
        xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword, null, "PatientNotificationEngine", "SSO_APP_STAGING", true);
        Thread.sleep(5000);

        logger.info("*** Step 7: Verify notification result");
        Pt pt = patientDao.getPtByEpi(epi);
        PtNotifCntct ptNotifCntct = patientDao.getPtNotifCntctBySeqId(pt.getSeqId());
        assertNotNull(ptNotifCntct);
        assertFalse(StringUtils.startsWith(ptNotifCntct.getPtCntct(), "+"), "Phone number should not start with a prefix, observedPhn=" + ptNotifCntct.getPtCntct() + ", expectedSuffix=+");
        assertTrue(StringUtils.endsWith(ptNotifCntct.getPtCntct(), phoneNumber), "Phone number does not have expected ending, observedPhn=" + ptNotifCntct.getPtCntct() + ", expectedPhn=" + phoneNumber);
        assertNull(ptNotifCntct.getOptOutDt());
        assertTrue(ptNotifCntct.getIsUndeliverable());
        assertFalse(ptNotifCntct.getIsVerified());

        PtNotifDetail ptNotifDetail = patientDao.getPtNotifDetailByPtNotifCntctId(ptNotifCntct.getSeqId());
        assertNull(ptNotifDetail);

        PtNotifPlan ptNotifPlan = patientDao.getPtNotifPlanBySeqId(pt.getSeqId());
        assertTrue(ptNotifPlan.getIsActive());
    }

    private void verifyPatientDemographicsIsSavedInPTByRequireFields(Header header, PatientInformation patientInformation, PayorInfo payorInfo) throws Exception {
        org.junit.Assert.assertNotNull(patientDemographicsUtil);
        org.junit.Assert.assertNotNull(header);
        org.junit.Assert.assertNotNull(patientInformation);
        org.junit.Assert.assertNotNull(payorInfo);
        PatientDemographicsRequireFields actPtDemoRequireFields = patientDemographicsUtil.mapPatientDemographicsAtRequireFieldsFromDB(header, patientInformation, payorInfo);
        PatientDemographicsRequireFields expPtDemoRequireFields = patientDemographicsUtil.mapPatientDemographicsAtRequireFields(header, patientInformation, payorInfo);
        assertEquals(actPtDemoRequireFields,expPtDemoRequireFields, "        Data are saved in DB correctly at require fields.");
    }

    private void verifyPatientDemographicsLoadPageIsDisplayed() throws Exception {
        wait.until(ExpectedConditions.elementToBeClickable(patientDemographics.headerPageTitleTxt()));
        assertFalse(isElementHidden(patientDemographics.ptDemoSection(), 10), "         Patient Demographics section is displayed");
    }

    private Header setDataPatientDemographicsSection() throws Exception {
        Header header = new Header();
        String newEpi = createNewEpi();
        enterValues(patientDemographics.ptDemoSectionEPIInput(), newEpi);
        header.setEpi(newEpi);
        return header;
    }

    private String createNewEpi() throws Exception {
        boolean isExist = true;
        RandomCharacter randomCharacter = new RandomCharacter();
        String newData = randomCharacter.getRandomAlphaString(3) + randomCharacter.getNonZeroRandomNumericString(9);

        while (isExist) {
            Pt pt = patientDao.getPtByEpi(newData);

            if (pt == null || pt.getSeqId() == 0) {
                isExist = false;
            } else {
                newData = randomCharacter.getRandomAlphaString(3) + randomCharacter.getNonZeroRandomNumericString(9);
            }
        }
        return newData;
    }

    private void clickOnSaveAndClearBtn() throws Exception {
        clickHiddenPageObject(patientDemographics.footerSaveAndClearBtn(), 0);
        xifinPortalUtils.waitForPageLoaded(wait);
    }

    private PatientInformation createPatientInformation(String phoneNumber)
    {
        PatientInformation patientInfo = new PatientInformation();
        patientInfo.setHomePhone(phoneNumber);
        patientInfo.setPostalCode("92130");
        patientInfo.setState("California");
        patientInfo.setCountry("USA");
        patientInfo.setCity("SAN DIEGO");
        return patientInfo;
    }

    private PayorInfo createPayorInfo()
    {
        PayorInfo payorInfo = new PayorInfo();
        payorInfo.setPayorId("P");
        payorInfo.setPayorName("PATIENT-X");
        payorInfo.setGroupName("PATIENT");
        payorInfo.setPayorPriority(1);
        return payorInfo;
    }

}
