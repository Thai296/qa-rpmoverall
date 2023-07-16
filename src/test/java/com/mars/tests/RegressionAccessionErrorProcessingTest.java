package com.mars.tests;


//import java.io.File;
//import java.net.URL;
import com.overall.accession.ErrorProcessing.EPDunningLetter;
import com.overall.accession.ErrorProcessing.EPSearch;
import com.overall.accession.ErrorProcessing.EPSearchResults;
import com.overall.accession.accessionNavigation.AccessionNavigation;
import com.overall.accession.accessionProcessing.AccessionDemographics;
import com.overall.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
import com.overall.fileMaintenance.sysMgt.DataCacheConfiguration;
import com.overall.headerNavigation.HeaderNavigation;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.filemaintenancews.dao.DaoManagerFileMaintenanceWS;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

//import org.apache.commons.lang.ArrayUtils;
//import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
//import com.gargoylesoftware.htmlunit.javascript.host.Window;
//import com.rpm.accession.accessionProcessing.AccessionSearch;
//import com.rpm.accession.accessionProcessing.AccessionSingleStatement;
//import com.rpm.accession.accessionProcessing.AccessionTransactionDetail;
//import com.rpm.fileMaintenance.orderProcessingConfig.FileMaintencePatternDefinition;
//import com.rpm.fileMaintenance.sysMgt.TaskScheduler;
//import com.rpm.fileMaintenance.sysMgt.TaskStatus;
/*
import com.rpm.help.Help;
import com.rpm.accession.orderProcessing.AccnTestUpdateOld;
import com.rpm.payor.payorDemographics.GroupDemographics;
import com.rpm.payor.payorDemographics.PricingConfig;
import com.rpm.payor.payorNavigation.PayorNavigation;
import com.rpm.accession.accessionProcessing.superSearch.SuperSearch;
import com.rpm.accession.accessionProcessing.superSearch.SuperSearchResults;
*/
//import com.xifin.utils.TestDataSetup;
//import com.xifin.utils.TimeStamp;



public class RegressionAccessionErrorProcessingTest extends SeleniumBaseTest  {
	
	private SsoLogin ssoLogin;
	private HeaderNavigation headerNavigation;
//	private Help help;
	private AccessionNavigation accessionNavigation;
//	private SuperSearch superSearch;
//	private SuperSearchResults superSearchResults;
	private AccessionDemographics accessionDemographics;
//	private AccessionSingleStatement accessionStatement;
//	private AccessionTransactionDetail accessionTransactionDetail;
//	private AccessionSearch accessionSearchPopup;
//	private AccnTestUpdateOld accnTestUpdate;
//	private TestDataSetup testDataSetup;
	private FileMaintenanceNavigation fileMaintenaceNavigation;
//	private PayorNavigation payorNavigation;
//	private GroupDemographics groupDemographics;
//	private PricingConfig pricingConfig;
	private DataCacheConfiguration dataCacheConfiguration;
//	private TaskScheduler taskScheduler;
//	private TaskStatus taskStatus;
//	private FileMaintencePatternDefinition fileMaintencePatternDefinition; 
//	private TimeStamp timeStamp;
	private EPSearch epSearch;
	private EPSearchResults epSearchResults;
	private EPDunningLetter epDunningLetter;
	private DaoManagerFileMaintenanceWS daoManagerFileMaintenanceWS;
	
	
	@Test(priority = 1, description = "EP Search - Submit Button Disabled")
	@Parameters({"email", "password"})
	public void testRPM_397(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_397 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Navigate to the Accession - EP Search page");			
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		accessionNavigation.navigateToEPSearchLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - The Accession - EP Search page displays");		
		epSearch = new EPSearch(driver, wait);
		Assert.assertTrue(isElementPresent(epSearch.filter1Dropdown(),5));
		
		logger.info("*** Step 3 Actions: - Select in Filter 1: dropdown - Accession option and enter in Value - '*'");	
		//"1" = Accession
		epSearch.setFilter1("1", "*");
		epSearch.submitBtn().click();

		//logger.info("*** Step 3 Expected Results: - Page has an overlay with Searching icon");
				
		//logger.info("*** Step 3 Expected Results: - Submit button is unavailable for selection");				
		
		logger.info("*** Step 3 Expected Results: - Results from search open in new window");
		//Switch to the search results popup window
		switchToPopupWin();
		epSearchResults = new EPSearchResults(driver); 
		Assert.assertTrue(isElementPresent(epSearchResults.clientIdText(),5));
		
		//Go to main page
		driver.close();
    }
	
