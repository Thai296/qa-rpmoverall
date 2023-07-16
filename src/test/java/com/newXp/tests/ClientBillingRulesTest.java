package com.newXp.tests;

import com.mbasys.mars.ejb.entity.clnBillingRulesPyrExcl.ClnBillingRulesPyrExcl;
import com.mbasys.mars.ejb.entity.clnBillingRulesPyrIncl.ClnBillingRulesPyrIncl;
import com.mbasys.mars.ejb.entity.clnBillingRulesTestExcl.ClnBillingRulesTestExcl;
import com.mbasys.mars.ejb.entity.clnBillingRulesTestIncl.ClnBillingRulesTestIncl;
import com.mbasys.mars.ejb.entity.clnPyrGrpTestEpRuleExcl.ClnPyrGrpTestEpRuleExcl;
import com.mbasys.mars.ejb.entity.clnPyrTestEpRule.ClnPyrTestEpRule;
import com.mbasys.mars.ejb.entity.clnPyrTestEpRuleExcl.ClnPyrTestEpRuleExcl;
import com.mbasys.mars.ejb.entity.ptTyp.PtTyp;
import com.mbasys.mars.ejb.entity.pyr.Pyr;
import com.mbasys.mars.ejb.entity.pyrGrp.PyrGrp;
import com.mbasys.mars.ejb.entity.testTyp.TestTyp;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.overall.client.clientBillingRules.ClientBillingRules;
import com.overall.menu.MenuNavigation;
import com.overall.search.ClientSearch;
import com.overall.search.ClientSearchResults;
import com.overall.utils.XifinPortalUtils;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.qa.dao.rpm.domain.ClientSearchResult;
import com.xifin.qa.dao.rpm.domain.ClnBillingRules;
import com.xifin.utils.*;
import domain.client.clientBillingRules.BillingRules;
import domain.client.clientBillingRules.Header;
import domain.client.clientBillingRules.PayorTestEPRules;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class ClientBillingRulesTest extends SeleniumBaseTest {
	private ClientSearch clientSearch;
	private RandomCharacter randomCharacter;
	private XifinPortalUtils xifinPortalUtils;
	private ClientBillingRules clientBillingRules;
	private ClientSearchResults clientSearchResults;

	private static final String EMPTY = "";
	private static final String LAST_OPTION = "last()";
	private static final String ADD_RECORD = "Add Record";
	private static final String VIEW_DOC = "View Document";
	private static final String TEST_EXCL_LBL= "Test Excl: ";
	private static final String TEST_INCL_LBL = "Test Incl: ";
	private static final String PYR_EXCL_LBL = "Payor Excl: ";
	private static final String PAYOR_INCL_LBL = "Payor Incl: ";
	private static final String ONLY_ALLOW_LBL = "Only Allow: ";
	private static final String DONT_ALLOW_LBL = "Don't Allow: ";
	private static final String CLIENT_SEARCH = "Client Search";
	private static final String TEST_TYPE_EXCL_LBL= "Test Type Excl:  ";
	private static final String BILLING_RULES_TITLE = "Billing Rules";
	private static final String TEST_TYPE_INCL_LBL = "Test Type Incl:  ";
	private static final String PYR_GRP_EXCL_LBL = "Payor Group Excl:  ";
	private static final String PAYOR_GROUP_INCL_LBL = "Payor Group Incl:  ";
	private static final String PATIENT_TYPE_INCL_LBL = "Patient Type Incl: ";
	private static final String SEARCH_PAGE_LNK = "/search/clientsearch.html";
	private static final String VIEW_CLN_PORTAL_DOC = "View Client Portal Doc UpDocument";
	private static final String CLIENT_SEARCH_XIFIN_TITLE = "Client Search - Xifin Portal";

	@BeforeMethod(alwaysRun = true)
    @Parameters({"ssoXpUsername", "ssoXpPassword"})
    public void beforeMethod(String ssoXpUsername, String ssoXpPassword, Method method) {
        try {
            logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
            xifinPortalUtils = new XifinPortalUtils(driver);
            logIntoSso(ssoXpUsername, ssoXpPassword);
            MenuNavigation menuNavigation = new MenuNavigation(driver, config);
            menuNavigation.navigateToClientBillingRulesPage();
        } catch (Exception e) {
            logger.error("Error running BeforeMethod", e);
        }
    }
	
	@Test(priority = 1, description = "Verify Help Icon")
	public void testXPR_1187() throws Throwable {
		logger.info("==== Testing - testXPR-1187 ====");

		clientBillingRules = new ClientBillingRules(driver, wait);

		logger.info("*** Step 1 Expected Results: User login successful, Billing Rules load page is diplayed");
		verifyClientBillingRulesLoadPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Click on help icon at header of the load page");
		clickHiddenPageObject(clientBillingRules.clientBillingRulesLoadPgHeaderHelpIco(), 0);
		
		logger.info("*** Step 2 Expected Results: Help page is displayed");
		xifinPortalUtils.verifyHelpPageIsDisplayed(this, "help/client_tab/client_processing_menu/p_client_billing_rules_header.htm", null);
		
		logger.info("*** Step 3 Action: Click on help icon at Load Client section");
		clickHiddenPageObject(clientBillingRules.clientBillingRulesLoadClientSectionHelpIcon(), 0);
		
		logger.info("*** Step 3 Expected Results: Help page is displayed");
		xifinPortalUtils.verifyHelpPageIsDisplayed(this, "help/client_tab/client_processing_menu/p_client_billing_rules_load_client_id.htm", null);
		
		logger.info("*** Step 4 Action: Enter a valid Client Id");
		ClnBillingRules expectedClnBillingRules = clientDao.getRandomClnBillingRuleDataFromCLN();
		Header expectedHeader = setValueToClientIdInput(expectedClnBillingRules);
		
		logger.info("*** Step 4 Expected Results: Billing Rules page is displayed");
		Header actualHeader = getClientBillingRuleValues();
		assertEquals(actualHeader, expectedHeader);
		
		logger.info("*** Step 5 Action: Click on help icon at header of the detail page");
		clickHiddenPageObject(clientBillingRules.headerHelpIco(), 0);
		
		logger.info("*** Step 5 Expected Results: Help page id displayed");
		xifinPortalUtils.verifyHelpPageIsDisplayed(this, "help/client_tab/client_processing_menu/p_client_billing_rules_header.htm", null);
		
		logger.info("*** Step 6 Action: Click on help icon at Biling Rules section of the detail page");
		clickHiddenPageObject(clientBillingRules.billingRulesSectionHelpIcon(), 0);
		
		logger.info("*** Step 6 Expected Results: Help page id displayed");
		xifinPortalUtils.verifyHelpPageIsDisplayed(this, "help/client_tab/client_processing_menu/p_client_billing_rules_billing_rules.htm", null);
		
		logger.info("*** Step 7 Action: Click on help icon at  Payor Test EP Rules section of the detail page");
		clickHiddenPageObject(clientBillingRules.payorTestEPRulesSectionHelpIco(), 0);
		
		logger.info("*** Step 7 Expected Results: Help page id displayed");
		xifinPortalUtils.verifyHelpPageIsDisplayed(this, "help/client_tab/client_processing_menu/p_client_billing_rules_payor_test_ep_rules.htm", null);
		
		logger.info("*** Step 8 Action: Click on Reset Button");
		clickOnResetBtn();
		
		logger.info("*** Step 8 Expected Results: System will take user return Billing Rules Load page");
		verifyClientBillingRulesLoadPageIsDisplayed();
		
		driver.quit();
	}

	@Test(priority=1, description="Load Billing Rules with Client Id at Client Search Result page")
	public void testXPR_1188() throws Throwable {
		logger.info("===== Testing - testXPR-1188 =====");

		clientSearch = new ClientSearch(driver);
		clientBillingRules = new ClientBillingRules(driver, wait);
		clientSearchResults = new ClientSearchResults(driver);

		logger.info("*** Step 1: Expected Results -   User login successfull. Billing Rules load page is displayed.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		
		logger.info("*** Step 2: Action - Click on Search Client Icon.");
		clickHiddenPageObject(clientBillingRules.clientBillingRulesClientIdSearchIco(),	0);
		
		logger.info("*** Step 2: Expected Results - Client Search page is displayed.");
		String parentWindow = switchToPopupWin();
		verifyClientSearchPageIsDisplayed(SEARCH_PAGE_LNK);
		String searchWindow = driver.getWindowHandle();
		
		logger.info("*** Step 3: Action - Enter random NPI Id to NPI Input. Click on Search button.");
		String npiInput = setClientSearchValues();
		
		logger.info("*** Step 3: Expected Results - All Client id records are displayed on Client Search Result page correctly.");
		verifyClientSearchResultIsDisplayedCorrectly(npiInput);
		
		logger.info("*** Step 4 Actions: Click on any Client Id");
		String clnIdClicked = clientSearchResults.clientSearchResultsTblClientIDColLink(LAST_OPTION).getText();
		clickHiddenPageObject(clientSearchResults.clientSearchResultsTblClientIDColLink(LAST_OPTION), 0);
		
		logger.info("*** Step 4 Expected Results - Search result page is closed. Billing Rules detail page is displayed with correct client id.");
		assertFalse(driver.getWindowHandles().contains(searchWindow));
		switchToParentWin(parentWindow);
		Header expectedHeader = setExpectedHeaderValues(clientDao.getClnBillingRuleDataFromCLNByClnAbbrev(clnIdClicked).get(0));
		Header actualHeader = getClientBillingRuleValues();
		assertEquals(actualHeader, expectedHeader);
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Verify Reset button")
	public void testXPR_1189() throws Throwable {
		logger.info("===== Testing - testXPR_1189 =====");

		randomCharacter = new RandomCharacter();
		clientBillingRules = new ClientBillingRules(driver, wait);

		logger.info("*** Step 1 Expected Results: User login successful and Billing Rules load page is displayed.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Enter valid client id to Client Id inputField.");
		ClnBillingRules clnBillingRules = clientDao.getRandomClnBillingRuleDataFromCLN();
		Header expectedHeader = setValueToClientIdInput(clnBillingRules);
		
		logger.info("*** Step 2 Expected Results: Billing Rules detail page is displayed with correct client data.");
		Header actualHeader = getClientBillingRuleValues();
		assertEquals(actualHeader, expectedHeader, "        Billing Rules detail page is displayed.");
		
		logger.info("*** Step 3 Action: Billing Rules table : Click on Add button and enter new valid Data");
		clickHiddenPageObject(clientBillingRules.billingRulesTblAddBtn(), 0);
		assertTrue(clientBillingRules.billingRulesTblPopupTitleTxt().getText().equals(ADD_RECORD),"        The title of Add record popup should be 'Add Record'.");
		BillingRules expectedBillingRules = setValuesInBillingRulesTbl(true);
		clickHiddenPageObject(clientBillingRules.billingRulesTblPopupOkBtn(), 0);
	
		logger.info("*** Step 3 Expected Results: Data is available to use.");
		filterRecordInBillingRules(expectedBillingRules);
		int totalRecordInResultSearch = Integer.parseInt(getTotalResultSearch(clientBillingRules.billingRulesTblTotalRecordTxt()));
		assertTrue(totalRecordInResultSearch>0,"        Exist a record in table.");

		logger.info("*** Step 4 Action: Click on Reset btn.");
		clickOnResetBtn();
		
		logger.info("*** Step 4 Expected Results: 4. The warning message is displayed "
				+ " - System will take user return Billing Rules load page and no records is saved in DB");
		String warningMessage ="Changes have been made to this page. Are you sure you want to reset the page?";
		assertEquals(warningMessage, clientBillingRules.warningPopupWarningTxt().getText(),"        A warning message '"+warningMessage+"' is correct displayed");
		clickHiddenPageObject(clientBillingRules.warningPopupBtn("Reset"), 0);
		verifyClientBillingRulesLoadPageIsDisplayed();
		verifyNoRecordSavedIntoBillingRuleTbl(clnBillingRules.getClnId(), expectedBillingRules);

		driver.quit();
	}

	@Test(priority = 1, description = "Billing Rules section - Add new record with Bill Client Id")
	public void testXPR_1190() throws Throwable {
		logger.info("===== Testing - testXPR_1190 =====");

		randomCharacter = new RandomCharacter();
		clientBillingRules = new ClientBillingRules(driver, wait);

		logger.info("*** Step 1 Expected Results: User login successful and Billing Rules load page is displayed.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Enter valid client id to Client Id inputField.");
		ClnBillingRules clnBillingRules = clientDao.getRandomClnBillingRuleDataFromCLN();
		Header expectedHeader = setValueToClientIdInput(clnBillingRules);
		
		logger.info("*** Step 2 Expected Results: Billing Rules detail page is displayed with correct client data.");
		Header actualHeader = getClientBillingRuleValues();
		assertEquals(actualHeader, expectedHeader, "        Billing Rules detail page is displayed.");
		
		logger.info("*** Step 3 Action: Billing Rules table : Click on Add button ");
		clickHiddenPageObject(clientBillingRules.billingRulesTblAddBtn(), 0);
		
		logger.info("*** Step 3 Expected Results: add record popup display.");
		assertTrue(clientBillingRules.billingRulesTblPopupTitleTxt().getText().equals(ADD_RECORD),"        The title of Add record popup should be 'Add Record'.");
		
		logger.info("*** Step 4 Action:  Enter valid data for all fields Override Criteria Options and Override value is Bill ClientId.");
		BillingRules expectedBillingRules = setValuesInBillingRulesTbl(true);
		
		logger.info("*** Step 4 Expected Results:  Add record popup display with corrected data.");
		assertTrue(isElementPresent(clientBillingRules.billingRulesTblPopupTitleTxt(), 5),"       Billing Rules Tbl, Title Popup Txt is displayed.");
		
		logger.info("*** Step 5 Action:  Click OK button.");
		clickHiddenPageObject(clientBillingRules.billingRulesTblPopupOkBtn(), 0);
		
		logger.info("*** Step 5 Expected Results:  New record is displayed correctly in the  Billing Rules table.");
		filterRecordInBillingRules(expectedBillingRules);
		int totalRecordInResultSearch = Integer.parseInt(getTotalResultSearch(clientBillingRules.billingRulesTblTotalRecordTxt()));
		assertTrue(totalRecordInResultSearch > 0,"        Exist a record in table.");
		
		logger.info("*** Step 6 Action: Click on Save And Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 6 Expected Results: System will take user return Billing Rules load page and new records is saved in CLN_BILLING_RULES.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		verifyBillingRuleDataIsSavedInDB(clnBillingRules.getClnId(), expectedBillingRules, true);
		
		logger.info("*** Step 7 Action: Revert data.");
		cleanDataBillingRules(clnBillingRules.getClnId(), expectedBillingRules);
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Billing Rules section - Add new record with Bill Payor Id")
	public void testXPR_1191() throws Throwable {
		logger.info("===== Testing - testXPR_1191 =====");

		randomCharacter = new RandomCharacter();
		clientBillingRules = new ClientBillingRules(driver, wait);

		logger.info("*** Step 1 Expected Results: User login successful and Billing Rules load page is displayed.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Enter valid client id to Client Id inputField.");
		ClnBillingRules clnBillingRules = clientDao.getRandomClnBillingRuleDataFromCLN();
		Header expectedHeader = setValueToClientIdInput(clnBillingRules);
		
		logger.info("*** Step 2 Expected Results: Billing Rules detail page is displayed with correct client data.");
		Header actualHeader = getClientBillingRuleValues();
		assertEquals(actualHeader, expectedHeader, "        Billing Rules detail page is displayed.");
		
		logger.info("*** Step 3 Action: Billing Rules table : Click on Add button ");
		clickHiddenPageObject(clientBillingRules.billingRulesTblAddBtn(), 0);
		
		logger.info("*** Step 3 Expected Results: add record popup display.");
		assertTrue(clientBillingRules.billingRulesTblPopupTitleTxt().getText().equals(ADD_RECORD),"        The title of Add record popup should be 'Add Record'.");
		
		logger.info("*** Step 4 Action:  Enter valid data for all fields Override Criteria Options and Override value is Bill PayorId.");
		BillingRules expectedBillingRules = setValuesInBillingRulesTbl(false);
		
		logger.info("*** Step 4 Expected Results:  Add record popup display with corrected data.");
		assertTrue(isElementPresent(clientBillingRules.billingRulesTblPopupTitleTxt(), 5),"       Billing Rules Tbl, Title Popup Txt is displayed.");
		
		logger.info("*** Step 5 Action:  Click OK button.");
		clickHiddenPageObject(clientBillingRules.billingRulesTblPopupOkBtn(), 0);
		
		logger.info("*** Step 5 Expected Results:  New record is displayed correctly in the  Billing Rules table.");
		xifinPortalUtils.waitForPageLoaded(wait);
		filterRecordInBillingRules(expectedBillingRules);
		int totalRecordInResultSearch = Integer.parseInt(getTotalResultSearch(clientBillingRules.billingRulesTblTotalRecordTxt()));
		assertTrue(totalRecordInResultSearch > 0,"        Exist a record in table.");
		
		logger.info("*** Step 6 Action: Click on Save And Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 6 Expected Results: System will take user return Billing Rules load page and new records is saved in CLN_BILLING_RULES and Fk_Bill_Pyr_Id is displayed correctly.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		verifyBillingRuleDataIsSavedInDB(clnBillingRules.getClnId(), expectedBillingRules, false);
		
		logger.info("*** Step 7 Action: Revert data.");
		cleanDataBillingRules(clnBillingRules.getClnId(), expectedBillingRules);
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Billing Rules section - Update record with valid data ")
	public void testXPR_1192() throws Throwable {
		logger.info("==== Testing - testXPR_1192 ====");

		clientBillingRules = new ClientBillingRules(driver, wait);

		logger.info("*** Step 1 Expected results: User login successful. Load Billing Rules page is displayed");
		verifyClientBillingRulesLoadPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Enter valid client Id to client input.");
		ClnBillingRules clnBillingRules = clientDao.getRandomClnBillingRuleDataFromCLN();
		Header expectedHeader = setValueToClientIdInput(clnBillingRules);
				
		logger.info("*** Step 2 Expected Results: Billing Rules detail page is displayed with correct client data.");
		Header actualHeader = getClientBillingRuleValues();
		assertEquals(actualHeader, expectedHeader, "        Billing Rules detail page is displayed with correct client data");
		
		logger.info("*** Step 3 Action: Billing Rules table - Add new record with valid data");
		clickHiddenPageObject(clientBillingRules.billingRulesTblAddBtn(), 0);
		BillingRules expectedBillingRules = setValuesInBillingRulesTbl(true);
		clickHiddenPageObject(clientBillingRules.billingRulesTblPopupOkBtn(), 0);
		
		logger.info("*** Step 3 Expected Results: New record is displayed correctly in the Billing Rules table.");
		filterRecordInBillingRules(expectedBillingRules);
		BillingRules actualBillingRules = getValuesInBillingRulesTable(LAST_OPTION);
		assertEquals(actualBillingRules, expectedBillingRules,"        New record is displayed correctly in the Billing Rules table.");
		
		logger.info("*** Step 4 Action: Click Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 4 Expected Results: System will take user return Billing Rules load page. New records are saved in CLN_BILLING_RULES,"
											  + " cln_billing_rules_pyr_excl, cln_billing_rules_pyr_incl, cln_billing_rules_test_excl, cln_billing_rules_test_incl table");
		verifyClientBillingRulesLoadPageIsDisplayed();
		int seqClnBillingRules = verifyBillingRuleDataIsSavedInClnBillingRules(clnBillingRules.getClnId(), actualBillingRules, true);
		List<Integer> seqs = verifyBillingRuleDataIsSaveInMultiClnBillingRules(seqClnBillingRules, 2, true);
		
		logger.info("*** Step 5 Action: Enter again clnId at step 2.");
		Header uploadExpectedHeader = setValueToClientIdInput(clnBillingRules);
		
		logger.info("*** Step 5 Expected Results: Billing Rules detail page is displayed with correct client data.");
		Header uploadActualHeader = getClientBillingRuleValues();
		assertEquals(uploadActualHeader,uploadExpectedHeader,"        Billing Rules detail page is displayed with correct client data");
		
		logger.info("*** Step 6 Action: Billing Rules table - Update data for the record was added new at step 3.");
		BillingRules expectedUpdateBillingRules = updateDataInBillingRule(expectedBillingRules);
		
		logger.info("*** Step 6 Expected Results: This record is updated correctly the Billing Rules table.");
		filterRecordInBillingRules(expectedUpdateBillingRules);
		BillingRules actualUpdateBillingRules = getValuesInBillingRulesTable(LAST_OPTION);
		assertEquals(actualUpdateBillingRules, expectedUpdateBillingRules,"        New record is displayed correctly in the Billing Rules table.");
		
		logger.info("*** Step 7 Action: Click Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 7 Expected Results: System will take user return Billing Rules load page. New records are updated in CLN_BILLING_RULES, cln_billing_rules_pyr_excl, cln_billing_rules_pyr_incl, cln_billing_rules_test_excl, cln_billing_rules_test_incl table.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		verifyBillingRuleDataIsNotExistInClnBillingRules(clnBillingRules.getClnId(), actualBillingRules);
		int updateSeqClnBillingRules = verifyBillingRuleDataIsSavedInClnBillingRules(clnBillingRules.getClnId(),actualUpdateBillingRules, true);
		verifyBillingRuleDataIsDeletedAndInsertInMultiClnBillingRules(updateSeqClnBillingRules, seqs);
		
		logger.info("*** Step 8 Action: Revert data.");
		cleanDataBillingRules(clnBillingRules.getClnId(), actualUpdateBillingRules) ;
		driver.quit();
	}
	
	@Test(priority = 1, description = "Billing Rules section - Delete record")
	public void testXPR_1193() throws Throwable {
		logger.info("==== Testing - testXPR_1193 ====");

		clientBillingRules = new ClientBillingRules(driver, wait);

		logger.info("*** Step 1 Expected results: User login successful. Load Billing Rules page is displayed");
		verifyClientBillingRulesLoadPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Enter valid client Id to client input.");
		ClnBillingRules clnBillingRules = clientDao.getRandomClnBillingRuleDataFromCLN();
		Header expectedHeader = setValueToClientIdInput(clnBillingRules);
				
		logger.info("*** Step 2 Expected Results: Billing Rules detail page is displayed with correct client data.");
		Header actualHeader = getClientBillingRuleValues();
		assertEquals(actualHeader,expectedHeader, "        Billing Rules detail page is displayed with correct client data");
		
		logger.info("*** Step 3 Action: Billing Rules table - Add new record with valid data");
		clickHiddenPageObject(clientBillingRules.billingRulesTblAddBtn(), 0);
		BillingRules expectedBillingRules = setValuesInBillingRulesTbl(true);
		clickHiddenPageObject(clientBillingRules.billingRulesTblPopupOkBtn(), 0);
		
		logger.info("*** Step 3 Expected Results: New record is displayed correctly in the Billing Rules table.");
		filterRecordInBillingRules(expectedBillingRules);
		BillingRules actualBillingRules = getValuesInBillingRulesTable(LAST_OPTION);
		assertEquals(actualBillingRules, expectedBillingRules, "        New record is displayed correctly in the Billing Rules table.");
		
		logger.info("*** Step 4 Action: Click Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 4 Expected Results: System will take user return Billing Rules load page. New records are saved in CLN_BILLING_RULES, cln_billing_rules_pyr_excl, cln_billing_rules_pyr_incl, cln_billing_rules_test_excl, cln_billing_rules_test_incl table.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		int seqClnBillingRules = verifyBillingRuleDataIsSavedInClnBillingRules(clnBillingRules.getClnId(), actualBillingRules, true);
		verifyBillingRuleDataIsSaveInMultiClnBillingRules(seqClnBillingRules, 2, false);
		
		logger.info("*** Step 5 Action: Enter again clnId at step 2.");
		Header uploadExpectedHeader = setValueToClientIdInput(clnBillingRules);
		
		logger.info("*** Step 5 Expected Results: Billing Rules detail page is displayed with correct client data.");
		Header uploadActualHeader = getClientBillingRuleValues();
		assertEquals(uploadActualHeader,uploadExpectedHeader, "        Billing Rules detail page is displayed with correct client data");
		
		logger.info("*** Step 6 Action: Billing Rules table - Check deleted checkbox of the record that was added new at step 3.");
		checkDeletedCheckboxOfTheRecordInBillingRuleTbl(expectedBillingRules);
		
		logger.info("*** Step 6 Expected Results: Deleted checkbox is checked for this row of Billing Rules table.");
		assertTrue(isChecked(clientBillingRules.billingRulesTblDeletedColChk(LAST_OPTION)), "        Deleted checkbox is checked for this row of Billing Rules table..");
		assertTrue(rowIsMarkForDelete(clientBillingRules.billingRulesTbl(), LAST_OPTION),"        The last row is displayed.");

		logger.info("*** Step 7 Action: Click Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 7 Expected Results: System will take user return Billing Rules load page. New records are deleted in CLN_BILLING_RULES, cln_billing_rules_pyr_excl, cln_billing_rules_pyr_incl, cln_billing_rules_test_excl, cln_billing_rules_test_incl table.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		int deleteSeqClnBillingRules = verifyBillingRuleDataIsNotExistInClnBillingRules(clnBillingRules.getClnId(), actualBillingRules);
		verifyBillingRuleDataIsSaveInMultiClnBillingRules(deleteSeqClnBillingRules, 0, false);
		verifyBillingRuleDataIsSaveInMultiClnBillingRules(seqClnBillingRules, 0, false);

		driver.quit();
	}
	
	@Test(priority = 1, description = "Payor Test EP Rules section - Add new record with valid data")
	public void testXPR_1194() throws Throwable {
		logger.info("==== Testing - testXPR_1194 ====");

		clientBillingRules = new ClientBillingRules(driver, wait);

		logger.info("*** Step 1 Expected results: User login successful. Load Billing Rules page is displayed");
		verifyClientBillingRulesLoadPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Enter valid client Id to client input.");
		ClnBillingRules clnBillingRules = clientDao.getRandomClnBillingRuleDataFromCLN();
		Header expectedHeader = setValueToClientIdInput(clnBillingRules);
				
		logger.info("*** Step 2 Expected Results: Billing Rules detail page is displayed with correct client data.");
		Header actualHeader = getClientBillingRuleValues();
		assertEquals(actualHeader,expectedHeader,"        Billing Rules detail page is displayed with correct client data");
		
		logger.info("*** Step 3 Action: Payor Test Ep Rules table: Click on Add button.");
		clickOnElement(clientBillingRules.pyrTestEPRulesTblAddBtn());
		
		logger.info("*** Step 3 Expected Results: Add popup is displayed.");
		assertEquals(clientBillingRules.pyrTestEPRulesTblPopupTitleTxt().getText(), ADD_RECORD,"        Title popup 'Add Record' is displayed.");

		logger.info("*** Step 4 Action: Enter valid data.");
		PayorTestEPRules expectedPayorTestEPRules = setValuesToPayorTestEPRulesWithEnterValue();
		
		logger.info("*** Step 4 Expected Results: Add record popup is displayed with correctly data.");
		PayorTestEPRules actulaPayorTestEPRules = getValuesFromPayorTestEPRulesPopup();
		assertEquals(actulaPayorTestEPRules,expectedPayorTestEPRules, "        Add record popup is displayed with correctly data.");

		logger.info("*** Step 5 Action: Click on OK button.");
		clickOnElement(clientBillingRules.pyrTestEPRulesSectionPopupOkBtn());
		
		logger.info("*** Step 5 Expected Results: New record is displayed correctly in the Payor Test EP Rules table.");
		PayorTestEPRules actualPayorTestEPRules = getValuesToPayorTestEPRulesWithEnterValue(expectedPayorTestEPRules, LAST_OPTION);
		assertEquals(actualPayorTestEPRules, expectedPayorTestEPRules, "        New record is displayed correctly in the Payor Test Ep Rules table");
	
		logger.info("*** Step 6 Action: Click Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 6 Expected Results: Billing Rules load page is displayed. Data is saved in cln_pyr_test_ep_rule.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		verifyDataIsSavedInClnPyrTestEpRule(clnBillingRules.getClnId(), actualPayorTestEPRules);
	
		logger.info("*** Step 7 Action: Revert data.");
		cleanDataPayorTestEPRules(clnBillingRules.getClnId(), actualPayorTestEPRules);
		driver.quit();
	}

	@Test(priority = 1, description = "Payor Test EP Rules section - Update record with valid data")
	public void testXPR_1195() throws Throwable {
		logger.info("==== Testing - testXPR_1195 ====");

		clientBillingRules = new ClientBillingRules(driver, wait);

		logger.info("*** Step 1 Expected results: User login successful. Load Billing Rules page is displayed");
		verifyClientBillingRulesLoadPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Enter valid client Id to client input.");
		ClnBillingRules clnBillingRules = clientDao.getRandomClnBillingRuleDataFromCLN();
		Header expectedHeader = setValueToClientIdInput(clnBillingRules);
				
		logger.info("*** Step 2 Expected Results: Billing Rules detail page is displayed with correct client data.");
		Header actualHeader = getClientBillingRuleValues();
		assertEquals(actualHeader,expectedHeader,"        Billing Rules detail page is displayed with correct client data");
		
		logger.info("*** Step 3 Action: Payor Test Ep Rules table - Add new record with valid data");
		PayorTestEPRules expectedpayorTestEPRules = addReCordInPayorTestEPRulesTbl();
		
		logger.info("*** Step 3 Expected Results: New record is displayed correctly in the Payor Test Ep Rules table.");
		xifinPortalUtils.waitForPageLoaded(wait);
		PayorTestEPRules actualPayorTestEPRules = getValuesToPayorTestEPRulesWithEnterValue(expectedpayorTestEPRules, LAST_OPTION);
		assertEquals(actualPayorTestEPRules,expectedpayorTestEPRules, "        New record is displayed correctly in the Payor Test Ep Rules table");
		
		logger.info("*** Step 4 Action: Click Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 4 Expected Results: Billing Rules load page is displayed. Data is saved in cln_pyr_test_ep_rule,cln_pyr_test_ep_rule_excl,cln_pyr_grp_test_ep_rule_excl");
		verifyClientBillingRulesLoadPageIsDisplayed();
		verifyDataIsSavedInClnPyrTestEpRule(clnBillingRules.getClnId(), actualPayorTestEPRules);
		
		logger.info("*** Step 5 Action: Enter again clnId at step 2.");
		expectedHeader = setValueToClientIdInput(clnBillingRules);
		
		logger.info("*** Step 5 Expected Results: Billing Rules detail page is displayed with correct client data.");
		actualHeader = getClientBillingRuleValues();
		assertEquals(actualHeader,expectedHeader,"        Billing Rules detail page is displayed with correct client data");
		
		logger.info("*** Step 6 Action: Payor Test Ep Rules table - Update data was added at step 3.");
		expectedpayorTestEPRules = updatePayorTestEpRulesPayorGroup(expectedpayorTestEPRules, LAST_OPTION);
		
		logger.info("*** Step 6 Expected Results: This record is updated correctly the Payor Test Ep Rules table.");
		actualPayorTestEPRules = getValuesToPayorTestEPRulesWithEnterValue(expectedpayorTestEPRules, LAST_OPTION);
		assertEquals(actualPayorTestEPRules,expectedpayorTestEPRules,"        New record is displayed correctly in the Payor Test Ep Rules table");
		
		logger.info("*** Step 7 Action: Click Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 7 Expected Results: Load Billing Rules page is displayed. Data is updated in CLN_BILLING_RULES,"
											  + " cln_billing_rules_pyr_excl, cln_billing_rules_pyr_incl, cln_billing_rules_test_excl, cln_billing_rules_test_incl table.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		verifyDataIsSavedInClnPyrTestEpRule(clnBillingRules.getClnId(), actualPayorTestEPRules);
		
		logger.info("*** Step 8 Action: Revert data.");
		cleanDataPayorTestEPRules(clnBillingRules.getClnId(), actualPayorTestEPRules);
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Payor Test EP Rules section - Delete record")
	public void testXPR_1196() throws Throwable {
		logger.info("==== Testing - testXPR_1196 ====");

		clientBillingRules = new ClientBillingRules(driver, wait);

		logger.info("*** Step 1 Expected results: User login successful. Load Billing Rules page is displayed");
		verifyClientBillingRulesLoadPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Enter valid client Id to client input.");
		ClnBillingRules clnBillingRules = clientDao.getRandomClnBillingRuleDataFromCLN();
		Header expectedHeader = setValueToClientIdInput(clnBillingRules);
				
		logger.info("*** Step 2 Expected Results: Billing Rules detail page is displayed with correct client data.");
		Header actualHeader = getClientBillingRuleValues();
		assertEquals(actualHeader,expectedHeader,"        Billing Rules detail page is displayed with correct client data");
		
		logger.info("*** Step 3 Action: Payor Test Ep Rules table - Add new record with valid data");
		PayorTestEPRules expectedpayorTestEPRules = addReCordInPayorTestEPRulesTbl();
		
		logger.info("*** Step 3 Expected Results: New record is displayed correctly in the Payor Test Ep Rules table.");
		PayorTestEPRules actualPayorTestEPRules = getValuesToPayorTestEPRulesWithEnterValue(expectedpayorTestEPRules, LAST_OPTION);
		assertEquals(actualPayorTestEPRules,expectedpayorTestEPRules,"        New record is displayed correctly in the Payor Test Ep Rules table");
		
		logger.info("*** Step 4 Action: Click Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 4 Expected Results: Billing Rules load page is displayed. Data is saved in cln_pyr_test_ep_rule,cln_pyr_test_ep_rule_excl,cln_pyr_grp_test_ep_rule_excl.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		verifyDataIsSavedInClnPyrTestEpRule(clnBillingRules.getClnId(), actualPayorTestEPRules);
		
		logger.info("*** Step 5 Action: Enter again clnId at step 2.");
		expectedHeader = setValueToClientIdInput(clnBillingRules);
		
		logger.info("*** Step 5 Expected Results: Billing Rules detail page is displayed with correct client data.");
		actualHeader = getClientBillingRuleValues();
		assertEquals(actualHeader,expectedHeader, "        Billing Rules detail page is displayed with correct client data");
		
		logger.info("*** Step 6 Action: Payor Test Ep Rules table - Click on Deleted check box of data was added at step 3.");
		clickOnDeletedCheckbox(expectedpayorTestEPRules,LAST_OPTION);
		
		logger.info("*** Step 6 Expected Results: Deleted Checkbox is checked.");
		assertTrue(clientBillingRules.pyrTestEPRulesTblDeletedColChk(LAST_OPTION).isSelected(), "        Deleted Checkbox is checked.");
		
		logger.info("*** Step 7 Action: Click Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 7 Expected Results: Load Billing Rules page is displayed. Data is deleted in cln_pyr_test_ep_rule,cln_pyr_test_ep_rule_excl,cln_pyr_grp_test_ep_rule_excl.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		verifyDataIsRemovedInClnPyrTestEpRule(clnBillingRules.getClnId(), actualPayorTestEPRules);
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Payor Test EP Rules section - Copy data from another ClientId")
	public void testXPR_1197() throws Throwable {
		logger.info("==== Testing - testXPR_1197 ====");

		clientBillingRules = new ClientBillingRules(driver, wait);

		logger.info("*** Step 1 Expected results: User login successful. Load Billing Rules page is displayed");
		verifyClientBillingRulesLoadPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Enter client Id no have record in CLN_BILLING_RULES to client input.");
		ClnBillingRules clnBillingRules = clientDao.getRandomClnBillingRuleDataFromCLN();
		ClnBillingRules clnBillingRulesNoRd = clientDao.getRandomClnBillingRuleDataFromCLN();
		Header expectedHeader = setValueToClientIdInput(clnBillingRules);
				
		logger.info("*** Step 2 Expected Results: Billing Rules detail page is displayed with correct client data.");
		Header actualHeader = getClientBillingRuleValues();
		assertEquals(actualHeader,expectedHeader, "        Billing Rules detail page is displayed with correct client data");
		
		logger.info("*** Step 3 Action: Enter ClientId that have record in CLN_PYR_TEST_EP_RULE");
		PayorTestEPRules payorTestEPRulesOfCopyCln = addReCordInPayorTestEPRulesTbl();
		clickOnSaveAndClearBtn();
		expectedHeader = setValueToClientIdInput(clnBillingRulesNoRd);
		enterValues(clientBillingRules.billingRulesSectionClientIdInput(),clnBillingRules.getClnAbbrev());
		
		logger.info("*** Step 3 Expected Results: Copy From section is displayed in Billing Rule section."
											 + "- Payor Test EP Rules of Copy Client are displayed in Payor Test EP Rules table.");
		assertEquals(clientBillingRules.billingRulesSectionClientIdInput().getAttribute("value"),clnBillingRules.getClnAbbrev(), "        Copy Client input is displayed.");
		ClnPyrTestEpRule clnPyrTestEpRule = clientDao.getClientPyrEpFromClnPyrTestEpRuleByClnId(clnBillingRulesNoRd.getClnId(), Integer.parseInt(payorTestEPRulesOfCopyCln.getPayorId()));
		PayorTestEPRules actPayorTestEPRules = mapClnPyrTestEpRuleToPayorTestEPRules(clnPyrTestEpRule,payorTestEPRulesOfCopyCln);
		verifyPayorTestEPRulesOfCopyClientIsDisplayed(actPayorTestEPRules);
		
		logger.info("*** Step 4 Action: Click Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 4 Expected Results: Billing Rules load page is displayed. Data is saved in cln_pyr_test_ep_rule,cln_pyr_test_ep_rule_excl,cln_pyr_grp_test_ep_rule_excl.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		verifyDataIsSavedInClnPyrTestEpRule(clnBillingRulesNoRd.getClnId(), payorTestEPRulesOfCopyCln);
		
		//Revert Data
		cleanDataPayorTestEPRules(clnBillingRulesNoRd.getClnId(), payorTestEPRulesOfCopyCln);
		cleanDataPayorTestEPRules(clnBillingRules.getClnId(), payorTestEPRulesOfCopyCln);
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Billing Rules section - Copy Billing Rules data from other ClientID")
	public void testXPR_1198() throws Throwable {
		logger.info("==== Testing - testXPR_1198 ====");

		clientBillingRules = new ClientBillingRules(driver, wait);

		logger.info("*** Step 1 Expected results: User login successful. Load Billing Rules page is displayed");
		verifyClientBillingRulesLoadPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Enter valid client Id to client input.");
		ClnBillingRules clnBillingRules = clientDao.getRandomClnBillingRuleDataFromCLN();
		Header expectedHeader = setValueToClientIdInput(clnBillingRules);
				
		logger.info("*** Step 2 Expected Results: Billing Rules detail page is displayed with correct client data.");
		Header actualHeader = getClientBillingRuleValues();
		assertEquals(actualHeader,expectedHeader,"        Billing Rules detail page is displayed with correct client data");
		
		logger.info("*** Step 3 Action: Billing Rules table - Add new record with valid data");
		clickHiddenPageObject(clientBillingRules.billingRulesTblAddBtn(), 0);
		BillingRules expectedBillingRules = setValuesInBillingRulesTbl(true);
		clickHiddenPageObject(clientBillingRules.billingRulesTblPopupOkBtn(), 0);
		
		logger.info("*** Step 3 Expected Results: New record is displayed correctly in the Billing Rules table.");
		filterRecordInBillingRules(expectedBillingRules);
		BillingRules actualBillingRules = getValuesInBillingRulesTable(LAST_OPTION);
		assertEquals(actualBillingRules, expectedBillingRules, "        New record is displayed correctly in the Billing Rules table.");
		
		logger.info("*** Step 4 Action: Click Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 4 Expected Results: System will take user return Billing Rules load page and a new record is saved in CLN_BILLING_RULES,"
											  + " cln_billing_rules_pyr_excl, cln_billing_rules_pyr_incl, cln_billing_rules_test_excl, cln_billing_rules_test_incl table.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		verifyBillingRuleDataIsSavedInDB(clnBillingRules.getClnId(), actualBillingRules, true);

		logger.info("*** Step 5 Action: Enter again clnId at step 2.");
		ClnBillingRules clnBillingRulesNoRd = clientDao.getRandomClnBillingRuleDataFromCLN();
		Header expectedHeader1 = setValueToClientIdInput(clnBillingRulesNoRd);
		
		logger.info("*** Step 5 Expected Results: Billing Rules detail page is displayed with correct client data.");
		Header actualHeader1 = getClientBillingRuleValues();
		assertEquals(actualHeader1,expectedHeader1, "        Billing Rules detail page is displayed with correct client data");
		
		logger.info("*** Step 6 Action: Billing Rules section - input the clienID of step 2 into ClientCopyId inputField and tab out.");
		enterValues(clientBillingRules.billingRulesSectionClientIdInput(),clnBillingRules.getClnAbbrev());
		
		logger.info("*** Step 6 Expected Results: Copy From section is displayed in Billing Rule section. "
											+ "- Billing Rules of Copy Client are displayed in Billing Rules table.");
		assertEquals(clientBillingRules.billingRulesSectionClientIdInput().getAttribute("value"), clnBillingRules.getClnAbbrev(), "        Copy Client input is displayed.");
		verifyBillingRulesOfCopyClientIsDisplayed(actualBillingRules);

		logger.info("*** Step 7 Action: Click Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 7 Expected Results: The new records in Billing Rule table are saved of 2 Cln in CLN_BILLING_RULES,"
											  + " cln_billing_rules_pyr_excl, cln_billing_rules_pyr_incl, cln_billing_rules_test_excl, cln_billing_rules_test_incl table will be same.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		verifyBillingRuleDataIsSavedInDB(clnBillingRulesNoRd.getClnId(), expectedBillingRules, true);
		
		logger.info("*** Step 8 Action: Revert data.");
		cleanDataBillingRules(clnBillingRules.getClnId(), actualBillingRules);
		cleanDataBillingRules(clnBillingRulesNoRd.getClnId(), actualBillingRules);
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Verify view document feature")
	public void testXPR_1199() throws Throwable {
		logger.info("===== Testing - testXPR_1199 =====");

		randomCharacter = new RandomCharacter();
		clientBillingRules = new ClientBillingRules(driver, wait);

		logger.info("*** Step 1 Expected Results: User login successful and Billing Rules load page is displayed.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Enter valid client id to Client Id inputField.");
		ClnBillingRules clnBillingRules = clientDao.getRandomClnBillingRuleDataFromCLN();
		Header expectedHeader = setValueToClientIdInput(clnBillingRules);
		
		logger.info("*** Step 2 Expected Results: Billing Rules detail page is displayed with correct client data.");
		Header actualHeader = getClientBillingRuleValues();
		assertEquals(actualHeader, expectedHeader, "        Billing Rules detail page is displayed.");
		
		logger.info("*** Step 3 Action: Header section - click on View Document link.");
		clickHiddenPageObject(clientBillingRules.headerViewDocumentLnk(), 0);
		
		logger.info("*** Step 3 Expected Results: Verify the document page of the clientID at step 2 is displayed.");
		verifyDocumentPageIsDisplayed(VIEW_DOC, clnBillingRules.getClnAbbrev());
		
		logger.info("*** Step 4 Action: Header section - click on View Client Portal Doc Up Document link.");
		clickHiddenPageObject(clientBillingRules.headerViewClientPortalDocUpDocument(), 0);
		
		logger.info("*** Step 4 Expected Results: Verify the docStoreWebApp page of the clientID at step 2 is displayed.");
		verifyDocumentPageIsDisplayed(VIEW_CLN_PORTAL_DOC, clnBillingRules.getClnAbbrev());
		
		driver.quit();
	}
	
	@Test(priority = 1, description = "Billing Rules section - Add new multi records with valid data")
	public void testXPR_1200() throws Throwable {
		logger.info("==== Testing - testXPR_1200 ====");

		clientBillingRules = new ClientBillingRules(driver, wait);

		logger.info("*** Step 1 Expected results: User login successful. Load Billing Rules page is displayed");
		verifyClientBillingRulesLoadPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Enter valid client Id to client input.");
		ClnBillingRules clnBillingRules = clientDao.getRandomClnBillingRuleDataFromCLN();
		Header expectedHeader = setValueToClientIdInput(clnBillingRules);
				
		logger.info("*** Step 2 Expected Results: Billing Rules detail page is displayed with correct client data.");
		Header actualHeader = getClientBillingRuleValues();
		assertEquals(actualHeader,expectedHeader,"        Billing Rules detail page is displayed with correct client data");
		
		logger.info("*** Step 3 Action: Billing Rules table - Add new multi records with valid data");
		List<BillingRules> expectedBillingRulesList = addMultiRecordsInBillingRulesTbl();
		
		logger.info("*** Step 3 Expected Results: New multi records are displayed correctly in the Billing Rules table.");
		filterRecordInBillingRules(expectedBillingRulesList.get(0));
		BillingRules actualBillingRules1 = getValuesInBillingRulesTable(LAST_OPTION);
		assertEquals(actualBillingRules1, expectedBillingRulesList.get(0),"        New record is displayed correctly in the Billing Rules table.");
		
		filterRecordInBillingRules(expectedBillingRulesList.get(1));
		BillingRules actualBillingRules2 = getValuesInBillingRulesTable(LAST_OPTION);
		assertEquals(actualBillingRules2, expectedBillingRulesList.get(1),"        New record is displayed correctly in the Billing Rules table.");
		
		logger.info("*** Step 4 Action: Click Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 4 Expected Results: System will take user return Billing Rules load page. New records are saved in CLN_BILLING_RULES, cln_billing_rules_pyr_excl, cln_billing_rules_pyr_incl, cln_billing_rules_test_excl, cln_billing_rules_test_incl table");
		verifyClientBillingRulesLoadPageIsDisplayed();
		int seqClnBillingRules1 = verifyBillingRuleDataIsSavedInClnBillingRules(clnBillingRules.getClnId(),actualBillingRules1,true);
		verifyBillingRuleDataIsSaveInMultiClnBillingRules(seqClnBillingRules1,2,false);
		int seqClnBillingRules2 = verifyBillingRuleDataIsSavedInClnBillingRules(clnBillingRules.getClnId(),actualBillingRules1,true);
		verifyBillingRuleDataIsSaveInMultiClnBillingRules(seqClnBillingRules2,2,false);
		
		logger.info("*** Step 5 Action: Revert data.");
		cleanDataBillingRules(clnBillingRules.getClnId(),actualBillingRules1) ;
		cleanDataBillingRules(clnBillingRules.getClnId(),actualBillingRules2) ;

		driver.quit();
	}
	
	@Test(priority = 1, description = "Payor Test EP Rules section - Add new multi records with valid data")
	public void testXPR_1201() throws Throwable {
		logger.info("==== Testing - testXPR_1201 ====");

		clientBillingRules = new ClientBillingRules(driver, wait);

		logger.info("*** Step 1 Expected results: User login successful. Load Billing Rules page is displayed");
		verifyClientBillingRulesLoadPageIsDisplayed();
		
		logger.info("*** Step 2 Action: Enter valid client Id to client input.");
		ClnBillingRules clnBillingRules = clientDao.getRandomClnBillingRuleDataFromCLN();
		Header expectedHeader = setValueToClientIdInput(clnBillingRules);
				
		logger.info("*** Step 2 Expected Results: Billing Rules detail page is displayed with correct client data.");
		Header actualHeader = getClientBillingRuleValues();
		assertEquals(actualHeader,expectedHeader,"        Billing Rules detail page is displayed with correct client data");
		
		logger.info("*** Step 3 Action: Payor Test Ep Rules table - Add multi record with valid data");
		PayorTestEPRules expectedpayorTestEPRules = addReCordInPayorTestEPRulesTbl();
		PayorTestEPRules expectedpayorTestEPRules1 = addMoreReCordsInPayorTestEPRulesTbl(expectedpayorTestEPRules);
		
		logger.info("*** Step 3 Expected Results: New record is displayed correctly in the Payor Test Ep Rules table.");
		PayorTestEPRules actualPayorTestEPRules = getValuesToPayorTestEPRulesWithEnterValue(expectedpayorTestEPRules, LAST_OPTION);
		assertEquals(actualPayorTestEPRules,expectedpayorTestEPRules, "        New record is displayed correctly in the Payor Test Ep Rules table");
		
		PayorTestEPRules actualPayorTestEPRules1 = getValuesToPayorTestEPRulesWithEnterValue(expectedpayorTestEPRules1, LAST_OPTION);
		assertEquals(actualPayorTestEPRules1,expectedpayorTestEPRules1, "        New record is displayed correctly in the Payor Test Ep Rules table");
		
		logger.info("*** Step 4 Action: Click Save and Clear button.");
		clickOnSaveAndClearBtn();
		
		logger.info("*** Step 4 Expected Results: Billing Rules load page is displayed. Data is saved in cln_pyr_test_ep_rule,cln_pyr_test_ep_rule_excl,cln_pyr_grp_test_ep_rule_excl.");
		verifyClientBillingRulesLoadPageIsDisplayed();
		verifyDataIsSavedInClnPyrTestEpRule(clnBillingRules.getClnId(), actualPayorTestEPRules);
		verifyDataIsSavedInClnPyrTestEpRule(clnBillingRules.getClnId(), actualPayorTestEPRules1);
		
		logger.info("*** Step 5 Action: Revert data.");
		cleanDataPayorTestEPRules(clnBillingRules.getClnId(), actualPayorTestEPRules);
		cleanDataPayorTestEPRules(clnBillingRules.getClnId(), actualPayorTestEPRules1);
		
		driver.quit();
	}

	/*** 
	 * Methods 
	 * 
	 ***/
	private BillingRules setValuesInBillingRulesTbl(boolean overrideValueClient) throws Exception {

		randomCharacter = new RandomCharacter(driver);
		BillingRules billingRules = new BillingRules();
		
		String priority = randomCharacter.getNonZeroRandomNumericString(3);
		PtTyp patientType = clientDao.getRandomPatientTypeDataFromPTTYP();
		PyrGrp pyrGrpIncl = rpmDao.getPyrGrp(testDb);
		PyrGrp pyrGrpExcl = rpmDao.getPyrGrp(testDb);
		while (pyrGrpIncl.equals(pyrGrpExcl)) {
			pyrGrpExcl = rpmDao.getPyrGrp(testDb);
		}
		String pyrIncl = rpmDao.getRandomPayorIdNotInPyrCntctFromPyr(testDb);
		String pyrExcl = rpmDao.getRandomPayorIdNotInPyrCntctFromPyr(testDb);
		while (pyrIncl.equals(pyrExcl)) {
			pyrExcl = rpmDao.getRandomPayorIdNotInPyrCntctFromPyr(testDb);
		}
		TestTyp testTypeIncl = testDao.getTestTyp();
		TestTyp testTypeExcl = testDao.getTestTyp();
		while (testTypeIncl.equals(testTypeExcl)) {
			testTypeExcl = testDao.getTestTyp();
		}
		com.mbasys.mars.ejb.entity.test.Test testIncls = testDao.getRandomTestByTestIdNotInTestFac();
		com.mbasys.mars.ejb.entity.test.Test testExcls = testDao.getRandomTestByTestIdNotInTestFac();
		while (testIncls.getTestAbbrev().equals(testExcls.getTestAbbrev())) {
			testExcls = testDao.getRandomTestByTestIdNotInTestFac();
		}
		ClnBillingRules cln = clientDao.getRandomClnBillingRuleDataFromCLN();
		String billPayorAbbrev = null; ;
		int billClnId = 0 ;
		int billPayorId = 0 ;
		String note = randomCharacter.getRandomAlphaString(6);
		
		enterValues(clientBillingRules.billingRulesTblPopupPriorityInput(), priority);
		clickHiddenPageObject(clientBillingRules.billingRulesTblPopupRetainInsuredInfoChk(),0);
		selectDropDownJQGridClickOnly(clientBillingRules.billingRulesTblPopupPatientTypeDdl(), patientType.getDescr());
		selectDropDownJQGridNoTagAClickOnly(clientBillingRules.billingRulesTblPopupPayorGroupInclusionDdl(), pyrGrpIncl.getGrpNm());
		selectDropDownJQGridNoTagAClickOnly(clientBillingRules.billingRulesTblPopupPayorGroupExclusionDdl(), pyrGrpExcl.getGrpNm());
		enterValues(clientBillingRules.billingRulesTblPopupPayorInclusionInput(), pyrIncl);
		enterValues(clientBillingRules.billingRulesTblPopupPayorExclusionInput(), pyrExcl);
		selectDropDownJQGridNoTagAClickOnly(clientBillingRules.billingRulesTblPopupTestTypeInclusionDdl(), testTypeIncl.getAbbrev());
		selectDropDownJQGridNoTagAClickOnly(clientBillingRules.billingRulesTblPopupTestTypeExclusionDdl(), testTypeExcl.getAbbrev());
		enterValues(clientBillingRules.billingRulesTblPopupTestInclusionInput(), testIncls.getTestAbbrev());
		enterValues(clientBillingRules.billingRulesTblPopupTestExclusionInput(), testExcls.getTestAbbrev());
		if (overrideValueClient) {
			billClnId = cln.getClnId();
			enterValues(clientBillingRules.billingRulesTblPopupBillClientIdInput(), cln.getClnAbbrev());
		} else {
			billPayorAbbrev = rpmDao.getRandomPayorIdNotInPyrCntctFromPyr(testDb);
			billPayorId = rpmDao.getPyrByPyrAbbrv(testDb, billPayorAbbrev).getPyrId();
			enterValues(clientBillingRules.billingRulesTblPopupBillPayorIdInput(), billPayorAbbrev);
		}
		enterValues(clientBillingRules.billingRulesTblPopupNotesInput(), note);
		
		billingRules.setPriority(Integer.parseInt(priority));
		billingRules.setRetainInsuredInfo(true);
		billingRules.setPatientTyp(patientType.getDescr());
		billingRules.setBillPyrGrpIncl(pyrGrpIncl.getGrpNm());
		billingRules.setBillPyrGrpExcl(pyrGrpExcl.getGrpNm());
		billingRules.setPayorInclusions(pyrIncl);
		billingRules.setPayorExclusions(pyrExcl);
		billingRules.setTestTypeIncl(testTypeIncl.getAbbrev());
		billingRules.setTestTypeExcl(testTypeExcl.getAbbrev());
		billingRules.setTestIncl(testIncls.getTestAbbrev());
		billingRules.setTestExcl(testExcls.getTestAbbrev());
		if (overrideValueClient) {
			billingRules.setBillClientId(billClnId);
			billingRules.setBillClientAbbrv(cln.getClnAbbrev());
			billingRules.setBillPayorId(0);
			billingRules.setBillPayorAbbrv(EMPTY);
		} else {
			billingRules.setBillClientId(0);
			billingRules.setBillClientAbbrv(EMPTY);
			billingRules.setBillPayorId(billPayorId);
			billingRules.setBillPayorAbbrv(billPayorAbbrev);
		}
		billingRules.setNotes(note);
		
		return billingRules;
	}
	
	private Header setValueToClientIdInput(ClnBillingRules clnBillingRules) throws Exception {
		Header header = new Header();
		enterValues(clientBillingRules.clientBillingRulesClientIdInput(), clnBillingRules.getClnAbbrev());
		xifinPortalUtils.waitForPageLoaded(wait);

		header.setClnId(clnBillingRules.getClnAbbrev());
		header.setClnName(ConvertUtil.standardizeString(clnBillingRules.getClnName()));
		header.setAccType(clnBillingRules.getAccTyp());
		header.setFacility(clnBillingRules.getFacAbbrev()+" - "+clnBillingRules.getFacName().replace("  ", " "));
		return header;
	}
	
	private PayorTestEPRules setValuesToPayorTestEPRulesWithEnterValue() throws Exception {
		PayorTestEPRules payorTestEPRules = new PayorTestEPRules();
		com.mbasys.mars.ejb.entity.test.Test test = testDao.getRandomTestByTestIdNotInTestFac();
		String pyrAbbr = rpmDao.getRandomPayorIdNotInPyrCntctFromPyr(testDb);
		PyrGrp pyrGrp = rpmDao.getPyrGrp(testDb);
		Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbr);

		clientBillingRules.enterPyrTestEPRulesSectionPopupTest(test.getTestAbbrev());
		clickOnElement(clientBillingRules.pyrTestEPRulesSectionPopupTestOnlyAllowRad());
		enterValues(clientBillingRules.pyrTestEPRulesSectionPopupPayorInput(),pyrAbbr);
		enterValues(clientBillingRules.pyrTestEPRulesSectionPopupPayorExclusionInput(),pyrAbbr);
		selectDropDownJQGridNoTagAClickOnly(clientBillingRules.pyrTestEPRulesSectionPopupPayorGroupExclusionDdl(),pyrGrp.getGrpNm());
		
		payorTestEPRules.setTestAbbrv(test.getTestAbbrev());
		payorTestEPRules.setOnlyAllowTestId(ONLY_ALLOW_LBL + test.getTestAbbrev());
		payorTestEPRules.setPayorAbbrv(pyrAbbr);
		payorTestEPRules.setPayorId(String.valueOf(pyr.getPyrId()));
		payorTestEPRules.setPayorExclusions(pyrAbbr);
		payorTestEPRules.setPayorGroupExclusions(pyrGrp.getGrpNm());
		return payorTestEPRules;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	private BillingRules setUpdateValuesInBillingRulesTbl(boolean overrideValueClient, BillingRules expectedBillingRules) throws Exception {

		randomCharacter = new RandomCharacter(driver);
		BillingRules billingRules = new BillingRules();
		
		String priority = randomCharacter.getNonZeroRandomNumericString(3);
		PtTyp patientType = clientDao.getRandomPatientTypeDataFromPTTYP();
		PyrGrp pyrGrpIncl = rpmDao.getPyrGrp(testDb);
		while (pyrGrpIncl.equals(expectedBillingRules.getBillPyrGrpIncl())
				|| pyrGrpIncl.equals(expectedBillingRules.getBillPyrGrpExcl())) {
			pyrGrpIncl = rpmDao.getPyrGrp(testDb);
		}
		PyrGrp pyrGrpExcl = rpmDao.getPyrGrp(testDb);
		while (pyrGrpExcl.equals(pyrGrpIncl) || pyrGrpExcl.equals(expectedBillingRules.getBillPyrGrpIncl())
				|| pyrGrpExcl.equals(expectedBillingRules.getBillPyrGrpExcl())) {
			pyrGrpExcl = rpmDao.getPyrGrp(testDb);
		}
		String pyrIncl = rpmDao.getRandomPayorIdNotInPyrCntctFromPyr(testDb);
		while (pyrIncl.equals(expectedBillingRules.getBillPyrIncl())
				|| pyrIncl.equals(expectedBillingRules.getBillPyrExcl())) {
			pyrIncl = rpmDao.getRandomPayorIdNotInPyrCntctFromPyr(testDb);
		}
		String pyrExcl = rpmDao.getRandomPayorIdNotInPyrCntctFromPyr(testDb);
		while (pyrExcl.equals(pyrIncl) || pyrExcl.equals(expectedBillingRules.getBillPyrIncl())
				|| pyrExcl.equals(expectedBillingRules.getBillPyrExcl())) {
			pyrExcl = rpmDao.getRandomPayorIdNotInPyrCntctFromPyr(testDb);
		}
		TestTyp testTypeIncl = testDao.getTestTyp();
		while (testTypeIncl.equals(expectedBillingRules.getTestTypeIncl())
				|| testTypeIncl.equals(expectedBillingRules.getTestTypeExcl())) {
			testTypeIncl = testDao.getTestTyp();
		}
		TestTyp testTypeExcl = testDao.getTestTyp();
		while (testTypeExcl.equals(testTypeIncl) || testTypeExcl.equals(expectedBillingRules.getTestTypeIncl())
				|| testTypeExcl.equals(expectedBillingRules.getTestTypeExcl())) {
			testTypeExcl = testDao.getTestTyp();
		}
		
		com.mbasys.mars.ejb.entity.test.Test testIncls = testDao.getRandomTestByTestIdNotInTestFac();
		while (testIncls.getTestAbbrev().equals(expectedBillingRules.getTestIncl())
				|| testIncls.getTestAbbrev().equals(expectedBillingRules.getTestExcl())) {
			testIncls = testDao.getRandomTestByTestIdNotInTestFac();
		}

		com.mbasys.mars.ejb.entity.test.Test testExcls = testDao.getRandomTestByTestIdNotInTestFac();
		while (testExcls.getTestAbbrev().equals(testIncls.getTestAbbrev())
				|| testExcls.getTestAbbrev().equals(expectedBillingRules.getTestIncl())
				|| testExcls.getTestAbbrev().equals(expectedBillingRules.getTestExcl())) {
			testExcls = testDao.getRandomTestByTestIdNotInTestFac();
		}
		ClnBillingRules cln = clientDao.getRandomClnBillingRuleDataFromCLN();
		String billPayorAbbrev = null; ;
		int billClnId = 0 ;
		int billPayorId = 0 ;
		String note = randomCharacter.getRandomAlphaString(6);

		enterValues(clientBillingRules.billingRulesTblPopupPriorityInput(), priority);
		clickHiddenPageObject(clientBillingRules.billingRulesTblPopupRetainInsuredInfoChk(),0);
		selectDropDownJQGridClickOnly(clientBillingRules.billingRulesTblPopupPatientTypeDdl(), patientType.getDescr());
		clickOnElement(clientBillingRules.billingRulesTblPopupPayorGroupInclusionDelIco());
		xifinPortalUtils.waitForPageLoaded(wait);
		selectDropDownJQGridNoTagAClickOnly(clientBillingRules.billingRulesTblPopupPayorGroupInclusionDdl(), pyrGrpIncl.getGrpNm());
		clickOnElement(clientBillingRules.billingRulesTblPopupPayorGroupExclusionDelIco());
		xifinPortalUtils.waitForPageLoaded(wait);
		selectDropDownJQGridNoTagAClickOnly(clientBillingRules.billingRulesTblPopupPayorGroupExclusionDdl(), pyrGrpExcl.getGrpNm());
		enterValues(clientBillingRules.billingRulesTblPopupPayorInclusionInput(), pyrIncl);
		enterValues(clientBillingRules.billingRulesTblPopupPayorExclusionInput(), pyrExcl);
		clickOnElement(clientBillingRules.billingRulesTblPopupTestTypeInclusionDelIco());
		xifinPortalUtils.waitForPageLoaded(wait);
		selectDropDownJQGridNoTagAClickOnly(clientBillingRules.billingRulesTblPopupTestTypeInclusionDdl(), testTypeIncl.getAbbrev());
		clickOnElement(clientBillingRules.billingRulesTblPopupTestTypeExclusionDelIco());
		xifinPortalUtils.waitForPageLoaded(wait);
		selectDropDownJQGridNoTagAClickOnly(clientBillingRules.billingRulesTblPopupTestTypeExclusionDdl(), testTypeExcl.getAbbrev());
		enterValues(clientBillingRules.billingRulesTblPopupTestInclusionInput(), testIncls.getTestAbbrev());
		enterValues(clientBillingRules.billingRulesTblPopupTestExclusionInput(), testExcls.getTestAbbrev());
		
		if (overrideValueClient) {
			billClnId = cln.getClnId();
			enterValues(clientBillingRules.billingRulesTblPopupBillClientIdInput(), cln.getClnAbbrev());
		} else {
			billPayorAbbrev = rpmDao.getRandomPayorIdNotInPyrCntctFromPyr(testDb);
			billPayorId = rpmDao.getPyrByPyrAbbrv(testDb, billPayorAbbrev).getPyrId();
			enterValues(clientBillingRules.billingRulesTblPopupBillPayorIdInput(), billPayorId);
		}
		enterValues(clientBillingRules.billingRulesTblPopupNotesInput(), note);
		
		billingRules.setPriority(Integer.parseInt(priority));
		billingRules.setRetainInsuredInfo(false);
		billingRules.setPatientTyp(patientType.getDescr());
		billingRules.setBillPyrGrpIncl(pyrGrpIncl.getGrpNm());
		billingRules.setBillPyrGrpExcl(pyrGrpExcl.getGrpNm());
		billingRules.setPayorInclusions(pyrIncl);
		billingRules.setPayorExclusions(pyrExcl);
		billingRules.setTestTypeIncl(testTypeIncl.getAbbrev());
		billingRules.setTestTypeExcl(testTypeExcl.getAbbrev());
		billingRules.setTestIncl(testIncls.getTestAbbrev());
		billingRules.setTestExcl(testExcls.getTestAbbrev());
		if (overrideValueClient) {
			billingRules.setBillClientId(billClnId);
			billingRules.setBillClientAbbrv(cln.getClnAbbrev());
			billingRules.setBillPayorId(0);
			billingRules.setBillPayorAbbrv(EMPTY);
		} else {
			billingRules.setBillClientId(0);
			billingRules.setBillClientAbbrv(EMPTY);
			billingRules.setBillPayorId(billPayorId);
			billingRules.setBillPayorAbbrv(billPayorAbbrev);
		}
		billingRules.setNotes(note);
		
		return billingRules;
	}
	
	private String setClientSearchValues() throws XifinDataAccessException, Exception {
		String npiRandomId = npiDao.getRandomNpiIdFromNpi();
		enterValues(clientSearch.npiInput(), npiRandomId);
		clickHiddenPageObject(clientSearch.clientSearchSearchButton(), 0);
		
		return npiRandomId;
	}
	
	private Header setExpectedHeaderValues(ClnBillingRules clnBillingRules) throws Exception {
		Header header = new Header();
		header.setClnId(clnBillingRules.getClnAbbrev());
		header.setClnName(clnBillingRules.getClnName());
		header.setAccType(clnBillingRules.getAccTyp());
		header.setFacility(clnBillingRules.getFacAbbrev()+" - "+clnBillingRules.getFacName().replace("  ", " "));
		return header;
	}
	
	private BillingRules getValuesInBillingRulesTable(String row) throws Exception {

		clientBillingRules = new ClientBillingRules(driver, wait);
		BillingRules actualBillingRules = new BillingRules();
		
		String priority = clientBillingRules.billingRulesTblPriorityColTxt(row).getText();
		String billClientId = clientBillingRules.billingRulesTblBillClientIdColTxt(row).getText().equals(" ")?EMPTY:clientBillingRules.billingRulesTblBillClientIdColTxt(row).getText();
		String billPayorId = clientBillingRules.billingRulesTblBillPayorIdColTxt(row).getText().equals(" ")?EMPTY:clientBillingRules.billingRulesTblBillPayorIdColTxt(row).getText();
		boolean retainInsured = Boolean.parseBoolean(clientBillingRules.billingRulesTblRetainInsuredInfoColChk(LAST_OPTION).getAttribute("value"));
		
		String[] temp = clientBillingRules.billingRulesTblInclusionsExclusionsColTxt(row).getAttribute("title").split(PATIENT_TYPE_INCL_LBL);
		String[] patientTypeIncl = temp[1].split(PAYOR_GROUP_INCL_LBL);
		String[] payorGroupIncl = patientTypeIncl[1].split(PAYOR_INCL_LBL);
		String[] payorIncl = payorGroupIncl[1].split(PYR_GRP_EXCL_LBL);
		String[] payorGroupExcl = payorIncl[1].split(PYR_EXCL_LBL);
		String[] payorExcl = payorGroupExcl[1].split(TEST_TYPE_INCL_LBL);
		String[] testTypeIncl = payorExcl[1].split(TEST_INCL_LBL);
		String[] testIncl = testTypeIncl[1].split(TEST_TYPE_EXCL_LBL);
		String[] testTypeExcl = testIncl[1].split(TEST_EXCL_LBL);
		
		clickHiddenPageObject(clientBillingRules.billingRulesTblNoteColHiddenTxt(row), 0);
		String note = clientBillingRules.billingRulesNotePopupNoteTxt().getText();
		clickOnElement(clientBillingRules.billingRulesNotePopupOkBtn());

		actualBillingRules.setPriority(Integer.parseInt(priority));
		actualBillingRules.setRetainInsuredInfo(retainInsured);
		actualBillingRules.setPatientTyp(patientTypeIncl[0]);
		actualBillingRules.setBillPyrGrpIncl(payorGroupIncl[0]);
		actualBillingRules.setBillPyrGrpExcl(payorGroupExcl[0]);
		actualBillingRules.setPayorInclusions(payorIncl[0]);
		actualBillingRules.setPayorExclusions(payorExcl[0]);
		actualBillingRules.setTestTypeIncl(testTypeIncl[0]);
		actualBillingRules.setTestTypeExcl(testTypeExcl[0]);
		actualBillingRules.setTestIncl(testIncl[0]);
		actualBillingRules.setTestExcl(testTypeExcl[1]);
		actualBillingRules.setBillClientAbbrv(billClientId);
		actualBillingRules.setBillPayorAbbrv(billPayorId);
		if (!billClientId.equals(EMPTY)) {
			actualBillingRules.setBillClientId(rpmDao.getClnByClnAbbrev(testDb, billClientId).getClnId());
		}
		if (!billPayorId.equals(EMPTY)) {
			actualBillingRules.setBillPayorId(rpmDao.getPyrByPyrAbbrv(testDb, billPayorId).getPyrId());
		}
		actualBillingRules.setNotes(note);
		return actualBillingRules;
	}
	
	private PayorTestEPRules getValuesToPayorTestEPRulesWithEnterValue(PayorTestEPRules expectedPayorTestEPRules, String row) throws Exception {
		PayorTestEPRules payorTestEPRules = new PayorTestEPRules();
		
		filterRecordInPayorTestEPRulesTbl(expectedPayorTestEPRules);
		Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, clientBillingRules.pyrTestEPRulesTblPayorColTxt(row).getText().trim());
		String[] temp = clientBillingRules.pyrTestEPRulesTblInclusionsExclusionsColTxt(row).getAttribute("title").split(PYR_GRP_EXCL_LBL);
		String[] inclExl = temp[1].split(PYR_EXCL_LBL);
		for (int i = 0; i < inclExl.length;) {
			payorTestEPRules.setPayorGroupExclusions(inclExl[i].trim());
			payorTestEPRules.setPayorExclusions(inclExl[++i].trim());
			break;
		}
		
		if (!expectedPayorTestEPRules.getOnlyAllowTestId().equals(EMPTY)) {
			payorTestEPRules.setTestAbbrv(clientBillingRules.pyrTestEPRulesTblTestIdColTxt(row).getText().split(ONLY_ALLOW_LBL)[1].trim());
		} else {
			payorTestEPRules.setTestAbbrv(clientBillingRules.pyrTestEPRulesTblTestIdColTxt(row).getText().split(DONT_ALLOW_LBL)[0].trim());
		}
		
		payorTestEPRules.setOnlyAllowTestId(clientBillingRules.pyrTestEPRulesTblTestIdColTxt(row).getAttribute("title"));
		payorTestEPRules.setPayorGroupAbbrv(clientBillingRules.pyrTestEPRulesTblPayorGroupColTxt(row).getText().trim());
		payorTestEPRules.setPayorAbbrv(clientBillingRules.pyrTestEPRulesTblPayorColTxt(row).getText().trim());
		payorTestEPRules.setPayorId(String.valueOf(pyr.getPyrId()));
		
		return payorTestEPRules;
	}
	
	private PayorTestEPRules getValuesFromPayorTestEPRulesPopup() throws Exception {
		PayorTestEPRules payorTestEPRules = new PayorTestEPRules();
		clientBillingRules = new ClientBillingRules(driver, wait);
		
		String testAbbrev = clientBillingRules.pyrTestEPRulesSectionPopupTestInput().getAttribute("value");
		String pyrAbbr = clientBillingRules.pyrTestEPRulesSectionPopupPayorInput().getAttribute("value");
		String payorGroupExcl = clientBillingRules.pyrTestEPRulesSectionPopupPayorExclusionInput().getAttribute("value");
		String payorExcl = clientBillingRules.pyrTestEPRulesSectionPopupPayorGroupExclusionDdl().getText();
		Pyr pyr = rpmDao.getPyrByPyrAbbrv(testDb, pyrAbbr);
		
		payorTestEPRules.setTestAbbrv(testAbbrev);
		payorTestEPRules.setOnlyAllowTestId(ONLY_ALLOW_LBL+testAbbrev);
		payorTestEPRules.setPayorAbbrv(pyrAbbr);
		payorTestEPRules.setPayorId(String.valueOf(pyr.getPyrId()));
		payorTestEPRules.setPayorExclusions(payorGroupExcl);
		payorTestEPRules.setPayorGroupExclusions(payorExcl);
		return payorTestEPRules;
	}

	private List<ClientSearchResult> getValueOfClientSearchResultGridByNpi(String npiId) throws Exception {
		List<ClientSearchResult> expClientSearchResults = new ArrayList<>();
		List<WebElement> lstRows = clientSearchResults.clientSearchResultTblAllDataRows();
		for (int i = 1; i <= lstRows.size(); i++) {
			String index = String.valueOf(i + 1);
			String clnAbbrev = clientSearchResults.clientSearchResultsTblClientIDColLink(index).getText().trim();
			String clnName = clientSearchResults.clientSearchResultsTblClientNameColText(index).getText().trim();
			String add1 = clientSearchResults.address1InClientSearchResults(index).getText().trim();
			String add2 = clientSearchResults.address2InClientSearchResults(index).getText().trim();
			String city = clientSearchResults.clientSearchResultTblCityTxt(index).getText().trim();
			String state = clientSearchResults.clientSearchResultTblStateTxt(index).getText().trim();
			String postalCd = clientSearchResults.clientSearchResultTblPostalCodeTxt(index).getText().trim();
			String country = clientSearchResults.clientSearchResultTblCountryTxt(index).getText().trim();
			String accountTyp = clientSearchResults.clientSearchResultTblAccountTypeTxt(index).getText().trim();
			String primaryFac = clientSearchResults.clientSearchResultTblPrimaryFacilityTxt(index).getText().trim();
			String balDue = clientSearchResults.clientSearchResultTblBalanceDueTxt(index).getText().trim().replace("$", EMPTY);

			ClientSearchResult clientSearchResult = new ClientSearchResult();
			clientSearchResult.setClnAbbrev(clnAbbrev);
			clientSearchResult.setClnName(clnName);
			clientSearchResult.setAdd1(add1);
			clientSearchResult.setAdd2(add2);
			clientSearchResult.setCity(city);
			clientSearchResult.setState(state);
			clientSearchResult.setPostalCd(postalCd);
			clientSearchResult.setCountry(country);
			clientSearchResult.setAccountTyp(accountTyp);
			clientSearchResult.setPrimaryFac(primaryFac);
			clientSearchResult.setBalDue(ConvertUtil.convertStringToDouble(balDue));
			expClientSearchResults.add(clientSearchResult);
		}

		return expClientSearchResults;
	}

	private Header getClientBillingRuleValues() throws Throwable {
		clientBillingRules = new ClientBillingRules(driver, wait);
		
		String headerClnAbbrev = clientBillingRules.headerClientIdTxt().getText();
		String headerClnNm = ConvertUtil.standardizeString(clientBillingRules.headerClientNmTxt().getText());
		String headerAcctTyp = clientBillingRules.headerClientAccountTypeTxt().getText();
		String headerFacAbbrev = clientBillingRules.headerClientOrderingFacAbbrevTxt().getText();
		String headerFacNm = clientBillingRules.headerClientOrderingFacNameTxt().getText();
		String fac = headerFacAbbrev +" - "+ headerFacNm;
		
		Header header = new Header();
		header.setClnId(headerClnAbbrev);
		header.setClnName(headerClnNm);
		header.setAccType(headerAcctTyp);
		header.setFacility(fac);		
		
		return header;
	}
	
	private void filterRecordInBillingRules(BillingRules billingRules)throws Exception {

		clientBillingRules = new ClientBillingRules(driver, wait);

		enterValues(clientBillingRules.billingRulesTblPriorityColFilterInput(), billingRules.getPriority());
		enterValues(clientBillingRules.billingRulesTblBillClientIdColFilterInput(), billingRules.getBillClientAbbrv());
		enterValues(clientBillingRules.billingRulesTblBillPayorIdColFilterInput(), billingRules.getBillPayorAbbrv());
		int totalRecordInResultSearch = Integer.parseInt(getTotalResultSearch(clientBillingRules.billingRulesTblTotalRecordTxt()));
		assertTrue(totalRecordInResultSearch > 0, "        Exist a record in table.");
	}
	
	private void filterRecordInPayorTestEPRulesTbl(PayorTestEPRules payorTestEPRules)throws Exception {
		enterValues(clientBillingRules.pyrTestEPRulesTblTestIdColFilterInput(), payorTestEPRules.getTestAbbrv());
		enterValues(clientBillingRules.pyrTestEPRulesTblTestTypeColFilterInput(), payorTestEPRules.getTestTypId());
		if (!payorTestEPRules.getPayorGroupAbbrv().equals("0")) {
			enterValues(clientBillingRules.pyrTestEPRulesTblPayorGroupColFilterInput(), payorTestEPRules.getPayorGroupAbbrv());
		}
		enterValues(clientBillingRules.pyrTestEPRulesTblPayorColFilterInput(), payorTestEPRules.getPayorAbbrv());
		enterValues(clientBillingRules.pyrTestEPRulesTblInclusionsExclusionsColFilterInput(), payorTestEPRules.getPayorExclusions());
	}
	
	private PayorTestEPRules addReCordInPayorTestEPRulesTbl() throws Exception {
		clickOnElement(clientBillingRules.pyrTestEPRulesTblAddBtn());
		PayorTestEPRules payorTestEPRules = setValuesToPayorTestEPRulesWithEnterValue();
		clickOnElement(clientBillingRules.pyrTestEPRulesSectionPopupOkBtn());
		
		return payorTestEPRules;
	}
	
	private PayorTestEPRules addMoreReCordsInPayorTestEPRulesTbl(PayorTestEPRules payorTestEPRules) throws Exception {
		clickOnElement(clientBillingRules.pyrTestEPRulesTblAddBtn());
		PayorTestEPRules payorTestEPRule = setValuesToPayorTestEPRulesWithEnterValue();
		while (payorTestEPRule.getPayorAbbrv().equals(payorTestEPRules.getPayorAbbrv())) {
			payorTestEPRule = setValuesToPayorTestEPRulesWithEnterValue();
		}
		clickOnElement(clientBillingRules.pyrTestEPRulesSectionPopupOkBtn());
		
		return payorTestEPRule;
	}
	
	private PayorTestEPRules updatePayorTestEpRulesPayorGroup(PayorTestEPRules expectedpayorTestEPRules, String row) throws Exception {
		PyrGrp pyrGrp = rpmDao.getPyrGrp(testDb);
		while (expectedpayorTestEPRules.getPayorGroupExclusions().equals(pyrGrp.getGrpNm())) {
			pyrGrp = rpmDao.getPyrGrp(testDb);
		}
		
		filterRecordInPayorTestEPRulesTbl(expectedpayorTestEPRules);
		clickOnElement(clientBillingRules.pyrTestEPRulesTblTestIdColTxt(row));
		clickOnElement(clientBillingRules.pyrTestEPRulesTblEditBtn());
		clickOnElement(clientBillingRules.pyrTestEPRulesTblTestIdColTxt(row));
		clickOnElement(clientBillingRules.pyrTestEPRulesSectionPopupPayorGroupExclusionDelIco());
		clientBillingRules.clearAllTestEPRulesSectionPopupPayorGroupExclusion();
		enterValues(clientBillingRules.pyrTestEPRulesSectionPopupPayorGroupExclusionInput(),pyrGrp.getGrpNm());
		clickOnElement(clientBillingRules.pyrTestEPRulesSectionPopupOkBtn());
		
		expectedpayorTestEPRules.setPayorGroupExclusions(pyrGrp.getGrpNm());
		
		return expectedpayorTestEPRules;
	}
	
	private void clickOnDeletedCheckbox(PayorTestEPRules expectedpayorTestEPRules, String row) throws Exception {
		filterRecordInPayorTestEPRulesTbl(expectedpayorTestEPRules);
		clickOnElement(clientBillingRules.pyrTestEPRulesTblDeletedColChk(row));
	}

	private void clickOnSaveAndClearBtn() throws Exception {
		clickHiddenPageObject(clientBillingRules.footerSaveAndClearBtn(),0);
		xifinPortalUtils.waitForPageLoaded(wait);
	}

	private void clickOnResetBtn() throws Exception {
		clickHiddenPageObject(clientBillingRules.footerResetBtn(), 0);
	}

	private void cleanDataPayorTestEPRules(int clnId,PayorTestEPRules payorTestEPRules) throws Exception {
		ClnPyrTestEpRule clnPyrTestEpRule = clientDao.getClientPyrEpFromClnPyrTestEpRuleByClnId(clnId, Integer.parseInt(payorTestEPRules.getPayorId()));
		ClnPyrTestEpRuleExcl clnPyrTestEpRuleExcl = clientDao.getClientPyrEpFromClnPyrTestEpRuleExclByCpTerSeqId(clnPyrTestEpRule.getSeqId(), Integer.parseInt(payorTestEPRules.getPayorId()));
		int pyrGrpExclUI = rpmDao.getPyrGrpByGrpNm(testDb, payorTestEPRules.getPayorGroupExclusions()).getPyrGrpId();
		ClnPyrGrpTestEpRuleExcl clnPyrGrpTestEpRuleExcl = clientDao.getClientPyrEpFromClnPyrGrpTestEpRuleExclByCpTerSeqId(clnPyrTestEpRule.getSeqId(), pyrGrpExclUI);
		
		clnPyrTestEpRule.setResultCode(ErrorCodeMap.DELETED_RECORD);
		clnPyrTestEpRuleExcl.setResultCode(ErrorCodeMap.DELETED_RECORD);
		clnPyrGrpTestEpRuleExcl.setResultCode(ErrorCodeMap.DELETED_RECORD);
		
		clientDao.setClnPyrGrpTestEpRuleExcl(clnPyrGrpTestEpRuleExcl);
		clientDao.setClnPyrTestEpRuleExcl(clnPyrTestEpRuleExcl);
		clientDao.setClnPyrTestEpRule(clnPyrTestEpRule);
	}
	
	private void cleanDataBillingRules(int clnId, BillingRules billingRules) throws Exception {
		com.mbasys.mars.ejb.entity.clnBillingRules.ClnBillingRules br = clientDao.getDataFromClnBillingRulesByClnId(clnId, billingRules.getPriority(), billingRules.getBillClientId(), billingRules.getBillPayorId());
		List<ClnBillingRulesPyrExcl> clnBillingRulesPyrExcls = clientDao.getListDataFromClnBillingRulesPyrExclByClnBillingRuleId(br.getSeqId());
		List<ClnBillingRulesPyrIncl> clnBillingRulesPyrIncls = clientDao.getListDataFromClnBillingRulesPyrInclByClnBillingRuleId(br.getSeqId());
		List<ClnBillingRulesTestExcl> clnBillingRulesTestExcls = clientDao.getListDataFromClnBillingRulesTestExclByClnBillingRuleId(br.getSeqId());
		List<ClnBillingRulesTestIncl> clnBillingRulesTestIncls = clientDao.getListDataFromClnBillingRulesTestInclByClnBillingRuleId(br.getSeqId());

		if (clnBillingRulesPyrExcls.size() > 0) {
			for (ClnBillingRulesPyrExcl clnBillingRulesPyrExcl : clnBillingRulesPyrExcls) {
				clnBillingRulesPyrExcl.setResultCode(ErrorCodeMap.DELETED_RECORD);
				clientDao.setClnBillingRulePyrExcl(clnBillingRulesPyrExcl);
			}
		}

		if (clnBillingRulesPyrIncls.size() > 0) {
			for (ClnBillingRulesPyrIncl clnBillingRulesPyrIncl : clnBillingRulesPyrIncls) {
				clnBillingRulesPyrIncl.setResultCode(ErrorCodeMap.DELETED_RECORD);
				clientDao.setClnBillingRulesPyrIncl(clnBillingRulesPyrIncl);
			}
		}

		if (clnBillingRulesTestExcls.size() > 0) {
			for (ClnBillingRulesTestExcl clnBillingRulesTestExcl : clnBillingRulesTestExcls) {
				clnBillingRulesTestExcl.setResultCode(ErrorCodeMap.DELETED_RECORD);
				clientDao.setClnBillingRulesTestExcl(clnBillingRulesTestExcl);
			}
		}

		if (clnBillingRulesTestIncls.size() > 0) {
			for (ClnBillingRulesTestIncl clnBillingRulesTestIncl : clnBillingRulesTestIncls) {
				clnBillingRulesTestIncl.setResultCode(ErrorCodeMap.DELETED_RECORD);
				clientDao.setClnBillingRulesTestIncl(clnBillingRulesTestIncl);
			}
		}

		br.setResultCode(ErrorCodeMap.DELETED_RECORD);
		clientDao.setClnBillingRules(br);
	}

	private void verifyNoRecordSavedIntoBillingRuleTbl(int clnId, BillingRules billingRules) throws XifinDataAccessException {
		com.mbasys.mars.ejb.entity.clnBillingRules.ClnBillingRules br = clientDao.getDataFromClnBillingRulesByClnId(clnId, billingRules.getPriority(), billingRules.getBillClientId(), billingRules.getBillPayorId());
		assertTrue(br.getSeqId() == 0,"        No records is saved in DB.");
	}

	private void verifyDocumentPageIsDisplayed(String docType, String ClnAbbrev) throws Exception {
		String parentWindow = switchToPopupWin();

		switch (docType) {
			case VIEW_DOC:
				String url = "xifinportal/docstorewebapp?id=" + ClnAbbrev;
				assertTrue(driver.getCurrentUrl().contains(url), "        View Document Page Is Displayed.");
				break;

			case VIEW_CLN_PORTAL_DOC:
				url = "imaging.xifin.com/?ClientID=" + ClnAbbrev;
				assertTrue(driver.getCurrentUrl().contains(url), "        View Client Portal Doc UpDocument Page Is Displayed.");
				break;
		}

		driver.close();
		switchToParentWin(parentWindow);	
	}

	private void verifyBillingRuleDataIsSavedInDB(int clnId, BillingRules billingRules, boolean overrideValueClient) throws XifinDataAccessException, XifinDataNotFoundException {
		com.mbasys.mars.ejb.entity.clnBillingRules.ClnBillingRules br = clientDao.getDataFromClnBillingRulesByClnId(clnId, billingRules.getPriority(), billingRules.getBillClientId(), billingRules.getBillPayorId());
		List<ClnBillingRulesPyrExcl> clnBillingRulesPyrExcls = clientDao.getListDataFromClnBillingRulesPyrExclByClnBillingRuleId(br.getSeqId());
		List<ClnBillingRulesPyrIncl> clnBillingRulesPyrIncls = clientDao.getListDataFromClnBillingRulesPyrInclByClnBillingRuleId(br.getSeqId());
		List<ClnBillingRulesTestExcl> clnBillingRulesTestExcls = clientDao.getListDataFromClnBillingRulesTestExclByClnBillingRuleId(br.getSeqId());
		List<ClnBillingRulesTestIncl> clnBillingRulesTestIncls = clientDao.getListDataFromClnBillingRulesTestInclByClnBillingRuleId(br.getSeqId());

		assertTrue(br.getSeqId() > 0,"        Data is saved in CLN_BILLING_RULES.");
		assertTrue(clnBillingRulesPyrExcls.size() == 2, "        Data is saved in CLN_BILLING_RULES_PYR_EXCL.");
		assertTrue(clnBillingRulesPyrIncls.size() == 2, "        Data is saved in CLN_BILLING_RULES_PYR_INCL.");
		assertTrue(clnBillingRulesTestExcls.size() == 2, "        Data is saved in CLN_BILLING_RULES_TEST_EXCL.");
		assertTrue(clnBillingRulesTestIncls.size() == 2, "        Data is saved in CLN_BILLING_RULES_TEST_INCL.");

		if (!overrideValueClient) {
			assertTrue(br.getBillPyrId() == billingRules.getBillPayorId(),"        Fk_Bill_Pyr_Id column is saved correctly in CLN_BILLING_RULES table.");
		}
	}

	private int verifyBillingRuleDataIsNotExistInClnBillingRules(int clnId, BillingRules billingRules) throws XifinDataAccessException, XifinDataNotFoundException {
		com.mbasys.mars.ejb.entity.clnBillingRules.ClnBillingRules br = clientDao.getDataFromClnBillingRulesByClnId(clnId, billingRules.getPriority(), billingRules.getBillClientId(), billingRules.getBillPayorId());
		assertTrue(br.getSeqId() == 0,"        Data is saved in CLN_BILLING_RULES.");
		return br.getSeqId();
	}

	private int verifyBillingRuleDataIsSavedInClnBillingRules(int clnId, BillingRules billingRules, boolean overrideValueClient) throws XifinDataAccessException, XifinDataNotFoundException {
		com.mbasys.mars.ejb.entity.clnBillingRules.ClnBillingRules br = clientDao.getDataFromClnBillingRulesByClnId(clnId, billingRules.getPriority(), billingRules.getBillClientId(), billingRules.getBillPayorId());
		assertTrue(br.getSeqId() > 0 ,"        Data is saved in CLN_BILLING_RULES.");
		if (!overrideValueClient) {
			assertTrue(br.getBillPyrId() == billingRules.getBillPayorId(),"        Fk_Bill_Pyr_Id column is saved correctly in CLN_BILLING_RULES table.");
		}
		return br.getSeqId();
	}

	private List<Integer> verifyBillingRuleDataIsSaveInMultiClnBillingRules(int seqClnBillingRules, int totoRecord, boolean flagSave) throws XifinDataAccessException, XifinDataNotFoundException {
		List<Integer> seqs = new ArrayList<>();
		List<ClnBillingRulesPyrExcl> clnBillingRulesPyrExcls = clientDao.getListDataFromClnBillingRulesPyrExclByClnBillingRuleId(seqClnBillingRules);
		List<ClnBillingRulesPyrIncl> clnBillingRulesPyrIncls = clientDao.getListDataFromClnBillingRulesPyrInclByClnBillingRuleId(seqClnBillingRules);
		List<ClnBillingRulesTestExcl> clnBillingRulesTestExcls = clientDao.getListDataFromClnBillingRulesTestExclByClnBillingRuleId(seqClnBillingRules);
		List<ClnBillingRulesTestIncl> clnBillingRulesTestIncls = clientDao.getListDataFromClnBillingRulesTestInclByClnBillingRuleId(seqClnBillingRules);
		assertTrue(clnBillingRulesPyrExcls.size() == totoRecord, "        Data is saved in CLN_BILLING_RULES_PYR_EXCL.");
		assertTrue(clnBillingRulesPyrIncls.size() == totoRecord, "        Data is saved in CLN_BILLING_RULES_PYR_INCL.");
		assertTrue(clnBillingRulesTestExcls.size() == totoRecord, "        Data is saved in CLN_BILLING_RULES_TEST_EXCL.");
		assertTrue(clnBillingRulesTestIncls.size() == totoRecord, "        Data is saved in CLN_BILLING_RULES_TEST_INCL.");
		if (flagSave) {
			seqs.add(clnBillingRulesPyrExcls.get(0).getSeqId());
			seqs.add(clnBillingRulesPyrExcls.get(1).getSeqId());
			seqs.add(clnBillingRulesPyrIncls.get(0).getSeqId());
			seqs.add(clnBillingRulesPyrIncls.get(1).getSeqId());
			seqs.add(clnBillingRulesTestExcls.get(0).getSeqId());
			seqs.add(clnBillingRulesTestExcls.get(1).getSeqId());
			seqs.add(clnBillingRulesTestIncls.get(0).getSeqId());
			seqs.add(clnBillingRulesTestIncls.get(1).getSeqId());
		}
		return seqs;
	}

	private void verifyBillingRuleDataIsDeletedAndInsertInMultiClnBillingRules(int seqClnBillingRules,List<Integer> seqs) throws XifinDataAccessException, XifinDataNotFoundException {
		List<ClnBillingRulesPyrExcl> clnBillingRulesPyrExcls = clientDao.getListDataFromClnBillingRulesPyrExclByClnBillingRuleId(seqClnBillingRules);
		List<ClnBillingRulesPyrIncl> clnBillingRulesPyrIncls = clientDao.getListDataFromClnBillingRulesPyrInclByClnBillingRuleId(seqClnBillingRules);
		List<ClnBillingRulesTestExcl> clnBillingRulesTestExcls = clientDao.getListDataFromClnBillingRulesTestExclByClnBillingRuleId(seqClnBillingRules);
		List<ClnBillingRulesTestIncl> clnBillingRulesTestIncls = clientDao.getListDataFromClnBillingRulesTestInclByClnBillingRuleId(seqClnBillingRules);
		assertTrue(clnBillingRulesPyrExcls.size() == 2, "        Data is added in CLN_BILLING_RULES_PYR_EXCL.");
		assertTrue(clnBillingRulesPyrIncls.size() == 2, "        Data is added in CLN_BILLING_RULES_PYR_INCL.");
		assertTrue(clnBillingRulesTestExcls.size() == 2, "        Data is added in CLN_BILLING_RULES_TEST_EXCL.");
		assertTrue(clnBillingRulesTestIncls.size() == 2, "        Data is added in CLN_BILLING_RULES_TEST_INCL.");
		assertNotEquals(clnBillingRulesPyrExcls.get(0),seqs.get(0), "        Data is deleted in CLN_BILLING_RULES_PYR_EXCL.");
		assertNotEquals(clnBillingRulesPyrExcls.get(1),seqs.get(1), "        Data is deleted in CLN_BILLING_RULES_PYR_EXCL.");
		assertNotEquals(clnBillingRulesPyrIncls.get(0),seqs.get(2), "        Data is deleted in CLN_BILLING_RULES_PYR_INCL.");
		assertNotEquals(clnBillingRulesPyrIncls.get(1),seqs.get(3), "        Data is deleted in CLN_BILLING_RULES_PYR_INCL.");
		assertNotEquals(clnBillingRulesTestExcls.get(0),seqs.get(4), "        Data is deleted in CLN_BILLING_RULES_TEST_EXCL.");
		assertNotEquals(clnBillingRulesTestExcls.get(1),seqs.get(5), "        Data is deleted in CLN_BILLING_RULES_TEST_EXCL.");
		assertNotEquals(clnBillingRulesTestIncls.get(0),seqs.get(6), "        Data is deleted in CLN_BILLING_RULES_TEST_INCL.");
		assertNotEquals(clnBillingRulesTestIncls.get(1),seqs.get(7), "        Data is deleted in CLN_BILLING_RULES_TEST_INCL.");

	}

	private void verifyClientBillingRulesLoadPageIsDisplayed() throws Exception {
		clientBillingRules = new ClientBillingRules(driver, wait);

		wait.until(ExpectedConditions.elementToBeClickable(clientBillingRules.clientBillingRulesLoadPgTitleTxt()));
		assertTrue(clientBillingRules.clientBillingRulesLoadPgTitleTxt().getText().equals(BILLING_RULES_TITLE),"        The title of Billing Rules page should be 'Billing Rules'.");
	}

	private void verifyDataIsSavedInClnPyrTestEpRule(int clnId,PayorTestEPRules payorTestEPRules) throws Exception {
		ClnPyrTestEpRule clnPyrTestEpRule = clientDao.getClientPyrEpFromClnPyrTestEpRuleByClnId(clnId, Integer.parseInt(payorTestEPRules.getPayorId()));
		ClnPyrTestEpRuleExcl clnPyrTestEpRuleExcl = clientDao.getClientPyrEpFromClnPyrTestEpRuleExclByCpTerSeqId(clnPyrTestEpRule.getSeqId(), Integer.parseInt(payorTestEPRules.getPayorId()));
		int pyrGrpExclUI = rpmDao.getPyrGrpByGrpNm(testDb, payorTestEPRules.getPayorGroupExclusions()).getPyrGrpId();
		ClnPyrGrpTestEpRuleExcl clnPyrGrpTestEpRuleExcl = clientDao.getClientPyrEpFromClnPyrGrpTestEpRuleExclByCpTerSeqId(clnPyrTestEpRule.getSeqId(), pyrGrpExclUI);
		com.mbasys.mars.ejb.entity.test.Test test = rpmDao.getTestByTestAbbrev(testDb, payorTestEPRules.getTestAbbrv());

		assertTrue(clnPyrTestEpRule.seqId != 0, "        Data is saved in CLN_PYR_TEST_EP_RULE table.");
		assertTrue(clnPyrTestEpRuleExcl.seqId != 0, "        Data is saved in CLN_PYR_TEST_EP_RULE_EXCL table.");
		assertTrue(clnPyrGrpTestEpRuleExcl.seqId != 0, "        Data is saved in CLN_PYR_GRP_TEST_EP_RULE_EXCL table.");
		assertEquals(clnPyrTestEpRule.getOnlyAllowTestId(), test.getTestId(), "        Test Id is saved in CLN_PYR_TEST_EP_RULE table.");
	}

	private void verifyDataIsRemovedInClnPyrTestEpRule(int clnId,PayorTestEPRules payorTestEPRules) throws Exception {
		ClnPyrTestEpRule clnPyrTestEpRule = clientDao.getClientPyrEpFromClnPyrTestEpRuleByClnId(clnId, Integer.parseInt(payorTestEPRules.getPayorId()));
		ClnPyrTestEpRuleExcl clnPyrTestEpRuleExcl = clientDao.getClientPyrEpFromClnPyrTestEpRuleExclByCpTerSeqId(clnPyrTestEpRule.getSeqId(), Integer.parseInt(payorTestEPRules.getPayorId()));
		int pyrGrpExclUI = rpmDao.getPyrGrpByGrpNm(testDb, payorTestEPRules.getPayorGroupExclusions()).getPyrGrpId();
		ClnPyrGrpTestEpRuleExcl clnPyrGrpTestEpRuleExcl = clientDao.getClientPyrEpFromClnPyrGrpTestEpRuleExclByCpTerSeqId(clnPyrTestEpRule.getSeqId(), pyrGrpExclUI);

		assertTrue(clnPyrTestEpRule.getSeqId() == 0, "        Data is saved in CLN_PYR_TEST_EP_RULE table.");
		assertTrue(clnPyrTestEpRuleExcl.getSeqId() == 0, "        Data is saved in CLN_PYR_TEST_EP_RULE_EXCL table.");
		assertTrue(clnPyrGrpTestEpRuleExcl.getSeqId() == 0, "        Data is saved in CLN_PYR_GRP_TEST_EP_RULE_EXCL table.");
	}

	private void verifyPayorTestEPRulesOfCopyClientIsDisplayed(PayorTestEPRules payorTestEPRulesOfCopyCln) throws Exception {
		PayorTestEPRules actualPayorTestEPRules = getValuesToPayorTestEPRulesWithEnterValue(payorTestEPRulesOfCopyCln, LAST_OPTION);
		assertEquals(actualPayorTestEPRules, payorTestEPRulesOfCopyCln,"        Payor Test EP Rules of Copy Client are displayed in Payor Test EP Rules table");
		int total = Integer.parseInt(getTotalResultSearch(clientBillingRules.pyrTestEPRulesTblTotalRecordTxt()));
		assertTrue(total > 0,"        Payor Test EP Rules of Copy Client are displayed in Payor Test EP Rules table.");
		
	}

	private void verifyBillingRulesOfCopyClientIsDisplayed(BillingRules billingRules) throws Exception {

		clientBillingRules = new ClientBillingRules(driver, wait);

		filterRecordInBillingRules(billingRules);
		int total = Integer.parseInt(getTotalResultSearch(clientBillingRules.billingRulesTblTotalRecordTxt()));
		assertTrue(total > 0,"        Billing Rules of Copy Client are displayed in Billing Rules table.");
		BillingRules actualBillingRules = getValuesInBillingRulesTable(LAST_OPTION);
		assertEquals(actualBillingRules, billingRules,"        New record is displayed correctly in the Billing Rules table.");
	}

	private void verifyClientSearchPageIsDisplayed(String clientSearchURL) throws Throwable {
		clientSearch = new ClientSearch(driver);
		assertTrue(driver.getCurrentUrl().contains(clientSearchURL),"        Client Search Page Is Displayed.");
		assertTrue(driver.getTitle().equals(CLIENT_SEARCH_XIFIN_TITLE));
		assertTrue(clientSearch.clientSearchTitle().getText().equals(CLIENT_SEARCH));
	}

	private void verifyClientSearchResultIsDisplayedCorrectly(String npiId) throws Throwable{
		List<ClientSearchResult> actClientSearchResults = clientDao.getClientSearchResultByNPI(npiId);
		for (ClientSearchResult clientSearchResult : actClientSearchResults) {
			String stateName = stateDao.getStateByStateId(clientSearchResult.getState()).get(0).getName();
			String countryName = countryDao.getCountryByCntryId(ConvertUtil.convertStringToInt(clientSearchResult.getCountry())).getName();
			String accountTyp = clientDao.getClnAccnTypByAccnTypId(ConvertUtil.convertStringToInt(clientSearchResult.getAccountTyp())).getDescr();
			String primaryFac = facilityDao.getFacByFacId(ConvertUtil.convertStringToInt(clientSearchResult.getPrimaryFac())).getAbbrv();

			clientSearchResult.setState(stateName);
			clientSearchResult.setCountry(countryName);
			clientSearchResult.setAccountTyp(accountTyp);
			clientSearchResult.setPrimaryFac(primaryFac);
		}
		List<ClientSearchResult> expClientSearchResults = getValueOfClientSearchResultGridByNpi(npiId);
		assertEquals(actClientSearchResults, expClientSearchResults, "        The Client Search Results page is displayed correctly.");
	}
	
	private BillingRules updateDataInBillingRule(BillingRules expectedBillingRules) throws Exception {
		BillingRules expectedUpdateBillingRules = new BillingRules();
		filterRecordInBillingRules(expectedBillingRules);
		clickHiddenPageObject(clientBillingRules.billingRulesTblRow(LAST_OPTION), 0);
		clickHiddenPageObject(clientBillingRules.billingRulesTblEditBtn(),0);
		expectedUpdateBillingRules = setUpdateValuesInBillingRulesTbl(true,expectedBillingRules);
		clickHiddenPageObject(clientBillingRules.billingRulesTblPopupOkBtn(), 0);
		return expectedUpdateBillingRules;
	}

	private void checkDeletedCheckboxOfTheRecordInBillingRuleTbl(BillingRules expectedBillingRules) throws Exception {
		filterRecordInBillingRules(expectedBillingRules);
		clickHiddenPageObject(clientBillingRules.billingRulesTblRow(LAST_OPTION),0);
		clickHiddenPageObject(clientBillingRules.billingRulesTblDeletedColChk(LAST_OPTION),0);
	}

	private List<BillingRules> addMultiRecordsInBillingRulesTbl() throws Exception{
		List<BillingRules> list = new ArrayList<BillingRules>();
		clickHiddenPageObject(clientBillingRules.billingRulesTblAddBtn(),0);
		BillingRules expectedBillingRules1 = setValuesInBillingRulesTbl(true);
		list.add(expectedBillingRules1);
		clickHiddenPageObject(clientBillingRules.billingRulesTblPopupOkBtn(), 0);

		clickHiddenPageObject(clientBillingRules.billingRulesTblAddBtn(),0);
		BillingRules expectedBillingRules2 = setValuesInBillingRulesTbl(true);
		clickHiddenPageObject(clientBillingRules.billingRulesTblPopupOkBtn(), 0);
		list.add(expectedBillingRules2);
		return list;
	}

	PayorTestEPRules mapClnPyrTestEpRuleToPayorTestEPRules(ClnPyrTestEpRule clnPyrTestEpRule, PayorTestEPRules payorTestEPRulesOfCopyCln) {
		if (clnPyrTestEpRule.getPyrGrpId() != 0) {
			payorTestEPRulesOfCopyCln.setPayorGroupAbbrv(String.valueOf(clnPyrTestEpRule.getPyrGrpId()));
		}
		return payorTestEPRulesOfCopyCln;
	}
}
