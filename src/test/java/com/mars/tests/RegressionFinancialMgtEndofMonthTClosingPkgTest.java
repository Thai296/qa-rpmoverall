package com.mars.tests;


//import java.io.File;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;

import com.overall.accession.accessionProcessing.AccessionDemographics;
import com.overall.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
import com.overall.fileMaintenance.sysMgt.DataCacheConfiguration;
import com.overall.fileMaintenance.sysMgt.TaskScheduler;
import com.overall.fileMaintenance.sysMgt.TaskStatus;
import com.overall.headerNavigation.HeaderNavigation;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TestDataSetup;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

//import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
//import com.overall.accession.accessionNavigation.AccessionNavigation;
//import com.overall.accession.accessionProcessing.AccessionSearch;
//import com.overall.accession.accessionProcessing.AccessionSingleStatement;
//import com.overall.accession.accessionProcessing.AccessionTransactionDetail;
//import com.overall.fileMaintenance.orderProcessingConfig.FileMaintencePatternDefinition;
//import com.overall.help.Help;
//import com.overall.accession.orderProcessing.AccnTestUpdateOld;
//import com.overall.payor.payorDemographics.GroupDemographics;
//import com.overall.payor.payorDemographics.PricingConfig;
//import com.overall.payor.payorNavigation.PayorNavigation;
//import com.overall.accession.accessionProcessing.superSearch.SuperSearch;
//import com.overall.accession.accessionProcessing.superSearch.SuperSearchResults;
//import com.xifin.utils.TimeStamp;



public class RegressionFinancialMgtEndofMonthTClosingPkgTest extends SeleniumBaseTest  {
	
	private SsoLogin ssoLogin;
	private HeaderNavigation headerNavigation;
//	private Help help;
//	private AccessionNavigation accessionNavigation;
//	private SuperSearch superSearch;
//	private SuperSearchResults superSearchResults;
	private AccessionDemographics accessionDemographics;
//	private AccessionSingleStatement accessionStatement;
//	private AccessionTransactionDetail accessionTransactionDetail;
//	private AccessionSearch accessionSearchPopup;
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
//	private TimeStamp timeStamp;
	
	
	
	@Test(priority = 1, description = "RPM - Accession - Accession Demographics - Update Accession - Add EPI")
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
		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//Create accession
		logger.info("*** Step 6 - Send request to create an accession");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null);
    	
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

		xifinAdminUtils.clearDataCache();
		
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
		
		//Go to Accession page
		logger.info("*** Step 17 - Navigate to Accession Tab");
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		
		//Get Accession Demographic windowHandle during popup
		logger.info("*** Step 18 - Load the accn data by entering the accnId in the accnId input field and tab");
		//String popupHandles = switchToPopupWin("c");
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
		//popupHandles = switchToPopupWin();
		
		//Ensure Create New Patient popup displays
		Assert.assertTrue(isElementPresent(accessionDemographics.createNewPatientPopupOkBtn(),5));
		
		//Add EPI to accession
		logger.info("*** Step 20 - Add the epi");
		accessionDemographics.addEpi();
		
		//Load data for accnId and switch to the Accession Search popup window
		logger.info("*** Step 21 - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);
		
		//Verify EPI is associated to the accession
		Assert.assertEquals(accessionDemographics.epiInput().getAttribute("value"), daoManagerPatientPortal.getEpiByAccnId(accnId, testDb));
    }
	
	
	
}
