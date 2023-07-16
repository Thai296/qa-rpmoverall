package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnClnQ.AccnClnQ;
import com.mbasys.mars.ejb.entity.accnDiag.AccnDiag;
import com.mbasys.mars.ejb.entity.accnPhys.AccnPhys;
import com.mbasys.mars.ejb.entity.accnPmt.AccnPmt;
import com.mbasys.mars.ejb.entity.accnProc.AccnProc;
import com.mbasys.mars.ejb.entity.accnProcErr.AccnProcErr;
import com.mbasys.mars.ejb.entity.accnPyr.AccnPyr;
import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.ejb.entity.accnTest.AccnTest;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.errCd.ErrCd;
import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.ejb.entity.ptNotifLtrConfig.PtNotifLtrConfig;
import com.mbasys.mars.ejb.entity.ptNotifLtrConfigLnk.PtNotifLtrConfigLnk;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.qAccnSubm.QAccnSubm;
import com.mbasys.mars.ejb.entity.submClaimAudit.SubmClaimAudit;
import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.mbasys.mars.ejb.entity.submFileAudit.SubmFileAudit;
import com.mbasys.mars.ejb.entity.submSvc.SubmSvc;
import com.mbasys.mars.ejb.entity.systemSetting.SystemSetting;
import com.mbasys.mars.ejb.entity.systemSettingOverrideByFac.SystemSettingOverrideByFac;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.mbasys.mars.persistance.MiscMap;
import com.mbasys.mars.persistance.PatientStatementFieldMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.menu.MenuNavigation;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.util.DateConversion;
import domain.fileMaintenance.ptNotifLtrConfig.PtNotificationLetterConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Test
public class NonClientStatementEnginePatientTest extends NonClientStatementEngineTest
{
    private static final int STMT_CONFIG_NEW_LINE_REPLACE = 30032;
    private static final String NEWLINE_SYMBOL = StringEscapeUtils.escapeJava("\n");

    private static final String REGEX_PIPE_DELIMITED_ACCN_CONTENT = "(?s)\\RACCN\\|%s\\|.*?(?=(ACCN\\||PRIMFAC\\||FTS\\||$))";

