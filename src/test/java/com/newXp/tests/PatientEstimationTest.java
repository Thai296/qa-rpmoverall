package com.newXp.tests;

import com.mbasys.common.utility.Money;
import com.mbasys.mars.ejb.entity.ptEstimation.PtEstimation;
import com.mbasys.mars.ejb.entity.ptEstimationBenefit.PtEstimationBenefit;
import com.mbasys.mars.ejb.entity.ptEstimationProc.PtEstimationProc;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.persistance.MiscMap;
import com.overall.accession.PatientServiceCenter.PtEstimationConfig;
import com.overall.menu.MenuNavigation;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.util.DateConversion;
import com.xifin.utils.SeleniumBaseTest;
import domain.accession.PatientServiceCenter.PtEstimationDomain;
import domain.accession.PatientServiceCenter.patientestimation.InsuranceInformation;
import domain.accession.PatientServiceCenter.patientestimation.OrderInformation;
import domain.accession.PatientServiceCenter.patientestimation.PatientEstimation;
import domain.accession.PatientServiceCenter.patientestimation.PatientInformation;
import domain.accession.PatientServiceCenter.patientestimation.PatientResponsibility;
import domain.accession.PatientServiceCenter.patientestimation.PatientResponsibilityDetail;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.testng.Assert.assertTrue;

public class PatientEstimationTest extends SeleniumBaseTest
{
    private static final String RELSHP_TYP_SPOUSE = "SPOUSE";
    private static final String RELSHP_TYP_SELF = "SELF";
    private static final String RELSHP_TYP_OTHER = "OTHER";
    private static final String SEPARATOR = "|";
    private static final String TEST_SEPARATOR = ",";
    private static final String DETAIL_SEPARATOR = "^";


    @BeforeMethod(alwaysRun = true)
    @Parameters({"ssoUsername", "ssoPassword"})
    public void beforeMethod(String ssoUsername, String ssoPassword, Method method)
    {
        try
        {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            logIntoSso(ssoUsername, ssoPassword);
            MenuNavigation navigation = new MenuNavigation(driver, config);
            navigation.navigateToPatientEstimationPage();
        }
        catch (Exception e)
        {
            logger.error("Error running BeforeMethod", e);
        }
    }

