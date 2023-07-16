package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.qAccnSubm.QAccnSubm;
import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.mbasys.mars.ejb.entity.submSvc.SubmSvc;
import com.overall.menu.MenuNavigation;
import com.pfEngines.tests.EligibilityBaseTest.TestParameters;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.utils.SeleniumBaseTest;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

;

public class NonClientStatementEngineTest extends SeleniumBaseTest
{
    private static final String OS_NAME = System.getProperty("os.name");
    private static final String WINDOWS = "windows";
    protected static final String NON_CLIENT_SUBMISSION_ENGINE = "NonClnSubmEngine";
    public static long QUEUE_POLL_TIME = TimeUnit.SECONDS.toMillis(5);
    public static long QUEUE_WAIT_TIME = TimeUnit.MINUTES.toMillis(10);
    protected TestParameters parameters;
    MenuNavigation navigation;
    private PDFParser parser;
    private PDFTextStripper pdfStripper;
    private PDDocument pdDoc;
    private COSDocument cosDoc;

    public static String getBaseDir()
    {
        String baseDir;

        if (StringUtils.containsIgnoreCase(OS_NAME, WINDOWS))
        {
            baseDir = File.separator + File.separator + "a3unity01-mp" + File.separator + "cnfs01";
        }
        else
        {
            baseDir = File.separator + "home";
        }
        return baseDir;
    }

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

