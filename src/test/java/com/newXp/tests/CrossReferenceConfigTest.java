package com.newXp.tests;

import com.mbasys.mars.ejb.entity.testXref.TestXref;
import com.mbasys.mars.ejb.entity.xref.Xref;
import com.mbasys.mars.ejb.entity.xrefCat.XrefCat;
import com.mbasys.mars.ejb.entity.xrefTyp.XrefTyp;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.overall.fileMaintenance.crossReferenceConfig.CrossReferenceConfig;
import com.overall.menu.MenuNavigation;
import com.overall.utils.XifinPortalUtils;
import com.xifin.utils.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNotNull;



public class CrossReferenceConfigTest extends SeleniumBaseTest {
	
	private static final String EMPTY = "";
	private static final String DATA_LOCKED = "Data locked";
	private static final String TITLE_TEXT = "Cross Reference Configuration";
	private static final String CROSS_REFERENCE_MEMBERS = "Cross Reference Members";

	private int xrefId;
	private Actions action;
	private TestXref testXref;
	private ListUtil listUtil;
	private TimeStamp timeStamp;
	private MenuNavigation menuNavigation;
	private RandomCharacter randomCharacter;
	private XifinPortalUtils xifinPortalUtils;
	private CrossReferenceConfig crossReferenceConfig;
	private com.mbasys.mars.ejb.entity.test.Test test;
	
	@BeforeMethod(alwaysRun = true)
    @Parameters({ "ssoXpUsername", "ssoXpPassword" })
    public void beforeMethod(String ssoXpUsername, String ssoXpPassword, Method method) {
        try {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            logIntoSso(ssoXpUsername, ssoXpPassword);
            menuNavigation = new MenuNavigation(driver, config);
	        menuNavigation.navigateToCrossReferenceConfigPage();
        } catch (Exception e) {
            logger.error("Error running BeforeMethod", e);
        }
    }
	
	@AfterMethod(alwaysRun = true)
	public void cleanData(Method method) {
		try {
			logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
			if (testXref != null) {
				testXref.setResultCode(ErrorCodeMap.DELETED_RECORD);
				testDao.setTestXref(testXref);
			}
			if (test != null) {
				test.setResultCode(ErrorCodeMap.DELETED_RECORD);
				testDao.setTest(test);
			}
			if (xrefId != 0) {
				Xref xref = crossReferenceDao.getXrefByXrefId(xrefId);
				xref.setResultCode(ErrorCodeMap.DELETED_RECORD);
				crossReferenceDao.setXref(xref);
			}
		} catch (Exception e) {
			logger.error("Error running afterMethod", e);
		}
	}

	/**
	 * Test Script
	 */
	
