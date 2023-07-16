package com.mars.tests;

//import java.io.File;
//import java.net.URL;
import com.overall.headerNavigation.HeaderNavigation;
import com.overall.payment.paymentNavigation.PaymentNavigation;
import com.overall.payment.paymentPayments.DepositSearch;
import com.overall.payment.paymentPayments.DepositSearchResults;
import com.overall.payment.paymentPayments.Deposits;
import com.overall.payment.paymentPayments.PaymentSearch;
import com.overall.payment.paymentPayments.PaymentSearchResults;
import com.overall.payment.paymentPayments.PayorSearch;
import com.overall.payment.paymentPayments.PayorSearchResults;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.TimeStamp;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;

//import java.util.List;
/*
import com.overall.accession.accessionNavigation.AccessionNavigation;
import com.overall.accession.accessionProcessing.AccessionDemographics;
import com.overall.accession.accessionProcessing.AccessionSearch;
import com.overall.accession.accessionProcessing.AccessionSingleStatement;
import com.overall.accession.accessionProcessing.AccessionTransactionDetail;
import com.overall.fileMaintenance.orderProcessingConfig.FileMaintencePatternDefinition;
import com.overall.fileMaintenance.sysMgt.DataCacheConfiguration;
import com.overall.fileMaintenance.sysMgt.TaskScheduler;
import com.overall.fileMaintenance.sysMgt.TaskStatus;
import com.overall.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
*/
//import com.overall.help.Help;
//import com.overall.accession.orderProcessing.AccnTestUpdateOld;
/*
import com.overall.payor.payorDemographics.GroupDemographics;
import com.overall.payor.payorDemographics.PricingConfig;
import com.overall.payor.payorNavigation.PayorNavigation;
import com.overall.accession.accessionProcessing.superSearch.SuperSearch;
import com.overall.accession.accessionProcessing.superSearch.SuperSearchResults;
*/
//import com.xifin.utils.TestDataSetup;

public class RegressionPaymentPaymentsTest extends SeleniumBaseTest  {
	
	private SsoLogin ssoLogin;
	private HeaderNavigation headerNavigation;
	/*
	private Help help;
	private AccessionNavigation accessionNavigation;
	private SuperSearch superSearch;
	private SuperSearchResults superSearchResults;
	private AccessionDemographics accessionDemographics;
	private AccessionSingleStatement accessionStatement;
	private AccessionTransactionDetail accessionTransactionDetail;
	private AccessionSearch accessionSearchPopup;
	private AccnTestUpdateOld accnTestUpdate;
	private TestDataSetup testDataSetup;
	private FileMaintenanceNavigation fileMaintenaceNavigation;
	private PayorNavigation payorNavigation;
	private GroupDemographics groupDemographics;
	private PricingConfig pricingConfig;
	private DataCacheConfiguration dataCacheConfiguration;
	private TaskScheduler taskScheduler;
	private TaskStatus taskStatus;
	private FileMaintencePatternDefinition fileMaintencePatternDefinition;
	*/
	private TimeStamp timeStamp;	
	private PaymentNavigation paymentNavigation;
	private DepositSearch depositSearch;
	private Deposits deposits;
	private DepositSearchResults depositSearchResults;
	private PaymentSearch paymentSearch;
	private PaymentSearchResults paymentSearchResults;
	private PayorSearch payorSearch;
	private PayorSearchResults payorSearchResults;

	
	@Test(priority = 1, description = "RPM - Payments - Deposits JSP - Deposit Search JSP - Verify that valid values will be accepted by the date range fields")
	@Parameters({"email", "password"})
	public void testRPM_401(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_401 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 4 Actions: - Click Deposit Date Range radio button and enter '01.01.2014' into the Deposit Date Range 'From' field and Tab off");
		depositSearch.depositDateRangeCheckBox().click();
		String validStr1 = "01.01.2014";
		depositSearch.setDepositDateRangeFrom(validStr1);
		
		logger.info("*** Step 4 Expected Results: - Verify that '01/01/2014' populate in the field");
		Assert.assertEquals(depositSearch.depositDateRangeFromInput().getAttribute("value"), "01/01/2014");
		
		logger.info("*** Step 5 Actions: - Click Search button");	
		depositSearch.searchBtn().click();
		
		logger.info("*** Step 5 Expected Results: - Verify that Deposite Search Results window shows");
		switchToPopupWin();		
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		logger.info("*** Step 6 Actions: - Click 'New Search' button");
		depositSearchResults.newResearchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that it's on the Deposit Search window");
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 7 Actions: - Click Deposit Date Range radio button and enter '01.01.2014' into the Deposit Date Range 'To' field and Tab off");
		depositSearch.depositDateRangeCheckBox().click();
		depositSearch.setDepositDateRangeTo(validStr1);
		
		logger.info("*** Step 7 Expected Results: - Verify that '01/01/2014' populate in the field");
		Assert.assertEquals(depositSearch.depositDateRangeToInput().getAttribute("value"), "01/01/2014");
		
		logger.info("*** Step 8 Actions: - Click Search button");	
		depositSearch.searchBtn().click();
		
		logger.info("*** Step 8 Expected Results: - Verify that Deposite Search Results window shows");
		switchToPopupWin();			
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		driver.close();
    }
	
	@Test(priority = 1, description = "RPM - Payments - Deposits JSP - Deposit Search JSP - Invalid value ad/1/2014 in Deposite Date Range fields")
	@Parameters({"email", "password"})
	public void testRPM_402(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_402 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 4 Actions: - Click Deposit Date Range radio button and enter 'ad/1/2014' into the Deposit Date Range 'From' field and Tab off");
		depositSearch.depositDateRangeCheckBox().click();
		String invalidStr1 = "ad/1/2014";
		depositSearch.setDepositDateRangeFrom(invalidStr1);
		
		logger.info("*** Step 4 Expected Results: - A popup should come up explaining the rules of the date field.");		
		Assert.assertTrue(closeAlertAndGetItsText(true).contains("Date fields"), "        A popup should come up explaining the rules of the date field.");
		
		logger.info("*** Step 5 Actions: - Clear the field and enter 'ad/01/2014' into the Deposit Date Range 'To' Field. Tab off.");
		depositSearch.depositDateRangeFromInput().clear();
		depositSearch.setDepositDateRangeTo(invalidStr1);
		
		logger.info("*** Step 5 Expected Results: - A popup should come up explaining the rules of the date field.");		
		Assert.assertTrue(closeAlertAndGetItsText(true).contains("Date fields"), "        A popup should come up explaining the rules of the date field.");
					
		driver.close();
    }

	@Test(priority = 1, description = "RPM - Payments - Deposits JSP - Deposit Search JSP - Invalid value ASD in Deposit Amount Range fields")
	@Parameters({"email", "password"})
	public void testRPM_403(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_403 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 4 Actions: - Enter 'ASD' into the Deposit Amount Range 'From:' field and Tab off");		
		String invalidStr1 = "ASD";
		depositSearch.setDepositAmountRangeFrom(invalidStr1);
		
		logger.info("*** Step 4 Expected Results: - A popup should come up explaining the rules of the amount field.");		
		Assert.assertTrue(closeAlertAndGetItsText(true).contains("This field must be a positive amount"), "        A popup should come up explaining the rules of the amount field.");
		
		logger.info("*** Step 5 Actions: - Clear the Deposit Amount Range 'From:' field. Enter in 'ASD' into the 'To:' field. Tab off.");
		depositSearch.depositAmountRangeFromInput().clear();
		depositSearch.setDepositAmountRangeTo(invalidStr1);
		
		logger.info("*** Step 5 Expected Results: - A popup should come up explaining the rules of the amount field.");		
		Assert.assertTrue(closeAlertAndGetItsText(true).contains("This field must be a positive amount"), "        A popup should come up explaining the rules of the amount field.");
		
		driver.close();
    }
	
