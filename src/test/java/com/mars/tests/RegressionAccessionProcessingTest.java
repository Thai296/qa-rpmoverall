package com.mars.tests;


// import java.io.File;
// import java.net.URL;

import com.overall.accession.accessionNavigation.AccessionNavigation;
import com.overall.accession.accessionProcessing.AccessionDemographics;
import com.overall.accession.accessionProcessing.AccessionDiagnosis;
import com.overall.accession.accessionProcessing.AccessionSearch;
import com.overall.accession.accessionProcessing.AccessionSearchResults;
import com.overall.accession.accessionProcessing.AccessionTransactionDetail;
import com.overall.accession.accessionProcessing.DiagnosisCodeSearch;
import com.overall.accession.accessionProcessing.DiagnosisCodeSearchResults;
import com.overall.accession.accessionProcessing.superSearch.SuperSearch;
import com.overall.accession.accessionProcessing.superSearch.SuperSearchResults;
import com.overall.client.clientNavigation.ClientNavigation;
import com.overall.client.clientProcessing.EligCensusConfig;
import com.overall.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
import com.overall.fileMaintenance.sysMgt.DataCacheConfiguration;
import com.overall.fileMaintenance.sysMgt.TaskScheduler;
import com.overall.fileMaintenance.sysMgt.TaskStatus;
import com.overall.headerNavigation.HeaderNavigation;

import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.ArrayList;

// import java.util.List;
// import org.openqa.selenium.interactions.Actions;
//import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
// import com.rpm.accession.ErrorProcessing.EPSearch;
// import com.rpm.accession.ErrorProcessing.EPSearchResults;
///import com.rpm.accession.accessionProcessing.AccessionSingleStatement;
// import com.rpm.client.clientProcessing.ClientDemographics;
// import com.rpm.fileMaintenance.orderProcessingConfig.FileMaintencePatternDefinition;
// import com.rpm.help.Help;
// import com.rpm.accession.orderProcessing.AccnTestUpdateOld;
// import com.rpm.payor.payorDemographics.GroupDemographics;
// import com.rpm.payor.payorDemographics.PricingConfig;
// import com.rpm.payor.payorNavigation.PayorNavigation;
// import com.xifin.patientportal.dao.DaoManagerPatientPortal;



public class RegressionAccessionProcessingTest extends SeleniumBaseTest  {
	
	private SsoLogin ssoLogin;
	private HeaderNavigation headerNavigation;
//	private Help help;
	private AccessionNavigation accessionNavigation;
	private SuperSearch superSearch;
	private SuperSearchResults superSearchResults;
	private AccessionDemographics accessionDemographics;
//	private AccessionSingleStatement accessionStatement;
	private AccessionTransactionDetail accessionTransactionDetail;
	private AccessionSearch accessionSearch;
//	private AccnTestUpdateOld accnTestUpdate;
	private TestDataSetup testDataSetup;
	private FileMaintenanceNavigation fileMaintenaceNavigation;
//	private PayorNavigation payorNavigation;
//	private GroupDemographics groupDemographics;
//	private PricingConfig pricingConfig;
	private DataCacheConfiguration dataCacheConfiguration;
	private TaskScheduler taskScheduler;
	private TaskStatus taskStatus;
//	private FileMaintencePatternDefinition fileMaintencePatternDefinition; 
	private TimeStamp timeStamp;
	private AccessionSearchResults accessionSearchResults;
	private AccessionDiagnosis accessionDiagnosis;
	private DiagnosisCodeSearch diagnosisCodeSearch;
	private DiagnosisCodeSearchResults diagnosisCodeSearchResults;

//	private ClientDemographics clientDemographics;
	private ClientNavigation clientNavigation;
	private EligCensusConfig eligCensusConfig;
//	private Actions actions;
	private RandomCharacter randomCharacter;
	
	
	
	@Test(priority = 1, description = "Accession Processing - Super Search - Submit Button Disabled")
	@Parameters({"email", "password"})
	public void testRPM_396(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_396 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Accession - Super Search page");			
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		accessionNavigation.navigateToSuperSearch();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - The Accession - Super Search page displays");		
		superSearch = new SuperSearch(driver);		
		Assert.assertTrue(isElementPresent(superSearch.searchIdInput(),5));
		
		logger.info("*** Step 3 Actions: - Select in Filter 1: dropdown - Accession option and enter in Value - '*'");				
		superSearch.setFilter1("1", "*");
		superSearch.submitBtn().click();

		//logger.info("*** Step 3 Expected Results: - Page has an overlay with Searching icon");
				
		//logger.info("*** Step 3 Expected Results: - Submit button is unavailable for selection");				
		
		logger.info("*** Step 3 Expected Results: - Results from search open in new window");
		//Switch to the search results popup window
		switchToPopupWin();
		superSearchResults = new SuperSearchResults(driver); 
		Assert.assertTrue(isElementPresent(superSearchResults.clientIdText(),5));
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    }
	
