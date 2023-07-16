package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.accnQue.AccnQue;
import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrSvc.PyrSvc;
import com.mbasys.mars.ejb.entity.qAccnSubm.QAccnSubm;
import com.mbasys.mars.ejb.entity.submClaimAudit.SubmClaimAudit;
import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.mbasys.mars.ejb.entity.submFileAudit.SubmFileAudit;
import com.mbasys.mars.ejb.entity.submSvc.SubmSvc;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.MiscMap;
import com.mchange.io.FileUtils;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.menu.MenuNavigation;
import com.xifin.automation.utils.X12Utils;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.qa.config.PropertyMap;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClaimStatusStatementEngineTest extends NonClientStatementEngineTest
{
    @Test(priority = 1, description = "Claim Batch - Process 276 claims queued up by PF-Claim Status engine, no other submissions for accn")
    @Parameters({"accnId", "primPyrAbbrv", "expectedBillingFacId", "expectedFile", "userClaimStatStmtEng", "submSvc212Abbrev", "submSvcAbbrev837", "docSeqIdsStr"})
    public void testPFER_648(String accnId, String primPyrAbbrv, String expectedBillingFacId, String expectedFile, String userClaimStatStmtEng, String submSvc212Abbrev, String submSvcAbbrev837, String docSeqIdsStr) throws Exception
    {
        logger.info("Running testPFER-648 Claim Batch - Process 276 claims queued up by PF-Claim Status engine, no other submissions for accn");
        File expected212File = new File(getClass().getResource("/mars/testFiles/" + expectedFile).getFile());
        String expected212Data = X12Utils.clearSegments(FileUtils.getContentsAsString(expected212File));
        Pyr primPyr = rpmDao.getPyrByPyrAbbrv(null, primPyrAbbrv);
        List<String> docSeqIdsStringList = Arrays.asList(com.xifin.util.StringUtils.split(docSeqIdsStr));
        cleanQASForPyrOnAccnExceptDocSeqIds(accnId, primPyr.getPyrId(), docSeqIdsStringList);

        logger.info("Get the expected billing facility - " + expectedBillingFacId);
        Fac expectedBillingFac = rpmDao.getFacByFacId(null, Integer.parseInt(expectedBillingFacId));
        Accn accn = accessionDao.getAccn(accnId);
        logger.info("Make sure accn is NOT FReported or ZBal - " + accn.getStaId());
        Assert.assertNotEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL);
        Assert.assertNotEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED);

        logger.info("Make sure primary pyr is setup for pyr_svc_typ 2 - " + accn.getStaId());
        SubmSvc submSvc212 = rpmDao.getSubmSvcByAbbrev(null, submSvc212Abbrev);
        Assert.assertTrue(isPyrSetupForBatchClaimStatusCheck(primPyr, submSvc212));
        logger.info("Make sure Accn has previous Q_ACCN_SUBM records for the same PyrId, SubmSvc - 837");
        QAccnSubm previousQAS = rpmDao.getQAccnSubmByAccnIdAndPyrIdAndSubmSvcId(testDb, accnId, primPyr.getPyrId(), rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev837).getSubmSvcSeqId());

        logger.info("Insert accn into Q_CLAIM_STATUS " + accnId);
        rpmDao.insertAccnIntoQClaimStatus(null, accnId, "xqatester", null);

        logger.info("Make sure Accn is in q_claim_status");
        Assert.assertTrue(isAccnInQClaimStatus(accnId, QUEUE_WAIT_TIME));

        logger.info("Wait until Accn is Processed by Claim Status Engine");
        boolean isAccnOutOfQClaimStatus = waitForClaimStatusEngineToProcessAccn(accnId, QUEUE_WAIT_TIME);
        Assert.assertTrue(isAccnOutOfQClaimStatus);

        logger.info("Make sure Accn is in Submission queue");
        AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
        Assert.assertEquals(accnQue.getQTyp(), AccnStatusMap.Q_ACCN_SUBM);

        logger.info("Make sure there is 1 new QAS record created by ClaimStatus Engine");
        List<QAccnSubm> qAccnSubm276List = rpmDao.getQAccnSubmByAccnIdAndSubmSvc(null, accnId, submSvc212.getSubmSvcSeqId());

        logger.info("Make sure a new 276 claim QAS record created with subm_svc from pyr_svc");
        QAccnSubm new276QAccnSubm = getNewlyCreatedQAccnSubm(qAccnSubm276List, previousQAS.getClaimIdSuffix());

        logger.info("Update Last Date for Subm cvs to be null, submSvcAbbrev=" + submSvc212.submSvcSeqId);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(null, submSvc212.submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(new276QAccnSubm, QUEUE_WAIT_TIME);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Claim Status Statement Engine to process the record in subm_file");
        SubmFile submFile = rpmDao.getSubmFile(null, submFileSeqId);
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile);

        SubmFile processedSubmFile = rpmDao.getSubmFile(null, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed());
        Assert.assertNotNull(submFile.getFilename());
        Assert.assertNotNull(submFile.getDir());

        logger.info("Make sure subm_file_audit record is created");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAuditByAccnIdAndSubmFileSeqId(null, accnId, submFileSeqId);
        Assert.assertFalse(submFileAudits.isEmpty());
        Assert.assertEquals(submFileAudits.get(0).getPyrId(), primPyr.getPyrId());
        Assert.assertEquals(submFileAudits.get(0).getDocSeqId(), new276QAccnSubm.getDocSeqId());
        Assert.assertNotNull(submFileAudits.get(0).getActivityDt());

        logger.info("Make sure subm_claim_audit record is created");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(null, new276QAccnSubm.getDocSeqId());
        Assert.assertEquals(submClaimAudit.getBillingFacId(), expectedBillingFac.getFacId());
        Assert.assertEquals(submClaimAudit.getSubmittedFacName(), expectedBillingFac.getName());
        Assert.assertEquals(submClaimAudit.getSubmittedNpi(), expectedBillingFac.getNpi());
        Assert.assertTrue(submClaimAudit.getAudUser().contains(userClaimStatStmtEng));

        logger.info("Make sure 276 claim QAS record was updated by Claim Status Statement Engine");
        qAccnSubm276List = rpmDao.getQAccnSubmByAccnIdAndSubmSvc(null, accnId, submSvc212.getSubmSvcSeqId());
        new276QAccnSubm = qAccnSubm276List.get(0);
        Assert.assertEquals(new276QAccnSubm.getBillingFacId(), 1);
        Assert.assertTrue(new276QAccnSubm.getAudUser().contains(userClaimStatStmtEng), "Unexpected auditUser=" + new276QAccnSubm.getAudUser() + ", docSeqId=" + new276QAccnSubm.getDocSeqId());
        Assert.assertEquals(new276QAccnSubm.getClaimIdSuffix(), previousQAS.getClaimIdSuffix());
        Assert.assertEquals(new276QAccnSubm.getSubmittedSubsId(), previousQAS.getSubmittedSubsId());

        logger.info("Make sure 276 claim subm_file record was updated by Claim Status Statement Engine");
        submFile = rpmDao.getSubmFile(null, submFileSeqId);
        Assert.assertTrue(submFile.getAudUser().contains(userClaimStatStmtEng));

        File actualFile = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFile.getFilename());
        Assert.assertTrue(actualFile.exists());

        logger.info("Make sure Actual File contains the Expected file");
        String contents = FileUtils.getContentsAsString(actualFile);
        Assert.assertFalse(contents.isEmpty(), "Contents should not be empty!");
        String actual212Data = X12Utils.clearSegments(contents);
        expected212Data = expected212Data.replaceAll(accnId, accnId + "Z" + new276QAccnSubm.getClaimIdSuffix());
        Assert.assertTrue(actual212Data.contains(expected212Data), "File difference: " + StringUtils.difference(expected212Data, actual212Data));
    }

    @Test(priority = 1, description = "Claim Batch - Process 276 claims queued up by PF-Claim Status engine and 837 claim")
    @Parameters({"accnId", "primPyrAbbrv", "submSvcAbbrev837", "subId", "expectedBillingFacId", "expectedFile", "userClaimStatStmtEng", "submSvc212Abbrev", "docSeqIdsStr"})
    public void testPFER_649(String accnId, String primPyrAbbrv, String submSvcAbbrev837, String subId, String expectedBillingFacId, String expectedFile, String userClaimStatStmtEng, String submSvc212Abbrev, String docSeqIdsStr) throws Exception
    {
        navigation = new MenuNavigation(driver, config);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-649 - Claim Batch - Process 276 claims queued up by PF-Claim Status engine and 837 claim");
        File expected212File = new File(getClass().getResource("/mars/testFiles/" + expectedFile).getFile());
        String expected212Data = X12Utils.clearSegments(FileUtils.getContentsAsString(expected212File));
        Fac expectedBillingFac = rpmDao.getFacByFacId(null, Integer.parseInt(expectedBillingFacId));

        Accn accn = accessionDao.getAccn(accnId);
        logger.info("Make sure accn is NOT FReported or ZBal - " + accn.getStaId());
        Assert.assertNotEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_ZBAL);
        Assert.assertNotEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED);
        logger.info("Make sure primary pyr is setup for pyr_svc_typ 2 - " + accn.getStaId());
        Pyr primPyr = rpmDao.getPyrByPyrAbbrv(null, primPyrAbbrv);
        SubmSvc submSvc212 = rpmDao.getSubmSvcByAbbrev(null, submSvc212Abbrev);
        Assert.assertTrue(isPyrSetupForBatchClaimStatusCheck(primPyr, submSvc212));

        List<String> docSeqIdsStringList = Arrays.asList(com.xifin.util.StringUtils.split(docSeqIdsStr));
        cleanQASForPyrOnAccnExceptDocSeqIds(accnId, primPyr.getPyrId(), docSeqIdsStringList);

        logger.info("Make sure Accn has previous Q_ACCN_SUBM records for the same PyrId, SubmSvc - 837");
        QAccnSubm previousQAS = rpmDao.getQAccnSubmByAccnIdAndPyrIdAndSubmSvcId(testDb, accnId, primPyr.getPyrId(), rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev837).getSubmSvcSeqId());
        navigation.navigateToAccnDetailPage();

        logger.info("Enter Claim Info for Primary Pyr into Submit Claim popup, primPyrAbbrv - " + primPyrAbbrv);
        accessionDetail.submitClaimsOnAccnDetail(accnId, primPyrAbbrv, subId, submSvcAbbrev837, wait);

        SubmSvc submSvc837 = rpmDao.getSubmSvcByAbbrev(null, submSvcAbbrev837);

        logger.info("Make sure new QAS record created with subm_svc from pyr_svc");
        List<QAccnSubm> qAccnSubm837StatusList = rpmDao.getQAccnSubmByAccnIdAndSubmSvc(null, accnId, submSvc837.getSubmSvcSeqId());
        QAccnSubm new837QAccnSubm = qAccnSubm837StatusList.get(0);

        logger.info("Insert accn into Q_CLAIM_STATUS " + accnId);
        rpmDao.insertAccnIntoQClaimStatus(null, accnId, "xqatester", null);

        logger.info("Make sure Accn is in q_claim_status");
        Assert.assertTrue(isAccnInQClaimStatus(accnId, QUEUE_WAIT_TIME));

        logger.info("Wait until Accn is Processed by Claim Status Engine");
        boolean isAccnOutOfQClaimStatus = waitForClaimStatusEngineToProcessAccn(accnId, QUEUE_WAIT_TIME);
        Assert.assertTrue(isAccnOutOfQClaimStatus);

        logger.info("Make sure Accn is in Submission queue");
        AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
        Assert.assertEquals(accnQue.getQTyp(), AccnStatusMap.Q_ACCN_SUBM);

        logger.info("Make sure there is 1 new QAS record created by ClaimStatus Engine");
        List<QAccnSubm> qAccnSubmWithClaimStatusList = rpmDao.getQAccnSubmByAccnIdAndSubmSvc(null, accnId, submSvc212.getSubmSvcSeqId());

        logger.info("Make sure new QAS record created with subm_svc from pyr_svc");
        QAccnSubm new276QAccnSubm = getNewlyCreatedQAccnSubm(qAccnSubmWithClaimStatusList, previousQAS.getClaimIdSuffix());

        logger.info("Update Last Date for Subm cvs to be null for both: 837 and 276 claim, submSvcAbbrev=" + submSvc837.getSubmSvcSeqId() + ", " + submSvc212.getSubmSvcSeqId());
        rpmDao.updateLastSubmDtSubmSvcToNullForMultipleSubmSvcBySubmSvcSeqId(null, submSvc837.getSubmSvcSeqId(), submSvc212.getSubmSvcSeqId());

        logger.info("Wait for Non-Client Submission Engine to process the 837 claim");
        int submFile837SeqId = waitForSubmissionEngine(new837QAccnSubm, QUEUE_WAIT_TIME);
        Assert.assertTrue(submFile837SeqId > 0);
        logger.info("Wait for Non-Client Submission Engine to process the 276 claim");
        int submFile276SeqId = waitForSubmissionEngine(new276QAccnSubm, QUEUE_WAIT_TIME);
        Assert.assertTrue(submFile276SeqId > 0);

        logger.info("Make sure subm_file records are created");
        SubmFile unprocessed276SubmFile = rpmDao.getSubmFile(null, submFile276SeqId);
        logger.info("Make sure subm_file record has 212 unprocessedSubmFile " + unprocessed276SubmFile);
        Assert.assertEquals(unprocessed276SubmFile.getSubmSvcSeqId(), submSvc212.getSubmSvcSeqId());
        Assert.assertTrue(unprocessed276SubmFile.getFilename().contains("TEST-212"));

        SubmFile unprocessed837SubmFile = rpmDao.getSubmFile(null, submFile837SeqId);
        logger.info("Make sure subm_file record has 837 unprocessedSubmFile " + unprocessed837SubmFile);
        Assert.assertEquals(unprocessed837SubmFile.getSubmSvcSeqId(), submSvc837.getSubmSvcSeqId());
        Assert.assertTrue(unprocessed837SubmFile.getFilename().contains("BCCA5010A1"));

        logger.info("Wait for Claim Status Statement Engine to process 276");
        boolean isStatementEngineProcessedFile276 = waitForStatementEngine(unprocessed276SubmFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(isStatementEngineProcessedFile276);

        SubmFile processedSubmFile276 = rpmDao.getSubmFile(null, submFile276SeqId);
        logger.info("Wait for Claim Status Statement Engine processedSubmFile276 " + processedSubmFile276);
        Assert.assertTrue(processedSubmFile276.getIsEgateProcessed());

        logger.info("Make sure subm_file_audit for 276 record is created");
        List<SubmFileAudit> submFileAudit276s = rpmDao.getSubmFileAuditByAccnIdAndSubmFileSeqId(null, accnId, submFile276SeqId);
        Assert.assertFalse(submFileAudit276s.isEmpty());
        Assert.assertEquals(submFileAudit276s.get(0).getPyrId(), primPyr.getPyrId());
        Assert.assertNotNull(submFileAudit276s.get(0).getActivityDt());
        Assert.assertEquals(submFileAudit276s.get(0).getDocSeqId(), new276QAccnSubm.getDocSeqId());

        logger.info("Make sure subm_claim_audit record is created for 276");
        SubmClaimAudit submClaimAudit276 = rpmDao.getSubmClaimAuditByDocSeqId(null, new276QAccnSubm.getDocSeqId());
        Assert.assertEquals(submClaimAudit276.getBillingFacId(), expectedBillingFac.getFacId());
        Assert.assertEquals(submClaimAudit276.getSubmittedFacName(), expectedBillingFac.getName());
        Assert.assertEquals(submClaimAudit276.getSubmittedNpi(), expectedBillingFac.getNpi());
        Assert.assertTrue(submClaimAudit276.getAudUser().contains(userClaimStatStmtEng), "Unexpected auditUser=" + submClaimAudit276.getAudUser() + ", docSeqId=" + new276QAccnSubm.getDocSeqId());

        logger.info("Make sure 276 claim QAS record was updated by Claim Status Statement Engine");
        qAccnSubmWithClaimStatusList = rpmDao.getQAccnSubmByAccnIdAndSubmSvc(null, accnId, submSvc212.getSubmSvcSeqId());
        new276QAccnSubm = qAccnSubmWithClaimStatusList.get(0);
        Assert.assertEquals(new276QAccnSubm.getBillingFacId(), 1);
        Assert.assertTrue(new276QAccnSubm.getAudUser().contains(userClaimStatStmtEng));
        Assert.assertEquals(new276QAccnSubm.getClaimIdSuffix(), previousQAS.getClaimIdSuffix());
        Assert.assertEquals(new276QAccnSubm.getSubmittedSubsId(), previousQAS.getSubmittedSubsId());

        logger.info("Make sure 276 claim subm_file record was updated by Claim Status Statement Engine");
        SubmFile processed276SubmFile = rpmDao.getSubmFile(null, submFile276SeqId);
        Assert.assertTrue(processed276SubmFile.getAudUser().contains(userClaimStatStmtEng));
        Assert.assertEquals(processed276SubmFile.getSubmSvcSeqId(), submSvc212.getSubmSvcSeqId());

        logger.info("Make sure No New Errors added to the accn");
        List<AccnPyrErr> newAccnPyrErrs = accessionDao.getAccnPyrErrsByAccnIdPyrPrio(accnId, new837QAccnSubm.getPyrPrio(), false);
        Assert.assertEquals(newAccnPyrErrs.size(), 0);

        logger.info("Make sure 276 file is saved to the dir");
        String submFileName = processedSubmFile276.getFilename();
        File actualFile = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName);
        Assert.assertTrue(actualFile.exists());

        logger.info("Make sure Actual File contains the Expected file");
        String actual212Data = X12Utils.clearSegments(FileUtils.getContentsAsString(actualFile));
        expected212Data = expected212Data.replaceAll(accnId, accnId + "Z" + new276QAccnSubm.getClaimIdSuffix());
        Assert.assertTrue(actual212Data.contains(expected212Data), "File difference: " + StringUtils.difference(expected212Data, actual212Data));
    }

    private QAccnSubm getNewlyCreatedQAccnSubm(List<QAccnSubm> qAccnSubmWithClaimStatusList, String suffixId)
    {
        QAccnSubm new276QAccnSubm = qAccnSubmWithClaimStatusList.get(0);
        Assert.assertEquals(new276QAccnSubm.getClaimTypId(), 0);
        Assert.assertNotNull(new276QAccnSubm.getClaimIdSuffix());
        Assert.assertEquals(new276QAccnSubm.getClaimIdSuffix(), suffixId);
        return new276QAccnSubm;
    }

    protected boolean isAccnInQClaimStatus(String accnId, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isInQueue = rpmDao.isAccnInQClaimStatus(null, accnId);
        while (!isInQueue && System.currentTimeMillis() < maxTime)
        {
            logger.info("Retriving the record from Q_CLAIM_STATUS");
            Thread.sleep(QUEUE_POLL_TIME);
            isInQueue = rpmDao.isAccnInQClaimStatus(null, accnId);
        }
        return isInQueue;
    }

    public boolean waitForClaimStatusEngineToProcessAccn(String accnId, long maxTime)
            throws InterruptedException, XifinDataAccessException
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isQClaimStatusRecordExist = rpmDao.isAccnInQClaimStatus(null, accnId);
        while (isQClaimStatusRecordExist && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for Claim Status Engine to process q_claim_status record,  accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME);
            isQClaimStatusRecordExist = rpmDao.isAccnInQClaimStatus(null, accnId);
        }
        return !isQClaimStatusRecordExist;
    }

    protected int waitForSubmissionEngine(QAccnSubm qAccnSubm, long maxTime) throws InterruptedException, XifinDataAccessException
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        int submFileSeqId = qAccnSubm.getSubmFileSeqId();
        while (submFileSeqId == 0 && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for Submission Engine to process claim file, accnId=" + qAccnSubm.getAccnId() + ",docSeqId=" + qAccnSubm.getDocSeqId() + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME);
            try
            {
                qAccnSubm = rpmDao.getQAccnSubm(null, qAccnSubm.getDocSeqId());
            }
            catch (XifinDataNotFoundException e)
            {
                submFileSeqId = 0;
            }
            submFileSeqId = qAccnSubm.getSubmFileSeqId();
        }
        return submFileSeqId;
    }

    private boolean isPyrSetupForBatchClaimStatusCheck(Pyr pyr, SubmSvc submSvc276) throws XifinDataAccessException
    {
        logger.info("Make sure Pyr on accn is setup for Batch Claim Status - pyr_svc table, enabled, pyr_svc_typ =1, outgoing pyr - Test");
        boolean isPyrSetupForBatchClaimStatusCheck = false;
        List<PyrSvc> pyrSvc = rpmDao.getPyrSvcsByPyrId(null, pyr.getPyrId());
        if (pyrSvc.get(0).getPyrSvcTypId() == MiscMap.PYR_SVC_TYP_BATCH_CLAIM_STATUS && pyrSvc.get(0).getOutPyrId().equals("TEST") && pyrSvc.get(0).getSubmSvcSeqId() == submSvc276.getSubmSvcSeqId())
        {
            isPyrSetupForBatchClaimStatusCheck = true;
        }
        return isPyrSetupForBatchClaimStatusCheck;
    }
}