	@Test(priority = 1, description = "Payment - Payments - Deposite Search - Valid value 123.12 in Deposit Amount Range fields")
	@Parameters({"email", "password"})
	public void testRPM_404(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_404 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 4 Actions: - Select All in the Deposit Period dropdown, enter '123.12' into the Deposit Amount Range 'From:' field and Tab off and click Search");
		//"0" = All
		depositSearch.setDepositPeriod("0");
		String validStr1 = "123.12";
		depositSearch.setDepositAmountRangeFrom(validStr1);
		depositSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Verify that search result is matching the sql query that contains 'dep.dep_amt>=123.12' was sent in the log..");
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		ArrayList<String> depList = daoManagerXifinRpm.getDepIdFromDEPByDepAmtFrom(validStr1, testDb);
		String depId = depList.get(0);
		Assert.assertTrue(getColumnValue(depositSearchResults.depositLedgerTable(), depId), "        Deposit ID " + depId + " should display in the Deposit Search Results.");
		
		logger.info("*** Step 5 Actions: - Once on the Deposit Search Results, click 'New Search'");
		depositSearchResults.newResearchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - The Deposit Search JSP should come back up.");
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 6 Actions: - Select All in the Deposit Period dropdown, enter '123.12' into the Deposit Amount Range 'To:' field and Tab off and click Search");
		//"0" = All
		depositSearch.setDepositPeriod("0");		
		depositSearch.setDepositAmountRangeTo(validStr1);
		depositSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that search result is matching the sql query that contains 'dep.dep_amt<=123.12' was sent in the log..");		
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		depList = daoManagerXifinRpm.getDepIdFromDEPByDepAmtTo(validStr1, testDb);
		depId = depList.get(0);
		Assert.assertTrue(getColumnValue(depositSearchResults.depositLedgerTable(), depId), "        Deposit ID " + depId + " should display in the Deposit Search Results.");
		
		driver.close();
    }
	
	@Test(priority = 1, description = "RPM - Payments - Deposit Search JSP - Deposit Date Range To date before the From date")
	@Parameters({"email", "password"})
	public void testRPM_405(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_405 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 4 Actions: - Enter '06/15/2012' date into the 'Deposit Date Range From:' field. Tab off. Enter '06/15/2013' date into the 'To:' field. Tab off. And click Search");
		String date1 = "06/15/2012";
		String date2 = "06/15/2013";		
		depositSearch.depositDateRangeCheckBox().click();
		depositSearch.setDepositDateRangeFrom(date1);
		depositSearch.setDepositDateRangeTo(date2);
		depositSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Verify that the search results match the sql query with fields like  AND dep.remit_dt >= TO_DATE ('06/15/2012', 'MM/DD/YYYY') AND dep.remit_dt <= TO_DATE ('06/15/2013', 'MM/DD/YYYY') in the log.");
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		ArrayList<String> depList = daoManagerXifinRpm.getDepIdFromDEPByDepDateRange(date1, date2, testDb);
		String depId = depList.get(0);
		Assert.assertTrue(getColumnValue(depositSearchResults.depositLedgerTable(), depId), "        Deposit ID " + depId + " should display in the Deposit Search Results.");

		driver.close();
    }
	
	@Test(priority = 1, description = "Payment - Payments - Deposite Search - From value higher than To value in Deposit Amount Range field")
	@Parameters({"email", "password"})
	public void testRPM_406(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_406 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 4 Actions: - Set Deposite Period to 'All'. Enter '100' into the 'Deposit Amount Range From:' field. Tab off. Enter '200' into the 'To:' field. Tab off. And click Search");
		//'0' = All
		depositSearch.setDepositPeriod("0");
		
		String amount1 = "100";
		String amount2 = "200";		
		
		depositSearch.setDepositAmountRangeFrom(amount1);
		depositSearch.setDepositAmountRangeTo(amount2);
		depositSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Verify that the search results match the sql query with fields like AND dep.dep_amt >= 100.0 AND dep.dep_amt <= 200.0 in the log.");
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		ArrayList<String> depList = daoManagerXifinRpm.getDepIdFromDEPByDepAmountRange(amount1, amount2, testDb);
		String depId = depList.get(0);
		Assert.assertTrue(getColumnValue(depositSearchResults.depositLedgerTable(), depId), "        Deposit ID " + depId + " should display in the Deposit Search Results.");
	
		driver.close();
    }
	
	@Test(priority = 1, description = "RPM - Payments - Deposits JSP - Deposit Search JSP - 'Remittance File Name' search criteria available")
	@Parameters({"email", "password"})
	public void testRPM_407(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_407 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window  and the Remittance File Name is a search criteria listed");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		Assert.assertTrue(depositSearch.fileNameInput().isDisplayed(), "        Remittance File Name Input field should show.");
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    }
	
	@Test(priority = 1, description = "RPM - Payments - Deposits JSP - Deposit Search JSP - Verify search results for Remittance File Name criteria")
	@Parameters({"email", "password"})
	public void testRPM_408(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_408 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 4 Actions: - Set Deposite Period to 'All'. Enter file name in Remittance File Name field and click Search button");
		//'0' = All
		depositSearch.setDepositPeriod("0");
		
		String fileName = "MCCA_ACME_20130312_01_00_04";	
		depositSearch.setFileName(fileName + "*");
		depositSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Verify that the search results match the sql query with Remittance File Name field value");
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		ArrayList<String> depList = daoManagerXifinRpm.getDepIdFromDEPByFileName(fileName, testDb);
		String depId = depList.get(0);
		Assert.assertTrue(getColumnValue(depositSearchResults.depositLedgerTable(), depId), "        Deposit ID " + depId + " should display in the Deposit Search Results.");
						
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    }
	
	@Test(priority = 1, description = "RPM - Payments - Deposits JSP - Deposit Search JSP - Search Results when Remittance File Name is excluded")
	@Parameters({"email", "password"})
	public void testRPM_409(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_409 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 4 Actions: - Set Deposite Period to 'All'. Exclude Remittance File Name criteria - leave it blank and click Search button");
		//'0' = All
		depositSearch.setDepositPeriod("0");		

		depositSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Verify that the search results match the sql query without specifying Remittance File Name field value");
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		ArrayList<String> depList = daoManagerXifinRpm.getDepIdFromDEPByDepositPeriodAll(testDb);
		String depId = depList.get(0);
		Assert.assertTrue(getColumnValue(depositSearchResults.depositLedgerTable(), depId), "        Deposit ID " + depId + " should display in the Deposit Search Results.");
						
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
    }
	
	@Test(priority = 1, description = "RPM - Payment - Payments - Deposite Search - Deposit Period")
	@Parameters({"email", "password"})
	public void testRPM_410(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_410 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp which is a sub-frame inside the Deposits page
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 4 Actions: - Set Deposite Period to '2014 MAY'. Set Bank to BOA and click Search button");
		//'20140' = 2014 MAY
		depositSearch.setDepositPeriod("201405");	
		//'1' = BOA
		depositSearch.setBank("1");
		depositSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Verify that the search results match the sql query with specifying values");
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		ArrayList<String> depList = daoManagerXifinRpm.getDepIdFromDEPByDepositPeriodBank("2014-05-01", "2014-05-31", "1", testDb);
		String depId = depList.get(0);
		Assert.assertTrue(getColumnValue(depositSearchResults.depositLedgerTable(), depId), "        Deposit ID " + depId + " should display in the Deposit Search Results.");
		
		driver.close();	
    }
	
	@Test(priority = 1, description = "RPM - Payments - Deposits JSP - Deposit Search JSP - Verify that the Deposit Period works")
	@Parameters({"email", "password"})
	public void testRPM_440(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_440 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp which is a sub-frame inside the Deposits page
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 4 Actions: - Set Deposite Period to '2014 MAY'. Set Back to BOA and click Search button");
		//'201405' = 2014 MAY
		depositSearch.setDepositPeriod("201405");	
		//'1' = BOA
		depositSearch.setBank("1");
		depositSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Verify that the search results match the sql query with specifying values");
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		//'0'= All Banks
		ArrayList<String> depList = daoManagerXifinRpm.getDepIdFromDEPByDepositPeriodBank("2014-05-01", "2014-05-31", "1", testDb);
		String depId = depList.get(0);
		Assert.assertTrue(getColumnValue(depositSearchResults.depositLedgerTable(), depId), "        Deposit ID " + depId + " should display in the Deposit Search Results.");
			
		driver.close();
    }
	
	@Test(priority = 1, description = "RPM - Payments - Payment Search - Search Results open in a new pop-up window")
	@Parameters({"email", "password"})
	public void testRPM_479(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_479 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to Payment Tab > Payment Search page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();				
		
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToPaymentSearchLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Payment Search page shows");
		paymentSearch = new PaymentSearch(driver, wait);
		Assert.assertTrue(paymentSearch.checkNumInput().isDisplayed(), "        Check # Input field in Payment Search page should show.");
		
		logger.info("*** Step 3 Actions: - Click Search button");
		paymentSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payment Search Results popup window shows");
		paymentSearchResults = new PaymentSearchResults(driver);
		Assert.assertTrue(paymentSearchResults.pageTitleText().getText().trim().contains("Payment Search Results"), "        Payment Search Results page title should show.");
		
    }
	
