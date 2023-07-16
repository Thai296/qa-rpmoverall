package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.accnQue.AccnQue;
import com.mbasys.mars.ejb.entity.agncy.Agncy;
import com.mbasys.mars.ejb.entity.errCd.ErrCd;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.qClnBillingAssignment.QClnBillingAssignment;
import com.mbasys.mars.ejb.entity.qValidateAccn.QValidateAccn;
import com.mbasys.mars.ejb.entity.qValidatePecos.QValidatePecos;
import com.mbasys.mars.ejb.entity.systemSetting.SystemSetting;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.DatabaseMap;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.mbasys.mars.validation.ValidateQueueMap;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.qa.dao.rpm.ErrorDao;
import com.xifin.qa.dao.rpm.ErrorDaoImpl;
import com.xifin.qa.dao.rpm.domain.AccnAgncy;
import com.xifin.qa.dao.rpm.domain.QEp;
import com.xifin.qa.dao.rpm.domain.QEpCorrespErr;
import com.xifin.qa.dao.rpm.domain.QEpErr;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.FileManipulation;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
import com.xifin.xap.utils.XifinAdminUtils;
import domain.engines.epoutsideagencyengine.CodeRyteRecord;
import domain.engines.epoutsideagencyengine.TevixRecord;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;


public class EPOutsideAgencyEngineTest extends SeleniumBaseTest  {
	private TestDataSetup testDataSetup;
	private TimeStamp timeStamp;

