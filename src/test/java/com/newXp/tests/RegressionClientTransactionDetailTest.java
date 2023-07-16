package com.newXp.tests;

import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.clnXref.ClnXref;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.client.clientInquiry.clientTransactionDetail.ClientTransactionDetail;
import com.overall.fileMaintenance.pricing.testCode.TestCode;
import com.overall.menu.MenuNavigation;
import com.overall.search.ClientSearch;
import com.overall.search.ClientSearchResults;
import com.overall.search.NpiSearch;
import com.overall.search.NpiSearchResults;
import com.overall.utils.ClientTransactionDetailUtils;
import com.overall.utils.TestCodeUtils;
import com.xifin.util.DateConversion;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.ConvertUtil;
import com.xifin.utils.RandomCharacter;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class RegressionClientTransactionDetailTest extends SeleniumBaseTest {

	private TestCodeUtils testCodeUtils;
	private MenuNavigation navigation;
	private ClientSearch clientSearch;
	private ClientSearchResults clientSearchResults;
	private NpiSearch npiSearch;
	private NpiSearchResults npiSearchResults;
	private ClientTransactionDetailUtils clientTransactionDetailUtils;
	private ClientTransactionDetail clientTransactionDetail;
	private AccessionDetail accessionDetail;
	private RandomCharacter randomCharacter;
	private TestCode testCode;
	private ConvertUtil convertUtil;
	private static final String EFFECTIVE_DATE_COLUMN = "effectiveDate";
	private static final String XREF_COLUMN = "xref";
	private static final String CLIENT_NAME_COLUMN = "clientName";
	private static final String ADDRESS_1_COLUMN = "address1";
	private static final String ADDRESS_2_COLUMN = "address2";
	private static final String ROW_NUMBER = "rn";
	private static final String FIRST_ROW = "2";
	private static final String LAST_ROW = "last()";

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

	// Load Client
	@Test(priority = 1, description = "Load Client - Load valid ClientID")
	public void testXPR_335() throws Exception {
		logger.info("===== Testing - testXPR_335 =====");          

		logger.info("*** Step 1 Actions: - Log into Xifin portal Client Transaction Detail screen with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
		
		logger.info("*** Step 2 Actions: - Load Client - Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client - Transaction Detail page title should be displayed");		
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"), "        Page Title should be 'Client - Transaction Detail'");

		logger.info("*** Step 3 Actions: - Enter a Valid Client ID");
		String clientID = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5), "        testId input field should show");
		clientTransactionDetail.checkInputClientId(clientID);

		logger.info("*** Step 3 Expected Results: - Client - Transaction Detail page is displayed with all Client's information");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        ClientID Label should be displayed.");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        ClientName Label should be displayed.");
		assertTrue(isElementPresent(clientTransactionDetail.accountTypeLabel(), 5),"        AccountType Label should be displayed.");	
		
		clientTransactionDetail.clickClientTransactionDetailResetButton();
		}

	@Test(priority = 1, description = "Load Client - Load invalid ClientID")
	public void testXPR_336() throws Exception {
		logger.info("===== Testing - testXPR_336 =====");          

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		logger.info("*** Step 2 Actions: - Click on Transaction Detail link");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 2 Expected Results: - Load Client - Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client - Transaction Detail page title should be displayed");		
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"), "        Page Title should be 'Client - Transaction Detail'");

		logger.info("*** Step 3 Actions: - Input invalid Client ID");
		String invalidClientID = String.valueOf(System.currentTimeMillis());
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5), "        testId input field should show");
		clientTransactionDetail.setClientIdInput(invalidClientID);

		logger.info("*** Step 3 Expected Results: - The error message 'Client account not on file. Could not find xxx' is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientNotFoundMessageContainer(), 5),"        Client Not Found Message Container should be displayed.");
		assertEquals(clientTransactionDetail.clientNotFoundMessageContainer().getText(), "Client account not on file. Could not find '"+ invalidClientID +"'.", "        The error message 'Client account not on file. Could not find xxx' is displayed");
	}
	// Search Client
	@Test(priority = 1, description = "Search Client - Search Client with valid data for full fields")
	public void testXPR_337() throws Exception {
		logger.info("===== Testing - testXPR_337 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
		 
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 2 Actions: - Click on Search ClientId Icon");
		assertTrue(isElementPresent(clientTransactionDetail.clientSearchIcon(), 5),"        The Client Search icon should be displayed");
		clientTransactionDetail.clickClientSearchIcon();
		String parent = switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - The Search Client page is displayed");
		clientSearch = new ClientSearch(driver);
		assertTrue(isElementPresent(clientSearch.clientSearchTitle(), 10), "        The Client Search title should be displayed");
		assertEquals(clientSearch.clientSearchTitle().getText(),"Client Search", "        The Client Search page should be displayed");

		logger.info("*** Step 3 Actions: - Input valid data for all fields in Client Info section and click on Search button");
		npiSearch = new NpiSearch(driver);
		List<ClnXref> clnXrefs = crossReferenceDao.getClnXrefForClientSearch();
		ClnXref clnXref = clnXrefs.get(RandomCharacter.getRandomIntInRange(0, clnXrefs.size()));
		Cln cln = clientDao.getClnByClnId(clnXref.getClnId());
		String ClientID = cln.getClnAbbrev();
		String ClientName = cln.getBilAcctNm();
		String Facility = facilityDao.getFacByFacId(cln.getOrderingFacId()).getAbbrv();
		String Address1 = cln.getBilAddr1();
		String Address2 = ConvertUtil.convertNull(cln.getBilAddr2());
		String City = cln.getBilCity();
		String State = cln.getBilStId();
		String Postal_Code = cln.getBilZipId();
		String Country = countryDao.getCountryByCntryId(cln.getBilCntryId()).getName();
		String NpiNember = String.valueOf(cln.getNpi());
		String Eff_date = DateConversion.dateToLgYrDtString(clnXref.getEffDt());
		String Xref_type = crossReferenceDao.getXrefTypByXrefId(clnXref.getXrefId()).getDescr();
		String Xref_member = crossReferenceDao.getXrefByXrefId(clnXref.getXrefId()).getDescr();
		
		assertTrue(isElementPresent(clientSearch.clientIDInput(), 10), "        Client Id Input field should be displayed");
		clientSearch.inputClientID(ClientID);
		assertTrue(isElementPresent(clientSearch.clientNameInput(), 10), "        Client Name Input field should be displayed");
		clientSearch.inputClientName(ClientName);
		assertTrue(isElementPresent(npiSearch.npiInput(), 10), "        Npi  Input field should be displayed");
		npiSearch.inputNPI(NpiNember);
		assertTrue(isElementPresent(clientSearch.primaryFacilityDropdown(), 10), "        Primary Facility field should be displayed");
		clientSearch.selectPrimaryFacilityDropdown(Facility);
		assertTrue(isElementPresent(clientSearch.clientSearchEffectiveDateInput(), 10), "        Effective Date  Input field should be displayed");
		clientSearch.inputClientSearchEffectiveDateByString(Eff_date);
		assertTrue(isElementPresent(clientSearch.clientSearchXRefTypeDropdown(), 10), "        Xref Type field should be displayed");
		clientSearch.selectClientSearchXRefTypeDropdown(Xref_type);
		assertTrue(isElementPresent(clientSearch.clientSearchXRefMemberDropdown(), 10), "        Xref Member field should be displayed");
		clientSearch.selectClientSearchXRefMemberDropdown(Xref_member);
		assertTrue(isElementPresent(clientSearch.clientSearchAddressInput(), 10), "        Address Input field should be displayed");
		clientSearch.inputClientSearchAddress(Address1);
		clientSearch.inputClientSearchAddress2(Address2);
		assertTrue(isElementPresent(clientSearch.clientSearchCityInput(), 10), "        City Input field should be displayed");
		clientSearch.inputClientSearchCity(City);
		assertTrue(isElementPresent(clientSearch.clientSearchStateDropdown(), 10), "        State field should be displayed");
		selectItemByVal(clientSearch.clientSearchStateDropdown(),State);
		assertTrue(isElementPresent(clientSearch.clientSearchPostalCodeInput(), 10), "        Postal field should be displayed");
		clientSearch.inputClientSearchPostalCode(Postal_Code);
		assertTrue(isElementPresent(clientSearch.clientSearchCountryDropdown(), 10), "        Country Input field should be displayed");
		clientSearch.selectClientSearchCountryDropdown(Country);

		assertTrue(isElementPresent(clientSearch.clientSearchSearchButton(), 10), "        Client Search button should be displayed");
		clientSearch.clickClientSearchSearchButton();

		logger.info("*** Step 3 Expected Results: - Client Info is match with search condition will be displayed");
		clientSearchResults = new ClientSearchResults(driver);
		assertEquals(clientSearchResults.clientSearchResultsTblClientID(2, 2).getText(),ClientID, "        ClientId  must be matched with search condition");
		assertEquals(clientSearchResults.clientSearchResultsCellByRowAndColumnName(FIRST_ROW, CLIENT_NAME_COLUMN).getText(), ClientName, "        Client Name  must be matched with search condition");
		assertEquals(clientSearchResults.clientSearchResultsCellByRowAndColumnName(FIRST_ROW, EFFECTIVE_DATE_COLUMN).getText(), Eff_date, "        Effective Date  must be matched with search condition");
		assertEquals(clientSearchResults.clientSearchResultsCellByRowAndColumnName(FIRST_ROW, XREF_COLUMN).getText(), (Xref_type + " - " + Xref_member).toUpperCase(), "        Xref  must be matched with search condition");
		assertTrue(clientSearchResults.address1InClientSearchResults(FIRST_ROW).getText().trim().contains(Address1));
		assertTrue(clientSearchResults.address2InClientSearchResults(FIRST_ROW).getText().trim().contains(Address2));
		
		clientSearchResults.clickClientSearchResultCloseButton();
		switchToParentWin(parent);
	}

	@Test(priority = 1, description = "Search Client - Search Client with valid data for Cross Section")
	public void testXPR_339() throws Exception {
		logger.info("===== Testing - testXPR_339 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
		 
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 2 Actions: - Click on Search ClientId Icon");
		assertTrue(isElementPresent(clientTransactionDetail.clientSearchIcon(), 5),"        The Client Search icon should be displayed");
		clientTransactionDetail.clickClientSearchIcon();
		String parent = switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - The Search Client page is displayed");
		clientSearch = new ClientSearch(driver);
		assertTrue(isElementPresent(clientSearch.clientSearchTitle(), 10), "        The Client Search title should be displayed");
		assertEquals(clientSearch.clientSearchTitle().getText(),"Client Search", "        The Client Search page should be displayed");

		logger.info("*** Step 3 Actions: - Input valid data for all fields in Client Info section and click on Search button");
		npiSearch = new NpiSearch(driver);
		List<String> procedureCodeTemp = daoManagerXifinRpm.getAllClientInfoByClientId(testDb);
		String Eff_date = procedureCodeTemp.get(4);
		String Xref_type = procedureCodeTemp.get(5);
		String Xref_member = procedureCodeTemp.get(11);

		assertTrue(isElementPresent(clientSearch.clientSearchEffectiveDateInput(), 10), "        Effective Date  Input field should be displayed");
		clientSearch.inputClientSearchEffectiveDateByString(Eff_date);
		assertTrue(isElementPresent(clientSearch.clientSearchXRefTypeDropdown(), 10), "        Xref Type field should be displayed");
		clientSearch.selectClientSearchXRefTypeDropdown(Xref_type);
		assertTrue(isElementPresent(clientSearch.clientSearchXRefMemberDropdown(), 10), "        Xref Member field should be displayed");
		clientSearch.selectClientSearchXRefMemberDropdown(Xref_member);
		assertTrue(isElementPresent(clientSearch.clientSearchSearchButton(), 10), "        Client Search button should be displayed");
		clientSearch.clickClientSearchSearchButton();

		logger.info("*** Step 3 Expected Results: - Client Info is match with search condition will be displayed");
		clientSearchResults = new ClientSearchResults(driver);
		assertEquals(clientSearchResults.clientSearchResultsCellByRowAndColumnName(FIRST_ROW, EFFECTIVE_DATE_COLUMN).getText(),Eff_date, "        Effective Date  must be matched with search condition");
		assertEquals(clientSearchResults.clientSearchResultsCellByRowAndColumnName(FIRST_ROW, XREF_COLUMN).getText(),(Xref_type + " - " + Xref_member).toUpperCase(), "        Xref  must be matched with search condition");

		clientSearchResults.clickClientSearchResultCloseButton();
		switchToParentWin(parent);
	}

	@Test(priority = 1, description = "Search Client - Search Client with valid data for Address info section")
	public void testXPR_340() throws Exception {
		logger.info("===== Testing - testXPR_340 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
	 
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 2 Actions: - Click on Search ClientId Icon");
		assertTrue(isElementPresent(clientTransactionDetail.clientSearchIcon(), 5),"        The Client Search icon should be displayed");
		clientTransactionDetail.clickClientSearchIcon();
		String parent = switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - The Search Client page is displayed");
		clientSearch = new ClientSearch(driver);
		assertTrue(isElementPresent(clientSearch.clientSearchTitle(), 10), "        The Client Search title should be displayed");
		assertEquals(clientSearch.clientSearchTitle().getText(),"Client Search", "        The Client Search page should be displayed");

		logger.info("*** Step 3 Actions: - Input valid data for all fields in Client Info section and click on Search button");
		npiSearch = new NpiSearch(driver);
		List<Cln> clnSearchAbbrvs = clientDao.getClnForClientSearch();
		Cln clnSearchAbbrv = clnSearchAbbrvs.get(RandomCharacter.getRandomIntInRange(0, clnSearchAbbrvs.size()));
		String Address1 = clnSearchAbbrv.getBilAddr1();
		String City = clnSearchAbbrv.getBilCity();
		String State = clnSearchAbbrv.getBilStId();
		String Postal_Code = clnSearchAbbrv.getBilZipId();
		String Country = countryDao.getCountryByCntryId(clnSearchAbbrv.getBilCntryId()).getName();
		String Address2 = clnSearchAbbrv.getBilAddr2();

		clientSearch.inputClientSearchAddress(Address1);
		clientSearch.inputClientSearchCity(City);
		selectItemByVal(clientSearch.clientSearchStateDropdown(),State);
		clientSearch.inputClientSearchPostalCode(Postal_Code);
		clientSearch.selectClientSearchCountryDropdown(Country);
		assertTrue(isElementPresent(clientSearch.clientSearchSearchButton(), 10), "        Client Search button should be displayed");
		clientSearch.clickClientSearchSearchButton();

		logger.info("*** Step 3 Expected Results: - Client Info is match with search condition will be displayed");
		clientSearchResults = new ClientSearchResults(driver);	
		assertEquals(clientSearchResults.address1InClientSearchResults("2").getText().trim(), ConvertUtil.convertNull(Address1));
		assertEquals(clientSearchResults.address2InClientSearchResults("2").getText().trim(), ConvertUtil.convertNull(Address2));
		clientSearchResults.clickClientSearchResultCloseButton();
		switchToParentWin(parent);
	}

	@Test(priority = 1, description = "Search Client - Search all client in the system")
	public void testXPR_341() throws Exception {
		logger.info("===== Testing - testXPR_341 =====");
		clientTransactionDetailUtils =new ClientTransactionDetailUtils(driver);
		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
	 
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 2 Actions: - Click on Search ClientId Icon");
		assertTrue(isElementPresent(clientTransactionDetail.clientSearchIcon(), 5),"        The Client Search icon should be displayed");
		clientTransactionDetail.clickClientSearchIcon();
		String parent = switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - The Search Client page is displayed");
		clientSearch = new ClientSearch(driver);
		assertTrue(isElementPresent(clientSearch.clientSearchTitle(), 10), "        The Client Search title should be displayed");
		assertEquals(clientSearch.clientSearchTitle().getText(),"Client Search", "        The Client Search page should be displayed");

		logger.info("*** Step 3 Actions: - Input * in ClientID field and click on Search button");		
		int totalClientFromDB = clientDao.getAllClientsGrpByMaxEffDt().size();
		assertTrue(isElementPresent(clientSearch.clientIDInput(), 10), "        Client Id Input field should be displayed");
		clientSearch.inputClientID("*");
		assertTrue(isElementPresent(clientSearch.clientSearchSearchButton(), 5),"        The Client Search button should be displayed");
		clientSearch.clickClientSearchSearchButton();

		logger.info("*** Step 3 Expected Results: - Client Info is match with search condition will be displayed");
		clientSearchResults = new ClientSearchResults(driver);
		assertTrue(isElementPresent(clientSearchResults.clientSearchResultTblLastestIcon(), 5),"        The Last icon should be displayed");
		clientSearchResults.clickClientSearchResultTblLastestIcon(this);
		int totalClientFromUI = Integer.valueOf(clientSearchResults.clientSearchResultsCellByRowAndColumnName(LAST_ROW, ROW_NUMBER).getText());
		assertEquals(totalClientFromUI, totalClientFromDB, "        Total number of Clients in Search Results should be " + totalClientFromDB);

		clientSearchResults.clickClientSearchResultCloseButton();
		switchToParentWin(parent);
	}

	@Test(priority = 1, description = "Search Client - Search invalid client")
	public void testXPR_342() throws Exception {
		logger.info("===== Testing - testXPR_342 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
		 
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 2 Actions: - Click on Search ClientId Icon");
		assertTrue(isElementPresent(clientTransactionDetail.clientSearchIcon(), 5),"        The Client Search icon should be displayed");
		clientTransactionDetail.clickClientSearchIcon();
		String parent = switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - The Search Client page is displayed");
		clientSearch = new ClientSearch(driver);
		assertTrue(isElementPresent(clientSearch.clientSearchTitle(), 10), "        The Client Search title should be displayed");
		assertEquals(clientSearch.clientSearchTitle().getText(),"Client Search", "        The Client Search page should be displayed");

		logger.info("*** Step 3 Actions: - Input invalid ClientID field and click on Search button");
		assertTrue(isElementPresent(clientSearch.clientIDInput(), 10), "        Client Id Input field should be displayed");		
		String invalidClientID = String.valueOf(System.currentTimeMillis());
		clientSearch.inputClientID(invalidClientID);	
		assertTrue(isElementPresent(clientSearch.clientSearchSearchButton(), 5),"        The Client Search button should be displayed");
		clientSearch.clickClientSearchSearchButton();

		logger.info("*** Step 3 Expected Results: - The search result is empty");
		clientSearchResults = new ClientSearchResults(driver);		
		assertEquals(clientSearchResults.clientSearchResultTblViewResultsLable().getText(), "No Result", "        Total Client  must be matched with search condition" );

		clientSearchResults.clickClientSearchResultCloseButton();
		switchToParentWin(parent);
	}

	@Test(priority = 1, description = "Search Client - Verify Keep Search Open Checkbox in search result page")
	public void testXPR_343() throws Exception {
		logger.info("===== Testing - testXPR_343 =====");
		clientTransactionDetailUtils=new ClientTransactionDetailUtils(driver);

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
		 
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 2 Actions: - Click on Search ClientId Icon");
		assertTrue(isElementPresent(clientTransactionDetail.clientSearchIcon(), 5),"        The Client Search icon should be displayed");
		clientTransactionDetail.clickClientSearchIcon();
		String winHandler = driver.getWindowHandle();
		clientTransactionDetailUtils.switchToPopupWin();
		

		logger.info("*** Step 2 Expected Results: - The Search Client page is displayed");
		clientSearch = new ClientSearch(driver);
		assertTrue(isElementPresent(clientSearch.clientSearchTitle(), 10), "        The Client Search title should be displayed");
		assertEquals(clientSearch.clientSearchTitle().getText(),"Client Search", "        The Client Search page should be displayed");

		logger.info("*** Step 3 Actions: - Enter a Client Abbrev in ClientID field and click on Search button");
		int totalClientFromDB = clientDao.getTotalClient();
		assertTrue(isElementPresent(clientSearch.clientIDInput(), 10), "        Client Id Input field should be displayed");
		List<Cln> clnSearchAbbrvs = clientDao.getClnForClientSearch();
		String clnSearchAbbrv = clientDao.getClnForClientSearch().get(RandomCharacter.getRandomIntInRange(0, clnSearchAbbrvs.size())).getClnAbbrev();
		clientSearch.inputClientID(clnSearchAbbrv);
		assertTrue(isElementPresent(clientSearch.clientSearchSearchButton(), 5),"        The Client Search button should be displayed");
		clientSearch.clickClientSearchSearchButton();

		logger.info("*** Step 3 Expected Results: - Client Info is match with search condition will be displayed");
		clientSearchResults = new ClientSearchResults(driver);
		driver.manage().window().maximize();
		assertTrue(isElementPresent(clientSearchResults.clientSearchResultTblLastestIcon(), 5),"        The Last icon should be displayed");
		clientSearchResults.clickClientSearchResultTblLastestIcon(this);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		String totalClientFromWeb1 = clientSearchResults.clientSearchResultTblViewResultsLable().getText().split(" ")[3].trim();
		assertEquals(totalClientFromWeb1, "1", "        Total number of Clients in Search Results should be " + totalClientFromDB);
		
		logger.info("*** Step 4 Actions: - Check on Keep Search open checkbox in Search Result page and click any ClientID link");
		assertTrue(isElementPresent(clientSearchResults.clientSearchResultKeepSearchOpenCheckbox(), 5),"        Keep Search checkbox should be displayed");
		clientSearchResults.clickClientSearchResultKeepSearchOpenCheckbox();	
		clientSearchResults.clickClientSearchResultTblFirstIcon(this);
		clientSearchResults.clickClientSearchTblClientID(2,2);
        switchToWin(winHandler);

		logger.info("*** Step 4 Expected Results: - The ClientID is auto-populate in Load Client Transaction Detail page - Search result page is kept");			
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(clnSearchAbbrv), 10), "The Client Id Label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.titlePage(), 10),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.titlePage().getText(), "Transaction Detail", "        The Client - Transaction Detail title should be displayed" );
		
		clientTransactionDetail.clickClientTransactionDetailResetButton();		
		switchToPopupWin();
		clientSearchResults.clickClientSearchResultCloseButton();
	}

	@Test(priority = 1, description = "Search Client - Verify footer button in search result page")
	public void testXPR_344() throws Exception {
		logger.info("===== Testing - testXPR_344 =====");
		clientTransactionDetailUtils=new ClientTransactionDetailUtils(driver);

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
		 
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 2 Actions: - Click on Search ClientId Icon");
		assertTrue(isElementPresent(clientTransactionDetail.clientSearchIcon(), 5),"        The Client Search icon should be displayed");
		clientTransactionDetail.clickClientSearchIcon();
		String parent = switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - The Search Client page is displayed");
		clientSearch = new ClientSearch(driver);
		assertTrue(isElementPresent(clientSearch.clientSearchTitle(), 10), "        The Client Search title should be displayed");
		assertEquals(clientSearch.clientSearchTitle().getText(),"Client Search", "        The Client Search page should be displayed");

		logger.info("*** Step 3 Actions: - Input * in ClientID field and click on Search button");
		int totalClientFromDB = clientDao.getAllClientsGrpByMaxEffDt().size();
		assertTrue(isElementPresent(clientSearch.clientIDInput(), 10), "        Client Id Input field should be displayed");
		clientSearch.inputClientID("*");	
		assertTrue(isElementPresent(clientSearch.clientSearchSearchButton(), 5),"        The Client Search button should be displayed");
		clientSearch.clickClientSearchSearchButton();

		logger.info("*** Step 3 Expected Results: - Client Info is match with search condition will be displayed");
		clientSearchResults = new ClientSearchResults(driver);
		assertTrue(isElementPresent(clientSearchResults.clientSearchResultTblLastestIcon(), 5),"        The Last icon should be displayed");
		clientSearchResults.clickClientSearchResultTblLastestIcon(this);
		int totalClientFromUI = Integer.valueOf(clientSearchResults.clientSearchResultsCellByRowAndColumnName(LAST_ROW, ROW_NUMBER).getText());
		assertEquals(totalClientFromUI, totalClientFromDB, "        Total number of Clients in Search Results should be " + totalClientFromDB);

		logger.info("*** Step 4 Actions: - Click to Next page icon, Previous page icon, Latest page icon, First page icon");
		assertTrue(isElementPresent(clientSearchResults.clientSearchResultTblFirstIcon(), 5),"        The First icon should be displayed");
		clientSearchResults.clickClientSearchResultTblFirstIcon(this);		
		assertTrue(isElementPresent(clientSearchResults.clientSearchResultTblNextIcon(), 5),"        The Next page icon should be displayed");
		clientSearchResults.clickClientSearchResultTblNextIcon(this);
		assertEquals(clientSearchResults.clientSearchResultTblNumberOfPageInput().getAttribute("value"),"2","        The page number should be increased by 1" );

		assertTrue(isElementPresent(clientSearchResults.clientSearchResultTblNumberOfPageLabel(), 5),"        The Previous page icon should be displayed");
		clientSearchResults.clickClientSearchResultTblPreviousIcon(this);
		assertEquals(clientSearchResults.clientSearchResultTblNumberOfPageInput().getAttribute("value"),"1", "        The page number should be decreased by 1" );

		assertTrue(isElementPresent(clientSearchResults.clientSearchResultTblLastestIcon(), 5),"        The Latest page icon should be displayed");
		clientSearchResults.clickClientSearchResultTblLastestIcon(this);
		assertEquals(Integer.parseInt(clientSearchResults.clientSearchResultTblNumberOfPageLabel().getText().replaceAll(",", "").trim()),Integer.parseInt(clientSearchResults.clientSearchResultTblNumberOfPageInput().getAttribute("value").replaceAll(",", "").trim()), "        The page number input should be equal the displayed page number" );
		
		assertTrue(isElementPresent(clientSearchResults.clientSearchResultTblFirstIcon(), 5),"        The First page icon should be displayed");
		clientSearchResults.clickClientSearchResultTblFirstIcon(this);
		assertEquals(clientSearchResults.clientSearchResultTblNumberOfPageInput().getAttribute("value"),"1", "        The page number input should be equal to 1" );

		logger.info("*** Step 4 Expected Results: - System will be change when user click on Next page, Previous page, Last page, First page icon");
		clientSearchResults.clickClientSearchResultCloseButton();
		switchToParentWin(parent);
	}

	@Test(priority = 1, description = "Search Client - Verify New Search button")
	public void testXPR_345() throws Exception {
		logger.info("===== Testing - testXPR_345 =====");
		clientTransactionDetailUtils=new ClientTransactionDetailUtils(driver);

		logger.info("*** Step 1 Actions: - Login to Xifin portal with valid SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
		 
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 2 Actions: - Click on Search ClientId Icon");
		assertTrue(isElementPresent(clientTransactionDetail.clientSearchIcon(), 5),"        The Client Search icon should be displayed");
		clientTransactionDetail.clickClientSearchIcon();
		String parent = switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - The Search Client page is displayed");
		clientSearch = new ClientSearch(driver);
		assertTrue(isElementPresent(clientSearch.clientSearchTitle(), 10), "        The Client Search title should be displayed");
		assertEquals(clientSearch.clientSearchTitle().getText(),"Client Search", "        The Client Search page should be displayed");

		logger.info("*** Step 3 Actions: - Enter '*' in ClientID field and click on Search button");
		int totalClientFromDB = clientDao.getAllClientsGrpByMaxEffDt().size();
		assertTrue(isElementPresent(clientSearch.clientIDInput(), 10), "        Client Id Input field should be displayed");
		clientSearch.inputClientID("*");
		assertTrue(isElementPresent(clientSearch.clientSearchSearchButton(), 5),"        The Client Search button should be displayed");
		clientSearch.clickClientSearchSearchButton();

		logger.info("*** Step 3 Expected Results: - Client Info is match with search condition will be displayed");
		clientSearchResults = new ClientSearchResults(driver);
		assertTrue(isElementPresent(clientSearchResults.clientSearchResultTblLastestIcon(), 5),"        The Last icon should be displayed");
		clientSearchResults.clickClientSearchResultTblLastestIcon(this);
		int totalClientFromUI = Integer.valueOf(clientSearchResults.clientSearchResultsCellByRowAndColumnName(LAST_ROW, ROW_NUMBER).getText());
		assertEquals(totalClientFromUI, totalClientFromDB, "        Total number of Clients in Search Results should be " + totalClientFromDB);

		logger.info("*** Step 4 Actions: - Click New search button");
		assertTrue(isElementPresent(clientSearchResults.clientSearchResultNewSearchButton(), 10), "        New Search button should be displayed");
		clientSearchResults.clickClientSearchResultNewSearchButton();

		logger.info("*** Step 4 Expected Results: - System will take user back to the Search Client page with all fields are cleared");		
		assertEquals(clientSearch.clientIDInput().getAttribute("value"), "","        Client Id Input field should be empty");
		assertEquals(clientSearch.clientNameInput().getAttribute("value"), "","        Client Name Input field should be empty");
		assertEquals(clientSearch.primaryFacilityDropdown().getAttribute("value"), "","        Primary Facility field should be empty");
		assertEquals(clientSearch.clientSearchEffectiveDateInput().getAttribute("value"), "","        Effective Date field should be empty");
		assertEquals(clientSearch.clientSearchXRefTypeDropdown().getAttribute("value"), " ","        Xref type field should be empty");
		assertEquals(clientSearch.clientSearchXRefMemberDropdown().getAttribute("value"), "","        Xref member Input field should be empty");
		assertEquals(clientSearch.clientSearchAddressInput().getAttribute("value"), "","        Address Input field should be empty");
		assertEquals(clientSearch.clientSearchCityInput().getAttribute("value"), "","        City Input field should be empty");
		assertEquals(clientSearch.clientSearchStateDropdown().getAttribute("value"), "","        State field should be empty");
		assertEquals(clientSearch.clientSearchPostalCodeInput().getAttribute("value"), "","       Postal Code Input field should be empty");
		assertEquals(clientSearch.clientSearchCountryDropdown().getAttribute("value"), "","        Country field should be empty");

		switchToParentWin(parent);			
	}

	@Test(priority = 1, description = "Search Client - Verify Reset button")
	public void testXPR_346() throws Exception {
		logger.info("===== Testing - testXPR_346 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
		 
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");

		logger.info("*** Step 2 Actions: - Click on Search ClientId Icon");
		assertTrue(isElementPresent(clientTransactionDetail.clientSearchIcon(), 5),"        The Client Search icon should be displayed");
		clientTransactionDetail.clickClientSearchIcon();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - The Search Client page is displayed");
		clientSearch = new ClientSearch(driver);
		assertTrue(isElementPresent(clientSearch.clientSearchTitle(), 10), "        The Client Search title should be displayed");
		assertEquals(clientSearch.clientSearchTitle().getText(),"Client Search", "        Client Search page should be displayed");

		logger.info("*** Step 3 Actions: - Input data for all field in Search Client page and click on Reset button");
		List<String> procedureCodeTemp = daoManagerXifinRpm.getAllClientInfoByClientId(testDb);
		String clientID = procedureCodeTemp.get(0);
		String clientName = procedureCodeTemp.get(1);
		String primaryFacility = procedureCodeTemp.get(2);
		String npiNumber = procedureCodeTemp.get(3);
		String effectiveDate = procedureCodeTemp.get(4);
		String xref_type = procedureCodeTemp.get(5);
		String bil_addr1 = procedureCodeTemp.get(6);
		String city = procedureCodeTemp.get(7);
		String state = procedureCodeTemp.get(8);
		String postalCode = procedureCodeTemp.get(9);
		String country = procedureCodeTemp.get(10);
		String xref_member = procedureCodeTemp.get(11);

		clientSearch = new ClientSearch(driver);
		npiSearch = new NpiSearch(driver);

		assertTrue(isElementPresent(clientSearch.clientIDInput(), 5),"        The ClientId input field should be displayed");
		clientSearch.inputClientID(clientID);
		assertTrue(isElementPresent(clientSearch.clientNameInput(), 5),"        The Client Name input field should be displayed");
		clientSearch.inputClientName(clientName);
		assertTrue(isElementPresent(npiSearch.npiInput(), 5),"        NPI input field should be shown");
		npiSearch.inputNPI(npiNumber);
		assertTrue(isElementPresent(clientSearch.primaryFacilityDropdown(), 5),"        Primary Facility dropdown should be shown");
		selectItemByVal(clientSearch.primaryFacilityDropdown(),primaryFacility);
		assertTrue(isElementPresent(clientSearch.clientSearchEffectiveDateInput(), 5),"        Effective Date input field should be shown");
		clientSearch.inputClientSearchEffectiveDateByString(effectiveDate);
		assertTrue(isElementPresent(clientSearch.clientSearchXRefTypeDropdown(), 5),"        Xref Type dropdown should be shown");
		selectItemByVal(clientSearch.clientSearchXRefTypeDropdown(),xref_type);
		assertTrue(isElementPresent(clientSearch.clientSearchXRefMemberDropdown(), 5),"         Xref Member dropdown should be shown");
		selectItemByVal(clientSearch.clientSearchXRefMemberDropdown(),xref_member);
		assertTrue(isElementPresent(clientSearch.clientSearchAddressInput(), 5),"        Address input field should be shown");
		clientSearch.inputClientSearchAddress(bil_addr1);
		assertTrue(isElementPresent(clientSearch.clientSearchCityInput(),5),"        City input field should be shown");
		clientSearch.inputClientSearchCity(city);
		assertTrue(isElementPresent(clientSearch.clientSearchStateDropdown(),5),"        State dropdown should be shown");
		selectItemByVal(clientSearch.clientSearchStateDropdown(),state);
		assertTrue(isElementPresent(clientSearch.clientSearchPostalCodeInput(), 5),"        Postal Code input field should be shown");
		clientSearch.inputClientSearchPostalCode(postalCode);
		assertTrue(isElementPresent(clientSearch.clientSearchCountryDropdown(), 5),"        Country dropdown should be shown");
		selectItem(clientSearch.clientSearchCountryDropdown(),country);

		clientSearch.clickClientSearchResetButton();

		logger.info("*** Step 3 Expected Results: - All data in fields will be clear");
		testCodeUtils = new TestCodeUtils(driver);		
		assertEquals(clientSearch.clientIDInput().getText(), "", "        Data in ClientID field should be reset to empty");
		assertEquals(clientSearch.clientNameInput().getText(), "", "        Data in Client Name field should be reset to empty");
		assertEquals(npiSearch.npiInput().getText(), "", "        Data in NPI field should be reset to empty");
		assertEquals(clientSearch.clientSearchEffectiveDateInput().getText(), "", "        Data in Effective Date field should be reset to empty");
		assertEquals(clientSearch.clientSearchAddressInput().getText(), "", "        Data in Address field should be reset to empty");
		assertEquals(clientSearch.clientSearchCityInput().getText(), "", "        Data in City field should be reset to empty");
		assertEquals(clientSearch.clientSearchPostalCodeInput().getText(), "", "        Data in Postal Code field should be reset to empty");
		assertEquals(testCodeUtils.getTextSelectedInDropdown(clientSearch.primaryFacilityDropdown()).trim(),"", "        Data in Primary Facility field should be reset to empty");
		assertEquals(testCodeUtils.getTextSelectedInDropdown(clientSearch.clientSearchXRefTypeDropdown()).trim(),"", "        Data in Xref Type field should be reset to empty");
		assertTrue(clientSearch.clientSearchXRefMemberDropdown().getText().isEmpty(),  "        Data in Xref Member field should be reset to empty");
		assertEquals(testCodeUtils.getTextSelectedInDropdown(clientSearch.clientSearchStateDropdown()).trim(),"",  "        Data in State field should be reset to empty");
		assertEquals(testCodeUtils.getTextSelectedInDropdown(clientSearch.clientSearchCountryDropdown()).trim(),"", "        Data in Country field should be reset to empty");

		clientSearch.clickClientSearchCloseButton();
	}

	@Test(priority = 1, description = "Search Client - Search Client with empty data")
	public void testXPR_347() throws Exception {
		logger.info("===== Testing - testXPR_347 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
		 
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");

		logger.info("*** Step 2 Actions: - Click on Search ClientId Icon");
		assertTrue(isElementPresent(clientTransactionDetail.clientSearchIcon(), 5),"        The Client Search icon should be displayed");
		clientTransactionDetail.clickClientSearchIcon();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - The Search Client page is displayed");
		clientSearch = new ClientSearch(driver);
		assertTrue(isElementPresent(clientSearch.clientSearchTitle(), 10), "        The Client Search title should be displayed");
		assertEquals(clientSearch.clientSearchTitle().getText(),"Client Search", "        Client Search page should be displayed");

		logger.info("*** Step 3 Actions: - Click on Search button without input any data");
		assertTrue(isElementPresent(clientSearch.clientSearchSearchButton(), 5),"        The Client Search button should be displayed");
		clientSearch.clickClientSearchSearchButton();

		logger.info("*** Step 3 Expected Results: - The error message 'At least one field must be used to initiate a search' is displayed");
		assertTrue(isElementPresent(clientSearch.errorMessageClientSearchContent(), 5),"        Error message must be display");
		assertTrue(clientSearch.errorMessageClientSearchContent().getText().equalsIgnoreCase("At least one field must be used to initiate a search."),"        Error message must be display correct");

		clientSearch.clickErrorMessageClientSearchCloseIcon();
		clientSearch.clickClientSearchCloseButton();
	}	

	@Test(priority = 1, description = "Search Client - Search NPI - Search NPI with Entity is Facility")
	public void testXPR_349() throws Exception {
		logger.info("===== Testing - testXPR_349 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
		 
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");

		logger.info("*** Step 2 Actions: - Click on Search ClientId icon");
		clientSearch = new ClientSearch(driver);
		assertTrue(isElementPresent(clientSearch.clientSearchIconBtn(), 5),"        The Client Search icon should be displayed");
		clientSearch.clickClientSearchIconBtn();
		String parent = switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Search Client page is displayed");
		assertTrue(isElementPresent(clientSearch.clientSearchTitle(), 5),"        The Client Search page title should be displayed");
		assertEquals(clientSearch.clientSearchTitle().getText(), "Client Search","        Client Search page should displayed");

		logger.info("*** Step 3 Actions: - Click on Search NPI icon");
		npiSearch = new NpiSearch(driver);
		assertTrue(isElementPresent(npiSearch.npiSearchIconBtn(), 5),"        The NPI Search icon should be displayed");
		npiSearch.clickNPISearchIconBtn();
		String parent1 = switchToPopupWin();

		logger.info("*** Step 3 Expected Results: - The Search NPI page is displayed");
		assertTrue(isElementPresent(npiSearch.npiTitle(), 5),"        The NPI Search page title should be displayed");
		assertEquals(npiSearch.npiTitle().getText(), "NPI Search","        NPI Search page should displayed");

		logger.info("*** Step 4 Actions: - Select NPI Entity is Facility");
		selectItem(npiSearch.npiEntityDropdown(), "Facility");
		assertTrue(isElementPresent(npiSearch.npiSearchButton(), 5),"        The NPI Search button should be displayed");
		npiSearch.clickNPISearchButton();

		logger.info("*** Step 4 Expected Results: - All NPI have type is Organization will be display");
		npiSearchResults = new NpiSearchResults(driver);	
		assertTrue(getColumnValue(npiSearchResults.npiTable(), "Organization"), "        All NPI have type is Organization will be display");

		npiSearchResults.clickNPISearchResultsCloseButton();
		switchToParentWin(parent1);
		clientSearch.clickClientSearchCloseButton();
		switchToParentWin(parent);
	}

	@Test(priority = 1, description = "Search Client - Search NPI - Search NPI with Entity is Physician")
	public void testXPR_350() throws Exception {
		logger.info("===== Testing - testXPR_350 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
		 
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");

		logger.info("*** Step 2 Actions: - Click on Search ClientId icon");
		clientSearch = new ClientSearch(driver);
		assertTrue(isElementPresent(clientSearch.clientSearchIconBtn(), 5),"        The Client Search icon should be displayed");
		clientSearch.clickClientSearchIconBtn();
		String parent = switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Search Client page is displayed");
		assertTrue(isElementPresent(clientSearch.clientSearchTitle(), 5),"        The Client Search page title should be displayed");
		assertEquals(clientSearch.clientSearchTitle().getText(), "Client Search","        Client Search page should displayed");

		logger.info("*** Step 3 Actions: - Click on Search NPI icon");
		npiSearch = new NpiSearch(driver);
		assertTrue(isElementPresent(npiSearch.npiSearchIconBtn(), 5),"        The NPI Search icon should be displayed");
		npiSearch.clickNPISearchIconBtn();
		String parent1 = switchToPopupWin();

		logger.info("*** Step 3 Expected Results: - The Search NPI page is displayed");
		assertTrue(isElementPresent(npiSearch.npiTitle(), 5),"        The NPI Search page title should be displayed");
		assertEquals(npiSearch.npiTitle().getText(), "NPI Search","        NPI Search page should displayed");

		logger.info("*** Step 4 Actions: - Select NPI Entity is Physician and click on Search button");
		selectItem(npiSearch.npiEntityDropdown(), "Physician");
		assertTrue(isElementPresent(npiSearch.firstNameInput(),5),"        The NPI Search page First Name should be displayed");
		assertTrue(isElementPresent(npiSearch.lastNameInput(),5),"        The NPI Search page Last Name should be displayed");
		assertTrue(isElementPresent(npiSearch.npiSearchButton(), 5),"        The NPI Search button should be displayed");
		npiSearch.clickNPISearchButton();

		logger.info("*** Step 4 Expected Results: - Field name will be break into two fields: First Name and Last Name and all NPIs have type Individual will be displayed");
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		npiSearchResults = new NpiSearchResults(driver);		
		assertTrue(getColumnValue(npiSearchResults.npiTable(), "Individual"), "        All NPI have type is Individual will be display");

		npiSearchResults.clickNPISearchResultsCloseButton();
		switchToParentWin(parent1);
		clientSearch.clickClientSearchCloseButton();
		switchToParentWin(parent);
	}

	@Test(priority = 1, description = "Search Client - Search NPI - Search NPI with invalid data")
	public void testXPR_351() throws Exception {
		logger.info("===== Testing - testXPR_351 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");

		logger.info("*** Step 2 Actions: - Click on Search ClientId icon");
		clientSearch = new ClientSearch(driver);
		assertTrue(isElementPresent(clientSearch.clientSearchIconBtn(), 5),"        The Client Search icon should be displayed");
		clientSearch.clickClientSearchIconBtn();
		String parent = switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Search Client page is displayed");
		assertTrue(isElementPresent(clientSearch.clientSearchTitle(), 5),"        The Client Search page title should be displayed");
		assertEquals(clientSearch.clientSearchTitle().getText(), "Client Search","        Client Search page should displayed");

		logger.info("*** Step 3 Actions: - Click on Search NPI icon");
		npiSearch = new NpiSearch(driver);
		assertTrue(isElementPresent(npiSearch.npiSearchIconBtn(), 5),"        The NPI Search icon should be displayed");
		npiSearch.clickNPISearchIconBtn();
		String parent1 = switchToPopupWin();

		logger.info("*** Step 3 Expected Results: - The Search NPI page is displayed");
		assertTrue(isElementPresent(npiSearch.npiTitle(), 5),"        The NPI Search page title should be displayed");
		assertEquals(npiSearch.npiTitle().getText(), "NPI Search","        NPI Search page should displayed");

		logger.info("*** Step 4 Actions: - Input invalid NPI and click on Search button");
		randomCharacter = new RandomCharacter(driver);
		String NPI = randomCharacter.getRandomNumericString(8);
		assertTrue(isElementPresent(npiSearch.npiInput(), 5),"        NPI input field should be shown");
		npiSearch.inputNPI(NPI);
		assertTrue(isElementPresent(npiSearch.npiSearchButton(), 5),"        The NPI Search button should be displayed");
		npiSearch.clickNPISearchButton();

		logger.info("*** Step 4 Expected Results: - The Search Result is empty");
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		npiSearchResults = new NpiSearchResults(driver);	
		assertTrue(isElementPresent(npiSearchResults.npiSearchResultsTblViewResultsLable(), 5), "        Pager in result table must be displayed");
		assertTrue(npiSearchResults.npiSearchResultsTblViewResultsLable().getText().trim().contains("No Result"),"        The Search Result is empty");

		npiSearchResults.clickNPISearchResultsCloseButton();
		switchToParentWin(parent1);
		clientSearch.clickClientSearchCloseButton();
		switchToParentWin(parent);
	}

	@Test(priority = 1, description = "Search Client - Search NPI - Verify Reset button")
	public void testXPR_352() throws Exception {
		logger.info("===== Testing - testXPR_352 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
		 
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");

		logger.info("*** Step 2 Actions: - Click on Search ClientId icon");
		clientSearch = new ClientSearch(driver);
		assertTrue(isElementPresent(clientSearch.clientSearchIconBtn(), 5),"        The Client Search icon should be displayed");
		clientSearch.clickClientSearchIconBtn();
		String parent = switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Search Client page is displayed");
		assertTrue(isElementPresent(clientSearch.clientSearchTitle(), 5),"        The Client Search page title should be displayed");
		assertEquals(clientSearch.clientSearchTitle().getText(), "Client Search","        Client Search page should displayed");

		logger.info("*** Step 3 Actions: - Click on Search NPI icon");
		npiSearch = new NpiSearch(driver);
		assertTrue(isElementPresent(npiSearch.npiSearchIconBtn(), 5),"        The NPI Search icon should be displayed");
		npiSearch.clickNPISearchIconBtn();
		String parent1 = switchToPopupWin();

		logger.info("*** Step 3 Expected Results: - The Search NPI page is displayed");
		assertTrue(isElementPresent(npiSearch.npiTitle(), 5),"        The NPI Search page title should be displayed");
		assertEquals(npiSearch.npiTitle().getText(), "NPI Search","        NPI Search page should displayed");

		logger.info("*** Step 4 Actions: - Input data for NPI field and click on Reset button");
		Cln procedureCodeTemp = clientDao.getAllNpiInfoByNpiNumber();
		String NpiNumber = String.valueOf(procedureCodeTemp.getNpi());
		assertTrue(isElementPresent(npiSearch.npiInput(), 5),"        NPI input field should be shown");
		npiSearch.inputNPI(NpiNumber);	
		assertTrue(isElementPresent(npiSearch.npiSearchButton(), 5),"        The NPI Search button should be displayed");
		npiSearch.clickNPIResetButton();

		logger.info("*** Step 4 Expected Results: - Data in NPI field is reset");
		assertEquals(npiSearch.npiInput().getText(), "", "        Data in NPI field should be reset to empty");

		npiSearch.clickNPICloseButton();
		switchToParentWin(parent1);
		clientSearch.clickClientSearchCloseButton();
		switchToParentWin(parent);
	}

	@Test(priority = 1, description = "Search Client - Search NPI - Verify New Search button")
	public void testXPR_353() throws Exception {
		logger.info("===== Testing - testXPR_353 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
		 
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");

		logger.info("*** Step 2 Actions: - Click on Search ClientId icon");
		clientSearch = new ClientSearch(driver);
		assertTrue(isElementPresent(clientSearch.clientSearchIconBtn(), 5),"        The Client Search icon should be displayed");
		clientSearch.clickClientSearchIconBtn();
		String parent = switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Search Client page is displayed");
		assertTrue(isElementPresent(clientSearch.clientSearchTitle(), 5),"        The Client Search page title should be displayed");
		assertEquals(clientSearch.clientSearchTitle().getText(), "Client Search","        Client Search page should displayed");

		logger.info("*** Step 3 Actions: - Click on Search NPI icon");
		npiSearch = new NpiSearch(driver);
		assertTrue(isElementPresent(npiSearch.npiSearchIconBtn(), 5),"        The NPI Search icon should be displayed");
		npiSearch.clickNPISearchIconBtn();
		String parent1 = switchToPopupWin();

		logger.info("*** Step 3 Expected Results: - The Search NPI page is displayed");
		assertTrue(isElementPresent(npiSearch.npiTitle(), 5),"        The NPI Search page title should be displayed");
		assertEquals(npiSearch.npiTitle().getText(), "NPI Search","        NPI Search page should displayed");

		logger.info("*** Step 4 Actions: - Input NPI in NPI field and click on Search button");
		Cln procedureCodeTemp = clientDao.getAllNpiInfoByNpiNumber();
		String NpiNumber = String.valueOf(procedureCodeTemp.getNpi());
		assertTrue(isElementPresent(npiSearch.npiInput(), 5),"        NPI input field should be shown");
		npiSearch.inputNPI(NpiNumber);	
		assertTrue(isElementPresent(npiSearch.npiSearchButton(), 5),"        The NPI Search button should be displayed");
		npiSearch.clickNPISearchButton();

		logger.info("*** Step 4 Expected Results: - Search Result page is displayed");
		npiSearchResults = new NpiSearchResults(driver);
		assertTrue(isElementPresent(npiSearchResults.npiSearchResultTitle(), 5),"        The NPI Search Results page title should be displayed");
		assertEquals(npiSearchResults.npiSearchResultTitle().getText(), "NPI Search Results","        NPI Search Results title should displayed");

		logger.info("*** Step 5 Actions: - Click on New Search button");
		assertTrue(isElementPresent(npiSearchResults.npiSearchResultsNewSearchButton(), 5),"        The New Search button should be displayed");
		npiSearchResults.clickNPISearchResultsNewSearchButton();

		logger.info("*** Step 5 Expected Results: - System will return the Search page");
		assertTrue(isElementPresent(npiSearch.npiTitle(), 5),"        The NPI Search page title should be displayed");
		assertEquals(npiSearch.npiTitle().getText(), "NPI Search","        NPI Search page should displayed");

		npiSearch.clickNPICloseButton();
		switchToParentWin(parent1);
		clientSearch.clickClientSearchCloseButton();
		switchToParentWin(parent);
	}

	// Client Account Aging Balances
	@Test(priority = 1, description = "Client Account Aging Balances - Verify Hide button")
	public void testXPR_355() throws Exception {
		logger.info("===== Testing - testXPR_355 =====");          

		logger.info("*** Step 1 Actions: - Log into Xifin portal Client Transaction Detail screen with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 2 Actions: - Load Client - Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client - Transaction Detail page title should be displayed");		
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"), "        Page Title should be 'Client - Transaction Detail'");

		logger.info("*** Step 3 Actions: - Enter a Valid Client ID");
		String clientID = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5), "        testId input field should show");
		clientTransactionDetail.checkInputClientId(clientID);

		logger.info("*** Step 3 Expected Results: - Client - Transaction Detail page is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        ClientID Label should be displayed.");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        ClientName Label should be displayed.");
		assertTrue(isElementPresent(clientTransactionDetail.accountTypeLabel(), 5),"        AccountType Label should be displayed.");

		logger.info("*** Step 4 Actions: In Client Account Aging Balance section: Click on Hide icon");
		assertTrue(isElementPresent(clientTransactionDetail.clientAABShowHideTableIcon(), 5),"        The hide icon should be displayed");
		clickHiddenPageObject(clientTransactionDetail.clientAABShowHideTableIcon(),0);

		logger.info("*** Step 4 Expected Results: - The Client account Aging Balance grid is hidden");	
		assertEquals(clientTransactionDetail.clientAABHeader().getCssValue("display"),"none", "        The client Account Aging Balance grid is hidden");
		clientTransactionDetail.clickClientTransactionDetailResetButton();
		}
	
/*  Removed due to a change to the UI that removed Create a new Statement Date pencil button
	// Statements
	@Test(priority = 1, description ="Client Accn Detail - Create Statement with invalid input")
	@Parameters({"email","password"})
	public void testXPR_364(String email,String password) throws Exception {
		logger.info("===== Testing - testXPR_364 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new Navigation(driver);
		navigation.navigateToClientTransactionDetailPage();
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Client - Transaction Detail","        Load Client Transaction Detail page should displayed");

		logger.info("*** Step 2 Actions: - Input valid ClientId and tab out");
		String clientID = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        ClientId field input should show");
		clientTransactionDetail.checkInputClientId(clientId);

		logger.info("*** Step 2 Expected Results: - The Client - Transaction detail page is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Client - Transaction Detail","        Load Client Transaction Detail page should displayed");

		logger.info("*** Step 3 Actions: - Click on Pencil icon and input invalid format for Statement Date and tab out");
		//assertTrue(isElementPresent(clientTransactionDetail.createNewStatementDateIcon(), 5),"        The Pencil icon should be displayed");
		//clickHiddenPageObject(clientTransactionDetail.createNewStatementDateIcon(), 0);	
		randomCharacter = new RandomCharacter(driver);
		String date = "30/15/2015";

		assertTrue(isElementPresent(clientTransactionDetail.statementDateInput(), 5),"        The Statement Date input should be displayed");		
		clientTransactionDetail.inputStatementDate(date);
		assertTrue(isElementPresent(clientTransactionDetail.clientTransactionDetailSaveButton(), 5),"        The Save button should be displayed");
		clientTransactionDetail.clickClientTransactionDetailSaveButton();

		logger.info("*** Step 3 Expected Results: - The Error message will be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.accDErrorMessContent(), 5),"        Error message must be display");
		assertTrue(clientTransactionDetail.accDErrorMessContent().getText().equalsIgnoreCase("Invalid date format."),"        Error message must be display correct");
		clientTransactionDetail.clickAccDErrorMessCloseIcon();

		clientTransactionDetail.clickClientTransactionDetailResetButton();
		closeAlertAndGetItsText(true);
		}


	@Test(priority = 1, description ="Client Accn Detail - Create Statement with date is a future date")
	@Parameters({"email","password"})
	public void testXPR_365(String email,String password) throws Exception {
		logger.info("===== Testing - testXPR_365 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new Navigation(driver);
		navigation.navigateToClientTransactionDetailPage();
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Client - Transaction Detail","        Load Client Transaction Detail page should displayed");

		logger.info("*** Step 2 Actions: - Input valid ClientId and tab out");
		String clientID = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        ClientId field input should show");
		clientTransactionDetail.checkInputClientId(clientId);

		logger.info("*** Step 2 Expected Results: - The Client - Transaction detail page is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Client - Transaction Detail","        Load Client Transaction Detail page should displayed");

		logger.info("*** Step 3 Actions: - Client Accession Detail - Click on Pencil icon and input Statement Date is future date and tab out");
		testCodeUtils = new TestCodeUtils(driver);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(isElementPresent(clientTransactionDetail.createNewStatementDateIcon(), 5),"        create New Statement Date icon should be shown");
		int x = clientTransactionDetailUtils.getLeftSide(clientTransactionDetail.createNewStatementDateIcon());
		int y = clientTransactionDetailUtils.getTopSide(clientTransactionDetail.createNewStatementDateIcon());
		scrollToElement(x, y - 150);
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Statement Date dropdown should show");
		//String minStatementDate = clientTransactionDetail.checkStatementSate(clientTransactionDetail.statementDateDropdown().getAttribute("value"));
		//String date = testCodeUtils.getNextYear(minStatementDate);
		timeStamp = new TimeStamp(driver);
		String date = testCodeUtils.getNextYear(timeStamp.getCurrentDate());		
		clientTransactionDetail.clickCreateNewStatementDateIcon();
		clientTransactionDetail.inputStatementDate(date);	

		logger.info("*** Step 3 Expected Results: - The Get Statement Date Info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        The Get Statement Date Info button is displayed");
		assertTrue(isElementEnabled(clientTransactionDetail.getStatementDateInfoBtn(), 5, true), "         The Get Statement Date Info button is enable");

		logger.info("*** Step 4 Actions: - Click on Get Statement Date Info button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");

		logger.info("*** Step 4 Expected Results: - The Error message 'Invalid Date. You cannot enter a future statement date' is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.accDErrorMessContentFutureDate(), 5),"        Error message must be display");
		assertTrue(clientTransactionDetail.accDErrorMessContentFutureDate().getText().equalsIgnoreCase("Invalid date. You cannot enter a future statement date."),"        Error message must be display correct");

		clientTransactionDetail.clickClientTransactionDetailResetButton();
		closeAlertAndGetItsText(true);
		}
		*/

	//Statement 

	@Test(priority = 1, description="Statements- View the file")
	public void testXPR_357() throws Exception{
		logger.info("===== Testing - testXPR_357 ======");

		logger.info("*** Step 1 action: login to SSO by valid username and password");

		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
		
		logger.info("*** Step 1 Expected result: the Load Client transaction Detail page is displayed");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        The Load Client Transaction Detail page is displayed");

		logger.info("*** Step 2 Action: Enter a valid Client ID and tab out");
		String clientId = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		clientTransactionDetail.setClientIdInput(clientId);

		logger.info("Step 2 Expected result: the Client Transaction Detail page is displayed with correct information");
		assertTrue(isElementPresent(clientTransactionDetail.clientTransactionDetailSaveButton(), 5));
		assertTrue(clientTransactionDetail.clientIDLabel().getText().trim().equalsIgnoreCase(clientId),"        The Client Transaction Detail page is displayed with correct Client information");

		logger.info("*** Step 3 action: Click on any hyperlink at Previous Statement table");
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(isElementPresent(clientTransactionDetail.statementTable(), 5));
		assertTrue(isElementPresent(clientTransactionDetail.statementTableValue(), 5),"        The hyperlink in Previous Statement table is displayed");
		String fileName = clientTransactionDetail.statementTableValue().getText();
		clickHiddenPageObject(clientTransactionDetail.statementTableValue(), 0);

		logger.info("*** Step 3 Expected result: New browser is open");
		String winHandler = driver.getWindowHandle();
		switchToPopupWin();
		assertTrue(driver.getCurrentUrl().contains(fileName),"        Statement file page is displayed");	
		
		driver.close();
		switchToWin(winHandler);
		clientTransactionDetail.clickClientTransactionDetailResetButton();		
	}

	@Test(priority = 1, description="Statements-Verify Hide icon")
	public void testXPR_360() throws Exception{
		logger.info("===== Testing - testXPR_360 =====");

		logger.info("*** Step 1 Actions: login by valid SSO username and password, navigate to Client Transaction Detail screen");
		
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected result: The Load Client Transaction detail page is displayed with correct page title");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"         The Load Client Transaction Detail page is displayed");

		logger.info("*** Step 2 Action: Enter a valid Client ID and tab out");
		String clientID = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		clientTransactionDetail.setClientIdInput(clientID);

		logger.info("*** Step 2 Expected result: the Client Transaction Detail page is displayed with correct information");
		assertTrue(isElementPresent(clientTransactionDetail.clientTransactionDetailSaveButton(), 10));
		assertTrue(clientTransactionDetail.clientIDLabel().getText().trim().equalsIgnoreCase(clientID),"        The Client Transaction Detail page is displayed with correct information");

		logger.info("*** Step 3 Action: Click on Hide Statement icon");
		assertTrue(isElementPresent(clientTransactionDetail.statementTable(), 5),"        the Previous statement table is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.iconHideStatement(), 5));
		clientTransactionDetail.clickHideStatementIcon();

		logger.info("*** Step 3 Expected result: The Previous Statement table is hidden");
		assertTrue(isElementHidden(clientTransactionDetail.wrapperOfPreviousStatementTable(), 5),"        The Previous Statement table is hidden");
		
		clientTransactionDetail.clickClientTransactionDetailResetButton();
		}

	@Test(priority=1, description="Statements-Verify footer icon in the grid")
	public void testXPR_361() throws Exception{
		logger.info("===== Testing - XPR_361 ======");

		logger.info("*** Step 1 Action: Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);

		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected result: The Load Client Transaction Detail page is displayed");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"     The Load Client Transaction Detail page is displayed");

		logger.info("*** Step 2 Action: Enter a valid Client Id");
		List<Cln> clns = clientDao.getClnForClientSearch();
		String clientId = clns.get(RandomCharacter.getRandomIntInRange(0, clns.size())).getClnAbbrev();
		clientTransactionDetail.checkInputClientId(clientId);

		logger.info("*** Step 2 Expected result: the Client Transaction Detail page is displayed with correct page title");
		assertTrue(isElementPresent(clientTransactionDetail.clientTransactionDetailSaveButton(), 5));
		assertTrue(clientTransactionDetail.clientIDLabel().getText().trim().equalsIgnoreCase(clientId),"        The Client Transaction Detail page is displayed with correct information");

		logger.info("*** Step 3 Action: Click on each buttons at footer menu");
		String beforePage = clientTransactionDetail.currentPageInput().getAttribute("value");
		assertTrue(isElementPresent(clientTransactionDetail.statementTable(), 5));
		assertTrue(isElementPresent(clientTransactionDetail.nextPageStatementIcon(), 5),"         The Next Page icon at Previous statement table is displayed");		
		clickHiddenPageObject(clientTransactionDetail.nextPageStatementIcon(), 0);
		assertTrue(isElementEnabled(clientTransactionDetail.previousPageStatementIcon(), 5, true),"        The previous page icon is enable to click");
		String currentPage = clientTransactionDetail.currentPageInput().getAttribute("value");
		assertEquals(Integer.parseInt(currentPage), Integer.parseInt(beforePage)+1,"        System navigate to next page");

		beforePage = currentPage;
		assertTrue(isElementPresent(clientTransactionDetail.previousPageStatementIcon(), 5),"          The Previous Page icon is displayed");
		clickHiddenPageObject(clientTransactionDetail.previousPageStatementIcon(), 0);
		currentPage = clientTransactionDetail.currentPageInput().getAttribute("value");
		assertEquals(Integer.parseInt(currentPage), Integer.parseInt(beforePage)-1,"        System take user return the previous page");

		assertTrue(isElementPresent(clientTransactionDetail.lastPageStatementIcon(), 5),"        The last page icon is displayed");
		clickHiddenPageObject(clientTransactionDetail.lastPageStatementIcon(), 0);
		assertTrue(isElementEnabled(clientTransactionDetail.firstPageStatementIcon(), 10, true),"        The first page icon is enable after user click on last page icon");
		currentPage =  clientTransactionDetail.currentPageInput().getAttribute("value");
		String totalPage = clientTransactionDetail.totalPageStatement().getText().replace(",", "");
		assertEquals(Integer.parseInt(currentPage), Integer.parseInt(totalPage));

		assertTrue(isElementPresent(clientTransactionDetail.firstPageStatementIcon(), 5),"        The first page icon is displayed");
		clickHiddenPageObject(clientTransactionDetail.firstPageStatementIcon(), 0);
		currentPage = clientTransactionDetail.currentPageInput().getAttribute("value");
		assertEquals(Integer.parseInt(currentPage), 1,"        System take user go to the first page");
		
		clientTransactionDetail.clickClientTransactionDetailResetButton();
		}
	
	//Client Adjustment
	@Test(priority=1, description="Client Accn Detail-Client Adjustments-Create Client adj with invalid data")
	public void testXPR_377() throws Exception{
		logger.info("====== Testing - XPR_377 ======");

		logger.info("*** Step 1 Action: login to SSO by valid username and password");

		navigation = new MenuNavigation(driver, config);

		navigation.navigateToClientTransactionDetailPage();


		logger.info("*** Step 1 Expected result: The Load Client Transaction Detail page is displayed");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        The Load Client Transaction Detail page is displayed");

		logger.info("*** Step 2 Action: Enter a valid Client ID and tab out");
		String clientId = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		clientTransactionDetail.setClientIdInput(clientId);

		logger.info("*** Step 2 Expected result: The Client Transaction Detail page is displayed with correct information");
		assertTrue(isElementPresent(clientTransactionDetail.clientTransactionDetailSaveButton(), 10),"         The Client Transaction Detail page is displayed");
		assertTrue(clientTransactionDetail.clientIDLabel().getText().trim().equalsIgnoreCase(clientId),"        The Client Transaction Detail page is displayed correct information");

		logger.info("*** Step 3 Actions: select any statement date and tab out");
		testCodeUtils = new TestCodeUtils(driver);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        The Statement date dropdown list is available");
		Date statementDate = clientDao.getClnSubmWithAmtFromCLNSUBMByClnAbbrev(clientId).getSubmDt();
		String date = ConvertUtil.getDateAsTring(statementDate, "MM/dd/yyyy", "");
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), date);

		logger.info("*** Step 3 Expected result: The Get Statement Date info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        The Get Statement Date Info is enable to click");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);

		logger.info("*** Step 4 Action: Click on Add icon at Client Adjustment table");
		assertTrue(isElementPresent(clientTransactionDetail.clientAdjustmentTable(), 5));
		clickHiddenPageObject(clientTransactionDetail.addNewClientAdjustmentsBtn(), 0);

		logger.info("*** Step 4 Expected result: the Add Record popup is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.amountInput(), 5),"        The Add Record popup is displayed");

		logger.info("*** Step 5 Actions: Enter an invalid AccnId and click on OK button in Add Record popup window");
		randomCharacter = new RandomCharacter(driver);
		String accnID = randomCharacter.getRandomNumericString(3);
		clientTransactionDetail.selectAdjCode();
		clientTransactionDetail.inputRevAdjAccnId(accnID);
		clientTransactionDetail.clickOkClientAdjBtn();
		Thread.sleep(1000);

		logger.info("*** Step 5 Expected results: Field Validation error message is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.validationClientAdjMessage(), 5),"        A Field Validation error message should be displayed.");
		String errMsg = clientTransactionDetail.validationClientAdjMessage().getText().trim();
		assertEquals(errMsg, "Accession " + accnID + " was not found.", "         The Field Validation error message: Accession " + accnID + " was not found. should show.");
		
		clientTransactionDetail.clickCancelAddAdjustmentBtn();
		clientTransactionDetail.clickClientTransactionDetailResetButton();		
		}

	@Test(priority=1, description="Client Accn Detail-Client Adjust-Add Client Adj with require field is empty")
	public void testXPR_378() throws Exception{
		logger.info("====== Testing - XPR_378 ======");

		logger.info("*** Step 1 Action: Login with valid username and password");

		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected result: The Load Client Transaction Detail page is displayed");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        The Load Client Transaction Detail page is displayed");

		logger.info("*** Step 2 Action: input valid ClientID and tab out");
		String clientId = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		clientTransactionDetail.checkInputClientId(clientId);

		logger.info("*** Step 2 Expected result: the Client Transaction Detail page is displayed with correct information");
		assertTrue(isElementPresent(clientTransactionDetail.clientTransactionDetailSaveButton(), 5),"        The Client Transaction Detail page is displayed");
		assertTrue(clientTransactionDetail.clientIDLabel().getText().trim().equalsIgnoreCase(clientId),"        The client transaction detail page is loaded correct information");

		logger.info("*** step 3 Action: select any statement date in dropdown list");
		testCodeUtils = new TestCodeUtils(driver);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Statement Date dropdown should show");
		testCodeUtils.selectItemByIndex(clientTransactionDetail.statementDateDropdown(), 0);
		Date statementDate = clientDao.getClnSubmWithAmtFromCLNSUBMByClnAbbrev(clientId).getSubmDt();
		String date = ConvertUtil.getDateAsTring(statementDate, "MM/dd/yyyy", "");
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), date);
		
		logger.info("*** Step 3 Expected result: the Get Statement date info is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(),5),"        The Get Statement Date info button is enable");

		logger.info("*** Step 4 Action: Click on Statement Date Info button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");

		logger.info("*** Step 4 Expected result: The reconciled PDF button is enable");
		assertTrue(isElementEnabled(clientTransactionDetail.reconciledPDFStatementBtn(), 10, true),"        The Reconciled PDF Statement button is enable");

		logger.info("*** Step 5 Action: click on Add icon at Client Adjustment table");
		assertTrue(isElementPresent(clientTransactionDetail.addNewClientAdjustmentsBtn(), 5));
		clickHiddenPageObject(clientTransactionDetail.addNewClientAdjustmentsBtn(), 0);

		logger.info("*** Step 5 Expected result: The Add record popup is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.amountInput(), 5),"        The Add record popup is displayed");

		logger.info("*** Step 6 Action: input data for some fields and level Adj Code field is empty");
		randomCharacter = new RandomCharacter(driver);
		String amount = randomCharacter.getRandomNumericString(3);
		String accnId = daoManagerXifinRpm.getRandomAccnIdForSuperSearch(testDb);
		assertTrue(isElementPresent(clientTransactionDetail.amountInput(), 5));
		clientTransactionDetail.inputPaymentAmount(amount);
		assertTrue(isElementPresent(clientTransactionDetail.reviewAdjustmentsAccn(), 5));
		clientTransactionDetail.inputRevAdjAccnId(accnId);
		assertTrue(isElementPresent(clientTransactionDetail.okClientAdjBtn(), 5));
		clientTransactionDetail.clickOkClientAdjBtn();

		logger.info("*** Step 6 Expected result: The error message is displayed correct content");
		assertTrue(isElementPresent(clientTransactionDetail.errorMessageClientAdj(), 5),"        The error message is displayed");
		assertEquals(clientTransactionDetail.errorMessageClientAdj().getText(), "ADJ Code: Field is required", "        the error message is displayed correctly");

		clientTransactionDetail.clickCancelAddAdjustmentBtn();
		clientTransactionDetail.clickClientTransactionDetailResetButton();
		closeAlertAndGetItsText(true);
		}

	@Test(priority=1, description="Client Accn Detail-Client Adjustment-Verify Canned Note button")
	public void testXPR_379() throws Exception{
		logger.info("====== Testing - XPR_379 ======");

		logger.info("*** Step 1 action: login to SSO by valid username and password");

		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected result: the Load Client Transaction Detail page is displayed");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"          The Load Client Transaction Detail page is displayed");

		logger.info("*** Step 2 Action: input client ID and tab out");
		String clientId = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		clientTransactionDetail.checkInputClientId(clientId);

		logger.info("*** step 2 Expected result: The Client Transaction Detail page is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientTransactionDetailResetButton(), 5),"       The Client Transaction Detail page is displayed");
		assertTrue(clientTransactionDetail.clientIDLabel().getText().trim().equalsIgnoreCase(clientId),"       The Client Transaction Detail page is displayed correct information");

		logger.info("*** step 3 Action: select any statement date from dropdown list");
		testCodeUtils = new TestCodeUtils(driver);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5));
		Date statementDate = clientDao.getClnSubmWithAmtFromCLNSUBMByClnAbbrev(clientId).getSubmDt();
		String date = ConvertUtil.getDateAsTring(statementDate, "MM/dd/yyyy", "");
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), date);

		logger.info("*** step 3 Expected result: the Get Statement Date Info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        the Get Statement Date Info button is enable");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);

		logger.info("*** Step 4 Action: Click on Add icon at Client Adjustment table");
		assertTrue(isElementPresent(clientTransactionDetail.addNewClientAdjustmentsBtn(), 5));
		clickHiddenPageObject(clientTransactionDetail.addNewClientAdjustmentsBtn(), 0);

		logger.info("*** Step 4 Expected result: the Add Record popup is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.amountInput(), 5),"         The Add record popup is displayed");

		logger.info("*** Step 5 Action: click on canned Note button");
		assertTrue(isElementPresent(clientTransactionDetail.cannedNotesBtn(), 5));
		clientTransactionDetail.clickCannedNotesBtn();

		logger.info("*** Step 5 Expected result: The Canned note page is displayed");
		String winHandler = driver.getWindowHandle();
		switchToPopupWin();
		assertTrue(isElementPresent(clientTransactionDetail.cannedNotesTbl(), 5),"        The table Canned Note is displayed");

		// Switch to main win
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		clientTransactionDetail.clickCancelAddAdjustmentBtn();
		clientTransactionDetail.clickClientTransactionDetailResetButton();
		}

	@Test(priority=1, description="Client Accn Detail-Client Adjust-Cancel New Client Adjustment")
	public void testXPR_380() throws Exception{
		logger.info("====== Testing - XPR_380 ======");

		logger.info("*** Step 1 action: login to Mar with valid username and password");

		navigation = new MenuNavigation(driver, config);

		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected result: The Load Client Transaction Detail page is displayed");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        The Load Client Transaction Detail page is displayed");

		logger.info("*** Step 2 Action: Input valid Client ID and tab out");
		String clientId = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		clientTransactionDetail.checkInputClientId(clientId);

		logger.info("*** Step 2 Expected result: The Client Transaction Detail is displayed with correct information");
		assertTrue(isElementPresent(clientTransactionDetail.clientTransactionDetailSaveButton(), 5),"        The Client Transaction Detail page is displayed");
		assertTrue(clientTransactionDetail.clientIDLabel().getText().trim().equalsIgnoreCase(clientId),"        The Client Transaction Detail page is displayed with correct information");

		logger.info("*** step 3 Action: Select any statement date from the dropdown list");
		testCodeUtils = new TestCodeUtils(driver);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5));
		Date statementDate = clientDao.getClnSubmWithAmtFromCLNSUBMByClnAbbrev(clientId).getSubmDt();
		String date = ConvertUtil.getDateAsTring(statementDate, "MM/dd/yyyy", "");
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), date);

		logger.info("*** Step 3 Expected result: The Get Statement Date Info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(),5),"        the Get Statement Date Info button is enable");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);

		logger.info("*** Step 4 Action: click on Add icon at Client Adjustments table");
		assertTrue(isElementPresent(clientTransactionDetail.addNewClientAdjustmentsBtn(), 5));
		clickHiddenPageObject(clientTransactionDetail.addNewClientAdjustmentsBtn(), 0);

		logger.info("*** Step 4 Expected result: the Add Record popup is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.amountInput(), 5),"        The Add Record popup is displayed");

		logger.info("*** Step 5 Action: Input valid data for some field and click on Cancel button");
		String accnId = daoManagerXifinRpm.getRandomAccnIdForSuperSearch(testDb);
		assertTrue(isElementPresent(clientTransactionDetail.adjustmentsCodeDropdown(), 5));		
		clientTransactionDetail.selectAdjCode();
		assertTrue(isElementPresent(clientTransactionDetail.reviewAdjustmentsAccn(), 5));
		clientTransactionDetail.inputRevAdjAccnId(accnId);
		assertTrue(isElementPresent(clientTransactionDetail.cancelAddAdjustmentBtn(), 5));
		randomCharacter = new RandomCharacter(driver);
		String amount = randomCharacter.getRandomNumericString(3);
		assertTrue(isElementPresent(clientTransactionDetail.amountInput(), 5));
		clientTransactionDetail.inputPaymentAmount(amount);		
		clientTransactionDetail.clickCancelAddAdjustmentBtn();

		logger.info("*** Step 5 Expected result: the Add record popup is closed");
		assertFalse(getColumnValue(clientTransactionDetail.clientAdjustmentTable(), amount));
		assertEquals(clientTransactionDetail.okClientAdjBtn().getCssValue("display"), "inline-block","        The Add record popup is close");


		clientTransactionDetail.clickClientTransactionDetailResetButton();
		closeAlertAndGetItsText(true);
		}

	@Test(priority = 1, description ="Client Accn Detail-Client Adjust-Reset new Client Adjustment")
	public void testXPR_381() throws Exception {

		logger.info("===== Testing - testXPR_381 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");

		logger.info("*** Step 2 Actions: - Input valid ClientId and tab out");
		ArrayList<String> adjInfo = daoManagerXifinRpm.getADJInfo(testDb);
		String clientId = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        ClientId field input should show");
		clientTransactionDetail.checkInputClientId(clientId);

		logger.info("*** Step 2 Expected Results: - The Client - Transaction detail page is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");

		logger.info("*** Step 3 Actions: - Client Accession Detail -Select any Statement Date in dropdown list and tab out");
		testCodeUtils = new TestCodeUtils(driver);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Statement Date dropdown should show");
		String statementDate = daoManagerPlatform.getClnSubmInfoFromCLNSUBMByClnAbbrev(clientId, testDb).get(1);
		selectItem(clientTransactionDetail.statementDateDropdown(), statementDate);

		logger.info("*** Step 3 Expected Results: - The Get Statement Date Info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        The Get Statement Date Info button is displayed");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);

		logger.info("*** Step 4 Actions: - Client Adjustments - Click on Add icon");
		assertTrue(isElementPresent(clientTransactionDetail.addNewClientAdjustmentsBtn(), 5),"        The Add new client adjustment button is displayed");
		clickHiddenPageObject(clientTransactionDetail.addNewClientAdjustmentsBtn(), 0);

		logger.info("*** Step 4 Expected Results: - Client Adjustments - Add Record popup is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.addNewClientAdjustmentsPopup(), 5),"        Client Adjustments - Add Record popup is displayed");

		logger.info("*** Step 5 Actions: - Client Adjustments - Input valid data for field: ADJ code, Rev ADJ Accn, Amount, Note and click OK button");
		String adjAccn = adjInfo.get(1);
		assertTrue(isElementPresent(clientTransactionDetail.reviewAdjustmentsAccn(), 5),"        Rev ADJ Accn field is displayed");
		clientTransactionDetail.inputRevAdjAccnId(adjAccn);
		assertTrue(isElementPresent(clientTransactionDetail.amountInput(), 5),"        Amount field is displayed");
		randomCharacter = new RandomCharacter(driver);
		String amount = randomCharacter.getRandomNumericString(4);
		clientTransactionDetail.inputAddRecordAmount(amount);
		assertTrue(isElementPresent(clientTransactionDetail.adjCodeDropdown(), 5),"        ADJ Code dropdown is displayed");
		String adjCode = clientTransactionDetail.selectAdjCode();
		assertTrue(isElementPresent(clientTransactionDetail.okClientAdjBtn(), 5),"        OK button is displayed");
		clientTransactionDetail.clickOkClientAdjBtn();

		logger.info("*** Step 5 Expected Results: - New Client adjustment is added in table ");
		assertTrue(isElementPresent(clientTransactionDetail.clientAdjustmentTbl(), 5),"        Client adjust table should show");
		assertTrue(getColumnValue(clientTransactionDetail.clientAdjustmentTbl(), adjCode));

		logger.info("*** Step 6 Actions : Click on Reset button");
		clientTransactionDetail.clickClientTransactionDetailResetButton();
		closeAlertAndGetItsText(true);

		logger.info("*** Step 6 Expected Results: - New Client adjustment is not save into database ");
		clientTransactionDetail.checkInputClientId(clientId);
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        statement Date Dropdown should show");
		selectItem(clientTransactionDetail.statementDateDropdown(), statementDate);
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		assertFalse(getColumnValue(clientTransactionDetail.clientPaymentTable(),testCodeUtils.formatDecimalPoint(Integer.parseInt(amount))),"       New Client adjustment is not save into database");

		clientTransactionDetail.clickClientTransactionDetailResetButton();
		}
	
	@Test(priority = 1, description ="Client Accn Detail-Client Adjust- Edit Client with valid data")
	public void testXPR_382() throws Exception {

		logger.info("===== Testing - testXPR_382 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");

		logger.info("*** Step 2 Actions: - Input valid ClientId and tab out");
		ArrayList<String> adjInfo = daoManagerXifinRpm.getADJInfo(testDb);
		String clientId = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        ClientId field input should show");
		clientTransactionDetail.checkInputClientId(clientId);

		logger.info("*** Step 2 Expected Results: - The Client - Transaction detail page is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");

		logger.info("*** Step 3 Actions: - Client Accession Detail Section : select any statement date in Dropdown list - Tab out");
		testCodeUtils = new TestCodeUtils(driver);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Statement Date dropdown should show");
		Date statementDate = clientDao.getClnSubmWithAmtFromCLNSUBMByClnAbbrev(clientId).getSubmDt();
		String date = ConvertUtil.getDateAsTring(statementDate, "MM/dd/yyyy", "");
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), date);

		logger.info("*** Step 3 Expected Results: - The Get Statement Date Info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        The Get Statement Date Info button is displayed");
		assertTrue(isElementEnabled(clientTransactionDetail.getStatementDateInfoBtn(), 5, true), "         The Get Statement Date Info button is enable");

		logger.info("*** Step 4 Actions: Click on Statement Date info Button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");

		logger.info("*** Step 4 Expected Results: The corresponding data for selected statement date will be load ");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(date, clientId, testDb),"        Data of statement date must be correct");
		
		scrollToElement(clientTransactionDetail.clientAdjustmentTable());

		logger.info("*** Step 5 Actions: - Client Adjustments - Click on Add icon");
		assertTrue(isElementPresent(clientTransactionDetail.addNewClientAdjustmentsBtn(), 5),"        The Add new client adjustment button is displayed");
		clickHiddenPageObject(clientTransactionDetail.addNewClientAdjustmentsBtn(), 0);

		logger.info("*** Step 5 Expected Results: - Client Adjustments - Add Record popup is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.addNewClientAdjustmentsPopup(), 5),"        Client Adjustments - Add Record popup is displayed");

		logger.info("*** Step 6 Actions: - Client Adjustments - Enter valid data for field: ADJ code, Rev ADJ Accn, Amount, Note and click OK button");
		String adjAccn = adjInfo.get(1);
		assertTrue(isElementPresent(clientTransactionDetail.reviewAdjustmentsAccn(), 5),"        Rev ADJ Accn field is displayed");
		clientTransactionDetail.inputRevAdjAccnId(adjAccn);
		assertTrue(isElementPresent(clientTransactionDetail.amountInput(), 5),"        Amount field is displayed");
		randomCharacter = new RandomCharacter(driver);
		String amount = randomCharacter.getRandomNumericString(4).replace("0", "1");
		clientTransactionDetail.inputAddRecordAmount(amount);
		assertTrue(isElementPresent(clientTransactionDetail.adjCodeDropdown(), 5),"        ADJ Code dropdown is displayed");
		String adjCode = clientTransactionDetail.selectAdjCode();
		assertTrue(isElementPresent(clientTransactionDetail.okClientAdjBtn(), 5),"        OK button is displayed");
		clickHiddenPageObject(clientTransactionDetail.okClientAdjBtn(), 0);

		logger.info("*** Step 6 Expected Results: - The newly added Client adjustment is showing in Client Adjustments table ");
		assertTrue(isElementPresent(clientTransactionDetail.clientAdjustmentTbl(), 5),"        Client Adjustments table should show.");
		assertTrue(getColumnValue(clientTransactionDetail.clientAdjustmentTbl(), adjCode));

		logger.info("*** Step 7 Actions : Double click on the Client Adjustment has just created");
		assertTrue(isElementPresent(clientTransactionDetail.adjCodeCellClientAdjustmentTable(adjCode), 5),"        ADJ Code Column should show.");
		clientTransactionDetailUtils.doubleclickHiddenPageObject(clientTransactionDetail.adjCodeCellClientAdjustmentTable(adjCode), 0);

		logger.info("*** Step 7 Expected Results: - Edit Record popup is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.addNewClientAdjustmentsPopup(), 5),"        Client Adjustments - Edit Record popup should show.");
		convertUtil = new ConvertUtil();
		String s = clientTransactionDetail.currentAmountDueAdj().getAttribute("value").replaceAll("[$,]", "").trim();
		double currAmtDue = Double.parseDouble(s);
		String newAmtDueStr = "$" + convertUtil.formatDollarAmount(currAmtDue + Double.parseDouble(amount)).substring(1);

		assertEquals(clientTransactionDetail.newAmountDueAdj().getAttribute("value"),newAmtDueStr,"       New amount due should be " + newAmtDueStr);
		assertEquals(clientTransactionDetail.newAmountDueAdj().getAttribute("disabled"),"true","       New amount due is read-only");

		logger.info("*** Step 8 Actions : Update the Adjustment with new data ");
		adjInfo = daoManagerXifinRpm.getADJInfo(testDb);
		assertTrue(isElementPresent(clientTransactionDetail.adjCodeDropdown(), 5),"        ADJ Code dropdown is displayed");
		adjCode = clientTransactionDetail.selectAdjCode();
		
		assertTrue(isElementPresent(clientTransactionDetail.reviewAdjustmentsAccn(), 5),"        Rev ADJ Accn field is displayed");
		String revADJAccnId = adjInfo.get(1);
		clientTransactionDetail.inputRevAdjAccnId(revADJAccnId);
				
		randomCharacter = new RandomCharacter(driver);
		String notes = randomCharacter.getRandomAlphaNumericString(8);
		clientTransactionDetail.enterNotes(notes);
		
		assertTrue(isElementPresent(clientTransactionDetail.okClientAdjBtn(), 5),"        OK button is displayed");
		clientTransactionDetail.clickOkClientAdjBtn();

		logger.info("*** Step 8 Expected Results: - New data is added in table ");
		assertTrue(isElementPresent(clientTransactionDetail.clientAdjustmentTbl(), 5),"        Client adjust table should show.");
		assertTrue(getColumnValue(clientTransactionDetail.clientAdjustmentTbl(), revADJAccnId),"       New data - Adj Accn ID: " + revADJAccnId + " should show in the Client Adjustment table.");
		assertTrue(getColumnValue(clientTransactionDetail.clientAdjustmentTbl(), adjCode),"       New data - Adj Accn Code: " + adjCode + " should show in the Client Adjustment table.");
		assertTrue(getColumnValue(clientTransactionDetail.clientAdjustmentTbl(), notes),"       New data - Notes: " + notes + " should show in the Client Adjustment table.");

		clientTransactionDetail.clickClientTransactionDetailResetButton();
		}
	
	@Test(priority = 1, description ="Client Accn Detail-Client Adjust-Verify user cannot edit save row")
	public void testXPR_383() throws Exception {

		logger.info("===== Testing - testXPR_383 =====");

		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");

		logger.info("*** Step 2 Actions: - Input valid ClientId and tab out");
		List<String> adjInfo = daoManagerXifinRpm.getAdjInfoWithAdjAmountGreaterThan5(testDb);
		String clientId = adjInfo.get(0);
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        ClientId field input should show");
		clientTransactionDetail.checkInputClientId(clientId);

		logger.info("*** Step 2 Expected Results: - The Client - Transaction detail page is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");

		logger.info("*** Step 3 Actions: - Client Accession Detail Section : select any statement date in Dropdown list - Tab out");
		testCodeUtils = new TestCodeUtils(driver);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		String statementDate = adjInfo.get(1);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), statementDate);

		logger.info("*** Step 3 Expected Results: - The Get Statement Date Info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        The Get Statement Date Info button is displayed");

		logger.info("*** Step 4 Actions: Click on Statement Date info Button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");

		logger.info("*** Step 4 Expected Results: The corresponding data for selected statement date will be load ");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(adjInfo.get(1), clientId, testDb),"        Data of statement date must be correct");

		logger.info("*** Step 5 Actions : Double click on the new client Adjustment has just create");
		assertTrue(isElementPresent(clientTransactionDetail.adjCodeCellClientAdjustmentTable(), 5),"        adj code cell should show");
		clientTransactionDetailUtils.doubleclickHiddenPageObject(clientTransactionDetail.adjCodeCellClientAdjustmentTable(), 0);

		logger.info("*** Step 5 Expected Results: - Edit Record popup should not displayed");
		assertNull(clientTransactionDetail.editClientAdjustmentDialog(),"        Edit Record popup should not displayed");

		clientTransactionDetail.clickClientTransactionDetailResetButton();
		}

	@Test(priority = 1, description ="Client Accn Detail - Client Adjust - Verify the Filter function")
	public void testXPR_384() throws Exception {
		logger.info("===== Testing - testXPR_384 =====");
		
		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");
		
		logger.info("*** Step 2 Actions: - Enter a valid Client ID and tab out");
		String clientId = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        ClientId field input should show");
		clientTransactionDetail.checkInputClientId(clientId);
		
		logger.info("*** Step 2 Expected Results: - The Client - Transaction detail page is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");
		
		logger.info("*** Step 3 Actions: - Client Accession Detail - Select any Statement Date and tab out");
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        The Statement dropdown is displayed");
		String statementDate = daoManagerPlatform.getClnSubmInfoFromCLNSUBMByClnAbbrev(clientId, testDb).get(1);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), statementDate);
		
		logger.info("*** Step 3 Expected Results: - The Get Statement Date Info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        The Get Statement Date Info button is displayed");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		
		logger.info("*** Step 4 Actions: - Client Adjustment - Enter data for each column");
		List<List<String>> adjInfo1 = clientTransactionDetail.addClientAdjustment(3, testDb,this);
		String adjCode = adjInfo1.get(0).get(0);
		String adjDate = adjInfo1.get(0).get(3);
		String adjAmount = adjInfo1.get(0).get(2);

		assertTrue(isElementPresent(clientTransactionDetail.adjTableAdjDateInput(), 5),"        The ADJ Date input is displayed");
		clientTransactionDetail.inputAdjTableAdjDate(statementDate);
		assertTrue(clientTransactionDetailUtils.getColumnvalueWithIndex(clientTransactionDetail.clientAdjustmentTbl(),adjDate,1),"        The system will filter the grid by ADJ Date is correctly");
		clientTransactionDetail.clearData(clientTransactionDetail.adjTableAdjDateInput());
		assertTrue(isElementPresent(clientTransactionDetail.adjTableAdjCodeInput(), 5),"        The ADJ Code input is displayed");
		clientTransactionDetail.inputAdjTableAdjCode(adjCode);
		assertTrue(clientTransactionDetailUtils.getColumnvalueWithIndex(clientTransactionDetail.clientAdjustmentTbl(),adjCode,2),"        The system will filter the grid by ADJ Code is correctly");
		clientTransactionDetail.clearData(clientTransactionDetail.adjTableAdjCodeInput());
		assertTrue(isElementPresent(clientTransactionDetail.adjTableAdjAmountInput(), 5),"        The ADJ Amount input is displayed");
		clientTransactionDetail.inputAdjTableAdjAmount(adjAmount);
		assertTrue(clientTransactionDetailUtils.getColumnvalueWithIndex(clientTransactionDetail.clientAdjustmentTbl(),adjAmount,4),"        The system will filter the grid by ADJ Amount is correctly");
		clientTransactionDetail.clearData(clientTransactionDetail.adjTableAdjAmountInput());
				
		logger.info("*** Step 4 Expected Results: - The grid will be filter based on inputted value for each column");
		clickHiddenPageObject(clientTransactionDetail.clientTransactionDetailResetButton(), 0);
		closeAlertAndGetItsText(true);
		}
	
	@Test(priority = 1, description ="Client Accn Detail - Client Adjust - Verify the Hide icon")
	public void testXPR_385() throws Exception {
		logger.info("===== Testing - testXPR_385 =====");
		
		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");
		
		logger.info("*** Step 2 Actions: - Input valid ClientId and tab out");
		String clientId = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        ClientId field input should show");
		clientTransactionDetail.checkInputClientId(clientId);
		
		logger.info("*** Step 2 Expected Results: - The Client - Transaction detail page is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");
		
		logger.info("*** Step 3 Actions: - Client Accession Detail - Select any Statement Date and tab out");
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		testCodeUtils = new TestCodeUtils(driver);
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        The Statement dropdown is displayed");
		String statementDate = daoManagerPlatform.getClnSubmInfoFromCLNSUBMByClnAbbrev(clientId, testDb).get(1);
		selectColumnValue(clientTransactionDetail.statementDateDropdown(), statementDate);
		
		logger.info("*** Step 3 Expected Results: - The Get Statement Date Info button is enabled");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        The Get Statement Date Info button is displayed");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);

		logger.info("*** Step 4 Actions: - Client Adjustment - Click on Hide icon in the grid");
		assertTrue(isElementPresent(clientTransactionDetail.adjShowHideIcon(), 5),"        The Hide icon should be displayed");
		clickHiddenPageObject(clientTransactionDetail.adjShowHideIcon(),0);
		
		logger.info("*** Step 4 Expected Results: - The Client Adjustment table is hidden and hide icon is replace by shown icon");
		assertEquals(clientTransactionDetail.adjTableHeader().getCssValue("display"),"none", "        The client Account Aging Balance grid is hidden");
		
		clientTransactionDetail.clickClientTransactionDetailResetButton();
		}
	
	@Test(priority = 1, description ="Client Accn Detail - Client Adjust - Verify the Sort function")
	public void testXPR_386() throws Exception {
		logger.info("===== Testing - testXPR_386 =====");
		
		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");
		
		logger.info("*** Step 2 Actions: - Input valid ClientId and tab out");
		List<String> adjInfo = clientTransactionDetail.getClientIdAndStatementHaveBilledAccession(testDb);
		String clientID = adjInfo.get(0);
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        ClientId field input should show");
		clientTransactionDetail.checkInputClientId(clientID);
	
		logger.info("*** Step 2 Expected Results: - The Client - Transaction detail page is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");
		
		logger.info("*** Step 3 Actions: - Client Accession Detail - Select any Statement Date and tab out");			
		String statementDate = adjInfo.get(1);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), statementDate);
		
		logger.info("*** Step 3 Expected Results: - The Get Statement Date Info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        The Get Statement Date Info button is displayed");
		assertTrue(isElementEnabled(clientTransactionDetail.getStatementDateInfoBtn(), 5, true), "         The Get Statement Date Info button is enable");
					
		logger.info("*** Step 4 Actions: - Click on Get Statement Date Info button");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        The Get Statement Date Info button is displayed");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");
		
		logger.info("*** Step 4 Expected Results: - The corresponding data for selected Statement Date is loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(statementDate, clientID, testDb),"        Data of statement date must be correct");

		logger.info("*** Step 5 Actions: - Client Adjustment - Click on Amount column");
		assertTrue(isElementPresent(clientTransactionDetail.adjAmountColumn(), 5),"        The Amount column is displayed");
		
		logger.info("*** Step 5 Expected Results: - The data for Client Adjustment table will be sort by ascending");
		assertTrue(getSortingComparisonOnTable(clientTransactionDetail.clientAdjustmentTable(), 4, "asc"),"         System will perform decrease sort fee schedule based on effective date ");
		
		clientTransactionDetail.clickClientTransactionDetailResetButton();
		}
	
	
	//Client Billed Accessions
	@Test(priority = 1, description = "Client Accn Detail - Client Bill Accn - Verify Accession Detail page")
	public void testXPR_387() throws Exception {		
		
		logger.info("===== Testing - XPR_387 =====");  
		
		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - The Load Client Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"),"        Page Title should be 'Client - Transaction Detail'");
		
		logger.info("*** Step 2 Actions: - Input valid Client ID. Tab out");
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        Client ID input should be displayed");
		List<String> accInfo = clientTransactionDetail.getClientIdAndStatementHaveBilledAccession(testDb);
		String clientID = accInfo.get(0);
		clientTransactionDetail.setClientIdInput(clientID);
		
		logger.info("*** Step 2 Expected Result: - The Client Transaction Detail page displays");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        Client id label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        Client name label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client Accession Detail - Statement drop down should be displayed");

		logger.info("*** Step 3 Actions: - Select any statement from Client Accession Detail section");
		String statementDate = accInfo.get(1);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), statementDate);
		
		logger.info("*** Step 3 Expected Result: - The Get Statement date info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Get statement date button should be displayed");
		assertTrue(clientTransactionDetail.getStatementDateInfoBtn().isEnabled(),"        Get statement date button should be enable");
		
		logger.info("*** Step 4 Actions: - Click on Get Statement Date Info button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");
		
		logger.info("*** Step 4 Expected Result: - The corresponding data for selected statement date is loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(statementDate, clientID, testDb),"        Data of statement date must be correct");
		
		logger.info("*** Step 5 Actions: - Click on Accession Detail button");
		assertTrue(isElementPresent(clientTransactionDetail.accDetailBtn(), 5),"        Accession Detail button should be displayed");
		clickHiddenPageObject(clientTransactionDetail.accDetailBtn(), 0);
		String parentWin = switchToPopupWin();
		
		logger.info("*** Step 5 Expected Result: - List accession detail page is displayed with main information");
		assertTrue(isElementPresent(clientTransactionDetail.clientBillAccTbl(), 5),"       Client Bill Accession table should be displayed");
		assertTrue(clientTransactionDetail.clientIDLblInAccDetailPopup().getText().equalsIgnoreCase(clientID),"        Client ID is correct");
		assertTrue(clientTransactionDetail.statementDateInputInAccDetailPopup().getAttribute("value").equalsIgnoreCase(statementDate),"        Statement date is correct");
		assertTrue(clientTransactionDetail.verifyDataOfClientBillAccCorrespondingStatementDate(statementDate, clientID, testDb),"       List out all accession id belong to selected client ID");
		
		Thread.sleep(1000);
		driver.close();
		switchToParentWin(parentWin);
		clickHiddenPageObject(clientTransactionDetail.clientTransactionDetailResetButton(), 0);
		}
	
	@Test(priority = 1, description = "Client Accn Detail - Client Bill Accn - View Accn detail page")
	public void testXPR_388() throws Exception {		
		
		logger.info("===== Testing - XPR_388 =====");  
		
		logger.info("*** Step 1 Actions: - Log into Client Transaction Detail screen with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - The Load Client Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"),"        Page Title should be 'Client - Transaction Detail'");
		
		logger.info("*** Step 2 Actions: - Enter a valid Client ID. Tab out");
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        Client ID input should be displayed");
		List<String> accInfo = clientTransactionDetail.getClientIdAndStatementHaveBilledAccession(testDb);
		String clientID = accInfo.get(0);
		clientTransactionDetail.setClientIdInput(clientID);
		
		logger.info("*** Step 2 Expected Result: - The Client Transaction Detail page displays");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        Client id label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        Client name label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client Accession Detail - Statement drop down should be displayed");

		logger.info("*** Step 3 Actions: - Select any statement from Client Accession Detail section");
		String statementDate = accInfo.get(1);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), statementDate);
		
		logger.info("*** Step 3 Expected Result: - The Get Statement date info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Get statement date button should be displayed");
		assertTrue(clientTransactionDetail.getStatementDateInfoBtn().isEnabled(),"        Get statement date button should be enable");
		
		logger.info("*** Step 4 Actions: - Click on Get Statement Date Info button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(),0);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");

		logger.info("*** Step 4 Expected Result: - The corresponding data for selected statement date is loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(statementDate, clientID, testDb),"        Data of statement date must be correct");
		
		logger.info("*** Step 5 Actions: - Click on Accession Detail button");
		assertTrue(isElementPresent(clientTransactionDetail.accDetailBtn(), 5),"        Accession Detail button should be displayed");
		clickHiddenPageObject(clientTransactionDetail.accDetailBtn(),0);
		String parentWin = switchToPopupWin();
		
		logger.info("*** Step 5 Expected Result: - List accession detail page show with list out all accession id belong to selected client ID");
		assertTrue(isElementPresent(clientTransactionDetail.clientBillAccTbl(), 5),"       Client Bill Accession table should be displayed");
		assertTrue(clientTransactionDetail.verifyDataOfClientBillAccCorrespondingStatementDate(statementDate, clientID, testDb),"       List out all accession id belong to selected client ID");
		
		logger.info("*** Step 6 Actions: - Click on Accession ID on table");
		List<List<String>> billAcc = daoManagerXifinRpm.getListClientBillAccByClientIDAndStatementDate(clientID, statementDate, testDb);
		String accnId = billAcc.get(0).get(1);
		clientTransactionDetail.clickAccessionID(accnId);
		Thread.sleep(1000);
		driver.close();
		switchToParentWin(parentWin);
		
		logger.info("*** Step 6 Expected Results: - System will take user to Accession Detail screen");
		accessionDetail = new AccessionDetail(driver, config, wait);
		assertTrue(isElementPresent(accessionDetail.accnIdText(), 5),"       Accession ID input field should be displayed.");
		assertEquals(billAcc.get(0).get(1), accessionDetail.accnIdText().getAttribute("value"), "       Accession ID: " + accnId + " should be loaded.");
		accessionDetail.resetBtn().click();
		}
	

	@Test(priority = 1, description = "Client Accn Detail - Client Bill Accn - Verify Hide icon in the Accn page")
	public void testXPR_389() throws Exception {		
		
		logger.info("===== Testing - XPR_389 =====");  
		
		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - The Load Client Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"),"        Page Title should be 'Client - Transaction Detail'");
		
		logger.info("*** Step 2 Actions: - Input valid Client ID. Tab out");
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        Client ID input should be displayed");
		List<String> accInfo = clientTransactionDetail.getClientIdAndStatementHaveBilledAccession(testDb);
		String clientID = accInfo.get(0);
		clientTransactionDetail.setClientIdInput(clientID);
		
		logger.info("*** Step 2 Expected Result: - The Client Transaction Detail page displays");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        Client id label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        Client name label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client Accession Detail - Statement drop down should be displayed");

		logger.info("*** Step 3 Actions: - Select any statement from Client Accession Detail section");
		String statementDate = accInfo.get(1);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), statementDate);
		
		logger.info("*** Step 3 Expected Result: - The Get Statement date info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Get statement date button should be displayed");
		assertTrue(clientTransactionDetail.getStatementDateInfoBtn().isEnabled(),"        Get statement date button should be enable");
		
		logger.info("*** Step 4 Actions: - Click on Get Statement Date Info button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(),0);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");
		
		logger.info("*** Step 4 Expected Result: - The corresponding data for selected statement date is loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(statementDate, clientID, testDb),"        Data of statement date must be correct");
		
		logger.info("*** Step 5 Actions: - Click on Accession Detail button");
		assertTrue(isElementPresent(clientTransactionDetail.accDetailBtn(), 5),"        Accession Detail button should be displayed");
		clickHiddenPageObject(clientTransactionDetail.accDetailBtn(),0);
		String parentWin = switchToPopupWin();
		
		logger.info("*** Step 5 Expected Result: - List accession detail page show with list out all accession id belong to selected client ID");
		assertTrue(isElementPresent(clientTransactionDetail.clientBillAccTbl(), 5),"       Client Bill Accession table should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.showHideGridIconInAccDetailPopup(), 5),"       Hide icon grid table should be displayed");
		assertTrue(clientTransactionDetail.verifyDataOfClientBillAccCorrespondingStatementDate(statementDate, clientID, testDb),"       List out all accession id belong to selected client ID");
		
		logger.info("*** Step 6 Actions: - Click hide icon on the grid");
		clickHiddenPageObject(clientTransactionDetail.showHideGridIconInAccDetailPopup(), 0);
		
		logger.info("*** Step 6 Expected Result: - The grid is hide");
		assertTrue(isElementHidden(clientTransactionDetail.wrapperTableInAccDetailPopup(), 5),"       Table grid shouldn't be displayed");
		
		Thread.sleep(1000);
		driver.close();
		switchToParentWin(parentWin);
		clickHiddenPageObject(clientTransactionDetail.clientTransactionDetailResetButton(), 0);
		}
	
	@Test(priority = 1, description = "Client Accn Detail - Client Bill Accn - Verify filter in Accn detail page")
	public void testXPR_390() throws Exception {		
		
		logger.info("===== Testing - XPR_390 =====");  
		
		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - The Load Client Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"),"        Page Title should be 'Client - Transaction Detail'");
		
		logger.info("*** Step 2 Actions: - Input valid Client ID. Tab out");
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        Client ID input should be displayed");
		List<String> accInfo = clientTransactionDetail.getClientIdAndStatementHaveBilledAccession(testDb);
		String clientID = accInfo.get(0);
		clientTransactionDetail.checkInputClientId(clientID);
		
		logger.info("*** Step 2 Expected Result: - The Client Transaction Detail page displays");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        Client id label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        Client name label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client Accession Detail - Statement drop down should be displayed");

		logger.info("*** Step 3 Actions: - Select any statement from Client Accession Detail section");
		String statementDate = accInfo.get(1);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), statementDate);
		
		logger.info("*** Step 3 Expected Result: - The Get Statement date info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Get statement date button should be displayed");
		assertTrue(clientTransactionDetail.getStatementDateInfoBtn().isEnabled(),"        Get statement date button should be enable");
		
		logger.info("*** Step 4 Actions: - Click on Get Statement Date Info button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(),0);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 20), "        Loading in Progress should be done after 10 seconds.");
		
		logger.info("*** Step 4 Expected Result: - The corresponding data for selected statement date is loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(statementDate, clientID, testDb),"        Data of statement date must be correct");
		
		logger.info("*** Step 5 Actions: - Click on Accession Detail button");
		assertTrue(isElementPresent(clientTransactionDetail.accDetailBtn(), 5),"        Accession Detail button should be displayed");
		clickHiddenPageObject(clientTransactionDetail.accDetailBtn(),0);
		String parentWin = switchToPopupWin();
		
		logger.info("*** Step 5 Expected Result: - List accession detail page show with list out all accession id belong to selected client ID");
		assertTrue(isElementPresent(clientTransactionDetail.clientBillAccTbl(), 5),"       Client Bill Accession table should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.showHideGridIconInAccDetailPopup(), 5),"       Hide icon grid table should be displayed");
		assertTrue(clientTransactionDetail.verifyDataOfClientBillAccCorrespondingStatementDate(statementDate, clientID, testDb),"       List out all accession id belong to selected client ID");
		
		logger.info("*** Step 6 Actions: - Input value for column to filter");
		List<List<String>> billAcc = daoManagerXifinRpm.getListClientBillAccByClientIDAndStatementDate(clientID, statementDate, testDb);
		assertTrue(isElementPresent(clientTransactionDetail.filterAccIdInputInAccDetailPopup(), 5),"       Accession ID input filter should be displayed");
		testCodeUtils = new TestCodeUtils(driver);
		clientTransactionDetail.setFilterAccIdInputInAccDetailPopup(billAcc.get(0).get(1));
		
		logger.info("*** Step 6 Expected Result: - The grid will be filter base on inputted value");
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(getColumnValue( clientTransactionDetail.clientBillAccTbl(),billAcc.get(0).get(1)),"        Filter must be correct");
		
		Thread.sleep(1000);
		driver.close();
		switchToParentWin(parentWin);
		clickHiddenPageObject(clientTransactionDetail.clientTransactionDetailResetButton(), 0);
		}
	
	@Test(priority = 1, description = "Client Accn Detail - Client Bill Accn - Verify Footer buttons")
	public void testXPR_391() throws Exception {		
		
		logger.info("===== Testing - XPR_391 =====");  
		
		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - The Load Client Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"),"        Page Title should be 'Client - Transaction Detail'");
		
		logger.info("*** Step 2 Actions: - Input valid Client ID. Tab out");
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        Client ID input should be displayed");
		List<String> accInfo = clientTransactionDetail.getClientIdAndStatementHaveMore10BilledAccession(testDb);
		String clientID = accInfo.get(0);
		clientTransactionDetail.setClientIdInput(clientID);
		
		logger.info("*** Step 2 Expected Result: - The Client Transaction Detail page displays");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        Client id label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        Client name label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client Accession Detail - Statement drop down should be displayed");

		logger.info("*** Step 3 Actions: - Select any statement from Client Accession Detail section");
		String statementDate = accInfo.get(1);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), statementDate);
		
		logger.info("*** Step 3 Expected Result: - The Get Statement date info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Get statement date button should be displayed");
		assertTrue(clientTransactionDetail.getStatementDateInfoBtn().isEnabled(),"        Get statement date button should be enable");
		
		logger.info("*** Step 4 Actions: - Click on Get Statement Date Info button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(),0);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");
		
		logger.info("*** Step 4 Expected Result: - The corresponding data for selected statement date is loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(statementDate, clientID, testDb),"        Data of statement date must be correct");
		
		logger.info("*** Step 5 Actions: - Click on Accession Detail button");
		assertTrue(isElementPresent(clientTransactionDetail.accDetailBtn(), 5),"        Accession Detail button should be displayed");
		clickHiddenPageObject(clientTransactionDetail.accDetailBtn(),0);
		String parentWin = switchToPopupWin();
		
		logger.info("*** Step 5 Expected Result: - List accession detail page show with list out all accession id belong to selected client ID");
		assertTrue(isElementPresent(clientTransactionDetail.clientBillAccTbl(), 5),"       Client Bill Accession table should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.showHideGridIconInAccDetailPopup(), 5),"       Hide icon grid table should be displayed");
		assertTrue(clientTransactionDetail.verifyDataOfClientBillAccCorrespondingStatementDate(statementDate, clientID, testDb),"       List out all accession id belong to selected client ID");
		assertTrue(isElementPresent(clientTransactionDetail.nextPageClientBillAccTbl(), 5),"       Next page Bill Accession icon should be displayed");
		
		logger.info("*** Step 6 Actions: - Click next page icon");
		clickHiddenPageObject(clientTransactionDetail.nextPageClientBillAccTbl(), 0);
		
		logger.info("*** Step 6 Expected Result: - Verify next page when user click next page icon");
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.verfiyNavigatePageOfTable(clientTransactionDetail.pageNavigateClientBillAccTbl(), "next"),"       The next page Bill Accession table should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.previousPageClientBillAccTbl(), 5),"       Previous page Bill Accession icon should be displayed");
		
		logger.info("*** Step 7 Actions: - Click previous page icon");
		clickHiddenPageObject(clientTransactionDetail.previousPageClientBillAccTbl(), 0);
		
		logger.info("*** Step 7 Expected Result: - Verify previous page when user click previous page icon");
		assertTrue(clientTransactionDetailUtils.verfiyNavigatePageOfTable(clientTransactionDetail.pageNavigateClientBillAccTbl(), "previous"),"       The previous page Bill Accession table should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.lastPageClientBillAccTbl(), 5),"       Last page Bill Accession icon should be displayed");
		
		logger.info("*** Step 8 Actions: - Click last page icon");
		clickHiddenPageObject(clientTransactionDetail.lastPageClientBillAccTbl(), 0);
		
		logger.info("*** Step 8 Expected Result: - Verify last page when user click last page icon");
		assertTrue(clientTransactionDetailUtils.verfiyNavigatePageOfTable(clientTransactionDetail.pageNavigateClientBillAccTbl(), "last"),"       The last page Bill Accession table should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.firstPageClientBillAccTbl(), 5),"       First page Bill Accession icon should be displayed");
		
		logger.info("*** Step 9 Actions: - Click first page icon");
		clickHiddenPageObject(clientTransactionDetail.firstPageClientBillAccTbl(), 0);
		
		logger.info("*** Step 9 Expected Result: - Verify first page when user click last page icon");
		assertTrue(clientTransactionDetailUtils.verfiyNavigatePageOfTable(clientTransactionDetail.pageNavigateClientBillAccTbl(), "first"),"       The first page Bill Accession table should be displayed");
		
		Thread.sleep(1000);
		driver.close();
		switchToParentWin(parentWin);
		clickHiddenPageObject(clientTransactionDetail.clientTransactionDetailResetButton(), 0);
		}
	
	@Test(priority = 1, description = "Client Accn Detail - Client Bill Accn - Uncheck Check box keep result")
	public void testXPR_392() throws Exception {		
		
		logger.info("===== Testing - XPR_392 =====");  
		
		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - The Load Client Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"),"        Page Title should be 'Client - Transaction Detail'");
		
		logger.info("*** Step 2 Actions: - Input valid Client ID. Tab out");
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        Client ID input should be displayed");
		List<String> accInfo = clientTransactionDetail.getClientIdAndStatementHaveBilledAccession(testDb);
		String clientID = accInfo.get(0);
		clientTransactionDetail.checkInputClientId(clientID);
		
		logger.info("*** Step 2 Expected Result: - The Client Transaction Detail page displays");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        Client id label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        Client name label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client Accession Detail - Statement drop down should be displayed");

		logger.info("*** Step 3 Actions: - Select any statement from Client Accession Detail section");
		String statementDate = accInfo.get(1);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), statementDate);
		
		logger.info("*** Step 3 Expected Result: - The Get Statement date info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Get statement date button should be displayed");
		assertTrue(clientTransactionDetail.getStatementDateInfoBtn().isEnabled(),"        Get statement date button should be enable");
		
		logger.info("*** Step 4 Actions: - Click on Get Statement Date Info button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(),0);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");
		
		logger.info("*** Step 4 Expected Result: - The corresponding data for selected statement date is loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(statementDate, clientID, testDb),"        Data of statement date must be correct");
		
		logger.info("*** Step 5 Actions: - Click on Accession Detail button");
		assertTrue(isElementPresent(clientTransactionDetail.accDetailBtn(), 5),"        Accession Detail button should be displayed");
		clickHiddenPageObject(clientTransactionDetail.accDetailBtn(),0);
		String parentWin = switchToPopupWin();
		
		logger.info("*** Step 5 Expected Result: - List accession detail page show with list out all accession id belong to selected client ID");
		assertTrue(isElementPresent(clientTransactionDetail.clientBillAccTbl(), 5),"       Client Bill Accession table should be displayed");
		assertTrue(clientTransactionDetail.verifyDataOfClientBillAccCorrespondingStatementDate(statementDate, clientID, testDb),"       List out all accession id belong to selected client ID");
		assertTrue(isElementPresent(clientTransactionDetail.keepResultWindowOpenCheckbox(), 5),"       Keep result window check box should be displayed");
		
		logger.info("*** Step 6 Actions: - Uncheck keep result window. Click on Accession ID on table");
		selectCheckBox(clientTransactionDetail.keepResultWindowOpenCheckbox());
		List<List<String>> billAcc = daoManagerXifinRpm.getListClientBillAccByClientIDAndStatementDate(clientID, statementDate, testDb);
		clientTransactionDetail.clickAccessionID(billAcc.get(0).get(1));
		
		logger.info("*** Step 6 Expected Result : - Popup will close and system take use to accession detail page");
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.verfiyWindowIsClosed(),"       Window should be closed");
		switchToParentWin(parentWin);
		Thread.sleep(2000);
		accessionDetail = new AccessionDetail(driver, config, wait);
		assertTrue(isElementPresent(accessionDetail.accnIdText(), 10),"       Accession id text should be displayed");
		assertEquals(billAcc.get(0).get(1), accessionDetail.accnIdText().getAttribute("value"), "       Accession id should be correct");
		
		}

	@Test(priority = 1, description = "Client Accession Detail - Client Bill Accession - Check footer icons")
	public void testXPR_397() throws Exception {
		logger.info("===== Testing - testXPR_397 =====");

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - The Load Client Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"),"        Page Title should be 'Client - Transaction Detail'");

		logger.info("*** Step 2 Actions: - Input valid Client ID. Tab out");
		List<String> accInfo = clientTransactionDetail.getClientIdAndStatementHaveMore10BilledAccession(testDb);
		String clientID = accInfo.get(0);
		String statementDate = accInfo.get(1);
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        Client ID input should be displayed");
		clientTransactionDetail.setClientIdInput(clientID);

		logger.info("*** Step 2 Expected Result: - The Client Transaction Detail page displays");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        Client id label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        Client name label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client Accession Detail - Statement dropdown should be displayed");

		logger.info("*** Step 3 Actions: - Select any statement from Client Accession Detail section");
		testCodeUtils = new TestCodeUtils(driver);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), statementDate);

		logger.info("*** Step 3 Expected Result: - The Get Statement date info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Get statement date button should be displayed");
		assertTrue(clientTransactionDetail.getStatementDateInfoBtn().isEnabled(),"        Get statement date button should be enable");

		logger.info("*** Step 4 Actions: -  Client Accession Detail  : click Get Statement Date Info");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");

		logger.info("*** Step 4 Expected Result: - The corresponding data for selected statement date is loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(statementDate, clientID, testDb),"        Data of statement date must be correct");

		logger.info("*** Step 5 Actions: - Click next page icon");
		clickHiddenPageObject(clientTransactionDetail.nextPageClientBillAccTbl(), 0);

		logger.info("*** Step 5 Expected Result: - Verify next page when user click next page icon");
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.verfiyNavigatePageOfTable(clientTransactionDetail.pageNavigateClientBillAccTbl(), "next"),"       The next page Bill Accession table should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.previousPageStatementIcon(), 5),"       Previous page Bill Accession icon should be displayed");

		logger.info("*** Step 6 Actions: - Click previous page icon");
		clickHiddenPageObject(clientTransactionDetail.previousPageStatementIcon(), 0);

		logger.info("*** Step 6 Expected Result: - Verify previous page when user click previous page icon");
		assertTrue(clientTransactionDetailUtils.verfiyNavigatePageOfTable(clientTransactionDetail.pageNavigateClientBillAccTbl(), "previous"),"       The previous page Bill Accession table should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.lastPageClientBillAccTbl(), 5),"       Last page Bill Accession icon should be displayed");

		logger.info("*** Step 7 Actions: - Click last page icon");
		clickHiddenPageObject(clientTransactionDetail.lastPageClientBillAccTbl(), 0);

		logger.info("*** Step 7 Expected Result: - Verify last page when user click last page icon");
		assertTrue(clientTransactionDetailUtils.verfiyNavigatePageOfTable(clientTransactionDetail.pageNavigateClientBillAccTbl(), "last"),"       The last page Bill Accession table should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.firstPageClientBillAccTbl(), 5),"       First page Bill Accession icon should be displayed");

		logger.info("*** Step 8 Actions: - Click first page icon");
		clickHiddenPageObject(clientTransactionDetail.firstPageClientBillAccTbl(), 0);

		logger.info("*** Step 8 Expected Result: - Verify first page when user click last page icon");
		assertTrue(clientTransactionDetailUtils.verfiyNavigatePageOfTable(clientTransactionDetail.pageNavigateClientBillAccTbl(), "first"),"       The first page Bill Accession table should be displayed");

		Thread.sleep(1000);
		clickHiddenPageObject(clientTransactionDetail.clientTransactionDetailResetButton(), 0);

	}
	@Test(priority = 1, description = "Client Accn Detail - Client Bill Accn - Check ClientID link")
	public void testXPR_393() throws Exception {
		logger.info("===== Testing - testXPR_393 =====");

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - The Load Client Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"),"        Page Title should be 'Client - Transaction Detail'");

		logger.info("*** Step 2 Actions: - Input valid Client ID. Tab out");
		List<String> accInfo = clientTransactionDetail.getClientIdAndStatementHaveBilledAccession(testDb);
        String clientID = accInfo.get(0);
		String statementDate = accInfo.get(1);
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        Client ID input should be displayed");
		clientTransactionDetail.setClientIdInput(clientID);

		logger.info("*** Step 2 Expected Result: - The Client Transaction Detail page displays");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        Client id label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        Client name label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client Accession Detail - Statement dropdown should be displayed");

		logger.info("*** Step 3 Actions: - Select any statement from Client Accession Detail section");
		testCodeUtils = new TestCodeUtils(driver);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), statementDate);

		logger.info("*** Step 3 Expected Result: - The Get Statement date info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Get statement date button should be displayed");
		assertTrue(clientTransactionDetail.getStatementDateInfoBtn().isEnabled(),"        Get statement date button should be enable");

		logger.info("*** Step 4 Actions: -  Client Accession Detail  : click Get Statement Date Info");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");

		logger.info("*** Step 4 Expected Result: - The corresponding data for selected statement date is loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(statementDate, clientID, testDb),"        Data of statement date must be correct");

		logger.info("*** Step 5 Actions: -  Client Billed Accessions  : click Accession Detail button");
		assertTrue(isElementPresent(clientTransactionDetail.accDetailBtn(), 5),"        Accession Detail button should be displayed");
		clickHiddenPageObject(clientTransactionDetail.accDetailBtn(), 0);
		String parentWin = switchToPopupWin();

		logger.info("*** Step 5 Expected Result: - The list of Accession Detail is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.showHideGridIconInAccDetailPopup(), 5),"        The list of Accession Detail should show.");

		logger.info("*** Step 6 Actions: -  Accession Detail page : Click on ClientID link");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLblInAccDetailPopup(), 5),"        Client ID link should be displayed");
		clickHiddenPageObject(clientTransactionDetail.clientIDLblInAccDetailPopup(), 0);
		driver.close();
		switchToParentWin(parentWin);
		Thread.sleep(1000);

		logger.info("*** Step 6 Expected Result: - System will take user to Client - Transaction Detail page");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        Client id label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        Client name label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client Accession Detail - Statement dropdown should be displayed");

		clickHiddenPageObject(clientTransactionDetail.clientTransactionDetailResetButton(), 0);
	}

	@Test(priority = 1, description = "Client Accn Detail - Client Bill Accn - Verify filter function")
	public void testXPR_394() throws Exception {
		logger.info("===== Testing - testXPR_394 =====");

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - The Load Client Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"),"        Page Title should be 'Client - Transaction Detail'");

		logger.info("*** Step 2 Actions: - Input valid Client ID and Tab out");
		List<String> accInfo = clientTransactionDetail.getClientIdAndStatementHaveBilledAccession(testDb);
        String clientID = accInfo.get(0);
		String statementDate = accInfo.get(1);
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        Client ID input should be displayed");
		clientTransactionDetail.setClientIdInput(clientID);

		logger.info("*** Step 2 Expected Result: - The Client Transaction Detail page displays");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        Client id label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        Client name label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client Accession Detail - Statement dropdown should be displayed");

		logger.info("*** Step 3 Actions: - Select any statement from Client Accession Detail section");
		testCodeUtils = new TestCodeUtils(driver);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), statementDate);

		logger.info("*** Step 3 Expected Result: - The Get Statement date info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Get statement date button should be displayed");
		assertTrue(clientTransactionDetail.getStatementDateInfoBtn().isEnabled(),"        Get statement date button should be enable");

		logger.info("*** Step 4 Actions: -  Client Accession Detail  : click Get Statement Date Info");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");

		logger.info("*** Step 4 Expected Result: - The corresponding data for selected statement date is loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(statementDate, clientID, testDb),"        Data of statement date must be correct");
		assertTrue(isElementPresent(clientTransactionDetail.accnIdFilter(), 5),"        Acc ID Filter must be display");
		assertTrue(isElementPresent(clientTransactionDetail.ptNameFilter(), 5),"        Acc Name Filter must be display");

		logger.info("*** Step 5 Actions: -  Client Billed Accessions  : input valid value for all column");

		List<List<String>> listBill = daoManagerXifinRpm.getListClientBillAccByClientIDAndStatementDate(clientID, statementDate, testDb);
		String AccnId = listBill.get(0).get(1);
		String ptName = listBill.get(0).get(3) +", "+listBill.get(0).get(2);

		testCodeUtils = new TestCodeUtils(driver);
		testCodeUtils.scrollIntoView(clientTransactionDetail.ptNameFilter());
		clientTransactionDetail.inputAccnIdFilter(AccnId);
		clientTransactionDetail.inputPtNameFilter(ptName);
		Thread.sleep(1000);

		logger.info("*** Step 5 Expected Result: - The list of Accession Detail is displayed");
		assertTrue(getColumnValue(clientTransactionDetail.clientBillAccTbl(), AccnId), "        AccnId should show.");
		assertTrue(getColumnValue(clientTransactionDetail.clientBillAccTbl(), ptName), "        PtName should show.");

		clickHiddenPageObject(clientTransactionDetail.clientTransactionDetailResetButton(), 0);
	}

	@Test(priority = 1, description = "Client Accn Detail - Client Bill Accn - Verify Hide icon")
	public void testXPR_395() throws Exception {
		logger.info("===== Testing - testXPR_395 =====");

		logger.info("*** Step 1 Actions: - Log into Client Transaction Detail screen with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - The Load Client Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"),"        Page Title should be 'Client - Transaction Detail'");

		logger.info("*** Step 2 Actions: - Enter a valid Client ID. Tab out");
		List<String> accInfo = clientTransactionDetail.getClientIdAndStatementHaveBilledAccession(testDb);
        String clientID = accInfo.get(0);
		String statementDate = accInfo.get(1);
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        Client ID input should be displayed");
		clientTransactionDetail.setClientIdInput(clientID);

		logger.info("*** Step 2 Expected Result: - The Client Transaction Detail page displays");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        Client id label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        Client name label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client Accession Detail - Statement dropdown should be displayed");

		logger.info("*** Step 3 Actions: - Select any statement from Client Accession Detail section");
		testCodeUtils = new TestCodeUtils(driver);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), statementDate);

		logger.info("*** Step 3 Expected Result: - The Get Statement date info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Get statement date button should be displayed");
		assertTrue(clientTransactionDetail.getStatementDateInfoBtn().isEnabled(),"        Get statement date button should be enable");

		logger.info("*** Step 4 Actions: -  Client Accession Detail  : click Get Statement Date Info");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");

		logger.info("*** Step 4 Expected Result: - The corresponding data for selected statement date is loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(statementDate, clientID, testDb),"        Data of statement date must be correct");

		logger.info("*** Step 5 Actions: -  Client Billed Accessions  : Click on Show/Hide icon");
		assertTrue(isElementPresent(clientTransactionDetail.showHideClientBilledAccessionsIcon(), 5),"        Show/Hide icon should be displayed");
		clickHiddenPageObject(clientTransactionDetail.showHideClientBilledAccessionsIcon(), 0);

		logger.info("*** Step 5 Expected Result: - The Client Billed Accessions is hidden and icon is change to show icon ");
		assertEquals(clientTransactionDetail.footerBilledAccession().getCssValue("display"), "none");

		Thread.sleep(1000);
		clientTransactionDetail.clickClientTransactionDetailResetButton();
		}

	@Test(priority = 1, description = "Client Accn Detail - Client Bill Accn - Check sort function")
	public void testXPR_396() throws Exception {
		logger.info("===== Testing - testXPR_396 =====");

		logger.info("*** Step 1 Actions: - Log into Client Transaction Detail screen with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - The Load Client Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"),"        Page Title should be 'Client - Transaction Detail'");

		logger.info("*** Step 2 Actions: - Enter a valid Client ID. Tab out");
		List<String> accInfo = clientTransactionDetail.getClientIdAndStatementHaveBilledAccession(testDb);
        String clientID = accInfo.get(0);
		String statementDate = accInfo.get(1);
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5),"        Client ID input should be displayed");
		clientTransactionDetail.setClientIdInput(clientID);

		logger.info("*** Step 2 Expected Result: - The Client Transaction Detail page displays");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        Client id label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        Client name label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client Accession Detail - Statement dropdown should be displayed");

		logger.info("*** Step 3 Actions: - Select any statement from Client Accession Detail section");
		testCodeUtils = new TestCodeUtils(driver);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), statementDate);

		logger.info("*** Step 3 Expected Result: - The Get Statement date info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Get statement date button should be displayed");
		assertTrue(clientTransactionDetail.getStatementDateInfoBtn().isEnabled(),"        Get statement date button should be enable");

		logger.info("*** Step 4 Actions: -  Client Accession Detail  : click Get Statement Date Info");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");

		logger.info("*** Step 4 Expected Result: - The corresponding data for selected statement date is loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(statementDate, clientID, testDb),"        Data of statement date must be correct");

		logger.info("*** Step 5 Actions: -  Client Billed Accessions grid : Click on Charge Column");
		assertTrue(isElementPresent(clientTransactionDetail.chargeColumnClnBilledAccnTable(), 5),"        Charge column icon should be displayed");
		clickHiddenPageObject(clientTransactionDetail.chargeColumnClnBilledAccnTable(), 0);

		logger.info("*** Step 5 Expected Result: - The Client Billed Accessions grid will be sort as ascending order");
		testCode = new TestCode(driver, config, wait);
		assertTrue(testCode.isSorting(this,clientTransactionDetail.chargeColumnClnBilledAccnTable(),7, "asc"),"       Lab cost should be sorted");

		Thread.sleep(1000);
		clientTransactionDetail.clickClientTransactionDetailResetButton();
		}


	@Test(priority = 1, description = "Load another ClientID in Client-Transaction Detail page")
	public void testXPR_398() throws Exception {
		logger.info("===== Testing - testXPR_398 =====");          

		logger.info("*** Step 1 Actions: - Log to SSO by valid username and password");
		logger.info("*** Step 2 Actions: - Navigate Client Transaction Detail screen");

		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 2 Expected Results: - Load Client - Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client - Transaction Detail page title should be displayed");		
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"), "        Page Title should be 'Client - Transaction Detail'");
		
		logger.info("*** Step 3 Actions: - Enter a valid Client ID");
		String clientID = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		assertTrue(isElementPresent(clientTransactionDetail.clientIdInput(), 5), "        Client ID input field should show.");
		clientTransactionDetail.checkInputClientId(clientID);
		
		logger.info("*** Step 3 Expected Results: - Client - Transaction Detail page is displayed with all Client's information");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        ClientID Label should be displayed.");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        ClientName Label should be displayed.");
		assertTrue(isElementPresent(clientTransactionDetail.accountTypeLabel(), 5),"        AccountType Label should be displayed.");
		
		logger.info("*** Step 4 Actions: - Click on Search ClientID icon");
		assertTrue(isElementPresent(clientTransactionDetail.clientSearchIcon(), 5),"        Client Search icon should be displayed.");
		clickHiddenPageObject(clientTransactionDetail.clientSearchIcon(), 0);
		String parent = switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - The Search Client Page is displayed");
		assertEquals(driver.getTitle(), "Client Search - XIFIN Portal","        Search page should be displayed");
		
		logger.info("*** Step 5 Actions: - Enter a different Client ID");
		clientSearch = new ClientSearch(driver);
		String anotherClientID = clientDao.getClientByMaxEffDt().getClnAbbrev();
		assertTrue(isElementPresent(clientSearch.clientIDInput(), 5), "        ClientID input field should show");
		clientSearch.inputClientID(anotherClientID);
		clientSearch.clickClientSearchSearchButton();
				
		logger.info("*** Step 5 Expected Results: - The Search Result page is displayed with ClientID match with search condition");
		clientSearchResults = new ClientSearchResults(driver);
		assertTrue(isElementPresent(clientSearchResults.clientSearchResultTitle(), 10), "        The Client Search title should be displayed");
		assertEquals(clientSearchResults.clientSearchResultTitle().getText(),"Client Search Results", "        The Client Search page should be displayed");
		
		logger.info("*** Step 6 Actions: - Click on the Client ID hyperlink in the Search Results");
		clientSearchResults.clickClientSearchTblClientID(2, 2);
		
		logger.info("*** Step 6 Expected Results: - The Client - Transaction Detail will be loaded again with newly selected Client ID");
		switchToParentWin(parent);
		assertEquals(driver.getTitle(),"Transaction Detail - XIFIN Portal", "        The Client Transaction Detail page should show.");
		
		clientTransactionDetail.clickClientTransactionDetailResetButton();
		}
	
}	