	@Test(priority = 1, description = "RPM - Payments - Payment Search - Payment Search Results show when specify a Check #")
	@Parameters({"email", "password"})
	public void testRPM_483(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_483 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to Payment Tab > Payment Search page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();				
		
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToPaymentSearchLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Payment Search page shows");
		paymentSearch = new PaymentSearch(driver, wait);
		Assert.assertTrue(paymentSearch.checkNumInput().isDisplayed(), "        Check # Input field in Payment Search page should show.");
		
		logger.info("*** Step 3 Actions: - Enter a Check # in the Check # field and tab out");		
		String chkNum = daoManagerXifinRpm.getChkNumFromACCNPMT(testDb);
		paymentSearch.setCheckNum(chkNum);		
		
		logger.info("*** Step 3 Actions: - Click Search button");
		paymentSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payment Search Results popup window shows");
		paymentSearchResults = new PaymentSearchResults(driver);
		Assert.assertTrue(paymentSearchResults.pageTitleText().getText().trim().contains("Payment Search Results"), "        Payment Search Results page title should show.");
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payment Search Results shows");
		//ArrayList<String> pmtList = daoManagerXifinRpm.getPmtInfoFromACCNPMT(chkNum, testDb);
		//String accnId = pmtList.get(0).trim();		
		
		//Find matching accnId in the Search Results table 
		//Assert.assertEquals(paymentSearchResults.searchResultsText("2", "1").getAttribute("value"), accnId);
		Assert.assertEquals(paymentSearchResults.searchResultsText("2", "10").getText().trim(), chkNum);
    }
	
	@Test(priority = 1, description = "RPM - Payments - Deposit Search JSP - Deposit Date Range To date after the From date")
	@Parameters({"email", "password"})
	public void testRPM_640(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_640 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		String date1 = "06/15/2012";
		String date2 = "06/15/2013";

		logger.info("*** Step 4 Actions: - Enter '06/15/2013' date into the 'Deposit Date Range From:' field. Tab off. Enter '06/15/2012' date into the 'To:' field. Tab off. And click Search");
		depositSearch.depositDateRangeCheckBox().click();
		depositSearch.setDepositDateRangeFrom(date2);
		depositSearch.setDepositDateRangeTo(date1);
		depositSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Verify that the search results match the sql query with fields like  AND dep.remit_dt >= TO_DATE ('06/15/2013', 'MM/DD/YYYY') AND dep.remit_dt <= TO_DATE ('06/15/2012', 'MM/DD/YYYY') in the log.");
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		//In this case, not search result returned
		ArrayList<String> depList = daoManagerXifinRpm.getDepIdFromDEPByDepDateRange(date2, date1, testDb);
		Assert.assertEquals(depList.size(), 0);		
		Assert.assertEquals(getTableTotalRowSize(depositSearchResults.depositLedgerTable()), 1);

		driver.close();
    }
	
	@Test(priority = 1, description = "RPM - Payments - Deposit Search JSP - Deposit Date Range To date equals to the From date")
	@Parameters({"email", "password"})
	public void testRPM_641(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_641 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 4 Actions: - Enter '03/12/2013' date into the 'Deposit Date Range From:' field. Tab off. Enter '03/12/2013' date into the 'To:' field. Tab off. And click Search");
		depositSearch.depositDateRangeCheckBox().click();
		String date3 = "03/12/2013";
		depositSearch.setDepositDateRangeFrom(date3);
		depositSearch.setDepositDateRangeTo(date3);
		depositSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Verify that the search results match the sql query with fields like  AND dep.remit_dt >= TO_DATE ('03/12/2013', 'MM/DD/YYYY') AND dep.remit_dt <= TO_DATE ('03/12/2013', 'MM/DD/YYYY') in the log.");
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		ArrayList<String> depList = daoManagerXifinRpm.getDepIdFromDEPByDepDateRange(date3, date3, testDb);
		String depId = depList.get(0);
		Assert.assertTrue(getColumnValue(depositSearchResults.depositLedgerTable(), depId), "        Deposit ID " + depId + " should display in the Deposit Search Results.");
								
		driver.close();
    }
	
	@Test(priority = 1, description = "RPM - Payment - Payments - Deposite Search - Payor ID")
	@Parameters({"email", "password"})
	public void testRPM_642(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_642 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp which is a sub-frame inside the Deposits page
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");

		logger.info("*** Step 4 Actions: - Set Deposit Period to All. Set Payor ID to 'C' and click Search button.");
		//'0' = All
		depositSearch.setDepositPeriod("0");
		depositSearch.setPayorID("C");
		depositSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Verify that the search results match the sql query with specifying values");
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		ArrayList<String> depList = daoManagerXifinRpm.getDepIdFromDEPByPyr("C", testDb);
		String depId = depList.get(0);
		Assert.assertTrue(getColumnValue(depositSearchResults.depositLedgerTable(), depId), "        Deposit ID " + depId + " should display in the Deposit Search Results.");
		
		driver.close();	
    }
	
	@Test(priority = 1, description = "RPM - Payment - Payments - Deposite Search - EFT ID")
	@Parameters({"email", "password"})
	public void testRPM_643(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_643 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp which is a sub-frame inside the Deposits page
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");

		logger.info("*** Step 4 Actions: - Set Deposit Period to All. Set EFT ID to '123456789' and click Search button.");
		//'0' = All
		depositSearch.setDepositPeriod("0");
		depositSearch.setEFTID("123456789");
		depositSearch.searchBtn().click();
		switchToPopupWin();		
		
		logger.info("*** Step 4 Expected Results: - Verify that the search results match the sql query with specifying values");
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		ArrayList<String> depList = daoManagerXifinRpm.getDepIdFromDEPByEFTId("123456789", testDb);
		String depId = depList.get(0);
		Assert.assertTrue(getColumnValue(depositSearchResults.depositLedgerTable(), depId), "        Deposit ID " + depId + " should display in the Deposit Search Results.");
		
		driver.close();	
    }
	
	@Test(priority = 1, description = "RPM - Payments - Deposits Search - Deposite Date Range fields accespt '01.1.2014'")
	@Parameters({"email", "password"})
	public void testRPM_644(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_644 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");

		logger.info("*** Step 4 Actions: - Click Deposit Date Range radio button and enter '01.1.2014' into the Deposit Date Range 'From' field and Tab off");
		depositSearch.depositDateRangeCheckBox().click();
		String validStr2 = "01.1.2014";
		depositSearch.setDepositDateRangeFrom(validStr2);
		
		logger.info("*** Step 4 Expected Results: - Verify that '01/01/2014' populate in the field");
		Assert.assertEquals(depositSearch.depositDateRangeFromInput().getAttribute("value"), "01/01/2014");
		
		logger.info("*** Step 5 Actions: - Click Search button");	
		depositSearch.searchBtn().click();
		
		logger.info("*** Step 5 Expected Results: - Verify that Deposite Search Results window shows");
		switchToPopupWin();			
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		logger.info("*** Step 6 Actions: - Click 'New Search' button");		
		depositSearchResults.newResearchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that it's on the Deposit Search window");
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 7 Actions: - Click Deposit Date Range radio button and enter '01.1.2014' into the Deposit Date Range 'To' field and Tab off");
		depositSearch.depositDateRangeCheckBox().click();
		depositSearch.setDepositDateRangeTo(validStr2);
		
		logger.info("*** Step 7 Expected Results: - Verify that '01/01/2014' populate in the field");
		Assert.assertEquals(depositSearch.depositDateRangeToInput().getAttribute("value"), "01/01/2014");
		
		logger.info("*** Step 8 Actions: - Click Search button");	
		depositSearch.searchBtn().click();
		
		logger.info("*** Step 8 Expected Results: - Verify that Deposite Search Results window shows");
		switchToPopupWin();			
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		driver.close();
    }

