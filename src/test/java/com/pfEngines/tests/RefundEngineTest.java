package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accnRefund.AccnRefund;
import com.mbasys.mars.ejb.entity.clnRefund.ClnRefund;
import com.mbasys.mars.ejb.entity.pmtSuspRefund.PmtSuspRefund;
import com.mbasys.mars.ejb.entity.refundBatch.RefundBatch;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.xifin.qa.config.PropertyMap;
import com.xifin.util.Money;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.ConvertUtil;
import com.xifin.utils.FileManipulation;
import com.xifin.utils.TimeStamp;
import domain.engines.refund.GenericRefundTxtFile;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;


public class RefundEngineTest extends SeleniumBaseTest {

    private TimeStamp timeStamp;
    private FileManipulation fileManipulation;
    private ConvertUtil convertUtil;
    private static final SimpleDateFormat DATE_FORMAT_MMDDYYYY_WITH_SLASH = new SimpleDateFormat("MM/dd/yyyy");

    @BeforeSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "xapEnv", "eType", "engConfigDB", "disableBrowserPlugins"})
    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, String xapEnv, String eType, String engConfigDB, @Optional String disableBrowserPlugins) {
        try {
            logger.info("Running BeforeSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            setUpTestCondition();
            logIntoSso(ssoUsername, ssoPassword);
            clearDataCache();
        } catch (Exception e) {
            Assert.fail("Error running BeforeSuite", e);
        } finally {
            quitWebDriver();
        }
    }

    @Test(priority = 1, description = "Client Refund-Generate txt file")
    @Parameters({"seqId", "formatType"})
    public void testPFER_623(String seqId, String formatType) throws Exception {
        logger.info("*** Testing - testPFER_623 ***");

        timeStamp = new TimeStamp(driver);
        fileManipulation = new FileManipulation(driver);

        logger.info("*** Actions - Set SS#7104 = 1 (Print Refund File) in DB");
        updateSystemSetting(SystemSettingMap.SS_GENERATE_REFUND_FILE_REFUND_CHECK_FILE, "Print Refund File", "1");

        logger.info("*** Actions - Set cln_refund.FK_REFUND_BATCH_ID = 0, b_posted = 1, b_approved = 1 in DB");
        ClnRefund clnRefund = clientDao.getClnRefundBySeqId(Integer.valueOf(seqId));
        clnRefund.setRefundBatchId(0);
        clnRefund.setIsApproved(true);
        clnRefund.setIsPosted(true);
        clientDao.setClnRefund(clnRefund);

        logger.info("*** Actions: - Run PF Refund Engine");
        waitForSeqIdToBeOutOfRefundQueue(Integer.parseInt(seqId), 1, QUEUE_WAIT_TIME_MS);

        logger.info("*** Expected Results: - Verify that cln_refund.FK_REFUND_BATCH_ID is being assigned with a new batch id ");
        clnRefund = clientDao.getClnRefundBySeqId(Integer.valueOf(seqId));
        assertNotEquals(clnRefund.getRefundBatchId(), 0, "       cln_refund.FK_REFUND_BATCH_ID should be assigned with a new batch id.");

        logger.info("*** Actions - Set refund_batch.b_posted = 1 in DB");
        int refundBatchSeqId = clnRefund.getRefundBatchId();
        RefundBatch refundBatch = paymentDao.getRefundBatchBySeqId(refundBatchSeqId);
        refundBatch.setIsPosted(true);
        paymentDao.setRefundBatch(refundBatch);

        //refund_batch.create_dt = currentDate
        String currentDate = timeStamp.getCurrentDate();
        assertEquals(refundBatch.getCreateDt(), new java.sql.Date(DATE_FORMAT_MMDDYYYY_WITH_SLASH.parse(currentDate).getTime()));

        logger.info("*** Actions - Set cln_refund.b_posted = 1 in DB");
        clnRefund = clientDao.getClnRefundBySeqId(Integer.valueOf(seqId));
        clnRefund.setIsPosted(true);
        clientDao.setClnRefund(clnRefund);

        logger.info("*** Actions: - Run PF Refund Engine again");
        waitForRefundEngineToProcessRefundBatch(refundBatchSeqId, QUEUE_WAIT_TIME_MS);

        logger.info("*** Expected Results: - Verify that the Refund Batch has been processed");
        refundBatch = paymentDao.getRefundBatchBySeqId(refundBatchSeqId);
        String fileName = refundBatch.getFilename().split("/")[1];
        assertNotNull(fileName, "       refund_batch.filename should be generated.");
        assertTrue(refundBatch.isBatchProcessed, "       refund_batch.B_BATCH_PROCESSED = 1");
        assertNotNull(refundBatch.getCorrespFileSeqId(), "       refund_batch.FK_CORRESP_FILE_SEQ_ID should be generated.");

        logger.info("*** Expected Results: - Verify that the Refund file has been generated under files/refund folder");
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File f = new File(dirBase + fileName);
        System.out.println("File Path = " + dirBase + fileName);//Debug Info
        Assert.assertTrue(isFileExists(f, 5), "        Refund file: " + fileName + " should be generated under " + dirBase + " folder.");

        logger.info("*** Expected Results: - Verify that the proper contents are generated in the txt file");
        String payeeZipId = clnRefund.getPayeeZipId().trim();
        String payeeAddr1 = clnRefund.getPayeeAddr1().trim();
        String payeeAddr2 = clnRefund.getPayeeAddr2().trim();
        String payeeFNm = clnRefund.getPayeeFNm().trim();
        String payeeLNm = clnRefund.getPayeeLNm().trim();
        String payeeFullName = payeeLNm + "^" + payeeFNm;
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String amtInPdf = formatter.format(clnRefund.getAmtAsMoney().floatValue());
        String checkDt = systemDao.getSystemSetting(SystemSettingMap.SS_SYSTEM_OVERRIDE_DATE).getDataValue();
        Date refundDate = clnRefund.getRefundDt();
        String pyrAbbrev = "C";
        String accnId = clientDao.getClnByClnId(clnRefund.getClnId()).getClnAbbrev();
        Money amtInTxt = new Money(String.valueOf(clnRefund.getAmtAsMoney()));

        List<Object> expectedRefundContentList = new ArrayList();
        expectedRefundContentList.add(pyrAbbrev);//0
        expectedRefundContentList.add(payeeFullName);//1
        expectedRefundContentList.add(payeeAddr1);//2
        expectedRefundContentList.add(payeeAddr2);//3
        expectedRefundContentList.add(payeeZipId);//4
        expectedRefundContentList.add(refundDate);//5
        expectedRefundContentList.add(accnId);//6
        expectedRefundContentList.add(amtInTxt);//7
        expectedRefundContentList.add(payeeFNm);//8
        expectedRefundContentList.add(payeeLNm);//9
        expectedRefundContentList.add(amtInPdf);//10
        expectedRefundContentList.add(checkDt);//11

        //Expected file contents
        GenericRefundTxtFile expectedGenericRefundTxtFile = setExpectedGenericRefundTxtFile(expectedRefundContentList);

        //Actual file contents
        GenericRefundTxtFile actualGenericRefundTxtFile = setActualGenericRefundTxtFile(f);

        assertEquals(actualGenericRefundTxtFile, expectedGenericRefundTxtFile);

    }

    @Test(priority = 1, description = "Accession Refund-Generate pdf file")
    @Parameters({"seqId", "formatType"})
    public void testPFER_621(String seqId, String formatType) throws Exception {
        logger.info("*** Testing - testPFER_621 ***");

        timeStamp = new TimeStamp(driver);
        fileManipulation = new FileManipulation(driver);
        convertUtil = new ConvertUtil();

        logger.info("*** Actions - Set SS#7104 = 2 (Print Refund Checks) in DB");
        updateSystemSetting(SystemSettingMap.SS_GENERATE_REFUND_FILE_REFUND_CHECK_FILE, "Print Refund Checks", "2");

        logger.info("*** Actions - Set accn_refund.FK_REFUND_BATCH_ID = 0, b_posted = 1, b_approved = 1 in DB");
        AccnRefund accnRefund = accessionDao.getAccnRefundBySeqId(Integer.valueOf(seqId));
        accnRefund.setRefundBatchId(0);
        accnRefund.setIsApproved(true);
        accnRefund.setIsPosted(true);
        accessionDao.setAccnRefund(accnRefund);

        logger.info("*** Actions: - Run PF Refund Engine");
        waitForSeqIdToBeOutOfRefundQueue(Integer.parseInt(seqId), 2, QUEUE_WAIT_TIME_MS);

        logger.info("*** Expected Results: - Verify that accn_refund.FK_REFUND_BATCH_ID is being assigned with a new batch id ");
        accnRefund = accessionDao.getAccnRefundBySeqId(Integer.valueOf(seqId));
        assertNotEquals(accnRefund.getRefundBatchId(), 0, "       accn_refund.FK_REFUND_BATCH_ID should be assigned with a new batch id.");

        logger.info("*** Actions - Set refund_batch.b_posted = 1 in DB");
        int refundBatchSeqId = accnRefund.getRefundBatchId();
        RefundBatch refundBatch = paymentDao.getRefundBatchBySeqId(refundBatchSeqId);
        refundBatch.setIsPosted(true);
        paymentDao.setRefundBatch(refundBatch);

        //refund_batch.create_dt = currentDate
        String currentDate = timeStamp.getCurrentDate();
        assertEquals(refundBatch.getCreateDt(), new java.sql.Date(DATE_FORMAT_MMDDYYYY_WITH_SLASH.parse(currentDate).getTime()));

        logger.info("*** Actions - Set accn_refund.b_posted = 1 in DB");
        accnRefund = accessionDao.getAccnRefundBySeqId(Integer.valueOf(seqId));
        accnRefund.setIsPosted(true);
        accessionDao.setAccnRefund(accnRefund);

        logger.info("*** Actions: - Run PF Refund Engine again");
        waitForRefundEngineToProcessRefundBatch(refundBatchSeqId, QUEUE_WAIT_TIME_MS);

        logger.info("*** Expected Results: - Verify that the Refund Batch has been processed");
        refundBatch = paymentDao.getRefundBatchBySeqId(refundBatchSeqId);
        String fileName = refundBatch.getFilename().split("/")[1];
        assertNotNull(fileName, "       refund_batch.filename should be generated.");
        assertTrue(refundBatch.isBatchProcessed, "       refund_batch.B_BATCH_PROCESSED = 1");
        assertNotNull(refundBatch.getCorrespFileSeqId(), "       refund_batch.FK_CORRESP_FILE_SEQ_ID should be generated.");

        logger.info("*** Expected Results: - Verify that the Refund file has been generated under files/refund folder");
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File f = new File(dirBase + fileName);
        System.out.println("File Path = " + dirBase + fileName);//Debug Info
        Assert.assertTrue(isFileExists(f, 5), "        Refund file: " + fileName + " should be generated under " + dirBase + " folder.");

        logger.info("*** Expected Results: - Verify that the proper contents are generated in the PDF file");
        String payeeZipId = accnRefund.getPayeeZipId().trim();
        String payeeAddr1 = accnRefund.getPayeeAddr1().trim();
        String payeeAddr2 = accnRefund.getPayeeAddr2().trim();
        String payeeFNm = accnRefund.getPayeeFNm().trim();
        String payeeLNm = accnRefund.getPayeeLNm().trim();
        String payeeFullName = payeeLNm + "^" + payeeFNm;
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String amtInPdf = formatter.format(accnRefund.getAmtAsMoney().floatValue());
        String checkDt = systemDao.getSystemSetting(SystemSettingMap.SS_SYSTEM_OVERRIDE_DATE).getDataValue();
        Date refundDate = accnRefund.getRefundDt();
        String pyrAbbrev = payorDao.getPyrByPyrId(accnRefund.getPyrId()).getPyrAbbrv();
        String accnId = accnRefund.getAccnId();
        Money amtInTxt = new Money(String.valueOf(accnRefund.getAmtAsMoney()));

        List<Object> expectedRefundContentList = new ArrayList();
        expectedRefundContentList.add(pyrAbbrev);//0
        expectedRefundContentList.add(payeeFullName);//1
        expectedRefundContentList.add(payeeAddr1);//2
        expectedRefundContentList.add(payeeAddr2);//3
        expectedRefundContentList.add(payeeZipId);//4
        expectedRefundContentList.add(refundDate);//5
        expectedRefundContentList.add(accnId);//6
        expectedRefundContentList.add(amtInTxt);//7
        expectedRefundContentList.add(payeeFNm);//8
        expectedRefundContentList.add(payeeLNm);//9
        expectedRefundContentList.add(amtInPdf);//10
        expectedRefundContentList.add(checkDt);//11

        verifyGenericRefundPdfFile(1, 1, dirBase + fileName, expectedRefundContentList);

    }


    @Test(priority = 1, description = "Suspense Pmt Refund-Generate both txt and pdf files")
    @Parameters({"seqId", "formatType"})
    public void testPFER_628(String seqId, String formatType) throws Exception {
        logger.info("*** Testing - testPFER_628 ***");


        timeStamp = new TimeStamp(driver);
        fileManipulation = new FileManipulation(driver);

        logger.info("*** Actions - Set SS#7104 = 3 (Print both Refund File and Refund Checks) in DB");
        updateSystemSetting(SystemSettingMap.SS_GENERATE_REFUND_FILE_REFUND_CHECK_FILE, "Print both Refund File and Refund Checks", "3");

        logger.info("*** Actions - Set pmt_susp_refund.FK_REFUND_BATCH_ID = 0, b_posted = 1, b_approved = 1 in DB");
        PmtSuspRefund pmtSuspRefund = paymentDao.getPmtSuspRefundBySeqId(Integer.valueOf(seqId));
        pmtSuspRefund.setRefundBatchId(0);
        pmtSuspRefund.setIsApproved(true);
        pmtSuspRefund.setIsPosted(true);
        paymentDao.setPmtSuspRefund(pmtSuspRefund);

        logger.info("*** Actions: - Run PF Refund Engine");
        waitForSeqIdToBeOutOfRefundQueue(Integer.parseInt(seqId), 3, QUEUE_WAIT_TIME_MS);
        logger.info("*** Expected Results: - Verify that pmt_susp_refund.FK_REFUND_BATCH_ID is being assigned with a new batch id ");
        pmtSuspRefund = paymentDao.getPmtSuspRefundBySeqId(Integer.valueOf(seqId));
        assertNotEquals(pmtSuspRefund.getRefundBatchId(), 0, "       pmt_susp_refund.FK_REFUND_BATCH_ID should be assigned with a new batch id.");

        logger.info("*** Actions - Set refund_batch.b_posted = 1 in DB");
        int refundBatchSeqId = pmtSuspRefund.getRefundBatchId();
        RefundBatch refundBatch = paymentDao.getRefundBatchBySeqId(refundBatchSeqId);
        refundBatch.setIsPosted(true);
        paymentDao.setRefundBatch(refundBatch);

        String currentDate = timeStamp.getCurrentDate();
        assertEquals(refundBatch.getCreateDt(), new java.sql.Date(DATE_FORMAT_MMDDYYYY_WITH_SLASH.parse(currentDate).getTime()));

        logger.info("*** Actions - Set pmt_susp_refund.b_posted = 1 in DB");
        pmtSuspRefund = paymentDao.getPmtSuspRefundBySeqId(Integer.valueOf(seqId));
        pmtSuspRefund.setIsPosted(true);
        paymentDao.setPmtSuspRefund(pmtSuspRefund);

        logger.info("*** Actions: - Run PF Refund Engine again");
        waitForRefundEngineToProcessRefundBatch(refundBatchSeqId, QUEUE_WAIT_TIME_MS);

        logger.info("*** Expected Results: - Verify that the Refund Batch has been processed");
        refundBatch = paymentDao.getRefundBatchBySeqId(refundBatchSeqId);
        String fileNameTxt = refundBatch.getFilename().split("/")[1];
        assertNotNull(fileNameTxt, "       refund_batch.filename should be generated.");
        assertTrue(refundBatch.isBatchProcessed, "       refund_batch.B_BATCH_PROCESSED = 1");
        assertNotNull(refundBatch.getCorrespFileSeqId(), "       refund_batch.FK_CORRESP_FILE_SEQ_ID should be generated.");

        logger.info("*** Expected Results: - Verify that both TXT and PDF Refund files have been generated under files/refund folder");
        //File path
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        File f1 = new File(dirBase + fileNameTxt);
        System.out.println("File Path = " + dirBase + fileNameTxt);//Debug Info
        Assert.assertTrue(isFileExists(f1, 5), "        Refund file: " + fileNameTxt + " should be generated under " + dirBase + " folder.");

        String fileNamePdf = fileNameTxt.split("[.]")[0] + ".pdf";
        File f2 = new File(dirBase + fileNamePdf);
        System.out.println("File Path = " + dirBase + fileNamePdf);//Debug Info
        Assert.assertTrue(isFileExists(f2, 5), "        Refund file: " + fileNamePdf + " should be generated under " + dirBase + " folder.");

        logger.info("*** Expected Results: - Verify that the proper contents are generated in the txt file");
        String payeeZipId = pmtSuspRefund.getPayeeZipId().trim();
        String payeeAddr1 = pmtSuspRefund.getPayeeAddr1().trim();
        String payeeAddr2 = pmtSuspRefund.getPayeeAddr2().trim();
        String payeeFNm = pmtSuspRefund.getPayeeFNm().trim();
        String payeeLNm = pmtSuspRefund.getPayeeLNm().trim();
        String payeeFullName = payeeLNm + "^" + payeeFNm;
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String amtInPdf = formatter.format(pmtSuspRefund.getAmtAsMoney().floatValue());
        String checkDt = systemDao.getSystemSetting(SystemSettingMap.SS_SYSTEM_OVERRIDE_DATE).getDataValue();
        Date refundDate = pmtSuspRefund.getRefundDt();
        String pyrAbbrev = payorDao.getPyrByPyrId(pmtSuspRefund.getPyrId()).getPyrAbbrv();
        String accnId = paymentDao.getPmtSuspBySuspId(pmtSuspRefund.getSuspId()).getAccnId();
        Money amtInTxt = new Money(String.valueOf(pmtSuspRefund.getAmtAsMoney()));

        List<Object> expectedRefundContentList = new ArrayList();
        expectedRefundContentList.add(pyrAbbrev);//0
        expectedRefundContentList.add(payeeFullName);//1
        expectedRefundContentList.add(payeeAddr1);//2
        expectedRefundContentList.add(payeeAddr2);//3
        expectedRefundContentList.add(payeeZipId);//4
        expectedRefundContentList.add(refundDate);//5
        expectedRefundContentList.add(accnId);//6
        expectedRefundContentList.add(amtInTxt);//7
        expectedRefundContentList.add(payeeFNm);//8
        expectedRefundContentList.add(payeeLNm);//9
        expectedRefundContentList.add(amtInPdf);//10
        expectedRefundContentList.add(checkDt);//11

        //Expected file contents
        GenericRefundTxtFile expectedGenericRefundTxtFile = setExpectedGenericRefundTxtFile(expectedRefundContentList);

        //Actual file contents
        GenericRefundTxtFile actualGenericRefundTxtFile = setActualGenericRefundTxtFile(f1);

        assertEquals(actualGenericRefundTxtFile, expectedGenericRefundTxtFile);

        logger.info("*** Expected Results: - Verify that the proper contents are generated in the PDF file");
        verifyGenericRefundPdfFile(1, 1, dirBase + fileNamePdf, expectedRefundContentList);

    }


    private void verifyGenericRefundPdfFile(int startPage, int endPage, String filePath, List<Object> expectedRefundContentList) throws Exception {
        convertUtil = new ConvertUtil();
        String pdfContentsStr = convertUtil.getTextFromPdf(1, 1, filePath);

        assertTrue(pdfContentsStr.contains(String.valueOf(expectedRefundContentList.get(8))), "        PDF file " + filePath + " should contains Payee FName " + expectedRefundContentList.get(8));
        assertTrue(pdfContentsStr.contains(String.valueOf(expectedRefundContentList.get(9))), "        PDF file " + filePath + " should contains Payee LName " + expectedRefundContentList.get(9));
        assertTrue(pdfContentsStr.contains(String.valueOf(expectedRefundContentList.get(2))), "        PDF file " + filePath + " should contains Payee Addr1 " + expectedRefundContentList.get(2));
        assertTrue(pdfContentsStr.contains(String.valueOf(expectedRefundContentList.get(3))), "        PDF file " + filePath + " should contains Payee Addr2 " + expectedRefundContentList.get(3));
        assertTrue(pdfContentsStr.contains(String.valueOf(expectedRefundContentList.get(4)).substring(1, 4)), "        PDF file " + filePath + " should contains Payee Zip " + String.valueOf(expectedRefundContentList.get(4)).substring(1, 4));
        if (!(expectedRefundContentList.get(11) == null))
            assertTrue(pdfContentsStr.contains(String.valueOf(expectedRefundContentList.get(11))), "        PDF file " + filePath + " should contains Check Date " + expectedRefundContentList.get(11));
        assertTrue(pdfContentsStr.contains(String.valueOf(expectedRefundContentList.get(6))), "        PDF file " + filePath + " should contains Accession ID " + expectedRefundContentList.get(6));
        assertTrue(pdfContentsStr.contains(String.valueOf(expectedRefundContentList.get(10))), "        PDF file " + filePath + " should contains Refund Amount" + expectedRefundContentList.get(10));
    }

    private GenericRefundTxtFile setExpectedGenericRefundTxtFile(List<Object> list) throws ParseException {
        //Expected file contents
        GenericRefundTxtFile expectedGenericRefundTxtFile = new GenericRefundTxtFile();

        expectedGenericRefundTxtFile.setPayeeId(String.valueOf(list.get(0)).trim());
        expectedGenericRefundTxtFile.setPayeeFullName(String.valueOf(list.get(1)));
        expectedGenericRefundTxtFile.setAddress1(String.valueOf(list.get(2)));
        expectedGenericRefundTxtFile.setAddress2(String.valueOf(list.get(3)));
        expectedGenericRefundTxtFile.setZip(String.valueOf(list.get(4)));

        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = new java.sql.Date(fm.parse(String.valueOf(list.get(5))).getTime());
        Date refundDate = new java.sql.Date(DATE_FORMAT_MMDDYYYY_WITH_SLASH.parse(DATE_FORMAT_MMDDYYYY_WITH_SLASH.format(dt)).getTime());
        expectedGenericRefundTxtFile.setRefundDate(refundDate);

        expectedGenericRefundTxtFile.setAccessionId(String.valueOf(list.get(6)));

        Money refundAmt = new Money(String.valueOf(list.get(7)));
        expectedGenericRefundTxtFile.setRefundAmount(refundAmt);

        logger.info("       Expected file contents: " + expectedGenericRefundTxtFile.toString());
        return expectedGenericRefundTxtFile;
    }

    private GenericRefundTxtFile setActualGenericRefundTxtFile(File f) throws ParseException, IOException {
        //Actual file contents
        List<String> outputFileList = FileUtils.readLines(f, "UTF-8");
        GenericRefundTxtFile actualGenericRefundTxtFile = new GenericRefundTxtFile();
        for (String line : outputFileList) {
            if (!line.contains("Payee ID")) { //Skip the 1st line
                System.out.println("line=" + line);
                String[] split = line.split(",");

                actualGenericRefundTxtFile.setPayeeId(split[0]);
                actualGenericRefundTxtFile.setPayeeFullName(split[1]);
                actualGenericRefundTxtFile.setAddress1(split[2]);
                actualGenericRefundTxtFile.setAddress2(split[3]);
                actualGenericRefundTxtFile.setZip(split[6]);
                actualGenericRefundTxtFile.setRefundDate(new java.sql.Date(DATE_FORMAT_MMDDYYYY_WITH_SLASH.parse(split[7]).getTime()));
                actualGenericRefundTxtFile.setAccessionId(split[8]);
                actualGenericRefundTxtFile.setRefundAmount(new Money(split[9]));

                break;
            }
        }

        logger.info("       Actual file contents: " + actualGenericRefundTxtFile.toString());
        return actualGenericRefundTxtFile;
    }

    private void setUpTestCondition() throws Exception {
        logger.info("*** Actions: - Set up related System Settings in DB");
        updateSystemSetting(SystemSettingMap.REFUND_ALLOW_APPROVE_OWN_REFUNDS, "True", "1");
        updateSystemSetting(SystemSettingMap.REFUND_FILE_FORMAT, null, null);
        updateSystemSetting(SystemSettingMap.REFUND_BATCH_FILE_GENERATION, "True", "1");
        updateSystemSetting(SystemSettingMap.REFUND_BATCH_FILE_GENERATION_DAYS, null, null);
        updateSystemSetting(SystemSettingMap.SS_GENERATE_REFUND_FILE_REFUND_CHECK_FILE, "Print Refund File", "1");
        updateSystemSetting(SystemSettingMap.SS_REFUND_FILE_DELIMITERS, null, null);

        logger.info("*** Actions: - Delete all the files under /files/refund/ folder");
        FileManipulation fileManipulation = new FileManipulation(driver);
        String dirBase = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), "refundEngine");
        try {
            FileUtils.cleanDirectory(new File(dirBase));
        } catch (Exception e) {
            logger.info("       Folder " + dirBase + " does not exist. No need to clear.");
        }
    }

    public void waitForSeqIdToBeOutOfRefundQueue(int seqId, int typ, long maxTime) throws Exception {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        if (typ == 1) {
            boolean isInQueue = clientDao.getCountOfClnRefundBySeqIdWhereBatchIdIsZero(seqId);
            while (isInQueue && System.currentTimeMillis() < maxTime) {
                logger.info("Waiting for ClnRefund to be processed, seqId=" + seqId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
                Thread.sleep(QUEUE_POLL_TIME_MS);
                isInQueue = clientDao.getCountOfClnRefundBySeqIdWhereBatchIdIsZero(seqId);
            }
        }
        if (typ == 2) {
            boolean isInQueue = accessionDao.getCountOfAccnRefundBySeqIdWhereBatchIdIsZero(seqId);
            while (isInQueue && System.currentTimeMillis() < maxTime) {
                logger.info("Waiting for AccnRefund to be processed, seqId=" + seqId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
                Thread.sleep(QUEUE_POLL_TIME_MS);
                isInQueue = accessionDao.getCountOfAccnRefundBySeqIdWhereBatchIdIsZero(seqId);
            }
        }
        if (typ == 3) {
            boolean isInQueue = paymentDao.getCountOfPmtSuspRefundBySeqIdWhereBatchIdIsZero(seqId);
            while (isInQueue && System.currentTimeMillis() < maxTime) {
                logger.info("Waiting for PmtSuspRefund to be processed, seqId=" + seqId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
                Thread.sleep(QUEUE_POLL_TIME_MS);
                isInQueue = paymentDao.getCountOfPmtSuspRefundBySeqIdWhereBatchIdIsZero(seqId);
            }
        }
    }

    public void waitForRefundEngineToProcessRefundBatch(int seqId, long maxTime) throws Exception {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean isInQueue = paymentDao.isRefundBatchInRefundEngineQueue(seqId);

        while (isInQueue && System.currentTimeMillis() < maxTime) {
            logger.info("Waiting for RefundBatch to be processed, seqId=" + seqId + ", elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            isInQueue = paymentDao.isRefundBatchInRefundEngineQueue(seqId);
        }
    }
}
