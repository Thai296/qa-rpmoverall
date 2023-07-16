package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnCntct.AccnCntct;
import com.mbasys.mars.ejb.entity.accnEligHist.AccnEligHist;
import com.mbasys.mars.ejb.entity.accnErr.AccnErr;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.eligResp.EligResp;
import com.mbasys.mars.ejb.entity.eligSvc.EligSvc;
import com.mbasys.mars.ejb.entity.hospitalAdmitCheck.HospitalAdmitCheck;
import com.mbasys.mars.ejb.entity.ptPyr.PtPyr;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrElig.PyrElig;
import com.mbasys.mars.eligibility.EligMap;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.DbErrorMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.util.DateConversion;
import org.openqa.selenium.WebDriverException;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EligibilityEngineQFrPendingTest extends EligibilityEngineTest
{

    public static final long THREE_MIN_TIMESTAMP = ((long) 180 * 1000L);

    @Override
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
            updateSystemSetting(SystemSettingMap.SS_FR_PENDING_FOR_ELIG, "True", "1");
            clearDataCache();
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

    @Override
    @BeforeTest(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins", "accnId"})
    public void beforeTest(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, String disableBrowserPlugins, @Optional String accnId)
    {
        try
        {
            logger.info("Running BeforeTest");
            parameters = new TestParameters();
            parameters.setPlatform(platform);
            parameters.setBrowserName(browser);
            parameters.setBrowserVersion(version);
            parameters.setPort(port);
            parameters.setHub(hub);
            parameters.setTimeout(timeout);
            parameters.setSsoUsername(ssoUsername);
            parameters.setSsoPassword(ssoPassword);
            parameters.setDisableBrowserPlugins(disableBrowserPlugins);
            parameters.setAccnId(accnId);
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            // Clean up eligibility history for accn
            cleanUpAccn(accnId);
            daoManagerXifinRpm.setQfrPendingReleaseToLaterByAccndId(accnId, null);
        }
        catch (SkipException e)
        {
            logger.warn("Skipped exception thrown during BeforeTest", e);
        }
        catch (Exception e)
        {
            throw new SkipException("Error running BeforeTest", e);
        }
        finally
        {
            quitWebDriver();
        }
    }

    @Override
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
            updateSystemSetting(SystemSettingMap.SS_FR_PENDING_FOR_ELIG, "False", "0");
            clearDataCache();
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

    @Test(priority = 1, description = "QFrPending-Accn Prim Pyr Translated To Same Pyr - B_check_elig Enabled")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testPFER_992050(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: PFER-992050, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);

        logger.info("Verifying eligibility translation rule(s), accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        List<EligResp> eligResps = rpmDao.getEligRespByPyrId(testDb, rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId());
        Assert.assertEquals(eligResps.size(), 2, "EligResp size was incorrect, eligRespSize=" + eligResps.size());
        Assert.assertEquals(eligResps.get(1).getNewPyrId(), eligResps.get(1).getPyrId(), "EligResp payor was incorrect,");
        Assert.assertTrue(eligResps.get(1).getIsCheckElig(), "Expected isCheckElig to be true,");

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession is in q_fr_pending, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED, "Expected accession status to be reported status,");
        //Pending or reported is a valid status but lets use reported here

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isQFrPendingAccnEligibilityProcessed(accnId, 1, accnPyrs.get(0).getPyrId(), QUEUE_WAIT_TIME), "Accession did not process eligibility,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getPyrId(), accnPyrs.get(0).getPyrId(), "Eligibility history is incorrect,");
        Assert.assertNull(accnEligHists.get(accnEligHists.size()-1).getTranslatedPyrId(), "Eligibility history expected to have no translation, translatedPyrId=" + accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTransId(), "Eligibility history expected to have transId,");
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTransDt(), "Eligibility history expected to have transDt,");
        Date today = DateConversion.getSystemDate();
        Assert.assertEquals((accnEligHists.get(accnEligHists.size()-1).getTransDt().toString()), today.toString(), "Eligibility history expected to have transDt of today, transDt=" + accnEligHists.get(accnEligHists.size()-1).getTransDt() + ", compareDate=" + today);
        Timestamp threeMinAgo = new Timestamp(DateConversion.getNowAsSqlTimestamp().getTime() - THREE_MIN_TIMESTAMP);
        Assert.assertTrue(accnEligHists.get(accnEligHists.size()-1).getAudDt().compareTo(threeMinAgo) > 0, "Eligibility history expected to have audDt in last 3 minutes, transDt=" + accnEligHists.get(accnEligHists.size()-1).getAudDt() + ", compareDate=" + threeMinAgo);
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE, "Eligibility history expected eligible status, eligStaTypId=" + accnEligHists.get(0).getEligStaTypId()+ ",");

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);
        Assert.assertEquals(accnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
        Assert.assertNotNull(accnPyrs.get(0).getEligChkDt());

        logger.info("Verifying no new errors added, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        logger.info("Verifying accnCntct added, accnId=" + accnId);
        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(null, accnId);
        Assert.assertEquals(1, accnCntcts.size(), "Expected one accession contact,");
        Assert.assertEquals(accnCntct, accnCntcts.get(0).getCntctInfo(), "Expected message was not found in accnCntct,");

        logger.info("Verifying no qElig added, accnId=" + accnId);
        Assert.assertFalse(accessionDao.isInEligibilityQueue(accnId), "Accession should not be in the eligibility queue,");

        verifyEligTransLog(accnEligHists.get(accnEligHists.size()-1));
    }

    @Test(priority = 1, description = "QFrPending-Accn Prim Pyr Translated To Different Pyr - B_check_elig Enabled")
    @Parameters({"accnId", "origPyrAbbrv", "newPyrAbbrv", "origSubId", "subIdFromIg", "accnCntct"})
    public void testPFER_992051(String accnId, String origPyrAbbrv, String newPyrAbbrv, String origSubId, String subIdFromIg, String accnCntct) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        Pyr pyr =rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        logger.info("Starting Test Case: PFER-992051, accnId=" + accnId + ", pyrAbbrv=" + origPyrAbbrv);

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
        accessionDetail.saveAndClear(wait);

        cleanUpAccn(accnId);

        logger.info("Verifying eligibility translation rule(s), accnId=" + accnId + ", origPyrAbbrv=" + origPyrAbbrv);
        List<EligResp> eligResps = rpmDao.getEligRespByPyrId(testDb, pyr.getPyrId());
        Assert.assertEquals(eligResps.size(), 4, "EligResp size was incorrect, eligRespSize=" + eligResps.size());
        int translatedPayorId = rpmDao.getPyrByPyrAbbrv(testDb, newPyrAbbrv).getPyrId();
        Assert.assertEquals(eligResps.get(1).getNewPyrId(), translatedPayorId, "Expected translated payor,");
        Assert.assertFalse(eligResps.get(1).getIsCheckElig(), "Expected isCheckElig to be false,");

        Accn accn = accessionDao.getAccn(accnId);

        logger.info("Verifying accession is in q_fr_pending, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED, "Expected accession status to be reported status,");
        //Pending or reported is a valid status but lets use reported here

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isQFrPendingAccnEligibilityProcessed(accnId, 1, translatedPayorId, QUEUE_WAIT_TIME*2), "Accession did not process eligibility,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);

        Assert.assertEquals(accnEligHists.size(), 2, "Eligibility History count is incorrect");
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), pyr.getPyrId(), "Eligibility History Original Payor ID is incorrect for first eligibility check");
        Assert.assertEquals(accnEligHists.get(0).getTranslatedPyrId().intValue(), translatedPayorId, "Eligibility History Translated Payor ID is incorrect for first eligibility check");
        Assert.assertNotNull(accnEligHists.get(0).getTransId(), "Eligibility History Trans ID is empty for first eligibility check");
        Date today = DateConversion.getSystemDate();
        Assert.assertEquals((accnEligHists.get(0).getTransDt().toString()), today.toString(), "Eligibility History Trans Date is incorrect for first eligibility check");
        Timestamp earliestTimeAllowed = new Timestamp(DateConversion.getNowAsSqlTimestamp().getTime() - QUEUE_WAIT_TIME);
        Assert.assertTrue(accnEligHists.get(0).getAudDt().compareTo(earliestTimeAllowed) > 0, "Eligibility History Audit Date is incorrect for first eligibility check");
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE, "Eligibility Status is incorrect for first eligibility check");

        Assert.assertEquals(accnEligHists.get(1).getPyrId(), translatedPayorId, "Eligibility History Original Payor ID is incorrect for second eligibility check");
        Assert.assertNull(accnEligHists.get(1).getTranslatedPyrId(), "Eligibility History Translated Payor ID is incorrect for second eligibility check");
        Assert.assertNotNull(accnEligHists.get(1).getTransId(), "Eligibility History Trans ID is empty for second eligibility check");
        Assert.assertEquals(accnEligHists.get(1).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE, "Eligibility Status is incorrect for second eligibility check");

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getSubsId(), subIdFromIg);
        Assert.assertEquals(translatedPayorId, accnPyrs.get(0).getPyrId(), "Expected payor not found,");
        Assert.assertEquals(accnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE,"Expected status not found,");
        Assert.assertEquals(accnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN, "Expected service not found,");
        Assert.assertNotNull(accnPyrs.get(0).getEligChkDt(), "Unexpected elig check date found for translated payor,");

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

        logger.info("Verifying no new errors added, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        logger.info("Verifying accnCntct added, accnId=" + accnId);
        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(null, accnId);
        Assert.assertEquals(2, accnCntcts.size(), "Expected one accession contact,");
        Assert.assertEquals(accnCntct, accnCntcts.get(0).getCntctInfo(), "Expected message was not found in accnCntct,");

        logger.info("Verifying no qElig added, accnId=" + accnId);
        Assert.assertFalse(accessionDao.isInEligibilityQueue(accnId), "Accession should not be in the eligibility queue,");

        verifyEligTransLog(accnEligHists.get(0));
    }

    @Test(priority = 1, description = "QFrPending-Ineligible status")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testPFER_992052(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: PFER-992052, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);

        logger.info("Verifying eligibility translation rule(s), accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        List<EligResp> eligResps = rpmDao.getEligRespByPyrId(testDb, rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId());
        Assert.assertEquals(eligResps.size(), 0, "EligResp list should be empty, eligRespSize=" + eligResps.size());

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession is in q_fr_pending, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED, "Expected accession status to be reported status,");
        //Pending or reported is a valid status but lets use reported here

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isQFrPendingAccnEligibilityProcessed(accnId, 1, accnPyrs.get(0).getPyrId(), QUEUE_WAIT_TIME), "Accession did not process eligibility,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getPyrId(), accnPyrs.get(0).getPyrId(), "Eligibility history is incorrect,");
        Assert.assertNull(accnEligHists.get(accnEligHists.size()-1).getTranslatedPyrId(), "Eligibility history expected to have no translation, translatedPyrId=" + accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTransId(), "Eligibility history expected to have transId,");
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTransDt(), "Eligibility history expected to have transDt,");
        Date today = DateConversion.getSystemDate();
        Assert.assertEquals((accnEligHists.get(accnEligHists.size()-1).getTransDt().toString()), today.toString(), "Eligibility history expected to have transDt of today, transDt=" + accnEligHists.get(accnEligHists.size()-1).getTransDt() + ", compareDate=" + today);
        Timestamp threeMinAgo = new Timestamp(DateConversion.getNowAsSqlTimestamp().getTime() - THREE_MIN_TIMESTAMP);
        Assert.assertTrue(accnEligHists.get(accnEligHists.size()-1).getAudDt().compareTo(threeMinAgo) > 0, "Eligibility history expected to have audDt in last 3 minutes, transDt=" + accnEligHists.get(accnEligHists.size()-1).getAudDt() + ", compareDate=" + threeMinAgo);
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getEligStaTypId(), EligMap.ELIG_STA_TYP_INELIGIBLE, "Eligibility history expected ineligible status, eligStaTypId=" + accnEligHists.get(0).getEligStaTypId()+ ",");

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_INELIGIBLE);
        Assert.assertEquals(accnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
        Assert.assertNotNull(accnPyrs.get(0).getEligChkDt());

        logger.info("Verifying new errors added, accnId=" + accnId);
        List<AccnPyrErr> accnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        Assert.assertFalse(accnPyrErrs.isEmpty());
        Assert.assertEquals(accnCntct, accnPyrErrs.get(0).getNote());

        logger.info("Verifying accnCntct added, accnId=" + accnId);
        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(null, accnId);
        Assert.assertEquals(1, accnCntcts.size(), "Expected one accession contact,");
        Assert.assertEquals(accnCntct, accnCntcts.get(0).getCntctInfo(), "Expected message was not found in accnCntct,");

        logger.info("Verifying no qElig added, accnId=" + accnId);
        Assert.assertFalse(accessionDao.isInEligibilityQueue(accnId), "Accession should not be in the eligibility queue,");

        verifyEligTransLog(accnEligHists.get(0));
    }

    @Test(priority = 1, description = "QFrPending-AAA Rejection with Next Action Resubmit")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testPFER_992053(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: PFER-992052, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession is in q_fr_pending, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED, "Expected accession status to be reported status,");
        //Pending or reported is a valid status but lets use reported here

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isQFrPendingAccnEligibilityProcessed(accnId, 1, accnPyrs.get(0).getPyrId(), QUEUE_WAIT_TIME), "Accession did not process eligibility,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getPyrId(), accnPyrs.get(0).getPyrId(), "Eligibility history is incorrect,");
        Assert.assertNull(accnEligHists.get(accnEligHists.size()-1).getTranslatedPyrId(), "Eligibility history expected to have no translation, translatedPyrId=" + accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTransId(), "Eligibility history expected to have transId,");
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTransDt(), "Eligibility history expected to have transDt,");
        Date today = DateConversion.getSystemDate();
        Assert.assertEquals((accnEligHists.get(accnEligHists.size()-1).getTransDt().toString()), today.toString(), "Eligibility history expected to have transDt of today, transDt=" + accnEligHists.get(accnEligHists.size()-1).getTransDt() + ", compareDate=" + today);
        Timestamp threeMinAgo = new Timestamp(DateConversion.getNowAsSqlTimestamp().getTime() - THREE_MIN_TIMESTAMP);
        Assert.assertTrue(accnEligHists.get(accnEligHists.size()-1).getAudDt().compareTo(threeMinAgo) > 0, "Eligibility history expected to have audDt in last 3 minutes, transDt=" + accnEligHists.get(accnEligHists.size()-1).getAudDt() + ", compareDate=" + threeMinAgo);
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getEligStaTypId(), EligMap.ELIG_STA_TYP_RECHK_ELIG, "Eligibility history expected recheck status, eligStaTypId=" + accnEligHists.get(0).getEligStaTypId()+ ",");

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_RECHK_ELIG);
        Assert.assertEquals(accnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
        Assert.assertNotNull(accnPyrs.get(0).getEligChkDt());

        logger.info("Verifying no new errors added, accnId=" + accnId);
        List<AccnPyrErr> accnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        Assert.assertTrue(accnPyrErrs.isEmpty());

        logger.info("Verifying accnCntct added, accnId=" + accnId);
        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(null, accnId);
        Assert.assertEquals(accnCntcts.size(), 1, "Expected one accession contact,");
        Assert.assertEquals(accnCntcts.get(0).getCntctInfo(), accnCntct, "Expected message was not found in accnCntct,");

        logger.info("Verifying no qElig added, accnId=" + accnId);
        Assert.assertFalse(accessionDao.isInEligibilityQueue(accnId), "Accession should not be in the eligibility queue,");

        verifyEligTransLog(accnEligHists.get(accnEligHists.size()-1));
    }

    @Test(priority = 1, description = "QFrPending-Elig DOB mismatch")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testPFER_992054(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: PFER-992054, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession is in q_fr_pending, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED, "Expected accession status to be reported status,");
        //Pending or reported is a valid status but lets use reported here

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isQFrPendingAccnEligibilityProcessed(accnId, 1, accnPyrs.get(0).getPyrId(), QUEUE_WAIT_TIME), "Accession did not process eligibility,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getPyrId(), accnPyrs.get(0).getPyrId(), "Eligibility history is incorrect,");
        Assert.assertNull(accnEligHists.get(accnEligHists.size()-1).getTranslatedPyrId(), "Eligibility history expected to have no translation, translatedPyrId=" + accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTransId(), "Eligibility history expected to have transId,");
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTransDt(), "Eligibility history expected to have transDt,");
        Date today = DateConversion.getSystemDate();
        Assert.assertEquals((accnEligHists.get(accnEligHists.size()-1).getTransDt().toString()), today.toString(), "Eligibility history expected to have transDt of today, transDt=" + accnEligHists.get(accnEligHists.size()-1).getTransDt() + ", compareDate=" + today);
        Timestamp threeMinAgo = new Timestamp(DateConversion.getNowAsSqlTimestamp().getTime() - THREE_MIN_TIMESTAMP);
        Assert.assertTrue(accnEligHists.get(accnEligHists.size()-1).getAudDt().compareTo(threeMinAgo) > 0, "Eligibility history expected to have audDt in last 3 minutes, transDt=" + accnEligHists.get(accnEligHists.size()-1).getAudDt() + ", compareDate=" + threeMinAgo);
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getEligStaTypId(), EligMap.ELIG_STA_TYP_DOB_MISMATCH, "Eligibility history expected dob mismatch status, eligStaTypId=" + accnEligHists.get(0).getEligStaTypId()+ ",");

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_DOB_MISMATCH);
        Assert.assertEquals(accnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
        Assert.assertNotNull(accnPyrs.get(0).getEligChkDt());

        logger.info("Verifying ELIGDOB error is added, accnId=" + accnId);
        verifyUnfixedUnbillableAccnPyrErrs(new HashSet<>(Arrays.asList(DbErrorMap.ELIG_DOB)), accn, accnPyrs.get(0));

        logger.info("Verifying accnCntct added, accnId=" + accnId);
        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(null, accnId);
        Assert.assertEquals(1, accnCntcts.size(), "Expected one accession contact,");
        Assert.assertEquals(accnCntct, accnCntcts.get(0).getCntctInfo(), "Expected message was not found in accnCntct,");

        logger.info("Verifying no qElig added, accnId=" + accnId);
        Assert.assertFalse(accessionDao.isInEligibilityQueue(accnId), "Accession should not be in the eligibility queue,");

        verifyEligTransLog(accnEligHists.get(0));
    }

    @Test(priority = 1, description = "QFrPending-Unreachable")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testPFER_992055(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: PFER-992055, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession is in q_fr_pending, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED, "Expected accession status to be reported status,");
        //Pending or reported is a valid status but lets use reported here

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isQFrPendingAccnEligibilityProcessed(accnId, 1, accnPyrs.get(0).getPyrId(), QUEUE_WAIT_TIME), "Accession did not process eligibility,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getPyrId(), accnPyrs.get(0).getPyrId(), "Eligibility history is incorrect,");
        Assert.assertNull(accnEligHists.get(accnEligHists.size()-1).getTranslatedPyrId(), "Eligibility history expected to have no translation, translatedPyrId=" + accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTransId(), "Eligibility history expected to have transId,");
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTransDt(), "Eligibility history expected to have transDt,");
        Date today = DateConversion.getSystemDate();
        Assert.assertEquals((accnEligHists.get(accnEligHists.size()-1).getTransDt().toString()), today.toString(), "Eligibility history expected to have transDt of today, transDt=" + accnEligHists.get(accnEligHists.size()-1).getTransDt() + ", compareDate=" + today);
        Timestamp threeMinAgo = new Timestamp(DateConversion.getNowAsSqlTimestamp().getTime() - THREE_MIN_TIMESTAMP);
        Assert.assertTrue(accnEligHists.get(accnEligHists.size()-1).getAudDt().compareTo(threeMinAgo) > 0, "Eligibility history expected to have audDt in last 3 minutes, transDt=" + accnEligHists.get(accnEligHists.size()-1).getAudDt() + ", compareDate=" + threeMinAgo);
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getEligStaTypId(), EligMap.ELIG_STA_TYP_UNREACHABLE, "Eligibility history expected eligible status, eligStaTypId=" + accnEligHists.get(0).getEligStaTypId()+ ",");

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_NOT_CHECKED);
        Assert.assertNull(accnPyrs.get(0).getEligSvcName());
        Assert.assertNull(accnPyrs.get(0).getEligChkDt());

        logger.info("Verifying no new errors added, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        logger.info("Verifying accnCntct added, accnId=" + accnId);
        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(null, accnId);
        Assert.assertEquals(1, accnCntcts.size(), "Expected one accession contact,");
        Assert.assertEquals(accnCntct, accnCntcts.get(0).getCntctInfo(), "Expected message was not found in accnCntct,");

        logger.info("Verifying no qElig added, accnId=" + accnId);
        Assert.assertFalse(accessionDao.isInEligibilityQueue(accnId), "Accession should not be in the eligibility queue,");

        logger.info("Check Xifin_elig_trans_log record, transactionId=" + accnEligHists.get(accnEligHists.size()-1).getTransId());
        List<String> eligTransLog = daoManagerEligibilityWS.getEligTransHistFromXIFINELIGTRANSLOGByTransId(accnEligHists.get(accnEligHists.size()-1).getTransId());
        Assert.assertTrue(eligTransLog.size() > 0, "Eligibility transaction log can not be found,");
        Assert.assertTrue(eligTransLog.size() == 6, "Expected 6 columns in eligTransLog result,");
    }

    @Test(priority = 1, description = "QFrPending-Circular Elig")
    @Parameters({"accnId", "origPyrAbbrv", "newPyrAbbrv", "accnCntct1", "accnCntct2"})
    public void testPFER_992056(String accnId, String origPyrAbbrv, String newPyrAbbrv, String accnCntct1, String accnCntct2) throws Exception
    {
        logger.info("Starting Test Case: PFER-992056, accnId=" + accnId + ", pyrAbbrv=" + origPyrAbbrv);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        Pyr pyr =rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);

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

        accessionDetail.saveAndClear(wait);

        cleanUpAccn(accnId);

        Assert.assertTrue(markAccnPyrErrAsFixed(accnId, 1, accessionDetail), "Expected existing accn_pyr_err to be fixable, accnId=" + accnId);

        logger.info("Verifying eligibility translation rule(s), accnId=" + accnId + ", pyrAbbrv=" + origPyrAbbrv);
        List<EligResp> eligResps = rpmDao.getEligRespByPyrId(testDb, rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv).getPyrId());
        int translatedPayorId = rpmDao.getPyrByPyrAbbrv(testDb, newPyrAbbrv).getPyrId();

        Assert.assertEquals(eligResps.size(), 2, "EligResp size was incorrect, eligRespSize=" + eligResps.size());
        Assert.assertNotEquals(eligResps.get(0).getNewPyrId(), eligResps.get(0).getPyrId(), "EligResp payor was incorrect,");
        //Translation line we will use
        Assert.assertNotEquals(eligResps.get(1).getNewPyrId(), eligResps.get(1).getPyrId(), "EligResp payor was incorrect,");
        Assert.assertEquals(translatedPayorId, eligResps.get(1).getNewPyrId());
        Assert.assertTrue(eligResps.get(1).getIsCheckElig(), "Expected isCheckElig to be true,");

        Accn accn = accessionDao.getAccn(accnId);

        logger.info("Verifying accession is in q_fr_pending, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED, "Expected accession status to be reported status,");
        //Pending or reported is a valid status but lets use reported here

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isQFrPendingAccnEligibilityProcessed(accnId, 1, pyr.getPyrId(), QUEUE_WAIT_TIME), "Accession did not process eligibility, origPyr="+origPyrAbbrv);
        Assert.assertTrue(isQFrPendingAccnEligibilityProcessed(accnId, 1, translatedPayorId, QUEUE_WAIT_TIME), "Accession did not process eligibility, newPyr="+newPyrAbbrv);

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(2, accnEligHists.size(), "Size did not match expected");

        Assert.assertEquals(accnEligHists.get(0).getPyrId(), pyr.getPyrId(), "Eligibility history is incorrect,");
        Assert.assertEquals(Integer.valueOf(translatedPayorId), Integer.valueOf(accnEligHists.get(0).getTranslatedPyrId()), "Eligibility history expected to have translation");
        Assert.assertNotNull(accnEligHists.get(0).getTransId(), "Eligibility history expected to have transId,");
        Assert.assertNotNull(accnEligHists.get(0).getTransDt(), "Eligibility history expected to have transDt,");
        Date today = DateConversion.getSystemDate();
        Assert.assertEquals((accnEligHists.get(0).getTransDt().toString()), today.toString(), "Eligibility history expected to have transDt of today, transDt=" + accnEligHists.get(0).getTransDt() + ", compareDate=" + today);
        Timestamp earliestTimeAllowed = new Timestamp(DateConversion.getNowAsSqlTimestamp().getTime() - QUEUE_WAIT_TIME);
        Assert.assertTrue(accnEligHists.get(0).getAudDt().compareTo(earliestTimeAllowed) > 0, "Eligibility history expected to have audDt in last " + TimeUnit.MILLISECONDS.toMinutes(QUEUE_WAIT_TIME) + " minutes, transDt=" + accnEligHists.get(0).getAudDt() + ", compareDate=" + earliestTimeAllowed);
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE, "Eligibility history expected eligible status, eligStaTypId=" + accnEligHists.get(0).getEligStaTypId()+ ",");

        Assert.assertEquals(accnEligHists.get(1).getPyrId(), translatedPayorId, "Eligibility history is incorrect,");
        Assert.assertEquals(Integer.valueOf(pyr.getPyrId()), Integer.valueOf(accnEligHists.get(1).getTranslatedPyrId()), "Eligibility history expected to have translation");
        Assert.assertNotNull(accnEligHists.get(1).getTransId(), "Eligibility history expected to have transId,");
        Assert.assertNotNull(accnEligHists.get(1).getTransDt(), "Eligibility history expected to have transDt,");
        Assert.assertEquals((accnEligHists.get(1).getTransDt().toString()), today.toString(), "Eligibility history expected to have transDt of today, transDt=" + accnEligHists.get(1).getTransDt() + ", compareDate=" + today);
        earliestTimeAllowed = new Timestamp(DateConversion.getNowAsSqlTimestamp().getTime() - QUEUE_WAIT_TIME);
        Assert.assertTrue(accnEligHists.get(1).getAudDt().compareTo(earliestTimeAllowed) > 0, "Eligibility history expected to have audDt in last " + TimeUnit.MILLISECONDS.toMinutes(QUEUE_WAIT_TIME) + " minutes, transDt=" + accnEligHists.get(1).getAudDt() + ", compareDate=" + earliestTimeAllowed);
        Assert.assertEquals(accnEligHists.get(1).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE, "Eligibility history expected eligible status, eligStaTypId=" + accnEligHists.get(1).getEligStaTypId()+ ",");


        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);
        Assert.assertEquals(accnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
        Assert.assertNull(accnPyrs.get(0).getEligChkDt(), "Unexpected elig check date found for translated payor,");

        logger.info("Verifying new error added, accnId=" + accnId);
        List<AccnPyrErr> accnPyrErrs = accessionDao.getAccnPyrErrsByAccnIdPyrPrio(accnId, 1, false);
        Assert.assertEquals(accnPyrErrs.size(), 1, "Expected AccnPyrErr from eligibility");
        Assert.assertEquals(DbErrorMap.CIRCULARELIG, rpmDao.getErrCd(testDb, accnPyrErrs.get(0).getErrCd()).getAbbrev());

        logger.info("Verifying accnCntct added, accnId=" + accnId);
        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(null, accnId);
        Assert.assertEquals(2, accnCntcts.size(), "Expected two accession contact,");
        Assert.assertEquals(accnCntct1, accnCntcts.get(0).getCntctInfo(), "Expected message was not found in accnCntct,");
        Assert.assertEquals(accnCntct2, accnCntcts.get(1).getCntctInfo(), "Expected message was not found in accnCntct,");

        logger.info("Verifying no qElig added, accnId=" + accnId);
        Assert.assertFalse(accessionDao.isInEligibilityQueue(accnId), "Accession should not be in the eligibility queue,");

        verifyEligTransLog(accnEligHists.get(0));
        verifyEligTransLog(accnEligHists.get(1));
    }

    @Test(priority = 1, description = "QFrPending-Undetermined")
    @Parameters({"accnId", "pyrAbbrv", "accnCntct"})
    public void testPFER_992071(String accnId, String pyrAbbrv, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: PFER-992055, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession is in q_fr_pending, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED, "Expected accession status to be reported status,");
        //Pending or reported is a valid status but lets use reported here

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isQFrPendingAccnEligibilityProcessed(accnId, 1, accnPyrs.get(0).getPyrId(), QUEUE_WAIT_TIME), "Accession did not process eligibility,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getPyrId(), accnPyrs.get(0).getPyrId(), "Eligibility history is incorrect,");
        Assert.assertNull(accnEligHists.get(accnEligHists.size()-1).getTranslatedPyrId(), "Eligibility history expected to have no translation, translatedPyrId=" + accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTransId(), "Eligibility history expected to have transId,");
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTransDt(), "Eligibility history expected to have transDt,");
        Date today = DateConversion.getSystemDate();
        Assert.assertEquals((accnEligHists.get(accnEligHists.size()-1).getTransDt().toString()), today.toString(), "Eligibility history expected to have transDt of today, transDt=" + accnEligHists.get(accnEligHists.size()-1).getTransDt() + ", compareDate=" + today);
        Timestamp threeMinAgo = new Timestamp(DateConversion.getNowAsSqlTimestamp().getTime() - THREE_MIN_TIMESTAMP);
        Assert.assertTrue(accnEligHists.get(accnEligHists.size()-1).getAudDt().compareTo(threeMinAgo) > 0, "Eligibility history expected to have audDt in last 3 minutes, transDt=" + accnEligHists.get(accnEligHists.size()-1).getAudDt() + ", compareDate=" + threeMinAgo);
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getEligStaTypId(), EligMap.ELIG_STA_TYP_UNDETERMINED, "Eligibility history expected undetermined status, eligStaTypId=" + accnEligHists.get(0).getEligStaTypId()+ ",");

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_UNDETERMINED);
        Assert.assertEquals(accnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN, "Expected service not found,");
        Assert.assertNotNull(accnPyrs.get(0).getEligChkDt());

        logger.info("Verifying new error added, accnId=" + accnId);
        List<AccnPyrErr> accnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        Assert.assertEquals(1, accnPyrErrs.size(), "AccnPyrErr was expected");
        Assert.assertEquals(DbErrorMap.ELIGUNDETERMND, rpmDao.getErrCd(testDb, accnPyrErrs.get(0).getErrCd()).getAbbrev());

        logger.info("Verifying accnCntct added, accnId=" + accnId);
        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(null, accnId);
        Assert.assertEquals(1, accnCntcts.size(), "Expected one accession contact,");
        Assert.assertEquals(accnCntct, accnCntcts.get(0).getCntctInfo(), "Expected message was not found in accnCntct,");

        logger.info("Verifying no qElig added, accnId=" + accnId);
        Assert.assertFalse(accessionDao.isInEligibilityQueue(accnId), "Accession should not be in the eligibility queue,");

        verifyEligTransLog(accnEligHists.get(0));
    }

    @Test(priority = 1, description = "QFrPending-Translate-NoEligCheckNext")
    @Parameters({"accnId", "origPyrAbbrv", "newPyrAbbrv", "origSubId", "subIdFromIg", "accnCntct"})
    public void testPFER_992072(String accnId, String origPyrAbbrv, String newPyrAbbrv, String origSubId, String subIdFromIg, String accnCntct) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        Pyr pyr =rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        logger.info("Starting Test Case: PFER-992072, accnId=" + accnId + ", pyrAbbrv=" + origPyrAbbrv);

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
        accessionDetail.saveAndClear(wait);
        cleanUpAccn(accnId);

        // Verify primary payor
        logger.info("Verifying primary payor is correct, accnId="+accnId+", pyrAbbrev="+pyr.getPyrAbbrv());
        List<AccnPyr> origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertFalse(origAccnPyrs.isEmpty());
        Assert.assertEquals(origAccnPyrs.get(0).getPyrId(), pyr.getPyrId());


        logger.info("Verifying eligibility translation rule(s), accnId=" + accnId + ", origPyrAbbrv=" + origPyrAbbrv);
        List<EligResp> eligResps = rpmDao.getEligRespByPyrId(testDb, pyr.getPyrId());
        Assert.assertEquals(eligResps.size(), 4, "EligResp size was incorrect, eligRespSize=" + eligResps.size());
        int translatedPayorId = rpmDao.getPyrByPyrAbbrv(testDb, newPyrAbbrv).getPyrId();
        Assert.assertEquals(eligResps.get(2).getNewPyrId(), translatedPayorId, "Expected translated payor,");
        Assert.assertFalse(eligResps.get(2).getIsCheckElig(), "Expected isCheckElig to be false,");

        Accn accn = accessionDao.getAccn(accnId);
        logger.info("Verifying accession is in q_fr_pending, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED, "Expected accession status to be reported status,");
        //Pending or reported is a valid status but lets use reported here

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isQFrPendingAccnEligibilityProcessed(accnId, 1, pyr.getPyrId(), QUEUE_WAIT_TIME), "Accession did not process eligibility,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getPyrId(), pyr.getPyrId(), "Eligibility history is incorrect,");
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTranslatedPyrId(), "Eligibility history expected to have translation, translatedPyrId=" + accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertEquals(Integer.valueOf(translatedPayorId), Integer.valueOf(accnEligHists.get(accnEligHists.size()-1).getTranslatedPyrId()), "Translated payor does not match expected,");
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTransId(), "Eligibility history expected to have transId,");
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTransDt(), "Eligibility history expected to have transDt,");
        Date today = DateConversion.getSystemDate();
        Assert.assertEquals((accnEligHists.get(accnEligHists.size()-1).getTransDt().toString()), today.toString(), "Eligibility history expected to have transDt of today, transDt=" + accnEligHists.get(accnEligHists.size()-1).getTransDt() + ", compareDate=" + today);
        Timestamp threeMinAgo = new Timestamp(DateConversion.getNowAsSqlTimestamp().getTime() - THREE_MIN_TIMESTAMP);
        Assert.assertTrue(accnEligHists.get(accnEligHists.size()-1).getAudDt().compareTo(threeMinAgo) > 0, "Eligibility history expected to have audDt in last 3 minutes, transDt=" + accnEligHists.get(accnEligHists.size()-1).getAudDt() + ", compareDate=" + threeMinAgo);
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE, "Eligibility history expected eligible status, eligStaTypId=" + accnEligHists.get(0).getEligStaTypId()+ ",");

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getSubsId(), subIdFromIg);
        Assert.assertEquals(translatedPayorId, accnPyrs.get(0).getPyrId(), "Expected payor not found,");
        Assert.assertEquals(accnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE,"Expected status not found,");
        Assert.assertEquals(accnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN, "Expected service not found,");
        Assert.assertNull(accnPyrs.get(0).getEligChkDt(), "Unexpected elig check date found for translated payor,");

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

        logger.info("Verifying no new errors added, accnId=" + accnId);
        //Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).size(), 1);
        //Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).get(0).getErrCd(), 74765);//unrelated error
        // DIAGTABLE error is no longer generated for this accession
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).size(), 0);

        logger.info("Verifying accnCntct added, accnId=" + accnId);
        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(null, accnId);
        Assert.assertEquals(1, accnCntcts.size(), "Expected one accession contact,");
        Assert.assertEquals(accnCntct, accnCntcts.get(0).getCntctInfo(), "Expected message was not found in accnCntct,");

        logger.info("Verifying no qElig added, accnId=" + accnId);
        Assert.assertFalse(accessionDao.isInEligibilityQueue(accnId), "Accession should not be in the eligibility queue,");

        verifyEligTransLog(accnEligHists.get(0));
    }

    @Test(priority = 1, description = "QFrPending-Hospital Admit")
    @Parameters({"accnId", "originalPyrAbbrv", "subId", "eligSvcId", "accnCntct"})
    public void testPFER_992073(String accnId, String originalPyrAbbrv, String subId, String eligSvcId, String accnCntct) throws Exception
    {
        logger.info("Starting Test Case: PFER-992073, accnId=" + accnId + ", pyrAbbrv=" + originalPyrAbbrv);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);

        logger.info("Clear Accn pyr and proc errs = " + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(testDb,accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);
        rpmDao.deleteAccnErrByAccnId(testDb, accnId);

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

        logger.info("Load accn on Accn Detail, accnId=" + accnId);
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

        cleanUpAccn(accnId);

        logger.info("Verifying eligibility translation rule(s), accnId=" + accnId + ", pyrAbbrv=" + originalPyrAbbrv);
        List<EligResp> eligResps = rpmDao.getEligRespByPyrId(testDb, rpmDao.getPyrByPyrAbbrv(testDb, originalPyrAbbrv).getPyrId());
        Assert.assertEquals(eligResps.size(), 0, "EligResp size was incorrect, eligRespSize=" + eligResps.size());

        logger.info("Verifying accession is in q_fr_pending, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED, "Expected accession status to be reported status,");
        //Pending or reported is a valid status but lets use reported here

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isQFrPendingAccnEligibilityProcessed(accnId, 1, origPyr.getPyrId(), QUEUE_WAIT_TIME), "Accession did not process eligibility,");

        logger.info("Verifying accession pyr is NOT changed, accnId=" + accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.size(), 1);
        Assert.assertEquals(accnPyrs.get(0).getPyrId(), origPyr.getPyrId());

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);

        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getPyrId(), accnPyrs.get(0).getPyrId(), "Eligibility history is incorrect,");
        Assert.assertNull(accnEligHists.get(accnEligHists.size()-1).getTranslatedPyrId(), "Eligibility history expected to have no translation, translatedPyrId=" + accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTransId(), "Eligibility history expected to have transId,");
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTransDt(), "Eligibility history expected to have transDt,");
        Date today = DateConversion.getSystemDate();
        Assert.assertEquals((accnEligHists.get(accnEligHists.size()-1).getTransDt().toString()), today.toString(), "Eligibility history expected to have transDt of today, transDt=" + accnEligHists.get(accnEligHists.size()-1).getTransDt() + ", compareDate=" + today);
        Timestamp threeMinAgo = new Timestamp(DateConversion.getNowAsSqlTimestamp().getTime() - THREE_MIN_TIMESTAMP);
        Assert.assertTrue(accnEligHists.get(accnEligHists.size()-1).getAudDt().compareTo(threeMinAgo) > 0, "Eligibility history expected to have audDt in last 3 minutes, transDt=" + accnEligHists.get(accnEligHists.size()-1).getAudDt() + ", compareDate=" + threeMinAgo);
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE, "Eligibility history expected eligible status, eligStaTypId=" + accnEligHists.get(0).getEligStaTypId()+ ",");

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

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1, "Expected accn cntct");
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));
    }

    @Test(priority = 1, description = "QFrPending-Client Roster")
    @Parameters({"accnId", "origPyrAbbrv", "origClnAbbrv", "newPyrAbbrv", "newClnAbbrv", "accnCntct"})
    public void testPFER_992074(String accnId, String origPyrAbbrv, String origClnAbbrv, String newPyrAbbrv, String newClnAbbrv, String accnCntct) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: PFER-992074, accnId=" + accnId + ", pyrAbbrv=" + origPyrAbbrv);
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        Cln origCln = rpmDao.getClnByClnAbbrev(testDb, origClnAbbrv);

        logger.info("Verifying original accession payor and client, accnId=" + accnId +", pyrAbbrv="+origPyr.getPyrAbbrv() + ", clnAbbrv="+origCln.getClnAbbrev());
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

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
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        }

        accessionDetail.saveAndClear(wait);
        cleanUpAccn(accnId);

        logger.info("Verifying eligibility translation rule(s), accnId=" + accnId + ", pyrAbbrv=" + origPyrAbbrv);
        List<EligResp> eligResps = rpmDao.getEligRespByPyrId(testDb, rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv).getPyrId());
        Assert.assertEquals(eligResps.size(), 5, "EligResp size was incorrect, eligRespSize=" + eligResps.size()); //we don't use these

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession is in q_fr_pending, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED, "Expected accession status to be reported status,");
        //Pending or reported is a valid status but lets use reported here

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isQFrPendingAccnEligibilityProcessed(accnId, 1, origPyr.getPyrId(), QUEUE_WAIT_TIME), "Accession did not process eligibility,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is not updated, accnId=" + accnId);
        Pyr newPyr = rpmDao.getPyrByPyrAbbrv(testDb, newPyrAbbrv);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getPyrId(), accnPyrs.get(0).getPyrId(), "Eligibility history is incorrect,");
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getTranslatedPyrId(), Integer.valueOf(newPyr.getPyrId()), "Eligibility history expected to have no translation, translatedPyrId=" + accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNull(accnEligHists.get(accnEligHists.size()-1).getTransId(), "Eligibility history expected to have no transId,");
        Assert.assertNotNull(accnEligHists.get(accnEligHists.size()-1).getTransDt(), "Eligibility history expected to have transDt,");
        Date today = DateConversion.getSystemDate();
        Assert.assertEquals((accnEligHists.get(accnEligHists.size()-1).getTransDt().toString()), today.toString(), "Eligibility history expected to have transDt of today, transDt=" + accnEligHists.get(accnEligHists.size()-1).getTransDt() + ", compareDate=" + today);
        Timestamp threeMinAgo = new Timestamp(DateConversion.getNowAsSqlTimestamp().getTime() - THREE_MIN_TIMESTAMP);
        Assert.assertTrue(accnEligHists.get(accnEligHists.size()-1).getAudDt().compareTo(threeMinAgo) > 0, "Eligibility history expected to have audDt in last 3 minutes, transDt=" + accnEligHists.get(accnEligHists.size()-1).getAudDt() + ", compareDate=" + threeMinAgo);
        Assert.assertEquals(accnEligHists.get(accnEligHists.size()-1).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE, "Eligibility history expected eligible status, eligStaTypId=" + accnEligHists.get(0).getEligStaTypId()+ ",");

        logger.info("Verifying post-eligibility client, accnId=" + accnId + ", newClnAbbrv=" + newClnAbbrv);
        Cln newCln = rpmDao.getClnByClnAbbrev(testDb, newClnAbbrv);
        Accn newAccn = accessionDao.getAccn(accnId);
        Assert.assertEquals(newAccn.getClnId(), newCln.getClnId());

        logger.info("Verifying post-eligibility payor, accnId=" + accnId + ", newPyrAbbrv=" + newPyrAbbrv);
        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertFalse(newAccnPyrs.isEmpty());
        Assert.assertEquals(newAccnPyrs.get(0).getPyrId(), newPyr.getPyrId());

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

        logger.info("Verifying no qElig added, accnId=" + accnId);
        Assert.assertFalse(accessionDao.isInEligibilityQueue(accnId), "Accession should not be in the eligibility queue,");

        List<AccnCntct> accnCntcts = rpmDao.getAccnCntctByAccnId(testDb, accnId);
        logger.info("Making sure accn_cntct record is created, accnId="+accnId+" qElig="+accnCntcts);
        Assert.assertEquals(accnCntcts.size(), 1);
        Assert.assertTrue(accnCntcts.get(0).getCntctInfo().contains(accnCntct));
    }

    private boolean isQFrPendingAccnEligibilityProcessed(String accnId, int expectedRecordCount, int pyrId, long queueWaitTime) throws XifinDataAccessException, InterruptedException
    {
        long startTime = System.currentTimeMillis();
        queueWaitTime += startTime;
        boolean isInQueue = accessionDao.isInQFrPendingQueue(accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        boolean isAccnEligHistFound = isInQueue && doesCountOfAccnEligHistMatch(accnEligHists, expectedRecordCount, pyrId);
        while (!isAccnEligHistFound && System.currentTimeMillis() < queueWaitTime)
        {
            logger.info("Waiting for accession to process eligibility for QFRPending, accnId=" + accnId + ", pyrId=" +  pyrId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME);
            accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
            isAccnEligHistFound = isInQueue && doesCountOfAccnEligHistMatch(accnEligHists, expectedRecordCount, pyrId);
            Assert.assertTrue(isInQueue, "Expected accession to stay in Q_FR_PENDING, accnId=" + accnId );
        }
        return isAccnEligHistFound;
    }

    private boolean doesCountOfAccnEligHistMatch(List<AccnEligHist> accnEligHists, int expectedRecordCount, int pyrId)
    {
        int countPyrRecords = 0;
        for (AccnEligHist accnEligHist : accnEligHists)
        {
            if (accnEligHist.getPyrId() == pyrId)
                countPyrRecords++;
        }
        return countPyrRecords >= expectedRecordCount;
    }

    /**
     * Marks the first Accn Pyr Error on Accn Detail as fixed and checks if all errors are fixed
     * @param accnId
     * @param pyrPrio
     * @param accessionDetail
     * @return if all accn_pyr_err records are fixed
     * @throws XifinDataAccessException error during accn_pyr_err fix check
     */
    private boolean markAccnPyrErrAsFixed(String accnId, int pyrPrio, AccessionDetail accessionDetail) throws XifinDataAccessException {

        logger.info("Load accn on Accn Detail, accnId=" + accnId);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        logger.info("Fixing the eligibility error, accnId=" + accnId);
        try
        {
            accessionDetail.fixUnfixedErrEligAccnDetail();
            accessionDetail.saveAndClear(wait);
        }
        catch (WebDriverException e)
        {
            logger.debug("Unable to fix expected eligibility error, accnId=" + accnId);
        }

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        return accessionDao.getAccnPyrErrsByAccnIdPyrPrio(accnId, pyrPrio, false).isEmpty();
    }
}
