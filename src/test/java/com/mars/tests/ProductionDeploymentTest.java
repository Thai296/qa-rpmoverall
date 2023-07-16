package com.mars.tests;

/*import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;*/

import com.overall.accession.accessionNavigation.AccessionNavigation;
import com.overall.accession.accessionProcessing.AccessionDetail;
import com.overall.accession.accessionProcessing.AccessionSearch;
import com.overall.accession.accessionProcessing.AccessionSearchResults;
import com.overall.accession.accessionProcessing.AccessionTransactionDetail;
import com.overall.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
import com.overall.fileMaintenance.pricing.TestCodeNew;
import com.overall.fileMaintenance.pricing.TestCodeSearchNew;
import com.overall.fileMaintenance.pricing.TestCodeSearchResultsNew;
import com.overall.financialManagement.endOfMonth.FinancialManagement;
import com.overall.headerNavigation.HeaderNavigation;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TimeStamp;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

//import com.rpm.accessionProcessing.AccessionDemographics;
//import com.rpm.accessionProcessing.AccessionSingleStatement;
/*import com.rpm.client.ClientInquiry.PriceInquiry;
import com.rpm.client.ClientProcessing.AuditLog;
import com.rpm.client.ClientProcessing.ClientDemographics;
import com.rpm.client.ClientProcessing.EligCensusConfig;
import com.rpm.client.ClientProcessing.PayorExclusions;
import com.rpm.client.ClientProcessing.PhysicianAssignment;
import com.rpm.clientNavigation.ClientNavigation;
import com.rpm.fileMaintenance.orderProcessingConfig.FileMaintencePatternDefinition;
import com.rpm.fileMaintenance.sysMgt.DataCacheConfiguration;
import com.rpm.fileMaintenance.sysMgt.TaskScheduler;
import com.rpm.fileMaintenance.sysMgt.TaskStatus;*/
//import com.rpm.help.Help;
/*import com.rpm.orderProcessing.AccnTestUpdate;
import com.rpm.payorDemographics.DialysisFrequencyControl;
import com.rpm.payorDemographics.GroupDemographics;
import com.rpm.payorDemographics.PricingConfig;
import com.rpm.payorNavigation.PayorNavigation;*/
/*import com.rpm.superSearch.SuperSearch;
import com.rpm.superSearch.SuperSearchResults;*/
//import com.xifin.utils.Parser;
/*import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TestDataSetup;*/



public class ProductionDeploymentTest extends SeleniumBaseTest  {
	//private Parser parser;
	private SsoLogin ssoLogin;
	private HeaderNavigation headerNavigation;
	//private Help help;
	private AccessionNavigation accessionNavigation;
	/*private SuperSearch superSearch;
	private SuperSearchResults superSearchResults;
	private AccessionDemographics accessionDemographics;
	private AccessionSingleStatement accessionStatement;*/
	private AccessionTransactionDetail accessionTransactionDetail;
	/*private AccessionSearch accessionSearchPopup;
	private AccnTestUpdate accnTestUpdate;
	private TestDataSetup testDataSetup;
	private FileMaintenanceNavigation fileMaintenaceNavigation;
	private PayorNavigation payorNavigation;
	private GroupDemographics groupDemographics;
	private PricingConfig pricingConfig;
	private DataCacheConfiguration dataCacheConfiguration;
	private TaskScheduler taskScheduler;
	private TaskStatus taskStatus;
	private FileMaintencePatternDefinition fileMaintencePatternDefinition; */
	private TimeStamp timeStamp;
	/*private ClientNavigation clientNavigation;
	private PayorExclusions payorExclusions;
	private AuditLog auditLog;
	private PhysicianAssignment physicianAssignment;
	private EligCensusConfig eligCensusConfig;
	private RandomCharacter randomCharacter;
	private ClientDemographics clientDemographics;
	private PriceInquiry priceInquiry;*/
	private AccessionSearch accessionSearch;
	private AccessionSearchResults accessionSearchResults;
	private AccessionDetail accessionDetail;
	private FileMaintenanceNavigation fileMaintenanceNavigation;
	private TestCodeNew testCodeNew;
	private TestCodeSearchNew testCodeSearchNew;
	private TestCodeSearchResultsNew testCodeSearchResultsNew;
	private FinancialManagement financialManagement;
	
	
	@Test(priority = 1, description = "Accn Transaction Detail-Accn Search-XP Detail")
	@Parameters({"email", "password"})
	public void testRPM_783(String email, String password) throws Exception {
    	logger.info("========== Testing - RPM_783 ==========");
    	
		logger.info("*** Step 1 Actions: - Log into RPM with SSO Username and Password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);	
		
		logger.info("*** Step 1 Expected Results: - Verify that the user and customer id are correct");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));  
		