	@Test(priority = 1, description = "EP Dunning Letter - Create new Patient Dunning Cycle")
	@Parameters({"email", "password"})
	public void testRPM_612(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_612 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Remove all the Patient Dunning Cycle from DB");

		//PK_EP_DUN_TYP = 2 (Patient)
		daoManagerXifinRpm.deleteEPDunningCycleFromEPDUNByDocTypId("2", testDb);
		
		logger.info("*** Step 3 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();		

		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Navigate to the Accession Tab");		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 6 Actions: - Navigate to the Accession - EP Dunning Letter screen");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToEPDunningLetter();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that it's on the Accession - EP Dunning Letter screen");		
		epDunningLetter = new EPDunningLetter(driver);		
		Assert.assertTrue(epDunningLetter.pageTitleText().getText().contains("Accession - EP Dunning Letter"), "        Page Title: Accession - EP Dunning Letter should show.");
				
		logger.info("*** Step 7 Actions: - Select Patient Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Patient");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 7 Actions: - Click Add Cycle button");
		epDunningLetter.addCycleBtn().click();
		
		logger.info("*** Step 7 Actions: - Enter Days to Next Letter");
		epDunningLetter.setDaysToNextLtr1("30");
		
		logger.info("*** Step 7 Actions: - Click Submit button");
		epDunningLetter.submitBtn().click();
		
		logger.info("*** Step 8 Actions: - Select Patient Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Patient");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 8 Expected Results: - Verify that the newly created Patient Dunning Cycle Letter Count, Document Type and Days to Next Letter display properly");
		Assert.assertTrue(epDunningLetter.ltrCountText1().getAttribute("value").trim().equals("1"), "        Letter Count: 1 should display.");
		
		
		List docList = daoManagerFileMaintenanceWS.getDocByDocType("Patient Error", testDb);
		List<String> docTypeList = (ArrayList<String>)docList.get(0);
		String docType = docTypeList.get(0);
		Assert.assertTrue(isDropdownItemSelected(epDunningLetter.docTypeDropdown1(), docType), "        Document Type: " + docType + " should be selected.");
		
		Assert.assertTrue(epDunningLetter.daysToNextLtr1Input().getAttribute("value").trim().equals("30"), "        Days to Next Letter: 30 should show.");

		epDunningLetter.resetBtn().click();
		driver.close();
    }
	
