package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.accnTest.AccnTest;
import com.mbasys.mars.ejb.entity.testXref.TestXref;
import com.mbasys.mars.ejb.entity.xref.Xref;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.MiscMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TestDataSetup;
import com.xifin.xap.utils.XifinAdminUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class BillingAssignmentEngineTest extends SeleniumBaseTest {

    @BeforeSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "disableBrowserPlugins"})
    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, @Optional String disableBrowserPlugins) {
        try {
            logger.info("Running BeforeSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_PERFORM_JOIN_LOGIC, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_CLIENT_BILLING_RULE_LOGIC_SPLIT, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_PERFORM_PYR_BILLING_RULES, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_DEFAULT_CLN_BILLING_RULES, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_JOIN_ACCNS, "False", "0");

            XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
            xifinAdminUtils.clearDataCache();
        } catch (Exception e) {
            Assert.fail("Error running BeforeSuite", e);
        } finally {
            quitWebDriver();
        }
    }

    @AfterSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "disableBrowserPlugins"})
    public void sfterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, @Optional String disableBrowserPlugins) {
        try {
            logger.info("Running BeforeSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "True", "1");
            updateSystemSetting(SystemSettingMap.SS_PERFORM_JOIN_LOGIC, "True", "1");
            updateSystemSetting(SystemSettingMap.SS_CLIENT_BILLING_RULE_LOGIC_SPLIT, "True", "1");
            updateSystemSetting(SystemSettingMap.SS_PERFORM_PYR_BILLING_RULES, "True", "1");
            updateSystemSetting(SystemSettingMap.SS_DEFAULT_CLN_BILLING_RULES, "True", "1");
            updateSystemSetting(SystemSettingMap.SS_JOIN_ACCNS, "True", "1");

            XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
            xifinAdminUtils.clearDataCache();
        } catch (Exception e) {
            Assert.fail("Error running BeforeSuite", e);
        } finally {
            quitWebDriver();
        }
    }

    @Test(priority = 1, description = "Client is assigned to Insurance and flagged to perform Billing Assignment")
    @Parameters({ "project", "testSuite", "testCase"})
    public void testPFER_19( String project, String testSuite, String testCase) throws Exception {
        logger.info("*** Testing - testPFER_19 ***");


        logger.info("*** Step 2 Actions: - Send WS request to create a new accession with a Client that has PerformBillingAssignment turned On");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("        AccnID: " + accnId);

        logger.info("*** Step 2 Expected Results: - Verify that a new Accession was genrated");
        Assert.assertTrue(accnId != null, "        Did not generate a new Accession.");

        logger.info("*** Step 2 Expected Results: - Verify that the Accession is in the Q_CLN_BILLING_ASSIGNMENT");

        ArrayList<String> clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
        Assert.assertFalse(clnBillingAssignInfoList.isEmpty(), "        Accession " + accnId + " should be in Q_CLN_BILLING_ASSIGNMENT.");

        logger.info("*** Step 3 Actions: - Set FK_NEW_BILLING_ASSIGN_TYP_ID vlaue in the Q_CLN_BILLING_ASSIGNMENT to 2 (INSURANCE) in DB");
        daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 2", testDb);

        logger.info("Waiting for accession to complete BA, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 4 Expected Results: - Verify that the Accession stays in the Q_CLN_BILLING_ASSIGNMENT");
        clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
        Assert.assertFalse(clnBillingAssignInfoList.isEmpty(), "        Accession " + accnId + " not stays in Q_CLN_BILLING_ASSIGNMENT.");

        logger.info("*** Step 4 Expected Results: - Verify that FK_CUR_BILLING_ASSIGN_TYP_ID vlaue in the Q_CLN_BILLING_ASSIGNMENT was updated to 2 (INSURANCE)");
        Assert.assertTrue(clnBillingAssignInfoList.get(1).equals("2"), "        The value for FK_CUR_BILLING_ASSIGN_TYP_ID in Q_CLN_BILLING_ASSIGNMENT should be updated to '2'.");

        logger.info("*** Step 5 Actions: - Select OE Posting Engine and run twice");

        logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 6 Actions: - Turn off the Billing Assignment Req for the Client on the Accession in DB");

        String clnAbbrev = daoManagerXifinRpm.getClientAbbrevIdByAccnId(accnId, testDb);
        daoManagerXifinRpm.setValuesFromCLNByClnAbbrev(clnAbbrev, "B_BILLING_ASSIGNMENT_REQ = 0", testDb);
    }


    @Test(priority = 1, description = "Priced Accn backed out pricing after changing the billing assignment")
    @Parameters({"project", "testSuite", "testCase"})
    public void testPFER_20(String project, String testSuite, String testCase) throws Exception {
        logger.info("*** Testing - testPFER_20 ***");


        logger.info("*** Step 2 Actions: - Send WS request to create a new accession with a Client that has PerformBillingAssignment turned On");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("        AccnID: " + accnId);

        logger.info("*** Step 2 Expected Results: - Verify that a new Accession was genrated");
        Assert.assertTrue(accnId != null, "        Did not generate a new Accession.");

        logger.info("*** Step 2 Expected Results: - Verify that the Accession is in the Q_CLN_BILLING_ASSIGNMENT");
        ArrayList<String> clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
        Assert.assertFalse(clnBillingAssignInfoList.isEmpty(), "        Accession " + accnId + " should be in Q_CLN_BILLING_ASSIGNMENT.");

        logger.info("*** Step 3 Actions: - Set FK_NEW_BILLING_ASSIGN_TYP_ID vlaue in the Q_CLN_BILLING_ASSIGNMENT to 2 (INSURANCE) in DB");
        daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 2", testDb);

        logger.info("Waiting for accession to complete BA, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 4 Expected Results: - Verify that the Accession stays in the Q_CLN_BILLING_ASSIGNMENT");
        clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
        Assert.assertFalse(clnBillingAssignInfoList.isEmpty(), "        Accession " + accnId + " does not stay in Q_CLN_BILLING_ASSIGNMENT.");

        logger.info("*** Step 4 Expected Results: - Verify that FK_CUR_BILLING_ASSIGN_TYP_ID vlaue in the Q_CLN_BILLING_ASSIGNMENT was updated to 2 (INSURANCE)");
        Assert.assertTrue(clnBillingAssignInfoList.get(1).equals("2"), "        The value for FK_CUR_BILLING_ASSIGN_TYP_ID in Q_CLN_BILLING_ASSIGNMENT should be updated to '2'.");

        logger.info("*** Step 5 Actions: - Select OE Posting Engine and run twice");
        logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 7 Expected Results: - Ensure the Accession Status is Priced (21)");
        Assert.assertEquals(daoManagerXifinRpm.getStatusIdByAccnId(accnId, testDb), 21, "        The Accession Status for " + accnId + " is not Priced (21).");

        logger.info("*** Step 8 Actions: - Set FK_NEW_BILLING_ASSIGN_TYP_ID vlaue in the Q_CLN_BILLING_ASSIGNMENT to 0 (Unassigned) in DB");
        daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 0", testDb);

        logger.info("Waiting for accession to complete BA, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));


        logger.info("*** Step 10 Actions: - Turn off the Billing Assignment Req for the Client on the Accession in DB");
        String clnAbbrev = daoManagerXifinRpm.getClientAbbrevIdByAccnId(accnId, testDb);
        daoManagerXifinRpm.setValuesFromCLNByClnAbbrev(clnAbbrev, "B_BILLING_ASSIGNMENT_REQ = 0", testDb);

        logger.info("*** Step 11 Expected Results: - Ensure the Accession was backed out pricing");
        Assert.assertTrue(daoManagerXifinRpm.getStatusIdByAccnId(accnId, testDb)!=21, "        The Accession Status for " + accnId + " should not be Priced (21).");

        logger.info("*** Step 11 Expected Results: - Ensure the Accession Q_TYP was updated in ACCN_QUE");
        Assert.assertEquals(daoManagerXifinRpm.getQtypByAccnId(accnId, testDb), 2, "        The Q_TYP for Accession " + accnId + " should be 2 (Manual Release).");

        logger.info("*** Step 11 Expected Results: - Ensure the Accession is in Q_FR_Pending table");
        Assert.assertEquals(daoManagerXifinRpm.getCountFromQERPendingTableByAccnId(accnId, testDb), 1, "        The Accession: " + accnId + " should be in Q_FR_Pending table.");

    }


    @Test(priority = 1, description = "Add remark to a Client billed Accn with Test Xref set to BILLCLN")
    @Parameters({"project", "testSuite", "testCase"})
    public void testPFER_25(String project, String testSuite, String testCase) throws Exception {
        logger.info("*** Testing - testPFER_25 ***");

        logger.info("*** Step 2 Actions: - Send WS request to create a new accession with a Client that has PerformBillingAssignment turned On");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("        AccnID: " + accnId);


        logger.info("*** Step 2 Expected Results: - Verify that a new Accession was generated");
        Assert.assertTrue(accnId != null, "        Did not generate a new Accession.");


        List<AccnTest> accnTests = accessionDao.getAccnTestsByAccnId(accnId);

        Xref xref = crossReferenceDao.getXrefByAbbrevXrefTypId("BILLCLN", MiscMap.XREF_TYP_PARTA_BILL_CLN);

        TestXref testXref = new TestXref();
        testXref.setTestId(accnTests.get(0).getTestId());
        testXref.setXrefId(xref.getXrefId());
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        java.sql.Date effDt = new java.sql.Date(df.parse("01-01-2016").getTime());
        testXref.setEffDt(effDt);
        testDao.setTestXref(testXref);

        logger.info("*** Step 2 Expected Results: - Verify that the Accession is in the Q_CLN_BILLING_ASSIGNMENT");
        ArrayList<String> clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
        Assert.assertFalse(clnBillingAssignInfoList.isEmpty(), "        Accession " + accnId + " should be in Q_CLN_BILLING_ASSIGNMENT.");

        logger.info("*** Step 3 Actions: - Set FK_NEW_BILLING_ASSIGN_TYP_ID vlaue in the Q_CLN_BILLING_ASSIGNMENT to 1 (CLIENT) in DB");
        daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 1", testDb);

        logger.info("Waiting for accession to complete BA, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 4 Expected Results: - Verify that the Accession stays in the Q_CLN_BILLING_ASSIGNMENT");
        clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
        Assert.assertFalse(clnBillingAssignInfoList.isEmpty(), "        Accession " + accnId + " does not stay in Q_CLN_BILLING_ASSIGNMENT.");

        logger.info("*** Step 4 Expected Results: - Verify that Remark Code - BACLIENT is added to the accession");
        ArrayList<String>accnRmkInfoList = daoManagerPlatform.getAccnRmkInfoFromACCNRMKByAccnIdRmkCdAbbrev(accnId, "BACLIENT", testDb);
        Assert.assertTrue(!(accnRmkInfoList.isEmpty()), "        Accession Remark BACLIENT should be added to the accession: " + accnId);

        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));

        logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 7 Expected Results: - Ensure the Accession Status is ZBal (51)");
        Assert.assertEquals(daoManagerXifinRpm.getStatusIdByAccnId(accnId, testDb), 51, "        The Accession Status for " + accnId + " is not ZBal (51).");

        logger.info("*** Step 8 Actions: - Set FK_NEW_BILLING_ASSIGN_TYP_ID vlaue in the Q_CLN_BILLING_ASSIGNMENT to 2 (INSURANCE) in DB");
        daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 2", testDb);

        logger.info("Waiting for accession to complete BA, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));


        logger.info("*** Step 9 Expected Results: - Verify that Remark Code - BACLIENT is removed from the accession");
        accnRmkInfoList = daoManagerPlatform.getAccnRmkInfoFromACCNRMKByAccnIdRmkCdAbbrev(accnId, "BACLIENT", testDb);
        Assert.assertTrue(accnRmkInfoList.isEmpty(), "        Accession Remark BACLIENT should be removed from the accession: " + accnId);

        logger.info("*** Step 9 Expected Results: - Verify that Client Payor was deleted from the accession");
        ArrayList<String> accnPyrDelInfoList = daoManagerPlatform.getAccnPyrDelInfoFromACCNPYRDELByAccnIdPyrAbbrev(accnId, "C", testDb);
        Assert.assertTrue(!(accnPyrDelInfoList.isEmpty()), "        The Client Payor should be deleted from the accession: " + accnId);

        logger.info("*** Step 9 Expected Results: - Ensure the Accession was backed out pricing");
        Assert.assertTrue(daoManagerXifinRpm.getStatusIdByAccnId(accnId, testDb)!=21, "        The Accession Status for " + accnId + " should not be Priced (21).");

        logger.info("*** Step 10 Actions: - Turn off the Billing Assignment Req for the Client on the Accession in DB");
        String clnAbbrev = daoManagerXifinRpm.getClientAbbrevIdByAccnId(accnId, testDb);
        daoManagerXifinRpm.setValuesFromCLNByClnAbbrev(clnAbbrev, "B_BILLING_ASSIGNMENT_REQ = 0", testDb);
    }


    @Test(priority = 1, description = "One test on the Accn when billing assignment changed from Ins to Cln")
    @Parameters({ "project", "testSuite", "testCase"})
    public void testPFER_26( String project, String testSuite, String testCase) throws Exception {
        logger.info("*** Testing - testPFER_26 ***");

        logger.info("*** Step 1 Actions: - Send WS request to create a new 3rd party payor accession with a Client that has PerformBillingAssignment turned On");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("        AccnID: " + accnId);

        logger.info("*** Step 1 Expected Results: - Verify that a new Accession was generated");
        Assert.assertTrue(accnId != null, "        Did not generate a new Accession.");

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);

        logger.info("*** Step 2 Expected Results: - Verify that the Accession is in the Q_CLN_BILLING_ASSIGNMENT");
        ArrayList<String> clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
        Assert.assertFalse(clnBillingAssignInfoList.isEmpty(), "        Accession " + accnId + " should be in Q_CLN_BILLING_ASSIGNMENT.");

        logger.info("*** Step 3 Actions: - Set FK_NEW_BILLING_ASSIGN_TYP_ID vlaue in the Q_CLN_BILLING_ASSIGNMENT to 2 (INSURANCE) in DB");
        daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 2", testDb);

        logger.info("Waiting for accession to complete BA, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 4 Expected Results: - Verify that the Accession stays in the Q_CLN_BILLING_ASSIGNMENT");
        clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
        Assert.assertFalse(clnBillingAssignInfoList.isEmpty(), "        Accession " + accnId + " does not stay in Q_CLN_BILLING_ASSIGNMENT.");

        logger.info("*** Step 4 Expected Results: - Verify that Remark Code - BACLIENT is not added to the accession");
        ArrayList<String>accnRmkInfoList = daoManagerPlatform.getAccnRmkInfoFromACCNRMKByAccnIdRmkCdAbbrev(accnId, "BACLIENT", testDb);
        Assert.assertTrue((accnRmkInfoList.isEmpty()), "        Accession Remark BACLIENT should not be added to the accession: " + accnId);

        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));

        logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 5 Expected Results: - Ensure the Accession Status is Priced (21)");
        Assert.assertEquals(daoManagerXifinRpm.getStatusIdByAccnId(accnId, testDb), 21, "        The Accession Status for " + accnId + " is not Priced (21).");


        List<AccnTest> accnTests = accessionDao.getAccnTestsByAccnId(accnId);

        Xref xref = crossReferenceDao.getXrefByAbbrevXrefTypId("BILLCLN", MiscMap.XREF_TYP_PARTA_BILL_CLN);

        TestXref testXref = new TestXref();
        testXref.setTestId(accnTests.get(0).getTestId());
        testXref.setXrefId(xref.getXrefId());
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        java.sql.Date effDt = new java.sql.Date(df.parse("01-01-2016").getTime());
        testXref.setEffDt(effDt);
        testDao.setTestXref(testXref);

        logger.info("*** Step 6 Actions: - Set FK_NEW_BILLING_ASSIGN_TYP_ID vlaue in the Q_CLN_BILLING_ASSIGNMENT to 1 (CLIENT) in DB");
        daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 1", testDb);

        logger.info("Waiting for accession to complete BA, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 7 Expected Results: - Verify that Remark Code - BACLIENT is added to the accessions");
        accnRmkInfoList = daoManagerPlatform.getAccnRmkInfoFromACCNRMKByAccnIdRmkCdAbbrev(accnId, "BACLIENT", testDb);
        Assert.assertTrue(!(accnRmkInfoList.isEmpty()), "        Accession Remark BACLIENT should be added to the accession: " + accnId);

        logger.info("*** Step 8 Expected Results: - Verify that Accession payor was updated to 'C'");
        ArrayList<String> mainAccnPyrInfoList = daoManagerAccnWS.getPyrAbbrevSubsIdRelshpTypFromAccnPyrByAccnIdPyrPrio(accnId, "1", testDb);

        String mainAccnPyrAbbrev = mainAccnPyrInfoList.get(0);
        Assert.assertEquals(mainAccnPyrAbbrev.trim(), "C", "        The Accession: " + accnId + " Payor should be changed to 'C'.");

        logger.info("*** Step 9 Expected Results: - Verify that the Accession is in the Q_CLN_BILLING_ASSIGNMENT");
        clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
        Assert.assertFalse(clnBillingAssignInfoList.isEmpty(), "        The main Accession " + accnId + " is not in Q_CLN_BILLING_ASSIGNMENT.");

        logger.info("*** Step 9 Expected Results: - Verify that the Accnession was backed out pricing and the Accessions' Status is FinalReported (11)");
        Assert.assertEquals(daoManagerXifinRpm.getStatusIdByAccnId(accnId, testDb), 11, "        The Accession Status for " + accnId + " is not FNLRPDT (11).");

        logger.info("*** Step 9 Expected Results: - Verify that the 3rd party payor was added to the ACCN_PYR_DEL for the Accession");
        ArrayList<String> accnPyrDelInfoList = daoManagerPlatform.getAccnPyrDelInfoFromACCNPYRDELByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);
        Assert.assertTrue(!(accnPyrDelInfoList.isEmpty()), "        The 3rd Party Payorr should be deleted from the accession: " + accnId);

        logger.info("*** Step 10 Actions: - Turn off the Billing Assignment Req for the Client on the Accession in DB");
        String clnAbbrev = daoManagerXifinRpm.getClientAbbrevIdByAccnId(accnId, testDb);
        daoManagerXifinRpm.setValuesFromCLNByClnAbbrev(clnAbbrev, "B_BILLING_ASSIGNMENT_REQ = 0", testDb);

    }

    @Test(priority = 1, description = "Two tests on the Accn when billing assignment changed from Ins to Cln")
    @Parameters({"project", "testSuite", "testCase"})
    public void testPFER_27(String project, String testSuite, String testCase) throws Exception {
        logger.info("*** Testing - testPFER_27 ***");

        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("        AccnID: " + accnId);

        logger.info("*** Step 2 Expected Results: - Verify that a new Accession was generated");
        Assert.assertTrue(accnId != null, "        Did not generate a new Accession.");

        String pyrAbbrev = daoManagerAccnWS.getPayorAbbrevByAccnIdAndPayorPrio(accnId, 1, testDb);

        logger.info("*** Step 2 Expected Results: - Verify that the Accession is in the Q_CLN_BILLING_ASSIGNMENT");
        ArrayList<String> clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
        Assert.assertFalse(clnBillingAssignInfoList.isEmpty(), "        Accession " + accnId + " should be in Q_CLN_BILLING_ASSIGNMENT.");

        logger.info("*** Step 3 Actions: - Set FK_NEW_BILLING_ASSIGN_TYP_ID vlaue in the Q_CLN_BILLING_ASSIGNMENT to 2 (INSURANCE) in DB");
        daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 2", testDb);

        logger.info("Waiting for accession to complete BA, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 4 Expected Results: - Verify that the Accession stays in the Q_CLN_BILLING_ASSIGNMENT");
        clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
        Assert.assertFalse(clnBillingAssignInfoList.isEmpty(), "        Accession " + accnId + " does not stay in Q_CLN_BILLING_ASSIGNMENT.");

        logger.info("*** Step 4 Expected Results: - Verify that Remark Code - BACLIENT is not added to the accession");
        ArrayList<String>accnRmkInfoList = daoManagerPlatform.getAccnRmkInfoFromACCNRMKByAccnIdRmkCdAbbrev(accnId, "BACLIENT", testDb);
        Assert.assertTrue((accnRmkInfoList.isEmpty()), "        Accession Remark BACLIENT should not be added to the accession: " + accnId);

        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));

        logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 7 Expected Results: - Ensure the Accession Status is Priced (21)");
        Assert.assertEquals(daoManagerXifinRpm.getStatusIdByAccnId(accnId, testDb), 21, "        The Accession Status for " + accnId + " is not Priced (21).");


        int testId = testDao.getTestByTestAbbrev("5010").getTestId();

        Xref xref = crossReferenceDao.getXrefByAbbrevXrefTypId("BILLCLN", MiscMap.XREF_TYP_PARTA_BILL_CLN);

        TestXref testXref = new TestXref();
        testXref.setTestId(testId);
        testXref.setXrefId(xref.getXrefId());
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        java.sql.Date effDt = new java.sql.Date(df.parse("01-01-2016").getTime());
        testXref.setEffDt(effDt);
        testDao.setTestXref(testXref);

        logger.info("*** Step 8 Actions: - Set FK_NEW_BILLING_ASSIGN_TYP_ID vlaue in the Q_CLN_BILLING_ASSIGNMENT to 1 (CLIENT) in DB");
        daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 1", testDb);

        logger.info("Waiting for accession to complete BA, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 9 Expected Results: - Verify that Remark Code - BACLIENT is added to the main accessions");
        accnRmkInfoList = daoManagerPlatform.getAccnRmkInfoFromACCNRMKByAccnIdRmkCdAbbrev(accnId, "BACLIENT", testDb);
        Assert.assertTrue(!(accnRmkInfoList.isEmpty()), "        Accession Remark BACLIENT should be added to the accession: " + accnId);

        logger.info("*** Step 9 Expected Results: - Verify that Remark Code - SPLIT is added to the main accession");
        accnRmkInfoList = daoManagerPlatform.getAccnRmkInfoFromACCNRMKByAccnIdRmkCdAbbrev(accnId, "SPLIT", testDb);
        Assert.assertTrue(!(accnRmkInfoList.isEmpty()), "        Accession Remark SPLIT should be added to the accession: " + accnId);

        logger.info("*** Step 9 Expected Results: - Verify that main Accession payor was updated to 'C'");
        ArrayList<String> mainAccnPyrInfoList = daoManagerAccnWS.getPyrAbbrevSubsIdRelshpTypFromAccnPyrByAccnIdPyrPrio(accnId, "1", testDb);
        String mainAccnPyrAbbrev = mainAccnPyrInfoList.get(0);
        Assert.assertEquals(mainAccnPyrAbbrev.trim(), "C", "        The Accession: " + accnId + " Payor should be changed to 'C'.");

        logger.info("*** Step 9 Expected Results: - Verify that the main Accession is in the Q_CLN_BILLING_ASSIGNMENT");
        clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
        Assert.assertFalse(clnBillingAssignInfoList.isEmpty(), "        The main Accession " + accnId + " is not in Q_CLN_BILLING_ASSIGNMENT.");

        logger.info("*** Step 9 Expected Results: - Verify that the main Accessions' Status is FinalReported (11)");
        Assert.assertEquals(daoManagerXifinRpm.getStatusIdByAccnId(accnId, testDb), 11, "        The Accession Status for " + accnId + " is not FNLRPDT (11).");

        logger.info("*** Step 9 Expected Results: - Verify that the 3rd party payor was deleted and added to the ACCN_PYR_DEL for the main Accession");
        ArrayList<String> accnPyrDelInfoList = daoManagerPlatform.getAccnPyrDelInfoFromACCNPYRDELByAccnIdPyrAbbrev(accnId, pyrAbbrev, testDb);
        Assert.assertTrue(!(accnPyrDelInfoList.isEmpty()), "        The 3rd Party Payorr should be deleted from the accession: " + accnId);

        logger.info("*** Step 10 Actions: - Get the split Accession ID");
        String splitAccnId = accnId + "XBA1";
        logger.info("        The split Accession ID: " + splitAccnId);

        logger.info("*** Step 10 Expected Results: - Verify that Remark Code - BACLIENT is added to the split accessions");
        accnRmkInfoList = daoManagerPlatform.getAccnRmkInfoFromACCNRMKByAccnIdRmkCdAbbrev(splitAccnId, "BACLIENT", testDb);
        Assert.assertTrue(!(accnRmkInfoList.isEmpty()), "        Accession Remark BACLIENT should be added to the split accession: " + splitAccnId);

        logger.info("*** Step 10 Expected Results: - Verify that Remark Code - SPLIT is added to the split accession");
        accnRmkInfoList = daoManagerPlatform.getAccnRmkInfoFromACCNRMKByAccnIdRmkCdAbbrev(splitAccnId, "SPLIT", testDb);
        Assert.assertTrue(!(accnRmkInfoList.isEmpty()), "        Accession Remark SPLIT should be added to the split accession: " + splitAccnId);

        logger.info("*** Step 10 Expected Results: - Verify that split Accession payor is still the original 3rd party payor");
        ArrayList<String> splitAccnPyrInfoList = daoManagerAccnWS.getPyrAbbrevSubsIdRelshpTypFromAccnPyrByAccnIdPyrPrio(splitAccnId, "1", testDb);
        String splitAccnPyrAbbrev = splitAccnPyrInfoList.get(0);
        Assert.assertEquals(splitAccnPyrAbbrev.trim(), pyrAbbrev, "        The split Accession: " + splitAccnId + " Payor should be " + pyrAbbrev);

        logger.info("*** Step 10 Expected Results: - Verify that the split Accession is NOT in the Q_CLN_BILLING_ASSIGNMENT");
        clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(splitAccnId, testDb);
        Assert.assertTrue(clnBillingAssignInfoList.isEmpty(), "        The split Accession " + splitAccnId + " should not in Q_CLN_BILLING_ASSIGNMENT.");

        logger.info("*** Step 10 Expected Results: - Verify that the split Accessions' Status is Final Reported (11)");
        Assert.assertEquals(daoManagerXifinRpm.getStatusIdByAccnId(splitAccnId, testDb), 11, "        The split Accession Status for " + splitAccnId + " is not Final Reported (11).");

        logger.info("*** Step 11 Actions: - Turn off the Billing Assignment Req for the Client on the Accession in DB");
        String clnAbbrev = daoManagerXifinRpm.getClientAbbrevIdByAccnId(accnId, testDb);
        daoManagerXifinRpm.setValuesFromCLNByClnAbbrev(clnAbbrev, "B_BILLING_ASSIGNMENT_REQ = 0", testDb);
    }

}
