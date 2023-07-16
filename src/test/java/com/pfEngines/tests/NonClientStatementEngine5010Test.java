package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accnOccurrence.AccnOccurrence;
import com.mbasys.mars.ejb.entity.accnPhys.AccnPhys;
import com.mbasys.mars.ejb.entity.accnPmt.AccnPmt;
import com.mbasys.mars.ejb.entity.accnProc.AccnProc;
import com.mbasys.mars.ejb.entity.accnProcErr.AccnProcErr;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.accnTest.AccnTest;
import com.mbasys.mars.ejb.entity.errCd.ErrCd;
import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.ejb.entity.phys.Phys;
import com.mbasys.mars.ejb.entity.physXref.PhysXref;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrSubmSvcProcOvrrdes.PyrSubmSvcProcOvrrdes;
import com.mbasys.mars.ejb.entity.qAccnSubm.QAccnSubm;
import com.mbasys.mars.ejb.entity.submClaimAudit.SubmClaimAudit;
import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.mbasys.mars.ejb.entity.submFileAudit.SubmFileAudit;
import com.mbasys.mars.ejb.entity.submSvc.SubmSvc;
import com.mbasys.mars.ejb.entity.testXref.TestXref;
import com.mbasys.mars.ejb.entity.xref.Xref;
import com.mbasys.mars.ejb.entity.xrefTyp.XrefTyp;
import com.mbasys.mars.errorProcessing.EpConstants;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.MiscMap;
import com.mchange.io.FileUtils;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.accession.accessionProcessing.superSearch.SuperSearch;
import com.overall.menu.MenuNavigation;
import com.xifin.automation.utils.Statement837Utils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NonClientStatementEngine5010Test extends NonClientStatementEngineTest
{
    @Test(priority = 1, description = "STMTSUBIDGRPID error added when SubId is equal to GrpId")
    @Parameters({"accnId", "pyrAbbrv", "subId", "grpId", "submSvcAbbrev", "errorCdId", "supportedBillTypes"})
    public void testPFER_577(String accnId, String pyrAbbrv, String subId, String grpId, String submSvcAbbrev, String errorCdId, String supportedBillTypes) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-577");
        cleanUpAccn(accnId);
        logger.info("Delete error on Accn " + errorCdId);
        accessionDao.deleteAccnPyrErrByAccnIdErrCd(accnId, Integer.parseInt(errorCdId));

        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);

        logger.info("Make sure Primary pyr is setup with only Claim Type 14X");
        Assert.assertEquals(rpmDao.getPyrBillTyp(testDb, pyr.getPyrBilTypId14x()).getAbbrev(), supportedBillTypes);
        Assert.assertEquals(pyr.getPyrBilTypId13x(), 0);
        Assert.assertEquals(pyr.getPyrBilTypId85x(), 0);
        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        logger.info("Make sure GroupId and SubId are equal");
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertEquals(accnPyrs.get(0).getGrpId(), accnPyrs.get(0).getSubsId());
        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm Svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        boolean IsStatementEngineDeletedFile = waitForStatementEngineToDeleteFile(accnId, QUEUE_WAIT_TIME);

        qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 0);

        ErrCd errorCd = rpmDao.getErrCd(testDb, Integer.parseInt(errorCdId));

        logger.info("Find New Error added to the accn");
        List<AccnPyrErr> newAccnPyrErr = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        Assert.assertEquals(newAccnPyrErr.size(), 1);
        Assert.assertEquals(newAccnPyrErr.get(0).getPyrPrio(), 1);
        Assert.assertEquals(newAccnPyrErr.get(0).getPyrId(), pyr.getPyrId());
        Assert.assertEquals(newAccnPyrErr.get(0).getSubsId(), subId);
        Assert.assertEquals(newAccnPyrErr.get(0).getSubsId(), grpId);
        Assert.assertEquals(errorCd.getErrGrpId(), EpConstants.ERR_GRP_UNBILLABLE);
        Assert.assertEquals(errorCd.getAbbrev(), errorCd.getAbbrev());

        logger.info("Reload Accn on Accn Detail");
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        logger.info("Verifying open error is on accession, accnId=" + accnId + " errCd= " + errorCd.getAbbrev());
        Assert.assertEquals(accessionDetail.reasonCodeText(0, 4).getText(), errorCd.getAbbrev() + "-" + errorCd.getShortDescr());

        logger.info("Click Save and Clear");
        accessionDetail.accnIdText().sendKeys(Keys.ALT + "S");
        wait.until(ExpectedConditions.textToBePresentInElement(accessionDetail.accessionDetailTitle(), "Detail"));
    }

    @Test(priority = 1, description = "STMTSUBIDGRPID error IS NOT added when SubId is different to GrpId,  Original Claim Type 13X, Client Ordering Fac is used")
    @Parameters({"accnId", "pyrAbbrv", "subId", "grpId", "submSvcAbbrev", "supportedBillTypes", "npiStmtConfig", "facAbbrv"})
    public void testPFER_578(String accnId, String pyrAbbrv, String subId, String grpId, String submSvcAbbrev, String supportedBillTypes, String npiStmtConfig, String facAbbrv) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-578");
        cleanUpAccn(accnId);

        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        logger.info("Make sure Primary pyr is setup with only Claim Type 14X");
        Assert.assertEquals(rpmDao.getPyrBillTyp(testDb, pyr.getPyrBilTypId14x()).getAbbrev(), supportedBillTypes);
        Assert.assertEquals(pyr.getPyrBilTypId13x(), 0);
        Assert.assertEquals(pyr.getPyrBilTypId85x(), 0);

        logger.info("Make sure Performing Facility is a type 5 - Reference Lab and there is no Bill To Fac");
        Fac performingFac = rpmDao.getFacByFacId(testDb, rpmDao.getAccnProcsByAccnId(testDb, accnId).get(0).getFacId());
        Assert.assertEquals(performingFac.getFacTypId(), 5);
        Assert.assertEquals(performingFac.getBillToFacId(), 0);

        Fac fac = rpmDao.getFacByAbbrv(testDb, facAbbrv);

        logger.info("Get Ordering Facility - Client Facility");
        Fac orderingFac = rpmDao.getFacByFacId(testDb, rpmDao.getClnByClnId(testDb, accessionDao.getAccn(accnId).getClnId()).getOrderingFacId());

        logger.info("Make sure GroupId and SubId are different");
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);
        Assert.assertNotEquals(accnPyrs.get(0).getGrpId(), accnPyrs.get(0).getSubsId());
        Assert.assertEquals(accnPyrs.get(0).getGrpId(), grpId);
        Assert.assertEquals(accnPyrs.get(0).getSubsId(), subId);

        logger.info("Enter Claim Info into Submit Claim popup");
        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);
        QAccnSubm qAccnSubm = rpmDao.getUnprocessedQAccnSubm(testDb, accnId);

        logger.info("Update Last Date for Subm Svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(rpmDao.getSubmFile(testDb, submFileSeqId), QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).size(), 0);

        logger.info("Make sure 5010 file is saved to the dir");
        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + processedSubmFile.getFilename());

        logger.info("Make sure New 5010 Statement File contains grpId and subId from accn_pyr.grp_id, accn_pyr.subId");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data = bufferedReader.readLine();

        Assert.assertTrue(data.contains(grpId));
        Assert.assertTrue(data.contains(subId));

        logger.info("Make sure New 5010 Institutional Statement File contains Original Claim Type 13X");
        Assert.assertTrue(data.contains("*14:A:1*"));

        logger.info("Make sure New 5010 Institutional Statement File contains Ordering Facility NPI");
        Assert.assertTrue(data.contains("NM1*85*2*ACME LAB*****XX*" + orderingFac.getNpi()));

        logger.info("Make sure subm_claim_audit record is created for 837");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(testDb, qAccnSubm.getDocSeqId());
        logger.info("SubmClaimAudit ValueObject [" + submClaimAudit.toString() + "]");
        logger.info("Expected values, facId=" + fac.getFacId() + ", npi=" + npiStmtConfig + ", facName=" + fac.getName());
        Assert.assertEquals(submClaimAudit.getBillingFacId(), fac.getFacId());
        Assert.assertEquals(String.valueOf(submClaimAudit.getSubmittedNpi()), npiStmtConfig);
        Assert.assertEquals(submClaimAudit.getSubmittedFacName(), fac.getName());

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "5010 Institutional statement - no blank HI* segment when Occurrence Code is set for different pyr")
    @Parameters({"accnId", "primPyrAbbrv", "secondPyrAbbrv", "subId", "submSvcAbbrev", "occurCode", "supportedBillTypes", "npi", "facAbbrv"})
    public void testPFER_579(String accnId, String primPyrAbbrv, String secondPyrAbbrv, String subId, String submSvcAbbrev, String occurCode, String supportedBillTypes, String npi, String facAbbrv) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-579");
        cleanUpAccn(accnId);
        logger.info("Make sure Occurrence Codes are saved in DB");
        List<AccnOccurrence> accnOccurrences = rpmDao.getAccnOccurenceByAccnId(testDb, accnId);
        Assert.assertEquals(accnOccurrences.size(), 1, "Did not find expected number of occurrence codes");

        Pyr primPyr = rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv);

        logger.info("Make sure Primary pyr is setup with only Claim Type 14X");
        Assert.assertEquals(rpmDao.getPyrBillTyp(testDb, primPyr.getPyrBilTypId13x()).getAbbrev(), supportedBillTypes);
        Assert.assertEquals(primPyr.getPyrBilTypId14x(), 0);
        Assert.assertEquals(primPyr.getPyrBilTypId85x(), 0);

        Fac fac = rpmDao.getFacByAbbrv(testDb, facAbbrv);

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

