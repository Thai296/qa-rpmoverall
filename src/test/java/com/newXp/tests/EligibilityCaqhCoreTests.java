package com.newXp.tests;

import com.mbasys.mars.ejb.entity.accnEligHist.AccnEligHist;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.accession.accessionProcessing.LoadAccession;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

public class EligibilityCaqhCoreTests extends AccessionDetailEligibilityTest
{
    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt001(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT001, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt002(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT002, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt003(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT003, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt004(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT004, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt005(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT005, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt006(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT006, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt007(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT007, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt008(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT008, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt009(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT009, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt010(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT010, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt011(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT011, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt012(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT012, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt013(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT013, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt014(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT014, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt015(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT015, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt016(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT016, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt017(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT017, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt018(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT018, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt019(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT019, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt020(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT020, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt021(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT021, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt022(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT022, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt023(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT023, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    @Test
    @Parameters({"accnId", "eligStaTypDescr"})
    public void testCt024(String accnId, String eligStaTypDescr) throws Exception
    {
        logger.info("message=Starting test case CT024, accnId=" + accnId);
        testCaqhCore(accnId, eligStaTypDescr);
    }

    private void testCaqhCore(String accnId, String eligStaTypDescr) throws Exception
    {
        LoadAccession loadAccession = new LoadAccession(driver);
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        accessionDetail.loadAccnOnAccnDetail(wait, accnId);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Check Eligibility checkbox");
        clickHiddenPageObject(accessionDetail.checkEligibilityCheckbox(), 0);
        logger.info("Click Submit button");
        accessionDetail.saveAndClear(wait);

        List<AccnEligHist> accnEligHists = accessionDao.getAccnEligHistByAccnId(accnId);
        logger.info("Verifying accession eligibility history is updated, accnId=" + accnId);
        Assert.assertEquals(accnEligHists.size(), 1);
        Assert.assertNotEquals(accnEligHists.get(0).getPyrId(), 0);
        Assert.assertNull(accnEligHists.get(0).getTranslatedPyrId());
        Assert.assertNotNull(accnEligHists.get(0).getTransDt());
        Assert.assertNotNull(accnEligHists.get(0).getTransId());
        String actualEligStaTypDescr = StringUtils.upperCase(rpmDao.getEligStaTyp(null, accnEligHists.get(0).getEligStaTypId()).getDescr());
        Assert.assertEquals(actualEligStaTypDescr, StringUtils.upperCase(eligStaTypDescr));
    }
}