	@Test(priority = 1, description = "RPM - Payments - Deposits Search - Deposite Date Range fields accespt '01/01/2014'")
	@Parameters({"email", "password"})
	public void testRPM_645(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_645 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");

		logger.info("*** Step 4 Actions: - Click Deposit Date Range radio button and enter '01/01/2014' into the Deposit Date Range 'From' field and Tab off");
		depositSearch.depositDateRangeCheckBox().click();
		String validStr3 = "01/01/2014";
		depositSearch.setDepositDateRangeFrom(validStr3);
		
		logger.info("*** Step 4 Expected Results: - Verify that '01/01/2014' populate in the field");
		Assert.assertEquals(depositSearch.depositDateRangeFromInput().getAttribute("value"), "01/01/2014");
		
		logger.info("*** Step 5 Actions: - Click Search button");	
		depositSearch.searchBtn().click();
		
		logger.info("*** Step 5 Expected Results: - Verify that Deposite Search Results window shows");
		switchToPopupWin();		
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		logger.info("*** Step 6 Actions: - Click 'New Search' button");
		depositSearchResults.newResearchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that it's on the Deposit Search window");
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 7 Actions: - Click Deposit Date Range radio button and enter '01/01/2014' into the Deposit Date Range 'To' field and Tab off");
		depositSearch.depositDateRangeCheckBox().click();
		depositSearch.setDepositDateRangeTo(validStr3);
		
		logger.info("*** Step 7 Expected Results: - Verify that '01/01/2014' populate in the field");
		Assert.assertEquals(depositSearch.depositDateRangeToInput().getAttribute("value"), "01/01/2014");
		
		logger.info("*** Step 8 Actions: - Click Search button");	
		depositSearch.searchBtn().click();
		
		logger.info("*** Step 8 Expected Results: - Verify that Deposite Search Results window shows");
		switchToPopupWin();			
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");

		driver.close();
    }

	@Test(priority = 1, description = "RPM - Payments - Deposits Search - Deposite Date Range fields accespt 't'")
	@Parameters({"email", "password"})
	public void testRPM_646(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_646 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");

		logger.info("*** Step 4 Actions: - Click Deposit Date Range radio button and enter 't' into the Deposit Date Range 'From' field and Tab off");
		depositSearch.depositDateRangeCheckBox().click();
		String validStr4 = "t";
		depositSearch.setDepositDateRangeFrom(validStr4);
		
		logger.info("*** Step 4 Expected Results: - Verify that 'MM/DD/YYYY' equal to the current date should populate the field");
		timeStamp = new TimeStamp();
		String currentDate = timeStamp.getCurrentDate("M/d/yyyy");  //"t" and tab out returns 5/1/2015 date format
		Assert.assertEquals(depositSearch.depositDateRangeFromInput().getAttribute("value"), currentDate);
		
		logger.info("*** Step 5 Actions: - Click Search button");	
		depositSearch.searchBtn().click();
		
		logger.info("*** Step 5 Expected Results: - Verify that Deposite Search Results window shows");
		switchToPopupWin();			
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		logger.info("*** Step 6 Actions: - Click 'New Search' button");
		depositSearchResults.newResearchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that it's on the Deposit Search window");
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 7 Actions: - Click Deposit Date Range radio button and enter 't' into the Deposit Date Range 'To' field and Tab off");
		depositSearch.depositDateRangeCheckBox().click();
		depositSearch.setDepositDateRangeTo(validStr4);
		
		logger.info("*** Step 7 Expected Results: - Verify that 'MM/DD/YYYY' equal to the current date should populate the field");
		Assert.assertEquals(depositSearch.depositDateRangeToInput().getAttribute("value"), currentDate);
		
		logger.info("*** Step 8 Actions: - Click Search button");	
		depositSearch.searchBtn().click();
		
		logger.info("*** Step 8 Expected Results: - Verify that Deposite Search Results window shows");
		switchToPopupWin();			
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		driver.close();
    }	

	@Test(priority = 1, description = "RPM - Payments - Deposits Search - Deposite Date Range fields accespt 'y'")
	@Parameters({"email", "password"})
	public void testRPM_647(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_647 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 4 Actions: - Click Deposit Date Range radio button and enter 'y' into the Deposit Date Range 'From' field and Tab off");
		depositSearch.depositDateRangeCheckBox().click();
		String validStr5 = "y";
		depositSearch.setDepositDateRangeFrom(validStr5);
		
		logger.info("*** Step 4 Expected Results: - Verify that 'MM/DD/YYYY' populates the field should be equal to the date that was yesterday ");
		timeStamp = new TimeStamp();
		String previousDate1 = timeStamp.getPreviousDate("M/d/yyyy", 1);  //"t" and tab out returns 5/1/2015 date format
		Assert.assertEquals(depositSearch.depositDateRangeFromInput().getAttribute("value"), previousDate1);
		
		logger.info("*** Step 5 Actions: - Click Search button");	
		depositSearch.searchBtn().click();
		
		logger.info("*** Step 5 Expected Results: - Verify that Deposite Search Results window shows");
		switchToPopupWin();		
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		logger.info("*** Step 6 Actions: - Click 'New Search' button");
		depositSearchResults.newResearchBtn().click();
		switchToPopupWin();		
		
		logger.info("*** Step 6 Expected Results: - Verify that it's on the Deposit Search window");
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 7 Actions: - Click Deposit Date Range radio button and enter 'y' into the Deposit Date Range 'To' field and Tab off");
		depositSearch.depositDateRangeCheckBox().click();
		depositSearch.setDepositDateRangeTo(validStr5);
		
		logger.info("*** Step 7 Expected Results: - Verify that 'MM/DD/YYYY' populates the field should be equal to the date that was yesterday.");
		Assert.assertEquals(depositSearch.depositDateRangeToInput().getAttribute("value"), previousDate1);
		
		logger.info("*** Step 8 Actions: - Click Search button");	
		depositSearch.searchBtn().click();
		
		logger.info("*** Step 8 Expected Results: - Verify that Deposite Search Results window shows");
		switchToPopupWin();			
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		driver.close();
    }		

	@Test(priority = 1, description = "RPM - Payments - Deposits Search - Deposite Date Range fields accespt 't-10'")
	@Parameters({"email", "password"})
	public void testRPM_648(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_648 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");

		logger.info("*** Step 4 Actions: - Click Deposit Date Range radio button and enter 't-10' into the Deposit Date Range 'From' field and Tab off");
		depositSearch.depositDateRangeCheckBox().click();
		String validStr6 = "t-10";
		depositSearch.setDepositDateRangeFrom(validStr6);
		
		logger.info("*** Step 4 Expected Results: - Verify that 'MM/DD/YYYY' populates the field should be equal to the date that was 10 days ago ");
		timeStamp = new TimeStamp();
		String previousDate2 = timeStamp.getPreviousDate("M/d/yyyy", 10);  //"t" and tab out returns 5/1/2015 date format
		Assert.assertEquals(depositSearch.depositDateRangeFromInput().getAttribute("value"), previousDate2);
		
		logger.info("*** Step 5 Actions: - Click Search button");	
		depositSearch.searchBtn().click();
		
		logger.info("*** Step 5 Expected Results: - Verify that Deposite Search Results window shows");
		switchToPopupWin();			
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		logger.info("*** Step 6 Actions: - Click 'New Search' button");
		depositSearchResults.newResearchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that it's on the Deposit Search window");
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 7 Actions: - Click Deposit Date Range radio button and enter 't-10' into the Deposit Date Range 'To' field and Tab off");
		depositSearch.depositDateRangeCheckBox().click();
		depositSearch.setDepositDateRangeTo(validStr6);
		
		logger.info("*** Step 7 Expected Results: - Verify that 'MM/DD/YYYY' populates the field should be equal to the date that was 10 days ago.");
		Assert.assertEquals(depositSearch.depositDateRangeToInput().getAttribute("value"), previousDate2);
		
		logger.info("*** Step 8 Actions: - Click Search button");	
		depositSearch.searchBtn().click();
		
		logger.info("*** Step 8 Expected Results: - Verify that Deposite Search Results window shows");
		switchToPopupWin();			
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		driver.close();
    }		

	@Test(priority = 1, description = "RPM - Payments - Deposits JSP - Deposit Search JSP - Invalid value 30/1/2014 in Deposite Date Range fields")
	@Parameters({"email", "password"})
	public void testRPM_650(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_650 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
	
		logger.info("*** Step 4 Actions: -  Click Deposit Date Range radio button and enter '30/1/2014' into the Deposit Date Range 'From' field and Tab off");
		depositSearch.depositDateRangeCheckBox().click();			
		String invalidStr2 = "30/1/2014";
		depositSearch.setDepositDateRangeFrom(invalidStr2);
		
		logger.info("*** Step 4 Expected Results: - A popup should come up and say 'Invalid date format.'");		
		Assert.assertTrue(closeAlertAndGetItsText(true).contains("Invalid date format"), "        A popup should come up and say 'Invalid date format.'");
		
		logger.info("*** Step 5 Actions: - Clear the field and enter '30/1/2014' into the Deposit Date Range 'To' Field. Tab off.");
		depositSearch.depositDateRangeFromInput().clear();
		depositSearch.setDepositDateRangeTo(invalidStr2);
		
		logger.info("*** Step 5 Expected Results: - A popup should come up and say 'Invalid date format.'");		
		Assert.assertTrue(closeAlertAndGetItsText(true).contains("Invalid date format"), "        A popup should come up and say 'Invalid date format.'");
				
		driver.close();
    }	
	