    @Test(priority = 1, description = "Verify Help Links")
    @Parameters({})
    public void testXPR_1893() throws Exception {
        logger.info("===== Testing - testXPR_1893 =====");
        PtEstimationConfig ptEstimation = new PtEstimationConfig(driver, wait, methodName);

        logger.info("Verify that the Patient Estimation page is displayed with correct page title");
        Assert.assertTrue(ptEstimation.patientEstimationPageTitle().isDisplayed());
        Assert.assertEquals(ptEstimation.patientEstimationPageTitle().getText(),"Patient Estimation","Patient Estimation screen should be displayed.");
        String parent = driver.getWindowHandle();

        logger.info("Click Help icon button in the Header");
        wait.until(ExpectedConditions.elementToBeClickable(ptEstimation.helpIconInHeaderSection()));
        ptEstimation.helpIconInHeaderSection().click();

        logger.info("Verify that Help file in the Header can be opened properly");
        switchToPopupWin();
        Assert.assertTrue(driver.getCurrentUrl().contains("p_patient_estimation_header.htm"),"Help file in Load page is opened.");
        Assert.assertTrue(ptEstimation.titleTextInHelp().getText().contains("Patient Estimation Header"), "Help file in Patient Estimation Header should be opened.");
        driver.close();
        switchToParentWindow(parent);

        ptEstimation.enterCreateNewEstimation();

        logger.info("Accession Information section: Click help icon");
        Assert.assertTrue(ptEstimation.helpIconInAccnInfoSection().isDisplayed());
        ptEstimation.helpIconInAccnInfoSection().click();

        logger.info("Verify that Help page in Accession Information section is opened properly");
        switchToPopupWin();
        Assert.assertTrue(driver.getCurrentUrl().contains("p_patient_estimation_accession_information.htm"),"Help page in Accession Information Section is opened.");
        Assert.assertTrue(ptEstimation.titleTextInHelp().getText().contains("Accession Information"), "Help file in Accession Information Section should be opened.");
        Assert.assertTrue(ptEstimation.helpPageTableOfContentsLink().isDisplayed());
        driver.close();
        switchToParentWindow(parent);

        logger.info("Patient Information section: Click help icon");
        Assert.assertTrue(ptEstimation.helpIconInPtInfoSection().isDisplayed());
        ptEstimation.helpIconInPtInfoSection().click();

        logger.info("Verify that Help page in Patient Information section is opened properly");
        switchToPopupWin();
        Assert.assertTrue(driver.getCurrentUrl().contains("p_patient_estimation_patient_information.htm"),"Help page in Patient Information Section is opened.");
        Assert.assertTrue(ptEstimation.titleTextInHelp().getText().contains("Patient Information"), "Help file in Patient Information Section should be opened.");
        Assert.assertTrue(ptEstimation.helpPageTableOfContentsLink().isDisplayed());
        driver.close();
        switchToParentWindow(parent);

        logger.info("Click help icon in Insured Information section is Displayed");
        Assert.assertTrue(ptEstimation.helpIconInInsuredInfoSection().isDisplayed());
        ptEstimation.helpIconInInsuredInfoSection().click();

        logger.info("Verify that Help file in Insured Information section is opened properly");
        switchToPopupWin();
        Assert.assertTrue(driver.getCurrentUrl().contains("p_patient_estimation_insurance_information.htm"),"Help page in Insurance Information Section is opened.");
        Assert.assertTrue(ptEstimation.titleTextInHelp().getText().contains("Insurance Information"), "Help file in Insurance Information Section should be opened.");
        Assert.assertTrue((ptEstimation.helpPageTableOfContentsLink().isDisplayed()));
        driver.close();
        switchToParentWindow(parent);

        logger.info("Click help icon in Order Information section");
        Assert.assertTrue(ptEstimation.helpIconInOrderInfoSection().isDisplayed());
        ptEstimation.helpIconInOrderInfoSection().click();

        logger.info("Verify that Help file is opened properly in Order Information section");
        switchToPopupWin();
        Assert.assertTrue(driver.getCurrentUrl().contains("p_patient_estimation_order_information.htm"),"Help file is opened in Order Information Section.");
        Assert.assertTrue(ptEstimation.titleTextInHelp().getText().contains("Order Information"), "Help file in Order Information Section should be opened.");
        Assert.assertTrue((ptEstimation.helpPageTableOfContentsLink().isDisplayed()));
        driver.close();
        switchToParentWindow(parent);

        logger.info("Click help icon in Patient Responsibility section");
        Assert.assertTrue(ptEstimation.helpIconInPtResponsibilitySection().isDisplayed());
        ptEstimation.helpIconInPtResponsibilitySection().click();

        logger.info("Verify that Help file is opened properly in Patient Responsibility section");
        switchToPopupWin();
        Assert.assertTrue(driver.getCurrentUrl().contains("p_patient_estimation_patient_responsibility.htm"),"Help file is opened in Patient Responsibility Section.");
        Assert.assertTrue(ptEstimation.titleTextInHelp().getText().contains("Patient Responsibility"), "Help file in Patient Responsibility Section should be opened.");
        Assert.assertTrue((ptEstimation.helpPageTableOfContentsLink().isDisplayed()));
        driver.close();
        switchToParentWindow(parent);

        logger.info("Click help icon in page footer section");
        scrollToElement(ptEstimation.helpIconInFooterSection());
        assertTrue(isElementPresent(ptEstimation.helpIconInFooterSection(),5),"Help icon should be displayed in page footer section.");
        clickHiddenPageObject(ptEstimation.helpIconInFooterSection(), 0);

        logger.info("Verify that Help file is opened properly in footer section");
        switchToPopupWin();
        Assert.assertTrue(driver.getCurrentUrl().contains("p_patient_estimation_summary.htm"),"Help file is opened in page footer section.");
        Assert.assertTrue(ptEstimation.titleTextInHelp().getText().contains("Patient Estimation"), "Help file in Patient Estimation Summary should be opened.");
        Assert.assertTrue((ptEstimation.helpPageTableOfContentsLink().isDisplayed()));
        driver.close();
        switchToParentWindow(parent);

        logger.info("Click on Reset button");
        Assert.assertTrue(ptEstimation.resetButton().isDisplayed());
        ptEstimation.resetButton().click();

    }
    @Test(priority = 1, description = "Verify Patient Estimated Responsibility Details with Deductible, Coinsurance and Copay")
    @Parameters({"patientInfo", "insuranceInfo", "orderInfo", "acceptResponsibility", "note", "ptEstimatedResponseDetail"})
    public void testXPR_1894(String patientInfo, String insuranceInfo, String orderInfo, String acceptResponsibility, String note, String ptEstimatedResponseDetail) throws Exception {
        logger.info("===== Testing - testXPR_1894 =====");
        PtEstimationConfig ptEstimation = new PtEstimationConfig(driver, wait, methodName);
        PtEstimationDomain ptEstimationDomain = new PtEstimationDomain();
        enterPatientEstimationFields(ptEstimationDomain, ptEstimation, StringUtils.EMPTY, patientInfo, insuranceInfo, orderInfo, RELSHP_TYP_SELF, null);

        logger.info("Create expected Estimated Patient Responsibility Information");
        List<String> expectedPtEstimatedResponseList = Arrays.asList(org.apache.commons.lang3.StringUtils.split(ptEstimatedResponseDetail));

        List<String> actualPtEstimatedResponseList = getActualPtEstimationResponseAsList(ptEstimation);
        logger.info("Compare Expected and Actual Patient Estimated Responsibilities, expectedPtEstimatedResponseList="+ expectedPtEstimatedResponseList+", actualPtEstimatedResponseList="+actualPtEstimatedResponseList);
        Assert.assertEquals(expectedPtEstimatedResponseList, actualPtEstimatedResponseList);

        logger.info("Verifying the Payor Updated Demographic Data is displayed in Patient Responsibility");
        Assert.assertEquals(ptEstimation.getPayorUpdatedLabel().getText(), "Payor Updated Demographic Data");
        logger.info("Verifying Relationship was updated to "+ptEstimation.updatedDemoDataByField(RELSHP_TYP_SPOUSE).getText());
        Assert.assertEquals(ptEstimation.updatedDemoDataByField(RELSHP_TYP_SPOUSE).getText(), RELSHP_TYP_SPOUSE);

        // Save note and resp check box
        enterPatientResponsibilityInfo(ptEstimationDomain, acceptResponsibility, note, ptEstimation);
        // Save Page
        logger.info("Click on Save button");
        Assert.assertTrue(ptEstimation.saveButton().isDisplayed());
        ptEstimation.saveButton().click();

        List<PtEstimation> ptEstimations = accessionDao.getPtEstimationsByAccnInfo(DateConversion.stringToSqlDate(ptEstimationDomain.getPtDateOfBirth()), ptEstimationDomain.getPtLastName(), ptEstimationDomain.getPtFirstName());
        Assert.assertNotNull(ptEstimations, "Saved PT_ESTIMATION records should not be null");
        Assert.assertTrue(!ptEstimations.isEmpty(), "Saved PT_ESTIMATION records should not be empty");
        // todo re-enable after upgrade to jdk 11 or query redo... 280.11.15.00.00 has these columns in the vo
        // Assert.assertEquals(ptEstimations.get(0).getNote(), ptEstimationDomain.getNote(), "Expected note to be persisted");
        // Assert.assertTrue(ptEstimations.get(0).getIsPtRespAccepted(), "Expected resp accepted");

        // Reset Page
        logger.info("Click on Reset button");
        Assert.assertTrue(ptEstimation.resetButton().isDisplayed());
        ptEstimation.resetButton().click();
    }

