package com.newXp.tests;

import com.mbasys.mars.ejb.entity.prc.Prc;
import com.overall.fileMaintenance.pricing.incrementalPricing.IncrementalPricing;
import com.overall.fileMaintenance.pricing.testCode.TestCode;
import com.overall.menu.MenuNavigation;
import com.overall.search.IncrementalPricingSearch;
import com.overall.search.IncrementalPricingSearchResults;
import com.overall.search.ProcCodeSearch;
import com.overall.search.ProcCodeSearchResults;
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
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class RegressionIncrementalPricing extends SeleniumBaseTest {

	private IncrementalPricing incrementalPricing;
	private TestCodeUtils testCodeUtils;
	private RandomCharacter randomCharacter;
	private MenuNavigation navigation;
	private IncrementalPricingUtils incrementalPricingUtils;
	private IncrementalPricingSearchResults incrementalPricingSearchResults;
	private IncrementalPricingSearch incrementalPricingSearch;
	private TestCode testCode;
	private ProcCodeSearch procCodeSearch;
	private ProcCodeSearchResults procCodeSearchResults;

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
	/*Search Pricing ID*/
	@Test(priority = 1, description ="Search Incremental Pricing with invalid data")
	public void testXPR_415() throws Exception {
		logger.info("===== Testing - testXPR_415 =====");

		logger.info("*** Step 1 Actions: Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: The Load Incremental Pricing table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		incrementalPricingSearch = new IncrementalPricingSearch(driver);
		incrementalPricingSearchResults = new IncrementalPricingSearchResults(driver, config);
		assertTrue(isElementPresent(incrementalPricing.inPriLoadTableTitle(), 5),"        The Load Incremental Pricing table page is shown");
		assertEquals(incrementalPricing.inPriLoadTableTitle().getText(), "Load Incremental Price Table","        The Load Incremental Pricing table page is displayed with correct page title");

		logger.info("*** Step 2 Actions: Click on Incremental Pricing Search icon");
		assertTrue(isElementPresent(incrementalPricing.inPriSearchIconBtn(), 5),"        The Incremental Pricing Search icon button is shown");
		incrementalPricing.clickInPriSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: The Incremental Pricing ID search page is displayed");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchTitle(), 5),"        The Incremental Pricing Search title is shown");
		assertEquals(incrementalPricingSearch.inPriSearchTitle().getText(), "Incremental Pricing Search","        The Incremental Pricing Search title is displayed");

		logger.info("*** Step 3 Actions: Input invalid data for Pricing ID/ Pricing Name and click on Search button");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchPriID(), 5),"        The Incremental Pricing Search Pricing ID field is shown");
		incrementalPricingSearch.inputInPriSearchPriID("123A-@");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchPriName(), 5),"        The Incremental Pricing Search Pricing Name field is shown");
		incrementalPricingSearch.inputInPriSearchPriName("123B-@");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchSearchBtn(), 5),"        The Incremental Pricing Search Search button is shown");
		incrementalPricingSearch.clickInPriSearchSearchBtn();

		logger.info("*** Step 3 Expected Results: The Search Result page is empty");
		assertTrue(isElementPresent(incrementalPricingSearchResults.inPriSearchResultTotalResult(), 5),"        The Incremental Pricing Search Result Total Results is shown");
		assertEquals(incrementalPricingSearchResults.inPriSearchResultTotalResult().getText(), "No Result","        The Search Result page is empty");

		assertTrue(isElementPresent(incrementalPricingSearchResults.inPriSearchResultCloseBtn(), 5),"        The Incremental Pricing Search Result Close button is shown");
		incrementalPricingSearchResults.clickInPriSearchCloseBtn();
	}

	@Test(priority = 3, description ="Search all Incremental Pricing ID")
	public void testXPR_416() throws Exception {
		logger.info("===== Testing - testXPR_416 =====");

		logger.info("*** Step 1 Actions: Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: The Load Incremental Pricing table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		incrementalPricingSearch = new IncrementalPricingSearch(driver);
		incrementalPricingSearchResults = new IncrementalPricingSearchResults(driver, config);
		assertTrue(isElementPresent(incrementalPricing.inPriLoadTableTitle(), 5),"        The Load Incremental Pricing table page is shown");
		assertEquals(incrementalPricing.inPriLoadTableTitle().getText(), "Load Incremental Price Table","        The Load Incremental Pricing table page is displayed with correct page title");

		logger.info("*** Step 2 Actions: Click on Incremental Pricing Search icon");
		assertTrue(isElementPresent(incrementalPricing.inPriSearchIconBtn(), 5),"        The Incremental Pricing Search icon button is shown");
		incrementalPricing.clickInPriSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: The Incremental Pricing ID search page is displayed");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchTitle(), 5),"        The Incremental Pricing Search title is shown");
		assertEquals(incrementalPricingSearch.inPriSearchTitle().getText(), "Incremental Pricing Search","        The Incremental Pricing Search title is displayed");

		logger.info("*** Step 3 Actions: Input * at Pricing ID field and click on Search button");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchPriID(), 5),"        The Incremental Pricing Search Pricing ID field is shown");
		incrementalPricingSearch.inputInPriSearchPriID("*");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchSearchBtn(), 5),"        The Incremental Pricing Search Search button is shown");
		incrementalPricingSearch.clickInPriSearchSearchBtn();

		logger.info("*** Step 3 Expected Results: All Pricing Ids are existing in db will be displayed at Search Result page");
		int total = incrementalPricingSearchResults.getTotalPricingID();
		assertTrue(isElementPresent(incrementalPricingSearchResults.inPriSearchResultTotalResult(), 5),"        The Incremental Pricing Search Result Total Results is shown");
		assertEquals(String.valueOf(incrementalPricingSearchResults.numOfRecordsInResult()), String.valueOf(total),"        All Pricing Ids are existing in db will be displayed at Search Result page");
		assertTrue(isElementPresent(incrementalPricingSearchResults.inPriSearchResultCloseBtn(), 5),"        The Incremental Pricing Search Result Close button is shown");
		incrementalPricingSearchResults.clickInPriSearchCloseBtn();
	}

	@Test(priority = 1, description ="Search Pricing ID - Verify New Search button")
	public void testXPR_418() throws Exception {
		logger.info("===== Testing - testXPR_418 =====");

		logger.info("*** Step 1 Actions: Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: The Load Incremental Pricing table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		incrementalPricingSearch = new IncrementalPricingSearch(driver);
		incrementalPricingSearchResults = new IncrementalPricingSearchResults(driver, config);
		assertTrue(isElementPresent(incrementalPricing.inPriLoadTableTitle(), 5),"        The Load Incremental Pricing table page is shown");
		assertEquals(incrementalPricing.inPriLoadTableTitle().getText(), "Load Incremental Price Table","        The Load Incremental Pricing table page is displayed with correct page title");

		logger.info("*** Step 2 Actions: Click on Incremental Pricing Search icon");
		assertTrue(isElementPresent(incrementalPricing.inPriSearchIconBtn(), 5),"        The Incremental Pricing Search icon button is shown");
		incrementalPricing.clickInPriSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: The Incremental Pricing ID search page is displayed");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchTitle(), 5),"        The Incremental Pricing Search title is shown");
		assertEquals(incrementalPricingSearch.inPriSearchTitle().getText(), "Incremental Pricing Search","        The Incremental Pricing Search title is displayed");

		logger.info("*** Step 3 Actions: Input data for Pricing ID and click on Search button");
		Prc priInfo = prcDao.getListOfIncrPricing();
		String priID = priInfo.getPrcAbbrev();
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchPriID(), 5),"        The Incremental Pricing Search Pricing ID field is shown");
		incrementalPricingSearch.inputInPriSearchPriID(priID);
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchSearchBtn(), 5),"        The Incremental Pricing Search Search button is shown");
		incrementalPricingSearch.clickInPriSearchSearchBtn();

		logger.info("*** Step 3 Expected Results: The Search Result page is displayed");
		assertTrue(isElementPresent(incrementalPricingSearchResults.inPriSearchResultTitle(), 5),"        The Incremental Pricing Search Result title is shown");
		assertEquals(incrementalPricingSearchResults.inPriSearchResultTitle().getText(), "Incremental Pricing Search Results","        The Incremental Pricing Search Result title is displayed");

		logger.info("*** Step 4 Actions: Click on New Search button");
		assertTrue(isElementPresent(incrementalPricingSearchResults.inPriSearchResultNewSearchBtn(), 5),"        The Incremental Pricing Search Result title is shown");
		clickHiddenPageObject(incrementalPricingSearchResults.inPriSearchResultNewSearchBtn(), 0);

		logger.info("*** Step 4 Expected Results: System will take user back to Incremental Pricing Search page");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchTitle(), 5),"        The Incremental Pricing Search title is shown");
		assertEquals(incrementalPricingSearch.inPriSearchTitle().getText(), "Incremental Pricing Search","        The Incremental Pricing Search title is displayed");
	}

	@Test(priority = 1, description ="Search Pricing - Verify Reset button")
	public void testXPR_419() throws Exception {
		logger.info("===== Testing - testXPR_419 =====");

		logger.info("*** Step 1 Actions: Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: The Load Incremental Pricing table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		incrementalPricingSearch = new IncrementalPricingSearch(driver);
		incrementalPricingSearchResults = new IncrementalPricingSearchResults(driver, config);
		assertTrue(isElementPresent(incrementalPricing.inPriLoadTableTitle(), 5),"        The Load Incremental Pricing table page is shown");
		assertEquals(incrementalPricing.inPriLoadTableTitle().getText(), "Load Incremental Price Table","        The Load Incremental Pricing table page is displayed with correct page title");

		logger.info("*** Step 2 Actions: Click on Incremental Pricing Search icon");
		assertTrue(isElementPresent(incrementalPricing.inPriSearchIconBtn(), 5),"        The Incremental Pricing Search icon button is shown");
		incrementalPricing.clickInPriSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: The Incremental Pricing ID search page is displayed");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchTitle(), 5),"        The Incremental Pricing Search title is shown");
		assertEquals(incrementalPricingSearch.inPriSearchTitle().getText(), "Incremental Pricing Search","        The Incremental Pricing Search title is displayed");

		logger.info("*** Step 3 Actions: Input data for Pricing ID and Pricing Name and click on Reset button");
		Prc priInfo = prcDao.getListOfIncrPricing();
		String priID = priInfo.getPrcAbbrev();
		String priName = priInfo.getDescr();
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchPriID(), 5),"        The Incremental Pricing Search Pricing ID field is shown");
		incrementalPricingSearch.inputInPriSearchPriID(priID);
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchPriName(), 5),"        The Incremental Pricing Search Pricing Name field is shown");
		incrementalPricingSearch.inputInPriSearchPriName(priName);
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchResetBtn(), 5),"        The Incremental Pricing Search Reset button is shown");
		incrementalPricingSearch.clickInPriSearchResetBtn();

		logger.info("*** Step 3 Expected Results: The data has just inputted be clear");
		assertEquals(incrementalPricingSearch.inPriSearchPriID().getText(), "","        The Incremental Pricing Search Pricing ID field is cleared");
		assertEquals(incrementalPricingSearch.inPriSearchPriName().getText(), "","        The Incremental Pricing Search Pricing Name field is cleared");
	}

	@Test(priority = 1, description ="Search Pricing without input data")
	public void testXPR_420() throws Exception {
		logger.info("===== Testing - testXPR_420 =====");

		logger.info("*** Step 1 Actions: Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: The Load Incremental Pricing table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		incrementalPricingSearch = new IncrementalPricingSearch(driver);
		incrementalPricingSearchResults = new IncrementalPricingSearchResults(driver, config);
		assertTrue(isElementPresent(incrementalPricing.inPriLoadTableTitle(), 5),"        The Load Incremental Pricing table page is shown");
		assertEquals(incrementalPricing.inPriLoadTableTitle().getText(), "Load Incremental Price Table","        The Load Incremental Pricing table page is displayed with correct page title");

		logger.info("*** Step 2 Actions: Click on Incremental Pricing Search icon");
		assertTrue(isElementPresent(incrementalPricing.inPriSearchIconBtn(), 5),"        The Incremental Pricing Search icon button is shown");
		incrementalPricing.clickInPriSearchIconBtn();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: The Incremental Pricing ID search page is displayed");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchTitle(), 5),"        The Incremental Pricing Search title is shown");
		assertEquals(incrementalPricingSearch.inPriSearchTitle().getText(), "Incremental Pricing Search","        The Incremental Pricing Search title is displayed");

		logger.info("*** Step 3 Actions: Without input data for Pricing ID and Pricing Name and click on Search button");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchSearchBtn(), 5),"        The Incremental Pricing Search Search button is shown");
		incrementalPricingSearch.clickInPriSearchSearchBtn();

		logger.info("*** Step 3 Expected Results: The Error Message 'At least one field must be used to initiate a search' is displayed");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchValidErrMessContent(), 5),"        The Incremental Pricing Search Validation Error is shown");
		assertEquals(incrementalPricingSearch.inPriSearchValidErrMessContent().getText(), "At least one field must be used to initiate a search.","        The Incremental Pricing Search Validation Error message is displayed");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchValidErrMessCloseIconBtn(), 5),"        The Incremental Pricing Search Validation Error Close icon button is shown");
		clickHiddenPageObject(incrementalPricingSearch.inPriSearchValidErrMessCloseIconBtn(), 0);
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchCloseBtn(), 5),"        The Incremental Pricing Search Close button is shown");
		clickHiddenPageObject(incrementalPricingSearch.inPriSearchCloseBtn(),0);
	}

	@Test(priority = 1, description ="Search Pricing - Verify Close button")
	public void testXPR_421() throws Exception {
		logger.info("===== Testing - testXPR_421 =====");

		logger.info("*** Step 1 Actions: Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: The Load Incremental Pricing table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		incrementalPricingSearch = new IncrementalPricingSearch(driver);
		incrementalPricingSearchResults = new IncrementalPricingSearchResults(driver, config);
		assertTrue(isElementPresent(incrementalPricing.inPriLoadTableTitle(), 5),"        The Load Incremental Pricing table page is shown");
		assertEquals(incrementalPricing.inPriLoadTableTitle().getText(), "Load Incremental Price Table","        The Load Incremental Pricing table page is displayed with correct page title");

		logger.info("*** Step 2 Actions: Click on Incremental Pricing Search icon");
		assertTrue(isElementPresent(incrementalPricing.inPriSearchIconBtn(), 5),"        The Incremental Pricing Search icon button is shown");
		incrementalPricing.clickInPriSearchIconBtn();
		String parent = switchToPopupWin();
		String searchUrl = driver.getCurrentUrl();

		logger.info("*** Step 2 Expected Results: The Incremental Pricing ID search page is displayed");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchTitle(), 5),"        The Incremental Pricing Search title is shown");
		assertEquals(incrementalPricingSearch.inPriSearchTitle().getText(), "Incremental Pricing Search","        The Incremental Pricing Search title is displayed");

		logger.info("*** Step 3 Actions: Click on Close button");
		assertTrue(isElementPresent(incrementalPricingSearch.inPriSearchCloseBtn(), 5),"        The Incremental Pricing Search Search button is shown");
		incrementalPricingSearch.clickInPriSearchCloseBtn();
		switchToParentWin(parent);

		logger.info("*** Step 3 Expected Results: The Incremental Pricing Search page is closed");
		String currentUrl = driver.getCurrentUrl();
		assertNotEquals(currentUrl, searchUrl,"        The Incremental Pricing Search page is closed");
	}
	/*Rule*/
	@Test(priority = 1, description ="Load IPT - input PricingID with special character")
	public void testXPR_413() throws Exception {
		logger.info("===== Testing - testXPR_413 =====");

		logger.info("*** Step 1 Actions: Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: The Load Incremental Pricing table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		incrementalPricingSearch = new IncrementalPricingSearch(driver);
		incrementalPricingSearchResults = new IncrementalPricingSearchResults(driver, config);
		assertTrue(isElementPresent(incrementalPricing.inPriLoadTableTitle(), 5),"        The Load Incremental Pricing table page is shown");
		assertEquals(incrementalPricing.inPriLoadTableTitle().getText(), "Load Incremental Price Table","        The Load Incremental Pricing table page is displayed with correct page title");

		logger.info("*** Step 2 Actions: Input non-existing PricingID with special characters is '#' and tab out");
		randomCharacter = new RandomCharacter();
		String priID = randomCharacter.getRandomAlphaNumericString(3)+"#";
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Incremental Pricing Pricing ID field is shown");
		incrementalPricing.inputInPriID(priID);

		logger.info("*** Step 2 Expected Results: The Error Message 'Invalid Pricing ID: xxx#' is displayed");
		assertTrue(isElementPresent(incrementalPricing.inPriWarningMessContent(), 5),"        The Incremental Pricing Warning Message is shown");
		assertEquals(incrementalPricing.inPriWarningMessContent().getText(), "Invalid pricing ID: "+priID+".","        The Incremental Pricing Search title is displayed");

		assertTrue(isElementPresent(incrementalPricing.inPriWarningMessCloseIconBtn(), 5),"        The Incremental Pricing Warning Message Close Icon button is shown");
		incrementalPricing.clickInPriWarningMessCloseIconBtn();
	}
	
	@Test(priority = 1, description = "Add Test Rule - Add quantity with require fields are empty")
	public void testXPR_445() throws Exception {
		logger.info("===== Testing - XPR_445 =====");

		logger.info("*** Step 1 Actions: Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: Incremental Pricing page title displays");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Pricing page title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Pricing'");

		logger.info("*** Step 2 Actions: Input new Pricing ID. Tab out");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing ID input should be displayed");
		randomCharacter = new RandomCharacter(driver);
		String priceID = randomCharacter.getRandomAlphaNumericString(6);
		incrementalPricing.checkInputPricingId(priceID,10);

		logger.info("*** Step 2 Expected Results: The Incremental Pricing page is displays without information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Pricing ID label should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Pricing name should be displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().equalsIgnoreCase(priceID),"        Pricing ID label should be equal priceID loaded input");

		logger.info("*** Step 3 Actions: Rule - Quantity : Click on add icon - input Quantity to, Price and leave Quantity from as blank and Click ok button");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		incrementalPricing.inputPricingName(randomCharacter.getRandomAlphaNumericString(6));
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblLeftBtn(0), 5),"        Add quantity button should be displayed");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblLeftBtn(0), 0);
		int qTo = incrementalPricingUtils.getNonZeroRandomNumber(2);
		String qPrice = randomCharacter.getRandomNumericString(2);

		assertTrue(isElementPresent(incrementalPricing.quantityToInput(), 5),"        Quantity To input should be displayed");
		incrementalPricing.inputQuantityTo(Integer.toString(qTo));
		assertTrue(isElementPresent(incrementalPricing.price(), 5),"        Price input should be displayed");
		incrementalPricing.inputPrice(qPrice);
		assertTrue(isElementPresent(incrementalPricing.quantityFromInput(), 5),"        Quantity From input should be displayed");
		incrementalPricing.inputQuantityFrom("");
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Submit button should be displayed");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 3 Expected Results: Error message 'Quantity From: Field is required' should show.");
		assertTrue(isElementPresent(incrementalPricing.tableRuleQuantity(0), 5),"        Table Quantity should be displayed");
		assertTrue(incrementalPricing.errorMessagePopupLeft().getText().trim().contains("Quantity From: Field is required"),"        Error message 'Quantity From: Field is required' should show.");

		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description = "Add Test rule - Edit quantity with valid input")
	public void testXPR_446() throws Exception {
		logger.info("===== Testing - XPR_446 =====");

		logger.info("*** Step 1 Action: login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected result: The Load Incremental Price Table page is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Price page is displayed");

		logger.info("*** Step 2 Action: input non-existing PricingID");
		randomCharacter = new RandomCharacter(driver);
		String pricingID = "ID" + randomCharacter.getRandomNumericString(4);
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
		assertTrue(isElementPresent(incrementalPricing.quantityFromInput(), 5),"        Quantity from input should show");
		incrementalPricing.inputQuantityFrom(Integer.toString(quantity));
		assertTrue(isElementPresent(incrementalPricing.quantityToInput(), 5),"        Quantity to input should show");
		incrementalPricing.inputQuantityTo(Integer.toString(quantityTo));
		assertTrue(isElementPresent(incrementalPricing.price(), 5),"        Price input should be displayed");
		incrementalPricing.inputPrice(Integer.toString(price));
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Ok button should show.");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);
		incrementalPricing.addMultiInclusionExclusion(1,0,this,daoManagerXifinRpm);

		logger.info("*** Step 4 Expected result: new quantity is added into the table");
		assertTrue(getColumnValue(incrementalPricing.quantityTbl(rule), Integer.toString(quantityTo)), "        New Quantity is added into the table");

		logger.info("*** Step 5 Action: click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button should show.");
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);

		logger.info("*** Step 5 Expected result: New pricing Id is save	with corresponding quantity and Test id");
		Assert.assertNotNull(prcDao.getPrcByPrcAbbrev(pricingID.toUpperCase()));

		logger.info("*** Step 6 Action: Load Pricing Id has just created again");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id input is displayed");
		incrementalPricing.inputInPriID(pricingID);

		logger.info("*** Step 6 Expected result: The Incremental Price page is displayed with correct information");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        The Incremental Price page is displayed");
		assertTrue(isElementPresent(incrementalPricing.ruleSection(0), 5),"        Rule 1 is displayed");

		logger.info("*** Step 7 Action: Select any row , Click edit button , updated valid data for some field and Click Ok button");
		testCode = new TestCode(driver, config, wait);
		selectColumnValue(incrementalPricing.quantityTbl(rule), Integer.toString(quantityTo));
		assertTrue(isElementPresent(incrementalPricing.quantityFromInput(), 5),"        Quantity input should show");
		assertTrue(isElementPresent(incrementalPricing.quantityToInput(), 5),"        Quantity to input should show");
		assertTrue(isElementPresent(incrementalPricing.price(), 5),"        Price input should be displayed");
		incrementalPricing.clearData(incrementalPricing.quantityFromInput());
		incrementalPricing.inputQuantityFrom(Integer.toString(quantity+1));
		incrementalPricing.clearData(incrementalPricing.quantityToInput());
		int qTo2 = quantityTo+1;
		incrementalPricing.inputQuantityTo(Integer.toString(qTo2));
		incrementalPricing.clearData(incrementalPricing.price());
		incrementalPricing.inputPrice(Integer.toString(price+1));
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Ok button should show.");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 7 Expected result: New Quantity is added into the table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleQuantity(rule), Integer.toString(qTo2)), "        New Quantity is added into the table");

		logger.info("*** Step 8 Action: Click on submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button should show.");
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);

		logger.info("*** Step 8 Expected result: The update data is save into the DB");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id input is displayed");
		incrementalPricing.inputInPriID(pricingID);
		assertTrue(getColumnValue(incrementalPricing.tableRuleQuantity(rule), Integer.toString(qTo2)), "        New Quantity is added into the table");

		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description = "Add Test Rule - Delete quantity")
	public void testXPR_447() throws Exception {
		logger.info("===== Testing - XPR_447 =====");

		logger.info("*** Step 1 Action: login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected result: The Load Incremental Price Table page is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Price page is displayed");

		logger.info("*** Step 2 Action: input non-existing PricingID");
		randomCharacter = new RandomCharacter(driver);
		String pricingID = "ID" + randomCharacter.getRandomNumericString(4);
		incrementalPricing.checkInputPricingId(pricingID,10);

		logger.info("*** Step 2 Expected result: The Incremental page is displayed without information");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        The Incremental Price page is displayed");
		assertTrue(isElementPresent(incrementalPricing.ruleSection(0), 5),"        Rule 1 is displayed");
		assertTrue(incrementalPricing.pricingName().getText().trim().contains(""),"        The Incremental Price Page is displayed without information");

		logger.info("*** Step 3 Actions: At Inclusion & Exclusion table : Add Inclusion & Exclusion id");
		incrementalPricing.inputPricingName(pricingID);
		List<String> listData = incrementalPricing.addMultiInclusionExclusion(1,0,this,daoManagerXifinRpm);

		logger.info("*** Step 3 Expected result: Add record popup is displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), listData.get(0)), "        The Test Id " + listData.get(0) + " should display in the table");

		logger.info("*** Step 4 Actions: Rule - Quantity : add new quantity with valid data");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		incrementalPricing.clickAddNewRuleTblLeftBtn(0);
		int quantity = incrementalPricingUtils.getNonZeroRandomNumber(1)+1;
		int quantityTo = quantity+1;
		int price = incrementalPricingUtils.getNonZeroRandomNumber(2);
		assertTrue(isElementPresent(incrementalPricing.quantityFromInput(), 5),"        Quantity input should show");
		incrementalPricing.inputQuantityFrom(Integer.toString(quantity));
		assertTrue(isElementPresent(incrementalPricing.quantityToInput(), 5),"        Quantity to input should show");
		incrementalPricing.inputQuantityTo(Integer.toString(quantityTo));
		assertTrue(isElementPresent(incrementalPricing.price(), 5),"        Price input should be displayed");
		incrementalPricing.inputPrice(Integer.toString(price));
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Ok button should show.");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 4 Expected result: new quantity is added into the table");
		assertTrue(getColumnValue(incrementalPricing.quantityTbl(0), Integer.toString(quantityTo)), "        New Quantity is added into the table");

		logger.info("*** Step 5 Action: click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button should show.");
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);

		logger.info("*** Step 5 Expected result: New pricing Id is save	with corresponding quantity and Test id");
		Assert.assertNotNull(prcDao.getPrcByPrcAbbrev(pricingID.toUpperCase()));

		logger.info("*** Step 6 Action: Load Pricing Id has just created again");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id input is displayed");
		incrementalPricing.checkInputPricingId(pricingID,10);

		logger.info("*** Step 6 Expected result: The Incremental Price page is displayed with correct information");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        The Incremental Price page is displayed");
		assertTrue(isElementPresent(incrementalPricing.ruleSection(0), 5),"        Rule 1 is displayed");

		logger.info("*** Step 7 Action: Select any row , Click edit button , Check on delete checkbox and Click ok button");
		selectColumnValue(incrementalPricing.quantityTbl(0), Integer.toString(quantityTo));
		selectCheckBox(incrementalPricing.delCheckbox());
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Ok button should show.");
		incrementalPricing.clickOkButton();

		logger.info("*** Step 7 Expected result: The selected row marked as delete with delete checkbox is checked");
		assertTrue(incrementalPricingUtils.checkDeleteRowInTable(incrementalPricing.serviceTypeCellInRuleQuantityTable(0,2), "rowMarkedForDelete"),"       The selected proc code will be marked as delete row with delete checkbox is check in table");

		logger.info("*** Step 8 Action: Click on submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button should show.");
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);

		logger.info("*** Step 8 Expected result: Data should deleted");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id input is displayed");
		incrementalPricing.inputInPriID(pricingID);
		assertFalse(getColumnValue(incrementalPricing.quantityTbl(0), Integer.toString(quantityTo)), "        Data should deleted");

		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description = "Add Test Rule - verify unit is change when user select quantity type")
	public void testXPR_448() throws Exception {
		logger.info("===== Testing - XPR_448 =====");

		logger.info("*** Step 1 Action: login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected result: The Load Incremental Price Table page is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Price page is displayed");

		logger.info("*** Step 2 Action: input non-existing PricingID");
		randomCharacter = new RandomCharacter(driver);
		String pricingID = "ID" + randomCharacter.getRandomNumericString(4);
		incrementalPricing.checkInputPricingId(pricingID,10);

		logger.info("*** Step 2 Expected result: The Incremental page is displayed without information");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        The Incremental Price page is displayed");
		assertTrue(isElementPresent(incrementalPricing.ruleSection(0), 5),"        Rule 1 is displayed");
		assertTrue(incrementalPricing.pricingName().getText().trim().contains(""),"        The Incremental Price Page is displayed without information");

		logger.info("*** Step 3 Actions: At Rule - Quantity : Select Quantity type is Ordered Tests");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		selectItem(incrementalPricing.quantityTypeDropdown(0),"Ordered Tests");

		logger.info("*** Step 3 Expected result: At Rule - Quantity : The fifth column will change label to 'Per Test'");
		assertTrue(incrementalPricing.fifthColumnTblLeft(0).getText().trim().contains("Per Test"),"        The label will change to 'Per Test'");

		logger.info("*** Step 4 Actions: At Rule - Quantity : Click on add icon");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblLeftBtn(0), 5),"        Add new 'Rule - Quantity' button should show");
		incrementalPricing.clickAddNewRuleTblLeftBtn(0);

		logger.info("*** Step 4 Expected result: Add record popup is displayed with 'Per Test' checkbox");
		assertTrue(isElementPresent(incrementalPricing.addNewRulePopup(0), 5),"        Add New Rule Popup should show.");
		assertTrue(incrementalPricing.isPerUnitlbl().getText().trim().contains("Per Test"),"        Label should be 'Per Test'");

		logger.info("*** Step 5 Actions: Click on Cancel button and Change Quantity type to 'Per Unit'");
		assertTrue(isElementPresent(incrementalPricing.cancelBtn(), 5),"        Cancel button should show.");
		incrementalPricing.clickCancelBtn();
		selectItem(incrementalPricing.quantityTypeDropdown(0),"Per Unit");

		logger.info("*** Step 5 Expected result: At Rule - Quantity : The fifth column will change label to 'Per Unit'");
		assertTrue(incrementalPricing.fifthColumnTblLeft(0).getText().trim().contains("Per Unit"),"        The label will change to 'Per Unit'");

		logger.info("*** Step 6 Actions: At Rule - Quantity : Click on add icon");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblLeftBtn(0), 5),"        Add new 'Rule - Quantity' button should show");
		incrementalPricing.clickAddNewRuleTblLeftBtn(0);

		logger.info("*** Step 6 Expected result: Add record popup is displayed with 'Per Test' checkbox");
		assertTrue(isElementPresent(incrementalPricing.addNewRulePopup(0), 5),"        Add New Rule Popup should show.");
		assertTrue(incrementalPricing.isPerUnitlbl().getText().trim().contains("Per Unit"),"        Label should be 'Per Unit'");

		incrementalPricing.clickCancelBtn();
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description = "Add Proc Rule - Add Duplicate Service type")
	public void testXPR_456() throws Exception {
		logger.info("===== Testing - testXPR_456 =====");

		logger.info("*** Step 1 Actions: Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: The load Incremental Price table is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price table title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Pricing'");

		logger.info("*** Step 2 Actions: Input new pricing Id ");
		randomCharacter = new RandomCharacter(driver);
		String newPricingId = randomCharacter.getRandomAlphaNumericString(6);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id Input field should show");
		incrementalPricing.checkInputPricingId(newPricingId,10);

		logger.info("*** Step 2 Expected Results: The Incremental Price table page is displayed without information");
		assertTrue(isElementPresent(incrementalPricing.incrementalPricingName(), 5),"        incremental Pricing Name should show");
		assertTrue(incrementalPricing.incrementalPricingName().getAttribute("value").isEmpty(),"       The Incremental Price table page is displayed without information");

		logger.info("*** Step 3 Actions: Input Pricing Name - Inclusion & Exclusion : check on procedure radio button");
		String pricingName ="testXPR456" + randomCharacter.getRandomAlphaString(5);
		incrementalPricing.inputIncrementalPricingName(pricingName);
		assertTrue(isElementPresent(incrementalPricing.procInclusionExclusionRadioBtn(), 5),"        Procedure Inclusion/Exclusion Radio button should show");
		selectCheckBox(incrementalPricing.procInclusionExclusionRadioBtn());

		logger.info("*** Step 3 Expected Results: Table column label will change");
		assertTrue(isElementPresent(incrementalPricing.procCodeHeaderInclusionExclusionTable(0), 5),"        Procedure code Inclusion/Exclusion header should show");
		assertTrue(incrementalPricing.procCodeHeaderInclusionExclusionTable(0).getText().equalsIgnoreCase("Proc Code"),"        Table column label will change to ProcCode Name");
		assertTrue(isElementPresent(incrementalPricing.serviceTypHeaderInclusionExclusionTable(0), 5),"        Service Type Inclusion/Exclusion header should show");
		assertTrue(incrementalPricing.serviceTypHeaderInclusionExclusionTable(0).getText().equalsIgnoreCase("Service Type"),"        Table column label will change to Service Type");

		logger.info("*** Step 4 Action: Click on Add icon, Input Service Type");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add new Inclusion/Exclusion button should show");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		String serviceType = serviceDao.getServiceTypFromSvcTypAndProcCd().getDescr();
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.serviceTypeDropdown(),serviceType);
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        ok button should show");
		incrementalPricing.clickOkButton();

		logger.info("*** Step 4 Expected Results: New proc Information is added into the table with");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), serviceType),"       New Proc Code is added in table");

		logger.info("*** Step 5 Actions : Click add icon again - input same Service Type - Click ok button");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add new Inclusion/Exclusion button should show");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.serviceTypeDropdown(),serviceType);
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        ok button should show");
		incrementalPricing.clickOkButton();

		logger.info("*** Step 5 Expected Results : New Proc Code is added in table - the error message is displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), serviceType),"       New Proc Code is added in table");
		assertTrue(incrementalPricing.errorMessageSection().getText().trim().contains("A Service type can only be included in one rule per Incremental Table"),"        The error message 'A Service type can only be included in one rule per Incremental Table. Service type Radiology is in Rule 1.'");

		incrementalPricing.clickResetIncrPricingBtn();
	}

	@Test(priority = 1, description = "Add Rule - Delete Rule")
	public void testXPR_457() throws Exception {
		logger.info("===== Testing - testXPR_457 =====");

		logger.info("*** Step 1 Actions: Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: The load Incremental Price table is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price table title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Pricing'");

		logger.info("*** Step 2 Actions: Input new pricing Id ");
		randomCharacter = new RandomCharacter(driver);
		String newPricingId = randomCharacter.getRandomAlphaNumericString(6);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id Input field should show");
		incrementalPricing.checkInputPricingId(newPricingId,10);

		logger.info("*** Step 2 Expected Results: The Incremental Price table page is displayed without information");
		assertTrue(isElementPresent(incrementalPricing.incrementalPricingName(), 5),"        incremental Pricing Name should show");
		assertTrue(incrementalPricing.incrementalPricingName().getAttribute("value").isEmpty(),"       The Incremental Price table page is displayed without information");

		logger.info("*** Step 3 Actions: Input Pricing Name - Inclusion & Exclusion : Add new data and Click submit button");
		String pricingName ="testXPR457" + randomCharacter.getRandomAlphaString(5);
		incrementalPricing.inputIncrementalPricingName(pricingName);
		List<String> listData = incrementalPricing.addMultiInclusionExclusion(2, 0, this, daoManagerXifinRpm);
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button should show");
		incrementalPricing.clickSubmitIncrPricingBtn();

		logger.info("*** Step 3 Expected Results: New Pricing Id is save into Database");
		Assert.assertNotNull(prcDao.getPrcByPrcAbbrev(newPricingId.toUpperCase()));

		logger.info("*** Step 4 Actions: Load Pricing ID has just created");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id Input field should show");
		incrementalPricing.inputInPriID(newPricingId);

		logger.info("*** Step 4 Expected Results: The Pricing table is load with correct information");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), listData.get(0)), "        New data is added into the table");

		logger.info("*** Step 5 Actions : Click on delete Rule icon");
		incrementalPricing.deleteRuleLink(0).click();

		logger.info("*** Step 5 Expected Results : The confirmation message is displayed");
		assertTrue(isElementPresent(incrementalPricing.deleteARuleDialog(), 5),"        Delete a rule dialog is displayed");

		logger.info("*** Step 6 Actions : Click on Ok button");
		incrementalPricing.clickOkButtonDeletePopUp();

		logger.info("*** Step 6 Expected Results : The rule section is not displayed for Pricing ID");
		assertNull(incrementalPricing.verifyEmptyData(0),"        The selected rule is deleted");

		logger.info("*** Step 7 Actions : Click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button should show");
		incrementalPricing.clickSubmitIncrPricingBtn();

		logger.info("*** Step 7 Expected Results : The rule is remove out of the PricingID");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id Input field should show");
		incrementalPricing.inputInPriID(newPricingId);
		assertNull(incrementalPricing.verifyEmptyData(0),"        The selected rule is deleted");

		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description = "Reset Add new Rule")
	public void testXPR_458()throws Exception {
		logger.info("===== Testing - testXPR_458 =====");

		logger.info("*** Step 1 Actions: Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: The load Incremental Price table is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price table title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Pricing'");

		logger.info("*** Step 2 Actions: Input new pricing Id ");
		randomCharacter = new RandomCharacter(driver);
		String newPricingId = randomCharacter.getRandomAlphaNumericString(6);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id Input field should show");
		incrementalPricing.checkInputPricingId(newPricingId,10);

		logger.info("*** Step 2 Expected Results: The Incremental Price table page is displayed without information");
		assertTrue(isElementPresent(incrementalPricing.incrementalPricingName(), 5),"        incremental Pricing Name should show");
		assertTrue(incrementalPricing.incrementalPricingName().getAttribute("value").isEmpty(),"       The Incremental Price table page is displayed without information");

		logger.info("*** Step 3 Actions: Input Pricing Name and Click Add New Rule button");
		String pricingName ="testXPR458" + randomCharacter.getRandomAlphaString(5);
		incrementalPricing.inputIncrementalPricingName(pricingName);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleBtn(), 5),"        Add New Rule Button should show");
		incrementalPricing.clickAddNewRuleBtn();

		logger.info("*** Step 3 Expected Results: New Rule section is displayed");
		assertTrue(isElementPresent(incrementalPricing.ruleSection(1), 5),"         New Rule Section should show");

		logger.info("*** Step 4 Actions: Inclusion & Exclusion - Click add icon, select Test Type and click OK button");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(1), 5),"        Add New Rule button should show");
		incrementalPricing.clickAddNewRuleTblRightBtn(1);
		String testType = testDao.getTestTyp().getAbbrev();
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.testTypeDropdown(),testType);
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        ok button should show");
		incrementalPricing.clickOkButton();

		logger.info("*** Step 4 Expected Results: New data is added to table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(1), testType), "        New data is added into the table");

		logger.info("*** Step 5 Actions : Click on delete Rule icon");
		assertTrue(isElementPresent(incrementalPricing.resetIncrPricingBtn(), 5),"        Reset button should show");
		incrementalPricing.clickResetIncrPricingBtn();

		logger.info("*** Step 5 Expected Results : System take user return Load Incremental Price table page and new Rule is not save to DB");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id Input field should show");
		incrementalPricing.checkInputPricingId(newPricingId,10);
		assertNull(incrementalPricing.verifyEmptyData(0),"        The selected rule is deleted");
		incrementalPricing.clickResetIncrPricingBtn();
	}

	@Test(priority = 1, description = "add Test rule with duplicate testID for both rules")
	public void testXPR_459() throws Exception {
		logger.info("===== Testing - testXPR_459 =====");

		logger.info("*** Step 1 Actions: Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: The load Incremental Price table is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price table title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Pricing'");

		logger.info("*** Step 2 Actions: Input new pricing Id ");
		randomCharacter = new RandomCharacter(driver);
		String newPricingId = randomCharacter.getRandomAlphaNumericString(6);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id Input field should show");
		incrementalPricing.checkInputPricingId(newPricingId,10);

		logger.info("*** Step 2 Expected Results: The Incremental Price table page is displayed without information");
		assertTrue(isElementPresent(incrementalPricing.incrementalPricingName(), 5),"        incremental Pricing Name should show");
		assertTrue(incrementalPricing.incrementalPricingName().getAttribute("value").isEmpty(),"       The Incremental Price table page is displayed without information");

		logger.info("*** Step 3 Actions: Input Pricing Name - Inclusion & Exclusion : Click on Add icon");
		String pricingName ="testXPR459" + randomCharacter.getRandomAlphaString(5);
		incrementalPricing.inputIncrementalPricingName(pricingName);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add new rule button should show");
		incrementalPricing.clickAddNewRuleTblRightBtn(0);

		logger.info("*** Step 3 Expected Results: Add new popup is displayed");
		assertTrue(isElementPresent(incrementalPricing.addNewInclusionExclusionPopup(0), 5),"        Add new rule popup should show");

		logger.info("*** Step 4 Actions: Input testID and click Ok button");
		String data = incrementalPricingUtils.getTestHaveTestType(daoManagerXifinRpm).get(0);
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test Id Input field should show");
		incrementalPricing.inputTestId(data);
		incrementalPricing.clickOkButton();

		logger.info("*** Step 4 Expected Results: New data is added to table with include radio button is checked");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), data), "        New data is added into the table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), "Include"), "        'Include' should display in the table");

		logger.info("*** Step 5 Actions : Click on Add New Rule Button");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleBtn(), 5),"        Add New Rule Button should show");
		incrementalPricing.clickAddNewRuleBtn();

		logger.info("*** Step 5 Expected Results: New Rule section is displayed");
		assertTrue(isElementPresent(incrementalPricing.ruleSection(1), 5),"         New Rule Section should show");

		logger.info("*** Step 6 Actions : Click on Add icon , Add the same test Id at step 4 and Click Ok button");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(1), 5),"        Add new rule button should show");
		incrementalPricing.clickAddNewRuleTblRightBtn(1);
		incrementalPricing.inputTestId(data);
		incrementalPricing.clickOkButton();

		logger.info("*** Step 6 Expected Results : New information added to table and Error message is displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(1), data), "        New data is added into the table");
		assertTrue(incrementalPricing.errorMessageSection().getText().trim().contains("A Test can only be included in one rule per Incremental Table. Test "+data+" is in Rule 1 and Rule 2."),"        The error message 'A Test can only be included in one rule per Incremental Table. Test "+data+" is in Rule 1 and Rule 2.'");

		incrementalPricing.clickResetIncrPricingBtn();
	}

	@Test(priority = 1, description = "Add new rule without Include")
	public void testXPR_460() throws Exception {
		logger.info("===== Testing - testXPR_460 =====");

		logger.info("*** Step 1 Actions: Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: The load Incremental Price table is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price table title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Pricing'");

		logger.info("*** Step 2 Actions: Input new pricing Id ");
		randomCharacter = new RandomCharacter(driver);
		String newPricingId = randomCharacter.getRandomAlphaNumericString(6);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id Input field should show");
		incrementalPricing.checkInputPricingId(newPricingId,10);

		logger.info("*** Step 2 Expected Results: The Incremental Price table page is displayed without information");
		assertTrue(isElementPresent(incrementalPricing.incrementalPricingName(), 5),"        incremental Pricing Name should show");
		assertTrue(incrementalPricing.incrementalPricingName().getAttribute("value").isEmpty(),"       The Incremental Price table page is displayed without information");

		logger.info("*** Step 3 Actions: Input Pricing Name - Click Add New Rule button");
		String pricingName ="testXPR460" + randomCharacter.getRandomAlphaString(10);
		incrementalPricing.inputIncrementalPricingName(pricingName);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleBtn(), 5),"        Add New Rule Button should show");
		incrementalPricing.clickAddNewRuleBtn();

		logger.info("*** Step 3 Expected Results: New Rule section is displayed");
		assertTrue(isElementPresent(incrementalPricing.ruleSection(1), 5),"         New Rule Section should show");

		logger.info("*** Step 4 Actions: Input Pricing Name - Inclusion & Exclusion : Add new data, select Excluded checkbox and Click submit button");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add new rule button should show");
		incrementalPricing.clickAddNewRuleTblRightBtn(1);
		String data = incrementalPricingUtils.getTestHaveTestType(daoManagerXifinRpm).get(0);
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test Id Input field should show");
		incrementalPricing.inputTestId(data);
		assertTrue(isElementPresent(incrementalPricing.excludeTypeDropDown(), 5),"        Include/Exclude/Require dropdown should be displayed.");
		selectItemByVal(incrementalPricing.excludeTypeDropDown(), "Exclude");
		incrementalPricing.clickOkButton();

		logger.info("*** Step 4 Expected Results: New Excluded is added to table with Exclude radio is checked");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(1), data), "        New data is added into the table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(1), "Exclude"), "        'Exclude' should be displayed in the table.");
		assertTrue(incrementalPricing.errorMessageSection().getText().trim().contains("At least 1 Include row is required in the Inclusion & Exclusion grid for Rule 1"),"        The error message 'At least 1 Include row is required in the Inclusion & Exclusion grid for Rule 1'");

		incrementalPricing.clickResetIncrPricingBtn();
	}

	@Test(priority = 1, description = "Add Rule - Add duplicate include Service type")
	public void testXPR_462() throws Exception {
		logger.info("===== Testing - testXPR_462 =====");

		logger.info("*** Step 1 Actions: Log into Xifin Portal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: The load Incremental Price table is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Price table title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Pricing'");

		logger.info("*** Step 2 Actions: Input new pricing Id ");
		randomCharacter = new RandomCharacter(driver);
		String newPricingId = randomCharacter.getRandomAlphaNumericString(6);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing Id Input field should show");
		incrementalPricing.checkInputPricingId(newPricingId,10);

		logger.info("*** Step 2 Expected Results: The Incremental Price table page is displayed without information");
		assertTrue(isElementPresent(incrementalPricing.incrementalPricingName(), 5),"        incremental Pricing Name should show");
		assertTrue(incrementalPricing.incrementalPricingName().getAttribute("value").isEmpty(),"       The Incremental Price table page is displayed without information");

		logger.info("*** Step 3 Actions: Input Pricing Name - Inclusion & Exclusion 1: Check Procedure radio");
		String pricingName ="testXPR462" + randomCharacter.getRandomAlphaString(5);
		incrementalPricing.inputIncrementalPricingName(pricingName);
		selectCheckBox(incrementalPricing.basicProcCheckboxTblRight(0));

		logger.info("*** Step 3 Expected Results: label is changed");
		assertTrue(incrementalPricing.procCodeColtblRight().getText().trim().contains("Proc Code"),"        Label should be 'Proc Code'");

		logger.info("*** Step 4 Actions: Click on Add icon, select Service Type and Click ok button");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add New Rule button should show");
		incrementalPricing.clickAddNewRuleTblRightBtn(0);
		String serviceType = serviceDao.getServiceTypFromSvcTypAndProcCd().getDescr();
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.serviceTypeDropdown(),serviceType);
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        ok button should show");
		incrementalPricing.clickOkButton();

		logger.info("*** Step 4 Expected Results: new data is added to table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), serviceType), "        New data is added into the table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), "Include"), "        'Include' should display in the table");

		logger.info("*** Step 5 Actions : Click on Add New Rule icon");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleBtn(), 5),"        Add New Rule Button should show");
		incrementalPricing.clickAddNewRuleBtn();

		logger.info("*** Step 5 Expected Results: New Rule section is displayed");
		assertTrue(isElementPresent(incrementalPricing.ruleSection(1), 5),"         New Rule Section should show");

		logger.info("*** Step 6 Actions : Rule 2 Section : Check on Procedure radio, click Add icon, select same Service Type at step 4 and Click ok button");
		selectCheckBox(incrementalPricing.basicProcCheckboxTblRight(1));
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(1), 5),"        Add New Rule button should show");
		incrementalPricing.clickAddNewRuleTblRightBtn(1);
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.serviceTypeDropdown(),serviceType);
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        ok button should show");
		incrementalPricing.clickOkButton();

		logger.info("*** Step 6 Expected Results: Error message is displayed");
		assertTrue(incrementalPricing.errorMessageSection().getText().trim().contains("A Service type can only be included in one rule per Incremental Table."),"        The error message 'A Service type can only be included in one rule per Incremental Table. Service type Pathology is in Rule 1 and Rule 2.'");

		incrementalPricing.clickResetIncrPricingBtn();
	}

	@Test(priority = 1, description = "Add Test Rule - Add duplicate test type")
	public void testXPR_436() throws Exception {
		logger.info("===== Testing - XPR_436 =====");

		logger.info("*** Step 1 Actions: Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: Incremental Pricing page title displays");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Pricing page title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Pricing'");

		logger.info("*** Step 2 Actions: Input new Pricing ID. Tab out");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing ID input should be displayed");
		randomCharacter = new RandomCharacter(driver);
		String priceID = randomCharacter.getRandomAlphaNumericString(6);
		incrementalPricing.checkInputPricingId(priceID,10);

		logger.info("*** Step 2 Expected Results: The Incremental Pricing page is displays without information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Pricing ID label should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Pricing name should be displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().equalsIgnoreCase(priceID),"        Pricing ID label should be equal priceID loaded input");
		assertTrue(isElementPresent(incrementalPricing.basicTestCheckboxTblRight(0), 5),"        Basic test radio should be displayed");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add new rule button should be displayed");

		logger.info("*** Step 3 Actions: Enter pricing name. At Rule one - Inclusion & Exclusion table. Check test radio. Click Add icon");
		incrementalPricing.inputPricingName(randomCharacter.getRandomAlphaNumericString(6));
		clickHiddenPageObject(incrementalPricing.basicTestCheckboxTblRight(0),0);
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0),0);

		logger.info("*** Step 3 Expected Results: An add record popup is displayed");
		assertTrue(isElementPresent(incrementalPricing.addNewInclusionExclusionPopup(0), 5),"        Add new popup should be displayed");
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test ID input should be displayed");
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Submit button should be displayed");

		logger.info("*** Step 4 Actions: Select test type. Click ok button");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		List<String> testCode = incrementalPricingUtils.getTestHaveTestType(daoManagerXifinRpm);
		String testType = testCode.get(3);
		assertTrue(isElementPresent(incrementalPricing.testTypeDropdown(), 5),"        Test Type dropdown should be displayed");
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.testTypeDropdown(), testType);
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 4 Expected Results: New test type added to table");
		assertTrue(isElementPresent(incrementalPricing.tableRuleTest(0), 5),"        Table rule test should be displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), testType),"        Test type should be add to table");

		logger.info("*** Step 5 Actions: Add again test type the same step 4. Click ok button");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0),0);
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.testTypeDropdown(), testType);
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 5 Expected Results: Test type excluded added to table and error message");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), testType),"        Test type should be add to table");
		assertTrue(incrementalPricing.errorMessageSection().getText().equalsIgnoreCase("A Test type can only be included in one rule per Incremental Table. Test type "+testType+" is in Rule 1."),"        Error message must be display correct");

		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description = "Add Test Rule - Add test ID with invalid TestID")
	public void testXPR_437() throws Exception {
		logger.info("===== Testing - XPR_437 =====");

		logger.info("*** Step 1 Actions: Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: Incremental Pricing page title displays");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Pricing page title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Pricing'");

		logger.info("*** Step 2 Actions: Input new Pricing ID. Tab out");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing ID input should be displayed");
		randomCharacter = new RandomCharacter(driver);
		String priceID = randomCharacter.getRandomAlphaNumericString(6);
		incrementalPricing.checkInputPricingId(priceID,10);

		logger.info("*** Step 2 Expected Results: The Incremental Pricing page is displays without information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Pricing ID label should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Pricing name should be displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().equalsIgnoreCase(priceID),"        Pricing ID label should be equal priceID loaded input");
		assertTrue(isElementPresent(incrementalPricing.basicTestCheckboxTblRight(0), 5),"        Basic test radio should be displayed");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add new rule button should be displayed");

		logger.info("*** Step 3 Actions: Add invalid test ID");
		incrementalPricing.inputPricingName(randomCharacter.getRandomAlphaNumericString(6));
		clickHiddenPageObject(incrementalPricing.basicTestCheckboxTblRight(0),0);
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0),0);
		assertTrue(isElementPresent(incrementalPricing.testIdInput(), 5),"        Test ID input should be displayed");
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Submit button should be displayed");
		String invalidID = randomCharacter.getRandomAlphaNumericString(10);
		incrementalPricing.inputTestId(invalidID);
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 3 Expected Results: Error message test ID invalid");
		assertTrue(isElementPresent(incrementalPricing.addRecordMessError(), 5),"        Error message should be displayed");
		assertTrue(incrementalPricing.addRecordMessError().getText().equalsIgnoreCase("Test ID '"+invalidID+"' : does not exist"),"        Error message should be displayed correct");

		clickHiddenPageObject(incrementalPricing.cancelBtn(), 0);
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description = "Add Test Rule - Cancel new TestID")
	public void testXPR_438() throws Exception {
		logger.info("===== Testing - XPR_438 =====");

		logger.info("*** Step 1 Actions: Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: Incremental Pricing page title displays");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Pricing page title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Pricing'");

		logger.info("*** Step 2 Actions: Input new Pricing ID. Tab out");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing ID input should be displayed");
		randomCharacter = new RandomCharacter(driver);
		String priceID = randomCharacter.getRandomAlphaNumericString(6);
		incrementalPricing.checkInputPricingId(priceID,10);

		logger.info("*** Step 2 Expected Results: The Incremental Pricing page is displays without information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Pricing ID label should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Pricing name should be displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().equalsIgnoreCase(priceID),"        Pricing ID label should be equal priceID loaded input");
		assertTrue(isElementPresent(incrementalPricing.basicTestCheckboxTblRight(0), 5),"        Basic test radio should be displayed");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add new rule button should be displayed");

		logger.info("*** Step 3 Actions: Add test type. Click cancel button");
		incrementalPricing.inputPricingName(randomCharacter.getRandomAlphaNumericString(6));
		clickHiddenPageObject(incrementalPricing.basicTestCheckboxTblRight(0),0);
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0),0);
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Submit button should be displayed");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		List<String> testCode = incrementalPricingUtils.getTestHaveTestType(daoManagerXifinRpm);
		String testType = testCode.get(3);
		assertTrue(isElementPresent(incrementalPricing.testTypeDropdown(), 5),"        Test Type dropdown should be displayed");
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.testTypeDropdown(), testType);
		assertTrue(isElementPresent(incrementalPricing.excludeTypeDropDown(), 5),"        Include/Exclude/Require dropdown should be displayed.");
		selectItemByVal(incrementalPricing.excludeTypeDropDown(), "Exclude");
		clickHiddenPageObject(incrementalPricing.cancelBtn(), 0);

		logger.info("*** Step 3 Expected Results: Add record close and test type not add to table");
		assertTrue(isElementHidden(incrementalPricing.addNewInclusionExclusionPopup(0), 5),"        Add new popup should be close");
		assertFalse(getColumnValue(incrementalPricing.tableRuleTest(0), testType),"        Test type should be not add to table");

		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description = "Update Inclusion & Exclusion with valid data")
	public void testXPR_439() throws Exception {
		logger.info("===== Testing - XPR_439 =====");

		logger.info("*** Step 1 Actions: Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: Incremental Pricing page title displays");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Pricing page title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Pricing'");

		logger.info("*** Step 2 Actions: Input new Pricing ID. Tab out");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing ID input should be displayed");
		randomCharacter = new RandomCharacter(driver);
		String priceID = randomCharacter.getRandomAlphaNumericString(6);
		incrementalPricing.checkInputPricingId(priceID,10);

		logger.info("*** Step 2 Expected Results: The Incremental Pricing page is displays without information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Pricing ID label should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Pricing name should be displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().equalsIgnoreCase(priceID),"        Pricing ID label should be equal priceID loaded input");
		assertTrue(isElementPresent(incrementalPricing.basicTestCheckboxTblRight(0), 5),"        Basic test radio should be displayed");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add new rule button should be displayed");

		logger.info("*** Step 3 Actions: Add test ID. Click OK button");
		incrementalPricing.inputPricingName(randomCharacter.getRandomAlphaString(6));
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		List<String> testCode = daoManagerXifinRpm.getSingleTest(testDb);
		String testID = testCode.get(1);
		incrementalPricing.inputTestId(testID);
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 3 Expected Results: A new test added to table");
		assertTrue(isElementPresent(incrementalPricing.tableRuleTest(0), 5),"        Table rule test should be displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), testID),"        Test ID should be add to table");

		logger.info("*** Step 4 Actions: Click submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        Submit button should be displayed");
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);

		logger.info("*** Step 4 Expected Results: System take user return to load pricing ID page");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing ID input should be displayed");

		logger.info("*** Step 5 Actions: Input new pricing has just created");
		incrementalPricing.checkInputPricingId(priceID,10);

		logger.info("*** Step 5 Expected Results: The Incremental Page display with correct information");
		assertTrue(incrementalPricing.pricingIdLbl().getText().equalsIgnoreCase(priceID),"        Pricing ID label should be equal priceID loaded input");
		assertTrue(isElementPresent(incrementalPricing.tableRuleTest(0), 5),"        Table rule test should be displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), testID),"        Test ID should be add to table");

		logger.info("*** Step 6 Actions: Select any row in Inclusion and Exclusion table to edit");
		selectColumnValue(incrementalPricing.tableRuleTest(0), testID);

		logger.info("*** Step 6 Expected Results: Edit popup display");
		assertTrue(isElementPresent(incrementalPricing.addNewInclusionExclusionPopup(0), 5),"        Edit popup should be displayed");

		logger.info("*** Step 7 Actions: Select other test type and check Exclusion checkbox. Click OK");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		testCode = incrementalPricingUtils.getTestHaveTestType(daoManagerXifinRpm);
		String testType = testCode.get(3);
		assertTrue(isElementPresent(incrementalPricing.testTypeDropdown(), 5),"        Test Type dropdown should be displayed");
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.testTypeDropdown(), testType);
		assertTrue(isElementPresent(incrementalPricing.excludeTypeDropDown(), 5),"        Include/Exclude/Require dropdown should be displayed.");
		selectItemByVal(incrementalPricing.excludeTypeDropDown(), "Exclude");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 7 Expected Results: Test type update to table and Exclusion checkbox checked");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), "Exclude"), "        'Exclude' should be displayed in the table.");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), testType),"        Test type should be add to table");

		logger.info("*** Step 8 Actions: Add inclusion test and submit all");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		incrementalPricing.inputTestId(testID);
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);
		Thread.sleep(1000);
		incrementalPricing.checkInputPricingId(priceID,10);

		logger.info("*** Step 8 Expected Results: All information saved");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), "Exclude"), "        'Exclude' should be displayed in the table.");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), testType),"        Test type should be add to table");

		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description = "Add quantity with quantity is overlap")
	public void testXPR_442() throws Exception {
		logger.info("===== Testing - XPR_442 =====");

		logger.info("*** Step 1 Actions: Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: Incremental Pricing page title displays");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Pricing page title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Pricing'");

		logger.info("*** Step 2 Actions: Input new Pricing ID. Tab out");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing ID input should be displayed");
		randomCharacter = new RandomCharacter(driver);
		String priceID = randomCharacter.getRandomAlphaNumericString(6);
		incrementalPricing.checkInputPricingId(priceID,10);

		logger.info("*** Step 2 Expected Results: The Incremental Pricing page is displays without information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Pricing ID label should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Pricing name should be displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().equalsIgnoreCase(priceID),"        Pricing ID label should be equal priceID loaded input");
		assertTrue(isElementPresent(incrementalPricing.basicTestCheckboxTblRight(0), 5),"        Basic test radio should be displayed");

		logger.info("*** Step 3 Actions: Add new Quantity. Click OK button");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblLeftBtn(0), 0);
		String qFrom = randomCharacter.getRandomNumericString(2);
		String qTo = String.valueOf(Integer.parseInt(qFrom)+1);
		String qPrice = randomCharacter.getRandomNumericString(2);
		assertTrue(isElementPresent(incrementalPricing.quantityFromInput(), 5),"        Quantity From input should be displayed");
		incrementalPricing.inputQuantityFrom(qFrom);
		assertTrue(isElementPresent(incrementalPricing.quantityToInput(), 5),"        Quantity To input should be displayed");
		incrementalPricing.inputQuantityTo(qTo);
		assertTrue(isElementPresent(incrementalPricing.price(), 5),"        Price input should be displayed");
		incrementalPricing.inputPrice(qPrice);
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Submit button should be displayed");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 3 Expected Results: A new quantity added to table");
		assertTrue(isElementPresent(incrementalPricing.tableRuleQuantity(0), 5),"        Table Quantity should be displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleQuantity(0), qFrom),"        Quantity should be add to table");

		logger.info("*** Step 4 Actions: Add new Quantity with quantity from < quantity to step 3");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblLeftBtn(0), 0);
		String qFrom2 = String.valueOf(Integer.parseInt(qTo)-1);
		incrementalPricing.inputQuantityFrom(qFrom2);
		incrementalPricing.inputPrice(qPrice);
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 4 Expected Results: Verify error message");
		assertTrue(isElementPresent(incrementalPricing.addRecordMessError(), 5),"        Error message should be displayed");
		assertTrue(incrementalPricing.addRecordMessError().getText().equalsIgnoreCase("Quantity From : Overlapping ranges are not allowed."),"        Error message must be displayed correct");

		clickHiddenPageObject(incrementalPricing.cancelBtn(), 0);
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description = "Add Test rule - Add quantity with invalid data")
	public void testXPR_444() throws Exception {
		logger.info("===== Testing - XPR_444 =====");

		logger.info("*** Step 1 Actions: Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: Incremental Pricing page title displays");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Pricing page title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Pricing'");

		logger.info("*** Step 2 Actions: Input new Pricing ID. Tab out");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing ID input should be displayed");
		randomCharacter = new RandomCharacter(driver);
		String priceID = randomCharacter.getRandomAlphaNumericString(6);
		incrementalPricing.checkInputPricingId(priceID,10);

		logger.info("*** Step 2 Expected Results: The Incremental Pricing page is displays without information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Pricing ID label should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Pricing name should be displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().equalsIgnoreCase(priceID),"        Pricing ID label should be equal priceID loaded input");
		assertTrue(isElementPresent(incrementalPricing.basicTestCheckboxTblRight(0), 5),"        Basic test radio should be displayed");

		logger.info("*** Step 3 Actions: Add new Quantity with invalid Quantity From");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblLeftBtn(0), 0);
		String qFrom = randomCharacter.getRandomAlphaString(5);
		String qTo = "10";
		String qPrice = randomCharacter.getRandomNumericString(2);
		assertTrue(isElementPresent(incrementalPricing.quantityFromInput(), 5),"        Quantity From input should be displayed");
		incrementalPricing.inputQuantityFrom(qFrom);
		assertTrue(isElementPresent(incrementalPricing.quantityToInput(), 5),"        Quantity To input should be displayed");
		incrementalPricing.inputQuantityTo(qTo);
		assertTrue(isElementPresent(incrementalPricing.price(), 5),"        Price input should be displayed");
		incrementalPricing.inputPrice(qPrice);
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"        Submit button should be displayed");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 3 Expected Results: Verify error message quantity from format");
		assertTrue(isElementPresent(incrementalPricing.addRecordMessError(), 5),"        Error message should be displayed");
		assertTrue(incrementalPricing.addRecordMessError().getText().equalsIgnoreCase("Quantity From : Invalid numeric format."),"        Error message must be displayed correct");

		logger.info("*** Step 4 Actions: Add with invalid format Quantity To");
		testCode = new TestCode(driver, config, wait);
		qFrom = "10";
		qTo = randomCharacter.getRandomAlphaString(5);
		incrementalPricing.clearData(incrementalPricing.quantityFromInput());
		incrementalPricing.clearData(incrementalPricing.quantityToInput());
		incrementalPricing.inputQuantityFrom(qFrom);
		incrementalPricing.inputQuantityTo(qTo);
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 4 Expected Results: Verify error message quantity to format");
		assertTrue(isElementPresent(incrementalPricing.addRecordMessError(), 5),"        Error message should be displayed");
		assertTrue(incrementalPricing.addRecordMessError().getText().equalsIgnoreCase("Quantity To : Invalid numeric format."),"        Error message must be displayed correct");

		logger.info("*** Step 5 Actions: Add with invalid format Price");
		qTo = "11";
		qPrice = randomCharacter.getRandomAlphaStringwithSpecialCharacters(6);
		incrementalPricing.clearData(incrementalPricing.price());
		incrementalPricing.clearData(incrementalPricing.quantityToInput());
		incrementalPricing.inputQuantityTo(qTo);
		incrementalPricing.inputPrice(qPrice);
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 5 Expected Results: Verify the price field doesn't entered");
		assertEquals(getInputVal(incrementalPricing.price()), "", "The price field isn't empty with price: " + getInputVal(incrementalPricing.price()));
		
		clickHiddenPageObject(incrementalPricing.cancelBtn(), 0);
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description = "Add duplicate include procedure code")
	public void testXPR_461() throws Exception {
		logger.info("===== Testing - XPR_461 =====");

		logger.info("*** Step 1 Actions: Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: Incremental Pricing page title displays");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 5),"        The Incremental Pricing page title should be displayed");
		assertTrue(incrementalPricing.inPriTitle().getText().trim().contains("Incremental Price Table"),"        Page Title should be 'Incremental Pricing'");

		logger.info("*** Step 2 Actions: Input new Pricing ID. Tab out");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Pricing ID input should be displayed");
		randomCharacter = new RandomCharacter(driver);
		String priceID = randomCharacter.getRandomAlphaNumericString(6);
		incrementalPricing.checkInputPricingId(priceID,10);

		logger.info("*** Step 2 Expected Results: The Incremental Pricing page is displays without information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        Pricing ID label should be displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Pricing name should be displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().equalsIgnoreCase(priceID),"        Pricing ID label should be equal priceID loaded input");
		assertTrue(isElementPresent(incrementalPricing.basicProcCheckboxTblRight(0), 5),"        Procedure radio should be displayed");

		logger.info("*** Step 3 Actions: Enter pricing name. Select Procedure checkbox");
		incrementalPricing.inputPricingName(randomCharacter.getRandomAlphaNumericString(6));
		selectCheckBox(incrementalPricing.basicProcCheckboxTblRight(0));

		logger.info("*** Step 3 Expected Results: Column label will be change");
		assertTrue(isElementPresent(incrementalPricing.procIDColLabelIncludExcludTable(0), 5),"        Proc Code label should be displayed");
		assertTrue(incrementalPricing.procIDColLabelIncludExcludTable(0).getText().equalsIgnoreCase("Proc Code"),"        Column label change to Proc Code");

		logger.info("*** Step 4 Actions: Add valid proc code");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        Add new rule button should be displayed");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		String procCode = daoManagerXifinRpm.getProcedureCodeId(testDb).get(0);
		assertTrue(isElementPresent(incrementalPricing.procIdInput(), 5),"        Proc code input should be displayed");
		incrementalPricing.inputProcId(procCode);
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 4 Expected Results: New proc code added to table");
		assertTrue(isElementPresent(incrementalPricing.tableRuleTest(0), 5),"        Proc Code table should be displayed");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), procCode),"        Proc code should be add to table");

		logger.info("*** Step 5 Actions: Click add new Rule button");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleBtn(), 5),"        Add new rule button should be displayed");
		clickHiddenPageObject(incrementalPricing.addNewRuleBtn(),0);

		logger.info("*** Step 5 Expected results: New rule section add display");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(1), 5),"        Add new in table Rule 2 should be displayed");

		logger.info("*** Step 6 Actions: Add the same proc step 4.");
		assertTrue(isElementPresent(incrementalPricing.basicProcCheckboxTblRight(1), 5),"        Procedure radio should be displayed");
		selectCheckBox(incrementalPricing.basicProcCheckboxTblRight(1));
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(1), 0);
		assertTrue(isElementPresent(incrementalPricing.procIdInput(), 5),"        Proc code input should be displayed");
		incrementalPricing.inputProcId(procCode);
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 6 Actions: New proc add to Rule 2 and error message.");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(1), procCode),"        Proc code should be add to table");
		assertTrue(incrementalPricing.errorMessageSection().getText().equalsIgnoreCase("A Procedure code can only be included in one rule per Incremental Table. Procedure code "+procCode+" is in Rule 1 and Rule 2."),"        Error message must be display correct");

		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	//Incremental Price Tbl
	@Test(priority = 1, description = "Add new Incremental Price input inclusion - Exclusion only")
	public void testXPR_423() throws Exception {
		logger.info("===== Testing - testXPR_423 =====");

		logger.info("*** Step 1 Actions: Log to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Result: The Load Incremental Pricing table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 10), "        The Incremental page should be displayed");
		assertEquals(incrementalPricing.inPriTitle().getText(),"Incremental Price Table", "        The Incremental Pricing page should be displayed");

		logger.info("*** Step 2 Actions: Input Non-existing Incremental Pricing ID");
		randomCharacter = new RandomCharacter();
		String newPriID = "ID"+randomCharacter.getRandomNumericString(5);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Pricing Pricing ID field is shown");
		incrementalPricing.inputInPriID(newPriID);

		logger.info("*** Step 2 Expected Result: The Incremental Pricing page is displayed without information");
		assertTrue(isElementPresent(incrementalPricing.pricingName(),5), "        The Pricing Name is shown");

		logger.info("*** Step 3 Actions: Input Name value - Click on Add icon at Inclusion / Exclusion table");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5), "        Pricing Name Input should be displayed");
		incrementalPricing.inputPricingName(newPriID+" 'S EXAMPLE");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        The Incremental Pricing Add New Rule icon is shown");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);

		logger.info("*** Step 3 Expected Result: Add Record popup is displayed");
		switchToPopupWin();
		assertTrue(isElementPresent(incrementalPricing.addNewInclusionExclusionPopup(0), 10), "        The popup Add Record is shown");

		logger.info("*** Step 4 Actions: Select any Test Type - Check on Exclusion check box - Click on OK");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		String testType = testDao.getTestTyp().getAbbrev();
		assertTrue(isElementPresent(incrementalPricing.testTypeDropdown(), 10), "        TestType drop down list is shown");
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.testTypeDropdown(), testType);
		assertTrue(isElementPresent(incrementalPricing.excludeTypeDropDown(), 5),"        Include/Exclude/Require dropdown should be displayed.");
		selectItemByVal(incrementalPricing.excludeTypeDropDown(), "Exclude");
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 10), "        OK button is shown");
		incrementalPricing.clickOkButton();

		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		switchToPopupWin();
		assertTrue(isElementPresent(incrementalPricing.addNewInclusionExclusionPopup(0), 10), "        The popup Add Record is shown");
		String testType2 = testDao.getTestTyp().getAbbrev();
		assertTrue(isElementPresent(incrementalPricing.testTypeDropdown(), 10), "        TestType drop down list is shown");
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.testTypeDropdown(), testType2);
		incrementalPricing.clickOkButton();

		logger.info("*** Step 4 Expected Result: New Exclusion is added into the table");
		assertEquals(incrementalPricing.inclusionTblNumberofRow(1).getText(), "2","        New Exclusion is added into the table");

		logger.info("*** Step 5 Actions: Click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 10), "        Submit button is shown");
		incrementalPricing.clickSubmitIncrPricingBtn();

		logger.info("*** Step 5 Expected Result: New Incremental Pricing ID is saved into the Database with Exclusion information only (without information about quantity)");
		incrementalPricing.inputInPriID(newPriID);
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), "Exclude"), "        'Exclude' should be displayed in the table.");
		assertTrue(isElementPresent(incrementalPricing.resetIncrPricingBtn(), 10),"        Reset button is shown");
		incrementalPricing.clickResetIncrPricingBtn();
	}

	@Test(priority = 1, description = "Add new Incremental without input name")
	public void testXPR_424() throws Exception {
		logger.info("===== Testing - testXPR_424 =====");

		logger.info("*** Step 1 Actions: Log to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Result: The Load Incremental Pricing table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 10), "        The Incremental page should be displayed");
		assertEquals(incrementalPricing.inPriTitle().getText(),"Incremental Price Table", "        The Incremental Pricing page should be displayed");

		logger.info("*** Step 2 Actions: Input Non-existing Incremental Pricing ID");
		randomCharacter = new RandomCharacter();
		String newPriID = "ID"+randomCharacter.getRandomNumericString(4);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Pricing Pricing ID field is shown");
		incrementalPricing.checkInputPricingId(newPriID,10);

		logger.info("*** Step 2 Expected Result: The Incremental Pricing page is displayed without information");
		assertEquals(incrementalPricing.pricingName().getAttribute("value"), "", "        The pricing name should be empty");

		logger.info("*** Step 3 Actions: Without input pricing Name - Click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 10), "        Submit button is shown");
		incrementalPricing.clickSubmitIncrPricingBtn();

		logger.info("*** Step 3 Expected Result: The error message 'Incremental pricing name' is required)");
		assertTrue(isElementPresent(incrementalPricing.warningPopupForPricingName(), 10), "        Warning popup is shown");
		assertEquals(incrementalPricing.warningPopupForPricingName().getText(), "Incremental Pricing Name is required.","        Waring popup is displayed with Incremental Pricing Name is required");

		assertTrue(isElementPresent(incrementalPricing.resetIncrPricingBtn(), 10),"        Reset button is shown");
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description = "Add new Incremental Price without Inclusion - Exclusion information")
	public void testXPR_425() throws Exception {
		logger.info("===== Testing - testXPR_425 =====");

		logger.info("*** Step 1 Actions: Log to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Result: The Load Incremental Pricing table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 10), "        The Incremental page should be displayed");
		assertEquals(incrementalPricing.inPriTitle().getText(),"Incremental Price Table", "        The Incremental Pricing page should be displayed");

		logger.info("*** Step 2 Actions: Input Non-existing Incremental Pricing ID");
		randomCharacter = new RandomCharacter();
		String newPriID = "ID"+randomCharacter.getRandomNumericString(5);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Pricing Pricing ID field is shown");
		incrementalPricing.checkInputPricingId(newPriID,10);

		logger.info("*** Step 2 Expected Result: The Incremental Pricing page is displayed without information");
		assertEquals(incrementalPricing.pricingName().getAttribute("value"), "", "        The pricing name should be empty");

		logger.info("*** Step 3 Actions: Input Name value - Click on Add icon at Inclusion / Exclusion table");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5), "        Pricing Name Input should be displayed");
		incrementalPricing.inputPricingName(newPriID+" 'S EXAMPLE");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblLeftBtn(0), 5),"        The Incremental Pricing Add New Rule icon is shown");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblLeftBtn(0), 0);

		logger.info("*** Step 3 Expected Result: Add Record popup is displayed");
		switchToPopupWin();
		assertTrue(isElementPresent(incrementalPricing.addNewRulePopup(0), 10), "        The popup Add Record is shown");

		logger.info("*** Step 4 Actions: Add Quantity From, Quantity To, Price - Click on OK button");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		int quantityFrom = incrementalPricingUtils.getNonZeroRandomNumber(3);
		int quantityTo = incrementalPricingUtils.getNonZeroRandomNumber(4);
		int price = incrementalPricingUtils.getNonZeroRandomNumber(3);

		assertTrue(isElementPresent(incrementalPricing.quantityFromInput(), 10), "         Quantity From input is shown");
		incrementalPricing.inputQuantityFrom(""+quantityFrom);
		assertTrue(isElementPresent(incrementalPricing.quantityToInput(), 10), "         Quantity To input is shown");
		incrementalPricing.inputQuantityTo(""+quantityTo);
		assertTrue(isElementPresent(incrementalPricing.price(), 10), "         Price input is shown");
		incrementalPricing.inputPrice(""+price);
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 10), "        OK button is shown");
		incrementalPricing.clickOkButton();

		logger.info("*** Step 4 Expected Result: New Quantity is added into the table");
		assertEquals(incrementalPricing.quantityTblNumberofRow(0).getText(), "(1)","        New Quantity is added into the table");

		logger.info("*** Step 5 Actions: Click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 10), "        Submit button is shown");
		incrementalPricing.clickSubmitIncrPricingBtn();

		logger.info("*** Step 5 Expected Result: The error message 'At least 1 Include row is required in the Inclusion & Exclusion grid for Rule 1'");
		assertTrue(isElementPresent(incrementalPricing.errorMessageSection(), 10), "        Error Message section is shown");
		assertEquals(incrementalPricing.errorMessageSection().getText(), "At least 1 Include row is required in the Inclusion & Exclusion grid for Rule 1", "       The error message should be mapped with expected message");

		assertTrue(isElementPresent(incrementalPricing.resetIncrPricingBtn(), 10),"        Reset button is shown");
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description = "Reset New Incremental Pricing ID")
	public void testXPR_427() throws Exception {
		logger.info("===== Testing - testXPR_427 =====");

		logger.info("*** Step 1 Actions: Log to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Result: The Load Incremental Pricing table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 10), "        The Incremental page should be displayed");
		assertEquals(incrementalPricing.inPriTitle().getText(),"Incremental Price Table", "        The Incremental Pricing page should be displayed");

		logger.info("*** Step 2 Actions: Input Non-existing Incremental Pricing ID");
		randomCharacter = new RandomCharacter();
		String newPriID = "ID"+randomCharacter.getRandomNumericString(5);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Pricing Pricing ID field is shown");
		incrementalPricing.checkInputPricingId(newPriID,10);

		logger.info("*** Step 2 Expected Result: The Incremental Pricing page is displayed without information");
		assertEquals(incrementalPricing.pricingName().getAttribute("value"), "", "        The pricing name should be empty");

		logger.info("*** Step 3 Actions: Input Name value - Click on Add icon at Inclusion / Exclusion table");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5), "        Pricing Name Input should be displayed");
		incrementalPricing.inputPricingName(newPriID+" 'S EXAMPLE");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        The Incremental Pricing Add New Rule icon is shown");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);

		logger.info("*** Step 3 Expected Result: Add Record popup is displayed");
		switchToPopupWin();
		assertTrue(isElementPresent(incrementalPricing.addNewInclusionExclusionPopup(0), 10), "        The popup Add Record is shown");

		logger.info("*** Step 4 Actions: Select any Test Type - Check on Exclusion check box - Click on OK");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		String testType = testDao.getTestTyp().getAbbrev();
		assertTrue(isElementPresent(incrementalPricing.testTypeDropdown(), 10), "        TestType drop down list is shown");
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.testTypeDropdown(), testType);
		assertTrue(isElementPresent(incrementalPricing.excludeTypeDropDown(), 5),"        Include/Exclude/Require dropdown should be displayed.");
		selectItemByVal(incrementalPricing.excludeTypeDropDown(), "Exclude");
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 10), "        OK button is shown");
		incrementalPricing.clickOkButton();

		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		switchToPopupWin();
		assertTrue(isElementPresent(incrementalPricing.addNewInclusionExclusionPopup(0), 10), "        The popup Add Record is shown");
		String testType2 = testDao.getTestTyp().getAbbrev();
		assertTrue(isElementPresent(incrementalPricing.testTypeDropdown(), 10), "        TestType drop down list is shown");
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.testTypeDropdown(), testType2);
		incrementalPricing.clickOkButton();

		logger.info("*** Step 4 Expected Result: New Exclusion is added into the table");
		assertEquals(incrementalPricing.inclusionTblNumberofRow(1).getText(), "2","        New Exclusion is added into the table");

		logger.info("*** Step 5 Actions: Click on Reset button");
		assertTrue(isElementPresent(incrementalPricing.resetIncrPricingBtn(), 10), "        Reset button is shown");
		incrementalPricing.clickResetIncrPricingBtn();

		logger.info("*** Step 5 Expected Result: System return the Load Incremental Price table page - new Incremental Pricing ID is not save into the database");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 10), "        Pricing ID input is displayed again");
		incrementalPricing.checkInputPricingId(newPriID,10);
		assertEquals(incrementalPricing.incrementalPricingName().getText(), "", "        The pricing name is empty");

		assertTrue(isElementPresent(incrementalPricing.resetIncrPricingBtn(), 10),"        Reset button is shown");
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description = "Update Incremental Pricing - Delete all rules")
	public void testXPR_430() throws Exception {
		logger.info("===== Testing - testXPR_430 =====");

		logger.info("*** Step 1 Actions: Log to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Result: The Load Incremental Pricing table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 10), "        The Incremental page should be displayed");
		assertEquals(incrementalPricing.inPriTitle().getText(),"Incremental Price Table", "        The Incremental Pricing page should be displayed");

		logger.info("*** Step 2 Actions: Input new Incremental Pricing ID");
		randomCharacter = new RandomCharacter();
		String newPriID = "ID"+randomCharacter.getRandomNumericString(5);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Pricing Pricing ID field is shown");
		incrementalPricing.checkInputPricingId(newPriID,10);

		logger.info("*** Step 2 Expected Result: The Incremental Pricing page is displayed without information");
		assertEquals(incrementalPricing.pricingName().getAttribute("value"), "", "        The pricing name should be empty");

		logger.info("*** Step 3 Actions: Add some rules for new pricing ID");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5), "        Pricing Name Input should be displayed");
		incrementalPricing.inputPricingName(newPriID+" 'S EXAMPLE");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"        The Incremental Pricing Add New Rule icon is shown");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);

		logger.info("*** Step 3 Expected Result: New rules will be displayed");
		switchToPopupWin();
		assertTrue(isElementPresent(incrementalPricing.addNewInclusionExclusionPopup(0), 10), "        The popup Add Record is shown");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		String testType = testDao.getTestTyp().getAbbrev();
		assertTrue(isElementPresent(incrementalPricing.testTypeDropdown(), 10), "        TestType drop down list is shown");
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.testTypeDropdown(), testType);
		assertTrue(isElementPresent(incrementalPricing.excludeTypeDropDown(), 5),"        Include/Exclude/Require dropdown should be displayed.");
		selectItemByVal(incrementalPricing.excludeTypeDropDown(), "Exclude");
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 10), "        OK button is shown");
		incrementalPricing.clickOkButton();

		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		switchToPopupWin();
		assertTrue(isElementPresent(incrementalPricing.addNewInclusionExclusionPopup(0), 10), "        The popup Add Record is shown");
		String testType2 = testDao.getTestTyp().getAbbrev();
		assertTrue(isElementPresent(incrementalPricing.testTypeDropdown(), 10), "        TestType drop down list is shown");
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.testTypeDropdown(), testType2);
		incrementalPricing.clickOkButton();
		assertEquals(incrementalPricing.inclusionTblNumberofRow(1).getText(), "2","        New Exclusion is added into the table");

		logger.info("*** Step 4 Actions: Click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 10), "        Submit button is shown");
		incrementalPricing.clickSubmitIncrPricingBtn();

		logger.info("*** Step 4 Expected Result: New Incremental Pricing ID is saved into the Database - System tell user return load incremental price table page");
		incrementalPricing.checkInputPricingId(newPriID,10);
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), "Exclude"), "        'Exclude' should be displayed in the table.");
		incrementalPricing.clickResetIncrPricingBtn();

		logger.info("*** Step 5 Actions: Load Pricing ID that just created at step 2");
		incrementalPricing.inputInPriID(newPriID);

		logger.info("*** Step 5 Expected Result: The Incremental Price table is displayed with correct information");
		assertEquals(incrementalPricing.incrementalPricingName().getAttribute("value"), newPriID+" 'S EXAMPLE", "        The Incremental Pricing Name should be displayed correctly");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), "Exclude"), "        'Exclude' should be displayed in the table.");

		logger.info("*** Step 6 Actions: Delete all rules of this incremental pricing ID");
		assertTrue(isElementPresent(incrementalPricing.deleteRuleLink(0),10), "        Delete Rule link should be displayed");
		clickHiddenPageObject(incrementalPricing.deleteRuleLink(0), 0);
		incrementalPricing.clickOkButtonDeletePopUp();

		logger.info("*** Step 6 Expected Result: There is no rules displayed for this pricing ID");
		assertFalse(incrementalPricing.ruleSection().getText().equalsIgnoreCase("rule"));

		logger.info("*** Step 7 Actions: Click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 10), "        Submit button is shown");
		incrementalPricing.clickSubmitIncrPricingBtn();

		logger.info("*** Step 7 Expected Result: The Update is saved successfully - System take user return the Load Incremental Price table page");
		assertTrue(isElementPresent(incrementalPricing.inPriTitle(), 10), "        Incremental Price title is shown");
		assertEquals(incrementalPricing.inPriTitle().getText(), "Incremental Price Table", "        The title page should be displayed");

		assertTrue(isElementPresent(incrementalPricing.resetIncrPricingBtn(), 10),"        Reset button is shown");
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description ="Xifin Portal - FM - Pricing - Incremental Price Tbl- Add Proc Rule - Update proc code change Proc Id by Service Type")
	public void testXPR_450() throws Exception {
		logger.info("===== Testing - testXPR_450 =====");

		logger.info("*** Step 1 Actions: Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: The Load Incremental Pricing table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		incrementalPricingSearch = new IncrementalPricingSearch(driver);
		incrementalPricingSearchResults = new IncrementalPricingSearchResults(driver, config);
		assertTrue(isElementPresent(incrementalPricing.inPriLoadTableTitle(), 5),"        The Load Incremental Pricing table page is shown");
		assertEquals(incrementalPricing.inPriLoadTableTitle().getText(), "Load Incremental Price Table","        The Load Incremental Pricing table page is displayed with correct page title");

		logger.info("*** Step 2 Actions: Input valid PricingID and tab out");
		randomCharacter = new RandomCharacter();
		String priID = "PRIID"+randomCharacter.getRandomAlphaString(3);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Incremental Pricing Pricing ID field is shown");
		incrementalPricing.inputInPriID(priID);

		logger.info("*** Step 2 Expected Results: The Incremental Pricing page is displayed with correct information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        The Incremental Pricing Pricing ID label is shown");
		assertEquals(incrementalPricing.pricingIdLbl().getText(),priID ,"        The Incremental Pricing page is displayed with correct information");

		logger.info("*** Step 3 Actions: Input Pricing Name and Check on Procedure radio button");
		String pricingName = priID +" 'S EXAMPLE";
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Pricing Name is displayed");
		incrementalPricing.inputPricingName(pricingName);
		assertTrue(isElementPresent(incrementalPricing.basicProcCheckboxTblRight(0), 5),"       Procedure radio button is shown");
		clickHiddenPageObject(incrementalPricing.basicProcCheckboxTblRight(0), 0);

		logger.info("*** Step 3 Expected Results: The column label will be changed");
		assertTrue(isElementPresent(incrementalPricing.procCodeHeaderInclusionExclusionTable(0), 5),"       Proc Code header is shown");
		assertEquals(incrementalPricing.procCodeHeaderInclusionExclusionTable(0).getText(),"Proc Code" ,"        The column label will be changed to Proc Code");
		assertTrue(isElementPresent(incrementalPricing.serviceTypHeaderInclusionExclusionTable(0), 5),"       Service Type header is shown");
		assertEquals(incrementalPricing.serviceTypHeaderInclusionExclusionTable(0).getText(),"Service Type" ,"        The column label will be changed to Service Type");

		logger.info("*** Step 4 Actions: Click on Add icon");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"       Add button is shown");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);

		logger.info("*** Step 4 Expected Results: Add Record popup is displayed");
		assertTrue(isElementPresent(incrementalPricing.procIdInput(), 5),"       Proc Code field is shown");

		logger.info("*** Step 5 Actions: Input valid Proc Id and check on Excluded check box and click on OK button");
		List<String> procInfo = daoManagerXifinRpm.getProcedureCodeId(testDb);
		String procID = procInfo.get(0);
		incrementalPricing.inputProcId(procID);
		assertTrue(isElementPresent(incrementalPricing.excludeTypeDropDown(), 5),"        Include/Exclude/Require dropdown should be displayed.");
		selectItemByVal(incrementalPricing.excludeTypeDropDown(), "Exclude");
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"       OK button is shown");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		procInfo = daoManagerXifinRpm.getProcedureCodeId(testDb);
		procID = procInfo.get(0);
		incrementalPricing.inputProcId(procID);
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 5 Expected Results: New Procedure information is displayed in the table");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		assertTrue(getColumnValue(incrementalPricing.inclusionTbl(0),procID),"        New Procedure is displayed in the table");

		logger.info("*** Step 6 Actions: Click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5));
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);
		Thread.sleep(2000);

		logger.info("*** Step 6 Expected Results: New Pricing Id is saved into db with procedure information");
		Prc pricingInfo = prcDao.getPrcFromPrcIncrRuleByPrcAbbrev(priID);
		String dbPriID = pricingInfo.getPrcAbbrev();
		assertEquals(dbPriID,priID ,"        New Pricing Id is saved into db with procedure information");

		logger.info("*** Step 7 Actions: Load new Pricing Id has just created again");
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        Load new Pricing Id has just created again");
		incrementalPricing.inputInPriID(priID);

		logger.info("*** Step 7 Expected Results: The Incremental Price page is displayed with correct information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        The Incremental Pricing Pricing ID label is shown");
		assertEquals(incrementalPricing.pricingIdLbl().getText(),priID ,"        The Incremental Pricing page is displayed with correct information");

		logger.info("*** Step 8 Actions: Rule - Inclusion and Exclusion table - Select any row and click on Edit icon");
		assertTrue(isElementPresent(incrementalPricing.valueOfRightTable(0, 2, 6), 5),"        Row in Inclusion and Exclusion table is shown");
		clickHiddenPageObject(incrementalPricing.valueOfRightTable(0, 2, 6), 0);
		assertTrue(isElementPresent(incrementalPricing.editRuleTblRightBtn(0), 5),"        Edit icon is shown");
		clickHiddenPageObject(incrementalPricing.editRuleTblRightBtn(0), 0);

		logger.info("*** Step 8 Expected Results: Edit popup is displayed");
		assertTrue(isElementPresent(incrementalPricing.editRecordHeader(0), 5),"        Edit Record header is shown");
		assertEquals(incrementalPricing.editRecordHeader(0).getText(),"Edit Record" ,"        Edit popup is displayed");

		logger.info("*** Step 9 Actions: Select Service Type from dropdown list");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		String serviceType = serviceDao.getServiceTypFromSvcTypAndProcCd().getDescr();
		assertTrue(isElementPresent(incrementalPricing.serviceTypeDropdown(), 5),"        Service Type dropdown is shown");
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.serviceTypeDropdown(), serviceType);
		Thread.sleep(2000);

		logger.info("*** Step 9 Expected Results: Proc Id is cleared when user select service type");
		assertEquals(incrementalPricing.procIdInput().getText(),"" ,"        Proc Id is cleared");
		assertTrue(isElementPresent(incrementalPricing.cancelBtn(), 5),"       Cancel button is shown");
		clickHiddenPageObject(incrementalPricing.cancelBtn(), 0);
		assertTrue(isElementPresent(incrementalPricing.resetIncrPricingBtn(), 5),"        The Incremental Pricing Reset button is shown");
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description ="Add Proc Rule - Add procedure with invalid data")
	public void testXPR_451() throws Exception {
		logger.info("===== Testing - testXPR_451 =====");

		logger.info("*** Step 1 Actions: Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: The Load Incremental Pricing table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		incrementalPricingSearch = new IncrementalPricingSearch(driver);
		incrementalPricingSearchResults = new IncrementalPricingSearchResults(driver, config);
		assertTrue(isElementPresent(incrementalPricing.inPriLoadTableTitle(), 5),"        The Load Incremental Pricing table page is shown");
		assertEquals(incrementalPricing.inPriLoadTableTitle().getText(), "Load Incremental Price Table","        The Load Incremental Pricing table page is displayed with correct page title");

		logger.info("*** Step 2 Actions: Input valid PricingID and tab out");
		randomCharacter = new RandomCharacter();
		String priID = "PRIID"+randomCharacter.getRandomAlphaString(3);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Incremental Pricing Pricing ID field is shown");
		incrementalPricing.inputInPriID(priID);

		logger.info("*** Step 2 Expected Results: The Incremental Pricing page is displayed with correct information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        The Incremental Pricing Pricing ID label is shown");
		assertEquals(incrementalPricing.pricingIdLbl().getText(),priID ,"        The Incremental Pricing page is displayed with correct information");

		logger.info("*** Step 3 Actions: Click on Add New Rule button");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleBtn(), 5),"        Add New Rule button is shown");
		clickHiddenPageObject(incrementalPricing.addNewRuleBtn(), 0);

		logger.info("*** Step 3 Expected Results: New Rule section is displayed");
		assertTrue(isElementPresent(incrementalPricing.ruleHeaderLeftTop("1"), 5),"       New Rule header is shown");
		assertEquals(incrementalPricing.ruleHeaderLeftTop("1").getText(),"Rule 2" ,"        New Rule is displayed with correct information");

		logger.info("*** Step 4 Actions: Rule - Inclusion and Exclusion table - Check on Procedure radio button");
		assertTrue(isElementPresent(incrementalPricing.basicProcCheckboxTblRight(1), 5),"       Procedure radio button is shown");
		clickHiddenPageObject(incrementalPricing.basicProcCheckboxTblRight(1), 0);

		logger.info("*** Step 4 Expected Results: The column label will be changed");
		assertTrue(isElementPresent(incrementalPricing.procCodeHeaderInclusionExclusionTable(1), 5),"       Proc Code header is shown");
		assertEquals(incrementalPricing.procCodeHeaderInclusionExclusionTable(1).getText(),"Proc Code" ,"        The column label will be changed to Proc Code");
		assertTrue(isElementPresent(incrementalPricing.serviceTypHeaderInclusionExclusionTable(1), 5),"       Service Type header is shown");
		assertEquals(incrementalPricing.serviceTypHeaderInclusionExclusionTable(1).getText(),"Service Type" ,"        The column label will be changed to Service Type");

		logger.info("*** Step 5 Actions: Click on Add icon then click on OK button without input data");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(1), 5),"       Add button is shown");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(1), 0);
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"       OK button is shown");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 5 Expected Results: The Error message 'Please fill on of these fields: Procode or Service Type' is displayed");
		assertTrue(isElementPresent(incrementalPricing.addRecordMessError(), 5),"       Add Record Message Error is shown");
		assertEquals(incrementalPricing.addRecordMessError().getText(),"Please fill one of these fields: Proc Code or Service Type" ,"        The Error message 'Please fill on of these fields: Procode or Service Type' is displayed");

		logger.info("*** Step 6 Actions: Input invalid Procode and click on OK button");
		String proCode = "abc";
		assertTrue(isElementPresent(incrementalPricing.procIdInput(), 5),"       Proc Code field is shown");
		incrementalPricing.inputProcId(proCode);
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 6 Expected Results: The error message 'Proc ID xxx: does not exist' is displayed");
		assertEquals(incrementalPricing.addRecordMessError().getText(),"Proc ID '"+proCode+"' : does not exist" ,"        The error message 'Proc ID xxx: does not exit' is displayed");
		assertTrue(isElementPresent(incrementalPricing.cancelBtn(), 5),"       Cancel button is shown");

		clickHiddenPageObject(incrementalPricing.cancelBtn(), 0);
		assertTrue(isElementPresent(incrementalPricing.resetIncrPricingBtn(), 5),"        The Incremental Pricing Reset button is shown");
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description ="Add Proc rule - Add the same Proc Code")
	public void testXPR_453() throws Exception {
		logger.info("===== Testing - testXPR_453 =====");

		logger.info("*** Step 1 Actions: Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: The Load Incremental Pricing table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		incrementalPricingSearch = new IncrementalPricingSearch(driver);
		incrementalPricingSearchResults = new IncrementalPricingSearchResults(driver, config);
		assertTrue(isElementPresent(incrementalPricing.inPriLoadTableTitle(), 5),"        The Load Incremental Pricing table page is shown");
		assertEquals(incrementalPricing.inPriLoadTableTitle().getText(), "Load Incremental Price Table","        The Load Incremental Pricing table page is displayed with correct page title");

		logger.info("*** Step 2 Actions: Input valid PricingID and tab out");
		randomCharacter = new RandomCharacter();
		String priID = "PRIID"+randomCharacter.getRandomAlphaNumericString(3);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Incremental Pricing Pricing ID field is shown");
		incrementalPricing.inputInPriID(priID);

		logger.info("*** Step 2 Expected Results: The Incremental Pricing page is displayed with correct information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        The Incremental Pricing Pricing ID label is shown");
		assertEquals(incrementalPricing.pricingIdLbl().getText(),priID ,"        The Incremental Pricing page is displayed with correct information");

		logger.info("*** Step 3 Actions: Input Pricing Name and Check on Procedure radio button");
		String pricingName = priID +" 'S EXAMPLE";
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Pricing Name is displayed");
		incrementalPricing.inputPricingName(pricingName);
		assertTrue(isElementPresent(incrementalPricing.basicProcCheckboxTblRight(0), 5),"       Procedure radio button is shown");
		clickHiddenPageObject(incrementalPricing.basicProcCheckboxTblRight(0), 0);

		logger.info("*** Step 3 Expected Results: The column label will be changed");
		assertTrue(isElementPresent(incrementalPricing.procCodeHeaderInclusionExclusionTable(0), 5),"       Proc Code header is shown");
		assertEquals(incrementalPricing.procCodeHeaderInclusionExclusionTable(0).getText(),"Proc Code" ,"        The column label will be changed to Proc Code");
		assertTrue(isElementPresent(incrementalPricing.serviceTypHeaderInclusionExclusionTable(0), 5),"       Service Type header is shown");
		assertEquals(incrementalPricing.serviceTypHeaderInclusionExclusionTable(0).getText(),"Service Type" ,"        The column label will be changed to Service Type");

		logger.info("*** Step 4 Actions: Click on Add icon and input valid Proc Id and click on OK button");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5),"       Add button is shown");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);

		List<String> procInfo = daoManagerXifinRpm.getProcedureCodeId(testDb);
		String procID = procInfo.get(0);
		assertTrue(isElementPresent(incrementalPricing.procIdInput(), 5),"       Proc Id field is shown");
		incrementalPricing.inputProcId(procID);
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"       OK button is shown");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 4 Expected Results:  New Proc Code information will be added to table with Include radio button is checked");
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		assertTrue(getColumnValue(incrementalPricing.inclusionTbl(0),procID),"        New Procedure is displayed in the table");
		assertTrue(getColumnValue(incrementalPricing.tableRuleTest(0), "Include"), "        'Include' should display in the table");

		logger.info("*** Step 5 Actions:  Click Add icon again then input same Proc Id at step 4 and check on Excluded check box and click on OK button");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		incrementalPricing.inputProcId(procID);
		assertTrue(isElementPresent(incrementalPricing.excludeTypeDropDown(), 5),"        Include/Exclude/Require dropdown should be displayed.");
		selectItemByVal(incrementalPricing.excludeTypeDropDown(), "Exclude");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 5 Expected Results: New Proc Code information will be added to table with Exclude radio button is checked");
		assertTrue(getColumnValue(incrementalPricing.inclusionTbl(0),procID),"        New Procedure is displayed in the table");
		assertTrue(isElementPresent(incrementalPricing.excludeTypeDropDown(), 5),"        Include/Exclude/Require dropdown should be displayed.");
		selectItemByVal(incrementalPricing.excludeTypeDropDown(), "Exclude");

		logger.info("*** Step 6 Actions: Click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5));
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);

		logger.info("*** Step 6 Expected Results: New Pricing is saved with 2 Proc Codes information");
		int total = daoManagerXifinRpm.getTotalProCodeByPriID(priID, testDb);
		assertEquals(total, 2,"        New Pricing is saved with 2 Proc Codes information");

		assertTrue(isElementPresent(incrementalPricing.resetIncrPricingBtn(), 5),"        The Incremental Pricing Reset button is shown");
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority = 1, description ="Add Proc Rule - Verify user can add duplicate Exclude proc code")
	public void testXPR_455() throws Exception {
		logger.info("===== Testing - testXPR_455 =====");

		logger.info("*** Step 1 Actions: Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Results: The Load Incremental Pricing table page is displayed with correct page title");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		incrementalPricingSearch = new IncrementalPricingSearch(driver);
		incrementalPricingSearchResults = new IncrementalPricingSearchResults(driver, config);
		assertTrue(isElementPresent(incrementalPricing.inPriLoadTableTitle(), 5),"        The Load Incremental Pricing table page is shown");
		assertEquals(incrementalPricing.inPriLoadTableTitle().getText(), "Load Incremental Price Table","        The Load Incremental Pricing table page is displayed with correct page title");

		logger.info("*** Step 2 Actions: Input valid PricingID and tab out");
		randomCharacter = new RandomCharacter();
		String priID = "PRIID"+randomCharacter.getRandomAlphaNumericString(3);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Incremental Pricing Pricing ID field is shown");
		incrementalPricing.checkInputPricingId(priID, 10);

		logger.info("*** Step 2 Expected Results: The Incremental Pricing page is displayed with correct information");
		assertTrue(isElementPresent(incrementalPricing.pricingIdLbl(), 5),"        The Incremental Pricing Pricing ID label is shown");
		assertEquals(incrementalPricing.pricingIdLbl().getText(),priID ,"        The Incremental Pricing page is displayed with correct information");
		String pricingName = priID +" 'S EXAMPLE";
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        Pricing Name is displayed");
		incrementalPricing.inputPricingName(pricingName);

		logger.info("*** Step 3 Actions: Click on Add New Rule button");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleBtn(), 5),"        Add New Rule button is shown");
		clickHiddenPageObject(incrementalPricing.addNewRuleBtn(), 0);

		logger.info("*** Step 3 Expected Results: New Rule section is displayed");
		assertTrue(isElementPresent(incrementalPricing.ruleHeaderLeftTop("1"), 5),"       New Rule header is shown");
		assertEquals(incrementalPricing.ruleHeaderLeftTop("1").getText(),"Rule 2" ,"        New Rule is displayed with correct information");

		logger.info("*** Step 4 Actions: Rule - Inclusion and Exclusion table - Check on Procedure radio button");
		assertTrue(isElementPresent(incrementalPricing.basicProcCheckboxTblRight(1), 5),"       Procedure radio button is shown");
		clickHiddenPageObject(incrementalPricing.basicProcCheckboxTblRight(1), 0);
		assertTrue(isElementPresent(incrementalPricing.basicProcCheckboxTblRight(0), 5),"       Procedure radio button is shown");
		clickHiddenPageObject(incrementalPricing.basicProcCheckboxTblRight(0), 0);

		logger.info("*** Step 4 Expected Results: The column label will be changed");
		assertTrue(isElementPresent(incrementalPricing.procCodeHeaderInclusionExclusionTable(1), 5),"       Proc Code header is shown");
		assertEquals(incrementalPricing.procCodeHeaderInclusionExclusionTable(1).getText(),"Proc Code" ,"        The column label will be changed to Proc Code");
		assertTrue(isElementPresent(incrementalPricing.serviceTypHeaderInclusionExclusionTable(1), 5),"       Service Type header is shown");
		assertEquals(incrementalPricing.serviceTypHeaderInclusionExclusionTable(1).getText(),"Service Type" ,"        The column label will be changed to Service Type");

		logger.info("*** Step 5 Actions:  Click on Add icon then select any Service Type and check on Excluded check box and click on OK button");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(1), 5),"       Add button is shown");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(1), 0);
		incrementalPricingUtils = new IncrementalPricingUtils(driver);
		String serviceType = serviceDao.getServiceTypFromSvcTypAndProcCd().getDescr();
		assertTrue(isElementPresent(incrementalPricing.serviceTypeDropdown(), 5),"        Service Type dropdown is shown");
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.serviceTypeDropdown(), serviceType);
		assertTrue(isElementPresent(incrementalPricing.excludeTypeDropDown(), 5),"        Include/Exclude/Require dropdown should be displayed.");
		selectItemByVal(incrementalPricing.excludeTypeDropDown(), "Exclude");
		assertTrue(isElementPresent(incrementalPricing.okBtn(), 5),"       OK button is shown");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.serviceTypeDropdown(), serviceType);
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 5 Expected Results: New Proc Code information will be added to table with Exclude radio button is checked");
		assertTrue(getColumnValue(incrementalPricing.inclusionTbl(1),serviceType),"        New Service Type is displayed in the table");
		assertTrue(getColumnValue(incrementalPricing.inclusionTbl(1), "Exclude"), "        'Exclude' should be displayed in the table.");

		logger.info("*** Step 6 Actions: Click on Add icon again and select same Service Type and check on Excluded check box and click on OK button");
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(1), 0);
		incrementalPricingUtils.selectDropDownJQGird(incrementalPricing.serviceTypeDropdown(), serviceType);
		assertTrue(isElementPresent(incrementalPricing.excludeTypeDropDown(), 5),"        Include/Exclude/Require dropdown should be displayed.");
		selectItemByVal(incrementalPricing.excludeTypeDropDown(), "Exclude");
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(1), 0);
		List<String> procInfo = daoManagerXifinRpm.getProcedureCodeId(testDb);
		String procID = procInfo.get(0);
		assertTrue(isElementPresent(incrementalPricing.procIdInput(), 5),"       Proc Id field is shown");
		incrementalPricing.inputProcId(procID);
		clickHiddenPageObject(incrementalPricing.okBtn(), 0);

		logger.info("*** Step 6 Expected Results: New Proc Code information will be added to table with Exclude radio button is checked");
		assertTrue(getColumnValue(incrementalPricing.inclusionTbl(1),serviceType),"        New Procedure is displayed in the table");
		assertTrue(getColumnValue(incrementalPricing.inclusionTbl(1), "Exclude"), "        'Exclude' should be displayed in the table.");

		logger.info("*** Step 7 Actions: Click on Submit button");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5));
		clickHiddenPageObject(incrementalPricing.submitIncrPricingBtn(), 0);

		logger.info("*** Step 7 Expected Results: The New Rule is saved with the same Proc information");
		incrementalPricing.checkInputPricingId(priID,10);
		String proc1 = incrementalPricing.valueOfRightTable(1, 2, 8).getText();
		String proc2 = incrementalPricing.valueOfRightTable(1, 3, 8).getText();
		assertEquals(proc1, proc2,"        The New Rule is saved with the same Proc information");

		assertTrue(isElementPresent(incrementalPricing.resetIncrPricingBtn(), 5),"        The Incremental Pricing Reset button is shown");
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	/*Search Proc Code*/
	@Test(priority=1, description="Add Proc Rule - Search Proc Code with valid input")
	public void testXPR_463() throws Exception{
		logger.info("====== Testing - XPR_463 ======");

		logger.info("*** Step 1 Action: login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected result: The Load Incremental Price table page is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Price Table page is displayed");

		logger.info("*** Step 2 Actions: Input valid Pricing ID and tab our");
		randomCharacter = new RandomCharacter(driver);
		String pricingId = "PricingID" +randomCharacter.getRandomNumericString(4);
		incrementalPricing.inputInPriID(pricingId);

		logger.info("*** Step 2 Expected Result: The Incremental Page is displayed");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        The Incremental Price Table page is displayed");
		assertTrue(incrementalPricing.pricingName().getAttribute("value").isEmpty(),"        The Incremental Price table page is displayed without information");

		logger.info("*** Step 3 Actions: Rule - Inclusion & Exclusion: Check on Procedure radio button, Click on Add icon");
		assertTrue(isElementPresent(incrementalPricing.basicProcCheckboxTblRight(0), 5),"         ");
		selectCheckBox(incrementalPricing.basicProcCheckboxTblRight(0));
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);

		logger.info("*** Step 3 Expected result: The Add record popup is displayed");
		assertTrue(isElementPresent(incrementalPricing.procIdInput(), 5),"        The Add record popup is displayed");

		logger.info("*** Step 4 Actions: Click on Procedure Code search icon");
		assertTrue(isElementPresent(incrementalPricing.procCodeSearchIcon(), 5));
		clickHiddenPageObject(incrementalPricing.procCodeSearchIcon(), 0);
		String parentWindow =switchToPopupWin();

		logger.info("*** step 4 Expect result: the Procedure code search page is displayed");
		procCodeSearch = new ProcCodeSearch(driver);
		assertTrue(isElementPresent(procCodeSearch.searchProcedureIdInput(), 5),"        The Search Procedure page is displayed");

		logger.info("*** Step 5 Action: input valid procedure code and click on Search button");
		List<String>  procCodeInfo = daoManagerXifinRpm.getProcedureCodeId(testDb);
		procCodeSearch.searchProcedureCode(procCodeInfo.get(0));

		logger.info("*** step 5 Expected result: the search result is display correct information");
		procCodeSearchResults = new ProcCodeSearchResults(driver);
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(procCodeSearch.searchButtonInSearchScreen(), 5),"        The Search result screen is displayed");
		assertTrue(getColumnValue(procCodeSearchResults.procCodeSearchResultTable(), procCodeInfo.get(0)));
		int totalPrcId = procedureCodeDao.getListProcCdByProcId(procCodeInfo.get(0)).size();
		String totalPrcResult = testCodeUtils.getTotalResultSearch(procCodeSearchResults.rightPagerTableResultSearch());
		assertEquals(totalPrcResult, String.valueOf(totalPrcId),"        The Procedure Codes are match with search condition are displayed");

		procCodeSearch.clickCloseBtn();
		switchToParentWin(parentWindow);
		clickHiddenPageObject(incrementalPricing.cancelBtn(), 0);
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority=1, description="Add Proc Rule - Search Proc Code type")
	public void testXPR_464() throws Exception{
		logger.info("====== Testing - XPR_464 ======");

		logger.info("*** Step 1 Action: Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected result: User login successful");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Price Table page is displayed");

		logger.info("*** Step 2 Action: Input new Pricing ID and tab out");
		randomCharacter = new RandomCharacter(driver);
		String pricingId = "ID" +randomCharacter.getRandomNumericString(4);
		incrementalPricing.inputInPriID(pricingId);

		logger.info("*** Step 2 Expected result: The Incremental Price table is displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        The Pricing Name field is displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().trim().equalsIgnoreCase(pricingId),"         The Incremental Price table is load with correct pricing ID");

		logger.info("*** Step 3 Action: Rule - Inclusion & Exclusion: Check on Procedure radio button, Click on Add icon");
		assertTrue(isElementPresent(incrementalPricing.basicProcCheckboxTblRight(0),5));
		selectCheckBox(incrementalPricing.basicProcCheckboxTblRight(0));

		logger.info("*** step 3 Expected result: the table is change to Proc code table");
		assertEquals(incrementalPricing.procCodeHeaderInclusionExclusionTable(0).getText().trim(), "Proc Code","        the table is change to proc code table");

		logger.info("*** Step 4 Actions: click on add icon and procedure code search icon");
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		assertTrue(isElementPresent(incrementalPricing.procCodeSearchIcon(), 5));
		clickHiddenPageObject(incrementalPricing.procCodeSearchIcon(), 0);
		String parentWindow = switchToPopupWin();

		logger.info("*** Step 4 Expected result: The Procedure Code search page is displayed");
		procCodeSearch = new ProcCodeSearch(driver);
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(procCodeSearch.searchProcTypeDropdown(), 5),"        The Procedure Code search page is displayed");

		logger.info("*** Step 5 Actions: select Procedure Type dropdown list and click on Search button");
		List<String> procCodeInfo = daoManagerXifinRpm.getProcedureCodeId(testDb);
		selectItem(procCodeSearch.searchProcTypeDropdown(), procCodeInfo.get(1));
		clickHiddenPageObject(procCodeSearch.procCodeSearchButton(), 0);

		logger.info("*** step 5 Expected result: The all procedure code is matched search condition will be displayed");
		int searchResultDB = daoManagerXifinRpm.getTotalProcCodeSearchBasedOnProcType(procCodeInfo.get(2), testDb);
		procCodeSearchResults = new ProcCodeSearchResults(driver);
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(procCodeSearchResults.keepSearchOpenCheckbox(), 5),"        The Search result page is displayed");
		assertTrue(getColumnValue(procCodeSearchResults.procCodeSearchResultTable(), procCodeInfo.get(1)),"        Search result page display result for Procedure Code type");
		String totalSearchResult = testCodeUtils.getTotalResultSearch(procCodeSearchResults.rightPagerTableResultSearch()).replace(",", "");
		assertEquals(totalSearchResult, String.valueOf(searchResultDB),"        The all proc codes are match with search condition be displayed");

		procCodeSearchResults.clickCloseSearchBtn();
		switchToParentWin(parentWindow);
		clickHiddenPageObject(incrementalPricing.cancelBtn(), 0);
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority=1, description="Add Proc Rule - Search Proc Code with select Revenue Code")
	public void testXPR_465() throws Exception{
		logger.info("====== Testing - XPR_465 ======");

		logger.info("*** Step 1 Action: login to SSO by valid username & pass");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected result: system take user to Load Incremental Price Tbl page");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Price table page is displayed");

		logger.info("*** Step 2 Actions: Input new Pricing ID and tab out");
		randomCharacter = new RandomCharacter(driver);
		String pricingID = "ID" + randomCharacter.getRandomNumericString(4);
		incrementalPricing.inputInPriID(pricingID);

		logger.info("*** Step 2 Expected result: the Incremental Price table page is displayed");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        The Incremental Price table page is displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().trim().equalsIgnoreCase(pricingID),"        The Incremental Price table is displayed with correct information");

		logger.info("*** Step 3 Actions: Rule - Inclusion & Exclusion: Check on Procedure radio button, Click on Add icon");
		assertTrue(isElementPresent(incrementalPricing.basicProcCheckboxTblRight(0), 5));
		selectCheckBox(incrementalPricing.basicProcCheckboxTblRight(0));
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		assertTrue(isElementPresent(incrementalPricing.procCodeSearchIcon(), 5));
		clickHiddenPageObject(incrementalPricing.procCodeSearchIcon(), 0);
		String parentWindow = switchToPopupWin();

		logger.info("*** Step 3 Expected result: procedure code search page is displayed");
		procCodeSearch = new ProcCodeSearch(driver);
		assertTrue(isElementPresent(procCodeSearch.searchRevenueCodeDropdown(), 5),"        The Procedure Code search is displayed");

		logger.info("*** Step 4 Actions: select Revenue from dropdown list and click on Search button");
		List<String> revenueInfo = daoManagerXifinRpm.getProcCodeSearchBaseOnRevenue(testDb);
		selectItem(procCodeSearch.searchRevenueCodeDropdown(), revenueInfo.get(1));
		assertTrue(isElementPresent(procCodeSearch.procCodeSearchButton(), 5));
		clickHiddenPageObject(procCodeSearch.procCodeSearchButton(), 0);

		logger.info("*** Step 4 Expected result: the search result is display correctly");
		procCodeSearchResults = new ProcCodeSearchResults(driver);
		testCodeUtils = new TestCodeUtils(driver);
		int resultInDb = daoManagerXifinRpm.getTotalProcCodeSearchResultBasedOnRevenue(revenueInfo.get(0), testDb);
		assertTrue(isElementPresent(procCodeSearchResults.keepSearchOpenCheckbox(), 5),"          The search result page ");
		assertTrue(getColumnValue(procCodeSearchResults.procCodeSearchResultTable(), revenueInfo.get(0)),"        The search result is displayed based on selected revenue");
		String totalSearchResult = testCodeUtils.getTotalResultSearch(procCodeSearchResults.rightPagerTableResultSearch()).replace(",", "");
		assertEquals(totalSearchResult, String.valueOf(resultInDb),"        The search result is display correctly");

		clickHiddenPageObject(procCodeSearchResults.closeSearchBtn(), 0);
		switchToParentWin(parentWindow);
		clickHiddenPageObject(incrementalPricing.cancelBtn(), 0);
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority=1,description="Add Proc Rule - Search Proc Code with select service type")
	public void testXPR_466() throws Exception{
		logger.info("====== Testing - XPR_466 ======");

		logger.info("*** Step 1 Action: login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected Result: The Load Incremental Price Table page is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Price Table page is displayed");

		logger.info("*** Step 2 Actions: Input new Pricing ID and tab out");
		randomCharacter = new RandomCharacter(driver);
		String pricingId = "ID" + randomCharacter.getRandomNumericString(4);
		incrementalPricing.inputInPriID(pricingId);

		logger.info("*** Step 2 Expected result: The Incremental Price tbl is displayed ");
		assertTrue(incrementalPricing.pricingIdLbl().getText().trim().equalsIgnoreCase(pricingId),"        System load correct PricingId");
		assertTrue(isElementPresent(incrementalPricing.pricingName(), 5),"        The Incremental Price Table page is loaded");

		logger.info("*** Step 3 Action: Rule - Inclusion & Exclusion: Check on Procedure radio button, Click on Add icon");
		assertTrue(isElementPresent(incrementalPricing.basicProcCheckboxTblRight(0), 5));
		selectCheckBox(incrementalPricing.basicProcCheckboxTblRight(0));
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		assertTrue(isElementPresent(incrementalPricing.procCodeSearchIcon(), 5));
		clickHiddenPageObject(incrementalPricing.procCodeSearchIcon(), 0);
		String parentWindow = switchToPopupWin();

		logger.info("*** Step 3 Expected result: the Procedure Code search page is displayed");
		procCodeSearch = new ProcCodeSearch(driver);
		procCodeSearchResults = new ProcCodeSearchResults(driver);
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(procCodeSearch.searchServiceTypeDropdown(), 5),"        The search Procedure Code page is displayed");

		logger.info("*** Step 4 Actions: select Service Type from dropdown list and click on search btn");
		String serviceType = serviceDao.getServiceTypFromSvcTypAndProcCd().getDescr();
		selectItem(procCodeSearch.searchServiceTypeDropdown(), serviceType);
		assertTrue(isElementPresent(procCodeSearch.procCodeSearchButton(), 5));
		clickHiddenPageObject(procCodeSearch.procCodeSearchButton(), 0);

		logger.info("*** Step 4 expected result: The search result is displayed correctly");
		int resultInDB = daoManagerXifinRpm.getTotalProcCodeSearchResultBasedOnServiceType(serviceType, testDb);
		assertTrue(isElementPresent(procCodeSearchResults.keepSearchOpenCheckbox(), 5),"        The search result page is displayed");
		assertTrue(getColumnValue(procCodeSearchResults.procCodeSearchResultTable(), serviceType),"        The search result is done by service type");
		String resultInUI = testCodeUtils.getTotalResultSearch(procCodeSearchResults.rightPagerTableResultSearch());
		assertEquals(resultInUI.replace(",", ""), String.valueOf(resultInDB),"        The search result is correct");

		clickHiddenPageObject(procCodeSearchResults.closeSearchBtn(), 0);
		switchToParentWin(parentWindow);
		clickHiddenPageObject(incrementalPricing.cancelBtn(), 0);
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority=1, description="Add Proc Rule - Search Proc code with select service level")
	public void testXPR_467() throws Exception{
		logger.info("====== Testing - XPR_467 ======");

		logger.info("*** Step 1 Action: Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected result: The Load Incremental Price Table is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Price Table page is displayed");

		logger.info("*** Step 2 Action: Input new Pricing ID and tab out");
		randomCharacter = new RandomCharacter(driver);
		String pricingId = "ID" + randomCharacter.getRandomNumericString(4);
		incrementalPricing.checkInputPricingId(pricingId, 10);

		logger.info("*** Step 2 Expected result: The Incremental Price table page is displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().trim().equalsIgnoreCase(pricingId),"        The Pricing ID is load correctly");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        The Incremental Price table page is displayed");

		logger.info("*** Step 3 Actions: Rule - Inclusion & Exclusion: Check on Procedure radio button, Click on Add icon");
		assertTrue(isElementPresent(incrementalPricing.basicProcCheckboxTblRight(0), 5));
		selectCheckBox(incrementalPricing.basicProcCheckboxTblRight(0));
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		assertTrue(isElementPresent(incrementalPricing.procCodeSearchIcon(), 5));
		clickHiddenPageObject(incrementalPricing.procCodeSearchIcon(), 0);
		String parentWindow = switchToPopupWin();

		logger.info("*** Step 3 Expected result: Procedure Code search page is displayed");
		procCodeSearch = new ProcCodeSearch(driver);
		procCodeSearchResults = new ProcCodeSearchResults(driver);
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(procCodeSearch.searchServiceLevelDropdown()	, 5),"        The Procedure Code search page is displayed");

		logger.info("*** Step 4 Action:- Select Service Level from dropdown list, Click on Search button");
		List<String> serviceLevel = daoManagerXifinRpm.getServiceLevel(testDb);
		selectItem(procCodeSearch.searchServiceLevelDropdown(), serviceLevel.get(1));
		assertTrue(isElementPresent(procCodeSearch.procCodeSearchButton(), 5));
		procCodeSearch.clickProcCodeSearchButton();

		logger.info("*** Step 4 Expected result: All procedure code have service type are matched with search condition will be displayed");
		int resultInDB = daoManagerXifinRpm.getTotalProcCodeSearchResultBasedOnServiceLevel(serviceLevel.get(0), testDb);
		assertTrue(getColumnValue(procCodeSearchResults.procCodeSearchResultTable(), serviceLevel.get(1)),"        The search result page is search based on service level");
		assertTrue(isElementPresent(procCodeSearch.keepSearchOpenCheckbox(), 5),"        The Search result page is displayed");
		String resultInUI = testCodeUtils.getTotalResultSearch(procCodeSearchResults.rightPagerTableResultSearch()).replace(",", "");
		assertEquals(resultInUI, String.valueOf(resultInDB),"        All proc codes are matched with search condition will displayed");

		clickHiddenPageObject(procCodeSearchResults.closeSearchBtn(), 0);
		switchToParentWin(parentWindow);
		clickHiddenPageObject(incrementalPricing.cancelBtn(), 0);
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority=1, description="Add Proc Rule - Search all Proc code")
	public void testXPR_468() throws Exception{
		logger.info("====== Testing - XPR_468 ======");

		logger.info("*** Step 1: Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected result: The Load Incremental Price Table page is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Price Table page is displayed");

		logger.info("*** Step 2 Actions: Input new Pricing Id and tab out");
		randomCharacter = new RandomCharacter(driver);
		String pricingId = "ID" + randomCharacter.getRandomNumericString(4);
		incrementalPricing.checkInputPricingId(pricingId,10);

		logger.info("*** Step 2 Expected result: The Incremental Price Table page is displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().trim().equalsIgnoreCase(pricingId),"        The Incremental Price table page is loaded correct pricing id");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        The Incremental Price Table page is displayed");

		logger.info("*** Step 3 Actions: Rule - Inclusion & Exclusion: Check on Procedure radio button, Click on Add icon");
		assertTrue(isElementPresent(incrementalPricing.basicProcCheckboxTblRight(0), 5));
		selectCheckBox(incrementalPricing.basicProcCheckboxTblRight(0));
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		assertTrue(isElementPresent(incrementalPricing.procCodeSearchIcon(), 5),"        The Add Procedure code popup is displayed");
		clickHiddenPageObject(incrementalPricing.procCodeSearchIcon(), 0);
		String parentWindow = switchToPopupWin();

		logger.info("*** Step 3 Expected Result: The Procedure Code Search page is displayed");
		procCodeSearch = new ProcCodeSearch(driver);
		procCodeSearchResults = new ProcCodeSearchResults(driver);
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(procCodeSearch.searchProcedureIdInput(), 5),"        The Procedure Code search page is displayed");

		logger.info("*** Step 4 Actions: Input * at Procedure Code field and click Search button");
		procCodeSearch.inputProcedureCodeID("*");
		procCodeSearch.clickProcCodeSearchButton();

		logger.info("*** Step 4 expected result: all existing Proc Code will be displayed");
		int resultInDB = daoManagerXifinRpm.getAllExistingProcCodeSearchResult(testDb);
		assertTrue(isElementPresent(procCodeSearchResults.keepSearchOpenCheckbox(),5),"        The Search result page is displayed");
		assertTrue(isElementPresent(procCodeSearchResults.rightPagerTableResultSearch(), 5));
		String resultInUI = testCodeUtils.getTotalResultSearch(procCodeSearchResults.rightPagerTableResultSearch());
		assertEquals(resultInUI.replace(",", ""), String.valueOf(resultInDB),"        The search result is correct");

		clickHiddenPageObject(procCodeSearchResults.closeSearchBtn(), 0);
		switchToParentWin(parentWindow);
		clickHiddenPageObject(incrementalPricing.cancelBtn(), 0);
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}

	@Test(priority=1, description="Add Proc rule - Search Proc code with invalid input")
	public void testXPR_469() throws Exception{
		logger.info("====== Testing - XPR_469 ======");

		logger.info("*** Step 1 Action: Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToIncrpricingPage();

		logger.info("*** Step 1 Expected result: the Load Incremental Price Table page is displayed");
		incrementalPricing = new IncrementalPricing(driver, config, wait);
		assertTrue(isElementPresent(incrementalPricing.inPriIDInput(), 5),"        The Load Incremental Price Table page is displayed");

		logger.info("*** Step 2 Actions: Input new Pricing Id and tab out");
		randomCharacter = new RandomCharacter(driver);
		String pricingId = "ID" + randomCharacter.getRandomNumericString(4);
		incrementalPricing.inputInPriID(pricingId);

		logger.info("*** Step 2 Expected result: the Incremental Page is displayed");
		assertTrue(incrementalPricing.pricingIdLbl().getText().trim().equalsIgnoreCase(pricingId),"        System load correct Pricing ID");
		assertTrue(isElementPresent(incrementalPricing.submitIncrPricingBtn(), 5),"        The Incremental Price Table page is displayed");

		logger.info("*** Step 3 Action: Rule - Inclusion & Exclusion: Check on Procedure radio button,Click on Add icon");
		assertTrue(isElementPresent(incrementalPricing.basicProcCheckboxTblRight(0), 5));
		selectCheckBox(incrementalPricing.basicProcCheckboxTblRight(0));
		assertTrue(isElementPresent(incrementalPricing.addNewRuleTblRightBtn(0), 5));
		clickHiddenPageObject(incrementalPricing.addNewRuleTblRightBtn(0), 0);
		assertTrue(isElementPresent(incrementalPricing.procCodeSearchIcon(), 5),"        The add record popup is displayed");
		clickHiddenPageObject(incrementalPricing.procCodeSearchIcon(), 0);
		String parentWindow = switchToPopupWin();

		logger.info("*** Step 3 Expected result: the Procedure Code search page is displayed");
		procCodeSearch =  new ProcCodeSearch(driver);
		assertTrue(isElementPresent(procCodeSearch.procCodeSearchButton(), 5),"         The Procedure Code search page is displayed");

		logger.info("*** Step 4 Action: click on Search button without input data");
		assertTrue(isElementPresent(procCodeSearch.procCodeSearchButton(), 5));
		procCodeSearch.clickProcCodeSearchButton();

		logger.info("*** Step 4 Expected result: the warning message is displayed");
		testCode = new TestCode(driver, config, wait);
		assertTrue(isElementPresent(testCode.messageContent(), 5),"        The warning message is displayed");
		assertTrue(testCode.messageContent().getText().trim().equalsIgnoreCase("At least one field must be used to initiate a search."),"        The error message content is correct");

		logger.info("*** Step 5 Actions: Close error message, input invalid procedure code and click on Search button");
		assertTrue(isElementPresent(testCode.closeErrorMessage(), 5),"        The Close error message button must be displayed");
		clickHiddenPageObject(testCode.closeErrorMessage(), 0);
		procCodeSearchResults = new ProcCodeSearchResults(driver);
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(procCodeSearch.searchProcedureIdInput(), 5));
		procCodeSearch.inputProcedureCodeID("aaaasddd");
		procCodeSearch.clickProcCodeSearchButton();

		logger.info("*** Step 6 expected result: No result is displayed");
		assertTrue(isElementPresent(procCodeSearchResults.keepSearchOpenCheckbox(), 5),"        The Search result page is displayed");
		assertTrue(isElementPresent(procCodeSearchResults.rightPagerTableResultSearch(), 5),"        No result is displayed");
		assertTrue(procCodeSearchResults.rightPagerTableResultSearch().getText().trim().contains("No Result"));

		clickHiddenPageObject(procCodeSearchResults.closeSearchBtn(), 0);
		switchToParentWin(parentWindow);
		clickHiddenPageObject(incrementalPricing.cancelBtn(), 0);
		clickHiddenPageObject(incrementalPricing.resetIncrPricingBtn(), 0);
	}
}	