	@Test(priority = 1, description = "EP Dunning Letter - Update Patient Dunning Cycle")
	@Parameters({"email", "password"})
	public void testRPM_613(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_613 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Remove all the Patient Dunning Cycle from DB");

		//PK_EP_DUN_TYP = 2 (Patient)
		daoManagerXifinRpm.deleteEPDunningCycleFromEPDUNByDocTypId("2", testDb);
		
		logger.info("*** Step 3 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();		

		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Navigate to the Accession Tab");		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 6 Actions: - Navigate to the Accession - EP Dunning Letter screen");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToEPDunningLetter();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that it's on the Accession - EP Dunning Letter screen");		
		epDunningLetter = new EPDunningLetter(driver);		
		Assert.assertTrue(epDunningLetter.pageTitleText().getText().contains("Accession - EP Dunning Letter"), "        Page Title: Accession - EP Dunning Letter should show.");
				
		logger.info("*** Step 7 Actions: - Select Patient Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Patient");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 7 Actions: - Click Add Cycle button");
		epDunningLetter.addCycleBtn().click();
		
		logger.info("*** Step 7 Actions: - Enter Days to Next Letter");
		epDunningLetter.setDaysToNextLtr1("30");
		
		logger.info("*** Step 7 Actions: - Click Submit button");
		epDunningLetter.submitBtn().click();
		
		logger.info("*** Step 8 Actions: - Select Patient Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Patient");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 8 Expected Results: - Verify that the newly created Patient Dunning Cycle Letter Count, Document Type and Days to Next Letter display properly");
		Assert.assertTrue(epDunningLetter.ltrCountText1().getAttribute("value").trim().equals("1"), "        Letter Count: 1 should display.");
		
		
		List docList = daoManagerFileMaintenanceWS.getDocByDocType("Patient Error", testDb);
		List<String> docTypeList = (ArrayList<String>)docList.get(0);
		String docType = docTypeList.get(0);
		Assert.assertTrue(isDropdownItemSelected(epDunningLetter.docTypeDropdown1(), docType), "        Document Type: " + docType + " should be selected.");
		
		Assert.assertTrue(epDunningLetter.daysToNextLtr1Input().getAttribute("value").trim().equals("30"), "        Days to Next Letter: 30 should show.");
		
		logger.info("*** Step 9 Actions: - Update Days to Next Letter for the 1st Dunning Cycle");
		epDunningLetter.setDaysToNextLtr1("15");
		
		logger.info("*** Step 9 Actions: - Click Add Cycle button");
		epDunningLetter.addCycleBtn().click();
		
		logger.info("*** Step 9 Actions: - Enter Days to Next Letter for the 2nd Dunning Cycle");
		epDunningLetter.setDaysToNextLtr2("41");
		
		logger.info("*** Step 9 Actions: - Click Submit button");
		epDunningLetter.submitBtn().click();
		
		logger.info("*** Step 10 Actions: - Select Patient Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Patient");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 10 Expected Results: - Verify that the Days to Next Letter for the 1st Dunning Cycle updated proerply");
		Assert.assertTrue(epDunningLetter.daysToNextLtr1Input().getAttribute("value").trim().equals("15"), "        Days to Next Letter for the 1st Dunning Cycle: 15 should show.");
		
		logger.info("*** Step 10 Expected Results: - Verify that the newly created 2nd Dunning Cycle Letter Count, Document Type and Days to Next Letter display properly");
		Assert.assertTrue(epDunningLetter.ltrCountText2().getAttribute("value").trim().equals("2"), "        Letter Count: 2 should display.");		
		Assert.assertTrue(isDropdownItemSelected(epDunningLetter.docTypeDropdown2(), docType), "        Document Type: " + docType + " should be selected.");		
		Assert.assertTrue(epDunningLetter.daysToNextLtr2Input().getAttribute("value").trim().equals("41"), "        Days to Next Letter: 41 should show.");

		epDunningLetter.resetBtn().click();
		driver.close();
    }
	
	@Test(priority = 1, description = "EP Dunning Letter - Delete Patient Dunning Cycle")
	@Parameters({"email", "password"})
	public void testRPM_614(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_614 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Remove all the Patient Dunning Cycle from DB");

		//PK_EP_DUN_TYP = 2 (Patient)
		daoManagerXifinRpm.deleteEPDunningCycleFromEPDUNByDocTypId("2", testDb);
		
		logger.info("*** Step 3 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();		

		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Navigate to the Accession Tab");		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 6 Actions: - Navigate to the Accession - EP Dunning Letter screen");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToEPDunningLetter();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that it's on the Accession - EP Dunning Letter screen");		
		epDunningLetter = new EPDunningLetter(driver);		
		Assert.assertTrue(epDunningLetter.pageTitleText().getText().contains("Accession - EP Dunning Letter"), "        Page Title: Accession - EP Dunning Letter should show.");
				
		logger.info("*** Step 7 Actions: - Select Patient Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Patient");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 7 Actions: - Click Add Cycle button");
		epDunningLetter.addCycleBtn().click();
		
		logger.info("*** Step 7 Actions: - Enter Days to Next Letter");
		epDunningLetter.setDaysToNextLtr1("30");
		
		logger.info("*** Step 7 Actions: - Click Submit button");
		epDunningLetter.submitBtn().click();
		
		logger.info("*** Step 8 Actions: - Select Patient Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Patient");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 8 Expected Results: - Verify that the newly created Patient Dunning Cycle Letter Count, Document Type and Days to Next Letter display properly");
		Assert.assertTrue(epDunningLetter.ltrCountText1().getAttribute("value").trim().equals("1"), "        Letter Count: 1 should display.");
		
		
		List docList = daoManagerFileMaintenanceWS.getDocByDocType("Patient Error", testDb);
		List<String> docTypeList = (ArrayList<String>)docList.get(0);
		String docType = docTypeList.get(0);
		Assert.assertTrue(isDropdownItemSelected(epDunningLetter.docTypeDropdown1(), docType), "        Document Type: " + docType + " should be selected.");
		
		Assert.assertTrue(epDunningLetter.daysToNextLtr1Input().getAttribute("value").trim().equals("30"), "        Days to Next Letter: 30 should show.");
		
		logger.info("*** Step 9 Actions: - Check the Delete checkbox for the 1st Dunning Cycle");
		epDunningLetter.deleteCheckBox("1").click();
		
		logger.info("*** Step 9 Actions: - Click Submit button");
		epDunningLetter.submitBtn().click();
		
		//logger.info("*** Step 10 Actions: - Select Patient Letter Destination and tab out");
		//selectItem(epDunningLetter.ltrDestinationDropdown(), "Patient");
		//epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 9 Expected Results: - Verify that Patient Dunning Cycle should be deleted");

		ArrayList<String> dunList = daoManagerXifinRpm.getDunCycleInfoFromEPDUNByDunTypDunId("2", "1", testDb);
		Assert.assertTrue(dunList.isEmpty(), "        The Patient Dunning Cycle should be deleted.");

		epDunningLetter.resetBtn().click();
		driver.close();
    }
	
