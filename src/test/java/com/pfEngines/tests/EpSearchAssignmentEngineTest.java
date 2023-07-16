package com.pfEngines.tests;


import com.mbasys.mars.ejb.entity.accnPyrErr.AccnPyrErr;
import com.mbasys.mars.errorProcessing.EpConstants;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.xifin.qa.dao.rpm.domain.QEp;
import com.xifin.qa.dao.rpm.domain.QEpErr;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.ConvertUtil;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
import com.xifin.xap.utils.XifinAdminUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.mbasys.mars.persistance.DatabaseMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class EpSearchAssignmentEngineTest extends SeleniumBaseTest {
    private RandomCharacter randomCharacter;
    private TimeStamp timeStamp;
    private TestDataSetup testDataSetup;
    private ConvertUtil convertUtil;
    private static final String EP_GRP_NAME = "EP_Clerk";

    @BeforeSuite(alwaysRun = true)
    @Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "disableBrowserPlugins"})
    public void beforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, @Optional String disableBrowserPlugins) {
        try {
            logger.info("Running BeforeSuite");
            super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
            updateSystemSetting(SystemSettingMap.SS_FORCE_ACCN_ELIG_ENGINE, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_PERFORM_JOIN_LOGIC, "False", "0");
            updateSystemSetting(SystemSettingMap.SS_JOIN_ACCNS, "False", "0");
            XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
            xifinAdminUtils.clearDataCache();
        } catch (Exception e) {
            Assert.fail("Error running BeforeSuite", e);
        } finally {
            quitWebDriver();
        }
    }

    @Test(priority = 1, description = "Select one filter only and QueuId=4 (Q_EP_MAN)")
    public void testPFER_305() throws Exception {
        logger.info("====== Testing - testPFER_305 ======");

        logger.info("*** Step 1 Actions: Generate a new Search ID");
        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);

        List<String> filter1List = new ArrayList<String>();
        filter1List.add("12");
        filter1List.add("1");
        String descr = randomCharacter.getRandomAlphaString(10);
        List<String> filterVals = new ArrayList<String>();
        String accnId = errorProcessingDao.getRandomAccnIdFromEpSearchVByQueueId(EpConstants.EP_Q_MANUAL_ID);
        filterVals.add("MAN");
        filterVals.add(accnId);
        List<String> orsList = new ArrayList<>();
        orsList.add("0");
        List<String> sortsList = new ArrayList<>();
        sortsList.add("0");
        String userId = usersDao.getUserIdByGrpName(EP_GRP_NAME);
        String searchId = "QAAUTOEPSAE" + randomCharacter.getRandomAlphaString(6);
        String assignmentDate = timeStamp.getCurrentDate("yyyy-MM-dd");
        String assignDt = timeStamp.getCurrentDate("MM/dd/yyyy");

        int accnNumBefore = errorProcessingDao.getEPAssignmentCountByUserAndCurrentDate(userId);
        logger.info("        The Accessions that assigned to User " + userId + " on Date " + assignmentDate + " before running the EPSearchAndAssignmentEngineengine are " + accnNumBefore);

        int addNewEpSrch = accessionDao.insertRecordIntoEpSrch(searchId, descr, filter1List, filterVals, orsList, sortsList, userId);

        logger.info("*** Step 1 Expected Results: The new Search ID  is available for using");
        Assert.assertEquals(addNewEpSrch, 1, "        A new search id should be added into EP_SRCH table.");

        logger.info("*** Step 2 Actions: Wait for EP Search and Assignment  Engine to Run");
        Assert.assertTrue(accessionDao.waitForEPSearchAndAssignEngineToFinishAssignment(userId, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 2 Expected: Verify that data are updated in DB correctly.");
        String msg = qMessageDao.getLatestMessageFromQMessageByUserId(userId);
        QEp qEp = errorProcessingDao.getQEpByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_MAN, accnId);
        QEpErr qEpErr = errorProcessingDao.getQEpErrByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_MAN_ERR, accnId);

        logger.info("Assign Date" + qEp.getAssgnDt());
        Assert.assertEquals(qEp.getUserId(), userId, "        UserID should be updated into Q_EP_MAN table.");
        Assert.assertEquals(qEp.getAssgnDt().toString(), assignmentDate, "        assignmentDate should be updated into Q_EP_MAN table.");
        Assert.assertEquals(qEpErr.getUserId(), userId, "        UserID should be updated into Q_EP_MAN_ERR table.");

        logger.info("*** Step 3 Actions: Clear/Reset test data in DB");
        errorProcessingDao.deleteEpSearchBySrchId(searchId);
        List<String> accnIdList = new ArrayList<>();
        accnIdList.add(accnId);
        errorProcessingDao.resetEpAssignmentInfoByTableNameAndAccnId("Q_EP_MAN", accnIdList);
        errorProcessingDao.resetEpAssignmentInfoByErrTableNameAndAccnId("Q_EP_MAN_ERR", accnIdList);
        errorProcessingDao.deleteFromEpAssignmentByUserIdCurrentDt(userId);
        qMessageDao.deleteFromQMessageByUserIdMsg(userId, msg.trim());
    }

    @Test(priority = 1, description = "Select one filter only and QueuId=6 (Q_EP_HLD)")
    @Parameters({"email", "password", "eType", "xapEnv", "engConfigDB"})
    public void testPFER_307(String email, String password, String eType, String xapEnv, String engConfigDB) throws Exception {
        logger.info("====== Testing - testPFER_307 ======");

        logger.info("*** Step 1 Actions: Generate a new Search ID");
        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);

        List<String> filter1List = new ArrayList<String>();
        filter1List.add("1");
        String descr = randomCharacter.getRandomAlphaString(10);
        List<String> accnIdList = new ArrayList<String>();
        String accnId = errorProcessingDao.getRandomAccnIdFromEpSearchVByQueueId(EpConstants.EP_Q_HOLD_ID);
        accnIdList.add(accnId);
        List<String> orsList = new ArrayList<>();
        orsList.add("0");
        List<String> sortsList = new ArrayList<>();
        sortsList.add("0");
        String userId = usersDao.getUserIdByGrpName(EP_GRP_NAME);
        String searchId = "QAAUTOEPSAE" + randomCharacter.getRandomAlphaString(6);
        String assignmentDate = timeStamp.getCurrentDate("yyyy-MM-dd");
        String assignDt = timeStamp.getCurrentDate("MM/dd/yyyy");

        int accnNumBefore = errorProcessingDao.getEPAssignmentCountByUserAndCurrentDate(userId);
        logger.info("        The Accessions that assigned to User " + userId + " on Date " + assignmentDate + " before running the EPSearchAndAssignmentEngineengine are " + accnNumBefore);

        int addNewEpSrch = accessionDao.insertRecordIntoEpSrch(searchId, descr, filter1List, accnIdList, orsList, sortsList, userId);

        logger.info("*** Step 1 Expected Results: Verify that a new Search ID  is available for using");
        Assert.assertNotEquals(addNewEpSrch, 0, "        A new search id should be added into EP_SRCH table.");

        logger.info("*** Step 2 Actions: Wait for EP Search and Assignment  Engine to Run");
        Assert.assertTrue(accessionDao.waitForEPSearchAndAssignEngineToFinishAssignment(userId, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 2 Expected: Verify that data are updated in DB correctly.");
        String msg = qMessageDao.getLatestMessageFromQMessageByUserId(userId);
        int accnNumAfter = errorProcessingDao.getEPAssignmentCountByUserAndCurrentDate(userId);
        QEp qEp = errorProcessingDao.getQEpByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_HLD, accnIdList.get(0));
        QEpErr qEpErr = errorProcessingDao.getQEpErrByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_HLD_ERR, accnIdList.get(0));

        if (accnNumAfter < 2) {
            Assert.assertEquals(msg, accnNumAfter + " accession has been assigned to you on " + assignDt + ".", "        Message - " + accnNumAfter + " accession has been assigned to you on " + assignDt + "." + " should be added into Q_MESSAGE");
        } else {
            Assert.assertEquals(msg, accnNumAfter + " accessions have been assigned to you on " + assignDt + ".", "        Message - " + accnNumAfter + " accessions have been assigned to you on " + assignDt + "." + " should be added into Q_MESSAGE");
        }
        Assert.assertTrue((accnNumAfter > accnNumBefore), "        Assigned Accession number (ORG) should be greater than " + accnNumBefore + " in EP_ASSGNMNT table after running the Engine.");
        Assert.assertEquals(qEp.getUserId(), userId, "        UserID should be updated into Q_EP_MAN table.");
        Assert.assertEquals(qEp.getAssgnDt().toString(), assignmentDate, "        assignmentDate should be updated into Q_EP_MAN table.");
        Assert.assertEquals(qEpErr.getUserId(), userId, "        UserID should be updated into Q_EP_MAN_ERR table.");

        logger.info("*** Step 3 Actions: Clear/Reset test data in DB");

        logger.info("*** Step 3 Actions: Clear/Reset test data in DB");
        errorProcessingDao.deleteEpSearchBySrchId(searchId);

        errorProcessingDao.resetEpAssignmentInfoByTableNameAndAccnId("Q_EP_HLD", accnIdList);
        errorProcessingDao.resetEpAssignmentInfoByErrTableNameAndAccnId("Q_EP_HLD_ERR", accnIdList);
        errorProcessingDao.deleteFromEpAssignmentByUserIdCurrentDt(userId);
        qMessageDao.deleteFromQMessageByUserIdMsg(userId, msg.trim());
    }

    @Test(priority = 1, description = "Select filters and filter options are ClientAbbrev and AccnId")
    public void testPFER_312() throws Exception {
        logger.info("====== Testing - testPFER_312 ======");

        logger.info("*** Step 1 Actions: - Add new PK_SRCH_ID in EP_SRCH with filters | Filter options are Client Abbrev and Accession ID | Val1 is Cln_Abbrv | Val2 is Accession ID |FK_ASSIGNEE_USER_ID is get from User table | B_DELETE = 0");
        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);

        //Setup Data
        String searchId = "TEST" + randomCharacter.getRandomAlphaString(5);
        String descr = "DESCR" + randomCharacter.getRandomAlphaString(5);
        String userId = "xqatester";
        errorProcessingDao.deleteFromEpAssignmentByUserIdCurrentDt(userId);
        String currentDt = timeStamp.getCurrentDate("yyyy-MM-dd");
        String assignDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        List<String> filterIds = Arrays.asList("8", "1"); // 8 = Client ID, 1 = Accession
        List<String> clnInfo = daoManagerXifinRpm.getInfoFromEPSRCHVByQueueId("6", testDb);
        String clnAbbrev = clnInfo.get(2);
        String accnId = clnInfo.get(1);
        List<String> filterValues = Arrays.asList(clnAbbrev, accnId);
        List<String> accnIdList = Arrays.asList(accnId);
        int addNewEpSrch = accessionDao.insertRecordIntoEpSrch(searchId, descr, filterIds, filterValues, Arrays.asList("0", "0"), Arrays.asList("0", "0"), userId);

        logger.info("*** Step 1 Expected Results: - Verify that a new EP Search is saved into EP_SRCH table in DB");
        Assert.assertEquals(addNewEpSrch, 1, "        New EP Search should be added into EP_SRCH.");
        List<String> epsrchInfo = daoManagerXifinRpm.getEPSrchInfoFromEPSRCHVBySrchId(searchId, testDb);
        Assert.assertEquals(epsrchInfo.get(0), searchId, "       Srch Id " + searchId + " should be saved.");
        Assert.assertEquals(epsrchInfo.get(1), descr, "       Descr " + descr + " should be saved.");
        Assert.assertEquals(epsrchInfo.get(2), "8", "       Filter 1 = 8 should be saved.");
        Assert.assertEquals(epsrchInfo.get(10), clnAbbrev, "       Value 1 = " + clnAbbrev + " should be saved.");
        Assert.assertEquals(epsrchInfo.get(3), "1", "       Filter 2 = 1 should be saved.");
        Assert.assertEquals(epsrchInfo.get(11), accnId, "       Value 2 = " + accnId + " should be saved.");

        logger.info("*** Step 2 Actions: Wait for EP Search and Assignment  Engine to Run");
        Assert.assertTrue(accessionDao.waitForEPSearchAndAssignEngineToFinishAssignment(userId, QUEUE_WAIT_TIME_MS));
        Thread.sleep(QUEUE_POLL_TIME_MS);

        logger.info("*** Step 2 Expected Results: - Verify that data are added/updated in Q_MESSAGE and EP_ASSGNMNT properly");
        String msg = qMessageDao.getLatestMessageFromQMessageByUserId(userId);
        int accsAssigned = errorProcessingDao.getEPAssignmentCountByUserAndCurrentDate(userId);
        if (accsAssigned < 2) {
            Assert.assertEquals(msg, accsAssigned + " accession has been assigned to you on " + assignDt + ".", "        Message - " + accsAssigned + " accession has been assigned to you on " + assignDt + "." + " should be added into Q_MESSAGE");
        } else {
            Assert.assertEquals(msg, accsAssigned + " accessions have been assigned to you on " + assignDt + ".", "        Message - " + accsAssigned + " accessions have been assigned to you on " + assignDt + "." + " should be added into Q_MESSAGE");
        }

        logger.info("*** Step 2 Expected Results: Verify that data are added/updated in Q_EP_HLD and Q_EP_HLD_ERR properly");
        QEp qEp = errorProcessingDao.getQEpByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_HLD, accnIdList.get(0));
        QEpErr qEpErr = errorProcessingDao.getQEpErrByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_HLD_ERR, accnIdList.get(0));

        Assert.assertEquals(qEp.getUserId(), userId, "        UserID " + userId + " should be updated into Q_EP_HLD table.");
        Assert.assertEquals(qEp.getAssgnDt().toString(), currentDt, "        AssignmentDate " + currentDt + " should be updated into Q_EP_HLD table.");
        Assert.assertEquals(qEpErr.getUserId(), userId, "        UserID " + userId + " should be updated into Q_EP_HLD_ERR table.");

        logger.info("*** Step 3 Actions: Clear/Reset test data in DB");
        errorProcessingDao.deleteEpSearchBySrchId(searchId);

        errorProcessingDao.resetEpAssignmentInfoByTableNameAndAccnId("Q_EP_HLD", accnIdList);
        errorProcessingDao.resetEpAssignmentInfoByErrTableNameAndAccnId("Q_EP_HLD_ERR", accnIdList);
        errorProcessingDao.deleteFromEpAssignmentByUserIdCurrentDt(userId);
        qMessageDao.deleteFromQMessageByUserIdMsg(userId, msg.trim());
    }

    @Test(priority = 1, description = "select multiple filters with OR options and QueuId = 6 (Hold)")
    @Parameters({"email", "password", "eType", "xapEnv", "engConfigDB"})
    public void testPFER_314(String email, String password, String eType, String xapEnv, String engConfigDB) throws Exception {
        logger.info("====== Testing - testPFER_314 ======");
        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);

        logger.info("*** Step 1 Actions: - Create new searchId in EP_Srch table");
        String searchId = randomCharacter.getRandomAlphaString(8);
        String descr = randomCharacter.getRandomAlphaString(10);
        ;
        List<String> filterIds = new ArrayList<String>();
        filterIds.add("8"); // 8 = Client ID
        filterIds.add("17"); // 17 = Payor ID
        List<String> filterValues = new ArrayList<String>();
        List<String> epInfoList = daoManagerXifinRpm.getUnassignedEPAccnClnPyrFromEPSRCHVByQueueId("6", testDb);
        String clnAbbrev = epInfoList.get(1);
        String pyrAbbrev = epInfoList.get(2);
        filterValues.add(clnAbbrev);
        filterValues.add(pyrAbbrev);
        logger.info("      List filter vaules:" + filterValues);
        String userId = usersDao.getUserIdByGrpName(EP_GRP_NAME);
        List<String> ors = new ArrayList<String>();
        ors.add("1");
        ors.add("0");
        List<String> sorts = new ArrayList<String>();
        sorts.add("0");
        sorts.add("0");
        int epSearchId = accessionDao.insertRecordIntoEpSrch(searchId, descr, filterIds, filterValues, ors, sorts, userId);

        logger.info("*** Step 1 Expected Results: - Verify that the search id is added to the EP_Srch table");
        Assert.assertTrue(epSearchId == 1, "        A new record " + epSearchId + " is added into EP_Srch table.");
        ArrayList<String> listAccn = daoManagerXifinRpm.getUnassignedInfoFromEPSRCHVByClnPyrAbbrevQId(clnAbbrev, pyrAbbrev, "6", testDb);
        int countAccn = listAccn.size();

        logger.info("*** Step 2 Actions: Wait for EP Search and Assignment  Engine to Run");
        Assert.assertTrue(accessionDao.waitForEPSearchAndAssignEngineToFinishAssignment(userId, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 2 Expected Results: - Verify that a new record is added to EP_Assgnmt table");
        String currentDt = timeStamp.getCurrentDate("yyyy-MM-dd");
        String assignDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        int accnsAssigned = errorProcessingDao.getEPAssignmentCountByUserAndCurrentDate(userId);

        Assert.assertTrue(accnsAssigned >= countAccn, "        The number of Accession are assigned correctly");


        logger.info("*** Step 2 Expected Results: - Verify that a new record is added into Q_Message table");
        String message = qMessageDao.getLatestMessageFromQMessageByUserId(userId);
        Assert.assertTrue(message.contains(assignDt), "        New record is added into Q_Message table");

        logger.info("*** Step 2 Expected results: - Verify that data in Q_EP_HLD, Q_EP_HLD_ERR are updated properly");
        QEp qEp = errorProcessingDao.getQEpByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_HLD, listAccn.get(0));
        QEpErr qEpErr = errorProcessingDao.getQEpErrByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_HLD_ERR, listAccn.get(0));
        Assert.assertEquals(qEp.getUserId(), userId, "        PK_User_ID is updated properly");
        Assert.assertEquals(qEp.getAssgnDt().toString(), currentDt, "        Assgn_Dt is updated properly");
        Assert.assertEquals(qEpErr.getUserId(), userId, "        PK_USer_ID is updated in Q_EP_HLD_ERR table properly");

        logger.info("*** Step 3 Actions: Clear/Reset test data in DB");

        errorProcessingDao.deleteEpSearchBySrchId(searchId);

        errorProcessingDao.resetEpAssignmentInfoByTableNameAndAccnId("Q_EP_HLD", listAccn);
        errorProcessingDao.resetEpAssignmentInfoByErrTableNameAndAccnId("Q_EP_HLD_ERR", listAccn);
        errorProcessingDao.deleteFromEpAssignmentByUserIdCurrentDt(userId);
        qMessageDao.deleteFromQMessageByUserIdMsg(userId, message.trim());
    }

    @Test(priority = 1, description = "Verify Sort options")
    public void testPFER_316() throws Exception {
        logger.info("====== Testing - testPFER_316 ======");
        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);

        logger.info("*** Step 1 Actions: - Create new searchId with 4 Sort Ids from EP_SRCH_FLD with b_sort = 1 in EP_Srch table");
        //Setup Data
        String srchId = "TEST" + randomCharacter.getRandomAlphaString(5);
        String descr = "DESCR" + randomCharacter.getRandomAlphaString(5);
        String userId = "xqatester";
        errorProcessingDao.deleteFromEpAssignmentByUserIdCurrentDt(userId);
        String currentDt = timeStamp.getCurrentDate("yyyy-MM-dd");
        String assignDt = timeStamp.getCurrentDate("MM/dd/yyyy");
        List<String> filterIds = Arrays.asList("27", "1"); // 27 = Test Code, 1 = Accession ID
        List<String> testInfo = daoManagerXifinRpm.getTestAbbrevForEPSearchFromACCNTESTByQueueId("6", testDb);
        String testAbbrev1 = testInfo.get(0);
        String testAbbrev2 = testInfo.get(2);
        String testIds = testAbbrev1 + "," + testAbbrev2;
        String accnId1 = testInfo.get(1);
        String accnId2 = testInfo.get(3);
        String accnIds = accnId1 + "," + accnId2;
        List<String> filterValues = Arrays.asList(testIds, accnIds);
        List<String> accnInfo = Arrays.asList(accnId1, accnId2);//daoManagerXifinRpm.getAccnIdFromEPSRCHVByTestAbbrev(testInfo.get(0), testDb);

        List<String> epSrchFldSortIdList = daoManagerXifinRpm.getEpSrchFldSortIdFromEPSRCHFLD(testDb);
        String sort1 = epSrchFldSortIdList.get(0);
        String sort2 = epSrchFldSortIdList.get(1);
        String sort3 = epSrchFldSortIdList.get(2);
        String sort4 = epSrchFldSortIdList.get(3);

        int rowEpSrch = accessionDao.insertRecordIntoEpSrch(srchId, descr, filterIds, filterValues, Arrays.asList("0"), epSrchFldSortIdList, userId);

        logger.info("*** Step 2 Expected Results: - Verify that a new EP Search id with sort options is added to EP_SRCH table");
        List<String> epsrchInfo = daoManagerXifinRpm.getEPSrchInfoFromEPSRCHVBySrchId(srchId, testDb);
        Assert.assertEquals(rowEpSrch, 1, "       A new EP Search should be added into EP_SRCH table.");
        Assert.assertEquals(epsrchInfo.get(0), srchId, "       Srch Id " + srchId + " is saved.");
        Assert.assertEquals(epsrchInfo.get(1), descr, "       Descr " + descr + " is saved.");
        Assert.assertEquals(epsrchInfo.get(2), "27", "       Filter 1 = 27 is saved.");
        Assert.assertEquals(epsrchInfo.get(10), testIds, "       Value 1 = " + testIds + " is saved.");
        Assert.assertEquals(epsrchInfo.get(3), "1", "       Filter 2 = 1 is saved.");
        Assert.assertEquals(epsrchInfo.get(11), accnIds, "       Value 2 = " + accnIds + " is saved.");
        Assert.assertEquals(epsrchInfo.get(19), sort1, "       sort1 = " + sort1 + " is saved.");
        Assert.assertEquals(epsrchInfo.get(20), sort2, "       sort2 = " + sort2 + " is saved.");
        Assert.assertEquals(epsrchInfo.get(21), sort3, "       sort3 = " + sort3 + " is saved.");
        Assert.assertEquals(epsrchInfo.get(22), sort4, "       sort4 = " + sort4 + " is saved.");

        logger.info("*** Step 3 Actions: Wait for EP Search and Assignment  Engine to Run");
        Assert.assertTrue(accessionDao.waitForEPSearchAndAssignEngineToFinishAssignment(userId, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 3 Expected Results: - Verify that the data in DB are updated properly. New records are added into Q_Message and EP_Assgmnt with the corect Pk_User_Id and Pk_Assgnt_dt");
        String message = qMessageDao.getLatestMessageFromQMessageByUserId(userId);
        Assert.assertTrue(message.contains(assignDt), "        New record is add into Q_Message table.");


        String[] accnArray = new String[accnInfo.size()];
        accnArray = accnInfo.toArray(accnArray);

        for (String accnId : accnArray) {
            QEp qEp = errorProcessingDao.getQEpByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_HLD, accnId);
            QEpErr qEpErr = errorProcessingDao.getQEpErrByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_HLD_ERR, accnId);
            if (qEp != null) {
                Assert.assertEquals(qEp.getUserId(), userId, "       The record in Q_EP_HLD are updated correct");
                Assert.assertEquals(qEp.getAssgnDt().toString(), currentDt, "       The record in Q_EP_HLD are updated correct");
                Assert.assertEquals(qEpErr.getUserId(), userId, "       The record in Q_EP_HLD_ERR are updated correct with Fk_user_id");
            }
        }

        logger.info("*** Step 4 Actions: Clear/Reset test data in DB");
        errorProcessingDao.deleteEpSearchBySrchId(srchId);

        errorProcessingDao.resetEpAssignmentInfoByTableNameAndAccnId("Q_EP_HLD", accnInfo);
        errorProcessingDao.resetEpAssignmentInfoByErrTableNameAndAccnId("Q_EP_HLD_ERR", accnInfo);
        errorProcessingDao.deleteFromEpAssignmentByUserIdCurrentDt(userId);
        qMessageDao.deleteFromQMessageByUserIdMsg(userId, message.trim());
    }

    @Test(priority = 1, description = "Select multiple filters and the options are POS, EP Queue and Accn Age")
    @Parameters({"errCdId3", "project", "testSuite", "testCase"})
    public void testPFER_304(String errCdId3, String project, String testSuite, String testCase) throws Exception {
        logger.info("====== Testing - testPFER_304 ======");
        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        convertUtil = new ConvertUtil();
        String currDt = timeStamp.getCurrentDate();
        String currDate = timeStamp.getCurrentDate("yyyy-MM-dd");
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        logger.info("*** Actions: - Send WS request to create a new Priced 3rd party payor accession");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());

        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("        AccnID: " + accnId);

        logger.info("*** Expected Results: - Verify that a new accession was generated");
        Assert.assertTrue(accnId != null, "        Did not generate a new Accession.");

        AccnPyrErr ape = new AccnPyrErr();
        int errSeqId = daoManagerPlatform.getNextvalFromDual("ACCN_PYR_ERR_SEQ", testDb);
        ape.setErrSeq(errSeqId);
        ape.setAccnId(accnId);
        ape.setPyrPrio(1);
        ape.setErrCd(Integer.valueOf(errCdId3));
        ape.setErrDt(new java.sql.Date(sysDt.getTime()));
        ape.setIsPosted(true);
        accessionDao.setAccnPyrErr(ape);

        ArrayList<String> clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
        if (!clnBillingAssignInfoList.isEmpty()) {
            daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 2", testDb);
            logger.info("Waiting for accession to complete BA, accnId=" + accnId);
            Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));
        }
        logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));
        logger.info("*** Actions: - Wait for PF EP Workflow Engine");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 1 Actions: Add new PK_SRCH_ID in EP_SRCH with multiple filters and the options are POS, EP Queue and Accn Age");
        //Setup Data
        String searchId = "TEST" + randomCharacter.getRandomAlphaString(5);
        String descr = "DESCR" + randomCharacter.getRandomAlphaString(5);
        String userId = usersDao.getUserIdByGrpName(EP_GRP_NAME); //Select users that can access EP screens
        String currentDt = timeStamp.getCurrentDate();
        String currentDate = timeStamp.getCurrentDate("yyyy-MM-dd");
        List<String> filterIds = new ArrayList<String>();
        filterIds.add("46"); //EP_SRCH_FLD = 46 (POS)
        filterIds.add("12"); //EP_SRCH_FLD = 12 (EP Queue)
        filterIds.add("41"); //41 (Age of Accn <)
        String pos = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
        List<String> filterValues = new ArrayList<String>();
        filterValues.add(pos);
        filterValues.add("MAN"); //Q_EP_MAN
        filterValues.add("2");
        List<String> accnInfo = new ArrayList<>();
        accnInfo.add(accnId);
        //Add a new search into EP_SRCH
        int addNewEpSrch = accessionDao.insertRecordIntoEpSrch(searchId, descr, filterIds, filterValues, Arrays.asList("0", "0", "0"), Arrays.asList("0", "0", "0"), userId);

        logger.info("*** Step 1 Expected Results: Verify that a new search is saved properly into EP_SRCH");
        Assert.assertEquals(addNewEpSrch, 1, "        New EP Search Id " + searchId + " should be added into EP_SRCH.");

        List<String> epsrchInfo = daoManagerXifinRpm.getEPSrchInfoFromEPSRCHVBySrchId(searchId, testDb);
        Assert.assertEquals(epsrchInfo.get(0), searchId, "       Srch Id " + searchId + " should be saved.");
        Assert.assertEquals(epsrchInfo.get(1), descr, "       Descr " + descr + " should be saved.");
        Assert.assertEquals(epsrchInfo.get(2), "46", "       Filter 1 = 46 should be saved.");
        Assert.assertEquals(epsrchInfo.get(10), pos, "       Value 1 = " + pos + " should be saved.");
        Assert.assertEquals(epsrchInfo.get(3), "12", "       Filter 2 = 12 should be saved.");
        Assert.assertEquals(epsrchInfo.get(11), "MAN", "       Value 2 = MAN should be saved.");
        Assert.assertEquals(epsrchInfo.get(4), "41", "       Filter 3 = 41 should be saved.");
        Assert.assertEquals(epsrchInfo.get(12), "2", "       Value 3 = 2 should be saved.");

        logger.info("*** Step 2 Actions: Wait for EP Search and Assignment  Engine to Run");
        Assert.assertTrue(accessionDao.waitForEPSearchAndAssignEngineToFinishAssignment(userId, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 2 Expected Results: Verify that data are added/updated in Q_MESSAGE and EP_ASSGNMNT properly");
        String msg = qMessageDao.getLatestMessageFromQMessageByUserId(userId);
        int accnsAssigned = errorProcessingDao.getEPAssignmentCountByUserAndCurrentDate(userId);
        if (accnsAssigned < 2) {
            Assert.assertEquals(msg, accnsAssigned + " accession has been assigned to you on " + currentDt + ".", "        Message - " + accnsAssigned + " accession has been assigned to you on " + currentDt + "." + " should be added into Q_MESSAGE");
        } else {
            Assert.assertEquals(msg, accnsAssigned + " accessions have been assigned to you on " + currentDt + ".", "        Message - " + accnsAssigned + " accessions have been assigned to you on " + currentDt + "." + " should be added into Q_MESSAGE");
        }

        logger.info("*** Step 2 Expected Results: Verify that data are added/updated in Q_EP_MAN and Q_EP_MAN_ERR properly");
        accnInfo = daoManagerXifinRpm.getAccnIdListByFacAbbrevQueueIdAccnAgeUserId("4", pos, "100", userId, testDb);
        QEp qEp = errorProcessingDao.getQEpByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_MAN, accnInfo.get(0));
        QEpErr qEpErr = errorProcessingDao.getQEpErrByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_MAN_ERR, accnInfo.get(0));

        Assert.assertEquals(qEp.getUserId(), userId, "        UserID should be updated into Q_EP_MAN table.");
        Assert.assertEquals(qEp.getAssgnDt().toString(), currentDate, "        assignmentDate should be updated into Q_EP_MAN table.");
        Assert.assertEquals(qEpErr.getUserId(), userId, "        UserID should be updated into Q_EP_MAN table.");

        logger.info("*** Step 3 Actions: Clear/Reset test data in DB");
        errorProcessingDao.deleteEpSearchBySrchId(searchId);

        //Revert data into EP_SRCH:

        errorProcessingDao.resetEpAssignmentInfoByTableNameAndAccnId("Q_EP_MAN", accnInfo);
        errorProcessingDao.resetEpAssignmentInfoByErrTableNameAndAccnId("Q_EP_MAN", accnInfo);
        errorProcessingDao.deleteFromEpAssignmentByUserIdCurrentDt(userId);
        qMessageDao.deleteFromQMessageByUserIdMsg(userId, msg.trim());
    }

    @Test(priority = 1, description = "Select one filter only and QueuId=5 (Q_EP_CORRESP)")
    @Parameters({"errCdId3", "project", "testSuite", "testCase"})
    public void testPFER_306(String errCdId3, String project, String testSuite, String testCase) throws Exception {
        logger.info("====== Testing - testPFER_306 ======");
        randomCharacter = new RandomCharacter(driver);
        convertUtil = new ConvertUtil();
        timeStamp = new TimeStamp(driver);
        String currDt = timeStamp.getCurrentDate();
        Date sysDt = convertUtil.convertStringToUtilDate(currDt, "MM/dd/yyyy");

        logger.info("*** Actions: - Send WS request to create a new Priced 3rd party payor accession");
        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());

        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("        AccnID: " + accnId);

        logger.info("*** Expected Results: - Verify that a new accession was generated");
        Assert.assertTrue(accnId != null, "        Did not generate a new Accession.");

        AccnPyrErr ape = new AccnPyrErr();
        int errSeqId = daoManagerPlatform.getNextvalFromDual("ACCN_PYR_ERR_SEQ", testDb);
        ape.setErrSeq(errSeqId);
        ape.setAccnId(accnId);
        ape.setPyrPrio(1);
        ape.setErrCd(Integer.valueOf(errCdId3));
        ape.setErrDt(new java.sql.Date(sysDt.getTime()));
        ape.setIsPosted(true);
        accessionDao.setAccnPyrErr(ape);

        ArrayList<String> clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
        if (!clnBillingAssignInfoList.isEmpty()) {
            daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 2", testDb);
            logger.info("Waiting for accession to complete BA, accnId=" + accnId);
            Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));
        }
        logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));
        logger.info("*** Actions: - Wait for PF EP Workflow Engine");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 1 Actions: Generate a new Search ID");
        List<String> filter1List = new ArrayList<String>();
        filter1List.add("1");
        String descr = randomCharacter.getRandomAlphaString(10);
        List<String> accnIdList = new ArrayList<String>();
        accnIdList.add(accnId);
        List<String> orsList = new ArrayList<>();
        orsList.add("0");
        List<String> sortsList = new ArrayList<>();
        sortsList.add("0");
        String userId = usersDao.getUserIdByGrpName(EP_GRP_NAME);
        String searchId = "QAAUTOEPSAE" + randomCharacter.getRandomAlphaString(6);
        String assignmentDate = timeStamp.getCurrentDate();
        String assignDt = timeStamp.getCurrentDate("yyyy-MM-dd");

        int accnNumBefore = errorProcessingDao.getEPAssignmentCountByUserAndCurrentDate(userId);
        logger.info("        The Accessions that assigned to User " + userId + " on Date " + assignmentDate + " before running the EPSearchAndAssignmentEngineengine are " + accnNumBefore);

        int addNewEpSrch = accessionDao.insertRecordIntoEpSrch(searchId, descr, filter1List, accnIdList, orsList, sortsList, userId);

        logger.info("*** Step 1 Expected Results: Verify that the new Search ID  is available for using");
        Assert.assertNotEquals(addNewEpSrch, 0, "        A new search id should be added into EP_SRCH table.");

        logger.info("*** Step 2 Actions: Wait for EP Search and Assignment  Engine to Run");
        Assert.assertTrue(accessionDao.waitForEPSearchAndAssignEngineToFinishAssignment(userId, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 2 Expected Results: Verify that data are updated in DB correctly.");
        String msg = qMessageDao.getLatestMessageFromQMessageByUserId(userId);
        int accnNumAfter = errorProcessingDao.getEPAssignmentCountByUserAndCurrentDate(userId);
        QEp qEp = errorProcessingDao.getQEpByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_CORRESP, accnIdList.get(0));
        QEpErr qEpErr = errorProcessingDao.getQEpErrByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_CORRESP_ERR, accnIdList.get(0));

        if (accnNumAfter < 2) {
            Assert.assertEquals(msg, accnNumAfter + " accession has been assigned to you on " + assignmentDate + ".", "        Message - " + String.valueOf(accnNumAfter) + " accession has been assigned to you on " + assignmentDate + "." + " should be added into Q_MESSAGE");
        } else {
            Assert.assertEquals(msg, accnNumAfter + " accessions have been assigned to you on " + assignmentDate + ".", "        Message - " + String.valueOf(accnNumAfter) + " accessions have been assigned to you on " + assignmentDate + "." + " should be added into Q_MESSAGE");
        }
        Assert.assertTrue((accnNumAfter > accnNumBefore), "        Assigned Accession number (ORG) should be greater than " + accnNumBefore + " in EP_ASSGNMNT table after running the Engine.");
        Assert.assertEquals(qEp.getUserId(), userId, "        UserID should be updated into Q_EP_CORRESP table.");
        Assert.assertEquals(qEp.getAssgnDt().toString(), assignDt, "        assignmentDate should be updated into Q_EP_CORRESP table.");
        Assert.assertEquals(qEpErr.getUserId(), userId, "        UserID should be updated into Q_EP_CORRESP_ERR table.");

        logger.info("*** Step 3 Actions: Clear/Reset test data in DB");

        errorProcessingDao.deleteEpSearchBySrchId(searchId);

        errorProcessingDao.resetEpAssignmentInfoByTableNameAndAccnId("Q_EP_CORRESP", accnIdList);
        errorProcessingDao.resetEpAssignmentInfoByErrTableNameAndAccnId("Q_EP_CORRESP_ERR", accnIdList);
        errorProcessingDao.deleteFromEpAssignmentByUserIdCurrentDt(userId);
        qMessageDao.deleteFromQMessageByUserIdMsg(userId, msg.trim());
    }

    @Test(priority = 1, description = "select one filter only and QueuId=7 (Q_EP_OUT_AGNCY)")
    public void testPFER_303() throws Exception {
        logger.info("====== Testing - testPFER_303 ======");
        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);
        convertUtil = new ConvertUtil();
        String currDt = timeStamp.getCurrentDate();

        String userId = "xqatester";
        errorProcessingDao.deleteFromEpAssignmentByUserIdCurrentDt(userId);
        logger.info("*** Step 1 Actions: Generate a new Search ID");
        List<String> filter1List = new ArrayList<String>();
        filter1List.add("12");
        filter1List.add("1");
        String descr = randomCharacter.getRandomAlphaString(10);
        List<String> filterVals = new ArrayList<String>();
        filterVals.add("OAPOST");
        String accnId = errorProcessingDao.getRandomAccnIdFromEpSearchVByQueueId(EpConstants.EP_Q_OUTSIDE_ID);

        logger.info("accnId =" + accnId);
        filterVals.add(accnId);
        logger.info("filtervals 1=" + filterVals.get(0));
        logger.info("filtervals 1=" + filterVals.get(1));
        List<String> orsList = new ArrayList<>();
        orsList.add("0");
        List<String> sortsList = new ArrayList<>();
        sortsList.add("0");
        String searchId = "QAAUTOEPSAE" + randomCharacter.getRandomAlphaString(6);
        String assignDt = timeStamp.getCurrentDate("yyyy-MM-dd");

        int addNewEpSrch = accessionDao.insertRecordIntoEpSrch(searchId, descr, filter1List, filterVals, orsList, sortsList, userId);

        logger.info("*** Step 1 Expected Results: Verify that the new Search ID  is available for using");
        Assert.assertNotEquals(addNewEpSrch, 0, "        A new search id should be added into EP_SRCH table.");

        logger.info("*** Step 2 Actions: Wait for EP Search and Assignment  Engine to Run");
        Assert.assertTrue(accessionDao.waitForEPSearchAndAssignEngineToFinishAssignment(userId, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 2 Expected Results: Verify that data are updated in DB correctly.");
        String msg = qMessageDao.getLatestMessageFromQMessageByUserId(userId);
        QEp qEp = errorProcessingDao.getQEpByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_OUT_AGNCY, accnId);
        QEpErr qEpErr = errorProcessingDao.getQEpErrByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_OUT_AGNCY_ERR, accnId);

        Assert.assertEquals(qEp.getUserId(), userId, "        UserID should be updated into Q_EP_OUT_AGNCY table.");
        Assert.assertEquals(qEp.getAssgnDt().toString(), assignDt, "        assignmentDate should be updated into Q_EP_OUT_AGNCY table.");
        Assert.assertEquals(qEpErr.getUserId(), userId, "        UserID should be updated into Q_EP_OUT_AGNCY_ERR table.");

        logger.info("*** Step 3 Actions: Clear/Reset test data in DB");

        errorProcessingDao.deleteEpSearchBySrchId(searchId);

        List<String> accnIdList = new ArrayList<>();
        accnIdList.add(accnId);
        errorProcessingDao.resetEpAssignmentInfoByTableNameAndAccnId("Q_EP_OUT_AGNCY", accnIdList);
        errorProcessingDao.resetEpAssignmentInfoByErrTableNameAndAccnId("Q_EP_OUT_AGNCY_ERR", accnIdList);
        errorProcessingDao.deleteFromEpAssignmentByUserIdCurrentDt(userId);
        qMessageDao.deleteFromQMessageByUserIdMsg(userId, msg.trim());
    }


    @Test(priority = 1, description = "Fix LCNOABN error and add GZ Modifier")
    @Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword", "eType", "xapEnv", "engConfigDB"})
    public void testPFER_475(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String eType, String xapEnv, String engConfigDB) throws Exception {
        logger.info("====== Testing - testPFER_475 ======");
        testDataSetup = new TestDataSetup(driver);
        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);


        Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
        String accnId = testProperties.getProperty("NewAccnID");
        logger.info("        AccnID: " + accnId);

        logger.info("*** Step 1 Expected Results: - Verify that a new Accession was generated");
        Assert.assertTrue(accnId != null, "        Did not generate a new Accession.");

        ArrayList<String> clnBillingAssignInfoList = daoManagerPlatform.getClnBillingAssignInfoFromQCLNBILLINGASSIGNMENTByAccnId(accnId, testDb);
        if (!clnBillingAssignInfoList.isEmpty()) {
            daoManagerPlatform.setValuesFromQCLNBILLINGASSIGNMENTByAccnId(accnId, "FK_NEW_BILLING_ASSIGN_TYP_ID = 2", testDb);
            logger.info("Waiting for accession to complete BA, accnId=" + accnId);
            Assert.assertTrue(accessionDao.waitForAccnToBeProcessedByBAE(accnId, QUEUE_WAIT_TIME_MS));
        }
        logger.info("Waiting for accession to exit q_fr_pending queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit eligibility queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_ELIG, QUEUE_WAIT_TIME_MS));
        logger.info("Waiting for accession to exit pricing queue, accnId=" + accnId);
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));
        logger.info("*** Actions: - Wait for PF EP Workflow Engine");
        Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_EP_UNBILLABLE, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 2 Actions: - Generate a new Search ID with EP_AUTO_ACTION_TYP_ID = 2 (Schedule Search and add GZ modifiers)");
        List<String> filter1List = new ArrayList<String>();
        filter1List.add("1");
        String descr = randomCharacter.getRandomAlphaString(10);
        List<String> accnIdList = new ArrayList<String>();
        accnIdList.add(accnId);
        List<String> orsList = new ArrayList<>();
        orsList.add("0");
        List<String> sortsList = new ArrayList<>();
        sortsList.add("0");
        String searchId = "QAAUTOEPSAE" + randomCharacter.getRandomAlphaString(6);
        int addNewEpSrch = accessionDao.insertRecordIntoEpSrch(searchId, descr, filter1List, accnIdList, orsList, sortsList, "");

        logger.info("*** Step 2 Expected Results: - Verify that a new Search ID is available for using");
        Assert.assertNotEquals(addNewEpSrch, 0, "        A new search id should be added into EP_SRCH table.");
        // Set FK_EP_AUTO_ACTION_TYP_ID = 2 (Schedule Search and add GZ modifiers)
        daoManagerPlatform.setValuesFromTable("EP_SRCH", "FK_EP_AUTO_ACTION_TYP_ID = 2", "PK_SRCH_ID = '" + searchId + "'", testDb);


        logger.info("*** Step 3 Expected Results: - Verify that Q_EP_CORRESP has a record available for testing");
        Map<String, List<String>> qEPList = daoManagerXifinRpm.getAssgnmntInfoByTableNameAccnId("Q_EP_CORRESP", "MM/dd/yyyy", accnIdList, testDb);

        Assert.assertTrue(qEPList.size() > 0, "       A new record with Accession ID = " + accnId + " should be inserted into Q_EP_CORRESP.");

        logger.info("*** Step 4 Actions: -Wait For EP Search and Assignment  Engine");
        Assert.assertTrue(accessionDao.waitForEPSearchAndAssignEngineToFinishFixingLCNOABNErrs(accnId, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 4 Expected: - Verify that the LCNOABN error is fixed");
        String currDt = timeStamp.getCurrentDate();
        String procCd = daoManagerAccnWS.getNonProfAccnTestInfoFromACCNTESTByAccnId(accnId, testDb).get(0);
        ArrayList<String> accnProcErrList = daoManagerAccnWS.getAccnProcErrInfoFromACCNPROCERRByAccnIdErrCdErrDtProcId(accnId, "1294", currDt, procCd, testDb);

        Assert.assertEquals(timeStamp.getConvertedDate("yyyy-MM-dd HH:mm:ss", "MM/dd/yyyy", accnProcErrList.get(0)), currDt, "      The LCNOABN error should be fixed.");
        int accnProcSeqId = Integer.valueOf(accnProcErrList.get(1));

        logger.info("*** Step 4 Expected: - Verify that the GZ modifier is added");
        ArrayList<String> accnProcInfList = daoManagerPlatform.getAccnInfoFromACCNPROCByAccnId(accnProcSeqId, accnId, testDb);
        Assert.assertEquals(accnProcInfList.get(2), "GZ", "      GZ modifier should be added.");

        logger.info("*** Step 4 Expected: - Verify that the Accession is pushed into Q_VALIDATE_ACCN with VALIDATE_TYP_ID = 6 (RE_ASSSESS_START_OVER) and NEW_PYR_ID = 0 (NO_PAYOR_CHANGE)");
        ArrayList<String> qValidateAccnInfoList = daoManagerPlatform.getQValidateAccnInfoFromQVALIDATEACCNByAccnIdInDt(accnId, currDt, testDb);
        Assert.assertEquals(qValidateAccnInfoList.get(0), "0", "      Q_VALIDATE_ACCN.FK_NEW_PYR_ID = 0");
        Assert.assertEquals(qValidateAccnInfoList.get(1), "6", "      Q_VALIDATE_ACCN.FK_VALIDATE_TYP_ID = 6");

        logger.info("*** Step 5 Actions: Clear/Reset test data in DB");
        errorProcessingDao.deleteEpSearchBySrchId(searchId);
        daoManagerPlatform.deleteFromQEPHLDByAccnId(accnId, testDb);

    }

    @Test(priority = 1, description = "select one filter only and QueuId=8 (Q_EP_OUT_AGNCY_PRE_CORRESP)")
    @Parameters({"email", "password", "eType", "xapEnv", "engConfigDB"})
    public void testPFER_602(String email, String password, String eType, String xapEnv, String engConfigDB) throws Exception {
        logger.info("====== Testing - testPFER_602 ======");
        randomCharacter = new RandomCharacter(driver);
        timeStamp = new TimeStamp(driver);

        String userId = "xqatester";
        errorProcessingDao.deleteFromEpAssignmentByUserIdCurrentDt(userId);
        logger.info("*** Step 1 Actions: Generate a new Search ID");
        List<String> filter1List = new ArrayList<String>();
        filter1List.add("12");
        filter1List.add("1");
        String descr = randomCharacter.getRandomAlphaString(10);
        List<String> filterVals = new ArrayList<String>();
        filterVals.add("OAPRE");
        String accnId = errorProcessingDao.getRandomAccnIdFromEpSearchVByQueueId(EpConstants.EP_Q_OUTSIDE_PRE_CORRESP_ID);
        filterVals.add(accnId);
        List<String> orsList = new ArrayList<>();
        orsList.add("0");
        List<String> sortsList = new ArrayList<>();
        sortsList.add("0");
        String searchId = "QAAUTOEPSAE" + randomCharacter.getRandomAlphaString(6);
        String assignmentDate = timeStamp.getCurrentDate();
        String assignDt = timeStamp.getCurrentDate("yyyy-MM-dd");

        int addNewEpSrch = accessionDao.insertRecordIntoEpSrch(searchId, descr, filter1List, filterVals, orsList, sortsList, userId);

        logger.info("*** Step 1 Expected Results: Verify that the new Search ID  is available for using");
        Assert.assertNotEquals(addNewEpSrch, 0, "        A new search id should be added into EP_SRCH table.");

        logger.info("*** Step 2 Actions: Wait for EP Search and Assignment  Engine to Run");
        Assert.assertTrue(accessionDao.waitForEPSearchAndAssignEngineToFinishAssignment(userId, QUEUE_WAIT_TIME_MS));

        logger.info("*** Step 2 Expected Results: Verify that data are updated in DB correctly.");
        String msg = qMessageDao.getLatestMessageFromQMessageByUserId(userId);
        QEp qEp = errorProcessingDao.getQEpByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_OUT_AGNCY_PRE_CORRESP, accnId);
        QEpErr qEpErr = errorProcessingDao.getQEpErrByTableNameAndAccnId(DatabaseMap.TBL_Q_EP_OUT_AGNCY_PRE_CORRESP_ERR, accnId);

        Assert.assertEquals(qEp.getUserId(), userId, "        UserID should be updated into TBL_Q_EP_OUT_AGNCY_PRE_CORRESP table.");
        Assert.assertEquals(qEp.getAssgnDt().toString(), assignDt, "        assignmentDate should be updated into Q_EP_OUT_AGNCY_PRE_CORRESP table.");
        Assert.assertEquals(qEpErr.getUserId(), userId, "        UserID should be updated into Q_EP_OUT_AGNCY_PRE_CORRESP_ERR table.");

        logger.info("*** Step 3 Actions: Clear/Reset test data in DB");

        errorProcessingDao.deleteEpSearchBySrchId(searchId);
        List<String> accnIdList = new ArrayList<>();
        accnIdList.add(accnId);
        errorProcessingDao.resetEpAssignmentInfoByTableNameAndAccnId("Q_EP_OUT_AGNCY_PRE_CORRESP", accnIdList);
        errorProcessingDao.resetEpAssignmentInfoByErrTableNameAndAccnId("Q_EP_OUT_AGNCY_PRE_CORRESP_ERR", accnIdList);
        errorProcessingDao.deleteFromEpAssignmentByUserIdCurrentDt(userId);
        qMessageDao.deleteFromQMessageByUserIdMsg(userId, msg.trim());

    }

}
