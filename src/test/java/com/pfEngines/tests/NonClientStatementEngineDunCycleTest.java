package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accnPromsPmt.AccnPromsPmt;
import com.mbasys.mars.ejb.entity.ptNotifPlan.PtNotifPlan;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrDun.PyrDun;
import com.mbasys.mars.ejb.entity.qAccnSubm.QAccnSubm;
import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.mbasys.mars.ejb.entity.submSvc.SubmSvc;
import com.mbasys.mars.persistance.MiscMap;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.xifin.util.DateConversion;
import com.xifin.util.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NonClientStatementEngineDunCycleTest extends NonClientStatementEngineTest
{
    /**
     * No Promise Payment, Dun Is Incremented
     * <p>
     * Preconditions Accession has no active Promised Payment plan.
     * <p>
     * Generate a patient statement for the first dunning cycle
     * <p>
     * Verify that a record is queued for dunning cycle #2 for the correct future date (based on the dunning cycle
     * configuration).
     *
     * @param accnId          Accession ID
     * @param patientPyrAbbrv Patient payor abbrv
     * @param subId           Subscriber ID
     * @param submSvcAbbrev   Subm svc abbrev
     * @param docSeqIdsStr    Doc Seq ID
     * @throws Exception Exception
     */
    @Test(priority = 1, description = "No Promise Payment, Dun Is Incremented")
    @Parameters({"accnId", "patientPyrAbbrv", "subId", "submSvcAbbrev", "docSeqIdsStr"})
    public void testPFER_739(String accnId, String patientPyrAbbrv, String subId, String submSvcAbbrev, String docSeqIdsStr) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-739");
        List<String> docSeqIdsStringList = Arrays.asList(StringUtils.split(docSeqIdsStr));
        int pyrId = rpmDao.getPyrByPyrAbbrv(null, patientPyrAbbrv).getPyrId();
        cleanQASForPyrOnAccnExceptDocSeqIds(accnId, pyrId, docSeqIdsStringList);

        logger.info("Verifying there is no promised payment plan on accession, accnId=" + accnId);
        List<AccnPromsPmt> accnPromsPmts = accessionDao.getAccnPromsPmts(accnId);
        Assert.assertEquals(accnPromsPmts.size(), 0, "AccnPromPmts expected size not found");
        //AccnPromsPmt accnPromsPmt = accnPromsPmts.get(0);

        accessionDetail.submitClaimsOnAccnDetail(accnId, patientPyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(null, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(null, rpmDao.getSubmSvcByAbbrev(null, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to create the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(null, submFileSeqId);
        waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        submFile = rpmDao.getSubmFile(null, submFileSeqId);
        Assert.assertTrue(submFile.getIsEgateProcessed(), "Expected b_gate_processed = 1, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure 2 QAS record are there");
        qasList = rpmDao.getQAccnSubm(null, accnId);
        Assert.assertEquals(qasList.size(), 2);

        logger.info("Make sure processed QAS record has subm_file_seq_id > 0");
        Assert.assertNotEquals(qasList.get(0).getSubmFileSeqId(), 0);

        logger.info("Make sure new QAS record has correct values");
        Assert.assertEquals(qasList.get(1).getSubmFileSeqId(), 0, "Make sure new QAS record has subm_file_seq_id = 0");
        Assert.assertEquals(qasList.get(1).getSubmCnt(), 2, "Make sure new QAS record has subm_cnt = 2");
        Assert.assertEquals(qasList.get(0).getSubmSvcSeqId(), qasList.get(1).getSubmSvcSeqId(), "Make sure new QAS record has same subm_svc");
        Assert.assertEquals(qasList.get(1).getNextSubmDt().toString(), DateConversion.addDays(DateConversion.getNowAsSqlDate(), 30).toString(), "Make sure new QAS record next_subm_dt is 30 days from now");
    }

    /**
     * No Promise Payment, Correct Submission Service Is Used
     * <p>
     * Preconditions Accession has no active Promised Payment Plan
     * <p>
     * Generate a patient statement for the second dunning cycle, where dunning cycle #3 specifies a Collections
     * submission service.
     * <p>
     * Verify that a record is queued for dunning cycle #3 for the correct future date (based on the dunning cycle
     * configuration), and that the submission service ID is the Collections submission service.
     *
     * @param accnId          Accession ID
     * @param patientPyrAbbrv Patient payor abbrv
     * @param subId           Subscriber ID
     * @param submSvcAbbrev   Subm svc abbrev
     * @param docSeqIdsStr    Doc Seq ID
     * @throws Exception Exception
     */
    @Test(priority = 1, description = "No Promise Payment, Correct Submission Service Is Used")
    @Parameters({"accnId", "patientPyrAbbrv", "subId", "submSvcAbbrev", "docSeqIdsStr"})
    public void testPFER_740(String accnId, String patientPyrAbbrv, String subId, String submSvcAbbrev, String docSeqIdsStr) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-740");
        List<String> docSeqIdsStringList = Arrays.asList(StringUtils.split(docSeqIdsStr));
        int pyrId = rpmDao.getPyrByPyrAbbrv(null, patientPyrAbbrv).getPyrId();
        cleanQASForPyrOnAccnExceptDocSeqIds(accnId, pyrId, docSeqIdsStringList);

        logger.info("Verifying there is no promised payment plan on accession, accnId=" + accnId);
        List<AccnPromsPmt> accnPromsPmts = accessionDao.getAccnPromsPmts(accnId);
        Assert.assertEquals(accnPromsPmts.size(), 0, "AccnPromPmts expected size not found");
        //AccnPromsPmt accnPromsPmt = accnPromsPmts.get(0);

        accessionDetail.submitClaimsOnAccnDetail(accnId, patientPyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(null, accnId);
        Assert.assertEquals(qasList.size(), 2);
        Assert.assertEquals(qasList.get(0).getSubmCnt(), 1);
        if (qasList.get(1).getSubmFileSeqId() > 0 || qasList.get(1).getSubmCnt() != 2)
        {
            qasList.get(1).setSubmCnt(2);
            qasList.get(1).setNextSubmDt(DateConversion.getNowAsSqlDate());
            qasList.get(1).setSubmFileSeqId(0);
            submissionDao.setQAccnSubm(qasList.get(1));
        }

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(null, rpmDao.getSubmSvcByAbbrev(null, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(1), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to create the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(null, submFileSeqId);
        waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        submFile = rpmDao.getSubmFile(null, submFileSeqId);
        Assert.assertTrue(submFile.getIsEgateProcessed(), "Expected b_gate_processed = 1, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure 3 QAS record are there");
        qasList = rpmDao.getQAccnSubm(null, accnId);
        Assert.assertEquals(qasList.size(), 3);

        logger.info("Make sure processed QAS record has subm_file_seq_id > 0");
        Assert.assertNotEquals(qasList.get(1).getSubmFileSeqId(), 0);

        logger.info("Make sure new QAS record has correct values");
        Assert.assertEquals(qasList.get(2).getSubmFileSeqId(), 0, "Make sure new QAS record has subm_file_seq_id = 0");
        Assert.assertEquals(qasList.get(2).getSubmCnt(), 3, "Make sure new QAS record has subm_cnt = 3");
        Assert.assertNotEquals(qasList.get(1).getSubmSvcSeqId(), qasList.get(2).getSubmSvcSeqId(), "Make sure new QAS record not same subm_svc");
        Assert.assertEquals(qasList.get(2).getNextSubmDt().toString(), DateConversion.addDays(DateConversion.getNowAsSqlDate(), 60).toString(), "Make sure new QAS record next_subm_dt is 30 days from now");
    }

    /**
     * Active Promise Payment Plan Created After Last Statement
     * <p>
     * Preconditions Accession has an active Promise Payment plan which was set up after the previous statement was
     * created
     * <p>
     * Generate a patient statement for the first dunning cycle, where dunning cycle #3 specifies a Collections
     * submission service.
     * <p>
     * Verify that: A record is queued for the correct future date (based on the dunning cycle configuration), and that
     * the dunning count has not been incremented (2) and the submission service has not been changed (i.e. is NOT the
     * Collections submission service). The due amount on the statement is the minimum payment amount specified by the
     * payment plan
     *
     * @param accnId          Accession ID
     * @param patientPyrAbbrv Patient payor abbrv
     * @param subId           Subscriber ID
     * @param submSvcAbbrev   Subm svc abbrev
     * @param docSeqIdsStr    Doc Seq ID
     * @throws Exception Exception
     */
    @Test(priority = 1, description = "Active Promise Payment Plan Created After Last Statement")
    @Parameters({"accnId", "patientPyrAbbrv", "subId", "submSvcAbbrev", "docSeqIdsStr"})
    public void testPFER_741(String accnId, String patientPyrAbbrv, String subId, String submSvcAbbrev, String docSeqIdsStr) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-741");
        List<String> docSeqIdsStringList = Arrays.asList(StringUtils.split(docSeqIdsStr));
        int pyrId = rpmDao.getPyrByPyrAbbrv(null, patientPyrAbbrv).getPyrId();
        cleanQASForPyrOnAccnExceptDocSeqIds(accnId, pyrId, docSeqIdsStringList);

        logger.info("Verifying there is no promised payment plan on accession, accnId=" + accnId);
        List<AccnPromsPmt> accnPromsPmts = accessionDao.getAccnPromsPmts(accnId);
        Assert.assertEquals(accnPromsPmts.size(), 1, "AccnPromPmts expected size not found");
        AccnPromsPmt accnPromsPmt = accnPromsPmts.get(0);
        accnPromsPmt.setVoidUserId("qatester");
        accnPromsPmt.setPromsPmtSta(4);
        accessionDao.setAccnPromsPmt(accnPromsPmt);

        accessionDetail.submitClaimsOnAccnDetail(accnId, patientPyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(null, accnId);
        Assert.assertEquals(qasList.size(), 2);
        Assert.assertEquals(qasList.get(0).getSubmCnt(), 1);
        if (qasList.get(1).getSubmFileSeqId() > 0 || qasList.get(1).getSubmCnt() != 2)
        {
            qasList.get(1).setSubmCnt(2);
            qasList.get(1).setNextSubmDt(DateConversion.getNowAsSqlDate());
            qasList.get(1).setSubmFileSeqId(0);
            submissionDao.setQAccnSubm(qasList.get(1));
        }

        accnPromsPmts = accessionDao.getAccnPromsPmts(accnId);
        Assert.assertEquals(accnPromsPmts.size(), 1, "AccnPromPmts expected size not found");
        accnPromsPmt = accnPromsPmts.get(0);
        accnPromsPmt.setVoidUserId("");
        accnPromsPmt.setPromsPmtSta(1);
        accessionDao.setAccnPromsPmt(accnPromsPmt);

        logger.info("Verify info on promised payment");
        SubmFile submFile = rpmDao.getSubmFile(null, qasList.get(0).getSubmFileSeqId());
        Assert.assertTrue(accnPromsPmt.getCntctDt().compareTo(submFile.getFileCreatDt()) > 0, "Expected cntctDt (" + accnPromsPmt.getCntctDt() + ") > fileCreatDt (" + submFile.getFileCreatDt() + ")");

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(null, rpmDao.getSubmSvcByAbbrev(null, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(1), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to create the record from QAS");
        submFile = rpmDao.getSubmFile(null, submFileSeqId);
        waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        submFile = rpmDao.getSubmFile(null, submFileSeqId);
        Assert.assertTrue(submFile.getIsEgateProcessed(), "Expected b_gate_processed = 1, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure 3 QAS record are there");
        qasList = rpmDao.getQAccnSubm(null, accnId);
        Assert.assertEquals(qasList.size(), 3);

        logger.info("Make sure processed QAS record has subm_file_seq_id > 0");
        Assert.assertNotEquals(qasList.get(1).getSubmFileSeqId(), 0);

        logger.info("Make sure new QAS record has correct values");
        Assert.assertEquals(qasList.get(2).getSubmFileSeqId(), 0, "Make sure new QAS record has subm_file_seq_id = 0");
        Assert.assertEquals(qasList.get(2).getSubmCnt(), 2, "Make sure new QAS record has subm_cnt = 2");
        Assert.assertEquals(qasList.get(1).getSubmSvcSeqId(), qasList.get(2).getSubmSvcSeqId(), "Make sure new QAS record has same subm_svc");
        Assert.assertEquals(qasList.get(2).getNextSubmDt().toString(), DateConversion.addDays(DateConversion.getNowAsSqlDate(), 30).toString(), "Make sure new QAS record next_subm_dt is 30 days from now");
    }

    /**
     * Active Promise Payment Plan, Minimum Payment Met
     * <p>
     * Preconditions: Accession has an active Promise Payment plan which was set up prior to the previous statement date
     * Patient payments have been made since the previous statement date which meet the minimum amount specified by the
     * payment plan
     * <p>
     * Generate a patient statement for the first dunning cycle, where dunning cycle #3 specifies a Collections
     * submission service.
     * <p>
     * Verify that: A record is queued for the correct future date (based on the dunning cycle configuration), and that
     * the dunning count has not been incremented (2) and the submission service has not been changed (i.e. is NOT the
     * Collections submission service). The due amount on the statement is the minimum due amount specified by the
     * payment plan.
     *
     * @param accnId          Accession ID
     * @param patientPyrAbbrv Patient payor abbrv
     * @param subId           Subscriber ID
     * @param submSvcAbbrev   Subm svc abbrev
     * @param docSeqIdsStr    Doc Seq ID
     * @throws Exception Exception
     */
    @Test(priority = 1, description = "Active Promise Payment Plan, Minimum Payment Met")
    @Parameters({"accnId", "patientPyrAbbrv", "subId", "submSvcAbbrev", "docSeqIdsStr"})
    public void testPFER_744(String accnId, String patientPyrAbbrv, String subId, String submSvcAbbrev, String docSeqIdsStr) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-744");
        List<String> docSeqIdsStringList = Arrays.asList(StringUtils.split(docSeqIdsStr));
        int pyrId = rpmDao.getPyrByPyrAbbrv(null, patientPyrAbbrv).getPyrId();
        cleanQASForPyrOnAccnExceptDocSeqIds(accnId, pyrId, docSeqIdsStringList);

        logger.info("Verifying there is no promised payment plan on accession, accnId=" + accnId);
        List<AccnPromsPmt> accnPromsPmts = accessionDao.getAccnPromsPmts(accnId);
        Assert.assertEquals(accnPromsPmts.size(), 1, "AccnPromPmts expected size not found");
        AccnPromsPmt accnPromsPmt = accnPromsPmts.get(0);
        accnPromsPmt.setVoidUserId("qatester");
        accnPromsPmt.setPromsPmtSta(4);
        accessionDao.setAccnPromsPmt(accnPromsPmt);

        accessionDetail.submitClaimsOnAccnDetail(accnId, patientPyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(null, accnId);
        Assert.assertEquals(qasList.size(), 2);
        Assert.assertEquals(qasList.get(0).getSubmCnt(), 1);
        if (qasList.get(1).getSubmFileSeqId() > 0 || qasList.get(1).getSubmCnt() != 2)
        {
            qasList.get(1).setSubmCnt(2);
            qasList.get(1).setNextSubmDt(DateConversion.getNowAsSqlDate());
            qasList.get(1).setSubmFileSeqId(0);
            submissionDao.setQAccnSubm(qasList.get(1));
        }

        accnPromsPmts = accessionDao.getAccnPromsPmts(accnId);
        Assert.assertEquals(accnPromsPmts.size(), 1, "AccnPromPmts expected size not found");
        accnPromsPmt = accnPromsPmts.get(0);
        accnPromsPmt.setVoidUserId("");
        accnPromsPmt.setPromsPmtSta(1);
        accessionDao.setAccnPromsPmt(accnPromsPmt);

        logger.info("Verify info on promised payment");
        SubmFile submFile = rpmDao.getSubmFile(null, qasList.get(0).getSubmFileSeqId());
        Assert.assertTrue(accnPromsPmt.getCntctDt().compareTo(submFile.getFileCreatDt()) > 0, "Expected cntctDt (" + accnPromsPmt.getCntctDt() + ") > fileCreatDt (" + submFile.getFileCreatDt() + ")");

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(null, rpmDao.getSubmSvcByAbbrev(null, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(1), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to create the record from QAS");
        submFile = rpmDao.getSubmFile(null, submFileSeqId);
        waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        submFile = rpmDao.getSubmFile(null, submFileSeqId);
        Assert.assertTrue(submFile.getIsEgateProcessed(), "Expected b_gate_processed = 1, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure 3 QAS record are there");
        qasList = rpmDao.getQAccnSubm(null, accnId);
        Assert.assertEquals(qasList.size(), 3);

        logger.info("Make sure processed QAS record has subm_file_seq_id > 0");
        Assert.assertNotEquals(qasList.get(1).getSubmFileSeqId(), 0);

        logger.info("Make sure new QAS record has correct values");
        Assert.assertEquals(qasList.get(2).getSubmFileSeqId(), 0, "Make sure new QAS record has subm_file_seq_id = 0");
        Assert.assertEquals(qasList.get(2).getSubmCnt(), 2, "Make sure new QAS record has subm_cnt = 2");
        Assert.assertEquals(qasList.get(1).getSubmSvcSeqId(), qasList.get(2).getSubmSvcSeqId(), "Make sure new QAS record has same subm_svc");
        Assert.assertEquals(qasList.get(2).getNextSubmDt().toString(), DateConversion.addDays(DateConversion.getNowAsSqlDate(), 30).toString(), "Make sure new QAS record next_subm_dt is 30 days from now");
    }

    /**
     * Active Promise Payment Plan, Min Had Been Met On Last Stmt But No Payments This Stmt
     * <p>
     * Preconditions: Accession has an active Promise Payment plan which was set up prior to the previous statement date
     * Minimum payment had been met on the previous statement date No patient payments have been made since the previous
     * statement date
     * <p>
     * Generate a patient statement for the first dunning cycle, where dunning cycle #3 specifies a Collections
     * submission service.
     * <p>
     * Verify that: A record is queued for the correct future date (based on the dunning cycle configuration), and that
     * the dunning count has been incremented (3) and the submission service has been changed to the Collections
     * submission service The Active Payment Plan has been cancelled and a qMessage has been created indicating that the
     * payment plan is in arrears The statement is generated with the full due amount on the accession
     *
     * @param accnId          Accession ID
     * @param patientPyrAbbrv Patient payor abbrv
     * @param subId           Subscriber ID
     * @param submSvcAbbrev   Subm svc abbrev
     * @param docSeqIdsStr    Doc Seq ID
     * @throws Exception Exception
     */
    @Test(priority = 1, description = "Active Promise Payment Plan, Min Had Been Met On Last Stmt But No Payments This Stmt")
    @Parameters({"accnId", "patientPyrAbbrv", "subId", "submSvcAbbrev", "docSeqIdsStr"})
    public void testPFER_745(String accnId, String patientPyrAbbrv, String subId, String submSvcAbbrev, String docSeqIdsStr) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-745");
        List<String> docSeqIdsStringList = Arrays.asList(StringUtils.split(docSeqIdsStr));
        int pyrId = rpmDao.getPyrByPyrAbbrv(null, patientPyrAbbrv).getPyrId();
        cleanQASForPyrOnAccnExceptDocSeqIds(accnId, pyrId, docSeqIdsStringList);

        logger.info("Verifying there is no promised payment plan on accession, accnId=" + accnId);
        List<AccnPromsPmt> accnPromsPmts = accessionDao.getAccnPromsPmts(accnId);
        Assert.assertEquals(accnPromsPmts.size(), 1, "AccnPromPmts expected size not found");
        AccnPromsPmt accnPromsPmt = accnPromsPmts.get(0);
        accnPromsPmt.setVoidUserId("qatester");
        accnPromsPmt.setPromsPmtSta(4);
        accessionDao.setAccnPromsPmt(accnPromsPmt);

        accessionDetail.submitClaimsOnAccnDetail(accnId, patientPyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(null, accnId);
        Assert.assertEquals(qasList.size(), 2);
        Assert.assertEquals(qasList.get(0).getSubmCnt(), 1);
        if (qasList.get(1).getSubmFileSeqId() > 0 || qasList.get(1).getSubmCnt() != 2)
        {
            qasList.get(1).setSubmCnt(2);
            qasList.get(1).setNextSubmDt(DateConversion.getNowAsSqlDate());
            qasList.get(1).setSubmFileSeqId(0);
            submissionDao.setQAccnSubm(qasList.get(1));
        }

        accnPromsPmts = accessionDao.getAccnPromsPmts(accnId);
        Assert.assertEquals(accnPromsPmts.size(), 1, "AccnPromPmts expected size not found");
        accnPromsPmt = accnPromsPmts.get(0);
        accnPromsPmt.setVoidUserId("");
        accnPromsPmt.setPromsPmtSta(1);
        accessionDao.setAccnPromsPmt(accnPromsPmt);

        logger.info("Verify info on promised payment");
        SubmFile submFile = rpmDao.getSubmFile(null, qasList.get(0).getSubmFileSeqId());
        Assert.assertTrue(accnPromsPmt.getCntctDt().compareTo(submFile.getFileCreatDt()) < 0, "Expected cntctDt (" + accnPromsPmt.getCntctDt() + ") > fileCreatDt (" + submFile.getFileCreatDt() + ")");

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(null, rpmDao.getSubmSvcByAbbrev(null, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(1), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to create the record from QAS");
        submFile = rpmDao.getSubmFile(null, submFileSeqId);
        waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        submFile = rpmDao.getSubmFile(null, submFileSeqId);
        Assert.assertTrue(submFile.getIsEgateProcessed(), "Expected b_gate_processed = 1, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure 3 QAS record are there");
        qasList = rpmDao.getQAccnSubm(null, accnId);
        Assert.assertEquals(qasList.size(), 3);

        logger.info("Make sure processed QAS record has subm_file_seq_id > 0");
        Assert.assertNotEquals(qasList.get(1).getSubmFileSeqId(), 0);

        logger.info("Make sure new QAS record has correct values");
        Assert.assertEquals(qasList.get(2).getSubmFileSeqId(), 0, "Make sure new QAS record has subm_file_seq_id = 0");
        Assert.assertEquals(qasList.get(2).getSubmCnt(), 3, "Make sure new QAS record has subm_cnt = 3");
        Assert.assertNotEquals(qasList.get(1).getSubmSvcSeqId(), qasList.get(2).getSubmSvcSeqId(), "Make sure new QAS record not same subm_svc");
        Assert.assertEquals(qasList.get(2).getNextSubmDt().toString(), DateConversion.addDays(DateConversion.getNowAsSqlDate(), 60).toString(), "Make sure new QAS record next_subm_dt is 60 days from now");
    }

    /**
     * No Promise Payment, Paperless Override Enabled with notification plan
     * <p>
     * Preconditions Accession has no active Promised Payment Plan
     * <p>
     * Generate a patient statement for the second dunning cycle which should be overridden with paperless service and where dunning cycle #3 specifies a Collections
     * submission service.
     * <p>
     * Verify that a record is queued for dunning cycle #3 for the correct future date (based on the dunning cycle
     * configuration), and that the submission service ID is the Collections submission service.  Second submission should have paperless service.
     *
     * @param accnId          Accession ID
     * @param patientPyrAbbrv Patient payor abbrv
     * @param subId           Subscriber ID
     * @param submSvcSubmitAbbrev   Subm svc submitted abbrev
     * @param submSvcCreatedAbbrev  Subm svc created abbrev
     * @param docSeqIdsStr    Doc Seq ID
     * @throws Exception Exception
     */
    @Test(priority = 1, description = "No Promise Payment, Paperless Override Enabled with notification plan")
    @Parameters({"accnId", "patientPyrAbbrv", "subId", "submSvcSubmitAbbrev", "submSvcCreatedAbbrev", "docSeqIdsStr"})
    public void testPFER_776(String accnId, String patientPyrAbbrv, String subId, String submSvcSubmitAbbrev, String submSvcCreatedAbbrev, String docSeqIdsStr) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-776");
        List<String> docSeqIdsStringList = Arrays.asList(StringUtils.split(docSeqIdsStr));
        int pyrId = rpmDao.getPyrByPyrAbbrv(null, patientPyrAbbrv).getPyrId();
        cleanQASForPyrOnAccnExceptDocSeqIds(accnId, pyrId, docSeqIdsStringList);

        logger.info("Verifying there is no promised payment plan on accession, accnId=" + accnId);
        List<AccnPromsPmt> accnPromsPmts = accessionDao.getAccnPromsPmts(accnId);
        Assert.assertEquals(accnPromsPmts.size(), 0, "AccnPromPmts expected size not found");

        logger.info("Setting up ptNotif records");
        int ptSeqId = accessionDao.getAccn(accnId).getPtSeqId();
        PtNotifPlan ptNotifPlan = patientDao.getPtNotifPlanBySeqId(ptSeqId);
        ptNotifPlan.setIsActive(true);
        databaseSequenceDao.setValueObject(ptNotifPlan);
        patientDao.activatePtNotifContact(ptSeqId);

        accessionDetail.submitClaimsOnAccnDetail(accnId, patientPyrAbbrv, subId, submSvcSubmitAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(null, accnId);
        Assert.assertEquals(qasList.size(), 2);
        Assert.assertEquals(qasList.get(0).getSubmCnt(), 1);
        if (qasList.get(1).getSubmFileSeqId() > 0 || qasList.get(1).getSubmCnt() != 2)
        {
            qasList.get(1).setSubmCnt(2);
            qasList.get(1).setNextSubmDt(DateConversion.getNowAsSqlDate());
            qasList.get(1).setSubmFileSeqId(0);
            submissionDao.setQAccnSubm(qasList.get(1));
        }

        SubmSvc submSvcCreated = rpmDao.getSubmSvcByAbbrev(null, submSvcCreatedAbbrev);
        SubmSvc submSvcSubmit = rpmDao.getSubmSvcByAbbrev(null, submSvcSubmitAbbrev);
        Set<SubmSvc> submSvcSet = new HashSet<>();
        submSvcSet.add(submSvcCreated);
        submSvcSet.add(submSvcSubmit);

        logger.info("Update Last Date for Subm svcs to be null, submSvcSubmitAbbrev=" + submSvcSubmitAbbrev + ", submSvcCreatedAbbrev=" + submSvcCreatedAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(null, submSvcSubmit.getSubmSvcSeqId());
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(null, submSvcCreated.getSubmSvcSeqId());

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(1), QUEUE_WAIT_TIME*2, submSvcSet);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to create the record from QAS and verify submSvc="+ submSvcCreatedAbbrev);
        SubmFile submFile = rpmDao.getSubmFile(null, submFileSeqId);
        waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        submFile = rpmDao.getSubmFile(null, submFileSeqId);
        Assert.assertEquals(submFile.getSubmSvcSeqId(), submSvcCreated.getSubmSvcSeqId(), "Expected different submission service, submFileSeqId=" + submFileSeqId);
        Assert.assertTrue(submFile.getIsEgateProcessed(), "Expected b_gate_processed = 1, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure 3 QAS record are there");
        qasList = rpmDao.getQAccnSubm(null, accnId);
        Assert.assertEquals(qasList.size(), 3);

        logger.info("Make sure processed QAS record has subm_file_seq_id > 0");
        Assert.assertNotEquals(qasList.get(1).getSubmFileSeqId(), 0);

        logger.info("Make sure new QAS record has correct values");
        Assert.assertEquals(qasList.get(2).getSubmFileSeqId(), 0, "Make sure new QAS record has subm_file_seq_id = 0");
        Assert.assertEquals(qasList.get(2).getSubmCnt(), 3, "Make sure new QAS record has subm_cnt = 3");
        Assert.assertNotEquals(qasList.get(0).getSubmSvcSeqId(), qasList.get(2).getSubmSvcSeqId(), "Make sure new QAS record not same subm_svc");
        Assert.assertEquals(qasList.get(2).getNextSubmDt().toString(), DateConversion.addDays(DateConversion.getNowAsSqlDate(), 60).toString(), "Make sure new QAS record next_subm_dt is 30 days from now");
    }

    @Test(priority = 1, description = "Cancelled Professional Claim Does Not Queue Submission for Next Dunning Cycle")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "claimId", "icn"})
    public void testCancelledProfessionalClaimDoesNotQueueNextDunningCycle(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String claimId, String icn) throws Exception
    {
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(null, pyrAbbrv);
        logger.info("Make sure payor is configured for more than 1 dunning cycle, accnId="+accnId+", pyrAbbrv="+pyrAbbrv);
        List<PyrDun> pyrDuns = payorDao.getPyrDunsByPyrId(pyr.getPyrId());
        Assert.assertTrue(pyrDuns.size() > 1);

        logger.info("Cleaning up prior submissions for the payor, accnId="+accnId+", pyrAbbrv="+pyrAbbrv);
        rpmDao.deleteUnprocessedQAccnSubmByAccnId(testDb, accnId);
        List<QAccnSubm> qAccnSubmsToDelete = new ArrayList<>();
        for (QAccnSubm qas : rpmDao.getQAccnSubm(null, accnId))
        {
            if (qas.getPyrId() == pyr.getPyrId() && !StringUtils.containsIgnoreCase(qas.getAccnId()+"Z"+qas.getClaimIdSuffix(), claimId))
            {
                qAccnSubmsToDelete.add(qas);
            }
        }
        submissionDao.deleteSubmissions(qAccnSubmsToDelete);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Submit a Cancelled claim on AccnDetail, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", icn="+icn);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        clickHiddenPageObject(accessionDetail.submitClaimAddBtn(), 0);
        accessionDetail.enterSubmitClaimInfo(pyrAbbrv, subId, submSvcAbbrev, "Cancelled", icn, null, wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.accnIdText().sendKeys(Keys.ALT + "S");
        wait.until(ExpectedConditions.textToBePresentInElement(accessionDetail.accessionDetailTitle(), "Detail"));

        logger.info("Make sure the submission is queued, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", claimTyp="+ MiscMap.CLAIM_TYP_CANCELLED_8);
        QAccnSubm unprocessedQas = rpmDao.getUnprocessedQAccnSubm(testDb, accnId);
        Assert.assertEquals(unprocessedQas.getPyrId(), pyr.getPyrId());
        Assert.assertEquals(unprocessedQas.getClaimTypId(), MiscMap.CLAIM_TYP_CANCELLED_8);
        Assert.assertEquals(unprocessedQas.getSendingIcn(), icn);
        Assert.assertEquals(unprocessedQas.getSubmCnt(), 1);

        List<QAccnSubm> oldQasForAccn = submissionDao.getQAccnSubmsByAccnId(accnId);

        logger.info("Resetting submission service, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        int submFileSeqId = waitForSubmissionEngine(unprocessedQas, QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);
        Assert.assertTrue(waitForStatementEngine(rpmDao.getSubmFile(testDb, submFileSeqId), QUEUE_WAIT_TIME));
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Make sure processed submission record has correct claim type, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", claimType="+MiscMap.CLAIM_TYP_CANCELLED_8);
        QAccnSubm qas = rpmDao.getQAccnSubmByAccnIdSubmFileSeqId(testDb, submFile.getSubmFileSeqId(), accnId);
        Assert.assertEquals(qas.getClaimTypId(), MiscMap.CLAIM_TYP_CANCELLED_8);
        Assert.assertEquals(qas.getSubmittedSubsId(), subId);
        Assert.assertEquals(qas.getSendingIcn(), icn);

        logger.info("Make sure no new submission is queued, accnId="+accnId);
        List<QAccnSubm> newQasForAccn = submissionDao.getQAccnSubmsByAccnId(accnId);
        Assert.assertEquals(newQasForAccn.size(), oldQasForAccn.size());
    }

    @Test(priority = 1, description = "Original Professional Claim Queues Submission for Next Dunning Cycle")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev"})
    public void testOriginalProfessionalClaimQueuesNextDunningCycle(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev) throws Exception
    {
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(null, pyrAbbrv);
        logger.info("Make sure payor is configured for more than 1 dunning cycle, accnId="+accnId+", pyrAbbrv="+pyrAbbrv);
        List<PyrDun> pyrDuns = payorDao.getPyrDunsByPyrId(pyr.getPyrId());
        Assert.assertTrue(pyrDuns.size() > 1);
        Assert.assertTrue(pyrDuns.get(0).getDaysToNextCycle() > 0);

        logger.info("Cleaning up prior submissions, accnId="+accnId+", pyrAbbrv="+pyrAbbrv);
        submissionDao.deleteSubmissions(rpmDao.getQAccnSubm(null, accnId));
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Submit Original claim on AccnDetail, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", subId="+subId);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        clickHiddenPageObject(accessionDetail.submitClaimAddBtn(), 0);
        accessionDetail.enterSubmitClaimInfo(pyrAbbrv, subId, submSvcAbbrev, "Original", null, null, wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.accnIdText().sendKeys(Keys.ALT + "S");
        wait.until(ExpectedConditions.textToBePresentInElement(accessionDetail.accessionDetailTitle(), "Detail"));

        logger.info("Make sure the submission is queued, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", claimTyp="+ MiscMap.CLAIM_TYP_CANCELLED_8);
        QAccnSubm unprocessedQas = rpmDao.getUnprocessedQAccnSubm(testDb, accnId);
        Assert.assertEquals(unprocessedQas.getPyrId(), pyr.getPyrId());
        Assert.assertEquals(unprocessedQas.getClaimTypId(), MiscMap.CLAIM_TYP_NEW_1);
        Assert.assertNull(unprocessedQas.getSendingIcn());
        Assert.assertEquals(unprocessedQas.getSubmCnt(), 1);

        List<QAccnSubm> oldQasForAccn = submissionDao.getQAccnSubmsByAccnId(accnId);

        logger.info("Resetting submission service, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        int submFileSeqId = waitForSubmissionEngine(unprocessedQas, QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);
        Assert.assertTrue(waitForStatementEngine(rpmDao.getSubmFile(testDb, submFileSeqId), QUEUE_WAIT_TIME));
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Make sure processed submission record has correct claim type, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", claimType="+MiscMap.CLAIM_TYP_NEW_1);
        QAccnSubm qas = rpmDao.getQAccnSubmByAccnIdSubmFileSeqId(testDb, submFile.getSubmFileSeqId(), accnId);
        Assert.assertEquals(qas.getClaimTypId(), MiscMap.CLAIM_TYP_NEW_1);
        Assert.assertEquals(qas.getSubmittedSubsId(), subId);
        Assert.assertNull(qas.getSendingIcn());

        logger.info("Make sure new submission is queued, accnId="+accnId);
        List<QAccnSubm> newQasForAccn = submissionDao.getQAccnSubmsByAccnId(accnId);
        Assert.assertEquals(newQasForAccn.size(), oldQasForAccn.size()+1);
        Assert.assertEquals(newQasForAccn.get(newQasForAccn.size()-1).getSubmCnt(), 2);
        Assert.assertEquals(newQasForAccn.get(newQasForAccn.size()-1).getPyrPrio(), 1);
        Assert.assertEquals(newQasForAccn.get(newQasForAccn.size()-1).getPyrId(), pyr.getPyrId());
        Assert.assertEquals(newQasForAccn.get(newQasForAccn.size()-1).getClaimTypId(), MiscMap.CLAIM_TYP_NOT_SPECIFIED);
        Date expectedNextSubmDt = DateUtils.addDays(new Date(), pyrDuns.get(0).getDaysToNextCycle());
        Assert.assertTrue(DateUtils.isSameDay(newQasForAccn.get(newQasForAccn.size()-1).getNextSubmDt(), expectedNextSubmDt));
    }
}