	@Test(priority = 1, description = "EP Dunning Letter - Create new Client Dunning Cycle")
	@Parameters({"email", "password"})
	public void testRPM_615(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_615 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Remove all the Patient Dunning Cycle from DB");

		//PK_EP_DUN_TYP = 1 (Client)
		daoManagerXifinRpm.deleteEPDunningCycleFromEPDUNByDocTypId("1", testDb);
		
		logger.info("*** Step 3 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();		

		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Navigate to the Accession Tab");		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 6 Actions: - Navigate to the Accession - EP Dunning Letter screen");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToEPDunningLetter();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that it's on the Accession - EP Dunning Letter screen");		
		epDunningLetter = new EPDunningLetter(driver);		
		Assert.assertTrue(epDunningLetter.pageTitleText().getText().contains("Accession - EP Dunning Letter"), "        Page Title: Accession - EP Dunning Letter should show.");
				
		logger.info("*** Step 7 Actions: - Select Client Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Client");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 7 Actions: - Click Add Cycle button");
		epDunningLetter.addCycleBtn().click();
		
		logger.info("*** Step 7 Actions: - Enter Days to Next Letter");
		epDunningLetter.setDaysToNextLtr1("30");
		
		logger.info("*** Step 7 Actions: - Click Submit button");
		epDunningLetter.submitBtn().click();
		
		logger.info("*** Step 8 Actions: - Select Client Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Client");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 8 Expected Results: - Verify that the newly created Client Dunning Cycle Letter Count, Document Type and Days to Next Letter display properly");
		Assert.assertTrue(epDunningLetter.ltrCountText1().getAttribute("value").trim().equals("1"), "        Letter Count: 1 should display.");
		
		
		List docList = daoManagerFileMaintenanceWS.getDocByDocType("Client Error", testDb);
		List<String> docTypeList = (ArrayList<String>)docList.get(0);
		String docType = docTypeList.get(0);
		Assert.assertTrue(isDropdownItemSelected(epDunningLetter.docTypeDropdown1(), docType), "        Document Type: " + docType + " should be selected.");
		
		Assert.assertTrue(epDunningLetter.daysToNextLtr1Input().getAttribute("value").trim().equals("30"), "        Days to Next Letter: 30 should show.");

		epDunningLetter.resetBtn().click();
		driver.close();
    }
	
	@Test(priority = 1, description = "EP Dunning Letter - Update Client Dunning Cycle")
	@Parameters({"email", "password"})
	public void testRPM_616(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_616 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Remove all the Client Dunning Cycle from DB");

		//PK_EP_DUN_TYP = 1 (Client)
		daoManagerXifinRpm.deleteEPDunningCycleFromEPDUNByDocTypId("1", testDb);
		
		logger.info("*** Step 3 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();		

		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Navigate to the Accession Tab");		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 6 Actions: - Navigate to the Accession - EP Dunning Letter screen");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToEPDunningLetter();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that it's on the Accession - EP Dunning Letter screen");		
		epDunningLetter = new EPDunningLetter(driver);		
		Assert.assertTrue(epDunningLetter.pageTitleText().getText().contains("Accession - EP Dunning Letter"), "        Page Title: Accession - EP Dunning Letter should show.");
				
		logger.info("*** Step 7 Actions: - Select Client Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Client");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);		
		
