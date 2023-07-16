package com.newXp.tests;

import com.mbasys.mars.ejb.entity.dept.Dept;
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
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

//import org.testng.Assert;

public class RegressionTestCodeTest extends SeleniumBaseTest {

	private TimeStamp timeStamp;
	private TestCode testCode;
	private TestCodeUtils testCodeUtils;
	private RandomCharacter randomCharacter;
	private MenuNavigation navigation;
	private ListUtil listUtil;
	private PayorSearch payorSearch;
	private PayorSearchResults payorSearchResults;
	private TestCodeSearch testCodeSearch;
	private TestCodeSearchResults testCodeSearchResults;
	private ProcCodeSearch procCodeSearch;
	private ProcCodeSearchResults procCodeSearchResults;
	private FeeScheduleSearch feeScheduleSearch;
	private FeeScheduleSearchResults feeScheduleSearchResults;

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

	@Test(priority = 1, description = "Landing page")
	public void testXPR_26() throws Exception {
		logger.info("===== Testing - testXPR_26 =====");
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		navigation = new MenuNavigation(driver, config);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");
	}

	@Test(priority = 1, description = "Load test Code - Load information detail of Test ID with invalid Test ID")
	public void testXPR_43() throws Exception {
		logger.info("===== Testing - testXPR_43 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input Invalid testCode ID");	
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeIdWithNewTestID(String.valueOf(System.currentTimeMillis()));

		logger.info("*** Step 2 Expected Results: - Create Option Pop up is displayed");
		assertTrue(isElementPresent(testCode.noLoadTestRadio(), 5),"         Create Option Pop up  should present.");

		logger.info("*** Step 3 Action: - Select No Load Test option in the Create options pop up ");	
		assertTrue(isElementPresent(testCode.noLoadTestRadio(), 5), "        No load test radio must be displayed");
		selectCheckBox(testCode.noLoadTestRadio());
		assertTrue(isElementPresent(testCode.createOptionOKBtn(), 5), "        OK button in Create Option popup must be displayed");
		testCode.clickCreateOption();

		logger.info("*** Step 3 Expected Results: - The effective date dropdown list is disable and all information is hidden");
		assertTrue(isElementPresent(testCode.effDate(), 5), "        Effective date input must be displayed");
		assertEquals(testCode.effDate().getAttribute("type"),"hidden","        The effective date dropdown list is disable");
		assertTrue(isElementHidden(testCode.tabs(), 5),"        All information is hidden.");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}	

	@Test(priority = 1, description = "Load test Code - Verify Test Code information with valid Test Code ID")
	public void testXPR_44() throws Exception {
		logger.info("===== Testing - testXPR_44 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input valid testCode ID");	
		String testCodeId = "5000";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(testCodeId);

		logger.info("*** Step 2 Expected Results: - Test Code Information page is displayed");
		assertTrue(isElementPresent(testCode.testNameInput(),10),"        Test Name field should be input field.");
		assertTrue(isElementPresent(testCode.labTestCodeID(), 10),"        Lab test Code ID field should be input field.");

		testCode.clickResetBtn();
	}	

	@Test(priority = 1, description = "Load Test Code - Verify All the fields is required in Create Test CodeInformation (Single Test) Screen")
	public void testXPR_148() throws Exception {
		logger.info("===== Testing - testXPR_148 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		randomCharacter = new RandomCharacter(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertEquals(testCode.testCodePageTitle().getText(), "Test Code","        page title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input a valid Test ID and Tab out");
		String TestID = randomCharacter.getRandomNumericString(4);
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeIdWithNewTestID(TestID);		

		logger.info("*** Step 2 Expected Results: - 'Create Option' screen appear");
		assertTrue(isElementPresent(testCode.createScreen(), 5), "        Create Screen should be available.");

		logger.info("*** Step 3 Action: - Choose Single Test option");
		assertTrue(isElementPresent(testCode.singleTestRadio(), 5), "        Single test radio must be displayed");
		selectCheckBox(testCode.singleTestRadio());
		assertTrue(isElementPresent(testCode.createOptionOKBtn(), 5), "        OK button on Create Option must be displayed");
		testCode.clickCreateOption();

		logger.info("*** Step 3 Expected Results: - Test Code Information (Single Test) appear");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        The label test must be displayed");
		assertEquals(testCode.labelTest().getText(), "(Single Test)", "        Label should be (Single Test)");

		logger.info("*** Step 4 Action: - Leave all fields as blank and click Save and Clear button ");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be displayed");
		testCode.testNameInput().clear();
		assertTrue(isElementPresent(testCode.addEffDate(), 5), "        Effective input must be displayed");
		testCode.addEffDate().clear();
		assertTrue(isElementPresent(testCode.saveAndClearButton(), 5), "        Save and clear button must be displayed");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 4 Expected Results: - Error popup will display besides required fields ");
		assertTrue(isElementPresent(testCode.messageTestName(),5), "        Error popup for Test Name should present.");
		assertTrue(isElementPresent(testCode.messageCreEffDate(),5), "        Error popup for Effective Date should present.");

		testCode.closeAllMessageClose();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Load test Code - Verify All the fields is required in Create Test CodeInformation (Profile) Screen")
	public void testXPR_151() throws Exception {
		logger.info("===== Testing - testXPR_151 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		randomCharacter = new RandomCharacter(driver);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertEquals(testCode.testCodePageTitle().getText(), "Test Code","        page title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input Invalid testCode ID");
		String testCodeId = randomCharacter.getRandomNumericString(5);
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeIdWithNewTestID(testCodeId);

		logger.info("*** Step 2 Expected Results: - Create Option Pop up is displayed");
		assertTrue(isElementPresent(testCode.profileTestRadio(), 5),"         Create Option Pop up  should present.");

		logger.info("*** Step 3 Action: - Select Profile option in the Create options pop up ");	
		assertTrue(isElementPresent(testCode.profileTestRadio(), 5), "        Profile radio button must be displayed");
		selectCheckBox(testCode.profileTestRadio());
		assertTrue(isElementPresent(testCode.createOptionOKBtn(), 5), "        Ok button on Create Option popup must be displayed");
		testCode.clickCreateOption();

		logger.info("*** Step 3 Expected Results: - Test Code Information page(Profile) is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test page label must be displayed");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Profile)"),"        Test Code Information page(Profile) is displayed");

		logger.info("*** Step 4 Action: - Input empty data in required fields");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be displayed");
		testCode.testNameInput().clear();
		assertTrue(isElementPresent(testCode.addEffDate(), 5), "        Effective date input must be displayed");
		testCode.addEffDate().clear();
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "       Save and clear button must be displayed");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 4 Expected Results: - Check all required fields in Create Test Code Information (Profile) Screen ");
		assertTrue(isElementPresent(testCode.messageTestName(), 5),"        System should show a error message : Test name: Field is required ");
		assertTrue(isElementPresent(testCode.messageCreEffDate(), 5),"        System should show a error message : Effective Date: Field is required");

		testCode.closeAllMessageClose();
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Log into RPM and verify The No Load Test Page")
	public void testXPR_154() throws Exception{
		logger.info("===== Testing - testXPR_154 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Login to RPM with SSO Username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertEquals(testCode.testCodePageTitle().getText(), "Test Code","        page title should be 'Test Code'");

		logger.info("*** Step 2 Actions: - Input new TestId");
		String newTestId = randomCharacter.getRandomNumericString(6);
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input must be displayed");
		testCode.checkInputTestCodeIdWithNewTestID(newTestId);

		logger.info("*** Step 2 Expected result: - Verify that Creat Option popup is displayed");
		assertTrue(isElementPresent(testCode.noLoadTestRadio(), 5), "        Create Option popup is displayed");

		logger.info("*** Step 3 Actions: - Select No Load Test option on popup");
		assertTrue(isElementPresent(testCode.noLoadTestRadio(), 5), "        No load test radio button must be displayed");
		selectCheckBox(testCode.noLoadTestRadio());
		assertTrue(isElementPresent(testCode.createOptionOKBtn(), 5), "        Ok button on Create Option popup must be displayed");
		testCode.clickCreateOption();

		logger.info("*** Step 3 Expected Results: - Verify that the Test Code page with title is No Load Test is displayed with some fields");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test page label must be displayed");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(No Load Test)"), "        Title should be No Load Test");
		assertTrue(isElementPresent(testCode.effectiveDatelabel(), 5), "        Effective date label must be displayed");
		assertEquals(testCode.effectiveDatelabel().getText(), timeStamp.getCurrentDate(), "Effective date is displayed current date");
		assertTrue(isElementPresent(testCode.effDate(), 5), "        Effective date must be displayed");
		assertEquals(testCode.effDate().getAttribute("type"),"hidden","        The effective date dropdown list is disable");
		assertTrue(isElementHidden(testCode.tabs(), 5),"        All information is hidden.");

		testCode.clickResetBtn();
	}

	//Search Test Code Function
	@Test(priority = 1, description = "Search Test Code - Search with invalid test id")
	@Parameters({"invalidTestId"})
	public void testXPR_38(String invalidTestId) throws Exception {
		logger.info("===== Testing - testXPR_38 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeSearch = new TestCodeSearch(driver);
		testCodeSearchResults = new TestCodeSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Open search test code");
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5), "        Test code search button must be displayed");
		testCodeSearch.clickTestCodeSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Search test code popup opened");
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "        Search button should be available.");

		logger.info("*** Step 3 Action: - Search with invalid test code id");
		assertTrue(isElementPresent(testCodeSearch.testCodeIdSearchPage(), 5), "        Test Code ID input must be displayed");
		testCodeSearch.setTestCodeIdSearchPage(invalidTestId);
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "        Search button must be displayed");
		testCodeSearch.clickSearchBtnInSearchScreen();

		logger.info("*** Step 3 Expected Results: - Search result should be empty");
		assertTrue(isElementPresent(testCodeSearchResults.testSearchTableResult(), 5), "        Search result should be available.");
		assertEquals(getTableTotalRowSize(testCodeSearchResults.testSearchTableResult()),1,"        Search result should be empty.");
	}
	
	@Test(priority = 1, description = "Search Test Code - Search valid Lab info")
	public void testXPR_40() throws Exception {
		logger.info("===== Testing - testXPR_40 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeSearch = new TestCodeSearch(driver);
		testCodeSearchResults = new TestCodeSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Click on test code search icon");
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5), "      Search button must be displayed");
		testCodeSearch.clickTestCodeSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Test code search page window is displayed");		
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "        Test Code Search page should be available.");

		logger.info("*** Step 3 Action: - Input Lab test code ID and Facility on Lab Info");
		List<String> testCodeInfo = daoManagerXifinRpm.getLabInfoSearch(testDb);
		assertTrue(isElementPresent(testCodeSearch.labTestCodeIdInputSearchPage(), 5), "        Label Test Code ID input must be displayed");
		testCodeSearch.setLabTestCodeId(testCodeInfo.get(6));
		assertTrue(isElementPresent(testCodeSearch.facilityDropdownOnSearchPage(), 5), "        Facility dropdown must be displayed");
		selectItem(testCodeSearch.facilityDropdownOnSearchPage(), testCodeInfo.get(9));
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "      Search button must be displayed");
		testCodeSearch.clickSearchBtnInSearchScreen();	

		logger.info("*** Step 3 Expected Result: - Test code search result page will display");
		assertTrue(isElementPresent(testCodeSearchResults.testSearchTableResult(), 5), "        Search result should be available.");
		assertTrue(getColumnValue(testCodeSearchResults.testSearchTableResult(), testCodeInfo.get(1)), "        Search result should be match will search Lab Code Id.");
		assertTrue(getColumnValue(testCodeSearchResults.testSearchTableResult(), testCodeInfo.get(6)), "        Search result should be match will search Facility.");
	}

	@Test(priority = 1, description = "Search Test Code - Verify Search Result Page")
	public void testXPR_41() throws Exception {
		logger.info("===== Testing - testXPR_41 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeSearch = new TestCodeSearch(driver);
		testCodeSearchResults = new TestCodeSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Click on test code search icon");
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5), "      Search button must be displayed");
		testCodeSearch.clickTestCodeSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Test code search page window is displayed");		
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "        Test Code Search page should be available.");

		logger.info("*** Step 3 Action: - Input Lab test code Name on Test Code Info ");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "      Test name input must be displayed");
		testCode.setTestName("*");
		testCodeSearch.clickSearchBtnInSearchScreen();	

		logger.info("*** Step 3 Expected Result: - Test code search result page will display");
		assertTrue(isElementPresent(testCodeSearchResults.testSearchTableResult(), 5), "        Test Code Search Result page should be available.");
		assertTrue(isElementPresent(testCodeSearchResults.firstPageButtonOnSearchResult(), 5), "       First button should be displayed.");
		assertTrue(isElementPresent(testCodeSearchResults.prevPageButtonOnSearchResult(), 5), "        Prev button should be displayed.");
		assertTrue(isElementPresent(testCodeSearchResults.nextPageButtonOnSearchResult(), 5), "        Next button should be displayed.");
		assertTrue(isElementPresent(testCodeSearchResults.lastPageButtonOnSearchResult(), 5), "        Last button should be displayed.");
		assertTrue(isElementPresent(testCodeSearchResults.entriesPerPageOnSearchResult(), 5), "        Entry Per Page dropdown should be displayed.");
	}

	@Test(priority=1, description="Search Test Code - Search With Empty Field")
	public void testXPR_136() throws Exception{
		logger.info("===== Testing - testXPR_136 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeSearch = new TestCodeSearch(driver);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Actions: - Click Test Code Search Icon Button");
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5),"        Search test code button is available");
		testCodeSearch.clickTestCodeSearchIconBtn();
		switchToPopupWin();		

		logger.info("*** Step 2 Expected Results: - Test Code Search Page Window is displayed");
		assertTrue(isElementPresent(testCodeSearch.testCodeIdSearchPage(), 5),"       Test Code Search Page window should be displayed");

		logger.info("*** Step 3 Action: - Search test code with empty data");
		assertTrue(isElementPresent(testCodeSearch.procCodeTypeOnSearchPage(), 5), "      Procedure Code input must be displayed");
		testCodeUtils.selectItemByIndex(testCodeSearch.procCodeTypeOnSearchPage(), 0);
		testCodeSearch.clickSearchBtnInSearchScreen();

		logger.info("*** Step 3 Expected Results: - System will show error message");
		assertTrue(isElementPresent(testCode.messageContent(), 5),"        System should be displayed a error message : At least one field must be used to initiate a search.");
	}

	@Test(priority = 1, description = "Search Test Code - Close search")
	public void testXPR_138() throws Exception {
		logger.info("===== Testing - testXPR_138 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeSearch = new TestCodeSearch(driver);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Open search test code");
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5),"        Search test code button is available");
		testCodeSearch.clickTestCodeSearchIconBtn();
		String parentWindow =  switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Search test code popup opened");
		String getSearchUrl = driver.getCurrentUrl();
		assertTrue(isElementPresent(testCodeSearch.closeSearchButton(), 5), "        Close search should be available.");

		logger.info("*** Step 3 Action: - Click close search popup");
		testCodeSearch.clickCloseSearch();

		logger.info("*** Step 3 Expected Results: - System must close test code search screen");
		switchToParentWin(parentWindow);
		String getCurrentUrl =  driver.getCurrentUrl();
		assertNotEquals(getSearchUrl, getCurrentUrl,"       Search screen must be closed");
	}

	@Test(priority = 1, description = "Search Test Code - Search All Test Code ID")
	public void testXPR_139() throws Exception {
		logger.info("===== Testing - testXPR_139 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeSearch = new TestCodeSearch(driver);
		testCodeUtils = new TestCodeUtils(driver);
		testCodeSearchResults = new TestCodeSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Click on test code search icon");
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5), "        Search button must be displayed");
		testCodeSearch.clickTestCodeSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Test code search page window is displayed");		
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "        Test Code Search page should be available.");

		logger.info("*** Step 3 Action: - Input Test Code ID on Test Code Info ");
		assertTrue(isElementPresent(testCodeSearch.testCodeIdSearchPage(), 5), "        Test Code ID input must be displayed");
		testCodeSearch.setTestCodeIdSearchPage("*");
		assertTrue(isElementPresent(testCodeSearch.procCodeTypeOnSearchPage(), 5), "        Procedure Code input must be displayed");
		String procCodeType = testCodeUtils.getTextSelectedInDropdown(testCodeSearch.procCodeTypeOnSearchPage());
		testCodeSearch.clickSearchBtnInSearchScreen();

		logger.info("*** Step 3 Expected Result: - System will display all TestID in database in search result page.");
		assertTrue(isElementPresent(testCodeSearchResults.testSearchTableResult(), 5), "        Test Code Search Result page should be available.");
		assertTrue(isElementPresent(testCodeSearchResults.rightPagerTableResultSearch(), 5), "        Pager in table result must be displayed");
		String totalTestCodeID = testCodeUtils.getTotalResultSearch(testCodeSearchResults.rightPagerTableResultSearch());
		String actualTotalSearchResult = testCodeUtils.convertDecimalFormat(Integer.parseInt(daoManagerXifinRpm.getTotalTestCodeIDOnSearch(testDb,procCodeType)));
		assertEquals(actualTotalSearchResult, totalTestCodeID, "        Total TestCode should be "+totalTestCodeID);
	}

	@Test(priority = 1, description = "Search Test Code - Search test code with any Procedure code")
	public void testXPR_140() throws Exception {
		logger.info("===== Testing - testXPR_140 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeSearch = new TestCodeSearch(driver);
		testCodeUtils= new TestCodeUtils(driver);
		testCodeSearchResults = new  TestCodeSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Click on test code search icon");
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5), "        Search button must be displayed");
		testCodeSearch.clickTestCodeSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Test code search page window is displayed");		
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "        Test Code Search page should be available.");

		logger.info("*** Step 3 Action: - Input Procedure to Procedure Dropdown on Procedure Code Info  ");
		List<String> testCodeInfo = daoManagerXifinRpm.getProcedureCodeInfoSearch(testDb);
		assertTrue(isElementPresent(testCodeSearch.procCodeTypeOnSearchPage(), 5), "        Procedure Code input must be displayed");
		selectItem(testCodeSearch.procCodeTypeOnSearchPage(), testCodeInfo.get(3));	
		String proCodeTable = testCodeUtils.getValueSelectedInDropdown(testCodeSearch.procCodeTypeOnSearchPage());
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "      Search button must be displayed");
		testCodeSearch.clickSearchBtnInSearchScreen();

		logger.info("*** Step 3 Expected Result: - System will display all TestID in database in search result page.");
		assertTrue(isElementPresent(testCodeSearchResults.rightPagerTableResultSearch(), 5), "        Pager in result table must be displayed");
		String actualTotalNum = testCodeUtils.getTotalResultSearch(testCodeSearchResults.rightPagerTableResultSearch());
		String expectTotalNum = testCodeUtils.convertDecimalFormat(Integer.parseInt(daoManagerXifinRpm.getTotalTestCodeIDOnSearch(testDb,proCodeTable)));
		assertEquals(actualTotalNum, expectTotalNum, "        Total TestCode should be " + expectTotalNum);
	}
	
	@Test(priority=1, description="Search Test Code - New Search")
	public void testXPR_141() throws Exception{
		logger.info("===== Testing - testXPR_141 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeSearch = new TestCodeSearch(driver);
		testCodeSearchResults = new TestCodeSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Open search test code");
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5),"        Search test code button is available");
		testCodeSearch.clickTestCodeSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Test Code Search page window is displayed");
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "        Test Code Search page window should be available.");

		logger.info("*** Step 3 Actions: - Input Valid value and Click Search button");
		String testCodeId = testDao.getTestCodeSearchInfo().getTestAbbrev();
		assertTrue(isElementPresent(testCodeSearch.testCodeIdSearchPage(), 5), "        Test Code ID input must be displayed");
		testCodeSearch.setTestCodeIdSearchPage(testCodeId);
		testCodeSearch.clickSearchBtnInSearchScreen();

		logger.info("*** Step 3 Expected Results: - System will open test search result screen");
		assertTrue(isElementPresent(testCodeSearchResults.testSearchTableResult(), 5),"        Search result screen should be  available.");

		logger.info("*** Step 4 Actions: - Click New Search Button");
		assertTrue(isElementPresent(testCodeSearchResults.newSearchButton(), 5), "        New Search Button should be displayed.");
		testCodeSearchResults.clickNewSearch();

		logger.info("*** Step 4 Expected Results: - System will return new search screen");
		assertTrue(isElementPresent(testCodeSearch.testCodeIdSearchPage(), 5), "        System should return new search screen.");
	}

	@Test(priority=1, description="Search Test Code - Search Test Code With Type is Profile")
	public void testXPR_219() throws Exception{
		logger.info("===== Testing - testXPR_219 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeSearch = new TestCodeSearch(driver);
		testCodeUtils = new TestCodeUtils(driver);
		testCodeSearchResults = new TestCodeSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Open search test code");
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5),"        Search test code button is available");
		testCodeSearch.clickTestCodeSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Test Code Search page window is displayed");
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "        Test Code Search page window should be available.");

		logger.info("*** Step 3 Actions: - Input Valid value and Click Search button");
		String procCodeType = daoManagerXifinRpm.getProcedureCodeInfoSearch(testDb).get(3);
		assertTrue(isElementPresent(testCodeSearch.procCodeTypeOnSearchPage(), 5), "      Procedure Code input must be displayed");
		selectItem(testCodeSearch.procCodeTypeOnSearchPage(), procCodeType);
		assertTrue(isElementPresent(testCodeSearch.searchOptionProfile(2), 5), "      Search option Profile must be displayed");
		selectCheckBox(testCodeSearch.searchOptionProfile(2));
		testCodeSearch.clickSearchBtnInSearchScreen();

		logger.info("*** Step 3 Expected Results: - System will display all test code Id have type is profile");
		assertTrue(isElementPresent(testCodeSearchResults.rightPagerTableResultSearch(), 5), "        Pager in Table Result must be displayed");
		int totalTestCodeInDB  = daoManagerXifinRpm.getTotalTestCodeIsProFileByProcedureCodeType(procCodeType, testDb);
		String totalTestCodeInGui = testCodeUtils.getTotalResultSearch(testCodeSearchResults.rightPagerTableResultSearch());
		assertEquals(totalTestCodeInGui, testCodeUtils.convertDecimalFormat((totalTestCodeInDB)),"        System should be displayed all test code Id have type is profile");
	}

	@Test(priority = 1, description = "Search Test Code - Search test code with type is non-profile")
	public void testXPR_220() throws Exception {
		logger.info("===== Testing - testXPR_220 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeSearch = new TestCodeSearch(driver);
		testCodeUtils = new TestCodeUtils(driver);
		testCodeSearchResults = new TestCodeSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Open search test code");
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5),"        Search test code button is available");
		testCodeSearch.clickTestCodeSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Search test code popup opened");
		assertTrue(isElementPresent(testCodeSearch.closeSearchButton(), 5), "        Close search should be available.");

		logger.info("*** Step 3 Action: - Select procedure code table and non-profile search options");
		String procedureCodeTable = daoManagerXifinRpm.getProcedureCodeInfoSearch(testDb).get(3);
		assertTrue(isElementPresent(testCodeSearch.procCodeTypeOnSearchPage(), 5), "        Procedure Code input must be displayed");
		selectItem(testCodeSearch.procCodeTypeOnSearchPage(), procedureCodeTable);
		String valueProcCode = testCodeUtils.getValueSelectedInDropdown(testCodeSearch.procCodeTypeOnSearchPage());
		assertTrue(isElementPresent(testCodeSearch.searchOptionProfile(3), 5), "        Search option Profile must be displayed");
		selectCheckBox(testCodeSearch.searchOptionProfile(3));
		testCodeSearch.clickSearchBtnInSearchScreen();

		logger.info("*** Step 3 Expected Results: - System will display all test code id that match with search condition");
		assertTrue(testCode.isElementDisplayed(testCodeSearchResults.rightPagerTableResultSearch(), 10), "        Pager in Table result should be displayed.");
		String actualTotalNum = testCodeUtils.getTotalResultSearch(testCodeSearchResults.rightPagerTableResultSearch());
		String expectTotalNum = testCodeUtils.convertDecimalFormat(Integer.parseInt(daoManagerXifinRpm.getTotalResultTestCodeSearchNonProfile(valueProcCode, testDb)));
		assertEquals(actualTotalNum, expectTotalNum, "        Search screen must be closed.");
	}

	@Test(priority = 1, description = "Search Test Code - Search test code with sort is facility")
	@Parameters({"sortBySearch"})
	public void testXPR_221(String sortBySearch) throws Exception {
		logger.info("===== Testing - testXPR_221 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeSearch = new TestCodeSearch(driver);
		testCodeSearchResults = new TestCodeSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Open search test code");
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5),"        Search test code button is available");
		testCodeSearch.clickTestCodeSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Search test code popup opened");
		assertTrue(isElementPresent(testCodeSearch.closeSearchButton(), 5), "        Close search should be available.");

		logger.info("*** Step 3 Action: - Select sort by Facility");
		assertTrue(isElementPresent(testCodeSearch.sortByDropDown(), 5), "        Sort by dropdown must be displayed");
		selectItem(testCodeSearch.sortByDropDown(), sortBySearch);
		testCodeSearch.clickSearchBtnInSearchScreen();

		logger.info("*** Step 3 Expected Results: - System will sort search result base on Facility");
		assertTrue(isElementPresent(testCodeSearchResults.testSearchTableResult(), 5), "        Table result must be displayed");
		assertTrue(getSortingComparisonOnTable(testCodeSearchResults.testSearchTableResult(), 4, "asc"),"        Search screen must be closed.");
	}

	@Test(priority = 1, description = "Search Test Code - Verify footer button at search test code result page")
	public void testXPR_222() throws Exception {
		logger.info("===== Testing - testXPR_222 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeSearch = new TestCodeSearch(driver);
		testCodeUtils = new TestCodeUtils(driver);
		testCodeSearchResults = new TestCodeSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Click on test code search icon");
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5), "      Search button must be displayed");
		testCodeSearch.clickTestCodeSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Test code search page window is displayed");		
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "        Test Code Search page should be available.");

		logger.info("*** Step 3 Action: - Input '*' in TestID field ");
		assertTrue(isElementPresent(testCodeSearch.testCodeIdSearchPage(), 5), "        Test Code input must be displayed");
		testCodeSearch.setTestCodeIdSearchPage("*");
		testCodeSearch.clickSearchBtnInSearchScreen();

		logger.info("*** Step 3 Expected Result: - Test code search result page will display");
		assertTrue(isElementPresent(testCodeSearchResults.testSearchTableResult(), 5), "        Test Code Search Result page should be available.");

		logger.info("*** Step 4 Action: -Click The Last page icon button ");
		assertTrue(isElementPresent(testCodeSearchResults.lastPageButtonOnSearchResult(), 5), "        Last pager button must be displayed");	
		testCodeSearchResults.clickLastPageButton();

		logger.info("*** Step 4 Expected Result: - The Last page is working");
		assertTrue(isElementPresent(testCodeSearchResults.currentPageOnSearchResultPage(), 5), "        Current page input must be displayed");
		assertTrue(isElementPresent(testCodeSearchResults.totalPageOnSearchResultPage(), 5), "        Total page label must be displayed");
		assertEquals(testCodeSearchResults.currentPageOnSearchResultPage().getAttribute("value"), testCodeSearchResults.totalPageOnSearchResultPage().getText().replace(",",""), "        Last Button should work");

		logger.info("*** Step 5 Action: - Click The First page icon Button");
		assertTrue(isElementPresent(testCodeSearchResults.firstPageButtonOnSearchResult(), 5), "        First page button must be displayed");	
		testCodeSearchResults.clickFirstPageButton();

		logger.info("*** Step 5 Expected Result: - The First page icon button is working");
		waitUntilElementIsNotVisible(testCodeSearchResults.lastPageButtonOnSearchResult(), 3);	
		assertEquals(testCodeSearchResults.currentPageOnSearchResultPage().getAttribute("value"),"1", "        First Button should work");

		logger.info("*** Step 6 Action: - Click The Next page icon button");
		assertTrue(isElementPresent(testCodeSearchResults.nextPageButtonOnSearchResult(), 5), "        Next page button must be displayed");	
		testCodeSearchResults.clickNextPageButton();

		logger.info("*** Step 6 Expected Result: - The Next page icon button is working");
		waitUntilElementIsNotVisible(testCodeSearchResults.firstPageButtonOnSearchResult(), 5);	
		assertEquals(testCodeSearchResults.currentPageOnSearchResultPage().getAttribute("value"),"2", "        Next Button should work. Current page number should be 2.");

		logger.info("*** Step 7 Action: -Click Previous page icon button");
		assertTrue(isElementPresent(testCodeSearchResults.prevPageButtonOnSearchResult(), 5), "        Previous page button must be displayed.");	
		testCodeSearchResults.clickPrevPageButton();

		logger.info("*** Step 7 Expected Result: - The Previous page icon button is working");
		waitUntilElementIsNotVisible(testCodeSearchResults.nextPageButtonOnSearchResult(), 3);	
		assertEquals(testCodeSearchResults.currentPageOnSearchResultPage().getAttribute("value"),"1", "        Prev Button should work. Current page number should be 1.");
	}

	@Test(priority = 1, description = "Search Test Code - Verify option display entry per page")
	public void testXPR_223() throws Exception {
		logger.info("===== Testing - testXPR_223 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeSearch = new TestCodeSearch(driver);
		testCodeUtils = new TestCodeUtils(driver);
		testCodeSearchResults = new TestCodeSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Click on test code search icon");
		assertTrue(isElementPresent(testCodeSearch.testCodeSearchIconBtn(), 5), "        Search button must be displayed");
		testCodeSearch.clickTestCodeSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Test code search page window is displayed");		
		assertTrue(isElementPresent(testCodeSearch.searchButtonInSearchScreen(), 5), "        Test Code Search page should be available.");

		logger.info("*** Step 3 Action: - Input '*' in TestID field ");
		assertTrue(isElementPresent(testCodeSearch.testCodeIdSearchPage(), 5), "        Test code id input must be displayed");
		testCodeSearch.setTestCodeIdSearchPage("*");
		testCodeSearch.clickSearchBtnInSearchScreen();

		logger.info("*** Step 3 Expected Result: - Test code search result page will display");
		assertTrue(isElementPresent(testCodeSearchResults.testSearchTableResult(), 5), "        Test Code Search Result page should be available.");

		logger.info("*** Step 4 Action: - Input '40' to entry per page field");	
		assertTrue(isElementPresent(testCodeSearchResults.entriesPerPageOnSearchResult(), 5), "        Entries record per page select must be displayed");
		selectItem(testCodeSearchResults.entriesPerPageOnSearchResult(), "40");
		wait.until(ExpectedConditions.invisibilityOf(testCode.searchInProgressInfoText()));

		logger.info("*** Step 4 Expected Result: - The number of entries per page is working");	
		int totalRowNum = getTableTotalRowSize(testCodeSearchResults.testSearchTableResult()) - 1;
		assertEquals(totalRowNum, 40, "        Current page should display 40 records.");
	}

	//Effective Date
	@Test(priority = 1, description = "Create new Effective date with invalid Exp Date")
	public void testXPR_47() throws Exception {
		logger.info("===== Testing - testXPR_47 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String singleTest = "HN550LR";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Input valid effective date");
		scrollToElement(0, -250);
		String effDate = testCode.currEffDate().getAttribute("value").trim();
		testCode.clickCreateEffDateIcon();
		testCode.setEffectiveDate(testCodeUtils.getNextDay(effDate));

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");

		logger.info("*** Step 4 Action: - Cancel copy Effective Date");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Expected Results: - Copy effective date dialog closed and all fields reset to empty");
		assertTrue(isElementHidden(testCode.effCopyDialog(), 5), "        Copy effective date dialog closed");
		assertEquals(testCodeUtils.getTextSelectedInDropdown(testCode.selectDeps()).trim(), "", "        Department must be reset");
		assertEquals(testCode.expDateInput().getText().trim(), "", "        Exp date must be reset");

		logger.info("*** Step 5 Action: - Input invalid format effective date");
		testCode.clickCreateEffDateIcon();
		String inValidEffDate = timeStamp.getCurrentDate("yyyy-MM/dd");
		testCode.setEffectiveDate(inValidEffDate);

		logger.info("*** Step 5 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.messageCreEffDate(), 5), "        Message warning dialog appear");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Verify procedure exp date of any effective date")
	public void testXPR_189() throws Exception {
		logger.info("===== Testing - testXPR_189 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		randomCharacter = new RandomCharacter();

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String singleTest = "LI970XL";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Input valid effective date");
		String minEffDate = testCodeUtils.getLastItemDropdown(testCode.selectEffDate());
		String effDateInput = testCodeUtils.getPreviousYear(minEffDate);  
		testCode.clickCreateEffDateIcon();
		testCode.setEffectiveDate(effDateInput);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");

		logger.info("*** Step 4 Action: - Cancel copy Effective Date");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Expected Results: - Copy effective date dialog closed and all fields reset to empty");
		assertTrue(isElementHidden(testCode.effCopyDialog(), 5), "        Copy effective date dialog closed");
		assertEquals(testCodeUtils.getTextSelectedInDropdown(testCode.selectDeps()).trim(), "", "        Department must be reset");
		assertEquals(testCode.expDateInput().getText().trim(), "", "        Exp date must be reset");

		logger.info("*** Step 5 Action: - Add procedures");
		testCode.clickProcedureTab();
		testCode.clickAddProcedureBtn();
		List<String> procedureCode = daoManagerXifinRpm.getProcCodeInfoFromPROCCDByTestAbbrev(singleTest, testDb);
		testCode.inputProcedureCode(procedureCode.get(0));
		testCode.selectProcedureTable(procedureCode.get(1));
		testCode.inputProcedureExpDate(testCodeUtils.getNextYear(minEffDate));
		testCode.clickOkProcedureBtn();

		logger.info("*** Step 5 Expected Result: - Verify that procedure code must be added");
		waitUntilElementIsNotVisible(testCode.procedureTable(), 5);
		assertTrue(isElementPresent(testCode.procedureTable(), 5), "      Procedure table must be displayed");
		assertTrue(getColumnValue(testCode.procedureTable(), procedureCode.get(1)), "        Procedures must be added in table");

		logger.info("*** Step 6 Action: - Add Facilities");
		testCode.clickFacilityTab();
		testCode.clickAddFacilityBtn();
		String facility = testCode.selectPerformingFacility(2);
		String minPrice = randomCharacter.getRandomNumericString(2);
		String labCost  = minPrice;
		assertTrue(isElementPresent(testCode.labCostInput(), 5), "        Lab cost input must be displayed");
		testCode.inputLabCost(labCost);
		assertTrue(isElementPresent(testCode.minPriceInput(), 5), "        Min Price input must be displayed");
		testCode.inputMinPrice(minPrice);
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button must be displayed");
		testCode.clickOkFacilityBtn();

		logger.info("*** Step 6 Expected Result: - Verify that facility code must be added");
		assertTrue(isElementPresent(testCode.facilityTable(), 5), "        Facility table must be displayed");
		assertTrue(getColumnValue(testCode.facilityTable(), facility), "        Facility must be added in table");

		logger.info("*** Step 7 Action: - Select Department");
		Dept depInfo = depDao.getDepartment();
		assertTrue(isElementPresent(testCode.selectDeps(), 5), "        Department select must be displayed");
		selectItemByVal(testCode.selectDeps(), String.valueOf(depInfo.getDeptId()));

		logger.info("*** Step 7 Expected Result : - Save and clear button must be available");
		assertTrue(isElementPresent(testCode.saveAndClearButton(), 5), "       Save and clear button must be available");

		logger.info("*** Step 8 Action: - Save all");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and clear button must be displayed");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 8 Expected Result : - Error must be present");
		assertTrue(isElementPresent(testCode.errorListPanel(), 5), "       Error must be present");

		logger.info("*** Step 9 Action: - Switch to procedure tab");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();

		logger.info("*** Step 9 Expected Result : - Exp procedure update to less than the next Eff date one day");
		assertTrue(getColumnValue(testCode.procedureTable(), testCodeUtils.getPreviousDay(minEffDate)), "       Exp procedure update to less than the next Eff date one day");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Effective Date - Add Payor ID With Exp Date greater next eff date")
	public void testXPR_190() throws Exception {
		logger.info("===== Testing - testXPR_190=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		randomCharacter = new RandomCharacter();

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String singleTest = "KIJ607";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Input valid effective date");
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date select must be displayed");
		String minEffDate = testCodeUtils.getLastItemDropdown(testCode.selectEffDate());
		String effDateInput = testCodeUtils.getPreviousYear(minEffDate); 
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Effective date icon must be displayed");
		testCode.clickCreateEffDateIcon();
		testCode.setEffectiveDate(effDateInput);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");

		logger.info("*** Step 4 Action: - Cancel copy Effective Date");
		assertTrue(isElementPresent(testCode.cancelEffCopyDialog(), 5), "        Cancel button for copy effective dialog must be displayed");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Expected Results: - Copy effective date dialog closed and all fields reset to empty");
		assertTrue(isElementHidden(testCode.effCopyDialog(), 5), "        Copy effective date dialog closed");
		assertEquals(testCodeUtils.getTextSelectedInDropdown(testCode.selectDeps()).trim(), "", "        Department must be reset");
		assertEquals(testCode.expDateInput().getText().trim(), "", "        Exp date must be reset");

		logger.info("*** Step 5 Action: - Add new procedures with exp date is less than 1 day the next day");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5), "        Add Procedure button must be displayed");
		testCode.clickAddProcedureBtn();
		List<String> procedureCode = daoManagerXifinRpm.getProcCodeInfoFromPROCCDByTestAbbrev(singleTest, testDb);
		assertTrue(isElementPresent(testCode.procedureCodeInput(), 5), "        Procedure Code input must be displayed");
		testCode.inputProcedureCode(procedureCode.get(0));
		assertTrue(isElementPresent(testCode.procedureTableType(), 5), "        Procedure Table Select must be displayed");
		testCode.selectProcedureTable(procedureCode.get(1));
		assertTrue(isElementPresent(testCode.procedureExpDateInput(), 5), "        Procedure Expiration Date input must be displayed");
		testCode.inputProcedureExpDate(testCodeUtils.getPreviousDay(minEffDate));
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button must be displayed");
		testCode.clickOkProcedureBtn();

		logger.info("*** Step 5 Expected Result: - Verify that procedure code must be added");
		waitUntilElementIsNotVisible(testCode.procedureTable(), 5);
		assertTrue(isElementPresent(testCode.procedureTable(), 5), "        Procedure table must be displayed");
		assertTrue(getColumnValue(testCode.procedureTable(), procedureCode.get(1)), "        Procedures must be added in table");

		logger.info("*** Step 6 Action: - Add Facilities");
		assertTrue(isElementPresent(testCode.facilityTab(), 5), "        Facility tab must be displayed");
		testCode.clickFacilityTab();
		assertTrue(isElementPresent(testCode.addFacilityBtn(), 5), "        Add facility button must be displayed");
		testCode.clickAddFacilityBtn();
		String facility = testCode.selectPerformingFacility(2);
		String minPrice = randomCharacter.getRandomNumericString(2);
		String labCost  = minPrice;
		assertTrue(isElementPresent(testCode.labCostInput(), 5), "        Lab cost input must be displayed");
		testCode.inputLabCost(labCost);
		assertTrue(isElementPresent(testCode.minPriceInput(), 5), "        Min price input must be displayed");
		testCode.inputMinPrice(minPrice);
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        OK button must be displayed");
		testCode.clickOkFacilityBtn();

		logger.info("*** Step 6 Expected Result: - Verify that facility code must be added");
		assertTrue(isElementPresent(testCode.facilityTable(), 5), "        Facility table must be displayed");
		assertTrue(getColumnValue(testCode.facilityTable(), facility), "        Facility must be added in table");

		logger.info("*** Step 7 Action: Modifier tab - Add new payor ID with exp date is greater than the next eff date on Payor Specific Modifier Table");
		List<List<String>> modifierList =  testCode.addModifiers(1, this, testCodeUtils.getNextDay(minEffDate));

		logger.info("*** Step 7 Expected Result: Verify that new payorID must be added ");
		assertTrue(isElementPresent(testCode.payorSpecificModifiersTable(), 5), "        Payor Specific table must be displayed in Modifier tab.");
		assertTrue(getColumnValue(testCode.payorSpecificModifiersTable(), modifierList.get(0).get(0)), "        new payor must be added in table");

		logger.info("*** Step 8 Action: - Select Department");
		Dept depInfo = depDao.getDepartment();
		assertTrue(isElementPresent(testCode.selectDeps(), 5), "        Department select must be displayed");
		selectItemByVal(testCode.selectDeps(), String.valueOf(depInfo.getDeptId()));

		logger.info("*** Step 8 Expected Result : - Save and clear button must be available");
		assertTrue(isElementPresent(testCode.saveAndClearButton(), 5), "       Save and clear button must be available");

		logger.info("*** Step 9 Action: - Click save all button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and clear button must be displayed");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 9 Expected Result : - Error message must be present");
		assertTrue(isElementPresent(testCode.errorListPanel(), 5), "       Error must be present");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "ProFile Code Detail - Eff date - Create new eff date is the same existing eff date")
	public void testXPR_226() throws Exception {
		logger.info("===== Testing - testXPR_226=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String TestID = "EEK735";
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Input valid effective date");
		String effDate = testCode.currEffDate().getAttribute("value").trim();
		String nextEffDate = testCodeUtils.getNextDay(effDate);
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "         Effective date icon must be displayed");
		testCode.clickCreateEffDateIcon();
		testCode.setEffectiveDate(nextEffDate);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");

		logger.info("*** Step 4 Action: - Cancel copy Effective Date and Click on pencil icon again and input existing eff date");
		assertTrue(isElementPresent(testCode.cancelEffCopyDialog(), 5), "        Cancel effective Copy dialog must be displayed");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Action: - Click on pencil icon again and input existing eff date");
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Effective date icon must be displayed");
		testCode.clickCreateEffDateIcon();
		testCode.setEffectiveDate(nextEffDate);

		logger.info("*** Step 4 Expected Results: - Error message should be display");
		assertTrue(isElementPresent(testCode.messageContent(), 5), "        Message error content must be displayed");
		assertTrue(testCode.messageContent().getText().trim().contains("Effective Date currently exists, please try again."),"        Error Message should be display.'");

		assertTrue(isElementPresent(testCode.closeErrorMessage(), 5), "        Close Message error button must be displayed");
		testCode.clickcloseErrorMessage();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Add component with Eff date less than component Eff date")
	@Parameters({"testName"})
	public void testXPR_227(String testName) throws Exception {
		logger.info("===== Testing - testXPR_227 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);
		randomCharacter = new RandomCharacter();

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("***Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input a Invalid Test ID and Tab out");
		String testCodeIdSingleTest = String.valueOf(System.currentTimeMillis());
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeIdWithNewTestID(testCodeIdSingleTest);

		logger.info("*** Step 2 Expected Results: - 'Create Option' screen appear");
		assertTrue(isElementPresent(testCode.createScreen(), 5), "        Create Screen should be available.");

		logger.info("*** Step 3 Action: - Choose Single Test option");
		assertTrue(isElementPresent(testCode.singleTestRadio(), 5), "        Single test radio button must be displayed");
		selectCheckBox(testCode.singleTestRadio());
		assertTrue(isElementPresent(testCode.createOptionOKBtn(), 5), "        Ok button must be displayed");
		testCode.clickCreateOption();

		logger.info("*** Step 3 Expected Results: - Test Code Information (Single Test) appear");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single page test label must be displayed");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		assertTrue(isElementPresent(testCode.effectiveDatelabel(), 5), "        Effective Date should be available.");		

		logger.info("*** Step 4 Action: - Enter Name and Effective Date");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Testname input must be displayed");
		testCode.setTestName(testName);
		assertTrue(isElementPresent(testCode.addEffDate(), 5), "        Effective date must be displayed");
		String curdate = timeStamp.getCurrentDate();
		testCode.setEffectiveDate(curdate);

		logger.info("*** Step 4 Expected Result: - Verify that tab list is displayed");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedures tab should be displayed");

		logger.info("*** Step 5 Action: - Add procedures");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5), "        Add procedure button must be displayed");
		testCode.clickAddProcedureBtn();
		List<String> procedureCode = daoManagerXifinRpm.getProcedureCodeId(testDb);
		assertTrue(isElementPresent(testCode.procedureCodeInput(), 5), "        Procedure code input must be displayed");
		testCode.inputProcedureCode(procedureCode.get(0));
		testCode.selectProcedureTable(procedureCode.get(1));
		testCode.clickOkProcedureBtn();

		logger.info("*** Step 5 Expected Result: - Verify that procedure code must be added");
		waitUntilElementIsNotVisible(testCode.procedureTable(), 5);
		assertTrue(isElementPresent(testCode.procedureTable(), 5), "        Procedure table must be displayed");
		assertTrue(getColumnValue(testCode.procedureTable(), procedureCode.get(1)), "        Procedures must be added in table");

		logger.info("*** Step 6 Action: - Add Facilities");
		assertTrue(isElementPresent(testCode.facilityTab(), 5), "        Facility tab must be displayed");
		testCode.clickFacilityTab();
		assertTrue(isElementPresent(testCode.addFacilityBtn(), 5), "        Add facility button must be displayed");
		testCode.clickAddFacilityBtn();
		String facility = testCode.selectPerformingFacility(2);
		String minPrice = randomCharacter.getRandomNumericString(2);
		String labCost  = minPrice;
		assertTrue(isElementPresent(testCode.labCostInput(), 5), "        Lab cost input must be displayed");
		testCode.inputLabCost(labCost);
		assertTrue(isElementPresent(testCode.minPriceInput(), 5), "        Min price input must be displayed");
		testCode.inputMinPrice(minPrice);
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        OK button must be displayed");
		testCode.clickOkFacilityBtn();

		logger.info("*** Step 6 Expected Result: - Verify that facility code must be added");
		assertTrue(isElementPresent(testCode.facilityTable(), 5), "        Facility table must be displayed");
		assertTrue(getColumnValue(testCode.facilityTable(), facility), "        Facility must be added in table");

		logger.info("*** Step 7 Action: - Select Department");
		Dept depInfo =depDao.getDepartment();
		assertTrue(isElementPresent(testCode.selectDeps(), 5), "        Select department must be displayed");
		selectItemByVal(testCode.selectDeps(), String.valueOf(depInfo.getDeptId()));

		logger.info("*** Step 7 Expected Result : - Save and clear button must be available");
		assertTrue(isElementPresent(testCode.saveAndClearButton(), 5), "       Save and clear button must be available");

		logger.info("*** Step 8 Action: - Save all");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 8 Expected Result : - Landing page load test code must be present");
		assertTrue(isElementPresent(testCode.testIdInput(), 10), "       Landing page load test code must be present");

		logger.info("*** Step 9 Actions: - Input new TestID to create testID (Profile Test)");
		testCode.checkInputTestCodeIdWithNewTestID(String.valueOf(System.currentTimeMillis()));

		logger.info("*** Step 9 Expected Results: - Verify that New Record screen is displayed");
		assertTrue(isElementPresent(testCode.profileTestRadio(), 5), "       Create Options popup should be displayed");

		logger.info("*** Step 10 Actions: - Click on Profile radio button and submit");
		assertTrue(isElementPresent(testCode.profileTestRadio(), 5), "        Profile test radio button must be displayed");
		selectCheckBox(testCode.profileTestRadio());
		assertTrue(isElementPresent(testCode.createOptionOKBtn(), 5), "        Ok button must be displayed");
		testCode.clickCreateOption();

		logger.info("*** Step 10 Expected Result: - Verify that it's display New Profile page");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test page label test must be displayed");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "       Test label should be Profile");

		logger.info("*** Step 11 Actions: - Input Test Name and effective date");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Testname input must be displayed");
		testCode.setTestName(testName);
		assertTrue(isElementPresent(testCode.addEffDate(), 5), "        Effective date input must be displayed");
		testCode.setEffectiveDate(testCodeUtils.getPreviousYear(curdate));

		logger.info("*** Step 11 Expected Result: - Verify that tab list is displayed");
		assertTrue(isElementPresent(testCode.componentTab(), 5), "        Tab table should be displayed");

		logger.info("*** Step 12 Actions: Add New Component with Existing valid testId");
		assertTrue(isElementPresent(testCode.addComponent(), 5), "        Add button must be displayed");
		testCode.clickAddComponent();

		logger.info("*** Step 12 Expected Result: Add Record popup should be display");
		assertTrue(isElementPresent(testCode.testID(),5), "        Add Record Popup should be display");

		logger.info("*** Step 13 Action: Add new component with single test created in step 2");
		assertTrue(isElementPresent(testCode.testID(), 5),"        testId input field should show");
		testCode.setComponentTestId(testCodeIdSingleTest);

		logger.info("*** Step 13 Expected Result: Test Name is displayed");
		assertTrue(isElementPresent(testCode.testName(), 5), "        Test name input must be displayed");
		assertTrue(testCode.testName().getAttribute("value").equalsIgnoreCase(testName), "        Test Name should be match will database");

		logger.info("*** Step 14 Action: Click on Ok button");
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button must be displayed");
		testCode.clickOkCompBtn();

		logger.info("*** Step 14 Expected Result: new TestId will be display in the table");
		assertTrue(getColumnValue(testCode.componentTable(), testName), "        Test should be added in component table");

		logger.info("*** Step 15 Action: - Save all");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and clear button must be displayed");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 15 Expected Result : - Error must be present alert component eff date must be older than Profile eff date");
		assertTrue(isElementPresent(testCode.errorListPanel(), 5), "       Error must be present alert component eff date must be older than Profile eff date");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Add new Eff date with invalid format date")
	public void testXPR_228() throws Exception {
		logger.info("===== Testing - testXPR_228 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Profile) and Tab out");
		String profileTest ="00065043";
		testCode.checkInputTestCodeId(profileTest);

		logger.info("*** Step 2 Expected Results: - Profile test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Profile)"),"        Test Label should be Profile.");
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5),"        Create eff date icon should be available.");

		logger.info("*** Step 3 Action: - Input invalid format effective date");
		String effDateInput = timeStamp.getCurrentDate("yyyy-dd/MM");
		testCode.clickCreateEffDateIcon();
		testCode.setEffectiveDate(effDateInput);

		logger.info("*** Step 3 Expected Results: - Message warning dialog appear");
		assertTrue(isElementPresent(testCode.messageCreEffDate(), 5), "        Message warning dialog appear");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Effective Date - Add new effective date is existing in the list")
	public void testXPR_229() throws Exception{
		logger.info("===== Testing - testXPR_229 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		String profileTestCodeId = "000668EX";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(profileTestCodeId);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single Test page label must be displayed");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Profile)"), "        Test Label should be Profile.");

		logger.info("*** Step 3 Action: - Click on Pencil Icon");
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date input must be displayed");
		String effDate = testCode.currEffDate().getAttribute("value").trim();
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Effective Date icon must be displayed");
		testCode.clickCreateEffDateIcon();

		logger.info("*** Step 3 Expected Results: - Effective date field Will be displayed");
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"          Effective date field should be displayed");

		logger.info("*** Step 4 Action: - Input valid Effective Date");
		testCode.setEffectiveDate(effDate);

		logger.info("*** Step 4 Expected Results: - The error message will be displayed");
		assertTrue(isElementPresent(testCode.messageContent(), 5),"          error message field should be displayed ");
		assertTrue(testCodeUtils.getTextValue(testCode.messageContent(),5).trim().contains("Effective Date currently exists, please try again."),"       error message field should be displayed : Effective Date currently exists, please try again.");

		assertTrue(isElementPresent(testCode.closeErrorMessage(), 5),"           Close error message field should be displayed ");
		testCode.clickcloseErrorMessage();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Effective Date - Verify New Record is not save when user re-select eff date")
	public void testXPR_240() throws Exception{
		logger.info("===== Testing - testXPR_240 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		String singleTestCodeId = "BZJ964";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeId);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test label must be displayed");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click on Pencil Icon");
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date must be displayed");
		String effDate = testCode.currEffDate().getAttribute("value").trim();
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

		logger.info("*** Step 5 Action: - Select valid Effective Date and click cancel button on Copy effective date popup");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 6 Action: - Add procedures");
		testCode.clickProcedureTab();
		testCode.clickAddProcedureBtn();
		List<String> procedureCode = daoManagerXifinRpm.getProcCodeInfoFromPROCCDByTestAbbrev(singleTestCodeId, testDb);
		testCode.inputProcedureCode(procedureCode.get(0));
		testCode.selectProcedureTable(procedureCode.get(1));
		testCode.inputProcedureExpDate(testCodeUtils.getNextDay(newEffDate));
		testCode.clickOkProcedureBtn();

		logger.info("*** Step 6 Expected Result: - Verify that procedure code must be added");
		waitUntilElementIsNotVisible(testCode.procedureTable(), 5);
		assertTrue(isElementPresent(testCode.procedureTable(), 5), "        Procedure Table must be displayed");
		assertTrue(getColumnValue(testCode.procedureTable(), procedureCode.get(1)), "        Procedures must be added in table");

		logger.info("*** Step 7 Action: - Click on Pencil Icon");
		testCode.clickCreateEffDateIcon();

		logger.info("*** Step 7 Expected Results: - Effective date field Will be displayed");
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"          Effective date field should be displayed");

		logger.info("*** Step 8 Action: - Input valid Effective Date");
		testCode.setEffectiveDate(testCodeUtils.getNextYear(effDate));

		logger.info("*** Step 8 Expected Results: - The Warning message popup is displayed");
		assertTrue(isElementPresent(testCode.warningDialog(), 5),"          The Warning message pop up should be displayed");

		logger.info("*** Step 9 Action: - Click Ok Button on Warning Dialog");
		assertTrue(isElementPresent(testCode.warningOkBtnDialog(), 5), "        Ok button on warning dialog must be displayed");
		testCode.clickWarningOkBtbDialog();

		logger.info("*** Step 9 Expected Results: - The Copy Effective Date pop up is display");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5),"          The Copy Effective Date pop up should be display");

		logger.info("*** Step 10 Action: - Click Cancel Button on Copy Effective Dialog");
		assertTrue(isElementPresent(testCode.cancelEffCopyDialog(), 5), "        Cancel button Effective date dialog must be displayed");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 10 Expected Results: - All field in tab list is empty");
		assertTrue(isElementPresent(testCode.procedureTable(), 5), "      Procedure table must be displayed");
		assertEquals(getTableTotalRowSize(testCode.procedureTable()), 1,"         All fields in tab list should be empty");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Verify new eff isn't save when user without input data in tab")
	public void testXPR_241() throws Exception {
		logger.info("===== Testing - testXPR_241 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String singleTest = "TS431DS";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5),"        Create eff date icon should be available.");

		logger.info("*** Step 3 Action: - Input valid format effective date");
		String minEffDate = testCodeUtils.getLastItemDropdown(testCode.selectEffDate());
		String effDateInput = testCodeUtils.getPreviousYear(minEffDate); // Always input eff date less than existing eff date. 
		testCode.clickCreateEffDateIcon();
		testCode.setEffectiveDate(effDateInput);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");

		logger.info("*** Step 4 Action: - Cancel copy Effective Date");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Expected Results: - Copy effective date dialog closed and all fields reset to empty");
		assertTrue(isElementHidden(testCode.effCopyDialog(), 5), "        Copy effective date dialog closed");
		assertEquals(testCodeUtils.getTextSelectedInDropdown(testCode.selectDeps()).trim(), "", "        Department must be reset");
		assertEquals(testCode.expDateInput().getText().trim(), "", "        Exp date must be reset");

		logger.info("*** Step 5 Action: - Reselect another effective date");
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date input must be displayed");
		selectItem(testCode.selectEffDate(), minEffDate);

		logger.info("*** Step 5 Expected Results: - New effective date not save to effective date list");
		assertFalse(testCodeUtils.checkTextInDropdown(testCode.selectEffDate(), effDateInput, true), "        New Eff date should not save to effective date list");

		testCode.clickResetBtn();
	}

	// Procedure Tab
	@Test(priority=1, description="Single Test Detail - Procedures - Add valid Procedures")
	public void testXPR_49() throws Exception{
		logger.info("===== Testing - testXPR_49 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		String singleTestId = "TESTXPR_49";
		testCode.createSingleTest(singleTestId, testDb,false,false,this);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test code page must be displayed");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click on Pencil Icon");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Select Effective date must be displayed");
		String effDate = testCode.currEffDate().getAttribute("value").trim();
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Effective date icon must be displayed");
		testCode.clickCreateEffDateIcon();

		logger.info("*** Step 3 Expected Results: - Effective date field Will be displayed");
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"          Effective date field should be displayed");

		logger.info("*** Step 4 Action: - Input valid Effective Date");
		String newEffDate = testCodeUtils.getNextDay(effDate);
		testCode.setEffectiveDate(newEffDate);

		logger.info("*** Step 4 Expected Results: - Effective Data pop up is displayed and click cancel button on Copy effective date pop up");
		assertTrue(isElementPresent(testCode.copyEffDt(), 5),"        Effective Data pop up should be displayed");
		assertTrue(isElementPresent(testCode.cancelEffCopyDialog(), 5),"        Effective Data pop up should be displayed");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 5 Action: - Add procedures");
		List<List<String>> procedureInfo = testCode.addMultiProcedures(1, this, testCode, testDb);

		logger.info("*** Step 5 Expected Result: - Verify that procedure code must be added");
		assertTrue(isElementPresent(testCode.procedureTable(), 5), "        Procedure table must be displayed");
		assertTrue(getColumnValue(testCode.procedureTable(), procedureInfo.get(0).get(0)), "        Procedures must be added in table");

		logger.info("*** Step 6 Action: - Add Facilities");
		String facility = testCode.addNewFacility(testCode.facilityTable(), this);

		logger.info("*** Step 6 Expected Result: - Verify that Facility must be added");
		assertTrue(isElementPresent(testCode.facilityTable(), 5), "        Facility table must be displayed");
		assertTrue(getColumnValue(testCode.facilityTable(), facility), "        Facility must be added in table");

		logger.info("*** Step 7 Action: - Select Department");
		Dept depInfo = depDao.getDepartment();
		assertTrue(isElementPresent(testCode.selectDeps(), 5), "        Select department must be displayed");
		selectItemByVal(testCode.selectDeps(), String.valueOf(depInfo.getDeptId()));

		logger.info("*** Step 7 Expected Result : - Save and clear button must be available");
		assertTrue(isElementPresent(testCode.saveAndClearButton(), 5), "       Save and clear button must be available");

		logger.info("*** Step 8 Action: - Click save and clear button");
		testCode.clickSaveAndClearBtn();
		wait.until(ExpectedConditions.invisibilityOf(testCode.saveInProgressInfoText()));

		logger.info("*** Step 8 Expected Result: - New Procedure code with corresponding testID is saved into Database ");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestId);
		assertTrue(isElementPresent(testCode.procedureTab(), 5),"        Procedure tab should show");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.procedureTable(), 5), "        Procedure table must be displayed");
		assertTrue(getColumnValue(testCode.procedureTable(), procedureInfo.get(0).get(0)), "        New Procedure code with corresponding testID is saved into Database ");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Procedures - Proc table and name are populated")
	public void testXPR_52() throws Exception {
		logger.info("===== Testing - testXPR_52 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		procCodeSearch = new ProcCodeSearch(driver);
		procCodeSearchResults = new ProcCodeSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String singleTest = "QQO478";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Create new Effective Date");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date must be displayed");
		String minEffDate = testCodeUtils.getLastItemDropdown(testCode.selectEffDate());
		String effDateInput = testCodeUtils.getPreviousYear(minEffDate);
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Effective date icon must be displayed");
		testCode.clickCreateEffDateIcon();
		testCode.setEffectiveDate(effDateInput);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");

		logger.info("*** Step 4 Action: - Cancel copy Effective Date");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Expected Results: - Copy effective date dialog closed and all fields reset to empty");
		assertTrue(isElementHidden(testCode.effCopyDialog(), 5), "        Copy effective date dialog closed");

		logger.info("*** Step 5 Action: - Add procedures");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5), "        Add procedure button must be displayed");
		testCode.clickAddProcedureBtn();

		logger.info("*** Step 5 Expected Results: - Add dialog appear");
		assertTrue(isElementPresent(testCode.procedureCodeInput(), 5), "        Procedure code input must be available");

		logger.info("*** Step 6 Action: Open search procedure popup");
		testCode.clickSearchProcedureIcon();
		String parentWindow = switchToPopupWin();

		logger.info("*** Step 6 Expected Results: - Search popup appear");
		assertTrue(isElementPresent(procCodeSearch.searchProcedureIdInput(), 5), "        Procedure code search input must be available");
		assertTrue(isElementPresent(procCodeSearch.searchButtonInSearchScreen(), 5), "        Search button must be available");

		logger.info("*** Step 7 Action: Search all procedures and click on first result search");
		procCodeSearch.searchProcedureCode("*");
		String procId = procCodeSearchResults.procedureIdLinkTestCodeSearchResults(2,2).getText();
		procCodeSearchResults.clickOnSearchResultsProcedureIdLink(2, 2);
		switchToParentWin(parentWindow);

		logger.info("*** Step 7 Expected Results: Procedure code search present in Add procedure dialog");
		assertTrue(testCode.procedureCodeInput().getAttribute("value").equalsIgnoreCase(procId), "        Procedure code search must put in Add procedure dialog");

		logger.info("*** Step 8 Action: Close add procedure dialog and finish add");
		testCode.clickOkProcedureBtn();

		logger.info("*** Step 8 Expected Results: - Procedures add to table");
		waitUntilElementIsNotVisible(testCode.procedureTable(), 5);
		assertTrue(getColumnValue(testCode.procedureTable(), procId), "        Procedures must be added in table");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Procedures - Verify Proc Name is removed when remove Proc Code")
	public void testXPR_54() throws Exception {
		logger.info("===== Testing - testXPR_54 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		procCodeSearch = new ProcCodeSearch(driver);
		procCodeSearchResults = new ProcCodeSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String singleTest = "VXT606";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Add procedures");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();
		testCodeUtils.scrollIntoView(testCode.addProcedureBtn());
		testCode.clickAddProcedureBtn();

		logger.info("*** Step 3 Expected Results: - Add dialog appear");
		assertTrue(isElementPresent(testCode.procedureCodeInput(), 5), "        Procedure code input must be available");

		logger.info("*** Step 4 Action: Open search procedure popup");
		assertTrue(isElementPresent(testCode.searchProcedureIcon(), 5), "        Procedure search icon must be displayed");
		testCode.clickSearchProcedureIcon();
		String parentWindow = driver.getWindowHandle();
		switchToPopupWin();

		logger.info("*** Step 4 Expected Results: - Search popup appear");
		assertTrue(isElementPresent(procCodeSearch.searchProcedureIdInput(), 5), "        Procedure code search input must be available");
		assertTrue(isElementPresent(procCodeSearch.searchButtonInSearchScreen(), 5), "        Search button must be available");

		logger.info("*** Step 5 Action: Search all procedures and click on first result search");
		procCodeSearch.searchProcedureCode("*");
		String procId = procCodeSearchResults.procedureIdLinkTestCodeSearchResults(2,2).getText();
		procCodeSearchResults.clickOnSearchResultsProcedureIdLink(2, 2);
		switchToParentWin(parentWindow);

		logger.info("*** Step 5 Expected Results: Procedure code search present in Add procedure dialog");
		assertTrue(testCode.procedureCodeInput().getAttribute("value").equalsIgnoreCase(procId), "        Procedure code search must put in Add procedure dialog");

		logger.info("*** Step 6 Action: Remove proc code in proc code input");
		assertTrue(isElementPresent(testCode.procedureCodeInput(), 5), "       Procedure code input must be displayed");
		testCode.clearInputProcedureCode();

		logger.info("*** Step 6 Expected Results: - Proc Name is removed when remove Proc Code");
		assertEquals(testCode.procedureCodeName().getAttribute("value"),"", "        Proc Name must be removed when remove Proc Code");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Procedures - Add procedures some required fields are empty")
	public void testXPR_55() throws Exception {
		logger.info("===== Testing - testXPR_55=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
		String TestID = "1624061502352";
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");

		logger.info("*** Step 3 Action: - Input valid effective date");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date input must be displayed");
		String minEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		String effDateInput = testCodeUtils.getNextDay(minEffDate); 
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Effective date icon must be displayed");
		testCode.clickCreateEffDateIcon();
		assertTrue(isElementPresent(testCode.addEffDate(), 5), "        Effective date input must be displayed");
		testCode.setEffectiveDate(effDateInput);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Action: - Click 'add' icon on Procedures tab");
		assertTrue(isElementPresent(testCode.procedureTab(),5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(),5), "        Procedure add button must be displayed");
		testCode.clickAddProcedureBtn();

		logger.info("*** Step 4 Expected Results: - Add record popup is displayed");		
		assertTrue(isElementPresent(testCode.addRecordProcedurePopUp(), 5), "        Add record popup appear");

		logger.info("*** Step 5 Action: - Select procedure table and leave procedure code as blank");
		List<String> procedureCode = daoManagerXifinRpm.getProcCodeInfoFromPROCCDByTestAbbrev(TestID, testDb);
		assertTrue(isElementPresent(testCode.procedureTableType(),5));		
		testCode.selectProcedureTable(procedureCode.get(1));
		testCode.clickOkProcedureBtn();

		logger.info("*** Step 5 Expected Result: - Error message will display");
		assertTrue(testCode.procedureDialogFormError().getText().trim().contains("Procedure Code: Field is required"),"        Error message should be 'Procedure Code: Field is required'.");

		logger.info("*** Step 6 Action: - Select procedure code and leave procedure table as blank");
		assertTrue(isElementPresent(testCode.procedureCodeInput(), 5), "        procedure Code input must be displayed");
		testCode.inputProcedureCode(procedureCode.get(0));
		assertTrue(isElementPresent(testCode.procedureTableType(), 5), "        Procedure table type must be displayed");
		testCode.selectProcedureTable("");
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button must be displayed");
		testCode.clickOkProcedureBtn();

		logger.info("*** Step 6 Expected Result: - Error message will display");
		assertTrue(testCodeUtils.getTextValue(testCode.procedureDialogFormError(),5).trim().contains("Procedure Table: Field is required"),"        Error message should be 'Procedure Table: Field is required'.");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Procedures - Cancel Add Procedure")
	public void testXPR_56() throws Exception {
		logger.info("===== Testing - testXPR_56=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> singleTestID = daoManagerXifinRpm.getSingleTest(testDb);
		String TestID = "LR043PK";
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");

		logger.info("*** Step 3 Action: - Input valid effective date");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date input must be displayed");
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date input must be displayed");
		String minEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		String effDateInput = testCodeUtils.getNextDay(minEffDate); 
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Effective date icon must be displayed");
		testCode.clickCreateEffDateIcon();
		assertTrue(isElementPresent(testCode.addEffDate(), 5), "        Effective date input must be displayed");
		testCode.setEffectiveDate(effDateInput);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.cancelEffCopyDialog(), 5), "        Cancel Effective Copy Dialog must be displayed");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Action: - Click 'add' icon on Procedures tab");
		assertTrue(isElementPresent(testCode.procedureTab(),5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(),5), "        Procedure add button must be displayed");
		testCode.clickAddProcedureBtn();

		logger.info("*** Step 4 Expected Results: - Add record popup is displayed");		
		assertTrue(isElementPresent(testCode.addRecordProcedurePopUp(), 5), "        Add record popup appear");

		logger.info("*** Step 5 Action: - Fill all fields with valid data and click Cancel button");
		List<String> procedureCode = daoManagerXifinRpm.getProcCodeInfoFromPROCCDByTestAbbrev(TestID, testDb);
		assertTrue(isElementPresent(testCode.procedureTableType(),5));	
		String procTable = procedureCode.get(1);
		String procCode = procedureCode.get(0);
		testCode.selectProcedureTable(procTable);
		assertTrue(isElementPresent(testCode.procedureCodeInput(),5));	
		testCode.inputProcedureCode(procCode);
		assertTrue(isElementPresent(testCode.cancelBtn(), 5), "        Cancel Procedure button must be displayed");
		testCode.clickCancelProcedureBtn();

		logger.info("*** Step 5 Expected Result: - New record will be not created");
		assertFalse(getColumnValue(testCode.procedureTable(), procCode), "        Procedures must not added in table");
		assertFalse(getColumnValue(testCode.procedureTable(), procTable), "        Procedures must not added in table");
		
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Procedures - Add Procedure with invalid Exp date format")
	public void testXPR_57() throws Exception{
		logger.info("===== Testing - testXPR_57 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
//		String singleTestCodeId = daoManagerXifinRpm.getSingleTest(testDb).get(1);
		String singleTestCodeId ="TU278KI";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeId);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test page label must be displayed");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click on Pencil Icon");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date input must be displayed");
		String effDate = testCode.currEffDate().getAttribute("value").trim();
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Effective date icon must be displayed");
		testCode.clickCreateEffDateIcon();

		logger.info("*** Step 3 Expected Results: - Effective date field Will be displayed");
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"          Effective date field should be displayed");

		logger.info("*** Step 4 Action: - Input valid Effective Date");
		String newEffDate = testCodeUtils.getNextDay(effDate);
		testCode.setEffectiveDate(newEffDate);

		logger.info("*** Step 4 Expected Results: - Effective Data pop up is displayed - Select valid Effective Date and click cancel button on Copy effective date pop up");
		assertTrue(isElementPresent(testCode.copyEffDt(), 5),"        Effective Data pop up should be displayed");
		assertTrue(isElementPresent(testCode.cancelEffCopyDialog(), 5),"        Effective Data pop up should be displayed");
		String copyEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.copyEffDt());
		selectItem(testCode.copyEffDt(), copyEffDate);
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 5 Action: - Click Add procedures");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5), "        Add procedure button must be displayed");
		testCode.clickAddProcedureBtn();

		logger.info("*** Step 5 Expected Results: - The Add new record pop up will be displayed");
		assertEquals(testCode.addRecordProcedurePopUp().getText(), "Add Record","        The Add new record pop up should be displayed");

		logger.info("*** Step 6 Action: - Enter Invalid Expiration Date and click ok button");
		List<String> procedureCode = daoManagerXifinRpm.getProcCodeInfoFromPROCCDByTestAbbrev(singleTestCodeId, testDb);
		testCode.selectProcedureTable(procedureCode.get(1));
		assertTrue(isElementPresent(testCode.procedureCodeInput(), 5), "        Procedure code input must be displayed");
		testCode.inputProcedureCode(procedureCode.get(0));
		assertTrue(isElementPresent(testCode.procedureExpDateInput(), 5), "        Procedure Expiration Date input must be displayed");
		testCode.inputProcedureExpDate("2015/05/12");
		assertTrue(isElementPresent(testCode.closeErrorMessage(), 5), "        Close error message button must be displayed");
		testCode.clickcloseErrorMessage();
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button must be displayed");
		testCode.clickOkProcedureBtn();

		logger.info("*** Step 6 Expected Results: - The error message should be displayed");
		assertTrue(isElementPresent(testCode.closeErrorMessage(), 5), "        Close error message button must be displayed");
		testCode.clickcloseErrorMessage();
		testCode.procedureExpDateInput().clear();
		testCode.procedureExpDateInput().sendKeys(Keys.TAB);

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Procedures - Updated procedure with valid input")
	public void testXPR_59() throws Exception{
		logger.info("===== Testing - testXPR_59 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		String singleTestId = "TESTXPR_59";
		testCode.createSingleTest(singleTestId, testDb,false,false,this);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test page label must be displayed");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click on any Row in Procedure Table and click on edit record");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.procedureTableInProcedureTable(), 5), "        Procedure table must be displayed");
		String procedureTable = testCode.procedureTableInProcedureTable().getText();
		assertTrue(isElementPresent(testCode.procedureCodeIdInProcedureTable(), 5), "        Procedure Code must be displayed");
		String procedureCode = testCode.procedureCodeIdInProcedureTable().getText();
		selectColumnValue(testCode.procedureTable(), procedureTable);

		logger.info("*** Step 3 Expected Results: - Edit Record popup is displayed");
		assertTrue(isElementPresent(testCode.editRecordProcedurePopUp(), 5),"         Edit Record popup should be displayed");

		logger.info("*** Step 4 Action: - Update Procedure with valid data and click save and clear button ");
		String newProCodeId = testCode.getNewProcCodeIdInDb(this, procedureCode, procedureTable, testDb);
		assertTrue(isElementPresent(testCode.procedureCodeInput(), 5), "        Procedure code input must be displayed");
		testCode.inputProcedureCode(newProCodeId);
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button must be displayed");
		testCode.clickOkProcedureBtn();
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and clear button must be displayed");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 4 Expected Results: - Updated Information is save in DB");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
		testCode.checkInputTestCodeId(singleTestId);
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		assertTrue(isElementPresent(testCode.procedureTab(), 5),"        Procedure  tab should show");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.procedureTable(), 5),"        Procedure  Table should show");
		assertTrue(getColumnValue(testCode.procedureTable(), newProCodeId),"        Updated Information is save in DB");
		
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Procedures - Verify procedure table icon")
	public void testXPR_60() throws Exception {
		logger.info("===== Testing - testXPR_60 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest="PQB451";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Hide procedure table");
		assertTrue(isElementPresent(testCode.hideShowProcedureTableIcon(), 5), "        Hide/Show procedure button must be displayed");
		testCode.clickHideShowProcedureTableIcon();

		logger.info("*** Step 3 Expected Results: - Procedure table must be hide");
		assertTrue(isElementHidden(testCode.wrapperOfProcedureTable(), 5), "        Procedure table must be hide");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Procedures - Filter procedure base on procedure table")
	public void testXPR_92() throws Exception {
		logger.info("===== Testing - testXPR_92 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest ="1592042904577";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Add procedures and filter procedure table");
		List<List<String>> procedureAdded = testCode.addMultiProcedures(2, this, testCode, testDb, singleTest);
		String procTable =  procedureAdded.get(1).get(1);
		testCode.inputProcTableFilter(procTable);

		logger.info("*** Step 3 Expected Results: - Verify filter procedure match with procedure table");
		assertEquals(getTableTotalRowSize(testCode.procedureTable()),2,"        Search result should be empty.");
		assertTrue(getColumnValue(testCode.procedureTable(), procTable), "        Procedure table only show filter procedure");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Procedures - Verify system display all list when user un-filter")
	public void testXPR_93() throws Exception {
		logger.info("===== Testing - testXPR_93=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> singleTestID = daoManagerXifinRpm.getSingleTest(testDb);
		String TestID = "IV202GD";
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");

		logger.info("*** Step 3 Action: - Add procedures with valid data");
		assertTrue(isElementPresent(testCode.procedureTab(),5), "        Procedure tab must be displayed");
		List<List<String>> procedureAdded = testCode.addMultiProcedures(2, this , testCode, testDb, TestID);
		String procTable =  procedureAdded.get(1).get(1);
		int totalRow = getTableTotalRowSize(testCode.procedureTable());

		logger.info("*** Step 3 Action: - Filter procedure table");
		assertTrue(isElementPresent(testCode.procTableFilter(), 5), "        Procedure table filter must be displayed");
		testCode.inputProcTableFilter(procTable);

		logger.info("*** Step 3 Expected Results: - Verify filter procedure match with procedure table");
		assertEquals(getTableTotalRowSize(testCode.procedureTable()),2,"        Search result should be empty.");
		assertTrue(getColumnValue(testCode.procedureTable(), procTable), "        Procedure table only show filter procedure");

		logger.info("*** Step 4 Action: - Remove the procedure table that has just inputted");
		testCode.clearData(testCode.procTableFilter());

		logger.info("*** Step 4 Expected Result: - Table should be displayed all data.");
		assertEquals(getTableTotalRowSize(testCode.procedureTable()),totalRow,"        Table should be displayed all data.");
		
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Procedures - Filter procedure grid with select multi filter options")
	public void testXPR_94() throws Exception {
		logger.info("===== Testing - testXPR_94=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> singleTestID = daoManagerXifinRpm.getSingleTest(testDb);
		String TestID = "QB623SV";
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");

		logger.info("*** Step 3 Action: - Add procedures and filter procedure table");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5), "        Add procedure button must be displayed");
		List<String> proc = testCode.addProcedure(this, testCode, "",testDb, TestID);
		String procCode  =  proc.get(0);
		String procTable =  proc.get(1);
		String procName  =  proc.get(2);

		assertTrue(isElementPresent(testCode.procTableFilter(), 5), "        Procedure table filter must be displayed");
		testCode.inputProcTableFilter(procTable);

		assertTrue(isElementPresent(testCode.procCodeFilter(), 5), "      Procedure code filter must be displayed");
		testCode.setProcCodeFilter(procCode);

		assertTrue(isElementPresent(testCode.procNameFilter(), 5), "        Procedure name filter must be displayed");
		testCode.setProcNameFilter(procName);

		logger.info("*** Step 3 Expected Results: - Verify filter procedure match with procedure table");
		assertTrue(getColumnValue(testCode.procedureTable(), procTable), "        Procedure table should be displayed.");
		assertTrue(getColumnValue(testCode.procedureTable(), procCode), "        Procedure code should be displayed.");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Procedures - Sort Procedure Table as per Procedure Name")
	public void testXPR_95() throws Exception{
		logger.info("===== Testing - testXPR_95 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
//		String singleTestCodeId = daoManagerXifinRpm.getSingleTest(testDb).get(1);
		String singleTestCodeId ="EZ230QK";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeId);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test page label must be displayed");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click on Pencil Icon");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date input must be displayed");
		String effDate = testCode.currEffDate().getAttribute("value").trim();
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Effective date icon must be displayed");
		testCode.clickCreateEffDateIcon();

		logger.info("*** Step 3 Expected Results: - Effective date field Will be displayed");
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"          Effective date field should be displayed");

		logger.info("*** Step 4 Action: - Input valid Effective Date");
		String newEffDate = testCodeUtils.getNextDay(effDate);
		assertTrue(isElementPresent(testCode.addEffDate(), 5), "        Effective date input must be displayed");
		testCode.setEffectiveDate(newEffDate);

		logger.info("*** Step 4 Expected Results: - Effective Data popup is displayed");
		assertTrue(isElementPresent(testCode.copyEffDt(), 5),"        Effective Data pop up should be displayed");
		assertTrue(isElementPresent(testCode.cancelEffCopyDialog(), 5),"        Effective Data pop up should be displayed");

		logger.info("*** Step 5 Action: - Select valid Effective Date and click cancel button on Copy effective date pop up");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 5 Action: - Add some procedure with Valid Data");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5), "        Add procedure button must be displayed");
		testCode.addMultiProcedures(2, this, testCode, testDb, singleTestCodeId);

		logger.info("*** Step 5 Action: - Click on Name Column");
		assertTrue(isElementPresent(testCode.sortByNameOnProcedureTable(), 5), "          Name column header must be displayed");
		testCode.clicksortNameOnProcedureTable();

		logger.info("*** Step 5 Expected Results: - Procedure List is sorted as alphabet as per procedure Name");
		assertTrue(getSortingComparisonOnTable(testCode.procedureTable(),5, "asc"));

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Procedures - update Procedure without select any row")
	public void testXPR_159() throws Exception{
		logger.info("===== Testing - testXPR_159 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
//		String singleTestCodeId = daoManagerXifinRpm.getSingleTest(testDb).get(1);
		String singleTestCodeId ="NE108WZ";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeId);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test page label must be displayed");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click on Pencil Icon");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date input must be displayed");
		String effDate = testCode.currEffDate().getAttribute("value").trim();
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Effective date icon must be displayed");
		testCode.clickCreateEffDateIcon();

		logger.info("*** Step 3 Expected Results: - Effective date field Will be displayed");
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"          Effective date field should be displayed");

		logger.info("*** Step 4 Action: - Input valid Effective Date");
		String newEffDate = testCodeUtils.getNextDay(effDate);
		testCode.setEffectiveDate(newEffDate);

		logger.info("*** Step 4 Expected Results: - Effective Data popup is displayed");
		assertTrue(isElementPresent(testCode.copyEffDt(), 5),"        Effective Data pop up should be displayed");
		assertTrue(isElementPresent(testCode.cancelEffCopyDialog(), 5),"        Effective Data pop up should be displayed");

		logger.info("*** Step 5 Action: - Select valid Effective Date and click cancel button on Copy effective date pop up");
		String copyEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.copyEffDt());
		selectItem(testCode.copyEffDt(), copyEffDate);
		testCode.clickCancelEffDateCopyDialog();
		
		logger.info("*** Step 5 Action: - Add some procedure with Valid Data");
		testCode.addMultiProcedures(2, this, testCode, testDb, singleTestCodeId);

		logger.info("*** Step 5 Action: - Without Select Any Row and  Click Edit Button on Procedure Table");
		assertTrue(isElementPresent(testCode.editProcedureBtn(), 5), "        Edit procedure button must be displayed");
		testCode.clickEditProcedureBtn();

		logger.info("*** Step 5 Expected Results: - The Error message pop up is displayed");
		assertTrue(isElementPresent(testCode.alertProcedureWarning(), 5), "        Alert Procedure Warning must be displayed");
		assertTrue(testCodeUtils.getTextValue(testCode.alertProcedureWarning(),5).contains("Please, select row"),"         The Error message pop up is displayed : Please, select row");

		assertTrue(isElementPresent(testCode.WarningCloseBtn(), 5), "        Close Message error button must be displayed");
		testCode.clickWarningCloseBtn();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Procedures - Update procedure with invalid data")
	public void testXPR_162() throws Exception {
		logger.info("===== Testing - testXPR_162 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		randomCharacter = new RandomCharacter(driver);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest="26790";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Add procedures");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5), "        Add procedure button must be displayed");
		List<String> procedureAdded = testCode.addProcedure(this, testCode, "", testDb, singleTest);
		String procId = procedureAdded.get(0);

		logger.info("*** Step 3 Expected Results: - Verify added procedure in table");
		assertTrue(getColumnValue(testCode.procedureTable(), procId), "        Procedure added in table");

		logger.info("*** Step 4 Action: - Edit procedure added");
		selectColumnValue(testCode.procedureTable(),procId);

		logger.info("*** Step 4 Expected Results: - Edit dialog appear");
		assertTrue(isElementPresent(testCode.procedureCodeInput(), 5), "        Procedure code input must be available");

		logger.info("*** Step 5 Action: - Input invalid proc code");
		String invalidProcCode = "INVALID_" + randomCharacter.getRandomNumericString(4);
		testCode.inputProcedureCode(invalidProcCode);
		testCode.clickOkProcedureBtn();

		logger.info("*** Step 5 Expected Results: - Error message display to indicate Proc code not found");
		assertTrue(isElementPresent(testCode.procedureDialogFormError(), 5), "        Error message must display to indicate Proc code not found");
		assertTrue( testCodeUtils.getTextValue(testCode.procedureDialogFormError(),5).contains("Procedure Code "+invalidProcCode+" was not found"), "        Error message must display to indicate Proc code not found");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Procedures - Update procedure with required field is empty")
	public void testXPR_179() throws Exception {
		logger.info("===== Testing - testXPR_179 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest="YGQ990";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Add procedure");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5), "        Add procedure button must be displayed");
		List<String> procedureAdded = testCode.addProcedure(this, testCode, "",testDb, singleTest);
		String procId = procedureAdded.get(0);

		logger.info("*** Step 3 Expected Results: - Verify added procedure in table");
		assertTrue(getColumnValue(testCode.procedureTable(), procId), "        Procedure added in table");

		logger.info("*** Step 4 Action: - Edit procedure added");
		assertTrue(isElementPresent(testCode.procedureTableInProcedureTable(), 5), "        Procedure table must be displayed");
		testCodeUtils.scrollIntoView(testCode.procedureTableInProcedureTable());
		testCode.clickProcedureTableInProcedureTable();
		assertTrue(isElementPresent(testCode.editProcedureBtn(), 5), "        Edit procedure button must be displayed");
		testCode.clickEditProcedureBtn();

		logger.info("*** Step 4 Expected Results: - Edit dialog appear");
		assertTrue(isElementPresent(testCode.procedureCodeInput(), 5), "        Procedure code input must be available");

		logger.info("*** Step 5 Action: - Update procedure with empty proc code");
		testCode.procedureCodeInput().clear();
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button must be displayed");
		testCode.clickOkProcedureBtn();

		logger.info("*** Step 5 Expected Results: - Error message display to input required field");
		assertTrue(isElementPresent(testCode.procedureDialogFormError(), 5), "        Error message must display to input required field");
		assertTrue(testCodeUtils.getTextValue(testCode.procedureDialogFormError(),5).contains("Procedure Code: Field is required"), "        Error message must display to input required field");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Procedures - Verify filter options")
	public void testXPR_200() throws Exception {
		logger.info("===== Testing - testXPR_200=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> singleTestID = daoManagerXifinRpm.getSingleTest(testDb);
		String TestID = "1578343945546";
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");

		logger.info("*** Step 3 Action: - Add procedures with valid data");
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date input must be displayed");
		String currentEff = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());	
		String expDate = testCodeUtils.getNextYear(currentEff);	
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5), "        Add procedure button must be displayed");
		List<String> proc = testCode.addProcedure(this, testCode, expDate, testDb, TestID);
		String procCode  =  proc.get(0);
		String procTable =  proc.get(1);
		String procName  =  proc.get(2);		

		logger.info("*** Step 3 Expected Results: - New procedure code are added into procedure table");
		assertTrue(getColumnValue(testCode.procedureTable(), procCode), "        New procedure code are added into procedure table");
		
		logger.info("*** Step 4 Action: - Check function filter in procedure tab - input valid in proc table filter");
		assertTrue(isElementPresent(testCode.procTableFilter(), 5), "        Procedure table filter must be displayed");
		testCode.inputProcTableFilter(procTable);
		
		logger.info("*** Step 4 Expected Results: - System will filter proc list  base on selected proc table");
		assertTrue(getColumnValue(testCode.procedureTable(), procTable), "        System will filter proc list  base on selected proc table.");
		testCode.clearData(testCode.procTableFilter());

		logger.info("*** Step 5 Action: - Check function filter in procedure tab - input valid in proc code filter");
		assertTrue(isElementPresent(testCode.procCodeFilter(), 5), "      Procedure code filter must be displayed");
		testCode.setProcCodeFilter(procCode);
		
		logger.info("*** Step 5 Expected Results: - System will filter proc list  base on selected proc code");
		assertTrue(getColumnValue(testCode.procedureTable(), procCode), "        System will filter proc list  base on selected proc code.");
		testCode.clearData(testCode.procCodeFilter());

		logger.info("*** Step 6 Action: - Check function filter in procedure tab - input valid in name filter");
		assertTrue(isElementPresent(testCode.procNameFilter(), 5), "        Procedure name filter must be displayed");
		testCode.setProcNameFilter(procName);
		
		logger.info("*** Step 6 Expected Results: - System will filter proc list base on selected name");
		assertTrue(getColumnValue(testCode.procedureTable(), procName), "        System will filter proc list base on selected name.");
		testCode.clearData(testCode.procNameFilter());

		logger.info("*** Step 7 Action: - Check function filter in procedure tab - input valid in Exp Date filter");
		assertTrue(isElementPresent(testCode.expDateFilter(), 5), "        Expiration date filter must be displayed");
		
		logger.info("*** Step 7 Expected Results: - System will filter proc list base on selected Exp Date");
		testCode.inputExpDateFilter(expDate);
		assertTrue(getColumnValue(testCode.procedureTable(), expDate), "        System will filter proc list base on selected Exp Date.");
		
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}


	@Test(priority=1, description="Single Test Detail - Procedures - Add new with pro code invalid")
	public void testXPR_230() throws Exception{
		logger.info("===== Testing - testXPR_230 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		randomCharacter = new RandomCharacter(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
//		String singleTestCodeId = daoManagerXifinRpm.getSingleTest(testDb).get(1);
		String singleTestCodeId="CNK850";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeId);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test page label must be displayed");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click on Pencil Icon");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date input must be displayed");
		String effDate = testCode.currEffDate().getAttribute("value").trim();
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Effective date icon must be displayed");
		testCode.clickCreateEffDateIcon();

		logger.info("*** Step 3 Expected Results: - Effective date field Will be displayed");
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"          Effective date field should be displayed");

		logger.info("*** Step 4 Action: - Input valid Effective Date");
		String newEffDate = testCodeUtils.getNextDay(effDate);
		assertTrue(isElementPresent(testCode.addEffDate(), 5), "        Effective date input must be displayed");
		testCode.setEffectiveDate(newEffDate);

		logger.info("*** Step 4 Expected Results: - Effective Data pop up is displayed - Select valid Effective Date and click cancel button on Copy effective date pop up");
		assertTrue(isElementPresent(testCode.copyEffDt(), 5),"        Effective Data pop up should be displayed");
		assertTrue(isElementPresent(testCode.cancelEffCopyDialog(), 5),"        Effective Data pop up should be displayed");
		String copyEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.copyEffDt());
		selectItem(testCode.copyEffDt(), copyEffDate);
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 5 Action: - Click Add procedures");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5), "        Add procedure button must be displayed");
		testCode.clickAddProcedureBtn();

		logger.info("*** Step 5 Expected Results: - The Add new record pop up will be displayed");
		assertEquals(testCode.addRecordProcedurePopUp().getText(), "Add Record","        The Add new record pop up should be displayed");

		logger.info("*** Step 6 Action: - Input invalid Proc Code Id and click ok button");
		List<String> procedureCode = daoManagerXifinRpm.getProcCodeInfoFromPROCCDByTestAbbrev(singleTestCodeId, testDb);
		String invalidProCode = randomCharacter.getRandomNumericString(10);
		testCode.selectProcedureTable(procedureCode.get(1));
		assertTrue(isElementPresent(testCode.procedureCodeInput(), 5), "        Procedure code input must be displayed");
		testCode.inputProcedureCode(invalidProCode);
		testCode.clickOkProcedureBtn();

		logger.info("*** Step 6 Expected Results: - The Error message will be displayed");
		assertTrue(isElementPresent(testCode.proCodeErrorMessageDialog(), 5), "        Procedure Error Code Message Dialog must be displayed");
		assertTrue(testCodeUtils.getTextValue(testCode.proCodeErrorMessageDialog(),5).contains("Procedure Code "+invalidProCode+" was not found."),"        The Error message will be displayed ");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Procedures - Verify that procedure table is changed when user re-select Procedure")
	public void testXPR_233() throws Exception {
		logger.info("===== Testing - testXPR_233=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		procCodeSearch = new ProcCodeSearch(driver);
		procCodeSearchResults = new ProcCodeSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> singleTestID = daoManagerXifinRpm.getSingleTest(testDb);
		String TestID = "08406";
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");

		logger.info("*** Step 3 Action: - Click on Pencil Icon");
		testCodeUtils.scrollIntoView(testCode.testNameInput());
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date input must be displayed");
		String effDate = testCode.currEffDate().getAttribute("value").trim();
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Effective date icon must be displayed");
		testCode.clickCreateEffDateIcon();

		logger.info("*** Step 3 Expected Results: - Effective date field Will be displayed");
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"          Effective date field should be displayed");

		logger.info("*** Step 4 Action: - Input valid Effective Date");
		String newEffDate = testCodeUtils.getNextDay(effDate);
		assertTrue(isElementPresent(testCode.addEffDate(), 5), "        Effective date input must be displayed");
		testCode.setEffectiveDate(newEffDate);

		logger.info("*** Step 4 Expected Results: - Effective Data popup is displayed");
		assertTrue(isElementPresent(testCode.copyEffDt(), 5),"        Effective Data pop up should be displayed");
		assertTrue(isElementPresent(testCode.cancelEffCopyDialog(), 5),"        Effective Data pop up should be displayed");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 5 Action: - Select procedure table and click search icon button");
		assertTrue(isElementPresent(testCode.procedureTab(),5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();
		testCode.clickAddProcedureBtn();
		List<String> procedureCodeTemp = daoManagerXifinRpm.getProcCodeInfoFromPROCCDByTestAbbrev(TestID, testDb);
		List<String> procedureCodeTemp1 = daoManagerXifinRpm.getProcCodeInfoFromPROCCDByTestAbbrev(TestID, testDb);
		String procedureCodeTable1 = procedureCodeTemp1.get(1);
		String procedureCodeTable = procedureCodeTemp.get(1);
		testCode.selectProcedureTable(procedureCodeTable);
		assertTrue(isElementPresent(testCode.searchProcedureIcon(),5), "        Search procedure icon must be displayed");
		testCode.clickSearchProcedureIcon();
		String parentWin = switchToPopupWin();

		logger.info("*** Step 5 Expected Results: - Search popup should be displayed");
		assertTrue(isElementPresent(procCodeSearch.searchProcedureIdInput(),5));

		logger.info("*** Step 6 Action: - Click on Search button");
		assertTrue(isElementPresent(procCodeSearch.searchButtonInSearchScreen(),5), "        Search button in search screen must be displayed");
		procCodeSearch.clickSearchBtnInSearchScreen();		

		logger.info("*** Step 6 Expected Results: - All procedure code belong to selected proc table are display");
		assertTrue(isElementPresent(procCodeSearchResults.pagerBottomRight(),5), "        The Page right footer view should show.");
		String totalProcedureCode = testCodeUtils.getTotalResultSearch(procCodeSearchResults.rightPagerTableResultSearch());
		String totalProcDB = testCodeUtils.convertDecimalFormat(Integer.parseInt(daoManagerXifinRpm.getProcedureSearchDetail(testDb,procedureCodeTable)));
		assertEquals(totalProcedureCode, totalProcDB, "        Total TestCode should be " + totalProcDB);

		logger.info("*** Step 7 Action: - Select any procedure link ");
		String procCodeID = procCodeSearchResults.procedureIdLinkTestCodeSearchResults(2, 2).getText();
		procCodeSearchResults.clickOnSearchResultsProcedureIdLink(2, 2);
		switchToParentWin(parentWin);

		logger.info("*** Step 7 Expected Results: - Procedure code Id displayed in the field, proc procedure name is populated based on procedure code Id");
		assertEquals(procCodeID, testCode.procedureCodeInput().getAttribute("value"));

		logger.info("*** Step 8 Action: - Click on search code, re-select another procedure table and click on search icon ");
		testCode.selectProcedureTable(procedureCodeTable1);
		testCode.clickSearchProcedureIcon();
		String parentWin1 = switchToPopupWin();
		procCodeSearch.clickSearchBtnInSearchScreen();
		String procCodeId1 = procCodeSearchResults.procedureIdLinkTestCodeSearchResults(3, 2).getText();
		procCodeSearchResults.clickOnSearchResultsProcedureIdLink(3, 2);
		switchToParentWin(parentWin1);

		logger.info("*** Step 8 Expected Results: Procedure table is updated based on new selected procedure table");
		assertEquals(procCodeId1, testCode.procedureCodeInput().getAttribute("value"));

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Procedures - Add new Proc with Exp date less than Effective date")
	public void testXPR_234() throws Exception {
		logger.info("===== Testing - testXPR_234 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest ="MM578NE";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Create new Effective Date");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Effective date input must be displayed");
		String minEffDate = testCodeUtils.getLastItemDropdown(testCode.selectEffDate());
		String effDateInput = testCodeUtils.getPreviousYear(minEffDate);
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Effective date icon must be displayed");
		testCode.clickCreateEffDateIcon();
		assertTrue(isElementPresent(testCode.addEffDate(), 5), "        Effective date input must be displayed");
		testCode.setEffectiveDate(effDateInput);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");

		logger.info("*** Step 4 Action: - Cancel copy Effective Date");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Expected Results: - Copy effective date dialog closed and all fields reset to empty");
		assertTrue(isElementHidden(testCode.effCopyDialog(), 5), "        Copy effective date dialog closed");

		logger.info("*** Step 5 Action: - Add procedures with Exp date before Effective date and click ok button");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5), "        Add procedure button must be displayed");
		testCode.clickAddProcedureBtn();
		List<String> procedureCode = daoManagerXifinRpm.getProcCodeInfoFromPROCCDByTestAbbrev(singleTest, testDb);
		assertTrue(isElementPresent(testCode.procedureCodeInput(), 5), "        Procedure code input must be displayed");
		testCode.inputProcedureCode(procedureCode.get(0));
		assertTrue(isElementPresent(testCode.procedureTableType(), 5));
		testCode.selectProcedureTable(procedureCode.get(1));
		String expDate = testCodeUtils.getPreviousYear(effDateInput);
		assertTrue(isElementPresent(testCode.procedureExpDateInput(), 5), "        Procedure Expiration Date input must be displayed");
		testCode.inputProcedureExpDate(expDate);
		testCode.clickOkProcedureBtn();

		logger.info("*** Step 5 Expected Results: - Verify error message exp date must after effective date");
		assertTrue(isElementPresent(testCode.procedureDialogFormError(), 5), "        Error message exp date must after effective date");
		assertTrue(testCodeUtils.getTextValue(testCode.procedureDialogFormError(),5).contains("Expiration date must be after effective date"), "        Error message exp date must after effective date");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Procedures - Verify new proc isn't save when user click reset button")
	public void testXPR_235() throws Exception {
		logger.info("===== Testing - testXPR_235 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest ="ITG307";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Add procedures");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5), "        Add procedure button must be displayed");
		List<String> procedureAdded = testCode.addProcedure(this, testCode, "",testDb, singleTest);
		String procId = procedureAdded.get(0);

		logger.info("*** Step 3 Expected Results: - Verify added procedure in table");
		assertTrue(getColumnValue(testCode.procedureTable(), procId), "        Procedure added in table");

		logger.info("*** Step 4 Action: - Reset all");
		assertTrue(isElementPresent(testCode.resetBtn(), 5), "        Reset button should show");
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);

		logger.info("*** Step 4 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 5 Action: - Load again test code");
		testCode.checkInputTestCodeId(singleTest);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 5 Expected Results: - Procedure had been added before not save");
		assertFalse(getColumnValue(testCode.procedureTable(), procId), "        Procedure had been added before must not save");

		testCode.clickResetBtn();
	}

	@Test(priority=1, description="Single Test Detail - Procedures - Reset Update Procedure")
	public void testXPR_236() throws Exception{
		logger.info("===== Testing - testXPR_236 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
//		List<String> singleTestCodeInfo = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTestCodeInfo ="TV912DQ";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test page label must be displayed");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click on any Row in Procedure Table");
		assertTrue(isElementPresent(testCode.selectEffDate(), 5), "        Select eff Date should show");
		String effDate = testCode.currEffDate().getAttribute("value").trim();
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedure tab must be displayed");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.procedureTableInProcedureTable(), 5), "        Procedure table must be displayed");
		String procedureTable = testCode.procedureTableInProcedureTable().getText();
		assertTrue(isElementPresent(testCode.procedureCodeIdInProcedureTable(), 5), "        Procedure Code must be displayed");
		String procedureCode = testCode.procedureCodeIdInProcedureTable().getText();
		selectColumnValue(testCode.procedureTable(), procedureTable);

		logger.info("*** Step 3 Expected Results: - Edit Record popup is displayed");
		assertTrue(isElementPresent(testCode.editRecordProcedurePopUp(), 5),"         Edit Record Pop Up should be displayed");

		logger.info("*** Step 4 Action: - Update Procedure with valid data and ");
		String newProCodeId = testCode.getNewProcCodeIdInDb(this, procedureCode, procedureTable, testDb);
		assertTrue(isElementPresent(testCode.procedureCodeInput(), 5), "        Procedure code input must be displayed");
		testCode.inputProcedureCode(newProCodeId);
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button must be displayed");
		testCode.clickOkProcedureBtn();
		assertTrue(isElementPresent(testCode.resetBtn(), 5), "        Reset button must be displayed");
		testCode.clickResetBtn();

		logger.info("*** Step 4  Expected Results: - Updated Information is not saved in DB");
		closeAlertAndGetItsText(true);
		String testCodeId = String.valueOf(testDao.getTestByTestAbbrev(singleTestCodeInfo).getTestId());
		boolean update = testCode.isSaveDone(newProCodeId, testCodeId, effDate, testDb);
		assertFalse(update,"         Updated Information should not be saved in DB");
	}

	// Facility Tab
	@Test(priority=1, description="Single Test Detail - Facility - Add Facility with valid input")
	public void testXPR_96() throws Exception{
		logger.info("===== Testing - testXPR_96 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		randomCharacter = new RandomCharacter(driver);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input not available test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		String testCodeAbb = randomCharacter.getRandomNumericString(5);
		testCode.checkInputTestCodeIdWithNewTestID(testCodeAbb);

		logger.info("*** Step 2 Expected Results: - Single Test Radio available in Create Option Popup");
		assertTrue(isElementPresent(testCode.singleTestRadio(), 5), "        Single Test Radio should be available.");

		logger.info("*** Step 3 Action: - Click on Single Test Radio and submit");
		assertTrue(isElementPresent(testCode.singleTestRadio(), 5));
		selectCheckBox(testCode.singleTestRadio());
		assertTrue(isElementPresent(testCode.createOptionOKBtn(), 5));
		testCode.clickCreateOption();

		logger.info("*** Step 3 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 4 Action: - Input valid Effective date");
		assertTrue(isElementPresent(testCode.addEffDate(), 5));
		String currentDay = timeStamp.getCurrentDate("MM/dd/yyyy");
		testCode.setEffectiveDate(currentDay);

		logger.info("*** Step 4 Expected Result: - Input valid Effective date");
		assertTrue(isElementPresent(testCode.procedureTable(), 5),"        Procedure Table should be displayed");

		logger.info("*** Step 5 Action: - Add some procedure with Valid Data");
		List<String> procCodeInfo = testCode.addProcedure(this, testCode, "", testDb);

		logger.info("*** Step 5 Expected Result: - Verify that procedure code must be added");
		assertTrue(getColumnValue(testCode.procedureTable(), procCodeInfo.get(0)),"        procedure code must be added");

		logger.info("*** Step 6 Action: - Add Facilities");
		assertTrue(isElementPresent(testCode.facilityTable(), 5),"        Facility Table should be displayed");
		String facility = testCode.addNewFacility(testCode.facilityTable(), this);

		logger.info("*** Step 6 Expected Result: - Verify that Facility must be added");
		assertTrue(isElementPresent(testCode.facilityTable(), 5));
		assertTrue(getColumnValue(testCode.facilityTable(), facility), "        Facility must be added in table");
		assertTrue(isElementPresent(testCode.testNameInput(), 5));
		testCode.setTestName("TestName");
		Dept depInfo = depDao.getDepartment();
		assertTrue(isElementPresent(testCode.selectDeps(), 5));
		selectItemByVal(testCode.selectDeps(), String.valueOf(depInfo.getDeptId()));

		logger.info("*** Step 7 Action: - Click save and clear button");
		assertTrue(isElementPresent(testCode.saveAndClearButton(), 5), "       Save and clear button must be available");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 7 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 8 Action: - Load again test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(testCodeAbb);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 8 Expected Results: -  New Facility is save into corresponding Single test");
		assertTrue(isElementPresent(testCode.facilityTab(), 5),"        Facility tab should show.");
		testCode.clickFacilityTab();
		assertTrue(isElementPresent(testCode.facilityTable(), 5),"        Facility table should show.");
		assertTrue(getColumnValue(testCode.facilityTable(), facility), "        New  Facility should be save into corresponding Single test");

		testCode.clickResetBtn();
	}

	@Test(priority=1, description="Single Test Detail - Facility - Update Facility with valid input")
	public void testXPR_97() throws Exception{
		logger.info("===== Testing - testXPR_97 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		String singleTestId = "TESTXPR_97";
		testCode.createSingleTest(singleTestId, testDb,false,false,this);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 3 Action: - Select any row in Facility table");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5));
		String effDate = testCode.currEffDate().getAttribute("value").trim();
		assertTrue(isElementPresent(testCode.facilityTable(), 5));
		String facility = testCode.addNewFacility(testCode.facilityTable(), this);
		selectColumnValue(testCode.facilityTable(), facility);

		logger.info("*** Step 3 Expected Result: - Edit Record pop up is displayed ");
		assertEquals(testCode.editfacilityDialog().getText(), "Edit Record","        Edit Record pop up should be display");

		logger.info("*** Step 4 Action: - Input valid data in Pop Up field");
        switchToPopupWin();
		List<String> facilityEditInfo = testCode.editFieldsInEditPopup(this);
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Submit button must be available");
		testCode.clickOkFacilityBtn();

		logger.info("*** Step 4 Expected Result: - Update data is show in facility list");
		assertTrue(isElementPresent(testCode.facilityTable(), 5),"        Facility table must be display");
		assertTrue(getColumnValue(testCode.facilityTable(), facilityEditInfo.get(3)), "        Facility must be added in table");

		logger.info("*** Step 5 Action: - Click Save and Clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and clear button must be available");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 5 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 6 Action: - Load again test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID must be available");
		testCode.checkInputTestCodeId(singleTestId);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 6 Expected Result: - Update Information is save in DataBase with corresponding testId");
		String facilityId = daoManagerXifinRpm.getFacilityIdByAbbrv(facility, testDb);
		assertFalse(testCodeUtils.isListEmpty(daoManagerXifinRpm.getTestCodeInfo(singleTestId, testDb)),"        TestCode info should have data");
		String pTestCodeId = daoManagerXifinRpm.getTestCodeInfo(singleTestId, testDb).get(2);
		List<String> facilityListDB = daoManagerXifinRpm.getFacilityInfoByFacId(facilityId,pTestCodeId,effDate,testDb);
		assertFalse(testCodeUtils.isListEmpty(facilityListDB),"        Facility info should have data");
		assertTrue(isElementPresent(testCode.facilityTab(), 5),"        Facility tab should show");
		testCode.clickFacilityTab();
		assertTrue(isElementPresent(testCode.facilityTable(), 5));
		assertTrue(getColumnValue(testCode.facilityTable(),testCodeUtils.formatDecimalPoint(Integer.parseInt((facilityEditInfo.get(0))))), "        Lab Cost should be save");
		assertTrue(getColumnValue(testCode.facilityTable(), testCodeUtils.formatDecimalPoint(Integer.parseInt((facilityEditInfo.get(1))))), "        company Cost should be save");
		assertTrue(getColumnValue(testCode.facilityTable(), testCodeUtils.formatDecimalPoint(Integer.parseInt((facilityEditInfo.get(2))))), "        Min price should be save");
		assertTrue(getColumnValue(testCode.facilityTable(), facilityEditInfo.get(3)), "        info1 should be save");
		assertTrue(getColumnValue(testCode.facilityTable(), facilityEditInfo.get(4)), "        info2 should be save");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Facility - Verify hidden icon in The Facility grid")
	public void testXPR_99() throws Exception {
		logger.info("===== Testing - testXPR_99 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest="1549580267380";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");
		assertTrue(isElementPresent(testCode.facilityTable(),5),"        Facility Table must be available");

		logger.info("*** Step 3 Action: - Hide facility table");
		assertTrue(isElementPresent(testCode.facilityTab(), 5));
		testCode.clickFacilityTab();
		assertTrue(isElementPresent(testCode.hideShowFacilityTableIcon(), 5));
		testCode.clickHideShowFacilityTableIcon();

		logger.info("*** Step 3 Expected Results: - Facility table must be hide");
		assertTrue(isElementHidden(testCode.wrapperOfFacilityTable(), 5), "        Facility table must be hide");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Facility - Add new facility with performing facility is duplicate")
	public void testXPR_180() throws Exception {
		logger.info("===== Testing - testXPR_180 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input a Invalid Test ID and Tab out");
		String testCodeIdSingleTest = String.valueOf(System.currentTimeMillis());
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeIdWithNewTestID(testCodeIdSingleTest);

		logger.info("*** Step 2 Expected Results: - 'Create Option' screen appear");
		assertTrue(isElementPresent(testCode.createScreen(), 5), "        Create Screen should be available.");

		logger.info("*** Step 3 Action: - Choose Single Test option");
		assertTrue(isElementPresent(testCode.singleTestRadio(), 5),"        Single test radio must be available");
		selectCheckBox(testCode.singleTestRadio());
		assertTrue(isElementPresent(testCode.createOptionOKBtn(), 5),"        Submit create must be available");
		testCode.clickCreateOption();

		logger.info("*** Step 3 Expected Results: - Test Code Information (Single Test) appear");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Label test must be available");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		assertTrue(isElementPresent(testCode.effectiveDatelabel(), 5), "        Effective Date should be available.");		

		logger.info("*** Step 4 Action: - Enter Name and Effective Date");
		assertTrue(isElementPresent(testCode.testNameInput(), 5),"        Test name input must be available");
		testCode.setTestName("TEST SINGLE");
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"        Eff date add must be available");
		String curDate = timeStamp.getCurrentDate();
		testCode.setEffectiveDate(curDate);

		logger.info("*** Step 4 Expected Result: - Verify that tab list is displayed");
		assertTrue(isElementPresent(testCode.procedureTab(), 5), "        Procedures tab should be displayed");

		logger.info("*** Step 5 Action: - Add facility table");
		String facility = testCode.addNewFacility(testCode.facilityTable(), this);

		logger.info("*** Step 5 Expected Results: - Facility must be added");
		assertTrue(getColumnValue(testCode.facilityTable(), facility), "        Facility table must be added");

		logger.info("*** Step 6 Action: - Add again facility table");
		assertTrue(isElementPresent(testCode.addFacilityBtn(), 5), "        Facility button should be displayed");
		testCodeUtils.scrollIntoView(testCode.addFacilityBtn());
		testCode.clickAddFacilityBtn();
		testCode.selectPerformingFacility(facility);
		assertTrue(isElementPresent(testCode.labCostInput(), 5),"        Lab cost input must be available");
		testCode.inputLabCost("1");
		assertTrue(isElementPresent(testCode.minPriceInput(), 5),"         Min price must be available");
		testCode.inputMinPrice("1");
		assertTrue(isElementPresent(testCode.okBtn(), 5),"         Submit button must be available");
		testCode.clickOkFacilityBtn();

		logger.info("*** Step 6 Expected Results: - Verify error message duplicate record is display");
		assertTrue(isElementPresent(testCode.facilityDialogFormError(), 5), "        Error message duplicate record is display");
		assertTrue(testCode.facilityDialogFormError().getText().equalsIgnoreCase("A duplicate record was entered"), "        Error message alert 'A duplicate record was entered'");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Facility - Add new Facility with lab cost is greater than Min Price")
	public void testXPR_181() throws Exception {
		logger.info("===== Testing - testXPR_181=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		randomCharacter = new RandomCharacter();

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> singleTestID = daoManagerXifinRpm.getSingleTest(testDb);
		String TestID = "16704";
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test id input must be available");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");		

		logger.info("*** Step 3 Action: - Input valid effective date");
		scrollToElement(0, -250);
		String minEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		String effDateInput = testCodeUtils.getNextDay(minEffDate); 
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5),"        Create eff date icon must be available");
		testCode.clickCreateEffDateIcon();
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"        Add eff date input must be available");
		testCode.setEffectiveDate(effDateInput);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Action: - Facility tab : click on add icon");
		assertTrue(isElementPresent(testCode.facilityTab(), 5),"       Facility tab should show");
		testCode.clickFacilityTab();
		testCodeUtils.scrollIntoView(testCode.addFacilityBtn());
		assertTrue(isElementPresent(testCode.addFacilityBtn(), 5),"        Add facility button should show");
		testCode.clickAddFacilityBtn();

		logger.info("*** Step 4 Expected Results: - Add record popup is displayed");
		assertTrue(isElementPresent(testCode.facilityTablePopup(), 5),"        Add record popup should be displayed");

		logger.info("*** Step 5 Action: - Add Facilities with lab code is greater than min code");
		assertTrue(isElementPresent(testCode.performingFacility(), 5),"        Performing facility should show");
		testCode.selectPerformingFacility(2);
		String minPrice = "0";
		String labCost  = "5";
		assertTrue(isElementPresent(testCode.labCostInput(), 5),"       labCost input field should show");
		testCode.inputLabCost(labCost);
		assertTrue(isElementPresent(testCode.minPriceInput(), 5),"       Min Price input field should show");
		testCode.inputMinPrice(minPrice);
		assertTrue(isElementPresent(testCode.okBtn(), 5),"       Ok facility button should show");
		testCode.clickOkFacilityBtn();

		logger.info("*** Step 5 Expected Results: - Error message should be displayed");
		assertTrue(isElementPresent(testCode.procedureDialogFormError(), 5),"       procedure Dialog FormError should show");
		assertTrue(testCode.procedureDialogFormError().getText().trim().contains("Min Price must be greater than or equal to Lab Cost"),"        Error message should be 'Min Price: Min Price must be greater than or equal to Lab Cost'");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Facility - Add new Facility with require field is empty")
	public void testXPR_182() throws Exception {
		logger.info("===== Testing - testXPR_182 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		randomCharacter = new RandomCharacter();

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> singleTestID = daoManagerXifinRpm.getSingleTest(testDb);
		String TestID = "NE263FW";
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test id input must be available");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");		

		logger.info("*** Step 3 Action: - Input valid effective date");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5),"       Select Effective date should show");
		String minEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		String effDateInput = testCodeUtils.getNextDay(minEffDate); 
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5),"        create EffDate icon should show");
		testCode.clickCreateEffDateIcon();
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"        Effective Date input field should show");
		testCode.setEffectiveDate(effDateInput);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Action: - Facility tab : click on add icon");
		assertTrue(isElementPresent(testCode.facilityTab(), 5),"        facility tab should show");
		testCode.clickFacilityTab();
		assertTrue(isElementPresent(testCode.addFacilityBtn(), 5),"        add facility button should show");
		testCode.clickAddFacilityBtn();

		logger.info("*** Step 4 Expected Results: - Add record popup is displayed");
		assertTrue(isElementPresent(testCode.facilityTablePopup(), 5),"        Add record popup should be displayed");

		logger.info("*** Step 5 Action: - Leave Performing Facility field as blank and click ok button");
		String price = randomCharacter.getRandomNumericString(2);
		assertTrue(isElementPresent(testCode.labCostInput(), 5),"        lab Cost input field should show");
		testCode.inputLabCost(price);
		assertTrue(isElementPresent(testCode.minPriceInput(), 5),"        Min Price input field should show");
		testCode.inputMinPrice(price);
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok facility button field should show");
		testCode.clickOkFacilityBtn();

		logger.info("*** Step 5 Expected Results: - Error message should be displayed");
		assertTrue(isElementPresent(testCode.facilityDialogFormError(), 5),"       facility Dialog FormError should show");
		assertTrue(testCode.facilityDialogFormError().getText().trim().contains("Performing Facility: Field is required"),"        Error message should be 'Performing Facility: Field is required'");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Facility - Update Facility lab cost is greater than min price")
	public void testXPR_183() throws Exception{
		logger.info("===== Testing - testXPR_183 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test Id input field should show");
		assertFalse(testCodeUtils.isListEmpty(daoManagerXifinRpm.getSingleTest(testDb)),"        Single Test info should have data");
//		List<String> singleTestCodeInfo = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTestCodeInfo = "85444";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input must be available");
		testCode.checkInputTestCodeId(singleTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 3 Action: - Select any row in Facility table");
		assertTrue(isElementPresent(testCode.facilityTable(), 5),"        Facility table must be available");
		String facility = testCode.addNewFacility(testCode.facilityTable(), this);
		selectColumnValue(testCode.facilityTable(), facility);

		logger.info("*** Step 3 Expected Result: - Edit Record popup is displayed ");
		assertEquals(testCode.editfacilityDialog().getText(), "Edit Record","        Edit Record pop up should be display");

		logger.info("*** Step 4 Action: - Input Lab Cost is greater than Min Price");
		assertTrue(isElementPresent(testCode.labCostInput(), 5),"        Lab cost must be available");
		testCode.inputLabCost("1000");
		assertTrue(isElementPresent(testCode.minPriceInput(), 5),"        Min price must be available");
		testCode.inputMinPrice("1");
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button must be available");
		testCode.clickOkFacilityBtn();

		logger.info("*** Step 4 Expected Result: - The system will display error message");
		assertTrue(isElementPresent(testCode.facilityDialogFormError(), 5),"        Facility form error must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.facilityDialogFormError(),5).trim().contains("Min Price must be greater than or equal to Lab Cost"),"        Error message should be 'Min Price: Min Price must be greater than or equal to Lab Cost'.");	

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Facility - Change default facility")
	public void testXPR_184() throws Exception {
		logger.info("===== Testing - testXPR_184 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		randomCharacter = new RandomCharacter(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input must be available");
		String singleTestId = "TESTXPR_184" + randomCharacter.getRandomNumericString(3);
		testCode.createSingleTest(singleTestId, testDb,false,false,this);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click on Facility Tab. Click on Add icon");
		assertTrue(isElementPresent(testCode.facilityTab(), 5), "        Facility tab must be displayed");
		testCode.clickFacilityTab();
		assertTrue(isElementPresent(testCode.addFacilityBtn(), 5), "        Add facility button must be displayed");
		testCodeUtils.scrollIntoView(testCode.addFacilityBtn());
		testCode.clickAddFacilityBtn();

		logger.info("*** Step 3 Expected Results: - Add New Record Popup is displayed");
		assertTrue(isElementPresent(testCode.facilityDialogWrapper(), 5), "        Add new record facility popup must be displayed");

		logger.info("*** Step 4 Action: - Input valid data for all fields and click OK button");
		String facility = testCode.inputAllFacilityFieldsUnique(testCode.facilityTable(), this);
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        OK button must be displayed");
		testCode.clickOkFacilityBtn();

		logger.info("*** Step 4 Expected Results: - The add popup Record is close and new facility is displayed in list");
		assertTrue(isElementPresent(testCode.facilityDialogWrapper(), 5),"        Facility dialog wrapper must be available");
		String addNewFacilityPopupDisplay = testCode.facilityDialogWrapper().getCssValue("display");
		assertEquals(addNewFacilityPopupDisplay, "none","        Add new button mustn't be display");
		assertTrue(getColumnValue(testCode.facilityTable(), facility), "        Facility must added in table");

		logger.info("*** Step 5 Action: - Click radio button to set default facility for new facility");
		assertTrue(isElementPresent(testCode.facilityTable(), 5),"        Facility table must be available");
		testCode.setDefaultFacility(testCode.facilityTable(), facility);

		logger.info("*** Step 5 Expected Results: - The radio button is checked");
		assertTrue(testCode.checkRadioButtonDefault(testCode.facilityTable(), facility), "        The radio button must be checked");

		logger.info("*** Step 6 Action: - Click save and clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and clear button must be available");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 6 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 7 Action: - Load again test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input must be available");
		testCode.checkInputTestCodeId(singleTestId);
		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 7 Expected Result : - New facility has been added and make it to default facility");
		assertTrue(isElementPresent(testCode.facilityTab(), 5),"        facility tab should show");
		testCode.clickFacilityTab();
		assertTrue(isElementPresent(testCode.facilityTable(), 5),"        facility table should show");
		assertTrue(getColumnValue(testCode.facilityTable(), facility), "        Facility must added in table");
		assertTrue(testCode.checkRadioButtonDefault(testCode.facilityTable(), facility),"      Your default facility is changed");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Facility - Sort the facility as alphabet ordering")
	public void testXPR_198() throws Exception {
		logger.info("===== Testing - testXPR_198 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input a valid Test ID Single Test and Tab out");
		String testCodeIdSingleTest = "31129";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(testCodeIdSingleTest);

		logger.info("*** Step 2 Expected Results: - Test Code Information (Single Test) appear");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		assertTrue(isElementPresent(testCode.effectiveDatelabel(), 5), "        Effective Date should be available.");		

		logger.info("*** Step 3 Action: - Enter a valid effective date");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5),"        Select eff date must be available");
		String minEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		String effDateInput = testCodeUtils.getNextDay(minEffDate); 
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Create Effdate icon must be available");
		testCode.clickCreateEffDateIcon();
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"        Add effdate must be available");
		testCode.setEffectiveDate(effDateInput);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Action: - Add multi facility to facility table and sort by Lab Cost");
		testCode.addMultiFacility(3,this,testCode, testDb);
		assertTrue(isElementPresent(testCode.labCostCol(), 5),"        Lab cost column must be available");
		testCode.clickLabCostCol();

		logger.info("*** Step 4 Expected Results: - Lab cost is sorted");
		assertTrue(isElementPresent(testCode.facilityTable(), 5),"        Facilities table should show.");
		assertTrue(testCode.isSorting(this,testCode.facilityTable(), 6, "asc"),"       Lab cost should be sorted in Ascending order.");
		
		logger.info("*** Step 5 Action: - Sort by Company Cost");
		assertTrue(isElementPresent(testCode.companyCostCol(), 5),"        Company cost column must be available");
		testCode.clickCompanyCostCol();

		logger.info("*** Step 5 Expected Results: - Company cost is sorted");
		assertTrue(testCode.isSorting(this,testCode.facilityTable(),7, "asc"),"       Company cost should be sorted");

		logger.info("*** Step 6 Action: - Sort by Min price");
		assertTrue(isElementPresent(testCode.minPriceCol(), 5),"        Min price column must be available");
		testCode.clickMinPriceCol();

		logger.info("*** Step 6 Expected Results: - Min price is sorted");
		assertTrue(testCode.isSorting(this,testCode.facilityTable(),8, "asc"),"       Min price should be sorted");
		
		testCode.clickResetBtn();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Facility - Filter facility as per performing facility")
	public void testXPR_201() throws Exception {
		logger.info("===== Testing - testXPR_201 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input a valid Test ID Single Test and Tab out");
		String testCodeIdSingleTest = "IFX720";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(testCodeIdSingleTest);

		logger.info("*** Step 2 Expected Results: - Test Code Information (Single Test) appear");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		assertTrue(isElementPresent(testCode.effectiveDatelabel(), 5), "        Effective Date should be available.");		

		logger.info("*** Step 3 Action: - Add New facility to facility table and filter by Performing Facility");
		List<String> facilities = testCode.addMultiFacility(3,this,testCode, testDb);

		logger.info("*** Step 3 Expected Results: - The new facility is added into the list");
		assertTrue(getColumnValue(testCode.facilityTable(), facilities.get(0)), "        The new facility is added into the list");

		logger.info("*** Step 4 Action: - Input valid by Performing Facility only");
		assertTrue(isElementPresent(testCode.performingFacilityFilterInput(), 5),"         Performing facility filter must be available");
		testCode.inputPerformingFacilityFilter(facilities.get(0));

		logger.info("*** Step 4 Expected Results: - System should perform filter correctly");
		assertEquals(getTableTotalRowSize(testCode.facilityTable()),2,"        Facility should be have only one row match with filter condition.");
		assertEquals(getFilteredDisplayRowCount(testCode.facilityTable()), 1,"        Facility should be have only one row match with filter condition.");	

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Facility - Add new Facility with invalid format exp date")
	public void testXPR_237() throws Exception {
		logger.info("===== Testing - testXPR_237=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> singleTestID = daoManagerXifinRpm.getSingleTest(testDb);
		String TestID = "77222";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single test information page is displayed");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");		

		logger.info("*** Step 3 Action: - Facility tab : click on add icon");
		assertTrue(isElementPresent(testCode.facilityTab(), 5), "        Facility tab must be available");
		testCode.clickFacilityTab();
		assertTrue(isElementPresent(testCode.addFacilityBtn(), 5),"        Add facility button must be available");
		testCodeUtils.scrollIntoView(testCode.addFacilityBtn());
		testCode.clickAddFacilityBtn();

		logger.info("*** Step 3 Expected Results: - Add record popup is displayed");
		assertTrue(isElementPresent(testCode.facilityTablePopup(), 5),"        Add record popup should be displayed");

		logger.info("*** Step 4 Action: - input invalid format date for expiration date");
		testCode.inputExpDateFacilityPopup("2015/10/10");

		logger.info("*** Step 4 Expected Results: - Error popup should be displayed");
		assertTrue(isElementPresent(testCode.errorPopup(), 5), "        Error popup should be displayed");
		assertTrue(isElementPresent(testCode.closeErrorMessage(), 5), "      Close Error popup should be displayed");

		testCode.clickcloseErrorMessage();
		testCode.expDateFacilityPopup().clear();
		testCode.expDateFacilityPopup().sendKeys(Keys.TAB);
		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Facility - Cancel add new facility")
	public void testXPR_238() throws Exception {
		logger.info("===== Testing - testXPR_238 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest="MZ936JZ";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click on Facility Tab. Click on Add icon");
		assertTrue(isElementPresent(testCode.facilityTab(), 5), "        Facility tab must be displayed");
		testCode.clickFacilityTab();
		assertTrue(isElementPresent(testCode.addFacilityBtn(), 5), "        Add facility button must be displayed");
		testCodeUtils.scrollIntoView(testCode.addFacilityBtn());
		testCode.clickAddFacilityBtn();

		logger.info("*** Step 3 Expected Results: - Add New Record Popup is displayed");
		assertTrue(isElementPresent(testCode.facilityDialogWrapper(), 5), "        Add new record facility popup must be displayed");

		logger.info("*** Step 4 Action: - Input valid data for all fields and click cancel button");
		String facility = testCode.inputAllFacilityFieldsUnique(testCode.facilityTable(), this);
		assertTrue(isElementPresent(testCode.cancelBtn(), 5), "        Cancel button must be displayed");
		testCode.clickCancelFacilityBtn();

		logger.info("*** Step 4 Expected Results: - The add popup Record is close and new facility is not displayed in list");
		String addNewFacilityPopupDisplay = testCode.facilityDialogWrapper().getCssValue("display");
		assertEquals(addNewFacilityPopupDisplay, "none","       New facility popup mustn't be display");
		assertFalse(getColumnValue(testCode.facilityTable(), facility), "        Facility was added in the Facilities Table.");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Facility - Verify that new facility is not saved when user click Reset")
	public void testXPR_239() throws Exception {
		logger.info("===== Testing - testXPR_239=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> singleTestID = daoManagerXifinRpm.getSingleTest(testDb);
		String TestID ="PRD201";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single test information page is displayed");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");		

		logger.info("*** Step 3 Action: - Facility tab - Click on add icon : Input valid data for all fields and click ok button");
		assertTrue(isElementPresent(testCode.facilityTab(), 5), "        Facility tab must be available");
		assertTrue(isElementPresent(testCode.addFacilityBtn(), 5),"        Add facility button must be available");
		List<String> facilityInfo = testCode.addMultiFacility(2, this, testCode, testDb);

		logger.info("*** Step 3 Expected Results: - New facility added in table");
		assertTrue(isElementPresent(testCode.facilityTable(), 5),"        Facility table must be available");
		assertTrue(getColumnValue(testCode.facilityTable(), facilityInfo.get(0)), "        Facility must be added in table");

		logger.info("*** Step 4 Action: - click Reset button");
		assertTrue(isElementPresent(testCode.resetBtn(), 5),"        Reset button must be available");
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);

		logger.info("*** Step 4 Expected Results: - System return test code page and New facility is not save in database");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);
		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		assertTrue(isElementPresent(testCode.facilityTable(), 5),"        Facility table must be available");
		assertFalse(getColumnValue(testCode.facilityTable(), facilityInfo.get(0)), "        Facility must not be added in table");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Facility - Reset the update facility")
	public void testXPR_242() throws Exception {
		logger.info("===== Testing - testXPR_242 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest ="CT610VG";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test information page is displayed");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click on Facility Tab. Select any row and click edit button");
		assertTrue(isElementPresent(testCode.facilityTab(), 5), "        Facility tab must be displayed");
		testCode.clickFacilityTab();
		testCode.selectFirstRowInFacilityTable();
		List<String> beforeEdited = testCode.getAllFieldsInFirstRow(testCode.facilityTable());
		assertTrue(isElementPresent(testCode.editFacilityTableButton(), 5), "        Edit button must be displayed");
		clickHiddenPageObject(testCode.editFacilityTableButton(), 0);

		logger.info("*** Step 3 Expected Results: - Edit popup is displayed");
		assertTrue(isElementPresent(testCode.alertModifiedPopupFacilityTable(), 5), "        Alert popup must be displayed");

		logger.info("*** Step 4 Action: - Update some fields with valid data");
		List<String> editListInfo =   testCode.editFieldsInEditPopup(this);
		testCode.clickOkFacilityBtn();
		List<String> afterEdited = testCode.getAllFieldsInFirstRow(testCode.facilityTable());

		logger.info("*** Step 4 Expected Results: - The update information is displayed in the table");
		assertNotEquals(beforeEdited, afterEdited,"        Facility must be edit correctly");
		assertTrue(getColumnValue(testCode.facilityTable(), editListInfo.get(3)), "        Facility added in table");
		assertTrue(getColumnValue(testCode.facilityTable(), editListInfo.get(4)), "        Facility added in table");

		logger.info("*** Step 5 Action: - Click reset button and dismiss alert popup");
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);

		logger.info("*** Step 5 Expected Results: - Go back to loading page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 6 Action: - Load the same test code again");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");
		testCode.clickFacilityTab();

		logger.info("*** Step 6 Expected Results: - The updated information for facility is not saved to DB after the Reset");
		List<String> currentRow = testCode.getAllFieldsInFirstRow(testCode.facilityTable());
		assertEquals(currentRow, beforeEdited,"        The Facilities should be the same as before the editing."); //check current first row with last first row before edited
		assertNotEquals(currentRow, afterEdited,"        The updated information for facility should not saved to DB."); //check current first row with last first row after edited

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Facility - Update facility without select any row")
	public void testXPR_243() throws Exception {
		logger.info("===== Testing - testXPR_243 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest="ND568ZU";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test information is displayed");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click on facility tab - click on edit icon without select any row");
		assertTrue(isElementPresent(testCode.facilityTab(), 5), "        Facility tab must be displayed");
		testCode.clickFacilityTab();
		assertTrue(isElementPresent(testCode.editFacilityTableButton(), 5), "        Edit button must be displayed");
		clickHiddenPageObject(testCode.editFacilityTableButton(), 0);

		logger.info("*** Step 3 Expected Results: - The error message popup 'Please select row' is displayed");
		assertTrue(isElementPresent(testCode.pleaseSelectRowPopup(), 5),"        Please select row must be available");
		String displayPleaseSelectRowPopup = testCode.pleaseSelectRowPopup().getText();
		assertTrue(displayPleaseSelectRowPopup.contains("Warning\nPlease, select row"),"        The error message popup 'Please select row' should be displayed");

		assertTrue(isElementPresent(testCode.WarningCloseBtn(), 5),"        Close warning button should show");
		testCode.clickWarningCloseBtn();
		testCode.clickResetBtn();
	}

	// Modifier Tab
	@Test(priority=1, description="Single Test Detail - Modifier - Add Payor Group with valid input")
	public void testXPR_100() throws Exception{
		logger.info("===== Testing - testXPR_100 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		randomCharacter = new RandomCharacter(driver);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input not available test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		String testCodeAbb = randomCharacter.getRandomNumericString(5);
		testCode.checkInputTestCodeIdWithNewTestID(testCodeAbb);

		logger.info("*** Step 2 Expected Results: - Single Test Radio available in Create Option Popup");
		assertTrue(isElementPresent(testCode.singleTestRadio(), 5), "        Single Test Radio should be available.");

		logger.info("*** Step 3 Action: - Click on Single Test Radio and submit");
		assertTrue(isElementPresent(testCode.singleTestRadio(), 5),"        Single test radio button must be available");
		selectCheckBox(testCode.singleTestRadio());
		assertTrue(isElementPresent(testCode.createOptionOKBtn(), 5),"        Ok button on create test code option must be available");
		testCode.clickCreateOption();

		logger.info("*** Step 3 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 4 Action: - Input valid Effective date");
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"        Add effdate must be available");
		String currentDay = timeStamp.getCurrentDate("MM/dd/yyyy");
		testCode.setEffectiveDate(currentDay);

		logger.info("*** Step 4 Expected Result: - Input valid Effective date");
		assertTrue(isElementPresent(testCode.procedureTable(), 5),"        Procedure Table should be displayed");

		logger.info("*** Step 5 Action: - Add some procedure with Valid Data");
		List<String> procCodeInfo = testCode.addProcedure(this, testCode, "", testDb);

		logger.info("*** Step 5 Expected Result: - Verify that procedure code must be added");
		getColumnValue(testCode.procedureTable(), procCodeInfo.get(0));

		logger.info("*** Step 6 Action: - Add Facilities");
		String facility = testCode.addNewFacility(testCode.facilityTable(), this);

		logger.info("*** Step 6 Expected Result: - Verify that Facility must be added");
		assertTrue(isElementPresent(testCode.facilityTable(), 5),"        Facility table must be available");
		assertTrue(getColumnValue(testCode.facilityTable(), facility), "        Facility must be added in table");

		logger.info("*** Step 7 Action: - Add Modifier");
		assertTrue(isElementPresent(testCode.testNameInput(), 5),"        Test name input must be available");
		testCode.setTestName("TestName");
		Dept depInfo = depDao.getDepartment();
		assertTrue(isElementPresent(testCode.selectDeps(), 5), "        Select department must be available");
		selectItemByVal(testCode.selectDeps(), String.valueOf(depInfo.getDeptId()));
		
		List<List<String>> groupModifierInfo =  testCode.addGroupModifiers(1, this,"",true,testDb);

		logger.info("*** Step 7 Expected Results: - A new group payor modifier id added in payor group modifier table");
		assertTrue(isElementPresent(testCode.payorGroupModifiersTable(), 5), "       Payor group modifier table should show");
		assertTrue(getColumnValue(testCode.payorGroupModifiersTable(), groupModifierInfo.get(0).get(1)), "        New Payor group modifier is added in table");
		
		logger.info("*** Step 8 Action: - Click save and clear button");
		assertTrue(isElementPresent(testCode.saveAndClearButton(), 5), "       Save and clear button must be available");
		testCode.clickSaveAndClearBtn();
		wait.until(ExpectedConditions.invisibilityOf(testCode.saveInProgressInfoText()));
		
		assertTrue(isElementPresent(testCode.testIdInput(), 10), "       Load test code page should show.");

		logger.info("*** Step 8 Expected Results: -  New Single test ID is save into DB. New PayorGroup is save into corresponding Single test");
		ArrayList<String> testCodeInfoList = daoManagerXifinRpm.getTestCodeInfo(testCodeAbb, testDb);
		assertNotNull(testCode.isSaveDone(testCodeInfoList),"        New Single Test Id should be saved in DB");
		String testCodeId = testCodeInfoList.get(2);
		boolean isSave = daoManagerXifinRpm.checkTestPyrGrpModInDb(groupModifierInfo.get(0).get(0),testCodeId,currentDay,testDb);
		assertTrue(isSave,"        New PayorGroup should be saved into corresponding Single test");
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Change default facility")
	public void testXPR_101() throws Exception {
		logger.info("===== Testing - testXPR_101 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		String singleTestId = "TESTXPR_101";
		testCode.createSingleTest(singleTestId, testDb,false,false,this);

		logger.info("*** Step 2 Expected Results: - System displayed test code with information");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Select any row on modifier group payor. Click edit");
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.payorGroupModifiersTable(), 5),"        Payor group modifier table must be available");
		int numberRowsBefore = getTableTotalRowSize(testCode.payorGroupModifiersTable());
		testCode.editModifierGroupPayor(numberRowsBefore, testDb, singleTestId);
		numberRowsBefore = getTableTotalRowSize(testCode.payorGroupModifiersTable());

		logger.info("*** Step 3 Expected Results: - Popup must be displayed. GroupID is readonly field");
		String popupDisplay = testCode.addPayorGroupDialog().getCssValue("display");
		assertEquals(popupDisplay, "block","        Pop up must be display");
		String groupIdCss = testCode.GroupIdTypeModifier().getAttribute("class");
		assertTrue(groupIdCss.contains("select2-container-disabled"),"        Container group id must be readonly");

		logger.info("*** Step 4 Action: - Click OK button");
		testCode.clickOkPayorGrpBtn();
		int numberRowsAfter = getTableTotalRowSize(testCode.payorGroupModifiersTable());

		logger.info("*** Step 4 Expected Results: - The update data is show in the table");
		List<String> dataFirstRowBefore = testCode.getAllFieldsInFirstRow(testCode.payorGroupModifiersTable());
		popupDisplay = testCode.addPayorGroupDialog().getCssValue("display");
		assertEquals(popupDisplay, "none","        Popup must be close");
		assertEquals(numberRowsBefore, numberRowsAfter,"        Number row before must equal number row after");

		logger.info("*** Step 5 Action: - Click save and clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and clear button must be available");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 5 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test Id input should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 6 Action: - Load again test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestId);
		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 6 Expected Result : - New facility has been added and make it to default facility");
		List<String> dataFirstRowCurrent = testCode.getAllFieldsInFirstRow(testCode.payorGroupModifiersTable());
		int numberRowsCurrent = getTableTotalRowSize(testCode.payorGroupModifiersTable());
		assertEquals(numberRowsAfter, numberRowsCurrent,"        Number row after must be equal number row current");
		assertEquals(dataFirstRowBefore, dataFirstRowCurrent,"        Data before must be equal data current");

		testCode.clickResetBtn();
	}


	@Test(priority = 1, description = "Single Test Detail - Modifier - Delete any payor group ID")
	public void testXPR_102() throws Exception {
		logger.info("===== Testing - testXPR_102 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("***Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");	
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"         Test label must be available");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input a valid Test ID and Tab out");
		String testCodeId = "TESTXPR_102";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.createSingleTest(testCodeId, testDb,false,false,this);

		logger.info("*** Step 2 Expected Results: - Test Code Information (Single Test) appear");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		assertTrue(isElementPresent(testCode.modifierTab(), 5), "        Modifier tab should be displayed");

		logger.info("*** Step 3 Action: - Add valid payor group to modifiers");
		String payorName = testCode.addGroupModifiers(1, this, "",true, testDb).get(0).get(1);

		logger.info("*** Step 3 Expected Results: - Payor group is added into table modifiers");
		assertTrue(getColumnValue(testCode.payorGroupModifiersTable(), payorName),"        Payor group is added into table modifiers");

		logger.info("*** Step 4 Action: - Click save and clear button. Load again test code id information");
		testCode.clickSaveAndClearBtn();
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(testCodeId);
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();

		logger.info("*** Step 4 Expected Results: - Verify payor group had been added before is saved");
		assertTrue(getColumnValue(testCode.payorGroupModifiersTable(), payorName),"        Payor group is added into table modifiers");

		logger.info("*** Step 5 Action: - Select any row");
		selectColumnValue(testCode.payorGroupModifiersTable(), payorName);

		logger.info("*** Step 5 Expected Results: - Edit dialog display");
		assertTrue(isElementPresent(testCode.editPayorGroupDialog(), 5),"        Payor edit dialog must be display");

		logger.info("*** Step 6 Action: - Delete payor group had been added before");
		selectCheckBox(testCode.deletePayorCheckBox());
		testCode.clickOkOnEditModifierGroup();

		logger.info("*** Step 6 Expected Results: - Verify payor group had been mark to remove from table");
		assertTrue(testCodeUtils.hasClass(testCode.getRowOfPayorGroupSpecificModifiersTable(testCode.getRowIndex(testCode.payorGroupModifiersTable(), payorName)-1), "rowMarkedForDelete"),"        Payor group must be marked to remove from table");

		logger.info("*** Step 7 Action: - Save and load again single test");
		testCode.clickSaveAndClearBtn();
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(testCodeId);
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();

		logger.info("*** Step 7 Expected Results: - The select payor group will be moved out of the corresponding single test id");
		assertFalse(getColumnValue(testCode.payorGroupModifiersTable(), payorName),"        Payor group had been added before must be removed");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Hide grid balances in Payor Group table")
	public void testXPR_103() throws Exception {
		logger.info("===== Testing - testXPR_103=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> singleTestID = daoManagerXifinRpm.getSingleTest(testDb);
		String TestID ="FI599OU";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");

		logger.info("*** Step 3 Action: - Input valid effective date");
		scrollToElement(0, -250);
		String minEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		String effDate = testCodeUtils.getNextDay(minEffDate); 
		testCode.clickCreateEffDateIcon();
		testCode.setEffectiveDate(effDate);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Action: - Hide Specific Modifier table");
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.hideGroupSpecificModifierTableButton(), 5),"        Hide group specific modifier table button must be available");
		testCode.clickHideGroupSpecificModifierTableButton();

		logger.info("*** Step 4 Expected Results: - Procedure table must be hide");
		assertTrue(isElementHidden(testCode.wrapperOfGroupSpecificModifierTable(), 5), "        Specific Modifier Table must be hide");		

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Add payor with valid input")
	public void testXPR_104() throws Exception {
		logger.info("===== Testing - testXPR_104 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);

		logger.info("***Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"         Test label must be available");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input a valid Test ID and Tab out");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		String singleTestID = "TESTXPR_104";
		testCode.createSingleTest(singleTestID, testDb,false,false,this);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Add valid payor to modifiers");
		String payorID = testCode.addModifiers(1, this, "").get(0).get(0);

		logger.info("*** Step 3 Expected Results: - Payor is added into table modifiers");
		assertTrue(getColumnValue(testCode.payorSpecificModifiersTable(), payorID),"        Payor is added into table modifiers");

		logger.info("*** Step 4 Action: - Click save and clear button. Load test id again");
		testCode.clickSaveAndClearBtn();
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestID);

		logger.info("*** Step 4 Expected Results: - New Modifier is displayed in table");
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();
		assertTrue(getColumnValue(testCode.payorSpecificModifiersTable(), payorID),"        Payor must be saved");

		testCode.clickResetBtn();
	}

	@Test(priority=1, description="Single Test Detail - Modifier - Edit payor with valid data")
	public void testXPR_105() throws Exception{
		logger.info("===== Testing - testXPR_105 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input not available test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		String singleTestId = "TESTXPR_105";
		testCode.createSingleTest(singleTestId, testDb,false,false,this);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 3 Action: - Add Modifier");
		List<List<String>> payorInfo =  testCode.addModifiers(1, this,"");

		logger.info("*** Step 3 Expected Results: - Payor Modifier will be added in Payor Specific Table");
		assertTrue(isElementPresent(testCode.payorSpecificModifiersTable(), 5),"        Payor Specific modifier table must be available");
		assertTrue(getColumnValue(testCode.payorSpecificModifiersTable(), payorInfo.get(0).get(1)),"        Payor modifier must be added to table");
		doubleClickHiddenPageObject(testCode.payorSpecificModifiersTable(), 0);

		logger.info("*** Step 4 Action: - Edit  Payor Specific Modifiers");
		String newPayorId = testCode.getNewPayorSpecificModifiersIdInDb(this, testDb);
		testCode.setPayorIdModifiersInput(newPayorId);
		testCode.selectModifier(true, 2); // Select modifier 1
		testCode.selectModifier(false, 2);// Select modifier 2
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button must be available");
		testCode.clickOkOnAddModifier();

		logger.info("*** Step 4 Expected Results: - The update information is displayed in table");
		assertTrue(isElementPresent(testCode.payorSpecificModifiersTable(), 5),"        Payor Specific modifier table must be available");
		assertTrue(getColumnValue(testCode.payorSpecificModifiersTable(), newPayorId),"         The update information should be displayed in table");

		logger.info("*** Step 5 Action: - Click Save and clear Button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and clear button must be available");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 5 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 6 Action: - Load again test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestId);
//		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 6  Expected Results: - The update information is saved in DB");
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.payorSpecificModifiersTable(), 5),"        Payor Specific modifier table must be available");
		testCodeUtils.scrollIntoView(testCode.payorSpecificModifiersTable());
		assertTrue(getColumnValue(testCode.payorSpecificModifiersTable(), newPayorId),"         The update information should be displayed in DB");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Verify hide icon in Payor Table")
	public void testXPR_107() throws Exception {
		logger.info("===== Testing - testXPR_107=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> singleTestID = daoManagerXifinRpm.getSingleTest(testDb);
		String TestID = "OCLVF";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");

		logger.info("*** Step 3 Action: - Input valid effective date");
		scrollToElement(0, -250);
		String minEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		String effDate = testCodeUtils.getNextDay(minEffDate); 
		clickHiddenPageObject(testCode.createEffDateIcon(), 0);
		testCode.setEffectiveDate(effDate);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Action: - Hide Specific Modifier table");
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.hideSpecificModifierTableButton(), 5),"        Hide specific modifier table must be available");
		testCode.clickHideSpecificModifierTableButton();

		logger.info("*** Step 4 Expected Results: - Procedure table must be hide");
		assertTrue(isElementHidden(testCode.wrapperOfSpecificModifierTable(), 5), "        Specific Modifier Table must be hide");		

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Modifier - Add Payor Group with invalid format date for  expiration date")
	public void testXPR_185() throws Exception{
		logger.info("===== Testing - testXPR_185 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input not available test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
//		List<String> testCodeInfo = daoManagerXifinRpm.getSingleTest(testDb);
		String testCodeInfo ="KL565XS";
		testCode.checkInputTestCodeId(testCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 3 Action: - Enter invalid Expiration Date");
		testCode.addGroupModifiers(1, this,"2015/01/01",true,testDb);

		logger.info("*** Step 3 Expected Results: - System will display Notice");
		assertTrue(isElementPresent(testCode.messageContent(), 5),"         Message content must be display");
		assertTrue(testCode.messageContent().getText().contains("Invalid date format."),"        System will display Notice");
		assertTrue(isElementPresent(testCode.closeErrorMessage(), 5),"        Close message button must be available");
		testCode.clickcloseErrorMessage();
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button must be available");
		testCode.clickOkPayorGrpBtn();
		testCode.clickcloseErrorMessage();

		testCode.expirationDatePayorGroup().clear();
		testCode.expirationDatePayorGroup().sendKeys(Keys.TAB);
		
		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Modifier - Add Payor Group with require field are empty")
	public void testXPR_186() throws Exception{
		logger.info("===== Testing - testXPR_186 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input not available test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
//		List<String> testCodeInfo = daoManagerXifinRpm.getSingleTest(testDb);
		String testCodeInfo = "HT115DR";
		testCode.checkInputTestCodeId(testCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 3 Action: - Leave Group Id field is empty");
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.addNewGroupPayorButton(), 5),"        Add new payor button must be available");
		testCode.clickAddGroupPayorBt();
		testCode.selectGroupModifier(true, 2); // Select modifier 1
		testCode.selectGroupModifier(false, 2);// Select modifier 2
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button must be available");
		testCode.clickOkPayorGrpBtn();

		logger.info("*** Step 3 Expected Results: - System will display Error message");
		assertTrue(isElementPresent(testCode.payorGroupDialogFormError(), 5));
		assertTrue(testCode.payorGroupDialogFormError().getText().contains("Group ID: Field is required"),"        System will display error message : Group ID: Field is required");

		logger.info("*** Step 4 Action: - Leave Group Id field is empty");
		assertFalse(testCodeUtils.isListEmpty(daoManagerXifinRpm.getGroupPayor(testDb)),"        Payor Group should have data to test");
		List<String> listGroupPayorInfo = daoManagerXifinRpm.getGroupPayor(testDb);
		testCode.selectGroupIdModifier(listGroupPayorInfo.get(1));
		testCode.selectGroupModifier(true, 0); // Select modifier 1
		testCode.selectGroupModifier(false, 0);// Select modifier 2
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button must be available");
		testCode.clickOkPayorGrpBtn();

		logger.info("*** Step 4 Expected Results: - System will display Error message");
		assertTrue(isElementPresent(testCode.payorGroupDialogFormError(), 5),"        Payor group dialog form error must be display");
		assertTrue(testCode.payorGroupDialogFormError().getText().contains("Modifier: Field is required"),"        System will display error message : Modifier: Field is required");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Update Group Payor with modifier fields are empty")
	public void testXPR_187() throws Exception {
		logger.info("===== Testing - testXPR_187 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest="DR644KR";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single displayed test code with information");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Select any row on modifier group payor. Click edit");
		testCode.clickModifierTab();
		int numberRowsBefore = getTableTotalRowSize(testCode.payorGroupModifiersTable());
		testCode.editModifierGroupPayorWithBlankFields(numberRowsBefore, testDb, singleTest);
		numberRowsBefore = getTableTotalRowSize(testCode.payorGroupModifiersTable());

		logger.info("*** Step 3 Expected Results: - Popup must be displayed. GroupID is read only field");
		String popupDisplay = testCode.addPayorGroupDialog().getCssValue("display");
		assertEquals(popupDisplay, "block", "        Popup must be display");
		String groupIdCss = testCode.GroupIdTypeModifier().getAttribute("class");
		assertTrue(groupIdCss.contains("select2-container-disabled"),"        Group ID must be readonly");

		logger.info("*** Step 4 Action: - Click OK button");
		testCode.clickOkPayorGrpBtn();

		logger.info("*** Step 4 Expected Results: - The error message is shown");
		String errorDisplay = testCode.payorGroupDialogFormError().getCssValue("display");
		assertEquals(errorDisplay, "table-row","        Form error must be display");
		assertEquals(testCode.payorGroupDialogFormError().getText(), "Modifier: Field is required");
		
		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Update payor with modifiers are empty")
	public void testXPR_188() throws Exception {
		logger.info("===== Testing - testXPR_188 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);

		logger.info("***Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");	
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"         Test label must be available");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input a valid Test ID and Tab out");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		String singleTest = "38658";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single display test code with information");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Add valid payor to modifiers");
		String payorID = testCode.addModifiers(1, this, "").get(0).get(0);

		logger.info("*** Step 3 Expected Results: - Payor is added into table modifiers");
		assertTrue(getColumnValue(testCode.payorSpecificModifiersTable(), payorID),"        Payor is added into table modifiers");

		logger.info("*** Step 4 Action: - Edit payor had been added");
		doubleClickHiddenPageObject(testCode.payorSpecificModifiersTable(), 0);

		logger.info("*** Step 4 Expected Results: - Verify payor edit dialog display");
		assertTrue(isElementPresent(testCode.editPayorDialog(), 5),"        Payor edit dialog must be display");

		logger.info("*** Step 5 Action: - Set empty 2 modifiers and click OK");
		testCode.selectModifier(true, 0);
		testCode.selectModifier(false, 0);
		testCode.clickOkOnAddModifier();

		logger.info("*** Step 5 Expected Results: - The error message is displayed");
		assertTrue(isElementPresent(testCode.payorSpecificmodifierDialogFormError(), 5),"        Error message must be display");
		assertTrue(testCode.payorSpecificmodifierDialogFormError().getText().equalsIgnoreCase("Modifier: Field is required"),"        Message error must alert 'Modifier: Field is required'");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Delete any payor info")
	public void testXPR_193() throws Exception {
		logger.info("===== Testing - testXPR_193 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("***Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"         Test label must be available");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input a valid Test ID and Tab out");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		String singleTestID = "TESTXPR_193";
		testCode.createSingleTest(singleTestID, testDb,false,false,this);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Add valid payor to modifiers");
		String payorID = testCode.addModifiers(1, this, "").get(0).get(0);

		logger.info("*** Step 3 Expected Results: - Payor is added into table modifiers");
		assertTrue(getColumnValue(testCode.payorSpecificModifiersTable(), payorID),"        Payor is added into table modifiers");

		logger.info("*** Step 4 Action: - Click save and clear button.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 4 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 5 Action: - Load again test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestID);
		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 5 Expected Results: - Verify payor had been added before is saved");
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();
		assertTrue(getColumnValue(testCode.payorSpecificModifiersTable(), payorID),"        Payor must be saved");

		logger.info("*** Step 6 Action: - Edit Payor added");
		testCodeUtils.scrollIntoView(testCode.payorSpecificModifiersTable());
		doubleClickHiddenPageObject(testCode.payorSpecificModifiersTable(), 0);

		logger.info("*** Step 6 Expected Results: - Verify payor edit dialog display");
		assertTrue(isElementPresent(testCode.editPayorDialog(), 5),"        Payor edit dialog must be display");

		logger.info("*** Step 7 Action: - Delete payor had been added before");
		selectCheckBox(testCode.deletePayorCheckBox());
		testCode.clickOkOnAddModifier();

		logger.info("*** Step 7 Expected Results: - Verify payor had been mark to remove from table");
		int indexOfRowDelete = testCode.getRowIndex(testCode.payorSpecificModifiersTable(), payorID)-1;
		assertTrue(testCodeUtils.hasClass(testCode.getRowOfPayorSpecificModifiersTable(indexOfRowDelete), "rowMarkedForDelete"),"        Payor must be marked to remove from table");

		logger.info("*** Step 8 Action: - Save and load again single test");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 8 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 9 Action: - Load again test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestID);
		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);

		logger.info("*** Step 9 Expected Results: - The Single test page must be displayed with information");
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");

		logger.info("*** Step 10 Actions: - Click Modifier Tab");
		testCode.clickModifierTab();
		testCodeUtils.scrollIntoView(testCode.payorSpecificModifiersTable());

		logger.info("*** Step 10 Expected Results: - The select row will be moved out of the corresponding Single Test");
		assertFalse(getColumnValue(testCode.payorSpecificModifiersTable(), payorID),"        Payor had been added before must be removed");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Filter in Payor Group Specific Modifiers")
	public void testXPR_202() throws Exception {
		logger.info("===== Testing - testXPR_202=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest="EA542LR";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Add modifier and filter modifier table with GROUP ID");
		testCode.clickModifierTab();
		String minEffDate = testCode.selectEffDate().getAttribute("value").trim();
		String effDateInput = testCodeUtils.getNextYear(minEffDate);
		List<List<String>> modifierAdded = testCode.addGroupModifiers(2, this, effDateInput ,true, testDb);
		int numRows = getTableTotalRowSize(testCode.payorGroupModifiersTable());
		testCode.clearAllGrpPayorFilter();
		String modTable =  modifierAdded.get(0).get(1);
		testCode.inputModTableFilterGrpId(modTable);

		logger.info("*** Step 3 Expected Results: - Verify filter procedure match with procedure table");
		assertTrue(getTableTotalRowSize(testCode.payorGroupModifiersTable()) <= numRows, "        Search result should be empty.");
		assertTrue(getColumnValue(testCode.payorGroupModifiersTable(), modTable), "        Procedure table only show filter modifier");

		logger.info("*** Step 4 Action: - Add modifier and filter modifier table with MODIFIER 1");
		modTable =  modifierAdded.get(0).get(2);
		testCode.clearAllGrpPayorFilter();
		testCode.inputModTableFilterMod1(modTable);

		logger.info("*** Step 4 Expected Results: - Verify filter procedure match with procedure table");
		assertTrue(getTableTotalRowSize(testCode.payorGroupModifiersTable()) <= numRows, "        Search result should be empty.");
		assertTrue(getColumnValue(testCode.payorGroupModifiersTable(), modTable), "        Procedure table only show filter modifier");

		logger.info("*** Step 5 Action: - Add modifier and filter modifier table with MODIFIER 2");
		modTable =  modifierAdded.get(0).get(3);
		testCode.clearAllGrpPayorFilter();
		testCode.inputModTableFilterMod2(modTable);

		logger.info("*** Step 5 Expected Results: - Verify filter procedure match with procedure table");
		assertTrue(getTableTotalRowSize(testCode.payorGroupModifiersTable()) <= numRows, "        Search result should be empty.");
		assertTrue(getColumnValue(testCode.payorGroupModifiersTable(), modTable), "        Procedure table only show filter modifier");

		logger.info("*** Step 6 Action: - Add modifier and filter modifier table with EXP DATE");
		modTable =  modifierAdded.get(0).get(4);
		testCode.clearAllGrpPayorFilter();
		testCode.inputModTableFilterExprDate(modTable);

		logger.info("*** Step 6 Expected Results: - Verify filter procedure match with procedure table");
		assertTrue(getTableTotalRowSize(testCode.payorGroupModifiersTable()) <= numRows, "        Search result should be empty.");
		assertTrue(getColumnValue(testCode.payorGroupModifiersTable(), modTable), "        Procedure table only show filter modifier");
		
		testCode.clickResetBtn();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Filter in Payor Specific Modifiers table")
	public void testXPR_203() throws Exception {
		logger.info("===== Testing - testXPR_203 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> singleTestID = daoManagerXifinRpm.getSingleTest(testDb);
		String TestID = "KRU472";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");

		logger.info("*** Step 3 Action: - Input valid effective date");
		scrollToElement(0, -250);
		String minEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		String effDate = testCodeUtils.getNextDay(minEffDate); 
		String expDate = testCodeUtils.getNextDay(effDate); 
		testCode.clickCreateEffDateIcon();
		testCode.setEffectiveDate(effDate);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Action: - Add some modifier to table");
		List<List<String>> listModifier = testCode.addModifiers(2,this,expDate);
		List<String> singleListModifier = listModifier.get(0);
		String payorID   = singleListModifier.get(0);
		String modifier1 = singleListModifier.get(1);
		String modifier2 = singleListModifier.get(2);
		String expdate   = singleListModifier.get(3);

		logger.info("*** Step 4 Expected Results: - All data will display on table");
		assertTrue(isElementPresent(testCode.payorSpecificModifiersTable(), 5),"        Payor Specific modifier table must be available");
		assertTrue(getColumnValue(testCode.payorSpecificModifiersTable(), payorID), "        New record must be added in table");

		logger.info("*** Step 5 Action: - Check function filter in Payor Specific Modifiers table : Input valid PayorID");
		assertTrue(isElementPresent(testCode.payorIDFilter(), 5),"        Payor id filter must be available");
		testCode.inputpayorIDFilter(payorID);
		
		logger.info("*** Step 5 Expected Results: - System will filter base on payor ID");
		assertTrue(getColumnValue(testCode.payorSpecificModifiersTable(), payorID), "        payorID must be displayed in table");
		testCode.clearData(testCode.payorIDFilter());

		logger.info("*** Step 6 Action: - Check function filter in Payor Specific Modifiers table : Input valid Modifier 1 filter");
		assertTrue(isElementPresent(testCode.modifier1Filter(), 5),"        Modifier 1 filter must be available");
		testCode.inputModifier1Filter(modifier1);
		
		logger.info("*** Step 6 Expected Results: - System will filter base on Modifier 1 ");
		assertTrue(getColumnValue(testCode.payorSpecificModifiersTable(), modifier1), "        modifier1 must be displayed in table");
		testCode.clearData(testCode.modifier1Filter());

		logger.info("*** Step 7 Action: - Check function filter in Payor Specific Modifiers table : Input valid Modifier 2 filter");
		assertTrue(isElementPresent(testCode.modifier2Filter(), 5),"        Modifier 2 filter must be available");
		testCode.inputModifier2Filter(modifier2);
		
		logger.info("*** Step 7 Expected Results: - System will filter base on Modifier 2 ");
		assertTrue(getColumnValue(testCode.payorSpecificModifiersTable(), modifier2), "        modifier2 must be displayed in table");
		testCode.clearData(testCode.modifier2Filter());

		logger.info("*** Step 8 Action: - Check function filter in Payor Specific Modifiers table : Input valid Exp Date filter");
		assertTrue(isElementPresent(testCode.expirationDateFilter(), 5),"        Exp date filter must be available");
		testCode.inputExpirationDateFilter(expdate);
		
		logger.info("*** Step 8 Expected Results: - System will filter base on exp date ");
		assertTrue(getColumnValue(testCode.payorSpecificModifiersTable(), expdate), "        expdate must be displayed in table");
		testCode.clearData(testCode.expirationDateFilter());
		
		testCode.clickResetBtn();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Cancel add new payor group")
	public void testXPR_244() throws Exception {
		logger.info("===== Testing - testXPR_244 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("***Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"         Test label must be available");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input a Invalid Test ID and Tab out");
		String testCodeId = String.valueOf(System.currentTimeMillis());
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeIdWithNewTestID(testCodeId);

		logger.info("*** Step 2 Expected Results: - 'Create Option' screen appear");
		assertTrue(isElementPresent(testCode.createScreen(), 5), "        Create Screen should be available.");

		logger.info("*** Step 3 Action: - Choose Single Test option");
		assertTrue(isElementPresent(testCode.singleTestRadio(), 5),"        Single test radio button must be available");
		selectCheckBox(testCode.singleTestRadio());
		assertTrue(isElementPresent(testCode.createOptionOKBtn(), 5),"        Ok button on create test code option must be available");
		testCode.clickCreateOption();

		logger.info("*** Step 3 Expected Results: - Test Code Information (Single Test) appear");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		assertTrue(isElementPresent(testCode.effectiveDatelabel(), 5), "        Effective Date should be available.");		

		logger.info("*** Step 4 Action: - Enter Name and Effective Date");
		assertTrue(isElementPresent(testCode.testNameInput(), 5),"        Test name input must be available");
		testCode.setTestName("TESTNAME");
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"        Add effdate must be available");
		String effDate = timeStamp.getCurrentDate("MM/dd/yyyy"); 
		testCode.setEffectiveDate(effDate);

		logger.info("*** Step 4 Expected Result: - Verify that tab list is displayed");
		assertTrue(isElementPresent(testCode.modifierTab(), 5), "        Modifier tab should be displayed");

		logger.info("*** Step 5 Action: - Add group payor valid data without save. Click on Cancel button");
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.addNewGroupPayorButton(), 5),"        Add new payor button must be available");
		testCode.clickAddGroupPayorBt();
		String payorGroupId = daoManagerXifinRpm.getGroupPayor(testDb).get(1);
		assertTrue(isElementPresent(testCode.GroupIdTypeModifier(), 5),"        Group Id type modifier must be available");
		testCode.selectGroupIdModifier(payorGroupId);
		assertTrue(isElementPresent(testCode.groupModifier1DropDown(), 5),"        Group modifier 1 dropdown must be available");
		testCode.selectGroupModifier(true, 2); // Select modifier 1
		assertTrue(isElementPresent(testCode.groupModifier2DropDown(), 5),"        Group modifier 2 must be available");
		testCode.selectGroupModifier(false, 2);// Select modifier 2
		assertTrue(isElementPresent(testCode.expDateOnAddNewPayor(), 5),"        Exp date on add new payor must be available");
		testCode.setExpDateOnAddNewPayor(testCodeUtils.getNextDay(effDate));
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button must be available");

		testCode.clickCancelAddPayorGroup();

		logger.info("*** Step 5 Expected Results: - New record popup is closed and new modifier is not displayed in the table");
		assertTrue(isElementHidden(testCode.addPayorGroupDialog(), 5),"        Add payor dialog must close");
		assertFalse(getColumnValue(testCode.payorGroupModifiersTable(), payorGroupId),"        Payor group mustn't add to table");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Reset add payor group")
	public void testXPR_245() throws Exception {
		logger.info("===== Testing - testXPR_245 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("***Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"         Test label must be available");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input a valid Test ID and Tab out");

//		String testCodeId = daoManagerXifinRpm.getSingleTest(testDb).get(1);
		String testCodeId ="VE455IT";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(testCodeId);

		logger.info("*** Step 2 Expected Results: - Test Code Information (Single Test) appear");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		assertTrue(isElementPresent(testCode.modifierTab(), 5), "        Modifier tab should be displayed");

		logger.info("*** Step 3 Action: - Add payor group");
		String payorGroupName = testCode.addGroupModifiers(1, this, "",true,testDb).get(0).get(1);

		logger.info("*** Step 3 Expected Results: - New payor group added to table");
		assertTrue(getColumnValue(testCode.payorGroupModifiersTable(), payorGroupName),"        Payor group must add to table");

		logger.info("*** Step 4 Action: - Click Reset button");
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);

		logger.info("*** Step 4 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 5 Action: - Load again test ID before");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(testCodeId);

		logger.info("*** Step 5 Expected Results: - Test Code Information (Single Test) appear");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");

		logger.info("*** Step 6 Action: - Switch to modifier tab");
		testCode.clickModifierTab();

		logger.info("*** Step 6 Expected Results: - Payor group had been added before not saved into the corresponding testID");
		assertFalse(getColumnValue(testCode.payorGroupModifiersTable(), payorGroupName),"        Payor group had been added before should not saved into the corresponding testID");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Add Payor with invalid data")
	public void testXPR_246() throws Exception {
		logger.info("===== Testing - testXPR_246=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		randomCharacter = new RandomCharacter();

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> singleTestID = daoManagerXifinRpm.getSingleTest(testDb);
		String TestID = "66497";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");

		logger.info("*** Step 3 Action: - Modifier tab - click on add icon");
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.addNewPayorIDButton(), 5),"        Add new payor id button must be available");
		testCodeUtils.scrollIntoView(testCode.addNewPayorIDButton());
		testCode.clickAddPayorIDBt();

		logger.info("*** Step 3 Expected Results: - Add record popup is displayed");
		assertTrue(isElementPresent(testCode.addNewPayorPopup(), 5), "        Add new payor popup must be available");

		logger.info("*** Step 4 Action: - Input invalid payorID");
		String payorID = randomCharacter.getRandomNumericString(2);
		assertTrue(isElementPresent(testCode.payorIdModifiersInput(), 5),"         Payor id modifier input must be available");
		testCode.setPayorIdModifiersInput(payorID);

		logger.info("*** Step 4 Expected Results: - The error message 'Payor ID xxx was not found.' is displayed");
		assertTrue(testCode.procedureDialogFormError().getText().trim().contains("Payor ID "+payorID+" was not found."),"        Page Title should be 'Payor ID "+payorID+" was not found.'");							

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Add Payor with some require fields are empty")
	public void testXPR_247() throws Exception {
		logger.info("===== Testing - testXPR_247=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> singleTestID = daoManagerXifinRpm.getSingleTest(testDb);
		String TestID = "TY961CX";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");

		logger.info("*** Step 3 Action: - Modifier tab - click on add icon");
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.addNewPayorIDButton(), 5),"        Add new payor id button must be available");
		testCodeUtils.scrollIntoView(testCode.addNewPayorIDButton());
		testCode.clickAddPayorIDBt();

		logger.info("*** Step 3 Expected Results: - Add record popup is displayed");
		assertTrue(isElementPresent(testCode.addNewPayorPopup(), 5), "        Add new payor popup must be available");

		logger.info("*** Step 4 Action: - Leave all field as blank and click ok button");
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button must be available");
		testCode.clickOkCompBtn();

		logger.info("*** Step 4 Expected Results: - Error message 'Payor ID: Field is required' will display");
		assertTrue(testCode.procedureDialogFormError().getText().trim().contains("Payor ID: Field is required"),"        Error message should be 'Payor ID: Field is required'");

		logger.info("*** Step 5 Action: - Input valid payorID, leave modifier as blank and click ok button");
		String payorID = daoManagerXifinRpm.getPayorID(testDb);
		assertTrue(isElementPresent(testCode.payorIdModifiersInput(), 5),"         Payor id modifier input must be available");
		testCode.setPayorIdModifiersInput(payorID);
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button must be available");
		testCode.clickOkCompBtn();

		logger.info("*** Step 5 Expected Results: - The error message 'Modifier: Field is required' is displayed");
		assertTrue(testCode.procedureDialogFormError().getText().trim().contains("Modifier: Field is required"),"        Error message should be 'Modifier: Field is required'");							

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Cancel the add new payor")
	public void testXPR_248() throws Exception {
		logger.info("===== Testing - testXPR_248 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String singleTest = "VUW270";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - The Single test page is displayed");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Add modifier and filter modifier table with GROUP ID");
		testCode.clickModifierTab();
		int numRowsBefore = getTableTotalRowSize(testCode.payorGroupModifiersTable());
		testCode.clickAddGroupPayorBt();

		logger.info("*** Step 3 Expected Results: - The new record popup is displayed");
		String popupDisplay = testCode.addPayorGroupDialog().getCssValue("display");
		assertEquals(popupDisplay, "block", "        Popup must be display");

		logger.info("*** Step 4 Action: - Input all valid data to popup and click on cancel button");
		assertTrue(isElementPresent(testCode.GroupIdTypeModifier(), 5), "      Group ID dropdown must be available");
		assertTrue(isElementPresent(testCode.groupModifier1DropDown(), 5), "      Modifier 1 dropdown must be available");
		assertTrue(isElementPresent(testCode.groupModifier2DropDown(), 5),"      Modifier 2 dropdown must be available");
		assertTrue(isElementPresent(testCode.expirationDatePayorGroup(), 5), "      Expiration Date dropdown must be available");
		String minEffDate = testCode.selectEffDate().getAttribute("value").trim();
		String effDateInput = testCodeUtils.getNextYear(minEffDate);
		List<List<String>> input = testCode.addGroupModifiers(1, this, effDateInput, false, testDb);
		int numRowsAfter = getTableTotalRowSize(testCode.payorGroupModifiersTable());

		logger.info("*** Step 4 Expected Results: - Popup is closed and new payor is cancel");
		popupDisplay = testCode.addPayorGroupDialog().getCssValue("display");
		assertEquals(popupDisplay, "none","        Popup must be close");
		assertEquals(numRowsAfter, numRowsBefore,"        Row after must equal row before");
		assertFalse(getColumnValue(testCode.payorGroupModifiersTable(), input.get(0).get(1)), "        Payor Group ID must not displayed in table");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Reset Add Payor information")
	public void testXPR_249() throws Exception {
		logger.info("===== Testing - testXPR_249 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest = "MDY839";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test information page is displayed");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Add modifier and filter modifier table with GROUP ID");
		testCode.clickModifierTab();
		int numRowsBefore = getTableTotalRowSize(testCode.payorGroupModifiersTable());
		testCode.clickAddGroupPayorBt();

		logger.info("*** Step 3 Expected Results: - Add new record page is displayed");
		String popupDisplay = testCode.addPayorGroupDialog().getCssValue("display");
		assertEquals(popupDisplay, "block", "        Popup must be display");

		logger.info("*** Step 4 Action: - Input all valid data to popup and click ok button");
		assertTrue(isElementPresent(testCode.GroupIdTypeModifier(), 5), "      Group ID dropdown must be available");
		assertTrue(isElementPresent(testCode.groupModifier1DropDown(), 5), "      Modifier 1 dropdown must be available");
		assertTrue(isElementPresent(testCode.groupModifier2DropDown(), 5),"      Modifier 2 dropdown must be available");
		assertTrue(isElementPresent(testCode.expirationDatePayorGroup(), 5), "      Expiration Date dropdown must be available");
		String minEffDate = testCode.selectEffDate().getAttribute("value").trim();
		String effDateInput = testCodeUtils.getNextYear(minEffDate);
		List<List<String>> input = testCode.addGroupModifiers(1, this, effDateInput, true, testDb);
		int numRowsAfter = getTableTotalRowSize(testCode.payorGroupModifiersTable());

		logger.info("*** Step 4 Expected Results: - Popup is closed and new payor id is displayed in table");
		popupDisplay = testCode.addPayorGroupDialog().getCssValue("display");
		assertEquals(popupDisplay, "none","        Popup must be close");
		assertNotEquals(numRowsAfter, numRowsBefore,"        Row after must be not equal row before");
		assertTrue(getColumnValue(testCode.payorGroupModifiersTable(), input.get(0).get(1)), "        Payor Group ID must not displayed in table");

		logger.info("*** Step 5 Action: - Click reset button and dismiss alert popup");
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);

		logger.info("*** Step 5 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 6 Action: - Load again test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest);
		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 6 Expected Results: - New payor id is not save into database");
		int numRowsCurrent = getTableTotalRowSize(testCode.payorGroupModifiersTable());
		assertEquals(numRowsBefore, numRowsCurrent,"        Number row before must be equal row current");
		assertFalse(getColumnValue(testCode.payorGroupModifiersTable(), input.get(0).get(1)), "        Payor Group ID must not displayed in table");

		testCode.clickResetBtn();
	}

	@Test(priority=1, description="Single Test Detail - Modifier - Cancel the update payor group")
	public void testXPR_250() throws Exception{
		logger.info("===== Testing - testXPR_250 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input valid single test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		String testCodeInfo = "AQ083LN";
		testCode.checkInputTestCodeId(testCodeInfo);

		logger.info("*** Step 2 Expected Results: - Single test information page is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 3 Action: - Add group modifier with valid data");
		List<List<String>> payorGroupInfo =  testCode.addGroupModifiers(1, this,"",true,testDb);

		logger.info("*** Step 3 Expected Results: - PayorGroup will be added in Payor Group Table");
		assertTrue(isElementPresent(testCode.payorGroupModifiersTable(), 5),"        Payor group modifier table must be available");
		assertTrue(getColumnValue(testCode.payorGroupModifiersTable(), payorGroupInfo.get(0).get(1)),"        Payor group must added to table");
		selectColumnValue(testCode.payorGroupModifiersTable(), payorGroupInfo.get(0).get(1));

		logger.info("*** Step 4 Action: - Click edit button. Edit Payor Group and click cancel button");
		assertFalse(testCodeUtils.isListEmpty(daoManagerXifinRpm.getGroupPayor(testDb)),"        Payor Group should have data to test");
		String newPayorGroupId = testCode.getNewPayorGroupIdInDb(this, testDb).get(1);
		testCode.selectGroupIdModifier(newPayorGroupId);
		testCode.selectGroupModifier(true, 2); // Select modifier 1
		testCode.selectGroupModifier(false, 2);// Select modifier 2
		assertTrue(isElementPresent(testCode.cancelBtn(), 5),"        Cancel button must be available");
		testCode.clickCancelPayorGrpBtn();

		logger.info("*** Step 4 Expected Results: - The update information is not displayed in table");
		assertTrue(isElementPresent(testCode.payorGroupModifiersTable(), 5),"        Payor group modifier table must be available");
		assertFalse(getColumnValue(testCode.payorGroupModifiersTable(), newPayorGroupId),"         The update information should not be displayed in table");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Modifier - Reset the update payor group")
	public void testXPR_251() throws Exception{
		logger.info("===== Testing - testXPR_251 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input not available test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		String testCodeInfo = "WW408EP";
		testCode.checkInputTestCodeId(testCodeInfo);

		logger.info("*** Step 2 Expected Results: - Single test information page is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 3 Action: - Add Modifier");
		assertTrue(isElementPresent(testCode.selectEffDate(), 5),"        Select eff date must be available");
		String currentDay = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		List<List<String>> payorGroupInfo =  testCode.addGroupModifiers(1, this,"",true,testDb);

		logger.info("*** Step 3 Expected Results: - Payor Group Modifier will be added in Payor Group Table");
		assertTrue(isElementPresent(testCode.payorGroupModifiersTable(), 5),"        Payor group modifier table must be available");
		assertTrue(getColumnValue(testCode.payorGroupModifiersTable(), payorGroupInfo.get(0).get(1)),"        Payor group must be added to table");

		logger.info("*** Step 4 Action: - Select any row. Edit Payor Group and click Cancel button");
		selectColumnValue(testCode.payorGroupModifiersTable(), payorGroupInfo.get(0).get(1));
		assertFalse(testCodeUtils.isListEmpty(daoManagerXifinRpm.getGroupPayor(testDb)),"        Payor Group should have data to test");
		List<String> newPayorGroupInfo = testCode.getNewPayorGroupIdInDb(this, testDb);
		testCode.selectGroupIdModifier(newPayorGroupInfo.get(1));
		testCode.selectGroupModifier(true, 2); // Select modifier 1
		testCode.selectGroupModifier(false, 2);// Select modifier 2
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button must be available");
		testCode.clickOkPayorGrpBtn();

		logger.info("*** Step 4 Expected Results: - The update information is displayed in table");
		assertTrue(isElementPresent(testCode.payorGroupModifiersTable(), 5),"        Payor group modifier table must be available");
		assertTrue(getColumnValue(testCode.payorGroupModifiersTable(), newPayorGroupInfo.get(1)),"         The update information should be displayed in table");

		logger.info("*** Step 5 Action: - Click Reset Button");
		assertTrue(isElementPresent(testCode.resetBtn(), 5),"        Reset button must be available");
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);

		logger.info("*** Step 5 Expected Results: - The update information is not saved in DB");
		String testCodeId = String.valueOf(testDao.getTestByTestAbbrev(testCodeInfo).getTestId());
		String payorGroupId = newPayorGroupInfo.get(0);
		boolean isSave = daoManagerXifinRpm.checkTestPyrGrpModInDb(payorGroupId,testCodeId,currentDay,testDb);
		assertFalse(isSave,"        The update information is not saved in DB");
	}

	@Test(priority=1, description="Single Test Detail - Modifier - Click Edit icon without select any row payor group")
	public void testXPR_252() throws Exception{
		logger.info("===== Testing - testXPR_252 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input not available test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		String testCodeInfo = "JJ472PY";
		testCode.checkInputTestCodeId(testCodeInfo);

		logger.info("*** Step 2 Expected Results: - Single test page information is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 3 Action: - Add Modifier");
		List<List<String>> payorGroupInfo =  testCode.addGroupModifiers(1, this, "", true,testDb);

		logger.info("*** Step 3 Expected Results: - Payor Group Modifier will be added in Payor Group Table");
		assertTrue(isElementPresent(testCode.payorGroupModifiersTable(), 5),"        Payor group modifier table must be available");
		assertTrue(getColumnValue(testCode.payorGroupModifiersTable(), payorGroupInfo.get(0).get(1)),"         Payor group must be added to table");

		logger.info("*** Step 4 Action: - Select any row without select any row");
		assertTrue(isElementPresent(testCode.editPayorGrpModifierButton(), 5),"        Edit payor group modifier button must available");
		testCode.clickeditPayorGrpModifierButton();

		logger.info("*** Step 4 Expected Results: - The error message popup 'Please select any row' id displayed");
		assertTrue(isElementPresent(testCode.alertPyrGrpModTable(), 5),"        Error message must be display");
		assertTrue(testCode.alertPyrGrpModTable().getText().contains("Please, select row"),"        The error message popup 'Please, select row' is displayed");

		assertTrue(isElementPresent(testCode.WarningCloseBtn(), 5), "        Close Message error button must be displayed");
		testCode.clickWarningCloseBtn();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Modifier - Add Payor Group with Duplicate GroupID")
	public void testXPR_256() throws Exception{
		logger.info("===== Testing - testXPR_256 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input not available test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		String testCodeInfo = "SCM443";
		testCode.checkInputTestCodeId(testCodeInfo  );

		logger.info("*** Step 2 Expected Results: - Single Test page information is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 3 Action: - Input valid Expiration Date");
		List<List<String>> payorGroupInfo =  testCode.addGroupModifiers(1, this, "", true, testDb);

		logger.info("*** Step 3 Expected Results: - PayorGroup will be added in Payor Group Table");
		assertTrue(isElementPresent(testCode.payorGroupModifiersTable(), 5),"        Payor group modifier table must be available");
		assertTrue(getColumnValue(testCode.payorGroupModifiersTable(), payorGroupInfo.get(0).get(1)),"        Payor group must be added to table");

		logger.info("*** Step 4 Action: - Input Duplicate Group ID is duplicate with existing Group ID in the list");
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.addNewGroupPayorButton(), 5),"        Add new payor button must be available");
		testCode.clickAddGroupPayorBt();
		testCode.selectGroupIdModifier(payorGroupInfo.get(0).get(1));
		testCode.selectGroupModifier(true, 2); // Select modifier 1
		testCode.selectGroupModifier(false, 2);// Select modifier 2
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button must be available");
		testCode.clickOkPayorGrpBtn();

		logger.info("*** Step 4 Expected Results: - System will display Error message");
		assertTrue(isElementPresent(testCode.payorGroupDialogFormError(), 5),"         Form error must be display");
		assertTrue(testCode.payorGroupDialogFormError().getText().contains("A duplicate record was entered"),"        System will display error message : A duplicate record was entered");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Add group payor with exp date before Eff date")
	public void testXPR_257() throws Exception {
		logger.info("===== Testing - testXPR_257 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("***Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"         Test label must be available");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input a valid Test ID and Tab out");
		String testCodeId = "QCS025";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(testCodeId);

		logger.info("*** Step 2 Expected Results: - Test Code Information (Single Test) appear");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Add group payor with exp date before eff date");
		testCode.addGroupModifiers(1,this, testCodeUtils.getPreviousDay(testCode.selectEffDate().getAttribute("value").trim()), true,testDb);

		logger.info("*** Step 3 Expected Results: - Error message exp date must be after eff date");
		assertTrue(isElementPresent(testCode.payorSpecificmodifierDialogFormError(), 5),"        Error message must be display");
		assertTrue(testCode.payorSpecificmodifierDialogFormError().getText().equalsIgnoreCase("Expiration date must be after effective date"),"        Message error must alert 'Expiration date must be after effective date'");
		
		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Add Payor with PayorID is duplicate with existing payorID")
	public void testXPR_258() throws Exception {
		logger.info("===== Testing - testXPR_258=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> singleTestID = daoManagerXifinRpm.getSingleTest(testDb);
		String TestID = "RE125ST";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test information page is displayed");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");

		logger.info("*** Step 3 Action: - Input valid effective date");
		scrollToElement(0, -250);
		String minEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		String effDateInput = testCodeUtils.getNextDay(minEffDate); 
		testCode.clickCreateEffDateIcon();
		testCode.setEffectiveDate(effDateInput);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Action: - Modifier tab - click on add icon");
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.addNewPayorIDButton(), 5),"        Add new payor id button must be available");
		testCodeUtils.scrollIntoView(testCode.addNewPayorIDButton());
		testCode.clickAddPayorIDBt();

		logger.info("*** Step 4 Expected Results: - Add record popup is displayed");
		assertTrue(isElementPresent(testCode.addNewPayorPopup(), 5), "        Add new payor popup must be available");

		logger.info("*** Step 5 Action: - Input valid payorID, modifier and click ok button");
		String payorID = daoManagerXifinRpm.getPayorID(testDb);
		assertTrue(isElementPresent(testCode.payorIdModifiersInput(), 5),"         Payor id modifier input must be available");
		testCode.setPayorIdModifiersInput(payorID);
		assertTrue(isElementPresent(testCode.modifierDopdownOnAddPayorID(), 5),"        Modifier dropdown must be available");
		String modifier = daoManagerXifinRpm.getRandomModId(testDb);
		testCode.setModifierDopdownOnAddPayorID(modifier);
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button must be available");
		testCode.clickOkCompBtn();
		
		logger.info("*** Step 5 Expected Results: - New payor modifier is added in payor modifier table");
	    assertTrue(isElementPresent(testCode.payorSpecificModifiersTable(), 5),"        payor table must be available");
		assertTrue(getColumnValue(testCode.payorSpecificModifiersTable(), payorID),"        New payor modifier is added in payor modifier table");

		logger.info("*** Step 6 Action: - Input existing payorID in the list");
		assertTrue(isElementPresent(testCode.addNewPayorIDButton(), 5),"        Add new payor id button must be available");
		testCode.clickAddPayorIDBt();
		assertTrue(isElementPresent(testCode.payorIdModifiersInput(), 5),"         Payor id modifier input must be available");
		testCode.setPayorIdModifiersInput(payorID);
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button must be available");
		testCode.clickOkCompBtn();

		logger.info("*** Step 6 Expected Results: - The error message 'A duplicate record was entered' is displayed");
		waitUntilElementIsNotVisible(testCode.procedureDialogFormError(), 5);
		assertTrue(testCode.procedureDialogFormError().getText().trim().contains("A duplicate record was entered"),"        Error message should be 'A duplicate record was entered'");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Add Payor with exp date is before eff date")
	public void testXPR_259() throws Exception {
		logger.info("===== Testing - testXPR_259=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> singleTestID = daoManagerXifinRpm.getSingleTest(testDb);
		String TestID = "TCC849";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test information page is displayed");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");

		logger.info("*** Step 3 Action: - Input valid effective date");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5),"        Select eff date must be available");
		String minEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		String effDate = testCodeUtils.getNextDay(minEffDate); 
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Create Effdate icon must be available");
		testCode.clickCreateEffDateIcon();
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"        Add effdate must be available");
		testCode.setEffectiveDate(effDate);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Action: Click on Modifier tab. click on add icon");
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.addNewPayorIDButton(), 5),"        Add new payor id button must be available");
		testCodeUtils.scrollIntoView(testCode.addNewPayorIDButton());
		testCode.clickAddPayorIDBt();

		logger.info("*** Step 4 Expected Results: - Add record popup is displayed");
		assertTrue(isElementPresent(testCode.addNewPayorPopup(), 5), "        Add new payor popup must be available");

		logger.info("*** Step 5 Action: - Input valid data to all field and select Modifier exp date is after before date and click ok button");
		String payorID = daoManagerXifinRpm.getPayorID(testDb);
		assertTrue(isElementPresent(testCode.payorIdModifiersInput(), 5),"         Payor id modifier input must be available");
		testCode.setPayorIdModifiersInput(payorID);
		assertTrue(isElementPresent(testCode.modifierDopdownOnAddPayorID(), 5),"        Modifier dropdown must be available");
		String modifier = daoManagerXifinRpm.getRandomModId(testDb);
		testCode.setModifierDopdownOnAddPayorID(modifier);
		String expDate = testCodeUtils.getPreviousDay(effDate);
		assertTrue(isElementPresent(testCode.expDateOnAddNewPayor(), 5),"        Exp date must be available");
		testCode.setExpDateOnAddNewPayor(expDate);
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button must be available");
		testCode.clickOkCompBtn();

		logger.info("*** Step 5 Expected Results: - The error message 'Expiration date must be after effective date'");
		assertTrue(isElementPresent(testCode.procedureDialogFormError(), 5),"        procedure Dialog FormError should show");
		assertTrue(testCode.procedureDialogFormError().getText().trim().contains("Expiration date must be after effective date"),"        Error message should be 'Expiration date must be after effective date'");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Reset delete payor group")
	public void testXPR_264() throws Exception {
		logger.info("===== Testing - testXPR_264 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("***Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"         Test label must be available");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input a valid Test ID and Tab out");
		String testCodeId = "AUTOTESTXPR_264";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.createSingleTest(testCodeId, testDb,false,false,this);
		daoManagerPlatform.deletePyrGrpModFromTESTPYRGRPMODByTestAbbrev(testCodeId, testDb);

		logger.info("*** Step 2 Expected Results: - Test Code Information (Single Test) is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		assertTrue(isElementPresent(testCode.modifierTab(), 5), "        Modifier tab should be displayed");

		logger.info("*** Step 3 Action: - Add valid payor group to modifiers");
		String payorName = testCode.addGroupModifiers(1,this, "", true, testDb).get(0).get(1);

		logger.info("*** Step 3 Expected Results: - Payor group is added into table modifiers");
		assertTrue(getColumnValue(testCode.payorGroupModifiersTable(), payorName),"        Payor group is added into table modifiers");

		logger.info("*** Step 4 Action: - Save all and load again single test");
		testCode.clickSaveAndClearBtn();
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.inputTestCodeId(testCodeId);
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();

		logger.info("*** Step 4 Expected Results: - Verify payor group had been added before is saved");
		assertTrue(getColumnValue(testCode.payorGroupModifiersTable(), payorName),"        Payor group is added into table modifiers");

		logger.info("*** Step 5 Action: - Edit Payor added");
		selectColumnValue(testCode.payorGroupModifiersTable(), payorName);

		logger.info("*** Step 5 Expected Results: - Verify payor edit dialog display");
		assertTrue(isElementPresent(testCode.editPayorGroupDialog(), 5),"        Payor edit dialog must be display");

		logger.info("*** Step 6 Action: - Delete payor group had been added before");
		selectCheckBox(testCode.deletePayorCheckBox());
		testCode.clickOkOnEditModifierGroup();

		logger.info("*** Step 6 Expected Results: - Verify payor group had been mark to remove from table");
		assertTrue(testCodeUtils.hasClass(testCode.getRowOfPayorGroupSpecificModifiersTable(testCode.getRowIndex(testCode.payorGroupModifiersTable(), payorName)-1), "rowMarkedForDelete"),"        Payor group must be marked to remove from table");

		logger.info("*** Step 7 Action: - Click reset button and reload all single test.");
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(testCodeId);
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();

		logger.info("*** Step 7 Expected Results: - Verify payor group had been marked delete before is still display in table");
		assertTrue(getColumnValue(testCode.payorGroupModifiersTable(), payorName),"        Payor group had been marked delete before must be display in table");
		
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Delete Payor - Reset the delete payor")
	public void testXPR_265() throws Exception {
		logger.info("===== Testing - testXPR_265=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
		String testId = "TESTXPR_265";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.createSingleTest(testId, testDb,false,false,this);

		logger.info("*** Step 2 Expected Results: - Single Test information page is displayed");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");

		logger.info("*** Step 3 Action: - Select any row and click edit button");
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();
		String payorId =   testCode.checkPayorSpecificModifiers(testId, testDb);
		assertTrue(isElementPresent(testCode.payorSpecificModifiersTable(), 5),"        Payor Specific modifier table must be available");
		testCodeUtils.scrollIntoView(testCode.payorSpecificModifiersTable());
		doubleClickHiddenPageObject(testCode.payorSpecificModifiersTable(), 0);

		logger.info("*** Step 3 Expected Result: - Edit record popup is displayed");
		assertTrue(isElementPresent(testCode.addNewPayorPopup(), 5), "        Edit new payor popup must be available");

		logger.info("*** Step 4 Action: - Select delete checkbox and click OK button");
		assertTrue(isElementPresent(testCode.deletePayorCheckBox(), 5),"        Delete payor checkbox must be available");
		selectCheckBox(testCode.deletePayorCheckBox());
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button must be available");
		testCode.clickOkButtonOnAddNewPayorID();

		logger.info("*** Step 4 Expected Result: - Selected row will mark for delete");
		testCodeUtils.hasClass(testCode.payorSpecificModifiersTable(), "rowMarkedForDelete");

		logger.info("*** Step 5 Action: - Click Reset button");
		assertTrue(isElementPresent(testCode.resetBtn(), 5),"        Reset button must be available");
		testCode.clickResetBtn();
		String confirm  = closeAlertAndGetItsText(true);
		
		logger.info("*** Step 5 Expected Result: - Confirm message is displayed");
		assertNotNull(confirm, "        Confirm message is displayed");

		logger.info("*** Step 6 Action: - input TestID in step 2 ");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(testId);

		logger.info("*** Step 6 Expected Result: - Single test will display ");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");

		logger.info("*** Step 7 Action: - Navigate to modifier tab");
		assertTrue(isElementPresent(testCode.modifierTab(), 5),"        Modifier tab must be available");
		testCode.clickModifierTab();

		logger.info("*** Step 7 Expected Result: - The selected row is not deleted and still kept shown in the list");
		assertTrue(isElementPresent(testCode.payorSpecificModifiersTable(), 5),"        Payor Specific modifier table must be available");
		testCodeUtils.scrollIntoView(testCode.payorSpecificModifiersTable());
		assertTrue(getColumnValue(testCode.payorSpecificModifiersTable(), payorId), "        The selected row is not deleted and still kept shown in the list");

		testCode.clickResetBtn();
	}

	//Modifier - Search Payor
	@Test(priority = 1, description = "Single Test Detail - Modifier - Search Payor - Search Payor info with invalid data")
	public void testXPR_109() throws Exception {
		logger.info("===== Testing - testXPR_109 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		payorSearch = new PayorSearch(driver, config);
		payorSearchResults = new PayorSearchResults(driver,wait);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest = "1582369562850";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click Modifier Tab. Click Add button. Click search payor ID.");
		assertTrue(isElementPresent(testCode.modifierTab(), 5), "        Modifier tab must be available");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.addPayorSpecBtn(), 5), "        Add Payor Specific must be available");
		testCodeUtils.scrollIntoView(testCode.addPayorSpecBtn());
		testCode.clickAddPayorSpecBtn();
		assertTrue(isElementPresent(testCode.searchPayorIDBtn(), 5), "        Search payor button must be available");
		testCode.clickSearchPayorIDBtn();
		String winHandler = driver.getWindowHandle();
		switchToPopupWin();

		logger.info("*** Step 3 Expected Results: - Add New Record Popup must be displayed. New popup window must be displayed");
		assertTrue(isElementPresent(payorSearch.payorIDTextInput(), 5), "        New popup window must be displayed");
		assertTrue(isElementPresent(payorSearch.searchBtn(), 5), "        Submit button must be displayed");
		String getAttribute = payorSearch.searchBtn().getAttribute("data-user-action");
		assertTrue(getAttribute.contains("search/payorsearch.html"), "        Payor Search URL should contains 'search/payorsearch.html'.");

		logger.info("*** Step 4 Action: - Input invalid payorId. Click Search button");
		String payorID = testCode.generatePayorIDNotExist(testDb);
		payorSearch.enterPayorID(payorID);
		payorSearch.clickSearchBtn();

		logger.info("*** Step 4 Expected Results: - Search result is empty");
		assertTrue(isElementPresent(payorSearchResults.payorSearchResultTable(), 5), "        Payor Search result must be displayed");
		assertTrue(getTableTotalRowSize(payorSearchResults.payorSearchResultTable()) == 1, "        Search result is empty");
		assertFalse(getColumnValue(payorSearchResults.payorSearchResultTable(),payorID), "        Payor ID must not be displayed");

		//Switch back to the main window
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//Click the Cancel Button in the Popup window
		testCode.cancelBtn().click();
		//Click the Reset Button in the Test Code screen
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Search Payor - Search all Payor")
	public void testXPR_253() throws Exception {
		logger.info("===== Testing - testXPR_253 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		payorSearch = new PayorSearch(driver, config);
		payorSearchResults = new PayorSearchResults(driver,wait);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest ="EZ609KX";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click Modifier Tab. Click Add button. Click search payor ID.");
		assertTrue(isElementPresent(testCode.modifierTab(), 5), "        Modifier tab must be available");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.addPayorSpecBtn(), 5), "        Add payor specific must be available");
		testCodeUtils.scrollIntoView(testCode.addPayorSpecBtn());
		testCode.clickAddPayorSpecBtn();
		assertTrue(isElementPresent(testCode.searchPayorIDBtn(), 5), "        Search payor must be available");
		testCode.clickSearchPayorIDBtn();
		String winHandler = driver.getWindowHandle();
		switchToPopupWin();

		logger.info("*** Step 3 Expected Results: - Add New Record Popup must be displayed. New popup window must be displayed");
		assertTrue(isElementPresent(payorSearch.payorIDTextInput(), 5), "        New popup window must be displayed");
		assertTrue(isElementPresent(payorSearch.searchBtn(), 5), "        Submit button must be displayed");
		String getAttribute = payorSearch.searchBtn().getAttribute("data-user-action");
		assertTrue(getAttribute.contains("search/payorsearch.html"), "        Payor Search URL should contains 'search/payorsearch.html'.");

		logger.info("*** Step 4 Action: - Enter * in payorID field. Click Search button");
		assertTrue(isElementPresent(payorSearch.payorIDTextInput(), 5), "        Payor ID input must be available");
		payorSearch.enterPayorID("*");
		assertTrue(isElementPresent(payorSearch.searchBtn(), 5), "        Search button must be available");
		payorSearch.clickSearchBtn();
		int numberRows = testCode.getNumberPayor(testDb);

		logger.info("*** Step 4 Expected Results: - The PayorID matched input is displayed");
		assertTrue(testCode.isElementDisplayed(payorSearchResults.recordsCount(), 20), "        Records count view in Search Results should show.");
		assertEquals(testCode.numberOfPayorInResult(), numberRows,"        Number of Payor in Search Results should be matched.");

		//Switch back to the main window
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		//Click the Cancel Button in the Popup window
		testCode.cancelBtn().click();
		//Click the Reset Button in the Test Code screen
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Search Payor - Search Payor with valid data for Payor Info")
	public void testXPR_254() throws Exception {
		logger.info("===== Testing - testXPR_254 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		payorSearch = new PayorSearch(driver, config);
		payorSearchResults = new PayorSearchResults(driver,wait);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest = "KF938BY";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click Modifier Tab. Click Add button. Click search payor ID.");
		assertTrue(isElementPresent(testCode.modifierTab(),5),"        Modifier tab must be available.");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.addPayorSpecBtn(),5),"        Add payor button must be available.");
		testCodeUtils.scrollIntoView(testCode.addPayorSpecBtn());
		testCode.clickAddPayorSpecBtn();
		assertTrue(isElementPresent(testCode.searchPayorIDBtn(),5),"        Search button must be available.");
		testCode.clickSearchPayorIDBtn();
		String winHandler = driver.getWindowHandle();
		switchToPopupWin();

		logger.info("*** Step 3 Expected Results: - Add New Record Popup must be displayed. New popup window must be displayed");
		assertTrue(isElementPresent(payorSearch.payorIDTextInput(), 5), "        New popup window must be displayed");
		assertTrue(isElementPresent(payorSearch.searchBtn(), 5), "        Submit button must be displayed");
		String getAttribute = payorSearch.searchBtn().getAttribute("data-user-action");
		assertTrue(getAttribute.contains("search/payorsearch.html"), "        Payor Search URL should contains 'search/payorsearch.html'.");

		logger.info("*** Step 4 Action: - Input valid data for payor Info. Click Search button");
		List<String> data = daoManagerXifinRpm.getRandomAllPayorInfo(testDb);
		assertTrue(isElementPresent(payorSearch.payorIDTextInput(),5),"        Payor id input must be available.");
		payorSearch.enterPayorID(data.get(1));
		assertTrue(isElementPresent(payorSearch.payorNameTextInput(),5),"        Payor name input must be available.");
		payorSearch.enterPayorName(data.get(2));
		assertTrue(isElementPresent(payorSearch.payorGroupDropDown(),5),"        Payor group dropdown must be available.");
		selectItemByVal(payorSearch.payorGroupDropDown(), data.get(16));
		assertTrue(isElementPresent(payorSearch.clearingHouseIdTextInput(),5),"        Clearing House ID input must be available.");
		payorSearch.enterClearingHouseId(data.get(20));
		assertTrue(isElementPresent(payorSearch.searchBtn(),5),"        Search button must be available.");
		payorSearch.clickSearchBtn();
		wait.until(ExpectedConditions.invisibilityOf(testCode.loadPayorSearchTable()));

		logger.info("*** Step 4 Expected Results: - The PayorID that matched with Search condition  are displayed");
		assertTrue(isElementPresent(payorSearchResults.payorSearchResultTable(), 5), "        Payor Search result must be displayed");
		int totalRowSize = getTableTotalRowSize(payorSearchResults.payorSearchResultTable());
		assertTrue(totalRowSize >= 2, "        Search results should show.");
		assertTrue(getColumnValue(payorSearchResults.payorSearchResultTable(),data.get(1)), "        Payor ID must be displayed");

		//Switch back to the main window
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		//Click the Cancel Button in the Popup window
		testCode.cancelBtn().click();
		//Click the Reset Button in the Test Code screen
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Search Payor - Search Payor with full data for Cross-Reference")
	public void testXPR_255() throws Exception {
		logger.info("===== Testing - testXPR_255 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		payorSearch = new PayorSearch(driver, config);
		randomCharacter = new RandomCharacter();
		payorSearchResults = new PayorSearchResults(driver,wait);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest = "IC690YO";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click Modifier Tab. Click Add button. Click search payor ID.");
		assertTrue(isElementPresent(testCode.modifierTab(),5),"        Modifier tab must be available.");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.addPayorSpecBtn(),5),"        Add payor button must be available.");
		testCodeUtils.scrollIntoView(testCode.addPayorSpecBtn());
		testCode.clickAddPayorSpecBtn();
		assertTrue(isElementPresent(testCode.searchPayorIDBtn(),5),"        Search button must be available.");
		testCode.clickSearchPayorIDBtn();
		String winHandler = driver.getWindowHandle();
		switchToPopupWin();

		logger.info("*** Step 3 Expected Results: - Add New Record Popup must be displayed. New popup window must be displayed");
		assertTrue(isElementPresent(payorSearch.payorIDTextInput(), 5), "        New popup window must be displayed");
		assertTrue(isElementPresent(payorSearch.searchBtn(), 5), "        Submit button must be displayed");
		String getAttribute = payorSearch.searchBtn().getAttribute("data-user-action");
		assertTrue(getAttribute.contains("search/payorsearch.html"), "        Payor Search URL should contains 'search/payorsearch.html'.");

		logger.info("*** Step 4 Action: - Input valid data for all fields in Cross-Reference info section . Click Search button");
		payorSearch.selectXRefTypeByIndex(Integer.parseInt(testCodeUtils.remove0Number(randomCharacter.getRandomNumericString(1))));
		payorSearch.clickSearchBtn();

		logger.info("*** Step 4 Expected Results: - The PayorID that matched with search condition are displayed");
		assertTrue(isElementPresent(payorSearchResults.payorSearchResultTable(), 5), "        Payor Search result must be displayed");
		assertTrue(getTableTotalRowSize(payorSearchResults.payorSearchResultTable()) >= 1, "        Result of this search must be displayed");

		//Switch back to the main window
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//Click the Cancel Button in the Popup window
		testCode.cancelBtn().click();
		//Click the Reset Button in the Test Code screen
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Search Payor - Search Payor with full address info")
	public void testXPR_260() throws Exception {
		logger.info("===== Testing - testXPR_260 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		payorSearch = new PayorSearch(driver, config);
		payorSearchResults = new PayorSearchResults(driver,wait);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String singleTest = "JSS262";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click Modifier Tab. Click Add button. Click search payor ID.");
		assertTrue(isElementPresent(testCode.modifierTab(),5),"        Modifier tab must be available.");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.addPayorSpecBtn(),5),"        Add payor button must be available.");
		testCodeUtils.scrollIntoView(testCode.addPayorSpecBtn());
		testCode.clickAddPayorSpecBtn();
		assertTrue(isElementPresent(testCode.searchPayorIDBtn(),5),"        Search button must be available.");
		testCode.clickSearchPayorIDBtn();
		String winHandler = driver.getWindowHandle();
		switchToPopupWin();

		logger.info("*** Step 3 Expected Results: - Add New Record Popup must be displayed. New popup window must be displayed");
		assertTrue(isElementPresent(payorSearch.payorIDTextInput(), 5), "        New popup window must be displayed");
		assertTrue(isElementPresent(payorSearch.searchBtn(), 5), "        Submit button must be displayed");
		String getAttribute = payorSearch.searchBtn().getAttribute("data-user-action");
		assertTrue(getAttribute.contains("search/payorsearch.html"), "        Payor Search URL should contains 'search/payorsearch.html'.");

		logger.info("*** Step 4 Action: - Input valid data for address info section. Click Search button");
		List<String> data = daoManagerXifinRpm.getRandomAllPayorInfo(testDb);
		String pyrAddr1 = data.get(4);
		payorSearch.enterAddress(pyrAddr1);
		payorSearch.clickSearchBtn();		

		logger.info("*** Step 4 Expected Results: - The Payor address info  matched with search condition  are displayed");
		assertTrue(testCode.isElementDisplayed(payorSearchResults.recordsCount(), 10), "        Records count view in Search Results should show.");
		assertTrue(isElementPresent(payorSearchResults.payorSearchResultTable(), 5), "        Payor Search result must be displayed");
		assertTrue(getTableTotalRowSize(payorSearchResults.payorSearchResultTable()) >= 2, "        Result of this search must be displayed");
		assertEquals(payorSearchResults.address1InPayorSearchResults("2").getText().trim(), pyrAddr1, "        Address 1: " + pyrAddr1 + " should show in the Payor Search Results.");

		//Switch back to the main window
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//Click the Cancel Button in the Popup window
		testCode.cancelBtn().click();
		//Click the Reset Button in the Test Code screen
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Search Payor - Verify New Search button")
	public void testXPR_261() throws Exception {
		logger.info("===== Testing - testXPR_261 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		payorSearch = new PayorSearch(driver, config);
		payorSearchResults = new PayorSearchResults(driver,wait);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String singleTest = "WM095FJ";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click Modifier Tab. Click Add button. Click search payor ID.");
		assertTrue(isElementPresent(testCode.modifierTab(),5),"        Modifier tab must be available.");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.addPayorSpecBtn(),5),"        Add payor button must be available.");
		testCodeUtils.scrollIntoView(testCode.addPayorSpecBtn());
		testCode.clickAddPayorSpecBtn();
		assertTrue(isElementPresent(testCode.searchPayorIDBtn(),5),"        Search button must be available.");
		testCode.clickSearchPayorIDBtn();
		String winHandler = driver.getWindowHandle();
		switchToPopupWin();

		logger.info("*** Step 3 Expected Results: - Add New Record Popup must be displayed. New popup window must be displayed");
		assertTrue(isElementPresent(payorSearch.payorIDTextInput(), 5), "        New popup window must be displayed");
		assertTrue(isElementPresent(payorSearch.searchBtn(), 5), "        Submit button must be displayed");
		String getAttribute = payorSearch.searchBtn().getAttribute("data-user-action");
		assertTrue(getAttribute.contains("search/payorsearch.html"), "        Payor Search URL should contains 'search/payorsearch.html'.");

		logger.info("*** Step 4 Action: - Enter all valid data to payor info search popup. Click Search button");
		List<String> data = daoManagerXifinRpm.getRandomAllPayorInfo(testDb);
		assertTrue(isElementPresent(payorSearch.payorIDTextInput(),5),"        Payor id input must be available.");
		payorSearch.enterPayorID(data.get(1));
		assertTrue(isElementPresent(payorSearch.searchBtn(),5),"        Search button must be available.");
		payorSearch.clickSearchBtn();

		logger.info("*** Step 4 Expected Results: - The PayorID matched input is displayed");
		assertTrue(testCode.isElementDisplayed(payorSearchResults.recordsCount(), 10), "        Records count view in Search Results should show.");
		assertTrue(isElementPresent(payorSearchResults.payorSearchResultTable(), 5), "        Payor Search result must be displayed");
		assertTrue(getTableTotalRowSize(payorSearchResults.payorSearchResultTable()) >= 2, "        Result of this search must be displayed");
		assertTrue(getColumnValue(payorSearchResults.payorSearchResultTable(),data.get(1)), "        Payor ID must be displayed");

		logger.info("*** Step 5 Action: - Click New search button");
		payorSearchResults.clickNewSearchBtn();

		logger.info("*** Step 5 Expected Results: - All fields are blank");
		assertEquals(payorSearch.payorIDTextInput().getText(), "", "        Payor id input must reset to blank");
		assertEquals(payorSearch.payorNameTextInput().getText(), "", "        Payor name input must reset to blank");
		assertEquals(payorSearch.clearingHouseIdTextInput().getText(), "", "        House id input must reset to blank");
		assertEquals(payorSearch.addressInputText().getText(), "", "        Address input must reset to blank");
		assertEquals(payorSearch.cityInputText().getText(), "", "        City input must reset to blank");
		assertEquals(payorSearch.postalCodeInputText().getText(), "", "        Postal code input must reset to blank");
		assertEquals(payorSearch.phoneInputText().getText(), "", "        Phone input must reset to blank");
		assertEquals(testCodeUtils.getTextSelectedInDropdown(payorSearch.payorGroupSelectWebElement()).trim(),"", "        Payor group dropdown must reset to blank");
		assertTrue(testCode.checkDefaultBlankSelect(payorSearch.XRefTypeSelectWebElement()), "        Xref type dropdown must reset to blank");
		assertTrue(testCode.checkDefaultBlankSelect(payorSearch.stateSelectWebElement()), "        State dropdown must reset to blank");
		assertTrue(testCode.checkDefaultBlankSelect(payorSearch.countrySelectWebElement()), "        Country dropdown must reset to blank");

		//Switch back to the main window
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();

		//Click the Cancel Button in the Popup window
		testCode.cancelBtn().click();
		//Click the Reset Button in the Test Code screen
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Search Payor - Verify Reset button")
	public void testXPR_262() throws Exception {
		logger.info("===== Testing - testXPR_262 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		payorSearch = new PayorSearch(driver, config);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String singleTest = "GI547QZ";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click Modifier Tab. Click Add button. Click search payor ID.");
		assertTrue(isElementPresent(testCode.modifierTab(),5),"        Modifier tab must be available.");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.addPayorSpecBtn(),5),"        Add payor button must be available.");
		testCodeUtils.scrollIntoView(testCode.addPayorSpecBtn());
		testCode.clickAddPayorSpecBtn();
		assertTrue(isElementPresent(testCode.searchPayorIDBtn(),5),"        Search button must be available.");
		testCode.clickSearchPayorIDBtn();
		String winHandler = driver.getWindowHandle();
		switchToPopupWin();

		logger.info("*** Step 3 Expected Results: - Add New Record Popup must be displayed. New popup window must be displayed");
		assertTrue(isElementPresent(payorSearch.payorIDTextInput(), 5), "        New popup window must be displayed");
		assertTrue(isElementPresent(payorSearch.searchBtn(), 5), "        Submit button must be displayed");
		String getAttribute = payorSearch.searchBtn().getAttribute("data-user-action");
		assertTrue(getAttribute.contains("search/payorsearch.html"), "        Payor Search URL should contains 'search/payorsearch.html'.");

		logger.info("*** Step 4 Action: - Enter all valid data to payor info search popup. Click Reset button");
		List<String> data = daoManagerXifinRpm.getRandomAllPayorInfo(testDb);
		assertTrue(isElementPresent(payorSearch.payorIDTextInput(), 5), "        PayorId must be displayed");
		payorSearch.enterPayorID(data.get(1));
		assertTrue(isElementPresent(payorSearch.payorNameTextInput(), 5), "        Payor name must be displayed");
		payorSearch.enterPayorName(data.get(2));
		assertTrue(isElementPresent(payorSearch.payorGroupDropDown(), 5), "        Payor group dropdown must be displayed");
		selectItemByVal(payorSearch.payorGroupDropDown(), data.get(16));
//		assertTrue(isElementPresent(payorSearch.xrefTypeDropDown(), 5), "        Xref type Dropdown must be displayed");
//		payorSearch.selectXRefType(data.get(21));
		payorSearch.clickResetSearch();

		logger.info("*** Step 4 Expected Results: - System will reset to empty dat for all field");
		assertEquals(payorSearch.payorIDTextInput().getText(), "","        Payor ID text input must be reset to blank");
		assertEquals(payorSearch.payorNameTextInput().getText(), "","        Payor name text input must be reset to blank");
		assertEquals(testCodeUtils.getTextSelectedInDropdown(payorSearch.payorGroupSelectWebElement()).trim(),"", "        Payor group dropdown must reset to blank");
		assertTrue(testCode.checkDefaultBlankSelect(payorSearch.XRefTypeSelectWebElement()),"         Xref type dropdown must be reset to blank");

		//Switch back to the main window
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		//Click the Cancel Button in the Popup window
		testCode.cancelBtn().click();
		//Click the Reset Button in the Test Code screen
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Modifier - Search Payor - Verify Close button")
	public void testXPR_263() throws Exception {
		logger.info("===== Testing - testXPR_263 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		payorSearch = new PayorSearch(driver, config);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String singleTest = "CLM784";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click Modifier Tab. Click Add button. Click search payor ID.");
		assertTrue(isElementPresent(testCode.modifierTab(),5),"        Modifier tab must be available.");
		testCode.clickModifierTab();
		assertTrue(isElementPresent(testCode.addPayorSpecBtn(),5),"        Add payor button must be available.");
		testCodeUtils.scrollIntoView(testCode.addPayorSpecBtn());
		testCode.clickAddPayorSpecBtn();
		assertTrue(isElementPresent(testCode.searchPayorIDBtn(),5),"        Search button must be available.");
		testCode.clickSearchPayorIDBtn();
		String parentWindow = switchToPopupWin();

		logger.info("*** Step 3 Expected Results: - Add New Record Popup must be displayed. New popup window must be displayed");
		String getSearchUrl = driver.getCurrentUrl();
		assertTrue(isElementPresent(payorSearch.payorIDTextInput(), 5), "        New popup window must be displayed");
		assertTrue(isElementPresent(payorSearch.searchBtn(), 5), "        Submit button must be displayed");
		String getAttribute = payorSearch.searchBtn().getAttribute("data-user-action");
		assertTrue(getAttribute.contains("search/payorsearch.html"), "        Payor Search URL should contains 'search/payorsearch.html'.");

		logger.info("*** Step 4 Action: - Click close button");
		assertTrue(isElementPresent(payorSearch.closeBtn(),5),"        Close button must be available.");
		payorSearch.clickCloseBtn();

		logger.info("*** Step 4 Expected Results: - Popup window is disappeared");
		switchToParentWin(parentWindow);
		String getCurrentUrl =  driver.getCurrentUrl();
		assertNotEquals(getSearchUrl, getCurrentUrl,"       Search screen must be closed");
		
		testCode.cancelBtn().click();
		testCode.clickResetBtn();
	}

	// Fee schedule
	@Test(priority=1, description="Single Test Detail - fee Schedule - Add new Fee Schedule with valid input")
	public void testXPR_110() throws Exception{
		logger.info("===== Testing - testXPR_110 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		randomCharacter = new RandomCharacter(driver);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);
		listUtil = new ListUtil();

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input not available test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		String testCodeAbb = randomCharacter.getRandomNumericString(5);
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeIdWithNewTestID(testCodeAbb);

		logger.info("*** Step 2 Expected Results: - Single Test Radio available in Create Option Popup");
		assertTrue(isElementPresent(testCode.singleTestRadio(), 5), "        Single Test Radio should be available.");

		logger.info("*** Step 3 Action: - Click on Single Test Radio and submit");
		selectCheckBox(testCode.singleTestRadio());
		assertTrue(isElementPresent(testCode.createOptionOKBtn(), 5),"        Ok button on create test code option must be available");
		testCode.clickCreateOption();

		logger.info("*** Step 3 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 4 Action: - Input valid Effective date");
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"        Add effdate must be available");
		String currentDay = timeStamp.getCurrentDate("MM/dd/yyyy");
		testCode.setEffectiveDate(currentDay);

		logger.info("*** Step 4 Expected Result: - Input valid Effective date");
		assertTrue(isElementPresent(testCode.procedureTable(), 5),"        Procedure Table should be displayed");

		logger.info("*** Step 5 Action: - Add new procedure with Valid Data");
		List<String> procCodeInfo = testCode.addProcedure(this, testCode, "", testDb);

		logger.info("*** Step 5 Expected Result: - Verify that procedure code must be added");
		getColumnValue(testCode.procedureTable(), procCodeInfo.get(0));

		logger.info("*** Step 6 Action: - Add new facilities with Valid Data");
		String facility = testCode.addNewFacility(testCode.facilityTable(), this);

		logger.info("*** Step 6 Expected Result: - Verify that Facility must be added");
		assertTrue(isElementPresent(testCode.facilityTable(), 5),"        Facility table must be available");
		assertTrue(getColumnValue(testCode.facilityTable(), facility), "        Facility must be added in table");
		
		assertTrue(isElementPresent(testCode.testNameInput(), 5),"        Test name input must be available");
		testCode.setTestName("TestName");
		Dept depInfo = depDao.getDepartment();
		assertTrue(isElementPresent(testCode.selectDeps(), 5), "        Select department must be available");
		selectItemByVal(testCode.selectDeps(), String.valueOf(depInfo.getDeptId()));

		logger.info("*** Step 7 Action: - Click on Fee Schedule Tab");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5),"        Fee Schedule tab must be available");
		testCode.clickfeeScheduleTab();

		logger.info("*** Step 7 Expected Results: - System displayed 9 record in fee schedule tab with main information");
		assertTrue(isElementPresent(testCode.feeSchTable(), 5),"        Fee schedule table must be available");
		List<String> listFsInDb = daoManagerXifinRpm.getFeeScheduleListInfo(testDb);
		List<String> listFsInUi = testCodeUtils.Handle_Dynamic_Webtable(testCode.feeSchTable(),1);
		assertTrue(listUtil.compareLists(listFsInDb, listFsInUi),"        System should  display 9 records in fee schedule by default");

		logger.info("*** Step 8 Action: - Click on Add button. Input valid data for all fields. Click OK.");
		List<String> feeScheduleInfo = testCode.addNewFeeSchedule(this,currentDay, currentDay,"5", true, testDb);

		logger.info("*** Step 8 Expected Results: - New fee schedules is displayed in the list");
		assertTrue(getColumnValue(testCode.feeSchTable(), feeScheduleInfo.get(0)),"        Fee schedule must be add to table");

		logger.info("*** Step 9 Action: - Click save and clear button");
		assertTrue(isElementPresent(testCode.saveAndClearButton(), 5), "       Save and clear button must be available");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 9 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 10 Action: - Load again test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(testCodeAbb);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 10 Expected Results: -  A new row is added on Test Based Fee Schedules table and saved in DB ");
		assertNotNull(testCode.isSaveDone(daoManagerXifinRpm.getTestCodeInfo(testCodeAbb, testDb)),"        New Single Test Id should be saved in DB");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5),"        Fee Schedule tab must be available");
		testCode.clickfeeScheduleTab();
		assertTrue(isElementPresent(testCode.feeSchTable(), 5),"        Fee schedule table must be available");
		assertTrue(getColumnValue(testCode.feeSchTable(), feeScheduleInfo.get(0)),"         Fee schedule must added correctly (ID)");
		assertTrue(getColumnValue(testCode.feeSchTable(), feeScheduleInfo.get(1)),"         Fee schedule must added correctly (Description)");
		assertTrue(getColumnValue(testCode.feeSchTable(), feeScheduleInfo.get(2)),"         Fee schedule must added correctly (Type)");

		testCode.clickResetBtn();
	}

	@Test(priority=1, description="Single Test Detail - fee Schedule - Edit fee schedule with valid input")
	public void testXPR_111() throws Exception{
		logger.info("===== Testing - testXPR_111 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		listUtil = new ListUtil();
		timeStamp = new TimeStamp(driver);
		randomCharacter = new RandomCharacter(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Enter an valid Test ID (Single Test) and Tab out");
		String testId = "TESTXPR_111";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.createSingleTest(testId, testDb,false,false,this);

		logger.info("*** Step 2 Expected Results: - System display test code with information");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");

		logger.info("*** Step 3 Action: - Click Fee Schedule Tab");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5),"        Fee Schedule tab must be available");
		testCode.clickfeeScheduleTab();

		logger.info("*** Step 3 Expected Results: - System displays records in Fee Schedule tab with main information");
		assertTrue(isElementPresent(testCode.feeSchTable(), 5),"        Fee schedule table must be available");
		List<String> listFsInDb = daoManagerXifinRpm.getFeeScheduleListInfo(testDb);
		List<String> listFsInUi = testCodeUtils.Handle_Dynamic_Webtable(testCode.feeSchTable(),1);
		assertTrue(listUtil.compareLists(listFsInDb, listFsInUi),"        System should  display 9 records in fee schedule by default");

		logger.info("*** Step 4 Action: - select a fee Schedule to Edit ");
		selectColumnValue(testCode.feeSchTable(), listFsInDb.get(0));

		logger.info("*** Step 4 Expected Results: - update fee schedule with valid data");
		String newPrice = randomCharacter.getRandomNumericString(3);
		assertTrue(isElementPresent(testCode.newPriceFS(), 5),"         New price must be available");
		testCode.inputnewPriceFS(newPrice);
		String effDateInput = timeStamp.getCurrentDate();
		String effDate = testCodeUtils.getNonDuplicateEffDate(this, testCode.feeSchTable(), effDateInput);
//		testCode.inputEffDtAddNewFS(effDate);
		//Can't edit Eff Date for the existing FS although the Eff Date field is enabled.
		//A new row will be added with the new Eff Date
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button must be available");
		testCode.clickOkFeeScheduleBtn();

		logger.info("*** Step 5 Action: - Click Save and clear button");
		assertTrue(isElementPresent(testCode.saveAndClearButton(), 5), "       Save and clear button must be available");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 5 Expected Results: - Go back to load test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 10), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 6 Action: - Load again test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(testId);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 6 Expected Results: -  New procedure and facility is save in Db");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5),"        Fee Schedule tab must be available");
		testCode.clickfeeScheduleTab();
		assertTrue(isElementPresent(testCode.feeSchTable(), 5),"        Fee schedule table must be available");
		assertTrue(getColumnValue(testCode.feeSchTable(), testCodeUtils.formatDecimalPoint(Integer.parseInt(newPrice))),"        New price must be save correctly");

		testCode.clickResetBtn();
	}


	@Test(priority=1, description="Single Test Detail - fee Schedule - Hide gird balance")
	public void testXPR_112() throws Exception{
		logger.info("===== Testing - testXPR_112 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input a valid Test ID and Tab out");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		String singleTestID ="TKM840";
		testCode.checkInputTestCodeId(singleTestID);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click Fee Schedule tab");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5),"        Fee Schedule tab must be available");
		testCode.clickfeeScheduleTab();

		logger.info("*** Step 3 Expected Results: - Fee Schedule is displayed");
		assertTrue(isElementPresent(testCode.hideShowFeeScheduleTableIcon(), 5),"        Fee Schedule table hide icon must be available");

		logger.info("*** Step 4 Action: - Click on [Hide grid] icon");
		testCode.clickHideShowFeeScheduleTableIcon();

		logger.info("*** Step 4 Expected Results: - Fee Schedules table should collapse");
		assertTrue(testCode.feeScheduleTablePagernav().getAttribute("style").contains("none"),"        Fee Schedules table should collapse.");
		
		testCode.clickResetBtn();
	}

	@Test(priority=1, description="Single Test Detail - fee Schedule - Verify Filter function in table")
	public void testXPR_205() throws Exception{
		logger.info("===== Testing - testXPR_205 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> singleTestID = daoManagerXifinRpm.getSingleTest(testDb);
		String TestID = "PR362DB";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - System displayed Test Code information Single screen");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be (Single Test).");

		logger.info("*** Step 3 Action: - Input valid effective date");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5),"        Select eff date must be available");
		String minEffDate = testCode.currEffDate().getAttribute("value").trim();
		String effDate = testCodeUtils.getNextDay(minEffDate); 
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5), "        Create Effdate icon must be available");
		testCode.clickCreateEffDateIcon();
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"        Add effdate must be available");
		testCode.setEffectiveDate(effDate);

		logger.info("*** Step 3 Expected Results: - Copy effective date dialog appear");
		assertTrue(isElementPresent(testCode.effCopyDialog(), 5), "        Copy effective date dialog appear");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 4 Action: - Add new Fee Schedule ");
		List<String> feeScheduleInfo = testCode.addNewFeeSchedule(this,timeStamp.getCurrentDate(), timeStamp.getCurrentDate(), "5", true, testDb);

		logger.info("*** Step 4 Expected Results: Fee Schedule is display in table");
		assertTrue(getColumnValue(testCode.feeSchTable(), feeScheduleInfo.get(0)), "        Fee schedule must be added");

		logger.info("*** Step 5 Action: - Verify Fee Schedule Id Filter Function");
		int before = getTableTotalRowSize(testCode.feeSchTable());
		assertTrue(isElementPresent(testCode.feeScheduleIdFilter(), 5),"        Fee schedule filter must be available");
		testCode.setFeeScheduleIdFilter(feeScheduleInfo.get(0));

		logger.info("*** Step 5 Expected Results: - System will perform filter ID correctly");
		int after = getTableTotalRowSize(testCode.feeSchTable());
		assertNotEquals(before, after,"        System should  perform filter ID correctly");
		assertTrue(getColumnValue(testCode.feeSchTable(), feeScheduleInfo.get(0)),"        System should  perform filter ID correctly");

		logger.info("*** Step 6 Action: - Verify Fee Schedule Type Filter Function");
		testCode.clearData(testCode.feeScheduleIdFilter());
		assertTrue(isElementPresent(testCode.feeScheduleTypeFilter(), 5),"        Fee schedule type filter must be available");
		testCode.setFeeScheduleTypeFilter(feeScheduleInfo.get(2));

		logger.info("*** Step 6 Expected Results: - System will perform filter Type correctly");
		assertTrue(getColumnValue(testCode.feeSchTable(), feeScheduleInfo.get(2)),"        System should  perform filter Type correctly");

		logger.info("*** Step 7 Action: - Verify Fee Schedule effective date Filter Function");
		testCode.clearData(testCode.feeScheduleTypeFilter());
		assertTrue(isElementPresent(testCode.feeScheduleEffDtFilter(), 5),"        Fee schedule eff date filter must be available");
		testCode.setFeeScheduleEffDtFilter(feeScheduleInfo.get(3));

		logger.info("*** Step 7 Expected Results: - System will perform filter effective date correctly");
		after = getTableTotalRowSize(testCode.feeSchTable());
		assertNotEquals(before, after,"         System should  perform filter effective date correctly");
		assertTrue(getColumnValue(testCode.feeSchTable(), feeScheduleInfo.get(3)),"        System should  perform filter effective date correctly");

		logger.info("*** Step 8 Action: - Verify Fee Schedule New Price Filter Function");
		testCode.clearData(testCode.feeScheduleEffDtFilter());
		assertTrue(isElementPresent(testCode.feeScheduleNewPriceFilter(), 5),"        Fee schedule new price filter must be available");
		testCode.setFeeScheduleNewPriceFilter(feeScheduleInfo.get(5));

		logger.info("*** Step 8 Expected Results: - System will perform filter New Price correctly");
		after = getTableTotalRowSize(testCode.feeSchTable());
		assertNotEquals(before, after,"         System should  perform filter New Price correctly");
		assertTrue(getColumnValue(testCode.feeSchTable(),testCodeUtils.formatDecimalPoint(Integer.parseInt(feeScheduleInfo.get(5)))),"        System should  perform filter New Price correctly");
		
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Fee Schedule - Add FS with new price is less than min price and dup ID")
	public void testXPR_266() throws Exception {
		logger.info("===== Testing - testXPR_266 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String singleTestId = "TESTXPR_266";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.createSingleTest(singleTestId, testDb,false,false,this);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"         Test label must be available");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Create new procedure with valid data");
		List<String> procedureInfo = testCode.addProcedure(this,testCode,"",testDb, singleTestId);

		logger.info("*** Step 3 Expected Result: - New procedure is displayed in the table");
		assertTrue(isElementPresent(testCode.procedureTable(), 5),"       Procedure table must be available");
		assertTrue(getColumnValue(testCode.procedureTable(), procedureInfo.get(0)), "        Procedures must be added in table");
		
		logger.info("*** Step 4 Action: - Create facility with lab cost = minprice > 5");
		assertTrue(isElementPresent(testCode.facilityTab(), 5),"       Facility tab must be available");
		testCode.clickFacilityTab();
		String minPrice = testCode.minPriceFacilityTable().getText();
		
		logger.info("*** Step 4 Expected Result: - New facility is displayed in the table");
		assertTrue(isElementPresent(testCode.facilityTable(), 5),"       Facility table must be available");
		assertTrue(getColumnValue(testCode.facilityTable(), minPrice), "        facility must be added in table");
		
		logger.info("*** Step 5 Action: - Add new Fee Schedule and click ok button");
		List<String> feeSch = testCode.addNewFeeSchedule(this, timeStamp.getCurrentDate(), testCodeUtils.getNextYear(timeStamp.getCurrentDate()), "1", true, testDb);

		logger.info("*** Step 5 Expected Result : - New Fee Schedule is added in the table list");
		assertTrue(getColumnValue(testCode.feeSchTable(), feeSch.get(0)), "       Fee Schedule added to table");

		logger.info("*** Step 6 Action: - click on save and clear button");
		Dept depInfo = depDao.getDepartment();
		assertTrue(isElementPresent(testCode.selectDeps(), 5), "        Select department must be available");
		selectItemByVal(testCode.selectDeps(), String.valueOf(depInfo.getDeptId()));
		assertTrue(isElementPresent(testCode.saveAndClearButton(), 5), "       Save and clear button must be available");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 6 Expected Result : - The error messages must be displayed");
		assertTrue(isElementPresent(testCode.errorListPanel(), 5), "       Error panel must be present");
		assertTrue(testCode.getAlertTestCode(1).contains("Invalid new price is less than the test's minimum price of $"+minPrice+" for fee schedule"), "       Error message must be correct");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Fee Schedule - Add FS with eff date is overlap the an other one")
	public void testXPR_267() throws Exception {
		logger.info("===== Testing - testXPR_267 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String singleTestId = "TESTXPR267";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.createSingleTest(singleTestId, testDb,false,false,this);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click on Pencil Icon");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5));
		String effDate = testCode.currEffDate().getAttribute("value").trim();
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5));
		clickHiddenPageObject(testCode.createEffDateIcon(), 0);

		logger.info("*** Step 3 Expected Results: - Effective date field Will be displayed");
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"          Effective date field should be displayed");

		logger.info("*** Step 4 Action: - Input valid Effective Date");
		String newEffDate = testCodeUtils.getNextDay(effDate);
		testCode.setEffectiveDate(newEffDate);

		logger.info("*** Step 4 Expected Results: - Effective Data pop up is displayed");
		assertTrue(isElementPresent(testCode.copyEffDt(), 5),"        Effective Data pop up should be displayed");
		assertTrue(isElementPresent(testCode.cancelEffCopyDialog(), 5),"        Effective Data pop up should be displayed");

		logger.info("*** Step 5 Action: - Select valid Effective Date and click cancel button on Copy effective date pop up");
		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 6 Action: - Add procedures with valid data");
		List<String> procedureInfo = testCode.addProcedure(this,testCode,newEffDate,testDb);

		logger.info("*** Step 6 Expected Result: - Verify that procedure code must be added");
		assertTrue(isElementPresent(testCode.procedureTable(), 5));
		assertTrue(getColumnValue(testCode.procedureTable(), procedureInfo.get(0)), "        Procedures must be added in table");

		logger.info("*** Step 7 Action: - Add Facilities with valid data");
		assertTrue(isElementPresent(testCode.facilityTable(), 5));
		String facility = testCode.addNewFacility(testCode.facilityTable(), this);

		logger.info("*** Step 7 Expected Result: - Verify that Facility must be added");
		assertTrue(getColumnValue(testCode.facilityTable(), facility), "        Facility must be added in table");

		logger.info("*** Step 8 Action: - Add new Fee Schedule");
		String fsEffDate = testCodeUtils.getNextDay(newEffDate);
		String fsExpDate = testCodeUtils.getNextDay(fsEffDate);
		List<String> feeSch = testCode.addNewFeeSchedule(this, fsEffDate, fsExpDate, "1", true, testDb);

		logger.info("*** Step 8 Expected Result : - New Fee Schedules row is add into the table");
		assertTrue(getColumnValue(testCode.feeSchTable(), feeSch.get(0)), "       Fee Schedule added to table");

		logger.info("*** Step 9 Action: - Select Department and click save and clear button");
		Dept depInfo = depDao.getDepartment();
		assertTrue(isElementPresent(testCode.selectDeps(), 5));
		selectItemByVal(testCode.selectDeps(), String.valueOf(depInfo.getDeptId()));
		testCode.setexpDateInput(testCodeUtils.getNextYear(newEffDate));
		assertTrue(isElementPresent(testCode.saveAndClearButton(), 5), "       Save and clear button must be available");
		testCode.clickSaveAndClearBtn();
		wait.until(ExpectedConditions.invisibilityOf(testCode.saveInProgressInfoText()));

		logger.info("*** Step 9 Expected Result : - Verify come back to login screen");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 10 Action: - Load again single test");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestId);

		logger.info("*** Step 10 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 11 Action: - Select effective date have added before");
		assertTrue(isElementPresent(testCode.selectEffDate(), 5));
		selectItem(testCode.selectEffDate(),newEffDate);

		logger.info("*** Step 11 Expected Results: - Verify Fee Schedule tab present");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5), "        Fee Schedule must be presented");

		logger.info("*** Step 12 Action: - Add new fee schedules with duplicate fee schedules ID, valid eff date,  new $. Click ok button.");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5));
		testCode.clickfeeScheduleTab();
		assertTrue(isElementPresent(testCode.addNewFSBtn(), 5));
		testCodeUtils.scrollIntoView(testCode.addNewFSBtn());
		testCode.clickAddNewFSBtn();
		assertTrue(isElementPresent(testCode.feeSchIdInput(), 5));
		testCode.inputfeeSchId(feeSch.get(0));
		assertTrue(isElementPresent(testCode.effDtAddNewFS(), 5));
		testCode.inputEffDtAddNewFS(fsEffDate);
		assertTrue(isElementPresent(testCode.expDtAddnewFS(), 5));
		testCode.inputExpDtAddnewFS(fsExpDate);
		assertTrue(isElementPresent(testCode.newPriceFS(), 5));
		testCode.inputnewPriceFS("1");
		assertTrue(isElementPresent(testCode.okBtn(), 5));
		testCode.clickOkFeeScheduleBtn();

		logger.info("*** Step 12 Expected Results: - Update new fee schedules with duplicate fee schedules ID is displayed in fee schedule table");
		assertTrue(isElementPresent(testCode.feeSchTable(), 5), "        Fee Schedule table must be presented");
		assertTrue(getColumnValue(testCode.feeSchTable(), feeSch.get(0)), "        Update new fee schedules with duplicate fee schedules ID is displayed in fee schedule table");
		
		logger.info("*** Step 13 Action: - Click on Save and Clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5));
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 13 Expected Results: - Verify error message");
		assertTrue(isElementPresent(testCode.errorListPanel(), 5), "       Error panel must be present");
		assertTrue(testCode.getAlertTestCode(1).contains("contain overlapping fee schedule effective and expiration date ranges. Please correct and resubmit your changes."), "       Error message must be correct");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Fee Schedule - Add FS with eff date and exp date is consistent with exist")
	public void testXPR_268() throws Exception {
		logger.info("===== Testing - testXPR_268 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String singleTestId = "TESTXPR_268";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.createSingleTest(singleTestId, testDb,false,false,this);

		logger.info("*** Step 2 Expected Results: - The Single Test information page is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Click on Pencil Icon");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5),"        Select EffDate should show");
		String effDate = testCodeUtils.getLastItemDropdown(testCode.selectEffDate());
		assertTrue(isElementPresent(testCode.createEffDateIcon(), 5),"       Create EffDate icon should show");
		testCode.clickCreateEffDateIcon();

		logger.info("*** Step 3 Expected Results: - Effective date field Will be displayed");
		assertTrue(isElementPresent(testCode.addEffDate(), 5),"          Effective date field should be displayed");

		logger.info("*** Step 4 Action: - Input valid Effective Date");
		String newEffDate = testCodeUtils.getPreviousYear(effDate);
		testCode.setEffectiveDate(newEffDate);

		logger.info("*** Step 4 Expected Results: - Effective Data pop up is displayed");
		assertTrue(isElementPresent(testCode.copyEffDt(), 5),"        Effective Data pop up should be displayed");
		assertTrue(isElementPresent(testCode.cancelEffCopyDialog(), 5),"        Effective Data pop up should be displayed");

		logger.info("*** Step 5 Action: - Select valid Effective Date and click cancel button on Copy effective date pop up");
//		testCode.clickCancelEffDateCopyDialog();

		logger.info("*** Step 6 Action: - Add procedures");
		List<String> procedureInfo = testCode.addProcedure(this,testCode,testCodeUtils.getPreviousDay(effDate),testDb);

		logger.info("*** Step 6 Expected Result: - Verify that procedure code must be added");
		assertTrue(isElementPresent(testCode.procedureTable(), 5),"        procedure code should show");
		assertTrue(getColumnValue(testCode.procedureTable(), procedureInfo.get(0)), "        Procedures must be added in table");

		logger.info("*** Step 7 Action: - Add Facilities");
		assertTrue(isElementPresent(testCode.facilityTab(), 5),"       facility tab should show");
		testCode.clickFacilityTab();
		assertTrue(isElementPresent(testCode.addFacilityBtn(), 5),"       add facility button should show");
		testCodeUtils.scrollIntoView(testCode.addFacilityBtn());
		testCode.clickAddFacilityBtn();
		String facility = testCode.selectPerformingFacility(2);
		String minPrice = "6";
		String labCost  = minPrice;
		assertTrue(isElementPresent(testCode.labCostInput(), 5),"        labCost Input should show");
		testCode.inputLabCost(labCost);
		assertTrue(isElementPresent(testCode.minPriceInput(), 5),"        Min price input field should show");
		testCode.inputMinPrice(minPrice);
		assertTrue(isElementPresent(testCode.okBtn(), 5),"       ok facility button should show");
		testCode.clickOkFacilityBtn();

		logger.info("*** Step 7 Expected Result: - Verify that Facility must be added");
		assertTrue(isElementPresent(testCode.facilityTable(), 5));
		assertTrue(getColumnValue(testCode.facilityTable(), facility), "        Facility must be added in table");

		logger.info("*** Step 8 Action: - Add new Fee Schedule duplicate with Fee Schedule exist which have effective date and exp date empty");
		String fsEffDate = newEffDate;
		String fsExpDate = newEffDate;
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5),"       fee schedule tab should show");
		testCode.clickfeeScheduleTab();
		assertTrue(isElementPresent(testCode.addNewFSBtn(), 5),"        add new fee schedule tab should show");
		testCodeUtils.scrollIntoView(testCode.addNewFSBtn());
		testCode.clickAddNewFSBtn();
		String specialFSID = testCode.findFSNonEffDateAndExpDate(testCode.feeSchTable()).get(1);
		assertTrue(isElementPresent(testCode.feeSchIdInput(), 5),"        feeSchIdInput should show");
		testCode.inputfeeSchId(specialFSID);	
		assertTrue(isElementPresent(testCode.effDtAddNewFS(), 5),"        effDtAddNewFS should show");
		testCode.inputEffDtAddNewFS(fsEffDate);
		assertTrue(isElementPresent(testCode.expDtAddnewFS(), 5),"         expDtAddnewFS should show");
		testCode.inputExpDtAddnewFS(fsExpDate);
		assertTrue(isElementPresent(testCode.newPriceFS(), 5),"         newPriceFS should show");
		testCode.inputnewPriceFS("2");
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        ok fee schedule button should show");
		testCode.clickOkFeeScheduleBtn();

		logger.info("*** Step 8 Expected Result : - New Fee Schedule added to table");
		assertTrue(getColumnValue(testCode.feeSchTable(), specialFSID), "       Fee Schedule must be added to table");

		logger.info("*** Step 9 Action: - Click add icon and add again new fee schedule (FS ID have been added before) not overlap eff date and exp date");
		String fsEffDate2 = testCodeUtils.getNextDay(fsEffDate);
		String fsExpDate2 = fsEffDate2;
		assertTrue(isElementPresent(testCode.addNewFSBtn(), 5),"        add fee schedule button should show");
		testCodeUtils.scrollIntoView(testCode.addNewFSBtn());
		testCode.clickAddNewFSBtn();
		testCode.inputfeeSchId(specialFSID);		
		testCode.inputEffDtAddNewFS(fsEffDate2);
		testCode.inputExpDtAddnewFS(fsExpDate2);
		testCode.inputnewPriceFS("3");
		testCode.clickOkFeeScheduleBtn();

		logger.info("*** Step 9 Expected Result : - Fee Schedule added to table");
		assertTrue(getColumnValue(testCode.feeSchTable(), specialFSID), "       Fee Schedule must be added to table");

		logger.info("*** Step 10 Action: - Select Department and save all information");
		Dept depInfo = depDao.getDepartment();
		assertTrue(isElementPresent(testCode.selectDeps(), 5));
		selectItemByVal(testCode.selectDeps(), String.valueOf(depInfo.getDeptId()));
		testCode.setexpDateInput(testCodeUtils.getNextYear(newEffDate));
		assertTrue(isElementPresent(testCode.saveAndClearButton(), 5), "       Save and clear button must be available");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 9 Expected Result : - Verify come back to login screen");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 10 Action: - Load again single test");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestId);

		logger.info("*** Step 10 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 11 Action: - Select effective date have added before");
		assertTrue(isElementPresent(testCode.selectEffDate(), 5),"        select effdate should show");
		selectItem(testCode.selectEffDate(),newEffDate);
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5),"        fee schedule tab should show");
		testCode.clickfeeScheduleTab();
		testCodeUtils.scrollIntoView(testCode.addNewFSBtn());

		logger.info("*** Step 11 Expected Results: - Verify FS added");
		assertTrue(isElementPresent(testCode.feeSchTable(), 5));
		assertTrue(getColumnValue(testCode.feeSchTable(), specialFSID), "       Fee Schedule must be added to table");
		assertTrue(getColumnValue(testCode.feeSchTable(), fsEffDate), "       Eff date must be update to table");
		assertTrue(getColumnValue(testCode.feeSchTable(), fsEffDate2), "       Fee Schedule new must be added to table");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Fee Schedule - Edit Fee Schedule with invalid format Exp Date")
	public void testXPR_269() throws Exception {
		logger.info("===== Testing - testXPR_269 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> singleTestCodeInfo = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTestCodeInfo = "II836DR";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Test Code page is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"       label test should show");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Select any row and click edit button");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5));
		testCode.clickfeeScheduleTab();
		List<String> listCRID = testCode.getAllFieldsInFirstRow(testCode.feeSchTable());
		selectColumnValue(testCode.feeSchTable(), listCRID.get(0));

		logger.info("*** Step 3 Expected Result : - Edit popup is displayed");
		assertTrue(isElementPresent(testCode.editPopupFeeSchedule(), 5), "       Fee Schedule popup appear");

		logger.info("*** Step 4 Action: - Enter invalid date format to exp date");
		String validFormatEff = "12/10/2010";
		String invalidFormatExp = "13/Nov/2040";
		assertTrue(isElementPresent(testCode.effDtAddNewFS(), 5),"       effDt Add NewFS should show");
		testCode.inputEffDtAddNewFS(validFormatEff);
		assertTrue(isElementPresent(testCode.expDtAddnewFS(), 5),"        expDt Add newFS should show");
		testCode.inputExpDtAddnewFS(invalidFormatExp);
		assertTrue(isElementPresent(testCode.closeErrorMessage(), 5), "        Close error message button must be displayed");
		testCode.clickcloseErrorMessage();

		logger.info("*** Step 4 Expected Result : - The error messages is displayed");
		//assertTrue(isElementPresent(testCode.feeScheduleDialogFormError(), 5), "       Error panel must be present");
		//assertTrue(testCode.feeScheduleDialogFormError().getText().equalsIgnoreCase("Expiration Date: Please enter a valid date."), "       Error panel must be alert correct message");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Fee Schedule - Edit FS with eff date is overlap with existing one")
	public void testXPR_270() throws Exception {
		logger.info("===== Testing - testXPR_270 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String singleTestId = "TESTXPR_270";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.createSingleTest(singleTestId, testDb,false,false,this);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test Label should show");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Add free schedule");
		String effDate = "10/10/2010"; 
		String expDate = "10/10/2011";
		List<String> fs = testCode.addNewFeeSchedule(this, effDate, expDate, "1", true, testDb);

		logger.info("*** Step 3 Expected Result : - New fee schedule added into table");
		assertTrue(isElementPresent(testCode.feeSchTable(), 5), "       Fee Schedule table must be presented");
		assertTrue(getColumnValue(testCode.feeSchTable(), fs.get(0)), "       Fee Schedule must be added into table");

		logger.info("*** Step 4 Action: - Update eff date and exp date is overlap with existing one");
		testCodeUtils.scrollIntoView(testCode.addNewFSBtn());
		testCode.clickAddNewFSBtn();
		assertTrue(isElementPresent(testCode.feeSchIdInput(), 5),"        feeSch Id Input field should show");
		testCode.inputfeeSchId(fs.get(0));
		assertTrue(isElementPresent(testCode.effDtAddNewFS(), 5),"        effDt Add New FS should show");
		testCode.inputEffDtAddNewFS(effDate);
		assertTrue(isElementPresent(testCode.expDtAddnewFS(), 5),"        exp Dt Add new FS should show");
		testCode.inputExpDtAddnewFS(expDate);
		assertTrue(isElementPresent(testCode.newPriceFS(), 5),"           newPriceFS should show");
		testCode.inputnewPriceFS("1");
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        ok fee schedule should show");
		testCode.clickOkFeeScheduleBtn();

		logger.info("*** Step 4 Expected Result: - Update eff date ais displayed in fee schedule table");
		assertTrue(isElementPresent(testCode.feeSchTable(), 5), "       Fee Schedule table must be presented");
		assertTrue(getColumnValue(testCode.feeSchTable(), expDate), "       Fee Schedule must be updated into table");
		
		logger.info("*** Step 5 Action: - Click Save and Clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        save and clear button should show");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 5 Expected Result : - The error message is displayed");
		assertTrue(isElementPresent(testCode.errorListPanel(), 5), "       Error panel must be present");
		assertTrue(testCode.getAlertTestCode(1).contains("contain overlapping fee schedule effective and expiration date ranges. Please correct and resubmit your changes."), "       Error message must be correct");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Profile Detail - Fee Schedule - Cancel the Add FS")
	public void testXPR_271() throws Exception {
		logger.info("===== Testing - testXPR_271=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		timeStamp = new TimeStamp();
		testCodeUtils = new TestCodeUtils(driver);
		randomCharacter = new RandomCharacter();

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> profileID = daoManagerXifinRpm.getProfileTest(testDb);
		String TestID = "000681QZ";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test information page is displayed");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Profile)"),"        Test Label should be (Profile).");

		logger.info("*** Step 3 Action: - Fee  Schedule tab - Add new Fee Schedule and click Cancel button");
		String effDate = timeStamp.getCurrentDate();
		String newPrice = randomCharacter.getRandomNumericString(2);
		List<String> feeSchedule = testCode.addNewFeeSchedule(this, effDate,effDate, newPrice, false, testDb);
		String feeSchedID = feeSchedule.get(0);

		logger.info("*** Step 3 Expected Result: - New Fee Schedule is not displayed in the table");
		assertTrue(getColumnValue(testCode.feeSchTable(), feeSchedID), "       New Fee Schedule ID is not displayed in the table");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Profile Detail - Fee Schedule - Fee Schedule - Reset the add new FS")
	public void testXPR_272() throws Exception {
		logger.info("===== Testing - testXPR_272=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		timeStamp= new TimeStamp(driver);
		testCodeUtils = new TestCodeUtils(driver);
		randomCharacter = new RandomCharacter();

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> profileTestID = daoManagerXifinRpm.getProfileTest(testDb);
		String TestID = "000886NQ";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Profile)"),"        Test Label should be (Profile).");

		logger.info("*** Step 3 Action: - Click fee schedules tab. Click add icon. Input all fields and click OK button.");
		String effDate = timeStamp.getCurrentDate();
		String newPrice = randomCharacter.getRandomNumericString(2);
		List<String> feeSchedule = testCode.addNewFeeSchedule(this, effDate,effDate, newPrice, true, testDb);
		String feeSchedID = feeSchedule.get(0);
		String effdate = feeSchedule.get(3);

		logger.info("*** Step 3 Expected Result: - New Fee Schedule is displayed in the table");
		assertTrue(isElementPresent(testCode.feeSchTable(),5));
		assertTrue(getColumnValue(testCode.feeSchTable(), feeSchedID), "       New Fee Schedule ID is displayed in the table");
		assertTrue(getColumnValue(testCode.feeSchTable(), effdate), "       New Eff date is displayed in the table");

		logger.info("*** Step 4 Action: - Click on reset button and ok button");
		assertTrue(isElementPresent(testCode.resetBtn(),5));
		testCode.clickResetBtn();
		String confirm =  closeAlertAndGetItsText(true);
		
		logger.info("*** Step 4 Expected Result: - Message confirmation is displayed ");
		assertNotNull(confirm,"        Message confirmation is displayed ");

		logger.info("*** Step 5 Action: - Login with TestId on step 2 and navigate to FS tab");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5));
		testCode.clickfeeScheduleTab();

		logger.info("*** Step 5 Expected Result: - New data is not save");
		scrollToElement(0, 250);
		assertFalse(getColumnValue(testCode.feeSchTable(), effdate), "       New Eff date is not displayed in the table");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Profile Detail - Fee Schedule - Reset the update FS")
	public void testXPR_273() throws Exception {
		logger.info("===== Testing - testXPR_273=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Enter an valid Test ID (Single Test) and Tab out");
//		List<String> profileTestID = daoManagerXifinRpm.getProfileTest(testDb);
		String TestID = "001108WM";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test information page is displayed");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Profile)"),"        Test Label should be (Profile).");

		logger.info("*** Step 3 Action: - Fee Schedule tab , select any row that eff and exp date are empty and click edit icon");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5));
		testCode.clickfeeScheduleTab();
		List<String> listFsInUi = testCodeUtils.Handle_Dynamic_Webtable(testCode.feeSchTable(),1);
		selectColumnValue(testCode.feeSchTable(), listFsInUi.get(0));

		logger.info("*** Step 3 Expected Results: - Edit popup will display");
		assertTrue(isElementPresent(testCode.addFSPopup(), 5),"        Edit Record must be displayed");

		logger.info("*** Step 4 Action: - Enter eff and exp date and click OK button");
		String effDateInput = timeStamp.getCurrentDate();
		String effDate = testCodeUtils.getNonDuplicateEffDate(this, testCode.feeSchTable(), effDateInput);
		String expDate = testCodeUtils.getNextDay(effDate);
		assertTrue(isElementPresent(testCode.effDtAddNewFS(), 5), "        Effective Date Input field should show.");
		testCode.inputEffDtAddNewFS(effDate);
		assertTrue(isElementPresent(testCode.expDtAddnewFS(), 5), "        Expiration Date Input field should show.");
		testCode.inputExpDtAddnewFS(expDate);
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        OK button should show.");
		testCode.clickOkFeeScheduleBtn();

		logger.info("*** Step 4 Expected Results: - The eff and exp date are displayed in the row");
		assertTrue(isElementPresent(testCode.feeSchTable(), 5), "        Fee Schedule table should show.");
		assertTrue(getColumnValue(testCode.feeSchTable(), effDate), "       The eff date should be displayed in the table");
		assertTrue(getColumnValue(testCode.feeSchTable(), expDate), "       The exp date should be displayed in the table");

		logger.info("*** Step 5 Action: - Click Reset button and login with ID in step 2");
		assertTrue(isElementPresent(testCode.resetBtn(), 5),"        Reset button should show.");
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 5 Expected Results: - The updated information is not saved into database");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5), "     Fee Schedule tab should show.");
		testCode.clickfeeScheduleTab();		
		assertFalse(getColumnValue(testCode.feeSchTable(), effDate), "       Effective Date should not be saved into database");
		assertFalse(getColumnValue(testCode.feeSchTable(), expDate), "       Expiration Date should not be saved into database");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Profile Detail - Fee Schedule - Verify Sort function in Test Based fee schedule")
	public void testXPR_274() throws Exception {
		logger.info("===== Testing - testXPR_274=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		timeStamp = new TimeStamp();
		testCodeUtils = new TestCodeUtils(driver);
		randomCharacter = new RandomCharacter();
		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Profile) and Tab out");
		String testID = "TESTXPR_274";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.createProfileTest(testID, testDb, this);

		logger.info("*** Step 2 Expected Results: - Profile Test test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Profile)"),"        Test Label should be (Profile).");

		logger.info("*** Step 3 Action: - Create new FS and click OK button");
		String effDate = timeStamp.getCurrentDate();
		String newPrice = randomCharacter.getRandomNumericString(2);
		List<String> fsInfo =  testCode.addNewFeeSchedule(this, effDate, effDate, newPrice, true, testDb);

		logger.info("*** Step 3 Expected Results: - New fee schedule display in fee schedule table");
		assertTrue(isElementPresent(testCode.feeSchTable(), 5));
		assertTrue(getColumnValue(testCode.feeSchTable(),fsInfo.get(0)),"        New fee schedule display in fee schedule table");
		
		logger.info("*** Step 4 Action: - Click on Effective date column");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5));
		clickHiddenPageObject(testCode.feeScheduleTab(), 0);
		assertTrue(isElementPresent(testCode.EffDateColumn(), 5));
		clickHiddenPageObject(testCode.EffDateColumn(), 0);

		logger.info("*** Step 4 Expected Results: - System will perform ascending sort fee schedule based on effective date ");
		assertTrue(isElementPresent(testCode.feeSchTable(), 5));
		assertTrue(getSortingComparisonOnTable(testCode.feeSchTable(), 4, "asc"),"        System will perform ascending sort fee schedule based on effective date ");

		logger.info("*** Step 5 Action: - Fee Schedule tab : Click on Effective date column again");
		assertTrue(isElementPresent(testCode.EffDateColumn(), 5));
		//testCode.clickEffDateColumn();
		clickHiddenPageObject(testCode.EffDateColumn(), 0);

		logger.info("*** Step 5 Expected Results: - System will perform decrease sort fee schedule based on effective date ");
		assertTrue(getSortingComparisonOnTable(testCode.feeSchTable(), 4, "desc"),"         System will perform decrease sort fee schedule based on effective date ");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Profile Detail - Fee Schedule - Edit FS without select any row")
	public void testXPR_275() throws Exception {
		logger.info("===== Testing - testXPR_275 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
//		List<String> profileTestID = daoManagerXifinRpm.getProfileTest(testDb);
		String TestID = "001270WM";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single Test information page is displayed");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Profile)"),"        Test Label should be (Profile).");

		logger.info("*** Step 3 Action: - Click on Edit button without select any row");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5),"        Fee Schedule tab should show.");
		testCode.clickfeeScheduleTab();
		assertTrue(isElementPresent(testCode.editXrefButton(), 5),"        Edit Xref button should show.");
		testCodeUtils.scrollIntoView(testCode.editXrefButton());
		testCode.clickEditXrefButton();

		logger.info("*** Step 3 Expected Results: - The error message is displayed");
		assertTrue(isElementPresent(testCode.alertpopup(), 5),"        Alert popup is displayed");

		assertTrue(isElementPresent(testCode.WarningCloseBtn(), 5), "        Close Message error button must be displayed");
		testCode.clickWarningCloseBtn();
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Profile Detail - Fee Schedule - Add FS with FS Id is not active fee schedule")
	public void testXPR_302() throws Exception {
		logger.info("===== Testing - testXPR_302 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Profile) and Tab out");
//		List<String> proFileTest = daoManagerXifinRpm.getProfileTest(testDb);
		String TestID = "001271AG";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Profile test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Profile)"),"        Test Label should be Profile.");

		logger.info("*** Step 3 Action: - Navigate to Fee Schedule Tab");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5));
		testCode.clickfeeScheduleTab();

		logger.info("*** Step 3 Expected Results: - Fee Schedule Table is display");
		assertTrue(isElementPresent(testCode.feeSchTable(), 5));

		logger.info("*** Step 4 Action: - Click Add New fee Schedule button");
		assertTrue(isElementPresent(testCode.addNewFSBtn(), 5));
		testCodeUtils.scrollIntoView(testCode.addNewFSBtn());
		testCode.clickAddNewFSBtn();

		logger.info("*** Step 4 Expected Results: - Add record popup is display");
		assertTrue(isElementPresent(testCode.addFSPopup(), 5));

		logger.info("*** Step 5 Action: - Input FS Id  is special price table");
		String effDate = timeStamp.getCurrentDate();
		String fsId = daoManagerXifinRpm.getSpecialFeeScheduleID(testDb);
		assertTrue(isElementPresent(testCode.feeSchIdInput(), 5),"        Fee Schedule input should show.");
		testCode.inputfeeSchId(fsId);
		assertTrue(isElementPresent(testCode.effDtAddNewFS(), 5),"        Effective date should show.");
		testCode.inputEffDtAddNewFS(effDate);
		assertTrue(isElementPresent(testCode.expDtAddnewFS(), 5),"        Expiration date should show.");
		testCode.inputExpDtAddnewFS(testCodeUtils.getNextDay(effDate));
		assertTrue(isElementPresent(testCode.newPriceFS(), 5),"        New price should show.");
		testCode.inputnewPriceFS("5");
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button should show.");
		testCode.clickOkFeeScheduleBtn();

		logger.info("*** Step 5 Expected Results: - The error message is displayed");
		assertTrue(isElementPresent(testCode.feeScheduleDialogFormError(), 5),"        Fee Schedule dialog form error should show.");
		assertTrue(testCode.feeScheduleDialogFormError().getText().contains("No active fee schedule found for "+fsId),"        The error message 'No active fee schedule found for "+fsId+"' is displayed");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Profile Detail - Fee Schedule - Add FS with invalid data")
	public void testXPR_303() throws Exception {
		logger.info("===== Testing - testXPR_303 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Profile) and Tab out");
//		List<String> proFileTest = daoManagerXifinRpm.getProfileTest(testDb);
		String TestID = "00173488";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Profile test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Profile)"),"        Test Label should be Profile.");

		logger.info("*** Step 3 Action: - Add new fee schedule with invalid eff date");
        testCode.clickfeeScheduleTab();
        testCodeUtils.scrollIntoView(testCode.addNewFSBtn());
        testCode.clickAddNewFSBtn();
        Assert.assertTrue(isElementPresent(testCode.addFSPopup(), 5));
       testCode.inputEffDtAddNewFS("2015/01/10");

		logger.info("*** Step 3 Expected Results: - The error message is displayed");
		assertTrue(isElementPresent(testCode.messageContent(), 5),"        Error message content should show.");
		assertTrue(testCode.messageContent().getText().contains("Invalid date format."),"        The error message 'Invalid date format.' is displayed");

        testCode.effDtAddNewFS().clear();
        testCode.effDtAddNewFS().sendKeys(Keys.TAB);
		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Profile Detail - Fee Schedule - Add FS with Eff date is overlap")
	public void testXPR_304() throws Exception {
		logger.info("===== Testing - testXPR_304 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		timeStamp = new TimeStamp(driver);
		testCodeUtils = new TestCodeUtils(driver);
		randomCharacter = new RandomCharacter(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single Test) and Tab out");
		String profileTestId = "TESTXPR_304";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.createProfileTest(profileTestId, testDb, this);

		logger.info("*** Step 2 Expected Results: - Profile information page is displayed");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Profile)"),"        Test Label should be (Profile).");

		logger.info("*** Step 3 Action: Select any row and click edit button");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5));
		testCode.clickfeeScheduleTab();
		assertTrue(isElementPresent(testCode.feeSchTable(), 5));
		List<String> listFSID = testCode.getAllFieldsInFirstRow(testCode.feeSchTable());
		String fSId = listFSID.get(1);
		selectColumnValue(testCode.feeSchTable(), fSId);

		logger.info("*** Step 3 Expected Results:- Edit record popup will displayed");
		assertTrue(isElementPresent(testCode.addFSPopup(), 5),"        Edit Record must be displayed");

		logger.info("*** Step 4 Action: - Update Effective Date, leave Expiration Date as blank and click Ok button");
		String effDate =  timeStamp.getCurrentDate();
		assertTrue(isElementPresent(testCode.effDtAddNewFS(), 5));
		testCode.inputEffDtAddNewFS(effDate);
		assertTrue(isElementPresent(testCode.expDtAddnewFS(), 5));
		testCode.inputExpDtAddnewFS("");
		assertTrue(isElementPresent(testCode.okBtn(), 5));
		testCode.clickOkProcedureBtn();

		logger.info("*** Step 4 Expected Results: - Update information is displayed in the table");
		assertTrue(isElementPresent(testCode.feeSchTable(), 5));
		assertTrue(getColumnValue(testCode.feeSchTable(), effDate)); 

		logger.info("*** Step 5 Action: - Click on Add icon");
		assertTrue(isElementPresent(testCode.addNewFSBtn(), 5));
		testCodeUtils.scrollIntoView(testCode.addNewFSBtn());
		testCode.clickAddNewFSBtn();

		logger.info("*** Step 5 Expected Results: - Add new record popup is displayed");
		assertTrue(isElementPresent(testCode.addFSPopup(), 5),"        Add new record popup must be displayed");

		logger.info("*** Step 6 Action: - Input FSId is duplicate with FSId at step 3, input Effective Date is greater than Effective Date at step 3 and click ok button");
		String newEffDate = testCodeUtils.getNextYear(effDate);
		assertTrue(isElementPresent(testCode.feeSchIdInput(), 5));
		testCode.inputfeeSchId(fSId);		
		assertTrue(isElementPresent(testCode.effDtAddNewFS(), 5));
		testCode.inputEffDtAddNewFS(newEffDate);
		assertTrue(isElementPresent(testCode.newPriceFS(), 5));
		String newPrice = randomCharacter.getRandomNumericString(3);
		testCode.inputnewPriceFS(newPrice);
		assertTrue(isElementPresent(testCode.okBtn(), 5));
		testCode.clickOkFeeScheduleBtn();

		logger.info("*** Step 6 Expected Results: - New data is updated");
		assertTrue(isElementPresent(testCode.feeSchTable(), 5));
		assertTrue(getColumnValue(testCode.feeSchTable(), testCodeUtils.formatDecimalPoint(Integer.parseInt(newPrice))),"        new price should update in table");
		assertTrue(getColumnValue(testCode.feeSchTable(), newEffDate),"       New effDate should update in table"); 
		assertTrue(getColumnValue(testCode.feeSchTable(), fSId),"        FSID should update in table");

		logger.info("*** Step 7 Action: - Click on Save and Clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 7 Expected Results: - Error message 'Rows n and m contain overlapping fee schedule effective and expiration date ranges. Please correct and resubmit your changes.' is displayed");
		waitUntilElementIsNotVisible(testCode.errorListPanel(), 10);
		assertTrue(testCode.errorListPanel().getText().trim().contains("contain overlapping fee schedule effective and expiration date ranges"),"        Rows n and m contain overlapping fee schedule effective and expiration date ranges. Please correct and resubmit your changes.'");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Profile Detail - Fee Schedule - Reset add new FS")
	public void testXPR_307() throws Exception {
		logger.info("===== Testing - testXPR_307 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> profileTestID = daoManagerXifinRpm.getProfileTest(testDb);
		String profileTestID = "002375CP";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(profileTestID);

		logger.info("*** Step 2 Expected Results: - Profile ID information page is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be Profile Test.");

		logger.info("*** Step 3 Action: - Add free schedule");
		String validFormatEff = timeStamp.getCurrentDate("MM/dd/yyyy");
		String validFormatExp = validFormatEff;
		List<String> fs = testCode.addNewFeeSchedule(this, validFormatEff, validFormatExp, "100", true, testDb);

		logger.info("*** Step 3 Expected Results: - New FS is displayed in the table");
		assertTrue(getColumnValue(testCode.feeSchTable(), fs.get(0)), "       Fee Schedule must be added to table");
		assertTrue(getColumnValue(testCode.feeSchTable(), fs.get(1)), "       Eff date must be update to table");

		logger.info("*** Step 4 Action: - Click reset button");
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);

		logger.info("*** Step 4 Expected Result : - Verify come back to login screen");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 5 Action: - Load again single test");
		testCode.checkInputTestCodeId(profileTestID);

		logger.info("*** Step 5 Expected Results: - Verify that it's on the Profile Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be Profile Test.");

		logger.info("*** Step 6 Action: - Switch to fee schedule tab");
		testCode.clickfeeScheduleTab();
		testCodeUtils.scrollIntoView(testCode.addNewFSBtn());

		logger.info("*** Step 6 Expected Results: - The FS is not save and not displayed in the table");
		assertFalse(getColumnValue(testCode.feeSchTable(), validFormatEff), "       Eff date must not save to table");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Profile Detail - Fee Schedule - Verify Filter function in Test Base FS table")
	public void testXPR_308() throws Exception {
		logger.info("===== Testing - testXPR_308=====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		timeStamp = new TimeStamp(driver);
		testCodeUtils = new TestCodeUtils(driver);
		randomCharacter = new RandomCharacter();

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Profile) and Tab out");
//		List<String> proFileID = daoManagerXifinRpm.getProfileTest(testDb);
		String TestID = "002740LF";
		testCode.checkInputTestCodeId(TestID);

		logger.info("*** Step 2 Expected Results: - Single ID information page is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be Profile Test.");

		logger.info("*** Step 3 Action: - Fee  Schedule tab - Add new Fee Schedule and click OK button");
		String effDate = timeStamp.getCurrentDate();
		String newPrice = randomCharacter.getRandomNumericString(2);
		List<String> feeSchedule = testCode.addNewFeeSchedule(this, effDate,effDate, newPrice, true, testDb);
		String feeSchedID = feeSchedule.get(0);

		logger.info("*** Step 3 Expected Result: - New Fee Schedule is displayed in the table");
		assertTrue(getColumnValue(testCode.feeSchTable(), feeSchedID), "       New Fee Schedule ID is displayed in the table");

		logger.info("*** Step 4 Action: - Input value to Fee Schedule filter ");
		assertTrue(isElementPresent(testCode.FSIdFilter(), 5));
		testCode.inputFSIdFilter(feeSchedID);

		logger.info("*** Step 4 Expected Result: - Table will display inputted ID");
		assertTrue(getColumnValue(testCode.feeSchTable(), feeSchedID), "       Inputted Fee Schedule ID is displayed in the table");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	// Xref Tab
	@Test(priority=1, description="Single Test Detail - Xref - Add Xref with valid Data")
	public void testXPR_115() throws Exception{
		logger.info("===== Testing - testXPR_115 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should display");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		String singleID = "TESTXPR_115";
		testCode.createSingleTest(singleID, testDb, false, false, this);
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Label Test should show");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 3 Action: - Add New Xref with valid data");
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		String effDate = testCodeUtils.getNonDuplicateEffDate(this, testCode.tblXrefTable(), timeStamp.getCurrentDate());
		assertTrue(isElementPresent(testCode.addNewXrefBtn(), 5));
		List<List<String>> xrefInfo =  testCode.addXref(1, this, effDate, "", true, testDb);

		logger.info("*** Step 3 Expected Results: - New Xref information is displayed in Xref table");
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5));
		assertTrue(getColumnValue(testCode.tblXrefTable(), xrefInfo.get(0).get(0)),"        New Xref information is displayed in Xref table"+xrefInfo.get(0).get(0));

		logger.info("*** Step 4 Action: - Click save an clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 4 Expected Result : - Landing page load test code must be present");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "       Landing page load test code must be present");

		logger.info("*** Step 5 Action: - Load again test code");
		testCode.checkInputTestCodeId(singleID);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 5 Expected Results: - A New Row is added in Cross Reference Description Table and Save in Db ");
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		assertTrue(getColumnValue(testCode.tblXrefTable(), xrefInfo.get(0).get(0)),"        A New Row should be added in Cross Reference Description Table and Save in Db : "+xrefInfo.get(0).get(0));

		testCode.clickResetBtn();
	}

	@Test(priority=1, description="Single Test Detail - Xref - Edit Xref with valid data")
	public void testXPR_116() throws Exception{
		logger.info("===== Testing - testXPR_116 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		String singleID = "TESTXPR_116";
		testCode.createSingleTest(singleID, testDb, false, true, this);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 3 Action: - CLick Xref Tab and Click Edit Xref Button");
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		assertTrue(isElementPresent(testCode.colRefIdXValue(), 5));
		String crossRefDes = testCode.colRefIdXValue().getText();
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		String effDate = testCodeUtils.getNonDuplicateEffDate(this, testCode.tblXrefTable(), timeStamp.getCurrentDate());
		selectColumnValue(testCode.tblXrefTable(), crossRefDes);

		logger.info("*** Step 3 Expected Results: - Edit Xref dialog is displayed");
		assertTrue(isElementPresent(testCode.xrefDialog(), 5),"        Xref Dialog should be displayed");

		logger.info("*** Step 4 Action: - Edit Xref with valid data");
		assertTrue(isElementPresent(testCode.effDtXref(), 5));
		testCode.clearData(testCode.effDtXref());
		testCode.setEffDtXref(effDate);
		assertTrue(isElementPresent(testCode.expDtXref(), 5));
		String exDate = testCodeUtils.getNextDay(effDate);
		testCode.clearData(testCode.expDtXref());
		testCode.setExpDtXref(exDate);
		assertTrue(isElementPresent(testCode.okBtn(), 5));
		testCode.clickOkXrefBtn();

		logger.info("*** Step 4 Expected Results: - Update Effective date is displayed in table");
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		assertTrue(getColumnValue(testCode.tblXrefTable(), effDate),"        Update Effective date is displayed in table");

		logger.info("*** Step 5 Action: - Click save an clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 5 Expected Result : - Landing page load test code must be present");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "       Landing page load test code must be present");

		logger.info("*** Step 6 Action: - Load again test code");
		testCode.checkInputTestCodeId(singleID);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 6 Expected Results: - Update data is save in DB ");
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		assertTrue(getColumnValue(testCode.tblXrefTable(), effDate),"         Updated data should be saved in DB");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Xref - Verify hide grid icon")
	public void testXPR_117() throws Exception {
		logger.info("===== Testing - testXPR_117 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> singleTestCodeInfo = daoManagerXifinRpm.getSingleHaveXref(testDb);
		String singleTestCodeInfo = "ZZZ834";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
		testCode.checkInputTestCodeId(singleTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Hide Xref table");
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		assertTrue(isElementPresent(testCode.hideGridXrefTable(), 5));
		testCode.clickHideGridXrefTable();

		logger.info("*** Step 3 Expected Results: - Verify cross reference table must be hide");
		assertTrue(isElementHidden(testCode.wrapperGridXrefTable(), 5), "        Cross reference table must be hide");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Xref - Delete any row in Xref tab")
	public void testXPR_194() throws Exception {
		logger.info("===== Testing - testXPR_194 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String singleID = "TESTXPR_194";
		testCode.createSingleTest(singleID, testDb, false, true, this);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Edit Xref");
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		testCodeUtils.doubleClickOnRowByIndex(testCode.tblXrefTable(), 1);

		logger.info("*** Step 3 Expected Results: - Verify edit pop up open");
		assertTrue(isElementPresent(testCode.xrefDialog(), 5), "        Edit Xref popup must display");

		logger.info("*** Step 4 Action: - Delete cross reference");
		assertTrue(isElementPresent(testCode.crossReferenceDescrDropdownText(), 5));
		String crossDesc = testCode.crossReferenceDescrDropdownText().getText();
		assertTrue(isElementPresent(testCode.deleteCheckBoxXref(), 5));
		selectCheckBox(testCode.deleteCheckBoxXref());
		assertTrue(isElementPresent(testCode.okBtn(), 5));
		testCode.clickOkXrefBtn();

		logger.info("*** Step 4 Expected Results: - Verify mark delete row");
		assertTrue(testCodeUtils.hasClass(testCode.getRowXrefTable(2), "rowMarkedForDelete"),"        Cross reference group must be marked to remove from table");

		logger.info("*** Step 5 Action: - Save all");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 5 Expected Result : - Verify come back to login screen");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 6 Action: - Load again single test");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
		testCode.checkInputTestCodeId(singleID);

		logger.info("*** Step 6 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 7 Action: - Select Xref tab");
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();

		logger.info("*** Step 7 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		assertFalse(getColumnValue(testCode.tblXrefTable(), crossDesc), "        Cross reference must be removed from table");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Xref - Verify filter function in cross reference table")
	public void testXPR_206() throws Exception {
		logger.info("===== Testing - testXPR_206 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String singleTestCodeInfo = "GUE538";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
		testCode.checkInputTestCodeId(singleTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Filter xref descr");
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		String xrefDescr = testCode.getTextFromXrefTable(2, 2);
		String xrefEff = testCode.getTextFromXrefTable(2, 3);
		assertTrue(isElementPresent(testCode.filterXrefDescrInput(), 5));
		testCode.filterXrefDescr(xrefDescr);

		logger.info("*** Step 3 Expected Results: - Verify cross reference table must be filter correct");
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		assertTrue(getColumnValue(testCode.tblXrefTable(), xrefDescr), "        Cross reference descr must be filter correct");

		logger.info("*** Step 4 Action: - Filter xref eff");
		assertTrue(isElementPresent(testCode.filterXrefDescrInput(), 5));
		testCode.clearData(testCode.filterXrefDescrInput());
		testCode.filterXrefEffDate(xrefEff);

		logger.info("*** Step 4 Expected Results: - Verify cross reference table must be filter correct");
		assertTrue(getColumnValue(testCode.tblXrefTable(), xrefEff), "        Cross reference eff must be filter correct");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Xref - Add Xref with some require fields are empty")
	public void testXPR_276() throws Exception{
		logger.info("===== Testing - testXPR_276 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
//		List<String> singleTestInfo = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTestInfo = "EE335FX";
		testCode.checkInputTestCodeId(singleTestInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 3 Action: - Add New Xref with Cross-Reference Description is empty");
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		assertTrue(isElementPresent(testCode.addNewXrefBtn(), 5));
		testCodeUtils.scrollIntoView(testCode.addNewXrefBtn());
		testCode.clickAddNewXrefBtn();
		assertTrue(isElementPresent(testCode.effDtXref(), 5));
		testCode.setEffDtXref(timeStamp.getCurrentDate());
		assertTrue(isElementPresent(testCode.expDtXref(), 5));
		testCode.setExpDtXref(timeStamp.getCurrentDate());
		testCode.clickOkXrefBtn();

		logger.info("*** Step 3 Expected Results: - The error message is displayed");
		assertTrue(isElementPresent(testCode.xrefDialogFormError(), 5));
		assertTrue(testCode.xrefDialogFormError().getText().contains("Cross Reference Description: Field is required"),"         The error message should be displayed : Cross-Reference Description: Field is required");

		logger.info("*** Step 4 Action: - Add New Xref with effective date is empty");
		testCode.cancelBtn().click();
		//testCode.clearData(testCode.effDtXref());
		testCode.addXref(1, this, "", timeStamp.getCurrentDate(), true, testDb);

		logger.info("*** Step 4 Expected Results: - The error message is displayed");
		assertTrue(isElementPresent(testCode.xrefDialogFormError(), 5));
		assertTrue(testCode.xrefDialogFormError().getText().contains("Effective Date: Field is required"),"         The error message should be displayed : Effective Date: Field is required");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Xref - Add Xref with invalid format")
	public void testXPR_277() throws Exception{
		logger.info("===== Testing - testXPR_277 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
//		List<String> singleTestInfo = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTestInfo = "300";
		testCode.checkInputTestCodeId(singleTestInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 3 Action: - Add New Xref with invalid format effective date");
		testCode.clickXrefTab();
		testCodeUtils.scrollIntoView(testCode.addNewXrefBtn());
		testCode.clickAddNewXrefBtn();
		testCode.effDtXref().sendKeys("2015/01/01");
		testCode.effDtXref().sendKeys(Keys.TAB);

		logger.info("*** Step 3 Expected Results: - The error message is displayed");
		assertTrue(isElementPresent(testCode.messageContent(), 5));
		assertTrue(testCode.messageContent().getText().contains("Invalid date format."),"        The error message should be displayed : Invalid date format.");

		testCode.clickcloseErrorMessage();
		testCode.effDtXref().clear();
		testCode.effDtXref().sendKeys(Keys.TAB);
		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Xref - Add Xref with duplicate Cross-Reference description")
	public void testXPR_278() throws Exception{
		logger.info("===== Testing - testXPR_278 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
//		List<String> singleTestInfo = daoManagerXifinRpm.getSingleHaveXref(testDb);
		String singleTestInfo = "VOW661";
		testCode.checkInputTestCodeId(singleTestInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 3 Action: - Click Xref Tab and Click Add Xref Button");
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		String crossRefDes = testCode.colRefIdXValue().getText();
		assertTrue(isElementPresent(testCode.addNewXrefBtn(), 5));
		testCodeUtils.scrollIntoView(testCode.addNewXrefBtn());
		testCode.clickAddNewXrefBtn();

		logger.info("*** Step 3 Expected Results: - Add New Xref dialog is displayed");
		assertTrue(isElementPresent(testCode.xrefDialog(), 5),"        Xref Dialog should be displayed");

		logger.info("*** Step 4 Action: - Add New Xref with Duplicate Cross-Reference Description");
		assertTrue(isElementPresent(testCode.crossReferenceDescriptionGroup(), 5));
		testCode.selectCrossReferenceDescription(crossRefDes);
		testCode.clickOkXrefBtn();

		logger.info("*** Step 4 Expected Results: - The Error message is displayed");
		assertTrue(isElementPresent(testCode.xrefDialogFormError(), 5));
		assertTrue(testCode.xrefDialogFormError().getText().contains("A duplicate record was entered"),"        The error message should be displayed : A duplicate record was entered");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Xref - Cancel Add Xref ")
	public void testXPR_279() throws Exception{
		logger.info("===== Testing - testXPR_279 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
//		List<String> singleTestInfo = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTestInfo = "UL179TX";
		testCode.checkInputTestCodeId(singleTestInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 3 Action: - Add New Xref with effective date is empty");
		List<List<String>> xrefInfo = testCode.addXref(1, this, timeStamp.getCurrentDate(), timeStamp.getCurrentDate(), false, testDb);

		logger.info("*** Step 3 Expected Results: - Add Record is closed and new xref is not display in table");
		assertEquals(testCode.xrefDialog().getCssValue("display"), "none","        Add record should be  closed");
		assertFalse(getColumnValue(testCode.tblXrefTable(), xrefInfo.get(0).get(0)));

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Xref - Reset add Xref ")
	public void testXPR_280() throws Exception{
		logger.info("===== Testing - testXPR_280 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
//		List<String> singleTestInfo = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTestInfo = "WR652MJ";
		testCode.checkInputTestCodeId(singleTestInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");	

		logger.info("*** Step 3 Action: - Add New Xref with effective date is empty");
		List<List<String>> xrefInfo = testCode.addXref(1, this, timeStamp.getCurrentDate(), timeStamp.getCurrentDate(), true, testDb);

		logger.info("*** Step 3 Expected Results: - New xref is displayed in xref table");
		assertTrue(getColumnValue(testCode.tblXrefTable(), xrefInfo.get(0).get(0)),"           New xref should be displayed in xref table");

		logger.info("*** Step 4 Action: - Click Reset Button");
		assertTrue(isElementPresent(testCode.resetBtn(), 5));
		testCode.clickResetBtn();

		logger.info("*** Step 4 Expected Results: - Confirmation message popup is displayed");
		String confirmationMessage =  closeAlertAndGetItsText(true);
		assertNotNull(confirmationMessage,"         Confirmation message popup should be displayed");

		logger.info("*** Step 5 Action: - Load again test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "       Landing page load test code must be present");
		testCode.inputTestCodeId(singleTestInfo);
		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 5 Expected Results: - A New Row is added in Cross Reference Description Table and Save in Db ");
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		assertFalse(getColumnValue(testCode.tblXrefTable(), xrefInfo.get(0).get(0)));

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Xref - Edit xref with invalid data input")
	public void testXPR_281() throws Exception {
		logger.info("===== Testing - testXPR_281 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> singleTestCodeInfo = daoManagerXifinRpm.getSingleHaveXref(testDb);
		String singleTestCodeInfo = "ZZT565";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
		testCode.checkInputTestCodeId(singleTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Edit Xref");
		testCode.clickXrefTab();
		testCodeUtils.doubleClickOnRowByIndex(testCode.tblXrefTable(), 1);

		logger.info("*** Step 3 Expected Results: - Verify edit popup open");
		assertTrue(isElementPresent(testCode.xrefDialog(), 5), "        Edit Xref popup must display");

		logger.info("*** Step 4 Action: - Remove eff date field");
		testCode.clearData(testCode.effDtXref());
		testCode.clickOkXrefBtn();

		logger.info("*** Step 4 Expected Results: - Verify message alert");
		assertTrue(isElementPresent(testCode.xrefDialogFormError(), 5), "        Edit Xref error panel must be display");
		assertTrue(testCode.xrefDialogFormError().getText().equalsIgnoreCase("Effective Date: Field is required"), "        'Effective Date: Field is required' message must be show correct");

		logger.info("*** Step 5 Action: - Input invalid format eff date field");
		testCode.setEffDtXref("13/05-6859");
		testCode.clickOkXrefBtn();

		logger.info("*** Step 5 Expected Results: - Verify message alert");
		assertTrue(isElementPresent(testCode.xrefDialogFormError(), 5), "        Edit Xref error panel must be display");
		assertTrue(testCode.xrefDialogFormError().getText().equalsIgnoreCase("Effective Date: Please enter a valid date."), "        'Effective Date: Please enter a valid date.' message must be show correct");

        testCode.effDtXref().clear();
        testCode.effDtXref().sendKeys(Keys.TAB);

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Xref - Edit xref without select any row")
	public void testXPR_282() throws Exception {
		logger.info("===== Testing - testXPR_282 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> singleTestCodeInfo = daoManagerXifinRpm.getSingleHaveXref(testDb);
		String singleTestCodeInfo = "ZZS971";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
		testCode.checkInputTestCodeId(singleTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"       label test should show");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Edit Xref without select any row");
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"      Xref tab should show");
		testCode.clickXrefTab();
		clickHiddenPageObject(testCode.editXrefBtn(), 0);

		logger.info("*** Step 3 Expected Results: - Verify alert dialog select row before click edit");
		assertTrue(isElementPresent(testCode.alertXrefDialog(), 5), "        Edit Xref alert dialog must display");
		assertTrue(testCode.alertXrefDialogMessage().getText().equalsIgnoreCase("Please, select row"), "        Message alert must be correct");

		assertTrue(isElementPresent(testCode.WarningCloseBtn(), 5), "        Close Message error button must be displayed");
		testCode.clickWarningCloseBtn();
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Xref - Verify sort function in cross reference table")
	public void testXPR_284() throws Exception {
		logger.info("===== Testing - testXPR_284 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> singleTestCodeInfo = daoManagerXifinRpm.getSingleHaveXref(testDb);
		String singleTestCodeInfo = "ZZR214";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
		testCode.checkInputTestCodeId(singleTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Sort effective date column");
		testCode.clickXrefTab();
		testCode.clickEffDateColumnCrossReferenceTable();

		logger.info("*** Step 3 Expected Results: - Verify effective date sorted");
		assertTrue(getSortingComparisonOnTable(testCode.facilityTable(),4, "asc"),"       Effective date column should be sorted");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Profile Detail - Xref - Add Xref with valid data")
	public void testXPR_309() throws Exception {
		logger.info("===== Testing - testXPR_309 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available Profile test code");
		String profileID = "TESTXPR_309";
		testCode.createProfileTest(profileID, testDb, this);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Profile Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be profile Test.");

		logger.info("*** Step 3 Action: - Input Cross-Reference, valid Effective date and click Save and Clear button");
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		String effDate = testCodeUtils.getNonDuplicateEffDate(this, testCode.tblXrefTable(), timeStamp.getCurrentDate());
		List<List<String>> listCrossRef =  testCode.addXref(1,this,effDate,effDate,true,testDb);
		String crossRef = listCrossRef.get(0).get(0);
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 3 Expected Results: - New data is saved to database");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
		testCode.checkInputTestCodeId(profileID);
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();	
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		assertTrue(getColumnValue(testCode.tblXrefTable(), crossRef), "       Cross Reference added to table");
				
		logger.info("*** Step 4 Action: - Select newly added xRef row and click Edit button");
		scrollToElement(testCode.crossReffElement(crossRef));
		selectColumnValue(testCode.tblXrefTable(), crossRef);

		logger.info("*** Step 4 Expected Results: - Edit popup will display with read-only Cross-Reference Description field");
		assertTrue(isElementPresent(testCode.editXrefDialog(), 5), "       Cross Reference Edit Record should show.");
		assertTrue(isElementPresent(testCode.addNewCrossRefField(), 5), "        Cross Reference Description field should show.");
		assertTrue(testCodeUtils.hasClass(testCode.addNewCrossRefField(), "select2-container-disabled"),"        Cross Reference Description should be a read only field.");

		logger.info("*** Step 5 Action: - Check on Delete checkbox ");
		assertTrue(isElementPresent(testCode.deleteCheckBoxXref(), 5), "        Delete checkbox should show."); 
		selectCheckBox(testCode.deleteCheckBoxXref());
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        OK button should show."); 
		testCode.clickOkXrefBtn();

		logger.info("*** Step 5 Expected Results: - Verify that the selected row was marked as to be deleted");
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show."); 
		int indexOfRowDelete = testCode.getRowIndex(testCode.tblXrefTable(), crossRef)-1;
		assertTrue(testCodeUtils.hasClass(testCode.getRowOfCrossRefTable(indexOfRowDelete), "rowMarkedForDelete"),"        Cross Reference " + crossRef + " should be marked to be deleted.");

		logger.info("*** Step 6 Action: - Click on Save and Clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and Clear button should show."); 
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 6 Expected Results: - Verify that the deleted data was removed");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID Input field should show."); 
		testCode.checkInputTestCodeId(profileID);
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show."); 
		testCode.clickXrefTab();
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Cross Reference Information table should show."); 
		assertFalse(getColumnValue(testCode.tblXrefTable(), crossRef), "       Deleted Cross Reference " +  crossRef + " shows in Cross Reference Information table.");
		
		testCode.clickResetBtn();
	}
	
	@Test(priority = 1, description = "Profile Detail - Xref - Add Xref with cross-reference is duplicate cross")
	public void testXPR_310() throws Exception {
		logger.info("===== Testing - testXPR_310 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available Profile test code");
//		List<String> profileTestCodeInfo = daoManagerXifinRpm.getProfileTest(testDb);
		String profileTestCodeInfo = "00285064";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
		testCode.checkInputTestCodeId(profileTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Profile Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be profile Test.");
		String effDate = timeStamp.getCurrentDate();

		logger.info("*** Step 3 Action: - Add new Cross Reference with valid data ");
		List<List<String>> listCrossRef =  testCode.addXref(1,this,effDate,effDate,true,testDb);
		String crossRef = listCrossRef.get(0).get(0);

		logger.info("*** Step 3 Expected Results: - New data is saved to database");
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		assertTrue(getColumnValue(testCode.tblXrefTable(), crossRef), "       Cross Reference added to table");

		logger.info("*** Step 4 Action: - Add new Cross Reference with duplicate Cross Ref ");
		assertTrue(isElementPresent(testCode.addNewXrefBtn(), 5));
		testCodeUtils.scrollIntoView(testCode.addNewXrefBtn());
		testCode.clickAddNewXrefBtn();
		testCode.selectCrossReferenceDescription(crossRef);
		assertTrue(isElementPresent(testCode.effDtXref(), 5));
		testCode.setEffDtXref(effDate);
		assertTrue(isElementPresent(testCode.okBtn(), 5));
		testCode.clickOkXrefBtn();

		logger.info("*** Step 4 Expected Results: - Error message 'A duplicate record was entered'");
		assertTrue(isElementPresent(testCode.xrefDialogFormError(), 5));
		assertTrue(testCode.xrefDialogFormError().getText().trim().contains("A duplicate record was entered"), "       Error message 'A duplicate record was entered' is displayed");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}
	
	@Test(priority = 1, description = "Profile Detail - Xref - Add Xref with invalid data input")
	public void testXPR_311() throws Exception {
		logger.info("===== Testing - testXPR_311 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available Profile test code");
//		List<String> profileTestCodeInfo = daoManagerXifinRpm.getProfileTest(testDb);
		String profileTestCodeInfo = "00309473";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
		testCode.checkInputTestCodeId(profileTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Profile Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be profile Test.");

		logger.info("*** Step 3 Action: - Add new Cross Reference with invalid format for Effective date");
		testCode.addXref(1,this,"2015/01/01","",true,testDb);

		logger.info("*** Step 3 Expected Results: - error message 'invalid format date' is displayed");
		assertTrue(isElementPresent(testCode.errorPopupAddNewXref(), 5),"        Error message is displayed");
		testCode.clickcloseErrorMessage();

		logger.info("*** Step 4 Action: - Re-enter valid eff date and exp date that is before the eff date");
		String expDate = timeStamp.getCurrentDate();
		String effDate = testCodeUtils.getNextDay(expDate);
		assertTrue(isElementPresent(testCode.effDtXref(), 5));
		testCode.clearData(testCode.effDtXref());
		testCode.setEffDtXref(effDate);
		assertTrue(isElementPresent(testCode.expDtXref(), 5));
		testCode.setExpDtXref(expDate);
		assertTrue(isElementPresent(testCode.okBtn(), 5));
		testCode.clickOkXrefBtn();

		logger.info("*** Step 4 Expected Results: - Error message 'Expiration date must be after effective date'");
		assertTrue(isElementPresent(testCode.xrefDialogFormError(), 5));
		assertTrue(testCode.xrefDialogFormError().getText().trim().contains("Expiration date must be after effective date"), "       Error message 'Expiration date must be after effective date' is displayed");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}
		
	@Test(priority = 1, description = "Profile Detail - Xref - Cancel add new xref information")
	public void testXPR_312() throws Exception {
		logger.info("===== Testing - testXPR_312 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Navigate to Xifin Portal Test Code and login with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available Profile test code");
//		List<String> profileTestCodeInfo = daoManagerXifinRpm.getProfileTest(testDb);
		String profileTestCodeInfo = "003245OR";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
		testCode.checkInputTestCodeId(profileTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Profile Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be profile Test.");

		logger.info("*** Step 3 Action: - Input Cross-Reference, valid Effective date and click Save and Clear button");
		String effDate = timeStamp.getCurrentDate();
		List<List<String>> listCrossRef = testCode.addXref(1,this,effDate,effDate,false,testDb);
		String crossRef = listCrossRef.get(0).get(0);

		logger.info("*** Step 3 Expected Results: - New data is not saved to database");
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		assertFalse(getColumnValue(testCode.tblXrefTable(), crossRef), "       Cross Reference will be cancel");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}
	
	@Test(priority = 1, description = "Profile Detail - Xref - Update Xref with valid data")
	public void testXPR_313() throws Exception {
		logger.info("===== Testing - testXPR_313 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String singleID = "TESTXPR_313";
		testCode.createSingleTest(singleID, testDb, false, true, this);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Select any row and click on Edit button");
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		List<String> listCRID = testCode.getAllFieldsInFirstRow(testCode.tblXrefTable());
		String CRId = listCRID.get(1);
		String newEffDate = testCodeUtils.getNonDuplicateEffDate(this, testCode.tblXrefTable(),timeStamp.getCurrentDate());
		selectColumnValue(testCode.tblXrefTable(), CRId);

		logger.info("*** Step 3 Expected Results: - Edit popup will display with Cross Ref is read only field");
		assertTrue(isElementPresent(testCode.addNewCrossRefField(), 5));
		Thread.sleep(3000);
		assertTrue(testCodeUtils.hasClass(testCode.addNewCrossRefField(), "select2-container-disabled"),"        Cross Reference Description is read only field");

		logger.info("*** Step 4 Action: - Update Effective date and Expiration date with valid data and click Reset button");
		String exDate = testCodeUtils.getNextDay(newEffDate);
		assertTrue(isElementPresent(testCode.effDtXref(), 5));
		testCode.clearData(testCode.effDtXref());
		assertTrue(isElementPresent(testCode.expDtXref(), 5));
		testCode.clearData(testCode.expDtXref());
		testCode.setEffDtXref(newEffDate);
		testCode.setExpDtXref(exDate);
		assertTrue(isElementPresent(testCode.okBtn(), 5));
		testCode.clickOkXrefBtn();

		logger.info("*** Step 4 Expected Results: - Updated data will display on table");
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		assertTrue(getColumnValue(testCode.tblXrefTable(), newEffDate), "       Updated data will display on table");

		logger.info("*** Step 5 Action: - Click Save and Clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 5 Expected Results: - Updated data will save to Database");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
		testCode.checkInputTestCodeId(singleID);
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		assertTrue(getColumnValue(testCode.tblXrefTable(), newEffDate), "       Updated data will display on table");
				
		logger.info("*** Step 6 Action: - Select newly added xRef row and click Edit button");
		selectColumnValue(testCode.tblXrefTable(), CRId);

		logger.info("*** Step 6 Expected Results: - Edit popup will display with read-only Cross-Reference Description field");
		assertTrue(isElementPresent(testCode.editXrefDialog(), 5), "       Cross Reference Edit Record should show.");
		assertTrue(isElementPresent(testCode.addNewCrossRefField(), 5), "        Cross Reference Description field should show.");
		assertTrue(testCodeUtils.hasClass(testCode.addNewCrossRefField(), "select2-container-disabled"),"        Cross Reference Description should be a read only field.");

		logger.info("*** Step 7 Action: - Check on Delete checkbox ");
		assertTrue(isElementPresent(testCode.deleteCheckBoxXref(), 5), "        Delete checkbox should show."); 
		selectCheckBox(testCode.deleteCheckBoxXref());
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        OK button should show."); 
		testCode.clickOkXrefBtn();

		logger.info("*** Step 7 Expected Results: - Verify that the selected row was marked as to be deleted");
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show."); 
		int indexOfRowDelete = testCode.getRowIndex(testCode.tblXrefTable(), CRId)-1;
		assertTrue(testCodeUtils.hasClass(testCode.getRowOfCrossRefTable(indexOfRowDelete), "rowMarkedForDelete"),"        Cross Reference " + CRId + " should be marked to be deleted.");

		logger.info("*** Step 8 Action: - Click on Save and Clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and Clear button should show."); 
		testCode.clickSaveAndClearBtn();
	}
	
	@Test(priority = 1, description = "Profile Detail - Xref - Reset the update Xref")
	public void testXPR_314() throws Exception {
		logger.info("===== Testing - testXPR_314 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> SingleCodeInfo = daoManagerXifinRpm.getSingleHaveXref(testDb);
		String singleID = "ZZN763";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
		testCode.checkInputTestCodeId(singleID);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Xref tab - Select any row and click edit button");
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		List<String> listCRID = testCode.getAllFieldsInFirstRow(testCode.tblXrefTable());
		String CRId = listCRID.get(1);
		String newEffDate = testCodeUtils.getNonDuplicateEffDate(this, testCode.tblXrefTable(),timeStamp.getCurrentDate());
		selectColumnValue(testCode.tblXrefTable(), CRId);

		logger.info("*** Step 3 Expected Results: - Edit popup will display with Cross Ref is read only field");
		assertTrue(isElementPresent(testCode.addNewCrossRefField(), 5));
		assertTrue(testCodeUtils.hasClass(testCode.addNewCrossRefField(), "select2-container-disabled"),"        Cross Reference Description is read only field");

		logger.info("*** Step 4 Action: - Update Effective date and Expiration date with valid data and click Reset button");
		String exDate = testCodeUtils.getNextDay(newEffDate);
		assertTrue(isElementPresent(testCode.effDtXref(), 5));
		testCode.clearData(testCode.effDtXref());
		assertTrue(isElementPresent(testCode.expDtXref(), 5));
		testCode.clearData(testCode.expDtXref());
		testCode.setEffDtXref(newEffDate);
		testCode.setExpDtXref(exDate);
		assertTrue(isElementPresent(testCode.okBtn(), 5));
		testCode.clickOkXrefBtn();
		
		logger.info("*** Step 4 Expected Results: - Update Effective date and Expiration date will display in table");
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		assertTrue(getColumnValue(testCode.tblXrefTable(), newEffDate),"        Update Effective date and Expiration date will display in table");

		logger.info("*** Step 5 Action: - Confirmation popup will display and click OK btn");
		assertTrue(isElementPresent(testCode.resetBtn(), 5));
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);

		logger.info("*** Step 5 Expected Results: - Updated data will not show");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
		testCode.checkInputTestCodeId(singleID);
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		assertFalse(getColumnValue(testCode.tblXrefTable(), newEffDate), "       Updated data should not be displayed on table");

		testCode.clickResetBtn();
	}
	
	@Test(priority = 1, description = "Profile Detail - Xref - Delete Xref")
	public void testXPR_315() throws Exception {
		logger.info("===== Testing - testXPR_315 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String singleID = "TESTXPR_315";
		testCode.createSingleTest(singleID, testDb, false, true,this);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Xref tab - Select any row and click edit button");
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		String crossRef = testCode.checkXrefInXrefTable();
		selectColumnValue(testCode.tblXrefTable(), crossRef);

		logger.info("*** Step 3 Expected Results: - Edit popup will display with Cross Ref is read only field");
		assertTrue(isElementPresent(testCode.editXrefDialog(), 5), "       Cross Reference will be displayed");
		assertTrue(isElementPresent(testCode.addNewCrossRefField(), 5));
		assertTrue(testCodeUtils.hasClass(testCode.addNewCrossRefField(), "select2-container-disabled"),"        Cross Reference Description is read only field");

		logger.info("*** Step 4 Action: - Check on Delete checkbox ");
		assertTrue(isElementPresent(testCode.deleteCheckBoxXref(), 5)); 
		selectCheckBox(testCode.deleteCheckBoxXref());
		assertTrue(isElementPresent(testCode.okBtn(), 5)); 
		testCode.clickOkXrefBtn();

		logger.info("*** Step 4 Expected Results: - Verify row had been mark to remove from table");
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show."); 
		int indexOfRowDelete = testCode.getRowIndex(testCode.tblXrefTable(), crossRef)-1;
		assertTrue(testCodeUtils.hasClass(testCode.getRowOfCrossRefTable(indexOfRowDelete), "rowMarkedForDelete"),"        Payor must be marked to remove from table");

		logger.info("*** Step 5 Action: - Click on Save and Clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and Clear button should show."); 
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 5 Expected Results: - Deleted data is removed from table");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show."); 
		testCode.checkInputTestCodeId(singleID);
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show"); 
		testCode.clickXrefTab();
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show."); 
		assertFalse(getColumnValue(testCode.tblXrefTable(), crossRef), "       Deleted Cross Ref is removed from table");

		testCode.clickResetBtn();
	}
	
	@Test(priority = 1, description = "Profile Detail - Xref - Reset the delete xref information")
	public void testXPR_316() throws Exception {
		logger.info("===== Testing - testXPR_316 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5));
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> SingleCodeInfo = daoManagerXifinRpm.getSingleHaveXref(testDb);
		String singleID = "ZZL551";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
		testCode.checkInputTestCodeId(singleID);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5));
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Xref tab - Select any row and click edit button");
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		List<String> listCRID = testCode.getAllFieldsInFirstRow(testCode.tblXrefTable());
		String CRId = listCRID.get(0);
		selectColumnValue(testCode.tblXrefTable(), CRId);

		logger.info("*** Step 3 Expected Results: - Edit popup will display with Cross Ref is read only field");
		assertTrue(isElementPresent(testCode.editXrefDialog(), 5), "       Cross Reference will be displayed");
		assertTrue(isElementPresent(testCode.addNewCrossRefField(), 5));
		assertTrue(testCodeUtils.hasClass(testCode.addNewCrossRefField(), "select2-container-disabled"),"        Cross Reference Description is read only field");

		logger.info("*** Step 4 Action: - Check on Delete checkbox ");
		assertTrue(isElementPresent(testCode.deleteCheckBoxXref(), 5)); 
		selectCheckBox(testCode.deleteCheckBoxXref());
		assertTrue(isElementPresent(testCode.okBtn(), 5)); 
		testCode.clickOkXrefBtn();

		logger.info("*** Step 4 Expected Results: - Verify row had been mark to remove from table");
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show."); 
		int indexOfRowDelete = testCode.getRowIndex(testCode.tblXrefTable(), CRId)-1;
		assertTrue(testCodeUtils.hasClass(testCode.getRowOfCrossRefTable(indexOfRowDelete), "rowMarkedForDelete"),"        Payor must be marked to remove from table");

		logger.info("*** Step 5 Action: - Click on Reset button");
		assertTrue(isElementPresent(testCode.resetBtn(), 5)); 
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);

		logger.info("*** Step 5 Expected Results: - Deleted data is not removed from table");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test id input should show.");
		testCode.checkInputTestCodeId(singleID);
		assertTrue(isElementPresent(testCode.xrefTab(), 5),"        Xref tab should show");
		testCode.clickXrefTab();
		assertTrue(isElementPresent(testCode.tblXrefTable(), 5),"        Xref table should show.");
		assertTrue(getColumnValue(testCode.tblXrefTable(), CRId), "       Deleted Cross Ref is not removed from table");

		testCode.clickResetBtn();
	}
	
	// Exclusion Tab
	@Test(priority = 1, description = "Single test Detail - Exclusions - Add Payor Group Exclusion")
	public void testXPR_118() throws Exception {
		logger.info("===== Testing - testXPR - 118 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String testID = "AUTOTESTXPR_118";
		testCode.createSingleTest(testID, testDb, false, false, this);
		daoManagerPlatform.deletePyrGrpExclFromTESTPYRGRPEXCLByTestAbbrev(testID, testDb);

		logger.info("*** Step 2 Expected Results: - Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input should show.");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label should show.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Actions: - Exclusion tab - Add new Payor Group Exclusion and Click Save and Clear button");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5), "        Payor Group Exclusion table is displayed");
		List<String> payorGroup = testCode.checkPyrGrpExclusion(this,true, testDb);

		logger.info("*** Step 3 Expected Results: - New row is displayed in the table");
		assertTrue(getColumnValue(testCode.pyrGrpExclusionTbl(), payorGroup.get(0)), "       New Payor Group is added to table");

		logger.info("*** Step 4 Actions: - Click Save and Clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 4 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 5 Action: - Load again test code");
		testCode.checkInputTestCodeId(testID);
		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);

		logger.info("*** Step 5 Expected Result : - Test Code loaded page must be present");
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();
		assertTrue(getColumnValue(testCode.pyrGrpExclusionTbl(), payorGroup.get(0)), "       New Payor Group is added to table");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Exclusions - Delete Payor Group")
	public void testXPR_119() throws Exception {
		logger.info("===== Testing - testXPR - 119 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String testID = "TESTXPR_119";
		testCode.createSingleTest(testID, testDb, false, false, this);

		logger.info("*** Step 2 Expected Results: - System display test code with information");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input should show.");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label should show.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Actions: - Exclusion tab - Add new Payor Group Exclusion and Click Save and Clear button");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5), "        Payor Group Exclusion table is displayed");
		List<String> payorGroup = testCode.checkPyrGrpExclusion(this, true, testDb);
		logger.info(payorGroup);
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 3 Expected Results: - New Data is added to table");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		testCode.checkInputTestCodeId(testID);
		////waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();
//		assertTrue(getColumnValue(testCode.pyrGrpExclusionTbl(), payorGroup.get(0)), "       New Payor Group is added to table");

		logger.info("*** Step 4 Actions: - Exclusion tab: Payor Group - Select any row and click edit");
		selectColumnValue(testCode.pyrGrpExclusionTbl(), "HMO");

		logger.info("*** Step 4 Expected Results: - The Edit Popup is displayed");
		String disabledClass = testCode.pyrGrpIdDropdown().getAttribute("class");
		assertTrue(disabledClass.contains("select2-container-disabled"), "        Dropdown list must have disabled class");
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button must be displayed");
		assertTrue(isElementPresent(testCode.cancelBtn(), 5), "        Cancel button must be displayed");

		logger.info("*** Step 5 Actions: - Click delete checkbox and submit");
		assertTrue(isElementPresent(testCode.deleteCheckbok(), 5), "        Delete checkbox must be displayed");
		selectCheckBox(testCode.deleteCheckbok());
		testCode.clickOkPyrGrpExclusion();

		logger.info("*** Step 5 Expected Results: - The Payor Group is marked as deleted line with middle line cross the line");
		int indexOfRowDelete = testCode.getRowIndex(testCode.pyrGrpExclusionTbl(), payorGroup.get(0))-1;
		assertTrue(testCodeUtils.hasClass(testCode.getRowOfPyrGrpExclusion(indexOfRowDelete), "rowMarkedForDelete"),"        Payor must be marked to remove from table");

		logger.info("*** Step 6 Actions: - Click Save and Clear Button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 6 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 7 Action: - Load again test code");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input should show.");
		testCode.checkInputTestCodeId(testID);
		////waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 7 Expected Result : - The selected row is removed out of list");
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5),"        Payor Group Exclusion table should show.");
		assertFalse(getColumnValue(testCode.pyrGrpExclusionTbl(), payorGroup.get(0)),"       Payor in Payor group has been deleted");	

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Exclusions - Verify Hide grid icon in Payor Group table")
	public void testXPR_120() throws Exception {
		logger.info("===== Testing - testXPR - 120 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest = "WI898SN";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - System display test code With Information");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input should show.");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label should show.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Actions: - Click on Exclusion tab");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();
		String arrowBeforeClass = testCode.arrowShowHidePyrGrpBtn().getAttribute("class");

		logger.info("*** Step 3 Expected Results: - System will display 2 table: Payor Group Exclusion table and Payor Exclusion table");
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5), "        Payor Group Exclusion table should show.");
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5), "        Payor Exclusion table should show.");
		assertTrue(arrowBeforeClass.contains("ui-icon-circle-triangle-n"), "        Arrow icon must up");
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTblHeader(), 5), "        Payor Group Table header should show.");
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTblContent(), 5), "        Payor Group Table content should show.");

		logger.info("*** Step 4 Actions: - Click on [Hide gird] icon");
		assertTrue(isElementPresent(testCode.showHidePyrGrpBtn(), 5),"        Show/Hide Payor Group button should show.");
		testCode.clickShowHidePyrGrpBtn();

		logger.info("*** Step 4 Expected Results: - System will hide the gird and icon change to [Show gird] icon");
		assertEquals(testCode.pyrGrpExclusionTblHeader().getCssValue("display"), "none");
		assertEquals(testCode.pyrGrpExclusionTblContent().getCssValue("display"), "none");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Exclusions - Add Payor with valid input")
	public void testXPR_121() throws Exception {
		logger.info("===== Testing - testXPR - 121 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String testID = "TESTXPR_121";
		testCode.createSingleTest(testID, testDb, false, false, this);

		logger.info("*** Step 2 Expected Results: - System display test code with information");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input should show.");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label should show.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Actions: - Click on Exclusion tab");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab must be displayed");
		testCode.clickExclusionTab();

		logger.info("*** Step 3 Expected Results: - System will display 2 tables Payor Group Exclusion table and Payor Exclusion table");
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5), "        Payor Group Exclusion table is displayed");
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5), "        Payor Exclusion table is displayed");

		logger.info("*** Step 4 Actions: - Add new Payor Exclusion and Click Save and Clear button");
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5), "        Payor Exclusion table should show.");
		String payorID = testCode.addPayorExclusions(2, this, true, testDb).get(0);
		assertTrue(getColumnValue(testCode.pyrExclusionTbl(), payorID), "       New Payor ID data is added to table");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 4 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 5 Action: - Load again test code");
		testCode.checkInputTestCodeId(testID);
		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 5 Expected Results: - A new row is added on Cross Reference information table and save in Db");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();
		assertTrue(getColumnValue(testCode.pyrExclusionTbl(), payorID), "       New Payor is added to table");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single test Detail - Exclusions - Delete Payor in the Payor List")
	public void testXPR_122() throws Exception {
		logger.info("===== Testing - testXPR - 122 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Enter an valid Test ID (Single) and Tab out");
		String testID = "TESTXPR_122";
		testCode.createSingleTest(testID, testDb, false, false, this);

		logger.info("*** Step 2 Expected Results: - System display test code with information");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Actions: - Click on Exclusion tab");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab must be displayed");
		testCode.clickExclusionTab();

		logger.info("*** Step 3 Expected Results: - Payor Group Exclusion table and Payor Exclusion table must be displayed");
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5), "        Payor Group Exclusion table is displayed");
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5), "        Payor Exclusion table is displayed");

		logger.info("*** Step 4 Actions: - Prepare data to test - Add new Payor Exclusion and Click Save and Clear button");
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5), "        Payor Exclusion table should show.");
		String payorID = testCode.addPayorExclusions(2, this, true, testDb).get(0);
		assertTrue(getColumnValue(testCode.pyrExclusionTbl(), payorID), "       New Payor is added to table");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 4 Expected Results: - New Data is added to table");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		testCode.checkInputTestCodeId(testID);
		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();
		assertTrue(getColumnValue(testCode.pyrExclusionTbl(), payorID), "       New Payor is added to table");

		logger.info("*** Step 5 Actions: - Exclusion tab: Payor Exclusions table - Select any row and click edit");
		selectColumnValue(testCode.pyrExclusionTbl(), payorID);

		logger.info("*** Step 5 Expected Results: - Edit Popup is displayed");
		String disabledClass = testCode.addPayorId().getAttribute("class");
		assertTrue(disabledClass.contains("field_disabled"), "        Field text must have disabled class");
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button  should show.");
		assertTrue(isElementPresent(testCode.cancelBtn(), 5), "        Cancel button should show.");

		logger.info("*** Step 6 Actions: - Click delete checkbox and submit");
		assertTrue(isElementPresent(testCode.deleteCheckbok(), 5), "        Delete checkbox should show.");
		selectCheckBox(testCode.deleteCheckbok());
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button should show.");
		testCode.clickOkPyrExclusion();
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5),"        Payor Exclusions table should show.");

		logger.info("*** Step 6 Expected Results: - The selected row is marked as delete row");
		int indexOfRowDelete = testCode.getRowIndex(testCode.pyrExclusionTbl(), payorID)-1;
		assertTrue(testCodeUtils.hasClass(testCode.getRowOfPyrExclusion(indexOfRowDelete), "rowMarkedForDelete"),"        Payor must be marked to remove from table");

		logger.info("*** Step 7 Actions: - Click Save and Clear Button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 7 Expected Results: - Go back to load test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 8 Action: - Load again test code");
		testCode.checkInputTestCodeId(testID);
		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page should show.");

		logger.info("*** Step 8 Expected Result : - The Selected PayorId row is deleted");
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5),"        Payor Exclusions table should show.");
		assertFalse(getColumnValue(testCode.pyrExclusionTbl(), payorID),"       Payor should be deleted");	

		testCode.clickResetBtn();
	}
	
	@Test(priority = 1, description = "Single test Detail - Exclusions - Verify Hide grid icon")
	public void testXPR_123() throws Exception {
		logger.info("===== Testing - testXPR - 123 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        TestID input should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest = "11666";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - System display test code with information");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input should show.");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label should show.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Actions: - Click on Exclusion tab");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();
		String arrowBeforeClass = testCode.arrowShowHidePyrBtn().getAttribute("class");

		logger.info("*** Step 3 Expected Results: - Payor Exclusion table and Payor Exclusion table must be displayed");
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5), "        Payor Group Exclusion table should show.");
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5), "        Payor Exclusion table should show.");
		assertTrue(arrowBeforeClass.contains("ui-icon-circle-triangle-n"), "        Arrow icon must up");
		assertTrue(isElementPresent(testCode.pyrExclusionTblHeader(), 5), "        Payor Group Table header should show.");
		assertTrue(isElementPresent(testCode.pyrExclusionTblContent(), 5), "        Payor Group Table content should show.");

		logger.info("*** Step 4 Actions: - Click on Hide gird icon");
		assertTrue(isElementPresent(testCode.showHidePyrBtn(), 5),"        Show/Hide button should show.");
		testCode.clickShowHidePyrBtn();

		logger.info("*** Step 4 Expected Results: - System will hide the gird and icon change to show gird icon");
		assertEquals(testCode.pyrExclusionTblHeader().getCssValue("display"), "none","        Show/Hide icon is down arrow. Table and content is hide");
		assertEquals(testCode.pyrExclusionTblContent().getCssValue("display"), "none","       Show/Hide icon is down arrow. Table and content is hide");
	
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Exclusions - Reset Delete payorID")
	public void testXPR_124() throws Exception {
		logger.info("===== Testing - testXPR - 124 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String testID = "TESTXPR_124";
		testCode.createSingleTest(testID, testDb, false, false, this);

		logger.info("*** Step 2 Expected Results: - System display test code with information");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input should show.");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label should show.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Actions: - Click on Exclusion tab, Add new Payor Exclusion and click Save and Clear button");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();
		String payorId = testCode.addPayorExclusions(1, this, true, testDb).get(0);
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 3 Expected Results: New data added to table");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		testCode.checkInputTestCodeId(testID);
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusions Tab should show.");
		testCode.clickExclusionTab();
		assertTrue(getColumnValue(testCode.pyrExclusionTbl(), payorId), "       New data is added to table");

		logger.info("*** Step 4 Actions: -CLick on Exclusion - payor Exclusion Table -  Select any row and click edit");
		String testCodeName = testCode.selectFirstRowExclusionTbl(this, testID,testDb);
		selectColumnValue(testCode.pyrExclusionTbl(), testCodeName);

		logger.info("*** Step 4 Expected Results: - Edit Popup is displayed");
		String disabledClass = testCode.addPayorId().getAttribute("class");
		assertTrue(disabledClass.contains("field_disabled"), "        Field text must have disabled class");
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button should show.");
		assertTrue(isElementPresent(testCode.cancelBtn(), 5), "        Cancel button should show.");

		logger.info("*** Step 5 Actions: - Click delete checkbox and submit");
		assertTrue(isElementPresent(testCode.deleteCheckbok(), 5), "        Delete checkbox should show.");
		selectCheckBox(testCode.deleteCheckbok());
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button should show.");
		testCode.clickOkPyrExclusion();
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5),"        Payor Exclusions table should show.");

		logger.info("*** Step 5 Expected Results: - The selected row is marked as delete record");
		assertTrue(testCode.checkDeleteRowInPyrExclusionTbl(),"        Selected row mark as delete.");

		logger.info("*** Step 6 Actions: - Click Reset Button");
		assertTrue(isElementPresent(testCode.resetBtn(), 5),"        Reset button should show.");
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);

		logger.info("*** Step 6 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 7 Action: - Load again test code");
		testCode.checkInputTestCodeId(testID);
		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page should show");

		logger.info("*** Step 7 Expected Result : - The selected row is not removed");
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5),"        Payor Exclusions table should show.");
		assertTrue(getColumnValue(testCode.pyrExclusionTbl(), testCodeName), "       New data is not removed to table");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single test Detail - Exclusions - Edit Payor Exclusion without select any row")
	public void testXPR_125() throws Exception {
		logger.info("===== Testing - testXPR - 125 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest = "QWD560";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - System display test code with information");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input should show.");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label should show.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Actions: - Click on Exclusion tab");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();

		logger.info("*** Step 3 Expected Results: - Payor Group Exclusion table and Payor Exclusion table must be displayed");
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5), "        Payor Group Exclusion table should show.");
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5), "        Payor Exclusion table should show.");

		logger.info("*** Step 4 Actions: - Click edit without select any row");
		assertTrue(isElementPresent(testCode.pyrExclusionEditBtn(), 5), "        Edit Payor Exclusions button should show.");
		testCode.clickPyrExclusionEditBtn();

		logger.info("*** Step 4 Expected Results: - Error popup must display");
		assertTrue(isElementPresent(testCode.widgetOverlay(), 5), "        Widget Overlay should show.");
		assertTrue(isElementPresent(testCode.alertPyrExclusionTbl(), 5), "        Error popup should show.");

		assertTrue(isElementPresent(testCode.WarningCloseBtn(), 5), "        Close Message error button must be displayed");
		testCode.clickWarningCloseBtn();
		testCode.clickResetBtn();
	}
	
	@Test(priority = 1, description = "Single Test Detail - Exclusions - Reset the delete Payor Group")
	public void testXPR_195() throws Exception {
		logger.info("===== Testing - testXPR_195 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test Id input should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		String singleTest = daoManagerXifinRpm.getSingleTestHaveExclusion(testDb);
		String singleTest = "AA158ES";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - System displays test code information single test screen");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input should show.");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label should show.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Actions: - Click on Exclusion tab");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();

		logger.info("*** Step 3 Expected Results: - Payor Group Exclusion table and Payor Exclusion table must be displayed");
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5), "        Payor Group Exclusion table should show.");
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5), "        Payor Exclusion table should show.");

		logger.info("*** Step 4 Actions: - Exclusions tab: Payor Group table - Select any row and click edit");
		String testCodeName =  testCode.selectFirstRowPyrGrpExclusionTbl(this, singleTest, testDb);
		selectColumnValue(testCode.pyrGrpExclusionTbl(), testCodeName);

		logger.info("*** Step 4 Expected Results: - First row is selected. Popup is displayed");
		assertTrue(isElementPresent(testCode.pyrGrpIdDropdown(), 5), "        Payor GroupId Dropdown should show");
		String disabledClass = testCode.pyrGrpIdDropdown().getAttribute("class");
		assertTrue(disabledClass.contains("select2-container-disabled"), "        Field text must have disabled class" + disabledClass);
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button should show.");
		assertTrue(isElementPresent(testCode.cancelBtn(), 5), "        Cancel button should show.");

		logger.info("*** Step 5 Actions: - Click delete checkbox and submit");
		assertTrue(isElementPresent(testCode.deleteCheckbok(), 5), "        Delete checkbox should show.");
		selectCheckBox(testCode.deleteCheckbok());
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Ok button should show.");
		testCode.clickOkPyrGrpExclusion();
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5),"        Payor Group Exclusions table should show.");

		logger.info("*** Step 5 Expected Results: - Row has middle line cross");
		assertTrue(testCode.checkDeleteRowInPyrGrpExclusionTbl(),"        Selected row mark as delete.");

		logger.info("*** Step 6 Actions: - Click Reset Button");
		assertTrue(isElementPresent(testCode.resetBtn(), 5),"        Reset button should show.");
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);

		logger.info("*** Step 6 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 7 Action: - Load again test code");
		testCode.checkInputTestCodeId(singleTest);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page should show.");

		logger.info("*** Step 7 Expected Result : - The selected row is not remove out of list");
		testCode.clickExclusionTab();
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5),"        Payor Group Exclusions table should show.");
		assertTrue(getColumnValue(testCode.pyrGrpExclusionTbl(), testCodeName),"       The selected row is not ot remove out of list");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Exclusions - Verify Filter function in Payor Group")
	public void testXPR_207() throws Exception {
		logger.info("===== Testing - testXPR - 207 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String testID = "AUTOTESTXPR_207";
		testCode.createSingleTest(testID, testDb, false, false, this);
		daoManagerPlatform.deletePyrGrpExclFromTESTPYRGRPEXCLByTestAbbrev(testID, testDb);

		logger.info("*** Step 2 Expected Results: - System displays test code information single screen");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input should show.");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label should show.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Actions: - Click on Exclusion tab");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();

		logger.info("*** Step 3 Expected Results: - Payor Group Exclusion table and Payor Exclusion table must be displayed");
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5), "        Payor Group Exclusion table should show.");
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5), "        Payor Exclusion table should show.");

		logger.info("*** Step 4 Actions: - Click add button on payor group exclusion table and add multi payor group");
		List<String> dataAdded = testCode.checkPyrGrpExclusion(this, true, testDb);
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5),"        Payor Group Exclusions table should show.");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 4 Expected Results: - Go back to load test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input field should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 5 Action: - Load again test code");
		testCode.checkInputTestCodeId(testID);
		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page should show.");

		logger.info("*** Step 5 Expected Results : - New payor group has been added");
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5),"        Payor Group Exclusion table should show.");
		assertTrue(getColumnValue(testCode.pyrGrpExclusionTbl(), dataAdded.get(0)),"        New payor group has been added");

		logger.info("*** Step 6 Actions : - Check function filter in exclusion tab : input value in group Id filter");
		int idx = new Random().nextInt(dataAdded.size());
		String grpId = dataAdded.get(idx);
		assertTrue(isElementPresent(testCode.grpIdExclusionFilter(), 5),"        Group ID Exclusion filter should show.");
		testCode.setGrpIdFilter(grpId);

		logger.info("*** Step 6 Expected Results : - System should perform filter correctly");
		assertTrue(getTableTotalRowSize(testCode.pyrGrpExclusionTbl()) > 0,"        System should perform filter correctly");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail-Exclusions - Verify Filter function in Payor Exclusions")
	public void testXPR_208() throws Exception {
		logger.info("===== Testing - testXPR - 208 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String testID = "TESTXPR_208";
		testCode.createSingleTest(testID, testDb, false,false, this);

		logger.info("*** Step 2 Expected Results: - System displays test code information Profile/Single screen");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input should show.");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label should show.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Actions : - Check function filter in Exclusions Tab : Input value in Payor Id filter");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5),"       Exclusion tab should show.");
		testCode.clickExclusionTab();
		String testCodeName = testCode.selectFirstRowExclusionTbl(this, testID,testDb);
		assertTrue(isElementPresent(testCode.pyrIdExclusionFilter(), 5),"        Payor ID Exclusion filter should show.");
		testCode.setPyrIdFilter(testCodeName);

		logger.info("*** Step 3 Expected Results : - System should perform filter correctly");
		assertTrue(getColumnValue(testCode.pyrExclusionTbl(), testCodeName),"        System should perform filter correctly");	

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}
	
	@Test(priority = 1, description = "Single Test Detail - Exclusion - Add Payor Group with duplicate GroupID")
	public void testXPR_285() throws Exception {
		logger.info("===== Testing - testXPR - 285 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		randomCharacter = new RandomCharacter(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
		String testID ="TESTXPR_285" + randomCharacter.getRandomNumericString(3);
		testCode.createSingleTest(testID, testDb, false, false, this);

		logger.info("*** Step 2 Expected Results: - Single test information page is displayed");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input should show.");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label should show.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Actions: - Click on Exclusion tab");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab must be displayed");
		testCode.clickExclusionTab();

		logger.info("*** Step 3 Expected Results: - Payor Group Exclusion table and Payor Exclusion table must be displayed");
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5), "        Payor Group Exclusion table should show.");
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5), "        Payor Exclusion table should show.");

		logger.info("*** Step 4 Actions: - Click add button on payor exclusion table and add multi payor");
		List<String> pyrGrpInfo =  testCode.checkPyrGrpExclusion(this, true, testDb);
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 4 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 5 Action: - Load again test code");
		testCode.checkInputTestCodeId(testID);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page should show.");

		logger.info("*** Step 5 Expected Results : - New payor group has been added");
		assertTrue(getColumnValue(testCode.pyrGrpExclusionTbl(), pyrGrpInfo.get(0)),"       New payor group has been added");
		String existGroupId = testCode.getAllFieldsInFirstRow(testCode.pyrGrpExclusionTbl()).get(1);

		logger.info("*** Step 6 Actions : - Click add button and Select Group Id is duplicate with existing one");
		testCodeUtils.scrollIntoView(testCode.addPayorGroup());
		testCode.clickAddPayorGroupBt();
		testCode.selectPayorGroupDescriptionExclusionTab(existGroupId);
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button should show.");
		testCode.clickOkPyrGrpExclusion();

		logger.info("*** Step 6 Expected Results : - The message :'A Duplicate record was entered' is displayed ");
		assertTrue(isElementPresent(testCode.errorMessage(), 5),"        Error message should show.");
		assertTrue(testCode.errorMessage().getText().contains("A duplicate record was entered"),"        The message :'A Duplicate record was entered' is displayed ");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}
	
	@Test(priority = 1, description = "Single Test Detail - Exclusions - Cancel add record")
	public void testXPR_286() throws Exception {
		logger.info("===== Testing - testXPR - 286 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input an valid Test ID (Single) and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest = "HRJ162";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - System displays test code information");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input should show.");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label should show.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Actions: - Click on Exclusion tab");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();

		logger.info("*** Step 3 Expected Results: - Payor Group Exclusion table and Payor Exclusion table must be displayed");
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5), "        Payor Group Exclusion table should show.");
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5), "        Payor Exclusion table should show.");

		logger.info("*** Step 4 Actions: - Click add button on payor exclusion table -Select groupId in the select Box - click cancel button");
		List<String> data = testCode.checkPyrGrpExclusion(this, false, testDb);

		logger.info("*** Step 4 Expected Results: - Add record pop up is closed and new record is cancel");
		assertFalse(getColumnValue(testCode.pyrGrpExclusionTbl(),data.get(0)),"        Add record pop up is closed and new record is cancel");

		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Exclusions - Reset the add payor Group")
	public void testXPR_287() throws Exception {
		logger.info("===== Testing - testXPR - 287 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"          Test Code Page Title should show.");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> SingleCodeInfo = daoManagerXifinRpm.getSingleTest(testDb);
		String singleID = "XL979YB";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input should show.");
		testCode.checkInputTestCodeId(singleID);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label should show.");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Exclusions Tab - Click add icon - Select GroupID - Click on Ok button ");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5),"        Exclusion Tab should show.");
		assertTrue(isElementPresent(testCode.addPayorGroup(), 5),"        Add Payor Group should show.");
		List<String> listData = testCode.checkPyrGrpExclusion(this, true, testDb);
		String PayorId = listData.get(0);

		logger.info("*** Step 3 Expected Results: - New data added to table");
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5),"        Payor Group Table should show.");
		assertTrue(getColumnValue(testCode.pyrGrpExclusionTbl(), PayorId), "       New data is added to table");

		logger.info("*** Step 4 Action: - Click Reset button and click ok button");
		assertTrue(isElementPresent(testCode.resetBtn(), 5),"        Reset button should show.");
		testCode.clickResetBtn();
		String confirmation = closeAlertAndGetItsText(true);
		
		logger.info("*** Step 4 Expected Results: - The Confirmation message is displayed");
		assertNotNull(confirmation,"        The Confirmation message is displayed");

		logger.info("*** Step 4 Expected Results: - New payor group Exclusion is not added into the table and databased");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input should show.");
		testCode.checkInputTestCodeId(singleID);
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5),"        Exclusion Tab should show.");
		testCode.clickExclusionTab();

		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5),"        Payor Group Exclusion Table should show.");
		assertFalse(getColumnValue(testCode.pyrGrpExclusionTbl(), PayorId), "       New data is not added to table");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Exclusions - Add Payor with duplicate payorID")
	public void testXPR_288() throws Exception {
		logger.info("===== Testing - testXPR - 288 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"          Test Code Page Title should show.");
		assertTrue(testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> SingleCodeInfo = daoManagerXifinRpm.getSingleTest(testDb);
		String singleID = "ZO616WV";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input should show.");
		testCode.checkInputTestCodeId(singleID);

		logger.info("*** Step 2 Expected Results: - Sing test information page is displayed ");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label should show.");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Exclusion - payor Exclusions : Click on add icon");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5),"        Exclusion Tab should show.");
		assertTrue(isElementPresent(testCode.addPayorExclusion(), 5));
		List<String> listData = testCode.addPayorExclusions(2, this,true, testDb);
		String PayorId = listData.get(0);

		logger.info("*** Step 3 Expected Results: - New data added to table");
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5));
		assertTrue(getColumnValue(testCode.pyrExclusionTbl(), PayorId), "       New data is added to table");

		logger.info("*** Step 4 Action: - Input payorId is duplicated with existing one");
		assertTrue(isElementPresent(testCode.addPayorExclusion(), 5));
		testCodeUtils.scrollIntoView(testCode.addPayorExclusion());
		testCode.clickAddPayorExclusionBtn();
		assertTrue(isElementPresent(testCode.addPayorId(), 5));
		testCode.inputPayorIDExclusion(PayorId);
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button should show.");
		testCode.clickOkPyrExclusion();

		logger.info("*** Step 4 Expected Results: - The Error message 'A duplicate record was entered' is displayed");
		assertTrue(testCodeUtils.getTextValue(testCode.PayorGroupExclusionTabDialogFormError(),5).trim().contains("A duplicate record was entered"),"        Error message should be 'A duplicate record was entered'");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}
	
	@Test(priority = 1, description = "Single Test Detail - Exclusions - Add Payor Group")
	public void testXPR_317() throws Exception {
		logger.info("===== Testing - testXPR_317 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"          Test Code Page Title should show.");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String profileId ="AUTOTESTXPR_317";
		testCode.createProfileTest(profileId, testDb, this);
		//Remove the Payor Group Exclusions for the test in DB
		daoManagerPlatform.deletePyrGrpExclFromTESTPYRGRPEXCLByTestAbbrev(profileId, testDb);
		
		logger.info("*** Step 2 Expected Results: - Profile Test Code page is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label should show.");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be Profile Test.");

		logger.info("*** Step 3 Actions: - Exclusion tab - Add new Payor Group Exclusion");		
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5), "        Payor Group Exclusion table should show.");
		String payorID = testCode.checkPyrGrpExclusion(this, true, testDb).get(0);

		logger.info("*** Step 3 Expected Results: - New Data is added to table");
		assertTrue(getColumnValue(testCode.pyrGrpExclusionTbl(), payorID), "       New Payor Group is added to table");

		logger.info("*** Step 4 Actions: -  Click Save and Clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 4 Expected Results: - New payor group is save to corresponding Profile ID");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		testCode.checkInputTestCodeId(profileId);
		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();
		assertTrue(getColumnValue(testCode.pyrGrpExclusionTbl(), payorID), "       New payor group is save to corresponding Profile ID");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Exclusions - Add Payor Group with GroupId is duplicate")
	public void testXPR_318() throws Exception {
		logger.info("===== Testing - testXPR - 318 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"          Test Code Page Title should show.");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String profileId = "AUTOTESTXPR_318";
		testCode.createProfileTest(profileId, testDb, this);
		daoManagerPlatform.deletePyrGrpExclFromTESTPYRGRPEXCLByTestAbbrev(profileId, testDb);

		logger.info("*** Step 2 Expected Results: - Profile information page is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label should show.");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be Profile Test.");

		logger.info("*** Step 3 Actions: - Exclusion tab - Add new Payor Group Exclusion and Click Save and Clear button");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();

		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5), "        Payor Group Exclusion table should show.");
		String payorID = testCode.checkPyrGrpExclusion(this, true, testDb).get(0);

		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 3 Expected Results: - New Data is added to table");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		testCode.checkInputTestCodeId(profileId);
		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();
		assertTrue(getColumnValue(testCode.pyrGrpExclusionTbl(), payorID), "       New Payor Group is added to table");

		logger.info("*** Step 4 Action: - Exclusions - Payor Group Exclusion : click on Add icon");
		testCodeUtils.scrollIntoView(testCode.addPayorGroup());
		testCode.clickAddPayorGroupBt();

		logger.info("*** Step 4 Expected Results: - Add Record popup is displayed");
		assertTrue(isElementPresent(testCode.PayorGroupDescription(), 5),"        Add Record pop up is displayed");

		logger.info("*** Step 5 Action : - Select Group ID is duplicate with existing one in the select box - click ok Button");
		testCode.selectPayorGroupDescriptionExclusionTab(payorID);
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button should show.");
		testCode.clickOkPyrGrpExclusion();

		logger.info("*** Step 5 Expected Results: - Error message 'A duplicate record was entered' is displayed");
		assertTrue(isElementPresent(testCode.PayorGroupExclusionTabDialogFormError(), 5),"        Error list should be show.");
		assertTrue( testCodeUtils.getTextValue(testCode.PayorGroupExclusionTabDialogFormError(),5).trim().contains("A duplicate record was entered"),"        Error message should be 'A duplicate record was entered'");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Exclusions - Cancel Add Payor Group")
	public void testXPR_319() throws Exception {
		logger.info("===== Testing - testXPR - 319 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"          Test Code Page Title should show.");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> profileInfo = daoManagerXifinRpm.getProfileTest(testDb);
		String profileId = "00334168";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input should show.");
		testCode.checkInputTestCodeId(profileId);

		logger.info("*** Step 2 Expected Results: - Profile information page is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label should show.");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be Profile.");

		logger.info("*** Step 3 Action: - Exclusions - Payor Group Exclusion - click on add icon - Select Group and click cancel button");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5),"        Exclusion Tab should show.");
		assertTrue(isElementPresent(testCode.addPayorGroup(), 5),"        Add Payor Group button should show.");
		List<String> listData = testCode.checkPyrGrpExclusion(this, false, testDb);
		String pyrID = listData.get(0);

		logger.info("*** Step 3 Expected Results: - Add Record popup is closed and new group is not added  into the table");
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5),"        Payor Group Exclusion table should show.");
		assertFalse(getColumnValue(testCode.pyrGrpExclusionTbl(), pyrID), "       New Group is not added to table");

		testCode.clickResetBtn();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}
	
	@Test(priority = 1, description = "Single Test Detail - Exclusions - Delete Payor Group")
	public void testXPR_320() throws Exception {
		logger.info("===== Testing - testXPR - 320 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"          Test Code Page Title should show.");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String profileId = "TESTXPR_320";
		testCode.createProfileTest(profileId, testDb, this);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the profile Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label should show.");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be Profile Test.");

		logger.info("*** Step 3 Actions: - Exclusion tab - Add new Payor Group Exclusion and Click Save and Clear button");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5), "        Payor Group Exclusion table should show.");
		String payorID = testCode.checkPyrGrpExclusion(this, true, testDb).get(0);
		assertTrue(getColumnValue(testCode.pyrGrpExclusionTbl(), payorID), "       New Payor Group is added to table");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 3 Expected Results: - New Data is added to table");
		logger.info("*** Step 3 Expected Results: - New Data is added to table");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		testCode.checkInputTestCodeId(profileId);
		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();
		assertTrue(getColumnValue(testCode.pyrGrpExclusionTbl(), payorID), "       New Payor Group is added to table");

		logger.info("*** Step 4 Action: - Exclusion tab - Select any row on Payor Group and click Edit button");
		String pyrID = testCode.selectFirstRowPyrGrpExclusionTbl(this, profileId,testDb);
		selectColumnValue(testCode.pyrGrpExclusionTbl(), pyrID);

		logger.info("*** Step 4 Expected Results: - Edit new record popup is displayed");
		assertTrue(isElementPresent(	testCode.addPayorGroupExclusionDialog(), 5),"        Add new Payor Group is displayed");

		logger.info("*** Step 5 Action: - Check on Delete checkbox and click Ok button");
		assertTrue(isElementPresent(	testCode.deleteCheckbok(), 5),"        Delete checkbox should show.");
		selectCheckBox(testCode.deleteCheckbok());
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button should show.");
		testCode.clickOkPyrGrpExclusion();

		logger.info("*** Step 5 Expected Results: - The selected row is marked as delete row");
		assertTrue(testCodeUtils.hasClass(testCode.getRowOfPyrGrpExclusion(1), "rowMarkedForDelete"),"        Payor must be marked to remove from table");

		logger.info("*** Step 6 Action: - Click save and Clear button");
		testCodeUtils.scrollIntoView(testCode.saveAndClearBtn());
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 6 Expected Results: - The selected row is removed out of the list ");
		assertTrue(isElementPresent(	testCode.testIdInput(), 5),"        Test ID input should show.");
		testCode.checkInputTestCodeId(profileId);
		assertTrue(isElementPresent(	testCode.exclusionsTab(), 5),"        Exclusion tab should show.");
		testCode.clickExclusionTab();
		assertTrue(isElementPresent(	testCode.pyrGrpExclusionTbl(), 5),"        Payor Group Exclusion table should show.");
		assertFalse(getColumnValue(testCode.pyrGrpExclusionTbl(), pyrID), "       The selected row is removed out of the list ");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Exclusions - Reset the Delete Payor Group")
	public void testXPR_321() throws Exception {
		logger.info("===== Testing - testXPR - 321 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"          Test Code Page Title should show.");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String testID = "AUTOTESTXPR_321";
		testCode.createProfileTest(testID, testDb, this);
		//Remove the Payor Group Exclusions for the test in DB
		daoManagerPlatform.deletePyrGrpExclFromTESTPYRGRPEXCLByTestAbbrev(testID, testDb);

		logger.info("*** Step 2 Expected Results: - Profile information page is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label should show.");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be Profile.");

		logger.info("*** Step 3 Action: - Prepare Data for Exclusion and Click Save and Clear button");
		List<String> listPyrGrp = testCode.checkPyrGrpExclusion(this, true, testDb);
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 3 Expected Results: - New Data should be added to database");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input should show.");
		testCode.checkInputTestCodeId(testID);
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5),"        Exclusion Tab should show.");
		testCode.clickExclusionTab();
		assertTrue(getColumnValue(testCode.pyrGrpExclusionTbl(),listPyrGrp.get(0)),"        New Data should be added to database");

		logger.info("*** Step 4 Action: - Exclusion tab - Select any row on Payor Group and click Edit button");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5),"        Exclusion Tab should show.");
		testCode.clickExclusionTab();
		testCode.clickFirstCellOnPyrGrp();
		List<String> listPyrID = testCode.getAllFieldsInFirstRow(testCode.pyrGrpExclusionTbl());
		String payorGroupId = listPyrID.get(1);
		selectColumnValue(testCode.pyrGrpExclusionTbl(), payorGroupId);

		logger.info("*** Step 4 Expected Results: - Add new record popup is displayed");
		assertTrue(isElementPresent(	testCode.addPayorGroupExclusionDialog(), 5),"        Add new Payor Group is displayed");

		logger.info("*** Step 5 Action: - Check on Delete checkbox and click Ok button");
		assertTrue(isElementPresent(	testCode.deleteCheckbok(), 5),"        Delete checkbox should show.");
		selectCheckBox(testCode.deleteCheckbok());
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button should show.");
		testCode.clickOkPyrGrpExclusion();

		logger.info("*** Step 5 Expected Results: - The selected row is marked as delete entry ");
		testCode.checkDeleteRowInPyrGrpExclusionTbl();

		logger.info("*** Step 6 Action: - Click Reset button");
		testCodeUtils.scrollIntoView(testCode.resetBtn());
		testCode.clickResetBtn();
		String confirmation =  closeAlertAndGetItsText(true);

		logger.info("*** Step 6 Expected Results: - The confirmation message is displayed");
		assertNotNull(confirmation,"        The confirmation message is displayed");

		logger.info("*** Step 6 Expected Results: - The selected row is not remove out of list ");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input should show.");
		testCode.checkInputTestCodeId(testID);
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5),"        Exclusion Tab should show.");
		testCode.clickExclusionTab();
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5),"        Payor Group Exclusion table should show.");
		assertTrue(getColumnValue(testCode.pyrGrpExclusionTbl(), payorGroupId), "       The selected row is not remove out of list ");

		testCode.clickResetBtn();
	}
	
	@Test(priority = 1, description = "Single Test Detail - Exclusions - Add Payor with valid PayorID")
	public void testXPR_322() throws Exception {
		logger.info("===== Testing - testXPR - 322 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"          Test Code Page Title should show.");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Enter an existing Test ID");
		String testID = "AUTOTESTXPR_322";
		testCode.createProfileTest(testID, testDb, this);

		logger.info("*** Step 2 Expected Results: - Profile information page is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label should show.");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be Profile.");

		logger.info("*** Step 3 Actions: - Exclusion tab - Add new Payor Exclusion and Click Save and Clear button");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5), "        Payor Exclusion table should show.");
		String payorID = testCode.addPayorExclusions(2, this, true, testDb).get(0);

		logger.info("*** Step 3 Expected Results: - New Data is added to table");
		assertTrue(getColumnValue(testCode.pyrExclusionTbl(), payorID), "       New Payor is added to table");

		logger.info("*** Step 4 Actions: - Click Save and Clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Save and Clear button should show.");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 4 Expected Results: - The new PayorId is save into the corresponding Profile ID");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        Test ID input should show.");
		testCode.checkInputTestCodeId(testID);
		//waitUntilElementIsNotVisible(testCode.testIdInput(), 5);
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5), "        Exclusion tab should show.");
		testCode.clickExclusionTab();
		assertTrue(getColumnValue(testCode.pyrExclusionTbl(), payorID), "       The new PayorId is save into the corresponding Profile ID");
		testCode.clickResetBtn();
		
		logger.info("*** Step 5 Actions: - Clear test data");
		daoManagerPlatform.deletePyrExclFromTESTPYREXCLByTestAbbrev(testID, testDb);
	}
	
	@Test(priority = 1, description = "Single Test Detail - Exclusions - Add Payor with invalid PayorID")
	public void testXPR_323() throws Exception {
		logger.info("===== Testing - testXPR - 323 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"          Test Code Page Title should show.");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String profileId = "003634JD";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input should show.");
		testCode.checkInputTestCodeId(profileId);

		logger.info("*** Step 2 Expected Results: - Page information page is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label should show.");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be Profile.");

		logger.info("*** Step 3 Action: - Exclusions - Payor Exclusion - Click on Add icon");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5),"        Exclusion Tab should show.");
		testCode.clickExclusionTab();
		testCodeUtils.scrollIntoView(testCode.addPayorExclusion());
		testCode.clickAddPayorExclusionBtn();

		logger.info("*** Step 3 Expected Results: - Add New Record popup is displayed");
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5),"        Add New Record popup should be display");

		logger.info("*** Step 4 Action: - Input invalid payorId - click Ok button");
		assertTrue(isElementPresent(testCode.addPayorId(), 5),"        Payor ID input should show.");
		testCode.inputPayorIDExclusion("abccbacbabcbabcbabcbab");
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button should show.");
		testCode.clickOkPyrExclusion();

		logger.info("*** Step 4 Expected Results: - Error message 'Payor ID xxx was not found.' id displayed");
		assertTrue( testCodeUtils.getTextValue(testCode.errorMessage(),5).trim().contains("Payor ID ABCCBACBABCBABCBABCBAB was not found."),"        Error message 'Payor ID ABCCBACBABCBABCBABCBAB was not found.' should be displayed");

		logger.info("*** Step 5 Action: - Leave payorId as blank");
		assertTrue(isElementPresent(testCode.addPayorId(), 5),"        Payor ID input should show.");
		testCode.clearData(testCode.addPayorId());
		testCode.inputPayorIDExclusion("");
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button should show.");
		testCode.clickOkPyrExclusion();

		logger.info("*** Step 5 Expected Results: - Error message 'Payor ID: Field is required' id displayed");
		assertTrue(isElementPresent(testCode.errorMessage(), 5));
		assertTrue( testCodeUtils.getTextValue(testCode.errorMessage(),5).trim().contains("Payor ID: Field is required"),"        Error message 'Payor ID: Field is required' should be displayed");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}
	
	@Test(priority = 1, description = "Single Test Detail - Exclusions - Reset Add Payor")
	public void testXPR_324() throws Exception {
		logger.info("===== Testing - testXPR - 324 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"          Test Code Page Title should show.");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String profileId = "003672OP";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input should show.");
		testCode.checkInputTestCodeId(profileId);

		logger.info("*** Step 2 Expected Results: - Profile information page is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label should show.");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be Profile.");

		logger.info("*** Step 3 Action: - Exclusions - Payor Exclusion - Add new Payor and click ok button");
		List<String> listPayorID = testCode.addPayorExclusions(1, this, true, testDb);
		String pyrId = listPayorID.get(0);

		logger.info("*** Step 3 Expected Results: - New Payor Id is added to table");
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5));
		assertTrue(getColumnValue(testCode.pyrExclusionTbl(), pyrId), "       New data is added to table");

		logger.info("*** Step 4 Action: - Click Reset button and click ok button");
		assertTrue(isElementPresent(testCode.resetBtn(), 5),"        Reset button should show.");
		testCode.clickResetBtn();
		String confirmation = closeAlertAndGetItsText(true);

		logger.info("*** Step 4 Expected Results: - The confirmation message is displayed and The new payorId is not added into the table list");
		assertNotNull(confirmation,"        The confirmation message is displayed");

		logger.info("*** Step 4 Expected Results: - The new payorId is not added into the table list");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input should show.");
		testCode.inputTestCodeId(profileId);
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5),"        Exclusion Tab should show.");
		testCode.clickExclusionTab();
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5),"        Payor Exclusion table should show.");
		assertFalse(getColumnValue(testCode.pyrExclusionTbl(), pyrId), "       The new payorId is not added into the table list");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Exclusions - Delete Payor Id")
	public void testXPR_325() throws Exception {
		logger.info("===== Testing - testXPR - 325 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"          Test Code Page Title should show.");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String profileId = "TESTXPR_325asa";
		testCode.createProfileTest(profileId, testDb, this);

		logger.info("*** Step 2 Expected Results: - Profile information page is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label should show.");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be Profile Test.");

		logger.info("*** Step 3 Action: - Exclusions - Payor Id - Select row and click on edit button");
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5),"        Test label should show.");
		testCode.clickExclusionTab();
		String payorID = testCode.checkPayorExclusions(profileId,testDb);
		selectColumnValue(testCode.pyrExclusionTbl(), payorID);

		logger.info("*** Step 3 Expected Results: - Edit new record popup is displayed");
		assertTrue(isElementPresent(	testCode.editPayorExclusionDialog(), 5),"        Add new Payor is displayed");

		logger.info("*** Step 4 Action: - Check on Delete checkbox and click Ok button");
		assertTrue(isElementPresent(	testCode.deleteCheckbok(), 5),"        Delete checkbox should show.");
		selectCheckBox(testCode.deleteCheckbok());
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Ok button should show.");
		testCode.clickOkPyrExclusion();

		logger.info("*** Step 4 Expected Results: - The selected row is market as delete row");
		int indexOfRowDelete = testCode.getRowIndex(testCode.pyrExclusionTbl(), payorID)-1;
		assertTrue(testCodeUtils.hasClass(testCode.getRowOfPyrExclusion(indexOfRowDelete), "rowMarkedForDelete"),"        Payor must be marked to remove from table");

		logger.info("*** Step 5 Action: - Click save and Clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and Clear button should show.");
		testCodeUtils.scrollIntoView(testCode.saveAndClearBtn());
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 5 Expected Results: - The selected row is removed out of the payor list");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input should show.");
		testCode.inputTestCodeId(profileId);
		assertTrue(isElementPresent(testCode.exclusionsTab(), 5),"        Exclusion Tab should show.");
		testCode.clickExclusionTab();
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5));
		assertFalse(getColumnValue(testCode.pyrExclusionTbl(), payorID), "       The selected row is removed out of the payor list");

		testCode.clickResetBtn();
	}
	
	@Test(priority = 1, description = "Single Test Detail - Exclusions - Verify Filter function in Payor Group Exclusions table")
	public void testXPR_326() throws Exception {
		logger.info("===== Testing - testXPR - 326 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"          Test Code Page Title should show.");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String profileId = "004382MC";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input should show.");
		testCode.checkInputTestCodeId(profileId);

		logger.info("*** Step 2 Expected Results: - Profile Information page is displayed");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label should show.");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be profile Test.");

		logger.info("*** Step 3 Action: - Exclusions - Payor Group - Add multi data");
		List<String> listData = testCode.addPayorGroupExclusions(2, this, true, testDb);
		String pyrGrpID = listData.get(1);

		logger.info("*** Step 3 Expected Results: - Data will show on table");
		assertTrue(isElementPresent(testCode.pyrGrpExclusionTbl(), 5),"        Payor Group Exclusion table should show.");
		assertTrue(getColumnValue(testCode.pyrGrpExclusionTbl(), pyrGrpID), "       New data is added to table");

		logger.info("*** Step 4 Action: - Input data to Payor Group Filter");
		assertTrue(isElementPresent(testCode.grpIdExclusionFilter(), 5),"        Group Id Exclusion filter should show.");
		testCode.setGrpIdFilter(pyrGrpID);

		logger.info("*** Step 4 Expected Results: - The list GroupID filter based on value inputted");
		assertTrue(getColumnValue(testCode.pyrGrpExclusionTbl(), pyrGrpID), "       The list GroupID filter based on value inputted");

		testCode.clickResetBtn();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}
	
	@Test(priority = 1, description = "Single Test Detail - Exclusions - Verify Filter function in Payor Exclusions table")
	public void testXPR_327() throws Exception {
		logger.info("===== Testing - testXPR - 327 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"          Test Code Page Title should show.");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String profileId = "004650WF";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input should show.");
		testCode.checkInputTestCodeId(profileId);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Profile Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label should show.");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Profile)"), "        Test Label should be profile Test.");

		logger.info("*** Step 3 Action: - Exclusions - Payor Exclusions - Add multi data");
		List<String>listData = testCode.addPayorExclusions(5, this, true, testDb);
		String pyrID = listData.get(1);

		logger.info("*** Step 3 Expected Results: - Data will show on table");
		assertTrue(isElementPresent(testCode.pyrExclusionTbl(), 5), "       Payor Exclusion table should show.");
		assertTrue(getColumnValue(testCode.pyrExclusionTbl(), pyrID), "       New data is added to table");

		logger.info("*** Step 4 Action: - Input data to Payor ID Filter");
		assertTrue(isElementPresent(testCode.grpIdExclusionFilter(), 5),"        Group ID exclusion filter should show.");
		testCode.setPyrIdFilter(pyrID);

		logger.info("*** Step 4 Expected Results: - The PayorId is filter based on value inputted");
		assertTrue(getColumnValue(testCode.pyrExclusionTbl(), pyrID),"        The PayorId is filter based on value inputted");

		testCode.clickResetBtn();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}

	// Component Tab
	@Test(priority=1, description="Single Test Detail - Component - Verify filter function profile component")
	public void testXPR_204() throws Exception{
		logger.info("===== Testing - testXPR_204 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"       The Test Code page title displays");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
//		List<String> profileInfo = daoManagerXifinRpm.getProfileTest(testDb);
		String profileInfo = "00479808";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        test Id inout field should show");
		testCode.checkInputTestCodeId(profileInfo);

		logger.info("*** Step 2 Expected Results: - Profile test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Profile)"),"        Test Label should be Profile.");

		logger.info("*** Step 3 Action: - add valid data component");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5),"       Effdate dropdown should show");
		String curEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		assertTrue(isElementPresent(testCode.componentTable(), 5),"      Component tab should show");
		List<List<String>> componentInfo =  testCode.addComponent(1, this, curEffDate, "", true, "s", true, testDb);

		logger.info("*** Step 3 Expected Results: - New component is displayed in table");
		assertTrue(getColumnValue(testCode.componentTable(), componentInfo.get(0).get(0)));

		logger.info("*** Step 4 Action: - input valid in test Id");
		assertTrue(isElementPresent(testCode.testIdFilterComponent(), 5),"       testId filter input field should show");
		testCode.setTestIdFilterComponent(componentInfo.get(0).get(0));

		logger.info("*** Step 4 Expected Results: - System will perform testID filter correctly");
		assertTrue(getColumnValue(testCode.componentTable(), componentInfo.get(0).get(0)));

		logger.info("*** Step 5 Action: - input valid in Name");
		testCode.clearData(testCode.testIdFilterComponent());
		assertTrue(isElementPresent(testCode.nameFilterComponent(), 5),"      testName input field should show");
		testCode.setNameFilterComponent(componentInfo.get(0).get(2));

		logger.info("*** Step 5 Expected Results: - System will perform Name filter correctly");
		assertTrue(getColumnValue(testCode.componentTable(), componentInfo.get(0).get(2)));

		testCode.clickResetBtn();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}
	
	@Test(priority=1, description="Single Test Detail - Component - add new component with valid data")
	public void testXPR_291() throws Exception{
		logger.info("===== Testing - testXPR_291 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"       The Test Code page title should display");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		String testID = "TESTXPR_291";
		testCode.createProfileTest(testID, testDb, this);

		logger.info("*** Step 2 Expected Results: - Profile test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Profile)"),"        Test Label should be Profile.");

		logger.info("*** Step 3 Action: - add valid data component");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5),"        effdate Dropdown list should show");
		String curEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		assertTrue(isElementPresent(testCode.componentTable(), 5),"        component tab should show");
		List<List<String>> componentInfo =  testCode.addComponent(1, this, curEffDate, "", true, "s", true, testDb);

		logger.info("*** Step 3 Expected Results: - New component is displayed in table");
		assertTrue(getColumnValue(testCode.componentTable(), componentInfo.get(0).get(0)));

		logger.info("*** Step 4 Action: - Click save and clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        save and clear button should show");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 4 Expected Results: - Go back to load test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page should be loaded.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 5 Action: - Load again test code");
		testCode.checkInputTestCodeId(testID);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 5 Expected Result : - New component is  save into database");
		assertTrue(isElementPresent(testCode.componentTab(), 5),"        component tab should show");
		testCode.clickComponentTab();
		assertTrue(getColumnValue(testCode.componentTable(), componentInfo.get(0).get(0)),"        New component should  be save into database");
				
		logger.info("*** Step 6 Action: - Select any testId to delete");
		selectColumnValue(testCode.componentTable(), componentInfo.get(0).get(0));

		logger.info("*** Step 6 Expected Result : - Edit component dialog is displayed");
		assertTrue(isElementPresent(testCode.editComponentDialog(), 5),"        edit component dialog should show");

		logger.info("*** Step 7 Action: -Check on delete checkbox - click ok button and  Click save and clear button");
		assertTrue(isElementPresent(testCode.delCheckComponent(), 5),"        del checkbox component should show");
		selectCheckBox(testCode.delCheckComponent());
		assertTrue(isElementPresent(testCode.okBtn(), 5),"          ok component button should show");
		testCode.clickOkCompBtn();
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        save and clear button should show");
		testCode.clickSaveAndClearBtn();
	}

	@Test(priority=1, description="Single Test Detail - Component - Add Component with testId is duplicate")
	public void testXPR_293() throws Exception{
		logger.info("===== Testing - testXPR_293 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should display");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
//		List<String> profileInfo = daoManagerXifinRpm.getProfileTest(testDb);
		String profileInfo = "005177GS";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(profileInfo);

		logger.info("*** Step 2 Expected Results: - Profile test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Profile)"),"        Test Label should be Profile.");

		logger.info("*** Step 3 Action: - Click Add component button ");
		assertTrue(isElementPresent(testCode.componentTab(), 5),"        component tab should show");
		testCode.clickComponentTab();
		assertTrue(isElementPresent(testCode.componentTable(), 5),"        component table should show");
		assertTrue(isElementPresent(testCode.testIDInProcedureTable(), 5));
		String curTestId = testCode.testIDInProcedureTable().getText();
		assertTrue(isElementPresent(testCode.addComponent(), 5),"        add button component should show");
		testCodeUtils.scrollIntoView(testCode.addComponent());
		testCode.clickAddComponent();

		logger.info("*** Step 3 Expected Results: - Add component dialog is displayed");
		assertTrue(isElementPresent(testCode.addComponentDialog(), 5),"       component dialog should show");

		logger.info("*** Step 4 Action: - add valid data with Test Id is duplicate");
		assertTrue(isElementPresent(testCode.testID(), 5),"        testId component should show");
		testCode.setComponentTestId(curTestId);
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        ok button should show");
		testCode.clickOkCompBtn();

		logger.info("*** Step 4 Expected Results: - The error message is displayed");
		assertTrue(isElementPresent(testCode.componentDialogFormError(), 5),"        Error message should display");
		assertTrue(testCode.componentDialogFormError().getText().contains("A duplicate record was entered"),"        The error message should be displayed : A duplicate record was entered");

		testCode.cancelBtn().click();
		testCode.resetBtn().click();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Component - Add Component with require are empty")
	public void testXPR_294() throws Exception{
		logger.info("===== Testing - testXPR_294 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should display");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
//		List<String> profileInfo = daoManagerXifinRpm.getProfileTest(testDb);
		String profileInfo = "00537614";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"       Testid input field should show");
		testCode.checkInputTestCodeId(profileInfo);

		logger.info("*** Step 2 Expected Results: - Profile test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Profile)"),"        Test Label should be Profile.");

		logger.info("*** Step 3 Action: - Click Add component button ");
		assertTrue(isElementPresent(testCode.componentTab(), 5),"       component tab should show");
		testCode.clickComponentTab();
		assertTrue(isElementPresent(testCode.addComponent(), 5),"        add component button  should show ");
		testCodeUtils.scrollIntoView(testCode.addComponent());
		testCode.clickAddComponent();

		logger.info("*** Step 3 Expected Results: - Add component dialog is displayed");
		assertTrue(isElementPresent(testCode.componentDialogFormError(), 5),"       Component FormError should show");

		logger.info("*** Step 4 Action: - add valid data with Test Id is empty");
		assertTrue(isElementPresent(testCode.testID(), 5),"       testId component input field should show");
		testCode.setComponentTestId("");
		assertTrue(isElementPresent(testCode.okBtn(), 5),"         Ok component button should show");
		testCode.clickOkCompBtn();

		logger.info("*** Step 4 Expected Results: - The error message is displayed");
		assertTrue(isElementPresent(testCode.componentDialogFormError(), 5),"       Component FormError should show");
		assertTrue(testCode.componentDialogFormError().getText().contains("Test ID: Field is required"),"        The error message should be displayed : Test ID: Field is required");

		logger.info("*** Step 5 Action: - add valid data with units is empty");
		assertTrue(isElementPresent(testCode.testID(), 5),"        testId component input field should show");
		testCode.setComponentTestId(profileInfo);
		assertTrue(isElementPresent(testCode.unitsComponent(), 5),"        units component should show");
		testCode.clearData(testCode.unitsComponent());
		testCode.setUnitsComponent("");
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        ok component button should show");
		testCode.clickOkCompBtn();

		logger.info("*** Step 5 Expected Results: - The error message is displayed");
		assertTrue(isElementPresent(testCode.componentDialogFormError(), 5),"        component dialog form error should show");
		assertTrue(testCode.componentDialogFormError().getText().contains("Units: Field is required"),"        The error message should be displayed : Units: Field is required");

		testCode.cancelBtn().click();
		testCode.resetBtn().click();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}

	@Test(priority=1, description="Single Test Detail - Component - Reset add new component")
	public void testXPR_295() throws Exception{
		logger.info("===== Testing - testXPR_295 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"       The Test Code page title should display");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
//		List<String> profileInfo = daoManagerXifinRpm.getProfileTest(testDb);
		String profileInfo = "00591965";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(profileInfo);

		logger.info("*** Step 2 Expected Results: - Profile test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Profile)"),"        Test Label should be Profile.");

		logger.info("*** Step 3 Action: - add valid data with Test Id is single test that have eff date is greater profile eff date");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5),"        EffDate dropdown list should show");
		String curEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		assertTrue(isElementPresent(testCode.componentTable(), 5),"        component table should show");
		List<List<String>> componentInfo =  testCode.addComponent(1, this, curEffDate, "", true, "s", true, testDb);

		logger.info("*** Step 3 Expected Results: - New component is displayed in table");
		assertTrue(getColumnValue(testCode.componentTable(), componentInfo.get(0).get(0)));

		logger.info("*** Step 4 Action: - Click save and clear button");
		assertTrue(isElementPresent(testCode.resetBtn(), 5),"        reset button should show");
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);

		logger.info("*** Step 4 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 5 Action: - Load again test code");
		testCode.checkInputTestCodeId(profileInfo);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 5 Expected Result : - New component is not save into database");
		assertTrue(isElementPresent(testCode.componentTab(), 5),"        component tab should show");
		testCode.clickComponentTab();
		assertFalse(getColumnValue(testCode.componentTable(), componentInfo.get(0).get(0)),"        New component should not be save into database");

		testCode.clickResetBtn();
	}

	@Test(priority=1, description="Single Test Detail - Component - Add Component with invalid testId")
	public void testXPR_296() throws Exception{
		logger.info("===== Testing - testXPR_296 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		randomCharacter  = new RandomCharacter(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should display");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
//		List<String> profileInfo = daoManagerXifinRpm.getProfileTest(testDb);
		String profileInfo = "006453AE";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test Id input field should show");
		testCode.checkInputTestCodeId(profileInfo);

		logger.info("*** Step 2 Expected Results: - Profile test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Profile)"),"        Test Label should be Profile.");

		logger.info("*** Step 3 Action: - Click Add component button ");
		assertTrue(isElementPresent(testCode.addComponent(), 5),"        add component button should show");
		testCodeUtils.scrollIntoView(testCode.addComponent());
		testCode.clickAddComponent();

		logger.info("*** Step 3 Expected Results: - Add component dialog is displayed");
		assertTrue(isElementPresent(testCode.componentDialogFormError(), 5),"        component dialog form error should show");

		logger.info("*** Step 4 Action: - add valid data with Invalid test ID");
		assertTrue(isElementPresent(testCode.testID(), 5),"        testId input component field should show");
		String invalidTestId = randomCharacter.getRandomAlphaNumericString(10);
		testCode.setComponentTestId(invalidTestId);
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        ok component button should show");
		testCode.clickOkCompBtn();

		logger.info("*** Step 4 Expected Results: - The error message is displayed");
		assertTrue(isElementPresent(testCode.componentDialogFormError(), 5),"        component dialog error should show");
		assertTrue(testCode.componentDialogFormError().getText().contains("Test Code "+invalidTestId+" was not found."),"        The error message should be displayed : Test Code "+invalidTestId+" was not found.");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
	}

	@Test(priority=1, description="Single Test Detail - Component - Delete Component")
	public void testXPR_299() throws Exception{
		logger.info("===== Testing - testXPR_299 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should display");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		String profileID = "TESTXPR_299";
		testCode.createProfileTest(profileID, testDb, this);

		logger.info("*** Step 2 Expected Results: - Profile test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Profile)"),"        Test Label should be Profile.");

		logger.info("*** Step 3 Action: - add valid data with Test Id is single test that have eff date is greater profile eff date");
		scrollToElement(0, -250);
		assertTrue(isElementPresent(testCode.selectEffDate(), 5),"        Eff Date Dropdown list should show");
		String curEffDate = testCodeUtils.getTextSelectedInDropdown(testCode.selectEffDate());
		assertTrue(isElementPresent(testCode.componentTable(), 5),"        Component table should show");
		List<List<String>> componentInfo =  testCode.addComponent(1, this, curEffDate, "", true, "s", true, testDb);

		logger.info("*** Step 3 Expected Results: - New component is displayed in table");
		assertTrue(getColumnValue(testCode.componentTable(), componentInfo.get(0).get(0)));

		logger.info("*** Step 4 Action: - Click save and clear button");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        save and clear button should show");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 4 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 5 Action: - Load again test code");
		testCode.checkInputTestCodeId(profileID);

		logger.info("*** Step 5 Expected Result : - Test Code loaded page must be present");
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 5 Expected Result : - New component is not save into database");
		assertTrue(isElementPresent(testCode.componentTab(), 5),"        component tab should show");
		testCode.clickComponentTab();
		assertTrue(getColumnValue(testCode.componentTable(), componentInfo.get(0).get(0)),"        New component should be save into database");

		logger.info("*** Step 6 Action: - Select any testId to delete");
		selectColumnValue(testCode.componentTable(), componentInfo.get(0).get(0));

		logger.info("*** Step 6 Expected Result : - Edit component dialog is displayed");
		assertTrue(isElementPresent(testCode.editComponentDialog(), 5),"        edit component dialog should show");

		logger.info("*** Step 7 Action: -Check on delete checkbox - click ok button and  Click save and clear button");
		assertTrue(isElementPresent(testCode.delCheckComponent(), 5),"        del checkbox component should show");
		selectCheckBox(testCode.delCheckComponent());
		assertTrue(isElementPresent(testCode.okBtn(), 5),"          ok component button should show");
		testCode.clickOkCompBtn();
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        save and clear button should show");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 7 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 8 Action: - Load again test code");
		testCode.checkInputTestCodeId(profileID);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");

		logger.info("*** Step 8 Expected Result : - The select component is remove out of profile component list ");
		assertTrue(isElementPresent(testCode.componentTab(), 5),"        component tab should show");
		testCode.clickComponentTab();
		assertFalse(getColumnValue(testCode.componentTable(), componentInfo.get(0).get(0)),"        The select component is remove out of profile component list");

		testCode.clickResetBtn();
	}

	@Test(priority=1, description="Single Test Detail - Component - Reset delete component")
	public void testXPR_300() throws Exception{
		logger.info("===== Testing - testXPR_300 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"       The Test Code page title should display");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
//		List<String> profileInfo = daoManagerXifinRpm.getProfileTest(testDb);
		String profileInfo = "00684945";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"       testId input field should show");
		testCode.checkInputTestCodeId(profileInfo);

		logger.info("*** Step 2 Expected Results: - Profile test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCodeUtils.getTextValue(testCode.labelTest(),5).equalsIgnoreCase("(Profile)"),"        Test Label should be Profile.");

		logger.info("*** Step 3 Action: - get Current test Id in Component Table");
		assertTrue(isElementPresent(testCode.componentTab(), 5),"        component tab should show");
		testCode.clickComponentTab();
		assertTrue(isElementPresent(testCode.componentTable(), 5),"        component table should show");
		assertTrue(isElementPresent(testCode.testIDInProcedureTable(), 5));
		String curTestId = testCode.testIDInProcedureTable().getText();

		logger.info("*** Step 3 Action: - Select any test Id in component Table");
		selectColumnValue(testCode.componentTable(), curTestId);

		logger.info("*** Step 3 Expected Results: - Edit component dialog is displayed");
		assertTrue(isElementPresent(testCode.editComponentDialog(), 5),"       editComponentDialog should show");

		logger.info("*** Step 4 Action: - Check on delete checkbox ");
		assertTrue(isElementPresent(testCode.delCheckComponent(), 5),"       delCheckComponent should show");
		selectCheckBox(testCode.delCheckComponent());
		assertTrue(isElementPresent(testCode.okBtn(), 5),"       okBtn should show");
		testCode.clickOkCompBtn();

		logger.info("*** Step 4 Action: - Click Reset button");
		assertTrue(isElementPresent(testCode.resetBtn(), 5),"        resetBtn should show");
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);

		logger.info("*** Step 4 Expected Results: - Go out loading test code page");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 5 Action: - Load again test code");
		testCode.checkInputTestCodeId(profileInfo);
		assertTrue(isElementPresent(testCode.testIDLabel(), 5), "       Test Code loaded page must be present");
		
		logger.info("*** Step 5 Expected Result : - New component is not save into database");
		assertTrue(isElementPresent(testCode.componentTab(), 5),"        component Tab should show");
		testCode.clickComponentTab();
		assertTrue(getColumnValue(testCode.componentTable(), curTestId),"        New component should not be save into database");

		testCode.clickResetBtn();
	}

	// Suppress Cross-Test Consolidations tab
	@Test(priority = 1, description = "Single Test Detail - Suppress Cross - Add Payor with valid data")
	public void testXPR_126() throws Exception {
		logger.info("===== Testing - testXPR_126 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should show");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> singleTestCodeInfo = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTestCodeInfo = "ZN555HT";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test Label should show");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Add valid suppress");
		assertTrue(isElementPresent(testCode.addSCTButton(), 5),"        Add suppress must be available");
		List<String> suppress = testCode.addSuppressCT("", "", true, testCode, this, testDb);

		logger.info("*** Step 3 Expected Results: - Verify that payor added to table");
		assertTrue(isElementPresent(testCode.sCTTable(), 5), "        Suppress table must be presented");
		assertTrue(getColumnValue(testCode.sCTTable(), suppress.get(0)), "        Payor ID must be added to table.");
		assertTrue(getColumnValue(testCode.sCTTable(), suppress.get(1)), "        Effective date must be added to table.");

		testCode.clickResetBtn();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Suppress Cross - Edit Payor with valid data")
	public void testXPR_127() throws Exception {
		logger.info("===== Testing - testXPR_127 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp  = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should show");
		assertTrue(testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String testID = "TESTXPR_127";
		testCode.createSingleTest(testID, testDb, false, false, this);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test Label should show");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Edit payor with valid info");
		String effDate = timeStamp.getCurrentDate("MM/dd/yyyy");
		String expDate = testCodeUtils.getNextDay(effDate);
		assertTrue(isElementPresent(testCode.sCTTable(), 5),"        Suppress table must be available");
		List<String> payorEdit = testCode.editSuppressCT(1, effDate, expDate, true, this);

		logger.info("*** Step 3 Expected Results: - Verify that all info edited");
		assertTrue(getColumnValue(testCode.sCTTable(), payorEdit.get(0)), "        Payor ID must be edited in table.");
		assertTrue(getColumnValue(testCode.sCTTable(), payorEdit.get(1)), "        Effective date must be edited in table.");
		assertTrue(getColumnValue(testCode.sCTTable(), payorEdit.get(2)), "        Exp date must be edited in table.");

		logger.info("*** Step 4 Action: - Save all");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5),"        Save and clear must be available");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 4 Expected Result : - Landing page load test code must be present");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "       Landing page load test code must be present");

		logger.info("*** Step 5 Actions: - Load again single test");		
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        TestId input field should show");
		testCode.checkInputTestCodeId(testID);

		logger.info("*** Step 5 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test Label should show");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 6 Actions: Switch to Suppress tab");
		assertTrue(isElementPresent(testCode.sCTTab(), 5),"        Suppress tab must be available");
		testCode.clickSCTTab();
		assertTrue(isElementPresent(testCode.addSCTButton(), 5),"        Add Suppress must be available");
		testCodeUtils.scrollIntoView(testCode.addSCTButton());

		logger.info("*** Step 6 Expected Results: - Verify that payor edited");
		assertTrue(getColumnValue(testCode.sCTTable(), payorEdit.get(0)), "        Payor ID must be edited in table.");
		assertTrue(getColumnValue(testCode.sCTTable(), payorEdit.get(1)), "        Effective date must be edited in table.");
		assertTrue(getColumnValue(testCode.sCTTable(), payorEdit.get(2)), "        Exp date must be edited in table.");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Suppress Cross - Verify hide grid icon")
	public void testXPR_128() throws Exception {
		logger.info("===== Testing - testXPR_128 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should show");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> singleTestCodeInfo = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTestCodeInfo = "ZSV188";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test Label should show");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Hide Suppress table");
		assertTrue(isElementPresent(testCode.sCTTab(), 5), "        Suppress tab must be show");
		testCode.clickSCTTab();
		assertTrue(isElementPresent(testCode.hideGridSCTTable(), 5), "        Hide grid suppress must be show");
		testCode.clickHideGridSCTTable();

		logger.info("*** Step 3 Expected Results: - Verify Suppress table hide");
		assertTrue(isElementHidden(testCode.wrapperTableSCT(), 5), "        Suppress table must be hide");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Suppress Cross - Edit Payor with invalid format effective date")
	public void testXPR_129() throws Exception {
		logger.info("===== Testing - testXPR_129 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should show");
		assertTrue(testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> singleTestCodeInfo = daoManagerXifinRpm.getSingleHavePayorSuppress(testDb);
		String singleTestCodeInfo = "AA361KY";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test Label should show");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Edit payor with invalid format exp date");
		assertTrue(isElementPresent(testCode.sCTTab(), 5),"       Suppress Cross-Test Consolidations should show");
		testCode.clickSCTTab();
		assertTrue(isElementPresent(testCode.sCTTable(), 5),"       Suppress table should be available");
		testCodeUtils.scrollIntoView(testCode.getRowOnTableSCT(1+1));
		testCodeUtils.doubleClickOnRowByIndex(testCode.sCTTable(), 1);
		assertTrue(isElementPresent(testCode.expDtOnSCTT(), 5),"       Expiration date input field should show");
		testCode.clearData(testCode.expDtOnSCTT());
		testCode.inputExpDtOnSCTT("2015/01/01");
		testCode.clickOkOnPayorSCT();
		assertTrue(isElementPresent(testCode.closeErrorMessage(), 5),"      error message should show");
		testCode.clickcloseErrorMessage();
		testCode.inputExpDtOnSCTT("01/01/2015");
		testCode.clickOkOnPayorSCT();

		logger.info("*** Step 4 Action: - Edit payor with empty effective date");
		testCode.clearData(testCode.effDtOnSCTT());
		testCode.clickOkOnPayorSCT();

		logger.info("*** Step 4 Expected Results: - Verify that error show correct");
		assertTrue(isElementPresent(testCode.payorSCTFormError(), 5), "        Error must be show.");
		assertTrue(testCode.payorSCTFormErrorMsg().getText().equalsIgnoreCase("Effective Date: Field is required"), "        Eff date error message must be show correct.");
		
		testCode.cancelBtn().click();	
		testCode.clickResetBtn();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}
	
	@Test(priority = 1, description = "Single Test Detail - Suppress Cross - Cancel add new payor")
	public void testXPR_130() throws Exception {
		logger.info("===== Testing - testXPR_130 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should show");
		assertTrue(testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> singleTestCodeInfo = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTestCodeInfo = "CEY926";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test Label should show");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Add payor with valid data");
		assertTrue(isElementPresent(testCode.addSCTButton(), 5),"        Add suppress should show");
		List<String> suppress = testCode.addSuppressCT("", "", false, testCode, this, testDb);
		assertTrue(isElementPresent(testCode.cancelBtn(), 5),"        Cancel button should show");
		testCode.clickCancelOnPayorSCT();

		logger.info("*** Step 3 Expected Results: - Verify that payor not add to table");
		assertTrue(isElementPresent(testCode.sCTTable(), 5), "        Suppress table must be presented");
		assertFalse(getColumnValue(testCode.sCTTable(), suppress.get(0)), "        Payor ID must be added to table.");
		assertFalse(getColumnValue(testCode.sCTTable(), suppress.get(1)), "        Effective date must be added to table.");

		testCode.clickResetBtn();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Suppress Cross - Reset the delete payor info")
	public void testXPR_165() throws Exception {
		logger.info("===== Testing - testXPR_165 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should show");
		assertTrue(testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> singleTestCodeInfo = daoManagerXifinRpm.getSingleHavePayorSuppress(testDb);
		String singleTestCodeInfo = "AAE015";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test Label should show");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Delete payor in Suppress");
		assertTrue(isElementPresent(testCode.sCTTable(), 5), "        Suppress table must be presented");
		List<String> suppress = testCode.editSuppressCT(1, "", "", false, this);
		assertTrue(isElementPresent(testCode.deleteCheckBoxSCT(), 5), "        Delete check box must be presented");
		selectCheckBox(testCode.deleteCheckBoxSCT());
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Submit button must be presented");
		testCode.clickOkOnPayorSCT();

		logger.info("*** Step 3 Expected Results: - Verify that payor had been marked remove from table");
		assertTrue(testCodeUtils.hasClass(testCode.getRowOnTableSCT(2), "rowMarkedForDelete"), "        Row must be marked to remove.");

		logger.info("*** Step 4 Action: - Reset all");
		assertTrue(isElementPresent(testCode.resetBtn(), 5), "        Reset button must be presented");
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);

		logger.info("*** Step 4 Expected Result : - Landing page load test code must be present");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "       Landing page load test code must be present");

		logger.info("*** Step 5 Actions: - Load again single test");		
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeInfo);
		assertTrue(isElementPresent(testCode.sCTTab(), 5), "        Suppress tab must be presented");
		testCode.clickSCTTab();

		logger.info("*** Step 5 Expected Result : - Payor will not remove from table Suppress");
		assertTrue(getColumnValue(testCode.sCTTable(), suppress.get(2)), "        Payor must be not removed from table.");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Suppress Cross - Reset add new payor ID")
	public void testXPR_166() throws Exception {
		logger.info("===== Testing - testXPR_166 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should show");
		assertTrue(testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> singleTestCodeInfo = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTestCodeInfo = "YQ899MV";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test Label should show");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Add payor with valid data");
		assertTrue(isElementPresent(testCode.addSCTButton(), 5), "        Add Suppress button must be presented");
		List<String> suppress = testCode.addSuppressCT("", "", true, testCode, this, testDb);

		logger.info("*** Step 3 Expected Results: - Verify that payor added to table");
		assertTrue(isElementPresent(testCode.sCTTable(), 5), "        Suppress table must be presented");
		assertTrue(getColumnValue(testCode.sCTTable(), suppress.get(0)), "        Payor ID must be added to table.");
		assertTrue(getColumnValue(testCode.sCTTable(), suppress.get(1)), "        Effective date must be added to table.");

		logger.info("*** Step 4 Action: - Reset all");
		assertTrue(isElementPresent(testCode.resetBtn(), 5), "        Reset button must be presented");
		testCode.clickResetBtn();
		closeAlertAndGetItsText(true);

		logger.info("*** Step 4 Expected Result : - Landing page load test code must be present");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "       Landing page load test code must be present");

		logger.info("*** Step 5 Actions: - Load again single test");		
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        TestId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeInfo);
		testCode.clickSCTTab();

		logger.info("*** Step 5 Expected Result : - Payor add before not add to table Suppress");
		assertFalse(getColumnValue(testCode.sCTTable(), suppress.get(1)), "        Eff date must be not available in table.");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Suppress Cross - Delete payor in the table list")
	public void testXPR_196() throws Exception {
		logger.info("===== Testing - testXPR_196 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should show");
		assertTrue(testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
		String testID = "TESTXPR_196";
		testCode.createSingleTest(testID, testDb, true, false, this);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test Label should show");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - select any row and click edit button");
		testCode.clickSCTTab();
		String payorId = testCode.checkSuppressCT();
		selectColumnValue(testCode.sCTTable(), payorId);
		
		logger.info("*** Step 3 Expected Results: - Edit pop up is displayed");
		assertTrue(isElementPresent(testCode.effDtOnSCTT(), 5),"       edit popup should show");
		
		logger.info("*** Step 4 Action: - Check on delete checkbox and click ok button");
		assertTrue(isElementPresent(testCode.deleteCheckBoxSCT(), 5), "        Delete Suppress button must be presented");
		selectCheckBox(testCode.deleteCheckBoxSCT());
		assertTrue(isElementPresent(testCode.okBtn(), 5), "        Submit button must be presented");
		testCode.clickOkOnPayorSCT();

		logger.info("*** Step 4 Expected Results: - Verify that payor had been marked remove from table");
		assertTrue(testCodeUtils.hasClass(testCode.getRowOnTableSCT(2), "rowMarkedForDelete"), "        Row must be marked to remove.");

		logger.info("*** Step 5 Action: - Save and clear");
		assertTrue(isElementPresent(testCode.saveAndClearBtn(), 5), "        Submit button must be presented");
		testCode.clickSaveAndClearBtn();

		logger.info("*** Step 5 Expected Result : - Landing page load test code must be present");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "       Landing page load test code must be present");

		logger.info("*** Step 6 Actions: - Load again single test");		
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        TestId input field should show");
		testCode.checkInputTestCodeId(testID);
		assertTrue(isElementPresent(testCode.sCTTab(), 5),"        Suppress tab should show");
		testCode.clickSCTTab();

		logger.info("*** Step 6 Expected Result : - Payor removed from table Suppress");
		assertFalse(getColumnValue(testCode.sCTTable(), payorId), "        Payor must be removed from table.");

		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Suppress Cross - Verify filter function")
	public void testXPR_209() throws Exception {
		logger.info("===== Testing - testXPR_209 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should show");
		assertTrue(testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> singleTestCodeInfo = daoManagerXifinRpm.getSingleHavePayorSuppress(testDb);
		String singleTestCodeInfo = "AAI873";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeInfo);
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test Label should show");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Filter with correct information");
		testCode.clickSCTTab();
		assertTrue(isElementPresent(testCode.sCTTable(), 5),"       Suppress Cross-Test Consolidations table should show.");
		String payID = testCode.getTextFromTableSCT(2, 2).trim();
		String payEff = testCode.getTextFromTableSCT(3, 2).trim();
		String payExp = testCode.getTextFromTableSCT(4, 2).trim();
		assertTrue(isElementPresent(testCode.payorFilterSCT(), 5),"        PayorFilter input field should show");
		testCode.filterPayorIDSCT(payID);
		assertTrue(isElementPresent(testCode.effFilterSCT(), 5),"        Eff Date input field should show");
		testCode.filterEffSCT(payEff);
		assertTrue(isElementPresent(testCode.expFilterSCT(), 5),"        Expiration date input field should show");
		testCode.filterExpSCT(payExp);

		logger.info("*** Step 3 Expected results : - Verify Suppress Cross-Test Consolidations filters work properly");
		assertTrue(testCode.verifyFilterSCT(payID, payEff, payExp),"        Suppress Cross-Test Consolidations filters should work properly.");

		testCode.clickResetBtn();
//		Close the popup alert
		closeAlertAndGetItsText(true);
	}

	@Test(priority = 1, description = "Single Test Detail - Suppress Cross - Add payor with required field is empty")
	public void testXPR_289() throws Exception {
		logger.info("===== Testing - testXPR_289 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		timeStamp = new TimeStamp(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should show");
		assertTrue(testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> singleTestCodeInfo = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTestCodeInfo = "YO412BD";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeInfo);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test Label should show");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: - Add payor with empty Payor ID");
		assertTrue(isElementPresent(testCode.sCTTab(), 5),"        Suppress tab should show");
		testCode.clickSCTTab();
		assertTrue(isElementPresent(testCode.addSCTButton(), 5),"        Add suppress button should show");
		testCodeUtils.scrollIntoView(testCode.addSCTButton());
		testCode.clickAddSCTButton();
		String effInput = timeStamp.getCurrentDate("MM/dd/yyyy");
		assertTrue(isElementPresent(testCode.effDtOnSCTT(), 5),"        eff Date input field should show");
		testCode.inputEffDtOnSCTT(effInput);
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Submit button should show");
		testCode.clickOkOnPayorSCT();

		logger.info("*** Step 3 Expected Results: - Verify that error show correct");
		assertTrue(isElementPresent(testCode.payorSCTFormError(), 5), "        Error must be show.");
		assertTrue(testCode.payorSCTFormErrorMsg().getText().equalsIgnoreCase("Payor ID: Field is required"), "        Payor ID error message must be show correct.");

		logger.info("*** Step 4 Action: - Edit payor with empty effective date");
		assertTrue(isElementPresent(testCode.cancelBtn(), 5),"        Cancel button should show");
		testCode.clickCancelOnPayorSCT();
		testCode.addSuppressCT("", "", false, testCode, this, testDb);
		assertTrue(isElementPresent(testCode.effDtOnSCTT(), 5),"        Eff date should show");
		testCode.clearData(testCode.effDtOnSCTT());
		assertTrue(isElementPresent(testCode.okBtn(), 5),"        Submit button should show");
		testCode.clickOkOnPayorSCT();

		logger.info("*** Step 4 Expected Results: - Verify that error show correct");
		assertTrue(isElementPresent(testCode.payorSCTFormError(), 5), "        Error must be show.");
		assertTrue(testCode.payorSCTFormErrorMsg().getText().equalsIgnoreCase("Effective Date: Field is required"), "        Eff date error message must be show correct.");

		testCode.cancelBtn().click();
		testCode.clickResetBtn();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}
	
	//Help button icon
	@Test(priority = 1, description = "Load Test - Verify Help link icon")
	public void testXPR_401() throws Exception {
		logger.info("===== Testing - testXPR_401 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
    	navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");
		
		logger.info("*** Step 2 Actions: - In Load Test Page - Click on Help icon");
		assertTrue(isElementPresent(testCode.testCodeHelpIcon(), 5), "        Help icon must be displayed in Test Code form");
		String beforeURL = driver.getCurrentUrl();
		testCode.clickTestCodeHelpIcon();
		String winHandler = driver.getWindowHandle();
		switchToPopupWin();
		String afterURL = driver.getCurrentUrl();
		
		logger.info("*** Step 2 Expected Results: - The Help page is displayed");
		assertNotEquals(beforeURL, afterURL, "        Popup Help page must be displayed. Current Popup URL must not equals Test Code URL");
		driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
	}
	
	@Test(priority = 1, description = "Test Code Detail - Verify Help link icon")
	public void testXPR_402() throws Exception {
		logger.info("===== Testing - testXPR_402 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
    	navigation.navigateToTestCodePage();

    	logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testIdInput(), 5), "        The Test Code page did not load");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");
		
		logger.info("*** Step 2 Action: - Input an valid Test ID and Tab out");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest = "1585130753700";
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - The Test Code detail page is displayed");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().contains("(Single Test)"),"        Test Label should be Single Test.");
		
		logger.info("*** Step 3 Actions: - At Effective date information section. Click on Help icon");
		assertTrue(isElementPresent(testCode.effDateHelpIcon(), 5), "        Help icon in Effective Date information section must be displayed");
		String beforeURL = driver.getCurrentUrl();
		testCode.clickeffDateHelpIcon();
		String winHandler = driver.getWindowHandle();
		switchToPopupWin();
		String afterURL = driver.getCurrentUrl();
		
		logger.info("*** Step 3 Expected Results: - The Help page is displayed");
		assertNotEquals(beforeURL, afterURL, "        Popup Help page must be displayed. Current Popup URL must not equals Single test URL");
		
		driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
        testCode.resetBtn().click();
	}
	
	/* PROC CODE SEARCH */
	@Test(priority = 1, description = "Single Test Detail - Procedures - Search Proc Code - Search Proc Code with select Proc table")
	public void testXPR_50() throws Exception {
		logger.info("===== Testing - testXPR_50 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		procCodeSearch = new ProcCodeSearch(driver);
		procCodeSearchResults = new ProcCodeSearchResults(driver);

		logger.info("***Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        Label test should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input a valid Test ID and Tab out");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test Id input should show.");
		String testAbbrev = "ULVD26";
		testCode.checkInputTestCodeId(testAbbrev);

		logger.info("*** Step 2 Expected Results: - System display Test Code  with main information as below.");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");
		assertTrue(isElementPresent(testCode.procedureTab(),5),"        Procedure tab should show.");
		assertTrue(isElementPresent(testCode.facilityTab(),5),"        Facility tab should show.");
		assertTrue(isElementPresent(testCode.modifierTab(),5),"        Modifier tab should show.");
		assertTrue(isElementPresent(testCode.feeScheduleTab(),5),"        FeeSchedule tab should show.");
		assertTrue(isElementPresent(testCode.xrefTab(),5),"        Xref tab should show.");
		assertTrue(isElementPresent(testCode.exclusionsTab(),5),"        Exclusions tab should show.");
		assertTrue(isElementPresent(testCode.sCTTab(),5),"            Suppress Cross-Test Consolidations tab should show.");
	
		logger.info("*** Step 3 Action: - Procedure tab: click on add icon");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(),5),"            Add Procedure button should show.");
		testCodeUtils.scrollIntoView(testCode.addProcedureBtn());
		testCode.clickAddProcedureBtn();

		logger.info("*** Step 3 Expected Results: - Add record popup is displayed.");
		assertTrue(isElementPresent(testCode.editProcPopup(),5),"            Edit Procedure Popup should show.");

		logger.info("*** Step 4 Action: - Select Procedure table and click on Proc code search icon");
		List<String> procedureCodeTemp = daoManagerXifinRpm.getProcCodeInfoFromPROCCDByTestAbbrev(testAbbrev, testDb);
		String procedureCodeTable = procedureCodeTemp.get(1);
		String procTypId = procedureCodeTemp.get(2);
		testCode.selectProcedureTable(procedureCodeTable);
		assertTrue(isElementPresent(procCodeSearch.procCodeSearchIcon(),5),"            Procedure Search icon should show.");
		procCodeSearch.clickProcCodeSearchIcon();
		String parentNo1 = switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Procedure Code Search popup and selected Procedure Code is displayed ");
		assertTrue(isElementPresent(procCodeSearchResults.procCodeSearchPage(),5),"            Procedure Search Result page should show.");
		assertTrue(isElementPresent(procCodeSearchResults.procTypeDropdown(),5),"            Procedure Code Type should show.");
		String value = testCodeUtils.getTextSelectedInDropdown(procCodeSearchResults.procTypeDropdown());
		assertEquals(value,procedureCodeTable,"        Selected Procedure Code should show.");
		
		logger.info("*** Step 5 Action: - Click on Procedure Code Search button");
		assertTrue(isElementPresent(procCodeSearch.procCodeSearchButton(),5),"            Procedure Code Search button should show.");
		procCodeSearch.clickProcCodeSearchButton();
		
		logger.info("*** Step 5 Expected Results: - System display Procedure Code that match with search condition in Search Result page");
		String totalProcedure = daoManagerXifinRpm.getProcCodeSearchBaseOnProcTyp(procTypId, testDb);
		assertTrue(isElementPresent(procCodeSearchResults.rightPagerTableResultSearch(), 5), "        Pager in result table must be displayed");
		String totalTestCodeID = testCodeUtils.getTotalResultSearch(procCodeSearchResults.rightPagerTableResultSearch());
		assertEquals(testCodeUtils.convertDecimalFormat(Integer.parseInt(totalProcedure)), totalTestCodeID, "        Total Procedure Code should be "+totalTestCodeID);	
		
		procCodeSearch.clickCloseBtn();
		switchToParentWin(parentNo1);
		testCode.clickCancelProcedureBtn();
		testCode.clickResetBtn();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}
	
	@Test(priority = 1, description = "Single Test Detail - Procedures - Search Proc Code - Search all procedure code for any procedure table")
	public void testXPR_53() throws Exception {
		logger.info("===== Testing - testXPR_53 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		procCodeSearch = new ProcCodeSearch(driver);
		procCodeSearchResults = new ProcCodeSearchResults(driver);
		testCodeUtils = new TestCodeUtils(driver);

		logger.info("***Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Expected Results: - Verify that it's on the Load Test Code page");
		wait.until(ExpectedConditions.visibilityOf(testCode.testCodePageTitle()));
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Action: - Enter an existing single Test ID and Tab out");
		String testAbbrev = "GH191DN";
		testCode.checkInputTestCodeId(testAbbrev);

		logger.info("*** Expected Results: - Verify that it's on the Test Code Single Test screen");
		wait.until(ExpectedConditions.visibilityOf(testCode.procedureTab()));
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");		

		logger.info("*** Action: - Click on Add button in the Procedures tab");
		testCode.clickProcedureTab();		
		clickHiddenPageObject(testCode.addProcedureBtn(), 0);

		logger.info("*** Expected Results: - Verify that Add Record popup is displayed.");		
		wait.until(ExpectedConditions.visibilityOf(testCode.editProcPopup()));

		logger.info("*** Action: - Select a Procedure Table");
		List<String> procedureCodeTemp = daoManagerXifinRpm.getProcCodeInfoFromPROCCDByTestAbbrev(testAbbrev, testDb);
		String procedureCodeTable = procedureCodeTemp.get(1);
		String procTypId = procedureCodeTemp.get(2);		
		selectItemByVal(testCode.procedureTableDropdownInAddRecord(), procTypId);	
		
		logger.info("*** Action: - Click Proc Code Search icon button");
		procCodeSearch.clickProcCodeSearchIcon();
		String parent = switchToPopupWin();
		
		logger.info("*** Expected Results: - Verify that Procedure Code Search popup is displayed and Procedure Code Type is default to the selected Procedure Code Table");
		wait.until(ExpectedConditions.visibilityOf(procCodeSearchResults.procTypeDropdown()));
		String value = testCodeUtils.getTextSelectedInDropdown(procCodeSearchResults.procTypeDropdown());
		assertEquals(value,procedureCodeTable,"        Selected Procedure Code should show.");
		
		logger.info("*** Action: - Enter '*' in Procedure Code input field and Click on Search button");		
		procCodeSearch.inputProcedureCodeID("*");		
		procCodeSearch.clickProcCodeSearchButton();
		switchToPopupWin();
		
		logger.info("*** Expected Results: - Verify that Procedure Code Search Results is displayed and matches with the search condition");
		String totalProcID = daoManagerXifinRpm.getAllProcCodeSearchBaseOnProcTyp(procTypId, testDb);		
		wait.until(ExpectedConditions.visibilityOf(procCodeSearchResults.rightPagerTableResultSearch()));
		String totalTestCodeID = testCodeUtils.getTotalResultSearch(procCodeSearchResults.rightPagerTableResultSearch());
		assertEquals(testCodeUtils.convertDecimalFormat(Integer.parseInt(totalProcID)), totalTestCodeID, "        Total Procedure Code should be "+totalTestCodeID);
		
		logger.info("*** Action: Reset the Test Code page");
		procCodeSearch.clickCloseBtn();
		switchToParentWin(parent);
		testCode.clickCancelProcedureBtn();
		testCode.clickResetBtn();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}
	
	@Test(priority = 1, description = "Single Test Detail - Procedures - Search Proc Code - Search invalid Procedure Code")
	public void testXPR_58() throws Exception {
		logger.info("===== Testing - testXPR_58 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		procCodeSearch = new ProcCodeSearch(driver);
		procCodeSearchResults = new ProcCodeSearchResults(driver);

		logger.info("***Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - Verify that it's on the Load Test Code page");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        Label test should show.");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input a valid Test ID and Tab out");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test Id input should show.");
		String testAbbrev = "50535";
		testCode.checkInputTestCodeId(testAbbrev);

		logger.info("*** Step 2 Expected Results: - System display Test Code  with main information as below.");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");
		assertTrue(isElementPresent(testCode.procedureTab(),5),"        Procedure tab should show.");

		logger.info("*** Step 3 Action: - Procedure tab: click on add icon");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(),5),"            Add Procedure button should show.");
		testCodeUtils.scrollIntoView(testCode.addProcedureBtn());
		testCode.clickAddProcedureBtn();

		logger.info("*** Step 3 Expected Results: - Add record popup is displayed.");
		assertTrue(isElementPresent(testCode.editProcPopup(),5),"            Edit Procedure Popup should show.");

		logger.info("*** Step 4 Action: - Select Procedure table and click on Proc code search icon");
		List<String> procedureCodeTemp = daoManagerXifinRpm.getProcCodeInfoFromPROCCDByTestAbbrev(testAbbrev, testDb);
		String procedureCodeTable = procedureCodeTemp.get(1).trim();		
		testCode.selectProcedureTable(procedureCodeTable);
		assertTrue(isElementPresent(procCodeSearch.procCodeSearchIcon(),5),"            Procedure Search icon should show.");
		procCodeSearch.clickProcCodeSearchIcon();
		String parent = switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Procedure Code Search popup and selected Procedure Code is displayed ");
		assertTrue(isElementPresent(procCodeSearchResults.procCodeSearchPage(),5),"            Procedure Search Result page should show.");
		assertTrue(isElementPresent(procCodeSearchResults.procTypeDropdown(),5),"            Procedure Code Type should show.");
		String value = testCodeUtils.getTextSelectedInDropdown(procCodeSearchResults.procTypeDropdown());
		assertEquals(value,procedureCodeTable,"        Selected Procedure Code should show.");
		
		logger.info("*** Step 5 Action: - Enter invalid value in Procedure Code ID field and Click on Procedure Code Search button");
		assertTrue(isElementPresent(procCodeSearch.searchProcedureIdInput(),5),"            Procedure Code Id input should show.");
		procCodeSearch.inputProcedureCodeID("aaabbbccc");
		assertTrue(isElementPresent(procCodeSearch.procCodeSearchButton(),5),"            Procedure Code Search button should show.");
		procCodeSearch.clickProcCodeSearchButton();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - System display Procedure Code that match with search condition in Search Result page");
		assertTrue(isElementPresent(procCodeSearchResults.rightPagerTableResultSearch(), 5), "        Pager in result table must be displayed");
		assertTrue(procCodeSearchResults.rightPagerTableResultSearch().getText().trim().contains("No Result"),"        System display 'No Result' label");
		
		procCodeSearch.clickCloseBtn();
		switchToParentWin(parent);
		testCode.clickCancelProcedureBtn();
		testCode.clickResetBtn();
        closeAlertAndGetItsText(true);
	}
	
	@Test(priority = 1, description = "Single Test Detail - Search Proc Code - Search Proc with empty field")
	public void testXPR_160() throws Exception {
		logger.info("===== Testing - testXPR_160 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		procCodeSearch = new ProcCodeSearch(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        Test Code Page title must be available");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest = "MD851RR";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input must be available");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label must be available");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		
		logger.info("*** Step 3 Action: - Switch to procedure tab. Click add icon");
		assertTrue(isElementPresent(testCode.procedureTab(), 5),"        Procedure tab must be available");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5),"        Procedure button add must be available");
		testCode.clickAddProcedureBtn();
		
		logger.info("*** Step 3 Expected Results: - Verify add procedure code popup display");
		assertTrue(isElementPresent(testCode.searchProcedureIcon(), 5),"        Search procedure icon must be available");

		logger.info("*** Step 4 Action: - Click search procedure");
		testCode.clickSearchProcedureIcon();
		String parent = switchToPopupWin();

		logger.info("*** Step 4 Expected Results: - Verify search procedure code popup display");
		assertTrue(isElementPresent(procCodeSearch.searchProcedureIdInput(), 5),"        Search proc code id must be available");
		assertTrue(isElementPresent(procCodeSearch.searchProcDescrInput(), 5),"        Search proc code description must be available");
		assertTrue(isElementPresent(procCodeSearch.searchProcTypeDropdown(), 5),"        Search proc type dropdown must be available");
		assertTrue(isElementPresent(procCodeSearch.searchRevenueCodeDropdown(), 5),"        Search revenue dropdown must be available");
		assertTrue(isElementPresent(procCodeSearch.searchButtonInSearchScreen(), 5),"        Search button must be available");
		
		logger.info("*** Step 5 Action: - Click search without input");
		procCodeSearch.clickSearchBtnInSearchScreen();
		
		logger.info("*** Step 5 Expected Results: - Verify that error message appear when search with empty field");
		assertTrue(isElementPresent(testCode.messageContent(), 5),"        Error message must be display");
		assertTrue(testCode.messageContent().getText().equalsIgnoreCase("At least one field must be used to initiate a search."),"        Error message must be display correct");
		assertTrue(isElementPresent(testCode.closeErrorMessage(), 5),"        Close Error message must be display");
		testCode.clickcloseErrorMessage();		
		
		procCodeSearch.clickCloseBtn();
		switchToParentWin(parent);
		testCode.clickCancelProcedureBtn();
		testCode.clickResetBtn();
	}
	
	@Test(priority = 1, description = "Single Test Detail - Search Proc Code - Reset button in search Proc")
	public void testXPR_161() throws Exception {
		logger.info("===== Testing - testXPR_161 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		procCodeSearch = new ProcCodeSearch(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        Test Code Page title must be available");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest = "HOZ189";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input must be available");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label must be available");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		
		logger.info("*** Step 3 Action: - Switch to procedure tab. Click add icon");
		assertTrue(isElementPresent(testCode.procedureTab(), 5),"        Procedure tab must be available");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5),"        Procedure button add must be available");
		testCode.clickAddProcedureBtn();
		
		logger.info("*** Step 3 Expected Results: - Verify add procedure code popup display");
		assertTrue(isElementPresent(testCode.searchProcedureIcon(), 5),"        Search procedure icon must be available");

		logger.info("*** Step 4 Action: - Click search procedure");
		testCode.clickSearchProcedureIcon();
		String parent = switchToPopupWin();

		logger.info("*** Step 4 Expected Results: - Verify search procedure code popup display");
		assertTrue(isElementPresent(procCodeSearch.searchProcedureIdInput(), 5),"        Search proc code id must be available");
		assertTrue(isElementPresent(procCodeSearch.searchProcDescrInput(), 5),"        Search proc code description must be available");
		assertTrue(isElementPresent(procCodeSearch.searchProcTypeDropdown(), 5),"        Search proc type dropdown must be available");
		assertTrue(isElementPresent(procCodeSearch.searchRevenueCodeDropdown(), 5),"        Search revenue dropdown must be available");
		assertTrue(isElementPresent(procCodeSearch.resetSearchButton(), 5),"        Reset search button must be available");
		
		logger.info("*** Step 5 Action: - Input search field. Click reset");
		procCodeSearch.inputProcedureCodeID("8XP9");
		procCodeSearch.inputSearchProcDescr("Testing input search procedure description...");
		testCodeUtils.selectItemByIndex(procCodeSearch.searchProcTypeDropdown(), 2);
		testCodeUtils.selectItemByIndex(procCodeSearch.searchRevenueCodeDropdown(), 2);
		procCodeSearch.clickResetSearch();
		
		logger.info("*** Step 5 Expected Results: - Verify that all field are reset");
		assertTrue(procCodeSearch.searchProcedureIdInput().getAttribute("value").isEmpty(),"        Procedure ID input must be reset to empty");
		assertTrue(procCodeSearch.searchProcTypeDropdown().getAttribute("value").isEmpty(),"        Procedure description input must be reset to empty");
		assertTrue(testCodeUtils.getTextSelectedInDropdown(procCodeSearch.searchProcTypeDropdown()).isEmpty(),"        Search proc type dropdown must be reset to empty");
		assertTrue(testCodeUtils.getTextSelectedInDropdown(procCodeSearch.searchRevenueCodeDropdown()).isEmpty(),"        Search proc revenue dropdown must be reset to empty");
				
		procCodeSearch.clickCloseBtn();
		switchToParentWin(parent);
		testCode.clickCancelProcedureBtn();
		testCode.clickResetBtn();
	}
	
	@Test(priority = 1, description = "Single Test Detail - Search Proc Code - Verify new search button")
	public void testXPR_163() throws Exception {
		logger.info("===== Testing - testXPR_163 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		procCodeSearch = new ProcCodeSearch(driver);
		procCodeSearchResults = new ProcCodeSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        Test Code Page title must be available");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available single test code");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest = "KW007IW";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input must be available");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label must be available");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		
		logger.info("*** Step 3 Action: - Switch to procedure tab. Click add icon");
		assertTrue(isElementPresent(testCode.procedureTab(), 5),"        Procedure tab must be available");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5),"        Procedure button add must be available");
		clickHiddenPageObject(testCode.addProcedureBtn(), 0);
		
		logger.info("*** Step 3 Expected Results: - Verify add procedure code popup display");
		assertTrue(isElementPresent(testCode.searchProcedureIcon(), 5),"        Search procedure icon must be available");

		logger.info("*** Step 4 Action: - Click search procedure");
		testCode.clickSearchProcedureIcon();
		String parent = switchToPopupWin();

		logger.info("*** Step 4 Expected Results: - Verify search procedure code popup display");
		assertTrue(isElementPresent(procCodeSearch.searchProcedureIdInput(), 5),"        Search proc code id must be available");
		assertTrue(isElementPresent(procCodeSearch.searchProcDescrInput(), 5),"        Search proc code description must be available");
		assertTrue(isElementPresent(procCodeSearch.searchProcTypeDropdown(), 5),"        Search proc type dropdown must be available");
		assertTrue(isElementPresent(procCodeSearch.searchRevenueCodeDropdown(), 5),"        Search revenue dropdown must be available");
		assertTrue(isElementPresent(procCodeSearch.searchButtonInSearchScreen(), 5),"        Search button must be available");
		
		logger.info("*** Step 5 Action: - Search valid procedure code ID");
		List<String> procedureCode = daoManagerXifinRpm.getProcedureCodeId(testDb);
		procCodeSearch.searchProcedureCode(procedureCode.get(0));
		
		logger.info("*** Step 5 Expected Results: - Verify that search result match with data input");
		assertTrue(procCodeSearchResults.procedureIdLinkTestCodeSearchResults(2,2).getText().equalsIgnoreCase(procedureCode.get(0)),"        Procedure ID search result must be match with search input");
		
		logger.info("*** Step 6 Action: - Click new search");
		assertTrue(isElementPresent(procCodeSearchResults.newSearchBtn(), 5),"        New search button must be available");
		procCodeSearchResults.clickNewSearchBtn();
		
		logger.info("*** Step 6 Expected Results: - Verify page come back to search page");
		assertTrue(isElementPresent(procCodeSearch.searchProcedureIdInput(), 5),"        Search proc code id must be available");
		assertTrue(isElementPresent(procCodeSearch.searchProcDescrInput(), 5),"        Search proc code description must be available");
		assertTrue(isElementPresent(procCodeSearch.searchProcTypeDropdown(), 5),"        Search proc type dropdown must be available");
		assertTrue(isElementPresent(procCodeSearch.searchRevenueCodeDropdown(), 5),"        Search revenue dropdown must be available");
		assertTrue(isElementPresent(procCodeSearch.searchButtonInSearchScreen(), 5),"        Search button must be available");
		
		procCodeSearch.clickCloseBtn();
		switchToParentWin(parent);
		testCode.clickCancelProcedureBtn();
		testCode.clickResetBtn();
	}

	@Test(priority = 1, description = "Single Test Detail - Search Proc Code - Verify Keep Search Open checkbox")
	public void testXPR_164() throws Exception {
		logger.info("===== Testing - testXPR_164 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		procCodeSearch = new ProcCodeSearch(driver);
		procCodeSearchResults = new ProcCodeSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        Test Code Page title must be available");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Actions: - Input available single test code");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest = "TH422GO";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input must be available");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label must be available");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		
		logger.info("*** Step 3 Actions: - Switch to procedure tab. Click add icon");
		assertTrue(isElementPresent(testCode.procedureTab(), 5),"        Procedure tab must be available");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5),"        Procedure button add must be available");
		testCode.clickAddProcedureBtn();
		
		logger.info("*** Step 3 Expected Results: - Verify add procedure code popup display");
		assertTrue(isElementPresent(testCode.searchProcedureIcon(), 5),"        Search procedure icon must be available");

		logger.info("*** Step 4 Actions: - Click search procedure");
		testCode.clickSearchProcedureIcon();
		String parent = switchToPopupWin();

		logger.info("*** Step 4 Expected Results: - Verify search procedure code popup display");
		assertTrue(isElementPresent(procCodeSearch.searchProcedureIdInput(), 5),"        Search proc code id must be available");
		assertTrue(isElementPresent(procCodeSearch.searchProcDescrInput(), 5),"        Search proc code description must be available");
		assertTrue(isElementPresent(procCodeSearch.searchProcTypeDropdown(), 5),"        Search proc type dropdown must be available");
		assertTrue(isElementPresent(procCodeSearch.searchRevenueCodeDropdown(), 5),"        Search revenue dropdown must be available");
		assertTrue(isElementPresent(procCodeSearch.searchButtonInSearchScreen(), 5),"        Search button must be available");
		
		logger.info("*** Step 5 Actions: - Enter valid data in search fields and click Search button");
		String procedureCode = daoManagerXifinRpm.getProcedureCodeId(testDb).get(0);
		procCodeSearch.searchProcedureCode(procedureCode);
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - System open Procedure code search result screen match with data input");
		assertTrue(isElementPresent(procCodeSearchResults.recordsCountRight(), 10), "        Records count view in Search Results should show.");
		assertTrue(isElementPresent(procCodeSearchResults.procCodeSearchTable(), 5), "        Procedure Code Search Results Table should show.");
		assertTrue(getTableTotalRowSize(procCodeSearchResults.procCodeSearchTable()) >= 1, "        Procedure Code Search Result has at least one result");
		assertTrue(getColumnValue(procCodeSearchResults.procCodeSearchTable(), procedureCode), "        Procedure code id " + procedureCode + " should show in results table.");
		
		logger.info("*** Step 6 Actions: - Check on Keep Search Open Checkbox. Select any Procedure Code link in result list");
		selectCheckBox(procCodeSearchResults.keepSearchOpenCheckbox());
		procCodeSearchResults.clickOnProcCodeResultLink();
		
		logger.info("*** Step 6 Expected Results: - The search result page is kept");
		assertTrue(isElementPresent(procCodeSearchResults.procCodeSearchTable(), 5), "        Procedure Code Search Result Table must be displayed");
		procCodeSearchResults.clickCloseBtn();

		switchToParentWin(parent);
		testCode.clickCancelProcedureBtn();
		testCode.clickResetBtn();
		//Close the popup alert
		closeAlertAndGetItsText(true);
	}
	
	@Test(priority = 1, description = "Single Test Detail - Search Proc Code - Verify icons at footer menu")
	public void testXPR_231() throws Exception {
		logger.info("===== Testing - testXPR_231 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		procCodeSearch = new ProcCodeSearch(driver);
		procCodeSearchResults = new ProcCodeSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        Test Code Page title must be available");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Actions: - Input available single test code");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest = "CO807FE";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input must be available");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label must be available");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		
		logger.info("*** Step 3 Actions: - Switch to procedure tab. Click add icon");
		assertTrue(isElementPresent(testCode.procedureTab(), 5),"        Procedure tab must be available");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5),"        Procedure button add must be available");
		testCode.clickAddProcedureBtn();
		
		logger.info("*** Step 3 Expected Results: - Verify add procedure code popup display");
		assertTrue(isElementPresent(testCode.searchProcedureIcon(), 5),"        Search procedure icon must be available");

		logger.info("*** Step 4 Actions: - Click search procedure");
		testCode.clickSearchProcedureIcon();
		String parentWindow = switchToPopupWin();

		logger.info("*** Step 4 Expected Results: - Verify search procedure code popup display");
		assertTrue(isElementPresent(procCodeSearch.searchProcedureIdInput(), 5),"        Search proc code id must be available");
		assertTrue(isElementPresent(procCodeSearch.searchProcDescrInput(), 5),"        Search proc code description must be available");
		assertTrue(isElementPresent(procCodeSearch.searchProcTypeDropdown(), 5),"        Search proc type dropdown must be available");
		assertTrue(isElementPresent(procCodeSearch.searchRevenueCodeDropdown(), 5),"        Search revenue dropdown must be available");
		assertTrue(isElementPresent(procCodeSearch.searchButtonInSearchScreen(), 5),"        Search button must be available");
		
		logger.info("*** Step 5 Actions: - Input valid data to field and click Search button");
		procCodeSearch.searchProcedureCode("*");
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - System open Procedure code search result screen match with data input");
		assertTrue(testCode.isElementDisplayed(procCodeSearchResults.recordsCountRight(), 10), "        Records count view in Search Results should show.");
		assertTrue(isElementPresent(procCodeSearchResults.procCodeSearchTable(), 5), "        Procedure Code Search Result Table must be displayed");
		assertTrue(getTableTotalRowSize(procCodeSearchResults.procCodeSearchTable()) >= 1, "        Procedure Code Search Result has at least one result");
		assertTrue(isElementPresent(procCodeSearchResults.currentPageInput(), 5), "        Current page input must be displayed");
		
		logger.info("*** Step 6 Actions: - In Search Result page: Click on Next Page icon");
		String beforePage = procCodeSearchResults.currentPageInput().getAttribute("value");
		assertTrue(isElementPresent(procCodeSearchResults.nextPageButton(), 5), "        Next button at bottom menu must be displayed");
		procCodeSearchResults.clickOnNextPageButton();
		String currentPage = procCodeSearchResults.currentPageInput().getAttribute("value");
		
		logger.info("*** Step 6 Expected Results: - System will navigate to next page");
		assertNotEquals(beforePage, currentPage, "        Previous page must not equals Current page");
		assertEquals(Integer.parseInt(currentPage), Integer.parseInt(beforePage) + 1, "        Current page number must more than one previous page number");
		
		logger.info("*** Step 7 Actions: - In Search Result page: Click on Prev Page icon");
		beforePage = currentPage;
		assertTrue(isElementPresent(procCodeSearchResults.prevPageButton(), 5), "        Prev button at bottom menu must be displayed");
		procCodeSearchResults.clickOnPrevPageButton();
		currentPage = procCodeSearchResults.currentPageInput().getAttribute("value");
		
		logger.info("*** Step 7 Expected Results: - System will return to previous page");
		assertNotEquals(beforePage, currentPage, "        Previous page must not equals Current page");
		assertEquals(Integer.parseInt(currentPage), Integer.parseInt(beforePage) - 1, "        Current page number must less than one previous page number");
		
		logger.info("*** Step 8 Actions: - In Search Result page: Click on Last Page icon");
		beforePage = currentPage;
		assertTrue(isElementPresent(procCodeSearchResults.lastPageButton(), 5), "        Last page button at bottom menu must be displayed");
		procCodeSearchResults.clickOnLastPageButton();	
		//yli
		assertTrue(isElementDisabled(procCodeSearchResults.lastPageButton(), "class", "disabled", 10), "        The Last Page Button should be disabled when it is on the last page of the Search Results.");
		currentPage = procCodeSearchResults.currentPageInput().getAttribute("value");
		String totalPage = procCodeSearchResults.totalPageSpan().getText().replace(",", "");
		
		logger.info("*** Step 8 Expected Results: - System will return to last page");
		assertNotEquals(beforePage, currentPage, "        Previous page must not equals Current page");
		assertEquals(Integer.parseInt(currentPage), Integer.parseInt(totalPage), "        Current page number must equals previous page number");
		
		logger.info("*** Step 9 Actions: - In Search Result page: Click on First Page icon");
		beforePage = currentPage;
		assertTrue(isElementPresent(procCodeSearchResults.firstPageButton(), 5), "        First page button at bottom menu must be displayed");
		procCodeSearchResults.clickOnFirstPageButton();
		//yli
		assertTrue(isElementDisabled(procCodeSearchResults.firstPageButton(), "class", "disabled", 10), "        The First Page Button should be disabled when it is on the First page of the Search Results.");
		currentPage = procCodeSearchResults.currentPageInput().getAttribute("value");
		
		logger.info("*** Step 9 Expected Results: - System will return to first page");
		assertNotEquals(beforePage, currentPage, "        Previous page must not equals Current page");
		assertEquals(Integer.parseInt(currentPage), 1, "        Current page number must equals 1");
		
		logger.info("*** Step 10 Actions: - In Search Result page: Select number of entries per page");
		assertTrue(isElementPresent(procCodeSearchResults.numberOfEntriesDropdown(), 5), "        Number of entry per page dropdown at the bottom menu must be displayed");
		selectItemByVal(procCodeSearchResults.numberOfEntriesDropdown(), "40");
		
		logger.info("*** Step 10 Expected Results: - System will display 40 entries per page");
		assertTrue(isElementPresent(procCodeSearchResults.procCodeSearchTable(), 5), "        Procedure Code Search Result table must be displayed");
		assertEquals(getTableTotalRowSize(procCodeSearchResults.procCodeSearchTable()) - 1, 40, "        Number of result in table must be 40");		
		
		procCodeSearch.clickCloseBtn();
		switchToParentWin(parentWindow);
		testCode.clickCancelProcedureBtn();
		testCode.clickResetBtn();
	}
	
	@Test(priority = 1, description = "Single Test Detail - Search Proc Code - Close Search page")
	public void testXPR_232() throws Exception {
		logger.info("===== Testing - testXPR_232 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		procCodeSearch = new ProcCodeSearch(driver);
		randomCharacter = new RandomCharacter();

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        Test Code Page title must be available");
		assertTrue( testCodeUtils.getTextValue(testCode.testCodePageTitle(),5).trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Actions: - Input available single test code");
//		List<String> singleTest = daoManagerXifinRpm.getSingleTest(testDb);
		String singleTest = "CX453GV";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test ID input must be available");
		testCode.checkInputTestCodeId(singleTest);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5),"        Test label must be available");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		
		logger.info("*** Step 3 Actions: - Switch to procedure tab. Click add icon");
		assertTrue(isElementPresent(testCode.procedureTab(), 5),"        Procedure tab must be available");
		testCode.clickProcedureTab();
		assertTrue(isElementPresent(testCode.addProcedureBtn(), 5),"        Procedure button add must be available");
		clickHiddenPageObject(testCode.addProcedureBtn(), 0);
		
		logger.info("*** Step 3 Expected Results: - Verify add procedure code popup display");
		assertTrue(isElementPresent(testCode.searchProcedureIcon(), 5),"        Search procedure icon must be available");

		logger.info("*** Step 4 Actions: - Click search procedure");
		testCode.clickSearchProcedureIcon();
		String parentWindow = switchToPopupWin();

		logger.info("*** Step 4 Expected Results: - Verify search procedure code popup display");
		String getSearchUrl = driver.getCurrentUrl();
		assertTrue(isElementPresent(procCodeSearch.searchProcedureIdInput(), 5),"        Search proc code id must be available");
		assertTrue(isElementPresent(procCodeSearch.searchProcDescrInput(), 5),"        Search proc code description must be available");
		assertTrue(isElementPresent(procCodeSearch.searchProcTypeDropdown(), 5),"        Search proc type dropdown must be available");
		assertTrue(isElementPresent(procCodeSearch.searchRevenueCodeDropdown(), 5),"        Search revenue dropdown must be available");
		assertTrue(isElementPresent(procCodeSearch.searchButtonInSearchScreen(), 5),"        Search button must be available");
		
		logger.info("*** Step 5 Actions: - Input some fields in Search Proc Code Page. Click on Close button");
		procCodeSearch.inputProcedureCodeID(randomCharacter.getRandomNumericString(5));
		procCodeSearch.inputProcDescrInput(randomCharacter.getRandomNumericString(5));
		procCodeSearch.clickCloseBtn();
		
		logger.info("*** Step 5 Expected Results: - The search procedure code page is closed");
		switchToParentWin(parentWindow);
		String getCurrentUrl =  driver.getCurrentUrl();
		assertNotEquals(getSearchUrl, getCurrentUrl,"       Search screen must be closed");
		
		testCode.clickCancelProcedureBtn();
		testCode.clickResetBtn();		
	}

	@Test(priority = 1, description ="Single Test Detail - Fee Schedule - Search FS with invalid input")
	public void testXPR_114() throws Exception {
		logger.info("===== Testing - testXPR_114 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		feeScheduleSearch = new FeeScheduleSearch(driver);
		feeScheduleSearchResults = new FeeScheduleSearchResults(driver);

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page is displayed with correct page title");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Actions: - Input valid Single Test ID and tab out");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
//		List<String> testCodeInfo = daoManagerXifinRpm.getSingleTest(testDb);
		String testCodeInfo = "AO954QU";
		testCode.checkInputTestCodeId(testCodeInfo);

		logger.info("*** Step 2 Expected Results: - System display test code with information");
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
		assertTrue(isElementPresent(feeScheduleSearch.searchFSIcon(), 5),"        Fee Sched ID Search icon button is shown");
		clickHiddenPageObject(feeScheduleSearch.searchFSIcon(), 0);
		
		logger.info("*** Step 4 Expected Results: - Search Fee Schedule page is displayed");
		String parent = switchToPopupWin();
		
		logger.info("*** Step 5 Actions: Input invalid Fee Sched ID and click on Search button");
		assertTrue(isElementPresent(feeScheduleSearch.fsIDInput(), 5),"        Fee Sched ID input is shown");
		feeScheduleSearch.inputFSID("123456");
		assertTrue(isElementPresent(feeScheduleSearch.searchBtn(), 5),"        Fee Sched ID Search button is shown");
		clickHiddenPageObject(feeScheduleSearch.searchBtn(), 0);
		
		logger.info("*** Step 5 Expected Results: - Search result list is empty");
		assertTrue(isElementPresent(feeScheduleSearchResults.feeScheduleTblRightResult(), 5),"       Fee Schedule table right result is shown");
		assertEquals(feeScheduleSearchResults.feeScheduleTblRightResult().getText(),"No Result" ,"        Search result list is empty");
		
		assertTrue(isElementPresent(feeScheduleSearchResults.closeSearchBtn(), 5),"        Close button is shown");
		clickHiddenPageObject(feeScheduleSearchResults.closeSearchBtn(), 0);
		switchToParentWin(parent);
		assertTrue(isElementPresent(testCode.cancelProcedureDialog(), 5),"        Cancel button is shown");
		clickHiddenPageObject(testCode.cancelProcedureDialog(), 0);
		assertTrue(isElementPresent(testCode.resetBtn(), 5),"        Reset button is shown");
		clickHiddenPageObject(testCode.resetBtn(), 0);
	}
	
	@Test(priority=1, description="Single test Detail - Fee Schedule - Search FS with Retail radio btn is checked")
	public void testXPR_171() throws Exception{
		logger.info("===== Testing - testXPR_171 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		feeScheduleSearch = new FeeScheduleSearch(driver);
		feeScheduleSearchResults = new FeeScheduleSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		String singleTestCodeId = "NZ634NF";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeId);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test code page must be displayed");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5),"        Fee Schedule tab must be available");
		
		logger.info("*** Step 3 Action: - Add fee schedule");
		testCode.clickfeeScheduleTab();
		assertTrue(isElementPresent(testCode.addNewFSBtn(), 5));
		testCodeUtils.scrollIntoView(testCode.addNewFSBtn());
		testCode.clickAddNewFSBtn();

		logger.info("*** Step 3 Expected Results: - Verify that a new popup display");
		assertTrue(isElementPresent(testCode.feeSchIdInput(), 5),"        Fee schedule ID input must be available");
		assertTrue(isElementPresent(feeScheduleSearch.searchFSIcon(), 5),"        Fee schedule icon search must be available");
		
		logger.info("*** Step 4 Action: - Click on search fee schedule icon");
		feeScheduleSearch.clickSearchFSIcon();
		String parent = switchToPopupWin();

		logger.info("*** Step 4 Expected Results: - Search Fee Schedule page display");
		assertTrue(isElementPresent(feeScheduleSearch.searchBtn(), 5),"        Search Fee schedule must be available");
		assertTrue(isElementPresent(feeScheduleSearch.fsIDInput(), 5),"        Fee schedule ID must be available");
		
		logger.info("*** Step 5 Action: - Select parameter to search");
		assertTrue(isElementPresent(feeScheduleSearch.typeRadio(1), 5),"        Type Fee Schedule must be available");
		feeScheduleSearch.selectTypeRadio(1);
		assertTrue(isElementPresent(feeScheduleSearch.retailRadio(1), 5),"        Retail - Retail radio must be available");
		feeScheduleSearch.selectRetailRadio(1);
		assertTrue(isElementPresent(feeScheduleSearch.testBaseRadio(1), 5),"        Test base - Test radio must be available");
		feeScheduleSearch.selectTestBaseRadio(1);
		assertTrue(isElementPresent(feeScheduleSearch.clnBaseRadio(3), 5),"        Client base - both must be available");
		feeScheduleSearch.selectClnBaseRadio(3);
		feeScheduleSearch.clickSearchBtn();

		logger.info("*** Step 5 Expected Results: - Verify result");
		assertTrue(feeScheduleSearchResults.feeScheduleTblFSCell(2,4).getText().equalsIgnoreCase("Fee Schedule"),"       Type result must be correct with search condition");
		assertTrue(feeScheduleSearchResults.feeScheduleTblFSCell(2,5).getText().equalsIgnoreCase("Retail"),"       Retail result must be correct with search condition");
		assertTrue(feeScheduleSearchResults.feeScheduleTblFSCell(2,6).getText().equalsIgnoreCase("Test Based"),"       Test base result must be correct with search condition");

		feeScheduleSearch.clickCloseBtn();
		switchToParentWin(parent);
		testCode.clickCancelFeeScheduleBtn();
		testCode.clickResetBtn();
	}
	
	@Test(priority=1, description="Single Test Detail - Fee Schedule - Verify New Search button")
	public void testXPR_172() throws Exception{
		logger.info("===== Testing - testXPR_172 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		feeScheduleSearch = new FeeScheduleSearch(driver);
		feeScheduleSearchResults = new FeeScheduleSearchResults(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		String singleTestCodeId = "XN370HJ";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeId);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test code page must be displayed");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5),"        Fee Schedule tab must be available");
		
		logger.info("*** Step 3 Action: - Add fee schedule");
		testCode.clickfeeScheduleTab();
		assertTrue(isElementPresent(testCode.addNewFSBtn(), 5));
		testCodeUtils.scrollIntoView(testCode.addNewFSBtn());
		testCode.clickAddNewFSBtn();

		logger.info("*** Step 3 Expected Results: - Verify that a new popup display");
		assertTrue(isElementPresent(testCode.feeSchIdInput(), 5),"        Fee schedule ID input must be available");
		assertTrue(isElementPresent(feeScheduleSearch.searchFSIcon(), 5),"        Fee schedule icon search must be available");
		
		logger.info("*** Step 4 Action: - Click on search fee schedule icon");
		feeScheduleSearch.clickSearchFSIcon();
		String parent = switchToPopupWin();

		logger.info("*** Step 4 Expected Results: - Search Fee Schedule page display");
		assertTrue(isElementPresent(feeScheduleSearch.searchBtn(), 5),"        Search Fee schedule must be available");
		assertTrue(isElementPresent(feeScheduleSearch.fsIDInput(), 5),"        Fee schedule ID must be available");
		assertTrue(isElementPresent(feeScheduleSearch.typeRadio(3), 5),"        Both type radio must be available");
		assertTrue(isElementPresent(feeScheduleSearch.retailRadio(3), 5),"        Both Retail radio must be available");
		assertTrue(isElementPresent(feeScheduleSearch.testBaseRadio(3), 5),"        Test base - both radio must be available");
		
		logger.info("*** Step 5 Action: - Input fs ID to search");
		String fsID = prcDao.getFeeScheduleId().getPrcAbbrev();
		feeScheduleSearch.inputFSID(fsID);
		feeScheduleSearch.selectTypeRadio(3);
		feeScheduleSearch.selectRetailRadio(3);
		feeScheduleSearch.selectTestBaseRadio(3);
		feeScheduleSearch.clickSearchBtn();

		logger.info("*** Step 5 Expected Results: - Verify result");
		assertTrue(feeScheduleSearchResults.feeScheduleTblFSCell(2,2).getText().equalsIgnoreCase(fsID),"       Fee ID result must be correct with search condition");

		feeScheduleSearch.clickCloseBtn();
		switchToParentWin(parent);
		testCode.clickCancelFeeScheduleBtn();
		testCode.clickResetBtn();
	}
	
	@Test(priority=1, description="Single Test Detail - Fee Schedule - Verify Reset button in Search FS page")
	public void testXPR_173() throws Exception{
		logger.info("===== Testing - testXPR_173 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		testCodeUtils = new TestCodeUtils(driver);
		feeScheduleSearch = new FeeScheduleSearch(driver);
		randomCharacter = new RandomCharacter(driver);

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: - The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: - Input available test code");
		String singleTestCodeId = "41887";
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        testId input field should show");
		testCode.checkInputTestCodeId(singleTestCodeId);

		logger.info("*** Step 2 Expected Results: - Verify that it's on the Single Test Code page");
		assertTrue(isElementPresent(testCode.labelTest(), 5), "        Single test code page must be displayed");
		assertTrue( testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"), "        Test Label should be Single Test.");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5),"        Fee Schedule tab must be available");
		
		logger.info("*** Step 3 Action: - Add fee schedule");
		testCode.clickfeeScheduleTab();
		assertTrue(isElementPresent(testCode.addNewFSBtn(), 5));
		testCodeUtils.scrollIntoView(testCode.addNewFSBtn());
		testCode.clickAddNewFSBtn();

		logger.info("*** Step 3 Expected Results: - Verify that a new popup display");
		assertTrue(isElementPresent(testCode.feeSchIdInput(), 5),"        Fee schedule ID input must be available");
		assertTrue(isElementPresent(feeScheduleSearch.searchFSIcon(), 5),"        Fee schedule icon search must be available");
		
		logger.info("*** Step 4 Action: - Click on search fee schedule icon");
		feeScheduleSearch.clickSearchFSIcon();
		String parent = switchToPopupWin();

		logger.info("*** Step 4 Expected Results: - Search Fee Schedule page display");
		assertTrue(isElementPresent(feeScheduleSearch.resetSearchBtn(), 5),"        Reset search Fee schedule must be available");
		assertTrue(isElementPresent(feeScheduleSearch.fsIDInput(), 5),"        Fee schedule ID must be available");
		assertTrue(isElementPresent(feeScheduleSearch.fsNameInput(), 5),"        Fee schedule name must be available");
		assertTrue(isElementPresent(feeScheduleSearch.clientInput(), 5),"        Fee schedule client must be available");
		assertTrue(isElementPresent(feeScheduleSearch.payorInput(), 5),"        Fee schedule payor must be available");
		assertTrue(isElementPresent(feeScheduleSearch.typeRadio(1), 5),"        Fee schedule type must be available");
		assertTrue(isElementPresent(feeScheduleSearch.typeRadio(3), 5),"        Both type radio must be available");
		assertTrue(isElementPresent(feeScheduleSearch.retailRadio(2), 5),"        Non-Retail radio must be available");
		assertTrue(isElementPresent(feeScheduleSearch.retailRadio(3), 5),"        Both Retail radio must be available");
		assertTrue(isElementPresent(feeScheduleSearch.testBaseRadio(1), 5),"        Test base - test radio must be available");
		assertTrue(isElementPresent(feeScheduleSearch.testBaseRadio(3), 5),"        Test base - both radio must be available");
		
		logger.info("*** Step 5 Action: - Input all search field");
		feeScheduleSearch.inputFSID(randomCharacter.getRandomAlphaString(5));
		feeScheduleSearch.inputFSName(randomCharacter.getRandomAlphaString(5));
		feeScheduleSearch.inputClient(randomCharacter.getRandomAlphaString(5));
		feeScheduleSearch.inputPayor(randomCharacter.getRandomAlphaString(5));
		feeScheduleSearch.selectTypeRadio(1);
		feeScheduleSearch.selectRetailRadio(1);
		feeScheduleSearch.selectTestBaseRadio(1);
		feeScheduleSearch.clickResetSearchBtn();

		logger.info("*** Step 5 Expected Results: - Verify all field reset");
		assertTrue(feeScheduleSearch.fsIDInput().getAttribute("value").isEmpty(), "        FS ID input must be empty");
		assertTrue(feeScheduleSearch.fsNameInput().getAttribute("value").isEmpty(), "        FS Name input must be empty");
		assertTrue(feeScheduleSearch.clientInput().getAttribute("value").isEmpty(), "        Client input must be empty");
		assertTrue(feeScheduleSearch.payorInput().getAttribute("value").isEmpty(), "        Payor input must be empty");
		assertTrue(isChecked(feeScheduleSearch.typeRadio(3)), "        Type Fee Schedule must be checked");
		assertTrue(isChecked(feeScheduleSearch.retailRadio(3)), "        Non-Retail must be checked");
		assertTrue(isChecked(feeScheduleSearch.testBaseRadio(3)), "        Test base test must be checked");

		feeScheduleSearch.clickCloseBtn();
		switchToParentWin(parent);
		testCode.clickCancelFeeScheduleBtn();
		testCode.clickResetBtn();
	}
	
	@Test(priority = 1, description ="Single Test Detail - Fee Schedule - Verify close Fee Sched")
	public void testXPR_174() throws Exception {
		logger.info("===== Testing - testXPR_174 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		feeScheduleSearch = new FeeScheduleSearch(driver);

		logger.info("*** Step 1 Actions: Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: Enter a valid Test ID and Tab out");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test Id input field should show");
		String singleTestID ="DDJ019";
		testCode.checkInputTestCodeId(singleTestID);

		logger.info("*** Step 2 Expected Results: Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: Click Fee Schedule tab");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5),"        Fee Schedule tab must be available");
		testCode.clickfeeScheduleTab();

		logger.info("*** Step 3 Expected Results: Fee Schedule is displayed");
		assertTrue(isElementPresent(testCode.feeSchTable(), 5),"        Fee Schedule table must be available");
		
		logger.info("*** Step 4 Action: Click on Add button");
		assertTrue(isElementPresent(testCode.addNewFSBtn(), 5),"        Add New Fee Schedule button must be available");
		clickHiddenPageObject(testCode.addNewFSBtn(), 0);
		
		logger.info("*** Step 4 Expected Results: System will display a popup with main information");
		assertTrue(isElementPresent(testCode.addFSPopup(), 5),"        Add New Fee Schedule popup must be available");
		assertTrue(isElementPresent(testCode.effDtAddNewFS(), 5),"        Effective date must be available");		
		
		logger.info("*** Step 5 Action: Click Search Fee Sched ID icon");
		assertTrue(isElementPresent(feeScheduleSearch.searchFSIcon(), 5),"        Add New Fee Schedule button must be available");
		feeScheduleSearch.clickSearchFSIcon();
		String parent = switchToPopupWin();
		logger.info("*** Step 5 Expected Results: System will display Fee Search popup with main information");
		assertTrue(isElementPresent(feeScheduleSearch.fsIDInput(), 5),"         Fee Schedule ID input field must be available");
		assertTrue(isElementPresent(feeScheduleSearch.fsNameInput(), 5),"        Name input field must be available");
		assertTrue(isElementPresent(feeScheduleSearch.clientInput(), 5),"        Client input field must be available");
		assertTrue(isElementPresent(feeScheduleSearch.payorInput(), 5),"        Payor input field must be available");
		
		logger.info("*** Step 6 Action: Enter to all field and click close button");
		feeScheduleSearch.inputFSID("AutomationTest");
		feeScheduleSearch.inputFSName("AutomationTest");
		feeScheduleSearch.inputClient("AutomationTest");
		feeScheduleSearch.inputPayor("AutomationTest");
		assertTrue(isElementPresent(feeScheduleSearch.closeBtn(), 5),"        Close button must be available");
		feeScheduleSearch.clickCloseBtn();
		switchToParentWin(parent);
		
		logger.info("*** Step 6 Expected Results: System will Close Fee Search screen");
		assertTrue(isElementPresent(testCode.addFSPopup(), 5),"        Add New Fee Schedule popup must be available");
		assertTrue(isElementPresent(testCode.effDtAddNewFS(), 5),"        Effective date must be available");
		
		testCode.clickCancelFeeScheduleBtn();
		testCode.clickResetBtn();
	}
	
	@Test(priority = 1, description ="Single Test Detail - Fee Schedule - Check on 'Keep search open' checkbox")
	public void testXPR_175() throws Exception {
		logger.info("===== Testing - testXPR_175 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		feeScheduleSearch = new FeeScheduleSearch(driver);
		feeScheduleSearchResults = new FeeScheduleSearchResults(driver);

		logger.info("*** Step 1 Actions: Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: Enter a valid Test ID and Tab out");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test Id input field should show");
		String singleTestID = "HND504";
		testCode.checkInputTestCodeId(singleTestID);

		logger.info("*** Step 2 Expected Results: Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: Click Fee Schedule tab");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5),"        Fee Schedule tab must be available");
		testCode.clickfeeScheduleTab();

		logger.info("*** Step 3 Expected Results: Fee Schedule is displayed");
		assertTrue(isElementPresent(testCode.feeSchTable(), 5),"        Fee Schedule table must be available");
		
		logger.info("*** Step 4 Action: Click on Add button and Click Fee Sched search icon");
		assertTrue(isElementPresent(testCode.addNewFSBtn(), 5),"        Add New Fee Schedule button must be available");
		clickHiddenPageObject(testCode.addNewFSBtn(), 0);
		assertTrue(isElementPresent(feeScheduleSearch.searchFSIcon(), 5),"        Add New Fee Schedule popup must be available");
		feeScheduleSearch.clickSearchFSIcon();
		String parent1 = switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: System will display Fee Search popup with main information");
		assertTrue(isElementPresent(feeScheduleSearch.fsIDInput(), 5),"         Fee Schedule ID input field must be available");
		assertTrue(isElementPresent(feeScheduleSearch.fsNameInput(), 5),"        Name input field must be available");
		assertTrue(isElementPresent(feeScheduleSearch.clientInput(), 5),"        Client input field must be available");
		assertTrue(isElementPresent(feeScheduleSearch.payorInput(), 5),"        Payor input field must be available");
		
		logger.info("*** Step 5 Action: Click on Search button");
		assertTrue(isElementPresent(feeScheduleSearch.searchBtn(), 5),"        Search button must be available");
		feeScheduleSearch.clickSearchBtn();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: System will display Fee Search Result");
		assertTrue(feeScheduleSearchResults.searchResultTitle().getText().equalsIgnoreCase("Fee Schedule Search Results"),"        Fee Schedule Search Results must be available");
		
		logger.info("*** Step 6 Action: Check Keep Search Open and click on any FS ID in the list");
		assertTrue(isElementPresent(feeScheduleSearchResults.keepSearchOpenCheckBox(), 5),"        Keep Search Open check box must be available");
		selectCheckBox(feeScheduleSearchResults.keepSearchOpenCheckBox());
		String schedIdActual = feeScheduleSearchResults.feeScheduleTblFSID(2,2).getText();
		clickHiddenPageObject(feeScheduleSearchResults.feeScheduleTblFSID(2,2), 0);

		logger.info("*** Step 6 Expected Results: Search result page is kept, selected ID will be populate into the add record page");
		feeScheduleSearchResults.clickCloseSearchBtn();
		switchToParentWin(parent1);
		String SchedIDExpected = testCode.feeSchIdInput().getAttribute("value");
		assertEquals(schedIdActual, SchedIDExpected);
		
		testCode.clickCancelFeeScheduleBtn();	
		testCode.clickResetBtn();
	}
	
	@Test(priority = 1, description ="Single Test Detail - Fee Schedule - Verify footer buttons")
	public void testXPR_176() throws Exception {
		logger.info("===== Testing - testXPR_176 =====");
		navigation = new MenuNavigation(driver, config);
		testCode = new TestCode(driver, config, wait);
		feeScheduleSearch = new FeeScheduleSearch(driver);
		feeScheduleSearchResults = new FeeScheduleSearchResults(driver);

		logger.info("*** Step 1 Actions: Log into RPM with SSO username and password");
		navigation.navigateToTestCodePage();

		logger.info("*** Step 1 Expected Results: The Test Code page title displays");
		assertTrue(isElementPresent(testCode.testCodePageTitle(), 5),"        The Test Code page title should be displayed");
		assertTrue(testCode.testCodePageTitle().getText().trim().contains("Test Code"),"        Page Title should be 'Test Code'");

		logger.info("*** Step 2 Action: Enter a valid Test ID and Tab out");
		assertTrue(isElementPresent(testCode.testIdInput(), 5),"        Test Id input field should show");
		String singleTestID = "EEU297";
		testCode.checkInputTestCodeId(singleTestID);

		logger.info("*** Step 2 Expected Results: Single test page appear");
		assertTrue(isElementPresent(testCode.testNameInput(), 5), "        Test name input must be available");
		assertTrue(isElementPresent(testCode.labelTest(),5),"        Test label must be available.");
		assertTrue(testCode.labelTest().getText().equalsIgnoreCase("(Single Test)"),"        Test Label should be Single Test.");

		logger.info("*** Step 3 Action: Click Fee Schedule tab");
		assertTrue(isElementPresent(testCode.feeScheduleTab(), 5),"        Fee Schedule tab must be available");
		testCode.clickfeeScheduleTab();

		logger.info("*** Step 3 Expected Results: Fee Schedule is displayed");
		assertTrue(isElementPresent(testCode.feeSchTable(), 5),"        Fee Schedule table must be available");
		
		logger.info("*** Step 4 Action: Click on Add button and Click Fee Sched search icon");
		assertTrue(isElementPresent(testCode.addNewFSBtn(), 5),"        Add New Fee Schedule button must be available");
		clickHiddenPageObject(testCode.addNewFSBtn(), 0);
		assertTrue(isElementPresent(feeScheduleSearch.searchFSIcon(), 5),"        Add New Fee Schedule popup must be available");
		feeScheduleSearch.clickSearchFSIcon();
		String parent1 = switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: System will display Fee Search popup with main information");
		assertTrue(isElementPresent(feeScheduleSearch.fsIDInput(), 5),"         Fee Schedule ID input field must be available");
		assertTrue(isElementPresent(feeScheduleSearch.fsNameInput(), 5),"        Name input field must be available");
		assertTrue(isElementPresent(feeScheduleSearch.clientInput(), 5),"        Client input field must be available");
		assertTrue(isElementPresent(feeScheduleSearch.payorInput(), 5),"        Payor input field must be available");
		
		logger.info("*** Step 5 Action: Click on Search button");
		assertTrue(isElementPresent(feeScheduleSearch.searchBtn(), 5),"        Search button must be available");
		feeScheduleSearch.clickSearchBtn();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: System will display Fee Search Result and Next page , Preview page , last page , first page button");
		assertTrue(feeScheduleSearchResults.searchResultTitle().getText().equalsIgnoreCase("Fee Schedule Search Results"),"        Fee Schedule Search Results must be available");
		assertTrue(isElementPresent(feeScheduleSearchResults.firstPage(), 5),"        firstPage button must be available");
		assertTrue(isElementPresent(feeScheduleSearchResults.nextPage(), 5),"        nextPage button must be available");
		assertTrue(isElementPresent(feeScheduleSearchResults.prevPage(), 5),"        prevPage button must be available");
		assertTrue(isElementPresent(feeScheduleSearchResults.lastPage(), 5),"        lastPage button must be available");
		
		feeScheduleSearchResults.clickCloseSearchBtn();
		switchToParentWin(parent1);
		testCode.clickCancelFeeScheduleBtn();
		testCode.clickResetBtn();		
	}
}
