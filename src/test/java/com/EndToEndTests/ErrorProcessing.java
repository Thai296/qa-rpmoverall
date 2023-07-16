package com.EndToEndTests;

import com.mbasys.common.utility.Money;
import com.mbasys.mars.ejb.entity.accnClnQ.AccnClnQ;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.clnXref.ClnXref;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.rpm.domain.QEpCoresp;
import com.xifin.qa.dao.rpm.domain.QEpCorrespErr;
import com.xifin.util.DateConversion;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.xifin.utils.*;
import com.xifin.xap.utils.XifinAdminUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;
import static org.testng.Assert.assertTrue;

public class ErrorProcessing extends EndToEndBaseTest {
    ConvertUtil convertUtil;
    RandomCharacter randomCharacter;
    TimeStamp timeStamp;
    @Test(priority = 1, description = "Getting into EP unpriceable")
    @Parameters({"email", "password", "project", "testSuite", "testCase"})
    public void gettingIntoEPUnpriceable(String email, String password, String project, String testSuite, String testCase) throws Exception {
        String accnId = createAccn(email, password, project, testSuite, testCase);

        logger.info("*** Step 2 Expected Result - Ensure AccnId is in Q_EP_Unpriceable");
        Assert.assertTrue(accessionDao.isAccnInQueueByQTyp(accnId, AccnStatusMap.Q_EP_UNPRICEABLE), " Accession is in Q_EP_UNPRICEABLE");
    }

