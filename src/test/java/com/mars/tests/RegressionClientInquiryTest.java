package com.mars.tests;

//import java.io.File;
//import java.net.URL;
import com.overall.client.clientInquiry.PriceInquiry;
import com.overall.client.clientNavigation.ClientNavigation;
import com.overall.client.clientProcessing.ClientDemographics;
import com.overall.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
import com.overall.fileMaintenance.sysMgt.DataCacheConfiguration;
import com.overall.headerNavigation.HeaderNavigation;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.TimeStamp;
import com.xifin.xap.utils.XifinAdminUtils;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;

//import java.util.List;
//import com.rpm.accession.accessionNavigation.AccessionNavigation;
//import com.rpm.accession.accessionProcessing.AccessionDemographics;
//import com.rpm.accession.accessionProcessing.AccessionSearch;
//import com.rpm.accession.accessionProcessing.AccessionSingleStatement;
//import com.rpm.accession.accessionProcessing.AccessionTransactionDetail;
//import com.rpm.client.clientProcessing.AuditLog;
//import com.rpm.client.clientProcessing.EligCensusConfig;
//import com.rpm.client.clientProcessing.PayorExclusions;
//import com.rpm.client.clientProcessing.PhysicianAssignment;
//import com.rpm.fileMaintenance.orderProcessingConfig.FileMaintencePatternDefinition;
//import com.rpm.fileMaintenance.sysMgt.TaskScheduler;
//import com.rpm.fileMaintenance.sysMgt.TaskStatus;
//import com.rpm.help.Help;
//import com.rpm.accession.orderProcessing.AccnTestUpdateOld;
//import com.rpm.payor.payorDemographics.DialysisFrequencyControl;
//import com.rpm.payor.payorDemographics.GroupDemographics;
//import com.rpm.payor.payorDemographics.PricingConfig;
//import com.rpm.payor.payorNavigation.PayorNavigation;
//import com.rpm.accession.accessionProcessing.superSearch.SuperSearch;
//import com.rpm.accession.accessionProcessing.superSearch.SuperSearchResults;
//import com.xifin.utils.Parser;
//import com.xifin.utils.RandomCharacter;
//import com.xifin.utils.TestDataSetup;

public class RegressionClientInquiryTest extends SeleniumBaseTest  {
//	private Parser parser;
	private SsoLogin ssoLogin;
	private HeaderNavigation headerNavigation;
//	private Help help;
//	private AccessionNavigation accessionNavigation;
//	private SuperSearch superSearch;
//	private SuperSearchResults superSearchResults;
//	private AccessionDemographics accessionDemographics;
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
	private TimeStamp timeStamp;
	private ClientNavigation clientNavigation;
//	private PayorExclusions payorExclusions;
//	private AuditLog auditLog;
//	private PhysicianAssignment physicianAssignment;
//	private EligCensusConfig eligCensusConfig;
//	private RandomCharacter randomCharacter;
	private ClientDemographics clientDemographics;
	private PriceInquiry priceInquiry;
	
	@Test(priority = 1, description = "Client pricing can be loaded when SS4029 and 4065 are null")
	@Parameters({"email", "password"})
	public void testRPM_723(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_723 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
				
		logger.info("*** Step 2 Actions: - Clear the value for System Setting 4029 and 4065 in DB");
		daoManagerXifinRpm.setSysSettings("null", 4029, testDb);
		daoManagerXifinRpm.setSysSettings("null", 4065, testDb);
				
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
        
        driver.close();
        switchToWin(winHandler);      
					
		logger.info("*** Step 5 Actions: - Navigate to Client Tab > Price Inquiry JSP");
		switchToDefaultWinFromFrame();	
		winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToPriceInquiry();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that it's on the Client - Price Inquiry page");		
		priceInquiry = new PriceInquiry(driver);
		Assert.assertTrue(priceInquiry.pageTitleText().trim().contains("Client - Price Inquiry"), "          Client - Price Inquiry page title should show.");
		
		logger.info("*** Step 6 Actions: - Enter an existing Client ID in Client ID field and tab out");
		String clnAbbrev = daoManagerAccnWS.getClnAbbrev(testDb);
		priceInquiry.setClnId(clnAbbrev);
		
		logger.info("*** Step 6 Expected Results: - Verify that the Client Name displays");
		ArrayList<String> clnList = daoManagerClientWS.getClientInfo(clnAbbrev, testDb);
		String clnName = clnList.get(1).trim(); 
		Assert.assertTrue(priceInquiry.nameInput().getAttribute("value").trim().contains(clnName), "        Name: " + clnName + " should show.");
		
		logger.info("*** Step 7 Actions: - Click Add Row button");
		priceInquiry.addRowBtn().click();		
		
		logger.info("*** Step 8 Actions: - Enter an existing Test ID in Test ID field and tab out");
		String testAbbrev = daoManagerAccnWS.getTestAbbrev(testDb);		
		priceInquiry.setTestId(testAbbrev, 3);
		
		logger.info("*** Step 8 Expected Results: - Verify that the Test Name displays");
		ArrayList<String> testList = daoManagerXifinRpm.getTestInfoFromTESTByTestAbbrev(testAbbrev, testDb);
		String testName = testList.get(1).trim();		
		Assert.assertTrue(priceInquiry.TestDescInput(3).getText().trim().contains(testName), "        Test Desc: " + testName + " should show.");
				
		logger.info("*** Step 8 Expected Results: - Verify that the pricing can be loaded");
		Assert.assertTrue(priceInquiry.procCdInfoClientTableText().isDisplayed(), "        The Client/Non-Client Pricing grids should show.");
		Assert.assertTrue(priceInquiry.procCdInfoNonClientTableText().isDisplayed(), "        The Non-Client Pricing grids should show.");
		Assert.assertTrue(priceInquiry.procCdInfoPyrSpecTableText().isDisplayed(), "        The Payor-Specific Pricing grids should show.");

		logger.info("*** Step 9 Actions: - Click Reset button");
		priceInquiry.resetBtn().click();
	
		driver.close();	
	}		