		logger.info("*** Step 7 Actions: - Click Add Cycle button");
		epDunningLetter.addCycleBtn().click();
		
		logger.info("*** Step 7 Actions: - Enter Days to Next Letter");
		epDunningLetter.setDaysToNextLtr1("30");
		
		logger.info("*** Step 7 Actions: - Click Submit button");
		epDunningLetter.submitBtn().click();			
				
		logger.info("*** Step 8 Actions: - Select Client Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Client");	
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 8 Expected Results: - Verify that the newly created Client Dunning Cycle Letter Count, Document Type and Days to Next Letter display properly");
		Assert.assertTrue(epDunningLetter.ltrCountText1().getAttribute("value").trim().equals("1"), "        Letter Count: 1 should display.");
		
		
		List docList = daoManagerFileMaintenanceWS.getDocByDocType("Client Error", testDb);
		List<String> docTypeList = (ArrayList<String>)docList.get(0);
		String docType = docTypeList.get(0);
		Assert.assertTrue(isDropdownItemSelected(epDunningLetter.docTypeDropdown1(), docType), "        Document Type: " + docType + " should be selected.");
		
		Assert.assertTrue(epDunningLetter.daysToNextLtr1Input().getAttribute("value").trim().equals("30"), "        Days to Next Letter: 30 should show.");
		
		logger.info("*** Step 9 Actions: - Update Days to Next Letter for the 1st Dunning Cycle");
		epDunningLetter.setDaysToNextLtr1("15");
		
		logger.info("*** Step 9 Actions: - Click Add Cycle button");
		epDunningLetter.addCycleBtn().click();
		
		logger.info("*** Step 9 Actions: - Enter Days to Next Letter for the 2nd Dunning Cycle");
		epDunningLetter.setDaysToNextLtr2("41");
		
		logger.info("*** Step 9 Actions: - Click Submit button");
		epDunningLetter.submitBtn().click();
		
		logger.info("*** Step 10 Actions: - Select Client Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Client");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 10 Expected Results: - Verify that the Days to Next Letter for the 1st Dunning Cycle updated proerply");
		Assert.assertTrue(epDunningLetter.daysToNextLtr1Input().getAttribute("value").trim().equals("15"), "        Days to Next Letter for the 1st Dunning Cycle: 15 should show.");
		
		logger.info("*** Step 10 Expected Results: - Verify that the newly created 2nd Dunning Cycle Letter Count, Document Type and Days to Next Letter display properly");
		Assert.assertTrue(epDunningLetter.ltrCountText2().getAttribute("value").trim().equals("2"), "        Letter Count: 2 should display.");		
		Assert.assertTrue(isDropdownItemSelected(epDunningLetter.docTypeDropdown2(), docType), "        Document Type: " + docType + " should be selected.");		
		Assert.assertTrue(epDunningLetter.daysToNextLtr2Input().getAttribute("value").trim().equals("41"), "        Days to Next Letter: 41 should show.");
		