	@Test(priority = 1, description = "RPM - Payments - Deposits JSP - Deposit Search JSP - Invalid value adsfadf in Deposite Date Range fields")
	@Parameters({"email", "password"})
	public void testRPM_651(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_651 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");

		logger.info("*** Step 4 Actions: - Click Deposit Date Range radio button and enter 'adsfadf' into the Deposit Date Range 'From' field and Tab off");
		depositSearch.depositDateRangeCheckBox().click();		
		String invalidStr3 = "adsfadf";
		depositSearch.setDepositDateRangeFrom(invalidStr3);
		
		logger.info("*** Step 4 Expected Results: - A popup should come up explaining the rules of the date field.");		
		Assert.assertTrue(closeAlertAndGetItsText(true).contains("Date fields"), "        A popup should come up explaining the rules of the date field.");
		
		logger.info("*** Step 5 Actions: - Clear the field and enter 'adsfadf' into the Deposit Date Range 'To' Field. Tab off.");
		depositSearch.depositDateRangeFromInput().clear();
		depositSearch.setDepositDateRangeTo(invalidStr3);
		
		logger.info("*** Step 5 Expected Results: - A popup should come up explaining the rules of the date field.");		
		Assert.assertTrue(closeAlertAndGetItsText(true).contains("Date fields"), "        A popup should come up explaining the rules of the date field.");
				
		driver.close();
    }

	@Test(priority = 1, description = "RPM - Payments - Deposits JSP - Deposit Search JSP - Invalid value 123123 in Deposite Date Range fields")
	@Parameters({"email", "password"})
	public void testRPM_652(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_652 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");

		logger.info("*** Step 4 Actions: - Click Deposit Date Range radio button and enter '123123' into the Deposit Date Range 'From' field and Tab off");
		depositSearch.depositDateRangeCheckBox().click();				
		String invalidStr4 = "123123";
		depositSearch.setDepositDateRangeFrom(invalidStr4);
		
		logger.info("*** Step 4 Expected Results: - A popup should come up and say 'Invalid date format.'");		
		Assert.assertTrue(closeAlertAndGetItsText(true).contains("Invalid date format"), "        A popup should come up and say 'Invalid date format.'");
		
		logger.info("*** Step 5 Actions: - Clear the field and enter '123123' into the Deposit Date Range 'To' Field. Tab off.");
		depositSearch.depositDateRangeFromInput().clear();
		depositSearch.setDepositDateRangeTo(invalidStr4);
		
		logger.info("*** Step 5 Expected Results: - A popup should come up and say 'Invalid date format.'");		
		Assert.assertTrue(closeAlertAndGetItsText(true).contains("Invalid date format"), "        A popup should come up and say 'Invalid date format.'");
		
		driver.close();
    }

	@Test(priority = 1, description = "RPM - Payments - Deposits JSP - Deposit Search JSP - Invalid value 01/01/114 in Deposite Date Range fields")
	@Parameters({"email", "password"})
	public void testRPM_653(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_653 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
	
		logger.info("*** Step 4 Actions: -  Click Deposit Date Range radio button and enter '01/01/100' into the Deposit Date Range 'From' field and Tab off");
		depositSearch.depositDateRangeCheckBox().click();		
		String invalidStr5 = "01/01/100";
		depositSearch.setDepositDateRangeFrom(invalidStr5);
		
		logger.info("*** Step 4 Expected Results: - A popup should come up and say 'Invalid date format.'");		
		Assert.assertTrue(closeAlertAndGetItsText(true).contains("Invalid date format"), "        A popup should come up and say 'Invalid date format.'");
		
		logger.info("*** Step 5 Actions: - Clear the field and enter '01/01/100' into the Deposit Date Range 'To' Field. Tab off.");
		depositSearch.depositDateRangeFromInput().clear();
		depositSearch.setDepositDateRangeTo(invalidStr5);
		
		logger.info("*** Step 5 Expected Results: - A popup should come up and say 'Invalid date format.'");		
		Assert.assertTrue(closeAlertAndGetItsText(true).contains("Invalid date format"), "        A popup should come up and say 'Invalid date format.'");
						
		driver.close();
    }
	
	@Test(priority = 1, description = "RPM - Payments - Deposits JSP - Deposit Search JSP - Invalid value asd in Deposit Amount Range fields")
	@Parameters({"email", "password"})
	public void testRPM_655(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_655 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");

		logger.info("*** Step 4 Actions: - Enter 'asd' to the From: Deposit Amount Range field and Tab off");				
		String invalidStr2 = "asd";
		depositSearch.setDepositAmountRangeFrom(invalidStr2);
		
		logger.info("*** Step 4 Expected Results: - A popup should come up explaining the rules of the amount field.");		
		Assert.assertTrue(closeAlertAndGetItsText(true).contains("This field must be a positive amount"), "        A popup should come up explaining the rules of the amount field.");
		
		logger.info("*** Step 5 Actions: - Clear the field and enter 'asd' into the Deposit Amount Range 'To' Field. Tab off.");
		depositSearch.depositAmountRangeFromInput().clear();
		depositSearch.setDepositAmountRangeTo(invalidStr2);
		
		logger.info("*** Step 5 Expected Results: - A popup should come up explaining the rules of the amount field.");		
		Assert.assertTrue(closeAlertAndGetItsText(true).contains("This field must be a positive amount"), "        A popup should come up explaining the rules of the amount field.");
		
		driver.close();
    }	

	@Test(priority = 1, description = "Payment - Payments - Deposit Search - Invalid value 1.1.1 in Deposit Amount Range fields")
	@Parameters({"email", "password"})
	public void testRPM_656(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_656 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 4 Actions: - Enter '1.1.1' into the Deposit Amount Range 'From' field and Tab off");		
		String invalidStr3 = "1.1.1";
		depositSearch.setDepositAmountRangeFrom(invalidStr3);
		
		logger.info("*** Step 4 Expected Results: - A popup should come up explaining the rules of the amount field.");		
		Assert.assertTrue(closeAlertAndGetItsText(true).contains("This field must be a positive amount"), "        A popup should come up explaining the rules of the amount field.");
		
		logger.info("*** Step 5 Actions: - Clear the field and enter '1.1.1' into the Deposit Amount Range 'To' Field. Tab off.");
		depositSearch.depositAmountRangeFromInput().clear();
		depositSearch.setDepositAmountRangeTo(invalidStr3);
		
		logger.info("*** Step 5 Expected Results: - A popup should come up explaining the rules of the amount field.");		
		Assert.assertTrue(closeAlertAndGetItsText(true).contains("This field must be a positive amount"), "        A popup should come up explaining the rules of the amount field.");
		
		driver.close();
    }
	
	@Test(priority = 1, description = "Payment - Payments - Deposite Search - Valid value 0 in Deposit Amount Range fields")
	@Parameters({"email", "password"})
	public void testRPM_657(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_657 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");

		logger.info("*** Step 4 Actions: - Select All in the Deposit Period dropdown, enter '0' into the Deposit Amount Range 'From:' field and Tab off and click Search");
		//"0" = All
		depositSearch.setDepositPeriod("0");
		String validStr2 = "0";
		depositSearch.setDepositAmountRangeFrom(validStr2);
		depositSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Verify that search result is matching the sql query that contains 'dep.dep_amt>=0' was sent in the log..");
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		ArrayList<String> depList = daoManagerXifinRpm.getDepIdFromDEPByDepAmtFrom(validStr2, testDb);
		String depId = depList.get(0);
		Assert.assertTrue(getColumnValue(depositSearchResults.depositLedgerTable(), depId), "        Deposit ID " + depId + " should display in the Deposit Search Results.");
		
		logger.info("*** Step 5 Actions: - Once on the Deposit Search Results, click 'New Search'");
		depositSearchResults.newResearchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - The Deposit Search JSP should come back up.");
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 6 Actions: - Select All in the Deposit Period dropdown, enter '0' into the Deposit Amount Range 'To:' field and Tab off and click Search");
		//"0" = All
		depositSearch.setDepositPeriod("0");		
		depositSearch.setDepositAmountRangeTo(validStr2);
		depositSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that search result is matching the sql query that contains 'dep.dep_amt<=0' was sent in the log..");		
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		depList = daoManagerXifinRpm.getDepIdFromDEPByDepAmtTo(validStr2, testDb);
		depId = depList.get(0);
		Assert.assertTrue(getColumnValue(depositSearchResults.depositLedgerTable(), depId), "        Deposit ID " + depId + " should display in the Deposit Search Results.");
		
		driver.close();
    }

