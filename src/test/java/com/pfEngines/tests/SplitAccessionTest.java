package com.pfEngines.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnTest.AccnTest;
import com.mbasys.mars.ejb.entity.accnTestMsg.AccnTestMsg;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.clnXref.ClnXref;
import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.ejb.entity.pyrDt.PyrDt;
import com.mbasys.mars.ejb.entity.testDt.TestDt;
import com.mbasys.mars.ejb.entity.testProc.TestProc;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.MiscMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.mbasys.mars.utility.cache.CacheMap;
import com.mbasys.mars.validation.ValidateQueueMap;
import com.overall.accession.orderProcessing.AccnTestUpdate;
import com.overall.utils.SplitAccessionUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qaautomation.webservices.accession.utils.AccessionWsUtils;
import com.xifin.utils.ClearCacheUtil;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TestDataSetup;

public class SplitAccessionTest extends SeleniumBaseTest {
	private AccessionWsUtils accessionWsUtils;
	private SplitAccessionUtils splitAccessionUtils;
	AccnTestUpdate accnTestUpdate;
	private static final String NEW_ACCN_ID_KEY = "NewAccnID";
	private static final String CLN_ABBREV_KEY = "ClnAbbrev";
	private static final String TEST_ABBREV_KEY = "TestAbbrev";
	private static final String PAYOR_ABBREV_KEY = "PayorAbbrev";
	private static final String FAC_ABBREV_KEY = "TestPOS";
	private static final int FIRST_RECORD = 0;
	private static final String DATA_VALUE_FALSE = "0";
	private static final String DISP_VALUE_FALSE = "False";
	private static final String DATA_VALUE_TRUE = "1";
	private static final String DISP_VALUE_TRUE = "True";
	 protected static long QUEUE_WAIT_TIME = TimeUnit.MINUTES.toMillis(2);
	
	private String accnId = null;
	private String splitAccnId = null;
	private String testAbbrev = null;
	private String dataValueSS1202 = null;
	private String dispValueSS1202 = null;
	private String dataValueSS76 = null;
	private String dispValueSS76 = null;
	private String dataValueSS1112 = "1";
	private String dispValueSS1112 = "True";
	private String dataValueSS1825 = "1";
	private String dispValueSS1825 = "True";
	
	private String dataValueSS18003 = "0";
	private String dispValueSS18003 = "False";
	
	private String profileTest = null;
	private String singleTest = null;
	private String pyrId = null;
	