	@Test(priority = 1, description = "Client pricing can be loaded when SS4029, 4065 have values")
	@Parameters({"email", "password"})
	public void testRPM_724(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_724 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
				
		logger.info("*** Step 2 Actions: - Set values for System Setting 4029 and 4065 in DB");		
		daoManagerXifinRpm.setSysSettings("4", 4029, testDb);
		
		timeStamp = new TimeStamp(driver);
		String dataVal = "'" + timeStamp.getCurrentDate() + "^4" + "'";		
		daoManagerXifinRpm.setSysSettings(dataVal, 4065, testDb);
				
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
        
        driver.close();
        switchToWin(winHandler);      
					
		logger.info("*** Step 5 Actions: - Navigate to Client Tab > Price Inquiry JSP");
		switchToDefaultWinFromFrame();	
		winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToPriceInquiry();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that it's on the Client - Price Inquiry page");		
		priceInquiry = new PriceInquiry(driver);
		Assert.assertTrue(priceInquiry.pageTitleText().trim().contains("Client - Price Inquiry"), "          Client - Price Inquiry page title should show.");
		
		logger.info("*** Step 6 Actions: - Enter an existing Client ID in Client ID field and tab out");
		String clnAbbrev = daoManagerAccnWS.getClnAbbrev(testDb);
		priceInquiry.setClnId(clnAbbrev);
		
		logger.info("*** Step 6 Expected Results: - Verify that the Client Name displays");
		ArrayList<String> clnList = daoManagerClientWS.getClientInfo(clnAbbrev, testDb);
		String clnName = clnList.get(1).trim(); 
		Assert.assertTrue(priceInquiry.nameInput().getAttribute("value").trim().contains(clnName), "        Name: " + clnName + " should show.");
		
		logger.info("*** Step 7 Actions: - Click Add Row button");
		priceInquiry.addRowBtn().click();		
		
		logger.info("*** Step 8 Actions: - Enter an existing Test ID in Test ID field and tab out");
		String testAbbrev = daoManagerAccnWS.getTestAbbrev(testDb);		
		priceInquiry.setTestId(testAbbrev, 3);
		
		logger.info("*** Step 8 Expected Results: - Verify that the Test Name displays");
		ArrayList<String> testList = daoManagerXifinRpm.getTestInfoFromTESTByTestAbbrev(testAbbrev, testDb);
		String testName = testList.get(1).trim();		
		Assert.assertTrue(priceInquiry.TestDescInput(3).getText().trim().contains(testName), "        Test Desc: " + testName + " should show.");
				
		logger.info("*** Step 8 Expected Results: - Verify that the pricing can be loaded");
		Assert.assertTrue(priceInquiry.procCdInfoClientTableText().isDisplayed(), "        The Client/Non-Client Pricing grids should show.");
		Assert.assertTrue(priceInquiry.procCdInfoNonClientTableText().isDisplayed(), "        The Non-Client Pricing grids should show.");
		Assert.assertTrue(priceInquiry.procCdInfoPyrSpecTableText().isDisplayed(), "        The Payor-Specific Pricing grids should show.");

		logger.info("*** Step 9 Actions: - Click Reset button");
		priceInquiry.resetBtn().click();
				
		logger.info("*** Step 10 Actions: - Clear the value for System Setting 4029 and 4065 in DB");
		daoManagerXifinRpm.setSysSettings("null", 4029, testDb);
		daoManagerXifinRpm.setSysSettings("null", 4065, testDb);
				
        logger.info("*** Step 11 Actions: - Navigate to the File Maintenance Page");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
        winHandler = driver.getWindowHandle();
        headerNavigation.navigateToFileMaintenanceTab();
        switchToPopupWin();        

        logger.info("*** Step 12 Actions: - Navigate to the System Data Cache Page");      
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
        fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
        fileMaintenaceNavigation.navigateToSystemDataCacheLink();
        switchToPopupWin();               
        
        logger.info("*** Step 12 Expected Results: - The Data Cache Configuration page displays");
        dataCacheConfiguration = new DataCacheConfiguration(driver);
        Assert.assertTrue(dataCacheConfiguration.pageTitleText().getText().contains("Data Cache Configuration"), "        Data Cache Configuration page title should show.");

		xifinAdminUtils.clearDataCache();
	
		driver.close();	
	}	
	