	@Test(priority = 1, description = "Payment - Payments - Deposite Search - Valid value 123.123 in Deposit Amount Range fields")
	@Parameters({"email", "password"})
	public void testRPM_658(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_658 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
	
		logger.info("*** Step 4 Actions: - Select All in the Deposit Period dropdown, enter '123.123' into the Deposit Amount Range 'From:' field and Tab off and click Search");
		//"0" = All
		depositSearch.setDepositPeriod("0");
		String validStr3 = "123.123";
		depositSearch.setDepositAmountRangeFrom(validStr3);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Deposit Amount Range 'From:' field is truncated to '123.12'.");
		String truncatedValidStr3 = "123.12";
		Assert.assertEquals(depositSearch.depositAmountRangeFromInput().getAttribute("value"), truncatedValidStr3);
		
		logger.info("*** Step 5 Actions: - Click Search.");
		depositSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that search result is matching the sql query that contains 'dep.dep_amt>=123.12' was sent in the log.");
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		ArrayList<String> depList = daoManagerXifinRpm.getDepIdFromDEPByDepAmtFrom(truncatedValidStr3, testDb);
		String depId = depList.get(0);
		Assert.assertTrue(getColumnValue(depositSearchResults.depositLedgerTable(), depId), "        Deposit ID " + depId + " should display in the Deposit Search Results.");
		
		logger.info("*** Step 6 Actions: - Once on the Deposit Search Results, click 'New Search'");
		depositSearchResults.newResearchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - The Deposit Search JSP should come back up.");
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		logger.info("*** Step 7 Actions: - Select All in the Deposit Period dropdown, enter '123.123' into the Deposit Amount Range 'To:' field and Tab off and click Search");
		//"0" = All
		depositSearch.setDepositPeriod("0");		
		depositSearch.setDepositAmountRangeTo(validStr3);
		
		logger.info("*** Step 7 Expected Results: - Verify that the Deposit Amount Range 'From:' field is truncated to '123.12'.");		
		Assert.assertEquals(depositSearch.depositAmountRangeToInput().getAttribute("value"), truncatedValidStr3);
		
		logger.info("*** Step 8 Actions: - Click Search.");
		depositSearch.searchBtn().click();
		switchToPopupWin();
				
		logger.info("*** Step 8 Expected Results: - Verify that search result is matching the sql query that contains 'dep.dep_amt<=123.12' was sent in the log..");		
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		depList = daoManagerXifinRpm.getDepIdFromDEPByDepAmtTo(truncatedValidStr3, testDb);
		depId = depList.get(0);
		Assert.assertTrue(getColumnValue(depositSearchResults.depositLedgerTable(), depId), "        Deposit ID " + depId + " should display in the Deposit Search Results.");
				
		driver.close();
    }
	
	@Test(priority = 1, description = "Payment - Payments - Deposite Search - From value equals To value in Deposit Amount Range fields")
	@Parameters({"email", "password"})
	public void testRPM_659(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_659 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		String amount1 = "100";

		logger.info("*** Step 4 Actions: - Set Deposite Period to 'All'. Enter '100' into the 'Deposit Amount Range From:' field. Tab off. Enter '100' date into the 'To:' field. Tab off. And click Search");
		//'0' = All
		depositSearch.setDepositPeriod("0");
		
		depositSearch.setDepositAmountRangeFrom(amount1);
		depositSearch.setDepositAmountRangeTo(amount1);
		depositSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Verify that the search results match the sql query with fields like  AND dep.dep_amt >= 100.0 AND dep.dep_amt <= 100.0 in the log.");
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		ArrayList<String> depList = daoManagerXifinRpm.getDepIdFromDEPByDepAmountRange(amount1, amount1, testDb);
		String depId = depList.get(0);
		Assert.assertTrue(getColumnValue(depositSearchResults.depositLedgerTable(), depId), "        Deposit ID " + depId + " should display in the Deposit Search Results.");
			
		driver.close();
    }
	
	@Test(priority = 1, description = "Payment - Payments - Deposite Search - From value lower than To in Deposit Amount Range fields")
	@Parameters({"email", "password"})
	public void testRPM_660(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_660 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Payments - Deposit page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		//Go to Deposits Content jsp
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToDepositsContentLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Deposits page");		
		deposits = new Deposits(driver, wait);
		Assert.assertTrue(deposits.depositLedgerBtn().isDisplayed(), "        Deposit Ledger button should show.");
		
		logger.info("*** Step 3 Actions: - Click Deposit Ledger button");		
		deposits.depositLedgerBtn().click();		
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that it's on the Deposit Search window");
		depositSearch = new DepositSearch(driver);		
		Assert.assertTrue(depositSearch.depositDateRangeCheckBox().isDisplayed(), "        Deposit Date Range checkbox should show.");
		
		String amount1 = "100";
		String amount2 = "200";

		logger.info("*** Step 4 Actions: - Set Deposite Period to 'All'. Enter '200' into the 'Deposit Amount Range From:' field. Tab off. Enter '100' into the 'To:' field. Tab off. And click Search");
		//'0' = All
		depositSearch.setDepositPeriod("0");
		
		depositSearch.setDepositAmountRangeFrom(amount2);
		depositSearch.setDepositAmountRangeTo(amount1);
		depositSearch.searchBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Verify that the search results match the sql query with fields like  AND dep.dep_amt >= 200.0 AND dep.dep_amt <= 100.0 in the log.");
		depositSearchResults = new DepositSearchResults(driver);
		Assert.assertTrue(depositSearchResults.depositLedgerTable().isDisplayed(), "        Deposit Search Results window should show.");
		
		//In this case, not search result returned
		ArrayList<String> depList = daoManagerXifinRpm.getDepIdFromDEPByDepAmountRange(amount2, amount1, testDb);
		Assert.assertEquals(depList.size(), 0);		
		Assert.assertEquals(getTableTotalRowSize(depositSearchResults.depositLedgerTable()), 1);

		driver.close();
    }
	
