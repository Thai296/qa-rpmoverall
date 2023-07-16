package com.newXp.tests;

import com.mbasys.mars.ejb.entity.consDiag.ConsDiag;
import com.overall.menu.MenuNavigation;
import com.overall.payor.consolidation.consolidationRules.ConsolidationRules;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.ListUtil;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.lang.reflect.Method;
import java.util.List;
import static org.testng.Assert.*;

public class SmokeConsolidationRulesTest extends SeleniumBaseTest {
	private MenuNavigation navigation;
	private RandomCharacter randomCharacter;
	private ConsolidationRules consolidationRules;
	private ListUtil listUtil;
	private TimeStamp timeStamp;

	@BeforeMethod(alwaysRun = true)
	@Parameters({"ssoUsername", "ssoPassword"})
	public void beforeMethod(String ssoUsername, String ssoPassword, Method method)
	{
		try
		{
			logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
			logIntoSso(ssoUsername, ssoPassword);
		}
		catch (Exception e)
		{
			logger.error("Error running BeforeMethod", e);
		}
	}
	
	@Test(priority = 1, description = "Update Consolidation Rule with add new Payor Info")
	@Parameters({"description"})
	public void testXPR_528(String description) throws Exception {
		logger.info("===== Testing - testXPR_528 =====");  	

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Load Consolidation page title displays");
		consolidationRules = new ConsolidationRules(driver, wait);
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"),"        Page title should be 'Load Consolidation'");
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        Load Consolidation page is displayed");
		
		logger.info("*** Step 2 Action: - Load a new Consolidation ID");
		randomCharacter = new RandomCharacter(driver);
		String consID = randomCharacter.getRandomAlphaNumericString(10);
		consolidationRules.inputToConsolidationIDInput(consID);
		
		logger.info("*** Step 2 Expected: - Consolidation page is displayed");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation page is displayed");
		
		logger.info("*** Step 3 Actions: - Enter valid data for require fields and add Proc Code into Requirements, Links and Result tables");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5));
		consolidationRules.inputConsDesc(description);
		String procCodeID = daoManagerXifinRpm.getProcedureCodeId(null).get(0);
		consolidationRules.addProcCodeToLevelAndResult(procCodeID,procCodeID, "10", "1", "1", "1", "1", this);
		
		logger.info("*** Step 4 Action:- Click Save Button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5));
		consolidationRules.clickOnSaveBtn();
		waitUntilElementPresent(consolidationRules.consolidationIDInput(), 30);
		
		logger.info("*** Step 4 Expected result: - Verify that new Consolidation ID is saved into DB");
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        System take user return Load Consolidation page");
		assertNotNull(consolidationDao.getConsByConsAbbrev(consID),"        New ConsolidationId is added into Db");
		
		logger.info("*** Step 5 Action: - Load the ConsolidationID again");
		consolidationRules.inputToConsolidationIDInput(consID);
		
		logger.info("*** Step 5 Expected result: - Consolidation page is displayed");
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        Consolidation page is displayed");
		assertEquals(consolidationRules.consolidationRulesIdTextBox().getText().trim(), consID.toUpperCase(),"        Consolidation page is load with correct ID");
		
		logger.info("*** Step 6 Action: - Add new Payor Group");
		timeStamp = new TimeStamp(driver);
		String payorGrp = payorDao.getRandomPyrGrpFromPyrGrp().getGrpNm();
		String DOSEffDt = timeStamp.getCurrentDate("MM/dd/yyyy");		
		assertTrue(isElementPresent(consolidationRules.addPayorGroupBtn(), 5));		
		consolidationRules.addDataToPyrGrp(payorGrp, DOSEffDt, this);
		
		logger.info("*** Step 6 Expected Result: - New Payor Group is shown in table");
		assertTrue(isElementPresent(consolidationRules.payorGrpTbl(), 5));
		assertTrue(getColumnValue(consolidationRules.payorGrpTbl(), payorGrp),"        New Payor Group is shown in Payor Group table");
		
		logger.info("*** Step 7 Action: - Add Payor ID into Payor table");
		String payorId = payorDao.getPayorId().getPyrAbbrv();
		assertTrue(isElementPresent(consolidationRules.addPayorBtn(), 5));
		consolidationRules.addDataToPyr(payorId, DOSEffDt, this);
		
		logger.info("*** Step 7 Expected Result: - Verify that new PayorId is shown in Payor table");
		assertTrue(isElementPresent(consolidationRules.payortbl(), 5),"        Payor table is displayed");
		assertTrue(getColumnValue(consolidationRules.payortbl(), payorId),"        PayorId is shown in Payor table");
		
		logger.info("*** Step 8 Action: - Click Save Button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5));
		consolidationRules.clickOnSaveBtn();

		logger.info("*** Step 8 expected result: - Verify that the new Payor Group and Payor Id are saved");
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        System take user return Load Consolidation page");
		assertNotNull(payorDao.getPyrGrpByConsAbbrevAndGrpNm(consID, payorGrp),"        New Payor group is added for corresponding ConsolidationID");
		assertTrue(daoManagerXifinRpm.checkConsPyrFromCONSPYRByPyrId(payorId, consID, null),"        New PayorId is added for corresponding ConsolidationID");
		
		logger.info("*** Step 9 Action: - Clear test data in DB");
		int consId = consolidationDao.getConsByConsAbbrev(consID).getConsId();
		daoManagerXifinRpm.deleteConsolidationByCONS_ID(String.valueOf(consId), null);
	}

	@Test(priority=1,description="Update Consolidation-Add multi Client account Info")
	@Parameters({"description","minProcCodeMatch","maxService"})
	public void testXPR_530(String description, String minProcCodeMatch, String maxService)throws Exception{
		logger.info("===== Testing - XPR_530 =====");
		
		logger.info("*** Step 1 Action: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected result: - Verify that user login success and Load Consolidation page is displayed");
		consolidationRules = new ConsolidationRules(driver,wait);
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        Load Consolidation page is displayed");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"), "        user login successful");
		
		logger.info("*** Step 2 Action: - Enter a new Consolidation ID");
		randomCharacter = new RandomCharacter(driver);
		String consID = randomCharacter.getRandomAlphaNumericString(8);
		consolidationRules.inputToConsolidationIDInput(consID);
		
		logger.info("*** Step 2 Expected result: - Verify that Consolidation page is displayed");
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5));
		assertEquals(consolidationRules.consolidationRulesIdTextBox().getText().trim(), consID.toUpperCase(),"        Consolidation page is displayed with correct ID");
		
		logger.info("*** Step 3 Action: - Enter valid data for Decryption, minimum fields and add Proc Code into Requirements, Links and Consolidation Result table");
		String procCode = daoManagerXifinRpm.getProcedureCodeId(null).get(0);
		assertTrue(isElementPresent(consolidationRules.activeCheckbox(), 5));
		selectCheckBox(consolidationRules.activeCheckbox());
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5));
		consolidationRules.inputConsDesc(description);
		assertTrue(isElementPresent(consolidationRules.minProcCodeMatch(1), 5));
		consolidationRules.addProcCodeInLevelSection(procCode, procCode, maxService, minProcCodeMatch, minProcCodeMatch, minProcCodeMatch, 1, this);
		consolidationRules.addDataToConsolidationResultsTable(procCode, this);
		
		logger.info("*** Step 3 Expected result: - Verify that the new Proc Code is displayed in Requirements, Links and Consolidation Result table");
		assertTrue(isElementPresent(consolidationRules.requirementsTbl(1), 5));
		assertTrue(getColumnValue(consolidationRules.requirementsTbl(1), procCode),"        New Proc Code is shown in Requirement table");
		assertTrue(isElementPresent(consolidationRules.linksTbl(1), 5));
		assertTrue(getColumnValue(consolidationRules.linksTbl(1), procCode),"        New Proc Code is shown in Links table");
		assertTrue(isElementPresent(consolidationRules.consolidationResultTbl(), 5));
		assertTrue(getColumnValue(consolidationRules.consolidationResultTbl(), procCode),"        New Proc Code is shown in Consolidation Result table");
		
		logger.info("*** Step 4 Action: - Click Save Button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5));
		consolidationRules.clickOnSaveBtn();
		waitUntilElementPresent(consolidationRules.consolidationIDInput(), 30);
		
		logger.info("*** Step 4 Expected result: - New ConsolidationId is added into DB and Load Consolidation page is displayed");
		assertNotNull(consolidationDao.getConsByConsAbbrev(consID.toUpperCase()),"        New ConsolidationID is added into Db");
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        system take user return Load Consolidation ");
		
		logger.info("*** Step 5 Actions: - Load the Consolidation ID again");
		consolidationRules.inputToConsolidationIDInput(consID);
		
		logger.info("*** Step 5 Expected result: - Verify that Consolidation page is displayed");
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        Consolidation page is displayed");
		assertTrue(consolidationRules.consolidationRulesIdTextBox().getText().trim().contains(consID),"        ConsolidationID is displayed correctly");
		
		logger.info("*** Step 6 Action: - Add new Client Info");
		String clnAccntTyp1 = clientDao.getClnAccntTypWhereAccntTypIdIsNotZero().getDescr();
		consolidationRules.addDataToClntAcctTyp(clnAccntTyp1, this);
		
		String clnAccntTyp2 = consolidationRules.getClnAccntTypNotDuplicate(clnAccntTyp1, daoManagerXifinRpm, null);
		consolidationRules.addDataToClntAcctTyp(clnAccntTyp2, this);
		
		logger.info("*** Step 6 Expected result: - Verify that the new Client Account Type is shown in table");
		assertTrue(isElementPresent(consolidationRules.clientAccountTypeTable(), 5));
		assertTrue(getColumnValue(consolidationRules.clientAccountTypeTable(), clnAccntTyp1),"        New Client Account Type 1 is shown in table");
		assertTrue(getColumnValue(consolidationRules.clientAccountTypeTable(), clnAccntTyp2),"        New Client Account Type 2 is shown in table");
		
		logger.info("*** Step 7 Action: - Click Save Button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5));
		consolidationRules.clickOnSaveBtn();
		waitUntilElementPresent(consolidationRules.consolidationIDInput(), 30);
		
		logger.info("*** Step 7 Expected result: - Verify that the new Client Type is saved");
		assertNotNull(consolidationDao.getConsFromConsClnAcctTypAndClnAccntTypByConsAbbrevAndDescr(consID, clnAccntTyp1),"        Client account typ1 is added into DB for ConsolidationID");
		assertNotNull(consolidationDao.getConsFromConsClnAcctTypAndClnAccntTypByConsAbbrevAndDescr(consID, clnAccntTyp2),"        Client account typ2 is added into DB for ConsolidationID");
		
		logger.info("*** Step 8 Action: - Clear test data in DB");
		int pkConsId = consolidationDao.getConsByConsAbbrev(consID).getConsId();
		daoManagerXifinRpm.deleteConsolidationByCONS_ID(String.valueOf(pkConsId), null);
	}
	
	@Test(priority = 1, description ="Update consolidation rule - Add more diagnosis with valid data")
	public void testXPR_529() throws Exception {
		logger.info("===== Testing - testXPR_529 =====");
		
		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Consolidation Rules page is displayed with correct page title");
		consolidationRules = new ConsolidationRules(driver,wait);
		randomCharacter = new RandomCharacter();
		listUtil = new ListUtil();
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation Rules page title should be displayed.");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"),"        Page Title should be 'Consolidation Rules'.");							

		logger.info("*** Step 2 Actions: - Enter a new Consolidation ID and tab out");
		String consolId = randomCharacter.getRandomAlphaNumericString(6);
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation ID input field should be displayed.");
		consolidationRules.inputToConsolidationIDInput(consolId);
	
		logger.info("*** Step 2 Expected Results: - Verify that Consolidation Rules Page displays");
		assertTrue(isElementPresent(consolidationRules.consolidationRulesIdTextBox(), 5),"        Consolidation ID input field should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Description input field should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationPrioritySelect(), 5),"        Priority dropdown list should show.");
		assertTrue(isElementPresent(consolidationRules.activeCheckbox(), 5),"        Active checkbox should show.");
		consolidationRules.clickActiveCheckbox();
		assertEquals(consolId,consolidationRules.consolidationRulesIdTextBox().getText(),"        Consolidation ID: " + consolId + " should be displayed.");
		
		logger.info("*** Step 3 Actions: - Enter valid data for Description, minimum for Procode that mathed and unit. Add the same Proc Code into Requirements and Links tables");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Description input field should show.");
		consolidationRules.inputConsolidationDesc(consolId+consolId);

		String procId1 = daoManagerXifinRpm.getProcedureCodeId(null).get(0);
		consolidationRules.addProcCodeInLevelSection(procId1,procId1, "1", "1", "1", "1", 1,this);
		consolidationRules.addDataToConsolidationResultsTable(procId1,this);	
		
		logger.info("*** Step 3 Expected Results: - Verify that the new proc code is displayed in Requirements and Links Tables");
		consolidationRules.checkValueIsAvailableInTable(consolidationRules.requirementsTbl(1),3,procId1);
		consolidationRules.checkValueIsAvailableInTable(consolidationRules.linksTbl(1),3,procId1);
		
		logger.info("*** Step 4 Actions: Click Save Button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5),"        Save Button should show.");
		clickHiddenPageObject(consolidationRules.saveBtn(), 0);
		waitUntilElementPresent(consolidationRules.consolidationIDInput(), 30);
		
		logger.info("*** Step 4 Expected Results: - New Consolidation Id is created in Db");
		assertNotNull(consolidationDao.getConsByConsAbbrev(consolId));
		
		logger.info("*** Step 5 Actions: Load the Consolidation ID again");
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation ID input field should show.");
		consolidationRules.inputToConsolidationIDInput(consolId);
		
		logger.info("*** Step 5 Expected Results: -  Verify that Consolidation Rules page was loaded with data properly");
		assertTrue(isElementPresent(consolidationRules.consolidationRulesIdTextBox(), 5),"        Consolidation ID input field should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Description input field should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationPrioritySelect(), 5),"        Priority dropdown list should show.");
		assertTrue(isElementPresent(consolidationRules.activeCheckbox(), 5),"        Active checkbox should show.");
		
		logger.info("*** Step 6 Actions: Diagnosis section: Add new Diagnosis with valid data");
		String diagnosId = diagnosisCodeDao.getRandomDataFromDiagCd().getDiagCdId();
		assertTrue(isElementPresent(consolidationRules.addDiagnosisBtn(), 5),"        Add button in Diagnosis table is displayed.");
		clickHiddenPageObject(consolidationRules.addDiagnosisBtn(), 0);
		assertTrue(isElementPresent(consolidationRules.diagnosisCodeID(), 5),"        Diagnosis Code input field in Diagnosis table is displayed.");
		consolidationRules.inputDiagnosisId(diagnosId);
		assertTrue(isElementPresent(consolidationRules.OKBtn(), 5),"        Ok Button in Links table is displayed.");
		consolidationRules.clickOkBtn();
		clickHiddenPageObject(consolidationRules.radioBtnNone(), 0);
		
		logger.info("*** Step 6 Expected Results: - Verify that the new Diagnosis is displayed in Diagnosis Table");
		assertTrue(getColumnValue(consolidationRules.diagnosTable(), diagnosId),"        Diagnosis is displayed in Diagnosis table.");
		
		logger.info("*** Step 7 Actions: Click Save Button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5),"        Save Button should show.");
		consolidationRules.clickOnSaveBtn();
		waitUntilElementPresent(consolidationRules.consolidationIDInput(), 30);
		
		logger.info("*** Step 7 Expected Results: - Verify that the new Diagnosis is saved into the corresponding Consolidation Rule in DB");
		ConsDiag info = consolidationDao.getConsDiagByConsId(consolidationDao.getConsByConsAbbrev(consolId).getConsId());
		assertEquals(diagnosId, info.getDiagCdId(),"        New Diagnosis: " + diagnosId + " should be saved into DB.");
		
		logger.info("*** Step 8 Action: - Clear test data in DB");
		daoManagerXifinRpm.deleteConsolidationByCONS_ID(String.valueOf(consolidationDao.getConsByConsAbbrev(consolId).getConsId()), null);
	}
	
	@Test(priority = 1, description ="Update Consolidation Rule - Delete Level")
	public void testXPR_542() throws Exception {
		logger.info("===== Testing - testXPR_542 =====");
		
		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that Consolidation Rules Page shows");
		consolidationRules = new ConsolidationRules(driver,wait);
		randomCharacter = new RandomCharacter();
		listUtil = new ListUtil();
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation Rules page title should be displayed");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"),"        Page Title should be 'Consolidation Rules'");							

		logger.info("*** Step 2 Actions: - Enter a new Consolidation ID and tab out");
		String consolId = randomCharacter.getRandomAlphaNumericString(6);
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation Id input should be displayed");
		consolidationRules.inputToConsolidationIDInput(consolId);
	
		logger.info("*** Step 2 Expected Results: - Verify that Consolidation Id was loaded properly");	
		assertTrue(isElementPresent(consolidationRules.consolidationRulesIdTextBox(), 5),"        Consolidation ID text should shows.");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Description Input field should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationPrioritySelect(), 5),"        Priority dropdown list should show.");
		assertTrue(isElementPresent(consolidationRules.activeCheckbox(), 5),"        Active checkbox should show.");
		consolidationRules.clickActiveCheckbox();
		assertEquals(consolId,consolidationRules.consolidationRulesIdTextBox().getText(),"        Consolidation ID: " + consolId + " should show.");
		
		logger.info("*** Step 3 Actions: - Enter valid data for Description, minimum for Procode that mathed and unit. Add the same Proc Code into Requirements and Links table");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Description input field is displayed.");
		consolidationRules.inputConsolidationDesc(consolId+consolId);
		String procId1 = daoManagerXifinRpm.getProcedureCodeId(null).get(0);
		consolidationRules.addProcCodeInLevelSection(procId1,procId1, "1", "1", "1", "1", 1,this);
		consolidationRules.addDataToConsolidationResultsTable(procId1,this);	
		
		logger.info("*** Step 3 Expected Results: - Verify that the new proc code is display in Requirements and Links Tables");
		consolidationRules.checkValueIsAvailableInTable(consolidationRules.requirementsTbl(1),3,procId1);
		consolidationRules.checkValueIsAvailableInTable(consolidationRules.linksTbl(1),3,procId1);
		
		logger.info("*** Step 4 Actions: Click Add New Level button");
		assertTrue(isElementPresent(consolidationRules.addNewLevel(), 5),"        Add New Level button is displayed");
		consolidationRules.clickAddNewLevelBtn();
		
		logger.info("*** Step 4 Expected Results: - Verify that Level 2 is displayed");
		assertTrue(isElementPresent(consolidationRules.requirementsTbl(2), 5),"         Requirements table level 2 is displayed");
		assertTrue(isElementPresent(consolidationRules.linksTbl(2), 5),"        Links table level 2 is displayed");
		
		logger.info("*** Step 5 Actions: Enter valid minimum Proc Code and Unit. Add proc code into requirements and Links tables");
		String procId2 = consolidationRules.getProcedureCodeNotDuplicate(procId1, daoManagerXifinRpm, null);

		consolidationRules.addProcCodeInLevelSection(procId2,procId2, "1", "1", "1", "1", 2,this);
		consolidationRules.addDataToConsolidationResultsTable(procId2,this);	
		
		logger.info("*** Step 5 Expected Results: - New proc code is display in Requirements and Links Table");
		assertTrue(consolidationRules.checkValueIsAvailableInTable(consolidationRules.requirementsTbl(2),3,procId2),"         Procedure code displayed in Requirements");
		assertTrue(consolidationRules.checkValueIsAvailableInTable(consolidationRules.linksTbl(2),3,procId2),"         Procedure code displayed in Link");
		
		logger.info("*** Step 6 Actions: Click Save button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5),"        Save button is displayed");
		consolidationRules.clickOnSaveBtn();
		waitUntilElementPresent(consolidationRules.consolidationIDInput(), 30);
		
		logger.info("*** Step 6 Expected Results: - Verify that the new Consolidation ID is created in Db");
		assertTrue(daoManagerXifinRpm.checkConsolidationIdFromCONSByConsolId(consolId, null));
		
		logger.info("*** Step 7 Actions: Load the Consolidation ID again");
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation ID input field should be displayed.");
		consolidationRules.inputToConsolidationIDInput(consolId);
		
		logger.info("*** Step 7 Expected Results: - Verify that Consolidation Rules Page shows");
		assertTrue(isElementPresent(consolidationRules.consolidationRulesIdTextBox(), 5),"        Consolidation ID text should shows.");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Description Input field should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationPrioritySelect(), 5),"        Priority dropdown list should show.");
		assertTrue(isElementPresent(consolidationRules.activeCheckbox(), 5),"        Active checkbox should show.");
		
		logger.info("*** Step 8 Actions: Level section: Click on delete Level link.");
		consolidationRules.scrollToElement(consolidationRules.consolidationResultTbl());
		assertTrue(isElementPresent(consolidationRules.deleteLinkIcon(2), 5),"        Delete Level link should show.");
		clickHiddenPageObject(consolidationRules.deleteLinkIcon(2), 0);
		assertTrue(isElementPresent(consolidationRules.warningComfirmOkBtn(), 5),"        Warning - Confirm Level Deletion Ok button is displayed");
		clickHiddenPageObject(consolidationRules.warningComfirmOkBtn(), 0);
		consolidationRules.selectCheckBoxOnTable("tbl_consolidationResults",3,10,procId2,this);
		
		logger.info("*** Step 8 Expected Results: - Verify that the selected Level is removed in UI");
		assertEquals(consolidationRules.levelFieldset(2).getCssValue("display"),"none","        The selected Level is remove out of page");
		
		logger.info("*** Step 9 Actions: Click Save button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5),"        Save button is displayed");
		consolidationRules.clickOnSaveBtn();
		waitUntilElementPresent(consolidationRules.consolidationIDInput(), 10);
		
		logger.info("*** Step 9 Expected Results: - Verify that only Level 1 left");
		assertEquals("1", daoManagerXifinRpm.countConsolLevelFromCONSLEVELByConsolId(daoManagerXifinRpm.getConsIDFromCONSByConsAbbrev(consolId,null).get(0), null),"        Only Level 1 left.");
		
		logger.info("*** Step 10 Action: - Clear test data in DB");
		daoManagerXifinRpm.deleteConsolidationByCONS_ID(daoManagerXifinRpm.getConsIDFromCONSByConsAbbrev(consolId,null).get(0), null);
	}
	
	@Test(priority = 1, description ="Update Consolidation Rules - Update PayorGroup with date is overlapping")
	public void testXPR_559() throws Exception {
		logger.info("===== Testing - testXPR_559 =====");
		
		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Consolidation Rules page is displayed with correct page title");
		consolidationRules = new ConsolidationRules(driver, wait);
		randomCharacter = new RandomCharacter();
		TimeStamp timeStamp = new TimeStamp();
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation Rules page title should be displayed");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"),"        Page Title should be 'Consolidation Rules'");							

		logger.info("*** Step 2 Actions: - Enter a new Consolidation ID and tab out");
		String consolId = randomCharacter.getRandomAlphaNumericString(6);
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation Id input field should be displayed");
		consolidationRules.inputToConsolidationIDInput(consolId);
		
		logger.info("*** Step 2 Expected Results: - Verify that Consolidation Rules Page shows");
		assertTrue(isElementPresent(consolidationRules.consolidationRulesIdTextBox(), 5),"        Consolidation ID text should shows.");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Description Input field should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationPrioritySelect(), 5),"        Priority dropdown list should show.");
		assertTrue(isElementPresent(consolidationRules.activeCheckbox(), 5),"        Active checkbox should show.");		
		consolidationRules.clickActiveCheckbox();
		assertEquals(consolId,consolidationRules.consolidationRulesIdTextBox().getText(),"        Consolidation ID: " + consolId + " should show.");
		
		logger.info("*** Step 3 Actions: Add new Payor Group in Payor Info section");
		String payorGrp = payorDao.getPyrGrpFromConsPyrGrpWhereGrpNmIsNotNull().getGrpNm();
		String dosEffDate = timeStamp.getPreviousDate("MM/d/yyyy",2);
		String calEffDate = timeStamp.getPreviousDate("MM/d/yyyy",2);
		String expDate = timeStamp.getCurrentDate("MM/d/yyyy");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Description input field is displayed.");
		consolidationRules.inputConsolidationDesc(consolId+consolId);
		assertTrue(isElementPresent(consolidationRules.addConsPyrGrpBtn(), 5),"        Add button in Payor Groups table is displayed.");		
		consolidationRules.clickAddConsPyrGrpBtn();
		assertTrue(isElementPresent(consolidationRules.payorGroupSelect(), 5),"        Payor Group dropdown in in Add Record popup is displayed");
		consolidationRules.selectDropDownJQGird(consolidationRules.payorGroupSelect(), payorGrp);
		assertTrue(isElementPresent(consolidationRules.dosEffDateInput(), 5),"        DOS Eff date input field in Add Record popup is displayed");
		consolidationRules.inputDosEffDt(dosEffDate);
		assertTrue(isElementPresent(consolidationRules.calendateEffDateInput(), 5),"        Calendar Eff date input in Add Record popup is displayed");
		consolidationRules.inputCalEffDt(calEffDate);
		assertTrue(isElementPresent(consolidationRules.expDateInput(), 5),"        Exp date input field in Add Record popup is displayed");
		consolidationRules.inputExpDt(expDate);
		assertTrue(isElementPresent(consolidationRules.OKBtn(), 5),"        Ok button in Add Record popup is displayed");
		consolidationRules.clickOkBtn();
		
		logger.info("*** Step 3 Expected Results: - Verify that the new Payor Group is displayed in Payor Group table");
		assertTrue(getColumnValue(consolidationRules.payorGrpTbl(), payorGrp),"        Payor Group is displayed in Payor Group table.");
		
		logger.info("*** Step 4 Actions: - Enter valid data for Description, minimum for Procode that mathed and unit. Add the same Proc Code into Requirements and Links tables");
		String procId1 = daoManagerXifinRpm.getProcedureCodeId(null).get(0);
		consolidationRules.addProcCodeInLevelSection(procId1,procId1, "1", "1", "1", "1", 1,this);
		consolidationRules.addDataToConsolidationResultsTable(procId1,this);	
		
		logger.info("*** Step 4 Expected Results: - Verify that new proc code is displayed in Requirements and Links Tables");
		consolidationRules.checkValueIsAvailableInTable(consolidationRules.requirementsTbl(1),3,procId1);
		consolidationRules.checkValueIsAvailableInTable(consolidationRules.linksTbl(1),3,procId1);
		
		logger.info("*** Step 5 Actions: Click Save button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5),"        Save button is displayed");		
		consolidationRules.clickOnSaveBtn();
		waitUntilElementPresent(consolidationRules.consolidationIDInput(), 30);
		
		logger.info("*** Step 5 Expected Results: - Verify that new Consolidation ID is created in DB");
		assertNotNull(consolidationDao.getConsByConsAbbrev(consolId));
		
		logger.info("*** Step 6 Actions: Load Consolidation ID again");
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation ID input field should be displayed");
		consolidationRules.inputToConsolidationIDInput(consolId);
		
		logger.info("*** Step 6 Expected Results: - Verify that Consolidation Rules Page shows");
		assertTrue(isElementPresent(consolidationRules.consolidationRulesIdTextBox(), 5),"        Consolidation ID text should shows.");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Description Input field should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationPrioritySelect(), 5),"        Priority dropdown list should show.");
		assertTrue(isElementPresent(consolidationRules.activeCheckbox(), 5),"        Active checkbox should show.");
		
		logger.info("*** Step 7 Actions: Add a new Payor Group that is the same as the existing one. New Eff date < existing Eff date");
		String dosEffDate1 = timeStamp.getPreviousDate("MM/d/yyyy",1);
		String calEffDate1 = timeStamp.getPreviousDate("MM/d/yyyy",1);
		String expDate1= timeStamp.getCurrentDate("MM/d/yyyy");
		assertTrue(isElementPresent(consolidationRules.addConsPyrGrpBtn(), 5),"        Add button in Payor Groups table is displayed.");
		clickHiddenPageObject(consolidationRules.addConsPyrGrpBtn(), 0);
		assertTrue(isElementPresent(consolidationRules.payorGroupSelect(), 5),"        Payor Group dropdown in in Add Record popup is displayed");
		consolidationRules.selectDropDownJQGird(consolidationRules.payorGroupSelect(), payorGrp);
		assertTrue(isElementPresent(consolidationRules.dosEffDateInput(), 5),"        DOS Eff date input field in Add Record popup is displayed");
		consolidationRules.inputDosEffDt(dosEffDate1);
		assertTrue(isElementPresent(consolidationRules.calendateEffDateInput(), 5),"        Calendar Eff date input in Add Record popup is displayed");
		consolidationRules.inputCalEffDt(calEffDate1);
		assertTrue(isElementPresent(consolidationRules.expDateInput(), 5),"        Exp date input field in Add Record popup is displayed");
		consolidationRules.inputExpDt(expDate1);
		assertTrue(isElementPresent(consolidationRules.OKBtn(), 5),"        Ok button in Add Record popup is displayed");
		consolidationRules.clickOkBtn();
		
		logger.info("*** Step 7 Expected Results: - Verify that the Payor Group is displayed in Table");
		assertTrue(consolidationRules.checkValueIsAvailableInTable(consolidationRules.payorGrpTbl(),3,payorGrp),"       Payor Group: " + payorGrp + " is displayed in Payor Group Table.");
		
		logger.info("*** Step 8 Actions: Click Save button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5),"        Save button is displayed");		
		consolidationRules.clickOnSaveBtn();		
		
		logger.info("*** Step 8 Expected Results: - Verify that an Error Message about overlapping is displayed");
		assertTrue(isElementPresent(consolidationRules.errorMessageLbl(), 5),"        Error Message is displayed");
		dosEffDate = timeStamp.getPreviousDate("yyyy-MM-dd",2);
		calEffDate = timeStamp.getPreviousDate("yyyy-MM-dd",2);
		expDate = timeStamp.getCurrentDate("yyyy-MM-dd");
		dosEffDate1 = timeStamp.getPreviousDate("yyyy-MM-dd",1);
		calEffDate1 = timeStamp.getPreviousDate("yyyy-MM-dd",1);
		String errMessageStr = "Overlapping date of services found for payor group "+payorGrp+": "+dosEffDate+" through "+expDate+" and "+dosEffDate1+" through "+expDate+"\n"
				+ "Overlapping effective dates found for payor group "+payorGrp+": "+calEffDate+" through "+expDate+" and "+calEffDate1+" through "+expDate+"";
		assertEquals(consolidationRules.errorMessageLbl().getText(), errMessageStr, "        Error Message: " + errMessageStr + " should show.");
				
		logger.info("*** Step 9 Action: - Clear test data in DB");
		daoManagerXifinRpm.deleteConsolidationByCONS_ID(String.valueOf(consolidationDao.getConsByConsAbbrev(consolId).getConsId()), null);
	}
	
	@Test(priority = 1, description ="Update Consolidation Rule - Verify Run Audit button")
	public void testXPR_572() throws Exception {
		logger.info("===== Testing - testXPR_572 =====");
		
		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Consolidation Rules page is displayed with correct page title");
		consolidationRules = new ConsolidationRules(driver, wait);
		randomCharacter = new RandomCharacter();
		listUtil = new ListUtil();
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation Rules page title should be displayed");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"),"        Page Title should be 'Consolidation Rules'");							

		logger.info("*** Step 2 Actions: - Enter a new Consolidation ID and tab out");
		String consolId = randomCharacter.getRandomAlphaNumericString(6);
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation ID Input field should be displayed");
		consolidationRules.inputToConsolidationIDInput(consolId);
	
		logger.info("*** Step 2 Expected Results: - Verify that Consolidation Rules Page shows");
		assertTrue(isElementPresent(consolidationRules.consolidationRulesIdTextBox(), 5),"        Consolidation ID text should shows.");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Description Input field should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationPrioritySelect(), 5),"        Priority dropdown list should show.");
		assertTrue(isElementPresent(consolidationRules.activeCheckbox(), 5),"        Active checkbox should show.");
		consolidationRules.clickActiveCheckbox();
		assertEquals(consolId,consolidationRules.consolidationRulesIdTextBox().getText(),"        Consolidation ID: " + consolId + " should show.");
		
		logger.info("*** Step 3 Actions: - Enter valid data for Description, minimum for Procode that mathed and unit. Add the same Proc Code into Requirements and Links tables");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Description Input field should show.");
		consolidationRules.inputConsolidationDesc(consolId+consolId);

		String procId1 = daoManagerXifinRpm.getProcedureCodeId(null).get(0);
		consolidationRules.addProcCodeInLevelSection(procId1,procId1, "1", "1", "1", "1", 1,this);
		consolidationRules.addDataToConsolidationResultsTable(procId1,this);	
		
		logger.info("*** Step 3 Expected Results: - Verify that new proc code is displayed in the Requirements and Links Tables");
		consolidationRules.checkValueIsAvailableInTable(consolidationRules.requirementsTbl(1),3,procId1);
		consolidationRules.checkValueIsAvailableInTable(consolidationRules.linksTbl(1),3,procId1);
		
		logger.info("*** Step 4 Actions: Click Save Button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5),"        Save button should be displayed.");
		clickHiddenPageObject(consolidationRules.saveBtn(), 0);
		waitUntilElementPresent(consolidationRules.consolidationIDInput(), 20);
		
		logger.info("*** Step 4 Expected Results: - Verify that the new Consolidation ID is created in Db");
		assertNotNull(consolidationDao.getConsByConsAbbrev(consolId));
		
		logger.info("*** Step 5 Actions: Load Consolidation ID again");
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation ID input field should be displayed.");
		consolidationRules.inputToConsolidationIDInput(consolId);
		
		logger.info("*** Step 5 Expected Results: - Verify that Consolidation Rules Page shows");
		assertTrue(isElementPresent(consolidationRules.consolidationRulesIdTextBox(), 5),"        Consolidation ID text should shows.");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Description Input field should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationPrioritySelect(), 5),"        Priority dropdown list should show.");
		assertTrue(isElementPresent(consolidationRules.activeCheckbox(), 5),"        Active checkbox should show.");
		
		logger.info("*** Step 6 Actions: Click Run Audit Button");
		assertTrue(isElementPresent(consolidationRules.runAuditBtn(), 5),"        Run Audit Button should be displayed.");
		consolidationRules.clickRunAuditBtn();
		Thread.sleep(5000);
		String parent = switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that Audit detail is displayed with correct data");
