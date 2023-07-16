package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnEligHist.AccnEligHist;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrElig.PyrElig;
import com.mbasys.mars.eligibility.EligMap;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.overall.accession.accessionProcessing.AccessionDetail;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

public class EligibilityEngineExternalTest extends EligibilityEngineTest
{
    @Test(priority = 1, description = "DORADO eligibility service is up and returning valid eligibility response")
    @Parameters({"accnId", "pyrAbbrv", "eligSvcId"})
    public void testXPR_1551(String accnId, String pyrAbbrv, String eligSvcId) throws Exception
    {
        logger.info("Starting Test Case: XPR-1551, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        logger.info("Clear Accn pyr and proc errs = " + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(testDb,accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);

        logger.info("Make sure payor is setup elig service = " + pyrAbbrv);

        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        int origPyrId = origPyr.getPyrId();

        List<PyrElig> pyrElig = rpmDao.getPyrElig(testDb, origPyrId);
        Assert.assertNotNull(pyrElig);
        Assert.assertEquals(pyrElig.size(), 1);
        String outPayor = pyrElig.get(0).getOutPyrId();

        logger.info("Getting payor xifin_payor_elig record for outgoing pyr, outPyrId="+outPayor);
        List<String> xifinPyrElig = daoManagerEligibilityWS.getXifinPyrEligByExtPyrId(outPayor);
        Assert.assertFalse(xifinPyrElig.isEmpty(), "Expected XifinPyrElig record, outPyrId="+outPayor);

        String eligSvc = xifinPyrElig.get(0);
        logger.info("Getting xifin_elig_svc by payor xifin_payor_elig record for outgoing pyr, eligSvc = "+eligSvc);
        List<String> xifinEligSvc = daoManagerEligibilityWS.getXifinEligSvcByEligSvcId(eligSvc);
        logger.info("Make sure xifin elig svc is Dorado, eligSvc = "+eligSvc);
        Assert.assertEquals(xifinEligSvc.get(3), eligSvcId);

        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

        loadAccession(accnId);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        // Verify the primary payor is correct
        // Verify the primary payor is correct
        if(!accessionDetail.primaryPayorAbbrText().getText().equals(origPyr.getName()))
        {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + accessionDetail.primaryPayorAbbrText().getText() + ", newPyrId=" + origPyr.getName());
            setOriginalPyrToAccn(accessionDetail, accnId, pyrAbbrv);
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        //   accessionDetail.setAccnId(accnId, wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        Accn accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertTrue(accn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.size(), 1, "Expected accnEligHist was not found,");
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), origPyrId);
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNotEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_UNREACHABLE);


        verifyEligTransLog(accnEligHists.get(0), EligMap.ELIG_STA_TYP_REJECTED);
    }

    @Test(priority = 1, description = "Availability eligibility service is up and returning valid eligibility response")
    @Parameters({"accnId", "pyrAbbrv", "eligSvcId"})
    public void testXPR_1550(String accnId, String pyrAbbrv, String eligSvcId) throws Exception
    {
        logger.info("Starting Test Case: XPR-1550, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        logger.info("Clear Accn pyr and proc errs = " + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(testDb,accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);

        logger.info("Make sure payor is setup elig service = " + pyrAbbrv);

        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        int origPyrId = origPyr.getPyrId();

        List<PyrElig> pyrElig = rpmDao.getPyrElig(testDb, origPyrId);
        Assert.assertNotNull(pyrElig);
        Assert.assertEquals(pyrElig.size(), 1);
        String outPayor = pyrElig.get(0).outPyrId;

        logger.info("Getting payor xifin_payor_elig record for outgoing pyr");
        List<String> xifinPyrElig = daoManagerEligibilityWS.getXifinPyrEligByExtPyrId(outPayor);
        String eligSvc = xifinPyrElig.get(0);
        logger.info("Getting xifin_elig_svc by payor xifin_payor_elig record for outgoing pyr, eligSvc = "+eligSvc);
        List<String> xifinEligSvc = daoManagerEligibilityWS.getXifinEligSvcByEligSvcId(eligSvc);
        logger.info("Make sure xifin elig svc is Availity, eligSvc = "+eligSvc);
        Assert.assertEquals(xifinEligSvc.get(3), eligSvcId);

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

        loadAccession(accnId);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        // Verify the accession is loaded
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        // Verify the primary payor is correct
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        //   accessionDetail.setAccnId(accnId, wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertTrue(accn.getStaId() == AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        logger.info("Found accession eligibility history records, accnId=" + accnId + ", count=" + accnEligHists.size());
        Assert.assertEquals(accnEligHists.size(), 1, "Expected accnEligHist was not found,");
        logger.info("Verifying accession eligibility history record, accnEligHist="+accnEligHists.get(0));
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), origPyrId);
        logger.info("Verifying translated payor Id is empty, translatedPyrId="+accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId(), "Translated Payor ID must be NULL,");
        logger.info("Verifying transaction Id is set, transId="+accnEligHists.get(0).getTransId());
        Assert.assertNotNull(accnEligHists.get(0).getTransId(), "Transaction ID must not be NULL,");
        logger.info("Verifying transaction date is set, transDt="+accnEligHists.get(0).getTransDt());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt(), "Transaction date must not be NULL,");
        Assert.assertNotEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_UNREACHABLE, "Eligibility status is UNREACHABLE,");

        verifyEligTransLog(accnEligHists.get(0));
    }

    @Test(priority = 1, description = "OPTUM eligibility service is up and returning valid eligibility response")
    @Parameters({"accnId", "pyrAbbrv", "eligSvcId"})
    public void testXPR_1552(String accnId, String pyrAbbrv, String eligSvcId) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: XPR-1552, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        logger.info("Clear Accn pyr and proc errs = " + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(testDb,accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);

        logger.info("Make sure payor is setup elig service = " + pyrAbbrv);

        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        int origPyrId = origPyr.getPyrId();

        List<PyrElig> pyrElig = rpmDao.getPyrElig(testDb, origPyrId);
        Assert.assertNotNull(pyrElig);
        Assert.assertEquals(pyrElig.size(), 1);
        String outPayor = pyrElig.get(0).outPyrId;

        logger.info("Getting payor xifin_payor_elig record for outgoing pyr");
        List<String> xifinPyrElig = daoManagerEligibilityWS.getXifinPyrEligByExtPyrId(outPayor);
        String eligSvc = xifinPyrElig.get(0);
        logger.info("Getting xifin_elig_svc by payor xifin_payor_elig record for outgoing pyr, eligSvc = "+eligSvc);
        List<String> xifinEligSvc = daoManagerEligibilityWS.getXifinEligSvcByEligSvcId(eligSvc);
        logger.info("Make sure xifin elig svc is OPTUM, eligSvc = "+eligSvc);
        Assert.assertEquals(xifinEligSvc.get(3), eligSvcId);

        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        //   accessionDetail.setAccnId(accnId, wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        Accn accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), origPyrId);
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNotEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_UNREACHABLE);

        verifyEligTransLog(accnEligHists.get(0), EligMap.ELIG_STA_TYP_REJECTED);
    }

    @Test(priority = 1, description = "PNT-5010 eligibility service is up and returning valid eligibility response")
    @Parameters({"accnId", "pyrAbbrv", "eligSvcId"})
    public void testXPR_1548(String accnId, String pyrAbbrv, String eligSvcId) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: XPR-1548, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        logger.info("Clear Accn pyr and proc errs = " + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(testDb,accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);

        logger.info("Make sure payor is setup elig service = " + pyrAbbrv);
        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        int origPyrId = origPyr.getPyrId();

        List<PyrElig> pyrElig = rpmDao.getPyrElig(testDb, origPyrId);
        Assert.assertNotNull(pyrElig);
        Assert.assertEquals(pyrElig.size(), 1);
        String outPayor = pyrElig.get(0).outPyrId;

        logger.info("Getting payor xifin_payor_elig record for outgoing pyr");
        List<String> xifinPyrElig = daoManagerEligibilityWS.getXifinPyrEligByExtPyrId(outPayor);
        String eligSvc = xifinPyrElig.get(0);
        logger.info("Getting xifin_elig_svc by payor xifin_payor_elig record for outgoing pyr, eligSvc = "+eligSvc);
        List<String> xifinEligSvc = daoManagerEligibilityWS.getXifinEligSvcByEligSvcId(eligSvc);
        logger.info("Make sure xifin elig svc is PNT-5010, eligSvc = "+eligSvc);
        Assert.assertEquals(xifinEligSvc.get(3), eligSvcId);

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        if(!accessionDetail.primaryPayorAbbrText().getText().equals(origPyr.getName()))
        {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + accessionDetail.primaryPayorAbbrText().getText() + ", newPyrId=" + origPyr.getName());
            setOriginalPyrToAccn(accessionDetail, accnId, pyrAbbrv);
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        //   accessionDetail.setAccnId(accnId, wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId() , AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), origPyrId);
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNotEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_UNREACHABLE);

        verifyEligTransLog(accnEligHists.get(0), EligMap.ELIG_STA_TYP_REJECTED);
    }

    @Test(priority = 1, description = "Hmsa-5010 eligibility service is up and returning valid eligibility response")
    @Parameters({"accnId", "pyrAbbrv", "eligSvcId"})
    public void testXPR_1549(String accnId, String pyrAbbrv, String eligSvcId) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: XPR-1549, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        logger.info("Clear Accn pyr and proc errs = " + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(testDb,accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);

        logger.info("Make sure payor is setup elig service = " + pyrAbbrv);

        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        int origPyrId = origPyr.getPyrId();

        List<PyrElig> pyrElig = rpmDao.getPyrElig(testDb, origPyrId);
        Assert.assertNotNull(pyrElig);
        Assert.assertEquals(pyrElig.size(), 1);
        String outPayor = pyrElig.get(0).outPyrId;

        logger.info("Getting payor xifin_payor_elig record for outgoing pyr");
        List<String> xifinPyrElig = daoManagerEligibilityWS.getXifinPyrEligByExtPyrId(outPayor);
        String eligSvc = xifinPyrElig.get(0);
        logger.info("Getting xifin_elig_svc by payor xifin_payor_elig record for outgoing pyr, eligSvc = "+eligSvc);
        List<String> xifinEligSvc = daoManagerEligibilityWS.getXifinEligSvcByEligSvcId(eligSvc);
        logger.info("Make sure xifin elig svc is Hmsa-5010, eligSvc = "+eligSvc);
        Assert.assertEquals(xifinEligSvc.get(3), eligSvcId);

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        if(!accessionDetail.primaryPayorAbbrText().getText().equals(origPyr.getName()))
        {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + accessionDetail.primaryPayorAbbrText().getText() + ", newPyrId=" + origPyr.getName());
            setOriginalPyrToAccn(accessionDetail, accnId, pyrAbbrv);
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        //   accessionDetail.setAccnId(accnId, wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), origPyrId);
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNotEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_UNREACHABLE);

        verifyEligTransLog(accnEligHists.get(0), EligMap.ELIG_STA_TYP_REJECTED);
    }

    @Test(priority = 1, description = "BCBSAZ-5010 eligibility service is up and returning valid eligibility response")
    @Parameters({"accnId", "pyrAbbrv", "eligSvcId"})
    public void testBcbsAz5010(String accnId, String pyrAbbrv, String eligSvcId) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: testBcbsAz5010, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        logger.info("Clear Accn pyr and proc errs = " + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(testDb,accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);

        logger.info("Make sure payor is setup elig service = " + pyrAbbrv);

        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        int origPyrId = origPyr.getPyrId();

        List<PyrElig> pyrElig = rpmDao.getPyrElig(testDb, origPyrId);
        Assert.assertNotNull(pyrElig);
        Assert.assertEquals(pyrElig.size(), 1);
        String outPayor = pyrElig.get(0).outPyrId;

        logger.info("Getting payor xifin_payor_elig record for outgoing pyr");
        List<String> xifinPyrElig = daoManagerEligibilityWS.getXifinPyrEligByExtPyrId(outPayor);
        String eligSvc = xifinPyrElig.get(0);
        logger.info("Getting xifin_elig_svc by payor xifin_payor_elig record for outgoing pyr, eligSvc = "+eligSvc);
        List<String> xifinEligSvc = daoManagerEligibilityWS.getXifinEligSvcByEligSvcId(eligSvc);
        logger.info("Make sure xifin elig svc is Hmsa-5010, eligSvc = "+eligSvc);
        Assert.assertEquals(xifinEligSvc.get(3), eligSvcId);

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        // Verify the primary payor is correct
        if(!accessionDetail.primaryPayorAbbrText().getText().equals(origPyr.getName()))
        {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + accessionDetail.primaryPayorAbbrText().getText() + ", newPyrId=" + origPyr.getName());
            setOriginalPyrToAccn(accessionDetail, accnId, pyrAbbrv);
            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        //   accessionDetail.setAccnId(accnId, wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), origPyrId);
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_INELIGIBLE);

        verifyEligTransLog(accnEligHists.get(0), EligMap.ELIG_STA_TYP_REJECTED);
    }

    @Test(priority = 1, description = "BCBSTN eligibility service is up and returning valid eligibility response")
    @Parameters({"accnId", "pyrAbbrv", "eligSvcId"})
    public void testBcbsTn(String accnId, String pyrAbbrv, String eligSvcId) throws Exception {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Starting Test Case: testBcbsTn, accnId=" + accnId + ", pyrAbbrv=" + pyrAbbrv);
        logger.info("Clear Accn pyr and proc errs = " + accnId);
        rpmDao.deleteAccnPyrErrByAccnId(testDb, accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);

        logger.info("Make sure payor is setup elig service = " + pyrAbbrv);

        Pyr origPyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv);
        int origPyrId = origPyr.getPyrId();

        List<PyrElig> pyrElig = rpmDao.getPyrElig(testDb, origPyrId);
        Assert.assertNotNull(pyrElig);
        Assert.assertEquals(pyrElig.size(), 1);
        String outPayor = pyrElig.get(0).outPyrId;

        logger.info("Getting payor xifin_payor_elig record for outgoing pyr");
        List<String> xifinPyrElig = daoManagerEligibilityWS.getXifinPyrEligByExtPyrId(outPayor);
        String eligSvc = xifinPyrElig.get(0);

        logger.info("Getting xifin_elig_svc by payor xifin_payor_elig record for outgoing pyr, eligSvc = " + eligSvc);
        List<String> xifinEligSvc = daoManagerEligibilityWS.getXifinEligSvcByEligSvcId(eligSvc);
        logger.info("Make sure xifin elig svc is BCBSTN, eligSvc = " + eligSvc);
        Assert.assertEquals(xifinEligSvc.get(3), eligSvcId);

        Accn accn = accessionDao.getAccn(accnId);
        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        List<AccnPyr> accnPyrs = accessionDao.getAccnPyrs(accnId);

        logger.info("Verifying accession eligibility history is cleared, accnId=" + accnId);
        verifyAccnEligDataIsCleared(accnEligHists, accnPyrs);

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        // Verify the primary payor is correct
        if(!accessionDetail.primaryPayorAbbrText().getText().equals(origPyr.getName())) {
            logger.info("Updating payor on the accnId=" + accnId + ", curPyrId=" + accessionDetail.primaryPayorAbbrText().getText() + ", newPyrId=" + origPyr.getName());
            setOriginalPyrToAccn(accessionDetail, accnId, pyrAbbrv);

            // Verify the accession is loaded
            Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        }
        Assert.assertEquals(accessionDetail.primaryPayorAbbrText().getText(), origPyr.getName());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accessionDetail.clickRepriceCheckbox(wait);
        accessionDetail.saveAndClear(wait);

        // Reload accession from DB
        accn = accessionDao.getAccn(accnId);
        logger.info("Verifying pricing is backed out on accession, accnId=" + accnId + ", staId=" + accn.getStaId());
        Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_REPORTED);

        // Wait for accn to get out of eligibility queue
        Assert.assertTrue(isOutOfEligibilityQueue(accnId, QUEUE_WAIT_TIME), "Accession did not leave eligibility queue,");

        // Verify eligibility data
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertEquals(accnEligHists.get(0).getPyrId(), origPyrId);
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertEquals(accnEligHists.get(0).getEligStaTypId(), EligMap.ELIG_STA_TYP_INELIGIBLE);

        verifyEligTransLog(accnEligHists.get(0), EligMap.ELIG_STA_TYP_REJECTED);
    }

}