	@BeforeMethod(alwaysRun = true)
	@Parameters({"ssoUsername", "ssoPassword"})
	public void beforeMethod(String username, String password) {
		try {
			
			accessionWsUtils = new AccessionWsUtils(config);
			splitAccessionUtils = new SplitAccessionUtils(this, driver, wait, config);
			accnTestUpdate = new AccnTestUpdate(driver, wait);
			
			logIntoSso(username, password);
			
			logger.info("message=Start cache");
			ClearCacheUtil clearCacheUtil = new ClearCacheUtil(config.getProperty(PropertyMap.ORGALIAS), config.getProperty(PropertyMap.XIFINADMINPORTAL_URL));
            clearCacheUtil.clearCache(CacheMap.SYSTEM_SETTINGS);
            logger.info("message=Cleared cache");
            
		} catch (Exception e) {
			Assert.fail("*** Error running beforeMethod ***", e);
		}
	}
	
	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result) {
		try {
			
			ClearCacheUtil clearCacheUtil = new ClearCacheUtil(config.getProperty(PropertyMap.ORGALIAS), config.getProperty(PropertyMap.XIFINADMINPORTAL_URL));
            clearCacheUtil.clearCache(CacheMap.SYSTEM_SETTINGS);
            
			String methodName = result.getMethod().getMethodName();
			
			logger.info("*** Starting revert data ***");
			switch(methodName) {
			case "verifySplitAccnWithTypeCIsWorkCorrectly":
			case "verifySplitAccnWithTypeXDIsWorkCorrectly":
//				splitAccessionUtils.deleteEligHistFromACCNELIGHISTByAccnIdPyrAbbrev(accnId, pyrId);
//				splitAccessionUtils.deleteAccnAndAccnLnkByAccnIdSplitAccnIdAndTestAbbrev(accnId, splitAccnId);
//				updateSystemSetting(SystemSettingMap.SS_USE_DIALISYS_LOGIC, dispValueSS1202, dataValueSS1202);
//				updateSystemSetting(SystemSettingMap.SS_PTESTIMATION_SUBMITTED_BY_ELIGENGINE, dispValueSS18003, dataValueSS18003);
				//updateSystemSetting(SystemSettingMap.SS_DFLT_FAC_NOT_USED, dispValueSS1112, dataValueSS1112);
//				updateSystemSetting(SystemSettingMap.SS_DFLT_FAC_NOT_USED, dispValueSS1825, dataValueSS1825);
//				updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, dispValueSS76, dataValueSS76);
				break;
			case "verifySplitAccnWithTypeXCIsWorkCorrectly":
				splitAccessionUtils.deleteAccnAndAccnLnkByAccnIdSplitAccnIdAndTestAbbrev(accnId, splitAccnId);
				updateSystemSetting(SystemSettingMap.SS_PTESTIMATION_SUBMITTED_BY_ELIGENGINE, dispValueSS18003, dataValueSS18003);
				break;
				
			case "verifySplitAccnWithTypeXFIsWorkCorrectly":
				splitAccessionUtils.deleteAccnAndAccnLnkByAccnIdSplitAccnIdAndTestAbbrev(accnId, splitAccnId);
//				updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, dispValueSS76, dataValueSS76);
				break;
			case "verifySplitAccnWithTypeXDBIsWorkCorrectly":
			case "verifySplitAccnWithTypeXGIsWorkCorrectly":
				splitAccessionUtils.deleteAccnAndAccnLnkByAccnIdSplitAccnIdAndTestAbbrev(accnId, splitAccnId);
//				splitAccessionUtils.deleteTestByTestCodeId(testAbbrev);
				break;
			case "verifySplitAccnWithTypeXMIsWorkCorrectly":
				splitAccessionUtils.deleteAccnAndAccnLnkByAccnIdSplitAccnIdAndTestAbbrev(accnId, splitAccnId);
				break;
			case "verifySplitAccnWithTypeXPIsWorkCorrectly":
//				splitAccessionUtils.deleteAccnAndAccnLnkByAccnIdSplitAccnIdAndTestAbbrev(accnId, splitAccnId);
//				splitAccessionUtils.deleteTestByTestCodeId(singleTest);
				break;
			case "verifySplitAccnWithTypeBRIsWorkCorrectly":
				splitAccessionUtils.deleteAccnAndAccnLnkByAccnIdSplitAccnIdAndTestAbbrev(accnId, splitAccnId);
				splitAccessionUtils.deleteTestByTestCodeId(profileTest);
				splitAccessionUtils.deleteTestByTestCodeId(singleTest);
			default:
				logger.info("*** No need to revert the data ***");
			}
		} catch (Exception e) {
			Assert.fail("*** Error running afterMethod ***", e);
		}
	}
	
	@Test(priority = 1, description = "Verify Split Accession is work correctly with Bill reference Tests to Client type")
	@Parameters({"project1", "testSuite1", "testCase1","project", "testSuite", "testCase"})
	public void verifySplitAccnWithTypeCIsWorkCorrectly(String project1, String testSuite1, String testCase1, String project, String testSuite, String testCase)  throws Exception {
		logger.info("*** Implementation verifySplitAccnWithTypeCIsWorkCorrectly test case ***");
//		splitAccessionUtils.updateOrderingFacId();
//		splitAccessionUtils.logClnDt();
		
		dataValueSS1202 = systemDao.getSystemSetting(SystemSettingMap.SS_USE_DIALISYS_LOGIC).getDataValue();
		dispValueSS1202 = systemDao.getSystemSetting(SystemSettingMap.SS_USE_DIALISYS_LOGIC).getDispValue();
		String dataValueSS102 = systemDao.getSystemSetting(SystemSettingMap.SS_CLN_BILLING_CATEGORY).getDataValue();
		String dispValueSS102 = systemDao.getSystemSetting(SystemSettingMap.SS_CLN_BILLING_CATEGORY).getDispValue();
		
		dataValueSS1112 = systemDao.getSystemSetting(SystemSettingMap.SS_DFLT_FAC_NOT_USED).getDataValue();
		dispValueSS1112 = systemDao.getSystemSetting(SystemSettingMap.SS_DFLT_FAC_NOT_USED).getDispValue();
		
		dataValueSS76 = systemDao.getSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE).getDataValue();
		dispValueSS76 = systemDao.getSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE).getDispValue();
		
		dataValueSS18003 = systemDao.getSystemSetting(SystemSettingMap.SS_PTESTIMATION_SUBMITTED_BY_ELIGENGINE).getDataValue();
		dispValueSS18003 = systemDao.getSystemSetting(SystemSettingMap.SS_PTESTIMATION_SUBMITTED_BY_ELIGENGINE).getDispValue();
		
		dataValueSS1825 = systemDao.getSystemSetting(SystemSettingMap.SS_INTF_USE_FACILITY_CROSS_REFERENCE).getDataValue();
		dispValueSS1825 = systemDao.getSystemSetting(SystemSettingMap.SS_INTF_USE_FACILITY_CROSS_REFERENCE).getDispValue();
		
		String dataValueSS1813 = systemDao.getSystemSetting(SystemSettingMap.SS_INTF_IGNORE_POS_FROM_HL7).getDataValue();
		String dispValueSS1813 = systemDao.getSystemSetting(SystemSettingMap.SS_INTF_IGNORE_POS_FROM_HL7).getDispValue();
		
		String dataValueSS60 = systemDao.getSystemSetting(SystemSettingMap.SS_DEFAULT_FAC_ID).getDataValue();
		String dispValueSS60 = systemDao.getSystemSetting(SystemSettingMap.SS_DEFAULT_FAC_ID).getDispValue();
		
		logger.info("dataValueSS1202 :" + dataValueSS1202);
		logger.info("dispValueSS1202 :" + dispValueSS1202);
		logger.info("dataValueSS102 :" + dataValueSS102);
		logger.info("dispValueSS102 :" + dispValueSS102);
		logger.info("dataValueSS76 :" + dataValueSS76);
		logger.info("dispValueSS76 :" + dispValueSS76);
		logger.info("dataValueSS18003 :" + dataValueSS18003);
		logger.info("dispValueSS18003 :" + dispValueSS18003);
		logger.info("dataValueSS1112 :" + dataValueSS1112);
		logger.info("dispValueSS1112 :" + dispValueSS1112);
		logger.info("dataValueSS1825 :" + dataValueSS1825);
		logger.info("dispValueSS1825 :" + dispValueSS1825);
		logger.info("dataValueSS1813 :" + dataValueSS1813);
		logger.info("dispValueSS1813 :" + dispValueSS1813);
		logger.info("dataValueSS60 :" + dataValueSS60);
		logger.info("dispValueSS60 :" + dispValueSS60);
		
