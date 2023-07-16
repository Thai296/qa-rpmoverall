package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnCntct.AccnCntct;
import com.mbasys.mars.ejb.entity.accnEligHist.AccnEligHist;
import com.mbasys.mars.ejb.entity.accnErr.AccnErr;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.accnPyrDel.AccnPyrDel;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.accnQue.AccnQue;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.eligPyrRoster.EligPyrRoster;
import com.mbasys.mars.ejb.entity.eligResp.EligResp;
import com.mbasys.mars.ejb.entity.eligSvc.EligSvc;
import com.mbasys.mars.ejb.entity.hospitalAdmitCheck.HospitalAdmitCheck;
import com.mbasys.mars.ejb.entity.pt.Pt;
import com.mbasys.mars.ejb.entity.ptPyr.PtPyr;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrElig.PyrElig;
import com.mbasys.mars.ejb.entity.pyrXref.PyrXref;
import com.mbasys.mars.ejb.entity.qElig.QElig;
import com.mbasys.mars.ejb.entity.qValidateAccn.QValidateAccn;
import com.mbasys.mars.ejb.entity.systemSetting.SystemSetting;
import com.mbasys.mars.eligibility.EligMap;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.DbErrorMap;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.mbasys.mars.persistance.MiscMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.mbasys.mars.utility.cache.CacheMap;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.menu.MenuNavigation;
import com.overall.utils.EligibilityConstants;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.util.DateConversion;
import com.xifin.util.StringUtils;
import com.xifin.utils.ClearCacheUtil;
import com.xifin.utils.RetryAnalyzer;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class EligibilityEngineTest extends EligibilityBaseTest
{
    private MenuNavigation navigation;
    private AccessionDetail accessionDetail;

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
            logIntoSso(ssoUsername, ssoPassword);
            updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "True", "1");
            updateSystemSetting(SystemSettingMap.SS_PTESTIMATION_SUBMITTED_BY_ELIGENGINE, "False", "0");
            ClearCacheUtil clearCacheUtil = new ClearCacheUtil(orgAlias, config.getProperty(PropertyMap.XIFINADMINPORTAL_URL));
            clearCacheUtil.clearCache(CacheMap.SYSTEM_SETTINGS);
            logger.info("Cleared cache");
            driver.close();
        }
        catch (SkipException e)
        {
            logger.warn("Skipped exception thrown during BeforeSuite action", e);
        }
        catch (Exception e)
        {
            throw new SkipException("Error running BeforeSuite", e);
        }
        finally
        {
            quitWebDriver();
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method)
    {
        try
        {
            logIntoSso(parameters.getSsoUsername(), parameters.getSsoPassword());
            SystemSetting ss = systemDao.getSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE);
            if (!StringUtils.equals(ss.getDataValue(), "1"))
            {
                logger.info("message=Re-enabling system setting, systemSettingId="+SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE);
                updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "True", "1");
                ClearCacheUtil clearCacheUtil = new ClearCacheUtil(config.getProperty(PropertyMap.ORGALIAS), config.getProperty(PropertyMap.XIFINADMINPORTAL_URL));
                clearCacheUtil.clearCache(CacheMap.SYSTEM_SETTINGS);
                logger.info("message=Cleared cache");
            }
            navigation = new MenuNavigation(driver, config);
            navigation.navigateToAccnDetailPage();
        }
        catch (SkipException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new SkipException("Error running BeforeMethod", e);
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
            logIntoSso(ssoUsername, ssoPassword);
            updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "False", "0");
            ClearCacheUtil clearCacheUtil = new ClearCacheUtil(orgAlias, config.getProperty(PropertyMap.XIFINADMINPORTAL_URL));
            clearCacheUtil.clearCache(CacheMap.SYSTEM_SETTINGS);
        }
        catch (SkipException e)
        {
            logger.warn("Skipped exception thrown during AfterSuite", e);
        }
        catch (Exception e)
        {
            throw new SkipException("Error running AfterSuite", e);
        }
        finally
        {
            quitWebDriver();
        }
    }

    @Test(priority = 1, description = "Eligibility check is ELIGIBLE")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testXPR_1013(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: XPR-1013, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        simpleDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_ELIGIBLE, null, false, accnCntct);
    }

    @Test(priority = 1, description = "Eligibility check is INELIGIBLE")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testXPR_1014(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: XPR-1014, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        simpleDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_INELIGIBLE, DbErrorMap.INELIGIBLE, true, accnCntct);
    }

    @Test(priority = 1, description = "Eligibility check is REJECTED/ADDERR")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testXPR_1015(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: XPR-1015, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        logger.info("Clear Accn_ pyr and proc errs = " + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(testDb,accnId);
        simpleDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_INELIGIBLE, DbErrorMap.INELIGIBLE, true, accnCntct);
    }

    @Test(priority = 1, description = "Eligibility check is REJECTED/SUBMIT")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testXPR_1016(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: XPR-1016, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        simpleDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_REJECTED, null, false, accnCntct);
    }

    @Test(priority = 1, description = "Eligibility check is REJECTED/RECHK - Information Receiver Level")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testXPR_1017(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: XPR-1017, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        simpleDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_RECHK_ELIG, null, true, accnCntct);
    }

    @Test(priority = 1, retryAnalyzer = RetryAnalyzer.class, description = "Eligibility check is UNDETERMINED")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testXPR_1018(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: XPR-1018, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        cleanUpAccn(accnId);
        simpleDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_UNDETERMINED, DbErrorMap.ELIGUNDETERMND, false, accnCntct);
    }

    @Test(priority = 1, description = "Eligibility check is UNREACHABLE")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testXPR_1019(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: XPR-1019, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        simpleDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_UNREACHABLE, null, true, accnCntct);
    }

    @Test(priority = 1, description = "Primary Payor Translates to Same Payor; Insurance Type Code Translation Rule")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testXPR_952(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: XPR-952, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);

        logger.info("Verifying eligibility translation rule(s), accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        List<EligResp> eligResps = rpmDao.getEligRespByPyrId(testDb, rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId());
        Assert.assertEquals(eligResps.size(), 1, "EligResp size was incorrect, eligRespSize=" + eligResps.size());
        Assert.assertEquals(eligResps.get(0).getNewPyrId(), eligResps.get(0).getPyrId(), "EligResp payor was incorrect,");
        Assert.assertTrue(eligResps.get(0).getIsCheckElig(), "Expected isCheckElig to be true,");

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

        logger.info("Verifying accession is priced, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Expected accession status to be priced status,");

        loadAccession(accnId);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait), "Accession detail failed to load,");
        // Verify the primary payor is correct
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), pyrAbbrv, "Primary payor abbrev not equal,");

        logger.info("Forcing accession to reprice, accnId=" + accnId);
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED, "Expected accession status to be reported status,");

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.size(), 1, "AccnEligHist is not correct size,");
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), accnPyrs.get(0).getPyrId(), "Eligibility history is incorrect,");
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId(), "Eligibility history expected to have no translation, translatedPyrId=" + accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransId(), "Eligibility history expected to have transId,");
        Assert.assertNotNull(accnEligHists.get(0).getTransDt(), "Eligibility history expected to have transDt,");
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE, "Eligibility history expected eligible status, eligStaTypId=" + accnEligHists.get(0).getEligStaTypId()+ ",");

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);
        Assert.assertEquals(accnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
        Assert.assertNotNull(accnPyrs.get(0).getEligChkDt());

        logger.info("Verifying no new errors added, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        verifyEligTransLog(accnEligHists.get(0));
    }

    @Test(priority = 1, description = "ELIGDOB Error Added For DOB Mismatch")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testXPR_953(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: XPR-953, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        logger.info("Clear Accn_ pyr and proc errs = " + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(testDb,accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb,accnId);
        logger.info("Verifying eligibility translation rule(s), accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        List<EligResp> eligResps = rpmDao.getEligRespByPyrId(testDb, rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId());
        Assert.assertEquals(eligResps.size(), 2);
        logger.info("Eligibility translation rule exists, pyrId=" + eligResps.get(0).getPyrId() + ", newPyrId=" + eligResps.get(0).getNewPyrId());
        Assert.assertNotEquals(eligResps.get(0).getNewPyrId(), eligResps.get(0).getPyrId());

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

        logger.info("Verifying accession is priced or zbal, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertTrue(ACCN_STA_TYP_PRICED_OR_ZBAL.contains(accn.getStaId()));

        loadAccession(accnId);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        // Verify the primary payor is correct
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), pyrAbbrv);
        List<AccnPyrErr> accnPyrErrs= rpmDao.getUnfixedAccnPyrErrsByAccnId(testDb, accnId);

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertTrue(accn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), accnPyrs.get(0).getPyrId());
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_DOB_MISMATCH);

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_DOB_MISMATCH);
        Assert.assertEquals(accnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
        Assert.assertNotNull(accnPyrs.get(0).getEligChkDt());

        logger.info("Verifying ELIGDOB error is added, accnId=" + accnId);
        verifyUnfixedUnbillableAccnPyrErrs(new HashSet<>(Arrays.asList(DbErrorMap.ELIG_DOB)), accn, accnPyrs.get(0));

        verifyEligTransLog(accnEligHists.get(0));

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));

    }

    @Test(priority = 1, description = "UNKELGRESP Error Added For Unknown Response")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testXPR_954(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: XPR-954, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        logger.info("Clear Accn_ pyr and proc errs = " + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(testDb,accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);
        rpmDao.deleteAccnErrByAccnId(testDb, accnId);
        QValidateAccn qValidateAccn = rpmDao.getQValidateAccnByAccnId(null, accnId);
        if (qValidateAccn == null)
        {
            qValidateAccn = new QValidateAccn();
            qValidateAccn.setAccnId(accnId);
        }
        qValidateAccn.setPriority(3);
        qValidateAccn.setValidateTypId(16);
        qValidateAccn.setIsErr(false);
        accessionDao.setQValidateAccn(qValidateAccn);
        Assert.assertTrue(isOutOfValidationQueue(accnId, QUEUE_WAIT_TIME_MS));
        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME_MS));

        logger.info("Verifying eligibility translation rule(s), accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        List<EligResp> eligResps = rpmDao.getEligRespByPyrId(testDb, rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId());
        Assert.assertEquals(eligResps.size(), 2);
        logger.info("Eligibility translation rule exists, pyrId=" + eligResps.get(0).getPyrId() + ", newPyrId=" + eligResps.get(0).getNewPyrId());
        Assert.assertNotEquals(eligResps.get(0).getNewPyrId(), eligResps.get(0).getPyrId());

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

        logger.info("Verifying accession is priced or zbal, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertTrue(ACCN_STA_TYP_PRICED_OR_ZBAL.contains(accn.getStaId()));

        loadAccession(accnId);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        // Verify the primary payor is correct
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), pyrAbbrv);
        List<AccnPyrErr> accnPyrErrs= rpmDao.getUnfixedAccnPyrErrsByAccnId(testDb, accnId);

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        logger.info("Forcing accession to reprice, accnId=" + accnId);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertTrue(accn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), accnPyrs.get(0).getPyrId());
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_UNK_RESPONSE);

        logger.info("Verifying accession payor eligibility status is NOT updated, accnId=" + accnId);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_NOT_CHECKED, "Expected eligStaTyp to not be updated,");
        Assert.assertNull(accnPyrs.get(0).getEligSvcName());
        Assert.assertNull(accnPyrs.get(0).getEligChkDt());

        logger.info("Verifying UNKELGRESP error is added, accnId=" + accnId);
        verifyUnfixedUnbillableAccnPyrErrs(new HashSet<>(Arrays.asList(DbErrorMap.UNKELGRESP)), accn, accnPyrs.get(0));

        verifyEligTransLog(accnEligHists.get(0));

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));
    }

    @Test(priority = 1, description = "ELIGMULTMAP Error Added For Multiple Response")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testXPR_955(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: XPR-955, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        logger.info("Clear Accn_ pyr and proc errs = " + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(testDb,accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);
        rpmDao.deleteAccnErrByAccnId(testDb, accnId);
        QValidateAccn qValidateAccn = rpmDao.getQValidateAccnByAccnId(null, accnId);
        if (qValidateAccn == null)
        {
            qValidateAccn = new QValidateAccn();
            qValidateAccn.setAccnId(accnId);
        }
        qValidateAccn.setPriority(3);
        qValidateAccn.setValidateTypId(16);
        qValidateAccn.setIsErr(false);
        accessionDao.setQValidateAccn(qValidateAccn);
        Assert.assertTrue(isOutOfValidationQueue(accnId, QUEUE_WAIT_TIME_MS));
        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME_MS));

        logger.info("Verifying eligibility translation rule(s), accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        List<EligResp> eligResps = rpmDao.getEligRespByPyrId(testDb, pyr.getPyrId());
        Assert.assertEquals(eligResps.size(), 2);
        for (EligResp eligResp : eligResps)
        {
            logger.info("Eligibility translation rule exists, pyrId=" + eligResp.getPyrId() + ", newPyrId=" + eligResp.getNewPyrId() + ", prio=" + eligResp.getPrio());
            Assert.assertNotEquals(eligResp.getNewPyrId(), eligResp.getPyrId());
            Assert.assertEquals(eligResp.getPrio(), 1);
        }

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

        logger.info("Verifying accession is priced or zbal, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertTrue(ACCN_STA_TYP_PRICED_OR_ZBAL.contains(accn.getStaId()));

        loadAccession(accnId);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), pyr.getName());
        List<AccnPyrErr> accnPyrErrs= rpmDao.getUnfixedAccnPyrErrsByAccnId(testDb, accnId);

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        logger.info("Forcing accession to reprice, accnId=" + accnId);
        //accessionDetail.setAccnId(accnId, wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertTrue(accn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME),"Accession did not leave eligibility queue,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), accnPyrs.get(0).getPyrId());
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_MULTI_RESPONSE);

        logger.info("Verifying accession payor eligibility status is NOT updated, accnId=" + accnId);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_NOT_CHECKED, "Expected eligStaTyp to not be updated,");
        Assert.assertNull(accnPyrs.get(0).getEligSvcName());
        Assert.assertNull(accnPyrs.get(0).getEligChkDt());

        logger.info("Verifying ELIGMULTMAP error is added, accnId=" + accnId);
        verifyUnfixedUnbillableAccnPyrErrs(new HashSet<>(Arrays.asList(DbErrorMap.ELIGMULTMAP)), accn, accnPyrs.get(0));

        verifyEligTransLog(accnEligHists.get(0));

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));
    }

    @Test(priority = 1, description = "Primary Payor Translates to Different Payor; Service Type Code UC")
    @Parameters({"accnId", "pyrAbbrv"})
    public void testXPR_956(String accnId, String pyrAbbrv) throws Exception
    {
        logger.info("Starting Test Case: XPR-956, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);

        logger.info("Verifying eligibility translation rule(s), accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        List<EligResp> eligResps = rpmDao.getEligRespByPyrId(testDb, pyr.getPyrId());
        Assert.assertEquals(eligResps.size(), 3);
        logger.info("Eligibility translation rules exist, pyrId=" + eligResps.get(0).getPyrId());
        Assert.assertEquals(eligResps.get(0).getNewPyrId(), eligResps.get(0).getPyrId(), "Expected translation rule to be to same payor");
        Assert.assertEquals(eligResps.get(0).getPrio(), 1);
        Assert.assertEquals(eligResps.get(0).getSvcTypCd(), EligibilityConstants.ServiceTypeCode.SERVICETYPECODE_UC.getCode());

        //This resp has first prio but should not be selected due to date
        Assert.assertNotEquals(eligResps.get(1).getNewPyrId(), eligResps.get(1).getPyrId(), "Expected translation rule to be to different payor");
        Assert.assertEquals(eligResps.get(1).getPrio(), 2);
        Assert.assertEquals(eligResps.get(1).getSvcTypCd(), EligibilityConstants.ServiceTypeCode.SERVICETYPECODE_UC.getCode());

        Assert.assertEquals(eligResps.get(2).getPrio(), 3);

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
        String subsId = accnPyrs.get(0).getSubsId();

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

        logger.info("Verifying accession is priced or zbal, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertTrue(ACCN_STA_TYP_PRICED_OR_ZBAL.contains(accn.getStaId()));

        loadAccession(accnId);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Fixing the INELIGIBLE error, accnId=" + accnId);
        if(rpmDao.getFixedAccnPyrErrsByAccnId(testDb, accnId).size()==0)
        {
            accessionDetail.setCurrAccnErrAction(2);
            accessionDetail.saveAndClear(wait);

            accessionDetail.setAccnId(accnId, wait);
        }
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        if (StringUtils.equalsIgnoreCase(accessionDetail.primaryPayorAbbrText().getText(), pyr.getName()))
        {
            logger.info("Primary payor already exists on accession; forcing to reprice, accnId=" + accnId + ", pyrAbbrv=" + pyr.getPyrAbbrv());
            accessionDetail.clickRepriceCheckbox(wait);
        }
        else
        {
            logger.info("Primary payor does not exist on accession; updating payor, accnId=" + accnId + ", pyrAbbrv=" + pyr.getPyrAbbrv());
            accessionDetail.clearPrimaryPyrId();
            accessionDetail.setPrimaryPyrId(pyrAbbrv);
        }
        accessionDetail.saveAndClear(wait);

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        List<AccnPyrErr> actualAccnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty(), "Expected no open payor errors on accn, accnId="+accnId+", actualAccnPyrErrs="+actualAccnPyrErrs);

        // Reload accession from DB
        accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertTrue(accn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for accn to get out of eligibility queue. Eligibility should be checked 2x.
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME * 2), "Accession did not leave eligibility queue,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        // Should be 2 elig checks
        // Verify 1st elig check translated to new payor
        Assert.assertTrue(accnEligHists.size() >=2, "Expected accnEligHist records were not found,");
        // Having stability issues with this test because of the history records
        for (AccnEligHist accnEligHist : accnEligHists)
        {
            if (accnEligHist.getPyrId() == Integer.valueOf(eligResps.get(1).getPyrId()))
            {
                Assert.assertEquals(accnEligHist.getTranslatedPyrId(), Integer.valueOf(eligResps.get(1).getNewPyrId()), "Expected accnEligHist transPyrId was not found,");
                Assert.assertNotNull(accnEligHist.getTransId(), "Expected accnEligHist transId was not found,");
                Assert.assertNotNull(accnEligHist.getTransDt(), "Expected accnEligHist transDt was not found,");
                Assert.assertEquals(accnEligHist.getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE, "Expected accnEligHist eligStaTyp was not found,");
            }
            else if (accnEligHist.getPyrId() == Integer.valueOf(eligResps.get(1).getNewPyrId()))
            {
                Assert.assertNull(accnEligHist.getTranslatedPyrId(), "Unexpected accnEligHist transPyrId was found,");
                Assert.assertNotNull(accnEligHist.getTransId(), "Expected accnEligHist transId was not found,");
                Assert.assertNotNull(accnEligHist.getTransDt(), "Expected accnEligHist transDt was not found,");
                Assert.assertEquals(accnEligHist.getEligStaTypId(), EligMap.ELIG_STA_TYP_INELIGIBLE, "Expected accnEligHist eligStaTyp was not found,");
            }
            else
            {
                Assert.fail("Failed to find any of the expected accnEligHist records, pyrId=" + accnEligHist.getPyrId());
            }
        }

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getPyrId(), eligResps.get(1).getNewPyrId());
        Assert.assertEquals(accnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_INELIGIBLE);
        Assert.assertEquals(accnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
        Assert.assertNotNull(accnPyrs.get(0).getEligChkDt());
        Assert.assertNotNull(accnPyrs.get(0).getSubsId());

        List<PtPyr> ptPyrs = accessionDao.getPtPyrs(accn.getPtSeqId(), accn.getDos());
        Assert.assertEquals(ptPyrs.size(), 1);
        boolean isPtPyrFound = false;
        for (PtPyr ptPyr : ptPyrs)
        {
            if (ptPyr.getPyrId() == ptPyr.getPyrId())
            {
                Assert.assertEquals(ptPyr.getPyrPrio(), 1, "Expected ptPyr pyrPrio to be primary");
                Assert.assertEquals(ptPyr.getSubsId(), accnPyrs.get(0).getSubsId(), "Expected same subsId");
                isPtPyrFound = true;
            }
        }
        Assert.assertTrue(isPtPyrFound, "Patient payor was not found");

        AccnPyrDel accnPyrDel = rpmDao.getAccnPyrDelByAccnAndPyrId(null, accn.getAccnId(), pyr.getPyrId());
        Assert.assertEquals(accnPyrDel.getPyrId(), pyr.getPyrId());
        Assert.assertEquals(accnPyrDel.getSubId(), subsId);

        logger.info("Verifying INELIGIBLE error is added, accnId=" + accnId);
        verifyUnfixedUnbillableAccnPyrErrs(new HashSet<>(Arrays.asList(DbErrorMap.INELIGIBLE)), accn, accnPyrs.get(0));

        verifyEligTransLog(accnEligHists.get(0));
    }

    @Test(priority = 1, description = "Primary Payor Translates to Different Payor; Service Type Code UC")
    @Parameters({"accnId", "origPyrAbbrvs", "newPyrAbbrvs", "accnCntct"})
    public void testXPR_957(String accnId, String origPyrAbbrvs, String newPyrAbbrvs, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: XPR-957, accnId=" + accnId + ", origPyrAbbrvs=" + origPyrAbbrvs + ", newPyrAbbrvs=" + newPyrAbbrvs);

        List<Pyr> origPyrs = new ArrayList<>();
        for (String abbrv : origPyrAbbrvs.split(","))
        {
            origPyrs.add(rpmDao.getPyrByPyrAbbrv(testDb, abbrv.trim()));
        }
        List<Pyr> newPyrs = new ArrayList<>();
        for (String abbrv : newPyrAbbrvs.split(","))
        {
            newPyrs.add(rpmDao.getPyrByPyrAbbrv(testDb, abbrv.trim()));
        }

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        // Verify eligibility translation setup
        logger.info("Verifying eligibility translation setup, accnId=" + accnId + ", pyrId=" + origPyrs.get(0).getPyrAbbrv());
        List<EligResp> eligResps = rpmDao.getEligRespByPyrId(testDb, origPyrs.get(0).getPyrId());
        Assert.assertEquals(eligResps.size(), 4);
        Assert.assertTrue(eligResps.get(0).getPrio() < eligResps.get(3).getPrio());
        // Make sure rule #1 translates to new primary payor
        Assert.assertEquals(eligResps.get(0).getNewPyrId(), newPyrs.get(0).getPyrId());
        // Make sure rule #2 translates to original primary payor
        Assert.assertEquals(eligResps.get(3).getNewPyrId(), newPyrs.get(1).getPyrId());
        Assert.assertTrue(eligResps.get(3).getIsAllowSecondary());

        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

        loadAccession(accnId);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait), "Accession was not loaded on Accn Detail screen, accnId=" + accnId);

        boolean isPayorRemoved = false;
        while (accnPyrs.size() > origPyrs.size())
        {
            logger.info("Removing payor from accession, accnId=" + accnId + ", pyrPrio=" + accnPyrs.get(0).getPyrPrio() + ", pyrId=" + accnPyrs.get(0).getPyrId());
            accessionDetail.deletePayor(accnPyrs.get(0).getPyrPrio() - 1);
            accnPyrs.remove(0);
            isPayorRemoved = true;
        }
        if (!isPayorRemoved)
        {
            logger.info("No payors removed (payors match); forcing reprice, accnId=" + accnId);
            accessionDetail.clickRepriceCheckbox(wait);
        }
        accessionDetail.saveAndClear(wait);

        // Verify original accession payors
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        logger.info("Verifying accession payors, accnId=" + accnId);
        Assert.assertEquals(accnPyrs.size(), origPyrs.size());
        for (int i = 0; i < accnPyrs.size(); i++)
        {
              Assert.assertEquals(accnPyrs.get(i).getPyrId(), origPyrs.get(i).getPyrId());

        }

        // Verify open payor errors
        // Exactly 2 unfixed payor errors
        logger.info("Verifying open unbillable payor errors, accnId=" + accnId);
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).size(), 2);
        // Primary payor should have INSDZIP
        verifyUnfixedUnbillableAccnPyrErrs(new HashSet<>(Arrays.asList(DbErrorMap.INSDZIP)), accn, accnPyrs.get(0));
        // Secondary payor should have SUBID
        verifyUnfixedUnbillableAccnPyrErrs(new HashSet<>(Arrays.asList(DbErrorMap.SUBID)), accn, accnPyrs.get(1));

        // Reload accession from DB
        accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertTrue(accn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for accn to get out of eligibility queue.
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        // Verify elig check translated to new payor
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(Integer.valueOf(accnEligHists.get(0).getPyrId()), Integer.valueOf(accnPyrs.get(0).getPyrId()));
        Assert.assertEquals(accnEligHists.get(0).getTranslatedPyrId(), Integer.valueOf(eligResps.get(0).getNewPyrId()));
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), newPyrs.size());
        for (int i = 0; i < newPyrs.size(); i++)
        {
            Assert.assertEquals(accnPyrs.get(i).getPyrId(), newPyrs.get(i).getPyrId());
            if (i == 0)
            {
                Assert.assertEquals(accnPyrs.get(i).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);
                Assert.assertEquals(accnPyrs.get(i).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
                Assert.assertNotNull(accnPyrs.get(i).getEligChkDt());
                Assert.assertNotNull(accnPyrs.get(i).getSubsId());
            }
        }

        logger.info("Verifying accession patient payor eligibility status is updated, accnId=" + accnId);
        List<PtPyr> ptPyrs = accessionDao.getPtPyrs(accn.getPtSeqId(), accn.getDos());
        boolean isPtPyrFound = false;
        Assert.assertEquals(ptPyrs.size(), 3);
        for (PtPyr ptPyr : ptPyrs)
        {
            if (ptPyr.getPyrId() == accnPyrs.get(0).getPyrId())
            {
                Assert.assertEquals(ptPyr.getPyrPrio(), 1, "Expected ptPyr pyrPrio to be primary");
                Assert.assertEquals(ptPyr.getSubsId(), accnPyrs.get(0).getSubsId(), "Expected same subsId");
                isPtPyrFound = true;
            }
        }
        Assert.assertTrue(isPtPyrFound, "Patient payor was not found");

        logger.info("Verifying new payor (non-elig) error added, accnId=" + accnId);
        // Should still only be 2 open errors
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).size(), 3);
        logger.info("Verifying payor errors were shuffled, accnId=" + accnId);
        // Secondary payor should have INSDZIP
        verifyUnfixedUnbillableAccnPyrErrs(new HashSet<>(Arrays.asList(DbErrorMap.INSDZIP)), accn, accnPyrs.get(0));
        // Secondary payor should have INSDZIP
        verifyUnfixedUnbillableAccnPyrErrs(new HashSet<>(Arrays.asList(DbErrorMap.INSDZIP)), accn, accnPyrs.get(1));
        // Tertiary payor should have SUBID
        verifyUnfixedUnbillableAccnPyrErrs(new HashSet<>(Arrays.asList(DbErrorMap.SUBID)), accn, accnPyrs.get(2));

        verifyEligTransLog(accnEligHists.get(0));

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));
    }

        
    @Test(priority = 1, description = "Eligibility check is ELIGIBLE, Patient's Name, Gender, Address are updated")
    @Parameters({"accnId", "pyrAbbrv", "patientFNm", "patientLNm", "patientAddr1", "patientAddr2", "patientSex", "patientCity", "patientZipId", "accnCntct"})
    public void testXPR_1046(String accnId, String pyrAbbrv, String patientFNm, String patientLNm, String patientAddr1, String patientAddr2, int patientSex, String patientCity, String patientZipId, String accnCntct) throws Exception
    {
       
    	int eligStaTypId=EligMap.ELIG_STA_TYP_ELIGIBLE;
    	logger.info("Starting Test Case: XPR-1046, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        logger.info("Verifying payor eligibility configuration, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        PyrElig pyrElig = null;
        for (PyrElig pe : rpmDao.getPyrEligByPyrIdEligSvcId(testDb, pyr.getPyrId(), EligMap.XIFIN))
        {
            if (!pe.getIsDeleted() && pe.getIsPreferred())
            {
                pyrElig = pe;
            }
        }
        Assert.assertNotNull(pyrElig);
        // Translation should be disabled
        Assert.assertFalse(pyrElig.getIsTranslationEnabled());
        // Patient demographic updates should be enabled
        Assert.assertFalse(pyrElig.getIsUpdateInsSubsId());
        Assert.assertFalse(pyrElig.getIsUpdateInsNm());
        Assert.assertFalse(pyrElig.getIsUpdateInsAddr());
        Assert.assertFalse(pyrElig.getIsUpdateInsGender());
        Assert.assertFalse(pyrElig.getIsUpdateInsRelshp());
        Assert.assertTrue(pyrElig.getIsUpdatePtNm());
        Assert.assertTrue(pyrElig.getIsUpdatePtAddr());
        Assert.assertTrue(pyrElig.getIsUpdatePtGender());

        
        Accn origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying accession status is priced or zbal, accnId=" + accnId + ", staId=" + origAccn.getStaId());
        Assert.assertTrue(ACCN_STA_TYP_PRICED_OR_ZBAL.contains(origAccn.getStaId()));

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        List<AccnPyr> origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        verifyAccnEligDataIsCleared(accessionDao.getAccnEligHistByAccnId(accnId), origAccnPyrs);

        loadAccession(accnId);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        // Verify the primary payor is correct
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), pyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());
        
        logger.info("Update Patient Name, Patient Address and Gender");
        updatePatientDemographics("testAddressLine1", "testAddressLine2","testPatientFirstName", "testPatientLastName", "0", "92130", "Los Angeles");
        
        logger.info("Forcing accession to reprice, accnId=" + accnId);
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + origAccn.getStaId());
        Assert.assertTrue(origAccn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        List<AccnEligHist> accnEligHists = waitForEligibilityCheck(accnId);

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), pyr.getPyrId());
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), eligStaTypId);

        logger.info("Create accession payor record, accnId=" + accnId);
        Accn newAccn = accessionDao.getAccn(accnId);
        List<PtPyr> ptPyrs = accessionDao.getPtPyrs(newAccn.getPtSeqId(), newAccn.getDos());
        PtPyr newPtPyr = null;

        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(newAccnPyrs.size(), 1);
        Assert.assertEquals(newAccnPyrs.get(0).getPyrId(), pyr.getPyrId());

        for (PtPyr ptPyr : ptPyrs)
        {
            if (ptPyr.getPyrId() == newAccnPyrs.get(0).getPyrId())
            {
                newPtPyr = ptPyr;
            }
        }

        Assert.assertNotNull(newPtPyr, "Expected patient payor record, ptSeqId=" + newAccn.getPtSeqId());
       
        // Verify patient/demo data is updated
        logger.info("Verifying patient demographic data is updated and insured demographic data is not updated, accnId=" +accnId);
       
        Assert.assertEquals(patientFNm, newAccn.getPtFNm());
        Assert.assertEquals(patientLNm, newAccn.getPtLNm());
        Assert.assertEquals(patientAddr1, newAccn.getPtAddr1());
        Assert.assertEquals(patientAddr2, newAccn.getPtAddr2());
        Assert.assertEquals(patientSex, newAccn.getPtSex());
        Assert.assertEquals(patientCity, newAccn.getPtCity());
        Assert.assertEquals(patientZipId, newAccn.getPtZipId());
        Assert.assertEquals(origAccnPyrs.get(0).getSubsId(), newAccnPyrs.get(0).getSubsId());
        Assert.assertEquals(origAccnPyrs.get(0).getInsFNm(), newAccnPyrs.get(0).getInsFNm());
        Assert.assertEquals(origAccnPyrs.get(0).getInsLNm(), newAccnPyrs.get(0).getInsLNm());
        Assert.assertEquals(origAccnPyrs.get(0).getInsAddr1(), newAccnPyrs.get(0).getInsAddr1());
        Assert.assertEquals(origAccnPyrs.get(0).getInsAddr2(), newAccnPyrs.get(0).getInsAddr2());
        Assert.assertEquals(origAccnPyrs.get(0).getInsSex(), newAccnPyrs.get(0).getInsSex());
        Assert.assertEquals(origAccnPyrs.get(0).getRelshpId(), newAccnPyrs.get(0).getRelshpId());

        Assert.assertEquals(newPtPyr.getSubsId(), newAccnPyrs.get(0).getSubsId(), "Permanent patient demographic does not match new payor values (subsId), ptSeqId=" + newAccn.getPtSeqId());
        Assert.assertEquals(newPtPyr.getInsFNm(), newAccnPyrs.get(0).getInsFNm(), "Permanent patient demographic does not match new payor values (fNm), ptSeqId=" + newAccn.getPtSeqId());
        Assert.assertEquals(newPtPyr.getInsLNm(), newAccnPyrs.get(0).getInsLNm(), "Permanent patient demographic does not match new payor values (lNm), ptSeqId=" + newAccn.getPtSeqId());
        Assert.assertEquals(newPtPyr.getInsAddr1(), newAccnPyrs.get(0).getInsAddr1(), "Permanent patient demographic does not match new payor values (insAddr1), ptSeqId=" + newAccn.getPtSeqId());
        Assert.assertEquals(newPtPyr.getInsAddr2(), newAccnPyrs.get(0).getInsAddr2(), "Permanent patient demographic does not match new payor values  (insAddr2), ptSeqId=" + newAccn.getPtSeqId());
        Assert.assertEquals(newPtPyr.getInsSex(), newAccnPyrs.get(0).getInsSex(), "Permanent patient demographic does not match new payor values (insSex), ptSeqId=" + newAccn.getPtSeqId());
        Assert.assertEquals(newPtPyr.getRelshpTypId(), newAccnPyrs.get(0).getRelshpId(), "Permanent patient demographic does not match new payor values (relTyp), ptSeqId=" + newAccn.getPtSeqId());

        logger.info("Verifying no new errors added, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty(), "Accession payor errors were found, accnId=" + accnId);
        Assert.assertTrue(isOutOfPricingQueue(accnId,QUEUE_WAIT_TIME * 5), "Accession did not leave pricing queue, accnId=" + accnId);
        origAccn = accessionDao.getAccn(accnId);
        Assert.assertEquals(origAccn.getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession was not priced, accnId=" + accnId);

        verifyEligTransLog(accnEligHists.get(0));

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));
    }

    @Test(priority = 1, description = "Eligibility check is ELIGIBLE, Insured Name, Gender, Address are updated")
    @Parameters({"accnId", "pyrAbbrv", "insuredFNm", "insuredLNm", "insuredAddr1", "insuredAddr2", "insuredSex", "insuredCity", "insuredZipId", "accnCntct"})
    public void testXPR_1047(String accnId, String pyrAbbrv, String insuredFNm, String insuredLNm, String insuredAddr1, String insuredAddr2, int insuredSex, String insuredCity, String insuredZipId, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: XPR-1047, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        logger.info("Verifying payor eligibility configuration, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        PyrElig pyrElig = null;
        for (PyrElig pe : rpmDao.getPyrEligByPyrIdEligSvcId(testDb, pyr.getPyrId(), EligMap.XIFIN))
        {
            if (!pe.getIsDeleted() && pe.getIsPreferred())
            {
                pyrElig = pe;
            }
        }
        Assert.assertNotNull(pyrElig);
        // Translation should be disabled
        Assert.assertFalse(pyrElig.getIsTranslationEnabled());
        // Insured demographic updates should be enabled, Patient demographic updates should be disabled
        Assert.assertFalse(pyrElig.getIsUpdateInsSubsId());
        Assert.assertTrue(pyrElig.getIsUpdateInsNm());
        Assert.assertTrue(pyrElig.getIsUpdateInsAddr());
        Assert.assertTrue(pyrElig.getIsUpdateInsGender());
        Assert.assertFalse(pyrElig.getIsUpdateInsRelshp());
        Assert.assertFalse(pyrElig.getIsUpdatePtNm());
        Assert.assertFalse(pyrElig.getIsUpdatePtAddr());
        Assert.assertFalse(pyrElig.getIsUpdatePtGender());

        Accn origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying accession status is priced or zbal, accnId=" + accnId + ", staId=" + origAccn.getStaId());
        Assert.assertTrue(ACCN_STA_TYP_PRICED_OR_ZBAL.contains(origAccn.getStaId()));

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        List<AccnPyr> origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        verifyAccnEligDataIsCleared(accessionDao.getAccnEligHistByAccnId(accnId), origAccnPyrs);

        loadAccession(accnId);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        // Verify the primary payor is correct
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), pyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        logger.info("Update Insured Name, Address and Gender");

        updateInsuredDemographics("InsuredAddressLine1", "InsuredAddressLine2", "InsuredFirstName", "testInsuredLastName", "0", "90009", "San Diego");

        logger.info("Forcing accession to reprice, accnId=" + accnId);
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + origAccn.getStaId());
        Assert.assertTrue(origAccn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for the eligibility check
        List<AccnEligHist> accnEligHists = waitForEligibilityCheck(accnId);

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), pyr.getPyrId());
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);

        logger.info("Create accession payor record, accnId=" + accnId);
        Accn newAccn = accessionDao.getAccn(accnId);
        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(newAccnPyrs.size(), 1);
        Assert.assertEquals(newAccnPyrs.get(0).getPyrId(), pyr.getPyrId());

        // Verify patient/demo data not updated, insured demo info is updated
        logger.info("Verifying Insured demographic data is updated and Patient demographic data is not updated, accnId=" + accnId);

        Assert.assertEquals(origAccn.getPtFNm(), newAccn.getPtFNm());
        Assert.assertEquals(origAccn.getPtLNm(), newAccn.getPtLNm());
        Assert.assertEquals(origAccn.getPtAddr1(), newAccn.getPtAddr1());
        Assert.assertEquals(origAccn.getPtAddr2(), newAccn.getPtAddr2());
        Assert.assertEquals(origAccn.getPtSex(), newAccn.getPtSex());
        Assert.assertEquals(origAccn.getPtCity(), newAccn.getPtCity());
        Assert.assertEquals(origAccn.getPtZipId(), newAccn.getPtZipId());
        Assert.assertEquals(origAccnPyrs.get(0).getSubsId(), newAccnPyrs.get(0).getSubsId());
        Assert.assertEquals(origAccnPyrs.get(0).getRelshpId(), newAccnPyrs.get(0).getRelshpId());
        Assert.assertEquals(newAccnPyrs.get(0).getInsFNm(), insuredFNm);
        Assert.assertEquals(newAccnPyrs.get(0).getInsLNm(), insuredLNm);
        Assert.assertEquals(newAccnPyrs.get(0).getInsAddr1(), insuredAddr1);
        Assert.assertEquals(newAccnPyrs.get(0).getInsAddr2(), insuredAddr2);
        Assert.assertEquals(newAccnPyrs.get(0).getInsSex(), insuredSex);
        Assert.assertEquals(newAccnPyrs.get(0).getInsCity(), insuredCity);
        Assert.assertEquals(newAccnPyrs.get(0).getInsZipId(), insuredZipId);

        logger.info("Verifying no new errors added, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        verifyEligTransLog(accnEligHists.get(0));

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));
    }

    @Test(priority = 1, description = "Eligibility check is ELIGIBLE, Patient and Insured Demographic Data is not updated")
    @Parameters({"accnId", "pyrAbbrv", "patientFNm", "patientLNm", "patientAddr1", "patientAddr2", "patientSex", "patientCity", "patientZipId", "subscriberId", "relshpId", "insuredFNm", "insuredLNm", "insuredAddr1", "insuredAddr2", "insuredSex", "insuredCity", "insuredZipId", "accnCntct"})
    public void testXPR_1048(String accnId, String pyrAbbrv, String patientFNm, String patientLNm, String patientAddr1, String patientAddr2, int patientSex, String patientCity, String patientZipId, String subscriberId, String relshpId, String insuredFNm, String insuredLNm, String insuredAddr1, String insuredAddr2, int insuredSex, String insuredCity, String insuredZipId, String accnCntct) throws Exception
    {

        logger.info("Starting Test Case: XPR-1048, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);

        Accn origAccn = accessionDao.getAccn(accnId);

        List<AccnPyr> origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        logger.info("Verifying that Patient and Insured Demograthic data is not mathing the 271 data for accnId = " + accnId);

        Assert.assertNotEquals(origAccn.getPtFNm(), patientFNm);
        Assert.assertNotEquals(origAccn.getPtLNm(), patientLNm);
        Assert.assertNotEquals(origAccn.getPtAddr1(), patientAddr1);
        Assert.assertNotEquals(origAccn.getPtAddr2(), patientAddr2);
        Assert.assertNotEquals(origAccn.getPtSex(), patientSex);
        Assert.assertNotEquals(origAccn.getPtCity(), patientCity);
        Assert.assertNotEquals(origAccn.getPtZipId(), patientZipId);
        Assert.assertNotEquals(origAccnPyrs.get(0).getSubsId(), subscriberId);
        Assert.assertNotEquals(origAccnPyrs.get(0).getRelshpId(), relshpId);
        Assert.assertNotEquals(origAccnPyrs.get(0).getInsFNm(), insuredFNm);
        Assert.assertNotEquals(origAccnPyrs.get(0).getInsLNm(), insuredLNm);
        Assert.assertNotEquals(origAccnPyrs.get(0).getInsAddr1(), insuredAddr1);
        Assert.assertNotEquals(origAccnPyrs.get(0).getInsAddr2(), insuredAddr2);
        Assert.assertNotEquals(origAccnPyrs.get(0).getInsSex(), insuredSex);
        Assert.assertNotEquals(origAccnPyrs.get(0).getInsCity(), insuredCity);
        Assert.assertNotEquals(origAccnPyrs.get(0).getInsZipId(), insuredZipId);

        simpleDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_ELIGIBLE, null, false, accnCntct);
    }


    @Test(priority = 1, description = "Eligibility check is ELIGIBLE, Patient and Insured Demographic Data is updated - mixed case")
    @Parameters({"accnId", "pyrAbbrv", "patientFNm", "patientLNm", "patientAddr1", "patientAddr2", "patientSex", "patientCity", "patientZipId", "subscriberId", "relshpId", "insuredFNm", "insuredLNm", "insuredAddr1", "insuredAddr2", "insuredSex", "insuredCity", "insuredZipId", "accnCntct"})
    public void testXPR_1049(String accnId, String pyrAbbrv, String patientFNm, String patientLNm, String patientAddr1, String patientAddr2, int patientSex, String patientCity, String patientZipId, String subscriberId, String relshpId, String insuredFNm, String insuredLNm, String insuredAddr1, String insuredAddr2, int insuredSex, String insuredCity, String insuredZipId, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: XPR-1049, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        logger.info("Verifying payor eligibility configuration, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
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

        Assert.assertTrue(pyrElig.getIsUpdateInsNm());
        Assert.assertTrue(pyrElig.getIsUpdateInsAddr());
        Assert.assertTrue(pyrElig.getIsUpdateInsGender());
        Assert.assertTrue(pyrElig.getIsUpdatePtNm());
        Assert.assertTrue(pyrElig.getIsUpdatePtAddr());
        Assert.assertTrue(pyrElig.getIsUpdatePtGender());

        Accn origAccn = accessionDao.getAccn(accnId);
        Assert.assertEquals(origAccn.getPtSeqId(), 0, "Accession should have no EPI, accnId="+origAccn.getAccnId()+", ptSeqId="+origAccn.getPtSeqId());

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        List<AccnPyr> origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        verifyAccnEligDataIsCleared(accessionDao.getAccnEligHistByAccnId(accnId), origAccnPyrs);

        loadAccession(accnId);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        // Verify the primary payor is correct
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), pyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        logger.info("Update Patient and Insured Name, Address and Gender");
        updatePatientDemographics("testAddressLine1", "Patient Addr2", "testPatientFirstName", "testPatientLastName", "0", "92130", "Los Angeles");
        updateInsuredDemographics("InsuredAddressLine1", "InsuredAddressLine2", "InsuredFirstName", "testInsuredLastName", "0", "90009", "San Diego");

        logger.info("Forcing accession to reprice, accnId=" + accnId);
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + origAccn.getStaId());
        Assert.assertTrue(origAccn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        Assert.assertEquals(origAccn.getPtSeqId(), 0, "Accession should have no EPI, accnId="+origAccn.getAccnId()+", ptSeqId="+origAccn.getPtSeqId());

        // Wait for the eligibility check
        List<AccnEligHist> accnEligHists = waitForEligibilityCheck(accnId);

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), pyr.getPyrId());
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);

        logger.info("Create accession payor record, accnId=" + accnId);
        Accn newAccn = accessionDao.getAccn(accnId);
        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(newAccnPyrs.size(), 1);
        Assert.assertEquals(newAccnPyrs.get(0).getPyrId(), pyr.getPyrId());

        // Verify patient/demo and insured demo info is updated
        logger.info("Verifying patient/demo and insured demo info is updated, accnId=" + accnId);

        Assert.assertEquals(newAccn.getPtFNm(), patientFNm);
        Assert.assertEquals(newAccn.getPtLNm(), patientLNm);
        Assert.assertEquals(newAccn.getPtAddr1(), patientAddr1);
        Assert.assertEquals(newAccn.getPtAddr2(), patientAddr2);
        Assert.assertEquals(newAccn.getPtSex(), patientSex);
        Assert.assertEquals(newAccn.getPtCity(), patientCity);
        Assert.assertEquals(newAccn.getPtZipId(), patientZipId);
        Assert.assertEquals(newAccnPyrs.get(0).getInsFNm(), insuredFNm);
        Assert.assertEquals(newAccnPyrs.get(0).getInsLNm(), insuredLNm);
        Assert.assertEquals(newAccnPyrs.get(0).getInsAddr1(), insuredAddr1);
        Assert.assertEquals(newAccnPyrs.get(0).getInsAddr2(), insuredAddr2);
        Assert.assertEquals(newAccnPyrs.get(0).getInsSex(), insuredSex);
        Assert.assertEquals(newAccnPyrs.get(0).getInsCity(), insuredCity);
        Assert.assertEquals(newAccnPyrs.get(0).getInsZipId(), insuredZipId);

        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME * 5));
        origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying accn is priced =" + origAccn.getStaId());
        Assert.assertEquals(origAccn.getPtSeqId(), 0, "Accession should have no EPI, accnId="+origAccn.getAccnId()+", ptSeqId="+origAccn.getPtSeqId());
        // logger.info("Verifying post-eligibility queue, accnId=" + accnId + ", qTyp=" + qTyp);
        Assert.assertEquals(origAccn.getStaId(), AccnStatusMap.ACCN_STATUS_PRICED);
        logger.info("Verifying no new errors added, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        verifyEligTransLog(accnEligHists.get(0));

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));
    }


    @Test(priority = 1, description = "Eligibility check is ELIGIBLE, Patient and Insured Demo (EPI) Data is updated - mixed case")
    @Parameters({"accnId", "pyrAbbrv", "patientFNm", "patientLNm", "patientAddr1", "patientAddr2", "patientSex", "patientCity", "patientZipId", "subscriberId", "relshpId", "insuredFNm", "insuredLNm", "insuredAddr1", "insuredAddr2", "insuredSex", "insuredCity", "insuredZipId", "accnCntct"})
    public void testXPR_1054(String accnId, String pyrAbbrv, String patientFNm, String patientLNm, String patientAddr1, String patientAddr2, int patientSex, String patientCity, String patientZipId, String subscriberId, String relshpId, String insuredFNm, String insuredLNm, String insuredAddr1, String insuredAddr2, int insuredSex, String insuredCity, String insuredZipId, String accnCntct) throws Exception
    {

        logger.info("Starting Test Case: XPR-1054, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        logger.info("Verifying payor eligibility configuration, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
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

        Assert.assertTrue(pyrElig.getIsUpdateInsNm());
        Assert.assertTrue(pyrElig.getIsUpdateInsAddr());
        Assert.assertTrue(pyrElig.getIsUpdateInsGender());
        Assert.assertTrue(pyrElig.getIsUpdatePtNm());
        Assert.assertTrue(pyrElig.getIsUpdatePtAddr());
        Assert.assertTrue(pyrElig.getIsUpdatePtGender());

        Accn origAccn = accessionDao.getAccn(accnId);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        List<AccnPyr> origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        verifyAccnEligDataIsCleared(accessionDao.getAccnEligHistByAccnId(accnId), origAccnPyrs);

        loadAccession(accnId);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        // Verify the primary payor is correct
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), pyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        logger.info("Update Patient and Insured Name, Address and Gender");
        updatePatientDemographics("testAddressLine1", "Patient Addr2", "testPatientFirstName", "testPatientLastName", "0", "92130", "Los Angeles");
        updateInsuredDemographics("InsuredAddressLine1", "InsuredAddressLine2", "InsuredFirstName", "testInsuredLastName", "0", "90009", "San Diego");

        logger.info("Forcing accession to reprice, accnId=" + accnId);
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + origAccn.getStaId());
        Assert.assertTrue(origAccn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for the eligibility check
        List<AccnEligHist> accnEligHists = waitForEligibilityCheck(accnId);

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), pyr.getPyrId());
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);

        logger.info("Create accession payor record, accnId=" + accnId);
        Accn newAccn = accessionDao.getAccn(accnId);
        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(newAccnPyrs.size(), 1);
        Assert.assertEquals(newAccnPyrs.get(0).getPyrId(), pyr.getPyrId());

        // Verify patient/demo and insured demo info is updated
        logger.info("Verifying patient/demo and insured demo info is updated, accnId=" + accnId);

        Assert.assertEquals(newAccn.getPtFNm(), patientFNm);
        Assert.assertEquals(newAccn.getPtLNm(), patientLNm);
        Assert.assertEquals(newAccn.getPtAddr1(), patientAddr1);
        Assert.assertEquals(newAccn.getPtAddr2(), patientAddr2);
        Assert.assertEquals(newAccn.getPtSex(), patientSex);
        Assert.assertEquals(newAccn.getPtCity(), patientCity);
        Assert.assertEquals(newAccn.getPtZipId(), patientZipId);
        Assert.assertEquals(newAccnPyrs.get(0).getInsFNm(), insuredFNm);
        Assert.assertEquals(newAccnPyrs.get(0).getInsLNm(), insuredLNm);
        Assert.assertEquals(newAccnPyrs.get(0).getInsAddr1(), insuredAddr1);
        Assert.assertEquals(newAccnPyrs.get(0).getInsAddr2(), insuredAddr2);
        Assert.assertEquals(newAccnPyrs.get(0).getInsSex(), insuredSex);
        Assert.assertEquals(newAccnPyrs.get(0).getInsCity(), insuredCity);
        Assert.assertEquals(newAccnPyrs.get(0).getInsZipId(), insuredZipId);

        // Verify patient/demo and insured demo info is updated
        logger.info("Verifying patient/demo and insured demo info is updated in PATIENT_PHI table, accnId=" + accnId);
        Pt pt = rpmDao.getPtPhi(testDb, accnId);
        Assert.assertEquals(pt.getPtFNm(), patientFNm);
        Assert.assertEquals(pt.getPtLNm(), patientLNm);
        Assert.assertEquals(pt.getPtAddr1(), patientAddr1);
        Assert.assertEquals(pt.getPtAddr2(), patientAddr2);
        Assert.assertEquals(pt.getPtCity(), patientCity);

        logger.info("Verifying accession patient payor eligibility status is updated, accnId=" + accnId);
        List<PtPyr> ptPyrs = accessionDao.getPtPyrs(newAccn.getPtSeqId(), newAccn.getDos());
        boolean isPtPyrFound = false;
        Assert.assertEquals(ptPyrs.size(), 1);
        for (PtPyr ptPyr : ptPyrs)
        {
            if (ptPyr.getPyrId() == ptPyr.getPyrId())
            {
                Assert.assertEquals(ptPyr.getPyrPrio(), 1, "Expected ptPyr pyrPrio to be primary");
                Assert.assertEquals(ptPyr.getSubsId(), newAccnPyrs.get(0).getSubsId(), "Expected same subsId");
                isPtPyrFound = true;
            }
        }
        Assert.assertTrue(isPtPyrFound, "Patient payor was not found");

        logger.info("Verifying no new errors added, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        verifyEligTransLog(accnEligHists.get(0));

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));
    }

    @Test(priority = 1, description = "Default Svc Type code is used if Additional svc_type_code is not in 271 file")
    @Parameters({"accnId", "pyrAbbrv", "additionalSvcTyps", "defaultSvcTypCd", "accnCntct"})
    public void testXPR_1222(String accnId, String pyrAbbrv, String additionalSvcTyps, String defaultSvcTypCd, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: XPR-1222, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);

        logger.info("Make sure there is default and additional service type codes in pyr_elig for pyrAbbrv = " + pyrAbbrv);

        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        int origPyrId = origPyr.getPyrId();
        List<PyrElig> pyrElig = rpmDao.getPyrEligByPyrIdEligSvcId(testDb, origPyrId,  EligMap.XIFIN);
        Assert.assertNotNull(pyrElig);
        Assert.assertEquals(pyrElig.size(), 1);
        Assert.assertEquals(pyrElig.get(0).getAddtlSvcTyps(),additionalSvcTyps);
        Assert.assertEquals(pyrElig.get(0).getDefaultSvcTypCd(), defaultSvcTypCd);

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

      //  logger.info("Verifying accession is priced or zbal, accnId=" + accnId + ", staId=" + accn.getStaId());
      //  Assert.assertTrue(ACCN_STA_TYP_PRICED_OR_ZBAL.contains(accn.getStaId()));

        loadAccession(accnId);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        // Verify the primary payor is correct
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertTrue(accn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), origPyrId);
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);
        Assert.assertEquals(accnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
        Assert.assertNotNull(accnPyrs.get(0).getEligChkDt());

        verifyEligTransLog(accnEligHists.get(0), EligMap.ELIG_STA_TYP_ELIGIBLE, defaultSvcTypCd);

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));
    }

    @Test(priority = 1, description = "No Svc Type code EB* segment is used if Additional svc_type_code and Default are not in 271 file")
    @Parameters({"accnId", "pyrAbbrv", "additionalSvcTyps", "defaultSvcTypCd", "accnCntct"})
    public void testXPR_1223(String accnId, String pyrAbbrv, String additionalSvcTyps, String defaultSvcTypCd, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: XPR-1223, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        logger.info("Make sure there is default and additional service type codes in pyr_elig for pyrAbbrv = " + pyrAbbrv);

        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        int origPyrId = origPyr.getPyrId();
        List<PyrElig> pyrElig = rpmDao.getPyrEligByPyrIdEligSvcId(testDb, origPyrId,  EligMap.XIFIN);
        Assert.assertNotNull(pyrElig);
        Assert.assertEquals(pyrElig.size(), 1);
        Assert.assertEquals(pyrElig.get(0).getAddtlSvcTyps(),additionalSvcTyps);
        Assert.assertEquals(pyrElig.get(0).getDefaultSvcTypCd(), defaultSvcTypCd);

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

        loadAccession(accnId);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        // Verify the primary payor is correct
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        //   accessionDetail.setAccnId(accnId, wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertTrue(accn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), origPyrId);
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);
        Assert.assertEquals(accnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
        Assert.assertNotNull(accnPyrs.get(0).getEligChkDt());

        verifyEligTransLog(accnEligHists.get(0), EligMap.ELIG_STA_TYP_ELIGIBLE, defaultSvcTypCd);

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));

    }
    @Test(priority = 1, description = "Eligibility check is ELIGIBLE, Pyr is translated, SubId updated from REF*1W if Subs ID Type set Member Id Number")
    @Parameters({"accnId", "origPyrAbbrv", "newPyrAbbrv", "origSubId", "subIdFromRef1W", "accnCntct"})
    public void testXPR_1617(String accnId, String origPyrAbbrv, String newPyrAbbrv, String origSubId, String subIdFromRef1W, String accnCntct) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: XPR-1617, accnId=" + accnId + ", pyrAbbrv=" + origPyrAbbrv);
        logger.info("Verifying payor eligibility configuration, accnId=" + accnId + ", pyrAbbrv=" + origPyrAbbrv);
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
        logger.info("Verifying that Subscriber Id update is enabled");
        Assert.assertTrue(pyrElig.getIsUpdateInsSubsId());

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        List<AccnPyr> origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        verifyAccnEligDataIsCleared(accessionDao.getAccnEligHistByAccnId(accnId), origAccnPyrs);
        logger.info("Load accn on Accn Detail, accnId=" + accnId);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        if(!accessionDetail.primaryPayorAbbrText().getText().equals(pyr.getName()))
        {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + accessionDetail.primaryPayorAbbrText().getText() + ", newPyrId=" + pyr.getName());
            setOriginalPyrToAccn(accessionDetail, accnId, origPyrAbbrv);
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        }

        logger.info("Make sure original subscriber Id is different from 271 file subscriber Id REF*IG");
        if (!org.apache.commons.lang.StringUtils.equals(accessionDetail.primaryPyrSubsIdInput().getAttribute("value"), origSubId))
        {
            accessionDetail.clearPrimaryPyrSubsId();
            accessionDetail.setPrimaryPyrSubsId(origSubId);
            accessionDetail.clickSave();
        }

        logger.info("Forcing accession to reprice, accnId=" + accnId);
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        Accn origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + origAccn.getStaId());
        Assert.assertEquals(origAccn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for the eligibility check
        List<AccnEligHist> accnEligHists = waitForEligibilityCheck(accnId);
        Pyr newPyr = rpmDao.getPyrByPyrAbbrv(testDb, newPyrAbbrv);
        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), pyr.getPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);

        logger.info("Create accession payor record, accnId=" + accnId);
        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(newAccnPyrs.size(), 1);

        logger.info("Make sure pyr is updated=newPyr - " + newPyr);
        Assert.assertEquals(newAccnPyrs.get(0).getPyrId(), newPyr.getPyrId());

        logger.info("Verifying patient payor subscriber ID is updated from REF*1W =" + newAccnPyrs.get(0).getSubsId());
        List<PtPyr> ptPyrs = accessionDao.getPtPyrs(origAccn.getPtSeqId(), origAccn.getDos());
        Assert.assertEquals(ptPyrs.size(), 1);
        boolean isPtPyrFound = false;
        for (PtPyr ptPyr : ptPyrs)
        {
            if (ptPyr.getPyrId() == newPyr.getPyrId())
            {
                Assert.assertEquals(ptPyr.getPyrPrio(), 1, "Expected ptPyr pyrPrio to be primary");
                Assert.assertEquals(ptPyr.getSubsId(), newAccnPyrs.get(0).getSubsId(), "Expected same subsId");
                isPtPyrFound = true;
            }
        }
        Assert.assertTrue(isPtPyrFound, "Patient payor was not found");

        logger.info("Verifying payor delete record");
        AccnPyrDel accnPyrDel = rpmDao.getAccnPyrDelByAccnAndPyrId(null, origAccn.getAccnId(), pyr.getPyrId());
        Assert.assertEquals(accnPyrDel.getPyrId(), pyr.getPyrId());
        Assert.assertEquals(accnPyrDel.getSubId(), origSubId);

        // Verify patient/demo and insured demo info is updated
        logger.info("Verifying subscriber ID is updated from REF*1W =" + newAccnPyrs.get(0).getSubsId());
        Assert.assertEquals(newAccnPyrs.get(0).getSubsId(), subIdFromRef1W);
        Assert.assertNotEquals(newAccnPyrs.get(0).getSubsId(), origSubId);

        verifyEligTransLog(accnEligHists.get(0));

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));
    }

    @Test(priority = 1, description = "EB date consideration with system setting 3300 populated. Whether DOS is in range determines if translation occurs")
    @Parameters({"accnId", "origPyrAbbrv", "newPyrAbbrv"})
    public void translationEbDateConsideration(String accnId, String origPyrAbbrv, String newPyrAbbrv) throws Exception
    {
        logger.info("Starting Test Case: translationEbDateConsideration, accnId=" + accnId + ", pyrAbbrv=" + origPyrAbbrv);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        List<AccnPyr> origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        verifyAccnEligDataIsCleared(accessionDao.getAccnEligHistByAccnId(accnId), origAccnPyrs);

        //end of EB date range
        updateAccnDos(accnId, DateConversion.getDate("20230418"));

        repriceAccnForTranslation(accnId, origPyrAbbrv);

        // Wait for the eligibility check
        List<AccnEligHist> accnEligHists = waitForEligibilityCheck(accnId);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        Pyr newPyr = rpmDao.getPyrByPyrAbbrv(testDb, newPyrAbbrv);

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated and that translation occurred, accnId=" + accnId);
        Assert.assertEquals(accnEligHists.size(), 1, "Expected AccnEligHist to have 1 record");
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), pyr.getPyrId(), "Expected AccnEligHist pyr to be newPyr");
        Assert.assertEquals(accnEligHists.get(0).getTranslatedPyrId().intValue(), newPyr.getPyrId(), "Expected translated payor");
        Assert.assertNotNull(accnEligHists.get(0).getTransDt(), "Expected transDt to not be null");
        Assert.assertNotNull(accnEligHists.get(0).getTransId(), "Expected transId to not be null");
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE, "Expected Eligible response");

        logger.info("Create accession payor record, accnId=" + accnId);
        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(newAccnPyrs.size(), 1, "Expected 1 AccnPyr record");

        logger.info("Make sure pyr is updated=newPyr - " + newPyr);
        Assert.assertEquals(newAccnPyrs.get(0).getPyrId(), newPyr.getPyrId(), "Expected payor to be updated to newPyr");


        //Reset and recheck elig with SS populated, preventing translation rule
        logger.info("Beginning 2nd Eligibility Check, accnId=" + accnId);

        cleanUpAccn(accnId);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        verifyAccnEligDataIsCleared(accessionDao.getAccnEligHistByAccnId(accnId), origAccnPyrs);

        //one day after EB date range, should no longer translate
        updateAccnDos(accnId, DateConversion.getDate("20230419"));

        repriceAccnForTranslation(accnId, origPyrAbbrv);

        accnEligHists = waitForEligibilityCheck(accnId);

        logger.info("Verifying accession eligibility history is updated and shows no translation, accnId=" + accnId);
        Assert.assertEquals(accnEligHists.size(), 1, "Expected 1 AccnEligHist record");
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), pyr.getPyrId(), "Expected AccnEligHist pyr to be origPyr");
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId(), "Expected no translation to occur");
        Assert.assertNotNull(accnEligHists.get(0).getTransDt(), "Expected transDt to not be null");
        Assert.assertNotNull(accnEligHists.get(0).getTransId(), "Expected transId to not be null");
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE, "Expected Eligible response");

        origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(origAccnPyrs.size(), 1, "Expected 1 AccnPyr record");

        logger.info("Make sure pyr is not updated, origPyr=" + pyr);
        Assert.assertEquals(origAccnPyrs.get(0).getPyrId(), pyr.getPyrId(), "Expected AccnPyr to be origPyr");

    }

    @Test(priority = 1, description = "Eligibility check is ELIGIBLE, No Transl Rule, SubId update setup-NM109 is used when Subs ID Type Fields available")
    @Parameters({"accnId", "origPyrAbbrv", "origSubId", "subIdFrom271", "accnCntct"})
    public void testXPR_1618(String accnId, String origPyrAbbrv, String origSubId, String subIdFrom271, String accnCntct) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: XPR-1618, accnId=" + accnId + ", pyrAbbrv=" + origPyrAbbrv);
        logger.info("Verifying payor eligibility configuration, accnId=" + accnId + ", pyrAbbrv=" + origPyrAbbrv);
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
        logger.info("Verifying that Subscriber Id update is enabled");
        Assert.assertTrue(pyrElig.getIsUpdateInsSubsId());

        logger.info("Verifying There is no Payor Translation Rule exists");
        List <EligResp> eligResp =  rpmDao.getEligRespByPyrId(testDb, pyr.getPyrId());
        Assert.assertEquals(eligResp.size(),0);
        logger.info("Load accn on Accn Detail, accnId=" + accnId);
        List<AccnPyr> origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        verifyAccnEligDataIsCleared(accessionDao.getAccnEligHistByAccnId(accnId), origAccnPyrs);

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        if(!accessionDetail.primaryPayorAbbrText().getText().equals(pyr.getName()))
        {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + accessionDetail.primaryPayorAbbrText().getText() + ", newPyrId=" + pyr.getName());
            setOriginalPyrToAccn(accessionDetail, accnId, origPyrAbbrv);
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        }

        logger.info("Make sure original subscriber Id is different from 271 file subscriber Id REF*IG");
        if (!org.apache.commons.lang.StringUtils.equals(accessionDetail.primaryPyrSubsIdInput().getAttribute("value"), origSubId))
        {
            accessionDetail.clearPrimaryPyrSubsId();
            accessionDetail.setPrimaryPyrSubsId(origSubId);
            accessionDetail.clickSave();
            WebElement okBtnInPtDemoUpdate = accessionDetail.okBtnInPtDemoUpdate();
            if (okBtnInPtDemoUpdate != null && okBtnInPtDemoUpdate.isDisplayed())
            {
                okBtnInPtDemoUpdate.click();
            }
        }

        logger.info("Forcing accession to reprice, accnId=" + accnId);
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        Accn origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + origAccn.getStaId());
        Assert.assertEquals(origAccn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for the eligibility check
        List<AccnEligHist> accnEligHists = waitForEligibilityCheck(accnId);

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        Assert.assertEquals(accnEligHists.size(), 1, "Expected accnEligHist records were not found,");
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), pyr.getPyrId());
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);

        logger.info("Create accession payor record, accnId=" + accnId);
        //  Accn newAccn = accessionDao.getAccn(accnId);
        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(newAccnPyrs.size(), 1);

        logger.info("Make sure pyr is not updated = newPyr - " + newAccnPyrs.get(0).getPyrAbbrv());
        Assert.assertEquals(newAccnPyrs.get(0).getPyrId(), pyr.getPyrId());

        logger.info("Verifying patient payor subscriber ID is updated from NM109 =" + newAccnPyrs.get(0).getSubsId());
        List<PtPyr> ptPyrs = accessionDao.getPtPyrs(origAccn.getPtSeqId(), origAccn.getDos());
        Assert.assertEquals(ptPyrs.size(), 1);
        boolean isPtPyrFound = false;
        for (PtPyr ptPyr : ptPyrs)
        {
            if (ptPyr.getPyrId() == pyr.getPyrId())
            {
                Assert.assertEquals(ptPyr.getPyrPrio(), 1, "Expected ptPyr pyrPrio to be primary");
                Assert.assertEquals(ptPyr.getSubsId(), newAccnPyrs.get(0).getSubsId(), "Expected same subsId");
                isPtPyrFound = true;
            }
        }
        Assert.assertTrue(isPtPyrFound, "Patient payor was not found");

        logger.info("Verifying payor delete record");
        Exception exception = null;
        try
        {
            AccnPyrDel accnPyrDel = rpmDao.getAccnPyrDelByAccnAndPyrId(null, origAccn.getAccnId(), pyr.getPyrId());
        }
        catch (XifinDataNotFoundException e)
        {
            exception = e;
        }
        Assert.assertNotNull(exception, "Accn Pyr Del should not be found");

        // Verify patient/demo and insured demo info is updated
        logger.info("Verifying subscriber ID is updated from NM109 =" + newAccnPyrs.get(0).getSubsId());
        Assert.assertNotEquals(newAccnPyrs.get(0).getSubsId(), origSubId);
        Assert.assertEquals(newAccnPyrs.get(0).getSubsId(), subIdFrom271);

        verifyEligTransLog(accnEligHists.get(0));

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));
    }

    @Test(priority = 1, description = "No Transl Rule, SubId update setup - Rejected response - SubId is updated")
    @Parameters({"accnId", "origPyrAbbrv", "jurisdPyr", "origSubId", "subIdFrom271", "accnCntct"})
    public void testXPR_1619(String accnId, String origPyrAbbrv, String jurisdPyr, String origSubId, String subIdFrom271, String accnCntct) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: XPR-1619, accnId=" + accnId + ", pyrAbbrv=" + origPyrAbbrv);
        logger.info("Verifying payor eligibility configuration, accnId=" + accnId + ", pyrAbbrv=" + origPyrAbbrv);
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

        logger.info("Load accn on Accn Detail, accnId=" + accnId);

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        if(!accessionDetail.primaryPayorAbbrText().getText().equals(pyr.getName()))
        {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + accessionDetail.primaryPayorAbbrText().getText() + ", newPyrId=" + pyr.getName());
            setOriginalPyrToAccn(accessionDetail, accnId, origPyrAbbrv);
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }
        logger.info("Forcing accession to reprice, accnId=" + accnId);
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        Accn origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + origAccn.getStaId());
        Assert.assertTrue(origAccn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for the eligibility check
        List<AccnEligHist> accnEligHists = waitForEligibilityCheck(accnId);

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId+", eligibility status is Rejected="+(accnEligHists.isEmpty() ? "" : accnEligHists.get(0).getEligStaTypId()));
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

        logger.info("Verifying subscriber ID is updated for primary and jurisdiction Pyr =" + newAccnPyrs.get(0).getSubsId());
        for(AccnPyr accnPyr : newAccnPyrs)
        {
            logger.info("Verifying subscriber ID is updated for Pyr ="+accnPyr.getPyrId());
            Assert.assertNotEquals(accnPyr.getSubsId(), origSubId);
            Assert.assertEquals(accnPyr.getSubsId(), subIdFrom271);
        }

        verifyEligTransLog(accnEligHists.get(0));

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));
    }

    @Test(priority = 1, description = "Client Roster Check - Patient Is On Roster For New Client")
    @Parameters({"accnId", "origPyrAbbrv", "origClnAbbrv", "newPyrAbbrv", "newClnAbbrv", "qTypDescr", "accnCntct"})
    public void testPFER_698(String accnId, String origPyrAbbrv, String origClnAbbrv, String newPyrAbbrv, String newClnAbbrv, String qTypDescr, String accnCntct) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: PFER-698, accnId=" + accnId);
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        Cln origCln = rpmDao.getClnByClnAbbrev(testDb, origClnAbbrv);

        AccnQue currentAccnQue = accessionDao.getCurrentAccnQue(accnId);

        logger.info("Verifying original accession payor and client, accnId=" + accnId +", pyrAbbrv="+origPyr.getPyrAbbrv() + ", clnAbbrv="+origCln.getClnAbbrev());
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        boolean isPayorChanged = false;
        // Verify the client is correct
        if (!accessionDetail.clnIdInput().getAttribute("value").equals(origCln.getClnAbbrev()))
        {
            logger.info("Updating client, accnId=" + accnId + ", currentClnId=" + accessionDetail.clientIdText().getAttribute("value") + ", newClnId=" + origCln.getClnAbbrev());

            setOriginalClnToAccn(accessionDetail, accnId, origCln.getClnAbbrev());
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }
        // Verify the primary payor is correct
        if (!accessionDetail.primaryPayorAbbrText().getText().equals(origPyr.getName()))
        {
            logger.info("Updating primary payor, accnId=" + accnId + ", currentPyr=" + accessionDetail.primaryPayorAbbrText().getText() + ", newPyr=" + origPyr.getName());
            setOriginalPyrToAccn(accessionDetail, accnId, origPyr.getPyrAbbrv());
            isPayorChanged = true;
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }
        if (!isPayorChanged)
        {
            logger.info("Forcing accession to reprice, accnId=" + accnId);
            accessionDetail.clickRepriceCheckbox(wait);
        }
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        Accn origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + origAccn.getStaId());
        Assert.assertTrue(origAccn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for the eligibility check
        logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));
        logger.info("Verifying post-eligibility queue, accnId=" + accnId + ", qTypDescr=" + qTypDescr);
        int qTyp = accessionDao.getQTypByDescr(qTypDescr);
        Assert.assertEquals(accessionDao.getCurrentAccnQueQTyp(accnId), qTyp);
        logger.info("Verifying accession went through eligibility queue one time accnId=" + accnId);
        int eligQCnt = 0;
        for (AccnQue accnQue : accessionDao.getAccnQueListByAccnIdMinQCnt(accnId, currentAccnQue.getQCnt()+1))
        {
            if (accnQue.getQTyp() == AccnStatusMap.Q_ELIG)
            {
                eligQCnt++;
            }
        }
        Assert.assertEquals(eligQCnt, 1);

        logger.info("Verifying post-eligibility client, accnId=" + accnId + ", newClnAbbrv=" + newClnAbbrv);
        Cln newCln = rpmDao.getClnByClnAbbrev(testDb, newClnAbbrv);
        Accn newAccn = accessionDao.getAccn(accnId);
        Assert.assertEquals(newAccn.getClnId(), newCln.getClnId());

        logger.info("Verifying post-eligibility payor, accnId=" + accnId + ", newPyrAbbrv=" + newPyrAbbrv);
        Pyr newPyr = rpmDao.getPyrByPyrAbbrv(testDb, newPyrAbbrv);
        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertFalse(newAccnPyrs.isEmpty());
        Assert.assertEquals(newAccnPyrs.get(0).getPyrId(), newPyr.getPyrId());

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));
    }


    @Test(priority = 1, description = "Payor Roster Check Is Successful, original Pyr is translated to the new Pyr")
    @Parameters({"accnId", "originalPyrAbbrv", "newPyrAbbrv", "subId", "eligSvcId", "accnCntct"})
    public void testPFER_700(String accnId, String originalPyrAbbrv, String newPyrAbbrv, String subId, String eligSvcId, String accnCntct) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: PFER-700, accnId=" + accnId + ", originalPyrAbbrv=" + originalPyrAbbrv);
        logger.info("Clear Accn pyr and proc errs = " + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(testDb,accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);
        QValidateAccn qValidateAccn = rpmDao.getQValidateAccnByAccnId(null, accnId);
        if (qValidateAccn == null)
        {
            qValidateAccn = new QValidateAccn();
            qValidateAccn.setAccnId(accnId);
        }
        qValidateAccn.setPriority(3);
        qValidateAccn.setValidateTypId(16);
        qValidateAccn.setIsErr(false);
        accessionDao.setQValidateAccn(qValidateAccn);
        Assert.assertTrue(isOutOfValidationQueue(accnId, QUEUE_WAIT_TIME_MS));
        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME_MS));
        Accn accn = accessionDao.getAccn(accnId);
        logger.info("Make sure elig service is a Roster service= " + eligSvcId);
        EligSvc rosterEligSvc = rpmDao.getEligSvcByEligSvcDesrc(testDb, eligSvcId);
        Assert.assertTrue(rosterEligSvc.getClassname().contains("Roster"));

        logger.info("Make sure payor is setup with Roster elig service = " + originalPyrAbbrv);
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, originalPyrAbbrv);
        int origPyrId = origPyr.getPyrId();

        List<PyrElig> pyrElig = rpmDao.getPyrElig(testDb, origPyrId);
        Assert.assertNotNull(pyrElig);
        Assert.assertEquals(pyrElig.size(), 1);
        Assert.assertEquals(pyrElig.get(0).getEligSvcId(), rosterEligSvc.getEligSvcId());

        logger.info("Make sure elig_payor_roster record exist for subscriberId on the accn with new PyrId, " +
                "elig service - roster, DOS on the accn after effective date, no expiration date");
        List<EligPyrRoster> eligPyrRosterRecords = rpmDao.getEligPyrRosterBySubscriberId(testDb, subId);
        Assert.assertEquals(eligPyrRosterRecords.size(), 1);
        EligPyrRoster eligPyrRoster = eligPyrRosterRecords.get(0);

        Assert.assertEquals(eligPyrRoster.getEligSvcId(), rosterEligSvc.getEligSvcId());
        Assert.assertEquals(eligPyrRoster.getData1(), newPyrAbbrv);
        Assert.assertEquals(eligPyrRoster.getData2(), rosterEligSvc.getDescr());
        Assert.assertNull(eligPyrRoster.getExpDt());


        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        // Verify the primary payor is correct
        if(!accessionDetail.primaryPayorAbbrText().getText().equals(origPyr.getName()))
        {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + accessionDetail.primaryPayorAbbrText().getText() + ", newPyrId=" + origPyr.getName());
            setOriginalPyrToAccn(accessionDetail, accnId, originalPyrAbbrv);
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertTrue(accn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        logger.info("Verifying accession pyr is changed to the new Pyr, newPyrAbbrv=" + newPyrAbbrv);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Pyr newPyr = rpmDao.getPyrByPyrAbbrv(testDb, newPyrAbbrv);
        Assert.assertEquals(accnPyrs.get(0).getPyrId(), newPyr.getPyrId());

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);

        Assert.assertEquals(accnEligHists.size(), 1);
        AccnEligHist accnEligHist = accnEligHists.get(0);
        logger.info("Accession eligibility history is updated, accnEligHist=" + accnEligHist);
        Assert.assertEquals(accnEligHist.getPyrId(), origPyrId);
        Assert.assertTrue(accnEligHist.getTranslatedPyrId()== newPyr.getPyrId());
        Assert.assertNull(accnEligHist.getTransId());
        Assert.assertNotNull(accnEligHist.getTransDt());
        Assert.assertEquals(accnEligHist.getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));
    }

    @Test(priority = 1, description = "Payor Roster Check Is Ineligible, DOS before the Effective Date - Ineligible error added")
    @Parameters({"accnId", "originalPyrAbbrv", "newPyrAbbrv", "subId", "eligSvcId"})
    public void testPFER_701(String accnId, String originalPyrAbbrv, String newPyrAbbrv, String subId, String eligSvcId) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: PFER-701, accnId=" + accnId + ", originalPyrAbbrv=" + originalPyrAbbrv);
        logger.info("Clear Accn pyr and proc errs = " + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(testDb,accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);
        QValidateAccn qValidateAccn = rpmDao.getQValidateAccnByAccnId(null, accnId);
        if (qValidateAccn == null)
        {
            qValidateAccn = new QValidateAccn();
            qValidateAccn.setAccnId(accnId);
        }
        qValidateAccn.setPriority(3);
        qValidateAccn.setValidateTypId(16);
        qValidateAccn.setIsErr(false);
        accessionDao.setQValidateAccn(qValidateAccn);
        Assert.assertTrue(isOutOfValidationQueue(accnId, QUEUE_WAIT_TIME_MS));
        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME_MS));
        Accn accn = accessionDao.getAccn(accnId);
        logger.info("Make sure elig service is a Roster service= " + eligSvcId);
        EligSvc rosterEligSvc = rpmDao.getEligSvcByEligSvcDesrc(testDb, eligSvcId);
        Assert.assertTrue(rosterEligSvc.getClassname().contains("Roster"));

        logger.info("Make sure payor is setup with Roster elig service = " + originalPyrAbbrv);
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, originalPyrAbbrv);
        int origPyrId = origPyr.getPyrId();

        List<PyrElig> pyrElig = rpmDao.getPyrElig(testDb, origPyrId);
        Assert.assertNotNull(pyrElig);
        Assert.assertEquals(pyrElig.size(), 1);
        Assert.assertEquals(pyrElig.get(0).getEligSvcId(), rosterEligSvc.getEligSvcId());

        logger.info("Make sure elig_payor_roster record exist for subscriberId on the accn with new PyrId, " +
                "elig service - roster, DOS on the accn BEFORE effective date, no expiration date");
        List<EligPyrRoster> eligPyrRosterRecords = rpmDao.getEligPyrRosterBySubscriberId(testDb, subId);
        Assert.assertEquals(eligPyrRosterRecords.size(), 1);
        EligPyrRoster eligPyrRoster = eligPyrRosterRecords.get(0);

        Assert.assertEquals(eligPyrRoster.getEligSvcId(), rosterEligSvc.getEligSvcId());
        Assert.assertTrue(eligPyrRoster.getEffDt().compareTo(accn.getDos()) > 0);
        Assert.assertEquals(eligPyrRoster.getData1(), newPyrAbbrv);
        Assert.assertEquals(eligPyrRoster.getData2(), rosterEligSvc.getDescr());
        Assert.assertNull(eligPyrRoster.getExpDt());

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        verifyAccnEligDataIsCleared(accnEligHists, origAccnPyrs);

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        if(!accessionDetail.primaryPayorAbbrText().getText().equals(origPyr.getName()))
        {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + accessionDetail.primaryPayorAbbrText().getText() + ", newPyrId=" + origPyr.getName());
            setOriginalPyrToAccn(accessionDetail, accnId, originalPyrAbbrv);
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnIdPyrPrio(accnId, MiscMap.ACCN_PYR_PRIO_ONE, false).isEmpty());

        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertTrue(accn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        logger.info("Verifying accession pyr is changed to the new Pyr, newPyrAbbrv=" + newPyrAbbrv);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), origAccnPyrs.size());
        Pyr newPyr = rpmDao.getPyrByPyrAbbrv(testDb, newPyrAbbrv);
        Assert.assertNotEquals(accnPyrs.get(0).getPyrId(), newPyr.getPyrId());

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);

        Assert.assertEquals(accnEligHists.size(), 1, "Expected accnEligHist was not found,");
        AccnEligHist accnEligHist = accnEligHists.get(0);
        logger.info("Accession eligibility history is updated, accnEligHist=" + accnEligHist);
        Assert.assertEquals(accnEligHist.getPyrId(), origPyrId, "Expected different payor in accnEligHist,");
        Assert.assertNull(accnEligHist.getTranslatedPyrId(), "Expected no translation in accnEligHist,");
        Assert.assertNull(accnEligHist.getTransId(), "Expected transId in accnEligHist,");
        Assert.assertNotNull(accnEligHist.getTransDt(), "Expected transDt in accnEligHist,");
        Assert.assertEquals(accnEligHist.getEligStaTypId(), EligMap.ELIG_STA_TYP_INELIGIBLE, "Expected INELIGIBLE status in accnEligHist,");

        logger.info("Make sure Ineligible error is added to Accession");
        List<AccnPyrErr> accnPyrErrs = accessionDao.getAccnPyrErrsByAccnIdPyrPrio(accnId, accnPyrs.get(0).getPyrPrio(), false);
        Assert.assertEquals(accnPyrErrs.size(),1);
        Set<String> actualErrors = new HashSet<>();
        for (AccnPyrErr ape : accnPyrErrs)
        {
            actualErrors.add(rpmDao.getErrCd(testDb, ape.getErrCd()).getAbbrev());
        }
        Set<String> expectedErrors = new HashSet<String>(Arrays.asList(DbErrorMap.INELIGIBLE));
        Assert.assertEquals(actualErrors, expectedErrors);

    }

    @Test(priority = 1, description = "Hospital Admit Check Is Successful - HOSPITALADMIT is added")
    @Parameters({"accnId", "originalPyrAbbrv", "subId", "eligSvcId", "accnCntct"})
    public void testPFER_702(String accnId, String originalPyrAbbrv, String subId, String eligSvcId, String accnCntct) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: PFER-702, accnId=" + accnId + ", originalPyrAbbrv=" + originalPyrAbbrv);
        logger.info("Clear Accn_ pyr and proc errs = " + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(testDb,accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);
        rpmDao.deleteAccnErrByAccnId(testDb, accnId);
        QValidateAccn qValidateAccn = rpmDao.getQValidateAccnByAccnId(null, accnId);
        if (qValidateAccn == null)
        {
            qValidateAccn = new QValidateAccn();
            qValidateAccn.setAccnId(accnId);
        }
        qValidateAccn.setPriority(3);
        qValidateAccn.setValidateTypId(16);
        qValidateAccn.setIsErr(false);
        accessionDao.setQValidateAccn(qValidateAccn);
        Assert.assertTrue(isOutOfValidationQueue(accnId, QUEUE_WAIT_TIME_MS));
        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME_MS));
        Accn accn = accessionDao.getAccn(accnId);
        logger.info("Make sure elig service is a Xifin service= " + eligSvcId);
        EligSvc eligSvc = rpmDao.getEligSvcByEligSvcDesrc(testDb, eligSvcId);
        Assert.assertTrue(eligSvc.getClassname().contains("Xifin"));

        logger.info("Make sure payor is setup with Xifin elig service = " + originalPyrAbbrv);
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, originalPyrAbbrv);
        int origPyrId = origPyr.getPyrId();

        List<PyrElig> pyrElig = rpmDao.getPyrElig(testDb, origPyrId);
        Assert.assertNotNull(pyrElig);
        Assert.assertEquals(pyrElig.size(), 1);
        Assert.assertEquals(pyrElig.get(0).getEligSvcId(), eligSvc.getEligSvcId());

        logger.info("Make sure hospital_admit_check record exist for subscriberId on the accn with new Admit Date after DOS on the accn, DOS="+accn.getDos());
        List<HospitalAdmitCheck> hospitalAdmitChecks = rpmDao.getHospitalAdmitCheckBySubsId(testDb, subId);
        Assert.assertEquals(hospitalAdmitChecks.size(), 1);

        Assert.assertTrue(hospitalAdmitChecks.get(0).getAdmitDt().compareTo(accn.getDos()) > 0);

        logger.info("Make sure Pyr is setup with PYR.HOSPITAL_ADMIT_CHECK_IN_HOURS=72 hours");
        Assert.assertEquals(origPyr.getHospitalAdmitCheckInHours(),72);

        logger.info("Make sure Client is setup with CLN.B_USE_HOSPITAL_ADMIT_CHECK=1, cln="+accn.getClnId());
        Assert.assertTrue(rpmDao.getClnByClnId(testDb, accn.getClnId()).getIsUseHospitalAdmitCheck());

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        if(!accessionDetail.primaryPayorAbbrText().getText().equals(origPyr.getName()))
        {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + accessionDetail.primaryPayorAbbrText().getText() + ", newPyrId=" + origPyr.getName());
            setOriginalPyrToAccn(accessionDetail, accnId, originalPyrAbbrv);
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        logger.info("Verifying accession pyr is NOT changed");
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getPyrId(), origPyr.getPyrId());

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);

        Assert.assertEquals(accnEligHists.size(), 1);
        AccnEligHist accnEligHist = accnEligHists.get(0);
        logger.info("Accession eligibility history is updated, accnEligHist=" + accnEligHist);
        Assert.assertEquals(accnEligHist.getPyrId(), origPyrId);
        Assert.assertNull(accnEligHist.getTranslatedPyrId());
        Assert.assertNotNull(accnEligHist.getTransId());
        Assert.assertEquals(accnEligHist.getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);

        logger.info("Make sure HOSPITAL_ADMIT_CHECK error is added to Accession");
        List<AccnErr> accnErr = accessionDao.getAccnErrsByAccnId(accnId);
        Assert.assertEquals(accnErr.size(),1, "Expected accnErr for hospital admit check");
        Assert.assertEquals(rpmDao.getErrCd(testDb, accnErr.get(0).getErrCd()).getErrCd(), DbErrorMap.HOSPITAL_ADMIT_CHECK, "Expected hospital admit check error code");

        logger.info("Make sure Accession is in EP Unpriceable or Manual Queue");
        AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
        Assert.assertTrue(accnQue.getQTyp() == AccnStatusMap.Q_EP_UNPRICEABLE || accnQue.getQTyp() == AccnStatusMap.Q_EP_MAN);

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));
    }

    @Test(priority = 1, description = "Hospital Admit Check Is Successful - Eligible, no error added")
    @Parameters({"accnId", "originalPyrAbbrv", "subId", "eligSvcId"})
    public void testPFER_703(String accnId, String originalPyrAbbrv, String subId, String eligSvcId) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: PFER-703, accnId=" + accnId + ", originalPyrAbbrv=" + originalPyrAbbrv);
        logger.info("Clear Accn_ pyr and proc errs = " + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(testDb,accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);
        rpmDao.deleteAccnErrByAccnId(testDb, accnId);
        QValidateAccn qValidateAccn = rpmDao.getQValidateAccnByAccnId(null, accnId);
        if (qValidateAccn == null)
        {
            qValidateAccn = new QValidateAccn();
            qValidateAccn.setAccnId(accnId);
        }
        qValidateAccn.setPriority(3);
        qValidateAccn.setValidateTypId(16);
        qValidateAccn.setIsErr(false);
        accessionDao.setQValidateAccn(qValidateAccn);
        Assert.assertTrue(isOutOfValidationQueue(accnId, QUEUE_WAIT_TIME_MS));
        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME_MS));
        Accn accn = accessionDao.getAccn(accnId);
        logger.info("Make sure elig service is a Xifin service= " + eligSvcId);
        EligSvc eligSvc = rpmDao.getEligSvcByEligSvcDesrc(testDb, eligSvcId);
        Assert.assertTrue(eligSvc.getClassname().contains("Xifin"));

        logger.info("Make sure payor is setup with Xifin elig service = " + originalPyrAbbrv);
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, originalPyrAbbrv);
        int origPyrId = origPyr.getPyrId();

        List<PyrElig> pyrElig = rpmDao.getPyrElig(testDb, origPyrId);
        Assert.assertNotNull(pyrElig);
        Assert.assertEquals(pyrElig.size(), 1);
        Assert.assertEquals(pyrElig.get(0).getEligSvcId(), eligSvc.getEligSvcId());

        logger.info("Make sure hospital_admit_check record exist for subscriberId on the accn with new Admit Date after DOS on the accn, DOS="+accn.getDos());
        List<HospitalAdmitCheck> hospitalAdmitChecks = rpmDao.getHospitalAdmitCheckBySubsId(testDb, subId);
        Assert.assertEquals(hospitalAdmitChecks.size(), 1);

        Assert.assertTrue(hospitalAdmitChecks.get(0).getAdmitDt().compareTo(accn.getDos()) > 0);

        logger.info("Make sure Pyr is setup with PYR.HOSPITAL_ADMIT_CHECK_IN_HOURS=72 hours");
        Assert.assertEquals(origPyr.getHospitalAdmitCheckInHours(),72);

        logger.info("Make sure Client is setup with CLN.B_USE_HOSPITAL_ADMIT_CHECK=1, cln="+accn.getClnId());
        Assert.assertTrue(rpmDao.getClnByClnId(testDb, accn.getClnId()).getIsUseHospitalAdmitCheck());

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        if(!accessionDetail.primaryPayorAbbrText().getText().equals(origPyr.getName()))
        {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + accessionDetail.primaryPayorAbbrText().getText() + ", newPyrId=" + origPyr.getName());
            setOriginalPyrToAccn(accessionDetail, accnId, originalPyrAbbrv);
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        logger.info("Verifying accession pyr is NOT changed");
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getPyrId(), origPyr.getPyrId());

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);

        Assert.assertEquals(accnEligHists.size(), 1);
        AccnEligHist accnEligHist = accnEligHists.get(0);
        logger.info("Accession eligibility history is updated, accnEligHist=" + accnEligHist);
        Assert.assertEquals(accnEligHist.getPyrId(), origPyrId);
        Assert.assertNull(accnEligHist.getTranslatedPyrId());
        Assert.assertNotNull(accnEligHist.getTransId());
        Assert.assertEquals(accnEligHist.getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);

        logger.info("Make sure HOSPITAL_ADMIT_CHECK error is NOT added to Accession");
        Assert.assertEquals(accessionDao.getAccnErrsByAccnId(accnId).size(), 0);

        logger.info("Make sure Accession is NOT in EP Queue");
        AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
        Assert.assertNotEquals(accnQue.getQTyp(),AccnStatusMap.Q_EP_MAN);
    }

    @Test(priority = 1, description = "Payor Roster Check Ineligible error fixed. DOS after the Effective Date and Expiration Date is null ")
    @Parameters({"accnId", "pyrAbbrv", "subId", "eligSvcId"})
    public void testPFER_704(String accnId, String pyrAbbrv, String subId, String eligSvcId) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: PFER-704, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);

        logger.info("Make sure elig service is a Roster service= " + eligSvcId);
        EligSvc rosterEligSvc = rpmDao.getEligSvcByEligSvcDesrc(testDb, eligSvcId);
        Assert.assertTrue(rosterEligSvc.getClassname().contains("Roster"));

        logger.info("Make sure payor is setup with Roster elig service = " + pyrAbbrv);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        int pyrId = pyr.getPyrId();

        List<PyrElig> pyrElig = rpmDao.getPyrElig(testDb, pyrId);
        Assert.assertNotNull(pyrElig);
        Assert.assertEquals(pyrElig.size(), 1);
        Assert.assertEquals(pyrElig.get(0).getEligSvcId(), rosterEligSvc.getEligSvcId());

        logger.info("Make sure Accession has Ineligible pyr error");
        boolean isErrorFound = false;
        boolean isErrorUnfixed = false;
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
        for (AccnPyrErr ape : accessionDao.getAccnPyrErrsByAccnIdPyrPrio(accnId, accnPyrs.get(0).getPyrPrio(), true))
        {
            if (StringUtils.equalsIgnoreCase(rpmDao.getErrCd(testDb, ape.getErrCd()).getAbbrev(), DbErrorMap.INELIGIBLE))
            {
                isErrorFound = true;
                if (ape.getFixDt() != null)
                {
                    ape.setFixDt(null);
                    ape.setFixUserId(null);
                    databaseSequenceDao.setValueObject(ape);
                    isErrorUnfixed = true;
                }
                break;
            }
        }
        Assert.assertTrue(isErrorFound, "INELIGIBLE error was not found");
        if (isErrorUnfixed)
        {
            QValidateAccn qValidateAccn = rpmDao.getQValidateAccnByAccnId(null, accnId);
            if (qValidateAccn == null)
            {
                qValidateAccn = new QValidateAccn();
                qValidateAccn.setAccnId(accnId);
            }
            qValidateAccn.setPriority(3);
            qValidateAccn.setValidateTypId(16);
            qValidateAccn.setIsErr(false);
            accessionDao.setQValidateAccn(qValidateAccn);
            Assert.assertTrue(isOutOfValidationQueue(accnId, QUEUE_WAIT_TIME_MS), "Accn is not out of validation queue");
        }
        logger.info("Make sure elig_payor_roster record exist for subscriberId on the accn with PyrId, " +
                "elig service - roster, DOS on the accn after effective date, no expiration date");
        List<EligPyrRoster> eligPyrRosterRecords = rpmDao.getEligPyrRosterBySubscriberId(testDb, subId);
        Assert.assertEquals(eligPyrRosterRecords.size(), 1);
        EligPyrRoster eligPyrRoster = eligPyrRosterRecords.get(0);

        Assert.assertEquals(eligPyrRoster.getEligSvcId(), rosterEligSvc.getEligSvcId());
        Assert.assertEquals(eligPyrRoster.getData1(), pyrAbbrv);
        Assert.assertEquals(eligPyrRoster.getData2(), rosterEligSvc.getDescr());
        Assert.assertNull(eligPyrRoster.getExpDt());


        logger.info("Fixing ineligible error on Accn Detail screen through reprice, accnId=" + accnId);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        Accn accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertTrue(accn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), pyrId);
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNull(accnEligHists.get(0).getTransId());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);

        logger.info("Verifying accn_pyr error has fixed date");
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnIdPyrPrio(accnId, accnPyrs.get(0).getPyrPrio(), false).isEmpty());
    }

    @Test(priority = 1, description = "Eligibility check is REJECTED/RECHK (P Code)")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testXPR_2046(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: XPR-2046, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        simpleDeterminationTest(accnId, pyrAbbrv, EligMap.ELIG_STA_TYP_RECHK_ELIG, null, true, accnCntct);
    }

    private void simpleDeterminationTest(String accnId, String pyrAbbrv, int eligStaTypId, String eligErrorAbbrev, Boolean isStayInEligQue, String accnCntct) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Verifying payor eligibility configuration, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        PyrElig pyrElig = null;
        for (PyrElig pe : rpmDao.getPyrEligByPyrIdEligSvcId(testDb, pyr.getPyrId(), EligMap.XIFIN))
        {
            if (!pe.getIsDeleted() && pe.getIsPreferred())
            {
                pyrElig = pe;
            }
        }
        Assert.assertNotNull(pyrElig);
        // Translation should be disabled
        Assert.assertFalse(pyrElig.getIsTranslationEnabled());
        // All demographic updates should be disabled
        Assert.assertFalse(pyrElig.getIsUpdateInsSubsId());
        Assert.assertFalse(pyrElig.getIsUpdateInsNm());
        Assert.assertFalse(pyrElig.getIsUpdateInsAddr());
        Assert.assertFalse(pyrElig.getIsUpdateInsGender());
        Assert.assertFalse(pyrElig.getIsUpdateInsRelshp());
        Assert.assertFalse(pyrElig.getIsUpdatePtNm());
        Assert.assertFalse(pyrElig.getIsUpdatePtAddr());
        Assert.assertFalse(pyrElig.getIsUpdatePtGender());
        // Days to check elig should be at least 1
        Assert.assertTrue(pyr.getDaysToChkElig() > 0);

        Accn origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying accession status is priced or zbal, accnId=" + accnId + ", staId=" + origAccn.getStaId());
        Assert.assertTrue(ACCN_STA_TYP_PRICED_OR_ZBAL.contains(origAccn.getStaId()));

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        List<AccnPyr> origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        verifyAccnEligDataIsCleared(accessionDao.getAccnEligHistByAccnId(accnId), origAccnPyrs);

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        if (!accessionDetail.primaryPayorAbbrText().getText().equals(pyr.getName()))
        {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + accessionDetail.primaryPayorAbbrText().getText() + ", newPyrId=" + pyr.getName());
            setOriginalPyrToAccn(accessionDetail, accnId, pyrAbbrv);
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }

        if (eligErrorAbbrev != null)
        {
            logger.info("Fixing the eligibility error, accnId=" + accnId + ", errorAbbrev=" + eligErrorAbbrev);
            try
            {
                accessionDetail.fixUnfixedErrEligAccnDetail();
                accessionDetail.saveAndClear(wait);
                accessionDetail.setAccnId(accnId, wait);
            }
            catch (WebDriverException e)
            {
                logger.debug("Unable to fix expected eligibility error, accnId=" + accnId + ", errorAbbrev=" + eligErrorAbbrev);
            }
        }
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnIdPyrPrio(accnId, 1, false).isEmpty());

        logger.info("Forcing accession to reprice, accnId=" + accnId);
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + origAccn.getStaId());
        Assert.assertTrue(origAccn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        List<AccnEligHist> accnEligHists = waitForEligibilityCheck(accnId);

        // logger.info("Verifying post-eligibility queue, accnId=" + accnId + ", qTyp=" + qTyp);
        // Assert.assertEquals(rpmDao.getCurrentAccnQueQTyp(testDb, accnId), qTyp);

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        Assert.assertEquals(accnEligHists.size(), 1, "Expected accnEligHist was not found,");
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), pyr.getPyrId());
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), eligStaTypId, "Accession has wrong eligStaTyp,");

        // If accession was staying in elig queue, make sure elig status, date last check, checked performed is updated,
        // update the dates on the q_elig record so it moves along on next engine pass.
        if (isStayInEligQue)
        {
            QElig qElig = accessionDao.getQEligByAccnId(accnId);
            logger.info("Making sure qElig data is updated, accnId="+accnId+" qElig="+qElig);
            Assert.assertEquals(qElig.getEligStaTypId(), eligStaTypId);
            if(qElig.getEligStaTypId()!=(EligMap.ELIG_STA_TYP_UNREACHABLE))
            {
                Assert.assertNotNull(qElig.getDtLstChk());
            }
            Assert.assertEquals(qElig.getChksPrfrmd(), 1);

            List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
            logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
            Assert.assertEquals(accnCntcts.size(), 1);
            Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));

            logger.info("Updating eligibility check dates to force accession out of eligibility queue, accnId=" + accnId);
            Date newDt = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1) * (pyr.getDaysToChkElig() + 1));
            accessionDao.updateQEligDates(accnId, newDt, newDt);
            Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");
        }

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        Accn newAccn = accessionDao.getAccn(accnId);
        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(newAccnPyrs.size(), origAccnPyrs.size(), "New accession payor list should be the same size as original accession payor list");
        Assert.assertEquals(newAccnPyrs.get(0).getPyrId(), pyr.getPyrId());

        // Unreachable status will not update accession payor elig fields
        if (eligStaTypId == EligMap.ELIG_STA_TYP_UNREACHABLE)
        {
            Assert.assertEquals(newAccnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_NOT_CHECKED, "Expected eligStaTyp to not be updated,");
            Assert.assertNull(newAccnPyrs.get(0).getEligSvcName());
            Assert.assertNull(newAccnPyrs.get(0).getEligChkDt());
        }
        else
        {
            Assert.assertEquals(newAccnPyrs.get(0).getEligStaTypId(), eligStaTypId);
            Assert.assertEquals(newAccnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
            Assert.assertNotNull(newAccnPyrs.get(0).getEligChkDt());
        }

        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME * 5));
        // Verify patient/demo data not updated
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

        origAccn = accessionDao.getAccn(accnId);
        // logger.info("Verifying post-eligibility queue, accnId=" + accnId + ", qTyp=" + qTyp);

        if(!isStayInEligQue)
        {
            Assert.assertEquals(origAccn.getStaId(), AccnStatusMap.ACCN_STATUS_PRICED);
        }
        if (eligErrorAbbrev == null)
        {
            logger.info("Verifying no new errors added, accnId=" + accnId);
            Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());
        }
        else
        {
            logger.info("Verifying eligibility error is added, accnId=" + accnId + ", errorAbbrev=" + eligErrorAbbrev);
            verifyUnfixedUnbillableAccnPyrErrs(new HashSet<>(Arrays.asList(eligErrorAbbrev)), origAccn, newAccnPyrs.get(0));
        }
    }

    private void repriceAccnForTranslation(String accnId, String origPyrAbbrv) throws Exception {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Verifying payor eligibility configuration, accnId=" + accnId + ", pyrAbbrv=" + origPyrAbbrv);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        PyrElig pyrElig = null;
        for (PyrElig pe : rpmDao.getPyrEligByPyrIdEligSvcId(testDb, pyr.getPyrId(), EligMap.XIFIN))
        {
            if (!pe.getIsDeleted() && pe.getIsPreferred())
            {
                pyrElig = pe;
            }
        }
        Assert.assertNotNull(pyrElig, "Expected pyrElig to not be null");

        logger.info("Load accn on Accn Detail, accnId=" + accnId);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        if(!accessionDetail.primaryPayorAbbrText().getText().equals(pyr.getName()))
        {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + accessionDetail.primaryPayorAbbrText().getText() + ", newPyrId=" + pyr.getName());
            setOriginalPyrToAccn(accessionDetail, accnId, origPyrAbbrv);
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait), "Expected Accn to load successfully");

        }

        logger.info("Forcing accession to reprice, accnId=" + accnId);
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        Accn origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + origAccn.getStaId());
        Assert.assertEquals(origAccn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED, "Expected Accn to back out of pricing");
    }

    protected void setOriginalClnToAccn(AccessionDetail accessionDetail, String accnId, String origClnAbbrv) throws InterruptedException
    {
        WebElement clientIdInput = accessionDetail.clnIdInput();
        wait.until(ExpectedConditions.visibilityOf(clientIdInput));
        Thread.sleep(4000);
        clientIdInput.clear();
        Thread.sleep(4000);
        clientIdInput.sendKeys(origClnAbbrv+ Keys.TAB);
        Thread.sleep(4000);
    }

    protected void setOriginalPyrToAccn(AccessionDetail accessionDetail, String accnId, String origPyrAbbrv) throws InterruptedException
    {
        WebElement primaryPyrId = accessionDetail.primaryPayorIDInput();
        wait.until(ExpectedConditions.visibilityOf(primaryPyrId));
        Thread.sleep(4000);
        primaryPyrId.clear();
        Thread.sleep(4000);
        primaryPyrId.sendKeys(origPyrAbbrv+ Keys.TAB);
        Thread.sleep(4000);
    }

    private boolean isOutOfValidationQueue(String accnId, long maxTime) throws InterruptedException, XifinDataAccessException
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isInQueue = accessionDao.isInValidationQueue(accnId);
        while (isInQueue && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for accession to exit validation queue, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME);
            isInQueue = accessionDao.isInValidationQueue(accnId);
        }
        return !isInQueue;
    }

    private void updateAccnDos(String accnId, Date date) throws Exception {
        Accn accn = accessionDao.getAccn(accnId);

        accn.setDos(date);
        accn.setRcptDt(new Timestamp(date.getTime()));
        accn.setLoadDt(date);
        accn.setFinalRptDt(date);
        accn.setOrigPrcDt(date);
        accn.setFirstFinalRptEntryDt(date);

        accn.setResultCode(ErrorCodeMap.RECORD_FOUND);
        accessionDao.setAccn(accn);
    }
}
