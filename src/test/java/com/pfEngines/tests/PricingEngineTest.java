package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accnProc.AccnProc;
import com.mbasys.mars.ejb.entity.accnTest.AccnTest;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.MiscMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.accession.orderProcessing.AccnTestUpdate;
import com.overall.menu.MenuNavigation;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.util.StringUtils;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TestDataSetup;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Properties;

public class PricingEngineTest extends SeleniumBaseTest
{
    @BeforeSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running BeforeSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            logIntoSso(ssoUsername, ssoPassword);
            //updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_PERFORM_JOIN_LOGIC, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_CLIENT_BILLING_RULE_LOGIC_SPLIT, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_PERFORM_PYR_BILLING_RULES, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_DEFAULT_CLN_BILLING_RULES, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_JOIN_ACCNS, "False", "0");

            daoManagerPlatform.setValuesFromTable("cln", "CLN.B_BILLING_ASSIGNMENT_REQ =0, CLN.B_CLN_PRC_SUSP_THRU_EOM = 0", "cln_abbrev not like 'AUTO%' and (CLN.B_BILLING_ASSIGNMENT_REQ =1 or CLN.B_CLN_PRC_SUSP_THRU_EOM = 1)", testDb);
            // todo afa Ensure Pricing Engine and OE Posting Engines are scheduled
            XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
            xifinAdminUtils.clearDataCache();
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

