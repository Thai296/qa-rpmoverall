package com.mars.tests;

import com.overall.accession.accessionNavigation.AccessionNavigation;
import com.overall.accession.accessionProcessing.AccessionDemographics;
import com.overall.accession.accessionProcessing.AccessionTransactionDetail;
import com.overall.accession.accessionProcessing.ReasonCodeSearch;
import com.overall.accession.accessionProcessing.ReasonCodeSearchResults;
import com.overall.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
import com.overall.fileMaintenance.sysMgt.DataCacheConfiguration;
import com.overall.fileMaintenance.sysMgt.TaskScheduler;
import com.overall.fileMaintenance.sysMgt.TaskStatus;
import com.overall.headerNavigation.HeaderNavigation;
import com.overall.utils.AccessionUtils;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.FileManipulation;
import com.xifin.utils.ListUtil;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegressionFileMaintenanceSysMgtTest extends SeleniumBaseTest  {

	private SsoLogin ssoLogin;
	private HeaderNavigation headerNavigation;
	private FileMaintenanceNavigation fileMaintenaceNavigation;
	private TaskScheduler taskScheduler;
	private TaskStatus taskStatus;
//	private TestDataSetup testDataSetup;
	private AccessionUtils accessionUtils;
	private AccessionDemographics accessionDemographics;
	private AccessionNavigation accessionNavigation;
	private AccessionTransactionDetail accessionTransactionDetail;
	private ReasonCodeSearch reasonCodeSearch;
	private ReasonCodeSearchResults reasonCodeSearchResults;
	private TimeStamp timeStamp;
	private File upOne = new File(System.getProperty("user.dir")).getParentFile();
	private ListUtil listUtil;
	private RandomCharacter randomCharacter;


	@Test(priority = 1, description = "RPM - File Maintenance Tab - System Management - System Data Cache - Reload All Button Exists")
	@Parameters({"email", "password"})
	public void testRPM_328(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_328 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);

		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    }

	@Test(priority = 1, description = "RPM - File Maintenance Tab - System Management - System Data Cache - Reload None Button Exists")
	@Parameters({"email", "password"})
	public void testRPM_329(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_329 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);

		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();

        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    }

	@Test(priority = 1, description = "RPM - File Maintenance Tab - System Management - System Data Cache - Reload All")
	@Parameters({"email", "password"})
	public void testRPM_330(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_330 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);

		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();

        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    }

	@Test(priority = 1, description = "RPM - File Maintenance Tab - System Management - System Data Cache - Reload None")
	@Parameters({"email", "password"})
	public void testRPM_331(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_331 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);

		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();

        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    }

	@Test(priority = 1, description = "RPM - File Maintenance Tab - System Management - System Data Cache - Reset Data Cache Group Reload")
	@Parameters({"email", "password"})
	public void testRPM_332(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_332 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);

		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();

        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();
		driver.close();
    }

	@Test(priority = 1, description = "RPM - File Maintenance Tab - System Management - System Data Cache - Submit Single Group Reload")
	@Parameters({"email", "password"})
	public void testRPM_334(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_334 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);

		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();

        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        //Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    }

	@Test(priority = 1, description = "RPM - File Maintenance Tab - System Management - System Data Cache -  Submit Reload All")
	@Parameters({"email", "password"})
	public void testRPM_335(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_335 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);

		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();

        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    }

	@Test(priority = 1, description = "RPM - Task Scheduler - Pricing Engine - Not to recreate unposted errors")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_478(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_478 *****");

		accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));

		String winHandler = driver.getWindowHandle();

		logger.info("*** Step 17 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();

		logger.info("*** Step 17 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		logger.info("*** Step 18 Actions: - Navigate to Accn Transaction Detail JSP");
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.transactionDetailLink();

		accessionTransactionDetail = new AccessionTransactionDetail(driver);

		logger.info("*** Step 19 Actions: - Load the accn in Accn Transaction Detail in the accnId input field and tab out");
		accessionTransactionDetail.accnIdInputInFrame(accnId);
		Thread.sleep(3000);

		logger.info("*** Step 20 Actions: - Click the Proc Code in the Billable Procedure Code Details grid");
		accessionTransactionDetail.selectProcCodeInFrame();
		//String procCd = accessionTransactionDetail.procCodeTextInFrame();

		logger.info("*** Step 21 Actions: - Click the Add Over-ride button and switch to Reason Code Search JSP");
		accessionTransactionDetail.addOverrideBtn();
		switchToPopupWin();

		logger.info("*** Step 21 Expected Results: - Verify that it's in the Reason Code Search JSP");
		reasonCodeSearch = new ReasonCodeSearch(driver);
		Assert.assertTrue(reasonCodeSearch.reasonCodeInput().isDisplayed(), "        Reason Code Input field should show.");

		logger.info("*** Step 22 Actions: - Click Search in Reason Code Search JSP");
		reasonCodeSearch.searchBtn().click();

		logger.info("*** Step 22 Expected Results: - Verify that it's on the Reason Code Search Results JSP");
		reasonCodeSearchResults = new ReasonCodeSearchResults(driver);
		Assert.assertTrue(reasonCodeSearchResults.reasonCodeSearchResultText(2, 1).isDisplayed(), "        Reason Code Search Results should show.");

		logger.info("*** Step 23 Actions: - Click on one of the Reason Code");
		String overrideCdAbbrev = reasonCodeSearchResults.reasonCodeSearchResultText(2, 1).getText().trim();
		String overrideCd = daoManagerXifinRpm.getErrCdFromERRCDByErrCdAbbrevErrGrp(overrideCdAbbrev, "Over-ride", testDb);
		reasonCodeSearchResults.reasonCodeSearchResultTable(2, 1, overrideCd, overrideCdAbbrev);

		//Switch back to the parent window which is ATD jsp
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//logger.info("*** Step 23 Expected Results: - Verify that the selected Over-ride Code was added to the Billable Procedure Code grid");
		//Assert.assertTrue(getColumnAttribute(accessionTransactionDetail.bpcTableInFrame(), overrideCd), "        Over-ride Code " + overrideCd + " should show.");

		logger.info("*** Step 24 Actions: - Click Submit button");
		accessionTransactionDetail.submitBtnInFrame();
		accessionTransactionDetail.resetBtnInFrame();

		logger.info("*** Step 24 Expected Results: - Verify that a new record was added into accn_proc_err table with b_posted = 0");
		ArrayList<String> errList = daoManagerXifinRpm.getUnpostedErrFromACCNPROCERRByAccnId(accnId, testDb);
		Assert.assertEquals(errList.get(0), overrideCd);

		logger.info("*** Step 25 Actions: - Navigate to Accession Tab");
        headerNavigation.navigateToAccessionTab();
        switchToPopupWin();

		logger.info("*** Step 25 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 26 Actions: - Load the accn in Accn Id field and tab out and check Force to Reprice checkbox and click Submit button");
		accessionDemographics.accnIdInput(accnId);
		accessionDemographics.forceToRepriceCheckbox().click();
		accessionDemographics.submitBtn().click();

		logger.info("*** Step 26 Expected Results: - Verify that the accn is in q_price");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);

		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		logger.info("*** Step 27 Actions: - Navigate to File Maintenance Tab");
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();

		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		logger.info("*** Step 28 Actions: - Navigate to Task Scheduler page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 28 Expected Results: - The Task Scheduler page displays");
		taskScheduler = new TaskScheduler(driver);
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 29 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		logger.info("*** Step 29 Expected Results: - All Task Scheduler Engines are reset");
		Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		logger.info("*** Step 30 Actions: - Set Task Scheduler for Pricing Engine");
		taskScheduler.setTaskScheduler("2", "now", 10);

		logger.info("*** Step 30 Expected Results: - Task Status for Pricing Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive("Pricing Engine"));

		logger.info("*** Step 30 Expected Results: - Ensure Task Status for Pricing Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("Pricing Engine"));

		logger.info("*** Step 31 Action: - Navigate to Task Scheduler");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 31 Expected Results: - The Task Scheduler page displays");
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 32 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		logger.info("*** Step 32 Expected Results: - All Task Scheduler Engines are reset");
		Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		logger.info("*** Step 32 Expected Results: - Ensure the new priced accession is NOT in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 0);

		logger.info("*** Step 33 Actions: - Verify that no previous unposted accn_proc_err was restored");
		Assert.assertEquals(daoManagerXifinRpm.getUnpostedErrFromACCNPROCERRByAccnId(accnId, testDb).size(), 0);

		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
	}

	@Test(priority = 1, description = "RPM - File Maintenance - Task Scheduler - Eligibility Engine - Accn sent to Eligibility Engine")
	@Parameters({"email", "password", "accnId", "pyrAbbrev"})
	public void testRPM_710(String email, String password, String accnId, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_710 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Remove existing Eligibility Transaction History for the accession and the payor in DB");
		daoManagerXifinRpm.deleteEligHistFromACCNELIGHISTByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);

		logger.info("*** Step 2 Actions: - Turn on System Setting #76 (Force unpriced accessions to eligibility engine)");
		daoManagerXifinRpm.setSysSettings(1, 76, testDb);

        logger.info("*** Step 3 Actions: - Navigate to the File Maintenance Page");
        switchToDefaultWinFromFrame();
        String winHandler = driver.getWindowHandle();
        headerNavigation.navigateToFileMaintenanceTab();
        switchToPopupWin();

        logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
        fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
        fileMaintenaceNavigation.navigateToSystemDataCacheLink();
        switchToPopupWin();

        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		//Go to Task Scheduler
		logger.info("*** Step 5 Actions: - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		//Reset All Task Scheduler Engines
		logger.info("*** Step 6 Actions: - Reset All Task Scheduler Engines");
		taskScheduler = new TaskScheduler(driver);
		taskScheduler.resetAllTaskSchedulerConcurrency();

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

        logger.info("*** Step 7 Actions: - Navigate to Accession Tab");
        headerNavigation.navigateToAccessionTab();
        switchToPopupWin();

        //Go to main page
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		logger.info("*** Step 8 Actions: - Navigate to the Accession - Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 8 Expected Results: - The Accession - Demographics page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));

		logger.info("*** Step 9 Actions: - Enter test accn in Accn ID field and tab out");
		accessionDemographics.searchAccnId(accnId);

		logger.info("*** Step 9 Expected Results: - Verify that the accn was loaded");
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled.");

		logger.info("*** Step 10 Actions: - Check Force to Reprice checkbox and click Submit button");
		selectCheckBox(accessionDemographics.forceToRepriceCheckbox());
		accessionDemographics.submitBtn().click();

		logger.info("*** Step 10 Expected Results: - Verify that the accn was in Q_ELIG table with FK_ELIG_STA_TYP_ID = 0 and DT_LST_CHK should be empty");
		ArrayList<String> qEligInfoList = daoManagerXifinRpm.getQEligInfoFromQELIGByAccnId(accnId, testDb);
		String eligStaTypId = qEligInfoList.get(0);
		String inDt = qEligInfoList.get(1);
		String dtLstChk = qEligInfoList.get(2);

		Assert.assertTrue(eligStaTypId.equals("0"), "       Q_ELIG.FK_ELIG_STA_TYP_ID should be 0.");

		timeStamp = new TimeStamp(driver);
		Assert.assertTrue(inDt.equals(timeStamp.getCurrentDate()), "       Accession Id: " + accnId + " should be pushed into Q_ELIG.");

		Assert.assertTrue(dtLstChk.equals(""), "       Q_ELIG.DT_LST_CHK should be empty.");

        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

        logger.info("*** Step 11 Actions: - Navigate to File Maintenance tab");
        headerNavigation.navigateToFileMaintenanceTab();
        switchToPopupWin();

        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

        logger.info("*** Step 12 Actions: - Navigate to Task Scheduler Screen");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 13 Actions: - Reset All Task Scheduler Engines");
		taskScheduler = new TaskScheduler(driver);
		taskScheduler.resetAllTaskSchedulerConcurrency();

		logger.info("*** Step 14 Actions: - Set Task Scheduler for Eligibility Engine");
		taskScheduler.setTaskScheduler("17", "now", 1);

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		logger.info("*** Step 15 Actions: - Navigate to Task Status screen");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();

		logger.info("*** Step 15 Expected Results: - Verify that Task Status for Eligibility Engine is completed");
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineActive("Eligibility Engine"));

		logger.info("*** Step 15 Expected Results: - Verify that the accession is NOT in Q_ELIG table");
		qEligInfoList = daoManagerXifinRpm.getQEligInfoFromQELIGByAccnId(accnId, testDb);
		Assert.assertTrue(qEligInfoList.isEmpty(), "        The Accession ID: " + accnId + " should not in Q_ELIG.");

		logger.info("*** Step 16 Actions: - Turn off System Setting #76 (Force unpriced accessions to eligibility engine)");
		daoManagerXifinRpm.setSysSettings(0, 76, testDb);

		driver.close();
		switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		logger.info("*** Step 17 Actions: - Navigate to Data Cache Configuration screen");
        fileMaintenaceNavigation.navigateToSystemDataCacheLink();
        switchToPopupWin();

        xifinAdminUtils.clearDataCache();
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//Go to Task Scheduler
		logger.info("*** Step 19 Actions: - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		//Reset All Task Scheduler Engines
		logger.info("*** Step 20 Actions: - Reset All Task Scheduler Engines");
		taskScheduler = new TaskScheduler(driver);
		taskScheduler.resetAllTaskSchedulerConcurrency();

		driver.close();
    }

	@Test(priority = 1, description = "RPM - File Maintenance - Task Scheduler - Eligibility Engine - Eligible Subscriber Eligibility Check through Elig Engine")
	@Parameters({"email", "password", "accnId", "pyrAbbrev"})
	public void testRPM_711(String email, String password, String accnId, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_711 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Remove existing Eligibility Transaction History for the accession and the payor in DB");
		daoManagerXifinRpm.deleteEligHistFromACCNELIGHISTByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);

		logger.info("*** Step 2 Actions: - Turn on System Setting #76 (Force unpriced accessions to eligibility engine)");
		daoManagerXifinRpm.setSysSettings(1, 76, testDb);

        logger.info("*** Step 3 Actions: - Navigate to the File Maintenance Page");
        switchToDefaultWinFromFrame();
        String winHandler = driver.getWindowHandle();
        headerNavigation.navigateToFileMaintenanceTab();
        switchToPopupWin();

        logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
        fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
        fileMaintenaceNavigation.navigateToSystemDataCacheLink();
        switchToPopupWin();

        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		//Go to Task Scheduler
		logger.info("*** Step 5 Actions: - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		//Reset All Task Scheduler Engines
		logger.info("*** Step 6 Actions: - Reset All Task Scheduler Engines");
		taskScheduler = new TaskScheduler(driver);
		taskScheduler.resetAllTaskSchedulerConcurrency();

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

        logger.info("*** Step 7 Actions: - Navigate to Accession Tab");
        headerNavigation.navigateToAccessionTab();
        switchToPopupWin();

        //Go to main page
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		logger.info("*** Step 8 Actions: - Navigate to the Accession - Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 8 Expected Results: - The Accession - Demographics page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));

		logger.info("*** Step 9 Actions: - Enter test accn in Accn ID field and tab out");
		accessionDemographics.searchAccnId(accnId);

		logger.info("*** Step 9 Expected Results: - Verify that the accn was loaded");
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled.");

		logger.info("*** Step 10 Actions: - Check Force to Reprice checkbox and click Submit button");
		selectCheckBox(accessionDemographics.forceToRepriceCheckbox());
		accessionDemographics.submitBtn().click();

		logger.info("*** Step 10 Expected Results: - Verify that the accn was in Q_ELIG table with FK_ELIG_STA_TYP_ID = 0 and DT_LST_CHK should be empty");
		ArrayList<String> qEligInfoList = daoManagerXifinRpm.getQEligInfoFromQELIGByAccnId(accnId, testDb);
		String eligStaTypId = qEligInfoList.get(0);
		String inDt = qEligInfoList.get(1);
		String dtLstChk = qEligInfoList.get(2);

		Assert.assertTrue(eligStaTypId.equals("0"), "       Q_ELIG.FK_ELIG_STA_TYP_ID should be 0.");

		timeStamp = new TimeStamp(driver);
		Assert.assertTrue(inDt.equals(timeStamp.getCurrentDate()), "       Accession Id: " + accnId + " should be pushed into Q_ELIG.");

		Assert.assertTrue(dtLstChk.equals(""), "       Q_ELIG.DT_LST_CHK should be empty.");

        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

        logger.info("*** Step 11 Actions: - Navigate to File Maintenance tab");
        headerNavigation.navigateToFileMaintenanceTab();
        switchToPopupWin();

        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

        logger.info("*** Step 12 Actions: - Navigate to Task Scheduler Screen");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 13 Actions: - Reset All Task Scheduler Engines");
		taskScheduler = new TaskScheduler(driver);
		taskScheduler.resetAllTaskSchedulerConcurrency();

		logger.info("*** Step 14 Actions: - Set Task Scheduler for Eligibility Engine");
		taskScheduler.setTaskScheduler("17", "now", 1);

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		logger.info("*** Step 15 Actions: - Navigate to Task Status screen");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();

		logger.info("*** Step 15 Expected Results: - Verify that Task Status for Eligibility Engine is completed");
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineActive("Eligibility Engine"));

		logger.info("*** Step 15 Expected Results: - Verify that the accession is NOT in Q_ELIG table");
		qEligInfoList = daoManagerXifinRpm.getQEligInfoFromQELIGByAccnId(accnId, testDb);
		Assert.assertTrue(qEligInfoList.isEmpty(), "        The Accession ID: " + accnId + " should not in Q_ELIG.");

		logger.info("*** Step 15 Expected Results: - Verify that the accession is eligible. (accn_pyr.fk_elig_sta_typ_id = 1)");
		ArrayList<String> eligStatusInfoList = daoManagerXifinRpm.getEligStatusFromACCNPYRByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);
		eligStaTypId = eligStatusInfoList.get(0);

		Assert.assertTrue(eligStaTypId.equals("1"), "       accn_pyr.fk_elig_sta_typ_id should be 1 (Eligible).");

		logger.info("*** Step 16 Actions: - Turn off System Setting #76 (Force unpriced accessions to eligibility engine)");
		daoManagerXifinRpm.setSysSettings(0, 76, testDb);

		driver.close();
		switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		logger.info("*** Step 17 Actions: - Navigate to Data Cache Configuration screen");
        fileMaintenaceNavigation.navigateToSystemDataCacheLink();
        switchToPopupWin();

        xifinAdminUtils.clearDataCache();

        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		//Go to Task Scheduler
		logger.info("*** Step 19 Actions: - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		//Reset All Task Scheduler Engines
		logger.info("*** Step 20 Actions: - Reset All Task Scheduler Engines");
		taskScheduler = new TaskScheduler(driver);
		taskScheduler.resetAllTaskSchedulerConcurrency();

		driver.close();
    }

	@Test(priority = 1, description = "RPM - File Maintenance - Task Scheduler - Pricing Engine - Exceptions when checks Incremental Pricing Logic")
	@Parameters({"email", "password", "accnId"})
	public void testRPM_727(String email, String password, String accnId) throws Exception {
    	logger.info("***** Testing - testRPM_727 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

        switchToDefaultWinFromFrame();
        String winHandler = driver.getWindowHandle();

        logger.info("*** Step 2 Actions: - Navigate to Accession Tab > Accession - Demographics page");
        headerNavigation.navigateToAccessionTab();
        switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - The Accession - Demographics page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));

		logger.info("*** Step 3 Actions: - Enter test accn in Accn ID field and tab out");
		accessionDemographics.searchAccnId(accnId);

		logger.info("*** Step 3 Expected Results: - Verify that the accn was loaded");
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled.");

		logger.info("*** Step 4 Actions: - Check Force to Reprice checkbox and click Submit button");
		selectCheckBox(accessionDemographics.forceToRepriceCheckbox());
		accessionDemographics.submitBtn().click();

		logger.info("*** Step 4 Expected Results: - Verify that the accn is in Q_PRICE");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1, "        Accession ID: " + accnId + " should be in Q_PRICE.");

		logger.info("*** Step 5 Action: - Navigate to File Maintenance Tab > Task Scheduler");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		headerNavigation.navigateToFileMaintenanceTab();
        switchToPopupWin();

        //Go to main page
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 5 Expected Results: - The Task Scheduler page displays");
		taskScheduler = new TaskScheduler(driver);
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 6 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		logger.info("*** Step 6 Expected Results: - All Task Scheduler Engines are reset");
		Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		logger.info("*** Step 7 Actions: - Set Task Scheduler for Pricing Engine");
		taskScheduler.setTaskScheduler("2", "now", 10);

		logger.info("*** Step 7 Expected Results: - Task Status for Pricing Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive("Pricing Engine"));

		logger.info("*** Step 7 Expected Results: - Ensure Task Status for Pricing Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("Pricing Engine"));

		logger.info("*** Step 8 Action: - Navigate to Task Scheduler");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 8 Expected Results: - The Task Scheduler page displays");
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 9 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		logger.info("*** Step 9 Expected Results: - All Task Scheduler Engines are reset");
		Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		logger.info("*** Step 9 Expected Results: - Ensure the new priced accession is NOT in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 0);

		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		logger.info("*** Step 10 Actions: - Navigate to Accession Tab > Accession - Transaction Detail page");
        headerNavigation.navigateToAccessionTab();
        switchToPopupWin();

        //Go to main page
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		//logger.info("*** Step 6 Actions: - Navigate to the Accession - Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToTransactionDetailLink();
		switchToPopupWin();

		logger.info("*** Step 10 Expected Results: - Verify that it is on Accession - Transaction Detail page");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertTrue(accessionTransactionDetail.pageTitleText().getText().trim().contains("Transaction Detail"), "        Page Title Accession  - Transaction Detail should show.");

		logger.info("*** Step 11 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionTransactionDetail.searchAccnId(accnId);

		logger.info("*** Step 11 Expected Results: - Verify that the Accession is loaded");
		Assert.assertTrue(isElementEnabled(accessionTransactionDetail.submitBtn(), 5, true), "        The Submit button should be enabled.");

		logger.info("*** Step 11 Expected Results: - Verify that the Accession is Priced");
		Assert.assertTrue(accessionTransactionDetail.accnStatusInput().getAttribute("value").trim().contains("Priced"), "        Accession ID: " + accnId + " should be Priced.");

		logger.info("*** Step 11 Expected Results: - Verify that the Single and the Profile Tests are INCREMENTAL Priced");
		//Single test
		Assert.assertTrue(accessionTransactionDetail.incrPriceMethodText("0").trim().contains("INCREMENTAL"), "        The Test Price Method should be INCREMENTAL.");
		//Profile test
		Assert.assertTrue(accessionTransactionDetail.incrPriceMethodText("1").trim().contains("INCREMENTAL"), "        The Profile Test Price Method should be INCREMENTAL.");

		logger.info("*** Step 12 Actions: - Reset the page");
		accessionTransactionDetail.resetBtn().click();
		driver.close();
    }

	@Test(priority = 1, description = "RPM - Task Scheduler - Pricing Engine - Indigent discount logic is no longer working")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "pyrAbbrev"})
	public void testRPM_730(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String pyrAbbrev) throws Exception {
		logger.info("***** Testing - testRPM_730 *****");

		accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));

		String winHandler = driver.getWindowHandle();

		logger.info("*** Step 17 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();

		logger.info("*** Step 17 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 18 Actions: - Enter the Accession ID in the Accn ID field and tab out");
		accessionDemographics.searchAccnId(accnId);

		logger.info("*** Step 18 Expected Results: - Verify that the accn was loaded");
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled after the accn " + accnId + " was loaded.");

		logger.info("*** Step 19 Actions: - Change the Accession Primary Payor the Patient Group Payor");
		accessionDemographics.setNewPayorId(pyrAbbrev);

		logger.info("*** Step 20 Actions: - Set Indigent % to 100");
		accessionDemographics.setIndigent("100");

		logger.info("*** Step 21 Actions: - Click Submit");
		accessionDemographics.submit();

		logger.info("*** Step 21 Expected Results: - Verify that the accn is in q_price");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);

		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		logger.info("*** Step 22 Actions: - Navigate to File Maintenance Tab");
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();

		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		logger.info("*** Step 23 Actions: - Navigate to Task Scheduler page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 23 Expected Results: - The Task Scheduler page displays");
		taskScheduler = new TaskScheduler(driver);
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 24 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		logger.info("*** Step 24 Expected Results: - All Task Scheduler Engines are reset");
		Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		logger.info("*** Step 25 Actions: - Set Task Scheduler for Pricing Engine");
		taskScheduler.setTaskScheduler("2", "now", 10);

		logger.info("*** Step 25 Expected Results: - Task Status for Pricing Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive("Pricing Engine"));

		logger.info("*** Step 25 Expected Results: - Ensure Task Status for Pricing Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("Pricing Engine"));

		logger.info("*** Step 26 Action: - Navigate to Task Scheduler");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 26 Expected Results: - The Task Scheduler page displays");
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 27 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		logger.info("*** Step 27 Expected Results: - All Task Scheduler Engines are reset");
		Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		logger.info("*** Step 27 Expected Results: - Ensure the new priced accession is NOT in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 0, "        Accession: " + accnId + " should not in Q_PRICE.");

		logger.info("*** Step 27 Expected Results: - Verify that the tests on the Accession are priced at minimum price");
		ArrayList<String> testFacList = daoManagerXifinRpm.getTestFacInfoFromTESTFACByAccnIdFacId(accnId, "1", testDb);
		String minPrice = testFacList.get(3);

		ArrayList<String> dueAmtList = daoManagerPlatform.getDueAmtWithBulkFromACCNPROCByAccnIdPyrPrio(accnId, "1", testDb);
		Assert.assertTrue(dueAmtList.get(0).equals(minPrice), "        The Bill Price for the tests on Accession " + accnId + " should be " + minPrice);

		driver.close();
	}

	@Test(priority = 1, description = "RPM - File Maintenance - Task Scheduler - Eligibility Engine - Eligibility Payor Roster Logic")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "pyrAbbrev", "subsId", "transPyrAbbrev"})
	public void testRPM_731(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String pyrAbbrev, String subsId, String transPyrAbbrev) throws Exception {
		logger.info("***** Testing - testRPM_731 *****");

		accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));

		String winHandler = driver.getWindowHandle();

		logger.info("*** Step 17 Actions: - Turn on System Setting #76 (Force unpriced accessions to eligibility engine)");
		daoManagerXifinRpm.setSysSettings(1, 76, testDb);

        logger.info("*** Step 18 Actions: - Navigate to the File Maintenance Page");
        headerNavigation = new HeaderNavigation(driver, config);
        headerNavigation.navigateToFileMaintenanceTab();
        switchToPopupWin();

        logger.info("*** Step 19 Actions: - Navigate to the System Data Cache Page");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
        fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
        fileMaintenaceNavigation.navigateToSystemDataCacheLink();
        switchToPopupWin();

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();

        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		//Go to Task Scheduler
		logger.info("*** Step 20 Actions: - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		//Reset All Task Scheduler Engines
		logger.info("*** Step 20 Actions: - Reset All Task Scheduler Engines");
		taskScheduler = new TaskScheduler(driver);
		taskScheduler.resetAllTaskSchedulerConcurrency();

	    //Go to main page
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

        logger.info("*** Step 21 Actions: - Navigate to Accession Tab");
        headerNavigation.navigateToAccessionTab();
        switchToPopupWin();

	    //Go to main page
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		logger.info("*** Step 22 Actions: - Navigate to the Accession - Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 22 Expected Results: - The Accession - Demographics page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 23 Actions: - Enter test accn in Accn ID field and tab out");
		accessionDemographics.searchAccnId(accnId);

		logger.info("*** Step 23 Expected Results: - Verify that the accn was loaded");
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled after the accn " + accnId + " was loaded.");

		logger.info("*** Step 24 Actions: - Change the Accession Primary Payor the Patient Group Payor");
		accessionDemographics.setNewPayorId(pyrAbbrev);

		logger.info("*** Step 25 Actions: - Set the new Subscriber ID");
		accessionDemographics.setPrimaryPyrSubsId(subsId);

		logger.info("*** Step 26 Actions: - Click Submit");
		accessionDemographics.submit();

		logger.info("*** Step 27 Actions: - Enter the accn in Accn ID field and tab out");
		accessionDemographics.searchAccnId(accnId);

		logger.info("*** Step 27 Expected Results: - Verify that the accn was loaded");
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled after the accn " + accnId + " was loaded.");

		logger.info("*** Step 28 Actions: - Check Force to Reprice checkbox and click Submit button");
		selectCheckBox(accessionDemographics.forceToRepriceCheckbox());
		accessionDemographics.submitBtn().click();
		Thread.sleep(5000);

		logger.info("*** Step 28 Expected Results: - Verify that the accn was in Q_ELIG table with FK_ELIG_STA_TYP_ID = 0 and DT_LST_CHK should be empty");
		ArrayList<String> qEligInfoList = daoManagerXifinRpm.getQEligInfoFromQELIGByAccnId(accnId, testDb);
		String eligStaTypId = qEligInfoList.get(0);
		String inDt = qEligInfoList.get(1);
		String dtLstChk = qEligInfoList.get(2);

		Assert.assertTrue(eligStaTypId.equals("0"), "       Q_ELIG.FK_ELIG_STA_TYP_ID should be 0.");

		timeStamp = new TimeStamp(driver);
		Assert.assertTrue(inDt.equals(timeStamp.getCurrentDate()), "       Accession Id: " + accnId + " should be pushed into Q_ELIG.");

		Assert.assertTrue(dtLstChk.equals(""), "       Q_ELIG.DT_LST_CHK should be empty.");

        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

        logger.info("*** Step 29 Actions: - Navigate to File Maintenance tab");
        headerNavigation.navigateToFileMaintenanceTab();
        switchToPopupWin();

        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

        logger.info("*** Step 30 Actions: - Navigate to Task Scheduler Screen");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 31 Actions: - Reset All Task Scheduler Engines");
		taskScheduler = new TaskScheduler(driver);
		taskScheduler.resetAllTaskSchedulerConcurrency();

		logger.info("*** Step 32 Actions: - Set Task Scheduler for Eligibility Engine");
		taskScheduler.setTaskScheduler("17", "now", 1);

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		logger.info("*** Step 33 Actions: - Navigate to Task Status screen");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();

		logger.info("*** Step 33 Expected Results: - Verify that Task Status for Eligibility Engine is completed");
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineActive("Eligibility Engine"));

		logger.info("*** Step 33 Expected Results: - Verify that the accession is NOT in Q_ELIG table");
		qEligInfoList = daoManagerXifinRpm.getQEligInfoFromQELIGByAccnId(accnId, testDb);
		Assert.assertTrue(qEligInfoList.isEmpty(), "        The Accession ID: " + accnId + " should not in Q_ELIG.");

		logger.info("*** Step 34 Actions: - Turn off System Setting #76 (Force unpriced accessions to eligibility engine)");
		daoManagerXifinRpm.setSysSettings(0, 76, testDb);

		driver.close();
		switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		logger.info("*** Step 35 Actions: - Navigate to Data Cache Configuration screen");
        fileMaintenaceNavigation.navigateToSystemDataCacheLink();
        switchToPopupWin();

		xifinAdminUtils.clearDataCache();

        //Go to main page
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		//Go to Task Scheduler
		logger.info("*** Step 37 Actions: - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		//Reset All Task Scheduler Engines
		logger.info("*** Step 38 Actions: - Reset All Task Scheduler Engines");
		taskScheduler = new TaskScheduler(driver);
		taskScheduler.resetAllTaskSchedulerConcurrency();

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		logger.info("*** Step 39 Actions: - Navigate to the Accession - Demographics page");
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();

		logger.info("*** Step 39 Expected Results: - The Accession - Demographics page displays");
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 40 Actions: - Enter the accn in Accn ID field and tab out");
		accessionDemographics.searchAccnId(accnId);

		logger.info("*** Step 40 Expected Results: - Verify that the accn was loaded");
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled after the accn " + accnId + " was loaded.");

		logger.info("*** Step 40 Expected Results: - Verify that Primary Payor was translated to a new Payor");
		Assert.assertTrue(accessionDemographics.payorIdInput().getAttribute("value").trim().equals(transPyrAbbrev), "        Primary Payor on the Accession " + accnId + " should be translated to " + transPyrAbbrev);

		logger.info("*** Step 40 Expected Results: - Verify that Primary Payor Subscriber ID was not being cleared and kept the same");
		Assert.assertTrue(accessionDemographics.primaryPyrSubsIdInput().getAttribute("value").trim().equals(subsId), "        Primary Payor Subscriber ID on the Accession " + accnId + " should be " + subsId);

		accessionDemographics.resetBtn().click();
		driver.close();
    }

	@Test(priority = 1, description = "RPM - File Maintenance - Task Scheduler - OE Posting Engine - SS1111 is On and SS1147 is On")
	@Parameters({"email", "password", "file", "formatType", "diagCdType", "testAbbrev1", "testAbbrev2", "ss1111DataValue", "ss1147DataValue"})
	public void testRPM_732(String email, String password, String file, String formatType, String diagCdType, String testAbbrev1, String testAbbrev2, String ss1111DataValue, String ss1147DataValue) throws Exception {
    	logger.info("*** Testing - testRPM_732 ***");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Set System Setting 1111 (Determines if test is to be deleted) and 1147 (Use new Debit Credit Logic) to be On");
		daoManagerXifinRpm.setSysSettings(ss1111DataValue, 1111, testDb);
		daoManagerXifinRpm.setSysSettings(ss1147DataValue, 1147, testDb);

        logger.info("*** Step 3 Actions: - Navigate to the File Maintenance Page");
        switchToDefaultWinFromFrame();
        String winHandler = driver.getWindowHandle();
        headerNavigation.navigateToFileMaintenanceTab();
        switchToPopupWin();

        logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
        fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
        fileMaintenaceNavigation.navigateToSystemDataCacheLink();
        switchToPopupWin();

     	XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
		logger.info("*** Step 5 Actions: - Navigate to the File Maintenance Page");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

        headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

    	logger.info("*** Step 6 Actions: - Generate HL7 file to process");
		FileManipulation fm = new FileManipulation();

		List<String> targetList = new ArrayList<String>();
		targetList.add(0, "AUTOTEST0001");
		targetList.add(1, "1114927050");
		targetList.add(2, "001.0");
		targetList.add(3, "GLUCOSE");
		targetList.add(4, "ACME");
		targetList.add(5, "EPI");

		timeStamp = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);
		String accnId = "AUTOOEPOSTING" + timeStamp.getTimeStamp();
		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		String diagCd = daoManagerAccnWS.getDiagnosisCode(diagCdType, testDb);
		String facId = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		String epi = "X" + randomCharacter.getRandomAlphaString(3) + randomCharacter.getRandomNumericString(10);

		List<String> newValList = new ArrayList<String>();
		newValList.add(0, accnId);
		newValList.add(1, npi);
		newValList.add(2, diagCd);
		newValList.add(3, testAbbrev1);
		newValList.add(4, facId);
		newValList.add(5, epi);

		logger.info("*** Step 6 Expected Results: - HL7 file gets generated and saved to files\\hl7\\archive folder in S drive");
		Assert.assertTrue(fm.generateFormatTypeContent(config.getProperty(PropertyMap.ORGALIAS), upOne + File.separator + "src"  + File.separator + "test" + File.separator + "resources"  + File.separator + file, targetList, newValList, formatType));

		logger.info("*** Step 7 Actions: - Navigate to Task Scheduler");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 7 Expected Results: - The Task Scheduler page displays");
		taskScheduler = new TaskScheduler(driver);
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 8 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		logger.info("*** Step 8 Expected Results: - All Task Scheduler Engines are reset");
		Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		logger.info("*** Step 9 Actions: - Set Task Scheduler for HL7 Parsing Engine");
		taskScheduler.setTaskScheduler("18", "now", 10);

		logger.info("*** Step 9 Expected Results: - Task Status for HL7 Parsing Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive("HL7 Parsing Engine"));

		logger.info("*** Step 9 Expected Results: - Ensure Task Status for HL7 Parsing Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("HL7 Parsing Engine"));

		logger.info("*** Step 9 Expected Results: - HL7 file gets removed from the in folder");
		String dirBase = fm.getDirBase();
		File f = new File(dirBase + File.separator + "in" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should be removed from the in folder.");

		logger.info("*** Step 9 Expected Results: - HL7 file is stored in archive directory");
		f = new File(dirBase + File.separator + "archive" + File.separator + fm.getFileName());
		Assert.assertTrue(isFileExists(f, 5), "        HL7 file: " + fm.getFileName() + " should be in the archive folder.");

		logger.info("*** Step 9 Expected Results: - HL7 file is not in err directory");
		f = new File(dirBase + File.separator + "err" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should not be in the err folder.");

		logger.info("*** Step 9 Expected Results: - HL7 file is not in dup directory");
		f = new File(dirBase + File.separator + "dup" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should not be in the dup folder.");

		logger.info("*** Step 10 Actions: - Navigate to Task Scheduler");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 10 Expected Results: - The Task Scheduler page displays");
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 11 Actions: - Set Task Scheduler for OE Posting Engine");
		taskScheduler.setTaskScheduler("1", "now", 10);

		logger.info("*** Step 11 Expected Results: - Task Status for OE Posting Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive("OE Posting Engine"));

		logger.info("*** Step 11 Expected Results: - Ensure Task Status for OE Posting Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));

		logger.info("*** Step 11 Exepected Results: The new accession is in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1, "       Accession: " + accnId + " should be in Q_FR_Pending.");

    	logger.info("*** Step 11 Exepected Results: A Test should be added to the Accession");
    	ArrayList<String> testInfoList = daoManagerAccnWS.getTestIdFacIdFromAccnTestByAccnId(accnId, testDb);
    	String testAbbrevinAccn = daoManagerAccnWS.getTestAbbrevFromTESTByTestId(testInfoList.get(0), testDb).trim();
    	Assert.assertEquals(testAbbrevinAccn, testAbbrev1, "        Test: " + testAbbrev1 + " should be added to the Accession " + accnId);

    	logger.info("*** Step 12 Actions: - Generate a new HL7 file with a different test to process");
		targetList = new ArrayList<String>();
		targetList.add(0, "AUTOTEST0001");
		targetList.add(1, "1114927050");
		targetList.add(2, "001.0");
		targetList.add(3, "GLUCOSE");
		targetList.add(4, "ACME");
		targetList.add(5, "EPI");

		newValList = new ArrayList<String>();
		newValList.add(0, accnId);
		newValList.add(1, npi);
		newValList.add(2, diagCd);
		newValList.add(3, testAbbrev2);
		newValList.add(4, facId);
		newValList.add(5, epi);

		logger.info("*** Step 12 Expected Results: - HL7 file gets generated and saved to files\\hl7\\archive folder in S drive");
		Assert.assertTrue(fm.generateFormatTypeContent(config.getProperty(PropertyMap.ORGALIAS), upOne + File.separator + "src"  + File.separator + "test" + File.separator + "resources"  + File.separator + file, targetList, newValList, formatType));

		logger.info("*** Step 13 Actions: - Navigate to Task Scheduler");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 13 Expected Results: - The Task Scheduler page displays");
		taskScheduler = new TaskScheduler(driver);
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 14 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		logger.info("*** Step 14 Expected Results: - All Task Scheduler Engines are reset");
		Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		logger.info("*** Step 15 Actions: - Set Task Scheduler for HL7 Parsing Engine");
		taskScheduler.setTaskScheduler("18", "now", 10);

		logger.info("*** Step 15 Expected Results: - Task Status for HL7 Parsing Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		Assert.assertFalse(taskStatus.isEngineInactive("HL7 Parsing Engine"));

		logger.info("*** Step 15 Expected Results: - Ensure Task Status for HL7 Parsing Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("HL7 Parsing Engine"));

		logger.info("*** Step 15 Expected Results: - HL7 file gets removed from the in folder");
		f = new File(dirBase + File.separator + "in" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should be removed from the in folder.");

		logger.info("*** Step 15 Expected Results: - HL7 file is stored in archive directory");
		f = new File(dirBase + File.separator + "archive" + File.separator + fm.getFileName());
		Assert.assertTrue(isFileExists(f, 5), "        HL7 file: " + fm.getFileName() + " should be in the archive folder.");

		logger.info("*** Step 15 Expected Results: - HL7 file is not in err directory");
		f = new File(dirBase + File.separator + "err" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should not be in the err folder.");

		logger.info("*** Step 15 Expected Results: - HL7 file is not in dup directory");
		f = new File(dirBase + File.separator + "dup" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should not be in the dup folder.");

		logger.info("*** Step 16 Actions: - Navigate to Task Scheduler");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 16 Expected Results: - The Task Scheduler page displays");
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 17 Actions: - Set Task Scheduler for OE Posting Engine");
		taskScheduler.setTaskScheduler("1", "now", 10);

		logger.info("*** Step 17 Expected Results: - Task Status for OE Posting Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive("OE Posting Engine"));

		logger.info("*** Step 17 Expected Results: - Ensure Task Status for OE Posting Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));

		logger.info("*** Step 17 Exepected Results: The new accession is in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1, "       Accession: " + accnId + " should be in Q_FR_Pending.");

    	logger.info("*** Step 17 Exepected Results: The old Test should be removed and a new Test should be added to the Accession");
    	testInfoList = daoManagerAccnWS.getTestIdFacIdFromAccnTestByAccnId(accnId, testDb);
    	testAbbrevinAccn = daoManagerAccnWS.getTestAbbrevFromTESTByTestId(testInfoList.get(0), testDb).trim();
    	Assert.assertEquals(testAbbrevinAccn, testAbbrev2, "        Test: " + testAbbrev2 + " should be added to the Accession " + accnId);
	}

	@Test(priority = 1, description = "RPM - File Maintenance - Task Scheduler - OE Posting Engine - SS1111 is On and SS1147 is Off")
	@Parameters({"email", "password", "file", "formatType", "diagCdType", "testAbbrev1", "testAbbrev2", "ss1111DataValue", "ss1147DataValue"})
	public void testRPM_733(String email, String password, String file, String formatType, String diagCdType, String testAbbrev1, String testAbbrev2, String ss1111DataValue, String ss1147DataValue) throws Exception {
    	logger.info("*** Testing - testRPM_733 ***");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Set System Setting 1111 (Determines if test is to be deleted) and 1147 (Use new Debit Credit Logic)");
		daoManagerXifinRpm.setSysSettings(ss1111DataValue, 1111, testDb);
		daoManagerXifinRpm.setSysSettings(ss1147DataValue, 1147, testDb);

        logger.info("*** Step 3 Actions: - Navigate to the File Maintenance Page");
        switchToDefaultWinFromFrame();
        String winHandler = driver.getWindowHandle();
        headerNavigation.navigateToFileMaintenanceTab();
        switchToPopupWin();

        logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
        fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
        fileMaintenaceNavigation.navigateToSystemDataCacheLink();
        switchToPopupWin();

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();

		logger.info("*** Step 5 Actions: - Navigate to the File Maintenance Page");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

        headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

    	logger.info("*** Step 6 Actions: - Generate HL7 file to process");
		FileManipulation fm = new FileManipulation();

		List<String> targetList = new ArrayList<String>();
		targetList.add(0, "AUTOTEST0001");
		targetList.add(1, "1114927050");
		targetList.add(2, "001.0");
		targetList.add(3, "GLUCOSE");
		targetList.add(4, "ACME");
		targetList.add(5, "EPI");

		timeStamp = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);
		String accnId = "AUTOOEPOSTING" + timeStamp.getTimeStamp();
		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		String diagCd = daoManagerAccnWS.getDiagnosisCode(diagCdType, testDb);
		String facId = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		String epi = "X" + randomCharacter.getRandomAlphaString(3) + randomCharacter.getRandomNumericString(10);

		List<String> newValList = new ArrayList<String>();
		newValList.add(0, accnId);
		newValList.add(1, npi);
		newValList.add(2, diagCd);
		newValList.add(3, testAbbrev1);
		newValList.add(4, facId);
		newValList.add(5, epi);

		logger.info("*** Step 6 Expected Results: - HL7 file gets generated and saved to files\\hl7\\archive folder in S drive");
		Assert.assertTrue(fm.generateFormatTypeContent(config.getProperty(PropertyMap.ORGALIAS), upOne + File.separator + "src"  + File.separator + "test" + File.separator + "resources"  + File.separator + file, targetList, newValList, formatType));

		logger.info("*** Step 7 Actions: - Navigate to Task Scheduler");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 7 Expected Results: - The Task Scheduler page displays");
		taskScheduler = new TaskScheduler(driver);
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 8 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		logger.info("*** Step 8 Expected Results: - All Task Scheduler Engines are reset");
		Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		logger.info("*** Step 9 Actions: - Set Task Scheduler for HL7 Parsing Engine");
		taskScheduler.setTaskScheduler("18", "now", 10);

		logger.info("*** Step 9 Expected Results: - Task Status for HL7 Parsing Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive("HL7 Parsing Engine"));

		logger.info("*** Step 9 Expected Results: - Ensure Task Status for HL7 Parsing Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("HL7 Parsing Engine"));

		logger.info("*** Step 9 Expected Results: - HL7 file gets removed from the in folder");
		String dirBase = fm.getDirBase();
		File f = new File(dirBase + File.separator + "in" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should be removed from the in folder.");

		logger.info("*** Step 9 Expected Results: - HL7 file is stored in archive directory");
		f = new File(dirBase + File.separator + "archive" + File.separator + fm.getFileName());
		Assert.assertTrue(isFileExists(f, 5), "        HL7 file: " + fm.getFileName() + " should be in the archive folder.");

		logger.info("*** Step 9 Expected Results: - HL7 file is not in err directory");
		f = new File(dirBase + File.separator + "err" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should not be in the err folder.");

		logger.info("*** Step 9 Expected Results: - HL7 file is not in dup directory");
		f = new File(dirBase + File.separator + "dup" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should not be in the dup folder.");

		logger.info("*** Step 10 Actions: - Navigate to Task Scheduler");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 10 Expected Results: - The Task Scheduler page displays");
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 11 Actions: - Set Task Scheduler for OE Posting Engine");
		taskScheduler.setTaskScheduler("1", "now", 10);

		logger.info("*** Step 11 Expected Results: - Task Status for OE Posting Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive("OE Posting Engine"));

		logger.info("*** Step 11 Expected Results: - Ensure Task Status for OE Posting Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));

		logger.info("*** Step 11 Exepected Results: The new accession is in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1, "       Accession: " + accnId + " should be in Q_FR_Pending.");

    	logger.info("*** Step 11 Exepected Results: A Test should be added to the Accession");
    	ArrayList<String> testInfoList = daoManagerAccnWS.getTestIdFacIdFromAccnTestByAccnId(accnId, testDb);
    	String testAbbrevinAccn = daoManagerAccnWS.getTestAbbrevFromTESTByTestId(testInfoList.get(0), testDb).trim();
    	Assert.assertEquals(testAbbrevinAccn, testAbbrev1, "        Test: " + testAbbrev1 + " should be added to the Accession " + accnId);

    	logger.info("*** Step 12 Actions: - Generate a new HL7 file with a different test to process");
		targetList = new ArrayList<String>();
		targetList.add(0, "AUTOTEST0001");
		targetList.add(1, "1114927050");
		targetList.add(2, "001.0");
		targetList.add(3, "GLUCOSE");
		targetList.add(4, "ACME");
		targetList.add(5, "EPI");

		newValList = new ArrayList<String>();
		newValList.add(0, accnId);
		newValList.add(1, npi);
		newValList.add(2, diagCd);
		newValList.add(3, testAbbrev2);
		newValList.add(4, facId);
		newValList.add(5, epi);

		logger.info("*** Step 12 Expected Results: - HL7 file gets generated and saved to files\\hl7\\archive folder in S drive");
		Assert.assertTrue(fm.generateFormatTypeContent(config.getProperty(PropertyMap.ORGALIAS), upOne + File.separator + "src"  + File.separator + "test" + File.separator + "resources"  + File.separator + file, targetList, newValList, formatType));


		logger.info("*** Step 13 Actions: - Navigate to Task Scheduler");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 13 Expected Results: - The Task Scheduler page displays");
		taskScheduler = new TaskScheduler(driver);
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 14 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		logger.info("*** Step 14 Expected Results: - All Task Scheduler Engines are reset");
		Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		logger.info("*** Step 15 Actions: - Set Task Scheduler for HL7 Parsing Engine");
		taskScheduler.setTaskScheduler("18", "now", 10);

		logger.info("*** Step 15 Expected Results: - Task Status for HL7 Parsing Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		Assert.assertFalse(taskStatus.isEngineInactive("HL7 Parsing Engine"));

		logger.info("*** Step 15 Expected Results: - Ensure Task Status for HL7 Parsing Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("HL7 Parsing Engine"));

		logger.info("*** Step 15 Expected Results: - HL7 file gets removed from the in folder");
		f = new File(dirBase + File.separator + "in" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should be removed from the in folder.");

		logger.info("*** Step 15 Expected Results: - HL7 file is stored in archive directory");
		f = new File(dirBase + File.separator + "archive" + File.separator + fm.getFileName());
		Assert.assertTrue(isFileExists(f, 5), "        HL7 file: " + fm.getFileName() + " should be in the archive folder.");

		logger.info("*** Step 15 Expected Results: - HL7 file is not in err directory");
		f = new File(dirBase + File.separator + "err" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should not be in the err folder.");

		logger.info("*** Step 15 Expected Results: - HL7 file is not in dup directory");
		f = new File(dirBase + File.separator + "dup" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should not be in the dup folder.");

		logger.info("*** Step 16 Actions: - Navigate to Task Scheduler");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 16 Expected Results: - The Task Scheduler page displays");
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 17 Actions: - Set Task Scheduler for OE Posting Engine");
		taskScheduler.setTaskScheduler("1", "now", 10);

		logger.info("*** Step 17 Expected Results: - Task Status for OE Posting Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive("OE Posting Engine"));

		logger.info("*** Step 17 Expected Results: - Ensure Task Status for OE Posting Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));

		logger.info("*** Step 17 Exepected Results: The new accession is in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1, "       Accession: " + accnId + " should be in Q_FR_Pending.");

    	logger.info("*** Step 17 Exepected Results: The old Test should be removed and a new Test should be added to the Accession");
    	testInfoList = daoManagerAccnWS.getTestIdFacIdFromAccnTestByAccnId(accnId, testDb);
    	testAbbrevinAccn = daoManagerAccnWS.getTestAbbrevFromTESTByTestId(testInfoList.get(0), testDb).trim();
    	Assert.assertEquals(testAbbrevinAccn, testAbbrev2, "        Test: " + testAbbrev2 + " should be added to the Accession " + accnId);
	}

	@Test(priority = 1, description = "RPM - File Maintenance - Task Scheduler - OE Posting Engine - SS1111 is Off and SS1147 is On")
	@Parameters({"email", "password", "file", "formatType", "diagCdType", "testAbbrev1", "testAbbrev2", "ss1111DataValue", "ss1147DataValue"})
	public void testRPM_734(String email, String password, String file, String formatType, String diagCdType, String testAbbrev1, String testAbbrev2, String ss1111DataValue, String ss1147DataValue) throws Exception {
    	logger.info("*** Testing - testRPM_734 ***");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Set System Setting 1111 (Determines if test is to be deleted) and 1147 (Use new Debit Credit Logic)");
		daoManagerXifinRpm.setSysSettings(ss1111DataValue, 1111, testDb);
		daoManagerXifinRpm.setSysSettings(ss1147DataValue, 1147, testDb);

        logger.info("*** Step 3 Actions: - Navigate to the File Maintenance Page");
        switchToDefaultWinFromFrame();
        String winHandler = driver.getWindowHandle();
        headerNavigation.navigateToFileMaintenanceTab();
        switchToPopupWin();

        logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
        fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
        fileMaintenaceNavigation.navigateToSystemDataCacheLink();
        switchToPopupWin();

        logger.info("*** Step 4 Actions: - Clear all the Cache");
		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();

		logger.info("*** Step 5 Actions: - Navigate to the File Maintenance Page");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

        headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

    	logger.info("*** Step 6 Actions: - Generate HL7 file to process");
		FileManipulation fm = new FileManipulation();

		List<String> targetList = new ArrayList<String>();
		targetList.add(0, "AUTOTEST0001");
		targetList.add(1, "1114927050");
		targetList.add(2, "001.0");
		targetList.add(3, "GLUCOSE");
		targetList.add(4, "ACME");
		targetList.add(5, "EPI");

		timeStamp = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);
		String accnId = "AUTOOEPOSTING" + timeStamp.getTimeStamp();
		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		String diagCd = daoManagerAccnWS.getDiagnosisCode(diagCdType, testDb);
		String facId = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		String epi = "X" + randomCharacter.getRandomAlphaString(3) + randomCharacter.getRandomNumericString(10);

		List<String> newValList = new ArrayList<String>();
		newValList.add(0, accnId);
		newValList.add(1, npi);
		newValList.add(2, diagCd);
		newValList.add(3, testAbbrev1);
		newValList.add(4, facId);
		newValList.add(5, epi);

		logger.info("*** Step 6 Expected Results: - HL7 file gets generated and saved to files\\hl7\\archive folder in S drive");
		Assert.assertTrue(fm.generateFormatTypeContent(config.getProperty(PropertyMap.ORGALIAS), upOne + File.separator + "src"  + File.separator + "test" + File.separator + "resources"  + File.separator + file, targetList, newValList, formatType));

		logger.info("*** Step 7 Actions: - Navigate to Task Scheduler");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 7 Expected Results: - The Task Scheduler page displays");
		taskScheduler = new TaskScheduler(driver);
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 8 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		logger.info("*** Step 8 Expected Results: - All Task Scheduler Engines are reset");
		Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		logger.info("*** Step 9 Actions: - Set Task Scheduler for HL7 Parsing Engine");
		taskScheduler.setTaskScheduler("18", "now", 10);

		logger.info("*** Step 9 Expected Results: - Task Status for HL7 Parsing Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive("HL7 Parsing Engine"));

		logger.info("*** Step 9 Expected Results: - Ensure Task Status for HL7 Parsing Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("HL7 Parsing Engine"));

		logger.info("*** Step 9 Expected Results: - HL7 file gets removed from the in folder");
		String dirBase = fm.getDirBase();
		File f = new File(dirBase + File.separator + "in" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should be removed from the in folder.");

		logger.info("*** Step 9 Expected Results: - HL7 file is stored in archive directory");
		f = new File(dirBase + File.separator + "archive" + File.separator + fm.getFileName());
		Assert.assertTrue(isFileExists(f, 5), "        HL7 file: " + fm.getFileName() + " should be in the archive folder.");

		logger.info("*** Step 9 Expected Results: - HL7 file is not in err directory");
		f = new File(dirBase + File.separator + "err" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should not be in the err folder.");

		logger.info("*** Step 9 Expected Results: - HL7 file is not in dup directory");
		f = new File(dirBase + File.separator + "dup" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should not be in the dup folder.");

		logger.info("*** Step 10 Actions: - Navigate to Task Scheduler");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 10 Expected Results: - The Task Scheduler page displays");
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 11 Actions: - Set Task Scheduler for OE Posting Engine");
		taskScheduler.setTaskScheduler("1", "now", 10);

		logger.info("*** Step 11 Expected Results: - Task Status for OE Posting Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive("OE Posting Engine"));

		logger.info("*** Step 11 Expected Results: - Ensure Task Status for OE Posting Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));

		logger.info("*** Step 11 Exepected Results: The new accession is in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1, "       Accession: " + accnId + " should be in Q_FR_Pending.");

    	logger.info("*** Step 11 Exepected Results: A Test should be added to the Accession");
    	ArrayList<String> testInfoList = daoManagerAccnWS.getTestIdFacIdFromAccnTestByAccnId(accnId, testDb);
    	String testAbbrevinAccn = daoManagerAccnWS.getTestAbbrevFromTESTByTestId(testInfoList.get(0), testDb).trim();
    	Assert.assertEquals(testAbbrevinAccn, testAbbrev1, "        Test: " + testAbbrev1 + " should be added to the Accession " + accnId);

    	logger.info("*** Step 12 Actions: - Generate a new HL7 file with a different test to process");
		targetList = new ArrayList<String>();
		targetList.add(0, "AUTOTEST0001");
		targetList.add(1, "1114927050");
		targetList.add(2, "001.0");
		targetList.add(3, "GLUCOSE");
		targetList.add(4, "ACME");
		targetList.add(5, "EPI");

		newValList = new ArrayList<String>();
		newValList.add(0, accnId);
		newValList.add(1, npi);
		newValList.add(2, diagCd);
		newValList.add(3, testAbbrev2);
		newValList.add(4, facId);
		newValList.add(5, epi);

		logger.info("*** Step 12 Expected Results: - HL7 file gets generated and saved to files\\hl7\\archive folder in S drive");
		Assert.assertTrue(fm.generateFormatTypeContent(config.getProperty(PropertyMap.ORGALIAS), upOne + File.separator + "src"  + File.separator + "test" + File.separator + "resources"  + File.separator + file, targetList, newValList, formatType));


		logger.info("*** Step 13 Actions: - Navigate to Task Scheduler");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 13 Expected Results: - The Task Scheduler page displays");
		taskScheduler = new TaskScheduler(driver);
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 14 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		logger.info("*** Step 14 Expected Results: - All Task Scheduler Engines are reset");
		Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		logger.info("*** Step 15 Actions: - Set Task Scheduler for HL7 Parsing Engine");
		taskScheduler.setTaskScheduler("18", "now", 10);

		logger.info("*** Step 15 Expected Results: - Task Status for HL7 Parsing Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		Assert.assertFalse(taskStatus.isEngineInactive("HL7 Parsing Engine"));

		logger.info("*** Step 15 Expected Results: - Ensure Task Status for HL7 Parsing Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("HL7 Parsing Engine"));

		logger.info("*** Step 15 Expected Results: - HL7 file gets removed from the in folder");
		f = new File(dirBase + File.separator + "in" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should be removed from the in folder.");

		logger.info("*** Step 15 Expected Results: - HL7 file is stored in archive directory");
		f = new File(dirBase + File.separator + "archive" + File.separator + fm.getFileName());
		Assert.assertTrue(isFileExists(f, 5), "        HL7 file: " + fm.getFileName() + " should be in the archive folder.");

		logger.info("*** Step 15 Expected Results: - HL7 file is not in err directory");
		f = new File(dirBase + File.separator + "err" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should not be in the err folder.");

		logger.info("*** Step 15 Expected Results: - HL7 file is not in dup directory");
		f = new File(dirBase + File.separator + "dup" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should not be in the dup folder.");

		logger.info("*** Step 16 Actions: - Navigate to Task Scheduler");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 16 Expected Results: - The Task Scheduler page displays");
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 17 Actions: - Set Task Scheduler for OE Posting Engine");
		taskScheduler.setTaskScheduler("1", "now", 10);

		logger.info("*** Step 17 Expected Results: - Task Status for OE Posting Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive("OE Posting Engine"));

		logger.info("*** Step 17 Expected Results: - Ensure Task Status for OE Posting Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));

		logger.info("*** Step 17 Exepected Results: The new accession is in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1, "       Accession: " + accnId + " should be in Q_FR_Pending.");

    	logger.info("*** Step 17 Exepected Results: Both old Test and the new Test should be on the Accession");
    	ArrayList<String> testAbbrevList = daoManagerXifinRpm.getTestAbbrevFromACCNTESTByAccnId(accnId, testDb);

    	ArrayList<String> expectedTestAbbrevList = new ArrayList<String>();
    	expectedTestAbbrevList.add(testAbbrev1);
    	expectedTestAbbrevList.add(testAbbrev2);

    	listUtil = new ListUtil();
    	Assert.assertTrue(listUtil.compareLists(testAbbrevList, expectedTestAbbrevList), "        Both Tests " + testAbbrev1 + " and " + testAbbrev2 + " should be added to the Accession " + accnId);
	}

	@Test(priority = 1, description = "RPM - File Maintenance - Task Scheduler - OE Posting Engine - SS1111 is Off and SS1147 is Off")
	@Parameters({"email", "password", "file", "formatType", "diagCdType", "testAbbrev1", "testAbbrev2", "ss1111DataValue", "ss1147DataValue"})
	public void testRPM_735(String email, String password, String file, String formatType, String diagCdType, String testAbbrev1, String testAbbrev2, String ss1111DataValue, String ss1147DataValue) throws Exception {
    	logger.info("*** Testing - testRPM_735 ***");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Set System Setting 1111 (Determines if test is to be deleted) and 1147 (Use new Debit Credit Logic)");
		daoManagerXifinRpm.setSysSettings(ss1111DataValue, 1111, testDb);
		daoManagerXifinRpm.setSysSettings(ss1147DataValue, 1147, testDb);

        logger.info("*** Step 3 Actions: - Navigate to the File Maintenance Page");
        switchToDefaultWinFromFrame();
        String winHandler = driver.getWindowHandle();
        headerNavigation.navigateToFileMaintenanceTab();
        switchToPopupWin();

        logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
        fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
        fileMaintenaceNavigation.navigateToSystemDataCacheLink();
        switchToPopupWin();

        logger.info("*** Step 4 Actions: - Clear all the Cache");
		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();

		logger.info("*** Step 5 Actions: - Navigate to the File Maintenance Page");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

        headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

    	logger.info("*** Step 6 Actions: - Generate HL7 file to process");
		FileManipulation fm = new FileManipulation();

		List<String> targetList = new ArrayList<String>();
		targetList.add(0, "AUTOTEST0001");
		targetList.add(1, "1114927050");
		targetList.add(2, "001.0");
		targetList.add(3, "GLUCOSE");
		targetList.add(4, "ACME");
		targetList.add(5, "EPI");

		timeStamp = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);
		String accnId = "AUTOOEPOSTING" + timeStamp.getTimeStamp();
		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		String diagCd = daoManagerAccnWS.getDiagnosisCode(diagCdType, testDb);
		String facId = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		String epi = "X" + randomCharacter.getRandomAlphaString(3) + randomCharacter.getRandomNumericString(10);

		List<String> newValList = new ArrayList<String>();
		newValList.add(0, accnId);
		newValList.add(1, npi);
		newValList.add(2, diagCd);
		newValList.add(3, testAbbrev1);
		newValList.add(4, facId);
		newValList.add(5, epi);

		logger.info("*** Step 6 Expected Results: - HL7 file gets generated and saved to files\\hl7\\archive folder in S drive");
		Assert.assertTrue(fm.generateFormatTypeContent(config.getProperty(PropertyMap.ORGALIAS), upOne + File.separator + "src"  + File.separator + "test" + File.separator + "resources"  + File.separator + file, targetList, newValList, formatType));

		logger.info("*** Step 7 Actions: - Navigate to Task Scheduler");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 7 Expected Results: - The Task Scheduler page displays");
		taskScheduler = new TaskScheduler(driver);
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 8 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		logger.info("*** Step 8 Expected Results: - All Task Scheduler Engines are reset");
		Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		logger.info("*** Step 9 Actions: - Set Task Scheduler for HL7 Parsing Engine");
		taskScheduler.setTaskScheduler("18", "now", 10);

		logger.info("*** Step 9 Expected Results: - Task Status for HL7 Parsing Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive("HL7 Parsing Engine"));

		logger.info("*** Step 9 Expected Results: - Ensure Task Status for HL7 Parsing Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("HL7 Parsing Engine"));

		logger.info("*** Step 9 Expected Results: - HL7 file gets removed from the in folder");
		String dirBase = fm.getDirBase();
		File f = new File(dirBase + File.separator + "in" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should be removed from the in folder.");

		logger.info("*** Step 9 Expected Results: - HL7 file is stored in archive directory");
		f = new File(dirBase + File.separator + "archive" + File.separator + fm.getFileName());
		Assert.assertTrue(isFileExists(f, 5), "        HL7 file: " + fm.getFileName() + " should be in the archive folder.");

		logger.info("*** Step 9 Expected Results: - HL7 file is not in err directory");
		f = new File(dirBase + File.separator + "err" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should not be in the err folder.");

		logger.info("*** Step 9 Expected Results: - HL7 file is not in dup directory");
		f = new File(dirBase + File.separator + "dup" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should not be in the dup folder.");

		logger.info("*** Step 10 Actions: - Navigate to Task Scheduler");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 10 Expected Results: - The Task Scheduler page displays");
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 11 Actions: - Set Task Scheduler for OE Posting Engine");
		taskScheduler.setTaskScheduler("1", "now", 10);

		logger.info("*** Step 11 Expected Results: - Task Status for OE Posting Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive("OE Posting Engine"));

		logger.info("*** Step 11 Expected Results: - Ensure Task Status for OE Posting Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));

		logger.info("*** Step 11 Exepected Results: The new accession is in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1, "       Accession: " + accnId + " should be in Q_FR_Pending.");

    	logger.info("*** Step 11 Exepected Results: A Test should be added to the Accession");
    	ArrayList<String> testInfoList = daoManagerAccnWS.getTestIdFacIdFromAccnTestByAccnId(accnId, testDb);
    	String testAbbrevinAccn = daoManagerAccnWS.getTestAbbrevFromTESTByTestId(testInfoList.get(0), testDb).trim();
    	Assert.assertEquals(testAbbrevinAccn, testAbbrev1, "        Test: " + testAbbrev1 + " should be added to the Accession " + accnId);

    	logger.info("*** Step 12 Actions: - Generate a new HL7 file with a different test to process");
		targetList = new ArrayList<String>();
		targetList.add(0, "AUTOTEST0001");
		targetList.add(1, "1114927050");
		targetList.add(2, "001.0");
		targetList.add(3, "GLUCOSE");
		targetList.add(4, "ACME");
		targetList.add(5, "EPI");

		newValList = new ArrayList<String>();
		newValList.add(0, accnId);
		newValList.add(1, npi);
		newValList.add(2, diagCd);
		newValList.add(3, testAbbrev2);
		newValList.add(4, facId);
		newValList.add(5, epi);

		logger.info("*** Step 12 Expected Results: - HL7 file gets generated and saved to files\\hl7\\archive folder in S drive");
		Assert.assertTrue(fm.generateFormatTypeContent(config.getProperty(PropertyMap.ORGALIAS), upOne + File.separator + "src"  + File.separator + "test" + File.separator + "resources"  + File.separator + file, targetList, newValList, formatType));

		logger.info("*** Step 13 Actions: - Navigate to Task Scheduler");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 13 Expected Results: - The Task Scheduler page displays");
		taskScheduler = new TaskScheduler(driver);
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 14 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		logger.info("*** Step 14 Expected Results: - All Task Scheduler Engines are reset");
		Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		logger.info("*** Step 15 Actions: - Set Task Scheduler for HL7 Parsing Engine");
		taskScheduler.setTaskScheduler("18", "now", 10);

		logger.info("*** Step 15 Expected Results: - Task Status for HL7 Parsing Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		Assert.assertFalse(taskStatus.isEngineInactive("HL7 Parsing Engine"));

		logger.info("*** Step 15 Expected Results: - Ensure Task Status for HL7 Parsing Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("HL7 Parsing Engine"));

		logger.info("*** Step 15 Expected Results: - HL7 file gets removed from the in folder");
		f = new File(dirBase + File.separator + "in" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should be removed from the in folder.");

		logger.info("*** Step 15 Expected Results: - HL7 file is stored in archive directory");
		f = new File(dirBase + File.separator + "archive" + File.separator + fm.getFileName());
		Assert.assertTrue(isFileExists(f, 5), "        HL7 file: " + fm.getFileName() + " should be in the archive folder.");

		logger.info("*** Step 15 Expected Results: - HL7 file is not in err directory");
		f = new File(dirBase + File.separator + "err" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should not be in the err folder.");

		logger.info("*** Step 15 Expected Results: - HL7 file is not in dup directory");
		f = new File(dirBase + File.separator + "dup" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists(), "        HL7 file: " + fm.getFileName() + " should not be in the dup folder.");

		logger.info("*** Step 16 Actions: - Navigate to Task Scheduler");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		logger.info("*** Step 16 Expected Results: - The Task Scheduler page displays");
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		logger.info("*** Step 17 Actions: - Set Task Scheduler for OE Posting Engine");
		taskScheduler.setTaskScheduler("1", "now", 10);

		logger.info("*** Step 17 Expected Results: - Task Status for OE Posting Engine is active");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive("OE Posting Engine"));

		logger.info("*** Step 17 Expected Results: - Ensure Task Status for OE Posting Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));

		logger.info("*** Step 17 Exepected Results: The new accession is in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1, "       Accession: " + accnId + " should be in Q_FR_Pending.");

    	logger.info("*** Step 17 Exepected Results: Both old Test and the new Test should be on the Accession");
    	ArrayList<String> testAbbrevList = daoManagerXifinRpm.getTestAbbrevFromACCNTESTByAccnId(accnId, testDb);

    	ArrayList<String> expectedTestAbbrevList = new ArrayList<String>();
    	expectedTestAbbrevList.add(testAbbrev1);
    	expectedTestAbbrevList.add(testAbbrev2);

    	listUtil = new ListUtil();
    	Assert.assertTrue(listUtil.compareLists(testAbbrevList, expectedTestAbbrevList), "        Both Tests " + testAbbrev1 + " and " + testAbbrev2 + " should be added to the Accession " + accnId);
	}

}