		epDunningLetter.resetBtn().click();
		driver.close();
    }
	
	@Test(priority = 1, description = "EP Dunning Letter - Delete Client Dunning Cycle")
	@Parameters({"email", "password"})
	public void testRPM_617(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_617 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Remove all the Client Dunning Cycle from DB");

		//PK_EP_DUN_TYP = 1 (Client)
		daoManagerXifinRpm.deleteEPDunningCycleFromEPDUNByDocTypId("1", testDb);
		
		logger.info("*** Step 3 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();		

		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Navigate to the Accession Tab");		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 6 Actions: - Navigate to the Accession - EP Dunning Letter screen");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToEPDunningLetter();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that it's on the Accession - EP Dunning Letter screen");		
		epDunningLetter = new EPDunningLetter(driver);		
		Assert.assertTrue(epDunningLetter.pageTitleText().getText().contains("Accession - EP Dunning Letter"), "        Page Title: Accession - EP Dunning Letter should show.");
				
		logger.info("*** Step 7 Actions: - Select Client Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Client");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 7 Actions: - Click Add Cycle button");
		epDunningLetter.addCycleBtn().click();
		
		logger.info("*** Step 7 Actions: - Enter Days to Next Letter");
		epDunningLetter.setDaysToNextLtr1("30");
		
		logger.info("*** Step 7 Actions: - Click Submit button");
		epDunningLetter.submitBtn().click();
		
		logger.info("*** Step 8 Actions: - Select Client Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Client");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 8 Expected Results: - Verify that the newly created Client Dunning Cycle Letter Count, Document Type and Days to Next Letter display properly");
		Assert.assertTrue(epDunningLetter.ltrCountText1().getAttribute("value").trim().equals("1"), "        Letter Count: 1 should display.");
		
		List docList = daoManagerFileMaintenanceWS.getDocByDocType("Client Error", testDb);
		List<String> docTypeList = (ArrayList<String>)docList.get(0);
		String docType = docTypeList.get(0);
		Assert.assertTrue(isDropdownItemSelected(epDunningLetter.docTypeDropdown1(), docType), "        Document Type: " + docType + " should be selected.");
		
		Assert.assertTrue(epDunningLetter.daysToNextLtr1Input().getAttribute("value").trim().equals("30"), "        Days to Next Letter: 30 should show.");
		
		logger.info("*** Step 9 Actions: - Check the Delete checkbox for the 1st Dunning Cycle");
		epDunningLetter.deleteCheckBox("1").click();
		
		logger.info("*** Step 9 Actions: - Click Submit button");
		epDunningLetter.submitBtn().click();
		
		logger.info("*** Step 9 Expected Results: - Verify that Client Dunning Cycle should be deleted");

		ArrayList<String> dunList = daoManagerXifinRpm.getDunCycleInfoFromEPDUNByDunTypDunId("1", "1", testDb);
		Assert.assertTrue(dunList.isEmpty(), "        The Client Dunning Cycle should be deleted.");

		epDunningLetter.resetBtn().click();
		driver.close();
    }
	
	@Test(priority = 1, description = "EP Dunning Letter - Input validation - No Days to Next Letter")
	@Parameters({"email", "password"})
	public void testRPM_618(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_618 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Remove all the Patient Dunning Cycle from DB");

		//PK_EP_DUN_TYP = 2 (Patient)
		daoManagerXifinRpm.deleteEPDunningCycleFromEPDUNByDocTypId("2", testDb);
		
		logger.info("*** Step 3 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();		

		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Navigate to the Accession Tab");		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 6 Actions: - Navigate to the Accession - EP Dunning Letter screen");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToEPDunningLetter();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that it's on the Accession - EP Dunning Letter screen");		
		epDunningLetter = new EPDunningLetter(driver);		
		Assert.assertTrue(epDunningLetter.pageTitleText().getText().contains("Accession - EP Dunning Letter"), "        Page Title: Accession - EP Dunning Letter should show.");
				
		logger.info("*** Step 7 Actions: - Select Patient Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Patient");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 7 Actions: - Click Add Cycle button");
		epDunningLetter.addCycleBtn().click();
		
		logger.info("*** Step 7 Actions: - Click Submit button");
		epDunningLetter.submitBtn().click();
		
		logger.info("*** Step 7 Expected Results: - Verify that The 'Days to Next Letter' field requires a valid input error message displays");
		Assert.assertTrue(epDunningLetter.fldInputReqErrMsgText().trim().contains("The \"Days to Next Letter\" field requires a valid input."), "        The \"Days to Next Letter\" field requires a valid input. error message should show.");
				
		epDunningLetter.resetBtn().click();
		driver.close();
    }
	
	@Test(priority = 1, description = "EP Dunning Letter - Letter Count validation")
	@Parameters({"email", "password"})
	public void testRPM_619(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_619 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Remove all the Client Dunning Cycle from DB");

		//PK_EP_DUN_TYP = 1 (Client)
		daoManagerXifinRpm.deleteEPDunningCycleFromEPDUNByDocTypId("1", testDb);
		
		logger.info("*** Step 3 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();		

		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Navigate to the Accession Tab");		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 6 Actions: - Navigate to the Accession - EP Dunning Letter screen");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToEPDunningLetter();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that it's on the Accession - EP Dunning Letter screen");		
		epDunningLetter = new EPDunningLetter(driver);		
		Assert.assertTrue(epDunningLetter.pageTitleText().getText().contains("Accession - EP Dunning Letter"), "        Page Title: Accession - EP Dunning Letter should show.");
				
		logger.info("*** Step 7 Actions: - Select Client Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Client");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);		
		
		logger.info("*** Step 7 Actions: - Click Add Cycle button");
		epDunningLetter.addCycleBtn().click();
		
		logger.info("*** Step 7 Actions: - Enter Days to Next Letter");
		epDunningLetter.setDaysToNextLtr1("30");
		
		logger.info("*** Step 7 Actions: - Click Submit button");
		epDunningLetter.submitBtn().click();			
				
		logger.info("*** Step 8 Actions: - Select Client Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Client");	
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 8 Expected Results: - Verify that the newly created Client Dunning Cycle Letter Count, Document Type and Days to Next Letter display properly");
		Assert.assertTrue(epDunningLetter.ltrCountText1().getAttribute("value").trim().equals("1"), "        Letter Count: 1 should display.");
		
		
		List docList = daoManagerFileMaintenanceWS.getDocByDocType("Client Error", testDb);
		List<String> docTypeList = (ArrayList<String>)docList.get(0);
		String docType = docTypeList.get(0);
		Assert.assertTrue(isDropdownItemSelected(epDunningLetter.docTypeDropdown1(), docType), "        Document Type: " + docType + " should be selected.");
		
		Assert.assertTrue(epDunningLetter.daysToNextLtr1Input().getAttribute("value").trim().equals("30"), "        Days to Next Letter: 30 should show.");
		
		logger.info("*** Step 9 Actions: - Click Add Cycle button");
		epDunningLetter.addCycleBtn().click();
		
		logger.info("*** Step 9 Actions: - Enter Days to Next Letter for the 2nd Dunning Cycle");
		epDunningLetter.setDaysToNextLtr2("41");		
		
		logger.info("*** Step 10 Actions: - Check the Delete checkbox for the 1st Dunning Cycle");
		epDunningLetter.deleteCheckBox("1").click();
		
		logger.info("*** Step 10 Actions: - Click Submit button");
		epDunningLetter.submitBtn().click();
		
		logger.info("*** Step 10 Expected Results: - Verify that Letter count must follow a non-breaking sequence error message displays");
		Assert.assertTrue(epDunningLetter.pageValidationErrMsgText().getText().trim().contains("Letter count must follow a non-breaking sequence"), "        Letter count must follow a non-breaking sequence error message should show.");

		epDunningLetter.resetBtn().click();
		driver.close();
    }
	
	@Test(priority = 1, description = "EP Dunning Letter - Input validation - Non-numeric value")
	@Parameters({"email", "password"})
	public void testRPM_620(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_620 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Remove all the Patient Dunning Cycle from DB");

		//PK_EP_DUN_TYP = 2 (Patient)
		daoManagerXifinRpm.deleteEPDunningCycleFromEPDUNByDocTypId("2", testDb);
		
		logger.info("*** Step 3 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();		

		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Navigate to the Accession Tab");		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 6 Actions: - Navigate to the Accession - EP Dunning Letter screen");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToEPDunningLetter();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that it's on the Accession - EP Dunning Letter screen");		
		epDunningLetter = new EPDunningLetter(driver);		
		Assert.assertTrue(epDunningLetter.pageTitleText().getText().contains("Accession - EP Dunning Letter"), "        Page Title: Accession - EP Dunning Letter should show.");
				
		logger.info("*** Step 7 Actions: - Select Patient Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Patient");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 7 Actions: - Click Add Cycle button");
		epDunningLetter.addCycleBtn().click();
		
		logger.info("*** Step 7 Actions: - Enter Non-numeric letters in the Days to Next Letter field and tab out");
		epDunningLetter.setDaysToNextLtr1("ABC");
		
		logger.info("*** Step 7 Expected Results: - Verify that Only numbers are allowed in this field error message displays");
		Assert.assertTrue(closeAlertAndGetItsText(true).trim().contains("Only numbers are allowed in this field"), "        Only numbers are allowed in this field error message should show.");
				
		epDunningLetter.resetBtn().click();
		driver.close();
    }
	
	@Test(priority = 1, description = "EP Dunning Letter - Reset")
	@Parameters({"email", "password"})
	public void testRPM_622(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_622 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Remove all the Patient Dunning Cycle from DB");

		//PK_EP_DUN_TYP = 2 (Patient)
		daoManagerXifinRpm.deleteEPDunningCycleFromEPDUNByDocTypId("2", testDb);
		
		logger.info("*** Step 3 Actions: - Navigate to the File Maintenance Page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();		

		logger.info("*** Step 3 Actions: - Navigate to the System Data Cache Page");	
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();			
		
		logger.info("*** Step 3 Expected Results: - The Data Cache Configuration page displays");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");
		XifinAdminUtils xifinAdminUtils = new XifinAdminUtils(driver, config);
		xifinAdminUtils.clearDataCache();

        logger.info("*** Step 5 Actions: - Navigate to the Accession Tab");		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 6 Actions: - Navigate to the Accession - EP Dunning Letter screen");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToEPDunningLetter();
		switchToPopupWin();
		
		logger.info("*** Step 6 Expected Results: - Verify that it's on the Accession - EP Dunning Letter screen");		
		epDunningLetter = new EPDunningLetter(driver);		
		Assert.assertTrue(epDunningLetter.pageTitleText().getText().contains("Accession - EP Dunning Letter"), "        Page Title: Accession - EP Dunning Letter should show.");
				
		logger.info("*** Step 7 Actions: - Select Patient Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Patient");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 7 Actions: - Click Add Cycle button");
		epDunningLetter.addCycleBtn().click();
		
		logger.info("*** Step 7 Actions: - Enter Days to Next Letter");
		epDunningLetter.setDaysToNextLtr1("30");
		
		logger.info("*** Step 7 Actions: - Click Submit button");
		epDunningLetter.submitBtn().click();
		
		logger.info("*** Step 8 Actions: - Select Patient Letter Destination and tab out");
		selectItem(epDunningLetter.ltrDestinationDropdown(), "Patient");
		epDunningLetter.ltrDestinationDropdown().sendKeys(Keys.TAB);
		
		logger.info("*** Step 8 Expected Results: - Verify that the newly created Patient Dunning Cycle Letter Count, Document Type and Days to Next Letter display properly");
		Assert.assertTrue(epDunningLetter.ltrCountText1().getAttribute("value").trim().equals("1"), "        Letter Count: 1 should display.");
		
		
		List docList = daoManagerFileMaintenanceWS.getDocByDocType("Patient Error", testDb);
		List<String> docTypeList = (ArrayList<String>)docList.get(0);
		String docType = docTypeList.get(0);
		Assert.assertTrue(isDropdownItemSelected(epDunningLetter.docTypeDropdown1(), docType), "        Document Type: " + docType + " should be selected.");
		
		Assert.assertTrue(epDunningLetter.daysToNextLtr1Input().getAttribute("value").trim().equals("30"), "        Days to Next Letter: 30 should show.");

		logger.info("*** Step 9 Actions: - Click Reset button");		
		epDunningLetter.resetBtn().click();
		
		logger.info("*** Step 9 Expected Results: - Verify that Letter Destination dropdown was cleared and enabled");	
		Assert.assertTrue(epDunningLetter.ltrDestinationDropdown().getAttribute("value").equals("0"), "        Letter Destination dropdown should be cleared.");		
		Assert.assertTrue(epDunningLetter.ltrDestinationDropdown().isEnabled(), "        Letter Destination dropdown should be enabled.");
		
		driver.close();
    }
	
	@Test(priority = 1, description = "EP Dunning Letter - Help")
	@Parameters({"email", "password"})
	public void testRPM_621(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_621 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();		
		
        logger.info("*** Step 2 Actions: - Navigate to the Accession Tab");				
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToAccessionTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click EP Dunning Letter link and navigate to the Accession - EP Dunning Letter screen");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.epDunningLetterLink();
		
		logger.info("*** Step 3 Actions: - Click Help button on the Accession - EP Dunning Letter screen");
		epDunningLetter = new EPDunningLetter(driver);
		epDunningLetter.helpBtn();
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that EP Dunning Letter help page displays");		
		Assert.assertTrue(driver.getTitle().trim().contains("EP Dunning Letter"), "        EP Dunning Letter help should show.");
		
		driver.close();
    }
	
}