		logger.info("*** Step 1 Expected Results: - Verify that MARS Message Board screen shows");
		switchToDefaultWinFromFrame();	    
		String winHandler = driver.getWindowHandle();
		switchToFrame(headerNavigation.marsContentFrame());
		Assert.assertTrue(headerNavigation.msgBoardText().getText().contains("MARS Message Board"), "        MARS Message Board page is displayed");		
		
		logger.info("*** Step 2 Action: - Click on Transaction Detail link in the sidebar on the left");
		switchToDefaultWinFromFrame();	    		
		winHandler = driver.getWindowHandle();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToTransactionDetailLink();
		switchToPopupWin();		
		driver.close();
		
		logger.info("*** Step 2 Expected results: - Verify that the Accession - Transction Detail page is displayed" );
		accessionTransactionDetail = new AccessionTransactionDetail(driver); 
		switchToWin(winHandler);
	    switchToFrame(headerNavigation.marsContentFrame());
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(), 5),"        Accession - Transaction Detail page is displayed");
		
		logger.info("*** Step 3 Actions: - Click on Accession Search icon button");
		accessionSearch = new AccessionSearch(driver);
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnSearchIconBtn(), 10));		
		accessionTransactionDetail.clickAccnSearchIconBtn();
		switchToPopupWin();
		
		logger.info("*** Step 4 Actions: - Enter DOS Range values and click Search button in Accession Search screen");
		Assert.assertTrue(isElementPresent(accessionSearch.dosRangeFromInput(), 10), "        DOS Range From Input field is displayed");
		Assert.assertTrue(isElementPresent(accessionSearch.dosRangeToInput(), 10), "        DOS Range To Input field is displayed");
		timeStamp = new TimeStamp(driver);
		accessionSearch.setDOSRange(timeStamp.getPreviousDate("MM/dd/yyyy", 100), timeStamp.getCurrentDate());		
		Assert.assertTrue(isElementPresent(accessionSearch.searchBtn(), 10), "        Search button is displayed.");		
		accessionSearch.clickSearchBtn();
		
		logger.info("*** Step 4 Expected results: - Verify that the Accession Search Results page is displayed");
		switchToPopupWin();
		accessionSearchResults = new AccessionSearchResults(driver);
		Assert.assertTrue(isElementPresent(accessionSearchResults.accnTable(), 10),"        Accession Search Results page is displayed.");
		
		logger.info("*** Step 5 Actions: - Select an Accession in Accession Search Results screen");
		String accnId = accessionSearchResults.accnIdLink(1, 3).getAttribute("value");
		accessionSearchResults.accnIdLink(1, 3).click();
		logger.info("        Selected Accession ID: " + accnId);
		
		logger.info("*** Step 5 Expected Results: - Verify that the Accession is loaded properly in Accession Transaction Detail screen");
		switchToWin(winHandler);
		switchToFrame(headerNavigation.marsContentFrame());
		Assert.assertTrue(isElementPresent(accessionTransactionDetail.accnIdInput(), 5));
		Assert.assertEquals(accnId, accessionTransactionDetail.accnIdInput().getAttribute("value"),"      The AccnId: " + accnId + " is displayed in Accession Transaction Detail page.");
		
		logger.info("*** Step 6 Actions: - Navigate to XP - Detail screen");
		switchToDefaultWinFromFrame();		
		clickHiddenPageObject(accessionNavigation.detailLink(), 0);
		
		logger.info("*** Step 6 Expected Results: - Verify that the Accession Detail page is displayed");
		accessionDetail = new AccessionDetail(driver, config, wait);
		switchToFrame(headerNavigation.marsContentFrame());
		switchToFrame(accessionNavigation.platformiFrame());			
		
		logger.info("*** Step 6 Expected Results: - Verify that the transaction function works and the Accession is loaded properly in the Detail screen");
		Assert.assertTrue(isElementPresent(accessionDetail.accnIdInput(), 10));
		Assert.assertTrue(accessionDetail.isAccnLoaded(accnId));
		accessionDetail.clickReset();
		
		driver.close();
    }	

	@Test(priority = 1, description = "Test Code-XP Test Code Search")
	@Parameters({"email", "password"})
	public void testRPM_784(String email, String password) throws Exception {
    	logger.info("========== Testing - RPM_784 ==========");
    	fileMaintenanceNavigation = new FileMaintenanceNavigation(driver, config);
		testCodeNew = new TestCodeNew(driver);
		testCodeSearchNew = new TestCodeSearchNew(driver);
		testCodeSearchResultsNew = new TestCodeSearchResultsNew(driver);
    	
		logger.info("*** Step 1 Actions: - Log into RPM with SSO Username and Password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);	
		
		logger.info("*** Step 1 Expected Results: - Verify that the user and customer id are correct");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));  		
		
		switchToDefaultWinFromFrame();	    		
		String winHandler = driver.getWindowHandle();	
		
		logger.info("*** Step 2 Action: - Click on File Maintenance tab. Click on Test Code (New) link on the sidebar in the left.");		
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();		
		driver.close();		
		switchToWin(winHandler);	    
		fileMaintenanceNavigation.navigateToTestCodeNewLink();
		
		logger.info("*** Step 2 Expected Results: - Verify that File Maintenance - TestCode Screen is displayed");
		switchToFrame(fileMaintenanceNavigation.contentFrame());
		switchToFrame(headerNavigation.platformiFrame());		
		
		logger.info("*** Step 3 Action: - Click on Test Code Search icon button");
		Assert.assertTrue(isElementPresent(testCodeNew.testCodeSearchIconBtn(), 5),"       Test Code Search Icon button is displayed.");		
		testCodeNew.clickTestCodeSearchIconBtn();
		String parent = switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Test Code Search page is displayed");
		Assert.assertTrue(isElementPresent(testCodeSearchNew.testCodeIdInput(), 5),"       Test Code input field is displayed.");		
		
		logger.info("*** Step 4 Action: - Enter '*' in Test Code ID Input field and click on Search button");
		testCodeSearchNew.setTestCodeId("*");
		Assert.assertTrue(isElementPresent(testCodeSearchNew.searchBtn(), 5),"       Search Button is displayed");		
		testCodeSearchNew.clickSearchBtn();
		
		logger.info("*** Step 4 Expected Results: - Verify that Test Code Search Results screen is displayed");
		Assert.assertTrue(isElementPresent(testCodeSearchResultsNew.testCodeSearchResultsTable(), 5),"       Test Code Search Results Table is displayed.");
		
		logger.info("*** Step 5 Action: - Select a Test Code ID link in Tesst Code Search Results screen");
		Assert.assertTrue(isElementPresent(testCodeSearchResultsNew.testCodeSearchResultsRow(2, 2), 5), "       Test Code Search Results Row is displayed.");
		String testCodeId = testCodeSearchResultsNew.testCodeSearchResultsRow(2, 2).getText();		
		testCodeSearchResultsNew.testCodeSearchResultsRow(2, 2).click();
		logger.info("        Selected Test Code ID: " + testCodeId);
		
		logger.info("*** Step 5 Expected Results: - Verify that the selected Test Code is loaded properly in the Test Code screen");
		switchToParentWin(parent);
		switchToFrame(fileMaintenanceNavigation.contentFrame());
		switchToFrame(headerNavigation.platformiFrame());
		Assert.assertTrue(isElementPresent(testCodeNew.testIdText(), 10),"       Test Code ID is displayed.");		
		Assert.assertEquals(testCodeNew.testIdText().getText(), testCodeId, "       Test Code ID: " + testCodeId + " is populated in Test Code screen.");
	
		testCodeNew.clickResetBtn();		
		driver.close();
    }	

	@Test(priority = 1, description = "EOM WebApp-EOM by Facility Closing Package")
	@Parameters({"email", "password"})
	public void testRPM_785(String email, String password) throws Exception {
    	logger.info("========== Testing - RPM_785 =========="); 
    	financialManagement = new FinancialManagement(driver, wait);
    	
		logger.info("*** Step 1 Actions: - Log into RPM with SSO Username and Password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);	
		
		logger.info("*** Step 1 Expected Results: - Verify that the user and customer id are correct");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));  		
		
		switchToDefaultWinFromFrame();					
		
		logger.info("*** Step 2 Actions: - Click on Financial Managerment Tab");
		headerNavigation.navigateToFinancialManagementTab();
		
		logger.info("*** Step 3 Actions: - Click on Closing Package link in the sidebar on the left side of the screen");
		switchToFrame(financialManagement.menuFrame());
		Assert.assertTrue(isElementPresent(financialManagement.closingPackage(), 5),"       Financial Management Closing Package link is displayed");
		switchToDefaultWinFromFrame();
		financialManagement.navigateToClosingPackageLink();
		
		logger.info("*** Step 3 Expected Results: - Verify that EOM by Facility Closing Package screen is displayed");
		switchToFrame(financialManagement.contentFrame());		
		switchToFrame(headerNavigation.platformiFrame());		
		Assert.assertTrue(isElementPresent(financialManagement.accountingPeriodDropdown(), 5),"        Accounting Period dropdown list is displayed.");
		
		logger.info("*** Step 4 Actions: - Select the most recent 'Accounting Period'. Select the first 'Client primary facility'");		
		financialManagement.clickAccountingPeriodDropdown();
		Assert.assertTrue(isElementPresent(financialManagement.facilityDropdown(), 5),"        Facility dropdown List is displayed");
		financialManagement.clickFacilityDropdown();
		
		logger.info("*** Step 5 Actions: - Click on the Load Package button");	
		Assert.assertTrue(isElementPresent(financialManagement.loadPackageBtn(), 5),"        Load page btn is displayed");		
		financialManagement.clickLoadPackageBtn();		
		waitUntilElementPresent(financialManagement.facilitytable(), 30);
		
		logger.info("*** Step 5 Expected Results: - Verify that the EOM Close spreadsheet is displayed");
		Assert.assertTrue(isElementPresent(financialManagement.facilitytable(), 5),"        The EOM Close spreadsheet is displayed.");		
	
		driver.close();
    }	

	@Test(priority = 1, description = "PF Order Entry")
	@Parameters({"email", "password"})
	public void testRPM_787(String email, String password) throws Exception {
    	logger.info("========== Testing - RPM_787 =========="); 
    	accessionNavigation = new AccessionNavigation(driver, config);
    	//Robot robot = new Robot();
    	
		logger.info("*** Step 1 Actions: - Log into RPM with SSO Username and Password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);	
		
		logger.info("*** Step 1 Expected Results: - Verify that the user and customer id are correct");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));  		
		
		switchToDefaultWinFromFrame();				
		
		logger.info("*** Step 3 Actions: - Click on Order Entry link in the sidebar on the left");			
		accessionNavigation.orderEntryLinkPF();									
		
		switchToFrame(headerNavigation.marsContentFrame());
		switchToFrame(accessionNavigation.platformiFrame());			
		
		logger.info("*** Step 4 Actions: - Click Keep button after downloading the jnlp file");		
		Thread.sleep(3000);
		/*
		Robot r = new Robot();
		
		r.mouseMove(486, 978);
		leftClick();	
		logger.info("        Clicked Keep button.");		
		Thread.sleep(5000);		
		
		logger.info("*** Step 5 Actions: - Click the downloaded the jnlp file at the bottom of the browser window");		
		r.mouseMove(220, 979);
		leftClick();		
		logger.info("        Clicked downloaded jnlp file.");		
		Thread.sleep(5000);		
		
		logger.info("*** Step 6 Actions: - Click the Continue button");		
		r.mouseMove(1178, 600);
		leftClick();	
		logger.info("        Clicked Continue button.");		
		Thread.sleep(5000);			
		
		logger.info("*** Step 7 Actions: - Click the Run button");
		r.mouseMove(1156, 612);
		leftClick();		
		logger.info("        Clicked Run button.");		
		Thread.sleep(10000);	
		
		logger.info("*** Step 8 Actions: - Click the Run with the latest version button");
		r.mouseMove(1056, 603);
		leftClick();	
		logger.info("        Clicked Run with the latest version button.");			
		Thread.sleep(10000);
		*/
		
		
		
		
		
		
		
		
		
		Robot r = new Robot();
		 r.keyPress(KeyEvent.VK_TAB);
		 r.keyPress(KeyEvent.VK_TAB);
		 r.keyPress(KeyEvent.VK_TAB);
		 r.keyPress(KeyEvent.VK_TAB);
		 r.keyPress(KeyEvent.VK_TAB);
		 r.keyPress(KeyEvent.VK_TAB);
		 r.keyPress(KeyEvent.VK_TAB		);
		 leftClick();
		
		/*
		PointerInfo a = MouseInfo.getPointerInfo();
		Point b = a.getLocation();
		int x = (int) b.getX();
		int y = (int) b.getY();
		System.out.println("x=" + x);
		System.out.println("y=" + y);
		
		Thread.sleep(8000);
		*/
		
		/*
        Robot r = new Robot();    
        
        Thread.sleep(8000);
        r.keyPress(KeyEvent.VK_ENTER);
        r.keyRelease(KeyEvent.VK_ENTER);
        
        logger.info("*** Step 5 Actions: Click Run button.");	
        Thread.sleep(15000);
        r.keyPress(KeyEvent.VK_TAB);
        r.keyRelease(KeyEvent.VK_TAB);
        
        Thread.sleep(8000);
        r.keyPress(KeyEvent.VK_ENTER);
        r.keyRelease(KeyEvent.VK_ENTER);
        
        logger.info("*** Step 6 Actions: Click on Run with the latest verson button on Warning popup.");	
        Thread.sleep(15000);
        r.keyPress(KeyEvent.VK_TAB);
        r.keyRelease(KeyEvent.VK_TAB);
        
        Thread.sleep(8000);
        r.keyPress(KeyEvent.VK_ENTER);
        r.keyRelease(KeyEvent.VK_ENTER);
        
        Thread.sleep(12000);       
		*/
		
		
		
		
		
		
		
		
																		
		driver.close();
    }	
	
	  private void leftClick() throws AWTException
	  {
		  Robot robot = new Robot();
		  robot.mousePress(InputEvent.BUTTON1_MASK);
		  robot.delay(200);
		  robot.mouseRelease(InputEvent.BUTTON1_MASK);
		  robot.delay(200);
	  }
	
}