	@Test(priority = 1, description = "Verify Cross Reference Members are displayed correctly.")
	public void testXPR_823() throws Throwable {		
		logger.info("===== Testing - testXPR_823 =====");  	

		listUtil = new ListUtil();
		crossReferenceConfig = new CrossReferenceConfig(driver);
		xifinPortalUtils = new XifinPortalUtils(driver);
		
		logger.info("*** Step 1 Expected Result: User login successfully, Cross Reference Configuration page is displayed.");
		verifyCrossReferenceConfigurationPageIsDisplayed();
		
		logger.info("*** Step 2 Action: - Select Category in Xref_cat,"
				+ "- Select random type based on selected Category in Xref_typ with above Xref_cat_id,"
				+ "- Tab out.");
		List<String> categoryInfo = daoManagerXifinRpm.getRandomCategoryFromXrefCat(testDb);
		ArrayList<String> xRefTypeInfo = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat(categoryInfo.get(1), testDb);
		crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
		crossReferenceConfig.selectTypeDropdown(xRefTypeInfo.get(3));
		Thread.sleep(5000);
		while (checkDataLock()) {
			menuNavigation.navigateToCrossReferenceConfigPage();
			String catId = categoryInfo.get(0);
			do {
				categoryInfo = daoManagerXifinRpm.getRandomCategoryFromXrefCat(testDb);
			}while (categoryInfo.get(0).equalsIgnoreCase(catId));
			xRefTypeInfo = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat(categoryInfo.get(1), testDb);
			crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
			crossReferenceConfig.selectTypeDropdown(xRefTypeInfo.get(3));
			Thread.sleep(5000);
			
		}
		
		logger.info("*** Step 2 Expected Result: The xref are displayed correctly in XREF table with correct Xref_typ_id.");
		Thread.sleep(5000);
		int pageNumberRecords = Integer.parseInt(getTotalResultSearch(crossReferenceConfig.totalRecordInTbl()));
		List<Integer> cols = Arrays.asList(new Integer[] {3,4});
		scrollToElement(crossReferenceConfig.nextIconInCrossReferenMem());
		Thread.sleep(3000);
		List<List<String>> xrefListUI = getAllDataOnAllPageTbl(this, pageNumberRecords, "tbl_crossReferenceMember", cols, crossReferenceConfig.nextIconInCrossReferenMem());
		List<String> abbrevListUI = listUtil.getListFromListList(xrefListUI, 0);
		List<String> abbrevListUIConvert = new ArrayList<String>();
		for (int i = 0; i < abbrevListUI.size(); i++) {
			if (abbrevListUI.get(i).equals(" ")) {
				abbrevListUIConvert.add(abbrevListUI.get(i).trim());
			} else {
				abbrevListUIConvert.add(abbrevListUI.get(i));
			}
		}
		List<List<String>> xrefListDB = daoManagerXifinRpm.getDataFromXREFByXrefTypId(xRefTypeInfo.get(3),testDb);
		List<String> abbrevListDB = listUtil.getListFromListList(xrefListDB, 4);
		assertTrue(listUtil.compareLists(abbrevListDB, abbrevListUIConvert), "        The xref should be displayed correctly in Xref table.");
		
		driver.quit();
	}

	
	@Test(priority = 1, description = "Update Xref_Typ with checked on check box.")
	public void testXPR_824() throws Throwable {		
		logger.info("===== Testing - testXPR_824 =====");  	

		listUtil = new ListUtil();
		crossReferenceConfig = new CrossReferenceConfig(driver);
		xifinPortalUtils = new XifinPortalUtils(driver);
		
		logger.info("*** Step 1 Expected Result: User login successfully, Cross Reference Configuration page is displayed.");
		verifyCrossReferenceConfigurationPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Select Category, select Type properly, tab out.");
		List<String> categoryInfo = daoManagerXifinRpm.getRandomCategoryFromXrefCat(testDb);
		ArrayList<String> xRefTypeList = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat(categoryInfo.get(1), testDb);
		crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
		crossReferenceConfig.selectTypeDropdown(xRefTypeList.get(3));
		Thread.sleep(5000);
		while (checkDataLock()) {
			menuNavigation.navigateToCrossReferenceConfigPage();
			String catId = categoryInfo.get(0);
			do {
				categoryInfo = daoManagerXifinRpm.getRandomCategoryFromXrefCat(testDb);
			}while (categoryInfo.get(0).equalsIgnoreCase(catId));
			xRefTypeList = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat(categoryInfo.get(1), testDb);
			crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
			crossReferenceConfig.selectTypeDropdown(xRefTypeList.get(3));
			Thread.sleep(5000);
			
		}
		
		logger.info("*** Step 2 Expected Result: Cross Reference Members table is displayed.");
		assertTrue(isElementPresent(crossReferenceConfig.crossReferenceMembersTbl(),5), "        Cross Reference Configuration table is displayed.");	
		
		logger.info("*** Step 3 Action: Check all check boxes: Type Required, Allow Duplicate, Single Use Xrefs, Click on Save and Clear button.");
		List<String> xrefTypInfo = daoManagerXifinRpm.getDataFromXrefTypByXrefTypId(xRefTypeList.get(0),testDb); 
		if (!xrefTypInfo.get(4).equalsIgnoreCase("1")) {
			clickHiddenPageObject(crossReferenceConfig.typRequiredCheck(), 0);
		}
		if (!xrefTypInfo.get(5).equalsIgnoreCase("1")) {
			clickHiddenPageObject(crossReferenceConfig.allowDupTypeCheck(), 0);
		}
		if (!xrefTypInfo.get(6).equalsIgnoreCase("1")) {
			clickHiddenPageObject(crossReferenceConfig.singleUseXrefsCheck(), 0);
		}
		clickHiddenPageObject(crossReferenceConfig.saveAndClearBtn(),0);
		
		logger.info("*** Step 3 Expected Result: The page is reset, Xref_Typ.B_Required, B_Dups_Allowed, B_Single_use = 1 for selected typ.");
		xifinPortalUtils.waitForPageLoaded(wait);
		List<String> newXrefTypInfo = daoManagerXifinRpm.getDataFromXrefTypByXrefTypId(xRefTypeList.get(0),testDb); 
		assertEquals(newXrefTypInfo.get(4), "1", "        Xref_Typ.B_Required = 1.");
		assertEquals(newXrefTypInfo.get(5), "1", "        Xref_Typ.B_Dups_Allowed = 1.");
		assertEquals(newXrefTypInfo.get(6), "1", "        Xref_Typ.B_Single_use = 1.");
		
		logger.info("*** Step 4 Action: Reload the Category and Type at step 2, tab out.");
		assertTrue(isElementPresent(crossReferenceConfig.categoryDropDownBox(), 5),"        Category dropdown box is displayed.");
		assertTrue(isElementPresent(crossReferenceConfig.typeDropDownBox(),5),"        Type dropdown box is displayed.");
		crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
		crossReferenceConfig.selectTypeDropdown(xRefTypeList.get(3));
		
		logger.info("*** Step 4 Expected Result: Cross Reference Members table is displayed with Type Required, Allow Duplicate Type and Single Use Xrefs checkboxes are checked.");
		assertTrue(crossReferenceConfig.typRequiredCheck().isSelected(),"        Type Required checkbox should be checked.");
		assertTrue(crossReferenceConfig.allowDupTypeCheck().isSelected(),"        Allow Duplicate Type checkbox should be checked.");
		assertTrue(crossReferenceConfig.singleUseXrefsCheck().isSelected(),"        Single Use Xref checkbox should be checked.");
		
		//revert data
		List<String> updatedFields = new ArrayList<String>();
		updatedFields.add("B_REQUIRED");
		updatedFields.add("B_SINGLE_USE");
		updatedFields.add("B_DUPS_ALLOWED");
		List<Object> updatedValues = new ArrayList<Object>();
		updatedValues.add(xrefTypInfo.get(4));
		updatedValues.add(xrefTypInfo.get(6));
		updatedValues.add(xrefTypInfo.get(5));
		List<String> whereFields = new ArrayList<String>();
		whereFields.add("PK_XREF_TYP_ID");
		List<Object> whereValues = new ArrayList<Object>();
		whereValues.add(xRefTypeList.get(0));
		daoManagerXifinRpm.updatedRecords("xref_typ", updatedFields, updatedValues, whereFields, whereValues, testDb);
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Add new XrefId for any type with valid data.")
	public void testXPR_825() throws Throwable {		
		logger.info("===== Testing - testXPR_825 =====");  	

		listUtil = new ListUtil();
		randomCharacter = new RandomCharacter(driver);
		crossReferenceConfig = new CrossReferenceConfig(driver);
		xifinPortalUtils = new XifinPortalUtils(driver);
		
		logger.info("*** Step 1 Expected Result: User login successfully, Cross Reference Configuration page is displayed.");
		verifyCrossReferenceConfigurationPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Select Category, select random type properly, tab out.");
		List<String> categoryInfo = daoManagerXifinRpm.getRandomCategoryFromXrefCat(testDb);
		ArrayList<String> xRefTypeList = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat(categoryInfo.get(1), testDb);
		crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
		crossReferenceConfig.selectTypeDropdown(xRefTypeList.get(3));
		Thread.sleep(5000);
		while (checkDataLock()) {
			menuNavigation.navigateToCrossReferenceConfigPage();
			String catId = categoryInfo.get(0);
			do {
				categoryInfo = daoManagerXifinRpm.getRandomCategoryFromXrefCat(testDb);
			}while (categoryInfo.get(0).equalsIgnoreCase(catId));
			xRefTypeList = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat(categoryInfo.get(1), testDb);
			crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
			crossReferenceConfig.selectTypeDropdown(xRefTypeList.get(3));
			Thread.sleep(5000);
			
		}
		
		logger.info("*** Step 2 Expected Result: The Cross Reference Members table is displayed.");
		assertTrue(isElementPresent(crossReferenceConfig.crossReferenceMembersTbl(), 5), "        Cross Reference Configuration table is displayed.");
		
		logger.info("*** Step 3 Action: Click on Add icon.");
		clickHiddenPageObject(crossReferenceConfig.addTblIcon(),0);
		
		logger.info("*** Step 3 Expected Result: Add record popup is opened.");
		assertTrue(isElementPresent(crossReferenceConfig.addRecordPopupTitle(), 5), "        Add record popup is opened.");
		
		logger.info("*** Step 4 Action: Input new Abbrew and Description, Click on OK button.");
		String abbrev = randomCharacter.getRandomAlphaNumericString(5);
		String desc = randomCharacter.getRandomAlphaNumericString(10);
		crossReferenceConfig.sendAbbrevInput(abbrev);
		crossReferenceConfig.sendDescInput(desc);
		clickHiddenPageObject(crossReferenceConfig.okBtn(),0);
		
		logger.info("*** Step 4 Expected Result: New record is shown in the grid.");
		Thread.sleep(5000);
		assertEquals(lastCellTbl("tbl_crossReferenceMember", 3).getText(), abbrev.toUpperCase(), "        New record is shown in the grid with correct Abbrev.");
		assertEquals(lastCellTbl("tbl_crossReferenceMember", 4).getText(), desc.toUpperCase(), "        New record is shown in the grid with correct Description.");
		
		logger.info("*** Step 5 Action: Click on Save and Clear button.");
		clickHiddenPageObject(crossReferenceConfig.saveAndClearBtn(),0);
		
		logger.info("*** Step 5 Expected Result: The page is reset, new record is inserted in Xref table with correct PK_Xref_Typ_Id, Desc, Abbrew.");
		List<String> xRefInfo = daoManagerXifinRpm.getDataFromXrefByXrefTypIdAndAbbrev(xRefTypeList.get(0),abbrev,testDb);
		assertEquals(xRefInfo.get(1), xRefTypeList.get(0), "        New record is inserted in Xref table with correct PK_Xref_Typ_Id.");
		assertEquals(xRefInfo.get(2), desc, "        New record is inserted in Xref table with correct Desc.");
		assertEquals(xRefInfo.get(3), abbrev, "        New record is inserted in Xref table with correct Abbrev.");
		
		logger.info("*** Step 6 Action: Reload the Catergory and Type at step 2, tab out.");
		assertTrue(isElementPresent(crossReferenceConfig.categoryDropDownBox(), 5),"        Category dropdown box is displayed.");
		assertTrue(isElementPresent(crossReferenceConfig.typeDropDownBox(),5),"        Type dropdown box is displayed.");
		crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
		crossReferenceConfig.selectTypeDropdown(xRefTypeList.get(3));
		
		logger.info("*** Step 6 Expected Result: The new record is shown in the grid with correct information.");
		crossReferenceConfig.sendAbbrevSearchInput(abbrev);
		assertEquals(abbrev.toUpperCase(), crossReferenceConfig.cellRowInTbl(2,3).getText(),"        The new record should be displayed with correct Abbrev infomation.");
		assertEquals(desc.toUpperCase(), crossReferenceConfig.cellRowInTbl(2,4).getText(),"        The new record should be displayed with correct description infomation.");
		
		logger.info("*** Step 7 Action: Remove new data out of DB.");
		daoManagerPlatform.deleteDataFromTableByCondition("XREF", "Abbrev = '" + abbrev + "'", testDb);

		logger.info("*** Step 7 Expected Result: The new data is removed properly.");
		xRefInfo = daoManagerXifinRpm.getDataFromXrefByXrefTypIdAndAbbrev(xRefTypeList.get(0),abbrev,testDb);
		assertTrue(xRefInfo.size() < 1, "        The new data is removed out of DB.");
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Remove data that is assigned for TEST_XREF.")
	public void testXPR_826() throws Throwable {		
		logger.info("===== Testing - testXPR_826 =====");  	

		action = new Actions(driver);
		crossReferenceConfig = new CrossReferenceConfig(driver);
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);
		xifinPortalUtils = new XifinPortalUtils(driver);
		
		logger.info("*** Prepare data: Prepare data in Xref, Test, Test_Xref table");
		Date curDt = ConvertUtil.convertStringToSQLDate(timeStamp.getCurrentDate("DD-MMM-YY"), "DD-MMM-YY");
		XrefTyp xrefTyp = crossReferenceDao.getRandomXrefTypByXrefCatId(6);
		XrefCat xrefCat = crossReferenceDao.getXrefCatByXrefCatId(xrefTyp.getXrefCatId());
		crossReferenceConfig.selectCategoryDropdown(xrefCat.getDescr());
		crossReferenceConfig.selectTypeDropdown(xrefTyp.getDescr());
		Thread.sleep(5000);
		while (checkDataLock()) {
			menuNavigation.navigateToCrossReferenceConfigPage();
			int xrefTypId = xrefTyp.getXrefTypId();
			do {
				xrefTyp = crossReferenceDao.getRandomXrefTypByXrefCatId(6);
			}while (xrefTyp.getXrefTypId() == xrefTypId);
			xrefCat = crossReferenceDao.getXrefCatByXrefCatId(xrefTyp.getXrefCatId());
			crossReferenceConfig.selectCategoryDropdown(xrefCat.getDescr());
			crossReferenceConfig.selectTypeDropdown(xrefTyp.getDescr());
			Thread.sleep(5000);
			
		}
		String xrefDescr = randomCharacter.getRandomAlphaString(5);
		String xrefAbbrev = randomCharacter.getRandomAlphaString(5);
		
		test = new com.mbasys.mars.ejb.entity.test.Test();
		int nextTestId = databaseSequenceDao.getNextSeqIdFromTBLByColName("PK_TEST_ID", "TEST");
		String testName = randomCharacter.getRandomAlphaString(5);
		String testAbbrev = randomCharacter.getRandomAlphaString(5);
		test.setTestId(nextTestId);
		test.setTestAbbrev(testAbbrev);
		test.setName(testName);
		test.setResultCode(ErrorCodeMap.NEW_RECORD);
		testDao.setTest(test);
		
		
		clickHiddenPageObject(crossReferenceConfig.addTblIcon(), 0);

		crossReferenceConfig.sendAbbrevInput(xrefAbbrev);
		crossReferenceConfig.sendDescInput(xrefDescr);
		clickHiddenPageObject(crossReferenceConfig.okBtn(),0);
		clickHiddenPageObject(crossReferenceConfig.saveAndClearBtn(),0);
		xifinPortalUtils.waitForPageLoaded(wait);
		Thread.sleep(5000);
		
		Xref listXref = crossReferenceDao.getXrefByAbbrevAndDescr(xrefAbbrev,xrefDescr);
		testXref = new TestXref();
		testXref.setTestId(nextTestId);
		testXref.setXrefId(listXref.getXrefId());
		testXref.setEffDt(curDt);
		testXref.setResultCode(ErrorCodeMap.NEW_RECORD);
		testDao.setTestXref(testXref);
		
		logger.info("*** Step 1 Expected Result: User login successfully, Cross Reference Configuration page is displayed.");
		verifyCrossReferenceConfigurationPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Get XrefId");
		xrefId = listXref.getXrefId();
		
		logger.info("*** Step 2 Expected Result: Data is ready for using.");
		assertNotNull(listXref, "        Xref should be ready for using.");
		
		logger.info("*** Step 3 Action: Select CATEGORY , TYPE , XREF_MEMBER as above query , check on Delete check box for this XrefID and Click Save and Clear button");
		crossReferenceConfig.selectCategoryDropdown(xrefCat.getDescr());
		crossReferenceConfig.selectTypeDropdown(xrefTyp.getDescr());
        action.sendKeys(Keys.TAB).build().perform();
		clickHiddenPageObject(crossReferenceConfig.lastCellRowCheckDelInTbl(5),0);
		clickHiddenPageObject(crossReferenceConfig.saveAndClearBtn(),0);
		
		logger.info("*** Step 3 Expected Result: The error message 'The Xref Member cannot be deleted. It is assigned to either an Adjustment(s), Client(s), UPIN(s), facility(s) or Payor(s). To delete an xref member: ....' ");
		Thread.sleep(5000);
		assertTrue(crossReferenceConfig.errMessSection().getText().contains("The Xref Member cannot be deleted. It is assigned to either an Adjustment(s)"),"        Err Message should be shown.");
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Update Xref Id with new Description.")
	public void testXPR_827() throws Throwable {		
		logger.info("===== Testing - testXPR_827 =====");  	

		listUtil = new ListUtil();
		randomCharacter = new RandomCharacter(driver);
		crossReferenceConfig = new CrossReferenceConfig(driver);
		
		logger.info("*** Step 1 Expected Result: User login successfully, Cross Reference Configuration page is displayed.");
		verifyCrossReferenceConfigurationPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Select CATEGORY , TYPE > Tab out");
		List<String> categoryInfo = daoManagerXifinRpm.getRandomCategoryFromXrefCat(testDb);
		ArrayList<String> xRefTypeInfo = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat(categoryInfo.get(1), testDb);
		crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
		crossReferenceConfig.selectTypeDropdown(xRefTypeInfo.get(3));
		Thread.sleep(5000);
		while (checkDataLock()) {
			menuNavigation.navigateToCrossReferenceConfigPage();
			String catId = categoryInfo.get(0);
			do {
				categoryInfo = daoManagerXifinRpm.getRandomCategoryFromXrefCat(testDb);
			}while (categoryInfo.get(0).equalsIgnoreCase(catId));
			xRefTypeInfo = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat(categoryInfo.get(1), testDb);
			crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
			crossReferenceConfig.selectTypeDropdown(xRefTypeInfo.get(3));
			Thread.sleep(5000);
			
		}
		
		logger.info("*** Step 2 Expected Result: The Cross Reference Member table is displayed.");
		assertTrue(isElementPresent(crossReferenceConfig.crossReferenceMembersTbl(), 5), "        The Cross Reference Member table shoud be displayed.");
		
		logger.info("*** Step 3 Action: Add new Xref with valid data");
		List<List<String>> xrefListOldInDB = daoManagerXifinRpm.getDataFromXREFByXrefTypId(xRefTypeInfo.get(3),testDb);
		String abbrev = "auto" + randomCharacter.getRandomAlphaNumericString(3);
		String descr = "auto" + randomCharacter.getRandomAlphaString(7);
		clickHiddenPageObject(crossReferenceConfig.addTblIcon(), 0);

		crossReferenceConfig.sendAbbrevInput(abbrev);
		crossReferenceConfig.sendDescInput(descr);
		clickHiddenPageObject(crossReferenceConfig.okBtn(),0);
		
		logger.info("*** Step 3 Expected Result: New data is displayed on table.");
		Thread.sleep(5000);
		assertTrue(getColumnValue(crossReferenceConfig.crossReferenceMembersTbl(), abbrev), "        New data should be added to table.");
		assertTrue(getColumnValue(crossReferenceConfig.crossReferenceMembersTbl(), descr), "        New data should be added to table.");
		
		logger.info("*** Step 4 Action: Click on Save and Clear button.");
		clickHiddenPageObject(crossReferenceConfig.saveAndClearBtn(),0);
		Thread.sleep(5000);
		
		logger.info("*** Step 4 Expected Result: Page is reset and New record is saved in Xref Table.");
		assertEquals(crossReferenceConfig.abbrevInput().getText(), EMPTY, "        abbrev input is empty.");
		assertEquals(crossReferenceConfig.descrInput().getText(), EMPTY, "        descr input is empty.");

		List<List<String>> xrefListNewInDB = daoManagerXifinRpm.getDataFromXREFByXrefTypId(xRefTypeInfo.get(3),testDb);
		assertTrue(xrefListNewInDB.size() > xrefListOldInDB.size(),"        A new record is saved in Xref table");
		assertTrue(ConvertUtil.convertListToString(listUtil.getListFromListList(xrefListNewInDB, 4)).contains(abbrev.toUpperCase()),"        The new record are corrected abbrev.");
		assertTrue(ConvertUtil.convertListToString(listUtil.getListFromListList(xrefListNewInDB, 2)).contains(descr.toUpperCase()),"        The new record are corrected descr.");
		
		logger.info("*** Step 5 Action: Reselect data at step 4.");
		crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
		crossReferenceConfig.selectTypeDropdown(xRefTypeInfo.get(3));
		
		logger.info("*** Step 5 Expected Result: The Cross Reference Member table is displayed.");
		assertTrue(isElementPresent(crossReferenceConfig.crossReferenceMembersTbl(),5),"        The Cross Reference Member table shoud be displayed.");
		
		logger.info("*** Step 6 Action: Reselect data at step check on Delete checkbox for new data.");
		crossReferenceConfig.sendAbbrevSearchInput(abbrev);
		clickHiddenPageObject(crossReferenceConfig.cellRowCheckDelInTbl(2, 5), 0);
		
		logger.info("*** Step 6 Expected Result: Row is marked as Delete.");
		assertEquals(rowIsMarkForDelete(crossReferenceConfig.crossReferenceMembersTbl(), 2), true,"        Row should be marked as Delete.");
		
		logger.info("*** Step 7 Action: Click on Save and Clear button.");
		clickHiddenPageObject(crossReferenceConfig.saveAndClearBtn(), 0);
		
		logger.info("*** Step 7 Expected Result: Data is remove out of table.");
		List<List<String>> xrefListAfterDelete = daoManagerXifinRpm.getDataFromXREFByXrefTypId(xRefTypeInfo.get(3),testDb);
		assertEquals(crossReferenceConfig.abbrevInput().getText(), EMPTY, "        abbrev input is empty.");
		assertEquals(crossReferenceConfig.descrInput().getText(), EMPTY, "        descr input is empty.");
		assertTrue(xrefListOldInDB.size() == xrefListAfterDelete.size(),"        A new record is remove out of table");
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Delete XrefId.")
	public void testXPR_828() throws Throwable {		
		logger.info("===== Testing - testXPR_828 =====");  	

		listUtil = new ListUtil();
		randomCharacter = new RandomCharacter(driver);
		crossReferenceConfig = new CrossReferenceConfig(driver);
		xifinPortalUtils = new XifinPortalUtils(driver);
		
		logger.info("*** Step 1 Expected Result: User login successfully, Cross Reference Configuration page is displayed.");
		verifyCrossReferenceConfigurationPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Select Category and Type properly, tab out.");
		List<String> categoryInfo = daoManagerXifinRpm.getRandomCategoryFromXrefCat(testDb);
		ArrayList<String> xRefTypeList = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat(categoryInfo.get(1), testDb);
		crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
		crossReferenceConfig.selectTypeDropdown(xRefTypeList.get(3));
		Thread.sleep(5000);
		while (checkDataLock()) {
			menuNavigation.navigateToCrossReferenceConfigPage();
			String catId = categoryInfo.get(0);
			do {
				categoryInfo = daoManagerXifinRpm.getRandomCategoryFromXrefCat(testDb);
			}while (categoryInfo.get(0).equalsIgnoreCase(catId));
			xRefTypeList = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat(categoryInfo.get(1), testDb);
			crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
			crossReferenceConfig.selectTypeDropdown(xRefTypeList.get(3));
			Thread.sleep(5000);
			
		}
		
		logger.info("*** Step 2 Expected Result: The Cross Reference Members table is displayed.");
		assertTrue(isElementPresent(crossReferenceConfig.crossReferenceMembersTbl(),5), "        Cross Reference Configuration table is displayed.");	
		
		logger.info("*** Step 3 Action: Add new Xref Id with valid Data.");
		clickHiddenPageObject(crossReferenceConfig.addTblIcon(),0);
		String abbrev = randomCharacter.getRandomAlphaNumericString(5);
		String desc = randomCharacter.getRandomAlphaNumericString(10);
		crossReferenceConfig.sendAbbrevInput(abbrev);
		crossReferenceConfig.sendDescInput(desc);
		assertTrue(isElementPresent(crossReferenceConfig.okBtn(),5),"        OK Button is displayed.");
		clickHiddenPageObject(crossReferenceConfig.okBtn(),0);
		
		logger.info("*** Step 3 Expected Result: New record is shown in the Grid.");
		Thread.sleep(5000);
		assertEquals(lastCellTbl("tbl_crossReferenceMember", 3).getText(), abbrev.toUpperCase(), "        New record is shown in the grid with correct Abbrev.");
		assertEquals(lastCellTbl("tbl_crossReferenceMember", 4).getText(), desc.toUpperCase(), "        New record is shown in the grid with correct Description.");
		
		logger.info("*** Step 4 Action: Click on Save and Clear button.");
		clickHiddenPageObject(crossReferenceConfig.saveAndClearBtn(),0);
		
		logger.info("*** Step 4 Expected Result: The table is reset. New record is saved into Xref table.");
		List<String> xRefInfo = daoManagerXifinRpm.getDataFromXrefByXrefTypIdAndAbbrev(xRefTypeList.get(0),abbrev,testDb);
		assertEquals(xRefInfo.get(1),xRefTypeList.get(0), "        New record is inserted in Xref table with correct PK_Xref_Typ_Id.");
		assertEquals(xRefInfo.get(2),desc, "        New record is inserted in Xref table with correct Desc.");
		assertEquals(xRefInfo.get(3),abbrev, "        New record is inserted in Xref table with correct Abbrev.");		
		
		logger.info("*** Step 5 Action: Reselected Category and Type again, tab out.");
		Thread.sleep(5000);
		assertTrue(isElementPresent(crossReferenceConfig.categoryDropDownBox(), 5),"        Category dropdown box is displayed.");
		assertTrue(isElementPresent(crossReferenceConfig.typeDropDownBox(), 5),"        Type dropdown box is displayed.");
		crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
		crossReferenceConfig.selectTypeDropdown(xRefTypeList.get(3));
		
		logger.info("*** Step 5 Expected Result: The Cross Reference Members table is displayed.");
		crossReferenceConfig.sendAbbrevSearchInput(abbrev);
		assertEquals(abbrev.toUpperCase(),crossReferenceConfig.cellRowInTbl(2,3).getText(), "        The new record should be displayed with correct Abbrev infomation.");
		assertEquals(desc.toUpperCase(), crossReferenceConfig.cellRowInTbl(2,4).getText(), "        The new record should be displayed with correct description infomation.");
		
		logger.info("*** Step 6 Action: Check on Delete check box in the grid for new XrefId.");
		crossReferenceConfig.sendAbbrevSearchInput(abbrev);
		clickHiddenPageObject(crossReferenceConfig.cellRowCheckDelInTbl(2,5),0);
		
		logger.info("*** Step 6 Expected Result: The record is highlighted as deleted record.");
		assertEquals(crossReferenceConfig.cellRowCheckDelInTbl(2,5).getAttribute("value"), "true","        The record is highlighted as deleted record.");
		
		logger.info("*** Step 7 Action: Click on Save and Clear button.");
		clickHiddenPageObject(crossReferenceConfig.saveAndClearBtn(), 0);
		
		logger.info("*** Step 7 Expected Result: The page is reset, the selected record is removed out of Xref table.");
		xRefInfo = new ArrayList<String> ();
		xRefInfo = daoManagerXifinRpm.getDataFromXrefByXrefTypIdAndAbbrev(xRefTypeList.get(0),abbrev,testDb);
		assertTrue(xRefInfo.size() < 1, "        The selected record is removed out of Xref table.");
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Verify Reset button")
	public void testXPR_829() throws Throwable {		
		logger.info("===== Testing - testXPR_829 =====");  	

		listUtil = new ListUtil();
		randomCharacter = new RandomCharacter();
		crossReferenceConfig = new CrossReferenceConfig(driver);
		xifinPortalUtils = new XifinPortalUtils(driver);
		
		logger.info("*** Step 1 Expected result: User login successful, the Cross Reference Configuration screen is displayed.");
		verifyCrossReferenceConfigurationPageIsDisplayed();

		logger.info("*** Step 2 Action: Select Category Id and Type properly, tab out.");
		List<String> categoryInfo = daoManagerXifinRpm.getRandomCategoryFromXrefCat(testDb);
		ArrayList<String> xRefTypeInfo = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat(categoryInfo.get(1), testDb);
		crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
		crossReferenceConfig.selectTypeDropdown(xRefTypeInfo.get(3));
		Thread.sleep(5000);
		while (checkDataLock()) {
			menuNavigation.navigateToCrossReferenceConfigPage();
			String catId = categoryInfo.get(0);
			do {
				categoryInfo = daoManagerXifinRpm.getRandomCategoryFromXrefCat(testDb);
			}while (categoryInfo.get(0).equalsIgnoreCase(catId));
			xRefTypeInfo = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat(categoryInfo.get(1), testDb);
			crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
			crossReferenceConfig.selectTypeDropdown(xRefTypeInfo.get(3));
			Thread.sleep(5000);
			
		}
		
		logger.info("*** Step 2 Expected result: The Cross Reference Members table is displayed.");
		Thread.sleep(5000);
		assertEquals(crossReferenceConfig.headerCrossReferenceMembersTbl().getText(), CROSS_REFERENCE_MEMBERS,"        The Cross Reference Members table is display with name 'Cross Reference Members'.");

		logger.info("*** Step 3 Action: Add a new record in the grid.");
		String abbrev = "testAutoAbrev" + randomCharacter.getRandomAlphaNumericString(3);
		String descr = "testAutoDescr" + randomCharacter.getRandomAlphaString(7);
		clickHiddenPageObject(crossReferenceConfig.addTblIcon(),0);
		crossReferenceConfig.sendAbbrevInput(abbrev);
		crossReferenceConfig.sendDescInput(descr);
		clickHiddenPageObject(crossReferenceConfig.okBtn(),0);
		
		logger.info("*** Step 3 Expected result: A new record is shown in the grid.");
		assertEquals(abbrev.toUpperCase(), lastCellTbl("tbl_crossReferenceMember", 3).getText(), "        New record is shown in the grid with correct Abbrev.");
		assertEquals(descr.toUpperCase(), lastCellTbl("tbl_crossReferenceMember", 4).getText(),"        New record is shown in the grid with correct Description.");
		
		List<List<String>> xrefListOldInDB = daoManagerXifinRpm.getDataFromXREFByXrefTypId(xRefTypeInfo.get(3),testDb);
		
		logger.info("*** Step 4 Action: Click on Reset btn.");
		clickHiddenPageObject(crossReferenceConfig.resetBtn(), 0);
		
		logger.info("*** Step 4 Expected result: The warning message is displayed.");
		assertTrue(isAlertPresent(), "        Warning message should be displayed.");
        String alertMess = getAlertMessage();
		assertTrue(alertMess.contains("Changes have been made to this cross reference configuration. Are you sure you want to reset the page?"),"        The warning message should be displayed");

		logger.info("*** Step 5 Action: Click on OK btn.");
		closeAlertAndGetItsText(true);
		
		logger.info("*** Step 5 Expected result: The new record is not save in table.");
		List<List<String>> xrefListNewInDB = daoManagerXifinRpm.getDataFromXREFByXrefTypId(xRefTypeInfo.get(3),testDb);
		assertTrue(xrefListNewInDB.size() == xrefListOldInDB.size(),"        A new record is not saved in Xref table");
		Assert.assertFalse(ConvertUtil.convertListToString(listUtil.getListFromListList(xrefListNewInDB, 4)).contains(abbrev.toUpperCase()),"        The new record are corrected abbrev.");
		Assert.assertFalse(ConvertUtil.convertListToString(listUtil.getListFromListList(xrefListNewInDB, 2)).contains(descr.toUpperCase()),"        The new record are corrected descr.");
		
		driver.quit();
	}

	@Test(priority = 1, description = "Verify Run Audit button")
	public void testXPR_830() throws Throwable {		
		logger.info("===== Testing - testXPR_830 =====");  	

		listUtil = new ListUtil();
		randomCharacter = new RandomCharacter();
		crossReferenceConfig  = new CrossReferenceConfig(driver);
		xifinPortalUtils = new XifinPortalUtils(driver);
		
		logger.info("*** Step 1 Expected result: User login successful, the Cross Reference Configuration screen is displayed.");
		verifyCrossReferenceConfigurationPageIsDisplayed();

		logger.info("*** Step 2 Action: Select any Category Id and Type properly, tab out.");
		List<String> categoryInfo = daoManagerXifinRpm.getRandomCategoryFromXrefCat(testDb);
	    ArrayList<String> xRefTypeInfo = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat(categoryInfo.get(1), testDb);
		crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
		crossReferenceConfig.selectTypeDropdown(xRefTypeInfo.get(3));
		Thread.sleep(5000);
		while (checkDataLock()) {
			menuNavigation.navigateToCrossReferenceConfigPage();
			String catId = categoryInfo.get(0);
			do {
				categoryInfo = daoManagerXifinRpm.getRandomCategoryFromXrefCat(testDb);
			}while (categoryInfo.get(0).equalsIgnoreCase(catId));
			xRefTypeInfo = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat(categoryInfo.get(1), testDb);
			crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
			crossReferenceConfig.selectTypeDropdown(xRefTypeInfo.get(3));
			Thread.sleep(5000);
			
		}
		
		logger.info("*** Step 2 Expected result: The Cross Reference Members table is displayed.");
		Thread.sleep(5000);
		assertEquals(crossReferenceConfig.headerCrossReferenceMembersTbl().getText(), CROSS_REFERENCE_MEMBERS,"        The Cross Reference Members table is display with name 'Cross Reference Members'.");
		List<List<String>> xrefListOldInDB = daoManagerXifinRpm.getDataFromXREFByXrefTypId(xRefTypeInfo.get(3),testDb);

		logger.info("*** Step 3 Action: Add new Xrefld with valid input. Click on Save and Clear btn.");
		String abbrev = "testAutoAbrev" + randomCharacter.getRandomAlphaNumericString(3);
		String descr = "testAutoDescr" + randomCharacter.getRandomAlphaString(7);
		clickHiddenPageObject(crossReferenceConfig.addTblIcon(), 0);
		crossReferenceConfig.sendAbbrevInput(abbrev);
		crossReferenceConfig.sendDescInput(descr);
		clickHiddenPageObject(crossReferenceConfig.okBtn(), 0);
		clickHiddenPageObject(crossReferenceConfig.saveAndClearBtn(), 0);
		
		logger.info("*** Step 3 Expected result: Page is reset. A new record is saved in Xref table");
		assertEquals(crossReferenceConfig.abbrevInput().getText(), EMPTY, "        abbrev input is empty.");
		assertEquals(crossReferenceConfig.descrInput().getText(), EMPTY, "        descr input is empty.");

		List<List<String>> xrefListNewInDB = daoManagerXifinRpm.getDataFromXREFByXrefTypId(xRefTypeInfo.get(3),testDb);
		assertTrue(xrefListNewInDB.size() > xrefListOldInDB.size(),"        A new record is saved in Xref table");
		assertTrue(ConvertUtil.convertListToString(listUtil.getListFromListList(xrefListNewInDB, 4)).contains(abbrev.toUpperCase()),"        The new record are corrected abbrev.");
		assertTrue(ConvertUtil.convertListToString(listUtil.getListFromListList(xrefListNewInDB, 2)).contains(descr.toUpperCase()),"        The new record are corrected descr.");
		
		logger.info("*** Step 4 Action: Reload the same Category and Type as step 3. Tab out.");
		assertTrue(isElementPresent(crossReferenceConfig.categoryDropDownBox(), 5),"        Category dropdown box is displayed.");
		assertTrue(isElementPresent(crossReferenceConfig.typeDropDownBox(), 5),"        Type dropdown box is displayed.");
		crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
	    crossReferenceConfig.selectTypeDropdown(xRefTypeInfo.get(3));

		logger.info("*** Step 4 Expected result: Data is reload with new Xref is display in Tbl.");
		crossReferenceConfig.sendAbbrevSearchInput(abbrev);
		assertEquals(abbrev.toUpperCase(), crossReferenceConfig.cellRowInTbl(2,3).getText(), "        Data is reload with new Xref is display in Tbl.");
		
		logger.info("*** Step 5 Action: Click on Run Audit btn.");
		clickHiddenPageObject(crossReferenceConfig.runAuditBtn(), 0);
		String popup = switchToPopupWin();
		
		logger.info("*** Step 5 Expected result: The Audit Detail page is open with corectly data is get from Audit_V table.");
		List<String> audRecIdList = listUtil.getListFromListList(daoManagerXifinRpm.getDataFromXREFByXrefTypId(xRefTypeInfo.get(3),testDb),6);
		List<List<String>> listAuditVInfo= daoManagerXifinRpm.getAuditFromAUDITVByAudRecId(ConvertUtil.convertListToString(audRecIdList),testDb);
		List<String> listActionOnAuditDetail = getValuesInColTable(this,"tbl_auditlogwait",3,listAuditVInfo.size(),listAuditVInfo.size() + 3,crossReferenceConfig.nextPagerIconInAuditDetailTbl());
		listUtil.compareLists(listUtil.getListFromListList(listAuditVInfo,3), listActionOnAuditDetail);
		List<String> listOldValueOnAuditDetail = getValuesInColTable(this,"tbl_auditlogwait",6,listAuditVInfo.size(),listAuditVInfo.size() + 3,crossReferenceConfig.nextPagerIconInAuditDetailTbl());
		listUtil.compareLists(listUtil.getListFromListList(listAuditVInfo,4), listOldValueOnAuditDetail);
		List<String> listNewValueOnAuditDetail = getValuesInColTable(this,"tbl_auditlogwait",7,listAuditVInfo.size(),listAuditVInfo.size() + 3,crossReferenceConfig.nextPagerIconInAuditDetailTbl());
		listUtil.compareLists(listUtil.getListFromListList(listAuditVInfo,5), listNewValueOnAuditDetail);
		daoManagerPlatform.deleteDataFromTableByCondition("XREF", "Abbrev = '"+abbrev+"'", testDb);
		driver.close();
		switchToParentWin(popup);
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Verify Help icon")
	public void testXPR_831() throws Throwable {		
		logger.info("===== Testing - testXPR_831 =====");  	

		randomCharacter = new RandomCharacter();
		crossReferenceConfig = new CrossReferenceConfig(driver);
		
		logger.info("*** Step 1 Expected result: User login successful, the Cross Reference Configuration screen is displayed.");
		verifyCrossReferenceConfigurationPageIsDisplayed();

		logger.info("*** Step 2 Action: Click on Help icon of the page.");
		clickHiddenPageObject(crossReferenceConfig.headerHelpIcon(),0);
		
		logger.info("*** Step 2 Expected result: Help page is displayed.");
		String popup = switchToPopupWin();
		assertTrue(driver.getCurrentUrl().contains("p_cross_reference_configuration_header.htm"),"        Help page is open.");
		driver.close();
		switchToParentWin(popup);
		
		logger.info("*** Step 3 Action: Select Category Id and Type properly, tab out.");
		List<String> categoryInfo = daoManagerXifinRpm.getRandomCategoryFromXrefCat(testDb);
	    ArrayList<String> xRefTypeInfo = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat(categoryInfo.get(1), testDb);
		crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
		crossReferenceConfig.selectTypeDropdown(xRefTypeInfo.get(3));
		Thread.sleep(5000);
		
		while (checkDataLock()) {
			menuNavigation.navigateToCrossReferenceConfigPage();
			String catId = categoryInfo.get(0);
			do {
				categoryInfo = daoManagerXifinRpm.getRandomCategoryFromXrefCat(testDb);
			}while (categoryInfo.get(0).equalsIgnoreCase(catId));
			xRefTypeInfo = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat(categoryInfo.get(1), testDb);
			crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
			crossReferenceConfig.selectTypeDropdown(xRefTypeInfo.get(3));
			Thread.sleep(5000);
			
		}
		logger.info("*** Step 3 Expected result: The Cross Reference Members table is displayed.");
		Thread.sleep(5000);
		assertEquals(crossReferenceConfig.headerCrossReferenceMembersTbl().getText(), CROSS_REFERENCE_MEMBERS,"        The Cross Reference Members table is display with name 'Cross Reference Members'.");

		logger.info("*** Step 4 Action: Click on Help icon at the Cross Reference members section.");
		clickHiddenPageObject(crossReferenceConfig.helpIconOfCrossReferenceMember(), 0);
		
		logger.info("*** Step 4 Expected result: Help page is open.");
		popup = switchToPopupWin();
		assertTrue(driver.getCurrentUrl().contains("p_cross_reference_configuration_load_category_type.htm"), "        The help page is displayed.");
		driver.close();
		switchToParentWin(popup);

		driver.quit();
	}
	
	private boolean checkDataLock () throws Exception {
		String errorLockData = "";
		try {
			errorLockData = crossReferenceConfig.errMessSection().getText();
		} catch (Exception e) {
			logger.error("        Element not found: " + e.getMessage());
		}
		if(errorLockData.contains(DATA_LOCKED)) {
	        return true;
		} else {
			return false;
		}
		
	}

	private void verifyCrossReferenceConfigurationPageIsDisplayed() throws Exception {
		assertEquals(crossReferenceConfig.crossReferenceConfigTitle().getText(), TITLE_TEXT, "        User login successful, Cross Reference Configuration page is displayed.");
	}

		@Test(priority = 1, description = "Verify Creating New Type from Create New Type link ")
	     public void testCreateNewType() throws Throwable  {
			logger.info("===== Testing Create New Type Functionality	 =====");

			crossReferenceConfig = new CrossReferenceConfig(driver);


			logger.info("*** Step 1 Expected Result: User login successfully, Cross Reference Configuration page is displayed.");
			verifyCrossReferenceConfigurationPageIsDisplayed();

			logger.info("*** Step 2 Action:  - Select Category in Xref_cat  and then click Create New Type Link ");
			List<String> categoryInfo = daoManagerXifinRpm.getRandomCategoryFromXrefCat(testDb);
			crossReferenceConfig.selectCategoryDropdown(categoryInfo.get(1));
			select2DropInput().sendKeys(Keys.ESCAPE);
			 crossReferenceConfig.createNewTypeLnk().click();

				logger.info("*** Step 2 Expected Result: Create New Type Pop-up is displayed successfully .");

			    assertEquals(crossReferenceConfig.createNewTypePopUpHeader().getText(),"Create New Type");
     			logger.info("*** Step 3 Action:  Create new  Type  with valid input for Type Abbrev and Type  Description . Click on Save and Clear btn.");
				final String typeAbbrev = "qaAuto123" ;
				final String typeDescr = "qaAutoTestDescr";

				crossReferenceConfig.sendAbbrevTypeInput(typeAbbrev);
				crossReferenceConfig.sendAbbrevDescrInput(typeDescr);
  			    crossReferenceConfig.newTypOkBtn().click();

			logger.info("*** Step 3  Expected Result: Cross Reference Members table is displayed.");

			/**
			 select typedescr  in select type drop down and verify that cross reference member
			 table is displayed
			 **/

			crossReferenceConfig.selectTypeDropdown(typeDescr);
			assertTrue(isElementPresent(crossReferenceConfig.crossReferenceMembersTbl(),5), "        Cross Reference Configuration table is displayed.");


// remove the record from DB table and clear cache
			crossReferenceDao.deleteXrefTypByAbbrev(typeAbbrev);
			 clearDataCache();


		}


}	
