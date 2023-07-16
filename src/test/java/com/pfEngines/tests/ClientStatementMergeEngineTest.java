package com.pfEngines.tests;


import com.mbasys.mars.persistance.SystemSettingMap;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.FileManipulation;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;

public class ClientStatementMergeEngineTest extends SeleniumBaseTest
{
    @BeforeSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "xapEnv", "eType1", "eType2", "engConfigDB", "disableBrowserPlugins"})
    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String xapEnv, String eType1, String eType2, String engConfigDB, @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running BeforeSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            updateSystemSetting(SystemSettingMap.SS_SYSTEM_OVERRIDE_DATE, "05/01/2015", "05/01/2015");
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

    @AfterSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "xapEnv", "eType1", "eType2", "engConfigDB", "disableBrowserPlugins"})
    public void afterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String xapEnv, String eType1, String eType2, String engConfigDB, @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running AfterSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            updateSystemSetting(SystemSettingMap.SS_SYSTEM_OVERRIDE_DATE, null, null);
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

    @Test(priority = 1, description = "Generate client statement merge file containing multiple submission services")
    @Parameters({"formatType", "mergedFileName"})
    public void testPFER_523(String formatType, String mergedFileName) throws Exception
    {
        FileManipulation fileManipulation = new FileManipulation(driver);

        logger.info("*** Step 1 Actions: - Setup test data in DB");

        String condition = "pk_subm_file_seq_id in (111177704, 111177700, 111177705, 111177699, 111177701, 111177702, 111177703)";
        List<List<String>> individualFileList = daoManagerPlatform.getIndividualFilesFromSubmFile(condition, testDb);

        // Delete all the individual files generated before
        String clnSubmFilePath = fileManipulation.getDir(config.getProperty(PropertyMap.ORGALIAS), formatType);
        System.out.println("clnSubmFilePath = " + clnSubmFilePath);//Debug Info
        String fileDir = individualFileList.get(0).get(7);
        String dir = fileDir.replaceAll("[^0-9]", "").trim();
        String filePathOut = clnSubmFilePath + dir + File.separator;
        File f = new File(filePathOut);

        if (f.exists())
        {
            assertTrue(fileManipulation.deleteAllFilesInDirectory(filePathOut), "        All files under " + filePathOut + " should be deleted.");///////////////
        }

        //Delete the merged file generated before
        daoManagerPlatform.deleteDataFromTableByCondition("subm_file", "filename = '" + mergedFileName + "'", testDb);

        int rowCount = daoManagerPlatform.setValuesFromTable("SUBM_FILE", "b_egate_processed = 0, b_processed = 0, processed_dt = '', tot_claims_cnt = 0, tot_claims_amt = 0, trans_confirm_id = ''", condition, testDb);

        logger.info("*** Step 1 Expected Results: - Verify that the test data is ready to use in DB");
        Assert.assertEquals(rowCount, 7, "        Individual files should be ready to be re-generated.");

        logger.info("*** Step 2 Actions: - Run PF-Client Statement Engine");

        logger.info("*** Step 2 Expected Results: - Verify that individual Client Statement PDF files are generated");

        boolean doClnStmtsExist = doClnStmtsExist(individualFileList, filePathOut, QUEUE_WAIT_TIME_MS * 4);

        Assert.assertTrue(doClnStmtsExist, "Individual client statement submission files have not generated");

        logger.info("*** Step 3 Actions: - Run PF-Client Statement Merge Engine");

        f = new File(filePathOut + mergedFileName);

        System.out.println("File Path = " + filePathOut + mergedFileName);//Debug Info

        logger.info("*** Step 3 Expected Results: - Verify that Merged Client Statement PDF file is generated");

        boolean doesMergeFileExist = isMergedFileExists(f, QUEUE_WAIT_TIME_MS * 3);

        Assert.assertTrue(doesMergeFileExist, "Merged client statement submission files has not generated");

        logger.info("*** Step 3 Expected Results: -        Merged Client Statement file: " + mergedFileName + " has been generated under " + filePathOut + " folder.");

        ArrayList<String> submFileInfoList = daoManagerPlatform.getSubmFileInfoFromSUBMFILEByFileName(mergedFileName, testDb);

        logger.info("*** Step 3 Expected Results: - Verify that the values for TOT_CLAIMS_CNT, TOT_CLAIMS_AMT, STMT_APPROVED_TYP for the Merged Client Statement PDF file are correct");

        Assert.assertEquals(submFileInfoList.get(5), "505", "       subm_file.FK_SUBM_SVC_SEQ_ID = '505' (Merged Standard Client Statement).");

        Assert.assertEquals(submFileInfoList.get(7), "1", "       subm_file.B_EGATE_PROCESSED = '1'.");

        String totClaimsCnt = String.valueOf(individualFileList.size());
        Assert.assertEquals(submFileInfoList.get(14), totClaimsCnt, "       subm_file.TOT_CLAIMS_CNT = " + totClaimsCnt);

        String totClaimsAmt = String.valueOf(daoManagerPlatform.getTotClaimsAmtFromSubmFileByTransConfirmId(mergedFileName, testDb));
        Assert.assertEquals(submFileInfoList.get(15), totClaimsAmt, "       subm_file.TOT_CLAIMS_AMT = " + totClaimsAmt);

        Assert.assertEquals(submFileInfoList.get(38), "2", "       subm_file.STMT_APPROVED_TYP = '2'");

        logger.info("*** Step 3 Expected Results: - Verify that the values for TRANS_CONFIRM_ID for individual submission files are equal the Merged Client Statement filename");

        for (List<String> anIndividualFileList : individualFileList)
        {
            Assert.assertEquals(anIndividualFileList.get(11), mergedFileName, "subm_file_seq_id = " + anIndividualFileList.get(0) + " doesn't match subm_file.TRANS_CONFIRM_ID");
        }
    }

    private boolean doClnStmtsExist(List<List<String>> individualFileList, String filePathOut, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean allFilesExists = false;
        while (!allFilesExists && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for individual client statement submission files to finish generating, , elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            allFilesExists = doAllExist(individualFileList, filePathOut);
        }
        return allFilesExists;
    }


    private boolean doAllExist(List<List<String>> individualFileList, String filePathOut) throws Exception
    {
        for (List<String> anIndividualFileList : individualFileList)
        {
            String individualFileName = anIndividualFileList.get(8);
            File f = new File(filePathOut + individualFileName);
            if (!isFileExists(f, 5))
            {
                return false;
            }
        }
        return true;
    }

    private boolean isMergedFileExists(File f, long maxTime) throws Exception
    {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        boolean fileExists = f.exists();
        while (!fileExists && System.currentTimeMillis() < maxTime)
        {
            logger.info("Waiting for merged client statement submission file to finish generating, , elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            fileExists = isFileExists(f, 5);
        }
        return fileExists;
    }
}