    @Test(priority = 1, description = "Verify Patient Responsibility and Payor Updated Demographic Data with Pyr translation and relationship updates")
    @Parameters({"accnInfo", "patientInfo", "insuranceInfo", "orderInfo", "ptEstimatedResponseDetail", "newPyrAbbrev"})
    public void testXPR_2048(String accnInfo, String patientInfo, String insuranceInfo, String orderInfo, String ptEstimatedResponseDetail, String newPyrAbbrev) throws Exception {
        logger.info("===== Testing - testXPR_2048 =====");
        PtEstimationConfig ptEstimation = new PtEstimationConfig(driver, wait, methodName);
        PtEstimationDomain ptEstimationDomain = new PtEstimationDomain();
        enterPatientEstimationFields(ptEstimationDomain, ptEstimation, accnInfo, patientInfo, insuranceInfo, orderInfo, RELSHP_TYP_SELF, null);

        logger.info("Create expected Estimated Patient Responsibility Information");
        List<String> expectedPtEstimatedResponseList = Arrays.asList(org.apache.commons.lang3.StringUtils.split(ptEstimatedResponseDetail));
        Pyr newPyr = rpmDao.getPyrByPyrAbbrv(testDb, newPyrAbbrev);
        List<String> actualPtEstimatedResponseList = getActualPtEstimationResponseAsList(ptEstimation);
        logger.info("Compare Expected and Actual Patient Estimated Responsibilities, expectedPtEstimatedResponsList="+ expectedPtEstimatedResponseList+", actualPtEstimatedResponsList="+actualPtEstimatedResponseList);
        Assert.assertEquals(actualPtEstimatedResponseList, expectedPtEstimatedResponseList);

        logger.info("Verifying the Payor Updated Demographic Data is displayed in Patient Responsibility");
        Assert.assertEquals(ptEstimation.getPayorUpdatedLabel().getText(), "Payor Updated Demographic Data");
        logger.info("Verifying Relationship was updated to "+ptEstimation.updatedDemoDataByField(RELSHP_TYP_SPOUSE).getText());
        Assert.assertEquals(ptEstimation.updatedDemoDataByField(RELSHP_TYP_SPOUSE).getText(), RELSHP_TYP_SPOUSE);

        logger.info("Verifying Payor ID was updated to "+ptEstimation.updatedDemoDataByField(newPyr.getPyrAbbrv()).getText());
        Assert.assertTrue(ptEstimation.updatedDemoDataByField(newPyr.getPyrAbbrv()).getText().contains(newPyr.getPyrAbbrv()+" - "+newPyr.getName()), RELSHP_TYP_SPOUSE);

        // Reset Page
        logger.info("Click on Reset button");
        Assert.assertTrue(ptEstimation.resetButton().isDisplayed());
        ptEstimation.resetButton().click();
    }
    @Test(priority = 1, description = "Verify Patient Responsibility and Payor Updated Demographic Data with Pyr translation and demo updates")
    @Parameters({"patientInfo", "insuranceInfo", "orderInfo", "ptEstimatedResponseDetail", "newPyrAbbrev", "patientFNm", "patientLNm", "subscriberFNm", "subscriberLNm"})
    public void testXPR_2049(String patientInfo, String insuranceInfo, String orderInfo, String ptEstimatedResponseDetail, String newPyrAbbrev, String patientFNm, String patientLNm, String subscriberFNm, String subscriberLNm) throws Exception {
        logger.info("===== Testing - testXPR_2049 =====");
        PtEstimationConfig ptEstimation = new PtEstimationConfig(driver, wait, methodName);
        PtEstimationDomain ptEstimationDomain = new PtEstimationDomain();
        enterPatientEstimationFields(ptEstimationDomain, ptEstimation, StringUtils.EMPTY, patientInfo, insuranceInfo, orderInfo, RELSHP_TYP_OTHER, null);

        logger.info("Create expected Estimated Patient Responsibility Information");
        List<String> expectedPtEstimatedResponseList = Arrays.asList(org.apache.commons.lang3.StringUtils.split(ptEstimatedResponseDetail));
        Pyr newPyr = rpmDao.getPyrByPyrAbbrv(testDb, newPyrAbbrev);
        List<String> actualPtEstimatedResponseList = getActualPtEstimationResponseAsList(ptEstimation);
        logger.info("Compare Expected and Actual Patient Estimated Responsibilities, expectedPtEstimatedResponsList="+ expectedPtEstimatedResponseList+", actualPtEstimatedResponsList="+actualPtEstimatedResponseList);
        Assert.assertEquals(actualPtEstimatedResponseList, expectedPtEstimatedResponseList);

        logger.info("Verifying the Payor Updated Demographic Data is displayed in Patient Responsibility");
        Assert.assertEquals(ptEstimation.getPayorUpdatedLabel().getText(), "Payor Updated Demographic Data");
        logger.info("Verifying Patient First Name was updated to "+ptEstimation.updatedDemoDataByField(patientFNm).getText());
        Assert.assertEquals(ptEstimation.updatedDemoDataByField(patientFNm).getText(), patientFNm);
        logger.info("Verifying Patient Last Name was updated to "+ptEstimation.updatedDemoDataByField(patientLNm).getText());
        Assert.assertEquals(ptEstimation.updatedDemoDataByField(patientLNm).getText(), patientLNm);

        logger.info("Verifying Subscriber First Name was updated to "+ptEstimation.updatedDemoDataByField(subscriberFNm).getText());
        Assert.assertEquals(ptEstimation.updatedDemoDataByField(subscriberFNm).getText(), subscriberFNm);
        logger.info("Verifying Subscriber Last Name was updated to "+ptEstimation.updatedDemoDataByField(subscriberLNm).getText());
        Assert.assertEquals(ptEstimation.updatedDemoDataByField(subscriberLNm).getText(), subscriberLNm);
        logger.info("Verifying Payor ID was updated to "+ptEstimation.updatedDemoDataByField(newPyr.getPyrAbbrv()).getText());
        Assert.assertTrue(ptEstimation.updatedDemoDataByField(newPyr.getPyrAbbrv()).getText().contains(newPyr.getPyrAbbrv()+" - "+newPyr.getName()), RELSHP_TYP_SPOUSE);

        // Reset Page
        logger.info("Click on Reset button");
        Assert.assertTrue(ptEstimation.resetButton().isDisplayed());
        ptEstimation.resetButton().click();
    }

    @Test(priority = 1, description = "Pt Estimated Response using existing Accession ID")
    @Parameters({"accnInfo", "ptEstimatedResponseDetail"})
    public void testXPR_2066(String accnInfo, String ptEstimatedResponseDetail) throws Exception {
        logger.info("===== Testing - testXPR_2066 =====");
        PtEstimationConfig ptEstimation = new PtEstimationConfig(driver, wait, methodName);
        PtEstimationDomain ptEstimationDomain = new PtEstimationDomain();
        enterPatientEstimationFields(ptEstimationDomain, ptEstimation, accnInfo, EMPTY, EMPTY, EMPTY, RELSHP_TYP_SELF, null);

        logger.info("Create expected Estimated Patient Responsibility Information");
        List<String> expectedPtEstimatedResponseList = Arrays.asList(org.apache.commons.lang3.StringUtils.split(ptEstimatedResponseDetail));
        logger.info("Create actual Estimated Patient Responsibility Information");
        List<String> actualPtEstimatedResponseList = getActualPtEstimationResponseAsList(ptEstimation);
        logger.info("Compare Expected and Actual Patient Estimated Responsibilities, expectedPtEstimatedResponseList="+ expectedPtEstimatedResponseList+", actualPtEstimatedResponseList="+actualPtEstimatedResponseList);
        Assert.assertEquals(actualPtEstimatedResponseList, expectedPtEstimatedResponseList);

        // Reset Page
        logger.info("Click on Reset button");
        Assert.assertTrue(ptEstimation.resetButton().isDisplayed());
        ptEstimation.resetButton().click();
    }