    @Test(priority = 1, description = "Price a Patient Payor Single Test accn")
    @Parameters({"project", "testSuite", "testCase"})
    public void testPFER_557(String project, String testSuite, String testCase) throws Exception
    {
        logger.info("*** Testing - testPFER_557 ***");
        logger.info("Sending Accn WS request to create an accession, testCase="+testCase+", accnWsUrl="+config.getProperty(PropertyMap.ACCNWS_URL));
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        String testAbbrev = testProperties.getProperty("TestAbbrev");
        Assert.assertTrue(StringUtils.isNotBlank(accnId), "Accession ID not found in WS response");
        Assert.assertTrue(StringUtils.isNotBlank(testAbbrev), "Test ID not found in WS response");
        accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS*2);
        accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS*2);
        accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS*2);
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession status is not Priced");
        Assert.assertEquals(accessionDao.getAccn(accnId).getDueAmtAsMoney().toString(), "2.50", "Accession due amount could not be verified");
        List<AccnProc> accnProcs = accessionDao.getAccnProcByAccnIdTestAbbrev(accnId, testAbbrev);
        Assert.assertFalse(accnProcs.isEmpty(), "No proc found for test");
        Assert.assertEquals(accnProcs.get(0).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession proc status is not Priced");
        Assert.assertEquals(accnProcs.get(0).getDueAmtAsMoney().toString(), "2.50", "Accession proc due amount could not be verified");
    }

    @Test(priority = 1, description = "Join Logic")
    @Parameters({"ssoUsername", "ssoPassword", "project", "testSuite", "testCase"})
    public void testPFER_558(String ssoUsername, String ssoPassword, String project, String testSuite, String testCase) throws Exception
    {
        logger.info("*** Testing - testPFER_558 ***");
        logger.info("*** Actions - Turn on SS#1220 (Join Accessions) for Pricing Engine");
        updateSystemSetting(SystemSettingMap.SS_JOIN_ACCNS, "True", "1");
        logger.info("*** Actions: - Clear System Cache in Xifin Admin Portal");
        logIntoSso(ssoUsername, ssoPassword);
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();
        logger.info("*** Actions: - Send WS request to create two accessions that can be joined");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId1 = testProperties.getProperty("NewAccnID");
        String accnId2 = testProperties.getProperty("NewAccnID2");
        String testAbbrev = testProperties.getProperty("TestAbbrev");
        Assert.assertTrue(StringUtils.isNotBlank(accnId1), "Accession ID #1 not found in WS response");
        Assert.assertTrue(StringUtils.isNotBlank(accnId2), "Accession ID #2 not found in WS response");
        Assert.assertTrue(StringUtils.isNotBlank(testAbbrev), "Test ID not found in WS response");
        accessionDao.waitForAccnToBeOutOfQueue(accnId1, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS*2);
        accessionDao.waitForAccnToBeOutOfQueue(accnId2, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS*2);
        accessionDao.waitForAccnToBeOutOfQueue(accnId1, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS*2);
        accessionDao.waitForAccnToBeOutOfQueue(accnId2, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS*2);
        accessionDao.waitForAccnToBeOutOfQueue(accnId1, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS*2);
        accessionDao.waitForAccnToBeOutOfQueue(accnId2, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS*2);
        logger.info("*** Expected Results: - Ensure the primary accession is Priced and the two accessions are joined properly");
        Assert.assertEquals(accessionDao.getAccn(accnId1).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession status is not Priced");
        Assert.assertEquals(accessionDao.getAccn(accnId1).getDueAmtAsMoney().toString(), "2.50", "Accession due amount could not be verified");
        List<AccnProc> accnProcs = accessionDao.getAccnProcByAccnIdTestAbbrev(accnId1, testAbbrev);
        Assert.assertFalse(accnProcs.isEmpty(), "No proc found for test");
        Assert.assertEquals(accnProcs.get(0).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession proc status is not Priced");
        Assert.assertEquals(accnProcs.get(0).getDueAmtAsMoney().toString(), "2.50", "Accession proc due amount could not be verified");
        List<AccnTest> accnTests = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId1, testAbbrev);
        Assert.assertFalse(accnTests.isEmpty(), "No accession test found");
        Assert.assertEquals(accnTests.get(0).getExpPrcAsMoney().toString(), "2.50", "Accession test expect price could not be verified");
        Assert.assertEquals(accnTests.get(0).getBilPrcAsMoney().toString(), "2.50", "Accession test bill price could not be verified");
        Assert.assertEquals(accnTests.get(0).getGrossPrcAsMoney().toString(), "2.50", "Accession test gross price could not be verified");
        Assert.assertEquals(accnTests.get(0).getPrcMthdTypId(), 52, "Accession test pricing method type could not be verified");
        Assert.assertEquals(accnTests.get(0).getBilPrcId(), 950001, "Accession test bill price Id could not be verified");
        Assert.assertEquals(accessionDao.getAccnLnkByAccnId(accnId1).get(0).getLnkAccnId(), accnId2, "Accession join could not be verified");
        String note = "Joined Accn " + accnId2 + " - Tests: " + testAbbrev.toUpperCase().trim();
        Assert.assertTrue(accessionDao.getAccnRmkByAccnIdRmkCdAbbrev(accnId1, "JOIN").get(0).getNote().trim().contains(note), "Accession join note could not be verified");
        logger.info("*** Expected Results: - Ensure the the joined accession is Obsolete");
        Assert.assertEquals(accessionDao.getAccn(accnId2).getStaId(), AccnStatusMap.ACCN_STATUS_SPLIT_OBSOLETE, "Accession #2 status is not Obsolete");

        logger.info("*** Actions: - Turn off Join Logic for Pricing Engine");
        updateSystemSetting(SystemSettingMap.SS_JOIN_ACCNS, "False", "0");
    }

    @Test(priority = 1, description = "Split-Ad Hoc Bill Client-XM")
    @Parameters({"ssoUsername", "ssoPassword", "project", "testSuite", "testCase"})
    public void testPFER_559(String ssoUsername, String ssoPassword, String project, String testSuite, String testCase) throws Exception
    {
        logger.info("*** Testing - testPFER_559 ***");
        logger.info("Sending Accn WS request to create an accession, testCase="+testCase+", accnWsUrl="+config.getProperty(PropertyMap.ACCNWS_URL));
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        String testAbbrev1 = testProperties.getProperty("TestAbbrev1");
        String testAbbrev2 = testProperties.getProperty("TestAbbrev2");
        Assert.assertTrue(StringUtils.isNotBlank(accnId), "Accession ID not found in WS response");
        Assert.assertTrue(StringUtils.isNotBlank(testAbbrev1), "Test ID #1 not found in WS response");
        Assert.assertTrue(StringUtils.isNotBlank(testAbbrev2), "Test ID #2 not found in WS response");
        logger.info("*** Actions: - Navigate to XP Accn Test Update screen and log in");
        logIntoSso(ssoUsername, ssoPassword);
        MenuNavigation navigation = new MenuNavigation(driver, getConfig());
        navigation.navigateToAccnTestUpdatePage();
        logger.info("*** Expected Results: - Verify that the Load Accession page is displayed with correct page title");
        AccnTestUpdate accnTestUpdate = new AccnTestUpdate(driver, wait);
        logger.info("*** Actions: - Load the accession");
        accnTestUpdate.setLookUpAccnId(accnId);
        logger.info("*** Expected Results: - Ensure the accession is loaded in the Accession Test Update screen");
        Assert.assertTrue(accnTestUpdate.isAccnLoaded(accnId, wait));
        logger.info("*** Actions: - Check Cln Bill checkbox for the selected test");
        int accnTestSeqId1 = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev1).get(0).getAccnTestSeqId();
        Assert.assertTrue(isElementPresent(accnTestUpdate.testId(accnTestSeqId1), 5), "Test ID #1 is not displayed");
        accnTestUpdate.clickTestId(accnTestSeqId1);
        Assert.assertTrue(isElementPresent(accnTestUpdate.clnBillChkbox(accnTestSeqId1), 5), "Client BIll checkbox not displayed");
        accnTestUpdate.checkClnBillChkbox(accnTestSeqId1);
        logger.info("*** Actions: - Click Save And Clear button");
        accnTestUpdate.accnIdInput().sendKeys(Keys.ALT + "S");
        logger.info("*** Expected Results: - Ensure the primary accession is Priced and test is split properly");
        accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS*2);
        accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS*2);
        accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS*2);
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession status is not Priced");
        Assert.assertEquals(accessionDao.getAccn(accnId).getDueAmtAsMoney().toString(), "320.00", "Accession due amount could not be verified");
        List<AccnProc> accnProcs = accessionDao.getAccnProcByAccnIdTestAbbrev(accnId, testAbbrev2);
        Assert.assertFalse(accnProcs.isEmpty(), "No proc found for test");
        Assert.assertEquals(accnProcs.get(0).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession proc status is not Priced");
        Assert.assertEquals(accnProcs.get(0).getDueAmtAsMoney().toString(), "320.00", "Accession proc due amount could not be verified");
        List<AccnTest> accnTests = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev2);
        Assert.assertFalse(accnTests.isEmpty(), "No accession test found");
        Assert.assertEquals(accnTests.get(0).getExpPrcAsMoney().toString(), "320.00", "Accession test expect price could not be verified");
        Assert.assertEquals(accnTests.get(0).getBilPrcAsMoney().toString(), "320.00", "Accession test bill price could not be verified");
        Assert.assertEquals(accnTests.get(0).getGrossPrcAsMoney().toString(), "320.00", "Accession test gross price could not be verified");
        Assert.assertEquals(accnTests.get(0).getPrcMthdTypId(), 52, "Accession test pricing method type could not be verified");
        Assert.assertEquals(accnTests.get(0).getBilPrcId(), 950001, "Accession test bill price Id could not be verified");

        logger.info("*** Expected Results: - Ensure a new accession with XM suffix is in Q_Price table");
        String splitAccnId = accnId+"XM";
        accessionDao.waitForAccnToBeOutOfQueue(splitAccnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS*2);
        accessionDao.waitForAccnToBeOutOfQueue(splitAccnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS*2);
        accessionDao.waitForAccnToBeOutOfQueue(splitAccnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS*2);
        Assert.assertEquals(accessionDao.getAccnLnkByAccnId(accnId).get(0).getLnkAccnId(), splitAccnId, "Split accession link could not be verified");
        String note = "AD HOC/Manual Split Accession";
        Assert.assertTrue(accessionDao.getAccnTestMsgByAccnIdAccnTestSeqId(accnId, accnTestSeqId1).get(0).note.trim().contains(note), "Accession test message could not be verified");
        logger.info("*** Expected Results: - Ensure the split accession status is ZBal");
        Assert.assertEquals(accessionDao.getAccn(splitAccnId).getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL, "The split accession status is not ZBal");
        logger.info("*** Expected Results: - Ensure the split accession's Payor is Client");
        Assert.assertEquals(accessionDao.getAccnPyrs(splitAccnId).get(0).getPyrId(), MiscMap.PYR_CLN, "Split accession payor is not Client");
        logger.info("*** Expected Results: - Ensure the split accession has the split test");
        Assert.assertEquals(String.valueOf(accessionDao.getAccnTestByAccnIdTestAbbrev(splitAccnId, testAbbrev1).get(0).getSourceAccnId()), accnId, "Source accession ID could not be verified");
    }



}