//		List<String> aud = daoManagerXifinRpm.getAudRecIdFromCONSByConsolId(daoManagerXifinRpm.getConsIDFromCONSByConsAbbrev(consolId,null).get(0),null);
//		//List<String> info = daoManagerXifinRpm.getTableFromAUDITVByAudrecId(aud,null);
//
//		//In this case (Create a new Consolidation ID), only CONS, CONS_RESULTS, CONS_PROC_REQ_PROC, and CONS_PROC_REQ tables will have the audit records
//		List<String> audRecList = Lists.newArrayList(aud.get(0),aud.get(1), aud.get(4), aud.get(5));
//		List<String> info = daoManagerXifinRpm.getTableFromAUDITVByAudrecId(audRecList,null);
//
//		List<String> infoUI = consolidationRules.getListValueOnTable("tbl_auditlogwait", 4);
//		System.out.println("        Audit Detail Table list from UI: " + infoUI);
//		assertTrue(listUtil.compareLists(infoUI, info),"        Audit Detail should be displayed with correct data.");
		driver.close();
		switchToParentWin(parent);
		
		logger.info("*** Step 9 Action: - Clear test data in DB");		
		daoManagerXifinRpm.deleteConsolidationByCONS_ID(String.valueOf(consolidationDao.getConsByConsAbbrev(consolId).getConsId()), null);
	}
	
	@Test(priority=1, description="Update Consolidation Rule - Add new Consolidation Result")
	public void testXPR_531() throws Exception{
		logger.info("===== Testing - testXPR_531 =====");          

		logger.info("*** Step 1 Action: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Consolidation Rules page is displayed with correct page title");
		consolidationRules = new ConsolidationRules(driver, wait);
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation Rules page title should be displayed");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"),"        Page Title should be 'Consolidation Rules'");							

		logger.info("*** Step 2 Actions: - Enter non-existing consolidation ID and tab out");
		randomCharacter = new RandomCharacter();
		String consolId = randomCharacter.getRandomAlphaNumericString(10);
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation Id input should be displayed");
		consolidationRules.inputToConsolidationIDInput(consolId);
		
		logger.info("*** Step 2 Expected Results: - Verify that Consolidation Page displays");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        System displayed Consolidation Page");
		
		logger.info("*** Step 3 Actions: - Enter valid data for Description and new procedure code to requirement and link table");
		String consolidationDescr = randomCharacter.getRandomAlphaNumericString(10);
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Description Input must be available");
		consolidationRules.inputConsolidationDesc(consolidationDescr);
		
		String procCodeID = daoManagerXifinRpm.getProcedureCodeId(null).get(0);
		consolidationRules.addProcCodeToLevelAndResult(procCodeID,procCodeID, "1", "1", "1", "1", "1",this);
		
		logger.info("*** Step 3 Expected Results: - Verify that the new procedure code added to table");
		assertTrue(consolidationRules.requirementsTblCell(2, 3, 1).getText().equalsIgnoreCase(procCodeID),"        Procedure Code ID must added to Requirement table");
		assertTrue(consolidationRules.linkTblCell(2, 3, 1).getText().equalsIgnoreCase(procCodeID),"        Procedure Code ID must added to Link table");
		assertTrue(consolidationRules.consolidationResultTblCell(2, 3).getText().equalsIgnoreCase(procCodeID),"        Procedure Code ID must added to Result table");
		
		logger.info("*** Step 4 Actions: - Click Save Button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(),5),"        Save button must be available");
		consolidationRules.clickOnSaveBtn();
		waitUntilElementPresent(consolidationRules.consolidationIDInput(),30);
				
		logger.info("*** Step 4 Expected Results: - Verify that the new consolidation rule was created in database and user returned to load screen");
		assertNotNull(consolidationDao.getConsByConsAbbrev(consolId),"        Consolidation must added to database");
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation Id input should be displayed");
		
		logger.info("*** Step 5 Actions: - Add new consolidation result");
		consolidationRules.inputToConsolidationIDInput(consolId);
		String procCodeID2 = consolidationRules.getProcedureCodeNotDuplicate(procCodeID, daoManagerXifinRpm, null);
		consolidationRules.addProcCodeToLevelAndResult(procCodeID2,procCodeID2, "1", "1", "1", "1", "1",this);
		
		logger.info("*** Step 5 Expected Results: - Verify that the new consolidation result was added");
		assertTrue(consolidationRules.consolidationResultTblCell(3, 3).getText().equalsIgnoreCase(procCodeID2),"        Procedure Code ID must added to Result table");
		
		logger.info("*** Step 6 Actions: - Click Save Button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(),5),"        Save button should show.");
		consolidationRules.clickOnSaveBtn();
		waitUntilElementPresent(consolidationRules.consolidationIDInput(),30);
		
		logger.info("*** Step 6 Expected Results: - Verify that the new consolidation result is saved to the corresponding consolidation rule id");
		assertEquals(consolidationDao.getConsResultsFromConsByConsAbbrev(consolId).size(), 2,"        " + procCodeID2 + " should be added to the corresponding consolidation rule ID: " + consolId);
		
		logger.info("*** Step 7 Action: - Clear test data in DB");
		daoManagerXifinRpm.deleteConsolidationByCONS_ID(String.valueOf(consolidationDao.getConsByConsAbbrev(consolId).getConsId()),null);
	}
	
	@Test(priority=1, description="Update Consolidation Rule - Add new Consolidation Rule that is not in level section")
	public void testXPR_532() throws Exception{
		logger.info("===== Testing - testXPR_532 =====");          

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Consolidation Rules page is displayed with correct page title");
		consolidationRules = new ConsolidationRules(driver, wait);
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation Rules page title should be displayed");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"),"        Page Title should be 'Consolidation Rules'");							

		logger.info("*** Step 2 Actions: - Enter a new Consolidation ID and tab out");
		String consolId = consolidationDao.getConsHavingConsAbbrevNotNull().getConsAbbrev();
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation Id input should be displayed");
		consolidationRules.inputToConsolidationIDInput(consolId);
		
		logger.info("*** Step 2 Expected Results: - Verify that Consolidation Rules Page displays");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Rules Page should show.");
		
		logger.info("*** Step 3 Actions: - Add consolidation result that is not show in Levels section");
		String procCodeID = consolidationRules.addNewProcCodeInResultTblNotInLevelSection(1,daoManagerXifinRpm,this,null);
		
		logger.info("*** Step 3 Expected Results: - Verify that the New procedure code is added");
		assertTrue(consolidationRules.checkProcCodeAddedToTableResult(procCodeID),"        Procedure Code ID should be added to Result table");
		
		logger.info("*** Step 4 Actions: - Click Save Button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(),5),"        Save Button should show.");
		consolidationRules.clickOnSaveBtn();
		
		logger.info("*** Step 4 Expected Results: - Verify that an Error shows");
		assertTrue(isElementPresent(consolidationRules.errorMessageLbl(),5),"        Error message should show.");
		assertTrue(consolidationRules.errorMessageLbl().getText().contains(procCodeID+": results procedure code is not found"),"        Error message: " + procCodeID + " : results procedure code is not found in either link table. should show.");
	}
	
	@Test(priority=1, description="Update Consolidation Rule - Deleted Procedure code in requirement table")
	public void testXPR_539() throws Exception{
		logger.info("===== Testing - testXPR_539 =====");          

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Consolidation Rules page is displayed with correct page title");
		consolidationRules = new ConsolidationRules(driver, wait);
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation Rules page title should be displayed");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"),"        Page Title should be 'Consolidation Rules'");							

		logger.info("*** Step 2 Actions: - Enter a new Consolidation ID and tab out");
		randomCharacter = new RandomCharacter(driver);
		String consolId = randomCharacter.getRandomAlphaNumericString(8);
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation ID input field should show.");
		consolidationRules.inputToConsolidationIDInput(consolId);
		
		logger.info("*** Step 2 Expected Results: - Verify that Consolidation Page displays");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Page should show.");
		
		logger.info("*** Step 3 Actions: - Add multi procedure codes to requirement table and enter Min Procedure code match, min unit");
		String procCodeID = daoManagerXifinRpm.getProcedureCodeId(null).get(0);
		String procCodeID2 = consolidationRules.getProcedureCodeNotDuplicate(procCodeID, daoManagerXifinRpm, null);
		consolidationRules.addProcCodeToRequirementTbl(procCodeID, "1", "1", "1",this);
		consolidationRules.addProcCodeToRequirementTbl(procCodeID2, "1", "1", "1",this);
		consolidationRules.addMinProcMatchAndMinUnit("1", "1", "1");
		
		logger.info("*** Step 3 Expected Results: - Verify that Procedure codes are added to the Requirements table");
		assertTrue(consolidationRules.requirementsTblCell(2, 3, 1).getText().equalsIgnoreCase(procCodeID),"        Procedure Code ID: " + procCodeID + " should be added to Requirements table.");
		assertTrue(consolidationRules.requirementsTblCell(3, 3, 1).getText().equalsIgnoreCase(procCodeID2),"        Procedure Code ID2: " + procCodeID2 + " should be added to Requirements table.");
		
		logger.info("*** Step 4 Actions: - Add procedure code to Links table");
		consolidationRules.addProcCodeToLinkTbl(procCodeID,procCodeID, "1", "1", "1",this);
		consolidationRules.addProcCodeToLinkTbl(procCodeID2,procCodeID2, "1", "1", "1",this);
		
		logger.info("*** Step 4 Expected Results: - Verify that Procedure code are added to the Links table");
		assertTrue(consolidationRules.linkTblCell(2, 3, 1).getText().equalsIgnoreCase(procCodeID),"        Procedure Code ID: " + procCodeID + "  should be added to Links table.");
		assertTrue(consolidationRules.linkTblCell(3, 3, 1).getText().equalsIgnoreCase(procCodeID2),"        Procedure Code ID2: " + procCodeID2 + " should be added to Links table.");
		
		logger.info("*** Step 5 Actions: - Save all data");
		consolidationRules.addProcCodeToResultTbl(procCodeID, this);
		consolidationRules.addProcCodeToResultTbl(procCodeID2, this);
		assertTrue(consolidationRules.consolidationResultTblCell(2, 3).getText().equalsIgnoreCase(procCodeID),"        Procedure Code ID must added to Result table");
		assertTrue(consolidationRules.consolidationResultTblCell(3, 3).getText().equalsIgnoreCase(procCodeID2),"        Procedure Code ID 2 must added to Result table");
		
		String consolidationDescr = randomCharacter.getRandomAlphaNumericString(10);
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Description Input must be available");
		consolidationRules.inputConsolidationDesc(consolidationDescr);
		
		assertTrue(isElementPresent(consolidationRules.saveBtn(),5),"        Save Button should show.");
		consolidationRules.clickOnSaveBtn();
		waitUntilElementPresent(consolidationRules.consolidationIDInput(),30);
		
		logger.info("*** Step 5 Expected Results: - Verify that new consolidation rule was created in database and user returns to the load screen");
		assertNotNull(consolidationDao.getConsByConsAbbrev(consolId),"        Consolidation rule should be saved in DB.");
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation ID input field should show.");
		
		logger.info("*** Step 6 Actions: - Load the Consolidation ID again");
		consolidationRules.inputToConsolidationIDInput(consolId);
		
		logger.info("*** Step 6 Expected Results: - Verify that Consolidation Rules Page shows");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Rules Page should show.");
		
		logger.info("*** Step 7 Actions: - Check Delete checkbox in the Requirements table, Links table and Consolidation Results table");
		assertTrue(isElementPresent(consolidationRules.delLinkCheckbox(2,1), 5),"       Delete check box link table should be displayed.");
		assertTrue(isElementPresent(consolidationRules.delRequireCheckbox(2,1), 5),"        Delete check box requirement table should be displayed.");
		assertTrue(isElementPresent(consolidationRules.delResultCheckbox(2), 5),"        Delete check box result table should be displayed.");
		consolidationRules.scrollToElement(consolidationRules.delLinkCheckbox(2,1));
		selectCheckBox(consolidationRules.delLinkCheckbox(2,1));
		selectCheckBox(consolidationRules.delRequireCheckbox(2,1));
		selectCheckBox(consolidationRules.delResultCheckbox(2));
		
		logger.info("*** Step 7 Expected Results: - Verify that the records are marked as Deleted");
		assertTrue(consolidationRules.rowIsMarkForDelete(consolidationRules.linksTbl(1), 2),"       Procedure code in Links table should be marked as Deleted.");
		assertTrue(consolidationRules.rowIsMarkForDelete(consolidationRules.requirementsTbl(1), 2),"       Procedure code in Requirements table should be marked as Deleted.");
		assertTrue(consolidationRules.rowIsMarkForDelete(consolidationRules.consolidationResultTbl(), 2),"       Procedure code in Consolidation Results table should be marked as Deleted.");
		
		logger.info("*** Step 8 Actions: - Click Save Button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5));
		consolidationRules.clickOnSaveBtn();
		waitUntilElementPresent(consolidationRules.consolidationIDInput(),30);
		
		logger.info("*** Step 8 Expected Results: - Verify that Consolidation Levels are removed from the corresponding Consolidation rule in database and user returns to the load screen");
		assertEquals(consolidationDao.getConsProcReqFromConsByConsAbbrev(consolId).size(), 1, "        Consolidation Requirement should be removed from the corresponding Consolidation Rule.");
		assertEquals(consolidationDao.getConsProcReqProcFromConsByConsAbbrev(consolId).size(), 1, "        Consolidation Link should be removed from the corresponding Consolidation Rule");
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation ID input field should be displayed.");
		
		logger.info("*** Step 9 Action: - Clear test data in DB");
		daoManagerXifinRpm.deleteConsolidationByCONS_ID(String.valueOf(consolidationDao.getConsByConsAbbrev(consolId).getConsId()),null);
	}
	
	@Test(priority=1, description="Update Consolidation Rule - Delete Consolidation Result")
	public void testXPR_540() throws Exception{
		logger.info("===== Testing - testXPR_540 =====");          

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Consolidation Rules page is displayed with correct page title");
		consolidationRules = new ConsolidationRules(driver, wait);
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation Rules page title should be displayed.");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"),"        Page Title should be 'Consolidation Rules'.");							

		logger.info("*** Step 2 Actions: - Enter a new Consolidation ID and tab out");
		randomCharacter = new RandomCharacter(driver);
		String consolId = randomCharacter.getRandomAlphaNumericString(8);
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation ID input field should be displayed.");
		consolidationRules.inputToConsolidationIDInput(consolId);
		
		logger.info("*** Step 2 Expected Results: - Verify that Consolidation Rules Page displays");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Rules Page should show.");
		
		logger.info("*** Step 3 Actions: - Add multi procedure code to Requirements table and enter Min Procedure code match, min unit");
		String procCodeID = daoManagerXifinRpm.getProcedureCodeId(null).get(0);
		String procCodeID2 = consolidationRules.getProcedureCodeNotDuplicate(procCodeID, daoManagerXifinRpm, null);
		consolidationRules.addProcCodeToRequirementTbl(procCodeID, "1", "1", "1",this);
		consolidationRules.addProcCodeToRequirementTbl(procCodeID2, "1", "1", "1",this);
		consolidationRules.addMinProcMatchAndMinUnit("1", "1", "1");
		
		logger.info("*** Step 3 Expected Results: - Verify that the Procedure code added to Requirements table");
		assertTrue(consolidationRules.requirementsTblCell(2, 3, 1).getText().equalsIgnoreCase(procCodeID),"        Procedure Code ID " + procCodeID + " should be added to Requirements table.");
		assertTrue(consolidationRules.requirementsTblCell(3, 3, 1).getText().equalsIgnoreCase(procCodeID2),"        Procedure Code ID 2 " + procCodeID2 + " should be added to Requirements table.");
		
		logger.info("*** Step 4 Actions: - Add procedure code in the Links table");
		consolidationRules.addProcCodeToLinkTbl(procCodeID,procCodeID, "1", "1", "1",this);
		consolidationRules.addProcCodeToLinkTbl(procCodeID2,procCodeID2, "1", "1", "1",this);
		
		logger.info("*** Step 4 Expected Results: - Verify that Procedure code added to Links table");
		assertTrue(consolidationRules.linkTblCell(2, 3, 1).getText().equalsIgnoreCase(procCodeID),"        Procedure Code ID " + procCodeID + " should be added to Links table.");
		assertTrue(consolidationRules.linkTblCell(3, 3, 1).getText().equalsIgnoreCase(procCodeID2),"        Procedure Code ID 2 " + procCodeID2 + " should be added to Links table.");
		
		logger.info("*** Step 5 Actions: - Save all data");
		consolidationRules.addProcCodeToResultTbl(procCodeID, this);
		consolidationRules.addProcCodeToResultTbl(procCodeID2, this);
		assertTrue(consolidationRules.consolidationResultTblCell(2, 3).getText().equalsIgnoreCase(procCodeID),"        Procedure Code ID must added to Consolidation Results table");
		assertTrue(consolidationRules.consolidationResultTblCell(3, 3).getText().equalsIgnoreCase(procCodeID2),"        Procedure Code ID 2 must added to Consolidation Results table");
		
		String consolidationDescr = randomCharacter.getRandomAlphaNumericString(10);
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Description Input field should show.");
		consolidationRules.inputConsolidationDesc(consolidationDescr);
		
		assertTrue(isElementPresent(consolidationRules.saveBtn(),5),"        Save Button should show. ");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5));
		consolidationRules.clickOnSaveBtn();
		waitUntilElementPresent(consolidationRules.consolidationIDInput(),30);
		
		logger.info("*** Step 5 Expected Results: - Verify that the new consolidation rule was created in database and user returns to load screen");
		assertNotNull(consolidationDao.getConsByConsAbbrev(consolId),"        Consolidation must added to database");
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation Id input should be displayed");
		
		logger.info("*** Step 6 Actions: - Load the Consolidation ID again");
		consolidationRules.inputToConsolidationIDInput(consolId);
		
		logger.info("*** Step 6 Expected Results: - Verify that Consolidation Rules Page shows");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Rules Page should show.");
		
		logger.info("*** Step 7 Actions: - Check Delete checkbox in Requirements table and Links table");
		assertTrue(isElementPresent(consolidationRules.delLinkCheckbox(2,1), 5),"       Delete checkbox in Links table should be displayed.");
		assertTrue(isElementPresent(consolidationRules.delRequireCheckbox(2,1), 5),"        Delete checkbox in Requirements table should be displayed.");
		assertTrue(isElementPresent(consolidationRules.delResultCheckbox(2), 5),"        Delete checkbox in Results table should be displayed.");
		consolidationRules.scrollToElement(consolidationRules.delLinkCheckbox(2,1));
		selectCheckBox(consolidationRules.delLinkCheckbox(2,1));
		selectCheckBox(consolidationRules.delRequireCheckbox(2,1));
		selectCheckBox(consolidationRules.delResultCheckbox(2));
		
		logger.info("*** Step 7 Expected Results: - Verify that the records are marked as Deleted");
		assertTrue(consolidationRules.rowIsMarkForDelete(consolidationRules.linksTbl(1), 2),"       Procedure code in Links table should be marked as deleted.");
		assertTrue(consolidationRules.rowIsMarkForDelete(consolidationRules.requirementsTbl(1), 2),"       Procedure code in Requirements table should be marked as deleted.");
		assertTrue(consolidationRules.rowIsMarkForDelete(consolidationRules.consolidationResultTbl(), 2),"       Procedure code in Consolidation Results table should be marked as deleted.");
		
		logger.info("*** Step 8 Actions: - Click Save Button");
		consolidationRules.clickOnSaveBtn();
		waitUntilElementPresent(consolidationRules.consolidationIDInput(),30);
		
		logger.info("*** Step 8 Expected Results: - Verify that Consolidation results were removed from the corresponding Consolidation rule in database and user returns to load screen");
		assertEquals(consolidationDao.getConsResultsFromConsByConsAbbrev(consolId).size(), 1, "        Consolidation results should be removed from the corresponding Consolidation Rule.");
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation ID input field should show.");
		
		logger.info("*** Step 9 Action: - Clear test data in DB");
		daoManagerXifinRpm.deleteConsolidationByCONS_ID(String.valueOf(consolidationDao.getConsByConsAbbrev(consolId).getConsId()),null);
	}
	
	@Test(priority=1, description="pdate Consolidation Rule - Reset the update")
	public void testXPR_541() throws Exception{
		logger.info("===== Testing - testXPR_541 =====");          

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Consolidation Rules page is displayed with correct page title");
		consolidationRules = new ConsolidationRules(driver, wait);
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation Rules page title should be displayed");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"),"        Page Title should be 'Consolidation Rules'");							

		logger.info("*** Step 2 Actions: - Enter an existing Consolidation ID and tab out");
		String consolId = consolidationDao.getConsNotInConsPyrGrp().getConsAbbrev();
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation ID input field should be displayed");
		consolidationRules.inputToConsolidationIDInput(consolId);
		
		logger.info("*** Step 2 Expected Results: - Verify that Consolidation Rules Page displays");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Rules Page should show.");
		
		logger.info("*** Step 3 Actions: - Add a Payor Group in Payor Groups table");
		timeStamp = new TimeStamp(driver);
		String effDate = timeStamp.getPreviousDate("MM/dd/YYYY", 1);
		String pyrGroup = payorDao.getPayorGrpToAddSingleFromPyrGrp().getGrpNm();
		consolidationRules.addDataToPyrGrp(pyrGroup, effDate, this);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor group was added to Payor Groups table");
		assertTrue(consolidationRules.checkValueIsAvailableInTable(consolidationRules.payorGrpTbl(), 3, pyrGroup),"        Payor Group: " + pyrGroup + " should be added to Payor Groups table.");
		
		consolidationRules.selectCheckBoxOnTable("tbl_consolidationPayorGroups",3,8,pyrGroup,this);
		
		logger.info("*** Step 4 Actions: - Click Reset Button");
		assertTrue(isElementPresent(consolidationRules.resetBtn(), 5),"        Reset Button should show.");
		consolidationRules.clickResetBtn();
		
		logger.info("*** Step 4 Expected Results: - Verify that the Payor group was not saved into the corresponding Consolidation Rule  in DB");
		assertNull(payorDao.getPyrGrpByConsAbbrevAndGrpNm(consolId, pyrGroup),"        Payor Group: " + pyrGroup + " was added to the Consolidation Rules: " + consolId);
	}
	
	@Test(priority = 1, description = "Add Consolidation Rules with valid data for all fields")
    public void testXPR_519() throws Exception {
		logger.info("*** Testing - XPR - 519 - Consolidation Rule - Add Consolidation Rules with input valid data for all fields ***");
		randomCharacter = new RandomCharacter(driver);
		consolidationRules = new ConsolidationRules(driver, wait);
		timeStamp = new TimeStamp();
		
		logger.info("*** Step 1 Actions: Log into Xifin Portal with SSO username and password");		
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();		

		logger.info("*** Step 1 Expected Results: Verify that user is logged into Consolidation page");
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        Page title should show");
		assertTrue(consolidationRules.consolidationTitle().getText().equalsIgnoreCase("Procedure Code Rules"),"          Page title should be 'Consolidation Rules'");

		logger.info("*** Step 2 Actions: Enter a new Consolidation ID and tab out");
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        Consolidation ID text should shows");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Description Input field should show.");
		String newConsId = randomCharacter.getRandomAlphaNumericString(6);
		String descr = newConsId + "_auto";
		consolidationRules.inputToConsolidationIDInput(newConsId);		
		
		logger.info("*** Step 2 Expected Results: Verify that Consolidation rule page is displayed. Consolidation is read-only field and the remaining fields are empty");
		assertTrue(isElementPresent(consolidationRules.consolidationRulesIdTextBox(), 5),"        Consolidation ID text should shows.");
		assertEquals(consolidationRules.consolidationRulesIdTextBox().getTagName(), "label","        Consolidation ID text field is read-only field");
		assertEquals(consolidationRules.consolidationDescInput().getText(), "","        Consolidation Description Input field should show.");
		
		consolidationRules.inputConsolidationDesc(descr);
		
		logger.info("*** Step 3 Actions: Payor Info Section : Click Add Payor Group icon > Add Payor Group");
		String date = timeStamp.getCurrentDate("MM/dd/yyyy");
		String pyrGrp = payorDao.getPayorGrpToAddSingleFromPyrGrp().getGrpNm();
		consolidationRules.addDataToPyrGrp(pyrGrp, date,this);
		
		logger.info("*** Step 3 Expected Results: Verify that new Payor Group is displayed on Payor Group table");
		assertTrue(isElementPresent(consolidationRules.payorGrpTbl(), 5),"        Payor Group Table should show");
		assertTrue(consolidationRules.checkValueIsAvailableInTable(consolidationRules.payorGrpTbl(),3,pyrGrp),"        Payor Group: " + pyrGrp + " should show.");
		
		logger.info("*** Step 4 Actions: Payor Info Section : Click Add Payor icon > Add Payor");
		String pyrID = daoManagerXifinRpm.getAllPayorID(null).get(1);
		consolidationRules.addDataToPyr(pyrID, date,this);
		
		logger.info("*** Step 4 Expected Results: Verify that new Payor is displayed on Payor table");
		assertTrue(isElementPresent(consolidationRules.payortbl(), 5),"        Payor Table should show");
		assertTrue(consolidationRules.checkValueIsAvailableInTable(consolidationRules.payortbl(),3,pyrID),"        Payor: " + pyrGrp + " should show. ");
		
		logger.info("*** Step 5 Actions: Diagnosis Section > Click on Add icon");
		waitUntilElementPresent(consolidationRules.addDiagnosisBtn(), 30);
		consolidationRules.clickAddDiagnosisBtn();
		
		logger.info("*** Step 5 Expected Result: Verify that Add Record Popup is displayed");
		assertTrue(isElementPresent(consolidationRules.addNewDiagnPopup(), 5),"        Add New Diagnosis Popup should show");

		logger.info("*** Step 6 Actions: Click Diagnosis Search icon > Search Type > Click on Search > Select any Diagnosis Code hyperlink > Click Ok Button");
		assertTrue(isElementPresent(consolidationRules.diagnosisSearchBtn(), 5),"        Search Diagnosis icon should show");
		consolidationRules.clickDiagnosisSearchBtn();
		Thread.sleep(1000);
		
		String parent = switchToPopupWin();
		String diagType = daoManagerXifinRpm.getRandomDiagnosisTypeFromDIAG_CD_TYP(null);
		selectItem(consolidationRules.diagnosisCodeTypeDropDown(), diagType);
		switchToPopupWin();
		assertTrue(isElementPresent(consolidationRules.searchBtnDiagnosisSearch(), 5),"        Search Diagnosis button on Search Diagnosis popup should show");
		consolidationRules.clickDiagnosisSearchBtnOnSearchDiagPopup();
		switchToPopupWin();
		assertTrue(isElementPresent(consolidationRules.DiagCodeSearchResultTable(), 5),"        Search Diagnosis Result should show");
		String diagResCode = consolidationRules.cellOnDiagCodeSearchResult(2,2,"a").getText();
		consolidationRules.cellOnDiagCodeSearchResult(2,4,"div").getText();
		consolidationRules.clickCellOnDiagCodeSearchResult(2, 2, "a");
		
		logger.info("*** Step 6 Expected Result: Verify that the new Diagnosis is displayed in Add record popup");
		switchToParentWin(parent);
		waitUntilElementPresent(consolidationRules.diagnosisCodeID(), 30);
		assertTrue(isElementPresent(consolidationRules.diagnosisCodeID(), 5),"        Search Diagnosis Code input field should show.");
		assertTrue(isElementPresent(consolidationRules.diagnosisDesc(), 5),"        Search Diagnosis Description input field should show.");
		assertTrue(consolidationRules.diagnosisCodeID().getAttribute("value").equalsIgnoreCase(diagResCode), "        Diagnosis Code: "+diagResCode + " should show.");
		
		/*bug for this section : description displayed is not match with search result*/
		//assertTrue(consolidationRules.diagnosisDesc().getAttribute("value").equalsIgnoreCase(diagResDesr), "        This should be "+diagResDesr);		
		consolidationRules.clickOkBtn();
		consolidationRules.clickNonRadioBtn();
		
		logger.info("*** Step 7 Actions: Client Account info Section > Add New Client Account");
		assertTrue(isElementPresent(consolidationRules.addClientAcctInforBtn(), 5),"        Add New Client Account Info button should show");
		String accntTyp = clientDao.getClnAccntTypWhereAccntTypIdIsNotZero().getDescr();
		consolidationRules.addDataToClntAcctTyp(accntTyp,this);
		
		logger.info("*** Step 7 Expected Result: Verify that new Diagnosis Code is displayed");
		assertTrue(getColumnValue(consolidationRules.clientAccountTypeTable(), accntTyp),"        Account Type: " + accntTyp + " should show.");
		
		logger.info("*** Step 8 Actions: Level Section > Add New Procedure Code in Requirements table> Add New Resulting Procedure Code in Link table");
		String procCodeID = daoManagerXifinRpm.getProcedureCodeId(null).get(0);
		consolidationRules.addProcCodeInLevelSection(procCodeID,procCodeID, "1", "1", "1", "1", 1,this);
	
		logger.info("*** Step 8 Expected Result: Verify that the new Procedure Code is displayed in requirement table | New resulting ProcCode is displayed in Links table");
		assertTrue(isElementPresent(consolidationRules.requirementsTbl(1), 5),"        Requirement table should show");
		assertTrue(isElementPresent(consolidationRules.linksTbl(1), 5),"        Links table should show");
		assertTrue(getColumnValue(consolidationRules.requirementsTbl(1), procCodeID),"        Proc Code: "+procCodeID+" should show in Requirement table.");
		assertTrue(getColumnValue(consolidationRules.linksTbl(1), procCodeID),"        Proc Code: "+procCodeID+" should show in Links table.");
		
		logger.info("*** Step 9 Actions: Consolidation Results Section > Add New Consolidation Code with valid ProcCode");
		consolidationRules.addDataToConsolidationResultsTable(procCodeID,this);
		
		logger.info("*** Step 9 Expected Result: Verify that the new Proc Code is displayed in Consolidation Result table");
		assertTrue(isElementPresent(consolidationRules.consolidationResultTbl(), 5),"        Consolidation Result Table should show");
		assertTrue(getColumnValue(consolidationRules.consolidationResultTbl(), procCodeID),"        Proc Code: "+procCodeID+" should show in Consolidation Result table.");
		
		logger.info("*** Step 10 Actions: Click Save button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5),"        Save button should show");
		consolidationRules.clickOnSaveBtn();
		waitUntilElementPresent(consolidationRules.consolidationIDInput(), 30);
		
		logger.info("*** Step 10 Expected Result: Verify that user returns to Consolidation Rule page");
		assertTrue(consolidationRules.consolidationTitle().getText().equalsIgnoreCase("Procedure Code Rules"),"          Page title should be 'Consolidation Rules'");
		assertNotNull(consolidationDao.getConsByConsAbbrev(newConsId), "        Consolidation ID: " + newConsId + " should be added to DB.");
		
		logger.info("*** Step 11 Actions: Clear test data in DB");
		int abbr = consolidationDao.getConsByConsAbbrev(newConsId).getConsId();
		int result = daoManagerXifinRpm.deleteConsolidationByCONS_ID(String.valueOf(abbr), null);
		assertEquals(result,1,"        Failed to delete "+newConsId);
	}
	
	@Test(priority = 1, description = "Add Consolidation rules with valid data for Level section only")
    public void testXPR_520() throws Exception {
		logger.info("*** Testing - XPR - 520 - Consolidation Rules - Add Consolidation rules with valid data for Level section only ***");
		randomCharacter = new RandomCharacter(driver);
		consolidationRules = new ConsolidationRules(driver, wait);
		timeStamp = new TimeStamp();
		
		logger.info("*** Step 1 Actions: Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();		

		logger.info("*** Step 1 Expected Results: Verify that user is logged in Consolidation page");
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        Page title should show");
		assertTrue(consolidationRules.consolidationTitle().getText().equalsIgnoreCase("Procedure Code Rules"),"          Page title should be 'Consolidation Rules'");

		logger.info("*** Step 2 Actions: Enter a new Consolidation ID and tab out");
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        Consolidation Input field should show.");
		String newConsId = randomCharacter.getRandomAlphaNumericString(6);
		consolidationRules.inputToConsolidationIDInput(newConsId);
		
		logger.info("*** Step 2 Expected Results: Verify that Consolidation rule page is displayed. Consolidation is read-only field and remaining fields are empty");
		assertTrue(isElementPresent(consolidationRules.consolidationRulesIdTextBox(), 5),"        Consolidation ID text should shows.");
		assertEquals(consolidationRules.consolidationRulesIdTextBox().getTagName(), "label","        Consolidation ID is read-only field.");
		assertEquals(consolidationRules.consolidationDescInput().getText(), "","        Consolidation description is empty");
		
		logger.info("*** Step 3 Actions: Enter valid Description | Level Section");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation description Input field should show.");
		String descr = newConsId + "_auto";
		consolidationRules.inputConsolidationDesc(descr);
		
		String procCodeID = daoManagerXifinRpm.getProcedureCodeId(null).get(0);
		consolidationRules.addProcCodeInLevelSection(procCodeID,procCodeID, "1", "1", "1", "1", 1,this);
		
		logger.info("*** Step 3 Expected Results: Verify that new Proc Code is displayed in Requirement and Link tables");
		assertTrue(isElementPresent(consolidationRules.requirementsTbl(1), 5),"        Requirement table should show.");
		assertTrue(isElementPresent(consolidationRules.linksTbl(1), 5),"        Links table should show.");
		assertTrue(getColumnValue(consolidationRules.requirementsTbl(1), procCodeID),"        Proc Code: "+procCodeID+" should show in Requirements table.");
		assertTrue(getColumnValue(consolidationRules.linksTbl(1), procCodeID),"        Proc Code: "+procCodeID+" should show in Links table.");
		consolidationRules.addDataToConsolidationResultsTable(procCodeID,this);
		
		logger.info("*** Step 4 Actions: Click Save button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5),"        Save button should show.");
		consolidationRules.clickOnSaveBtn();
		waitUntilElementPresent(consolidationRules.consolidationIDInput(), 30);
		
		logger.info("*** Step 4 Expected Result: Verify that user returns to Consolidation Rule page");
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        Consolidation Rules Page title should show.");
		assertTrue(consolidationRules.consolidationTitle().getText().equalsIgnoreCase("Procedure Code Rules"),"          Page title should be 'Consolidation Rules'.");
		assertNotNull(consolidationDao.getConsByConsAbbrev(newConsId),"        New Consolidation ID: " + newConsId + " should be saved.");
		
		logger.info("*** Step 5 Actions: Clear test data in DB");
		int abbr = consolidationDao.getConsByConsAbbrev(newConsId).getConsId();
		int result = daoManagerXifinRpm.deleteConsolidationByCONS_ID(String.valueOf(abbr), null);
		assertEquals(result,1,"        Failed to Delete "+newConsId);
	}
	
	@Test(priority = 1, description = "Add Consolidation rules with multi consolidation results")
	public void testXPR_524() throws Exception {
		logger.info("===== Testing - testXPR-524 =====");          

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that Consolidation Rules Page shows");
		consolidationRules = new ConsolidationRules(driver, wait);
		waitUntilElementPresent(consolidationRules.consolidationIDInput(), 30);
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation page title should be displayed");		
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"), "        Page Title should be 'Consolidation Rules'");
		
		logger.info("*** Step 2 Actions: - Enter a new Consolidation ID");
		randomCharacter = new RandomCharacter();
		String consolidationID = randomCharacter.getRandomNumericString(5);
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5), "        consolidation ID input field should be shown.");
		consolidationRules.inputToConsolidationIDInput(consolidationID);
		
		logger.info("*** Step 2 Expected Results: - Verify that Consolidation ID is read only field and the remaining fields are empty");
		assertTrue(isElementPresent(consolidationRules.consolidationRulesIdTextBox(), 5),"        Consolidation ID text should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Description Input field should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationPrioritySelect(), 5),"        Priority dropdown list should show.");
		
		logger.info("*** Step 3 Actions: Level section:  Enter minimum matches and units for proc code - add procedure code to Requirements and Links tables");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5), "        Consolidation Description input field should be shown.");
		consolidationRules.inputConsolidationDesc(randomCharacter.getRandomAlphaNumericString(10));
		assertTrue(isElementPresent(consolidationRules.activeCheckbox(), 5),"        Active Checkbox should show.");
		consolidationRules.clickActiveCheckbox();
		//enter ProCode for Requirement table
		String proCode1 = daoManagerXifinRpm.getProcedureCodeId(null).get(0);
		String proCode2 = consolidationRules.getProcedureCodeNotDuplicate(proCode1, daoManagerXifinRpm, null);
		consolidationRules.addProcCodeToRequirementTbl(proCode1, "10", "1", "1", this);
		consolidationRules.addProcCodeToRequirementTbl(proCode2, "10", "1", "1", this);
		consolidationRules.addMinProcMatchAndMinUnit("1", "1", "1");
		consolidationRules.addProcCodeToLinkTbl(proCode1,proCode1, "10", "1", "1", this);
		consolidationRules.addProcCodeToLinkTbl(proCode2,proCode2, "10", "1", "1", this);		
		
		logger.info("*** Step 3 Expected Results: - Verify that the new Proc Code are displayed in Requirements and Links tables");
		assertTrue(consolidationRules.checkValueIsAvailableInTable(consolidationRules.requirementsTbl(1), 3, proCode1), "        Table requirement should contain '"+proCode1+"'");
		assertTrue(consolidationRules.checkValueIsAvailableInTable(consolidationRules.linksTbl(1), 3, proCode1), "        Table link should contain '"+proCode1+"'");
		assertTrue(consolidationRules.checkValueIsAvailableInTable(consolidationRules.requirementsTbl(1), 3, proCode2), "        Table requirement should contain '"+proCode2+"'");
		assertTrue(consolidationRules.checkValueIsAvailableInTable(consolidationRules.linksTbl(1), 3, proCode2), "        Table link should contain '"+proCode2+"'");
		
		logger.info("*** Step 4 Actions: Consolidation Result section: add multi proc codes that included in Levels section");
		consolidationRules.addProcCodeToResultTbl(proCode1, this);
		consolidationRules.addProcCodeToResultTbl(proCode2, this);
		
		logger.info("*** Step 4 Expected Results: Verify that the new proc codes are displayed in Consolidation Results table");
		assertTrue(getColumnValue(consolidationRules.consolidationResultTbl(), proCode1), "        Consolidate Result Table should contain '"+proCode1+"'");
		assertTrue(getColumnValue(consolidationRules.consolidationResultTbl(), proCode2), "        Consolidate Result Table should contain '"+proCode2+"'");
		
		logger.info("*** Step 5 Actions: Click Save button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5),"        Save button should be displayed.");
		consolidationRules.clickOnSaveBtn();
		waitUntilElementPresent(consolidationRules.consolidationIDInput(), 30);
		
		logger.info("*** Step 5 Expected Results: Verify that the new Consolidation rule is added into DB");
		assertNotNull(consolidationDao.getConsByConsAbbrev(consolidationID),"        New Consolidation should be added into DB.");
		
		logger.info("*** Step 6 Actions: Clear test data in DB");
		int cons_id = consolidationDao.getConsByConsAbbrev(consolidationID).getConsId();
		assertEquals(daoManagerXifinRpm.deleteConsolidationByCONS_ID(String.valueOf(cons_id), null),1,"        New Consolidation ID: " + cons_id + " should be deleted in DB.");
	}
		
	@Test(priority = 1, description = "Add Consolidation Rule with multi Proc Code for Requirements and Links table")
	public void testXPR_525() throws Exception {
		logger.info("===== Testing - testXPR-525	 =====");          

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that user login successfully - Load consolidation page is displayed");
		consolidationRules = new ConsolidationRules(driver, wait);
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation page title should be displayed");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"), "        Page Title should be 'Consolidation Rules'");
		
		logger.info("*** Step 2 Actions: - Enter a new Consolidation ID and tab out");
		randomCharacter = new RandomCharacter();
		String consolidationID = randomCharacter.getRandomNumericString(5);
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5), "        Consolidation ID input field should be shown.");
		consolidationRules.inputToConsolidationIDInput(consolidationID);
		
		logger.info("*** Step 2 Expected Results: - Verify that Consolidation Rules page displays properly. Consolidation ID is read only field and the remaining fields are empty");
		assertTrue(isElementPresent(consolidationRules.consolidationRulesIdTextBox(), 5),"        Consolidation ID text should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Description Input field should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationPrioritySelect(), 5),"        Priority dropdown list should show.");
		
		logger.info("*** Step 3 Actions: Level section:  Enter minimum matches and units for proc code - add procedure code in Requirements and Links tables");
		consolidationRules.inputConsolidationDesc(randomCharacter.getRandomAlphaNumericString(10));
		assertTrue(isElementPresent(consolidationRules.activeCheckbox(), 5),"        Active Checkbox should show.");
		consolidationRules.clickActiveCheckbox();
		//enter ProCode for Requirement table
		String proCode1 = daoManagerXifinRpm.getProcedureCodeId(null).get(0);
		String proCode2 = consolidationRules.getProcedureCodeNotDuplicate(proCode1, daoManagerXifinRpm, null);
		consolidationRules.addProcCodeToLevelAndResult(proCode1,proCode1, "10", "1", "1", "1", "1", this);
		consolidationRules.addProcCodeToLevelAndResult(proCode2,proCode2, "10", "1", "1", "1", "1", this);
		
		logger.info("*** Step 3 Expected Results: - Verify that the new Proc Codes are displayed in Requirements and Links tables");
		assertTrue(consolidationRules.checkValueIsAvailableInTable(consolidationRules.requirementsTbl(1), 3, proCode1), "        Table requirements should contain '"+proCode1+"'");
		assertTrue(consolidationRules.checkValueIsAvailableInTable(consolidationRules.linksTbl(1), 3, proCode1), "        Table links should contain '"+proCode1+"'");
		assertTrue(consolidationRules.checkValueIsAvailableInTable(consolidationRules.requirementsTbl(1), 3, proCode2), "        Table requirements should contain '"+proCode2+"'");
		assertTrue(consolidationRules.checkValueIsAvailableInTable(consolidationRules.linksTbl(1), 3, proCode2), "        Table links should contain '"+proCode2+"'");
		
		logger.info("*** Step 4 Actions: Click Save button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5),"        Save button should be displayed.");
		consolidationRules.clickOnSaveBtn();
		waitUntilElementPresent(consolidationRules.consolidationIDInput(), 30);
		
		logger.info("*** Step 4 Expected Results: Verify that the new Consolidation Rule is added into DB");
		assertNotNull(consolidationDao.getConsByConsAbbrev(consolidationID),"        New Consolidation ID: " + consolidationID + " should be added in DB.");
		
		logger.info("*** Step 5 Actions: Clear test data in DB");
		int cons_id = consolidationDao.getConsByConsAbbrev(consolidationID).getConsId();
		assertEquals(daoManagerXifinRpm.deleteConsolidationByCONS_ID(String.valueOf(cons_id), null),1,"        New Consolidation ID: " + cons_id + " should be deleted in DB.");
	}
	
	@Test(priority = 1, description = "Add Consolidation rule without adding Level information")
	public void testXPR_526() throws Exception {
		logger.info("===== Testing - testXPR-526 =====");          

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that Consolidation Rules Page shows");
		consolidationRules = new ConsolidationRules(driver, wait);
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation page title should be displayed");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"), "        Page Title should be 'Consolidation Rules'");
		
		logger.info("*** Step 2 Actions: - Enter a new Consolidation ID and tab out");
		randomCharacter = new RandomCharacter();
		String consolidationID = randomCharacter.getRandomNumericString(5);
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5), "        Consolidation ID input field should be shown.");
		consolidationRules.inputToConsolidationIDInput(consolidationID);
		
		logger.info("*** Step 2 Expected Results: - Verify that the Consolidation Rules page displays properly. Consolidation ID is read only field and the remaining fields are empty");
		assertTrue(isElementPresent(consolidationRules.consolidationRulesIdTextBox(), 5),"        Consolidation ID text should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Description Input field should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationPrioritySelect(), 5),"        Priority dropdown list should show.");
		
		logger.info("*** Step 3 Actions: Level section:  enter minimum matches and units for proc code. Click Save button");		
		consolidationRules.inputConsolidationDesc(randomCharacter.getRandomAlphaNumericString(10));
		assertTrue(isElementPresent(consolidationRules.activeCheckbox(), 5),"        Active Checkbox should show.");
		consolidationRules.clickActiveCheckbox();
		assertTrue(isElementPresent(consolidationRules.minProcCodeMatch(1), 5),"        Min Procedure Code Matches input field should be displayed.");
		consolidationRules.inputMinProCodeMatch("1",1);
		assertTrue(isElementPresent(consolidationRules.minUnit(1), 5),"        Min Units input field should be displayed.");
		consolidationRules.inputMinUnit("1",1);
		
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5),"        Save button should be displayed.");
		consolidationRules.clickOnSaveBtn();
		
		logger.info("*** Step 3 Expected Results: Verify that the error message : At least one requirement is required at level 1 - At least one Link is required at level 1");
		assertTrue(consolidationRules.errorMessageLbl().getText().contains("At least one Requirement is required at level 1\nAt least one Link is required at level 1"),"        Error message table should be displayed: At least one Requirement is required at level 1");
	}
	
	@Test(priority = 1, description = "Add Consolidation Rules with Proc Code in Requirements table is not in Links table")
	public void testXPR_535() throws Exception {
		logger.info("===== Testing - testXPR-535 =====");          

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that Consolidation Rules Page shows");
		consolidationRules = new ConsolidationRules(driver, wait);
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation page title should be displayed");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"), "        Page Title should be 'Consolidation Rules'");
		
		logger.info("*** Step 2 Actions: - Enter a new Consolidation ID and tab out");
		randomCharacter = new RandomCharacter();
		String consolidationID = randomCharacter.getRandomNumericString(5);
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5), "        Consolidation ID input field should be shown");
		consolidationRules.inputToConsolidationIDInput(consolidationID);
		
		logger.info("*** Step 2 Expected Results: - Verify that Consolidation Rules page displays properly. Consolidation ID is read only field and the remaining fields are empty");
		assertTrue(isElementPresent(consolidationRules.consolidationRulesIdTextBox(), 5),"        Consolidation ID text should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Description Input field should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationPrioritySelect(), 5),"        Priority dropdown list should show.");
		
		logger.info("*** Step 3 Actions: Level section:  Enter minimum matches and units for proc code. Click Save button");		
		consolidationRules.inputConsolidationDesc(randomCharacter.getRandomAlphaNumericString(10));
		assertTrue(isElementPresent(consolidationRules.activeCheckbox(), 5),"        Active Checkbox should show.");
		consolidationRules.clickActiveCheckbox();
		//enter ProCode for Requirement table
		String proCode1 = daoManagerXifinRpm.getProcedureCodeId(null).get(0);
		String proCode2 = consolidationRules.getProcedureCodeNotDuplicate(proCode1, daoManagerXifinRpm, null);
		consolidationRules.addProcCodeToRequirementTbl(proCode1, "10", "1", "1", this);
		consolidationRules.addMinProcMatchAndMinUnit("1", "1", "1");
		consolidationRules.addProcCodeToLinkTbl(proCode2,proCode2, "10", "1", "1", this);		
		
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5));
		consolidationRules.clickOnSaveBtn();
		
		logger.info("*** Step 3 Expected Results: Verify that the error message: procedure code requirement is not found in the link table shows");
		assertTrue(consolidationRules.errorMessageLbl().getText().contains(""+proCode1+"- procedure code requirement is not found in the link table."),"        Error message: procedure code requirement is not found in the link table shows.");
	}
	
	@Test(priority = 1, description ="Verify Help Icon")
	public void testXPR_556() throws Exception {
		logger.info("===== Testing - testXPR_556 =====");
		
		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that Consolidation Rules Page shows");
		consolidationRules = new ConsolidationRules(driver, wait);
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation Rules page title should be displayed");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"),"        Page Title should be 'Consolidation Rules'");							

		logger.info("*** Step 2 Actions: - Click Help Icon on Load Consolidation Page");
		assertTrue(isElementPresent(consolidationRules.loadPageHelpIcon(), 5),"        Help Icon should show.");
		consolidationRules.clickLoadPageHelpIcon();
		Thread.sleep(1000);
		
		logger.info("*** Step 2 Expected Results: - Verify that Help page is displayed");
		String parent = switchToPopupWin();
		String URL = driver.getCurrentUrl();
		assertTrue(URL.contains("p_consolidation_load_rule.htm"),"        Consolidation Rules - Load Consolidation help popup should be opened.");
		driver.close();
		switchToParentWin(parent);
	}
	
	@Test(priority = 1, description ="Add Consolidation - Test Consolidation Rules with valid AccnId")
	public void testXPR_557() throws Exception {
		logger.info("===== Testing - testXPR_557 =====");
		
		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that Consolidation Rules Page shows");
		consolidationRules = new ConsolidationRules(driver, wait);
		randomCharacter = new RandomCharacter();
		listUtil = new ListUtil();
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation Rules page title should be displayed");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"),"        Page Title should be 'Consolidation Rules'");							

		logger.info("*** Step 2 Actions: - Enter a new Consolidation ID and tab out");
		String consolId = randomCharacter.getRandomNumericString(5);
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation Id Input field should be displayed.");
		consolidationRules.inputToConsolidationIDInput(consolId);
		
		logger.info("*** Step 2 Expected Results: - Verify that the Consolidation Rules Page Info display properly");
		assertTrue(isElementPresent(consolidationRules.consolidationRulesIdTextBox(), 5),"        Consolidation ID text should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Description Input field should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationPrioritySelect(), 5),"        Priority dropdown list should show.");
		assertTrue(isElementPresent(consolidationRules.activeCheckbox(), 5),"        Active checkbox should show.");
		assertEquals(consolId,consolidationRules.consolidationRulesIdTextBox().getText(),"        Consolidation ID: " + consolId + " should show.");
		
		logger.info("*** Step 3 Actions: - Footer menu: Enter a valid AccnId. Click Test Rules Button");
		String accnId = accessionDao.getAccnFromAccnTest().getAccnId();
		assertTrue(isElementPresent(consolidationRules.accnIDSearch(), 5),"        Accession id search input field should be displayed.");
		consolidationRules.inputToAccnIDSearch(accnId);
		assertTrue(isElementPresent(consolidationRules.testRuleBtn(), 5),"        The Test Rule button should be displayed.");
		consolidationRules.clickTestRuleBtn();
		Thread.sleep(1000);
		String parent = switchToPopupWin();
		assertTrue(isElementPresent(consolidationRules.testConsolRulesTblAccnIdLbl(), 5),"        Accession ID text in Test Consolidation Rules page should show.");
		assertEquals(consolidationRules.testConsolRulesTblAccnIdLbl().getText(), accnId,"        Accession ID: " + accnId + " should show in Test Consolidation Rules page.");
		
		logger.info("*** Step 3 Expected Results: - Test Consolidation Rules page displays properly");
		List<String> infoDB = daoManagerXifinRpm.getTestCodeFromACCNTESTByAccnId(accnId, null);
		List<String> infoUI = consolidationRules.getListValueOnTable("tbl_consolidationAccnTests", 2);
		boolean compare = listUtil.compareLists(infoDB, infoUI);
		assertTrue(compare,"        Test Consolidation Rule page Info for Accession ID: " + accnId + " should display properly.");
		driver.close();
		switchToParentWin(parent);
	}
	
	@Test(priority = 1, description ="Add Consolidation Rule with Proc Code in Links table but it's not in Result table")
	public void testXPR_573() throws Exception {
		logger.info("===== Testing - testXPR_573 =====");
		
		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Consolidation Rules page is displayed with correct page title");
		consolidationRules = new ConsolidationRules(driver, wait);
		randomCharacter = new RandomCharacter();
		listUtil = new ListUtil();
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation Rules page title should be displayed");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"),"        Page Title should be 'Consolidation Rules'");							

		logger.info("*** Step 2 Actions: - Enter a new Consolidation ID and tab out");
		String consolId = randomCharacter.getRandomNumericString(5);
		assertTrue(isElementPresent(consolidationRules.consolidationIDInput(), 5),"        The Consolidation Id Input field should be displayed.");
		consolidationRules.inputToConsolidationIDInput(consolId);
		
		logger.info("*** Step 2 Expected Results: - Verify that Consolidation Rules Page shows");
		assertTrue(isElementPresent(consolidationRules.consolidationRulesIdTextBox(), 5),"        Consolidation ID text should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationDescInput(), 5),"        Consolidation Description Input field should show.");
		assertTrue(isElementPresent(consolidationRules.consolidationPrioritySelect(), 5),"        Priority dropdown list should show.");
		assertTrue(isElementPresent(consolidationRules.activeCheckbox(), 5),"        Active checkbox should show.");
		assertEquals(consolId,consolidationRules.consolidationRulesIdTextBox().getText(),"        Consolidation ID: " + consolId + " should show.");
		
		logger.info("*** Step 3 Actions: - Enter valid data for Description, minimum for Procode that mathed and unit. Add the same Proc Code into Requirements and Links table");
		String desc = randomCharacter.getRandomAlphaNumericString(5);
		consolidationRules.inputConsolidationDesc(desc);
		String procId = daoManagerXifinRpm.getProcedureCodeId(null).get(0);
		String min = "1";
		String minServ = randomCharacter.getNonZeroRandomNumericString(1);
		String maxServ = minServ + randomCharacter.getRandomNumericString(1);
		consolidationRules.addProcCodeToRequirementTbl(procId, maxServ, minServ, "1", this);
		consolidationRules.addMinProcMatchAndMinUnit(min, min, "1");
		consolidationRules.addProcCodeToLinkTbl(procId,procId, maxServ, minServ, "1", this);
		
		logger.info("*** Step 3 Expected Results: - Verify that the new Proc Code is displayed in requirements and Link table");
		assertTrue(getColumnValue(consolidationRules.requirementsTbl(1), procId),"        Proc Code: " + procId + " should be displayed in Requirements table.");
		assertTrue(getColumnValue(consolidationRules.linksTbl(1), procId),"        Proc Code: " + procId + " should be displayed in Links table.");
		
		logger.info("*** Step 4 Actions: - Click Save button");
		assertTrue(isElementPresent(consolidationRules.saveBtn(), 5),"        Save button should show.");		
		consolidationRules.clickOnSaveBtn();
		
		logger.info("*** Step 4 Expected Results: - Verify that the Error message: 'xxx-xxx procedure code link is not found in the result at table 1' shows");
		assertTrue(isElementPresent(consolidationRules.errorMessageLbl(), 5),"        Error Message should show.");
		assertTrue(consolidationRules.errorMessageLbl().getText().contains(""+procId+"-"+procId+" procedure code link is not found"), "        Error Message: " + "'"+procId+"-"+procId+" procedure code link is not found in the results table. should show.");
	}

	@Test(priority = 1, description ="Search Consolidation Rule - Search With all data")
	public void testXPR_544() throws Exception {
		logger.info("===== Testing - testXPR_544 =====");
		
		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		consolidationRules = new ConsolidationRules(driver, wait);
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Consolidation Rules page is displayed with correct page title");
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation Rules page title should be displayed");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"),"        Page Title should be 'Consolidation Rules'");							

		logger.info("*** Step 2 Actions: - Click Consolidation ID search icon");
		assertTrue(isElementPresent(consolidationRules.searchConsBtn(), 5),"        Consolidation Search button should be displayed.");
		consolidationRules.clickSearchConsBtn();
		Thread.sleep(1000);
		String parent = switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Consolidation Search page is displayed");
		assertTrue(consolidationRules.consolidationSearchPopupHeader().getText().trim().contains("Consolidation Search"),"        Page Title should be 'Consolidation Search'");
		
		logger.info("*** Step 3 Actions: - Enter * in Consolidation ID input field - Click Search button");
		assertTrue(isElementPresent(consolidationRules.searchConsolidationIDInput(), 5),"        Consolidation ID Input field should be displayed.");
		consolidationRules.inputToSearchConsolidationIDInput("*");
		assertTrue(isElementPresent(consolidationRules.searchSearchPageBtn(), 5),"        Search button should be displayed.");
		consolidationRules.clickSearchSearchPageBtn();
		
		logger.info("*** Step 3 Expected Results: - Verify that the active Consolidation IDs are displayed");
		assertTrue(isElementPresent(consolidationRules.consolSearchResultTbl(), 5),"        Consolidation search results should be displayed.");
		String numOfConsDB = daoManagerXifinRpm.getConsolidationCountsFromCONSByDeletedInUseFlags(0, 1, null);
		String totalCountsInUI = consolidationRules.totalConsolidationResultLabel().getText().split(" ")[5]; 
		assertEquals(totalCountsInUI, numOfConsDB, "        Total Consolidation IDs should be " + numOfConsDB);
		
		driver.close();
		switchToParentWin(parent);
	}
	
	 @Test(priority = 1, description ="Search Consolidation rule with valid Description")
	 public void testXPR_545() throws Exception {
		 logger.info("===== Testing - testXPR_545 =====");
		  
		 logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		 navigation = new MenuNavigation(driver, config);
		 navigation.navigateToConsolidationRulesPage();
		  
		 logger.info("*** Step 1 Expected Results: - Verify that the Consolidation Rules page is displayed with correct page title");
		 consolidationRules = new ConsolidationRules(driver, wait);
		 assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation Rules page title should be displayed");
		 assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"),"        Page Title should be 'Consolidation Rules'");       
		
		 logger.info("*** Step 2 Actions: - Click on Consolidation ID search icon");
		 assertTrue(isElementPresent(consolidationRules.searchConsBtn(), 5),"        Consolidation Search button should be displayed");
		 consolidationRules.clickSearchConsBtn();
		 Thread.sleep(1000);
		 String parent = switchToPopupWin();
		  
		 logger.info("*** Step 2 Expected Results: - Verify that the Consolidation Search page is displayed");
		 assertTrue(consolidationRules.consolidationSearchPopupHeader().getText().trim().contains("Consolidation Search"),"        Page Title should be 'Consolidation Search'");
		  
		 logger.info("*** Step 3 Actions: - Enter valid Description - Click on Search button");
		 assertTrue(isElementPresent(consolidationRules.searchDescriptionInput(), 5),"        Consolidation Description textbox should be displayed");
		 String description = consolidationDao.getConsHavingConsAbbrevNotNull().getDescr();
		 consolidationRules.inputToSearchDescriptionInput(description);
		  
		 assertTrue(isElementPresent(consolidationRules.searchSearchPageBtn(), 5),"        Search button should be displayed");
		 consolidationRules.clickSearchSearchPageBtn();
		  
		 logger.info("*** Step 3 Expected Results: - Verify that the Consolidation ID that have description matched the search condition is displayed");
		 assertTrue(isElementPresent(consolidationRules.consolSearchResultTbl(), 5),"        Consolidation search results should be displayed");
		 String numOfConsDB = daoManagerXifinRpm.countAllConsInfoFromCONSByDesc(description, null);
		 String totalCons = consolidationRules.verifySearchResult(numOfConsDB);
		 
		 assertEquals(numOfConsDB,totalCons,"        Total counts of Consolidation IDs should be "+totalCons);
		 driver.close();
		 switchToParentWin(parent);
	 }
	
	@Test(priority = 1, description ="Search Consolidation rules with Payor Group")
	public void testXPR_546() throws Exception {
		logger.info("===== Testing - testXPR_546 =====");
		
		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		consolidationRules = new ConsolidationRules(driver, wait);
		randomCharacter = new RandomCharacter();
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Consolidation Rules page is displayed with correct page title");
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation Rules page title should be displayed");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"),"        Page Title should be 'Consolidation Rules'");							

		logger.info("*** Step 2 Actions: - Click on Consolidation ID search icon");
		assertTrue(isElementPresent(consolidationRules.searchConsBtn(), 5),"        Consolidation Search button should be displayed");
		consolidationRules.clickSearchConsBtn();
		Thread.sleep(1000);
		String parent = switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Consolidation Search page is displayed");
		assertTrue(consolidationRules.consolidationSearchPopupHeader().getText().trim().contains("Consolidation Search"),"        Page Title should be 'Consolidation Search'");
		
		logger.info("*** Step 3 Actions: - Select Payor Group in dropdownlist - Click on Search button");
		String payorGrpName = payorDao.getPyrGrpFromConsPyrGrpWhereGrpNmIsNotNull().getGrpNm();
		assertTrue(isElementPresent(consolidationRules.searchPayorGroupCombobox(), 5),"        Consolidation Description textbox should be displayed");
		selectItemByVal(consolidationRules.searchPayorGroupCombobox(), payorGrpName);
		assertTrue(isElementPresent(consolidationRules.searchSearchPageBtn(), 5),"        Search button should be displayed");
		consolidationRules.clickSearchSearchPageBtn();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Consolidation ID that have description matched the search condition is displayed");
		String numOfConsDB = daoManagerXifinRpm.countAllConsInfoFromCONSByPayorGrp(payorGrpName, null);
		String totalCons = consolidationRules.verifySearchResult(numOfConsDB);
		assertTrue(isElementPresent(consolidationRules.consolSearchResultTbl(), 5),"        Consolidation search results should be displayed");
		assertEquals(numOfConsDB,totalCons,"        Total counts of Consolidation IDs should be "+totalCons);
		driver.close();
		switchToParentWin(parent);
	}
	
	@Test(priority = 1, description ="Search Consolidation rule with input valid consolidated Proc Code")
	public void testXPR_549() throws Exception {
		logger.info("===== Testing - testXPR_549 =====");
		
		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		consolidationRules = new ConsolidationRules(driver, wait);
		randomCharacter = new RandomCharacter();
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: - Verify that the Consolidation Rules page is displayed with correct page title");
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        The Consolidation Rules page title should be displayed");
		assertTrue(consolidationRules.consolidationTitle().getText().trim().contains("Procedure Code Rules"),"        Page Title should be 'Consolidation Rules'");							

		logger.info("*** Step 2 Actions: - Click Consolidation ID search icon");
		assertTrue(isElementPresent(consolidationRules.searchConsBtn(), 5),"        Consolidation Search button should be displayed");
		consolidationRules.clickSearchConsBtn();
		Thread.sleep(1000);
		String parent = switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Consolidation Search page is displayed");
		assertTrue(consolidationRules.consolidationSearchPopupHeader().getText().trim().contains("Consolidation Search"),"        Page Title should be 'Consolidation Search'");
		
		logger.info("*** Step 3 Actions: - Enter Consolidated Proc Code - Click Search button");
		String proCode = daoManagerXifinRpm.getProcedureCodeId(null).get(0);
		assertTrue(isElementPresent(consolidationRules.searchConsolidatedProcedureCodeInput(), 5),"        Consolidation Description textbox should be displayed");
		consolidationRules.inputSearchConsolidatedProcedureCode(proCode);
		assertTrue(isElementPresent(consolidationRules.searchSearchPageBtn(), 5),"        Search button should be displayed");
		consolidationRules.clickSearchSearchPageBtn();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Consolidation ID that have description matched the search condition is displayed");
		String numOfConsDB = daoManagerXifinRpm.countAllConsInfoFromCONS_PROC_REQ_PROCByProCodeID(proCode, null);
		String totalCons = consolidationRules.verifySearchResult(numOfConsDB);
		assertTrue(isElementPresent(consolidationRules.consolSearchResultTbl(), 5),"        Consolidation search results should be displayed");
		assertEquals(numOfConsDB,totalCons,"        Total counts of the Consolidation IDs should be "+totalCons);
		driver.close();
		switchToParentWin(parent);
	}

	@Test(priority = 1, description = "Search Consolidation rule with Deleted radio button is checked")
    public void testXPR_550() throws Exception {
		logger.info("===== Testing - testXPR_550 =====");
		
		logger.info("*** Step 1 Actions: Log into Xifin Portal with SSO username and password");
		randomCharacter = new RandomCharacter(driver);
		consolidationRules = new ConsolidationRules(driver, wait);
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: Verify that user is logged in Consolidation page");
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        Page title should show");
		assertTrue(consolidationRules.consolidationTitle().getText().contains("Procedure Code Rules"),"          Page title should be 'Consolidation Rules'");

		logger.info("*** Step 2 Actions: Click Search Consolidation ID icon");
		assertTrue(isElementPresent(consolidationRules.searchConsBtn(), 5),"        Search Consolidation icon should show.");
		consolidationRules.clickSearchConsBtn();
		Thread.sleep(1000);
		String parent = switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: Verify that the Search Consolidation page is displayed");
		assertTrue(isElementPresent(consolidationRules.consolidationSearchPopupHeader(), 5),"        Consolidation Search Popup is displayed");
		assertTrue(consolidationRules.consolidationSearchPopupHeader().getText().equalsIgnoreCase("Consolidation Search"),"          Page title should be 'Consolidation Search'");
		
		logger.info("*** Step 3 Actions: Enter * to Consolidation ID field > Check on Delete radio button > Click on Search Button");
		assertTrue(isElementPresent(consolidationRules.searchConsolidationIDInput(), 5),"        Consolidation Search Input field should show.");
		consolidationRules.inputToSearchConsolidationIDInput("*");
		assertTrue(isElementPresent(consolidationRules.deleteRuleDeletedCheckbox(), 5),"        Delete radio button should show.");
		consolidationRules.clickDeleteRuleDeletedCheckbox();
		assertTrue(isElementPresent(consolidationRules.searchSearchPageBtn(), 5),"        Search Consolidation button should show.");
		consolidationRules.clickSearchSearchPageBtn();
		
		logger.info("*** Step 3 Expected Results: Verify that All Consolidation IDs that marked B_delete = 1 displays");
		String totalConsDB = daoManagerXifinRpm.getConsolidationCountsFromCONSByDeletedInUseFlags(1,1,null);
		String totalCons = consolidationRules.verifySearchResult(totalConsDB);
		assertEquals(totalConsDB,totalCons,"        Total counts  of Consolidation IDs should be "+totalCons);
		driver.close();
		switchToParentWin(parent);
	}
	
	@Test(priority = 1, description = "Search consolidation rule with Inactive radio button is checked")
    public void testXPR_552() throws Exception {
		logger.info("===== Testing - testXPR_552 =====");
		
		logger.info("*** Step 1 Actions: Log into Xifin Portal with SSO username and password");
		randomCharacter = new RandomCharacter(driver);
		consolidationRules = new ConsolidationRules(driver, wait);
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: Verify that user is logged into Consolidation page");
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        Page title should show");
		assertTrue(consolidationRules.consolidationTitle().getText().contains("Procedure Code Rules"),"          Page title should be 'Consolidation Rules'");

		logger.info("*** Step 2 Actions: Click Search Consolidation ID icon");
		assertTrue(isElementPresent(consolidationRules.searchConsBtn(), 5),"        Search Consolidation icon should show.");
		consolidationRules.clickSearchConsBtn();
		Thread.sleep(1000);
		String parent = switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: Verify that the Search Consolidation page is displayed");
		assertTrue(consolidationRules.consolidationSearchPopupHeader().getText().equalsIgnoreCase("Consolidation Search"),"          Page title should be 'Consolidation Search'");
		
		logger.info("*** Step 3 Actions: Enter * in Consolidation ID input field > Check on InActive radio button > Click on Search Button");
		assertTrue(isElementPresent(consolidationRules.searchConsolidationIDInput(), 5),"        Consolidation Search Input field should show.");
		consolidationRules.inputToSearchConsolidationIDInput("*");
		assertTrue(isElementPresent(consolidationRules.ActiveRuleInactiveCheckbox(), 5),"        Inactive radio button should show.");
		consolidationRules.clickActiveRuleInactiveCheckbox();
		assertTrue(isElementPresent(consolidationRules.searchSearchPageBtn(), 5),"        Search Consolidation button should show.");
		consolidationRules.clickSearchSearchPageBtn();
		
		logger.info("*** Step 3 Expected Results: Verify that Consolidation IDs that have B_delete = 1 displayed");
		String totalConsDB = daoManagerXifinRpm.getConsolidationCountsFromCONSByDeletedInUseFlags(0,0,null);
		String totalCons = consolidationRules.verifySearchResult(totalConsDB);
		assertEquals(totalConsDB,totalCons,"        Total counts of the Consolidation IDs should be "+totalCons);
		driver.close();
		switchToParentWin(parent);
	}
	
	
	@Test(priority = 1, description = "Search Consolidation rule - Reset data")
    public void testXPR_554() throws Exception {
		logger.info("===== Testing - testXPR_554 =====");
		
		logger.info("*** Step 1 Actions: Log into Xifin Portal with SSO username and password");
		randomCharacter = new RandomCharacter(driver);
		consolidationRules = new ConsolidationRules(driver, wait);
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: Verify that user is logged in Consolidation page");
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        Consolidation Rules Page title should show.");
		assertTrue(consolidationRules.consolidationTitle().getText().contains("Procedure Code Rules"),"          Consolidation Rules Page title should be 'Consolidation Rules'.");

		logger.info("*** Step 2 Actions: Click Search Consolidation ID icon");
		assertTrue(isElementPresent(consolidationRules.searchConsBtn(), 5),"        Search Consolidation icon should show.");
		consolidationRules.clickSearchConsBtn();
		Thread.sleep(1000);
		String parent = switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: Verify that the Search Consolidation page is displayed");
		assertTrue(isElementPresent(consolidationRules.consolidationSearchPopupHeader(), 5),"        Consolidation Search Popup is displayed");
		assertTrue(consolidationRules.consolidationSearchPopupHeader().getText().equalsIgnoreCase("Consolidation Search"),"          Page title should be 'Consolidation Search'");
		
		logger.info("*** Step 3 Actions: Enter data in all input fields > Click Reset button");
		String data = "AutomationTesting";
		String pyrGrp = payorDao.getPayorGrpToAddSingleFromPyrGrp().getGrpNm();
		assertTrue(isElementPresent(consolidationRules.searchConsolidationIDInput(), 5),"        Consolidation Search Input field should show.");
		assertTrue(isElementPresent(consolidationRules.searchDescriptionInput(), 5),"        Consolidation Description Search Input field should show.");
		assertTrue(isElementPresent(consolidationRules.searchPayorGroupCombobox(), 5),"        Payor Group Search Dropdown should show.");
		assertTrue(isElementPresent(consolidationRules.searchPayorIDInput(), 5),"        Payor ID Search input field should show.");
		assertTrue(isElementPresent(consolidationRules.searchRequiredProcedureCodeInput(), 5),"        Search Required Procedure Code Input field should show.");
		assertTrue(isElementPresent(consolidationRules.searchConsolidatedProcedureCodeInput(), 5),"        Search Consolidated Procedure Code Input field should show.");
		consolidationRules.inputConsolidationDescriptionSearchInput(data);
		consolidationRules.inputToSearchConsolidationIDInput(data);
		selectItemByVal(consolidationRules.searchPayorGroupCombobox(), pyrGrp);
		consolidationRules.inputSearchPayorID(data);
		consolidationRules.inputSearchRequiredProcedureCode(data);
		consolidationRules.inputSearchConsolidatedProcedureCode(data);

		assertTrue(isElementPresent(consolidationRules.resetSearchPageBtn(), 5),"        Reset Search button should show.");
		consolidationRules.clickResetSearchPageBtn();
		
		logger.info("*** Step 3 Expected Results: Verify that all input fields will cleared");
		assertEquals(consolidationRules.searchConsolidationIDInput().getText(), "", "        Consolidation Search Input field should be cleared.");
		assertEquals(consolidationRules.searchDescriptionInput().getText(), "", "        Consolidation Description Search Input field should be cleared.");
		isDropdownItemSelected(consolidationRules.searchPayorGroupCombobox(), "");
		assertEquals(consolidationRules.searchPayorIDInput().getText(), "", "       Payor ID Search input field should be cleared.");
		assertEquals(consolidationRules.searchRequiredProcedureCodeInput().getText(), "", "        Search Required Procedure Code Input field should be cleared.");
		assertEquals(consolidationRules.searchConsolidatedProcedureCodeInput().getText(), "", "        Search Consolidated Procedure Code Input field should be cleared.");
		
		driver.close();
		switchToParentWin(parent);
	}
	
	@Test(priority = 1, description = "Search Consolidation rule - Help icon")
    public void testXPR_555() throws Exception {
		logger.info("===== Testing - testXPR_555 =====");
		
		logger.info("*** Step 1 Actions: Log into Xifin Portal with SSO username and password");
		randomCharacter = new RandomCharacter(driver);
		consolidationRules = new ConsolidationRules(driver, wait);
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToConsolidationRulesPage();

		logger.info("*** Step 1 Expected Results: Verify that user is logged in Consolidation Rules page");
		assertTrue(isElementPresent(consolidationRules.consolidationTitle(), 5),"        Consolidation Rules Page title should show");
		assertTrue(consolidationRules.consolidationTitle().getText().contains("Procedure Code Rules"),"          Page title should be 'Consolidation Rules'");

		logger.info("*** Step 2 Actions: Click Search Consolidation ID icon");
		assertTrue(isElementPresent(consolidationRules.searchConsBtn(), 5),"        Search Consolidation icon should show.");
		consolidationRules.clickSearchConsBtn();
		Thread.sleep(1000);
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: Verify that the Search Consolidation page is displayed");
		assertTrue(isElementPresent(consolidationRules.consolidationSearchPopupHeader(), 5),"        Consolidation Search Popup is displayed");
		assertTrue(consolidationRules.consolidationSearchPopupHeader().getText().equalsIgnoreCase("Consolidation Search"),"          Page title should be 'Consolidation Search'");
		
		logger.info("*** Step 3 Actions: Click Page Help Link");
		assertTrue(isElementPresent(consolidationRules.pageHelpLink(), 5),"        Page Help Link should show.");
		consolidationRules.clickPageHelpLink();
		Thread.sleep(1000);
		String parent = switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: Verify that the Help page for Consolidation Search is displayed");
		String URL = driver.getCurrentUrl();
		assertTrue(URL.contains("searches/p_consolidation_search.htm"),"        Help Page for Consolidation Search should show.");
		driver.close();
		switchToParentWin(parent);
	}
}
