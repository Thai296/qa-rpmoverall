package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.accnQue.AccnQue;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.correspFile.CorrespFile;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.rpm.domain.QEpCoresp;
import com.xifin.utils.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

public class EpWSLetterEngineTest extends SeleniumBaseTest {
	private RandomCharacter randomCharacter;	
	private ConvertUtil convertUtil;
	private TimeStamp timeStamp;
	private FileManipulation fileManipulation;

	
	@BeforeSuite(alwaysRun = true)
	    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "xapEnv", "eType", "engConfigDB", "disableBrowserPlugins"})
	    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, String xapEnv, String eType, String engConfigDB, @Optional String disableBrowserPlugins)
	    {
	        try
	        {
	            logger.info("Running BeforeSuite");
	            // Disable excess Selenium logging
	            //java.util.logging.Logger.getLogger("org.openqa.selenium.remote").setLevel(java.util.logging.Level.OFF);
	            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
	            logIntoSso(ssoUsername, ssoPassword);
	            updateSystemSetting(SystemSettingMap.SS_TIME_LIMIT_WS_CORRESP, "1", "1");
	            updateSystemSetting(SystemSettingMap.SS_EP_LTR_USE_FAX_OPTION, "0", "0");	            
	            updateSystemSetting(SystemSettingMap.SS_EP_LTR_GEN_TOA_LTRS, "0", "0");	            
	            updateSystemSetting(SystemSettingMap.SS_EP_LTR_USE_PSC_OPTION, "0", "0");	            
	            updateSystemSetting(SystemSettingMap.SS_EP_LTR_SPLIT_PULLS_BY_USER_ID, "0", "0");	            
	            updateSystemSetting(SystemSettingMap.SS_EP_LTR_DISPLAY_XREF_CLN_INFO_BLK, "0", "0");	            
	            updateSystemSetting(SystemSettingMap.SS_EP_DAYS_BEFORE_FIRST_PT_CORRESP_SEND, "0", "0");
                updateSystemSetting(SystemSettingMap.SS_CLIENT_BILLING_RULE_LOGIC_SPLIT, "False", "0");
                updateSystemSetting(SystemSettingMap.SS_PERFORM_PYR_BILLING_RULES, "False", "0");
	            updateSystemSetting(SystemSettingMap.SS_EP_LTR_DISP_TESTS_ON_CLN_LTR, "1", "1");
	            clearDataCache();
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
	    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "xapEnv", "eType", "engConfigDB", "disableBrowserPlugins"})
	    public void afterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, String xapEnv, String eType, String engConfigDB, @Optional String disableBrowserPlugins)
	    {
	        try
	        {
	            logger.info("Running AfterSuite");
	            // Disable excess Selenium logging
	            //java.util.logging.Logger.getLogger("org.openqa.selenium.remote").setLevel(java.util.logging.Level.OFF);
	            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
	            logIntoSso(ssoUsername, ssoPassword);
	            updateSystemSetting(SystemSettingMap.SS_TIME_LIMIT_WS_CORRESP, "40", "40");
	            updateSystemSetting(SystemSettingMap.SS_EP_LTR_USE_FAX_OPTION, "1", "1");
                updateSystemSetting(SystemSettingMap.SS_EP_LTR_GEN_TOA_LTRS, "1", "1");
                updateSystemSetting(SystemSettingMap.SS_EP_LTR_USE_PSC_OPTION, "1", "1");
                updateSystemSetting(SystemSettingMap.SS_EP_LTR_SPLIT_PULLS_BY_USER_ID, "1", "1");
                updateSystemSetting(SystemSettingMap.SS_EP_LTR_DISPLAY_XREF_CLN_INFO_BLK, "1", "1");
                updateSystemSetting(SystemSettingMap.SS_EP_DAYS_BEFORE_FIRST_PT_CORRESP_SEND, "1", "1");
                updateSystemSetting(SystemSettingMap.SS_EP_LTR_DISP_TESTS_ON_CLN_LTR, "0", "0");
                updateSystemSetting(SystemSettingMap.SS_CLIENT_BILLING_RULE_LOGIC_SPLIT, "True", "1");
                updateSystemSetting(SystemSettingMap.SS_PERFORM_PYR_BILLING_RULES, "True", "1");
	            clearDataCache();
	            clearDataCache();
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

	@Test(priority = 1, description="Generate Letter with Type: EP-WS")
	@Parameters({"project", "testSuite", "testCase", "formatType", "clnAbbrev"})
    public void testPFER_561(String project, String testSuite, String testCase, String formatType, String clnAbbrev) throws Exception {
		logger.info("========== Testing - testPFER_561 ==========");
		
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);
		fileManipulation = new FileManipulation(driver);

		convertUtil = new ConvertUtil();
       	List<String> listCol = new ArrayList<>();
       	List<String> listVal = new ArrayList<>();

		logger.info("*** Actions: - Set up test data in CLN table in DB");		
       	int epLetterSubSvcSeqId = submissionDao.getSubmSvcByAbbrevWithDelvryTypWS("EP-WS").getSubmSvcSeqId();
       	Cln clnInfoList = clientDao.getClnByClnAbbrevWithNoPullEpDt(clnAbbrev);
        int clnId = clnInfoList.getClnId();
        
        Cln cln = clientDao.getClnByClnAbbrev(clnAbbrev);
        cln.setEpLetterSubmSvcId(epLetterSubSvcSeqId);
        cln.setIsEpLtrListMon(true);
        cln.setIsEpLtrListTues(true);
        cln.setIsEpLtrListWed(true);
        cln.setIsEpLtrListThurs(true);
        cln.setIsEpLtrListFri(true);
        cln.setIsEpLtrListSat(true);
        cln.setIsEpLtrListSun(true);
        clientDao.setCln(cln);
        clientDao.setCln(cln);

        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("        AccnID: " + accnId);

        logger.info("*** Step 1 Expected Results: - Verify that a new Accession was generated");
        Assert.assertNotNull(accnId, "        Did not generate a new Accession.");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));

        logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Actions: - Set up test data in ACCN tables in DB");
        Accn accn = accessionDao.getAccn(accnId);
        int orgClnId = accn.getClnId();
        accn.setClnId(clnId);
        accessionDao.setAccn(accn);

        logger.info("*** Actions: - Set up test data in ACCN_PYR_ERR table in DB");		
        int errCd = 72268;//Choose err_cd.abbrv = 'CR11', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");
       
        AccnPyrErr ape = new AccnPyrErr();
        int errSeqId = daoManagerPlatform.getNextvalFromDual("ACCN_PYR_ERR_SEQ", testDb);
        ape.setErrSeq(errSeqId);
        ape.setAccnId(accnId);
        ape.setPyrPrio(1);
        ape.setErrCd(errCd);
        ape.setErrDt(new java.sql.Date(sysDt.getTime()));
        ape.setIsPosted(true);
        accessionDao.setAccnPyrErr(ape);
		
        logger.info("*** Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, String.valueOf(errCd), testDb);
        assertTrue(epLetterVList.size() > 0,"        A record should show in EP_LETTER_V.");        
 
        logger.info("*** Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);
        Date todayDt = convertUtil.convertStringToUtilDate(timeStamp.getCurrentDate(), "MM/dd/yyyy");
        
        Accn accnInfoList = accessionDao.getAccn(accnId);
        
//        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = accnInfoList.getDos();

        listCol.add("PK_ACCN_ID");
        listCol.add("FK_USER_ID");
        listCol.add("FK_ERR_GRP_ACCN");
        listCol.add("FK_CLN_ID");
        listCol.add("IN_DT");
        listCol.add("ASSGN_DT");
        listCol.add("DOS_DT"); 
        listCol.add("DUE_AMT_FROM_ERRORS");        
        listCol.add("LTR_CNT");        
        listCol.add("LTR_DEST");
        listCol.add("NXT_LTR_DT");      
        
        List<Object> insertValues = new ArrayList<>();
        insertValues.add(accnId);
        insertValues.add("qatester");
        insertValues.add("1");
        insertValues.add(clnId);        
        insertValues.add(todayDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);        
        insertValues.add("0");
        insertValues.add("2");
        insertValues.add(prevDt);           
        
        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);
        
        logger.info("*** Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0,"        A new record should be added into Q_EP_CORRESP table.");        
        
        logger.info("*** Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        int accnProcSeqId = accessionDao.getAccnProcByAccnId(accnId).getAccnProcSeqId();
        int pyrId = accessionDao.getAccnPyrs(accnId).get(0).getPyrId();
        String pyrAbbrev = payorDao.getPyrByAccnIdAndPyrPrio(accnId, 1).getPyrAbbrv();
        int pyrGrpId = payorDao.getPyrByPyrAbbrev(pyrAbbrev).getPyrGrpId();
        int errGrpId = errorProcessingDao.getErrCdByErrCdId(errCd).getErrGrpId();
        
        listCol.clear();
        listCol.add("PK_ACCN_ID");        
        listCol.add("PK_ERR_CD");
        listCol.add("PK_PYR_PRIO");
        
        listCol.add("FK_PYR_ID");        
        listCol.add("FK_PYR_GRP_ID");
        listCol.add("FK_ERR_GRP_ID");
        
        listCol.add("PK_PSEUDO_ACCN_PROC_SEQ_ID");
        listCol.add("ERR_DT");
        listCol.add("DOS_DT");
        listCol.add("DUE_AMT_BY_ERROR");
        listCol.add("B_RELEVANT");
        
        insertValues.clear();
        insertValues.add(accnId);
        insertValues.add(errCd);
        insertValues.add("1");        
        insertValues.add(pyrId);
        insertValues.add(pyrGrpId);
        insertValues.add(errGrpId);        
        insertValues.add(accnProcSeqId);
        insertValues.add(sysDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        
        int qEpCorrespErrCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP_ERR", listCol, insertValues, testDb);
        
        logger.info("*** Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0,"        A new record should be added into Q_EP_CORRESP_ERR table.");       
        
        logger.info("*** Actions: - Set up test data in ACCN_QUE table in DB");        
        AccnQue accnQueInfoList = accessionDao.getCurrentAccnQue(accnId);
        int orgQCnt = accnQueInfoList.getQCnt();
        int orgQTyp = accnQueInfoList.getQTyp();
        String orgAudUser = accnQueInfoList.getAudUser();
        
        listCol.clear();
        listCol.add("FK_Q_TYP");

        listVal.add("13"); //EP Correspondence
        
        logger.info("*** Expected Results: - Verify that a record is updated in ACCN_QUE table in DB");
        int accnQueCount = daoManagerPlatform.updateRowInTable("ACCN_QUE", listCol, listVal, "pk_accn_id = '" + accnId + "' and pk_q_cnt = " + orgQCnt, testDb);
        assertTrue(accnQueCount > 0,"        A record should be updated into ACCN_QUE table.");

        daoManagerXifinRpm.deleteDataFromQValidationAccnByAccnID(accnId, testDb);

        logger.info("*** Actions: - Wait for PF-EP WS Letter Engine");
        Assert.assertTrue(hasLetterEngineProcessed(accnId, QUEUE_WAIT_TIME_MS), "Accn not yet processed by EP WS Letter Engine");

        logger.info("*** Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
		int correspFileSeqId = errorProcessingDao.getEpCorrespHistByAccnIdOrderByFileSeq(accnId).getCorrespFileSeq();
		CorrespFile correspFileInfoList = correspFileDao.getCorrespFileByFileSeq(correspFileSeqId);
		assertNotNull(correspFileInfoList,"        A new record should be added into CORRESP_FILE table.");
		
		String fileName = correspFileInfoList.getFileName().trim();
		
		logger.info("*** Expected Results: - Verify that a Client EP Letter is generated under /epClient/out/ folder");
		assertTrue(fileName.contains("Client_" + timeStamp.getCurrentDate("yyyyMMdd")),"        An EP WS Letter is generated.");
		
		String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
		File fOutput = new File(filePathOut + fileName);
		//System.out.println("File Path = " + filePathOut + fileName);//Debug Info	
		Assert.assertTrue(isFileExists(fOutput, 5), "        EP WS file: " + fileName + " should be generated under " + filePathOut + " folder.");
				
		logger.info("*** Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
		QEpCoresp qEpCorrespInfoList = errorProcessingDao.getQEpCorrespByAccnId(accnId);
		assertEquals(qEpCorrespInfoList.getLtrCnt(), 1, "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");
		
		logger.info("*** Expected Results: - Verify that the PDF generated contains proper data");
		String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut+fileName);
		assertTrue(pdfContentsStr.contains("EP-WS"),"        PDF file " + fileName + " should contains EP-WS as banner.");	
		
        logger.info("*** Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);        
        
        //Reset the cln id in accn table back to original cln id       
        accn.setClnId(orgClnId);
        accessionDao.setAccn(accn);
        
        accessionDao.deleteAccnPyrErrByAccnIdErrCd(accnId, errCd);
        rpmDao.deleteEpCorrespHistByCorrespFileSeq(testDb, correspFileSeqId);
        rpmDao.deleteCorrespFileByFileSeq(testDb, correspFileSeqId);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '"+accnId+"'", testDb);        
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '"+accnId+"'", testDb);            
      
        //Reset the q_typ in accn_que table back to the original q_typ and out_dt back to null
        listCol.clear();
        listCol.add("FK_Q_TYP");
        listCol.add("OUT_DT");
        listCol.add("AUD_USER");
        
        listVal.clear();
        listVal.add(String.valueOf(orgQTyp));
        listVal.add("''");
        listVal.add("'" + orgAudUser + "'");
        
        accnQueCount = daoManagerPlatform.updateRowInTable("ACCN_QUE", listCol, listVal, "pk_accn_id = '" + accnId + "' and pk_q_cnt = " + orgQCnt, testDb);
        assertTrue(accnQueCount > 0,"        A record should be updated into ACCN_QUE table."); 	
	}

    /**
     * Check to see if the EP WSW letter engine has processed accn.
     * @param accnId accn Id
     * @param maxTime max time
     * @return ltr count increases by 1
     * @throws Exception throws exception
     */
    private boolean hasLetterEngineProcessed(String accnId, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        int ltrCnt = rpmDao.getCorrespLtrCntByAccnId(accnId);
        while ( (ltrCnt==0) && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for EP WS Letter Engine to process accn, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            ltrCnt = rpmDao.getCorrespLtrCntByAccnId(accnId);
        }
        return (ltrCnt==1);
    }

	
}
