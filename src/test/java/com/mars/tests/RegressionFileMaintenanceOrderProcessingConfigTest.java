package com.mars.tests;


//import java.io.File;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;

import com.overall.accession.accessionNavigation.AccessionNavigation;
import com.overall.accession.orderProcessing.AccnTestUpdateOld;
import com.overall.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
import com.overall.fileMaintenance.orderProcessingConfig.FileMaintencePatternDefinition;
import com.overall.fileMaintenance.sysMgt.DataCacheConfiguration;
import com.overall.fileMaintenance.sysMgt.TaskScheduler;
import com.overall.fileMaintenance.sysMgt.TaskStatus;
import com.overall.headerNavigation.HeaderNavigation;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

//import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
//import com.overall.accession.accessionProcessing.AccessionDemographics;
//import com.overall.accession.accessionProcessing.AccessionSearch;
//import com.overall.accession.accessionProcessing.AccessionSingleStatement;
//import com.overall.accession.accessionProcessing.AccessionTransactionDetail;
//import com.overall.help.Help;
//import com.overall.payor.payorDemographics.GroupDemographics;
//import com.overall.payor.payorDemographics.PricingConfig;
//import com.overall.payor.payorNavigation.PayorNavigation;
//import com.overall.accession.accessionProcessing.superSearch.SuperSearch;
//import com.overall.accession.accessionProcessing.superSearch.SuperSearchResults;



public class RegressionFileMaintenanceOrderProcessingConfigTest extends SeleniumBaseTest  {

	private SsoLogin ssoLogin;
	private HeaderNavigation headerNavigation;
	//private Help help;
	private AccessionNavigation accessionNavigation;
//	private SuperSearch superSearch;
//	private SuperSearchResults superSearchResults;
//	private AccessionDemographics accessionDemographics;
//	private AccessionSingleStatement accessionStatement;
//	private AccessionTransactionDetail accessionTransactionDetail;
//	private AccessionSearch accessionSearchPopup;
	private AccnTestUpdateOld accnTestUpdate;
	private TestDataSetup testDataSetup;
	private FileMaintenanceNavigation fileMaintenaceNavigation;
//	private PayorNavigation payorNavigation;
//	private GroupDemographics groupDemographics;
//	private PricingConfig pricingConfig;
	private DataCacheConfiguration dataCacheConfiguration;
	private TaskScheduler taskScheduler;
	private TaskStatus taskStatus;
	private FileMaintencePatternDefinition fileMaintencePatternDefinition;
	private TimeStamp timeStamp;



