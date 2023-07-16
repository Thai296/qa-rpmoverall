package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.accnQue.AccnQue;
import com.mbasys.mars.ejb.entity.accnTestErr.AccnTestErr;
import com.mbasys.mars.ejb.entity.errCd.ErrCd;
import com.mbasys.mars.ejb.entity.errCdOverride.ErrCdOverride;
import com.mbasys.mars.ejb.entity.pyrReqmnts.PyrReqmnts;
import com.mbasys.mars.ejb.entity.pyrReqmntsFld.PyrReqmntsFld;
import com.mbasys.mars.ejb.entity.qAccnSubm.QAccnSubm;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.mbasys.mars.persistance.MiscMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.headerNavigation.HeaderNavigation;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.rpm.domain.QEp;
import com.xifin.qa.dao.rpm.domain.QEpErr;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.ConvertUtil;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
import org.testng.Assert;
import org.testng.annotations.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

//import org.testng.Assert;


public class EPWorkflowEngineTest extends SeleniumBaseTest  {
	
	private SsoLogin ssoLogin;
	private HeaderNavigation headerNavigation;
	private TestDataSetup testDataSetup;
	private TimeStamp timeStamp;
	private XifinAdminUtils xifinAdminUtils;
	private ConvertUtil convertUtil;

 @AfterSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "xapEnv", "eType", "engConfigDB", "errCdId", "disableBrowserPlugins"})
    public void afterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, String xapEnv, String eType, String engConfigDB, int errCdId, @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running AfterSuite");	
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            logIntoSso(ssoUsername, ssoPassword); 	            
            //Reset err_cd SUBID
          	ErrCd errCd = rpmDao.getErrCd(testDb, Integer.valueOf(errCdId)); //SUBID
			//Reset err_cd
			errCd.setOutAgncyIdPreCorresp(0);
			errCd.setOutAgncyId(0);
			errCd.setCorrespTypId(2);//2: Client
			errCd.setFinalActnId(13);//13: Patient
			errCd.setIsManActn(false);
			errCd.setIsAutoMtch(true);
			errCd.setIsMtchCmp(true);
			errCd.setLtrTxt("Please provide the insurance subscriber identification number.");
			rpmDao.setErrCd(testDb, errCd);

			//Update Err Cd override
			List<ErrCdOverride> errCdOverrides = rpmDao.getErrCdOverrideByErrCd(testDb, errCdId);
			for(ErrCdOverride errCdOverride: errCdOverrides){
				if(errCdOverride.getPyrGrpId() >0){
					errCdOverride.setOutAgncyIdPreCorresp(0);
					errCdOverride.setOutAgncyId(0);
					errCdOverride.setCorrespTypId(0);
					errCdOverride.setLtrTxt("");
					errCdOverride.setFinalActnTyp(12);
					rpmDao.setErrCdOverride(testDb,errCdOverride);
				}
			}

			//Clear payor requirement override for P payor (4) and SubId field (4)
           	List<PyrReqmntsFld> pyrReqmntsFldList = rpmDao.getPyrReqmntsFldByFldIdPyrId(testDb, 4, 4);
    		for (PyrReqmntsFld pyrReqmntsFld : pyrReqmntsFldList){
    			pyrReqmntsFld.setResultCode(ErrorCodeMap.DELETED_RECORD);
    			rpmDao.setPyrReqmntsFld(testDb, pyrReqmntsFld);
    		} 
        	//Delete from pyr_reqmnts
        	List<PyrReqmnts> pyrReqmntsList = rpmDao.getPyrReqmntsByPyrId(testDb, 4);
        	for (PyrReqmnts pyrReqmnts : pyrReqmntsList){
        		pyrReqmnts.setResultCode(ErrorCodeMap.DELETED_RECORD);
        		rpmDao.setPyrReqmnts(testDb, pyrReqmnts);
        	}  
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


	@Test(priority = 1, description = "Process from Q_EP_UNBILLABLE")
	@Parameters({ "ssoUsername", "ssoPassword", "project", "testSuite", "testCase", "propLevel", "propName","wsUsername", "wsPassword", "eType","xapEnv", "engConfigDB", "errCdId"})
	public void testPFER_572( String ssoUsername, String ssoPassword, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String eType, String xapEnv, String engConfigDB, String errCdId) throws Exception {
    	logger.info("========== Testing - testPFER_572 ==========");

		logger.info("*** Actions: - Update SUBID error code to only have Final Action - Patient in DB");
        //Update err_cd
       	ErrCd errCd = rpmDao.getErrCd(testDb, Integer.valueOf(errCdId)); //SUBID
		errCd.setOutAgncyIdPreCorresp(0);
    	errCd.setOutAgncyId(0);
    	errCd.setCorrespTypId(0);
    	errCd.setFinalActnId(13);//7: Patient
    	errCd.setIsManActn(false);
    	errCd.setIsAutoMtch(false);
    	errCd.setIsMtchCmp(false);
    	errCd.setLtrTxt("Please provide the insurance subscriber identification number.");
    	rpmDao.setErrCd(testDb, errCd);

		//Update Err Cd override
		List<ErrCdOverride> errCdOverrides = rpmDao.getErrCdOverrideByErrCd(testDb, Integer.parseInt(errCdId));
		for(ErrCdOverride errCdOverride: errCdOverrides){
			if(errCdOverride.getPyrGrpId() >0){
				errCdOverride.setOutAgncyIdPreCorresp(0);
				errCdOverride.setOutAgncyId(0);
				errCdOverride.setCorrespTypId(0);
				errCdOverride.setLtrTxt("");
				errCdOverride.setFinalActnTyp(13);//7: Hold
				errCdOverride.setIsAutoMatch(false);
				errCdOverride.setIsMatchCompare(false);
				errCdOverride.setIsForceToMan(false);
				rpmDao.setErrCdOverride(testDb,errCdOverride);
			}
		}

    	logger.info("*** Actions: - Add Payor Requirement Override for Patient Payor and SubsId field in DB");
    	//Delete from pyr_reqmnts_fld
    	List<PyrReqmntsFld> pyrReqmntsFldList = rpmDao.getPyrReqmntsFldByFldIdPyrId(testDb, 4, 4);
    	for (PyrReqmntsFld pyrReqmntsFld : pyrReqmntsFldList){
    		pyrReqmntsFld.setResultCode(ErrorCodeMap.DELETED_RECORD);
    		rpmDao.setPyrReqmntsFld(testDb, pyrReqmntsFld);
    	}
    	//Delete from pyr_reqmnts
    	List<PyrReqmnts> pyrReqmntsList = rpmDao.getPyrReqmntsByPyrId(testDb, 4);
    	for (PyrReqmnts pyrReqmnts : pyrReqmntsList){
    		pyrReqmnts.setResultCode(ErrorCodeMap.DELETED_RECORD);
    		rpmDao.setPyrReqmnts(testDb, pyrReqmnts);
    	}

        ConvertUtil convertUtil = new ConvertUtil();
        Date effDt = convertUtil.convertStringToUtilDate("01/01/2000", "MM/dd/yyyy");

        //Insert new record into pyr_reqmnts
        PyrReqmnts pyrReqmnts = new PyrReqmnts();
        pyrReqmnts.setPyrId(4);
        pyrReqmnts.setEffDt(new java.sql.Date(effDt.getTime()));
        pyrReqmnts.setFldId(4);
        pyrReqmnts.setPatternOvrrde(".*");
        pyrReqmnts.setErrMsgOvrrde("NA");
        pyrReqmnts.setDosEffDt(new java.sql.Date(effDt.getTime()));
        rpmDao.setPyrReqmnts(testDb, pyrReqmnts);
        //Insert new records into pyr_reqmnts_fld
    	pyrReqmntsFldList = new ArrayList<PyrReqmntsFld>();
       	for (int scrnId = 1; scrnId <= 4; scrnId++){
       		PyrReqmntsFld pyrReqmntsFld = new PyrReqmntsFld();
       		pyrReqmntsFld.setPyrId(4);
       		pyrReqmntsFld.setEffDt(new java.sql.Date(effDt.getTime()));
       		pyrReqmntsFld.setFldId(4);
       		pyrReqmntsFld.setScrnId(scrnId);
       		pyrReqmntsFld.setReq(1); // "1" = optional
       		rpmDao.setPyrReqmntsFld(testDb, pyrReqmntsFld);
    	}

		logger.info("*** Actions: - Send WS request to create a new Priced 3rd party payor accession with SUBID error");
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());

		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("        AccnID: " + accnId);

		logger.info("*** Expected Results: - Verify that a new accession was generated");
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

    	logger.info("*** Expected Results: - Verify that the accession has SUBID accn_pyr_err");

    	List<AccnPyrErr> accnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId);
    	AccnPyrErr subIdErr = new AccnPyrErr();
    	for(AccnPyrErr accnPyrErr: accnPyrErrs){
    		if(accnPyrErr.getErrCd() == Integer.parseInt(errCdId)){
    			subIdErr = accnPyrErr;
			}
		}
    	Assert.assertTrue(subIdErr.getErrCd() == Integer.parseInt(errCdId) , "        Accession ID " + accnId + " should have SUBID accn_pyr_err.");

    	logger.info("*** Expected Results: - Verify that the accession is in Q_EP_UNBILLABLE (8) queue");
    	ArrayList<String> accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
    	Assert.assertTrue(accnQueInfoList.get(3).equals("8"), "        Accession ID " + accnId + " should be in Q_Typ = 8 (Unbillable).");

    	assertNotNull(rpmDao.getQEpUnbillableByAccnId(testDb, accnId), "       Accession " + accnId + " should be in Q_EP_UNBILLABLE table.");

		logger.info("*** Actions: - Wait for PF EP Workflow Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Expected Results: - Verify that a Patient Payor is added to the accession");
		List<AccnPyr> accnPyrList = accessionDao.getAccnPyrs(accnId);
		logger.info("*** Expected Results: - Pt payor id="+accnPyrList.get(1));
		assertEquals(accnPyrList.size(), 2, "       A Payor will be added to the Accession " + accnId);
		AccnPyr newPyr = accnPyrList.get(1);
		assertEquals(newPyr.getPyrId(), 4, "       A Patient Payor will be added to the Accession " + accnId);

		logger.info("*** Expected Results: - Verify that the accession is moved out of Q_EP_UNBILLABLE (8) queue");
		assertNull(rpmDao.getQEpUnbillableByAccnId(testDb, accnId), "       Accession " + accnId + " should be moved out of Q_EP_UNBILLABLE table.");

		logger.info("*** Expected Results: - Verify that the accession is in Q_ACCN_SUBM queue");
		List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
		assertEquals(qasList.size(), 1, "       Accession " + accnId + " should be in Q_ACCN_SUBM.");
		QAccnSubm qas = qasList.get(0);
		assertEquals(qas.getPyrId(), newPyr.getPyrId(), "Queued submission should have patient payor ID, accnId="+accnId);
		assertEquals(qas.getPyrGrpId(), MiscMap.PYR_GRP_PT, "Queued submission should have patient payor group, accnId="+accnId);
		assertEquals(qas.getPyrPrio(), newPyr.getPyrPrio(), "Queued submission should have patient payor prio, accnId="+accnId);

		logger.info("*** Actions: - Clear test data in DB");
		List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
		int accnPyrId=0;
		for(AccnPyr accnPyr: accnPyrs){
			if(accnPyr.getPyrPrio() == 1){
				accnPyrId = accnPyr.getPyrId();
			}
		}
    	String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(String.valueOf(accnPyrId), testDb);
		assertNotEquals(daoManagerAccnWS.deletePyrReqmntsFldByPyrAbrrev(pyrAbbrev, testDb), 0);
		assertNotEquals(daoManagerAccnWS.deletePyrReqmntsByPyrAbrrev(pyrAbbrev, testDb), 0);

    	pyrReqmntsFldList = rpmDao.getPyrReqmntsFldByFldIdPyrId(testDb, 4, 4);
    	for (PyrReqmntsFld pyrReqmntsFld : pyrReqmntsFldList){
    		pyrReqmntsFld.setResultCode(ErrorCodeMap.DELETED_RECORD);
    		rpmDao.setPyrReqmntsFld(testDb, pyrReqmntsFld);
    	}

    	//Delete from pyr_reqmnts
    	pyrReqmntsList = rpmDao.getPyrReqmntsByPyrId(testDb, 4);
    	for (PyrReqmnts pyrReqmnt : pyrReqmntsList){
    		pyrReqmnt.setResultCode(ErrorCodeMap.DELETED_RECORD);
    		rpmDao.setPyrReqmnts(testDb, pyrReqmnt);
    	}

	}


	@Test(priority = 1, description = "Skip if the accession in Obsolete (62) status")
	@Parameters({"ssoUsername", "ssoPassword", "project", "testSuite", "testCase", "propLevel", "propName","wsUsername", "wsPassword", "eType","xapEnv", "engConfigDB", "errCdId"})
	public void testPFER_573(String ssoUsername, String ssoPassword, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String eType, String xapEnv, String engConfigDB, String errCdId) throws Exception {
    	logger.info("========== Testing - testPFER_573 ==========");

		logger.info("*** Actions: - Send WS request to create a new Priced 3rd party payor accession with SUBID error");
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());

		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("        AccnID: " + accnId);

		logger.info("*** Expected Results: - Verify that a new accession was generated");
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

		List<AccnPyrErr> accnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId);
		AccnPyrErr subIdErr = new AccnPyrErr();
		for(AccnPyrErr accnPyrErr: accnPyrErrs){
			if(accnPyrErr.getErrCd() == Integer.parseInt(errCdId)){
				subIdErr = accnPyrErr;
			}
		}
		Assert.assertTrue(subIdErr.getErrCd() == Integer.parseInt(errCdId) , "        Accession ID " + accnId + " should have SUBID accn_pyr_err.");

    	logger.info("*** Expected Results: - Verify that the accession is in Q_EP_UNBILLABLE (8) queue");
    	ArrayList<String> accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
    	Assert.assertTrue(accnQueInfoList.get(3).equals("8"), "        Accession ID " + accnId + " should be in Q_Typ = 8 (Unbillable).");

    	assertNotNull(rpmDao.getQEpUnbillableByAccnId(testDb, accnId), "       Accession " + accnId + " should be in Q_EP_UNBILLABLE table.");

    	logger.info("*** Actions: - Manually change the accession status to 62 (Obsolete) in ACCN table in DB");
    	Accn accn = accessionDao.getAccn(accnId);
    	int originalAccnStatusId = accn.getStaId();
    	accn.setStaId(62);
    	accessionDao.setAccn(accn);

		logger.info("*** Actions: - Wait for PF EP Workflow Engine");
		assertFalse(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, (QUEUE_WAIT_TIME_MS/2)));

		logger.info("*** Expected Results: - Verify that the accession is not processed from Q_EP_UNBILLABLE (8) queue");
		assertNotNull(rpmDao.getQEpUnbillableByAccnId(testDb, accnId), "       Accession " + accnId + " should be in the Q_EP_UNBILLABLE table.");

		logger.info("*** Actions: - Clear test data in DB");
		List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
		int accnPyrId=0;
		for(AccnPyr accnPyr: accnPyrs){
			if(accnPyr.getPyrPrio() == 1){
				accnPyrId = accnPyr.getPyrId();
			}
		}
    	String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(String.valueOf(accnPyrId), testDb);
		assertNotEquals(daoManagerAccnWS.deletePyrReqmntsFldByPyrAbrrev(pyrAbbrev, testDb), 0);
		assertNotEquals(daoManagerAccnWS.deletePyrReqmntsByPyrAbrrev(pyrAbbrev, testDb), 0);

	   	accn.setStaId(originalAccnStatusId);
    	accessionDao.setAccn(accn);

	}


	@Test(priority = 1, description="Process from Q_EP_UNBILLABLE and the Error Code has Prioritized Actions")
	@Parameters({"ssoUsername", "ssoPassword","project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "eType", "xapEnv", "engConfigDB", "errCdId"})
	public void testPFER_574(String ssoUsername, String ssoPassword, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword,String eType, String xapEnv, String engConfigDB, String errCdId) throws Exception {
		logger.info("========== Testing - testPFER_574 ==========");


		logger.info("*** Actions: - Update SUBID error code to have Prioritized Actions in DB");
        //Update err_cd
      	ErrCd errCd = rpmDao.getErrCd(testDb, Integer.valueOf(errCdId)); //SUBID
       	errCd.setOutAgncyId(2);//2: TEVIXMD
       	errCd.setCorrespTypId(2);//2: Client
       	errCd.setFinalActnId(7);//7: Hold
       	errCd.setIsManActn(true);
       	errCd.setIsAutoMtch(true);
       	errCd.setIsMtchCmp(true);
       	errCd.setLtrTxt("Please provide the insurance subscriber identification number.");
       	rpmDao.setErrCd(testDb, errCd);
		timeStamp = new TimeStamp(driver);
		logger.info("*** Actions: - Send WS request to create a new Priced 3rd party payor accession with SUBID error");
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());

		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("        AccnID: " + accnId);

		logger.info("*** Expected Results: - Verify that a new accession was generated");
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

    	logger.info("*** Expected Results: - Verify that the accession has SUBID accn_pyr_err");
    	List<List<String>> accnInfoPyrList = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
    	String accnPyrId = accnInfoPyrList.get(0).get(0);

    	ArrayList<String> accnPyrInfoList = daoManagerXifinRpm.getAccnPyrInfoFromACCNPYRByAccnIdPyrIdPyrPrio(accnId, accnPyrId, "1", testDb);
    	Assert.assertTrue(accnPyrInfoList.get(4) == "", "        Accession ID " + accnId + " should not have a subs id.");

       	String currDt = timeStamp.getCurrentDate();
    	String errCdStr = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where abbrev = 'SUBID'", testDb).get(0);
    	List<String> accnPyrErrInfoList = daoManagerXifinRpm.getAccnPyErrFromACCNPYRERRByAccnId(accnId, "1", currDt, errCdStr, testDb);
    	Assert.assertTrue(accnPyrErrInfoList.size() > 0, "        Accession ID " + accnId + " should have SUBID accn_pyr_err.");

    	logger.info("*** Expected Results: - Verify that the accession is in Q_EP_UNBILLABLE (8) queue");
    	ArrayList<String> accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
		Thread.sleep(QUEUE_POLL_TIME_MS);
    	Assert.assertTrue(accnQueInfoList.get(3).equals("8"), "        Accession ID " + accnId + " should be in Q_Typ = 8 (Unbillable).");

		logger.info("*** Actions: - Wait for PF EP Workflow Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

    	logger.info("*** Expected Results: - Verify that the accession is moved to Q_EP_AUTO_MTCH (10) queue");
    	assertNotNull(rpmDao.getQEpAutoMtchByAccnId(testDb, accnId), "       Accession " + accnId + " should be in Q_EP_AUTO_MTCH table.");

		logger.info("*** Expected Results: - Verify that the ACCN_QUE.fk_q_typ is updated to 10 (EP Auto Match queue)");
		accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
    	Assert.assertTrue(accnQueInfoList.get(3).equals("10"), "        Accession " + accnId + " should be in Q_Typ = 10 (EP Auto Match).");

    	logger.info("*** Expected Results: - Verify that the accession is added to Q_EP_AUTO_MTCH_ERR table");
    	ArrayList<String> qEpAutoMtchErrInfoList = daoManagerPlatform.getQEPAutoMtchErrByConditon("pk_accn_id = '" + accnId + "' and pk_err_cd = " + errCdStr, testDb);
    	Assert.assertTrue(qEpAutoMtchErrInfoList.size() > 0, "        Accession ID " + accnId + " should be in Q_EP_AUTO_MTCH_ERR table.");

    	logger.info("*** Actions: - Clear test data in DB");
       	String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyrId, testDb);
    	assertNotEquals(daoManagerAccnWS.deletePyrReqmntsFldByPyrAbrrev(pyrAbbrev, testDb), 0);
    	assertNotEquals(daoManagerAccnWS.deletePyrReqmntsByPyrAbrrev(pyrAbbrev, testDb), 0);

		//Reset err_cd
		logger.info("*** Actions: - Reset SUBID ErrCd");
		errCd.setOutAgncyIdPreCorresp(0);
		errCd.setOutAgncyId(0);
		errCd.setCorrespTypId(2);//2: Client
		errCd.setFinalActnId(13);//13: Patient
		errCd.setIsManActn(false);
		errCd.setIsAutoMtch(true);
		errCd.setIsMtchCmp(true);
		errCd.setLtrTxt("Please provide the insurance subscriber identification number.");
		rpmDao.setErrCd(testDb, errCd);

	}


	@Test(priority = 1, description="Process from Q_EP_UNPRICEABLE")
	@Parameters({"ssoUsername", "ssoPassword","project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "eType", "eType2", "xapEnv", "engConfigDB", "errCdId2"})
	public void testPFER_575(String ssoUsername, String ssoPassword, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword,String eType, String eType2, String xapEnv, String engConfigDB, String errCdId2) throws Exception {
		logger.info("========== Testing - testPFER_575 ==========");


		logger.info("*** Actions: - Update INVFAC error code in DB");
        //Update err_cd
      	ErrCd errCd = rpmDao.getErrCd(testDb, Integer.valueOf(errCdId2)); //INVFAC
       	errCd.setOutAgncyId(0);
       	errCd.setCorrespTypId(0);
       	errCd.setFinalActnId(7);//7: Hold
       	errCd.setIsManActn(true);
       	errCd.setIsAutoMtch(false);
       	errCd.setIsMtchCmp(false);
       	errCd.setLtrTxt("");
       	rpmDao.setErrCd(testDb, errCd);

		logger.info("*** Actions: - Send WS request to create a new Priced 3rd party payor accession with SUBID error");
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());

		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("        AccnID: " + accnId);

    	logger.info("*** Expected Results: - Verify that a new accession was generated");
    	Assert.assertTrue(accnId != null, "        Did not generate a new Accession.");

    	logger.info("*** Expected Results: - Verify that the accession has INVFAC accn_test_err");
    	List<AccnTestErr> accnTestErrList = accessionDao.getAccnTestErrByAccnIdErrCd(accnId, Integer.valueOf(errCdId2));
    	Assert.assertTrue(accnTestErrList.size() > 0, "        Accession ID " + accnId + " should have INVFAC accn_test_err.");

		ArrayList<String> clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
		if(!clnBillingAssignInfoList.isEmpty()){
			daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 2", testDb);
			logger.info("Waiting for accession to complete BA, accnId=" + accnId);
			Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));
		}
		logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));

    	logger.info("*** Expected Results: - Verify that the accession is in Q_EP_UNPRICEABLE (7) queue");
    	ArrayList<String> accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
    	Assert.assertTrue(accnQueInfoList.get(3).equals("7"), "        Accession ID " + accnId + " should be in Q_Typ = 7 (Unpriceable).");

		logger.info("*** Actions: - Wait for PF EP Workflow Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNPRICEABLE, QUEUE_WAIT_TIME_MS));

    	logger.info("*** Expected Results: - Verify that the accession is moved to Q_EP_MAN (12) queue");
    	assertNotNull(rpmDao.getQEpManByAccnId(testDb, accnId), "       Accession " + accnId + " should be in Q_EP_MAN table.");

		logger.info("*** Expected Results: - Verify that the ACCN_QUE.fk_q_typ is updated to 12 (EP Manual queue)");
		accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
    	Assert.assertTrue(accnQueInfoList.get(3).equals("12"), "        Accession " + accnId + " should be in Q_Typ = 12 (EP Manual).");

	}


	@Test(priority = 1, description="Process from Q_EP_DENIAL")
	@Parameters({"ssoUsername", "ssoPassword","eType", "xapEnv", "engConfigDB", "errCdId3", "testSuite", "testCase", "project"})
	public void testPFER_576(String ssoUsername, String ssoPassword, String eType, String xapEnv, String engConfigDB, String errCdId3, String testSuite, String testCase, String project) throws Exception {
		logger.info("========== Testing - testPFER_576 ==========");


		timeStamp = new TimeStamp(driver);
		convertUtil = new ConvertUtil();

		logger.info("*** Actions: - Update PR31 error code in DB");
        //Update err_cd
      	ErrCd errCd = rpmDao.getErrCd(testDb, Integer.valueOf(errCdId3)); //PR31
       	errCd.setIsManActn(false);
       	errCd.setIsAutoMtch(true);
       	errCd.setIsMtchCmp(true);
       	rpmDao.setErrCd(testDb, errCd);

		String currDt = timeStamp.getCurrentDate();
		Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

		logger.info("*** Actions: - Send WS request to create a new Priced 3rd party payor accession");
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());

		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("        AccnID: " + accnId);

		logger.info("*** Expected Results: - Verify that a new accession was generated");
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

		AccnPyrErr ape = new AccnPyrErr();
		int errSeqId = daoManagerPlatform.getNextvalFromDual("ACCN_PYR_ERR_SEQ", testDb);
		ape.setErrSeq(errSeqId);
		ape.setAccnId(accnId);
		ape.setPyrPrio(1);
		ape.setErrCd(Integer.valueOf(errCdId3));
		ape.setErrDt(new java.sql.Date(sysDt.getTime()));
		ape.setIsPosted(true);
		accessionDao.setAccnPyrErr(ape);

		logger.info("*** Actions: - Update accn_que in DB");
		AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
		accnQue.setQTyp(9);//9: Q_EP_DENIAL
		accessionDao.setAccnQue(accnQue);

		logger.info("*** Actions: - Manually insert a record in Q_EP_DENIAL (9) queue in DB");
		Date todayDt = convertUtil.convertStringToUtilDate(timeStamp.getCurrentDate(), "MM/dd/yyyy");

		List<String> listCol = new ArrayList<String>();
		listCol.add("PK_ACCN_ID");
		listCol.add("IN_DT");

		List<Object> insertValues = new ArrayList<Object>();
		insertValues.add(accnId);
		insertValues.add(todayDt);

		int qEpDenailCount = daoManagerPlatform.addRecordIntoTable("Q_EP_DENIAL", listCol, insertValues, testDb);

		logger.info("*** Expected Results: - Verify that a new record is added into Q_EP_DENIAL table in DB");
		assertTrue(qEpDenailCount > 0,"        A new record should be added into Q_EP_DENIAL table.");

		logger.info("*** Actions: - Wait for PF EP Workflow Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_DENIAL, QUEUE_WAIT_TIME_MS));

    	logger.info("*** Expected Results: - Verify that the accession is moved to Q_EP_AUTO_MTCH (10) queue");
    	assertNotNull(rpmDao.getQEpAutoMtchByAccnId(testDb, accnId), "       Accession " + accnId + " should be in Q_EP_AUTO_MTCH table.");

		logger.info("*** Expected Results: - Verify that the ACCN_QUE.fk_q_typ is updated to 10 (EP Auto Match queue)");
		ArrayList<String> accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
    	Assert.assertTrue(accnQueInfoList.get(3).equals("10"), "        Accession " + accnId + " should be in Q_Typ = 10 (EP Auto Match).");

    	logger.info("*** Expected Results: - Verify that the accession is added to Q_EP_AUTO_MTCH_ERR table");
    	ArrayList<String> qEpAutoMtchErrInfoList = daoManagerPlatform.getQEPAutoMtchErrByConditon("pk_accn_id = '" + accnId + "' and pk_err_cd = " + errCdId3, testDb);
    	Assert.assertTrue(qEpAutoMtchErrInfoList.size() > 0, "        Accession ID " + accnId + " should be in Q_EP_AUTO_MTCH_ERR table.");

	}

	@Test(priority = 1, description="Process from Q_EP_UNBILLABLE and send to Outside Agency Pre Corresp queue")
	@Parameters({"ssoUsername", "ssoPassword","project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "eType", "xapEnv", "engConfigDB", "errCdId"})
	public void testPFER_605(String ssoUsername, String ssoPassword, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword,String eType, String xapEnv, String engConfigDB, String errCdId) throws Exception {
		logger.info("========== Testing - testPFER_605 ==========");

		timeStamp = new TimeStamp(driver);
		logger.info("*** Actions: - Update SUBID error code to have Prioritized Actions - Outside Agency Pre Corresp in DB");
		//Update err_cd
		ErrCd errCd = rpmDao.getErrCd(testDb, Integer.valueOf(errCdId)); //SUBID
		errCd.setOutAgncyIdPreCorresp(9); //9: FR-INS-PRE
		errCd.setOutAgncyId(2);//2: TEVIXMD
		errCd.setCorrespTypId(2);//2: Client
		errCd.setFinalActnId(7);//7: Hold
		errCd.setIsManActn(false);
		errCd.setIsAutoMtch(false);
		errCd.setIsMtchCmp(false);
		errCd.setLtrTxt("Please provide the insurance subscriber identification number.");
		rpmDao.setErrCd(testDb, errCd);

		logger.info("*** Actions: - Send WS request to create a new Priced 3rd party payor accession with SUBID error");
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());

		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("        AccnID: " + accnId);

		logger.info("*** Expected Results: - Verify that a new accession was generated");
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

		logger.info("*** Expected Results: - Verify that the accession has SUBID accn_pyr_err");
		List<List<String>> accnInfoPyrList = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
		String accnPyrId = accnInfoPyrList.get(0).get(0);

		ArrayList<String> accnPyrInfoList = daoManagerXifinRpm.getAccnPyrInfoFromACCNPYRByAccnIdPyrIdPyrPrio(accnId, accnPyrId, "1", testDb);
		Assert.assertTrue(accnPyrInfoList.get(4) == "", "        Accession ID " + accnId + " should not have a subs id.");

		String currDtStr = timeStamp.getCurrentDate();
		String errCdStr = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where abbrev = 'SUBID'", testDb).get(0);
		List<String> accnPyrErrInfoList = daoManagerXifinRpm.getAccnPyErrFromACCNPYRERRByAccnId(accnId, "1", currDtStr, errCdStr, testDb);
		Assert.assertTrue(accnPyrErrInfoList.size() > 0, "        Accession ID " + accnId + " should have SUBID accn_pyr_err.");

		logger.info("*** Expected Results: - Verify that the accession is in Q_EP_UNBILLABLE (8) queue");
		ArrayList<String> accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
		Assert.assertTrue(accnQueInfoList.get(3).equals("8"), "        Accession ID " + accnId + " should be in Q_Typ = 8 (Unbillable).");

		logger.info("*** Actions: - Wait for PF EP Workflow Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

		logger.info("*** Expected Results: - Verify that the ACCN_QUE.fk_q_typ is updated to 16 (EP Auto Match queue)");
		//Accn_que
		accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
		Assert.assertTrue(accnQueInfoList.get(3).equals("16"), "        Accession ID " + accnId + " should be in Q_Typ = 16 (EP Oustsie Agency Pre Corresp).");

		logger.info("*** Expected Results: - Verify that the accession is in Q_EP_OUT_AGNCY_PRE_CORRESP queue");
		//Q_EP_OUT_AGNCY_PRE_CORRESP
		QEp qEp = rpmDao.getQEpOutAgncyPreCorrespByAccnId(testDb, accnId);
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date currDt = new java.sql.Date(df.parse(currDtStr).getTime());
		String inDtStr = df.format(qEp.getInDt());
		Date inDt = new java.sql.Date(df.parse(inDtStr).getTime());
		assertEquals(inDt, currDt, "       Q_EP_OUT_AGNCY_PRE_CORRESP.IN_DT = " + currDt);

		logger.info("*** Expected Results: - Verify that the accession is in Q_EP_OUT_AGNCY_PRE_CORRESP_ERR table");
		//Q_EP_OUT_AGNCY_PRE_CORRESP_ERR
		QEpErr qEpErr = rpmDao.getQEpOutAgncyPreCorrespErrByAccnId(testDb, accnId);
		assertEquals(qEpErr.getErrDt(), currDt);

		logger.info("*** Actions: - Clear test data in DB");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId(accnPyrId, testDb);
		assertNotEquals(daoManagerAccnWS.deletePyrReqmntsFldByPyrAbrrev(pyrAbbrev, testDb), 0);
		assertNotEquals(daoManagerAccnWS.deletePyrReqmntsByPyrAbrrev(pyrAbbrev, testDb), 0);

		//Reset err_cd
		logger.info("*** Actions: - Reset SUBID ErrCd");
		errCd.setOutAgncyIdPreCorresp(0);
		errCd.setOutAgncyId(0);
		errCd.setCorrespTypId(2);//2: Client
		errCd.setFinalActnId(13);//13: Patient
		errCd.setIsManActn(false);
		errCd.setIsAutoMtch(true);
		errCd.setIsMtchCmp(true);
		errCd.setLtrTxt("Please provide the insurance subscriber identification number.");
		rpmDao.setErrCd(testDb, errCd);

	}

}
