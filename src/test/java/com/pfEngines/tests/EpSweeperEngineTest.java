package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnDial.AccnDial;
import com.mbasys.mars.ejb.entity.accnErr.AccnErr;
import com.mbasys.mars.ejb.entity.accnTest.AccnTest;
import com.mbasys.mars.ejb.entity.accnTestErr.AccnTestErr;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.errChk.ErrChk;
import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.ejb.entity.pt.Pt;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.ListUtil;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
import com.xifin.xap.utils.XifinAdminUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class EpSweeperEngineTest extends SeleniumBaseTest  {
	private TimeStamp timeStamp;
	private ListUtil listUtil;

	@BeforeSuite(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "disableBrowserPlugins"})
	public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, @Optional String disableBrowserPlugins) {
		try {
			logger.info("Running BeforeSuite");
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "False", "0");
			updateSystemSetting(SystemSettingMap.SS_PERFORM_JOIN_LOGIC, "False", "0");
			updateSystemSetting(SystemSettingMap.SS_CLIENT_BILLING_RULE_LOGIC_SPLIT, "False", "0");
			updateSystemSetting(SystemSettingMap.SS_PERFORM_PYR_BILLING_RULES, "False", "0");
			updateSystemSetting(SystemSettingMap.SS_DEFAULT_CLN_BILLING_RULES, "False", "0");
			updateSystemSetting(SystemSettingMap.SS_JOIN_ACCNS, "False", "0");
			XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
			xifinAdminUtils.clearDataCache();
		} catch (Exception e) {
			Assert.fail("Error running BeforeSuite", e);
		} finally {
			quitWebDriver();
		}
	}

	@AfterSuite(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "disableBrowserPlugins"})
	public void sfterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, @Optional String disableBrowserPlugins) {
		try {
			logger.info("Running BeforeSuite");
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "True", "1");
			updateSystemSetting(SystemSettingMap.SS_PERFORM_JOIN_LOGIC, "True", "1");
			updateSystemSetting(SystemSettingMap.SS_CLIENT_BILLING_RULE_LOGIC_SPLIT, "True", "1");
			updateSystemSetting(SystemSettingMap.SS_PERFORM_PYR_BILLING_RULES, "True", "1");
			updateSystemSetting(SystemSettingMap.SS_DEFAULT_CLN_BILLING_RULES, "True", "1");
			updateSystemSetting(SystemSettingMap.SS_JOIN_ACCNS, "True", "1");
			XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
			xifinAdminUtils.clearDataCache();
		} catch (Exception e) {
			Assert.fail("Error running BeforeSuite", e);
		} finally {
			quitWebDriver();
		}
	}

	@Test(priority = 1, description = "INVALID CLIENT(Error Code 1128)")
	@Parameters({"project", "testSuite", "testCase","errChkId", "note"})
	public void testPFER_319(String project, String testSuite, String testCase, int errChkId, String note) throws Exception {
    	logger.info("*** Testing - testPFER_319 ***");
    	timeStamp = new TimeStamp(driver);
		
		daoManagerPlatform.setValuesFromTable("ERR_CHK", "fix_cnt = 1, b_fixed = 1", "fix_cnt = 0 and b_fixed = 0", testDb);
	
		logger.info("*** Step 1 Actions: - Send WS request to create a new accession with INVALID CLIENT accn error");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = resultProperties.getProperty("NewAccnID");
		logger.info("AccnID: " + accnId);

    	logger.info("*** Step 1 Expected Results: - Verify that a new Accession was generated");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");

    	logger.info("*** Step 1 Expected Results: - Verify that the new Accession has INVALID CLIENT accn error");
    	List<AccnErr> accnErrList = accessionDao.getAccnErrByAccnIdAndErrCd(accnId, 1128);
    	Assert.assertFalse(accnErrList.isEmpty(), "        Accession " + accnId + " should have INVALID CLIENT accn error generated.");

    	logger.info("*** Step 2 Actions: - Set up data in DB so the accession can be picked up by the EP Sweeper Engine");
		ErrChk errChk=errorDao.getErrChkByErrChkId(errChkId);
		errChk.setFixCnt(0);
		errChk.setIsFixed(false);
		errorDao.setErrChk(errChk);
		AccnErr accnErr=accnErrList.get(0);
		accnErr.setNote(note);
		accessionDao.setAccnErr(accnErr);

		logger.info("*** Step 3 Actions: - Run PF EP Sweeper Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNPRICEABLE, QUEUE_WAIT_TIME_MS), "Accession is not out of Q_EP_UNPRICEABLE Queue");
		Assert.assertTrue(accessionDao.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_EP_MAN,QUEUE_WAIT_TIME_MS), "Accession is not in Q_EP_Man queue");
		Assert.assertTrue(waitForEpSweeperEngineToProcessTheAccn(errChkId,QUEUE_WAIT_TIME_MS), "Accession is not in EP Sweeper Engine queue");

    	logger.info("*** Step 3 Expected Results: - Verify that INVALID CLIENT accn error is not fixed by the EP Sweeper Engine");
    	accnErrList = accessionDao.getAccnErrByAccnIdAndErrCd(accnId, 1128);
    	Assert.assertNull(accnErrList.get(0).getFixDt(), "        INVALID CLIENT on Accession " + accnId + " should not be fixed.");

    	logger.info("*** Step 3 Expected Results: - Verify that Client Id is added to the accession");
		Accn accnInfoList = accessionDao.getAccn(accnId);
		int clnIdInAccn = accnInfoList.getClnId();
		String clientAbbrev = note.substring(19).trim();
		logger.info("        clientAbbrev = " + clientAbbrev);
		Cln clnInfoList = clientDao.getClnByClnAbbrev(clientAbbrev);
		int clnId = clnInfoList.getClnId();
		Assert.assertEquals(clnIdInAccn, clnId, "        Client Abbrev " + clientAbbrev + " should be added to Accession " + accnId);

    	logger.info("*** Step 3 Expected Results: - Verify that the accession is pushed into Q_VALIDATE_ACCN with validate type 1 (validateAccn)");
    	List<String> qValidateAccnInfoList = daoManagerPlatform.getQValidateAccnInfoFromQVALIDATEACCNByAccnId(accnId, testDb);
    	Assert.assertEquals(qValidateAccnInfoList.get(0), "1", "        The validation type for Accession " + accnId + " should be 1 (validateAccn).");
    	String currDt = timeStamp.getCurrentDate();
    	Assert.assertEquals(qValidateAccnInfoList.get(1), currDt, "        The Q_VALIDATE_ACCN.IN_DT for Accession " + accnId + " should be " + currDt);

    	logger.info("*** Step 3 Expected Results: - Verify that the values for fix_cnt and b_fixed in ERR_CHK table get updated");
		ErrChk errChkInfoList =errorDao.getErrChkByErrChkId(errChkId);
		Assert.assertTrue(errChkInfoList.getFixCnt() >= 1, "        ERR_CHK.FIX_CNT should be updated.");
		Assert.assertTrue(errChkInfoList.getIsFixed(), "        ERR_CHK.B_FIXED should be updated to 1.");

	}		
	

	@Test(priority = 1, description = "INVALID TEST CONSTRAINT (Error Code 1164)")
	@Parameters({"project", "testSuite", "testCase", "errChkId", "note"})
	public void testPFER_321(String project, String testSuite, String testCase, int errChkId, String note) throws Exception {
    	logger.info("*** Testing - testPFER_321 ***");
		timeStamp = new TimeStamp(driver);
	
		logger.info("*** Step 1 Actions: - Send WS request to create a new accession with INVALID TEST CONSTRAINT accn test error");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = resultProperties.getProperty("NewAccnID");
		logger.info("AccnID: " + accnId);

    	logger.info("*** Step 1 Expected Results: - Verify that a new Accession was generated");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");

    	logger.info("*** Step 1 Expected Results: - Verify that the new Accession has INVALID TEST CONSTRAINT accn test error");
    	List<AccnTestErr> accnTestErrList = accessionDao.getAccnTestErrByAccnIdErrCd(accnId, 1164);
    	Assert.assertFalse(accnTestErrList.isEmpty(), "        Accession " + accnId + " should have INVALID TEST CONSTRAINT accn test error generated.");

    	logger.info("*** Step 2 Actions: - Set up data in DB so the accession can be picked up by the EP Sweeper Engine");
		ErrChk errChk=errorDao.getErrChkByErrChkId(errChkId);
		errChk.setFixCnt(0);
		errChk.setIsFixed(false);
		errorDao.setErrChk(errChk);
		AccnTestErr accnTestErr=accnTestErrList.get(0);
		accnTestErr.setNote(note);
		accessionDao.setAccnTestErr(accnTestErr);

		logger.info("*** Step 3 Actions: - Run PF EP Sweeper Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNPRICEABLE, QUEUE_WAIT_TIME_MS), "Accession is not out of Q_EP_UNPRICEABLE Queue");
		Assert.assertTrue(accessionDao.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_EP_MAN,QUEUE_WAIT_TIME_MS), "Accession is not in Q_EP_MAN Queue");
		Assert.assertTrue(waitForEpSweeperEngineToProcessTheAccn(errChkId,QUEUE_WAIT_TIME_MS), "Accession is not in EP Sweeper Engine queue");

		logger.info("*** Step 3 Expected Results: - Verify that Test Id is added to the accession");
    	String testAbbrev = note.substring(19).trim();
    	logger.info("        testAbbrev = " + testAbbrev);
    	List<AccnTest> accnTestInfoList = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev);
    	Assert.assertFalse(accnTestInfoList.isEmpty(), "        Test " + testAbbrev + " should be added to Accession " + accnId );

    	logger.info("*** Step 3 Expected Results: - Verify that INVALID TEST CONSTRAINT accn test error is not fixed by the EP Sweeper Engine");
    	accnTestErrList = accessionDao.getAccnTestErrByAccnIdErrCd(accnId, 1164);
    	Assert.assertNull(accnTestErrList.get(0).getFixDt(), "        INVALID TEST CONSTRAINT on Accession " + accnId + " should not be fixed.");

    	logger.info("*** Step 3 Expected Results: - Verify that the accession is pushed into Q_VALIDATE_ACCN with validate type 1 (validateAccn)");
    	List<String> qValidateAccnInfoList = daoManagerPlatform.getQValidateAccnInfoFromQVALIDATEACCNByAccnId(accnId, testDb);
    	Assert.assertEquals(qValidateAccnInfoList.get(0), "1", "        The validation type for Accession " + accnId + " should be 1 (validateAccn).");
    	String currDt = timeStamp.getCurrentDate();
    	Assert.assertEquals(qValidateAccnInfoList.get(1), currDt, "        The Q_VALIDATE_ACCN.IN_DT for Accession " + accnId + " should be " + currDt);

    	logger.info("*** Step 3 Expected Results: - Verify that the values for fix_cnt and b_fixed in ERR_CHK table get updated");
		ErrChk errChkInfoList = errorDao.getErrChkByErrChkId(errChkId);
		Assert.assertTrue(errChkInfoList.getFixCnt() >= 1, "        ERR_CHK.FIX_CNT should be updated.");
		Assert.assertTrue(errChkInfoList.getIsFixed(), "        ERR_CHK.B_FIXED should be updated to 1.");

	}

	@Test(priority = 1, description = "ESRDCLN (Error Code 1229)")
	@Parameters({"project", "testSuite", "testCase", "errChkId", "ptSeqId"})
	public void testPFER_330(String project, String testSuite, String testCase, int errChkId, String ptSeqId) throws Exception {
    	logger.info("*** Testing - testPFER_330 ***");
    	timeStamp = new TimeStamp(driver);
	
		logger.info("*** Step 1 Actions: - Send WS request to create a new accession with ESRDCLN accn error");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = resultProperties.getProperty("NewAccnID");
		logger.info("AccnID: " + accnId);

    	logger.info("*** Step 1 Expected Results: - Verify that a new Accession was generated");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");

    	logger.info("*** Step 1 Expected Results: - Verify that the new Accession has ESRDCLN accn error");
       	List<AccnErr> accnErrList = accessionDao.getAccnErrByAccnIdAndErrCd(accnId, 1229);
    	Assert.assertFalse(accnErrList.isEmpty(), "        Accession " + accnId + " should have ESRDCLN accn error generated.");

    	logger.info("*** Step 2 Actions: - Set up data in DB so the accession can be picked up by the EP Sweeper Engine");
    	ErrChk errChk=errorDao.getErrChkByErrChkId(errChkId);
    	errChk.setFixCnt(0);
    	errChk.setIsFixed(false);
    	errorDao.setErrChk(errChk);
    	Accn accn=accessionDao.getAccn(accnId);
    	accn.setPtSeqId(Integer.parseInt(ptSeqId));
    	accessionDao.setAccn(accn);

		logger.info("*** Step 3 Actions: - Run PF EP Sweeper Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNPRICEABLE, QUEUE_WAIT_TIME_MS), "Accession is not out of Q_EP_UNPRECEABLE Queue");
		Assert.assertTrue(accessionDao.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_EP_MAN,QUEUE_WAIT_TIME_MS), "Accession is not in Q_EP_MAN Queue");
		Assert.assertTrue(waitForEpSweeperEngineToProcessTheAccn(errChkId,QUEUE_WAIT_TIME_MS), "Accession is not in EP Sweeper Engine queue");

		logger.info("*** Step 3 Expected Results: - Verify that ESRDCLN accn error is not fixed by the EP Sweeper Engine");
       	accnErrList = accessionDao.getAccnErrByAccnIdAndErrCd(accnId, 1229);
    	Assert.assertNull(accnErrList.get(0).getFixDt(), "        ESRDCLN on Accession " + accnId + " should not be fixed.");

    	logger.info("*** Step 3 Expected Results: - Verify that the accession is pushed into Q_VALIDATE_ACCN with validate type 1 (validateAccn)");
    	List<String> qValidateAccnInfoList = daoManagerPlatform.getQValidateAccnInfoFromQVALIDATEACCNByAccnId(accnId, testDb);
    	Assert.assertEquals(qValidateAccnInfoList.get(0), "1", "        The validation type for Accession " + accnId + " should be 1 (validateAccn).");
    	String currDt = timeStamp.getCurrentDate();
    	Assert.assertEquals(qValidateAccnInfoList.get(1), currDt, "        The Q_VALIDATE_ACCN.IN_DT for Accession " + accnId + " should be " + currDt);

    	logger.info("*** Step 3 Expected Results: - Verify that the values for fix_cnt and b_fixed in ERR_CHK table get updated");
		ErrChk errChkInfoList = errorDao.getErrChkByErrChkId(errChkId);
		Assert.assertTrue(errChkInfoList.getFixCnt() >= 1, "        ERR_CHK.FIX_CNT should be updated.");
		Assert.assertTrue(errChkInfoList.getIsFixed(), "        ERR_CHK.B_FIXED should be updated to 1.");

    	logger.info("*** Step 3 Expected Results: - Verify that a new record is created in ACCN_DIAL table and FK_MED_TYP_ID and FK_DIAL_TYP_ID are copied from PT");
    	Pt ptInfoList = patientDao.getPtBySeqId(Integer.parseInt(ptSeqId));
    	int dialTypId = ptInfoList.getDialTypId();
    	int medTypId = ptInfoList.getMedTypId();

    	AccnDial accnDialInfoList = accessionDao.getAccnDialByAccnId(accnId);
    	Assert.assertEquals(accnDialInfoList.getMedTypId(), medTypId, "         FK_MED_TYP_ID in ACCN_DAIL for Accession ID " + accnId + " should be " + medTypId);
    	Assert.assertEquals(accnDialInfoList.getDialTypId(), dialTypId, "         FK_DIAL_TYP_ID in ACCN_DAIL for Accession ID " + accnId + " should be " + dialTypId);


	}
	
	@Test(priority = 1, description = "INVALID PROFILE (Error Code 1135)")
	@Parameters({"project", "testSuite", "testCase","errChkId", "note"})
	public void testPFER_331(String project, String testSuite, String testCase, int errChkId, String note) throws Exception {
    	logger.info("*** Testing - testPFER_331 ***");
    	timeStamp = new TimeStamp(driver);
	
		logger.info("*** Step 1 Actions: - Send WS request to create a new accession with INVALID PROFILE accn test error");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = resultProperties.getProperty("NewAccnID");
		logger.info("AccnID: " + accnId);

    	logger.info("*** Step 1 Expected Results: - Verify that a new Accession was generated");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");

    	logger.info("*** Step 1 Expected Results: - Verify that the new Accession has INVALID PROFILE accn test error");
    	List<AccnTestErr> accnTestErrList = accessionDao.getAccnTestErrByAccnIdErrCd(accnId, 1135);
    	Assert.assertFalse(accnTestErrList.isEmpty(), "        Accession " + accnId + " should have INVALID PROFILE accn test error generated.");

    	logger.info("*** Step 2 Actions: - Set up data in DB so the accession can be picked up by the EP Sweeper Engine");
		ErrChk errChk=errorDao.getErrChkByErrChkId(errChkId);
		errChk.setFixCnt(0);
		errChk.setIsFixed(false);
		errorDao.setErrChk(errChk);
		AccnTestErr accnTestErr=accnTestErrList.get(0);
		accnTestErr.setNote(note);
    	accessionDao.setAccnTestErr(accnTestErr);

    	logger.info("*** Step 3 Actions: - Run PF EP Sweeper Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNPRICEABLE, QUEUE_WAIT_TIME_MS), "Accession is not out of Q_EP_UNPRICEABLE Queue");
		Assert.assertTrue(accessionDao.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_EP_MAN,QUEUE_WAIT_TIME_MS), "Accession is not in Q_EP_MAN Queue");
		Assert.assertTrue(waitForEpSweeperEngineToProcessTheAccn(errChkId,QUEUE_WAIT_TIME_MS), "Accession is not in EP Sweeper Engine queue");

		logger.info("*** Step 3 Expected Results: - Verify that profile id is get updated in ACCN_TEST");
       	String profAbbrev = note.substring(22).trim();
    	logger.info("        profAbbrev = " + profAbbrev);
    	int profId = testDao.getTestByTestAbbrev(profAbbrev).getTestId();
    	List<AccnTest> accnTestInfoList = accessionDao.getAccnTestsByAccnId(accnId);
    	int profIdInDB = accnTestInfoList.get(0).getProfId();
    	String testAbbrev = testDao.getTestByTestId(accnTestInfoList.get(0).getTestId()).getTestAbbrev();
    	Assert.assertEquals(profIdInDB, profId, "        FK_PROF_ID in ACCN_TEST for Test " + testAbbrev + " should be updated to " + profId + " for Accession " + accnId );

    	logger.info("*** Step 3 Expected Results: - Verify that INVALID PROFILE accn test error is not fixed by the EP Sweeper Engine");
    	accnTestErrList = accessionDao.getAccnTestErrByAccnIdErrCd(accnId, 1135);
    	Assert.assertNull(accnTestErrList.get(0).getFixDt(), "        INVALID PROFILE on Accession " + accnId + " should not be fixed.");

    	logger.info("*** Step 3 Expected Results: - Verify that the accession is pushed into Q_VALIDATE_ACCN with validate type 1 (validateAccn)");
    	List<String> qValidateAccnInfoList = daoManagerPlatform.getQValidateAccnInfoFromQVALIDATEACCNByAccnId(accnId, testDb);
    	Assert.assertEquals(qValidateAccnInfoList.get(0), "1", "        The validation type for Accession " + accnId + " should be 1 (validateAccn).");
    	String currDt = timeStamp.getCurrentDate();
    	Assert.assertEquals(qValidateAccnInfoList.get(1), currDt, "        The Q_VALIDATE_ACCN.IN_DT for Accession " + accnId + " should be " + currDt);

    	logger.info("*** Step 3 Expected Results: - Verify that the values for fix_cnt and b_fixed in ERR_CHK table get updated");
		ErrChk errChkInfoList = errorDao.getErrChkByErrChkId(errChkId);
		Assert.assertTrue(errChkInfoList.getFixCnt() >= 1, "        ERR_CHK.FIX_CNT should be updated.");
		Assert.assertTrue(errChkInfoList.getIsFixed(), "        ERR_CHK.B_FIXED should be updated to 1.");


	}

	@Test(priority = 1, description = "INVALID FACILITY (Error Code 1140)")
	@Parameters({"project", "testSuite", "testCase", "errChkId", "note"})
	public void testPFER_335(String project, String testSuite, String testCase, int errChkId, String note) throws Exception {
    	logger.info("*** Testing - testPFER_335 ***");
		timeStamp = new TimeStamp(driver);
	
		logger.info("*** Step 1 Actions: - Send WS request to create a new accession with INVALID FACILITY accn test error");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = resultProperties.getProperty("NewAccnID");
		logger.info("AccnID: " + accnId);

    	logger.info("*** Step 1 Expected Results: - Verify that a new Accession was generated");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");

    	logger.info("*** Step 1 Expected Results: - Verify that the new Accession has INVALID FACILITY accn test error");
    	List<AccnTestErr> accnTestErrList = accessionDao.getAccnTestErrByAccnIdErrCd(accnId,1140);
    	Assert.assertFalse(accnTestErrList.isEmpty(), "        Accession " + accnId + " should have INVALID FACILITY accn test error generated.");

    	logger.info("*** Step 2 Actions: - Set up data in DB so the accession can be picked up by the EP Sweeper Engine");
		ErrChk errChk=errorDao.getErrChkByErrChkId(errChkId);
		errChk.setFixCnt(0);
		errChk.setIsFixed(false);
		errorDao.setErrChk(errChk);
		AccnTestErr accnTestErr=accnTestErrList.get(0);
		accnTestErr.setNote(note);
		accessionDao.setAccnTestErr(accnTestErr);

    	logger.info("*** Step 3 Actions: - Run PF EP Sweeper Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNPRICEABLE, QUEUE_WAIT_TIME_MS), "Accession is not out of Q_EP_UNPRICEABLE Queue");
		Assert.assertTrue(accessionDao.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_EP_MAN,QUEUE_WAIT_TIME_MS), "Accession is not in Q_EP_MAN Queue");
		Assert.assertTrue(waitForEpSweeperEngineToProcessTheAccn(errChkId,QUEUE_WAIT_TIME_MS), "Accession is not in EP Sweeper Engine queue");

		logger.info("*** Step 3 Expected Results: - Verify that INVALID FACILITY accn test error is not fixed by the EP Sweeper Engine");
    	accnTestErrList = accessionDao.getAccnTestErrByAccnIdErrCd(accnId,1140);
    	Assert.assertNull(accnTestErrList.get(0).getFixDt(), "        INVALID FACILITY on Accession " + accnId + " should not be fixed.");

    	logger.info("*** Step 3 Expected Results: - Verify that Facility ID is added to the Accession Test");
    	String facAbbrev = note.substring(23).trim();
    	logger.info("        facAbbrev = " + facAbbrev);
    	String testAbbrev = testDao.getTestByTestId(accnTestErrList.get(0).getTestId()).getTestAbbrev();
    	List<AccnTest> accnTestInfoList = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev);
    	Fac facInfoList = facilityDao.getFacFromFacTypByAbbrv(facAbbrev);
    	int facId = facInfoList.getFacId();
    	Assert.assertEquals(accnTestInfoList.get(0).getFacId(), facId, "        Fac Id " + facId + " should be added to Accession " + accnId + " for test " + testAbbrev);

    	logger.info("*** Step 3 Expected Results: - Verify that the accession is pushed into Q_VALIDATE_ACCN with validate type 1 (validateAccn)");
    	List<String> qValidateAccnInfoList = daoManagerPlatform.getQValidateAccnInfoFromQVALIDATEACCNByAccnId(accnId, testDb);
    	Assert.assertEquals(qValidateAccnInfoList.get(0), "1", "        The validation type for Accession " + accnId + " should be 1 (validateAccn).");
    	String currDt = timeStamp.getCurrentDate();
    	Assert.assertEquals(qValidateAccnInfoList.get(1), currDt, "        The Q_VALIDATE_ACCN.IN_DT for Accession " + accnId + " should be " + currDt);

    	logger.info("*** Step 3 Expected Results: - Verify that the values for fix_cnt and b_fixed in ERR_CHK table get updated");
		ErrChk errChkInfoList = errorDao.getErrChkByErrChkId(errChkId);
		Assert.assertTrue(errChkInfoList.getFixCnt() >= 1, "        ERR_CHK.FIX_CNT should be updated.");
		Assert.assertTrue(errChkInfoList.getIsFixed(), "        ERR_CHK.B_FIXED should be updated to 1.");

	}

	@Test(priority = 1, description = "Process payment posting status (41) Accessions")
	@Parameters({"project", "testSuite", "testCase", "errChkId", "note"})
	public void testPFER_336(String project, String testSuite, String testCase, int errChkId, String note) throws Exception {
    	logger.info("*** Testing - testPFER_336 ***");
		timeStamp = new TimeStamp(driver);
	
		logger.info("*** Step 1 Actions: - Send WS request to create a new accession with INVALID FACILITY accn test error");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = resultProperties.getProperty("NewAccnID");
		logger.info("AccnID: " + accnId);

    	logger.info("*** Step 1 Expected Results: - Verify that a new Accession was generated");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");

		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");

    	logger.info("*** Step 1 Expected Results: - Verify that the new Accession has INVALID FACILITY accn test error");
    	List<AccnTestErr> accnTestErrList = accessionDao.getAccnTestErrByAccnIdErrCd(accnId, 1140);
    	Assert.assertFalse(accnTestErrList.isEmpty(), "        Accession " + accnId + " should have INVALID FACILITY accn test error generated.");

    	logger.info("*** Step 2 Actions: - Set up data in DB so the accession can be picked up by the EP Sweeper Engine");
		ErrChk errChk=errorDao.getErrChkByErrChkId(errChkId);
		errChk.setFixCnt(0);
		errChk.setIsFixed(false);
		errorDao.setErrChk(errChk);
		AccnTestErr accnTestErr=accnTestErrList.get(0);
		accnTestErr.setNote(note);
		accessionDao.setAccnTestErr(accnTestErr);

       	logger.info("*** Step 3 Actions: - Set accn.fk_sta_id to 41 (Payment Posting) in DB");
		Accn accn = accessionDao.getAccn(accnId);
		accn.setStaId(41);
		accessionDao.setAccn(accn);

    	logger.info("*** Step 4 Actions: - Run PF EP Sweeper Engine");
		accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNPRICEABLE, QUEUE_WAIT_TIME_MS);
		waitForEpSweeperEngineToProcessTheAccn(errChkId, QUEUE_WAIT_TIME_MS);

		logger.info("*** Step 4 Expected Results: - Verify that INVALID FACILITY accn test error is not fixed by the EP Sweeper Engine");
    	accnTestErrList = accessionDao.getAccnTestErrByAccnIdErrCd(accnId, 1140);
    	Assert.assertNull(accnTestErrList.get(0).getFixDt(), "        INVALID FACILITY on Accession " + accnId + " should not be fixed.");

    	logger.info("*** Step 4 Expected Results: - Verify that Facility ID is added to the Accession Test");
    	String facAbbrev = note.substring(23).trim();
    	logger.info("        facAbbrev = " + facAbbrev);
		String testAbbrev = testDao.getTestByTestId(accnTestErrList.get(0).getTestId()).getTestAbbrev();
		List<AccnTest> accnTestInfoList = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev);
		Fac facInfoList = facilityDao.getFacFromFacTypByAbbrv(facAbbrev);
		int facId = facInfoList.getFacId();
		Assert.assertEquals(accnTestInfoList.get(0).getFacId(), facId, "        Fac Id " + facId + " should be added to Accession " + accnId + " for test " + testAbbrev);

    	logger.info("*** Step 4 Expected Results: - Verify that the accession is pushed into Q_VALIDATE_ACCN with validate type 1 (validateAccn)");
    	List<String> qValidateAccnInfoList = daoManagerPlatform.getQValidateAccnInfoFromQVALIDATEACCNByAccnId(accnId, testDb);
    	Assert.assertEquals(qValidateAccnInfoList.get(0), "1", "        The validation type for Accession " + accnId + " should be 1 (validateAccn).");
    	String currDt = timeStamp.getCurrentDate();
    	Assert.assertEquals(qValidateAccnInfoList.get(1), currDt, "        The Q_VALIDATE_ACCN.IN_DT for Accession " + accnId + " should be " + currDt);

    	logger.info("*** Step 4 Expected Results: - Verify that the values for fix_cnt and b_fixed in ERR_CHK table get updated");
		ErrChk errChkInfoList = errorDao.getErrChkByErrChkId(errChkId);
		Assert.assertTrue(errChkInfoList.getFixCnt() >= 1, "        ERR_CHK.FIX_CNT should be updated.");
		Assert.assertTrue(errChkInfoList.getIsFixed(), "        ERR_CHK.B_FIXED should33 be updated to 1.");

       	logger.info("*** Step 5 Actions: - Clear/reset the test data in DB");
		accn.setStaId(11);
		accessionDao.setAccn(accn);

	}

	@Test(priority = 1, description = "Skip Zbal (51) Accessions")
	@Parameters({"project", "testSuite", "testCase","errChkId", "note"})
	public void testPFER_337(String project, String testSuite, String testCase, int errChkId, String note) throws Exception {
    	logger.info("*** Testing - testPFER_337 ***");
		timeStamp = new TimeStamp(driver);
	
		logger.info("*** Step 1 Actions: - Send WS request to create a new accession with INVALID FACILITY accn test error");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = resultProperties.getProperty("NewAccnID");
		logger.info("AccnID: " + accnId);

    	logger.info("*** Step 1 Expected Results: - Verify that a new Accession was generated");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");

    	logger.info("*** Step 1 Expected Results: - Verify that the new Accession has INVALID FACILITY accn test error");
    	List<AccnTestErr> accnTestErrList = accessionDao.getAccnTestErrByAccnIdErrCd(accnId, 1140);
    	Assert.assertFalse(accnTestErrList.isEmpty(), "        Accession " + accnId + " should have INVALID FACILITY accn test error generated.");

    	logger.info("*** Step 2 Actions: - Set up data in DB so the accession can be picked up by the EP Sweeper Engine");
		ErrChk errChk=errorDao.getErrChkByErrChkId(errChkId);
		errChk.setFixCnt(0);
		errChk.setIsFixed(false);
		errorDao.setErrChk(errChk);
		AccnTestErr accnTestErr=accnTestErrList.get(0);
		accnTestErr.setNote(note);
		accessionDao.setAccnTestErr(accnTestErr);

    	logger.info("*** Step 3 Actions: - Set accn.fk_sta_id to 51 (ZBal) in DB");
    	Accn accn=accessionDao.getAccn(accnId);
    	accn.setStaId(51);
    	accessionDao.setAccn(accn);

    	logger.info("*** Step 4 Actions: - Run PF EP Sweeper Engine");
		accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNPRICEABLE, QUEUE_WAIT_TIME_MS);

		logger.info("*** Step 4 Expected Results: - Verify that INVALID FACILITY accn test error is not fixed by the EP Sweeper Engine");
    	accnTestErrList = accessionDao.getAccnTestErrByAccnIdErrCd(accnId, 1140);
    	Assert.assertNull(accnTestErrList.get(0).getFixDt(), "        INVALID FACILITY on Accession " + accnId + " should not be fixed.");

    	logger.info("*** Step 4 Expected Results: - Verify that Facility ID is NOT added to the Accession Test");
    	String facAbbrev = note.substring(23).trim();
    	logger.info("        facAbbrev = " + facAbbrev);
		String testAbbrev = testDao.getTestByTestId(accnTestErrList.get(0).getTestId()).getTestAbbrev();
		List<AccnTest> accnTestInfoList = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev);
		Assert.assertEquals(accnTestInfoList.get(0).getFacId(), 0, "        Fac Id in Accession " + accnId + " for test " + testAbbrev + " should not be updated.");

    	logger.info("*** Step 4 Expected Results: - Verify that the values for fix_cnt and b_fixed in ERR_CHK table are NOT get updated");
		ErrChk errChkInfoList =errorDao.getErrChkByErrChkId(errChkId);
		Assert.assertTrue(errChkInfoList.getFixCnt() >= 1, "        ERR_CHK.FIX_CNT should be updated.");
		Assert.assertTrue(errChkInfoList.getIsFixed(), "        ERR_CHK.B_FIXED should be updated to 1.");

    	logger.info("*** Step 5 Actions: - Clear/reset the test data in DB");
    	accn.setStaId(11);
    	accessionDao.setAccn(accn);

	}

	@Test(priority = 1, description = "PYRGRPEXCEL (Error Code 1277)")
	@Parameters({"project", "testSuite", "testCase", "errChkId"})
	public void testPFER_338(String project, String testSuite, String testCase, int errChkId) throws Exception {
    	logger.info("*** Testing - testPFER_338 ***");
    	timeStamp = new TimeStamp(driver);
	
		logger.info("*** Step 1 Actions: - Send WS request to create a new accession with PYRGRPEXCEL accn error");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = resultProperties.getProperty("NewAccnID");
		logger.info("AccnID: " + accnId);

    	logger.info("*** Step 1 Expected Results: - Verify that a new Accession was generated");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");

    	logger.info("*** Step 1 Expected Results: - Verify that the new Accession has PYRGRPEXCEL accn error");
       	List<AccnErr> accnErrList = accessionDao.getAccnErrByAccnIdAndErrCd(accnId, 1277);
    	Assert.assertFalse(accnErrList.isEmpty(), "        Accession " + accnId + " should have PYRGRPEXCEL accn error generated.");

    	logger.info("*** Step 2 Actions: - Set up data in DB so the accession can be picked up by the EP Sweeper Engine");
		ErrChk errChk=errorDao.getErrChkByErrChkId(errChkId);
		errChk.setFixCnt(0);
		errChk.setIsFixed(false);
		errorDao.setErrChk(errChk);

		logger.info("*** Step 3 Actions: - Run PF EP Sweeper Engine");
		accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNPRICEABLE, QUEUE_POLL_TIME_MS);

		logger.info("*** Step 3 Expected Results: - Verify that PYRGRPEXCEL accn error is not fixed by the EP Sweeper Engine");
       	accnErrList = accessionDao.getAccnErrByAccnIdAndErrCd(accnId, 1277);
    	Assert.assertNull(accnErrList.get(0).getFixDt(), "        PYRGRPEXCEL on Accession " + accnId + " should not be fixed.");

    	logger.info("*** Step 3 Expected Results: - Verify that the accession is pushed into Q_VALIDATE_ACCN with validate type 1 (validateAccn)");
    	List<String> qValidateAccnInfoList = daoManagerPlatform.getQValidateAccnInfoFromQVALIDATEACCNByAccnId(accnId, testDb);
    	Assert.assertEquals(qValidateAccnInfoList.get(0), "1", "        The validation type for Accession " + accnId + " should be 1 (validateAccn).");
    	String currDt = timeStamp.getCurrentDate();
    	Assert.assertEquals(qValidateAccnInfoList.get(1), currDt, "        The Q_VALIDATE_ACCN.IN_DT for Accession " + accnId + " should be " + currDt);

    	logger.info("*** Step 3 Expected Results: - Verify that the values for fix_cnt and b_fixed in ERR_CHK table get updated");
    	ErrChk errChkInfoList = errorDao.getErrChkByErrChkId(errChkId);
    	Assert.assertTrue(errChkInfoList.getFixCnt() >= 1, "        ERR_CHK.FIX_CNT should be updated.");
		Assert.assertTrue(errChkInfoList.getIsFixed(), "        ERR_CHK.B_FIXED should be updated to 1.");

	}
	
	@Test(priority = 1, description = "INVALID TEST CONSTRAINT (Error Code 1164)-Profile")
	@Parameters({"project", "testSuite", "testCase","errChkId", "note"})
	public void testPFER_355 (String project, String testSuite, String testCase, int errChkId, String note) throws Exception {
    	logger.info("*** Testing - testPFER_355 ***");
    	timeStamp = new TimeStamp(driver);
		listUtil = new ListUtil();

		daoManagerPlatform.setValuesFromTable("ERR_CHK", "fix_cnt = 1, b_fixed = 1", "fix_cnt = 0 and b_fixed = 0", testDb);
	
		logger.info("*** Step 1 Actions: - Send WS request to create a new accession with INVALID TEST CONSTRAINT accn test error");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = resultProperties.getProperty("NewAccnID");
		logger.info("AccnID: " + accnId);

    	logger.info("*** Step 1 Expected Results: - Verify that a new Accession was generated");
		Assert.assertNotNull(accnId, "        Did not generate a new Accession.");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");

    	logger.info("*** Step 1 Expected Results: - Verify that the new Accession has INVALID TEST CONSTRAINT accn test error");
    	List<AccnTestErr> accnTestErrList = accessionDao.getAccnTestErrByAccnIdErrCd(accnId, 1164);
    	Assert.assertFalse(accnTestErrList.isEmpty(), "        Accession " + accnId + " should have INVALID TEST CONSTRAINT accn test error generated.");

    	logger.info("*** Step 2 Actions: - Set up data in DB so the accession can be picked up by the EP Sweeper Engine");
		ErrChk errChk=errorDao.getErrChkByErrChkId(errChkId);
		errChk.setFixCnt(0);
		errChk.setIsFixed(false);
		errorDao.setErrChk(errChk);
		AccnTestErr accnTestErr=accnTestErrList.get(0);
		accnTestErr.setNote(note);
		accessionDao.setAccnTestErr(accnTestErr);

		logger.info("*** Step 3 Actions: - Run PF EP Sweeper Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNPRICEABLE, QUEUE_WAIT_TIME_MS), "Accession is not out of Q_EP_UNPRICEABLE Queue");
		Assert.assertTrue(accessionDao.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_EP_MAN,QUEUE_WAIT_TIME_MS), "Accession is not in Q_EP_MAN Queue");
		Assert.assertTrue(waitForEpSweeperEngineToProcessTheAccn(errChkId,QUEUE_WAIT_TIME_MS), "Accession is not in EP Sweeper Engine queue");

		logger.info("*** Step 3 Expected Results: - Verify that Test Id is added to the accession");
    	String testAbbrev = note.substring(19).trim();
    	logger.info("        testAbbrev = " + testAbbrev);
    	List<AccnTest> accnTestInfoList = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev);
    	Assert.assertFalse(accnTestInfoList.isEmpty(), "        Test " + testAbbrev + " should be added to Accession " + accnId );

    	logger.info("*** Step 3 Expected Results: - Verify that Test Profile components are added properly");
    	ArrayList<ArrayList<String>> accnTestList = daoManagerPlatform.getAccnProfInfoFromACCNTESTByAccnIdProfTestAbbrev(accnId, testAbbrev, testDb);
    	ArrayList<ArrayList<String>> testProfList = daoManagerPlatform.getTestProfInfoFromTESTPROFByTestAbbrev(testAbbrev, testDb);
    	Assert.assertEquals(accnTestList.size(), testProfList.size());

    	List<String> list1 = new ArrayList<>();
    	List<String> list2 = new ArrayList<>();
    	for (int i=0; i<accnTestList.size(); i++){
    		list1.add(accnTestList.get(i).get(2));
    		list2.add(testProfList.get(i).get(2));
    	}
    	Assert.assertTrue(listUtil.compareLists(list1, list2), "        Test Profile " + testAbbrev + " was not added to Accession " + accnId);

    	logger.info("*** Step 3 Expected Results: - Verify that INVALID TEST CONSTRAINT accn test error is not fixed by the EP Sweeper Engine");
    	accnTestErrList = accessionDao.getAccnTestErrByAccnIdErrCd(accnId, 1164);
    	Assert.assertNull(accnTestErrList.get(0).getFixDt(), "        INVALID TEST CONSTRAINT on Accession " + accnId + " should not be fixed.");

    	logger.info("*** Step 3 Expected Results: - Verify that the accession is pushed into Q_VALIDATE_ACCN with validate type 1 (validateAccn)");
    	List<String> qValidateAccnInfoList = daoManagerPlatform.getQValidateAccnInfoFromQVALIDATEACCNByAccnId(accnId, testDb);
    	Assert.assertEquals(qValidateAccnInfoList.get(0), "1", "        The validation type for Accession " + accnId + " should be 1 (validateAccn).");
    	String currDt = timeStamp.getCurrentDate();
    	Assert.assertEquals(qValidateAccnInfoList.get(1), currDt, "        The Q_VALIDATE_ACCN.IN_DT for Accession " + accnId + " should be " + currDt);

    	logger.info("*** Step 3 Expected Results: - Verify that the values for fix_cnt and b_fixed in ERR_CHK table get updated");
		ErrChk errChkInfoList = errorDao.getErrChkByErrChkId(errChkId);
		Assert.assertTrue(errChkInfoList.getFixCnt() >= 1, "        ERR_CHK.FIX_CNT should be updated.");
		Assert.assertTrue(errChkInfoList.getIsFixed(), "        ERR_CHK.B_FIXED should be updated to 1.");
	}
	private Boolean waitForEpSweeperEngineToProcessTheAccn(int errChkId, Long maxTime) throws XifinDataAccessException, InterruptedException {
		long startTime = System.currentTimeMillis();
		maxTime += startTime;
		boolean isInQueue = errorDao.getErrChkByErrChkId(errChkId).getIsFixed();
		while (!isInQueue && System.currentTimeMillis() < maxTime) {
			logger.info("Waiting for Ep Sweeper  engine, elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
			Thread.sleep(QUEUE_POLL_TIME_MS);
			isInQueue = errorDao.getErrChkByErrChkId(errChkId).getIsFixed();
		}
		return isInQueue;

	}

}
