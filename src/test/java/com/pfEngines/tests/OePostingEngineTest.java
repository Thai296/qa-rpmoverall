package com.pfEngines.tests;

import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.accession.orderProcessing.AccnTestUpdate;
import com.overall.menu.MenuNavigation;
import com.overall.utils.AccnTestUpdateUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TestDataSetup;
import com.xifin.xap.utils.XifinAdminUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class OePostingEngineTest extends SeleniumBaseTest {

    @BeforeSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running BeforeSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_PERFORM_JOIN_LOGIC, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_CLIENT_BILLING_RULE_LOGIC_SPLIT, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_PERFORM_PYR_BILLING_RULES, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_DEFAULT_CLN_BILLING_RULES, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_JOIN_ACCNS, "False", "0");
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

    @Test(priority = 1, description = "Process Accession from Q_FR_PENDING")
    @Parameters({"project", "testSuite", "testCase"})
    public void testPFER_15(String project, String testSuite, String testCase) throws Exception
    {
        logger.info("*** Testing - testPFER_15 ***");

        logger.info("*** Step 3 Actions: - Send WS request to create an accession");
        Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = resultProperties.getProperty("NewAccnID");
        logger.info("AccnID: " + accnId);
        Assert.assertNotNull(accnId, "New Accn ID not returned");

        logger.info("*** Step 4 Expected Results: - Ensure the new accession gets taken out of Q_FR_Pending table");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS), "Accession is not out of Pricing Queue");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession status is not Priced");
    }

    @Test(priority = 1, description = "Process Accession from Q_OE by submitting from Accn Test Update")
    @Parameters({"email", "password", "project", "testSuite", "testCase"})
    public void testPFER_16(String email, String password, String project, String testSuite, String testCase) throws Exception
    {
        logger.info("*** Testing - testPFER_16 ***");
        logIntoSso(email, password);
        MenuNavigation menuNavigation = new MenuNavigation(driver, config);
        AccnTestUpdate accnTestUpdate = new AccnTestUpdate(driver, wait);
        AccnTestUpdateUtils accnTestUpdateUtils = new AccnTestUpdateUtils(driver, config, wait);

        logger.info("*** Step 3 Actions: - Send Accession WS request to create an accession");
        Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = resultProperties.getProperty("NewAccnID");
        logger.info("AccnID: " + accnId);
        Assert.assertNotNull(accnId, "New Accn ID not returned");

        logger.info("*** Step 4 Actions: - Navigate to XP - Accn Test Update screen");
        menuNavigation.navigateToAccnTestUpdatePage();

        logger.info("*** Step 4 Expected Results: - Verify that the Load Accession page is displayed with correct page title");
        assertTrue(isElementPresent(accnTestUpdate.loadPageTitle(), 10), "        The Test Update Load Accession page title should show.");
        assertEquals(accnTestUpdate.loadPageTitle().getText(),"Test Update", "        The Load Accession page title should be 'Test Update'");

        logger.info("*** Step 5 Actions: - Enter the newly created Accession ID in Load Accession screen and tab out");
        assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
        accnTestUpdate.setLookUpAccnId(accnId);

        logger.info("*** Step 5 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");
        Thread.sleep(2000);
        assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Test Update page title should be 'Test Update'");

        assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
        assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
        assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, true), "        Save And Clear button should be enabled.");

        logger.info("*** Step 6 Actions: - Click Save And Clear button");
        accnTestUpdate.clickSaveAndClearBtn();

        logger.info("*** Step 6 Expected Results: - Ensure the new accession is in the q_oe table");
        Assert.assertTrue(accessionDao.isInQOEQueue(accnId),"Accession is not in Q OE queue");

        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS), "Accession is not out of Pricing Queue");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession status is not Priced");

    }

}