    @Test(priority = 1, description = "Check Copay + Coinsurance + Deductible equals Patient Responsibility")
    @Parameters({"patientInfo", "insuranceInfo", "orderInfo", "ptEstimatedResponseDetail", "newPtEstimatedResponseDetail"})
    public void testRPM_NEW_992902(String patientInfo, String insuranceInfo, String orderInfo, String ptEstimatedResponseDetail, String newPtEstimatedResponseDetail) throws Exception {
        logger.info("===== Testing - testRPM_NEW_992902 =====");
        PtEstimationConfig ptEstimation = new PtEstimationConfig(driver, wait, methodName);
        PtEstimationDomain ptEstimationDomain = new PtEstimationDomain();
        enterPatientEstimationFields(ptEstimationDomain, ptEstimation, StringUtils.EMPTY, patientInfo, insuranceInfo, orderInfo, RELSHP_TYP_SELF, null);

        logger.info("Create expected Estimated Patient Responsibility Information");
        List<String> expectedPtEstimatedResponseList = Arrays.asList(org.apache.commons.lang3.StringUtils.split(ptEstimatedResponseDetail));

        List<String> actualPtEstimatedResponseList = getActualPtEstimationResponseAsList(ptEstimation);
        logger.info("Compare Expected and Actual Patient Estimated Responsibilities, expectedPtEstimatedResponseList="+ expectedPtEstimatedResponseList+", actualPtEstimatedResponseList="+actualPtEstimatedResponseList);
        Assert.assertEquals(expectedPtEstimatedResponseList, actualPtEstimatedResponseList);

        logger.info("Verifying the Payor Updated Demographic Data is displayed in Patient Responsibility");
        Assert.assertEquals(ptEstimation.getPayorUpdatedLabel().getText(), "Payor Updated Demographic Data");
        logger.info("Verifying Relationship was updated to "+ptEstimation.updatedDemoDataByField(RELSHP_TYP_SPOUSE).getText());
        Assert.assertEquals(ptEstimation.updatedDemoDataByField(RELSHP_TYP_SPOUSE).getText(), RELSHP_TYP_SPOUSE);

        List<String> newExpectedPtEstimatedResponseList = Arrays.asList(org.apache.commons.lang3.StringUtils.split(newPtEstimatedResponseDetail));

        // Enter new Patient Resp Amt and Proc Code grid values
        scrollToElement(ptEstimation.editProcCodesButton());
        ptEstimation.enterPtRespAmtAndProcDataForPatientEstimation(newExpectedPtEstimatedResponseList);

        // Save Page
        logger.info("Click on Save button");
        Assert.assertTrue(ptEstimation.saveButton().isDisplayed());
        ptEstimation.saveButton().click();

        List<String> newActualPtEstimatedResponseList = getActualPtEstimationResponseAsList(ptEstimation);
        logger.info("Compare NEW Expected and NEW Actual Patient Estimated Responsibilities, newExpectedPtEstimatedResponseList="+ newExpectedPtEstimatedResponseList+", newActualPtEstimatedResponseList="+newActualPtEstimatedResponseList);
        Assert.assertEquals(newExpectedPtEstimatedResponseList, newActualPtEstimatedResponseList);

        PtEstimation ptEstimationSavedRow = accessionDao.getPtEstimation(Integer.parseInt(ptEstimation.getEstimationId().getAttribute("value")));
        Assert.assertNotNull(ptEstimationSavedRow, "Saved PT_ESTIMATION records should not be null");
        Assert.assertEquals(ptEstimationSavedRow.getPtRespAmtAsMoney().toDisplayString(), newExpectedPtEstimatedResponseList.get(9), "Updated PT_ESTIMATION records should contain the new amount");

        // Reset Page
        logger.info("Click on Reset button");
        Assert.assertTrue(ptEstimation.resetButton().isDisplayed());
        ptEstimation.resetButton().click();
    }

    @Test(priority = 1, description = "Update an existing Patient Estimation based on typed estimation ID")
    @Parameters({"estimationInfo", "patientInfo", "insuranceInfo", "orderInfo", "ptEstimatedResponseDetail", "newPtEstimatedResponseDetail"})
    private void testRPM_NEW_993186(String estimationInfo, String patientInfo, String insuranceInfo, String orderInfo, String ptEstimatedResponseDetail, String newPtEstimatedResponseDetail) throws Exception
    {
        logger.info("===== Testing - testRPM_NEW_993186 =====");
        PtEstimationConfig ptEstimation = new PtEstimationConfig(driver, wait, methodName);
        PtEstimationDomain ptEstimationDomain = new PtEstimationDomain();
        enterPatientEstimationFields(ptEstimationDomain, ptEstimation, StringUtils.EMPTY, patientInfo, insuranceInfo, orderInfo, RELSHP_TYP_SELF, estimationInfo);

        logger.info("Create expected Estimated Patient Responsibility Information");
        List<String> expectedPtEstimatedResponseList = Arrays.asList(org.apache.commons.lang3.StringUtils.split(ptEstimatedResponseDetail));

        List<String> actualPtEstimatedResponseList = getActualPtEstimationResponseAsList(ptEstimation);
        logger.info("Compare Expected and Actual Patient Estimated Responsibilities, expectedPtEstimatedResponseList="+ expectedPtEstimatedResponseList+", actualPtEstimatedResponseList="+actualPtEstimatedResponseList);
        Assert.assertEquals(expectedPtEstimatedResponseList, actualPtEstimatedResponseList);

        logger.info("Verifying the Payor Updated Demographic Data is displayed in Patient Responsibility");
        Assert.assertEquals(ptEstimation.getPayorUpdatedLabel().getText(), "Payor Updated Demographic Data");
        logger.info("Verifying Relationship was updated to " + ptEstimation.updatedDemoDataByField(RELSHP_TYP_SPOUSE).getText());
        Assert.assertEquals(ptEstimation.updatedDemoDataByField(RELSHP_TYP_SPOUSE).getText(), RELSHP_TYP_SPOUSE);

        List<String> newExpectedPtEstimatedResponseList = Arrays.asList(org.apache.commons.lang3.StringUtils.split(newPtEstimatedResponseDetail));

        // Enter new Patient Resp Amt and Proc Code grid values
        scrollToElement(ptEstimation.editProcCodesButton());
        ptEstimation.enterPtRespAmtAndProcDataForPatientEstimation(newExpectedPtEstimatedResponseList);

        // Save Page
        logger.info("Click on Save button");
        Assert.assertTrue(ptEstimation.saveButton().isDisplayed());
        ptEstimation.saveButton().click();

        List<String> newActualPtEstimatedResponseList = getActualPtEstimationResponseAsList(ptEstimation);
        logger.info("Compare NEW Expected and NEW Actual Patient Estimated Responsibilities, newExpectedPtEstimatedResponseList="+ newExpectedPtEstimatedResponseList+", newActualPtEstimatedResponseList="+newActualPtEstimatedResponseList);
        Assert.assertEquals(newExpectedPtEstimatedResponseList, newActualPtEstimatedResponseList);

        PtEstimation ptEstimationSavedRow = accessionDao.getPtEstimation(Integer.parseInt(ptEstimation.getEstimationId().getAttribute("value")));
        Assert.assertNotNull(ptEstimationSavedRow, "Saved PT_ESTIMATION records should not be null");
        Assert.assertEquals(ptEstimationSavedRow.getPtRespAmtAsMoney().toDisplayString(), newExpectedPtEstimatedResponseList.get(9), "Updated PT_ESTIMATION records should contain the new amount");

        String newEstimationId = ptEstimation.estimationId().getAttribute("value");
        logger.info("Comparing old estimation ID to new one, newEstimationId=" + newEstimationId + ", originalEstimationId=" + estimationInfo);
        Assert.assertTrue(StringUtils.isNotBlank(newEstimationId), "Expected to find new Estimation ID");
        Assert.assertNotEquals(estimationInfo, newEstimationId, "Expected different sequence after responsibility check");

        // Reset Page
        logger.info("Click on Reset button");
        Assert.assertTrue(ptEstimation.resetButton().isDisplayed());
        ptEstimation.resetButton().click();
    }