	@Test(priority = 1, description = "RPM - Accession - Transaction Detail - Update Accession - Add Modifer")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "mod"})
	public void testRPM_32(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String mod) throws Exception {
    	logger.info("*** Testing - testRPM_32 ***");

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

		//Go to Accn Test Update page
		logger.info("*** Step 19 - Navigate to Accn Test Update");
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToAccnTestUpdateLinkLink();
		switchToPopupWin();

		//Load the accn data
		logger.info("*** Step 20 - Load the accn data by entering the accnId in the accnId input field and tab");
		accnTestUpdate = new AccnTestUpdateOld(driver);
		Assert.assertTrue(isElementPresent(accnTestUpdate.accnIdInput(),5));
		accnTestUpdate.searchAccnId(accnId);

		//Verify patient Name is correct when accession data loads
		String pName = daoManagerPatientPortal.getLnameByAccnId(accnId, testDb);
		pName = pName + ", " + daoManagerPatientPortal.getFnameByAccnId(accnId, testDb);

		Assert.assertEquals(accnTestUpdate.ptNameInput(pName).getAttribute("value"), pName);

		//Add Modifier to ordered Test Details
		logger.info("*** Step 21 - Add Modifier for the ordered Test Details to the Accession");
		accnTestUpdate.selectOrderTestDetailMod(this, mod);

		//Load the accn data
		logger.info("*** Step 22 - Load the accn data by entering the accnId in the accnId input field and tab");
		Assert.assertTrue(isElementPresent(accnTestUpdate.accnIdInput(),5));
		accnTestUpdate.searchAccnId(accnId);

		//Verify the error The accession is currently posting displays
		Assert.assertEquals(accnTestUpdate.accessionErrorsText().getText(), "The accession is currently posting.");

		//check that the accn is in the q_oe_data table
		logger.info("*** Step 23 - Ensure that the accn is in the q_oe_data table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQOeDataByQOeSeq(daoManagerXifinRpm.getSeqIdFromQOEByAccnId(accnId, testDb), testDb), 1);

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

    	//Ensure accession is in Q_FR_Pending table
    	logger.info("*** Step 24 - Ensure the new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);

    	//Update Q_FR_Pending table so accession can be released
    	logger.info("*** Step 25 - Ensure the new accession in the Q_FR_Pending table can be released");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);

    	//Go to File Maintenance Tab
    	logger.info("*** Step 26 - Navigate to File Maintenance Page");
    	headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

    	//Go to System Data Cache page
    	logger.info("*** Step 27 - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();

		//Clear the Cache
		logger.info("*** Step 28 - Clear the Cache");
		xifinAdminUtils.clearDataCache();

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//Go to Task Scheduler
		logger.info("*** Step 29 - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		//Reset All Task Scheduler Engines
		logger.info("*** Step 30 - Reset All Task Scheduler Engines");
		taskScheduler = new TaskScheduler(driver);
		taskScheduler.resetAllTaskSchedulerConcurrency();

		//Set Task Scheduler for OE Posting Engine
		logger.info("*** Step 31 - Set Task Scheduler for OE Posting Engine");
		taskScheduler.setTaskScheduler("1", "now", 12);

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		///Go to Task Status
		logger.info("*** Step 32 - Navigate to Task Status");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();

		//Ensure Task Status for OE Posting Engine is completed
		logger.info("*** Step 33 - Ensure Task Status for OE Posting Engine is completed");
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//Ensure accession is in Q_FR_Pending table
    	logger.info("*** Step 34 - Ensure the new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);

    	//Update Q_FR_Pending table so accession can be released
    	logger.info("*** Step 35 - Ensure the new accession in the Q_FR_Pending table can be released");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);

    	//Go to File Maintenance Tab
    	logger.info("*** Step 36 - Navigate to File Maintenance Page");
    	headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

    	//Go to System Data Cache page
    	logger.info("*** Step 37 - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();

		//Clear the Cache
		logger.info("*** Step 38 - Clear the Cache");
		xifinAdminUtils.clearDataCache();
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//Go to Task Scheduler
		logger.info("*** Step 39 - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();

		//Reset All Task Scheduler Engines
		logger.info("*** Step 40 - Reset All Task Scheduler Engines");
		taskScheduler = new TaskScheduler(driver);
		taskScheduler.resetAllTaskSchedulerConcurrency();

		//Set Task Scheduler for OE Posting Engine
		logger.info("*** Step 41 - Set Task Scheduler for OE Posting Engine");
		taskScheduler.setTaskScheduler("1", "now", 12);

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		///Go to Task Status
		logger.info("*** Step 42 - Navigate to Task Status");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();

		//Ensure Task Status for OE Posting Engine is completed
		logger.info("*** Step 43 - Ensure Task Status for OE Posting Engine is completed");
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));

		//Wait for Task Scheduler OE Posting Engine to complete then verify accession is in the Q_Price table
		logger.info("*** Step 44 - Ensure the new accession is in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//Go to Accession page
		logger.info("*** Step 45 - Navigate to Accession Tab");
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//Go to Accn Test Update page
		logger.info("*** Step 46 - Navigate to Accn Test Update");
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToAccnTestUpdateLinkLink();
		switchToPopupWin();

		//Load the accn data
		logger.info("*** Step 47 - Load the accn data by entering the accnId in the accnId input field and tab");
		accnTestUpdate = new AccnTestUpdateOld(driver);
		Assert.assertTrue(isElementPresent(accnTestUpdate.accnIdInput(),5));
		accnTestUpdate.searchAccnId(accnId);

		//Verify patient Name is correct when accession data loads
		Assert.assertEquals(accnTestUpdate.ptNameInput(pName).getAttribute("value"), pName);

		//Verify the Modifier is associated to the accession
		Assert.assertEquals(accnTestUpdate.orderTestDetailsMod1Text().getAttribute("value"), daoManagerXifinRpm.getMod1IdFromAccnTestByAccnId(accnId, testDb));
    }

	@Test(priority = 1, description = "RPM - File Maintenance - Pattern Definition - Add New Pattern Button Exists")
	@Parameters({"email", "password"})
	public void testRPM_68(String email, String password) throws Exception {
    	logger.info("*** Testing - testRPM_68 ***");

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
		logger.info("*** Step 2 - Navigate to the File Maintenance Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//Go to Order Processing Coinfig Pattern Definitiion page
		logger.info("*** Step 3 - Navigate to Pattern Definition Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToPatternDefinitionLink();
		switchToPopupWin();

		//Ensure Add New Pattern button exist
		fileMaintencePatternDefinition = new FileMaintencePatternDefinition(driver);
		Assert.assertTrue(isElementPresent(fileMaintencePatternDefinition.addNewPatternBtn(),5));
    }

	@Test(priority = 1, description = "RPM - File Maintenance - Pattern Definition - Submit New Pattern Definition")
	@Parameters({"email", "password", "patternDefDescr", "patternDefRegex"})
	public void testRPM_69(String email, String password, String patternDefDescr, String patternDefRegex) throws Exception {
    	logger.info("*** Testing - testRPM_69 ***");

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
		logger.info("*** Step 2 - Navigate to the File Maintenance Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//Go to Order Processing Coinfig Pattern Definitiion page
		logger.info("*** Step 3 - Navigate to Pattern Definition Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToPatternDefinitionLink();
		switchToPopupWin();

		//Use Reset for workaround to unlock screen and then Add New Pattern Definition
		logger.info("*** Step 4 - Submit a New Pattern Definition");
		fileMaintencePatternDefinition = new FileMaintencePatternDefinition(driver);
		fileMaintencePatternDefinition.resetPatternDefinition();

		timeStamp = new TimeStamp(driver);
		String tStamp = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
		patternDefDescr = patternDefDescr + tStamp;
		patternDefRegex = patternDefRegex + tStamp;
		fileMaintencePatternDefinition.setPatternDefinition(patternDefDescr, patternDefRegex);

		//Ensure the Pattern Definition Descr gets added
		Assert.assertEquals(fileMaintencePatternDefinition.getPatternDescr(0), patternDefDescr);

		//Ensure the Pattern Definition Regex gets added
		Assert.assertEquals(fileMaintencePatternDefinition.getPatternRegex(0), patternDefRegex);
    }

	@Test(priority = 1, description = "RPM - File Maintenance - Pattern Definition - Reset Added Pattern Definition")
	@Parameters({"email", "password", "patternDefDescr", "patternDefRegex"})
	public void testRPM_70(String email, String password, String patternDefDescr, String patternDefRegex) throws Exception {
    	logger.info("*** Testing - testRPM_70 ***");

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
		logger.info("*** Step 2 - Navigate to the File Maintenance Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//Go to Order Processing Coinfig Pattern Definitiion page
		logger.info("*** Step 3 - Navigate to Pattern Definition Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToPatternDefinitionLink();
		switchToPopupWin();

		//Use Reset for workaround to unlock screen and then Enter a New Pattern Definition
		logger.info("*** Step 4 - Enter a New Pattern Definition");
		fileMaintencePatternDefinition = new FileMaintencePatternDefinition(driver);
		fileMaintencePatternDefinition.resetPatternDefinition();

		timeStamp = new TimeStamp(driver);
		String tStamp = timeStamp.getCurrentDate("yyyyMMddHHmmssSS");
		patternDefDescr = patternDefDescr + tStamp;
		patternDefRegex = patternDefRegex + tStamp;
		fileMaintencePatternDefinition.setPatternDefinitionNoSubmit(patternDefDescr, patternDefRegex);

		//Reset Pattern Definitiion
		logger.info("*** Step 5 - Reset the Pattern Definitiion");
		fileMaintencePatternDefinition.resetBtn();

		//Ensure the Pattern Definition Descr is removed
		Assert.assertNotEquals(fileMaintencePatternDefinition.getPatternDescr(1), patternDefDescr);

		//Ensure the Pattern Definition Regex is removed
		Assert.assertNotEquals(fileMaintencePatternDefinition.getPatternRegex(1), patternDefRegex);
    }


}