	@Test(priority = 1, description = "RPM - Payment - Payment Search - Payor Search Results - PayorId")
	@Parameters({"email", "password", "payorId"})
	public void testRPM_736(String email, String password, String payorId) throws Exception {
    	logger.info("***** Testing - testRPM_736 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to Payment Tab > Payment Search page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();				
		
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToPaymentSearchLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Payment Search page shows");
		paymentSearch = new PaymentSearch(driver, wait);
		Assert.assertTrue(paymentSearch.checkNumInput().isDisplayed(), "        Check # Input field in Payment Search page should show.");
		
		logger.info("*** Step 3 Actions: - Click Payor Search button");
		paymentSearch.payorSearchhBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Search popup window shows");
		payorSearch = new PayorSearch(driver);
		Assert.assertTrue(isElementPresent(payorSearch.payorIdInput(), 5));
		
		logger.info("*** Step 4 Actions: - Submit payorId in the PayorId input");
		payorSearch.searchPayorId(payorId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Payor Search Results popup window shows");
		payorSearchResults = new PayorSearchResults(driver);
		Assert.assertTrue(payorSearchResults.pageTitleText().getText().trim().contains("Payor Search Results"), "        Payor Search Results page title should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that the PayorId ABC displays in te PayorId column");
		Assert.assertTrue(getColumnValue(payorSearchResults.pyrSrchTable(), payorId));
		
    }
	
	@Test(priority = 1, description = "RPM - Payment - Payment Search - Payor Search Page - Displays")
	@Parameters({"email", "password"})
	public void testRPM_737(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_737 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to Payment Tab > Payment Search page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();				
		
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToPaymentSearchLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Payment Search page shows");
		paymentSearch = new PaymentSearch(driver, wait);
		Assert.assertTrue(paymentSearch.checkNumInput().isDisplayed(), "        Check # Input field in Payment Search page should show.");
		
		logger.info("*** Step 3 Actions: - Click Payor Search button");
		paymentSearch.payorSearchhBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Search popup window shows");
		payorSearch = new PayorSearch(driver);
		Assert.assertTrue(isElementPresent(payorSearch.payorIdInput(), 5));
    }
	
	@Test(priority = 1, description = "RPM - Payment - Payment Search - Payor Search Results - Payor Name")
	@Parameters({"email", "password", "payorId", "payorName"})
	public void testRPM_738(String email, String password, String payorId, String payorName) throws Exception {
    	logger.info("***** Testing - testRPM_738 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to Payment Tab > Payment Search page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();				
		
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToPaymentSearchLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Payment Search page shows");
		paymentSearch = new PaymentSearch(driver, wait);
		Assert.assertTrue(paymentSearch.checkNumInput().isDisplayed(), "        Check # Input field in Payment Search page should show.");
		
		logger.info("*** Step 3 Actions: - Click Payor Search button");
		paymentSearch.payorSearchhBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Search popup window shows");
		payorSearch = new PayorSearch(driver);
		Assert.assertTrue(isElementPresent(payorSearch.payorIdInput(), 5));
		
		logger.info("*** Step 4 Actions: - Submit payorId in the PayorId input");
		payorSearch.searchPayorId(payorId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Payor Search Results popup window shows");
		payorSearchResults = new PayorSearchResults(driver);
		Assert.assertTrue(payorSearchResults.pageTitleText().getText().trim().contains("Payor Search Results"), "        Payor Search Results page title should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that the payor Name ABC Insurance displays in te Payor Name column");
		Assert.assertTrue(getColumnValue(payorSearchResults.pyrSrchTable(), payorName));
		
    }
	
	@Test(priority = 1, description = "RPM - Payment - Payment Search - Payor Search Results - Payor Denial Tbl Descr")
	@Parameters({"email", "password", "payorId", "payorDenial"})
	public void testRPM_739(String email, String password, String payorId, String payorDenial) throws Exception {
    	logger.info("***** Testing - testRPM_739 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to Payment Tab > Payment Search page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();				
		
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToPaymentSearchLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Payment Search page shows");
		paymentSearch = new PaymentSearch(driver, wait);
		Assert.assertTrue(paymentSearch.checkNumInput().isDisplayed(), "        Check # Input field in Payment Search page should show.");
		
		logger.info("*** Step 3 Actions: - Click Payor Search button");
		paymentSearch.payorSearchhBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Search popup window shows");
		payorSearch = new PayorSearch(driver);
		Assert.assertTrue(isElementPresent(payorSearch.payorIdInput(), 5));
		
		logger.info("*** Step 4 Actions: - Submit payorId in the PayorId input");
		payorSearch.searchPayorId(payorId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Payor Search Results popup window shows");
		payorSearchResults = new PayorSearchResults(driver);
		Assert.assertTrue(payorSearchResults.pageTitleText().getText().trim().contains("Payor Search Results"), "        Payor Search Results page title should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that the payor Denial Tbl Descr 4010 GENERIC displays in te Payor Denial Tbl Descr column");
		Assert.assertTrue(getColumnValue(payorSearchResults.pyrSrchTable(), payorDenial));
		
    }
	
	@Test(priority = 1, description = "RPM - Payment - Payment Search - Payor Search Results - Payor City")
	@Parameters({"email", "password", "payorId", "payorCity"})
	public void testRPM_740(String email, String password, String payorId, String payorCity) throws Exception {
    	logger.info("***** Testing - testRPM_740 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to Payment Tab > Payment Search page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();				
		
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToPaymentSearchLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Payment Search page shows");
		paymentSearch = new PaymentSearch(driver, wait);
		Assert.assertTrue(paymentSearch.checkNumInput().isDisplayed(), "        Check # Input field in Payment Search page should show.");
		
		logger.info("*** Step 3 Actions: - Click Payor Search button");
		paymentSearch.payorSearchhBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Search popup window shows");
		payorSearch = new PayorSearch(driver);
		Assert.assertTrue(isElementPresent(payorSearch.payorIdInput(), 5));
		
		logger.info("*** Step 4 Actions: - Submit payorId in the PayorId input");
		payorSearch.searchPayorId(payorId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Payor Search Results popup window shows");
		payorSearchResults = new PayorSearchResults(driver);
		Assert.assertTrue(payorSearchResults.pageTitleText().getText().trim().contains("Payor Search Results"), "        Payor Search Results page title should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that the payor City SAN DIEGO displays in payor city column");
		Assert.assertTrue(getColumnValue(payorSearchResults.pyrSrchTable(), payorCity));
		
    }
	
	@Test(priority = 1, description = "RPM - Payment - Payment Search - Payor Search Results - Payor Zip")
	@Parameters({"email", "password", "payorId", "payorZip"})
	public void testRPM_741(String email, String password, String payorId, String payorZip) throws Exception {
    	logger.info("***** Testing - testRPM_741 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to Payment Tab > Payment Search page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();				
		
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToPaymentSearchLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Payment Search page shows");
		paymentSearch = new PaymentSearch(driver, wait);
		Assert.assertTrue(paymentSearch.checkNumInput().isDisplayed(), "        Check # Input field in Payment Search page should show.");
		
		logger.info("*** Step 3 Actions: - Click Payor Search button");
		paymentSearch.payorSearchhBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Search popup window shows");
		payorSearch = new PayorSearch(driver);
		Assert.assertTrue(isElementPresent(payorSearch.payorIdInput(), 5));
		
		logger.info("*** Step 4 Actions: - Submit payorId in the PayorId input");
		payorSearch.searchPayorId(payorId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Payor Search Results popup window shows");
		payorSearchResults = new PayorSearchResults(driver);
		Assert.assertTrue(payorSearchResults.pageTitleText().getText().trim().contains("Payor Search Results"), "        Payor Search Results page title should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that the payor Zip 92121 displays in payor zip column");
		Assert.assertTrue(getColumnValue(payorSearchResults.pyrSrchTable(), payorZip));
		
    }
	
	@Test(priority = 1, description = "RPM - Payment - Payment Search - Payor Search Results - Payor Country")
	@Parameters({"email", "password", "payorId", "payorCountry"})
	public void testRPM_742(String email, String password, String payorId, String payorCountry) throws Exception {
    	logger.info("***** Testing - testRPM_742 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to Payment Tab > Payment Search page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();				
		
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToPaymentSearchLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Payment Search page shows");
		paymentSearch = new PaymentSearch(driver, wait);
		Assert.assertTrue(paymentSearch.checkNumInput().isDisplayed(), "        Check # Input field in Payment Search page should show.");
		
		logger.info("*** Step 3 Actions: - Click Payor Search button");
		paymentSearch.payorSearchhBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Search popup window shows");
		payorSearch = new PayorSearch(driver);
		Assert.assertTrue(isElementPresent(payorSearch.payorIdInput(), 5));
		
		logger.info("*** Step 4 Actions: - Submit payorId in the PayorId input");
		payorSearch.searchPayorId(payorId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Payor Search Results popup window shows");
		payorSearchResults = new PayorSearchResults(driver);
		Assert.assertTrue(payorSearchResults.pageTitleText().getText().trim().contains("Payor Search Results"), "        Payor Search Results page title should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that the payor country USA displays in payor country column");
		Assert.assertTrue(getColumnValue(payorSearchResults.pyrSrchTable(), payorCountry));
		
    }
	
	@Test(priority = 1, description = "RPM - Payment - Payment Search - Payor Search Results - Payor Group")
	@Parameters({"email", "password", "payorId", "payorGroup"})
	public void testRPM_743(String email, String password, String payorId, String payorGroup) throws Exception {
    	logger.info("***** Testing - testRPM_743 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to Payment Tab > Payment Search page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();				
		
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToPaymentSearchLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Payment Search page shows");
		paymentSearch = new PaymentSearch(driver, wait);
		Assert.assertTrue(paymentSearch.checkNumInput().isDisplayed(), "        Check # Input field in Payment Search page should show.");
		
		logger.info("*** Step 3 Actions: - Click Payor Search button");
		paymentSearch.payorSearchhBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Search popup window shows");
		payorSearch = new PayorSearch(driver);
		Assert.assertTrue(isElementPresent(payorSearch.payorIdInput(), 5));
		
		logger.info("*** Step 4 Actions: - Submit payorId in the PayorId input");
		payorSearch.searchPayorId(payorId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Payor Search Results popup window shows");
		payorSearchResults = new PayorSearchResults(driver);
		Assert.assertTrue(payorSearchResults.pageTitleText().getText().trim().contains("Payor Search Results"), "        Payor Search Results page title should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that the payor group contracted displays in payor group column");
		Assert.assertTrue(getColumnValue(payorSearchResults.pyrSrchTable(), payorGroup));
		
    }
	
	@Test(priority = 1, description = "RPM - Payment - Payment Search - Payor Search Results - Payor Suspended")
	@Parameters({"email", "password", "payorId", "payorSuspended"})
	public void testRPM_744(String email, String password, String payorId, String payorSuspended) throws Exception {
    	logger.info("***** Testing - testRPM_744 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to Payment Tab > Payment Search page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();				
		
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToPaymentSearchLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Payment Search page shows");
		paymentSearch = new PaymentSearch(driver, wait);
		Assert.assertTrue(paymentSearch.checkNumInput().isDisplayed(), "        Check # Input field in Payment Search page should show.");
		
		logger.info("*** Step 3 Actions: - Click Payor Search button");
		paymentSearch.payorSearchhBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Search popup window shows");
		payorSearch = new PayorSearch(driver);
		Assert.assertTrue(isElementPresent(payorSearch.payorIdInput(), 5));
		
		logger.info("*** Step 4 Actions: - Submit payorId in the PayorId input");
		payorSearch.searchPayorId(payorId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Payor Search Results popup window shows");
		payorSearchResults = new PayorSearchResults(driver);
		Assert.assertTrue(payorSearchResults.pageTitleText().getText().trim().contains("Payor Search Results"), "        Payor Search Results page title should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that the payor suspended no displays in payor suspended column");
		Assert.assertTrue(getColumnValue(payorSearchResults.pyrSrchTable(), payorSuspended));
		
    }
	
	@Test(priority = 1, description = "RPM - Payment - Payment Search - Payor Search Results - Payor Clearinghouse Id")
	@Parameters({"email", "password", "payorId", "payorClearinghouseId"})
	public void testRPM_745(String email, String password, String payorId, String payorClearinghouseId) throws Exception {
    	logger.info("***** Testing - testRPM_745 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to Payment Tab > Payment Search page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();				
		
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToPaymentSearchLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Payment Search page shows");
		paymentSearch = new PaymentSearch(driver, wait);
		Assert.assertTrue(paymentSearch.checkNumInput().isDisplayed(), "        Check # Input field in Payment Search page should show.");
		
		logger.info("*** Step 3 Actions: - Click Payor Search button");
		paymentSearch.payorSearchhBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Search popup window shows");
		payorSearch = new PayorSearch(driver);
		Assert.assertTrue(isElementPresent(payorSearch.payorIdInput(), 5));
		
		logger.info("*** Step 4 Actions: - Submit payorId in the PayorId input");
		payorSearch.searchPayorId(payorId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Payor Search Results popup window shows");
		payorSearchResults = new PayorSearchResults(driver);
		Assert.assertTrue(payorSearchResults.pageTitleText().getText().trim().contains("Payor Search Results"), "        Payor Search Results page title should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that the payor clearninghouse Id abc displays in payor clearninghouse id column");
		Assert.assertTrue(getColumnValue(payorSearchResults.pyrSrchTable(), payorClearinghouseId));
		
    }
	
	@Test(priority = 1, description = "RPM - Payment - Payment Search - Payor Search Results - Payor Address 1")
	@Parameters({"email", "password", "payorId", "addr1"})
	public void testRPM_746(String email, String password, String payorId, String addr1) throws Exception {
    	logger.info("***** Testing - testRPM_746 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to Payment Tab > Payment Search page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();				
		
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToPaymentSearchLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Payment Search page shows");
		paymentSearch = new PaymentSearch(driver, wait);
		Assert.assertTrue(paymentSearch.checkNumInput().isDisplayed(), "        Check # Input field in Payment Search page should show.");
		
		logger.info("*** Step 3 Actions: - Click Payor Search button");
		paymentSearch.payorSearchhBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Search popup window shows");
		payorSearch = new PayorSearch(driver);
		Assert.assertTrue(isElementPresent(payorSearch.payorIdInput(), 5));
		
		logger.info("*** Step 4 Actions: - Submit payorId in the PayorId input");
		payorSearch.searchPayorId(payorId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Payor Search Results popup window shows");
		payorSearchResults = new PayorSearchResults(driver);
		Assert.assertTrue(payorSearchResults.pageTitleText().getText().trim().contains("Payor Search Results"), "        Payor Search Results page title should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that the payor Address 1 123 ABC WAY displays in payor Address 1 column");
		Assert.assertTrue(getColumnValue(payorSearchResults.pyrSrchTable(), addr1));
		
    }
	
	@Test(priority = 1, description = "RPM - Payment - Payment Search - Payor Search Results - Payor Address 2")
	@Parameters({"email", "password", "payorId", "addr2"})
	public void testRPM_747(String email, String password, String payorId, String addr2) throws Exception {
    	logger.info("***** Testing - testRPM_747 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to Payment Tab > Payment Search page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();				
		
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToPaymentSearchLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Payment Search page shows");
		paymentSearch = new PaymentSearch(driver, wait);
		Assert.assertTrue(paymentSearch.checkNumInput().isDisplayed(), "        Check # Input field in Payment Search page should show.");
		
		logger.info("*** Step 3 Actions: - Click Payor Search button");
		paymentSearch.payorSearchhBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Search popup window shows");
		payorSearch = new PayorSearch(driver);
		Assert.assertTrue(isElementPresent(payorSearch.payorIdInput(), 5));
		
		logger.info("*** Step 4 Actions: - Submit payorId in the PayorId input");
		payorSearch.searchPayorId(payorId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Payor Search Results popup window shows");
		payorSearchResults = new PayorSearchResults(driver);
		Assert.assertTrue(payorSearchResults.pageTitleText().getText().trim().contains("Payor Search Results"), "        Payor Search Results page title should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that the payor Address 2 456 ABC WAY displays in payor Address 2 column");
		Assert.assertTrue(getColumnValue(payorSearchResults.pyrSrchTable(), addr2));
		
    }
	
	@Test(priority = 1, description = "RPM - Payment - Payment Search - Payor Search Results - Payor Phone")
	@Parameters({"email", "password", "payorId", "phone"})
	public void testRPM_748(String email, String password, String payorId, String phone) throws Exception {
    	logger.info("***** Testing - testRPM_748 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to Payment Tab > Payment Search page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();				
		
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToPaymentSearchLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Payment Search page shows");
		paymentSearch = new PaymentSearch(driver, wait);
		Assert.assertTrue(paymentSearch.checkNumInput().isDisplayed(), "        Check # Input field in Payment Search page should show.");
		
		logger.info("*** Step 3 Actions: - Click Payor Search button");
		paymentSearch.payorSearchhBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Search popup window shows");
		payorSearch = new PayorSearch(driver);
		Assert.assertTrue(isElementPresent(payorSearch.payorIdInput(), 5));
		
		logger.info("*** Step 4 Actions: - Submit payorId in the PayorId input");
		payorSearch.searchPayorId(payorId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Payor Search Results popup window shows");
		payorSearchResults = new PayorSearchResults(driver);
		Assert.assertTrue(payorSearchResults.pageTitleText().getText().trim().contains("Payor Search Results"), "        Payor Search Results page title should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that the payor Phone (619) 222-3333 displays in payor Phone column");
		Assert.assertTrue(getColumnValue(payorSearchResults.pyrSrchTable(), phone));
		
    }
	
	@Test(priority = 1, description = "RPM - Payment - Payment Search - Payor Search Results - New Search")
	@Parameters({"email", "password", "payorId"})
	public void testRPM_749(String email, String password, String payorId) throws Exception {
    	logger.info("***** Testing - testRPM_749 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to Payment Tab > Payment Search page");	
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPaymentTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();				
		
		paymentNavigation = new PaymentNavigation(driver, config);
		paymentNavigation.navigateToPaymentSearchLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Payment Search page shows");
		paymentSearch = new PaymentSearch(driver, wait);
		Assert.assertTrue(paymentSearch.checkNumInput().isDisplayed(), "        Check # Input field in Payment Search page should show.");
		
		logger.info("*** Step 3 Actions: - Click Payor Search button");
		paymentSearch.payorSearchhBtn().click();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Search popup window shows");
		payorSearch = new PayorSearch(driver);
		Assert.assertTrue(isElementPresent(payorSearch.payorIdInput(), 5));
		
		logger.info("*** Step 4 Actions: - Submit payorId in the PayorId input");
		payorSearch.searchPayorId(payorId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Payor Search Results popup window shows");
		payorSearchResults = new PayorSearchResults(driver);
		Assert.assertTrue(payorSearchResults.pageTitleText().getText().trim().contains("Payor Search Results"), "        Payor Search Results page title should show.");
		
		logger.info("*** Step 5 Actions: - Click New Search");
		payorSearchResults.newSearch();
		
		logger.info("*** Step 5 Expected Results: - Verify that the Payor Search popup window shows");
		Assert.assertTrue(isElementPresent(payorSearch.payorIdInput(), 5));
    }
	
}
