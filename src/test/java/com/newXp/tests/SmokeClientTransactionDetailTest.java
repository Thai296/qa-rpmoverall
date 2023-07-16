package com.newXp.tests;

import com.mbasys.mars.ejb.entity.cln.Cln;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.client.clientInquiry.clientTransactionDetail.ClientTransactionDetail;
import com.overall.electronicpayment.ClientTransactionDetailElectronicPayment;
import com.overall.menu.MenuNavigation;
import com.overall.search.ClientSearch;
import com.overall.search.ClientSearchResults;
import com.overall.search.NpiSearch;
import com.overall.search.NpiSearchResults;
import com.overall.utils.ClientTransactionDetailUtils;
import com.overall.utils.TestCodeUtils;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.ConvertUtil;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.*;

public class SmokeClientTransactionDetailTest extends SeleniumBaseTest {

	private TimeStamp timeStamp;
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
	//Search Client
	
	@Test(priority = 1, description = "Search Client - Search Client with valid data for Client Info Section")
	public void testXPR_338() throws Exception {
		logger.info("===== Testing - testXPR_338 =====");
		
		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
		
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 2 Actions: - Click on Search ClientId Icon");
		clientTransactionDetail.clickClientSearchIcon();
		String parent = switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - The Search Client page is displayed");
		clientSearch = new ClientSearch(driver);
		assertTrue(isElementPresent(clientSearch.clientSearchTitle(), 10), "        The Client Search title should be displayed");
		assertEquals(clientSearch.clientSearchTitle().getText(),"Client Search", "        The Client Search page should be displayed");
		
		logger.info("*** Step 3 Actions: - Input valid data for all fields in Client Info section and click on Search button");
		npiSearch = new NpiSearch(driver);
		List<String> procedureCodeTemp = daoManagerXifinRpm.getAllClientInfoByClientId(testDb);
		String ClientID = procedureCodeTemp.get(0);
		String ClientName = procedureCodeTemp.get(1);
		String Facility = procedureCodeTemp.get(2);
		String NpiNumber = procedureCodeTemp.get(3);
		assertTrue(isElementPresent(clientSearch.clientIDInput(), 10), "        Client Id Input field should be displayed");
		clientSearch.inputClientID(ClientID);
		assertTrue(isElementPresent(clientSearch.clientNameInput(), 10), "        Client Name Input field should be displayed");
		clientSearch.inputClientName(ClientName);
		assertTrue(isElementPresent(npiSearch.npiInput(), 10), "        Npi  Input field should be displayed");
		npiSearch.inputNPI(NpiNumber);
		clientSearch.selectPrimaryFacilityDropdown(Facility);
		assertTrue(isElementPresent(clientSearch.clientSearchSearchButton(), 10), "        Client Search button should be displayed");
		clientSearch.clickClientSearchSearchButton();
		
		logger.info("*** Step 3 Expected Results: - Client Info is match with search condition will be displayed");
		clientSearchResults = new ClientSearchResults(driver);
		assertEquals(clientSearchResults.clientSearchResultsTblClientID(2, 2).getText(),ClientID, "        ClientId  must be matched with search condition");
		assertEquals(clientSearchResults.clientSearchResultsCell(2, 3).getText(),ClientName, "        Client Name  must be matched with search condition");
		assertEquals(clientSearchResults.clientSearchResultsCell(2, 13).getText(),Facility, "        Primary Facility  must be matched with search condition");
		clientSearchResults.clickClientSearchResultCloseButton();
		switchToParentWin(parent);
	}

	@Test(priority = 1, description = "Search Client - Search NPI-Search NPI with Entity is Client")
	public void testXPR_348() throws Exception {
		logger.info("===== Testing - testXPR_348 =====");
		
		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 2 Actions: - Click on Search ClientId Icon");
		assertTrue(isElementPresent(clientTransactionDetail.clientSearchIcon(), 5),"        Client Search Icon should be displayed");
		clientTransactionDetail.clickClientSearchIcon();
		String parent = switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - The Search Client page is displayed");
		clientSearch = new ClientSearch(driver);
		assertTrue(isElementPresent(clientSearch.clientSearchTitle(), 10), "        The Client Search title should be displayed");
		assertEquals(clientSearch.clientSearchTitle().getText(),"Client Search", "        Client Search page should be displayed");
		
		logger.info("*** Step 3 Actions: - Click on Search NPI icon in Search Client page");
		npiSearch = new NpiSearch(driver);
		assertTrue(isElementPresent(npiSearch.npiSearchIconBtn(), 5),"        Npi Search icon button should be displayed");
		npiSearch.clickNPISearchIconBtn();
		String parent1 = switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - NPI Search page is displayed");
		assertTrue(isElementPresent(npiSearch.npiTitle(), 5),"        The NPI Search page title should be displayed");
		assertEquals(npiSearch.npiTitle().getText(), "NPI Search","        The NPI Search page is displayed");
		
		logger.info("*** Step 4 Actions: - Input valid NPI and select NPI Entity is Client and click on Search button");
		Cln procedureCodeTemp = clientDao.getAllNpiInfoByNpiNumber();
		String NpiNumber = String.valueOf(procedureCodeTemp.getNpi());
		assertTrue(isElementPresent(npiSearch.npiInput(), 5),"        The NPI input field should be displayed");
		npiSearch.inputNPI(NpiNumber);	
		assertTrue(isElementPresent(npiSearch.npiEntityDropdown(), 5),"        Npi entity Dropdown should be displayed");
		selectItem(npiSearch.npiEntityDropdown(), "Client");
		npiSearch.clickNPISearchButton();
		
		logger.info("*** Step 4 Expected Results: - The NPI is match with search condition is displayed in search result page");
		npiSearchResults = new NpiSearchResults(driver);
		assertEquals(npiSearchResults.npiSearchResultsTblNPINumber(2, 2).getText(),NpiNumber, "        The NPI must be matched with search condition");
		
		npiSearchResults.clickNPISearchResultsCloseButton();
		switchToParentWin(parent1);
		clientSearch.clickClientSearchCloseButton();
		switchToParentWin(parent);
	}
	
