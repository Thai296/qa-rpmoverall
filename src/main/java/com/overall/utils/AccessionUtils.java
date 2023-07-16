package com.overall.utils;

import com.overall.accession.accessionProcessing.AccessionDemographics;
import com.overall.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
import com.overall.fileMaintenance.sysMgt.TaskScheduler;
import com.overall.fileMaintenance.sysMgt.TaskStatus;
import com.overall.headerNavigation.HeaderNavigation;
import com.overall.menu.MenuNavigation;
import com.xifin.mars.dao.IGenericDaoXifinRpm;
import com.xifin.patientportal.dao.IGenericDaoPatientPortal;
import com.xifin.platform.dao.IGenericDaoPlatform;
import com.xifin.qa.config.PropertyMap;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
import com.xifin.xap.utils.XifinAdminUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import java.util.ArrayList;



public class AccessionUtils
{
	private static final Logger LOG = Logger.getLogger(AccnDetailUtils.class);

	private final RemoteWebDriver driver;
	private final IGenericDaoXifinRpm daoManagerXifinRpm;
	private final IGenericDaoPlatform daoManagerPlatform;
	private final IGenericDaoPatientPortal daoManagerPatientPortal;

	private SsoLogin ssoLogin;
	private HeaderNavigation headerNavigation;
	private TestDataSetup testDataSetup;
	private FileMaintenanceNavigation fileMaintenaceNavigation;
	private TaskScheduler taskScheduler;
	private TaskStatus taskStatus;

	public AccessionUtils(RemoteWebDriver driver, IGenericDaoXifinRpm daoManagerXifinRpm, IGenericDaoPlatform daoManagerPlatform, IGenericDaoPatientPortal daoManagerPatientPortal)
	{
		this.driver = driver;
		this.daoManagerXifinRpm = daoManagerXifinRpm;
		this.daoManagerPlatform = daoManagerPlatform;
		this.daoManagerPatientPortal = daoManagerPatientPortal;
	}

	public String createPricedAccn(SeleniumBaseTest b, String env, String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String testDb, String endpoint) throws Exception {

		LOG.info("*** Step 1 Actions: - Log into RPM with username and password");

		ssoLogin.login(email, password);

		LOG.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, b.getConfig());
		b.switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		LOG.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		LOG.info("*** Step 2 Actions: - Navigate to the File Maintenance Page");
		b.switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		b.switchToPopupWin();

		LOG.info("*** Step 2 Expected Results: - Verify that the file maintenance page displays");
		//Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("File Maintenance - Exam Code "), "        File Maintenance - Exam Code  page title should show.");

