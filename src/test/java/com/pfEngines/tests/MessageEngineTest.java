package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.qMessage.QMessage;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MessageEngineTest extends SeleniumBaseTest {

    private TimeStamp timeStamp;
    private XifinAdminUtils xifinAdminUtils;
    private RandomCharacter randomCharacter;

    @Test(priority = 1, description = "PyrContrct-Send expired message to managers when review_dt<SYSDATE,end_dt=SYSDATE")
    public void testPFER_29() throws Exception {
        logger.info("***** Testing - PFER-29:Message Engine-PyrContrct-Send expired message to managers when review_dt<SYSDATE,end_dt=SYSDATE  *****");

        logger.info("*** Step 1 Actions: - Add new PYR_CONTRCT with review_dt < SYSDATE & end_dt = SYSDATE, msg_status = 0 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        String newContrctID = randomCharacter.getRandomAlphaNumericString(10);
        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CONTRCT_ID");
        listCol.add("REVIEW_DT");
        listCol.add("END_DT");
        listCol.add("MSG_STATUS");
        listCol.add("RPT_FRM_ID");
        listCol.add("START_DT");
        listCol.add("FK_ADMIN_CNTRY_ID");
        listCol.add("FK_CONTRCT_COVRG_ID");
        listCol.add("FREQ_PATTERN_ID");
        listCol.add("CONTRCT_NM");
        listCol.add("ADMIN_NM");

        listVal.add("'" + newContrctID + "'");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')-2");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("2");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("4");
        listVal.add("2");
        listVal.add("'AUTOTEST" + newContrctID + "'");
        listVal.add("'AUTOTEST" + newContrctID + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("PYR_CONTRCT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of PYR_CONTRCT was inserted to database");
        Assert.assertTrue(totalRowInserted > 0, "       No new PYR_CONTRCT record is inserted into to the table.");

        logger.info("*** Step 3 Actions: - Add PYR_DT with pyr_eff_dt <= SYSDATE , FK_CONTRCT_ID = contrct was created step 2 in DB");
        Pyr listPyr = payorDao.getPyrFromPyrDtAndPyr();
        int pyrId = listPyr.getPyrId();
        String pyrAbbrev = listPyr.getPyrAbbrv();

        totalRowInserted = daoManagerPlatform.addNewPyrDtWContractFromPYRDTByPyrIdContractId(String.valueOf(pyrId), newContrctID, testDb);

        logger.info("*** Step 3 Expected Results: - New row of PYR_DT was inserted to database");
        Assert.assertTrue(totalRowInserted > 0, "       New PYR_DT can't be inserted to the table.");

        logger.info("*** Step 4 Actions: - Clear System cache");
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("PYR_CONTRCT", "PK_CONTRCT_ID='" + newContrctID + "'", 1, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 1, "        The Message Status should not be updated.");

        logger.info("*** Step 5 Expected Results: - Verify message add to Q_MESSAGE table");
        String prio = "MED";
        String subject = "Contract Expired " + pyrAbbrev;
        Assert.assertTrue(daoManagerPlatform.getPyrMessageFromQ_Message(pyrAbbrev, prio, subject, testDb).size() > 0, "       Contract Expired messages should be added to Q_MESSAGE table.");

        logger.info("*** Step 6 Actions: - Delete the Contract in DB");
        payorDao.deletePyrDtByPyrIdSysDtContrctId(pyrId, newContrctID);
        payorDao.deletePyrCntrctByContrctId(newContrctID);

        logger.info("*** Step 7 Actions: - Clear System cache");
        xifinAdminUtils.clearDataCache();
    }

    @Test(priority = 1, description = "PyrContrct-Send expiring messages to assigned user when end_dt > sysdate")
    public void testPFER_55() throws Exception {
        logger.info("***** Testing - PFER-55:Message Engine-PyrContrct-Send expiring messages to assigned user when end_dt > sysdate  *****");

        logger.info("*** Step 1 Actions: - Add new PYR_CONTRCT with REVIEW_DT = SYSDATE | END_DT > SYSDATE | MSG_STATUS = 0 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        String newContrctID = randomCharacter.getRandomAlphaNumericString(10);
        timeStamp = new TimeStamp();
        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();
        String endDt = timeStamp.getFutureDate("MM/dd/yyyy", 5);

        listCol.add("PK_CONTRCT_ID");
        listCol.add("FK_ADMIN_CNTRY_ID");
        listCol.add("B_DELETED");
        listCol.add("B_CONT_PRC_IF_EXPIRED");
        listCol.add("START_DT");
        listCol.add("RPT_FRM_ID");
        listCol.add("REVIEW_DT");
        listCol.add("MSG_STATUS");
        listCol.add("END_DT");
        listCol.add("FK_CONTRCT_COVRG_ID");
        listCol.add("FREQ_PATTERN_ID");
        listCol.add("CONTRCT_NM");
        listCol.add("ADMIN_NM");

        listVal.add("'" + newContrctID + "'");
        listVal.add("0");
        listVal.add("0");
        listVal.add("0");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("TO_DATE('" + endDt + "', 'MM/DD/YYYY')");
        listVal.add("4");
        listVal.add("2");
        listVal.add("'AUTOTEST" + newContrctID + "'");
        listVal.add("'AUTOTEST" + newContrctID + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("PYR_CONTRCT", listCol, listVal, testDb);

        logger.info("*** Step 1 Expected Results: - New row of PYR_CONTRCT was inserted to database");
        Assert.assertTrue(totalRowInserted > 0, "       New PYR_CONTRCT can't be inserted to the table.");

        logger.info("*** Step 2 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 3 Actions: - Add new PYR_DT with PYR_ID = PYR_ID in pyr table | PYR_EFF_DT = SYSDATE | FK_CONTRCT_ID = CONTRACT Id created at step 2 in DB");
        Pyr listPyr = payorDao.getPyrFromPyrDtAndPyr();
        int pyrId = listPyr.getPyrId();
        String pyrAbbrev = listPyr.getPyrAbbrv();

        totalRowInserted = daoManagerPlatform.addNewPyrDtWContractFromPYRDTByPyrIdContractId(String.valueOf(pyrId), newContrctID, testDb);

        logger.info("*** Step 3 Expected Results: - New row of PYR_DT was inserted to the table");
        Assert.assertTrue(totalRowInserted > 0, "       New PYR_DT can't be inserted to the table.");

        logger.info("*** Step 4 Actions: - Clear System cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("PYR_CONTRCT", "PK_CONTRCT_ID='" + newContrctID + "'", 1, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 1, "        The Message Status should not be updated.");

        logger.info("*** Step 5 Expected Results:  Contract for  " + pyrAbbrev + " is Expiring" + " \n\r" + " Not a compliance Message " + " \n\r" + " Not logged in the compliance log");
        QMessage listResult = qMessageDao.getQMessageByPyrAbbrev(pyrAbbrev);

        logger.info("*** Step 5 Expected Results: SUBJECT = Expiring contract " + pyrAbbrev);
        String subject = listResult.getSubject();
        Assert.assertEquals(subject, "Expiring contract " + pyrAbbrev);

        logger.info("*** Step 6 Actions: Check message counts in Q_MESSAGE table");
        int qSize = qMessageDao.getQMessageBySubject(subject).size();
        Assert.assertEquals(groupDao.getCountGrpMemberAndUsersAsContractsManager(), qSize);

        logger.info("*** Step 6 Expected Results: PRIO = MED");
        String prio = listResult.getPrio();
        Assert.assertEquals(prio, "MED");

        logger.info("*** Step 6 Expected Results: RECV_DT = the current time");
        String recvDt = String.valueOf(listResult.getRecvDt());
        String crnDate = timeStamp.getCurrentDate("YYYY-MM-dd");
        Assert.assertEquals(recvDt, crnDate);

        logger.info("*** Step 6 Expected Results: FK_FROM_USER_ID = system");
        String fromUserID = listResult.getFromUserId();
        Assert.assertEquals(fromUserID, "system");

        logger.info("*** Step 7 Actions: - Delete the Contract in DB");
        payorDao.deletePyrDtByPyrIdSysDtContrctId(pyrId, newContrctID);
        payorDao.deletePyrCntrctByContrctId(newContrctID);

        logger.info("*** Step 8 Actions: - Clear System cache");
        xifinAdminUtils.clearDataCache();

    }

    @Test(priority = 1, description = "PyrContrct-No message is sent when end_dt > sysdate, review_dt > sysdate")
    public void testPFER_56() throws Exception {
        logger.info("***** Testing - PFER-56:Message Engine-PyrContrct-No message is sent when end_dt > sysdate, review_dt > sysdate  *****");
        xifinAdminUtils = new XifinAdminUtils(driver, config);

        logger.info("*** Step 1 Actions: - Add new PYR_CONTRCT with REVIEW_DT > SYSDATE | END_DT > SYSDATE | MSG_STATUS = 0 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        String newContrctID = randomCharacter.getRandomAlphaNumericString(10);
        timeStamp = new TimeStamp();
        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();
        String reviewDt = timeStamp.getFutureDate("MM/dd/yyyy", 5);
        String endDt = timeStamp.getFutureDate("MM/dd/yyyy", 5);

        listCol.add("PK_CONTRCT_ID");
        listCol.add("FK_ADMIN_CNTRY_ID");
        listCol.add("B_DELETED");
        listCol.add("B_CONT_PRC_IF_EXPIRED");
        listCol.add("START_DT");
        listCol.add("RPT_FRM_ID");
        listCol.add("REVIEW_DT");
        listCol.add("MSG_STATUS");
        listCol.add("END_DT");
        listCol.add("FK_CONTRCT_COVRG_ID");
        listCol.add("FREQ_PATTERN_ID");
        listCol.add("CONTRCT_NM");
        listCol.add("ADMIN_NM");

        listVal.add("'" + newContrctID + "'");
        listVal.add("0");
        listVal.add("0");
        listVal.add("0");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("TO_DATE('" + reviewDt + "', 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("TO_DATE('" + endDt + "', 'MM/DD/YYYY')");
        listVal.add("4");
        listVal.add("2");
        listVal.add("'AUTOTEST" + newContrctID + "'");
        listVal.add("'AUTOTEST" + newContrctID + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("PYR_CONTRCT", listCol, listVal, testDb);

        logger.info("*** Step 1 Expected Results: - New row of PYR_CONTRCT was inserted to database");
        Assert.assertTrue(totalRowInserted > 0, "       New PYR_CONTRCT can't be inserted to the table.");

        logger.info("*** Step 2 Actions: - Update SS_MESSAGE_WAIT_TIME = 0 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "0", "0");

        logger.info("*** Step 3 Actions: - Add new PYR_DT with PYR_ID = PYR_ID in pyr table | PYR_EFF_DT = SYSDATE | FK_CONTRCT_ID = CONTRACT Id created at step 2 in DB");
        Pyr listPyr = payorDao.getPyrFromPyrDtAndPyr();
        int pyrId = listPyr.getPyrId();
        String pyrAbbrev = listPyr.getPyrAbbrv();

        totalRowInserted = daoManagerPlatform.addNewPyrDtWContractFromPYRDTByPyrIdContractId(String.valueOf(pyrId), newContrctID, testDb);

        logger.info("*** Step 3 Expected Results: - New row of PYR_DT was inserted to the table");
        Assert.assertTrue(totalRowInserted > 0, "       New PYR_DT can't be inserted to the table.");

        logger.info("*** Step 4 Actions: - Clear System cache");
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("PYR_CONTRCT", "PK_CONTRCT_ID='" + newContrctID + "'", 0, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 0, "        The Message Status should not be updated.");

        logger.info("*** Step 6 Actions: Check Q_MESSAGE table");
        //Assert.assertEquals(daoManagerPlatform.countAllUserOnQMessage(testDb), "0", "        No message should be sent.");
        Assert.assertEquals(String.valueOf(qMessageDao.getQMessageBySubject(pyrAbbrev).size()), "0", "        No message should be sent.");

        logger.info("*** Step 7 Actions: - Delete the Contract in DB");
        payorDao.deletePyrDtByPyrIdSysDtContrctId(pyrId, newContrctID);
        payorDao.deletePyrCntrctByContrctId(newContrctID);

        logger.info("*** Step 8 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 9 Actions: - Clear System cache");
        xifinAdminUtils.clearDataCache();

    }

    @Test(priority = 1, description = "PyrContrct-No message is sent when review_dt <sysdate, end_dt>sysdate")
    public void testPFER_57() throws Exception {
        logger.info("***** Testing - PFER-57:Message Engine-PyrContrct-No message is sent when review_dt <sysdate, end_dt>sysdate  *****");

        logger.info("*** Step 2 Actions: - Add new PYR_CONTRCT with REVIEW_DT < SYSDATE | END_DT > SYSDATE | MSG_STATUS = 0 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        String newContrctID = randomCharacter.getRandomAlphaNumericString(10);
        timeStamp = new TimeStamp();
        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();
        String reviewDt = timeStamp.getPreviousDate("MM/dd/yyyy", 5);
        String endDt = timeStamp.getFutureDate("MM/dd/yyyy", 5);

        listCol.add("PK_CONTRCT_ID");
        listCol.add("FK_ADMIN_CNTRY_ID");
        listCol.add("B_DELETED");
        listCol.add("B_CONT_PRC_IF_EXPIRED");
        listCol.add("START_DT");
        listCol.add("RPT_FRM_ID");
        listCol.add("REVIEW_DT");
        listCol.add("MSG_STATUS");
        listCol.add("END_DT");
        listCol.add("FK_CONTRCT_COVRG_ID");
        listCol.add("FREQ_PATTERN_ID");
        listCol.add("CONTRCT_NM");
        listCol.add("ADMIN_NM");

        listVal.add("'" + newContrctID + "'");
        listVal.add("0");
        listVal.add("0");
        listVal.add("0");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("TO_DATE('" + reviewDt + "', 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("TO_DATE('" + endDt + "', 'MM/DD/YYYY')");
        listVal.add("4");
        listVal.add("2");
        listVal.add("'AUTOTEST" + newContrctID + "'");
        listVal.add("'AUTOTEST" + newContrctID + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("PYR_CONTRCT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of PYR_CONTRCT was inserted to database");
        Assert.assertTrue(totalRowInserted > 0, "       New PYR_CONTRCT can't be inserted to the table.");

        logger.info("*** Step 4 Actions: - Add new PYR_DT with PYR_ID = PYR_ID in pyr table | PYR_EFF_DT = SYSDATE | FK_CONTRCT_ID = CONTRACT Id created at step 2 in DB");
        Pyr listPyr = payorDao.getPyrFromPyrDtAndPyr();
        int pyrId = listPyr.getPyrId();
        String pyrAbbrev = listPyr.getPyrAbbrv();

        totalRowInserted = daoManagerPlatform.addNewPyrDtWContractFromPYRDTByPyrIdContractId(String.valueOf(pyrId), newContrctID, testDb);

        logger.info("*** Step 4 Expected Results: - New row of PYR_DT was inserted to the table");
        Assert.assertTrue(totalRowInserted > 0, "       New PYR_DT can't be inserted to the table.");

        logger.info("*** Step 4 Actions: - Clear System cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 6 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("PYR_CONTRCT", "PK_CONTRCT_ID='" + newContrctID + "'", 0, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 0, "        The Message Status should not be updated.");

        logger.info("*** Step 7 Actions: Check Q_MESSAGE table and make sure no message sent");
        Assert.assertEquals(String.valueOf(qMessageDao.getQMessageBySubject(pyrAbbrev).size()), "0", "        No message should be sent.");

        logger.info("*** Step 8 Actions: - Delete the Contract in DB");
        payorDao.deletePyrDtByPyrIdSysDtContrctId(pyrId, newContrctID);
        payorDao.deletePyrCntrctByContrctId(newContrctID);

        logger.info("*** Step 9 Actions: - Clear System cache");
        xifinAdminUtils.clearDataCache();

    }

    @Test(priority = 1, description = "PyrContrct-Send expired messages to supervisor when review_dt<sysdate, end_dt=sysdate")
    public void testPFER_58() throws Exception {
        logger.info("***** Testing - PFER-58:Message Engine-PyrContrct-Send expired messages to supervisor when review_dt<sysdate, end_dt=sysdate  *****");

        logger.info("*** Step 1 Actions: - Add new PYR_CONTRCT with REVIEW_DT < SYSDATE | END_DT > SYSDATE | MSG_STATUS = 1 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        String newContrctID = randomCharacter.getRandomAlphaNumericString(10);
        timeStamp = new TimeStamp();
        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();
        String reviewDt = timeStamp.getPreviousDate("MM/dd/yyyy", 5);

        listCol.add("PK_CONTRCT_ID");
        listCol.add("FK_ADMIN_CNTRY_ID");
        listCol.add("B_DELETED");
        listCol.add("B_CONT_PRC_IF_EXPIRED");
        listCol.add("START_DT");
        listCol.add("RPT_FRM_ID");
        listCol.add("REVIEW_DT");
        listCol.add("MSG_STATUS");
        listCol.add("END_DT");
        listCol.add("FK_CONTRCT_COVRG_ID");
        listCol.add("FREQ_PATTERN_ID");
        listCol.add("CONTRCT_NM");
        listCol.add("ADMIN_NM");

        listVal.add("'" + newContrctID + "'");
        listVal.add("0");
        listVal.add("0");
        listVal.add("0");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("TO_DATE('" + reviewDt + "', 'MM/DD/YYYY')");
        listVal.add("1");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("4");
        listVal.add("2");
        listVal.add("'AUTOTEST" + newContrctID + "'");
        listVal.add("'AUTOTEST" + newContrctID + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("PYR_CONTRCT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of PYR_CONTRCT was inserted to database");
        Assert.assertTrue(totalRowInserted > 0, "       New PYR_CONTRCT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: -Add new PYR_DT with PYR_ID = PYR_ID in pyr table | PYR_EFF_DT <= SYSDATE | FK_CONTRCT_ID = CONTRACT Id created at step 2 in DB");
        Pyr listPyr = payorDao.getPyrFromPyrDtAndPyr();
        int pyrId = listPyr.getPyrId();
        String pyrAbbrev = listPyr.getPyrAbbrv();

        String pyrEffDt = timeStamp.getCurrentDate();
        totalRowInserted = daoManagerPlatform.addNewPyrDtWContractFromPYRDTByPyrIdEffDtContractId(String.valueOf(pyrId), pyrEffDt, newContrctID, testDb);

        logger.info("*** Step 3 Expected Results: - New row of PYR_DT was inserted to the table");
        Assert.assertTrue(totalRowInserted > 0, "       New PYR_DT can't be inserted to the table.");

        logger.info("*** Step 4 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 5 Actions: - Clear System cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 6 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("PYR_CONTRCT", "PK_CONTRCT_ID='" + newContrctID + "'", 2, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 2, "        The Message Status should not be updated.");

        logger.info("*** Step 6 Actions: Check Q_MESSAGE table");
        Assert.assertEquals(groupDao.getCountGrpMemberAndUsersAsContractsManager(), qMessageDao.getQMessageBySubject(pyrAbbrev).size());

        logger.info("*** Step 6 Expected Results: Message should be sent");
        QMessage listResult = qMessageDao.getQMessageForPyrContrctByMessage("Contract Expired", pyrAbbrev);

        logger.info("*** Step 6 Expected Results:  Contract for  " + pyrAbbrev + " is Expiring" + " \n\r" + " Not a compliance Message " + " \n\r" + " Not logged in the compliance log");
        String msg = listResult.getMessage();
        Assert.assertEquals(msg, " Contract for  " + pyrAbbrev + " Expired" + " \n\r" + " Not a compliance Message " + " \n\r" + " Not logged in the compliance log");

        logger.info("*** Step 6 Expected Results: PRIO = MED");
        String prio = listResult.getPrio();
        Assert.assertEquals(prio, "MED");

        logger.info("*** Step 6 Expected Results: SUBJECT = Contract Expired " + pyrAbbrev);
        String subject = listResult.getSubject();
        Assert.assertEquals(subject, "Contract Expired " + pyrAbbrev);

        logger.info("*** Step 6 Expected Results: RECV_DT = the current date");
        String recvDt = String.valueOf(listResult.getRecvDt());
        String crnDate = timeStamp.getCurrentDate("YYYY-MM-dd");
        Assert.assertEquals(recvDt, crnDate);

        logger.info("*** Step 6 Expected Results: FK_FROM_USER_ID = system");
        String fromUserID = listResult.getFromUserId();
        Assert.assertEquals(fromUserID, "system");

        logger.info("*** Step 7 Actions: - Delete the Contract in DB");
        payorDao.deletePyrDtByPyrIdSysDtContrctId(pyrId, newContrctID);
        payorDao.deletePyrCntrctByContrctId(newContrctID);

        logger.info("*** Step 8 Actions: - Clear System cache");
        xifinAdminUtils.clearDataCache();
    }

    @Test(priority = 1, description = "PyrContrct-No message is sent when end_dt < sysdate, review_dt < sysdate")
    public void testPFER_59() throws Exception {
        logger.info("***** Testing - PFER-59:Message Engine-PyrContrct-No message is sent when end_dt < sysdate, review_dt < sysdate  *****");

        logger.info("*** Step 1 Actions: - Add new PYR_CONTRCT with review_dt & end_dt < SYSDATE, msg_status = 0 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        String newContrctID = randomCharacter.getRandomAlphaNumericString(10);
        timeStamp = new TimeStamp();
        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();
        String reviewDt = timeStamp.getPreviousDate("MM/dd/yyyy", 2);
        String endDt = timeStamp.getPreviousDate("MM/dd/yyyy", 2);

        listCol.add("PK_CONTRCT_ID");
        listCol.add("FK_ADMIN_CNTRY_ID");
        listCol.add("B_DELETED");
        listCol.add("B_CONT_PRC_IF_EXPIRED");
        listCol.add("START_DT");
        listCol.add("RPT_FRM_ID");
        listCol.add("REVIEW_DT");
        listCol.add("MSG_STATUS");
        listCol.add("END_DT");
        listCol.add("FK_CONTRCT_COVRG_ID");
        listCol.add("FREQ_PATTERN_ID");
        listCol.add("CONTRCT_NM");
        listCol.add("ADMIN_NM");

        listVal.add("'" + newContrctID + "'");
        listVal.add("0");
        listVal.add("0");
        listVal.add("0");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("TO_DATE('" + reviewDt + "', 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("TO_DATE('" + endDt + "', 'MM/DD/YYYY')");
        listVal.add("4");
        listVal.add("2");
        listVal.add("'AUTOTEST" + newContrctID + "'");
        listVal.add("'AUTOTEST" + newContrctID + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("PYR_CONTRCT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of PYR_CONTRCT was inserted to database");
        Assert.assertTrue(totalRowInserted > 0, "       New PYR_CONTRCT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: - Add PYR_DT with pyr_eff_dt <= SYSDATE , FK_CONTRCT_ID = contrct was created step 2 in DB");
        Pyr listPyr = payorDao.getPyrFromPyrDtAndPyr();
        int pyrId = listPyr.getPyrId();
        String pyrAbbrev = listPyr.getPyrAbbrv();

        totalRowInserted = daoManagerPlatform.addNewPyrDtWContractFromPYRDTByPyrIdContractId(String.valueOf(pyrId), newContrctID, testDb);

        logger.info("*** Step 3 Expected Results: - New row of PYR_DT was inserted to the table");
        Assert.assertTrue(totalRowInserted > 0, "       New PYR_DT can't be inserted to the table.");

        logger.info("*** Step 4 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 5 Actions: - Clear System cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 6 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("PYR_CONTRCT", "PK_CONTRCT_ID='" + newContrctID + "'", 0, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 0, "        The Message Status should not be updated.");

        logger.info("*** Step 7 Actions: Check Q_MESSAGE table and make sure no message sent");
        Assert.assertEquals(String.valueOf(qMessageDao.getQMessageBySubject(pyrAbbrev).size()), "0", "        No message should be sent.");

        logger.info("*** Step 8 Actions: - Delete the Contract in DB");
        payorDao.deletePyrDtByPyrIdSysDtContrctId(pyrId, newContrctID);
        payorDao.deletePyrCntrctByContrctId(newContrctID);

        logger.info("*** Step 9 Actions: - Clear System cache");
        xifinAdminUtils.clearDataCache();

    }

    @Test(priority = 1, description = "PyrContrct-Send expiring, expired messages to supervisor if review_dt=sysdate=end_dt")
    public void testPFER_60() throws Exception {
        logger.info("***** Testing - PFER-60:Message Engine-PyrContrct-Send expiring, expired messages to supervisor if review_dt=sysdate=end_dt  *****");

        logger.info("*** Step 1 Actions: - Add new PYR_CONTRCT with review_dt & end_dt = SYSDATE, msg_status = 0 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        String newContrctID = randomCharacter.getRandomAlphaNumericString(10);
        timeStamp = new TimeStamp();
        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CONTRCT_ID");
        listCol.add("FK_ADMIN_CNTRY_ID");
        listCol.add("B_DELETED");
        listCol.add("B_CONT_PRC_IF_EXPIRED");
        listCol.add("START_DT");
        listCol.add("RPT_FRM_ID");
        listCol.add("REVIEW_DT");
        listCol.add("MSG_STATUS");
        listCol.add("END_DT");
        listCol.add("FK_CONTRCT_COVRG_ID");
        listCol.add("FREQ_PATTERN_ID");
        listCol.add("CONTRCT_NM");
        listCol.add("ADMIN_NM");

        listVal.add("'" + newContrctID + "'");
        listVal.add("0");
        listVal.add("0");
        listVal.add("0");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("4");
        listVal.add("2");
        listVal.add("'AUTOTEST" + newContrctID + "'");
        listVal.add("'AUTOTEST" + newContrctID + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("PYR_CONTRCT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of PYR_CONTRCT was inserted to database");
        Assert.assertTrue(totalRowInserted > 0, "       New PYR_CONTRCT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: - Add PYR_DT with pyr_eff_dt <= SYSDATE , FK_CONTRCT_ID = contrct was created step 2 in DB");
        Pyr listPyr = payorDao.getPyrFromPyrDtAndPyr();
        int pyrId = listPyr.getPyrId();
        String pyrAbbrev = listPyr.getPyrAbbrv();

        totalRowInserted = daoManagerPlatform.addNewPyrDtWContractFromPYRDTByPyrIdContractId(String.valueOf(pyrId), newContrctID, testDb);

        logger.info("*** Step 3 Expected Results: - New row of PYR_DT was inserted to the table");
        Assert.assertTrue(totalRowInserted > 0, "       New PYR_DT can't be inserted to the table.");

        logger.info("*** Step 4 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 5 Actions: - Clear System cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 6 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("PYR_CONTRCT", "PK_CONTRCT_ID='" + newContrctID + "'", 2, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 2, "        The Message Status should not be updated.");

        logger.info("*** Step 6 Expected Results: - Verify Expired and the Expiring messages added to Q_MESSAGE table");
        String prio = "MED";
        String subject = "Contract Expired " + pyrAbbrev;
        Assert.assertTrue(daoManagerPlatform.getPyrMessageFromQ_Message(pyrAbbrev, prio, subject, testDb).size() > 0, "       Message should add to Q_MESSAGE table");

        logger.info("*** Step 6 Expected Results:  Contract for  " + pyrAbbrev + " is Expiring" + " \n\r" + " Not a compliance Message " + " \n\r" + " Not logged in the compliance log");
        QMessage listResult = qMessageDao.getQMessageByPyrAbbrev(pyrAbbrev);

        logger.info("*** Step 6 Expected Results: SUBJECT = Expiring contract " + pyrAbbrev);
        subject = listResult.getSubject();
        Assert.assertEquals(subject, "Expiring contract " + pyrAbbrev);

        logger.info("*** Step 6 Expected Results: RECV_DT = the current time");
        String recvDt = String.valueOf(listResult.getRecvDt());
        String crnDate = timeStamp.getCurrentDate("YYYY-MM-dd");
        Assert.assertEquals(recvDt, crnDate);

        logger.info("*** Step 6 Expected Results: FK_FROM_USER_ID = system");
        String fromUserID = listResult.getFromUserId();
        Assert.assertEquals(fromUserID, "system");

        logger.info("*** Step 7 Actions: - Delete the Contract in DB");
        payorDao.deletePyrDtByPyrIdSysDtContrctId(pyrId, newContrctID);
        payorDao.deletePyrCntrctByContrctId(newContrctID);

        logger.info("*** Step 7 Actions: - Clear System cache");
        xifinAdminUtils.clearDataCache();
    }

    @Test(priority = 1, description = "ClnCntct-Send message to assigned user when msg_status = 1 and follow_up_dt= Sysdate")
    @Parameters({"clnId"})
    public void testPFER_35(int clnId) throws Exception {
        logger.info("***** Testing - PFER-35:Message Engine-ClnCntct-Send message to assigned user when msg_status = 1 and follow_up_dt= Sysdate  *****");

        logger.info("*** Step 1 Actions: - Add new CLN_CNTCT with follow_up_dt = SYSDATE, msg_status = 0 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        clientDao.deleteClnCntctByClnId(clnId);

        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String contactInfo = "Automation Contact Info " + randomCharacter.getRandomAlphaNumericString(6);
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        timeStamp = new TimeStamp();
        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CLN_ID");
        listCol.add("FK_USER_ID");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_DT");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("MSG_STATUS");
        listCol.add("PK_CNTCT_SEQ");
        listCol.add("B_VOID");
        listCol.add("CNTCT_INFO");
        listCol.add("NOTE");

        listVal.add("'" + clnId + "'");
        listVal.add("'" + userId + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("0");
        listVal.add("1");
        listVal.add("0");
        listVal.add("'" + contactInfo + "'");
        listVal.add("'" + note + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("CLN_CNTCT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of CLN_CNTCT was inserted to database");
        Assert.assertTrue(totalRowInserted > 0, "       New CLN_CNTCT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: - Clear System cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();


        logger.info("*** Step 4 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("CLN_CNTCT", "Pk_Cln_Id = '" + clnId + "'", 1, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 1, "        The Message Status should not be updated.");

        logger.info("*** Step 4 Expected result: new message is added into Q_Message table");
        String clnAbbrev = clientDao.getClnByClnId(clnId).getClnAbbrev();
        List<QMessage> listInfo = qMessageDao.getQMessageByClnId(clnId);

        Assert.assertEquals(listInfo.get(0).getPrio(), "MED", "        Prio Column is displayed correctly.");

        String message = "A follow up activity for client " + clnAbbrev + "  is Scheduled for TODAY \n\r " + contactInfo + "\n\r" + note + "";
        String messageInDB = listInfo.get(0).getMessage();
        Assert.assertEquals(message.replaceAll(" ", "").trim(), messageInDB.replaceAll(" ", "").trim(), "        Message: " + message + " should be displayed correctly.");

        String subject = "Client Contact - " + clnAbbrev;
        Assert.assertEquals(subject, listInfo.get(0).getSubject(), "        Subject " + subject + " should be displayed correctly.");

        Assert.assertEquals(followUpUserId, listInfo.get(0).getToUserId(), "        To User Id: " + followUpUserId + " should be displayed correctly.");

        String recvDate = String.valueOf(listInfo.get(0).getRecvDt());
        String SYSDATE = timeStamp.getCurrentDate("YYYY-MM-dd");
        Assert.assertEquals(recvDate, SYSDATE, "        Received date: " + recvDate + " should be displayed correctly.");
        String Hlink = "clnCntctMgr(" + clnId + ")";
        String HlinkDb = listInfo.get(0).getHLink();
        Assert.assertEquals(Hlink, HlinkDb, "        Hlink is displayed correctly");

        logger.info("*** Step 5 Actions: - Delete the Client Contact in DB");
        clientDao.deleteClnCntctByClnId(clnId);

        logger.info("*** Step 6 Actions: - Clear System cache");
        xifinAdminUtils.clearDataCache();
    }

    @Test(priority = 1, description = "ClnCntct-No message is sent when msg_status =0 and follow_up_dt=null")
    @Parameters({"clnId"})
    public void testPFER_36(int clnId) throws Exception {
        logger.info("***** Testing - PFER-36:Message Engine-ClnCntct-No message is sent when msg_status =1 and follow_up_dt=null  *****");

        logger.info("*** Step 1 Actions: - Add new CLN_CNTCT with follow_up_dt = null, msg_status = 0 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        clientDao.deleteClnCntctByClnId(clnId);

        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String contactInfo = "Automation Contact Info " + randomCharacter.getRandomAlphaNumericString(6);
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        timeStamp = new TimeStamp();
        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CLN_ID");
        listCol.add("FK_USER_ID");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_DT");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("MSG_STATUS");
        listCol.add("PK_CNTCT_SEQ");
        listCol.add("B_VOID");
        listCol.add("CNTCT_INFO");
        listCol.add("NOTE");

        listVal.add("'" + clnId + "'");
        listVal.add("'" + userId + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("null");
        listVal.add("0");
        listVal.add("0");
        listVal.add("1");
        listVal.add("0");
        listVal.add("'" + contactInfo + "'");
        listVal.add("'" + note + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("CLN_CNTCT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of CLN_CNTCT was inserted to database");
        Assert.assertTrue(totalRowInserted > 0, "       New CLN_CNTCT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: - Clear System cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 4 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("CLN_CNTCT", "Pk_Cln_Id = '" + clnId + "'", 0, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 0, "        The Message Status should not be updated.");

        logger.info("*** Step 4 Expected result: The message is not added into the Q_Message table");
        Assert.assertEquals(qMessageDao.getQMessageByHLink(String.valueOf(clnId)).size(), 0, "        No Q_MESSAGE should be sent.");

        logger.info("*** Step 5 Actions: - Delete the Client Contact in DB");
        clientDao.deleteClnCntctByClnId(clnId);

        logger.info("*** Step 6 Actions: - Clear System cache");
        xifinAdminUtils.clearDataCache();

    }

    @Test(priority = 1, description = "ClnCntct-No message is sent when follow_up_dt > Sysdate (Not over the number of days in SS#4)")
    @Parameters({"clnId"})
    public void testPFER_37(int clnId) throws Exception {
        logger.info("***** Testing - PFER-37:Message Engine-ClnCntct-No message is sent when follow_up_dt <= Sysdate (Not over the date the number of days SS#4)  *****");

        logger.info("*** Step 2 Actions: - Add new CLN_CNTCT with follow_up_dt > sysdate (Not over the date the number of days SS#4), msg_status = 0 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        clientDao.deleteClnCntctByClnId(clnId);

        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String contactInfo = "Automation Contact Info " + randomCharacter.getRandomAlphaNumericString(6);
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        timeStamp = new TimeStamp();
        String followUpDt = timeStamp.getFutureDate("MM/dd/yyyy", 2);

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CLN_ID");
        listCol.add("FK_USER_ID");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_DT");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("MSG_STATUS");
        listCol.add("PK_CNTCT_SEQ");
        listCol.add("B_VOID");
        listCol.add("CNTCT_INFO");
        listCol.add("NOTE");

        listVal.add("'" + clnId + "'");
        listVal.add("'" + userId + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + followUpDt + "', 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("0");
        listVal.add("1");
        listVal.add("0");
        listVal.add("'" + contactInfo + "'");
        listVal.add("'" + note + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("CLN_CNTCT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of CLN_CNTCT was inserted to database");
        Assert.assertTrue(totalRowInserted > 0, "       New CLN_CNTCT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 4 Actions: - Clear System cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("CLN_CNTCT", "Pk_Cln_Id = '" + clnId + "'", 0, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 0, "        The Message Status should not be updated.");

        logger.info("*** Step 5 Expected result: The message is not added into the Q_Message table");
        Assert.assertEquals(qMessageDao.getQMessageByHLink(String.valueOf(clnId)).size(), 0, "        No Q_MESSAGE should be sent.");

        logger.info("*** Step 6 Actions: - Delete the Client Contact in DB");
        clientDao.deleteClnCntctByClnId(clnId);

        logger.info("*** Step 7 Actions: - Clear System cache");
        xifinAdminUtils.clearDataCache();
    }

    @Test(priority = 1, description = "ClnCntct-Send overdue message to supervisor when message is OVERDUE")
    @Parameters({"clnId"})
    public void testPFER_38(int clnId) throws Exception {
        logger.info("***** Testing -  PFER-38:Message Engine-ClnCntct-Send overdue message to supervisor when message is OVERDUE  *****");

        logger.info("*** Step 1 Actions: - Add new CLN_CNTCT with follow_up_dt < (sysdate - the date the number of days in SS#4), msg_status = 1 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        clientDao.deleteClnCntctByClnId(clnId);

        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String contactInfo = "Automation Contact Info " + randomCharacter.getRandomAlphaNumericString(6);
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        timeStamp = new TimeStamp();
        String followUpDt = timeStamp.getPreviousDate("MM/dd/yyyy", 6);

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CLN_ID");
        listCol.add("FK_USER_ID");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_DT");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("MSG_STATUS");
        listCol.add("PK_CNTCT_SEQ");
        listCol.add("B_VOID");
        listCol.add("CNTCT_INFO");
        listCol.add("NOTE");

        listVal.add("'" + clnId + "'");
        listVal.add("'" + userId + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + followUpDt + "', 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("1");
        listVal.add("1");
        listVal.add("0");
        listVal.add("'" + contactInfo + "'");
        listVal.add("'" + note + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("CLN_CNTCT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of CLN_CNTCT was inserted to database");
        Assert.assertTrue(totalRowInserted > 0, "       New CLN_CNTCT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 4 Actions: - Clear System cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("CLN_CNTCT", "Pk_Cln_Id = '" + clnId + "'", 2, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 2, "        The Message Status should not be updated.");

        logger.info("*** Step 5 Expected result: Check message is displayed correctly");
        List<QMessage> listInfo = qMessageDao.getQMessageByClnId(clnId);
        String clnAbbrev = clientDao.getClnByClnId(clnId).getClnAbbrev();
        String messageDb = listInfo.get(0).getMessage();
        String message = "A follow up activity for client " + clnAbbrev + " is OVERDUE \n\r" + contactInfo + "\n\r" + note + "";
        Assert.assertEquals(messageDb.replaceAll("[\\t\\n\\r]", "").replaceAll(" ", "").trim(), message.replaceAll("[\\t\\n\\r]", "").replaceAll(" ", "").trim(), "        Message: " + message + "should be displayed.");

        logger.info("*** Step 5 Expected result: The values in Q_Message should be added as expected");
        String priority = listInfo.get(0).getPrio();
        Assert.assertEquals(priority, "HIGH", "        Prio column should display HIGH.");

        String Subject = listInfo.get(0).getSubject();
        Assert.assertEquals(Subject.trim(), "Client F/U Overdue - " + clnAbbrev, "        Subject: " + "Client F/U Overdue - " + clnAbbrev + " should be displayed correctly.");

        String assignUser = listInfo.get(0).getToUserId();
        String supervisorUser = usersDao.getUsersByUserId(followUpUserId).getSupervisorId();
        Assert.assertEquals(assignUser, supervisorUser, "        The assigned user " + supervisorUser + " should be displayed correctly.");

        String recvDt = String.valueOf(listInfo.get(0).getRecvDt());
        String contactDt = timeStamp.getCurrentDate("YYYY-MM-dd");
        Assert.assertEquals(recvDt, contactDt, "        THe received date " + contactDt + " should be displayed correctly.");

        String hLink = listInfo.get(0).getHLink();
        Assert.assertEquals(hLink, "clnCntctMgr(" + clnId + ")", "        Hlink: " + "clnCntctMgr(" + clnId + ")" + " should be displayed correctly.");

        logger.info("*** Step 6 Actions: - Delete the Client Contact in DB");
        clientDao.deleteClnCntctByClnId(clnId);

        logger.info("*** Step 7 Actions: - Clear System cache");
        xifinAdminUtils.clearDataCache();
    }

    @Test(priority = 1, description = "ClnPromsPmt-No message is sent when due_dt > sysdate")
    @Parameters({"clnId"})
    public void testPFER_39(int clnId) throws Exception {
        logger.info("***** Testing - PFER-39:Message Engine-ClnPromsPmt-No message is sent when due_dt > sysdate  *****");

        logger.info("*** Step 1 Actions: - Add new CLN_PROMS_PMT with due_dt > sysdate, msg_status = 0 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        clientDao.deleteClnPromsPmtByClnId(clnId);

        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        String dueAmt = randomCharacter.getNonZeroRandomNumericString(2);
        timeStamp = new TimeStamp();
        String followUpDt = timeStamp.getFutureDate("MM/dd/yyyy", 5);
        String dueDate = timeStamp.getFutureDate("MM/dd/yyyy", 2);

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CLN_ID");
        listCol.add("PK_PROMS_PMT_SEQ");
        listCol.add("FK_FREQ_ID");
        listCol.add("FK_USER_ID");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_DT");
        listCol.add("DUE_DT");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("B_VOID");
        listCol.add("MSG_STATUS");
        listCol.add("PMT_AMT");
        listCol.add("NOTE");

        listVal.add("'" + clnId + "'");
        listVal.add("'1'");
        listVal.add("'2'");
        listVal.add("'" + userId + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + dueDate + "', 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + followUpDt + "', 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("0");
        listVal.add("0");
        listVal.add("'" + dueAmt + "'");
        listVal.add("'" + note + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("CLN_PROMS_PMT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of CLN_PROMS_PMT was inserted to database");
        Assert.assertTrue(totalRowInserted > 0, "       New CLN_PROMS_PMT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: - Run Platform Message Engine");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        int statusUpdate = isOutOfMessageQueue("CLN_PROMS_PMT", "Pk_Cln_Id = '" + clnId + "'", 0, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 0, "        The Message Status should not be updated.");

        logger.info("*** Step 3 Expected result: The message is not added into the Q_Message table");
        Assert.assertEquals(qMessageDao.getQMessageByHLink(String.valueOf(clnId)).size(), 0, "        No Q_MESSAGE should be sent.");

        logger.info("*** Step 4 Actions: - Delete the CLN_PROMS_PMT in DB");
        clientDao.deleteClnPromsPmtByClnId(clnId);

    }

    @Test(priority = 1, description = "ClnPromsPmt-Send scheduled message to assigned user when due_dt > sysdate")
    @Parameters({"clnId"})
    public void testPFER_40(int clnId) throws Exception {
        logger.info("***** Testing - PFER-40:Message Engine-ClnPromsPmt-Send scheduled message to assigned user when due_dt > sysdate  *****");

        logger.info("*** Step 2 Actions: - Add new CLN_PROMS_PMT with due_dt > sysdate, msg_status = 0 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        clientDao.deleteClnPromsPmtByClnId(clnId);

        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        String dueAmt = randomCharacter.getNonZeroRandomNumericString(2);
        timeStamp = new TimeStamp();
        String dueDate = timeStamp.getFutureDate("MM/dd/yyyy", 2);

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CLN_ID");
        listCol.add("PK_PROMS_PMT_SEQ");
        listCol.add("FK_FREQ_ID");
        listCol.add("FK_USER_ID");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_DT");
        listCol.add("DUE_DT");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("B_VOID");
        listCol.add("MSG_STATUS");
        listCol.add("PMT_AMT");
        listCol.add("NOTE");

        listVal.add("'" + clnId + "'");
        listVal.add("'1'");
        listVal.add("'2'");
        listVal.add("'" + userId + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + dueDate + "', 'MM/DD/YYYY')");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("0");
        listVal.add("0");
        listVal.add("'" + dueAmt + "'");
        listVal.add("'" + note + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("CLN_PROMS_PMT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of CLN_PROMS_PMT was inserted to database");
        Assert.assertTrue(totalRowInserted > 0, "       New CLN_PROMS_PMT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("CLN_PROMS_PMT", "Pk_Cln_Id = '" + clnId + "'", 1, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 1, "        The Message Status should not be updated.");

        logger.info("*** Step 3 Expected Result: new message is generated in Q_Message");
        List<QMessage> listInfo = qMessageDao.getQMessageByClnId(clnId);
        String clnAbbrev = clientDao.getClnByClnId(clnId).getClnAbbrev();
        String messageInDB = listInfo.get(0).getMessage();
        String message = "A follow-up action on a promised payment for client " + clnAbbrev + " is scheduled for TODAY \n\r " + note + "";
        Assert.assertEquals(messageInDB.replaceAll(" ", "").trim(), message.replaceAll(" ", "").trim(), "        The message: " + message + " should be generated.");

        logger.info("*** Step 3 Expected result: the information is generated correctly in Q_Message table");
        String prioCol = listInfo.get(0).getPrio();
        Assert.assertEquals(prioCol, "MED", "        Prio Column should have MED value.");

        String subject = listInfo.get(0).getSubject();
        Assert.assertEquals(subject.trim(), "Cln Promise PMT Action", "        Subject: Cln Promise PMT Action " + " should be generated.");

        String assignUser = listInfo.get(0).getToUserId();
        Assert.assertEquals(assignUser, userId, "        Assigned User: " + userId + " should be generated.");

        String recvDate = String.valueOf(listInfo.get(0).getRecvDt());
        String sysdate = timeStamp.getCurrentDate("YYYY-MM-dd");
        Assert.assertEquals(recvDate, sysdate, "        Receive date: " + sysdate + " should be generated.");

        String hLink = listInfo.get(0).getHLink();
        Assert.assertEquals(hLink, "clnCntctMgr(" + clnId + ")", "        Hlink: " + "clnCntctMgr(" + clnId + ")" + " should be generated. ");

        String fromUser = listInfo.get(0).getFromUserId();
        Assert.assertEquals(fromUser, "system", "        The From User Id should be 'system'.");

        logger.info("*** Step 4 Actions: - Delete the CLN_PROMS_PMT in DB");
        clientDao.deleteClnPromsPmtByClnId(clnId);

    }

    @Test(priority = 1, description = "ClnPromsPmt-Send msg to user when follow_up_dt<=SYSDATE(<days in SS#4),due_dt>sysdate")
    @Parameters({"clnId"})
    public void testPFER_41(int clnId) throws Exception {
        logger.info("***** Testing -  PFER-41:Message Engine-ClnPromsPmt-Send msg to user when follow_up_dt<=SYSDATE(<days in SS#4),due_dt>sysdate  *****");

        logger.info("*** Step 1 Actions: - Add new CLN_PROMS_PMT with follow_up_dt<=SYSDATE(not over 4 days) & due_dt>SYSDATE, msg_status = 0 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        clientDao.deleteClnPromsPmtByClnId(clnId);

        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        String dueAmt = randomCharacter.getNonZeroRandomNumericString(2);
        timeStamp = new TimeStamp();
        String dueDate = timeStamp.getFutureDate("MM/dd/yyyy", 2);
        String followUpDt = timeStamp.getPreviousDate("MM/dd/yyyy", 2);

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CLN_ID");
        listCol.add("PK_PROMS_PMT_SEQ");
        listCol.add("FK_FREQ_ID");
        listCol.add("FK_USER_ID");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_DT");
        listCol.add("DUE_DT");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("B_VOID");
        listCol.add("MSG_STATUS");
        listCol.add("PMT_AMT");
        listCol.add("NOTE");

        listVal.add("'" + clnId + "'");
        listVal.add("'1'");
        listVal.add("'2'");
        listVal.add("'" + userId + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + dueDate + "', 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + followUpDt + "', 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("0");
        listVal.add("1");
        listVal.add("'" + dueAmt + "'");
        listVal.add("'" + note + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("CLN_PROMS_PMT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of CLN_PROMS_PMT was inserted into DB");
        Assert.assertTrue(totalRowInserted > 0, "       New CLN_PROMS_PMT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 4 Actions: - Clear System cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("CLN_PROMS_PMT", "Pk_Cln_Id = '" + clnId + "'", 1, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 1, "        The Message Status should not be updated.");

        logger.info("*** Step 5 Expected Results: - Verify  message is added to Q_MESSAGE table - Verify values in Priority, Message columns in Q_MESSAGE");
        Thread.sleep(3000);
        Assert.assertTrue(qMessageDao.getAllQMessage().size() > 0, "       Message should be added to Q_MESSAGE table.");
        List<QMessage> listResult = qMessageDao.getQMessageByClnId(clnId);
        note = " \n\r " + note;

        Assert.assertNull(listResult.get(0).getPrio(), "       PRIO column should be null.");
        Assert.assertEquals(listResult.get(0).getMessage(), note, "       Message: " + note + " should be added to the MESSAGE column.");

        logger.info("*** Step 6 Actions: - Delete the CLN_PROMS_PMT in DB");
        clientDao.deleteClnPromsPmtByClnId(clnId);

    }

    @Test(priority = 1, description = "ClnPromsPmt-Send overdue message to supervisor when followUpDt<(Sysdate-days in SS#4) and due_dt > sysdate")
    @Parameters({"clnId"})
    public void testPFER_42(int clnId) throws Exception {
        logger.info("***** Testing - PFER-42:Message Engine-ClnPromsPmt-Send overdue message to supervisor when followUpDt<(Sysdate-days in SS#4) and due_dt > sysdate  *****");

        logger.info("*** Step 1 Actions: - Add new CLN_PROMS_PMT with follow_up_dt<=SYSDATE(not over 4 days) & due_dt>SYSDATE, b_follow_up_complete=0, b_void=0, msg_status = 1 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        clientDao.deleteClnPromsPmtByClnId(clnId);

        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        String dueAmt = randomCharacter.getNonZeroRandomNumericString(2);
        timeStamp = new TimeStamp();
        String dueDate = timeStamp.getFutureDate("MM/dd/yyyy", 2);
        String followUpDt = timeStamp.getPreviousDate("MM/dd/yyyy", 6);

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CLN_ID");
        listCol.add("PK_PROMS_PMT_SEQ");
        listCol.add("FK_FREQ_ID");
        listCol.add("FK_USER_ID");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_DT");
        listCol.add("DUE_DT");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("B_VOID");
        listCol.add("MSG_STATUS");
        listCol.add("PMT_AMT");
        listCol.add("NOTE");

        listVal.add("'" + clnId + "'");
        listVal.add("'1'");
        listVal.add("'2'");
        listVal.add("'" + userId + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + dueDate + "', 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + followUpDt + "', 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("0");
        listVal.add("1");
        listVal.add("'" + dueAmt + "'");
        listVal.add("'" + note + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("CLN_PROMS_PMT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of CLN_PROMS_PMT was inserted into DB");
        Assert.assertTrue(totalRowInserted > 0, "       New CLN_PROMS_PMT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 4 Actions: - Clear System cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("CLN_PROMS_PMT,CLN", "CLN_PROMS_PMT.PK_CLN_ID = CLN.PK_CLN_ID AND CLN_PROMS_PMT.PK_CLN_ID = '" + clnId + "'", 2, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 2, "        The Message Status should not be updated.");

        logger.info("*** Step 5 Expected Results: - Message status is updated to 2 (send to supervisor)");
        ArrayList<String> clientInfo = daoManagerPlatform.getClientInfoFromCLNPROMSPMTByClientId(String.valueOf(clnId), testDb);
        String clnAbbrev = clientInfo.get(1);

        logger.info("*** Step 5 Expected Results: - Verify  message is added to Q_MESSAGE table - Verify Priority, Message column values");
        List<QMessage> listResult = qMessageDao.getQMessageByClnId(clnId);
        note = " \n\r " + note;
        String msg;
        TimeStamp timeStamp = new TimeStamp();
        String supervisor_ID = usersDao.getUsersByUserId(clientInfo.get(2)).getSupervisorId();

        for (QMessage list : listResult) {
            msg = list.getMessage();
            Assert.assertEquals(list.getPrio(), "HIGH", "       Priority must be high");
            Assert.assertEquals(msg, "A follow-up action on a promised payment for client " + clnAbbrev + " is OVERDUE " + note, "        Q_Message inserted 2 row");
            Assert.assertEquals(list.getSubject(), " Cln Promise PMT Action Overdue ", "       Subject must be Cln Promise PMT Action Overdue");
            Assert.assertEquals(supervisor_ID, list.getToUserId(), "       FK_TO_USER_ID must be fk_supervisor_id");
            Assert.assertEquals(list.getFromUserId(), "system", "       FK_FROM_USER_ID must be system");
            Assert.assertEquals(list.getHLink(), "clnCntctMgr(" + clnId + ")", "        H_LINK : clnCntctMgr(" + clnId + ")");
            Assert.assertEquals(String.valueOf(list.getRecvDt()), timeStamp.getCurrentDate("YYYY-MM-dd"), "RECV_DT should be current date");
        }

        logger.info("*** Step 6 Actions: - Delete the CLN_PROMS_PMT in DB");
        clientDao.deleteClnPromsPmtByClnId(clnId);

    }

    @Test(priority = 1, description = "ClnPromsPmt-Send due message to assigned user when due_dt<=(sysdate-days in SS#4)")
    @Parameters({"clnId"})
    public void testPFER_43(int clnId) throws Exception {
        logger.info("***** Testing - PFER-43:Message Engine-ClnPromsPmt-Send due message to assigned user when due_dt<=(sysdate-days in SS#4)  *****");
        xifinAdminUtils = new XifinAdminUtils(driver, config);

        logger.info("*** Step 2 Actions: - Add new CLN_PROMS_PMT with follow_up_dt>SYSDATE & due_dt<=(sysdate-days in SS#4), msg_status = 0 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        clientDao.deleteClnPromsPmtByClnId(clnId);

        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        String dueAmt = randomCharacter.getNonZeroRandomNumericString(2);
        timeStamp = new TimeStamp();
        String followUpDt = timeStamp.getFutureDate("MM/dd/yyyy", 2);
        String dueDate = timeStamp.getPreviousDate("MM/dd/yyyy", 2);

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CLN_ID");
        listCol.add("PK_PROMS_PMT_SEQ");
        listCol.add("FK_FREQ_ID");
        listCol.add("FK_USER_ID");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_DT");
        listCol.add("DUE_DT");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("B_VOID");
        listCol.add("MSG_STATUS");
        listCol.add("PMT_AMT");
        listCol.add("NOTE");

        listVal.add("'" + clnId + "'");
        listVal.add("'1'");
        listVal.add("'2'");
        listVal.add("'" + userId + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + dueDate + "', 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + followUpDt + "', 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("0");
        listVal.add("0");
        listVal.add("'" + dueAmt + "'");
        listVal.add("'" + note + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("CLN_PROMS_PMT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of CLN_PROMS_PMT was inserted into DB");
        Assert.assertTrue(totalRowInserted > 0, "       New CLN_PROMS_PMT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("CLN_PROMS_PMT,CLN", "CLN_PROMS_PMT.PK_CLN_ID = CLN.PK_CLN_ID AND CLN_PROMS_PMT.PK_CLN_ID = '" + clnId + "'", 1, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 1, "        The Message Status should not be updated.");

        logger.info("*** Step 5 Expected Results: - Message status is updated to 1 (send to assigned user)");
        ArrayList<String> clientInfo = daoManagerPlatform.getClientInfoFromCLNPROMSPMTByClientId(String.valueOf(clnId), testDb);
        String clnAbbrev = clientInfo.get(1);

        logger.info("*** Step 5 Expected Results: - Verify  message is added to Q_MESSAGE table - Verify Priority, Message, Subject, Fk_to_user_id, Fk_from_user_id, Recv_dt, H_link column");
        List<QMessage> listResult = qMessageDao.getQMessageByClnId(clnId);
        note = " \n\r " + note;
        String msg;
        TimeStamp timeStamp = new TimeStamp();

        for (QMessage list : listResult) {
            msg = list.getMessage();
            Assert.assertEquals(list.getPrio(), "MED", "       Priority should be MED.");
            Assert.assertEquals(msg, "A promised payment for client " + clnAbbrev + " is due TODAY " + note, "        Q_Message: " + "A promised payment for client " + clnAbbrev + " is due TODAY " + note + " should be saved.");
            Assert.assertEquals(list.getSubject(), " Cln Promise PMT Due ", "       Subject should be 'Cln Promise PMT Due'.");
            Assert.assertEquals(userId, list.getToUserId(), "       FK_TO_USER_ID should be " + userId);
            Assert.assertEquals(list.getFromUserId(), "system", "       FK_FROM_USER_ID should be 'system'");
            Assert.assertEquals(list.getHLink(), "clnCntctMgr(" + clnId + ")", "        H_LINK : clnCntctMgr(" + clnId + ") should be saved.");
            Assert.assertEquals(String.valueOf(list.getRecvDt()), timeStamp.getCurrentDate("YYYY-MM-dd"), "        RECV_DT: " + timeStamp.getCurrentDate("YYYY-MM-dd") + " should be saved.");
        }

        logger.info("*** Step 6 Actions: - Delete the CLN_PROMS_PMT in DB");
        clientDao.deleteClnPromsPmtByClnId(clnId);

    }

    @Test(priority = 1, description = "ClnPromsPmt-Send message to user when due_dt<=(sysDt-days in SS#4), followUpDt>sysDt")
    @Parameters({"clnId"})
    public void testPFER_44(int clnId) throws Exception {
        logger.info("***** Testing - PFER-44:Message Engine-ClnPromsPmt-Send message to user when due_dt<=(sysDt-days in SS#4), followUpDt>sysDt  *****");
        xifinAdminUtils = new XifinAdminUtils(driver, config);

        logger.info("*** Step 2 Actions: - Add new record in CLN_PROMS_PMT with follow_up_dt>SYSDATE & due_dt<=SYSDATE(not over 4 days) & b_follow_up_complete=0 & b_void=0 & msg_status=1 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        clientDao.deleteClnPromsPmtByClnId(clnId);

        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        String dueAmt = randomCharacter.getNonZeroRandomNumericString(2);
        timeStamp = new TimeStamp();
        String followUpDt = timeStamp.getFutureDate("MM/dd/yyyy", 2);
        String dueDate = timeStamp.getPreviousDate("MM/dd/yyyy", 2);

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CLN_ID");
        listCol.add("PK_PROMS_PMT_SEQ");
        listCol.add("FK_FREQ_ID");
        listCol.add("FK_USER_ID");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_DT");
        listCol.add("DUE_DT");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("B_VOID");
        listCol.add("MSG_STATUS");
        listCol.add("PMT_AMT");
        listCol.add("NOTE");

        listVal.add("'" + clnId + "'");
        listVal.add("'1'");
        listVal.add("'2'");
        listVal.add("'" + userId + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + dueDate + "', 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + followUpDt + "', 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("0");
        listVal.add("1");
        listVal.add("'" + dueAmt + "'");
        listVal.add("'" + note + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("CLN_PROMS_PMT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of CLN_PROMS_PMT was inserted into DB");
        Assert.assertTrue(totalRowInserted > 0, "       New CLN_PROMS_PMT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 4 Actions: - Clear Mars cache");
        switchToDefaultWinFromFrame();
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("CLN_PROMS_PMT,CLN", "CLN_PROMS_PMT.PK_CLN_ID = CLN.PK_CLN_ID AND CLN_PROMS_PMT.PK_CLN_ID = '" + clnId + "'", 1, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 1, "        The Message Status should not be updated.");

        logger.info("*** Step 5 Expected Results: - Verify  message is added to Q_MESSAGE table - Verify Priority, Message, Subject, Fk_to_user_id, Fk_from_user_id, Recv_dt, H_link column");
        List<QMessage> listResult = qMessageDao.getQMessageByClnId(clnId);
        String msg = null;
        TimeStamp timeStamp = new TimeStamp();

        for (QMessage list : listResult) {
            msg = list.getMessage();
            Assert.assertNotNull(msg);
            Assert.assertEquals(list.getPrio(), "", "       Priority should be null.");
            Assert.assertEquals(userId, list.getToUserId(), "       FK_TO_USER_ID should be " + userId);
            Assert.assertEquals(list.getFromUserId(), "system", "       FK_FROM_USER_ID should be 'system'");
            Assert.assertEquals(list.getHLink(), "clnCntctMgr(" + clnId + ")", "        H_LINK : clnCntctMgr(" + clnId + ") should be saved.");
            Assert.assertEquals(String.valueOf(list.getRecvDt()), timeStamp.getCurrentDate("YYYY-MM-dd"), "        RECV_DT: " + timeStamp.getCurrentDate("YYYY-MM-dd") + " should be saved.");
        }

        logger.info("*** Step 6 Actions: - Delete the CLN_PROMS_PMT in DB");
        clientDao.deleteClnPromsPmtByClnId(clnId);

    }

    @Test(priority = 1, description = "ClnPromsPmt-Send message when followUpDt>sysDt, dueDt>sysDt-daysInSS#4, msgStatus=1")
    @Parameters({"clnId"})
    public void testPFER_45(int clnId) throws Exception {
        logger.info("***** Testing -  PFER-45:Message Engine-ClnPromsPmt-Send message when followUpDt>sysDt, dueDt>sysDt-daysInSS#4, msgStatus=1  *****");

        logger.info("*** Step 1 Actions: - Add new record in CLN_PROMS_PMT with follow_up_dt>SYSDATE & due_dt<=SYSDATE(over 4 days) & b_follow_up_complete=0 & b_void=0 & msg_status=1 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        clientDao.deleteClnPromsPmtByClnId(clnId);

        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        String dueAmt = randomCharacter.getNonZeroRandomNumericString(2);
        timeStamp = new TimeStamp();
        String followUpDt = timeStamp.getFutureDate("MM/dd/yyyy", 2);
        String dueDate = timeStamp.getPreviousDate("MM/dd/yyyy", 6);

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CLN_ID");
        listCol.add("PK_PROMS_PMT_SEQ");
        listCol.add("FK_FREQ_ID");
        listCol.add("FK_USER_ID");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_DT");
        listCol.add("DUE_DT");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("B_VOID");
        listCol.add("MSG_STATUS");
        listCol.add("PMT_AMT");
        listCol.add("NOTE");

        listVal.add("'" + clnId + "'");
        listVal.add("'1'");
        listVal.add("'2'");
        listVal.add("'" + userId + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + dueDate + "', 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + followUpDt + "', 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("0");
        listVal.add("1");
        listVal.add("'" + dueAmt + "'");
        listVal.add("'" + note + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("CLN_PROMS_PMT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of CLN_PROMS_PMT was inserted into DB");
        Assert.assertTrue(totalRowInserted > 0, "       New CLN_PROMS_PMT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 4 Actions: - Clear System cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("CLN_PROMS_PMT,CLN", "CLN_PROMS_PMT.PK_CLN_ID = CLN.PK_CLN_ID AND CLN_PROMS_PMT.PK_CLN_ID = '" + clnId + "'", 1, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 1, "        The Message Status should not be updated.");

        logger.info("*** Step 5 Expected Results: - Message status is still 1 (send to assigned user)");
        ArrayList<String> clientInfo = daoManagerPlatform.getClientInfoFromCLNPROMSPMTByClientId(String.valueOf(clnId), testDb);
        String clnAbbrev = clientInfo.get(1);
        Assert.assertNotNull(clnAbbrev);

        logger.info("*** Step 5 Expected Results: - Verify  message is added to Q_MESSAGE table - Verify Priority, Message, Subject, Fk_to_user_id, Fk_from_user_id, Recv_dt, H_link column");
        List<QMessage> listResult = qMessageDao.getQMessageByClnId(clnId);
        String msg = null;
        TimeStamp timeStamp = new TimeStamp();

        for (QMessage list : listResult) {
            msg = list.getMessage();
            Assert.assertNotNull(msg);
            Assert.assertEquals(list.getPrio(), "", "       Priority should be null.");
            Assert.assertEquals(userId, list.getToUserId(), "       FK_TO_USER_ID should be " + userId);
            Assert.assertEquals(list.getFromUserId(), "system", "       FK_FROM_USER_ID should be 'system'");
            Assert.assertEquals(list.getHLink(), "clnCntctMgr(" + clnId + ")", "        H_LINK : clnCntctMgr(" + clnId + ") should be saved.");
            Assert.assertEquals(String.valueOf(list.getRecvDt()), timeStamp.getCurrentDate("YYYY-MM-dd"), "        RECV_DT: " + timeStamp.getCurrentDate("YYYY-MM-dd") + " should be saved.");
        }

        logger.info("*** Step 6 Actions: - Delete the CLN_PROMS_PMT in DB");
        clientDao.deleteClnPromsPmtByClnId(clnId);

    }

    @Test(priority = 1, description = "ClnPromsPmt-Send message when followUpDt=dueDt<=sysDt, msgStatus=0")
    @Parameters({"clnId"})
    public void testPFER_46(int clnId) throws Exception {
        logger.info("***** Testing - PFER-46:Message Engine-ClnPromsPmt-Send message when followUpDt=dueDt<=sysDt, msgStatus=0  *****");
        xifinAdminUtils = new XifinAdminUtils(driver, config);

        logger.info("*** Step 2 Actions: - Add new record in CLN_PROMS_PMT with follow_up_dt = due_dt<=SYSDATE (Not over the days in SS#4) & b_follow_up_complete=0 & b_void=0 & msg_status=0 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        clientDao.deleteClnPromsPmtByClnId(clnId);

        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        String dueAmt = randomCharacter.getNonZeroRandomNumericString(2);
        timeStamp = new TimeStamp();
        String dueDate = timeStamp.getPreviousDate("MM/dd/yyyy", 2);

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CLN_ID");
        listCol.add("PK_PROMS_PMT_SEQ");
        listCol.add("FK_FREQ_ID");
        listCol.add("FK_USER_ID");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_DT");
        listCol.add("DUE_DT");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("B_VOID");
        listCol.add("MSG_STATUS");
        listCol.add("PMT_AMT");
        listCol.add("NOTE");

        listVal.add("'" + clnId + "'");
        listVal.add("'1'");
        listVal.add("'2'");
        listVal.add("'" + userId + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + dueDate + "', 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + dueDate + "', 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("0");
        listVal.add("0");
        listVal.add("'" + dueAmt + "'");
        listVal.add("'" + note + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("CLN_PROMS_PMT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of CLN_PROMS_PMT was inserted into DB");
        Assert.assertTrue(totalRowInserted > 0, "       New CLN_PROMS_PMT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 4 Actions: - Clear Mars cache");
        switchToDefaultWinFromFrame();
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("CLN_PROMS_PMT,CLN", "CLN_PROMS_PMT.PK_CLN_ID = CLN.PK_CLN_ID AND CLN_PROMS_PMT.PK_CLN_ID = '" + clnId + "'", 1, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 1, "        The Message Status should not be updated.");

        logger.info("*** Step 5 Expected Results: - Message status is updated to 1 (send to assigned user)");
        ArrayList<String> clientInfo = daoManagerPlatform.getClientInfoFromCLNPROMSPMTByClientId(String.valueOf(clnId), testDb);
        String clnAbbrev = clientInfo.get(1);

        logger.info("*** Step 5 Expected Results: - Verify  message is added to Q_MESSAGE table - Verify Priority, Message column");
        List<QMessage> listResult = qMessageDao.getQMessageByClnId(clnId);
        note = " \n\r " + note;
        String msg;
        TimeStamp timeStamp = new TimeStamp();
        String sysDt = timeStamp.getCurrentDate("YYYY-MM-dd");

        for (QMessage list : listResult) {
            msg = list.getMessage().trim();
            Assert.assertEquals(list.getPrio(), "MED", "       Priority should be MED");
            Assert.assertEquals(msg, "A follow-up action on a promised payment for client " + clnAbbrev + " is scheduled for TODAY " + note, "        Q_Message: '" + "A follow-up action on a promised payment for client " + clnAbbrev + " is scheduled for TODAY' should be saved.");
            Assert.assertEquals(list.getSubject(), " Cln Promise PMT Action ", "       Subject must be Cln Promise PMT Action Overdue");
            Assert.assertEquals(userId, list.getToUserId(), "       FK_TO_USER_ID should be userId: " + followUpUserId);
            Assert.assertEquals(list.getFromUserId(), "system", "       FK_FROM_USER_ID should be system");
            Assert.assertEquals(list.getHLink(), "clnCntctMgr(" + clnId + ")", "        H_LINK : clnCntctMgr(" + clnId + ") should be saved.");
            Assert.assertEquals(String.valueOf(list.getRecvDt()), sysDt, "        RECV_DT should be current date: " + sysDt);
        }

        logger.info("*** Step 6 Actions: - Delete the CLN_PROMS_PMT in DB");
        clientDao.deleteClnPromsPmtByClnId(clnId);

    }

    @Test(priority = 1, description = "ClnPromsPmt-Send message when dueDt>=sysdate-daysInSS#4, followUpDt<=sysDt")
    @Parameters({"clnId"})
    public void testPFER_47(int clnId) throws Exception {
        logger.info("***** Testing - PFER-47:Message Engine-ClnPromsPmt-Send message when dueDt<=sysdate-daysInSS#4, followUpDt<=sysDt  *****");
        xifinAdminUtils = new XifinAdminUtils(driver, config);

        logger.info("*** Step 2 Actions: - Add new CLN_PROMS_PMT with follow_up_dt <= Sysdate, due_dt < Sysdate(over 4 days),b_follow_up_complete =0,b_void=0,msg_status = 0 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        clientDao.deleteClnPromsPmtByClnId(clnId);

        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        String dueAmt = randomCharacter.getNonZeroRandomNumericString(2);
        timeStamp = new TimeStamp();
        String dueDate = timeStamp.getPreviousDate("MM/dd/yyyy", 6);

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CLN_ID");
        listCol.add("PK_PROMS_PMT_SEQ");
        listCol.add("FK_FREQ_ID");
        listCol.add("FK_USER_ID");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_DT");
        listCol.add("DUE_DT");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("B_VOID");
        listCol.add("MSG_STATUS");
        listCol.add("PMT_AMT");
        listCol.add("NOTE");

        listVal.add("'" + clnId + "'");
        listVal.add("'1'");
        listVal.add("'2'");
        listVal.add("'" + userId + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + dueDate + "', 'MM/DD/YYYY')");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("0");
        listVal.add("0");
        listVal.add("'" + dueAmt + "'");
        listVal.add("'" + note + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("CLN_PROMS_PMT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of CLN_PROMS_PMT was inserted into DB");
        Assert.assertTrue(totalRowInserted > 0, "       New CLN_PROMS_PMT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 4 Actions: - Clear Mars cache");
        switchToDefaultWinFromFrame();
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("CLN_PROMS_PMT,CLN", "CLN_PROMS_PMT.PK_CLN_ID = CLN.PK_CLN_ID AND CLN_PROMS_PMT.PK_CLN_ID = '" + clnId + "'", 1, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 1, "        The Message Status should not be updated.");

        logger.info("*** Step 5 Expected Results: - Message status is updated to 1 (send to assigned user)");
        ArrayList<String> clientInfo = daoManagerPlatform.getClientInfoFromCLNPROMSPMTByClientId(String.valueOf(clnId), testDb);
        String clnAbbrev = clientInfo.get(1);

        logger.info("*** Step 5 Expected Results: - Verify that new messages with proper values were added to Q_MESSAGE");
        List<QMessage> qMsgInfo = qMessageDao.getQMessageByClnId(clnId);
        note = " \n\r " + note;
        String msg;

        for (int i = 0; i < qMsgInfo.size(); i++) {
            Assert.assertEquals(qMsgInfo.size(), 2, "        2 new records should be added into Q_Message.");
            msg = qMsgInfo.get(i).getMessage().trim();
            Assert.assertEquals(msg, "A follow-up action on a promised payment for client " + clnAbbrev + " is scheduled for TODAY " + note, "        Q_Message: " + "A follow-up action on a promised payment for client " + clnAbbrev + " is scheduled for TODAY should be saved.");
            Assert.assertEquals(qMsgInfo.get(i).getPrio(), "MED", "        PRIO: MED should be saved.");
            Assert.assertEquals(qMsgInfo.get(i).getSubject().trim(), "Cln Promise PMT Action", "        SUBJECT: Cln Promise PMT Action should be saved.");
            Assert.assertEquals(qMsgInfo.get(i).getHLink(), "clnCntctMgr(" + clnId + ")", "        H_LINK : clnCntctMgr(" + clnId + ") should be saved.");
        }

        logger.info("*** Step 6 Actions: - Delete the CLN_PROMS_PMT in DB");
        clientDao.deleteClnPromsPmtByClnId(clnId);

    }

    @Test(priority = 1, description = "ClnPromsPmt-Send message when followUpDt=dueDt=sysDt, msgStatus=1")
    @Parameters({"clnId"})
    public void testPFER_48(int clnId) throws Exception {
        logger.info("***** Testing - PFER-48:Message Engine-ClnPromsPmt-Send message when followUpDt=dueDt=sysDt, msgStatus=1  *****");
        xifinAdminUtils = new XifinAdminUtils(driver, config);

        logger.info("*** Step 2 Actions: - Add new CLN_PROMS_PMT with follow_up_dt = Sysdate(not over 4 days), due_dt = Sysdate(not over 4 days),b_follow_up_complete=0,b_void=0,msg_status=1 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        clientDao.deleteClnPromsPmtByClnId(clnId);

        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        String dueAmt = randomCharacter.getNonZeroRandomNumericString(2);
        timeStamp = new TimeStamp();

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CLN_ID");
        listCol.add("PK_PROMS_PMT_SEQ");
        listCol.add("FK_FREQ_ID");
        listCol.add("FK_USER_ID");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_DT");
        listCol.add("DUE_DT");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("B_VOID");
        listCol.add("MSG_STATUS");
        listCol.add("PMT_AMT");
        listCol.add("NOTE");

        listVal.add("'" + clnId + "'");
        listVal.add("'1'");
        listVal.add("'2'");
        listVal.add("'" + userId + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("0");
        listVal.add("1");
        listVal.add("'" + dueAmt + "'");
        listVal.add("'" + note + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("CLN_PROMS_PMT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of CLN_PROMS_PMT was inserted into DB");
        Assert.assertTrue(totalRowInserted > 0, "       New CLN_PROMS_PMT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 4 Actions: - Clear Mars cache");
        switchToDefaultWinFromFrame();
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("CLN_PROMS_PMT,CLN", "CLN_PROMS_PMT.PK_CLN_ID = CLN.PK_CLN_ID AND CLN_PROMS_PMT.PK_CLN_ID = '" + clnId + "'", 1, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 1, "        The Message Status should not be updated.");

        logger.info("*** Step 5 Expected Results: - Verify that new messages with proper values were added to Q_MESSAGE");
        List<QMessage> qMsgInfo = qMessageDao.getQMessageByClnId(clnId);
        String msg;

        for (int i = 0; i < qMsgInfo.size(); i++) {
            Assert.assertEquals(qMsgInfo.size(), 2, "        2 new records should be added into Q_Message.");
            msg = qMsgInfo.get(i).getMessage().trim();
            Assert.assertEquals(msg, note, "        Q_Message: " + note + " should be saved.");
            Assert.assertEquals(qMsgInfo.get(i).getPrio(), "", "        PRIO should be null.");
            Assert.assertEquals(qMsgInfo.get(i).getHLink(), "clnCntctMgr(" + clnId + ")", "        H_LINK : clnCntctMgr(" + clnId + ") should be saved.");
        }

        logger.info("*** Step 6 Actions: - Delete the CLN_PROMS_PMT in DB");
        clientDao.deleteClnPromsPmtByClnId(clnId);

    }

    @Test(priority = 1, description = "ClnPromsPmt-Send messages dueDt before sysDt-daysInSS#4, followUpDt=sysDt,msgStatus=1")
    @Parameters({"clnId"})
    public void testPFER_49(int clnId) throws Exception {
        logger.info("***** Testing - PFER-49:Message Engine-ClnPromsPmt-Send messages dueDt before sysDt-daysInSS#4, followUpDt=sysDt,msgStatus=1  *****");
        xifinAdminUtils = new XifinAdminUtils(driver, config);

        logger.info("*** Step 2 Actions: - Add new CLN_PROMS_PMT with follow_up_dt = Sysdate(not over 4 days), due_dt before systemDate - days in SS#4,b_follow_up_complete=0,b_void=0,msg_status=1 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        clientDao.deleteClnPromsPmtByClnId(clnId);

        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        String dueAmt = randomCharacter.getNonZeroRandomNumericString(2);
        timeStamp = new TimeStamp();
        String dueDate = timeStamp.getPreviousDate("MM/dd/yyyy", 6);

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CLN_ID");
        listCol.add("PK_PROMS_PMT_SEQ");
        listCol.add("FK_FREQ_ID");
        listCol.add("FK_USER_ID");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_DT");
        listCol.add("DUE_DT");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("B_VOID");
        listCol.add("MSG_STATUS");
        listCol.add("PMT_AMT");
        listCol.add("NOTE");

        listVal.add("'" + clnId + "'");
        listVal.add("'1'");
        listVal.add("'2'");
        listVal.add("'" + userId + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("TO_DATE('" + dueDate + "', 'MM/DD/YYYY')");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("0");
        listVal.add("0");
        listVal.add("1");
        listVal.add("'" + dueAmt + "'");
        listVal.add("'" + note + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("CLN_PROMS_PMT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of CLN_PROMS_PMT was inserted into DB");
        Assert.assertTrue(totalRowInserted > 0, "       New CLN_PROMS_PMT can't be inserted to the table.");

        logger.info("*** Step 3 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 4 Actions: - Clear Mars cache");
        switchToDefaultWinFromFrame();
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("CLN_PROMS_PMT,CLN", "CLN_PROMS_PMT.PK_CLN_ID = CLN.PK_CLN_ID AND CLN_PROMS_PMT.PK_CLN_ID = '" + clnId + "'", 1, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 1, "        The Message Status should not be updated.");

        logger.info("*** Step 5 Expected Results: - Verify that new messages with proper values were added to Q_MESSAGE");
        List<QMessage> qMsgInfo = qMessageDao.getQMessageByClnId(clnId);
        String msg;

        for (int i = 0; i < qMsgInfo.size(); i++) {
            Assert.assertEquals(qMsgInfo.size(), 2, "        2 new records should be added into Q_Message.");
            msg = qMsgInfo.get(i).getMessage().trim();
            Assert.assertEquals(msg, note, "        Q_Message: " + note + " should be saved.");
            Assert.assertEquals(qMsgInfo.get(i).getPrio(), "", "        PRIO should be null.");
            Assert.assertEquals(qMsgInfo.get(i).getSubject().trim(), "", "        SUBJECT should be null.");
            Assert.assertEquals(qMsgInfo.get(i).getHLink(), "clnCntctMgr(" + clnId + ")", "        H_LINK : clnCntctMgr(" + clnId + ") should be saved.");
        }

        logger.info("*** Step 6 Actions: - Delete the CLN_PROMS_PMT in DB");
        clientDao.deleteClnPromsPmtByClnId(clnId);

    }

    @Test(priority = 1, description = "AccnCntct-Send message to assigned user when followUpDt<=sysDt, msgStatus=0")
    @Parameters({"accnId"})
    public void testPFER_50(String accnId) throws Exception {
        logger.info("***** Testing - PFER-50:Message Engine-AccnCntct-Send message to assigned user when followUpDt<=sysDt, msgStatus=0  *****");

        logger.info("*** Step 1 Actions: - Add new ACCN_CNTCT with follow_up_dt null,b_follow_up_complete=0,msg_status=1,fk_follow_up_user_id not null in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);

        String cntctInfo = "Automation Contact Info " + randomCharacter.getRandomAlphaNumericString(6);
        int seqID = accessionDao.getAccnCntctByAccnId(accnId).getCntctSeq() + 1;
        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        timeStamp = new TimeStamp();
        String followUpDt = timeStamp.getPreviousDate("MM/dd/yyyy", 2);
        accessionDao.deleteAccnCntctByAccnIdCntctSeq(accnId, seqID);

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CNTCT_SEQ");
        listCol.add("PK_ACCN_ID");
        listCol.add("PRNT_STMNT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("FK_USER_ID");
        listCol.add("B_VOID");
        listCol.add("CNTCT_DT");
        listCol.add("NOTE");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_INFO");
        listCol.add("MSG_STATUS");
        listCol.add("FK_DOC_ID");

        listVal.add("'" + seqID + "'");
        listVal.add("'" + accnId + "'");
        listVal.add("0");
        listVal.add("0");
        listVal.add("TO_DATE('" + followUpDt + "', 'MM/DD/YYYY')");
        listVal.add("'" + userId + "'");
        listVal.add("0");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("'" + note + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("'" + cntctInfo + "'");
        listVal.add("0");
        listVal.add("0");

        int totalRowInserted = daoManagerPlatform.createRowInTable("ACCN_CNTCT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of ACCN_CNTCT was inserted into DB");
        Assert.assertTrue(totalRowInserted > 0, "       New row of ACCN_CNTCT should be inserted to the table.");

        logger.info("*** Step 3 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 4 Actions: - Clear System cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("ACCN_CNTCT", "PK_ACCN_ID = '" + accnId + "' AND PK_CNTCT_SEQ = " + seqID + "", 1, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 1, "        The Message Status should not be updated.");

        logger.info("*** Step 5 Expected Results: - Verify that new messages with proper values were added to Q_MESSAGE");
        String accnID = "''" + accnId + "''";
        List<QMessage> qMsgInfo = qMessageDao.getQMessageByAccnId(accnID);
        note = "\n\r" + note;
        cntctInfo = "\n\r" + cntctInfo;
        String msg = qMsgInfo.get(0).getMessage();
        Assert.assertEquals(msg, "A follow up activity for accession " + accnId + " is scheduled for TODAY " + cntctInfo + "" + note, "        Q_MESSAGE should have message info: " + "A follow up activity for accession " + accnId + " is scheduled for TODAY " + cntctInfo + "" + note);

        logger.info("*** Step 5 Expected Results: - Q_MESSAGE will be displayed info:FK_FOLLOW_UP_USER_ID in Q_MESSAGE is updated from FK_USER_ID, SUBJECT: Accn Contact [AccnId]");
        Assert.assertEquals(qMsgInfo.get(0).getToUserId(), followUpUserId, "        FK_TO_USER_ID in Q_MESSAGE should be " + followUpUserId);
        Assert.assertEquals(qMsgInfo.get(0).getSubject(), " Accn Contact - " + accnId, "         Subject in Q_MESSAGE should have value: Accn Contact - " + accnId);

        logger.info("*** Step 6 Actions: - Delete the test data in ACCN_CNTCT in DB");
        accessionDao.deleteAccnCntctByAccnIdCntctSeq(accnId, seqID);
        Assert.assertNull(accessionDao.getAccnCntctByAccnIdCntctSeq(accnId, seqID), "        Test data should be cleared from ACCN_CNTCT.");

    }

    @Test(priority = 1, description = "AccnCntct-No message is sent when msg_status = 1 and follow_up_dt = null")
    @Parameters({"accnId"})
    public void testPFER_51(String accnId) throws Exception {
        logger.info("***** Testing -  PFER-51:Message Engine-AccnCntct-No message is sent when msg_status = 1 and follow_up_dt = null  *****");
        xifinAdminUtils = new XifinAdminUtils(driver, config);

        logger.info("*** Step 2 Actions: - Add new ACCN_CNTCT with follow_up_dt null,b_follow_up_complete=0,msg_status=1,fk_follow_up_user_id not null in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);

        String cntctInfo = "Automation Contact Info " + randomCharacter.getRandomAlphaNumericString(6);
        int seqID = accessionDao.getAccnCntctByAccnId(accnId).getCntctSeq() + 1;
        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        accessionDao.deleteAccnCntctByAccnIdCntctSeq(accnId, seqID);
        timeStamp = new TimeStamp();

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CNTCT_SEQ");
        listCol.add("PK_ACCN_ID");
        listCol.add("PRNT_STMNT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("FK_USER_ID");
        listCol.add("B_VOID");
        listCol.add("CNTCT_DT");
        listCol.add("NOTE");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_INFO");
        listCol.add("MSG_STATUS");
        listCol.add("FK_DOC_ID");

        listVal.add("'" + seqID + "'");
        listVal.add("'" + accnId + "'");
        listVal.add("0");
        listVal.add("0");
        listVal.add("'" + userId + "'");
        listVal.add("0");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("'" + note + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("'" + cntctInfo + "'");
        listVal.add("1");
        listVal.add("0");

        int totalRowInserted = daoManagerPlatform.createRowInTable("ACCN_CNTCT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of ACCN_CNTCT was inserted into DB");
        Assert.assertTrue(totalRowInserted > 0, "       New row of ACCN_CNTCT should be inserted to the table.");

        logger.info("*** Step 3 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 4 Actions: - Clear Mars cache");
        switchToDefaultWinFromFrame();
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("ACCN_CNTCT", "PK_ACCN_ID = '" + accnId + "' AND PK_CNTCT_SEQ = " + seqID + "", 1, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 1, "        The Message Status should not be updated.");

        logger.info("*** Step 5 Expected Results: - No Message is added in Q_MESSAGE");
        Assert.assertEquals(qMessageDao.getQMessageByAccnId(accnId).size(), 0, "        No Message should be added into Q_MESSAGE.");

        logger.info("*** Step 6 Actions: - Delete the test data in ACCN_CNTCT in DB");
        accessionDao.deleteAccnCntctByAccnIdCntctSeq(accnId, seqID);
        Assert.assertNull(accessionDao.getAccnCntctByAccnIdCntctSeq(accnId, seqID), "        Test data should be cleared from ACCN_CNTCT.");

    }

    @Test(priority = 1, description = "AccnCntct-No message sent when followUpDt>sysDt-days in SS#4 and msg_status = 1")
    @Parameters({"accnId"})
    public void testPFER_52(String accnId) throws Exception {
        logger.info("***** Testing - PFER-52:Message Engine-AccnCntct-No message sent when followUpDt>sysDt-days in SS#4 and msg_status = 1  *****");

        logger.info("*** Step 1 Actions: - Add new ACCN_CNTCT with follow_up_dt >sysDt-days in SS#4,b_follow_up_complete=0,msg_status=1,fk_follow_up_user_id not null in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);

        String cntctInfo = "Automation Contact Info " + randomCharacter.getRandomAlphaNumericString(6);
        int seqID = accessionDao.getAccnCntctByAccnId(accnId).getCntctSeq() + 1;
        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        timeStamp = new TimeStamp();
        String followUpDt = timeStamp.getFutureDate("MM/dd/yyyy", 6);
        accessionDao.deleteAccnCntctByAccnIdCntctSeq(accnId, seqID);

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CNTCT_SEQ");
        listCol.add("PK_ACCN_ID");
        listCol.add("PRNT_STMNT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("FK_USER_ID");
        listCol.add("B_VOID");
        listCol.add("CNTCT_DT");
        listCol.add("NOTE");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_INFO");
        listCol.add("MSG_STATUS");
        listCol.add("FK_DOC_ID");

        listVal.add("'" + seqID + "'");
        listVal.add("'" + accnId + "'");
        listVal.add("0");
        listVal.add("0");
        listVal.add("TO_DATE('" + followUpDt + "', 'MM/DD/YYYY')");
        listVal.add("'" + userId + "'");
        listVal.add("0");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("'" + note + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("'" + cntctInfo + "'");
        listVal.add("1");
        listVal.add("0");

        int totalRowInserted = daoManagerPlatform.createRowInTable("ACCN_CNTCT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of ACCN_CNTCT was inserted into DB");
        Assert.assertTrue(totalRowInserted > 0, "       New row of ACCN_CNTCT should be inserted to the table.");

        logger.info("*** Step 3 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 4 Actions: - Clear System cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("ACCN_CNTCT", "PK_ACCN_ID = '" + accnId + "' AND PK_CNTCT_SEQ = " + seqID + "", 1, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 1, "        The Message Status should not be updated.");

        logger.info("*** Step 5 Expected Results: - No Message is added in Q_MESSAGE");
        Assert.assertEquals(qMessageDao.getQMessageByAccnId(accnId).size(), 0, "        No Message should be added into Q_MESSAGE.");

        logger.info("*** Step 6 Actions: - Delete the test data in ACCN_CNTCT in DB");
        accessionDao.deleteAccnCntctByAccnIdCntctSeq(accnId, seqID);
        Assert.assertNull(accessionDao.getAccnCntctByAccnIdCntctSeq(accnId, seqID), "        Test data should be cleared from ACCN_CNTCT.");

    }

    @Test(priority = 1, description = "AccnCntct-Send message to supervisor when followUpDt<=sysDt-days in SS#4,msgStatus=1")
    @Parameters({"accnId"})
    public void testPFER_53(String accnId) throws Exception {
        logger.info("***** Testing - PFER-53:Message Engine-AccnCntct-Send message to supervisor when followUpDt<=sysDt-days in SS#4,msgStatus=1  *****");

        logger.info("*** Step 1 Actions: - Add new ACCN_CNTCT with FOLLOW_UP_DT = Sysdate - 6 | B_FOLLOW_UP_COMPLETE = 0 | MSG_STATUS = 1 | FK_FOLLOW_UP_USER_ID IS NOT NULL in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);

        String cntctInfo = "Automation Contact Info " + randomCharacter.getRandomAlphaNumericString(6);
        int seqID = accessionDao.getAccnCntctByAccnId(accnId).getCntctSeq() + 1;
        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String followUpUserId = payorDao.getRandomCurrentUserIdFromUSERS();
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        timeStamp = new TimeStamp();
        String followUpDt = timeStamp.getPreviousDate("MM/dd/yyyy", 6);
        accessionDao.deleteAccnCntctByAccnIdCntctSeq(accnId, seqID);

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_CNTCT_SEQ");
        listCol.add("PK_ACCN_ID");
        listCol.add("PRNT_STMNT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("FOLLOW_UP_DT");
        listCol.add("FK_USER_ID");
        listCol.add("B_VOID");
        listCol.add("CNTCT_DT");
        listCol.add("NOTE");
        listCol.add("FK_FOLLOW_UP_USER_ID");
        listCol.add("CNTCT_INFO");
        listCol.add("MSG_STATUS");
        listCol.add("FK_DOC_ID");

        listVal.add("'" + seqID + "'");
        listVal.add("'" + accnId + "'");
        listVal.add("0");
        listVal.add("0");
        listVal.add("TO_DATE('" + followUpDt + "', 'MM/DD/YYYY')");
        listVal.add("'" + userId + "'");
        listVal.add("0");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("'" + note + "'");
        listVal.add("'" + followUpUserId + "'");
        listVal.add("'" + cntctInfo + "'");
        listVal.add("1");
        listVal.add("0");

        int totalRowInserted = daoManagerPlatform.createRowInTable("ACCN_CNTCT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of ACCN_CNTCT was inserted into DB");
        Assert.assertTrue(totalRowInserted > 0, "       New row of ACCN_CNTCT should be inserted to the table.");

        logger.info("*** Step 3 Actions: - Update SS_MESSAGE_WAIT_TIME = 4 in DB");
        updateSystemSetting(SystemSettingMap.SS_MESSAGE_WAIT_TIME, "4", "4");

        logger.info("*** Step 4 Actions: - Clear System cache");
        xifinAdminUtils = new XifinAdminUtils(driver, config);
        xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("ACCN_CNTCT", "PK_ACCN_ID = '" + accnId + "' AND PK_CNTCT_SEQ = " + seqID + "", 2, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 2, "        The Message Status should not be updated.");

        logger.info("*** Step 5 Expected Results: Verify that new messages with proper values were added to Q_MESSAGE");

        QMessage result = qMessageDao.getQMessageByMessage(cntctInfo);

        logger.info("*** Step 5 Expected Results: Q_MESSAGE is displayed SUBJECT : Accn F/U Overdue - " + accnId + "");
        Assert.assertEquals(result.getSubject(), " Accn F/U Overdue - " + accnId + "");

        logger.info("*** Step 5 Expected Results: Verify that proper value in FK_TO_USER_ID in Q_MESSAGE");
        String supervisorId = usersDao.getUsersByUserId(followUpUserId).getSupervisorId();
        Assert.assertEquals(result.getToUserId(), supervisorId, "         The value in FK_TO_USER_ID in Q_MESSAGE should be " + supervisorId);

        logger.info("*** Step 5 Expected Results: Verify that proper value in PRIO in Q_MESSAGE");
        Assert.assertEquals(result.getPrio(), "HIGH", "        The value in PRIO in Q_MESSAGE should be HIGH.");

        logger.info("*** Step 5 Expected Results: Verify that proper value in MESSAGE in Q_MESSAGE");
        String qMessage = "A follow up activity for " + followUpUserId + " and accession " + accnId + " is OVERDUE";
        Assert.assertTrue(result.getMessage().contains(qMessage), "The value in MESSAGE in Q_MESSAGE should contains " + qMessage);
        Assert.assertTrue(result.getMessage().contains(note), "The value in MESSAGE in Q_MESSAGE should contains " + note);

        logger.info("*** Step 5 Expected Results: Verify that proper value in H_LINK in Q_MESSAGE");
        String hLink = "accnDetail('" + accnId + "')";
        Assert.assertEquals(result.getHLink(), hLink, "        The value in H_LINK in Q_MESSAGE should be " + hLink);

        logger.info("*** Step 6 Actions: - Delete the test data in ACCN_CNTCT in DB");
        accessionDao.deleteAccnCntctByAccnIdCntctSeq(accnId, seqID);
        Assert.assertNull(accessionDao.getAccnCntctByAccnIdCntctSeq(accnId, seqID), "        Test data should be cleared from ACCN_CNTCT.");

    }

    @Test(priority = 1, description = "AccnPromsPmt-Send message to assigned user when fk_proms_pmt_st = 2, msg_status = 0")
    @Parameters({"accnId"})
    public void testPFER_54(String accnId) throws Exception {
        logger.info("***** Testing - PFER-54:Message Engine-AccnPromsPmt-Send message to assigned user when fk_proms_pmt_st = 2, msg_status = 0  *****");

        logger.info("*** Step 2 Actions: - Add new ACCN_PROMS_PMT with B_FOLLOW_UP_COMPLETE = 0 | msg_status = 0 | FK_PROMS_PMT_STA = 2 in DB");
        qMessageDao.clearQMessage();
        randomCharacter = new RandomCharacter(driver);
        int promsPmtSeq = 2;
        String userId = payorDao.getRandomCurrentUserIdFromUSERS();
        String note = "Automation Note " + randomCharacter.getRandomAlphaNumericString(6);
        String reason = "Automation RSN_SYS_ENDED " + randomCharacter.getRandomAlphaNumericString(6);
        timeStamp = new TimeStamp();

        List<String> listCol = new ArrayList<>();
        List<String> listVal = new ArrayList<>();

        listCol.add("PK_ACCN_ID");
        listCol.add("PK_PROMS_PMT_SEQ");
        listCol.add("FK_CREAT_USER_ID");
        listCol.add("CNTCT_DT");
        listCol.add("DUN_CYCLE_AMT");
        listCol.add("NOTE");
        listCol.add("B_PRINT_STMT");
        listCol.add("B_FOLLOW_UP_COMPLETE");
        listCol.add("MSG_STATUS");
        listCol.add("SUBM_CNT");
        listCol.add("FK_PROMS_PMT_STA");
        listCol.add("RSN_SYS_ENDED");

        listVal.add("'" + accnId + "'");
        listVal.add("'" + promsPmtSeq + "'");
        listVal.add("'" + userId + "'");
        listVal.add("TO_DATE(TO_CHAR(sysdate, 'MM/DD/YYYY'), 'MM/DD/YYYY')");
        listVal.add("10");
        listVal.add("'" + note + "'");
        listVal.add("0");
        listVal.add("0");
        listVal.add("0");
        listVal.add("1");
        listVal.add("2");
        listVal.add("'" + reason + "'");

        int totalRowInserted = daoManagerPlatform.createRowInTable("ACCN_PROMS_PMT", listCol, listVal, testDb);

        logger.info("*** Step 2 Expected Results: - New row of ACCN_PROMS_PMT was inserted into DB");
        Assert.assertTrue(totalRowInserted > 0, "       New row of ACCN_PROMS_PMT should be inserted to the table.");

        logger.info("*** Step 3 Actions: - Run Platform Message Engine");
        int statusUpdate = isOutOfMessageQueue("ACCN_PROMS_PMT", "PK_ACCN_ID='" + accnId + "' AND PK_PROMS_PMT_SEQ = '" + promsPmtSeq + "'", 1, QUEUE_WAIT_TIME_MS);
        Assert.assertEquals(statusUpdate, 1, "       Message status should be updated to 1.");

//		logger.info("*** Step 3 Expected Results: Verify that Message Status updated to 1 (Send to assigned user)");
//		String newStatus = daoManagerPlatform.getMSG_STATUSfromTable("ACCN_PROMS_PMT", "PK_ACCN_ID='"+accnID+"' AND PK_PROMS_PMT_SEQ = '"+promsPmtSeq+"'", testDb);
//		Assert.assertTrue(newStatus.equalsIgnoreCase("1"),"       Message status should be updated to 1.");

        logger.info("*** Step 3 Expected Results: Verify that proper value in MESSAGE column in Q_MESSAGE table");
        QMessage result = qMessageDao.getQMessageByMessageAndUserId(reason, userId);
        Assert.assertEquals(result.getMessage(), reason);

        logger.info("*** Step 3 Expected Results: Verify that proper value in SUBJECT column in Q_MESSAGE table.");
        Assert.assertEquals(result.getSubject(), "Accn: " + accnId + ", proms pmt note");

        logger.info("*** Step 4 Actions: - Delete the test data in ACCN_PROMS_PMT in DB");
        int numOfRows = accessionDao.deleteAccnPromsPmtByAccnIdCntctSeq(accnId, promsPmtSeq);
        Assert.assertTrue(numOfRows > 0, "        The test data in ACCN_PROMS_PMT was not removed.");
    }

    private int isOutOfMessageQueue(String table, String clause, int msgStatus, long maxTime) throws Exception {
        long startTime = System.currentTimeMillis();
        maxTime += startTime;
        int isInQueue = Integer.parseInt(daoManagerPlatform.getMSG_STATUSfromTable(table, clause, testDb));
        while (isInQueue != msgStatus && System.currentTimeMillis() < maxTime) {
            logger.info("Waiting for message engine, elapsedTime=" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s");
            Thread.sleep(QUEUE_POLL_TIME_MS);
            isInQueue = Integer.parseInt(daoManagerPlatform.getMSG_STATUSfromTable(table, clause, testDb));
        }
        return isInQueue;
    }
}
