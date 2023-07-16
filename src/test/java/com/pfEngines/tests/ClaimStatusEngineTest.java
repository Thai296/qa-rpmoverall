package com.pfEngines.tests;

import com.mbasys.common.utility.Money;
import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnClmstatHist.AccnClmstatHist;
import com.mbasys.mars.ejb.entity.accnPmt.AccnPmt;
import com.mbasys.mars.ejb.entity.accnProcErr.AccnProcErr;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.accnQue.AccnQue;
import com.mbasys.mars.ejb.entity.claimStatus.ClaimStatus;
import com.mbasys.mars.ejb.entity.claimStatusProc.ClaimStatusProc;
import com.mbasys.mars.ejb.entity.claimStatusStatus.ClaimStatusStatus;
import com.mbasys.mars.ejb.entity.errCd.ErrCd;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrSvc.PyrSvc;
import com.mbasys.mars.ejb.entity.qAccnSubm.QAccnSubm;
import com.mbasys.mars.ejb.entity.qMessage.QMessage;
import com.mbasys.mars.ejb.entity.submAckFileLink.SubmAckFileLink;
import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.mbasys.mars.ejb.entity.submSvc.SubmSvc;
import com.mbasys.mars.ejb.entity.taskTyp.TaskTyp;
import com.mbasys.mars.errorProcessing.EpConstants;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.MiscMap;
import com.overall.accession.accessionNavigation.AccessionNavigation;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.accession.accessionProcessing.superSearch.SuperSearch;
import com.overall.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
import com.overall.fileMaintenance.sysMgt.TaskScheduler;
import com.overall.headerNavigation.HeaderNavigation;
import com.overall.menu.MenuNavigation;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.qa.dao.rpm.domain.QEpErr;
import com.xifin.utils.SeleniumBaseTest;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class ClaimStatusEngineTest extends SeleniumBaseTest
{
    protected static long QUEUE_POLL_TIME = TimeUnit.SECONDS.toMillis(4);
    protected static long QUEUE_WAIT_TIME = TimeUnit.MINUTES.toMillis(4);
    private AccessionDetail accessionDetail;
    private MenuNavigation navigation;
    private SuperSearch superSearch;
    private static final SimpleDateFormat DATE_FORMAT_MMDDYYYY = new SimpleDateFormat("MMddyyyy");

    @BeforeMethod(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins", "accnId"})
    public void beforeTest(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, String disableBrowserPlugins, @Optional String accnId)
    {
        try
        {
            logger.info("Running Before Method");
            logIntoSso(ssoUsername, ssoPassword);
            cleanClaimStatusTablesAndClaimStatHistoryByAccnId(accnId);
        }
        catch (Exception e)
        {
            Assert.fail("Error running Before Method", e);
        }
    }

    @AfterSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
    public void afterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running AfterSuite");
            // Disable excess Selenium logging
            java.util.logging.Logger.getLogger("org.openqa.selenium.remote").setLevel(java.util.logging.Level.OFF);
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

    @Test(alwaysRun = true, description = "Unfixed Pyr level DEN Error is populated by Engine")
    @Parameters({"accnId", "pyrAbbrv", "pyrErrs", "procErrs", "statusDate", "message", "userFrom"})
    public void testPFR_587(String accnId, String pyrAbbrv, String pyrErrs, String procErrs, String statusDate, String message, String userFrom) throws Exception
    {
        superSearch = new SuperSearch(driver, config, wait);
        navigation = new MenuNavigation(driver, config);
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));
        logger.info("Starting Test Case PFR_587 - Claim Status Engine - Unfixed Pyr level DEN Error is populated by Engine");
        logger.info("Clearing AccnPyrErrs, accnId=" + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(null, accnId);
        logger.info("Make sure Pyr on the accn is setup for Real Time Claim Status pyr_svc for accnId " + accnId);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        Assert.assertTrue(isPyrSetupForRealTimeClaimStatusCheck(pyr));
        navigation.navigateToSuperSearchPage();
        superSearch.performCheckClaimStatusActionOnSuperSearch(accnId, wait);

        logger.info("Make sure Accn is Processed by Claim Status Engine");
        boolean IsAccnOutOfQClaimStatus = waitForClaimStatusEngineToProcessAccn(accnId, QUEUE_WAIT_TIME);
        logger.info("Verify Proc and Pyr level errors");
        verifyProcAndPyrErrorsOnAccn(accnId, statusDate, pyrErrs, procErrs);

        logger.info("Make sure Accn that has a Pyr level Error is in EP queue");
        AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
        Assert.assertEquals(accnQue.getQTyp(), AccnStatusMap.Q_EP_MAN);
        logger.info("Make sure Accn have claim status history records created");
        List<AccnClmstatHist> accnClmstatHist = rpmDao.getAccnClaimStatHistoryByAccnId(testDb, accnId);
        Assert.assertEquals(accnClmstatHist.size(), 1);

        logger.info("Make sure 1 Message for the user is created");
        verifyQMessage(accnId, message, userFrom, startTime, QUEUE_WAIT_TIME);
    }


    @Test(alwaysRun = true, description = "Proc level Errors: Acknowledgment - fixed, Denial unfixed -  populated by Engine")
    @Parameters({"accnId", "pyrAbbrv", "pyrErrs", "procErrs", "statusDate", "message", "userFrom"})
    public void testPFR_588(String accnId, String pyrAbbrv, String pyrErrs, String procErrs, String statusDate, String message, String userFrom) throws Exception
    {
        superSearch = new SuperSearch(driver, config, wait);
        navigation = new MenuNavigation(driver, config);
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));
        logger.info("Starting Test Case PFR_588 - Claim Status Engine - Proc level Errors: Acknowledgment - fixed, Denial unfixed -  populated by Engine");
        logger.info("Clearing AccnProcErrs, accnId=" + accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);
        logger.info("Clearing q_claim_status record for accnId=" + accnId);
        rpmDao.deleteQClaimStatus(testDb, accnId);
        logger.info("Clearing accn_clmstat_hist records for accnId=" + accnId);
        rpmDao.deleteAccnClaimStatHistByAccnId(testDb, accnId);
        logger.info("Make sure Pyr on the accn is setup for Real Time Claim Status pyr_svc for accnId " + accnId);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        Assert.assertTrue(isPyrSetupForRealTimeClaimStatusCheck(pyr));

        navigation.navigateToSuperSearchPage();
        superSearch.performCheckClaimStatusActionOnSuperSearch(accnId, wait);

        logger.info("Wait until Accn is Processed by Claim Status Engine");
        boolean IsAccnOutOfQClaimStatus = waitForClaimStatusEngineToProcessAccn(accnId, QUEUE_WAIT_TIME);
        logger.info("Verify Proc and Pyr level errors");
        verifyProcAndPyrErrorsOnAccn(accnId, statusDate, pyrErrs, procErrs);

        logger.info("Make sure Accn that has a Pyr level Error is in EP queue");
        AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
        Assert.assertEquals(accnQue.getQTyp(), AccnStatusMap.Q_EP_MAN);
        logger.info("Make sure Accn have claim status history records created");
        List<AccnClmstatHist> accnClmstatHist = rpmDao.getAccnClaimStatHistoryByAccnId(testDb, accnId);
        Assert.assertEquals(accnClmstatHist.size(), 1);

        logger.info("Make sure 1 Message for the user is created");
        verifyQMessage(accnId, message, userFrom, startTime, QUEUE_WAIT_TIME);
    }

    @Test(alwaysRun = true, description = "Unfixing existing pyr and proc level DEN errors if same errors already there and fixed")
    @Parameters({"accnId", "pyrAbbrv", "statusDate", "pyrErrs", "procErrs", "message", "userFrom"})
    public void testPFR_589(String accnId, String pyrAbbrv, String statusDate, String pyrErrs, String procErrs, String message, String userFrom) throws Exception
    {
        navigation = new MenuNavigation(driver, config);
        superSearch = new SuperSearch(driver, config, wait);
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));
        logger.info("Starting Test Case PFR_589 - Claim Status Engine - Unfixing existing pyr and proc level DEN errors if same errors already there and fixed");
        logger.info("Clearing q_claim_status record for accnId=" + accnId);
        rpmDao.deleteQClaimStatus(testDb, accnId);
        logger.info("Clearing accn_clmstat_hist records for accnId=" + accnId);
        rpmDao.deleteAccnClaimStatHistByAccnId(testDb, accnId);
        logger.info("Make sure proc level and pyr level errors are unfixed");
        //TODO change when Accn Detail submit added the NOTE is fixed
        //rpmDao.updateFixedAccnProcErrSetUnfixedByAccnId(testDb, accnId);
        // rpmDao.updateFixedAccnPyrErrSetUnfixedByAccnId(testDb, accnId);
        rpmDao.updateUnfixedAccnProcErrSetFixedByAccnId(testDb, accnId);
        rpmDao.updateUnfixedAccnPyrErrSetFixedByAccnId(testDb, accnId);
        logger.info("Make sure Pyr on the accn is setup for Real Time Claim Status pyr_svc for accnId " + accnId);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        Assert.assertTrue(isPyrSetupForRealTimeClaimStatusCheck(pyr));

        logger.info("Make sure there are 2 unfixed Errors - accn_proc_err and accn_pyr_level Denial errors, accnId=" + accnId);
        logger.info("Make sure Proc level error is unfixed, has 1 claim status error");
        List<AccnProcErr> origAccnProcErrs = accessionDao.getAccnProcErrsByAccnId(accnId);
        int numberOfProcErrs = origAccnProcErrs.size();
        Assert.assertEquals(numberOfProcErrs, 1);

        logger.info("Pyr level error has 1 claim status error");
        List<AccnPyrErr> origAccnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId);
        int numberOfPyrErrs = origAccnPyrErrs.size();
        Assert.assertEquals(numberOfPyrErrs, 1);
        //TODO change when Accn Detail submit added the NOTE is fixed
        // fixUnfixedAccnErrors(accnId);

        logger.info("Make sure proc level and pyr level errors are fixed - " + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());
        Assert.assertTrue(accessionDao.getAccnProcErrsByAccnId(accnId, false, false).isEmpty());
       // switchToDefaultWinFromFrame();
        navigation.navigateToSuperSearchPage();
        superSearch.performCheckClaimStatusActionOnSuperSearch(accnId, wait);

        logger.info("Wait until Accn is Processed by Claim Status Engine");
        boolean IsAccnOutOfQClaimStatus = waitForClaimStatusEngineToProcessAccn(accnId, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsAccnOutOfQClaimStatus);
        logger.info("Make sure Claim Status Engine did not add any new errors");
        Assert.assertEquals(accessionDao.getAccnProcErrsByAccnId(accnId).size(), numberOfProcErrs);
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), numberOfPyrErrs);
        List<AccnProcErr> newAccnProcErrs = accessionDao.getAccnProcErrsByAccnId(accnId);
        logger.info("Make sure there is still 1 pyr-level error and it is unfixed");
        Assert.assertEquals(newAccnProcErrs.size(), numberOfPyrErrs);
        Assert.assertNull(newAccnProcErrs.get(0).getFixDt());
        logger.info("Make sure Accn have claim status history records created");
        List<AccnClmstatHist> accnClmstatHist = rpmDao.getAccnClaimStatHistoryByAccnId(testDb, accnId);
        Assert.assertEquals(accnClmstatHist.size(), 1);
        logger.info("Make sure Pyr level error is unfixed, has 1 claim status error");
        Assert.assertNull(accessionDao.getAccnPyrErrsByAccnId(accnId).get(0).getFixDt());
        logger.info("Verify Proc and Pyr level errors");
        verifyProcAndPyrErrorsOnAccn(accnId, statusDate, pyrErrs, procErrs);

        logger.info("Make sure Accn that has a Pyr level Error is in EP queue");
        AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
        Assert.assertEquals(accnQue.getQTyp(), AccnStatusMap.Q_EP_MAN);

        logger.info("Make sure 1 Message for the user is created");
        verifyQMessage(accnId, message, userFrom, startTime, QUEUE_WAIT_TIME);
    }

    @Test(alwaysRun = true, description = "Skipping Claim Status Check by the Engine - Pyr is not Setup")
    @Parameters({"accnId", "pyrAbbrv", "message", "userFrom", "procErrs", "statusDate", "pyrErrs"})
    public void testPFR_590(String accnId, String pyrAbbrv, String message, String userFrom, String procErrs, String statusDate, String pyrErrs) throws Exception
    {
        superSearch = new SuperSearch(driver, config, wait);
        navigation = new MenuNavigation(driver, config);

        Timestamp startTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));
        logger.info("Starting Test Case PFR_590 - Skipping Claim Status Check by the Engine - Pyr is not Setup");
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        AccnQue originalAccnQue = accessionDao.getAccnQueByAccnId(accnId);

        logger.info("Make sure there are no accn_proc_err and accn_pyr_level errors, accnId=" + accnId);
        verifyNoProcAndPyrErrsOnAccn(accnId);

        logger.info("Make sure Pyr on accn is NOT setup for Claim Status - pyr_svc table, enabled, pyr_svc_typ =1, outgoing pyr - Test");
        Assert.assertFalse(isPyrSetupForRealTimeClaimStatusCheck(pyr));

        navigation.navigateToSuperSearchPage();
        superSearch.performCheckClaimStatusActionOnSuperSearch(accnId, wait);

        logger.info("Make sure accn is NOT queued up for Claim Status Check");
        Assert.assertFalse(rpmDao.isAccnInQClaimStatus(testDb, accnId));

        logger.info("Make sure Accn is in the same queue, no errors are added");
        AccnQue newAccnQue = accessionDao.getAccnQueByAccnId(accnId);
        Assert.assertEquals(originalAccnQue, newAccnQue);
        logger.info("Make sure Claim Status Check was not done");
        logger.info("Make sure Accn has no claim status history record created");
        List<AccnClmstatHist> accnClmstatHist = rpmDao.getAccnClaimStatHistoryByAccnId(testDb, accnId);
        Assert.assertEquals(accnClmstatHist.size(), 0);
        logger.info("Make sure 1 Message for the user is created");
        verifyQMessage(accnId, message, userFrom, startTime, QUEUE_WAIT_TIME);
        logger.info("Create Expected Proc Errors added to the accn");
        logger.info("Verify Proc and Pyr level errors");
        verifyProcAndPyrErrorsOnAccn(accnId, statusDate, pyrErrs, procErrs);
    }

    @Test(alwaysRun = true, description = "skipping Claim Status Check for zbal accn, pyr setup for Real Time Claim Status Check")
    @Parameters({"accnId", "pyrAbbrv", "message", "userFrom", "procErrs", "statusDate", "pyrErrs"})
    public void testPFR_591(String accnId, String pyrAbbrv, String message, String userFrom, String procErrs, String statusDate, String pyrErrs) throws Exception
    {
        superSearch = new SuperSearch(driver, config, wait);
        navigation = new MenuNavigation(driver, config);
        logger.info("Starting Test Case PFR_591 - Claim Status Engine - Skipping Claim Status Check for zbal accn");
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));
        logger.info("Make sure there are no accn_proc_err and accn_pyr_level errors, accnId=" + accnId);
        verifyNoProcAndPyrErrsOnAccn(accnId);

        logger.info("Make sure Pyr on accn is setup for Claim Status - pyr_svc table, enabled, pyr_svc_typ =1, outgoing pyr - Test");
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        Assert.assertTrue(isPyrSetupForRealTimeClaimStatusCheck(pyr));

        logger.info("Make sure accn is ZBal");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL);
        navigation.navigateToSuperSearchPage();
        superSearch.performCheckClaimStatusActionOnSuperSearch(accnId, wait);
        logger.info("Make sure Claim Status Check was not done");
        logger.info("Make sure Accn has no claim status history record created");
        List<AccnClmstatHist> accnClmstatHist = rpmDao.getAccnClaimStatHistoryByAccnId(testDb, accnId);
        Assert.assertEquals(accnClmstatHist.size(), 0);
        logger.info("Make sure 1 Message for the user is created");
        verifyQMessage(accnId, message, userFrom, startTime, QUEUE_WAIT_TIME);
        logger.info("Verify Proc and Pyr level errors");
        verifyProcAndPyrErrorsOnAccn(accnId, statusDate, pyrErrs, procErrs);
    }

    @Test(alwaysRun = true, description = "Skipping Claim Status Check for final reported accn")
    @Parameters({"accnId", "pyrAbbrv", "message", "userFrom", "procErrs", "statusDate", "pyrErrs"})
    public void testPFR_592(String accnId, String pyrAbbrv, String message, String userFrom, String procErrs, String statusDate, String pyrErrs) throws Exception
    {   accessionDetail = new AccessionDetail(driver, config, wait);
        navigation = new MenuNavigation(driver, config);
        superSearch = new SuperSearch(driver, config, wait);
        logger.info("Starting Test Case PFR_592 - Claim Status Engine - skipping Claim Status Check for final reported accn");
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));
        logger.info("Make sure there are no accn_proc_err and accn_pyr_level errors, accnId=" + accnId);
        verifyNoProcAndPyrErrsOnAccn(accnId);

        logger.info("Make sure Pyr on accn is setup for Claim Status - pyr_svc table, enabled, pyr_svc_typ =1, outgoing pyr - Test");
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        Assert.assertTrue(isPyrSetupForRealTimeClaimStatusCheck(pyr));

        Accn accn = accessionDao.getAccn(accnId);
        logger.info("Make sure accn is final reported - " + accn.getStaId());
        if (accn.getStaId() != AccnStatusMap.ACCN_STATUS_REPORTED)
        {
            navigation.navigateToAccnDetailPage();
            logger.info("Load accn on Accn Detail - " + accnId);
            accessionDetail.loadAccnOnAccnDetail(wait, accnId);

            logger.info("Force Accn to reprice");
            accessionDetail.clickRepriceCheckbox(wait);
            accessionDetail.saveAndClear(wait);
            switchToDefaultWinFromFrame();
        }
        navigation.navigateToSuperSearchPage();
        superSearch.performCheckClaimStatusActionOnSuperSearch(accnId, wait);

        logger.info("Wait until Accn is Processed by Claim Status Engine");
        logger.info("Make sure 1 Message for the user is created");
        logger.info("accn -"+accnId+" message - "+message+" userFrom "+userFrom);
        verifyQMessage(accnId, message, userFrom, startTime, QUEUE_WAIT_TIME);

        logger.info("Make sure Accn has no claim status history record created");
        List<AccnClmstatHist> accnClmstatHist = rpmDao.getAccnClaimStatHistoryByAccnId(testDb, accnId);
        Assert.assertEquals(accnClmstatHist.size(), 0);

        logger.info("Verify Proc and Pyr level errors");
        verifyProcAndPyrErrorsOnAccn(accnId, statusDate, pyrErrs, procErrs);
    }

    @Test(alwaysRun = true, description = "Performing Claim Status Check for the secondary Pyr")
    @Parameters({"accnId", "primPyrAbbrv", "secondPyrAbbrv", "procErrs", "pyrErrs", "statusDate", "message", "userFrom"})
    public void testPFR_593(String accnId, String primPyrAbbrv, String secondPyrAbbrv, String procErrs, String pyrErrs, String statusDate, String message, String userFrom) throws Exception
    {
        logger.info("Starting Test Case PFR_593 - Claim Status Engine - Performing Claim Status Check for the secondary Pyr");
        navigation = new MenuNavigation(driver, config);
        superSearch = new SuperSearch(driver, config, wait);
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));
        logger.info("Clearing q_claim_status record for accnId=" + accnId);
        rpmDao.deleteQClaimStatus(testDb, accnId);
        logger.info("Clearing accn_clmstat_hist records for accnId=" + accnId);
        rpmDao.deleteAccnClaimStatHistByAccnId(testDb, accnId);
        logger.info("Make sure there is Primary pyr and Secondary pyr claims for accnId=" + accnId);
        List<QAccnSubm> qAccnSubms = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertTrue(qAccnSubms.size()>1);
        Pyr primaryPyr =  rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv);
        Pyr secondaryPyr =  rpmDao.getPyrByPyrAbbrv(testDb, secondPyrAbbrv);
        Assert.assertEquals(qAccnSubms.get(0).getPyrId(), primaryPyr.getPyrId());
        Assert.assertEquals(qAccnSubms.get(1).getPyrId(), secondaryPyr.getPyrId());

        logger.info("Make sure there is Primary pyr payment on accnId=" + accnId);
        List<AccnPmt> accnPmts = rpmDao.getAccnPmtsByAccnId(testDb, accnId);
        for(AccnPmt accnPmt : accnPmts){
            Assert.assertEquals(accnPmt.getPmtPyrPrio(), 1);
        }

        logger.info("Delete Accn proc and pyr errs");
        rpmDao.deleteAccnPyrErrByAccnId(testDb, accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);

        logger.info("Make sure there are no accn_proc_err and accn_pyr_level errors, accnId=" + accnId);
        verifyNoProcAndPyrErrsOnAccn(accnId);

        logger.info("Make sure both Pyrs on accn are setup for Claim Status - pyr_svc table, enabled, pyr_svc_typ =1, outgoing pyr - Test");
        Assert.assertTrue(isPyrSetupForRealTimeClaimStatusCheck(rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv)));
        logger.info("Primary pyr is setup " + primPyrAbbrv);
        Assert.assertTrue(isPyrSetupForRealTimeClaimStatusCheck(rpmDao.getPyrByPyrAbbrv(testDb, secondPyrAbbrv)));
        logger.info("Secondary pyr is setup " + secondPyrAbbrv);

        navigation.navigateToSuperSearchPage();
        superSearch.performCheckClaimStatusActionOnSuperSearch(accnId, wait);

        logger.info("Wait until Accn is Processed by Claim Status Engine");
        boolean IsAccnOutOfQClaimStatus = waitForClaimStatusEngineToProcessAccn(accnId, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsAccnOutOfQClaimStatus);
        logger.info("Verify Proc and Pyr level errors");
        verifyProcAndPyrErrorsOnAccn(accnId, statusDate, pyrErrs, procErrs);

        logger.info("Make sure Accn has 1 claim status history record created");
        List<AccnClmstatHist> accnClmstatHist = rpmDao.getAccnClaimStatHistoryByAccnId(testDb, accnId);
        Assert.assertEquals(accnClmstatHist.size(), 1);
        logger.info("Make sure 1 Message for the user is created");
        verifyQMessage(accnId, message, userFrom, startTime, QUEUE_WAIT_TIME);
        logger.info("Make sure 1 ClaimStatus record was created for accnId="+accnId);
        List<ClaimStatus> claimStatusList = rpmDao.getClaimStatusList(accnId, 0);
        Assert.assertEquals(claimStatusList.size(), 1);
        Assert.assertEquals(claimStatusList.get(0).getPyrId(), secondaryPyr.getPyrId());
        Assert.assertEquals(claimStatusList.get(0).getClaimIdSuffix(), "02");
        logger.info("Make sure there are Claim Status Status records created for claimStatusId="+claimStatusList.get(0).getSeqId());
        List<ClaimStatusStatus> claimStatusStatuses = rpmDao.getClaimStatusStatusList(claimStatusList.get(0).getSeqId());
        Assert.assertEquals(claimStatusStatuses.size(), 2);
        logger.info("Make sure there are Claim Status Proc records created for claimStatusId="+claimStatusList.get(0).getSeqId());
        List<ClaimStatusProc> claimStatusProcs = rpmDao.getClaimStatusProcList(claimStatusList.get(0).getSeqId());
        Assert.assertEquals(claimStatusProcs.size(), 2);
    }

    @Test(alwaysRun = true, description = "Performing Claim Status Check, add the error with action to move accn to EP_OUT_AGNCY_PRE_CORRESP")
    @Parameters({"accnId", "primPyrAbbrv", "pyrErrs", "procErrs", "statusDate", "message", "userFrom"})
    public void
    testPFR_603(String accnId, String primPyrAbbrv, String pyrErrs, String procErrs, String statusDate, String message, String userFrom) throws Exception
    {
        navigation = new MenuNavigation(driver, config);
        superSearch = new SuperSearch(driver, config, wait);
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));
        logger.info("Starting Test Case PFR_603 - Claim Status Engine - Performing Claim Status Check, add the error with action to move accn to EP_OUT_AGNCY_PRE_CORRESP");
        submissionDao.deleteSubmissions(rpmDao.getQAccnSubmByAccnIdAndClaimIdSuffixMoreThan01AndNull(accnId));

        logger.info("Make sure there are no errors on proc level (accn_proc) and 1 accn_pyr_level error, accnId=" + accnId);
        List<AccnProcErr> origAccnProcErrs = accessionDao.getAccnProcErrsByAccnId(accnId);
        Assert.assertEquals(origAccnProcErrs.size(), 0);

        List<AccnPyrErr> origAccnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId);
        Assert.assertEquals(origAccnPyrErrs.size(), 1);

        ErrCd errCd = rpmDao.getErrCd(testDb, (origAccnPyrErrs.get(0).getErrCd()));
        logger.info("Verify the claim status error has correct OUT_AGNCY_ID and OUT_AGNCY_ID_PRE_CORRESP, errCdAbbrev="+errCd.getAbbrev());
        Assert.assertEquals(errCd.getOutAgncyIdPreCorresp(), 1);
        Assert.assertEquals(errCd.getOutAgncyId(), 5);

        logger.info("Make sure pyr level error is fixed - " + origAccnPyrErrs.get(0).getFixDt());
        if (origAccnPyrErrs.get(0).getFixDt() == null)
        {
            navigation.navigateToAccnDetailPage();
            fixUnfixedAccnErrors(accnId);
        }

        origAccnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId);
        Assert.assertNotEquals(origAccnPyrErrs.get(0).getFixDt(), null);

        logger.info("Make sure Pyr on accn are setup for Claim Status - pyr_svc table, enabled, pyr_svc_typ =1, outgoing pyr - Test");
        Pyr primPyr = rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv);
        Assert.assertTrue(isPyrSetupForRealTimeClaimStatusCheck(primPyr));
        logger.info("Primary pyr is setup " + primPyrAbbrv);
        navigation.navigateToSuperSearchPage();
        superSearch.performCheckClaimStatusActionOnSuperSearch(accnId, wait);

        logger.info("Wait until Accn is Processed by Claim Status Engine");
        boolean IsAccnOutOfQClaimStatus = waitForClaimStatusEngineToProcessAccn(accnId, QUEUE_WAIT_TIME);
        logger.info("Make sure Accn have claim status history records created");
        List<AccnClmstatHist> accnClmstatHist = rpmDao.getAccnClaimStatHistoryByAccnId(testDb, accnId);
        Assert.assertEquals(accnClmstatHist.size(), 1);

        logger.info("Make sure pyr errors are added for both pyrs");
        verifyProcAndPyrErrorsOnAccn(accnId, statusDate, pyrErrs, procErrs);

        logger.info("Make sure Accn is in queue - Q_EP_OUT_AGNCY_PRE_CORRESP_ERR");
        AccnQue newAccnQue = accessionDao.getAccnQueByAccnId(accnId);
        Assert.assertEquals(newAccnQue.qTyp, AccnStatusMap.Q_EP_OUT_AGNCY_PRE_CORRESP);

        logger.info("Verify that the accession is in Q_EP_OUT_AGNCY_PRE_CORRESP_ERR table");
        QEpErr qEpErr = rpmDao.getQEpOutAgncyPreCorrespErrByAccnId(testDb, accnId);
        Assert.assertEquals(qEpErr.getErrCd(), errCd.getErrCd());

        logger.info("Make sure 1 Message for the user is created");
        verifyQMessage(accnId, message, userFrom, startTime, QUEUE_WAIT_TIME);
    }

    @Test(alwaysRun = true, description = "skipping Claim Status Check for zbal accn, setup for Claim Status Batch")
    @Parameters({"accnId", "message", "userFrom", "pyrAbbrv", "submSvcAbbrev"})
    public void testPFR_640(String accnId, String message, String userFrom, String pyrAbbrv, String submSvcAbbrev) throws Exception
    {
        navigation = new MenuNavigation(driver, config);
        superSearch = new SuperSearch(driver, config, wait);
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));
        logger.info("Starting Test Case PFR_640 - Claim Status Engine - skipping Claim Status Check for zbal accn");
        AccnQue originalAccnQue = accessionDao.getAccnQueByAccnId(accnId);

        logger.info("Make sure there are no accn_proc_err and accn_pyr_level errors, accnId=" + accnId);
        verifyNoProcAndPyrErrsOnAccn(accnId);

        logger.info("Make sure Pyr on accn is setup for Claim Status - pyr_svc table, enabled, pyr_svc_typ =2, outgoing pyr - Test");
        SubmSvc submSvc276 = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        Assert.assertTrue(isPyrSetupForBatchClaimStatusCheck(rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv), submSvc276));
        logger.info("Primary pyr is setup " + pyrAbbrv);

        logger.info("Make sure accn is ZBal");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL);
        navigation.navigateToSuperSearchPage();
        superSearch.performCheckClaimStatusActionOnSuperSearch(accnId, wait);

        logger.info("Make sure Accn is in the same Queue - " + originalAccnQue);
        Assert.assertEquals(originalAccnQue, accessionDao.getAccnQueByAccnId(accnId));

        logger.info("Make sure there are no accn_proc_err and accn_pyr_level errors, accnId=" + accnId);
        verifyNoProcAndPyrErrsOnAccn(accnId);
        logger.info("Make sure 1 Message for the user is created");
        verifyQMessage(accnId, message, userFrom, startTime, QUEUE_WAIT_TIME);
    }
    @Test(alwaysRun = true, description = "Skipping Claim Status Check accn - pyr setup for Batch Claim Status Check")
    @Parameters({"accnId", "message", "userFrom", "pyrAbbrv", "submSvcAbbrev"})
    public void testPFR_653(String accnId, String message, String userFrom, String pyrAbbrv, String submSvcAbbrev) throws Exception
    {
        navigation = new MenuNavigation(driver, config);
        superSearch = new SuperSearch(driver, config, wait);
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));
        logger.info("Starting Test Case PFR_653 - Skipping Claim Status Check accn - pyr setup for Batch Claim Status Check");
        AccnQue originalAccnQue = accessionDao.getAccnQueByAccnId(accnId);

        logger.info("Make sure there are no accn_proc_err and accn_pyr_level errors, accnId=" + accnId);
        verifyNoProcAndPyrErrsOnAccn(accnId);

        logger.info("Make sure Pyr on accn is not setup for Real time Claim Status - pyr_svc table, enabled, pyr_svc_typ =1, outgoing pyr - Test");
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        Assert.assertFalse(isPyrSetupForRealTimeClaimStatusCheck(pyr));

        logger.info("Make sure Pyr on accn is setup for Batch Claim Status - pyr_svc table, enabled, pyr_svc_typ =2, outgoing pyr - Test");
        SubmSvc submSvc276 = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        Assert.assertTrue(isPyrSetupForBatchClaimStatusCheck(pyr, submSvc276));
        logger.info("Primary pyr is setup for Batch Claim Status" + pyrAbbrv);

        Accn accn = accessionDao.getAccn(accnId);
        logger.info("Make sure accn is NOT FReported or ZBal - " + accn.getStaId());
        Assert.assertNotEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL);
        Assert.assertNotEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED);
        navigation.navigateToSuperSearchPage();
        superSearch.performCheckClaimStatusActionOnSuperSearch(accnId, wait);
        logger.info("Make sure accn is queued up for Claim Status Check");
        Assert.assertTrue(rpmDao.isAccnInQClaimStatus(testDb, accnId));

        logger.info("Make sure 1 Message for the user is created");
        verifyQMessage(accnId, message, userFrom, startTime, QUEUE_WAIT_TIME);
        logger.info("Make sure there are no accn_proc_err and accn_pyr_level errors, accnId=" + accnId);
        verifyNoProcAndPyrErrsOnAccn(accnId);
    }

    @Test(alwaysRun = true, description = "Accn with pyr setup for Claim Status Batch 276 is queued up for Submission")
    @Parameters({"accnId", "pyrAbbrv", "submSvcAbbrev", "submSvcAbbrev837", "message", "userFrom"})
    public void testPFR_641(String accnId, String pyrAbbrv, String submSvcAbbrev, String submSvcAbbrev837, String message, String userFrom) throws Exception
    {
        navigation = new MenuNavigation(driver, config);
        accessionDetail = new AccessionDetail(driver, config, wait);
        superSearch = new SuperSearch(driver, config, wait);
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));
        logger.info("Starting Test Case PFR_641 - Claim Status Engine - Accn with pyr setup for Claim Status Batch 276 is queued up for Submission");
        logger.info("Delete unprocessed QAS");

        cleanQASForPyrOnAccn(accnId, rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId());
        SubmSvc submSvc276 = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        cleanQASBatchClaimStatus(accnId, submSvc276.getSubmSvcSeqId());
        List<QAccnSubm> originalQAccnSubmList = rpmDao.getQAccnSubm(testDb, accnId);

        logger.info("Make sure there are no accn_proc_err and accn_pyr_level errors, accnId=" + accnId);
        verifyNoProcAndPyrErrsOnAccn(accnId);

        logger.info("Make sure Pyr on accn is setup for Claim Status - pyr_svc table, enabled, pyr_svc_typ =2, outgoing pyr - Test");
        Assert.assertTrue(isPyrSetupForBatchClaimStatusCheck(rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv), submSvc276));
        logger.info("Primary pyr is setup " + pyrAbbrv);

        Accn accn = accessionDao.getAccn(accnId);

        logger.info("Make sure Accn has previous Q_ACCN_SUBM records for the same PyrId, SubmSvc - 837");
        QAccnSubm previousQAS = rpmDao.getQAccnSubmByAccnIdAndPyrIdAndSubmSvcId(testDb, accnId, rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId(), rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev837).getSubmSvcSeqId());

        logger.info("Make sure accn is NOT FReported or ZBal - " + accn.getStaId());
        Assert.assertNotEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL);
        Assert.assertNotEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED);

        logger.info("Perform Claim Status Check on Super Search jsp for RealTime CS accnId=" + accnId);
        navigation.navigateToSuperSearchPage();
        superSearch.performCheckClaimStatusActionOnSuperSearch(accnId, wait);

        logger.info("Make sure Accn is in q_claim_status");
        Assert.assertTrue(isAccnInQClaimStatus(accnId, QUEUE_WAIT_TIME));

        logger.info("Wait until Accn is Processed by Claim Status Engine");
        boolean IsAccnOutOfQClaimStatus = waitForClaimStatusEngineToProcessAccn(accnId, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsAccnOutOfQClaimStatus);

        logger.info("Make sure there are no accn_proc_err and accn_pyr_level added, accnId=" + accnId);
        verifyNoProcAndPyrErrsOnAccn(accnId);

        logger.info("Make sure Accn is in Submission queue");
        Assert.assertEquals(accessionDao.getAccnQueByAccnId(accnId).getQTyp(), AccnStatusMap.Q_ACCN_SUBM);

        logger.info("Make sure there is 1 new QAS record created by ClaimStatus Engine");
        List<QAccnSubm> newQasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(newQasList.size(), originalQAccnSubmList.size() + 1);

        logger.info("Make sure new QAS record created with subm_svc from pyr_svc");
        verifyUnprocessedClaimStatBatchQAccnSubm(accnId, submSvc276, previousQAS.getClaimIdSuffix());
        logger.info("Make sure there are no accn_proc_err and accn_pyr_level errors, accnId=" + accnId);
        verifyNoProcAndPyrErrsOnAccn(accnId);
        logger.info("Make sure 1 Message for the user is created");
        verifyQMessage(accnId, message, userFrom, startTime, QUEUE_WAIT_TIME);

        logger.info("Delete q_accn_subm reqord queued up for 276 subm_svc");
        rpmDao.deleteUnprocessedQAccnSubmByAccnId(testDb, accnId);
    }

    @Test(alwaysRun = true, description = "Accn with pyr setup for Claim Status Batch 276 is queued up for Submission/unprocessed claim exist")
    @Parameters({"accnId", "pyrAbbrv", "pyrSvcSubmSvcAbbrev", "subId", "claimSubmSvc", "message", "userFrom"})
    public void testPFR_647(String accnId, String pyrAbbrv, String pyrSvcSubmSvcAbbrev, String subId, String claimSubmSvc, String message, String userFrom) throws Exception
    {
        logger.info("Starting Test Case PFR_647 - Claim Status Engine - Accn with pyr setup for Claim Status Batch 276 is queued up for Submission");
        navigation = new MenuNavigation(driver, config);
        accessionDetail = new AccessionDetail(driver, config, wait);
        superSearch = new SuperSearch(driver, config, wait);
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));

        SubmSvc pyrSvcSubmSvc = rpmDao.getSubmSvcByAbbrev(testDb, pyrSvcSubmSvcAbbrev);
        SubmSvc claimStatSubmSvc = rpmDao.getSubmSvcByAbbrev(testDb, claimSubmSvc);
        logger.info("Delete unprocessed subm_file records associated with the accnId="+accnId);
        cleanQASForPyrOnAccn(accnId, rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId());
        cleanQASBatchClaimStatus(accnId, claimStatSubmSvc.getSubmSvcSeqId());
        List<QAccnSubm> originalQAccnSubmList = rpmDao.getQAccnSubm(testDb, accnId);
        logger.info("Get the number of QAS records ="+originalQAccnSubmList.size());

        logger.info("Make sure there are no accn_proc_err and accn_pyr_level errors, accnId=" + accnId);
        verifyNoProcAndPyrErrsOnAccn(accnId);

        logger.info("Make sure Accn has previous Q_ACCN_SUBM records for the same PyrId, SubmSvc - 837 = "+ pyrSvcSubmSvc.getSubmSvcSeqId());
        QAccnSubm previousQAS = rpmDao.getQAccnSubmByAccnIdAndPyrIdAndSubmSvcId(testDb, accnId, rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId(), pyrSvcSubmSvc.getSubmSvcSeqId());

        logger.info("Load Accn on Accn Detail and submit a claim");
        navigation.navigateToAccnDetailPage();
        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, pyrSvcSubmSvcAbbrev, wait);
        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> newQAccnSubmList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(originalQAccnSubmList.size() + 1, newQAccnSubmList.size());

        logger.info("Make sure new QAS record is unprocessed");
        QAccnSubm unprocessedClaimQAccnSubm = rpmDao.getUnprocessedQAccnSubm(testDb, accnId);
        Assert.assertEquals(unprocessedClaimQAccnSubm.getSubmSvcSeqId(), pyrSvcSubmSvc.getSubmSvcSeqId());

        logger.info("Make sure Pyr on accn is setup for Claim Status - pyr_svc table, enabled, pyr_svc_typ =2, outgoing pyr - Test");
        Assert.assertTrue(isPyrSetupForBatchClaimStatusCheck(rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv), claimStatSubmSvc));
        logger.info("Primary pyr is setup " + pyrAbbrv);

        Accn accn = accessionDao.getAccn(accnId);
        logger.info("Make sure accn is NOT FReported or ZBal - " + accn.getStaId());
        Assert.assertNotEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL);
        Assert.assertNotEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED);

        logger.info("Perform Claim Status Check on Super Search jsp for RealTime CS accnId=" + accnId);
        navigation.navigateToSuperSearchPage();
        superSearch.performCheckClaimStatusActionOnSuperSearch(accnId, wait);

        logger.info("Make sure Accn is in q_claim_status");
        Assert.assertTrue(isAccnInQClaimStatus(accnId, QUEUE_WAIT_TIME));

        logger.info("Wait until Accn is Processed by Claim Status Engine");
        boolean IsAccnOutOfQClaimStatus = waitForClaimStatusEngineToProcessAccn(accnId, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsAccnOutOfQClaimStatus);

        logger.info("Make sure there are no accn_proc_err and accn_pyr_level added, accnId=" + accnId);
        verifyNoProcAndPyrErrsOnAccn(accnId);

        logger.info("Make sure Accn is in Submission queue");
        Assert.assertEquals(accessionDao.getAccnQueByAccnId(accnId).getQTyp(), AccnStatusMap.Q_ACCN_SUBM);

        logger.info("Make sure there is 1 new QAS record created by ClaimStatus Engine");
        List<QAccnSubm> qAccnSubmWithClaimStatusList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qAccnSubmWithClaimStatusList.size(), newQAccnSubmList.size() + 1);

        verifyUnprocessedClaimStatBatchQAccnSubm(accnId, claimStatSubmSvc, previousQAS.getClaimIdSuffix());

        logger.info("Make sure unprocessed 837 QAS record was not deleted");
        QAccnSubm unprocessed837QAccnSubm = rpmDao.getQAccnSubm(testDb, unprocessedClaimQAccnSubm.getDocSeqId());
        Assert.assertEquals(unprocessed837QAccnSubm.getSubmSvcSeqId(), pyrSvcSubmSvc.getSubmSvcSeqId());
        logger.info("Make sure there are no accn_proc_err and accn_pyr_level errors, accnId=" + accnId);
        verifyNoProcAndPyrErrsOnAccn(accnId);
        logger.info("Make sure 1 Message for the user is created");
        verifyQMessage(accnId, message, userFrom, startTime, QUEUE_WAIT_TIME);
    }

    @Test(alwaysRun = true, description = "Report in-progress batch Claim Status Check, Batch Pending - Receipt Acknowledged")
    @Parameters({"accnId", "pyrAbbrv", "pyrSvcSubmSvcAbbrev", "subId", "claimSubmSvc", "message", "userFrom"})
    public void testPFR_704(String accnId, String pyrAbbrv, String pyrSvcSubmSvcAbbrev, String subId, String claimSubmSvc, String message, String userFrom) throws Exception
    {
        logger.info("Starting Test Case PFR_704 - Claim Status Engine - Report in-progress batch Claim Status Check, Batch Pending - Receipt Acknowledged");
        accessionDetail = new AccessionDetail(driver, config, wait);
        navigation = new MenuNavigation(driver, config);

        logger.info("Delete unprocessed QAS");
        rpmDao.deleteUnprocessedQAccnSubmByAccnId(testDb, accnId);
        SubmSvc pyrSvcSubmSvc = rpmDao.getSubmSvcByAbbrev(testDb, pyrSvcSubmSvcAbbrev);
        SubmSvc claimStatSubmSvc = rpmDao.getSubmSvcByAbbrev(testDb, claimSubmSvc);
        logger.info("Delete unprocessed subm_file records associated with the, accnId=" + accnId);
        deleteUnprocessedSubmFileForAccn(accnId);

        List<QAccnSubm> originalQAccnSubmList = rpmDao.getQAccnSubm(testDb, accnId);
        logger.info("Get the number of QAS records ="+originalQAccnSubmList.size());

        logger.info("Make sure there are no accn_proc_err and accn_pyr_level errors, accnId=" + accnId);
        verifyNoProcAndPyrErrsOnAccn(accnId);
        logger.info("Make sure Accn has previous Q_ACCN_SUBM records for the same PyrId, SubmSvc - 837, it has a subm_file, submSvc="+pyrSvcSubmSvc.getSubmSvcSeqId());
        QAccnSubm previous837QAS = rpmDao.getQAccnSubmByAccnIdAndPyrIdAndSubmSvcId(testDb, accnId, rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId(), pyrSvcSubmSvc.getSubmSvcSeqId());
        Assert.assertNotEquals(previous837QAS.getSubmFileSeqId(), 0);
        SubmFile submFile837 = rpmDao.getSubmFile(testDb, previous837QAS.getSubmFileSeqId());

        logger.info("Make sure 837 subm_file record is processed");
        Assert.assertTrue(submFile837.getIsEgateProcessed());
        Assert.assertTrue(submFile837.getIsProcessed());
        logger.info("Make sure Accn has previous Q_ACCN_SUBM records for the same PyrId, SubmSvc - 212, it has a subm_file");
        QAccnSubm previous212QAS = rpmDao.getQAccnSubmByAccnIdAndPyrIdAndSubmSvcId(testDb, accnId, rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId(), claimStatSubmSvc.getSubmSvcSeqId());
        Assert.assertNotEquals(previous212QAS.getSubmFileSeqId(), 0);
        SubmFile submFile212 = rpmDao.getSubmFile(testDb, previous212QAS.getSubmFileSeqId());
        logger.info("Make sure 837 subm_file record is processed");
        Assert.assertTrue(submFile212.getIsEgateProcessed());
        Assert.assertTrue(submFile212.getIsProcessed());

        logger.info("Make sure There is a record in subm_ack_file_link for the subm_file 212");
        List<SubmAckFileLink> submAckFileLink = rpmDao.getSubmAckFileLinkBySubmFileId(testDb, submFile212.submFileSeqId);
        Assert.assertEquals(submAckFileLink.size(), 1);
        logger.info("Make sure 1 Claim Status Transaction is created with Batch Pending - Receipt Acknowledged");
        navigation.navigateToAccnDetailPage();
        logger.info("Load accn on Accn Detail - " + accnId);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        logger.info("Make sure Check Claim Status Checkbox is not displayed");
        Assert.assertFalse(accessionDetail.checkClaimStatusCheckbox().isDisplayed());
        logger.info("Scroll to Claim Status Transaction grid");
//        scrollToElement(accessionDetail.claimStatusTransactionsCollapsedRadio());
//        logger.info("Count rows and columns in Claim Status Transaction table");
//        int rowCount = accessionDetail.getRowNumInClaimTransactionTbl();
//        logger.info("There are existing rows - " + rowCount);
//        logger.info("There is a status in Claim Status Transaction table - "+accessionDetail.statusInClaimTransactionTblCelData(rowCount, 6).getText());
//        Assert.assertTrue(accessionDetail.statusInClaimTransactionTblCelData(rowCount, 6).getText().equalsIgnoreCase("Batch Pending - Receipt Acknowledged"));
//        logger.info("There is a PyrAbbrev in Claim Status Transaction table - "+accessionDetail.statusInClaimTransactionTblCelData(rowCount, 2).getText());
//        Assert.assertTrue(accessionDetail.statusInClaimTransactionTblCelData(rowCount, 2).getText().equalsIgnoreCase(pyrAbbrv));

    }


    @Test(alwaysRun = true, description = "Claim Status Engine Processes Specific Claim")
    @Parameters({"accnId", "pyrAbbrv", "claimSuffixIds"})
    public void test_ClaimStatusEngineProcessesSpecificClaim(String accnId, String pyrAbbrv, String claimSuffixIds) throws Exception
    {
        MenuNavigation navigation = new MenuNavigation(driver, config);
        SuperSearch superSearch = new SuperSearch(driver, config, wait);
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));

        logger.info("Starting Test Case test_ClaimStatusEngineProcessesSpecificClaim, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", claimSuffixIds="+claimSuffixIds);
        List<String> claimSuffixIdsToCheck = Arrays.asList(StringUtils.split(claimSuffixIds, ","));
        List<QAccnSubm> submissionsToDelete = new ArrayList<>();
        for (QAccnSubm qas : rpmDao.getQAccnSubm(null, accnId))
        {
            if (!claimSuffixIdsToCheck.contains(qas.getClaimIdSuffix()))
            {
                submissionsToDelete.add(qas);
            }
        }
        logger.info("Deleting submissions, accnId="+accnId+", submissionsToDelete="+submissionsToDelete);
        submissionDao.deleteSubmissions(submissionsToDelete);

        logger.info("Verifying payor is set up for real-time claim status, accnId="+accnId+", pyrAbbrv="+pyrAbbrv);
        Assert.assertTrue(isPyrSetupForRealTimeClaimStatusCheck(rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv)));

        logger.info("Performing super search claim status check, accnId="+accnId+", pyrAbbrv="+pyrAbbrv);
        navigation.navigateToSuperSearchPage();
        superSearch.performCheckClaimStatusActionOnSuperSearch(accnId, wait);

        Assert.assertTrue(waitForClaimStatusEngineToProcessAccn(accnId, QUEUE_WAIT_TIME), "Accession was not removed from QClaimStatus, accnId="+accnId);

        logger.info("Verifying Claim Status History data, accnId="+accnId+", pyrAbbrv="+pyrAbbrv);
        List<AccnClmstatHist> accnClmstatHists = rpmDao.getAccnClaimStatHistoryByAccnId(testDb, accnId);
        Assert.assertEquals(accnClmstatHists.size(), claimSuffixIdsToCheck.size(), "Claim Status History Check count does not match expected number");
        List<String> transIds = new ArrayList<>();
        for (AccnClmstatHist accnClmstatHist : accnClmstatHists)
        {
            transIds.add(accnClmstatHist.getTransId());
        }

        logger.info("Verifying Claim Status data, accnId="+accnId+", pyrAbbrv="+pyrAbbrv);
        List<ClaimStatus> claimStatusList = rpmDao.getClaimStatusList(accnId, 0);
        Assert.assertEquals(claimStatusList.size(), claimSuffixIdsToCheck.size(), "Claim Status count does not match expected number");
        List<String> claimStatusSuffixIdsChecked = new ArrayList<>();
        for (ClaimStatus claimStatus : claimStatusList)
        {
            claimStatusSuffixIdsChecked.add(claimStatus.getClaimIdSuffix());
        }
        Collections.sort(claimSuffixIdsToCheck);
        Collections.sort(claimStatusSuffixIdsChecked);
        Assert.assertEquals(claimStatusSuffixIdsChecked, claimSuffixIdsToCheck, "Claim Status Suffix IDs Checked do not match expected");

        logger.info("Identifying docSeqId for specific claim status check, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", claimSuffixIdToCheck="+claimSuffixIdsToCheck.get(claimSuffixIdsToCheck.size()-1));
        Integer docSeqId = null;
        for (QAccnSubm qas : rpmDao.getQAccnSubm(null, accnId))
        {
            if (StringUtils.equals(qas.getClaimIdSuffix(), claimSuffixIdsToCheck.get(claimSuffixIdsToCheck.size()-1)))
            {
                docSeqId = qas.getDocSeqId();
            }
        }
        Assert.assertNotNull(docSeqId, "DocSeqId for specific claim status check must not be null");
        logger.info("Performing claim status check for single claim, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", docSeqId="+docSeqId);
        rpmDao.insertAccnIntoQClaimStatus(null, accnId, "xqatester", docSeqId);

        Assert.assertTrue(waitForClaimStatusEngineToProcessAccn(accnId, QUEUE_WAIT_TIME), "Accession was not removed from QClaimStatus, accnId="+accnId);

        logger.info("Verifying Claim Status History data for single claim check, accnId="+accnId+", pyrAbbrv="+pyrAbbrv);
        accnClmstatHists = rpmDao.getAccnClaimStatHistoryByAccnId(testDb, accnId);
        Assert.assertEquals(accnClmstatHists.size(), claimSuffixIdsToCheck.size()+1, "Claim Status History Check count does not match expected number");

        logger.info("Verifying Claim Status data, accnId="+accnId+", pyrAbbrv="+pyrAbbrv);
        claimStatusList = rpmDao.getClaimStatusList(accnId, 0);
        Assert.assertEquals(claimStatusList.size(), claimSuffixIdsToCheck.size()+1, "Claim Status count does not match expected number");
        Assert.assertEquals(claimStatusList.get(claimStatusList.size()-1).getClaimIdSuffix(), claimSuffixIdsToCheck.get(claimSuffixIdsToCheck.size()-1), "Claim Status Suffix ID Checked do not match expected");
    }

    private void deleteUnprocessedSubmFileForAccn(String accnId) throws XifinDataAccessException
    {
        List<QAccnSubm> originalQAccnSubmList = rpmDao.getQAccnSubm(testDb, accnId);
        for (QAccnSubm accnSubm : originalQAccnSubmList)
        {
            SubmFile submFile = rpmDao.getSubmFile(testDb, accnSubm.submFileSeqId);
            if (!submFile.getIsEgateProcessed())
            {
                rpmDao.updateAccnProcSubmFileSeqId(testDb, submFile.getSubmFileSeqId());
                rpmDao.deleteQAccnSubmBySubmFileSeqId(testDb, submFile.getSubmFileSeqId());
                rpmDao.deleteSubmFile(testDb, submFile.getSubmFileSeqId());
            }
        }
    }


    private boolean isAccnInQClaimStatus(String accnId, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isInQueue = rpmDao.isAccnInQClaimStatus(testDb, accnId);
        while (!isInQueue && System.currentTimeMillis() < maxTime)
        {
            logger.info("Retriving the record from Q_CLAIM_STATUS");
            Thread.sleep(QUEUE_POLL_TIME);
            isInQueue = rpmDao.isAccnInQClaimStatus(testDb, accnId);
        }
        return isInQueue;
    }

    protected void cleanQASForPyrOnAccn(String accnId, int pyrId) throws Exception
    {
        logger.info("Clearing QAS for Pyr on accn, accnId=" + accnId+", pyrId"+ pyrId);
        logger.info("Deleting Unprocessed QAccnSubm records, accnId=" + accnId);
        List<QAccnSubm> qaccnSubmList = rpmDao.getQAccnSubmByAccnIdAndClaimIdSuffixMoreThan01AndNull(accnId);
        for(QAccnSubm qAccnSubm : qaccnSubmList){
            rpmDao.deleteSubmClaimAuditByDocId(testDb, qAccnSubm.getDocSeqId());
        }
        rpmDao.deleteUnprocessedQAccnSubmByAccnId(testDb, accnId);
        logger.info("Get the number of QAS records ="+qaccnSubmList.size());
        for (QAccnSubm accnSubm : qaccnSubmList)
        {
            if (accnSubm.getPyrId()==pyrId)
            {
                logger.info("Deleting SubmFileAudit records, accnId=" + accnId);
                rpmDao.deleteSubmClaimAuditByDocId(testDb, accnSubm.getDocSeqId());
                if(accnSubm.getSubmFileSeqId()!=0)
                {
                    SubmFile submFile = rpmDao.getSubmFile(testDb, accnSubm.submFileSeqId);
                    rpmDao.deleteSubmFileAuditByAccnIdAndSubmFileSeqId(testDb, accnId, submFile.getSubmFileSeqId());
                    logger.info("Updating AccnProc subm file records, accnId=" + accnId + ", SubmFileSeqId()=" + submFile.getSubmFileSeqId());
                    rpmDao.updateAccnProcSubmFileSeqId(testDb, submFile.getSubmFileSeqId());
                    logger.info("Updating QAS.subm_file_seq_id record to 0, accnId=" + accnId + ", SubmFileSeqId()=" + submFile.getSubmFileSeqId());
                    rpmDao.updateQASSubmFileSeqIdBySubmFileSeq(testDb, submFile.getSubmFileSeqId());
                    logger.info("Deleting SubmFile records, accnId=" + accnId + ", SubmFileSeqId()=" + submFile.getSubmFileSeqId());
                    rpmDao.deleteSubmFile(testDb, submFile.getSubmFileSeqId());

                }
                logger.info("Deleting QAS records, accnId=" + accnId+", DocSeqId="+accnSubm.getDocSeqId());
                rpmDao.deleteQAccnSubmByDocSeqId(testDb, accnSubm.getDocSeqId());
            }
        }
    }

    protected void cleanQASBatchClaimStatus(String accnId, int submSvcSeqId) throws Exception
    {
        logger.info("Clearing QAS for Batch Claim Status, accnId="+accnId+", submSvcSeqId="+ submSvcSeqId);
        List<QAccnSubm> qaccnSubmList = rpmDao.getQAccnSubmByAccnIdAndSubmSvc(testDb, accnId, submSvcSeqId);
        for(QAccnSubm qAccnSubm : qaccnSubmList){
            rpmDao.deleteSubmClaimAuditByDocId(testDb, qAccnSubm.getDocSeqId());
        }
        logger.info("Get the number of QAS records ="+qaccnSubmList.size());
        for (QAccnSubm accnSubm : qaccnSubmList)
        {
                logger.info("Deleting SubmFileAudit records, accnId=" + accnId);
                rpmDao.deleteSubmClaimAuditByDocId(testDb, accnSubm.getDocSeqId());
                if(accnSubm.getSubmFileSeqId()!=0)
                {
                    SubmFile submFile = rpmDao.getSubmFile(testDb, accnSubm.submFileSeqId);
                    logger.info("Updating AccnProc subm file records, accnId=" + accnId + ", SubmFileSeqId()=" + submFile.getSubmFileSeqId());
                    rpmDao.updateAccnProcSubmFileSeqId(testDb, submFile.getSubmFileSeqId());
                    logger.info("Updating QAS.subm_file_seq_id record to 0, accnId=" + accnId + ", SubmFileSeqId()=" + submFile.getSubmFileSeqId());
                    rpmDao.updateQASSubmFileSeqIdBySubmFileSeq(testDb, submFile.getSubmFileSeqId());
                    logger.info("Deleting SubmFile records, accnId=" + accnId + ", SubmFileSeqId()=" + submFile.getSubmFileSeqId());
                    rpmDao.deleteSubmFile(testDb, submFile.getSubmFileSeqId());
                }
                logger.info("Deleting QAS records, accnId=" + accnId+", doc seq id="+accnSubm.getDocSeqId());
                rpmDao.deleteQAccnSubmByDocSeqId(testDb, accnSubm.getDocSeqId());
        }
    }
    private boolean waitForClaimStatusEngineToProcessAccn(String accnId, long maxTime)
            throws InterruptedException, XifinDataAccessException
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isQClaimStatusRecordExist = rpmDao.isAccnInQClaimStatus(testDb, accnId);
        while (isQClaimStatusRecordExist && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for Claim Status Engine to process q_claim_status record,  accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME);
            isQClaimStatusRecordExist = rpmDao.isAccnInQClaimStatus(testDb, accnId);
        }
        return !isQClaimStatusRecordExist;
    }

    private boolean isPyrSetupForRealTimeClaimStatusCheck(Pyr pyr) throws XifinDataAccessException
    {
        logger.info("Making sure payor is configured for real-time claim status checks, pyrAbbrv="+pyr.getPyrAbbrv()+", pyrId="+pyr.getPyrId()+", outPyrId=TEST");
        List<PyrSvc> pyrSvc = rpmDao.getPyrSvcsByPyrId(testDb, pyr.getPyrId());
        return !pyrSvc.isEmpty() && pyrSvc.get(0).getPyrSvcTypId() == MiscMap.PYR_SVC_TYP_CLAIM_STATUS && pyrSvc.get(0).getOutPyrId().equals("TEST");
    }

    private boolean isPyrSetupForBatchClaimStatusCheck(Pyr pyr, SubmSvc submSvc276) throws XifinDataAccessException
    {
        logger.info("Make sure Pyr on accn is setup for Batch Claim Status - pyr_svc table, enabled, pyr_svc_typ =1, outgoing pyr - Test");
        boolean isPyrSetupForBatchClaimStatusCheck = false;
        List<PyrSvc> pyrSvc = rpmDao.getPyrSvcsByPyrId(testDb, pyr.getPyrId());
        if (pyrSvc.get(0).getPyrSvcTypId() == MiscMap.PYR_SVC_TYP_BATCH_CLAIM_STATUS && pyrSvc.get(0).getOutPyrId().equals("TEST") && pyrSvc.get(0).getSubmSvcSeqId() == submSvc276.getSubmSvcSeqId())
        {
            isPyrSetupForBatchClaimStatusCheck = true;
        }
        return isPyrSetupForBatchClaimStatusCheck;
    }

    private void fixUnfixedAccnErrors(String accnId) throws Exception
    {
        logger.info("Load accn on Accn Detail - " + accnId);
        accessionDetail = new AccessionDetail(driver, config, wait);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        logger.info("Go to Accession Errors grid and click 'Show all unfixed errors' radio button");
        accessionDetail.setShowAllUnfixedErr();

        logger.info("Fix proc level and pyr level errors on Accn Detail - " + accnId);
        int numberOfErrors = accessionDetail.getTotalRowSize(accessionDetail.currentAccnErrTable());
        numberOfErrors -= 1;
        logger.info("There are unfixed errors " + numberOfErrors);
        if (numberOfErrors > 1)
        {
            accessionDetail.setCurrAccnErrAction(3);
        }
        accessionDetail.setCurrAccnErrAction(2);
        accessionDetail.saveAndClear(wait);
    }

    private Set<AccnPyrErr> createExpectedAccnPyrErrs(String accnId, String statusDate, List<String> expectedPyrErrList) throws XifinDataAccessException, XifinDataNotFoundException, ParseException
    {
        Accn accn = accessionDao.getAccn(accnId);
        Set<AccnPyrErr> expectedAccnPyrErrs = new HashSet<>();
        for (String err : expectedPyrErrList)
        {
            String[] errInfo = StringUtils.split(err, ":");
            String pyrAbbrev = errInfo.length > 0 ? errInfo[0] : StringUtils.EMPTY;
            String errAbbrev = errInfo.length > 1 ? errInfo[1] : StringUtils.EMPTY;
            int pyrPrio = Integer.parseInt(errInfo.length > 2 ? errInfo[2] : StringUtils.EMPTY);
            Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrev);
            ErrCd errCd = rpmDao.getErrCd(testDb, errAbbrev, EpConstants.DNL_TBL_ID_CLAIM_STATUS, accn.getDos());
            AccnPyrErr ape = new AccnPyrErr();
            ape.setErrSeq(0);
            ape.setErrCd(errCd.getErrCd());
            ape.setAudUser(null);
            ape.audRecId = 0;
            ape.audDt = null;
            ape.setErrDt(new Date(DATE_FORMAT_MMDDYYYY.parse(statusDate).getTime()));
            ape.setErrSeq(0);
            ape.setFixDt(null);
            ape.setSubsId(null);
            ape.setClaimIdSuffix(null);
            ape.setAccnId(accnId);
            ape.setErrCd(errCd.getErrCd());
            ape.setPyrId(pyr.getPyrId());
            if (pyrPrio == 2)
            {
                ape.setPyrPrio(2);
            }
            else
            {
                ape.setPyrPrio(1);
            }
            if (errCd.getClaimStatusErrTypId() == EpConstants.CLAIM_STATUS_ERR_TYP_ID_ACK)
            {
                ape.setFixDt(ape.getErrDt());
                ape.setFixUserId("ClaimStatusEngine");
            }
            expectedAccnPyrErrs.add(ape);
        }
        return expectedAccnPyrErrs;
    }

    private Set<AccnPyrErr> createActualAccnPyrErrs(List<AccnPyrErr> newAccnPyrErrs)
    {
        Set<AccnPyrErr> actualAccnPyrErrs = new HashSet<>();
        for (AccnPyrErr ape : newAccnPyrErrs)
        {
            ape.setErrSeq(0);
            ape.setClaimIdSuffix(null);
            ape.setAudUser(null);
            ape.audRecId = 0;
            ape.audDt = null;
            ape.setErrSeq(0);
            ape.setSubsId(null);
            ape.setResultCode(0);
            actualAccnPyrErrs.add(ape);
        }
        return actualAccnPyrErrs;
    }

    private Set<AccnProcErr> createActualAccnProcErrs(List<AccnProcErr> newAccnProcErrs)
    {
        Set<AccnProcErr> actualAccnProcErrs = new HashSet<>();
        for (AccnProcErr ape : newAccnProcErrs)
        {
            ape.setErrSeq(0);
            ape.setAccnProcSeqId(1);
            ape.setAudUser(null);
            ape.audRecId = 0;
            ape.audDt = null;
            ape.setBilPrcAsMoney(new Money());
            ape.setDocSeqId(null);
            ape.setErrSeq(0);
            ape.setMods(null);
            ape.setSubsId(null);
            ape.setResultCode(0);
            ape.setNote(null);
            actualAccnProcErrs.add(ape);
        }
        return actualAccnProcErrs;
    }

    private Set<AccnProcErr> createExpectedAccnProcErrs(String accnId, String statusDate, List<String> expectedProcErrList) throws XifinDataAccessException, XifinDataNotFoundException, ParseException
    {
        Accn accn = accessionDao.getAccn(accnId);
        Set<AccnProcErr> expectedAccnProcErrs = new HashSet<>();
        for (String err : expectedProcErrList)
        {
            String[] errInfo = StringUtils.split(err, ":");
            String pyrAbbrev = errInfo.length > 0 ? errInfo[0] : StringUtils.EMPTY;
            String procCd = errInfo.length > 1 ? errInfo[1] : StringUtils.EMPTY;
            String errAbbrev = errInfo.length > 2 ? errInfo[2] : StringUtils.EMPTY;
            int pyrPrio = Integer.parseInt(errInfo.length > 3 ? errInfo[3] : StringUtils.EMPTY);
            Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrev);
            ErrCd errCd = rpmDao.getErrCd(testDb, errAbbrev, EpConstants.DNL_TBL_ID_CLAIM_STATUS, accn.getDos());
            AccnProcErr ape = new AccnProcErr();
            ape.setAccnId(accnId);
            ape.setAccnProcSeqId(1);
            ape.setErrCd(errCd.getErrCd());
            ape.setPyrId(pyr.getPyrId());
            ape.setErrDt(new Date(DATE_FORMAT_MMDDYYYY.parse(statusDate).getTime()));
            ape.setProcId(procCd);
            ape.setResultCode(0);
            if (pyrPrio == 2)
            {
                ape.setPyrPrio(2);
            }
            else
            {
                ape.setPyrPrio(1);
            }
            if (errCd.getClaimStatusErrTypId() == EpConstants.CLAIM_STATUS_ERR_TYP_ID_ACK)
            {
                ape.setFixDt(ape.getErrDt());
                ape.setFixUserId(AccnStatusMap.CLAIM_STATUS_ENGINE_USER);
            }
            expectedAccnProcErrs.add(ape);
        }
        return expectedAccnProcErrs;
    }

    private void verifyNoProcAndPyrErrsOnAccn(String accnId) throws XifinDataAccessException
    {
        List<AccnProcErr> origAccnProcErrs = accessionDao.getAccnProcErrsByAccnId(accnId, false, false);
        Assert.assertEquals(origAccnProcErrs.size(), 0);
        List<AccnPyrErr> origAccnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        Assert.assertEquals(origAccnPyrErrs.size(), 0);
    }

    private void verifyQMessage(String accnId, String message, String userFrom, Timestamp startTime, long maxTime) throws XifinDataAccessException, InterruptedException
    {
        long startWaitTime = System.currentTimeMillis();
        maxTime += startWaitTime;
        List<QMessage> qMessages = rpmDao.getNewMessagesByMessage(testDb, message, accnId, startTime);
        while (qMessages.size() == 0 && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for Claim Status Engine to create a qMessage  accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startWaitTime) + "s"+", startTime="+startTime+" message="+message);
            Thread.sleep(QUEUE_POLL_TIME);
            qMessages = rpmDao.getNewMessagesByMessage(testDb, message, accnId, startTime);
        }
        Assert.assertEquals(qMessages.size(), 1);
        Assert.assertTrue(qMessages.get(0).getMessage().contains(accnId));
        Assert.assertEquals(qMessages.get(0).getFromUserId(), userFrom);
    }

    private void verifyProcAndPyrErrorsOnAccn(String accnId, String statusDate, String pyrErrs, String procErrs) throws XifinDataAccessException, XifinDataNotFoundException, ParseException
    {
        logger.info("Create Expected Proc Errors added to the accn");
        List<String> expectedProcErrList = Arrays.asList(StringUtils.split(procErrs, ","));
        Set<AccnProcErr> expectedAccnProcErrs = createExpectedAccnProcErrs(accnId, statusDate, expectedProcErrList);
        logger.info("Create Expected Pyr Errors added to the accn");
        List<String> expectedPyrErrList = Arrays.asList(StringUtils.split(pyrErrs, ","));
        Set<AccnPyrErr> expectedAccnPyrErrs = createExpectedAccnPyrErrs(accnId, statusDate, expectedPyrErrList);
        logger.info("Create Lists of Actual Proc and Pyr Errors added to the accn");
        Set<AccnPyrErr> actualAccnPyrErrs = createActualAccnPyrErrs(accessionDao.getAccnPyrErrsByAccnId(accnId));
        Set<AccnProcErr> actualAccnProcErrs = createActualAccnProcErrs(accessionDao.getAccnProcErrsByAccnId(accnId));

        logger.info("Make sure expected and actual proc errors are matching");
        logger.info("actualAccnProcErrs "+actualAccnProcErrs);
        logger.info("expectedAccnProcErrs "+expectedAccnProcErrs);
        Assert.assertEquals(actualAccnProcErrs, expectedAccnProcErrs);
        logger.info("Make sure expected and actual pyr errors are matching");
        Assert.assertEquals(actualAccnPyrErrs, expectedAccnPyrErrs);
    }

    private void verifyUnprocessedClaimStatBatchQAccnSubm(String accnId, SubmSvc claimStatSubmSvc, String suffixId) throws XifinDataAccessException, XifinDataNotFoundException
    {
        logger.info("Make sure new QAS record created with subm_svc from pyr_svc");
        QAccnSubm clmStatBatchQAccnSubm = rpmDao.getUnprocessedQAccnSubmByAccnIdAndUser(testDb, accnId, "ClaimStatusEngine");
        Assert.assertEquals(clmStatBatchQAccnSubm.getSubmSvcSeqId(), claimStatSubmSvc.getSubmSvcSeqId());
        Assert.assertEquals(clmStatBatchQAccnSubm.getClaimTypId(), 0);
        Assert.assertEquals(clmStatBatchQAccnSubm.getClaimIdSuffix(), suffixId);
        Assert.assertEquals(clmStatBatchQAccnSubm.getBillingFacId(), 0);
        Assert.assertNull(clmStatBatchQAccnSubm.getSubmittedSubsId());
    }

    private void cleanClaimStatusTablesAndClaimStatHistoryByAccnId(@Optional String accnId) throws XifinDataAccessException
    {
        logger.info("Deleting all QClaimStatus records for accession, accnId=" + accnId);
        rpmDao.deleteQClaimStatus(testDb, accnId);
        logger.info("Deleting all AccnClmStatHist records for accession, accnId=" + accnId);
        rpmDao.deleteAccnClaimStatHistByAccnId(testDb, accnId);

        logger.info("Deleting all claim status transaction data for accession, accnId=" + accnId);
        List<ClaimStatus> claimStatuses = rpmDao.getClaimStatusList(accnId, 0);
        for (ClaimStatus claimStatus : claimStatuses)
        {
            rpmDao.deleteClaimStatusStatusRecordsByClaimId(claimStatus.getSeqId());
            rpmDao.deleteClaimStatusProcRecordsByClaimId(claimStatus.getSeqId());
            rpmDao.deleteClaimStatusRecordBySeqId(claimStatus.getSeqId());
        }
    }

    @Deprecated
    protected void navigateFromAccnDetailToSuperSearchPF() throws Exception
    {
        logger.info("Navigate from Accn Detail to Accn Super Search");
        HeaderNavigation headerNavigation = new HeaderNavigation(driver, config);
        AccessionNavigation accessionNavigation = new AccessionNavigation(driver, config);
        switchToDefaultWinFromFrame();
        WebElement contentFrame = driver.findElement(By.id("content"));
        accessionNavigation.superSearchLinkPF();
        wait.until(ExpectedConditions.stalenessOf(contentFrame));
        contentFrame = driver.findElement(By.id("content"));
        driver.switchTo().frame(contentFrame);
        wait.until(ExpectedConditions.stalenessOf(contentFrame));
        contentFrame = driver.findElement(By.id("platformiframe"));
        driver.switchTo().frame(contentFrame);
    }
    @Deprecated
    protected void updateRpmEngines(List<Integer> taskTypIds, String startTime, String interval, int concurrency)
            throws Exception
    {
        Set<String> taskTypAbbrvs = new TreeSet<>();
        for (int taskTypId : taskTypIds)
        {
            TaskTyp taskTyp = rpmDao.getTaskTyp(testDb, taskTypId);
            taskTypAbbrvs.add(taskTyp.getDescr());
        }

        logger.info("Updating RPM engines, tasks=" + taskTypAbbrvs + ", concurrency=" + concurrency);
        HeaderNavigation headerNavigation = new HeaderNavigation(driver, config);
        headerNavigation.fileMaintenanceTab();
        FileMaintenanceNavigation fileMaintenanceNavigation = new FileMaintenanceNavigation(driver, config);
        fileMaintenanceNavigation.taskSchedulerLink();
        TaskScheduler taskScheduler = new TaskScheduler(driver);
        switchToFrame(taskScheduler.contentFrame());
        taskScheduler.setTaskSchedules(taskTypIds, startTime, interval, concurrency);
        switchToDefaultWinFromFrame();
    }
    @Deprecated
    public void enterSubmitClaimInfo(String pyrAbbrv, String subId, String submSvcAbbrev, AccessionDetail accessionDetail) throws Exception
    {
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("edithdtbl_submitClaims"))));
        accessionDetail.setPayorAndSubscriberID(pyrAbbrv + " | " + subId, wait);
        wait.until(ExpectedConditions.elementToBeClickable(accessionDetail.actionText()));
        accessionDetail.setActionText("Original", wait);
        accessionDetail.setFormatText(submSvcAbbrev, wait);
        accessionDetail.okPopupBtn().click();
    }
    @Deprecated
    protected void navigateToSuperSearch() throws Exception
    {
        logger.info("Navigate to Accn Super Search");
        HeaderNavigation headerNavigation = new HeaderNavigation(driver, config);
        headerNavigation.accessionTab();
        AccessionNavigation accessionNavigation = new AccessionNavigation(driver, config);
        WebElement contentFrame = driver.findElement(By.id("content"));
        accessionNavigation.superSearchLink();
        wait.until(ExpectedConditions.stalenessOf(contentFrame));
        contentFrame = driver.findElement(By.id("content"));
        driver.switchTo().frame(contentFrame);
    }
    @Deprecated
    private void deleteFixedProcAndPyrErrByAccnId(String accnId) throws XifinDataAccessException
    {
        logger.info("Deleting Fixed accn pyr and proc errors for given accnId = " + accnId);
        rpmDao.deleteFixedAccnProcErr(testDb, accnId);
        rpmDao.deleteFixedAccnPyrErr(testDb, accnId);
    }
    @Deprecated
    private void unfixFixedProcAndPyrErrByAccnId(String accnId) throws XifinDataAccessException
    {
        logger.info("Deleting Fixed accn pyr and proc errors for given accnId = "+accnId);
        rpmDao.deleteFixedAccnProcErr(testDb, accnId);
        rpmDao.deleteFixedAccnPyrErr(testDb, accnId);
    }
}
