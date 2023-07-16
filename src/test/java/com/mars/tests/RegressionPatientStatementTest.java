package com.mars.tests;

import com.overall.accession.accessionNavigation.AccessionNavigation;
import com.overall.accession.accessionProcessing.AccessionDemographics;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.accession.accessionProcessing.AccessionSearch;
import com.overall.accession.accessionProcessing.AccessionSingleStatement;
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
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.TestDataSetup;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class RegressionPatientStatementTest extends SeleniumBaseTest  {

	private HeaderNavigation headerNavigation;
	private Help help;
	private AccessionNavigation accessionNavigation;
	private SuperSearch superSearch;
	private SuperSearchResults superSearchResults;
	private AccessionDemographics accessionDemographics;
	private AccessionSingleStatement accessionStatement;
	private AccessionSearch accessionSearch;
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
			MenuNavigation navigation = new MenuNavigation(driver, config);
			navigation.navigateToPatientNotificationConfigurationPage();
		}
		catch (Exception e)
		{
			logger.error("Error running BeforeMethod", e);
		}
	}

    @Test(priority = 1, description = "RPM - Task Scheduler - Non-Client Statement Engine - Generate Patient Statement")
	@Parameters({"email", "password", "customer", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "xapEnv", "eType", "engConfigDB"})
	public void testPatientStatement_01(String email, String password, String customer, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String xapEnv, String eType, String engConfigDB) throws Exception {

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
	  	xifinAdminUtils.runPFEngine(this, email, password, xapEnv, eType, engConfigDB, true);
    	Thread.sleep(5000);     	
    	
	  	xifinAdminUtils.runPFEngine(this, email, password, xapEnv, eType, engConfigDB, true);
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
}