    @Test(priority = 1, description = "Remaining Deductible equal 0 is applied, negative deductible is not applied")
    @Parameters({"patientInfo", "insuranceInfo", "orderInfo", "ptEstimatedResponseDetail"})
    public void testZeroDollarDeductibleIsApplied(String patientInfo, String insuranceInfo, String orderInfo, String ptEstimatedResponseDetail) throws Exception {
        logger.info("message=Testing zero-dollar Deductible is applied, and negative Deductible is not applied");
        PtEstimationConfig ptEstimation = new PtEstimationConfig(driver, wait, methodName);
        PtEstimationDomain ptEstimationDomain = new PtEstimationDomain();
        enterPatientEstimationFields(ptEstimationDomain, ptEstimation, StringUtils.EMPTY, patientInfo, insuranceInfo, orderInfo, RELSHP_TYP_SELF, null);

        logger.info("Create expected Estimated Patient Responsibility Information");
        List<String> expectedPtEstimatedResponseList = Arrays.asList(org.apache.commons.lang3.StringUtils.split(ptEstimatedResponseDetail));

        List<String> actualPtEstimatedResponseList = getActualPtEstimationResponseAsList(ptEstimation);
        logger.info("Compare Expected and Actual Patient Estimated Responsibilities, expectedPtEstimatedResponseList="+ expectedPtEstimatedResponseList+", actualPtEstimatedResponseList="+actualPtEstimatedResponseList);
        Assert.assertEquals(actualPtEstimatedResponseList, expectedPtEstimatedResponseList);

        // Save Page
        logger.info("Click on Save button");
        Assert.assertTrue(ptEstimation.saveButton().isDisplayed());
        ptEstimation.saveButton().click();

        PtEstimation ptEstimationSavedRow = accessionDao.getPtEstimation(Integer.parseInt(ptEstimation.getEstimationId().getAttribute("value")));
        Assert.assertNotNull(ptEstimationSavedRow, "Saved PT_ESTIMATION records should not be null");
        Assert.assertEquals(ptEstimationSavedRow.getPtRespAmtAsMoney().toDisplayString(), expectedPtEstimatedResponseList.get(9), "Patient Responsibility Amount does not match expected");
        List<PtEstimationProc> ptEstimationProcs = accessionDao.getPtEstimationProcsByPtEstimationId(Collections.singletonList(ptEstimationSavedRow.getSeqId()));
        Assert.assertFalse(ptEstimationProcs.isEmpty(), "Patient Responsibility Proc count should be >= 0");
        for (PtEstimationProc ptEstimationProc : ptEstimationProcs)
        {
            logger.info("message=Verifying $0 applied deductible for proc, procCd="+ptEstimationProc.getProcCd());
            List<PtEstimationBenefit> ptEstimationDeductibles = accessionDao.getPtEstimationBenefitByPtEstimationProcId(Collections.singletonList(ptEstimationProc.getSeqId()))
                    .stream().filter(f->f.getBenefitTypId()== MiscMap.PT_ESTIMATION_BENEFIT_TYP_DEDUCTIBLE).collect(Collectors.toList());
            Assert.assertEquals(ptEstimationDeductibles.size(), 1, "Patient Responsibility Proc should have 1 Deductible record");
            Assert.assertEquals(ptEstimationDeductibles.get(0).getTimePeriod(), "Remaining", "Patient Responsibility Proc Deductible Time Period should be Remaining");
            Assert.assertEquals(ptEstimationDeductibles.get(0).getAppliedAmtAsMoney(), new Money(0), "Patient Responsibility Proc Deductible Remaining amount should be $0");
            Assert.assertTrue(ptEstimationDeductibles.get(0).getIsApplied(), "Patient Responsibility Proc Deductible should be marked as Applied");
        }
        // Reset Page
        logger.info("Click on Reset button");
        Assert.assertTrue(ptEstimation.resetButton().isDisplayed());
        ptEstimation.resetButton().click();
    }

    @Test(priority = 1, description = "Service Type Selection rules are applied")
    @Parameters({"patientData", "insuranceData", "orderData", "patientResponsibilityData"})
    public void testServiceTypeSelectionRules(String patientData, String insuranceData, String orderData, String patientResponsibilityData) throws Exception
    {
        logger.info("message=Testing Service Type Selection rules are applied");
        PatientEstimation patientEstimation = new PatientEstimation(driver);
        patientEstimation.setPatientInformation(parsePatientInformation(patientData));
        patientEstimation.setInsuranceInformation(parseInsuranceInformation(insuranceData));
        patientEstimation.setOrderInformation(parseOrderInformation(orderData));
        PatientResponsibility expectedPatientResponsibility = parsePatientResponsibility(patientResponsibilityData);
        PtEstimationConfig ptEstimationConfig = new PtEstimationConfig(driver, wait, methodName);
        performPatientEstimation(ptEstimationConfig, patientEstimation);
        PatientResponsibility actualPatientResponsibility = extractPatientResponsibility(ptEstimationConfig);
        ptEstimationConfig.resetButton().click();
        Assert.assertEquals(actualPatientResponsibility, expectedPatientResponsibility, "Actual Patient Responsibility does not match Expected");
    }

    private PatientInformation parsePatientInformation(String patientData)
    {
        if (StringUtils.isBlank(patientData))
        {
            return null;
        }
        PatientInformation patientInformation = new PatientInformation();
        String[] parts = StringUtils.split(patientData, SEPARATOR);
        patientInformation.setFirstName(parts.length > 0 ? parts[0] : EMPTY);
        patientInformation.setLastName(parts.length > 1 ? parts[1] : EMPTY);
        patientInformation.setDob(parts.length > 2 ? parts[2] : EMPTY);
        patientInformation.setGender(parts.length > 3 ? parts[3] : EMPTY);
        return patientInformation;
    }

    private InsuranceInformation parseInsuranceInformation(String insuranceData) throws XifinDataNotFoundException, XifinDataAccessException
    {
        if (StringUtils.isBlank(insuranceData))
        {
            return null;
        }
        InsuranceInformation insuranceInformation = new InsuranceInformation();
        String[] parts = StringUtils.split(insuranceData, SEPARATOR);
        insuranceInformation.setPayorId(parts.length > 0 ? parts[0] : EMPTY);
        if (StringUtils.isNotBlank(insuranceInformation.getPayorId()))
        {
            insuranceInformation.setPayorName(payorDao.getPyrByPyrAbbrv(insuranceInformation.getPayorId()).getName());
        }
        insuranceInformation.setSubscriberId(parts.length > 1 ? parts[1] : EMPTY);
        insuranceInformation.setRelationship(parts.length > 2 ? parts[2] : EMPTY);
        insuranceInformation.setFirstName(parts.length > 3 ? parts[3] : EMPTY);
        insuranceInformation.setLastName(parts.length > 4 ? parts[4] : EMPTY);
        insuranceInformation.setDob(parts.length > 5 ? parts[5] : EMPTY);
        insuranceInformation.setGender(parts.length > 6 ? parts[6] : EMPTY);
        return insuranceInformation;
    }

