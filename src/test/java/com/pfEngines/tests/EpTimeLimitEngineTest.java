package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.errCd.ErrCd;
import com.mbasys.mars.ejb.entity.errCdOverride.ErrCdOverride;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.ConvertUtil;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
import com.xifin.xap.utils.XifinAdminUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.assertNotNull;


public class EpTimeLimitEngineTest extends SeleniumBaseTest {
	private TimeStamp timeStamp;	

	
	@BeforeSuite(alwaysRun = true)
	    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "xapEnv", "engConfigDB", "errCdId", "disableBrowserPlugins"})
	    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, String xapEnv, String engConfigDB, int errCdId, @Optional String disableBrowserPlugins)
	    {
	        try
	        {
	            logger.info("Running BeforeSuite");
	            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
	            logIntoSso(ssoUsername, ssoPassword);
	            //Update system_setting
	            updateSystemSetting(SystemSettingMap.SS_EP_TL_CHECK_QUEUES, "1", "1");
	            updateSystemSetting(SystemSettingMap.SS_TIME_LIMIT_AUTO_MTCH, "1", "1");
	            updateSystemSetting(SystemSettingMap.SS_TIME_LIMIT_MTCH_CMP, "1", "1");
	            updateSystemSetting(SystemSettingMap.SS_TIME_LIMIT_MANUAL, "1", "1");
	            updateSystemSetting(SystemSettingMap.SS_TIME_LIMIT_CORRESP, "1", "1");
	            updateSystemSetting(SystemSettingMap.SS_TIME_LIMIT_AGENCY, "1", "1");	            
	            updateSystemSetting(SystemSettingMap.SS_TIME_LIMIT_WS_CORRESP, "1", "1");
				updateSystemSetting(SystemSettingMap.SS_TIME_LIMIT_AGENCY_PRE_CORRESP, "1", "1");
	            //Update q_ep queues so the EP Time Limit Engine will only process specified accn	            
	            TimeStamp timeStamp = new TimeStamp(driver);
	            ConvertUtil convertUtil = new ConvertUtil();
	            Date currDt = convertUtil.convertStringToUtilDate(timeStamp.getCurrentDate(), "MM/dd/yyyy");
	            rpmDao.updateQEpAutoMtchInDt(testDb, new java.sql.Date(currDt.getTime()));
	            rpmDao.updateQEpMtchCmpInDt(testDb, new java.sql.Date(currDt.getTime()));	            
	            rpmDao.updateQEpManInDt(testDb, new java.sql.Date(currDt.getTime()));
	            rpmDao.updateQEpCorrspInDt(testDb, new java.sql.Date(currDt.getTime()));
	            rpmDao.updateQEpOutAgncyInDt(testDb, new java.sql.Date(currDt.getTime()));
				rpmDao.updateQEpOutAgncyPreCorrespInDt(testDb, new java.sql.Date(currDt.getTime()));
	            //Update err_cd 
	          	ErrCd errCd = rpmDao.getErrCd(testDb, Integer.valueOf(errCdId)); //SUBID
				errCd.setOutAgncyIdPreCorresp(9);//9: FR-INS-PRE
	           	errCd.setOutAgncyId(2);//2: FR-ADD
	           	errCd.setCorrespTypId(2);//2: Client
	           	errCd.setFinalActnId(7);//7: Hold
	           	errCd.setIsManActn(true);
	           	errCd.setIsAutoMtch(true);
	           	errCd.setIsMtchCmp(true);
	           	errCd.setLtrTxt("Please provide the insurance subscriber identification number.");	
	           	rpmDao.setErrCd(testDb, errCd);

	           	//Update Err Cd override
				List<ErrCdOverride> errCdOverrides = rpmDao.getErrCdOverrideByErrCd(testDb, errCdId);
				for(ErrCdOverride errCdOverride: errCdOverrides){
					if(errCdOverride.getPyrGrpId() >0){
						errCdOverride.setOutAgncyIdPreCorresp(9);
						errCdOverride.setOutAgncyId(2);
						errCdOverride.setCorrespTypId(2);
						errCdOverride.setLtrTxt("Test");
						errCdOverride.setFinalActnTyp(7);//7: Hold
						rpmDao.setErrCdOverride(testDb,errCdOverride);
					}
				}


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
	    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "xapEnv", "engConfigDB", "errCdId", "disableBrowserPlugins"})
	    public void afterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, String xapEnv, String engConfigDB, int errCdId, @Optional String disableBrowserPlugins)
	    {
	        try
	        {
	            logger.info("Running AfterSuite");	
	            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
	            logIntoSso(ssoUsername, ssoPassword);
	            //Reset system_setting
	            updateSystemSetting(SystemSettingMap.SS_EP_TL_CHECK_QUEUES, "1", "1");
	            updateSystemSetting(SystemSettingMap.SS_TIME_LIMIT_AUTO_MTCH, "100", "100");
	            updateSystemSetting(SystemSettingMap.SS_TIME_LIMIT_MTCH_CMP, "100", "100");
	            updateSystemSetting(SystemSettingMap.SS_TIME_LIMIT_MANUAL, "100", "100");
	            updateSystemSetting(SystemSettingMap.SS_TIME_LIMIT_CORRESP, "400", "400");
	            updateSystemSetting(SystemSettingMap.SS_TIME_LIMIT_AGENCY, "450", "450");
	            updateSystemSetting(SystemSettingMap.SS_TIME_LIMIT_WS_CORRESP, "400", "400");
				updateSystemSetting(SystemSettingMap.SS_TIME_LIMIT_AGENCY_PRE_CORRESP, "450", "450");
	            //Reset err_cd 
	          	ErrCd errCd = rpmDao.getErrCd(testDb, Integer.valueOf(errCdId)); //SUBID
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
	 
	 
	 

	@Test(priority = 1, description="The accn is moved to the next queue based on the prioritized actions")
	@Parameters({"ssoUsername", "ssoPassword","project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "eType1", "eType2", "xapEnv", "engConfigDB"})
	public void testPFER_564(String ssoUsername, String ssoPassword, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword,String eType1, String eType2, String xapEnv, String engConfigDB) throws Exception {
		logger.info("========== Testing - testPFER_564 ==========");

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
    	
    	logger.info("*** Expected Results: - Verify that the accession has SUBID accn_pyr_err and is in Unbillable queue (8)");
    	List<List<String>> accnInfoPyrList = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb);
    	String accnPyrId = accnInfoPyrList.get(0).get(0);

    	ArrayList<String> accnPyrInfoList = daoManagerXifinRpm.getAccnPyrInfoFromACCNPYRByAccnIdPyrIdPyrPrio(accnId, accnPyrId, "1", testDb);
    	Assert.assertTrue(accnPyrInfoList.get(4) == "", "        Copyied Accession ID " + accnId + " should not have a subs id.");

    	String currDt = timeStamp.getCurrentDate();
    	String errCd = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where abbrev = 'SUBID'", testDb).get(0);
    	List<String> accnPyrErrInfoList = daoManagerXifinRpm.getAccnPyErrFromACCNPYRERRByAccnId(accnId, "1", currDt, errCd, testDb);
    	Assert.assertTrue(accnPyrErrInfoList.size() > 0, "        Copyied Accession ID " + accnId + " should have SUBID accn_pyr_err.");
    	
		logger.info("*** Actions: - Wait for PF EP Workflow Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

    	logger.info("*** Expected Results: - Verify that the accession is added to Q_EP_AUTO_MTCH table");
    	assertNotNull(rpmDao.getQEpAutoMtchByAccnId(testDb, accnId), "       Accession " + accnId + " should be in Q_EP_AUTO_MTCH table.");
		
		logger.info("*** Expected Results: - Verify that the ACCN_QUE.fk_q_typ is updated to 10 (EP Auto Match queue)");
		ArrayList<String> accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null order by pk_q_cnt desc", testDb);
    	Assert.assertTrue(accnQueInfoList.get(3).equals("10"), "        Accession " + accnId + " should be in Q_Typ = 10 (EP Auto Match).");
				
    	logger.info("*** Expected Results: - Verify that the accession is added to Q_EP_AUTO_MTCH_ERR table");
    	ArrayList<String> qEpAutoMtchErrInfoList = daoManagerPlatform.getQEPAutoMtchErrByConditon("pk_accn_id = '" + accnId + "' and pk_err_cd = " + errCd, testDb);
    	Assert.assertTrue(qEpAutoMtchErrInfoList.size() > 0, "        Copyied Accession ID " + accnId + " should be in Q_EP_AUTO_MTCH_ERR table.");
    	
    	logger.info("*** Actions: - Update Q_EP_AUTO_MTCH.in_dt for the selected accession");
    	SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
    	String prevDtStr = timeStamp.getPreviousDate("MM/dd/yyyy", 12);
    	java.sql.Date prevDt = new java.sql.Date(format.parse(prevDtStr).getTime());
    	rpmDao.updateQEpAutoMtchInDtByAccnId(testDb, prevDt, accnId);

    	logger.info("*** Actions: - Wait for PF EP Time Limit Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_AUTO_MTCH, QUEUE_WAIT_TIME_MS));
    	
       	logger.info("*** Expected Results: - Verify that the accession is in Q_EP_MTCH_CMP queue");
       	assertNotNull(rpmDao.getQEpMtchCmpByAccnId(testDb, accnId), "       Accession " + accnId + " should be in Q_EP_MTCH_CMP table.");

    	logger.info("*** Actions: - Update Q_EP_MTCH_CMP.in_dt for the selected accession");
    	rpmDao.updateQEpMtchCmpInDtByAccnId(testDb, prevDt, accnId);

    	logger.info("*** Actions: - Wait for PF EP Time Limit Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_MTCH_CMP, QUEUE_WAIT_TIME_MS));

    	logger.info("*** Expected Results: - Verify that the accession is in Q_EP_MAN queue");
    	assertNotNull(rpmDao.getQEpManByAccnId(testDb, accnId), "       Accession " + accnId + " should be in Q_EP_MAN table.");

    	logger.info("*** Actions: - Update Q_EP_MAN.in_dt for the selected accession");
    	rpmDao.updateQEpManInDtByAccnId(testDb, prevDt, accnId);

    	logger.info("*** Actions: - Wait for PF EP Time Limit Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_MAN, QUEUE_WAIT_TIME_MS));

		logger.info("*** Expected Results: - Verify that the accession is in Q_EP_OUT_AGNCY_PRE_CORRESP queue");
		assertNotNull(rpmDao.getQEpOutAgncyPreCorrespByAccnId(testDb, accnId), "       Accession " + accnId + " should be in Q_EP_OUT_AGNCY_PRE_CORRESP table.");

		logger.info("*** Actions: - Update Q_EP_OUT_AGNCY_PRE_CORRESP.in_dt for the selected accession");
		rpmDao.updateQEpOutAgncyPreCorrespInDtByAccnId(testDb, prevDt, accnId);

		logger.info("*** Actions: - Wait for PF EP Time Limit Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_OUT_AGNCY_PRE_CORRESP, QUEUE_WAIT_TIME_MS));

    	logger.info("*** Expected Results: - Verify that the accession is in Q_EP_CORRESP queue");
    	assertNotNull(rpmDao.getQEpCorrespByAccnId(testDb, accnId), "       Accession " + accnId + " should be in Q_EP_CORRESP table.");

    	logger.info("*** Actions: - Update Q_EP_CORRESP.in_dt for the selected accession");
    	rpmDao.updateQEpCorrespInDtByAccnId(testDb, prevDt, accnId);

    	logger.info("*** Actions: - Wait for PF EP Time Limit Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_CORRESP, QUEUE_WAIT_TIME_MS));

    	logger.info("*** Expected Results: - Verify that the accession is in Q_EP_OUT_AGNCY queue");
    	assertNotNull(rpmDao.getQEpOutAgncyByAccnId(testDb, accnId), "       Accession " + accnId + " should be in Q_EP_OUT_AGNCY table.");

    	logger.info("*** Actions: - Update Q_EP_OUT_AGNCY.in_dt for the selected accession");
    	rpmDao.updateQEpOutAgncyInDtByAccnId(testDb, prevDt, accnId);

    	logger.info("*** Actions: - Wait for PF EP Time Limit Engine");
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_OUT_AGNCY, QUEUE_WAIT_TIME_MS));

    	logger.info("*** Expected Results: - Verify that the accession is in Q_EP_HLD queue");
    	assertNotNull(rpmDao.getQEpHldByAccnId(testDb, accnId), "       Accession " + accnId + " should be in Q_EP_HLD table.");
	}
	
}