	@Test(priority = 1, description = "Accession Demographics - Search - Submit Button Disabled")
	@Parameters({"email", "password"})
	public void testRPM_398(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_398 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Accession - Demographics page");			
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - The Accession - Demographics page displays");		
		accessionDemographics = new AccessionDemographics(driver);		
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));
		
		logger.info("*** Step 3 Actions: - Next to Accession ID field > Click on magnifying glass");
		accessionDemographics.accnSearchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Accession - Search pop-up window appears");
		accessionSearch = new AccessionSearch(driver);
		Assert.assertTrue(isElementPresent(accessionSearch.accnIdInput(),5));
		
		logger.info("*** Step 4 Actions: - In Accession ID field > Enter '*' and click on the Submit button");	
		accessionSearch.setAccnId("*");
		accessionSearch.searchBtn().click();
		switchToPopupWin();
		
		//logger.info("*** Step 4 Expected Results: - Page has an overlay with Searching icon");
		
		//logger.info("*** Step 4 Expected Results: - Submit button is unavailable for selection");				
		
		logger.info("*** Step 4 Expected Results: - Results from search display in the same pop-up window");
		accessionSearchResults = new AccessionSearchResults(driver);
		Assert.assertTrue(isElementPresent(accessionSearchResults.accnTable(),5));
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    }
	
	@Test(priority = 1, description = "Accession Demographics - Update Accession - Add EPI")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_28(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
    	logger.info("*** Testing - testRPM_28 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		/*
		//Go to File Maintenance page
		logger.info("*** Step 2 - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to System Data Cache page
		logger.info("*** Step 4 - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();
		
		//Clear the Cache
		logger.info("*** Step 5 - Clear all the Cache");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		dataCacheConfiguration.setClearCacheAll(this);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		*/
		//Create accession
		logger.info("*** Step 6 - Send request to create an accession");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null);
    	/*
    	//Ensure accession is in Q_FR_Pending table
    	logger.info("*** Step 7 - Ensure the new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	//Update Q_FR_Pending table so accession can be released
    	logger.info("*** Step 8 - Ensure the new accession in the Q_FR_Pending table can be released");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);
    	
    	//Go to System Data Cache page
    	logger.info("*** Step 9 - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();
		
		//Clear the Cache
		logger.info("*** Step 10 - Clear the Cache");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		dataCacheConfiguration.setClearCacheAll(this);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Task Scheduler
		logger.info("*** Step 11 - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();
		
		//Reset All Task Scheduler Engines
		logger.info("*** Step 12 - Reset All Task Scheduler Engines");
		taskScheduler = new TaskScheduler(driver);
		taskScheduler.resetAllTaskSchedulerConcurrency();

		//Set Task Scheduler for OE Posting Engine
		logger.info("*** Step 13 - Set Task Scheduler for OE Posting Engine");
		taskScheduler.setTaskScheduler("1", "now", 10);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		///Go to Task Status
		logger.info("*** Step 14 - Navigate to Task Status");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		
		//Ensure Task Status for OE Posting Engine is completed
		logger.info("*** Step 15 - Ensure Task Status for OE Posting Engine is completed");
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));
	
		//Wait for Task Scheduler OE Posting Engine to complete then verify accession is in the Q_Price table
		logger.info("*** Step 16 - Ensure the new accession is in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		*/
		//Go to Accession page
		logger.info("*** Step 17 - Navigate to Accession Tab");
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		
		//Get Accession Demographic windowHandle during popup
		logger.info("*** Step 18 - Load the accn data by entering the accnId in the accnId input field and tab");
		String popupHandles = switchToPopupWin("c");
		switchToPopupWin();
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));
		
		//Load data for accnId and switch to the Accession Search popup window
		accessionDemographics.searchAccnId(accnId);
		
		//Verify patient First Name is correct when accession data loads
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));
		
		//Navigate to Create New Patient Pop up 
		logger.info("*** Step 19 - Navigate to Create New Patient popup");
		accessionDemographics.naviateToCreateNewPatient();
		switchToPopupWin();
		
		//Ensure Create New Patient popup displays
		Assert.assertTrue(isElementPresent(accessionDemographics.createNewPatientPopupOkBtn(),5));
		
		//Add EPI to accession
		logger.info("*** Step 20 - Add the epi");
		accessionDemographics.addEpi();
		accessionDemographics.reset();
		
		//Load data for accnId and switch to the Accession Search popup window
		logger.info("*** Step 21 - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);
		
		//Verify EPI is associated to the accession
		Assert.assertEquals(accessionDemographics.epiText().toString(), daoManagerPatientPortal.getEpiByAccnId(accnId, testDb));
    }
	
	@Test(priority = 1, description = "Accession Demographics - Update Accession - Update Payor")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_29(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
    	logger.info("*** Testing - testRPM_29 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to File Maintenance page
		logger.info("*** Step 2 - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Turn off sys setting for price Engine
		logger.info("*** Step 3 - Turn off sys setting for price Engine");
    	Assert.assertEquals(daoManagerXifinRpm.setSysSettingsPriceEngine(testDb), 6);

    	//Turn off sys setting for CBC rule and ensure cbc is off
    	logger.info("*** Step 4 - Turn off sys setting for cbc rule");
    	Assert.assertEquals(daoManagerXifinRpm.setSysSettings(0, 102, testDb), 1);
    	Assert.assertEquals(daoManagerXifinRpm.getSysSettingsDataValueBySettingId(102, testDb), 0);
    	
		//Go to System Data Cache page
		logger.info("*** Step 5 - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();
		
		//Clear the Cache
		logger.info("*** Step 6 - Clear all the Cache");
		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//Create accession
		logger.info("*** Step 7 - Send request to create an accession");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null);
    	
    	//Ensure accession is in Q_FR_Pending table
    	logger.info("*** Step 8 - Ensure the new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	//Update Q_FR_Pending table so accession can be released
    	logger.info("*** Step 9 - Ensure the new accession in the Q_FR_Pending table can be released");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);
    	
    	//Go to System Data Cache page
    	logger.info("*** Step 10 - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();
		
		//Clear the Cache
		logger.info("*** Step 11 - Clear the Cache");

		xifinAdminUtils.clearDataCache();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Task Scheduler
		logger.info("*** Step 12 - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();
		
		//Reset All Task Scheduler Engines
		logger.info("*** Step 13 - Reset All Task Scheduler Engines");
		taskScheduler = new TaskScheduler(driver);
		taskScheduler.resetAllTaskSchedulerConcurrency();

		//Set Task Scheduler for OE Posting Engine
		logger.info("*** Step 14 - Set Task Scheduler for OE Posting Engine");
		taskScheduler.setTaskScheduler("1", "now", 10);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		///Go to Task Status
		logger.info("*** Step 15 - Navigate to Task Status");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		
		//Ensure Task Status for OE Posting Engine is completed
		logger.info("*** Step 16 - Ensure Task Status for OE Posting Engine is completed");
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));
	}
	
	
	@Test(priority = 1, description = "Transaction Detail - Update Accession - Add Dx Code")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_30(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
    	logger.info("*** Testing - testRPM_30 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to File Maintenance page
		logger.info("*** Step 2 - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Turn off sys setting for price Engine
		logger.info("*** Step 3 - Turn off sys setting for price Engine");
    	Assert.assertEquals(daoManagerXifinRpm.setSysSettingsPriceEngine(testDb), 6);
    	
		//Go to System Data Cache page
		logger.info("*** Step 5 - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();
		
		//Clear the Cache
		logger.info("*** Step 6 - Clear all the Cache");
		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//Create accession
		logger.info("*** Step 7 - Send request to create an accession");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null);
    	
    	//Ensure accession is in Q_FR_Pending table
    	logger.info("*** Step 8 - Ensure the new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	//Update Q_FR_Pending table so accession can be released
    	logger.info("*** Step 9 - Ensure the new accession in the Q_FR_Pending table can be released");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);
    	
    	//Go to System Data Cache page
    	logger.info("*** Step 10 - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();
		
		//Clear the Cache
		logger.info("*** Step 11 - Clear the Cache");

		xifinAdminUtils.clearDataCache();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Task Scheduler
		logger.info("*** Step 12 - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();
		
		//Reset All Task Scheduler Engines
		logger.info("*** Step 13 - Reset All Task Scheduler Engines");
		taskScheduler = new TaskScheduler(driver);
		taskScheduler.resetAllTaskSchedulerConcurrency();

		//Set Task Scheduler for OE Posting Engine
		logger.info("*** Step 14 - Set Task Scheduler for OE Posting Engine");
		taskScheduler.setTaskScheduler("1", "now", 10);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		///Go to Task Status
		logger.info("*** Step 15 - Navigate to Task Status");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		
		//Ensure Task Status for OE Posting Engine is completed
		logger.info("*** Step 16 - Ensure Task Status for OE Posting Engine is completed");
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));
	
		//Wait for Task Scheduler OE Posting Engine to complete then verify accession is in the Q_Price table
		logger.info("*** Step 17 - Ensure the new accession is in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Accession page
		logger.info("*** Step 18 - Navigate to Accession Tab");
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Transaction Detail page
		logger.info("*** Step 19 - Navigate to Transaction Detail");
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToTransactionDetailLink();
		switchToPopupWin();
		
		//Load the accn data
		logger.info("*** Step 20 - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(),5));
		accessionTransactionDetail.searchAccnId(accnId);
		
		//Verify patient First Name is correct when accession data loads
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.ptFNameInput(),5));
		Assert.assertEquals(accessionTransactionDetail.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));
		
		//Get Random Diagnosis code
		String dxCode = daoManagerAccnWS.getDiagnosisCode(testDb);
		
		//Add Diagnosis Code to the Accession
		logger.info("*** Step 21 - Add Diagnosis Code to the Accession");
		accessionTransactionDetail.addDiagnosisCode(dxCode, "QATetser");
		
		//Load the accn data
		logger.info("*** Step 22 - Load the accn data by entering the accnId in the accnId input field and tab");
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(),5));
		accessionTransactionDetail.searchAccnId(accnId);
		
		//Verify the Diagnosis code is associated to the accession
		Assert.assertEquals(accessionTransactionDetail.dXCodeText(dxCode).getAttribute("value"), daoManagerAccnWS.getDiagnosisCodeByAccnIdUserDiagOrder(accnId, 2, testDb));
    }
	
	@Test(priority = 1, description = "Transaction Detail - Update Accession - Add Modifer")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "mod"})
	public void testRPM_31(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String mod) throws Exception {
    	logger.info("*** Testing - testRPM_30 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to File Maintenance page
		logger.info("*** Step 2 - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Turn off sys setting for price Engine
		logger.info("*** Step 3 - Turn off sys setting for price Engine");
    	Assert.assertEquals(daoManagerXifinRpm.setSysSettingsPriceEngine(testDb), 6);
    	
		//Go to System Data Cache page
		logger.info("*** Step 5 - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();
		
		//Clear the Cache
		logger.info("*** Step 6 - Clear all the Cache");
		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//Create accession
		logger.info("*** Step 7 - Send request to create an accession");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null);
    	
    	//Ensure accession is in Q_FR_Pending table
    	logger.info("*** Step 8 - Ensure the new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	//Update Q_FR_Pending table so accession can be released
    	logger.info("*** Step 9 - Ensure the new accession in the Q_FR_Pending table can be released");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);
    	
    	//Go to System Data Cache page
    	logger.info("*** Step 10 - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();
		
		//Clear the Cache
		logger.info("*** Step 11 - Clear the Cache");

		xifinAdminUtils.clearDataCache();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Task Scheduler
		logger.info("*** Step 12 - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();
		
		//Reset All Task Scheduler Engines
		logger.info("*** Step 13 - Reset All Task Scheduler Engines");
		taskScheduler = new TaskScheduler(driver);
		taskScheduler.resetAllTaskSchedulerConcurrency();

		//Set Task Scheduler for OE Posting Engine
		logger.info("*** Step 14 - Set Task Scheduler for OE Posting Engine");
		taskScheduler.setTaskScheduler("1", "now", 10);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		///Go to Task Status
		logger.info("*** Step 15 - Navigate to Task Status");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		
		//Ensure Task Status for OE Posting Engine is completed
		logger.info("*** Step 16 - Ensure Task Status for OE Posting Engine is completed");
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));
	
		//Wait for Task Scheduler OE Posting Engine to complete then verify accession is in the Q_Price table
		logger.info("*** Step 17 - Ensure the new accession is in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Accession page
		logger.info("*** Step 18 - Navigate to Accession Tab");
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Transaction Detail page
		logger.info("*** Step 19 - Navigate to Transaction Detail");
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToTransactionDetailLink();
		switchToPopupWin();
		
		//Load the accn data
		logger.info("*** Step 20 - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(),5));
		accessionTransactionDetail.searchAccnId(accnId);
		
		//Verify patient First Name is correct when accession data loads
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.ptFNameInput(),5));
		Assert.assertEquals(accessionTransactionDetail.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));
		
		//Add Modifier to ordered Test Details
		logger.info("*** Step 21 - Add Modifier for the ordered Test Details to the Accession");
		accessionTransactionDetail.selectOrderTestDetailMod(this, mod);
		
		//Load the accn data
		logger.info("*** Step 22 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(),5));
		accessionTransactionDetail.searchAccnId(accnId);
		
		logger.info("*** Step 22 Expected Results: - Verify the Modifier is associated to the accession");		
		Assert.assertEquals(accessionTransactionDetail.orderTestDetailsMod1Text().getAttribute("value"), daoManagerXifinRpm.getMod1IdFromAccnTestByAccnId(accnId, testDb));

    }
	
	@Test(priority = 1, description = "Transaction Detail - Billable Procedure Code - Active adj codes display")
	@Parameters({"email", "password"})
	public void testRPM_33(String email, String password) throws Exception {
    	logger.info("*** Testing - testRPM_33 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Accession page
		logger.info("*** Step 2 - Navigate to Accession Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Transaction Detail page
		logger.info("*** Step 3 - Navigate to Transaction Detail");
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToTransactionDetailLink();
		switchToPopupWin();
		
		//Load the accn data
		logger.info("*** Step 4 - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(),5));
		
		//Get random accnId
		String accnId = daoManagerXifinRpm.getPricedAccession(testDb);
		accessionTransactionDetail.searchAccnId(accnId);
		
		//Ensure items in Billable Procedure Code Details dropdown matches active adj codes in the db
		logger.info("*** Step 5 - Get inactive adj codes in the db");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getAdjAbbrvFromAdjCdByBDeletedBSysAdded(0, 0, testDb), accessionTransactionDetail.adjCodeDropdown(), false));
    }
	
	@Test(priority = 1, description = "Transaction Detail - Bulk Payments - Active adj codes display")
	@Parameters({"email", "password"})
	public void testRPM_34(String email, String password) throws Exception {
    	logger.info("*** Testing - testRPM_34 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Accession page
		logger.info("*** Step 2 - Navigate to Accession Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Transaction Detail page
		logger.info("*** Step 3 - Navigate to Transaction Detail");
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToTransactionDetailLink();
		switchToPopupWin();
		
		//Load the accn data
		logger.info("*** Step 4 - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(),5));
		
		//Get random accnId
		String accnId = daoManagerXifinRpm.getPricedAccession(testDb);
		accessionTransactionDetail.searchAccnId(accnId);
		
		//Ensure items in Bulk Payments Code Details dropdown matches active adj codes in the db
		logger.info("*** Step 5 - Get active adj codes in the db");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getAdjAbbrvFromAdjCdByBDeletedBSysAdded(0, 0, testDb), accessionTransactionDetail.adjCodeBulkDropdown(), false));
    }
	
	@Test(priority = 1, description = "Transaction Detail - Billable Procedure Code - Inactive adj codes don't display")
	@Parameters({"email", "password"})
	public void testRPM_35(String email, String password) throws Exception {
    	logger.info("*** Testing - testRPM_35 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Accession page
		logger.info("*** Step 2 - Navigate to Accession Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Transaction Detail page
		logger.info("*** Step 3 - Navigate to Transaction Detail");
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToTransactionDetailLink();
		switchToPopupWin();
		
		//Load the accn data
		logger.info("*** Step 4 - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(),5));
		
		//Get random accnId
		String accnId = daoManagerXifinRpm.getPricedAccession(testDb);
		accessionTransactionDetail.searchAccnId(accnId);
		
		//Ensure items in Billable Procedure Code Details dropdown do not matches inactive items in the db
		logger.info("*** Step 5 - Get inactive adj codes in the db");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertTrue(compareDropdownInverseItems(daoManagerXifinRpm.getAdjAbbrvFromAdjCdByBDeletedBSysAdded(0, 1, testDb), accessionTransactionDetail.adjCodeDropdown()));
    }
	
	@Test(priority = 1, description = "Transaction Detail - Bulk Payments - Inactive adj codes don't display")
	@Parameters({"email", "password"})
	public void testRPM_36(String email, String password) throws Exception {
    	logger.info("*** Testing - testRPM_36 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Accession page
		logger.info("*** Step 2 - Navigate to Accession Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Transaction Detail page
		logger.info("*** Step 3 - Navigate to Transaction Detail");
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToTransactionDetailLink();
		switchToPopupWin();
		
		//Load the accn data
		logger.info("*** Step 4 - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(),5));
		
		//Get random accnId
		String accnId = daoManagerXifinRpm.getPricedAccession(testDb);
		accessionTransactionDetail.searchAccnId(accnId);
		
		//Ensure items in Bulk Payments Code Details dropdown do not match inactive adj codes in the db
		logger.info("*** Step 5 - Get inactive adj codes in the db");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertTrue(compareDropdownInverseItems(daoManagerXifinRpm.getAdjAbbrvFromAdjCdByBDeletedBSysAdded(0, 1, testDb), accessionTransactionDetail.adjCodeBulkDropdown()));
    }
	
	@Test(priority = 1, description = "Transaction Detail - Billable Procedure Code - Reactive adj codes display")
	@Parameters({"email", "password"})
	public void testRPM_37(String email, String password) throws Exception {
    	logger.info("*** Testing - testRPM_37 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Accession page
		logger.info("*** Step 2 - Navigate to Accession Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Transaction Detail page
		logger.info("*** Step 3 - Navigate to Transaction Detail");
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToTransactionDetailLink();
		switchToPopupWin();
		
		//Load the accn data
		logger.info("*** Step 4 - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(),5));
		
		//Get random accnId
		String accnId = daoManagerXifinRpm.getPricedAccession(testDb);
		accessionTransactionDetail.searchAccnId(accnId);
		
		//Reactive the adj code in the db
		logger.info("*** Step 5 - Reactive the adj code in the db");
		Assert.assertEquals(daoManagerXifinRpm.setAdjCodeFromAdjCdByAdjCdIdBDeletedBSysAdded(0, 0, 100, testDb), 1);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to File Maintenance Tab
    	logger.info("*** Step 6 - Navigate to File Maintenance Page");
    	headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    	
    	//Go to System Data Cache page
    	logger.info("*** Step 7 - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();
		
		//Clear the Cache
		logger.info("*** Step 8 - Clear the Cache");
		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 9 - Navigate to the Accession Tab");
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Transaction Detail page
		logger.info("*** Step 10 - Navigate to Transaction Detail");
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToTransactionDetailLink();
		switchToPopupWin();
		
		//Load the accn data
		logger.info("*** Step 11 - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(),5));
		accessionTransactionDetail.searchAccnId(accnId);
		
		//Ensure items in Billable Procedure Codee Details dropdown do not match inactive adj codes in the db
		logger.info("*** Step 12 - Get active adj codes in the db");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getAdjAbbrvFromAdjCdByBDeletedBSysAdded(0, 0, testDb), accessionTransactionDetail.adjCodeDropdown(), false));
		
		//Set the Adj code back 
		logger.info("*** Step 13 - Set the adj code back to inactive in the db");
		int reset = daoManagerXifinRpm.setAdjCodeFromAdjCdByAdjCdIdBDeletedBSysAdded(0, 1, 100, testDb);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to File Maintenance Tab
    	logger.info("*** Step 14 - Navigate to File Maintenance Page");
    	headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    	
    	//Go to System Data Cache page
    	logger.info("*** Step 15 - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();
		
		//Clear the Cache
		logger.info("*** Step 16 - Clear the Cache");

		xifinAdminUtils.clearDataCache();
    }
	
	@Test(priority = 1, description = "Transaction Detail - Bulkd Payments - Reactive adj codes display")
	@Parameters({"email", "password"})
	public void testRPM_48(String email, String password) throws Exception {
    	logger.info("*** Testing - testRPM_48 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Accession page
		logger.info("*** Step 2 - Navigate to Accession Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Transaction Detail page
		logger.info("*** Step 3 - Navigate to Transaction Detail");
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToTransactionDetailLink();
		switchToPopupWin();
		
		//Load the accn data
		logger.info("*** Step 4 - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(),5));
		
		//Get random accnId
		String accnId = daoManagerXifinRpm.getPricedAccession(testDb);
		accessionTransactionDetail.searchAccnId(accnId);
		
		//Reactive the adj code in the db
		logger.info("*** Step 5 - Reactive the adj code in the db");
		Assert.assertEquals(daoManagerXifinRpm.setAdjCodeFromAdjCdByAdjCdIdBDeletedBSysAdded(0, 0, 100, testDb), 1);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to File Maintenance Tab
    	logger.info("*** Step 6 - Navigate to File Maintenance Page");
    	headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    	
    	//Go to System Data Cache page
    	logger.info("*** Step 7 - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();
		
		//Clear the Cache
		logger.info("*** Step 8 - Clear the Cache");
		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 9 - Navigate to the Accession Tab");
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Transaction Detail page
		logger.info("*** Step 10 - Navigate to Transaction Detail");
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToTransactionDetailLink();
		switchToPopupWin();
		
		//Load the accn data
		logger.info("*** Step 11 - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(),5));
		accessionTransactionDetail.searchAccnId(accnId);
		
		//Ensure items in Bulk Payments Codee Details dropdown do not match inactive adj codes in the db
		logger.info("*** Step 12 - Get active adj codes in the db");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getAdjAbbrvFromAdjCdByBDeletedBSysAdded(0, 0, testDb), accessionTransactionDetail.adjCodeBulkDropdown(), false));
		
		//Set the Adj code back 
		logger.info("*** Step 13 - Set the adj code back to inactive in the db");
		int reset = daoManagerXifinRpm.setAdjCodeFromAdjCdByAdjCdIdBDeletedBSysAdded(0, 1, 100, testDb);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to File Maintenance Tab
    	logger.info("*** Step 14 - Navigate to File Maintenance Page");
    	headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    	
    	//Go to System Data Cache page
    	logger.info("*** Step 15 - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();
		
		//Clear the Cache
		logger.info("*** Step 16 - Clear the Cache");

		xifinAdminUtils.clearDataCache();
    }
	
	@Test(priority = 1, description = "Accession Demographics - Reason Code FxDate - Submit Accn unfixed WELLEXAM")
	@Parameters({"email", "password"})
	public void testRPM_65(String email, String password) throws Exception {
    	logger.info("*** Testing - testRPM_65 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to File Maintenance page
		logger.info("*** Step 2 - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Accession page
		logger.info("*** Step 3 - Navigate to Accession Tab");
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		
		//Get Accession Demographic windowHandle during popup
		logger.info("*** Step 4 - Load the accn data by entering the accnId in the accnId input field and tab");
		//String popupHandles = switchToPopupWin("c");
		switchToPopupWin();
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));
		
		//Get accnId that has an unfixed 'WELLEXAM' error
		String accnId = daoManagerXifinRpm.getAccnIdFromAccnPyrErrByErrCdFixDt(1112, "null", testDb);
		
		//Load data for accnId and switch to the Accession Search popup window
		accessionDemographics.searchAccnId(accnId);
		Assert.assertEquals(accessionDemographics.accnErrorReasonCodeText("WELLEXAM-Well exam").getAttribute("value"), "WELLEXAM-Well exam");
    }
	
	@Test(priority = 1, description = "Accession Demographics - Reason Code FxDate - Submit Accn fixed WELLEXAM")
	@Parameters({"email", "password"})
	public void testRPM_66(String email, String password) throws Exception {
    	logger.info("*** Testing - testRPM_66 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to File Maintenance page
		logger.info("*** Step 2 - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Accession page
		logger.info("*** Step 3 - Navigate to Accession Tab");
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		
		//Get Accession Demographic windowHandle during popup
		logger.info("*** Step 4 - Load the accn data by entering the accnId in the accnId input field and tab");
		//String popupHandles = switchToPopupWin("c");
		switchToPopupWin();
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));
		
		//Get accnId that has an fixed 'WELLEXAM' error
		String accnId = daoManagerXifinRpm.getAccnIdFromAccnPyrErrByErrCdFixDt(1112, "not null", testDb);
		
		//Load data for accnId and switch to the Accession Search popup window
		accessionDemographics.searchAccnId(accnId);
		Assert.assertTrue(isElementNotPresent(accessionDemographics.accnErrorReasonCodeText("WELLEXAM-Well exam"), 5));
    }
	
	@Test(priority = 1, description = "Accession Demographic - Accn Status")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_448(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_448 *****");    	
		
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
		/*
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the file maintenance page displays");
		//Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("File Maintenance - Exam Code "), "        File Maintenance - Exam Code  page title should show.");
		
		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
		
		logger.info("*** Step 4 Actions: - Clear all the Cache");
		dataCacheConfiguration.setClearCacheAll(this);
		
		logger.info("*** Step 4 Expected Results: - Verify the cache is cleared");
		int rowNum = dataCacheConfiguration.getRowNumberInDataCacheTable(dataCacheConfiguration.dataCacheConfigTable(), "ADJ_CD");
		Assert.assertFalse(dataCacheConfiguration.reloadNowText(rowNum).isSelected(), "        The Reload checkbox for ADJ_CD should be unchecked.");
		
		logger.info("*** Step 5 Actions: - Send request to create an accession");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, configuration.getUrl(UrlTyp.ACCN_WS).toString());
    	logger.info("AccnID: " + accnId);
    	
    	logger.info("*** Step 5 Expected Results: The accession gets created");
    	Assert.assertTrue(accnId != null);
    	
    	logger.info("*** Step 5 Expected Results: - The new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	logger.info("*** Step 6 Actions: - Release the new accession in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);	
    	
    	logger.info("*** Step 6 Exepected Results: The new accession in the Q_FR_Pending table is set");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnIdHoldRuleBReleaseBOEPerformed(accnId, testDb), 1);
		*/
    	logger.info("*** Step 7 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 7 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 8 Actions: - Navigate to Demographics");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();
		
		logger.info("*** Step 8 Expected Results: - The Demographics page displays");
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "       Accession Demographics page title should show.");

		logger.info("*** Step 9 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		//String accnId = daoManagerXifinRpm.getAccnIdWPtSexFromACCN(testDb);

		ArrayList<String> accnList = daoManagerAccnWS.getPricedThirdPartyAccnWOErrorFromACCNAndACCNPYR(testDb);
		String accnId = accnList.get(0);
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 9 Expected Results: - Verify that the accn status is correct");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));		
		Assert.assertEquals(accessionDemographics.accnStatIdInput().getAttribute("value"), daoManagerXifinRpm.getStatusAbbrevByAccnIdStatusId(accnId, testDb));
	}
	
	@Test(priority = 1, description = "Accession Demographic - ClientId")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_449(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_449 *****");    	
		
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
		/*
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the file maintenance page displays");
		//Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("File Maintenance - Exam Code "), "        File Maintenance - Exam Code  page title should show.");
		
		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
		
		logger.info("*** Step 4 Actions: - Clear all the Cache");
		dataCacheConfiguration.setClearCacheAll(this);
		
		logger.info("*** Step 4 Expected Results: - Verify the cache is cleared");
		int rowNum = dataCacheConfiguration.getRowNumberInDataCacheTable(dataCacheConfiguration.dataCacheConfigTable(), "ADJ_CD");
		Assert.assertFalse(dataCacheConfiguration.reloadNowText(rowNum).isSelected(), "        The Reload checkbox for ADJ_CD should be unchecked.");
		
		logger.info("*** Step 5 Actions: - Send request to create an accession");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, configuration.getUrl(UrlTyp.ACCN_WS).toString());
    	logger.info("AccnID: " + accnId);
    	
    	logger.info("*** Step 5 Expected Results: The accession gets created");
    	Assert.assertTrue(accnId != null);
    	
    	logger.info("*** Step 5 Expected Results: - The new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	logger.info("*** Step 6 Actions: - Release the new accession in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);	
    	
    	logger.info("*** Step 6 Exepected Results: The new accession in the Q_FR_Pending table is set");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnIdHoldRuleBReleaseBOEPerformed(accnId, testDb), 1);
		*/
    	logger.info("*** Step 7 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 7 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 8 Actions: - Navigate to Demographics");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();
		
		logger.info("*** Step 8 Expected Results: - The Demographics page displays");
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "       Accession Demographics page title should show.");

		logger.info("*** Step 9 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		String accnId = daoManagerXifinRpm.getAccnIdWPtSexFromACCN(testDb);
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 9 Expected Results: - Verify that the clientId is correct");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.clnAbbrvInput().getAttribute("value"), daoManagerXifinRpm.getClientAbbrevIdByAccnId(accnId, testDb));
	}
	
	@Test(priority = 1, description = "Accession Demographic - Patient Address 1")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_450(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_450 *****");    	
		
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
		//headerNavigation.navigateToFileMaintenanceTab();
		//switchToPopupWin();	
		
		/*
		logger.info("*** Step 2 Expected Results: - Verify that the file maintenance page displays");
		//Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("File Maintenance - Exam Code "), "        File Maintenance - Exam Code  page title should show.");
		
		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
		
		logger.info("*** Step 4 Actions: - Clear all the Cache");
		dataCacheConfiguration.setClearCacheAll(this);
		
		logger.info("*** Step 4 Expected Results: - Verify the cache is cleared");
		int rowNum = dataCacheConfiguration.getRowNumberInDataCacheTable(dataCacheConfiguration.dataCacheConfigTable(), "ADJ_CD");
		Assert.assertFalse(dataCacheConfiguration.reloadNowText(rowNum).isSelected(), "        The Reload checkbox for ADJ_CD should be unchecked.");
		
		logger.info("*** Step 5 Actions: - Send request to create an accession");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, configuration.getUrl(UrlTyp.ACCN_WS).toString());
    	logger.info("AccnID: " + accnId);
    	
    	logger.info("*** Step 5 Expected Results: The accession gets created");
    	Assert.assertTrue(accnId != null);
    	
    	logger.info("*** Step 5 Expected Results: - The new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	logger.info("*** Step 6 Actions: - Release the new accession in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);	
    	
    	logger.info("*** Step 6 Exepected Results: The new accession in the Q_FR_Pending table is set");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnIdHoldRuleBReleaseBOEPerformed(accnId, testDb), 1);
    	*/
		String accnId = daoManagerXifinRpm.getAccnIdWPtAddr1FromACCN(testDb);
		
    	logger.info("*** Step 7 Actions: - Navigate to Accession Tab");
		//headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 7 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 8 Actions: - Navigate to Demographics");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();
		
		logger.info("*** Step 8 Expected Results: - The Demographics page displays");
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "       Accession Demographics page title should show.");

		logger.info("*** Step 9 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 9 Expected Results: - Verify that the patient address 1 is correct");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));		
		Assert.assertEquals(accessionDemographics.ptAddr1Input().getAttribute("value"), daoManagerPatientPortal.getAddr1FromAccnByAccnId(accnId, testDb));
		
		accessionDemographics.reset();
	}
	
	@Test(priority = 1, description = "Accession Demographic - Patient Gender")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_451(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_451 *****");    	
		
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
		/*
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the file maintenance page displays");
		//Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("File Maintenance - Exam Code "), "        File Maintenance - Exam Code  page title should show.");
		
		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
		
		logger.info("*** Step 4 Actions: - Clear all the Cache");
		dataCacheConfiguration.setClearCacheAll(this);
		
		logger.info("*** Step 4 Expected Results: - Verify the cache is cleared");
		int rowNum = dataCacheConfiguration.getRowNumberInDataCacheTable(dataCacheConfiguration.dataCacheConfigTable(), "ADJ_CD");
		Assert.assertFalse(dataCacheConfiguration.reloadNowText(rowNum).isSelected(), "        The Reload checkbox for ADJ_CD should be unchecked.");
		
		logger.info("*** Step 5 Actions: - Send request to create an accession");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, configuration.getUrl(UrlTyp.ACCN_WS).toString());
    	logger.info("AccnID: " + accnId);
    	
    	logger.info("*** Step 5 Expected Results: The accession gets created");
    	Assert.assertTrue(accnId != null);
    	
    	logger.info("*** Step 5 Expected Results: - The new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	logger.info("*** Step 6 Actions: - Release the new accession in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);	
    	
    	logger.info("*** Step 6 Exepected Results: The new accession in the Q_FR_Pending table is set");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnIdHoldRuleBReleaseBOEPerformed(accnId, testDb), 1);
		*/
    	logger.info("*** Step 7 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 7 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 8 Actions: - Navigate to Demographics");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();
		
		logger.info("*** Step 8 Expected Results: - The Demographics page displays");
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "       Accession Demographics page title should show.");

		logger.info("*** Step 9 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		String accnId = daoManagerXifinRpm.getAccnIdWPtSexFromACCN(testDb);
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 9 Expected Results: - Verify that the patient gender is correct");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));	
		Assert.assertTrue(isDropdownItemSelected(accessionDemographics.ptSexDropdown(), daoManagerPatientPortal.getGenderFromGenderTypByAccnId(accnId, testDb)));
	}
	
	@Test(priority = 1, description = "Accession Demographic - Patient zip code")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_452(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_452 *****");    	
		
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
		/*
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the file maintenance page displays");
		//Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("File Maintenance - Exam Code "), "        File Maintenance - Exam Code  page title should show.");
		
		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
		
		logger.info("*** Step 4 Actions: - Clear all the Cache");
		dataCacheConfiguration.setClearCacheAll(this);
		
		logger.info("*** Step 4 Expected Results: - Verify the cache is cleared");
		int rowNum = dataCacheConfiguration.getRowNumberInDataCacheTable(dataCacheConfiguration.dataCacheConfigTable(), "ADJ_CD");
		Assert.assertFalse(dataCacheConfiguration.reloadNowText(rowNum).isSelected(), "        The Reload checkbox for ADJ_CD should be unchecked.");
		
		logger.info("*** Step 5 Actions: - Send request to create an accession");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, configuration.getUrl(UrlTyp.ACCN_WS).toString());
    	logger.info("AccnID: " + accnId);
    	
    	logger.info("*** Step 5 Expected Results: The accession gets created");
    	Assert.assertTrue(accnId != null);
    	
    	logger.info("*** Step 5 Expected Results: - The new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	logger.info("*** Step 6 Actions: - Release the new accession in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);	
    	
    	logger.info("*** Step 6 Exepected Results: The new accession in the Q_FR_Pending table is set");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnIdHoldRuleBReleaseBOEPerformed(accnId, testDb), 1);
		*/
    	logger.info("*** Step 7 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 7 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 8 Actions: - Navigate to Demographics");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();
		
		logger.info("*** Step 8 Expected Results: - The Demographics page displays");
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "       Accession Demographics page title should show.");

		logger.info("*** Step 9 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		String accnId = daoManagerXifinRpm.getAccnIdWPtAddr1FromACCN(testDb);
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 9 Expected Results: - Verify that the patient zip code is correct");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));		
		Assert.assertEquals(accessionDemographics.ptZipInput().getAttribute("value"), daoManagerPatientPortal.getZipByAccnId(accnId, testDb));
	}
	
	@Test(priority = 1, description = "Accession Demographic - Patient Ordering NPI")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_453(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_453 *****");    	
		
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
		/*
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the file maintenance page displays");
		//Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("File Maintenance - Exam Code "), "        File Maintenance - Exam Code  page title should show.");
		
		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
		
		logger.info("*** Step 4 Actions: - Clear all the Cache");
		dataCacheConfiguration.setClearCacheAll(this);
		
		logger.info("*** Step 4 Expected Results: - Verify the cache is cleared");
		int rowNum = dataCacheConfiguration.getRowNumberInDataCacheTable(dataCacheConfiguration.dataCacheConfigTable(), "ADJ_CD");
		Assert.assertFalse(dataCacheConfiguration.reloadNowText(rowNum).isSelected(), "        The Reload checkbox for ADJ_CD should be unchecked.");
		
		logger.info("*** Step 5 Actions: - Send request to create an accession");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, configuration.getUrl(UrlTyp.ACCN_WS).toString());
    	logger.info("AccnID: " + accnId);
    	
    	logger.info("*** Step 5 Expected Results: The accession gets created");
    	Assert.assertTrue(accnId != null);
    	
    	logger.info("*** Step 5 Expected Results: - The new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	logger.info("*** Step 6 Actions: - Release the new accession in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);	
    	
    	logger.info("*** Step 6 Exepected Results: The new accession in the Q_FR_Pending table is set");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnIdHoldRuleBReleaseBOEPerformed(accnId, testDb), 1);
		*/
    	logger.info("*** Step 7 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 7 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 8 Actions: - Navigate to Demographics");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();
		
		logger.info("*** Step 8 Expected Results: - The Demographics page displays");
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "       Accession Demographics page title should show.");

		logger.info("*** Step 9 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		String accnId = daoManagerXifinRpm.getAccnIdWOrderingPhysFromACCN(testDb);
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 9 Expected Results: - Verify that the patient NIP is correct");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));		
		Assert.assertEquals(accessionDemographics.orderNpiInput().getAttribute("value"), daoManagerPatientPortal.getNpiFromAccnPhysTypByAccnId(accnId, testDb));
	}
	
	@Test(priority = 1, description = "Accession Demographic - Patient PSC Location ID")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_454(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_454 *****");    	
		
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
		/*
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the file maintenance page displays");
		//Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("File Maintenance - Exam Code "), "        File Maintenance - Exam Code  page title should show.");
		
		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
		
		logger.info("*** Step 4 Actions: - Clear all the Cache");
		dataCacheConfiguration.setClearCacheAll(this);
		
		logger.info("*** Step 4 Expected Results: - Verify the cache is cleared");
		int rowNum = dataCacheConfiguration.getRowNumberInDataCacheTable(dataCacheConfiguration.dataCacheConfigTable(), "ADJ_CD");
		Assert.assertFalse(dataCacheConfiguration.reloadNowText(rowNum).isSelected(), "        The Reload checkbox for ADJ_CD should be unchecked.");
		
		logger.info("*** Step 5 Actions: - Send request to create an accession");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, configuration.getUrl(UrlTyp.ACCN_WS).toString());
    	logger.info("AccnID: " + accnId);
    	
    	logger.info("*** Step 5 Expected Results: The accession gets created");
    	Assert.assertTrue(accnId != null);
    	
    	logger.info("*** Step 5 Expected Results: - The new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	logger.info("*** Step 6 Actions: - Release the new accession in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);	
    	
    	logger.info("*** Step 6 Exepected Results: The new accession in the Q_FR_Pending table is set");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnIdHoldRuleBReleaseBOEPerformed(accnId, testDb), 1);
		*/
    	logger.info("*** Step 7 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 7 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 8 Actions: - Navigate to Demographics");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();
		
		logger.info("*** Step 8 Expected Results: - The Demographics page displays");
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "       Accession Demographics page title should show.");

		logger.info("*** Step 9 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		String accnId = daoManagerXifinRpm.getAccnIdWPSCFromACCN(testDb);
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 9 Expected Results: - Verify that the patient PSC Location ID is correct");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));		
		Assert.assertEquals(accessionDemographics.phlebFacAbbrvInput().getAttribute("value"), daoManagerPatientPortal.getFacultyAbbrevFromFacByAccnId(accnId, testDb));
	}
	
	@Test(priority = 1, description = "Accession Demographic - Patient PSC Location Name")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_455(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_455 *****");    	
		
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
		/*
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the file maintenance page displays");
		//Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("File Maintenance - Exam Code "), "        File Maintenance - Exam Code  page title should show.");
		
		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
		
		logger.info("*** Step 4 Actions: - Clear all the Cache");
		dataCacheConfiguration.setClearCacheAll(this);
		
		logger.info("*** Step 4 Expected Results: - Verify the cache is cleared");
		int rowNum = dataCacheConfiguration.getRowNumberInDataCacheTable(dataCacheConfiguration.dataCacheConfigTable(), "ADJ_CD");
		Assert.assertFalse(dataCacheConfiguration.reloadNowText(rowNum).isSelected(), "        The Reload checkbox for ADJ_CD should be unchecked.");
		
		logger.info("*** Step 5 Actions: - Send request to create an accession");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, configuration.getUrl(UrlTyp.ACCN_WS).toString());
    	logger.info("AccnID: " + accnId);
    	
    	logger.info("*** Step 5 Expected Results: The accession gets created");
    	Assert.assertTrue(accnId != null);
    	
    	logger.info("*** Step 5 Expected Results: - The new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	logger.info("*** Step 6 Actions: - Release the new accession in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);	
    	
    	logger.info("*** Step 6 Exepected Results: The new accession in the Q_FR_Pending table is set");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnIdHoldRuleBReleaseBOEPerformed(accnId, testDb), 1);
		*/
    	logger.info("*** Step 7 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 7 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 8 Actions: - Navigate to Demographics");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();
		
		logger.info("*** Step 8 Expected Results: - The Demographics page displays");
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "       Accession Demographics page title should show.");

		logger.info("*** Step 9 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		String accnId = daoManagerXifinRpm.getAccnIdWPSCFromACCN(testDb);
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 9 Expected Results: - Verify that the patient PSC Location Name is correct");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));		
		Assert.assertEquals(accessionDemographics.phlebFacDescrInput().getAttribute("value"), daoManagerPatientPortal.getFacultyNameFromFacByAccnId(accnId, testDb));
	}
	
	@Test(priority = 1, description = "Accession Demographic - Insurance Subscr Id")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_456(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_456 *****");    	
		
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
		/*
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the file maintenance page displays");
		//Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("File Maintenance - Exam Code "), "        File Maintenance - Exam Code  page title should show.");
		
		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
		
		logger.info("*** Step 4 Actions: - Clear all the Cache");
		dataCacheConfiguration.setClearCacheAll(this);
		
		logger.info("*** Step 4 Expected Results: - Verify the cache is cleared");
		int rowNum = dataCacheConfiguration.getRowNumberInDataCacheTable(dataCacheConfiguration.dataCacheConfigTable(), "ADJ_CD");
		Assert.assertFalse(dataCacheConfiguration.reloadNowText(rowNum).isSelected(), "        The Reload checkbox for ADJ_CD should be unchecked.");
		
		logger.info("*** Step 5 Actions: - Send request to create an accession");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, configuration.getUrl(UrlTyp.ACCN_WS).toString());
    	logger.info("AccnID: " + accnId);
    	
    	logger.info("*** Step 5 Expected Results: The accession gets created");
    	Assert.assertTrue(accnId != null);
    	
    	logger.info("*** Step 5 Expected Results: - The new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	logger.info("*** Step 6 Actions: - Release the new accession in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);	
    	
    	logger.info("*** Step 6 Exepected Results: The new accession in the Q_FR_Pending table is set");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnIdHoldRuleBReleaseBOEPerformed(accnId, testDb), 1);
		*/
    	logger.info("*** Step 7 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 7 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 8 Actions: - Navigate to Demographics");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();
		
		logger.info("*** Step 8 Expected Results: - The Demographics page displays");
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "       Accession Demographics page title should show.");

		logger.info("*** Step 9 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		String accnId = daoManagerXifinRpm.getAccnIdWPyrSubsIdFromACCN(testDb);
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 9 Expected Results: - Verify that the insurance subscriber Id is correct");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		//Assert.assertEquals(accessionDemographics.subsIdInput().getAttribute("value"), daoManagerPatientPortal.getSubscrIdFromAccnPyrByAccnId(accnId, testDb));		
		Assert.assertEquals(accessionDemographics.subsIdInput().getAttribute("value"), daoManagerXifinRpm.getSubscrIdFromAccnPyrByAccnIdPyrPrio(accnId, "1", testDb));
		
	}
	
	@Test(priority = 1, description = "Accession Demographic - Insurance Provider Id")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_457(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_457 *****");    	
		
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
		/*
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the file maintenance page displays");
		//Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("File Maintenance - Exam Code "), "        File Maintenance - Exam Code  page title should show.");
		
		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
		
		logger.info("*** Step 4 Actions: - Clear all the Cache");
		dataCacheConfiguration.setClearCacheAll(this);
		
		logger.info("*** Step 4 Expected Results: - Verify the cache is cleared");
		int rowNum = dataCacheConfiguration.getRowNumberInDataCacheTable(dataCacheConfiguration.dataCacheConfigTable(), "ADJ_CD");
		Assert.assertFalse(dataCacheConfiguration.reloadNowText(rowNum).isSelected(), "        The Reload checkbox for ADJ_CD should be unchecked.");
		
		logger.info("*** Step 5 Actions: - Send request to create an accession");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, configuration.getUrl(UrlTyp.ACCN_WS).toString());
    	logger.info("AccnID: " + accnId);
    	
    	logger.info("*** Step 5 Expected Results: The accession gets created");
    	Assert.assertTrue(accnId != null);
    	
    	logger.info("*** Step 5 Expected Results: - The new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	logger.info("*** Step 6 Actions: - Release the new accession in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);	
    	
    	logger.info("*** Step 6 Exepected Results: The new accession in the Q_FR_Pending table is set");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnIdHoldRuleBReleaseBOEPerformed(accnId, testDb), 1);
		*/
    	logger.info("*** Step 7 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 7 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 8 Actions: - Navigate to Demographics");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();
		
		logger.info("*** Step 8 Expected Results: - The Demographics page displays");
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "       Accession Demographics page title should show.");

		logger.info("*** Step 9 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		String accnId = daoManagerXifinRpm.getAccnIdWOrderingPhysFromACCN(testDb);
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 9 Expected Results: - Verify that the insurance provider Id is correct");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));		
		Assert.assertEquals(accessionDemographics.insProviderIdInput().getAttribute("value"), daoManagerPatientPortal.getNpiFromAccnPhysTypByAccnId(accnId, testDb));
	}
	
	@Test(priority = 1, description = "Accession Demographic - Insurance Payor Priority")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_458(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_458 *****");    	
		
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
		/*
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the file maintenance page displays");
		//Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("File Maintenance - Exam Code "), "        File Maintenance - Exam Code  page title should show.");
		
		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
		
		logger.info("*** Step 4 Actions: - Clear all the Cache");
		dataCacheConfiguration.setClearCacheAll(this);
		
		logger.info("*** Step 4 Expected Results: - Verify the cache is cleared");
		int rowNum = dataCacheConfiguration.getRowNumberInDataCacheTable(dataCacheConfiguration.dataCacheConfigTable(), "ADJ_CD");
		Assert.assertFalse(dataCacheConfiguration.reloadNowText(rowNum).isSelected(), "        The Reload checkbox for ADJ_CD should be unchecked.");
		
		logger.info("*** Step 5 Actions: - Send request to create an accession");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, configuration.getUrl(UrlTyp.ACCN_WS).toString());
    	logger.info("AccnID: " + accnId);
    	
    	logger.info("*** Step 5 Expected Results: The accession gets created");
    	Assert.assertTrue(accnId != null);
    	
    	logger.info("*** Step 5 Expected Results: - The new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	logger.info("*** Step 6 Actions: - Release the new accession in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);	
    	
    	logger.info("*** Step 6 Exepected Results: The new accession in the Q_FR_Pending table is set");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnIdHoldRuleBReleaseBOEPerformed(accnId, testDb), 1);
		*/
    	logger.info("*** Step 7 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 7 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 8 Actions: - Navigate to Demographics");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();
		
		logger.info("*** Step 8 Expected Results: - The Demographics page displays");
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "       Accession Demographics page title should show.");

		logger.info("*** Step 9 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		String accnId = daoManagerXifinRpm.getAccnIdWPyrSubsIdFromACCN(testDb);
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 9 Expected Results: - Verify that the insurance Payor Priority is correct");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.insPyrPrioInput().getAttribute("value"), "1");
		//Assert.assertEquals(accessionDemographics.insPyrPrioInput().getAttribute("value"), daoManagerPatientPortal.getPyrPriorityFromAccnPyrByAccnId(accnId, testDb));
	}
	
	@Test(priority = 1, description = "Accession Demographic - Patient Ordering UPIN")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_459(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_459 *****");    	
		
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
		/*
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the file maintenance page displays");
		//Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("File Maintenance - Exam Code "), "        File Maintenance - Exam Code  page title should show.");
		
		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
		
		logger.info("*** Step 4 Actions: - Clear all the Cache");
		dataCacheConfiguration.setClearCacheAll(this);
		
		logger.info("*** Step 4 Expected Results: - Verify the cache is cleared");
		int rowNum = dataCacheConfiguration.getRowNumberInDataCacheTable(dataCacheConfiguration.dataCacheConfigTable(), "ADJ_CD");
		Assert.assertFalse(dataCacheConfiguration.reloadNowText(rowNum).isSelected(), "        The Reload checkbox for ADJ_CD should be unchecked.");
		
		logger.info("*** Step 5 Actions: - Send request to create an accession");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, configuration.getUrl(UrlTyp.ACCN_WS).toString());
    	logger.info("AccnID: " + accnId);
    	
    	logger.info("*** Step 5 Expected Results: The accession gets created");
    	Assert.assertTrue(accnId != null);
    	
    	logger.info("*** Step 5 Expected Results: - The new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	logger.info("*** Step 6 Actions: - Release the new accession in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);	
    	
    	logger.info("*** Step 6 Exepected Results: The new accession in the Q_FR_Pending table is set");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnIdHoldRuleBReleaseBOEPerformed(accnId, testDb), 1);
		*/
    	logger.info("*** Step 7 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 7 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 8 Actions: - Navigate to Demographics");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();
		
		logger.info("*** Step 8 Expected Results: - The Demographics page displays");
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "       Accession Demographics page title should show.");

		logger.info("*** Step 9 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		String accnId = daoManagerXifinRpm.getAccnIdWOrderingPhysFromACCN(testDb);
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 9 Expected Results: - Verify that the patient UPIN is correct");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));		
		Assert.assertEquals(accessionDemographics.orderPhysIdInput().getAttribute("value"), daoManagerPatientPortal.getUpniFromAccnPhysTypByAccnId(accnId, testDb));
	}
	
	@Test(priority = 1, description = "Accession Diagnosis - Load Accession")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_569(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_569 *****");    	
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession via Accession WS");
		
	  	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null);
    	
		//accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		//String accnId = accessionUtils.createPricedAccn(this, env, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, endpoint);
		
		logger.info("*** Step 17 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		String winHandler = driver.getWindowHandle();
		String popupHandles = switchToPopupWin("c");
		switchToPopupWin();
		
		logger.info("*** Step 17 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 18 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 18 Expected Results: - Verify patient First Name is correct when accession data loads");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));
		
		logger.info("*** Step 19 Actions: - Navigate to Diagnosis page");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDiagnosis();
		switchToPopupWin();
		
		logger.info("*** Step 19 Expected Results: - The Accession - Diagnosis Configuration displays");
		accessionDiagnosis = new AccessionDiagnosis(driver);
		Assert.assertEquals(accessionDiagnosis.pageTitleText().getText().trim(), "Accession - Diagnosis Configuration", "        Accession - Diagnosis Configuration page title should show.");
		
		logger.info("*** Step 20 Actions: - Enter the Accession Id in the Accession ID input field");
		accessionDiagnosis.searchAccnId(accnId);
		
		logger.info("*** Step 20 Expected Results: - The Accession Id loads in the Accession Id input field");
		Assert.assertEquals(accessionDiagnosis.accnIdInput().getAttribute("value"), accnId);
    }
	
	@Test(priority = 1, description = "Accession Diagnosis - EPI")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_570(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_570 *****");    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession via Accession WS");
		
	  	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null);
    	/*
		accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		String accnId = accessionUtils.createPricedAccn(this, env, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, endpoint);
		*/
		logger.info("*** Step 17 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		String winHandler = driver.getWindowHandle();
		String popupHandles = switchToPopupWin("c");
		switchToPopupWin();
		
		logger.info("*** Step 17 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 18 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 18 Expected Results: - Verify patient First Name is correct when accession data loads");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));
		 
		logger.info("*** Step 19 Actions: - Navigate to Create New Patient popup");
		accessionDemographics.naviateToCreateNewPatient();
		//switchToPopupWin();
		
		logger.info("*** Step 19 Expected Results: - Verify Create New Patient popup displays");
		Assert.assertTrue(isElementPresent(accessionDemographics.createNewPatientPopupOkBtn(),5));
	
		logger.info("*** Step 20 Actions: - Add the epi");
		accessionDemographics.sysGenerateEpi();
		
		logger.info("*** Step 20 Expected Results: - Verify that the EPI gets generated");
		Assert.assertFalse(accessionDemographics.epiText().trim().isEmpty(), "        EPI: " + accessionDemographics.epiText() + " should be generated.");
				
		logger.info("*** Step 21 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.submit();
		accessionDemographics.reset();		
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 21 Expected Results: - Verify EPI is associated to the accession");
		Assert.assertEquals(accessionDemographics.epiText().toString(), daoManagerPatientPortal.getEpiByAccnId(accnId, testDb));
				
		logger.info("*** Step 22 Actions: - Navigate to Diagnosis page");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDiagnosis();
		switchToPopupWin();
		
		logger.info("*** Step 22 Expected Results: - The Accession - Diagnosis Configuration displays");
		accessionDiagnosis = new AccessionDiagnosis(driver);
		Assert.assertEquals(accessionDiagnosis.pageTitleText().getText().trim(), "Accession - Diagnosis Configuration", "        Accession - Diagnosis Configuration page title should show.");
		
		logger.info("*** Step 23 Actions: - Enter the Accession Id in the Accession ID input field");
		accessionDiagnosis.searchAccnId(accnId);
		
		logger.info("*** Step 23 Expected Results: - The Accession Id loads in the Accession Id input field");
		Assert.assertEquals(accessionDiagnosis.accnIdInput().getAttribute("value"), accnId);
		
		logger.info("*** Step 23 Expected Results: - The EPI should be correct");
		Assert.assertEquals(accessionDiagnosis.epiInput().getAttribute("value"), daoManagerPatientPortal.getEpiByAccnId(accnId, testDb).trim());
    }
	
	@Test(priority = 1, description = "Accession Diagnosis - Patient Name")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_571(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_571 *****");    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
				
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
				
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession via Accession WS");
				
		testDataSetup = new TestDataSetup(driver);
		String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
		logger.info("AccnID: " + accnId);
		Assert.assertTrue(accnId != null);
		/*
		accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		String accnId = accessionUtils.createPricedAccn(this, env, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, endpoint);
		*/
		logger.info("*** Step 17 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		String winHandler = driver.getWindowHandle();
		String popupHandles = switchToPopupWin("c");
		switchToPopupWin();
		
		logger.info("*** Step 17 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 18 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 18 Expected Results: - Verify patient First Name is correct when accession data loads");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));
		 
		logger.info("*** Step 19 Actions: - Navigate to Create New Patient popup");
		accessionDemographics.naviateToCreateNewPatient();
		//switchToPopupWin();
		
		logger.info("*** Step 19 Expected Results: - Verify Create New Patient popup displays");
		Assert.assertTrue(isElementPresent(accessionDemographics.createNewPatientPopupOkBtn(),5));
			
		logger.info("*** Step 20 Actions: - Add the epi");
		accessionDemographics.sysGenerateEpi();
		
		logger.info("*** Step 20 Expected Results: - Verify that the EPI gets generated");
		Assert.assertFalse(accessionDemographics.epiText().trim().isEmpty(), "        EPI: " + accessionDemographics.epiText() + " should be generated.");
		
		logger.info("*** Step 21 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.submit();				
		accessionDemographics.reset();
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 21 Expected Results: - Verify EPI is associated to the accession");
		Assert.assertEquals(accessionDemographics.epiText().toString(), daoManagerPatientPortal.getEpiByAccnId(accnId, testDb));
		
		logger.info("*** Step 22 Actions: - Navigate to Diagnosis page");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDiagnosis();
		switchToPopupWin();
		
		logger.info("*** Step 22 Expected Results: - The Accession - Diagnosis Configuration displays");
		accessionDiagnosis = new AccessionDiagnosis(driver);
		Assert.assertEquals(accessionDiagnosis.pageTitleText().getText().trim(), "Accession - Diagnosis Configuration", "        Accession - Diagnosis Configuration page title should show.");
		
		logger.info("*** Step 23 Actions: - Enter the Accession Id in the Accession ID input field");
		accessionDiagnosis.searchAccnId(accnId);
		
		logger.info("*** Step 23 Expected Results: - The Accession Id loads in the Accession Id input field");
		Assert.assertEquals(accessionDiagnosis.accnIdInput().getAttribute("value"), accnId);
		
		logger.info("*** Step 23 Expected Results: - The patient name should be correct");
		Assert.assertEquals(accessionDiagnosis.ptNameInput().getAttribute("value"), daoManagerPatientPortal.getFullNameByAccnId(accnId, testDb));
    }
	
	@Test(priority = 1, description = "Accession Diagnosis - Accession Status")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_572(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_572 *****");    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
				
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
				
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession via Accession WS");
				
		testDataSetup = new TestDataSetup(driver);
		String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
		logger.info("AccnID: " + accnId);
		Assert.assertTrue(accnId != null);    	
		/*
		accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		String accnId = accessionUtils.createPricedAccn(this, env, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, endpoint);
		*/
		logger.info("*** Step 17 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		String winHandler = driver.getWindowHandle();
		String popupHandles = switchToPopupWin("c");
		switchToPopupWin();
		
		logger.info("*** Step 17 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 18 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 18 Expected Results: - Verify patient First Name is correct when accession data loads");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));
		 
		logger.info("*** Step 19 Actions: - Navigate to Diagnosis page");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDiagnosis();
		switchToPopupWin();
		
		logger.info("*** Step 19 Expected Results: - The Accession - Diagnosis Configuration displays");
		accessionDiagnosis = new AccessionDiagnosis(driver);
		Assert.assertEquals(accessionDiagnosis.pageTitleText().getText().trim(), "Accession - Diagnosis Configuration", "        Accession - Diagnosis Configuration page title should show.");
		
		logger.info("*** Step 20 Actions: - Enter the Accession Id in the Accession ID input field");
		accessionDiagnosis.searchAccnId(accnId);
		
		logger.info("*** Step 20 Expected Results: - The Accession Id loads in the Accession Id input field");
		Assert.assertEquals(accessionDiagnosis.accnIdInput().getAttribute("value"), accnId);
		
		logger.info("*** Step 20 Expected Results: - The accessio status should be correct");
		Assert.assertEquals(accessionDiagnosis.accnStatusTypeInput().getAttribute("value"), "Priced");
    }
	
	@Test(priority = 1, description = "Accession Diagnosis - Date")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_573(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_573 *****");    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
				
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
				
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession via Accession WS");
				
		testDataSetup = new TestDataSetup(driver);
		String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
		logger.info("AccnID: " + accnId);
		Assert.assertTrue(accnId != null);    	
		/*
		accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		String accnId = accessionUtils.createPricedAccn(this, env, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, endpoint);
		*/
		logger.info("*** Step 17 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		String winHandler = driver.getWindowHandle();
		String popupHandles = switchToPopupWin("c");
		switchToPopupWin();
		
		logger.info("*** Step 17 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 18 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 18 Expected Results: - Verify patient First Name is correct when accession data loads");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));
		
		logger.info("*** Step 19 Actions: - Navigate to Diagnosis page");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDiagnosis();
		switchToPopupWin();
		
		logger.info("*** Step 19 Expected Results: - The Accession - Diagnosis Configuration displays");
		accessionDiagnosis = new AccessionDiagnosis(driver);
		Assert.assertEquals(accessionDiagnosis.pageTitleText().getText().trim(), "Accession - Diagnosis Configuration", "        Accession - Diagnosis Configuration page title should show.");
		
		logger.info("*** Step 20 Actions: - Enter the Accession Id in the Accession ID input field");
		accessionDiagnosis.searchAccnId(accnId);
		
		logger.info("*** Step 20 Expected Results: - The Accession Id loads in the Accession Id input field");
		Assert.assertEquals(accessionDiagnosis.accnIdInput().getAttribute("value"), accnId);
		
		logger.info("*** Step 20 Expected Results: - The date should be correct");
		ArrayList<String> list = daoManagerXifinRpm.getDiagnosisInfoFromaccnDiagByAccnId(accnId, 0, testDb);
		String date = list.get(1);
		Assert.assertEquals(accessionDiagnosis.dateInput().getAttribute("value"), date);
    }
	
	@Test(priority = 1, description = "Accession Diagnosis - Diagnosis Code")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "diagCodeType"})
	public void testRPM_574(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String diagCodeType) throws Exception {
		logger.info("***** Testing - testRPM_574 *****");    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession via Accession WS");
		
	  	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null);    	
		/*
		accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		String accnId = accessionUtils.createPricedAccn(this, env, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, endpoint);
		*/
		logger.info("*** Step 17 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		String winHandler = driver.getWindowHandle();
		String popupHandles = switchToPopupWin("c");
		switchToPopupWin();
		
		logger.info("*** Step 17 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 18 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 18 Expected Results: - Verify patient First Name is correct when accession data loads");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));
		 
		logger.info("*** Step 19 Actions: - Navigate to Create New Patient popup");
		accessionDemographics.naviateToCreateNewPatient();
		//switchToPopupWin();
		
		logger.info("*** Step 19 Expected Results: - Verify Create New Patient popup displays");
		Assert.assertTrue(isElementPresent(accessionDemographics.createNewPatientPopupOkBtn(),5));
			
		logger.info("*** Step 20 Actions: - Add the epi");
		accessionDemographics.sysGenerateEpi();
		
		logger.info("*** Step 20 Expected Results: - Verify that the EPI gets generated");
		Assert.assertFalse(accessionDemographics.epiText().trim().isEmpty(), "        EPI: " + accessionDemographics.epiText() + " should be generated.");
		
		logger.info("*** Step 21 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.submit();				
		accessionDemographics.reset();
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 20 Expected Results: - Verify EPI is associated to the accession");
		Assert.assertEquals(accessionDemographics.epiText().toString(), daoManagerPatientPortal.getEpiByAccnId(accnId, testDb));
		
		logger.info("*** Step 21 Actions: - Navigate to Diagnosis page");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDiagnosis();
		switchToPopupWin();
		String winHandler2 = driver.getWindowHandle();
		
		logger.info("*** Step 21 Expected Results: - The Accession - Diagnosis Configuration displays");
		accessionDiagnosis = new AccessionDiagnosis(driver);
		Assert.assertEquals(accessionDiagnosis.pageTitleText().getText().trim(), "Accession - Diagnosis Configuration", "        Accession - Diagnosis Configuration page title should show.");
		
		logger.info("*** Step 22 Actions: - Enter the Accession Id in the Accession ID input field");
		accessionDiagnosis.searchAccnId(accnId);
		
		logger.info("*** Step 22 Expected Results: - The Accession Id loads in the Accession Id input field");
		Assert.assertEquals(accessionDiagnosis.accnIdInput().getAttribute("value"), accnId);
		
		logger.info("*** Step 23 Actions: - Click on the Diagnosis Search button");
		accessionDiagnosis.navigateToDiagnosisCode();
		switchToPopupWin();
		
		logger.info("*** Step 23 Expected Results: - The Diagnosis Code Search popup displays");
		diagnosisCodeSearch = new DiagnosisCodeSearch(driver);
		Assert.assertTrue(isElementPresent(diagnosisCodeSearch.searchBtn(), 5));
		
		logger.info("*** Step 24 Actions: - Search Diagnosis Code");
		diagnosisCodeSearch.setDiagnosisDxType(this, diagCodeType);
		
		logger.info("*** Step 24 Expected Results: - The Diagnosis Code Search Results popup displays");
		switchToPopupWin();
		diagnosisCodeSearchResults = new DiagnosisCodeSearchResults(driver);
		Assert.assertTrue(isElementPresent(diagnosisCodeSearchResults.diagonsisCodeSearchResultTable(), 5));
		
		logger.info("*** Step 25 Actions: - Search Diagnosis Code");
		diagnosisCodeSearchResults.selectDxCode(2, 2, diagnosisCodeSearchResults.diagonsisCodeSearchResultText(2,2).getAttribute("value"));
		switchToWin(winHandler2);
		
		logger.info("*** Step 25 Expected Results: - The Diagnosis Code loads");
		Assert.assertTrue(isElementPresent(accessionDiagnosis.dxCodeInput(), 5));
		ArrayList<String> list = daoManagerXifinRpm.getDiagnosisInfoFromaccnDiagByAccnId(accnId, 0, testDb);
		String dxCode = list.get(5);
		Assert.assertNotNull(accessionDiagnosis.dxCodeInput().getAttribute("value"));
		
		logger.info("*** Step 26 Actions: - Enter Diagnosis Info");
		accessionDiagnosis.setDiagnosisInfo(this);
		
		logger.info("*** Step 26 Expected Results: - The Diagnosis Info gets submitted");
		Assert.assertEquals(accessionDiagnosis.accnIdInput().getAttribute("value"), "");
		
		logger.info("*** Step 27 Actions: - Navigate to Transaction Details page");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToTransactionDetailLink();
		switchToPopupWin();
		
		logger.info("*** Step 27 Expected Results: - Verify that the Accession - Transaction Detail page displays");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertEquals(accessionTransactionDetail.pageTitleText().getText().trim(), "Accession - Transaction Detail", "        Accession - Transaction Detail page title should show.");

		logger.info("*** Step 28 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionTransactionDetail.searchAccnId(accnId);
		
		logger.info("*** Step 28 Expected Results: - Verify the accession loads");
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(),5));
		Assert.assertEquals(accessionTransactionDetail.accnIdInput().getAttribute("value"), accnId);
		
		logger.info("*** Step 28 Expected Results: - Verify the DxCode is correct");
		Assert.assertEquals(accessionTransactionDetail.dXCodeText(dxCode).getAttribute("value"), dxCode);
    }
	
	@Test(priority = 1, description = "Accession Diagnosis - Comment")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "diagCodeType"})
	public void testRPM_575(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String diagCodeType) throws Exception {
		logger.info("***** Testing - testRPM_575 *****");    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession via Accession WS");
		
	  	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null);    	
		/*
		accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		String accnId = accessionUtils.createPricedAccn(this, env, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, endpoint);
		*/
		logger.info("*** Step 17 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		String winHandler = driver.getWindowHandle();
		String popupHandles = switchToPopupWin("c");
		switchToPopupWin();
		
		logger.info("*** Step 17 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 18 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 18 Expected Results: - Verify patient First Name is correct when accession data loads");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));
		 
		logger.info("*** Step 19 Actions: - Navigate to Create New Patient popup");
		accessionDemographics.naviateToCreateNewPatient();
		//switchToPopupWin();
		
		logger.info("*** Step 19 Expected Results: - Verify Create New Patient popup displays");
		Assert.assertTrue(isElementPresent(accessionDemographics.createNewPatientPopupOkBtn(),5));
			
		logger.info("*** Step 20 Actions: - Add the epi");
		accessionDemographics.sysGenerateEpi();
		
		logger.info("*** Step 20 Expected Results: - Verify that the EPI gets generated");
		Assert.assertFalse(accessionDemographics.epiText().trim().isEmpty(), "        EPI: " + accessionDemographics.epiText() + " should be generated.");
		
		logger.info("*** Step 21 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.submit();			
		accessionDemographics.reset();		
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 20 Expected Results: - Verify EPI is associated to the accession");
		Assert.assertEquals(accessionDemographics.epiText().toString(), daoManagerPatientPortal.getEpiByAccnId(accnId, testDb));
		
		logger.info("*** Step 21 Actions: - Navigate to Diagnosis page");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDiagnosis();
		switchToPopupWin();
		String winHandler2 = driver.getWindowHandle();
		
		logger.info("*** Step 21 Expected Results: - The Accession - Diagnosis Configuration displays");
		accessionDiagnosis = new AccessionDiagnosis(driver);
		Assert.assertEquals(accessionDiagnosis.pageTitleText().getText().trim(), "Accession - Diagnosis Configuration", "        Accession - Diagnosis Configuration page title should show.");
		
		logger.info("*** Step 22 Actions: - Enter the Accession Id in the Accession ID input field");
		accessionDiagnosis.searchAccnId(accnId);
		
		logger.info("*** Step 22 Expected Results: - The Accession Id loads in the Accession Id input field");
		Assert.assertEquals(accessionDiagnosis.accnIdInput().getAttribute("value"), accnId);
		
		logger.info("*** Step 23 Actions: - Click on the Diagnosis Search button");
		accessionDiagnosis.navigateToDiagnosisCode();
		switchToPopupWin();
		
		logger.info("*** Step 23 Expected Results: - The Diagnosis Code Search popup displays");
		diagnosisCodeSearch = new DiagnosisCodeSearch(driver);
		Assert.assertTrue(isElementPresent(diagnosisCodeSearch.searchBtn(), 5));
		
		logger.info("*** Step 24 Actions: - Search Diagnosis Code");
		diagnosisCodeSearch.setDiagnosisDxType(this, diagCodeType);
		
		logger.info("*** Step 24 Expected Results: - The Diagnosis Code Search Results popup displays");
		switchToPopupWin();
		diagnosisCodeSearchResults = new DiagnosisCodeSearchResults(driver);
		Assert.assertTrue(isElementPresent(diagnosisCodeSearchResults.diagonsisCodeSearchResultTable(), 5));
		
		logger.info("*** Step 25 Actions: - Search Diagnosis Code");
		diagnosisCodeSearchResults.selectDxCode(2, 2, diagnosisCodeSearchResults.diagonsisCodeSearchResultText(2,2).getAttribute("value"));
		switchToWin(winHandler2);
		
		logger.info("*** Step 25 Expected Results: - The Diagnosis Code loads");
		Assert.assertTrue(isElementPresent(accessionDiagnosis.dxCodeInput(), 5));
		Assert.assertNotNull(accessionDiagnosis.dxCodeInput().getAttribute("value"));
		
		logger.info("*** Step 26 Actions: - Enter Diagnosis Info");
		accessionDiagnosis.setDiagnosisInfo(this);
		
		logger.info("*** Step 26 Expected Results: - The Diagnosis Info gets submitted");
		Assert.assertEquals(accessionDiagnosis.accnIdInput().getAttribute("value"), "");
		
		logger.info("*** Step 27 Actions: - Navigate to Transaction Details page");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToTransactionDetailLink();
		switchToPopupWin();
		
		logger.info("*** Step 27 Expected Results: - Verify that the Accession - Transaction Detail page displays");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertEquals(accessionTransactionDetail.pageTitleText().getText().trim(), "Accession - Transaction Detail", "        Accession - Transaction Detail page title should show.");

		logger.info("*** Step 28 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionTransactionDetail.searchAccnId(accnId);
		
		logger.info("*** Step 28 Expected Results: - Verify the accession loads");
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(),5));
		Assert.assertEquals(accessionTransactionDetail.accnIdInput().getAttribute("value"), accnId);
		
		logger.info("*** Step 28 Expected Results: - Verify the Comments is correct");
		ArrayList<String> list = daoManagerXifinRpm.getDiagnosisInfoFromaccnDiagByAccnId(accnId, 1, testDb);
		String comments = list.get(2);
		Assert.assertEquals(accessionTransactionDetail.commentInputRow2Text().getAttribute("value"), comments);
    }
	
	@Test(priority = 1, description = "Accession Diagnosis - Document Received")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "diagCodeType"})
	public void testRPM_576(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String diagCodeType) throws Exception {
		logger.info("***** Testing - testRPM_576 *****");    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession via Accession WS");
		
	  	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null);    	
		/*
		accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		String accnId = accessionUtils.createPricedAccn(this, env, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, endpoint);
		*/
		logger.info("*** Step 17 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		String winHandler = driver.getWindowHandle();
		String popupHandles = switchToPopupWin("c");
		switchToPopupWin();
		
		logger.info("*** Step 17 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 18 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 18 Expected Results: - Verify patient First Name is correct when accession data loads");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));
		 
		logger.info("*** Step 19 Actions: - Navigate to Create New Patient popup");
		accessionDemographics.naviateToCreateNewPatient();
		//switchToPopupWin();
		
		logger.info("*** Step 19 Expected Results: - Verify Create New Patient popup displays");
		Assert.assertTrue(isElementPresent(accessionDemographics.createNewPatientPopupOkBtn(),5));
	
		logger.info("*** Step 20 Actions: - Add the epi");
		accessionDemographics.sysGenerateEpi();
		
		logger.info("*** Step 20 Expected Results: - Verify that the EPI gets generated");
		Assert.assertFalse(accessionDemographics.epiText().trim().isEmpty(), "        EPI: " + accessionDemographics.epiText() + " should be generated.");
		
		logger.info("*** Step 21 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.submit();		
		accessionDemographics.reset();
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 21 Expected Results: - Verify EPI is associated to the accession");
		Assert.assertEquals(accessionDemographics.epiText().toString(), daoManagerPatientPortal.getEpiByAccnId(accnId, testDb));
		
		logger.info("*** Step 22 Actions: - Navigate to Diagnosis page");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDiagnosis();
		switchToPopupWin();
		String winHandler2 = driver.getWindowHandle();
		
		logger.info("*** Step 22 Expected Results: - The Accession - Diagnosis Configuration displays");
		accessionDiagnosis = new AccessionDiagnosis(driver);
		Assert.assertEquals(accessionDiagnosis.pageTitleText().getText().trim(), "Accession - Diagnosis Configuration", "        Accession - Diagnosis Configuration page title should show.");
		
		logger.info("*** Step 23 Actions: - Enter the Accession Id in the Accession ID input field");
		accessionDiagnosis.searchAccnId(accnId);
		
		logger.info("*** Step 23 Expected Results: - The Accession Id loads in the Accession Id input field");
		Assert.assertEquals(accessionDiagnosis.accnIdInput().getAttribute("value"), accnId);
		
		logger.info("*** Step 24 Actions: - Click on the Diagnosis Search button");
		accessionDiagnosis.navigateToDiagnosisCode();
		switchToPopupWin();
		
		logger.info("*** Step 24 Expected Results: - The Diagnosis Code Search popup displays");
		diagnosisCodeSearch = new DiagnosisCodeSearch(driver);
		Assert.assertTrue(isElementPresent(diagnosisCodeSearch.searchBtn(), 5));
		
		logger.info("*** Step 25 Actions: - Search Diagnosis Code");
		diagnosisCodeSearch.setDiagnosisDxType(this, diagCodeType);
		
		logger.info("*** Step 25 Expected Results: - The Diagnosis Code Search Results popup displays");
		switchToPopupWin();
		diagnosisCodeSearchResults = new DiagnosisCodeSearchResults(driver);
		Assert.assertTrue(isElementPresent(diagnosisCodeSearchResults.diagonsisCodeSearchResultTable(), 5));
		
		logger.info("*** Step 26 Actions: - Search Diagnosis Code");
		diagnosisCodeSearchResults.selectDxCode(2, 2, diagnosisCodeSearchResults.diagonsisCodeSearchResultText(2,2).getAttribute("value"));
		switchToWin(winHandler2);
		
		logger.info("*** Step 26 Expected Results: - The Diagnosis Code loads");
		Assert.assertTrue(isElementPresent(accessionDiagnosis.dxCodeInput(), 5));
		Assert.assertNotNull(accessionDiagnosis.dxCodeInput().getAttribute("value"));
		
		logger.info("*** Step 27 Actions: - Enter Diagnosis Info");
		accessionDiagnosis.setDiagnosisInfo(this);
		
		logger.info("*** Step 27 Expected Results: - The Diagnosis Info gets submitted");
		Assert.assertEquals(accessionDiagnosis.accnIdInput().getAttribute("value"), "");
		
		logger.info("*** Step 28 Actions: - Navigate to Transaction Details page");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToTransactionDetailLink();
		switchToPopupWin();
		
		logger.info("*** Step 28 Expected Results: - Verify that the Accession - Transaction Detail page displays");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertEquals(accessionTransactionDetail.pageTitleText().getText().trim(), "Accession - Transaction Detail", "        Accession - Transaction Detail page title should show.");

		logger.info("*** Step 29 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionTransactionDetail.searchAccnId(accnId);
		
		logger.info("*** Step 29 Expected Results: - Verify the accession loads");
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(),5));
		Assert.assertEquals(accessionTransactionDetail.accnIdInput().getAttribute("value"), accnId);
		
		logger.info("*** Step 29 Expected Results: - Verify the Document Received is correct");
		ArrayList<String> list = daoManagerXifinRpm.getDiagnosisInfoFromaccnDiagByAccnId(accnId, 1, testDb);
		String docId = list.get(0);
		Assert.assertEquals(accessionTransactionDetail.docRecvInputRow2Text().getAttribute("value"), daoManagerXifinRpm.getDocNameFromDocByDocIdStatusId(docId, testDb));
    }
	
	@Test(priority = 1, description = "Accession Diagnosis - Client Contact")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "diagCodeType"})
	public void testRPM_577(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String diagCodeType) throws Exception {
		logger.info("***** Testing - testRPM_577 *****");    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession via Accession WS");
		
	  	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null);    	
		/*
		accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		String accnId = accessionUtils.createPricedAccn(this, env, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, endpoint);
		*/
		logger.info("*** Step 17 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		String winHandler = driver.getWindowHandle();
		String popupHandles = switchToPopupWin("c");
		switchToPopupWin();
		
		logger.info("*** Step 17 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 18 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 18 Expected Results: - Verify patient First Name is correct when accession data loads");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));
		 
		logger.info("*** Step 19 Actions: - Navigate to Create New Patient popup");
		accessionDemographics.naviateToCreateNewPatient();
		//switchToPopupWin();
		
		logger.info("*** Step 19 Expected Results: - Verify Create New Patient popup displays");
		Assert.assertTrue(isElementPresent(accessionDemographics.createNewPatientPopupOkBtn(),5));
			
		logger.info("*** Step 20 Actions: - Add the epi");
		accessionDemographics.sysGenerateEpi();
		
		logger.info("*** Step 20 Expected Results: - Verify that the EPI gets generated");
		Assert.assertFalse(accessionDemographics.epiText().trim().isEmpty(), "        EPI: " + accessionDemographics.epiText() + " should be generated.");
		
		logger.info("*** Step 21 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.submit();			
		accessionDemographics.reset();
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 21 Expected Results: - Verify EPI is associated to the accession");
		Assert.assertEquals(accessionDemographics.epiText().toString(), daoManagerPatientPortal.getEpiByAccnId(accnId, testDb));
		
		logger.info("*** Step 22 Actions: - Navigate to Diagnosis page");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDiagnosis();
		switchToPopupWin();
		String winHandler2 = driver.getWindowHandle();
		
		logger.info("*** Step 22 Expected Results: - The Accession - Diagnosis Configuration displays");
		accessionDiagnosis = new AccessionDiagnosis(driver);
		Assert.assertEquals(accessionDiagnosis.pageTitleText().getText().trim(), "Accession - Diagnosis Configuration", "        Accession - Diagnosis Configuration page title should show.");
		
		logger.info("*** Step 23 Actions: - Enter the Accession Id in the Accession ID input field");
		accessionDiagnosis.searchAccnId(accnId);
		
		logger.info("*** Step 23 Expected Results: - The Accession Id loads in the Accession Id input field");
		Assert.assertEquals(accessionDiagnosis.accnIdInput().getAttribute("value"), accnId);
		
		logger.info("*** Step 24 Actions: - Click on the Diagnosis Search button");
		accessionDiagnosis.navigateToDiagnosisCode();
		switchToPopupWin();
		
		logger.info("*** Step 24 Expected Results: - The Diagnosis Code Search popup displays");
		diagnosisCodeSearch = new DiagnosisCodeSearch(driver);
		Assert.assertTrue(isElementPresent(diagnosisCodeSearch.searchBtn(), 5));
		
		logger.info("*** Step 25 Actions: - Search Diagnosis Code");
		diagnosisCodeSearch.setDiagnosisDxType(this, diagCodeType);
		
		logger.info("*** Step 25 Expected Results: - The Diagnosis Code Search Results popup displays");
		switchToPopupWin();
		diagnosisCodeSearchResults = new DiagnosisCodeSearchResults(driver);
		Assert.assertTrue(isElementPresent(diagnosisCodeSearchResults.diagonsisCodeSearchResultTable(), 5));
		
		logger.info("*** Step 26 Actions: - Search Diagnosis Code");
		diagnosisCodeSearchResults.selectDxCode(2, 2, diagnosisCodeSearchResults.diagonsisCodeSearchResultText(2,2).getAttribute("value"));
		switchToWin(winHandler2);
		
		logger.info("*** Step 26 Expected Results: - The Diagnosis Code loads");
		Assert.assertTrue(isElementPresent(accessionDiagnosis.dxCodeInput(), 5));
		Assert.assertNotNull(accessionDiagnosis.dxCodeInput().getAttribute("value"));
		
		logger.info("*** Step 27 Actions: - Enter Diagnosis Info");
		accessionDiagnosis.setDiagnosisInfo(this);
		
		logger.info("*** Step 27 Expected Results: - The Diagnosis Info gets submitted");
		Assert.assertEquals(accessionDiagnosis.accnIdInput().getAttribute("value"), "");
		
		logger.info("*** Step 28 Actions: - Navigate to Transaction Details page");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToTransactionDetailLink();
		switchToPopupWin();
		
		logger.info("*** Step 28 Expected Results: - Verify that the Accession - Transaction Detail page displays");
		accessionTransactionDetail = new AccessionTransactionDetail(driver);
		Assert.assertEquals(accessionTransactionDetail.pageTitleText().getText().trim(), "Accession - Transaction Detail", "        Accession - Transaction Detail page title should show.");

		logger.info("*** Step 29 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionTransactionDetail.searchAccnId(accnId);
		
		logger.info("*** Step 29 Expected Results: - Verify the accession loads");
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(),5));
		Assert.assertEquals(accessionTransactionDetail.accnIdInput().getAttribute("value"), accnId);
		
		logger.info("*** Step 29 Expected Results: - Verify the Client Contact is correct");
		ArrayList<String> list = daoManagerXifinRpm.getDiagnosisInfoFromaccnDiagByAccnId(accnId, 1, testDb);
		String clnContact = list.get(3);
		Assert.assertEquals(accessionTransactionDetail.clientContactInputRow2Text().getAttribute("value"), clnContact);
    }
	
	@Test(priority = 1, description = "Accession Diagnosis - Reset")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_578(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_578 *****");    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession via Accession WS");
		
	  	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null);
		/*
		accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		String accnId = accessionUtils.createPricedAccn(this, env, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, endpoint);
		*/
		logger.info("*** Step 17 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		String winHandler = driver.getWindowHandle();
		String popupHandles = switchToPopupWin("c");
		switchToPopupWin();
		
		logger.info("*** Step 17 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 18 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 18 Expected Results: - Verify patient First Name is correct when accession data loads");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));
		 
		logger.info("*** Step 19 Actions: - Navigate to Diagnosis page");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDiagnosis();
		switchToPopupWin();
		
		logger.info("*** Step 19 Expected Results: - The Accession - Diagnosis Configuration displays");
		accessionDiagnosis = new AccessionDiagnosis(driver);
		Assert.assertEquals(accessionDiagnosis.pageTitleText().getText().trim(), "Accession - Diagnosis Configuration", "        Accession - Diagnosis Configuration page title should show.");
		
		logger.info("*** Step 20 Actions: - Enter the Accession Id in the Accession ID input field");
		accessionDiagnosis.searchAccnId(accnId);
		
		logger.info("*** Step 20 Expected Results: - The Accession Id loads in the Accession Id input field");
		Assert.assertEquals(accessionDiagnosis.accnIdInput().getAttribute("value"), accnId);
		
		logger.info("*** Step 21 Actions: - Click on the Reset button");
		accessionDiagnosis.resetDiagnosis();
		
		logger.info("*** Step 21 Expected Results: - The Accession Id loads in the Accession Id input field");
		Assert.assertEquals(accessionDiagnosis.accnIdInput().getAttribute("value"), "");
		
    }
	
	@Test(priority = 1, description = "Accession Demographics - Eligible Subscriber Eligibility Check with WS")
	@Parameters({"email", "password", "accnId", "pyrAbbrev"})
	public void testRPM_704(String email, String password, String accnId, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_704 *****");    	
		
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
        
        logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
        dataCacheConfiguration = new DataCacheConfiguration(driver);
        Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
        
        logger.info("*** Step 4 Actions: - Clear all the Cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
        
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame(); 
        
        logger.info("*** Step 5 Actions: - Navigate to Accession Tab");
        headerNavigation.navigateToAccessionTab();
        switchToPopupWin();
        
        //Go to main page
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		logger.info("*** Step 6 Actions: - Navigate to the Accession - Demographics page");	
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();
		winHandler = driver.getWindowHandle();
		
		logger.info("*** Step 6 Expected Results: - The Accession - Demographics page displays");		
		accessionDemographics = new AccessionDemographics(driver);		
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));
		
		logger.info("*** Step 7 Actions: - Enter test accn in Accn ID field and tab out");			
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 7 Expected Results: - Verify that the accn was loaded");		
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled after the accn " + accnId + " was loaded.");
		
		logger.info("*** Step 8 Actions: - Check Check Eligibility checkbox and click Submit button");
		selectCheckBox(accessionDemographics.checkEligibilityCheckBox());
		accessionDemographics.submitBtn().click();			
		
		logger.info("*** Step 8 Expected Results: - Verify that Eligibility is Valid message returned");
		Assert.assertTrue(accessionDemographics.eligibleMsgText().trim().contains("Eligibility is Valid. Response: Eligibility check successful"), "        Eligibility is Valid. Response: Eligibility check successful message should show.");
		
		logger.info("*** Step 8 Expected Results: - Verify that Eligible Eligibility status was saved in DB");	
		ArrayList<String> eligStatusList = daoManagerXifinRpm.getEligStatusFromACCNPYRByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);
		//"1" = Eligible
		Assert.assertTrue(eligStatusList.get(0).trim().equals("1"), "        The value in ACCN_PYR.FK_ELIG_STA_TYP_ID should be '1'");
		
		logger.info("*** Step 9 Actions: - Reload the same accn on Accession - Demographics page");
		accessionDemographics.resetBtn().click();
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 9 Expected Results: - Verify that the accn was loaded");
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled after the accn " + accnId + " was loaded.");
				
		logger.info("*** Step 10 Actions: - Click Show Eligibility History Link");
		accessionDemographics.showEligHistoryLink().click();
		
		logger.info("*** Step 10 Expected Results: - Verify that Eligibility Transaction History table shows");		
		//Assert.assertTrue(isElementPresent(accessionDemographics.eligTransHistoryTable(), 5), "        Eligibility Transaction History table should show.");		
		Assert.assertTrue(isElemAttrMatch(accessionDemographics.eligTransHistoryTable(), "style", "display", 15), "        Eligibility Transaction History table should show.");
		
		logger.info("*** Step 11 Actions: - Click Eligibility Trans ID header twice to get the latest Transaction ID");
		accessionDemographics.eligTransIDSortBtn();
		accessionDemographics.eligTransIDSortBtn();
		
		logger.info("*** Step 11 Expected Results: - Verify that the Eligibility History (Payor Name, Eligibility Service, Eligibility Check Date and Eligibility Trans ID display properly");
		ArrayList<String> pyrInfoList = daoManagerXifinRpm.getPyrContactInfoFromPyrByPyrAbbrev(pyrAbbrev, testDb);
		String pyrName = pyrInfoList.get(0).trim();
		Assert.assertTrue(accessionDemographics.eligTransPyrNameText().trim().contains(pyrName), "        Eligibility Payor Name: " + pyrName + " should show.");
		
		Assert.assertTrue(accessionDemographics.eligTransEligServiceText().trim().contains("xifin"), "        Eligibility Service: xifin should show.");
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		Assert.assertTrue(accessionDemographics.eligTransEligChkDtText().trim().contains(currDt), "        Eligibility Check Date: " + currDt + " should show.");
		
		ArrayList<String> eligHistInfoList = daoManagerXifinRpm.getEligTransHistFromACCNELIGHISTByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);
		String latestEligTransId = eligHistInfoList.get(3);
		Assert.assertTrue(accessionDemographics.eligTransIdText().equals(latestEligTransId), "        Eligibility Trans ID: " + latestEligTransId + " should show.");
		
		//Can't open the file in a new window. It opens up with the correct url and redirected to a blank page. 
		logger.info("*** Step 11 Expected Results: - Verify that Eligibility Transaction Response PDF file can be opened properly");
		accessionDemographics.eligRespViewBtn().click();	
		switchToPopupWin();

		URL url = new URL(config.getProperty(PropertyMap.RPM_URL)+"/servlets/AccnDemogrphcServlet?COMMAND=ELIG_RESPONSE&eligTransId=" + latestEligTransId);
		Assert.assertFalse(driver.getTitle().contains("Error Open PDF"), "        PDF file: " + url + " should be opened.");				
		
		logger.info("*** Step 12 Actions: - Switch back to the Accession Demographics and Reset the page");
		driver.close();       
        switchToWin(winHandler);
		accessionDemographics = new AccessionDemographics(driver);		
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));				
		
		accessionDemographics.resetBtn().click();	
		driver.close();
    }	
	
	@Test(priority = 1, description = "Accession Demographics - Ineligible Subscriber Eligibility Check with WS")
	@Parameters({"email", "password", "accnId", "pyrAbbrev", "disableBrowserPlugins"})
	public void testRPM_706(String email, String password, String accnId, String pyrAbbrev,String disableBrowserPlugins) throws Exception {
    	logger.info("***** Testing - testRPM_706 *****");    	
		
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
        
        logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
        dataCacheConfiguration = new DataCacheConfiguration(driver);
        Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
        
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame(); 
        
        logger.info("*** Step 5 Actions: - Navigate to Accession Tab");
        headerNavigation.navigateToAccessionTab();
        switchToPopupWin();
        
        //Go to main page
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		logger.info("*** Step 6 Actions: - Navigate to the Accession - Demographics page");	
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();
		winHandler = driver.getWindowHandle();
		
		logger.info("*** Step 6 Expected Results: - The Accession - Demographics page displays");		
		accessionDemographics = new AccessionDemographics(driver);		
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));
		
		logger.info("*** Step 7 Actions: - Enter test accn in Accn ID field and tab out");		
		accessionDemographics.resetBtn().click();
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 7 Expected Results: - Verify that the accn was loaded");
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled after the accn " + accnId + " was loaded.");		
		
		logger.info("*** Step 8 Actions: - Check Check Eligibility checkbox and click Submit button");
		selectCheckBox(accessionDemographics.checkEligibilityCheckBox());
		accessionDemographics.submitBtn().click();
		
		logger.info("*** Step 8 Expected Results: - Verify that Ineligible error message was returned");
		Assert.assertTrue(accessionDemographics.ineligibleErrMsgText().trim().contains("Eligibility denied"), "        Transaction Acknowledged. Eligibility denied due to error reported by payor error message should show.");
		
		logger.info("*** Step 9 Actions: - Click the Reset button to not save the ineligible error to the accn");
		accessionDemographics.resetBtn().click();
		
		//CR 48846 - All elig changes should be rolled back when Reset is issued on Accn demo, including the accn_elig_hist entry
		logger.info("*** Step 9 Expected Results: - Verify that the Eligibility History is not saved");
		ArrayList<String> eligHistInfoList = daoManagerXifinRpm.getEligTransHistFromACCNELIGHISTByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);		
		Assert.assertTrue(eligHistInfoList.isEmpty(), "        No eligibility transaction history should be logged.");
		/*
		logger.info("*** Step 10 Actions: - Reload the same accn on Accession - Demographics page");
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 10 Expected Results: - Verify that the accn was loaded");
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled after the accn " + accnId + " was loaded.");
		
		logger.info("*** Step 11 Actions: - Click Show Eligibility History Link");
		accessionDemographics.showEligHistoryLink().click();
		
		logger.info("*** Step 11 Expected Results: - Verify that Eligibility Transaction History table shows");		
		//Assert.assertTrue(isElementPresent(accessionDemographics.eligTransHistoryTable(), 5), "        Eligibility Transaction History table should show.");
		Assert.assertTrue(isElemAttrMatch(accessionDemographics.eligTransHistoryTable(), "style", "display", 15), "        Eligibility Transaction History table should show.");
				
		logger.info("*** Step 12 Actions: - Click Eligibility Trans ID header twice to get the latest Transaction ID");
		accessionDemographics.eligTransIDSortBtn();
		accessionDemographics.eligTransIDSortBtn();
		
		logger.info("*** Step 12 Expected Results: - Verify that the Eligibility History (Payor Name, Eligibility Service, Eligibility Check Date and Eligibility Trans ID display properly");
		ArrayList<String> pyrInfoList = daoManagerXifinRpm.getPyrContactInfoFromPyrByPyrAbbrev(pyrAbbrev, testDb);
		String pyrName = pyrInfoList.get(0).trim();
		Assert.assertTrue(accessionDemographics.eligTransPyrNameText().trim().contains(pyrName), "        Eligibility Payor Name: " + pyrName + " should show.");
		
		Assert.assertTrue(accessionDemographics.eligTransEligServiceText().trim().contains("xifin"), "        Eligibility Service: xifin should show.");
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		Assert.assertTrue(accessionDemographics.eligTransEligChkDtText().trim().contains(currDt), "        Eligibility Check Date: " + currDt + " should show.");
		
		ArrayList<String> eligHistInfoList = daoManagerXifinRpm.getEligTransHistFromACCNELIGHISTByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);
		String latestEligTransId = eligHistInfoList.get(3);
		Assert.assertTrue(accessionDemographics.eligTransIdText().equals(latestEligTransId), "        Eligibility Trans ID: " + latestEligTransId + " should show.");
		
		logger.info("*** Step 12 Expected Results: - Verify that Ineligible Eligibility Status was saved to ACCN_ELIG_HIST table");
		String eligStatusTypId = eligHistInfoList.get(4);
		Assert.assertTrue(eligStatusTypId.equals("2"), "        Eligibility Status Type ID: " + eligStatusTypId + " should be saved to ACCN_ELIG_HIST table.");
				
		//Can't open the file in a new window. It opens up with the correct url and redirected to a blank page. 
		logger.info("*** Step 12 Expected Results: - Verify that Eligibility Transaction Response PDF file can be opened properly");
		accessionDemographics.eligRespViewBtn().click();	
		switchToPopupWin();
		
        config = new Configuration(Env.valueOf(env), driver);
        String str = "/servlets/AccnDemogrphcServlet?COMMAND=ELIG_RESPONSE&eligTransId=" + latestEligTransId;
        String url = config.getUrls().concat(str);        
		//Assert.assertFalse(driver.getTitle().contains("Error Open PDF"), "        PDF file: " + url + " should be opened.");	
		Assert.assertFalse(driver.getCurrentUrl().split("=")[1].contains(latestEligTransId), "        PDF file: " + url + " should be opened.");
		
		logger.info("*** Step 13 Actions: - Switch back to the Accession Demographics and Reset the page");
		driver.close();       
        switchToWin(winHandler);
		accessionDemographics = new AccessionDemographics(driver);		
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));				
		
		accessionDemographics.resetBtn().click();	
		*/
		driver.close();
    }
	
	@Test(priority = 1, description = "Accession Demographics - Eligible Dependant Eligibility Check with WS")
	@Parameters({"email", "password", "accnId", "pyrAbbrev", "disableBrowserPlugins"})
	public void testRPM_705(String email, String password, String accnId, String pyrAbbrev, String disableBrowserPlugins) throws Exception {
    	logger.info("***** Testing - testRPM_705 *****");    	
		
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
        
        logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
        dataCacheConfiguration = new DataCacheConfiguration(driver);
        Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame(); 
        
        logger.info("*** Step 5 Actions: - Navigate to Accession Tab");
        headerNavigation.navigateToAccessionTab();
        switchToPopupWin();
        
        //Go to main page
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		logger.info("*** Step 6 Actions: - Navigate to the Accession - Demographics page");	
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToDemographicsLink();
		switchToPopupWin();
		winHandler = driver.getWindowHandle();
		
		logger.info("*** Step 6 Expected Results: - The Accession - Demographics page displays");		
		accessionDemographics = new AccessionDemographics(driver);		
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));
		
		logger.info("*** Step 7 Actions: - Enter test accn in Accn ID field and tab out");			
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 7 Expected Results: - Verify that the accn was loaded");		
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled.");
		
		logger.info("*** Step 8 Actions: - Check Check Eligibility checkbox and click Submit button");
		selectCheckBox(accessionDemographics.checkEligibilityCheckBox());
		accessionDemographics.submitBtn().click();				
		
		logger.info("*** Step 8 Expected Results: - Verify that Eligibility is Valid message returned");
		Assert.assertTrue(accessionDemographics.eligibleMsgText().trim().contains("Eligibility is Valid. Response: Eligibility check successful"), "        Eligibility is Valid. Response: Eligibility check successful message should show.");
		
		logger.info("*** Step 8 Expected Results: - Verify that Eligible Eligibility status was saved in DB");		
		ArrayList<String> eligStatusList = daoManagerXifinRpm.getEligStatusFromACCNPYRByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);
		//"1" = Eligible
		Assert.assertTrue(eligStatusList.get(0).trim().equals("1"), "        The value in ACCN_PYR.FK_ELIG_STA_TYP_ID should be '1'");
		
		logger.info("*** Step 9 Actions: - Reload the same accn on Accession - Demographics page");
		accessionDemographics.resetBtn().click();
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 9 Expected Results: - Verify that the accn was loaded");		
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled after the accn " + accnId + " was loaded.");
		
		logger.info("*** Step 10 Actions: - Click Show Eligibility History Link");
		accessionDemographics.showEligHistoryLink().click();
		
		logger.info("*** Step 10 Expected Results: - Verify that Eligibility Transaction History table shows");
		//Assert.assertTrue(isElementPresent(accessionDemographics.eligTransHistoryTable(), 15), "        Eligibility Transaction History table should show.");
		Assert.assertTrue(isElemAttrMatch(accessionDemographics.eligTransHistoryTable(), "style", "display", 15), "        Eligibility Transaction History table should show.");
		
		logger.info("*** Step 11 Actions: - Click Eligibility Trans ID header twice to get the latest Transaction ID");
		accessionDemographics.eligTransIDSortBtn();
		accessionDemographics.eligTransIDSortBtn();
		
		logger.info("*** Step 11 Expected Results: - Verify that the Eligibility History (Payor Name, Eligibility Service, Eligibility Check Date and Eligibility Trans ID display properly");
		ArrayList<String> pyrInfoList = daoManagerXifinRpm.getPyrContactInfoFromPyrByPyrAbbrev(pyrAbbrev, testDb);
		String pyrName = pyrInfoList.get(0).trim();
		Assert.assertTrue(accessionDemographics.eligTransPyrNameText().trim().contains(pyrName), "        Eligibility Payor Name: " + pyrName + " should show.");
		
		Assert.assertTrue(accessionDemographics.eligTransEligServiceText().trim().contains("xifin"), "        Eligibility Service: xifin should show.");
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		Assert.assertTrue(accessionDemographics.eligTransEligChkDtText().trim().contains(currDt), "        Eligibility Check Date: " + currDt + " should show.");
		
		ArrayList<String> eligHistInfoList = daoManagerXifinRpm.getEligTransHistFromACCNELIGHISTByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);
		String latestEligTransId = eligHistInfoList.get(3);
		Assert.assertTrue(accessionDemographics.eligTransIdText().equals(latestEligTransId), "        Eligibility Trans ID: " + latestEligTransId + " should show.");
		
		//Can't open the file in a new window. It opens up with the correct url and redirected to a blank page. 
		logger.info("*** Step 11 Expected Results: - Verify that Eligibility Transaction Response PDF file can be opened properly");
		accessionDemographics.eligRespViewBtn().click();	
		switchToPopupWin();

		URL url = new URL(config.getProperty(PropertyMap.RPM_URL)+"/servlets/AccnDemogrphcServlet?COMMAND=ELIG_RESPONSE&eligTransId=" + latestEligTransId);
		Assert.assertFalse(driver.getCurrentUrl().split("=")[1].contains(latestEligTransId), "        PDF file: " + url + " should be opened.");
		
		logger.info("*** Step 12 Actions: - Switch back to the Accession Demographics and Reset the page");
		driver.close();       
        switchToWin(winHandler);
		accessionDemographics = new AccessionDemographics(driver);		
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));				
		
		accessionDemographics.resetBtn().click();	
		driver.close();
    }		

	@Test(priority = 1, description = "Accession Demographics - Ineligible Dependant Eligibility Check with WS")
	@Parameters({"email", "password", "accnId", "pyrAbbrev", "disableBrowserPlugins"})
	public void testRPM_707(String email, String password, String accnId, String pyrAbbrev, String disableBrowserPlugins) throws Exception {
    	logger.info("***** Testing - testRPM_707 *****");    	
		
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
        
        logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
        dataCacheConfiguration = new DataCacheConfiguration(driver);
        Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
        
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame(); 
        
        logger.info("*** Step 5 Actions: - Navigate to Accession Tab");
        headerNavigation.navigateToAccessionTab();
        switchToPopupWin(); 
		winHandler = driver.getWindowHandle();
		
		logger.info("*** Step 6 Expected Results: - The Accession - Demographics page displays");		
		accessionDemographics = new AccessionDemographics(driver);		
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));
		
		logger.info("*** Step 7 Actions: - Enter test accn in Accn ID field and tab out");			
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 7 Expected Results: - Verify that the accn was loaded");
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled.");
				
		logger.info("*** Step 8 Actions: - Check Check Eligibility checkbox and click Submit button");
		selectCheckBox(accessionDemographics.checkEligibilityCheckBox());
		accessionDemographics.submitBtn().click();
		
		logger.info("*** Step 8 Expected Results: - Verify that Ineligible error message was returned");
		Assert.assertTrue(accessionDemographics.ineligibleErrMsgText().trim().contains("Eligibility denied"), "        Transaction Acknowledged. Eligibility denied due to error reported by payor error message should show.");
		
		logger.info("*** Step 9 Actions: - Click the Reset button to not save the ineligible error to the accn");
		accessionDemographics.resetBtn().click();
		
		//CR 48846 - All elig changes should be rolled back when Reset is issued on Accn demo, including the accn_elig_hist entry
		logger.info("*** Step 9 Expected Results: - Verify that the Eligibility History is not saved");
		ArrayList<String> eligHistInfoList = daoManagerXifinRpm.getEligTransHistFromACCNELIGHISTByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);		
		Assert.assertTrue(eligHistInfoList.isEmpty(), "        No eligibility transaction history should be logged.");
		/*
		logger.info("*** Step 10 Actions: - Reload the same accn on Accession - Demographics page");
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 10 Expected Results: - Verify that the accn was loaded");
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled after the accn " + accnId + " was loaded.");
				
		logger.info("*** Step 11 Actions: - Click Show Eligibility History Link");
		accessionDemographics.showEligHistoryLink().click();
		
		logger.info("*** Step 11 Expected Results: - Verify that Eligibility Transaction History table shows");		
		//Assert.assertTrue(isElementPresent(accessionDemographics.eligTransHistoryTable(), 5), "        Eligibility Transaction History table should show.");
		Assert.assertTrue(isElemAttrMatch(accessionDemographics.eligTransHistoryTable(), "style", "display", 15), "        Eligibility Transaction History table should show.");
				
		logger.info("*** Step 12 Actions: - Click Eligibility Trans ID header twice to get the latest Transaction ID");
		accessionDemographics.eligTransIDSortBtn();
		accessionDemographics.eligTransIDSortBtn();
		
		logger.info("*** Step 12 Expected Results: - Verify that the Eligibility History (Payor Name, Eligibility Service, Eligibility Check Date and Eligibility Trans ID display properly");
		ArrayList<String> pyrInfoList = daoManagerXifinRpm.getPyrContactInfoFromPyrByPyrAbbrev(pyrAbbrev, testDb);
		String pyrName = pyrInfoList.get(0).trim();
		Assert.assertTrue(accessionDemographics.eligTransPyrNameText().trim().contains(pyrName), "        Eligibility Payor Name: " + pyrName + " should show.");
		
		Assert.assertTrue(accessionDemographics.eligTransEligServiceText().trim().contains("xifin"), "        Eligibility Service: xifin should show.");
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		Assert.assertTrue(accessionDemographics.eligTransEligChkDtText().trim().contains(currDt), "        Eligibility Check Date: " + currDt + " should show.");
		
		ArrayList<String> eligHistInfoList = daoManagerXifinRpm.getEligTransHistFromACCNELIGHISTByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);
		String latestEligTransId = eligHistInfoList.get(3);
		Assert.assertTrue(accessionDemographics.eligTransIdText().equals(latestEligTransId), "        Eligibility Trans ID: " + latestEligTransId + " should show.");
				
		logger.info("*** Step 12 Expected Results: - Verify that Ineligible Eligibility Status was saved to ACCN_ELIG_HIST table");
		String eligStatusTypId = eligHistInfoList.get(4);
		Assert.assertTrue(eligStatusTypId.equals("2"), "        Eligibility Status Type ID: " + eligStatusTypId + " should be saved to ACCN_ELIG_HIST table.");
				
		logger.info("*** Step 12 Expected Results: - Verify that Eligibility Transaction Response PDF file can be opened properly");
		accessionDemographics.eligRespViewBtn().click();	
		switchToPopupWin();
		
        config = new Configuration(Env.valueOf(env), driver);
        String str = "/servlets/AccnDemogrphcServlet?COMMAND=ELIG_RESPONSE&eligTransId=" + latestEligTransId;
        String url = config.getUrls().concat(str);        
		//Assert.assertFalse(driver.getTitle().contains("Error Open PDF"), "        PDF file: " + url + " should be opened.");	
        Assert.assertFalse(driver.getCurrentUrl().split("=")[1].contains(latestEligTransId), "        PDF file: " + url + " should be opened.");
		
		logger.info("*** Step 13 Actions: - Switch back to the Accession Demographics and Reset the page");
		driver.close();       
        switchToWin(winHandler);
		accessionDemographics = new AccessionDemographics(driver);		
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));				
		
		accessionDemographics.resetBtn().click();	
		*/
		driver.close();
    }	
	
	@Test(priority = 1, description = "Accession Demographics - Subscriber Eligibility Check will update Pt Name")
	@Parameters({"email", "password", "accnId", "pyrAbbrev", "ptLName", "ptFName"})
	public void testRPM_713(String email, String password, String accnId, String pyrAbbrev, String ptLName, String ptFName) throws Exception {
    	logger.info("***** Testing - testRPM_713 *****");    	
		
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
		
		logger.info("*** Step 2 Actions: - Update the Patient name in DB");
		randomCharacter = new RandomCharacter(driver);
		String newPtLName = randomCharacter.getRandomAlphaString(6);
		String newPtFName = randomCharacter.getRandomAlphaString(6);
		
		String colValues = "PT_L_NM = '" + newPtLName + "', PT_F_NM = '" + newPtFName + "'";
		daoManagerXifinRpm.setValuesFromACCNByAccnId(accnId, colValues, testDb);		
		
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
        
        logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
        dataCacheConfiguration = new DataCacheConfiguration(driver);
        Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
        
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
        
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame(); 
        
        logger.info("*** Step 5 Actions: - Navigate to Accession Tab");
        headerNavigation.navigateToAccessionTab();
        switchToPopupWin(); 
		
		logger.info("*** Step 6 Expected Results: - The Accession - Demographics page displays");		
		accessionDemographics = new AccessionDemographics(driver);		
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));
		
		logger.info("*** Step 7 Actions: - Enter test accn in Accn ID field and tab out");			
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 7 Expected Results: - Verify that the accn was loaded");
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled.");
			
		logger.info("*** Step 7 Expected Results: - Verify that the Patient Name was updated");
		Assert.assertTrue(accessionDemographics.ptFNameInput().getAttribute("value").trim().contains(newPtFName), "        Pt First Name should be updated to " + newPtFName);
		Assert.assertTrue(accessionDemographics.ptLNameInput().getAttribute("value").trim().contains(newPtLName), "        Pt Last Name should be updated to " + newPtLName);
				
		logger.info("*** Step 8 Actions: - Check Check Eligibility checkbox and click Submit button");
		selectCheckBox(accessionDemographics.checkEligibilityCheckBox());
		accessionDemographics.submitBtn().click();		
				
		logger.info("*** Step 8 Expected Results: - Verify that Eligibility is Valid message returned");
		Assert.assertTrue(accessionDemographics.eligibleMsgText().trim().contains("Eligibility is Valid. Response: Eligibility check successful"), "        Eligibility is Valid. Response: Eligibility check successful message should show.");
		
		logger.info("*** Step 8 Expected Results: - Verify that Eligible Eligibility status was saved in DB");		
		ArrayList<String> eligStatusList = daoManagerXifinRpm.getEligStatusFromACCNPYRByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);
		//"1" = Eligible
		Assert.assertTrue(eligStatusList.get(0).trim().equals("1"), "        The value in ACCN_PYR.FK_ELIG_STA_TYP_ID should be '1'");
		
		logger.info("*** Step 8 Expected Results: - Verify that Patient name got updated in ACCN table in DB");
		Assert.assertTrue(daoManagerAccnWS.getFnameByAccnId(accnId, testDb).trim().contains(ptFName), "        The Patient First Name in ACCN table should be updated to " + ptFName);
		Assert.assertTrue(daoManagerAccnWS.getLnameByAccnId(accnId, testDb).trim().contains(ptLName), "        The Patient Last Name in ACCN table should be updated to " + ptLName);
		
		logger.info("*** Step 8 Expected Results: - Verify that Patient name got updated in PT_PHI table in DB");
		ArrayList<String> ptPHIInfoList = daoManagerXifinRpm.getPtPHIInfoFromPTPHIByAccnId(accnId, testDb);
		Assert.assertTrue(ptPHIInfoList.get(2).trim().contains(ptFName), "        The Patient First Name in PT_PHI table should be updated to " + ptFName);
		Assert.assertTrue(ptPHIInfoList.get(3).trim().contains(ptLName), "        The Patient lAST Name in PT_PHI table should be updated to " + ptLName);
				
		driver.close();
    }	

	@Test(priority = 1, description = "Accession Demographics - Subscriber Eligibility Check will update Subscriber ID")
	@Parameters({"email", "password", "accnId", "pyrAbbrev", "subsId"})
	public void testRPM_714(String email, String password, String accnId, String pyrAbbrev, String subsId) throws Exception {
    	logger.info("***** Testing - testRPM_714 *****");    	
		
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
		
		logger.info("*** Step 2 Actions: - Update Subscriber ID in both ACCN_PYR and PT_PYR_PHI tables in DB");		
		String newSubsId = "A123456789";	
		String colValues = "SUBS_ID = '" + newSubsId + "'";
		daoManagerXifinRpm.setValuesFromACCNPYRByAccnIdPyrPrio(accnId, colValues, "1", testDb);		
		
		//update subs id in pt_pyr_phi table
		ArrayList<String> ptPHIInfoList = daoManagerXifinRpm.getPtPHIInfoFromPTPHIByAccnId(accnId, testDb);
		String ptSeqId = ptPHIInfoList.get(0);
		ArrayList<String> ptPyrInfoList = daoManagerXifinRpm.getPtPyrInfoFromPTPYRV2BypPtSeqIdPyrPrio(ptSeqId,"1", testDb);
		String effDt = ptPyrInfoList.get(28);
		daoManagerXifinRpm.setValuesFromPTPYRPHIByAccnIdPyrPrioPyrAbbrevEffDt(accnId, colValues, "1", pyrAbbrev, effDt, testDb);
				
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
        
        logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
        dataCacheConfiguration = new DataCacheConfiguration(driver);
        Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
        
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame(); 
        
        logger.info("*** Step 5 Actions: - Navigate to Accession Tab");
        headerNavigation.navigateToAccessionTab();
        switchToPopupWin(); 
		
		logger.info("*** Step 6 Expected Results: - The Accession - Demographics page displays");		
		accessionDemographics = new AccessionDemographics(driver);		
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));
		
		logger.info("*** Step 7 Actions: - Enter test accn in Accn ID field and tab out");	
		accessionDemographics.resetBtn().click();
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 7 Expected Results: - Verify that the accn was loaded");
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled.");
			
		logger.info("*** Step 7 Expected Results: - Verify that the Subscriber ID was updated");
		Assert.assertTrue(accessionDemographics.subsIdInput().getAttribute("value").trim().contains(newSubsId), "        Primary Payor's Subscriber ID should be updated to " + newSubsId);
						
		logger.info("*** Step 8 Actions: - Check Check Eligibility checkbox and click Submit button");
		selectCheckBox(accessionDemographics.checkEligibilityCheckBox());
		accessionDemographics.submitBtn().click();				
		
		logger.info("*** Step 8 Expected Results: - Verify that Eligibility is Valid message returned");
		Assert.assertTrue(accessionDemographics.eligibleMsgText().trim().contains("Eligibility is Valid. Response: Eligibility check successful"), "        Eligibility is Valid. Response: Eligibility check successful message should show.");
		
		logger.info("*** Step 8 Expected Results: - Verify that Eligible Eligibility status was saved in DB");		
		ArrayList<String> eligStatusList = daoManagerXifinRpm.getEligStatusFromACCNPYRByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);
		//"1" = Eligible
		Assert.assertTrue(eligStatusList.get(0).trim().equals("1"), "        The value in ACCN_PYR.FK_ELIG_STA_TYP_ID should be '1'");
		
		logger.info("*** Step 8 Expected Results: - Verify that Primary Payor's subscriber id got updated in ACCN_PYR table in DB");
		ArrayList<String> accnPyrInfoList = daoManagerAccnWS.getPyrAbbrevSubsIdRelshpTypFromAccnPyrByAccnIdPyrPrio(accnId, "1", testDb);		
		Assert.assertTrue(accnPyrInfoList.get(1).trim().contains(subsId), "        The Primary Payor subscriber id in ACCN_PYR table should be updated to " + subsId);
		
		logger.info("*** Step 8 Expected Results: - Verify that Primary Payor's subscriber id got updated in PT_PYR_PHI table in DB");
		ArrayList<String> ptPyrPHIInfoList = daoManagerXifinRpm.getPtPyrPHIInfoFromPTPYRPHIByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);
		Assert.assertTrue(ptPyrPHIInfoList.get(1).trim().contains(subsId), "        The Primary Payor subscriber id in PT_PYR_PHI table should be updated to " + subsId);

		driver.close();
    }	

	@Test(priority = 1, description = "Accession Demographics - Subscriber Eligibility Check will update Relationship")
	@Parameters({"email", "password", "accnId", "pyrAbbrev"})
	public void testRPM_715(String email, String password, String accnId, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_715 *****");    	
		
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
		
		logger.info("*** Step 2 Actions: - Update Relationship in both ACCN_PYR and PT_PYR_V2 tables in DB");	
		//"2" = "spouse"
		String newRelshpId = "2";	
		String colValues1 = "FK_RELSHP_ID = " + newRelshpId;
		String colValues2 = "FK_RELSHP_TYP_ID = " + newRelshpId;
		ArrayList<String> ptPHIInfoList = daoManagerXifinRpm.getPtPHIInfoFromPTPHIByAccnId(accnId, testDb);
		String ptSeqId = ptPHIInfoList.get(0);
		ArrayList<String> ptPyrInfoList = daoManagerXifinRpm.getPtPyrInfoFromPTPYRV2BypPtSeqIdPyrPrio(ptSeqId,"1", testDb);
		String effDt = ptPyrInfoList.get(28);
		daoManagerXifinRpm.setValuesFromACCNPYRByAccnIdPyrPrio(accnId, colValues1, "1", testDb);		
		daoManagerXifinRpm.setValuesFromPTPYRV2ByAccnIdPyrPrioPyrAbbrevEffDt(accnId, colValues2, "1", pyrAbbrev, effDt, testDb);		
						
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
        
        logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
        dataCacheConfiguration = new DataCacheConfiguration(driver);
        Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
        
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame(); 
        
        logger.info("*** Step 5 Actions: - Navigate to Accession Tab");
        headerNavigation.navigateToAccessionTab();
        switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - The Accession - Demographics page displays");		
		accessionDemographics = new AccessionDemographics(driver);		
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));
		
		logger.info("*** Step 7 Actions: - Enter test accn in Accn ID field and tab out");			
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 7 Expected Results: - Verify that the accn was loaded");
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled.");
			
		logger.info("*** Step 7 Expected Results: - Verify that the Relationship was updated");
		Assert.assertTrue(isDropdownItemSelected(accessionDemographics.primaryPyrRlshpDropDown(), "spouse"), "        Primary Payor's Relationship should be updated to 'spouse'.");
						
		logger.info("*** Step 8 Actions: - Check Check Eligibility checkbox and click Submit button");
		selectCheckBox(accessionDemographics.checkEligibilityCheckBox());
		accessionDemographics.submitBtn().click();			
		
		logger.info("*** Step 8 Expected Results: - Verify that Eligibility is Valid message returned");
		Assert.assertTrue(accessionDemographics.eligibleMsgText().trim().contains("Eligibility is Valid. Response: Eligibility check successful"), "        Eligibility is Valid. Response: Eligibility check successful message should show.");
		
		logger.info("*** Step 8 Expected Results: - Verify that Eligible Eligibility status was saved in DB");		
		ArrayList<String> eligStatusList = daoManagerXifinRpm.getEligStatusFromACCNPYRByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);
		//"1" = Eligible
		Assert.assertTrue(eligStatusList.get(0).trim().equals("1"), "        The value in ACCN_PYR.FK_ELIG_STA_TYP_ID should be '1'");
		
		logger.info("*** Step 8 Expected Results: - Verify that Primary Payor's relationship got updated in ACCN_PYR table in DB");	
		//"3" = child
		String updatedRlspId = "3";
		ArrayList<String> accnPyrInfoList = daoManagerAccnWS.getPyrAbbrevSubsIdRelshpTypFromAccnPyrByAccnIdPyrPrio(accnId, "1", testDb);		
		Assert.assertTrue(accnPyrInfoList.get(3).trim().contains("child"), "        The Primary Payor relationship in ACCN_PYR table should be updated to child.");
		
		logger.info("*** Step 8 Expected Results: - Verify that Primary Payor's relationship got updated in PT_PYR_V2 table in DB");
		ptPyrInfoList = daoManagerXifinRpm.getPtPyrInfoFromPTPYRV2BypPtSeqIdPyrPrio(ptSeqId, "1", testDb);
		Assert.assertTrue(ptPyrInfoList.get(3).trim().equals(updatedRlspId), "        The Primary Payor relationship in PT_PYR_V2 table should be updated to " + updatedRlspId);

		driver.close();
    }	

	@Test(priority = 1, description = "Accession Demographics - Eligible Roster Eligibility Check")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_708(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_708 *****");    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession with EPI via Accession WS");		
	  	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null);    	
		
		//accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		//String accnId = accessionUtils.createAccnWithEPI(this, env, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, endpoint);	
    	switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		
		logger.info("*** Step 17 Actions: -  Navigate to File Maintenance Tab");
		headerNavigation = new HeaderNavigation(driver, config);
        headerNavigation.navigateToFileMaintenanceTab();
        switchToPopupWin();  
     
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
        
        logger.info("*** Step 18 Actions: - Navigate to the System Data Cache Page");  
        fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
        fileMaintenaceNavigation.navigateToSystemDataCacheLink();
        switchToPopupWin();               
        
        logger.info("*** Step 18 Expected Results: - The Data Cache Configuration page displays");
        dataCacheConfiguration = new DataCacheConfiguration(driver);
        Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
        
        logger.info("*** Step 19 Actions: - Turn on Perform Eligibility Census Checking in DB");
		String clnAbbrev = daoManagerXifinRpm.getClientAbbrevIdByAccnId(accnId, testDb);
		String epi = daoManagerPatientPortal.getEpiByAccnId(accnId, testDb);
		ArrayList<String> clnAcctInfoList = daoManagerClientPortal.getClientNameAccountTypeByClientId(clnAbbrev);
		String clnName = clnAcctInfoList.get(0);
		
		String colValues = "B_CHECK_ELIG = 1"; 
		daoManagerXifinRpm.setValuesFromCLNByClnAbbrev(clnAbbrev, colValues, testDb);
		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
        
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();   
			
		logger.info("*** Step 21 Actions: - Navigate to Client Tab");		
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 21 Actions: - Navigate to Client - Eligibility Census Configuration page");
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 21 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 22 Actions: - Load the Client in Client ID field and tab out");
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 22 Expected Results: - Verify that the Client Name is loaded properly");			
		Assert.assertEquals(eligCensusConfig.nameInput().getAttribute("value").trim(), clnName);
		
		logger.info("*** Step 23 Actions: - Set Date Roster is Valid Through and tab out");
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		eligCensusConfig.setRosterValidThroughDt(currDt);
		
		logger.info("*** Step 24 Actions: - Set EPI and tab out");
		eligCensusConfig.setEPI(epi);
		
		logger.info("*** Step 25 Actions: - Click Add Row button");
		eligCensusConfig.addRowBtn().click();
		
		logger.info("*** Step 26 Actions: - Enter the Eff Date for the new Eligibility Roster");
		eligCensusConfig.setNewEffDt(currDt);
		
		logger.info("*** Step 27 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		eligCensusConfig.resetBtn().click();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 28 Actions: - Navigate to Accession tab Accession Demographics page");
        accessionNavigation = new AccessionNavigation(driver, config);
        accessionNavigation.navigateToDemographicsLink();
        switchToPopupWin();
        
		logger.info("*** Step 28 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 29 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 29 Expected Results: - Verify patient First Name is correct when accession data loads");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));  
        		
		logger.info("*** Step 30 Actions: - Check Check Eligibility checkbox and click Submit button");
		selectCheckBox(accessionDemographics.checkEligibilityCheckBox());
		accessionDemographics.submitBtn().click();		
		
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, false), "        The Submit button should be disabled.");
		
		logger.info("*** Step 30 Expected Results: - Verify that Eligibility is Valid message returned");
		Assert.assertTrue(accessionDemographics.eligibleMsgText().trim().contains("Eligibility is Valid for census"), "         Eligibility is Valid for census message should show.");
		
		logger.info("*** Step 31 Actions: - Reload the same accn in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 31 Expected Results: - Verify patient First Name is correct when accession data loads");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb)); 
		
		logger.info("*** Step 31 Expected Results: - Verify that the Primary Payor on the accession should be updated to C (Client)");
		Assert.assertTrue(accessionDemographics.payorIdInput().getAttribute("value").trim().contains("C"), "        Primary Payor should be changed to C (Client).");
        
		logger.info("*** Step 32 Actions: - Click Show Eligibility History Link");
		accessionDemographics.showEligHistoryLink().click();
		
		logger.info("*** Step 32 Expected Results: - Verify that No Eligibility Transactions History should show");			
		//Assert.assertTrue(isElementPresent(accessionDemographics.eligTransHistoryTable(), 5), "        Eligibility Transaction History table should show.");
		Assert.assertTrue(isElemAttrMatch(accessionDemographics.eligTransHistoryTable(), "style", "display", 15), "        Eligibility Transaction History table should show.");
		Assert.assertTrue(accessionDemographics.emptyEligTransHistText().trim().contains("No results"), "        No Eligibility Transactions History should show.");
		
		logger.info("*** Step 33 Actions: - Click Reset button in Accession Demographic page");
		accessionDemographics.resetBtn().click();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 34 Actions: - Navigate to Client - Eligibility Census Configuration page");
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 35 Actions: - Navigate to Client - Eligibility Census Configuration page");		
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 35 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");			
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 36 Actions: - Load the Client in Client ID field and tab out");
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 36 Expected Results: - Verify that the Client Name is loaded properly");			
		Assert.assertEquals(eligCensusConfig.nameInput().getAttribute("value").trim(), clnName);
		
		logger.info("*** Step 37 Actions: - Clear the Date Roster is Valid Through Input field");
		eligCensusConfig.rosterValidThroughDateInput().clear();
		
		logger.info("*** Step 38 Actions: - Reload the same EPI in the EPI Input field");
		eligCensusConfig.setEPI(epi);
		
		logger.info("*** Step 39 Actions: - Check the Delete checkbox for selected Patient Eligibility Roster");
		selectCheckBox(eligCensusConfig.deleteCheckBox());
		
		logger.info("*** Step 40 Actions: - Click Submit and Reset buttons");
		eligCensusConfig.submitBtn().click();
		eligCensusConfig.resetBtn().click();		
		
		logger.info("*** Step 41 Actions: - Turn off Perform Eligibility Census Checking in DB");
		colValues = "B_CHECK_ELIG = 0"; 
		daoManagerXifinRpm.setValuesFromCLNByClnAbbrev(clnAbbrev, colValues, testDb);
		
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
        
        logger.info("*** Step 42 Actions: - Navigate to File Maintenance Tab");
        headerNavigation.navigateToFileMaintenanceTab();
        switchToPopupWin();        
        
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

        logger.info("*** Step 43 Actions: - Navigate to System Data Cache page");
        fileMaintenaceNavigation.navigateToSystemDataCacheLink();
        switchToPopupWin();               
        
        logger.info("*** Step 43 Expected Results: - The Data Cache Configuration page displays");       
        Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");


		xifinAdminUtils.clearDataCache();
        	
		driver.close();
	}
	
	@Test(priority = 1, description = "Accession Demographics - Ineligible Roster Eligibility Check")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_709(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_709 *****");    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession with EPI via Accession WS");		
	  	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null);
    	switchToDefaultWinFromFrame();    	
		//accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		//String accnId = accessionUtils.createAccnWithEPI(this, env, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, endpoint);	
			
		String winHandler = driver.getWindowHandle();
		
		logger.info("*** Step 17 Actions: -  Navigate to File Maintenance Tab");
		headerNavigation = new HeaderNavigation(driver, config);
        headerNavigation.navigateToFileMaintenanceTab();
        switchToPopupWin();  
     
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
        
        logger.info("*** Step 18 Actions: - Navigate to the System Data Cache Page");  
        fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
        fileMaintenaceNavigation.navigateToSystemDataCacheLink();
        switchToPopupWin();               
        
        logger.info("*** Step 18 Expected Results: - The Data Cache Configuration page displays");
        dataCacheConfiguration = new DataCacheConfiguration(driver);
        Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
        
        logger.info("*** Step 19 Actions: - Turn on Perform Eligibility Census Checking in DB");
		String clnAbbrev = daoManagerXifinRpm.getClientAbbrevIdByAccnId(accnId, testDb);
		String epi = daoManagerPatientPortal.getEpiByAccnId(accnId, testDb);
		ArrayList<String> clnAcctInfoList = daoManagerClientPortal.getClientNameAccountTypeByClientId(clnAbbrev);
		String clnName = clnAcctInfoList.get(0);
		
		String colValues = "B_CHECK_ELIG = 1"; 
		daoManagerXifinRpm.setValuesFromCLNByClnAbbrev(clnAbbrev, colValues, testDb);

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
        
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();   
			
		logger.info("*** Step 21 Actions: - Navigate to Client Tab");		
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 21 Actions: - Navigate to Client - Eligibility Census Configuration page");
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 21 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 22 Actions: - Load the Client in Client ID field and tab out");
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 22 Expected Results: - Verify that the Client Name is loaded properly");			
		Assert.assertEquals(eligCensusConfig.nameInput().getAttribute("value").trim(), clnName);
		
		logger.info("*** Step 23 Actions: - Set Date Roster is Valid Through and tab out");
		timeStamp = new TimeStamp(driver);
		String prevDt = timeStamp.getPreviousDate("MM/dd/yyyy", 10);
		eligCensusConfig.setRosterValidThroughDt(prevDt);
		
		logger.info("*** Step 24 Actions: - Set EPI and tab out");
		eligCensusConfig.setEPI(epi);
		
		logger.info("*** Step 25 Actions: - Click Add Row button");
		eligCensusConfig.addRowBtn().click();
		
		logger.info("*** Step 26 Actions: - Enter the Eff Date for the new Eligibility Roster");
		eligCensusConfig.setNewEffDt(prevDt);
		
		logger.info("*** Step 27 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		eligCensusConfig.resetBtn().click();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 28 Actions: - Navigate to Accession tab Accession Demographics page");
        accessionNavigation = new AccessionNavigation(driver, config);
        accessionNavigation.navigateToDemographicsLink();
        switchToPopupWin();
        
		logger.info("*** Step 28 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 29 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 29 Expected Results: - Verify patient First Name is correct when accession data loads");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));  
        		
		logger.info("*** Step 30 Actions: - Check Check Eligibility checkbox and click Submit button");
		selectCheckBox(accessionDemographics.checkEligibilityCheckBox());
		accessionDemographics.submitBtn().click();				
		
		logger.info("*** Step 30 Expected Results: - Verify that Eligibility not found message returned");
		Assert.assertTrue(accessionDemographics.eligibleMsgText().trim().contains("Eligibility not found"), "         Eligibility not found message should show.");

		logger.info("*** Step 31 Actions: - Click Reset button in Accession Demographic page");
		accessionDemographics.resetBtn().click();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 32 Actions: - Navigate to Client - Eligibility Census Configuration page");
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 33 Actions: - Navigate to Client - Eligibility Census Configuration page");		
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 33 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");			
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 34 Actions: - Load the Client in Client ID field and tab out");
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 34 Expected Results: - Verify that the Client Name is loaded properly");			
		Assert.assertEquals(eligCensusConfig.nameInput().getAttribute("value").trim(), clnName);
		
		logger.info("*** Step 35 Actions: - Clear the Date Roster is Valid Through Input field");
		eligCensusConfig.rosterValidThroughDateInput().clear();
		
		logger.info("*** Step 36 Actions: - Reload the same EPI in the EPI Input field");
		eligCensusConfig.setEPI(epi);
		
		logger.info("*** Step 37 Actions: - Check the Delete checkbox for selected Patient Eligibility Roster");
		selectCheckBox(eligCensusConfig.deleteCheckBox());
		
		logger.info("*** Step 38 Actions: - Click Submit and Reset buttons");
		eligCensusConfig.submitBtn().click();
		eligCensusConfig.resetBtn().click();		
		
		logger.info("*** Step 39 Actions: - Turn off Perform Eligibility Census Checking in DB");
		colValues = "B_CHECK_ELIG = 0"; 
		daoManagerXifinRpm.setValuesFromCLNByClnAbbrev(clnAbbrev, colValues, testDb);
		
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
        
        logger.info("*** Step 40 Actions: - Navigate to File Maintenance Tab");
        headerNavigation.navigateToFileMaintenanceTab();
        switchToPopupWin();        
        
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

        logger.info("*** Step 41 Actions: - Navigate to System Data Cache page");
        fileMaintenaceNavigation.navigateToSystemDataCacheLink();
        switchToPopupWin();               
        
        logger.info("*** Step 42 Expected Results: - The Data Cache Configuration page displays");       
        Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");


		xifinAdminUtils.clearDataCache();
        	
		driver.close();
	}

	@Test(priority = 1, description = "Accession Demographics - Subscriber Eligibility Check that translates the payor")
	@Parameters({"email", "password", "accnId", "pyrAbbrev", "translatedPyrAbbrev", "disableBrowserPlugins"})
	public void testRPM_716(String email, String password, String accnId, String pyrAbbrev, String translatedPyrAbbrev, String disableBrowserPlugins) throws Exception {
    	logger.info("***** Testing - testRPM_716 *****");    	
		
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
		
		logger.info("*** Step 2 Actions: - Remove existing Accnession Payor Delete History for the accession in DB");
		daoManagerXifinRpm.deleteAccnPyrDelHistFromACCNPYRDELByAccnId(accnId, testDb);
		
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
        
        logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
        dataCacheConfiguration = new DataCacheConfiguration(driver);
        Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
        
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame(); 
        
        logger.info("*** Step 5 Actions: - Navigate to Accession Tab");
        headerNavigation.navigateToAccessionTab();
        switchToPopupWin();  
		winHandler = driver.getWindowHandle();
		
		logger.info("*** Step 6 Expected Results: - The Accession - Demographics page displays");		
		accessionDemographics = new AccessionDemographics(driver);		
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));
		
		logger.info("*** Step 7 Actions: - Enter test accn in Accn ID field and tab out");			
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 7 Expected Results: - Verify that the accn was loaded");		
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled after the accn " + accnId + " was loaded.");
		
		logger.info("*** Step 8 Actions: - Change the Accession Primary Payor back to the original Payor and click Submit button");	
		accessionDemographics.setPayorId(pyrAbbrev);
		accessionDemographics.submit();
		accessionDemographics.reset();
		
		logger.info("*** Step 9 Actions: - Enter test accn in Accn ID field and tab out");
		accessionDemographics.searchAccnId(accnId);
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled after the accn " + accnId + " was loaded.");
				
		logger.info("*** Step 10 Actions: - Check Check Eligibility checkbox and click Submit button");
		selectCheckBox(accessionDemographics.checkEligibilityCheckBox());
		accessionDemographics.submitBtn().click();			
		
		logger.info("*** Step 10 Expected Results: - Verify that Eligibility is Valid message returned");
		Assert.assertTrue(accessionDemographics.eligibleMsgText().trim().contains("Eligibility is Valid. Response: Eligibility check successful"), "        Eligibility is Valid. Response: Eligibility check successful message should show.");
		
		logger.info("*** Step 10 Expected Results: - Verify that Eligible Eligibility status for Translated Payor was saved in DB");	
		ArrayList<String> eligStatusList = daoManagerXifinRpm.getEligStatusFromACCNPYRByAccnIdPyrAbbrev(accnId, translatedPyrAbbrev, testDb);
		//"1" = Eligible
		Assert.assertTrue(eligStatusList.get(0).trim().equals("1"), "        The value in ACCN_PYR.FK_ELIG_STA_TYP_ID should be '1'");
		
		logger.info("*** Step 11 Actions: - Reload the same accn on Accession - Demographics page");
		accessionDemographics.resetBtn().click();
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 11 Expected Results: - Verify that the accn was loaded");
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled after the accn " + accnId + " was loaded.");
				
		logger.info("*** Step 12 Actions: - Click Show Eligibility History Link");
		accessionDemographics.showEligHistoryLink().click();
		
		logger.info("*** Step 12 Expected Results: - Verify that Eligibility Transaction History table shows");		
		//Assert.assertTrue(isElementPresent(accessionDemographics.eligTransHistoryTable(), 15), "        Eligibility Transaction History table should show.");		
		Assert.assertTrue(isElemAttrMatch(accessionDemographics.eligTransHistoryTable(), "style", "display", 15), "        Eligibility Transaction History table should show.");
		
		logger.info("*** Step 13 Actions: - Click Eligibility Trans ID header twice to get the latest Transaction ID");
		accessionDemographics.eligTransIDSortBtn();
		accessionDemographics.eligTransIDSortBtn();
		
		logger.info("*** Step 13 Expected Results: - Verify that the Eligibility History (Payor Name, Eligibility Service, Eligibility Check Date and Eligibility Trans ID) for the original Payor display properly");
		ArrayList<String> pyrInfoList = daoManagerXifinRpm.getPyrContactInfoFromPyrByPyrAbbrev(pyrAbbrev, testDb);
		String pyrName = pyrInfoList.get(0).trim();
		Assert.assertTrue(accessionDemographics.eligTransPyrNameText().trim().contains(pyrName), "        Eligibility Payor Name: " + pyrName + " should show.");
		
		Assert.assertTrue(accessionDemographics.eligTransEligServiceText().trim().contains("xifin"), "        Eligibility Service: xifin should show.");
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		Assert.assertTrue(accessionDemographics.eligTransEligChkDtText().trim().contains(currDt), "        Eligibility Check Date: " + currDt + " should show.");
		
		ArrayList<String> eligHistInfoList = daoManagerXifinRpm.getEligTransHistFromACCNELIGHISTByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);
		String latestEligTransId = eligHistInfoList.get(3);
		Assert.assertTrue(accessionDemographics.eligTransIdText().equals(latestEligTransId), "        Eligibility Trans ID: " + latestEligTransId + " should show.");
				
		logger.info("*** Step 13 Expected Results: - Verify that Eligibility Transaction Response PDF file can be opened properly");
		accessionDemographics.eligRespViewBtn().click();	
		switchToPopupWin();

		URL url = new URL(config.getProperty(PropertyMap.RPM_URL)+"/servlets/AccnDemogrphcServlet?COMMAND=ELIG_RESPONSE&eligTransId=" + latestEligTransId);
		Assert.assertFalse(driver.getCurrentUrl().split("=")[1].contains(latestEligTransId), "        PDF file: " + url + " should be opened.");
		
		logger.info("*** Step 14 Actions: - Switch back to the Accession Demographics and Reset the page");
		driver.close();       
        switchToWin(winHandler);
		accessionDemographics = new AccessionDemographics(driver);		
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));	
		
		accessionDemographics.resetBtn().click();	
		driver.close();
    }	

	@Test(priority = 1, description = "Accession Demographics - Subscriber Eligibility Check that translates & adds Q_ELIG")
	@Parameters({"email", "password", "accnId", "pyrAbbrev", "translatedPyrAbbrev"})
	public void testRPM_717(String email, String password, String accnId, String pyrAbbrev, String translatedPyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_717 *****");    	
		
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
		daoManagerXifinRpm.deleteEligHistFromACCNELIGHISTByAccnIdPyrAbbrev(accnId, translatedPyrAbbrev, testDb);
		
		logger.info("*** Step 2 Actions: - Remove existing Accnession Payor Delete History for the accession in DB");
		daoManagerXifinRpm.deleteAccnPyrDelHistFromACCNPYRDELByAccnId(accnId, testDb);
		
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
        
        logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
        dataCacheConfiguration = new DataCacheConfiguration(driver);
        Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
        
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame(); 
        
        logger.info("*** Step 5 Actions: - Navigate to Accession Tab");
        headerNavigation.navigateToAccessionTab();
        switchToPopupWin(); 
		winHandler = driver.getWindowHandle();
		
		logger.info("*** Step 6 Expected Results: - The Accession - Demographics page displays");		
		accessionDemographics = new AccessionDemographics(driver);		
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));
		
		logger.info("*** Step 7 Actions: - Enter test accn in Accn ID field and tab out");			
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 7 Expected Results: - Verify that the accn was loaded");		
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled after the accn " + accnId + " was loaded.");
		
		logger.info("*** Step 8 Actions: - Change the Accession Primary Payor back to the original Payor and click Submit button");	
		accessionDemographics.setPayorId(pyrAbbrev);
		accessionDemographics.submit();
		accessionDemographics.reset();
		
		logger.info("*** Step 9 Actions: - Enter test accn in Accn ID field and tab out");
		accessionDemographics.searchAccnId(accnId);
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled after the accn " + accnId + " was loaded.");
		
		logger.info("*** Step 10 Actions: - Check Check Eligibility checkbox and click Submit button");
		selectCheckBox(accessionDemographics.checkEligibilityCheckBox());
		accessionDemographics.submitBtn().click();			
		
		logger.info("*** Step 10 Expected Results: - Verify that Eligibility is Valid message returned");
		Assert.assertTrue(accessionDemographics.eligibleMsgText().trim().contains("Eligibility is Valid. Response: Eligibility check successful"), "        Eligibility is Valid. Response: Eligibility check successful message should show.");
		
		logger.info("*** Step 10 Expected Results: - Verify that Eligible Eligibility status for Translated Payor was saved in DB");	
		ArrayList<String> eligStatusList = daoManagerXifinRpm.getEligStatusFromACCNPYRByAccnIdPyrAbbrev(accnId, translatedPyrAbbrev, testDb);
		//"1" = Eligible
		Assert.assertTrue(eligStatusList.get(0).trim().equals("1"), "        The value in ACCN_PYR.FK_ELIG_STA_TYP_ID should be '1'");
		
		logger.info("*** Step 10 Expected Results: - Verify that the accn was pushed into Q_ELIG table");	
		ArrayList<String> qEligInfoList = daoManagerXifinRpm.getQEligInfoFromQELIGByAccnId(accnId, testDb);
		Assert.assertFalse(qEligInfoList.isEmpty(), "        The Accession " + accnId + " should be in Q_ELIG.");		
		
		logger.info("*** Step 11 Actions: - Reload the same accn on Accession - Demographics page");
		accessionDemographics.resetBtn().click();
		accessionDemographics.searchAccnId(accnId);
		
		logger.info("*** Step 11 Expected Results: - Verify that the accn was loaded");
		Assert.assertTrue(isElementEnabled(accessionDemographics.submitBtn(), 5, true), "        The Submit button should be enabled after the accn " + accnId + " was loaded.");
				
		logger.info("*** Step 12 Actions: - Click Show Eligibility History Link");
		accessionDemographics.showEligHistoryLink().click();
		
		logger.info("*** Step 12 Expected Results: - Verify that Eligibility Transaction History table shows");		
		//Assert.assertTrue(isElementPresent(accessionDemographics.eligTransHistoryTable(), 5), "        Eligibility Transaction History table should show.");	
		Assert.assertTrue(isElemAttrMatch(accessionDemographics.eligTransHistoryTable(), "style", "display", 15), "        Eligibility Transaction History table should show.");
		
		logger.info("*** Step 13 Actions: - Click Eligibility Trans ID header twice to get the latest Transaction ID");
		accessionDemographics.eligTransIDSortBtn();
		Thread.sleep(1000);
		accessionDemographics.eligTransIDSortBtn();
		Thread.sleep(2000);
		
		logger.info("*** Step 13 Expected Results: - Verify that the Eligibility History (Payor Name, Eligibility Service, Eligibility Check Date and Eligibility Trans ID) for the original Payor display properly");
		ArrayList<String> pyrInfoList = daoManagerXifinRpm.getPyrContactInfoFromPyrByPyrAbbrev(pyrAbbrev, testDb);
		String pyrName = pyrInfoList.get(0).trim();
		Assert.assertTrue(accessionDemographics.eligTransPyrNameText().trim().contains(pyrName), "        Eligibility Payor Name: " + pyrName + " should show.");
		
		Assert.assertTrue(accessionDemographics.eligTransEligServiceText().trim().contains("xifin"), "        Eligibility Service: xifin should show.");
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		Assert.assertTrue(accessionDemographics.eligTransEligChkDtText().trim().contains(currDt), "        Eligibility Check Date: " + currDt + " should show.");
		
		ArrayList<String> eligHistInfoList = daoManagerXifinRpm.getEligTransHistFromACCNELIGHISTByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);
		String latestEligTransId = eligHistInfoList.get(3);
		Assert.assertTrue(accessionDemographics.eligTransIdText().equals(latestEligTransId), "        Eligibility Trans ID: " + latestEligTransId + " should show.");
		
		//Can't open the file in a new window. It opens up with the correct url and redirected to a blank page. 
		logger.info("*** Step 13 Expected Results: - Verify that Eligibility Transaction Response PDF file can be opened properly");
		accessionDemographics.eligRespViewBtn().click();	
		switchToPopupWin();

		URL url = new URL(config.getProperty(PropertyMap.RPM_URL)+"/servlets/AccnDemogrphcServlet?COMMAND=ELIG_RESPONSE&eligTransId=" + latestEligTransId);
		Assert.assertFalse(driver.getTitle().contains("Error Open PDF"), "        PDF file: " + url + " should be opened.");				
		
		logger.info("*** Step 14 Actions: - Switch back to the Accession Demographics");
		driver.close();       
        switchToWin(winHandler);
		accessionDemographics = new AccessionDemographics(driver);		
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));			
		
		logger.info("*** Step 15 Actions: - Change the Accession Primary Payor back to the original Payor and click Submit button");		
		accessionDemographics.setPayorId(pyrAbbrev);		
		accessionDemographics.submitBtn().click();
		
		accessionDemographics.resetBtn().click();	
		driver.close();
    }	
	
	
}
