package com.newXp.tests;


import com.mbasys.mars.ejb.entity.prc.Prc;
import com.overall.fileMaintenance.pricing.incrementalPricing.IncrementalPricing;
import com.overall.menu.MenuNavigation;
import com.overall.search.IncrementalPricingSearch;
import com.overall.search.IncrementalPricingSearchResults;
import com.overall.utils.IncrementalPricingUtils;
import com.overall.utils.TestCodeUtils;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;



public class SmokeIncrementalPricingTest extends SeleniumBaseTest {

	private IncrementalPricing incrementalPricing;
	private TestCodeUtils testCodeUtils;
	private RandomCharacter randomCharacter;
	private MenuNavigation navigation;
	private IncrementalPricingUtils incrementalPricingUtils;
	private IncrementalPricingSearchResults incrementalPricingSearchResults;
	private IncrementalPricingSearch incrementalPricingSearch;

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
	//Load Incremental Price Table
	@Test(priority = 1, description = "Load IPT - Enter valid Pricing Id")
	public void testXPR_410() throws Exception {
		logger.info("===== Testing - testXPR_410 =====");          

		logger.info("*** Step 1 Actions: - Log to SSO by valid username and password");		
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Result: - The Load Incremental Price Table table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 10), "        The Incremental page should be displayed");
		assertEquals(incrementalPricing.inPriTitle().getText(),"Incremental Price Table", "        The Incremental Price Table page should be displayed");

		logger.info("*** Step 2 Actions: - Enter Valid Pricing ID");
		Prc listIncPricing = prcDao.getListOfIncrPricing();
		String pricingID = listIncPricing.getPrcAbbrev();
		String pricingName = listIncPricing.getDescr();
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 10), "        Pricing Id Input field should be displayed");
		incrementalPricing.checkInputPricingId(pricingID,10);

		logger.info("*** Step 2 Expected Result: - The Incremental Price Table page is displayed with correct page title - The Add New Rule, Reset and Submit button - Rule list");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleBtn(), 10), "        Add New Rule button should be displayed");
		assertTrue(isElementPresent(incrementalPricing.resetIncrPricingBtn(), 10), "        Reset button should be displayed");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 10), "        Submit button should be displayed");
		assertTrue(isElementPresent(incrementalPricing.tableRuleQuantity(0), 10), "        Quantity data should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 10), "        Pricing Name input should be displayed");
		assertEquals(incrementalPricing.pricingName().getAttribute("value"), pricingName, "        The pricing name should be equal to condition");

		assertTrue(isElementPresent(incrementalPricing.resetIncrPricingBtn(), 5),"        The Incremental Price Table Reset button is shown");
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
		}

	@Test(priority = 1, description = "Load IPT - Enter Invalid Pricing Id")
	public void testXPR_411() throws Exception {
		logger.info("===== Testing - testXPR_411 =====");          

		logger.info("*** Step 1 Actions: - Log to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Result: - The Load Incremental Price Table table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);		
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 10), "        The Incremental page should be displayed");
		assertEquals(incrementalPricing.inPriTitle().getText(),"Incremental Price Table", "        The Incremental Price Table page should be displayed");

		logger.info("*** Step 2 Actions: - Enter Invalid Pricing ID");		
		String invalidPricingID = String.valueOf(System.currentTimeMillis());	
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 10), "        Pricing Id Input field should be displayed");
		incrementalPricing.inputInPriID(invalidPricingID);

		logger.info("*** Step 2 Expected Result: - The Incremental Price Table page is displayed with empty information - Rule 1 is displayed without any information");
		assertTrue(isElementPresent(incrementalPricing.tableRuleQuantity(0), 10), "        Quantity data should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 10), "        Pricing Name input should be displayed");		
		assertEquals(incrementalPricing.pricingName().getAttribute("value"), "", "        The pricing name should be equal to condition");

		assertTrue(isElementPresent(incrementalPricing.resetIncrPricingBtn(), 5),"        The Incremental Price Table Reset button is shown");
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
		}

	@Test(priority = 1, description = "Load IPT - Verify Help icon")
	public void testXPR_412() throws Exception {
		logger.info("===== Testing - testXPR_412 =====");          

		logger.info("*** Step 1 Actions: - Log to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Result: - The Load Incremental Price Table table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 10), "        The Incremental page should be displayed");
		assertEquals(incrementalPricing.inPriTitle().getText(),"Incremental Price Table", "        The Incremental Price Table page should be displayed");

		logger.info("*** Step 2 Actions: - Click on Help icon at Load Incremental Price Table table page");		
		incrementalPricing.clickInPriHelpIconBtn();		

		logger.info("*** Step 2 Expected Result: - The Help page is displayed");
		switchToPopupWin();
		assertTrue(driver.getCurrentUrl().contains("help"), "        The help page should be displayed");
	}

	//Search Pricing ID
	@Test(priority = 1, description ="Search PricingID - Search with valid data")
	public void testXPR_414() throws Exception {
		logger.info("===== Testing - testXPR_414 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: - The Load Incremental Price Table table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		incrementalPricingSearch = new IncrementalPricingSearch(driver);
		incrementalPricingSearchResults = new IncrementalPricingSearchResults(driver, config);
		assertTrue(isElementPresent(incrementalPricing.inPriLoadTableTitle(), 5),"        The Load Incremental Price Table table page is shown");
		assertEquals(incrementalPricing.inPriLoadTableTitle().getText(), "Load Incremental Price Table","        The Load Incremental Price Table table page is displayed with correct page title");

		logger.info("*** Step 2 Actions: - Click on Incremental Price Table Search icon");
		assertTrue(isElementPresent(incrementalPricing.inPriSearchIconBtn(), 5),"        The Incremental Price Table Search icon button is shown");
		incrementalPricing.clickInPriSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - The Incremental Price Table ID search page is displayed with 2 fields - Pricing ID anf Pricing Name");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchPriID(), 5),"        The Incremental Price Table Search Pricing ID field is shown");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchPriName(), 5),"        The Incremental Price Table Search Pricing Name field is shown");

		logger.info("*** Step 3 Actions: - Enter a valid data for Pricing ID and Pricing Name then click on Search button");			
		Prc inPriInfo = prcDao.getListOfIncrPricing();
		String pricingID = inPriInfo.getPrcAbbrev();
		String pricingName = inPriInfo.getDescr();
		incrementalPricingSearch.inputInPriSearchPriID(pricingID);
		incrementalPricingSearch.inputInPriSearchPriName(pricingName);
		incrementalPricingSearch.clickInPriSearchSearchBtn();
		switchToPopupWin();

		logger.info("*** Step 3 Expected Results: - The Pricing is match with search condition will be displayed in Search result page");
		assertTrue(isElementPresent(incrementalPricingSearchResults.inPriSearchResultTable(), 5),"        The Incremental Price Table Search Result table is shown");
		assertTrue(isElementPresent(incrementalPricingSearchResults.inPriSearchResultPriIDColumn(2, 2), 5),"        The Incremental Price Table Search Result table - Pricing ID column is shown");
		assertEquals(incrementalPricingSearchResults.inPriSearchResultPriIDColumn(2, 2).getText(), pricingID,"        The Pricing ID is match with search condition");
		assertTrue(isElementPresent(incrementalPricingSearchResults.inPriSearchResultPriTableColumn(2, 3), 5),"        The Incremental Price Table Search Result table - Pricing Name column is shown");
		assertEquals(incrementalPricingSearchResults.inPriSearchResultPriTableColumn(2, 3).getText(), pricingName,"        The Pricing Name is match with search condition");

		assertTrue(isElementPresent(incrementalPricingSearchResults.inPriSearchResultCloseBtn(), 5),"        The Incremental Price Table Search Result Close button is shown");
		incrementalPricingSearchResults.clickInPriSearchCloseBtn();				
	}

	
	@Test(priority = 2, description ="Search PricingID - Verify Keep Search Open works")
	public void testXPR_417() throws Exception {
		logger.info("===== Testing - testXPR_417 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: - The Load Incremental Price Table table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		incrementalPricingSearch = new IncrementalPricingSearch(driver);
		incrementalPricingSearchResults = new IncrementalPricingSearchResults(driver, config);
		assertTrue(isElementPresent(incrementalPricing.inPriLoadTableTitle(), 5),"        The Load Incremental Price Table table page is shown");
		assertEquals(incrementalPricing.inPriLoadTableTitle().getText(), "Load Incremental Price Table","        The Load Incremental Price Table table page is displayed with correct page title");

		logger.info("*** Step 2 Actions: - Click on Incremental Price Table Search icon");
		assertTrue(isElementPresent(incrementalPricing.inPriSearchIconBtn(), 5),"        The Incremental Price Table Search icon button is shown");
		incrementalPricing.clickInPriSearchIconBtn();
		String parent = switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - The Incremental Price Table ID search page is displayed");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchTitle(), 5),"        The Incremental Price Table Search title is shown");
		assertEquals(incrementalPricingSearch.inPriSearchTitle().getText(), "Incremental Pricing Search","        The Incremental Price Table Search title is displayed");

		logger.info("*** Step 3 Actions: - Enter * at Pricing ID field and click on Search button");			
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchPriID(), 5),"        The Incremental Price Table Search Pricing ID field is shown");
		incrementalPricingSearch.inputInPriSearchPriID("*");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchSearchBtn(), 5),"        The Incremental Price Table Search Search button is shown");
		incrementalPricingSearch.clickInPriSearchSearchBtn();
		String winHandler = driver.getWindowHandle();

		logger.info("*** Step 3 Expected Results: - All Pricing Ids are displayed");
		assertTrue(isElementPresent(incrementalPricingSearchResults.inPriSearchResultTotalResult(), 5),"        The Incremental Price Table Search Result Total Results is shown");

		logger.info("*** Step 4 Actions: - Check on Keep Search Open checkbox and click on any Pricing ID link");
		assertTrue(isElementPresent(incrementalPricingSearchResults.inPriSearchResultKeepSearchOpenCheckbox(), 5),"        The Incremental Price Table Search Results Keep Search Open checkbox is shown");
		incrementalPricingSearchResults.clickInPriSearchKeepSearchOpenChechbox();
		assertTrue(isElementPresent(incrementalPricingSearchResults.inPriSearchResultPriIDColumn(6, 2), 5),"        The Incremental Price Table Search Results Pricing ID is shown");
		String priID = incrementalPricingSearchResults.inPriSearchResultPriIDColumn(6, 2).getText();
		incrementalPricingSearchResults.clickInPriSearchPriIDColumn(6, 2);

		logger.info("*** Step 4 Expected Results: - The Incremental Price Table Search is still kept open and The Incremental Price Table is displayed correct information with selected Pricing ID");
		switchToParentWin(parent);
		assertEquals(incrementalPricing.pricingIdLbl().getText(),priID,"        The Incremental Price Table ID must be match with selected Pricing ID");
		switchToWin(winHandler);
		assertTrue(isElementPresent(incrementalPricingSearchResults.inPriSearchResultTitle(), 5),"        The Incremental Price Table Search Results title is shown");
		switchToParentWin(parent);
		
		incrementalPricing.clickResetIncrPricingBtn();		
	}

	//Add Incremental Price
	
	@Test(priority=1, description="Add new Incremental Price with valid data")
	public void testXPR_422() throws Exception{
		
		logger.info("====== Testing - XPR_422 ======");
		
		logger.info("*** Step 1 Action: login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected result: The Load Incremental Price Table page is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Price page is displayed");
		
		logger.info("*** Step 2 Action: Enter a new Pricing ID");
		randomCharacter = new RandomCharacter(driver);
		String pricingID = "AUTOTEST" + randomCharacter.getRandomNumericString(6);
		incrementalPricing.checkInputPricingId(pricingID,10);
		
		logger.info("*** Step 2 Expected result: The Incremental page is displayed without information");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        The Incremental Price page is displayed");
		assertTrue(isElementPresent(incrementalPricing.ruleSection(0), 5),"        Rule 1 is displayed");
		assertTrue(incrementalPricing.pricingName().getText().trim().contains(""),"        The Incremental Price Page is displayed without information");
		
		logger.info("*** step 3 Actions: Input Pricing Name, and click on Add Quantity icon");
		String pricingName = "Pricing" + randomCharacter.getRandomAlphaString(8);
		int rule = 0;
		incrementalPricing.inputPricingName(pricingName);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblLeftBtn(rule), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleTblLeftBtn(rule), 0);
		
		logger.info("*** Step 3 Expected result: Add record popup is displayed");
		assertTrue(isElementPresent(incrementalPricing.addNewRulePopup(rule), 5),"      The Add Quantity popup is displayed");
		
		logger.info("*** Step 4 Actions: Input data for all fields and click on OK button");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		int quantity = incrementalPricingUtils.getNonZeroRandomNumber(1);
		int quantityTo = incrementalPricingUtils.getNonZeroRandomNumber(2);
		int price = incrementalPricingUtils.getNonZeroRandomNumber(2);
		assertTrue(isElementPresent(incrementalPricing.quantityFromInput(), 5),"        Quantity input should be displayed");
		incrementalPricing.inputQuantityFrom(Integer.toString(quantity));
		assertTrue(isElementPresent(incrementalPricing.quantityToInput(), 5),"        Quantity to input should be displayed");
		incrementalPricing.inputQuantityTo(Integer.toString(quantityTo));
		assertTrue(isElementPresent(incrementalPricing.price(), 5),"        Price input should be displayed");
		incrementalPricing.inputPrice(Integer.toString(price));
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Ok button should show.");
		incrementalPricing.clickOkButton();
		
		logger.info("*** Step 4 Expected result: new quantity is added into the table");
		assertTrue(getColumnValue(incrementalPricing.quantityTbl(rule), Integer.toString(quantityTo)), "        New Quantity is added into the table");
		
		logger.info("*** Step 5 Action: Click on Add icon at Inclusion&Exclusion table");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(rule), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(rule), 0);
		
		logger.info("*** Step 5 Expected result: Add record popup is displayed");
		assertTrue(isElementPresent(incrementalPricing.addNewInclusionExclusionPopup(rule), 5),"        Add Record popup is displayed");
	
		logger.info("*** step 6 Actions: input valid data for new Include");
		String include = incrementalPricing.inputIncludeRule(this, testDb);
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5));
		incrementalPricing.clickOkButton();
		
		logger.info("*** Step 6 expected result: New Included is added into the table");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		assertTrue(getColumnValue(incrementalPricing.inclusionTbl(rule), include),"        New Included is added into the table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(rule), "Include"), "        'Include' should display in the table");
		
		logger.info("*** Step 7 Action: click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5));
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);
		
		logger.info("*** Step 7 Expected result: new Pricing is saved");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        System take user return the Load Incremental Price Table page");
		incrementalPricing.checkInputPricingId(pricingID,10);
		assertTrue(incrementalPricing.pricingIdLbl().getText().equalsIgnoreCase(pricingID),"        The Incremental Price Table is displayed with correct PricingID");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(rule), "Include"), "        'Include' should display in the table");
		
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
		}
	
	@Test(priority=1, description="Add new Incremental with more than 10 rules")
	public void testXPR_426() throws Exception{
		logger.info("====== Testing - XPR_426 ======");
		
		logger.info("*** step 1 Actions: Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected result: The Load Incremental Price table page is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Price Table is displayed");
		
		logger.info("*** Step 2 Action: Input new Pricing ID and tab out");
		randomCharacter = new RandomCharacter(driver);
		String pricingID = "AUTOTEST" + randomCharacter.getRandomNumericString(6);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5));
		incrementalPricing.checkInputPricingId(pricingID,10);
		
		logger.info("*** Step 2 Expected result: The Incremental Price Table page is displayed without information");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        The Incremental Price Table page is displayed");
		assertTrue(incrementalPricing.pricingName().getText().trim().contains(""),"        The Incremental Price Page is displayed without information");
		
		logger.info("*** Step 3 Actions: Add 10 rules for new Pricing Id");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		String pricingName = "Name" + randomCharacter.getRandomAlphaString(5);
		incrementalPricing.inputPricingName(pricingName);
		// add rule 1
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Click on Add icon at Rule 1");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		List<String> testId1 = daoManagerXifinRpm.getSingleTest(testDb);
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test Id input is displayed");
		incrementalPricing.inputTestId(testId1.get(1));
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5));
		incrementalPricing.clickOkButton();
		
		// Add rule 2
		assertTrue(isElementPresent(incrementalPricing.addNewRuleBtn(), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleBtn(), 0);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(1), 5),"        Verify Rule 2 section is displayed");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(1), 0);
		List<String> testId2 = daoManagerXifinRpm.getSingleTest(testDb);
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test Id input is displayed");
		incrementalPricing.inputTestId(testId2.get(1));
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5));
		incrementalPricing.clickOkButton();
		
		// Add rule 3
		assertTrue(isElementPresent(incrementalPricing.addNewRuleBtn(), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleBtn(), 0);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(2), 5),"       Verify Rule 3 section is displayed");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(2), 0);
		List<String> testId3 = daoManagerXifinRpm.getSingleTest(testDb);
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test Id input is displayed");
		incrementalPricing.inputTestId(testId3.get(1));
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5));
		incrementalPricing.clickOkButton();
		
		//Add rule 4
		assertTrue(isElementPresent(incrementalPricing.addNewRuleBtn(), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleBtn(), 0);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(3), 5),"        Verify Rule 4 section is displayed");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(3), 0);
		List<String> testId4 = daoManagerXifinRpm.getSingleTest(testDb);
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test Id input is displayed");
		incrementalPricing.inputTestId(testId4.get(1));
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5));
		incrementalPricing.clickOkButton();
		
		// Add rule 5
		assertTrue(isElementPresent(incrementalPricing.addNewRuleBtn(), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleBtn(), 0);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(4), 5),"        Verify Rule 5 section is displayed");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(4), 0);
		List<String> testId5 = daoManagerXifinRpm.getSingleTest(testDb);
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test Id input is displayed");
		incrementalPricing.inputTestId(testId5.get(1));
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5));
		incrementalPricing.clickOkButton();
		
		//ADd rule 6
		assertTrue(isElementPresent(incrementalPricing.addNewRuleBtn(), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleBtn(), 0);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(5), 5),"        Verify that Rule 6 section is displayed");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(5), 0);
		List<String> testId6 = daoManagerXifinRpm.getSingleTest(testDb);
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test Id input is displayed");
		incrementalPricing.inputTestId(testId6.get(1));
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5));
		incrementalPricing.clickOkButton();
		
		//Add Rule 7
		assertTrue(isElementPresent(incrementalPricing.addNewRuleBtn(), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleBtn(), 0);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(6), 5),"        Verify that rule 7 section is displayed");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(6), 0);
		List<String> testId7 = daoManagerXifinRpm.getSingleTest(testDb);
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test Id input is displayed");
		incrementalPricing.inputTestId(testId7.get(1));
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5));
		incrementalPricing.clickOkButton();
		
		//Add rule 8
		assertTrue(isElementPresent(incrementalPricing.addNewRuleBtn(), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleBtn(), 0);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(7), 5),"        Verify that rule 8 section is displayed");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(7), 0);
		String testType8 = testDao.getTestTyp().getAbbrev();
		assertTrue(isElementPresent(incrementalPricing.testTypeDropdown(), 5),"        Verify that rule 9 section is displayed");
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.testTypeDropdown(), testType8);
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5));
		incrementalPricing.clickOkButton();
		
		//Add Rule 9
		assertTrue(isElementPresent(incrementalPricing.addNewRuleBtn(), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleBtn(), 0);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(8), 5),"        Verify that Rule 9 is displayed");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(8), 0);
		List<String> testId9 = daoManagerXifinRpm.getSingleTest(testDb);
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test Id input is displayed");
		incrementalPricing.inputTestId(testId9.get(1));
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5));
		incrementalPricing.clickOkButton();
		
		//Add rule 10
		assertTrue(isElementPresent(incrementalPricing.addNewRuleBtn(), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleBtn(), 0);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(9), 5),"        Verify that Rule 10 section is displayed");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(9), 0);
		List<String> testId10 = daoManagerXifinRpm.getSingleTest(testDb);
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test Id input is displayed");
		incrementalPricing.inputTestId(testId10.get(1));
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5));
		incrementalPricing.clickOkButton();
		
		logger.info("*** Step 3 Expected results: 10 rules are displayed with correct data");
		//rule 1
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), testId1.get(1)),"        Test ID: " + testId1.get(1) + " should be added into the table Rule1.");
		//Rule 2
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(1), testId2.get(1)),"        Test ID: " + testId2.get(1) + " should be added into the table Rule2.");
		//Rule 3
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(2), testId3.get(1)),"        Test ID: " + testId3.get(1) + " should be added into the table Rule3.");
		//Rule 4
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(3), testId4.get(1)),"        Test ID: " + testId4.get(1) + " should be added into the table Rule4.");
		//Rule 5
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(4), testId5.get(1)),"        Test ID: " + testId5.get(1) + " should be added into the table Rule5.");
		//Rule 6 
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(5), testId6.get(1)),"        Test ID: " + testId6.get(1) + " should be added into the table Rule6.");
		//Rule 7 
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(6), testId7.get(1)),"        Test ID: " + testId7.get(1) + " should be added into the table Rule7.");
		//Rule 8 
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(7), testType8),"        Test Type: " + testType8 + " should be added into the table Rule8.");
		//Rule 9 
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(8), testId9.get(1)),"        Test ID: " + testId9.get(1) + " should be added into the table Rule9.");
		//Rule 10 
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(9), testId10.get(1)),"        Test ID: " + testId10.get(1) + " should be added into the table Rule10.");
		
		logger.info("*** step 4 Action: Click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 10));
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);
		
		logger.info("*** Step 4 Expected result: New Pricing ID is added into the db");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        System should take user back to the Load Incremental Price page");
		incrementalPricing.checkInputPricingId(pricingID,10);
		assertTrue(isElementPresent(incrementalPricing.tableRuleTest(9), 5),"        10 rules should be displayed for this pricing ID: " + pricingID);
		
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
		}
	
	@Test(priority=1,description="FM - Pricing - Incremental Price Tbl - Add New incremental without any rule")
	public void testXPR_428() throws Exception{
		logger.info("====== Testing - XPR_428 ======");
		
		logger.info("*** Step 1 Action: login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** step 1 expected result: the Load incremental Price table is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Price table page is displayed");
		
		logger.info("*** step 2 Action: Enter a new Pricing ID");
		randomCharacter = new RandomCharacter(driver);
		String pricingId = "AUTOTEST" + randomCharacter.getRandomNumericString(6);
		incrementalPricing.checkInputPricingId(pricingId,10);
		
		logger.info("*** Step 2 Expected result: The Incremental Price table is displayed without information");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        The Incremental Price table page is displayed");
		assertTrue(incrementalPricing.pricingName().getText().trim().contains(""),"        The Incremental Price Page is displayed without information");
		
		logger.info("*** Step 3 Actions: Input Pricing Name and click on Delete Rule icon");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Pricing Name is displayed");
		incrementalPricing.inputPricingName(pricingId);
		assertTrue(isElementPresent(incrementalPricing.deleteRuleLink(0), 5),"        Delete rule link is displayed");
		clickHiddenPageObject(incrementalPricing.deleteRuleLink(0), 0);
		assertTrue(isElementPresent(incrementalPricing.warningMessage(), 5),"        The warning message is displayed");
		
		logger.info("*** Step 3 Expected result: Warning popup for delete rule is displayed");
		assertTrue(isElementPresent(incrementalPricing.deleteARuleDialog(), 5),"        Delete a rule dialog is displayed");
		
		logger.info("*** step 4 Action: click on OK button");
		clickHiddenPageObject(incrementalPricing.okWarningMessage(), 0);
		
		logger.info("*** step 4 Expected result: the selected rule is not displayed ");
		assertNull(incrementalPricing.verifyEmptyData(0),"        The selected rule is deleted");
		
		logger.info("*** Step 5 Action: click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5));
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);
		
		logger.info("*** Step 5 Expected result: new Pricing ID is save without rule");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        System take user return Load Incremental Price page");
		incrementalPricing.checkInputPricingId(pricingId,10);
		assertNull(incrementalPricing.verifyEmptyData(0),"        The selected rule is deleted");
		
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
		}
	
	@Test(priority = 1, description ="Update Incremental Price Table with valid data")
	public void testXPR_429() throws Exception {
		logger.info("===== Testing - testXPR_429 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: - The Load Incremental Price Table table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		incrementalPricingSearch = new IncrementalPricingSearch(driver);
		incrementalPricingSearchResults = new IncrementalPricingSearchResults(driver, config);
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		assertTrue(isElementPresent(incrementalPricing.inPriLoadTableTitle(), 5),"        The Load Incremental Price Table table page is shown");
		assertEquals(incrementalPricing.inPriLoadTableTitle().getText(), "Load Incremental Price Table","        The Load Incremental Price Table table page is displayed with correct page title");

		logger.info("*** Step 2 Actions: - Input new Pricing ID and tab out");
		randomCharacter = new RandomCharacter();
		String newPriID = "AUTOTEST" + randomCharacter.getRandomNumericString(6);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Price Table Pricing ID field is shown");			
		incrementalPricing.checkInputPricingId(newPriID,10);

		logger.info("*** Step 2 Expected Results: - The Incremental Price Table ID search page is displayed");

		logger.info("incrementalPricing.inPricingRuleTitle() = "+incrementalPricing.inPricingRuleTitle());
		assertTrue(isElementPresent(incrementalPricing.inPricingRuleTitle(), 5),"        The Incremental Price Table title is shown");

		logger.info("*** Step 3 Actions: - Enter Pricing Name and Create rule with valid data and click on Submit button");			
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        The Incremental Price Table Pricing Name field is shown");			
		incrementalPricing.inputPricingName(newPriID+" 'S EXAMPLE");			
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblLeftBtn(0), 5),"        The Incremental Price Table Add New Rule icon is shown");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblLeftBtn(0), 0);
		assertTrue(isElementPresent(incrementalPricing.quantityFromInput(), 5),"        The Incremental Price Table Add New Rule Quality From field is shown");
		incrementalPricing.inputQuantityFrom(""+incrementalPricingUtils.getNonZeroRandomNumber(1));
		assertTrue(isElementPresent(incrementalPricing.quantityToInput(), 5),"        The Incremental Price Table Add New Rule Quality To field is shown");
		incrementalPricing.inputQuantityTo(""+incrementalPricingUtils.getNonZeroRandomNumber(2));
		assertTrue(isElementPresent(incrementalPricing.price(), 5),"        The Incremental Price Table Add New Rule Price field is shown");
		incrementalPricing.inputPrice(""+incrementalPricingUtils.getNonZeroRandomNumber(1));
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        The Incremental Price Table Add New Rule OK button is shown");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        The Incremental Price Table Add New Inclusion and Exclusion icon is shown");			
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);

		List<String> testCode = incrementalPricingUtils.getTestHaveTestType(daoManagerXifinRpm);
		String testID = testCode.get(0);
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        The Incremental Price Table Test ID field is shown");
		incrementalPricing.inputTestId(testID);
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        The Incremental Price Table OK button is shown");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        The Incremental Price Table Submit button is shown");
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);

		logger.info("*** Step 3 Expected Results: - The new Incremental Price Table ID is saved into db");

		Prc priInfo = prcDao.getPrcFromPrcIncrRuleByPrcAbbrev(newPriID);
		String dbPriID = priInfo.getPrcAbbrev();
		assertEquals(newPriID, dbPriID,"        The new Incremental Price Table ID is saved into db");

		logger.info("*** Step 4 Actions: - Input Pricing ID has just created and tab out");
		incrementalPricing.checkInputPricingId(newPriID,10);

		logger.info("*** Step 4 Expected Results: - The Incremental Price Table page is displayed with correct information");
		assertTrue(isElementPresent(incrementalPricing.inPricingRuleTitle(), 5),"        The Incremental Price Table title is shown");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        The Incremental Price Table Pricing ID label is shown");
		assertEquals(incrementalPricing.pricingIdLbl().getText(),newPriID,"        The Incremental Price Table Pricing ID label is match with new Pricing ID");

		logger.info("*** Step 5 Actions: - Update Pricing Name and click on Add New Rule button");
		incrementalPricing.incrementalPricingName().clear();
		incrementalPricing.inputPricingName(newPriID+" 'S NEW "+randomCharacter.getRandomAlphaNumericString(3));
		String newPriName = incrementalPricing.pricingName().getAttribute("value");
		clickHiddenPageObject(incrementalPricing.addNewRuleBtn(), 0);

		logger.info("*** Step 5 Expected Results: - Rule2 is displayed without information");
		assertTrue(isElementPresent(incrementalPricing.ruleHeaderLeftTop("1"), 5),"        The Incremental Price Table Rule2 header is shown");
		assertEquals(incrementalPricing.ruleHeaderLeftTop("1").getText(),"Rule 2","        The Incremental Price Table Rule2 header is displayed correctly");

		logger.info("*** Step 6 Actions: - Click on Add icon at Inclusion and Exclusion table and add Exclusion with valid data");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(1), 0);	
		testCode = incrementalPricingUtils.getTestHaveTestType(daoManagerXifinRpm);
		testID = testCode.get(0);
		incrementalPricing.inputTestId(testID);
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 7 Actions: - Click on Submit button");
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);

		logger.info("*** Step 7 Expected Results: - The Pricing Name and New Rule will be saved into database with corresponding Pricing ID");
		incrementalPricing.checkInputPricingId(newPriID,10);
		String dbPriName = incrementalPricing.incrementalPricingName().getAttribute("value");
		assertEquals(newPriName, dbPriName,"        The new Incremental New Pricing Name is saved into db");

		assertTrue(isElementPresent(incrementalPricing.resetIncrPricingBtn(), 5),"        The Incremental Price Table Reset button is shown");
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
		}

	//Rule
	
	@Test(priority = 1, description = "Add Test Rule - Add duplicate Test ID")
	public void testXPR_435() throws Exception {
		logger.info("===== Testing - testXPR_435 =====");
		
		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: - The Incremental Price Table page title displays");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price Table page title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Price Table'");
		
		logger.info("*** Step 2 Actions: - Enter a valid Incremental Price Table ID. Tab out");
		randomCharacter = new RandomCharacter();
		String incPricingID = "AUTOTEST" + randomCharacter.getRandomNumericString(6);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Incremental Price Table ID input should be displayed");
		incrementalPricing.checkInputPricingId(incPricingID,10);
		
		logger.info("*** Step 2 Expected Result: - The Incremental Price Table page displays");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Incremental Price Table ID should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Incremental Price Table Name should be displayed");
		
		logger.info("*** Step 3 Actions: - At Inclusion & Exclusion table : Click on Add icon , input valid Test ID and click OK button");
		incrementalPricing.inputPricingName(incPricingID);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        add new Inclusion & Exclusion button should show.");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		assertTrue(isElementPresent(incrementalPricing.addNewInclusionExclusionPopup(0), 5),"        add new Inclusion & Exclusion popup should show.");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		
		String listTestId = incrementalPricingUtils.getTestHaveTestType(daoManagerXifinRpm).get(0);
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test Id input should show.");
		incrementalPricing.inputTestId(listTestId);
		
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Ok button should show.");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);
		
		logger.info("*** Step 3 Expected Result: - New Test Id is added to table include radio button is checked");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), listTestId), "        The Test Id " + listTestId + " should display in the table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), "Include"), "        'Include' should display in the table");
	
		logger.info("*** Step 4 Actions: - At Inclusion & Exclusion table : Click on Add icon again, input the same Test ID as step 3");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        add new Inclusion & Exclusion button should show.");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test Id input should show.");
		incrementalPricing.inputTestId(listTestId);
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Ok button should show.");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);
		
		logger.info("*** Step 4 Expected Result: - New Test Id is added to table include radio button is checked");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), listTestId), "        The Test Id " + listTestId + " should display in the table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), "Include"), "        'Include' should display in the table");
	
		logger.info("*** Step 5 Actions: Click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button should show.");
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);
		
		logger.info("*** Step 5 Expected Result: - Error message should show");
		assertTrue(incrementalPricing.errorMessageSection().getText().trim().contains("A Test can only be included in one rule per Incremental Table. Test "+listTestId+" is in Rule 1."),"        Error message should be 'A Test can only be included in one rule per Incremental Table. Test "+listTestId+" is in Rule 1.'");
	
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
		}
	
	@Test(priority = 1, description = "Add Test Rule - Delete Test ID")
	public void testXPR_440() throws Exception {
		logger.info("===== Testing - testXPR_440 =====");
		
		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: - The Incremental Price Table page title displays");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price Table page title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Price Table'");
		
		logger.info("*** Step 2 Actions: - Enter a valid Incremental Price Table ID. Tab out");
		randomCharacter = new RandomCharacter();
		String incPricingID = "AUTOTEST" + randomCharacter.getRandomNumericString(6);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Incremental Price Table ID input should be displayed");
		incrementalPricing.checkInputPricingId(incPricingID,10);
		
		logger.info("*** Step 2 Expected Result: - The Incremental Price Table page displays");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Incremental Price Table ID should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Incremental Price Table Name should be displayed");
		
		logger.info("*** Step 3 Actions: - At Inclusion & Exclusion table : Add 2 Inclusion & Exclusion id");
		incrementalPricing.inputPricingName(incPricingID);
		List<String> listData = incrementalPricing.addMultiInclusionExclusion(2,0,this,daoManagerXifinRpm);
		
		logger.info("*** Step 3 Expected Result: - New Test Id is added to table include radio button is checked");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), listData.get(0)), "        The Test Id " + listData.get(0) + " should display in the table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), "Include"), "        'Include' should display in the table");
	
		logger.info("*** Step 4 Actions: - Click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button should show.");
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);
		
		logger.info("*** Step 4 Expected Result: - New PricingID is save into the DB");
		Assert.assertNotNull(prcDao.getPrcByPrcAbbrev(listData.get(0).toUpperCase()));

		logger.info("*** Step 5 Actions: - Load Pricing has just created");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Incremental Price Table ID input should be displayed");
		incrementalPricing.checkInputPricingId(incPricingID,10);
		
		logger.info("*** Step 5 Expected Result: - The Incremental Price Table page displays");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Incremental Price Table ID should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Incremental Price Table Name should be displayed");
		
		logger.info("*** Step 6 Actions: - Select any row : Click on Edit icon : Check on delete checkbox : Click on Ok button");
		selectColumnValue(incrementalPricing.tableRuleTest(0), listData.get(0));
		assertTrue(isElementPresent(incrementalPricing.delCheckbox(), 5),"        Delete checkbox should show");
		selectCheckBox(incrementalPricing.delCheckbox());
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Ok button should show.");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);
		
		logger.info("*** Step 6 Expected Result: - The selected row is marked as delete row, The delete checkbox is checked");
		isChecked(incrementalPricing.delCheckboxTblRight(0));
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		incrementalPricingUtils.checkDeleteRowInTable(incrementalPricing.tableRuleTest(0),listData.get(0));
	
		logger.info("*** Step 7 Actions: - Click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button should show.");
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);
		
		logger.info("*** Step 7 Expected Result: - New PricingID is delete from the DB");
		incrementalPricing.checkInputPricingId(incPricingID,10);
		assertFalse(getColumnValue(incrementalPricing.tableRuleTest(0),listData.get(0)), "        New PricingId "+listData.get(0)+" is delete from the DB");
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
		}
	
	@Test(priority = 1, description = "Add Test Rule - Add quantity with valid data")
	public void testXPR_441() throws Exception {
		logger.info("===== Testing - testXPR_441 =====");
		
		logger.info("*** Step 1 Actions: - Log into Incremental Price Table with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: - The Incremental Price Table page title displays");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price Table page title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Price Table'");
		
		logger.info("*** Step 2 Actions: - Enter a valid Incremental Price Table ID. Tab out");
		randomCharacter = new RandomCharacter();
		String incPricingID = "AUTOTEST" + randomCharacter.getRandomNumericString(6);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Incremental Price Table ID input should be displayed");
		incrementalPricing.checkInputPricingId(incPricingID,10);
		
		logger.info("*** Step 2 Expected Result: - The Incremental Price Table page displays");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Incremental Price Table ID should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Incremental Price Table Name should be displayed");
		
		logger.info("*** Step 3 Actions: - At Rule and Quantity table : Click on Add icon");
		incrementalPricing.inputPricingName(incPricingID);
		clickHiddenPageObject(incrementalPricing.addNewRuleTblLeftBtn(0), 0);
		
		logger.info("*** Step 3 Expected Result: - Add new record popup is displayed");
		assertTrue(isElementPresent(incrementalPricing.addNewRulePopup(0), 5),"        New record popup should be displayed");
		
		logger.info("*** Step 4 Actions: - Enter valid data to Quantity From, Quantity To, Price and click Ok button");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		int quantityFrom = incrementalPricingUtils.getNonZeroRandomNumber(1);
		int quantityTo = incrementalPricingUtils.getNonZeroRandomNumber(2);
		int price = incrementalPricingUtils.getNonZeroRandomNumber(2);
		assertTrue(isElementPresent(incrementalPricing.quantityFromInput(), 5),"        Quantity From input should be displayed");
		incrementalPricing.inputQuantityFrom(Integer.toString(quantityFrom));
		assertTrue(isElementPresent(incrementalPricing.quantityToInput(), 5),"        Quantity To input should be displayed");
		incrementalPricing.inputQuantityTo(Integer.toString(quantityTo));
		assertTrue(isElementPresent(incrementalPricing.price(), 5),"        Price input should be displayed");
		incrementalPricing.inputPrice(Integer.toString(price));
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        OK button should show.");
		incrementalPricing.clickOkButton();
		
		logger.info("*** Step 4 Expected Result: - New price is added into rule table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleQuantity(0), Integer.toString(quantityFrom)), "        Quantity From: " + quantityFrom + " should display in the table");
		
		logger.info("*** Step 5 Actions: - At Inclusion & Exclusion table : Add Inclusion & Exclusion id");
		List<String> listData = incrementalPricing.addMultiInclusionExclusion(1,0,this, daoManagerXifinRpm);
		
		logger.info("*** Step 5 Expected Result: - New Test Id is added to table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), listData.get(0)), "        The Test Id " + listData.get(0) + " should display in the table");
	
		logger.info("*** Step 6 Actions: - Click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button should show.");
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);
		
		logger.info("*** Step 6 Expected Result: - New PricingID is save into the DB");
		incrementalPricing.checkInputPricingId(incPricingID,10);
		assertTrue(getColumnValue(incrementalPricing.tableRuleQuantity(0), Integer.toString(quantityFrom)), "        Quantity From: " + quantityFrom + " should display in the table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), listData.get(0)), "        The Test Id " + listData.get(0) + " should display in the table");
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
		}
	
	@Test(priority = 1, description = "Test Rule - Add rule with Test radio button is checked")
	public void testXPR_431() throws Exception {
		
		logger.info("===== Testing - XPR_431 =====");  
		
		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: - Incremental Price Table page title displays");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price Table page title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Price Table'");
		
		logger.info("*** Step 2 Actions: - Enter a new Pricing ID. Tab out");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing ID input should be displayed");
		randomCharacter = new RandomCharacter(driver);
		String pricingID = "AUTOTEST" + randomCharacter.getRandomNumericString(6);
		incrementalPricing.checkInputPricingId(pricingID,10);
		
		logger.info("*** Step 2 Expected Results: - The Incremental Price Table page is displays without information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Pricing ID label should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Pricing name should be displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().equalsIgnoreCase(pricingID),"        Pricing ID label should be equal priceID loaded input");
		assertTrue(isElementPresent(incrementalPricing.basicTestCheckboxTblRight(0), 5),"        Basic test radio should be displayed");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add new rule button should be displayed");
		
		logger.info("*** Step 3 Actions: - Enter pricing name. At Rule one - Inclusion & Exclusion table. Check test radio. Click Add icon");
		incrementalPricing.inputPricingName(randomCharacter.getRandomAlphaNumericString(6));
		clickHiddenPageObject(incrementalPricing.basicTestCheckboxTblRight(0),0);
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0),0);
		
		logger.info("*** Step 3 Expected Results: - An add record popup is displayed");
		assertTrue(isElementPresent(incrementalPricing.addNewInclusionExclusionPopup(0), 5),"        Add new popup should be displayed");
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test ID input should be displayed");
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Submit button should be displayed");
		
		logger.info("*** Step 4 Actions: - Enter valid test ID. Click ok button");
		List<String> testCode = daoManagerXifinRpm.getSingleTest(testDb);
		String testID = testCode.get(1);
		incrementalPricing.inputTestId(testID);
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);
		
		logger.info("*** Step 4 Expected Results: - New inclusion added to table");
		assertTrue(isElementPresent(incrementalPricing.tableRuleTest(0), 5),"        Table rule test should be displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), testID),"        Test ID should be add to table");
		
		logger.info("*** Step 5 Actions: - Click add icon at Quantity table");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblLeftBtn(0), 5),"        Add quantity button should be displayed");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblLeftBtn(0), 0);
		
		logger.info("*** Step 5 Expected Results: - New add record popup is displayed");
		assertTrue(isElementPresent(incrementalPricing.addNewRulePopup(0), 5),"        Add new popup should be displayed");
		assertTrue(isElementPresent(incrementalPricing.price(), 5),"        Price input field should be displayed");
		assertTrue(isElementPresent(incrementalPricing.quantityFromInput(), 5),"        Quantity From input field should be displayed");
		assertTrue(isElementPresent(incrementalPricing.quantityToInput(), 5),"        Quantity To input field should be displayed");
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Submit button should be displayed");
		
		logger.info("*** Step 6 Actions: - Enter valid Quantity From, Quantity To, Price. Click ok");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		int quantity = incrementalPricingUtils.getNonZeroRandomNumber(1);
		int quantityTo = incrementalPricingUtils.getNonZeroRandomNumber(2);
		int price = incrementalPricingUtils.getNonZeroRandomNumber(2);
		assertTrue(isElementPresent(incrementalPricing.quantityFromInput(), 5),"        Quantity input should be displayed");
		incrementalPricing.inputQuantityFrom(Integer.toString(quantity));
		assertTrue(isElementPresent(incrementalPricing.quantityToInput(), 5),"        Quantity to input should be displayed");
		incrementalPricing.inputQuantityTo(Integer.toString(quantityTo));
		assertTrue(isElementPresent(incrementalPricing.price(), 5),"        Price input should be displayed");
		incrementalPricing.inputPrice(Integer.toString(price));
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Ok button should show.");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);
		
		logger.info("*** Step 6 Expected Results: - New quantity added to table");
		assertTrue(isElementPresent(incrementalPricing.tableRuleQuantity(0), 5),"        Table Quantity should be displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleQuantity(0), Integer.toString(quantity)),"        Quantity should be add to table");
		
		logger.info("*** Step 7 Actions: - Click submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button should be displayed");
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);
		
		logger.info("*** Step 7 Expected Results: - Screen come back to load pricing page");
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price Table page title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Price Table'");
		
		logger.info("*** Step 8 Actions: - Load the same Pricing ID again");
		incrementalPricing.checkInputPricingId(pricingID,10);
		
		logger.info("*** Step 8 Expected Results: - The Incremental Price Table page is displays with correct information. At rule 1 procedure radio disable");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Pricing ID label should be displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().equalsIgnoreCase(pricingID),"        Pricing ID label should be equal priceID loaded input");
		assertTrue(isElementPresent(incrementalPricing.tableRuleTest(0), 5),"        Table rule test should be displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), testID),"        Test ID should be added to table");
		assertTrue(isElementPresent(incrementalPricing.tableRuleQuantity(0), 5),"        Table Quantity should be displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleQuantity(0), Integer.toString(quantity)),"        Quantity should be add to table");
		assertTrue(isElementDisabled(incrementalPricing.basicProcCheckboxTblRight(0), "disabled", "", 5),"        Procedure radio should be disable");
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
		}
	
	@Test(priority = 1, description = "Add Test Rule - Verify that user can add testID or test type")
	public void testXPR_432() throws Exception {
		
		logger.info("===== Testing - XPR_432 =====");  
		
		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: - Incremental Price Table page title displays");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price Table page title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Price Table'");
		
		logger.info("*** Step 2 Actions: - Enter a new Pricing ID. Tab out");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing ID input should be displayed");
		randomCharacter = new RandomCharacter(driver);
		String pricingID = "AUTOTEST" + randomCharacter.getRandomNumericString(6);
		incrementalPricing.checkInputPricingId(pricingID,10);
		
		logger.info("*** Step 2 Expected Results: - The Incremental Price Table page is displays without information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Pricing ID label should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Pricing name should be displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().equalsIgnoreCase(pricingID),"        Pricing ID label should be equal priceID loaded input");
		assertTrue(isElementPresent(incrementalPricing.basicTestCheckboxTblRight(0), 5),"        Basic test radio should be displayed");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add new rule button should be displayed");
		
		logger.info("*** Step 3 Actions: - Enter pricing name. At Rule one - Inclusion & Exclusion table. Check test radio. Click Add icon");
		incrementalPricing.inputPricingName(randomCharacter.getRandomAlphaNumericString(6));
		clickHiddenPageObject(incrementalPricing.basicTestCheckboxTblRight(0),0);
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0),0);
		
		logger.info("*** Step 3 Expected Results: - An add record popup is displayed");
		assertTrue(isElementPresent(incrementalPricing.addNewInclusionExclusionPopup(0), 5),"        Add new popup should be displayed");
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test ID input should be displayed");
		assertTrue(isElementPresent(incrementalPricing.testTypeDropdown(), 5),"        Test type dropdown should be displayed");
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Submit button should be displayed");
		
		logger.info("*** Step 4 Actions: - Enter invalid test ID. Select test type");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		List<String> testCode = incrementalPricingUtils.getTestHaveTestType(daoManagerXifinRpm);
		String testID = testCode.get(0);
		String testType = testCode.get(3);
		incrementalPricing.inputTestId(randomCharacter.getRandomAlphaString(5));
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.testTypeDropdown(), testType);

		logger.info("*** Step 4 Expected Results: - The test ID will be removed");
		assertTrue(incrementalPricing.testIdInput().getAttribute("value").length()<1,"        Test ID should be removed");
		
		logger.info("*** Step 5 Actions: - Enter a valid test ID");
		testCodeUtils = new TestCodeUtils(driver);
		testCodeUtils.inputText(incrementalPricing.testIdInput(), testID, true);
		
		logger.info("*** Step 5 Expected Results: - Test type dropdown will be removed");
		assertTrue(incrementalPricingUtils.getCurrentSelectTextInJQGridDropdown(incrementalPricing.testTypeDropdown()).length() < 1,"        Test type selected should be removed");
		
		clickHiddenPageObject(incrementalPricing.cancelBtn(),0);
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
		}
	
	@Test(priority = 1, description = "Add Test Rule-Add 2 same Tests into Inclusion and Exclusion table")
	public void testXPR_433() throws Exception {
		
		logger.info("===== Testing - XPR_433 =====");  
		
		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: - Incremental Price Table page title displays");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price Table page title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Price Table'");
		
		logger.info("*** Step 2 Actions: - Enter a new Pricing ID. Tab out");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing ID input should be displayed");
		randomCharacter = new RandomCharacter(driver);
		String pricingID = "AUTOTEST" + randomCharacter.getRandomNumericString(6);
		incrementalPricing.checkInputPricingId(pricingID,10);
		
		logger.info("*** Step 2 Expected Results: - The Incremental Price Table page is displays without information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Pricing ID label should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Pricing name should be displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().trim().equalsIgnoreCase(pricingID),"        Pricing ID label should be equal priceID loaded input");
		assertTrue(isElementPresent(incrementalPricing.basicTestCheckboxTblRight(0), 5),"        Basic test radio should be displayed");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add new rule button should be displayed");
		
		logger.info("*** Step 3 Actions: - Enter pricing name. At Rule one - Inclusion & Exclusion table. Check test radio. Click Add icon");
		incrementalPricing.inputPricingName(randomCharacter.getRandomAlphaNumericString(6));
		clickHiddenPageObject(incrementalPricing.basicTestCheckboxTblRight(0),0);
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0),0);
		
		logger.info("*** Step 3 Expected Results: - An add record popup is displayed");
		assertTrue(isElementPresent(incrementalPricing.addNewInclusionExclusionPopup(0), 5),"        Add new popup should be displayed");
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test ID input should be displayed");
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Submit button should be displayed");
		
		logger.info("*** Step 4 Actions: - Enter valid test ID. Click ok button");
		List<String> testCode = daoManagerXifinRpm.getSingleTest(testDb);
		String testID = testCode.get(1);
		incrementalPricing.inputTestId(testID);
		incrementalPricing.clickOkButton();
		
		logger.info("*** Step 4 Expected Results: - New inclusion added to table");
		assertTrue(isElementPresent(incrementalPricing.tableRuleTest(0), 5),"        Table rule test should be displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), testID),"        Test ID should be add to table");
		
		logger.info("*** Step 5 Actions: - Enter test ID again. Check on Exclude checkbox. Click ok button");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0),0);
		incrementalPricing.inputTestId(testID);
		assertTrue(isElementPresent(incrementalPricing.excludeTypeDropDown(), 5),"        Include/Exclude/Require dropdown should be displayed.");
		selectItemByVal(incrementalPricing.excludeTypeDropDown(), "Exclude");
		incrementalPricing.clickOkButton();

		logger.info("*** Step 5 Expected Results: - New test id added to table with exclude option");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), testID),"        Test ID should be add to table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), "Exclude"), "        'Exclude' should be displayed in the table.");
		
		logger.info("*** Step 6 Actions: - Click submit button. And load again test ID");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button should be displayed");
		
		logger.info("*** Step 6 Expected Results: - Two test ID added is save corresponding pricing ID");
		assertTrue(isElementPresent(incrementalPricing.tableRuleTest(0), 5),"        Table rule test should be displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), testID),"        Test ID should be added to table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), "Exclude"), "        'Exclude' should be displayed in the table.");
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
		}
	
	@Test(priority = 1, description = "Add Test Rule - One rule with the same Test Type")
	public void testXPR_434() throws Exception {
		
		logger.info("===== Testing - XPR_434 =====");  
		
		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: - Incremental Price Table page title displays");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price Table page title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Price Table'");
		
		logger.info("*** Step 2 Actions: - Enter a new Pricing ID. Tab out");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing ID input should be displayed");
		randomCharacter = new RandomCharacter(driver);
		String pricingID = "AUTOTEST" + randomCharacter.getRandomNumericString(6);
		incrementalPricing.checkInputPricingId(pricingID,10);
		
		logger.info("*** Step 2 Expected Results: - The Incremental Price Table page is displays without information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Pricing ID label should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Pricing name should be displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().equalsIgnoreCase(pricingID),"        Pricing ID label should be equal priceID loaded input");
		assertTrue(isElementPresent(incrementalPricing.basicTestCheckboxTblRight(0), 5),"        Basic test radio should be displayed");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add new rule button should be displayed");
		
		logger.info("*** Step 3 Actions: - Enter pricing name. At Rule one - Inclusion & Exclusion table. Check test radio. Add test type");
		incrementalPricing.inputPricingName(randomCharacter.getRandomAlphaNumericString(6));
		clickHiddenPageObject(incrementalPricing.basicTestCheckboxTblRight(0),0);
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0),0);
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		List<String> testInfo = incrementalPricingUtils.getTestHaveTestType(daoManagerXifinRpm);
		String testType = testInfo.get(3);
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.testTypeDropdown(), testType);
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Submit button should be displayed");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);
		
		logger.info("*** Step 3 Expected Results: - New test type added to table");
		assertTrue(isElementPresent(incrementalPricing.tableRuleTest(0), 5),"        Table rule test should be displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), testType),"        Test type should be add to table");
		
		logger.info("*** Step 4 Actions: - Input again test type. Check on Excluded checkbox. Click ok button");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0),0);
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.testTypeDropdown(), testType);
		assertTrue(isElementPresent(incrementalPricing.excludeTypeDropDown(), 5),"        Include/Exclude/Require dropdown should be displayed.");
		selectItemByVal(incrementalPricing.excludeTypeDropDown(), "Exclude");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);
		
		logger.info("*** Step 4 Expected Results: - New test type added to table with exclude");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), testType),"        Test type should be add to table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), "Exclude"), "        'Exclude' should be displayed in the table.");
		
		logger.info("*** Step 5 Actions: - Click submit button. And load again test ID");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button should be displayed");
		
		logger.info("*** Step 5 Expected Results: - Two test type added is save corresponding pricing ID");
		assertTrue(isElementPresent(incrementalPricing.tableRuleTest(0), 5),"        Table rule test should be displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), testType),"        Test type should be added to table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), "Exclude"), "        'Exclude' should be displayed in the table.");
		
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
		}
	
	@Test(priority = 1, description = "Add Test Rule - Add 2 non - overlapping quantities")
	public void testXPR_443() throws Exception {
		
		logger.info("===== Testing - XPR_443 =====");  
		
		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: - Incremental Price Table page title displays");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price Table page title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Price Table'");
		
		logger.info("*** Step 2 Actions: - Enter a new Pricing ID. Tab out");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing ID input should be displayed");
		randomCharacter = new RandomCharacter(driver);
		String priceID = "AUTOTEST" + randomCharacter.getRandomNumericString(6);
		incrementalPricing.inputInPriID(priceID);
		
		logger.info("*** Step 2 Expected Results: - The Incremental Price Table page is displays without information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Pricing ID label should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Pricing name should be displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().trim().equalsIgnoreCase(priceID),"        Pricing ID label should be equal priceID loaded input");
		
		logger.info("*** Step 3 Actions: - Enter pricing name. Add quantity with valid data");
		incrementalPricing.inputPricingName(randomCharacter.getRandomAlphaNumericString(6));
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblLeftBtn(0), 5),"        Add quantity button should be displayed");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblLeftBtn(0), 0);
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		int quantity = incrementalPricingUtils.getNonZeroRandomNumber(1);
		int quantityTo = incrementalPricingUtils.getNonZeroRandomNumber(2);
		int price = incrementalPricingUtils.getNonZeroRandomNumber(2);
		assertTrue(isElementPresent(incrementalPricing.quantityFromInput(), 5),"        Quantity input should be displayed");
		incrementalPricing.inputQuantityFrom(Integer.toString(quantity));
		assertTrue(isElementPresent(incrementalPricing.quantityToInput(), 5),"        Quantity to input should be displayed");
		incrementalPricing.inputQuantityTo(Integer.toString(quantityTo));
		assertTrue(isElementPresent(incrementalPricing.price(), 5),"        Price input should be displayed");
		incrementalPricing.inputPrice(Integer.toString(price));
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Ok button should show.");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);
		
		logger.info("*** Step 3 Expected Results: - New quantity added to table");
		assertTrue(isElementPresent(incrementalPricing.tableRuleQuantity(0), 5),"        Table Quantity should be displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleQuantity(0), Integer.toString(quantity)),"        Quantity should be add to table");
		
		logger.info("*** Step 4 Actions: - Add again quantity with Quantity from > Quantity to above");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblLeftBtn(0), 0);
		int qFrom2 = quantityTo+1;
		incrementalPricing.inputQuantityFrom(Integer.toString(qFrom2));
		incrementalPricing.inputPrice(Integer.toString(price));
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);
		
		logger.info("*** Step 4 Expected Results: - New quantity added to table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleQuantity(0), Integer.toString(qFrom2)),"        Quantity should be add to table");
		
		logger.info("*** Step 5 Actions: - Add new test ID with valid data");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.basicTestCheckboxTblRight(0), 5),"        Basic test radio should be displayed");
		clickHiddenPageObject(incrementalPricing.basicTestCheckboxTblRight(0),0);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add new rule button should be displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0),0);

		List<String> testCode = daoManagerXifinRpm.getSingleTest(testDb);
		String testID = testCode.get(1);
		incrementalPricing.inputTestId(testID);
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Submit button should be displayed");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);
		
		logger.info("*** Step 5 Expected Results: - New inclusion added to table");
		assertTrue(isElementPresent(incrementalPricing.tableRuleTest(0), 5),"        Table rule test should be displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), testID),"        Test ID should be add to table");
		
		logger.info("*** Step 6 Actions: - Submit all data and load ID again");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button should be displayed");
		
		logger.info("*** Step 6 Expected Results: - The Incremental Price Table page is displays with correct information. Quantity have 2 records");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Pricing ID label should be displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().equalsIgnoreCase(priceID),"        Pricing ID label should be equal priceID loaded input");
		assertTrue(isElementPresent(incrementalPricing.tableRuleTest(0), 5),"        Table rule test should be displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), testID),"        Test ID should be added to table");
		assertTrue(isElementPresent(incrementalPricing.tableRuleQuantity(0), 5),"        Table Quantity should be displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleQuantity(0), Integer.toString(quantity)),"        Quantity should be add to table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleQuantity(0), Integer.toString(qFrom2)),"        Quantity should be add to table");
		
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
		}
	
	@Test(priority = 1, description = "Add Proc Rule - Add Procs with valid data")
	public void testXPR_449() throws Exception {
		logger.info("===== Testing - testXPR_449 =====");  	

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: - The load Incremental Price table is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price table title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Price Table'");							

		logger.info("*** Step 2 Actions: - Enter a new pricing Id ");
		randomCharacter = new RandomCharacter(driver);
		String newPricingId = "AUTOTEST" + randomCharacter.getRandomNumericString(6);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id Input filed should show");
		incrementalPricing.checkInputPricingId(newPricingId,10);

		logger.info("*** Step 2 Expected Results: - The Incremental Price table page is displayed without information");
		assertTrue(isElementPresent(incrementalPricing.incrementalPricingName(), 5),"        Incremental Price Table Name should show");

		logger.info("*** Step 3 Actions: - Input Pricing Name - Inclusion & Exclusion : check on procedure radio button");
		String pricingName ="testXPR449" + randomCharacter.getRandomAlphaString(10);
		incrementalPricing.inputIncrementalPricingName(pricingName);
		assertTrue(isElementPresent(incrementalPricing.procInclusionExclusionRadioBtn(), 5),"        Procedure Inclusion/Exclusion Radio button should show");
		selectCheckBox(incrementalPricing.procInclusionExclusionRadioBtn());

		logger.info("*** Step 3 Expected Results: - Table column label will change");
		assertTrue(isElementPresent(incrementalPricing.procCodeHeaderInclusionExclusionTable(0), 5),"        Procedure code Inclusion/Exclusion header should show");
		assertTrue(incrementalPricing.procCodeHeaderInclusionExclusionTable(0).getText().equalsIgnoreCase("Proc Code"),"        Table column label will change to ProcCode Name");
		assertTrue(isElementPresent(incrementalPricing.serviceTypHeaderInclusionExclusionTable(0), 5),"        Service Type Inclusion/Exclusion header should show");
		assertTrue(incrementalPricing.serviceTypHeaderInclusionExclusionTable(0).getText().equalsIgnoreCase("Service Type"),"        Table column label will change to Service Type");

		logger.info("*** Step 4 Actions: - Input valid ProcCode");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add new  Inclusion/Exclusion button should show");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		String proCodeId = daoManagerXifinRpm.getProcedureCodeId(testDb).get(0);
		String serviceType = serviceDao.getServiceTypFromSvcTypAndProcCd().getDescr();
		assertTrue(isElementPresent(incrementalPricing.procIdInput(), 5),"        Proc Id input field should show");
		incrementalPricing.inputProcId(proCodeId);
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.serviceTypeDropdown(),serviceType);
		selectCheckBox(incrementalPricing.excludeRefTestsCheckbox());
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        ok button should show");
		incrementalPricing.clickOkButton();

		logger.info("*** Step 4 Expected Results: - New proc information is added into the table with");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), "Include"), "        'Include' should display in the table");
		assertTrue(incrementalPricingUtils.checkCheckBoxInTable(incrementalPricing.tableRuleTest(0), "offval", "no"),"       Xref test check box is selected");

		logger.info("*** Step 5 Actions : - Click On Submit");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button  should show");
		incrementalPricing.clickSubmitIncrPricingBtn();

		logger.info("*** Step 5 Expected Results : - New pricing is save with rule is Procedure");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id Input filed should show");
		incrementalPricing.checkInputPricingId(newPricingId,10);

		incrementalPricing.clickResetIncrPricingBtn();
		}	

	@Test(priority = 1, description = "Add Proc Rule - Delete Procedure")
	public void testXPR_452() throws Exception {
		logger.info("===== Testing - testXPR_452 =====");  	

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: - The load Incremental Price table is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price table title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Price Table'");							

		logger.info("*** Step 2 Actions: - Enter a new Pricing Id ");
		randomCharacter = new RandomCharacter(driver);
		String newPricingId = "AUTOTEST" + randomCharacter.getRandomNumericString(6);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id Input filed should show");
		incrementalPricing.checkInputPricingId(newPricingId,10);

		logger.info("*** Step 2 Expected Results: - The Incremental Price table page is displayed without information");
		assertTrue(isElementPresent(incrementalPricing.incrementalPricingName(), 5),"        Incremental Price Table Name should show");
		assertTrue(incrementalPricing.incrementalPricingName().getAttribute("value").isEmpty(),"       The Incremental Price table page is displayed without information");

		logger.info("*** Step 3 Actions: - Enter Pricing Name - Inclusion & Exclusion : check on procedure radio button");
		String pricingName ="testXPR452" + randomCharacter.getRandomAlphaString(10);
		incrementalPricing.inputIncrementalPricingName(pricingName);
		assertTrue(isElementPresent(incrementalPricing.procInclusionExclusionRadioBtn(), 5),"        Procedure Inclusion/Exclusion Radio button should show");
		selectCheckBox(incrementalPricing.procInclusionExclusionRadioBtn());

		logger.info("*** Step 3 Expected Results: - Table column label will change");
		assertTrue(isElementPresent(incrementalPricing.procCodeHeaderInclusionExclusionTable(0), 5),"        Procedure code Inclusion/Exclusion header should show");
		assertTrue(incrementalPricing.procCodeHeaderInclusionExclusionTable(0).getText().equalsIgnoreCase("Proc Code"),"        Table column label will change to ProcCode Name");
		assertTrue(isElementPresent(incrementalPricing.serviceTypHeaderInclusionExclusionTable(0), 5),"        Service Type Inclusion/Exclusion header should show");
		assertTrue(incrementalPricing.serviceTypHeaderInclusionExclusionTable(0).getText().equalsIgnoreCase("Service Type"),"        Table column label will change to Service Type");

		logger.info("*** Step 4 Actions: - Input valid ProcCode");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		List<List<String>> incluseRuleInfo = incrementalPricing.addMultiIncludeRule(2, 0, this, testDb);

		logger.info("*** Step 4 Expected Results: - New proc information is added into the table with");
		assertTrue(incrementalPricingUtils.checkCheckBoxInTable(incrementalPricing.tableRuleTest(0), "offval", "no"),"       Xref test check box is selected");

		logger.info("*** Step 5 Actions : - Click On Submit");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button should show");
		incrementalPricing.clickSubmitIncrPricingBtn();

		logger.info("*** Step 5 Expected Results : - New pricing is save with rule is Procedure");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id Input field should show");
		Assert.assertNotNull(prcDao.getPrcByPrcAbbrev(newPricingId.toUpperCase()),"        New pricing is save with rule is Procedure");

		logger.info("*** Step 6 Actions : - Load Pricing Id has just created");
		incrementalPricing.checkInputPricingId(newPricingId,10);

		logger.info("*** Step 6 Expected Results : - The Incremental Price table page is displayed with correct information");
		assertTrue(isElementPresent(incrementalPricing.incrementalPricingName(), 5),"        Incremental Price Table Name should show");
		assertTrue(incrementalPricing.incrementalPricingName().getAttribute("value").trim().equalsIgnoreCase(pricingName.toUpperCase().trim()),"       The Incremental Price table page is displayed with correct information");

		logger.info("*** Step 7 Actions : - Rule Inclusion/Exclusion : Select any row - click on edit button - check on delete row");
		assertTrue(isElementPresent(incrementalPricing.tableRuleTest(0), 5),"        Table rule should show");
		selectColumnValue(incrementalPricing.tableRuleTest(0), incluseRuleInfo.get(0).get(1));
		assertTrue(isElementPresent(incrementalPricing.delCheckbox(), 5),"        delete check box should show");
		selectCheckBox(incrementalPricing.delCheckbox());
		incrementalPricing.clickOkButton();

		logger.info("*** Step 7 Expected Results : - The selected proc code will be marked as delete row with delete checkbox is check in table");
		assertTrue(isElementPresent(incrementalPricing.serviceTypeCellInInclusionTable(0,2), 5),"        First row in table should show");
		assertTrue(incrementalPricingUtils.checkDeleteRowInTable(incrementalPricing.serviceTypeCellInInclusionTable(0,2), "rowMarkedForDelete"),"       The selected proc code will be marked as delete row with delete checkbox is check in table");

		logger.info("*** Step 8 Actions : - Click On Submit");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button should show");
		incrementalPricing.clickSubmitIncrPricingBtn();

		logger.info("*** Step 8 Expected Results : - The selected procCode is remove out of pricing Id");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id Input filed should show");
		incrementalPricing.checkInputPricingId(newPricingId,10);
		assertFalse(getColumnValue(incrementalPricing.tableRuleTest(0), incluseRuleInfo.get(0).get(1)),"        The selected procCode is remove out of pricing Id");

		incrementalPricing.clickResetIncrPricingBtn();
		}	


	@Test(priority = 1, description = "Add Proc Rule - Add Duplicate Proc Code")
	public void testXPR_454() throws Exception {
		logger.info("===== Testing - testXPR_454 =====");  	

		logger.info("*** Step 1 Actions: - Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: - The load Incremental Price table is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price table title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Price Table'");							

		logger.info("*** Step 2 Actions: - Enter a new pricing Id ");
		randomCharacter = new RandomCharacter(driver);
		String newPricingId = "AUTOTEST" + randomCharacter.getRandomNumericString(6);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id Input filed should show");
		incrementalPricing.checkInputPricingId(newPricingId,10);

		logger.info("*** Step 2 Expected Results: - The Incremental Price table page is displayed without information");
		assertTrue(isElementPresent(incrementalPricing.incrementalPricingName(), 5),"        Incremental Price Table Name should show");
		assertTrue(incrementalPricing.incrementalPricingName().getAttribute("value").isEmpty(),"       The Incremental Price table page is displayed without information");

		logger.info("*** Step 3 Actions: - Enter Pricing Name - Inclusion & Exclusion : check on procedure radio button");
		String pricingName ="testXPR454" + randomCharacter.getRandomAlphaString(10);
		incrementalPricing.inputIncrementalPricingName(pricingName);
		assertTrue(isElementPresent(incrementalPricing.procInclusionExclusionRadioBtn(), 5),"        Procedure Inclusion/Exclusion Radio button should show");
		selectCheckBox(incrementalPricing.procInclusionExclusionRadioBtn());

		logger.info("*** Step 3 Expected Results: - Table column label will change");
		assertTrue(isElementPresent(incrementalPricing.procCodeHeaderInclusionExclusionTable(0), 5),"        Procedure code Inclusion/Exclusion header should show");
		assertTrue(incrementalPricing.procCodeHeaderInclusionExclusionTable(0).getText().equalsIgnoreCase("Proc Code"),"        Table column label will change to ProcCode Name");
		assertTrue(isElementPresent(incrementalPricing.serviceTypHeaderInclusionExclusionTable(0), 5),"        Service Type Inclusion/Exclusion header should show");
		assertTrue(incrementalPricing.serviceTypHeaderInclusionExclusionTable(0).getText().equalsIgnoreCase("Service Type"),"        Table column label will change to Service Type");

		logger.info("*** Step 4 Action: - Enter valid ProcCode");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add new Inclusion/Exclusion button should show");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		String proCodeId = daoManagerXifinRpm.getProcedureCodeId(testDb).get(0);
		assertTrue(isElementPresent(incrementalPricing.procIdInput(), 5),"        Proc Id input field should show");
		incrementalPricing.inputProcId(proCodeId);
		selectCheckBox(incrementalPricing.excludeRefTestsCheckbox());
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        ok button should show");
		incrementalPricing.clickOkButton();

		logger.info("*** Step 4 Expected Results: - New proc information is added into the table with");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), proCodeId),"       New Proc Code is added in table");

		logger.info("*** Step 5 Actions : - Click add icon again - input same proccode Id - Click ok button");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add new  Inclusion/Exclusion button should show");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		assertTrue(isElementPresent(incrementalPricing.procIdInput(), 5),"        Proc Id input field should show");
		incrementalPricing.inputProcId(proCodeId);
		selectCheckBox(incrementalPricing.excludeRefTestsCheckbox());
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        ok button should show");
		incrementalPricing.clickOkButton();

		logger.info("*** Step 5 Expected Results : - New Proc Code is added in table - the error message is displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), proCodeId),"       New Proc Code is added in table");
		assertTrue(incrementalPricing.errorMessagePanel().getText().contains("A Procedure code can only be included in one rule per Incremental Table. Procedure code "+proCodeId+" is in Rule 1."),"        The error message 'A Procedure code can only be included in one rule per Incremental Table. Procedure code XXX is in Rule X.'");

		incrementalPricing.clickResetIncrPricingBtn();
		}

}	