//		updateSystemSetting(SystemSettingMap.SS_USE_DIALISYS_LOGIC, DISP_VALUE_FALSE, DATA_VALUE_FALSE);
//		updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, DISP_VALUE_FALSE, DATA_VALUE_FALSE);
//		updateSystemSetting(SystemSettingMap.SS_PTESTIMATION_SUBMITTED_BY_ELIGENGINE, DISP_VALUE_FALSE, DATA_VALUE_FALSE);
//		updateSystemSetting(SystemSettingMap.SS_DFLT_FAC_NOT_USED, "false", "0");
//		updateSystemSetting(SystemSettingMap.SS_INTF_USE_FACILITY_CROSS_REFERENCE, DISP_VALUE_FALSE, DATA_VALUE_FALSE);
//		updateSystemSetting(SystemSettingMap.SS_INTF_USE_FACILITY_CROSS_REFERENCE, DISP_VALUE_FALSE, DATA_VALUE_FALSE);
		
		logger.info("*** Implementation verifySplitAccnWithTypeCIsWorkCorrectly test case ***");
		
		Properties commonProperties = config.getProperties();
		
		logger.info("Step 1 Actions: Send FileMaintenance Ws request to create a new single test");
//		Properties fileMaintenanceProperties = TestDataSetup.executeWsTestCase(project1, testSuite1, testCase1, config.getProperty(PropertyMap.FMWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), commonProperties);
//		testAbbrev = fileMaintenanceProperties.getProperty("testCodeID");
//		logger.info("testAbbrev :" + testAbbrev);
//		splitAccessionUtils.logAllDataByFacTypIsFive();
//		logger.info("DataByFacTypIsFive :" + datas);
		testAbbrev = "QAA2060MHUUHSX";
		commonProperties.setProperty("testCodeID", testAbbrev);
		//logger.info("facAbbrev :" + facAbbrev);
		splitAccessionUtils.logAllTestByTestAbbrev(testAbbrev);
		//int testID = testDao.getListTestIdHasReferLabAndSingleTest().get(0).getTestId();
		//testAbbrev = testDao.getTestByTestId(testID).getTestAbbrev();
//		List<TestDt> tdts = testDao.getTestDtByTestAbbrv(testAbbrev);
//		try {
//			for (TestDt testDt : tdts) {
//				logger.info("testDt :" + testDt);
//			}
//		} catch (Exception e) {
//			logger.info("IS NULL");
//		}
		
		//commonProperties.setProperty("testCodeID", testAbbrev);
		logger.info("Step 2 Actions: Send Accn WS request to create new accession");
		Properties properties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD),commonProperties);
		accnId = properties.getProperty(NEW_ACCN_ID_KEY);
		Accn primaryAccn = accessionDao.getAccn(accnId);
		Cln cln = clientDao.getClnByClnAbbrev(properties.getProperty(CLN_ABBREV_KEY));
		//Fac refFac = facilityDao.getFacByAbbrv(properties.getProperty(FAC_ABBREV_KEY));
		pyrId = properties.getProperty(PAYOR_ABBREV_KEY);
//		testAbbrev = properties.getProperty(TEST_ABBREV_KEY);

//		List<AccnTest> accnTests = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev);
//		TestProc testproc = testDao.getTestProcByTestIdProcTypId(accnTests.get(FIRST_RECORD).getTestId(), 38);
//		logger.info("testproc :" + testproc);