	//Statement
	@Test(priority = 1, description = "Statements - Verify filters function in Previous Statement table")
	public void testXPR_356() throws Exception {
		logger.info("===== Testing - testXPR_356 =====");
		
		logger.info("*** Step 1 Actions: - Login to SSO with valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 2 Actions: - Enter a valid Client ID");
		clientSearch = new ClientSearch(driver);
		Cln procedureCodeTemp1 = clientDao.getClientIDHasStatement();
		String ClientID = procedureCodeTemp1.getClnAbbrev();
		clientSearch.inputClientSearchLookupID(ClientID);
		
		logger.info("*** Step 2 Expected Results: - Verify that the Client-Transaction Detail page is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.statementTable(), 5),"        Client - Transaction Detail page should be displayed");

		logger.info("*** Step 3 Actions: - Enter data for filter input fields: Statement Date - Creation Date - Description - File Processed date");
		List<String> procedureCodeTemp = daoManagerXifinRpm.getAllStatementWithFileCreateDtProcessedDt(ClientID , testDb);
		String statementDate = procedureCodeTemp.get(0);
		String creationDate = procedureCodeTemp.get(1);
		String description = procedureCodeTemp.get(2);
		String processDate = procedureCodeTemp.get(3);
		
		logger.info("*** Step 3 Expected Results: - Verify that the filters in Previous Statements table are working properly");
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(isElementPresent(clientTransactionDetail.statementTableStatementDate(), 5),"        Statement Date filter input field should show.");
		clientTransactionDetail.inputStatementTblStatementDate(statementDate);
		assertTrue(getColumnValue(clientTransactionDetail.statementTable(),statementDate),"        Statement Date filter should work properly.");
		
		clientTransactionDetail.statementTableStatementDate().clear();
		assertTrue(isElementPresent(clientTransactionDetail.documentsTableCreationDateInput(), 5),"        Creation Date filter input field should show.");
		clientTransactionDetail.inputDocumentTableCreationDate(creationDate);
		assertTrue(getColumnValue(clientTransactionDetail.statementTable(),creationDate), "        Creation Date filter should work properly.");
		
		clientTransactionDetail.documentsTableCreationDateInput().clear();
		assertTrue(isElementPresent(clientTransactionDetail.documentsTableDescriptionInput(), 5),"        Description filter input field should show.");
		clientTransactionDetail.inputDocumentTableDescription(description);
		assertTrue(getColumnValue(clientTransactionDetail.statementTable(),description), "        Description filter should work properly.");
		
		clientTransactionDetail.documentsTableDescriptionInput().clear();
		assertTrue(isElementPresent(clientTransactionDetail.documentsTableProcessedDate(), 5),"        Processed Date filter input field should be displayed");
		clientTransactionDetail.inputDocumentTableProcessedDate(processDate);
		assertTrue(getColumnValue(clientTransactionDetail.statementTable(),processDate), "        Processed Date filter should work properly.");

		clientTransactionDetail.clickClientTransactionDetailResetButton();
	}
	
	@Test(priority = 1, description = "Statements - View Statement By Excel format")
	public void testXPR_358() throws Exception {
		logger.info("===== Testing - testXPR_358 =====");          

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - The Client - Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client - Transaction Detail page title should be displayed");		
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"), "        Page Title should be 'Client - Transaction Detail'");
		
		logger.info("*** Step 2 Actions: - Input Valid Client ID");
		String clientID = clientDao.getClientIdWithEnableExcelIcon().getClnAbbrev();
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
		clientTransactionDetail.setClientIdInput(clientID);
		
		logger.info("*** Step 2 Expected Results: - The Client - Transaction Detail page is displayed with all information of this client");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        ClientID Label should be displayed.");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        ClientName Label should be displayed.");
		assertTrue(isElementPresent(clientTransactionDetail.accountTypeLabel(), 5),"        AccountType Label should be displayed.");
				
		logger.info("*** Step 3 Actions: Statement section: Click on Excel icon");
		String winHandler = driver.getWindowHandle();
		clientTransactionDetail.clickIconViewExcel();
		
		logger.info("*** Step 3 Expected Results: - The Statement for the Client will be viewed in Excel format");
		switchToPopupWin();
		assertEquals(driver.getCurrentUrl().split("=")[1], "VIEWSTMT&isSuppress", "        Excel file is not opened.");
		//Need to be updated later ...

		driver.close();
		switchToWin(winHandler);	
		switchToDefaultWinFromFrame();	
		
		clientTransactionDetail.clickClientTransactionDetailResetButton();
	}
	
	@Test(priority = 1, description = "Statements - View Statement By PDF format")
	public void testXPR_359() throws Exception {
		logger.info("===== Testing - testXPR_359 =====");          

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - The Client - Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client - Transaction Detail page title should be displayed");		
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"), "        Page Title should be 'Client - Transaction Detail'");
		
		logger.info("*** Step 2 Actions: - Enter a Valid Client ID");
		String clientID = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
		clientTransactionDetail.checkInputClientId(clientID);
		
		logger.info("*** Step 2 Expected Results: - The Client - Transaction Detail page is displayed with all information of this client");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        ClientID Label should be displayed.");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        ClientName Label should be displayed.");
		assertTrue(isElementPresent(clientTransactionDetail.accountTypeLabel(), 5),"        AccountType Label should be displayed.");
		
		logger.info("*** Step 3 Actions: Statement section: Click on PDF icon");
		String winHandler = driver.getWindowHandle();//yli
		clientTransactionDetail.clickIconViewPDF();
		
		logger.info("*** Step 3 Expected Results: - The Statement for the Client will be viewed in PDF format");
		switchToPopupWin();//yli
		assertTrue(driver.getCurrentUrl().contains("VIEWSTMT"), "        PDF file should be opened.");
		