    @Test(priority = 1, description = "Generate letter with Type fax")
    @Parameters({"email", "password", "project", "testSuite", "testCase", "formatType"})
    public void generateLetterWithTypeFax(String email, String password, String project, String testSuite, String testCase, String formatType) throws Exception {
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Action - Create new Third Party Payor Accession");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("accnId "+accnId);
        String clnAbbrev = testProperties.getProperty("ClnAbbrev");
        logger.info(clnAbbrev);

        logger.info("*** Step 1 Expected Results - Verify Accession is created");
        Assert.assertNotNull(accnId, " Accession is not generated");
        logger.info(" accession Id = " + accnId);

        logger.info("*** Step 2 Action - Run OE Posting Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "OePostingEngine", "SSO_APP_STAGING", false);

        logger.info("*** Step 3 Action - Run Pricing Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "PricingEngine", "SSO_APP_STAGING", true);

        logger.info("*** Step 3 Expected Result - Ensure AccnId is processed by Pricing Engine");
        Assert.assertFalse(accessionDao.isAccnInQueueByQTyp(accnId, AccnStatusMap.Q_PRICE), " Accession is not in Q_PRICE");

        logger.info("*** Step 4 Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);

        logger.info("*** Step 5 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =9 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0", testDb).get(0);
        String correspFaxNum = "8585551234";
        cln.setEpLetterSubmSvcId(Integer.parseInt(epLetterSubSvcSeqId));
        cln.setIsEpLtrListMon(true);
        cln.setIsEpLtrListTues(true);
        cln.setIsEpLtrListWed(true);
        cln.setIsEpLtrListThurs(true);
        cln.setIsEpLtrListFri(true);
        cln.setIsEpLtrListSat(true);
        cln.setCorrespFax(correspFaxNum);
        rpmDao.setCln(testDb, cln);
        cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);

        logger.info("*** Step 5 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue( correspFaxNum.equals(cln.getCorrespFax())  ,"        Client with clnAbbrev: " + clnAbbrev + " should be updated.");

        logger.info("*** Step 6 Actions: - Set up test data in ACCN_CLN_Q and ACCN tables in DB");
        List<String> accnForEPLetterEngineList = daoManagerPlatform.getAccnForEPLetterEngine(testDb);
        accnId = accnForEPLetterEngineList.get(0);
        int rowCountInAccnClnQ = 0;
        List<String> accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        if(accnClnQList.size()==0){
            AccnClnQ accnClnQ = new AccnClnQ();
            accnClnQ.setAccnId(accnId);
            accnClnQ.setQuestnId(14);
            accnClnQ.setRespns(randomCharacter.getRandomAlphaNumericString(10));
            accnClnQ.setIsDeleted(false);
            accessionDao.setAccnClnQ(accnClnQ);
            accnClnQList = daoManagerAccnWS.getAccnFromACCN_CLN_Q(accnId, testDb);
        }
        rowCountInAccnClnQ = accnClnQList.size();

        accessionDao.updateAccnSetClnIdAndPrimClnId(cln.clnId, accnId);

        logger.info("*** Step 6 Expected Results: - Verify that the ACCN_CLN_Q and ACCN table is updated in DB");
        assertTrue(rowCountInAccnClnQ > 0,"        Accession " + accnId + " should have record in ACCN_CLN_Q table.");

        logger.info("*** Step 7 Actions: - Set up test data in ACCN_PYR_ERR table in DB");
        String errCd = "55342";//CR140;//errCdInfoList.get(0);// Choose err_cd.abbrv = 'CR140', not all error code is going to work
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = DateConversion.parseMMDDYYY(currDt);
        int accnPyrErrCount = daoManagerPlatform.addAccnPyrErr("ACCN_PYR_ERR_SEQ.nextval", accnId, "1", errCd, "to_date('" + currDt + "', 'MM/dd/yyyy')", "''", "1", testDb);

        logger.info("*** Step 7 Expected Results: - Verify that the ACCN_PYR_ERR table is updated in DB");
        assertTrue(accnPyrErrCount > 0,"        A new record should be added into Accn_Pyr_err table.");

        logger.info("*** Step 7 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0,"        A record should show in EP_LETTER_V.");

        logger.info("*** Step 8 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);

        logger.info("*** Step 8 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0,"        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 9 Actions: - Set up test data in Q_EP_CORRESP table in DB");
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        String randomNum = randomCharacter.getNonZeroRandomNumericString(2);

        ArrayList<String> accnInfoList = daoManagerPlatform.getAccnInfoFromACCNByAccnId(accnId, testDb);

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dosDt = convertUtil.convertStringToUtilDate(accnInfoList.get(4), "yyyy-MM-dd HH:mm:ss");

        QEpCoresp qEpCoresp = new QEpCoresp();
        qEpCoresp.setAccnId(accnId);
        qEpCoresp.setUserId("qatester");
        qEpCoresp.setErrGrpAccn(1);
        qEpCoresp.setClnId(cln.clnId);

        java.sql.Date sqlDate = new java.sql.Date(dosDt.getTime());
        java.sql.Timestamp timestamp = new java.sql.Timestamp(sqlDate.getTime());
        qEpCoresp.setInDt(timestamp);
        qEpCoresp.setAssgnDt(sqlDate);
        qEpCoresp.setDosDt(sqlDate);
        qEpCoresp.setDueAmtFromErrors(new Money( randomNum));
        qEpCoresp.setLtrCnt(0);
        qEpCoresp.setLtrDest(2);
        java.sql.Date sqlprevDt = new java.sql.Date(prevDt.getTime());
        qEpCoresp.setNxtLtrDt(sqlprevDt);
        errorProcessingDao.setQEpCorresp(qEpCoresp);

        logger.info("*** Step 9 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertNotNull(errorProcessingDao.getQEpCorrespByAccnId(accnId),"        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 10 Actions: - Set up test data in Q_EP_CORRESP_ERR table in DB");
        String accnProcSeqId = Integer.toString(accessionDao.getAccnProcByAccnId(accnId).accnProcSeqId);
        String pyrId = daoManagerPlatform.getAccnPyrFromACCNPYRByAccnId(accnId, testDb).get(0).get(0);
        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);
        String pyrGrpId = daoManagerPlatform.getPyrInfoFromPYRByPyrAbbrev(pyrAbbrev, testDb).get(1);
        String errGrpId = daoManagerPlatform.getErrCdInfoFromERRCDByCondition("where pk_err_cd = " + errCd, testDb).get(1);

        QEpCorrespErr qEpCorrespErr = new QEpCorrespErr();
        qEpCorrespErr.setAccnId(accnId);
        qEpCorrespErr.setErrCd(Integer.parseInt(errCd));
        qEpCorrespErr.setPyrPrio(1);
        qEpCorrespErr.setPyrId(Integer.parseInt(pyrId));
        qEpCorrespErr.setPyrGrpId(Integer.parseInt(pyrGrpId));
        qEpCorrespErr.setErrGrpId(Integer.parseInt(errGrpId));
        qEpCorrespErr.setPseudoAccnProcSeqId(Integer.parseInt(accnProcSeqId));
        java.sql.Date sqlsysDt = new java.sql.Date(sysDt.getTime());
        qEpCorrespErr.setErrDt(sqlsysDt);
        qEpCorrespErr.setDosDt(sqlDate);
        qEpCorrespErr.setDueAmtByError(new Money( randomNum));
        qEpCorrespErr.setRelevant(false);
        errorProcessingDao.setQEpCorrespErr(qEpCorrespErr);

        logger.info("*** Step 10 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertNotNull(errorProcessingDao.getQEpCorrespErrByAccnId(accnId) ,"        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 11 Actions: - Set up test data in CLN_XREF table in DB");
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);

        ClnXref clnXref = clientDao.getClnXrefByClnIdAndXrefId(cln.clnId,Integer.parseInt(xrefId));
        clnXref.setClnId(cln.clnId);
        clnXref.setXrefId(Integer.parseInt(xrefId));
        clnXref.setEffDt(sqlprevDt);
        clientDao.setClnXref(clnXref);

        logger.info("*** Step 11 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        clnXref = clientDao.getClnXrefByClnIdAndXrefId(cln.clnId,Integer.parseInt(xrefId));

        assertTrue((clnXref.getEffDt()).equals(sqlprevDt),"        A record should be added/updated into CLN_XREF table.");
        logger.info("*** Step 6 Expected Result - Run EP Letter Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "EpLetterEngine", "SSO_APP_STAGING", true);

        logger.info("*** Step 11 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0,"        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 11 Expected Results: - Verify that EP FAX Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("FAX"),"        a EP FAX Letter is generated.");
        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        logger.info("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP FAX file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 11 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 11 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut+fileName);
        assertTrue(pdfContentsStr.contains(accnId),"        PDF file " + fileName + " should contains Accession ID " + accnId);
        assertTrue(pdfContentsStr.contains("Fax Number: (858) 555-1234"),"        PDF file " + fileName + " should contains 'Fax Number: (858) 555-1234' in Letter cover page.");
        assertTrue(pdfContentsStr.contains("Dear " + physName),"        PDF file " + fileName + " should contains 'Dear " + physName + "' in Letter.");
    }

    @Test(priority = 1, description = "Generate letter with Type fax and action Client")
    @Parameters({"email", "password", "project", "testSuite", "testCase", "formatType"})
    public void generateLetterWithTypeFax_action_client(String email, String password, String project, String testSuite, String testCase, String formatType) throws Exception {
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        fileManipulation = new FileManipulation(driver);
        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Step 1 Action - Create new Third Party Payor Accession");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("accnId " + accnId);
        String clnAbbrev = testProperties.getProperty("ClnAbbrev");
        logger.info(clnAbbrev);

        logger.info("*** Step 1 Expected Results - Verify Accession is created");
        Assert.assertNotNull(accnId, " Accession is not generated");
        logger.info(" accession Id = " + accnId);

        logger.info("*** Step 2 Action - Run OE Posting Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "OePostingEngine", "SSO_APP_STAGING", false);

        logger.info("*** Step 3 Action - Check if accn is in QElig and then run Elig engine");
        boolean qElig = accessionDao.isInEligibilityQueue(accnId);
        if(qElig) {
            xifinAdminUtils.runPFEngine(this, email, password, null, "EligEngine", "SSO_APP_STAGING", true);
        }

        logger.info("*** Step 4 Expected Result - Ensure AccnId is processed by Elig Engine");
        Assert.assertFalse(accessionDao.isAccnInQueueByQTyp(accnId, AccnStatusMap.Q_ELIG), " Accession is still in Q_ELIG");

        logger.info("*** Step 5 Action - Run Pricing Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "PricingEngine", "SSO_APP_STAGING", true);

        logger.info("*** Step 5 Expected Result - Ensure AccnId is processed by Pricing Engine");
        Assert.assertFalse(accessionDao.isAccnInQueueByQTyp(accnId, AccnStatusMap.Q_PRICE), " Accession is not in Q_PRICE");

        logger.info("*** Step 6 Actions: - Setup Client test data in DB");
        Cln cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpClnEffDt(null);
        cln.setPullEpPtEffDt(null);
        cln.setPullEpPtExpDt(null);
        rpmDao.setCln(testDb, cln);

        logger.info("*** Step 7 Actions: - Set up test data in CLN table in DB");
        String epLetterSubSvcSeqId = daoManagerPlatform.getSubmSvcInfoFromSUBMSVCByCondition("where Fk_Delvry_Mthd_Typ_Id =2 and pk_subm_svc_seq_id > 0 and b_mon = 1 and b_tues = 1 and b_wed = 1 and b_thurs = 1 and b_fri = 1 and b_sat = 1 and b_sun = 1 and b_production =1 and b_supported = 1 and b_pull = 0 and FK_DATA_FMT_TYP_ID = 0", testDb).get(0);
        String correspFaxNum = "8585551234";
        cln.setEpLetterSubmSvcId(Integer.parseInt(epLetterSubSvcSeqId));
        cln.setIsEpLtrListMon(true);
        cln.setIsEpLtrListTues(true);
        cln.setIsEpLtrListWed(true);
        cln.setIsEpLtrListThurs(true);
        cln.setIsEpLtrListFri(true);
        cln.setIsEpLtrListSat(true);
        cln.setCorrespFax(correspFaxNum);
        rpmDao.setCln(testDb, cln);
        cln = rpmDao.getClnByClnAbbrev(testDb, clnAbbrev);

        logger.info("*** Step 7 Expected Results: - Verify that the CLN table is updated in DB");
        assertTrue(correspFaxNum.equals(cln.getCorrespFax()), "        Client with clnAbbrev: " + clnAbbrev + " should be updated.");

        logger.info("*** Step 8 Expected Results: - Verify that the record shows in EP_LETTER_V in DB");
        String errCd = "94784";
        List<String> epLetterVList = daoManagerPlatform.getEPLetterVInfoByAccnIdErrCd(accnId, errCd, testDb);
        assertTrue(epLetterVList.size() > 0, "        A record should show in EP_LETTER_V.");

        logger.info("*** Step 9 Actions: - Set up test data in ACCN_PHYS table in DB");
        ArrayList<String> physInfoList = daoManagerPlatform.getPhysicianInfoFromPHYS(testDb);
        String physSeqId = physInfoList.get(0);
        String physName = physInfoList.get(2).toUpperCase() + " " + physInfoList.get(1).toUpperCase();

        int accnPhysCount = daoManagerPlatform.addAccnPhys("ACCN_PHYS_SEQ.nextval", accnId, physSeqId, "1", "0", testDb);

        logger.info("*** Step 9 Expected Results: - Verify that a new record is added into ACCN_PHYS table in DB");
        assertTrue(accnPhysCount > 0, "        A new record should be added into ACCN_PHYS table.");

        logger.info("*** Step 9 Expected Results: - Verify that a new record is added into Q_EP_CORRESP table in DB");
        assertNotNull(errorProcessingDao.getQEpCorrespByAccnId(accnId), "        A new record should be added into Q_EP_CORRESP table.");

        logger.info("*** Step 9 Expected Results: - Verify that a new record is added into Q_EP_CORRESP_ERR table in DB");
        assertNotNull(errorProcessingDao.getQEpCorrespErrByAccnId(accnId), "        A new record should be added into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 10 Actions: - Set up test data in CLN_XREF table in DB");
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = DateConversion.parseMMDDYYY(currDt);
        Date prevDt = convertUtil.convertStringToUtilDate(timeStamp.getPreviousMonth("MM/dd/yyyy", sysDt, 1), "MM/dd/yyyy");
        java.sql.Date sqlprevDt = new java.sql.Date(prevDt.getTime());
        String xrefId = daoManagerPlatform.getXrefByCondition("WHERE FK_XREF_TYP_ID = 5 AND B_DELETED = 0", testDb).get(0);
        ClnXref clnXref = clientDao.getClnXrefByClnIdAndXrefId(cln.clnId, Integer.parseInt(xrefId));
        clnXref.setClnId(cln.clnId);
        clnXref.setXrefId(Integer.parseInt(xrefId));
        clnXref.setEffDt(sqlprevDt);
        clientDao.setClnXref(clnXref);

        logger.info("*** Step 10 Expected Results: - Verify that a record is added/updated into CLN_XREF table in DB");
        clnXref = clientDao.getClnXrefByClnIdAndXrefId(cln.clnId, Integer.parseInt(xrefId));

        assertTrue((clnXref.getEffDt()).equals(sqlprevDt), "        A record should be added/updated into CLN_XREF table.");

        logger.info("*** Step 11 - Run EP Letter Engine");
        xifinAdminUtils.runPFEngine(this, email, password, null, "EpLetterEngine", "SSO_APP_STAGING", true);

        logger.info("*** Step 11 Expected Results: - Verify that a new record is added into CORRESP_FILE table in DB");
        String correspFileSeqId = daoManagerXifinRpm.getEPCorrespHistFromEPCORRESPHISTByAccnId(accnId, testDb).get(0);
        ArrayList<String> correspFileInfoList = daoManagerXifinRpm.getCorrespFileInfoFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        assertTrue(correspFileInfoList.size() > 0, "        A new record should be added into CORRESP_FILE table.");

        String fileName = correspFileInfoList.get(6).trim();

        logger.info("*** Step 11 Expected Results: - Verify that EP FAX Letter is generated under /epClient/out/ folder");
        assertTrue(fileName.contains("PSC"), "        a EP FAX Letter is generated.");
        String filePathOut = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File fOutput = new File(filePathOut + fileName);
        logger.info("File Path = " + filePathOut + fileName);//Debug Info
        Assert.assertTrue(isFileExists(fOutput, 5), "        EP FAX file: " + fileName + " should be generated under " + filePathOut + " folder.");

        logger.info("*** Step 11 Expected Results: - Verify that the record in Q_EP_CORRESP table is updated in DB");
        ArrayList<String> qEpCorrespInfoList = daoManagerPlatform.getQEpCorrespInfoByAccnId(accnId, testDb);
        assertEquals(qEpCorrespInfoList.get(7), "1", "       Q_EP_CORRESP_ERR.LTR_CNT should be updated to '1'.");

        logger.info("*** Step 11 Expected Results: - Verify that the PDF generated contains proper data");
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 2, filePathOut + fileName);
        assertTrue(pdfContentsStr.contains(accnId), "        PDF file " + fileName + " should contains Accession ID " + accnId);
    }
}