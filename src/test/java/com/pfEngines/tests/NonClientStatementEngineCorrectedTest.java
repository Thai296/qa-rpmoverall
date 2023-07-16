package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnProc.AccnProc;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.accnQue.AccnQue;
import com.mbasys.mars.ejb.entity.eobClaim.EobClaim;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.qAccnSubm.QAccnSubm;
import com.mbasys.mars.ejb.entity.qValidateAccn.QValidateAccn;
import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.mbasys.mars.ejb.entity.submFileAudit.SubmFileAudit;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.MiscMap;
import com.mbasys.mars.validation.ValidateQueueMap;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.util.Money;
import com.xifin.util.StringUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NonClientStatementEngineCorrectedTest extends NonClientStatementEngineTest
{
    @Test(priority = 1, description = "Corrected Institutional Claim - match the previous payment and submission on SubscriberId")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev"})
    public void testPFER_759(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER_759");

        rpmDao.deleteUnprocessedQAccnSubmByAccnId(testDb, accnId);
        List<QAccnSubm> originalQAccnSubmList = rpmDao.getQAccnSubm(testDb, accnId);
        logger.debug("message=Cleaning up submission data, accnId="+accnId);
        for (QAccnSubm accnSubm : originalQAccnSubmList)
        {
            if (accnSubm.getClaimTypId() != MiscMap.CLAIM_TYP_NEW_141 && submissionDao.getSubmSvc(accnSubm.getSubmSvcSeqId()).getDocTypId() != MiscMap.DOC_TYP_CLAIM_STATUS_REQUEST)
            {
                //Delete claims that are not original and not claim status requests
                try
                {
                    SubmFile submFile = rpmDao.getSubmFile(testDb, accnSubm.submFileSeqId);
                    logger.debug("message=Cleaning up processed claim, accnId="+accnId+", claimTyp="+accnSubm.getClaimTypId());
                    rpmDao.deleteSubmClaimAuditBySubmFileSeqId(submFile.getSubmFileSeqId());
                    rpmDao.clearAccnProcSubmFileSeqIdBySubmFileSeqId(testDb, submFile.getSubmFileSeqId());
                    rpmDao.deleteSubmFileAuditBySubmFileSeqId(submFile.getSubmFileSeqId());
                    rpmDao.deleteQAccnSubmBySubmFileSeqId(testDb, submFile.getSubmFileSeqId());
                    rpmDao.deleteSubmFile(testDb, submFile.getSubmFileSeqId());
                }
                catch (Exception e)
                {
                    logger.info("Could not finish the submission cleanup", e);
                }
            }
            else
            {
                //Delete unprocessed files
                SubmFile submFile = rpmDao.getSubmFile(testDb, accnSubm.submFileSeqId);
                if (!submFile.getIsEgateProcessed())
                {
                    rpmDao.updateAccnProcSubmFileSeqId(testDb, submFile.getSubmFileSeqId());
                    rpmDao.deleteQAccnSubmBySubmFileSeqId(testDb, submFile.getSubmFileSeqId());
                    rpmDao.deleteSubmFile(testDb, submFile.getSubmFileSeqId());
                }
            }
        }

        originalQAccnSubmList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(originalQAccnSubmList.size(), 2, "Expected two q_accn_subm entries - one for claims status and one for original claim");

        logger.info("Make sure there is 1 previous submission for the same pyr");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubmByAccnIdAndSubmSvc(testDb, accnId, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).getSubmSvcSeqId());
        Assert.assertEquals(qasList.size(), 1);
        Assert.assertEquals(qasList.get(0).getPyrId(), rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId());
        logger.info("Make sure pyr Subscriber Id on the Accn is the same as previous QAS.SUBMITTED_SUBS_ID");
        Assert.assertEquals(qasList.get(0).getSubmittedSubsId(), subId);
        Assert.assertEquals(accessionDao.getAccnPyrs(accnId).get(0).getSubsId(), subId);
        logger.info("Make sure pyr QAS.SENDING_ICN is null");
        Assert.assertNull(qasList.get(0).getSendingIcn());
        logger.info("Make sure pyr QAS.CLAIM_TYP is NEW CLAIM");
        Assert.assertEquals(qasList.get(0).getClaimTypId(), MiscMap.CLAIM_TYP_NEW_141);

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        Accn accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED, "Expected accession status to be reported status,");

        // Wait for accn to get out of pricing queue
        AccnQue accnQue = accessionDao.getAccnQueByAccnId(accnId);
        if(accnQue.getQTyp()== AccnStatusMap.Q_ELIG){
            Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue");
        }
        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave pricing queue");

        QAccnSubm unprocessedQas = rpmDao.getUnprocessedQAccnSubm(testDb, accnId);
        logger.info("Make sure 1 more QAS record is created ="+unprocessedQas);
        Assert.assertEquals(unprocessedQas.getClaimTypId(), MiscMap.CLAIM_TYP_NOT_SPECIFIED, "Claim type was expected to be unspecified");

        logger.info("Update Last Date for Subm cvs to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(unprocessedQas, QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");

        waitForStatementEngine(rpmDao.getSubmFile(testDb, submFileSeqId), QUEUE_WAIT_TIME);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);

        logger.info("Make sure QAS record has CLAIM_TYP_ID - CORRECTED");
        QAccnSubm processedQas = rpmDao.getQAccnSubmByAccnIdSubmFileSeqId(testDb, processedSubmFile.getSubmFileSeqId(), accnId);
        Assert.assertEquals(processedQas.getClaimTypId(), MiscMap.CLAIM_TYP_ADJUSTED_147);
        logger.info("Make sure QAS record still has SUBMITTED_SUBS_ID");
        Assert.assertEquals(processedQas.getSubmittedSubsId(), subId);

        logger.info("Make sure 5010 file is saved to the dir");
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + processedSubmFile.getFilename());
        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, unprocessedQas.getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "Corrected Institutional 5010 Claim - match the previous payment and submission on ICN")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "icn"})
    public void testPFER_760(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String icn) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER_760");

        rpmDao.deleteUnprocessedQAccnSubmByAccnId(testDb, accnId);
        List<QAccnSubm> qasToDelete = new ArrayList<>();
        QAccnSubm originalQas = null;
        for (QAccnSubm qAccnSubm : rpmDao.getQAccnSubm(testDb, accnId))
        {
            if (qAccnSubm.getClaimTypId() == MiscMap.CLAIM_TYP_NEW_141)
            {
                if (originalQas != null)
                {
                    qasToDelete.add(qAccnSubm);
                }
                else
                {
                    originalQas = qAccnSubm;
                }
            }
            else
            {
                qasToDelete.add(qAccnSubm);
            }
        }
        submissionDao.deleteSubmissions(qasToDelete);
        logger.info("Make sure there is 1 previous submission for the same pyr");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubmByAccnIdAndSubmSvc(testDb, accnId, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).getSubmSvcSeqId());
        Assert.assertEquals(qasList.size(), 1);
        Assert.assertEquals(qasList.get(0).getPyrId(), rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId());
        logger.info("Make sure pyr Subscriber Id on the Accn is different from previous QAS.SUBMITTED_SUBS_ID="+qasList.get(0).getSubmittedSubsId());
        Assert.assertNotEquals(qasList.get(0).getSubmittedSubsId(), subId);
        Assert.assertEquals(accessionDao.getAccnPyrs(accnId).get(0).getSubsId(), subId);
        logger.info("Make sure pyr QAS.SENDING_ICN is null");
        Assert.assertNull(qasList.get(0).getSendingIcn());
        logger.info("Make sure pyr QAS.CLAIM_TYP is NEW CLAIM");
        Assert.assertEquals(qasList.get(0).getClaimTypId(), MiscMap.CLAIM_TYP_NEW_141);

        logger.info("Make sure Accn primary payor has record in EOB_CLAIM with the ICN provided");
        List<EobClaim> eobClaim = rpmDao.getEobClaimsByInternalCtrlId(testDb, accnId + "Z01");
        Assert.assertEquals(eobClaim.size(), 1);
        Assert.assertEquals(eobClaim.get(0).getExternalCtrlId(), icn);
        Assert.assertEquals(eobClaim.get(0).getPyrId(), rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId());

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        logger.info("Submit a Corrected Claim on Accession Detail - Click on Submit Claim");
        clickHiddenPageObject(accessionDetail.submitClaimAddBtn(), 0);

        logger.info("Enter Claim Info into Submit Claim popup");
        accessionDetail.enterSubmitClaimInfo(pyrAbbrv, subId, submSvcAbbrev, "Replacement", icn, "D2", wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Click Save and Clear");
        accessionDetail.accnIdText().sendKeys(Keys.ALT + "S");
        wait.until(ExpectedConditions.textToBePresentInElement(accessionDetail.accessionDetailTitle(), "Detail"));

        logger.info("Make sure 1 more QAS record is created");
        QAccnSubm unprocessedQas = rpmDao.getUnprocessedQAccnSubm(testDb, accnId);
        Assert.assertEquals(unprocessedQas.getClaimTypId(), MiscMap.CLAIM_TYP_ADJUSTED_147);
        Assert.assertEquals(unprocessedQas.getSendingIcn(), icn);

        logger.info("Update Last Date for Subm cvs to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(unprocessedQas, QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(rpmDao.getSubmFile(testDb, submFileSeqId), QUEUE_WAIT_TIME);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);

        logger.info("Make sure QAS record still has CLAIM_TYP_ID - CORRECTED");
        QAccnSubm processedQas = rpmDao.getQAccnSubmByAccnIdSubmFileSeqId(testDb, processedSubmFile.getSubmFileSeqId(), accnId);
        Assert.assertEquals(processedQas.getClaimTypId(), MiscMap.CLAIM_TYP_ADJUSTED_147);
        logger.info("Make sure QAS record still has SUBMITTED_SUBS_ID");
        Assert.assertEquals(processedQas.getSubmittedSubsId(), subId);
        Assert.assertEquals(processedQas.getSendingIcn(), icn);
        logger.info("Make sure 5010 file is saved to the dir");
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + processedSubmFile.getFilename());
        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, unprocessedQas.getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "Cancelled Institutional 5010 Claim - submit claim when all procs 0 priced")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "icn"})
    public void testPFER_761(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String icn) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER_761");

        rpmDao.deleteUnprocessedQAccnSubmByAccnId(testDb, accnId);
        List<QAccnSubm> originalQAccnSubmList = rpmDao.getQAccnSubm(testDb, accnId);
        for (QAccnSubm accnSubm : originalQAccnSubmList)
        {
            if (accnSubm.getClaimTypId() != MiscMap.CLAIM_TYP_NEW_141)
            {
                try
                {
                    SubmFile submFile = rpmDao.getSubmFile(testDb, accnSubm.submFileSeqId);
                    rpmDao.deleteSubmClaimAuditBySubmFileSeqId(submFile.getSubmFileSeqId());
                    rpmDao.updateAccnProcSubmFileSeqId(testDb, submFile.getSubmFileSeqId());
                    rpmDao.deleteSubmFileAuditBySubmFileSeqId(submFile.getSubmFileSeqId());
                    rpmDao.deleteQAccnSubmBySubmFileSeqId(testDb, submFile.getSubmFileSeqId());
                    rpmDao.deleteSubmFile(testDb, submFile.getSubmFileSeqId());
                }
                catch (Exception e)
                {
                    logger.info("Could not finish the cleanup, accnId="+accnId, e);
                }
            }
            else
            {
                SubmFile submFile = rpmDao.getSubmFile(testDb, accnSubm.submFileSeqId);
                if (!submFile.getIsEgateProcessed())
                {

                    rpmDao.updateAccnProcSubmFileSeqId(testDb, submFile.getSubmFileSeqId());
                    rpmDao.deleteQAccnSubmBySubmFileSeqId(testDb, submFile.getSubmFileSeqId());
                    rpmDao.deleteSubmFile(testDb, submFile.getSubmFileSeqId());
                }
            }
        }

        logger.info("Make sure there is 1 previous submission for the same pyr");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubmByAccnIdAndSubmSvc(testDb, accnId, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).getSubmSvcSeqId());
        Assert.assertEquals(qasList.size(), 1);
        Assert.assertEquals(qasList.get(0).getPyrId(), rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId());
        List<AccnProc> accnProcs = rpmDao.getAccnProcsByAccnId(null, accnId);
        logger.info("Make sure pyr QAS.SENDING_ICN is null");
        Assert.assertNull(qasList.get(0).getSendingIcn());
        logger.info("Make sure pyr QAS.CLAIM_TYP is NEW CLAIM");
        Assert.assertEquals(qasList.get(0).getClaimTypId(), MiscMap.CLAIM_TYP_NEW_141);
        logger.info("Make sure accn_proc on Accn is 0 price="+accnProcs.get(0).getBilPrcAsMoney());
        Assert.assertNotEquals(new Money(0), accnProcs.get(0).getBilPrcAsMoney());

        logger.info("Make sure Accn primary payor has record in EOB_CLAIM with the ICN provided");
        List<EobClaim> eobClaim = rpmDao.getEobClaimsByInternalCtrlId(testDb, accnId + "Z03");
        Assert.assertEquals(eobClaim.size(), 1);
        Assert.assertEquals(eobClaim.get(0).getExternalCtrlId(), icn);
        Assert.assertEquals(eobClaim.get(0).getPyrId(), rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId());

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        logger.info("Submit a Cancelled Claim on Accession Detail - Click on Submit Claim");
        clickHiddenPageObject(accessionDetail.submitClaimAddBtn(), 0);

        logger.info("Enter Claim Info into Submit Claim popup");
        accessionDetail.enterSubmitClaimInfo(pyrAbbrv, subId, submSvcAbbrev, "Cancelled", icn, "D6", wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Click Save and Clear");
        accessionDetail.accnIdText().sendKeys(Keys.ALT + "S");
        wait.until(ExpectedConditions.textToBePresentInElement(accessionDetail.accessionDetailTitle(), "Detail"));

        logger.info("Make sure 1 more QAS record is created");
        QAccnSubm unprocessedQas = rpmDao.getUnprocessedQAccnSubm(testDb, accnId);
        Assert.assertEquals(unprocessedQas.getClaimTypId(), MiscMap.CLAIM_TYP_CANCELLED_148);
        Assert.assertEquals(unprocessedQas.getSendingIcn(), icn);

        logger.info("Update Last Date for Subm cvs to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(unprocessedQas, QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(rpmDao.getSubmFile(testDb, submFileSeqId), QUEUE_WAIT_TIME);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);

        logger.info("Make sure QAS record still has CLAIM_TYP_ID - CANCELLED");
        QAccnSubm processedQas = rpmDao.getQAccnSubmByAccnIdSubmFileSeqId(testDb, processedSubmFile.getSubmFileSeqId(), accnId);
        Assert.assertEquals(processedQas.getClaimTypId(), MiscMap.CLAIM_TYP_CANCELLED_148);
        logger.info("Make sure QAS record still has SUBMITTED_SUBS_ID");
        Assert.assertEquals(processedQas.getSubmittedSubsId(), subId);
        Assert.assertEquals(processedQas.getSendingIcn(), icn);
        logger.info("Make sure 5010 file is saved to the dir");
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + processedSubmFile.getFilename());
        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, unprocessedQas.getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "Cancelled Professional Crossover Claim Is Generated")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "claimId", "icn"})
    public void testCancelledProfessionalCrossoverClaim(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String claimId, String icn) throws Exception
    {
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(null, pyrAbbrv);

        logger.info("Cleaning up prior submissions for the payor, accnId="+accnId+", pyrAbbrv="+pyrAbbrv);
        rpmDao.deleteUnprocessedQAccnSubmByAccnId(testDb, accnId);
        List<QAccnSubm> qAccnSubmsToDelete = new ArrayList<>();
        for (QAccnSubm qas : rpmDao.getQAccnSubm(null, accnId))
        {
            if (qas.getPyrId() == pyr.getPyrId())
            {
                qAccnSubmsToDelete.add(qas);
            }
        }
        submissionDao.deleteSubmissions(qAccnSubmsToDelete);

        logger.info("Make sure there is no submission for the payor, accnId="+accnId+", pyrAbbrv="+pyrAbbrv);
        for (QAccnSubm qas : rpmDao.getQAccnSubm(null, accnId))
        {
            if (qas.getPyrId() == pyr.getPyrId())
            {
                Assert.fail("Found prior submission for payor, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", docSeqId="+qas.getDocSeqId());
            }
        }

        logger.info("Make sure payor has EOB for ICN, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", icn="+icn+", claimId="+claimId);
        List<EobClaim> eobClaims = new ArrayList<>();
        for (EobClaim eobClaim : rpmDao.getEobClaimsByInternalCtrlId(testDb, claimId))
        {
            if (eobClaim.getPyrId() == pyr.getPyrId())
            {
                eobClaims.add(eobClaim);
            }
        }
        Assert.assertEquals(eobClaims.size(), 1);
        Assert.assertEquals(eobClaims.get(0).getExternalCtrlId(), icn);
        Assert.assertEquals(eobClaims.get(0).getPyrId(), pyr.getPyrId());

        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Submit a Cancelled claim on AccnDetail, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", icn="+icn);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        clickHiddenPageObject(accessionDetail.submitClaimAddBtn(), 0);
        accessionDetail.enterSubmitClaimInfo(pyrAbbrv, subId, submSvcAbbrev, "Cancelled", icn, null, wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.accnIdText().sendKeys(Keys.ALT + "S");
        wait.until(ExpectedConditions.textToBePresentInElement(accessionDetail.accessionDetailTitle(), "Detail"));

        logger.info("Make sure the submission is queued, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", claimTyp="+MiscMap.CLAIM_TYP_CANCELLED_8);
        QAccnSubm unprocessedQas = rpmDao.getUnprocessedQAccnSubm(testDb, accnId);
        Assert.assertEquals(unprocessedQas.getPyrId(), pyr.getPyrId());
        Assert.assertEquals(unprocessedQas.getClaimTypId(), MiscMap.CLAIM_TYP_CANCELLED_8);
        Assert.assertEquals(unprocessedQas.getSendingIcn(), icn);

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
    }


    @Test(priority = 1, description = "Replacement Institutional 5010 Claim is generated when Test has been added")
    @Parameters({"accnId", "pyrAbbrv", "submSvcAbbrev", "claimId"})
    public void testInstitutionalReplacementClaimIsGeneratedWhenTestIsAdded(String accnId, String pyrAbbrv, String submSvcAbbrev, String claimId) throws Exception
    {
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(null, pyrAbbrv);
        Accn accn = accessionDao.getAccn(accnId);

        logger.info("Cleaning up prior submissions for the payor, accnId="+accnId+", pyrAbbrv="+pyrAbbrv);
        rpmDao.deleteUnprocessedQAccnSubmByAccnId(testDb, accnId);
        boolean isOriginalClaimFound = false;
        List<QAccnSubm> qAccnSubmsToDelete = new ArrayList<>();
        List<QAccnSubm> originalQAccnSubms = new ArrayList<>();
        for (QAccnSubm qas : rpmDao.getQAccnSubm(null, accnId))
        {
            if (qas.getPyrId() == pyr.getPyrId() && StringUtils.equals(claimId, qas.getAccnId() + "Z" + qas.getClaimIdSuffix()))
            {
                isOriginalClaimFound = true;
                originalQAccnSubms.add(qas);
            }
            else
            {
                qAccnSubmsToDelete.add(qas);
            }
        }
        submissionDao.deleteSubmissions(qAccnSubmsToDelete);
        Assert.assertEquals(originalQAccnSubms.size(), 1, "Original claim submission count should be 1, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", claimId="+claimId);
        Assert.assertTrue(isOriginalClaimFound, "Original claim not found, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", claimId="+claimId);

        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertFalse(accnPyrs.isEmpty(), "No payors found on accession, accnId="+accnId);
        String origOtherInfo = accnPyrs.get(0).getOtherInfo();
        Assert.assertFalse(StringUtils.isBlank(origOtherInfo), "AccnPyr Other Info should not be blank, accnId="+accnId);

        Pyr actualPyr = rpmDao.getPyrByPyrId(null, accnPyrs.get(0).getPyrId());
        Assert.assertEquals(actualPyr.getPyrAbbrv(), pyrAbbrv, "Expected payor not found on accession, accnId="+accnId+", expectedPyrAbbrv="+pyrAbbrv+", actualPyrAbbrev="+actualPyr.getPyrAbbrv());

        int origPrcCnt = accn.getPrcCnt();
        logger.info("Repricing accession, accnId="+accnId+", prcCnt="+origPrcCnt);
        QValidateAccn qva;
        try
        {
            qva = accessionDao.getQValidateAccn(accnId);
        }
        catch (XifinDataNotFoundException e)
        {
            qva = new QValidateAccn();
            qva.setAccnId(accnId);
        }
        qva.setValidateTypId(ValidateQueueMap.RE_VALIDATE_RE_PRICE);
        qva.setPriority(3);
        qva.setIsErr(false);
        qva.setStatus(null);
        accessionDao.setQValidateAccn(qva);

        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfValidationQueue(accnId, ValidateQueueMap.RE_VALIDATE_RE_PRICE, QUEUE_WAIT_TIME));
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Expected accn to leave eligibility queue, accnId=" + accnId);
        Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME), "Expected accn to leave pricing queue, accnId=" + accnId);

        accn = accessionDao.getAccn(accnId);
        Assert.assertEquals(accn.getPrcCnt(), origPrcCnt+1, "Expected accn price count to be incremented, accnId=" + accnId+", origPrcCnt="+origPrcCnt+", newPrcCnt="+accn.getPrcCnt());

        List<QAccnSubm> newQAccnSubms = rpmDao.getQAccnSubm(null, accnId);
        Assert.assertEquals(newQAccnSubms.size(), 2, "Expected 1 new claim submission, accnId=" + accnId+", newSubmissionCnt="+(newQAccnSubms.size()-1));

        logger.info("Resetting submission service, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        int submFileSeqId = waitForSubmissionEngine(newQAccnSubms.get(1), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);
        Assert.assertTrue(waitForStatementEngine(rpmDao.getSubmFile(testDb, submFileSeqId), QUEUE_WAIT_TIME));

        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Make sure processed submission record has correct claim type, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", claimType="+MiscMap.CLAIM_TYP_ADJUSTED_147);
        QAccnSubm qas = rpmDao.getQAccnSubmByAccnIdSubmFileSeqId(testDb, submFile.getSubmFileSeqId(), accnId);
        Assert.assertEquals(qas.getClaimTypId(), MiscMap.CLAIM_TYP_ADJUSTED_147);

        logger.info("Make sure AccnPyr Other Info is not cleared, accnId="+accnId+", pyrAbbrv="+pyrAbbrv);
        accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertFalse(accnPyrs.isEmpty(), "No payors found on accession, accnId="+accnId);
        Assert.assertNotNull(accnPyrs.get(0).getOtherInfo(), "AccnPyr Other Info should not be cleared, accnId="+accnId);
        Assert.assertEquals(accnPyrs.get(0).getOtherInfo(), origOtherInfo, "AccnPyr Other Info should not be changed, accnId="+accnId+", expectedOtherInfo="+origOtherInfo+", actualOtherInfo="+accnPyrs.get(0).getOtherInfo());
    }
}
