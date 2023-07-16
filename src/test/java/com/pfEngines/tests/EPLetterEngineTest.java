package com.pfEngines.tests;

import com.mbasys.common.utility.Money;
import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnClnQ.AccnClnQ;
import com.mbasys.mars.ejb.entity.accnPhys.AccnPhys;
import com.mbasys.mars.ejb.entity.accnProc.AccnProc;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.accnQue.AccnQue;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.clnXref.ClnXref;
import com.mbasys.mars.ejb.entity.correspFile.CorrespFile;
import com.mbasys.mars.ejb.entity.epCorrespHist.EpCorrespHist;
import com.mbasys.mars.ejb.entity.errCd.ErrCd;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.xref.Xref;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.qa.dao.rpm.domain.QEpCoresp;
import com.xifin.qa.dao.rpm.domain.QEpCorrespErr;
import com.xifin.utils.*;
import com.xifin.xap.utils.XifinAdminUtils;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.*;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

public class EPLetterEngineTest extends SeleniumBaseTest {
    private RandomCharacter randomCharacter;
    private ConvertUtil convertUtil;
    private TimeStamp timeStamp;
    private FileManipulation fileManipulation;
    private XifinAdminUtils xifinAdminUtils;

    @BeforeSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "disableBrowserPlugins"})
    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, @Optional String disableBrowserPlugins) {
        try {
            logger.info("Running BeforeSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            updateSystemSetting(SystemSettingMap.SS_RELEASE_Q_OE_ACCN, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_Q_OE_MNL_LAB_MSGS, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_OEPOSTING_CLIENT_ID_BASED_ON_ENCOUNTER_FACILITY, "False", "0");

        } catch (Exception e) {
            Assert.fail("Error running BeforeSuite", e);
        } finally {
            quitWebDriver();
        }
    }

    @AfterMethod(alwaysRun = true)
    @Parameters({"clnAbbrev1", "clnAbbrev2", "clnAbbrev3"})
    public void afterMethod(String clnAbbrev1, String clnAbbrev2, String clnAbbrev3) {
        try {
            logger.info("Running AfterMethod");
            cleanUpCln(Arrays.asList(clnAbbrev1, clnAbbrev2, clnAbbrev3));
        } catch (Exception e) {
            Assert.fail("Error running AfterMethod", e);
        } finally {
            quitWebDriver();
        }
    }

    @Test(priority = 1, description = "Generate Letter with Type: PATIENT")
    @Parameters({"formatType", "clnAbbrev1", "project", "testSuite", "testCase"})
    public void testPFER_501(String formatType, String clnAbbrev, String project, String testSuite, String testCase) throws Exception {
        logger.info("========== Testing - testPFER_501 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 0, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_USE_PSC_OPTION, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_USE_FAX_OPTION, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_SPLIT_PULLS_BY_USER_ID, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_DAYS_BEFORE_FIRST_PT_CORRESP_SEND, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_GEN_TOA_LTRS, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_DISP_TESTS_ON_CLN_LTR, "1", "1");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_DISPLAY_XREF_CLN_INFO_BLK, "0", "0");

        logger.info("*** Step 2 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id not in (3,10,8) and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        Cln cln = clientDao.getClnByClnAbbrev(clnAbbrev);
        int clnId = cln.getClnId();
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        cln.setEpLetterSubmSvcId(Integer.parseInt(epLetterSubSvcSeqId));
        cln.setIsEpLtrListMon(true);
        cln.setIsEpLtrListTues(true);
        cln.setIsEpLtrListWed(true);
        cln.setIsEpLtrListThurs(true);
        cln.setIsEpLtrListFri(true);
        cln.setIsEpLtrListSat(true);
        cln.setIsEpLtrListSun(true);
        clientDao.setCln(cln);

        logger.info("*** Step 3 Actions: - Create a new Priced Accession via Accession WS");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        Assert.assertNotNull(accnId);

        Accn accn = accessionDao.getAccn(accnId);
        accn.setClnId(clnId);
        accessionDao.setAccn(accn);

        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS), "Accession is not out of Eligibility Queue");
        accessionDao.deleteUnfixedAccnErrByAccnId(accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNPRICEABLE, QUEUE_WAIT_TIME_MS));
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS), "Accession is not out of Pricing Queue");

        logger.info("*** Step 3 Expected Results: - Verify that the accession is priced");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession status is not Priced");

        logger.info("*** Step 4 Actions: - Delete accession from Q_EP_CORRESP_ERR, Q_EP_CORRESP and ACCN_CLN_Q tables");
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q table in DB");
        AccnClnQ accnClnQ = new AccnClnQ();
        accnClnQ.setAccnId(accnId);
        accnClnQ.setQuestnId(14);
        accnClnQ.setRespns(randomCharacter.getRandomAlphaNumericString(10));
        accnClnQ.setIsDeleted(false);
        accessionDao.setAccnClnQ(accnClnQ);

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        Date currentDt = convertUtil.convertStringToUtilDate(timeStamp.getCurrentDate(), "MM/dd/yyyy");

        int errSeqId = databaseSequenceDao.getNextSequenceFromOracle("ACCN_PYR_ERR_SEQ");
        AccnPyrErr accnPyrErr = new AccnPyrErr();
        accnPyrErr.setAccnId(accnId);
        accnPyrErr.setErrSeq(errSeqId);
        accnPyrErr.setErrCd(55342);
        accnPyrErr.setPyrPrio(1);
        accnPyrErr.setErrDt(new java.sql.Date(currentDt.getTime()));
        accnPyrErr.setIsPosted(true);
        accessionDao.setAccnPyrErr(accnPyrErr);

        logger.info("*** Step 5 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);

        int seqId = databaseSequenceDao.getNextSequenceFromOracle("ACCN_PHYS_SEQ");
        AccnPhys accnPhys = new AccnPhys();
        accnPhys.setSeqId(seqId);
        accnPhys.setAccnId(accnId);
        accnPhys.setPhysSeqId(Integer.parseInt(physSeqId));
        accnPhys.setAccnPhysTypId(1);
        accnPhys.setAccnTestSeqId(0);
        accessionDao.setAccnPhys(accnPhys);

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);
        com.mbasys.common.utility.Money dueAmt = new Money(Float.parseFloat(randomNum));

        QEpCoresp qEpCoresp = new QEpCoresp();
        qEpCoresp.setAccnId(accnId);
        qEpCoresp.setUserId("qatester");
        qEpCoresp.setErrGrpAccn(1);
        qEpCoresp.setClnId(clnId);
        qEpCoresp.setInDt(new java.sql.Timestamp(accn.getDos().getTime()));
        qEpCoresp.setAssgnDt(accn.getDos());
        qEpCoresp.setDosDt(accn.getDos());
        qEpCoresp.setDueAmtFromErrors(dueAmt);
        qEpCoresp.setLtrCnt(0);
        qEpCoresp.setLtrDest(1);
        qEpCoresp.setNxtLtrDt(new java.sql.Date(currentDt.getTime()));
        errorProcessingDao.setQEpCorresp(qEpCoresp);

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        AccnProc accnProc = accessionDao.getAccnProcByAccnId(accnId);
        logger.info(accnProc);
        Pyr pyr = payorDao.getPyrByPyrId(accessionDao.getAccnPyrByAccnIdAndPyrPrio(accnId, 1).getPyrId());
        ErrCd errCd1 = errorProcessingDao.getErrCdByErrCdId(Integer.parseInt(errCd));

        QEpCorrespErr qEpCorrespErr = new QEpCorrespErr();
        qEpCorrespErr.setAccnId(accnId);
        qEpCorrespErr.setErrCd(errCd1.getErrCd());
        qEpCorrespErr.setPyrPrio(1);
        qEpCorrespErr.setPyrId(pyr.getPyrId());
        qEpCorrespErr.setPyrGrpId(pyr.getPyrGrpId());
        qEpCorrespErr.setErrGrpId(errCd1.getErrGrpId());
        qEpCorrespErr.setPseudoAccnProcSeqId(accnProc.getAccnProcSeqId());
        qEpCorrespErr.setErrDt(new java.sql.Date(currentDt.getTime()));
        qEpCorrespErr.setDosDt(accn.getDos());
        qEpCorrespErr.setDueAmtByError(dueAmt);
        qEpCorrespErr.setRelevant(false);
        errorProcessingDao.setQEpCorrespErr(qEpCorrespErr);

        logger.info("*** Step 9.0 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 9.2 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        List<EpCorrespHist> epCorrespHistList = errorProcessingDao.getEpCorrespHistByAccnId(accnId);
        EpCorrespHist latestEpCorrespHist = epCorrespHistList.get(0);
        CorrespFile correspFile = correspFileDao.getCorrespFileByFileSeq(latestEpCorrespHist.getCorrespFileSeq());
        assertNotNull(correspFile, "A new record should be added into CORRESP_FILE table.");
        String fileName = correspFile.getFileName().trim();

        logger.info("*** Step 9.3 Expected Results: - Verify that a Patient EP Letter is generated under /epPatient/out/ folder");
        assertTrue(fileName.contains("Patient"), "        a Patient EP Letter is generated.");
        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP Patient file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 9.4 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Patient Correspondence"), "        PDF file " + fileName + " should contains 'Patient Correspondence' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear Patient"), "        PDF file " + fileName + " should contains 'Dear Patient' in Letter.");

        logger.info("*** Step 10 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");

        //Reset the cln id in accn table back to original cln id
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnExpDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        clientDao.setCln(cln);

        accnPyrErr.setResultCode(ErrorCodeMap.DELETED_RECORD);
        accessionDao.setAccnPyrErr(accnPyrErr);

        accnPhys.setResultCode(ErrorCodeMap.DELETED_RECORD);
        accessionDao.setAccnPhys(accnPhys);

        latestEpCorrespHist.setResultCode(ErrorCodeMap.DELETED_RECORD);
        errorProcessingDao.setEpCorrespHist(latestEpCorrespHist);

        correspFile.setResultCode(ErrorCodeMap.DELETED_RECORD);
        correspFileDao.setCorrespFile(correspFile);

        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
    }

    @Test(priority = 1, description = "Generate Letter with Type: PATIENTPULL")
    @Parameters({"formatType", "clnAbbrev2", "project", "testSuite", "testCase"})
    public void testPFER_502(String formatType, String clnAbbrev, String project, String testSuite, String testCase) throws Exception {
        logger.info("========== Testing - testPFER_502 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 0, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_USE_PSC_OPTION, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_USE_FAX_OPTION, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_SPLIT_PULLS_BY_USER_ID, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_DAYS_BEFORE_FIRST_PT_CORRESP_SEND, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_GEN_TOA_LTRS, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_DISP_TESTS_ON_CLN_LTR, "1", "1");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_DISPLAY_XREF_CLN_INFO_BLK, "0", "0");

        logger.info("*** Step 2 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id not in (3,10,8) and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        String currDt = timeStamp.getCurrentDate();
        Cln cln = clientDao.getClnByClnAbbrev(clnAbbrev);
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        int clnId = cln.getClnId();
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(new java.sql.Date(prevDt.getTime()));
        cln.setPullEpPtExpDt(null);
        cln.setEpLetterSubmSvcId(Integer.parseInt(epLetterSubSvcSeqId));
        cln.setIsEpLtrListMon(true);
        cln.setIsEpLtrListTues(true);
        cln.setIsEpLtrListWed(true);
        cln.setIsEpLtrListThurs(true);
        cln.setIsEpLtrListFri(true);
        cln.setIsEpLtrListSat(true);
        cln.setIsEpLtrListSun(true);
        clientDao.setCln(cln);

        logger.info("*** Step 3 Actions: - Create a new Priced Accession via Accession WS");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        Assert.assertNotNull(accnId);
        Accn accn = accessionDao.getAccn(accnId);
        accn.setClnId(clnId);
        accessionDao.setAccn(accn);

        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS), "Accession is not out of Eligibility Queue");
        accessionDao.deleteUnfixedAccnErrByAccnId(accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNPRICEABLE, QUEUE_WAIT_TIME_MS));
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS), "Accession is not out of Pricing Queue");

        logger.info("*** Step 3 Expected Results: - Verify that the accession is priced");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession status is not Priced");

        logger.info("*** Step 4 Actions: - Delete accession from Q_EP_CORRESP_ERR, Q_EP_CORRESP and ACCN_CLN_Q tables");
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        AccnClnQ accnClnQ = new AccnClnQ();
        accnClnQ.setAccnId(accnId);
        accnClnQ.setQuestnId(14);
        accnClnQ.setRespns(randomCharacter.getRandomAlphaNumericString(10));
        accnClnQ.setIsDeleted(false);
        accessionDao.setAccnClnQ(accnClnQ);

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        Date currentDt = convertUtil.convertStringToUtilDate(timeStamp.getCurrentDate(), "MM/dd/yyyy");

        int errSeqId = databaseSequenceDao.getNextSequenceFromOracle("ACCN_PYR_ERR_SEQ");
        AccnPyrErr accnPyrErr = new AccnPyrErr();
        accnPyrErr.setAccnId(accnId);
        accnPyrErr.setErrSeq(errSeqId);
        accnPyrErr.setErrCd(55342);
        accnPyrErr.setPyrPrio(1);
        accnPyrErr.setErrDt(new java.sql.Date(currentDt.getTime()));
        accnPyrErr.setIsPosted(true);
        accessionDao.setAccnPyrErr(accnPyrErr);

        logger.info("*** Step 5.4 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);

        int seqId = databaseSequenceDao.getNextSequenceFromOracle("ACCN_PHYS_SEQ");
        AccnPhys accnPhys = new AccnPhys();
        accnPhys.setSeqId(seqId);
        accnPhys.setAccnId(accnId);
        accnPhys.setPhysSeqId(Integer.parseInt(physSeqId));
        accnPhys.setAccnPhysTypId(1);
        accnPhys.setAccnTestSeqId(0);
        accessionDao.setAccnPhys(accnPhys);

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);
        com.mbasys.common.utility.Money dueAmt = new Money(Float.parseFloat(randomNum));
        QEpCoresp qEpCoresp = new QEpCoresp();
        qEpCoresp.setAccnId(accnId);
        qEpCoresp.setUserId("qatester");
        qEpCoresp.setErrGrpAccn(1);
        qEpCoresp.setClnId(clnId);
        qEpCoresp.setInDt(new java.sql.Timestamp(accn.getDos().getTime()));
        qEpCoresp.setAssgnDt(accn.getDos());
        qEpCoresp.setDosDt(accn.getDos());
        qEpCoresp.setDueAmtFromErrors(dueAmt);
        qEpCoresp.setLtrCnt(0);
        qEpCoresp.setLtrDest(1);
        qEpCoresp.setNxtLtrDt(new java.sql.Date(currentDt.getTime()));
        errorProcessingDao.setQEpCorresp(qEpCoresp);

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        AccnProc accnProc = accessionDao.getAccnProcByAccnId(accnId);
        Pyr pyr = payorDao.getPyrByPyrId(accessionDao.getAccnPyrByAccnIdAndPyrPrio(accnId, 1).getPyrId());
        ErrCd errCd1 = errorProcessingDao.getErrCdByErrCdId(Integer.parseInt(errCd));

        QEpCorrespErr qEpCorrespErr = new QEpCorrespErr();
        qEpCorrespErr.setAccnId(accnId);
        qEpCorrespErr.setErrCd(errCd1.getErrCd());
        qEpCorrespErr.setPyrPrio(1);
        qEpCorrespErr.setPyrId(pyr.getPyrId());
        qEpCorrespErr.setPyrGrpId(pyr.getPyrGrpId());
        qEpCorrespErr.setErrGrpId(errCd1.getErrGrpId());
        qEpCorrespErr.setPseudoAccnProcSeqId(accnProc.getAccnProcSeqId());
        qEpCorrespErr.setErrDt(new java.sql.Date(currentDt.getTime()));
        qEpCorrespErr.setDosDt(accn.getDos());
        qEpCorrespErr.setDueAmtByError(dueAmt);
        qEpCorrespErr.setRelevant(false);
        errorProcessingDao.setQEpCorrespErr(qEpCorrespErr);

        logger.info("*** Step 9.0 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 9.2 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        List<EpCorrespHist> epCorrespHistList = errorProcessingDao.getEpCorrespHistByAccnId(accnId);
        EpCorrespHist latestEpCorrespHist = epCorrespHistList.get(0);
        CorrespFile correspFile = correspFileDao.getCorrespFileByFileSeq(latestEpCorrespHist.getCorrespFileSeq());
        assertNotNull(correspFile, "A new record should be added into CORRESP_FILE table.");
        String fileName = correspFile.getFileName().trim();

        logger.info("*** Step 9.4 Expected Results: - Verify that a EP Patient Pull Letter is generated under /epPatient/out/ folder");
        assertTrue(fileName.contains("PatientPull"), "        a Patient Pull EP Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP Patient Pull file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 9.6 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 9.8 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Patient 'Pull' Correspondence"), "        PDF file " + fileName + " should contains 'Patient 'Pull' Correspondence' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear Patient"), "        PDF file " + fileName + " should contains 'Dear Patient' in the Letter contents.");

        logger.info("*** Step 10 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");

        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnExpDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        clientDao.setCln(cln);

        accnPyrErr.setResultCode(ErrorCodeMap.DELETED_RECORD);
        accessionDao.setAccnPyrErr(accnPyrErr);

        accnPhys.setResultCode(ErrorCodeMap.DELETED_RECORD);
        accessionDao.setAccnPhys(accnPhys);

        latestEpCorrespHist.setResultCode(ErrorCodeMap.DELETED_RECORD);
        errorProcessingDao.setEpCorrespHist(latestEpCorrespHist);

        correspFile.setResultCode(ErrorCodeMap.DELETED_RECORD);
        correspFileDao.setCorrespFile(correspFile);

        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);

    }


    @Test(priority = 1, description = "Generate Letter with Type: CLIENT")
    @Parameters({"formatType", "clnAbbrev3", "project", "testSuite", "testCase"})
    public void testPFER_503(String formatType, String clnAbbrev, String project, String testSuite, String testCase) throws Exception {
        logger.info("========== Testing - testPFER_503 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 0, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_USE_PSC_OPTION, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_USE_FAX_OPTION, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_SPLIT_PULLS_BY_USER_ID, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_DAYS_BEFORE_FIRST_PT_CORRESP_SEND, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_GEN_TOA_LTRS, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_DISP_TESTS_ON_CLN_LTR, "1", "1");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_DISPLAY_XREF_CLN_INFO_BLK, "0", "0");

        logger.info("*** Actions: - Setup Client test data in DB");
        logger.info("*** Step 2 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id not in (3,10,8) and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        Cln cln = clientDao.getClnByClnAbbrev(clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnExpDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        cln.setEpLetterSubmSvcId(Integer.parseInt(epLetterSubSvcSeqId));
        cln.setIsEpLtrListMon(true);
        cln.setIsEpLtrListTues(true);
        cln.setIsEpLtrListWed(true);
        cln.setIsEpLtrListThurs(true);
        cln.setIsEpLtrListFri(true);
        cln.setIsEpLtrListSat(true);
        cln.setIsEpLtrListSun(true);
        cln.setClnLtrTypId(2);
        clientDao.setCln(cln);

        logger.info("*** Step 3 Actions: - Create a new Priced Accession via Accession WS");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        Assert.assertNotNull(accnId);

        Accn accn = accessionDao.getAccn(accnId);
        accn.setClnId(cln.getClnId());
        accessionDao.setAccn(accn);

        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS), "Accession is not out of Eligibility Queue");
        accessionDao.deleteUnfixedAccnErrByAccnId(accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNPRICEABLE, QUEUE_WAIT_TIME_MS));
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS), "Accession is not out of Pricing Queue");

        logger.info("*** Step 3 Expected Results: - Verify that the accession is priced");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession status is not Priced");

        logger.info("*** Step 4 Actions: - Delete accession from Q_EP_CORRESP_ERR, Q_EP_CORRESP and ACCN_CLN_Q tables");
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        AccnClnQ accnClnQ = new AccnClnQ();
        accnClnQ.setAccnId(accnId);
        accnClnQ.setQuestnId(14);
        accnClnQ.setRespns(randomCharacter.getRandomAlphaNumericString(10));
        accnClnQ.setIsDeleted(false);
        accessionDao.setAccnClnQ(accnClnQ);

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");
        Date currentDt = convertUtil.convertStringToUtilDate(timeStamp.getCurrentDate(), "MM/dd/yyyy");
        int errSeqId = databaseSequenceDao.getNextSequenceFromOracle("ACCN_PYR_ERR_SEQ");
        AccnPyrErr accnPyrErr = new AccnPyrErr();
        accnPyrErr.setAccnId(accnId);
        accnPyrErr.setErrSeq(errSeqId);
        accnPyrErr.setErrCd(55342);
        accnPyrErr.setPyrPrio(1);
        accnPyrErr.setErrDt(new java.sql.Date(currentDt.getTime()));
        accnPyrErr.setIsPosted(true);
        accessionDao.setAccnPyrErr(accnPyrErr);

        logger.info("*** Step 5.4 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);

        int seqId = databaseSequenceDao.getNextSequenceFromOracle("ACCN_PHYS_SEQ");
        AccnPhys accnPhys = new AccnPhys();
        accnPhys.setSeqId(seqId);
        accnPhys.setAccnId(accnId);
        accnPhys.setPhysSeqId(Integer.parseInt(physSeqId));
        accnPhys.setAccnPhysTypId(1);
        accnPhys.setAccnTestSeqId(0);
        accessionDao.setAccnPhys(accnPhys);

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);
        com.mbasys.common.utility.Money dueAmt = new Money(Float.parseFloat(randomNum));

        QEpCoresp qEpCoresp = new QEpCoresp();
        qEpCoresp.setAccnId(accnId);
        qEpCoresp.setUserId("qatester");
        qEpCoresp.setErrGrpAccn(1);
        qEpCoresp.setClnId(cln.getClnId());
        qEpCoresp.setInDt(new java.sql.Timestamp(accn.getDos().getTime()));
        qEpCoresp.setAssgnDt(accn.getDos());
        qEpCoresp.setDosDt(accn.getDos());
        qEpCoresp.setDueAmtFromErrors(dueAmt);
        qEpCoresp.setLtrCnt(0);
        qEpCoresp.setLtrDest(2);
        qEpCoresp.setNxtLtrDt(new java.sql.Date(currentDt.getTime()));
        errorProcessingDao.setQEpCorresp(qEpCoresp);


        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        AccnProc accnProc = accessionDao.getAccnProcByAccnId(accnId);
        Pyr pyr = payorDao.getPyrByPyrId(accessionDao.getAccnPyrByAccnIdAndPyrPrio(accnId, 1).getPyrId());
        ErrCd errCd1 = errorProcessingDao.getErrCdByErrCdId(Integer.parseInt(errCd));
        QEpCorrespErr qEpCorrespErr = new QEpCorrespErr();
        qEpCorrespErr.setAccnId(accnId);
        qEpCorrespErr.setErrCd(errCd1.getErrCd());
        qEpCorrespErr.setPyrPrio(1);
        qEpCorrespErr.setPyrId(pyr.getPyrId());
        qEpCorrespErr.setPyrGrpId(pyr.getPyrGrpId());
        qEpCorrespErr.setErrGrpId(errCd1.getErrGrpId());
        qEpCorrespErr.setPseudoAccnProcSeqId(accnProc.getAccnProcSeqId());
        qEpCorrespErr.setErrDt(new java.sql.Date(currentDt.getTime()));
        qEpCorrespErr.setDosDt(accn.getDos());
        qEpCorrespErr.setDueAmtByError(dueAmt);
        qEpCorrespErr.setRelevant(false);
        errorProcessingDao.setQEpCorrespErr(qEpCorrespErr);

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        Xref xref = crossReferenceDao.getRandomXrefByXrefTypId(5);
        ClnXref clnXref = clientDao.getClnXrefByClnIdAndXrefId(cln.getClnId(), xref.getXrefId());
        clnXref.setClnId(cln.getClnId());
        clnXref.setXrefId(xref.getXrefId());
        clnXref.setEffDt(new java.sql.Date(prevDt.getTime()));
        clientDao.setClnXref(clnXref);

        logger.info("*** Step 9.4 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10.0 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        List<EpCorrespHist> epCorrespHistList = errorProcessingDao.getEpCorrespHistByAccnId(accnId);
        EpCorrespHist latestEpCorrespHist = epCorrespHistList.get(0);
        CorrespFile correspFile = correspFileDao.getCorrespFileByFileSeq(latestEpCorrespHist.getCorrespFileSeq());
        assertNotNull(correspFile, "A new record should be added into CORRESP_FILE table.");
        String fileName = correspFile.getFileName().trim();

        logger.info("*** Step 10.4 Expected Results: - Verify that a EP Client Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("Client"), "        a Client EP Letter is generated.");
        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP Client file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10.8 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains("Client Correspondence"), "        PDF file " + fileName + " should contains 'Client Correspondence' in Letter cover page.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_USE_FAX_OPTION, "1", "1");

        accnPyrErr.setResultCode(ErrorCodeMap.DELETED_RECORD);
        accessionDao.setAccnPyrErr(accnPyrErr);

        accnPhys.setResultCode(ErrorCodeMap.DELETED_RECORD);
        accessionDao.setAccnPhys(accnPhys);

        latestEpCorrespHist.setResultCode(ErrorCodeMap.DELETED_RECORD);
        errorProcessingDao.setEpCorrespHist(latestEpCorrespHist);

        correspFile.setResultCode(ErrorCodeMap.DELETED_RECORD);
        correspFileDao.setCorrespFile(correspFile);

        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);

        clnXref.setResultCode(ErrorCodeMap.DELETED_RECORD);
        clientDao.setClnXref(clnXref);

        logger.info("*** Step 12 Actions: - Clear System Cache in Xifin Admin Portal");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();
    }

    @Test(priority = 1, description = "Generate Letter with Type: CLIENTPULL")
    @Parameters({"formatType", "clnAbbrev1", "project", "testSuite", "testCase"})
    public void testPFER_504(String formatType, String clnAbbrev, String project, String testSuite, String testCase) throws Exception {
        logger.info("========== Testing - testPFER_504 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 0, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_USE_PSC_OPTION, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_USE_FAX_OPTION, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_SPLIT_PULLS_BY_USER_ID, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_DAYS_BEFORE_FIRST_PT_CORRESP_SEND, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_GEN_TOA_LTRS, "0", "0");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_DISP_TESTS_ON_CLN_LTR, "1", "1");
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_DISPLAY_XREF_CLN_INFO_BLK, "0", "0");

        logger.info("*** Actions: - Setup Client test data in DB");
        logger.info("*** Step 2 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id not in (3,10,8) and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        Cln cln = clientDao.getClnByClnAbbrev(clnAbbrev);
        cln.setPullEpClnEffDt(new java.sql.Date(prevDt.getTime()));
        cln.setPullEpClnExpDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        cln.setEpLetterSubmSvcId(Integer.parseInt(epLetterSubSvcSeqId));
        cln.setIsEpLtrListMon(true);
        cln.setIsEpLtrListTues(true);
        cln.setIsEpLtrListWed(true);
        cln.setIsEpLtrListThurs(true);
        cln.setIsEpLtrListFri(true);
        cln.setIsEpLtrListSat(true);
        cln.setIsEpLtrListSun(true);
        cln.setClnLtrTypId(2);
        clientDao.setCln(cln);

        logger.info("*** Step 3 Actions: - Create a new Final Reported Accession via Accession WS");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        Assert.assertNotNull(accnId);

        Accn accn = accessionDao.getAccn(accnId);
        accn.setClnId(cln.getClnId());
        accessionDao.setAccn(accn);

        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS), "Accession is not out of FR Pending Queue");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS), "Accession is not out of Eligibility Queue");
        accessionDao.deleteUnfixedAccnErrByAccnId(accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNPRICEABLE, QUEUE_WAIT_TIME_MS));
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS), "Accession is not out of Pricing Queue");

        logger.info("*** Step 3 Expected Results: - Verify that the accession is priced");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Accession status is not Priced");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        AccnClnQ accnClnQ = new AccnClnQ();
        accnClnQ.setAccnId(accnId);
        accnClnQ.setQuestnId(14);
        accnClnQ.setRespns(randomCharacter.getRandomAlphaNumericString(10));
        accnClnQ.setIsDeleted(false);
        accessionDao.setAccnClnQ(accnClnQ);

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work

        Date currentDt = convertUtil.convertStringToUtilDate(timeStamp.getCurrentDate(), "MM/dd/yyyy");
        int errSeqId = databaseSequenceDao.getNextSequenceFromOracle("ACCN_PYR_ERR_SEQ");
        AccnPyrErr accnPyrErr = new AccnPyrErr();
        accnPyrErr.setAccnId(accnId);
        accnPyrErr.setErrSeq(errSeqId);
        accnPyrErr.setErrCd(55342);
        accnPyrErr.setPyrPrio(1);
        accnPyrErr.setErrDt(new java.sql.Date(currentDt.getTime()));
        accnPyrErr.setIsPosted(true);
        accessionDao.setAccnPyrErr(accnPyrErr);

        logger.info("*** Step 5.5 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int seqId = databaseSequenceDao.getNextSequenceFromOracle("ACCN_PHYS_SEQ");
        AccnPhys accnPhys = new AccnPhys();
        accnPhys.setSeqId(seqId);
        accnPhys.setAccnId(accnId);
        accnPhys.setPhysSeqId(Integer.parseInt(physSeqId));
        accnPhys.setAccnPhysTypId(1);
        accnPhys.setAccnTestSeqId(0);
        accessionDao.setAccnPhys(accnPhys);

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);
        com.mbasys.common.utility.Money dueAmt = new Money(Float.parseFloat(randomNum));

        QEpCoresp qEpCoresp = new QEpCoresp();
        qEpCoresp.setAccnId(accnId);
        qEpCoresp.setUserId("qatester");
        qEpCoresp.setErrGrpAccn(1);
        qEpCoresp.setClnId(cln.getClnId());
        qEpCoresp.setInDt(new java.sql.Timestamp(accn.getDos().getTime()));
        qEpCoresp.setAssgnDt(accn.getDos());
        qEpCoresp.setDosDt(accn.getDos());
        qEpCoresp.setDueAmtFromErrors(dueAmt);
        qEpCoresp.setLtrCnt(0);
        qEpCoresp.setLtrDest(2);
        qEpCoresp.setNxtLtrDt(new java.sql.Date(currentDt.getTime()));
        errorProcessingDao.setQEpCorresp(qEpCoresp);

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        AccnProc accnProc = accessionDao.getAccnProcByAccnId(accnId);
        Pyr pyr = payorDao.getPyrByPyrId(accessionDao.getAccnPyrByAccnIdAndPyrPrio(accnId, 1).getPyrId());
        ErrCd errCd1 = errorProcessingDao.getErrCdByErrCdId(Integer.parseInt(errCd));
        QEpCorrespErr qEpCorrespErr = new QEpCorrespErr();
        qEpCorrespErr.setAccnId(accnId);
        qEpCorrespErr.setErrCd(errCd1.getErrCd());
        qEpCorrespErr.setPyrPrio(1);
        qEpCorrespErr.setPyrId(pyr.getPyrId());
        qEpCorrespErr.setPyrGrpId(pyr.getPyrGrpId());
        qEpCorrespErr.setErrGrpId(errCd1.getErrGrpId());
        qEpCorrespErr.setPseudoAccnProcSeqId(accnProc.getAccnProcSeqId());
        qEpCorrespErr.setErrDt(new java.sql.Date(currentDt.getTime()));
        qEpCorrespErr.setDosDt(accn.getDos());
        qEpCorrespErr.setDueAmtByError(dueAmt);
        qEpCorrespErr.setRelevant(false);
        errorProcessingDao.setQEpCorrespErr(qEpCorrespErr);

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        Xref xref = crossReferenceDao.getRandomXrefByXrefTypId(5);
        ClnXref clnXref = clientDao.getClnXrefByClnIdAndXrefId(cln.getClnId(), xref.getXrefId());

        clnXref.setClnId(cln.getClnId());
        clnXref.setXrefId(xref.getXrefId());
        clnXref.setEffDt(new java.sql.Date(prevDt.getTime()));
        clientDao.setClnXref(clnXref);

        logger.info("*** Step 9.6 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10.2 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        List<EpCorrespHist> epCorrespHistList = errorProcessingDao.getEpCorrespHistByAccnId(accnId);
        EpCorrespHist latestEpCorrespHist = epCorrespHistList.get(0);
        CorrespFile correspFile = correspFileDao.getCorrespFileByFileSeq(latestEpCorrespHist.getCorrespFileSeq());
        assertNotNull(correspFile, "A new record should be added into CORRESP_FILE table.");
        String fileName = correspFile.getFileName().trim();

        logger.info("*** Step 10.6 Expected Results: - Verify that a EP Client Pull Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("ClientPull"), "        a Client Pull EP Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP Client Pull file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10.8 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        QEpCoresp qEpCorrespInfoList = errorProcessingDao.getQEpCorrespByAccnId(accnId);
        assertEquals(qEpCorrespInfoList.getLtrCnt(), 1, "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 10.9 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Client 'Pull' Correspondence"), "        PDF file " + fileName + " should contains 'Client 'Pull' Correspondence' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(SystemSettingMap.SS_EP_LTR_USE_FAX_OPTION, "1", "1");

        //Reset pull_ep_cln_eff_dt, Pull_Ep_Cln_Exp_Dt, Pull_Ep_Pt_Eff_Dt, and Pull_Ep_Pt_Exp_Dt to Null in CLN table
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnExpDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        clientDao.setCln(cln);

        accnPyrErr.setResultCode(ErrorCodeMap.DELETED_RECORD);
        accessionDao.setAccnPyrErr(accnPyrErr);

        accnPhys.setResultCode(ErrorCodeMap.DELETED_RECORD);
        accessionDao.setAccnPhys(accnPhys);

        latestEpCorrespHist.setResultCode(ErrorCodeMap.DELETED_RECORD);
        errorProcessingDao.setEpCorrespHist(latestEpCorrespHist);

        correspFile.setResultCode(ErrorCodeMap.DELETED_RECORD);
        correspFileDao.setCorrespFile(correspFile);

        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);

        clnXref.setResultCode(ErrorCodeMap.DELETED_RECORD);
        clientDao.setClnXref(clnXref);

    }

    @Test(priority = 1, description = "Generate Letter with Type: EMAIL")
    @Parameters({"formatType", "clnAbbrev"})
    public void testPFER_505(String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_505 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 0, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = clientDao.getClnByClnAbbrev(clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        clientDao.setCln(cln);
        logger.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =3 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);
        String correspEmail = "qatester@xifin.com";

        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");
        listCol.add("CORRESP_EMAIL");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        listVal.add("'" + correspEmail + "'");
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(clnId);
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4.2 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5.2 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5.4 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6.5 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("2");
        insertValues.add(prevDt);

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7.5 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");

        logger.info("*** Step 9.2 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 10.2 Expected Results: - Verify that a EP Email Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("EMAIL"), "        a EP EMAIL Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP Email file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10.6 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Document Type: EP Letter"), "        PDF file " + fileName + " should contains 'Document Type: EP Letter' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");
        assertTrue(pdfContentsStr.contains("EMAIL: " + correspEmail), "        PDF file " + fileName + " should contains 'EMAIL: " + correspEmail + "' in Letter cover page.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);

    }

    @Test(priority = 1, description = "Generate Letter with Type: NO EMAIL")
    @Parameters({"formatType", "clnAbbrev"})
    public void testPFER_506(String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_506 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 0, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);

        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =3 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);

        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");
        listCol.add("CORRESP_EMAIL");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        listVal.add("''");
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(clnId);
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5.2 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5.4 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6.5 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("2");
        insertValues.add(prevDt);

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");

        logger.info("*** Step 9.3 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 10 Expected Results: - Verify that a EP No Email Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("EMAIL_NO_EMAIL_SPECIFIED"), "        a EP EMAIL_NO_EMAIL_SPECIFIED Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP No Email file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10.2 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 10.3 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Document Type: EP Letter"), "        PDF file " + fileName + " should contains 'Document Type: EP Letter' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");
        assertTrue(pdfContentsStr.contains("EMAIL: NO_EMAIL_SPECIFIED"), "        PDF file " + fileName + " should contains 'EMAIL: NO_EMAIL_SPECIFIED' in Letter cover page.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);

    }

    @Test(priority = 1, description = "Generate Letter with Type: PSC")
    @Parameters({"formatType", "clnAbbrev"})
    public void testPFER_507(String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_507 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 1, 1356 = 0, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);

        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =3 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);

        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");
        listCol.add("FK_PHLEB_FAC_ID");

        listVal.clear();
        listVal.add(clnId);
        listVal.add("1");
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5.2 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6.2 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("4"); //4: PSC
        insertValues.add(prevDt);

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8.5 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");

        logger.info("*** Step 9.0 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 10.1 Expected Results: - Verify that a EP PSC Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("PSC"), "        a EP PSC Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP PSC file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10.2 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 10.5 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Client Correspondence"), "        PDF file " + fileName + " should contains 'Client Correspondence' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);

    }

    @Test(priority = 1, description = "Generate Letter with Type: PSCPULL")
    @Parameters({"formatType", "clnAbbrev"})
    public void testPFER_508(String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_508 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();
        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 1, 1356 = 0, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);

        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =3 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");


        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");
        listCol.add("pull_ep_cln_eff_dt");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");
        listCol.add("FK_PHLEB_FAC_ID");

        listVal.clear();
        listVal.add(clnId);
        listVal.add("1");
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5.2 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5.4 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("4"); //4: PSC
        insertValues.add(prevDt);

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");

        logger.info("*** Step 9.0 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 10 Expected Results: - Verify that a EP PSC Pull Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("PSC_Pull"), "        a EP PSC_Pull Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP PSC Pull file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 10 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Client 'Pull' Correspondence"), "        PDF file " + fileName + " should contains 'Client 'Pull' Correspondence' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1356", testDb);
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        //Reset pull_ep_cln_eff_dt, Pull_Ep_Cln_Exp_Dt, Pull_Ep_Pt_Eff_Dt, and Pull_Ep_Pt_Exp_Dt to Null in CLN table
        listCol.clear();
        listCol.add("pull_ep_cln_eff_dt");
        listCol.add("Pull_Ep_Cln_Exp_Dt");
        listCol.add("Pull_Ep_Pt_Eff_Dt");
        listCol.add("Pull_Ep_Pt_Exp_Dt");

        listVal.clear();
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_cln_id = '" + clnId + "'", testDb);

        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);

    }

    @Test(priority = 1, description = "Generate Letter with Type: FAX")
    @Parameters({"formatType", "clnAbbrev"})
    public void testPFER_509(String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_509 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 1, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1.2 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);


        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =2 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);
        String correspFaxNum = "8585551234";

        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");
        listCol.add("corresp_fax");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        listVal.add(correspFaxNum);
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(clnId);
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5.2 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6.2 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("2");
        insertValues.add(prevDt);

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");

        logger.info("*** Step 9.0 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 10 Expected Results: - Verify that a EP FAX Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("FAX"), "        a EP FAX Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP FAX file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 10 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Fax Number: (858) 555-1234"), "        PDF file " + fileName + " should contains 'Fax Number: (858) 555-1234' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        //Reset pull_ep_cln_eff_dt, Pull_Ep_Cln_Exp_Dt, Pull_Ep_Pt_Eff_Dt, and Pull_Ep_Pt_Exp_Dt to Null in CLN table
        listCol.clear();
        listCol.add("pull_ep_cln_eff_dt");
        listCol.add("Pull_Ep_Cln_Exp_Dt");
        listCol.add("Pull_Ep_Pt_Eff_Dt");
        listCol.add("Pull_Ep_Pt_Exp_Dt");

        listVal.clear();
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_cln_id = '" + clnId + "'", testDb);


        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);

    }

    @Test(priority = 1, description = "Generate Letter with Type: NO FAX")
    @Parameters({"formatType", "clnAbbrev"})
    public void testPFER_520(String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_520 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 1, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);

        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =2 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);

        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");
        listCol.add("corresp_fax");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        listVal.add("''");
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(clnId);
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        //List<String> errCdInfoList = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where Fk_Err_Grp_Id = 3 And Fk_Corresp_Typ_Id In (1,2)",testDb);
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5.2 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5.4 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6.2 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("2");
        insertValues.add(prevDt);

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9.5 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");

        logger.info("*** Step 9.0 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 10.2 Expected Results: - Verify that a EP NO FAX Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("FAX_NO_FAX_SPECIFIED"), "        a EP FAX_NO_FAX_SPECIFIED Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP NO FAX file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10.4 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 10.6 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Fax Number: NO_FAX_SPECIFIED"), "        PDF file " + fileName + " should contains 'Fax Number: NO_FAX_SPECIFIED' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        //Reset pull_ep_cln_eff_dt, Pull_Ep_Cln_Exp_Dt, Pull_Ep_Pt_Eff_Dt, and Pull_Ep_Pt_Exp_Dt to Null in CLN table
        listCol.clear();
        listCol.add("pull_ep_cln_eff_dt");
        listCol.add("Pull_Ep_Cln_Exp_Dt");
        listCol.add("Pull_Ep_Pt_Eff_Dt");
        listCol.add("Pull_Ep_Pt_Exp_Dt");

        listVal.clear();
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_cln_id = '" + clnId + "'", testDb);


        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);
    }

    @Test(priority = 1, description = "Generate Letter with Type: SFAX")
    @Parameters({"formatType", "clnAbbrev"})
    public void testPFER_510(String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_510 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 1, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);

        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =11 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);
        String correspFaxNum = "8585551234";

        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");
        listCol.add("corresp_fax");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        listVal.add(correspFaxNum);
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(clnId);
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5.2 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6.2 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("2");
        insertValues.add(prevDt);

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");

        logger.info("*** Step 9.2 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 10 Expected Results: - Verify that a EP SFAX Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("SFAX"), "        a EP SFAX Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP SFAX file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 10 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Fax Number: (858) 555-1234"), "        PDF file " + fileName + " should contains 'Fax Number: (858) 555-1234' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        //Reset pull_ep_cln_eff_dt, Pull_Ep_Cln_Exp_Dt, Pull_Ep_Pt_Eff_Dt, and Pull_Ep_Pt_Exp_Dt to Null in CLN table
        listCol.clear();
        listCol.add("pull_ep_cln_eff_dt");
        listCol.add("Pull_Ep_Cln_Exp_Dt");
        listCol.add("Pull_Ep_Pt_Eff_Dt");
        listCol.add("Pull_Ep_Pt_Exp_Dt");

        listVal.clear();
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_cln_id = '" + clnId + "'", testDb);


        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);

    }


    @Test(priority = 1, description = "Generate Letter with Type: EFAX")
    @Parameters({"formatType", "clnAbbrev"})
    public void testPFER_511(String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_511 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 1, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);

        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =9 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);
        String correspFaxNum = "8585551234";

        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");
        listCol.add("corresp_fax");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        listVal.add(correspFaxNum);
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(clnId);
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("2");
        insertValues.add(prevDt);

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");

        logger.info("*** Step 9.2 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 10.2 Expected Results: - Verify that a EP EFAX Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("EFAX"), "        a EP EFAX Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP EFAX file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10.4 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 10.5 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Fax Number: (858) 555-1234"), "        PDF file " + fileName + " should contains 'Fax Number: (858) 555-1234' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        //Reset pull_ep_cln_eff_dt, Pull_Ep_Cln_Exp_Dt, Pull_Ep_Pt_Eff_Dt, and Pull_Ep_Pt_Exp_Dt to Null in CLN table
        listCol.clear();
        listCol.add("pull_ep_cln_eff_dt");
        listCol.add("Pull_Ep_Cln_Exp_Dt");
        listCol.add("Pull_Ep_Pt_Eff_Dt");
        listCol.add("Pull_Ep_Pt_Exp_Dt");

        listVal.clear();
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_cln_id = '" + clnId + "'", testDb);


        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);

    }

    @Test(priority = 1, description = "Generate Letter with Type: EFAX NO FAX")
    @Parameters({"email", "password", "eType", "xapEnv", "engConfigDB", "formatType", "clnAbbrev"})
    public void testPFER_522(String ssoUsername, String ssoPassword, String eType, String xapEnv, String engConfigDB, String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_522 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 1, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);


        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =9 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);

        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");
        listCol.add("corresp_fax");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        listVal.add("''");
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(clnId);
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4.2 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5.2 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5.4 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6.2 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("2");
        insertValues.add(prevDt);

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9.2 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");

        logger.info("*** Step 9.4 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 10.2 Expected Results: - Verify that a EP EFAX NO FAX Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("EFAX_NO_FAX_SPECIFIED"), "        an EP EFAX_NO_FAX_SPECIFIED Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP EFAX NO FAX file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10.4 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 10.6 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Fax Number: NO_FAX_SPECIFIED"), "        PDF file " + fileName + " should contains 'Fax Number: NO_FAX_SPECIFIED' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        //Reset pull_ep_cln_eff_dt, Pull_Ep_Cln_Exp_Dt, Pull_Ep_Pt_Eff_Dt, and Pull_Ep_Pt_Exp_Dt to Null in CLN table
        listCol.clear();
        listCol.add("pull_ep_cln_eff_dt");
        listCol.add("Pull_Ep_Cln_Exp_Dt");
        listCol.add("Pull_Ep_Pt_Eff_Dt");
        listCol.add("Pull_Ep_Pt_Exp_Dt");

        listVal.clear();
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_cln_id = '" + clnId + "'", testDb);


        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);

    }

    @Test(priority = 1, description = "SKIPS -EMAIL")
    @Parameters({"formatType", "clnAbbrev"})
    public void testPFER_512(String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_512 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 0, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);

        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =8 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        String altEpLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =3 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);
        String correspEmail = "qatester@xifin.com";

        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");
        listCol.add("CORRESP_EMAIL");
        listCol.add("FK_ALT_EP_LETTER_SUBM_SVC_ID");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        listVal.add("'" + correspEmail + "'");
        listVal.add(altEpLetterSubSvcSeqId);
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(clnId);
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5.2 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5.4 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6.2 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        listCol.add("B_Use_Alt_Ep_Ltr_Subm_Svc");

        List<Object> insertValues = new ArrayList<>();
        insertValues.add(accnId);
        insertValues.add("qatester");
        insertValues.add("1");
        insertValues.add(clnId);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("2");
        insertValues.add(prevDt);
        insertValues.add("1");

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9.1 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");


        logger.info("*** Step 9.4 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 10.2 Expected Results: - Verify that a EP SKIPS Email Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("EMAIL"), "        a EP EMAIL Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP SKIPS Email file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10.4 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 10.6 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Document Type: EP Letter"), "        PDF file " + fileName + " should contains 'Document Type: EP Letter' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");
        assertTrue(pdfContentsStr.contains("EMAIL: " + correspEmail), "        PDF file " + fileName + " should contains 'EMAIL: " + correspEmail + "' in Letter cover page.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        //Reset pull_ep_cln_eff_dt, Pull_Ep_Cln_Exp_Dt, Pull_Ep_Pt_Eff_Dt, and Pull_Ep_Pt_Exp_Dt to Null in CLN table
        listCol.clear();
        listCol.add("pull_ep_cln_eff_dt");
        listCol.add("Pull_Ep_Cln_Exp_Dt");
        listCol.add("Pull_Ep_Pt_Eff_Dt");
        listCol.add("Pull_Ep_Pt_Exp_Dt");

        listVal.clear();
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_cln_id = '" + clnId + "'", testDb);

        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);

    }

    @Test(priority = 1, description = "SKIPS -EMAIL_NO_EMAIL_SPECIFIED")
    @Parameters({"email", "password", "eType", "xapEnv", "engConfigDB", "formatType", "clnAbbrev"})
    public void testPFER_513(String ssoUsername, String ssoPassword, String eType, String xapEnv, String engConfigDB, String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_513 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 0, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);

        logger.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");
        logIntoSso(ssoUsername, ssoPassword);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =8 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        String altEpLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =3 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);

        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");
        listCol.add("CORRESP_EMAIL");
        listCol.add("FK_ALT_EP_LETTER_SUBM_SVC_ID");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        listVal.add("''");
        listVal.add(altEpLetterSubSvcSeqId);
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(clnId);
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5.2 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5.4 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6.2 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        listCol.add("B_Use_Alt_Ep_Ltr_Subm_Svc");

        List<Object> insertValues = new ArrayList<>();
        insertValues.add(accnId);
        insertValues.add("qatester");
        insertValues.add("1");
        insertValues.add(clnId);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("2");
        insertValues.add(prevDt);
        insertValues.add("1");

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");

        logger.info("*** Step 9.0 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 10 Expected Results: - Verify that a EP SKIPS No Email Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("EMAIL_NO_EMAIL_SPECIFIED"), "        a EP EMAIL_NO_EMAIL_SPECIFIED Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP SKIPS No Email file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 10 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Document Type: EP Letter"), "        PDF file " + fileName + " should contains 'Document Type: EP Letter' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");
        assertTrue(pdfContentsStr.contains("EMAIL: NO_EMAIL_SPECIFIED"), "        PDF file " + fileName + " should contains 'EMAIL: NO_EMAIL_SPECIFIED' in Letter cover page.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        //Reset pull_ep_cln_eff_dt, Pull_Ep_Cln_Exp_Dt, Pull_Ep_Pt_Eff_Dt, and Pull_Ep_Pt_Exp_Dt to Null in CLN table
        listCol.clear();
        listCol.add("pull_ep_cln_eff_dt");
        listCol.add("Pull_Ep_Cln_Exp_Dt");
        listCol.add("Pull_Ep_Pt_Eff_Dt");
        listCol.add("Pull_Ep_Pt_Exp_Dt");

        listVal.clear();
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_cln_id = '" + clnId + "'", testDb);

        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);

        logger.info("*** Step 12 Actions: - Clear System Cache in Xifin Admin Portal");
        logIntoSso(ssoUsername, ssoPassword);
        xifinAdminUtils.clearDataCache();
    }

    @Test(priority = 1, description = "SKIPS -FAX")
    @Parameters({"email", "password", "eType", "xapEnv", "engConfigDB", "formatType", "clnAbbrev"})
    public void testPFER_514(String ssoUsername, String ssoPassword, String eType, String xapEnv, String engConfigDB, String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_514 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 1, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);

        logger.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");
        logIntoSso(ssoUsername, ssoPassword);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String altEpLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =2 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =8 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);
        String correspFaxNum = "8585551234";

        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");
        listCol.add("corresp_fax");
        listCol.add("FK_ALT_EP_LETTER_SUBM_SVC_ID");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        listVal.add(correspFaxNum);
        listVal.add(altEpLetterSubSvcSeqId);
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3.2 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(clnId);
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4.2 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5.2 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5.4 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6.2 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        listCol.add("B_Use_Alt_Ep_Ltr_Subm_Svc");

        List<Object> insertValues = new ArrayList<>();
        insertValues.add(accnId);
        insertValues.add("qatester");
        insertValues.add("1");
        insertValues.add(clnId);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("2");
        insertValues.add(prevDt);
        insertValues.add("1");

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9.2 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");

        logger.info("*** Step 9.0 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 10 Expected Results: - Verify that a EP SKIPS FAX Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("FAX"), "        a EP FAX SKIPS Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP SKIPS FAX file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 10 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Fax Number: (858) 555-1234"), "        PDF file " + fileName + " should contains 'Fax Number: (858) 555-1234' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        //Reset pull_ep_cln_eff_dt, Pull_Ep_Cln_Exp_Dt, Pull_Ep_Pt_Eff_Dt, and Pull_Ep_Pt_Exp_Dt to Null in CLN table
        listCol.clear();
        listCol.add("pull_ep_cln_eff_dt");
        listCol.add("Pull_Ep_Cln_Exp_Dt");
        listCol.add("Pull_Ep_Pt_Eff_Dt");
        listCol.add("Pull_Ep_Pt_Exp_Dt");

        listVal.clear();
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_cln_id = '" + clnId + "'", testDb);

        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);

        logger.info("*** Step 12 Actions: - Clear System Cache in Xifin Admin Portal");
        logIntoSso(ssoUsername, ssoPassword);
        xifinAdminUtils.clearDataCache();
    }


    @Test(priority = 1, description = "SKIPS -FAX_NO_FAX_SPECIFIED")
    @Parameters({"email", "password", "eType", "xapEnv", "engConfigDB", "formatType", "clnAbbrev"})
    public void testPFER_515(String ssoUsername, String ssoPassword, String eType, String xapEnv, String engConfigDB, String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_515 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 1, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);

        logger.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");
        logIntoSso(ssoUsername, ssoPassword);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =8 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        String altEpLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =2 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);

        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");
        listCol.add("corresp_fax");
        listCol.add("FK_ALT_EP_LETTER_SUBM_SVC_ID");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        listVal.add("''");
        listVal.add(altEpLetterSubSvcSeqId);
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(clnId);
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        //List<String> errCdInfoList = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where Fk_Err_Grp_Id = 3 And Fk_Corresp_Typ_Id In (1,2)",testDb);
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5.2 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5.4 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6.2 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        listCol.add("B_Use_Alt_Ep_Ltr_Subm_Svc");

        List<Object> insertValues = new ArrayList<>();
        insertValues.add(accnId);
        insertValues.add("qatester");
        insertValues.add("1");
        insertValues.add(clnId);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("2");
        insertValues.add(prevDt);
        insertValues.add("1");

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9.2 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");

        logger.info("*** Step 9.0 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 10.1 Expected Results: - Verify that a EP SKIPS NO FAX Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("FAX_NO_FAX_SPECIFIED"), "        a EP SKIPS FAX_NO_FAX_SPECIFIED Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP SKIPS NO FAX file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10.2 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 10.4 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Fax Number: NO_FAX_SPECIFIED"), "        PDF file " + fileName + " should contains 'Fax Number: NO_FAX_SPECIFIED' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        //Reset pull_ep_cln_eff_dt, Pull_Ep_Cln_Exp_Dt, Pull_Ep_Pt_Eff_Dt, and Pull_Ep_Pt_Exp_Dt to Null in CLN table
        listCol.clear();
        listCol.add("pull_ep_cln_eff_dt");
        listCol.add("Pull_Ep_Cln_Exp_Dt");
        listCol.add("Pull_Ep_Pt_Eff_Dt");
        listCol.add("Pull_Ep_Pt_Exp_Dt");

        listVal.clear();
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_cln_id = '" + clnId + "'", testDb);


        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);

        logger.info("*** Step 12 Actions: - Clear System Cache in Xifin Admin Portal");
        logIntoSso(ssoUsername, ssoPassword);
        xifinAdminUtils.clearDataCache();
    }

    @Test(priority = 1, description = "SKIPS -EFAX")
    @Parameters({"email", "password", "eType", "xapEnv", "engConfigDB", "formatType", "clnAbbrev"})
    public void testPFER_516(String ssoUsername, String ssoPassword, String eType, String xapEnv, String engConfigDB, String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_516 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 1, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);

        logger.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");
        logIntoSso(ssoUsername, ssoPassword);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =8 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        String altEpLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =9 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);
        String correspFaxNum = "8585551234";

        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");
        listCol.add("corresp_fax");
        listCol.add("FK_ALT_EP_LETTER_SUBM_SVC_ID");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        listVal.add(correspFaxNum);
        listVal.add(altEpLetterSubSvcSeqId);
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(clnId);
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4.2 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5.2 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5.4 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        listCol.add("B_Use_Alt_Ep_Ltr_Subm_Svc");

        List<Object> insertValues = new ArrayList<>();
        insertValues.add(accnId);
        insertValues.add("qatester");
        insertValues.add("1");
        insertValues.add(clnId);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("2");
        insertValues.add(prevDt);
        insertValues.add("1");

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8.4 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");

        logger.info("*** Step 9.0 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 10.1 Expected Results: - Verify that a EP SKIPS EFAX Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("EFAX"), "        a EP SKIPS EFAX Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP SKIPS EFAX file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10.2 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 10.5 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Fax Number: (858) 555-1234"), "        PDF file " + fileName + " should contains 'Fax Number: (858) 555-1234' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        //Reset pull_ep_cln_eff_dt, Pull_Ep_Cln_Exp_Dt, Pull_Ep_Pt_Eff_Dt, and Pull_Ep_Pt_Exp_Dt to Null in CLN table
        listCol.clear();
        listCol.add("pull_ep_cln_eff_dt");
        listCol.add("Pull_Ep_Cln_Exp_Dt");
        listCol.add("Pull_Ep_Pt_Eff_Dt");
        listCol.add("Pull_Ep_Pt_Exp_Dt");

        listVal.clear();
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_cln_id = '" + clnId + "'", testDb);

        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);

        logger.info("*** Step 12 Actions: - Clear System Cache in Xifin Admin Portal");
        logIntoSso(ssoUsername, ssoPassword);
        xifinAdminUtils.clearDataCache();
    }

    @Test(priority = 1, description = "SKIPS -SFAX")
    @Parameters({"email", "password", "eType", "xapEnv", "engConfigDB", "formatType", "clnAbbrev"})
    public void testPFER_517(String ssoUsername, String ssoPassword, String eType, String xapEnv, String engConfigDB, String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_517 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 1, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);


        logger.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");
        logIntoSso(ssoUsername, ssoPassword);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =8 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        String altEpLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =11 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);
        String correspFaxNum = "8585551234";

        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");
        listCol.add("corresp_fax");
        listCol.add("FK_ALT_EP_LETTER_SUBM_SVC_ID");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        listVal.add(correspFaxNum);
        listVal.add(altEpLetterSubSvcSeqId);
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3.1 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(clnId);
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4.3 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        //List<String> errCdInfoList = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where Fk_Err_Grp_Id = 3 And Fk_Corresp_Typ_Id In (1,2)",testDb);
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5.2 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5.4 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6.2 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        listCol.add("B_Use_Alt_Ep_Ltr_Subm_Svc");

        List<Object> insertValues = new ArrayList<>();
        insertValues.add(accnId);
        insertValues.add("qatester");
        insertValues.add("1");
        insertValues.add(clnId);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("2");
        insertValues.add(prevDt);
        insertValues.add("1");

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");

        logger.info("*** Step 9.0 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 10.2 Expected Results: - Verify that a EP SKIPS SFAX Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("SFAX"), "        a EP SKIPS SFAX Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP SKIPS SFAX file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10.4 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 10.6 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Fax Number: (858) 555-1234"), "        PDF file " + fileName + " should contains 'Fax Number: (858) 555-1234' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        //Reset pull_ep_cln_eff_dt, Pull_Ep_Cln_Exp_Dt, Pull_Ep_Pt_Eff_Dt, and Pull_Ep_Pt_Exp_Dt to Null in CLN table
        listCol.clear();
        listCol.add("pull_ep_cln_eff_dt");
        listCol.add("Pull_Ep_Cln_Exp_Dt");
        listCol.add("Pull_Ep_Pt_Eff_Dt");
        listCol.add("Pull_Ep_Pt_Exp_Dt");

        listVal.clear();
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_cln_id = '" + clnId + "'", testDb);


        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);

        logger.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");
        logIntoSso(ssoUsername, ssoPassword);
        xifinAdminUtils.clearDataCache();
    }

    @Test(priority = 1, description = " SKIPS -CLIENT")
    @Parameters({"email", "password", "eType", "xapEnv", "engConfigDB", "formatType", "clnAbbrev"})
    public void testPFER_518(String ssoUsername, String ssoPassword, String eType, String xapEnv, String engConfigDB, String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_518 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 0, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);

        logger.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");
        logIntoSso(ssoUsername, ssoPassword);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =8 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        String altEpLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id not in (3,9,10,11,8) and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);

        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");
        listCol.add("FK_ALT_EP_LETTER_SUBM_SVC_ID");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        listVal.add(altEpLetterSubSvcSeqId);
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3.2 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(clnId);
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4.2 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        //List<String> errCdInfoList = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where Fk_Err_Grp_Id = 3 And Fk_Corresp_Typ_Id In (1,2)",testDb);
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5.2 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6.2 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        listCol.add("B_Use_Alt_Ep_Ltr_Subm_Svc");

        List<Object> insertValues = new ArrayList<>();
        insertValues.add(accnId);
        insertValues.add("qatester");
        insertValues.add("1");
        insertValues.add(clnId);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("2");
        insertValues.add(prevDt);
        insertValues.add("1");

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9.2 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");

        logger.info("*** Step 9.0 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 10.2 Expected Results: - Verify that a EP SKIPS Client Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("Client"), "        a EP SKIPS Client Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP SKIPS Client file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10.4 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 10.6 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Client Correspondence"), "        PDF file " + fileName + " should contains 'Client Correspondence' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        //Reset pull_ep_cln_eff_dt, Pull_Ep_Cln_Exp_Dt, Pull_Ep_Pt_Eff_Dt, and Pull_Ep_Pt_Exp_Dt to Null in CLN table
        listCol.clear();
        listCol.add("pull_ep_cln_eff_dt");
        listCol.add("Pull_Ep_Cln_Exp_Dt");
        listCol.add("Pull_Ep_Pt_Eff_Dt");
        listCol.add("Pull_Ep_Pt_Exp_Dt");

        listVal.clear();
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_cln_id = '" + clnId + "'", testDb);

        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);

        logger.info("*** Step 12 Actions: - Clear System Cache in Xifin Admin Portal");
        logIntoSso(ssoUsername, ssoPassword);
        xifinAdminUtils.clearDataCache();
    }

    @Test(priority = 1, description = "SKIPS -CLIENT_PULL")
    @Parameters({"email", "password", "eType", "xapEnv", "engConfigDB", "formatType", "clnAbbrev"})
    public void testPFER_519(String ssoUsername, String ssoPassword, String eType, String xapEnv, String engConfigDB, String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_519 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();
        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 0, 1355 = 0, 1356 = 0, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1.2 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);

        logger.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");
        logIntoSso(ssoUsername, ssoPassword);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =8 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        String altEpLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id not in (3,9,11,2,8) and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");

        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");
        listCol.add("pull_ep_cln_eff_dt");
        listCol.add("FK_ALT_EP_LETTER_SUBM_SVC_ID");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");
        listVal.add(altEpLetterSubSvcSeqId);
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3.2 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(clnId);
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4.2 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        //List<String> errCdInfoList = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where Fk_Err_Grp_Id = 3 And Fk_Corresp_Typ_Id In (1,2)",testDb);
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5.2 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5.4 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6.2 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        listCol.add("B_Use_Alt_Ep_Ltr_Subm_Svc");

        List<Object> insertValues = new ArrayList<>();
        insertValues.add(accnId);
        insertValues.add("qatester");
        insertValues.add("1");
        insertValues.add(clnId);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("2");
        insertValues.add(prevDt);
        insertValues.add("1");

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9.2 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");


        logger.info("*** Step 9.0 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 10 Expected Results: - Verify that a EP SKIPS Client Pull Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("ClientPull"), "        a EP SKIPS Client Pull_ Letter is generated.");

        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        System.out.println("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP SKIPS Client Pull file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 10 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 10 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Client 'Pull' Correspondence"), "        PDF file " + fileName + " should contains 'Client 'Pull' Correspondence' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName), "        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");

        logger.info("*** Step 11 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName);
        updateSystemSetting(1356, "1", "1");

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        //Reset pull_ep_cln_eff_dt, Pull_Ep_Cln_Exp_Dt, Pull_Ep_Pt_Eff_Dt, and Pull_Ep_Pt_Exp_Dt to Null in CLN table
        listCol.clear();
        listCol.add("pull_ep_cln_eff_dt");
        listCol.add("Pull_Ep_Cln_Exp_Dt");
        listCol.add("Pull_Ep_Pt_Eff_Dt");
        listCol.add("Pull_Ep_Pt_Exp_Dt");

        listVal.clear();
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        listVal.add("''");
        daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_cln_id = '" + clnId + "'", testDb);

        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);

        logger.info("*** Step 12 Actions: - Clear System Cache in Xifin Admin Portal");
        logIntoSso(ssoUsername, ssoPassword);
        xifinAdminUtils.clearDataCache();
    }

    @Test(priority = 1, description = "Generate Letter with Type: ClientTOA, SummaryTOA")
    @Parameters({"email", "password", "eType", "xapEnv", "engConfigDB", "formatType", "clnAbbrev"})
    public void testPFER_500(String ssoUsername, String ssoPassword, String eType, String xapEnv, String engConfigDB, String formatType, String clnAbbrev) throws Exception {
        logger.info("========== Testing - testPFER_500 ==========");

        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Actions: - Update system_setting and set 1387 = 1, 1355 = 0, 1356 = 0, 1358 = 0, 1432 = 0, 1359 = 0, 1418 = 1 in DB");
        int ss1387RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1387", testDb);
        int ss1355RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1355", testDb);
        int ss1356RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1356", testDb);
        int ss1358RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1358", testDb);
        int ss1432RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1432", testDb);
        int ss1359RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1359", testDb);
        int ss1418RowCount = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 1", "PK_SETTING_ID", "1418", testDb);

        logger.info("*** Step 1 Expected Results: - Verify that the System Settings should be updated");
        assertTrue(ss1387RowCount > 0, "        SS1387 is updated.");
        assertTrue(ss1355RowCount > 0, "        SS1355 is updated.");
        assertTrue(ss1356RowCount > 0, "        SS1356 is updated.");
        assertTrue(ss1358RowCount > 0, "        SS1358 is updated.");
        assertTrue(ss1432RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1359RowCount > 0, "        SS1432 is updated.");
        assertTrue(ss1418RowCount > 0, "        SS1418 is updated.");

        logger.info("*** Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);

        logger.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");
        logIntoSso(ssoUsername, ssoPassword);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 3 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = "7003";//daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =7003 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        List<String> clnInfoList = daoManagerPlatform.getClnInfoFromCLNByConditon("pull_ep_cln_eff_dt is null And Pull_Ep_Cln_Exp_Dt Is Null And Pull_Ep_Pt_Eff_Dt Is Null And Pull_Ep_Pt_Exp_Dt Is Null and cln_abbrev = '" + clnAbbrev + "'", testDb);
        String clnId = clnInfoList.get(0);

        List<String> listCol = new ArrayList<>();
        listCol.add("FK_EP_LETTER_SUBM_SVC_ID");
        listCol.add("B_EP_LTR_LIST_MON");
        listCol.add("B_EP_LTR_LIST_TUES");
        listCol.add("B_EP_LTR_LIST_WED");
        listCol.add("B_EP_LTR_LIST_THURS");
        listCol.add("B_EP_LTR_LIST_FRI");
        listCol.add("B_EP_LTR_LIST_SAT");
        listCol.add("B_EP_LTR_LIST_SUN");
        listCol.add("FK_CLN_LTR_TYP_ID");

        List<String> listVal = new ArrayList<>();
        listVal.add(epLetterSubSvcSeqId);
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("1");
        listVal.add("2");
        int row = daoManagerPlatform.updateRowInTable("CLN", listCol, listVal, "Pk_Cln_id = " + clnId, testDb);

        logger.info("*** Step 3.2 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(row > 0, "        Client " + clnInfoList.get(4) + " should be updated.");

        logger.info("*** Step 4 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        String accnId = accnForEPLetterEngineList.get(0);
        String orgClnId = accnForEPLetterEngineList.get(1);
        int rowCountInAccnClnQ;

        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if (accnClnQList.size() == 0) {
            List<String> listCol1 = new ArrayList<>();
            listCol1.add("pk_accn_id");
            listCol1.add("pk_questn_id");
            listCol1.add("Respns");
            listCol1.add("B_deleted");

            List<Object> listVal1 = new ArrayList<>();
            listVal1.add(accnId);
            listVal1.add("14");
            listVal1.add(randomCharacter.getRandomAlphaNumericString(10));
            listVal1.add("0");
            rowCountInAccnClnQ = daoManagerPlatform.addRecordIntoTable("Accn_Cln_Q", listCol1, listVal1, testDb);
        } else {
            rowCountInAccnClnQ = accnClnQList.size();
        }

        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(clnId);
        int updatedRowCountInAccn = daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        logger.info("*** Step 4.2 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0, "        Accession " + accnId + " should have record in ACCN_CLN_Q table.");
        assertTrue(updatedRowCountInAccn > 0, "        Accession " + accnId + " should be updated.");

        logger.info("*** Step 5 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "72944";//NEEDTOA;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'NEEDTOA', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 5.2 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0, "        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 5.4 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(1).toUpperCase() + " " + physInfoList.get(2).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);//addRecordIntoTable("Accn_Phys", listCol, insertValues, testDb);

        logger.info("*** Step 6.2 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 7 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        listCol.clear();
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
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(dosDt);
        insertValues.add(randomNum);
        insertValues.add("0");
        insertValues.add("3"); //TOA
        insertValues.add(prevDt);

        int qEpCorrespCount = daoManagerPlatform.addRecordIntoTable("Q_EP_CORRESP", listCol, insertValues, testDb);

        logger.info("*** Step 7.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertTrue(qEpCorrespCount > 0, "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 8 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = daoManagerAccnWS.getProcIdFromACCNPROCByAccnId(accnId, testDb).get(1);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);

        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

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

        logger.info("*** Step 8.2 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertTrue(qEpCorrespErrCount > 0, "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 9 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        listCol.clear();
        listCol.add("PK_CLN_ID");
        listCol.add("PK_XREF_ID");
        listCol.add("EFF_DT");

        insertValues.clear();
        insertValues.add(clnId);
        insertValues.add(xrefId);
        insertValues.add(prevDt);

        listVal.clear();
        listVal.add(clnId);
        listVal.add(xrefId);
        listVal.add("to_date('" + df1.format(prevDt) + "', 'MM/dd/yyyy')");

        ArrayList<String> clnXrefList = daoManagerPlatform.getClnXrefByConditon("pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        int clnXrefCount;
        if (clnXrefList.size() > 0) {
            clnXrefCount = daoManagerPlatform.updateRowInTable("CLN_XREF", listCol, listVal, "pk_cln_id = " + clnId + " and pk_xref_id = " + xrefId, testDb);
        } else {
            clnXrefCount = daoManagerPlatform.addRecordIntoTable("CLN_XREF", listCol, insertValues, testDb);
        }

        logger.info("*** Step 9.2 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        assertTrue(clnXrefCount > 0, "        A record should be added/updated into CLN_XREF table.");

        logger.info("*** Step 10 Actions: - Set up test data in ACCN_QUE table in DB");
        ArrayList<String> accnQueInfoList = daoManagerPlatform.getAccnQueByConditon("pk_accn_id = '" + accnId + "' and out_dt is null", testDb);
        String orgQCnt = accnQueInfoList.get(1);
        String orgQTyp = accnQueInfoList.get(3);
        String orgAudUser = accnQueInfoList.get(8);
        System.out.println("orgQTyp = " + orgQTyp);
        System.out.println("orgAudUser = " + orgAudUser);

        listCol.clear();
        listCol.add("FK_Q_TYP");

        listVal.clear();
        listVal.add("13"); //EP Correspondence

        logger.info("*** Step 10 Expected Results: - Verify that a record is updated in ACCN_QUE table in DB");
        int accnQueCount = daoManagerPlatform.updateRowInTable("ACCN_QUE", listCol, listVal, "pk_accn_id = '" + accnId + "' and pk_q_cnt = " + orgQCnt, testDb);
        assertTrue(accnQueCount > 0, "        A record should be updated into ACCN_QUE table.");

        logger.info("*** Step 11.0 Actions: - Wait for EP Letter Engine to process accn");
        assertTrue(isAccnInEPCorrrespProcessedByEngine(accnId, 0, QUEUE_WAIT_TIME_MS * 2));

        logger.info("*** Step 11.2 Expected Results: - Verify that two new records are added into CORRESP_FILE table in DB");
        String partialFileName1 = "ClientTOA_" + timeStamp.getCurrentDate("yyyyMMdd");
        ArrayList<String> correspFileInfoList1 = daoManagerPlatform.getCorrespFileInfoFromCORRESPFILEByfTypfPathfName("6", "epClient", partialFileName1, testDb);
        assertTrue(correspFileInfoList1.size() > 0, "        A new record for ClientTOA should be added into CORRESP_FILE table.");

        String fileName1 = correspFileInfoList1.get(5).trim();
        String correspFileSeqId1 = correspFileInfoList1.get(0);

        String partialFileName2 = "SummaryTOA_" + timeStamp.getCurrentDate("yyyyMMdd");
        ArrayList<String> correspFileInfoList2 = daoManagerPlatform.getCorrespFileInfoFromCORRESPFILEByfTypfPathfName("6", "epClient", partialFileName2, testDb);
        assertTrue(correspFileInfoList2.size() > 0, "        A new record for SummaryTOA should be added into CORRESP_FILE table.");

        String fileName2 = correspFileInfoList2.get(5).trim();
        String correspFileSeqId2 = correspFileInfoList2.get(0);

        logger.info("*** Step 11.4 Expected Results: - Verify that EP ClientTOA and SummaryTOA Letter are generated under /epClient/out/ folder");
        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput1 = new File(filePathOut + fileName1);
        System.out.println("File Path = " + filePathOut + fileName1);//Debug Info
        Assert.assertTrue(isFileExists(fOutput1, 5), "        EP ClientTOA file: " + fileName1 + " should be generated under " + filePathOut + " folder.");

        File fOutput2 = new File(filePathOut + fileName2);
        System.out.println("File Path = " + filePathOut + fileName2);//Debug Info
        Assert.assertTrue(isFileExists(fOutput2, 5), "        EP SummaryTOA file: " + fileName2 + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 11.6 Expected Results: - Verify that the record in Q_EP_CORRESP table is removed in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.size(), 0, "       The record in Q_EP_CORRESP should be removed.");

        logger.info("*** Step 11.8 Expected Results: - Verify that the ClientTOA PDF generated contains proper data");
        String pdfContentsStr1 = convertUtil.getTextFromPdf(1, 1, filePathOut + fileName1);
        String newPDFContentsStr = pdfContentsStr1.replaceAll("[\n\r]", "").replaceAll(" ", "").trim();
        assertTrue(newPDFContentsStr.contains(physName.replaceAll(" ", "").trim()), "        ClientTOA PDF file " + fileName1 + " should contains '" + physName + "' in Letter.");

        logger.info("*** Step 11.9 Expected Results: - Verify that the SummaryTOA PDF generated contains proper data");
        String pdfContentsStr2 = convertUtil.getTextFromPdf(1, 3, filePathOut + fileName2);
        assertTrue(pdfContentsStr2.contains(accnId), "        SummaryTOA PDF file " + fileName2 + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr2.contains("New York Medicaid TOA Forms"), "        SummaryTOA PDF file " + fileName2 + " should contains 'New York Medicaid TOA Forms' in Letter cover page.");
        assertTrue(pdfContentsStr2.contains("Summary of TOA Letters"), "        SummaryTOA PDF file " + fileName2 + " should contains 'Summary of TOA Letters' in Letter.");

        logger.info("*** Step 12 Actions: - Clear test data");
        fileManipulation.deleteFile(filePathOut, fileName1);
        fileManipulation.deleteFile(filePathOut, fileName2);
        updateSystemSetting(1356, "1", "1");
        daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING", "DATA_VALUE = 0", "PK_SETTING_ID", "1387", testDb);

        //Reset the cln id in accn table back to original cln id
        listCol.clear();
        listCol.add("fk_cln_id");

        listVal.clear();
        listVal.add(orgClnId);
        daoManagerPlatform.updateRowInTable("Accn", listCol, listVal, "Pk_accn_id = '" + accnId + "'", testDb);

        //Reset the q_typ in accn_que table back to the original q_typ and out_dt back to null
        listCol.clear();
        listCol.add("FK_Q_TYP");
        listCol.add("OUT_DT");
        listCol.add("AUD_USER");

        listVal.clear();
        listVal.add(orgQTyp);
        listVal.add("''");
        listVal.add("'" + orgAudUser + "'");

        accnQueCount = daoManagerPlatform.updateRowInTable("ACCN_QUE", listCol, listVal, "pk_accn_id = '" + accnId + "' and pk_q_cnt = " + orgQCnt, testDb);
        assertTrue(accnQueCount > 0, "        A record should be updated into ACCN_QUE table.");

        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PYR_ERR", " FK_ACCN_ID = '" + accnId + "'" + " and fk_err_cd = " + errCd, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId1, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId1, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("EP_CORRESP_HIST", " PK_CORRESP_FILE_SEQ =" + correspFileSeqId2, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CORRESP_FILE", " PK_FILE_SEQ =" + correspFileSeqId2, testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("Q_EP_CORRESP_ERR", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_CLN_Q", " PK_ACCN_ID = '" + accnId + "'", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("ACCN_PHYS", " FK_ACCN_ID = '" + accnId + "' and FK_ACCN_PHYS_TYP_ID = 1 and FK_PHYS_SEQ_ID  = '" + physSeqId + "' ", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_XREF", " PK_CLN_ID = " + clnId + " and pk_xref_id = " + xrefId, testDb);

        logger.info("*** Step 2 Actions: - Clear System Cache in Xifin Admin Portal");
        logIntoSso(ssoUsername, ssoPassword);
        xifinAdminUtils.clearDataCache();
    }

    @Test(priority = 1, description = "Generate Appeal Letter")
    @Parameters({"errCdId", "testSuite", "testCase", "project"})
    public void testPFER_525(String errCdId, String testSuite, String testCase, String project) throws Exception {
        logger.info("========== Testing - testPFER_525 ==========");
        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        updateSystemSetting(SystemSettingMap.SS_FR_PENDING_FOR_ELIG, "False", "0");
        updateSystemSetting(SystemSettingMap.SS_PTESTIMATION_SUBMITTED_BY_ELIGENGINE, "False", "0");

        logger.info("*** Actions: - Send WS request to create a new Priced 3rd party payor accession");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());

        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("        AccnID: " + accnId);

        logger.info("*** Expected Results: - Verify that a new accession was generated");
        assertNotNull(accnId, "        Did not generate a new Accession.");

        ArrayList<String> clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
        if (!clnBillingAssignInfoList.isEmpty()) {
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
        int errSeqId = databaseSequenceDao.getNextSequenceFromOracle("ACCN_PYR_ERR_SEQ");
        ape.setErrSeq(errSeqId);
        ape.setAccnId(accnId);
        ape.setPyrPrio(1);
        ape.setErrCd(Integer.parseInt(errCdId));
        ape.setErrDt(new java.sql.Date(sysDt.getTime()));
        ape.setIsPosted(true);
        accessionDao.setAccnPyrErr(ape);

        logger.info("*** Actions: - Update accn_que in DB");
        AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
        accnQue.setQTyp(9);//9: Q_EP_DENIAL
        accessionDao.setAccnQue(accnQue);

        logger.info("*** Actions: - Manually insert a record in Q_EP_DENIAL (9) queue in DB");
        Date todayDt = convertUtil.convertStringToUtilDate(timeStamp.getCurrentDate(), "MM/dd/yyyy");

        List<String> listCol = new ArrayList<>();
        listCol.add("PK_ACCN_ID");
        listCol.add("IN_DT");

        List<Object> insertValues = new ArrayList<>();
        insertValues.add(accnId);
        insertValues.add(todayDt);

        int qEpDenailCount = daoManagerPlatform.addRecordIntoTable("Q_EP_DENIAL", listCol, insertValues, testDb);

        logger.info("*** Expected Results: - Verify that a new record is added into Q_EP_DENIAL table in DB");
        assertTrue(qEpDenailCount > 0, "        A new record should be added into Q_EP_DENIAL table.");

        logger.info("*** Actions: - Wait for PF EP Workflow Engine");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_DENIAL, QUEUE_WAIT_TIME_MS));

        logger.info("*** Expected Results: - Verify that the accession is moved to E_QP_APPEAL_LTR (17) queue");
        assertNotNull(errorProcessingDao.getQEpAppealLtrByAccnId(accnId), "       Accession " + accnId + " should be in Q_EP_APPEAL_LTR table.");

        logger.info("*** Step 11.0 Actions: - Wait for EP Letter Engine to generate appeal letter");
        assertTrue(isAppealLetterGenerated(accnId, QUEUE_WAIT_TIME_MS * 2));

        List<EpCorrespHist> epCorrespHistList = errorProcessingDao.getEpCorrespHistByAccnId(accnId);
        EpCorrespHist latestEpCorrespHist = epCorrespHistList.get(0);

        CorrespFile correspFile = correspFileDao.getCorrespFileByFileSeq(latestEpCorrespHist.getCorrespFileSeq());
        assertNotNull(correspFile, "A new record should be added into CORRESP_FILE table.");

        String fileName = correspFile.getFileName().trim();

        logger.info("*** Expected Results: - Verify that an Appeal Letter is generated");
        assertTrue(fileName.contains("Appeal"), "        an Appeal Letter is generated.");

    }

    /**
     * Check if Appeal letter is generated
     *
     * @param accnId  accnId
     * @param maxTime max time to wait
     * @return true/false
     * @throws XifinDataNotFoundException Data not found
     * @throws XifinDataAccessException   data access error
     * @throws InterruptedException       exception
     */
    private boolean isAppealLetterGenerated(String accnId, long maxTime) throws XifinDataNotFoundException, XifinDataAccessException, InterruptedException {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isAppealGenerated = isEpCorrespHistRecordCreated(accnId);
        while (!isAppealGenerated && System.currentTimeMillis() < maxTime) {
            logger.info("Waiting for Engine to generate EP Letter to generate appeal letter, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            isAppealGenerated = isEpCorrespHistRecordCreated(accnId);
        }
        return isAppealGenerated;
    }

    /**
     * Check if EP Corresp Hist record is created
     *
     * @param accnId accn Id
     * @return true/false
     * @throws XifinDataAccessException   data access exception
     * @throws XifinDataNotFoundException record not found
     */
    private boolean isEpCorrespHistRecordCreated(String accnId) throws XifinDataAccessException, XifinDataNotFoundException {
        boolean appealGenerated = false;
        List<EpCorrespHist> epCorrespHistList = errorProcessingDao.getEpCorrespHistByAccnId(accnId);
        if (epCorrespHistList.size() > 0) {
            EpCorrespHist latestEpCorrespHist = epCorrespHistList.get(0);
            logger.info("Ep Corresp Hist, audUser=" + latestEpCorrespHist.getAudUser());
            if (latestEpCorrespHist.getAudUser().contains("EpLetterEngine")) {
                appealGenerated = true;
            }
        }
        return appealGenerated;
    }

    private boolean isAccnInEPCorrrespProcessedByEngine(String accnId, int ltr_cnt, long maxTime) throws Exception {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isLetterGenerated = accessionDao.isLetterGenerated(accnId, ltr_cnt);
        while (!isLetterGenerated && System.currentTimeMillis() < maxTime) {
            logger.info("Waiting for Engine to generate EP Letter, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            isLetterGenerated = accessionDao.isLetterGenerated(accnId, ltr_cnt);
        }
        return isLetterGenerated;
    }

    void cleanUpCln(List<String> clnAbbrevList) throws XifinDataAccessException {
        for (String clnAbbrev : clnAbbrevList) {
            Cln cln = clientDao.getClnByClnAbbrev(clnAbbrev);
            cln.setPullEpClnEffDt(null);
            cln.setPullEpClnEffDt(null);
            cln.setPullEpPtEffDt(null);
            cln.setPullEpPtExpDt(null);
            cln.setEpLetterSubmSvcId(0);
            cln.setIsEpLtrListMon(false);
            cln.setIsEpLtrListTues(false);
            cln.setIsEpLtrListWed(false);
            cln.setIsEpLtrListThurs(false);
            cln.setIsEpLtrListFri(false);
            cln.setIsEpLtrListSat(false);
            cln.setIsEpLtrListSun(false);
            clientDao.setCln(cln);
        }
    }
}

