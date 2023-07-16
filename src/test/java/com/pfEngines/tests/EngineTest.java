package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.accession.accessionNavigation.AccessionNavigation;
import com.overall.accession.accessionProcessing.superSearch.SuperSearch;
import com.overall.accession.accessionProcessing.superSearch.SuperSearchResults;
import com.overall.accession.orderProcessing.AccnTestUpdate;
import com.overall.headerNavigation.HeaderNavigation;
import com.overall.menu.MenuNavigation;
import com.overall.utils.AccessionUtils;
import com.overall.utils.AccnDetailUtils;
import com.overall.utils.AccnTestUpdateUtils;
import com.xifin.xap.menu.Navigation;
import com.xifin.xap.scheduler.Scheduler;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.FileManipulation;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class EngineTest extends SeleniumBaseTest  {

	private SsoLogin ssoLogin;
	private HeaderNavigation headerNavigation;
	private AccessionNavigation accessionNavigation;
	private SuperSearch superSearch;
	private SuperSearchResults superSearchResults;
	private AccnTestUpdate accnTestUpdate;
	private TestDataSetup testDataSetup;
	private TimeStamp timeStamp;
	private AccessionUtils accessionUtils;
	private File upOne = new File(System.getProperty("user.dir")).getParentFile();
	private String jvmArgs = " -Xms128M -Xmx512M";
	private String engineType = " -Dengine=";
	private String sysProp = " -Dlog4j.configuration=file:" + upOne + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "enginelog.xml -Dengine.properties.file=engine.properties -Dlog.output.path=" + upOne + File.separator + "target" + " -Dspring.profiles.active=prod";
	private String eRunner = " com.xifin.engine.EngineRunner ";
	private int instanceId = 1;
	private Navigation navigation;
	private Scheduler scheduler;
	private XifinAdminUtils xifinAdminUtils;
	private MenuNavigation menuNavigation;
	private AccnTestUpdateUtils accnTestUpdateUtils;
	private AccnDetailUtils accessionDetailUtils;
	public static long QUEUE_WAIT_TIME = TimeUnit.MINUTES.toMillis(10);
	private static final String OS_NAME = System.getProperty("os.name");
	private static final String WINDOWS = "windows";

	@Test(priority = 1, description = "RPM - Test Import Engine - Format Type HL7")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "eType", "eJar", "file", "formatType"})
	public void testXP_importEngineByHL7(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String eType, String eJar, String file, String formatType) throws Exception {
    	logger.info("*** Testing - testXP_importEngineByHL7 ***");

    	logger.info("*** Step 1 Actions: - Generate HL7 file");
		FileManipulation fm = new FileManipulation();

		List<String> targetList = new ArrayList<String>();
		targetList.add(0, "AUTOTEST0001");
		targetList.add(1, "1114927050");
		targetList.add(2, "001.0");
		targetList.add(3, "GLUCOSE");
		targetList.add(4, "ACME");

		List<String> newValList = new ArrayList<String>();
		newValList.add(0, "xxxxx");
		newValList.add(1, "xxxxx");
		newValList.add(2, "xxxxx");
		newValList.add(3, "xxxxx");
		newValList.add(4, "xxxxx");

		logger.info("*** Step 1 Expected Results: - HL7 file gets generated");
		Assert.assertTrue(fm.generateFormatTypeContent(config.getProperty(PropertyMap.ORGALIAS), upOne + File.separator + "src"  + File.separator + "test" + File.separator + "resources"  + File.separator + file, targetList, newValList, formatType));

		logger.info("*** Step 2 Actions: - Run ImportEngine");
		engineType = engineType + eType;


		ArrayList<String> argsList = new ArrayList();
		argsList.add(jvmArgs);
		argsList.add(eJar);
		argsList.add(" -Dorg=" + config.getProperty(PropertyMap.ORGALIAS) + engineType + sysProp + eRunner + config.getProperty(PropertyMap.ORGALIAS) + " " + instanceId);

	}

	@Test(priority = 1, description = "RPM - Test Import Engine - Valid Format Type USPS")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "eType", "eJar", "file", "formatType", "newAddr1", "newZip"})
	public void testXP_importEngineByValidUSPS(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String eType, String eJar, String file, String formatType, String newAddr1, String newZip) throws Exception {
    	logger.info("*** Testing - testXP_importEngineByValidUSPS ***");

    	accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		String accnId = accessionUtils.createAccnWithEPI(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));

		logger.info("*** Step 17 Actions: - Generate USPS file");
    	List<String> ptPhiList = daoManagerXifinRpm.getPtPHIInfoFromPTPHIByAccnId(accnId, testDb);
    	String addr1 = ptPhiList.get(5);
    	String zip = ptPhiList.get(8);

    	FileManipulation fm = new FileManipulation();

		List<String> targetList = new ArrayList<String>();
		targetList.add(0, "A201106021148472");
		targetList.add(1, "315 PARK VILLAGE");
		targetList.add(2, "55306-4923");
		targetList.add(3, "92111");
		targetList.add(4, "12345 APPLE DR");

		List<String> newValList = new ArrayList<String>();
		newValList.add(0, accnId);
		newValList.add(1, addr1);
		newValList.add(2, zip);
		newValList.add(3, newZip);
		newValList.add(4, newAddr1);

		logger.info("*** Step 17 Expected Results: - USPS file gets generated");
		Assert.assertTrue(fm.generateFormatTypeContent(config.getProperty(PropertyMap.ORGALIAS), upOne + File.separator + "src"  + File.separator + "test" + File.separator + "resources"  + File.separator + file, targetList, newValList, formatType));

		logger.info("*** Step 18 Actions: - Run ImportEngine");
		engineType = engineType + eType;

		ArrayList<String> argsList = new ArrayList<String>();
		argsList.add(jvmArgs);
		argsList.add(eJar);
		argsList.add(" -Dorg=" + config.getProperty(PropertyMap.ORGALIAS) + engineType + sysProp + eRunner + config.getProperty(PropertyMap.ORGALIAS) + " " + instanceId);

		Thread.sleep(2000);

		logger.info("*** Step 18 Expected Results: - USPS file gets removed from the incoming");
		String dirBase = fm.getDirBase();
		File f = new File(dirBase + File.separator + "incoming" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists());

		logger.info("*** Step 18 Expected Results: - USPS file is stored in processed directory");
		f = new File(dirBase + File.separator + "processed" + File.separator + fm.getFileName());
		Assert.assertTrue(f.exists());

		logger.info("*** Step 18 Expected Results: - USPS file is not in dup directory");
		f = new File(dirBase + File.separator + "dup" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists());

		logger.info("*** Step 18 Expected Results: - USPS file is not in errored directory");
		f = new File(dirBase + File.separator + "errored" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists());

		ptPhiList = daoManagerXifinRpm.getPtPHIInfoFromPTPHIByAccnId(accnId, testDb);
    	String updatedAddr1 = ptPhiList.get(5);
    	String updatedZip = ptPhiList.get(8);

    	logger.info("*** Step 18 Expected Results: - USPS file zip code in db gets updated");
    	Assert.assertEquals(updatedAddr1, newAddr1);

    	logger.info("*** Step 18 Expected Results: - USPS file addr1 in db gets updated");
    	Assert.assertEquals(updatedZip, newZip);
	}

	@Test(priority = 1, description = "RPM - Test Import Engine - Invalid Format Type USPS")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "eType", "eJar", "file", "formatType", "newAccnId"})
	public void testXP_importEngineByInvalidUSPS(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String eType, String eJar, String file, String formatType, String newAccnId) throws Exception {
    	logger.info("*** Testing - testXP_importEngineByInvalidUSPS ***");

		logger.info("*** Step 1Actions: - Generate USPS file");
    	FileManipulation fm = new FileManipulation();

		List<String> targetList = new ArrayList<String>();
		targetList.add(0, "A201106021148472");

		List<String> newValList = new ArrayList<String>();
		newValList.add(0, newAccnId);

		logger.info("*** Step 1 Expected Results: - USPS file gets generated");
		Assert.assertTrue(fm.generateFormatTypeContent(config.getProperty(PropertyMap.ORGALIAS), upOne + File.separator + "src"  + File.separator + "test" + File.separator + "resources"  + File.separator + file, targetList, newValList, formatType));

		logger.info("*** Step 2 Actions: - Run ImportEngine");
		engineType = engineType + eType;

		ArrayList<String> argsList = new ArrayList<String>();
		argsList.add(jvmArgs);
		argsList.add(eJar);
		argsList.add(" -Dorg=" + config.getProperty(PropertyMap.ORGALIAS) + engineType + sysProp + eRunner + config.getProperty(PropertyMap.ORGALIAS) + " " + instanceId);

		Thread.sleep(2000);

		logger.info("*** Step 2 Expected Results: - USPS file gets removed from the incoming");
		String dirBase = fm.getDirBase();
		File f = new File(dirBase + File.separator + "incoming" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists());

		logger.info("*** Step 2 Expected Results: - USPS file is stored in processed directory");
		f = new File(dirBase + File.separator + "processed" + File.separator + fm.getFileName());
		Assert.assertTrue(f.exists());

		logger.info("*** Step 2 Expected Results: - USPS file is not in dup directory");
		f = new File(dirBase + File.separator + "dup" + File.separator + fm.getFileName());
		Assert.assertTrue(!f.exists());

		logger.info("*** Step 2 Expected Results: - USPS file is not in errored directory");
		f = new File(dirBase + File.separator + "errored" + File.separator + fm.getFileName() + ".error");
		Assert.assertTrue(f.exists());
	}

	@Test(priority = 1, description = "Billing Assignment Engine - Client is Unassigned and not flagged to perform Billing Assignment")
	@Parameters({"env", "email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "eType", "xapEnv", "engConfigDB"})
	public void testPFER_18(String env, String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String eType, String xapEnv, String engConfigDB) throws Exception {
    	logger.info("*** Testing - testPFER_18 ***");

		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that the user and customer id are correct");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Send WS request to create a new accession with a Client that has PerformBillingAssignment turned On");
    	testDataSetup = new TestDataSetup(driver);
    	String accnId = testDataSetup.sendWSReqestTestCase(project, testSuite, testCase, propLevel, propName, config.getProperty(PropertyMap.ORGALIAS), wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));
    	logger.info("        AccnID: " + accnId);

    	logger.info("*** Step 2 Expected Results: - Verify that a new Accession was genrated");
    	Assert.assertTrue(accnId != null, "        Did not generate a new Accession.");

    	logger.info("*** Step 2 Expected Results: - Verify that the Accession is in the Q_CLN_BILLING_ASSIGNMENT");

    	ArrayList<String> clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
    	Assert.assertFalse(clnBillingAssignInfoList.isEmpty(), "        Accession " + accnId + " should be in Q_CLN_BILLING_ASSIGNMENT.");

		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();

		logger.info("*** Step 3 Actions: - Navigate To Xifin Admin Portal");
		navigation = new Navigation(new URL(config.getProperty(PropertyMap.XIFINADMINPORTAL_URL)));
		navigation.navigateToXifinAdminPortalLink(driver);
		switchToPopupWin();
		ssoLogin.login(email, password);

		logger.info("*** Step 3 Expected Results: - Verify that Scheduler Tab displays");
		scheduler = new Scheduler(driver);
		Assert.assertTrue(scheduler.schudulerTab().isDisplayed(), "        Scheduler Tab should display.");

		logger.info("*** Step 4 Actions: - Click on Scheduler Tab");
		scheduler.schudulerTab().click();

		logger.info("*** Step 4 Expected Results: - Verify that Scheduler Page displays");
		Assert.assertTrue(scheduler.schedulerTitleText().getText().contains("Scheduler"), "        Scheduler Page should show.");

		logger.info("*** Step 5 Actions: - Select Billing Assignment Engine and run it");
		ArrayList<String> engineConfigList = xifinAdminPortalDao.getEngineConfigInfoFromEngineConfigByEngineTypOrgAlias(eType, config.getProperty(PropertyMap.ORGALIAS), engConfigDB);
		String engineId = engineConfigList.get(0).trim().toString();
		Assert.assertTrue(scheduler.runNowBtn(engineId).isDisplayed(), "        Billing Assignment Engine Run Now button should show.");
		scheduler.runNowBtn(engineId).click();
		logger.info("        Clicked Run Now button.");

		logger.info("*** Step 5 Expected Results: - Verify that Billing Assignment Engine has finished");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        Assert.assertFalse(xifinAdminUtils.isEngineRunning(eType, engConfigDB, 100), "        Engine " + eType + " was running after 100 seconds.");

        logger.info("*** Step 5 Expected Results: - Verify that the Accession stays in the Q_CLN_BILLING_ASSIGNMENT");
    	clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
    	Assert.assertFalse(clnBillingAssignInfoList.isEmpty(), "        Accession " + accnId + " should stay in Q_CLN_BILLING_ASSIGNMENT.");

    	logger.info("*** Step 6 Actions: - Turn off the Billing Assignment Req for the Client on the Accession in DB");

		String clnAbbrev = daoManagerXifinRpm.getClientAbbrevIdByAccnId(accnId, testDb);
		daoManagerXifinRpm.setValuesFromCLNByClnAbbrev(clnAbbrev, "B_BILLING_ASSIGNMENT_REQ = 0", testDb);
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		logger.info("*** Step 7 Actions: - Clear the System Data Cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        logger.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");
        xifinAdminUtils.clearDataCache();

		logger.info("*** Step 8 Actions: - Navigate To Xifin Admin Portal");
		navigation.navigateToXifinAdminPortalLink(driver);
		switchToPopupWin();
		//Don't need to login again
		//ssoLogin.login(email, password);

		logger.info("*** Step 8 Expected Results: - Verify that Scheduler Tab displays");
		Assert.assertTrue(scheduler.schudulerTab().isDisplayed(), "        Scheduler Tab should display.");

		logger.info("*** Step 9 Actions: - Click on Scheduler Tab");
		scheduler.schudulerTab().click();

		logger.info("*** Step 9 Expected Results: - Verify that Scheduler Page displays");
		Assert.assertTrue(scheduler.schedulerTitleText().getText().contains("Scheduler"), "        Scheduler Page should show.");

		logger.info("*** Step 10 Actions: - Select Billing Assignment Engine and run it");
		Assert.assertTrue(scheduler.runNowBtn(engineId).isDisplayed(), "        Billing Assignment Engine Run Now button should show.");
		scheduler.runNowBtn(engineId).click();
		logger.info("        Clicked Run Now button.");

		logger.info("*** Step 10 Expected Results: - Verify that Billing Assignment Engine has finished");
		Assert.assertFalse(xifinAdminUtils.isEngineRunning(eType, engConfigDB, 100), "        Engine " + eType + " was running after 100 seconds.");

		logger.info("*** Step 10 Expected Results: - Verify that the Accession got processed and not in the Q_CLN_BILLING_ASSIGNMENT");
    	clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
    	Assert.assertTrue(clnBillingAssignInfoList.isEmpty(), "        Accession " + accnId + " should NOT in Q_CLN_BILLING_ASSIGNMENT.");

		driver.close();
	}

	protected boolean isOutOfPricingQueue(String accnId, long maxTime) throws Exception
	{
		long startTime = System.currentTimeMillis();
		maxTime += startTime;
		boolean isInQueue = accessionDao.isInPricingQueue(accnId);
		while (isInQueue && System.currentTimeMillis() < maxTime)
		{
			logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
			Thread.sleep(QUEUE_POLL_TIME_MS);
			isInQueue = accessionDao.isInPricingQueue(accnId);
		}
		return !isInQueue;
	}

	protected boolean isOutOfQFrPendingQueue(String accnId, long maxTime) throws Exception
	{
		long startTime = System.currentTimeMillis();
		maxTime += startTime;
		boolean isInQueue = accessionDao.isInQFrPendingQueue(accnId);
		while (isInQueue && System.currentTimeMillis() < maxTime)
		{
			logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
			Thread.sleep(QUEUE_POLL_TIME_MS);
			isInQueue = accessionDao.isInQFrPendingQueue(accnId);
		}
		return !isInQueue;
	}

	public static String getBaseDir() throws IOException
	{
		String baseDir = null;

		if (StringUtils.containsIgnoreCase(OS_NAME, WINDOWS))
		{
			baseDir = File.separator + File.separator + "a3unity01-mp" + File.separator + "cnfs01";
		} else
		{
			baseDir = File.separator + "home";
		}
		return baseDir;
	}
}