    private OrderInformation parseOrderInformation(String orderData)
    {
        if (StringUtils.isBlank(orderData))
        {
            return null;
        }
        OrderInformation orderInformation = new OrderInformation();
        String[] parts = StringUtils.split(orderData, SEPARATOR);
        orderInformation.setOrderId(parts.length > 0 ? parts[0] : EMPTY);
        orderInformation.setClientId(parts.length > 1 ? parts[1] : EMPTY);
        orderInformation.setDos(parts.length > 2 ? parts[2] : EMPTY);
        if (parts.length > 3 && StringUtils.isNotBlank(parts[3]))
        {
            orderInformation.getTestIds().addAll(List.of(StringUtils.split(parts[3], TEST_SEPARATOR)));

        }
        return orderInformation;
    }

    private PatientResponsibility parsePatientResponsibility(String patientResponsibilityData)
    {
        if (StringUtils.isBlank(patientResponsibilityData))
        {
            return null;
        }
        PatientResponsibility patientResponsibility = new PatientResponsibility();
        Money totalPatientResponsibilityAmt = new Money(0);
        for (String detailData : StringUtils.split(patientResponsibilityData, SEPARATOR))
        {
            PatientResponsibilityDetail detail = new PatientResponsibilityDetail();
            String[] parts = StringUtils.split(StringUtils.trim(detailData), DETAIL_SEPARATOR);
            detail.setProcCode(parts.length > 0 ? parts[0] : EMPTY);
            detail.setUnits(parts.length > 1 ? parts[1] : EMPTY);
            detail.setBillAmt(parts.length > 2 ? parts[2] : EMPTY);
            detail.setExpectAmt(parts.length > 3 ? parts[3] : EMPTY);
            detail.setCopayAmt(parts.length > 4 ? parts[4] : EMPTY);
            detail.setCoinsPct(parts.length > 5 ? parts[5] : EMPTY);
            detail.setCoinsAmt(parts.length > 6 ? parts[6] : EMPTY);
            detail.setDedAppliedAmt(parts.length > 7 ? parts[7] : EMPTY);
            detail.setDedRemainingAmt(parts.length > 8 ? parts[8] : EMPTY);
            detail.setDedAnnualAmt(parts.length > 9 ? parts[9] : EMPTY);
            totalPatientResponsibilityAmt.add(new Money(detail.getCopayAmt()));
            totalPatientResponsibilityAmt.add(new Money(detail.getCoinsAmt()));
            totalPatientResponsibilityAmt.add(new Money(detail.getDedAppliedAmt()));
            patientResponsibility.getDetails().add(detail);
        }
        patientResponsibility.setEstimatedPatientResponsibility(totalPatientResponsibilityAmt.toString());
        return patientResponsibility;
    }

    private List<String> getActualPtEstimationResponseAsList(PtEstimationConfig ptEstimation)
    {
        logger.info("Get the actual Estimated Patient Responsibility Information from the Details table");
        List<String> actualPtEstimatedResponseList = new ArrayList<>();

        try {
            logger.info("Reading proc code ID");
            actualPtEstimatedResponseList.add(ptEstimation.ptEstimationDetailTableProcCd().getText());
        } catch (StaleElementReferenceException e) {
            logger.info("Got stale element reference. Trying again top read proc code ID");
            actualPtEstimatedResponseList.add(ptEstimation.ptEstimationDetailTableProcCd().getText());
        }
        logger.info("Patient Estimation, procCd=" + actualPtEstimatedResponseList.get(0));

        try
        {
            actualPtEstimatedResponseList.add(ptEstimation.ptEstimationDetailTableUnits().getText());
        }
        catch (StaleElementReferenceException e)
        {
            actualPtEstimatedResponseList.add(ptEstimation.ptEstimationDetailTableUnits().getText());
        }
        actualPtEstimatedResponseList.add(ptEstimation.ptEstimationDetailTableProcCdBillPrice().getText());
        actualPtEstimatedResponseList.add(ptEstimation.ptEstimationDetailTableCopay().getText());
        actualPtEstimatedResponseList.add(ptEstimation.ptEstimationDetailTableCoinsurance().getText());
        actualPtEstimatedResponseList.add(ptEstimation.ptEstimationDetailTableCoinsuranceRate().getText());
        actualPtEstimatedResponseList.add(ptEstimation.ptEstimationDetailTableDeductible().getText());
        actualPtEstimatedResponseList.add(ptEstimation.ptEstimationDetailTableDeductibleRemaining().getText());
        actualPtEstimatedResponseList.add(ptEstimation.ptEstimationDetailTableDeductibleAnnual().getText());
        String actualEstimatedPtRespAmount = ptEstimation.getEstimatedPatientResponsibilityAmount();
        actualPtEstimatedResponseList.add(actualEstimatedPtRespAmount);
        return actualPtEstimatedResponseList;
    }

