package com.newXp.tests;

import com.pfEngines.tests.EligibilityBaseTest;
import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnEligHist.AccnEligHist;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.accnPyrDel.AccnPyrDel;
import com.mbasys.mars.ejb.entity.accnQue.AccnQue;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrElig.PyrElig;
import com.mbasys.mars.eligibility.EligMap;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.accession.ErrorProcessing.EpSearchPlatform;
import com.overall.accession.ErrorProcessing.EpSearchResultsPlatform;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.accession.accessionProcessing.LoadAccession;
import com.overall.menu.MenuNavigation;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EligibilityEPSearchTest extends EligibilityBaseTest
{
    private static final Date TODAYS_DT = new Date(DateUtils.truncate(new java.util.Date(), Calendar.DATE).getTime());
    private MenuNavigation navigation;
    private AccessionDetail accessionDetail;
    private LoadAccession loadAccession;

    @BeforeSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running BeforeSuite");
            // Disable excess Selenium logging
            java.util.logging.Logger.getLogger("org.openqa.selenium.remote").setLevel(java.util.logging.Level.OFF);
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "False", "0");
            new XifinAdminUtils(driver, config).clearDataCache();
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

    @AfterSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
    public void afterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running AfterSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
        }
        catch (Exception e)
        {
            Assert.fail("Error running AfterSuite", e);
        }
        finally
        {
            quitWebDriver();
        }
    }
    @BeforeMethod(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins",})
    public void beforeMethod(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, String disableBrowserPlugins, Method method)
    {
        try
        {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            logIntoSso(ssoUsername, ssoPassword);
            navigation = new MenuNavigation(driver, config);
            navigation.navigateToAccnDetailPage();
        }
        catch (Exception e)
        {
            logger.error("Error running BeforeMethod", e);
        }
    }

    @Test(priority = 1, description = "No Transl Rule, SubId update setup - Rejected response - SubId is updated ")
    @Parameters({"accnId", "origPyrAbbrv", "jurisdPyr", "origSubId", "subIdFrom271"})
    public void testXPR_1896(String accnId, String origPyrAbbrv, String jurisdPyr, String origSubId, String subIdFrom271) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        navigation = new MenuNavigation(driver, config);
        logger.info("Starting Test Case: XPR-1896, accnId=" + accnId + ", origPyrAbbrv=" + origPyrAbbrv);
        logger.info("Verifying payor is configured for Eligibility and Patient and Insured Demo data updates, accnId=" + accnId + ", pyrAbbrv=" + origPyrAbbrv);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        PyrElig pyrElig = null;
        for (PyrElig pe : rpmDao.getPyrEligByPyrIdEligSvcId(testDb, pyr.getPyrId(), EligMap.XIFIN))
        {
            if (!pe.getIsDeleted() && pe.getIsPreferred())
            {
                pyrElig = pe;
            }
        }
        Assert.assertNotNull(pyrElig);

        logger.info("Verifying that Subscriber Id update is enabled");
        Assert.assertTrue(pyrElig.getIsUpdateInsSubsId());

        logger.info("Verifying There is no Payor Translation Rule exists");
        Assert.assertEquals(rpmDao.getEligRespByPyrId(testDb, pyr.getPyrId()).size(),0);

        logger.info("Verifying that accn has Jurisdiction Pyr="+jurisdPyr);
        verifyAccnEligDataIsCleared(accessionDao.getAccnEligHistByAccnId(accnId), accessionDao.getAccnPyrs(accnId));
        Assert.assertTrue(rpmDao.getAccnJurisdictionPyrs(testDb, accnId).size()>0);

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        logger.info("Make sure original subscriber Id is different from 271 file subscriber Id");
        if (!StringUtils.equals(accessionDetail.primaryPyrSubsIdInput().getAttribute("value"), origSubId))
        {
            accessionDetail.clearPrimaryPyrSubsId();
            accessionDetail.setPrimaryPyrSubsId(origSubId);
            accessionDetail.clickSave();
            //accessionDetail.clickOKBtnInPtDemoUpdate();
        }

        verifyAccnIsInEPHold(accnId, accessionDetail);

        navigation.navigateToEPSearchPage();
      //  switchToDefaultWinFromFrame();
        performEligibilityCheckOnEPSearch(accnId);


        List<AccnEligHist>  accnEligHists = verifyAccnEligHistoryRecord(accnId, pyr.getPyrId(), null, EligMap.ELIG_STA_TYP_REJECTED);
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId+", eligibility status is Rejected="+accnEligHists.get(0).getEligStaTypId());
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), pyr.getPyrId());
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_REJECTED);

        logger.info("Create accession payor record, accnId=" + accnId);
        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(newAccnPyrs.size(), 2);
        Assert.assertEquals(newAccnPyrs.get(1).getPyrId(), rpmDao.getPyrByPyrAbbrv(testDb, jurisdPyr).getPyrId());

        logger.info("Make sure pyr is not updated = newPyr - " + newAccnPyrs.get(0).getPyrAbbrv());
        Assert.assertEquals(newAccnPyrs.get(0).getPyrId(), pyr.getPyrId());

        logger.info("Verifying subscriber ID is updated for primary and jurisdiction Pyr, subId=" + newAccnPyrs.get(0).getSubsId());
        for(AccnPyr accnPyr : newAccnPyrs)
        {
            logger.info("Verifying subscriber ID is updated for Pyr ="+accnPyr.getPyrId()+", subId="+accnPyr.getSubsId());
            Assert.assertNotEquals(accnPyr.getSubsId(), origSubId);
            Assert.assertEquals(accnPyr.getSubsId(), subIdFrom271);
        }
        //  TODO Issue on EP SEARCH
        Assert.assertEquals(newAccnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_REJECTED);
        Assert.assertEquals(newAccnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
        Assert.assertNotNull(newAccnPyrs.get(0).getEligChkDt());


    }

    @Test(priority = 1, description = "Eligibility check is ELIGIBLE, Patient and Insured Demographic Data is updated - mixed case")
    @Parameters({"accnId", "origPyrAbbrv", "patientFNm", "patientLNm", "patientAddr1", "patientAddr2", "patientSex", "patientCity", "patientZipId", "subscriberId", "relshpId", "insuredFNm", "insuredLNm", "insuredAddr1", "insuredAddr2", "insuredSex", "insuredCity", "insuredZipId"})
    public void testXPR_1897(String accnId, String origPyrAbbrv, String patientFNm, String patientLNm, String patientAddr1, String patientAddr2, int patientSex, String patientCity, String patientZipId, String subscriberId, String relshpId, String insuredFNm, String insuredLNm, String insuredAddr1, String insuredAddr2, int insuredSex, String insuredCity, String insuredZipId) throws Exception
    {
        navigation = new MenuNavigation(driver, config);
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: XPR-1897, accnId=" + accnId + ", origPyrAbbrv=" + origPyrAbbrv);
        logger.info("Verifying payor is configured for Eligibility and Patient and Insured Demo data updates, accnId=" + accnId + ", pyrAbbrv=" + origPyrAbbrv);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        PyrElig pyrElig = null;
        for (PyrElig pe : rpmDao.getPyrEligByPyrIdEligSvcId(testDb, pyr.getPyrId(), EligMap.XIFIN))
        {
            if (!pe.getIsDeleted() && pe.getIsPreferred())
            {
                pyrElig = pe;
            }
        }
        Assert.assertNotNull(pyrElig);
        // Insured and Patient demographic updates should be enabled
        Assert.assertFalse(pyrElig.getIsUpdateInsSubsId());
        Assert.assertTrue(pyrElig.getIsUpdateInsNm());
        Assert.assertTrue(pyrElig.getIsUpdateInsAddr());
        Assert.assertTrue(pyrElig.getIsUpdateInsGender());
        Assert.assertFalse(pyrElig.getIsUpdateInsRelshp());
        Assert.assertTrue(pyrElig.getIsUpdatePtNm());
        Assert.assertTrue(pyrElig.getIsUpdatePtAddr());
        Assert.assertTrue(pyrElig.getIsUpdatePtGender());

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        logger.info("Update Patient and Insured Name, Address and Gender");

        updatePatientDemographics("testAddressLine1", "testAddressLine2", "testPatientFirstName", "testPatientLastName", "0", "92130", "Los Angeles");
        updateInsuredDemographics("InsuredAddressLine1", "InsuredAddressLine2", "InsuredFirstName", "testInsuredLastName", "0", "90009", "San Diego");
        accessionDetail.saveAndClear(wait);

        verifyAccnIsInEPHold(accnId, accessionDetail);

//        logger.info("Save Accn Demographic Data before the eligibility Check");
//        Accn origAccn = accessionDao.getAccn(accnId);
//        List<AccnPyr> origAccnPyrs = accessionDao.getAccnPyrs(accnId);

        //switchToDefaultWinFromFrame();

        navigation.navigateToEPSearchPage();
        performEligibilityCheckOnEPSearch(accnId);
        List<AccnEligHist>  accnEligHists = verifyAccnEligHistoryRecord(accnId, pyr.getPyrId(), null, EligMap.ELIG_STA_TYP_ELIGIBLE);

        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int newPyrId = newAccnPyrs.get(0).getPyrId();

        logger.info("Check Accn primary payor is not changed, original Pyr Id ="+pyr.getPyrId()+", new Pyr Id="+newPyrId);
        Assert.assertEquals(pyr.getPyrId(), newPyrId);
        //  TODO Issue on EP SEARCH
//        Assert.assertEquals(newAccnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);
//        Assert.assertEquals(newAccnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
//        Assert.assertNotNull(newAccnPyrs.get(0).getEligChkDt());
        Accn newAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying patient and insured demographic data is not updated, accnId=" + accnId);
        Assert.assertEquals(newAccn.getPtFNm(), patientFNm);
        Assert.assertEquals(newAccn.getPtLNm(), patientLNm);
        Assert.assertEquals(newAccn.getPtAddr1(), patientAddr1);
        Assert.assertEquals(newAccn.getPtAddr2(), patientAddr2);
        Assert.assertEquals(newAccn.getPtSex(), patientSex);
        Assert.assertEquals(newAccnPyrs.get(0).getSubsId(), subscriberId);
        Assert.assertEquals(newAccnPyrs.get(0).getInsFNm(), insuredFNm);
        Assert.assertEquals(newAccnPyrs.get(0).getInsLNm(), insuredLNm);
        Assert.assertEquals(newAccnPyrs.get(0).getInsAddr1(), insuredAddr1);
        Assert.assertEquals(newAccnPyrs.get(0).getInsAddr2(), insuredAddr2);
        Assert.assertEquals(newAccnPyrs.get(0).getInsSex(), insuredSex);
        Assert.assertEquals(newAccnPyrs.get(0).getRelshpId(), Integer.parseInt(relshpId));
    }

    @Test(priority = 1, description = "Eligibility check is ELIGIBLE, Patient and Insured Demographic Data is Not updated - mixed case")
    @Parameters({"accnId", "origPyrAbbrv", "patientFNm", "patientLNm", "patientAddr1", "patientAddr2", "patientSex", "patientCity", "patientZipId", "subscriberId", "relshpId", "insuredFNm", "insuredLNm", "insuredAddr1", "insuredAddr2", "insuredSex", "insuredCity", "insuredZipId"})
    public void testXPR_1898(String accnId, String origPyrAbbrv, String patientFNm, String patientLNm, String patientAddr1, String patientAddr2, int patientSex, String patientCity, String patientZipId, String subscriberId, String relshpId, String insuredFNm, String insuredLNm, String insuredAddr1, String insuredAddr2, int insuredSex, String insuredCity, String insuredZipId) throws Exception
    {
        navigation = new MenuNavigation(driver, config);
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: XPR-1898, accnId=" + accnId + ", origPyrAbbrv=" + origPyrAbbrv);
        logger.info("Verifying payor is configured for Eligibility and Patient and Insured Demo data updates, accnId=" + accnId + ", pyrAbbrv=" + origPyrAbbrv);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        PyrElig pyrElig = null;
        for (PyrElig pe : rpmDao.getPyrEligByPyrIdEligSvcId(testDb, pyr.getPyrId(), EligMap.XIFIN))
        {
            if (!pe.getIsDeleted() && pe.getIsPreferred())
            {
                pyrElig = pe;
            }
        }
        Assert.assertNotNull(pyrElig);
        // Insured and Patient demographic updates should be enabled
        Assert.assertFalse(pyrElig.getIsUpdateInsSubsId());
        Assert.assertFalse(pyrElig.getIsUpdateInsNm());
        Assert.assertFalse(pyrElig.getIsUpdateInsAddr());
        Assert.assertFalse(pyrElig.getIsUpdateInsGender());
        Assert.assertFalse(pyrElig.getIsUpdateInsRelshp());
        Assert.assertFalse(pyrElig.getIsUpdatePtNm());
        Assert.assertFalse(pyrElig.getIsUpdatePtAddr());
        Assert.assertFalse(pyrElig.getIsUpdatePtGender());

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        logger.info("Update Patient and Insured Name, Address and Gender");

        updatePatientDemographics("testAddressLine1", "testAddressLine2", "testPatientFirstName", "testPatientLastName", "0", "92130", "Los Angeles");
        updateInsuredDemographics("InsuredAddressLine1", "InsuredAddressLine2", "InsuredFirstName", "testInsuredLastName", "0", "90009", "San Diego");
        accessionDetail.saveAndClear(wait);

//        AccnQue accnQue = rpmDao.getAccnQueByAccnId(testDb, accnId);
        verifyAccnIsInEPHold(accnId, accessionDetail);

        logger.info("Save Accn Demographic Data before the eligibility Check");
        Accn origAccn = accessionDao.getAccn(accnId);
        List<AccnPyr> origAccnPyrs = accessionDao.getAccnPyrs(accnId);

       // switchToDefaultWinFromFrame();

        navigation.navigateToEPSearchPage();
        performEligibilityCheckOnEPSearch(accnId);
        List<AccnEligHist>  accnEligHists = verifyAccnEligHistoryRecord(accnId, pyr.getPyrId(), null, EligMap.ELIG_STA_TYP_ELIGIBLE);

        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int newPyrId = newAccnPyrs.get(0).getPyrId();

        logger.info("Check Accn primary payor is not changed, original Pyr Id ="+pyr.getPyrId()+", new Pyr Id="+newPyrId);
        Assert.assertEquals(pyr.getPyrId(), newPyrId);

      //  TODO Issue on EP SEARCH
//        Assert.assertEquals(newAccnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);
//        Assert.assertEquals(newAccnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
//        Assert.assertNotNull(newAccnPyrs.get(0).getEligChkDt());
        Accn newAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying patient and insured demographic data is not updated, accnId=" + accnId);
        Assert.assertEquals(origAccn.getPtFNm(), newAccn.getPtFNm());
        Assert.assertEquals(origAccn.getPtLNm(), newAccn.getPtLNm());
        Assert.assertEquals(origAccn.getPtAddr1(), newAccn.getPtAddr1());
        Assert.assertEquals(origAccn.getPtAddr2(), newAccn.getPtAddr2());
        Assert.assertEquals(origAccn.getPtSex(), newAccn.getPtSex());
        Assert.assertEquals(origAccnPyrs.get(0).getSubsId(), newAccnPyrs.get(0).getSubsId());
        Assert.assertEquals(origAccnPyrs.get(0).getInsFNm(), newAccnPyrs.get(0).getInsFNm());
        Assert.assertEquals(origAccnPyrs.get(0).getInsLNm(), newAccnPyrs.get(0).getInsLNm());
        Assert.assertEquals(origAccnPyrs.get(0).getInsAddr1(), newAccnPyrs.get(0).getInsAddr1());
        Assert.assertEquals(origAccnPyrs.get(0).getInsAddr2(), newAccnPyrs.get(0).getInsAddr2());
        Assert.assertEquals(origAccnPyrs.get(0).getInsSex(), newAccnPyrs.get(0).getInsSex());
        Assert.assertEquals(origAccnPyrs.get(0).getRelshpId(), newAccnPyrs.get(0).getRelshpId());


    }

    @Test(priority = 1, description = "Eligibility check is ELIGIBLE, Primary Pyr is translated,Pmnts and Adjs are persisted in accn_pyr_del")
    @Parameters({"accnId", "origPyrAbbrv", "origSubId", "origPyrPayments", "origPyrAdjustments"})
    public void testXPR_1900(String accnId, String origPyrAbbrv, String origSubId, String origPyrPayments, String origPyrAdjustments) throws Exception
    {
        navigation = new MenuNavigation(driver, config);
        loadAccession = new LoadAccession(driver);
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: XPR-1900, accnId=" + accnId + ", origPyrAbbrv=" + origPyrAbbrv + ", subId=" + origSubId);
        logger.info("Load Accn on Accession Detail jsp, accnId=" + accnId);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        List<AccnPyr> currentAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int currentPyrId = currentAccnPyrs.get(0).getPyrId();
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        int origPyrId = origPyr.getPyrId();
        logger.info("Make sure primary pyrAbbrev=" + origPyrAbbrv);
        if (currentPyrId != origPyrId)
        {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + currentPyrId + ", newPyrId=" + origPyrId);
            setOriginalPyrToAccn(accessionDetail, accnId, origPyrAbbrv);
            loadAccession.setAccnId(accnId, wait);
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }
        logger.info("Make sure accn is in EP Hold queue");
        verifyAccnIsInEPHold(accnId, accessionDetail);
      //  switchToDefaultWinFromFrame();

        navigation.navigateToEPSearchPage();
        performEligibilityCheckOnEPSearch(accnId);

        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);

        int newPyrId = newAccnPyrs.get(0).getPyrId();
        logger.info("Check Accn primary payor is changed, origPyrId="+origPyrId);
        logger.info("Check Accn primary payor is changed, new Pyr="+newPyrId);
        Assert.assertNotEquals(origPyrId, newPyrId);

        logger.info("Check Accn_pyr_del record is created with payment and adjustment for the original pyr");
        AccnPyrDel accnPyrDel = rpmDao.getAccnPyrDelByAccnAndPyrId(testDb, accnId, origPyrId);
        logger.info("AccnPyrDel record is created = " + accnPyrDel);

        Assert.assertEquals(accnPyrDel.getSubId(), origSubId);
        Assert.assertEquals(accnPyrDel.getPmts(), origPyrPayments);
        Assert.assertEquals(accnPyrDel.getAdjs(), origPyrAdjustments);
        Assert.assertEquals(accnPyrDel.getPyrPrio(), 1);
        List<AccnEligHist>  accnEligHists = verifyAccnEligHistoryRecord(accnId, origPyrId, newPyrId, EligMap.ELIG_STA_TYP_ELIGIBLE);


    }
    @Test(priority = 1, description = "Default Svc Type code is used if Additional svc_type_code is not in 271 file")
    @Parameters({"accnId", "pyrAbbrv", "additionalSvcTyps", "defaultSvcTypCd"})
    public void testXPR_1899(String accnId, String pyrAbbrv, String additionalSvcTyps, String defaultSvcTypCd) throws Exception
    {
        navigation = new MenuNavigation(driver, config);
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: XPR-1899, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);

        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        int origPyrId = origPyr.getPyrId();
        List<PyrElig> pyrElig = rpmDao.getPyrEligByPyrIdEligSvcId(testDb, origPyrId,  EligMap.XIFIN);
        Assert.assertNotNull(pyrElig);
        Assert.assertEquals(pyrElig.size(), 1);
        Assert.assertEquals(pyrElig.get(0).getAddtlSvcTyps(),additionalSvcTyps);
        Assert.assertEquals(pyrElig.get(0).getDefaultSvcTypCd(), defaultSvcTypCd);
        logger.info("There are default and additional service type codes in pyr_elig for pyrAbbrv = " + pyrAbbrv+",Additional Svc Typs()"+pyrElig.get(0).getAddtlSvcTyps()+",Default Svc Typ "+pyrElig.get(0).getDefaultSvcTypCd());

        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        verifyAccnEligDataIsCleared(accessionDao.getAccnEligHistByAccnId(accnId), accnPyrs);

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyr.getName());

        verifyAccnIsInEPHold(accnId, accessionDetail);

      //  switchToDefaultWinFromFrame();

        navigation.navigateToEPSearchPage();
        performEligibilityCheckOnEPSearch(accnId);

        List<AccnEligHist>  accnEligHists = verifyAccnEligHistoryRecord(accnId, origPyrId, null, EligMap.ELIG_STA_TYP_ELIGIBLE);

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        //TODO issue on EP Search
//        Assert.assertEquals(accnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);
//        Assert.assertEquals(accnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
//        Assert.assertNotNull(accnPyrs.get(0).getEligChkDt());

        verifyEligTransLog(accnEligHists.get(0), EligMap.ELIG_STA_TYP_ELIGIBLE, defaultSvcTypCd);
    }

    private List<AccnEligHist> verifyAccnEligHistoryRecord(String accnId, int origPyrId, Integer translatedPyr, int expectedEligStatus) throws XifinDataAccessException
    {
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId+", eligibility status="+accnEligHists.get(0).getEligStaTypId()+
                ", pyr id="+accnEligHists.get(0).getPyrId()+", translated pyr id="+accnEligHists.get(0).getTranslatedPyrId()
                +", transaction id="+accnEligHists.get(0).getTransId() +", transaction date="+accnEligHists.get(0).getTransDt());

        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), origPyrId);
        Assert.assertEquals(accnEligHists.get(0).getTranslatedPyrId(), translatedPyr);
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), expectedEligStatus);
        return accnEligHists;
    }

    private void performEligibilityCheckOnEPSearch(String accnId) throws Exception
    {
        int currentWindowCount = driver.getWindowHandles().size();
        logger.info("Load accn on Accn EP Search - " + accnId);
        EpSearchPlatform epSearchPlatform = new EpSearchPlatform(driver);
        epSearchPlatform.setFilterAndValueIntoSearchPF("Accession", accnId);
        epSearchPlatform.saveBtn().click();
        waitForEPSearchResultsScreen(currentWindowCount, QUEUE_WAIT_TIME);
        logger.info("Perform the action Check Eligibility on Accn EP Search - " + accnId);
        performActionInEPSearchPF("Check Payor Eligibility", 1, wait);
        waitForEPSearchResultsScreenToCollapse(currentWindowCount, QUEUE_WAIT_TIME);
    }

    private void waitForEPSearchResultsScreenToCollapse(final int currentWindowCount, long maxTime)
    {
        WebDriverWait popupWait = new WebDriverWait(driver, TimeUnit.MILLISECONDS.toSeconds(maxTime));
        popupWait.until(new ExpectedCondition<Boolean>()
        {
            public Boolean apply(WebDriver d)
            {
                return (d.getWindowHandles().size() == currentWindowCount);
            }
        });
    }
    private void performActionInEPSearchPF(String action, int rowNumber, WebDriverWait wait)
    {
        EpSearchResultsPlatform epSearchResultsPlatform = new EpSearchResultsPlatform(driver);
        wait.until(ExpectedConditions.elementToBeClickable(epSearchResultsPlatform.submitBtn()));
        epSearchResultsPlatform.selectCheckBoxPF(rowNumber).click();
        epSearchResultsPlatform.setActionDropDown(action);
        epSearchResultsPlatform.submitBtn().click();
    }
    private void waitForEPSearchResultsScreen(final int currentWindowCount, long maxTime) throws InterruptedException
    {
        WebDriverWait popupWait = new WebDriverWait(driver, TimeUnit.MILLISECONDS.toSeconds(maxTime));
        popupWait.until(new ExpectedCondition<Boolean>()
        {
            public Boolean apply(WebDriver d)
            {
                return (d.getWindowHandles().size() > currentWindowCount);
            }
        });
        switchToPopupWin();
    }

    private void setOriginalPyrToAccn(AccessionDetail accessionDetail, String accnId, String origPyrAbbrv) throws Exception
    {
        // accessionDetail.primaryPayorIDInput().click();
        WebElement primaryPyrId = accessionDetail.primaryPayorIDInput();
        wait.until(ExpectedConditions.visibilityOf(primaryPyrId));
        Thread.sleep(4000);
        primaryPyrId.clear();
        Thread.sleep(4000);
        primaryPyrId.sendKeys(origPyrAbbrv+Keys.TAB);
        Thread.sleep(4000);
        wait.until(ExpectedConditions.elementToBeClickable(accessionDetail.saveAndClearBtn()));
        accessionDetail.saveAndClearBtn().click();
        // Wait for accn to get out of Pricing queue. Pricing should be checked 2x.
        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME * 2));
    }

    private void navigateToEPSearchPF() throws Exception
    {
        logger.info("Navigate to Accn EP Search");
        MenuNavigation menuNavigation = new MenuNavigation(driver, config);
        menuNavigation.navigateToEPSearchPage();
    }
    private void verifyAccnIsInEPHold(String accnId, AccessionDetail accessionDetail) throws XifinDataAccessException
    {
        logger.info("Check Accession queue, "+ accnId);
        AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
        if (accnQue.getQTyp() != AccnStatusMap.Q_EP_HLD){
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
            accessionDetail.setForceToEP("EP Hold with INTFORCE", wait);
            accessionDetail.epHoldComments().sendKeys("TEST");
            wait.until(ExpectedConditions.elementToBeClickable(accessionDetail.saveAndClearBtn()));
            accessionDetail.saveAndClear(wait);
            Assert.assertEquals(accessionDao.getAccnQueByAccnId(accnId).getQTyp(), AccnStatusMap.Q_EP_HLD);
        }
    }

}