		driver.close();
		switchToWin(winHandler);//yli
		switchToDefaultWinFromFrame();
		clientTransactionDetail.clickClientTransactionDetailResetButton();
	}

	// Client Account Aging Balance
	@Test(priority = 1, description = "Client Account Aging Balance - Verify data")
	public void testXPR_354() throws Exception {
		logger.info("===== Testing - testXPR_354 =====");    	

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - Verify that user is logged into the correct customer - Load Client - Transaction Detail page is displayed ");		
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 2 Actions: - Enter a valid Client ID and tab Out");
		String clientId = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();		
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
		clientTransactionDetail.setClientIdInput(clientId);

		logger.info("*** Step 2 Expected Results: - Client - Transaction Detail page is displayed ");
		assertTrue(isElementPresent(clientTransactionDetail.lastPaymentDateInput(), 5),"        Client - Transaction Detail page should be displayed");

		logger.info("*** Step 3 Actions: - Verify Data in Client Account Aging Balance Section");
		assertTrue(isElementPresent(clientTransactionDetail.clientAABTable(), 5),"        Client - Transaction Detail page should be displayed");

		logger.info("*** Step 3 Expected Results: - Verify that the Client Account Aging Balances for Balance, Current Month, 30 Days, 60 Days and 90 Days are displaying properly.");
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);

		//Get the Client Aging Balance
		String balanceAmount = String.valueOf(clientDao.getClnAgingBalFromCLNBALHISTByClnId(clientId));
		
		//Get the Client Aging Current Month Balance
		Date accountingPeriodEndDt = new SimpleDateFormat("MM/dd/yyy").parse(systemDao.getSystemSetting(71).getDataValue());
		timeStamp = new TimeStamp(driver);
		String firstDtOfMonth = timeStamp.getFirstDateOfMonth("MM/dd/yyyy", accountingPeriodEndDt);		
		String currentMonth  = daoManagerPlatform.getClnAgingCurrBalFromCLNBALHISTByClnIdDate(clientId, firstDtOfMonth, testDb);
		
		//Get the Client Aging 30 Days Balance		
		Date prevMonthDate = new SimpleDateFormat("MM/dd/yyy").parse(timeStamp.getPreviousMonth("MM/dd/yyyy", accountingPeriodEndDt, 1));		
		firstDtOfMonth = timeStamp.getFirstDateOfMonth("MM/dd/yyyy", prevMonthDate);
		String lastDtOfMonth = timeStamp.getLastDateOfMonth("MM/dd/yyyy", prevMonthDate);
		String getClientAging30Days = daoManagerPlatform.getClnAgingBalFromCLNBALHISTByClnIdStartEndDate(clientId, firstDtOfMonth, lastDtOfMonth, testDb);
		
		//Get the Client Aging 60 Days Balance		
		prevMonthDate = new SimpleDateFormat("MM/dd/yyy").parse(timeStamp.getPreviousMonth("MM/dd/yyyy", accountingPeriodEndDt, 2));		
		firstDtOfMonth = timeStamp.getFirstDateOfMonth("MM/dd/yyyy", prevMonthDate);
		lastDtOfMonth = timeStamp.getLastDateOfMonth("MM/dd/yyyy", prevMonthDate);
		String getClientAging60Days = daoManagerPlatform.getClnAgingBalFromCLNBALHISTByClnIdStartEndDate(clientId, firstDtOfMonth, lastDtOfMonth, testDb);
		
		//Get the Client Aging 90 Days Balance		
		prevMonthDate = new SimpleDateFormat("MM/dd/yyy").parse(timeStamp.getPreviousMonth("MM/dd/yyyy", accountingPeriodEndDt, 3));		
		firstDtOfMonth = timeStamp.getFirstDateOfMonth("MM/dd/yyyy", prevMonthDate);
		lastDtOfMonth = timeStamp.getLastDateOfMonth("MM/dd/yyyy", prevMonthDate);
		String getClientAging90Days = daoManagerPlatform.getClnAgingBalFromCLNBALHISTByClnIdStartEndDate(clientId, firstDtOfMonth, lastDtOfMonth, testDb);
		
		String balAmt = clientTransactionDetailUtils.formatDollarAmount(Double.parseDouble(balanceAmount));		
		String currMonthAmt = clientTransactionDetailUtils.formatDollarAmount(Double.parseDouble(currentMonth));		
		String clnAging30DaysAmt = clientTransactionDetailUtils.formatDollarAmount(Double.parseDouble(getClientAging30Days));
		String clnAging60DaysAmt = clientTransactionDetailUtils.formatDollarAmount(Double.parseDouble(getClientAging60Days));
		String clnAging90DaysAmt = clientTransactionDetailUtils.formatDollarAmount(Double.parseDouble(getClientAging90Days));
		
		assertTrue(getColumnValue(clientTransactionDetail.clientAABTable(), balAmt),"        Client Account Aging Balance Amount should be " + balAmt);
		assertTrue(getColumnValue(clientTransactionDetail.clientAABTable(), currMonthAmt),"        Client Account Aging Current Month Amount should be " + currMonthAmt);
		assertTrue(getColumnValue(clientTransactionDetail.clientAABTable(), clnAging30DaysAmt),"        Client Account Aging 30 Days Amount should be " + clnAging30DaysAmt);
		assertTrue(getColumnValue(clientTransactionDetail.clientAABTable(), clnAging60DaysAmt),"        Client Account Aging 60 Days Amount should be " + clnAging60DaysAmt);
		assertTrue(getColumnValue(clientTransactionDetail.clientAABTable(), clnAging90DaysAmt),"        Client Account Aging 90 Days Amount should be " + clnAging90DaysAmt);
		
		clientTransactionDetail.clickClientTransactionDetailResetButton();
	}

	/*
	//client payment - Client Accn Detail
	@Test(priority = 1, description = "Client Accn Detail-Create new statement date with empty data")
	@Parameters({"email", "password"})
	public void testXPR_362(String email, String password) throws Exception {		
		logger.info("===== Testing - XPR_362 =====");    	

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new Navigation(driver);
		navigation.navigateToClientTransactionDetailPage();

		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		

		logger.info("*** Step 1 Expected Results: - The Load Client Transaction page is displayed");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
		
		logger.info("*** Step 2 Action: - Enter valid Client ID and tab out");
		String clnId = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		clientTransactionDetail.setClientIdInput(clnId);
		
		logger.info("*** Step 2 Expected result: - Verify that the Client Transaction page is display with correct clientId and client name");
		assertTrue(isElementPresent(clientTransactionDetail.clientTransactionDetailSaveButton(), 5));
		assertTrue(clientTransactionDetail.clientIDLabel().getText().trim().contains(clnId), "        The Client Transaction Detail page is displayed correct clientID'");
		
		logger.info("*** Step 3 Action: - Click on Pencil icon at Client Accession Detail section");
		assertTrue(isElementPresent(clientTransactionDetail.createNewStatementDateIcon(), 5));
		int x = clientTransactionDetailUtils.getLeftSide(clientTransactionDetail.createNewStatementDateIcon());
		int y = clientTransactionDetailUtils.getTopSide(clientTransactionDetail.createNewStatementDateIcon());
		scrollToElement(x, y - 150);
		clientTransactionDetail.clickCreateNewStatementDateIcon();
		
		logger.info("*** Step 3 Expect result: Statement date is displayed as text box");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateInput(), 5), "        The Statement date is displayed as text box");
		
		logger.info("*** Step 4 Action: Input valid statement date");
		timeStamp = new TimeStamp(driver);
		//Infonam//String statementDate = timeStamp.getCurrentDate("MM/DD/YYYY");
		String statementDate = timeStamp.getCurrentDate();
		//
		//ArrayList<String> clnSubmList = daoManagerPlatform.getClnSubmInfoFromCLNSUBMByClnAbbrev(clnId, testDb);
		//String statementDate = clnSubmList.get(1);
		clientTransactionDetail.inputStatementDate(statementDate);
		
		logger.info("*** Step 4 Expected result: The Get Statement Date Info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.clientTransactionDetailSaveButton(), 5));
		assertTrue(isElementEnabled(clientTransactionDetail.getStatementDateInfoBtn(), 5, true), "        The Statement Date Info button is enable");
		
		logger.info("*** Step 5 Action: Click on Get Statement Date Info button");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5));
		clientTransactionDetail.clickGetStatementDateInfoBtn();
		
		logger.info("*** Step 5 expect result: The reconciled PDF Statement button is enable");
		assertTrue(isElementEnabled(clientTransactionDetail.reconciledPDFStatementBtn(), 5, true),"        The Reconciled PDF Statement button is enable");	
		
		clientTransactionDetail.clickClientTransactionDetailResetButton();
		closeAlertAndGetItsText(true);
		driver.close();
	}	
	*/
	
	@Test(priority = 1, description="Client Accn Detail-Statement date-Create Client Payment with valid input")
	@Parameters({"paymentAmount"})
	public void testXPR_363(String paymentAmount) throws Exception{
		logger.info("===== Testing - XPR_363 =====");
		
		logger.info("*** Step 1 Actions: login to Mars with valid username and password and click on Transaction Detail link");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected result: the Load Client - Transaction Detail page is displayed ");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
		
		logger.info("*** Step 2 Actions: login with valid ClientID");
		String clientId = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		clientTransactionDetail.setClientIdInput(clientId);
		
		logger.info("*** Step 2 Expected result: the Client - Transaction Detail page is displayed with valid ClientId");
		assertTrue(isElementPresent(clientTransactionDetail.clientTransactionDetailSaveButton(), 5));
		assertTrue(clientTransactionDetail.clientIDLabel().getText().trim().equalsIgnoreCase(clientId),"       The Client Transaction detail page is displayed with correct client id");
		
		logger.info("*** Step 3 Action: Select a statement date with valid data");
		testCodeUtils = new TestCodeUtils(driver);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		String statementDate = daoManagerPlatform.getClnSubmInfoFromCLNSUBMByClnAbbrev(clientId, testDb).get(1);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), statementDate);
		
		logger.info("*** Step 3 Expected result: the Get Statement Date Info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5));
		assertTrue(isElementEnabled(clientTransactionDetail.getStatementDateInfoBtn(), 5, true),"       The Get Statement Date Info button is enable when user input valid statement date");
		
		logger.info("*** Step 4 Action: click on Get Statement Date info button");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5));
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");
		
		logger.info("*** Step 4 Expected result: The Reconciled PDF Statement and Save button are enabled");		
		assertTrue(isElementEnabled(clientTransactionDetail.clientTransactionDetailSaveButton(), 5, true), "        The Save button is enabled");
		
		logger.info("*** Step 5 Action: click on Add icon in Client Payment section");
		assertTrue(isElementPresent(clientTransactionDetail.addNewClientPaymentBtn(), 5));
		clickHiddenPageObject(clientTransactionDetail.addNewClientPaymentBtn(), 0);
		
		logger.info("*** Step 5 Expected Results: Verify that the Electronic Payment Details screen displayed");		
		ClientTransactionDetailElectronicPayment clientTransactionDetailElectronicPayment = new ClientTransactionDetailElectronicPayment(driver);
		wait.until(ExpectedConditions.visibilityOf(clientTransactionDetailElectronicPayment.paymentAmountInput()));
		
		logger.info("*** Step 6 Action: Enter Payment Amount");
		clientTransactionDetailElectronicPayment.setPaymentAmount(paymentAmount);			

		logger.info("*** Step 6 Expected Results: Verify that the Invoice Payment column will be auto-filled with matched Payment Amount");			
		String totalInvoice = String.valueOf(clientTransactionDetail.totalInvoicePayment(clientTransactionDetail.listElementInvoicePayment()));	
		assertEquals(paymentAmount, totalInvoice,"        The Invoice Payment Distribution column should be auto-filled with Payment Amount " + paymentAmount);
		
		logger.info("*** Step 7 Action: Cancel the Payment and Reset the page");		
		clientTransactionDetailElectronicPayment.clickCancelInPayment();
		clientTransactionDetail.clickClientTransactionDetailResetButton();
	}
	
	@Test(priority = 1, description="Client Accn Detail-Client Payment-Cancel New payment")
	@Parameters({"cardNumber", "expirationMonth", "expirationYear", "paymentAmount"})
	public void testXPR_369( String cardNumber, String expirationMonth, String expirationYear, String paymentAmount) throws Exception{
		
		logger.info("===== Testing - XPR_369 =====");

		logger.info("*** Step 1 action: login to Mars with valid username and password and navigate to Client tab");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();
		
		logger.info("Step 1 Expected result: system take user to Load Client transaction page");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
		
		logger.info("*** Step 2 Action: input valid ClientID and tab out" );
		String clientId = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		clientTransactionDetail.setClientIdInput(clientId);
		
		logger.info("*** Step 2 Expected result: the Client - Transaction Detail is displayed with correct information");
		assertTrue(isElementPresent(clientTransactionDetail.clientTransactionDetailSaveButton(), 10));
		assertTrue(clientTransactionDetail.clientIDLabel().getText().trim().equalsIgnoreCase(clientId), "         The Load Client Transaction detail is load correct Client ID");
		
		logger.info("*** Step 3 Action: Select any statement date in Accession Detail section");
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		testCodeUtils = new TestCodeUtils(driver);

		Date statementDate = clientDao.getClnSubmWithAmtFromCLNSUBMByClnAbbrev(clientId).getSubmDt();
		String date = ConvertUtil.getDateAsTring(statementDate, "MM/dd/yyyy", "");
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), date);
		
		logger.info("*** Step 3 Expected result: The Get Statement Date Info button is enable");
		assertTrue(isElementEnabled(clientTransactionDetail.getStatementDateInfoBtn(), 5, true), "         The Get Statement Date Info button is enable");
		
		logger.info("*** Step 4 Action: Click on Get Statement Date Info button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");
		
		logger.info("*** Step 4 Expected result: the Reconciled PDF Statement button and Save button are enable");
		assertTrue(isElementEnabled(clientTransactionDetail.reconciledPDFStatementBtn(), 5, true),"        THe Reconciled PDF Statement button is enable");
		assertTrue(isElementEnabled(clientTransactionDetail.clientTransactionDetailSaveButton(), 5, true),"        THe Save button is enable");
		
		logger.info("*** Step 5 Action: click on Add icon at Client Payment section");
		assertTrue(isElementPresent(clientTransactionDetail.addNewClientPaymentBtn(), 5));
		clickHiddenPageObject(clientTransactionDetail.addNewClientPaymentBtn(), 0);
		
		logger.info("*** Step 5 Expected result: the Add Client Payment popup is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.paymentMethodDropdown(), 5), "       The Electronic Payment Detail & Cardholder information popup is displayed");
		
		logger.info("*** Step 6 Actions: Enter payment amount and Credit Card information and click on Cancel button");
		ClientTransactionDetailElectronicPayment clientTransactionDetailElectronicPayment = new ClientTransactionDetailElectronicPayment(driver);
		randomCharacter = new RandomCharacter(driver);
		String firstName = randomCharacter.getRandomAlphaString(10);
		String lastName = randomCharacter.getRandomAlphaString(10);
		String streetAdress = randomCharacter.getRandomNumericString(3) + " " + randomCharacter.getRandomAlphaString(10);
		String zipCode = randomCharacter.getRandomNumericString(5);
		clientTransactionDetailElectronicPayment.cancelElectronicPayment(this, cardNumber, expirationMonth, expirationYear, paymentAmount, wait, "123", firstName, lastName, streetAdress, zipCode);
		
		logger.info("*** step 6 Expected Results: - Verify that no new Electronic Payment is added to the Client Payment table");
		assertFalse(getColumnValue(clientTransactionDetail.clientPaymentTable(), paymentAmount), "       New Client Payment is not added into the client payment table");
		
		logger.info("*** Step 7 Actions: - Reset the screen");
		clientTransactionDetail.clickClientTransactionDetailResetButton();				
	}

	@Test(priority = 1, description = "Client Accn Detail-Client payment-Reset new payment")
	@Parameters({"cardNumber","expirationMonth", "expirationYear", "paymentAmount"})
	public void testXPR_370( String cardNumber, String expirationMonth, String expirationYear, String paymentAmount) throws Exception{
		logger.info("===== Testing - XPR_370 =====");
		
		logger.info("*** Step 1 Actions: - Log into XifinPortal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - Verify that user is logged into the correct customer - Load Client - Transaction Detail page is displayed ");		
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 2 Actions: - Enter a valid Client ID and Tab Out");
		Cln listClientInfo = clientDao.getListClientIdHaveClientPayment();
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
		String clnAbbrev = listClientInfo.getClnAbbrev();
		clientTransactionDetail.checkInputClientId(clnAbbrev);

		logger.info("*** Step 2 Expected Results: - Client - Transaction Detail page is displayed ");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client - Transaction Detail page should be displayed.");
		
		logger.info("*** Step 3 Actions: - Client Accession Detail Section : select a Date in the Statement Date Dropdown list and Tab out");
		testCodeUtils = new TestCodeUtils(driver);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Statement Date Dropdown should show.");

		//Select a Statement Date that has a positive Amount Due
		ArrayList<String> clnBalInfoList = daoManagerPlatform.getDueAmtFromCLNBALHISTByClnId(clnAbbrev, testDb);
		String stmtDt = clnBalInfoList.get(0);
		selectItem(clientTransactionDetail.statementDateDropdown(), stmtDt);
		
		logger.info("*** Step 3 Expected Results: - Get Statement Date info button is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Statement Date Button should show.");
		assertNull(clientTransactionDetail.getStatementDateInfoBtn().getAttribute("disabled"), "        Get Statement Date info button should be displayed.");
		
		logger.info("*** Step 4 Actions: Click on Statement Date info Button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");
		
		logger.info("*** Step 4 Expected Results: The corresponding data for the selected Statement Date will be loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(stmtDt, listClientInfo.getClnAbbrev(), testDb),"        The corresponding data for the selected Statement Date " + stmtDt + " should display properly.");
				
		logger.info("*** Step 5 Actions: - Click Add button in Client Payments grid");
		clientTransactionDetail.clickAddBtnInClnPmtGrid();

		logger.info("*** Actions: - Add Electronic Payment");
		ClientTransactionDetailElectronicPayment clientTransactionDetailElectronicPayment = new ClientTransactionDetailElectronicPayment(driver);
		randomCharacter = new RandomCharacter(driver);
		String comments = randomCharacter.getRandomAlphaNumericString(10);
		clientTransactionDetailElectronicPayment.setElectronicPayment(this, cardNumber, expirationMonth, expirationYear, "123", paymentAmount, comments, wait);
		
		logger.info("*** Actions: - Click Reset button in Client Transaction Detail page");
		clientTransactionDetail.clickClientTransactionDetailResetButton();
		
		logger.info("*** Expected Results: - Verify that it is back to the Load Client page");
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
			
		logger.info("*** Step 18 Actions: - Reload the same Client ID");
		clientTransactionDetail.checkInputClientId(clnAbbrev);

		logger.info("*** Step 18 Expected Results: - Client - Transaction Detail page is displayed ");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client - Transaction Detail page should be displayed.");
		
		logger.info("*** Step 19 Actions: - Select the same Statement Date");
		selectItem(clientTransactionDetail.statementDateDropdown(), stmtDt);
		
		logger.info("*** Step 19 Expected Results: - Get Statement Date info button is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Statement Date Button should show.");
		assertNull(clientTransactionDetail.getStatementDateInfoBtn().getAttribute("disabled"), "        Get Statement Date info button should be displayed.");
		
		logger.info("*** Step 20 Actions: - Click on Statement Date info Button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");
		
		logger.info("*** Step 20 Expected Results: - The corresponding data for the selected Statement Date will be loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(stmtDt, listClientInfo.getClnAbbrev(), testDb),"        The corresponding data for the selected Statement Date: " + stmtDt + " should display properly.");
		
		logger.info("*** Step 20 Expected Results: - Client Payments table should show");
		assertTrue(clientTransactionDetail.clnPmtTable().isDisplayed(), "        Client Payments table should show.");
		
		logger.info("*** Step 21 Actions: - Enter the Comments added to the Credit payment in the Comments filter and tab out");		
		clientTransactionDetail.enterClnPmtNotesFilter(comments);
		
		logger.info("*** Step 21 Expected Results: - Verify that the Credit payment is saved and showing in the Client Payments grid");
		assertTrue(getColumnValue(clientTransactionDetail.clientPaymentTable(), comments),"      The Credit client payment should show in the Client Payments grid.");
		
		clientTransactionDetail.clickClientTransactionDetailResetButton();
	}

	@Test(priority = 1, description = "Client Accn Detail-Client Payment-View client Payment")
	public void testXPR_371() throws Exception {
		logger.info("===== Testing - testXPR_371 =====");    	

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - Verify that user is logged into the correct customer - Load Client - Transaction Detail page is displayed ");		
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 2 Actions: - Enter a valid Client ID and Tab Out");
		List<String> listClientInfo = daoManagerXifinRpm.getListClientIdHaveClientPayment(testDb);
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
		clientTransactionDetail.checkInputClientId(listClientInfo.get(0));

		logger.info("*** Step 2 Expected Results: - Client - Transaction Detail page is displayed ");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client - Transaction Detail page should be displayed");
		
		logger.info("*** Step 3 Actions: - Client Accession Detail Section : select a statement date in Dropdown list and Tab out");
		testCodeUtils = new TestCodeUtils(driver);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Statement Date Dropdown should show.");
		selectItem(clientTransactionDetail.statementDateDropdown(), listClientInfo.get(1));
		
		logger.info("*** Step 3 Expected Results: - Get Statement Date info button is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Get Statement Date info Button should show.");

		logger.info("*** Step 4 Actions: Click on Statement Date info Button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		Thread.sleep(5000);
		
		logger.info("*** Step 4 Expected Results: The corresponding data for selected statement date will be load ");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(listClientInfo.get(1), listClientInfo.get(0), testDb),"        Data for the selected Statement Date should be displaying properly.");
		
		logger.info("*** Step 5 Actions : Client Payment table - click CC Payment row");
		clientTransactionDetail.setCCTypeFilterInput("visa");
		Thread.sleep(2000);
		clickHiddenPageObject(clientTransactionDetail.ccTypeIconBtn(),0);
		
		logger.info("*** Step 5 Expected Results: The Electronic Payment Details & Cardholder Information popup is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.creditElectronicPaymentDetails(), 5),"        Credit button should show.");
		assertTrue(isElementPresent(clientTransactionDetail.voidElectronicPaymentDetails(), 5),"        Void button should show.");
		assertTrue(isElementPresent(clientTransactionDetail.cancelClientPaymentBtn(), 5),"        Cancel button should show.");
		
		logger.info("*** Step 6 Actions : Click Cancel button in the Electronic Payment Details & Cardholder Information popup window");
		clientTransactionDetail.clickCancelClientPaymentBtn();
		
		logger.info("*** Step 6 Expected Results: The Electronic Payment Details & Cardholder Information popup is Closed");
		assertEquals(clientTransactionDetail.addNewClientPaymentPopup().getAttribute("aria-hidden"),"true","        The Electronic Payment Details & Cardholder Information popup is Closed.");
		
		clientTransactionDetail.clickClientTransactionDetailResetButton();
	}

	@Test(priority = 1, description = "Client Payment-credit payment with valid data")
	@Parameters({"cardNumber","expirationMonth", "expirationYear", "paymentAmount"})
	public void testXPR_407(String cardNumber, String expirationMonth, String expirationYear, String paymentAmount) throws Exception {
		logger.info("===== Testing - testXPR_407 =====");    	

		logger.info("*** Step 1 Actions: - Log into XifinPortal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - Verify that user is logged into the correct customer - Load Client - Transaction Detail page is displayed ");		
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 2 Actions: - Enter a valid Client ID and Tab Out");
		Cln listClientInfo = clientDao.getListClientIdHaveClientPayment();
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
		String clnAbbrev = listClientInfo.getClnAbbrev();
		clientTransactionDetail.checkInputClientId(clnAbbrev);

		logger.info("*** Step 2 Expected Results: - Client - Transaction Detail page is displayed ");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client - Transaction Detail page should be displayed.");
		
		logger.info("*** Step 3 Actions: - Client Accession Detail Section : select a Date in the Statement Date Dropdown list and Tab out");
		testCodeUtils = new TestCodeUtils(driver);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Statement Date Dropdown should show.");

		//Select a Statement Date that has a positive Amount Due
		ArrayList<String> clnBalInfoList = daoManagerPlatform.getDueAmtFromCLNBALHISTByClnId(clnAbbrev, testDb);
		String stmtDt = clnBalInfoList.get(0);
		selectItem(clientTransactionDetail.statementDateDropdown(), stmtDt);
		
		logger.info("*** Step 3 Expected Results: - Get Statement Date info button is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Statement Date Button should show.");
		assertNull(clientTransactionDetail.getStatementDateInfoBtn().getAttribute("disabled"), "        Get Statement Date info button should be displayed.");
		
		logger.info("*** Step 4 Actions: Click on Statement Date info Button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");
		
		logger.info("*** Step 4 Expected Results: The corresponding data for the selected Statement Date will be loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(stmtDt, listClientInfo.getClnAbbrev(), testDb),"        The corresponding data for the selected Statement Date " + stmtDt + " should display properly.");
				
		logger.info("*** Step 5 Actions: - Click Add button in Client Payments grid");
		clientTransactionDetail.clickAddBtnInClnPmtGrid();
		
		logger.info("*** Actions: - Add Electronic Payment");
		randomCharacter = new RandomCharacter(driver);
		String comments = randomCharacter.getRandomAlphaNumericString(10);
		ClientTransactionDetailElectronicPayment clientTransactionDetailElectronicPayment = new ClientTransactionDetailElectronicPayment(driver);
		clientTransactionDetailElectronicPayment.setElectronicPayment(this, cardNumber, expirationMonth, expirationYear, "123", paymentAmount, comments, wait);
		
		logger.info("*** Step 8 Actions: - Click Save button");
		clientTransactionDetail.clickClientTransactionDetailSaveButton();		
		
		logger.info("*** Step 8 Expected Results: - Verify that it is back to transaction detail page");
		Thread.sleep(2000);
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 9 Actions: - Reload the same Client ID");
		Thread.sleep(2000);
		clientTransactionDetail.checkInputClientId(clnAbbrev);

		logger.info("*** Step 9 Expected Results: - Client - Transaction Detail page is displayed ");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client - Transaction Detail page should be displayed.");
		
		logger.info("*** Step 10 Actions: - Select the same Statement Date");
		selectItem(clientTransactionDetail.statementDateDropdown(), stmtDt);
		
		logger.info("*** Step 10 Expected Results: - Get Statement Date info button is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Statement Date Button should show.");
		assertNull(clientTransactionDetail.getStatementDateInfoBtn().getAttribute("disabled"), "        Get Statement Date info button should be displayed.");
		
		logger.info("*** Step 11 Actions: - Click on Statement Date info Button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");
		
		logger.info("*** Step 11 Expected Results: - The corresponding data for the selected Statement Date will be loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(stmtDt, listClientInfo.getClnAbbrev(), testDb),"        The corresponding data for the selected Statement Date: " + stmtDt + " should display properly.");

		logger.info("*** Step 11 Expected Results: - Client Payments table should show");
		assertTrue(isElementPresent(clientTransactionDetail.clnPmtTable(),5), "        Client Payments table should show.");

		logger.info("*** Step 12 Actions: - Select the electronic payment just added");		
		clientTransactionDetail.enterClnPmtNotesFilter(comments);
		Thread.sleep(3000);		
		clientTransactionDetail.clickCCTypeIconBtn();

		logger.info("*** Step 12 Expected Results: - The Electronic Payment Details & Cardholder Information popup is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.creditElectronicPaymentDetails(), 5),"        Credit button  should show");
		assertTrue(isElementPresent(clientTransactionDetail.voidElectronicPaymentDetails(), 5),"        Void button  should show");
		assertTrue(isElementPresent(clientTransactionDetail.cancelClientPaymentBtn(), 5),"        Cancel button  should show");

		logger.info("*** Step 13 Actions: - Click Credit button");
		clientTransactionDetail.clickCreditButton();

		logger.info("*** Step 13 Expected Results: - Payment amount is enabled to update");
		assertNull(clientTransactionDetail.amountInput().getAttribute("disabled"),"        Payment amount should be enabled to update.");

		logger.info("*** Step 14 Actions: - Enter a Payment amount that is less than the current payment amount and tab out");
		String newPaymentAmount = "5.0";
		clientTransactionDetail.inputPaymentAmount(newPaymentAmount);
		comments = randomCharacter.getRandomAlphaNumericString(10);
		clientTransactionDetail.inputComment(comments);
		
		logger.info("*** Step 14 Expected result: - Distribution table will be updated based on new Credit payment amount");
		String totalInvoice = String.valueOf(clientTransactionDetail.totalInvoicePayment(clientTransactionDetail.listElementEditInvoicePayment()));
		assertEquals(newPaymentAmount, totalInvoice,"        Distribution table should be updated based on new Credit payment amount.");
		Thread.sleep(3000);
		logger.info("*** Step 15 Actions : Click on Continue button ");
		assertTrue(isElementPresent(clientTransactionDetail.continueButton(), 5), "         The Continue button should show.");
		clientTransactionDetail.clickContinueButton();
		Thread.sleep(6000);
		logger.info("*** Step 15 Expected result: The Confirm transaction CREDIT popup is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.confirmTransactionBtn(), 5), "        The confirmation transaction popup should show.");
		
		logger.info("*** Step 16 Actions : Click on Confirm Transaction button ");
		clientTransactionDetail.clickConfirmTransactionBtn();
		//Wait up to 40 seconds until the Processing is done
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.processingMsgText(), 40), "        Processing should be done after 40 seconds.");
						
		logger.info("*** Step 16 Expected Results: - Verify that Transaction was approved and click OK button");			
		assertTrue(isElementPresent(clientTransactionDetail.okTransactionProcessBtn(), 5), "         The transaction processing popup should show.");
		clientTransactionDetail.clickOkTransactionProcessBtn();
		
		logger.info("*** Step 17 Actions : Click on Save button ");
		assertTrue(isElementPresent(clientTransactionDetail.clientTransactionDetailSaveButton(), 5), "         Save button should show.");
		clientTransactionDetail.clickClientTransactionDetailSaveButton();		
				
		logger.info("*** Step 17 Expected Results: - Verify that it is back to the Load Client page");
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
			
		logger.info("*** Step 18 Actions: - Reload the same Client ID");
		clientTransactionDetail.checkInputClientId(clnAbbrev);

		logger.info("*** Step 18 Expected Results: - Client - Transaction Detail page is displayed ");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client - Transaction Detail page should be displayed.");
		
		logger.info("*** Step 19 Actions: - Select the same Statement Date");
		selectItem(clientTransactionDetail.statementDateDropdown(), stmtDt);
		
		logger.info("*** Step 19 Expected Results: - Get Statement Date info button is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Statement Date Button should show.");
		assertNull(clientTransactionDetail.getStatementDateInfoBtn().getAttribute("disabled"), "        Get Statement Date info button should be displayed.");
		
		logger.info("*** Step 20 Actions: - Click on Statement Date info Button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		assertTrue(clientTransactionDetailUtils.isProgressingNotPresent(clientTransactionDetail.loadInProgressMsgText(), 10), "        Loading in Progress should be done after 10 seconds.");
		
		logger.info("*** Step 20 Expected Results: - The corresponding data for the selected Statement Date will be loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(stmtDt, listClientInfo.getClnAbbrev(), testDb),"        The corresponding data for the selected Statement Date: " + stmtDt + " should display properly.");
		
		logger.info("*** Step 20 Expected Results: - Client Payments table should show");
		assertTrue(clientTransactionDetail.clnPmtTable().isDisplayed(), "        Client Payments table should show.");
		
		logger.info("*** Step 21 Actions: - Enter the Comments added to the Credit payment in the Comments filter and tab out");		
		clientTransactionDetail.enterClnPmtNotesFilter(comments);
		
		logger.info("*** Step 21 Expected Results: - Verify that the Credit payment show in the Client Payments grid");
		assertTrue(getColumnValue(clientTransactionDetail.clientPaymentTable(), comments),"      The Credit client payment should show properly in the Client Payments grid.");

		clientTransactionDetail.clickClientTransactionDetailResetButton();
	}
	
	@Test(priority = 1, description = "Client Accn Detail-Client Payment-Create the existing payment with invalid")
	@Parameters({"paymentMethod","cardType", "cardNumber","expirationDate"})
	public void testXPR_408(String paymentMethod, String cardType, String cardNumber, String expirationDate) throws Exception {
		logger.info("===== Testing - testXPR_408 =====");    	

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - Verify that user is logged into the correct customer - Load Client - Transaction Detail page is displayed ");		
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Client - Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 2 Actions: - Input valid ClientId -> Tab Out");
		String clientId = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
		clientTransactionDetail.checkInputClientId(clientId);

		logger.info("*** Step 2 Expected Results: - Client - Transaction Detail page is displayed ");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client - Transaction Detail page should be displayed");
		
		logger.info("*** Step 3 Actions: - Client Accession Detail Section : select any statement date in Dropdown list - Tab out");
		testCodeUtils = new TestCodeUtils(driver);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		String minStatementDate = daoManagerPlatform.getClnSubmInfoFromCLNSUBMByClnAbbrev(clientId, testDb).get(1);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), minStatementDate);
		
		logger.info("*** Step 3 Expected Results: - Get Statement Date info button is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        statement Date Button should show");

		logger.info("*** Step 4 Actions: Click on Statement Date info Button");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		Thread.sleep(5000);
				
		logger.info("*** Step 4 Expected Results: The corresponding data for selected statement date will be load ");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(minStatementDate, clientId, testDb),"        Data of statement date must be correct");
		
		logger.info("*** Step 5 Actions: Client Payment - Client add icon button");
		assertTrue(isElementPresent(clientTransactionDetail.addNewClientPaymentBtn(), 5),"        Add new client payment button should show");
		clickHiddenPageObject(clientTransactionDetail.addNewClientPaymentBtn(), 0);
		
		logger.info("*** Step 5 Expected Results: The Electronic Payment Details & Cardholder Information popup is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.paymentMethodDropdown(), 5),"        paymentMethod Dropdown should show");
		
		logger.info("*** Step 6 Actions: Update payment amount is greater than current payment amount - tab out");
		assertTrue(isElementPresent(clientTransactionDetail.totalDueAmount(), 5),"        Total payment amount should show");
		String totalAmount = clientTransactionDetail.totalDueAmount().getText();
		clientTransactionDetail.addNewClientPayment(paymentMethod, cardType, cardNumber, expirationDate,totalAmount.replace("$", "2"));
		
		logger.info("*** Step 6 Expected Results: The error message 'The maximum transaction amount is XXX' is displayed ");
		assertTrue(isElementPresent(clientTransactionDetail.messageContent(), 5),"        Error message should show");
		assertTrue(clientTransactionDetail.messageContent().getText().contains("The maximum transaction amount is"),"        The error message 'The maximum transaction amount is XXX' should show.");
		
		clientTransactionDetail.clickCancelBtn();
		clientTransactionDetail.clickClientTransactionDetailResetButton();
	}

	//Document
	@Test(priority = 1, description ="Verify View Document button")
	public void testXPR_399() throws Exception {
		logger.info("===== Testing - testXPR_399 =====");
		
		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");
		
		logger.info("*** Step 2 Actions: - Enter a valid Client ID and tab out");
		String clientId = clientDao.getClnFromClnAndClnSubmWhereDueAmtIsMoreThan5().getClnAbbrev();
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
		clientTransactionDetail.checkInputClientId(clientId);
		
		logger.info("*** Step 2 Expected Results: - The Client - Transaction detail page is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");
		
		logger.info("*** Step 3 Actions: - Click on View Document link in the header menu");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.viewDocumentLink(), 5), "        The View Document link should be displayed");
		clientTransactionDetail.clickViewDocumentLink();
		
		logger.info("*** Step 3 Expected Results: - The View Document page is displayed");
		String parent = switchToPopupWin();
		assertTrue(driver.getCurrentUrl().contains("docstorewebapp"), "        Docstorewebapp page should be opened.");
		driver.close();
		switchToParentWin(parent);
		clientTransactionDetail.clickClientTransactionDetailResetButton();
	}
	
	@Test(priority = 1, description = "Documents-View any document in the grid")
	public void testXPR_405() throws Exception {
		logger.info("===== Testing - testXPR_405 =====");    	

		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - Verify that user is logged into the correct customer - Load Client - Transaction Detail page is displayed ");		
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail Page should displayed");

		logger.info("*** Step 2 Actions: - Input valid ClientId -> Tab Out");
		String clientId = clientDao.getClientIdHaveDocument().getClnAbbrev();
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
		clientTransactionDetail.checkInputClientId(clientId);

		logger.info("*** Step 2 Expected Results: - Client - Transaction Detail page is displayed ");
		testCodeUtils = new TestCodeUtils(driver);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client - Transaction Detail page should be displayed");
		String statementDate =  clientTransactionDetail.getStatementDateHaveDocument(clientId, testDb);
		selectItem(clientTransactionDetail.statementDateDropdown(), statementDate);
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        statement Date Button should show");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		Thread.sleep(5000);
				
		logger.info("*** Step 3 Actions: - Document Section : select any document link in gird");
		scrollToElement(0, 1000);
		testCodeUtils = new TestCodeUtils(driver);
		clickHiddenPageObject(clientTransactionDetail.FileLink(2), 0);
		
		logger.info("*** Step 3 Expected Results: - Document Page will be displayed");
		String parentWin = driver.getWindowHandle();
		switchToPopupWin();
		assertNotNull(driver.getCurrentUrl(),"        Document Page will be displayed");

		driver.close();
		switchToParentWin(parentWin);
		clientTransactionDetail.clickClientTransactionDetailResetButton();
	}
	
	// Client Adjustment
	@Test(priority = 1, description ="Client Adjustments - Create Client Adj with valid data")
	public void testXPR_376() throws Exception {
		
		logger.info("===== Testing - testXPR_376 =====");
		
		logger.info("*** Step 1 Actions: - Login to SSO by valid username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail","        Load Client Transaction Detail page should displayed");
		
		logger.info("*** Step 2 Actions: - Enter a valid Client ID and tab out");
		ArrayList<String> adjInfo = daoManagerXifinRpm.getADJInfo(testDb);
		String clientId = adjInfo.get(2).trim();
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
		clientTransactionDetail.checkInputClientId(clientId);
		
		logger.info("*** Step 2 Expected Results: - Verify that the Client - Transaction detail page is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed.");
		assertEquals(clientTransactionDetail.clnTransDetPageTitle().getText(), "Transaction Detail", "        Load Client Transaction Detail page should be displayed.");
		
		logger.info("*** Step 3 Actions: - Client Accession Detail -Select any Statement Date in dropdown list and tab out");
		testCodeUtils = new TestCodeUtils(driver);
		clientTransactionDetailUtils = new ClientTransactionDetailUtils(driver);
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Statement Date dropdown should show.");
		Date statementDate = clientDao.getClnSubmWithAmtFromCLNSUBMByClnAbbrev(clientId).getSubmDt();
		String date = ConvertUtil.getDateAsTring(statementDate, "MM/dd/yyyy", "");
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), date);
		
		logger.info("*** Step 3 Expected Results: - The Get Statement Date Info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        The Get Statement Date Info button should show.");
		assertTrue(isElementEnabled(clientTransactionDetail.getStatementDateInfoBtn(), 5, true), "         The Get Statement Date Info button should be enabled.");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		Thread.sleep(5000);
				
		logger.info("*** Step 4 Actions: - Client Adjustments - Click on Add icon");
		assertTrue(isElementPresent(clientTransactionDetail.addNewClientAdjustmentsBtn(), 5),"        The Add new Client Adjustment button should show.");
		clickHiddenPageObject(clientTransactionDetail.addNewClientAdjustmentsBtn(), 0);
				
		logger.info("*** Step 4 Expected Results: - Client Adjustments - Add Record popup is displayed");
		assertTrue(isElementPresent(clientTransactionDetail.currentAmountDueAdj(), 5),"        Current Amount Due field is displayed");
		assertEquals(clientTransactionDetail.currentAmountDueAdj().getAttribute("disabled"),"true","          Current Amount Due field is read only field ");
		assertTrue(isElementPresent(clientTransactionDetail.newAmountDueAdj(), 5),"        New Amount Due field is displayed");
		assertEquals(clientTransactionDetail.newAmountDueAdj().getAttribute("disabled"),"true","          New Amount Due field is read-only field ");
		assertTrue(isElementPresent(clientTransactionDetail.userIDInput(), 5),"        User ID field is displayed");
		assertEquals(clientTransactionDetail.userIDInput().getAttribute("disabled"),"true","          User ID field is read-only field.");
			
		logger.info("*** Step 5 Actions: - Client Adjustments - Enter valid data for fields: ADJ code, Rev ADJ Accn, Amount and Note. Click OK button");
		String adjAccn = adjInfo.get(1);
		assertTrue(isElementPresent(clientTransactionDetail.reviewAdjustmentsAccn(), 5),"        Rev ADJ Accn field is displayed.");
		clientTransactionDetail.inputRevAdjAccnId(adjAccn);
		assertTrue(isElementPresent(clientTransactionDetail.amountInput(), 5),"        Amount field is displayed.");
		randomCharacter = new RandomCharacter(driver);
		String amount = randomCharacter.getRandomNumericString(1);
		clientTransactionDetail.inputAddRecordAmount(amount);
		assertTrue(isElementPresent(clientTransactionDetail.adjCodeDropdown(), 5),"        ADJ Code dropdown is displayed.");
		String adjCode = clientTransactionDetail.selectAdjCode();
		assertTrue(isElementPresent(clientTransactionDetail.okClientAdjBtn(), 5),"        OK button is displayed.");
		clientTransactionDetail.clickOkClientAdjBtn();
		
		logger.info("*** Step 5 Expected Results: - Verify that the new Amount due displays after entering the Adjustment");
		assertTrue(isElementPresent(clientTransactionDetail.clientAdjustmentTbl(), 5),"        Client Adjustment table should show.");
		assertTrue(getColumnValue(clientTransactionDetail.clientAdjustmentTbl(), adjCode));
		
		clientTransactionDetail.clickClientTransactionDetailResetButton();
		driver.switchTo().alert().accept();
	}
	
	@Test(priority = 1, description = "Client Accn Detail - Client Adjustment - Verify Canned note btn")
	public void testXPR_379() throws Exception {
		
		logger.info("===== Testing - XPR_379 =====");  
		
		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - The Load Client Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"),"        Page Title should be 'Client - Transaction Detail'");
		
		logger.info("*** Step 2 Actions: - Enter a valid Client ID. Tab out");
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
		ArrayList<String> adjInfo = daoManagerXifinRpm.getADJInfo(testDb);
		String clientID = adjInfo.get(2).trim();
		clientTransactionDetail.checkInputClientId(clientID);
		
		logger.info("*** Step 2 Expected Result: - The Client Transaction Detail page displays");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        Client id label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        Client name label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.statementDateDropdown(), 5),"        Client Accession Detail - Statement drop down should be displayed");

		logger.info("*** Step 3 Actions: - Select any statement from Client Accession Detail section");
		testCodeUtils = new TestCodeUtils(driver);
		Date statementDate = clientDao.getClnSubmWithAmtFromCLNSUBMByClnAbbrev(clientID).getSubmDt();
		String date = ConvertUtil.getDateAsTring(statementDate, "MM/dd/yyyy", "");
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), date);
		
		logger.info("*** Step 3 Expected Result: - The Get Statement date info button is enable");
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Get statement date button should be displayed");
		assertTrue(clientTransactionDetail.getStatementDateInfoBtn().isEnabled(),"        Get statement date button should be enabled.");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);
		
		logger.info("*** Step 4 Actions: - Client Adjustments section : click add icon");
		assertTrue(isElementPresent(clientTransactionDetail.addNewClientAdjustmentsBtn(), 5),"        Add adjustment button should be displayed");
		clickHiddenPageObject(clientTransactionDetail.addNewClientAdjustmentsBtn(), 0);
		
		logger.info("*** Step 4 Expected Result: - Add new client adjustment popup displayed");
		assertTrue(isElementPresent(clientTransactionDetail.addNewClientAdjustmentsPopup(), 5),"       Add new client adjustment popup should be displayed.");
		assertTrue(isElementPresent(clientTransactionDetail.cannedNotesBtn(), 5),"       Canned Notes button should be displayed.");
		
		logger.info("*** Step 5 Actions: - Click on Canned Notes button");
		clientTransactionDetail.clickCannedNotesBtn();
		String parentWin = switchToPopupWin();
		
		logger.info("*** Step 5 Expected Result: - Canned note page display");
		assertTrue(isElementPresent(clientTransactionDetail.cannedNotesTbl(), 5),"       Canned Notes table should be displayed.");
		
		driver.close();
		switchToParentWin(parentWin);
		clientTransactionDetail.clickCancelAddAdjustmentBtn();
		clientTransactionDetail.clickClientTransactionDetailResetButton();
	}
	
	//Reconciled Statement
	@Test(priority = 1, description = "Reconciled Statement - View Reconciled statement in PDF")
	public void testXPR_403() throws Exception {
		
		logger.info("===== Testing - XPR_403 =====");  
		
		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - The Load Client Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"),"        Page Title should be 'Client - Transaction Detail'");
		
		logger.info("*** Step 2 Actions: - Enter a valid Client ID. Tab out");
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
		String clientID = clientDao.getClientIdWithEnableExcelIcon().getClnAbbrev();
		clientTransactionDetail.setClientIdInput(clientID);
		
		logger.info("*** Step 2 Expected Result: - The Client Transaction Detail page displays");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        Client id label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        Client name label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.reconciledPDFStatementBtn(), 5),"        Reconciled PDF Statement button should be displayed");
		
		logger.info("*** Step 3 Actions: - Click on Reconciled PDF Statement button");
		timeStamp = new TimeStamp(driver);
		ArrayList<String> clnSubmList = daoManagerPlatform.getClnSubmInfoFromCLNSUBMByClnAbbrev(clientID, testDb);
		String statementDate = clnSubmList.get(1);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), statementDate);
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Get statement date button should be displayed");
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);

		clickHiddenPageObject(clientTransactionDetail.reconciledPDFStatementBtn(), 0);
		String parentWin = clientTransactionDetail.switchToPopupWin();
		
		logger.info("*** Step 3 Expected Result: - The Reconciled PDF Statement display in PDF format");
