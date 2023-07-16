package com.mars.tests;

/*
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.net.URL;
*/

import com.overall.accession.accessionNavigation.AccessionNavigation;
import com.overall.accession.accessionProcessing.AccessionDemographics;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.accession.accessionProcessing.LoadAccession;
import com.overall.accession.accessionProcessing.superSearch.SuperSearch;
import com.overall.accession.accessionProcessing.superSearch.SuperSearchResults;
import com.overall.client.clientNavigation.ClientNavigation;
import com.overall.client.clientProcessing.ClientContactManager;
import com.overall.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
import com.overall.fileMaintenance.sysMgt.DataCacheConfiguration;
import com.overall.fileMaintenance.sysMgt.TaskScheduler;
import com.overall.fileMaintenance.sysMgt.TaskStatus;
import com.overall.headerNavigation.HeaderNavigation;
import com.overall.help.Help;
import com.overall.menu.MenuNavigation;
import com.overall.payor.payorDemographics.PayorContactManager;
import com.overall.payor.payorNavigation.PayorNavigation;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TestDataSetup;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

//import com.overall.accession.accessionProcessing.AccessionSearch;
//import com.overall.accession.accessionProcessing.AccessionSingleStatement;
//import com.xifin.accnws.dao.DaoManagerAccnWS;
//import com.xifin.clientportal.dao.DaoManagerClientPortal;
//import com.xifin.mars.dao.DaoManagerXifinRpm;
//import com.xifin.patientportal.dao.DaoManagerPatientPortal;
//import com.xifin.platform.dao.DaoManagerPlatform;
//import com.xifin.sso.dao.DaoManagerSSO;
//import com.xifin.utils.RandomCharacter;
//import com.xifin.utils.TimeStamp;
//import com.xifinportal.menu.Navigation;



public class SmokeTest extends SeleniumBaseTest  {

	private HeaderNavigation headerNavigation;
	private Help help;
	private AccessionNavigation accessionNavigation;
	private SuperSearch superSearch;
	private SuperSearchResults superSearchResults;
	private AccessionDemographics accessionDemographics;
//	private AccessionSingleStatement accessionStatement;
//	private AccessionSearch accessionSearch;
	private TestDataSetup testDataSetup;
	private FileMaintenanceNavigation fileMaintenaceNavigation;
	private ClientNavigation  clientNavigation;
	private PayorNavigation  payorNavigation;
	private ClientContactManager clientContactManager;
	private PayorContactManager payorContactManager;
	private DataCacheConfiguration dataCacheConfiguration;
	private TaskScheduler taskScheduler;
	private TaskStatus taskStatus;
	private XifinAdminUtils xifinAdminUtils;
	private MenuNavigation menuNavigation;
	private AccessionDetail accessionDetail;
	private LoadAccession loadAccession;

	@BeforeMethod(alwaysRun = true)
	@Parameters({"ssoUsername", "ssoPassword"})
	public void beforeMethod(String ssoUsername, String ssoPassword, Method method)
	{
		try
		{
			logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));

