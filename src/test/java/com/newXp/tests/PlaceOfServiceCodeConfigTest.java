package com.newXp.tests;

import com.mbasys.mars.ejb.entity.fac.Fac;
import com.mbasys.mars.ejb.entity.mod.Mod;
import com.mbasys.mars.ejb.entity.posOverride.PosOverride;
import com.mbasys.mars.ejb.entity.posTyp.PosTyp;
import com.mbasys.mars.ejb.entity.ptLocTyp.PtLocTyp;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.svcTyp.SvcTyp;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.overall.fileMaintenance.placeOfServiceCodeConfig.PlaceOfServiceCodeConfig;
import com.overall.menu.MenuNavigation;
import com.overall.search.PayorSearch;
import com.overall.search.PayorSearchResults;
import com.overall.search.ProcCodeSearch;
import com.overall.search.ProcCodeSearchResults;
import com.overall.utils.XifinPortalUtils;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.utils.SeleniumBaseTest;
import jodd.typeconverter.Convert;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class PlaceOfServiceCodeConfigTest extends SeleniumBaseTest {
	
	private static final String EMPTY = "";
	private static final String PAYOR_SEARCH_TITLE = "Payor Search";
	private static final String TBL_POSCODE_CONFIG = "tbl_posCodeConfig";
	private static final String PROCEDURE_CODE_SEARCH_TITLE = "Procedure Code Search";
	private static final String PLACE_OF_SERVICE_CODE_CONFIGURATION_TITLE = "Place of Service Code Configuration";
	private static final String WARNING_MESSAGE = "Changes have been made to this accession.  Are you sure you want to reset the page?";

	private PayorSearch payorSearch;
	private ProcCodeSearch procCodeSearch;
	private XifinPortalUtils xifinPortalUtils;
	private PayorSearchResults payorSearchResults;
	private ProcCodeSearchResults procCodeSearchResults;
	private PlaceOfServiceCodeConfig placeOfServiceCodeConfig;

	@BeforeMethod(alwaysRun = true)
    @Parameters({ "ssoXpUsername", "ssoXpPassword" })
    public void beforeMethod(String ssoXpUsername, String ssoXpPassword, Method method) {
        try {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            xifinPortalUtils = new XifinPortalUtils(driver);
            logIntoSso(ssoXpUsername, ssoXpPassword);
            MenuNavigation menuNavigation = new MenuNavigation(driver, config);
	        menuNavigation.navigateToPlaceOfServiceCodeConfigPage();
        } catch (Exception e) {
            logger.error("Error running BeforeMethod", e);
        }
    }

	/**
	 * Test Script
	 */
	
	@Test(priority = 1, description = "Verify Place Of Service Code Configuration grid ")
	public void testXPR_908() throws Throwable {		
		logger.info("===== Testing - testXPR_908 =====");  	

		placeOfServiceCodeConfig = new PlaceOfServiceCodeConfig(driver);
		
		logger.info("*** Step 1 Expected Result: User login successfully, the Place Of Service code Configuration page is displayed.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();

		logger.info("*** Step 2 Action: Focus on Place Of Service Code configuration grid.");
		int totalRecord = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		
		logger.info("*** Step 2 Expected Result: The number of record in this grid is display that matched with table POS_OVERRIDE.");
		List<PosOverride> overrides = posDao.getPosOverrideBySeqIdIsNotNull();
		assertEquals(totalRecord, overrides.size(),"        The ProcCode Detail Title screen is displayed.");
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Verify Place Of Service Code Configuration grid ")
	public void testXPR_909() throws Throwable {		
		logger.info("===== Testing - testXPR_908 =====");  	

		placeOfServiceCodeConfig = new PlaceOfServiceCodeConfig(driver);
		
		logger.info("*** Step 1 Expected Result: User login successfully, the Place Of Service code Configuration page is displayed.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();
		int totalOldRecord = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));

		logger.info("*** Step 2 Action: Click on Add icon.");
		clickOnAddBtn();
		
		logger.info("*** Step 2 Expected Result: Add record popup is displayed.");
		verifyTheAddRecordPopupIsDisplayed();

		logger.info("*** Step 3 Action: Fill all fields with condition following test case, click on OK button.");
		String payorId = daoManagerXifinRpm.getPayorID(testDb);
		placeOfServiceCodeConfig.enterPayorIdInput(payorId);
		String procCode = daoManagerPlatform.getProcIdFromPROCCD(testDb);
		placeOfServiceCodeConfig.enterProcCodeInput(procCode);
		PosTyp posTyp = posDao.getPosTyp();
		String posType = posTyp.getPosTypId();
		List<String> ptTyp  = daoManagerAccnWS.getRandomPatientTypeFromPT_TYP(testDb);
		SvcTyp svcTyp = serviceDao.getSvcTyp();
		Fac fac = facilityDao.getRandomFac();
		List<String> clnAccntTyp = daoManagerXifinRpm.getClnAccntTypFromCLNACCNTTYPI(testDb);
		Mod mod = modDao.getMod();
		PtLocTyp ptLocTyp = patientDao.getRandomDataFromPtLocTyp();
		enterDataToAddRecordPlaceOfServiceCode(EMPTY, fac.getAbbrv() + " - " + fac.getName(), svcTyp.getDescr(),
				mod.getModId(), clnAccntTyp.get(1), ptLocTyp.getDescr(), ptTyp.get(0), posType);

		logger.info("*** Step 3 Expected Result: New record is shown in the grid.");
		waitUntilElementPresent(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl(), 30);
		int totalNewRecord = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		int maxsize = totalNewRecord + 1;
		if (totalNewRecord > 20) {
			maxsize = 22;
		}
		assertTrue(totalNewRecord > totalOldRecord, "        The New record is shown in the grid.");
		assertTrue(payorId.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,3).getText()),"        The PayorId Of Main Section table is displayed.");
		String actFullName = fac.getAbbrv() + " - " + fac.getName();
		String expFullName = placeOfServiceCodeConfig.getCellOfTbl(maxsize, 5).getText();
		assertTrue(actFullName.equalsIgnoreCase(expFullName), "        The Pref Facility Of Main Section table is displayed.");
		assertTrue(procCode.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,6).getText()),"        The Procedure Code Of Main Section table is displayed.");
		assertTrue(svcTyp.getDescr().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,7).getText()),"        The Service Type Of Main Section table is displayed.");
		assertTrue(mod.getModId().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,9).getText()),"        The Modifier Of Main Section table is displayed.");
		assertTrue(clnAccntTyp.get(1).equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,10).getText()),"        The Client Account Type Code Of Main Section table is displayed.");
		assertTrue(ptLocTyp.getDescr().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,11).getText()),"        The Patient Location Of Main Section table is displayed.");
		assertTrue(ptTyp.get(0).equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,12).getText()),"        The Patient Type Of Main Section table is displayed.");
		assertTrue(posType.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,13).getText()),"        The POS Code Of Main Section table is displayed.");
		Pyr pyr = payorDao.getPyrByPyrAbbrv(payorId);

		logger.info("*** Step 4 Action: Click on Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 4 Expected Result: The page is reset, a new record is show in the grid, a new record is save in POS_OVERRIDE table.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();
		List<PosOverride> resultlNewInDB = posDao.getPosOverrideByMoreConditionOfServiceCode(clnAccntTyp.get(0), String.valueOf(fac.getFacId()), posType, String.valueOf(ptLocTyp.getPtLocId()), String.valueOf(pyr.getPyrId()), String.valueOf(svcTyp.getSvcTypId()), ptTyp.get(1), mod.getModId(), procCode);
		assertTrue(totalNewRecord > totalOldRecord, "        The New record is shown in the grid.");
		placeOfServiceCodeConfig.enterIDSearchBox(String.valueOf(resultlNewInDB.get(0).getSeqId()));
		
		assertTrue(payorId.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,3).getText()),"        The PayorId Of Main Section table is displayed.");
		assertTrue((fac.getAbbrv()+" - "+fac.getName()).equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,5).getText()),"        The Pref Facility Of Main Section table is displayed.");
		assertTrue(procCode.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,6).getText()),"        The Procedure Code Of Main Section table is displayed.");
		assertTrue(svcTyp.getDescr().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,7).getText()),"        The Service Type Of Main Section table is displayed.");
		assertTrue(mod.getModId().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,9).getText()),"        The Modifier Of Main Section table is displayed.");
		assertTrue(clnAccntTyp.get(1).equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,10).getText()),"        The Client Account Type Code Of Main Section table is displayed.");
		assertTrue(ptLocTyp.getDescr().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,11).getText()),"        The Patient Location Of Main Section table is displayed.");
		assertTrue(ptTyp.get(0).equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,12).getText()),"        The Patient Type Of Main Section table is displayed.");
		assertTrue(posType.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,13).getText()),"        The POS Code Of Main Section table is displayed.");
		
		assertTrue(isElementPresent(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl(),5),"        The total Record In Place Of Service table is displayed.");
		assertTrue(resultlNewInDB.size() > 0,"        The new record is save in POS_OVERRIDE table.");

		logger.info("*** Step 5 Action: Delete new record.");
		daoManagerPlatform.deleteDataFromTableByCondition(" POS_OVERRIDE ","PK_SEQ_ID = "+resultlNewInDB.get(resultlNewInDB.size()-1).getSeqId() , testDb);
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Add new Service Code with Payor and Proc Cd is get from Search result")
	public void testXPR_910() throws Throwable {		
		logger.info("===== Testing - testXPR_910 =====");  	

		payorSearch = new PayorSearch(driver, config);
		procCodeSearch = new ProcCodeSearch(driver);
		payorSearchResults = new PayorSearchResults(driver, wait);
		procCodeSearchResults = new ProcCodeSearchResults(driver);
		placeOfServiceCodeConfig = new PlaceOfServiceCodeConfig(driver);
		
		logger.info("*** Step 1 Expected Result: User login successfully, the Place Of Service code Configuration page is displayed.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();
		int totalOldRecord = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));

		logger.info("*** Step 2 Action: Click on Add icon.");
		clickOnAddBtn();
		
		logger.info("*** Step 2 Expected Result: Add record popup is displayed.");
		verifyTheAddRecordPopupIsDisplayed();

		logger.info("*** Step 3 Action: Click on Payor Id search icon.");
		clickHiddenPageObject(placeOfServiceCodeConfig.searchPayorIdIcon(), 0);
		String parent = switchToPopupWin();
		
		logger.info("*** Step 3 Expected Result: Payor search page is open.");
		assertEquals(payorSearch.payorSearchTitleTxt().getText(), PAYOR_SEARCH_TITLE,"        The payor Search Title is displayed.");

		logger.info("*** Step 4 Action: Enter * at payor Id field, click on Search button, select first row in Payor search result page.");
		payorSearch.enterPayorID("*");
		clickHiddenPageObject(payorSearch.searchBtn(), 0);
		String payorId = payorSearchResults.getPayorIdCellInTable(2, 2).getText();
		clickHiddenPageObject(payorSearchResults.getPayorIdCellInTable(2, 2), 0);
		
		logger.info("*** Step 4 Expected Result: Search result page is close, payor Id is populated in the Add Record add popup.");
		switchToParentWin(parent);
		
		assertTrue(isElementPresent(placeOfServiceCodeConfig.addIconOfMainSectionTbl(),5),"        Search result page is close.");
		assertTrue(isElementPresent(placeOfServiceCodeConfig.payorIdInput(),5),"        The payorId Input is displayed.");
		assertEquals(placeOfServiceCodeConfig.payorIdInput().getAttribute("value"), payorId,"        The payor Id is populated in the Add Record.");
		
		logger.info("*** Step 5 Action: Click on procedure code Search icon.");
		clickHiddenPageObject(placeOfServiceCodeConfig.procCodeSearchIcon(), 0);
		parent = switchToPopupWin();
		
		logger.info("*** Step 5 Expected Result: Procedure Code Search page is displayed.");
		assertEquals(procCodeSearch.procedureCodeSearchTitle().getText(), PROCEDURE_CODE_SEARCH_TITLE,"        The Procedure Code Search Title is displayed.");

		logger.info("*** Step 6 Action: Enter * at Procedure code field, click on Search button, select the first row.");
		procCodeSearch.inputProcedureCodeID("*");
		clickHiddenPageObject(procCodeSearch.procCodeSearchButton(), 0);
		
		String procCode = procCodeSearchResults.procedureIdLinkTestCodeSearchResults(2, 2).getText();
		clickHiddenPageObject(procCodeSearchResults.procedureIdLinkTestCodeSearchResults(2, 2), 0);
		
		logger.info("*** Step 6 Expected Result: The Search result page is closed, procedure code is populated in procedure code field.");
		switchToParentWin(parent);
		
		assertTrue(isElementPresent(placeOfServiceCodeConfig.addIconOfMainSectionTbl(),5),"        Search result page is close.");
		assertTrue(isElementPresent(placeOfServiceCodeConfig.procCodeInput(),5),"        The procCode Input is displayed.");
		assertEquals(placeOfServiceCodeConfig.procCodeInput().getAttribute("value"),procCode,"        The procCode is populated in the Add Record.");
	
		logger.info("*** Step 7 Action: Fill all fields follow condition in test case.");
		PosTyp posTyp = posDao.getPosTyp();
		String posType = posTyp.getPosTypId();
		List<String> ptTyp  = daoManagerAccnWS.getRandomPatientTypeFromPT_TYP(testDb);
		SvcTyp svcTyp = serviceDao.getSvcTyp();
		Fac fac = facilityDao.getRandomFac();
		List<String> clnAccntTyp = daoManagerXifinRpm.getClnAccntTypFromCLNACCNTTYPI(testDb);
		Mod mod = modDao.getMod();
		PtLocTyp ptLocTyp = patientDao.getRandomDataFromPtLocTyp();
		
		enterDataToAddRecordPlaceOfServiceCode(EMPTY,fac.getAbbrv()+" - "+fac.getName(), svcTyp.getDescr(), mod.getModId(), clnAccntTyp.get(1), ptLocTyp.getDescr(), ptTyp.get(0),  posType);
		
		logger.info("*** Step 7 Expected Result: A new record is shown in the grid.");
		waitUntilElementPresent(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl(), 30);
		int totalNewRecord = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		int maxsize = totalNewRecord + 1;
		if (totalNewRecord > 20) {
			maxsize = 22;
		}
		assertTrue(totalNewRecord > totalOldRecord, "        The new record is shown in the grid.");

		assertTrue(payorId.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,3).getText()),"        The PayorId Of Main Section table is displayed.");
		assertTrue((fac.getAbbrv()+" - "+fac.getName()).equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,5).getText()),"        The Pref Facility Of Main Section table is displayed.");
		assertTrue(procCode.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,6).getText()),"        The Procedure Code Of Main Section table is displayed.");
		assertTrue(svcTyp.getDescr().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,7).getText()),"        The Service Type Of Main Section table is displayed.");
		assertTrue(mod.getModId().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,9).getText()),"        The Modifier Of Main Section table is displayed.");
		assertTrue(clnAccntTyp.get(1).equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,10).getText()),"        The Client Account Type Code Of Main Section table is displayed.");
		assertTrue(ptLocTyp.getDescr().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,11).getText()),"        The Patient Location Of Main Section table is displayed.");
		assertTrue(ptTyp.get(0).equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,12).getText()),"        The Patient Type Of Main Section table is displayed.");
		assertTrue(posType.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,13).getText()),"        The POS Code Of Main Section table is displayed.");
		
		Pyr pyr = payorDao.getPyrByPyrAbbrv(payorId);

		logger.info("*** Step 8 Action: Click on Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 8 Expected Result: New record is shown in the grid and New data is saved to DB.");
		List<PosOverride> resultlNewInDB = posDao.getPosOverrideByMoreConditionOfServiceCode(clnAccntTyp.get(0), String.valueOf(fac.getFacId()), posType, String.valueOf(ptLocTyp.getPtLocId()), String.valueOf(pyr.getPyrId()), String.valueOf(svcTyp.getSvcTypId()), ptTyp.get(1), mod.getModId(), procCode);
		placeOfServiceCodeConfig.enterIDSearchBox(String.valueOf(resultlNewInDB.get(0).getSeqId()));		logger.info("*** Step 8 Expected Result: The page is reset, a new record is shown in the grid, a new record is saved in POS_OVERRIDE table.");
		assertTrue(payorId.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,3).getText()),"        The PayorId Of Main Section table is displayed.");
		assertTrue((fac.getAbbrv()+" - "+fac.getName()).equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,5).getText()),"        The Pref Facility Of Main Section table is displayed.");
		assertTrue(procCode.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,6).getText()),"        The Procedure Code Of Main Section table is displayed.");
		assertTrue(svcTyp.getDescr().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,7).getText()),"        The Service Type Of Main Section table is displayed.");
		assertTrue(mod.getModId().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,9).getText()),"        The Modifier Of Main Section table is displayed.");
		assertTrue(clnAccntTyp.get(1).equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,10).getText()),"        The Client Account Type Code Of Main Section table is displayed.");
		assertTrue(ptLocTyp.getDescr().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,11).getText()),"        The Patient Location Of Main Section table is displayed.");
		assertTrue(ptTyp.get(0).equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,12).getText()),"        The Patient Type Of Main Section table is displayed.");
		assertTrue(posType.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,13).getText()),"        The POS Code Of Main Section table is displayed.");
		assertTrue(resultlNewInDB.size() > 0,"        The new record is save in POS_OVERRIDE table.");

		logger.info("*** Step 9 Action: Delete new record.");
		daoManagerPlatform.deleteDataFromTableByCondition(" POS_OVERRIDE ","PK_SEQ_ID = "+resultlNewInDB.get(0).getSeqId() , testDb);
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Add new record with enter valid fields only")
	@Parameters({ "pyrAbbrv", "posTypId" })
	public void testXPR_911(String pyrAbbrv, String posTypId) throws Throwable {
		logger.info("Starting test case, pyrAbbrv="+pyrAbbrv+", posTypId="+posTypId);

		Pyr pyr = payorDao.getPyrByPyrAbbrv(pyrAbbrv);
		PosTyp posTyp = posDao.getPosTypByTypId(Integer.parseInt(posTypId));

		deleteExistingPosOverrides(pyr.getPyrId());

		List<PosOverride> origPosOverrides = posDao.getPosOverridesByPyrId(pyr.getPyrId());
		assertTrue(origPosOverrides.isEmpty(), "Expected no existing POS Overrides, pyrId="+pyr.getPyrId()+", posOverrideCnt="+origPosOverrides.size());

		placeOfServiceCodeConfig = new PlaceOfServiceCodeConfig(driver);

		// Since the POS Config screen is loaded in the BeforeMethod, we need to reset the page after deleting the POS_OVERRIDE records above
		placeOfServiceCodeConfig.pressAltR();

		logger.info("*** Step 1 Expected Result: User login successfully, the Place Of Service code Configuration page is displayed.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();
		int totalOldRecord = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));

		logger.info("*** Step 2 Action: Click on Add icon.");
		clickOnAddBtn();
		
		logger.info("*** Step 2 Expected Result: Add record popup is displayed.");
		verifyTheAddRecordPopupIsDisplayed();
	
		logger.info("*** Step 3 Action: Select Payor and POS Code only, click on OK button.");
		placeOfServiceCodeConfig.enterPayorIdInput(pyrAbbrv);
		placeOfServiceCodeConfig.enterPosCodeInput(posTyp.getPosTypId());
		clickHiddenPageObject(placeOfServiceCodeConfig.okPopupBtn(),0);
		
		logger.info("*** Step 3 Expected Result: The new record is shown in the grid with valid POS Code.");
		waitUntilElementPresent(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl(), 30);
		
		int totalNewRecord = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		int maxsize = totalNewRecord + 1;
		if (totalNewRecord > 20) {
			maxsize = 22;
		}
		assertTrue(totalNewRecord > totalOldRecord, "        The New record is shown in the grid.");
		assertTrue(posTyp.getPosTypId().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize, 13).getText()), "        The POS Code Of Main Section table is displayed.");

		logger.info("*** Step 4 Action: Click on Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 4 Expected Result: The page is reset, a new record is shown in the grid, a new record is saved in POS_OVERRIDE table.");
		List<PosOverride> newPosOverrides = posDao.getPosOverridesByPyrId(pyr.getPyrId());
		assertEquals(newPosOverrides.size(), 1, "Expected 1 new POS Override, pyrId="+pyr.getPyrId()+", posOverrideCnt="+newPosOverrides.size());
		assertEquals(newPosOverrides.get(0).getOverrideValue(), posTypId, "POS Override value to match, pyrId="+pyr.getPyrId()+", overrideValue="+newPosOverrides.get(0).getOverrideValue());

		placeOfServiceCodeConfig.enterIDSearchBox(String.valueOf(newPosOverrides.get(0).getSeqId()));
		
		assertTrue(isElementPresent(placeOfServiceCodeConfig.placeOfServiceTitle(),5),"        The place Of Service Title is displayed.");
		assertTrue(isElementPresent(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl(),5),"        The total Record In Place Of Service table is displayed.");
		assertTrue(posTyp.getPosTypId().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,13).getText()),"        The POS Code Of Main Section table is displayed.");

		driver.quit();
	}

	private void deleteExistingPosOverrides(int pyrId) throws XifinDataAccessException
	{
		for (PosOverride posOverride : posDao.getPosOverridesByPyrId(pyrId))
		{
			logger.info("Deleting POS Override, seqId="+posOverride.getSeqId()+", pyrId="+posOverride.getPyrId()+", overrideValue="+posOverride.getOverrideValue());
			posOverride.setResultCode(ErrorCodeMap.DELETED_RECORD);
			databaseSequenceDao.setValueObject(posOverride);
		}
	}

	@Test(priority = 1, description = "Add new record with duplicate PayorID and POS Code")
	public void testXPR_912() throws Throwable {		
		logger.info("===== Testing - testXPR_912 =====");  	

		placeOfServiceCodeConfig = new PlaceOfServiceCodeConfig(driver);
		
		logger.info("*** Step 1 Expected Result: User login successfully, the Place Of Service code Configuration page is displayed.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();
		int totalOldRecord = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));

		logger.info("*** Step 2 Action: Click on Add icon.");
		clickOnAddBtn();
		
		logger.info("*** Step 2 Expected Result: Add record popup is displayed.");
		verifyTheAddRecordPopupIsDisplayed();
	
		logger.info("*** Step 3 Action: Select PayorId that is not existing in the grid, select POS Code, click on OK button.");
		Pyr pyr = payorDao.getRandomPyrByPyrIdNotInPosOverride();
		String payorId = pyr.getPyrAbbrv();
		PosTyp posTyp = posDao.getPosTyp();
		String posType = posTyp.getPosTypId();
		
		placeOfServiceCodeConfig.enterPayorIdInput(payorId);
		placeOfServiceCodeConfig.enterPosCodeInput(posType);
		clickHiddenPageObject(placeOfServiceCodeConfig.okPopupBtn(),0);
		
		logger.info("*** Step 3 Expected Result: The new record is shown in the grid with valid POS Code and PayorId.");
		waitUntilElementPresent(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl(), 30);
		
		int totalNewRecord = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		int maxsize = totalNewRecord + 1;
		if (totalNewRecord > 20) {
			maxsize = 22;
		}
		assertTrue(totalNewRecord > totalOldRecord,"        The New record is shown in the grid.");
		assertTrue(payorId.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,3).getText()),"        The PayorId Of Main Section table is displayed with valid PayorId.");
		assertTrue(posType.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,13).getText()),"        The POS Code Of Main Section table is displayed with valid POS Code.");
		
		List<PosOverride> resultOldInDB = posDao.getPosOverrideByPyrIdAndOverrideValue(String.valueOf(pyr.getPyrId()), posType);

		logger.info("*** Step 4 Action: Click on Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 4 Expected Result: The page is reset, a new record is shown in the grid, a new record is saved in POS_OVERRIDE table.");
		assertTrue(isElementPresent(placeOfServiceCodeConfig.placeOfServiceTitle(),5),"        The place Of Service Title is displayed.");
		assertTrue(isElementPresent(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl(),5),"        The total Record In Place Of Service table is displayed.");
		int totalNewRecordSave = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		assertTrue(totalNewRecordSave > totalOldRecord, "        The new record is shown in the grid.");
		List<PosOverride> resultlNewInDB = posDao.getPosOverrideByPyrIdAndOverrideValue(String.valueOf(pyr.getPyrId()), posType);
		assertTrue(resultlNewInDB.size() > resultOldInDB.size(),"        The new record is save in POS_OVERRIDE table.");

		logger.info("*** Step 5 Action: Add new record with the same PayorId and POS code at step 3, click on OK button.");
		clickOnAddBtn();
		placeOfServiceCodeConfig.enterPayorIdInput(payorId);
		placeOfServiceCodeConfig.enterPosCodeInput(posType);
		clickHiddenPageObject(placeOfServiceCodeConfig.okPopupBtn(),0);
		
		logger.info("*** Step 5 Expected result: The error message 'Record is identical to Record#Seq_Id' is displayed, no new record is save in POS_OVERRIDE table");
		String actErrorMsg = "Record is identical to Record #" + resultlNewInDB.get(0).getSeqId();
		String expErrorMsg = placeOfServiceCodeConfig.getErrorMessInPopup().getText();
		assertEquals(actErrorMsg, expErrorMsg, "        The error message 'Record is identical to Record#Seq_Id' is displayed");
		List<PosOverride> resultlNew2InDB = posDao.getPosOverrideByPyrIdAndOverrideValue(String.valueOf(pyr.getPyrId()), posType);
		assertTrue(resultlNewInDB.size() == resultlNew2InDB.size(),"        No the new record is save in POS_OVERRIDE table.");

		logger.info("*** Step 6 Action: Removed new record");
		daoManagerPlatform.deleteDataFromTableByCondition(" POS_OVERRIDE ","PK_SEQ_ID = "+resultlNewInDB.get(0).getSeqId() , testDb);
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Add new record with select both PayorID and Payor Group")
	public void testXPR_913() throws Throwable {		
		logger.info("===== Testing - testXPR_913 =====");  	

		placeOfServiceCodeConfig = new PlaceOfServiceCodeConfig(driver);
		
		logger.info("*** Step 1 Expected Result: User login successfully, the Place Of Service code Configuration page is displayed.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();
		int resultOldDataInDB = posDao.getPosOverrideBySeqIdIsNotNull().size();

		logger.info("*** Step 2 Action: Click on Add icon.");
		clickOnAddBtn();
		
		logger.info("*** Step 2 Expected Result: Add record popup is displayed.");
		verifyTheAddRecordPopupIsDisplayed();
	
		logger.info("*** Step 3 Action: Select PayorId that is not existing in the grid, get Payor Grp from pyr_grp, select POS code, click on OK button.");
		Pyr pyr = payorDao.getRandomPyrByPyrIdNotInPosOverride();
		String payorId = pyr.getPyrAbbrv();
		List<String> payorGrp = daoManagerXifinRpm.getGroupPayor(testDb);
		PosTyp posTyp = posDao.getPosTyp();
		String posType = posTyp.getPosTypId();
		
		placeOfServiceCodeConfig.enterPayorIdInput(payorId);
		placeOfServiceCodeConfig.enterPayorGroupInput(payorGrp.get(1));;
		placeOfServiceCodeConfig.enterPosCodeInput(posType);
		clickHiddenPageObject(placeOfServiceCodeConfig.okPopupBtn(),0);
		
		logger.info("*** Step 3 Expected Result: The new error mesage '10201 - Either Payor or Payor Group (not both) can be selected' is displayed, no new record is save in DB.");
		String actErrMsg = "10201 - Either Payor or Payor Group (not both) can be selected";
		String expErrMsg = placeOfServiceCodeConfig.getErrorMessInPopup().getText();
		assertEquals(actErrMsg, expErrMsg,"        The error message '10201 - Either Payor or Payor Group (not both) can be selected' is displayed");
		int resultNewDataInDB = posDao.getPosOverrideBySeqIdIsNotNull().size();
		assertTrue(resultNewDataInDB == resultOldDataInDB, "        No the new record is save in POS_OVERRIDE table.");
		
		driver.quit();
	}
	

	@Test(priority = 1, description = "Update record with valid data")
	public void testXPR_914() throws Throwable {		
		logger.info("===== Testing - testXPR_914 =====");  	

		payorSearch = new PayorSearch(driver, config);
		placeOfServiceCodeConfig = new PlaceOfServiceCodeConfig(driver);
		
		logger.info("*** Step 1 Expected Result: User login successfully, the Place Of Service code Configuration page is displayed.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();
		int totalOldRecord = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));

		logger.info("*** Step 2 Action: Click on Add icon.");
		clickOnAddBtn();
		
		logger.info("*** Step 2 Expected Result: Add record popup is displayed.");
		verifyTheAddRecordPopupIsDisplayed();
	
		logger.info("*** Step 3 Action: Enter valid data for all fields, click on OK button.");
		String payorId = daoManagerXifinRpm.getPayorID(testDb);
		Pyr pyr = payorDao.getPyrByPyrAbbrv(payorId);
		String procCode = daoManagerPlatform.getProcIdFromPROCCD(testDb);
		PosTyp posTyp = posDao.getPosTyp();
		String posType = posTyp.getPosTypId();
		List<String> ptTyp = daoManagerAccnWS.getRandomPatientTypeFromPT_TYP(testDb);
		SvcTyp svcTyp = serviceDao.getSvcTyp();
		Fac fac = facilityDao.getRandomFac();
		Mod mod = modDao.getMod();
		List<String> clnAccntTyp = daoManagerXifinRpm.getClnAccntTypFromCLNACCNTTYPI(testDb);
		PtLocTyp ptLocTyp = patientDao.getRandomDataFromPtLocTyp();
		
		placeOfServiceCodeConfig.enterPayorIdInput(payorId);
		placeOfServiceCodeConfig.enterProcCodeInput(procCode);
		enterDataToAddRecordPlaceOfServiceCode(EMPTY, fac.getAbbrv() + " - " + fac.getName(), svcTyp.getDescr(),
				mod.getModId(), clnAccntTyp.get(1), ptLocTyp.getDescr(), ptTyp.get(0), posType);

		logger.info("*** Step 3 Expected Result: New record is shown in the grid.");
		waitUntilElementPresent(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl(), 30);
		
		int totalNewRecord = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		int maxsize = totalNewRecord + 1;
		if (totalNewRecord > 20) {
			maxsize = 22;
		}
		assertTrue(totalNewRecord > totalOldRecord,"        The new record is shown in the grid.");
		assertTrue(payorId.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,3).getText()),"        The PayorId Of Main Section table is displayed with correct Data.");
		String actFullName = fac.getAbbrv() + " - " + fac.getName();
		String expFullName = placeOfServiceCodeConfig.getCellOfTbl(maxsize,5).getText();
		assertTrue(actFullName.equalsIgnoreCase(expFullName),"        The Pref Facility Of Main Section table is displayed with correct Data.");
		assertTrue(procCode.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,6).getText()),"        The Procedure Code Of Main Section table is displayed with correct Data.");
		assertTrue(svcTyp.getDescr().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,7).getText()),"        The Service Type Of Main Section table is displayed with correct Data.");
		assertTrue(mod.getModId().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,9).getText()),"        The Modifier Of Main Section table is displayed with correct Data.");
		assertTrue(clnAccntTyp.get(1).equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,10).getText()),"        The Client Account Type Code Of Main Section table is displayed with correct Data.");
		assertTrue(ptLocTyp.getDescr().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,11).getText()),"        The Patient Location Of Main Section table is displayed with correct Data.");
		assertTrue(ptTyp.get(0).equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,12).getText()),"        The Patient Type Of Main Section table is displayed with correct Data.");
		assertTrue(posType.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(maxsize,13).getText()),"        The POS Code Of Main Section table is displayed with correct Data.");

		logger.info("*** Step 4 Action: Click on Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 4 Expected Result: The page is reset, a new record is show in the grid, a new record is save in POS_OVERRIDE table.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();
		List<PosOverride> resultlNewInDB = posDao.getPosOverrideByMoreConditionOfServiceCode(clnAccntTyp.get(0), String.valueOf(fac.getFacId()), posType, String.valueOf(ptLocTyp.getPtLocId()), String.valueOf(pyr.getPyrId()), String.valueOf(svcTyp.getSvcTypId()), ptTyp.get(1), mod.getModId(), procCode);
		placeOfServiceCodeConfig.enterIDSearchBox(String.valueOf(resultlNewInDB.get(0).getSeqId()));	
		assertTrue(payorId.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,3).getText()),"        The PayorId Of Main Section table is displayed with correct Data.");
		assertTrue((fac.getAbbrv()+" - "+fac.getName()).equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,5).getText()),"        The Pref Facility Of Main Section table is displayed with correct Data.");
		assertTrue(procCode.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,6).getText()),"        The Procedure Code Of Main Section table is displayed with correct Data.");
		assertTrue(svcTyp.getDescr().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,7).getText()),"        The Service Type Of Main Section table is displayed with correct Data.");
		assertTrue(mod.getModId().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,9).getText()),"        The Modifier Of Main Section table is displayed with correct Data.");
		assertTrue(clnAccntTyp.get(1).equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,10).getText()),"        The Client Account Type Code Of Main Section table is displayed with correct Data.");
		assertTrue(ptLocTyp.getDescr().equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,11).getText()),"        The Patient Location Of Main Section table is displayed with correct Data.");
		assertTrue(ptTyp.get(0).equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,12).getText()),"        The Patient Type Of Main Section table is displayed with correct Data.");
		assertTrue(posType.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,13).getText()),"        The POS Code Of Main Section table is displayed with correct Data.");
		int totalNewRecordSave = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		assertTrue(resultlNewInDB.size() > 0,"        The new record is save in POS_OVERRIDE table.");

		logger.info("*** Step 5 Expected Result: select the new record, click on Edit icon.");
		int numPagerNavigate = Convert.toInteger(placeOfServiceCodeConfig.getNumPagerNavigate().getText());
		while (numPagerNavigate > 1) {
			clickHiddenPageObject(placeOfServiceCodeConfig.nextPagerIcon(), 0);
			numPagerNavigate--;
		}
		clickHiddenPageObject(placeOfServiceCodeConfig.getLastRowInTable(), 0);
		clickHiddenPageObject(placeOfServiceCodeConfig.editIconOfMainSectionTbl(), 0);
		
		logger.info("*** Step 5 Expected Result: The edit popup is open.");
		verifyTheAddRecordPopupIsDisplayed();

		logger.info("*** Step 6 Action: Update some fields: new Payor ID , new POS Code, click Ok button, click on Save button.");
		String payorId2 = daoManagerXifinRpm.getPayorID(testDb);
		Pyr pyr2 = payorDao.getPyrByPyrAbbrv(payorId2);

		while (payorId2.equals(payorId)) {
			payorId2 = daoManagerXifinRpm.getPayorID(testDb);
		}
		placeOfServiceCodeConfig.enterPayorIdInput(payorId2);
		String procCode2 = daoManagerPlatform.getProcIdFromPROCCD(testDb);
		while (procCode2.equals(procCode)) {
			procCode2 = daoManagerXifinRpm.getPayorID(testDb);
		}
		placeOfServiceCodeConfig.enterProcCodeInput(procCode2);
		
		clickHiddenPageObject(placeOfServiceCodeConfig.okPopupBtn(), 0);
		clickOnSaveAndClearBtn();
	
		logger.info("*** Step 6 Expected Result: Grid is reset, the new data is saved in POS_OVERRIDE.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();
		List<PosOverride> resultlNewInDB2 =posDao.getPosOverrideByMoreConditionOfServiceCode(clnAccntTyp.get(0), String.valueOf(fac.getFacId()), posType, String.valueOf(ptLocTyp.getPtLocId()), String.valueOf(pyr2.getPyrId()), String.valueOf(svcTyp.getSvcTypId()), ptTyp.get(1), mod.getModId(), procCode2);
		placeOfServiceCodeConfig.enterIDSearchBox(String.valueOf(resultlNewInDB2.get(0).getSeqId()));
		assertTrue(payorId2.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,3).getText()),"        The PayorId Of Main Section table is displayed with correct Data.");
		assertTrue(procCode2.equalsIgnoreCase(placeOfServiceCodeConfig.getCellOfTbl(2,6).getText()),"        The Procedure Code Of Main Section table is displayed with correct Data.");
		int totalNewRecordSave2 = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		assertTrue(totalNewRecordSave2 == totalNewRecordSave,"        The New record is shown in the grid.");
		assertTrue(resultlNewInDB2.size() > 0,"        The new record is save in POS_OVERRIDE table.");

		logger.info("*** Step 7 Action: Delete new record.");
		daoManagerPlatform.deleteDataFromTableByCondition(" POS_OVERRIDE ","PK_SEQ_ID = "+resultlNewInDB.get(resultlNewInDB.size()-1).getSeqId() , testDb);
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Delete record.")
	public void testXPR_915() throws Throwable {		
		logger.info("===== Testing - testXPR_915 =====");  	

		procCodeSearch = new ProcCodeSearch(driver);
		placeOfServiceCodeConfig = new PlaceOfServiceCodeConfig(driver);
		
		logger.info("*** Step 1 Expected Result: User login successfully, the Place Of Service Code Configuration Screen is displayed.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Click on Add icon.");
		clickOnAddBtn();
		
		logger.info("*** Step 2 Expected Result: Add record popup is displayed.");
		verifyTheAddRecordPopupIsDisplayed();
		
		logger.info("*** Step 3 Action: Enter valid data for all fields, click on OK button.");
		String payorAbbrev = daoManagerXifinRpm.getPayorID(testDb);
		Pyr pyr = payorDao.getPyrByPyrAbbrv(payorAbbrev);
		String procCodeId = daoManagerPlatform.getProcIdFromPROCCD(testDb);
		PosTyp posTyp = posDao.getPosTyp();
		String posCode = posTyp.getPosTypId();
		List<String> patientTyp = daoManagerAccnWS.getRandomPatientTypeFromPT_TYP(testDb);
		SvcTyp svcTyp = serviceDao.getSvcTyp();
		Fac fac = facilityDao.getRandomFac();
		String perFacility = fac.getAbbrv()+" - "+fac.getName();
		Mod mod = modDao.getMod();
		PtLocTyp ptLocTyp = patientDao.getRandomDataFromPtLocTyp();
		List<String> clnAccntTyp     = daoManagerXifinRpm.getClnAccntTypFromCLNACCNTTYPI(testDb);
		Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		enterDataToAddRecordPlaceOfServiceCode(payorAbbrev, perFacility, procCodeId, svcTyp.getDescr(), mod.getModId(), clnAccntTyp.get(1), ptLocTyp.getDescr(), patientTyp.get(0), posCode);
		
		logger.info("*** Step 3 Expected Result: New record is shown in the grid.");
		assertEquals(lastCellTbl(TBL_POSCODE_CONFIG, 3).getText(), payorAbbrev,"        New record should be shown in the grid with correct Payor Id.");
		
		logger.info("*** Step 4 Action: Click on Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 4 Expected Result: The grid is reset and new record is shown in the grid, new record is saved in POS_OVERRIDE table.");
		List<PosOverride> dataInDb = posDao.getPosOverrideByConditionOfServiceCode(clnAccntTyp.get(0), String.valueOf(fac.getFacId()), posCode, String.valueOf(ptLocTyp.getPtLocId()), String.valueOf(pyr.getPyrId()), String.valueOf(svcTyp.getSvcTypId()), patientTyp.get(1), mod.getModId());
		
		assertEquals(dataInDb.size(), 1,"        The new record should be saved in POS_OVERRIDE table.");
		placeOfServiceCodeConfig.enterIDSearchBox(String.valueOf(dataInDb.get(0).getSeqId()));
		int searchResult = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		assertEquals(searchResult,1, "        The new records should be shown in the grid.");
				
		logger.info("*** Step 5 Action: Select new record, click on Edit icon.");
		clickHiddenPageObject(placeOfServiceCodeConfig.getCellOfTbl(2,3),0);
		clickHiddenPageObject(placeOfServiceCodeConfig.editIconOfMainSectionTbl(), 0);
		
		logger.info("*** Step 5 Expected Result: The edit popup is opened.");
		verifyTheAddRecordPopupIsDisplayed();
		
		logger.info("*** Step 6 Action: Check on Delete check box, click on OK button, click on Save and Clear button.");
		clickHiddenPageObject(placeOfServiceCodeConfig.checkDeleteInput(),0);
		clickHiddenPageObject(placeOfServiceCodeConfig.okPopupBtn(),0);
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 6 Expected Result: Grid is reset, the selected data is not shown in the grid, the record are removed out of the POS_OVERRIDE table.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();
		
		placeOfServiceCodeConfig.enterIDSearchBox(String.valueOf(dataInDb.get(0).getSeqId()));
		
		searchResult = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		assertEquals(searchResult, 0, "        The selected records should not be shown in the grid.");
		dataInDb = posDao.getPosOverrideByConditionOfServiceCode(clnAccntTyp.get(0), String.valueOf(fac.getFacId()), posCode, String.valueOf(ptLocTyp.getPtLocId()), String.valueOf(pyr.getPyrId()), String.valueOf(svcTyp.getSvcTypId()), patientTyp.get(1), mod.getModId());
		assertTrue(dataInDb.size() == 0,"        The selected record is removed out of the POS_OVERRIDE table.");
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Delete multi records.")
	public void testXPR_916() throws Throwable {		
		logger.info("===== Testing - testXPR_916 =====");  	

		procCodeSearch = new ProcCodeSearch(driver);
		placeOfServiceCodeConfig = new PlaceOfServiceCodeConfig(driver);
		
		logger.info("*** Step 1 Expected Result: User login successfully, the Place Of Service Code Configuration Screen is displayed.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Add multi new records in grid. Click on save and clear button.");
		String payorAbbrev1 = daoManagerXifinRpm.getPayorID(testDb);
		Pyr pyr1 = payorDao.getPyrByPyrAbbrv(payorAbbrev1);
		String procCodeId1 = daoManagerPlatform.getProcIdFromPROCCD(testDb);
		PosTyp posTyp1 = posDao.getPosTyp();
		String posCode1 = posTyp1.getPosTypId();
		List<String> patientTyp1 = daoManagerAccnWS.getRandomPatientTypeFromPT_TYP(testDb);
		SvcTyp svcTyp1 = serviceDao.getSvcTyp();
		Fac fac1 = facilityDao.getRandomFac();
		String perFacility1 = fac1.getAbbrv() + " - " + fac1.getName();
		Mod mod1 =modDao.getMod();
		PtLocTyp ptLocTyp1 = patientDao.getRandomDataFromPtLocTyp();
		List<String> clnAccntTyp1 = daoManagerXifinRpm.getClnAccntTypFromCLNACCNTTYPI(testDb);

		String payorAbbrev2 = daoManagerXifinRpm.getPayorID(testDb);
		Pyr pyr2 = payorDao.getPyrByPyrAbbrv(payorAbbrev2);
		String procCodeId2 = daoManagerPlatform.getProcIdFromPROCCD(testDb);
		PosTyp posTyp2 = posDao.getPosTyp();
		String posCode2 = posTyp2.getPosTypId();
		List<String> patientTyp2 = daoManagerAccnWS.getRandomPatientTypeFromPT_TYP(testDb);
		SvcTyp svcTyp2 = serviceDao.getSvcTyp();
		Fac fac2 = facilityDao.getRandomFac();
		String perFacility2 = fac2.getAbbrv() + " - " + fac2.getName();
		Mod mod2 = modDao.getMod();
		PtLocTyp ptLocTyp2 = patientDao.getRandomDataFromPtLocTyp();
		List<String> clnAccntTyp2     = daoManagerXifinRpm.getClnAccntTypFromCLNACCNTTYPI(testDb);
		
		int oldTotalRecord = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		
		clickOnAddBtn();
		enterDataToAddRecordPlaceOfServiceCode(payorAbbrev1, perFacility1, procCodeId1, svcTyp1.getDescr(), mod1.getModId(), clnAccntTyp1.get(1), ptLocTyp1.getDescr() ,patientTyp1.get(0), posCode1);
		clickOnAddBtn();
		enterDataToAddRecordPlaceOfServiceCode(payorAbbrev2, perFacility2, procCodeId2, svcTyp2.getDescr(), mod2.getModId(), clnAccntTyp2.get(1), ptLocTyp2.getDescr() ,patientTyp2.get(0), posCode2);
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 2 Expected Result: new records are shown in the grids. New records are saved in POS_OVERRIDE table.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();
		
		int newTotalRecords = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		assertEquals(newTotalRecords, oldTotalRecord + 2, "        The new records should be shown in the grid.");
		
		List<PosOverride> dataInDb1 = posDao.getPosOverrideByConditionOfServiceCode(clnAccntTyp1.get(0), String.valueOf(fac1.getFacId()), posCode1, String.valueOf(ptLocTyp1.getPtLocId()), String.valueOf(pyr1.getPyrId()), String.valueOf(svcTyp1.getSvcTypId()), patientTyp1.get(1), mod1.getModId());
		List<PosOverride> dataInDb2 = posDao.getPosOverrideByConditionOfServiceCode(clnAccntTyp2.get(0), String.valueOf(fac2.getFacId()), posCode2, String.valueOf(ptLocTyp2.getPtLocId()), String.valueOf(pyr2.getPyrId()), String.valueOf(svcTyp2.getSvcTypId()), patientTyp2.get(1), mod2.getModId());
		
		assertEquals(dataInDb1.size() + dataInDb2.size(), 2,"        The new records should be saved in POS_OVERRIDE table.");

		logger.info("*** Step 3 Action: Focus on the grid, Check on Delete checkbox in the grids for new records, click on Save and Clear button.");
		placeOfServiceCodeConfig.enterIDSearchBox(String.valueOf(dataInDb1.get(0).getSeqId()));
		clickHiddenPageObject(placeOfServiceCodeConfig.getCellInputOfTbl(2,15),0);
		
		placeOfServiceCodeConfig.enterIDSearchBox(String.valueOf(dataInDb2.get(0).getSeqId()));		
		clickHiddenPageObject(placeOfServiceCodeConfig.getCellInputOfTbl(2,15),0);
		
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 3 Expected Result: The page is reset, the selected records are not shown in the grid, new records are removed out of the POS_OVERRIDE table.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();
		
		newTotalRecords = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		assertEquals(newTotalRecords, oldTotalRecord, "        The selected records should not be shown in the grid.");
		
		dataInDb1 = posDao.getPosOverrideByConditionOfServiceCode(clnAccntTyp1.get(0), String.valueOf(fac1.getFacId()), posCode1, String.valueOf(ptLocTyp1.getPtLocId()), String.valueOf(pyr1.getPyrId()), String.valueOf(svcTyp1.getSvcTypId()), patientTyp1.get(1), mod1.getModId());
		dataInDb2 = posDao.getPosOverrideByConditionOfServiceCode(clnAccntTyp2.get(0), String.valueOf(fac2.getFacId()), posCode2, String.valueOf(ptLocTyp2.getPtLocId()), String.valueOf(pyr2.getPyrId()), String.valueOf(svcTyp2.getSvcTypId()), patientTyp2.get(1), mod2.getModId());
		assertEquals(dataInDb1.size(), 0, "        The new records are removed out of the POS_OVERRIDE table.");
		assertEquals(dataInDb2.size(), 0, "        The new records are removed out of the POS_OVERRIDE table.");
		
		driver.quit();
	}
	

	@Test(priority = 1, description = "Verify Reset button.")
	public void testXPR_917() throws Throwable {		
		logger.info("===== Testing - testXPR_917 =====");  	

		payorSearch = new PayorSearch(driver, config);
		placeOfServiceCodeConfig = new PlaceOfServiceCodeConfig(driver);
		
		logger.info("*** Step 1 Expected Result: User login successfully, the Place Of Service Code Configuration Screen is displayed.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();
		int oldTotalRecord = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		
		logger.info("*** Step 2 Action: Add new record in grid.");
		String payorAbbrev = daoManagerXifinRpm.getPayorID(testDb);
		Pyr pyr = payorDao.getPyrByPyrAbbrv(payorAbbrev);
		String procCodeId = daoManagerPlatform.getProcIdFromPROCCD(testDb);
		PosTyp posTyp = posDao.getPosTyp();
		String posCode = posTyp.getPosTypId();
		List<String> patientTyp = daoManagerAccnWS.getRandomPatientTypeFromPT_TYP(testDb);
		SvcTyp svcTyp = serviceDao.getSvcTyp();
		Fac fac = facilityDao.getRandomFac();
		String perFacility = fac.getAbbrv() + " - " + fac.getName();
		Mod mod = modDao.getMod();
		PtLocTyp ptLocTyp = patientDao.getRandomDataFromPtLocTyp();
		List<String> clnAccntTyp = daoManagerXifinRpm.getClnAccntTypFromCLNACCNTTYPI(testDb);
		
		clickOnAddBtn();
		enterDataToAddRecordPlaceOfServiceCode(payorAbbrev, perFacility, procCodeId, svcTyp.getDescr(), mod.getModId(), clnAccntTyp.get(1), ptLocTyp.getDescr(), patientTyp.get(0), posCode);
		
		logger.info("*** Step 2 Expected Result: New record is shown in the grid.");
		assertEquals(lastCellTbl(TBL_POSCODE_CONFIG, 3).getText(), payorAbbrev,"        New record should be shown in the grid with correct Payor Id.");
		
		logger.info("*** Step 3 Action: Click on Reset button.");
		clickHiddenPageObject(placeOfServiceCodeConfig.resetBtn(),0);
		
		logger.info("*** Step 3 Expected Result: The warning message 'Changes have been made to this accession.  Are you sure you want to reset the page?' is displayed.");
		assertTrue(isAlertPresent(), "        Warning message should be displayed.");
		String mes = getAlertMessage();
		assertEquals(mes, WARNING_MESSAGE, "        The warning  message 'Changes have been made to this accession.  Are you sure you want to reset the page?' should be displayed.");
		
		logger.info("*** Step 4 Action: Click on OK button.");
		closeAlertAndGetItsText(true);
		
		logger.info("*** Step 4 Expected Result: The grid is reset, new record is not shown in the grid, no record is saved in POS_OVERRIDE table.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();
		
		int newTotalRecord = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		assertTrue(newTotalRecord == oldTotalRecord, "        The new record should not be shown in the grid.");
		
		List<PosOverride> totalRecordInDB = posDao.getPosOverrideByConditionOfServiceCode(clnAccntTyp.get(0), String.valueOf(fac.getFacId()), posCode, String.valueOf(ptLocTyp.getPtLocId()), String.valueOf(pyr.getPyrId()), String.valueOf(svcTyp.getSvcTypId()), patientTyp.get(1), mod.getModId());
		assertTrue(totalRecordInDB.size() == 0,"        The new record should not be saved in POS_OVERRIDE table.");

		logger.info("*** Step 5 Action: Add new record in the grid.");
		clickOnAddBtn();
		enterDataToAddRecordPlaceOfServiceCode(payorAbbrev, perFacility, procCodeId, svcTyp.getDescr(), mod.getModId(), clnAccntTyp.get(1), ptLocTyp.getDescr() ,patientTyp.get(0), posCode);
		
		logger.info("*** Step 5 Expected Result: New record is shown in the grid.");
		assertEquals(lastCellTbl(TBL_POSCODE_CONFIG, 3).getText(),payorAbbrev,"        New record should be shown in the grid with correct Payor Id.");
		
		logger.info("*** Step 6 Action: Press key 'Alt + R'.");
		placeOfServiceCodeConfig.pressAltR();
		
		logger.info("*** Step 6 Expected Result: The warning message 'Changes have been made to this Accession. Are you sure to reset the page?' is displayed.");
		assertTrue(isAlertPresent(), "        Warning message should be displayed.");
		assertEquals(getAlertMessage(), WARNING_MESSAGE, "        The warning  message 'Changes have been made to this accession.  Are you sure you want to reset the page?' should be displayed.");
		
		logger.info("*** Step 7 Action: Click on OK button.");
		closeAlertAndGetItsText(true);
		
		logger.info("*** Step 7 Expected Result: The page is reset and no record is shown and also saved in POS_OVERRIDE table.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();
		
		newTotalRecord = Convert.toInteger(getTotalResultSearch(placeOfServiceCodeConfig.totalRecordInPlaceOfServiceTbl()));
		assertTrue(newTotalRecord == oldTotalRecord, "        The new record should not be shown in the grid.");
		totalRecordInDB = posDao.getPosOverrideByConditionOfServiceCode(clnAccntTyp.get(0), String.valueOf(fac.getFacId()), posCode, String.valueOf(ptLocTyp.getPtLocId()), String.valueOf(pyr.getPyrId()), String.valueOf(svcTyp.getSvcTypId()), patientTyp.get(1), mod.getModId());
		assertTrue(totalRecordInDB.size() == 0,"        The new record should not be saved in POS_OVERRIDE table.");
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Verify Help Icon ")
	public void testXPR_918() throws Throwable {		
		logger.info("===== Testing - testXPR_918 =====");  	

		payorSearch = new PayorSearch(driver, config);
		placeOfServiceCodeConfig = new PlaceOfServiceCodeConfig(driver);
		
		logger.info("*** Step 1 Expected Result: User login successfully, the Place Of Service Code Configuration Screen is displayed.");
		verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed();

		logger.info("*** Step 2 Action: Click on Help icon at the top of the page.");
		clickHiddenPageObject(placeOfServiceCodeConfig.helpIconHeader(),0);
		
		logger.info("*** Step 2 Expected Result: Help page is opened.");
		String popup = switchToPopupWin();
		assertTrue(driver.getCurrentUrl().contains("help/file_maintenance_tab/codes_menu/p_place_of_service_code_configuration_header.htm"),"        Help page is opened.");
		driver.close();
		switchToParentWin(popup);
		
		logger.info("*** Step 3 Action: Click on Help icon at the Grid.");
		clickHiddenPageObject(placeOfServiceCodeConfig.helpIconOfMainSection(),0);
		
		logger.info("*** Step 3 Expected Result: Help page is opened.");
		popup = switchToPopupWin();
		assertTrue(driver.getCurrentUrl().contains("help/file_maintenance_tab/codes_menu/p_place_of_service_code_configuration.htm"),"        Help page is opened.");
		driver.close();
		switchToParentWin(popup);
		
		logger.info("*** Step 4 Action: Click on Help icon at the footer page.");
		clickHiddenPageObject(placeOfServiceCodeConfig.helpIconBottom(),0);
		
		logger.info("*** Step 4 Expected Result: Help page is opened.");
		popup = switchToPopupWin();
		assertTrue(driver.getCurrentUrl().contains("help/file_maintenance_tab/codes_menu/p_place_of_service_code_configuration_summary.htm"),"        Help page is opened.");
		driver.close();
		switchToParentWin(popup);
		
		driver.quit();
	}

	private void clickOnSaveAndClearBtn() throws Exception {
		clickHiddenPageObject(placeOfServiceCodeConfig.saveAndClearBtn(),0);
		xifinPortalUtils.waitForPageLoaded(wait);
	}

	private void clickOnAddBtn() throws Exception {
		clickHiddenPageObject(placeOfServiceCodeConfig.addIconOfMainSectionTbl(), 0);
	}

	
	public void enterDataToAddRecordPlaceOfServiceCode(String payorId, String perFacility, String procCodeId,
			String svcTyp, String modifier, String clnAccntTyp, String patientLocation, String patientTyp,
			String posCode) throws Exception {
		placeOfServiceCodeConfig.enterPayorIdInput(payorId);
		selectDropDownJQGridClickOnly(placeOfServiceCodeConfig.perfFacillityInput(), perFacility);
		placeOfServiceCodeConfig.enterProcCodeInput(procCodeId);
		selectDropDownJQGridClickOnly(placeOfServiceCodeConfig.serviceTypeInput(), svcTyp);
		selectDropDownJQGridClickOnly(placeOfServiceCodeConfig.modifierInput(), modifier);
		selectDropDownJQGridClickOnly(placeOfServiceCodeConfig.clientAccntTypeInput(), clnAccntTyp);
		selectDropDownJQGridClickOnly(placeOfServiceCodeConfig.patientLocationInput(), patientLocation);
		selectDropDownJQGridClickOnly(placeOfServiceCodeConfig.patientTypeInput(), patientTyp);
		selectDropDownJQGridClickOnly(placeOfServiceCodeConfig.posCodeInput(), posCode);
		placeOfServiceCodeConfig.enterPosCodeInput(posCode);
		placeOfServiceCodeConfig.okPopupBtn().click();

		logger.info("        Enter Data to add Place of Service Code Record with values: " + payorId + ": "
				+ perFacility + ": " + procCodeId + ": " + svcTyp + ": " + modifier + ": " + clnAccntTyp + ": "
				+ patientLocation + ": " + patientTyp + ": " + posCode + EMPTY);
	}

	public void enterDataToAddRecordPlaceOfServiceCode(String payorGroup, String perFacility, String svcTyp,
			String modifier, String clnAccntTyp, String patientLocation, String patientTyp, String posCode)
			throws Exception {
		selectDropDownJQGridClickOnly(placeOfServiceCodeConfig.payorGroupInput(), payorGroup);
		selectDropDownJQGridClickOnly(placeOfServiceCodeConfig.perfFacillityInput(), perFacility);
		selectDropDownJQGridClickOnly(placeOfServiceCodeConfig.serviceTypeInput(), svcTyp);
		selectDropDownJQGridClickOnly(placeOfServiceCodeConfig.modifierInput(), modifier);
		selectDropDownJQGridClickOnly(placeOfServiceCodeConfig.clientAccntTypeInput(), clnAccntTyp);
		selectDropDownJQGridClickOnly(placeOfServiceCodeConfig.patientLocationInput(), patientLocation);
		selectDropDownJQGridClickOnly(placeOfServiceCodeConfig.patientTypeInput(), patientTyp);
		selectDropDownJQGridClickOnly(placeOfServiceCodeConfig.posCodeInput(), posCode);
		placeOfServiceCodeConfig.okPopupBtn().click();

		logger.info("        Enter Data to add Place of Service Code Record with values: " + payorGroup + ": "
				+ perFacility + ": " + svcTyp + ": " + modifier + ": " + clnAccntTyp + ": " + patientLocation + ": "
				+ patientTyp + ": " + posCode + EMPTY);
	}

	public void enterDataToSearchBoxes(String payorId, String perFacility, String procCodeId, String svcTyp,
			String modifier, String clnAccntTyp, String patientLocation, String patientTyp, String posCode)
			throws Exception {
		placeOfServiceCodeConfig.enterPayorIdSearchBox(payorId);
		placeOfServiceCodeConfig.enterPerformingFacilitySearchBox(perFacility);
		placeOfServiceCodeConfig.enterProcedureCodeSearchBox(procCodeId);
		placeOfServiceCodeConfig.enterServiceTypeSearchBox(svcTyp);
		placeOfServiceCodeConfig.enterModifierSearchBox(modifier);
		placeOfServiceCodeConfig.enterClientAccountTypeSearchBox(clnAccntTyp);
		placeOfServiceCodeConfig.enterPatientLocationSearchBox(patientLocation);
		enterValues(placeOfServiceCodeConfig.patientTypeSearchBox(), patientTyp);
		placeOfServiceCodeConfig.enterPosCodeSearchBox(posCode);

		logger.info("        Enter Data search boxes with values: " + payorId + ": " + perFacility + ": " + procCodeId
				+ ": " + svcTyp + ": " + modifier + ": " + clnAccntTyp + ": " + patientLocation + ": " + patientTyp
				+ ": " + posCode + EMPTY);
	}

	private void verifyTheLoadPlaceOfServiceCodeConfigPageIsDisplayed() throws Exception {
		String actTitle = placeOfServiceCodeConfig.placeOfServiceTitle().getText();
		assertEquals(actTitle, PLACE_OF_SERVICE_CODE_CONFIGURATION_TITLE,
				"        The load Place Of Service Code Configuration page is displayed.");
		xifinPortalUtils.waitForPageLoaded(wait);
	}

	private void verifyTheAddRecordPopupIsDisplayed() throws Exception {
		assertTrue(isElementPresent(placeOfServiceCodeConfig.headerPopup(), 5),"        Add record popup is displayed.");
	}

}
