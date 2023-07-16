package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.ackFile.AckFile;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.correspFile.CorrespFile;
import com.mbasys.mars.ejb.entity.docStore.DocStore;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.submAckFileLink.SubmAckFileLink;
import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.mbasys.mars.ejb.entity.submSvc.SubmSvc;
import com.mbasys.mars.persistance.MiscMap;
import com.xifin.qa.config.PropertyMap;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.FileManipulation;
import com.xifin.xap.utils.XifinAdminUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FileRouterEngineTest extends SeleniumBaseTest
{
    private static final Logger LOG = Logger.getLogger(FileRouterEngineTest.class);

    private static final int DOC_STORE_LOCATION_ID_LOCAL = 1;
    private static final int DOC_STORE_LOCATION_ID_S3 = 2;
    private static final String FILE_ROUTER_ENGINE_USER = "FileRouterEngine";

    private static final long QUEUE_WAIT_TIME_MS_CONCORD = TimeUnit.MINUTES.toMillis(15);

    @Test(priority = 1, description = "S-Fax Client Statement PDF from Local Storage")
    @Parameters({"ssoUsername", "ssoPassword", "submFileName", "clnAbbrev", "faxNumber"})
    public void test_ENG101660_01(String ssoUsername, String ssoPassword, String submFileName, String clnAbbrev, String faxNumber) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_01 ***");

        logIntoSso(ssoUsername, ssoPassword);
        SubmFile submFile = submissionDao.getSubmFileByFilename(submFileName);

        SubmSvc submSvc = submissionDao.getSubmSvc(submFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), MiscMap.DELVRY_MTHD_TYP_SFAX, "Delivery method is not S-Fax");
        Assert.assertNull(submFile.getDocStoreSeqId() , "SubmFile should be in local storage");

        Cln cln = rpmDao.getClnByClnAbbrev(null, clnAbbrev);
        Assert.assertEquals(cln.getBilFax(), faxNumber, "Client's Billing Fax is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Delete AckFile links
        submissionDao.deleteSubmAckFileLinkBySubmFileSeqId(submFile.getSubmFileSeqId());

        // Reset SubmFile
        submFile.setProcessedDt(null);
        submFile.setIsProcessed(false);
        submFile.setProcessedUserId(null);
        rpmDao.set(submFile);

        // Wait for subm file to be processed
        waitForSubmFileToBeProcessed(submFile.getSubmFileSeqId(), true, false, QUEUE_WAIT_TIME_MS*2);
        submFile = rpmDao.getSubmFile(null, submFile.getSubmFileSeqId());
        LOG.info("Verifying SubmFile, submFileSeqId="+submFile.getSubmFileSeqId());
        Assert.assertTrue(submFile.getIsProcessed(), "SubmFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(submFile.getProcessedDt().getTime()), new Date()), "SubmFile processed date is incorrect");
        Assert.assertEquals(submFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "SubmFile processed UserId is incorrect");

        LOG.info("Make sure AckFile is created");
        List<SubmAckFileLink> submAckFileLinks = rpmDao.getSubmAckFileLinkBySubmFileId(null, submFile.getSubmFileSeqId());
        Assert.assertEquals(submAckFileLinks.size(), 1,"Expected AckFile");
        Assert.assertTrue(waitForAckFileToBeProcessed(submAckFileLinks.get(0).getAckFileId(), QUEUE_WAIT_TIME_MS), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(submAckFileLinks.get(0).getAckFileId());
        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");
        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "sfax" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+ackFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());
        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Message: OK"), "Ack does not contain status");
        Assert.assertTrue(ackContent.contains(faxNumber), "Ack does not contain fax number");
        Assert.assertTrue(ackContent.contains("Result Code: 0"), "Ack does not contain result code");
    }

    @Test(priority = 1, description = "S-Fax Client Letter from Local Storage")
    @Parameters({"ssoUsername", "ssoPassword", "correspFileName", "clnAbbrev", "faxNumber"})
    public void test_ENG101660_02(String ssoUsername, String ssoPassword, String correspFileName, String clnAbbrev, String faxNumber) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_02 ***");

        logIntoSso(ssoUsername, ssoPassword);
        List<CorrespFile> correspFiles = rpmDao.getCorrespFilesByFileName(correspFileName);
        Assert.assertEquals(correspFiles.size(), 1, "Could not identify CorrespFile");
        CorrespFile correspFile = correspFiles.get(0);

        String correspFilePath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + correspFile.getFileRelPath() + correspFile.getFileName();
        Assert.assertTrue(new File(correspFilePath).exists(), "Corresp file does not exist, correspFilePath="+correspFilePath);

        SubmSvc submSvc = submissionDao.getSubmSvc(correspFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), MiscMap.DELVRY_MTHD_TYP_SFAX, "Delivery method is not S-Fax");
        Assert.assertNull(correspFile.getDocStoreId() , "EP Letter should not be in DocStore");

        Cln cln = rpmDao.getClnByClnAbbrev(null, clnAbbrev);
        Assert.assertEquals(cln.getCorrespFax(), faxNumber, "Client's Correspondence Fax is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Reset corresp file
        correspFile.setProcessedDt(null);
        correspFile.setAckFileId(0);
        correspFile.setIsProcessed(false);
        correspFile.setProcessedUserId(null);
        rpmDao.set(correspFile);
        logger.info("Clearing cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();
        // Wait for corresp file to be processed by FRE
        waitForCorrespFileToBeProcessedFileRouterEng(correspFile.getFileSeq(), QUEUE_WAIT_TIME_MS);
        correspFile = rpmDao.getCorrespFileByEPCorrespHist(correspFile.getFileSeq());
        LOG.info("Verifying corresp file, correspFileSeq="+correspFile.getFileSeq());
        Assert.assertTrue(correspFile.getIsProcessed(), "CorrespFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(correspFile.getProcessedDt().getTime()), new Date()), "CorrespFile processed date is incorrect");
        Assert.assertEquals(correspFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "CorrespFile processed user Id is incorrect");

        LOG.info("Make sure AckFile is created");
        Assert.assertTrue(correspFile.getAckFileId() > 0, "Ack file is not set");

        Assert.assertTrue(waitForAckFileToBeProcessed(correspFile.getAckFileId(), QUEUE_WAIT_TIME_MS), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(correspFile.getAckFileId());

        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");

        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "sfax" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+correspFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());

        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Message: OK"), "Ack does not contain status");
        Assert.assertTrue(ackContent.contains(faxNumber), "Ack does not contain fax number");
        Assert.assertTrue(ackContent.contains("Result Code: 0"), "Ack does not contain result code");
    }

    @Test(priority = 1, description = "S-Fax Appeal Letter from DocStore (S3)")
    @Parameters({"ssoUsername", "ssoPassword", "correspFileName", "faxNumber"})
    public void test_ENG101660_12(String ssoUsername, String ssoPassword, String correspFileName, String faxNumber) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_12 ***");

        logIntoSso(ssoUsername, ssoPassword);
        List<CorrespFile> correspFiles = rpmDao.getCorrespFilesByFileName(correspFileName);
        Assert.assertEquals(correspFiles.size(), 1, "Could not identify CorrespFile");
        CorrespFile correspFile = correspFiles.get(0);

        SubmSvc submSvc = submissionDao.getSubmSvc(correspFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), MiscMap.DELVRY_MTHD_TYP_SFAX, "Delivery method is not S-Fax");
        Assert.assertNotNull(correspFile.getDocStoreId() , "Could not identify DocStore record");
        DocStore docStore = rpmDao.getDocStore(correspFile.getDocStoreId());
        Assert.assertEquals(docStore.getDocStoreLocationId(), DOC_STORE_LOCATION_ID_S3, "DocStore location is incorrect");

        Pyr pyr = rpmDao.getPyrByPyrId(null, correspFile.getPyrId());
        Assert.assertEquals(pyr.getAppealFax(), faxNumber, "Payor's Appeal Fax is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Reset corresp file
        correspFile.setProcessedDt(null);
        correspFile.setAckFileId(0);
        correspFile.setIsProcessed(false);
        correspFile.setProcessedUserId(null);
        rpmDao.set(correspFile);

        // Wait for corresp file to be processed by FRE
        waitForCorrespFileToBeProcessedFileRouterEng(correspFile.getFileSeq(), QUEUE_WAIT_TIME_MS);
        correspFile = rpmDao.getCorrespFileByEPCorrespHist(correspFile.getFileSeq());
        LOG.info("Verifying corresp file, correspFileSeq="+correspFile.getFileSeq());
        Assert.assertTrue(correspFile.getIsProcessed(), "CorrespFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(correspFile.getProcessedDt().getTime()), new Date()), "CorrespFile processed date is incorrect");
        Assert.assertEquals(correspFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "CorrespFile processed user Id is incorrect");

        LOG.info("Make sure AckFile is created");
        Assert.assertTrue(correspFile.getAckFileId() > 0, "Ack file is not set");

        Assert.assertTrue(waitForAckFileToBeProcessed(correspFile.getAckFileId(), QUEUE_WAIT_TIME_MS), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(correspFile.getAckFileId());

        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");

        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "sfax" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+correspFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());

        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Message: OK"), "Ack does not contain status");
        Assert.assertTrue(ackContent.contains(faxNumber), "Ack does not contain fax number");
        Assert.assertTrue(ackContent.contains("Result Code: 0"), "Ack does not contain result code");
    }


    @Test(priority = 1, description = "S-Fax Attachment PDF")
    @Parameters({"ssoUsername", "ssoPassword", "submFileName", "pyrAbbrev", "faxNumber"})
    public void test_ENG101660_13(String ssoUsername, String ssoPassword, String submFileName, String pyrAbbrev, String faxNumber) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_13 ***");

        logIntoSso(ssoUsername, ssoPassword);
        SubmFile submFile = submissionDao.getSubmFileByFilename(submFileName);

        SubmSvc submSvc = submissionDao.getSubmSvc(submFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), MiscMap.DELVRY_MTHD_TYP_SFAX, "Delivery method is not S-Fax");
        Assert.assertNotNull(submFile.getDocStoreSeqId() , "SubmFile should be in doc storage");

        Pyr pyr = rpmDao.getPyrByPyrAbbrv(null, pyrAbbrev);
        Assert.assertEquals(pyr.getAttachFax(), faxNumber, "Payor's Attachment Fax is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Delete AckFile links
        submissionDao.deleteSubmAckFileLinkBySubmFileSeqId(submFile.getSubmFileSeqId());

        // Reset SubmFile
        submFile.setProcessedDt(null);
        submFile.setIsProcessed(false);
        submFile.setProcessedUserId(null);
        rpmDao.set(submFile);

        // Wait for subm file to be processed
        waitForSubmFileToBeProcessed(submFile.getSubmFileSeqId(), true, false, QUEUE_WAIT_TIME_MS);
        submFile = rpmDao.getSubmFile(null, submFile.getSubmFileSeqId());
        LOG.info("Verifying SubmFile, submFileSeqId="+submFile.getSubmFileSeqId());
        Assert.assertTrue(submFile.getIsProcessed(), "SubmFile is not flagged as processed");

        Assert.assertTrue(DateUtils.isSameDay(new Date(submFile.getProcessedDt().getTime()), new Date()), "SubmFile processed date is incorrect");

        Assert.assertEquals(submFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "SubmFile processed UserId is incorrect");

        LOG.info("Make sure AckFile is created");
        List<SubmAckFileLink> submAckFileLinks = rpmDao.getSubmAckFileLinkBySubmFileId(null, submFile.getSubmFileSeqId());
        Assert.assertEquals(submAckFileLinks.size(), 1,"Expected AckFile");
        Assert.assertTrue(waitForAckFileToBeProcessed(submAckFileLinks.get(0).getAckFileId(), QUEUE_WAIT_TIME_MS), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(submAckFileLinks.get(0).getAckFileId());
        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");
        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "sfax" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+ackFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());
        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Message: OK"), "Ack does not contain status");
        Assert.assertTrue(ackContent.contains(faxNumber), "Ack does not contain fax number");
        Assert.assertTrue(ackContent.contains("Result Code: 0"), "Ack does not contain result code");
    }

    @Test(priority = 1, description = "E-Fax Client Letter from Local Storage")
    @Parameters({"ssoUsername", "ssoPassword", "correspFileName", "clnAbbrev", "faxNumber"})
    public void test_ENG101660_03(String ssoUsername, String ssoPassword, String correspFileName, String clnAbbrev, String faxNumber) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_03 ***");

        logIntoSso(ssoUsername, ssoPassword);
        List<CorrespFile> correspFiles = rpmDao.getCorrespFilesByFileName(correspFileName);
        Assert.assertEquals(correspFiles.size(), 1, "Could not identify CorrespFile");
        CorrespFile correspFile = correspFiles.get(0);

        SubmSvc submSvc = submissionDao.getSubmSvc(correspFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), MiscMap.DELVRY_MTHD_TYP_EFAX, "Delivery method is not E-Fax");
        Assert.assertNull(correspFile.getDocStoreId() , "EP Letter should not be in DocStore");

        Cln cln = rpmDao.getClnByClnAbbrev(null, clnAbbrev);
        Assert.assertEquals(cln.getCorrespFax(), faxNumber, "Client's Correspondence Fax is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Reset corresp file
        correspFile.setProcessedDt(null);
        correspFile.setAckFileId(0);
        correspFile.setIsProcessed(false);
        correspFile.setProcessedUserId(null);
        rpmDao.set(correspFile);
        logger.info("Clearing cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();
        // Wait for corresp file to be processed by FRE
        waitForCorrespFileToBeProcessedFileRouterEng(correspFile.getFileSeq(), QUEUE_WAIT_TIME_MS);
        correspFile = rpmDao.getCorrespFileByEPCorrespHist(correspFile.getFileSeq());
        LOG.info("Verifying corresp file, correspFileSeq="+correspFile.getFileSeq());
        Assert.assertTrue(correspFile.getIsProcessed(), "CorrespFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(correspFile.getProcessedDt().getTime()), new Date()), "CorrespFile processed date is incorrect");
        Assert.assertEquals(correspFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "CorrespFile processed user Id is incorrect");

        LOG.info("Make sure AckFile is created");
        Assert.assertTrue(correspFile.getAckFileId() > 0, "Ack file is not set");

        Assert.assertTrue(waitForAckFileToBeProcessed(correspFile.getAckFileId(), QUEUE_WAIT_TIME_MS), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(correspFile.getAckFileId());

        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");

        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "efax" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+correspFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());

        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Status Outcome: \"Success\""), "Ack does not contain status");
        Assert.assertTrue(ackContent.contains(faxNumber), "Ack does not contain fax number");
        Assert.assertTrue(ackContent.contains("Your transmission has completed."), "Ack does not contain completed transmission");
    }

    @Test(priority = 1, description = "E-Fax Appeal Letter from DocStore (S3)")
    @Parameters({"ssoUsername", "ssoPassword", "correspFileName", "faxNumber"})
    public void test_ENG101660_04(String ssoUsername, String ssoPassword, String correspFileName, String faxNumber) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_04 ***");

        logIntoSso(ssoUsername, ssoPassword);
        List<CorrespFile> correspFiles = rpmDao.getCorrespFilesByFileName(correspFileName);
        Assert.assertEquals(correspFiles.size(), 1, "Could not identify CorrespFile");
        CorrespFile correspFile = correspFiles.get(0);

        SubmSvc submSvc = submissionDao.getSubmSvc(correspFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), MiscMap.DELVRY_MTHD_TYP_EFAX, "Delivery method is not E-Fax");
        Assert.assertNotNull(correspFile.getDocStoreId() , "Could not identify DocStore record");
        DocStore docStore = rpmDao.getDocStore(correspFile.getDocStoreId());
        Assert.assertEquals(docStore.getDocStoreLocationId(), DOC_STORE_LOCATION_ID_S3, "DocStore location is incorrect");

        Pyr pyr = rpmDao.getPyrByPyrId(null, correspFile.getPyrId());
        Assert.assertEquals(pyr.getAppealFax(), faxNumber, "Payor's Appeal Fax is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Reset corresp file
        correspFile.setProcessedDt(null);
        correspFile.setAckFileId(0);
        correspFile.setIsProcessed(false);
        correspFile.setProcessedUserId(null);
        rpmDao.set(correspFile);

        // Wait for corresp file to be processed by FRE
        waitForCorrespFileToBeProcessedFileRouterEng(correspFile.getFileSeq(), QUEUE_WAIT_TIME_MS);
        correspFile = rpmDao.getCorrespFileByEPCorrespHist(correspFile.getFileSeq());
        LOG.info("Verifying corresp file, correspFileSeq="+correspFile.getFileSeq());
        Assert.assertTrue(correspFile.getIsProcessed(), "CorrespFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(correspFile.getProcessedDt().getTime()), new Date()), "CorrespFile processed date is incorrect");
        Assert.assertEquals(correspFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "CorrespFile processed user Id is incorrect");

        LOG.info("Make sure AckFile is created");
        Assert.assertTrue(correspFile.getAckFileId() > 0, "Ack file is not set");

        Assert.assertTrue(waitForAckFileToBeProcessed(correspFile.getAckFileId(), QUEUE_WAIT_TIME_MS), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(correspFile.getAckFileId());

        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");

        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "efax" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+correspFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());

        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Status Outcome: \"Success\""), "Ack does not contain status");
        Assert.assertTrue(ackContent.contains(faxNumber), "Ack does not contain fax number");
        Assert.assertTrue(ackContent.contains("Your transmission has completed."), "Ack does not contain completed transmission");
    }

    @Test(priority = 1, description = "E-Fax Client Statement PDF from Local Storage")
    @Parameters({"ssoUsername", "ssoPassword", "submFileName", "clnAbbrev", "faxNumber"})
    public void test_ENG101660_05(String ssoUsername, String ssoPassword, String submFileName, String clnAbbrev, String faxNumber) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_05 ***");

        logIntoSso(ssoUsername, ssoPassword);
        SubmFile submFile = submissionDao.getSubmFileByFilename(submFileName);

        SubmSvc submSvc = submissionDao.getSubmSvc(submFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), MiscMap.DELVRY_MTHD_TYP_EFAX, "Delivery method is not E-Fax");
        Assert.assertNull(submFile.getDocStoreSeqId() , "SubmFile should be in local storage");

        Cln cln = rpmDao.getClnByClnAbbrev(null, clnAbbrev);
        Assert.assertEquals(cln.getBilFax(), faxNumber, "Client's Billing Fax is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Delete AckFile links
        submissionDao.deleteSubmAckFileLinkBySubmFileSeqId(submFile.getSubmFileSeqId());

        // Reset SubmFile
        submFile.setProcessedDt(null);
        submFile.setIsProcessed(false);
        submFile.setProcessedUserId(null);
        rpmDao.set(submFile);

        // Wait for subm file to be processed
        waitForSubmFileToBeProcessed(submFile.getSubmFileSeqId(), true, false, QUEUE_WAIT_TIME_MS);
        submFile = rpmDao.getSubmFile(null, submFile.getSubmFileSeqId());
        LOG.info("Verifying SubmFile, submFileSeqId="+submFile.getSubmFileSeqId());
        Assert.assertTrue(submFile.getIsProcessed(), "SubmFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(submFile.getProcessedDt().getTime()), new Date()), "SubmFile processed date is incorrect");
        Assert.assertEquals(submFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "SubmFile processed UserId is incorrect");

        LOG.info("Make sure AckFile is created");
        List<SubmAckFileLink> submAckFileLinks = rpmDao.getSubmAckFileLinkBySubmFileId(null, submFile.getSubmFileSeqId());
        Assert.assertEquals(submAckFileLinks.size(), 1,"Expected AckFile");
        Assert.assertTrue(waitForAckFileToBeProcessed(submAckFileLinks.get(0).getAckFileId(), QUEUE_WAIT_TIME_MS), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(submAckFileLinks.get(0).getAckFileId());
        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");
        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "efax" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+ackFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());
        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Status Outcome: \"Success\""), "Ack does not contain status");
        Assert.assertTrue(ackContent.contains(faxNumber), "Ack does not contain fax number");
        Assert.assertTrue(ackContent.contains("Your transmission has completed."), "Ack does not contain completed transmission");
    }

    @Test(priority = 1, description = "E-Fax Attachment PDF")
    @Parameters({"ssoUsername", "ssoPassword", "submFileName", "pyrAbbrev", "faxNumber"})
    public void test_ENG101660_06(String ssoUsername, String ssoPassword, String submFileName, String pyrAbbrev, String faxNumber) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_06 ***");

        logIntoSso(ssoUsername, ssoPassword);
        SubmFile submFile = submissionDao.getSubmFileByFilename(submFileName);

        SubmSvc submSvc = submissionDao.getSubmSvc(submFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), MiscMap.DELVRY_MTHD_TYP_EFAX, "Delivery method is not E-Fax");
        Assert.assertNotNull(submFile.getDocStoreSeqId() , "SubmFile should be in doc storage");

        Pyr pyr = rpmDao.getPyrByPyrAbbrv(null, pyrAbbrev);
        Assert.assertEquals(pyr.getAttachFax(), faxNumber, "Payor's Attach Fax is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Delete AckFile links
        submissionDao.deleteSubmAckFileLinkBySubmFileSeqId(submFile.getSubmFileSeqId());

        // Reset SubmFile
        submFile.setProcessedDt(null);
        submFile.setIsProcessed(false);
        submFile.setProcessedUserId(null);
        rpmDao.set(submFile);

        // Wait for subm file to be processed
        waitForSubmFileToBeProcessed(submFile.getSubmFileSeqId(), true, false, QUEUE_WAIT_TIME_MS);
        submFile = rpmDao.getSubmFile(null, submFile.getSubmFileSeqId());
        LOG.info("Verifying SubmFile, submFileSeqId="+submFile.getSubmFileSeqId());
        Assert.assertTrue(submFile.getIsProcessed(), "SubmFile is not flagged as processed");

        // FRE does not set SUBM_FILE.PROCESSED_DT for Attachment files
        //Assert.assertTrue(DateUtils.isSameDay(new Date(submFile.getProcessedDt().getTime()), new Date()), "SubmFile processed date is incorrect");

        Assert.assertEquals(submFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "SubmFile processed UserId is incorrect");

        LOG.info("Make sure AckFile is created");
        List<SubmAckFileLink> submAckFileLinks = rpmDao.getSubmAckFileLinkBySubmFileId(null, submFile.getSubmFileSeqId());
        Assert.assertEquals(submAckFileLinks.size(), 1,"Expected AckFile");
        Assert.assertTrue(waitForAckFileToBeProcessed(submAckFileLinks.get(0).getAckFileId(), QUEUE_WAIT_TIME_MS), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(submAckFileLinks.get(0).getAckFileId());
        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");
        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "efax" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+ackFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());
        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Status Outcome: \"Success\""), "Ack does not contain status");
        Assert.assertTrue(ackContent.contains(faxNumber), "Ack does not contain fax number");
        Assert.assertTrue(ackContent.contains("Your transmission has completed."), "Ack does not contain completed transmission");
    }

    @Test(priority = 1, description = "Attachment statement with address override is sent correctly (E-Fax)")
    @Parameters({"ssoUsername", "ssoPassword", "submFileName", "pyrAbbrev", "faxNumber"})
    public void testAttachmentStatementWithAddressOverride(String ssoUsername, String ssoPassword, String submFileName, String pyrAbbrev, String faxNumber) throws Exception
    {
        LOG.info("*** Starting - Test Attachment Statement With Address Override ***");

        logIntoSso(ssoUsername, ssoPassword);
        SubmFile submFile = submissionDao.getSubmFileByFilename(submFileName);

        SubmSvc submSvc = submissionDao.getSubmSvc(submFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), MiscMap.DELVRY_MTHD_TYP_EFAX, "Delivery method is not E-Fax");
        Assert.assertNotNull(submFile.getDocStoreSeqId() , "SubmFile should be in doc storage");

        Pyr pyr = rpmDao.getPyrByPyrAbbrv(null, pyrAbbrev);
        Assert.assertTrue(StringUtils.isNotBlank(pyr.getAttachFax()), "Payor's Attachment Fax should not be empty");
        Assert.assertNotEquals(pyr.getAttachFax(), faxNumber, "Payor's Attachment Fax should not match override fax");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Delete AckFile links
        submissionDao.deleteSubmAckFileLinkBySubmFileSeqId(submFile.getSubmFileSeqId());

        // Reset SubmFile
        submFile.setProcessedDt(null);
        submFile.setIsProcessed(false);
        submFile.setProcessedUserId(null);
        rpmDao.set(submFile);

        // Wait for subm file to get ack file links
        waitForSubmAckFileLinks(submFile.getSubmFileSeqId(), QUEUE_WAIT_TIME_MS);
        submFile = rpmDao.getSubmFile(null, submFile.getSubmFileSeqId());

        Assert.assertTrue(DateUtils.isSameDay(new Date(submFile.getProcessedDt().getTime()), new Date()), "SubmFile processed date is incorrect");

        Assert.assertEquals(submFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "SubmFile processed UserId is incorrect");

        LOG.info("Make sure AckFile is created");
        List<SubmAckFileLink> submAckFileLinks = rpmDao.getSubmAckFileLinkBySubmFileId(null, submFile.getSubmFileSeqId());
        Assert.assertEquals(submAckFileLinks.size(), 1,"Expected AckFile");
        Assert.assertTrue(waitForAckFileToBeProcessed(submAckFileLinks.get(0).getAckFileId(), QUEUE_WAIT_TIME_MS), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(submAckFileLinks.get(0).getAckFileId());
        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");
        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "efax" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+ackFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());
        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Status Outcome: \"phone number failed to pass phone mask\""), "Ack does not contain status");
        Assert.assertTrue(ackContent.contains("Fax Number: "+faxNumber), "Ack does not contain fax number");
        Assert.assertTrue(ackContent.contains("Your transmission has completed."), "Ack does not contain completed transmission");
    }

    @Test(priority = 1, description = "Appeal letter with address override is sent correctly (E-Fax)")
    @Parameters({"ssoUsername", "ssoPassword", "correspFileName", "faxNumber"})
    public void testAppealLetterWithAddressOverride(String ssoUsername, String ssoPassword, String correspFileName, String faxNumber) throws Exception
    {
        LOG.info("*** Starting - Test Appeal Letter With Address Override ***");

        logIntoSso(ssoUsername, ssoPassword);
        List<CorrespFile> correspFiles = rpmDao.getCorrespFilesByFileName(correspFileName);
        Assert.assertEquals(correspFiles.size(), 1, "Could not identify CorrespFile");
        CorrespFile correspFile = correspFiles.get(0);

        SubmSvc submSvc = submissionDao.getSubmSvc(correspFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), MiscMap.DELVRY_MTHD_TYP_EFAX, "Delivery method is not E-Fax");
        Assert.assertNotNull(correspFile.getDocStoreId() , "Could not identify DocStore record");
        DocStore docStore = rpmDao.getDocStore(correspFile.getDocStoreId());
        Assert.assertEquals(docStore.getDocStoreLocationId(), DOC_STORE_LOCATION_ID_S3, "DocStore location is incorrect");

        Pyr pyr = rpmDao.getPyrByPyrId(null, correspFile.getPyrId());
        Assert.assertTrue(StringUtils.isNotBlank(pyr.getAppealFax()), "Payor's Appeal Fax should not be empty");
        Assert.assertNotEquals(pyr.getAppealFax(), faxNumber, "Payor's Appeal Fax should not match override fax");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Reset corresp file
        correspFile.setProcessedDt(null);
        correspFile.setAckFileId(0);
        correspFile.setIsProcessed(false);
        correspFile.setProcessedUserId(null);
        rpmDao.set(correspFile);

        // Wait for corresp file to be processed by FRE
        waitForCorrespFileAckFile(correspFile.getFileSeq(), QUEUE_WAIT_TIME_MS);
        correspFile = rpmDao.getCorrespFileByEPCorrespHist(correspFile.getFileSeq());
        LOG.info("Verifying corresp file, correspFileSeq="+correspFile.getFileSeq());
        Assert.assertTrue(DateUtils.isSameDay(new Date(correspFile.getProcessedDt().getTime()), new Date()), "CorrespFile processed date is incorrect");
        Assert.assertEquals(correspFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "CorrespFile processed user Id is incorrect");

        LOG.info("Make sure AckFile is created");
        Assert.assertTrue(correspFile.getAckFileId() > 0, "Ack file is not set");

        Assert.assertTrue(waitForAckFileToBeProcessed(correspFile.getAckFileId(), QUEUE_WAIT_TIME_MS), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(correspFile.getAckFileId());

        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");

        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "efax" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+correspFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());

        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Status Outcome: \"phone number failed to pass phone mask\""), "Ack does not contain status");
        Assert.assertTrue(ackContent.contains("Fax Number: "+faxNumber), "Ack does not contain fax number");
        Assert.assertTrue(ackContent.contains("Your transmission has completed."), "Ack does not contain completed transmission");
    }

    @Test(priority = 1, description = "LuxSci Client Letter from Local Storage")
    @Parameters({"ssoUsername", "ssoPassword", "correspFileName", "clnAbbrev", "emailAddr"})
    public void test_ENG101660_07(String ssoUsername, String ssoPassword, String correspFileName, String clnAbbrev, String emailAddr) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_07 ***");

        logIntoSso(ssoUsername, ssoPassword);
        List<CorrespFile> correspFiles = rpmDao.getCorrespFilesByFileName(correspFileName);
        Assert.assertEquals(correspFiles.size(), 1, "Could not identify CorrespFile");
        CorrespFile correspFile = correspFiles.get(0);

        SubmSvc submSvc = submissionDao.getSubmSvc(correspFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), MiscMap.DELVRY_MTHD_TYP_LUXSCI, "Delivery method is not LuxSci");
        Assert.assertNull(correspFile.getDocStoreId() , "EP Letter should not be in DocStore");

        Cln cln = rpmDao.getClnByClnAbbrev(null, clnAbbrev);
        Assert.assertEquals(cln.getCorrespEmail(), emailAddr, "Client's Correspondence email is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Reset corresp file
        correspFile.setProcessedDt(null);
        correspFile.setAckFileId(0);
        correspFile.setIsProcessed(false);
        correspFile.setProcessedUserId(null);
        rpmDao.set(correspFile);
        logger.info("Clearing cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();
        // Wait for corresp file to be processed by FRE
        waitForCorrespFileToBeProcessedFileRouterEng(correspFile.getFileSeq(), QUEUE_WAIT_TIME_MS);
        correspFile = rpmDao.getCorrespFileByEPCorrespHist(correspFile.getFileSeq());
        LOG.info("Verifying corresp file, correspFileSeq="+correspFile.getFileSeq());
        Assert.assertTrue(correspFile.getIsProcessed(), "CorrespFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(correspFile.getProcessedDt().getTime()), new Date()), "CorrespFile processed date is incorrect");
        Assert.assertEquals(correspFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "CorrespFile processed user Id is incorrect");

        LOG.info("Make sure AckFile is created");
        Assert.assertTrue(correspFile.getAckFileId() > 0, "Ack file is not set");

        Assert.assertTrue(waitForAckFileToBeProcessed(correspFile.getAckFileId(), QUEUE_WAIT_TIME_MS), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(correspFile.getAckFileId());

        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");

        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "luxsci" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+correspFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());

        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Status: Delivered"), "Ack does not contain delivered status");
        Assert.assertTrue(ackContent.contains("To: "+emailAddr), "Ack does not contain recipient email address");
    }

    @Test(priority = 1, description = "LuxSci Appeal Letter from DocStore (S3)")
    @Parameters({"ssoUsername", "ssoPassword", "correspFileName", "emailAddr"})
    public void test_ENG101660_08(String ssoUsername, String ssoPassword, String correspFileName, String emailAddr) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_08 ***");

        logIntoSso(ssoUsername, ssoPassword);
        List<CorrespFile> correspFiles = rpmDao.getCorrespFilesByFileName(correspFileName);
        Assert.assertEquals(correspFiles.size(), 1, "Could not identify CorrespFile");
        CorrespFile correspFile = correspFiles.get(0);

        SubmSvc submSvc = submissionDao.getSubmSvc(correspFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), MiscMap.DELVRY_MTHD_TYP_LUXSCI, "Delivery method is not LuxSci");
        Assert.assertNotNull(correspFile.getDocStoreId() , "Could not identify DocStore record");
        DocStore docStore = rpmDao.getDocStore(correspFile.getDocStoreId());
        Assert.assertEquals(docStore.getDocStoreLocationId(), DOC_STORE_LOCATION_ID_S3, "DocStore location is incorrect");

        Pyr pyr = rpmDao.getPyrByPyrId(null, correspFile.getPyrId());
        Assert.assertEquals(pyr.getAppealEmail(), emailAddr, "Payor's appeal email is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Reset corresp file
        correspFile.setProcessedDt(null);
        correspFile.setAckFileId(0);
        correspFile.setIsProcessed(false);
        correspFile.setProcessedUserId(null);
        rpmDao.set(correspFile);

        // Wait for corresp file to be processed by FRE
        waitForCorrespFileToBeProcessedFileRouterEng(correspFile.getFileSeq(), QUEUE_WAIT_TIME_MS);
        correspFile = rpmDao.getCorrespFileByEPCorrespHist(correspFile.getFileSeq());
        LOG.info("Verifying corresp file, correspFileSeq="+correspFile.getFileSeq());
        Assert.assertTrue(correspFile.getIsProcessed(), "CorrespFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(correspFile.getProcessedDt().getTime()), new Date()), "CorrespFile processed date is incorrect");
        Assert.assertEquals(correspFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "CorrespFile processed user Id is incorrect");

        LOG.info("Make sure AckFile is created");
        Assert.assertTrue(correspFile.getAckFileId() > 0, "Ack file is not set");

        Assert.assertTrue(waitForAckFileToBeProcessed(correspFile.getAckFileId(), QUEUE_WAIT_TIME_MS), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(correspFile.getAckFileId());

        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");

        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "luxsci" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+correspFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());

        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Status: Delivered"), "Ack does not contain delivered status");
        Assert.assertTrue(ackContent.contains("To: "+emailAddr), "Ack does not contain recipient email address");
    }

    @Test(priority = 1, description = "LuxSci Client Statement PDF from Local Storage")
    @Parameters({"ssoUsername", "ssoPassword", "submFileName", "clnAbbrev", "emailAddr"})
    public void test_ENG101660_09(String ssoUsername, String ssoPassword, String submFileName, String clnAbbrev, String emailAddr) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_09 ***");

        logIntoSso(ssoUsername, ssoPassword);
        SubmFile submFile = submissionDao.getSubmFileByFilename(submFileName);

        SubmSvc submSvc = submissionDao.getSubmSvc(submFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), MiscMap.DELVRY_MTHD_TYP_LUXSCI, "Delivery method is not LuxSci");
        Assert.assertNull(submFile.getDocStoreSeqId() , "SubmFile should be in local storage");

        Cln cln = rpmDao.getClnByClnAbbrev(null, clnAbbrev);
        Assert.assertEquals(cln.getBilEmail(), emailAddr, "Client's Billing email is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Delete AckFile links
        submissionDao.deleteSubmAckFileLinkBySubmFileSeqId(submFile.getSubmFileSeqId());

        // Reset SubmFile
        submFile.setProcessedDt(null);
        submFile.setIsProcessed(false);
        submFile.setProcessedUserId(null);
        rpmDao.set(submFile);

        // Wait for subm file to be processed
        waitForSubmFileToBeProcessed(submFile.getSubmFileSeqId(), true, false, QUEUE_WAIT_TIME_MS);
        submFile = rpmDao.getSubmFile(null, submFile.getSubmFileSeqId());
        LOG.info("Verifying SubmFile, submFileSeqId="+submFile.getSubmFileSeqId());
        Assert.assertTrue(submFile.getIsProcessed(), "SubmFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(submFile.getProcessedDt().getTime()), new Date()), "SubmFile processed date is incorrect");
        Assert.assertEquals(submFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "SubmFile processed UserId is incorrect");

        LOG.info("Make sure AckFile is created");
        List<SubmAckFileLink> submAckFileLinks = rpmDao.getSubmAckFileLinkBySubmFileId(null, submFile.getSubmFileSeqId());
        Assert.assertEquals(submAckFileLinks.size(), 1,"Expected AckFile");
        Assert.assertTrue(waitForAckFileToBeProcessed(submAckFileLinks.get(0).getAckFileId(), QUEUE_WAIT_TIME_MS), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(submAckFileLinks.get(0).getAckFileId());
        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");
        Assert.assertEquals(ackFile.getNotes(), "Delivered", "AckFile not is not correct");
        Assert.assertEquals(ackFile.getAckTypId(), MiscMap.ACK_TYP_LUXSCI, "AckFile type is not LUXSCI");

        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "luxsci" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+ackFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());
        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Status: Delivered"), "Ack does not contain delivered status");
        Assert.assertTrue(ackContent.contains("To: "+emailAddr), "Ack does not contain recipient email address");
    }

    @Test(priority = 1, description = "LuxSci Attachment PDF")
    @Parameters({"ssoUsername", "ssoPassword", "submFileName", "pyrAbbrev", "emailAddr"})
    public void test_ENG101660_10(String ssoUsername, String ssoPassword, String submFileName, String pyrAbbrev, String emailAddr) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_10 ***");

        logIntoSso(ssoUsername, ssoPassword);
        SubmFile submFile = submissionDao.getSubmFileByFilename(submFileName);

        SubmSvc submSvc = submissionDao.getSubmSvc(submFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), MiscMap.DELVRY_MTHD_TYP_LUXSCI, "Delivery method is not LuxSci");
        Assert.assertNotNull(submFile.getDocStoreSeqId() , "SubmFile should be in doc storage");

        Pyr pyr = rpmDao.getPyrByPyrAbbrv(null, pyrAbbrev);
        Assert.assertEquals(pyr.getAttachEmail(), emailAddr, "Payor Attach Email is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Delete AckFile links
        submissionDao.deleteSubmAckFileLinkBySubmFileSeqId(submFile.getSubmFileSeqId());

        // Reset SubmFile
        submFile.setProcessedDt(null);
        submFile.setIsProcessed(false);
        submFile.setProcessedUserId(null);
        rpmDao.set(submFile);

        // Wait for subm file to be processed
        waitForSubmFileToBeProcessed(submFile.getSubmFileSeqId(), true, false, QUEUE_WAIT_TIME_MS);
        submFile = rpmDao.getSubmFile(null, submFile.getSubmFileSeqId());
        LOG.info("Verifying SubmFile, submFileSeqId="+submFile.getSubmFileSeqId());
        Assert.assertTrue(submFile.getIsProcessed(), "SubmFile is not flagged as processed");

        // FRE does not set SUBM_FILE.PROCESSED_DT for Attachment files
        //Assert.assertTrue(DateUtils.isSameDay(new Date(submFile.getProcessedDt().getTime()), new Date()), "SubmFile processed date is incorrect");

        Assert.assertEquals(submFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "SubmFile processed UserId is incorrect");

        LOG.info("Make sure AckFile is created");
        List<SubmAckFileLink> submAckFileLinks = rpmDao.getSubmAckFileLinkBySubmFileId(null, submFile.getSubmFileSeqId());
        Assert.assertEquals(submAckFileLinks.size(), 1,"Expected AckFile");
        Assert.assertTrue(waitForAckFileToBeProcessed(submAckFileLinks.get(0).getAckFileId(), QUEUE_WAIT_TIME_MS), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(submAckFileLinks.get(0).getAckFileId());
        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");
        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "luxsci" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+ackFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());
        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Status: Delivered"), "Ack does not contain delivered status");
        Assert.assertTrue(ackContent.contains("To: "+emailAddr), "Ack does not contain recipient email address");
    }

    @Test(priority = 1, description = "LuxSci Client Statement Undeliverable Email")
    @Parameters({"ssoUsername", "ssoPassword", "submFileName", "clnAbbrev", "emailAddr"})
    public void test_ENG101660_11(String ssoUsername, String ssoPassword, String submFileName, String clnAbbrev, String emailAddr) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_11 ***");

        logIntoSso(ssoUsername, ssoPassword);
        SubmFile submFile = submissionDao.getSubmFileByFilename(submFileName);

        SubmSvc submSvc = submissionDao.getSubmSvc(submFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), MiscMap.DELVRY_MTHD_TYP_LUXSCI, "Delivery method is not LuxSci");
        Assert.assertNull(submFile.getDocStoreSeqId() , "SubmFile should be in local storage");

        Cln cln = rpmDao.getClnByClnAbbrev(null, clnAbbrev);
        logger.info("Verifying client's billing email address, clnAbbrev="+cln.getClnAbbrev()+", bilEmail="+emailAddr);
        Assert.assertEquals(cln.getBilEmail(), emailAddr, "Client's billing email address is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Delete AckFile links
        submissionDao.deleteSubmAckFileLinkBySubmFileSeqId(submFile.getSubmFileSeqId());

        // Reset SubmFile
        submFile.setProcessedDt(null);
        submFile.setIsProcessed(false);
        submFile.setProcessedUserId(null);
        rpmDao.set(submFile);

        // Wait for subm file to be processed
        waitForSubmFileToBeProcessed(submFile.getSubmFileSeqId(), false, true, QUEUE_WAIT_TIME_MS);
        submFile = rpmDao.getSubmFile(null, submFile.getSubmFileSeqId());
        LOG.info("Verifying SubmFile, submFileSeqId="+submFile.getSubmFileSeqId());
        Assert.assertFalse(submFile.getIsProcessed(), "SubmFile is flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(submFile.getProcessedDt().getTime()), new Date()), "SubmFile processed date is incorrect");
        Assert.assertEquals(submFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "SubmFile processed UserId is incorrect");

        LOG.info("Make sure AckFile is created");
        List<SubmAckFileLink> submAckFileLinks = rpmDao.getSubmAckFileLinkBySubmFileId(null, submFile.getSubmFileSeqId());
        Assert.assertEquals(submAckFileLinks.size(), 1,"Expected AckFile");
        Assert.assertTrue(waitForAckFileToBeProcessed(submAckFileLinks.get(0).getAckFileId(), QUEUE_WAIT_TIME_MS), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(submAckFileLinks.get(0).getAckFileId());
        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");
        Assert.assertEquals(ackFile.getNotes(), "Failed", "AckFile not is not correct");
        Assert.assertEquals(ackFile.getAckTypId(), MiscMap.ACK_TYP_LUXSCI, "AckFile type is not LUXSCI");

        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "luxsci" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+ackFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());
        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("To: "+emailAddr), "To address not found");
        Assert.assertFalse(ackContent.contains("Status: Delivered"), "Ack contains Delivered status");
        Assert.assertTrue(ackContent.contains("Status: Failed"), "Ack does not contain Failed status");
        Assert.assertTrue(ackContent.contains("Details: no.such.domain said 'Host unknown (Name server: no.such.domain.: host not found'"), "Error Details not foudn");
    }

    @Test(priority = 1, description = "Concord Client Letter from Local Storage")
    @Parameters({"ssoUsername", "ssoPassword", "correspFileName", "clnAbbrev", "faxNumber"})
    public void test_ENG101660_14(String ssoUsername, String ssoPassword, String correspFileName, String clnAbbrev, String faxNumber) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_14 ***");

        logIntoSso(ssoUsername, ssoPassword);
        List<CorrespFile> correspFiles = rpmDao.getCorrespFilesByFileName(correspFileName);
        Assert.assertEquals(correspFiles.size(), 1, "Could not identify CorrespFile");
        CorrespFile correspFile = correspFiles.get(0);

        SubmSvc submSvc = submissionDao.getSubmSvc(correspFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), 13, "Delivery method is not ConcordFax");
        Assert.assertNull(correspFile.getDocStoreId() , "EP Letter should not be in DocStore");

        Cln cln = rpmDao.getClnByClnAbbrev(null, clnAbbrev);
        Assert.assertEquals(cln.getCorrespFax(), faxNumber, "Client's Correspondence Fax is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Reset corresp file
        correspFile.setProcessedDt(null);
        correspFile.setAckFileId(0);
        correspFile.setIsProcessed(false);
        correspFile.setProcessedUserId(null);
        rpmDao.set(correspFile);
        logger.info("Clearing cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();
        // Wait for corresp file to be processed by FRE
        waitForCorrespFileToBeProcessedFileRouterEng(correspFile.getFileSeq(), QUEUE_WAIT_TIME_MS_CONCORD);
        correspFile = rpmDao.getCorrespFileByEPCorrespHist(correspFile.getFileSeq());
        LOG.info("Verifying corresp file, correspFileSeq="+correspFile.getFileSeq());
        Assert.assertTrue(correspFile.getIsProcessed(), "CorrespFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(correspFile.getProcessedDt().getTime()), new Date()), "CorrespFile processed date is incorrect");
        Assert.assertEquals(correspFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "CorrespFile processed user Id is incorrect");

        LOG.info("Make sure AckFile is created");
        Assert.assertTrue(correspFile.getAckFileId() > 0, "Ack file is not set");

        Assert.assertTrue(waitForAckFileToBeProcessed(correspFile.getAckFileId(), QUEUE_WAIT_TIME_MS_CONCORD), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(correspFile.getAckFileId());

        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");

        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "concord" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+correspFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());

        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Status: Success"), "Ack does not contain successfully delivered status");
    }

    @Test(priority = 1, description = "Concord Appeal Letter from DocStore (S3)")
    @Parameters({"ssoUsername", "ssoPassword", "correspFileName", "faxNumber"})
    public void test_ENG101660_15(String ssoUsername, String ssoPassword, String correspFileName, String faxNumber) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_15 ***");

        logIntoSso(ssoUsername, ssoPassword);
        List<CorrespFile> correspFiles = rpmDao.getCorrespFilesByFileName(correspFileName);
        Assert.assertEquals(correspFiles.size(), 1, "Could not identify CorrespFile");
        CorrespFile correspFile = correspFiles.get(0);

        SubmSvc submSvc = submissionDao.getSubmSvc(correspFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), 13, "Delivery method is not Concord");
        Assert.assertNotNull(correspFile.getDocStoreId() , "Could not identify DocStore record");
        DocStore docStore = rpmDao.getDocStore(correspFile.getDocStoreId());
        Assert.assertEquals(docStore.getDocStoreLocationId(), DOC_STORE_LOCATION_ID_S3, "DocStore location is incorrect");

        Pyr pyr = rpmDao.getPyrByPyrId(null, correspFile.getPyrId());
        Assert.assertEquals(pyr.getAppealFax(), faxNumber, "Payor's Appeal Fax is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Reset corresp file
        correspFile.setProcessedDt(null);
        correspFile.setAckFileId(0);
        correspFile.setIsProcessed(false);
        correspFile.setProcessedUserId(null);
        rpmDao.set(correspFile);

        // Wait for corresp file to be processed by FRE
        waitForCorrespFileToBeProcessedFileRouterEng(correspFile.getFileSeq(), QUEUE_WAIT_TIME_MS_CONCORD);
        correspFile = rpmDao.getCorrespFileByEPCorrespHist(correspFile.getFileSeq());
        LOG.info("Verifying corresp file, correspFileSeq="+correspFile.getFileSeq());
        Assert.assertTrue(correspFile.getIsProcessed(), "CorrespFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(correspFile.getProcessedDt().getTime()), new Date()), "CorrespFile processed date is incorrect");
        Assert.assertEquals(correspFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "CorrespFile processed user Id is incorrect");

        LOG.info("Make sure AckFile is created");
        Assert.assertTrue(correspFile.getAckFileId() > 0, "Ack file is not set");

        Assert.assertTrue(waitForAckFileToBeProcessed(correspFile.getAckFileId(), QUEUE_WAIT_TIME_MS_CONCORD), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(correspFile.getAckFileId());

        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");

        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "concord" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+correspFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());

        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Status: Success"), "Ack does not contain successfully delivered status");
    }


    @Test(priority = 1, description = "Concord Client Statement PDF from Local Storage")
    @Parameters({"ssoUsername", "ssoPassword", "submFileName", "clnAbbrev", "faxNumber"})
    public void test_ENG101660_16(String ssoUsername, String ssoPassword, String submFileName, String clnAbbrev, String faxNumber) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_16 ***");

        logIntoSso(ssoUsername, ssoPassword);
        SubmFile submFile = submissionDao.getSubmFileByFilename(submFileName);

        SubmSvc submSvc = submissionDao.getSubmSvc(submFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), 13, "Delivery method is not Concord");
        Assert.assertNull(submFile.getDocStoreSeqId() , "SubmFile should be in local storage");

        Cln cln = rpmDao.getClnByClnAbbrev(null, clnAbbrev);
        Assert.assertEquals(cln.getBilFax(), faxNumber, "Client's Billing Fax is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Delete AckFile links
        submissionDao.deleteSubmAckFileLinkBySubmFileSeqId(submFile.getSubmFileSeqId());

        // Reset SubmFile
        submFile.setProcessedDt(null);
        submFile.setIsProcessed(false);
        submFile.setProcessedUserId(null);
        rpmDao.set(submFile);

        // Wait for subm file to be processed
        waitForSubmFileToBeProcessed(submFile.getSubmFileSeqId(), true, true, QUEUE_WAIT_TIME_MS_CONCORD);
        submFile = rpmDao.getSubmFile(null, submFile.getSubmFileSeqId());
        LOG.info("Verifying SubmFile, submFileSeqId="+submFile.getSubmFileSeqId());
        Assert.assertTrue(submFile.getIsProcessed(), "SubmFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(submFile.getProcessedDt().getTime()), new Date()), "SubmFile processed date is incorrect");
        Assert.assertEquals(submFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "SubmFile processed UserId is incorrect");

        LOG.info("Make sure AckFile is created");
        List<SubmAckFileLink> submAckFileLinks = rpmDao.getSubmAckFileLinkBySubmFileId(null, submFile.getSubmFileSeqId());
        Assert.assertEquals(submAckFileLinks.size(), 1,"Expected AckFile");
        Assert.assertTrue(waitForAckFileToBeProcessed(submAckFileLinks.get(0).getAckFileId(), QUEUE_WAIT_TIME_MS_CONCORD), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(submAckFileLinks.get(0).getAckFileId());
        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");
        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "concord" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+ackFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());
        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Status: Success"), "Ack does not contain successfully delivered status");
    }

    @Test(priority = 1, description = "Concord Attachment PDF")
    @Parameters({"ssoUsername", "ssoPassword", "submFileName", "pyrAbbrev", "faxNumber"})
    public void test_ENG101660_17(String ssoUsername, String ssoPassword, String submFileName, String pyrAbbrev, String faxNumber) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_17 ***");

        logIntoSso(ssoUsername, ssoPassword);
        SubmFile submFile = submissionDao.getSubmFileByFilename(submFileName);

        SubmSvc submSvc = submissionDao.getSubmSvc(submFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), 13, "Delivery method is not Concord");
        Assert.assertNotNull(submFile.getDocStoreSeqId() , "SubmFile should be in doc storage");

        Pyr pyr = rpmDao.getPyrByPyrAbbrv(null, pyrAbbrev);
        Assert.assertEquals(pyr.getAttachFax(), faxNumber, "Payor's Attachment Fax is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Delete AckFile links
        submissionDao.deleteSubmAckFileLinkBySubmFileSeqId(submFile.getSubmFileSeqId());

        // Reset SubmFile
        submFile.setProcessedDt(null);
        submFile.setIsProcessed(false);
        submFile.setProcessedUserId(null);
        rpmDao.set(submFile);

        // Wait for subm file to be processed
        waitForSubmFileToBeProcessed(submFile.getSubmFileSeqId(), true, true, QUEUE_WAIT_TIME_MS_CONCORD);
        submFile = rpmDao.getSubmFile(null, submFile.getSubmFileSeqId());
        LOG.info("Verifying SubmFile, submFileSeqId="+submFile.getSubmFileSeqId());
        Assert.assertTrue(submFile.getIsProcessed(), "SubmFile is not flagged as processed");

        Assert.assertTrue(DateUtils.isSameDay(new Date(submFile.getProcessedDt().getTime()), new Date()), "SubmFile processed date is incorrect");

        Assert.assertEquals(submFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "SubmFile processed UserId is incorrect");

        LOG.info("Make sure AckFile is created");
        List<SubmAckFileLink> submAckFileLinks = rpmDao.getSubmAckFileLinkBySubmFileId(null, submFile.getSubmFileSeqId());
        Assert.assertEquals(submAckFileLinks.size(), 1,"Expected AckFile");
        Assert.assertTrue(waitForAckFileToBeProcessed(submAckFileLinks.get(0).getAckFileId(), QUEUE_WAIT_TIME_MS_CONCORD), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(submAckFileLinks.get(0).getAckFileId());
        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");
        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "concord" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        Assert.assertTrue(actualAckFile.exists(), "Failed to verify ack file exists, ackFileId="+ackFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        LOG.info("Verifying ack file content, ackFileId="+ackFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Status: Success"), "Ack does not contain successfully delivered status");
    }

    @Test(priority = 1, description = "Concord Client Statement Undeliverable Fax")
    @Parameters({"ssoUsername", "ssoPassword", "submFileName", "clnAbbrev", "faxNumber"})
    public void test_ENG101660_18(String ssoUsername, String ssoPassword, String submFileName, String clnAbbrev, String faxNumber) throws Exception
    {
        LOG.info("*** Starting - test_ENG101660_18 ***");

        logIntoSso(ssoUsername, ssoPassword);
        SubmFile submFile = submissionDao.getSubmFileByFilename(submFileName);

        SubmSvc submSvc = submissionDao.getSubmSvc(submFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), 13, "Delivery method is not Concord");
        Assert.assertNull(submFile.getDocStoreSeqId() , "SubmFile should be in local storage");

        Cln cln = rpmDao.getClnByClnAbbrev(null, clnAbbrev);
        logger.info("Verifying client's billing fax number, clnAbbrev="+cln.getClnAbbrev()+", faxNumber="+faxNumber);
        Assert.assertEquals(cln.getBilFax(), faxNumber, "Client's billing fax number is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Delete AckFile links
        submissionDao.deleteSubmAckFileLinkBySubmFileSeqId(submFile.getSubmFileSeqId());

        // Reset SubmFile
        submFile.setProcessedDt(null);
        submFile.setIsProcessed(false);
        submFile.setProcessedUserId(null);
        rpmDao.set(submFile);

        // Wait for subm file to be processed
        waitForSubmFileToBeProcessed(submFile.getSubmFileSeqId(), false, true, QUEUE_WAIT_TIME_MS_CONCORD);
        submFile = rpmDao.getSubmFile(null, submFile.getSubmFileSeqId());
        LOG.info("Verifying SubmFile, submFileSeqId="+submFile.getSubmFileSeqId());
        Assert.assertFalse(submFile.getIsProcessed(), "SubmFile is flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(submFile.getProcessedDt().getTime()), new Date()), "SubmFile processed date is incorrect");
        Assert.assertEquals(submFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "SubmFile processed UserId is incorrect");

        LOG.info("Make sure AckFile is created");
        List<SubmAckFileLink> submAckFileLinks = rpmDao.getSubmAckFileLinkBySubmFileId(null, submFile.getSubmFileSeqId());
        Assert.assertEquals(submAckFileLinks.size(), 1,"Expected AckFile");
        Assert.assertTrue(waitForAckFileToBeProcessed(submAckFileLinks.get(0).getAckFileId(), QUEUE_WAIT_TIME_MS_CONCORD), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(submAckFileLinks.get(0).getAckFileId());
        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");
        Assert.assertEquals(ackFile.getNotes(), "Blocked Number", "AckFile not is not correct");
        Assert.assertEquals(ackFile.getAckTypId(), 700, "AckFile type is not Concord");

        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "concord" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+ackFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());
        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertFalse(ackContent.contains("Status: Success"), "Ack contains success status");
        Assert.assertTrue(ackContent.contains("Error Message: Blocked Number"), "Ack does not contain error message");
    }


    @Test(priority = 1, description = "E-Fax Daily Weekly Notification Letter from DocStore")
    @Parameters({"ssoUsername", "ssoPassword", "correspFileName", "clnAbbrev", "faxNumber"})
    public void testDailyWeeklyNotificationLetter(String ssoUsername, String ssoPassword, String correspFileName, String clnAbbrev, String faxNumber) throws Exception
    {
        LOG.info("message=Starting test case testDailyWeeklyNotificationLetterS3, correspFileName="+correspFileName);

        logIntoSso(ssoUsername, ssoPassword);
        List<CorrespFile> correspFiles = rpmDao.getCorrespFilesByFileName(correspFileName);
        Assert.assertEquals(correspFiles.size(), 1, "Could not identify CorrespFile");
        CorrespFile correspFile = correspFiles.get(0);

        SubmSvc submSvc = submissionDao.getSubmSvc(correspFile.getSubmSvcSeqId());
        Assert.assertEquals(submSvc.getDelvryMthdTypId(), MiscMap.DELVRY_MTHD_TYP_EFAX, "Delivery method is not E-Fax");
        Assert.assertNotNull(correspFile.getDocStoreId() , "Could not identify DocStore record");
        DocStore docStore = rpmDao.getDocStore(correspFile.getDocStoreId());
//        Assert.assertEquals(docStore.getDocStoreLocationId(), DOC_STORE_LOCATION_ID_S3, "DocStore location is incorrect");


        Cln cln = rpmDao.getClnByClnAbbrev(null, clnAbbrev);
        Assert.assertEquals(cln.getCorrespFax(), faxNumber, "Client's Correspondence Fax is incorrect");

        logger.info("Clearing cache");
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        // Reset corresp file
        correspFile.setProcessedDt(null);
        correspFile.setAckFileId(0);
        correspFile.setIsProcessed(false);
        correspFile.setProcessedUserId(null);
        rpmDao.set(correspFile);

        // Wait for corresp file to be processed by FRE
        waitForCorrespFileToBeProcessedFileRouterEng(correspFile.getFileSeq(), QUEUE_WAIT_TIME_MS);
        correspFile = rpmDao.getCorrespFileByEPCorrespHist(correspFile.getFileSeq());
        LOG.info("Verifying corresp file, correspFileSeq="+correspFile.getFileSeq());
        Assert.assertTrue(correspFile.getIsProcessed(), "CorrespFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(correspFile.getProcessedDt().getTime()), new Date()), "CorrespFile processed date is incorrect");
        Assert.assertEquals(correspFile.getProcessedUserId(), FILE_ROUTER_ENGINE_USER, "CorrespFile processed user Id is incorrect");

        LOG.info("Make sure AckFile is created");
        Assert.assertTrue(correspFile.getAckFileId() > 0, "Ack file is not set");

        Assert.assertTrue(waitForAckFileToBeProcessed(correspFile.getAckFileId(), QUEUE_WAIT_TIME_MS), "Ack file was not processed");
        AckFile ackFile = rpmDao.getAckFile(correspFile.getAckFileId());

        Assert.assertTrue(ackFile.getIsProcessed(), "AckFile is not flagged as processed");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getProcessedDt().getTime()), new Date()), "AckFile processed date is incorrect");
        Assert.assertTrue(DateUtils.isSameDay(new Date(ackFile.getReceivedDt().getTime()), new Date()), "AckFile received date is incorrect");
        Assert.assertNotNull(ackFile.getValidationId(), "AckFile validation Id is not set");

        String fullPath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + "ack" + File.separator + "efax" + File.separator + ackFile.getFilename();
        File actualAckFile = new File(fullPath);
        LOG.info("Verifying ack file, ackFileId="+correspFile.getAckFileId()+", path="+actualAckFile.getAbsolutePath());
        Assert.assertTrue(actualAckFile.exists());

        LOG.info("Verifying ack file content");
        String ackContent = FileUtils.readFileToString(actualAckFile);
        Assert.assertTrue(ackContent.contains("Status Outcome: \"Success\""), "Ack does not contain status");
        Assert.assertTrue(ackContent.contains(faxNumber), "Ack does not contain fax number");
        Assert.assertTrue(ackContent.contains("Your transmission has completed."), "Ack does not contain completed transmission");
    }

    private boolean waitForCorrespFileToBeProcessedFileRouterEng(int epCorrespFileSeqId, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        long endTime = maxTime + startTime;
        boolean isProcessedFirstTime = rpmDao.getCorrespFileByEPCorrespHist(epCorrespFileSeqId).getProcessedUserId() == null;
        while (isProcessedFirstTime && System.currentTimeMillis() < endTime)
        {
            LOG.info("Waiting for Corresp File To Be Processed By File Router Eng 1 time - Processed User Id should not be equal to null, epCorrespFileSeqId=" + epCorrespFileSeqId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            isProcessedFirstTime = rpmDao.getCorrespFileByEPCorrespHist(epCorrespFileSeqId).getProcessedUserId() == null;
        }
        boolean isProcessedSecondTime = rpmDao.getCorrespFileByEPCorrespHist(epCorrespFileSeqId).getIsProcessed();
        startTime = System.currentTimeMillis();
        endTime = maxTime + startTime;
        while (!isProcessedFirstTime && System.currentTimeMillis() < endTime)
        {
            LOG.info("Waiting for Corresp File To Be Processed By File Router Eng, epCorrespFileSeqId=" + epCorrespFileSeqId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            isProcessedFirstTime = rpmDao.getCorrespFileByEPCorrespHist(epCorrespFileSeqId).getIsProcessed();
        }
        return isProcessedSecondTime;
    }

    private int waitForCorrespFileAckFile(int epCorrespFileSeqId, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        long endTime = maxTime + startTime;
        int ackFileId = rpmDao.getCorrespFileByEPCorrespHist(epCorrespFileSeqId).getAckFileId();
        while (ackFileId == 0 && System.currentTimeMillis() < endTime)
        {
            LOG.info("Waiting for Corresp File To get AckFile, epCorrespFileSeqId=" + epCorrespFileSeqId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            ackFileId = rpmDao.getCorrespFileByEPCorrespHist(epCorrespFileSeqId).getAckFileId();
        }
        return ackFileId;
    }

    private boolean waitForSubmFileToBeProcessed(int submFileSeqId, boolean checkProcessedFlag, boolean checkProcessedDt, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        long endTime = maxTime + startTime;
        SubmFile sf = rpmDao.getSubmFile(null, submFileSeqId);
        boolean isProcessed = (!checkProcessedFlag || sf.getIsProcessed()) && (!checkProcessedDt || sf.getProcessedDt() != null);
        while (!isProcessed && System.currentTimeMillis() < endTime)
        {
            LOG.info("Waiting for SubmFile To Be Processed, submFileSeqId=" + submFileSeqId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            sf = rpmDao.getSubmFile(null, submFileSeqId);
            isProcessed = (!checkProcessedFlag || sf.getIsProcessed()) && (!checkProcessedDt || sf.getProcessedDt() != null);
        }
        return isProcessed;
    }

    private boolean waitForAckFileToBeProcessed(int ackFileId, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isProcessed = false;
        while (!isProcessed && System.currentTimeMillis() < maxTime)
        {
            LOG.info("Waiting for ack file to be processed, ackFileId=" + ackFileId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            try
            {
                AckFile ackFile = rpmDao.getAckFile(ackFileId);
                isProcessed = ackFile.getIsProcessed();
            }
            catch (XifinDataNotFoundException e)
            {
                // Just wait it out...
            }
        }
        return isProcessed;
    }

    private void waitForSubmAckFileLinks(int submFileSeqId, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        long endTime = maxTime + startTime;
        List<SubmAckFileLink> submAckFileLinks = rpmDao.getSubmAckFileLinkBySubmFileId(null, submFileSeqId);
        while (submAckFileLinks.isEmpty() && System.currentTimeMillis() < endTime)
        {
            LOG.info("Waiting for SubmAckFileLink record, submFileSeqId=" + submFileSeqId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            submAckFileLinks = rpmDao.getSubmAckFileLinkBySubmFileId(null, submFileSeqId);
        }
    }
}
