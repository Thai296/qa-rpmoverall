package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.accession.accessionProcessing.LoadAccession;
import com.overall.headerNavigation.HeaderNavigation;
import com.overall.menu.MenuNavigation;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class EPMatchEngineTest extends SeleniumBaseTest  {

	private HeaderNavigation headerNavigation;
	private TestDataSetup testDataSetup;
	private TimeStamp timeStamp;
	private XifinAdminUtils xifinAdminUtils;
	private MenuNavigation navigation;
	private LoadAccession loadAccession;
	private AccessionDetail accessionDetail;

	@BeforeSuite(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "disableBrowserPlugins"})
	public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, @Optional String disableBrowserPlugins)
	{
		try
		{
			logger.info("Running BeforeSuite");
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			updateSystemSetting(SystemSettingMap.SS_FR_PENDING_FOR_ELIG, "False", "0");
			updateSystemSetting(SystemSettingMap.SS_PTESTIMATION_SUBMITTED_BY_ELIGENGINE, "False", "0");
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
	@AfterSuite(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "xapEnv", "engConfigDB", "disableBrowserPlugins"})
	public void afterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, String xapEnv, String engConfigDB, @Optional String disableBrowserPlugins)
	{
		try
		{
			logger.info("Running AfterSuite");
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			logIntoSso(ssoUsername, ssoPassword);
			//Clear Cache
			XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
			xifinAdminUtils.clearDataCache();
		}
		catch (Exception e)
		{
			Assert.fail("Error running AfterSuite", e);
		}
		finally
		{
			quitWebDriver();
		}
	}





	@Test(priority = 1, description = "Priced 3rd party pyr accn with SUBID (FIELD ID=4) error")
	@Parameters({"project", "testSuite", "testCase"})
	public void testPFER_545(String project, String testSuite, String testCase) throws Exception {
		logger.info("========== Testing - testPFER_545 ==========");

		timeStamp = new TimeStamp(driver);
		logger.info("*** Step 1 Actions: - Send WS request to create a new Priced 3rd party payor accession and a copy of the accession without SUBID");
		logger.info("*** Actions: - Send WS request to create a new Priced 3rd party payor accession");
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		logger.info("TestProperties:"+testProperties);
		String accnId = testProperties.getProperty("CopiedAccnId");
		String mtchAccnId = testProperties.getProperty("NewAccnID");
		logger.info("        AccnID: " + accnId);
		logger.info("        MatchedAccnID: " + mtchAccnId);

		logger.info("*** Step 2 Expected Results: - Verify that a new copied accession was generated");
		Assert.assertTrue(accnId != null, "        Did not generate a new Accession.");

		ArrayList<String> clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
		if(!clnBillingAssignInfoList.isEmpty()){
			daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 2", testDb);
			logger.info("Waiting for accession to complete BA, accnId=" + accnId);
			Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));
		}
		logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Step 2 Expected Results: - Verify that the copied accession has SUBID accn_pyr_err and is in Unbillable queue (8)");
		List<List<String>> accnInfoPyrList = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
		String accnPyrId = accnInfoPyrList.get(0).get(0);

		List<AccnPyr> accnPyrList = accessionDao.getAccnPyrs(accnId);
		AccnPyr accnPyr = accnPyrList.get(0);

		Assert.assertTrue(StringUtils.isEmpty(accnPyr.getSubsId()), "        Copied Accession ID " + accnId + " should not have a subs id.");

		ArrayList<String> accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
		Assert.assertTrue(accnQueInfoList.get(3).equals("8"), "        Copied Accession ID " + accnId + " should be in Q_Typ = 8 (Unbillable).");

		String currDt = timeStamp.getCurrentDate();
		String errCd = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where abbrev = 'SUBID'", testDb).get(0);
		List<String> accnPyrErrInfoList = daoManagerXifinRpm.getAccnPyErrFromACCNPYRERRByAccnId(accnId, "1", currDt, errCd, testDb);
		Assert.assertTrue(accnPyrErrInfoList.size() > 0, "        copied Accession ID " + accnId + " should have SUBID accn_pyr_err.");

		logger.info("*** Actions: - Wait for PF EP Workflow Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Step 4 Expected Results: - Verify that the accession is moved to EP Auto Match queue (10)");
		accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
		Assert.assertTrue(accnQueInfoList.get(3).equals("10"), "        copied Accession ID " + accnId + " should be in Q_Typ = 10 (EP Auto Match).");

		logger.info("*** Step 4 Expected Results: - Verify that the accession is added to Q_EP_AUTO_MTCH table");
		ArrayList<String> qEpAutoMtchInfoList = daoManagerPlatform.getQEPAutoMtchByConditon("pk_accn_id = '" + accnId + "'", testDb);
		Assert.assertTrue(qEpAutoMtchInfoList.size() > 0, "        copied Accession ID " + accnId + " should be in Q_EP_AUTO_MTCH table.");

		logger.info("*** Step 4 Expected Results: - Verify that the accession is added to Q_EP_AUTO_MTCH_ERR table");
		ArrayList<String> qEpAutoMtchErrInfoList = daoManagerPlatform.getQEPAutoMtchErrByConditon("pk_accn_id = '" + accnId + "' and pk_err_cd = " + errCd, testDb);
		Assert.assertTrue(qEpAutoMtchErrInfoList.size() > 0, "        copied Accession ID " + accnId + " should be in Q_EP_AUTO_MTCH_ERR table.");

		logger.info("*** Step 6 Actions: -Wait for PF EP Match Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_AUTO_MTCH, QUEUE_WAIT_TIME_MS));

		logger.info("*** Step 6 Expected Results: - Verify that the accession is moved out of Q_EP_AUTO_MTCH, Q_EP_AUTO_MTCH_ERR and ACCN_POTNL_MTCH tables");
		qEpAutoMtchInfoList = daoManagerPlatform.getQEPAutoMtchByConditon("pk_accn_id = '" + accnId + "'", testDb);
		Assert.assertTrue(qEpAutoMtchInfoList.size() == 0, "        copied Accession ID " + accnId + " should be moved out of Q_EP_AUTO_MTCH table.");

		qEpAutoMtchErrInfoList = daoManagerPlatform.getQEPAutoMtchErrByConditon("pk_accn_id = '" + accnId + "' and pk_err_cd = " + errCd, testDb);
		Assert.assertTrue(qEpAutoMtchErrInfoList.size() == 0, "        copied Accession ID " + accnId + " should be moved out of Q_EP_AUTO_MTCH_ERR table.");

		ArrayList<String> qAccnPotnlMtchInfoList = daoManagerPlatform.getAccnPotnlMtchByConditon("PK_ERR_ACCN_ID = '" + accnId + "' and PK_ERR_PYR_PRIO = 1", testDb);
		Assert.assertTrue(qAccnPotnlMtchInfoList.size() == 0, "        copied Accession ID " + accnId + " should be moved out of ACCN_POTNL_MTCH table.");

		logger.info("*** Step 6 Expected Results: - Verify that the subs id is added in ACCN_PYR table for the accession");
		List<AccnPyr> mtchdAccnPyrList = accessionDao.getAccnPyrs(mtchAccnId);
		String mtchAccnPyrSubsId = mtchdAccnPyrList.get(0).getSubsId();

		List<AccnPyr> accnPyrList1 = accessionDao.getAccnPyrs(accnId);
		Assert.assertTrue(accnPyrList1.get(0).getSubsId().equals(mtchAccnPyrSubsId), "        Copied Accession ID " + accnId + " subs id should be updated to " + mtchAccnPyrSubsId);

		logger.info("*** Step 6 Expected Results: - Verify that the SUBID error is fixed in ACCN_PYR_ERR table for the accession");
		accnPyrErrInfoList = daoManagerXifinRpm.getAccnPyErrFromACCNPYRERRByAccnId(accnId, "1", currDt, errCd, testDb);
		Assert.assertTrue(accnPyrErrInfoList.get(3).equals(currDt), "        Copied Accession ID " + accnId + " should have SUBID error fixed in accn_pyr_err table.");

		logger.info("*** Step 6 Expected Results: - Verify that the accesssion is moved out of EP Auto Match queue and moved to the next queue");
		accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
		Assert.assertFalse(accnQueInfoList.get(3).equals("10"), "        Copied Accession ID " + accnId + " should be out of EP Auto Match queue.");

		logger.info("*** Step 7 Actions: - Clear test data");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyrId, testDb);
		Assert.assertNotEquals(daoManagerAccnWS.deletePyrReqmntsFldByPyrAbrrev(pyrAbbrev, testDb), 0);
		Assert.assertNotEquals(daoManagerAccnWS.deletePyrReqmntsByPyrAbrrev(pyrAbbrev, testDb), 0);

	}

	@Test(priority = 1, description = "Priced 3rd party pyr accn with SUBID (FIELD ID=4) err,matched accn status=62(Obsolete)")
	@Parameters({"project", "testSuite", "testCase"})
	public void testPFER_546(String project, String testSuite, String testCase) throws Exception {
		logger.info("========== Testing - testPFER_546 ==========");

		timeStamp = new TimeStamp(driver);

		logger.info("*** Actions: - Send WS request to create a new Priced 3rd party payor accession and a copy of the accession without SUBID");
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		logger.info("TestProperties:"+testProperties);
		String accnId = testProperties.getProperty("CopiedAccnId");
		String mtchAccnId = testProperties.getProperty("NewAccnID");
		logger.info("        AccnID: " + accnId);
		logger.info("        MatchedAccnID: " + mtchAccnId);

		logger.info("*** Expected Results: - Verify that a new copied accession was generated");
		Assert.assertTrue(accnId != null, "        Did not generate a new Accession.");

		ArrayList<String> clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
		if(!clnBillingAssignInfoList.isEmpty()){
			daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 2", testDb);
			logger.info("Waiting for accession to complete BA, accnId=" + accnId);
			Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));
		}
		logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Expected Results: - Verify that the copied accession has SUBID accn_pyr_err and is in Unbillable queue (8)");
		List<List<String>> accnInfoPyrList = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
		String accnPyrId = accnInfoPyrList.get(0).get(0);

		ArrayList<String> accnPyrInfoList = daoManagerXifinRpm.getAccnPyrInfoFromACCNPYRByAccnIdPyrIdPyrPrio(accnId, accnPyrId, "1", testDb);
		Assert.assertTrue(accnPyrInfoList.get(4) == "", "        copied Accession ID " + accnId + " should not have a subs id.");

		ArrayList<String> accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
		Assert.assertTrue(accnQueInfoList.get(3).equals("8"), "        copied Accession ID " + accnId + " should be in Q_Typ = 8 (Unbillable).");

		String currDt = timeStamp.getCurrentDate();
		String errCd = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where abbrev = 'SUBID'", testDb).get(0);
		List<String> accnPyrErrInfoList = daoManagerXifinRpm.getAccnPyErrFromACCNPYRERRByAccnId(accnId, "1", currDt, errCd, testDb);
		Assert.assertTrue(accnPyrErrInfoList.size() > 0, "        copied Accession ID " + accnId + " should have SUBID accn_pyr_err.");

		logger.info("*** Actions: - Wait for PF EP Workflow Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Actions: - Manually change the matched accession status to 62 (Obsolete) in ACCN table in DB");
		int rowCount = daoManagerPlatform.setValuesFromACCN("FK_STA_ID = 62", "pk_accn_id = '" + mtchAccnId + "'", testDb);
		Assert.assertTrue(rowCount > 0, "        ACCN table should be updated for matched Accession ID = " + mtchAccnId);

		logger.info("*** Expected Results: - Verify that the accession is moved to EP Auto Match queue (10)");
		accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
		Assert.assertTrue(accnQueInfoList.get(3).equals("10"), "        copied Accession ID " + accnId + " should be in Q_Typ = 10 (EP Auto Match).");

		logger.info("*** Expected Results: - Verify that the accession is added to Q_EP_AUTO_MTCH table");
		ArrayList<String> qEpAutoMtchInfoList = daoManagerPlatform.getQEPAutoMtchByConditon("pk_accn_id = '" + accnId + "'", testDb);
		Assert.assertTrue(qEpAutoMtchInfoList.size() > 0, "        copied Accession ID " + accnId + " should be in Q_EP_AUTO_MTCH table.");

		logger.info("*** Expected Results: - Verify that the accession is added to Q_EP_AUTO_MTCH_ERR table");
		ArrayList<String> qEpAutoMtchErrInfoList = daoManagerPlatform.getQEPAutoMtchErrByConditon("pk_accn_id = '" + accnId + "' and pk_err_cd = " + errCd, testDb);
		Assert.assertTrue(qEpAutoMtchErrInfoList.size() > 0, "        copied Accession ID " + accnId + " should be in Q_EP_AUTO_MTCH_ERR table.");

		String origMtchAccnStatus = daoManagerPlatform.getAccnInfoFromACCNByAccnId(mtchAccnId, testDb).get(2);

		logger.info("*** Actions: - Wait For PF EP Match Engine");
		Assert.assertTrue(errorProcessingDao.waitForEpMatchEngineToProcessRecord(accnId, QUEUE_WAIT_TIME_MS));

		logger.info("*** Expected Results: - Verify that the accession is NOT moved out of Q_EP_AUTO_MTCH, Q_EP_AUTO_MTCH_ERR and ACCN_POTNL_MTCH tables");
		qEpAutoMtchInfoList = daoManagerPlatform.getQEPAutoMtchByConditon("pk_accn_id = '" + accnId + "'", testDb);
		Assert.assertTrue(qEpAutoMtchInfoList.size() > 0, "        copied Accession ID " + accnId + " should NOT be moved out of Q_EP_AUTO_MTCH table.");

		ArrayList<String> qAccnPotnlMtchInfoList = daoManagerPlatform.getAccnPotnlMtchByConditon("PK_ERR_ACCN_ID = '" + accnId + "' and PK_ERR_PYR_PRIO = 1", testDb);
		Assert.assertTrue(qAccnPotnlMtchInfoList.size() > 0, "        copied Accession ID " + accnId + " should NOT be moved out of ACCN_POTNL_MTCH table.");
		Thread.sleep(3000);
		qEpAutoMtchErrInfoList = daoManagerPlatform.getQEPAutoMtchErrByConditon("pk_accn_id = '" + accnId + "' and pk_err_cd = " + errCd, testDb);
		Assert.assertTrue(qEpAutoMtchErrInfoList.size() > 0, "        copied Accession ID " + accnId + " should NOT be moved out of Q_EP_AUTO_MTCH_ERR table.");

		logger.info("*** Expected Results: - Verify that the accession Q_EP_AUTO_MTCH_ERR.MTCH_STATUS = 0");
		Assert.assertTrue(qEpAutoMtchErrInfoList.get(13).equals("0"), "        copied Accession ID " + accnId + " Q_EP_AUTO_MTCH_ERR.MTCH_STATUS = 0.");

		logger.info("*** Expected Results: - Verify that the subs id is NOT added in ACCN_PYR table for the accession");
		accnPyrInfoList = daoManagerXifinRpm.getAccnPyrInfoFromACCNPYRByAccnIdPyrIdPyrPrio(accnId, accnPyrId, "1", testDb);
		Assert.assertTrue(accnPyrInfoList.get(4).equals(""), "        copied Accession ID " + accnId + " subs id should NOT be updated.");

		logger.info("*** Expected Results: - Verify that the SUBID error is NOT fixed in ACCN_PYR_ERR table for the accession");
		accnPyrErrInfoList = daoManagerXifinRpm.getAccnPyErrFromACCNPYRERRByAccnId(accnId, "1", currDt, errCd, testDb);
		Assert.assertTrue(accnPyrErrInfoList.get(3) == null, "        copied Accession ID " + accnId + " should NOT have SUBID error fixed in accn_pyr_err table.");

		logger.info("*** Expected Results: - Verify that the accesssion is NOT moved out of EP Auto Match queue and moved to the next queue");
		accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
		Assert.assertTrue(accnQueInfoList.get(3).equals("10"), "        copied Accession ID " + accnId + " should stays in the EP Auto Match queue.");

		logger.info("*** Actions: - Clear test data");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyrId, testDb);
		Assert.assertNotEquals(daoManagerAccnWS.deletePyrReqmntsFldByPyrAbrrev(pyrAbbrev, testDb), 0);
		Assert.assertNotEquals(daoManagerAccnWS.deletePyrReqmntsByPyrAbrrev(pyrAbbrev, testDb), 0);
		daoManagerPlatform.setValuesFromACCN("FK_STA_ID = " + origMtchAccnStatus, "pk_accn_id = '" + mtchAccnId + "'", testDb);

	}

	@Test(priority = 1, description = "Run Engine with StaId!=(62,41), Errored accn PyrId=SS#84 (UNK)")
	@Parameters({"project", "testSuite", "testCase"})
	public void testPFER_540( String project, String testSuite, String testCase) throws Exception {
		logger.info("========== Testing - testPFER_540 ==========");

		timeStamp = new TimeStamp(driver);


		logger.info("*** Actions: - Send WS request to create a new Priced 3rd party payor accession and a copy of the accession without SUBID");
		testDataSetup = new TestDataSetup(driver);

		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		logger.info("TestProperties:"+testProperties);
		String accnId = testProperties.getProperty("CopiedAccnId");
		String mtchAccnId = testProperties.getProperty("NewAccnID");
		logger.info("        AccnID: " + accnId);
		logger.info("        MatchedAccnID: " + mtchAccnId);


		logger.info("*** Expected Results: - Verify that a new copied accession was generated");
		Assert.assertTrue(accnId != null, "        Did not generate a new Accession.");

		ArrayList<String> clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
		if(!clnBillingAssignInfoList.isEmpty()){
			daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 2", testDb);
			logger.info("Waiting for accession to complete BA, accnId=" + accnId);
			Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));
		}
		logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Expected Results: - Verify that the copied accession has SUBID accn_pyr_err and is in Unbillable queue (8)");
		List<List<String>> accnInfoPyrList = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
		String accnPyrId = accnInfoPyrList.get(0).get(0);

		ArrayList<String> accnPyrInfoList = daoManagerXifinRpm.getAccnPyrInfoFromACCNPYRByAccnIdPyrIdPyrPrio(accnId, accnPyrId, "1", testDb);
		Assert.assertTrue(accnPyrInfoList.get(4) == "", "        copied Accession ID " + accnId + " should not have a subs id.");

		ArrayList<String> accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
		Assert.assertTrue(accnQueInfoList.get(3).equals("8"), "        copied Accession ID " + accnId + " should be in Q_Typ = 8 (Unbillable).");

		String currDt = timeStamp.getCurrentDate();
		String errCd = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where abbrev = 'SUBID'", testDb).get(0);
		List<String> accnPyrErrInfoList = daoManagerXifinRpm.getAccnPyErrFromACCNPYRERRByAccnId(accnId, "1", currDt, errCd, testDb);
		Assert.assertTrue(accnPyrErrInfoList.size() > 0, "        copied Accession ID " + accnId + " should have SUBID accn_pyr_err.");

		logger.info("*** Actions: - Wait for PF EP Workflow Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Expected Results: - Verify that the accession is moved to EP Auto Match queue (10)");
		accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
		Assert.assertTrue(accnQueInfoList.get(3).equals("10"), "        copied Accession ID " + accnId + " should be in Q_Typ = 10 (EP Auto Match).");

		logger.info("*** Expected Results: - Verify that the accession is added to Q_EP_AUTO_MTCH table");
		ArrayList<String> qEpAutoMtchInfoList = daoManagerPlatform.getQEPAutoMtchByConditon("pk_accn_id = '" + accnId + "'", testDb);
		Assert.assertTrue(qEpAutoMtchInfoList.size() > 0, "        copied Accession ID " + accnId + " should be in Q_EP_AUTO_MTCH table.");

		logger.info("*** Expected Results: - Verify that the accession is added to Q_EP_AUTO_MTCH_ERR table");
		ArrayList<String> qEpAutoMtchErrInfoList = daoManagerPlatform.getQEPAutoMtchErrByConditon("pk_accn_id = '" + accnId + "' and pk_err_cd = " + errCd, testDb);
		Assert.assertTrue(qEpAutoMtchErrInfoList.size() > 0, "        copied Accession ID " + accnId + " should be in Q_EP_AUTO_MTCH_ERR table.");

		logger.info("*** Actions: - Manually change the Accession Payor to UNK (SS#84) in ACCN_PYR table in DB");
		String unkPyrId = daoManagerPlatform.getValueFromSYSTEMSETTINGBySettingId(84, testDb);
		int rowCount = daoManagerPlatform.setValuesFromTable("ACCN_PYR", "FK_PYR_ID = " + unkPyrId, "pk_accn_id = '" + accnId + "' and  PK_PYR_PRIO = 1", testDb);
		Assert.assertTrue(rowCount > 0, "        ACCN_PYR table should be updated for Accession ID = " + accnId);

		logger.info("*** Step 6 Actions: -Wait for PF EP Match Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_AUTO_MTCH, QUEUE_WAIT_TIME_MS));

		logger.info("*** Expected Results: - Verify that the accession is moved out of Q_EP_AUTO_MTCH, Q_EP_AUTO_MTCH_ERR and ACCN_POTNL_MTCH tables");
		qEpAutoMtchInfoList = daoManagerPlatform.getQEPAutoMtchByConditon("pk_accn_id = '" + accnId + "'", testDb);
		Assert.assertTrue(qEpAutoMtchInfoList.size() == 0, "        copied Accession ID " + accnId + " should be moved out of Q_EP_AUTO_MTCH table.");

		qEpAutoMtchErrInfoList = daoManagerPlatform.getQEPAutoMtchErrByConditon("pk_accn_id = '" + accnId + "' and pk_err_cd = " + errCd, testDb);
		Assert.assertTrue(qEpAutoMtchErrInfoList.size() == 0, "        copied Accession ID " + accnId + " should be moved out of Q_EP_AUTO_MTCH_ERR table.");

		ArrayList<String> qAccnPotnlMtchInfoList = daoManagerPlatform.getAccnPotnlMtchByConditon("PK_ERR_ACCN_ID = '" + accnId + "' and PK_ERR_PYR_PRIO = 1", testDb);
		Assert.assertTrue(qAccnPotnlMtchInfoList.size() == 0, "        copied Accession ID " + accnId + " should be moved out of ACCN_POTNL_MTCH table.");

		logger.info("*** Expected Results: - Verify that the subs id is added in ACCN_PYR table for the accession");
		accnPyrInfoList = daoManagerXifinRpm.getAccnPyrInfoFromACCNPYRByAccnIdPyrIdPyrPrio(mtchAccnId, accnPyrId, "1", testDb);
		String mtchAccnPyrSubsId = accnPyrInfoList.get(4);

		accnPyrInfoList = daoManagerXifinRpm.getAccnPyrInfoFromACCNPYRByAccnIdPyrIdPyrPrio(accnId, unkPyrId, "1", testDb);
		Assert.assertTrue(accnPyrInfoList.get(4).equals(mtchAccnPyrSubsId), "        copied Accession ID " + accnId + " subs id should be updated to " + mtchAccnPyrSubsId);

		logger.info("*** Expected Results: - Verify that the SUBID error is fixed in ACCN_PYR_ERR table for the accession");
		accnPyrErrInfoList = daoManagerXifinRpm.getAccnPyErrFromACCNPYRERRByAccnId(accnId, "1", currDt, errCd, testDb);
		Assert.assertTrue(accnPyrErrInfoList.get(3).equals(currDt), "        copied Accession ID " + accnId + " should have SUBID error fixed in accn_pyr_err table.");

		logger.info("*** Expected Results: - Verify that the accesssion is moved out of EP Auto Match queue and moved to the next queue");
		accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
		Assert.assertFalse(accnQueInfoList.get(3).equals("10"), "        copied Accession ID " + accnId + " should be moved out of the EP Auto Match queue.");

		logger.info("*** Expected Results: - Verify that the accesssion is added to ACCN_MTCHD table");
		ArrayList<String> accnMtchdInfoList = daoManagerPlatform.getAccnMtchdByConditon("PK_ERR_ACCN_ID = '" + accnId + "'", testDb);
		Assert.assertEquals(accnMtchdInfoList.get(2), "1", "       ACCN_MTCHD = 1 for errored Accession ID " + accnId);
		Assert.assertEquals(accnMtchdInfoList.get(3), mtchAccnId, "       FK_MTCH_ACCN_ID = " +  mtchAccnId + " for errored Accession ID " + accnId);
		Assert.assertEquals(accnMtchdInfoList.get(4), "1", "       FK_MTCH_PYR_PRIO = 1 for errored Accession ID " + accnId);
		Assert.assertEquals(accnMtchdInfoList.get(5), "EpAutoMatchEngine", "       FK_USER_ID = EpAutoMatchEngine for errored Accession ID " + accnId);
		Assert.assertEquals(accnMtchdInfoList.get(6), currDt, "       MTCH_DT = " + currDt + " for errored Accession ID " + accnId);

		logger.info("*** Actions: - Clear test data");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyrId, testDb);
		Assert.assertNotEquals(daoManagerAccnWS.deletePyrReqmntsFldByPyrAbrrev(pyrAbbrev, testDb), 0);
		Assert.assertNotEquals(daoManagerAccnWS.deletePyrReqmntsByPyrAbrrev(pyrAbbrev, testDb), 0);
		daoManagerPlatform.setValuesFromTable("ACCN_PYR", "FK_PYR_ID = " + accnPyrId, "pk_accn_id = '" + accnId + "' and  PK_PYR_PRIO = 1", testDb);

	}

	@Test(priority = 1, description = "3rd party pyr accn with SUBID (FIELD ID=4) err, accn status=41(Posting)")
	@Parameters({"project", "testSuite", "testCase"})
	public void testPFER_547(String project, String testSuite, String testCase) throws Exception {
		logger.info("========== Testing - testPFER_547 ==========");

		timeStamp = new TimeStamp(driver);

		logger.info("*** Actions: - Send WS request to create a new Priced 3rd party payor accession and a copy of the accession without SUBID");
		testDataSetup = new TestDataSetup(driver);

		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		logger.info("TestProperties:"+testProperties);
		String accnId = testProperties.getProperty("CopiedAccnId");
		String mtchAccnId = testProperties.getProperty("NewAccnID");
		logger.info("        AccnID: " + accnId);
		logger.info("        MatchedAccnID: " + mtchAccnId);

		logger.info("*** Expected Results: - Verify that a new copied accession was generated");
		Assert.assertTrue(accnId != null, "        Did not generate a new Accession.");

		ArrayList<String> clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
		if(!clnBillingAssignInfoList.isEmpty()){
			daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 2", testDb);
			logger.info("Waiting for accession to complete BA, accnId=" + accnId);
			Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));
		}
		logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Expected Results: - Verify that the copied accession has SUBID accn_pyr_err and is in Unbillable queue (8)");
		List<List<String>> accnInfoPyrList = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
		String accnPyrId = accnInfoPyrList.get(0).get(0);

		ArrayList<String> accnPyrInfoList = daoManagerXifinRpm.getAccnPyrInfoFromACCNPYRByAccnIdPyrIdPyrPrio(accnId, accnPyrId, "1", testDb);
		Assert.assertTrue(accnPyrInfoList.get(4) == "", "        copied Accession ID " + accnId + " should not have a subs id.");

		ArrayList<String> accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
		Assert.assertTrue(accnQueInfoList.get(3).equals("8"), "        copied Accession ID " + accnId + " should be in Q_Typ = 8 (Unbillable).");

		String currDt = timeStamp.getCurrentDate();
		String errCd = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where abbrev = 'SUBID'", testDb).get(0);
		List<String> accnPyrErrInfoList = daoManagerXifinRpm.getAccnPyErrFromACCNPYRERRByAccnId(accnId, "1", currDt, errCd, testDb);
		Assert.assertTrue(accnPyrErrInfoList.size() > 0, "        copied Accession ID " + accnId + " should have SUBID accn_pyr_err.");

		logger.info("*** Actions: - Wait for PF EP Workflow Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Expected Results: - Verify that the accession is moved to EP Auto Match queue (10)");
		accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
		Assert.assertTrue(accnQueInfoList.get(3).equals("10"), "        copied Accession ID " + accnId + " should be in Q_Typ = 10 (EP Auto Match).");

		logger.info("*** Expected Results: - Verify that the accession is added to Q_EP_AUTO_MTCH table");
		ArrayList<String> qEpAutoMtchInfoList = daoManagerPlatform.getQEPAutoMtchByConditon("pk_accn_id = '" + accnId + "'", testDb);
		Assert.assertTrue(qEpAutoMtchInfoList.size() > 0, "        copied Accession ID " + accnId + " should be in Q_EP_AUTO_MTCH table.");

		logger.info("*** Expected Results: - Verify that the accession is added to Q_EP_AUTO_MTCH_ERR table");
		ArrayList<String> qEpAutoMtchErrInfoList = daoManagerPlatform.getQEPAutoMtchErrByConditon("pk_accn_id = '" + accnId + "' and pk_err_cd = " + errCd, testDb);
		Assert.assertTrue(qEpAutoMtchErrInfoList.size() > 0, "        copied Accession ID " + accnId + " should be in Q_EP_AUTO_MTCH_ERR table.");

		String errAccnStatus = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb).get(2);

		logger.info("*** Actions: - Manually change the accession status to 41 (Posting) in ACCN table in DB");
		int rowCount = daoManagerPlatform.setValuesFromACCN("FK_STA_ID = 41", "pk_accn_id = '" + accnId + "'", testDb);
		Assert.assertTrue(rowCount > 0, "        ACCN table should be updated for Accession ID = " + accnId);

		logger.info("*** Actions: - Run PF EP Match Engine");
		Assert.assertTrue(errorProcessingDao.waitForEpMatchEngineToProcessRecord(accnId, QUEUE_WAIT_TIME_MS));

		logger.info("*** Expected Results: - Verify that the accession is NOT moved out of Q_EP_AUTO_MTCH, Q_EP_AUTO_MTCH_ERR and ACCN_POTNL_MTCH tables");
		qEpAutoMtchInfoList = daoManagerPlatform.getQEPAutoMtchByConditon("pk_accn_id = '" + accnId + "'", testDb);
		Assert.assertTrue(qEpAutoMtchInfoList.size() > 0, "        copied Accession ID " + accnId + " should NOT be moved out of Q_EP_AUTO_MTCH table.");

		qEpAutoMtchErrInfoList = daoManagerPlatform.getQEPAutoMtchErrByConditon("pk_accn_id = '" + accnId + "' and pk_err_cd = " + errCd, testDb);
		Assert.assertTrue(qEpAutoMtchErrInfoList.size() > 0, "        copied Accession ID " + accnId + " should NOT be moved out of Q_EP_AUTO_MTCH_ERR table.");

		ArrayList<String>qAccnPotnlMtchInfoList = daoManagerPlatform.getAccnPotnlMtchByConditon("PK_ERR_ACCN_ID = '" + accnId + "' and PK_ERR_PYR_PRIO = 1", testDb);
		Assert.assertTrue(qAccnPotnlMtchInfoList.size() > 0, "        copied Accession ID " + accnId + " should NOT be moved out of ACCN_POTNL_MTCH table.");

		logger.info("*** Expected Results: - Verify that the accession Q_EP_AUTO_MTCH_ERR.MTCH_STATUS = 1");
		Assert.assertTrue(qEpAutoMtchErrInfoList.get(13).equals("1"), "        copied Accession ID " + accnId + " Q_EP_AUTO_MTCH_ERR.MTCH_STATUS = 1.");

		logger.info("*** Expected Results: - Verify that the subs id is NOT added in ACCN_PYR table for the accession");
		accnPyrInfoList = daoManagerXifinRpm.getAccnPyrInfoFromACCNPYRByAccnIdPyrIdPyrPrio(accnId, accnPyrId, "1", testDb);
		Assert.assertTrue(accnPyrInfoList.get(4).equals(""), "        copied Accession ID " + accnId + " subs id should NOT be updated.");

		logger.info("*** Expected Results: - Verify that the SUBID error is NOT fixed in ACCN_PYR_ERR table for the accession");
		accnPyrErrInfoList = daoManagerXifinRpm.getAccnPyErrFromACCNPYRERRByAccnId(accnId, "1", currDt, errCd, testDb);
		Assert.assertTrue(accnPyrErrInfoList.get(3) == null, "        copied Accession ID " + accnId + " should NOT have SUBID error fixed in accn_pyr_err table.");

		logger.info("*** Expected Results: - Verify that the accesssion is NOT moved out of EP Auto Match queue and moved to the next queue");
		accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
		Assert.assertTrue(accnQueInfoList.get(3).equals("10"), "        copied Accession ID " + accnId + " should stays in the EP Auto Match queue.");

		logger.info("*** Actions: - Clear test data");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyrId, testDb);
		Assert.assertNotEquals(daoManagerAccnWS.deletePyrReqmntsFldByPyrAbrrev(pyrAbbrev, testDb), 0);
		Assert.assertNotEquals(daoManagerAccnWS.deletePyrReqmntsByPyrAbrrev(pyrAbbrev, testDb), 0);
		daoManagerPlatform.setValuesFromACCN("FK_STA_ID = " + errAccnStatus, "pk_accn_id = '" + accnId + "'", testDb);

	}

	@Test(priority = 1, description = "Priced 3rd party pyr accn with Denial error")
	@Parameters({"accnId", "errCdId", "mtchedAccnId"})
	public void testPFER_549(String accnId, String errCdId, String mtchedAccnId) throws Exception {
		logger.info("========== Testing - testPFER_549 ==========");

		timeStamp = new TimeStamp(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);

		navigation = new MenuNavigation(driver, config);
		navigation.navigateToAccnDetailPage();

		logger.info("*** Expected Results: - Verify that it's on the Load Accession page");
		loadAccession = new LoadAccession(driver);
		Assert.assertTrue(isElementPresent(loadAccession.accnIdInput(),5), "        Accession ID input field should present.");

		logger.info("*** Actions: - Load an accession in the Accession ID field and tab out");
		loadAccession.setAccnId(accnId);
		accessionDetail = new AccessionDetail(driver, config, wait);
		Assert.assertTrue(isElementPresent(accessionDetail.accnIdText(),10), "Accession ID input field was not found");
		Assert.assertTrue(isElementPresent(accessionDetail.saveBtn(), 5), "        Save button should be clickable.");

		logger.info("*** Expected Results: - Verify that the accn is loaded properly");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded propertly.");

		// QATESTERC20171218152516863 -- errored accn
		// QATESTER20171218152515570 --matched accn

		logger.info("*** Actions: - Go to Accession Errors grid and click Accession Error grid to expend the grid");
		//Click Accession Error grid to expend the grid
		accessionDetail.setAccnErrGrid();

		String reasonCdDescr = daoManagerPlatform.getReasonCodeAbbrevAndShortDescrFromERRCDByErrCDId(errCdId, testDb);

		//Ensure the fixed error is in the "Fixed Accession Errors" grid
		logger.info("*** Expected Results: - Verify that the fixed Denial error should show in the 'Fixed Accession Errors' grid");
		Assert.assertTrue(getColumnValue(accessionDetail.currentAccnErrTable(), reasonCdDescr));
		//Get the row number for fixed error in the Fixed Accn Error grid
		int rowNum = accessionDetail.getRowNumber(accessionDetail.currentAccnErrTable(), reasonCdDescr, "");

		logger.info("*** Actions: - Click the Unfix hyperlink and click 'Show all unfixed errors' radio button");
		//Click the Unfix hyperlink
		accessionDetail.setFixedAccnErrAction(rowNum);
		//click 'Show all unfixed errors' radio button
		accessionDetail.setShowAllUnfixedErr();

		logger.info("*** Expected Results: - Verify that the Denial error should is not shown");
		Assert.assertFalse(accessionDetail.currentAccnErrTable().isDisplayed());

		logger.info("*** Actions: - Click the 'Show current relevant errors only' radio button");

		accessionDetail.setRelevantErrorRadio();
		logger.info("*** Expected Results: - Verify that the Denial error should is in 'Current Accession Errors' grid");
		Assert.assertTrue(getColumnValue(accessionDetail.currentAccnErrTable(), reasonCdDescr));

		logger.info("*** Actions: - Click Save button");
		accessionDetail.clickSave();

		logger.info("*** Expected Results: - Verify that the accession is Saved properly");
		Assert.assertTrue(accessionDetail.isSaveDone(), "        Save was not done.");
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId), "        Accession " + accnId + " should be loaded propertly.");

		logger.info("*** Actions: - Click Reset button");
		accessionDetail.clickReset();
		Thread.sleep(2000);

		logger.info("*** Expected Results: - Verify that the accession is moved to EP Auto Match queue (10)");
		ArrayList<String> accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
		Assert.assertTrue(accnQueInfoList.get(3).equals("10"), "        Errored Accession ID " + accnId + " should be in Q_Typ = 10 (EP Auto Match).");

		logger.info("*** Expected Results: - Verify that the accession is added to Q_EP_AUTO_MTCH table");
		ArrayList<String> qEpAutoMtchInfoList = daoManagerPlatform.getQEPAutoMtchByConditon("pk_accn_id = '" + accnId + "'", testDb);
		Assert.assertTrue(qEpAutoMtchInfoList.size() > 0, "        Errored Accession ID " + accnId + " should be in Q_EP_AUTO_MTCH table.");

		logger.info("*** Expected Results: - Verify that the accession is added to Q_EP_AUTO_MTCH_ERR table");
		ArrayList<String> qEpAutoMtchErrInfoList = daoManagerPlatform.getQEPAutoMtchErrByConditon("pk_accn_id = '" + accnId + "' and pk_err_cd = " + errCdId, testDb);
		Assert.assertTrue(qEpAutoMtchErrInfoList.size() > 0, "        Errored Accession ID " + accnId + " should be in Q_EP_AUTO_MTCH_ERR table.");

		logger.info("*** Actions: - Manually update Q_EP_AUTO_MTCH_ERR.MTCH_STATUS = 1 in DB");
		daoManagerPlatform.setValuesFromTable("Q_EP_AUTO_MTCH_ERR", "MTCH_STATUS = 1", "pk_accn_id = '" + accnId + "'", testDb);

		logger.info("*** Actions: - Manually add a new record in ACCN_POTNL_MTCH in DB");
		List<String> colList = new ArrayList<String>();
		colList.add("PK_ERR_ACCN_ID");
		colList.add("PK_ERR_PYR_PRIO");
		colList.add("PK_RANK");
		colList.add("FK_MTCH_ACCN_ID");
		colList.add("FK_MTCH_PYR_PRIO");

		List<Object> valList = new ArrayList<Object>();
		valList.add(accnId);
		valList.add(1);
		valList.add(1);
		valList.add(mtchedAccnId);
		valList.add(1);

		logger.info("*** Expected Results: - Verify that a new record should be added to ACCN_POTNL_MTCH table");
		int count = daoManagerPlatform.addRecordIntoTable("ACCN_POTNL_MTCH", colList, valList, testDb);
		Assert.assertTrue(count > 0, "        A new record should be added to ACCN_POTNL_MTCH table.");

		logger.info("*** Step 6 Actions: -Wait for PF EP Match Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_AUTO_MTCH, QUEUE_WAIT_TIME_MS));

		logger.info("*** Expected Results: - Verify that the accession is moved out of Q_EP_AUTO_MTCH, Q_EP_AUTO_MTCH_ERR and ACCN_POTNL_MTCH tables");
		qEpAutoMtchInfoList = daoManagerPlatform.getQEPAutoMtchByConditon("pk_accn_id = '" + accnId + "'", testDb);
		Assert.assertTrue(qEpAutoMtchInfoList.size() == 0, "        Errored Accession ID " + accnId + " should be moved out of Q_EP_AUTO_MTCH table.");

		qEpAutoMtchErrInfoList = daoManagerPlatform.getQEPAutoMtchErrByConditon("pk_accn_id = '" + accnId + "' and pk_err_cd = " + errCdId, testDb);
		Assert.assertTrue(qEpAutoMtchErrInfoList.size() == 0, "        Errored Accession ID " + accnId + " should be moved out of Q_EP_AUTO_MTCH_ERR table.");

		ArrayList<String> qAccnPotnlMtchInfoList = daoManagerPlatform.getAccnPotnlMtchByConditon("PK_ERR_ACCN_ID = '" + accnId + "' and PK_ERR_PYR_PRIO = 1", testDb);
		Assert.assertTrue(qAccnPotnlMtchInfoList.size() == 0, "        Errored Accession ID " + accnId + " should be moved out of ACCN_POTNL_MTCH table.");

		logger.info("*** Expected Results: - Verify that the Denail error is fixed in ACCN_PYR_ERR table for the accession");
		String errDt = "12/19/2017";
		String currDt = timeStamp.getCurrentDate();
		List<String> accnPyrErrInfoList = daoManagerXifinRpm.getAccnPyErrFromACCNPYRERRByAccnId(accnId, "1", errDt, errCdId, testDb);
		Assert.assertTrue(accnPyrErrInfoList.get(3).equals(currDt), "        copied Accession ID " + accnId + " should have SUBID error fixed in accn_pyr_err table.");

		logger.info("*** Expected Results: - Verify that the accesssion is moved out of EP Auto Match queue and moved to the next queue");
		accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
		Assert.assertFalse(accnQueInfoList.get(3).equals("10"), "        Errored Accession ID " + accnId + " should be moved out of the EP Auto Match queue.");

	}

}