			logIntoSso(ssoUsername, ssoPassword);

		}
		catch (Exception e)
		{
			logger.error("Error running BeforeMethod", e);
		}
	}
	@Test(priority = 1, description = "RPM - Task Scheduler - OE Posting Engine - Accession in Q_Price Table")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "xapEnv", "eType", "engConfigDB"})
	public void testRPM_24(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String xapEnv, String eType, String engConfigDB) throws Exception {
    	logger.info("*** Testing - testRPM_24 ***"); 
    	
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		headerNavigation = new HeaderNavigation(driver, config);
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		taskScheduler = new TaskScheduler(driver); 		
		
		logger.info("*** Step 1 Actions: - Turn off system setting for Price Engine");
    	Assert.assertEquals(daoManagerXifinRpm.setSysSettingsPriceEngine(testDb), 6);
    	daoManagerPlatform.setValuesFromTable("cln", "CLN.B_BILLING_ASSIGNMENT_REQ =0, CLN.B_CLN_PRC_SUSP_THRU_EOM = 0", "cln_abbrev not like 'AUTO%' and (CLN.B_BILLING_ASSIGNMENT_REQ =1 or CLN.B_CLN_PRC_SUSP_THRU_EOM = 1)", testDb);
		
		logger.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");				
		xifinAdminUtils.clearDataCache();
		Thread.sleep(5000);

		//Create accession
		logger.info("*** Step 3 Actions: - Send request to create an accession");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null); 
    	
    	logger.info("*** Step 3 Expected Results: - Verify that the new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1, "       Accession ID " + accnId + " should in Q_FR_PENDING table.");
    	 
		logger.info("*** Step 4 Actions: - Run PF-OE Posting Engine twice");		
	  	xifinAdminUtils.runPFEngineInCurrentWindow(this, email, password, xapEnv, eType, engConfigDB, true);
    	Thread.sleep(5000);     	
    	
	  	xifinAdminUtils.runPFEngineInCurrentWindow(this, email, password, xapEnv, eType, engConfigDB, true);
    	Thread.sleep(5000);				

		logger.info("*** Step 4 Expected Results: - Verify that the new accession is in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);

		driver.close(); 
    }
    
    @Test(priority = 1, description = "RPM - Task Scheduler - Pricing Engine - Generate Pricing")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "xapEnv", "eType", "engConfigDB"})
	public void testRPM_22(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String xapEnv, String eType, String engConfigDB) throws Exception {
    	logger.info("*** Testing - testRPM_22 ***");

		xifinAdminUtils = new XifinAdminUtils(driver, config);
		headerNavigation = new HeaderNavigation(driver, config);
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		taskScheduler = new TaskScheduler(driver); 		
		
		logger.info("*** Step 1 Actions: - Turn off system setting for Price Engine");
    	Assert.assertEquals(daoManagerXifinRpm.setSysSettingsPriceEngine(testDb), 6);
    	daoManagerPlatform.setValuesFromTable("cln", "CLN.B_BILLING_ASSIGNMENT_REQ =0, CLN.B_CLN_PRC_SUSP_THRU_EOM = 0", "cln_abbrev not like 'AUTO%' and (CLN.B_BILLING_ASSIGNMENT_REQ =1 or CLN.B_CLN_PRC_SUSP_THRU_EOM = 1)", testDb);
		
		logger.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");				
		xifinAdminUtils.clearDataCache();
		Thread.sleep(5000);

		//Create accession
		logger.info("*** Step 3 Actions: - Send request to create an accession");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null); 
    	
    	logger.info("*** Step 3 Expected Results: - Verify that the new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1, "       Accession ID " + accnId + " should in Q_FR_PENDING table.");
    	 
		logger.info("*** Step 4 Actions: - Run PF-OE Posting Engine twice");		
	  	xifinAdminUtils.runPFEngineInCurrentWindow(this, email, password, xapEnv, eType, engConfigDB, true);
    	Thread.sleep(5000);     	
    	
	  	xifinAdminUtils.runPFEngineInCurrentWindow(this, email, password, xapEnv, eType, engConfigDB, true);
    	Thread.sleep(5000);				

		logger.info("*** Step 4 Expected Results: - Verify that the new accession is in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);
		
		logger.info("*** Step 5 Actions: - Navigate to RPM");
		menuNavigation.navigateToRPM();
		
		//logger.info("*** Step 1 - Log into Mars with username and password");
		//ssoLogin = new SsoLogin(driver);
		//ssoLogin.login(email, password);		
		logger.info("*** Step 5 Expected Results: - Verify that the user is logged into RPM successfully");				
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 6 Actions: - Navigate to RPM File Maintenance tab"); 		
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 6 Actions: - Navigate to Task Scheduler page");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();		
		
		logger.info("*** Step 7 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();			
		
		logger.info("*** Step 8 Actions: - Set Task Scheduler for RPM Pricing Engine");
		taskScheduler.setTaskScheduler("2", "now", 10);
		
		//Go to main page
		driver.close();
		this.switchToWin(winHandler);
		this.switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 9 Actions: - Navigate to Task Status page");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		this.switchToPopupWin();		
		
		logger.info("*** Step 9 Expected Results: - Ensure Task Status for Pricing Engine is completed");
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineActive("Pricing Engine"));
		
		//Go to main page
		driver.close();
		this.switchToWin(winHandler);
		this.switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 10 Actions: - Navigate to Task Scheduler page");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		this.switchToPopupWin();		
		
		logger.info("*** Step 11 Actions: - Reset All Task Scheduler Engines");		
		taskScheduler.resetAllTaskSchedulerConcurrency();
		
		//sleep for 10 sec
		Thread.sleep(10000);
		
		logger.info("*** Step 12 Actions: - Ensure the new accession is NOT in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 0);
		
		//Verify the accn statusId is either 21 or 51 to ensure the accn is priced
		logger.info("*** Step 12 Expected Results: - Ensure the new accession is Priced");
		Assert.assertTrue(daoManagerXifinRpm.getStatusIdByAccnId(accnId, testDb) == 21 || daoManagerXifinRpm.getStatusIdByAccnId(accnId, testDb) == 51);	
		
		driver.close();
    }
    
    @Test(priority = 1, description = "RPM - Task Scheduler - Non-Client Submission Engine - Accession in Q_Accn_Subm_Stage table")
	@Parameters({"email", "password", "customer", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "xapEnv", "eType", "engConfigDB"})
	public void testRPM_23(String email, String password, String customer, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String xapEnv, String eType, String engConfigDB) throws Exception {
    	logger.info("*** Testing - testRPM_23 ***"); 
    	
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		headerNavigation = new HeaderNavigation(driver, config);
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		taskScheduler = new TaskScheduler(driver); 		
		
		logger.info("*** Step 1 Actions: - Turn off system setting for Price Engine");
    	Assert.assertEquals(daoManagerXifinRpm.setSysSettingsPriceEngine(testDb), 6);
    	daoManagerPlatform.setValuesFromTable("cln", "CLN.B_BILLING_ASSIGNMENT_REQ =0, CLN.B_CLN_PRC_SUSP_THRU_EOM = 0", "cln_abbrev not like 'AUTO%' and (CLN.B_BILLING_ASSIGNMENT_REQ =1 or CLN.B_CLN_PRC_SUSP_THRU_EOM = 1)", testDb);
		
		logger.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");				
		xifinAdminUtils.clearDataCache();
		Thread.sleep(5000);

		//Create accession
		logger.info("*** Step 3 Actions: - Send request to create an accession");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null); 
    	
    	logger.info("*** Step 3 Expected Results: - Verify that the new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1, "       Accession ID " + accnId + " should in Q_FR_PENDING table.");
    	 
		logger.info("*** Step 4 Actions: - Run PF-OE Posting Engine twice");		
	  	xifinAdminUtils.runPFEngineInCurrentWindow(this, email, password, xapEnv, eType, engConfigDB, true);
    	Thread.sleep(5000);     	
    	
	  	xifinAdminUtils.runPFEngineInCurrentWindow(this, email, password, xapEnv, eType, engConfigDB, true);
    	Thread.sleep(5000);				

		logger.info("*** Step 4 Expected Results: - Verify that the new accession is in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);
		
		logger.info("*** Step 5 Actions: - Navigate to RPM");
		menuNavigation.navigateToRPM();
		
		//logger.info("*** Step 1 - Log into Mars with username and password");
		//ssoLogin = new SsoLogin(driver);
		//ssoLogin.login(email, password);		
		logger.info("*** Step 5 Expected Results: - Verify that the user is logged into RPM successfully");				
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 6 Actions: - Navigate to RPM File Maintenance tab"); 		
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 6 Actions: - Navigate to Task Scheduler page");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();		
		
		logger.info("*** Step 7 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();			
		
		logger.info("*** Step 8 Actions: - Set Task Scheduler for RPM Pricing Engine");
		taskScheduler.setTaskScheduler("2", "now", 10);
		
		//Go to main page
		driver.close();
		this.switchToWin(winHandler);
		this.switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 9 Actions: - Navigate to Task Status page");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		this.switchToPopupWin();		
		
		logger.info("*** Step 9 Expected Results: - Ensure Task Status for Pricing Engine is completed");
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineActive("Pricing Engine"));
		
		//Go to main page
		driver.close();
		this.switchToWin(winHandler);
		this.switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 10 Actions: - Navigate to Task Scheduler page");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		this.switchToPopupWin();		
		
		logger.info("*** Step 11 Actions: - Reset All Task Scheduler Engines");		
		taskScheduler.resetAllTaskSchedulerConcurrency();
		
		//sleep for 10 sec
		Thread.sleep(10000);
		
		logger.info("*** Step 12 Actions: - Ensure the new accession is NOT in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 0);
		
		//Verify the accn statusId is either 21 or 51 to ensure the accn is priced
		logger.info("*** Step 12 Expected Results: - Ensure the new accession is Priced");
		Assert.assertTrue(daoManagerXifinRpm.getStatusIdByAccnId(accnId, testDb) == 21 || daoManagerXifinRpm.getStatusIdByAccnId(accnId, testDb) == 51);		
		
		//Verify the accn is in submission queue
		logger.info("*** Step 12 Expected Results: - Ensure the accn is in submission queue");
		Assert.assertTrue(daoManagerXifinRpm.getQtypByAccnId(accnId, testDb) > 0);
		
		//Get submSvcSeqId
		logger.info("*** Step 13 Actions: - Get the submSvcSeqId");
		int submSvcSeqId = daoManagerXifinRpm.getSubmSvcSeqIdByAccnId(accnId, testDb);
		
		//Verify Submission Service is ready to process
		logger.info("*** Step 14 Actions: - Ensure the Submission Service is ready to process");
		Assert.assertEquals(daoManagerXifinRpm.setSeqIdProcessBySubmSvcSeqId(submSvcSeqId, testDb), 1);
		daoManagerXifinRpm.setLastSubmDtFromSUBMSVCBySubmSvcSeqId(submSvcSeqId, testDb);//Added on 04/14/2016

		//Verify SequenceID is not ready to process
		logger.info("*** Step 15 Actions: - Ensure the SequenceID is not ready to process");
		Assert.assertNotNull(daoManagerXifinRpm.setSeqIdNotProcessBySubmSvcSeqId(submSvcSeqId, testDb));

		//Verify all other accns will be submitted to same submission service to a future date (2mins queuery or longer)
		logger.info("*** Step 16 Actions: - Ensure all other accns will be submitted to same submission service to a future date");
		Assert.assertNotNull(daoManagerXifinRpm.setNextSubmDtBySubmSvcSeqIdAccnId(submSvcSeqId, accnId, testDb));
		
		logger.info("*** Step 17 Actions: - Clear System Cache in Xifin Admin Portal");				
		xifinAdminUtils.clearDataCache();
		Thread.sleep(5000);		

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Verify the Non Client Submission Engine Pre Processors is stopped
		logger.info("*** Step 18 Actions: - Ensure the Non Client Submission Engine Pre Processors is stopped");
		Assert.assertTrue(daoManagerXifinRpm.setBactiveByTaskTypId(7, testDb) > 0);

		//Navigate to Task Scheduler
		logger.info("*** Step 19 Actions: - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();
		
		//Reset All Task Scheduler Engines
		logger.info("*** Step 20 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();
		
		//Set Task Scheduler for Submission Engine 
		logger.info("*** Step 21 Actions: - Set Task Scheduler for Non-Client Submission Engine");
		taskScheduler.setTaskScheduler("7", "now", 1);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		///Go to Task Status
		logger.info("*** Step 22 Actions: - Navigate to Task Status");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		
		//Ensure Task Status for Non-Client Submission Engine is completed
		logger.info("*** Step 23 Actions: - Ensure Task Status for Non-Client Submission Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("Non-Client Submission Engine", 80000));
		
		//Ensure the accn is in subm stage table
		logger.info("*** Step 23 Expected Results: - Ensure the accn is in subm stage table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQAccnSubmStageTableByAccnId(accnId, testDb), 1);
		
		driver.close(); 
    }
    
    @Test(priority = 1, description = "RPM - Task Scheduler - Non-Client Stmt Engine - Generate Non-Client Stmt")
	@Parameters({"email", "password", "customer", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "xapEnv", "eType", "engConfigDB"})
	public void testRPM_25(String email, String password, String customer, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String xapEnv, String eType, String engConfigDB) throws Exception {
    	logger.info("*** Testing - testRPM_25 ***");
 
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		headerNavigation = new HeaderNavigation(driver, config);
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		taskScheduler = new TaskScheduler(driver); 		
		
		logger.info("*** Step 1 Actions: - Turn off system setting for Price Engine");
    	Assert.assertEquals(daoManagerXifinRpm.setSysSettingsPriceEngine(testDb), 6);
    	daoManagerPlatform.setValuesFromTable("cln", "CLN.B_BILLING_ASSIGNMENT_REQ =0, CLN.B_CLN_PRC_SUSP_THRU_EOM = 0", "cln_abbrev not like 'AUTO%' and (CLN.B_BILLING_ASSIGNMENT_REQ =1 or CLN.B_CLN_PRC_SUSP_THRU_EOM = 1)", testDb);
		
		logger.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");				
		xifinAdminUtils.clearDataCache();
		Thread.sleep(5000);

		//Create accession
		logger.info("*** Step 3 Actions: - Send request to create an accession");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("AccnID: " + accnId);
    	Assert.assertTrue(accnId != null); 
    	
    	logger.info("*** Step 3 Expected Results: - Verify that the new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1, "       Accession ID " + accnId + " should in Q_FR_PENDING table.");
    	 
		logger.info("*** Step 4 Actions: - Run PF-OE Posting Engine twice");		
	  	xifinAdminUtils.runPFEngineInCurrentWindow(this, email, password, xapEnv, eType, engConfigDB, true);
    	Thread.sleep(5000);     	
    	
	  	xifinAdminUtils.runPFEngineInCurrentWindow(this, email, password, xapEnv, eType, engConfigDB, true);
    	Thread.sleep(5000);				

		logger.info("*** Step 4 Expected Results: - Verify that the new accession is in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);
		
		logger.info("*** Step 5 Actions: - Navigate to RPM");
		menuNavigation.navigateToRPM();
		
		//logger.info("*** Step 1 - Log into Mars with username and password");
		//ssoLogin = new SsoLogin(driver);
		//ssoLogin.login(email, password);		
		logger.info("*** Step 5 Expected Results: - Verify that the user is logged into RPM successfully");				
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 6 Actions: - Navigate to RPM File Maintenance tab"); 		
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 6 Actions: - Navigate to Task Scheduler page");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();		
		
		logger.info("*** Step 7 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();			
		
		logger.info("*** Step 8 Actions: - Set Task Scheduler for RPM Pricing Engine");
		taskScheduler.setTaskScheduler("2", "now", 10);
		
		//Go to main page
		driver.close();
		this.switchToWin(winHandler);
		this.switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 9 Actions: - Navigate to Task Status page");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		this.switchToPopupWin();		
		
		logger.info("*** Step 9 Expected Results: - Ensure Task Status for Pricing Engine is completed");
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineActive("Pricing Engine"));
		
		//Go to main page
		driver.close();
		this.switchToWin(winHandler);
		this.switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 10 Actions: - Navigate to Task Scheduler page");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		this.switchToPopupWin();		
		
		logger.info("*** Step 11 Actions: - Reset All Task Scheduler Engines");		
		taskScheduler.resetAllTaskSchedulerConcurrency();
		
		//sleep for 10 sec
		Thread.sleep(10000);
		
		logger.info("*** Step 12 Actions: - Ensure the new accession is NOT in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 0);
		
		//Verify the accn statusId is either 21 or 51 to ensure the accn is priced
		logger.info("*** Step 12 Expected Results: - Ensure the new accession is Priced");
		Assert.assertTrue(daoManagerXifinRpm.getStatusIdByAccnId(accnId, testDb) == 21 || daoManagerXifinRpm.getStatusIdByAccnId(accnId, testDb) == 51);		
		
		//Verify the accn is in submission queue
		logger.info("*** Step 12 Expected Results: - Ensure the accn is in submission queue");
		Assert.assertTrue(daoManagerXifinRpm.getQtypByAccnId(accnId, testDb) > 0);
		
		//Get submSvcSeqId
		logger.info("*** Step 13 Actions: - Get the submSvcSeqId");
		int submSvcSeqId = daoManagerXifinRpm.getSubmSvcSeqIdByAccnId(accnId, testDb);
		
		//Verify Submission Service is ready to process
		logger.info("*** Step 14 Actions: - Ensure the Submission Service is ready to process");
		Assert.assertEquals(daoManagerXifinRpm.setSeqIdProcessBySubmSvcSeqId(submSvcSeqId, testDb), 1);
		daoManagerXifinRpm.setLastSubmDtFromSUBMSVCBySubmSvcSeqId(submSvcSeqId, testDb);//Added on 04/14/2016

		//Verify SequenceID is not ready to process
		logger.info("*** Step 15 Actions: - Ensure the SequenceID is not ready to process");
		Assert.assertNotNull(daoManagerXifinRpm.setSeqIdNotProcessBySubmSvcSeqId(submSvcSeqId, testDb));

		//Verify all other accns will be submitted to same submission service to a future date (2mins queuery or longer)
		logger.info("*** Step 16 Actions: - Ensure all other accns will be submitted to same submission service to a future date");
		Assert.assertNotNull(daoManagerXifinRpm.setNextSubmDtBySubmSvcSeqIdAccnId(submSvcSeqId, accnId, testDb));
		
		logger.info("*** Step 17 Actions: - Clear System Cache in Xifin Admin Portal");				
		xifinAdminUtils.clearDataCache();
		Thread.sleep(5000);		

		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Verify the Non Client Submission Engine Pre Processors is stopped
		logger.info("*** Step 18 Actions: - Ensure the Non Client Submission Engine Pre Processors is stopped");
		Assert.assertTrue(daoManagerXifinRpm.setBactiveByTaskTypId(7, testDb) > 0);

		//Navigate to Task Scheduler
		logger.info("*** Step 19 Actions: - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();
		
		//Reset All Task Scheduler Engines
		logger.info("*** Step 20 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();
		
		//Set Task Scheduler for Submission Engine 
		logger.info("*** Step 21 Actions: - Set Task Scheduler for Non-Client Submission Engine");
		taskScheduler.setTaskScheduler("7", "now", 1);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		///Go to Task Status
		logger.info("*** Step 22 Actions: - Navigate to Task Status");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		
		//Ensure Task Status for Non-Client Submission Engine is completed
		logger.info("*** Step 23 Actions: - Ensure Task Status for Non-Client Submission Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("Non-Client Submission Engine", 80000));
		
		//Ensure the accn is in subm stage table
		logger.info("*** Step 23 Expected Results: - Ensure the accn is in subm stage table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQAccnSubmStageTableByAccnId(accnId, testDb), 1);		
		
		//Get submFileSeqId
		logger.info("*** Step 24 Actions: - Get the submFileSeqId");
		int submFileSeqId = daoManagerXifinRpm.getSubMFileSeqIdByAccnId(accnId, testDb);
		
		//Ensure the the specified file only gets processed for the SubMFileBEgateProcessed
		logger.info("*** Step 24 Expected Results: - Ensure the the specified file only gets processed for the SubMFileBEgateProcessed");
		Assert.assertTrue(daoManagerXifinRpm.setSubMFileBEgateProcessedBySubMFileSeqId(submFileSeqId, testDb) > -1);    	
    	
		logger.info("*** Step 25 Actions: - Clear System Cache in Xifin Admin Portal");				
		xifinAdminUtils.clearDataCache();
		Thread.sleep(5000);			
		
		driver.close();   	
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Get the filename for the submFileSeqId
		logger.info("*** Step 26 Actions: - Get the submFileSeqId");
		String filename = daoManagerXifinRpm.getFilenameBySubMFileSeqId(submFileSeqId, testDb);
		
		//Navigate to Task Scheduler
		logger.info("*** Step 27 Actions: - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();
		
		//Reset All Task Scheduler Engines
		logger.info("*** Step 28 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();
		
		//Set Task Scheduler for Non-Client Statement Engine 
		logger.info("*** Step 29 Actions: - Set Task Scheduler for RPM Non-Client Statement Engine");
		taskScheduler.setTaskScheduler("8", "now", 1);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		///Go to Task Status
		logger.info("*** Step 30 Actions: - Navigate to Task Status page ");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		
		//Ensure Task Status for Non-Client Statement Engine is completed
		logger.info("*** Step 30 Expected Results: - Ensure Task Status for Non-Client Statement Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("Non-Client Statement Engine", 50000));
		
		//Ensure the file gets processed successfully for submFileSeqId
		logger.info("*** Step 30 Expected Results: - Ensure the file gets processed successfully for submFileSeqId");
		Assert.assertEquals(daoManagerXifinRpm.getBEgateProcessedBySubMFileSeqId(submFileSeqId, filename, testDb), 1);
		
		driver.close();
    }
    
	@Test(priority = 1, description = "RPM - Login")
	@Parameters({"email", "password"})
	public void testRPM_18(String email, String password) throws Exception {
		logger.info("*** Testing - testRPM_18 ***");

		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		driver.close();
	}
	
	@Test(priority = 1, description = "RPM - Help")
	@Parameters({"email", "password"})
	public void testRPM_20(String email, String password) throws Exception {
		logger.info("*** Testing - testRPM_20 ***");
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 - Click Help link");
		//Go to Help page
		switchToDefaultWinFromFrame();
		help = new Help(driver, config);
		help.navigateToHelp();
		
		//Verify the Help pad displays
		switchToPopupWin();
		//Assert.assertEquals(help.helpTitle(),"Help Frame");
		Assert.assertTrue(help.helpTitle().contains("Help Frame"), "        Help Frame title should show.");
		driver.close();
		
	}
	
	@Test(priority = 1, description = "RPM - Accession Processing - Super Search - Accession Client Abbrev")
	@Parameters({"email", "password"})
	public void testRPM_21(String email, String password) throws Exception {
		logger.info("*** Testing - testRPM_21 ***");

		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 - Click Accession Processing -> Super Search Link");
		//Go to Super Search page
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToSuperSearch();
		
		logger.info("*** Step 3 - Select Accession in Filter 1, enter accessionId in Filter 1 value input and click submit");
		//Search for accnId
		superSearch = new SuperSearch(driver);
		switchToPopupWin();
		Assert.assertTrue(isElementPresent(superSearch.searchIdInput(),5));
		
		//Get random accnId that is not EP and not obsolete but is a payor
		String accnId = daoManagerXifinRpm.getRandomAccnIdForSuperSearch(testDb);
		superSearch.superSearchAccn(this, accnId);
		
		//Switch to popup 
		switchToPopupWin();
		superSearchResults = new SuperSearchResults(driver); 
		Assert.assertTrue(isElementPresent(superSearchResults.clientIdText(),5));
		
		//Get clientId by accnId
		String clientId = daoManagerXifinRpm.getClientIdByAccnId(accnId, testDb);
		
		//Verify the accnId contains the correct clientAbbrev in the results
		Assert.assertEquals(superSearchResults.clientIdText().getText(), daoManagerXifinRpm.getClientAbbrevIdByClientId(clientId, testDb));
		driver.close();
	}
		
	@Test(priority = 1, description = "RPM - Accession - Accession Demographics - Load Existing Accession")
	@Parameters({"email", "password"})
	public void testRPM_2(String email, String password) throws Exception {
		logger.info("*** Testing - testRPM_2 ***");
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Accession page
		logger.info("*** Step 2 - Navigate to Accession Tab");
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		
		//Get Accession Demographic windowHandle during popup
		logger.info("*** Step 2 - Enter Accn ID in the Accn ID input field and tab");
		String popupHandles = switchToPopupWin("c");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));
		
		//Load data for accnId and switch to the Accession Search popup window
		String accnId = daoManagerXifinRpm.getPricedAccession(testDb);
		accessionDemographics.searchAccnId(accnId);
		//switchToPopupWin();
		//accessionSearch = new AccessionSearch(driver);
		
		//Close popup
		//accessionSearch.closePopup();
		
		//Switch back to the Accession Demographic window
		//switchToWin(popupHandles);
		
		//Verify patient address 1 is correct when accession data loads
		Assert.assertTrue(isElementPresent(accessionDemographics.ptAddr1Input(),5));
		Assert.assertEquals(accessionDemographics.ptAddr1Input().getAttribute("value"), daoManagerPatientPortal.getAddr1FromAccnByAccnId(accnId, testDb));
		driver.close();
	}
	
	@Test(priority = 1, description = "EP Setup")
	@Parameters({"email", "password"})
	public void setupUpdateEp(String email, String password) throws Exception {		
    	logger.info("***** Testing - EP Setup *****");
    	
    	String accnId = System.getProperty("accnId");

		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		//Assert.assertEquals(headerNavigation.marsCustomerText().getText(), "acme");
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
		logger.info("*** Step 3 Action: - Turn off sys setting for Price Engine and subm_file table");
    	Assert.assertEquals(daoManagerXifinRpm.setSysSettingsPriceEngine(testDb), 6);
      	//Set B_EGATE_PROCESSED = 0 in subm_file table for PK_SUBM_FILE_SEQ_ID = 0
    	Assert.assertEquals(daoManagerXifinRpm.setBEGateProcessedFROMSUBMFILEBySubmFileSeqId("0", testDb), 1);

		//Go to System Data Cache page
		logger.info("*** Step 4 - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();
		
		//Clear the Cache
		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

    	//Ensure accession is in Q_FR_Pending table
    	logger.info("*** Step 7 - Ensure the new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	//Update Q_FR_Pending table so accession can be released
    	logger.info("*** Step 8 - Ensure the new accession in the Q_FR_Pending table can be released");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);

		xifinAdminUtils.clearDataCache();
//
		
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
		
		//Navigate to Task Scheduler
		logger.info("*** Step 17 - Navigate to Task Scheduler");		
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();
		
		//Reset All Task Scheduler Engines
		logger.info("*** Step 18 - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();
		
		//Set Task Scheduler for Pricing Engine 
		logger.info("*** Step 19 - Set Task Scheduler for Pricing Engine");
		taskScheduler.setTaskScheduler("2", "now", 10);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		///Go to Task Status
		logger.info("*** Step 20 - Navigate to Task Status");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		
		//Ensure Task Status for Pricing Engine is completed
		logger.info("*** Step 21 - Ensure Task Status for Pricing Engine is completed");
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineActive("Pricing Engine"));
		
		//Wait for Task Scheduler Pricing Engine to complete then verify accessionIn is not in the Q_Price table
		logger.info("*** Step 22 - Ensure the new accession is not in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 0);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		
		//Get Accession Demographic windowHandle during popup
		logger.info("*** Step 23 - Switch to Accn Demo jsp  and enter Accn ID in the Accn ID input field and tab");
		//Switch to Accn Demo page
		String popupHandles = switchToPopupWin("c");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertTrue(isElementPresent(accessionDemographics.accnIdInput(),5));
		
		//Load data for accnId and switch to the Accession Search popup window
		accessionDemographics.searchAccnId(accnId);
		Thread.sleep(5000);
		
		//Ensure reason code is correct
		Assert.assertEquals(accessionDemographics.reasonCodeText().getAttribute("value"), "185");
		
		//Submit Accession to q_ep_corresp
		logger.info("*** Step 24 - Check Force to Correspondence and Submit");
		accessionDemographics.setforceToCorresp();
		
		//Ensure the accession gets stored in the q_ep_corresp_err
		logger.info("*** Step 25 - Ensure the accn was pushed into q_ep_corresp_err");
		Assert.assertEquals(daoManagerAccnWS.getCountFromQEpCorrespErrTableByAccnId(accnId, testDb), 1);
		driver.close();
    }
	
	@Test(priority = 1, description = "Price an Accession")
	@Parameters({"email", "password"})
	public void priceAccn(String email, String password) throws Exception {		
    	logger.info("***** Start to pricing an Accession *****");
    	
    	String accnId = System.getProperty("accnId");

		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		logger.info("*** Step 1 Expected Results: - Verify that the user is logged in");
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		//Assert.assertEquals(headerNavigation.marsCustomerText().getText(), "acme");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));	
		
		//Go to File Maintenance page
		logger.info("*** Step 2 Action: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Turn off sys setting for price Engine
		logger.info("*** Step 3 Action: - Turn off sys setting for Price Engine and subm_file table");
    	Assert.assertEquals(daoManagerXifinRpm.setSysSettingsPriceEngine(testDb), 6);
    	//Set B_EGATE_PROCESSED = 0 in subm_file table for PK_SUBM_FILE_SEQ_ID = 0
    	Assert.assertEquals(daoManagerXifinRpm.setBEGateProcessedFROMSUBMFILEBySubmFileSeqId("0", testDb), 1);

		//Go to System Data Cache page
		logger.info("*** Step 4 Action: - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

    	//Ensure accession is in Q_FR_Pending table
    	logger.info("*** Step 6 Action: - Ensure the new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);
    	
    	//Update Q_FR_Pending table so accession can be released
    	logger.info("*** Step 7 Action: - Ensure the new accession in the Q_FR_Pending table can be released");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);
    	
    	//Go to System Data Cache page
    	logger.info("*** Step 8 Action: - Navigate to the System Data Cache Page");		
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();
		xifinAdminUtils.clearDataCache();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Task Scheduler
		logger.info("*** Step 10 Action: - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();
		
		//Reset All Task Scheduler Engines
		logger.info("*** Step 11 Action: - Reset All Task Scheduler Engines");
		taskScheduler = new TaskScheduler(driver);
		taskScheduler.resetAllTaskSchedulerConcurrency();

		//Set Task Scheduler for OE Posting Engine
		logger.info("*** Step 12 Action: - Set Task Scheduler for OE Posting Engine");
		taskScheduler.setTaskScheduler("1", "now", 10);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		///Go to Task Status
		logger.info("*** Step 13 Action: - Navigate to Task Status");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		
		//Ensure Task Status for OE Posting Engine is completed
		logger.info("*** Step 14 Action: - Ensure Task Status for OE Posting Engine is completed");
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));
		
		Thread.sleep(10000);
	
		//Wait for Task Scheduler OE Posting Engine to complete then verify accession is in the Q_Price table
		logger.info("*** Step 15 Action: - Ensure the new accession is in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Navigate to Task Scheduler
		logger.info("*** Step 16 Action: - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();
		
		//Reset All Task Scheduler Engines
		logger.info("*** Step 17 Action: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();
		
		//Set Task Scheduler for Pricing Engine 
		logger.info("*** Step 18 Action: - Set Task Scheduler for Pricing Engine");
		taskScheduler.setTaskScheduler("2", "now", 10);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		///Go to Task Status
		logger.info("*** Step 19 Action: - Navigate to Task Status");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		switchToPopupWin();
		
		//Ensure Task Status for Pricing Engine is completed
		logger.info("*** Step 20 Action: - Ensure Task Status for Pricing Engine is completed");
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineActive("Pricing Engine"));
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Task Scheduler
		logger.info("*** Step 21 Action: - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();
		
		//Reset All Task Scheduler Engines
		logger.info("*** Step 22 Action: - Reset All Task Scheduler Engines");
		taskScheduler = new TaskScheduler(driver);
		taskScheduler.resetAllTaskSchedulerConcurrency();
		
		//sleep for 10 sec
		Thread.sleep(10000);
		
		logger.info("*** Step 23 Action: - Ensure the new accession is NOT in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 0);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		driver.close();
    }
	
	/*public void updateTestLinkResult(String testCase, String exception, String result) throws TestLinkAPIException {
        TestLinkAPIClient testlinkAPIClient = new TestLinkAPIClient(DEV_KEY, SERVER_URL);
        testlinkAPIClient.reportTestCaseResult(PROJECT_NAME, PLAN_NAME, testCase, BUILD_NAME, exception, result);
   }*/
	
	@Test(priority = 1, description = "EP Setup")
	@Parameters({"email", "password"})
	public void setupUpdateClientContactDetail(String email, String password) throws Exception {		
    	logger.info("***** Testing - EP Setup *****");
    	
    	String accnId = System.getProperty("accnId");
    	
    	//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		//Assert.assertEquals(headerNavigation.marsCustomerText().getText(), "acme");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));	
		
		//Go to Client Tab
		logger.info("*** Step 2 - Navigate to the Client Tab Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to client Contact
		logger.info("*** Step 4 - Navigate to the System Data Cache Page");
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToContactManagerLink();
		switchToPopupWin();
		
		//input client Id
		clientContactManager = new ClientContactManager(driver, wait);
		clientContactManager.inputClientId("100");
		
		boolean countClientContact =  this.getColumnValue(clientContactManager.tableContactManger(),"54");
		
		Assert.assertEquals(countClientContact, true);
		
		driver.close();
    }
	
	@Test(priority = 1, description = "CheckPayor")
	@Parameters({"email", "password"})
	public void CheckPayor(String username, String password) throws Exception {
		
		logger.info("==== Testing - CheckPayor ====");
		
		logger.info("*** Setup Create Client ");
		headerNavigation = new HeaderNavigation(driver, config);
		clientContactManager = new ClientContactManager(driver, wait);
		clientNavigation = new ClientNavigation(driver, config);

		logger.info("*** Step 1 Action: Log into Mars with username and password");
		
		logger.info("*** Step 1 Expect Result: User login successful");
		//verify that user is logged in
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(username, testDb)));
		// VErify customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), "acme");
		
		// Navigate to Payor tab
		logger.info("*** Step 2 Action: Navigate to Payor page - Payor Contact Manager tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expect result: User can navigate to Payor Contact Manager tab");
		Assert.assertTrue(isElementPresent(clientContactManager.clientIdInput(), 5));
		
		logger.info("*** Step 3 actual result: Input valid data to create new Client - Demographics");		
	}	
	
	@Test(priority = 1, description = "EP Setup")
	@Parameters({"email", "password"})
	public void setupUpdatePayorContactDetail(String email, String password) throws Exception {		
    	logger.info("***** Testing - EP Setup *****");
    	
    	String payorID = System.getProperty("accnId");

		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		//Assert.assertEquals(headerNavigation.marsCustomerText().getText(), "acme");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));	
		
		//Go to Client Tab
		logger.info("*** Step 2 - Navigate to the Payor Tab Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to client Contact
		logger.info("*** Step 4 - Navigate to the System Data Cache Page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPyrContactManager();
		switchToPopupWin();
		
		//input client Id
		payorContactManager = new PayorContactManager(driver);		
		payorContactManager.inputPayorId(payorID);
		String payorName = daoManagerPayorWS.getPayorNameByPayorId(payorID, testDb);
		Assert.assertEquals(payorName.toUpperCase(), payorContactManager.payorNameInput().getAttribute("value").toUpperCase());
		
		driver.close();
	}
	
	@Test(priority = 1, description = "Clear System Data Cache")
	@Parameters({"email", "password"})
	public void clearSystemDataCache(String email, String password) throws Exception {		
    	logger.info("***** Testing - Clear System Data Cache *****");
		
		logger.info("*** Step 1 Expected Results: - Verify that the user and customer id are correct");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));			
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));	
		
		logger.info("*** Step 2 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		logger.info("*** Step 3 Actions:- Navigate to the System Data Cache Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();
		
		driver.close();
    }
	
	@Test(priority = 1, description = "Price an Accession by using the PF Engines")
	@Parameters({"email", "password", "xapEnv", "eType1", "eType2", "engConfigDB", "hasLoggedIn"})
	public void priceAnAccn(String email, String password, String xapEnv, String eType1, String eType2, String engConfigDB, boolean hasLoggedIn) throws Exception {		
    	logger.info("***** Start to pricing an Accession *****");

		xifinAdminUtils = new XifinAdminUtils(driver, config);
		headerNavigation = new HeaderNavigation(driver, config);
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		taskScheduler = new TaskScheduler(driver); 		
		
		logger.info("*** Actions: - Turn off system setting for Price Engine");
    	Assert.assertEquals(daoManagerXifinRpm.setSysSettingsPriceEngine(testDb), 6);
    	daoManagerPlatform.setValuesFromTable("cln", "CLN.B_BILLING_ASSIGNMENT_REQ =0, CLN.B_CLN_PRC_SUSP_THRU_EOM = 0", "cln_abbrev not like 'AUTO%' and (CLN.B_BILLING_ASSIGNMENT_REQ =1 or CLN.B_CLN_PRC_SUSP_THRU_EOM = 1)", testDb);
		
		logger.info("*** Actions: - Clear System Cache in Xifin Admin Portal");		
		if (!hasLoggedIn){
			xifinAdminUtils.clearDataCache();
		}
		else{
			xifinAdminUtils.clearDataCache();
		}
		Thread.sleep(5000);
		
		logger.info("*** Actions: - Get the Accession ID");
		String accnId = System.getProperty("accnId");
		
    	logger.info("*** Expected Results: - Verify that the new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1, "       Accession ID " + accnId + " should in Q_FR_PENDING table.");
    	 
		logger.info("*** Actions: - Run PF-OE Posting Engine twice");		
	  	xifinAdminUtils.runPFEngineInCurrentWindow(this, email, password, xapEnv, eType1, engConfigDB, true);    	    	
    	
	  	xifinAdminUtils.runPFEngineInCurrentWindow(this, email, password, xapEnv, eType1, engConfigDB, true);    				

		logger.info("*** Expected Results: - Verify that the new accession is in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);
		
		logger.info("*** Actions: - Run PF-Pricing Engine");
		xifinAdminUtils.runPFEngineInCurrentWindow(this, email, password, xapEnv, eType2, engConfigDB, true);
		/*
		logger.info("*** Step 5 Actions: - Navigate to RPM");
		menuNavigation.navigateToRPM();
		
		//logger.info("*** Step 1 - Log into Mars with username and password");
		//ssoLogin = new SsoLogin(driver);
		//ssoLogin.login(email, password);		
		logger.info("*** Step 5 Expected Results: - Verify that the user is logged into RPM successfully");				
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 5 Actions: - Navigate to RPM File Maintenance tab"); 		
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 6 Actions: - Navigate to Task Scheduler page");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();		
		
		logger.info("*** Step 7 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();			
		
		logger.info("*** Step 8 Actions: - Set Task Scheduler for RPM Pricing Engine");
		taskScheduler.setTaskScheduler("2", "now", 10);
		
		//Go to main page
		driver.close();
		this.switchToWin(winHandler);
		this.switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 9 Actions: - Navigate to Task Status page");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		this.switchToPopupWin();		
		
		logger.info("*** Step 9 Expected Results: - Ensure Task Status for Pricing Engine is completed");
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineActive("Pricing Engine"));
		
		//Go to main page
		driver.close();
		this.switchToWin(winHandler);
		this.switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 10 Actions: - Navigate to Task Scheduler page");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		this.switchToPopupWin();		
		
		logger.info("*** Step 11 Actions: - Reset All Task Scheduler Engines");		
		taskScheduler.resetAllTaskSchedulerConcurrency();
		
		//sleep for 10 sec
		Thread.sleep(10000);
		*/
		
		logger.info("*** Step 12 Actions: - Ensure the new accession is NOT in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 0);
		
		//Go to main page
		/*
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		*/
		driver.close();
	
	}
	
	@Test(priority = 1, description = "EP Setup")
	@Parameters({"email", "password", "xapEnv", "eType1", "eType2", "engConfigDB", "hasLoggedIn", "xpEnv", "xpVersion"})
	public void setupEPAccn(String email, String password, String xapEnv, String eType1, String eType2, String engConfigDB, boolean hasLoggedIn, String xpEnv, String xpVersion) throws Exception {		
    	logger.info("***** Testing - EP Setup *****");    	

		xifinAdminUtils = new XifinAdminUtils(driver, config);
		headerNavigation = new HeaderNavigation(driver, config);
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		taskScheduler = new TaskScheduler(driver); 		
		menuNavigation = new MenuNavigation(driver, config);
		accessionDetail = new AccessionDetail(driver, config, wait);
		loadAccession = new LoadAccession(driver);
		
		logger.info("*** Actions: - Turn off system setting for Price Engine");
    	Assert.assertEquals(daoManagerXifinRpm.setSysSettingsPriceEngine(testDb), 6);
    	daoManagerPlatform.setValuesFromTable("cln", "CLN.B_BILLING_ASSIGNMENT_REQ =0, CLN.B_CLN_PRC_SUSP_THRU_EOM = 0", "cln_abbrev not like 'AUTO%' and (CLN.B_BILLING_ASSIGNMENT_REQ =1 or CLN.B_CLN_PRC_SUSP_THRU_EOM = 1)", testDb);
		
		logger.info("*** Actions: - Clear System Cache in Xifin Admin Portal");		
		if (!hasLoggedIn){
			xifinAdminUtils.clearDataCache();
		}
		else{
			xifinAdminUtils.clearDataCache();
		}		
		
		logger.info("*** Actions: - Get the Accession ID");
		String accnId = System.getProperty("accnId");
		
    	logger.info("*** Expected Results: - Verify that the new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1, "       Accession ID " + accnId + " should in Q_FR_PENDING table.");
    	 
		logger.info("*** Actions: - Run PF-OE Posting Engine twice");		
	  	xifinAdminUtils.runPFEngineInCurrentWindow(this, email, password, xapEnv, eType1, engConfigDB, true);    	    	
    	
	  	xifinAdminUtils.runPFEngineInCurrentWindow(this, email, password, xapEnv, eType1, engConfigDB, true);    					

		logger.info("*** Expected Results: - Verify that the new accession is in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);
		
		logger.info("*** Actions: - Run PF-Pricing Engine");
		xifinAdminUtils.runPFEngineInCurrentWindow(this, email, password, xapEnv, eType2, engConfigDB, true);
		
		logger.info("*** Expected Results: - Ensure the new accession is NOT in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 0);	
		/*
		logger.info("*** Actions: - Navigate to RPM");
		menuNavigation.navigateToRPM();
		
		//logger.info("*** Step 1 - Log into Mars with username and password");
		//ssoLogin = new SsoLogin(driver);
		//ssoLogin.login(email, password);		
		logger.info("*** Expected Results: - Verify that the user is logged into RPM successfully");				
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//logger.info("*** Step 5 Actions: - Navigate to RPM File Maintenance tab"); 		
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();		
		
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 6 Actions: - Navigate to Task Scheduler page");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		switchToPopupWin();		
		
		logger.info("*** Step 7 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();			
		
		logger.info("*** Step 8 Actions: - Set Task Scheduler for RPM Pricing Engine");
		taskScheduler.setTaskScheduler("2", "now", 10);
		
		//Go to main page
		driver.close();
		this.switchToWin(winHandler);
		this.switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 9 Actions: - Navigate to Task Status page");
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		this.switchToPopupWin();		
		
		logger.info("*** Step 9 Expected Results: - Ensure Task Status for Pricing Engine is completed");
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineActive("Pricing Engine"));
		
		//Go to main page
		driver.close();
		this.switchToWin(winHandler);
		this.switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 10 Actions: - Navigate to Task Scheduler page");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		this.switchToPopupWin();		
		
		logger.info("*** Step 11 Actions: - Reset All Task Scheduler Engines");		
		taskScheduler.resetAllTaskSchedulerConcurrency();
		
		//sleep for 10 sec
		Thread.sleep(10000);
		
		logger.info("*** Step 12 Actions: - Ensure the new accession is NOT in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 0);			
		
		//Go to main page
		//driver.close();
		 
		
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		//headerNavigation.navigateToAccessionTab();
		 */
		logger.info("*** Actions: - Navigate to XP - Accn Detail screen");
		menuNavigation.navigateToAccnDetailPage();
		
		logger.info("*** Expected Results: - Verify that it's on the Load Accession page");			
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present."); 
		
		logger.info("*** Actions: - Load the Accession");
		loadAccession.setAccnId(accnId);
		
		logger.info("*** Expected Results: - Verify that the accn was loadeded properly");	
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");			
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded propertly.");
		
		logger.info("*** Actions: - Select Force to Correspondence from dropdown");
		//selectCheckBox(accessionDetail.forceToCorrespondenceCheckbox());
		selectItemByVal(accessionDetail.forceToEpDropdown(wait), "CORR");
		
		logger.info("*** Actions: - Check Save And Clear button");
		accessionDetail.clickSavdAndClearBtn();
		
		logger.info("*** Expected Results: - Verify that it goes back to the Load Accession page");
		Assert.assertTrue(accessionDetail.isSaveDone(), "        Save should be done.");					
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");	
		
		//Ensure the accession gets stored in the q_ep_corresp_err
		logger.info("*** Expected Results: - Ensure the accn was pushed into q_ep_corresp_err");
		Assert.assertEquals(daoManagerAccnWS.getCountFromQEpCorrespErrTableByAccnId(accnId, testDb), 1);
		
		driver.close();
    }
	
	@Test(priority = 1, description = "Clear RPM System Data Cache")
	@Parameters({"email", "password", "xapEnv"})
	public void clearRPMDataCache(String email, String password, String xapEnv) throws Exception {
		logger.info("***** Testing - Clear RPM System Data Cache *****");

		xifinAdminUtils = new XifinAdminUtils(driver, config);

		xifinAdminUtils.clearDataCache();
		Thread.sleep(5000);

		driver.close();
	}

	@Test(priority = 1, description = "Run Platform Engine in XAP")
	@Parameters({"email", "password", "xapEnv", "eType", "engConfigDB", "hasLoggedIn"})
	public void runPFEngine(String email, String password, String xapEnv, String eType, String engConfigDB, boolean hasLoggedIn) throws Exception {
		logger.info("***** Starting - Run PF Engine *****");

		xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.runPFEngine(this, email, password, xapEnv, eType, engConfigDB, hasLoggedIn);

		driver.close();
	}
	
	
}
