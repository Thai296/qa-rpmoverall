package com.newXp.tests;

import com.mbasys.mars.ejb.entity.dept.Dept;
import com.mbasys.mars.ejb.entity.prc.Prc;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrGrp.PyrGrp;
import com.overall.fileMaintenance.pricing.testCode.TestCode;
import com.overall.menu.MenuNavigation;
import com.overall.search.FeeScheduleSearch;
import com.overall.search.FeeScheduleSearchResults;
import com.overall.search.PayorSearch;
import com.overall.search.PayorSearchResults;
import com.overall.search.ProcCodeSearch;
import com.overall.search.ProcCodeSearchResults;
import com.overall.search.TestCodeSearch;
import com.overall.search.TestCodeSearchResults;
import com.overall.utils.TestCodeUtils;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.ListUtil;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;

import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class SmokeTestCodeTest extends SeleniumBaseTest {

	private TimeStamp timeStamp;
	private TestCode testCode;
	private TestCodeUtils testCodeUtils;
	private RandomCharacter randomCharacter;
	private MenuNavigation navigation;
	private ListUtil listUtil;
	private TestCodeSearch testCodeSearch;
	private TestCodeSearchResults testCodeSearchResults;
	private ProcCodeSearch procCodeSearch;
	private ProcCodeSearchResults procCodeSearchResults;
	private PayorSearch payorSearch;
	private PayorSearchResults payorSearchResults;
	private FeeScheduleSearch feeScheduleSearch;
	private FeeScheduleSearchResults feeScheduleSearchResults;
	private static final String TEST_ABBREV = "testAbbrev";
	private static final String TEST_ID = "testId";
	private String newTestAbbrev;
	private String oldTestAbbrev;
	private static final int THE_FIRST_ROW = 2;
	private Pyr pyr;
	private PyrGrp pyrGrp;

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
	
	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) throws Exception {
		String methodName = result.getMethod().getMethodName();
		logger.info("*** Starting revert data ***");
		switch (methodName) {
		case "verifyUsersAreAbleToUpdateTestCodeID":
			testCode.updateTestWithOldvalue(newTestAbbrev, oldTestAbbrev);
			logger.info("*** Reverting data is successfully ***");
			break;
		case "testXPR_29":
			testCode.deleteNoLoadTestByTestAbbrev(newTestAbbrev);
			logger.info("*** Reverting data is successfully ***");
			break;
		case "testXPR_34":
			testCode.deleteProfileTestByTestAbbrev(newTestAbbrev, pyr, pyrGrp);
			logger.info("*** Reverting data is successfully ***");
			break;
		case "testXPR_224":
		case "testXPR_400":
			testCode.deleteSingleTestByTestAbbrev(newTestAbbrev);
			break;
		default:
			logger.info("*** No need to revert ***");
		}
	}
	
	@Test(priority = 1, description = "Load Test Code - Single Test Code")
	public void testXPR_27() throws Exception {
		logger.info("===== Testing - testXPR_27 =====");  	

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
    	navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that the Test Code page title displays");
		testCode = new TestCode(driver, config, wait);
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'");							

		logger.info("*** Step 2 Action: - Enter non-existing Test ID");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID Input field should show.");
		testCode.checkInputTestCodeIdWithNewTestID(String.valueOf(System.currentTimeMillis()));

		logger.info("*** Step 2 Expected Results: - Verify that Single Test radio button available in Create Option Popup window");
		assertTrue(isElementPresent(testCode.singleTestRadio(), 5), "        Single Test Radio button should be available.");

		logger.info("*** Step 3 Action: - Click on Single Test Radio and Submit");
		assertTrue(isElementPresent(testCode.singleTestRadio(), 5), "        Single Test radio button should show.");
		selectCheckBox(testCode.singleTestRadio());
		assertTrue(isElementPresent(testCode.createOptionOKBtn(), 5), "        Ok button on Create Option should show.");
		testCode.clickCreateOption();

		logger.info("*** Step 3 Expected Results: - Verify that it's on the Single Test Code page");
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single Test page label should show.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Page Label should be Single Test.");
		
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}	
	
	@Test(priority = 1, description = "No Load Test")
	@Parameters({"testMess"})
	public void testXPR_29(String testMess) throws Exception {
		logger.info("===== Testing - testXPR_29 =====");    	

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
    	navigation.navigateToTestCodePage();

		logger.info("***Step 1 Expected Results: - Verify that it's on the Load Test Code page");	
		testCode = new TestCode(driver, config, wait);
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5), "        Single test page label must be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'");							

		logger.info("***Step 2 Action: - Input an non-existing Test ID and Tab out");
		randomCharacter = new RandomCharacter(driver);
		newTestAbbrev = String.valueOf(System.currentTimeMillis());
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeIdWithNewTestID(newTestAbbrev);

		logger.info("***Step 2 Expected Results: - 'Create Option' screen appear");
		assertTrue(isElementPresent(testCode.createScreen(), 5), "        Create Screen should be available.");

		logger.info("***Step 3 Action: - Choose No Load Test option");
		assertTrue(isElementPresent(testCode.noLoadTestRadio(), 5), "        No load test radio must be displayed");
		selectCheckBox(testCode.noLoadTestRadio());
		assertTrue(isElementPresent(testCode.createOptionOKBtn(), 5), "        OK button on Create Option popup must be displayed");
		testCode.clickCreateOption();

		logger.info("***Step 3 Expected Results: - Test Code Information (No Load Test) appear and The Effective Date should match the current date");
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(No Load Test)"), "        Page Label should be Single Test.");
		assertTrue(isElementPresent(testCode.effectiveDatelabel(), 5), "        Effective Date should be available.");		
		
		logger.info("***Step 3 Expected Results: - The Effective Date should match the current date");
		timeStamp = new TimeStamp();
		assertEquals(testCode.effectiveDatelabel().getText(), timeStamp.getCurrentDate());		

		logger.info("***Step 4 Action: - Enter Name and some test note");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be displayed");
		testCode.setTestName(testMess);
		assertTrue(isElementPresent(testCode.testNote(), 5), "        Test note textarea must be displayed");
		testCode.inputTestNote(testMess);
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and clear button must be available");
		testCode.clickSaveAndClearBtn();
		waitUntilElementIsNotVisible(testCode.saveAndClearBtn(), 15);

		logger.info("***Step 4 Expected Results: - The Test Code page should display");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'");							
		
		logger.info("***Step 5 Action: - Enter Test ID");		
		testCode.checkInputTestCodeId(newTestAbbrev);

		logger.info("*** Step 5 Expected Results: - Test Code data save successfully and Test Code Landing page appears");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be displayed");
		assertTrue(testCode.labelTest().getText().trim().contains("(No Load Test)"),"        The correct title page '(No Load Test)' should display");
		assertEquals(testCode.testNameInput().getAttribute("value"), testMess, "        The Test Name " + testMess + " displays");
		assertTrue(testCode.labelTest().getText().trim().contains("(No Load Test)"),"        This should be '(No Load Test)'");
		
		testCode.clickResetBtn();
	}
	
	@Test(priority = 1, description = "Load Test Code - Profile Test Code")
	public void testXPR_30() throws Exception {
		logger.info("===== Testing - testXPR_30 =====");    	

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
    	navigation.navigateToTestCodePage();
		
		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		testCode = new TestCode(driver, config, wait);
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'");							

		logger.info("*** Step 2 Action: - Enter a new Test ID");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID Input field should show.");
		String newTestID = String.valueOf(System.currentTimeMillis());
		testCode.inputTestCodeId(newTestID);

		logger.info("*** Step 2 Expected Results: - Profile Radio button available in Create Options Popup");
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(testCode.profileTestRadio(), 10), "        Profile Radio button should be available.");

		logger.info("*** Step 3 Action: - Click on Profile Test Radio and submit");
		selectCheckBox(testCode.profileTestRadio());
		waitUntilElementPresent(testCode.createOptionOKBtn(), 5);
		assertTrue(isElementPresent(testCode.createOptionOKBtn(), 5), "        Ok button on Create Option popup must be displayed");
		testCode.clickCreateOption();

		logger.info("*** Step 3 Expected Results: - Verify that it's on the Profile Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Profile test page label must be displayed");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Page title should be Profile Test.");
		
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}
	
	@Test(priority = 1, description = "Load Test Code-Create Profile as a component")
	@Parameters({"groupId"})
	public void testXPR_34(String groupId) throws Exception {
		logger.info("===== Testing - testXPR_34 =====");    	

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
    	navigation.navigateToTestCodePage();
    	
		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		testCode = new TestCode(driver, config, wait);
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'");							

		logger.info("*** Step 2 Actions: - Input new TestID to create testID");		
		newTestAbbrev = String.valueOf(System.currentTimeMillis());
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        TestID input must be displayed");
		testCode.checkInputTestCodeIdWithNewTestID(newTestAbbrev);

		logger.info("*** Step 2 Expected Results: - Verify that New Record screen is displayed");
		assertTrue(isElementPresent(testCode.createScreen(), 5), "        Create Screen should be available.");
		assertTrue(isElementPresent(testCode.profileTestRadio(), 10), "        Profile Radio button should show.");

		logger.info("*** Step 3 Actions: - Click on Profile radio button and submit");
		selectCheckBox(testCode.profileTestRadio());
		assertTrue(isElementPresent(testCode.createOptionOKBtn(), 5), "        Ok button on Create Option popup must be displayed");
		testCode.clickCreateOption();

		logger.info("*** Step 3 Expected Result: - Verify that it's display New Profile page");
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test page label must be displayed");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Profile)"), "       Test label should be Profile");

		logger.info("*** Step 4 Actions: - Input Test Name and effective date");
		randomCharacter = new RandomCharacter();
		String testName = "TESTNAME" + randomCharacter.getRandomAlphaString(5);
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be displayed");
		testCode.setTestName(testName);
		assertTrue(isElementPresent(testCode.addEffDate(), 5), "        Effective date input must be displayed");
		timeStamp = new TimeStamp(driver);
		String effDate = timeStamp.getCurrentDate();
		testCode.setEffectiveDate(effDate);

		logger.info("*** Step 4 Expected Result: - Verify that tab list is displayed");
		waitUntilElementPresent(testCode.componentTab(), 15);
		assertTrue(isElementPresent(testCode.componentTab(), 5), "        Tab component should be displayed");

		logger.info("*** Step 5 Actions: Add New Component with Existing valid testId");
		assertTrue(isElementPresent(testCode.addComponent(), 5), "        Add button must be displayed");
		testCode.clickAddComponent();

		logger.info("*** Step 5 Expected Result: Add Record popup should be display");
		assertTrue(isElementPresent(testCode.addRecordComponentsPopup(), 5), "Add Record Popup should be display");

		logger.info("**** Step 6 Action: Add new component with valid testID and Unit");
		List<String> testCodeId = daoManagerXifinRpm.getTestCodeId(testDb);
		assertTrue(isElementPresent(testCode.testID(), 5),"        testId input field should show");
		testCode.setComponentTestId(testCodeId.get(1));

		logger.info("*** Step 6 Expected Result: Test Name is displayed");
		assertTrue(isElementPresent(testCode.testName(), 5), "        Test name input must be displayed");
		assertEquals(testCode.testName().getAttribute("value"), testCodeId.get(2), "        Test name value must equals TestCode ID");

		logger.info("*** Step 7 Action: Click on Ok button");
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button must be available");
		testCode.clickOkCompBtn();

		logger.info("*** Step 7 Expected Result: new TestId will be display in the table");
		assertTrue(isElementPresent(testCode.testIdTable(), 5), "        Test ID Input field must be displayed");
		assertEquals(testCode.testIdTable().getText(), testCodeId.get(1), "        Test ID value must equals test code ID entered");

		logger.info("*** Step 8 Action: Click on Exclusion tab");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab must be displayed");
		testCode.clickExclusionTab();

		logger.info("*** Step 8 Expected Result: System navigate to Exclusion tab");
		assertTrue(isElementPresent(testCode.payorGroupTbl(), 5), "        Exclusion tab should be displayed");

		logger.info("*** Step 9 actions: click add Payor Group into Exclusions tab");
		assertTrue(isElementPresent(testCode.addPayorGroup(), 5), "        Add button in Payor Group Exclusion must be displayed");
		testCode.clickAddPayorGroupBt();

		logger.info("*** Step 9 Expected result: The Add record popup is displayed");
		assertTrue(isElementPresent(testCode.addPayorGroupExclusionDialog(), 5), "        Add Record popup is displayed");

		logger.info("*** Step 10 action: select Group ID in add new record popup");
		pyrGrp = payorDao.getPyrGrpByPyrGrpNm(groupId);
		assertTrue(isElementPresent(testCode.groupIdDropdown(), 5), "        Group ID list box must be displayed");
		testCode.clickGroupIdList();
		assertTrue(isElementPresent(testCode.groupIdSearch(), 5), "       Group ID must be displayed");
		testCode.setGroupId(groupId);
		testCode.clickOkCompBtn();

		logger.info("*** Step 10 expected results: the GroupId is display on GroupID table");
		assertTrue(isElementPresent(testCode.payorGroupExclusionsByRow(THE_FIRST_ROW), 5), "        Payor group exclusion table must be displayed");
		assertEquals(testCode.payorGroupExclusionsByRow(THE_FIRST_ROW).getText(), groupId, "        Group ID must exist in Payor Group Exclusion table");

		logger.info("*** Step 11 Actions: Click on Add Payor ID icon");
		assertTrue(isElementPresent(testCode.addPayorExclusion(), 5), "        Add payor button must be displayed");
		testCode.clickAddPayorExclusionBtn();

		logger.info("*** Step 11 Expected Result: The add new record popup is displayed");
		assertTrue(isElementPresent(testCode.addPayorExclusionDialog(), 5), "        Add New record Payor ID popup is displayed");

		logger.info("*** Step 12 Action: input PayorId");
		pyr = payorDao.getPayorId();
		String payorId = pyr.getPyrAbbrv();
		assertTrue(isElementPresent(testCode.addPayorId(), 5), "        Payor ID input must be displayed");
		testCode.setPayorID(payorId);
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button must be displayed");
		testCode.clickOkCompBtn();

		logger.info("*** Step 12 Expected Results: PayorId is displayed on table");
		assertTrue(isElementPresent(testCode.payorIDOfPayorExclusionsByRow(THE_FIRST_ROW), 5), "       Payor table must be displayed");
		assertEquals(testCodeUtils.getTextValue(testCode.payorIDOfPayorExclusionsByRow(THE_FIRST_ROW), 5), payorId, "        The Payor Id " + payorId + " should display in the table");

		logger.info("*** Step 13 Action: Click on Save and Clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and clear button must be available");
		testCode.clickSaveAndClearBtn();
		waitUntilElementPresent(testCode.testIdInput(), 15);

		logger.info("*** Step 13 Expected Result: New TestId is add into DB");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "       System return the Load Test Code page for input new testId");
		List<com.mbasys.mars.ejb.entity.test.Test> testCodeInfo = testDao.getTestFromTestAndTestTypByAbbrev(newTestAbbrev);
		assertEquals(testCodeInfo.get(0).getTestAbbrev(), newTestAbbrev, "        The Test Code Id " + testCodeInfo.get(0) + " should be correct");
		assertEquals(testCodeInfo.get(0).getName(), testName, "        The Test Name " + testCodeInfo.get(0) + " should be correct");
	}	
	
	@Test(priority = 1, description = "Load Test Code - Cancel create new test code")
	public void testXPR_218() throws Exception {
    	logger.info("===== Testing - testXPR_218 =====");    	

    	logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
    	navigation.navigateToTestCodePage();
    	
		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");	
		testCode = new TestCode(driver, config, wait);
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'");							
			
		logger.info("*** Step 2 Action: - Input a valid Test ID and Tab out");		
		randomCharacter = new RandomCharacter(driver);
		String TestID = String.valueOf(System.currentTimeMillis());
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeIdWithNewTestID(TestID);		
		
		logger.info("*** Step 2 Expected Results: - 'Create Option' screen appear");
		assertTrue(isElementPresent(testCode.createScreen(), 5), "        Create Screen should be available.");
		
		logger.info("*** Step 3 Action: - Click cancel Button");
		assertTrue(isElementPresent(testCode.cancelButton(), 5), "         Cancel button on Create Option popup must be displayed");
		clickHiddenPageObject(testCode.cancelButton(), 0);
		
		logger.info("*** Step 3 Expected Results: - Test ID input should be null");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID Input field should show.");
		assertEquals(testCode.testIdInput().getText(), "","        Test ID Input field should be cleared.");		
	}	

	@Test(priority = 1, description = "Verify saving all the fields")
	@Parameters({"testName"})
	public void testXPR_400(String testName) throws Exception {
		logger.info("===== Testing - testXPR_400 =====");    	

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToTestCodePage();
		
		logger.info("***Step 1 Expected Results: - Verify that it's on the Load Test Code page");	
		testCode = new TestCode(driver, config, wait);
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'");

		logger.info("*** Step 2 Action: - Input a Invalid Test ID and Tab out");
		testCodeUtils = new TestCodeUtils(driver);
		String testCodeId = String.valueOf(System.currentTimeMillis());
		newTestAbbrev = testCodeId;
		assertTrue(isElementPresent(testCode.testId(), 5), "        Test ID Input field should show.");
		testCode.checkInputTestCodeIdWithNewTestID(testCodeId);

		logger.info("*** Step 2 Expected Results: - 'Create Option' screen appear");
		assertTrue(isElementPresent(testCode.createScreen(), 5), "        Create Screen should be available.");

		logger.info("*** Step 3 Action: - Choose Single Test option");
		assertTrue(isElementPresent(testCode.singleTestRadio(), 5), "        Single test radio must be displayed");
		selectCheckBox(testCode.singleTestRadio());
		assertTrue(isElementPresent(testCode.createOptionOKBtn(), 5), "        Ok button on Create Option popup must be displayed");
		testCode.clickCreateOption();

		logger.info("*** Step 3 Expected Results: - Test Code Information (Single Test) appear");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test page label must be displayed");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		assertTrue(isElementPresent(testCode.effectiveDatelabel(), 5), "        Effective Date should be available.");		

		logger.info("*** Step 4 Action: - Enter Name and Effective Date");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be displayed");
		testCode.setTestName(testName);
		assertTrue(isElementPresent(testCode.addEffDate(), 5), "        Effective date input must be displayed");
		timeStamp = new TimeStamp(driver);
		String effDate = timeStamp.getCurrentDate();
		testCode.setEffectiveDate(effDate);

		logger.info("*** Step 4 Expected Result: - Verify that tab list is displayed");
		waitUntilElementPresent(testCode.procedureTab(), 15);
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedures tab should be displayed");

		logger.info("*** Step 5 Action: - Add procedures");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5), "        Add new procedure button must be displayed");
		testCode.clickAddProcedureBtn();
		List<String> procedureCode = daoManagerXifinRpm.getProcedureCodeId(testDb);
		assertTrue(isElementPresent(testCode.procedureTableType(), 5), "        Procedure Table Type must be displayed");
		testCode.selectProcedureTable(procedureCode.get(1));
		assertTrue(isElementPresent(testCode.procedureCodeInput(), 5), "        Procedure Code input must be displayed");
		testCode.inputProcedureCode(procedureCode.get(0));
		assertEquals(testCode.procedureCodeName().getAttribute("value"), procedureCode.get(3), "        Procedure code name must be matching with Procedure code name of procedure code " + procedureCode.get(1));

		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button must be displayed");
		testCode.clickOkProcedureBtn();

		logger.info("*** Step 5 Expected Result: - Verify that procedure code must be added");
		waitUntilElementIsNotVisible(testCode.procedureTable(), 5);
		assertTrue(isElementPresent(testCode.procedureTable(), 5), "        Procedure tab must be displayed");
		assertTrue(getColumnValue(testCode.procedureTable(), procedureCode.get(1)), "        Procedures must be added in table");

		logger.info("*** Step 6 Action: - Add Facilities");
		assertTrue(isElementPresent(testCode.facilityTab(), 5), "        Facility tab must be displayed");
		testCode.clickFacilityTab();
		assertTrue(isElementPresent(testCode.addFacilityBtn(), 5), "        Add new facility button must be displayed");
		testCode.clickAddFacilityBtn();
		String facility = testCode.selectPerformingFacility(2);
		randomCharacter = new RandomCharacter();
		String minPrice = randomCharacter.getNonZeroRandomNumericString(2) + ".00";
		String labCost  = minPrice;
		assertTrue(isElementPresent(testCode.labCostInput(), 5), "        Lab cost input must be displayed");
		testCode.inputLabCost(labCost);
		assertTrue(isElementPresent(testCode.minPriceInput(), 5), "        Min price input must be displayed");
		testCode.inputMinPrice(minPrice);
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button must be displayed");
		testCode.clickOkFacilityBtn();

		logger.info("*** Step 6 Expected Result: - Verify that facility code must be added");
		assertTrue(isElementPresent(testCode.facilityTable(), 5), "        Facility table must be displayed");
		assertTrue(getColumnValue(testCode.facilityTable(), facility), "        Facility must be added in table");
		assertEquals(testCode.recordInFacilitiesTableByRowAndAttribute(2, "PerfFac").getText(), facility, "        Facility must be added in table");
		assertEquals(testCode.recordInFacilitiesTableByRowAndAttribute(2, "LabCost").getText(), labCost, "        Lab cost must be added in table");
		assertEquals(testCode.recordInFacilitiesTableByRowAndAttribute(2, "MinPrice").getText(), minPrice, "        Min price must be added in table");

		logger.info("*** Step 7 Action: - Select Department");
		Dept depInfo = depDao.getDepartment();
		assertTrue(isElementPresent(testCode.selectDeps(), 5), "        Select Department must be displayed");
		selectItem(testCode.selectDeps(), depInfo.getDescr());

		logger.info("*** Step 7 Expected Result : - Save and clear button must be available");
		assertTrue(isElementPresent(testCode.saveAndClearButton(), 5), "       Save and clear button must be available");

		logger.info("*** Step 8 Action: - Save all");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and clear button must be displayed");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 8 Expected Result : - Landing page load test code must be present");
		waitUntilElementIsNotVisible(testCode.saveAndClearBtn(), 15);
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "       Landing page load test code must be present");

		logger.info("*** Step 9 Action: - Load again test code");
		testCode.checkInputTestCodeId(testCodeId);

		logger.info("*** Step 9 Expected Result : - Test Code loaded page must be present and all information are saved");
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 9 Expected Result: Verify all information are saved");
		List<com.mbasys.mars.ejb.entity.test.Test> testCodeInfo = testDao.getTestFromTestAndTestTypByAbbrev(testCodeId);
		assertTrue(isElementPresent(testCode.testCodeIdLableInTestCodePage(), 5));
		assertTrue(testCode.testCodeIdLableInTestCodePage().getText().equalsIgnoreCase(testCodeInfo.get(0).getTestAbbrev()), "        Test ID must match will database");
		assertTrue(isElementPresent(testCode.testNameInput(), 5));
		assertTrue(testCode.testNameInput().getAttribute("value").equalsIgnoreCase(testCodeInfo.get(0).getName()), "        Test Name must match will database");
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date input must be displayed");
		assertEquals(effDate, testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate()), "        Effective Date must match will database");
		assertTrue(isElementPresent(testCode.selectDeps(), 5));
		assertEquals(testCodeUtils.getTextSelectedInDropdown(testCode.selectDeps()), depInfo.getDescr(), "        Department must match will database");

		logger.info("*** Step 10 Action: - Select Procedure Tab");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();

		logger.info("*** Step 10 Expected Result: - Procedure must be saved in database");
		assertTrue(isElementPresent(testCode.procedureTable(), 5), "        Procedure table must be displayed");
		assertTrue(getColumnValue(testCode.procedureTable(), procedureCode.get(1)), "        Procedure must be saved in database");

		logger.info("*** Step 11 Action: - Select Facility tab");
		assertTrue(isElementPresent(testCode.facilityTab(), 5), "        Facility tab must be displayed");
		testCode.clickFacilityTab();

		logger.info("*** Step 11 Expected Result: - Verify that facility code must be saved in database");
		assertTrue(isElementPresent(testCode.facilityTable(), 5), "        Facility table must be displayed");
		assertTrue(getColumnValue(testCode.facilityTable(), facility), "        Facility must be saved in database");
		
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Search Test Code - Search with valid test information")
	public void testXPR_39() throws Exception {
		logger.info("===== Testing - testXPR_39 =====");    	

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToTestCodePage();
		
		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");	
		testCode = new TestCode(driver, config, wait);
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'"); 	

		logger.info("*** Step 2 Action: - Open search test code");
		testCodeSearch = new TestCodeSearch(driver);
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5),"        Search test code button is available");
		testCodeSearch.clickTestCodeSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Search test code popup opened");
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "        Search button should be available.");

		logger.info("*** Step 3 Action: - Search with valid test code information");
		List<String> testCodeInfo = daoManagerXifinRpm.getTestCodeId(testDb);
		String testCodeId = testCodeInfo.get(1); 
		String testCodeName = testCodeInfo.get(2);
		String testCodeType = testCodeInfo.get(3);
		assertTrue(isElementPresent(testCodeSearch.testCodeIdSearchPage(), 5), "        Test Code ID input must be displayed");
		testCodeSearch.setTestCodeIdSearchPage(testCodeId);
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be displayed");
		testCode.setTestName(testCodeName);
		assertTrue(isElementPresent(testCode.selectTestType(), 5), "      Test type must be displayed");
		selectItem(testCode.selectTestType(), testCodeType);
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "      Search button must be displayed");
		testCodeSearch.clickSearchBtnInSearchScreen();

		logger.info("*** Step 3 Expected Results: - Search result should be match with condition");
		testCodeSearchResults = new TestCodeSearchResults(driver);
		assertTrue(isElementPresent(testCodeSearchResults.testSearchTableResult(), 5), "        Search result should be available.");
		assertTrue(getColumnValue(testCodeSearchResults.testSearchTableResult(), testCodeId), "        Search result should be match will search test code id.");
		assertTrue(getColumnValue(testCodeSearchResults.testSearchTableResult(), testCodeName), "        Search result should be match will search test code name.");
		assertTrue(getColumnValue(testCodeSearchResults.testSearchTableResult(), testCodeType), "        Search result should be match will search test code type.");

	}

	@Test(priority=1, description="Search Test Code - Search Test Code with valid data")
	public void testXPR_135() throws Exception{
		logger.info("===== Testing - testXPR_135 =====");    	

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");	
		testCode = new TestCode(driver, config, wait);
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'"); 	

		logger.info("*** Step 2 Actions: - Click Test Code Search Icon Button");
		testCodeSearch = new TestCodeSearch(driver);
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5),"        Search test code button is available");
		testCodeSearch.clickTestCodeSearchIconBtn();
		switchToPopupWin();		

		logger.info("*** Step 2 Expected Results: - Test Code Search Page Window is displayed");
		assertTrue(isElementPresent(testCodeSearch.testCodeIdSearchPage(), 5),"       Test Code Search Page window should be displayed");

		logger.info("*** Step 3 Action: - Input with valid test code information");
		List<String> testCodeSearchInfo = daoManagerXifinRpm.getTestCodeSearchInfo(testDb);
		String testCodeId = testCodeSearchInfo.get(1); 
		String testCodeName = testCodeSearchInfo.get(4);
		String testCodeType = testCodeSearchInfo.get(8);
		assertTrue(isElementPresent(testCodeSearch.testCodeIdSearchPage(), 5), "        Test code id input must be displayed");
		testCodeSearch.setTestCodeIdSearchPage(testCodeId);
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be displayed");
		testCode.setTestName(testCodeName);
		assertTrue(isElementPresent(testCode.selectTestType(), 5), "        Test Type select must be displayed");
		selectItem(testCode.selectTestType(), testCodeType);
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "        Search button must be displayed");
		testCodeSearch.clickSearchBtnInSearchScreen();

		logger.info("*** Step 3 Expected Results: - System display search result that matched with search condition ");
		testCodeSearchResults = new TestCodeSearchResults(driver);
		assertTrue(isElementPresent(testCodeSearchResults.testSearchTableResult(), 5), "        Search result should be available.");
		assertTrue(getColumnValue(testCodeSearchResults.testSearchTableResult(), testCodeId), "        Search result should be match will search test code id.");
		assertTrue(getColumnValue(testCodeSearchResults.testSearchTableResult(), testCodeName), "        Search result should be match will search test code name.");
		assertTrue(getColumnValue(testCodeSearchResults.testSearchTableResult(), testCodeType), "        Search result should be match will search test code type.");

	}

	@Test(priority = 1, description = "Search Test Code - Reset search")
	public void testXPR_137() throws Exception {
		logger.info("===== Testing - testXPR_137 =====");    	

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");	
		testCode = new TestCode(driver, config, wait);
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'"); 	

		logger.info("*** Step 2 Action: - Open search test code");
		testCodeSearch = new TestCodeSearch(driver);
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5),"        Search test code button is available");
		testCodeSearch.clickTestCodeSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Search test code popup opened");
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "        Search button should be available.");
		assertTrue(isElementPresent(testCodeSearch.resetSearchButton(), 5), "        Reset search button should be available.");
		assertTrue(isElementPresent(testCodeSearch.closeSearchButton(), 5), "        Close search should be available.");
		assertTrue(isElementPresent(testCodeSearch.testCodeIdSearchPage(), 5), "        Test code input should be available.");
		assertTrue(isElementPresent(testCodeSearch.testNameInput(), 5), "        Test code name input should be available.");
		assertTrue(isElementPresent(testCodeSearch.labTestCodeIdInputSearchPage(), 5), "        Lab test code id input should be available.");
		assertTrue(isElementPresent(testCodeSearch.procCodeInputSearchPage(), 5), "        Procedure code input should be available.");
		assertTrue(isElementPresent(testCodeSearch.procCodeDescriptionInputSearchPage(), 5), "        Procedure Code Description input should be available.");

		logger.info("*** Step 3 Action: - Input and reset all field");
		randomCharacter = new RandomCharacter(driver);
		testCodeSearch.setTestCodeIdSearchPage(randomCharacter.getRandomAlphaNumericString(2));
		testCodeSearch.setTestName(randomCharacter.getRandomAlphaString(10));
		testCodeSearch.setLabTestCodeId(randomCharacter.getRandomAlphaNumericString(2));
		testCodeSearch.setprocCodeOnSearchPage(randomCharacter.getRandomAlphaNumericString(2));
		testCodeSearch.setProcCodeDescriptionOnSearchPage(randomCharacter.getRandomAlphaString(10));
		testCodeSearch.clickResetSearch();

		logger.info("*** Step 3 Expected Results: - System must reset all the field as blank, and focus highlight on first field");
		assertEquals(testCodeSearch.testCodeIdSearchPage().getText(), "", "        Test code should be reset to empty.");
		assertEquals(testCodeSearch.testNameInput().getText(), "", "        Test code name should be reset to empty.");
		assertEquals(testCodeSearch.labTestCodeIdInputSearchPage().getText(), "", "        Lab test code id should be reset to empty.");
		assertEquals(testCodeSearch.procCodeInputSearchPage().getText(), "", "        Procedure code should be reset to empty.");
		assertEquals(testCodeSearch.procCodeDescriptionInputSearchPage().getText(), "", "        Procedure code description should be reset to empty.");

	}
	
	@Test(priority=1, description="Search Test Code - New Search")
	public void testXPR_141() throws Exception{
		logger.info("===== Testing - testXPR_141 =====");    	

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToTestCodePage();
		
		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");	
		testCode = new TestCode(driver, config, wait);
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'"); 	

		logger.info("*** Step 2 Action: - Open search test code");
		testCodeSearch = new TestCodeSearch(driver);
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5),"        Search test code button is available");
		testCodeSearch.clickTestCodeSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Test Code Search page window is displayed");
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "        Test Code Search page window should be available.");

		logger.info("*** Step 3 Actions: - Input Valid value and Click Search button");

		String testCodeId = daoManagerXifinRpm.getTestCodeSearchInfo(testDb).get(1);
		assertTrue(isElementPresent(testCodeSearch.testCodeIdSearchPage(), 5), "        Test Code ID input must be displayed");
		testCodeSearch.setTestCodeIdSearchPage(testCodeId);
		testCodeSearch.clickSearchBtnInSearchScreen();

		logger.info("*** Step 3 Expected Results: - System will open test search result screen");
		testCodeSearchResults = new TestCodeSearchResults(driver);
		assertTrue(isElementPresent(testCodeSearchResults.testSearchTableResult(), 5),"        Search result screen should be  available.");

		logger.info("*** Step 4 Actions: - Click New Search Button");
		assertTrue(isElementPresent(testCodeSearchResults.newSearchButton(), 5), "        NewSearch Button should be displayed.");
		testCodeSearchResults.clickNewSearch();

		logger.info("*** Step 4 Expected Results: - System will return new search screen");
		assertTrue(isElementPresent(testCodeSearch.testCodeIdSearchPage(), 5), "        System should return new search screen.");

	}

	//Effective Date 
	@Test(priority=1, description="Effective Date - Add new effective date with copy data from another effective date")
	public void testXPR_42() throws Exception{
		logger.info("===== Testing - testXPR_42 =====");          

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		testCode = new TestCode(driver, config, wait);
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'"); 

		logger.info("*** Step 2 Action: - Input available test code");
		String singleTestCodeId = testDao.getSingleTest().getTestAbbrev();
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeId);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Test Code page label must be displayed");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Get Data from Procedures Table and Click on Pencil Icon");
		assertTrue(isElementPresent(testCode.procedureTable(), 5), "        Procedure table must be displayed");
		List<String> listProcBefore =  testCodeUtils.getAllColumnValue(testCode.procedureTable());

		assertTrue(isElementPresent(testCode.facilityTab(), 5), "       Facility tab must be displayed");
		testCode.clickFacilityTab();
		assertTrue(isElementPresent(testCode.facilityTable(), 5), "        Facility table must be displayed");
		List<String> listFacBefore =  testCodeUtils.getAllColumnValue(testCode.facilityTable());

		logger.info("*** Step 3 Action: - Click on Pencil Icon");
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date input must be displayed");
		String effDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Effective date icon must be displayed");
		testCode.clickCreateEffDateIcon();

		logger.info("*** Step 3 Expected Results: - Effective date field Will be displayed");
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"          Effective date field should be displayed");

		logger.info("*** Step 4 Action: - Input valid Effective Date");
		String newEffDate = testCodeUtils.getNextYear(effDate);
		testCode.setEffectiveDate(newEffDate);

		logger.info("*** Step 4 Expected Results: - Effective Data pop up is displayed");
		assertTrue(isElementPresent(testCode.copyEffDt(), 5),"        Effective Data pop up should be displayed");
		assertTrue(isElementPresent(testCode.okCpyEffDate(), 5),"        Effective Data pop up should be displayed");

		logger.info("*** Step 5 Action: - Select valid Effective Date on Copy effective date popup and Get data after populating in all field of new effective date");
		String copyEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.copyEffDt());
		selectItem(testCode.copyEffDt(), copyEffDate);
		testCode.clickokCpyEffDate();

		logger.info("*** Step 5 Actions: - Get data after populating in all field of new effective date ");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();
		List<String> listProcAfter =  testCodeUtils.getAllColumnValue(testCode.procedureTable());
		assertTrue(isElementPresent(testCode.facilityTab(), 5), "        Facility table must be displayed");
		testCode.clickFacilityTab();
		List<String> listFacAfter =  testCodeUtils.getAllColumnValue(testCode.facilityTable());

		logger.info("*** Step 5 Expected Results: - The data of selected effective data is populated in all field of new effective date ");
		listUtil = new ListUtil();
		assertTrue(listUtil.compareLists(listProcBefore, listProcAfter),"        The data of selected effective data should be populated in all field of new effective date") ;
		assertTrue(listUtil.compareLists(listFacBefore, listFacAfter),"        The data of selected effective data should be populated in all field of new effective date") ;
		
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Effective Date - Create new effective date without data")
	public void testXPR_46() throws Exception{
		logger.info("===== Testing - testXPR_46 =====");          

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		testCode = new TestCode(driver, config, wait);
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'"); 

		logger.info("*** Step 2 Action: - Input available test code");
		String singleTestCodeId = testDao.getSingleTest().getTestAbbrev();
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeId);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test code page label must be displayed");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click on Pencil Icon");
		assertTrue(isElementPresent(testCode.procedureTable(), 5), "        Procedure table must be displayed");
		List<String> listProcBefore =  testCodeUtils.getAllColumnValue(testCode.procedureTable());
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date select must be displayed");
		String effDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Effective Date icon must be displayed");
		testCode.clickCreateEffDateIcon();

		logger.info("*** Step 3 Expected Results: - Effective date field Will be displayed");
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"          Effective date field should be displayed");

		logger.info("*** Step 4 Action: - Input valid Effective Date");
		String newEffDate = testCodeUtils.getNextYear(effDate);
		testCode.setEffectiveDate(newEffDate);

		logger.info("*** Step 4 Expected Results: - Effective Data pop up is displayed");
		assertTrue(isElementPresent(testCode.copyEffDt(), 5),"        Effective Data pop up should be displayed");
		assertTrue(isElementPresent(testCode.cancelEffCopyDialog(), 5),"        Effective Data pop up should be displayed");

		logger.info("*** Step 5 Action: - Select valid Effective Date and click cancel button on Copy effective date pop up");
		String copyEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.copyEffDt());
		selectItem(testCode.copyEffDt(), copyEffDate);
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 5 Expected Results: - New effective date is created and component table is empty(no data is added) ");
		listUtil = new ListUtil();
		List<String> listProcAfter =  testCodeUtils.getAllColumnValue(testCode.procedureTable());
		assertEquals(getTableTotalRowSize(testCode.procedureTable()), 1,"         New effective date should be created and component table is empty(no data is added)");
		assertFalse(listUtil.compareLists(listProcBefore, listProcAfter),"        New effective date should be created and component table is empty(no data is added)") ;
		
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Effective Date - Update tab list for effective date with valid data")
	public void testXPR_224() throws Exception{
		logger.info("===== Testing - testXPR_224 =====");          

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		testCode = new TestCode(driver, config, wait);
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'"); 

		logger.info("*** Step 2 Action: - Input available test code");
		String singleTestCodeId = String.valueOf(System.currentTimeMillis());
		newTestAbbrev = singleTestCodeId;
		testCode.createSingleTest(singleTestCodeId, testDb, false, false, this);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test code page label must be displayed");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click on Pencil Icon");
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date must be displayed");
		String effDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Effective date icon must be displayed");
		testCode.clickCreateEffDateIcon();

		logger.info("*** Step 3 Expected Results: - Effective date field Will be displayed");
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"          Effective date field should be displayed");

		logger.info("*** Step 4 Action: - Input valid Effective Date");
		String newEffDate = testCodeUtils.getNextDay(effDate);
		testCode.setEffectiveDate(newEffDate);

		logger.info("*** Step 4 Expected Results: - Effective Data pop up is displayed");
		assertTrue(isElementPresent(testCode.copyEffDt(), 5),"        Effective Data pop up should be displayed");
		assertTrue(isElementPresent(testCode.cancelEffCopyDialog(), 5),"        Effective Data pop up should be displayed");

		logger.info("*** Step 5 Action: - Select valid Effective Date, click cancel button on Copy effective date popup and Add procedures");
		String copyEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.copyEffDt());
		selectItem(testCode.copyEffDt(), copyEffDate);
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 6 Action: - Add procedures");
		List<String> proCodeInfo = testCode.addProcedure(this, testCode, "", testDb);

		logger.info("*** Step 6 Expected Result: - Verify that procedure code must be added");
		assertTrue(isElementPresent(testCode.procedureTable(), 5), "       Procedure table must be displayed");
		assertTrue(getColumnValue(testCode.procedureTable(), proCodeInfo.get(0)), "        Procedures must be added in table");
		assertTrue(getColumnValue(testCode.procedureTable(), proCodeInfo.get(1)), "        Procedures must be added in table");
		assertTrue(getColumnValue(testCode.procedureTable(), proCodeInfo.get(2)), "        Procedures must be added in table");

		logger.info("*** Step 7 Action: - Add Facilities");
		String facility =  testCode.addNewFacility(testCode.facilityTable(), this);

		logger.info("*** Step 7 Expected Result: - Verify that Facility must be added");
		assertTrue(isElementPresent(testCode.facilityTable(), 5), "        facility table must be displayed");
		assertTrue(getColumnValue(testCode.facilityTable(), facility), "        Facility must be added in table");

		logger.info("*** Step 8 Action: - Select Department");
		Dept depInfo = depDao.getDepartment();
		assertTrue(isElementPresent(testCode.selectDeps(), 5));
		selectItem(testCode.selectDeps(), depInfo.getDescr());

		logger.info("*** Step 8 Expected Result: - Verify that Department must be added");
		assertEquals(testCodeUtils.getTextSelectedInDropdown(testCode.selectDeps()), depInfo.getDescr(), "        Department must be added");

		logger.info("*** Step 9 Action: - Select row to update facility ");
		selectColumnValue(testCode.facilityTable(), facility);

		logger.info("*** Step 9 Expected Result: - Edit Record pop up is displayed ");
		assertEquals(testCode.editfacilityDialog().getText(), "Edit Record","        Edit Record pop up should be display");

		logger.info("*** Step 10 Action: - Update  Facility");
		assertTrue(isElementPresent(testCode.colFacilityInfo1(), 5), "        Facility info1 input must be displayed");
		testCode.colFacilityInfo1().clear();
		testCode.setColFacilityInfo1("Editfacility");
		testCode.clickOkFacilityBtn();

		logger.info("*** Step 10 Action: Click on Save and Clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and clear button must be displayed");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 10 Expected Result: New and update data are save for selected effective date ");
		waitUntilElementIsNotVisible(testCode.saveAndClearBtn(), 15);
		testCode.checkInputTestCodeId(singleTestCodeId);
		waitUntilElementPresent(testCode.procedureTab(),15);
		assertEquals(testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate()), newEffDate, "        The new Effective date should be updated");
		
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.procedureTable(), 5), "        Procedure table must be displayed");
		assertTrue(getColumnValue(testCode.procedureTable(), proCodeInfo.get(1)), "        Procedures must be save in DB");

		assertTrue(isElementPresent(testCode.facilityTab(), 5), "        Facility tab must be displayed");
		testCode.clickFacilityTab();
		assertTrue(isElementPresent(testCode.facilityTable(), 5), "        Facility table must be displayed");
		assertTrue(getColumnValue(testCode.facilityTable(), "Editfacility"), "        Facility must be edit in DB");
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "ProFile Code Detail - Eff date - Create new eff date with valid eff date format")
	public void testXPR_225() throws Exception {
		logger.info("===== Testing - testXPR_225=====");    	

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'");							

		logger.info("*** Step 2 Action: - Input an valid Test ID (Profile) and Tab out");
		String testID = String.valueOf(System.currentTimeMillis());
		testCode.createProfileTest(testID, testDb, this);

		logger.info("*** Step 2 Expected Results: - Profile test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Profile)"),"        Test Label should be Profile.");

		logger.info("*** Step 3 Action: - Input valid effective date");
		String EffDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		String nextEffDate = testCodeUtils.getNextDay(EffDate);
		clickHiddenPageObject(testCode.createEffDateIcon(), 0);
		testCode.setEffectiveDate(nextEffDate);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");

		logger.info("*** Step 4 Action: - Cancel copy Effective Date");
		assertTrue(isElementPresent(testCode.cancelEffCopyDialog(), 5), "        Cancel Effective Copy Dialog must be displayed");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Expected Results: - Copy effective date dialog closed and all fields reset to empty");
		assertTrue(isElementHidden(testCode.effCopyDialog(), 5), "        Copy effective date dialog closed");
		assertTrue(isElementPresent(testCode.expDateInput(), 5), "        Expiration date input must be displayed");
		assertEquals(testCode.expDateInput().getText(), "", "        Exp date must be reset");

		logger.info("*** Step 5 Action: - Add new component with exp date is older eff date of current profile ID");
		testCode.clickComponentTab();
		testCode.clickAddComponent();		
		assertTrue(isElementPresent(testCode.testID(), 5),"        testId input field should show");
		com.mbasys.mars.ejb.entity.test.Test singleTest = testDao.getSingleTestFromTestByEffDt(nextEffDate);
		String singleTestID = singleTest.getTestAbbrev();
		testCode.setComponentTestId(singleTestID);		
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button must be displayed");
		testCode.clickOkCompBtn();		

		logger.info("*** Step 5 Action: - Click Save and Clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();
		waitUntilElementIsNotVisible(testCode.saveAndClearBtn(), 15);
		assertTrue(isElementPresent(testCode.testId(), 10), "        Test ID Input field should show.");
		testCode.checkInputTestCodeId(testID);

		logger.info("*** Step 5 Expected Results: - New component should be added ");
		assertTrue(isElementPresent(testCode.componentTable(), 10), "        Components tab should show.");
		testCode.clickComponentTab();		
		assertTrue(getColumnValue(testCode.componentTable(), singleTestID), "        Test ID: " + singleTestID + " should show in Profile Components table.");
		
		testCode.clickResetBtn();
	}

	//Search Test Code Function
	@Test(priority = 1, description = "Search Test Code - Search valid test ID")
	public void testXPR_36() throws Exception {
		logger.info("===== Testing - testXPR_36 =====");          

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		testCode = new TestCode(driver, config, wait);
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'");								

		logger.info("*** Step 2 Actions: - Click Test Code Search Icon Button");
		testCodeSearch = new TestCodeSearch(driver);
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5), "      Test code search button must be displayed");
		testCodeSearch.clickTestCodeSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Test Code Search Page Window is displayed");
		assertTrue(isElementPresent(testCodeSearch.testCodeIdSearchPage(), 5),"       Test Code Search Page window should be displayed");
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "       Search button must be displayed");

		logger.info("*** Step 3 Actions: - Search with valid Test Code ID");
		com.mbasys.mars.ejb.entity.test.Test testCodeInfo = testDao.getSingleTest();
		testCodeSearch.setTestCodeIdSearchPage(testCodeInfo.getTestAbbrev());
		testCodeSearch.clickSearchBtnInSearchScreen();

		logger.info("*** Step 3 Expected Results - Test Id is match with Search condition will be shown");
		testCodeSearchResults = new TestCodeSearchResults(driver);
		assertTrue(isElementPresent(testCodeSearchResults.testSearchTableResult(), 5), "        Test search table result must be displayed");
		assertTrue(getColumnValue(testCodeSearchResults.testSearchTableResult(), testCodeInfo.getTestAbbrev()), "      Test code id must be displayed in test search table");

	}
	
	@Test(priority=1, description="Single Test Detail - Procedures - Verify that the Proc Code name is populated")
	public void testXPR_51() throws Exception{
		logger.info("===== Testing - testXPR_51 =====");          

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'"); 

		logger.info("*** Step 2 Action: - Input available test code");
		String singleTestCodeId = testDao.getSingleTest().getTestAbbrev();
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeId);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test page label must be displayed");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click Add procedures");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5), "        Add procedure button must be displayed");
		testCodeUtils.scrollIntoView(testCode.addProcedureBtn());
		testCode.clickAddProcedureBtn();

		logger.info("*** Step 3 Expected Results: - The Add new record pop up will be displayed");
		assertTrue(isElementPresent(testCode.addRecordProcedurePopUp(),5),"        The Add new record pop up should be displayed");

		logger.info("*** Step 4 Action: - Click Search Procedure Icon button");
		String curProcTable = testCode.procedureTableInProcedureTable().getText();
		String procTable = testCode.getNewProcTableInDb(this, curProcTable, testDb);
		testCode.selectProcedureTable(procTable);
		assertTrue(isElementPresent(testCode.searchProcedureIcon(), 5), "        Procedure Search icon must be displayed");
		testCode.clickSearchProcedureIcon();
		String parentWindow = switchToPopupWin();

		logger.info("*** Step 4 Expected Results: - System display all pro code that match with search condition");
		procCodeSearch = new ProcCodeSearch(driver);
		assertTrue(isElementPresent(procCodeSearch.searchProcedureIdInput(), 5), "        Search button for procedure ID must be displayed");
		procCodeSearch.searchProcedureCode("*");

		logger.info("*** Step 5 Action: - Click on any procedure code link");
		procCodeSearchResults = new ProcCodeSearchResults(driver);
		assertTrue(isElementPresent(procCodeSearchResults.procedureIdLinkTestCodeSearchResults(2, 2), 5));
		String procedureCodeId = testCodeUtils.getTextValue(procCodeSearchResults.procedureIdLinkTestCodeSearchResults(2, 2), 5);
		assertTrue(isElementPresent(procCodeSearchResults.procedureNameLinkTestCodeSearchResults(2, 4), 5));
		String procedureCodeName = testCodeUtils.getTextValue(procCodeSearchResults.procedureNameLinkTestCodeSearchResults(2, 4), 5);
		procCodeSearchResults.clickOnSearchResultsProcedureIdLink(2,2);
		switchToParentWin(parentWindow);

		logger.info("*** Step 5 Expected Results: - Search Window is close and proc code is populated on add record window");
		assertTrue(isElementPresent(testCode.procedureCodeInput(), 5), "        Procedure Code input must be displayed");
		assertEquals(testCodeUtils.getAttributeValue(testCode.procedureCodeInput(),5), procedureCodeId,"        The Proc Code is populated");
		assertTrue(isElementPresent(testCode.procedureCodeName(), 5), "        Procedure code name input must be displayed");
		assertEquals(testCode.procedureCodeName().getAttribute("disabled"),"true","          Name field is read only field ");
		assertTrue(testCodeUtils.getAttributeValue(testCode.procedureCodeName(),5).contains(procedureCodeName),"        The procedure Code Name is populated");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);

	}

	@Test(priority = 1, description = "Search Test Code - Verify that testID is populate correctly")
	public void testXPR_37() throws Exception {
		logger.info("===== Testing - testXPR_37 =====");          

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		testCode = new TestCode(driver, config, wait);
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'");								

		logger.info("*** Step 2 Actions: - Click Test Code Search Icon Button");
		testCodeSearch = new TestCodeSearch(driver);
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5), "        Test code search button must be displayed");
		testCodeSearch.clickTestCodeSearchIconBtn();
		String parentWindow = driver.getWindowHandle();
		switchToPopupWin();		

		logger.info("*** Step 2 Expected Results: - Test Code Search Page Window is displayed");
		assertTrue(isElementPresent(testCodeSearch.testCodeIdSearchPage(), 5),"       Test Code Search Page window should be displayed");
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "      Search button in search screen must be displayed");

		logger.info("*** Step 3 Actions: - Search with valid Test Code ID");
		com.mbasys.mars.ejb.entity.test.Test testCodeInfo = testDao.getSingleTest();
		testCodeSearch.setTestCodeIdSearchPage(testCodeInfo.getTestAbbrev());
		testCodeSearch.clickSearchBtnInSearchScreen();

		logger.info("*** Step 3 Expected Results: - Test Code Search Results is displayed");
		testCodeSearchResults = new TestCodeSearchResults(driver);
		assertTrue(isElementPresent(testCodeSearchResults.testCodeIdLinkTestCodeSearchResults(2,2), 5),"       Test Code Search Results should be displayed"); 
		String testCodeId = testCodeSearchResults.testCodeIdLinkTestCodeSearchResults(2,2).getText();

		logger.info("*** Step 4 Actions: - Click the Test Code link in the first column of Test Code search results ");
		testCodeSearchResults.clickOnSearchResultsTestCodeIdLink(2, 2);
		switchToWin(parentWindow);
		switchToDefaultWinFromFrame();

		logger.info("*** Step 4 Expected Results - Test Id is match with Search condition will be shown");
		assertTrue(isElementPresent(testCode.verifyTestIDinTestCodepage(), 5),"        System should navigate to TestCode detail page");
		assertEquals(testCode.verifyTestIDinTestCodepage().getText(), testCodeId,"        System should populate Test Id on Test Id field");

		testCode.clickResetBtn();
	}	
	
	@Test(priority = 1, description = "Single Test Detail - Modifier - Search Payor - Search PayorID with valid full conditions")
	public void testXPR_108() throws Exception {
		logger.info("===== Testing - testXPR_108 =====");

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'");							

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		com.mbasys.mars.ejb.entity.test.Test singleTest = testDao.getSingleTest();
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest.getTestAbbrev());

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click Modifier Tab. Click Add button. Click search payor ID.");
		testCode.clickModifierTab();
		testCodeUtils.scrollIntoView(testCode.addPayorSpecBtn());
		testCode.clickAddPayorSpecBtn();
		testCode.clickSearchPayorIDBtn();
		String winHandler = switchToPopupWin();

		logger.info("*** Step 3 Expected Results: - Add New Record Popup must be displayed. New popup window must be displayed");
		payorSearch = new PayorSearch(driver, config);
		assertTrue(isElementPresent(payorSearch.payorIDTextInput(), 5), "        New popup window must be displayed");
		assertTrue(isElementPresent(payorSearch.searchBtn(), 5), "        Submit button must be displayed");
		String getAttribute = payorSearch.searchBtn().getAttribute("data-user-action");
		assertTrue(getAttribute.contains("search/payorsearch.html"), "        Payor Search URL should contains 'search/payorsearch.html'.");

		logger.info("*** Step 4 Action: - Enter all valid data to payor search popup. Click Search button");
		List<String> input = payorSearch.enterPayorInfoToPayorSearchPopupWindow(testDb);
		payorSearch.clickSearchBtn();

		logger.info("*** Step 4 Expected Results: - The PayorID matched input is displayed");
		payorSearchResults = new PayorSearchResults(driver, wait);
		assertTrue(isElementPresent(payorSearchResults.recordsCount(), 10), "        Records count view in Search Results should show.");
		assertTrue(isElementPresent(payorSearchResults.payorSearchResultTable(), 5), "        Payor Search result must be displayed");
		assertTrue(getTableTotalRowSize(payorSearchResults.payorSearchResultTable()) >= 2, "        Result of this search must be displayed");
		assertTrue(getColumnValue(payorSearchResults.payorSearchResultTable(),input.get(1)), "        Payor ID must be displayed");

		//Switch back to the main window
		clickHiddenPageObject(payorSearchResults.closeBtn(), 0);
		switchToWin(winHandler);

		//Click the Cancel Button in the Popup window
		clickHiddenPageObject(testCode.cancelBtn(), 0);
		//Click the Reset Button in the Test Code screen
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description ="Single Test Detail - Fee Schedule - Search Fee Sched with valid input")
	public void testXPR_113() throws Exception {
		logger.info("===== Testing - testXPR_113 =====");
		
		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page is displayed with correct page title");
		testCode = new TestCode(driver, config, wait);
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'");							

		logger.info("*** Step 2 Actions: - Enter a valid Single Test ID and tab out");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        TestId input field should show");
		com.mbasys.mars.ejb.entity.test.Test testCodeInfo = testDao.getSingleTest();
		testCode.checkInputTestCodeId(testCodeInfo.getTestAbbrev());

		logger.info("*** Step 2 Expected Results: - System display test code with information");
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 3 Actions: - Click on Add button");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5),"        Tab Fee Schedule is shown");
		clickHiddenPageObject(testCode.feeScheduleTab(), 0);
		
		assertTrue(isElementPresent(testCode.addNewFSBtn(), 5),"        Add button is shown");
		clickHiddenPageObject(testCode.addNewFSBtn(), 0);
		
		logger.info("*** Step 3 Expected Results: - Add Record popup is displayed");
		assertTrue(isElementPresent(testCode.editPopupFeeSchedule(), 5),"       Add Record popup is displayed");
		
		logger.info("*** Step 4 Actions: Click on Fee Sched ID Search button");
		feeScheduleSearch = new FeeScheduleSearch(driver);
		feeScheduleSearchResults = new FeeScheduleSearchResults(driver);
		assertTrue(isElementPresent(feeScheduleSearch.searchFSIcon(), 5),"        Fee Sched ID Search button is shown");
		clickHiddenPageObject(feeScheduleSearch.searchFSIcon(), 0);
		
		logger.info("*** Step 4 Expected Results: - Search Fee Schedule page is displayed");
		String parent = switchToPopupWin();
		
		logger.info("*** Step 5 Actions: Enter a valid Fee Schedule ID and Type is Fee Schedule and Retail, Test Based, Client Based are Both and click on Search button");
		Prc list = prcDao.getFeeScheduleId();
		String validFeeID = list.getPrcAbbrev();
		assertTrue(isElementPresent(feeScheduleSearch.fsIDInput(), 5),"        Fee Sched ID input is shown");
		feeScheduleSearch.inputFSID(validFeeID);
		
		assertTrue(isElementPresent(feeScheduleSearch.typeRadio(1), 5),"        Type radio is shown");
		clickHiddenPageObject(feeScheduleSearch.typeRadio(1), 0);
		assertTrue(isElementPresent(feeScheduleSearch.retailRadio(3), 5),"        Retail radio is shown");
		clickHiddenPageObject(feeScheduleSearch.retailRadio(3), 0);
		assertTrue(isElementPresent(feeScheduleSearch.testBaseRadio(3), 5),"        Test Based radio is shown");
		clickHiddenPageObject(feeScheduleSearch.testBaseRadio(3), 0);
		assertTrue(isElementPresent(feeScheduleSearch.clnBaseRadio(3), 5),"        Retail radio is shown");
		clickHiddenPageObject(feeScheduleSearch.clnBaseRadio(3), 0);
		
		assertTrue(isElementPresent(feeScheduleSearch.searchBtn(), 5),"        Fee Sched ID Search button is shown");
		clickHiddenPageObject(feeScheduleSearch.searchBtn(), 0);		
			
		logger.info("*** Step 5 Expected Results: - Search result list is matched with search condition");
		assertTrue(isElementPresent(feeScheduleSearchResults.feeScheduleTblFSCell(2, 2), 5),"       Fee Schedule ID column is shown");
		assertEquals(feeScheduleSearchResults.feeScheduleTblFSCell(2, 2).getText(),validFeeID ,"        Search result list is matched with search condition");
		String feeID = feeScheduleSearchResults.feeScheduleTblFSID(2, 2).getText();
		
		logger.info("*** Step 6 Actions: Select Fee Schedule ID");
		assertTrue(isElementPresent(feeScheduleSearchResults.feeScheduleTblFSID(2, 2), 5),"       Fee Schedule ID column is shown");
		clickHiddenPageObject(feeScheduleSearchResults.feeScheduleTblFSID(2, 2), 0);		
		
		logger.info("*** Step 6 Expected Results: - System will display selected item on New Record popup");
		switchToParentWin(parent);
		assertTrue(isElementPresent(testCode.feeSchIdInput(), 5),"       Fee Schedule input is shown");
		assertEquals(testCode.feeSchIdInput().getAttribute("value"), feeID,"        System will display selected item on New Record popup");
				
		assertTrue(isElementPresent(testCode.cancelProcedureDialog(), 5),"        Cancel button is shown");
		clickHiddenPageObject(testCode.cancelProcedureDialog(), 0);
		assertTrue(isElementPresent(testCode.resetBtn(), 5),"        Reset button is shown");
		clickHiddenPageObject(testCode.resetBtn(), 0);
		closeAlertAndGetItsText(true);

	}
	
	@Test(priority = 1, description = "Verify that there is a new button to changing Test Code ID")
	public void verifyThereIsANewButtonToChangingTestCodeID() throws Exception {
		logger.info("*** Implementation test case [verifyThereIsANewButtonToChangingTestCodeID] ***");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		randomCharacter = new RandomCharacter();
		List<String> testCodeInfoList = daoManagerXifinRpm.getRandomTestCodeId(testDb);
		Map<String, String> testCodeInfoMap = testCodeUtils.getMapByGivenList(testCodeInfoList);
		String testAbbrevIncludedSpecialCharacter = randomCharacter.getRandomAlphaNumericString(17) + "\"'\"";
		
		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
    	navigation.navigateToTestCodePage();
		
		logger.info("*** Step 1 Expected Result: Verify that the Load Test Code page is displayed");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 15),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'");							
		
		logger.info("*** Step 2 Actions: Input a valid Test Code ID");
		enterValues(testCode.testId(), testCodeInfoMap.get(TEST_ABBREV));
		
		logger.info("*** Step 2 Expected Result: Verify the Test Code page is displayed correctly with information of Test Code ID");
		assertTrue(isElementPresent(testCode.saveAndClearButton(), 15), "        Waiting for the Test Code page is displayed");
		List<String> testCodeInfoActual = testCode.getTestCodeInfoOnHeaderOfTestCode();
		assertEquals(testCodeInfoActual, testCodeInfoList.subList(1, 5), "        Information of Test Code should be displayed correctly");
		
		logger.info("*** Step 2 Expected Result: 'Change Test Code ID' button is displayed");
		assertTrue(isElementPresent(testCode.changeTestCodeIdButton(), 15), "        Change Test Code ID button should be displayed");
		
		logger.info("*** Step 3 Actions: Click on 'Change Test Code ID' button");
		clickHiddenPageObject(testCode.changeTestCodeIdButton(), 0);
		
		logger.info("*** Step 3 Expected Result: The 'Change Test Code ID' pop up is displayed ");
		assertEquals(testCodeUtils.getTextValue(testCode.changeTestCodeIdPopupTitle(), 15), "Change Test Code ID", "        Change Test Code ID popup should be displayed");
		
		logger.info("*** Step 3 Expected Result: Current Test Code ID field is read only");
		assertTrue(isElementEnabled(testCode.currentTestCodeIdField(), 15, false), "        Current Test Code ID field should be disabled");
		assertEquals(testCodeUtils.getAttributeValue(testCode.currentTestCodeIdField(), 15), testCodeInfoMap.get(TEST_ABBREV), "        The Test Code Id should be displayed correctly");
		
		logger.info("*** Step 3 Expected Result: New Test Code ID field is enabled");
		assertTrue(isElementEnabled(testCode.newTestCodeIdField(), 15, true), "        New Test Code ID field should be enabled");
		
		logger.info("*** Step 4 Actions: Leave New Test Code ID blank" + " | Click on 'OK' button");
		enterValues(testCode.newTestCodeIdField(), "");
		clickHiddenPageObject(testCode.okChangeTestCodeIdPopupButton(), 0);
		
		logger.info("*** Step 4 Expected Result: Field Validation popup is displayed");
		assertEquals(testCodeUtils.getTextValue(testCode.validationMessageOfChangeTestCodeIdPopup(), 15).trim(), "New Test Code ID is required.", "        Validation message should be displayed");
		
		logger.info("*** Step 5 Actions: Close Field Validation popup" + " | Input new Test Code ID includes special characters" + " | Click on 'OK' button");
		clickHiddenPageObject(testCode.closeValidationMessageOfChangeTestCodeIdPopup(), 0);
		enterValues(testCode.newTestCodeIdField(), testAbbrevIncludedSpecialCharacter);
		clickHiddenPageObject(testCode.okChangeTestCodeIdPopupButton(), 0);
		
		logger.info("*** Step 5 Expected Result: Field Validation popup is displayed");
		assertEquals(testCodeUtils.getTextValue(testCode.validationMessageOfChangeTestCodeIdPopup(), 15), testAbbrevIncludedSpecialCharacter + " is not a valid test ID. Valid IDs are no longer than 20 characters and contain only letters, numbers, hyphens and underscores.", "        Validation message should be displayed");
	}
	
	@Test(priority = 1, description = "Verify that users are able to update Test Code ID")
	public void verifyUsersAreAbleToUpdateTestCodeID() throws Exception {
		logger.info("*** Implementation test case [verifyUsersAreAbleToUpdateTestCodeID] ***");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		randomCharacter = new RandomCharacter();
		List<String> testCodeInfoList = daoManagerXifinRpm.getRandomTestCodeId(testDb);
		Map<String, String> testCodeInfoMap = testCodeUtils.getMapByGivenList(testCodeInfoList);
		oldTestAbbrev = testCodeInfoMap.get(TEST_ABBREV);
		newTestAbbrev = testCode.getNewTestCodeIdNotExistedInSystem(oldTestAbbrev);
		
		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
    	navigation.navigateToTestCodePage();
		
		logger.info("*** Step 1 Expected Result: Verify that the Load Test Code page is displayed");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 15),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'");							
		
		logger.info("*** Step 2 Actions: Input a valid Test Code ID");
		enterValues(testCode.testId(), testCodeInfoMap.get(TEST_ABBREV));
		
		logger.info("*** Step 2 Expected Result: Verify the Test Code page is displayed correctly with information of Test Code ID");
		assertTrue(isElementPresent(testCode.saveAndClearButton(), 15), "        Waiting for the Test Code page is displayed");
		List<String> testCodeInfoActual = testCode.getTestCodeInfoOnHeaderOfTestCode();
		assertEquals(testCodeInfoActual, testCodeInfoList.subList(1, 5), "        Information of Test Code should be displayed correctly");
		
		logger.info("*** Step 3 Actions: Click on 'Change Test Code ID' button");
		clickHiddenPageObject(testCode.changeTestCodeIdButton(), 0);
		
		logger.info("*** Step 3 Expected Result: The 'Change Test Code ID' pop up is displayed ");
		assertEquals(testCodeUtils.getTextValue(testCode.changeTestCodeIdPopupTitle(), 15), "Change Test Code ID", "        Change Test Code ID popup should be displayed");
		
		logger.info("*** Step 3 Expected Result: Current Test Code ID field is read only");
		assertTrue(isElementEnabled(testCode.currentTestCodeIdField(), 15, false), "        Current Test Code ID field should be disabled");
		assertEquals(testCodeUtils.getAttributeValue(testCode.currentTestCodeIdField(), 15), testCodeInfoMap.get(TEST_ABBREV), "        The Test Code Id should be displayed correctly");
		
		logger.info("*** Step 3 Expected Result: New Test Code ID field is enabled");
		assertTrue(isElementEnabled(testCode.newTestCodeIdField(), 15, true), "        New Test Code ID field should be enabled");
		
		logger.info("*** Step 4 Actions: Input a new Test Code ID which isn't existed in the system" + " | Click on 'OK' button");
		enterValues(testCode.newTestCodeIdField(), newTestAbbrev);
		clickHiddenPageObject(testCode.okChangeTestCodeIdPopupButton(), 0);
		
		logger.info("*** Step 4 Expected Result: Test Code ID field is updated with the new value");
		assertEquals(testCodeUtils.getTextValue(testCode.testIDLabel(), 15), newTestAbbrev, "Test code Id field should be equals " + newTestAbbrev);
		
		logger.info("*** Step 5 Actions: Click on 'Save And Clear' button");
		clickHiddenPageObject(testCode.saveAndClearButton(), 0);
		waitUntilElementIsNotVisible(testCode.saveAndClearButton(), 15);
		
		logger.info("*** Step 5 Expected Result: Verify that the Load Test Code page is displayed");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 15),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'");							
		
		logger.info("*** Step 5 Expected Result: Test Code ID value is updated in Database as new value");
		String testAbbrevUpdated = testDao.getTestByTestId(Integer.valueOf(testCodeInfoMap.get(TEST_ID))).getTestAbbrev();
		assertEquals(newTestAbbrev, testAbbrevUpdated, "        The Test Code ID value should be updated as " + newTestAbbrev);
		
		logger.info("*** Step 6 Actions: Input a updated Test Code ID");
		enterValues(testCode.testId(), newTestAbbrev);
		
		logger.info("*** Step 6 Expected Result: Verify the Test Code page is displayed correctly with information of Test Code ID");
		assertTrue(isElementPresent(testCode.saveAndClearButton(), 15), "        Waiting for the Test Code page is displayed");
		assertEquals(testCodeUtils.getTextValue(testCode.testIDLabel(), 15), newTestAbbrev, "Test Code ID should be displayed with value " + newTestAbbrev);
	}
	
	@Test(priority = 1, description = "Verify that there is an error when users change Test Code ID to the existing one in the system")
	public void verifyThereIsAnErrorWhenUsersChangeTestCodeIDToTheExistingOne() throws Exception {
		logger.info("*** Implementation test case [verifyThereIsAnErrorWhenUsersChangeTestCodeIDToTheExistingOne] ***");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		randomCharacter = new RandomCharacter();
		List<String> testCodeInfoList = daoManagerXifinRpm.getRandomTestCodeId(testDb);
		Map<String, String> testCodeInfoMap = testCodeUtils.getMapByGivenList(testCodeInfoList);
		String testAbbrevExisted = testCode.getDifferentTestAbbrev(testCodeInfoMap.get(TEST_ABBREV));
		
		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
    	navigation.navigateToTestCodePage();
		
		logger.info("*** Step 1 Expected Result: Verify that the Load Test Code page is displayed");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 15),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'File Maintenance - Test Code'");							
		
		logger.info("*** Step 2 Actions: Input a valid Test Code ID");
		enterValues(testCode.testId(), testCodeInfoMap.get(TEST_ABBREV));
		
		logger.info("*** Step 2 Expected Result: Verify the Test Code page is displayed correctly with information of Test Code ID");
		assertTrue(isElementPresent(testCode.saveAndClearButton(), 15), "        Waiting for the Test Code page is displayed");
		List<String> testCodeInfoActual = testCode.getTestCodeInfoOnHeaderOfTestCode();
		assertEquals(testCodeInfoActual, testCodeInfoList.subList(1, 5), "        Information of Test Code should be displayed correctly");
		
		logger.info("*** Step 2 Expected Result: 'Change Test Code ID' button is displayed");
		assertTrue(isElementPresent(testCode.changeTestCodeIdButton(), 15), "        Change Test Code ID button should be displayed");
		
		logger.info("*** Step 3 Actions: Click on 'Change Test Code ID' button");
		clickHiddenPageObject(testCode.changeTestCodeIdButton(), 0);
		
		logger.info("*** Step 3 Expected Result: The 'Change Test Code ID' pop up is displayed ");
		assertEquals(testCodeUtils.getTextValue(testCode.changeTestCodeIdPopupTitle(), 15), "Change Test Code ID", "        Change Test Code ID popup should be displayed");
		
		logger.info("*** Step 3 Expected Result: Current Test Code ID field is read only");
		assertTrue(isElementEnabled(testCode.currentTestCodeIdField(), 15, false), "        Current Test Code ID field should be disabled");
		assertEquals(testCodeUtils.getAttributeValue(testCode.currentTestCodeIdField(), 15), testCodeInfoMap.get(TEST_ABBREV), "        The Test Code Id should be displayed correctly");
		
		logger.info("*** Step 3 Expected Result: New Test Code ID field is enabled");
		assertTrue(isElementEnabled(testCode.newTestCodeIdField(), 15, true), "        New Test Code ID field should be enabled");
		
		logger.info("*** Step 4 Actions: Input a new Test Code ID which is existed in the system" + " | Click on 'OK' button");
		enterValues(testCode.newTestCodeIdField(), testAbbrevExisted);
		clickHiddenPageObject(testCode.okChangeTestCodeIdPopupButton(), 0);
		
		logger.info("*** Step 4 Expected Result: Field Validation popup is displayed");
		assertEquals(testCodeUtils.getTextValue(testCode.validationMessageOfChangeTestCodeIdPopup(), 15).trim(), "Test ID " + testAbbrevExisted + " is already in use. Please enter a Test ID that is not already in use.", "        Validation message should be displayed");
	}
}	