//		assertEquals(driver.getCurrentUrl().split("=")[1], "RECONCILEDSTMT&isSuppress", "        PDF file should opened");
		
		driver.close();
		switchToParentWin(parentWin);
		clientTransactionDetail.clickClientTransactionDetailResetButton();
	}
	
	@Test(priority = 1, description = "Reconciled Statement - View Reconciled statement in Excel")
	public void testXPR_404() throws Exception {
		
		logger.info("===== Testing - XPR_404 =====");  
		
		logger.info("*** Step 1 Actions: - Log into XifinPortal with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - The Load Client Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"),"        Page Title should be 'Client - Transaction Detail'");
		
		logger.info("*** Step 2 Actions: - Enter a valid Client ID. Tab out");
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
		String clientID = clientDao.getClientIdWithEnableExcelIcon().getClnAbbrev();
		clientTransactionDetail.setClientIdInput(clientID);
		
		logger.info("*** Step 2 Expected Result: - The Client Transaction Detail page displays");
		assertTrue(isElementPresent(clientTransactionDetail.clientIDLabel(), 5),"        Client ID label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.clientNameLabel(), 5),"        Client name label should be displayed");
		assertTrue(isElementPresent(clientTransactionDetail.reconciledExcelStatementBtn(), 5),"        Reconciled Excel Statement button should be displayed");
		
		logger.info("*** Step 3 Actions: - Click on Reconciled Excel Statement button");
		timeStamp = new TimeStamp(driver);
		ArrayList<String> clnSubmList = daoManagerPlatform.getClnSubmInfoFromCLNSUBMByClnAbbrev(clientID, testDb);
		String statementDate = clnSubmList.get(0);
		selectItemByVal(clientTransactionDetail.statementDateDropdown(), statementDate);
		assertTrue(isElementPresent(clientTransactionDetail.getStatementDateInfoBtn(), 5),"        Get statement date button should be displayed");
//		clientTransactionDetail.getStatementDateInfoBtn().click();
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);

		clickHiddenPageObject(clientTransactionDetail.reconciledExcelStatementBtn(), 0);
		String parentWin = clientTransactionDetail.switchToPopupWin();
		
		logger.info("*** Step 3 Expected Result: - The Reconciled Excel Statement display in Excel format");