    private void enterPatientEstimationFields(PtEstimationDomain ptEstimationDomain, PtEstimationConfig ptEstimation, String accnInfo, String patientInfo, String insuranceInfo, String orderInfo, String relationshipTyp, String estimationInfo) throws XifinDataAccessException, XifinDataNotFoundException
    {
        logger.info("Verify that the Patient Estimation page is displayed with correct page title");
        Assert.assertTrue(ptEstimation.patientEstimationPageTitle().isDisplayed());
        Assert.assertEquals(ptEstimation.patientEstimationPageTitle().getText(),"Patient Estimation","Patient Estimation screen should be displayed.");

        // Parse values
        String[] expectedAccnInfo = org.apache.commons.lang3.StringUtils.split(accnInfo);
        String accnId = expectedAccnInfo.length > 0 ? expectedAccnInfo[0] : EMPTY;
        String patientFirstName = EMPTY;
        String patientLastName = EMPTY;
        String patientDOB = EMPTY;
        String patientGender = EMPTY;
        String insuredFirstName = EMPTY;
        String insuredLastName = EMPTY;
        String insuredDOB = EMPTY;
        String insuredGender = EMPTY;
        String subId = EMPTY;
        String pyrId = EMPTY;
        String orderId = EMPTY;
        String clientId = EMPTY;
        String DOS = EMPTY;
        String testId = EMPTY;
        String pyrName = EMPTY;
        boolean skipPatientInfo = true;
        boolean skipInsuredInfo = true;
        boolean skipOrderInfo = true;

        if (StringUtils.isNotBlank(patientInfo))
        {
            skipPatientInfo = false;
            String[] expectedPatientInfo = org.apache.commons.lang3.StringUtils.split(patientInfo);
            patientFirstName = expectedPatientInfo.length > 0 ? expectedPatientInfo[0] : EMPTY;
            patientLastName = expectedPatientInfo.length > 1 ? expectedPatientInfo[1] : EMPTY;
            patientDOB = expectedPatientInfo.length > 2 ? expectedPatientInfo[2] : EMPTY;
            patientGender = expectedPatientInfo.length > 3 ? expectedPatientInfo[3] : EMPTY;
            logger.info("Created Patient test data, patientFirstName=" + patientFirstName + ", patientLastName=" + patientLastName + ", patientDOB=" + patientDOB + ", patientGender=" + patientGender);
        }
        if (StringUtils.isNotBlank(insuranceInfo))
        {
            skipInsuredInfo = false;
            String[] expectedInsuredInfo = org.apache.commons.lang3.StringUtils.split(insuranceInfo);
            subId = expectedInsuredInfo.length > 0 ? expectedInsuredInfo[0] : EMPTY;
            insuredFirstName = expectedInsuredInfo.length > 1 ? expectedInsuredInfo[1] : EMPTY;
            insuredLastName = expectedInsuredInfo.length > 2 ? expectedInsuredInfo[2] : EMPTY;
            insuredDOB = expectedInsuredInfo.length > 3 ? expectedInsuredInfo[3] : EMPTY;
            insuredGender = expectedInsuredInfo.length > 4 ? expectedInsuredInfo[4] : EMPTY;
            logger.info("Created Insured test data, subId=" + subId + ",insuredFirstName=" + insuredFirstName + ", insuredLastName=" + insuredLastName + ", insuredDOB=" + insuredDOB + ", insuredGender=" + insuredGender);
        }
        if (StringUtils.isNotBlank(orderInfo))
        {
            skipOrderInfo = false;
            String[] orderInfoData = org.apache.commons.lang3.StringUtils.split(orderInfo);
            pyrId = orderInfoData.length > 0 ? orderInfoData[0] : EMPTY;
            clientId = orderInfoData.length > 1 ? orderInfoData[1] : EMPTY;
            DOS = orderInfoData.length > 2 ? orderInfoData[2] : EMPTY;
            testId = orderInfoData.length > 3 ? orderInfoData[3] : EMPTY;
            orderId = orderInfoData.length > 4 ? orderInfoData[4] : EMPTY;
            Pyr pyr = rpmDao.getPyrByPyrAbbrv(null, pyrId);
            pyrName = pyr.getName();
            logger.info("Created Insured test data, pyrId="+pyrId+",clientId="+clientId+", DOS="+DOS+", testId="+testId);
        }

        // Enter values
        if (StringUtils.isNotBlank(estimationInfo))
        {
            ptEstimation.enterEstimationId(estimationInfo);
        }
        else
        {
            ptEstimation.enterCreateNewEstimation();
        }

        if (StringUtils.isNotBlank(accnId))
        {
            logger.info("Enter accession information");
            ptEstimationDomain.setAccnId(accnId);
            ptEstimation.enterAccnInfoDataForPatientEstimation(ptEstimationDomain);
        }
        if (!skipPatientInfo)
        {
            logger.info("Enter Patient Information");
            fillPatientInformationPtEstimationDomain(ptEstimationDomain, patientFirstName, patientLastName, patientDOB, patientGender, insuredFirstName, insuredLastName, insuredDOB, insuredGender);
            ptEstimation.enterPatientInfoDataForPatientEstimation(ptEstimationDomain, relationshipTyp);
        }
        if (!skipInsuredInfo)
        {
            logger.info("Enter Subscriber Id");
            ptEstimation.setSubscriberIdInput(subId);

            logger.info("Set Relationship to " + relationshipTyp);
            ptEstimation.setRelationshipDropDown(StringUtils.lowerCase(relationshipTyp));

            logger.info("Verify the Insured information is populated with the same First, Last name, DOB and gender");
            logger.info(ptEstimation.insuredFirstNameInput().getAttribute("value"));
            logger.info(ptEstimation.insuredLastNameInput().getAttribute("value"));
            logger.info(ptEstimation.insuredDateOfBirthInput().getAttribute("value"));
            logger.info(ptEstimation.insuredGenderDropDown().getAttribute("value"));
            if (StringUtils.equalsIgnoreCase(relationshipTyp, RELSHP_TYP_SELF))
            {
                Assert.assertTrue(ptEstimation.insuredFirstNameInput().getAttribute("value").equals(insuredFirstName) && ptEstimation.insuredFirstNameInput().getAttribute("value").equals(patientFirstName));
                Assert.assertTrue(ptEstimation.insuredLastNameInput().getAttribute("value").equals(insuredLastName) && ptEstimation.insuredLastNameInput().getAttribute("value").equals(patientLastName));
                Assert.assertTrue(ptEstimation.insuredDateOfBirthInput().getAttribute("value").equals(insuredDOB) && ptEstimation.insuredDateOfBirthInput().getAttribute("value").equals(patientDOB));
            }
        }
        if (!skipOrderInfo)
        {
            logger.info("Enter Order Information");
            fillOrderInformationPtEstimationDomain(ptEstimationDomain, pyrId, orderId, clientId, DOS, testId, pyrName);
            ptEstimation.enterOrderInfoDataForPatientEstimation(ptEstimationDomain);
        }

        // Finished entering values, estimate responsibility
        wait.until(ExpectedConditions.elementToBeClickable(ptEstimation.estimatePtResponsibilityButton()));
        logger.info("Click on Estimate Patient Responsibility button");
        ptEstimation.estimatePtResponsibilityButton().click();
        ptEstimation.ptRespExpandGridButton().click();
        wait.until(ExpectedConditions.visibilityOf(ptEstimation.ptEstimationDetailTable()));
    }