    @BeforeMethod(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins", "accnId"})
    public void beforeTest(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword,
                           String disableBrowserPlugins, @Optional String accnId)
    {
        try
        {
            logger.info("Running Before Method");
            logIntoSso(ssoUsername, ssoPassword);
            navigation = new MenuNavigation(driver, config);
            navigation.navigateToAccnDetailPage();
        }
        catch (Exception e)
        {
            Assert.fail("Error running Before Method", e);
        }
    }

    @AfterSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins"})
    public void afterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword,
                           @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running AfterSuite");
            //super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            //logIntoSso(ssoUsername, ssoPassword);
            //updateRpmEngines(Arrays.asList(TaskTypMap.NON_CLN_SUB_TASK_TYP_ID, TaskTypMap.NON_CLN_STMT_TASK_TYP_ID), "1 day", 0);
        }
        catch (Exception e)
        {
            Assert.fail("Error running AfterSuite", e);
        }
        finally
        {
            quitWebDriver();
        }
    }

    protected String convertPdfToText(File file) throws IOException
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

    protected String convertPdfPageToText(File file, int pageNum) throws IOException
    {
        PDFParser pdfParser = new PDFParser(new RandomAccessFile(file, "r"));
        pdfParser.parse();
        PDDocument doc = new PDDocument(pdfParser.getDocument());
        PDFTextStripper pdfStripper = new PDFTextStripper();
        pdfStripper.setStartPage(pageNum);
        pdfStripper.setEndPage(pageNum);
        return pdfStripper.getText(doc);
    }

    protected void cleanUpAccn(String accnId) throws Exception
    {
        logger.info("Deleting QClaimStatus, accnId=" + accnId);
        rpmDao.deleteQClaimStatus(null, accnId);
        logger.info("Clearing AccnProc SubmFileSeqId, accnId=" + accnId);
        rpmDao.clearAccnProcSubmFileSeqIdByAccnId(testDb, accnId);
        logger.info("Deleting SubmFileAudit records, accnId=" + accnId);
        rpmDao.deleteSubmFileAuditByAccnId(testDb, accnId);
        logger.info("Deleting ClaimJson records, accnId=" + accnId);
        submissionDao.deleteClaimJsonByAccnId(accnId);
        logger.info("Deleting SubmClaimAudit records, accnId=" + accnId);
        List<QAccnSubm> qAccnSubms = rpmDao.getQAccnSubm(testDb, accnId);
        for (QAccnSubm qas : qAccnSubms)
        {
            rpmDao.deleteSubmClaimAuditByDocId(testDb, qas.getDocSeqId());
        }
        logger.info("Deleting QAccnSubm records, accnId=" + accnId);
        rpmDao.deleteQAccnSubmByAccnId(testDb, accnId);

    }

    protected void cleanQASForPyrOnAccn(String accnId, int pyrId) throws Exception
    {
        cleanQASForPyrOnAccnExceptDocSeqIds(accnId, pyrId, new ArrayList<String>());
    }

    protected int waitForSubmissionEngine(QAccnSubm qAccnSubm, long maxTime, String submSvcAbbrev) throws InterruptedException, XifinDataAccessException
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
                long currentTime = System.currentTimeMillis();
                if (currentTime == (startTime + maxTime) / 2)
                {
                    logger.info("Waiting for maxTime/2=" + maxTime / 2);
                    rpmDao.updateLastSubmDtSubmSvcToNullBySubmSvcSeqId(testDb, rpmDao.getSubmSvcByAbbrev(testDb, submSvcAbbrev).submSvcSeqId);
                }
                qAccnSubm = rpmDao.getQAccnSubm(testDb, qAccnSubm.getDocSeqId());
            }
            catch (XifinDataNotFoundException e)
            {
                return -1;
            }
            submFileSeqId = qAccnSubm.getSubmFileSeqId();
        }
        return submFileSeqId;
    }

    protected int waitForSubmissionEngine(QAccnSubm qAccnSubm, long maxTime, Set<SubmSvc> submSvcSet) throws InterruptedException, XifinDataAccessException
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
                for (SubmSvc submSvc : submSvcSet)
                {
                    SubmSvc s = submissionDao.getSubmSvc(submSvc.getSubmSvcSeqId());
                    if (s.getLastSubmDt() != null)
                    {
                        logger.info("Clearing last_subm_dt, submSvcSeqId="+s.getSubmSvcSeqId()+", lastSubmDt="+s.getLastSubmDt());
                        s.setLastSubmDt(null);
                        databaseSequenceDao.setValueObject(s);
                    }
                }
                qAccnSubm = rpmDao.getQAccnSubm(testDb, qAccnSubm.getDocSeqId());
            }
            catch (XifinDataNotFoundException e)
            {
                submFileSeqId = -1;
            }
            submFileSeqId = qAccnSubm.getSubmFileSeqId();
        }
        return submFileSeqId;
    }

    protected boolean waitForStatementEngine(SubmFile submFile, long maxTime)
            throws InterruptedException, XifinDataAccessException
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean submFileBEgateProcessed = submFile.getIsEgateProcessed();
        while (!submFileBEgateProcessed && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for Statement Engine to process subm file, submFileSeqId=" + submFile.getSubmFileSeqId() + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME);
            SubmFile submFile1 = rpmDao.getSubmFile(testDb, submFile.getSubmFileSeqId());
            if (submFile1 == null)
            {
                logger.info("Submission file was not found, submFileSeqId=" + submFile.getSubmFileSeqId());
                break;
            }
            submFileBEgateProcessed = submFile1.getIsEgateProcessed();
        }
        return submFileBEgateProcessed;
    }

    /**
     * For a particular accession and payor, delete all the QAS entries except those that match the docSeqId list.
     *
     * @param accnId              Accession ID
     * @param pyrId               Payor ID
     * @param docSeqIdsStringList Doc Seq ID list
     * @throws Exception Exception
     */
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

    /**
     * For a particular accession, delete all the QAS entries except those that match the docSeqId list.
     *
     * @param accnId              Accession ID
     * @param docSeqIdsStringList Doc Seq ID list
     * @throws Exception Exception
     */
    protected void cleanQASForAccnExceptDocSeqIds(String accnId, List<String> docSeqIdsStringList) throws Exception
    {
        logger.info("Clearing QAS for accn, accnId=" + accnId);
        List<QAccnSubm> qasToDelete = new ArrayList<>();
        for (QAccnSubm accnSubm : rpmDao.getQAccnSubm(null, accnId))
        {
            if (!docSeqIdsStringList.contains(String.valueOf(accnSubm.getDocSeqId())))
            {
                qasToDelete.add(accnSubm);
            }
        }
        submissionDao.deleteSubmissions(qasToDelete);
    }

    protected boolean isOutOfPricingQueue(String accnId, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        maxTime +=  startTime;
        boolean isInQueue = accessionDao.isInPricingQueue(accnId);
        if (isInQueue)
        {
            accessionDao.clearQPriceError(accnId);
        }
        while (isInQueue && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            isInQueue = accessionDao.isInPricingQueue(accnId);
        }
        return !isInQueue;
    }

    protected boolean isOutOfEligibilityQueue(String accnId, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isInQueue = accessionDao.isInEligibilityQueue(accnId);
        while (isInQueue && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME);
            isInQueue = accessionDao.isInEligibilityQueue(accnId);
        }
        return !isInQueue;
    }
}