//		assertEquals(driver.getCurrentUrl().split("=")[1], "RECONCILEDEXCELSTMT&isSuppress", "        Excel file should opened");
		//Need to be updated later ...

		driver.close();
		switchToParentWin(parentWin);
		clientTransactionDetail.clickClientTransactionDetailResetButton();
	}
	
	//Client Bill Accn
	
	@Test(priority = 1, description = "Client Accn Detail - Client Bill Accn - View Accn detail page")
	public void testXPR_388() throws Exception {
		
		logger.info("===== Testing - XPR_388 =====");  
		
		logger.info("*** Step 1 Actions: - Log into RPM with SSO username and password");
		navigation = new MenuNavigation(driver, config);
		navigation.navigateToClientTransactionDetailPage();

		logger.info("*** Step 1 Expected Results: - The Load Client Transaction Detail page title displays");
		clientTransactionDetail = new ClientTransactionDetail(driver, config, wait);
		assertTrue(isElementPresent(clientTransactionDetail.clnTransDetPageTitle(), 5),"        The Client Transaction Detail page title should be displayed");
		assertTrue(clientTransactionDetail.clnTransDetPageTitle().getText().trim().contains("Transaction Detail"),"        Page Title should be 'Client - Transaction Detail'");
		
		logger.info("*** Step 2 Actions: - Enter a valid Client ID. Tab out");
		wait.until(ExpectedConditions.presenceOfElementLocated(clientTransactionDetail.clientIdInputLocator()));
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
		clickHiddenPageObject(clientTransactionDetail.getStatementDateInfoBtn(), 0);

		logger.info("*** Step 4 Expected Result: - The corresponding data for selected statement date is loaded");
		assertTrue(clientTransactionDetail.verifyDataCorrespondingStatementDate(statementDate, clientID, testDb),"        Data of statement date must be correct");
		
		logger.info("*** Step 5 Actions: - Click on Accession Detail button");
		assertTrue(isElementPresent(clientTransactionDetail.accDetailBtn(), 5),"        Accession Detail button should be displayed");
		clickHiddenPageObject(clientTransactionDetail.accDetailBtn(), 0);
		String parentWin = switchToPopupWin();
		
		logger.info("*** Step 5 Expected Result: - List accession detail page show with list out all accession id belong to selected client ID");
		assertTrue(isElementPresent(clientTransactionDetail.clientBillAccTbl(), 5),"       Client Bill Accession table should be displayed");
		assertTrue(clientTransactionDetail.verifyDataOfClientBillAccCorrespondingStatementDate(statementDate, clientID, testDb),"       List out all accession id belong to selected client ID");
		
		logger.info("*** Step 6 Actions: - Click on Accession ID on table");
		List<List<String>> billAcc = daoManagerXifinRpm.getListClientBillAccByClientIDAndStatementDate(clientID, statementDate, testDb);
		clientTransactionDetail.clickAccessionID(billAcc.get(0).get(1));
		driver.close();
		switchToParentWin(parentWin);
		
		logger.info("*** Step 6 Actions: - System will take user to Accession Detail page");
		accessionDetail = new AccessionDetail(driver, config, wait);
		assertTrue(isElementPresent(accessionDetail.accnIdText(), 5),"       Accn ID text field should show.");
		String accnId = billAcc.get(0).get(1);
		assertEquals(accnId, accessionDetail.accnIdText().getAttribute("value"), "       Accn ID: " + accnId + "should show.");
		
		accessionDetail.clickReset();
	}
}	
