package com.newXp.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnPmt.AccnPmt;
import com.mbasys.mars.ejb.entity.accnProc.AccnProc;
import com.mbasys.mars.ejb.entity.claimTyp.ClaimTyp;
import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.qAccnSubm.QAccnSubm;
import com.mbasys.mars.ejb.entity.submClaimAudit.SubmClaimAudit;
import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.mbasys.mars.ejb.entity.submFileAudit.SubmFileAudit;
import com.mbasys.mars.ejb.entity.systemSettingOverrideByFac.SystemSettingOverrideByFac;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.PyrGrpMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.accession.accnSingleStatement.SingleStatement;
import com.overall.menu.MenuNavigation;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.utils.SeleniumBaseTest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class SingleStatementNonClientFormatsTests extends SeleniumBaseTest
{
    private PDFParser parser;
    private PDFTextStripper pdfStripper;
    private PDDocument pdDoc;
    private COSDocument cosDoc;
    private AccessionDetail accessionDetail;
    private SingleStatement singleStatement;
    private MenuNavigation navigation;
    private static final SimpleDateFormat DATE_FORMAT_HHMM = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat DATE_FORMAT_MMDDYYYY = new SimpleDateFormat("MMddyyyy");
    private static final String CLAIM_STATUS_ENGINE = "ClaimStatusEngine";
    private static final Logger LOG = Logger.getLogger(SingleStatementNonClientFormatsTests.class);
    private static final String OS_NAME = System.getProperty("os.name");
    private static final String WINDOWS = "windows";

    @BeforeSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running BeforeSuite");
            // Disable excess Selenium logging
            java.util.logging.Logger.getLogger("org.openqa.selenium.remote").setLevel(java.util.logging.Level.OFF);
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
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

    @BeforeMethod(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins",})
    public void beforeMethod(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, String disableBrowserPlugins, Method method)
    {
        try
        {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            logIntoSso(ssoUsername, ssoPassword);
            navigation = new MenuNavigation(driver, config);
            navigation.navigateToSingleStatementPage();
        }
        catch (Exception e)
        {
            logger.error("Error running BeforeMethod", e);
        }
    }

    @Test(alwaysRun = true, description = "Generate Secondary pyr Cms1500 + EOB report")
    @Parameters({"accnId", "primPyrAbbrv", "secondPyrAbbrv", "submSvcAbbrev", "procs", "fac"})
    public void testXPR_1946(String accnId, String primPyrAbbrv, String secondPyrAbbrv, String submSvcAbbrev, String procs, String fac) throws Exception
    {
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));
        logger.info("Starting Test Case XPR_1946 - Accn-SingleStatement-Generate Secondary pyr Cms1500 + EOB report");
        logger.info("Clearing AccnPyrErrs, accnId=" + accnId);
        Pyr primPyr = rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv);
        Pyr secondPyr = rpmDao.getPyrByPyrAbbrv(testDb, secondPyrAbbrv);
        List<String> expectedProcList = Arrays.asList(org.apache.commons.lang3.StringUtils.split(procs));
        cleanQASForPyrOnAccn(accnId, secondPyr.getPyrId());
        rpmDao.deleteAccnPyrErrByAccnId(testDb, accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);

        File statementFile = submitClaimsOnSingleStatement(accnId, secondPyrAbbrv, submSvcAbbrev, 2, wait, "", true);
        String textStatement = convertPdfToText(statementFile);
        File eobFile = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + (statementFile.getName().replace("CMSv02-12", "CMSv02-12EOB")));
        Assert.assertTrue(eobFile.exists());
        logger.info("Verify File content, submFileName " +eobFile.getName());
        String textEOB = convertPdfToText(eobFile);
        List<AccnPmt> accnPmts = rpmDao.getAccnPmtsByAccnId(testDb, accnId);
        logger.info("Make sure New Statement File contains accnId="+accnId+", primPyr="+primPyr.getName());
        Assert.assertTrue(textStatement.contains(accnId.substring(0,23)));
        Assert.assertTrue(textStatement.contains(secondPyr.getName().toUpperCase()));
        Assert.assertTrue(textStatement.contains(primPyr.getName().toUpperCase()));

        logger.info("Make sure New EOB File contains accnId="+accnId+", primPyr="+primPyr.getName());
        Assert.assertTrue(textEOB.contains(accnId.substring(0,13)));
        Assert.assertTrue(textEOB.contains("REMITTANCE"));
        Assert.assertTrue(textStatement.contains(primPyr.getName().toUpperCase()));
        for (String proc : expectedProcList)
        {
            logger.info("Make sure New Statement File contains accnProc="+proc);
            List<AccnProc> accnProcs = accessionDao.getAccnProcByAccnIdAndProcCd(accnId, proc);
            for (AccnProc accnProc : accnProcs)
            {
                logger.info("Make sure New Statement File contains accnProc="+accnProc.getProcId());
                logger.info("Make sure New Statement File contains accnProcBill="+accnProc.getBilPrc());
                Assert.assertTrue(textStatement.contains(accnProc.getProcId()));
                Assert.assertTrue(textStatement.contains(String.valueOf(accnProc.getBilPrc()).replace(".", " ")));
                logger.info("Make sure New EOB File contains accnProc="+accnProc.getProcId());
                logger.info("Make sure New EOB File contains accnProc.getExpPrc()="+accnProc.getExpPrc());
                Assert.assertTrue(textEOB.contains(accnProc.getProcId()));
                Assert.assertTrue(textEOB.contains(String.valueOf(accnProc.getBilPrc())));
            }
        }
        logger.info("Make sure New Statement File contains fac="+fac);
        Assert.assertTrue(textStatement.contains(fac));

    }

    @Test(alwaysRun = true, description = "Generate UB04, replacement claim type")
    @Parameters({"accnId", "primPyrAbbrv", "submSvcAbbrev", "fac", "supportedBillTypes"})
    public void testXPR_1947(String accnId, String primPyrAbbrv, String submSvcAbbrev, String fac, String supportedBillTypes) throws Exception
    {
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));
        logger.info("Starting Test Case XPR_1947 - Accn-SingleStatement-Generate UB04, corrected claim type");
        Pyr primPyr = rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv);
        cleanQASForPyrOnAccn(accnId, primPyr.getPyrId());
        logger.info("Clearing AccnPyrErrs, accnId=" + accnId);
        logger.info("Make sure Primary pyr is setup with only Claim Type 85X");
        Assert.assertEquals(rpmDao.getPyrBillTyp(testDb, primPyr.getPyrBilTypId85x()).getAbbrev(), supportedBillTypes);
        Assert.assertEquals(primPyr.getPyrBilTypId14x(), 0);

        rpmDao.deleteAccnPyrErrByAccnId(testDb, accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);
        String correctedBillType="857-Replacement Critical Access Hospital Claim";
        File statement = submitClaimsOnSingleStatement(accnId, primPyrAbbrv, submSvcAbbrev, 1, wait, correctedBillType, true);
        logger.info("Verify File content, submFileName " +statement.getName());
        String textStatement = convertPdfToText(statement);
        logger.info("Make sure New Statement File contains accnId="+accnId+", primPyr="+primPyr.getName());
        Assert.assertTrue(textStatement.contains(accnId.substring(0,20)));
        Assert.assertTrue(textStatement.contains(primPyr.getName().toUpperCase()));

        logger.info("Make sure New Statement File contains correctedBillType="+correctedBillType);
        Assert.assertTrue(textStatement.contains(correctedBillType.substring(0,3)));

        List<AccnProc> accnProcs=rpmDao.getAccnProcsByAccnId(testDb, accnId);
        for(AccnProc accnProc:accnProcs)
        {
            logger.info("Make sure New Statement File contains accnProc=");
            logger.info("Make sure New Statement File contains accnProc.getProcId=" + accnProc.getProcId()+", due amount="+accnProc.getDueAmtAsMoney());
            Assert.assertTrue(textStatement.contains(accnProc.getProcId()));
            Assert.assertTrue(textStatement.contains(String.valueOf(accnProc.getDueAmtAsMoney()).replace(".", " ")));
        }
        logger.info("Make sure New Statement File contains fac="+fac);
        Assert.assertTrue(textStatement.contains(fac));

    }

    @Test(alwaysRun = true, description = "Generate Patient Statement")
    @Parameters({"accnId", "primPyrAbbrv", "secondPyrAbbrv", "submSvcAbbrev", "fac"})
    public void testXPR_1948(String accnId, String primPyrAbbrv, String secondPyrAbbrv, String submSvcAbbrev, String fac) throws Exception
    {
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));
        logger.info("Starting Test Case XPR_1948 - Accn-SingleStatement-Generate Patient Statement");
        logger.info("Clearing AccnPyrErrs, accnId=" + accnId);
        Pyr primPyr = rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv);
        Pyr secondPyr = rpmDao.getPyrByPyrAbbrv(testDb, secondPyrAbbrv);
        cleanQASForPyrOnAccn(accnId, secondPyr.getPyrId());
        rpmDao.deleteAccnPyrErrByAccnId(testDb, accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);

        File statementFile = submitClaimsOnSingleStatement(accnId, secondPyrAbbrv, submSvcAbbrev, 2, wait, "", true);
        Assert.assertTrue(statementFile.exists());
        logger.info("Make sure in the Patient Statement file Accession Client Specific question and Response are NOT displayed");
        String patientStatementFile = FileUtils.readFileToString(statementFile);

        List<AccnProc> accnProcs = rpmDao.getAccnProcsByAccnId(testDb, accnId);
        logger.info("Make sure New Patient Statement File contains accnId="+accnId);
        Assert.assertTrue(patientStatementFile.contains("ACCN|"+accnId));
        logger.info("Make sure New Patient Statement File contains primPyrAbbrev="+primPyr.getName());
        Assert.assertTrue(patientStatementFile.contains("PYR||"+primPyr.getName()));
        logger.info("Make sure New Patient Statement File contains patient Pyr Abbrev="+secondPyr.getName());
        Assert.assertTrue(patientStatementFile.contains("PYR||"+secondPyr.getName()));
        Accn accn = accessionDao.getAccn(accnId);
        logger.info("Make sure New Patient Statement File contains patient data, First name="+accn.getPtFNm()+", last name="+accn.getPtLNm()
                +", address 1="+accn.getPtAddr1()+ ", address 2="+accn.getPtAddr2());
        Assert.assertTrue(patientStatementFile.contains(accn.getPtFNm()));
        Assert.assertTrue(patientStatementFile.contains(accn.getPtLNm()));
        Assert.assertTrue(patientStatementFile.contains(accn.getPtAddr1()));
        Assert.assertTrue(patientStatementFile.contains(accn.getPtAddr2()));

        for (AccnProc proc : accnProcs)
        {
            logger.info("Make sure New Patient File contains procId="+proc.getProcId()+", accnProcBill="+proc.getBilPrc());
            Assert.assertTrue(patientStatementFile.contains("PROC|"+proc.getProcId()));
        }
        logger.info("Make sure New Statement File contains fac="+fac);
        Assert.assertTrue(patientStatementFile.contains(fac));

    }
    @Test(alwaysRun = true, description = "MayoPtStmtPdf - displaying logo on the Patient Statement - logo is enabled ")
    @Parameters({"accnId", "primPyrAbbrv", "submSvcAbbrev", "facAbbrev", "expectedFile"})
    public void testXPR_1969(String accnId, String primPyrAbbrv, String submSvcAbbrev, String facAbbrev, String expectedFile) throws Exception
    {
        logger.info("Starting Test Case XPR_1969 - Accn-SingleStatement-Generate MayoPtStmtPdf Patient Statement");
        logger.info("Clearing QAS for patient pyr, AccnPyrErrs, AccnProcErrs, accnId=" + accnId);
        cleanQASForPyrOnAccn(accnId, rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv).getPyrId());
        rpmDao.deleteAccnPyrErrByAccnId(testDb, accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);
        Fac fac = rpmDao.getFacByAbbrv(testDb, facAbbrev);
        logger.info("Make sure fac is not Main fac, fac typ=" + fac.getFacTypId());
        Assert.assertNotEquals(fac.getFacTypId(), 1);

        logger.info("Make sure there is system_setting_override_by_fac set with logo location, fac=" + facAbbrev);
        SystemSettingOverrideByFac systemSettingOverrideByFac = rpmDao.getSystemSettingOverrideByFacAndSystemSettingId(fac.getFacId(), SystemSettingMap.SS_PT_STMT_LOGO);
        Assert.assertEquals(systemSettingOverrideByFac.getDataValue(), "/images/ARXLogo2018.png");

        logger.info("Make sure there is system_setting_override_by_fac set with logo location for Main fac");
        SystemSettingOverrideByFac systemSettingOverrideByFacMainFac = rpmDao.getSystemSettingOverrideByFacAndSystemSettingId(1, SystemSettingMap.SS_PT_STMT_LOGO);
        Assert.assertEquals(systemSettingOverrideByFacMainFac.getDataValue(), "/images/logo_myriad.png");

        logger.info("Clearing AccnPyrErrs, accnId=" + accnId);
        Pyr primPyr = rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv);
        cleanQASForPyrOnAccn(accnId, primPyr.getPyrId());
        rpmDao.deleteAccnPyrErrByAccnId(testDb, accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);

        File statementFile = submitClaimsOnSingleStatement(accnId, primPyrAbbrv, submSvcAbbrev, 1, wait, "", true);
        Assert.assertTrue(statementFile.exists());

        logger.info("Make sure in the Patient Statement file's Logo is the same as expected Image");
        File actualImageFile = extractImageFromPdf(statementFile);
        File expectedImageFile = new File(getClass().getResource("/mars/testFiles/" + expectedFile).getFile());

        Assert.assertTrue(FileUtils.contentEquals(actualImageFile, expectedImageFile));
    }

    @Test(alwaysRun = true, description = "GenericPtStmt - displaying logo on the Patient Statement - logo is enabled - SYSTEM_SETTING_OVERRIDE_BY_FAC and SS 1523 - Not Main Fac")
    @Parameters({"accnId", "primPyrAbbrv", "submSvcAbbrev", "facAbbrev", "expectedFile"})
    public void testXPR_1970(String accnId, String primPyrAbbrv, String submSvcAbbrev, String facAbbrev, String expectedFile) throws Exception
    {
        logger.info("Starting Test Case XPR_1970 - GenericPtStmt - displaying logo on the Patient Statement - logo is enabled - SYSTEM_SETTING_OVERRIDE_BY_FAC and SS 1523 - Not Main Fac");
        logger.info("Clearing QAS for patient pyr, AccnPyrErrs, AccnProcErrs, accnId=" + accnId);
        cleanQASForPyrOnAccn(accnId, rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv).getPyrId());
        rpmDao.deleteAccnPyrErrByAccnId(testDb, accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);

        logger.info("Make sure there is system_setting_override_by_fac set with logo location, fac=" + facAbbrev);
        Fac fac = rpmDao.getFacByAbbrv(testDb, facAbbrev);
        SystemSettingOverrideByFac systemSettingOverrideByFac = rpmDao.getSystemSettingOverrideByFacAndSystemSettingId(fac.getFacId(), SystemSettingMap.SS_PT_STMT_LOGO);
        Assert.assertEquals(systemSettingOverrideByFac.getDataValue(), "/images/ARXLogo2018.png");

        logger.info("Make sure fac is not Main fac, fac typ=" + fac.getFacTypId());
        Assert.assertNotEquals(fac.getFacTypId(), 1);
        File statementFile = submitClaimsOnSingleStatement(accnId, primPyrAbbrv, submSvcAbbrev, 1, wait, "", true);
        Assert.assertTrue(statementFile.exists());

        logger.info("Make sure in the Patient Statement file's Logo is the same as expected Image");
        File actualImageFile = extractImageFromPdf(statementFile);
        File expectedImageFile = new File(getClass().getResource("/mars/testFiles/" + expectedFile).getFile());

        Assert.assertTrue(FileUtils.contentEquals(actualImageFile, expectedImageFile));
    }

    @Test(alwaysRun = true, description = "PDFPTLTR - displaying Main Fac logo (SS1523) on the Patient Statement - NO SYSTEM_SETTING_OVERRIDE_BY_FAC")
    @Parameters({"accnId", "primPyrAbbrv", "submSvcAbbrev", "expectedFile", "facAbbrev"})
    public void testXPR_1971(String accnId, String primPyrAbbrv,  String submSvcAbbrev, String expectedFile, String facAbbrev) throws Exception
    {
        logger.info("Starting Test Case XPR_1971 - Accn-SingleStatement-displaying Main Fac logo (SS1523) on the Patient Statement - NO SYSTEM_SETTING_OVERRIDE_BY_FAC");
        logger.info("Clearing QAS for patient pyr, AccnPyrErrs, AccnProcErrs, accnId=" + accnId);
        cleanQASForPyrOnAccn(accnId, rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv).getPyrId());
        rpmDao.deleteAccnPyrErrByAccnId(testDb, accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);

        logger.info("Make sure there is system_setting_override_by_fac set with logo location, fac=" + facAbbrev);
        Fac fac = rpmDao.getFacByAbbrv(testDb, facAbbrev);
        SystemSettingOverrideByFac systemSettingOverrideByFac = rpmDao.getSystemSettingOverrideByFacAndSystemSettingId(fac.getFacId(), SystemSettingMap.SS_PT_STMT_LOGO);
        Assert.assertNull(systemSettingOverrideByFac);

        logger.info("Make sure fac is not Main fac, fac typ=" + fac.getFacTypId());
        Assert.assertNotEquals(fac.getFacTypId(), 1);

        File statementFile = submitClaimsOnSingleStatement(accnId, primPyrAbbrv, submSvcAbbrev, 1, wait, "", true);
        Assert.assertTrue(statementFile.exists());
        logger.info("Make sure in the Patient Statement file's Logo is the same as expected Image");
        File actualImageFile = extractImageFromPdf(statementFile);
        File expectedImageFile = new File(getClass().getResource("/mars/testFiles/" + expectedFile).getFile());

        Assert.assertTrue(FileUtils.contentEquals(actualImageFile, expectedImageFile));

    }

    @Test(alwaysRun = true, description = "Generate Cms1500, Box 31 is displaying PYR.HCFA_SIGNATURE")
    @Parameters({"accnId", "primPyrAbbrv", "submSvcAbbrev", "fac"})
    public void testXPR_2045(String accnId, String primPyrAbbrv, String submSvcAbbrev, String fac) throws Exception
    {
        Timestamp startTime = new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));
        logger.info("Starting Test Case XPR_2045 - Accn-SingleStatement-Generate Cms1500, Box 31 is displaying signature from PYR.HCFA_SIGNATURE");
        logger.info("Clearing AccnPyrErrs, accnId=" + accnId);
        Pyr primPyr = rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv);

        logger.info("Make sure there is no statement config 110025");
        Assert.assertNull(rpmDao.getStatementConfigStmtDataByPyrId(testDb, rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv).getPyrId(), 110025));

        logger.info("Make sure PYR has a signature saved in PYR.HCFA_SIGNATURE, primPyrAbbrv=" + primPyr+" hcfaSignature="+primPyr.getHcfaSignature());
        Assert.assertNotNull(primPyr.getHcfaSignature());

        cleanQASForPyrOnAccn(accnId, primPyr.getPyrId());
        rpmDao.deleteAccnPyrErrByAccnId(testDb, accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);

        File statementFile = submitClaimsOnSingleStatement(accnId, primPyrAbbrv, submSvcAbbrev, 1, wait, "", true);
        String textStatement = convertPdfToText(statementFile);

        logger.info("Make sure New Statement File contains pyr's signature="+primPyr.getHcfaSignature()+" accnId="+accnId+" fac="+fac);
        Assert.assertTrue(textStatement.contains(accnId.substring(0,23)));
        Assert.assertTrue(textStatement.contains(primPyr.getHcfaSignature()));
        Assert.assertTrue(textStatement.contains(fac));

    }

    @Test(alwaysRun = true, description = "XP Accession Single Statement With No Submit to Payor Checked")
    @Parameters({"accnId", "primPyrAbbrv", "submSvcAbbrev"})
    public void testSingleStatementWithNoSubmitToPayor(String accnId, String primPyrAbbrv, String submSvcAbbrev) throws Exception
    {
        logger.info("Starting Test Case testSingleStatementWithNoSubmitToPayor, submSvcAbbrev="+submSvcAbbrev+", accnId="+accnId);
        Pyr primPyr = rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv);

        cleanQASForPyrOnAccnExceptDocSeqIds(accnId, primPyr.getPyrId(), Collections.emptyList());
        rpmDao.deleteAccnPyrErrByAccnId(testDb, accnId);
        rpmDao.deleteAccnProcErrByAccnId(testDb, accnId);

        File statementFile = submitClaimsOnSingleStatement(accnId, primPyrAbbrv, submSvcAbbrev, 1, wait, "", false);
        Assert.assertNull(statementFile, "No statement file expected");
    }

    public File submitClaimsOnSingleStatement(String accnId, String pyrAbbrv, String submSvcAbbrev, int submissionNum, WebDriverWait wait, String institBillType, boolean isSubmitToPayor) throws Exception
    {
        LOG.info("Load Accn on Accn Single Statement, accnId="+accnId);
        singleStatement = new SingleStatement(driver, wait, methodName);
        accessionDetail = new AccessionDetail(driver, config, wait);
        Accn accn = accessionDao.getAccn(accnId);
        if (accn.getStaId() != AccnStatusMap.ACCN_STATUS_PRICED){
            Assert.assertTrue(isOutOfPricingQueue(accnId, QUEUE_WAIT_TIME_MS * 2));
            accn = accessionDao.getAccn(accnId);
            LOG.info("Waiting for accn to be priced, accnId="+accnId);
            Assert.assertEquals(accn.getStaId(), AccnStatusMap.ACCN_STATUS_PRICED);
        }

        accessionDetail.setAccnId(accnId, wait);
        LOG.info("Wait Accn to load, accnId="+accnId);
        boolean isAccnLoaded = singleStatement.isAccnLoaded(accnId, wait);
        if (!isAccnLoaded)
        {
            LOG.info("Accession was not loaded on Single Statement after first attempt, accnId="+accnId);
            driver.navigate().refresh();
            accessionDetail.setAccnId(accnId, wait);
            isAccnLoaded = singleStatement.isAccnLoaded(accnId, wait);
        }
        Assert.assertTrue(isAccnLoaded, "Unable to load accn on Single Statement, accnId="+accnId);

        if(submissionNum>1)
        {
            LOG.info("Clicking current billed payor row, accnId="+accnId+", submissionNum="+submissionNum);
            singleStatement.clickCurrentBilledPayorsRow(submissionNum+1);
        }
        LOG.info("Select submission svc in Statement Type drop down, accnId="+accnId+", submissionNum="+submissionNum+", submSvcAbbrev="+submSvcAbbrev);
        singleStatement.setSubmSvcInStatementType(submissionNum, submSvcAbbrev);

        if (!StringUtils.isEmpty(institBillType))
        {
            LOG.info("Selecting institutional bill type, accnId="+accnId+", institBillType="+institBillType);
            String institutionalBillTypeSelection = singleStatement.setClaimTypeDropDown(institBillType);
            LOG.info("Selected institutional bill type, accnId="+accnId+", institBillType="+institutionalBillTypeSelection);
        }

        if (isSubmitToPayor)
        {
            LOG.info("Clicking submit to payor, accnId=" + accnId);
            Assert.assertTrue(singleStatement.setStatementToSpecPyr(), "Submit to Payor checkbox is not checked");
        }

        LOG.info("Click Save and Clear, accnId="+accnId);
        wait.until(ExpectedConditions.elementToBeClickable(singleStatement.footerSaveBtn()));
        singleStatement.footerSaveBtn().sendKeys(Keys.ALT + "S");
        //singleStatement.footerSaveBtn().click();

        logger.info("Click Reset");
        wait.until(ExpectedConditions.elementToBeClickable(singleStatement.statementToSpecPyrCheckBox()));
        wait.until(ExpectedConditions.elementToBeClickable(singleStatement.footerResetBtn()));
        LOG.info("Waiting for invisibility of progress pane");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("xf_message_pane")));

        String statementUrl = "/accession/accnsinglestatement.html/getstatementcontent";
        String mainPage = switchToPopupWinWithUrl(statementUrl);
        Assert.assertTrue(StringUtils.contains(driver.getCurrentUrl(), statementUrl), "Single Statement popup did not display as expected, currentUrl=" + driver.getCurrentUrl());
        driver.close();
        switchToParentWin(mainPage);

        singleStatement.footerResetBtn().sendKeys(Keys.ALT + "R");
        wait.until(ExpectedConditions.elementToBeClickable(singleStatement.warningPopupResetBtn()));
        singleStatement.warningPopupResetBtn().click();

        int submSvcSeqId =  rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).getSubmSvcSeqId();
        int pyrId = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbrv).getPyrId();
        QAccnSubm newQAccnSubm;
        try
        {
            newQAccnSubm = rpmDao.getQAccnSubmByAccnIdAndPyrIdAndSubmSvcId(testDb, accnId, pyrId, submSvcSeqId);
        }
        catch (XifinDataNotFoundException e)
        {
            newQAccnSubm = null;
        }

        File file = null;
        if (isSubmitToPayor)
        {
            file = verifyDataWithSubmitToPayor(newQAccnSubm, accnId, institBillType);
        }
        else
        {
            verifyDataNoSubmitToPayor(newQAccnSubm, accnId);
        }

        return file;
    }

    private File verifyDataWithSubmitToPayor(QAccnSubm newQAccnSubm, String accnId, String institBillType) throws Exception
    {
        // Verify the QAS record
        Assert.assertNotNull(newQAccnSubm);

        // Verify the SubmClaimAudit record
        if (newQAccnSubm.getPyrGrpId() != PyrGrpMap.PT_PYR_GRP)
        {
            logger.info("Make sure subm_claim_audit record is created for 837, accnId="+accnId+", docSeqId="+newQAccnSubm.getDocSeqId());
            SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(testDb, newQAccnSubm.getDocSeqId());
            Assert.assertNotNull(submClaimAudit, "SubmClaimAudit should not be null, accnId="+accnId+", docSeqId="+newQAccnSubm.getDocSeqId());
        }

        // Verify SubmFile record
        LOG.info("Make sure subm_file record is proccessed for submFileSeqId=" + newQAccnSubm.getSubmFileSeqId());
        SubmFile submFile = rpmDao.getSubmFile(testDb, newQAccnSubm.getSubmFileSeqId());
        Assert.assertTrue(submFile.getIsEgateProcessed(), "SubmFile should be marked as E-Gate Processed, submFileSeqId="+submFile.getSubmFileSeqId());
        Assert.assertTrue(DateUtils.isSameDay(submFile.getFileCreatDt(), new Date()), "SubmFile File Create Date should be today, submFileSeqId="+submFile.getSubmFileSeqId());

        // Verify SubmFileAudit records
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAuditByAccnIdAndSubmFileSeqId(null, accnId, submFile.getSubmFileSeqId());
        Assert.assertFalse(submFileAudits.isEmpty(), "SubmFileAudits should not be empty, accnId="+accnId);
        for (SubmFileAudit submFileAudit : submFileAudits)
        {
            Assert.assertTrue(StringUtils.isNotBlank(submFileAudit.getProcId()), "SubmFileAudit proc ID should not be blank, accnId="+accnId + ", submFileAuditSeqId="+submFileAudit.getSeqId());
            Assert.assertEquals(submFileAudit.getSubmFileSeqId(), submFile.getSubmFileSeqId(), "SubmFileAudit SubmFileSeqId is incorrect, accnId="+accnId + ", submFileAuditSeqId="+submFileAudit.getSeqId());
            Assert.assertEquals(submFileAudit.getDocSeqId(), newQAccnSubm.getDocSeqId(), "SubmFileAudit DocSeqId is incorrect, accnId="+accnId + ", submFileAuditSeqId="+submFileAudit.getSeqId());
        }

        // Verify claim type
        if (!StringUtils.isEmpty(institBillType))
        {
            ClaimTyp claimTyp = rpmDao.getClaimTypByAbbrev(institBillType.substring(0, 3));
            Assert.assertEquals(newQAccnSubm.getClaimTypId(), claimTyp.getClaimTypId(), "Claim type on QAS record is incorrect");
        }

        logger.info("Find New Statement File in the nonclientsubm/out dir");
        String submFileName = submFile.getFilename();
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);
        logger.info("Verify File content, submFileName " + submFileName);

        return file;
    }

    private void verifyDataNoSubmitToPayor(QAccnSubm newQAccnSubm, String accnId) throws Exception
    {
        // Verify the QAS record
        Assert.assertNull(newQAccnSubm);

        // Verify no SubmFileAudit records
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAuditByAccnId(accnId);
        Assert.assertTrue(submFileAudits.isEmpty(), "SubmFileAudits should be empty, accnId="+accnId);
    }

    public static String getBaseDir() throws IOException
    {
        String baseDir = null;

        if (StringUtils.containsIgnoreCase(OS_NAME, WINDOWS))
        {
            baseDir = File.separator + File.separator + "a3unity01-mp" + File.separator + "cnfs01";
        } else
        {
            baseDir = File.separator + "home";
        }
        return baseDir;
    }
    public String convertPdfToText(File file) throws IOException
    {
        parser = new PDFParser(new RandomAccessFile(file, "r"));
        parser.parse();
        cosDoc = parser.getDocument();
        pdDoc = new PDDocument(cosDoc);
        int pageNum = pdDoc.getNumberOfPages();
        pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(pdDoc);
        return text;
    }
