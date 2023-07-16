package com.newXp.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnEligHist.AccnEligHist;
import com.mbasys.mars.ejb.entity.accnProcErr.AccnProcErr;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.eligResp.EligResp;
import com.mbasys.mars.ejb.entity.eligRoster.EligRoster;
import com.mbasys.mars.ejb.entity.pt.Pt;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrElig.PyrElig;
import com.mbasys.mars.ejb.entity.qAccnSubm.QAccnSubm;
import com.mbasys.mars.eligibility.EligMap;
import com.mbasys.mars.errorProcessing.EpConstants;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.DbErrorMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.accession.accessionProcessing.LoadAccession;
import com.overall.menu.MenuNavigation;
import com.pfEngines.tests.EligibilityBaseTest;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openqa.selenium.Keys;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.AssertJUnit.assertEquals;

public class AccessionDetailEligibilityTest extends EligibilityBaseTest
{   private AccessionDetail accessionDetail;
    private LoadAccession loadAccession;
    private MenuNavigation navigation;
    protected static long QUEUE_WAIT_TIME_MS = TimeUnit.MINUTES.toMillis(2);
    private static final Date TODAYS_DT = new Date(DateUtils.truncate(new java.util.Date(), Calendar.DATE).getTime());
    Date newDt = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(365));

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
    public void beforeMethod(Method method)
    {
        try
        {
            logger.info("Running BeforeMethod");
            logIntoSso(parameters.getSsoUsername(), parameters.getSsoPassword());
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

    @Test(priority = 1, description = "Eligibility check-Payor and Proc errors have pyr_prio =0 when Payor is translated")
    @Parameters({"accnId", "origPyrAbbrv", "origSubId", "user"})
    public void testXPR_840(String accnId, String origPyrAbbrv, String origSubId, String user) throws Exception
    {
        logger.info("Starting Test Case: XPR-840, accnId=" + accnId + ", origPyrAbbrv=" + origPyrAbbrv + ", subId=" + origSubId);
        loadAccession = new LoadAccession(driver);
        accessionDetail = new AccessionDetail(driver, config, wait);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        List<AccnPyr> currentAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int currentPyrId = currentAccnPyrs.get(0).getPyrId();
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        int origPyrId = origPyr.getPyrId();

        setOriginalPayorOnAccession(accnId, origPyrAbbrv, accessionDetail, currentPyrId, origPyrId);

        Accn accn = accessionDao.getAccn(accnId);

        if (accn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED)
        {
            if (accessionDetail.isAccnLoaded(accnId, wait))
            {
                accessionDetail.saveAndClearBtn().click();
            }

            Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));
            accn = accessionDao.getAccn(accnId);
            loadAccession.setAccnId(accnId, wait);
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }
        //  Assert.assertEquals(accn.getStaId(),AccnStatusMap.ACCN_STATUS_PRICED);
        logger.info("Validating original proc-level errors, accnId=" + accnId);
        List<AccnProcErr> origAccnProcErrs = accessionDao.getAccnProcErrsByAccnId(accnId);
        Assert.assertEquals(origAccnProcErrs.size(), 1);
        Assert.assertEquals(origAccnProcErrs.get(0).getPyrPrio(), 1);
        Assert.assertEquals(origAccnProcErrs.get(0).getSubsId(), origSubId);
        Assert.assertEquals(origAccnProcErrs.get(0).getPyrId(), origPyrId);
        Assert.assertNotEquals(origAccnProcErrs.get(0).getAccnProcSeqId(), 0);
        Assert.assertEquals(rpmDao.getErrCd(testDb, origAccnProcErrs.get(0).getErrCd()).getErrGrpId(), EpConstants.ERR_GRP_DENIAL);
        AccnProcErr expectedAccnProcErr = createExpectedAccnProcErr(origAccnProcErrs);

        logger.info("Validating original payor-level errors, accnId=" + accnId);
        List<AccnPyrErr> origAccnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        Assert.assertEquals(origAccnPyrErrs.size(), 1);
        Assert.assertEquals(origAccnPyrErrs.get(0).getPyrPrio(), 1);
        Assert.assertEquals(origAccnPyrErrs.get(0).getSubsId(), origSubId);
        Assert.assertEquals(origAccnPyrErrs.get(0).getPyrId(), origPyrId);
        Assert.assertEquals(rpmDao.getErrCd(testDb, origAccnPyrErrs.get(0).getErrCd()).getErrGrpId(), EpConstants.ERR_GRP_DENIAL);
        AccnPyrErr expectedAccnPyrErr = createExpectedAccnPyrErr(origAccnPyrErrs);
        expectedAccnPyrErr.setFixDt(TODAYS_DT);
        expectedAccnPyrErr.setPrioZeroFix("Y");
        expectedAccnPyrErr.setFixUserId(user);

        logger.info("Check Eligibility checkbox");
        clickHiddenPageObject(accessionDetail.checkEligibilityCheckbox(), 0);
        logger.info("Click Submit button");
        accessionDetail.saveAndClear(wait);

        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(newAccnPyrs.size(), 1);
        Assert.assertNotEquals(newAccnPyrs.get(0).getPyrId(), origPyrId);

        List<AccnProcErr> actualAccnProcErrs = accessionDao.getAccnProcErrsByAccnId(accnId);
        Assert.assertEquals(actualAccnProcErrs.size(), 1);
        actualAccnProcErrs.get(0).setAudUser(null);
        actualAccnProcErrs.get(0).audDt = null;
        actualAccnProcErrs.get(0).setNote(null);
        actualAccnProcErrs.get(0).audRecId = 0;
        actualAccnProcErrs.get(0).setResultCode(0);
        Assert.assertEquals(actualAccnProcErrs.get(0), expectedAccnProcErr);

        List<AccnPyrErr> actualAccnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId);
        Assert.assertEquals(actualAccnPyrErrs.size(), 1);
        actualAccnPyrErrs.get(0).setAudUser(null);
        actualAccnPyrErrs.get(0).audDt = null;
        actualAccnPyrErrs.get(0).setNote(null);
        actualAccnPyrErrs.get(0).audRecId = 0;
        actualAccnPyrErrs.get(0).setResultCode(0);
        Assert.assertEquals(actualAccnPyrErrs.get(0), expectedAccnPyrErr);
    }

    @Test(priority = 1, description = "Eligibility check-Payor errors have pyr_prio =1 when original Payor is back")
    @Parameters({"accnId", "origPyrAbbrv", "subId"})
    public void testXPR_886(String accnId, String origPyrAbbrv, String subId) throws Exception
    {

        logger.info("Starting Test Case: XPR-886, accnId=" + accnId + ", origPyrAbbrv=" + origPyrAbbrv + ", subId=" + subId);
        loadAccession = new LoadAccession(driver);
        accessionDetail = new AccessionDetail(driver, config, wait);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        List<AccnPyr> currentAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int currentPyrId = currentAccnPyrs.get(0).getPyrId();
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        int origPyrId = origPyr.getPyrId();

        setOriginalPayorOnAccession(accnId, origPyrAbbrv, accessionDetail, currentPyrId, origPyrId);

        logger.info("Validating original payor-level errors, accnId=" + accnId);
        List<AccnPyrErr> origAccnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        Assert.assertEquals(origAccnPyrErrs.size(), 1);
        Assert.assertNotEquals(origAccnPyrErrs.get(0).getPyrPrio(), 0);
        Assert.assertNotEquals(origAccnPyrErrs.get(0).getPyrId(), 0);
        Assert.assertNotEquals(origAccnPyrErrs.get(0).getSubsId(), null);
        Assert.assertEquals(rpmDao.getErrCd(testDb, origAccnPyrErrs.get(0).getErrCd()).getErrGrpId(), EpConstants.ERR_GRP_DENIAL);

        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Check Eligibility checkbox and click Submit button");
        clickHiddenPageObject(accessionDetail.checkEligibilityCheckbox(), 0);

        accessionDetail.saveAndClear(wait);
        loadAccession.setAccnId(accnId, wait);

        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int newPyrId = newAccnPyrs.get(0).getPyrId();

        logger.info("Verifying payor is translated, accnId=" + accnId + ", origPyrId=" + origPyrId + ", newPyrId=" + newPyrId);
        Assert.assertNotEquals(origPyrId, newPyrId);

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        AccnPyrErr expectedAccnPyrErr = createExpectedAccnPyrErr(origAccnPyrErrs);

        expectedAccnPyrErr.setAudUser(null);
        expectedAccnPyrErr.audDt = null;
        expectedAccnPyrErr.audRecId = 0;
        expectedAccnPyrErr.setResultCode(0);
        expectedAccnPyrErr.setPyrPrio(1);

        // Clean up accn elig hist to avoid circularelig error
        accessionDao.deleteAccnEligHistByAccnId(accnId);

        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Check Eligibility checkbox second time and click Submit button");
        clickHiddenPageObject(accessionDetail.checkEligibilityCheckbox(), 0);
        accessionDetail.saveAndClear(wait);

        logger.info("Original Payor is back as a Primary payor: " + origPyrAbbrv);
        List<AccnPyr> restoredAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int restoredPyrId = restoredAccnPyrs.get(0).getPyrId();

        logger.info("Verify original payor is back on accn");

        Assert.assertEquals(origPyrId, restoredPyrId);
        logger.info("Validate original payor level errors");

        List<AccnPyrErr> restoredAccnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId);
        Assert.assertEquals(restoredAccnPyrErrs.size(), 1);
        Assert.assertNotEquals(restoredAccnPyrErrs.get(0).getPyrPrio(), 0);
        Assert.assertNotEquals(restoredAccnPyrErrs.get(0).getPyrId(), 0);
        Assert.assertNotEquals(restoredAccnPyrErrs.get(0).getSubsId(), null);
        Assert.assertEquals(rpmDao.getErrCd(testDb, restoredAccnPyrErrs.get(0).getErrCd()).getErrGrpId(), EpConstants.ERR_GRP_DENIAL);

        AccnPyrErr restoredAccnPyrErr = new AccnPyrErr(restoredAccnPyrErrs.get(0));
        restoredAccnPyrErr.setAudUser(null);
        restoredAccnPyrErr.audDt = null;
        restoredAccnPyrErr.audRecId = 0;
        restoredAccnPyrErr.setResultCode(0);

        Assert.assertEquals(expectedAccnPyrErr, restoredAccnPyrErr);
    }

    @Test(priority = 1, description = "Eligibility check- Proc errors pyr_prio =1 when original Payor is back Accn is Priced ")
    @Parameters({"accnId", "origPyrAbbrv", "subId"})
    public void testXPR_887(String accnId, String origPyrAbbrv, String subId) throws Exception
    {
        logger.info("Starting Test Case: XPR-887, accnId=" + accnId);
        loadAccession = new LoadAccession(driver);
        accessionDetail = new AccessionDetail(driver, config, wait);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        List<AccnPyr> currentAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int currentPyrId = currentAccnPyrs.get(0).getPyrId();
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        int origPyrId = origPyr.getPyrId();

        logger.info(currentPyrId);
        setOriginalPayorOnAccession(accnId, origPyrAbbrv, accessionDetail, currentPyrId, origPyrId);

        Assert.assertEquals(currentAccnPyrs.get(0).getSubsId(), subId);
        Accn origAccn = accessionDao.getAccn(accnId);
        logger.info("Verifying accession status is priced or zbal, accnId=" + accnId + ", staId=" + origAccn.getStaId());

        if (origAccn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED)
        {
            Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS*2));
            origAccn = accessionDao.getAccn(accnId);
            Assert.assertEquals(origAccn.getStaId(), AccnStatusMap.ACCN_STATUS_PRICED);
        }

        logger.info("Make sure there is Proc level error exist");
        List<AccnProcErr> origAccnProcErrs = accessionDao.getAccnProcErrsByAccnId(accnId);
        Assert.assertEquals(origAccnProcErrs.size(), 1);
        Assert.assertNotEquals(origAccnProcErrs.get(0).getPyrPrio(), 0);
        Assert.assertNotEquals(origAccnProcErrs.get(0).getPyrId(), 0);
        Assert.assertNotEquals(origAccnProcErrs.get(0).getSubsId(), null);
        Assert.assertEquals(rpmDao.getErrCd(testDb, origAccnProcErrs.get(0).getErrCd()).getErrGrpId(), EpConstants.ERR_GRP_DENIAL);

        AccnProcErr expectedAccnProcErr = createExpectedAccnProcErr(origAccnProcErrs);

        expectedAccnProcErr.setPyrPrio(1);

        logger.info("Check Eligibility checkbox and click Submit button");
        clickHiddenPageObject(accessionDetail.checkEligibilityCheckbox(), 0);
        accessionDetail.accnIdText().sendKeys(Keys.ALT + "S");

        loadAccession.setAccnId(accnId, wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int newPyrId = newAccnPyrs.get(0).getPyrId();

        logger.info(origPyrId);
        logger.info(newPyrId);
        logger.info("Make sure primary payor is changed");
        Assert.assertNotEquals(origPyrId, newPyrId);

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnProcErrsByAccnId(accnId, false, false).isEmpty());

        // Clean up accn elig hist to avoid circularelig error
        accessionDao.deleteAccnEligHistByAccnId(accnId);

        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Check Eligibility checkbox and click Submit button");
        clickHiddenPageObject(accessionDetail.checkEligibilityCheckbox(), 0);
        accessionDetail.saveAndClear(wait);

        List<AccnPyr> restoredAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int restoredPyrId = restoredAccnPyrs.get(0).getPyrId();

        logger.info(restoredPyrId);
        logger.info("Verifying Payor is restored, pyrId=" + restoredPyrId);
        Assert.assertEquals(restoredPyrId, origPyrId);
        // Wait for accn to get out of Pricing queue. Pricing should be checked 2x.
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS*2));

        logger.info("Make sure Proc level error is back to the accn");
        List<AccnProcErr> actualAccnProcErrs = accessionDao.getAccnProcErrsByAccnId(accnId);
        Assert.assertEquals(actualAccnProcErrs.size(), 1);
        Assert.assertNotEquals(actualAccnProcErrs.get(0).getPyrPrio(), 0);
        Assert.assertNotEquals(actualAccnProcErrs.get(0).getPyrId(), 0);
        Assert.assertNotEquals(actualAccnProcErrs.get(0).getSubsId(), null);
        Assert.assertEquals(rpmDao.getErrCd(testDb, actualAccnProcErrs.get(0).getErrCd()).getErrGrpId(), EpConstants.ERR_GRP_DENIAL);

        AccnProcErr actualAccnProcErr = new AccnProcErr(actualAccnProcErrs.get(0));
        actualAccnProcErr.setAccnProcSeqId(0);
        actualAccnProcErr.setAudUser(null);
        actualAccnProcErr.audDt = null;
        actualAccnProcErr.setNote(null);
        actualAccnProcErr.audRecId = 0;
        actualAccnProcErr.setResultCode(0);

        logger.info("Verify that original Proc level error is restored");
        Assert.assertEquals(actualAccnProcErr, expectedAccnProcErr);
    }

    @Test(priority = 1, description = "Prim pyr is translated and Sub Id is saved from original Payor if No SubId in the file")
    @Parameters({"accnId", "origPyrAbbrv", "origSubId", "subIdFrom271"})
    public void testXPR_919(String accnId, String origPyrAbbrv, String origSubId, String subIdFrom271) throws Exception
    {
        logger.info("Starting Test Case: XPR-919, accnId=" + accnId);

        accessionDetail = new AccessionDetail(driver, config, wait);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        List<AccnPyr> currentAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int currentPyrId = currentAccnPyrs.get(0).getPyrId();
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        int origPyrId = origPyr.getPyrId();

        logger.info(currentAccnPyrs.get(0).getPyrId());
        setOriginalPayorOnAccession(accnId, origPyrAbbrv, accessionDetail, currentPyrId, origPyrId);

        logger.info("Verify that primary payor id is AETNA = " + accessionDetail.primaryPayorAbbrText().getText());
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyrAbbrv);
        currentAccnPyrs = accessionDao.getAccnPyrs(accnId);
        currentPyrId = currentAccnPyrs.get(0).getPyrId();
        logger.info("Verify that SubID is A123456789 " + origSubId);
        Assert.assertEquals(origSubId, accessionDetail.primaryPyrSubsIdInput().getAttribute("value"));

        logger.info("Check Eligibility checkbox and click Submit button");
        clickHiddenPageObject(accessionDetail.checkEligibilityCheckbox(), 0);
        accessionDetail.accnIdText().sendKeys(Keys.ALT + "S");
        wait.until(ExpectedConditions.visibilityOf(accessionDetail.accnIdInput()));

        List<AccnEligHist> accnEligHists = waitForEligibilityCheck(accnId);

        // Reload accession from DB
        Accn origAccn = accessionDao.getAccn(accnId);

        logger.info("Check Accn primary payor is changed");
        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int newPyrId = newAccnPyrs.get(0).getPyrId();
        Assert.assertNotEquals(currentPyrId, newPyrId);

        logger.info("Check Subscriber Id is saved from original Payor");
        String newSubId = newAccnPyrs.get(0).getSubsId();
        logger.info("Primary payor Subscriber ID = " + newSubId);
        Assert.assertEquals(origSubId, newSubId);

        logger.info("Primary payor Subscriber ID = " + newSubId + " is NOT equal Subscriber ID provided in 271 file (REF*IG*null) - " + subIdFrom271);
    }

    @Test(priority = 1, description = "Prim pyr is translated and Sub Id is saved from 271 file")
    @Parameters({"accnId", "origPyrAbbrv", "newPyrAbbrv", "origSubId", "subIdFrom271"})
    public void testXPR_922(String accnId, String origPyrAbbrv, String newPyrAbbrv, String origSubId, String subIdFrom271) throws Exception
    {
        logger.info("Starting Test Case: XPR-922, accnId=" + accnId);
        accessionDetail = new AccessionDetail(driver, config, wait);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        List<AccnPyr> currentAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int currentPyrId = currentAccnPyrs.get(0).getPyrId();
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        int origPyrId = origPyr.getPyrId();

        logger.info(currentAccnPyrs.get(0).getPyrId());
        setOriginalPayorOnAccession(accnId, origPyrAbbrv, accessionDetail, currentPyrId, origPyrId);

        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Verify that primary payor is AETNA = " + accessionDetail.payorIDText().getText());
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyrAbbrv);

        logger.info("Make sure original subscriber Id is different from 271 file subscriber Id");
        changeSubscriberIdOriginal(origSubId, accessionDetail);

        logger.info("Verify that SubID is A123456789 " + origSubId);
        Assert.assertEquals(origSubId, accessionDetail.primaryPyrSubsIdInput().getAttribute("value"));

        logger.info("Check Eligibility checkbox and click Submit button");
        accessionDetail.clickEligibilityCheckbox();

        accessionDetail.saveAndClear(wait);
        Assert.assertTrue(accessionDetail.isSaveDone(), "Save should be done.");

        waitForEligibilityCheck(accnId);

        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int newPyrId = newAccnPyrs.get(0).getPyrId();
        logger.info("Check Accn primary payor is changed to" + newPyrId);

        logger.info("Accn primary payor is " + newPyrId);
        Assert.assertNotEquals(origPyrId, newPyrId);
        logger.info("Check Subscriber Id is saved from original Payor");
        String newSubIdFrom271 = newAccnPyrs.get(0).getSubsId();
        logger.info("newSubIdFrom271 " + newSubIdFrom271);
        Assert.assertEquals(newSubIdFrom271, subIdFrom271);
        logger.info("Primary payor Subscriber ID = " + newSubIdFrom271 + " is equal Subscriber ID provided in 271 file (REF*IG*subID) - " + subIdFrom271);
    }

    @Test(priority = 1, description = "Undetermined response and error added based on 271 file: same Plan names, conflicting statuses")
    @Parameters({"accnId", "origPyrAbbrv", "origSubId"})
    public void testXPR_950(String accnId, String origPyrAbbrv, String origSubId) throws Exception
    {
        logger.info("Starting Test Case: XPR-950, accnId=" + accnId + ", origPyrAbbrv=" + origPyrAbbrv + ", origSubId=" + origSubId);
        accessionDetail = new AccessionDetail(driver, config, wait);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        List<AccnPyr> origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        List<AccnPyrErr> origUnfixedAccnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        verifyAccnEligDataIsCleared(accessionDao.getAccnEligHistByAccnId(accnId), origAccnPyrs);
        if(origUnfixedAccnPyrErrs.size()>0) {
            rpmDao.updateUnfixedAccnPyrErrSetFixedByAccnId(testDb, accnId);
        }

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), pyr.getName());
        // Verify the subscriber ID is correct
        Assert.assertEquals(accessionDetail.primaryPyrSubsIdInput().getAttribute("value"), origSubId);

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        accessionDetail.clickEligibilityCheckbox();
        //clickHiddenPageObject(accessionDetail.checkEligibilityCheckbox(), 0);

        wait.until(ExpectedConditions.visibilityOf(accessionDetail.saveAndClearBtn()));
        accessionDetail.accnIdText().sendKeys(Keys.ALT + "S");

        wait.until(ExpectedConditions.visibilityOf(driver.findElementByClassName("serverErrorsList")));
        Assert.assertTrue(accessionDetail.isColumnValueExist(accessionDetail.currentAccnErrTable(), "ELIGUNDETERMND-Eligibility undetermined"));
        accessionDetail.saveAndClear(wait);

        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), pyr.getPyrId());
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_UNDETERMINED);

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(newAccnPyrs.size(), 1);
        Assert.assertEquals(newAccnPyrs.get(0).getPyrId(), pyr.getPyrId());
        Assert.assertEquals(newAccnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_UNDETERMINED);
        Assert.assertNull(newAccnPyrs.get(0).getEligSvcName());
        Assert.assertNotNull(newAccnPyrs.get(0).getEligChkDt());

        logger.info("Verifying UNDETERMINED error is added, accnId=" + accnId);
        verifyUnfixedUnbillableAccnPyrErrs(new HashSet<>(Collections.singletonList(DbErrorMap.ELIGUNDETERMND)), accessionDao.getAccn(accnId), newAccnPyrs.get(0));
    }

    @Test(priority = 1, description = " Eligible response and undetermined error is NOT added based on 271 file: Different Plan names, conflicting statuses")
    @Parameters({"accnId", "origPyrAbbrv", "origSubId"})
    public void testXPR_951(String accnId, String origPyrAbbrv, String origSubId) throws Exception
    {
        logger.info("Starting Test Case: XPR-951, accnId=" + accnId + ", origPyrAbbrv=" + origPyrAbbrv + ", origSubId=" + origSubId);
        accessionDetail = new AccessionDetail(driver, config, wait);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        List<AccnPyr> origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        verifyAccnEligDataIsCleared(accessionDao.getAccnEligHistByAccnId(accnId), origAccnPyrs);

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), pyr.getName());
        // Verify the subscriber ID is correct
        Assert.assertEquals(accessionDetail.primaryPyrSubsIdInput().getAttribute("value"), origSubId);

        accessionDetail.clickEligibilityCheckbox();

        accessionDetail.saveAndClear(wait);

        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), pyr.getPyrId());
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(newAccnPyrs.size(), 1);
        Assert.assertEquals(newAccnPyrs.get(0).getPyrId(), pyr.getPyrId());
        Assert.assertEquals(newAccnPyrs.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_ELIGIBLE);
        Assert.assertEquals(newAccnPyrs.get(0).getEligSvcName(), EligMap.ELIG_SVC_NAME_XIFIN);
        Assert.assertNotNull(newAccnPyrs.get(0).getEligChkDt());

        logger.info("Verifying no new errors added, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());
        verifyEligTransLog(accnEligHists.get(0));
    }

    @Test(priority = 1, description = "Eligibility check is ELIGIBLE, Patient and Insured Demographic Data is updated - mixed case")
    @Parameters({"accnId", "pyrAbbrv", "patientFNm", "patientLNm", "patientAddr1", "patientAddr2", "patientSex", "patientCity", "patientZipId", "subscriberId", "relshpId", "insuredFNm", "insuredLNm", "insuredAddr1", "insuredAddr2", "insuredSex", "insuredCity", "insuredZipId"})
    public void testXPR_1050(String accnId, String pyrAbbrv, String patientFNm, String patientLNm, String patientAddr1, String patientAddr2, int patientSex, String patientCity, String patientZipId, String subscriberId, String relshpId, String insuredFNm, String insuredLNm, String insuredAddr1, String insuredAddr2, int insuredSex, String insuredCity, String insuredZipId) throws Exception
    {

        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: XPR-1050, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
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
        Assert.assertFalse(pyrElig.getIsUpdateInsSubsId());
        Assert.assertTrue(pyrElig.getIsUpdateInsNm());
        Assert.assertTrue(pyrElig.getIsUpdateInsAddr());
        Assert.assertTrue(pyrElig.getIsUpdateInsGender());
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

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), pyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());
        logger.info("Update Patient and Insured Name, Address and Gender");

        updatePatientDemographics("testAddressLine1", "testAddressLine2", "testPatientFirstName", "testPatientLastName", "0", "92130", "Los Angeles");
        updateInsuredDemographics("InsuredAddressLine1", "InsuredAddressLine2", "InsuredFirstName", "testInsuredLastName", "0", "90009", "San Diego");

        accessionDetail.clickSave();

        // Reload accession from DB to get Patient and Insured Demo before Eligibility check
        origAccnPyrs = accessionDao.getAccnPyrs(accnId);

        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        logger.info("Performing eligibility check, accnId=" + accnId);
        accessionDetail.clickEligibilityCheckbox();

        accessionDetail.saveAndClear(wait);
        wait.until(ExpectedConditions.visibilityOf(accessionDetail.accnIdInput()));

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

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
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
    }

    @Test(priority = 1, description = "Eligibility Engine - Eligibility check is ELIGIBLE, Insured Name, Gender, Address are updated")
    @Parameters({"accnId", "pyrAbbrv", "insuredFNm", "insuredLNm", "insuredAddr1", "insuredAddr2", "insuredSex", "insuredCity", "insuredZipId"})
    public void testXPR_1051(String accnId, String pyrAbbrv, String insuredFNm, String insuredLNm, String insuredAddr1, String insuredAddr2, int insuredSex, String insuredCity, String insuredZipId) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: XPR-1051, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
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

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), pyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        logger.info("Update Insured Name, Address and Gender");

        updateInsuredDemographics("InsuredAddressLine1", "InsuredAddressLine2", "InsuredFirstName", "testInsuredLastName", "0", "90009", "San Diego");
        accessionDetail.clickSave();

        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        // Reload accession from DB to get Patient and Insured Demo before Eligibility check
        origAccn = accessionDao.getAccn(accnId);
        origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        logger.info("Performing eligibility check, accnId=" + accnId);
        accessionDetail.clickEligibilityCheckbox();

        accessionDetail.saveAndClear(wait);

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

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
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
    }

    @Test(priority = 1, description = "Eligibility Engine - Eligibility check is ELIGIBLE, Patient's Name, Gender, Address are updated")
    @Parameters({"accnId", "pyrAbbrv", "patientFNm", "patientLNm", "patientAddr1", "patientAddr2", "patientSex", "patientCity", "patientZipId"})
    public void testXPR_1052(String accnId, String pyrAbbrv, String patientFNm, String patientLNm, String patientAddr1, String patientAddr2, int patientSex, String patientCity, String patientZipId) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);

        logger.info("Starting Test Case: XPR-1052, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
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

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), pyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        logger.info("Update Patient Name, Patient Address and Gender");

        updatePatientDemographics("testAddressLine1", "testAddressLine2", "testPatientFirstName", "testPatientLastName", "0", "92130", "Los Angeles");
        accessionDetail.clickSave();

        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        // Reload accession from DB to get Patient and Insured Demo before Eligibility check
        origAccnPyrs = accessionDao.getAccnPyrs(accnId);
        logger.info("Performing eligibility check, accnId=" + accnId);
        accessionDetail.clickEligibilityCheckbox();

        accessionDetail.saveAndClear(wait);

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

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        Accn newAccn = accessionDao.getAccn(accnId);
        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(newAccnPyrs.size(), 1);
        Assert.assertEquals(newAccnPyrs.get(0).getPyrId(), pyr.getPyrId());

        // Verify patient/demo data is updated
        logger.info("Verifying patient demographic data is updated and insured demographic data is not updated, accnId=" + accnId);

        Assert.assertEquals(newAccn.getPtFNm(), patientFNm);
        Assert.assertEquals(newAccn.getPtLNm(), patientLNm);
        Assert.assertEquals(newAccn.getPtAddr1(), patientAddr1);
        Assert.assertEquals(newAccn.getPtAddr2(), patientAddr2);
        Assert.assertEquals(newAccn.getPtSex(), patientSex);
        Assert.assertEquals(newAccn.getPtCity(), patientCity);
        Assert.assertEquals(newAccn.getPtZipId(), patientZipId);
        Assert.assertEquals(origAccnPyrs.get(0).getSubsId(), newAccnPyrs.get(0).getSubsId());
        Assert.assertEquals(origAccnPyrs.get(0).getInsFNm(), newAccnPyrs.get(0).getInsFNm());
        Assert.assertEquals(origAccnPyrs.get(0).getInsLNm(), newAccnPyrs.get(0).getInsLNm());
        Assert.assertEquals(origAccnPyrs.get(0).getInsAddr1(), newAccnPyrs.get(0).getInsAddr1());
        Assert.assertEquals(origAccnPyrs.get(0).getInsAddr2(), newAccnPyrs.get(0).getInsAddr2());
        Assert.assertEquals(origAccnPyrs.get(0).getInsSex(), newAccnPyrs.get(0).getInsSex());
        Assert.assertEquals(origAccnPyrs.get(0).getRelshpId(), newAccnPyrs.get(0).getRelshpId());

        logger.info("Verifying no new errors added, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        verifyEligTransLog(accnEligHists.get(0));
    }

    @Test(priority = 1, description = "Eligibility check is ELIGIBLE, Patient and Insured Demographic Data (EPI) is updated - mixed case")
    @Parameters({"accnId", "pyrAbbrv", "patientFNm", "patientLNm", "patientAddr1", "patientAddr2", "patientSex", "patientCity", "patientZipId", "subscriberId", "relshpId", "insuredFNm", "insuredLNm", "insuredAddr1", "insuredAddr2", "insuredSex", "insuredCity", "insuredZipId"})
    public void testXPR_1055(String accnId, String pyrAbbrv, String patientFNm, String patientLNm, String patientAddr1, String patientAddr2, int patientSex, String patientCity, String patientZipId, String subscriberId, String relshpId, String insuredFNm, String insuredLNm, String insuredAddr1, String insuredAddr2, int insuredSex, String insuredCity, String insuredZipId) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: XPR-1055, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
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
        Assert.assertFalse(pyrElig.getIsUpdateInsSubsId());
        Assert.assertTrue(pyrElig.getIsUpdateInsNm());
        Assert.assertTrue(pyrElig.getIsUpdateInsAddr());
        Assert.assertTrue(pyrElig.getIsUpdateInsGender());
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

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), pyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        logger.info("Update Patient and Insured Name, Address and Gender");
        updatePatientDemographics("testAddressLine1", "Patient Addr2", "testPatientFirstName", "testPatientLastName", "0", "92130", "Los Angeles");
        updateInsuredDemographics("InsuredAddressLine1", "InsuredAddressLine2", "InsuredFirstName", "testInsuredLastName", "0", "90009", "San Diego");

        accessionDetail.clickSave();

        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        logger.info("Performing eligibility check, accnId=" + accnId);
        accessionDetail.clickEligibilityCheckbox();
        accessionDetail.saveAndClear(wait);

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

        logger.info("Verifying accession payor eligibility status is updated, accnId=" + accnId);
        Accn newAccn = accessionDao.getAccn(accnId);
        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(newAccnPyrs.size(), 1);
        Assert.assertEquals(newAccnPyrs.get(0).getPyrId(), pyr.getPyrId());

        // Verify patient/demo and insured demo info is updated
        logger.info("Verifying patient/demo and insured demo info is updated, accnId=" + accnId);
        Thread.sleep(6000);
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

        logger.info("Verifying no new errors added, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        verifyEligTransLog(accnEligHists.get(0));
    }

    @Test(priority = 1, description = "Eligibility check - No Svc Type code set in Payor Elig config, none sent in request")
    @Parameters({"accnId", "origPyrAbbrv", "additionalSvcTyps"})
    public void testXPR_1225(String accnId, String origPyrAbbrv, String additionalSvcTyps) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: XPR-1225, accnId=" + accnId + ", origPyrAbbrv=" + origPyrAbbrv);
        logger.info("Make sure there is no default and additional service type codes in pyr_elig for origPyrAbbrv = " + origPyrAbbrv);

        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        int origPyrId = origPyr.getPyrId();
        List<PyrElig> pyrElig = rpmDao.getPyrEligByPyrIdEligSvcId(testDb, origPyrId, EligMap.XIFIN);
        Assert.assertNotNull(pyrElig);
        Assert.assertEquals(pyrElig.size(), 1);
        Assert.assertNull(pyrElig.get(0).getAddtlSvcTyps());
        Assert.assertNull(pyrElig.get(0).getDefaultSvcTypCd());
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        logger.info("Check Eligibility checkbox and click Submit button");
        accessionDetail.clickEligibilityCheckbox();

        accessionDetail.saveAndClear(wait);


        logger.info("Verify Accn_elig_hist records is created with status Ineligible");
        List<AccnEligHist> accnEligHists = waitForEligibilityCheck(accnId);

        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), origPyrId);
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_INELIGIBLE);

        verifyEligTransLog(accnEligHists.get(0), EligMap.ELIG_STA_TYP_INELIGIBLE);
    }

    @Test(priority = 1, description = "Eligibility check - Response contains svc type code requested, default and no svc type code - use requested - Additional svc_typ_code")
    @Parameters({"accnId", "origPyrAbbrv", "additionalSvcTyps", "defaultSvcTypCd"})
    public void testXPR_1224(String accnId, String origPyrAbbrv, String additionalSvcTyps, String defaultSvcTypCd) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: XPR-1224, accnId=" + accnId + ", origPyrAbbrv=" + origPyrAbbrv);
        logger.info("Make sure there is default and additional service type codes in pyr_elig for origPyrAbbrv = " + origPyrAbbrv);

        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        int origPyrId = origPyr.getPyrId();
        List<PyrElig> pyrElig = rpmDao.getPyrEligByPyrIdEligSvcId(testDb, origPyrId, EligMap.XIFIN);
        Assert.assertNotNull(pyrElig);
        Assert.assertEquals(pyrElig.size(), 1);
        Assert.assertEquals(pyrElig.get(0).getAddtlSvcTyps(), additionalSvcTyps);
        Assert.assertEquals(pyrElig.get(0).getDefaultSvcTypCd(), defaultSvcTypCd);

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        logger.info("Check Eligibility checkbox and click Submit button");
        accessionDetail.clickEligibilityCheckbox();

        accessionDetail.saveAndClear(wait);

        logger.info("Verify Accn_elig_hist records is created with status Ineligible");
        List<AccnEligHist> accnEligHists = waitForEligibilityCheck(accnId);

        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), origPyrId);
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_INELIGIBLE);

        verifyEligTransLog(accnEligHists.get(0), EligMap.ELIG_STA_TYP_INELIGIBLE, additionalSvcTyps);

    }

    @Test(priority = 1, description = "Prim pyr is translated and Sub Id from REF*IG if Subs ID Type not setup and 271 file has all additional segments")
    @Parameters({"accnId", "origPyrAbbrv", "newPyrAbbrv", "origSubId", "subIdFrom271"})
    public void testXPR_1590(String accnId, String origPyrAbbrv, String newPyrAbbrv, String origSubId, String subIdFrom271) throws Exception
    {
        logger.info("Starting Test Case: XPR-1590, accnId=" + accnId);
        accessionDetail = new AccessionDetail(driver, config, wait);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        logger.info("Verify there is Pyr Eligibility Translation Rule without Subs ID Type - Default 0");
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        Pyr translatedPyr = rpmDao.getPyrByPyrAbbrv(testDb, newPyrAbbrv);
        List<EligResp> eligResp = rpmDao.getEligRespByPyrId(testDb, origPyr.getPyrId());
        Assert.assertEquals(eligResp.get(0).getNewPyrId(), translatedPyr.getPyrId());
        Assert.assertEquals(eligResp.get(0).getEligRespSubsRefTypId(), 0);
        List<AccnPyr> currentAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int currentPyrId = currentAccnPyrs.get(0).getPyrId();

        int origPyrId = origPyr.getPyrId();

        logger.info("Current Pyr Id =" + currentPyrId);
        setOriginalPayorOnAccession(accnId, origPyrAbbrv, accessionDetail, currentPyrId, origPyrId);

        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Verify that primary payor is AETNA = " + accessionDetail.payorIDText().getText());
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyrAbbrv);

        logger.info("Make sure original subscriber Id is different from 271 file subscriber Id");
        if (!StringUtils.equals(accessionDetail.primaryPyrSubsIdInput().getAttribute("value"), origSubId))
        {
            accessionDetail.clearPrimaryPyrSubsId();
            accessionDetail.setPrimaryPyrSubsId(origSubId);
            accessionDetail.clickSave();
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }

        logger.info("Verify that SubID is 101010101 " + origSubId);
        Assert.assertEquals(origSubId, accessionDetail.primaryPyrSubsIdInput().getAttribute("value"));

        logger.info("Check Eligibility checkbox and click Submit button");
        accessionDetail.clickEligibilityCheckbox();

        accessionDetail.saveAndClear(wait);
        //   accessionDetail.saveAndClearWithReload(wait);
        Assert.assertTrue(accessionDetail.isSaveDone(), "Save should be done.");

        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int newPyrId = newAccnPyrs.get(0).getPyrId();
        logger.info("Check Accn primary payor is changed to" + newPyrId);

        logger.info("Check Subscriber Id is saved from REF*IG*SUBIG11");
        String newSubIdFrom271 = newAccnPyrs.get(0).getSubsId();
        logger.info("NewSubIdFrom271 " + newSubIdFrom271);
        Assert.assertEquals(newSubIdFrom271, subIdFrom271);
        logger.info("Primary payor Subscriber ID = " + newSubIdFrom271 + " is equal Subscriber ID provided in 271 file (REF*IG*SUBIG11) - " + subIdFrom271);
    }


    @Test(priority = 1, description = "Pyr is translated, SubId updated from REF*18 if Subs ID Type set to Plan Number")
    @Parameters({"accnId", "origPyrAbbrv", "newPyrAbbrv", "origSubId", "subIdFrom271"})
    public void testXPR_1591(String accnId, String origPyrAbbrv, String newPyrAbbrv, String origSubId, String subIdFrom271) throws Exception
    {
        logger.info("Starting Test Case: XPR-1591, accnId=" + accnId);
        accessionDetail = new AccessionDetail(driver, config, wait);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        logger.info("Verify there is Pyr Eligibility Translation Rule with Subs Id Type - Plan Number");
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        Pyr translatedPyr = rpmDao.getPyrByPyrAbbrv(testDb, newPyrAbbrv);
        List<EligResp> eligResp = rpmDao.getEligRespByPyrId(testDb, origPyr.getPyrId());
        Assert.assertEquals(eligResp.get(0).getEligRespSubsRefTypId(), 1);
        Assert.assertEquals(eligResp.get(0).getNewPyrId(), translatedPyr.getPyrId());
        List<AccnPyr> currentAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int currentPyrId = currentAccnPyrs.get(0).getPyrId();
        int origPyrId = origPyr.getPyrId();

        logger.info(currentPyrId);
        setOriginalPayorOnAccession(accnId, origPyrAbbrv, accessionDetail, currentPyrId, origPyrId);

        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Verify that primary payor is BCBSNY = " + accessionDetail.payorIDText().getText());
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyr.getName());

        logger.info("Make sure original subscriber Id is different from 271 file subscriber Id REF*18");
        changeSubscriberIdOriginal(origSubId, accessionDetail);

        logger.info("Verify that SubID is " + origSubId);
        Assert.assertEquals(origSubId, accessionDetail.primaryPyrSubsIdInput().getAttribute("value"));

        logger.info("Check Eligibility checkbox and click Submit button");
        accessionDetail.clickEligibilityCheckbox();

        accessionDetail.saveAndClear(wait);
        Assert.assertTrue(accessionDetail.isSaveDone(), "Save should be done.");

        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int newPyrId = newAccnPyrs.get(0).getPyrId();
        logger.info("Check Accn primary payor is changed to" + newPyrId);

        logger.info("Check Subscriber Id is saved from REF*18*subID");
        String newSubIdFrom271 = newAccnPyrs.get(0).getSubsId();
        logger.info("NewSubIdFrom271 " + newSubIdFrom271);
        Assert.assertEquals(newSubIdFrom271, subIdFrom271);
        logger.info("Primary payor Subscriber ID = " + newSubIdFrom271 + " is equal Subscriber ID provided in 271 file (REF*18*subID) - " + subIdFrom271);

    }

    @Test(priority = 1, description = "Pyr is translated, SubId updated from NM109, not REF*IG if Subs ID Type set to Member Id Number (REF*1W), but not in the 271 file \">")
    @Parameters({"accnId", "origPyrAbbrv", "newPyrAbbrv", "origSubId", "subIdFromREFIG", "subIdFromNM109"})
    public void testXPR_1616(String accnId, String origPyrAbbrv, String newPyrAbbrv, String origSubId, String subIdFromREFIG, String subIdFromNM109) throws Exception
    {
        logger.info("Starting Test Case: XPR-1616, accnId=" + accnId);
        accessionDetail = new AccessionDetail(driver, config, wait);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        logger.info("Verify there is Pyr Eligibility Translation Rule with Subs Id Type - Plan Number");
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        Pyr translatedPyr = rpmDao.getPyrByPyrAbbrv(testDb, newPyrAbbrv);
        List<EligResp> eligResp = rpmDao.getEligRespByPyrId(testDb, origPyr.getPyrId());

        Assert.assertEquals(eligResp.get(0).getNewPyrId(), translatedPyr.getPyrId());
        Assert.assertEquals(eligResp.get(0).getEligRespSubsRefTypId(), 3);
        List<AccnPyr> currentAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int currentPyrId = currentAccnPyrs.get(0).getPyrId();
        int origPyrId = origPyr.getPyrId();

        logger.info(currentPyrId);
        setOriginalPayorOnAccession(accnId, origPyrAbbrv, accessionDetail, currentPyrId, origPyrId);

        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Verify that primary payor is BSCNY = " + accessionDetail.payorIDText().getText());
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyr.getName());

        logger.info("Make sure original subscriber Id is different from 271 file subscriber Id REF*IG");
        if (!StringUtils.equals(accessionDetail.primaryPyrSubsIdInput().getAttribute("value"), origSubId))
        {
            accessionDetail.clearPrimaryPyrSubsId();
            accessionDetail.setPrimaryPyrSubsId(origSubId);
            accessionDetail.clickSave();
        }

        logger.info("Verify that SubID is " + origSubId);
        Assert.assertEquals(origSubId, accessionDetail.primaryPyrSubsIdInput().getAttribute("value"));

        logger.info("Check Eligibility checkbox and click Submit button");
        accessionDetail.clickEligibilityCheckbox();

        accessionDetail.saveAndClear(wait);

        Assert.assertTrue(accessionDetail.isSaveDone(), "Save should be done.");

        List<AccnPyr> newAccnPyrs = accessionDao.getAccnPyrs(accnId);
        int newPyrId = newAccnPyrs.get(0).getPyrId();
        logger.info("Check Accn primary payor is changed to" + newPyrId);

        logger.info("Check Subscriber Id is saved from NM109");
        String newSubIdFrom271 = newAccnPyrs.get(0).getSubsId();
        logger.info("subIdFromNM109 " + subIdFromNM109);
        Assert.assertNotEquals(newSubIdFrom271, subIdFromREFIG);
        Assert.assertEquals(newSubIdFrom271, subIdFromNM109);
        logger.info("Primary payor Subscriber ID = " + newSubIdFrom271 + " is equal Subscriber ID provided in 271 file (NM109) - " + subIdFromNM109);

    }

    @Test(priority = 1, description = "Client Roster Check - Patient Is On Roster For New Client \">")
    @Parameters({"accnId", "origPyrAbbrv", "origClnAbbrv", "newClnAbbrv", "newPyrAbbrv"})
    public void testXPR_2044(String accnId, String origPyrAbbrv, String origClnAbbrv, String newClnAbbrv, String newPyrAbbrv) throws Exception
    {
        accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: XPR-2044, accnId=" + accnId);
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, origPyrAbbrv);
        Cln origCln = rpmDao.getClnByClnAbbrev(testDb, origClnAbbrv);
        Accn accn = accessionDao.getAccn(accnId);

        List<EligRoster> eligRosters = rpmDao.getEligRostersByPtSeqId(accn.getPtSeqId());
        assertEquals("Verify Client Census Roster Eligibility is set for this patient", 1, eligRosters.size());

        logger.info("Reload Accn on Accn Detail");
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

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
            List<AccnPyr> currentAccnPyrs = accessionDao.getAccnPyrs(accnId);
            int currentPyrId = currentAccnPyrs.get(0).getPyrId();
            int origPyrId = origPyr.getPyrId();
            setOriginalPayorOnAccession(accnId, origPyrAbbrv, accessionDetail, currentPyrId, origPyrId);
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyr.getName(), "Primary payor is not set to correct payor");

        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait), "Did not verify the accession is loaded");

        logger.info("Check Eligibility checkbox and click Submit button");
        accessionDetail.clickEligibilityCheckbox();

        accessionDetail.saveAndClear(wait);
        Assert.assertTrue(accessionDetail.isSaveDone(), "Save should have been done.");


        logger.info("Verifying post-eligibility client, accnId=" + accnId + ", newClnAbbrv=" + newClnAbbrv + ", pyrAbbev=" + newPyrAbbrv);
        Cln newCln = rpmDao.getClnByClnAbbrev(testDb, newClnAbbrv);
        Accn newAccn = accessionDao.getAccn(accnId);
        Assert.assertEquals(newAccn.getClnId(), newCln.getClnId());
        AccnPyr newAccnPyr = accessionDao.getAccnPyrs(accnId).get(0);
        Pyr newPyr = rpmDao.getPyrByPyrAbbrv(testDb, newPyrAbbrv);
        Assert.assertEquals(newPyr.getPyrId(), newAccnPyr.getPyrId());
    }
    private void changeSubscriberIdOriginal(String origSubId, AccessionDetail accessionDetail) throws InterruptedException
    {
        if (!StringUtils.equals(accessionDetail.primaryPyrSubsIdInput().getAttribute("value"), origSubId))
        {
            accessionDetail.clearPrimaryPyrSubsId();
            accessionDetail.setPrimaryPyrSubsId(origSubId);
            accessionDetail.clickSave();
            // accessionDetail.clickOKBtnInPtDemoUpdate();
        }
    }

    private void setOriginalPayorOnAccession(String accnId, String origPyrAbbrv, AccessionDetail accessionDetail, int currentPyrId, int origPyrId) throws Exception
    {
        if (currentPyrId != origPyrId)
        {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + currentPyrId + ", newPyrId=" + origPyrId);
            setOriginalPyrOnAccn(accessionDetail, accnId, origPyrAbbrv);
            accessionDetail.loadAccnOnAccnDetail(wait, accnId);
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }
    }

    private void setOriginalPyrOnAccn(AccessionDetail accessionDetail, String accnId, String origPyrAbbrv) throws Exception
    {
        // accessionDetail.primaryPayorIDInput().click();
        WebElement primaryPyrId = accessionDetail.primaryPayorIDInput();
        wait.until(ExpectedConditions.visibilityOf(primaryPyrId));
        Thread.sleep(4000);
        primaryPyrId.clear();
        Thread.sleep(4000);
        primaryPyrId.sendKeys(origPyrAbbrv + Keys.TAB);
        Thread.sleep(4000);
        wait.until(ExpectedConditions.elementToBeClickable(accessionDetail.saveAndClearBtn()));
        accessionDetail.saveAndClearBtn().click();
        // Wait for accn to get out of Pricing queue. Pricing should be checked 2x.
        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME * 4));
    }

    public AccnPyrErr createExpectedAccnPyrErr(List<AccnPyrErr> origAccnPyrErrs)
    {
        AccnPyrErr expectedAccnPyrErr = new AccnPyrErr(origAccnPyrErrs.get(0));
        expectedAccnPyrErr.setPyrPrio(0);
        expectedAccnPyrErr.setAudUser(null);
        expectedAccnPyrErr.audDt = null;
        expectedAccnPyrErr.audRecId = 0;
        expectedAccnPyrErr.setResultCode(0);
        return expectedAccnPyrErr;
    }

    public AccnProcErr createExpectedAccnProcErr(List<AccnProcErr> origAccnProcErrs)
    {
        AccnProcErr expectedAccnProcErr = new AccnProcErr(origAccnProcErrs.get(0));
        expectedAccnProcErr.setPyrPrio(0);
        expectedAccnProcErr.setAccnProcSeqId(0);
        expectedAccnProcErr.setAudUser(null);
        expectedAccnProcErr.audDt = null;
        expectedAccnProcErr.setNote(null);
        expectedAccnProcErr.audRecId = 0;
        expectedAccnProcErr.setResultCode(0);
        return expectedAccnProcErr;
    }

    private void setOriginalClnToAccn(AccessionDetail accessionDetail, String accnId, String origClnAbbrv) throws InterruptedException
    {
        WebElement clientIdInput = accessionDetail.clnIdInput();
        wait.until(ExpectedConditions.visibilityOf(clientIdInput));
        Thread.sleep(4000);
        clientIdInput.clear();
        Thread.sleep(4000);
        clientIdInput.sendKeys(origClnAbbrv+ Keys.TAB);
        Thread.sleep(4000);
    }
    protected void cleanUpAccn(String accnId) throws Exception
    {
        logger.info("Deleting AccnEligHist records, accnId=" + accnId);
        accessionDao.deleteAccnEligHistByAccnId(accnId);
        logger.info("Deleting AccnPyrDel records, accnId=" + accnId);
        accessionDao.deleteAccnPyrDelByAccnId(accnId);
        for (AccnPyr accnPyr : accessionDao.getAccnPyrs(accnId))
        {
            logger.info("Clearing AccnPyr eligibility fields, accnId=" + accnId + ", pyrPrio=" + accnPyr.getPyrPrio() + ", pyrId=" + accnPyr.getPyrId());
            accnPyr.setEligChkDt(null);
            accnPyr.setEligChkDtAsString(null);
            accnPyr.setEligStaTypId(0);
            accnPyr.setEligStaTypDescr(null);
            accnPyr.setEligSvcName(null);
            accessionDao.setAccnPyr(accnPyr);
        }
        logger.info("Clearing AccnProc SubmFileSeqId, accnId=" + accnId);
        rpmDao.clearAccnProcSubmFileSeqIdByAccnId(testDb, accnId);
        logger.info("Deleting SubmFileAudit records, accnId=" + accnId);
        rpmDao.deleteSubmFileAuditByAccnId(testDb, accnId);
        List<QAccnSubm> qAccnSubms = rpmDao.getQAccnSubm(testDb, accnId);
        logger.info("Deleteing submClaimAudit and claimJson, accnId=" + accnId + ", qasRows=" + qAccnSubms.size());
        for(QAccnSubm qas : qAccnSubms){
            rpmDao.deleteSubmClaimAuditByDocId(testDb, qas.getDocSeqId());
            submissionDao.deleteClaimJsonByDocSeqId(qas.getDocSeqId());
        }
        logger.info("Deleting Processed QAccnSubm records, accnId=" + accnId);
        rpmDao.deleteProcessedQAccnSubmByAccnId(testDb, accnId);
    }

    @Override
    public List<AccnEligHist> waitForEligibilityCheck(String accnId)
            throws XifinDataAccessException, InterruptedException
    {
        long curTime, startTime = System.currentTimeMillis();
        long endTime = startTime + QUEUE_WAIT_TIME;
        while (accessionDao.getAccnEligHistByAccnId(accnId).isEmpty() && (curTime = System.currentTimeMillis()) < endTime)
        {
            logger.info("Waiting for AccnDetail eligibility check, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(curTime-startTime) + "s, maxTime=" + TimeUnit.MILLISECONDS.toSeconds(QUEUE_WAIT_TIME) + "s");
            Thread.sleep(QUEUE_POLL_TIME);
        }
        return accessionDao.getAccnEligHistByAccnId(accnId);
    }
}