	@Test(priority = 1, description = "Help")
	@Parameters({"email", "password"})
	public void testRPM_725(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_725 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
			
		logger.info("*** Step 3 Actions: - Click Price Inquiry link");
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.priceInquiryLink();
		
		logger.info("*** Step 4 Actions: - Click Help Button");
		clientDemographics = new ClientDemographics(driver, wait);
		clientDemographics.helpBtnInFrame();
		switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Verify that Client Pricing Inquiry Help file opens");
		Assert.assertTrue(driver.getTitle().trim().contains("Client Pricing Inquiry"), "        Client Pricing Inquiry help file should show.");

		driver.close();	
	}
	
	@Test(priority = 1, description = "Reset")
	@Parameters({"email", "password"})
	public void testRPM_726(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_726 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
						
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Price Inquiry JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToPriceInquiry();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Price Inquiry page");		
		priceInquiry = new PriceInquiry(driver);
		Assert.assertTrue(priceInquiry.pageTitleText().trim().contains("Client - Price Inquiry"), "          Client - Price Inquiry page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter an existing Client ID in Client ID field and tab out");
		String clnAbbrev = daoManagerAccnWS.getClnAbbrev(testDb);
		priceInquiry.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Client Name displays");
		ArrayList<String> clnList = daoManagerClientWS.getClientInfo(clnAbbrev, testDb);
		String clnName = clnList.get(1).trim(); 
		Assert.assertTrue(priceInquiry.nameInput().getAttribute("value").trim().contains(clnName), "        Name: " + clnName + " should show.");
		
		logger.info("*** Step 4 Actions: - Click Add Row button");
		priceInquiry.addRowBtn().click();		
		
		logger.info("*** Step 5 Actions: - Enter an existing Test ID in Test ID field and tab out");
		String testAbbrev = daoManagerAccnWS.getTestAbbrev(testDb);		
		priceInquiry.setTestId(testAbbrev, 3);
		
		logger.info("*** Step 5 Expected Results: - Verify that the Test Name displays");
		ArrayList<String> testList = daoManagerXifinRpm.getTestInfoFromTESTByTestAbbrev(testAbbrev, testDb);
		String testName = testList.get(1).trim();		
		Assert.assertTrue(priceInquiry.TestDescInput(3).getText().trim().contains(testName), "        Test Desc: " + testName + " should show.");
				
		logger.info("*** Step 5 Expected Results: - Verify that the pricing can be loaded");
		Assert.assertTrue(priceInquiry.procCdInfoClientTableText().isDisplayed(), "        The Client/Non-Client Pricing grids should show.");
		Assert.assertTrue(priceInquiry.procCdInfoNonClientTableText().isDisplayed(), "        The Non-Client Pricing grids should show.");
		Assert.assertTrue(priceInquiry.procCdInfoPyrSpecTableText().isDisplayed(), "        The Payor-Specific Pricing grids should show.");

		logger.info("*** Step 6 Actions: - Click Reset button");
		priceInquiry.resetBtn().click();
		
		logger.info("*** Step 6 Expected Results: - Verify that the value in Client ID field is cleared");
		Assert.assertTrue(priceInquiry.clnIdInput().getAttribute("value").trim().isEmpty(), "        Client ID field value should be cleared.");
			
		driver.close();	
	}
	
}