    private void performPatientEstimation(PtEstimationConfig ptEstimationConfig, PatientEstimation patientEstimation)
    {
        logger.info("Verify that the Patient Estimation page is displayed with correct page title");
        Assert.assertTrue(ptEstimationConfig.patientEstimationPageTitle().isDisplayed());
        Assert.assertEquals(ptEstimationConfig.patientEstimationPageTitle().getText(),"Patient Estimation","Patient Estimation screen should be displayed.");

        if (StringUtils.isNotBlank(patientEstimation.getEstimationId()))
        {
            ptEstimationConfig.enterEstimationId(patientEstimation.getEstimationId());
        }
        else
        {
            ptEstimationConfig.enterCreateNewEstimation();
        }

        if (patientEstimation.getAccnId() != null)
        {
            ptEstimationConfig.enterAccessionId(patientEstimation.getAccnId());
            Assert.assertEquals(ptEstimationConfig.accnInput().getAttribute("value"), patientEstimation.getAccnId());
        }

        if (patientEstimation.getPatientInformation() != null)
        {
            ptEstimationConfig.enterPatientInformation(patientEstimation.getPatientInformation());
            Assert.assertEquals(ptEstimationConfig.ptFirstNameInput().getAttribute("value"), patientEstimation.getPatientInformation().getFirstName());
            Assert.assertEquals(ptEstimationConfig.ptLastNameInput().getAttribute("value"), patientEstimation.getPatientInformation().getLastName());
            Assert.assertEquals(ptEstimationConfig.ptDateOfBirthInput().getAttribute("value"), patientEstimation.getPatientInformation().getDob());
            Assert.assertEquals(ptEstimationConfig.getSelectedPtGenderText(), patientEstimation.getPatientInformation().getGender());
        }

        if (patientEstimation.getInsuranceInformation() != null)
        {
            ptEstimationConfig.enterInsuranceInformation(patientEstimation.getInsuranceInformation());
            Assert.assertTrue(ptEstimationConfig.getSelectedPayorIdText().startsWith(patientEstimation.getInsuranceInformation().getPayorId()));
            Assert.assertEquals(ptEstimationConfig.subscriberIdInput().getAttribute("value"), patientEstimation.getInsuranceInformation().getSubscriberId());
            Assert.assertEquals(ptEstimationConfig.getSelectedRelationshipText(), patientEstimation.getInsuranceInformation().getRelationship());
            Assert.assertEquals(ptEstimationConfig.insuredFirstNameInput().getAttribute("value"), patientEstimation.getInsuranceInformation().getFirstName());
            Assert.assertEquals(ptEstimationConfig.insuredLastNameInput().getAttribute("value"), patientEstimation.getInsuranceInformation().getLastName());
            Assert.assertEquals(ptEstimationConfig.insuredDateOfBirthInput().getAttribute("value"), patientEstimation.getInsuranceInformation().getDob());
            Assert.assertEquals(ptEstimationConfig.getSelectedInsuredGenderText(), patientEstimation.getInsuranceInformation().getGender());
        }

        if (patientEstimation.getOrderInformation() != null)
        {
            ptEstimationConfig.enterOrderInformation(patientEstimation.getOrderInformation());
        }

        ptEstimationConfig.clickEstimatePtResponsibilityButton();
    }

    private PatientResponsibility extractPatientResponsibility(PtEstimationConfig ptEstimationConfig)
    {
        PatientResponsibility patientResponsibility = new PatientResponsibility();
        patientResponsibility.setEstimatedPatientResponsibility(ptEstimationConfig.getEstimatedPatientResponsibilityAmount());
        int detailRowCount = ptEstimationConfig.getPatientResponsibilityDetailRowCount();
        for (int i = 0; i < detailRowCount; i++)
        {
            PatientResponsibilityDetail detail = new PatientResponsibilityDetail();
            detail.setProcCode(ptEstimationConfig.getPatientResponsibilityDetailProcCd(i));
            detail.setUnits(ptEstimationConfig.getPatientResponsibilityDetailUnits(i));
            detail.setBillAmt(ptEstimationConfig.getPatientResponsibilityDetailBillPrice(i));
            detail.setExpectAmt(ptEstimationConfig.getPatientResponsibilityDetailExpectPrice(i));
            detail.setCopayAmt(ptEstimationConfig.getPatientResponsibilityDetailCopayAmt(i));
            detail.setCoinsPct(ptEstimationConfig.getPatientResponsibilityDetailCoinsPct(i));
            detail.setCoinsAmt(ptEstimationConfig.getPatientResponsibilityDetailCoinsAmt(i));
            detail.setDedAppliedAmt(ptEstimationConfig.getPatientResponsibilityDetailDeductible(i));
            detail.setDedRemainingAmt(ptEstimationConfig.getPatientResponsibilityDetailRemainingDeductible(i));
            detail.setDedAnnualAmt(ptEstimationConfig.getPatientResponsibilityDetailAnnualDeductible(i));
            patientResponsibility.getDetails().add(detail);
        }
        return patientResponsibility;
    }

    public void enterPatientResponsibilityInfo(PtEstimationDomain ptEstimationDomain, String acceptResponsibility, String note, PtEstimationConfig ptEstimation)
    {
        fillRespInformationPtEstimationDomain(ptEstimationDomain, acceptResponsibility, note);
        ptEstimation.enterRespInfoDataForPatientEstimation(ptEstimationDomain);
    }

    public PtEstimationDomain fillPatientInformationPtEstimationDomain(PtEstimationDomain ptEstimationDomain, String ptFirstName, String ptLastName, String dateOfBirth, String gender, String insureFirstName, String insuredLastName, String insuredDOB, String insuredGender) {
        ptEstimationDomain.setPtFirstName(ptFirstName);
        ptEstimationDomain.setPtLastName(ptLastName);
        ptEstimationDomain.setPtDateOfBirth(dateOfBirth);
        ptEstimationDomain.setPtGender(gender);
        ptEstimationDomain.setInsuredFirstName(insureFirstName);
        ptEstimationDomain.setInsuredLastName(insuredLastName);
        ptEstimationDomain.setInsuredDateOfBirth(insuredDOB);
        ptEstimationDomain.setInsuredGender(insuredGender);
        return ptEstimationDomain;
    }

    public PtEstimationDomain fillInsuredInformationPtEstimationDomain(PtEstimationDomain ptEstimationDomain, String insuredFirstName, String insuredLastName, String dateOfBirth, String gender, String relationship, String subscriberId) {
        if(relationship.equalsIgnoreCase(RELSHP_TYP_SELF)){
            ptEstimationDomain.setRelationship(relationship);
            ptEstimationDomain.setSubscriberId(subscriberId);
        }
        else
        {
            ptEstimationDomain.setInsuredFirstName(insuredFirstName);
            ptEstimationDomain.setInsuredLastName(insuredLastName);
            ptEstimationDomain.setInsuredDateOfBirth(dateOfBirth);
            ptEstimationDomain.setInsuredGender(gender);
            ptEstimationDomain.setRelationship(relationship);
            ptEstimationDomain.setSubscriberId(subscriberId);
        }
        return ptEstimationDomain;
    }
    public PtEstimationDomain fillOrderInformationPtEstimationDomain(PtEstimationDomain ptEstimationDomain, String payorId, String orderId, String clientId, String dateOfService, String testId, String payorName) {
        ptEstimationDomain.setPayorId(payorId);
        ptEstimationDomain.setPayorName(payorName);
        ptEstimationDomain.setOrderId(orderId);
        ptEstimationDomain.setClientId(clientId);
        ptEstimationDomain.setDateOfService(dateOfService);
        ptEstimationDomain.setTestId(testId);

        return ptEstimationDomain;
    }
    public PtEstimationDomain fillRespInformationPtEstimationDomain(PtEstimationDomain ptEstimationDomain, String acceptResponsibility, String note)
    {
        Boolean isResp = Boolean.parseBoolean(acceptResponsibility);
        ptEstimationDomain.setIsResp(isResp);
        ptEstimationDomain.setNote(note);
        return ptEstimationDomain;
    }

    public void switchToParentWindow(String currentWindow) {
        driver.switchTo().window(currentWindow);
    }
}
