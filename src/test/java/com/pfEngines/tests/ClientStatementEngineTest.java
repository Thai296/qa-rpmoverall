package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.submFile.SubmFile;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.FileManipulation;
import com.xifin.xap.utils.XifinAdminUtils;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;

public class ClientStatementEngineTest extends SeleniumBaseTest {
    @Test(priority = 1, description = "Generate PDF file format Client Statement")
    @Parameters({"ssoUsername","ssoPassword","submFileSeqId"})
    public void pdfClientStatement(String ssoUsername, String ssoPassword, int submFileSeqId) throws Exception {
        FileManipulation fileManipulation = new FileManipulation(driver);
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);

        logger.info("*** Step 1 Action - Delete any existing file for Subm File");
        SubmFile submFile = submissionDao.getSubmFileBySubmFileSeqId(submFileSeqId);
        String filePath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + submFile.getDir() + File.separator;
        String fileName = submFile.getFilename();
        File statement = new File(filePath + fileName);
        fileManipulation.deleteFile(filePath,fileName);

        logger.info("*** Step 1 Expected Result - Verify the file is deleted");
        Assert.assertFalse(statement.exists(), "        Client Statement file: " + fileName + " still exists in " + filePath+ " folder.");

        logger.info("*** Step 2 Action - set B_EGATE_PROCESSED to 0");
        daoManagerXifinRpm.setBEGateProcessedFROMSUBMFILEBySubmFileSeqId(String.valueOf(submFileSeqId),testDb);

        logger.info("*** Step 3 Action - Run ClnStmt Engine");
        xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword, null, "ClnStmtEngine", "SSO_APP_STAGING", false);
        Thread.sleep(5000);

        logger.info("*** Step 3 Expecteds Result - File is created in \\\\a3unity01-mp\\cnfs01\\qa07\\files\\clientsubm\\(Refer to following tables SUBM_FILE.DIR and SUBM_FILE.FILENAME)");
        fileName = submFile.getFilename();
        filePath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + submFile.getDir() + File.separator;
        statement = new File(filePath + fileName);
        Assert.assertTrue(isFileExists(statement, 5), "        Client Statement file: " + fileName + " should be generated in " + filePath + " folder.");

        logger.info("*** Step 3 Expected Result - Verify the file is Pdf file format");
        Assert.assertTrue(fileName.contains(".pdf"));
    }
    @Test(priority = 1, description = "Generate Excel file format Client Statement")
    @Parameters({"ssoUsername","ssoPassword","submFileSeqId"})
    public void excelClientStatement(String ssoUsername, String ssoPassword, int submFileSeqId) throws Exception {
        FileManipulation fileManipulation = new FileManipulation(driver);
        XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);

        logger.info("*** Step 1 Action - Delete any existing file for Subm File");
        SubmFile submFile = submissionDao.getSubmFileBySubmFileSeqId(submFileSeqId);
        String filePath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + submFile.getDir() + File.separator;
        String fileName = submFile.getFilename();
        File statement = new File(filePath + fileName);
        fileManipulation.deleteFile(filePath,fileName);

        logger.info("*** Step 1 Expected Result - Verify the file is deleted");
        Assert.assertFalse(statement.exists(), "        Client Statement file: " + fileName + " still exists in " + filePath+ " folder.");

        logger.info("*** Step 2 Action - set B_EGATE_PROCESSED to 0");
        daoManagerXifinRpm.setBEGateProcessedFROMSUBMFILEBySubmFileSeqId(String.valueOf(submFileSeqId),testDb);

        logger.info("*** Step 3 Action - Run ClnStmt Engine");
        xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword, null, "ClnStmtEngine", "SSO_APP_STAGING", false);
        Thread.sleep(5000);

        logger.info("*** Step 3 Expected Results - File is created in \\\\a3unity01-mp\\cnfs01\\qa07\\files\\clientsubm\\(Refer to following tables SUBM_FILE.DIR and SUBM_FILE.FILENAME)");
        fileName = submFile.getFilename();
        filePath = FileManipulation.getBaseDir() + File.separator + config.getProperty(PropertyMap.ORGALIAS) + File.separator + "files" + File.separator + submFile.getDir() + File.separator;
        statement = new File(filePath + fileName);
        Assert.assertTrue(isFileExists(statement, 5), "        Client Statement file: " + fileName + " should be generated in " + filePath + " folder.");

        logger.info("*** Step 3 Expected Results - Verify the file is Excel file format");
        Assert.assertTrue(fileName.contains(".xlsx"));
    }
}
