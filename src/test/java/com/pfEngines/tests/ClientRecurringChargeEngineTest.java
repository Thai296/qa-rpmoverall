package com.pfEngines.tests;

import com.mbasys.mars.ejb.entity.clnAdj.ClnAdj;
import com.mbasys.mars.ejb.entity.clnBalHist.ClnBalHist;
import com.mbasys.mars.ejb.entity.clnChrgs.ClnChrgs;
import com.mbasys.mars.ejb.entity.clnSubm.ClnSubm;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.utils.*;
import com.xifin.qa.config.PropertyMap;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;

public class ClientRecurringChargeEngineTest extends SeleniumBaseTest  {

	private TimeStamp timeStamp;
	private XifinAdminUtils xifinAdminUtils;
	private RandomCharacter randomCharacter;
	private final DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	
	@Test(priority = 1, description = "Freq_Typ_id=1(SemiMonthly),no data in cln_bal_hist")
	@Parameters({"project", "testSuite", "testCase"})
	public void testPFER_455(String project, String testSuite, String testCase) throws Exception {
		logger.info("====== Testing - PFER_455 ======");		
		
		timeStamp=new TimeStamp(driver);
		randomCharacter=new RandomCharacter(driver);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		
		logger.info("*** Step 1 Actions: - Create a new Client via Client WS");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.CLNWS_URL), config.getProperty(PropertyMap.CLNWS_USER), config.getProperty(PropertyMap.CLNWS_PASSWORD), config.getProperties());
		String clnAbbrev = resultProperties.getProperty("ClientId");
		
		logger.info("*** Step 1 Expected Results: - Verify that a new Client is available for using");
		assertNotNull(clnAbbrev, "        A new Client should be created.");
		
		logger.info("*** Step 2 Actions: - Update cln.FK_SUBM_FREQ_TYP_ID = 1 (SemiMonthly) in DB");
		int clnId=clientDao.getClnByClnAbbrev(clnAbbrev.toUpperCase()).getClnId();
		int num = daoManagerPlatform.setValuesFromTableByColNameValue("CLN","FK_SUBM_FREQ_TYP_ID =1","PK_CLN_ID", String.valueOf(clnId),testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that the Client is updated");
		assertTrue(num > 0, "        Client " + clnAbbrev + " should be updated with cln.FK_SUBM_FREQ_TYP_ID = 1.");
		
		logger.info("*** Step 3 Actions: - Update SS 1 (Override Operating System Date) with date < 15");
		Date currDt = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getCurrentDate());
		String  newSS1Date = timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 1, 14);
		num = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE='"+newSS1Date+"'","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 3 Expected results: - Verify that SS#1 is updated.");
		assertTrue(num > 0, "        SS#1 should be updated to " + newSS1Date);
		
		logger.info("*** Step 4 Actions: - Add a new record in Cln_Chrgs table with FK_FREQ_TYP_ID = 1 (SemiMonthly) in DB");
		String freqTyp="1";
		String firstDateOfMonth = timeStamp.getFirstDateOfMonth("MM/dd/yyyy", new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date));
		Date effDtInClnChrg = new SimpleDateFormat("MM/dd/yyyy").parse(firstDateOfMonth);
		int adjCd = adjustmentCodeDao.getAdjCdByTypIdOneOrTwo().getAdjCdId();
		int chrgs = Integer.parseInt(randomCharacter.getNonZeroRandomNumericString(2));
		
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_SEQ_NUM");
		insertFields.add("FK_FREQ_TYP_ID");
		insertFields.add("FK_ADJ_CD_ID");
		insertFields.add("EFF_DT");
		insertFields.add("EXP_DT");
		insertFields.add("CHRG");
		insertFields.add("CMNT");
		
		List <Object> insertValues= new ArrayList<>();
		insertValues.add(clnId);
		insertValues.add(1);
		insertValues.add(freqTyp);
		insertValues.add(adjCd);
		insertValues.add(effDtInClnChrg);
		insertValues.add("");
		insertValues.add(chrgs);
		insertValues.add("");
		num = daoManagerPlatform.addRecordIntoTable("CLN_CHRGS", insertFields, insertValues, testDb);
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added into Cln_Chrgs table");
		assertTrue(num > 0, "        A new record should be added into Cln_Chrgs table.");

		String midOfMonthDt = timeStamp.getNextDay("MM/dd/yyyy", new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date));
		Date submDt = new SimpleDateFormat("MM/dd/yyyy").parse(midOfMonthDt);		
		
		logger.info("*** Step 5 Actions: - Add a new record in Cln_Subm table in DB");
		String dueAmtInClnSubm = randomCharacter.getNonZeroRandomNumericString(2);
		
		insertFields.clear();//Remove all the elements from the list
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_SUBM_DT");
		insertFields.add("DUE_AMT");
		insertFields.add("ORIG_AMT");
		insertFields.add("PER_DIEM_COUNT");
		insertFields.add("PER_DIEM_AMT");
		
		insertValues.clear();//Remove all the elements from the list
		insertValues.add(clnId);
		insertValues.add(submDt);
		insertValues.add(dueAmtInClnSubm);
		insertValues.add(dueAmtInClnSubm);
		insertValues.add("0");
		insertValues.add("0");
		num = daoManagerPlatform.addRecordIntoTable("CLN_SUBM", insertFields, insertValues, testDb);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new record should be added in CLN_SUBM table");
		assertTrue(num>0,"        A new record should be added into CLN_SUBM table.");
		
		logger.info("*** Step 6 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 7 Actions: - Run PF-Client Recurring Charge Engine");
		Assert.assertTrue(clientDao.waitForClientRecurringChargesEngine(clnId, QUEUE_WAIT_TIME_MS));
		
		logger.info("*** Step 7 Expected Results: - Verify that the data in Cln_Chrgs table are updated properly");
		ClnChrgs clnChrgsInfoList = clientDao.getClnChrgsByClnId(clnId);
		String lastAppliedDt =formatter.format(clnChrgsInfoList.getLastAppliedDt());
		String audUser = clnChrgsInfoList.getAudUser();
		assertEquals(lastAppliedDt, newSS1Date, "        cln_chrgs.LAST_APPLIED_DT should be updated to " + newSS1Date);
		assertTrue(audUser.contains("ClnRecurringChargesEngine"),"        cln_chrgs.aud_users should be ClnRecurringChargesEngine.");
		
		logger.info("*** Step 7 Expected Results: - Verify that a new record is added into cln_bal_hist table");
		Date SS1DateinDate = new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date);
		Date midMothDt = new SimpleDateFormat("MM/dd/yyyy").parse(midOfMonthDt);
		ClnBalHist clnBalHistInfoList=clientDao.getClnBalHistByClnId(clnId);
		assertNotNull(clnBalHistInfoList, "        A new record should be inserted into cln_bal_hist table.");
		assertEquals((int)clnBalHistInfoList.getExpctdDueAmt(), chrgs, "        cln_bal_hist.Expctd_Due_Amt = cln_chrgs.chrg.");
				
		logger.info("*** Step 7 Expected Results: - Verify that the data is updated in cln_subm table properly");
		int updatedDueAmt= chrgs + Integer.parseInt(dueAmtInClnSubm);
		ClnSubm clnSubmInfoList=clientDao.getClnSubmByClnIdAndSubmDt(clnId,new java.sql.Date(midMothDt.getTime()));
		assertEquals((int)clnSubmInfoList.getDueAmt(), updatedDueAmt,"        cln_subm.due_amt = original cln_subm.due_amt + cln_chrgs.chrg.");
		assertTrue(clnSubmInfoList.getAudUser().contains("ClnRecurringChargesEngine"), "        cln_subm.Aud_User should be ClnRecurringChargesEngine.");
		
		logger.info("*** Step 7 Expected Results: - Verify that a new record is added into cln_adj table");
		ClnAdj clnAdjInfoList = clientDao.getClnAdjByClnId(clnId);
		assertNotNull(clnAdjInfoList ,"        A new record should be added to Cln_Adj table.");
		
		assertTrue(clnAdjInfoList.getIsPrintable(),"        cln_adj.B_Printable = 1.");
		assertTrue(clnAdjInfoList.getIsPosted(),"        cln_adj.B_Posted = 1.");
		assertEquals(clnAdjInfoList.getAdjSeq(),1,"        cln_adj.Pk_Adj_Seq = 1.");
		assertEquals(clnAdjInfoList.getAdjCdId(),clnChrgsInfoList.getAdjCdId(),"        cln_adj.Fk_Adj_Cd_Id = Cln_Chrgs.fk_adj_cd_id.");
		assertEquals(clnAdjInfoList.getAdjAmtAsMoney(),clnChrgsInfoList.getChrgAsMoney(),"        cln_adj.Adj_Amt = Cln_Chrgs.Charge.");
		assertEquals(clnAdjInfoList.getNote(),clnChrgsInfoList.getCmnt(),"        cln_adj.Note = Cln_Chrgs.CMNT.");
		assertEquals(clnAdjInfoList.getUserId(),"ClnRecurringCharges","        cln_adj.Fk_User_Id = 'ClnRecurringCharges'.");
		assertEquals(clnAdjInfoList.getAdjDt(),SS1DateinDate,"        cln_adj.Adj_Dt = SS#1 (" + newSS1Date + ")");
		
		logger.info("*** Step 8 Actions: - Clear/Reset test data in DB");
		daoManagerPlatform.deleteClnFromCLNByClnId(String.valueOf(clnId), testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = null","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 9 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();

	}
	
	@Test(priority = 1, description = "Freq_Typ_id=1(SemiMonthly),cln_bal_hist and cln_subm has data")
	@Parameters({"project", "testSuite", "testCase"})
	public void testPFER_456(String project, String testSuite, String testCase) throws Exception {
		logger.info("====== Testing - PFER_456 ======");		
		
		timeStamp=new TimeStamp(driver);
		randomCharacter=new RandomCharacter(driver);		
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		
		logger.info("*** Step 1 Actions: - Create a new Client via Client WS");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.CLNWS_URL), config.getProperty(PropertyMap.CLNWS_USER), config.getProperty(PropertyMap.CLNWS_PASSWORD), config.getProperties());
		String clnAbbrev = resultProperties.getProperty("ClientId");
		
		logger.info("*** Step 1 Expected Results: - Verify that a new Client is available for using");
		assertNotNull(clnAbbrev, "        A new Client should be created.");
		
		logger.info("*** Step 2 Actions: - Update cln.FK_SUBM_FREQ_TYP_ID = 1 (SemiMonthly) in DB");
		int clnId=clientDao.getClnByClnAbbrev(clnAbbrev.toUpperCase()).getClnId();
		int num = daoManagerPlatform.setValuesFromTableByColNameValue("CLN","FK_SUBM_FREQ_TYP_ID =1","PK_CLN_ID", String.valueOf(clnId),testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that the Client is updated");
		assertTrue(num > 0, "        Client " + clnAbbrev + " should be updated with cln.FK_SUBM_FREQ_TYP_ID = 1.");
		
		logger.info("*** Step 3 Actions: - Update SS 1 (Override Operating System Date) with date < 15");
		Date currDt = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getCurrentDate());
		String  newSS1Date = timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 1, 14);
		num = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE='"+newSS1Date+"'","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 3 Expected results: - Verify that SS#1 is updated.");
		assertTrue(num > 0, "        SS#1 should be updated to " + newSS1Date);
		
		logger.info("*** Step 4 Actions: - Add a new record in Cln_Chrgs table with FK_FREQ_TYP_ID = 1 (SemiMonthly) in DB");
		String freqTyp="1";
		String firstDateOfMonth = timeStamp.getFirstDateOfMonth("MM/dd/yyyy", new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date));
		Date effDtInClnChrg = new SimpleDateFormat("MM/dd/yyyy").parse(firstDateOfMonth);
		int adjCd = adjustmentCodeDao.getAdjCdByTypIdOneOrTwo().getAdjCdId();
		int chrgs = Integer.parseInt(randomCharacter.getNonZeroRandomNumericString(2));
		
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_SEQ_NUM");
		insertFields.add("FK_FREQ_TYP_ID");
		insertFields.add("FK_ADJ_CD_ID");
		insertFields.add("EFF_DT");
		insertFields.add("EXP_DT");
		insertFields.add("CHRG");
		insertFields.add("CMNT");
		
		List <Object> insertValues=new ArrayList<>();
		insertValues.add(clnId);
		insertValues.add(1);
		insertValues.add(freqTyp);
		insertValues.add(adjCd);
		insertValues.add(effDtInClnChrg);
		insertValues.add("");
		insertValues.add(chrgs);
		insertValues.add("");
		num = daoManagerPlatform.addRecordIntoTable("CLN_CHRGS", insertFields, insertValues, testDb);
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added into Cln_Chrgs table");
		assertTrue(num > 0, "        A new record should be added into Cln_Chrgs table.");
		
		logger.info("*** Step 5 Actions: - Add a new record in Cln_Bal_Hist table in DB");
		Date effDtInClnBalHist= new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date);
		String midOfMonthDt = timeStamp.getNextDay("MM/dd/yyyy", new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date));
		Date submDt = new SimpleDateFormat("MM/dd/yyyy").parse(midOfMonthDt);
		String expctdDueAmt = randomCharacter.getNonZeroRandomNumericString(2);
		
		insertFields.clear();//Remove all the elements from the list
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_EFF_DT");
		insertFields.add("PK_SUBM_DT");
		insertFields.add("EXPCTD_DUE_AMT");
		insertFields.add("RETRO_DUE_AMT");
		
		insertValues.clear();//Remove all the elements from the list
		insertValues.add(clnId);
		insertValues.add(effDtInClnBalHist); 
		insertValues.add(submDt);
		insertValues.add(expctdDueAmt);
		insertValues.add("0");
		num = daoManagerPlatform.addRecordIntoTable("CLN_BAL_HIST", insertFields, insertValues, testDb);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new record should be added in CLN_BAL_HIST table");
		assertTrue(num>0,"        A new record should be added into CLN_BAL_HIST table.");
		
		logger.info("*** Step 6 Actions: - Add a new record in Cln_Subm table in DB");
		String dueAmtInClnSubm = randomCharacter.getNonZeroRandomNumericString(2);
		
		insertFields.clear();//Remove all the elements from the list
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_SUBM_DT");
		insertFields.add("DUE_AMT");
		insertFields.add("ORIG_AMT");
		insertFields.add("PER_DIEM_COUNT");
		insertFields.add("PER_DIEM_AMT");
		
		insertValues.clear();//Remove all the elements from the list
		insertValues.add(clnId);
		insertValues.add(submDt);
		insertValues.add(dueAmtInClnSubm);
		insertValues.add(dueAmtInClnSubm);
		insertValues.add("0");
		insertValues.add("0");
		num = daoManagerPlatform.addRecordIntoTable("CLN_SUBM", insertFields, insertValues, testDb);
		
		logger.info("*** Step 6 Expected Results: - Verify that a new record should be added in CLN_SUBM table");
		assertTrue(num>0,"        A new record should be added into CLN_SUBM table.");
		
		logger.info("*** Step 7 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 8 Actions: - Run PF-Client Recurring Charge Engine");
		Assert.assertTrue(clientDao.waitForClientRecurringChargesEngine(clnId, QUEUE_WAIT_TIME_MS));
		
		logger.info("*** Step 8 Expected Results: - Verify that the data in Cln_Chrgs table are updated properly");
		ClnChrgs clnChrgsInfoList = clientDao.getClnChrgsByClnId(clnId);
		String lastAppliedDt =formatter.format(clnChrgsInfoList.getLastAppliedDt());
		String audUser = clnChrgsInfoList.getAudUser();
		assertEquals(lastAppliedDt, newSS1Date, "        cln_chrgs.LAST_APPLIED_DT should be updated to " + newSS1Date);
		assertTrue(audUser.contains("ClnRecurringChargesEngine"),"        cln_chrgs.aud_users should be ClnRecurringChargesEngine.");
		
		logger.info("*** Step 8 Expected Results: - Verify that the record is updated in cln_bal_hist table properly");
		Date SS1DateinDate = new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date);
		Date midMothDt = new SimpleDateFormat("MM/dd/yyyy").parse(midOfMonthDt);
		ClnBalHist clnBalHistInfoList=clientDao.getClnBalHistByClnId(clnId);
		int updatedExpctdDueAmt = chrgs + Integer.parseInt(expctdDueAmt);
		assertEquals((int)clnBalHistInfoList.getExpctdDueAmt(), updatedExpctdDueAmt, "        New Expctd_Due_Amt = cln_bal_hist.Expctd_Due_Amt + cln_chrgs.chrg.");
				
		logger.info("*** Step 8 Expected Results: - Verify that the data is updated in cln_subm table properly");
		int updatedDueAmt= chrgs + Integer.parseInt(dueAmtInClnSubm);
		ClnSubm clnSubmInfoList=clientDao.getClnSubmByClnIdAndSubmDt(clnId,new java.sql.Date(midMothDt.getTime()));
		assertEquals((int)clnSubmInfoList.getDueAmt(), updatedDueAmt,"        cln_subm.due_amt = original cln_subm.due_amt + cln_chrgs.chrg.");
		assertTrue(clnSubmInfoList.getAudUser().contains("ClnRecurringChargesEngine"), "        cln_subm.Aud_User should be ClnRecurringChargesEngine.");
		
		logger.info("*** Step 8 Expected Results: - Verify that a new record is added into cln_adj table");
		ClnAdj clnAdjInfoList = clientDao.getClnAdjByClnId(clnId);
		assertNotNull(clnAdjInfoList ,"        A new record should be added to Cln_Adj table.");
		
		assertTrue(clnAdjInfoList.getIsPrintable(),"        cln_adj.B_Printable = 1.");
		assertTrue(clnAdjInfoList.getIsPosted(),"        cln_adj.B_Posted = 1.");
		assertEquals(clnAdjInfoList.getAdjSeq(),1,"        cln_adj.Pk_Adj_Seq = 1.");
		assertEquals(clnAdjInfoList.getAdjCdId(),clnChrgsInfoList.getAdjCdId(),"        cln_adj.Fk_Adj_Cd_Id = Cln_Chrgs.fk_adj_cd_id.");
		assertEquals(clnAdjInfoList.getAdjAmtAsMoney(),clnChrgsInfoList.getChrgAsMoney(),"        cln_adj.Adj_Amt = Cln_Chrgs.Charge.");
		assertEquals(clnAdjInfoList.getNote(),clnChrgsInfoList.getCmnt(),"        cln_adj.Note = Cln_Chrgs.CMNT.");
		assertEquals(clnAdjInfoList.getUserId(),"ClnRecurringCharges","        cln_adj.Fk_User_Id = 'ClnRecurringCharges'.");
		assertEquals(clnAdjInfoList.getAdjDt(),SS1DateinDate,"        cln_adj.Adj_Dt = SS#1 (" + newSS1Date + ")");
		
		logger.info("*** Step 9 Actions: - Clear/Reset test data in DB");
		daoManagerPlatform.deleteClnFromCLNByClnId(String.valueOf(clnId), testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = null","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 10 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();

	}
	
	@Test(priority = 1, description = "Freq_Typ_id=1(SemiMonthly), no data in Cln_subm")
	@Parameters({"project", "testSuite", "testCase"})
	public void testPFER_457(String project, String testSuite, String testCase) throws Exception {
		logger.info("====== Testing - PFER_457 ======");		
		
		timeStamp=new TimeStamp(driver);
		randomCharacter=new RandomCharacter(driver);		
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		
		logger.info("*** Step 1 Actions: - Create a new Client via Client WS");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.CLNWS_URL), config.getProperty(PropertyMap.CLNWS_USER), config.getProperty(PropertyMap.CLNWS_PASSWORD), config.getProperties());
		String clnAbbrev = resultProperties.getProperty("ClientId");
		
		logger.info("*** Step 1 Expected Results: - Verify that a new Client is available for using");
		assertNotNull(clnAbbrev, "        A new Client should be created.");
		
		logger.info("*** Step 2 Actions: - Update cln.FK_SUBM_FREQ_TYP_ID = 1 (SemiMonthly) in DB");
		int clnId=clientDao.getClnByClnAbbrev(clnAbbrev.toUpperCase()).getClnId();
		int num = daoManagerPlatform.setValuesFromTableByColNameValue("CLN","FK_SUBM_FREQ_TYP_ID =1","PK_CLN_ID", String.valueOf(clnId),testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that the Client is updated");
		assertTrue(num > 0, "        Client " + clnAbbrev + " should be updated with cln.FK_SUBM_FREQ_TYP_ID = 1.");
		
		logger.info("*** Step 3 Actions: - Update SS 1 (Override Operating System Date) with date < 15");
		Date currDt = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getCurrentDate());
		String  newSS1Date = timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 1, 14);
		num = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE='"+newSS1Date+"'","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 3 Expected results: - Verify that SS#1 is updated.");
		assertTrue(num > 0, "        SS#1 should be updated to " + newSS1Date);
		
		logger.info("*** Step 4 Actions: - Add a new record in Cln_Chrgs table with FK_FREQ_TYP_ID = 1 (SemiMonthly) in DB");
		String freqTyp="1";
		String firstDateOfMonth = timeStamp.getFirstDateOfMonth("MM/dd/yyyy", new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date));
		Date effDtInClnChrg = new SimpleDateFormat("MM/dd/yyyy").parse(firstDateOfMonth);
		int adjCd = adjustmentCodeDao.getAdjCdByTypIdOneOrTwo().getAdjCdId();
		int chrgs = Integer.parseInt(randomCharacter.getNonZeroRandomNumericString(2));
		
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_SEQ_NUM");
		insertFields.add("FK_FREQ_TYP_ID");
		insertFields.add("FK_ADJ_CD_ID");
		insertFields.add("EFF_DT");
		insertFields.add("EXP_DT");
		insertFields.add("CHRG");
		insertFields.add("CMNT");
		
		List <Object> insertValues=new ArrayList<>();
		insertValues.add(clnId);
		insertValues.add(1);
		insertValues.add(freqTyp);
		insertValues.add(adjCd);
		insertValues.add(effDtInClnChrg);
		insertValues.add("");
		insertValues.add(chrgs);
		insertValues.add("");
		num = daoManagerPlatform.addRecordIntoTable("CLN_CHRGS", insertFields, insertValues, testDb);
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added into Cln_Chrgs table");
		assertTrue(num > 0, "        A new record should be added into Cln_Chrgs table.");

		logger.info("*** Step 5 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 6 Actions: - Run PF-Client Recurring Charge Engine");
		Assert.assertTrue(clientDao.waitForClientRecurringChargesEngine(clnId, QUEUE_WAIT_TIME_MS));
		
		logger.info("*** Step 6 Expected Results: - Verify that the data in Cln_Chrgs table are updated properly");
		ClnChrgs clnChrgsInfoList = clientDao.getClnChrgsByClnId(clnId);
		String lastAppliedDt =formatter.format(clnChrgsInfoList.getLastAppliedDt());
		String audUser = clnChrgsInfoList.getAudUser();
		assertEquals(lastAppliedDt, newSS1Date, "        cln_chrgs.LAST_APPLIED_DT should be updated to " + newSS1Date);
		assertTrue(audUser.contains("ClnRecurringChargesEngine"),"        cln_chrgs.aud_users should be ClnRecurringChargesEngine.");		
				
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added into cln_bal_hist table");		
		String midOfMonthDt = timeStamp.getNextDay("MM/dd/yyyy", new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date));
		Date SS1DateinDate = new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date);
		Date midMothDt = new SimpleDateFormat("MM/dd/yyyy").parse(midOfMonthDt);
		ClnBalHist clnBalHistInfoList=clientDao.getClnBalHistByClnId(clnId);
		assertNotNull(clnBalHistInfoList, "        A new record should be inserted into cln_bal_hist table.");
		assertEquals((int)clnBalHistInfoList.getExpctdDueAmt(), chrgs, "        cln_bal_hist.Expctd_Due_Amt = cln_chrgs.chrg.");
		
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added in cln_subm table");
		ClnSubm clnSubmInfoList=clientDao.getClnSubmByClnIdAndSubmDt(clnId,new java.sql.Date(midMothDt.getTime()));
		assertNotNull(clnSubmInfoList, "        A new record should be inserted into cln_subm table.");
		
		assertEquals((int)clnSubmInfoList.getDueAmt(), chrgs,"        cln_subm.due_amt = cln_chrgs.chrg.");
		assertTrue(clnSubmInfoList.getAudUser().contains("ClnRecurringChargesEngine"), "        cln_subm.Aud_User should be ClnRecurringChargesEngine.");
		assertEquals(clnSubmInfoList.getNumUnpostedPmt(), 0,"        cln_subm.NUM_UNPOSTED_PMT = 0.");
		
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added into cln_adj table");
		ClnAdj clnAdjInfoList = clientDao.getClnAdjByClnId(clnId);
		assertNotNull(clnAdjInfoList,"        A new record should be added to Cln_Adj table.");
		
		assertTrue(clnAdjInfoList.getIsPrintable(),"        cln_adj.B_Printable = 1.");
		assertTrue(clnAdjInfoList.getIsPosted(),"        cln_adj.B_Posted = 1.");
		assertEquals(clnAdjInfoList.getAdjSeq(),1,"        cln_adj.Pk_Adj_Seq = 1.");
		assertEquals(clnAdjInfoList.getAdjCdId(),clnChrgsInfoList.getAdjCdId(),"        cln_adj.Fk_Adj_Cd_Id = Cln_Chrgs.fk_adj_cd_id.");
		assertEquals(clnAdjInfoList.getAdjAmtAsMoney(),clnChrgsInfoList.getChrgAsMoney(),"        cln_adj.Adj_Amt = Cln_Chrgs.Charge.");
		assertEquals(clnAdjInfoList.getNote(),clnChrgsInfoList.getCmnt(),"        cln_adj.Note = Cln_Chrgs.CMNT.");
		assertEquals(clnAdjInfoList.getUserId(),"ClnRecurringCharges","        cln_adj.Fk_User_Id = 'ClnRecurringCharges'.");
		assertEquals(clnAdjInfoList.getAdjDt(),SS1DateinDate,"        cln_adj.Adj_Dt = SS#1 (" + newSS1Date + ")");
		
		logger.info("*** Step 7 Actions: - Clear/Reset test data in DB");
		daoManagerPlatform.deleteClnFromCLNByClnId(String.valueOf(clnId), testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = null","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 8 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();

	}

	@Test(priority = 1, description = "Freq_Typ_id=1(SemiMonthly),SS1Day>15,no data in cln_bal_hist,Cln_subm")
	@Parameters({"project", "testSuite", "testCase"})
	public void testPFER_458(String project, String testSuite, String testCase) throws Exception {
		logger.info("====== Testing - PFER_458 ======");		
		
		timeStamp=new TimeStamp(driver);
		randomCharacter=new RandomCharacter(driver);		
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		
		logger.info("*** Step 1 Actions: - Create a new Client via Client WS");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.CLNWS_URL), config.getProperty(PropertyMap.CLNWS_USER), config.getProperty(PropertyMap.CLNWS_PASSWORD), config.getProperties());
		String clnAbbrev = resultProperties.getProperty("ClientId");
		
		logger.info("*** Step 1 Expected Results: - Verify that a new Client is available for using");
		assertNotNull(clnAbbrev, "        A new Client should be created.");
		
		logger.info("*** Step 2 Actions: - Update cln.FK_SUBM_FREQ_TYP_ID = 1 (SemiMonthly) in DB");
		int clnId=clientDao.getClnByClnAbbrev(clnAbbrev.toUpperCase()).getClnId();
		int num = daoManagerPlatform.setValuesFromTableByColNameValue("CLN","FK_SUBM_FREQ_TYP_ID =1","PK_CLN_ID", String.valueOf(clnId),testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that the Client is updated");
		assertTrue(num > 0, "        Client " + clnAbbrev + " should be updated with cln.FK_SUBM_FREQ_TYP_ID = 1.");
		
		logger.info("*** Step 3 Actions: - Update SS 1 (Override Operating System Date) with date > 15");
		Date currDt = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getCurrentDate());
		String  newSS1Date = timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 2, 16);
		num = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE='"+newSS1Date+"'","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 3 Expected results: - Verify that SS#1 is updated.");
		assertTrue(num > 0, "        SS#1 should be updated to " + newSS1Date);
		
		logger.info("*** Step 4 Actions: - Add a new record in Cln_Chrgs table with FK_FREQ_TYP_ID = 1 (SemiMonthly), EffDt <= SS#1.FirstDateOfMonth in DB");
		String freqTyp="1";
		Date effDtInClnChrg = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 3, 16));
		int adjCd = adjustmentCodeDao.getAdjCdByTypIdOneOrTwo().getAdjCdId();
		int chrgs = Integer.parseInt(randomCharacter.getNonZeroRandomNumericString(2));
		
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_SEQ_NUM");
		insertFields.add("FK_FREQ_TYP_ID");
		insertFields.add("FK_ADJ_CD_ID");
		insertFields.add("EFF_DT");
		insertFields.add("EXP_DT");
		insertFields.add("CHRG");
		insertFields.add("CMNT");
		
		List <Object> insertValues=new ArrayList<>();
		insertValues.add(clnId);
		insertValues.add(1);
		insertValues.add(freqTyp);
		insertValues.add(adjCd);
		insertValues.add(effDtInClnChrg);
		insertValues.add("");
		insertValues.add(chrgs);
		insertValues.add("");
		num = daoManagerPlatform.addRecordIntoTable("CLN_CHRGS", insertFields, insertValues, testDb);
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added into Cln_Chrgs table");
		assertTrue(num > 0, "        A new record should be added into Cln_Chrgs table.");

		logger.info("*** Step 5 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 6 Actions: - Run PF-Client Recurring Charge Engine");
		Assert.assertTrue(clientDao.waitForClientRecurringChargesEngine(clnId, QUEUE_WAIT_TIME_MS));
		
		logger.info("*** Step 6 Expected Results: - Verify that the data in Cln_Chrgs table are updated properly");
		ClnChrgs clnChrgsInfoList = clientDao.getClnChrgsByClnId(clnId);
		String lastAppliedDt =formatter.format(clnChrgsInfoList.getLastAppliedDt());
		String audUser = clnChrgsInfoList.getAudUser();
		assertEquals(lastAppliedDt, newSS1Date, "        cln_chrgs.LAST_APPLIED_DT should be updated to " + newSS1Date);
		assertTrue(audUser.contains("ClnRecurringChargesEngine"),"        cln_chrgs.aud_users should be ClnRecurringChargesEngine.");		
				
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added into cln_bal_hist table");		
		String lastDtOfMonth = timeStamp.getLastDateOfMonth("MM/dd/yyyy", new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date));
		Date SS1DateinDate = new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date);
		Date midMothDt = new SimpleDateFormat("MM/dd/yyyy").parse(lastDtOfMonth);
		ClnBalHist clnBalHistInfoList=clientDao.getClnBalHistByClnId(clnId);
		assertNotNull(clnBalHistInfoList, "        A new record should be inserted into cln_bal_hist table.");
		assertEquals((int)clnBalHistInfoList.getExpctdDueAmt(), chrgs, "        cln_bal_hist.Expctd_Due_Amt = cln_chrgs.chrg.");
		
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added in cln_subm table");
		ClnSubm clnSubmInfoList=clientDao.getClnSubmByClnIdAndSubmDt(clnId,new java.sql.Date(midMothDt.getTime()));
		assertNotNull(clnSubmInfoList, "        A new record should be inserted into cln_subm table.");
		
		assertEquals((int)clnSubmInfoList.getDueAmt(), chrgs,"        cln_subm.due_amt = cln_chrgs.chrg.");
		assertTrue(clnSubmInfoList.getAudUser().contains("ClnRecurringChargesEngine"), "        cln_subm.Aud_User should be ClnRecurringChargesEngine.");
		assertEquals(clnSubmInfoList.getNumUnpostedPmt(), 0,"        cln_subm.NUM_UNPOSTED_PMT = 0.");
		
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added into cln_adj table");
		ClnAdj clnAdjInfoList = clientDao.getClnAdjByClnId(clnId);
		assertNotNull(clnAdjInfoList ,"        A new record should be added to Cln_Adj table.");
		
		assertTrue(clnAdjInfoList.getIsPrintable(),"        cln_adj.B_Printable = 1.");
		assertTrue(clnAdjInfoList.getIsPosted(),"        cln_adj.B_Posted = 1.");
		assertEquals(clnAdjInfoList.getAdjSeq(),1,"        cln_adj.Pk_Adj_Seq = 1.");
		assertEquals(clnAdjInfoList.getAdjCdId(),clnChrgsInfoList.getAdjCdId(),"        cln_adj.Fk_Adj_Cd_Id = Cln_Chrgs.fk_adj_cd_id.");
		assertEquals(clnAdjInfoList.getAdjAmtAsMoney(),clnChrgsInfoList.getChrgAsMoney(),"        cln_adj.Adj_Amt = Cln_Chrgs.Charge.");
		assertEquals(clnAdjInfoList.getNote(),clnChrgsInfoList.getCmnt(),"        cln_adj.Note = Cln_Chrgs.CMNT.");
		assertEquals(clnAdjInfoList.getUserId(),"ClnRecurringCharges","        cln_adj.Fk_User_Id = 'ClnRecurringCharges'.");
		assertEquals(clnAdjInfoList.getAdjDt(),SS1DateinDate,"        cln_adj.Adj_Dt = SS#1 (" + newSS1Date + ")");
		assertEquals(clnAdjInfoList.getPrntSubmDt(),midMothDt,"        cln_adj.FK_PRNT_SUBM_DT = " + lastDtOfMonth);
		
		logger.info("*** Step 7 Actions: - Clear/Reset test data in DB");
		daoManagerPlatform.deleteClnFromCLNByClnId(String.valueOf(clnId), testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = null","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 8 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();

	}
	
	@Test(priority = 1, description = "Freq_Typ_id=2(Monthly),SS1Day greater than 15,no data in Cln_Subm,Cln_bal_hist tables")
	@Parameters({"project", "testSuite", "testCase"})
	public void testPFER_459(String project, String testSuite, String testCase ) throws Exception {
		logger.info("====== Testing - PFER_459 ======");		
		
		timeStamp=new TimeStamp(driver);
		randomCharacter=new RandomCharacter(driver);		
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		
		logger.info("*** Step 1 Actions: - Create a new Client via Client WS");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.CLNWS_URL), config.getProperty(PropertyMap.CLNWS_USER), config.getProperty(PropertyMap.CLNWS_PASSWORD), config.getProperties());
		String clnAbbrev = resultProperties.getProperty("ClientId");
		
		logger.info("*** Step 1 Expected Results: - Verify that a new Client is available for using");
		assertNotNull(clnAbbrev, "        A new Client should be created.");
		
		logger.info("*** Step 2 Actions: - Update cln.FK_SUBM_FREQ_TYP_ID = 2 (Monthly) in DB");
		int clnId=clientDao.getClnByClnAbbrev(clnAbbrev.toUpperCase()).getClnId();
		int num = daoManagerPlatform.setValuesFromTableByColNameValue("CLN","FK_SUBM_FREQ_TYP_ID =2","PK_CLN_ID", String.valueOf(clnId),testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that the Client is updated");
		assertTrue(num > 0, "        Client " + clnAbbrev + " should be updated with cln.FK_SUBM_FREQ_TYP_ID = 2.");
		
		logger.info("*** Step 3 Actions: - Update SS 1 (Override Operating System Date) with date > 15");
		Date currDt = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getCurrentDate());
		String  newSS1Date = timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 2, 16);
		num = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE='"+newSS1Date+"'","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 3 Expected results: - Verify that SS#1 is updated.");
		assertTrue(num > 0, "        SS#1 should be updated to " + newSS1Date);
		
		logger.info("*** Step 4 Actions: - Add a new record in Cln_Chrgs table with FK_FREQ_TYP_ID = 2 (Monthly), EffDt <= SS#1.FirstDateOfMonth in DB");
		String freqTyp = "2";
		Date effDtInClnChrg = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 3, 16));
		int adjCd = adjustmentCodeDao.getAdjCdByTypIdOneOrTwo().getAdjCdId();
		int chrgs = Integer.parseInt(randomCharacter.getNonZeroRandomNumericString(2));
		
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_SEQ_NUM");
		insertFields.add("FK_FREQ_TYP_ID");
		insertFields.add("FK_ADJ_CD_ID");
		insertFields.add("EFF_DT");
		insertFields.add("EXP_DT");
		insertFields.add("CHRG");
		insertFields.add("CMNT");
		
		List <Object> insertValues=new ArrayList<>();
		insertValues.add(clnId);
		insertValues.add(1);
		insertValues.add(freqTyp);
		insertValues.add(adjCd);
		insertValues.add(effDtInClnChrg);
		insertValues.add("");
		insertValues.add(chrgs);
		insertValues.add("");
		num = daoManagerPlatform.addRecordIntoTable("CLN_CHRGS", insertFields, insertValues, testDb);
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added into Cln_Chrgs table");
		assertTrue(num > 0, "        A new record should be added into Cln_Chrgs table.");

		logger.info("*** Step 5 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 6 Actions: - Run PF-Client Recurring Charge Engine");
		Assert.assertTrue(clientDao.waitForClientRecurringChargesEngine(clnId, QUEUE_WAIT_TIME_MS));
		
		logger.info("*** Step 6 Expected Results: - Verify that the data in Cln_Chrgs table are updated properly");
		ClnChrgs clnChrgsInfoList = clientDao.getClnChrgsByClnId(clnId);
		String lastAppliedDt =formatter.format(clnChrgsInfoList.getLastAppliedDt());
		String audUser = clnChrgsInfoList.getAudUser();
		assertEquals(lastAppliedDt, newSS1Date, "        cln_chrgs.LAST_APPLIED_DT should be updated to " + newSS1Date);
		assertTrue(audUser.contains("ClnRecurringChargesEngine"),"        cln_chrgs.aud_users should be ClnRecurringChargesEngine.");		
				
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added into cln_bal_hist table");		
		String lastDtOfMonth = timeStamp.getLastDateOfMonth("MM/dd/yyyy", new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date));
		Date SS1DateinDate = new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date);
		Date midMothDt = new SimpleDateFormat("MM/dd/yyyy").parse(lastDtOfMonth);
		ClnBalHist clnBalHistInfoList=clientDao.getClnBalHistByClnId(clnId);
		assertNotNull(clnBalHistInfoList, "        A new record should be inserted into cln_bal_hist table.");
		assertEquals((int)clnBalHistInfoList.getExpctdDueAmt(), chrgs, "        cln_bal_hist.Expctd_Due_Amt = cln_chrgs.chrg.");
		
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added in cln_subm table");
		ClnSubm clnSubmInfoList=clientDao.getClnSubmByClnIdAndSubmDt(clnId,new java.sql.Date(midMothDt.getTime()));
		assertNotNull(clnSubmInfoList, "        A new record should be inserted into cln_subm table.");
		
		assertEquals((int)clnSubmInfoList.getDueAmt(), chrgs,"        cln_subm.due_amt = cln_chrgs.chrg.");
		assertTrue(clnSubmInfoList.getAudUser().contains("ClnRecurringChargesEngine"), "        cln_subm.Aud_User should be ClnRecurringChargesEngine.");
		assertEquals(clnSubmInfoList.getNumUnpostedPmt(), 0,"        cln_subm.NUM_UNPOSTED_PMT = 0.");
		
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added into cln_adj table");
		ClnAdj clnAdjInfoList = clientDao.getClnAdjByClnId(clnId);
		assertNotNull(clnAdjInfoList ,"        A new record should be added to Cln_Adj table.");
		
		assertTrue(clnAdjInfoList.getIsPrintable(),"        cln_adj.B_Printable = 1.");
		assertTrue(clnAdjInfoList.getIsPosted(),"        cln_adj.B_Posted = 1.");
		assertEquals(clnAdjInfoList.getAdjSeq(),1,"        cln_adj.Pk_Adj_Seq = 1.");
		assertEquals(clnAdjInfoList.getAdjCdId(),clnChrgsInfoList.getAdjCdId(),"        cln_adj.Fk_Adj_Cd_Id = Cln_Chrgs.fk_adj_cd_id.");
		assertEquals(clnAdjInfoList.getAdjAmtAsMoney(),clnChrgsInfoList.getChrgAsMoney(),"        cln_adj.Adj_Amt = Cln_Chrgs.Charge.");
		assertEquals(clnAdjInfoList.getNote(),clnChrgsInfoList.getCmnt(),"        cln_adj.Note = Cln_Chrgs.CMNT.");
		assertEquals(clnAdjInfoList.getUserId(),"ClnRecurringCharges","        cln_adj.Fk_User_Id = 'ClnRecurringCharges'.");
		assertEquals(clnAdjInfoList.getAdjDt(),SS1DateinDate,"        cln_adj.Adj_Dt = SS#1 (" + newSS1Date + ")");
		assertEquals(clnAdjInfoList.getPrntSubmDt(),midMothDt,"        cln_adj.FK_PRNT_SUBM_DT = " + lastDtOfMonth);
		
		logger.info("*** Step 7 Actions: - Clear/Reset test data in DB");
		daoManagerPlatform.deleteClnFromCLNByClnId(String.valueOf(clnId), testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = null","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 8 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();

	}
	
	
	@Test(priority = 1, description = "Freq_Typ_id=2(Monthly), Cln_Bal_hist has data")
	@Parameters({ "project", "testSuite", "testCase"})
	public void testPFER_460(String project, String testSuite, String testCase) throws Exception {
		logger.info("====== Testing - PFER_460 ======");		
		
		timeStamp=new TimeStamp(driver);
		randomCharacter=new RandomCharacter(driver);		
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		
		logger.info("*** Step 1 Actions: - Create a new Client via Client WS");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.CLNWS_URL), config.getProperty(PropertyMap.CLNWS_USER), config.getProperty(PropertyMap.CLNWS_PASSWORD), config.getProperties());
		String clnAbbrev = resultProperties.getProperty("ClientId");
		
		logger.info("*** Step 1 Expected Results: - Verify that a new Client is available for using");
		assertNotNull(clnAbbrev, "        A new Client should be created.");
		
		logger.info("*** Step 2 Actions: - Update cln.FK_SUBM_FREQ_TYP_ID = 2 (Monthly) in DB");
		int clnId=clientDao.getClnByClnAbbrev(clnAbbrev.toUpperCase()).getClnId();
		int num = daoManagerPlatform.setValuesFromTableByColNameValue("CLN","FK_SUBM_FREQ_TYP_ID =2","PK_CLN_ID", String.valueOf(clnId),testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that the Client is updated");
		assertTrue(num > 0, "        Client " + clnAbbrev + " should be updated with cln.FK_SUBM_FREQ_TYP_ID = 2.");
		
		logger.info("*** Step 3 Actions: - Update SS 1 (Override Operating System Date) with date > 15");
		Date currDt = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getCurrentDate());
		String  newSS1Date = timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 2, 16);
		num = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE='"+newSS1Date+"'","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 3 Expected results: - Verify that SS#1 is updated.");
		assertTrue(num > 0, "        SS#1 should be updated to " + newSS1Date);
		
		logger.info("*** Step 4 Actions: - Add a new record in Cln_Chrgs table with FK_FREQ_TYP_ID = 2 (Monthly) in DB");
		String freqTyp="2";		
		Date effDtInClnChrg = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 3, 16));
		int adjCd = adjustmentCodeDao.getAdjCdByTypIdOneOrTwo().getAdjCdId();
		int chrgs = Integer.parseInt(randomCharacter.getNonZeroRandomNumericString(2));
		
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_SEQ_NUM");
		insertFields.add("FK_FREQ_TYP_ID");
		insertFields.add("FK_ADJ_CD_ID");
		insertFields.add("EFF_DT");
		insertFields.add("EXP_DT");
		insertFields.add("CHRG");
		insertFields.add("CMNT");
		
		List <Object> insertValues=new ArrayList<>();
		insertValues.add(clnId);
		insertValues.add(1);
		insertValues.add(freqTyp);
		insertValues.add(adjCd);
		insertValues.add(effDtInClnChrg);
		insertValues.add("");
		insertValues.add(chrgs);
		insertValues.add("");
		num = daoManagerPlatform.addRecordIntoTable("CLN_CHRGS", insertFields, insertValues, testDb);
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added into Cln_Chrgs table");
		assertTrue(num > 0, "        A new record should be added into Cln_Chrgs table.");
		
		logger.info("*** Step 5 Actions: - Add a new record in Cln_Bal_Hist table in DB");
		Date effDtInClnBalHist= new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date);
		String lastDayOfMonth = timeStamp.getLastDateOfMonth("MM/dd/yyyy", new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date));
		Date submDt = new SimpleDateFormat("MM/dd/yyyy").parse(lastDayOfMonth);
		String expctdDueAmt = randomCharacter.getNonZeroRandomNumericString(2);
		
		insertFields.clear();//Remove all the elements from the list
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_EFF_DT");
		insertFields.add("PK_SUBM_DT");
		insertFields.add("EXPCTD_DUE_AMT");
		insertFields.add("RETRO_DUE_AMT");
		
		insertValues.clear();//Remove all the elements from the list
		insertValues.add(clnId);
		insertValues.add(effDtInClnBalHist); 
		insertValues.add(submDt);
		insertValues.add(expctdDueAmt);
		insertValues.add("0");
		num = daoManagerPlatform.addRecordIntoTable("CLN_BAL_HIST", insertFields, insertValues, testDb);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new record should be added in CLN_BAL_HIST table");
		assertTrue(num>0,"        A new record should be added into CLN_BAL_HIST table.");
		
		logger.info("*** Step 6 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 7 Actions: - Run PF-Client Recurring Charge Engine");
		Assert.assertTrue(clientDao.waitForClientRecurringChargesEngine(clnId, QUEUE_WAIT_TIME_MS));
		
		logger.info("*** Step 7 Expected Results: - Verify that the data in Cln_Chrgs table are updated properly");
		ClnChrgs clnChrgsInfoList = clientDao.getClnChrgsByClnId(clnId);
		String lastAppliedDt =formatter.format(clnChrgsInfoList.getLastAppliedDt());
		String audUser = clnChrgsInfoList.getAudUser();
		assertEquals(lastAppliedDt, newSS1Date, "        cln_chrgs.LAST_APPLIED_DT should be updated to " + newSS1Date);
		assertTrue(audUser.contains("ClnRecurringChargesEngine"),"        cln_chrgs.aud_users should be ClnRecurringChargesEngine.");
		
		logger.info("*** Step 7 Expected Results: - Verify that the record is updated in cln_bal_hist table properly");
		Date SS1DateinDate = new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date);
		Date midMothDt = new SimpleDateFormat("MM/dd/yyyy").parse(lastDayOfMonth);
		ClnBalHist clnBalHistInfoList=clientDao.getClnBalHistByClnId(clnId);
		int updatedExpctdDueAmt = chrgs + Integer.parseInt(expctdDueAmt);		
		assertEquals((int)clnBalHistInfoList.getExpctdDueAmt(), updatedExpctdDueAmt, "        New Expctd_Due_Amt = cln_bal_hist.Expctd_Due_Amt + cln_chrgs.chrg.");
				
		logger.info("*** Step 7 Expected Results: - Verify that a new record is added in cln_subm table properly");
		ClnSubm clnSubmInfoList=clientDao.getClnSubmByClnIdAndSubmDt(clnId,new java.sql.Date(midMothDt.getTime()));
		assertEquals((int)clnSubmInfoList.getDueAmt(), chrgs,"        cln_subm.due_amt = cln_chrgs.chrg.");
		assertTrue(clnSubmInfoList.getAudUser().contains("ClnRecurringChargesEngine"), "        cln_subm.Aud_User should be ClnRecurringChargesEngine.");
		assertEquals(clnSubmInfoList.getNumUnpostedPmt(), 0,"        cln_subm.NUM_UNPOSTED_PMT = 0.");
		
		logger.info("*** Step 7 Expected Results: - Verify that a new record is added into cln_adj table properly");
		ClnAdj clnAdjInfoList = clientDao.getClnAdjByClnId(clnId);
		assertNotNull(clnAdjInfoList ,"        A new record should be added to Cln_Adj table.");
		
		assertTrue(clnAdjInfoList.getIsPrintable(),"        cln_adj.B_Printable = 1.");
		assertTrue(clnAdjInfoList.getIsPosted(),"        cln_adj.B_Posted = 1.");
		assertEquals(clnAdjInfoList.getAdjSeq(),1,"        cln_adj.Pk_Adj_Seq = 1.");
		assertEquals(clnAdjInfoList.getAdjCdId(),clnChrgsInfoList.getAdjCdId(),"        cln_adj.Fk_Adj_Cd_Id = Cln_Chrgs.fk_adj_cd_id.");
		assertEquals(clnAdjInfoList.getAdjAmtAsMoney(),clnChrgsInfoList.getChrgAsMoney(),"        cln_adj.Adj_Amt = Cln_Chrgs.Charge.");
		assertEquals(clnAdjInfoList.getNote(),clnChrgsInfoList.getCmnt(),"        cln_adj.Note = Cln_Chrgs.CMNT.");
		assertEquals(clnAdjInfoList.getUserId(),"ClnRecurringCharges","        cln_adj.Fk_User_Id = 'ClnRecurringCharges'.");
		assertEquals(clnAdjInfoList.getAdjDt(),SS1DateinDate,"        cln_adj.Adj_Dt = SS#1 (" + newSS1Date + ")");
		
		logger.info("*** Step 8 Actions: - Clear/Reset test data in DB");
		daoManagerPlatform.deleteClnFromCLNByClnId(String.valueOf(clnId), testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = null","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 9 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();

	}
	
	@Test(priority = 1, description = "Freq_Typ_id=2(Monthly), Cln_subm.pk_subm_dt!=SS#1")
	@Parameters({"project", "testSuite", "testCase"})
	public void testPFER_461(String project, String testSuite, String testCase) throws Exception {
		logger.info("====== Testing - PFER_461 ======");		
		
		timeStamp=new TimeStamp(driver);
		randomCharacter=new RandomCharacter(driver);		
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		
		logger.info("*** Step 1 Actions: - Create a new Client via Client WS");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.CLNWS_URL), config.getProperty(PropertyMap.CLNWS_USER), config.getProperty(PropertyMap.CLNWS_PASSWORD), config.getProperties());
		String clnAbbrev = resultProperties.getProperty("ClientId");
		
		logger.info("*** Step 1 Expected Results: - Verify that a new Client is available for using");
		assertNotNull(clnAbbrev, "        A new Client should be created.");
		
		logger.info("*** Step 2 Actions: - Update cln.FK_SUBM_FREQ_TYP_ID = 2 (Monthly) in DB");
		int clnId=clientDao.getClnByClnAbbrev(clnAbbrev.toUpperCase()).getClnId();
		int num = daoManagerPlatform.setValuesFromTableByColNameValue("CLN","FK_SUBM_FREQ_TYP_ID =2","PK_CLN_ID", String.valueOf(clnId),testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that the Client is updated");
		assertTrue(num > 0, "        Client " + clnAbbrev + " should be updated with cln.FK_SUBM_FREQ_TYP_ID = 2.");
		
		logger.info("*** Step 3 Actions: - Update SS 1 (Override Operating System Date) with date > 15");
		Date currDt = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getCurrentDate());
		String  newSS1Date = timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 2, 16);
		num = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE='"+newSS1Date+"'","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 3 Expected results: - Verify that SS#1 is updated.");
		assertTrue(num > 0, "        SS#1 should be updated to " + newSS1Date);
		
		logger.info("*** Step 4 Actions: - Add a new record in Cln_Chrgs table with FK_FREQ_TYP_ID = 2 (Monthly), EffDt <= SS#1.FirstDateOfMonth in DB");
		String freqTyp="2";
		Date effDtInClnChrg = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 3, 16));
		int adjCd = adjustmentCodeDao.getAdjCdByTypIdOneOrTwo().getAdjCdId();
		int chrgs = Integer.parseInt(randomCharacter.getNonZeroRandomNumericString(2));
		
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_SEQ_NUM");
		insertFields.add("FK_FREQ_TYP_ID");
		insertFields.add("FK_ADJ_CD_ID");
		insertFields.add("EFF_DT");
		insertFields.add("EXP_DT");
		insertFields.add("CHRG");
		insertFields.add("CMNT");
		
		List <Object> insertValues=new ArrayList<>();
		insertValues.add(clnId);
		insertValues.add(1);
		insertValues.add(freqTyp);
		insertValues.add(adjCd);
		insertValues.add(effDtInClnChrg);
		insertValues.add("");
		insertValues.add(chrgs);
		insertValues.add("");
		num = daoManagerPlatform.addRecordIntoTable("CLN_CHRGS", insertFields, insertValues, testDb);
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added into Cln_Chrgs table");
		assertTrue(num > 0, "        A new record should be added into Cln_Chrgs table.");		
		
		logger.info("*** Step 5 Actions: - Add a new record in Cln_Subm table with cln_subm.pk_subm_dt != SS#1.EndOfMonth in DB");
		String dueAmtInClnSubm = randomCharacter.getNonZeroRandomNumericString(2);
		String firstDateOfMonth = timeStamp.getFirstDateOfMonth("MM/dd/yyyy", new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date));
		Date effDtInClnSubm = new SimpleDateFormat("MM/dd/yyyy").parse(firstDateOfMonth);
		
		insertFields.clear();//Remove all the elements from the list
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_SUBM_DT");
		insertFields.add("DUE_AMT");
		insertFields.add("ORIG_AMT");
		insertFields.add("PER_DIEM_COUNT");
		insertFields.add("PER_DIEM_AMT");
		
		insertValues.clear();//Remove all the elements from the list
		insertValues.add(clnId);
		insertValues.add(effDtInClnSubm);
		insertValues.add(dueAmtInClnSubm);
		insertValues.add(dueAmtInClnSubm);
		insertValues.add("0");
		insertValues.add("0");
		num = daoManagerPlatform.addRecordIntoTable("CLN_SUBM", insertFields, insertValues, testDb);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new record should be added in CLN_SUBM table");
		assertTrue(num>0,"        A new record should be added into CLN_SUBM table.");		

		logger.info("*** Step 6 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 7 Actions: - Run PF-Client Recurring Charge Engine");
		Assert.assertTrue(clientDao.waitForClientRecurringChargesEngine(clnId, QUEUE_WAIT_TIME_MS));
		
		logger.info("*** Step 7 Expected Results: - Verify that the data in Cln_Chrgs table are updated properly");
		ClnChrgs clnChrgsInfoList = clientDao.getClnChrgsByClnId(clnId);
		String lastAppliedDt =formatter.format(clnChrgsInfoList.getLastAppliedDt());
		String audUser = clnChrgsInfoList.getAudUser();
		assertEquals(lastAppliedDt, newSS1Date, "        cln_chrgs.LAST_APPLIED_DT should be updated to " + newSS1Date);
		assertTrue(audUser.contains("ClnRecurringChargesEngine"),"        cln_chrgs.aud_users should be ClnRecurringChargesEngine.");		
				
		logger.info("*** Step 7 Expected Results: - Verify that a new record is added into cln_bal_hist table");		
		String lastDtOfMonth = timeStamp.getLastDateOfMonth("MM/dd/yyyy", new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date));
		Date SS1DateinDate = new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date);
		Date midMothDt = new SimpleDateFormat("MM/dd/yyyy").parse(lastDtOfMonth);
		ClnBalHist clnBalHistInfoList=clientDao.getClnBalHistByClnId(clnId);
		assertNotNull(clnBalHistInfoList, "        A new record should be inserted into cln_bal_hist table.");
		assertEquals((int)clnBalHistInfoList.getExpctdDueAmt(), chrgs, "        cln_bal_hist.Expctd_Due_Amt = cln_chrgs.chrg.");
		
		logger.info("*** Step 7 Expected Results: - Verify that a new record is added in cln_subm table");
		ClnSubm clnSubmInfoList=clientDao.getClnSubmByClnIdAndSubmDt(clnId,new java.sql.Date(midMothDt.getTime()));
		assertNotNull(clnSubmInfoList, "        A new record should be inserted into cln_subm table.");
		
		assertEquals((int)clnSubmInfoList.getDueAmt(), chrgs,"        cln_subm.due_amt = cln_chrgs.chrg.");
		assertTrue(clnSubmInfoList.getAudUser().contains("ClnRecurringChargesEngine"), "        cln_subm.Aud_User should be ClnRecurringChargesEngine.");
		assertEquals(clnSubmInfoList.getNumUnpostedPmt(), 0,"        cln_subm.NUM_UNPOSTED_PMT = 0.");
		
		logger.info("*** Step 7 Expected Results: - Verify that a new record is added into cln_adj table");
		ClnAdj clnAdjInfoList = clientDao.getClnAdjByClnId(clnId);
		assertNotNull(clnAdjInfoList ,"        A new record should be added to Cln_Adj table.");

		assertTrue(clnAdjInfoList.getIsPrintable(),"        cln_adj.B_Printable = 1.");
		assertTrue(clnAdjInfoList.getIsPosted(),"        cln_adj.B_Posted = 1.");
		assertEquals(clnAdjInfoList.getAdjSeq(),1,"        cln_adj.Pk_Adj_Seq = 1.");
		assertEquals(clnAdjInfoList.getAdjCdId(),clnChrgsInfoList.getAdjCdId(),"        cln_adj.Fk_Adj_Cd_Id = Cln_Chrgs.fk_adj_cd_id.");
		assertEquals(clnAdjInfoList.getAdjAmtAsMoney(),clnChrgsInfoList.getChrgAsMoney(),"        cln_adj.Adj_Amt = Cln_Chrgs.Charge.");
		assertEquals(clnAdjInfoList.getNote(),clnChrgsInfoList.getCmnt(),"        cln_adj.Note = Cln_Chrgs.CMNT.");
		assertEquals(clnAdjInfoList.getUserId(),"ClnRecurringCharges","        cln_adj.Fk_User_Id = 'ClnRecurringCharges'.");
		assertEquals(clnAdjInfoList.getAdjDt(),SS1DateinDate,"        cln_adj.Adj_Dt = SS#1 (" + newSS1Date + ")");
		assertEquals(clnAdjInfoList.getPrntSubmDt(),midMothDt,"        cln_adj.FK_PRNT_SUBM_DT = " + lastDtOfMonth);
		
		logger.info("*** Step 8 Actions: - Clear/Reset test data in DB");
		daoManagerPlatform.deleteClnFromCLNByClnId(String.valueOf(clnId), testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = null","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 9 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();

	}
	
	@Test(priority = 1, description = "Freq_typ_id=3(Weekly), SS#1.dayOfMonth<=22")
	@Parameters({"project", "testSuite", "testCase"})
	public void testPFER_462(String project, String testSuite, String testCase) throws Exception {
		logger.info("====== Testing - PFER_462 ======");		
		
		timeStamp=new TimeStamp(driver);
		randomCharacter=new RandomCharacter(driver);		
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		
		logger.info("*** Step 1 Actions: - Create a new Client via Client WS");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.CLNWS_URL), config.getProperty(PropertyMap.CLNWS_USER), config.getProperty(PropertyMap.CLNWS_PASSWORD), config.getProperties());
		String clnAbbrev = resultProperties.getProperty("ClientId");

		logger.info("*** Step 1 Expected Results: - Verify that a new Client is available for using");
		assertNotNull(clnAbbrev, "        A new Client should be created.");
		
		logger.info("*** Step 2 Actions: - Update cln.FK_SUBM_FREQ_TYP_ID = 3 (Weekly) in DB");
		int clnId=clientDao.getClnByClnAbbrev(clnAbbrev.toUpperCase()).getClnId();
		int num = daoManagerPlatform.setValuesFromTableByColNameValue("CLN","FK_SUBM_FREQ_TYP_ID =3","PK_CLN_ID", String.valueOf(clnId),testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that the Client is updated");
		assertTrue(num > 0, "        Client " + clnAbbrev + " should be updated with cln.FK_SUBM_FREQ_TYP_ID = 3.");
		
		logger.info("*** Step 3 Actions: - Update SS 1 (Override Operating System Date) with date < 22");
		Date currDt = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getCurrentDate());
		String  newSS1Date = timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 2, 22);
		num = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE='"+newSS1Date+"'","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 3 Expected results: - Verify that SS#1 is updated.");
		assertTrue(num > 0, "        SS#1 should be updated to " + newSS1Date);
		
		logger.info("*** Step 4 Actions: - Add a new record in Cln_Chrgs table with FK_FREQ_TYP_ID = 3 (Weekly), EffDt <= SS#1.FirstDateOfMonth in DB");
		String freqTyp = "3";
		Date effDtInClnChrg = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 3, 22));
		Date expDtInClnChrg = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, -5, 22));
		int adjCd = adjustmentCodeDao.getAdjCdByTypIdOneOrTwo().getAdjCdId();
		int chrgs = Integer.parseInt(randomCharacter.getNonZeroRandomNumericString(2));
		
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_SEQ_NUM");
		insertFields.add("FK_FREQ_TYP_ID");
		insertFields.add("FK_ADJ_CD_ID");
		insertFields.add("EFF_DT");
		insertFields.add("EXP_DT");
		insertFields.add("CHRG");
		insertFields.add("CMNT");
		
		List <Object> insertValues= new ArrayList<>();
		insertValues.add(clnId);
		insertValues.add(1);
		insertValues.add(freqTyp);
		insertValues.add(adjCd);
		insertValues.add(effDtInClnChrg);
		insertValues.add(expDtInClnChrg);
		insertValues.add(chrgs);
		insertValues.add("");
		num = daoManagerPlatform.addRecordIntoTable("CLN_CHRGS", insertFields, insertValues, testDb);
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added into Cln_Chrgs table");
		assertTrue(num > 0, "        A new record should be added into Cln_Chrgs table.");

		logger.info("*** Step 5 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 6 Actions: - Run PF-Client Recurring Charge Engine");
		Assert.assertTrue(clientDao.waitForClientRecurringChargesEngine(clnId, QUEUE_WAIT_TIME_MS));
		
		logger.info("*** Step 6 Expected Results: - Verify that the data in Cln_Chrgs table are updated properly");
		ClnChrgs clnChrgsInfoList = clientDao.getClnChrgsByClnId(clnId);
		String lastAppliedDt =formatter.format(clnChrgsInfoList.getLastAppliedDt());
		String audUser = clnChrgsInfoList.getAudUser();
		assertEquals(lastAppliedDt, newSS1Date, "        cln_chrgs.LAST_APPLIED_DT should be updated to " + newSS1Date);
		assertTrue(audUser.contains("ClnRecurringChargesEngine"),"        cln_chrgs.aud_users should be ClnRecurringChargesEngine.");		
				
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added into cln_bal_hist table");			
		String dt22OfMonth = timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 2, 22);
		Date SS1DateinDate = new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date);
		Date midMothDt = new SimpleDateFormat("MM/dd/yyyy").parse(dt22OfMonth);
		ClnBalHist clnBalHistInfoList=clientDao.getClnBalHistByClnId(clnId);
		assertNotNull(clnBalHistInfoList, "        A new record should be inserted into cln_bal_hist table.");
		assertEquals((int)clnBalHistInfoList.getExpctdDueAmt(), chrgs, "        cln_bal_hist.Expctd_Due_Amt = cln_chrgs.chrg.");
		
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added in cln_subm table");
		ClnSubm clnSubmInfoList=clientDao.getClnSubmByClnIdAndSubmDt(clnId,new java.sql.Date(midMothDt.getTime()));
		assertNotNull(clnSubmInfoList, "        A new record should be inserted into cln_subm table.");

		assertEquals((int)clnSubmInfoList.getDueAmt(), chrgs,"        cln_subm.due_amt = cln_chrgs.chrg.");
		assertTrue(clnSubmInfoList.getAudUser().contains("ClnRecurringChargesEngine"), "        cln_subm.Aud_User should be ClnRecurringChargesEngine.");
		assertEquals(clnSubmInfoList.getNumUnpostedPmt(), 0,"        cln_subm.NUM_UNPOSTED_PMT = 0.");
		
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added into cln_adj table");
		ClnAdj clnAdjInfoList = clientDao.getClnAdjByClnId(clnId);
		assertNotNull(clnAdjInfoList ,"        A new record should be added to Cln_Adj table.");

		assertTrue(clnAdjInfoList.getIsPrintable(),"        cln_adj.B_Printable = 1.");
		assertTrue(clnAdjInfoList.getIsPosted(),"        cln_adj.B_Posted = 1.");
		assertEquals(clnAdjInfoList.getAdjSeq(),1,"        cln_adj.Pk_Adj_Seq = 1.");
		assertEquals(clnAdjInfoList.getAdjCdId(),clnChrgsInfoList.getAdjCdId(),"        cln_adj.Fk_Adj_Cd_Id = Cln_Chrgs.fk_adj_cd_id.");
		assertEquals(clnAdjInfoList.getAdjAmtAsMoney(),clnChrgsInfoList.getChrgAsMoney(),"        cln_adj.Adj_Amt = Cln_Chrgs.Charge.");
		assertEquals(clnAdjInfoList.getNote(),clnChrgsInfoList.getCmnt(),"        cln_adj.Note = Cln_Chrgs.CMNT.");
		assertEquals(clnAdjInfoList.getUserId(),"ClnRecurringCharges","        cln_adj.Fk_User_Id = 'ClnRecurringCharges'.");
		assertEquals(clnAdjInfoList.getAdjDt(),SS1DateinDate,"        cln_adj.Adj_Dt = SS#1 (" + newSS1Date + ")");
		assertEquals(clnAdjInfoList.getPrntSubmDt(), midMothDt,"        cln_adj.FK_PRNT_SUBM_DT = " + dt22OfMonth);
		
		logger.info("*** Step 7 Actions: - Clear/Reset test data in DB");
		daoManagerPlatform.deleteClnFromCLNByClnId(String.valueOf(clnId), testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = null","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 8 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();

	}
	
	@Test(priority = 1, description = "Freq_typ_id=3(Weekly), SS#1.dayOfMonth<=15")
	@Parameters({"project", "testSuite", "testCase"})
	public void testPFER_463(String project, String testSuite, String testCase) throws Exception {
		logger.info("====== Testing - PFER_463 ======");		
		
		timeStamp=new TimeStamp(driver);
		randomCharacter=new RandomCharacter(driver);		
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		
		logger.info("*** Step 1 Actions: - Create a new Client via Client WS");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.CLNWS_URL), config.getProperty(PropertyMap.CLNWS_USER), config.getProperty(PropertyMap.CLNWS_PASSWORD), config.getProperties());
		String clnAbbrev = resultProperties.getProperty("ClientId");

		logger.info("*** Step 1 Expected Results: - Verify that a new Client is available for using");
		assertNotNull(clnAbbrev, "        A new Client should be created.");
		
		logger.info("*** Step 2 Actions: - Update cln.FK_SUBM_FREQ_TYP_ID = 3 (Weekly) in DB");
		int clnId=clientDao.getClnByClnAbbrev(clnAbbrev.toUpperCase()).getClnId();
		int num = daoManagerPlatform.setValuesFromTableByColNameValue("CLN","FK_SUBM_FREQ_TYP_ID =3","PK_CLN_ID", String.valueOf(clnId),testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that the Client is updated");
		assertTrue(num > 0, "        Client " + clnAbbrev + " should be updated with cln.FK_SUBM_FREQ_TYP_ID = 3.");
		
		logger.info("*** Step 3 Actions: - Update SS 1 (Override Operating System Date) with date < 15");
		Date currDt = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getCurrentDate());
		String  newSS1Date = timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 2, 14);
		num = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE='"+newSS1Date+"'","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 3 Expected results: - Verify that SS#1 is updated.");
		assertTrue(num > 0, "        SS#1 should be updated to " + newSS1Date);
		
		logger.info("*** Step 4 Actions: - Add a new record in Cln_Chrgs table with FK_FREQ_TYP_ID = 3 (Weekly), EffDt <= SS#1.FirstDateOfMonth in DB");
		String freqTyp = "3";
		Date effDtInClnChrg = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 3, 14));
		Date expDtInClnChrg = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, -5, 14));
		int adjCd = adjustmentCodeDao.getAdjCdByTypIdOneOrTwo().getAdjCdId();
		int chrgs = Integer.parseInt(randomCharacter.getNonZeroRandomNumericString(2));
		
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_SEQ_NUM");
		insertFields.add("FK_FREQ_TYP_ID");
		insertFields.add("FK_ADJ_CD_ID");
		insertFields.add("EFF_DT");
		insertFields.add("EXP_DT");
		insertFields.add("CHRG");
		insertFields.add("CMNT");
		
		List <Object> insertValues= new ArrayList<>();
		insertValues.add(clnId);
		insertValues.add(1);
		insertValues.add(freqTyp);
		insertValues.add(adjCd);
		insertValues.add(effDtInClnChrg);
		insertValues.add(expDtInClnChrg);
		insertValues.add(chrgs);
		insertValues.add("");
		num = daoManagerPlatform.addRecordIntoTable("CLN_CHRGS", insertFields, insertValues, testDb);
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added into Cln_Chrgs table");
		assertTrue(num > 0, "        A new record should be added into Cln_Chrgs table.");

		logger.info("*** Step 5 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 6 Actions: - Run PF-Client Recurring Charge Engine");
		Assert.assertTrue(clientDao.waitForClientRecurringChargesEngine(clnId, QUEUE_WAIT_TIME_MS));
		
		logger.info("*** Step 6 Expected Results: - Verify that the data in Cln_Chrgs table are updated properly");
		ClnChrgs clnChrgsInfoList = clientDao.getClnChrgsByClnId(clnId);
		String lastAppliedDt =formatter.format(clnChrgsInfoList.getLastAppliedDt());
		String audUser = clnChrgsInfoList.getAudUser();
		assertEquals(lastAppliedDt, newSS1Date, "        cln_chrgs.LAST_APPLIED_DT should be updated to " + newSS1Date);
		assertTrue(audUser.contains("ClnRecurringChargesEngine"),"        cln_chrgs.aud_users should be ClnRecurringChargesEngine.");		
				
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added into cln_bal_hist table");			
		String dt15OfMonth = timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 2, 15);
		Date SS1DateinDate = new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date);
		Date midMothDt = new SimpleDateFormat("MM/dd/yyyy").parse(dt15OfMonth);
		ClnBalHist clnBalHistInfoList=clientDao.getClnBalHistByClnId(clnId);
		assertNotNull(clnBalHistInfoList, "        A new record should be inserted into cln_bal_hist table.");
		assertEquals((int)clnBalHistInfoList.getExpctdDueAmt(), chrgs, "        cln_bal_hist.Expctd_Due_Amt = cln_chrgs.chrg.");
		
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added in cln_subm table");
		ClnSubm clnSubmInfoList=clientDao.getClnSubmByClnIdAndSubmDt(clnId,new java.sql.Date(midMothDt.getTime()));
		assertNotNull(clnSubmInfoList, "        A new record should be inserted into cln_subm table.");

		assertEquals((int)clnSubmInfoList.getDueAmt(), chrgs,"        cln_subm.due_amt = cln_chrgs.chrg.");
		assertTrue(clnSubmInfoList.getAudUser().contains("ClnRecurringChargesEngine"), "        cln_subm.Aud_User should be ClnRecurringChargesEngine.");
		assertEquals(clnSubmInfoList.getNumUnpostedPmt(), 0,"        cln_subm.NUM_UNPOSTED_PMT = 0.");
		
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added into cln_adj table");
		ClnAdj clnAdjInfoList = clientDao.getClnAdjByClnId(clnId);
		assertNotNull(clnAdjInfoList ,"        A new record should be added to Cln_Adj table.");

		assertTrue(clnAdjInfoList.getIsPrintable(),"        cln_adj.B_Printable = 1.");
		assertTrue(clnAdjInfoList.getIsPosted(),"        cln_adj.B_Posted = 1.");
		assertEquals(clnAdjInfoList.getAdjSeq(),1,"        cln_adj.Pk_Adj_Seq = 1.");
		assertEquals(clnAdjInfoList.getAdjCdId(),clnChrgsInfoList.getAdjCdId(),"        cln_adj.Fk_Adj_Cd_Id = Cln_Chrgs.fk_adj_cd_id.");
		assertEquals(clnAdjInfoList.getAdjAmtAsMoney(),clnChrgsInfoList.getChrgAsMoney(),"        cln_adj.Adj_Amt = Cln_Chrgs.Charge.");
		assertEquals(clnAdjInfoList.getNote(),clnChrgsInfoList.getCmnt(),"        cln_adj.Note = Cln_Chrgs.CMNT.");
		assertEquals(clnAdjInfoList.getUserId(),"ClnRecurringCharges","        cln_adj.Fk_User_Id = 'ClnRecurringCharges'.");
		assertEquals(clnAdjInfoList.getAdjDt(),SS1DateinDate,"        cln_adj.Adj_Dt = SS#1 (" + newSS1Date + ")");
		assertEquals(clnAdjInfoList.getPrntSubmDt(), midMothDt,"        cln_adj.FK_PRNT_SUBM_DT = " + dt15OfMonth);
		
		logger.info("*** Step 7 Actions: - Clear/Reset test data in DB");
		daoManagerPlatform.deleteClnFromCLNByClnId(String.valueOf(clnId), testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = null","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 8 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();

	}
	
	@Test(priority = 1, description = "Freq_typ_id=3(Weekly), SS#1.dayOfMonth<=7")
	@Parameters({"project", "testSuite", "testCase"})
	public void testPFER_464(String project, String testSuite, String testCase) throws Exception {
		logger.info("====== Testing - PFER_464 ======");		
		
		timeStamp=new TimeStamp(driver);
		randomCharacter=new RandomCharacter(driver);		
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		
		logger.info("*** Step 1 Actions: - Create a new Client via Client WS");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.CLNWS_URL), config.getProperty(PropertyMap.CLNWS_USER), config.getProperty(PropertyMap.CLNWS_PASSWORD), config.getProperties());
		String clnAbbrev = resultProperties.getProperty("ClientId");

		logger.info("*** Step 1 Expected Results: - Verify that a new Client is available for using");
		assertNotNull(clnAbbrev, "        A new Client should be created.");
		
		logger.info("*** Step 2 Actions: - Update cln.FK_SUBM_FREQ_TYP_ID = 3 (Weekly) in DB");
		int clnId=clientDao.getClnByClnAbbrev(clnAbbrev.toUpperCase()).getClnId();
		int num = daoManagerPlatform.setValuesFromTableByColNameValue("CLN","FK_SUBM_FREQ_TYP_ID =3","PK_CLN_ID", String.valueOf(clnId),testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that the Client is updated");
		assertTrue(num > 0, "        Client " + clnAbbrev + " should be updated with cln.FK_SUBM_FREQ_TYP_ID = 3.");
		
		logger.info("*** Step 3 Actions: - Update SS 1 (Override Operating System Date) with date < 7");
		Date currDt = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getCurrentDate());
		String  newSS1Date = timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 1, 5);
		num = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE='"+newSS1Date+"'","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 3 Expected results: - Verify that SS#1 is updated.");
		assertTrue(num > 0, "        SS#1 should be updated to " + newSS1Date);
		
		logger.info("*** Step 4 Actions: - Add a new record in Cln_Chrgs table with FK_FREQ_TYP_ID = 3 (Weekly), EffDt <= SS#1.FirstDateOfMonth in DB");
		String freqTyp = "3";
		Date effDtInClnChrg = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 1, 1));
		Date expDtInClnChrg = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 1, 16));
		int adjCd = adjustmentCodeDao.getAdjCdByTypIdOneOrTwo().getAdjCdId();
		int chrgs = Integer.parseInt(randomCharacter.getNonZeroRandomNumericString(2));
		
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_SEQ_NUM");
		insertFields.add("FK_FREQ_TYP_ID");
		insertFields.add("FK_ADJ_CD_ID");
		insertFields.add("EFF_DT");
		insertFields.add("EXP_DT");
		insertFields.add("CHRG");
		insertFields.add("CMNT");
		
		List <Object> insertValues= new ArrayList<>();
		insertValues.add(clnId);
		insertValues.add(1);
		insertValues.add(freqTyp);
		insertValues.add(adjCd);
		insertValues.add(effDtInClnChrg);
		insertValues.add(expDtInClnChrg);
		insertValues.add(chrgs);
		insertValues.add("");
		num = daoManagerPlatform.addRecordIntoTable("CLN_CHRGS", insertFields, insertValues, testDb);
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added into Cln_Chrgs table");
		assertTrue(num > 0, "        A new record should be added into Cln_Chrgs table.");

		logger.info("*** Step 5 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 6 Actions: - Run PF-Client Recurring Charge Engine");
		Assert.assertTrue(clientDao.waitForClientRecurringChargesEngine(clnId, QUEUE_WAIT_TIME_MS));
		
		logger.info("*** Step 6 Expected Results: - Verify that the data in Cln_Chrgs table are updated properly");
		ClnChrgs clnChrgsInfoList = clientDao.getClnChrgsByClnId(clnId);
		String lastAppliedDt =formatter.format(clnChrgsInfoList.getLastAppliedDt());
		String audUser = clnChrgsInfoList.getAudUser();
		assertEquals(lastAppliedDt, newSS1Date, "        cln_chrgs.LAST_APPLIED_DT should be updated to " + newSS1Date);
		assertTrue(audUser.contains("ClnRecurringChargesEngine"),"        cln_chrgs.aud_users should be ClnRecurringChargesEngine.");		
				
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added into cln_bal_hist table");			
		String dt7OfMonth = timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 1, 7);
		Date SS1DateinDate = new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date);
		Date midMothDt = new SimpleDateFormat("MM/dd/yyyy").parse(dt7OfMonth);
		ClnBalHist clnBalHistInfoList=clientDao.getClnBalHistByClnId(clnId);
		assertNotNull(clnBalHistInfoList, "        A new record should be inserted into cln_bal_hist table.");
		assertEquals((int)clnBalHistInfoList.getExpctdDueAmt(), chrgs, "        cln_bal_hist.Expctd_Due_Amt = cln_chrgs.chrg.");
		
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added in cln_subm table");
		ClnSubm clnSubmInfoList=clientDao.getClnSubmByClnIdAndSubmDt(clnId,new java.sql.Date(midMothDt.getTime()));
		assertNotNull(clnSubmInfoList, "        A new record should be inserted into cln_subm table.");

		assertEquals((int)clnSubmInfoList.getDueAmt(), chrgs,"        cln_subm.due_amt = cln_chrgs.chrg.");
		assertTrue(clnSubmInfoList.getAudUser().contains("ClnRecurringChargesEngine"), "        cln_subm.Aud_User should be ClnRecurringChargesEngine.");
		assertEquals(clnSubmInfoList.getNumUnpostedPmt(), 0,"        cln_subm.NUM_UNPOSTED_PMT = 0.");
		
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added into cln_adj table");
		ClnAdj clnAdjInfoList = clientDao.getClnAdjByClnId(clnId);
		assertNotNull(clnAdjInfoList ,"        A new record should be added to Cln_Adj table.");

		assertTrue(clnAdjInfoList.getIsPrintable(),"        cln_adj.B_Printable = 1.");
		assertTrue(clnAdjInfoList.getIsPosted(),"        cln_adj.B_Posted = 1.");
		assertEquals(clnAdjInfoList.getAdjSeq(),1,"        cln_adj.Pk_Adj_Seq = 1.");
		assertEquals(clnAdjInfoList.getAdjCdId(),clnChrgsInfoList.getAdjCdId(),"        cln_adj.Fk_Adj_Cd_Id = Cln_Chrgs.fk_adj_cd_id.");
		assertEquals(clnAdjInfoList.getAdjAmtAsMoney(),clnChrgsInfoList.getChrgAsMoney(),"        cln_adj.Adj_Amt = Cln_Chrgs.Charge.");
		assertEquals(clnAdjInfoList.getNote(),clnChrgsInfoList.getCmnt(),"        cln_adj.Note = Cln_Chrgs.CMNT.");
		assertEquals(clnAdjInfoList.getUserId(),"ClnRecurringCharges","        cln_adj.Fk_User_Id = 'ClnRecurringCharges'.");
		assertEquals(clnAdjInfoList.getAdjDt(),SS1DateinDate,"        cln_adj.Adj_Dt = SS#1 (" + newSS1Date + ")");
		assertEquals(clnAdjInfoList.getPrntSubmDt(), midMothDt,"        cln_adj.FK_PRNT_SUBM_DT = " + dt7OfMonth);
		
		logger.info("*** Step 7 Actions: - Clear/Reset test data in DB");
		daoManagerPlatform.deleteClnFromCLNByClnId(String.valueOf(clnId), testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = null","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 8 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();

	}
	
	@Test(priority = 1, description = "Freq_typ_id=3(Weekly), SS#1.dayOfMonth>22")
	@Parameters({"project", "testSuite", "testCase"})
	public void testPFER_465(String project, String testSuite, String testCase) throws Exception {
		logger.info("====== Testing - PFER_465 ======");		
		
		timeStamp=new TimeStamp(driver);
		randomCharacter=new RandomCharacter(driver);		
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		
		logger.info("*** Step 1 Actions: - Create a new Client via Client WS");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.CLNWS_URL), config.getProperty(PropertyMap.CLNWS_USER), config.getProperty(PropertyMap.CLNWS_PASSWORD), config.getProperties());
		String clnAbbrev = resultProperties.getProperty("ClientId");

		logger.info("*** Step 1 Expected Results: - Verify that a new Client is available for using");
		assertNotNull(clnAbbrev, "        A new Client should be created.");
		
		logger.info("*** Step 2 Actions: - Update cln.FK_SUBM_FREQ_TYP_ID = 3 (Weekly) in DB");
		int clnId=clientDao.getClnByClnAbbrev(clnAbbrev.toUpperCase()).getClnId();
		int num = daoManagerPlatform.setValuesFromTableByColNameValue("CLN","FK_SUBM_FREQ_TYP_ID =3","PK_CLN_ID", String.valueOf(clnId),testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that the Client is updated");
		assertTrue(num > 0, "        Client " + clnAbbrev + " should be updated with cln.FK_SUBM_FREQ_TYP_ID = 3.");
		
		logger.info("*** Step 3 Actions: - Update SS 1 (Override Operating System Date) with date = 23");
		Date currDt = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getCurrentDate());
		String  newSS1Date = timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 2, 23);
		num = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE='"+newSS1Date+"'","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 3 Expected results: - Verify that SS#1 is updated.");
		assertTrue(num > 0, "        SS#1 should be updated to " + newSS1Date);
		
		logger.info("*** Step 4 Actions: - Add a new record in Cln_Chrgs table with FK_FREQ_TYP_ID = 3 (Weekly), EffDt <= SS#1.Date23OfMonth in DB");
		String freqTyp = "3";
		Date effDtInClnChrg = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 2, 21));
		Date expDtInClnChrg = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 1, 16));
		int adjCd = adjustmentCodeDao.getAdjCdByTypIdOneOrTwo().getAdjCdId();
		int chrgs = Integer.parseInt(randomCharacter.getNonZeroRandomNumericString(2));
		
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_SEQ_NUM");
		insertFields.add("FK_FREQ_TYP_ID");
		insertFields.add("FK_ADJ_CD_ID");
		insertFields.add("EFF_DT");
		insertFields.add("EXP_DT");
		insertFields.add("CHRG");
		insertFields.add("CMNT");
		
		List <Object> insertValues= new ArrayList<>();
		insertValues.add(clnId);
		insertValues.add(1);
		insertValues.add(freqTyp);
		insertValues.add(adjCd);
		insertValues.add(effDtInClnChrg);
		insertValues.add(expDtInClnChrg);
		insertValues.add(chrgs);
		insertValues.add("");
		num = daoManagerPlatform.addRecordIntoTable("CLN_CHRGS", insertFields, insertValues, testDb);
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added into Cln_Chrgs table");
		assertTrue(num > 0, "        A new record should be added into Cln_Chrgs table.");

		logger.info("*** Step 5 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 6 Actions: - Run PF-Client Recurring Charge Engine");
		Assert.assertTrue(clientDao.waitForClientRecurringChargesEngine(clnId, QUEUE_WAIT_TIME_MS));
		
		logger.info("*** Step 6 Expected Results: - Verify that the data in Cln_Chrgs table are updated properly");
		ClnChrgs clnChrgsInfoList = clientDao.getClnChrgsByClnId(clnId);
		String lastAppliedDt =formatter.format(clnChrgsInfoList.getLastAppliedDt());
		String audUser = clnChrgsInfoList.getAudUser();
		assertEquals(lastAppliedDt, newSS1Date, "        cln_chrgs.LAST_APPLIED_DT should be updated to " + newSS1Date);
		assertTrue(audUser.contains("ClnRecurringChargesEngine"),"        cln_chrgs.aud_users should be ClnRecurringChargesEngine.");		
				
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added into cln_bal_hist table");			
		String lastDtOfMonth = timeStamp.getLastDateOfMonth("MM/dd/yyyy", new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date));
		Date SS1DateinDate = new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date);
		Date midMothDt = new SimpleDateFormat("MM/dd/yyyy").parse(lastDtOfMonth);
		ClnBalHist clnBalHistInfoList=clientDao.getClnBalHistByClnId(clnId);
		assertNotNull(clnBalHistInfoList, "        A new record should be inserted into cln_bal_hist table.");
		assertEquals((int)clnBalHistInfoList.getExpctdDueAmt(), chrgs, "        cln_bal_hist.Expctd_Due_Amt = cln_chrgs.chrg.");
		
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added in cln_subm table");
		ClnSubm clnSubmInfoList=clientDao.getClnSubmByClnIdAndSubmDt(clnId,new java.sql.Date(midMothDt.getTime()));
		assertNotNull(clnSubmInfoList, "        A new record should be inserted into cln_subm table.");

		assertEquals((int)clnSubmInfoList.getDueAmt(), chrgs,"        cln_subm.due_amt = cln_chrgs.chrg.");
		assertTrue(clnSubmInfoList.getAudUser().contains("ClnRecurringChargesEngine"), "        cln_subm.Aud_User should be ClnRecurringChargesEngine.");
		assertEquals(clnSubmInfoList.getNumUnpostedPmt(), 0,"        cln_subm.NUM_UNPOSTED_PMT = 0.");
		
		logger.info("*** Step 6 Expected Results: - Verify that a new record is added into cln_adj table");
		ClnAdj clnAdjInfoList = clientDao.getClnAdjByClnId(clnId);
		assertNotNull(clnAdjInfoList ,"        A new record should be added to Cln_Adj table.");

		assertTrue(clnAdjInfoList.getIsPrintable(),"        cln_adj.B_Printable = 1.");
		assertTrue(clnAdjInfoList.getIsPosted(),"        cln_adj.B_Posted = 1.");
		assertEquals(clnAdjInfoList.getAdjSeq(),1,"        cln_adj.Pk_Adj_Seq = 1.");
		assertEquals(clnAdjInfoList.getAdjCdId(),clnChrgsInfoList.getAdjCdId(),"        cln_adj.Fk_Adj_Cd_Id = Cln_Chrgs.fk_adj_cd_id.");
		assertEquals(clnAdjInfoList.getAdjAmtAsMoney(),clnChrgsInfoList.getChrgAsMoney(),"        cln_adj.Adj_Amt = Cln_Chrgs.Charge.");
		assertEquals(clnAdjInfoList.getNote(),clnChrgsInfoList.getCmnt(),"        cln_adj.Note = Cln_Chrgs.CMNT.");
		assertEquals(clnAdjInfoList.getUserId(),"ClnRecurringCharges","        cln_adj.Fk_User_Id = 'ClnRecurringCharges'.");
		assertEquals(clnAdjInfoList.getAdjDt(),SS1DateinDate,"        cln_adj.Adj_Dt = SS#1 (" + newSS1Date + ")");
		assertEquals(clnAdjInfoList.getPrntSubmDt(), midMothDt,"        cln_adj.FK_PRNT_SUBM_DT = " + lastDtOfMonth);
		
		logger.info("*** Step 7 Actions: - Clear/Reset test data in DB");
		daoManagerPlatform.deleteClnFromCLNByClnId(String.valueOf(clnId), testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = null","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 8 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();

	}
	
	@Test(priority = 1, description = "Freq_typ_id=3(Weekly), SS#1.dayOfMonth>22,ClnBalHist.Eff_dt<SS#1")
	@Parameters({"project", "testSuite", "testCase"})
	public void testPFER_466(String project, String testSuite, String testCase) throws Exception {
		logger.info("====== Testing - PFER_466 ======");		
		
		timeStamp=new TimeStamp(driver);
		randomCharacter=new RandomCharacter(driver);		
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		
		logger.info("*** Step 1 Actions: - Create a new Client via Client WS");
		Properties resultProperties =  TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.CLNWS_URL), config.getProperty(PropertyMap.CLNWS_USER), config.getProperty(PropertyMap.CLNWS_PASSWORD), config.getProperties());
		String clnAbbrev = resultProperties.getProperty("ClientId");

		logger.info("*** Step 1 Expected Results: - Verify that a new Client is available for using");
		assertNotNull(clnAbbrev, "        A new Client should be created.");
		
		logger.info("*** Step 2 Actions: - Update cln.FK_SUBM_FREQ_TYP_ID = 3 (Weekly) in DB");
		int clnId=clientDao.getClnByClnAbbrev(clnAbbrev.toUpperCase()).getClnId();
		int num = daoManagerPlatform.setValuesFromTableByColNameValue("CLN","FK_SUBM_FREQ_TYP_ID =3","PK_CLN_ID", String.valueOf(clnId),testDb);
		
		logger.info("*** Step 2 Expected Results: - Verify that the Client is updated");
		assertTrue(num > 0, "        Client " + clnAbbrev + " should be updated with cln.FK_SUBM_FREQ_TYP_ID = 3.");
		
		logger.info("*** Step 3 Actions: - Update SS 1 (Override Operating System Date) with date > 22");
		Date currDt = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getCurrentDate());
		String  newSS1Date = timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 3, 25);
		num = daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE='"+newSS1Date+"'","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 3 Expected results: - Verify that SS#1 is updated.");
		assertTrue(num > 0, "        SS#1 should be updated to " + newSS1Date);
		
		logger.info("*** Step 4 Actions: - Add a new record in Cln_Chrgs table with FK_FREQ_TYP_ID = 3 (Weekly), EffDt <= SS#1.Date23OfMonth in DB");
		String freqTyp = "3";
		Date effDtInClnChrg = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 3, 19));
		Date expDtInClnChrg = new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 1, 10));
		int adjCd = adjustmentCodeDao.getAdjCdByTypIdOneOrTwo().getAdjCdId();
		int chrgs = Integer.parseInt(randomCharacter.getNonZeroRandomNumericString(2));
		
		List<String> insertFields = new ArrayList<>();
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_SEQ_NUM");
		insertFields.add("FK_FREQ_TYP_ID");
		insertFields.add("FK_ADJ_CD_ID");
		insertFields.add("EFF_DT");
		insertFields.add("EXP_DT");
		insertFields.add("CHRG");
		insertFields.add("CMNT");
		
		List <Object> insertValues= new ArrayList<>();
		insertValues.add(clnId);
		insertValues.add(1);
		insertValues.add(freqTyp);
		insertValues.add(adjCd);
		insertValues.add(effDtInClnChrg);
		insertValues.add(expDtInClnChrg);
		insertValues.add(chrgs);
		insertValues.add("");
		num = daoManagerPlatform.addRecordIntoTable("CLN_CHRGS", insertFields, insertValues, testDb);
		
		logger.info("*** Step 4 Expected Results: - Verify that a new record is added into Cln_Chrgs table");
		assertTrue(num > 0, "        A new record should be added into Cln_Chrgs table.");
		
		logger.info("*** Step 5 Actions: - Add a new record in Cln_Bal_Hist table in DB");
		Date effDtInClnBalHist= new SimpleDateFormat("MM/dd/yyyy").parse(timeStamp.getDayOfPrevMonth("MM/dd/yyyy", currDt, 3, 23));//new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date);
		String lastDayOfMonth = timeStamp.getLastDateOfMonth("MM/dd/yyyy", new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date));
		Date submDt = new SimpleDateFormat("MM/dd/yyyy").parse(lastDayOfMonth);
		String expctdDueAmt = randomCharacter.getNonZeroRandomNumericString(2);
		
		insertFields.clear();//Remove all the elements from the list
		insertFields.add("PK_CLN_ID");
		insertFields.add("PK_EFF_DT");
		insertFields.add("PK_SUBM_DT");
		insertFields.add("EXPCTD_DUE_AMT");
		insertFields.add("RETRO_DUE_AMT");
		
		insertValues.clear();//Remove all the elements from the list
		insertValues.add(clnId);
		insertValues.add(effDtInClnBalHist); 
		insertValues.add(submDt);
		insertValues.add(expctdDueAmt);
		insertValues.add("0");
		num = daoManagerPlatform.addRecordIntoTable("CLN_BAL_HIST", insertFields, insertValues, testDb);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new record should be added in CLN_BAL_HIST table");
		assertTrue(num>0,"        A new record should be added into CLN_BAL_HIST table.");		

		logger.info("*** Step 6 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 7 Actions: - Run PF-Client Recurring Charge Engine");
		Assert.assertTrue(clientDao.waitForClientRecurringChargesEngine(clnId, QUEUE_WAIT_TIME_MS));
		
		logger.info("*** Step 7 Expected Results: - Verify that the data in Cln_Chrgs table are updated properly");
		ClnChrgs clnChrgsInfoList = clientDao.getClnChrgsByClnId(clnId);
		String lastAppliedDt =formatter.format(clnChrgsInfoList.getLastAppliedDt());
		String audUser = clnChrgsInfoList.getAudUser();
		assertEquals(lastAppliedDt, newSS1Date, "        cln_chrgs.LAST_APPLIED_DT should be updated to " + newSS1Date);
		assertTrue(audUser.contains("ClnRecurringChargesEngine"),"        cln_chrgs.aud_users should be ClnRecurringChargesEngine.");		
				
		logger.info("*** Step 7 Expected Results: - Verify that a new record is added into cln_bal_hist table");			
		String lastDtOfMonth = timeStamp.getLastDateOfMonth("MM/dd/yyyy", new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date));
		Date SS1DateinDate = new SimpleDateFormat("MM/dd/yyyy").parse(newSS1Date);
		Date midMothDt = new SimpleDateFormat("MM/dd/yyyy").parse(lastDtOfMonth);
		ClnBalHist clnBalHistInfoList=clientDao.getClnBalHistByClnId(clnId);
		assertNotNull(clnBalHistInfoList, "        A new record should be inserted into cln_bal_hist table.");

		int updatedExpctdDueAmt = chrgs + Integer.parseInt(expctdDueAmt);		
		assertEquals((int)clnBalHistInfoList.getExpctdDueAmt(), updatedExpctdDueAmt, "        New Expctd_Due_Amt = cln_bal_hist.Expctd_Due_Amt + cln_chrgs.chrg.");
		
		logger.info("*** Step 7 Expected Results: - Verify that a new record is added in cln_subm table");
		ClnSubm clnSubmInfoList=clientDao.getClnSubmByClnIdAndSubmDt(clnId,new java.sql.Date(midMothDt.getTime()));
		assertNotNull(clnSubmInfoList, "        A new record should be inserted into cln_subm table.");

		assertEquals((int)clnSubmInfoList.getDueAmt(), chrgs,"        cln_subm.due_amt = cln_chrgs.chrg.");
		assertTrue(clnSubmInfoList.getAudUser().contains("ClnRecurringChargesEngine"), "        cln_subm.Aud_User should be ClnRecurringChargesEngine.");
		assertEquals(clnSubmInfoList.getNumUnpostedPmt(), 0,"        cln_subm.NUM_UNPOSTED_PMT = 0.");
		
		logger.info("*** Step 7 Expected Results: - Verify that a new record is added into cln_adj table");
		ClnAdj clnAdjInfoList = clientDao.getClnAdjByClnId(clnId);
		assertNotNull(clnAdjInfoList ,"        A new record should be added to Cln_Adj table.");

		assertTrue(clnAdjInfoList.getIsPrintable(),"        cln_adj.B_Printable = 1.");
		assertTrue(clnAdjInfoList.getIsPosted(),"        cln_adj.B_Posted = 1.");
		assertEquals(clnAdjInfoList.getAdjSeq(),1,"        cln_adj.Pk_Adj_Seq = 1.");
		assertEquals(clnAdjInfoList.getAdjCdId(),clnChrgsInfoList.getAdjCdId(),"        cln_adj.Fk_Adj_Cd_Id = Cln_Chrgs.fk_adj_cd_id.");
		assertEquals(clnAdjInfoList.getAdjAmtAsMoney(),clnChrgsInfoList.getChrgAsMoney(),"        cln_adj.Adj_Amt = Cln_Chrgs.Charge.");
		assertEquals(clnAdjInfoList.getNote(),clnChrgsInfoList.getCmnt(),"        cln_adj.Note = Cln_Chrgs.CMNT.");
		assertEquals(clnAdjInfoList.getUserId(),"ClnRecurringCharges","        cln_adj.Fk_User_Id = 'ClnRecurringCharges'.");
		assertEquals(clnAdjInfoList.getAdjDt(),SS1DateinDate,"        cln_adj.Adj_Dt = SS#1 (" + newSS1Date + ")");
		assertEquals(clnAdjInfoList.getPrntSubmDt(), midMothDt,"        cln_adj.FK_PRNT_SUBM_DT = " + lastDtOfMonth);
		
		logger.info("*** Step 8 Actions: - Clear/Reset test data in DB");
		daoManagerPlatform.deleteClnFromCLNByClnId(String.valueOf(clnId), testDb);
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = null","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 9 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();

	}
	
	@Test(priority = 1, description = "Reset SS1")
	public void testPFER_472() throws Exception {
		logger.info("====== Testing - PFER_472 ======");		

		xifinAdminUtils = new XifinAdminUtils(driver, config);
		
		logger.info("*** Step 1 Actions: - Clear SS#1 data in DB");		
		daoManagerPlatform.setValuesFromTableByColNameValue("SYSTEM_SETTING","DATA_VALUE = null","PK_SETTING_ID","1",testDb);
		
		logger.info("*** Step 2 Actions: - Clear the System Data Cache");
		xifinAdminUtils.clearDataCache();

	}
	
}
