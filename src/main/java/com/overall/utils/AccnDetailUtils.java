package com.overall.utils;

import com.mbasys.mars.persistance.AccnStatusMap;
import com.overall.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
import com.overall.fileMaintenance.sysMgt.TaskScheduler;
import com.overall.fileMaintenance.sysMgt.TaskStatus;
import com.overall.headerNavigation.HeaderNavigation;
import com.overall.menu.MenuNavigation;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.accnws.dao.DaoManagerAccnWS;
import com.xifin.accnws.dao.IGenericDaoAccnWS;
import com.xifin.clientportal.dao.DaoManagerClientPortal;
import com.xifin.clientportal.dao.IGenericDaoClientPortal;
import com.xifin.mars.dao.DaoManagerXifinRpm;
import com.xifin.mars.dao.IGenericDaoXifinRpm;
import com.xifin.patientportal.dao.DaoManagerPatientPortal;
import com.xifin.patientportal.dao.IGenericDaoPatientPortal;
import com.xifin.platform.dao.DaoManagerPlatform;
import com.xifin.platform.dao.IGenericDaoPlatform;
import com.xifin.qa.config.Configuration;
import com.xifin.qa.dao.rpm.AccessionDao;
import com.xifin.qa.dao.rpm.AccessionDaoImpl;
import com.xifin.sso.dao.DaoManagerSSO;
import com.xifin.sso.dao.IGenericDaoSSO;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TestDataSetup;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AccnDetailUtils
{
	private static final Logger LOG = Logger.getLogger(AccnDetailUtils.class);
	
	private final RemoteWebDriver driver;
	private final Configuration config;

	protected IGenericDaoClientPortal daoManagerClientPortal;
	protected IGenericDaoXifinRpm daoManagerXifinRpm;
	protected IGenericDaoSSO daoManagerSSO;
	protected IGenericDaoPatientPortal daoManagerPatientPortal;
	protected IGenericDaoAccnWS daoManagerAccnWS;
	protected IGenericDaoPlatform daoManagerPlatform;
	protected AccessionDao accessionDao;
	
	public AccnDetailUtils(RemoteWebDriver driver, Configuration config)
	{
		this.driver = driver;
		this.config = config;
		this.daoManagerClientPortal = new DaoManagerClientPortal(config.getRpmDatabase());
		this.daoManagerXifinRpm = new DaoManagerXifinRpm(config.getRpmDatabase());
		this.daoManagerSSO = new DaoManagerSSO(config.getSsoDatabase());
		this.daoManagerPatientPortal = new DaoManagerPatientPortal(config.getRpmDatabase());
		this.daoManagerAccnWS = new DaoManagerAccnWS(config.getRpmDatabase());
		this.daoManagerPlatform = new DaoManagerPlatform(config.getRpmDatabase());
		this.accessionDao=new AccessionDaoImpl(config.getRpmDatabase());
	}

	protected static long QUEUE_POLL_TIME_MS = TimeUnit.SECONDS.toMillis(5);
	protected static long QUEUE_WAIT_TIME_MS = TimeUnit.MINUTES.toMillis(10);

	public String createNewPricedAccn(SeleniumBaseTest b, String env, String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String testDb, String wsorgAlias, String endpoint, String xapEnv, String eType, String engConfigDB, boolean hasLoggedIn) throws Exception
	{
    	LOG.info("***** Start Create New Priced Accession *****");
    	
		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		MenuNavigation navigation = new MenuNavigation(driver, config);
		FileMaintenanceNavigation fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		TaskScheduler taskScheduler = new TaskScheduler(driver);
		
		LOG.info("*** Step 1 Actions: - Turn off system setting for Price Engine");
    	Assert.assertEquals(daoManagerXifinRpm.setSysSettingsPriceEngine(testDb), 6);
    	daoManagerPlatform.setValuesFromTable("cln", "CLN.B_BILLING_ASSIGNMENT_REQ =0, CLN.B_CLN_PRC_SUSP_THRU_EOM = 0", "cln_abbrev not like 'AUTO%' and (CLN.B_BILLING_ASSIGNMENT_REQ =1 or CLN.B_CLN_PRC_SUSP_THRU_EOM = 1)", testDb);
		
		LOG.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");
		if (!hasLoggedIn){
			xifinAdminUtils.clearDataCache();
		}
		else{
			xifinAdminUtils.clearDataCache();
		}
		Thread.sleep(5000);

		LOG.info("*** Step 3 Actions: - Send request to create an accession via Accession WebService");
		TestDataSetup testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, wsorgAlias, wsUsername, wsPassword, testDb, endpoint);
    	
    	LOG.info("*** Step 3 Expected Results: - Verify that the new Accession was generated");
    	LOG.info("       New Accession ID: " + accnId);
		Assert.assertNotNull(accnId);
    	
    	LOG.info("*** Step 3 Expected Results: - Verify that the new accession is in Q_FR_Pending table");
    	Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1, "       Accession ID " + accnId + " should in Q_FR_PENDING table.");
    	 
		LOG.info("*** Step 4 Actions: - Run PF-OE Posting Engine twice");		
	  	xifinAdminUtils.runPFEngine(b, email, password, xapEnv, eType, engConfigDB, true);
    	Thread.sleep(5000);     	
    	
	  	xifinAdminUtils.runPFEngineInCurrentWindow(b, email, password, xapEnv, eType, engConfigDB, true);
    	Thread.sleep(5000);				

		LOG.info("*** Step 4 Expected Results: - Verify that the new accession is in Q_Price table");
		Assert.assertEquals(daoManagerXifinRpm.getCountFromQPriceTableByAccnId(accnId, testDb), 1);		
		
		LOG.info("*** Step 5 Actions: - Navigate to RPM");
		navigation.navigateToRPM();

		LOG.info("*** Step 5 Expected Results: - Verify that the user is logged into RPM successfully");
		HeaderNavigation headerNavigation = new HeaderNavigation(driver, config);
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
		TaskStatus taskStatus = new TaskStatus(driver);
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
		
		if(taskStatus.isEngineActive("Pricing Engine")){
			 return "false";
 		} 
		
		return accnId;
	}
	
	public String createPricedAccn(SeleniumBaseTest b, String env, String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String testDb, String wsorgAlias, String endpoint, String xapEnv, String eType1, String eType2, String engConfigDB, boolean hasLoggedIn) throws Exception {
    	LOG.info("***** Start Creating New Priced Accession *****");    	

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		MenuNavigation navigation = new MenuNavigation(driver, config);
		
		LOG.info("*** Actions: - Turn off system setting for Price Engine");
    	Assert.assertEquals(daoManagerXifinRpm.setSysSettingsPriceEngine(testDb), 6);
    	daoManagerPlatform.setValuesFromTable("cln", "CLN.B_BILLING_ASSIGNMENT_REQ =0, CLN.B_CLN_PRC_SUSP_THRU_EOM = 0", "cln_abbrev not like 'AUTO%' and (CLN.B_BILLING_ASSIGNMENT_REQ =1 or CLN.B_CLN_PRC_SUSP_THRU_EOM = 1)", testDb);

		LOG.info("*** Actions: - Clear System Cache in Xifin Admin Portal");
		if (!hasLoggedIn){
			xifinAdminUtils.clearDataCache();
		}
		else{
			xifinAdminUtils.clearDataCache();
		}		

		LOG.info("*** Actions: - Send request to create an accession via Accession WebService");
		TestDataSetup testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, wsorgAlias, wsUsername, wsPassword, testDb, endpoint);
    	
    	LOG.info("*** Expected Results: - Verify that the new Accession was generated");
    	LOG.info("       New Accession ID: " + accnId);
		Assert.assertNotNull(accnId);

		daoManagerPlatform.setValuesFromTable("q_fr_pending", "Q_FR_PENDING.B_OE_PERFORMED=1", "Q_FR_PENDING.PK_ACCN_ID='"+accnId+"' and Q_FR_PENDING.B_OE_PERFORMED=0", testDb);
		xifinAdminUtils.clearDataCache();

		LOG.info("*** Step 3 Expected Results: - Verify that the new accession is in Q_FR_Pending table");
		Assert.assertTrue(accessionDao.isInQFrPendingQueue(accnId), "       Accession ID " + accnId + " should in Q_FR_PENDING table.");

		LOG.info("*** Step 4 Actions: - Run PF-OE Posting Engine");
		LOG.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));

		LOG.info("*** Step 4 Expected Results: - Verify that the new accession is not in Q_FR_Pending table");
		Assert.assertFalse(accessionDao.isInQFrPendingQueue(accnId), "       Accession ID " + accnId + " should in Q_FR_PENDING table.");

		LOG.info("*** Step 5 Actions: - Run PF-Eligibility Engine and pricing engine");
//		if(accessionDao.isInEligibilityQueue(accnId)) {
//			Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
//			Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));
//		}
//		else if(accessionDao.isInPricingQueue(accnId))
			Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

		LOG.info("*** Step 6 Expected Results: - Ensure the new accession is NOT in Q_Price table");
		Assert.assertFalse(accessionDao.isInPricingQueue(accnId));

		return accnId;
	}

	//This method find that the passed string value exists in the List
	public <T extends Comparable<T>> boolean isValueExistInList(List<T> list, String val) {		
		boolean flag = false;

		for(int i=0; i<list.size(); i++){
			if(list.get(i)!=null && list.get(i).equals(val)){
				LOG.info("       Match found " + list.get(i));
				flag = true;
				break;
			}
		}			
		
		return flag;
	}
	
}