	@BeforeSuite(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "xapEnv", "engConfigDB", "disableBrowserPlugins"})
	public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, String xapEnv, String engConfigDB, @Optional String disableBrowserPlugins)
	{
		try
		{
			logger.info("Running BeforeSuite");
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			setUpTestCondition();
			updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "False", "0");
            logIntoSso(ssoUsername, ssoPassword);
			//Clear Cache
			XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
			xifinAdminUtils.clearDataCache();
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

	@AfterSuite(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "xapEnv", "eType1", "eType2", "engConfigDB", "disableBrowserPlugins"})
	public void afterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, String xapEnv, String eType1, String eType2, String engConfigDB, @Optional String disableBrowserPlugins)
	{
		try
		{
			logger.info("Running AfterSuite");
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			logIntoSso(ssoUsername, ssoPassword);
			updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "True", "1");
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

	@BeforeMethod(alwaysRun = true)
	@Parameters({"ssoUsername", "ssoPassword"})
	public void beforeMethod(String ssoUsername, String ssoPassword, Method method) {
		try {
			logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
			logIntoSso(ssoUsername, ssoPassword);
		} catch (Exception e) {
			logger.error("Error running BeforeMethod", e);
		}
	}

	@Test(priority = 1, description = "FR-ADD(TevixMD)-Verify file can be generated")
	@Parameters({ "project", "testSuite", "testCase"})
	public void testPFER_582(String project, String testSuite, String testCase) throws Exception {
    	logger.info("========== Testing - testPFER_582 ==========");

		timeStamp = new TimeStamp(driver);

		logger.info("*** Actions: - Set up AGNCY HOLD_DAYS");
		Agncy agncy = agencyDao.getAgncyByAbbrev("FR-ADD");
		agncy.setHoldDays(0); //Hold the accn for 0 days
		agencyDao.setAgncy(agncy);

		logger.info("*** Actions: - Send WS request to create a Final Reported 3rd Party Accession with PTZIP error");
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("        AccnID: " + accnId);

    	logger.info("*** Expected Results: - Verify that a new accession was generated");
        assertNotNull(accnId, "        A new Accession was generated.");

		logger.info("*** Actions: - Add Pt State and Pt City manually in DB");
		Accn accn = accessionDao.getAccn(accnId);
		accn.setPtStId("CA");
		accn.setPtCity("SAN DIEGO");
		accessionDao.setAccn(accn);

        QClnBillingAssignment clnBillingRecord = errorProcessingDao.getQClnBillingAssignmentByAccnId(accnId);
        if (clnBillingRecord != null) {
            clnBillingRecord.setNewBillingAssignTypId(2);
            errorProcessingDao.setQClnBillingAssignment(clnBillingRecord);
            logger.info("Waiting for accession to complete BA, accnId=" + accnId);
            Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));
        }
		logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Actions: - Wait for PF EP Workflow Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Expected Results: - Verify that the accession has PTZIP accn_pyr_err");
		String currDtStr = timeStamp.getCurrentDate();
        AccnPyrErr accnPyrErrInfoList = errorProcessingDao.getAccnPyrErrByAccnIdErrCdPyrPrioErrDt(accnId, 2069, 1, currDtStr); //2069: PTZIP ErrCd
        Assert.assertNotNull(accnPyrErrInfoList, "        Accession ID " + accnId + " should have PTZIP accn_pyr_err.");

        logger.info("*** Actions: - Wait for PF EP Outside Agency Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_OUT_AGNCY, QUEUE_WAIT_TIME_MS));

        logger.info("*** Expected Results: - Verify that the accession is out of Q_EP_OUT_AGNCY queue");
        QEp qEp = errorProcessingDao.getQEpOutAgncyByAccnId(accnId);
        assertNull(qEp);

        logger.info("*** Expected Results: - Verify that the accession is out of Q_EP_OUT_AGNCY_ERR table");
        QEpErr qEpErr = errorProcessingDao.getQEpOutAgncyErrByAccnId(accnId);
        assertNull(qEpErr);

        logger.info("*** Expected Results: - Verify that the accession is in Q_EP_HLD queue");
        qEp = errorProcessingDao.getQEpHldByAccnId(accnId);
        assertNotNull(qEp);

        logger.info("*** Expected Results: - Verify that the accession is in Q_EP_HLD_ERR table");
        qEpErr = errorProcessingDao.getQEpHldErrByAccnId(accnId);
        assertNotNull(qEpErr);

        logger.info("*** Expected Results: - Verify that the accession is added into ACCN_AGNCY table");
        AccnAgncy accnAgncy = accessionDao.getAccnAgncyByAccnId(accnId);
        assertNotNull(accnAgncy);

        logger.info("*** Expected Results: - Verify that ACCN_QUE is updated properly");
        AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
        assertEquals(accnQue.getQTyp(), 15); //15: EP Hold

		logger.info("*** Expected Results: - Verify that the file is generated under the folder");
		FileManipulation fileManipulation = new FileManipulation(driver);
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "epAgency");
		String filePathForFrAdd = dirBase + "frontrunner" + File.separator;
		String partialFileName = "FR-ADD_" + timeStamp.getCurrentDate("yyyyMMdd");
        String fullFileName = agencyDao.getAgncyFileByPartialFileName(partialFileName).get(0).getFilename();

		logger.info("*** Expected Results: - Verify that the values are correctly generated in the file");
		//Expected file contents
		TevixRecord expectedTevixRecord = setTevixRecord(accnId);

		//Actual file contents
		List<String> outputFileList = FileUtils.readLines(new File(filePathForFrAdd + fullFileName), "UTF-8");
		TevixRecord actualTevixRecord = new TevixRecord();
		for (String line:outputFileList){
			if (line.contains(accnId)){
				System.out.println("line=" + line);
				actualTevixRecord = new TevixRecord(line);
				break;
			}
		}
		logger.info("       File contents: " + actualTevixRecord.toString());

		assertEquals(actualTevixRecord, expectedTevixRecord);

	}

	@Test(priority = 1, description = "FR-INS-Verify file can be generated")
	@Parameters({"project", "testSuite", "testCase"})
	public void testPFER_584(String project, String testSuite, String testCase) throws Exception {
		logger.info("========== Testing - testPFER_584 ==========");
		timeStamp = new TimeStamp(driver);

		logger.info("*** Actions: - Send WS request to create a Final Reported 3rd Party Accession with SUBID error");
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("        AccnID: " + accnId);

		logger.info("*** Expected Results: - Verify that a new accession was generated");
        assertNotNull(accnId, "        A new Accession was generated.");

		logger.info("*** Actions: - Add Pt State and Pt City manually in DB");
		Accn accn = accessionDao.getAccn(accnId);
		accn.setPtStId("CA");
		accn.setPtCity("SAN DIEGO");
		accessionDao.setAccn(accn);

        QClnBillingAssignment clnBillingRecord = errorProcessingDao.getQClnBillingAssignmentByAccnId(accnId);
        if (clnBillingRecord != null) {
            clnBillingRecord.setNewBillingAssignTypId(2);
            errorProcessingDao.setQClnBillingAssignment(clnBillingRecord);
            logger.info("Waiting for accession to complete BA, accnId=" + accnId);
            Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));
        }
		logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Actions: - Wait for PF EP Workflow Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Expected Results: - Verify that the accession has SUBID accn_pyr_err");
        String currDtStr = timeStamp.getCurrentDate();
        AccnPyrErr accnPyrErrInfoList = errorProcessingDao.getAccnPyrErrByAccnIdErrCdPyrPrioErrDt(accnId, 2136, 1, currDtStr); //2136: SUBID ErrCd
        Assert.assertNotNull(accnPyrErrInfoList, "        Accession ID " + accnId + " should have SUBID accn_pyr_err.");

		logger.info("*** Actions: - Wait for PF EP Outside Agency Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_OUT_AGNCY, QUEUE_WAIT_TIME_MS));

        logger.info("*** Expected Results: - Verify that the accession is out of Q_EP_OUT_AGNCY queue");
        QEp qEp = errorProcessingDao.getQEpOutAgncyByAccnId(accnId);
        assertNull(qEp);

        logger.info("*** Expected Results: - Verify that the accession is out of Q_EP_OUT_AGNCY_ERR table");
        QEpErr qEpErr = errorProcessingDao.getQEpOutAgncyErrByAccnId(accnId);
        assertNull(qEpErr);

        logger.info("*** Expected Results: - Verify that the accession is in Q_EP_HLD queue");
        qEp = errorProcessingDao.getQEpHldByAccnId(accnId);
        assertNotNull(qEp);

        logger.info("*** Expected Results: - Verify that the accession is in Q_EP_HLD_ERR table");
        qEpErr = errorProcessingDao.getQEpHldErrByAccnId(accnId);
        assertNotNull(qEpErr);

        logger.info("*** Expected Results: - Verify that the accession is added into ACCN_AGNCY table");
        AccnAgncy accnAgncy = accessionDao.getAccnAgncyByAccnId(accnId);
        assertNotNull(accnAgncy);

		logger.info("*** Expected Results: - Verify that ACCN_QUE is updated properly");
		AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
		assertEquals(accnQue.getQTyp(), 15); //15: EP Hold

		logger.info("*** Expected Results: - Verify that the file is generated under the folder");
		FileManipulation fileManipulation = new FileManipulation(driver);
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "epAgency");
		String filePathForFrAdd = dirBase + "frontrunner" + File.separator;
		String partialFileName = "FR-INS_" + timeStamp.getCurrentDate("yyyyMMdd");
        String fullFileName = agencyDao.getAgncyFileByPartialFileName(partialFileName).get(0).getFilename();

		logger.info("*** Expected Results: - Verify that the values are correctly generated in the file");
		//Expected file contents
		TevixRecord expectedTevixRecord = setTevixRecord(accnId);

		//Actual file contents
		List<String> outputFileList = FileUtils.readLines(new File(filePathForFrAdd + fullFileName), "UTF-8");
		TevixRecord actualTevixRecord = new TevixRecord();
		for (String line:outputFileList){
			if (line.contains(accnId)){
				System.out.println("line=" + line);
				actualTevixRecord = new TevixRecord(line);
				break;
			}
		}
		logger.info("       File contents: " + actualTevixRecord.toString());

		assertEquals(actualTevixRecord, expectedTevixRecord);

	}

	@Test(priority = 1, description = "CodeRyte-Verify file can be generated")
	@Parameters({ "project", "testSuite", "testCase"})
	public void testPFER_583( String project, String testSuite, String testCase) throws Exception {
		logger.info("========== Testing - testPFER_583 ==========");
		timeStamp = new TimeStamp(driver);


		logger.info("*** Actions: - Send WS request to create a Final Reported 3rd Party Accession with AUTH error");
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("        AccnID: " + accnId);

		logger.info("*** Expected Results: - Verify that a new accession was generated");
        assertNotNull(accnId, "        A new Accession was generated.");

        QClnBillingAssignment clnBillingRecord = errorProcessingDao.getQClnBillingAssignmentByAccnId(accnId);
        if (clnBillingRecord != null) {
            clnBillingRecord.setNewBillingAssignTypId(2);
            errorProcessingDao.setQClnBillingAssignment(clnBillingRecord);
            logger.info("Waiting for accession to complete BA, accnId=" + accnId);
            Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));
        }
		logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Actions: - Wait for PF EP Workflow Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Expected Results: - Verify that the accession has AUTH accn_pyr_err");
        String currDtStr = timeStamp.getCurrentDate();
        AccnPyrErr accnPyrErrInfoList = errorProcessingDao.getAccnPyrErrByAccnIdErrCdPyrPrioErrDt(accnId, 2149, 1, currDtStr); //2149: AUTH ErrCd
        Assert.assertNotNull(accnPyrErrInfoList, "        Accession ID " + accnId + " should have AUTH accn_pyr_err.");

		logger.info("*** Actions: - Wait for PF EP Outside Agency Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_OUT_AGNCY, QUEUE_WAIT_TIME_MS));

        logger.info("*** Expected Results: - Verify that the accession is in Q_EP_HLD queue");
        QEp qEp = errorProcessingDao.getQEpHldByAccnId(accnId);
        assertNotNull(qEp);

        logger.info("*** Expected Results: - Verify that the accession is in Q_EP_HLD_ERR table");
        QEpErr qEpErr = errorProcessingDao.getQEpHldErrByAccnId(accnId);
        assertNotNull(qEpErr);

        logger.info("*** Expected Results: - Verify that the accession is added into ACCN_AGNCY table");
        AccnAgncy accnAgncy = accessionDao.getAccnAgncyByAccnId(accnId);
        assertNotNull(accnAgncy);

		logger.info("*** Expected Results: - Verify that ACCN_QUE is updated properly");
		AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
		assertEquals(accnQue.getQTyp(), 15); //15: EP Hold

		logger.info("*** Expected Results: - Verify that the file is generated under the folder");
		FileManipulation fileManipulation = new FileManipulation(driver);
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "epAgency");
		String filePathForFrAdd = dirBase + "coderyte" + File.separator;
		String partialFileName = "CodeRyte_" + config.getProperty(PropertyMap.ORGALIAS) + "_" + timeStamp.getCurrentDate("yyyyMMdd");
        String fullFileName = agencyDao.getAgncyFileByPartialFileName(partialFileName).get(0).getFilename();

		File[] fileList = fileManipulation.listFilesByDescendingDate(new File(filePathForFrAdd));
		for (File file:fileList){
			if (file.getName().contains(fullFileName)){
				logger.info("       The file " + fullFileName + " is generated in folder " + filePathForFrAdd);
				break;
			}
		}

		logger.info("*** Expected Results: - Verify that the values are correctly generated in the file");
		//Expected file contents
		CodeRyteRecord expectedCodeRyteRecord = setCodeRyteRecord(accnId);

		//Actual file contents
		List<String> outputFileList = FileUtils.readLines(new File(filePathForFrAdd + fullFileName), "UTF-8");
		CodeRyteRecord actualCodeRyteRecord = new CodeRyteRecord();
		for (String line:outputFileList){
			if (line.contains(accnId)){
				System.out.println("line=" + line);
				actualCodeRyteRecord = new CodeRyteRecord(line);
				break;
			}
		}
		logger.info("       File contents: " + actualCodeRyteRecord.toString());

		assertEquals(actualCodeRyteRecord, expectedCodeRyteRecord);


	}

	@Test(priority = 1, description = "PECOS-Verify the accn is pushed into Q_VALIDATE_ACCN")
	@Parameters({ "project", "testSuite", "testCase"})
	public void testPFER_585( String project, String testSuite, String testCase) throws Exception {
		logger.info("========== Testing - testPFER_585 ==========");
		timeStamp = new TimeStamp(driver);

		logger.info("*** Actions: - Send Accession WS to create a new Priced accession");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());

		String accnId = resultProperties.getProperty("NewAccnID");
		logger.info("*** Expected Results: - Verify that a new accession was generated");
        assertNotNull(accnId, "        A new Accession was generated.");

        QClnBillingAssignment clnBillingRecord = errorProcessingDao.getQClnBillingAssignmentByAccnId(accnId);
        if (clnBillingRecord != null) {
            clnBillingRecord.setNewBillingAssignTypId(2);
            errorProcessingDao.setQClnBillingAssignment(clnBillingRecord);
            logger.info("Waiting for accession to complete BA, accnId=" + accnId);
            Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));
        }
		logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

		String currDtStr = timeStamp.getCurrentDate();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date currDt = new java.sql.Date(df.parse(currDtStr).getTime());


		List<AccnPyrErr> accnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId);

		for(AccnPyrErr accnPyrErr: accnPyrErrs){
			accnPyrErr.setFixDt(currDt);
			accnPyrErr.setFixUserId("AutomationTest");
			accessionDao.setAccnPyrErr(accnPyrErr);
		}

		int errSeqId = daoManagerPlatform.getNextvalFromDual("ACCN_PYR_ERR_SEQ", testDb);
		AccnPyrErr ape = new AccnPyrErr();
		ape.setErrSeq(errSeqId);
		ape.setAccnId(accnId);
		ape.setPyrPrio(1);
		ape.setErrCd(205); //PECOSPHYS
		ape.setErrDt(currDt);
		ape.setIsPosted(true);
		accessionDao.setAccnPyrErr(ape);

		logger.info("*** Actions: - Manually add a new record in Q_VALIDATE_ACCN table");
        try {
            accessionDao.getQValidateAccn(accnId);
        } catch (Exception e) {
            QValidateAccn qValidateAccn = new QValidateAccn();
            qValidateAccn.setAccnId(accnId);
            qValidateAccn.setValidateTypId(ValidateQueueMap.RE_ASSSESS_START_OVER);
            qValidateAccn.setPriority(1);
            qValidateAccn.setIsErr(false);
            qValidateAccn.setNewPyrId(0);
            accessionDao.setQValidateAccn(qValidateAccn);
        }
		logger.info("Waiting for accession to exit validation queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfValidationQueue(accnId, ValidateQueueMap.RE_ASSSESS_START_OVER, QUEUE_WAIT_TIME_MS));

		logger.info("*** Actions: - Manually add a new record in Q_VALIDATE_PECOS table");
        int accnPhysSeqId = accessionDao.getAccnPhysByAccnId(accnId).get(0).getPhysSeqId();
		logger.info("accnPhysSeqId: " + accnPhysSeqId);
		QValidatePecos qValidatePecos = new QValidatePecos();
		qValidatePecos.setPhysSeqId(accnPhysSeqId);
		qValidatePecos.setInDt(currDt);
		qValidatePecos.setIsErr(false);
		errorProcessingDao.setQValidatePecos(qValidatePecos);

        logger.info("*** Expected Results: - Verify that the accession has PECOSPHYS accn_pyr_err");
        AccnPyrErr accnPyrErrInfoList = errorProcessingDao.getAccnPyrErrByAccnIdErrCdPyrPrioErrDt(accnId, 205, 1, currDtStr); //205: PECOSPHYS ErrCd
        Assert.assertNotNull(accnPyrErrInfoList, "        Accession ID " + accnId + " should have AUTH accn_pyr_err.");

		logger.info("*** Actions: - Wait for PF EP Outside Agency Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_OUT_AGNCY, QUEUE_WAIT_TIME_MS));

		logger.info("***Remove qValidatePecos");
		qValidatePecos.setResultCode(ErrorCodeMap.DELETED_RECORD);
		errorProcessingDao.setQValidatePecos(qValidatePecos);
	}

	@Test(priority = 1, description = "Verify the accn can be hold for certain days before moving to the next action")
	@Parameters({ "project", "testSuite", "testCase"})
	public void testPFER_586( String project, String testSuite, String testCase) throws Exception {
		logger.info("========== Testing - testPFER_586 ==========");
		timeStamp = new TimeStamp(driver);

        logger.info("*** Actions: - Set up AGNCY HOLD_DAYS");
        Agncy agncy = agencyDao.getAgncyByAbbrev("FR-ADD");
        agncy.setHoldDays(7); //Hold the accn for 7 days
		agencyDao.setAgncy(agncy);

		logger.info("*** Actions: - Send WS request to create a Final Reported 3rd Party Accession with PTZIP error");
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("        AccnID: " + accnId);

		logger.info("*** Expected Results: - Verify that a new accession was generated");
        assertNotNull(accnId, "        A new Accession was generated.");

		logger.info("*** Actions: - Add Pt State and Pt City manually in DB");
		Accn accn = accessionDao.getAccn(accnId);
		accn.setPtStId("CA");
		accn.setPtCity("SAN DIEGO");
		accessionDao.setAccn(accn);

        QClnBillingAssignment clnBillingRecord = errorProcessingDao.getQClnBillingAssignmentByAccnId(accnId);
        if (clnBillingRecord != null) {
            clnBillingRecord.setNewBillingAssignTypId(2);
            errorProcessingDao.setQClnBillingAssignment(clnBillingRecord);
            logger.info("Waiting for accession to complete BA, accnId=" + accnId);
            Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));
        }
		logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Actions: - Wait for PF EP Workflow Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Expected Results: - Verify that the accession has PTZIP accn_pyr_err");
        String currDtStr = timeStamp.getCurrentDate();
        AccnPyrErr accnPyrErrInfoList = errorProcessingDao.getAccnPyrErrByAccnIdErrCdPyrPrioErrDt(accnId, 2069, 1, currDtStr); //2069: PTZIP ErrCd
        Assert.assertNotNull(accnPyrErrInfoList, "        Accession ID " + accnId + " should have PTZIP accn_pyr_err.");

		logger.info("*** Expected Results: - Verify that the accession is in Q_EP_OUT_AGNCY queue");
		//Accn_que
		AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
		assertEquals(accnQue.getQTyp(), 14); //14: EP Oustsie Agency

		//Q_EP_OUT_AGNCY
        QEp qEp = errorProcessingDao.getQEpOutAgncyByAccnId(accnId);
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date currDt = new java.sql.Date(df.parse(currDtStr).getTime());
		String inDtStr = df.format(qEp.getInDt());
		Date inDt = new java.sql.Date(df.parse(inDtStr).getTime());
		assertEquals(inDt, currDt, "       Q_EP_OUT_AGNCY.IN_DT = " + currDt);

        logger.info("*** Expected Results: - Verify that the accession is in Q_EP_OUT_AGNCY_ERR table");
        //Q_EP_OUT_AGNCY_ERR
        QEpErr qEpErr = errorProcessingDao.getQEpOutAgncyErrByAccnId(accnId);
        assertEquals(qEpErr.getErrDt(), currDt);

		logger.info("*** Actions: - Wait for PF EP Outside Agency Engine");
		accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_OUT_AGNCY,QUEUE_WAIT_TIME_MS);

        logger.info("*** Expected Results: - Verify that the accession is not processed from Q_EP_OUT_AGNCY queue");
        qEp = errorProcessingDao.getQEpOutAgncyByAccnId(accnId);
        assertNotNull(qEp);

        logger.info("*** Expected Results: - Verify that the accession is not processed from Q_EP_OUT_AGNCY_ERR table");
        qEpErr = errorProcessingDao.getQEpOutAgncyErrByAccnId(accnId);
        assertNotNull(qEpErr);

        logger.info("*** Expected Results: - Verify that the accession is added to the ACCN_AGNCY with proper REQUEST_DT");
        AccnAgncy accnAgncy = accessionDao.getAccnAgncyByAccnId(accnId);
        assertNotNull(accnAgncy);
        assertEquals(accnAgncy.getRequestDt(), currDt);

        logger.info("*** Expected Results: - Verify that the accession is NOT moved into Q_EP_HLD queue");
        qEp = errorProcessingDao.getQEpHldByAccnId(accnId);
        assertNull(qEp);

        logger.info("*** Expected Results: - Verify that the accession is Not moved into Q_EP_HLD_ERR table");
        qEpErr = errorProcessingDao.getQEpHldErrByAccnId(accnId);
        assertNull(qEpErr);

		logger.info("*** Expected Results: - Verify that the file is generated under the folder");
		FileManipulation fileManipulation = new FileManipulation(driver);
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "epAgency");
		String filePathForFrAdd = dirBase + "frontrunner" + File.separator;
		String partialFileName = "FR-ADD_" + timeStamp.getCurrentDate("yyyyMMdd");
        String fullFileName = agencyDao.getAgncyFileByPartialFileName(partialFileName).get(0).getFilename();

		logger.info("*** Expected Results: - Verify that the values are correctly generated in the file");
		//Expected file contents
		TevixRecord expectedTevixRecord = setTevixRecord(accnId);

		//Actual file contents
		List<String> outputFileList = FileUtils.readLines(new File(filePathForFrAdd + fullFileName), "UTF-8");
		TevixRecord actualTevixRecord = new TevixRecord();
		for (String line:outputFileList){
			if (line.contains(accnId)){
				System.out.println("line=" + line);
				actualTevixRecord = new TevixRecord(line);
				break;
			}
		}
		logger.info("       File contents: " + actualTevixRecord.toString());

		assertEquals(actualTevixRecord, expectedTevixRecord);

		logger.info("*** Actions: - Manually update REQUEST_DT to be the date passes the hold days in AGNCY table");
		String previousDateStr = timeStamp.getPreviousDate("MM/dd/yyyy", 7);
		Date updatedRquestDt = new java.sql.Date(df.parse(previousDateStr).getTime());
		rpmDao.updateAccnAgncyRequestDtByAccnId(testDb, updatedRquestDt, accnId);

        logger.info("*** Actions: - Wait for PF EP Outside Agency Engine");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_OUT_AGNCY, QUEUE_WAIT_TIME_MS));

        logger.info("*** Expected Results: - Verify that the accession is out of Q_EP_OUT_AGNCY queue");
        qEp = errorProcessingDao.getQEpOutAgncyByAccnId(accnId);
        assertNull(qEp);

        logger.info("*** Expected Results: - Verify that the accession is out of Q_EP_OUT_AGNCY_ERR table");
        qEpErr = errorProcessingDao.getQEpOutAgncyErrByAccnId(accnId);
        assertNull(qEpErr);

        logger.info("*** Expected Results: - Verify that the accession stays into ACCN_AGNCY table");
        accnAgncy = accessionDao.getAccnAgncyByAccnId(accnId);
        assertNotNull(accnAgncy);

        logger.info("*** Expected Results: - Verify that the accession is moved into Q_EP_HLD queue");
        qEp = errorProcessingDao.getQEpHldByAccnId(accnId);
        assertNotNull(qEp);

        logger.info("*** Expected Results: - Verify that the accession is moved into Q_EP_HLD_ERR table");
        qEpErr = errorProcessingDao.getQEpHldErrByAccnId(accnId);
        assertNotNull(qEpErr);


	}

	@Test(priority = 1, description = "FR-ADD-PreCorresp-Verify file can be generated")
	@Parameters({ "project", "testSuite", "testCase"})
	public void testPFER_598( String project, String testSuite, String testCase) throws Exception {
		logger.info("========== Testing - testPFER_598 ==========");
		timeStamp = new TimeStamp(driver);

        logger.info("*** Actions: - Set up AGNCY HOLD_DAYS");
        Agncy agncy = agencyDao.getAgncyByAbbrev("FR-ADD-PRE");
        agncy.setHoldDays(0); //Hold the accn for 0 days
		agencyDao.setAgncy(agncy);

		logger.info("*** Actions: - Send WS request to create a Final Reported 3rd Party Accession with NODIAG error");
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("        AccnID: " + accnId);

		logger.info("*** Expected Results: - Verify that a new accession was generated");
        assertNotNull(accnId, "        A new Accession was generated.");

		logger.info("*** Actions: - Add Pt State and Pt City manually in DB");
		Accn accn = accessionDao.getAccn(accnId);
		accn.setPtStId("CA");
		accn.setPtCity("SAN DIEGO");
		accessionDao.setAccn(accn);

        QClnBillingAssignment clnBillingRecord = errorProcessingDao.getQClnBillingAssignmentByAccnId(accnId);
        if (clnBillingRecord != null) {
            clnBillingRecord.setNewBillingAssignTypId(2);
            errorProcessingDao.setQClnBillingAssignment(clnBillingRecord);
            logger.info("Waiting for accession to complete BA, accnId=" + accnId);
            Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));
        }
		logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Actions: - Wait for PF EP Workflow Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Expected Results: - Verify that the accession is in Q_EP_OUT_AGNCY_PRE_CORRESP queue");
        //Accn_que
        AccnQue accnQueInfoList = accessionDao.getCurrentAccnQue(accnId);
        assertEquals(accnQueInfoList.getQTyp(), 16, "        Accession ID " + accnId + " should be in Q_Typ = 16 (EP Outside Agency Pre Corresp).");

        logger.info("*** Expected Results: - Verify that the accession has NODIAG accn_pyr_err");
        String currDtStr = timeStamp.getCurrentDate();
        AccnPyrErr accnPyrErrInfoList = errorProcessingDao.getAccnPyrErrByAccnIdErrCdPyrPrioErrDt(accnId, 1105, 1, currDtStr); //1105: NODIAG ErrCd
        Assert.assertNotNull(accnPyrErrInfoList, "        Accession ID " + accnId + " should have NODIAG accn_pyr_err.");

		logger.info("*** Actions: - Wait for PF EP Outside Agency Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_OUT_AGNCY_PRE_CORRESP, QUEUE_WAIT_TIME_MS));

        logger.info("*** Expected Results: - Verify that the accession is in Q_EP_HLD queue");
        QEp qEp = errorProcessingDao.getQEpByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_HLD, accnId);
        assertNotNull(qEp);

        logger.info("*** Expected Results: - Verify that the accession is in Q_EP_HLD_ERR table");
        QEpErr qEpErr = errorProcessingDao.getQEpErrByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_HLD_ERR, accnId);
        assertNotNull(qEpErr);

		logger.info("*** Expected Results: - Verify that ACCN_QUE is updated properly");
		AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
		assertEquals(accnQue.getQTyp(), 15); //15: EP Hold

        logger.info("*** Expected Results: - Verify that the accession is added into ACCN_AGNCY table");
        AccnAgncy accnAgncy = accessionDao.getAccnAgncyByAccnId(accnId);
        assertNotNull(accnAgncy);

		logger.info("*** Expected Results: - Verify that the file is generated under the folder");
		FileManipulation fileManipulation = new FileManipulation(driver);
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "epAgency");
		String filePathForFrAdd = dirBase + "frontrunner" + File.separator;
		String partialFileName = "FR-ADD-PRE_" + timeStamp.getCurrentDate("yyyyMMdd");
        String fullFileName = agencyDao.getAgncyFileByPartialFileName(partialFileName).get(0).getFilename();

		logger.info("*** Expected Results: - Verify that the values are correctly generated in the file");
		//Expected file contents
		TevixRecord expectedTevixRecord = setTevixRecord(accnId);

		//Actual file contents
		List<String> outputFileList = FileUtils.readLines(new File(filePathForFrAdd + fullFileName), "UTF-8");
		TevixRecord actualTevixRecord = new TevixRecord();
		for (String line:outputFileList){
			if (line.contains(accnId)){
				System.out.println("line=" + line);
				actualTevixRecord = new TevixRecord(line);
				break;
			}
		}
		logger.info("       File contents: " + actualTevixRecord.toString());

		assertEquals(actualTevixRecord, expectedTevixRecord);

	}

	@Test(priority = 1, description = "Accns in both Pre and Post Corresp queues-Verify both files can be generated")
	@Parameters({ "project", "testSuite", "testCase1", "testCase2"})
	public void testPFER_601( String project, String testSuite, String testCase1, String testCase2) throws Exception {
		logger.info("========== Testing - testPFER_601 ==========");
		timeStamp = new TimeStamp(driver);

		logger.info("*** Actions: - Set up AGNCY HOLD_DAYS");
        Agncy agncy = agencyDao.getAgncyByAbbrev("FR-INS-PRE");
        agncy.setHoldDays(0); //Hold the accn for 0 days
		agencyDao.setAgncy(agncy);

		logger.info("*** Actions: - Set up Err Code SUBID");
        ErrCd errCd = errorDao.getErrCdByErrCd(2136); //2136: SUBID ErrCd
		errCd.setIsAutoMtch(false);
		errCd.setIsMtchCmp(false);
		errCd.setIsManActn(false);
		errCd.setCorrespTypId(0);
		errCd.setOutAgncyId(0);
		errCd.setFinalActnId(7); //7: Hold
		errCd.setOutAgncyIdPreCorresp(9); //9: FR-INS-PRE
		errorDao.setErrCd(errCd);

		logger.info("*** Actions: - Set up Err Code AUTH");
        errCd = errorDao.getErrCdByErrCd(2149); //2149: AUTH ErrCd
		errCd.setIsAutoMtch(false);
		errCd.setIsMtchCmp(false);
		errCd.setIsManActn(false);
		errCd.setCorrespTypId(0);
		errCd.setOutAgncyId(3); //3: CODERYTE
		errCd.setFinalActnId(7); //7: Hold
		errCd.setOutAgncyIdPreCorresp(0);
		errorDao.setErrCd(errCd);

		logger.info("*** Actions: - Send WS request to create a Final Reported 3rd Party Accession with SUBID error");
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase1, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId1 = testProperties.getProperty("NewAccnID");
		System.out.println("        AccnID1: " + accnId1);

		logger.info("*** Expected Results: - Verify that a new accession1 was generated");
        assertNotNull(accnId1, "        A new Accession1 was generated.");

		logger.info("*** Actions: - Add Pt State and Pt City manually in DB for accession1");
		Accn accn = accessionDao.getAccn(accnId1);
		accn.setPtStId("CA");
		accn.setPtCity("SAN DIEGO");
		accessionDao.setAccn(accn);

		logger.info("*** Actions: - Send WS request to create a Final Reported 3rd Party Accession with AUTH error");
		Properties testProperties2 = TestDataSetup.executeWsTestCase(project, testSuite, testCase2, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId2 = testProperties2.getProperty("NewAccnID");
		System.out.println("        AccnID2: " + accnId2);

		logger.info("*** Expected Results: - Verify that a new accession2 was generated");
        assertNotNull(accnId2, "        A new Accession2 was generated.");

		logger.info("*** Actions: - Add Pt State and Pt City manually in DB for accession2");
		accn = accessionDao.getAccn(accnId2);
		accn.setPtStId("CA");
		accn.setPtCity("SAN DIEGO");
		accessionDao.setAccn(accn);

        QClnBillingAssignment clnBillingRecord = errorProcessingDao.getQClnBillingAssignmentByAccnId(accnId2);
        if (clnBillingRecord != null) {
            clnBillingRecord.setNewBillingAssignTypId(2);
            errorProcessingDao.setQClnBillingAssignment(clnBillingRecord);
            logger.info("Waiting for accession to complete BA, accnId=" + accnId2);
            Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId2, QUEUE_WAIT_TIME_MS));
        }
		logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId2);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId2, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId2);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId2, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
		logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId2);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId2, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Actions: - Wait for PF EP Workflow Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId2, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Expected Results: - Verify that the accession1 has SUBID accn_pyr_err");
		String currDtStr = timeStamp.getCurrentDate();
        AccnPyrErr accnPyrErrInfoList = errorProcessingDao.getAccnPyrErrByAccnIdErrCdPyrPrioErrDt(accnId1, 2136, 1, currDtStr); //2136: SUBID ErrCd
        Assert.assertNotNull(accnPyrErrInfoList, "        Accession ID1 " + accnId1 + " should have SUBID accn_pyr_err.");

        logger.info("*** Expected Results: - Verify that the accession2 has AUTH accn_pyr_err");
        accnPyrErrInfoList = errorProcessingDao.getAccnPyrErrByAccnIdErrCdPyrPrioErrDt(accnId2, 2149, 1, currDtStr); //2149: AUTH ErrCd
        Assert.assertNotNull(accnPyrErrInfoList, "        Accession ID2 " + accnId2 + " should have AUTH accn_pyr_err.");

		logger.info("*** Actions: - Wait for PF EP Outside Agency Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId2, AccnStatusMap.Q_EP_OUT_AGNCY, QUEUE_WAIT_TIME_MS));

        logger.info("*** Expected Results: - Verify that the accession1 is added into ACCN_AGNCY table");
        AccnAgncy accnAgncy = accessionDao.getAccnAgncyByAccnId(accnId1);
        assertNotNull(accnAgncy);

		logger.info("*** Expected Results: - Verify that the accession1 is in Q_EP_HLD queue");
		QEp qEp = errorProcessingDao.getQEpByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_HLD,accnId1);
        assertNotNull(qEp);

		logger.info("*** Expected Results: - Verify that the accession1 is in Q_EP_HLD_ERR table");
		QEpErr qEpErr = errorProcessingDao.getQEpErrByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_HLD_ERR, accnId1);
        assertNotNull(qEpErr);

		logger.info("*** Expected Results: - Verify that ACCN_QUE is updated properly for accession1");
		AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId1);
		assertEquals(accnQue.getQTyp(), 15); //15: EP Hold

		logger.info("*** Expected Results: - Verify that the file is generated under the folder for accession1");
		FileManipulation fileManipulation = new FileManipulation(driver);
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "epAgency");
		String filePathForFrAdd = dirBase + "frontrunner" + File.separator;
		String partialFileName = "FR-INS-PRE_" + timeStamp.getCurrentDate("yyyyMMdd");
        String fullFileName = agencyDao.getAgncyFileByPartialFileName(partialFileName).get(0).getFilename();

		logger.info("*** Expected Results: - Verify that the values are correctly generated in the file for accession1");
		//Expected file contents
		TevixRecord expectedTevixRecord = setTevixRecord(accnId1);

		//Actual file contents
		List<String> outputFileList = FileUtils.readLines(new File(filePathForFrAdd + fullFileName), "UTF-8");
		TevixRecord actualTevixRecord = new TevixRecord();
		for (String line:outputFileList){
			if (line.contains(accnId1)){
				System.out.println("line=" + line);
				actualTevixRecord = new TevixRecord(line);
				break;
			}
		}
		logger.info("       File contents: " + actualTevixRecord.toString());

		assertEquals(actualTevixRecord, expectedTevixRecord);

        logger.info("*** Expected Results: - Verify that the accession2 is added into ACCN_AGNCY table");
        accnAgncy = accessionDao.getAccnAgncyByAccnId(accnId2);
        assertNotNull(accnAgncy);

        logger.info("*** Expected Results: - Verify that the accession is in Q_EP_HLD queue");
        qEp = errorProcessingDao.getQEpByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_HLD, accnId2);
        assertNotNull(qEp);

        logger.info("*** Expected Results: - Verify that the accession is in Q_EP_HLD_ERR table");
        qEpErr = errorProcessingDao.getQEpErrByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_HLD_ERR, accnId2);
        assertNotNull(qEpErr);

		logger.info("*** Expected Results: - Verify that ACCN_QUE is updated properly for accession2");
		accnQue = accessionDao.getAccnQueByAccnId(accnId2);
		assertEquals(accnQue.getQTyp(), 15); //15: EP Hold

		logger.info("*** Expected Results: - Verify that the file is generated under the folder for accession2");
		filePathForFrAdd = dirBase + "coderyte" + File.separator;
		partialFileName = "CodeRyte_" + config.getProperty(PropertyMap.ORGALIAS) + "_" + timeStamp.getCurrentDate("yyyyMMdd");
        fullFileName = agencyDao.getAgncyFileByPartialFileName(partialFileName).get(0).getFilename();

		File[] fileList = fileManipulation.listFilesByDescendingDate(new File(filePathForFrAdd));
		for (File file:fileList){
			if (file.getName().contains(fullFileName)){
				logger.info("       The file " + fullFileName + " is generated in folder " + filePathForFrAdd);
				break;
			}
		}

		logger.info("*** Expected Results: - Verify that the values are correctly generated in the file for accession2");
		//Expected file contents
		CodeRyteRecord expectedCodeRyteRecord = setCodeRyteRecord(accnId2);

		//Actual file contents
		outputFileList = FileUtils.readLines(new File(filePathForFrAdd + fullFileName), "UTF-8");
		CodeRyteRecord actualCodeRyteRecord = new CodeRyteRecord();
		for (String line:outputFileList){
			if (line.contains(accnId2)){
				System.out.println("line=" + line);
				actualCodeRyteRecord = new CodeRyteRecord(line);
				break;
			}
		}
		logger.info("       File contents: " + actualCodeRyteRecord.toString());

		assertEquals(actualCodeRyteRecord, expectedCodeRyteRecord);

	}


	//*******************************************************************************************************************//
	private void setUpTestCondition() throws XifinDataNotFoundException, XifinDataAccessException {
		logger.info("*** Actions: - Set up Err Code PTZIP");
		ErrCd errCd = errorDao.getErrCdByErrCd(2069); //2609: PTZIP ErrCd
		errCd.setIsAutoMtch(false);
		errCd.setIsMtchCmp(false);
		errCd.setIsManActn(false);
		errCd.setCorrespTypId(0);
		errCd.setOutAgncyId(2); //2: FR-ADD(TevixMD)
		errCd.setFinalActnId(7); //7: Hold
		errCd.setOutAgncyIdPreCorresp(0);
        errorDao.setErrCd(errCd);

		logger.info("*** Actions: - Set up Err Code SUBID");
        errCd = errorDao.getErrCdByErrCd(2136); //2136: SUBID ErrCd
		errCd.setIsAutoMtch(false);
		errCd.setIsMtchCmp(false);
		errCd.setIsManActn(false);
		errCd.setCorrespTypId(0);
		errCd.setOutAgncyId(5); //5: FR-INS
		errCd.setFinalActnId(7); //7: Hold
		errCd.setOutAgncyIdPreCorresp(0);
        errorDao.setErrCd(errCd);

		logger.info("*** Actions: - Set up Err Code AUTH");
        errCd = errorDao.getErrCdByErrCd(2149); //2149: AUTH ErrCd
		errCd.setIsAutoMtch(false);
		errCd.setIsMtchCmp(false);
		errCd.setIsManActn(false);
		errCd.setCorrespTypId(0);
		errCd.setOutAgncyId(3); //3: CODERYTE
		errCd.setFinalActnId(7); //7: Hold
		errCd.setOutAgncyIdPreCorresp(0);
        errorDao.setErrCd(errCd);

		logger.info("*** Actions: - Set up SS# = 1434 (List of agency Ids that allow accessions to be included in Ep Outside Agency file multiple times)");
		SystemSetting systemSetting = systemDao.getSystemSetting(1434);
		systemSetting.setDataValue("2,3,5,6,7,9");
		systemDao.setSystemSetting(systemSetting);

        logger.info("*** Actions: - Set up AGNCY HOLD_DAYS");
        Agncy agncy = agencyDao.getAgncyByAbbrev("FR-ADD");
        agncy.setHoldDays(0);
		agencyDao.setAgncy(agncy);
        agncy = agencyDao.getAgncyByAbbrev("FR-INS");
        agncy.setHoldDays(0);
		agencyDao.setAgncy(agncy);
        agncy = agencyDao.getAgncyByAbbrev("FR-ADD-PRE");
        agncy.setHoldDays(0);
		agencyDao.setAgncy(agncy);
        agncy = agencyDao.getAgncyByAbbrev("FR-INS-PRE");
        agncy.setHoldDays(0);
		agencyDao.setAgncy(agncy);

		logger.info("*** Actions: - Delete all the files under /files/epAgency/ folder");
		FileManipulation fileManipulation = new FileManipulation(driver);
		String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "epAgency");
		//Clear frontrunner folder
		String filePathForFrAdd = dirBase + "frontrunner" + File.separator;
		try {
			FileUtils.cleanDirectory(new File(filePathForFrAdd));
		}catch (Exception e){
			logger.info("       Folder " + filePathForFrAdd + " does not exist. No need to clear.");
		}
		//Clear coderyte folder
		String filePathForCodeRyte = dirBase + "coderyte" + File.separator;
		try {
			FileUtils.cleanDirectory(new File(filePathForCodeRyte));
		}catch (Exception e){
			logger.info("       Folder " + filePathForCodeRyte + " does not exist. No need to clear.");
		}

		//Clear tevix folder
		String filePathForTevix = dirBase + "tevix" + File.separator;
		try {
			FileUtils.cleanDirectory(new File(filePathForTevix));
		}catch (Exception e){
			logger.info("       Folder " + filePathForTevix + " does not exist. No need to clear.");
		}

        logger.info("*** Actions: - Delete all the records in Q_VALIDATE_PECOS");
        errorProcessingDao.deleteQValidatePecos();

        logger.info("*** Actions: - Delete all the records in AGNCY_FILE");
		agencyDao.deleteAgncyFile();

        logger.info("*** Actions: - Set Payor to require the Dx Code");
        Pyr pyr = payorDao.getPyrByPyrAbbrv("BSEAST"); //The pyr used for PFER-598
        pyr.setIsDiagReq(true);
        payorDao.setPyr(pyr);

		logger.info("*** Actions: - Set up Err Code NODIAG");
        errCd = errorDao.getErrCdByErrCd(1105); //1105: NODIAG ErrCd
		errCd.setIsAutoMtch(false);
		errCd.setIsMtchCmp(false);
		errCd.setIsManActn(false);
		errCd.setCorrespTypId(0);
		errCd.setOutAgncyId(0);
		errCd.setFinalActnId(7); //7: Hold
		errCd.setOutAgncyIdPreCorresp(6); //6: FR-ADD-PRE
        errorDao.setErrCd(errCd);
	}

	private TevixRecord setTevixRecord(String accnId) throws XifinDataNotFoundException, XifinDataAccessException {
		TevixRecord tevixRecord = new TevixRecord();
		Accn accn = accessionDao.getAccn(accnId);

		tevixRecord.setAccnId(accn.getAccnId());
		tevixRecord.setSsn(convertValue(String.valueOf(accn.getPtSsn())));
		tevixRecord.setLastName(convertValue(accn.getPtLNm()));
		tevixRecord.setFirstName(convertValue(accn.getPtFNm().split(" ")[0])); //Only get the First Name w/o Middle Name
		tevixRecord.setAddress1(convertValue(accn.getPtAddr1()));
		tevixRecord.setAddress2(convertValue(accn.getPtAddr2()));
		tevixRecord.setCity(convertValue(accn.getPtCity()));
		tevixRecord.setState(convertValue(accn.getPtStId()));
		tevixRecord.setZip(convertValue(accn.getPtZipId()));
		tevixRecord.setHomePhone(convertValue(accn.getPtHmPhn()));

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String dobStr = df.format(accn.getPtDob());
		tevixRecord.setDob(dobStr);

		String dosStr = df.format(accn.getDos());
		tevixRecord.setDos(dosStr);

		int genderId = accn.getPtSex();
		tevixRecord.setGender(getGender(genderId));

		return tevixRecord;
	}

	private CodeRyteRecord setCodeRyteRecord(String accnId) throws XifinDataNotFoundException, XifinDataAccessException {
		CodeRyteRecord codeRyteRecord = new CodeRyteRecord();
		Accn accn = accessionDao.getAccn(accnId);

		codeRyteRecord.setHeader("ACCN");
		codeRyteRecord.setAccnId(accn.getAccnId());

		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String dosStr = df.format(accn.getDos());
		codeRyteRecord.setDos(dosStr);

		String dobStr = df.format(accn.getPtDob());
		codeRyteRecord.setDob(dobStr);

		int genderId = accn.getPtSex();
		codeRyteRecord.setGender(genderDao.getGenderTypByGenderId(genderId).getDescr().trim());

		return codeRyteRecord;
	}

	private String getGender(int genderId){
		String gender = "";

		switch(genderId){
			case 1:
				gender = "M";
				break;
			case 2:
				gender = "F";
				break;
			default:
				//other types
		}

		return gender;
	}

	private String convertValue(String s){
		String str;

		if (s == null){
			str = "";
		}else if (s.equals("0")){
			str = "";
		}else{
			str = s;
		}

		return str;
	}

}