//    private void navigateToSingleStatement()
//    {
//        logger.info("Navigate to Accession Single Statement");
//        HeaderNavigation headerNavigation = new HeaderNavigation(driver, config);
//        headerNavigation.accessionTab();
//        AccessionNavigation accessionNavigation = new AccessionNavigation(driver, config);
//        WebElement contentFrame = driver.findElement(By.id("content"));
//        accessionNavigation.accnSingleStatementLinkPF();
//        wait.until(ExpectedConditions.stalenessOf(contentFrame));
//        contentFrame = driver.findElement(By.id("content"));
//        driver.switchTo().frame(contentFrame);
//        wait.until(ExpectedConditions.stalenessOf(contentFrame));
//        contentFrame = driver.findElement(By.id("platformiframe"));
//        driver.switchTo().frame(contentFrame);
//    }

    public void switchToParentWindow(String currentWindow) {
        driver.switchTo().window(currentWindow);
        driver.switchTo().frame(driver.findElement(By.id("content")));
        driver.switchTo().frame(driver.findElement(By.id("platformiframe")));
    }
    
    protected boolean isOutOfPricingQueue(String accnId, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        maxTime +=  startTime;
        boolean isInQueue = accessionDao.isInPricingQueue(accnId);
        while (isInQueue && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            isInQueue = accessionDao.isInPricingQueue(accnId);
        }
        return !isInQueue;
    }

    public void cleanQASForPyrOnAccn(String accnId, int pyrId) throws Exception
    {
        logger.info("Clearing QAS for Pyr on accn, accnId=" + accnId + ", pyrId=" + pyrId);
        logger.info("Deleting Unprocessed QAccnSubm records, accnId=" + accnId);
        rpmDao.deleteUnprocessedQAccnSubmByAccnId(testDb, accnId);
        logger.info("Deleting Unprocessed SubmFile records, accnId=" + accnId);
        List<QAccnSubm> qaccnSubmList = rpmDao.getQAccnSubmByAccnIdNotOriginal(accnId);
        rpmDao.deleteUnprocessedSubmFileByQAccnSubmList(testDb, qaccnSubmList);
        logger.info("Get the number of QAS records =" + qaccnSubmList.size());
        for (QAccnSubm accnSubm : rpmDao.getQAccnSubmByAccnIdNotOriginal(accnId))
        {
            if (accnSubm.getPyrId() == pyrId)
            {
                logger.info("Deleting SubmFileAudit records, accnId=" + accnId);
                rpmDao.deleteSubmClaimAuditByDocId(testDb, accnSubm.getDocSeqId());
                if (accnSubm.getSubmFileSeqId() != 0)
                {
                    SubmFile submFile = rpmDao.getSubmFile(testDb, accnSubm.submFileSeqId);
                    try
                    {
                        logger.info("Updating AccnProc subm file records, accnId=" + accnId + ", SubmFileSeqId()=" + submFile.getSubmFileSeqId());
                        rpmDao.updateAccnProcSubmFileSeqId(testDb, submFile.getSubmFileSeqId());
                        logger.info("Updating QAS.subm_file_seq_id record to 0, accnId=" + accnId + ", SubmFileSeqId()=" + submFile.getSubmFileSeqId());
                        rpmDao.updateQASSubmFileSeqIdBySubmFileSeq(testDb, submFile.getSubmFileSeqId());
                        logger.info("Deleting SubmFileAudit records, accnId=" + accnId + ", SubmFileSeqId()=" + submFile.getSubmFileSeqId());
                        rpmDao.deleteSubmFileAuditByAccnIdAndSubmFileSeqId(testDb, accnId, submFile.getSubmFileSeqId());
                        logger.info("Deleting SubmClaimAudit records, accnId=" + accnId + ", accnSubm.getDocSeqId()()=" + accnSubm.getDocSeqId());
                        rpmDao.deleteSubmClaimAuditByDocId(testDb, accnSubm.getDocSeqId());
                        logger.info("Deleting ClaimJson records, accnId=" + accnId + ", docSeqId=" + accnSubm.getDocSeqId());
                        submissionDao.deleteClaimJsonByDocSeqId(accnSubm.getDocSeqId());
                        logger.info("Deleting SubmFile records, accnId=" + accnId + ", SubmFileSeqId()=" + submFile.getSubmFileSeqId());
                        rpmDao.deleteSubmFile(testDb, submFile.getSubmFileSeqId());
                    }
                    catch (Exception e)
                    {
                        logger.info("Exception happened deleting previous submission data");
                    }
                }
            }
            logger.info("Deleting QAS records, accnId=" + accnId + ", accnSubm.getDocSeqId()=" + accnSubm.getDocSeqId());
            rpmDao.deleteQAccnSubmByDocSeqId(testDb, accnSubm.getDocSeqId());
        }
    }

    protected void cleanQASForPyrOnAccnExceptDocSeqIds(String accnId, int pyrId, List<String> docSeqIdsStringList) throws Exception
    {
        logger.info("Clearing QAS for Pyr on accn, accnId=" + accnId + ", pyrId" + pyrId);
        List<QAccnSubm> qasToDelete = new ArrayList<>();
        for (QAccnSubm accnSubm : rpmDao.getQAccnSubm(null, accnId))
        {
            if (accnSubm.getPyrId() == pyrId && !docSeqIdsStringList.contains(String.valueOf(accnSubm.getDocSeqId())))
            {
                qasToDelete.add(accnSubm);
            }
        }
        submissionDao.deleteSubmissions(qasToDelete);
    }

    public File extractImageFromPdf(File file) throws IOException
    {
        PDDocument document = PDDocument.load(file);
        String filename = getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator;
        try
        {

            PDPageTree list = document.getPages();
            for (PDPage page : list)
            {
                PDResources pdResources = page.getResources();
                int i = 1;
                for (COSName name : pdResources.getXObjectNames())
                {
                    PDXObject o = pdResources.getXObject(name);
                    if (o instanceof PDImageXObject)
                    {
                        PDImageXObject image = (PDImageXObject) o;
                        filename = filename + image + i + ".png";
                        ImageIO.write(image.getImage(), "png", new File(filename));
                        i++;
                    }
                }
            }

        }
        catch (IOException e)
        {
            System.err.println("Exception while trying to create pdf document - " + e);
        }
        return new File(filename);
    }
}