    private static final SimpleDateFormat DATE_FMT_PIPE_DELIMITED_STATEMENT = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);


    @Override
    @BeforeSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword,
                            @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running BeforeSuite");
            // Disable excess Selenium logging
            //java.util.logging.Logger.getLogger("org.openqa.selenium.remote").setLevel(java.util.logging.Level.OFF);
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            logIntoSso(ssoUsername, ssoPassword);
            updatePtNotifLtrConfig();
            Assert.assertTrue(ssoDao.isCronJobSet(orgAlias, NON_CLIENT_SUBMISSION_ENGINE));
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

    @Test(priority = 1, description = "Accn client specific question - display ACCNQUEST segment with Question and Response")
    @Parameters({"accnId", "primPyrAbbrv", "secondPyrAbbrv", "subId", "submSvcAbbrev", "accnClientQuestion", "accnClientResponse", "configElementTyp"})
    public void testPFER_594(String accnId, String primPyrAbbrv, String secondPyrAbbrv, String subId, String submSvcAbbrev, String accnClientQuestion, String accnClientResponse, int configElementTyp) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-594");
        cleanUpAccn(accnId);
        logger.info("Make sure NO Accession Client Questions are saved in DB");
        List<AccnClnQ> accnClnQS = rpmDao.getAccnClnQByAccnId(testDb, accnId);
        logger.info(accnClnQS);
        if (accnClnQS.size() > 0)
        {
            rpmDao.deleteAccnClnQForAccnId(testDb, accnId);
            logger.info("DELETED " + accnClnQS);
        }
        logger.info("Make sure Accession Client Question is set up in statement config 30022");
        int submSvcSeqId = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).getSubmSvcSeqId();
        String statementConfigStmtData = rpmDao.getStatementConfigStmtDataBySubmSvcAndConfigElementTyp(testDb, submSvcSeqId, configElementTyp);
        Assert.assertTrue(statementConfigStmtData.contains(accnClientQuestion.substring(0, 2)));

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        accnClnQS = rpmDao.getAccnClnQByAccnId(testDb, accnId);
        Assert.assertEquals(accnClnQS.size(), 0);
        logger.info("Click on Add Client Specific Questions button");
        clickHiddenPageObject(accessionDetail.addClientSpecificQuestionButton(), 0);
        logger.info("Enter New Client SpecificQuestion=" + accnClientQuestion + " and Response =" + accnClientResponse);
        accessionDetail.setClientSpecificQuestion(accnClientQuestion, accnClientResponse, wait);

        logger.info("Click Save button");
        accessionDetail.clickSave(wait);

        logger.info("Make sure accn is loaded");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));
        accnClnQS = rpmDao.getAccnClnQByAccnId(testDb, accnId);
        Assert.assertEquals(accnClnQS.size(), 1);

        logger.info("Click on Submit Claim");
        clickHiddenPageObject(accessionDetail.submitClaimAddBtn(), 0);

        logger.info("Enter Claim Info for Primary Pyr with Condition Code into Submit Claim popup, primPyrAbbrv - " + primPyrAbbrv);
        accessionDetail.enterSubmitClaimInfo(secondPyrAbbrv, subId, submSvcAbbrev, "Original", "", "", wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Click Save and Clear");
        accessionDetail.accnIdText().sendKeys(Keys.ALT + "S");
        wait.until(ExpectedConditions.textToBePresentInElement(accessionDetail.accessionDetailTitle(), "Detail"));

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm cvs to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date set, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        List<AccnPyrErr> newAccnPyrErr = accessionDao.getAccnPyrErrsByAccnId(accnId);
        Assert.assertEquals(newAccnPyrErr.size(), 0);

        logger.info("Make sure Patient Statement file is saved to the dir");
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + processedSubmFile.getFilename());

        logger.info("Make sure in the Patient Statement file Accession CLient Specific question and Response are displayed");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data = null;
        String line = null;
        while ((line = bufferedReader.readLine()) != null)
        {
            data = data + line;
        }
        logger.info("ACCNCSQ|" + accnClientQuestion.substring(0, 2) + "|" + accnClientResponse);
        Assert.assertTrue(data.contains("ACCNCSQ|" + accnClientQuestion.substring(0, 2) + "|" + accnClientResponse));

        logger.info("Make sure Subm File Audit records do have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }

        logger.info("Make sure DOB suppressed");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(null, qasList.get(0).getDocSeqId());
        Assert.assertEquals(submClaimAudit.getSuppressedFlds(), String.valueOf(PatientStatementFieldMap.PT_DOB), "Expected DOB to be suppressed");
    }

    @Test(priority = 1, description = "Accn client specific questions - display 2 ACCNQUEST segments with Question and Response")
    @Parameters({"accnId", "primPyrAbbrv", "secondPyrAbbrv", "subId", "submSvcAbbrev", "accnClientQuestion1", "accnClientResponse1", "accnClientQuestion2", "accnClientResponse2", "configElementTyp"})
    public void testPFER_595(String accnId, String primPyrAbbrv, String secondPyrAbbrv, String subId, String submSvcAbbrev, String accnClientQuestion1, String accnClientResponse1, String accnClientQuestion2, String accnClientResponse2, int configElementTyp) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-595");
        cleanUpAccn(accnId);
        logger.info("Make sure NO Accession Client Questions are saved in DB");
        List<AccnClnQ> accnClnQS = rpmDao.getAccnClnQByAccnId(testDb, accnId);
        logger.info(accnClnQS);
        if (accnClnQS.size() > 0)
        {
            rpmDao.deleteAccnClnQForAccnId(testDb, accnId);
            logger.info("DELETED " + accnClnQS);
        }
        logger.info("Make sure Accession Client Question is set up in statement config 30022");

        int submSvcSeqId = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).getSubmSvcSeqId();
        String statementConfigStmtData = rpmDao.getStatementConfigStmtDataBySubmSvcAndConfigElementTyp(testDb, submSvcSeqId, configElementTyp);
        Assert.assertTrue(statementConfigStmtData.contains(accnClientQuestion1.substring(0, 2)));
        Assert.assertTrue(statementConfigStmtData.contains(accnClientQuestion2.substring(0, 2)));

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        accnClnQS = rpmDao.getAccnClnQByAccnId(testDb, accnId);
        Assert.assertEquals(accnClnQS.size(), 0);
        logger.info("Click on Add Client Specific Questions button");
        clickHiddenPageObject(accessionDetail.addClientSpecificQuestionButton(), 0);
        logger.info("Enter New Client Specific Question=" + accnClientQuestion1 + " and Response =" + accnClientResponse1);
        accessionDetail.setClientSpecificQuestion(accnClientQuestion1, accnClientResponse1, wait);

        logger.info("Make sure accn is loaded");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Click on Add Client Specific Questions button");
        clickHiddenPageObject(accessionDetail.addClientSpecificQuestionButton(), 0);
        logger.info("Enter New Client Specific Question=" + accnClientQuestion2 + " and Response =" + accnClientResponse2);
        accessionDetail.setClientSpecificQuestion(accnClientQuestion2, accnClientResponse2, wait);

        logger.info("Make sure accn is loaded");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Click on Submit Claim");
        clickHiddenPageObject(accessionDetail.submitClaimAddBtn(), 0);

        logger.info("Enter Claim Info for Primary Pyr with Condition Code into Submit Claim popup, primPyrAbbrv - " + primPyrAbbrv);
        accessionDetail.enterSubmitClaimInfo(secondPyrAbbrv, subId, submSvcAbbrev, "Original", "", "", wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Click Save and Clear");
        accessionDetail.accnIdText().sendKeys(Keys.ALT + "S");
        wait.until(ExpectedConditions.textToBePresentInElement(accessionDetail.accessionDetailTitle(), "Detail"));

        accnClnQS = rpmDao.getAccnClnQByAccnId(testDb, accnId);
        Assert.assertEquals(accnClnQS.size(), 2);
        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm cvs to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date set, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), 0);
        logger.info("Make sure Patient Statement file is saved to the dir");
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + processedSubmFile.getFilename());
        logger.info("Make sure in the Patient Statement file Accession Client Specific question and Response are NOT displayed");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data = null;
        String line;
        while ((line = bufferedReader.readLine()) != null)
        {
            data = data + line;
        }
        Assert.assertTrue(data.contains("ACCNCSQ|" + accnClientQuestion1.substring(0, 2) + "|" + accnClientResponse1));
        Assert.assertTrue(data.contains("ACCNCSQ|" + accnClientQuestion2.substring(0, 2) + "|" + accnClientResponse2));

        logger.info("Make sure Subm File Audit records do have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }

        logger.info("Make sure DOB suppressed");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(null, qasList.get(0).getDocSeqId());
        Assert.assertEquals(submClaimAudit.getSuppressedFlds(), String.valueOf(PatientStatementFieldMap.PT_DOB), "Expected DOB to be suppressed");
    }

    @Test(priority = 1, description = "Accn client specific question is not in Statement Config - DO NOT display ACCNQUEST")
    @Parameters({"accnId", "primPyrAbbrv", "secondPyrAbbrv", "subId", "submSvcAbbrev", "accnClientQuestion", "accnClientResponse", "configElementTyp"})
    public void testPFER_596(String accnId, String primPyrAbbrv, String secondPyrAbbrv, String subId, String submSvcAbbrev, String accnClientQuestion, String accnClientResponse, int configElementTyp) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-596");
        cleanUpAccn(accnId);

        logger.info("Make sure NO Accession Client Questions are saved in DB");
        List<AccnClnQ> accnClnQS = rpmDao.getAccnClnQByAccnId(testDb, accnId);
        logger.info(accnClnQS);
        if (accnClnQS.size() > 0)
        {
            rpmDao.deleteAccnClnQForAccnId(testDb, accnId);
            logger.info("DELETED " + accnClnQS);
        }

        logger.info("Make sure Accession Client Question is set up in statement config 30022");
        int submSvcSeqId = (rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).getSubmSvcSeqId());
        String statementConfigStmtData = rpmDao.getStatementConfigStmtDataBySubmSvcAndConfigElementTyp(testDb, submSvcSeqId, configElementTyp);
        Assert.assertFalse(statementConfigStmtData.contains(accnClientQuestion.substring(0, 2)));

        accessionDetail.loadAccnOnAccnDetail(wait, accnId);

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        accnClnQS = rpmDao.getAccnClnQByAccnId(testDb, accnId);
        Assert.assertEquals(accnClnQS.size(), 0);
        logger.info("Click on Add Client Specific Questions button");
        clickHiddenPageObject(accessionDetail.addClientSpecificQuestionButton(), 0);
        logger.info("Enter New Client Question=" + accnClientQuestion + " and Response =" + accnClientResponse);
        accessionDetail.setClientSpecificQuestion(accnClientQuestion, accnClientResponse, wait);

        logger.info("Make sure accn is loaded");
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Click on Submit Claim");
        clickHiddenPageObject(accessionDetail.submitClaimAddBtn(), 0);

        logger.info("Enter Claim Info for Primary Pyr with Condition Code into Submit Claim popup, primPyrAbbrv - " + primPyrAbbrv);
        accessionDetail.enterSubmitClaimInfo(secondPyrAbbrv, subId, submSvcAbbrev, "Original", "", "", wait);
        Assert.assertTrue(accessionDetail.isAccnLoaded(accnId, wait));

        logger.info("Click Save and Clear");
        accessionDetail.accnIdText().sendKeys(Keys.ALT + "S");
        wait.until(ExpectedConditions.textToBePresentInElement(accessionDetail.accessionDetailTitle(), "Detail"));

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm cvs to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date set, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), 0);
        logger.info("Make sure Patient Statement file is saved to the dir");
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + processedSubmFile.getFilename());
        Assert.assertTrue(file.exists(), "Expected file, filename=" + processedSubmFile.getFilename());
        logger.info("Make sure in the Patient Statement file Accession Client Specific question and Response are NOT displayed");
        String patientStatementFile = FileUtils.readFileToString(file);

        Assert.assertTrue(patientStatementFile.contains(accnId));
        Assert.assertFalse(patientStatementFile.contains("ACCNCSQ|" + accnClientQuestion.substring(0, 1) + "|" + accnClientResponse));

        logger.info("Verify accession prints with payments at the same date at proc & accn level for adjustment & payment");
        Assert.assertTrue(patientStatementFile.contains("PROCPMT|20180808|MEDICARE PART A -Palmetto||10||0|T"));
        Assert.assertTrue(patientStatementFile.contains("PROCPMT|20180808|MEDICARE PART A -Palmetto||10||0|T"));
        Assert.assertTrue(patientStatementFile.contains("PROCADJ|20180808|MEDICARE PART A -Palmetto|40|TEST ADJUSTENT||0|T"));
        Assert.assertTrue(patientStatementFile.contains("PROCADJ|20180808|MEDICARE PART A -Palmetto|-10|TEST ADJUSTENT||0|T"));
        Assert.assertTrue(patientStatementFile.contains("ACCNPMT|20180808|MEDICARE PART A -Palmetto||10||0|T"));
        Assert.assertTrue(patientStatementFile.contains("ACCNADJ|20180808|MEDICARE PART A -Palmetto|-10|TEST ADJUSTENT||0|T"));

        logger.info("Make sure Subm File Audit records do have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }

        logger.info("Make sure DOB suppressed");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(null, qasList.get(0).getDocSeqId());
        Assert.assertEquals(submClaimAudit.getSuppressedFlds(), String.valueOf(PatientStatementFieldMap.PT_DOB), "Expected DOB to be suppressed");
    }

    @Test(priority = 1, description = "Same-day accn level payments and adjustments are correctly displayed and group together")
    @Parameters({"accnId", "primPyrAbbrv", "secondPyrAbbrv", "subId", "submSvcAbbrev",})
    public void testPFER_607(String accnId, String primPyrAbbrv, String secondPyrAbbrv, String subId, String submSvcAbbrev) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-607");
        cleanUpAccn(accnId);
        logger.info("Make sure there are multiple Accession payments are saved in DB");
        List<AccnPmt> accnPmts = rpmDao.getAccnPmtsByAccnId(testDb, accnId);
        Assert.assertTrue(accnPmts.size() > 2);
        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        int submSvcSeqId = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).getSubmSvcSeqId();
        accessionDetail.submitClaimsOnAccnDetail(accnId, secondPyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process subm file, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date set, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), 0);
        logger.info("Make sure Patient Statement file is saved to the dir");
        String submFileName = processedSubmFile.getFilename();
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);
        logger.info("Make sure in the Patient Statement file Accn Payments and Adjustments are present");
        String patientStatementFile = FileUtils.readFileToString(file);
        Assert.assertTrue(patientStatementFile.contains(accnId));
        Assert.assertTrue(patientStatementFile.contains("ACCNPMT|20180802|United Health Care||50||0|T"));
        Assert.assertTrue(patientStatementFile.contains("ACCNPMT|20180802|United Health Care||75||0|T"));
        Assert.assertTrue(patientStatementFile.contains("ACCNPMT|20180802|United Health Care||100||0|T"));
        Assert.assertTrue(patientStatementFile.contains("ACCNADJ|20180802|United Health Care|100|COLLECTIONS|Adj 1|0|T"));
        Assert.assertTrue(patientStatementFile.contains("ACCNADJ|20180802|United Health Care|200|COLLECTIONS|Adj 2|0|T"));
        Assert.assertTrue(patientStatementFile.contains("ACCNADJ|20180802|United Health Care|-500|COLLECTIONS|Adj 3|0|T"));

        logger.info("Make sure Subm File Audit records do have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }

        logger.info("Make sure DOB suppressed");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(null, qasList.get(0).getDocSeqId());
        Assert.assertEquals(submClaimAudit.getSuppressedFlds(), String.valueOf(PatientStatementFieldMap.PT_DOB), "Expected DOB to be suppressed");
    }

    @Test(priority = 1, description = "Patient Statement PDF should not print CC Box when stmt_config is on")
    @Parameters({"accnId", "primPyrAbbrv", "secondPyrAbbrv", "subId", "submSvcAbbrev"})
    public void testPFER_651(String accnId, String primPyrAbbrv, String secondPyrAbbrv, String subId, String submSvcAbbrev) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-651");
        cleanUpAccn(accnId);

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        accessionDetail.submitClaimsOnAccnDetail(accnId, secondPyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process subm file, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date set, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), 0);
        logger.info("Make sure Patient Statement file is saved to the dir");
        String submFileName = processedSubmFile.getFilename();
        String fullPath = getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName;

        logger.info("Make sure in the Patient Statement file accession is present");
        File file = new File(fullPath);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);
        String pdfContentsStr1 = convertPdfToText(file);
        String newPDFContentsStr = pdfContentsStr1.replaceAll("[\n\r]", "").replaceAll(" ", "").trim();
        Assert.assertTrue(newPDFContentsStr.contains(accnId), "        Patient PDF file " + submFileName + " should contain '" + accnId + "' in PDF.");

        logger.info("Make sure in the Patient Statement file CC box is not present");
        /*
           String contents of CC box: "VISA", "MASTERCARD", "AMERICAN EXPRESS", "CARD NUMBER", "SIGNATURE", "EXP. DATE", "STATEMENT DATE", "PAY THIS AMOUNT", "ACCT #", "SHOW AMOUNT", "PAID HERE"
         */
        Assert.assertFalse(newPDFContentsStr.contains("CARDNUMBER"), "        Patient PDF file " + submFileName + " should not contain CC box in PDF.");
        Assert.assertFalse(newPDFContentsStr.contains("AMERICANEXPRESS"));

        logger.info("Make sure Subm File Audit records do have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }

        logger.info("Make sure DOB suppressed");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(null, qasList.get(0).getDocSeqId());
        Assert.assertTrue(StringUtils.contains(submClaimAudit.getSuppressedFlds(), String.valueOf(PatientStatementFieldMap.PT_DOB)), "Expected DOB to be suppressed");
    }

    @Test(priority = 1, description = "Patient Statement PDF should print CC Box when stmt_config is disabled")
    @Parameters({"accnId", "primPyrAbbrv", "secondPyrAbbrv", "subId", "submSvcAbbrev"})
    public void testPFER_652(String accnId, String primPyrAbbrv, String secondPyrAbbrv, String subId, String submSvcAbbrev) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-652");
        cleanUpAccn(accnId);

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());
        accessionDetail.submitClaimsOnAccnDetail(accnId, secondPyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process subm file, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date set, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), 0);
        logger.info("Make sure Patient Statement file is saved to the dir");
        String submFileName = processedSubmFile.getFilename();
        String fullPath = getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName;

        logger.info("Make sure in the Patient Statement file accession is present");
        File file = new File(fullPath);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);
        String pdfContentsStr1 = convertPdfToText(file);
        String newPDFContentsStr = pdfContentsStr1.replaceAll("[\n\r]", "").replaceAll(" ", "").trim();
        Assert.assertTrue(newPDFContentsStr.contains(accnId), "        Patient PDF file " + submFileName + " should contain '" + accnId + "' in PDF.");

        logger.info("Make sure in the Patient Statement file CC box is not present");
        /*
           String contents of CC box: "VISA", "MASTERCARD", "AMERICAN EXPRESS", "CARD NUMBER", "SIGNATURE", "EXP. DATE", "STATEMENT DATE", "PAY THIS AMOUNT", "ACCT #", "SHOW AMOUNT", "PAID HERE"
         */
        Assert.assertTrue(newPDFContentsStr.contains("CARDNUMBER"), "        Patient PDF file " + submFileName + " should not contain CC box in PDF.");
        Assert.assertTrue(newPDFContentsStr.contains("AMERICANEXPRESS"));

        logger.info("Make sure Subm File Audit records do have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }

        logger.info("Make sure DOB suppressed");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(null, qasList.get(0).getDocSeqId());
        Assert.assertTrue(StringUtils.contains(submClaimAudit.getSuppressedFlds(), String.valueOf(PatientStatementFieldMap.PT_DOB)), "Expected DOB to be suppressed");
    }

    @Test(priority = 1, description = "Patient Statement Letter Segment - Payor group incl, client incl, test incl prints 3 letters IDs")
    @Parameters({"accnId", "primPyrAbbrv", "subId", "submSvcAbbrev", "letterId1", "letterId2", "letterId3", "testAbbrev"})
    public void testPFER_637(String accnId, String primPyrAbbrv, String subId, String submSvcAbbrev, String letterId1, String letterId2, String letterId3, String testAbbrev) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-637");
        cleanUpAccn(accnId);
        Pyr primPyr = rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv);

        logger.info("Check for accession client");
        Accn accn = accessionDao.getAccn(accnId);

        logger.info("Check for accession test");
        List<AccnTest> accnTests = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev);
        int accnTestId = 0;
        if (accnTests.size() > 0)
        {
            accnTestId = accnTests.get(0).getTestId();
        }
        Assert.assertTrue(accnTestId > 0);
        // Check that the accession has the Test

        logger.info("Add the Patient Notification Letter Configurations we need for pyrGrp=" + primPyr.getPyrGrpId() + ", clnId=" + accn.getClnId() + ", testId=" + accnTestId);
        PtNotificationLetterConfig ptNotificationLetterConfig1 = new PtNotificationLetterConfig();
        ptNotificationLetterConfig1.setLetterId(letterId1);
        ptNotificationLetterConfig1.setSeqId(1);
        ptNotificationLetterConfig1.addLink(MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_PYR_GRP_INCL, String.valueOf(primPyr.getPyrGrpId()));
        savePtNotificationLetterConfig(ptNotificationLetterConfig1);

        PtNotificationLetterConfig ptNotificationLetterConfig2 = new PtNotificationLetterConfig();
        ptNotificationLetterConfig2.setLetterId(letterId2);
        ptNotificationLetterConfig2.setSeqId(2);
        ptNotificationLetterConfig2.addLink(MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_CLN_INCL, String.valueOf(accn.getClnId()));
        savePtNotificationLetterConfig(ptNotificationLetterConfig2);

        PtNotificationLetterConfig ptNotificationLetterConfig3 = new PtNotificationLetterConfig();
        ptNotificationLetterConfig3.setLetterId(letterId3);
        ptNotificationLetterConfig3.setSeqId(3);
        ptNotificationLetterConfig3.addLink(MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_TEST_INCL, String.valueOf(accnTestId));
        savePtNotificationLetterConfig(ptNotificationLetterConfig3);

        logger.info("Check for persisted Patient Notification Letter Configs");
        Assert.assertTrue(rpmDao.getPtNotifLtrConfigs(testDb).size() > 0);
        accessionDetail.submitClaimsOnAccnDetail(accnId, primPyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm cvs to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).getSubmSvcSeqId());

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process subm file, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date set, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), 0);

        logger.info("Make sure Patient Notification file is saved to the dir");
        String submFileName = processedSubmFile.getFilename();
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);
        logger.info("Make sure in the Letter IDs are displayed");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String data = null;
        String line;
        while ((line = bufferedReader.readLine()) != null)
        {
            data = data + line;
        }

        Assert.assertTrue(data.matches(".+[,|]" + letterId1 + ".*"));
        Assert.assertTrue(data.matches(".+[,|]" + letterId2 + ".*"));
        Assert.assertTrue(data.matches(".+[,|]" + letterId3 + ".*"));

        logger.info("Make sure Subm File Audit records do have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "Patient Notification Letter Segment - Payor excl, test type excl, client accnt typ excl, taxonomy ex")
    @Parameters({"accnId", "primPyrAbbrv", "subId", "submSvcAbbrev", "letterId4", "letterId5", "letterId6", "letterId7", "testAbbrev"})
    public void testPFER_638(String accnId, String primPyrAbbrv, String subId, String submSvcAbbrev, String letterId4, String letterId5, String letterId6, String letterId7, String testAbbrev) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        navigation = new MenuNavigation(driver, config);
        logger.info("Running testPFER-638");
        cleanUpAccn(accnId);
        Pyr primPyr = rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv);

        logger.info("Check for accession client");
        Accn accn = accessionDao.getAccn(accnId);

        logger.info("Check for accession test");
        List<AccnTest> accnTests = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev);
        Assert.assertTrue(accnTests.size() > 0);
        com.mbasys.mars.ejb.entity.test.Test test = rpmDao.getTestByTestId(testDb, accnTests.get(0).getTestId());
        int testTypId = test.getTestTypId();

        Cln cln = rpmDao.getClnByClnId(testDb, accn.getClnId());

        List<AccnPhys> orderingPhysList = accessionDao.getAccnPhysByAccnIdAccnPhysTypId(accnId, String.valueOf(MiscMap.ACCN_PHYS_TYP_ORDERING));
        int orderingPhysId = !orderingPhysList.isEmpty() ? orderingPhysList.get(0).getPhysSeqId() : 0;
        String orderingTaxonomyCd = rpmDao.getPhys(testDb, orderingPhysId).getTaxonomyCd();

        logger.info("Add the Patient Notification Letter Configurations we will exclude for payorGrp=" + primPyr.getPyrGrpId() + ", clnAccntTyp=" + cln.getAcctTypId() + ", testTypId=" + testTypId);
        PtNotificationLetterConfig ptNotificationLetterConfig4 = new PtNotificationLetterConfig();
        ptNotificationLetterConfig4.setLetterId(letterId4);
        ptNotificationLetterConfig4.setSeqId(4);
        ptNotificationLetterConfig4.addLink(MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_PYR_GRP_EXCL, String.valueOf(primPyr.getPyrGrpId()));
        savePtNotificationLetterConfig(ptNotificationLetterConfig4);

        PtNotificationLetterConfig ptNotificationLetterConfig5 = new PtNotificationLetterConfig();
        ptNotificationLetterConfig5.setLetterId(letterId5);
        ptNotificationLetterConfig5.setSeqId(5);
        ptNotificationLetterConfig5.addLink(MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_TEST_TYP_EXCL, String.valueOf(testTypId));
        savePtNotificationLetterConfig(ptNotificationLetterConfig5);

        PtNotificationLetterConfig ptNotificationLetterConfig6 = new PtNotificationLetterConfig();
        ptNotificationLetterConfig6.setLetterId(letterId6);
        ptNotificationLetterConfig6.setSeqId(6);
        ptNotificationLetterConfig6.addLink(MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_CLN_ACCNT_TYP_EXCL, String.valueOf(cln.getAcctTypId()));
        savePtNotificationLetterConfig(ptNotificationLetterConfig6);

        PtNotificationLetterConfig ptNotificationLetterConfig7 = new PtNotificationLetterConfig();
        ptNotificationLetterConfig7.setLetterId(letterId7);
        ptNotificationLetterConfig7.setSeqId(7);
        ptNotificationLetterConfig7.addLink(MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_TAXONOMY_CD_EXCL, orderingTaxonomyCd);
        savePtNotificationLetterConfig(ptNotificationLetterConfig7);

        clearDataCache();
        navigation.navigateToAccnDetailPage();
        logger.info("Check for persisted Patient Notification Letter Configs");
        Assert.assertTrue(rpmDao.getPtNotifLtrConfigs(testDb).size() > 0);

        accessionDetail.submitClaimsOnAccnDetail(accnId, primPyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm cvs to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).getSubmSvcSeqId());

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process subm file, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date set, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), 0);

        logger.info("Make sure Patient Notification file is saved to the dir");
        String submFileName = processedSubmFile.getFilename();
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);
        logger.info("Make sure in the letter IDs are NOT displayed");
        String accnRecord = null;
        for (String line : FileUtils.readLines(file))
        {
            if (StringUtils.startsWith(line, accnId))
            {
                accnRecord = line;
                break;
            }
        }
        Assert.assertTrue(StringUtils.isNotBlank(accnRecord), "Patient Notification record not found, accnId="+accnId+", filename="+submFileName);
        String[] fields = StringUtils.splitPreserveAllTokens(accnRecord, "|");
        Assert.assertTrue(fields.length >= 22, "Letter ID field not found in Patient Notification record, accnId="+accnId+", filename="+submFileName);
        List<String> actualLetterIds = List.of(StringUtils.split(fields[21], ","));
        Assert.assertFalse(actualLetterIds.contains(letterId4), "Letter ID should not be contained in field 23, accnId="+accnId+", filename="+submFileName+", letterIds="+actualLetterIds+", letterId="+letterId4);
        Assert.assertFalse(actualLetterIds.contains(letterId5), "Letter ID should not be contained in field 23, accnId="+accnId+", filename="+submFileName+", letterIds="+actualLetterIds+", letterId="+letterId5);
        Assert.assertFalse(actualLetterIds.contains(letterId6), "Letter ID should not be contained in field 23, accnId="+accnId+", filename="+submFileName+", letterIds="+actualLetterIds+", letterId="+letterId6);
        Assert.assertFalse(actualLetterIds.contains(letterId7), "Letter ID should not be contained in field 23, accnId="+accnId+", filename="+submFileName+", letterIds="+actualLetterIds+", letterId="+letterId7);

        logger.info("Make sure Subm File Audit records do have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "Patient Notification Letter Segment - Payor incl, client excl, test excl, facility incl")
    @Parameters({"accnId", "primPyrAbbrv", "subId", "submSvcAbbrev", "letterId8", "letterId9"})
    public void testPFER_639(String accnId, String primPyrAbbrv, String subId, String submSvcAbbrev, String letterId8, String letterId9) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-639");
        cleanUpAccn(accnId);
        Pyr primPyr = rpmDao.getPyrByPyrAbbrv(testDb, primPyrAbbrv);

        logger.info("Check for accession client");
        Accn accn = accessionDao.getAccn(accnId);

        logger.info("Check for accession test");
        List<AccnTest> accnTests = accessionDao.getAccnTestsByAccnId(accnId);
        Assert.assertTrue(accnTests.size() > 0);
        com.mbasys.mars.ejb.entity.test.Test exclTest = null;
        for (int i = 0; i < 10; i++)
        {
            exclTest = getTestExclusion(accnTests);
            if (exclTest != null)
            {
                break;
            }
        }
        Assert.assertNotNull(exclTest);
        // Find a test the accn doesn't have

        Cln clnNotMatchAccn = rpmDao.getClnNotEqualClnId(testDb, accn.getClnId());
        Fac facNotMatchAccn = rpmDao.getFacNotEqualFacId(testDb, rpmDao.getClnByClnId(testDb, accn.getClnId()).getOrderingFacId(), MiscMap.FAC_TYP_REMOTE_AFFIL);
        Pyr pyrNotMatchAccn = rpmDao.getPyrNotEqualPyrId(testDb, primPyr.getPyrId());

        logger.info("Add the Patient Notification Letter Configurations we need for Payor Incl " + primPyr.getPyrAbbrv() + ", Client Excl " + clnNotMatchAccn.getClnAbbrev() + ", Test Excl " + exclTest.getTestAbbrev());
        PtNotificationLetterConfig ptNotificationLetterConfig8 = new PtNotificationLetterConfig();
        ptNotificationLetterConfig8.setLetterId(letterId8);
        ptNotificationLetterConfig8.setSeqId(8);
        ptNotificationLetterConfig8.addLink(MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_TEST_EXCL, String.valueOf(exclTest.getTestId()));
        ptNotificationLetterConfig8.addLink(MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_CLN_EXCL, String.valueOf(clnNotMatchAccn.getClnId()));
        savePtNotificationLetterConfig(ptNotificationLetterConfig8);

        PtNotificationLetterConfig ptNotificationLetterConfig9 = new PtNotificationLetterConfig();
        ptNotificationLetterConfig9.setLetterId(letterId9);
        ptNotificationLetterConfig9.setSeqId(9);
        ptNotificationLetterConfig9.addLink(MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_PYR_INCL, String.valueOf(pyrNotMatchAccn.getPyrId()));
        ptNotificationLetterConfig9.addLink(MiscMap.PT_NOTIF_LTR_CONFIG_LNK_TYP_CLN_PRIM_FAC_INCL, String.valueOf(facNotMatchAccn.getFacId()));
        savePtNotificationLetterConfig(ptNotificationLetterConfig9);

        logger.info("Check for persisted Patient Notification Letter Configs");
        Assert.assertTrue(rpmDao.getPtNotifLtrConfigs(testDb).size() > 0);
        accessionDetail.submitClaimsOnAccnDetail(accnId, primPyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm cvs to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).getSubmSvcSeqId());

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process subm file, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date set, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), 0);

        logger.info("Make sure Patient Notification file is saved to the dir");
        String submFileName = processedSubmFile.getFilename();
        File file = new File(getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);
        logger.info("Make sure in the correct Letter IDs are displayed");
        String accnRecord = null;
        for (String line : FileUtils.readLines(file))
        {
            if (StringUtils.startsWith(line, accnId))
            {
                accnRecord = line;
                break;
            }
        }
        Assert.assertTrue(StringUtils.isNotBlank(accnRecord), "Patient Notification record not found, accnId="+accnId+", filename="+submFileName);
        String[] fields = StringUtils.splitPreserveAllTokens(accnRecord, "|");
        Assert.assertTrue(fields.length >= 22, "Letter ID field not found in Patient Notification record, accnId="+accnId+", filename="+submFileName);
        List<String> actualLetterIds = List.of(StringUtils.split(fields[21], ","));
        Assert.assertTrue(actualLetterIds.contains(letterId8), "Letter ID should be contained in field 23, accnId="+accnId+", filename="+submFileName+", letterIds="+actualLetterIds+", letterId="+letterId8);
        Assert.assertFalse(actualLetterIds.contains(letterId9), "Letter ID should not be contained in field 23, accnId="+accnId+", filename="+submFileName+", letterIds="+actualLetterIds+", letterId="+letterId9);

        logger.info("Make sure Subm File Audit records have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }
    }

    @Test(priority = 1, description = "Patient statement - PtStmtPdf - CVV is displayed - config exists for the submission service and is set to TRUE")
    @Parameters({"accnId", "primPyrAbbrv", "subId", "submSvcAbbrev", "stmtConfig"})
    public void testPFER_665(String accnId, String primPyrAbbrv, String subId, String submSvcAbbrev, int stmtConfig) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-665");
        cleanUpAccn(accnId);

        logger.info("Make sure statement config exists, stmtConfig=" + stmtConfig + ", submSvcAbbrev=" + submSvcAbbrev);
        String stmtConfigStmtData = rpmDao.getStatementConfigStmtDataBySubmSvcAndConfigElementTyp(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).getSubmSvcSeqId(), stmtConfig);

        Assert.assertTrue(stmtConfigStmtData.contains("true"));

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        accessionDetail.submitClaimsOnAccnDetail(accnId, primPyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process subm file, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date set, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), 0);
        logger.info("Make sure Patient Statement file is saved to the dir");
        String submFileName = processedSubmFile.getFilename();
        String fullPath = getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName;

        logger.info("Make sure in the Patient Statement file accession is present");
        File file = new File(fullPath);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);
        String pdfContentsStr1 = convertPdfToText(file);
        String newPDFContentsStr = pdfContentsStr1.replaceAll("[\n\r]", ""); //.replaceAll(" ", "").trim();
        Assert.assertTrue(newPDFContentsStr.contains(accnId), "        Patient PDF file " + submFileName + " should contain '" + accnId + "' in PDF.");
        Assert.assertTrue(newPDFContentsStr.contains("CVV"), "        Patient PDF file " + submFileName + " should contain '" + "CVV" + "' in PDF.");

        logger.info("Make sure in the Patient Statement file CVV box is present");
        /*
           String contents of CC box: "VISA", "MASTERCARD", "AMERICAN EXPRESS", "CARD NUMBER", "SIGNATURE", "EXP. DATE", "STATEMENT DATE", "PAY THIS AMOUNT", "ACCT #", "SHOW AMOUNT", "PAID HERE"
         */
        Assert.assertTrue(newPDFContentsStr.contains("PAY THIS AMOUNT"));
        Assert.assertTrue(newPDFContentsStr.contains("ACCT #"));
        Assert.assertTrue(newPDFContentsStr.contains("EXP. DATE"));
        Assert.assertTrue(newPDFContentsStr.contains("AMOUNT"));
        Assert.assertTrue(newPDFContentsStr.contains("SIGNATURE"));

        logger.info("Make sure Subm File Audit records do have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }

        logger.info("Make sure DOB suppressed");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(null, qasList.get(0).getDocSeqId());
        Assert.assertTrue(StringUtils.contains(submClaimAudit.getSuppressedFlds(), String.valueOf(PatientStatementFieldMap.PT_DOB)), "Expected DOB to be suppressed");
    }


    @Test(priority = 1, description = "MayoPtStmtPdf - Main Fac is displaying logo on the Patient Statement - logo is enabled - SYSTEM_SETTING_OVERRIDE_BY_FAC and SS 1523 - Main Fac")
    @Parameters({"accnId", "patientPyrAbbrv", "subId", "submSvcAbbrev", "expectedFile", "facAbbrev"})
    public void testPFER_694(String accnId, String patientPyrAbbrv, String subId, String submSvcAbbrev, String expectedFile, String facAbbrev) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-694");
        cleanUpAccn(accnId);
        cleanQASForPyrOnAccn(accnId, rpmDao.getPyrByPyrAbbrv(testDb, patientPyrAbbrv).getPyrId());
        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());
        accessionDetail.submitClaimsOnAccnDetail(accnId, patientPyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure there is system_setting_override_by_fac set with logo location, fac=" + facAbbrev);
        Fac fac = rpmDao.getFacByAbbrv(testDb, facAbbrev);
        SystemSettingOverrideByFac systemSettingOverrideByFac = rpmDao.getSystemSettingOverrideByFacAndSystemSettingId(fac.getFacId(), SystemSettingMap.SS_PT_STMT_LOGO);
        Assert.assertEquals(systemSettingOverrideByFac.getDataValue(), "/images/ARXLogo2018.png");

        logger.info("Make sure there is system_setting_override_by_fac set with logo location for Main fac");
        SystemSettingOverrideByFac systemSettingOverrideByFacMainFac = rpmDao.getSystemSettingOverrideByFacAndSystemSettingId(1, SystemSettingMap.SS_PT_STMT_LOGO);
        Assert.assertEquals(systemSettingOverrideByFacMainFac.getDataValue(), "/images/logo_myriad.png");

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process subm file, submFileSeqId=" + submFileSeqId);
        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date set, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), 0);

        logger.info("Make sure Patient Statement file is saved to the dir");
        String submFileName = processedSubmFile.getFilename();
        String fullPath = getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName;

        logger.info("Make sure in the Patient Statement file accession is present");
        File file = new File(fullPath);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);
        String pdfContentsStr1 = convertPdfToText(file);
        String newPDFContentsStr = pdfContentsStr1.replaceAll("[\n\r]", "").replaceAll(" ", "").trim();
        Assert.assertTrue(newPDFContentsStr.contains(accnId), "Patient PDF file " + submFileName + " should contain '" + accnId + "' in PDF.");

        logger.info("Make sure in the Patient Statement file's Logo is the same as expected Image");
        List<File> actualImageFiles = extractImagesFromPdf(file, accnId);
        Assert.assertEquals(actualImageFiles.size(), 1, "Expected one image file, file="+file.getAbsolutePath()+", actualImageFiles="+actualImageFiles);
        File expectedImageFile = new File(getClass().getResource("/mars/testFiles/" + expectedFile).getFile());

        Assert.assertTrue(FileUtils.contentEquals(actualImageFiles.get(0), expectedImageFile));

        logger.info("Make sure Subm File Audit records do have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }

        logger.info("Make sure DOB suppressed");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(null, qasList.get(0).getDocSeqId());
        Assert.assertTrue(StringUtils.contains(submClaimAudit.getSuppressedFlds(), String.valueOf(PatientStatementFieldMap.PT_DOB)), "Expected DOB to be suppressed");
    }

    @Test(priority = 1, description = "GenericPtStmt - displaying logo on the Patient Statement - logo is enabled - SYSTEM_SETTING_OVERRIDE_BY_FAC and SS 1523 - Not Main Fac")
    @Parameters({"accnId", "patientPyrAbbrv", "subId", "submSvcAbbrev", "expectedFile", "facAbbrev"})
    public void testPFER_695(String accnId, String patientPyrAbbrv, String subId, String submSvcAbbrev, String expectedFile, String facAbbrev) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-695");
        cleanUpAccn(accnId);
        cleanQASForPyrOnAccn(accnId, rpmDao.getPyrByPyrAbbrv(testDb, patientPyrAbbrv).getPyrId());
        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());
        accessionDetail.submitClaimsOnAccnDetail(accnId, patientPyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure there is system_setting_override_by_fac set with logo location, fac=" + facAbbrev);
        Fac fac = rpmDao.getFacByAbbrv(testDb, facAbbrev);
        SystemSettingOverrideByFac systemSettingOverrideByFac = rpmDao.getSystemSettingOverrideByFacAndSystemSettingId(fac.getFacId(), SystemSettingMap.SS_PT_STMT_LOGO);
        Assert.assertEquals(systemSettingOverrideByFac.getDataValue(), "/images/ARXLogo2018.png");

        logger.info("Make sure fac is not Main fac, fac typ=" + fac.getFacTypId());
        Assert.assertNotEquals(fac.getFacTypId(), 1);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);

        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process subm file, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date set, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), 0);

        logger.info("Make sure Patient Statement file is saved to the dir");
        String submFileName = processedSubmFile.getFilename();
        String fullPath = getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName;

        logger.info("Make sure in the Patient Statement file accession is present");
        File file = new File(fullPath);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);
        String pdfContentsStr1 = convertPdfToText(file);
        String newPDFContentsStr = pdfContentsStr1.replaceAll("[\n\r]", "").replaceAll(" ", "").trim();
        Assert.assertTrue(newPDFContentsStr.contains(accnId), "Patient PDF file " + submFileName + " should contain '" + accnId + "' in PDF.");

        logger.info("Make sure in the Patient Statement file's Logo is the same as expected Image");
        List<File> actualImageFiles = extractImagesFromPdf(file, accnId);
        Assert.assertEquals(actualImageFiles.size(), 1, "Expected one image file, file="+file.getAbsolutePath()+", actualImageFiles="+actualImageFiles);
        File expectedImageFile = new File(getClass().getResource("/mars/testFiles/" + expectedFile).getFile());

        Assert.assertTrue(FileUtils.contentEquals(actualImageFiles.get(0), expectedImageFile));

        logger.info("Make sure Subm File Audit records do have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }

        logger.info("Make sure DOB suppressed");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(null, qasList.get(0).getDocSeqId());
        Assert.assertTrue(StringUtils.contains(submClaimAudit.getSuppressedFlds(), String.valueOf(PatientStatementFieldMap.PT_DOB)), "Expected DOB to be suppressed");
    }

    @Test(priority = 1, description = "PDFPTLTR - displaying Main Fac logo (SS1523) on the Patient Statement - NO SYSTEM_SETTING_OVERRIDE_BY_FAC")
    @Parameters({"accnId", "patientPyrAbbrv", "subId", "submSvcAbbrev", "expectedFile", "facAbbrev"})
    public void testPFER_696(String accnId, String patientPyrAbbrv, String subId, String submSvcAbbrev, String expectedFile, String facAbbrev) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-696");
        cleanUpAccn(accnId);
        cleanQASForPyrOnAccn(accnId, rpmDao.getPyrByPyrAbbrv(testDb, patientPyrAbbrv).getPyrId());

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());
        accessionDetail.submitClaimsOnAccnDetail(accnId, patientPyrAbbrv, subId, submSvcAbbrev, wait);

        Fac fac = rpmDao.getFacByAbbrv(testDb, facAbbrev);
        logger.info("Make sure fac is not Main fac, fac typ=" + fac.getFacTypId());
        Assert.assertNotEquals(fac.getFacTypId(), 1);

        logger.info("Make sure there is no SYSTEM_SETTING_OVERRIDE_BY_FAC for the Accn Clients Primary FAC =" + fac.getFacId());
        SystemSettingOverrideByFac systemSettingOverrideByFac = rpmDao.getSystemSettingOverrideByFacAndSystemSettingId(fac.getFacId(), SystemSettingMap.SS_PT_STMT_LOGO);
        Assert.assertNull(systemSettingOverrideByFac);

        logger.info("Make sure there is SYSTEM_SETTING 1523 set for default (Main) FAC");
        SystemSetting systemSetting1523 = systemDao.getSystemSetting(SystemSettingMap.SS_PT_STMT_LOGO);
        Assert.assertEquals(systemSetting1523.getDataValue(), "/images/" + expectedFile);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process subm file, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date set, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), 0);
        logger.info("Make sure Patient Statement file is saved to the dir");
        String submFileName = processedSubmFile.getFilename();
        String fullPath = getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName;

        logger.info("Make sure in the Patient Statement file accession is present");
        File file = new File(fullPath);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);
        String pdfContentsStr1 = convertPdfToText(file);
        String newPDFContentsStr = pdfContentsStr1.replaceAll("[\n\r]", "").replaceAll(" ", "").trim();
        Assert.assertTrue(newPDFContentsStr.contains(accnId), "Patient PDF file " + submFileName + " should contain '" + accnId + "' in PDF.");

        File expectedImageFile = new File(getClass().getResource("/mars/testFiles/" + expectedFile).getFile());
        logger.info("Make sure in the Patient Statement file's Logo is the same as expected Image, actualFilePath="+file.getAbsolutePath()+", expectedFilePath="+expectedImageFile.getAbsolutePath());
        List<File> actualImageFiles = extractImagesFromPdf(file, accnId);
        Assert.assertEquals(actualImageFiles.size(), 1, "Expected one image file, file="+file.getAbsolutePath()+", actualImageFiles="+actualImageFiles);

        Assert.assertTrue(FileUtils.contentEquals(actualImageFiles.get(0), expectedImageFile));

        logger.info("Make sure Subm File Audit records do have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }

        logger.info("Make sure DOB suppressed");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(null, qasList.get(0).getDocSeqId());
        Assert.assertTrue(StringUtils.contains(submClaimAudit.getSuppressedFlds(), String.valueOf(PatientStatementFieldMap.PT_DOB)), "Expected DOB to be suppressed");
    }

    @Test(priority = 1, description = "NONSTDPT - Patient Statement is generated for accn with Narrative Diag")
    @Parameters({"accnId", "patientPyrAbbrv", "subId", "submSvcAbbrev", "diag"})
    public void testPFER_697(String accnId, String patientPyrAbbrv, String subId, String submSvcAbbrev, String diag) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-697");
        cleanUpAccn(accnId);
        cleanQASForPyrOnAccn(accnId, rpmDao.getPyrByPyrAbbrv(testDb, patientPyrAbbrv).getPyrId());
        logger.info("Make sure there is narrative diagnosis on the accn, diagnosis description=" + diag);
        List<AccnDiag> accnDiags = rpmDao.getAccnDiagByAccnId(testDb, accnId);
        Assert.assertNull(accnDiags.get(accnDiags.size() - 1).getDiagCdId());
        Assert.assertEquals(accnDiags.get(accnDiags.size() - 1).getDescr(), diag);
        Assert.assertEquals(accnDiags.get(accnDiags.size() - 1).getDocId(), 1);

        SubmSvc submSvc = rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev);
        String stmtConfigNewLine = rpmDao.getStatementConfigStmtDataBySubmSvcAndConfigElementTyp(null, submSvc.getSubmSvcSeqId(), STMT_CONFIG_NEW_LINE_REPLACE);
        Assert.assertEquals(stmtConfigNewLine, NEWLINE_SYMBOL);

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        accessionDetail.submitClaimsOnAccnDetail(accnId, patientPyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, submSvc.getSubmSvcSeqId());

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);

        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process subm file, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date set, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), 0);

        logger.info("Make sure Patient Statement file is saved to the dir");
        String submFileName = processedSubmFile.getFilename();
        String fullPath = getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName;

        logger.info("Make sure in the Patient Statement file accession is present");
        File file = new File(fullPath);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);

        String patientStatementFile = FileUtils.readFileToString(file);
        logger.info("Make sure in the Patient Statement file Accn Payments and Adjustments are present");
        Assert.assertTrue(patientStatementFile.contains("ACCN|" + accnId), "Expected accession segment was not found, accnId=" + accnId);
        logger.info("Make sure in the Patient Statement diagnosis is present");
        Assert.assertTrue(patientStatementFile.contains(accnDiags.get(0).getDiagCdId()), "Expected diagnosis was not found, diagCd=" + accnDiags.get(0).getDiagCdId());
        logger.info("Make sure in the Patient Statement TEST is present");
        Assert.assertTrue(patientStatementFile.contains("TEST|"), "Expected TEST segment was not found");
        logger.info("Make sure in the Patient Statement newline symbol is present");
        Assert.assertTrue(StringEscapeUtils.escapeJava(patientStatementFile).contains(NEWLINE_SYMBOL), "Expected newline symbol for dunning message, symbol='" + NEWLINE_SYMBOL + "'");
        logger.info("Make sure in the Patient Statement does not print DOB");
        Accn accn = accessionDao.getAccn(accnId);
        Assert.assertNotNull(accn);
        String dob = DateConversion.formatYYYY_MM_DD(accn.getPtDob());
        Assert.assertFalse(patientStatementFile.contains(dob), "Expected DOB to be suppressed, dob=" + accn.getPtDobAsString());

        logger.info("Make sure Subm File Audit records do have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }

        logger.info("Make sure DOB suppressed");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(null, qasList.get(0).getDocSeqId());
        Assert.assertEquals(submClaimAudit.getSuppressedFlds(), String.valueOf(PatientStatementFieldMap.PT_DOB), "Expected DOB to be suppressed");
    }


    @Test(priority = 1, description = "All procs are in EP -> Add qas.note, accn excluded from statement")
    @Parameters({"accnId", "patientPyrAbbrv", "subId", "submSvcAbbrev", "note"})
    public void testPFER_738(String accnId, String patientPyrAbbrv, String subId, String submSvcAbbrev, String note) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-738");
        cleanUpAccn(accnId);

        logger.info("Verifying there is an open pyr error on accession, accnId=" + accnId);
        List<AccnPyrErr> accnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        Assert.assertEquals(accnPyrErrs.size(), 1);
        Assert.assertEquals(rpmDao.getErrCd(testDb, accnPyrErrs.get(0).getErrCd()).getAbbrev(), ErrorCodeMap.BAD_ADDRESS);

        logger.info("Verifying all procs on accession are in EP");
        List<AccnProc> accnProcs = rpmDao.getAccnProcsByAccnId(testDb, accnId);
        for (AccnProc accnProc : accnProcs)
        {
            Assert.assertEquals(accnProc.getStaId(), AccnStatusMap.ACCN_STATUS_PROC_EP_GENERAL, "Expected proc to be in EP, accnId=" + accnId + ", accnProcSeqId=" + accnProc.getAccnProcSeqId());
        }

        accessionDetail.submitClaimsOnAccnDetail(accnId, patientPyrAbbrv, subId, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to add error note to the claim");
        String qasNote = waitForSubmissionEngineToAddNote(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(StringUtils.isNotEmpty(qasNote));

        logger.info("Make sure 1 QAS record is still there");
        qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Make sure 1 QAS record has subm_file_seq_id = 0");
        Assert.assertEquals(qasList.get(0).getSubmFileSeqId(), 0);

        logger.info("Make sure 1 QAS record has subm_file_seq_id = 0");
        Assert.assertTrue(qasList.get(0).getNote().contains(note));

        logger.info("Make sure Subm File Audit records do have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }
    }

    @Test(description = "SS 1554 = 2, No facility bill to override - NonStandardPatientStatement  display Accn Billing Fac in PRIMFAC")
    @Parameters({"accnId", "patientPyrAbbrv", "subId", "submSvcAbbrev", "accnBillFac"})
    public void testPFER_746(String accnId, String patientPyrAbbrv, String subId, String submSvcAbbrev, String accnBillFac) throws Exception
    {
        //This test case only for Informdx builds until these special builds are merged with the rest of the logic
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-746");
        cleanUpAccn(accnId);

        logger.info("Update SS1554 to 2 to use Correspondence Facility on Patient Statements");
        updateSystemSetting(SystemSettingMap.SS_PT_STMT_OVERRIDE_MAIN_FAC_WITH_CLN_ORDERING_FAC, "Accession Billing", "2");

        logger.info("Verifying there are no open pyr errors on accession, accnId=" + accnId);
        List<AccnPyrErr> accnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        Assert.assertEquals(accnPyrErrs.size(), 0);

        logger.info("Verifying there are no open proc errors on accession, accnId=" + accnId);
        List<AccnProcErr> accnProcErrs = accessionDao.getAccnProcErrsByAccnId(accnId, false, false);
        Assert.assertEquals(accnProcErrs.size(), 0);

        logger.info("Verifying there is accn.fk_accn_billing_fac_id, accnId=" + accnId);
        Accn accn = accessionDao.getAccn(accnId);
        Fac accnFac = rpmDao.getFacByFacId(testDb, accn.getAccnBillingFacId());
        Assert.assertEquals(accnFac.getAbbrv(), accnBillFac);

        logger.info("Verifying SS1554 is set to 2");
        SystemSetting systemSetting1554 = systemDao.getSystemSetting(SystemSettingMap.SS_PT_STMT_OVERRIDE_MAIN_FAC_WITH_CLN_ORDERING_FAC);
        Assert.assertEquals(systemSetting1554.getDataValue(), "2");

        accessionDetail.submitClaimsOnAccnDetail(accnId, patientPyrAbbrv, subId, submSvcAbbrev, wait);

        clearDataCache();
        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);

        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process subm file, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date set, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), 0);

        logger.info("Make sure Patient Statement file is saved to the dir");
        String submFileName = processedSubmFile.getFilename();
        String fullPath = getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName;

        logger.info("Make sure in the Patient Statement file accession is present");
        File file = new File(fullPath);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);

        logger.info("Make sure Accession Billing Facility Name, Correspondence address, Tax Id displayed in PRIMFAC segment");
        String patientStatementFile = FileUtils.readFileToString(file);
        Assert.assertTrue(patientStatementFile.contains("PRIMFAC|Acme lab|" + accnFac.getName() + "|" + accnFac.getCorrespAddr1()));
        Assert.assertTrue(patientStatementFile.contains(accnFac.getCorrespZip()));
        Assert.assertTrue(patientStatementFile.contains(accnFac.getCorrespCity()));
        Assert.assertTrue(patientStatementFile.contains(accnFac.getCorrespPhn()));
        Assert.assertTrue(patientStatementFile.contains(rpmDao.getFacLicByFacAbbrevFacLicTypDescr(testDb, accnFac.getAbbrv(), "Tax ID").getLicId()));

        logger.info("Make sure Subm File Audit records do have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }

        logger.info("Make sure DOB suppressed");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(null, qasList.get(0).getDocSeqId());
        Assert.assertEquals(submClaimAudit.getSuppressedFlds(), String.valueOf(PatientStatementFieldMap.PT_DOB), "Expected DOB to be suppressed");
    }

    @Test(dependsOnMethods = {"testPFER_746"}, description = "SS 1554 = 0, No facility bill to override - NonStandardPatientStatement Client's Ordering Facility in PRIMFAC")
    @Parameters({"accnId", "patientPyrAbbrv", "subId", "submSvcAbbrev"})
    public void testPFER_747(String accnId, String patientPyrAbbrv, String subId, String submSvcAbbrev) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-747");
        cleanUpAccn(accnId);

        logger.info("Update SS1554 to 0 to use Client's Ordering Facility on Patient Statements");
        updateSystemSetting(SystemSettingMap.SS_PT_STMT_OVERRIDE_MAIN_FAC_WITH_CLN_ORDERING_FAC, "main", "0");

        logger.info("Verifying there are no open pyr errors on accession, accnId=" + accnId);
        List<AccnPyrErr> accnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        Assert.assertEquals(accnPyrErrs.size(), 0);

        logger.info("Verifying there are no open proc errors on accession, accnId=" + accnId);
        List<AccnProcErr> accnProcErrs = accessionDao.getAccnProcErrsByAccnId(accnId, false, false);
        Assert.assertEquals(accnProcErrs.size(), 0);

        logger.info("Verifying there is accn.fk_accn_billing_fac_id, accnId=" + accnId);
        Accn accn = accessionDao.getAccn(accnId);
        Fac accnFac = rpmDao.getFacByFacId(testDb, accn.getAccnBillingFacId());

        Cln accnCln = rpmDao.getClnByClnId(testDb, accn.getClnId());
        logger.info("Verifying there is client's ordering facility, clnId=" + accnCln);
        Fac clnOrderingFac = rpmDao.getFacByFacId(testDb, accnCln.getOrderingFacId());
        Assert.assertNotNull(clnOrderingFac);

        logger.info("Verifying SS1554 is set to 0");
        SystemSetting systemSetting1554 = systemDao.getSystemSetting(SystemSettingMap.SS_PT_STMT_OVERRIDE_MAIN_FAC_WITH_CLN_ORDERING_FAC);
        Assert.assertEquals(systemSetting1554.getDataValue(), "0");

        accessionDetail.submitClaimsOnAccnDetail(accnId, patientPyrAbbrv, subId, submSvcAbbrev, wait);

        clearDataCache();
        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);

        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process subm file, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date set, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), 0);

        logger.info("Make sure Patient Statement file is saved to the dir");
        String submFileName = processedSubmFile.getFilename();
        String fullPath = getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName;

        logger.info("Make sure in the Patient Statement file accession is present");
        File file = new File(fullPath);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);

        logger.info("Make sure Client's Ordering Facility Name, Correspondence address, Tax Id displayed in PRIMFAC segment");
        String patientStatementFile = FileUtils.readFileToString(file);
        Assert.assertTrue(patientStatementFile.contains("PRIMFAC|" + clnOrderingFac.getName() + "|" + clnOrderingFac.getName() + "|" + clnOrderingFac.getCorrespAddr1()));
        Assert.assertTrue(patientStatementFile.contains(clnOrderingFac.getCorrespZip()));
        Assert.assertTrue(patientStatementFile.contains(clnOrderingFac.getCorrespCity()));
        Assert.assertTrue(patientStatementFile.contains(clnOrderingFac.getCorrespPhn()));
        Assert.assertTrue(patientStatementFile.contains(rpmDao.getFacLicByFacAbbrevFacLicTypDescr(testDb, clnOrderingFac.getAbbrv(), "Tax ID").getLicId()));

        logger.info("Make sure Subm File Audit records do have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }

        logger.info("Make sure DOB suppressed");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(null, qasList.get(0).getDocSeqId());
        Assert.assertEquals(submClaimAudit.getSuppressedFlds(), String.valueOf(PatientStatementFieldMap.PT_DOB), "Expected DOB to be suppressed");
    }

    @Test(dependsOnMethods = {"testPFER_747"}, description = "SS 1554 = 1, No facility bill to override - NonStandardPatientStatement Client's Ordering Facility in PRIMFAC")
    @Parameters({"accnId", "patientPyrAbbrv", "subId", "submSvcAbbrev"})
    public void testPFER_748(String accnId, String patientPyrAbbrv, String subId, String submSvcAbbrev) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        logger.info("Running testPFER-748");
        cleanUpAccn(accnId);

        logger.info("Update SS1554 to 1 to use Client's Ordering Facility on Patient Statements");
        updateSystemSetting(SystemSettingMap.SS_PT_STMT_OVERRIDE_MAIN_FAC_WITH_CLN_ORDERING_FAC, "Ordering", "1");

        logger.info("Verifying there are no open pyr errors on accession, accnId=" + accnId);
        List<AccnPyrErr> accnPyrErrs = accessionDao.getAccnPyrErrsByAccnId(accnId, false, false);
        Assert.assertEquals(accnPyrErrs.size(), 0);

        logger.info("Verifying there are no open proc errors on accession, accnId=" + accnId);
        List<AccnProcErr> accnProcErrs = accessionDao.getAccnProcErrsByAccnId(accnId, false, false);
        Assert.assertEquals(accnProcErrs.size(), 0);

        logger.info("Verifying there is accn.fk_accn_billing_fac_id, accnId=" + accnId);
        Accn accn = accessionDao.getAccn(accnId);
        Fac accnFac = rpmDao.getFacByFacId(testDb, accn.getAccnBillingFacId());

        Cln accnCln = rpmDao.getClnByClnId(testDb, accn.getClnId());
        logger.info("Verifying there is client's ordering facility, clnId=" + accnCln);
        Fac clnOrderingFac = rpmDao.getFacByFacId(testDb, accnCln.getOrderingFacId());
        Assert.assertNotNull(clnOrderingFac);

        logger.info("Verifying SS1554 is set to 1");
        SystemSetting systemSetting1554 = systemDao.getSystemSetting(SystemSettingMap.SS_PT_STMT_OVERRIDE_MAIN_FAC_WITH_CLN_ORDERING_FAC);
        Assert.assertEquals(systemSetting1554.getDataValue(), "1");

        accessionDetail.submitClaimsOnAccnDetail(accnId, patientPyrAbbrv, subId, submSvcAbbrev, wait);

        clearDataCache();
        logger.info("Make sure 1 QAS record is created");
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim");
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        logger.info("Wait for Non-Client Statement Engine to delete the record from QAS");
        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);

        logger.info("Subm_file=" + submFile.getSubmFileSeqId() + " is created with the filename - " + submFile.getFilename());
        boolean IsStatementEngineProcessedFile = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(IsStatementEngineProcessedFile, "Failed to process subm file, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date set, submFileSeqId=" + submFileSeqId);

        logger.info("Make sure No New Errors added to the accn");
        Assert.assertEquals(accessionDao.getAccnPyrErrsByAccnId(accnId).size(), 0);

        logger.info("Make sure Patient Statement file is saved to the dir");
        String submFileName = processedSubmFile.getFilename();
        String fullPath = getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName;

        logger.info("Make sure in the Patient Statement file accession is present");
        File file = new File(fullPath);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);

        logger.info("Make sure Client's Ordering Facility Name, Correspondence address, Tax Id displayed in PRIMFAC segment");
        String patientStatementFile = FileUtils.readFileToString(file);
        Assert.assertTrue(patientStatementFile.contains("PRIMFAC|" + clnOrderingFac.getName() + "|" + clnOrderingFac.getName() + "|" + clnOrderingFac.getCorrespAddr1()));
        Assert.assertTrue(patientStatementFile.contains(clnOrderingFac.getCorrespZip()));
        Assert.assertTrue(patientStatementFile.contains(clnOrderingFac.getCorrespCity()));
        Assert.assertTrue(patientStatementFile.contains(clnOrderingFac.getCorrespPhn()));
        Assert.assertTrue(patientStatementFile.contains(rpmDao.getFacLicByFacAbbrevFacLicTypDescr(testDb, clnOrderingFac.getAbbrv(), "Tax ID").getLicId()));

        logger.info("Make sure Subm File Audit records do have date set");
        List<SubmFileAudit> submFileAudits = rpmDao.getSubmFileAudits(testDb, qasList.get(0).getDocSeqId());
        for(SubmFileAudit submFileAudit : submFileAudits){
            Assert.assertNotNull(submFileAudit.getActivityDt());
        }

        logger.info("Make sure DOB suppressed");
        SubmClaimAudit submClaimAudit = rpmDao.getSubmClaimAuditByDocSeqId(null, qasList.get(0).getDocSeqId());
        Assert.assertEquals(submClaimAudit.getSuppressedFlds(), String.valueOf(PatientStatementFieldMap.PT_DOB), "Expected DOB to be suppressed");
    }

    @Test(priority = 1, description = "XIFIN-Auto delivery method is marked a processed")
    @Parameters({"accnId", "primPyrAbbrv", "submSvcAbbrev"})
    public void testXifinAutoDeliveryMethodIsMarkedAsProcessed(String accnId, String primPyrAbbrv, String submSvcAbbrev) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        cleanUpAccn(accnId);

        logger.info("Verifying no open errors on accession, accnId=" + accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        accessionDetail.submitClaimsOnAccnDetail(accnId, primPyrAbbrv, StringUtils.EMPTY, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created, accnId=" + accnId);
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), 1);

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim, accnId="+accnId);
        int submFileSeqId = waitForSubmissionEngine(qasList.get(0), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Wait for Non-Client Statement Engine to process the file, accnId="+accnId+", submFileSeqId="+submFileSeqId+", filename="+submFile.getFilename());
        boolean isFileProcessed = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(isFileProcessed, "Failed to process subm file, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsProcessed(), "File should be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertTrue(DateUtils.isSameDay(processedSubmFile.getProcessedDt(), new Date()), "File should have a processed date equal to today's date, submFileSeqId=" + submFileSeqId+", processedDt="+submFile.getProcessedDt());

        logger.info("Make sure No New Errors added to the accn, accnId="+accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        logger.info("Make sure Patient Statement file is saved to the dir, filename="+processedSubmFile.getFilename());
        String submFileName = processedSubmFile.getFilename();
        String fullPath = getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName;
        logger.info("Make sure in the Patient Statement file accession is present, path="+fullPath);
        File file = new File(fullPath);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);
    }


    @Test(priority = 1, description = "Latest Denial Date is not determined from No Action error")
    @Parameters({"accnId", "pyrAbbrv", "submSvcAbbrev", "denial", "noActionDenial"})
    public void testLatestDenialDateIsNotDeterminedFromNoActionError(String accnId, String pyrAbbrv, String submSvcAbbrev, String denial, String noActionDenial) throws Exception
    {
        AccessionDetail accessionDetail = new AccessionDetail(driver, config, wait);
        Pyr pyr = payorDao.getPyrByPyrAbbrv(pyrAbbrv);
        cleanQASForPyrOnAccn(accnId, pyr.getPyrId());

        AccnProcErr denialAccnProcErr = null;
        AccnProcErr noActionDenialAccnProcErr = null;
        for (AccnProcErr accnProcErr : accessionDao.getAccnProcErrsByAccnId(accnId, true, false))
        {
            ErrCd errCd = errorDao.getErrCdByErrCd(accnProcErr.getErrCd());
            if (StringUtils.equalsIgnoreCase(errCd.getAbbrev(), denial) && !errCd.getIsNoActn())
            {
                denialAccnProcErr = accnProcErr;
            }
            else if (StringUtils.equalsIgnoreCase(errCd.getAbbrev(), noActionDenial) && errCd.getIsNoActn())
            {
                noActionDenialAccnProcErr = accnProcErr;
            }
        }
        Assert.assertNotNull(denialAccnProcErr);
        Assert.assertTrue(denialAccnProcErr.getAccnProcSeqId() > 0);
        Assert.assertNotNull(noActionDenialAccnProcErr);
        Assert.assertEquals(noActionDenialAccnProcErr.getAccnProcSeqId(), denialAccnProcErr.getAccnProcSeqId());
        Assert.assertTrue(noActionDenialAccnProcErr.getErrDt().after(denialAccnProcErr.getErrDt()));

        List<QAccnSubm> origQasList = rpmDao.getQAccnSubm(testDb, accnId);

        accessionDetail.submitClaimsOnAccnDetail(accnId, pyrAbbrv, StringUtils.EMPTY, submSvcAbbrev, wait);

        logger.info("Make sure 1 QAS record is created, accnId=" + accnId);
        List<QAccnSubm> qasList = rpmDao.getQAccnSubm(testDb, accnId);
        Assert.assertEquals(qasList.size(), origQasList.size()+1, "Expected 1 new QAS record, accnId="+accnId+", origQasCnt="+origQasList.size()+", newQasCnt="+qasList.size());

        logger.info("Update Last Date for Subm svc to be null, submSvcAbbrev=" + submSvcAbbrev);
        rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);

        logger.info("Wait for Non-Client Submission Engine to process the claim, accnId="+accnId);
        int submFileSeqId = waitForSubmissionEngine(qasList.get(qasList.size()-1), QUEUE_WAIT_TIME, submSvcAbbrev);
        Assert.assertTrue(submFileSeqId > 0);

        SubmFile submFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        logger.info("Wait for Non-Client Statement Engine to process the file, accnId="+accnId+", submFileSeqId="+submFileSeqId+", filename="+submFile.getFilename());
        boolean isFileProcessed = waitForStatementEngine(submFile, QUEUE_WAIT_TIME);
        Assert.assertTrue(isFileProcessed, "Failed to process subm file, submFileSeqId=" + submFileSeqId);

        SubmFile processedSubmFile = rpmDao.getSubmFile(testDb, submFileSeqId);
        Assert.assertTrue(processedSubmFile.getIsEgateProcessed(), "File did not process, submFileSeqId=" + submFileSeqId);
        Assert.assertFalse(processedSubmFile.getIsProcessed(), "File should not be marked as processed, submFileSeqId=" + submFileSeqId);
        Assert.assertNull(processedSubmFile.getProcessedDt(), "File should not have a processed date, submFileSeqId=" + submFileSeqId+", processedDt="+submFile.getProcessedDt());

        logger.info("Make sure No New Errors added to the accn, accnId="+accnId);
        Assert.assertTrue(accessionDao.getAccnPyrErrsByAccnId(accnId, false, false).isEmpty());

        logger.info("Make sure Patient Statement file is saved to the dir, filename="+processedSubmFile.getFilename());
        String submFileName = processedSubmFile.getFilename();
        String fullPath = getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + submFileName;
        logger.info("Make sure in the Patient Statement file accession is present, path="+fullPath);
        File file = new File(fullPath);
        Assert.assertTrue(file.exists(), "Expected file, filename=" + submFileName);
        String fileContent = FileUtils.readFileToString(file);
        String accnContent = extractAccnContentFromPipeDelimitedStatement(accnId, fileContent);
        Assert.assertNotNull(accnContent);
        ErrCd denialErrCd = errorDao.getErrCdByErrCd(denialAccnProcErr.getErrCd());
        AccnPyr denialAccnPyr = accessionDao.getAccnPyrByAccnIdAndPyrPrio(denialAccnProcErr.getAccnId(), denialAccnProcErr.getPyrPrio());
        Pyr denialPyr = payorDao.getPyrByPyrId(denialAccnPyr.getPyrId());
        // PROCDEN|20230401|MEDICARE - SOUTHERN CALIFORNIA|PR1|Deductible Amount||T
        String expectedDenialSegment = "PROCDEN|"+DATE_FMT_PIPE_DELIMITED_STATEMENT.format(denialAccnProcErr.getErrDt())+"|"+denialPyr.getName()+"|"+denialErrCd.getAbbrev()+"|"+denialErrCd.getShortDescr()+"||T";
        Assert.assertTrue(accnContent.contains(expectedDenialSegment));
    }

    private String extractAccnContentFromPipeDelimitedStatement(String accnId, String fileContent)
    {
        Matcher m = Pattern.compile(String.format(REGEX_PIPE_DELIMITED_ACCN_CONTENT, accnId)).matcher(fileContent);
        return m.find() ? m.group() : null;
    }


    /**
     * Saves DB data associated with the given PtNotificationLetterConfig. Saves the config record and the links.
     *
     * @param config PtNotificationLetterConfig
     */
    private void savePtNotificationLetterConfig(PtNotificationLetterConfig config) throws XifinDataAccessException
    {
        PtNotifLtrConfig ptNotifLtrConfig = getPtNotifLtrConfig(config);
        rpmDao.setPtNotifLtrConfig(testDb, ptNotifLtrConfig);
        int i = 0;
        for (PtNotifLtrConfigLnk ptNotifLtrConfigLnk : getPtNotifLtrConfigLnks(config))
        {
            ptNotifLtrConfigLnk.setPtNotifLtrConfigSeqId(ptNotifLtrConfig.getSeqId());
            ptNotifLtrConfigLnk.setSeqId(ptNotifLtrConfig.getSeqId() * 1000 + i);
            rpmDao.setPtNotifLtrConfigLnk(testDb, ptNotifLtrConfigLnk);
            i++;
        }
        logger.info("Created PtNotifLtrConfig & PtNotifLtrConfigLnk, ptNotifLtrConfigSeqId=" + ptNotifLtrConfig.getSeqId() + ", countNewPtNotifLtrConfigLnk=" + i);
    }

    /**
     * Gets a PtNotifLtrConfig value object for the given config. Creates a new record if one does not already exist.
     *
     * @param config PtNotificationLetterConfig
     * @return PtNotifLtrConfig
     */
    private PtNotifLtrConfig getPtNotifLtrConfig(PtNotificationLetterConfig config)
    {
        PtNotifLtrConfig ptNotifLtrConfig = new PtNotifLtrConfig(0);
        ptNotifLtrConfig.setResultCode(ErrorCodeMap.NEW_RECORD);
        ptNotifLtrConfig.setLetterId(config.getLetterId());
        ptNotifLtrConfig.setSeqId(config.getSeqId());
        return ptNotifLtrConfig;
    }

    /**
     * Creates new records for links that do not already exist.
     *
     * @param config PtNotificationLetterConfig
     * @return list of PtNotifLtrConfigLnk
     */
    private List<PtNotifLtrConfigLnk> getPtNotifLtrConfigLnks(PtNotificationLetterConfig config)
    {
        List<PtNotifLtrConfigLnk> ptNotifLtrConfigLnks = new ArrayList<>();
        Set<PtNotificationLetterConfig.Link> newLinks = new HashSet<>(config.getLinks());

        // Create PtNotifLtrConfigLnk objects for the new links.
        for (PtNotificationLetterConfig.Link newLink : newLinks)
        {
            PtNotifLtrConfigLnk newLnk = new PtNotifLtrConfigLnk();
            newLnk.setPtNotifLtrConfigLnkTyp(newLink.getType());
            newLnk.setConfigId(newLink.getId());
            ptNotifLtrConfigLnks.add(newLnk);
        }
        return ptNotifLtrConfigLnks;
    }

    private com.mbasys.mars.ejb.entity.test.Test getTestExclusion(List<AccnTest> accnTests) throws XifinDataAccessException
    {
        com.mbasys.mars.ejb.entity.test.Test exclTestRandom = rpmDao.getTestByTestAbbrev(testDb, rpmDao.getRandomTestAbbrevByCurrentDate(testDb, "01012014"));
        boolean exclTestExcluded = true;
        for (AccnTest accnTest : accnTests)
        {
            if (accnTest.getTestId() == exclTestRandom.getTestId())
            {
                exclTestExcluded = false;
            }
        }
        if (!exclTestExcluded)
        {
            exclTestRandom = null;
        }
        return exclTestRandom;
    }

    private List<File> extractImagesFromPdf(File file, String accnId) throws IOException
    {
        logger.info("message=Extracting images from PDF, file="+file.getAbsolutePath()+", accnId="+accnId);
        List<File> imageFiles = new ArrayList<>();
        String baseName = getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "nonclientsubm" + File.separator + "out" + File.separator + file.getName();
        int pageNum = 0;
        try (PDDocument document = PDDocument.load(file))
        {
            for (PDPage page : document.getPages())
            {
                pageNum++;
                String pageText = convertPdfPageToText(file, pageNum);
                if (StringUtils.isNotBlank(accnId) && !StringUtils.containsIgnoreCase(pageText, accnId))
                {
                    logger.info("message=PDF page does not contain accnId - skipping, file=" + file.getAbsolutePath() + ", pageNum=" + pageNum + ", accnId=" + accnId);
                    continue;
                }
                logger.info("message=Extracting images from PDF page, file="+file.getAbsolutePath()+", pageNum="+pageNum+", accnId="+accnId);
                PDResources pageResources = page.getResources();
                for (COSName objectName : pageResources.getXObjectNames())
                {
                    PDXObject pageObject = pageResources.getXObject(objectName);
                    if (pageObject instanceof PDImageXObject)
                    {
                        File imageFile = new File(baseName+"_image"+imageFiles.size()+".png");
                        logger.info("message=Writing PDF image file, i=" + imageFiles.size() + ", image=" + (PDImageXObject) pageObject + ", imageFile=" + imageFile.getAbsolutePath());
                        ImageIO.write(((PDImageXObject) pageObject).getImage(), "png", imageFile);
                        imageFiles.add(imageFile);
                    }
                }
            }
        }
        return imageFiles;
    }

    private void updatePtNotifLtrConfig() throws XifinDataAccessException
    {
        logger.info("Make sure NO Patient Notification Letter Configurations are saved in DB");
        List<PtNotifLtrConfig> ptNotifLtrConfigs = rpmDao.getPtNotifLtrConfigs(testDb);
        logger.info(ptNotifLtrConfigs);
        for (PtNotifLtrConfig ptNotifLtrConfig : ptNotifLtrConfigs)
        {
            rpmDao.deletePtNotifLtrConfig(testDb, ptNotifLtrConfig.getSeqId());
            logger.info("DELETED " + ptNotifLtrConfig.getSeqId());
        }
    }

    private String waitForSubmissionEngineToAddNote(QAccnSubm qAccnSubm, long maxTime, String submSvcAbbrev) throws InterruptedException, XifinDataAccessException
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        String note = qAccnSubm.getNote();
        while (StringUtils.isEmpty(note) && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for Submission Engine to add note, accnId=" + qAccnSubm.getAccnId() + ",docSeqId=" + qAccnSubm.getDocSeqId() + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME);
            try
            {
                long currentTime = System.currentTimeMillis();
                if (currentTime == maxTime / 2)
                {
                    logger.info("Waiting for maxTime/2=" + maxTime / 2);
                    rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);
                }
                qAccnSubm = rpmDao.getQAccnSubm(testDb, qAccnSubm.getDocSeqId());
            }
            catch (XifinDataNotFoundException e)
            {
                note = null;
            }
            note = qAccnSubm.getNote();
        }
        return note;
    }
}
