package com.pfEngines.tests;


import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.clnSubm.ClnSubm;
import com.mbasys.mars.ejb.entity.clnSubmFile.ClnSubmFile;
import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.mbasys.mars.ejb.entity.submSvc.SubmSvc;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.xap.utils.XifinAdminUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ClientSubmissionEngineTest extends SeleniumBaseTest {

    private XifinAdminUtils xifinAdminUtils;

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            logger.info("Running AfterMethod");
            updateSystemSetting(SystemSettingMap.SS_SYSTEM_OVERRIDE_DATE, null, null);
        } catch (Exception e) {
            Assert.fail("Error running AfterMethod", e);
        } finally {
            quitWebDriver();
        }
    }

    @Test(priority = 1, description = "creates CLN_STATEMENT_STAGE, CLN_SUBM_FILE and SUBM_FILE records")
    public void testPFER_562() throws Exception {
        logger.info("====== Testing - PFER_562 ======");
        xifinAdminUtils = new XifinAdminUtils(driver, config);

        logger.info("*** Actions: - Setup test data by running the following script to simulate an EOM staged environment");
        String submDate = "07/31/2015";
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        java.sql.Date clnSubmDate = new java.sql.Date(format.parse(submDate).getTime());
        String systemDate = "08/01/2015";
        String prevDate = "06/30/2015";

        //Set accn
        Accn accn = accessionDao.getAccnForClnSubmStage();
        int clnId = accn.getClnId();
        accn.setClnSubmDt(clnSubmDate);
        accessionDao.setAccn(accn);

        //Set system_setting 1, 71, 1546
        updateSystemSetting(SystemSettingMap.SS_SYSTEM_OVERRIDE_DATE, systemDate, systemDate);
        updateSystemSetting(SystemSettingMap.SS_ACCOUNT_PERIOD_END_DATE, submDate, submDate);
        updateSystemSetting(SystemSettingMap.SS_SUBMISSION_PERIOD_DATE, prevDate, prevDate);

        //Set cln
        SubmSvc submSvc = submissionDao.getSubmSvcForClnSubm();
        String userId = usersDao.getRandomUserFromUsers().getUserId();
        int submSvcId = submSvc.getSubmSvcSeqId();
        String clnAbbrev = clientDao.getClnByClnId(clnId).getClnAbbrev();
        Cln cln = clientDao.getClnByClnAbbrev(clnAbbrev);
        cln.setClnErrLtrRespUsrId(userId);
        cln.setPullStmtClnEffDt(clnSubmDate);
        cln.setPullStmtClnExpDt(null);
        cln.setSubmSvcId(submSvcId);
        clientDao.setCln(cln);

        //Clear data in the related tables
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_STATEMENT_STAGE", "CLN_SUBM_DT = to_date('" + submDate + "','MM/dd/yyyy')", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_SUBM_FILE", "FK_SUBM_DT = to_date('" + submDate + "','MM/dd/yyyy')", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_BAL_HIST", "PK_SUBM_DT = to_date('" + submDate + "','MM/dd/yyyy')", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_ADJ", "FK_PRNT_SUBM_DT = to_date('" + submDate + "','MM/dd/yyyy')" + " or PK_SUBM_DT = to_date('" + submDate + "','MM/dd/yyyy')", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_PMT", "FK_PRNT_SUBM_DT = to_date('" + submDate + "','MM/dd/yyyy')", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_PMT", "pk_cln_id in (select pk_cln_id from cln_subm WHERE PK_SUBM_DT = to_date('" + submDate + "','MM/dd/yyyy'))", testDb);
        daoManagerPlatform.deleteDataFromTableByCondition("CLN_SUBM", "PK_SUBM_DT = to_date('" + submDate + "','MM/dd/yyyy')", testDb);

        //Set cln_subm
        RandomCharacter randomCharacter = new RandomCharacter(driver);
        double dueAmt = Double.valueOf(randomCharacter.getNonZeroRandomNumericString(2));
        double origAmt = Double.valueOf(randomCharacter.getNonZeroRandomNumericString(2));
        ClnSubm clnSubm = new ClnSubm();
        clnSubm.setClnId(clnId);
        clnSubm.setSubmDt(clnSubmDate);
        clnSubm.setDueAmt(dueAmt);
        clnSubm.setOrigAmt(origAmt);
        clnSubm.setSubmFilePageNum(0);
        rpmDao.setClnSubm(testDb, clnSubm);

        //Set cln_subm_file
        List<ClnSubmFile> clnSubmFileList = clientDao.getClnSubmFileBySubmDt(clnSubmDate);
        for (ClnSubmFile clnSubmFile : clnSubmFileList) {
            clnSubmFile.setResultCode(ErrorCodeMap.DELETED_RECORD);
            rpmDao.setClnSubmFile(testDb, clnSubmFile);
        }

        logger.info("*** Expected Results: - Verify that the EOM staged environment has been set up for a given submission date");
        clnSubmFileList = clientDao.getClnSubmFileBySubmDt(clnSubmDate);
        assertEquals(clnSubmFileList.size(), 0, "        Data is cleared in CLN_SUBM_FILE table for subm_dt = " + clnSubmDate);

        List<SubmFile> submFileList = submissionDao.getSubmFileByClnSubmFileSumDt(clnSubmDate);
        assertEquals(submFileList.size(), 0, "        Data is cleared in SUBM_FILE table for the subm files in cln_subm_file.fk_subm_dt = " + clnSubmDate);

        int clnStatementStageCount = clientDao.getCountFromClnStatementStageBySubmDt(clnSubmDate);
        assertEquals(clnStatementStageCount, 0, "        Data is cleared in CLN_STATEMENT_STAGE table for cln_subm_dt = " + submDate);

        logger.info("*** Actions: - Run PF-Client Submission Engine");
        clientDao.waitForClientSubmissionEngine(clnSubmDate, QUEUE_WAIT_TIME_MS);

        logger.info("*** Expected Results: - Verify that CLN_SUBM_FILE and SUBM_FILE records are created for the given submission date.");
        clnSubmFileList = clientDao.getClnSubmFileBySubmDt(clnSubmDate);
        assertTrue(clnSubmFileList.size() > 0, "        Data is created in CLN_SUBM_FILE table for subm_dt = " + clnSubmDate);

        submFileList = submissionDao.getSubmFileByClnSubmFileSumDt(clnSubmDate);
        assertTrue(submFileList.size() > 0, "        Data is created in SUBM_FILE table for the subm files in cln_subm_file.fk_subm_dt = " + clnSubmDate);

        logger.info("*** Expected Results: - Verify that CLN_STATEMENT_STAGE records are created for the given submission date");
        int i = 0;
        while (clnStatementStageCount == 0 && i < 120) {
            clnStatementStageCount = clientDao.getCountFromClnStatementStageBySubmDt(clnSubmDate);
            logger.info("waiting for submDt = " + clnSubmDate + " in ClnStstementStage ");
            Thread.sleep(3000);
            i++;
        }

        assertTrue(clnStatementStageCount > 0, "        Data is created in CLN_STATEMENT_STAGE table for cln_subm_dt = " + submDate);

    }
}
