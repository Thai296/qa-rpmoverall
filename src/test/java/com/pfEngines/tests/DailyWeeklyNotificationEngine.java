package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.utils.AccessionUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Properties;

import static com.mbasys.mars.persistance.MiscMap.DOC_TYP_DAILY_WEEKLY_CHARGE_NOTIF;

public class DailyWeeklyNotificationEngine extends SeleniumBaseTest {
    private AccessionUtils accessionUtils;
    private TimeStamp timeStamp;

    @BeforeSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "disableBrowserPlugins"})
    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running BeforeSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "False", "0");
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
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "disableBrowserPlugins"})
    public void afterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, @Optional String disableBrowserPlugins)
    {
        try
        {
            logger.info("Running AfterSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "True", "1");
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
    @Test(priority = 1, description = "Generate Daily - per Patient Notification")
    @Parameters({"project", "testSuite", "testCase", })
    public void testPFER_21(String project, String testSuite, String testCase) throws Exception {
        logger.info("*** Testing - testPFER_21 ***");
        accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
        timeStamp = new TimeStamp(driver);

        logger.info("*** Step 1 Actions: - Create a Priced accession");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        String clnAbbrev = testProperties.getProperty("ClnAbbrev");
        logger.info("        AccnID: " + accnId);

        logger.info("*** Expected Results: - Verify that a new accession was generated");
        Assert.assertNotNull(accnId, "        Did not generate a new Accession.");

        logger.info("*** If client id is 0 in accn table, update it to correct client id");
        Accn accn = accessionDao.getAccn(accnId);
        if((accn.getClnId()==0) || (accn.getPrimClnId()==0))
        {
            Cln cln = clientDao.getClnByClnAbbrev(clnAbbrev);
            accessionDao.updateAccnSetClnIdAndPrimClnId(cln.clnId, accnId);
        }

        logger.info("*** Set Q_FR_PENDING for given accnId");
        if(accessionDao.isAccnInQueueByQTyp(accnId,AccnStatusMap.Q_FR_PENDING))
        {
            daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId,testDb);
        }
        logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));

        logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 1 Expected Results: - Ensure the Accession is Priced");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21, "        The Accession Status for " + accnId + " should be Priced (21).");

        logger.info("*** Step 2 Actions: - Prepare data in DB so it can be processed");
        daoManagerPlatform.setValuesFromACCN("ACCN.PRC_DT = sysdate + 1", "ACCN.PRC_DT < sysdate and ACCN.CLN_SUBM_DT > sysdate", testDb);
        //"1" = Daily - per Patient
        daoManagerXifinRpm.setValuesFromCLNByClnAbbrev(clnAbbrev, "FK_DAILY_CHARGES_FREQ_TYP_ID = 1, B_DAILY_CHARGES = 1", testDb);
        //Set prc_dt to be date before, set cln_subm_dt to be future date
        daoManagerXifinRpm.setValuesFromACCNByAccnId(accnId, "prc_dt = TRUNC (SYSDATE - 1),  cln_subm_dt = TRUNC (SYSDATE)", testDb);
        String date1 = timeStamp.getCurrentDate(); //'mm/dd/yyyy'
        String date2 = timeStamp.getPreviousDate("yyyyMMdd", 1);
        String fileName = clnAbbrev + "_" + date2;
        logger.info("Expected File Name:  " + fileName);

        ArrayList<String> correspFileInfoList = daoManagerPlatform.getCorrespFileInfoFromCORRESPFILEByfTypCreateDtfPathfName(DOC_TYP_DAILY_WEEKLY_CHARGE_NOTIF, date1, "client_charge_notices", fileName, testDb);
        if (accessionUtils.isCorrespFileExist(correspFileInfoList)) {
            logger.info("Corresp File: " + fileName + " already exists, deleting it..");
            String correspFileSeqId = correspFileInfoList.get(0);
            daoManagerPlatform.deleteFileHistFromCLNCORRESPHISTByClnAbbrevFileSeqId(clnAbbrev, correspFileSeqId, testDb);
            daoManagerPlatform.deleteFileFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        }

        logger.info("*** Step 3 Expected Results: - Verify that the records show in CLN_DAILY_CHARGE_V and CLN_DAILY_CHARGE_ACCN_V");
        Assert.assertTrue(clientDao.isClnInClnDailyChargeV(clnAbbrev), "Record " + clnAbbrev + " is not found in CLN_DAILY_CHARGE_V");
        Assert.assertTrue(dailyChargesDao.isAccnInClnDailyChargeAccnV(accnId), "Record " + clnAbbrev + " is not found in CLN_DAILY_CHARGE_ACCN_V");

        logger.info("*** Step 4 Actions: - Run Platform Daily Weekly Notification Engine ");
        dailyChargesDao.waitForAccnToBeOutOfClientDailyChargeQueue(accnId,QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 4 Expected Results: - Verify that the records in CLN_DAILY_CHARGE_V and CLN_DAILY_CHARGE_ACCN_V are being processed");
        Assert.assertFalse(clientDao.isClnInClnDailyChargeV(clnAbbrev),"Record " + clnAbbrev + " is still in CLN_DAILY_CHARGE_V");
        Assert.assertFalse(dailyChargesDao.isAccnInClnDailyChargeAccnV(accnId),"Record " + clnAbbrev + " is still in CLN_DAILY_CHARGE_ACCN_V");

        logger.info("*** Step 4 Expected Results: - Verify that Daily Weekly Notification was generated for the Client");
        correspFileInfoList = daoManagerPlatform.getCorrespFileInfoFromCORRESPFILEByfTypCreateDtfPathfName(DOC_TYP_DAILY_WEEKLY_CHARGE_NOTIF, date1, "client_charge_notices", fileName, testDb);
        Assert.assertTrue(accessionUtils.isCorrespFileExist(correspFileInfoList), "        Daily Weekly Notification file CLIENT_NOTIFICATION_" + fileName + ".pdf should be generated.");

        logger.info("*** Deleting Generated Corresp File");
        correspFileInfoList = daoManagerPlatform.getCorrespFileInfoFromCORRESPFILEByfTypCreateDtfPathfName(DOC_TYP_DAILY_WEEKLY_CHARGE_NOTIF, date1, "client_charge_notices", fileName, testDb);
        if (accessionUtils.isCorrespFileExist(correspFileInfoList)) {
            String correspFileSeqId = correspFileInfoList.get(0);
            daoManagerPlatform.deleteFileHistFromCLNCORRESPHISTByClnAbbrevFileSeqId(clnAbbrev, correspFileSeqId, testDb);
            daoManagerPlatform.deleteFileFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        }

    }

    @Test(priority = 1, description = "Generate Weekly - per Patient Notification")
    @Parameters({"project", "testSuite", "testCase"})
    public void testPFER_22(String project, String testSuite, String testCase) throws Exception {
        logger.info("*** Testing - testPFER_22 ***");

        accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
        timeStamp = new TimeStamp(driver);

        logger.info("*** Step 1 Actions: - Create a Priced accession");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("        AccnID: " + accnId);

        logger.info("*** Expected Results: - Verify that a new accession was generated");
        Assert.assertNotNull(accnId, "        Did not generate a new Accession.");

        logger.info("Wait for an accession to be out of Q_FR_PENDING");
        logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 1 Expected Results: - Ensure the Accession is Priced");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21, "        The Accession Status for " + accnId + " should be Priced (21).");

        logger.info("*** Step 2 Actions: - Prepare data in DB so it can be processed");
        String clnAbbrev = clientDao.getClnByAccnId(accnId).getClnAbbrev();

        //"3" = Weekly - per Patient
        daoManagerXifinRpm.setValuesFromCLNByClnAbbrev(clnAbbrev, "FK_DAILY_CHARGES_FREQ_TYP_ID = 3, B_DAILY_CHARGES = 1", testDb);
        //Set prc_dt to weekly bases, set cln_subm_dt to be future date

        daoManagerPlatform.setPrcDtClnSubmDtFromACCNByAccnId(accnId,  testDb);

        String date1 = timeStamp.getCurrentDate(); //'mm/dd/yyyy'
        String date2 = timeStamp.getPreviousDate("yyyyMMdd", 1);
        String fileName = clnAbbrev + "_" + date2;

        //"8" = Daily/Weekly Charge Notifications
        ArrayList<String> correspFileInfoList = daoManagerPlatform.getCORRESPFILEByfTypfPathfNameOrderByAudDt("8", "client_charge_notices", clnAbbrev, testDb);
        if (accessionUtils.isCorrespFileExist(correspFileInfoList)) {
            String correspFileSeqId = correspFileInfoList.get(0);
            daoManagerPlatform.deleteFileHistFromCLNCORRESPHISTByClnAbbrevFileSeqId(clnAbbrev, correspFileSeqId, testDb);
            daoManagerPlatform.deleteFileFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        }

        clearDataCache();

        logger.info("*** Step 2 Expected Results: - Verify that the records show in CLN_WEEKLY_CHARGE_V and CLN_WEEKLY_CHARGE_ACCN_V");
        Assert.assertTrue(clientDao.isClnInClnDailyChargeV(clnAbbrev));
        Assert.assertTrue(dailyChargesDao.isAccnInClnDailyChargeAccnV(accnId));

        logger.info("*** Step 3 Actions: - Run Platform Daily Weekly Notification Engine ");
        dailyChargesDao.waitForAccnToBeOutOfClientDailyChargeQueue(accnId,QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 3 Expected Results: - Verify that the records in CLN_WEEKLY_CHARGE_V and CLN_WEEKLY_CHARGE_ACCN_V are being processed");
        Assert.assertFalse(clientDao.isClnInClnDailyChargeV(clnAbbrev));
        Assert.assertFalse(dailyChargesDao.isAccnInClnDailyChargeAccnV(accnId));

        logger.info("*** Step 3 Expected Results: - Verify that Daily Weekly Notification was generated for the Client");
        correspFileInfoList = daoManagerPlatform.getCorrespFileInfoFromCORRESPFILEByfTypCreateDtfPathfName(DOC_TYP_DAILY_WEEKLY_CHARGE_NOTIF, date1, "client_charge_notices", fileName, testDb);
        Assert.assertTrue(accessionUtils.isCorrespFileExist(correspFileInfoList), "        Daily Weekly Notification file CLIENT_NOTIFICATION_" + fileName + ".pdf should be generated.");

        correspFileInfoList = daoManagerPlatform.getCORRESPFILEByfTypfPathfNameOrderByAudDt("8", "client_charge_notices", clnAbbrev, testDb);
        if (accessionUtils.isCorrespFileExist(correspFileInfoList)) {
            String correspFileSeqId = correspFileInfoList.get(0);
            daoManagerPlatform.deleteFileHistFromCLNCORRESPHISTByClnAbbrevFileSeqId(clnAbbrev, correspFileSeqId, testDb);
            daoManagerPlatform.deleteFileFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        }

    }

    @Test(priority = 1, description = "Generate Daily - Aggregate Notification")
    @Parameters({"project", "testSuite", "testCase"})
    public void testPFER_23(String project, String testSuite, String testCase) throws Exception {
        logger.info("*** Testing - testPFER_23 ***");

        accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
        timeStamp = new TimeStamp(driver);

        logger.info("*** Step 1 Actions: - Create a Priced accession");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("        AccnID: " + accnId);
        String clnAbbrev = testProperties.getProperty("ClnAbbrev");

        logger.info("*** Expected Results: - Verify that a new accession was generated");
        Assert.assertNotNull(accnId, "        Did not generate a new Accession.");

        logger.info("*** If client id is 0 in accn table, update it to correct client id");
        Accn accn = accessionDao.getAccn(accnId);
        if((accn.getClnId()==0) || (accn.getPrimClnId()==0))
        {
            Cln cln = clientDao.getClnByClnAbbrev(clnAbbrev);
            accessionDao.updateAccnSetClnIdAndPrimClnId(cln.clnId, accnId);
        }

        logger.info("*** Set Q_FR_PENDING for given accnId");
        if(accessionDao.isAccnInQueueByQTyp(accnId,AccnStatusMap.Q_FR_PENDING))
        {
            daoManagerXifinRpm.setQfrPendingReleaseByAccndId(accnId,testDb);
        }

        logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));

        logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 1 Expected Results: - Ensure the Accession is Priced");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21, "        The Accession Status for " + accnId + " should be Priced (21).");

        logger.info("*** Step 2 Actions: - Prepare data in DB so it can be processed");
        //"2" = Daily - Aggregate
        daoManagerXifinRpm.setValuesFromCLNByClnAbbrev(clnAbbrev, "FK_DAILY_CHARGES_FREQ_TYP_ID = 2, B_DAILY_CHARGES = 1", testDb);
        //Set prc_dt to be date before, set cln_subm_dt to be future date
        daoManagerXifinRpm.setValuesFromACCNByAccnId(accnId, "prc_dt = TRUNC (SYSDATE - 1),  cln_subm_dt = TRUNC (SYSDATE)", testDb);

        String date1 = timeStamp.getCurrentDate(); //'mm/dd/yyyy'
        String date2 = timeStamp.getPreviousDate("yyyyMMdd", 1);
        String fileName = clnAbbrev + "_" + date2;
        logger.info("Expected File Name:  " + fileName);

        //"8" = Daily/Weekly Charge Notifications
        ArrayList<String> correspFileInfoList = daoManagerPlatform.getCorrespFileInfoFromCORRESPFILEByfTypCreateDtfPathfName(DOC_TYP_DAILY_WEEKLY_CHARGE_NOTIF, date1, "client_charge_notices", fileName, testDb);
        if (accessionUtils.isCorrespFileExist(correspFileInfoList)) {
            logger.info("Corresp File :" + fileName + " already exists, deleting it..");
            String correspFileSeqId = correspFileInfoList.get(0);
            daoManagerPlatform.deleteFileHistFromCLNCORRESPHISTByClnAbbrevFileSeqId(clnAbbrev, correspFileSeqId, testDb);
            daoManagerPlatform.deleteFileFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        }

        logger.info("*** Step 2 Expected Results: - Verify that the records show in CLN_DAILY_CHARGE_V and CLN_DAILY_CHARGE_ACCN_V");
        Assert.assertTrue(clientDao.isClnInClnDailyChargeV(clnAbbrev), "Record " + clnAbbrev + " is not found in CLN_DAILY_CHARGE_V");
        Assert.assertTrue(dailyChargesDao.isAccnInClnDailyChargeAccnV(accnId), "Record " + clnAbbrev + " is not in CLN_DAILY_CHARGE_ACCN_V");

        logger.info("*** Step 3 Actions: - Run Platform Daily Weekly Notification Engine ");
        dailyChargesDao.waitForAccnToBeOutOfClientDailyChargeQueue(accnId,QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 3 Expected Results: - Verify that the records in CLN_DAILY_CHARGE_V and CLN_DAILY_CHARGE_ACCN_V are being processed");
        Assert.assertFalse(clientDao.isClnInClnDailyChargeV(clnAbbrev), "Record " + clnAbbrev + " is still in CLN_DAILY_CHARGE_V");
        Assert.assertFalse(dailyChargesDao.isAccnInClnDailyChargeAccnV(accnId), "Record " + clnAbbrev + " is still in CLN_DAILY_CHARGE_ACCN_V");

        logger.info("*** Step 3 Expected Results: - Verify that Daily Weekly Notification was generated for the Client");
        correspFileInfoList = daoManagerPlatform.getCorrespFileInfoFromCORRESPFILEByfTypCreateDtfPathfName(DOC_TYP_DAILY_WEEKLY_CHARGE_NOTIF, date1, "client_charge_notices", fileName, testDb);
        Assert.assertTrue(accessionUtils.isCorrespFileExist(correspFileInfoList), "        Daily Weekly Notification file CLIENT_NOTIFICATION_" + fileName + ".pdf should be generated.");

        logger.info("*** Deleting Generated Corresp File");
        correspFileInfoList = daoManagerPlatform.getCorrespFileInfoFromCORRESPFILEByfTypCreateDtfPathfName(DOC_TYP_DAILY_WEEKLY_CHARGE_NOTIF, date1, "client_charge_notices", fileName, testDb);
        if (accessionUtils.isCorrespFileExist(correspFileInfoList)) {
            String correspFileSeqId = correspFileInfoList.get(0);
            daoManagerPlatform.deleteFileHistFromCLNCORRESPHISTByClnAbbrevFileSeqId(clnAbbrev, correspFileSeqId, testDb);
            daoManagerPlatform.deleteFileFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        }

    }

    @Test(priority = 1, description = "Generate Weekly - Aggregate Notification")
    @Parameters({ "project", "testSuite", "testCase"})
    public void testPFER_24(String project, String testSuite, String testCase) throws Exception {
        logger.info("*** Testing - testPFER_24 ***");

        accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
        timeStamp = new TimeStamp(driver);

        logger.info("*** Step 1 Actions: - Create a Priced accession");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());

        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("        AccnID: " + accnId);

        logger.info("*** Expected Results: - Verify that a new accession was generated");
        Assert.assertNotNull(accnId, "        Did not generate a new Accession.");

        logger.info("Wait for an accession to be out of Q_FR_PENDING");
        logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 1 Expected Results: - Ensure the Accession is Priced");
        Assert.assertEquals(accessionDao.getAccn(accnId).getStaId(), 21, "        The Accession Status for " + accnId + " should be Priced (21).");

        logger.info("*** Step 2 Actions: - Prepare data in DB so it can be processed");
        String clnAbbrev = clientDao.getClnByAccnId(accnId).getClnAbbrev();

        //"4" = Weekly - Aggregate
        daoManagerXifinRpm.setValuesFromCLNByClnAbbrev(clnAbbrev, "FK_DAILY_CHARGES_FREQ_TYP_ID = 4, B_DAILY_CHARGES = 1", testDb);
        //Set prc_dt to weekly bases, set cln_subm_dt to be future date

        daoManagerXifinRpm.setValuesFromACCNByAccnId(accnId, "prc_dt = TRUNC (SYSDATE - 1),  cln_subm_dt = TRUNC (SYSDATE)", testDb);

        String date1 = timeStamp.getCurrentDate(); //'mm/dd/yyyy'
        String date2 = timeStamp.getPreviousDate("yyyyMMdd", 1);
        String fileName = clnAbbrev + "_" + date2;

        //"8" = Daily/Weekly Charge Notifications
        ArrayList<String> correspFileInfoList = daoManagerPlatform.getCORRESPFILEByfTypfPathfNameOrderByAudDt("8", "client_charge_notices", clnAbbrev, testDb);
        if (accessionUtils.isCorrespFileExist(correspFileInfoList)) {
            String correspFileSeqId = correspFileInfoList.get(0);
            daoManagerPlatform.deleteFileHistFromCLNCORRESPHISTByClnAbbrevFileSeqId(clnAbbrev, correspFileSeqId, testDb);
            daoManagerPlatform.deleteFileFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        }

        logger.info("*** Step 3 Actions: - Clear System Cache in Xifin Admin Portal");
        clearDataCache();

        logger.info("*** Step 3 Expected Results: - Verify that the records show in CLN_WEEKLY_CHARGE_V and CLN_WEEKLY_CHARGE_ACCN_V");
        Assert.assertTrue(clientDao.isClnInClnDailyChargeV(clnAbbrev));
        Assert.assertTrue(dailyChargesDao.isAccnInClnDailyChargeAccnV(accnId));

        logger.info("*** Step 4 Actions: - Run Platform Daily Weekly Notification Engine ");
        dailyChargesDao.waitForAccnToBeOutOfClientDailyChargeQueue(accnId,QUEUE_WAIT_TIME_MS);

        logger.info("*** Step 4 Expected Results: - Verify that the records in CLN_WEEKLY_CHARGE_V and CLN_WEEKLY_CHARGE_ACCN_V are being processed");
        Assert.assertFalse(clientDao.isClnInClnDailyChargeV(clnAbbrev));
        Assert.assertFalse(dailyChargesDao.isAccnInClnDailyChargeAccnV(accnId));

        logger.info("*** Step 4 Expected Results: - Verify that Daily Weekly Notification was generated for the Client");
        correspFileInfoList = daoManagerPlatform.getCorrespFileInfoFromCORRESPFILEByfTypCreateDtfPathfName(DOC_TYP_DAILY_WEEKLY_CHARGE_NOTIF, date1, "client_charge_notices", fileName, testDb);
        Assert.assertTrue(accessionUtils.isCorrespFileExist(correspFileInfoList), "        Daily Weekly Notification file CLIENT_NOTIFICATION_" + fileName + ".pdf should be generated.");

        correspFileInfoList = daoManagerPlatform.getCORRESPFILEByfTypfPathfNameOrderByAudDt("8", "client_charge_notices", clnAbbrev, testDb);
        if (accessionUtils.isCorrespFileExist(correspFileInfoList)) {
            String correspFileSeqId = correspFileInfoList.get(0);
            daoManagerPlatform.deleteFileHistFromCLNCORRESPHISTByClnAbbrevFileSeqId(clnAbbrev, correspFileSeqId, testDb);
            daoManagerPlatform.deleteFileFromCORRESPFILEByFileSeqId(correspFileSeqId, testDb);
        }

    }
}