		LOG.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");
		driver.close();
		b.switchToWin(winHandler);
		b.switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, b.getConfig());
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		b.switchToPopupWin();

		new XifinAdminUtils(driver, b.getConfig()).clearDataCache();

		LOG.info("*** Step 5 Actions: - Send request to create an accession");
		driver.close();
		b.switchToWin(winHandler);
		b.switchToDefaultWinFromFrame();
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, b.getConfig().getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, endpoint);
    	LOG.info("        AccnID: " + accnId);

    	LOG.info("*** Step 5 Expected Results: The accession gets created");
    	Assert.assertTrue(accnId != null);

    	LOG.info("*** Step 5 Expected Results: - The new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);

    	LOG.info("*** Step 6 Actions: - Release the new accession in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);

    	LOG.info("*** Step 6 Exepected Results: The new accession in the Q_FR_Pending table is set");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnIdHoldRuleBReleaseBOEPerformed(accnId, testDb), 1);

    	LOG.info("*** Step 7 Actions: - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		b.switchToPopupWin();

		new XifinAdminUtils(driver, b.getConfig()).clearDataCache();

		LOG.info("*** Step 9 Actions: - Navigate to Task Scheduler");
		driver.close();
		b.switchToWin(winHandler);
		b.switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		b.switchToPopupWin();

		LOG.info("*** Step 9 Expected Results: - The Task Scheduler page displays");
		taskScheduler = new TaskScheduler(driver);
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		LOG.info("*** Step 10 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		//LOG.info("*** Step 10 Expected Results: - All Task Scheduler Engines are reset");
		//Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		LOG.info("*** Step 11 Actions: - Set Task Scheduler for OE Posting Engine");
		taskScheduler.setTaskScheduler("1", "now", 10);

		LOG.info("*** Step 11 Expected Results: - Task Status for OE Posting Engine is active");
		driver.close();
		b.switchToWin(winHandler);
		b.switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		b.switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive("OE Posting Engine"));

		LOG.info("*** Step 11 Expected Results: - Ensure Task Status for OE Posting Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));

		LOG.info("*** Step 11 Exepected Results: The new accession in the Q_FR_Pending table is released");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 0);

		LOG.info("*** Step 11 Expected Results: - Ensure the new accession is in Q_Price table");
		driver.close();
		b.switchToWin(winHandler);
		b.switchToDefaultWinFromFrame();
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);

		LOG.info("*** Step 12 Action: - Navigate to Task Scheduler");
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		b.switchToPopupWin();

		LOG.info("*** Step 12 Expected Results: - The Task Scheduler page displays");
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		LOG.info("*** Step 13 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		//LOG.info("*** Step 13 Expected Results: - All Task Scheduler Engines are reset");
		//Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		LOG.info("*** Step 14 Actions: - Set Task Scheduler for Pricing Engine");
		taskScheduler.setTaskScheduler("2", "now", 10);

		LOG.info("*** Step 14 Expected Results: - Task Status for Pricing Engine is active");
		driver.close();
		b.switchToWin(winHandler);
		b.switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		b.switchToPopupWin();
		Assert.assertFalse(taskStatus.isEngineInactive("Pricing Engine"));

		LOG.info("*** Step 14 Expected Results: - Ensure Task Status for Pricing Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("Pricing Engine"));

		LOG.info("*** Step 15 Action: - Navigate to Task Scheduler");
		driver.close();
		b.switchToWin(winHandler);
		b.switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		b.switchToPopupWin();

		LOG.info("*** Step 15 Expected Results: - The Task Scheduler page displays");
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		LOG.info("*** Step 16 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		//LOG.info("*** Step 16 Expected Results: - All Task Scheduler Engines are reset");
		//Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		LOG.info("*** Step 16 Expected Results: - Ensure the new priced accession is NOT in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 0);

		driver.close();
		b.switchToWin(winHandler);
		b.switchToDefaultWinFromFrame();

		return accnId;
	}

	public String createAccnWithEPI(SeleniumBaseTest b, String env, String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String testDb, String endpoint) throws Exception {

		LOG.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		LOG.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, b.getConfig());
		b.switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		LOG.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		LOG.info("*** Step 2 Actions: - Navigate to the File Maintenance Page");
		b.switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		b.switchToPopupWin();

		LOG.info("*** Step 2 Expected Results: - Verify that the file maintenance page displays");
		//Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("File Maintenance - Exam Code "), "        File Maintenance - Exam Code  page title should show.");

		new XifinAdminUtils(driver, b.getConfig()).clearDataCache();

		LOG.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");
		driver.close();

		LOG.info("*** Step 5 Actions: - Send request to create an accession");
		driver.close();
		b.switchToWin(winHandler);
		b.switchToDefaultWinFromFrame();
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, b.getConfig().getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, endpoint);
    	LOG.info("AccnID: " + accnId);

    	LOG.info("*** Step 5 Expected Results: The accession gets created");
    	Assert.assertTrue(accnId != null);

    	LOG.info("*** Step 5 Expected Results: - The new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);

    	LOG.info("*** Step 6 Actions: - Release the new accession in the Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);

    	LOG.info("*** Step 6 Exepected Results: The new accession in the Q_FR_Pending table is set");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnIdHoldRuleBReleaseBOEPerformed(accnId, testDb), 1);

    	LOG.info("*** Step 7 Actions: - Navigate to the System Data Cache Page");
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		b.switchToPopupWin();

		LOG.info("*** Step 9 Actions: - Navigate to Task Scheduler");
		driver.close();

		new XifinAdminUtils(driver, b.getConfig()).clearDataCache();

		b.switchToWin(winHandler);
		b.switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		b.switchToPopupWin();

		LOG.info("*** Step 9 Expected Results: - The Task Scheduler page displays");
		taskScheduler = new TaskScheduler(driver);
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		LOG.info("*** Step 10 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		LOG.info("*** Step 10 Expected Results: - All Task Scheduler Engines are reset");
		Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		LOG.info("*** Step 11 Actions: - Set Task Scheduler for OE Posting Engine");
		taskScheduler.setTaskScheduler("1", "now", 10);

		LOG.info("*** Step 11 Expected Results: - Task Status for OE Posting Engine is active");
		driver.close();
		b.switchToWin(winHandler);
		b.switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		b.switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive("OE Posting Engine"));

		LOG.info("*** Step 11 Expected Results: - Ensure Task Status for OE Posting Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));

		LOG.info("*** Step 11 Exepected Results: The new accession in the Q_FR_Pending table is released");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 0);

		LOG.info("*** Step 11 Expected Results: - Ensure the new accession is in Q_Price table");
		driver.close();
		b.switchToWin(winHandler);
		b.switchToDefaultWinFromFrame();
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);

		LOG.info("*** Step 12 Actions: - Navigate to Accession Tab");
		headerNavigation.navigateToAccessionTab();

		LOG.info("*** Step 13 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		String popupHandles = b.switchToPopupWin("c");
		b.switchToPopupWin();
		AccessionDemographics accessionDemographics = new AccessionDemographics(driver);
		Assert.assertTrue(b.isElementPresent(accessionDemographics.accnIdInput(),5));

		LOG.info("*** Step 13 Actions: - Load data for accnId and switch to the Accession Search popup window");
		accessionDemographics.searchAccnId(accnId);

		LOG.info("*** Step 13 Expected Results: - Verify patient First Name is correct when accession data loads");
		Assert.assertTrue(b.isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));

		LOG.info("*** Step 14 Actions: - Navigate to Create New Patient popup");
		accessionDemographics.naviateToCreateNewPatient();
		b.switchToPopupWin();

		LOG.info("*** Step 14 Expected Results: - Ensure Create New Patient popup displays");
		Assert.assertTrue(b.isElementPresent(accessionDemographics.createNewPatientPopupOkBtn(),5));

		LOG.info("*** Step 15 Actions: - Add the epi");
		accessionDemographics.addEpi();

		LOG.info("*** Step 16 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.reset();
		accessionDemographics.searchAccnId(accnId);

		LOG.info("*** Step 16 Expected Results: - Verify EPI is associated to the accession");
		Assert.assertEquals(accessionDemographics.epiText().toString(), daoManagerPatientPortal.getEpiByAccnId(accnId, testDb));

		driver.close();
		b.switchToWin(winHandler);
		b.switchToDefaultWinFromFrame();

		return accnId;
	}


	public void runRPMEngine(SeleniumBaseTest b, String env, String email, String password, String testDb, String taskTypeID, int concurrency, String taskType) throws Exception {
    	LOG.info("***** Testing - Run RPM " + taskType + " *****");

		LOG.info("*** Step 1 Actions: - Navigate to the File Maintenance Page");
		headerNavigation = new HeaderNavigation(driver, b.getConfig());
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		b.switchToPopupWin();
		driver.close();
		b.switchToWin(winHandler);
		b.switchToDefaultWinFromFrame();

		LOG.info("*** Step 2 Actions:- Navigate to the Task Scheduler Page");
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, b.getConfig());
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		b.switchToPopupWin();

		LOG.info("*** Step 2 Expected Results: - The Task Scheduler page displays");
		taskScheduler = new TaskScheduler(driver);
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		LOG.info("*** Step 3 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		LOG.info("*** Step 3 Expected Results: - All Task Scheduler Engines are reset");
		Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		LOG.info("*** Step 4 Actions: - Set Task Scheduler for Engine");
		taskScheduler.setTaskScheduler(taskTypeID, "now", concurrency);

		LOG.info("*** Step 4 Expected Results: - Task Status for Engine is active");
		driver.close();
		b.switchToWin(winHandler);
		b.switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskStatusLink();
		b.switchToPopupWin();
		taskStatus = new TaskStatus(driver);
		Assert.assertFalse(taskStatus.isEngineInactive(taskType));

		LOG.info("*** Step 4 Expected Results: - Ensure Task Status for Engine is completed");
		Assert.assertFalse(taskStatus.isEngineActive(taskType));

		LOG.info("*** Step 5 Action: - Navigate to Task Scheduler");
		driver.close();
		b.switchToWin(winHandler);
		b.switchToDefaultWinFromFrame();
		fileMaintenaceNavigation.navigateToTaskSchedulerLink();
		b.switchToPopupWin();

		LOG.info("*** Step 5 Expected Results: - The Task Scheduler page displays");
		Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

		LOG.info("*** Step 6 Actions: - Reset All Task Scheduler Engines");
		taskScheduler.resetAllTaskSchedulerConcurrency();

		LOG.info("*** Step 6 Expected Results: - All Task Scheduler Engines are reset");
		Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

		LOG.info("***** Done - Run RPM " + taskType + " *****");

		driver.close();
		b.switchToWin(winHandler);
		b.switchToDefaultWinFromFrame();
    }

	//This method find that the passed string value exists in the List
	public boolean isCorrespFileExist(ArrayList<String>correspFileInfoList ) throws Exception {
		boolean flag = false;

		if (!(correspFileInfoList.isEmpty())) {
			LOG.info("       Corresp file found " + correspFileInfoList.get(5));
			flag = true;
		}else{
			LOG.info("       No Corresp file found.");
		}
		return flag;
	}

	public String generateClnStmtFileName(String clnId, String submSvcSeqId, String testDb) throws Exception {
		String newFileName = "";

		ArrayList<String> submSvcInfoList = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCBySubmSvcSeqId(submSvcSeqId, testDb);
		String dataFmtTypId = submSvcInfoList.get(0).trim();
		String submSvcAbbrev = submSvcInfoList.get(2).trim();

		ArrayList<String> dataFmtInfoList = daoManagerPlatform.getDataFmtTypInfoFromDATAFMTTYPByDataFmtTypId(dataFmtTypId, testDb);
		String fileExt = dataFmtInfoList.get(5).trim();

		String clnAbbrev = daoManagerXifinRpm.getClientAbbrevIdByClientId(clnId, testDb).trim();

		TimeStamp timeStamp = new TimeStamp(driver);
		newFileName = submSvcAbbrev + "_" + clnAbbrev + "-" + timeStamp.getTimeStamp() + "." + fileExt;
		LOG.info("        Generated new Client Statement File Name: " + newFileName);

		return newFileName;
	}


	public String createPricedAccn(SeleniumBaseTest b, String env, String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String testDb, String endpoint, String xapEnv, String eType, String engConfigDB) throws Exception {
		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, b.getConfig());
        headerNavigation = new HeaderNavigation(driver, b.getConfig());
        fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, b.getConfig());
        taskScheduler = new TaskScheduler(driver);

        LOG.info("*** Step 1 Actions: - Turn off system setting for Price Engine");
 Assert.assertEquals(daoManagerXifinRpm.setSysSettingsPriceEngine(testDb), 6);
 daoManagerPlatform.setValuesFromTable("cln", "CLN.B_BILLING_ASSIGNMENT_REQ =0, CLN.B_CLN_PRC_SUSP_THRU_EOM = 0", "cln_abbrev not like 'AUTO%' and (CLN.B_BILLING_ASSIGNMENT_REQ =1 or CLN.B_CLN_PRC_SUSP_THRU_EOM = 1)", testDb);

        LOG.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");

            //   xifinAdminUtils.clearDataCache(Configuration config);

        Thread.sleep(5000);


        /*
        LOG.info("*** Step 1 Actions: - Log into RPM with username and password");
        ssoLogin = new SsoLogin(driver);
        ssoLogin.login(email, password);

        LOG.info("*** Step 1 Expected Results: - Verify that user is logged in");
        headerNavigation = new HeaderNavigation(driver, b.getConfig());
        b.switchToFrame(headerNavigation.marsHeaderFrame());

 Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

        LOG.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

        Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

        LOG.info("*** Step 2 Actions: - Navigate to the File Maintenance Page");
        b.switchToDefaultWinFromFrame();
        String winHandler = driver.getWindowHandle();
        headerNavigation.navigateToFileMaintenanceTab();
        b.switchToPopupWin();

        LOG.info("*** Step 2 Expected Results: - Verify that the file maintenance page displays");
        //Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("File Maintenance - Exam Code "), "        File Maintenance - Exam Code  page title should show.");

        LOG.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");
        driver.close();
        b.switchToWin(winHandler);
        b.switchToDefaultWinFromFrame();
        fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, b.getConfig());
        fileMaintenaceNavigation.navigateToSystemDataCacheLink();
        b.switchToPopupWin();

        LOG.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
        dataCacheConfiguration = new DataCacheConfiguration(driver);
        Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

        LOG.info("*** Step 4 Actions: - Clear all the Cache");
        dataCacheConfiguration.setClearCacheAll(b);

        LOG.info("*** Step 4 Expected Results: - Verify the cache is cleared");
        int rowNum = dataCacheConfiguration.getRowNumberInDataCacheTable(dataCacheConfiguration.dataCacheConfigTable(), "ADJ_CD");
        Assert.assertFalse(dataCacheConfiguration.reloadNowText(rowNum).isSelected(), "        The Reload checkbox for ADJ_CD should be unchecked.");
        */
        LOG.info("*** Step 5 Actions: - Send request to create an accession");
        //driver.close();
        //b.switchToWin(winHandler);
        //b.switchToDefaultWinFromFrame();
 testDataSetup = new TestDataSetup(driver);
 String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, b.getConfig().getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, endpoint);
 LOG.info("        AccnID: " + accnId);
 /*
 LOG.info("*** Step 5 Expected Results: The accession gets created");
 Assert.assertTrue(accnId != null);

 LOG.info("*** Step 5 Expected Results: - The new accession is in Q_FR_Pending table");
 Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1);

 LOG.info("*** Step 6 Actions: - Release the new accession in the Q_FR_Pending table");
 Assert.assertEquals(daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId, testDb), 1);

 LOG.info("*** Step 6 Exepected Results: The new accession in the Q_FR_Pending table is set");
Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnIdHoldRuleBReleaseBOEPerformed(accnId, testDb), 1);

 LOG.info("*** Step 7 Actions: - Navigate to the System Data Cache Page");
        fileMaintenaceNavigation.navigateToSystemDataCacheLink();
        b.switchToPopupWin();

        LOG.info("*** Step 7 Expected Results: - The Data Cache Configuration page displays");
        Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

        LOG.info("*** Step 8 Actions: - Clear all the Cache");
        dataCacheConfiguration.setClearCacheAll(b);

        LOG.info("*** Step 8 Expected Results: - Verify the cache is cleared");
        rowNum = dataCacheConfiguration.getRowNumberInDataCacheTable(dataCacheConfiguration.dataCacheConfigTable(), "ADJ_CD");
        Assert.assertFalse(dataCacheConfiguration.reloadNowText(rowNum).isSelected(), "        The Reload checkbox for ADJ_CD should be unchecked.");

        LOG.info("*** Step 9 Actions: - Navigate to Task Scheduler");
        driver.close();
        b.switchToWin(winHandler);
        b.switchToDefaultWinFromFrame();
        fileMaintenaceNavigation.navigateToTaskSchedulerLink();
        b.switchToPopupWin();

        LOG.info("*** Step 9 Expected Results: - The Task Scheduler page displays");
        taskScheduler = new TaskScheduler(driver);
        Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

        LOG.info("*** Step 10 Actions: - Reset All Task Scheduler Engines");
        taskScheduler.resetAllTaskSchedulerConcurrency();

        //LOG.info("*** Step 10 Expected Results: - All Task Scheduler Engines are reset");
        //Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

        LOG.info("*** Step 11 Actions: - Set Task Scheduler for OE Posting Engine");
        taskScheduler.setTaskScheduler("1", "now", 10);

        LOG.info("*** Step 11 Expected Results: - Task Status for OE Posting Engine is active");
        driver.close();
        b.switchToWin(winHandler);
        b.switchToDefaultWinFromFrame();
        fileMaintenaceNavigation.navigateToTaskStatusLink();
        b.switchToPopupWin();
        taskStatus = new TaskStatus(driver);
        Assert.assertFalse(taskStatus.isEngineInactive("OE Posting Engine"));

        LOG.info("*** Step 11 Expected Results: - Ensure Task Status for OE Posting Engine is completed");
        Assert.assertFalse(taskStatus.isEngineActive("OE Posting Engine"));

        LOG.info("*** Step 11 Exepected Results: The new accession in the Q_FR_Pending table is released");
 Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 0);

        LOG.info("*** Step 11 Expected Results: - Ensure the new accession is in Q_Price table");
        driver.close();
        b.switchToWin(winHandler);
        b.switchToDefaultWinFromFrame();
        Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);

        LOG.info("*** Step 12 Action: - Navigate to Task Scheduler");
        fileMaintenaceNavigation.navigateToTaskSchedulerLink();
        b.switchToPopupWin();

        LOG.info("*** Step 12 Expected Results: - The Task Scheduler page displays");
        Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

        LOG.info("*** Step 13 Actions: - Reset All Task Scheduler Engines");
        taskScheduler.resetAllTaskSchedulerConcurrency();

        //LOG.info("*** Step 13 Expected Results: - All Task Scheduler Engines are reset");
        //Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

        LOG.info("*** Step 14 Actions: - Set Task Scheduler for Pricing Engine");
        taskScheduler.setTaskScheduler("2", "now", 10);

        LOG.info("*** Step 14 Expected Results: - Task Status for Pricing Engine is active");
        driver.close();
        b.switchToWin(winHandler);
        b.switchToDefaultWinFromFrame();
        fileMaintenaceNavigation.navigateToTaskStatusLink();
        b.switchToPopupWin();
        Assert.assertFalse(taskStatus.isEngineInactive("Pricing Engine"));

        LOG.info("*** Step 14 Expected Results: - Ensure Task Status for Pricing Engine is completed");
        Assert.assertFalse(taskStatus.isEngineActive("Pricing Engine"));

        LOG.info("*** Step 15 Action: - Navigate to Task Scheduler");
        driver.close();
        b.switchToWin(winHandler);
        b.switchToDefaultWinFromFrame();
        fileMaintenaceNavigation.navigateToTaskSchedulerLink();
        b.switchToPopupWin();

        LOG.info("*** Step 15 Expected Results: - The Task Scheduler page displays");
        Assert.assertTrue(taskScheduler.pageTitleText().getText().contains("File Maintenance - Task Scheduler"), "        File Maintenance - Task Scheduler page title should show.");

        LOG.info("*** Step 16 Actions: - Reset All Task Scheduler Engines");
        taskScheduler.resetAllTaskSchedulerConcurrency();

        //LOG.info("*** Step 16 Expected Results: - All Task Scheduler Engines are reset");
        //Assert.assertTrue(taskScheduler.isAllTaskSchedulerConcurrencyReset());

        LOG.info("*** Step 16 Expected Results: - Ensure the new priced accession is NOT in Q_Price table");
        Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 0);

        driver.close();
        b.switchToWin(winHandler);
        b.switchToDefaultWinFromFrame();
        */
 LOG.info("*** Step 3 Expected Results: - Verify that the new accession is in Q_FR_Pending table");
 Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1, "       Accession ID " + accnId + " should in Q_FR_PENDING table.");

        LOG.info("*** Step 4 Actions: - Run PF-OE Posting Engine twice");
        xifinAdminUtils.runPFEngine(b, email, password, xapEnv, eType, engConfigDB, true);
 Thread.sleep(5000);

        xifinAdminUtils.runPFEngine(b, email, password, xapEnv, eType, engConfigDB, true);
 Thread.sleep(5000);

        LOG.info("*** Step 4 Expected Results: - Verify that the new accession is in Q_Price table");
        Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);

        LOG.info("*** Step 5 Actions: - Navigate to RPM");
        new MenuNavigation(driver, b.getConfig()).navigateToRPM();

        //LOG.info("*** Step 1 - Log into Mars with username and password");
        //ssoLogin = new SsoLogin(driver);
        //ssoLogin.login(email, password);
        LOG.info("*** Step 5 Expected Results: - Verify that the user is logged into RPM successfully");
        b.switchToFrame(headerNavigation.marsHeaderFrame());
 Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
        Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

        LOG.info("*** Step 5 Actions: - Navigate to RPM File Maintenance tab");
        b.switchToDefaultWinFromFrame();
        String winHandler = driver.getWindowHandle();
        headerNavigation.navigateToFileMaintenanceTab();
        b.switchToPopupWin();
        driver.close();
        b.switchToWin(winHandler);
        b.switchToDefaultWinFromFrame();

        LOG.info("*** Step 6 Actions: - Navigate to Task Scheduler page");
        fileMaintenaceNavigation.navigateToTaskSchedulerLink();
        b.switchToPopupWin();

        LOG.info("*** Step 7 Actions: - Reset All Task Scheduler Engines");
        taskScheduler.resetAllTaskSchedulerConcurrency();

        LOG.info("*** Step 8 Actions: - Set Task Scheduler for RPM Pricing Engine");
        taskScheduler.setTaskScheduler("2", "now", 10);

        //Go to main page
        driver.close();
        b.switchToWin(winHandler);
        b.switchToDefaultWinFromFrame();

        LOG.info("*** Step 9 Actions: - Navigate to Task Status page");
        fileMaintenaceNavigation.navigateToTaskStatusLink();
        b.switchToPopupWin();

        LOG.info("*** Step 9 Expected Results: - Ensure Task Status for Pricing Engine is completed");
        taskStatus = new TaskStatus(driver);
        Assert.assertFalse(taskStatus.isEngineActive("Pricing Engine"));

        //Go to main page
        driver.close();
        b.switchToWin(winHandler);
        b.switchToDefaultWinFromFrame();

        LOG.info("*** Step 10 Actions: - Navigate to Task Scheduler page");
        fileMaintenaceNavigation.navigateToTaskSchedulerLink();
        b.switchToPopupWin();

        LOG.info("*** Step 11 Actions: - Reset All Task Scheduler Engines");
        taskScheduler.resetAllTaskSchedulerConcurrency();

        //sleep for 10 sec
        Thread.sleep(10000);

        LOG.info("*** Step 12 Actions: - Ensure the new accession is NOT in Q_Price table");
        Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 0);

        //Go to main page
        driver.close();
        b.switchToWin(winHandler);
        b.switchToDefaultWinFromFrame();
        headerNavigation.navigateToAccessionTab();
        driver.close();


        return accnId;
 }


}