//		int payorId =  Integer.parseInt(properties.getProperty("payorId"));
//		List<PyrDt> pyrDtlst = payorDao.getPyrDtByPyrId(payorId);
//		
//		logger.info("pyrDtlst :" + pyrDtlst);
		
		logger.info("Step 2 Expected Results: Verify new Accn is created successfully");
		assertNotNull(accnId, "Accession Id is not exist");
		
		logger.info("Step 2 Expected Results: Verify b_cln_bill_ref_lab of Cln is true");
		assertTrue(cln.getIsClnBillRefLab(), "Cln Bill reference Lab is false");
		
		logger.info("Step 2 Expected Results: Verify Fac of AccnTest is Reference Lab");
		//assertEquals(accnTests.get(FIRST_RECORD).getFacId(), refFac.getFacId(), "AccnTest isn't updated reference lab properly");
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		
		logger.info("Accession: " + accessionDao.getAccn(accnId));
		logger.info("Accession Link: " + accessionDao.getAccnLnkByAccnId(accnId));
		
        logger.info("Step 4 Actions: Waiting for accession is out of q_fr_pending");
        accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME);
        splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
        splitAccessionUtils.getErrsWhenCreateAccn(accnId);

        logger.info("Accession: " + accessionDao.getAccn(accnId));
		logger.info("Accession Link: " + accessionDao.getAccnLnkByAccnId(accnId));
		
        logger.info("Step 5 Actions: Waiting for accession is ouf of q_price");
        splitAccessionUtils.waitingAccessionIdProcess(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME);
        splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
        splitAccessionUtils.getErrsWhenCreateAccn(accnId);

        logger.info("Accession: " + accessionDao.getAccn(accnId));
		logger.info("Accession Link: " + accessionDao.getAccnLnkByAccnId(accnId));
        splitAccessionUtils.waitingAccessionIdProcess(accnId, AccnStatusMap.Q_ZBAL, QUEUE_WAIT_TIME);
        splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
        splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		
		splitAccnId = splitAccessionUtils.verifyAccnLnkIsCreatedSuccessful(accnId, MiscMap.ACCN_SPLIT_TYPE_CLN_BILL_REF_SUFFIX);
		splitAccessionUtils.verifyAccnTestMsgIsWorkProperlyByAccnTestAndSplitTyp(accnId, MiscMap.ACCN_SPLIT_TYPE_CLN_BILL_REF_SUFFIX);
		logger.info("Step 6 Actions: Waiting for the lnk accession is out of q_price");
		
		splitAccessionUtils.waitingAccessionIdProcess(splitAccnId, AccnStatusMap.Q_ZBAL, QUEUE_WAIT_TIME);
	    splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(splitAccnId);
		assertTrue(accessionDao.waitForAccnToBeOutOfQueue(splitAccnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME), "Accession link is still in q_price");
		
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		
		logger.info("Step 6 Expected Results: Verify the lnk accession is working properly for cln bill ref");
		splitAccessionUtils.verifySplitAccnIsWorkProperlyForClnBilRef(splitAccnId, testAbbrev);
	}
	
	@Test(priority = 1, description = "Verify Split Accession is work correctly with Combination of Flow & Molecular type")
	@Parameters({"project", "testSuite", "testCase"})
	public void verifySplitAccnWithTypeXCIsWorkCorrectly(String project, String testSuite, String testCase) throws Exception  {
		logger.info("*** Implementation verifySplitAccnWithTypeXCIsWorkCorrectly test case ***");
		
		dataValueSS18003 = systemDao.getSystemSetting(SystemSettingMap.SS_PTESTIMATION_SUBMITTED_BY_ELIGENGINE).getDataValue();
		dispValueSS18003 = systemDao.getSystemSetting(SystemSettingMap.SS_PTESTIMATION_SUBMITTED_BY_ELIGENGINE).getDispValue();
		
		updateSystemSetting(SystemSettingMap.SS_PTESTIMATION_SUBMITTED_BY_ELIGENGINE, DISP_VALUE_FALSE, DATA_VALUE_FALSE);
		
		logger.info("Step 1 Actions: Send Accn Ws request to create new accession");
		Properties properties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		accnId = properties.getProperty(NEW_ACCN_ID_KEY);
		
		logger.info("Step 1 Expected Results: Verify new Accn is created successfully");
		assertNotNull(accnId, "Accession Id is not exist");
		
		logger.info("Step 2 Actions: Update Release_Dt in q_fr_pending");
		splitAccessionUtils.updateReleaseDtInQFrPending(accnId);
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
		
		logger.info("Step 3 Actions: Waiting for accession is out of q_fr_pending");
		assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME), "Accession is still in q_fr_pending");
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
		
		logger.info("Step 4 Actions: Waiting for accession is ouf of q_price");
		accessionWsUtils.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_PRICE);
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
		
		logger.info("Step 5 Actions: Waiting for accession in q_zbal");
		accessionWsUtils.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_ZBAL);
		
		logger.info("Step 4 Expected Results: Verify the primary accession is ZBal and test is splitted properly");
		assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL, "Accession status is not ZBal");
		splitAccnId = splitAccessionUtils.verifyAccnLnkIsCreatedSuccessful(accnId, MiscMap.ACCN_SPLIT_TYPE_FLOW_SUFFIX);
		splitAccessionUtils.verifyAccnTestMsgIsWorkProperlyByAccnTestAndSplitTyp(accnId, MiscMap.ACCN_SPLIT_TYPE_FLOW_SUFFIX);
	}
	

	@Test(priority = 1, description = "Verify Split Accession is work correctly with Direct Billing Split type")
	@Parameters({"project1", "testSuite1", "testCase1", "project2", "testSuite2", "testCase2", "testCodeID"})
	public void verifySplitAccnWithTypeXDBIsWorkCorrectly(String project1, String testSuite1, String testCase1, String project2, String testSuite2, String testCase2, String testCodeID) throws Exception {
		logger.info("*** Implementation verifySplitAccnWithTypeXDBIsWorkCorrectly test case ***");
		
//		splitAccessionUtils.updateClnDtByCln();
//		splitAccessionUtils.logAllTest(testCodeID);
//		splitAccessionUtils.logAllPrc("INTERP");
		
		Properties commonProperties = config.getProperties();
		
		
		logger.info("Step 1 Actions: Send FileMaintenance Ws request to create a new profile test");
//		Properties fileMaintenanceProperties = TestDataSetup.executeWsTestCase(project1, testSuite1, testCase1, config.getProperty(PropertyMap.FMWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), commonProperties);
//		testAbbrev = fileMaintenanceProperties.getProperty("testCodeID");
		commonProperties.setProperty("testCodeID", testCodeID);
//		logger.info("testAbbrev :" + testAbbrev);	
		
		logger.info("Step 2 Actions: Send Accn Ws request to create new accession");
		Properties accnProperties = TestDataSetup.executeWsTestCase(project2, testSuite2, testCase2, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), commonProperties);
		accnId = accnProperties.getProperty(NEW_ACCN_ID_KEY);

		logger.info("Step 1 Expected Results: Verify new Accn is created successfully");
		assertNotNull(accnId, "Accession Id is not exist");

		logger.info("Step 2 Actions: Update Release_Dt in q_fr_pending");
		splitAccessionUtils.updateReleaseDtInQFrPending(accnId);

		logger.info("Step  3 Actions: Waiting for accession is out of q_fr_pending");
		accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME);

		logger.info("Step  4 Actions: Waiting for accession is ouf of q_price");
		accessionWsUtils.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_PRICE);
		
		accessionWsUtils.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_ZBAL);

		logger.info("Accession after pricing process: " + accessionDao.getAccn(accnId));
		logger.info("Accession Link after pricing process: " + accessionDao.getAccnLnkByAccnId(accnId));
		
		logger.info("Step  4 Expected Results: Verify the primary accession is ZBal and test is splitted properly");
		assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL, "Accession status is not ZBal");
		splitAccnId = splitAccessionUtils.verifyAccnLnkIsCreatedSuccessful(accnId, MiscMap.ACCN_SPLIT_TYPE_DIRECT_BILLING_SUFFIX);
		AccnTestMsg accnTestMsg = splitAccessionUtils.verifyAccnTestMsgIsWorkProperlyByAccnTestAndSplitTyp(accnId, MiscMap.ACCN_SPLIT_TYPE_DIRECT_BILLING_SUFFIX);
		splitAccessionUtils.verifyTestMsgIsWorkProperly(accnTestMsg);
		splitAccessionUtils.verifyErrCdIsWorkProperly(splitAccnId);
		splitAccessionUtils.verifyRmkCdIsWorkProperly(splitAccnId);
	}
	
	@Test(priority = 1, description = "Verify Split Accession is work correctly with Non-FDA Tests to Client type")
	@Parameters({ "project", "testSuite", "testCase" })
	public void verifySplitAccnWithTypeXFIsWorkCorrectly(String project, String testSuite, String testCase) throws Exception {
		logger.info("*** Implementation verifySplitAccnWithTypeXFIsWorkCorrectly test case ***");
		
		dataValueSS76= systemDao.getSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE).getDataValue();
		dispValueSS76 = systemDao.getSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE).getDispValue();
	
		updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, DISP_VALUE_FALSE, DATA_VALUE_FALSE);
		
		logger.info("Step 1 Actions: Send Accn WS request to create new accession");
		Properties properties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER),
				config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		accnId = properties.getProperty(NEW_ACCN_ID_KEY);
		
		logger.info("Step 1 Expected Results: Verify new Accn is created successfully");
		assertNotNull(accnId, "Accession Id is not exist");

		logger.info("Step 2 Actions: Update Release_Dt in q_fr_pending");
		splitAccessionUtils.updateReleaseDtInQFrPending(accnId);

		logger.info("Step 3 Actions: Waiting for accession is out of q_fr_pending");
		accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME);
		
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
		
		logger.info("Step 4 Actions: Waiting for accession is ouf of q_price");
		accessionWsUtils.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_PRICE);
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
		
		logger.info("Step 5 Actions: Waiting for accession in q_zbal");
		accessionWsUtils.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_ZBAL);
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
		
		logger.info("Step 5 Expected Results: Verify the primary accession is ZBal and test is splitted properly");
		assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL, "Accession status is not ZBal");
		splitAccnId = splitAccessionUtils.verifyAccnLnkIsCreatedSuccessful(accnId, MiscMap.ACCN_SPLIT_TYPE_NONFDA_SUFFIX);
		splitAccessionUtils.verifyAccnTestMsgIsWorkProperlyByAccnTestAndSplitTyp(accnId, MiscMap.ACCN_SPLIT_TYPE_NONFDA_SUFFIX);
		splitAccessionUtils.verifyAccnLnkIsCreatedCorrectlyWithAccnLnkTyp(accnId, MiscMap.ACCN_LNK_TYP_NONFDA);
	}

	@Test(priority = 1, description = "Verify Split Accession is work correctly with Ad Hoc Bill Client")
	@Parameters({ "project", "testSuite", "testCase" })
	public void verifySplitAccnWithTypeXMIsWorkCorrectly(String project, String testSuite, String testCase) throws Exception {
		logger.info("Step 1 Actions: Send Accn WS request to create new accession");
		String dataValueSS1152 = systemDao.getSystemSetting(SystemSettingMap.SS_USE_DIALISYS_LOGIC).getDataValue();
		String dispValueSS1152 = systemDao.getSystemSetting(SystemSettingMap.SS_USE_DIALISYS_LOGIC).getDispValue();
		
		logger.info("dataValueSS1152: " + dataValueSS1152);
		logger.info("dispValueSS1152: " + dispValueSS1152);
		
		Properties properties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER),
				config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		accnId = properties.getProperty(NEW_ACCN_ID_KEY);

		logger.info("Step 1 Expected Results: Verify new Accn is created successfully");
		assertNotNull(accnId, "Accession Id is not exist");
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);

		logger.info("Step 2 Actions: Update Release_Dt in q_fr_pending");
		accessionWsUtils.updateReleaseDtInQFrPending(accnId);

		logger.info("Step 3 Actions: Waiting for accession is out of q_fr_pending");
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
		
		accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS);
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
		
		splitAccessionUtils.getAccnInQEligByAccnId(accnId);
		
		logger.info("Step 4 Actions: Waiting for accession is ouf of q_price");
		accessionWsUtils.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_PRICE);
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		splitAccessionUtils.getAccnInQEligByAccnId(accnId);
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);

		accessionWsUtils.waitForAccnToBeInTheQueue(accnId, AccnStatusMap.Q_ZBAL);
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
		splitAccessionUtils.getAccnInQEligByAccnId(accnId);
		
		logger.info("Step 4 Expected Results: Verify the primary accession is ZBal and test is splitted properly");
		assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL, "Accession status is not ZBal");
		splitAccnId = splitAccessionUtils.verifyAccnLnkIsCreatedSuccessful(accnId, MiscMap.ACCN_SPLIT_TYPE_MAN_SUFFIX);
		splitAccessionUtils.verifyAccnTestMsgIsWorkProperlyByAccnTestAndSplitTyp(accnId, MiscMap.ACCN_SPLIT_TYPE_MAN_SUFFIX);
		splitAccessionUtils.verifyAccnLnkIsCreatedCorrectlyWithAccnLnkTyp(accnId, MiscMap.ACCN_LNK_TYP_ADHOC);
	}

	@Test(priority = 1, description = "Verify Split Accession is work correctly with Payor/Modifier")
	@Parameters({"project1", "testSuite1", "testCase1", "project2", "testSuite2", "testCase2"})
	public void verifySplitAccnWithTypeXPIsWorkCorrectly(String project1, String testSuite1, String testCase1, String project2, String testSuite2, String testCase2) throws Exception {
		
		logger.info("*** Implementation verifySplitAccnWithTypeXPIsWorkCorrectly test case ***");
		Properties commonProperties = config.getProperties();
		
		splitAccessionUtils.logAllPyrModSplitPyr();
		logger.info("Step 1 Actions: Send FileMaintenance Ws request to create a new single test were created");
		Properties fileMaintenanceProperties = TestDataSetup.executeWsTestCase(project1, testSuite1, testCase1, config.getProperty(PropertyMap.FMWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), commonProperties);
		
		logger.info("Step 1 Expected Results: A new single test");
		singleTest = fileMaintenanceProperties.getProperty("testCodeID");
	
		logger.info("singleTest : " + singleTest);
		commonProperties.setProperty("singleTest", singleTest);
		
		logger.info("Step 1 Actions: Send Accn WS request to create new accession");
	
		Properties properties = TestDataSetup.executeWsTestCase(project2, testSuite2, testCase2, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER),
				config.getProperty(PropertyMap.ACCNWS_PASSWORD), commonProperties);
		accnId = properties.getProperty(NEW_ACCN_ID_KEY);
		Accn accn = accessionDao.getAccn(accnId);
		logger.info("Accession after pricing process: " + accn);
		logger.info("AccnPyr: " + accessionDao.getAccnPyrByAccnIdAndPyrPrio(accnId, 1));
		
		logger.info("ProcCdByProcIdWhereSSIs58: " + procedureCodeDao.getProcCdByProcIdWhereSSIs58("88184"));
		
		logger.info("Step 1 Expected Results: Verify new Accn is created successfully");
		assertNotNull(accnId, "Accession Id is not exist");
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		
		logger.info("Step 2 Actions: Update Release_Dt in q_fr_pending");
		splitAccessionUtils.updateReleaseDtInQFrPending(accnId);

		logger.info("Step 3 Actions: Waiting for accession is out of q_fr_pending");
		accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME);
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		logger.info("Accession after pricing process: " + accessionDao.getAccn(accnId));
		
		logger.info("Step 4 Actions: Waiting for accession is ouf of q_price");
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
		splitAccessionUtils.waitingAccessionIdProcess(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME);
		
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);

		logger.info("Accession after pricing process: " + accessionDao.getAccn(accnId));
		logger.info("Accession Link after pricing process: " + accessionDao.getAccnLnkByAccnId(accnId));
		splitAccessionUtils.waitingAccessionIdProcess(accnId, AccnStatusMap.Q_ZBAL, QUEUE_WAIT_TIME);
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);

		logger.info("Accession after pricing process: " + accessionDao.getAccn(accnId));
		logger.info("Accession Link after pricing process: " + accessionDao.getAccnLnkByAccnId(accnId));
		
		
		logger.info("Step 4 Expected Results: Verify the primary accession is ZBal and test is splitted properly");
		assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL, "Accession status is not ZBal");
		splitAccnId = splitAccessionUtils.verifyAccnLnkIsCreatedSuccessful(accnId, MiscMap.ACCN_SPLIT_TYPE_HMO_SUFFIX);
		splitAccessionUtils.verifyAccnTestMsgIsWorkProperlyByAccnTestAndSplitTyp(accnId, MiscMap.ACCN_SPLIT_TYPE_HMO_SUFFIX);
		splitAccessionUtils.verifyAccnLnkIsCreatedCorrectlyWithAccnLnkTyp(accnId, MiscMap.ACCN_LNK_TYP_CAL_HMO);
	}
	
	@Test(priority = 1, description = "Verify Split Accession is work correctly with Dialysis / ESRD (End Stage Renal Disease)")
	@Parameters({ "project", "testSuite", "testCase" })
	public void verifySplitAccnWithTypeXDIsWorkCorrectly(String project, String testSuite, String testCase) throws Exception {
		logger.info("*** Implementation verifySplitAccnWithTypeXDIsWorkCorrectly test case ***");
		dataValueSS1202 = systemDao.getSystemSetting(SystemSettingMap.SS_USE_DIALISYS_LOGIC).getDataValue();
		dispValueSS1202 = systemDao.getSystemSetting(SystemSettingMap.SS_USE_DIALISYS_LOGIC).getDispValue();
		updateSystemSetting(SystemSettingMap.SS_USE_DIALISYS_LOGIC, DISP_VALUE_TRUE, DATA_VALUE_TRUE);
		
		logger.info("Step 1 Actions: Send Accn WS request to create new accession");
		Properties properties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER),
				config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		accnId = properties.getProperty(NEW_ACCN_ID_KEY);

		logger.info("Step 1 Expected Results: Verify new Accn is created successfully");
		assertNotNull(accnId, "Accession Id is not exist");
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);

		logger.info("Step 2 Actions: Update Release_Dt in q_fr_pending");
		accessionWsUtils.updateReleaseDtInQFrPending(accnId);

		logger.info("Step 3 Actions: Waiting for accession is out of q_fr_pending");
		accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME);
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);

		logger.info("Step 4 Actions: Waiting for accession is ouf of q_price");
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
		accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME);
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		splitAccessionUtils.getAccnQueByAccnIdAndTyp(accnId,AccnStatusMap.Q_PRICE);
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);

		logger.info("Step 4 Expected Results: Verify the primary accession is ZBal and test is splitted properly");
		assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL, "Accession status is not ZBal");
		splitAccnId = splitAccessionUtils.verifyAccnLnkIsCreatedSuccessful(accnId, MiscMap.ACCN_SPLIT_TYPE_ESRD_SUFFIX);
		splitAccessionUtils.verifyAccnTestMsgIsWorkProperlyByAccnTestAndSplitTyp(accnId, MiscMap.ACCN_SPLIT_TYPE_ESRD_SUFFIX);
		splitAccessionUtils.verifyAccnLnkIsCreatedCorrectlyWithAccnLnkTyp(accnId, MiscMap.ACCN_LNK_TYP_ESRD_DIALYSIS);
		
		logger.info("Step 5 Actions: Waiting for the lnk accession is out of q_price");
		assertTrue(accessionDao.waitForAccnToBeOutOfQueue(splitAccnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME), "Accession link is still in q_price");
		logger.info("Step 5 Expected Results: Verify the split accession is ZBal and test is splitted properly");
		assertEquals(accessionDao.getAccn(splitAccnId).getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL, "Accession status is not ZBal");
	}
	
	@Test(priority = 1, description = "Verify Split Accession is work correctly with Technical Component of Physician Pathology Services type")
	@Parameters({"project1", "testSuite1", "testCase1", "project2", "testSuite2", "testCase2"})
	public void verifySplitAccnWithTypeXGIsWorkCorrectly(String project1, String testSuite1, String testCase1, String project2, String testSuite2, String testCase2) throws Exception {

		String dataValueSS1230 = systemDao.getSystemSetting(SystemSettingMap.SS_CLN_ACCNT_TYPS_FLOW_MOLECULAR_SPLIT).getDataValue();
		String dispValueSS1230 = systemDao.getSystemSetting(SystemSettingMap.SS_CLN_ACCNT_TYPS_FLOW_MOLECULAR_SPLIT).getDispValue();
		
		logger.info("dataValueSS1230 :" + dataValueSS1230);
		logger.info("dispValueSS1202 :" + dispValueSS1230);
		
		logger.info("*** Implementation verifySplitAccnWithTypeXGIsWorkCorrectly test case ***");
        Properties commonProperties = config.getProperties();
        
        logger.info("Step 1 Actions: Send FileMaintenance Ws request to create a new single test");
        Properties fileMaintenanceProperties = TestDataSetup.executeWsTestCase(project1, testSuite1, testCase1, config.getProperty(PropertyMap.FMWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), commonProperties);
        testAbbrev = fileMaintenanceProperties.getProperty("testCodeID");
        
        logger.info("Step 1 Expected Results: A new single test was created");
        commonProperties.setProperty("testCodeID", testAbbrev);
        
        logger.info("Step 2 Actions: Send Accn Ws request to create new accession");
        Properties accnProperties = TestDataSetup.executeWsTestCase(project2, testSuite2, testCase2, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), commonProperties);
        accnId = accnProperties.getProperty(NEW_ACCN_ID_KEY);
        
        logger.info("Step 2 Expected Results: Verify new Accn is created successfully");
        assertNotNull(accnId, "Accession Id is not exist");
        splitAccessionUtils.getErrsWhenCreateAccn(accnId);
        
        logger.info("Step 3 Actions: Update Release_Dt in q_fr_pending");
        splitAccessionUtils.updateReleaseDtInQFrPending(accnId);

        logger.info("Accession: " + accessionDao.getAccn(accnId));
		logger.info("Accession Link: " + accessionDao.getAccnLnkByAccnId(accnId));
		
        logger.info("Step 4 Actions: Waiting for accession is out of q_fr_pending");
        accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME);
        splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
        splitAccessionUtils.getErrsWhenCreateAccn(accnId);

        logger.info("Accession: " + accessionDao.getAccn(accnId));
		logger.info("Accession Link: " + accessionDao.getAccnLnkByAccnId(accnId));
		
        logger.info("Step 5 Actions: Waiting for accession is ouf of q_price");
        splitAccessionUtils.waitingAccessionIdProcess(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME);
        splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
        splitAccessionUtils.getErrsWhenCreateAccn(accnId);

        logger.info("Accession: " + accessionDao.getAccn(accnId));
		logger.info("Accession Link: " + accessionDao.getAccnLnkByAccnId(accnId));
        splitAccessionUtils.waitingAccessionIdProcess(accnId, AccnStatusMap.Q_ZBAL, QUEUE_WAIT_TIME);
        splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
        splitAccessionUtils.getErrsWhenCreateAccn(accnId);
        
        logger.info("Accession: " + accessionDao.getAccn(accnId));
		logger.info("Accession Link: " + accessionDao.getAccnLnkByAccnId(accnId));
        logger.info("Step 5 Expected Results: Verify the primary accession is ZBal and test is splitted properly");
        assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL, "Accession status is not ZBal");
        splitAccnId = splitAccessionUtils.verifyAccnLnkIsCreatedSuccessful(accnId, MiscMap.ACCN_SPLIT_TYPE_PATHOLOGY_SUFFIX);
        splitAccessionUtils.verifyAccnTestMsgIsWorkProperlyByAccnTestAndSplitTyp(accnId, MiscMap.ACCN_SPLIT_TYPE_PATHOLOGY_SUFFIX);
        splitAccessionUtils.verifyAccnLnkIsCreatedCorrectlyWithAccnLnkTyp(accnId, MiscMap.ACCN_LNK_TYP_PHYS_PATHOLOGY_SVC);
	}
	
	@Test(priority = 1, description = "Verify Split Accession is work correctly with Technical Component of Physician Pathology Services type")
	@Parameters({"project1", "testSuite1", "testCase1", "project2", "testSuite2", "testCase2"})
	public void verifySplitAccnWithTypeBRIsWorkCorrectly(String project1, String testSuite1, String testCase1, String project2, String testSuite2, String testCase2) throws Exception {
	
		logger.info("*** Implementation verifySplitAccnWithTypeBRIsWorkCorrectly test case ***");
		Properties commonProperties = config.getProperties();
		
		logger.info("Step 1 Actions: Send FileMaintenance Ws request to create a new single test were created");

		Properties fileMaintenanceProperties = TestDataSetup.executeWsTestCase(project1, testSuite1, testCase1, config.getProperty(PropertyMap.FMWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), commonProperties);
		
		logger.info("Step 1 Expected Results:: A new profile test and a new single test");
		profileTest = fileMaintenanceProperties.getProperty("profileTest");
		singleTest = fileMaintenanceProperties.getProperty("singleTest");
		logger.info("profileTest : " + profileTest);
		logger.info("singleTest : " + singleTest);
		
		commonProperties.setProperty("profileTest", profileTest);
		commonProperties.setProperty("singleTest", singleTest);
		
		logger.info("Step 2 Actions: Send Accn Ws request to create new accession");
		Properties accnProperties = TestDataSetup.executeWsTestCase(project2, testSuite2, testCase2, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), commonProperties);
		accnId = accnProperties.getProperty(NEW_ACCN_ID_KEY);
		String clnId = accnProperties.getProperty("clnId");
		List<ClnXref>  clnXreflst = clientDao.getClnXrefByClnId(Integer.parseInt(clnId));
		for (ClnXref clnXref : clnXreflst) {
			logger.info("Test Xref :" + clnXref.getClnId());
			logger.info("Test Xref :" + clnXref.getXrefId());
		}
		splitAccessionUtils.getErrsWhenCreateAccn(accnId);
		
		logger.info("Step 2 Expected Results: Verify new Accn is created successfully");
		assertNotNull(accnId, "Accession Id is not exist");
		
		logger.info("Step 3 Actions: Waiting for accession is out of q_accn_validation");
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
		accessionDao.waitForAccnToBeOutOfValidationQueue(accnId, ValidateQueueMap.CHECK_CLN_BILLING_RULES, QUEUE_WAIT_TIME);
		splitAccessionUtils.getAccnInAccnQueByAccnIdAndOutDtIsNull(accnId);
		
		logger.info("Step 3 Expected Results: Verify the primary accession is splitted properly");
		int suffix = splitAccessionUtils.getNextAvailableSplitAccnSuffixId(accnId, "BR");
		splitAccnId = splitAccessionUtils.verifyAccnLnkIsCreatedSuccessful(accnId, "BR" + Integer.toString(suffix));
		splitAccessionUtils.verifyAccnLnkIsCreatedCorrectlyWithAccnLnkTyp(accnId, MiscMap.ACCN_LNK_TYP_CLN_BR_NY_ARTICLE_28);

	}
}