//        logger.info("Click on Add Occurrence Codes button");
//        clickHiddenPageObject(accessionDetail.occurrenceCodeAddBtn(), 0);
//        logger.info("Enter Occurrence record for the secondary pyr to popup - Occurrent Code - " + occurCode);
//        enterOccurrenceCode(accessionDetail, secondPyrAbbrv, occurCode);

        logger.info("Make sure accn is loaded");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Click on Submit Claim");
        clickHiddenPageObject(accessionDetail.submitClaimAddBtn(), 0);

        logger.info("Enter Claim Info for the primary pyr with Condition Code into Submit Claim popup, pyrAbbrev - " + primPyrAbbrv);
        accessionDetail.enterSubmitClaimInfo(primPyrAbbrv, subId, submSvcAbbrev, "", "", "0E", wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Click Save and Clear");
        accessionDetail.accnIdText().sendKeys(Keys.ALT + "S");
        wait.until(ExpectedConditions.textToBePresentInElement(accessionDetail.accessionDetailTitle(), "Detail"));

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm Svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed());

        logger.info("Make sure No New Errors added to the accn");
        List<AccnPyrErr> newAccnPyrErr = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        Assert.assertEquals(newAccnPyrErr.size(), 0);
        QAccnSubm qAccnSubm = rpmDao.getQAccnSubm(testDb, accnId).get(0);
        logger.info("Make sure 5010 file is saved to the dir");
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + processedSubmFile.getFilename());

        logger.info("Make sure New 5010 Institutional Statement File does not contain NOT matching pyr Occurrence code, No blank HI* segment");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data = bufferedReader.readLine();
        Assert.assertTrue(data.contains(qAccnSubm.getAccnId() + "Z" + qAccnSubm.getClaimIdSuffix()));
        Assert.assertFalse(data.contains("HI*~HI"));
        logger.info("Make sure New 5010 Institutional Statement File contains matching pyr Occurrence code");
        Assert.assertFalse(data.contains("BH:" + occurCode.substring(0, 2) + ":"));
        logger.info("Make sure New 5010 Institutional Statement File contains Original Claim Type 13X");
        Assert.assertTrue(data.contains("*13:A:1*"));

        logger.info("Make sure subm_claim_audit record is created for 837");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(testDb, qAccnSubm.getDocSeqId());
        logger.info("Expected values, facId=" + fac.getFacId() + ", npi=" + npi + ", facName=" + fac.getName());
        Assert.assertEquals(submClaimAudit.getBillingFacId(), fac.getFacId());
        Assert.assertEquals(String.valueOf(submClaimAudit.getSubmittedNpi()), npi);
        Assert.assertEquals(submClaimAudit.getSubmittedFacName(), fac.getName());

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "5010 Institutional statement - matching Occurrence code in HI segment when Occurrence code setup for pyr")
    @Parameters({"accnId", "primPyrAbbrv", "subId", "submSvcAbbrev", "occurCode1", "occurCode2", "npi", "facAbbrv"})
    public void testPFER_580(String accnId, String primPyrAbbrv, String subId, String submSvcAbbrev, String occurCode1, String occurCode2, String npi, String facAbbrv) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-580");
        cleanUpAccn(accnId);
        logger.info("Make sure Occurrence Codes are saved in DB");
        List<AccnOccurrence> accnOccurrences = rpmDao.getAccnOccurenceByAccnId(testDb, accnId);
        Assert.assertEquals(accnOccurrences.size(), 2, "Did not find expected number of occurrence codes");

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        logger.info("Make sure accn is loaded");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Click on Submit Claim");
        clickHiddenPageObject(accessionDetail.submitClaimAddBtn(), 0);

        logger.info("Enter Claim Info for the primary Pyr with Condition Code into Submit Claim popup, primary Pyr - " + primPyrAbbrv);
        accessionDetail.enterSubmitClaimInfo(primPyrAbbrv, subId, submSvcAbbrev, "", "", "0E", wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Click Save and Clear");
        accessionDetail.accnIdText().sendKeys(Keys.ALT + "S");
        wait.until(ExpectedConditions.textToBePresentInElement(accessionDetail.accessionDetailTitle(), "Detail"));

        accnOccurrences = rpmDao.getAccnOccurenceByAccnId(testDb, accnId);
        Assert.assertEquals(accnOccurrences.size(), 2);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm Svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).size(), 0);
        QAccnSubm qAccnSubm = rpmDao.getQAccnSubm(testDb, accnId).get(0);

        logger.info("Make sure 5010 file is saved to the dir");
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + processedSubmFile.getFilename());
        Fac fac = rpmDao.getFacByAbbrv(testDb, facAbbrv);

        logger.info("Make sure in the New 5010 Institutional Statement File blank HI segment is not displayed");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data = bufferedReader.readLine();
        Assert.assertTrue(data.contains(qAccnSubm.getAccnId() + "Z" + qAccnSubm.getClaimIdSuffix()));
        Assert.assertFalse(data.contains("HI*~HI"));
        logger.info("Make sure New 5010 Institutional Statement File contains both matching pyr Occurrence code");
        Assert.assertTrue(data.contains("BH:" + occurCode1.substring(0, 2) + ":"));
        Assert.assertTrue(data.contains("*BH:" + occurCode2.substring(0, 2) + ":"));

        logger.info("Make sure subm_claim_audit record is created for 837");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(testDb, qAccnSubm.getDocSeqId());
        logger.info("Expected values, facId=" + fac.getFacId() + ", npi=" + npi + ", facName=" + fac.getName());
        Assert.assertEquals(submClaimAudit.getBillingFacId(), fac.getFacId());
        Assert.assertEquals(String.valueOf(submClaimAudit.getSubmittedNpi()), npi);
        Assert.assertEquals(submClaimAudit.getSubmittedFacName(), fac.getName());

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "5010 Institutional statement - Claim Type 85X is supported for CAH Fac")
    @Parameters({"accnId", "primPyrAbbrv", "subId", "submSvcAbbrev", "supportedBillTypes13", "supportedBillTypes85"})
    public void testPFER_629(String accnId, String primPyrAbbrv, String subId, String submSvcAbbrev, String supportedBillTypes13, String supportedBillTypes85) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-629");
        cleanUpAccn(accnId);

        Pyr primPyr = rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv);

        logger.info("Make sure Primary pyr is setup with only Claim Type 13X and 85X");
        Assert.assertEquals(rpmDao.getPyrBillTyp(testDb, primPyr.getPyrBilTypId13x()).getAbbrev(), supportedBillTypes13);
        Assert.assertEquals(primPyr.getPyrBilTypId14x(), 0);
        Assert.assertEquals(rpmDao.getPyrBillTyp(testDb, primPyr.getPyrBilTypId85x()).getAbbrev(), supportedBillTypes85);

        logger.info("Make sure Billing Facility is a Critical Access Facility");
        Fac performingFac = rpmDao.getFacByFacId(testDb, rpmDao.getAccnProcsByAccnId(testDb, accnId).get(0).getFacId());
        Assert.assertEquals(performingFac.getFacSpecialtyTypId(), 1);

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        logger.info("Click on Submit Claim");
        clickHiddenPageObject(accessionDetail.submitClaimAddBtn(), 0);
        logger.info("Enter Claim Info for the primary pyr with Condition Code into Submit Claim popup, pyrAbbrev - " + primPyrAbbrv);
        accessionDetail.enterSubmitClaimInfo(primPyrAbbrv, subId, submSvcAbbrev, "", "", "0E", wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Click Save and Clear");
        accessionDetail.accnIdText().sendKeys(Keys.ALT + "S");
        wait.until(ExpectedConditions.textToBePresentInElement(accessionDetail.accessionDetailTitle(), "Detail"));

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm Svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).getSubmSvcSeqId());

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).size(), 0);
        QAccnSubm qAccnSubm = rpmDao.getQAccnSubm(testDb, accnId).get(0);

        logger.info("Make sure 5010 file is saved to the dir");
        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + processedSubmFile.getFilename());
        logger.info("Make sure New 5010 Institutional Statement File contains Original Claim Type 85X");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data = bufferedReader.readLine();
        Assert.assertTrue(data.contains(qAccnSubm.getAccnId() + "Z" + qAccnSubm.getClaimIdSuffix()));
        Assert.assertTrue(data.contains("*85:A:1*"));

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "Performing fac is fac_type=4 - Performing fac is used on 5010 Instit Statement")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "supportedBillTypes13", "supportedBillTypes85"})
    public void testPFER_630(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String supportedBillTypes13, String supportedBillTypes85) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-630");
        cleanUpAccn(accnId);

        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);

        logger.info("Make sure Primary pyr is setup with Claim Types 13X and 85X");
        Assert.assertEquals(rpmDao.getPyrBillTyp(testDb, pyr.getPyrBilTypId13x()).getAbbrev(), supportedBillTypes13);
        Assert.assertEquals(pyr.getPyrBilTypId14x(), 0);
        Assert.assertEquals(rpmDao.getPyrBillTyp(testDb, pyr.getPyrBilTypId85x()).getAbbrev(), supportedBillTypes85);

        logger.info("Make sure Performing Facility is a type 4 - Remote Affiliate and there is no Bill To Fac");
        Fac performingFac = rpmDao.getFacByFacId(testDb, rpmDao.getAccnProcsByAccnId(testDb, accnId).get(0).getFacId());
        Assert.assertEquals(performingFac.getFacTypId(), 4);
        Assert.assertEquals(performingFac.getBillToFacId(), 0);

        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait);
        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);
        Assert.assertEquals(qasList.get(0).getClaimTypId(), MiscMap.CLAIM_TYP_NEW_851);

        logger.info("Update Last Date for Subm Svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);

        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed());

        logger.info("Make sure No New Errors added to the accn");
        List<AccnPyrErr> newAccnPyrErr = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        Assert.assertEquals(newAccnPyrErr.size(), 0);

        logger.info("Make sure 5010 file is saved to the dir");
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + processedSubmFile.getFilename());
        logger.info("Make sure New 5010 Statement File contains grpId and subId from accn_pyr.grp_id, accn_pyr.subId");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data = bufferedReader.readLine();
        Assert.assertTrue(data.contains(subId));

        logger.info("Make sure New 5010 Institutional Statement File contains Original Claim Type 85X");
        Assert.assertTrue(data.contains("*85:A:1*"));

        logger.info("Make sure New 5010 Institutional Statement File contains Ordering Facility NPI");
        Assert.assertTrue(data.contains("NM1*85*2*" + performingFac.getName() + "*****XX*" + performingFac.getNpi()));

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "5010 Institutional statement / statement_config override is on")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "billingFac", "stmtConf201024", "stmtConfig220032"})
    public void testPFER_631(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String billingFac, String stmtConf201024, String stmtConfig220032) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-631");
        cleanUpAccn(accnId);

        logger.info("Make sure Statement Config 201024 submitter override is setup for Billing Facility and Submission Service");
        SubmSvc submSvc = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        String stmtConfig201024StmtData = rpmDao.getStatementConfigStmtDataByFacIdAndSubmSvc(testDb, Integer.parseInt(billingFac), submSvc.getSubmSvcSeqId(), 201024);
        Assert.assertTrue(stmtConfig201024StmtData.contains(stmtConf201024));
        String stmtConfig220032StmtData = rpmDao.getStatementConfigStmtDataByFacIdAndSubmSvc(testDb, Integer.parseInt(billingFac), submSvc.getSubmSvcSeqId(), 220032);
        Assert.assertTrue(stmtConfig220032StmtData.contains(stmtConfig220032));

        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait);
        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm Svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submSvc.submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(rpmDao.getSubmFile(testDb, submFileSeqId), QUEUE_WAIT_TIME);

        logger.info("Make sure No New Errors added to the accn");
        List<AccnPyrErr> newAccnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        Assert.assertEquals(newAccnPyrErrs.size(), 0);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Make sure 5010 file is saved to the dir");
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + processedSubmFile.getFilename());
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data = bufferedReader.readLine();

        logger.info("Make sure File contains 1 accn in the file");
        Assert.assertTrue(data.contains("CLM*" + accnId));
        Pattern patternAccn = Pattern.compile("CLM\\*" + accnId);
        Matcher matcherAccn = patternAccn.matcher(data);
        int accnIdsCount = 0;
        while (matcherAccn.find())
        {
            accnIdsCount++;
        }
        Assert.assertEquals(accnIdsCount, 1);

        logger.info("Make sure New 5010 Institutional Statement File contains submitter override Statement Config 201024 Override data in GS*HC");
        Assert.assertTrue(data.contains("GS*HC*" + stmtConf201024));
        int countOfGS = 0;
        Pattern patternGS = Pattern.compile("GS\\*HC\\*" + stmtConf201024);
        Matcher matcherGS = patternGS.matcher(data);
        while (matcherGS.find())
        {
            countOfGS++;
        }
        Assert.assertEquals(countOfGS, 1);

        logger.info("Make sure New 5010 Institutional Statement File contains submitter override from Statement Config 220032");
        Assert.assertTrue(data.contains("NM1*41*2*XIFIN, INC.*****46*" + stmtConfig220032));
        int countOfNM1 = 0;
        Pattern patternNM1 = Pattern.compile("NM1\\*41\\*2\\*XIFIN\\, INC\\.\\*\\*\\*\\*\\*46\\*" + stmtConfig220032);
        Matcher matcherNM1 = patternNM1.matcher(data);
        while (matcherNM1.find())
        {
            countOfNM1++;
        }
        Assert.assertEquals(countOfNM1, 1);

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "5010 Institutional statement / statement_config override is off")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "billingFac"})
    public void testPFER_632(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String billingFac) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-632");
        cleanUpAccn(accnId);

        logger.info("Make sure Statement Config 201024 submitter override is NOT setup for Billing Facility and Submission Service");
        SubmSvc submSvc = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        String stmtConfig201024StmtData = rpmDao.getStatementConfigStmtDataByFacIdAndSubmSvc(testDb, Integer.parseInt(billingFac), submSvc.getSubmSvcSeqId(), 201024);
        Assert.assertNull(stmtConfig201024StmtData);
        String stmtConfig220032StmtData = rpmDao.getStatementConfigStmtDataByFacIdAndSubmSvc(testDb, Integer.parseInt(billingFac), submSvc.getSubmSvcSeqId(), 220032);
        Assert.assertNull(stmtConfig220032StmtData);

        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait);
        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm Svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submSvc.submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);

        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed());

        logger.info("Make sure No New Errors added to the accn");
        List<AccnPyrErr> newAccnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        Assert.assertEquals(newAccnPyrErrs.size(), 0);

        logger.info("Make sure 5010 file is saved to the dir");
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + processedSubmFile.getFilename());
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data = bufferedReader.readLine();

        logger.info("Make sure File contains 1 accn in the file");
        Assert.assertTrue(data.contains("CLM*" + accnId));
        Pattern patternAccn = Pattern.compile("CLM\\*" + accnId);
        Matcher matcherAccn = patternAccn.matcher(data);
        int accnIdsCount = 0;
        while (matcherAccn.find())
        {
            accnIdsCount++;
        }
        Assert.assertEquals(accnIdsCount, 1);

        logger.info("Make sure New 5010 Institutional Statement File contains submitter from SUBM_SVC.GRP_SUBMTR_ID in GS*HC");
        Assert.assertTrue(data.contains("GS*HC*" + submSvc.getGrpSubmtrId()));
        int countOfGS = 0;
        Pattern patternGS = Pattern.compile("GS\\*HC\\*" + submSvc.getGrpSubmtrId());
        Matcher matcherGS = patternGS.matcher(data);
        while (matcherGS.find())
        {
            countOfGS++;
        }
        Assert.assertEquals(countOfGS, 1);

        logger.info("Make sure New 5010 Institutional Statement File contains submitter from SUBM_SVC.TXN_HDR_SUBMTR_ID in 1000ANM109");
        Assert.assertTrue(data.contains("NM1*41*2*XIFIN, INC.*****46*" + submSvc.getTxnHdrSubmtrId()));
        int countOfNM1 = 0;
        Pattern patternNM1 = Pattern.compile("NM1\\*41\\*2\\*XIFIN\\, INC\\.\\*\\*\\*\\*\\*46\\*" + submSvc.getTxnHdrSubmtrId());
        Matcher matcherNM1 = patternNM1.matcher(data);
        while (matcherNM1.find())
        {
            countOfNM1++;
        }
        Assert.assertEquals(countOfNM1, 1);

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "5010 Professional / statement_config override is on")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "billingFac", "stmtConf201024", "stmtConfig220032"})
    public void testPFER_633(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String billingFac, String stmtConf201024, String stmtConfig220032) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-633");
        cleanUpAccn(accnId);

        logger.info("Make sure Statement Config 201024 submitter override is setup for Billing Facility and Submission Service");
        SubmSvc submSvc = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        String stmtConfig201024StmtData = rpmDao.getStatementConfigStmtDataByFacIdAndSubmSvc(testDb, Integer.parseInt(billingFac), submSvc.getSubmSvcSeqId(), 201024);
        Assert.assertTrue(stmtConfig201024StmtData.contains(stmtConf201024));
        String stmtConfig220032StmtData = rpmDao.getStatementConfigStmtDataByFacIdAndSubmSvc(testDb, Integer.parseInt(billingFac), submSvc.getSubmSvcSeqId(), 220032);
        Assert.assertTrue(stmtConfig220032StmtData.contains(stmtConfig220032));

        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait);
        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm Svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submSvc.submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");

        boolean IsStatementEngineProcessedFile = waitForStatementEngine(rpmDao.getSubmFile(testDb, submFileSeqId), QUEUE_WAIT_TIME);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).size(), 0);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Make sure 5010 file is saved to the dir");
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + processedSubmFile.getFilename());
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data = bufferedReader.readLine();

        logger.info("Make sure File contains 1 accn in the file");
        Assert.assertTrue(data.contains("CLM*" + accnId));
        Pattern patternAccn = Pattern.compile("CLM\\*" + accnId);
        Matcher matcherAccn = patternAccn.matcher(data);
        int accnIdsCount = 0;
        while (matcherAccn.find())
        {
            accnIdsCount++;
        }
        Assert.assertEquals(accnIdsCount, 1);

        logger.info("Make sure New 5010 Institutional Statement File contains submitter override Statement Config Override data in GS*HC");
        Assert.assertTrue(data.contains("GS*HC*" + stmtConf201024));
        int countOfGS = 0;
        Pattern patternGS = Pattern.compile("GS\\*HC\\*" + stmtConf201024);
        Matcher matcherGS = patternGS.matcher(data);
        while (matcherGS.find())
        {
            countOfGS++;
        }
        Assert.assertEquals(countOfGS, 1);

        logger.info("Make sure New 5010 Institutional Statement File contains submitter override from Statement Config 220032");
        Assert.assertTrue(data.contains("NM1*41*2*XIFIN, INC.*****46*" + stmtConfig220032));
        int countOfNM1 = 0;
        Pattern patternNM1 = Pattern.compile("NM1\\*41\\*2\\*XIFIN\\, INC\\.\\*\\*\\*\\*\\*46\\*" + stmtConfig220032);
        Matcher matcherNM1 = patternNM1.matcher(data);
        while (matcherNM1.find())
        {
            countOfNM1++;
        }
        Assert.assertEquals(countOfNM1, 1);

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "5010 Professional / statement_config is off")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "billingFac"})
    public void testPFER_634(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String billingFac) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-634");
        cleanUpAccn(accnId);

        logger.info("Make sure Statement Config 201024 submitter override is NOT setup for Billing Facility and Submission Service");
        SubmSvc submSvc = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        String stmtConfig201024StmtData = rpmDao.getStatementConfigStmtDataByFacIdAndSubmSvc(testDb, Integer.parseInt(billingFac), submSvc.getSubmSvcSeqId(), 201024);
        Assert.assertNull(stmtConfig201024StmtData);
        String stmtConfig220032StmtData = rpmDao.getStatementConfigStmtDataByFacIdAndSubmSvc(testDb, Integer.parseInt(billingFac), submSvc.getSubmSvcSeqId(), 220032);
        Assert.assertNull(stmtConfig220032StmtData);

        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait);
        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm Svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submSvc.submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).size(), 0);

        logger.info("Make sure 5010 file is saved to the dir");
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + processedSubmFile.getFilename());
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data = bufferedReader.readLine();

        logger.info("Make sure File contains 1 accn in the file");
        Assert.assertTrue(data.contains("CLM*" + accnId));
        Pattern patternAccn = Pattern.compile("CLM\\*" + accnId);
        Matcher matcherAccn = patternAccn.matcher(data);
        int accnIdsCount = 0;
        while (matcherAccn.find())
        {
            accnIdsCount++;
        }
        Assert.assertEquals(accnIdsCount, 1);

        logger.info("Make sure New 5010 Institutional Statement File contains submitter from SUBM_SVC.GRP_SUBMTR_ID in GS*HC");
        Assert.assertTrue(data.contains("GS*HC*" + submSvc.getGrpSubmtrId()));
        int countOfGS = 0;
        Pattern patternGS = Pattern.compile("GS\\*HC\\*" + submSvc.getGrpSubmtrId());
        Matcher matcherGS = patternGS.matcher(data);
        while (matcherGS.find())
        {
            countOfGS++;
        }
        Assert.assertEquals(countOfGS, 1);

        logger.info("Make sure New 5010 Institutional Statement File contains submitter from SUBM_SVC.TXN_HDR_SUBMTR_ID in 1000ANM109");
        Assert.assertTrue(data.contains("NM1*41*2*XIFIN, INC.*****46*" + submSvc.getTxnHdrSubmtrId()));
        int countOfNM1 = 0;
        Pattern patternNM1 = Pattern.compile("NM1\\*41\\*2\\*XIFIN\\, INC\\.\\*\\*\\*\\*\\*46\\*" + submSvc.getTxnHdrSubmtrId());
        Matcher matcherNM1 = patternNM1.matcher(data);
        while (matcherNM1.find())
        {
            countOfNM1++;
        }
        Assert.assertEquals(countOfNM1, 1);

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "print taxonomy code when it's available for Ordering Phys")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "taxonomyCd"})
    public void testPFER_663(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String taxonomyCd) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPPFER-663");

        rpmDao.deleteUnprocessedQAccnSubmByAccnId(testDb, accnId);
        cleanUpAccn(accnId);

        logger.info("Make sure accn Rendering Physician has Taxonomy Code");
        List<AccnPhys> accnPhys = rpmDao.getAccnPhysByAccnIdAndTyp(testDb, accnId, MiscMap.ACCN_PHYS_TYP_ORDERING);
        Assert.assertEquals(accnPhys.size(), 1);
        Phys phys = rpmDao.getPhys(testDb, accnPhys.get(0).getPhysSeqId());
        Assert.assertEquals(phys.getTaxonomyCd(), taxonomyCd);

        logger.info("Enter Claim Info into Submit Claim popup");
        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 more QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertTrue(qasList.size() == 1, "One QAS record should be created");
        QAccnSubm unprocessedQas = qasList.get(0);
        Assert.assertEquals(unprocessedQas.getClaimTypId(), MiscMap.CLAIM_TYP_NEW_141);

        logger.info("Update Last Date for Subm Svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(unprocessedQas, QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);

        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed());

        logger.info("Make sure 5010 file is saved to the dir");
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + processedSubmFile.getFilename());
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data = bufferedReader.readLine();
        logger.info("Make sure in the 5010 Institutional Statement Physician Taxonomy Codes is present");
        Assert.assertTrue(data.contains("PRV*AT*PXC*" + taxonomyCd));
        Assert.assertTrue(data.contains(accnId));

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "5010 Institutional statement - Payments &Adjustments balanced to bill price of the TAX proc - 5010 Stat secondary pyr")
    @Parameters({"accnId", "primPyrAbbrv", "secondPyrAbbrv", "subId", "submSvcAbbrev", "expected5010File"})
    public void testPFER_664(String accnId, String primPyrAbbrv, String secondPyrAbbrv, String subId, String submSvcAbbrev, String expected5010File) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-664");
        SubmSvc submSvc = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        Pyr secondPyr = rpmDao.getPyrByPyrAbbrv(testDb, secondPyrAbbrv);
        cleanQASForPyrOnAccn(accnId, secondPyr.getPyrId());
        List<AccnPyrErr> originalAccnPyrErr = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        List<AccnProcErr> originalAccnProcErr = accessionDao.getAccnProcErrsByAccnId(accnId);

        File expected5010 = new File(getClass().getResource("/mars/testFiles/" + expected5010File).getFile());
        String expected5010Data = Statement837Utils.clearSegments(com.mchange.io.FileUtils.getContentsAsString(expected5010));
        Pyr primaryPyr = rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv);
        logger.info("Make sure there is 1 QAS record for the primary pyr on the accn, pyrAbbrev=" + primPyrAbbrv);
        QAccnSubm qasPrimaryPyr = rpmDao.getQAccnSubmByAccnIdAndPyrIdAndSubmSvcId(testDb, accnId, primaryPyr.getPyrId(), submSvc.getSubmSvcSeqId());
        Assert.assertNotEquals(qasPrimaryPyr.getSubmFileSeqId(), 0);

        logger.info("Make sure there payments from the primary pyr on the accn, pyrAbbrev=" + primPyrAbbrv);
        List<AccnPmt> accnPmts = rpmDao.getAccnPmtsByAccnId(testDb, accnId);
        Assert.assertTrue(accnPmts.size() > 0);
        for (AccnPmt accnPmt : accnPmts)
        {
            Assert.assertEquals(accnPmt.getPyrId(), primaryPyr.getPyrId());
        }

        logger.info("Submit the claim for the secondary pyr, pyrId=" + secondPyr.getPyrId());
        accessionDetail.submitClaimsOnAccnDetail(accnId, secondPyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        QAccnSubm newQAS = rpmDao.getQAccnSubmByAccnIdAndPyrIdAndSubmSvcId(testDb, accnId, rpmDao.getPyrByPyrAbbrv(testDb, secondPyrAbbrv).getPyrId(), submSvc.getSubmSvcSeqId());
        Assert.assertNotNull(newQAS);

        logger.info("Update Last Date for Subm Svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submSvc.getSubmSvcSeqId());

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(newQAS, QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to process the file, fileSeqId=" + newQAS.getDocSeqId());
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed());

        logger.info("Make sure No New Errors added to the accn");
        List<AccnPyrErr> newAccnPyrErr = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        Assert.assertEquals(newAccnPyrErr.size(), originalAccnPyrErr.size());

        logger.info("Make sure No New Errors added to the accn");
        List<AccnProcErr> newAccnProcErr = accessionDao.getAccnProcErrsByAccnId(accnId);
        Assert.assertEquals(newAccnProcErr.size(), originalAccnProcErr.size());

        logger.info("Make sure subm_claim_audit record is created for 5010 statement");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(testDb, newQAS.getDocSeqId());
        Assert.assertNotNull(submClaimAudit);

        logger.info("Make sure 5010 file is saved to the dir");
        File actualFile = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(actualFile.exists());

        logger.info("Make sure Actual File contains the Expected file");
        String actual5010Data = Statement837Utils.clearSegments(com.mchange.io.FileUtils.getContentsAsString(actualFile));
        Assert.assertTrue(actual5010Data.contains(expected5010Data), "Data difference=" + StringUtils.difference(actual5010Data, expected5010Data));

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, newQAS.getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "1 of multiple procs on accession has Pyr override - 2 files created: 837 and CMS1500")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "expected5010File"})
    public void testPFER_735(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String expected5010File) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-735");
        int pyrId = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId();
        cleanQASForPyrOnAccn(accnId, pyrId);
        File expected5010 = new File(getClass().getResource("/mars/testFiles/" + expected5010File).getFile());
        String expected5010Data = Statement837Utils.clearSegments(com.mchange.io.FileUtils.getContentsAsString(expected5010));

        logger.info("Make sure there is a pyr override for Pyr Id and 1 of the procs");
        PyrSubmSvcProcOvrrdes pyrSubmSvcProcOvrrdes = rpmDao.getPyrSubmSvcProcOvrrdes(testDb, pyrId);
        List<AccnProc> accnProcs = rpmDao.getAccnProcsByAccnId(testDb, accnId);
        Assert.assertEquals(accnProcs.size(), 2);
        boolean isProcInOverride;
        for (AccnProc accnProc : accnProcs)
        {
            isProcInOverride = pyrSubmSvcProcOvrrdes.getProcId().equals(accnProc.getProcId());
            if (isProcInOverride)
            {
                break;
            }
        }

        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait);
        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);
        SubmSvc submittedSubmSvc = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        logger.info("Update Last Date for Subm Svc to be null, submSvcAbbrev=" + submittedSubmSvc.getAbbrev());
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submittedSubmSvc.submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).size(), 0);

        logger.info("Make sure 5010 file is saved to the dir");
        File actualFile = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(actualFile.exists());

        logger.info("Make sure Actual File contains the Expected file");
        String actual5010Data = Statement837Utils.clearSegments(com.mchange.io.FileUtils.getContentsAsString(actualFile));
        Assert.assertTrue(actual5010Data.contains(expected5010Data), "Data difference=" + StringUtils.difference(actual5010Data, expected5010Data));

        logger.info("Make sure 1 more QAS is created");
        QAccnSubm qasForOverride = rpmDao.getUnprocessedQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasForOverride.getSubmSvcSeqId(), pyrSubmSvcProcOvrrdes.getSubmSvcSeqId());

        logger.info("Update Last Date for Subm Svc to be null,  pyrSubmSvcProcOvrrdes.getSubmSvcSeqId()=" + pyrSubmSvcProcOvrrdes.getSubmSvcSeqId());
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, pyrSubmSvcProcOvrrdes.getSubmSvcSeqId());

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileForOverrideSeqId = waitForSubmissionEngine(qasForOverride, QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileForOverrideSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");

        SubmFile submFileForOverride = rpmDao.getSubmFile(testDb, submFileForOverrideSeqId);
        boolean IsStatementEngineProcessedFileForOverride = waitForStatementEngine(submFileForOverride, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFileForOverride);

        SubmFile processedSubmFileForOverride = rpmDao.getSubmFile(testDb, submFileForOverrideSeqId);
        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).size(), 0);

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "Proc Level MolDX Z-codes match accn procs - included on 5010")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "expected5010File"})
    public void testPFER_736(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String expected5010File) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-736");
        int pyrId = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId();
        cleanQASForPyrOnAccn(accnId, pyrId);
        File expected5010 = new File(getClass().getResource("/mars/testFiles/" + expected5010File).getFile());
        String expected5010Data = Statement837Utils.clearSegments(com.mchange.io.FileUtils.getContentsAsString(expected5010));
        logger.info("Expected file " + expected5010Data);
        logger.info("Make sure there are multiple XREF records with typ 510 - MolDXProcZcode");
        List<Xref> xRefs = rpmDao.getXrefByXrefType(testDb, MiscMap.XREF_TYP_MOL_DX_PROC_Z_CODE);
        Assert.assertTrue(xRefs.size() > 1);

        logger.info("Make sure Accn Test has test_xref and it is Xref type 510");
        List<AccnTest> accnTests = accessionDao.getAccnTestsByAccnId(accnId);
        Assert.assertEquals(accnTests.size(), 1);

        List<TestXref> testXref = rpmDao.getTestXrefByTestId(testDb, accnTests.get(0).getTestId());
        Assert.assertTrue(testXref.size() > 1);
        boolean isTestXrefType510;
        for (Xref xRef : xRefs)
        {
            logger.info("Checking if Accession has test that is in TEST_XREF with XREF Typ 510, xRef=" + xRef);
            TestXref testXrefType510 = rpmDao.getTestXrefByTestXrefAndTestId(testDb, accnTests.get(0).getTestId(), xRef.getXrefId());
            isTestXrefType510 = testXrefType510 != null;
            if (isTestXrefType510)
            {
                break;
            }
        }
        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait);
        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);
        SubmSvc submittedSubmSvc = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        logger.info("Update Last Date for Subm Svc to be null, submSvcAbbrev=" + submittedSubmSvc.getAbbrev());
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submittedSubmSvc.submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).size(), 0);

        logger.info("Make sure 5010 file is saved to the dir");
        File actualFile = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(actualFile.exists());

        logger.info("Make sure Actual File contains the Expected file");
        String actual5010Data = Statement837Utils.clearSegments(com.mchange.io.FileUtils.getContentsAsString(actualFile));
        logger.info("actual5010Data = " + actual5010Data);

        Assert.assertTrue(actual5010Data.contains(expected5010Data), "Data difference=" + StringUtils.difference(actual5010Data, expected5010Data));

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "Suppress loop2310A in Ver5010A1837Stmt (SUPPRESSPHYS Ordering PHYS_XREF)")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "expected5010Data"})
    public void testRpmNew_992893(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String expected5010Data) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testRpmNew-992893");
        int pyrId = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId();
        cleanQASForPyrOnAccn(accnId, pyrId);

        logger.info("Make sure there are XREF records with typ SUPPRESSPHYS");
        XrefTyp xrefTyp = rpmDao.getXrefTypByAbbrev(testDb, "SUPPRESSPHYS");
        List<Xref> xRefs = rpmDao.getXrefByXrefType(testDb, xrefTyp.getXrefTypId());
        Assert.assertEquals(1, xRefs.size(), "Expected only on XREF of SUPPRESSPHYS type");

        logger.info("Make sure Accn Phys has phys_xref");
        List<AccnPhys> accnPhys = accessionDao.getAccnPhysByAccnId(accnId);
        Assert.assertEquals(accnPhys.size(), 1);

        List<PhysXref> physXrefs = physicianDao.getPhysXrefByPhysId(accnPhys.get(0).getPhysSeqId());
        Assert.assertTrue(physXrefs.size() >= 1, "PhysXref not found for physSeqId=" + accnPhys.get(0).getPhysSeqId());

        boolean isPhysXrefCorrect = false;
        for (PhysXref physXref : physXrefs)
        {
            if (physXref.getXrefId() == xRefs.get(0).getXrefId())
            {
                isPhysXrefCorrect = true;
            }
        }
        Assert.assertTrue(isPhysXrefCorrect, "PhysXref of SUPPRESSPHYS was expected");

        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait);
        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);
        SubmSvc submittedSubmSvc = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        logger.info("Update Last Date for Subm Svc to be null, submSvcAbbrev=" + submittedSubmSvc.getAbbrev());
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submittedSubmSvc.submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).size(), 0);

        logger.info("Make sure 5010 file is saved to the dir");
        File actualFile = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(actualFile.exists());

        logger.info("Make sure Actual File does not contain suppressed physician");
        String actual5010Data = Statement837Utils.clearSegments(com.mchange.io.FileUtils.getContentsAsString(actualFile));
        logger.info("actual5010Data = " + actual5010Data);

        Assert.assertFalse(actual5010Data.contains(expected5010Data), "Data difference=" + StringUtils.difference(actual5010Data, expected5010Data));

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "Test DOS Range on Professional 837")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "expected5010File"})
    public void testDosRange(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String expected5010File) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testDosRange");
        int pyrId = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId();
        cleanQASForPyrOnAccn(accnId, pyrId);
        File expected5010 = new File(getClass().getResource("/mars/testFiles/" + expected5010File).getFile());
        String expected5010Data = Statement837Utils.clearSegments(com.mchange.io.FileUtils.getContentsAsString(expected5010));
        logger.info("Expected file " + expected5010Data);

        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait);
        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);
        SubmSvc submittedSubmSvc = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        logger.info("Update Last Date for Subm Svc to be null, submSvcAbbrev=" + submittedSubmSvc.getAbbrev());
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submittedSubmSvc.submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).size(), 0);

        logger.info("Make sure 5010 file is saved to the dir");
        File actualFile = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(actualFile.exists());

        logger.info("Make sure Actual File contains the Expected file");
        String actual5010Data = Statement837Utils.clearSegments(com.mchange.io.FileUtils.getContentsAsString(actualFile));
        logger.info("actual5010Data = " + actual5010Data);

        Assert.assertTrue(actual5010Data.contains(expected5010Data), "Data difference=" + StringUtils.difference(actual5010Data, expected5010Data));

        logger.info("Make sure Subm File Audit records do not have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "ENG-104853 5010 Professional - With Payor Address Override Applied")
    @Parameters({"accnId", "pyrAbbrv", "subId", "submSvcAbbrev", "overrideAddress"})
    public void testPayorAddressOverride(String accnId, String pyrAbbrv, String subId, String submSvcAbbrev, String overrideAddress) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running ENG-104853 5010 Professional - With Payor Address Override Applied");
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        int pyrId = pyr.getPyrId();
        cleanQASForPyrOnAccn(accnId, pyrId);

        String[] overrideAddressParts = StringUtils.split(overrideAddress, "|");
        Assert.assertEquals(overrideAddressParts.length, 7, "Expected override address is not complete, accnId="+accnId);
        Assert.assertNotEquals(StringUtils.upperCase(pyr.getCntctAddr1()), StringUtils.upperCase(overrideAddressParts[2]), "Expected override address line 1 to be different from payor's contact address line 1, accnId="+accnId);
        Assert.assertNotEquals(StringUtils.upperCase(pyr.getCntctAddr2()), StringUtils.upperCase(overrideAddressParts[3]), "Expected override address line 2 to be different from payor's contact address line 2, accnId="+accnId);
        Assert.assertNotEquals(StringUtils.upperCase(pyr.getCntctCity()), StringUtils.upperCase(overrideAddressParts[4]), "Expected override address city to be different from payor's contact address city, accnId="+accnId);
        Assert.assertNotEquals(StringUtils.upperCase(pyr.getCntctStId()), StringUtils.upperCase(overrideAddressParts[5]), "Expected override address state to be different from payor's contact address state, accnId="+accnId);
        Assert.assertNotEquals(StringUtils.removePattern(pyr.getCntctZipId(), "[^0-9]"), StringUtils.removePattern(overrideAddressParts[6], "[^0-9]"), "Expected override address zip to be different from payor's contact address zip, accnId="+accnId);

        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait);
        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);
        SubmSvc submittedSubmSvc = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        logger.info("Update Last Date for Subm Svc to be null, submSvcAbbrev=" + submittedSubmSvc.getAbbrev());
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submittedSubmSvc.submSvcSeqId);

        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).size(), 0);

        logger.info("Make sure 5010 file is saved to the dir");
        File actualFile = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(actualFile.exists());
        String actual5010Data = FileUtils.getContentsAsString(actualFile);
        String overrideMatch = "NM1*PR*2*"+StringUtils.upperCase(overrideAddressParts[0])+"*****PI*"+StringUtils.upperCase(overrideAddressParts[1])+"~"
                + "N3*"+StringUtils.upperCase(overrideAddressParts[2])+"*"+StringUtils.upperCase(overrideAddressParts[3])+"~"
                + "N4*"+StringUtils.upperCase(overrideAddressParts[4])+"*"+StringUtils.upperCase(overrideAddressParts[5])+"*"+ StringUtils.removePattern(overrideAddressParts[6], "[^0-9]")+"~";
        logger.info("Make sure 5010 content contains the override address, overrideMatch="+overrideMatch+", fileContent="+actual5010Data);
        Assert.assertTrue(actual5010Data.contains(overrideMatch));
    }
    @Test(priority = 1, description = "Duplicate submissions are not triggered when LCNOABN error is resolved")
    @Parameters({"accnId", "pyrAbbrv", "lcNoAbnProc", "submittedProc"})
    public void testNoDuplicateWhenLcnoabnIsResolved(String accnId, String pyrAbbrv, String lcNoAbnProc, String submittedProc) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("message=Starting test case, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", lcNoAbnProc="+lcNoAbnProc+", submittedProc="+submittedProc);
        Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        logger.info("message=Verifying submitted proc, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", lcNoAbnProc="+lcNoAbnProc+", submittedProc="+submittedProc);
        List<AccnProc> submittedProcList = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, submittedProc);
        Assert.assertEquals(submittedProcList.size(), 1, "Expected 1 submitted proc, accnId="+accnId+", submittedProc="+submittedProc+", submittedProcListSize="+submittedProcList.size());
        Assert.assertTrue(submittedProcList.get(0).getSubmFileSeqId() > 0, "Expected submission file on proc to be > 0, accnId="+accnId+", submittedProc="+submittedProc+", submFileSeqId="+submittedProcList.get(0).getSubmFileSeqId());
        SubmFile origSubmFile = submissionDao.getSubmFileBySubmFileSeqId(submittedProcList.get(0).getSubmFileSeqId());
        Assert.assertTrue(origSubmFile.getIsEgateProcessed(), "Expected submission file on submitted proc to be processed, accnId="+accnId+", submittedProc="+submittedProc+", submFileSeqId="+submittedProcList.get(0).getSubmFileSeqId()+", isEgateProcessed="+origSubmFile.getIsEgateProcessed());
        List<String> qAccnSubmsToKeep = new ArrayList<>();
        for (QAccnSubm qAccnSubm : submissionDao.getQAccnSubmsBySubmFileSeqId(origSubmFile.getSubmFileSeqId()))
        {
            if (StringUtils.equals(qAccnSubm.getAccnId(), accnId))
            {
                qAccnSubmsToKeep.add(String.valueOf(qAccnSubm.getDocSeqId()));
            }
        }
        logger.info("message=Cleaning QAS, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", lcNoAbnProc="+lcNoAbnProc+", submittedProc="+submittedProc+", qasListToKeep="+qAccnSubmsToKeep);
        cleanQASForPyrOnAccnExceptDocSeqIds(accnId, pyr.getPyrId(), qAccnSubmsToKeep);
        logger.info("message=Verifying LCNOABN proc, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", lcNoAbnProc="+lcNoAbnProc+", submittedProc="+submittedProc);
        List<AccnProc> lcNoAbnProcList = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, lcNoAbnProc);
        Assert.assertEquals(lcNoAbnProcList.size(), 1, "Expected 1 LCNOABN proc, accnId="+accnId+", lcNoAbnProc="+lcNoAbnProc+", lcNoAbnProcListSize="+lcNoAbnProcList.size());
        AccnTest lcNoAbnAccnTest = accessionDao.getAccnTestsByAccnIdAndAccnTestSeqId(accnId, lcNoAbnProcList.get(0).getAccnTestSeqId());
        Assert.assertNotNull(lcNoAbnAccnTest, "Expected LCNOAB accn test not found, accnId="+accnId+", accnProcSeqId="+lcNoAbnProcList.get(0).getAccnProcSeqId()+", accnTestSeqId="+lcNoAbnProcList.get(0).getAccnTestSeqId());
        Assert.assertTrue(lcNoAbnAccnTest.getIsAbnReq(), "Expected ABN required flag to be true, accnId="+accnId+", accnTestSeqId="+lcNoAbnAccnTest.getAccnTestSeqId()+", isAbnRequired="+lcNoAbnAccnTest.getIsAbnReq());
        if (lcNoAbnAccnTest.getIsAbnRec())
        {
            logger.info("message=Loading accession on Accession Detail, accnId="+accnId);
            accessionDetail.loadAccnOnAccnDetail(wait, accnId);
            logger.info("message=Clearing ABN received flag, accnId="+accnId+", accnProcSeqId="+lcNoAbnProcList.get(0).getAccnProcSeqId()+", accnTestSeqId="+lcNoAbnProcList.get(0).getAccnTestSeqId());
            accessionDetail.clickOrderedTestAbnReceivedCheckbox(lcNoAbnProcList.get(0).getAccnTestSeqId());
            accessionDetail.clickSavdAndClearBtn();
            accessionDetail.saveAndClear(wait);
            lcNoAbnProcList = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, lcNoAbnProc);
            Assert.assertEquals(lcNoAbnProcList.size(), 1, "Expected 1 LCNOABN proc, accnId="+accnId+", lcNoAbnProc="+lcNoAbnProc+", lcNoAbnProcListSize="+lcNoAbnProcList.size());
            lcNoAbnAccnTest = accessionDao.getAccnTestsByAccnIdAndAccnTestSeqId(accnId, lcNoAbnProcList.get(0).getAccnTestSeqId());
        }
        Assert.assertEquals(lcNoAbnProcList.get(0).getStaId(), AccnStatusMap.ACCN_STATUS_PROC_EP_GENERAL, "Expected EP status on proc, accnId="+accnId+", accnProcSeqId="+lcNoAbnProcList.get(0).getAccnProcSeqId()+", staId="+lcNoAbnProcList.get(0).getStaId());
        Assert.assertEquals(lcNoAbnProcList.get(0).getSubmFileSeqId(), 0, "Expected submission file on EP proc to be = 0, accnId="+accnId+", accnProcSeqId="+lcNoAbnProcList.get(0).getAccnProcSeqId()+", staId="+lcNoAbnProcList.get(0).getSubmFileSeqId());
        Assert.assertFalse(lcNoAbnAccnTest.getIsAbnRec(), "Expected ABN received flag to be false, accnId="+accnId+", accnTestSeqId="+lcNoAbnAccnTest.getAccnTestSeqId()+", isAbnReceived="+lcNoAbnAccnTest.getIsAbnRec());
        logger.info("message=Loading accession on Accession Detail, accnId="+accnId);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        logger.info("message=Setting ABN received flag, accnId="+accnId+", accnProcSeqId="+lcNoAbnProcList.get(0).getAccnProcSeqId()+", accnTestSeqId="+lcNoAbnProcList.get(0).getAccnTestSeqId());
        accessionDetail.clickOrderedTestAbnReceivedCheckbox(lcNoAbnProcList.get(0).getAccnTestSeqId());
        accessionDetail.saveAndClear(wait);
        lcNoAbnAccnTest = accessionDao.getAccnTestsByAccnIdAndAccnTestSeqId(accnId, lcNoAbnProcList.get(0).getAccnTestSeqId());
        Assert.assertTrue(lcNoAbnAccnTest.getIsAbnReq(), "Expected ABN received flag to be true, accnId="+accnId+", accnTestSeqId="+lcNoAbnAccnTest.getAccnTestSeqId()+", isAbnReceived="+lcNoAbnAccnTest.getIsAbnRec());
        lcNoAbnProcList = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, lcNoAbnProc);
        Assert.assertEquals(lcNoAbnProcList.size(), 1, "Expected 1 LCNOABN proc, accnId="+accnId+", lcNoAbnProc="+lcNoAbnProc+", lcNoAbnProcListSize="+lcNoAbnProcList.size());
        Assert.assertEquals(lcNoAbnProcList.get(0).getStaId(), AccnStatusMap.ACCN_STATUS_PRICED, "Expected priced status on proc, accnId="+accnId+", accnProcSeqId="+lcNoAbnProcList.get(0).getAccnProcSeqId()+", staId="+lcNoAbnProcList.get(0).getStaId());
        Assert.assertEquals(lcNoAbnProcList.get(0).getSubmFileSeqId(), 0, "Expected submission file on EP proc to be = 0, accnId="+accnId+", accnProcSeqId="+lcNoAbnProcList.get(0).getAccnProcSeqId()+", submFileSeqId="+lcNoAbnProcList.get(0).getSubmFileSeqId());
        QAccnSubm newQas = rpmDao.getUnprocessedQAccnSubm(null, accnId);
        Assert.assertNotNull(newQas, "Expected new QAS record, accnId="+accnId);
        SubmSvc submSvc = submissionDao.getSubmSvc(newQas.getSubmSvcSeqId());
        logger.info("message=Clearing last submit date, submSvcAbbrev="+submSvc.getAbbrev());
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(null, newQas.getSubmSvcSeqId());
        logger.info("message=Wait for Non-Client Submission Engine to process the claim, accnId="+accnId+", docSeqId="+newQas.getDocSeqId());
        int newSubmFileSeqId = waitForSubmissionEngine(newQas, QUEUE_WAIT_TIME*2, submSvc.getAbbrev());
        Assert.assertTrue(newSubmFileSeqId > 0, "Expected new submission file ID to be > 0, accnId="+accnId+", docSeqId="+newQas.getDocSeqId()+", newSubmFileSeqId="+newSubmFileSeqId);
        logger.info("message=Wait for Non-Client Statement Engine to process the file");
        boolean isProcessedFile = waitForStatementEngine(rpmDao.getSubmFile(testDb, newSubmFileSeqId), QUEUE_WAIT_TIME*2);
        Assert.assertTrue(isProcessedFile, "Expected file to be processed, accnId="+accnId+", docSeqId="+newQas.getDocSeqId()+", submFileSeqId="+newSubmFileSeqId+", isProcessedFile="+isProcessedFile);
        logger.info("message=Verifying new statement only contains ABN proc, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", lcNoAbnProc="+lcNoAbnProc+", docSeqId="+newQas.getDocSeqId());
        List<SubmFileAudit> newSubmFileAudits = rpmDao.getSubmFileAudits(testDb, newQas.getDocSeqId());
        Assert.assertEquals(newSubmFileAudits.size(), 1, "Expected 1 new submitted proc, newSubmFileAuditSize="+newSubmFileAudits.size());
        Assert.assertEquals(newSubmFileAudits.get(0).getProcId(), lcNoAbnProc, "Expected ABN proc code, submittedProcId="+newSubmFileAudits.get(0).getProcId()+", expectedProcId="+lcNoAbnProc);
        logger.info("message=Verifying submission file IDs, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", lcNoAbnProc="+lcNoAbnProc+", submittedProc="+submittedProc);
        submittedProcList = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, submittedProc);
        Assert.assertEquals(submittedProcList.size(), 1, "Expected 1 submitted proc, accnId="+accnId+", submittedProc="+submittedProc+", submittedProcListSize="+submittedProcList.size());
        Assert.assertEquals(submittedProcList.get(0).getSubmFileSeqId(), origSubmFile.getSubmFileSeqId(), "Expected submission file on non-ABN proc to be unchanged, accnId="+accnId+", submittedProc="+submittedProc+", submFileSeqId="+submittedProcList.get(0).getSubmFileSeqId()+", origSubmFileSeqId="+origSubmFile.getSubmFileSeqId());
        lcNoAbnProcList = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, lcNoAbnProc);
        Assert.assertEquals(lcNoAbnProcList.size(), 1, "Expected 1 ABN proc, accnId="+accnId+", lcNoAbnProc="+lcNoAbnProc+", lcNoAbnProcListSize="+lcNoAbnProcList.size());
        Assert.assertEquals(lcNoAbnProcList.get(0).getSubmFileSeqId(), newSubmFileSeqId, "Expected submission file on ABN proc did not match, accnId="+accnId+", lcNoAbnProc="+lcNoAbnProc+", submFileSeqId="+lcNoAbnProcList.get(0).getSubmFileSeqId()+", newSubmFileSeqId="+newSubmFileSeqId);
    }

    @Test(priority = 1, description = "Proc Code is translated when required diagnosis code is present")
    @Parameters({"accnId", "origDocSeqId", "pyrAbbrv", "subId", "submSvcAbbrev", "expected5010File", "origProcId", "translatedProcId"})
    public void testProcCodeTranslation(String accnId, String origDocSeqId, String pyrAbbrv, String subId, String submSvcAbbrev, String expected5010File, String origProcId, String translatedProcId) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testProcCodeTranslation");
        int pyrId = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId();
        cleanQASForAccnExceptDocSeqIds(accnId, List.of(origDocSeqId));
        File expected5010 = new File(getClass().getResource("/mars/testFiles/" + expected5010File).getFile());
        String expected5010Data = Statement837Utils.clearSegments(com.mchange.io.FileUtils.getContentsAsString(expected5010));
        List<AccnProc> accnProcs = rpmDao.getAccnProcsByAccnId(null, accnId);
        Assert.assertEquals(accnProcs.size(), 1, "Expected 1 AccnProc record, accnId="+accnId+", accnProcCnt="+accnProcs.size());
        Assert.assertEquals(accnProcs.get(0).getProcId(), origProcId, "Expected original proc Id to match, accnId="+accnId+", actualProcId="+accnProcs.get(0).getProcId()+", expectedProcId="+origProcId);
        List<QAccnSubm> origQasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(origQasList.size(), 1, "Expected 1 existing QAS record, accnId="+accnId+", qasCnt="+origQasList.size());
        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, subId, submSvcAbbrev, wait, false, true);
        List<QAccnSubm> newQasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(newQasList.size(), 2, "Expected 2 QAS records after submitting claim, accnId="+accnId+", qasCnt="+origQasList.size());
        SubmSvc submittedSubmSvc = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submittedSubmSvc.submSvcSeqId);
        logger.info("Cleared LastSubmDate for SubmSvc, submSvcAbbrev=" + submittedSubmSvc.getAbbrev());
        int submFileSeqId = waitForSubmissionEngine(newQasList.get(1), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0, "Expected SubmFileSeqId > 0, accnId="+accnId+", docSeqId="+newQasList.get(1).getDocSeqId());
        boolean isProcessedFile = waitForStatementEngine(rpmDao.getSubmFile(testDb, submFileSeqId), QUEUE_WAIT_TIME*2);
        Assert.assertTrue(isProcessedFile, "Expected file to be processed, accnId="+accnId+", docSeqId="+newQasList.get(1).getDocSeqId()+", submFileSeqId="+submFileSeqId);
        List<SubmFileAudit> newSubmFileAudits = rpmDao.getSubmFileAudits(testDb, newQasList.get(1).getDocSeqId());
        Assert.assertEquals(newSubmFileAudits.size(), 1, "Expected 1 new submitted proc, accnId="+accnId+", docSeqId="+newQasList.get(1).getDocSeqId()+", newSubmFileAuditSize="+newSubmFileAudits.size());
        Assert.assertEquals(newSubmFileAudits.get(0).getProcId(), translatedProcId, "Expected translated proc Id in SubmFileAudit, submittedProcId="+newSubmFileAudits.get(0).getProcId()+", expectedProcId="+translatedProcId);
        logger.info("message=Verifying new statement contains translated proc, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", origProcId="+origProcId+", translatedProcId="+translatedProcId+", docSeqId="+newQasList.get(1).getDocSeqId());
        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).size(), 0, "Expected no new errors added to the accession, accnId="+accnId);
        File actualFile = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(actualFile.exists(), "Expected statement file to exist, accnId="+accnId+", file="+actualFile.getAbsolutePath());
        logger.info("Verifying statement content, accnId="+accnId+", file="+actualFile.getAbsolutePath());
        String actual5010Data = Statement837Utils.clearSegments(com.mchange.io.FileUtils.getContentsAsString(actualFile));
        Assert.assertTrue(actual5010Data.contains(expected5010Data), "Data difference=" + StringUtils.difference(actual5010Data, expected5010Data));
    }

    @Test(priority = 1, description = "Test Super Search Force to Resubmit With Specific Payor")
    @Parameters({"accnId", "pyrAbbrv", "procForPayor", "procNotForPayor"})
    public void testSuperSearchForceToResubmitWithSpecificPayor(String accnId, String pyrAbbrv, String procForPayor, String procNotForPayor) throws Exception
    {
        logger.info("message=Starting test case, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", procForPayor="+procForPayor+", procNotForPayor="+procNotForPayor);
        logger.info("message=Verifying proc for payor, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", procForPayor="+procForPayor);
        List<AccnProc> procForPayorList = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, procForPayor);
        Assert.assertEquals(procForPayorList.size(), 1, "Expected 1 proc for payor, accnId="+accnId+", procForPayor="+procForPayor+", procForPayorListSize="+procForPayorList.size());
        Assert.assertTrue(procForPayorList.get(0).getSubmFileSeqId() > 0, "Expected submission file on proc for payor to be > 0, accnId="+accnId+", procForPayor="+procForPayor+", submFileSeqId="+procForPayorList.get(0).getSubmFileSeqId());
        SubmFile origSubmFile = submissionDao.getSubmFileBySubmFileSeqId(procForPayorList.get(0).getSubmFileSeqId());
        Assert.assertTrue(origSubmFile.getIsEgateProcessed(), "Expected submission file on proc for payor to be processed, accnId="+accnId+", procForPayor="+procForPayor+", submFileSeqId="+procForPayorList.get(0).getSubmFileSeqId()+", isEgateProcessed="+origSubmFile.getIsEgateProcessed());
        List<AccnProc> procsForOtherPayorList = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, procNotForPayor);
        Assert.assertEquals(procsForOtherPayorList.size(), 1, "Expected 1 proc not for payor, accnId="+accnId+", procNotForPayor="+procNotForPayor+", procsForOtherPayorListSize="+procsForOtherPayorList.size());
        Assert.assertTrue(procsForOtherPayorList.get(0).getSubmFileSeqId() > 0, "Expected submission file on proc not for payor > 0, accnId="+accnId+", procNotForPayor="+procNotForPayor+", submFileSeqId="+procsForOtherPayorList.get(0).getSubmFileSeqId());
        Assert.assertNotEquals(procsForOtherPayorList.get(0).getSubmFileSeqId(), procForPayorList.get(0).getSubmFileSeqId(), "Expected submission file on proc not for payor != submission file on proc for payor, accnId="+accnId+", procForPayorSubmFileSeqId="+procForPayorList.get(0).getSubmFileSeqId()+", procNotForPayorSubmFileSeqId="+procsForOtherPayorList.get(0).getSubmFileSeqId());
        Assert.assertNotEquals(procsForOtherPayorList.get(0).getSubmPyrPrio(), procForPayorList.get(0).getSubmPyrPrio(), "Expected submission payor prio on proc not for payor != submission payor prio on proc for payor, accnId="+accnId+", procForPayorSubmFileSeqId="+procForPayorList.get(0).getSubmFileSeqId()+", procNotForPayorSubmFileSeqId="+procsForOtherPayorList.get(0).getSubmFileSeqId());

        List<String> qAccnSubmsToKeep = new ArrayList<>();
        for (QAccnSubm qAccnSubm : submissionDao.getQAccnSubmsBySubmFileSeqId(origSubmFile.getSubmFileSeqId()))
        {
            if (StringUtils.equals(qAccnSubm.getAccnId(), accnId))
            {
                qAccnSubmsToKeep.add(String.valueOf(qAccnSubm.getDocSeqId()));
            }
        }
        SubmFile origSubmFileNotForPayor = submissionDao.getSubmFileBySubmFileSeqId(procsForOtherPayorList.get(0).getSubmFileSeqId());
        Assert.assertTrue(origSubmFileNotForPayor.getIsEgateProcessed(), "Expected submission file on proc not for payor to be processed, accnId="+accnId+", procNotForPayor="+procNotForPayor+", submFileSeqId="+procsForOtherPayorList.get(0).getSubmFileSeqId()+", isEgateProcessed="+origSubmFileNotForPayor.getIsEgateProcessed());
        for (QAccnSubm qAccnSubm : submissionDao.getQAccnSubmsBySubmFileSeqId(origSubmFileNotForPayor.getSubmFileSeqId()))
        {
            if (StringUtils.equals(qAccnSubm.getAccnId(), accnId))
            {
                qAccnSubmsToKeep.add(String.valueOf(qAccnSubm.getDocSeqId()));
            }
        }
        logger.info("message=Cleaning QAS, accnId="+accnId+", pyrAbbrv="+pyrAbbrv+", qasListToKeep="+qAccnSubmsToKeep);
        cleanQASForAccnExceptDocSeqIds(accnId, qAccnSubmsToKeep);
        List<QAccnSubm> currentQasList = submissionDao.getQAccnSubmsByAccnId(accnId);
        Assert.assertEquals(currentQasList.size(), 2, "Expected 2 QAS record, accnId="+accnId+", qasCount="+currentQasList.size());
        // Need to reset QAS claim ID suffixes so  we don't keep incrementing
        logger.info("message=Resetting QAS claim ID suffix, accnId="+accnId+", docSeqId="+currentQasList.get(0)+", currClaimIdSuffix="+currentQasList.get(0).getClaimIdSuffix()+", newClaimIdSuffix=01");
        currentQasList.get(0).setClaimIdSuffix("01");
        databaseSequenceDao.setValueObject(currentQasList.get(0));
        logger.info("message=Resetting QAS claim ID suffix, accnId="+accnId+", docSeqId="+currentQasList.get(1)+", currClaimIdSuffix="+currentQasList.get(1).getClaimIdSuffix()+", newClaimIdSuffix=02");
        currentQasList.get(1).setClaimIdSuffix("02");
        databaseSequenceDao.setValueObject(currentQasList.get(1));

        SuperSearch superSearch = new SuperSearch(driver);
        MenuNavigation menuNavigation = new MenuNavigation(driver, config);
        menuNavigation.navigateToSuperSearchPage();
        superSearch.performForceToResubmitActionOnSuperSearch(accnId, pyrAbbrv, wait);

        List<QAccnSubm> newQasList = submissionDao.getQAccnSubmsByAccnId(accnId);
        Assert.assertEquals(newQasList.size(), 3, "Expected 3 QAS records after Super Search resubmit, accnId="+accnId+", qasCount="+currentQasList.size());
        QAccnSubm newQas = newQasList.get(newQasList.size()-1);
        Assert.assertNotNull(newQas.getNextSubmDt(), "Expected next submit date on new QAS to be not null, accnId="+accnId+", docSeqId="+newQas.getDocSeqId());
        Assert.assertEquals(newQas.getSubmFileSeqId(), 0, "Expected subm file seq Id on new QAS to be 0, accnId="+accnId+", docSeqId=="+newQas.getDocSeqId()+", submFileSeqId="+newQas.getSubmFileSeqId());

        List<AccnProc> newProcForPayorList = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, procForPayor);
        Assert.assertEquals(newProcForPayorList.size(), 1, "Expected 1 AccnProc record, accnId="+accnId+", procForPayor="+procForPayor+", accnProcCount="+newProcForPayorList.size());
        Assert.assertEquals(newProcForPayorList.get(0).getSubmFileSeqId(), 0, "Expected AccnProc submFileSeqId to be zero on proc for payor accnId="+accnId+", accnProcSubmFileSeqId="+newProcForPayorList.get(0).getSubmFileSeqId());
        Assert.assertEquals(newProcForPayorList.get(0).getSubmSvcOvrrdeSta(), 0, "Expected AccnProc submSvcOvrrdeSta to be zero on proc for payor, accnId="+accnId+", accnProcSubmSvcOvrrdeSta="+newProcForPayorList.get(0).getSubmSvcOvrrdeSta());

        List<AccnProc> newProcNotForPayorList = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, procNotForPayor);
        Assert.assertEquals(newProcNotForPayorList.size(), 1, "Expected 1 AccnProc record, accnId="+accnId+", procNotForPayor="+procNotForPayor+", accnProcCount="+newProcNotForPayorList.size());
        Assert.assertEquals(newProcNotForPayorList.get(0).getSubmFileSeqId(), procsForOtherPayorList.get(0).getSubmFileSeqId(), "Expected AccnProc submFileSeqId to be unchanged on proc not for payor, accnId="+accnId+", accnProcSubmFileSeqId="+newProcNotForPayorList.get(0).getSubmFileSeqId());
        Assert.assertEquals(newProcNotForPayorList.get(0).getSubmSvcOvrrdeSta(), procsForOtherPayorList.get(0).getSubmSvcOvrrdeSta(), "Expected AccnProc submSvcOvrrdeSta to be unchanged on proc not for payor, accnId="+accnId+", accnProcSubmSvcOvrrdeSta="+newProcNotForPayorList.get(0).getSubmSvcOvrrdeSta());

        SubmSvc submSvc = submissionDao.getSubmSvc(newQas.getSubmSvcSeqId());
        logger.info("message=Clearing last submit date, submSvcAbbrev="+submSvc.getAbbrev());
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(null, submSvc.getSubmSvcSeqId());

        logger.info("message=Wait for Non-Client Submission Engine to process the claim, accnId="+accnId+", docSeqId="+newQas);
        int newSubmFileSeqId = waitForSubmissionEngine(newQas, QUEUE_WAIT_TIME*2, submSvc.getAbbrev());
        Assert.assertTrue(newSubmFileSeqId > 0, "Expected new submission file ID to be > 0, accnId="+accnId+", docSeqId="+newQas.getDocSeqId()+", newSubmFileSeqId="+newSubmFileSeqId);

        logger.info("message=Wait for Non-Client Statement Engine to process the file");
        boolean isProcessedFile = waitForStatementEngine(rpmDao.getSubmFile(testDb, newSubmFileSeqId), QUEUE_WAIT_TIME*2);
        Assert.assertTrue(isProcessedFile, "Expected file to be processed, accnId="+accnId+", docSeqId="+newQas.getDocSeqId()+", submFileSeqId="+newSubmFileSeqId+", isProcessedFile="+isProcessedFile);
        SubmFile submFile = submissionDao.getSubmFileBySubmFileSeqId(newSubmFileSeqId);
        Assert.assertTrue(DateUtils.isSameDay(submFile.getFileCreatDt(), new Date(System.currentTimeMillis())), "Expected file create date to be today, processed accnId="+accnId+", docSeqId="+newQas.getDocSeqId()+", submFileSeqId="+newSubmFileSeqId+", fileCreatDt="+submFile.getFileCreatDt());

        logger.info("message=Verifying new statement contains only the proc for the payor, accnId="+accnId+", docSeqId="+newQas.getDocSeqId()+", submFileSeqId="+submFile.getSubmFileSeqId()+", procForPayor="+procForPayor);
        newProcForPayorList = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, procForPayor);
        Assert.assertEquals(newProcForPayorList.size(), 1, "Expected 1 AccnProc record, accnId="+accnId+", procForPayor="+procForPayor+", accnProcCount="+newProcForPayorList.size());
        Assert.assertTrue(newProcForPayorList.get(0).getSubmFileSeqId() > 0, "Expected AccnProc submFileSeqId to be > 0, accnId="+accnId+", accnProcSubmFileSeqId="+newProcForPayorList.get(0).getSubmFileSeqId());
        Assert.assertEquals(newProcForPayorList.get(0).getSubmFileSeqId(), newSubmFileSeqId, "Expected AccnProc submFileSeqId to be equal to the value on the new QAS record, accnId="+accnId+", accnProcSubmFileSeqId="+newProcForPayorList.get(0).getSubmFileSeqId()+", qasSubmFileSeqId="+newSubmFileSeqId);

        newProcNotForPayorList = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, procNotForPayor);
        Assert.assertEquals(newProcNotForPayorList.size(), 1, "Expected 1 AccnProc record, accnId="+accnId+", procNotForPayor="+procNotForPayor+", accnProcCount="+newProcNotForPayorList.size());
        Assert.assertEquals(newProcNotForPayorList.get(0).getSubmFileSeqId(), procsForOtherPayorList.get(0).getSubmFileSeqId(), "Expected AccnProc submFileSeqId to be unchanged on proc not for payor, accnId="+accnId+", accnProcSubmFileSeqId="+newProcNotForPayorList.get(0).getSubmFileSeqId());
        Assert.assertEquals(newProcNotForPayorList.get(0).getSubmSvcOvrrdeSta(), procsForOtherPayorList.get(0).getSubmSvcOvrrdeSta(), "Expected AccnProc submSvcOvrrdeSta to be unchanged on proc not for payor, accnId="+accnId+", accnProcSubmSvcOvrrdeSta="+newProcNotForPayorList.get(0).getSubmSvcOvrrdeSta());

        List<SubmFileAudit> newSubmFileAudits = rpmDao.getSubmFileAudits(null, newQas.getDocSeqId());
        Assert.assertEquals(newSubmFileAudits.size(), 1, "Expected 1 new submitted proc, accnId="+accnId+", docSeqId="+newQas.getDocSeqId()+", newSubmFileAuditSize="+newSubmFileAudits.size());
        Assert.assertEquals(newSubmFileAudits.get(0).getProcId(), procForPayor, "Expected submitted proc code in SubmFileAudit to be proc for payor, submittedProcId="+newSubmFileAudits.get(0).getProcId()+", procForPayor="+procForPayor);
    }

    @Test(priority = 1, description = "Test Super Search Force to Resubmit Without Specific Payor")
    @Parameters({"accnId","procForPayor1","procForPayor2"})
    public void testSuperSearchForceToResubmitWithoutSpecificPayor(String accnId, String procForPayor1, String procForPayor2) throws Exception
    {
        logger.info("message=Starting test case, accnId="+accnId+", procForPayor1="+procForPayor1+", procForPayor2="+procForPayor2);

        logger.info("message=Verifying proc for payor 1, accnId="+accnId+", procForPayor1="+procForPayor1);
        List<AccnProc> procForPayor1List = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, procForPayor1);
        Assert.assertEquals(procForPayor1List.size(), 1, "Expected 1 proc for payor 1, accnId="+accnId+", procForPayor1="+procForPayor1+", procForPayor1ListSize="+procForPayor1List.size());
        Assert.assertTrue(procForPayor1List.get(0).getSubmFileSeqId() > 0, "Expected submission file on proc for payor 1 to be > 0, accnId="+accnId+", procForPayor1="+procForPayor1+", submFileSeqId="+procForPayor1List.get(0).getSubmFileSeqId());
        Assert.assertTrue(submissionDao.getSubmFileBySubmFileSeqId(procForPayor1List.get(0).getSubmFileSeqId()).getIsEgateProcessed(), "Expected submission file on proc for payor 1 to be processed, accnId="+accnId+", procForPayor1="+procForPayor1+", submFileSeqId="+procForPayor1List.get(0).getSubmFileSeqId());
        Assert.assertEquals(procForPayor1List.get(0).getSubmPyrPrio(), 1, "Expected proc for payor 1 subm pyr prio to be 1, accnId="+accnId+", procForPayor1="+procForPayor1+", procForPayor1SubmPyrPrio="+procForPayor1List.get(0).getSubmPyrPrio());

        logger.info("message=Verifying proc for payor 2, accnId="+accnId+", procForPayor2="+procForPayor2);
        List<AccnProc> procForPayor2List = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, procForPayor2);
        Assert.assertEquals(procForPayor2List.size(), 1, "Expected 1 proc for payor 2, accnId="+accnId+", procForPayor2="+procForPayor2+", procForPayor2ListSize="+procForPayor2List.size());
        Assert.assertTrue(procForPayor2List.get(0).getSubmFileSeqId() > 0, "Expected submission file on proc for payor 2 to be > 0, accnId="+accnId+", procForPayor2="+procForPayor2+", submFileSeqId="+procForPayor2List.get(0).getSubmFileSeqId());
        Assert.assertTrue(submissionDao.getSubmFileBySubmFileSeqId(procForPayor2List.get(0).getSubmFileSeqId()).getIsEgateProcessed(), "Expected submission file on proc for payor 2 to be processed, accnId="+accnId+", procForPayor2="+procForPayor2+", submFileSeqId="+procForPayor2List.get(0).getSubmFileSeqId());
        Assert.assertEquals(procForPayor2List.get(0).getSubmPyrPrio(), 2, "Expected proc for payor 2 subm pyr prio to be 2, accnId="+accnId+", procForPayor2="+procForPayor2+", procForPayor2SubmPyrPrio="+procForPayor2List.get(0).getSubmPyrPrio());

        List<String> qAccnSubmsToKeep = new ArrayList<>();
        for (QAccnSubm qAccnSubm : submissionDao.getQAccnSubmsBySubmFileSeqId(procForPayor1List.get(0).getSubmFileSeqId()))
        {
            if (StringUtils.equals(qAccnSubm.getAccnId(), accnId))
            {
                qAccnSubmsToKeep.add(String.valueOf(qAccnSubm.getDocSeqId()));
            }
        }
        for (QAccnSubm qAccnSubm : submissionDao.getQAccnSubmsBySubmFileSeqId(procForPayor2List.get(0).getSubmFileSeqId()))
        {
            if (StringUtils.equals(qAccnSubm.getAccnId(), accnId))
            {
                qAccnSubmsToKeep.add(String.valueOf(qAccnSubm.getDocSeqId()));
            }
        }

        logger.info("message=Cleaning QAS, accnId="+accnId+", qasListToKeep="+qAccnSubmsToKeep);
        cleanQASForAccnExceptDocSeqIds(accnId, qAccnSubmsToKeep);
        List<QAccnSubm> currentQasList = submissionDao.getQAccnSubmsByAccnId(accnId);
        Assert.assertEquals(currentQasList.size(), 2, "Expected 2 QAS record, accnId="+accnId+", qasCount="+currentQasList.size());
        // Need to reset QAS claim ID suffixes so  we don't keep incrementing
        logger.info("message=Resetting QAS claim ID suffix, accnId="+accnId+", docSeqId="+currentQasList.get(0)+", currClaimIdSuffix="+currentQasList.get(0).getClaimIdSuffix()+", newClaimIdSuffix=01");
        currentQasList.get(0).setClaimIdSuffix("01");
        databaseSequenceDao.setValueObject(currentQasList.get(0));
        logger.info("message=Resetting QAS claim ID suffix, accnId="+accnId+", docSeqId="+currentQasList.get(1)+", currClaimIdSuffix="+currentQasList.get(1).getClaimIdSuffix()+", newClaimIdSuffix=02");
        currentQasList.get(1).setClaimIdSuffix("02");
        databaseSequenceDao.setValueObject(currentQasList.get(1));

        SuperSearch superSearch = new SuperSearch(driver);
        MenuNavigation menuNavigation = new MenuNavigation(driver, config);
        menuNavigation.navigateToSuperSearchPage();
        superSearch.performForceToResubmitActionOnSuperSearch(accnId, wait);

        List<QAccnSubm> newQasList = submissionDao.getQAccnSubmsByAccnId(accnId);
        Assert.assertEquals(newQasList.size(), 4, "Expected 4 QAS records after Super Search resubmit, accnId="+accnId+", qasCount="+currentQasList.size());

        QAccnSubm newQasForPayor1 = newQasList.get(2);
        Assert.assertNotNull(newQasForPayor1.getNextSubmDt(), "Expected next submit date on new QAS for payor 1 to be not null, accnId="+accnId+", docSeqId="+newQasForPayor1.getDocSeqId());
        Assert.assertEquals(newQasForPayor1.getSubmFileSeqId(), 0, "Expected subm file seq Id on new QAS for payor 1 to be 0, accnId="+accnId+", docSeqId="+newQasForPayor1.getDocSeqId()+", submFileSeqId="+newQasForPayor1.getSubmFileSeqId());
        Assert.assertEquals(newQasForPayor1.getPyrPrio(), 1, "Expected payor prio on new QAS for payor 1 to be 1, accnId="+accnId+", docSeqId="+newQasForPayor1.getDocSeqId()+", pyrPrio="+newQasForPayor1.getPyrPrio());

        QAccnSubm newQasForPayor2 = newQasList.get(3);
        Assert.assertNotNull(newQasForPayor2.getNextSubmDt(), "Expected next submit date on new QAS for payor 2 to be not null, accnId="+accnId+", docSeqId="+newQasForPayor2.getDocSeqId());
        Assert.assertEquals(newQasForPayor2.getSubmFileSeqId(), 0, "Expected subm file seq Id on new QAS for payor 2 to be 0, accnId="+accnId+", docSeqId=="+newQasForPayor2.getDocSeqId()+", submFileSeqId="+newQasForPayor2.getSubmFileSeqId());
        Assert.assertEquals(newQasForPayor2.getPyrPrio(), 2, "Expected payor prio on new QAS for payor 2 to be 2, accnId="+accnId+", docSeqId="+newQasForPayor2.getDocSeqId()+", pyrPrio="+newQasForPayor2.getPyrPrio());

        procForPayor1List = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, procForPayor1);
        Assert.assertEquals(procForPayor1List.size(), 1, "Expected 1 AccnProc record, accnId="+accnId+", procForPayor1="+procForPayor1+", accnProcCount="+procForPayor1List.size());
        Assert.assertEquals(procForPayor1List.get(0).getSubmFileSeqId(), 0, "Expected AccnProc submFileSeqId to be zero on proc for payor 1 accnId="+accnId+", accnProcSubmFileSeqId="+procForPayor1List.get(0).getSubmFileSeqId());
        Assert.assertEquals(procForPayor1List.get(0).getSubmSvcOvrrdeSta(), 0, "Expected AccnProc submSvcOvrrdeSta to be zero on proc for payor 1, accnId="+accnId+", accnProcSubmSvcOvrrdeSta="+procForPayor1List.get(0).getSubmSvcOvrrdeSta());

        procForPayor2List = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, procForPayor2);
        Assert.assertEquals(procForPayor2List.size(), 1, "Expected 1 AccnProc record, accnId="+accnId+", procForPayor2="+procForPayor2+", accnProcCount="+procForPayor2List.size());
        Assert.assertEquals(procForPayor2List.get(0).getSubmFileSeqId(), 0, "Expected AccnProc submFileSeqId to be zero on proc for payor 2 accnId="+accnId+", accnProcSubmFileSeqId="+procForPayor2List.get(0).getSubmFileSeqId());
        Assert.assertEquals(procForPayor2List.get(0).getSubmSvcOvrrdeSta(), 0, "Expected AccnProc submSvcOvrrdeSta to be zero on proc for payor 2, accnId="+accnId+", accnProcSubmSvcOvrrdeSta="+procForPayor2List.get(0).getSubmSvcOvrrdeSta());

        SubmSvc submSvcForPayor1 = submissionDao.getSubmSvc(newQasForPayor1.getSubmSvcSeqId());
        logger.info("message=Clearing last submit date, submSvcAbbrev="+submSvcForPayor1.getAbbrev());
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(null, submSvcForPayor1.getSubmSvcSeqId());

        logger.info("message=Wait for Non-Client Submission Engine to process the claim for payor 1, accnId="+accnId+", docSeqId="+newQasForPayor1.getDocSeqId());
        int newSubmFileSeqIdForPayor1 = waitForSubmissionEngine(newQasForPayor1, QUEUE_WAIT_TIME*2, submSvcForPayor1.getAbbrev());
        Assert.assertTrue(newSubmFileSeqIdForPayor1 > 0, "Expected new submission file ID for payor 1 submission to be > 0, accnId="+accnId+", docSeqId="+newQasForPayor1.getDocSeqId()+", newSubmFileSeqIdForPayor1="+newSubmFileSeqIdForPayor1);

        logger.info("message=Wait for Non-Client Statement Engine to process the file for payor 1");
        boolean isProcessedFileForPayor1 = waitForStatementEngine(rpmDao.getSubmFile(testDb, newSubmFileSeqIdForPayor1), QUEUE_WAIT_TIME*2);
        Assert.assertTrue(isProcessedFileForPayor1, "Expected file for payor 1 to be processed, accnId="+accnId+", docSeqId="+newQasForPayor1.getDocSeqId()+", submFileSeqIdForPayor1="+newSubmFileSeqIdForPayor1+", isProcessedFileForPayor1="+isProcessedFileForPayor1);
        SubmFile submFileForPayor1 = submissionDao.getSubmFileBySubmFileSeqId(newSubmFileSeqIdForPayor1);
        Assert.assertTrue(DateUtils.isSameDay(submFileForPayor1.getFileCreatDt(), new Date(System.currentTimeMillis())), "Expected file create date for payor 1 to be today, processed accnId="+accnId+", docSeqId="+newQasForPayor1.getDocSeqId()+", submFileSeqId="+newSubmFileSeqIdForPayor1+", fileCreatDt="+submFileForPayor1.getFileCreatDt());

        logger.info("message=Verifying new statement for payor 1 contains only the proc for the payor, accnId="+accnId+", docSeqId="+newQasForPayor1.getDocSeqId()+", submFileSeqId="+submFileForPayor1.getSubmFileSeqId()+", procForPayor1="+procForPayor1);
        procForPayor1List = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, procForPayor1);
        Assert.assertEquals(procForPayor1List.size(), 1, "Expected 1 AccnProc record, accnId="+accnId+", procForPayor1="+procForPayor1+", accnProcCount="+procForPayor1List.size());
        Assert.assertTrue(procForPayor1List.get(0).getSubmFileSeqId() > 0, "Expected AccnProc submFileSeqId to be > 0, accnId="+accnId+", accnProcSubmFileSeqId="+procForPayor1List.get(0).getSubmFileSeqId());
        Assert.assertEquals(procForPayor1List.get(0).getSubmFileSeqId(), newSubmFileSeqIdForPayor1, "Expected AccnProc submFileSeqId to be equal to the value on the new QAS record, accnId="+accnId+", accnProcSubmFileSeqId="+procForPayor1List.get(0).getSubmFileSeqId()+", qasSubmFileSeqId="+newSubmFileSeqIdForPayor1);

        SubmSvc submSvcForPayor2 = submissionDao.getSubmSvc(newQasForPayor2.getSubmSvcSeqId());
        logger.info("message=Clearing last submit date, submSvcAbbrev="+submSvcForPayor2.getAbbrev());
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(null, submSvcForPayor2.getSubmSvcSeqId());

        logger.info("message=Wait for Non-Client Submission Engine to process the claim for payor 2, accnId="+accnId+", docSeqId="+newQasForPayor2.getDocSeqId());
        int newSubmFileSeqIdForPayor2 = waitForSubmissionEngine(newQasForPayor2, QUEUE_WAIT_TIME*2, submSvcForPayor2.getAbbrev());
        Assert.assertTrue(newSubmFileSeqIdForPayor2 > 0, "Expected new submission file ID for payor 2 submission to be > 0, accnId="+accnId+", docSeqId="+newQasForPayor2.getDocSeqId()+", newSubmFileSeqIdForPayor2="+newSubmFileSeqIdForPayor2);

        logger.info("message=Wait for Non-Client Statement Engine to process the file for payor 2");
        boolean isProcessedFileForPayor2 = waitForStatementEngine(rpmDao.getSubmFile(testDb, newSubmFileSeqIdForPayor2), QUEUE_WAIT_TIME*2);
        Assert.assertTrue(isProcessedFileForPayor2, "Expected file for payor 2 to be processed, accnId="+accnId+", docSeqId="+newQasForPayor2.getDocSeqId()+", submFileSeqIdForPayor2="+newSubmFileSeqIdForPayor2+", isProcessedFileForPayor2="+isProcessedFileForPayor2);
        SubmFile submFileForPayor2 = submissionDao.getSubmFileBySubmFileSeqId(newSubmFileSeqIdForPayor2);
        Assert.assertTrue(DateUtils.isSameDay(submFileForPayor2.getFileCreatDt(), new Date(System.currentTimeMillis())), "Expected file create date for payor 2 to be today, processed accnId="+accnId+", docSeqId="+newQasForPayor2.getDocSeqId()+", submFileSeqId="+newSubmFileSeqIdForPayor2+", fileCreatDt="+submFileForPayor2.getFileCreatDt());
        List<SubmFileAudit> newSubmFileAuditsForPayor1 = rpmDao.getSubmFileAudits(null, newQasForPayor1.getDocSeqId());
        Assert.assertEquals(newSubmFileAuditsForPayor1.size(), 1, "Expected 1 new submitted proc for payor 1, accnId="+accnId+", docSeqId="+newQasForPayor1.getDocSeqId()+", newSubmFileAuditSize="+newSubmFileAuditsForPayor1.size());
        Assert.assertEquals(newSubmFileAuditsForPayor1.get(0).getProcId(), procForPayor1, "Expected submitted proc code in SubmFileAudit to be proc for payor 1, submittedProcId="+newSubmFileAuditsForPayor1.get(0).getProcId()+", procForPayor1="+procForPayor1);

        logger.info("message=Verifying new statement for payor 2 contains only the proc for the payor, accnId="+accnId+", docSeqId="+newQasForPayor2.getDocSeqId()+", submFileSeqId="+submFileForPayor2.getSubmFileSeqId()+", procForPayor2="+procForPayor2);
        procForPayor2List = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, procForPayor2);
        Assert.assertEquals(procForPayor2List.size(), 1, "Expected 1 AccnProc record, accnId="+accnId+", procForPayor2="+procForPayor2+", accnProcCount="+procForPayor2List.size());
        Assert.assertTrue(procForPayor2List.get(0).getSubmFileSeqId() > 0, "Expected AccnProc submFileSeqId to be > 0, accnId="+accnId+", accnProcSubmFileSeqId="+procForPayor2List.get(0).getSubmFileSeqId());
        Assert.assertEquals(procForPayor2List.get(0).getSubmFileSeqId(), newSubmFileSeqIdForPayor2, "Expected AccnProc submFileSeqId to be equal to the value on the new QAS record, accnId="+accnId+", accnProcSubmFileSeqId="+procForPayor2List.get(0).getSubmFileSeqId()+", qasSubmFileSeqId="+newSubmFileSeqIdForPayor2);//
        List<SubmFileAudit> newSubmFileAuditsForPayor2 = rpmDao.getSubmFileAudits(null, newQasForPayor2.getDocSeqId());
        Assert.assertEquals(newSubmFileAuditsForPayor2.size(), 1, "Expected 1 new submitted proc for payor 2, accnId="+accnId+", docSeqId="+newQasForPayor2.getDocSeqId()+", newSubmFileAuditSize="+newSubmFileAuditsForPayor2.size());
        Assert.assertEquals(newSubmFileAuditsForPayor2.get(0).getProcId(), procForPayor2, "Expected submitted proc code in SubmFileAudit to be proc for payor 2, submittedProcId="+newSubmFileAuditsForPayor2.get(0).getProcId()+", procForPayor2="+procForPayor2);
    }

    private void enterOccurrenceCode(AccessionDetail accessionDetail, String pyrAbbrv, String code)
    {
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("occurrenceCode"))));
        logger.info("Entering Code Description into Occurrence Add Record, code=" + code);
        accessionDetail.setOccurrenceCodeId(code, wait);
        logger.info("Entering Payor Id into Occurrence Add Record, pyrAbbrv=" + pyrAbbrv);
        accessionDetail.setOccurrencePyr(pyrAbbrv, wait);
        wait.until(ExpectedConditions.elementToBeClickable(accessionDetail.okPopupBtn()));
        accessionDetail.okPopupBtn().click();
        logger.info("Clicked Submit button on Add Record to Occurrence Code");
    }

    private boolean waitForStatementEngineToDeleteFile(String accnId, long maxTime)
            throws InterruptedException, XifinDataAccessException
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isQAccnSubmRecordExist = rpmDao.isQAccnSubmRecordExistByAccnId(testDb, accnId);
        while (isQAccnSubmRecordExist && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for Statement Engine to delete q_accn_subm file,  accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME);
            isQAccnSubmRecordExist = rpmDao.isQAccnSubmRecordExistByAccnId(testDb, accnId);
        }
        return !isQAccnSubmRecordExist;
    }
}
