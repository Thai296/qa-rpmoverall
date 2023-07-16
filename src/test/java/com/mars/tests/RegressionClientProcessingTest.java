package com.mars.tests;


//import java.io.File;
//import java.net.URL;
import com.overall.client.clientNavigation.ClientNavigation;
import com.overall.client.clientProcessing.*;
import com.overall.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
import com.overall.fileMaintenance.sysMgt.DataCacheConfiguration;
import com.overall.headerNavigation.HeaderNavigation;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.Parser;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;
import com.xifin.xap.utils.XifinAdminUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;

//import java.util.Calendar;
//import java.util.List;
//import org.apache.commons.lang3.StringUtils;
//import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
//import com.overall.accession.accessionNavigation.AccessionNavigation;
//import com.overall.accession.accessionProcessing.AccessionDemographics;
//import com.overall.accession.accessionProcessing.AccessionSearch;
//import com.overall.accession.accessionProcessing.AccessionSingleStatement;
//import com.overall.accession.accessionProcessing.AccessionTransactionDetail;
//import com.overall.fileMaintenance.orderProcessingConfig.FileMaintencePatternDefinition;
//import com.overall.fileMaintenance.sysMgt.TaskScheduler;
//import com.overall.fileMaintenance.sysMgt.TaskStatus;
//import com.overall.help.Help;
//import com.overall.accession.orderProcessing.AccnTestUpdateOld;
//import com.overall.payor.payorDemographics.DialysisFrequencyControl;
//import com.overall.payor.payorDemographics.GroupDemographics;
//import com.overall.payor.payorDemographics.PricingConfig;
//import com.overall.payor.payorNavigation.PayorNavigation;
//import com.overall.accession.accessionProcessing.superSearch.SuperSearch;
//import com.overall.accession.accessionProcessing.superSearch.SuperSearchResults;
//import com.xifin.utils.TestDataSetup;



public class RegressionClientProcessingTest extends SeleniumBaseTest  {
	
	private Parser parser;
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
	private PayorExclusions payorExclusions;
	private AuditLog auditLog;
	private PhysicianAssignment physicianAssignment;
	private EligCensusConfig eligCensusConfig;
	private RandomCharacter randomCharacter;
	private ClientDemographics clientDemographics;
	
/*
	@Test(priority = 1, description = "Payor Exclusions Verify that new Payor exclusion can be added")
	@Parameters({"email", "password"})
	public void testRPM_443(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_443 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Payor Exclusions JSP");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Payor Exclusions page		
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToPyrExclusionsLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Payor Exclusions page");
		payorExclusions = new PayorExclusions(driver);
		Assert.assertTrue(payorExclusions.clnIdInput().isDisplayed(), "       Client ID Input field should show.");
		
		logger.info("*** Step 3 Actions: - Load a Client that does not have any Payor Group or Payor exclusions and tab out");
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevWOPyrExclusionsFromCLN(testDb);
		String clnAbbrev = clnList.get(1);
		payorExclusions.setClnID(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify tha the Client was loaded properly");	
		Assert.assertEquals(payorExclusions.clnNameInput().getAttribute("value"), clnList.get(2));	
		
		logger.info("*** Step 4 Actions: - Click Add Payor Exclusion button to add a new Payor exclusion and click Submit button");
		payorExclusions.addPyrExclusionBtn().click();
		payorExclusions.setPyrEffDate("t");
		String pyrAbbrev = daoManagerAccnWS.getPayorAbbrev(testDb);
		payorExclusions.setPyrID(pyrAbbrev);
		payorExclusions.submitBtn().click();
		Thread.sleep(3000);		
		
		logger.info("*** Step 4 Expected Results: - Verify that the changes were saved to DB");
		String effDt = daoManagerXifinRpm.getEffDtFromPYRCLNEXCLByClnAbbrevPyrAbbrev(clnAbbrev, pyrAbbrev, testDb);
		timeStamp = new TimeStamp();		
		Assert.assertEquals(effDt, timeStamp.getCurrentDate("MM/dd/yyyy"));
		
		logger.info("*** Step 5 Actions: - Load the same Client in Client ID field and tab out");
		payorExclusions.setClnID(clnAbbrev);
		
		logger.info("*** Step 5 Expected Results: - Verify that the Client was loaded properly");	
		Assert.assertEquals(payorExclusions.clnNameInput().getAttribute("value"), clnList.get(2));
		
		logger.info("*** Step 5 Expected Results: - Verify that the newly added Payor exlusion displays properly");	
		Assert.assertEquals(payorExclusions.pyrEffDateInput().getAttribute("value"), effDt);
		Assert.assertEquals(payorExclusions.pyrIDInput().getAttribute("value"), pyrAbbrev);
		
		logger.info("*** Step 6 Actions: - Run a sql query to removed the Payor exclusion from DB for the Client");	
		daoManagerXifinRpm.deleteClnPyrExclFromPYRCLNEXCLByClnAbbrevPyrAbbrev(clnAbbrev, pyrAbbrev, testDb);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 7 Actions: - Navigate to File Maintenance tab>System Data Cache JSP");	
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();				
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();		
	
		logger.info("*** Step 7 Actions: - Clear the Cache");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		dataCacheConfiguration.setClearCacheAll(this);		
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();	
	}
	
	@Test(priority = 1, description = "Payor Exclusions Verify that new Payor Group exclusion can be added")
	@Parameters({"email", "password"})
	public void testRPM_444(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_444 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Payor Exclusions JSP");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Payor Exclusions page		
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToPyrExclusionsLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Payor Exclusions page");
		payorExclusions = new PayorExclusions(driver);
		Assert.assertTrue(payorExclusions.clnIdInput().isDisplayed(), "       Client ID Input field should show.");
		
		logger.info("*** Step 3 Actions: - Load a Client that does not have any Payor Group or Payor exclusions and tab out");
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevWOPyrExclusionsFromCLN(testDb);
		String clnAbbrev = clnList.get(1);
		payorExclusions.setClnID(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify tha the Client was loaded properly");	
		Assert.assertEquals(payorExclusions.clnNameInput().getAttribute("value"), clnList.get(2));	
		
		logger.info("*** Step 4 Actions: - Click Add Payor Group Exclusion button to add a new Payor Group exclusion and click Submit button");
		payorExclusions.addPyrGrpExclusionBtn().click();
		payorExclusions.setPyrGrpEffDate("t");
		//"4" = Patient
		payorExclusions.setPyrGrp("4");
		payorExclusions.submitBtn().click();
		Thread.sleep(3000);		
		
		logger.info("*** Step 4 Expected Results: - Verify that the changes were saved to DB");
		String effDt = daoManagerXifinRpm.getEffDtFromPYRGRPCLNEXCLByClnAbbrevGrpName(clnAbbrev, "Patient", testDb);
		timeStamp = new TimeStamp();		
		Assert.assertEquals(effDt, timeStamp.getCurrentDate("MM/dd/yyyy"));
		
		logger.info("*** Step 5 Actions: - Load the same Client in Client ID field and tab out");
		payorExclusions.setClnID(clnAbbrev);
		
		logger.info("*** Step 5 Expected Results: - Verify that the Client was loaded properly");	
		Assert.assertEquals(payorExclusions.clnNameInput().getAttribute("value"), clnList.get(2));
		
		logger.info("*** Step 5 Expected Results: - Verify that the newly added Payor Group exlusion displays properly");	
		Assert.assertEquals(payorExclusions.pyrGrpEffDateInput().getAttribute("value"), effDt);
		Assert.assertEquals(payorExclusions.pyrGrpDropDown().getText().trim(), "Patient");
		
		logger.info("*** Step 6 Actions: - Run a sql query to removed the Payor Group exclusion from DB for the Client");	
		daoManagerXifinRpm.deleteClnPyrGrpExclFromPYRGRPCLNEXCLByClnAbbrevGrpName(clnAbbrev, "Patient", testDb);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 7 Actions: - Navigate to File Maintenance tab>System Data Cache JSP");	
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();				
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();		
	
		logger.info("*** Step 7 Actions: - Clear the Cache");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		dataCacheConfiguration.setClearCacheAll(this);		
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();	
	}
	
	@Test(priority = 1, description = "Payor Exclusions Verify that modified Payor exclusion can be saved")
	@Parameters({"email", "password"})
	public void testRPM_446(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_446 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Payor Exclusions JSP");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Payor Exclusions page		
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToPyrExclusionsLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Payor Exclusions page");
		payorExclusions = new PayorExclusions(driver);
		Assert.assertTrue(payorExclusions.clnIdInput().isDisplayed(), "       Client ID Input field should show.");
		
		logger.info("*** Step 3 Actions: - Load a Client that does not have any Payor Group or Payor exclusions and tab out");
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevWOPyrExclusionsFromCLN(testDb);
		String clnAbbrev = clnList.get(1);
		payorExclusions.setClnID(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify tha the Client was loaded properly");	
		Assert.assertEquals(payorExclusions.clnNameInput().getAttribute("value"), clnList.get(2));	
		
		logger.info("*** Step 4 Actions: - Click Add Payor Exclusion button to add a new Payor exclusion and click Submit button");
		payorExclusions.addPyrExclusionBtn().click();
		payorExclusions.setPyrEffDate("t");
		String pyrAbbrev = daoManagerAccnWS.getPayorAbbrev(testDb);
		payorExclusions.setPyrID(pyrAbbrev);
		payorExclusions.submitBtn().click();
		Thread.sleep(3000);		
		
		logger.info("*** Step 4 Expected Results: - Verify that the changes were saved to DB");
		String effDt = daoManagerXifinRpm.getEffDtFromPYRCLNEXCLByClnAbbrevPyrAbbrev(clnAbbrev, pyrAbbrev, testDb);
		timeStamp = new TimeStamp();		
		Assert.assertEquals(effDt, timeStamp.getCurrentDate("MM/dd/yyyy"));
		
		logger.info("*** Step 5 Actions: - Load the same Client in Client ID field and tab out");
		payorExclusions.setClnID(clnAbbrev);
		
		logger.info("*** Step 5 Expected Results: - Verify that the Client was loaded properly");	
		Assert.assertEquals(payorExclusions.clnNameInput().getAttribute("value"), clnList.get(2));
		
		logger.info("*** Step 5 Expected Results: - Verify that the newly added Payor exlusion displays properly");	
		Assert.assertEquals(payorExclusions.pyrEffDateInput().getAttribute("value"), effDt);		
		Assert.assertEquals(payorExclusions.pyrIDInput().getAttribute("value"), pyrAbbrev);
		
		logger.info("*** Step 6 Actions: - Add an expiration date (>= eff_dt) to the Payor exclusion and tab out and click Submit button ");	
		//t+10 = today's date + 10
		payorExclusions.setPyrExpDate("t+10");
		payorExclusions.submitBtn().click();
		Thread.sleep(3000);
		
		logger.info("*** Step 6 Expected Results: - Verify that the changes were saved to DB");
		String expDt = daoManagerXifinRpm.getExpDtFromPYRCLNEXCLByClnAbbrevPyrAbbrev(clnAbbrev, pyrAbbrev, testDb);
		Assert.assertEquals(expDt, timeStamp.getFutureDate("MM/dd/yyyy", 10));
		
		logger.info("*** Step 7 Actions: - Load the same Client in Client ID field and tab out");
		payorExclusions.setClnID(clnAbbrev);
		
		logger.info("*** Step 7 Expected Results: - Verify that the Client was loaded properly");	
		Assert.assertEquals(payorExclusions.clnNameInput().getAttribute("value"), clnList.get(2));
		
		logger.info("*** Step 7 Expected Results: - Verify that the newly added Payor exlusion Expiration Date displays properly");	
		Assert.assertEquals(payorExclusions.pyrEffDateInput().getAttribute("value"), effDt);		
		Assert.assertEquals(payorExclusions.pyrIDInput().getAttribute("value"), pyrAbbrev);
		Assert.assertEquals(payorExclusions.pyrExpDateInput().getAttribute("value"), expDt);
		
		logger.info("*** Step 8 Actions: - Clear the expiration date for the Payor exclusion and tab out and click Submit button");
		payorExclusions.clearPyrExpDate();		
		payorExclusions.submitBtn().click();
		Thread.sleep(3000);
		
		logger.info("*** Step 8 Expected Results: - Verify that the changes were saved to DB");
		//Get the eff_dt for the record that exp_dt = null
		effDt = daoManagerXifinRpm.getEffDtFromPYRCLNEXCLByClnAbbrevPyrAbbrev(clnAbbrev, pyrAbbrev, testDb);			
		Assert.assertEquals(effDt, timeStamp.getCurrentDate("MM/dd/yyyy"));
		
		logger.info("*** Step 9 Actions: - Load the same Client in Client ID field and tab out");
		payorExclusions.setClnID(clnAbbrev);
		
		logger.info("*** Step 9 Expected Results: - Verify that the Client was loaded properly");	
		Assert.assertEquals(payorExclusions.clnNameInput().getAttribute("value"), clnList.get(2));
		
		logger.info("*** Step 9 Expected Results: - Verify that the Payor exlusion Expiration Date is blank");	
		Assert.assertEquals(payorExclusions.pyrEffDateInput().getAttribute("value"), effDt);		
		Assert.assertEquals(payorExclusions.pyrIDInput().getAttribute("value"), pyrAbbrev);
		Assert.assertTrue(payorExclusions.pyrExpDateInput().getAttribute("value").isEmpty(), "        Payor Exclusion Exp Date should be blank.");
				
		logger.info("*** Step 10 Actions: - Run a sql query to removed the Payor exclusion from DB for the Client");	
		daoManagerXifinRpm.deleteClnPyrExclFromPYRCLNEXCLByClnAbbrevPyrAbbrev(clnAbbrev, pyrAbbrev, testDb);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 11 Actions: - Navigate to File Maintenance tab>System Data Cache JSP");	
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();				
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();		
	
		logger.info("*** Step 12 Actions: - Clear the Cache");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		dataCacheConfiguration.setClearCacheAll(this);		
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();	
	}
	
	@Test(priority = 1, description = "Payor Exclusions Verify that modified Payor Group exclusion can be saved")
	@Parameters({"email", "password"})
	public void testRPM_447(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_447 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Payor Exclusions JSP");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Payor Exclusions page		
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToPyrExclusionsLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Payor Exclusions page");
		payorExclusions = new PayorExclusions(driver);
		Assert.assertTrue(payorExclusions.clnIdInput().isDisplayed(), "       Client ID Input field should show.");
		
		logger.info("*** Step 3 Actions: - Load a Client that does not have any Payor Group or Payor exclusions and tab out");
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevWOPyrExclusionsFromCLN(testDb);
		String clnAbbrev = clnList.get(1);
		payorExclusions.setClnID(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify tha the Client was loaded properly");	
		Assert.assertEquals(payorExclusions.clnNameInput().getAttribute("value"), clnList.get(2));	
		
		logger.info("*** Step 4 Actions: - Click Add Payor Group Exclusion button to add a new Payor Group exclusion and click Submit button");
		payorExclusions.addPyrGrpExclusionBtn().click();
		payorExclusions.setPyrGrpEffDate("t");
		//"4" = Patient
		payorExclusions.setPyrGrp("4");
		payorExclusions.submitBtn().click();
		Thread.sleep(3000);		
		
		logger.info("*** Step 4 Expected Results: - Verify that the changes were saved to DB");
		String effDt = daoManagerXifinRpm.getEffDtFromPYRGRPCLNEXCLByClnAbbrevGrpName(clnAbbrev, "Patient", testDb);
		timeStamp = new TimeStamp();		
		Assert.assertEquals(effDt, timeStamp.getCurrentDate("MM/dd/yyyy"));
		
		logger.info("*** Step 5 Actions: - Load the same Client in Client ID field and tab out");
		payorExclusions.setClnID(clnAbbrev);
		
		logger.info("*** Step 5 Expected Results: - Verify that the Client was loaded properly");	
		Assert.assertEquals(payorExclusions.clnNameInput().getAttribute("value"), clnList.get(2));
		
		logger.info("*** Step 5 Expected Results: - Verify that the newly added Payor Group exclusion displays properly");	
		Assert.assertEquals(payorExclusions.pyrGrpEffDateInput().getAttribute("value"), effDt);
		Assert.assertEquals(payorExclusions.pyrGrpDropDown().getText().trim(), "Patient");
		
		logger.info("*** Step 6 Actions: - Add an expiration date (>= eff_dt) to the Payor Group exclusion and tab out and click Submit button ");	
		//t+10 = today's date + 10
		payorExclusions.setPyrGrpExpDate("t+10");
		payorExclusions.submitBtn().click();
		Thread.sleep(3000);
		
		logger.info("*** Step 6 Expected Results: - Verify that the changes were saved to DB");
		String expDt = daoManagerXifinRpm.getExpDtFromPYRGRPCLNEXCLByClnAbbrevGrpName(clnAbbrev, "Patient", testDb);
		Assert.assertEquals(expDt, timeStamp.getFutureDate("MM/dd/yyyy", 10));
		
		logger.info("*** Step 7 Actions: - Load the same Client in Client ID field and tab out");
		payorExclusions.setClnID(clnAbbrev);
		
		logger.info("*** Step 7 Expected Results: - Verify that the Client was loaded properly");	
		Assert.assertEquals(payorExclusions.clnNameInput().getAttribute("value"), clnList.get(2));
		
		logger.info("*** Step 7 Expected Results: - Verify that the newly added Payor Group exlusion Expiration Date displays properly");	
		Assert.assertEquals(payorExclusions.pyrGrpEffDateInput().getAttribute("value"), effDt);		
		Assert.assertEquals(payorExclusions.pyrGrpDropDown().getText().trim(), "Patient");
		Assert.assertEquals(payorExclusions.pyrGrpExpDateInput().getAttribute("value"), expDt);
		
		logger.info("*** Step 8 Actions: - Clear the expiration date for the Payor Group exclusion and tab out and click Submit button");
		payorExclusions.clearPyrGrpExpDate();		
		payorExclusions.submitBtn().click();
		Thread.sleep(3000);
		
		logger.info("*** Step 8 Expected Results: - Verify that the changes were saved to DB");
		//Get the eff_dt for the record that exp_dt = null
		effDt = daoManagerXifinRpm.getEffDtFromPYRGRPCLNEXCLByClnAbbrevGrpName(clnAbbrev, "Patient", testDb);			
		Assert.assertEquals(effDt, timeStamp.getCurrentDate("MM/dd/yyyy"));
		
		logger.info("*** Step 9 Actions: - Load the same Client in Client ID field and tab out");
		payorExclusions.setClnID(clnAbbrev);
		
		logger.info("*** Step 9 Expected Results: - Verify that the Client was loaded properly");	
		Assert.assertEquals(payorExclusions.clnNameInput().getAttribute("value"), clnList.get(2));
		
		logger.info("*** Step 9 Expected Results: - Verify that the Payor Group exlusion Expiration Date is blank");	
		Assert.assertEquals(payorExclusions.pyrGrpEffDateInput().getAttribute("value"), effDt);		
		Assert.assertEquals(payorExclusions.pyrGrpDropDown().getText().trim(), "Patient");
		Assert.assertTrue(payorExclusions.pyrGrpExpDateInput().getAttribute("value").isEmpty(), "        Payor Group Exclusion Exp Date should be blank.");
				
		logger.info("*** Step 10 Actions: - Run a sql query to removed the Payor Group exclusion from DB for the Client");	
		daoManagerXifinRpm.deleteClnPyrGrpExclFromPYRGRPCLNEXCLByClnAbbrevGrpName(clnAbbrev, "Patient", testDb);
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 11 Actions: - Navigate to File Maintenance tab>System Data Cache JSP");	
		headerNavigation = new HeaderNavigation(driver, config);
		headerNavigation.navigateToFileMaintenanceTab();
		switchToPopupWin();				
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		fileMaintenaceNavigation = new FileMaintenanceNavigation(driver, config);
		fileMaintenaceNavigation.navigateToSystemDataCacheLink();
		switchToPopupWin();		
	
		logger.info("*** Step 12 Actions: - Clear the Cache");
		dataCacheConfiguration = new DataCacheConfiguration(driver);
		dataCacheConfiguration.setClearCacheAll(this);		
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();	
	}
	*/
	
	@Test(priority = 1, description = "Audit Log  Tooltip displays full text")
	@Parameters({"email", "password"})
	public void testRPM_502(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_502 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Audit Log JSP");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Audit Log page		
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToAuditLogLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Audit Log page");
		auditLog = new AuditLog(driver, config);
		Assert.assertTrue(auditLog.clnIdInput().isDisplayed(), "       Client ID Input field should show.");
		
		logger.info("*** Step 3 Actions: - Update CLN.FK_CLN_LTR_TYP_ID field to different value for an existing Client");	
		//Get the existing Client Abbrev
		ArrayList<String> clnList = daoManagerClientWS.getExistClient(testDb);		
		String clnAbbrev = clnList.get(0);
		
		//Get the CLN.FK_CLN_LTR_TYP_ID
		ArrayList<String> clnDescrList = daoManagerXifinRpm.getClnLtrTypAcctNameTypFromCLNByClnAbbrev(clnAbbrev, testDb);
		String clnLtrTypId = clnDescrList.get(0);
		
		//Update CLN.FK_CLN_LTR_TYP_ID
		auditLog.updateClnLtrType(clnLtrTypId, clnAbbrev, testDb);
		
		logger.info("*** Step 4 Actions: - Load the Client and tab out");
		String clnName = clnList.get(1);
		auditLog.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Client was loaded properly");	
		Assert.assertEquals(auditLog.nameInput().getAttribute("value").trim(), clnName);	
			
		logger.info("*** Step 4 Expected Results: - Verify that FK_CLN_LTR_TYP_ID tooltip displays full text");
		Assert.assertTrue(auditLog.toolTipText("FK_CLN_LTR_TYP_ID").isDisplayed(), "        FK_CLN_LTR_TYP_ID tooltip should display full text.");
	
		driver.close();
	}
	
	@Test(priority = 1, description = "Audit Log  Client info display properly when loaded a Client")
	@Parameters({"email", "password"})
	public void testRPM_503(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_503 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Audit Log JSP");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Audit Log page		
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToAuditLogLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Audit Log page");
		auditLog = new AuditLog(driver, config);
		Assert.assertTrue(auditLog.clnIdInput().isDisplayed(), "       Client ID Input field should show.");
		
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");
		//Get the existing Client Abbrev and Name
		ArrayList<String> clnList = daoManagerClientWS.getExistClient(testDb);		
		String clnAbbrev = clnList.get(0);		
		
		auditLog.setClnID(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Client Name and Account Type were loaded properly");
		//Get the Client name and the account type
		ArrayList<String> clnDescrList = daoManagerXifinRpm.getClnLtrTypAcctNameTypFromCLNByClnAbbrev(clnAbbrev, testDb);
		String clnAcctName = clnDescrList.get(1).trim();
		String clnAcctTyp = clnDescrList.get(2).trim();		
			
		Assert.assertEquals(auditLog.nameInput().getAttribute("value").trim(), clnAcctName);	
		Assert.assertEquals(auditLog.acctTypInput().getAttribute("value").trim(), clnAcctTyp);
		
		driver.close();
	}
	
	@Test(priority = 1, description = "Audit Log  Reset button clear the page")
	@Parameters({"email", "password"})
	public void testRPM_504(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_504 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Audit Log JSP");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Audit Log page		
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToAuditLogLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Audit Log page");
		auditLog = new AuditLog(driver, config);
		Assert.assertTrue(auditLog.clnIdInput().isDisplayed(), "       Client ID Input field should show.");
		
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");
		//Get the existing Client Abbrev and Name
		ArrayList<String> clnList = daoManagerClientWS.getExistClient(testDb);		
		String clnAbbrev = clnList.get(0);		
		
		auditLog.setClnID(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Client Name and Account Type were loaded properly");
		//Get the Client name and the account type
		ArrayList<String> clnDescrList = daoManagerXifinRpm.getClnLtrTypAcctNameTypFromCLNByClnAbbrev(clnAbbrev, testDb);
		String clnAcctName = clnDescrList.get(1).trim();
		String clnAcctTyp = clnDescrList.get(2).trim();		
			
		Assert.assertEquals(auditLog.nameInput().getAttribute("value").trim(), clnAcctName);	
		Assert.assertEquals(auditLog.acctTypInput().getAttribute("value").trim(), clnAcctTyp);
		
		logger.info("*** Step 4 Actions: - Click the Reset button");
		auditLog.resetBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that all the input fields were cleared");
		Assert.assertEquals(auditLog.clnIdInput().getAttribute("value").trim(), "");	
		Assert.assertEquals(auditLog.nameInput().getAttribute("value").trim(), "");	
		Assert.assertEquals(auditLog.acctTypInput().getAttribute("value").trim(), "");
		
		driver.close();
	}
	
	@Test(priority = 1, description = "Client Processing - Physician Assignment - Client Name and Account Type display")
	@Parameters({"email", "password"})
	public void testRPM_623(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_623 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Physician Assignment JSP");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Physician Assignment page		
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToPhysAssignment();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Physician Assignment page");
		physicianAssignment = new PhysicianAssignment(driver);
		Assert.assertTrue(physicianAssignment.pageTitleText().trim().contains("Client - Physician Assignment"), "        Client - Physician Assignment page title should show.");
		
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");
		//Get the existing Client Abbrev and Name
		ArrayList<String> clnList = daoManagerClientWS.getExistClient(testDb);		
		String clnAbbrev = clnList.get(0);		
		
		physicianAssignment.setClnID(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Client Name and Account Type were loaded properly");
		//Get the Client name and the account type
		ArrayList<String> clnDescrList = daoManagerXifinRpm.getClnLtrTypAcctNameTypFromCLNByClnAbbrev(clnAbbrev, testDb);
		String clnAcctName = clnDescrList.get(1).trim();
		String clnAcctTyp = clnDescrList.get(2).trim();		
			
		Assert.assertEquals(physicianAssignment.nameInput().getAttribute("value").trim(), clnAcctName);	
		Assert.assertEquals(physicianAssignment.acctTypInput().getAttribute("value").trim(), clnAcctTyp);
		
		logger.info("*** Step 4 Actions: - Click the Reset button");
		physicianAssignment.resetBtn().click();
		
		driver.close();
	}

	@Test(priority = 1, description = "Client Processing - Physician Assignment - Reset")
	@Parameters({"email", "password"})
	public void testRPM_629(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_629 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Physician Assignment JSP");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Physician Assignment page		
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToPhysAssignment();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Physician Assignment page");
		physicianAssignment = new PhysicianAssignment(driver);
		Assert.assertTrue(physicianAssignment.pageTitleText().trim().contains("Client - Physician Assignment"), "        Client - Physician Assignment page title should show.");
		
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");
		//Get the existing Client Abbrev and Name
		ArrayList<String> clnList = daoManagerClientWS.getExistClient(testDb);		
		String clnAbbrev = clnList.get(0);		
		
		physicianAssignment.setClnID(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Client Name and Account Type were loaded properly");
		//Get the Client name and the account type
		ArrayList<String> clnDescrList = daoManagerXifinRpm.getClnLtrTypAcctNameTypFromCLNByClnAbbrev(clnAbbrev, testDb);
		String clnAcctName = clnDescrList.get(1).trim();
		String clnAcctTyp = clnDescrList.get(2).trim();		
			
		Assert.assertEquals(physicianAssignment.nameInput().getAttribute("value").trim(), clnAcctName);	
		Assert.assertEquals(physicianAssignment.acctTypInput().getAttribute("value").trim(), clnAcctTyp);
		
		logger.info("*** Step 4 Actions: - Click the Reset button");
		physicianAssignment.resetBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that Client ID, Name and Account Type input fields were cleared");
		Assert.assertEquals(physicianAssignment.clnIdInput().getAttribute("value").trim(), "");	
		Assert.assertEquals(physicianAssignment.nameInput().getAttribute("value").trim(), "");	
		Assert.assertEquals(physicianAssignment.acctTypInput().getAttribute("value").trim(), "");
		
		driver.close();
	}
	
	@Test(priority = 1, description = "Client Processing - Physician Assignment - Help")
	@Parameters({"email", "password"})
	public void testRPM_628(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_628 *****");    	
		
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
		
		logger.info("*** Step 2 Actions: - Click Physician Assignment Link");			
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.physAssignmentLink();		
			
		logger.info("*** Step 3 Actions: - Click Help button on the Client Physician Assignment page");
		physicianAssignment = new PhysicianAssignment(driver);
		physicianAssignment.helpBtn();
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that Physician Assignment help page displays");		
		Assert.assertTrue(driver.getTitle().trim().contains("Client Physician Assignment"), "        Client Physician Assignment help should show.");
				
		driver.close();
	}
	
	@Test(priority = 1, description = "Client Processing - Physician Assignment - Add new Physician Assignment")
	@Parameters({"email", "password"})
	public void testRPM_624(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_624 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Remove Physician Assignment for the Client in DB");
		//Get the existing Client Abbrev and Name
		ArrayList<String> clnList = daoManagerClientWS.getExistClient(testDb);		
		String clnAbbrev = clnList.get(0);		
		daoManagerXifinRpm.deleteClnPhysAssignFromPHYSCLNByClnAbbrev(clnAbbrev, testDb);
		
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
		
		logger.info("*** Step 4 Actions: - Clear all the Cache");
		new XifinAdminUtils(driver, config).clearDataCache();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 5 Actions: - Navigate to Client Tab > Physician Assignment JSP");
		//switchToDefaultWinFromFrame();
		//String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Physician Assignment page		
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToPhysAssignment();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that it's on the Physician Assignment page");
		physicianAssignment = new PhysicianAssignment(driver);
		Assert.assertTrue(physicianAssignment.pageTitleText().trim().contains("Client - Physician Assignment"), "        Client - Physician Assignment page title should show.");
		
		logger.info("*** Step 6 Actions: - Load the Client in Client ID field and tab out");		
		physicianAssignment.setClnID(clnAbbrev);
		
		logger.info("*** Step 6 Actions: - Click Add Physician button");
		physicianAssignment.addPhysBtn().click();
		
		logger.info("*** Step 6 Actions: - Enter a Physcian's NPI");
		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		physicianAssignment.setNPI(npi);
		
		logger.info("*** Step 6 Actions: - Click Submit button");
		physicianAssignment.submitBtn().click();
		
		logger.info("*** Step 7 Actions: - Load the same Client in Client ID field and tab out");
		physicianAssignment.setClnID(clnAbbrev);	
		
		logger.info("*** Step 7 Expected Results: - Verify that the Physician's NPI was added properly");
		Assert.assertTrue(physicianAssignment.npiInput().getAttribute("value").trim().contains(npi), "        Physician's NPI " + npi + " should show.");
				
		logger.info("*** Step 8 Actions: - Click the Reset button");
		physicianAssignment.resetBtn().click();		
		driver.close();
	}

	@Test(priority = 1, description = "Client Processing - Physician Assignment - Delete Physician Assignment")
	@Parameters({"email", "password"})
	public void testRPM_625(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_625 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Remove Physician Assignment for the Client in DB");
		//Get the existing Client Abbrev and Name
		ArrayList<String> clnList = daoManagerClientWS.getExistClient(testDb);		
		String clnAbbrev = clnList.get(0);		
		daoManagerXifinRpm.deleteClnPhysAssignFromPHYSCLNByClnAbbrev(clnAbbrev, testDb);
		
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

		new XifinAdminUtils(driver, config).clearDataCache();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 5 Actions: - Navigate to Client Tab > Physician Assignment JSP");
		//switchToDefaultWinFromFrame();
		//String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Physician Assignment page		
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToPhysAssignment();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that it's on the Physician Assignment page");
		physicianAssignment = new PhysicianAssignment(driver);
		Assert.assertTrue(physicianAssignment.pageTitleText().trim().contains("Client - Physician Assignment"), "        Client - Physician Assignment page title should show.");
		
		logger.info("*** Step 6 Actions: - Load the Client in Client ID field and tab out");		
		physicianAssignment.setClnID(clnAbbrev);
		
		logger.info("*** Step 6 Actions: - Click Add Physician button");
		physicianAssignment.addPhysBtn().click();
		
		logger.info("*** Step 6 Actions: - Enter a Physcian's NPI");
		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		physicianAssignment.setNPI(npi);
		
		logger.info("*** Step 6 Actions: - Click Submit button");
		physicianAssignment.submitBtn().click();
		
		logger.info("*** Step 7 Actions: - Load the same Client in Client ID field and tab out");
		physicianAssignment.setClnID(clnAbbrev);	
		
		logger.info("*** Step 7 Expected Results: - Verify that the Physician's NPI was added properly");
		Assert.assertTrue(physicianAssignment.npiInput().getAttribute("value").trim().contains(npi), "        Physician's NPI " + npi + " should show.");
		
		logger.info("*** Step 8 Actions: - Check Delete checkbox for selected NPI");
		selectCheckBox(physicianAssignment.deleteCheckBox());
		
		logger.info("*** Step 8 Actions: - Click Submit button");
		physicianAssignment.submitBtn().click();
		
		logger.info("*** Step 8 Expected Results: - Verify that Physician's NPI for the Client was deleted in DB");
		Assert.assertTrue(daoManagerXifinRpm.getPhysSeqIdFromPHYSCLNByClnAbbrev(clnAbbrev, testDb).isEmpty(), "        The Physician's NPI " + npi + " should be deleted.");		
	
		driver.close();
	}
	
	@Test(priority = 1, description = "Client Processing - Physician Assignment - Physician Info display")
	@Parameters({"email", "password"})
	public void testRPM_627(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_627 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Remove Physician Assignment for the Client in DB");
		//Get the existing Client Abbrev and Name
		ArrayList<String> clnList = daoManagerClientWS.getExistClient(testDb);		
		String clnAbbrev = clnList.get(0);		
		daoManagerXifinRpm.deleteClnPhysAssignFromPHYSCLNByClnAbbrev(clnAbbrev, testDb);
		
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

		new XifinAdminUtils(driver, config).clearDataCache();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 5 Actions: - Navigate to Client Tab > Physician Assignment JSP");
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Physician Assignment page		
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToPhysAssignment();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that it's on the Physician Assignment page");
		physicianAssignment = new PhysicianAssignment(driver);
		Assert.assertTrue(physicianAssignment.pageTitleText().trim().contains("Client - Physician Assignment"), "        Client - Physician Assignment page title should show.");
		
		logger.info("*** Step 6 Actions: - Load the Client in Client ID field and tab out");		
		physicianAssignment.setClnID(clnAbbrev);
		
		logger.info("*** Step 6 Actions: - Click Add Physician button");
		physicianAssignment.addPhysBtn().click();
		
		logger.info("*** Step 6 Actions: - Enter a Physcian's NPI");
		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		physicianAssignment.setNPI(npi);
		
		logger.info("*** Step 6 Actions: - Click Submit button");
		physicianAssignment.submitBtn().click();
		
		logger.info("*** Step 7 Actions: - Load the same Client in Client ID field and tab out");
		physicianAssignment.setClnID(clnAbbrev);	
		
		logger.info("*** Step 7 Expected Results: - Verify that the Physician's NPI was added properly");
		Assert.assertTrue(physicianAssignment.npiInput().getAttribute("value").trim().contains(npi), "        Physician's NPI " + npi + " should show.");
		
		logger.info("*** Step 7 Expected Results: - Verify that the Physician's UPIN, Last Name, First Name, Zip, State, Specialty and Credentials  display properly");
		ArrayList<String> physInfoList = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(npi, testDb);
		String upin = physInfoList.get(13);
		String lName = physInfoList.get(1);
		String fName = physInfoList.get(2);		
		String zip = physicianAssignment.getZip(physInfoList.get(7));
		String state = physInfoList.get(6);
		String credentials = physInfoList.get(10);				
		String specialty = daoManagerXifinRpm.getPhysSpecialtyFromPHYSSPECTYPByPhysSpecId(physInfoList.get(11), testDb).trim();
		
		Assert.assertTrue(physicianAssignment.upinInput().getAttribute("value").trim().contains(upin), "        UPIN: " + upin + " should show.");
		Assert.assertTrue(physicianAssignment.lNameInput().getAttribute("value").trim().contains(lName), "        Last Name: " + lName + " should show.");
		Assert.assertTrue(physicianAssignment.fNameInput().getAttribute("value").trim().contains(fName), "        First Name: " + fName + " should show.");
		Assert.assertTrue(physicianAssignment.zipInput().getAttribute("value").trim().contains(zip), "        First 5 digits Zip: " + zip + " should show.");
		Assert.assertTrue(physicianAssignment.stateInput().getAttribute("value").trim().contains(state), "        State: " + state + " should show.");
		Assert.assertTrue(physicianAssignment.credentialsInput().getAttribute("value").trim().contains(credentials), "        Credentials: " + credentials + " should show.");
		Assert.assertTrue(physicianAssignment.specialtyInput().getAttribute("value").trim().equals(specialty), "        Specialty: " + specialty + " should show.");
				
		logger.info("*** Step 8 Actions: - Click the Reset button");
		physicianAssignment.resetBtn().click();		
		driver.close();
	}
	
	@Test(priority = 1, description = "Client Processing - Physician Assignment - Invalid Physician NPI Validation")
	@Parameters({"email", "password"})
	public void testRPM_630(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_630 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Remove Physician Assignment for the Client in DB");
		//Get the existing Client Abbrev and Name
		ArrayList<String> clnList = daoManagerClientWS.getExistClient(testDb);		
		String clnAbbrev = clnList.get(0);		
		daoManagerXifinRpm.deleteClnPhysAssignFromPHYSCLNByClnAbbrev(clnAbbrev, testDb);
		
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

		new XifinAdminUtils(driver, config).clearDataCache();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 5 Actions: - Navigate to Client Tab > Physician Assignment JSP");
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Physician Assignment page		
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToPhysAssignment();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that it's on the Physician Assignment page");
		physicianAssignment = new PhysicianAssignment(driver);
		Assert.assertTrue(physicianAssignment.pageTitleText().trim().contains("Client - Physician Assignment"), "        Client - Physician Assignment page title should show.");
		
		logger.info("*** Step 6 Actions: - Load the Client in Client ID field and tab out");		
		physicianAssignment.setClnID(clnAbbrev);
		
		logger.info("*** Step 6 Actions: - Click Add Physician button");
		physicianAssignment.addPhysBtn().click();
		
		logger.info("*** Step 6 Actions: - Enter an invalid Physcian's NPI and tab out");
		String invalidNPI = "12345";
		physicianAssignment.setNPI(invalidNPI);
		
		logger.info("*** Step 6 Expected Results: - Verify that an invalid Physician NPI error message shows");
		Assert.assertTrue(physicianAssignment.errMsgText().trim().contains("Invalid Physician NPI"), "        Invalid Physician NPI error message should show.");
				
		logger.info("*** Step 7 Actions: - Click the Reset button");
		physicianAssignment.resetBtn().click();		
		driver.close();
	}
	
	@Test(priority = 1, description = "Client Processing - Physician Assignment - Duplicate Physician NPI Validation")
	@Parameters({"email", "password"})
	public void testRPM_631(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_631 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Remove Physician Assignment for the Client in DB");
		//Get the existing Client Abbrev and Name
		ArrayList<String> clnList = daoManagerClientWS.getExistClient(testDb);		
		String clnAbbrev = clnList.get(0);		
		daoManagerXifinRpm.deleteClnPhysAssignFromPHYSCLNByClnAbbrev(clnAbbrev, testDb);
		
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

		new XifinAdminUtils(driver, config).clearDataCache();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 5 Actions: - Navigate to Client Tab > Physician Assignment JSP");
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Physician Assignment page		
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToPhysAssignment();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that it's on the Physician Assignment page");
		physicianAssignment = new PhysicianAssignment(driver);
		Assert.assertTrue(physicianAssignment.pageTitleText().trim().contains("Client - Physician Assignment"), "        Client - Physician Assignment page title should show.");
		
		logger.info("*** Step 6 Actions: - Load the Client in Client ID field and tab out");		
		physicianAssignment.setClnID(clnAbbrev);
		
		logger.info("*** Step 6 Actions: - Click Add Physician button");
		physicianAssignment.addPhysBtn().click();
		
		logger.info("*** Step 6 Actions: - Enter a Physcian's NPI");
		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		physicianAssignment.setNPI(npi);		
		
		logger.info("*** Step 6 Actions: - Click Submit button");
		physicianAssignment.submitBtn().click();
		
		logger.info("*** Step 7 Actions: - Load the same Client in Client ID field and tab out");
		physicianAssignment.setClnID(clnAbbrev);	
		
		logger.info("*** Step 7 Expected Results: - Verify that the Physician's NPI was added properly");
		Assert.assertTrue(physicianAssignment.npiInput().getAttribute("value").trim().contains(npi), "        Physician's NPI " + npi + " should show.");
			
		logger.info("*** Step 8 Actions: - Click Add Physician button");
		physicianAssignment.addPhysBtn().click();
		
		logger.info("*** Step 8 Actions: - Enter the same Physcian's NPI and tab out");		
		physicianAssignment.setNewNPI(npi);
		
		logger.info("*** Step 8 Expected Results: - Verify that Duplicate Physician ID error message shows");
		Assert.assertTrue(physicianAssignment.errMsgText().trim().contains("Duplicate Physician ID"), "        Duplicate Physician ID error message should show.");
						
		logger.info("*** Step 9 Actions: - Click the Reset button");
		physicianAssignment.resetBtn().click();		
		driver.close();
	}

	@Test(priority = 1, description = "Client Processing - Physician Assignment - Invalid Physician UPIN Validation")
	@Parameters({"email", "password"})
	public void testRPM_632(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_632 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Remove Physician Assignment for the Client in DB");
		//Get the existing Client Abbrev and Name
		ArrayList<String> clnList = daoManagerClientWS.getExistClient(testDb);		
		String clnAbbrev = clnList.get(0);		
		daoManagerXifinRpm.deleteClnPhysAssignFromPHYSCLNByClnAbbrev(clnAbbrev, testDb);
		
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

		new XifinAdminUtils(driver, config).clearDataCache();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 5 Actions: - Navigate to Client Tab > Physician Assignment JSP");
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Physician Assignment page		
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToPhysAssignment();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that it's on the Physician Assignment page");
		physicianAssignment = new PhysicianAssignment(driver);
		Assert.assertTrue(physicianAssignment.pageTitleText().trim().contains("Client - Physician Assignment"), "        Client - Physician Assignment page title should show.");
		
		logger.info("*** Step 6 Actions: - Load the Client in Client ID field and tab out");		
		physicianAssignment.setClnID(clnAbbrev);
		
		logger.info("*** Step 6 Actions: - Click Add Physician button");
		physicianAssignment.addPhysBtn().click();
		
		logger.info("*** Step 6 Actions: - Enter an invalid Physcian's UPIN and tab out");		
		physicianAssignment.setUPIN("12345");		
		
		logger.info("*** Step 6 Expected Results: - Verify that an error message shows");
		Assert.assertTrue(physicianAssignment.errMsgText().trim().contains("Physician not on file"), "        Physician not on file error message should show.");
						
		logger.info("*** Step 7 Actions: - Click the Reset button");
		physicianAssignment.resetBtn().click();		
		driver.close();
	}	
	
	@Test(priority = 1, description = "Client Processing - Physician Assignment - Invalid Client ID Validation")
	@Parameters({"email", "password"})
	public void testRPM_633(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_633 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
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
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Physician Assignment JSP");
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Physician Assignment page		
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToPhysAssignment();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Physician Assignment page");
		physicianAssignment = new PhysicianAssignment(driver);
		Assert.assertTrue(physicianAssignment.pageTitleText().trim().contains("Client - Physician Assignment"), "        Client - Physician Assignment page title should show.");
		
		logger.info("*** Step 3 Actions: - Load an invalid Client in Client ID field and tab out");		
		physicianAssignment.setClnID("ABC");

		logger.info("*** Step 3 Expected Results: - Verify that an error message shows");
		Assert.assertTrue(physicianAssignment.errMsgText().trim().contains("Client not on file"), "        Client not on file error message should show.");
						
		logger.info("*** Step 4 Actions: - Click the Reset button");
		physicianAssignment.resetBtn().click();		
		driver.close();
	}
	
	@Test(priority = 1, description = "Client Processing - Physician Assignment - Copy Physician Assignment to Client")
	@Parameters({"email", "password"})
	public void testRPM_626(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_626 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		logger.info("*** Step 2 Actions: - Remove the Physician Assignment for two Clients in DB");
		//Get random two different Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev1 = clnList.get(1);		
		daoManagerXifinRpm.deleteClnPhysAssignFromPHYSCLNByClnAbbrev(clnAbbrev1, testDb);
		
		clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);
		String clnAbbrev2 = clnList.get(1);		
		daoManagerXifinRpm.deleteClnPhysAssignFromPHYSCLNByClnAbbrev(clnAbbrev2, testDb);		
		
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

		new XifinAdminUtils(driver, config).clearDataCache();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
		
		logger.info("*** Step 5 Actions: - Navigate to Client Tab > Physician Assignment JSP");
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Physician Assignment page		
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToPhysAssignment();
		switchToPopupWin();
		
		logger.info("*** Step 5 Expected Results: - Verify that it's on the Physician Assignment page");
		physicianAssignment = new PhysicianAssignment(driver);
		Assert.assertTrue(physicianAssignment.pageTitleText().trim().contains("Client - Physician Assignment"), "        Client - Physician Assignment page title should show.");
		
		logger.info("*** Step 6 Actions: - Load the Client in Client ID field and tab out");		
		physicianAssignment.setClnID(clnAbbrev1);
		
		logger.info("*** Step 6 Actions: - Click Add Physician button");
		physicianAssignment.addPhysBtn().click();
		
		logger.info("*** Step 6 Actions: - Enter a Physcian's NPI");
		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		physicianAssignment.setNPI(npi);
		
		logger.info("*** Step 6 Actions: - Click Submit button");
		physicianAssignment.submitBtn().click();
		
		logger.info("*** Step 7 Actions: - Load the same Client in Client ID field and tab out");
		physicianAssignment.setClnID(clnAbbrev1);	
		
		logger.info("*** Step 7 Expected Results: - Verify that the Physician's NPI was added properly");
		Assert.assertTrue(physicianAssignment.npiInput().getAttribute("value").trim().contains(npi), "        Physician's NPI " + npi + " should show.");
		
		logger.info("*** Step 8 Actions: - Enter another Client ID in Copy physician assignments to Client ID Input field and tab out");
		physicianAssignment.setCopyClnId(clnAbbrev2);
		
		logger.info("*** Step 8 Actions: - Click Submit button");
		physicianAssignment.submitBtn().click();
		
		logger.info("*** Step 9 Actions: - Load the Copy To Client in Client ID field and tab out");
		physicianAssignment.setClnID(clnAbbrev2);	
		
		logger.info("*** Step 9 Expected Results: - Verify that the Physician's NPI was copied properly");
		Assert.assertTrue(physicianAssignment.npiInput().getAttribute("value").trim().contains(npi), "        Physician's NPI " + npi + " should show.");
						
		logger.info("*** Step 10 Actions: - Click the Reset button");
		physicianAssignment.resetBtn().click();		
		driver.close();
	}	
	
	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Client Name and Account Type display")
	@Parameters({"email", "password"})
	public void testRPM_662(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_662 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
		
		
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");
		//Get the Client Abbrev and name
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();	
		String clnAcctName = clnList.get(2).trim();	
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Client Name and Account Type were loaded properly");
		//Get the Client account type
		ArrayList<String> clnDescrList = daoManagerXifinRpm.getClnLtrTypAcctNameTypFromCLNByClnAbbrev(clnAbbrev, testDb);	
		String clnAcctTyp = clnDescrList.get(2).trim();		
			
		Assert.assertEquals(eligCensusConfig.nameInput().getAttribute("value").trim(), clnAcctName);	
		Assert.assertEquals(eligCensusConfig.acctTypInput().getAttribute("value").trim(), clnAcctTyp);
		
		logger.info("*** Step 4 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Help")
	@Parameters({"email", "password"})
	public void testRPM_663(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_663 *****");    	
		
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
		
		logger.info("*** Step 2 Actions: - Click Eligibility Census Configuration Link");			
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.eligCensusConfigLink();		
			
		logger.info("*** Step 3 Actions: - Click Help button on the Client Physician Assignment page");
		eligCensusConfig = new EligCensusConfig(driver,wait);
		eligCensusConfig.helpBtn();
		switchToPopupWin();		
		
		logger.info("*** Step 3 Expected Results: - Verify that Client Eligibility Census help page displays");		
		Assert.assertTrue(driver.getTitle().trim().contains("Client Eligibility Census"), "        Client Eligibility Census help should show.");
				
		driver.close();
	}
	
	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Reset Client")
	@Parameters({"email", "password"})
	public void testRPM_664(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_664 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
		
		
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");
		//Get the Client Abbrev and name
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();	
		String clnAcctName = clnList.get(2).trim();	
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Client Name and Account Type were loaded properly");
		//Get the Client account type
		ArrayList<String> clnDescrList = daoManagerXifinRpm.getClnLtrTypAcctNameTypFromCLNByClnAbbrev(clnAbbrev, testDb);	
		String clnAcctTyp = clnDescrList.get(2).trim();			
			
		Assert.assertEquals(eligCensusConfig.nameInput().getAttribute("value").trim(), clnAcctName);	
		Assert.assertEquals(eligCensusConfig.acctTypInput().getAttribute("value").trim(), clnAcctTyp);
		
		logger.info("*** Step 4 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that Client ID, Name and Account Type input fields were cleared");
		Assert.assertEquals(eligCensusConfig.clnIdInput().getAttribute("value").trim(), "");	
		Assert.assertEquals(eligCensusConfig.nameInput().getAttribute("value").trim(), "");	
		Assert.assertEquals(eligCensusConfig.acctTypInput().getAttribute("value").trim(), "");
				
		driver.close();	
	}	
	
	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Reset EPI")
	@Parameters({"email", "password"})
	public void testRPM_678(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_678 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
		
		
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");
		//Get the Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();		
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Actions: - Click Create New EPI button");
		eligCensusConfig.createNewEPIBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that it's on the Create New Patient popup window");
		Assert.assertTrue(eligCensusConfig.createNewPtPageTitleText().trim().contains("Create New Patient"), "        Create New Patient popup window should show.");
		
		logger.info("*** Step 5 Actions: - Enter a random Patient Last Name");
		randomCharacter = new RandomCharacter(driver);
		eligCensusConfig.setSysGenPtLName(randomCharacter.getRandomAlphaString(6));
		
		logger.info("*** Step 5 Actions: - Click Ok button in the Create New Patient popup window");
		eligCensusConfig.okBtn().click();
		
		eligCensusConfig.epiInput().sendKeys(Keys.TAB);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new EPI was generated");
		Assert.assertNotEquals(eligCensusConfig.epiInput().getAttribute("value").trim(), "");
		
		logger.info("*** Step 6 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		
		logger.info("*** Step 6 Expected Results: - Verify that the EPI Input field was cleared");
		Assert.assertEquals(eligCensusConfig.epiInput().getAttribute("value").trim(), "");
			
		driver.close();	
	}	

	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Create new Patient (with EPI only) Elig Roster")
	@Parameters({"email", "password"})
	public void testRPM_665(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_665 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");
		//Get the Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();		
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Actions: - Click Create New EPI button");
		eligCensusConfig.createNewEPIBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that it's on the Create New Patient popup window");
		Assert.assertTrue(eligCensusConfig.createNewPtPageTitleText().trim().contains("Create New Patient"), "        Create New Patient popup window should show.");
		
		logger.info("*** Step 5 Actions: - Enter a random Patient Last Name");
		randomCharacter = new RandomCharacter(driver);
		eligCensusConfig.setSysGenPtLName(randomCharacter.getRandomAlphaString(6));
		
		logger.info("*** Step 5 Actions: - Click Ok button in the Create New Patient popup window");
		eligCensusConfig.okBtn().click();
		
		eligCensusConfig.epiInput().sendKeys(Keys.TAB);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new EPI was generated");
		String epi = eligCensusConfig.epiInput().getAttribute("value").trim();
		Assert.assertNotEquals(epi, "");
		
		logger.info("*** Step 6 Actions: - Enter Eff Date and Comments in the Patient Update grid");
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		eligCensusConfig.setEffDate(currDt);		
		
		String comments = randomCharacter.getRandomAlphaString(8);
		eligCensusConfig.setComments(comments);
		
		logger.info("*** Step 7 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 7 Actions: - Reload the same EPI in the EPI Input field");
		eligCensusConfig.setEPI(epi);		
		
		logger.info("*** Step 7 Expected Results: - Verify that Eligibility Roster for the Patient was added");
		Assert.assertEquals(eligCensusConfig.effDateInput().getAttribute("value").trim(), currDt);		
		Assert.assertEquals(eligCensusConfig.commentsInput().getAttribute("value").trim(), comments);

		logger.info("*** Step 8 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		eligCensusConfig.resetBtn().click();
		
		driver.close();	
	}	
	
	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Create new Patient (Client&Fac IDs) Elig Roster")
	@Parameters({"email", "password"})
	public void testRPM_666(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_666 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");		
		//Get the Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();	
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Actions: - Click Create New EPI button");
		eligCensusConfig.createNewEPIBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that it's on the Create New Patient popup window");
		Assert.assertTrue(eligCensusConfig.createNewPtPageTitleText().trim().contains("Create New Patient"), "        Create New Patient popup window should show.");
		
		logger.info("*** Step 5 Actions: - Enter a random Patient Last Name");
		randomCharacter = new RandomCharacter(driver);
		eligCensusConfig.setSysGenPtLName(randomCharacter.getRandomAlphaString(6));
		
		logger.info("*** Step 5 Actions: - Click Ok button in the Create New Patient popup window");
		eligCensusConfig.okBtn().click();
		
		eligCensusConfig.epiInput().sendKeys(Keys.TAB);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new EPI was generated");
		String epi = eligCensusConfig.epiInput().getAttribute("value").trim();
		Assert.assertNotEquals(epi, "");
		
		logger.info("*** Step 6 Actions: - Enter Client Pt ID and Client's Primary Facility Pt ID");
		String clnPtId = randomCharacter.getRandomAlphaString(8);
		String clnFacPtId = randomCharacter.getRandomAlphaString(8);
		eligCensusConfig.setClnPtId(clnPtId);
		eligCensusConfig.setClnFacPtId(clnFacPtId);		
		
		logger.info("*** Step 7 Actions: - Enter Eff Date and Comments in the Patient Update grid");
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		eligCensusConfig.setEffDate(currDt);		
		
		String comments = randomCharacter.getRandomAlphaString(8);
		eligCensusConfig.setComments(comments);
		
		logger.info("*** Step 8 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 8 Actions: - Reload the same EPI in the EPI Input field");
		eligCensusConfig.setEPI(epi);	
		
		logger.info("*** Step 8 Expected Results: - Verify that Client Pt ID and the Client's Primary Facility Pt ID were added");
		Assert.assertEquals(eligCensusConfig.clnPtIdInput().getAttribute("value").trim(), clnPtId);
		Assert.assertEquals(eligCensusConfig.clnFacPtIdInput().getAttribute("value").trim(), clnFacPtId);		
		
		logger.info("*** Step 8 Expected Results: - Verify that Eligibility Roster for the Patient was added");
		Assert.assertEquals(eligCensusConfig.effDateInput().getAttribute("value").trim(), currDt);		
		Assert.assertEquals(eligCensusConfig.commentsInput().getAttribute("value").trim(), comments);

		logger.info("*** Step 9 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		eligCensusConfig.resetBtn().click();
		
		driver.close();	
	}

	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Existing Patient Eligibility Roster display")
	@Parameters({"email", "password"})
	public void testRPM_667(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_667 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");		
		//Get the Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();		
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Actions: - Click Create New EPI button");
		eligCensusConfig.createNewEPIBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that it's on the Create New Patient popup window");
		Assert.assertTrue(eligCensusConfig.createNewPtPageTitleText().trim().contains("Create New Patient"), "        Create New Patient popup window should show.");
		
		logger.info("*** Step 5 Actions: - Enter a random Patient Last Name");
		randomCharacter = new RandomCharacter(driver);
		eligCensusConfig.setSysGenPtLName(randomCharacter.getRandomAlphaString(6));
		
		logger.info("*** Step 5 Actions: - Click Ok button in the Create New Patient popup window");
		eligCensusConfig.okBtn().click();
		
		eligCensusConfig.epiInput().sendKeys(Keys.TAB);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new EPI was generated");
		String epi = eligCensusConfig.epiInput().getAttribute("value").trim();
		Assert.assertNotEquals(epi, "");	
		
		logger.info("*** Step 6 Actions: - Enter Eff/Exp/Rpt Dates and Comments in the Patient Update grid");
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		eligCensusConfig.setEffDate(currDt);	
		eligCensusConfig.setExpDate(currDt);
		eligCensusConfig.setRptDate(currDt);
		
		String comments = randomCharacter.getRandomAlphaString(8);
		eligCensusConfig.setComments(comments);
		
		logger.info("*** Step 6 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 7 Actions: - Reload the same EPI in the EPI Input field");
		eligCensusConfig.setEPI(epi);		
		
		logger.info("*** Step 7 Expected Results: - Verify that Eligibility Roster Info (Eff/Exp/Rpt Dates, Days and Comments) for the Patient was added and display properly");
		Assert.assertEquals(eligCensusConfig.effDateInput().getAttribute("value").trim(), currDt);	
		Assert.assertEquals(eligCensusConfig.expDateInput().getAttribute("value").trim(), currDt);
		Assert.assertEquals(eligCensusConfig.daysInput().getAttribute("value").trim(), "1");
		Assert.assertEquals(eligCensusConfig.rptDateInput().getAttribute("value").trim(), currDt);
		Assert.assertEquals(eligCensusConfig.commentsInput().getAttribute("value").trim(), comments);

		logger.info("*** Step 8 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		eligCensusConfig.resetBtn().click();
		
		driver.close();	
	}	
	
	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Existing Patient Info display")
	@Parameters({"email", "password"})
	public void testRPM_668(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_668 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");		
		//Get the Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();		
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Actions: - Click Create New EPI button");
		eligCensusConfig.createNewEPIBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that it's on the Create New Patient popup window");
		Assert.assertTrue(eligCensusConfig.createNewPtPageTitleText().trim().contains("Create New Patient"), "        Create New Patient popup window should show.");
		
		logger.info("*** Step 5 Actions: - Enter a random Patient Last Name");
		randomCharacter = new RandomCharacter(driver);
		String lName = randomCharacter.getRandomAlphaString(6);
		eligCensusConfig.setSysGenPtLName(lName);
		
		logger.info("*** Step 5 Actions: - Click Ok button in the Create New Patient popup window");
		eligCensusConfig.okBtn().click();
		
		eligCensusConfig.epiInput().sendKeys(Keys.TAB);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new EPI was generated");
		String epi = eligCensusConfig.epiInput().getAttribute("value").trim();
		Assert.assertNotEquals(epi, "");
		
		logger.info("*** Step 6 Actions: - Enter Patient Last/First Nm, DOB and SSN");
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		String fName = randomCharacter.getRandomAlphaString(7);
		String ssn = "123-45-6789";
		eligCensusConfig.setfName(fName);
		eligCensusConfig.setlName(lName);
		eligCensusConfig.setDOB(currDt);
		eligCensusConfig.setSSN(ssn);
		
		logger.info("*** Step 6 Actions: - Enter Client Pt ID and Client's Primary Facility Pt ID");
		String clnPtId = randomCharacter.getRandomAlphaString(8);
		String clnFacPtId = randomCharacter.getRandomAlphaString(8);
		eligCensusConfig.setClnPtId(clnPtId);
		eligCensusConfig.setClnFacPtId(clnFacPtId);		
		
		logger.info("*** Step 7 Actions: - Enter Eff Date in the Patient Update grid");
		eligCensusConfig.setEffDate(currDt);	
		
		logger.info("*** Step 8 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 9 Actions: - Reload the same EPI in the EPI Input field");
		eligCensusConfig.setEPI(epi);	
		
		logger.info("*** Step 9 Expected Results: - Verify that Patient Last/First Nm, DOB and SSN were added and display properly");
		Assert.assertEquals(eligCensusConfig.lNameInput().getAttribute("value").trim(), lName);
		Assert.assertEquals(eligCensusConfig.fNameInput().getAttribute("value").trim(), fName);
		Assert.assertEquals(eligCensusConfig.dobInput().getAttribute("value").trim(), currDt);	
		Assert.assertEquals(eligCensusConfig.ssnInput().getAttribute("value").trim(), ssn);
		
		logger.info("*** Step 9 Expected Results: - Verify that Client Pt ID and the Client's Primary Facility Pt ID were added and display properly");
		Assert.assertEquals(eligCensusConfig.clnPtIdInput().getAttribute("value").trim(), clnPtId);
		Assert.assertEquals(eligCensusConfig.clnFacPtIdInput().getAttribute("value").trim(), clnFacPtId);		
		
		logger.info("*** Step 9 Expected Results: - Verify that Eligibility Roster for the Patient was added");
		Assert.assertEquals(eligCensusConfig.effDateInput().getAttribute("value").trim(), currDt);				

		logger.info("*** Step 10 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		eligCensusConfig.resetBtn().click();
		
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Update existing Patient Eligibility Roster")
	@Parameters({"email", "password"})
	public void testRPM_669(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_669 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");		
		//Get the Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();		
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Actions: - Click Create New EPI button");
		eligCensusConfig.createNewEPIBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that it's on the Create New Patient popup window");
		Assert.assertTrue(eligCensusConfig.createNewPtPageTitleText().trim().contains("Create New Patient"), "        Create New Patient popup window should show.");
		
		logger.info("*** Step 5 Actions: - Enter a random Patient Last Name");
		randomCharacter = new RandomCharacter(driver);
		eligCensusConfig.setSysGenPtLName(randomCharacter.getRandomAlphaString(6));
		
		logger.info("*** Step 5 Actions: - Click Ok button in the Create New Patient popup window");
		eligCensusConfig.okBtn().click();
		
		eligCensusConfig.epiInput().sendKeys(Keys.TAB);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new EPI was generated");
		String epi = eligCensusConfig.epiInput().getAttribute("value").trim();
		Assert.assertNotEquals(epi, "");	
		
		logger.info("*** Step 6 Actions: - Enter Eff/Exp/Rpt Dates and Comments in the Patient Update grid");
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		String prevTwoDt = timeStamp.getPreviousDate("MM/dd/yyyy", 2);
		String prevOneDt = timeStamp.getPreviousDate("MM/dd/yyyy", 1);
		
		eligCensusConfig.setEffDate(prevTwoDt);	
		eligCensusConfig.setExpDate(prevOneDt);
		eligCensusConfig.setRptDate(currDt);
		
		String comments = randomCharacter.getRandomAlphaString(8);
		eligCensusConfig.setComments(comments);
		
		logger.info("*** Step 6 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 7 Actions: - Reload the same EPI in the EPI Input field");
		eligCensusConfig.setEPI(epi);		
		
		logger.info("*** Step 7 Expected Results: - Verify that Eligibility Roster Info (Eff/Exp/Rpt Dates, Days and Comments) for the Patient was added and display properly");
		Assert.assertEquals(eligCensusConfig.effDateInput().getAttribute("value").trim(), prevTwoDt);	
		Assert.assertEquals(eligCensusConfig.expDateInput().getAttribute("value").trim(), prevOneDt);
		Assert.assertEquals(eligCensusConfig.daysInput().getAttribute("value").trim(), "2");
		Assert.assertEquals(eligCensusConfig.rptDateInput().getAttribute("value").trim(), currDt);
		Assert.assertEquals(eligCensusConfig.commentsInput().getAttribute("value").trim(), comments);
		
		logger.info("*** Step 8 Actions: - Update Exp/Rpt Dates and Comments in the Patient Update grid");
		eligCensusConfig.setExpDate(currDt);
		eligCensusConfig.setRptDate(prevOneDt);
		
		comments = randomCharacter.getRandomAlphaString(10);
		eligCensusConfig.setComments(comments);
		
		logger.info("*** Step 8 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 9 Actions: - Reload the same EPI in the EPI Input field");
		eligCensusConfig.setEPI(epi);		
		
		logger.info("*** Step 9 Expected Results: - Verify that Eligibility Roster Info (Exp/Rpt Dates, Days and Comments) for the Patient was updated and display properly");
		
		Assert.assertEquals(eligCensusConfig.expDateInput().getAttribute("value").trim(), currDt);
		Assert.assertEquals(eligCensusConfig.daysInput().getAttribute("value").trim(), "3");
		Assert.assertEquals(eligCensusConfig.rptDateInput().getAttribute("value").trim(), prevOneDt);
		Assert.assertEquals(eligCensusConfig.commentsInput().getAttribute("value").trim(), comments);
		
		logger.info("*** Step 10 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		eligCensusConfig.resetBtn().click();
		
		driver.close();	
	}

	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Update existing Patient Info")
	@Parameters({"email", "password"})
	public void testRPM_670(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_670 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");		
		//Get the Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();		
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Actions: - Click Create New EPI button");
		eligCensusConfig.createNewEPIBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that it's on the Create New Patient popup window");
		Assert.assertTrue(eligCensusConfig.createNewPtPageTitleText().trim().contains("Create New Patient"), "        Create New Patient popup window should show.");
		
		logger.info("*** Step 5 Actions: - Enter a random Patient Last Name");
		randomCharacter = new RandomCharacter(driver);
		String lName = randomCharacter.getRandomAlphaString(6);
		eligCensusConfig.setSysGenPtLName(lName);
		
		logger.info("*** Step 5 Actions: - Click Ok button in the Create New Patient popup window");
		eligCensusConfig.okBtn().click();
		
		eligCensusConfig.epiInput().sendKeys(Keys.TAB);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new EPI was generated");
		String epi = eligCensusConfig.epiInput().getAttribute("value").trim();
		Assert.assertNotEquals(epi, "");
		
		logger.info("*** Step 6 Actions: - Enter Patient Last/First Nm, DOB and SSN");
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		String fName = randomCharacter.getRandomAlphaString(7);
		String ssn = "123-45-6789";
		eligCensusConfig.setfName(fName);
		eligCensusConfig.setlName(lName);
		eligCensusConfig.setDOB(currDt);
		eligCensusConfig.setSSN(ssn);
		
		logger.info("*** Step 6 Actions: - Enter Client Pt ID and Client's Primary Facility Pt ID");
		String clnPtId = randomCharacter.getRandomAlphaString(8);
		String clnFacPtId = randomCharacter.getRandomAlphaString(8);
		eligCensusConfig.setClnPtId(clnPtId);
		eligCensusConfig.setClnFacPtId(clnFacPtId);		
		
		logger.info("*** Step 7 Actions: - Enter Eff Date in the Patient Update grid");
		eligCensusConfig.setEffDate(currDt);	
		
		logger.info("*** Step 8 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 9 Actions: - Reload the same EPI in the EPI Input field");
		eligCensusConfig.setEPI(epi);	
		
		logger.info("*** Step 9 Expected Results: - Verify that Patient Last/First Nm, DOB and SSN were added and display properly");
		Assert.assertEquals(eligCensusConfig.lNameInput().getAttribute("value").trim(), lName);
		Assert.assertEquals(eligCensusConfig.fNameInput().getAttribute("value").trim(), fName);
		Assert.assertEquals(eligCensusConfig.dobInput().getAttribute("value").trim(), currDt);	
		Assert.assertEquals(eligCensusConfig.ssnInput().getAttribute("value").trim(), ssn);
		
		logger.info("*** Step 9 Expected Results: - Verify that Client Pt ID and the Client's Primary Facility Pt ID were added and display properly");
		Assert.assertEquals(eligCensusConfig.clnPtIdInput().getAttribute("value").trim(), clnPtId);
		Assert.assertEquals(eligCensusConfig.clnFacPtIdInput().getAttribute("value").trim(), clnFacPtId);		
		
		logger.info("*** Step 9 Expected Results: - Verify that Eligibility Roster for the Patient was added");
		Assert.assertEquals(eligCensusConfig.effDateInput().getAttribute("value").trim(), currDt);				

		logger.info("*** Step 10 Actions: - Update Patient Last/First Nm, DOB and SSN");		
		lName = randomCharacter.getRandomAlphaString(6);
		fName = randomCharacter.getRandomAlphaString(8);
		ssn = "623-54-8967";
		String prevTwoDt = timeStamp.getPreviousDate("MM/dd/yyyy", 2);
		eligCensusConfig.setfName(fName);
		eligCensusConfig.setlName(lName);
		eligCensusConfig.setDOB(prevTwoDt);
		eligCensusConfig.setSSN(ssn);
		
		logger.info("*** Step 10 Actions: - Update Client Pt ID and tab out");
		clnPtId = randomCharacter.getRandomAlphaString(10);
		clnFacPtId = randomCharacter.getRandomAlphaString(10);
		eligCensusConfig.setClnPtId(clnPtId);
		
		logger.info("*** Step 10 Expected Results: - Verify that Confirm Patient Permanent Record Update popup window shows");
		Assert.assertTrue(eligCensusConfig.ptRecordUpdatePopup().isDisplayed(), "        Confirm Patient Permanent Record Update popup window should show.");
		
		logger.info("*** Step 11 Actions: - Click Ok button in Confirm Patient Permanent Record Update popup window");
		eligCensusConfig.okBtnInPtUpdatePopup().click();		
		
		logger.info("*** Step 12 Actions: - Update Client Pt ID and Client's Primary Facility Pt ID");
		eligCensusConfig.setClnFacPtId(clnFacPtId);	
		
		logger.info("*** Step 12 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 13 Actions: - Reload the same EPI in the EPI Input field");
		eligCensusConfig.setEPI(epi);	
		
		logger.info("*** Step 13 Expected Results: - Verify that Patient Last/First Nm, DOB and SSN were updated and display properly");
		Assert.assertEquals(eligCensusConfig.lNameInput().getAttribute("value").trim(), lName);
		Assert.assertEquals(eligCensusConfig.fNameInput().getAttribute("value").trim(), fName);
		Assert.assertEquals(eligCensusConfig.dobInput().getAttribute("value").trim(), prevTwoDt);	
		Assert.assertEquals(eligCensusConfig.ssnInput().getAttribute("value").trim(), ssn);
		
		logger.info("*** Step 13 Expected Results: - Verify that Client Pt ID and the Client's Primary Facility Pt ID were updated and display properly");
		Assert.assertEquals(eligCensusConfig.clnPtIdInput().getAttribute("value").trim(), clnPtId);
		Assert.assertEquals(eligCensusConfig.clnFacPtIdInput().getAttribute("value").trim(), clnFacPtId);		
		
		logger.info("*** Step 14 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		eligCensusConfig.resetBtn().click();
		
		driver.close();	
	}	
	
	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Delete existing Patient Eligibility Roster")
	@Parameters({"email", "password"})
	public void testRPM_671(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_671 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");
		//Get the Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();		
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Actions: - Click Create New EPI button");
		eligCensusConfig.createNewEPIBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that it's on the Create New Patient popup window");
		Assert.assertTrue(eligCensusConfig.createNewPtPageTitleText().trim().contains("Create New Patient"), "        Create New Patient popup window should show.");
		
		logger.info("*** Step 5 Actions: - Enter a random Patient Last Name");
		randomCharacter = new RandomCharacter(driver);
		eligCensusConfig.setSysGenPtLName(randomCharacter.getRandomAlphaString(6));
		
		logger.info("*** Step 5 Actions: - Click Ok button in the Create New Patient popup window");
		eligCensusConfig.okBtn().click();
		
		eligCensusConfig.epiInput().sendKeys(Keys.TAB);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new EPI was generated");
		String epi = eligCensusConfig.epiInput().getAttribute("value").trim();
		Assert.assertNotEquals(epi, "");
		
		logger.info("*** Step 6 Actions: - Enter Eff Date and Comments in the Patient Update grid");
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		eligCensusConfig.setEffDate(currDt);		
		
		String comments = randomCharacter.getRandomAlphaString(8);
		eligCensusConfig.setComments(comments);
		
		logger.info("*** Step 7 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 7 Actions: - Reload the same EPI in the EPI Input field");
		eligCensusConfig.setEPI(epi);		
		
		logger.info("*** Step 7 Expected Results: - Verify that Eligibility Roster for the Patient was added");
		Assert.assertEquals(eligCensusConfig.effDateInput().getAttribute("value").trim(), currDt);		
		Assert.assertEquals(eligCensusConfig.commentsInput().getAttribute("value").trim(), comments);
		
		logger.info("*** Step 8 Actions: - Check the Delete checkbox for selected Patient Eligibility Roster");
		selectCheckBox(eligCensusConfig.deleteCheckBox());
		
		logger.info("*** Step 8 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 8 Expected Results: - Verify that Eligibility Roster for the Patient was deleted in DB");
		Assert.assertTrue(daoManagerXifinRpm.getEligRosterInfoFromELIGROSTERByClnAbbrevEPI(clnAbbrev, epi, testDb).isEmpty(), "        The Eligibility Roster for the Patient EPI " + epi + " should be deleted.");
				
		logger.info("*** Step 9 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		eligCensusConfig.resetBtn().click();
		
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Do not allow empty Eff Date")
	@Parameters({"email", "password"})
	public void testRPM_672(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_672 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");
		//Get the Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();		
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Actions: - Click Create New EPI button");
		eligCensusConfig.createNewEPIBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that it's on the Create New Patient popup window");
		Assert.assertTrue(eligCensusConfig.createNewPtPageTitleText().trim().contains("Create New Patient"), "        Create New Patient popup window should show.");
		
		logger.info("*** Step 5 Actions: - Enter a random Patient Last Name");
		randomCharacter = new RandomCharacter(driver);
		eligCensusConfig.setSysGenPtLName(randomCharacter.getRandomAlphaString(6));
		
		logger.info("*** Step 5 Actions: - Click Ok button in the Create New Patient popup window");
		eligCensusConfig.okBtn().click();
		
		eligCensusConfig.epiInput().sendKeys(Keys.TAB);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new EPI was generated");
		String epi = eligCensusConfig.epiInput().getAttribute("value").trim();
		Assert.assertNotEquals(epi, "");
		
		logger.info("*** Step 6 Actions: - Enter Comments in the Patient Update grid and do not enter Eff Date");		
		String comments = randomCharacter.getRandomAlphaString(8);
		eligCensusConfig.setComments(comments);
		
		logger.info("*** Step 7 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 7 Expected Results: - Verify that Required Effective Date error message should show");
		Assert.assertTrue(eligCensusConfig.errMsgText().trim().contains("Required Effective Date"), "        Required Effective Date error message should show.");
		
		logger.info("*** Step 8 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		eligCensusConfig.resetBtn().click();
		
		driver.close();	
	}	
	
	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Do not allow overlap Eff Date")
	@Parameters({"email", "password"})
	public void testRPM_673(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_673 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");
		//Get the Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();		
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Actions: - Click Create New EPI button");
		eligCensusConfig.createNewEPIBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that it's on the Create New Patient popup window");
		Assert.assertTrue(eligCensusConfig.createNewPtPageTitleText().trim().contains("Create New Patient"), "        Create New Patient popup window should show.");
		
		logger.info("*** Step 5 Actions: - Enter a random Patient Last Name");
		randomCharacter = new RandomCharacter(driver);
		eligCensusConfig.setSysGenPtLName(randomCharacter.getRandomAlphaString(6));
		
		logger.info("*** Step 5 Actions: - Click Ok button in the Create New Patient popup window");
		eligCensusConfig.okBtn().click();
		
		eligCensusConfig.epiInput().sendKeys(Keys.TAB);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new EPI was generated");
		String epi = eligCensusConfig.epiInput().getAttribute("value").trim();
		Assert.assertNotEquals(epi, "");
		
		logger.info("*** Step 6 Actions: - Enter Eff Date and Comments in the Patient Update grid");
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		eligCensusConfig.setEffDate(currDt);		
		
		String comments = randomCharacter.getRandomAlphaString(8);
		eligCensusConfig.setComments(comments);
		
		logger.info("*** Step 7 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 7 Actions: - Reload the same EPI in the EPI Input field");
		eligCensusConfig.setEPI(epi);		
		
		logger.info("*** Step 7 Expected Results: - Verify that Eligibility Roster for the Patient was added");
		Assert.assertEquals(eligCensusConfig.effDateInput().getAttribute("value").trim(), currDt);		
		Assert.assertEquals(eligCensusConfig.commentsInput().getAttribute("value").trim(), comments);
		
		logger.info("*** Step 8 Actions: - Click Add Row button");
		eligCensusConfig.addRowBtn().click();
		
		logger.info("*** Step 8 Actions: - Enter the same Eff Date for the new Eligibility Roster");
		eligCensusConfig.setNewEffDt(currDt);
		
		logger.info("*** Step 8 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 8 Expected Results: - Verify that overlaps with an existing pt record error message should show");
		Assert.assertTrue(eligCensusConfig.errMsgText().trim().contains("overlaps with an existing pt record"), "        overlaps with an existing pt record  error message should show.");
				
		logger.info("*** Step 9 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		eligCensusConfig.resetBtn().click();
		
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Do not allow future Eff Date")
	@Parameters({"email", "password"})
	public void testRPM_674(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_674 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");
		//Get the Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();		
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Actions: - Click Create New EPI button");
		eligCensusConfig.createNewEPIBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that it's on the Create New Patient popup window");
		Assert.assertTrue(eligCensusConfig.createNewPtPageTitleText().trim().contains("Create New Patient"), "        Create New Patient popup window should show.");
		
		logger.info("*** Step 5 Actions: - Enter a random Patient Last Name");
		randomCharacter = new RandomCharacter(driver);
		eligCensusConfig.setSysGenPtLName(randomCharacter.getRandomAlphaString(6));
		
		logger.info("*** Step 5 Actions: - Click Ok button in the Create New Patient popup window");
		eligCensusConfig.okBtn().click();
		
		eligCensusConfig.epiInput().sendKeys(Keys.TAB);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new EPI was generated");
		String epi = eligCensusConfig.epiInput().getAttribute("value").trim();
		Assert.assertNotEquals(epi, "");
		
		logger.info("*** Step 6 Actions: - Enter Future Eff Date in the Patient Update grid");
		eligCensusConfig.setEffDate("t+1");		
		
		logger.info("*** Step 7 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 7 Expected Results: - Verify that Future effective dates or dates older than 1800 are not allowed error message should show");
		Assert.assertTrue(eligCensusConfig.errMsgText().trim().contains("Future effective dates or dates older than 1800 are not allowed"), "        Future effective dates or dates older than 1800 are not allowed error message should show.");
		
		logger.info("*** Step 8 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		eligCensusConfig.resetBtn().click();
		
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Invalid EPI validation")
	@Parameters({"email", "password"})
	public void testRPM_675(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_675 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");
		//Get the Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();		
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Actions: - Enter a non-existing EPI and tab out");
		randomCharacter = new RandomCharacter(driver);	
		String epi = randomCharacter.getRandomAlphaString(6);
		eligCensusConfig.setEPI(epi);
		
		logger.info("*** Step 4 Expected Results: - Verify that a Patient ID not found error message shows");
		Assert.assertTrue(eligCensusConfig.ptIdErrMsgText().trim().contains("Patient ID \""+ epi + "\" was not found"), "        Patient ID \""+ epi + "\" was not found should show.");
		
		logger.info("*** Step 5 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		eligCensusConfig.resetBtn().click();
		
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Invalid DOB validation")
	@Parameters({"email", "password"})
	public void testRPM_676(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_676 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");
		//Get the Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();		
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Actions: - Click Create New EPI button");
		eligCensusConfig.createNewEPIBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that it's on the Create New Patient popup window");
		Assert.assertTrue(eligCensusConfig.createNewPtPageTitleText().trim().contains("Create New Patient"), "        Create New Patient popup window should show.");
		
		logger.info("*** Step 5 Actions: - Enter a random Patient Last Name");
		randomCharacter = new RandomCharacter(driver);
		eligCensusConfig.setSysGenPtLName(randomCharacter.getRandomAlphaString(6));
		
		logger.info("*** Step 5 Actions: - Click Ok button in the Create New Patient popup window");
		eligCensusConfig.okBtn().click();
		
		eligCensusConfig.epiInput().sendKeys(Keys.TAB);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new EPI was generated");
		String epi = eligCensusConfig.epiInput().getAttribute("value").trim();
		Assert.assertNotEquals(epi, "");
		
		logger.info("*** Step 6 Actions: - Enter invalid DOB and tab out");
		eligCensusConfig.setDOB("12345");
		
		logger.info("*** Step 6 Expected Results: - Verify that Invalid date format page error message shows");
		Assert.assertTrue(closeAlertAndGetItsText(true).trim().contains("Invalid date format"), "        Invalid date format error message should show.");		
		
		logger.info("*** Step 7 Actions: - Clear the DOB input field and click the Reset button");
		eligCensusConfig.dobInput().clear();
		eligCensusConfig.resetBtn().click();
		eligCensusConfig.resetBtn().click();
		
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Invalid SSN validation")
	@Parameters({"email", "password"})
	public void testRPM_677(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_677 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");
		//Get the Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();		
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Actions: - Enter an invalid SSN and tab out");
		String ssn = "ABC";
		eligCensusConfig.setSSN(ssn);
		
		logger.info("*** Step 4 Expected Results: - Verify that a SSN not valid error message displays");
		Assert.assertTrue(eligCensusConfig.ptSSNErrMsgText().trim().contains("SSN \"" + ssn + "\" is not valid"), "        SSN \"" + ssn + "\" is not valid error message should show.");
		
		logger.info("*** Step 5 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		eligCensusConfig.resetBtn().click();
		
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Patient Details")
	@Parameters({"email", "password"})
	public void testRPM_679(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_679 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");		
		//Get the Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();	
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Actions: - Click Create New EPI button");
		eligCensusConfig.createNewEPIBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that it's on the Create New Patient popup window");
		Assert.assertTrue(eligCensusConfig.createNewPtPageTitleText().trim().contains("Create New Patient"), "        Create New Patient popup window should show.");
		
		logger.info("*** Step 5 Actions: - Enter a random Patient Last Name");
		randomCharacter = new RandomCharacter(driver);
		eligCensusConfig.setSysGenPtLName(randomCharacter.getRandomAlphaString(6));
		
		logger.info("*** Step 5 Actions: - Click Ok button in the Create New Patient popup window");
		eligCensusConfig.okBtn().click();
		
		eligCensusConfig.epiInput().sendKeys(Keys.TAB);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new EPI was generated");
		String epi = eligCensusConfig.epiInput().getAttribute("value").trim();
		Assert.assertNotEquals(epi, "");
		
		logger.info("*** Step 6 Actions: - Enter Client Pt ID and Client's Primary Facility Pt ID");
		String clnPtId = randomCharacter.getRandomAlphaString(8);
		String clnFacPtId = randomCharacter.getRandomAlphaString(8);
		eligCensusConfig.setClnPtId(clnPtId);
		eligCensusConfig.setClnFacPtId(clnFacPtId);		
		
		logger.info("*** Step 7 Actions: - Enter Eff Date and Comments in the Patient Update grid");
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		eligCensusConfig.setEffDate(currDt);		
		
		String comments = randomCharacter.getRandomAlphaString(8);
		eligCensusConfig.setComments(comments);
		
		logger.info("*** Step 8 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 8 Actions: - Reload the same EPI in the EPI Input field");
		eligCensusConfig.setEPI(epi);	
		
		logger.info("*** Step 8 Expected Results: - Verify that Client Pt ID and the Client's Primary Facility Pt ID were added");
		Assert.assertEquals(eligCensusConfig.clnPtIdInput().getAttribute("value").trim(), clnPtId);
		Assert.assertEquals(eligCensusConfig.clnFacPtIdInput().getAttribute("value").trim(), clnFacPtId);		
		
		logger.info("*** Step 8 Expected Results: - Verify that Eligibility Roster for the Patient was added");
		Assert.assertEquals(eligCensusConfig.effDateInput().getAttribute("value").trim(), currDt);		
		Assert.assertEquals(eligCensusConfig.commentsInput().getAttribute("value").trim(), comments);
		
		logger.info("*** Step 9 Actions: - Click Patient Details button");
		eligCensusConfig.ptDetailsBtn().click();
		
		logger.info("*** Step 9 Expected Results: - Verify that Associated Patient ID pop up window displays");
		Assert.assertTrue(eligCensusConfig.asscPtIdPopup().isDisplayed(), "        Associated Patient ID pop up window should display.");
		
		logger.info("*** Step 9 Expected Results: - Verify that the Client and the Client Facility Patient IDs display");	
		Assert.assertTrue(selectColumnValue(clnPtId, eligCensusConfig.asscPtIdTable()), "        Client Pt ID: " + clnPtId + " should display.");		
		Assert.assertTrue(selectColumnValue(clnFacPtId, eligCensusConfig.asscPtIdTable()), "        Client Facility Pt ID: " + clnFacPtId + " should display.");
				
		logger.info("*** Step 10 Actions: - Close the Associated Patient ID pop up window");
		eligCensusConfig.closeBtn().click();

		logger.info("*** Step 11 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		eligCensusConfig.resetBtn().click();
		
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Duplicate Client Pt ID")
	@Parameters({"email", "password"})
	public void testRPM_680(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_680 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");		
		//Get the Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();	
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Actions: - Click Create New EPI button");
		eligCensusConfig.createNewEPIBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that it's on the Create New Patient popup window");
		Assert.assertTrue(eligCensusConfig.createNewPtPageTitleText().trim().contains("Create New Patient"), "        Create New Patient popup window should show.");
		
		logger.info("*** Step 5 Actions: - Enter a random Patient Last Name");
		randomCharacter = new RandomCharacter(driver);
		eligCensusConfig.setSysGenPtLName(randomCharacter.getRandomAlphaString(6));
		
		logger.info("*** Step 5 Actions: - Click Ok button in the Create New Patient popup window");
		eligCensusConfig.okBtn().click();
		
		eligCensusConfig.epiInput().sendKeys(Keys.TAB);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new EPI was generated");
		String epi = eligCensusConfig.epiInput().getAttribute("value").trim();
		Assert.assertNotEquals(epi, "");
		
		logger.info("*** Step 6 Actions: - Enter Client Pt ID and Client's Primary Facility Pt ID");
		String clnPtId = randomCharacter.getRandomAlphaString(8);
		String clnFacPtId = randomCharacter.getRandomAlphaString(8);
		eligCensusConfig.setClnPtId(clnPtId);
		eligCensusConfig.setClnFacPtId(clnFacPtId);		
		
		logger.info("*** Step 7 Actions: - Enter Eff Date and Comments in the Patient Update grid");
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		eligCensusConfig.setEffDate(currDt);		
		
		String comments = randomCharacter.getRandomAlphaString(8);
		eligCensusConfig.setComments(comments);
		
		logger.info("*** Step 8 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 8 Actions: - Reload the same EPI in the EPI Input field");
		eligCensusConfig.setEPI(epi);	
		
		logger.info("*** Step 8 Expected Results: - Verify that Client Pt ID and the Client's Primary Facility Pt ID were added");
		Assert.assertEquals(eligCensusConfig.clnPtIdInput().getAttribute("value").trim(), clnPtId);
		Assert.assertEquals(eligCensusConfig.clnFacPtIdInput().getAttribute("value").trim(), clnFacPtId);		
		
		logger.info("*** Step 8 Expected Results: - Verify that Eligibility Roster for the Patient was added");
		Assert.assertEquals(eligCensusConfig.effDateInput().getAttribute("value").trim(), currDt);		
		Assert.assertEquals(eligCensusConfig.commentsInput().getAttribute("value").trim(), comments);
		
		logger.info("*** Step 9 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		
		logger.info("*** Step 10 Actions: - Click Create New EPI button");
		eligCensusConfig.createNewEPIBtn().click();
		
		logger.info("*** Step 10 Expected Results: - Verify that it's on the Create New Patient popup window");
		Assert.assertTrue(eligCensusConfig.createNewPtPageTitleText().trim().contains("Create New Patient"), "        Create New Patient popup window should show.");
		
		logger.info("*** Step 11 Actions: - Enter a random Patient Last Name");
		randomCharacter = new RandomCharacter(driver);
		eligCensusConfig.setSysGenPtLName(randomCharacter.getRandomAlphaString(6));
		
		logger.info("*** Step 11 Actions: - Click Ok button in the Create New Patient popup window");
		eligCensusConfig.okBtn().click();		
		eligCensusConfig.epiInput().sendKeys(Keys.TAB);
		
		logger.info("*** Step 11 Expected Results: - Verify that a new EPI was generated");
		epi = eligCensusConfig.epiInput().getAttribute("value").trim();
		Assert.assertNotEquals(epi, "");
		
		logger.info("*** Step 12 Actions: - Enter the same Client Pt ID");
		eligCensusConfig.setClnPtId(clnPtId);
		
		logger.info("*** Step 12 Actions: - Enter Eff Date in the Patient Update grid");
		eligCensusConfig.setEffDate(currDt);
		
		logger.info("*** Step 12 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 12 Expected Results: - Verify that Duplicate Client Patient ID error message displays");
		Assert.assertTrue(eligCensusConfig.errMsgText().trim().contains("Duplicate Client ID/Specific Patient ID not allowed"), "        Duplicate Client ID/Specific Patient ID not allowed error message should show.");
		
		logger.info("*** Step 13 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		eligCensusConfig.resetBtn().click();
		
		driver.close();	
	}	
	
	@Test(priority = 1, description = "Client Processing - Eligibility Census Configuration - Duplicate Client Facility Pt ID")
	@Parameters({"email", "password"})
	public void testRPM_681(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_681 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Eligibility Census Configuration JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
				
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.navigateToEligCensusConfig();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Eligibility Census Configuration page");		
		eligCensusConfig = new EligCensusConfig(driver,wait);
		Assert.assertTrue(eligCensusConfig.pageTitleText().trim().contains("Client - Eligibility Census Configuration"), "        Client - Eligibility Census Configuration page title should show.");
				
		logger.info("*** Step 3 Actions: - Load the Client in Client ID field and tab out");		
		//Get the Client Abbrev
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);		
		String clnAbbrev = clnList.get(1).trim();	
		
		eligCensusConfig.setClnID(clnAbbrev);
		
		logger.info("*** Step 4 Actions: - Click Create New EPI button");
		eligCensusConfig.createNewEPIBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that it's on the Create New Patient popup window");
		Assert.assertTrue(eligCensusConfig.createNewPtPageTitleText().trim().contains("Create New Patient"), "        Create New Patient popup window should show.");
		
		logger.info("*** Step 5 Actions: - Enter a random Patient Last Name");
		randomCharacter = new RandomCharacter(driver);
		eligCensusConfig.setSysGenPtLName(randomCharacter.getRandomAlphaString(6));
		
		logger.info("*** Step 5 Actions: - Click Ok button in the Create New Patient popup window");
		eligCensusConfig.okBtn().click();
		
		eligCensusConfig.epiInput().sendKeys(Keys.TAB);
		
		logger.info("*** Step 5 Expected Results: - Verify that a new EPI was generated");
		String epi = eligCensusConfig.epiInput().getAttribute("value").trim();
		Assert.assertNotEquals(epi, "");
		
		logger.info("*** Step 6 Actions: - Enter Client Pt ID and Client's Primary Facility Pt ID");
		String clnPtId = randomCharacter.getRandomAlphaString(8);
		String clnFacPtId = randomCharacter.getRandomAlphaString(8);
		eligCensusConfig.setClnPtId(clnPtId);
		eligCensusConfig.setClnFacPtId(clnFacPtId);		
		
		logger.info("*** Step 7 Actions: - Enter Eff Date and Comments in the Patient Update grid");
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		eligCensusConfig.setEffDate(currDt);		
		
		String comments = randomCharacter.getRandomAlphaString(8);
		eligCensusConfig.setComments(comments);
		
		logger.info("*** Step 8 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 8 Actions: - Reload the same EPI in the EPI Input field");
		eligCensusConfig.setEPI(epi);	
		
		logger.info("*** Step 8 Expected Results: - Verify that Client Pt ID and the Client's Primary Facility Pt ID were added");
		Assert.assertEquals(eligCensusConfig.clnPtIdInput().getAttribute("value").trim(), clnPtId);
		Assert.assertEquals(eligCensusConfig.clnFacPtIdInput().getAttribute("value").trim(), clnFacPtId);		
		
		logger.info("*** Step 8 Expected Results: - Verify that Eligibility Roster for the Patient was added");
		Assert.assertEquals(eligCensusConfig.effDateInput().getAttribute("value").trim(), currDt);		
		Assert.assertEquals(eligCensusConfig.commentsInput().getAttribute("value").trim(), comments);
		
		logger.info("*** Step 9 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		
		logger.info("*** Step 10 Actions: - Click Create New EPI button");
		eligCensusConfig.createNewEPIBtn().click();
		
		logger.info("*** Step 10 Expected Results: - Verify that it's on the Create New Patient popup window");
		Assert.assertTrue(eligCensusConfig.createNewPtPageTitleText().trim().contains("Create New Patient"), "        Create New Patient popup window should show.");
		
		logger.info("*** Step 11 Actions: - Enter a random Patient Last Name");
		randomCharacter = new RandomCharacter(driver);
		eligCensusConfig.setSysGenPtLName(randomCharacter.getRandomAlphaString(6));
		
		logger.info("*** Step 11 Actions: - Click Ok button in the Create New Patient popup window");
		eligCensusConfig.okBtn().click();		
		eligCensusConfig.epiInput().sendKeys(Keys.TAB);
		
		logger.info("*** Step 11 Expected Results: - Verify that a new EPI was generated");
		epi = eligCensusConfig.epiInput().getAttribute("value").trim();
		Assert.assertNotEquals(epi, "");
		
		logger.info("*** Step 12 Actions: - Enter the same Client Facility Pt ID");
		eligCensusConfig.setClnFacPtId(clnFacPtId);	
		
		logger.info("*** Step 12 Actions: - Enter Eff Date in the Patient Update grid");
		eligCensusConfig.setEffDate(currDt);
		
		logger.info("*** Step 12 Actions: - Click the Submit button");
		eligCensusConfig.submitBtn().click();
		
		logger.info("*** Step 12 Expected Results: - Verify that Duplicate Facility Patient ID error message displays");
		Assert.assertTrue(eligCensusConfig.errMsgText().trim().contains("Duplicate Facility ID/Specific Patient ID not allowed"), "        Duplicate Facility ID/Specific Patient ID not allowed error message should show.");
		
		logger.info("*** Step 13 Actions: - Click the Reset button");
		eligCensusConfig.resetBtn().click();
		eligCensusConfig.resetBtn().click();
		
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Demographics - Create new Client")
	@Parameters({"email", "password"})
	public void testRPM_682(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_682 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 3 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		logger.info("*** Step 3 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 3 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 3 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);	
		
		logger.info("*** Step 3 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 4 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 4 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
		

		logger.info("*** Step 4 Expected Results: - Verify that Client Billing contact info display properly");
		Assert.assertTrue(clientDemographics.billingAddr1Input().getAttribute("value").trim().contains(bilAddr1), "        Billing Addr1: " + bilAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.billingZipInput().getAttribute("value").trim().contains(billZip), "        Billing Zip: " + billZip + " should show.");
		Assert.assertTrue(clientDemographics.billingPhone1Input().getAttribute("value").trim().contains(billPhone1), "        Billing Phone1: " + billPhone1 + " should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that Client Street contact info display properly");		
		Assert.assertTrue(clientDemographics.streetAddr1Input().getAttribute("value").trim().contains(streetAddr1), "        Street Addr1: " + streetAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.streetZipInput().getAttribute("value").trim().contains(streetZip), "        Street Zip: " + streetZip + " should show.");
		Assert.assertTrue(clientDemographics.streetPhone1Input().getAttribute("value").trim().contains(streetPhone1), "        Street Phone1: " + streetPhone1 + " should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that Client Correspondence contact info display properly");
		Assert.assertTrue(clientDemographics.correspAddr1Input().getAttribute("value").trim().contains(correspAddr1), "        Correspondence Addr1: " + correspAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.correspZipInput().getAttribute("value").trim().contains(correspZip), "        Correspondence Zip: " + correspZip + " should show.");
		Assert.assertTrue(clientDemographics.correspPhone1Input().getAttribute("value").trim().contains(correspPhone1), "        Correspondence Phone1: " + correspPhone1 + " should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that Client Shipping contact info display properly");
		Assert.assertTrue(clientDemographics.shippingAddr1Input().getAttribute("value").trim().contains(shippingAddr1), "        Shipping Addr1: " + shippingAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.shippingZipInput().getAttribute("value").trim().contains(shippingZip), "        Shipping Zip: " + shippingZip + " should show.");
		Assert.assertTrue(clientDemographics.shippingPhone1Input().getAttribute("value").trim().contains(shippingPhone1), "        Shipping Phone1: " + shippingPhone1 + " should show.");
				
		logger.info("*** Step 5 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Demographics - Update existing Client")
	@Parameters({"email", "password"})
	public void testRPM_683(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_683 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 3 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		logger.info("*** Step 3 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 3 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 3 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);	
		
		logger.info("*** Step 3 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 4 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 4 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that Client Billing contact info display properly");
		Assert.assertTrue(clientDemographics.billingAddr1Input().getAttribute("value").trim().contains(bilAddr1), "        Billing Addr1: " + bilAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.billingZipInput().getAttribute("value").trim().contains(billZip), "        Billing Zip: " + billZip + " should show.");
		Assert.assertTrue(clientDemographics.billingPhone1Input().getAttribute("value").trim().contains(billPhone1), "        Billing Phone1: " + billPhone1 + " should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that Client Street contact info display properly");		
		Assert.assertTrue(clientDemographics.streetAddr1Input().getAttribute("value").trim().contains(streetAddr1), "        Street Addr1: " + streetAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.streetZipInput().getAttribute("value").trim().contains(streetZip), "        Street Zip: " + streetZip + " should show.");
		Assert.assertTrue(clientDemographics.streetPhone1Input().getAttribute("value").trim().contains(streetPhone1), "        Street Phone1: " + streetPhone1 + " should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that Client Correspondence contact info display properly");
		Assert.assertTrue(clientDemographics.correspAddr1Input().getAttribute("value").trim().contains(correspAddr1), "        Correspondence Addr1: " + correspAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.correspZipInput().getAttribute("value").trim().contains(correspZip), "        Correspondence Zip: " + correspZip + " should show.");
		Assert.assertTrue(clientDemographics.correspPhone1Input().getAttribute("value").trim().contains(correspPhone1), "        Correspondence Phone1: " + correspPhone1 + " should show.");
		
		logger.info("*** Step 4 Expected Results: - Verify that Client Shipping contact info display properly");
		Assert.assertTrue(clientDemographics.shippingAddr1Input().getAttribute("value").trim().contains(shippingAddr1), "        Shipping Addr1: " + shippingAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.shippingZipInput().getAttribute("value").trim().contains(shippingZip), "        Shipping Zip: " + shippingZip + " should show.");
		Assert.assertTrue(clientDemographics.shippingPhone1Input().getAttribute("value").trim().contains(shippingPhone1), "        Shipping Phone1: " + shippingPhone1 + " should show.");
		
		logger.info("*** Step 5 Actions: - Update Client Name, Account Type, Primary Facility");
		String newClnName = randomCharacter.getRandomAlphaNumericString(7);	
		clientDemographics.setName(newClnName);
		
		ArrayList<String> clnAccntList = daoManagerXifinRpm.getClnAccntTypFromCLNACCNTTYPI(testDb);
		clnAccountTyp = clnAccntList.get(1);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);		
		
		ArrayList<String> facList = daoManagerXifinRpm.getFacInfoFromFAC(testDb);
		primaryFac = facList.get(11);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 5 Actions: - Update Billing Addr1, Zip and Phone1");
		bilAddr1 = randomCharacter.getRandomAlphaString(7);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		billPhone1 = "(858) 754-2154";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		logger.info("*** Step 5 Actions: - Update Street Addr1, Zip and Phone1");
		streetAddr1 = randomCharacter.getRandomAlphaString(7);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		streetZip = "92111";
		clientDemographics.setStreetZip(streetZip);		
		
		streetPhone1 = "(858) 987-5412";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 5 Actions: - Update Correspondence Addr1, Zip and Phone1");
		correspAddr1 = randomCharacter.getRandomAlphaString(7);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		correspZip = "92126";
		clientDemographics.setCorrespZip(correspZip);
		
		correspPhone1 = "(619) 436-9157";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 5 Actions: - Update Shipping Addr1, Zip and Phone1");
		shippingAddr1 = randomCharacter.getRandomAlphaString(7);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		shippingZip = "92123";
		clientDemographics.setShippingZip(shippingZip);
		
		shippingPhone1 = "(760) 735-9217";
		clientDemographics.setShippingPhone1(shippingPhone1);	
		
		logger.info("*** Step 5 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 6 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 6 Expected Results: - Verify that updated Client account info (Client Name, Account Type, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(newClnName), "        Updated Name: " + newClnName + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Updated Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Updated Primary Facility: " + primaryFac + " should be selected.");
		
		logger.info("*** Step 6 Expected Results: - Verify that updated Client Billing contact info display properly");
		Assert.assertTrue(clientDemographics.billingAddr1Input().getAttribute("value").trim().contains(bilAddr1), "        Updated Billing Addr1: " + bilAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.billingZipInput().getAttribute("value").trim().contains(billZip), "        Updated Billing Zip: " + billZip + " should show.");
		Assert.assertTrue(clientDemographics.billingPhone1Input().getAttribute("value").trim().contains(billPhone1), "        Updated Billing Phone1: " + billPhone1 + " should show.");
		
		logger.info("*** Step 6 Expected Results: - Verify that Updated Client Street contact info display properly");		
		Assert.assertTrue(clientDemographics.streetAddr1Input().getAttribute("value").trim().contains(streetAddr1), "        Updated Street Addr1: " + streetAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.streetZipInput().getAttribute("value").trim().contains(streetZip), "        Updated Street Zip: " + streetZip + " should show.");
		Assert.assertTrue(clientDemographics.streetPhone1Input().getAttribute("value").trim().contains(streetPhone1), "        Updated Street Phone1: " + streetPhone1 + " should show.");
		
		logger.info("*** Step 6 Expected Results: - Verify that Updated Client Correspondence contact info display properly");
		Assert.assertTrue(clientDemographics.correspAddr1Input().getAttribute("value").trim().contains(correspAddr1), "        Updated Correspondence Addr1: " + correspAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.correspZipInput().getAttribute("value").trim().contains(correspZip), "        Updated Correspondence Zip: " + correspZip + " should show.");
		Assert.assertTrue(clientDemographics.correspPhone1Input().getAttribute("value").trim().contains(correspPhone1), "        Updated Correspondence Phone1: " + correspPhone1 + " should show.");
		
		logger.info("*** Step 6 Expected Results: - Verify that Updated Client Shipping contact info display properly");
		Assert.assertTrue(clientDemographics.shippingAddr1Input().getAttribute("value").trim().contains(shippingAddr1), "        Updated Shipping Addr1: " + shippingAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.shippingZipInput().getAttribute("value").trim().contains(shippingZip), "        Updated Shipping Zip: " + shippingZip + " should show.");
		Assert.assertTrue(clientDemographics.shippingPhone1Input().getAttribute("value").trim().contains(shippingPhone1), "        Updated Shipping Phone1: " + shippingPhone1 + " should show.");
		
		logger.info("*** Step 7 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Demographics - Client account type info display")
	@Parameters({"email", "password"})
	public void testRPM_684(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_684 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 3 Actions: - Enter EAV, Annual Disclosure Letter, Tax ID, Client Facility NPI");
		String eav = "100";
		clientDemographics.setEAV(eav);
		
		parser = new Parser(driver);
		String month = parser.parseStringBy(currDt, "/", 0);	
		String letterMonth = clientDemographics.convertStringToMonth(month);
		selectItem(clientDemographics.adlDropDown(), letterMonth);
		
		String taxId = "123456789";
		clientDemographics.setTaxId(taxId);
		
		ArrayList<String> clnNPIList = daoManagerXifinRpm.getClnNPIFromCLN(testDb);
		String clnFacNPI = clnNPIList.get(0);
		clientDemographics.setClnFacNPI(clnFacNPI);		
		
		logger.info("*** Step 3 Actions: - Select Perform Billing Assignment, Do Not Require Order Entry checkboxes");
		selectCheckBox(clientDemographics.performBilAssgnCheckBox());
		selectCheckBox(clientDemographics.doNotReqOECheckBox());		
		
		logger.info("*** Step 3 Expected Results: - Verify that Perform Eligibility Census Checking checkbox diabled after Perform Billing Assignment checkbox was checked");
		Assert.assertFalse(clientDemographics.performEligCensusCheckBox().isEnabled(), "        Perform Eligibility Census Checking checkbox should be disabled.");
		
		logger.info("*** Step 4 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		logger.info("*** Step 4 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 4 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 4 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);	
		
		logger.info("*** Step 4 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 5 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 5 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
		
		logger.info("*** Step 5 Expected Results: - Verify that Client account info (EAV, Annual Disclosure Lette, Tax ID, Client Facility NPI, Perform Eligibility Census Checking) display properly");
		Assert.assertTrue(clientDemographics.eavInput().getAttribute("value").trim().contains(eav), "        EAV: " + eav + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.adlDropDown(), letterMonth), "        Annual Disclosure Letter: " + letterMonth + " should be selected.");
		Assert.assertTrue(clientDemographics.taxIdInput().getAttribute("value").trim().contains(taxId), "        Tax ID: " + taxId + " should show.");
		Assert.assertTrue(clientDemographics.clnFacNPIInput().getAttribute("value").trim().contains(clnFacNPI), "        Client Facility NPI: " + clnFacNPI + " should show.");		
		Assert.assertFalse(clientDemographics.performEligCensusCheckBox().isSelected(), "        Perform Eligibility Census Checking checkbox should not be selected.");
				
		logger.info("*** Step 6 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Demographics - Update Client account type info")
	@Parameters({"email", "password"})
	public void testRPM_685(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_685 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 3 Actions: - Enter EAV, Annual Disclosure Letter, Tax ID, Client Facility NPI");
		String eav = "100";
		clientDemographics.setEAV(eav);
		
		parser = new Parser(driver);
		String month = parser.parseStringBy(currDt, "/", 0);	
		String letterMonth = clientDemographics.convertStringToMonth(month);
		selectItem(clientDemographics.adlDropDown(), letterMonth);
		
		String taxId = "123456789";
		clientDemographics.setTaxId(taxId);
		
		ArrayList<String> clnNPIList = daoManagerXifinRpm.getClnNPIFromCLN(testDb);
		String clnFacNPI = clnNPIList.get(0);
		clientDemographics.setClnFacNPI(clnFacNPI);		
		
		logger.info("*** Step 3 Actions: - Select Perform Billing Assignment, Do Not Require Order Entry checkboxes");
		selectCheckBox(clientDemographics.performBilAssgnCheckBox());
		selectCheckBox(clientDemographics.doNotReqOECheckBox());		
		
		logger.info("*** Step 3 Expected Results: - Verify that Perform Eligibility Census Checking checkbox diabled after Perform Billing Assignment checkbox was checked");
		Assert.assertFalse(clientDemographics.performEligCensusCheckBox().isEnabled(), "        Perform Eligibility Census Checking checkbox should be disabled.");
		
		logger.info("*** Step 4 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		logger.info("*** Step 4 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 4 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 4 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);	
		
		logger.info("*** Step 4 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 5 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 5 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
		
		logger.info("*** Step 5 Expected Results: - Verify that Client account info (EAV, Annual Disclosure Lette, Tax ID, Client Facility NPI, Perform Eligibility Census Checking) display properly");
		Assert.assertTrue(clientDemographics.eavInput().getAttribute("value").trim().contains(eav), "        EAV: " + eav + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.adlDropDown(), letterMonth), "        Annual Disclosure Letter: " + letterMonth + " should be selected.");
		Assert.assertTrue(clientDemographics.taxIdInput().getAttribute("value").trim().contains(taxId), "        Tax ID: " + taxId + " should show.");
		Assert.assertTrue(clientDemographics.clnFacNPIInput().getAttribute("value").trim().contains(clnFacNPI), "        Client Facility NPI: " + clnFacNPI + " should show.");		
		Assert.assertFalse(clientDemographics.performEligCensusCheckBox().isSelected(), "        Perform Eligibility Census Checking checkbox should not be selected.");
				
		logger.info("*** Step 6 Actions: - Update Client Name, Account Type and Primary Facility");
		String clnName = randomCharacter.getRandomAlphaString(8);
		clientDemographics.setName(clnName);		
		
		ArrayList<String> clnAccntList = daoManagerXifinRpm.getClnAccntTypFromCLNACCNTTYPI(testDb);
		clnAccountTyp = clnAccntList.get(1);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);		

		ArrayList<String> facList = daoManagerXifinRpm.getFacInfoFromFAC(testDb);
		primaryFac = facList.get(11);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);		
		
		logger.info("*** Step 7 Actions: - Update EAV, Annual Disclosure Letter, Tax ID, Client Facility NPI");
		eav = "300";
		clientDemographics.setEAV(eav);		
		
		String newDt = timeStamp.getFutureDate("MM/dd/yyyy", 32);
		month = parser.parseStringBy(newDt, "/", 0);
		letterMonth = clientDemographics.convertStringToMonth(month);
		selectItem(clientDemographics.adlDropDown(), letterMonth);
		
		taxId = "987654321";
		clientDemographics.setTaxId(taxId);
		
		clnNPIList = daoManagerXifinRpm.getClnNPIFromCLN(testDb);
		clnFacNPI = clnNPIList.get(4);
		clientDemographics.setClnFacNPI(clnFacNPI);		
		
		logger.info("*** Step 7 Actions: - Un-select Perform Billing Assignment/Do Not Require Order Entry and select Perform Eligibility Census Checking checkboxes");
		selectCheckBox(clientDemographics.performBilAssgnCheckBox());
		selectCheckBox(clientDemographics.performEligCensusCheckBox());
		selectCheckBox(clientDemographics.doNotReqOECheckBox());		
		
		logger.info("*** Step 7 Expected Results: - Verify that Perform Billing Assignment checkbox diabled after Perform Eligibility Census Checking checkbox was checked");
		Assert.assertFalse(clientDemographics.performBilAssgnCheckBox().isEnabled(), "        Perform Billing Assignment checkbox should be disabled.");
	
		logger.info("*** Step 8 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 9 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 9 Expected Results: - Verify that updated Client account info (Client Name, Account Type, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnName), "        Updated Name: " + clnName + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Updated Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Updated Primary Facility: " + primaryFac + " should be selected.");
		
		logger.info("*** Step 9 Expected Results: - Verify that updated Client account info (EAV, Annual Disclosure Lette, Tax ID, Client Facility NPI, Perform Eligibility Census Checking, Do Not Require Order Entry) display properly");
		Assert.assertTrue(clientDemographics.eavInput().getAttribute("value").trim().contains(eav), "        Updated EAV: " + eav + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.adlDropDown(), letterMonth), "        Updated Annual Disclosure Letter: " + letterMonth + " should be selected.");
		Assert.assertTrue(clientDemographics.taxIdInput().getAttribute("value").trim().contains(taxId), "        Updated Tax ID: " + taxId + " should show.");
		Assert.assertTrue(clientDemographics.clnFacNPIInput().getAttribute("value").trim().contains(clnFacNPI), "        Updated Client Facility NPI: " + clnFacNPI + " should show.");		
		Assert.assertTrue(clientDemographics.performEligCensusCheckBox().isSelected(), "        Perform Eligibility Census Checking checkbox should be selected.");
		Assert.assertFalse(clientDemographics.doNotReqOECheckBox().isSelected(), "        Do Not Require Order Entry checkbox should not be selected.");
				
		logger.info("*** Step 10 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Demographics - Add and display Client Xref")
	@Parameters({"email", "password"})
	public void testRPM_686(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_686 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 4 Actions: - Click Add Xref button");
		clientDemographics.addXrefBtn().click();
		
		logger.info("*** Step 4 Actions: - Enter Eff Date, select Xref Type, Member Abbrev and Member Description");
		clientDemographics.setEffDate(currDt);
		clientDemographics.expDateInput().click();
		clientDemographics.expDateInput().sendKeys(Keys.TAB);
		
		ArrayList<String> xRefTypList = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat("Client", testDb);
		String xRefTyp = xRefTypList.get(3).trim();				
		clientDemographics.setXref(xRefTyp);
		
		ArrayList<String> xRefList = daoManagerXifinRpm.getXrefFromXREFByXrefTyp(xRefTyp, testDb);
		String xRefAbbrev = xRefList.get(4).trim();
		String xRefDescr = xRefList.get(2).trim();
		String xRef = xRefAbbrev + " - " + xRefDescr;
		clientDemographics.xRefInput().click();
		selectItem(clientDemographics.xRefMemberDropDown(), xRef);	
		clientDemographics.setXref(xRef);		
				
		logger.info("*** Step 5 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		logger.info("*** Step 5 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 5 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 5 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);	
		
		logger.info("*** Step 5 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 6 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 6 Expected Results: - Verify that Xref info (Eff Date, Xref Type, Member Abbrev and Member Description) display properly");
		Assert.assertTrue(clientDemographics.effDateInput().getAttribute("value").trim().contains(currDt), "        Eff Date: " + currDt + " should show.");
		Assert.assertTrue(clientDemographics.xRefTypeInput().getAttribute("value").trim().contains(xRefTyp), "        Xref Type: " + xRefTyp + " should show.");
		Assert.assertTrue(clientDemographics.xRefMemberAbbrevInput().getAttribute("value").trim().contains(xRefAbbrev), "        Xref Member Abbrev: " + xRefAbbrev + " should show.");
		Assert.assertTrue(clientDemographics.xRefMemberDescrInput().getAttribute("value").trim().contains(xRefDescr), "        Xref Member Description: " + xRefDescr + " should show.");
		
		logger.info("*** Step 7 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}	

	@Test(priority = 1, description = "Client Processing - Demographics - Update Client Xref Info")
	@Parameters({"email", "password"})
	public void testRPM_687(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_687 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 4 Actions: - Click Add Xref button");
		clientDemographics.addXrefBtn().click();
		
		logger.info("*** Step 4 Actions: - Enter Eff Date, select Xref Type, Member Abbrev and Member Description");
		clientDemographics.setEffDate(currDt);
		clientDemographics.expDateInput().click();
		clientDemographics.expDateInput().sendKeys(Keys.TAB);
		
		ArrayList<String> xRefTypList = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat("Client", testDb);
		String xRefTyp = xRefTypList.get(3).trim();				
		clientDemographics.setXref(xRefTyp);
		
		ArrayList<String> xRefList = daoManagerXifinRpm.getXrefFromXREFByXrefTyp(xRefTyp, testDb);
		String xRefAbbrev = xRefList.get(4).trim();
		String xRefDescr = xRefList.get(2).trim();
		String xRef = xRefAbbrev + " - " + xRefDescr;
		clientDemographics.xRefInput().click();
		selectItem(clientDemographics.xRefMemberDropDown(), xRef);	
		clientDemographics.setXref(xRef);		
				
		logger.info("*** Step 5 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		logger.info("*** Step 5 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 5 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 5 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);	
		
		logger.info("*** Step 5 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 6 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 6 Expected Results: - Verify that Xref info (Eff Date, Xref Type, Member Abbrev and Member Description) display properly");
		Assert.assertTrue(clientDemographics.effDateInput().getAttribute("value").trim().contains(currDt), "        Eff Date: " + currDt + " should show.");
		Assert.assertTrue(clientDemographics.xRefTypeInput().getAttribute("value").trim().contains(xRefTyp), "        Xref Type: " + xRefTyp + " should show.");
		Assert.assertTrue(clientDemographics.xRefMemberAbbrevInput().getAttribute("value").trim().contains(xRefAbbrev), "        Xref Member Abbrev: " + xRefAbbrev + " should show.");
		Assert.assertTrue(clientDemographics.xRefMemberDescrInput().getAttribute("value").trim().contains(xRefDescr), "        Xref Member Description: " + xRefDescr + " should show.");
		
		logger.info("*** Step 7 Actions: - Add Exp Date for selected Xref");
		clientDemographics.setExpDate(currDt);
		
		logger.info("*** Step 7 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 8 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 8 Expected Results: - Verify that Xref Exp Date display properly");
		Assert.assertTrue(clientDemographics.expDateInput().getAttribute("value").trim().contains(currDt), "        Exp Date: " + currDt + " should show.");
				
		logger.info("*** Step 9 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}	
	
	@Test(priority = 1, description = "Client Processing - Demographics - Delete Client Xref")
	@Parameters({"email", "password"})
	public void testRPM_688(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_688 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 4 Actions: - Click Add Xref button");
		clientDemographics.addXrefBtn().click();
		
		logger.info("*** Step 4 Actions: - Enter Eff Date, select Xref Type, Member Abbrev and Member Description");
		clientDemographics.setEffDate(currDt);
		clientDemographics.expDateInput().click();
		clientDemographics.expDateInput().sendKeys(Keys.TAB);
		
		ArrayList<String> xRefTypList = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat("Client", testDb);
		String xRefTyp = xRefTypList.get(3).trim();				
		clientDemographics.setXref(xRefTyp);
		
		ArrayList<String> xRefList = daoManagerXifinRpm.getXrefFromXREFByXrefTyp(xRefTyp, testDb);
		String xRefAbbrev = xRefList.get(4).trim();
		String xRefDescr = xRefList.get(2).trim();
		String xRef = xRefAbbrev + " - " + xRefDescr;
		clientDemographics.xRefInput().click();
		selectItem(clientDemographics.xRefMemberDropDown(), xRef);	
		clientDemographics.setXref(xRef);		
				
		logger.info("*** Step 5 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		logger.info("*** Step 5 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 5 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 5 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);	
		
		logger.info("*** Step 5 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 6 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 6 Expected Results: - Verify that Xref info (Eff Date, Xref Type, Member Abbrev and Member Description) display properly");
		Assert.assertTrue(clientDemographics.effDateInput().getAttribute("value").trim().contains(currDt), "        Eff Date: " + currDt + " should show.");
		Assert.assertTrue(clientDemographics.xRefTypeInput().getAttribute("value").trim().contains(xRefTyp), "        Xref Type: " + xRefTyp + " should show.");
		Assert.assertTrue(clientDemographics.xRefMemberAbbrevInput().getAttribute("value").trim().contains(xRefAbbrev), "        Xref Member Abbrev: " + xRefAbbrev + " should show.");
		Assert.assertTrue(clientDemographics.xRefMemberDescrInput().getAttribute("value").trim().contains(xRefDescr), "        Xref Member Description: " + xRefDescr + " should show.");
		
		logger.info("*** Step 7 Actions: - Check Delete checkbox for selected Xref");
		clientDemographics.deleteCheckBox().click();
		
		logger.info("*** Step 7 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 7 Expected Results: - Verify that Client Xref was deleted in DB");
		ArrayList<String> clnXrefList = daoManagerXifinRpm.getClnXrefInfoFromCLNXREFByClnAbbrev(clnAbbrev, testDb);
		Assert.assertTrue(clnXrefList.isEmpty(), "        Client Xref " + xRefTyp + " should be deleted.");

		logger.info("*** Step 8 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}	
	
	@Test(priority = 1, description = "Client Processing - Demographics - Client Billing Info diaplay")
	@Parameters({"email", "password"})
	public void testRPM_689(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_689 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 3 Actions: - Enter Billing Address Information (Contact 1, Preferred Contact Method, Addr1, Addr2, Zip, Phone1, Fax1 and Email1)");
		String bilContact1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingContact1(bilContact1);
		
		selectItem(clientDemographics.billingContctMethodDropDown(), "E-Mail");		
		
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String bilAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr2(bilAddr2);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		String billFax1 = "(858) 457-4545";
		clientDemographics.setBillingFax1(billFax1);
		
		String billEmail1 = randomCharacter.getRandomAlphaString(6) + "@xifin.com";
		clientDemographics.setBillingEmail1(billEmail1);
		
		logger.info("*** Step 3 Actions: - Enter Billing Address Information (Contact 2, Phone2, Fax2 and Email2)");
		String bilContact2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingContact2(bilContact2);
		
		String billPhone2 = "(619) 457-4512";
		clientDemographics.setBillingPhone2(billPhone2);			
		
		String billFax2 = "(619) 457-4545";
		clientDemographics.setBillingFax2(billFax2);
		
		String billEmail2 = randomCharacter.getRandomAlphaString(6) + "@gmail.com";
		clientDemographics.setBillingEmail2(billEmail2);		
		
		logger.info("*** Step 4 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 4 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 4 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);	
		
		logger.info("*** Step 5 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 6 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 6 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
		
		logger.info("*** Step 6 Expected Results: - Verify that Client Billing Address Information (Contact 1, Preferred Contact Method, Addr1, Addr2, Zip, City, State, Phone1, Fax1 and Email1) display properly");
		Assert.assertTrue(clientDemographics.billingContact1Input().getAttribute("value").trim().contains(bilContact1), "        Billing Contact1: " + bilContact1 + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.billingContctMethodDropDown(), "E-Mail"), "        Billing Contact Method: E-Mail should be selected.");		
		Assert.assertTrue(clientDemographics.billingAddr1Input().getAttribute("value").trim().contains(bilAddr1), "        Billing Addr1: " + bilAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.billingAddr2Input().getAttribute("value").trim().contains(bilAddr2), "        Billing Addr2: " + bilAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.billingZipInput().getAttribute("value").trim().contains(billZip), "        Billing Zip: " + billZip + " should show.");
		Assert.assertTrue(clientDemographics.billingCityInput().getAttribute("value").trim().contains("SAN DIEGO"), "        Billing City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.billingStateDropDown(), "CA"), "        Billing State: CA should be selected.");		
		Assert.assertTrue(clientDemographics.billingPhone1Input().getAttribute("value").trim().contains(billPhone1), "        Billing Phone1: " + billPhone1 + " should show.");
		Assert.assertTrue(clientDemographics.billingFax1Input().getAttribute("value").trim().contains(billFax1), "        Billing Fax1: " + billFax1 + " should show.");
		Assert.assertTrue(clientDemographics.billingEmail1Input().getAttribute("value").trim().contains(billEmail1), "        Billing Email1: " + billEmail1 + " should show.");
		
		logger.info("*** Step 6 Expected Results: - Verify that Client Billing Address Information (Contact 2, Phone2, Fax2 and Email2) display properly");
		Assert.assertTrue(clientDemographics.billingContact2Input().getAttribute("value").trim().contains(bilContact2), "        Billing Contact2: " + bilContact2 + " should show.");
		Assert.assertTrue(clientDemographics.billingPhone2Input().getAttribute("value").trim().contains(billPhone2), "        Billing Phone2: " + billPhone2 + " should show.");
		Assert.assertTrue(clientDemographics.billingFax2Input().getAttribute("value").trim().contains(billFax2), "        Billing Fax2: " + billFax2 + " should show.");
		Assert.assertTrue(clientDemographics.billingEmail2Input().getAttribute("value").trim().contains(billEmail2), "        Billing Email1: " + billEmail2 + " should show.");

		logger.info("*** Step 7 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}

	@Test(priority = 1, description = "Client Processing - Demographics - Update Client Billing Address and Contact Info")
	@Parameters({"email", "password"})
	public void testRPM_690(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_690 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 3 Actions: - Enter Billing Address Information (Contact 1, Preferred Contact Method, Addr1, Addr2, Zip, Phone1, Fax1 and Email1)");
		String bilContact1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingContact1(bilContact1);
		
		selectItem(clientDemographics.billingContctMethodDropDown(), "E-Mail");		
		
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String bilAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr2(bilAddr2);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		String billFax1 = "(858) 457-4545";
		clientDemographics.setBillingFax1(billFax1);
		
		String billEmail1 = randomCharacter.getRandomAlphaString(6) + "@xifin.com";
		clientDemographics.setBillingEmail1(billEmail1);
		
		logger.info("*** Step 3 Actions: - Enter Billing Address Information (Contact 2, Phone2, Fax2 and Email2)");
		String bilContact2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingContact2(bilContact2);
		
		String billPhone2 = "(619) 457-4512";
		clientDemographics.setBillingPhone2(billPhone2);			
		
		String billFax2 = "(619) 457-4545";
		clientDemographics.setBillingFax2(billFax2);
		
		String billEmail2 = randomCharacter.getRandomAlphaString(6) + "@gmail.com";
		clientDemographics.setBillingEmail2(billEmail2);		
		
		logger.info("*** Step 4 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 4 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 4 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);	
		
		logger.info("*** Step 5 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 6 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 6 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
		
		logger.info("*** Step 6 Expected Results: - Verify that Client Billing Address Information (Contact 1, Preferred Contact Method, Addr1, Addr2, Zip, City, State, Phone1, Fax1 and Email1) display properly");
		Assert.assertTrue(clientDemographics.billingContact1Input().getAttribute("value").trim().contains(bilContact1), "        Billing Contact1: " + bilContact1 + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.billingContctMethodDropDown(), "E-Mail"), "        Billing Contact Method: E-Mail should be selected.");		
		Assert.assertTrue(clientDemographics.billingAddr1Input().getAttribute("value").trim().contains(bilAddr1), "        Billing Addr1: " + bilAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.billingAddr2Input().getAttribute("value").trim().contains(bilAddr2), "        Billing Addr2: " + bilAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.billingZipInput().getAttribute("value").trim().contains(billZip), "        Billing Zip: " + billZip + " should show.");
		Assert.assertTrue(clientDemographics.billingCityInput().getAttribute("value").trim().contains("SAN DIEGO"), "        Billing City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.billingStateDropDown(), "CA"), "        Billing State: CA should be selected.");		
		Assert.assertTrue(clientDemographics.billingPhone1Input().getAttribute("value").trim().contains(billPhone1), "        Billing Phone1: " + billPhone1 + " should show.");
		Assert.assertTrue(clientDemographics.billingFax1Input().getAttribute("value").trim().contains(billFax1), "        Billing Fax1: " + billFax1 + " should show.");
		Assert.assertTrue(clientDemographics.billingEmail1Input().getAttribute("value").trim().contains(billEmail1), "        Billing Email1: " + billEmail1 + " should show.");
		
		logger.info("*** Step 6 Expected Results: - Verify that Client Billing Address Information (Contact 2, Phone2, Fax2 and Email2) display properly");
		Assert.assertTrue(clientDemographics.billingContact2Input().getAttribute("value").trim().contains(bilContact2), "        Billing Contact2: " + bilContact2 + " should show.");
		Assert.assertTrue(clientDemographics.billingPhone2Input().getAttribute("value").trim().contains(billPhone2), "        Billing Phone2: " + billPhone2 + " should show.");
		Assert.assertTrue(clientDemographics.billingFax2Input().getAttribute("value").trim().contains(billFax2), "        Billing Fax2: " + billFax2 + " should show.");
		Assert.assertTrue(clientDemographics.billingEmail2Input().getAttribute("value").trim().contains(billEmail2), "        Billing Email1: " + billEmail2 + " should show.");

		logger.info("*** Step 7 Actions: - Update Client Billing Address Information (Contact 1, Preferred Contact Method, Addr1, Addr2, Zip, City, State, Phone1, Fax1 and Email1)");
		bilContact1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingContact1(bilContact1);
		
		selectItem(clientDemographics.billingContctMethodDropDown(), "Fax");		
		
		bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		bilAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr2(bilAddr2);
		
		billZip = "92127";
		clientDemographics.setBillingZip(billZip);
		
		billPhone1 = "(760) 754-2154";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		billFax1 = "(619) 754-5454";
		clientDemographics.setBillingFax1(billFax1);
		
		billEmail1 = randomCharacter.getRandomAlphaString(6) + "@gmail.com";
		clientDemographics.setBillingEmail1(billEmail1);
		
		logger.info("*** Step 7 Actions: - Update Client Billing Address Information (Contact 2, Phone2, Fax2 and Email2)");
		bilContact2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingContact2(bilContact2);
		
		billPhone2 = "(212) 754-2154";
		clientDemographics.setBillingPhone2(billPhone2);			
		
		billFax2 = "(212) 754-5454";
		clientDemographics.setBillingFax2(billFax2);
		
		billEmail2 = randomCharacter.getRandomAlphaString(6) + "@yahoo.com";
		clientDemographics.setBillingEmail2(billEmail2);
		
		logger.info("*** Step 8 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 9 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 9 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
		
		logger.info("*** Step 9 Expected Results: - Verify that updated Client Billing Address Information (Contact 1, Preferred Contact Method, Addr1, Addr2, Zip, City, State, Phone1, Fax1 and Email1) display properly");
		Assert.assertTrue(clientDemographics.billingContact1Input().getAttribute("value").trim().contains(bilContact1), "        Updated Billing Contact1: " + bilContact1 + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.billingContctMethodDropDown(), "Fax"), "        Updated Billing Contact Method: Fax should be selected.");		
		Assert.assertTrue(clientDemographics.billingAddr1Input().getAttribute("value").trim().contains(bilAddr1), "        Updated Billing Addr1: " + bilAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.billingAddr2Input().getAttribute("value").trim().contains(bilAddr2), "        Updated Billing Addr2: " + bilAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.billingZipInput().getAttribute("value").trim().contains(billZip), "        Updated Billing Zip: " + billZip + " should show.");
		Assert.assertTrue(clientDemographics.billingCityInput().getAttribute("value").trim().contains("SAN DIEGO"), "        Updated Billing City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.billingStateDropDown(), "CA"), "        Updated Billing State: CA should be selected.");		
		Assert.assertTrue(clientDemographics.billingPhone1Input().getAttribute("value").trim().contains(billPhone1), "        Updated Billing Phone1: " + billPhone1 + " should show.");
		Assert.assertTrue(clientDemographics.billingFax1Input().getAttribute("value").trim().contains(billFax1), "        Updated Billing Fax1: " + billFax1 + " should show.");
		Assert.assertTrue(clientDemographics.billingEmail1Input().getAttribute("value").trim().contains(billEmail1), "        Updated Billing Email1: " + billEmail1 + " should show.");
		
		logger.info("*** Step 9 Expected Results: - Verify that updated Client Billing Address Information (Contact 2, Phone2, Fax2 and Email2) display properly");
		Assert.assertTrue(clientDemographics.billingContact2Input().getAttribute("value").trim().contains(bilContact2), "        Updated Billing Contact2: " + bilContact2 + " should show.");
		Assert.assertTrue(clientDemographics.billingPhone2Input().getAttribute("value").trim().contains(billPhone2), "        Updated Billing Phone2: " + billPhone2 + " should show.");
		Assert.assertTrue(clientDemographics.billingFax2Input().getAttribute("value").trim().contains(billFax2), "        Updated Billing Fax2: " + billFax2 + " should show.");
		Assert.assertTrue(clientDemographics.billingEmail2Input().getAttribute("value").trim().contains(billEmail2), "        Updated Billing Email1: " + billEmail2 + " should show.");
				
		logger.info("*** Step 10 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}	
	
	@Test(priority = 1, description = "Client Processing - Demographics - Client Street Info diaplay")
	@Parameters({"email", "password"})
	public void testRPM_691(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_691 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 4 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);
				
		logger.info("*** Step 5 Actions: - Enter Street Address Information (Contact 1, Preferred Contact Method, Addr1, Addr2, Zip, Phone1, Fax1 and Email1)");
		String streetContact1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetContact1(streetContact1);
		
		selectItem(clientDemographics.streetContctMethodDropDown(), "E-Mail");		
		
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);
		
		String streetAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr2(streetAddr2);
		
		String streetZip = "92127";
		clientDemographics.setStreetZip(streetZip);
		
		String streetPhone1 = "(716) 457-4512";
		clientDemographics.setStreetPhone1(streetPhone1);			
		
		String streetFax1 = "(716) 457-4545";
		clientDemographics.setStreetFax1(streetFax1);
		
		String streetEmail1 = randomCharacter.getRandomAlphaString(6) + "@yahoo.com";
		clientDemographics.setStreetEmail1(streetEmail1);
		
		logger.info("*** Step 6 Actions: - Enter Street Address Information (Contact 2, Phone2, Fax2 and Email2)");
		String streetContact2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetContact2(streetContact2);
		
		String streetPhone2 = "(619) 457-4512";
		clientDemographics.setStreetPhone2(streetPhone2);			
		
		String streetFax2 = "(619) 457-4545";
		clientDemographics.setStreetFax2(streetFax2);
		
		String streetEmail2 = randomCharacter.getRandomAlphaString(6) + "@san.rr.com";
		clientDemographics.setStreetEmail2(streetEmail2);		
				
		logger.info("*** Step 7 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 8 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);	
		
		logger.info("*** Step 9 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 10 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 10 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
				
		logger.info("*** Step 10 Expected Results: - Verify that Client Street Address Information (Contact 1, Preferred Contact Method, Addr1, Addr2, Zip, City, State, Phone1, Fax1 and Email1) display properly");
		Assert.assertTrue(clientDemographics.streetContact1Input().getAttribute("value").trim().contains(streetContact1), "        Street Contact1: " + streetContact1 + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.streetContctMethodDropDown(), "E-Mail"), "        Street Contact Method: E-Mail should be selected.");		
		Assert.assertTrue(clientDemographics.streetAddr1Input().getAttribute("value").trim().contains(streetAddr1), "        Street Addr1: " + streetAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.streetAddr2Input().getAttribute("value").trim().contains(streetAddr2), "        Street Addr2: " + streetAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.streetZipInput().getAttribute("value").trim().contains(streetZip), "        Street Zip: " + streetZip + " should show.");
		Assert.assertTrue(clientDemographics.streetCityInput().getAttribute("value").trim().contains("SAN DIEGO"), "        Street City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.streetStateDropDown(), "CA"), "        Street State: CA should be selected.");		
		Assert.assertTrue(clientDemographics.streetPhone1Input().getAttribute("value").trim().contains(streetPhone1), "        Street Phone1: " + streetPhone1 + " should show.");
		Assert.assertTrue(clientDemographics.streetFax1Input().getAttribute("value").trim().contains(streetFax1), "        Street Fax1: " + streetFax1 + " should show.");
		Assert.assertTrue(clientDemographics.streetEmail1Input().getAttribute("value").trim().contains(streetEmail1), "        Street Email1: " + streetEmail1 + " should show.");
		
		logger.info("*** Step 10 Expected Results: - Verify that Client Street Address Information (Contact 2, Phone2, Fax2 and Email2) display properly");
		Assert.assertTrue(clientDemographics.streetContact2Input().getAttribute("value").trim().contains(streetContact2), "        Street Contact2: " + streetContact2 + " should show.");
		Assert.assertTrue(clientDemographics.streetPhone2Input().getAttribute("value").trim().contains(streetPhone2), "        Street Phone2: " + streetPhone2 + " should show.");
		Assert.assertTrue(clientDemographics.streetFax2Input().getAttribute("value").trim().contains(streetFax2), "        Street Fax2: " + streetFax2 + " should show.");
		Assert.assertTrue(clientDemographics.streetEmail2Input().getAttribute("value").trim().contains(streetEmail2), "        Street Email1: " + streetEmail2 + " should show.");

		logger.info("*** Step 11 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}

	@Test(priority = 1, description = "Client Processing - Demographics - Update Client Street Info")
	@Parameters({"email", "password"})
	public void testRPM_692(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_692 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 4 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);
				
		logger.info("*** Step 5 Actions: - Enter Street Address Information (Contact 1, Preferred Contact Method, Addr1, Addr2, Zip, Phone1, Fax1 and Email1)");
		String streetContact1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetContact1(streetContact1);
		
		selectItem(clientDemographics.streetContctMethodDropDown(), "E-Mail");		
		
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);
		
		String streetAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr2(streetAddr2);
		
		String streetZip = "92127";
		clientDemographics.setStreetZip(streetZip);
		
		String streetPhone1 = "(716) 457-4512";
		clientDemographics.setStreetPhone1(streetPhone1);			
		
		String streetFax1 = "(716) 457-4545";
		clientDemographics.setStreetFax1(streetFax1);
		
		String streetEmail1 = randomCharacter.getRandomAlphaString(6) + "@yahoo.com";
		clientDemographics.setStreetEmail1(streetEmail1);
		
		logger.info("*** Step 6 Actions: - Enter Street Address Information (Contact 2, Phone2, Fax2 and Email2)");
		String streetContact2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetContact2(streetContact2);
		
		String streetPhone2 = "(619) 457-4512";
		clientDemographics.setStreetPhone2(streetPhone2);			
		
		String streetFax2 = "(619) 457-4545";
		clientDemographics.setStreetFax2(streetFax2);
		
		String streetEmail2 = randomCharacter.getRandomAlphaString(6) + "@san.rr.com";
		clientDemographics.setStreetEmail2(streetEmail2);		
				
		logger.info("*** Step 7 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 8 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);	
		
		logger.info("*** Step 9 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 10 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 10 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
				
		logger.info("*** Step 10 Expected Results: - Verify that Client Street Address Information (Contact 1, Preferred Contact Method, Addr1, Addr2, Zip, City, State, Phone1, Fax1 and Email1) display properly");
		Assert.assertTrue(clientDemographics.streetContact1Input().getAttribute("value").trim().contains(streetContact1), "        Street Contact1: " + streetContact1 + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.streetContctMethodDropDown(), "E-Mail"), "        Street Contact Method: E-Mail should be selected.");		
		Assert.assertTrue(clientDemographics.streetAddr1Input().getAttribute("value").trim().contains(streetAddr1), "        Street Addr1: " + streetAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.streetAddr2Input().getAttribute("value").trim().contains(streetAddr2), "        Street Addr2: " + streetAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.streetZipInput().getAttribute("value").trim().contains(streetZip), "        Street Zip: " + streetZip + " should show.");
		Assert.assertTrue(clientDemographics.streetCityInput().getAttribute("value").trim().contains("SAN DIEGO"), "        Street City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.streetStateDropDown(), "CA"), "        Street State: CA should be selected.");		
		Assert.assertTrue(clientDemographics.streetPhone1Input().getAttribute("value").trim().contains(streetPhone1), "        Street Phone1: " + streetPhone1 + " should show.");
		Assert.assertTrue(clientDemographics.streetFax1Input().getAttribute("value").trim().contains(streetFax1), "        Street Fax1: " + streetFax1 + " should show.");
		Assert.assertTrue(clientDemographics.streetEmail1Input().getAttribute("value").trim().contains(streetEmail1), "        Street Email1: " + streetEmail1 + " should show.");
		
		logger.info("*** Step 10 Expected Results: - Verify that Client Street Address Information (Contact 2, Phone2, Fax2 and Email2) display properly");
		Assert.assertTrue(clientDemographics.streetContact2Input().getAttribute("value").trim().contains(streetContact2), "        Street Contact2: " + streetContact2 + " should show.");
		Assert.assertTrue(clientDemographics.streetPhone2Input().getAttribute("value").trim().contains(streetPhone2), "        Street Phone2: " + streetPhone2 + " should show.");
		Assert.assertTrue(clientDemographics.streetFax2Input().getAttribute("value").trim().contains(streetFax2), "        Street Fax2: " + streetFax2 + " should show.");
		Assert.assertTrue(clientDemographics.streetEmail2Input().getAttribute("value").trim().contains(streetEmail2), "        Street Email1: " + streetEmail2 + " should show.");

		logger.info("*** Step 11 Actions: - Update Street Address Information (Contact 1, Preferred Contact Method, Addr1, Addr2, Zip, Phone1, Fax1 and Email1)");
		streetContact1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetContact1(streetContact1);
		
		selectItem(clientDemographics.streetContctMethodDropDown(), "Fax");		
		
		streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);
		
		streetAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr2(streetAddr2);
		
		streetZip = "32202";
		clientDemographics.setStreetZip(streetZip);
		//Clear the City and the State fields
		clientDemographics.streetCityInput().clear();	
		new Select(clientDemographics.streetStateDropDown()).selectByValue(" ");
		
		streetPhone1 = "(619) 457-2154";
		clientDemographics.setStreetPhone1(streetPhone1);			
		
		streetFax1 = "(619) 457-5454";
		clientDemographics.setStreetFax1(streetFax1);
		
		streetEmail1 = randomCharacter.getRandomAlphaString(6) + "@xifin.com";
		clientDemographics.setStreetEmail1(streetEmail1);
		
		logger.info("*** Step 12 Actions: - Update Street Address Information (Contact 2, Phone2, Fax2 and Email2)");
		streetContact2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetContact2(streetContact2);
		
		streetPhone2 = "(858) 457-1234";
		clientDemographics.setStreetPhone2(streetPhone2);			
		
		streetFax2 = "(858) 457-4444";
		clientDemographics.setStreetFax2(streetFax2);
		
		streetEmail2 = randomCharacter.getRandomAlphaString(6) + "@google.com";
		clientDemographics.setStreetEmail2(streetEmail2);		
			
		logger.info("*** Step 13 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 14 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 14 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
				
		logger.info("*** Step 14 Expected Results: - Verify that Updated Client Street Address Information (Contact 1, Preferred Contact Method, Addr1, Addr2, Zip, City, State, Phone1, Fax1 and Email1) display properly");
		Assert.assertTrue(clientDemographics.streetContact1Input().getAttribute("value").trim().contains(streetContact1), "        Updated Street Contact1: " + streetContact1 + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.streetContctMethodDropDown(), "Fax"), "        Updated Street Contact Method: E-Mail should be selected.");		
		Assert.assertTrue(clientDemographics.streetAddr1Input().getAttribute("value").trim().contains(streetAddr1), "        Updated Street Addr1: " + streetAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.streetAddr2Input().getAttribute("value").trim().contains(streetAddr2), "        Updated Street Addr2: " + streetAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.streetZipInput().getAttribute("value").trim().contains(streetZip), "        Updated Street Zip: " + streetZip + " should show.");
		Assert.assertTrue(clientDemographics.streetCityInput().getAttribute("value").trim().contains("JACKSONVILLE"), "        Updated Street City: JACKSONVILLE should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.streetStateDropDown(), "FL"), "        Updated Street State: FL should be selected.");		
		Assert.assertTrue(clientDemographics.streetPhone1Input().getAttribute("value").trim().contains(streetPhone1), "        Updated Street Phone1: " + streetPhone1 + " should show.");
		Assert.assertTrue(clientDemographics.streetFax1Input().getAttribute("value").trim().contains(streetFax1), "        Updated Street Fax1: " + streetFax1 + " should show.");
		Assert.assertTrue(clientDemographics.streetEmail1Input().getAttribute("value").trim().contains(streetEmail1), "        Updated Street Email1: " + streetEmail1 + " should show.");
		
		logger.info("*** Step 14 Expected Results: - Verify that updated Client Street Address Information (Contact 2, Phone2, Fax2 and Email2) display properly");
		Assert.assertTrue(clientDemographics.streetContact2Input().getAttribute("value").trim().contains(streetContact2), "        Updated Street Contact2: " + streetContact2 + " should show.");
		Assert.assertTrue(clientDemographics.streetPhone2Input().getAttribute("value").trim().contains(streetPhone2), "        Updated Street Phone2: " + streetPhone2 + " should show.");
		Assert.assertTrue(clientDemographics.streetFax2Input().getAttribute("value").trim().contains(streetFax2), "        Updated Street Fax2: " + streetFax2 + " should show.");
		Assert.assertTrue(clientDemographics.streetEmail2Input().getAttribute("value").trim().contains(streetEmail2), "        Updated Street Email1: " + streetEmail2 + " should show.");
			
		logger.info("*** Step 15 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Demographics - Client Correspondence Info diaplay")
	@Parameters({"email", "password"})
	public void testRPM_693(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_693 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 4 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);
		
		logger.info("*** Step 5 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
				
		logger.info("*** Step 6 Actions: - Enter Correspondence Address Information (Contact 1, Addr1, Addr2, Zip, Phone1, Fax1 and Email1)");
		String correspContact1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespContact1(correspContact1);				
		
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr2(correspAddr2);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(716) 457-4512";
		clientDemographics.setCorrespPhone1(correspPhone1);			
		
		String correspFax1 = "(716) 457-4545";
		clientDemographics.setCorrespFax1(correspFax1);
		
		String correspEmail1 = randomCharacter.getRandomAlphaString(6) + "@yahoo.com";
		clientDemographics.setCorrespEmail1(correspEmail1);
				
		logger.info("*** Step 7 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);	
		
		logger.info("*** Step 8 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 9 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 9 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
				
		logger.info("*** Step 9 Expected Results: - Verify that Client Correspondence Address Information (Contact 1, Addr1, Addr2, Zip, City, State, Phone1, Fax1 and Email1) display properly");
		Assert.assertTrue(clientDemographics.correspContact1Input().getAttribute("value").trim().contains(correspContact1), "        Correspondence Contact1: " + correspContact1 + " should show.");			
		Assert.assertTrue(clientDemographics.correspAddr1Input().getAttribute("value").trim().contains(correspAddr1), "        Correspondence Addr1: " + correspAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.correspAddr2Input().getAttribute("value").trim().contains(correspAddr2), "        Correspondence Addr2: " + correspAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.correspZipInput().getAttribute("value").trim().contains(correspZip), "        Correspondence Zip: " + correspZip + " should show.");
		Assert.assertTrue(clientDemographics.correspCityInput().getAttribute("value").trim().contains("SAN DIEGO"), "        Correspondence City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.correspStateDropDown(), "CA"), "        Correspondence State: CA should be selected.");		
		Assert.assertTrue(clientDemographics.correspPhone1Input().getAttribute("value").trim().contains(correspPhone1), "        Correspondence Phone1: " + correspPhone1 + " should show.");
		Assert.assertTrue(clientDemographics.correspFax1Input().getAttribute("value").trim().contains(correspFax1), "        Correspondence Fax1: " + correspFax1 + " should show.");
		Assert.assertTrue(clientDemographics.correspEmail1Input().getAttribute("value").trim().contains(correspEmail1), "        Correspondence Email1: " + correspEmail1 + " should show.");
		
		logger.info("*** Step 10 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}	

	@Test(priority = 1, description = "Client Processing - Demographics - Update Client Correspondence Info")
	@Parameters({"email", "password"})
	public void testRPM_694(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_694 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 4 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);
		
		logger.info("*** Step 5 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
				
		logger.info("*** Step 6 Actions: - Enter Correspondence Address Information (Contact 1, Addr1, Addr2, Zip, Phone1, Fax1 and Email1)");
		String correspContact1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespContact1(correspContact1);				
		
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr2(correspAddr2);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(716) 457-4512";
		clientDemographics.setCorrespPhone1(correspPhone1);			
		
		String correspFax1 = "(716) 457-4545";
		clientDemographics.setCorrespFax1(correspFax1);
		
		String correspEmail1 = randomCharacter.getRandomAlphaString(6) + "@yahoo.com";
		clientDemographics.setCorrespEmail1(correspEmail1);
				
		logger.info("*** Step 7 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);	
		
		logger.info("*** Step 8 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 9 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 9 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
				
		logger.info("*** Step 9 Expected Results: - Verify that Client Correspondence Address Information (Contact 1, Addr1, Addr2, Zip, City, State, Phone1, Fax1 and Email1) display properly");
		Assert.assertTrue(clientDemographics.correspContact1Input().getAttribute("value").trim().contains(correspContact1), "        Correspondence Contact1: " + correspContact1 + " should show.");			
		Assert.assertTrue(clientDemographics.correspAddr1Input().getAttribute("value").trim().contains(correspAddr1), "        Correspondence Addr1: " + correspAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.correspAddr2Input().getAttribute("value").trim().contains(correspAddr2), "        Correspondence Addr2: " + correspAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.correspZipInput().getAttribute("value").trim().contains(correspZip), "        Correspondence Zip: " + correspZip + " should show.");
		Assert.assertTrue(clientDemographics.correspCityInput().getAttribute("value").trim().contains("SAN DIEGO"), "        Correspondence City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.correspStateDropDown(), "CA"), "        Correspondence State: CA should be selected.");		
		Assert.assertTrue(clientDemographics.correspPhone1Input().getAttribute("value").trim().contains(correspPhone1), "        Correspondence Phone1: " + correspPhone1 + " should show.");
		Assert.assertTrue(clientDemographics.correspFax1Input().getAttribute("value").trim().contains(correspFax1), "        Correspondence Fax1: " + correspFax1 + " should show.");
		Assert.assertTrue(clientDemographics.correspEmail1Input().getAttribute("value").trim().contains(correspEmail1), "        Correspondence Email1: " + correspEmail1 + " should show.");
				
		logger.info("*** Step 10 Actions: - Update Correspondence Address Information (Contact 1, Addr1, Addr2, Zip, Phone1, Fax1 and Email1)");
		correspContact1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespContact1(correspContact1);				
		
		correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		correspAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr2(correspAddr2);
		
		correspZip = "32202";
		clientDemographics.setCorrespZip(correspZip);
		clientDemographics.correspCityInput().clear();
		new Select(clientDemographics.correspStateDropDown()).selectByValue(" ");		
		
		correspPhone1 = "(212) 457-4512";
		clientDemographics.setCorrespPhone1(correspPhone1);			
		
		correspFax1 = "(212) 457-4545";
		clientDemographics.setCorrespFax1(correspFax1);
		
		correspEmail1 = randomCharacter.getRandomAlphaString(6) + "@google.com";
		clientDemographics.setCorrespEmail1(correspEmail1);
				
		logger.info("*** Step 11 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 12 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 12 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
				
		logger.info("*** Step 12 Expected Results: - Verify that updated Client Correspondence Address Information (Contact 1, Addr1, Addr2, Zip, City, State, Phone1, Fax1 and Email1) display properly");
		Assert.assertTrue(clientDemographics.correspContact1Input().getAttribute("value").trim().contains(correspContact1), "        Updated Correspondence Contact1: " + correspContact1 + " should show.");			
		Assert.assertTrue(clientDemographics.correspAddr1Input().getAttribute("value").trim().contains(correspAddr1), "        Updated Correspondence Addr1: " + correspAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.correspAddr2Input().getAttribute("value").trim().contains(correspAddr2), "        Updated Correspondence Addr2: " + correspAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.correspZipInput().getAttribute("value").trim().contains(correspZip), "        Updated Correspondence Zip: " + correspZip + " should show.");
		Assert.assertTrue(clientDemographics.correspCityInput().getAttribute("value").trim().contains("JACKSONVILLE"), "        Updated Correspondence City: JACKSONVILLE should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.correspStateDropDown(), "FL"), "        Updated Correspondence State: FL should be selected.");		
		Assert.assertTrue(clientDemographics.correspPhone1Input().getAttribute("value").trim().contains(correspPhone1), "        Updated Correspondence Phone1: " + correspPhone1 + " should show.");
		Assert.assertTrue(clientDemographics.correspFax1Input().getAttribute("value").trim().contains(correspFax1), "        Updated Correspondence Fax1: " + correspFax1 + " should show.");
		Assert.assertTrue(clientDemographics.correspEmail1Input().getAttribute("value").trim().contains(correspEmail1), "        Updated Correspondence Email1: " + correspEmail1 + " should show.");
				
		logger.info("*** Step 13 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Demographics - Client Comments diaplay")
	@Parameters({"email", "password"})
	public void testRPM_695(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_695 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 3 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		logger.info("*** Step 3 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 3 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 3 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);	
				
		logger.info("*** Step 4 Actions: - Enter General Comments");
		String genComment = randomCharacter.getRandomAlphaString(10);
		clientDemographics.setGeneralComments(genComment);
		
		logger.info("*** Step 5 Actions: - Enter Internal Comments");
		String interComment = randomCharacter.getRandomAlphaString(10);
		clientDemographics.setInternalComments(interComment);		
		
		logger.info("*** Step 6 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 7 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 7 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");

		logger.info("*** Step 7 Expected Results: - Verify that General and Internal Comments info display properly");
		Assert.assertTrue(clientDemographics.generalCommentsInput().getAttribute("value").trim().contains(genComment), "        General Comments: " + genComment + " should show.");
		Assert.assertTrue(clientDemographics.internalCommentsInput().getAttribute("value").trim().contains(interComment), "        Internal Comments: " + interComment + " should show.");
			
		logger.info("*** Step 8 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}	
	
	@Test(priority = 1, description = "Client Processing - Demographics - Update Client Comments")
	@Parameters({"email", "password"})
	public void testRPM_696(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_696 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 3 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		logger.info("*** Step 3 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 3 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 3 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);	
				
		logger.info("*** Step 4 Actions: - Enter General Comments");
		String genComment = randomCharacter.getRandomAlphaString(10);
		clientDemographics.setGeneralComments(genComment);
		
		logger.info("*** Step 5 Actions: - Enter Internal Comments");
		String interComment = randomCharacter.getRandomAlphaString(10);
		clientDemographics.setInternalComments(interComment);		
		
		logger.info("*** Step 6 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 7 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 7 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
		
		logger.info("*** Step 7 Expected Results: - Verify that General and Internal Comments info display properly");
		Assert.assertTrue(clientDemographics.generalCommentsInput().getAttribute("value").trim().contains(genComment), "        General Comments: " + genComment + " should show.");
		Assert.assertTrue(clientDemographics.internalCommentsInput().getAttribute("value").trim().contains(interComment), "        Internal Comments: " + interComment + " should show.");
		
		logger.info("*** Step 8 Actions: - Update General Comments");
		genComment = randomCharacter.getRandomAlphaString(10);
		clientDemographics.setGeneralComments(genComment);
		
		logger.info("*** Step 9 Actions: - Update Internal Comments");
		interComment = randomCharacter.getRandomAlphaString(10);
		clientDemographics.setInternalComments(interComment);
		
		logger.info("*** Step 10 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 11 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 11 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");

		logger.info("*** Step 11 Expected Results: - Verify that updated General and Internal Comments info display properly");
		Assert.assertTrue(clientDemographics.generalCommentsInput().getAttribute("value").trim().contains(genComment), "       Updated General Comments: " + genComment + " should show.");
		Assert.assertTrue(clientDemographics.internalCommentsInput().getAttribute("value").trim().contains(interComment), "       Updated Internal Comments: " + interComment + " should show.");
			
		logger.info("*** Step 12 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}	
	

	@Test(priority = 1, description = "Client Processing - Demographics - Reset")
	@Parameters({"email", "password"})
	public void testRPM_698(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_698 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter an existing Client ID in Client ID field and tab out");
		ArrayList<String> clnList = daoManagerXifinRpm.getClnAbbrevFromCLN(testDb);
		String clnAbbrev = clnList.get(1);
		String clnName = clnList.get(2);
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Client is loaded properly");
		Assert.assertTrue(clientDemographics.clnIdInput().getAttribute("value").trim().contains(clnAbbrev), "        Client ID: " + clnAbbrev + " should show.");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnName), "        Client Name: " + clnName + " should show.");
				
		logger.info("*** Step 4 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();			
		
		logger.info("*** Step 4 Expected Results: - Verify that Client ID and Name fields are cleared");
		Assert.assertEquals(clientDemographics.clnIdInput().getAttribute("value").trim().length(), 0);
		Assert.assertEquals(clientDemographics.nameInput().getAttribute("value").trim().length(), 0);
	
		driver.close();	
	}	

	
	@Test(priority = 1, description = "Client Processing - Demographics - Help")
	@Parameters({"email", "password"})
	public void testRPM_699(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_699 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();	
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();		
			
		logger.info("*** Step 3 Actions: - Click Client Demographics link");
		clientNavigation = new ClientNavigation(driver, config);
		clientNavigation.clientDeomgraphicsLink();
		
		logger.info("*** Step 4 Actions: - Click Help Button");
		clientDemographics = new ClientDemographics(driver, wait);
		clientDemographics.helpBtnInFrame();
		switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Verify that Client Demographics Help file opens");
		Assert.assertTrue(driver.getTitle().trim().contains("Client Demographics"), "        Client Demographics help file should show.");

		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Demographics - Client Refund Address display")
	@Parameters({"email", "password"})
	public void testRPM_702(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_702 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");	
		String customer = daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb).trim();
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(),customer, "        Customer " + customer + " should show.");
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
				
		logger.info("*** Step 4 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		logger.info("*** Step 5 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 6 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 7 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);
				
		logger.info("*** Step 8 Actions: - Enter Client Refund Address Information (Addr1, Addr2 and Zip)");
		String refundAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setRefundAddr1(refundAddr1);
		
		String refundAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setRefundAddr2(refundAddr2);
		
		String refundZip = "92127";
		clientDemographics.setRefundZip(refundZip);
						
		logger.info("*** Step 9 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 10 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 10 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
				
		logger.info("*** Step 10 Expected Results: - Verify that Client Refund Address Information (Addr1, Addr2, Zip, City, State) display properly");
		Assert.assertTrue(clientDemographics.refundAddr1Input().getAttribute("value").trim().contains(refundAddr1), "        Refund Addr1: " + refundAddr1 + " should show.");			
		Assert.assertTrue(clientDemographics.refundAddr2Input().getAttribute("value").trim().contains(refundAddr2), "        Refund Addr2: " + refundAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.refundZipInput().getAttribute("value").trim().contains(refundZip), "        Refund Zip: " + refundZip + " should show.");
		Assert.assertTrue(clientDemographics.refundCityInput().getAttribute("value").trim().contains("SAN DIEGO"), "        Refund City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.refundStateDropDown(), "CA"), "        Refund State: CA should be selected.");		

		logger.info("*** Step 11 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}

	@Test(priority = 1, description = "Client Processing - Demographics - Update Client Refund Address")
	@Parameters({"email", "password"})
	public void testRPM_703(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_703 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");	
		String customer = daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb).trim();
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(),customer, "        Customer " + customer + " should show.");
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
				
		logger.info("*** Step 4 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		logger.info("*** Step 5 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 6 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 7 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);
				
		logger.info("*** Step 8 Actions: - Enter Client Refund Address Information (Addr1, Addr2 and Zip)");
		String refundAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setRefundAddr1(refundAddr1);
		
		String refundAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setRefundAddr2(refundAddr2);
		
		String refundZip = "92128";
		clientDemographics.setRefundZip(correspZip);
						
		logger.info("*** Step 9 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 10 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 10 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
				
		logger.info("*** Step 10 Expected Results: - Verify that Client Refund Address Information (Addr1, Addr2, Zip, City, State) display properly");
		Assert.assertTrue(clientDemographics.refundAddr1Input().getAttribute("value").trim().contains(refundAddr1), "        Refund Addr1: " + refundAddr1 + " should show.");			
		Assert.assertTrue(clientDemographics.refundAddr2Input().getAttribute("value").trim().contains(refundAddr2), "        Refund Addr2: " + refundAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.refundZipInput().getAttribute("value").trim().contains(refundZip), "        Refund Zip: " + refundZip + " should show.");
		Assert.assertTrue(clientDemographics.refundCityInput().getAttribute("value").trim().contains("SAN DIEGO"), "        Refund City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.refundStateDropDown(), "CA"), "        Refund State: CA should be selected.");		
				
		logger.info("*** Step 11 Actions: - Enter Updated Client Refund Address Information (Addr1, Addr2 and Zip)");
		refundAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setRefundAddr1(refundAddr1);
		
		refundAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setRefundAddr2(refundAddr2);
		
		refundZip = "32202";
		clientDemographics.setRefundZip(refundZip);
		clientDemographics.refundCityInput().clear();
		new Select(clientDemographics.refundStateDropDown()).selectByValue(" ");
						
		logger.info("*** Step 12 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 13 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 13 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
				
		logger.info("*** Step 13 Expected Results: - Verify that updated Client Refund Address Information (Addr1, Addr2, Zip, City, State) display properly");
		Assert.assertTrue(clientDemographics.refundAddr1Input().getAttribute("value").trim().contains(refundAddr1), "        Updated Refund Addr1: " + refundAddr1 + " should show.");			
		Assert.assertTrue(clientDemographics.refundAddr2Input().getAttribute("value").trim().contains(refundAddr2), "        Updated Refund Addr2: " + refundAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.refundZipInput().getAttribute("value").trim().contains(refundZip), "        Updated Refund Zip: " + refundZip + " should show.");
		Assert.assertTrue(clientDemographics.refundCityInput().getAttribute("value").trim().contains("JACKSONVILLE"), "        Updated Refund City: JACKSONVILLE should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.refundStateDropDown(), "FL"), "        Updated Refund State: FL should be selected.");
		
		logger.info("*** Step 14 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}	
	
	@Test(priority = 1, description = "Client Processing - Demographics - Client Shipping Info diaplay")
	@Parameters({"email", "password"})
	public void testRPM_719(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_719 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");	
		String customer = daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb).trim();
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(),customer, "        Customer " + customer + " should show.");
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 4 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);
		
		logger.info("*** Step 5 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 6 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);	
						
		logger.info("*** Step 7 Actions: - Enter Shipping Address Information (Contact 1, Addr1, Addr2, Zip, Phone1, Fax1 and Email1)");
		String shippingContact1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingContact1(shippingContact1);				
		
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr2(shippingAddr2);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);				
		
		String shippingFax1 = "(716) 457-4545";
		clientDemographics.setShippingFax1(shippingFax1);
		
		String shippingEmail1 = randomCharacter.getRandomAlphaString(6) + "@yahoo.com";
		clientDemographics.setShippingEmail1(shippingEmail1);
			
		logger.info("*** Step 8 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 9 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 9 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
				
		logger.info("*** Step 9 Expected Results: - Verify that Client Shipping Address Information (Contact 1, Addr1, Addr2, Zip, City, State, Phone1, Fax1 and Email1) display properly");
		Assert.assertTrue(clientDemographics.shippingContact1Input().getAttribute("value").trim().contains(shippingContact1), "        Shipping Contact1: " + shippingContact1 + " should show.");			
		Assert.assertTrue(clientDemographics.shippingAddr1Input().getAttribute("value").trim().contains(shippingAddr1), "        Shipping Addr1: " + shippingAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.shippingAddr2Input().getAttribute("value").trim().contains(shippingAddr2), "        Shipping Addr2: " + shippingAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.shippingZipInput().getAttribute("value").trim().contains(shippingZip), "        Shipping Zip: " + shippingZip + " should show.");
		Assert.assertTrue(clientDemographics.shippingCityInput().getAttribute("value").trim().contains("SAN DIEGO"), "        Shipping City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.shippingStateDropDown(), "CA"), "        Shipping State: CA should be selected.");		
		Assert.assertTrue(clientDemographics.shippingPhone1Input().getAttribute("value").trim().contains(shippingPhone1), "        Shipping Phone1: " + shippingPhone1 + " should show.");
		Assert.assertTrue(clientDemographics.shippingFax1Input().getAttribute("value").trim().contains(shippingFax1), "        Shipping Fax1: " + shippingFax1 + " should show.");
		Assert.assertTrue(clientDemographics.shippingEmail1Input().getAttribute("value").trim().contains(shippingEmail1), "        Shipping Email1: " + shippingEmail1 + " should show.");
		
		logger.info("*** Step 10 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}	

	@Test(priority = 1, description = "Client Processing - Demographics - Update Client Shipping Address Info")
	@Parameters({"email", "password"})
	public void testRPM_720(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_720 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");	
		String customer = daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb).trim();
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(),customer, "        Customer " + customer + " should show.");
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 4 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);
		
		logger.info("*** Step 5 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 6 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);	
						
		logger.info("*** Step 7 Actions: - Enter Shipping Address Information (Contact 1, Addr1, Addr2, Zip, Phone1, Fax1 and Email1)");
		String shippingContact1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingContact1(shippingContact1);				
		
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr2(shippingAddr2);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);				
		
		String shippingFax1 = "(716) 457-4545";
		clientDemographics.setShippingFax1(shippingFax1);
		
		String shippingEmail1 = randomCharacter.getRandomAlphaString(6) + "@yahoo.com";
		clientDemographics.setShippingEmail1(shippingEmail1);
			
		logger.info("*** Step 8 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 9 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 9 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
				
		logger.info("*** Step 9 Expected Results: - Verify that Client Shipping Address Information (Contact 1, Addr1, Addr2, Zip, City, State, Phone1, Fax1 and Email1) display properly");
		Assert.assertTrue(clientDemographics.shippingContact1Input().getAttribute("value").trim().contains(shippingContact1), "        Shipping Contact1: " + shippingContact1 + " should show.");			
		Assert.assertTrue(clientDemographics.shippingAddr1Input().getAttribute("value").trim().contains(shippingAddr1), "        Shipping Addr1: " + shippingAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.shippingAddr2Input().getAttribute("value").trim().contains(shippingAddr2), "        Shipping Addr2: " + shippingAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.shippingZipInput().getAttribute("value").trim().contains(shippingZip), "        Shipping Zip: " + shippingZip + " should show.");
		Assert.assertTrue(clientDemographics.shippingCityInput().getAttribute("value").trim().contains("SAN DIEGO"), "        Shipping City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.shippingStateDropDown(), "CA"), "        Shipping State: CA should be selected.");		
		Assert.assertTrue(clientDemographics.shippingPhone1Input().getAttribute("value").trim().contains(shippingPhone1), "        Shipping Phone1: " + shippingPhone1 + " should show.");
		Assert.assertTrue(clientDemographics.shippingFax1Input().getAttribute("value").trim().contains(shippingFax1), "        Shipping Fax1: " + shippingFax1 + " should show.");
		Assert.assertTrue(clientDemographics.shippingEmail1Input().getAttribute("value").trim().contains(shippingEmail1), "        Shipping Email1: " + shippingEmail1 + " should show.");
				
		logger.info("*** Step 10 Actions: - Update Shipping Address Information (Contact 1, Addr1, Addr2, Zip, Phone1, Fax1 and Email1)");
		shippingContact1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingContact1(shippingContact1);				
		
		shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		shippingAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr2(shippingAddr2);
		
		shippingZip = "32202";
		clientDemographics.setShippingZip(shippingZip);
		clientDemographics.shippingCityInput().clear();
		new Select(clientDemographics.shippingStateDropDown()).selectByValue(" ");
				
		shippingPhone1 = "(714) 537-9217";
		clientDemographics.setShippingPhone1(shippingPhone1);				
		
		shippingFax1 = "(714) 457-5454";
		clientDemographics.setShippingFax1(shippingFax1);
		
		shippingEmail1 = randomCharacter.getRandomAlphaString(6) + "@san.rr.com";
		clientDemographics.setShippingEmail1(shippingEmail1);
			
		logger.info("*** Step 11 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 12 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 12 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
				
		logger.info("*** Step 12 Expected Results: - Verify that updated Client Shipping Address Information (Contact 1, Addr1, Addr2, Zip, City, State, Phone1, Fax1 and Email1) display properly");
		Assert.assertTrue(clientDemographics.shippingContact1Input().getAttribute("value").trim().contains(shippingContact1), "        Updated Shipping Contact1: " + shippingContact1 + " should show.");			
		Assert.assertTrue(clientDemographics.shippingAddr1Input().getAttribute("value").trim().contains(shippingAddr1), "        Updated Shipping Addr1: " + shippingAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.shippingAddr2Input().getAttribute("value").trim().contains(shippingAddr2), "        Updated Shipping Addr2: " + shippingAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.shippingZipInput().getAttribute("value").trim().contains(shippingZip), "        Updated Shipping Zip: " + shippingZip + " should show.");
		Assert.assertTrue(clientDemographics.shippingCityInput().getAttribute("value").trim().contains("JACKSONVILLE"), "        Updated Shipping City: JACKSONVILLE should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.shippingStateDropDown(), "FL"), "        Updated Shipping State: FL should be selected.");		
		Assert.assertTrue(clientDemographics.shippingPhone1Input().getAttribute("value").trim().contains(shippingPhone1), "        Updated Shipping Phone1: " + shippingPhone1 + " should show.");
		Assert.assertTrue(clientDemographics.shippingFax1Input().getAttribute("value").trim().contains(shippingFax1), "        Updated Shipping Fax1: " + shippingFax1 + " should show.");
		Assert.assertTrue(clientDemographics.shippingEmail1Input().getAttribute("value").trim().contains(shippingEmail1), "        Updated Shipping Email1: " + shippingEmail1 + " should show.");
				
		logger.info("*** Step 13 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}	
	
	@Test(priority = 1, description = "Client Processing - Demographics - Copy Billing Address to Shipping Address")
	@Parameters({"email", "password"})
	public void testRPM_721(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_721 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 3 Actions: - Enter Billing Address Information (Contact 1, Preferred Contact Method, Addr1, Addr2, Zip, Phone1, Fax1 and Email1)");
		String bilContact1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingContact1(bilContact1);
		
		selectItem(clientDemographics.billingContctMethodDropDown(), "E-Mail");		
		
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String bilAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr2(bilAddr2);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		String billFax1 = "(858) 457-4545";
		clientDemographics.setBillingFax1(billFax1);
		
		String billEmail1 = randomCharacter.getRandomAlphaString(6) + "@xifin.com";
		clientDemographics.setBillingEmail1(billEmail1);
		
		logger.info("*** Step 3 Actions: - Enter Billing Address Information (Contact 2, Phone2, Fax2 and Email2)");
		String bilContact2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingContact2(bilContact2);
		
		String billPhone2 = "(619) 457-4512";
		clientDemographics.setBillingPhone2(billPhone2);			
		
		String billFax2 = "(619) 457-4545";
		clientDemographics.setBillingFax2(billFax2);
		
		String billEmail2 = randomCharacter.getRandomAlphaString(6) + "@gmail.com";
		clientDemographics.setBillingEmail2(billEmail2);		
		
		logger.info("*** Step 4 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 4 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 5 Actions: - Check Copy Billing Address to Shipping Address checkbox");
		selectCheckBox(clientDemographics.copyToShippingAddrCheckBox());
		
		logger.info("*** Step 6 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();	
		clientDemographics.resetBtn().click();
		
		logger.info("*** Step 7 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 7 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
				
		logger.info("*** Step 7 Expected Results: - Verify that Client Shipping Address Information (Contact 1, Addr1, Addr2, Zip, City, State, Phone1, Fax1 and Email1) display the same as Billing Address Information");
		Assert.assertTrue(clientDemographics.shippingContact1Input().getAttribute("value").trim().contains(bilContact1), "        Shipping Contact1: " + bilContact1 + " should show.");			
		Assert.assertTrue(clientDemographics.shippingAddr1Input().getAttribute("value").trim().contains(bilAddr1), "        Shipping Addr1: " + bilAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.shippingAddr2Input().getAttribute("value").trim().contains(bilAddr2), "        Shipping Addr2: " + bilAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.shippingZipInput().getAttribute("value").trim().contains(billZip), "        Shipping Zip: " + billZip + " should show.");
		Assert.assertTrue(clientDemographics.shippingCityInput().getAttribute("value").trim().contains("SAN DIEGO"), "        Shipping City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.shippingStateDropDown(), "CA"), "        Shipping State: CA should be selected.");		
		Assert.assertTrue(clientDemographics.shippingPhone1Input().getAttribute("value").trim().contains(billPhone1), "        Shipping Phone1: " + billPhone1 + " should show.");
		Assert.assertTrue(clientDemographics.shippingFax1Input().getAttribute("value").trim().contains(billFax1), "        Shipping Fax1: " + billFax1 + " should show.");
		Assert.assertTrue(clientDemographics.shippingEmail1Input().getAttribute("value").trim().contains(billEmail1), "        Shipping Email1: " + billEmail1 + " should show.");
				
		logger.info("*** Step 8 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Demographics - Copy Billing Address to Street Address")
	@Parameters({"email", "password"})
	public void testRPM_700(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_700 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 3 Actions: - Enter Billing Address Information (Contact 1, Preferred Contact Method, Addr1, Addr2, Zip, Phone1, Fax1 and Email1)");
		String bilContact1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingContact1(bilContact1);
		
		selectItem(clientDemographics.billingContctMethodDropDown(), "E-Mail");		
		
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String bilAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr2(bilAddr2);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		String billFax1 = "(858) 457-4545";
		clientDemographics.setBillingFax1(billFax1);
		
		String billEmail1 = randomCharacter.getRandomAlphaString(6) + "@xifin.com";
		clientDemographics.setBillingEmail1(billEmail1);
		
		logger.info("*** Step 3 Actions: - Enter Billing Address Information (Contact 2, Phone2, Fax2 and Email2)");
		String bilContact2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingContact2(bilContact2);
		
		String billPhone2 = "(619) 457-4512";
		clientDemographics.setBillingPhone2(billPhone2);			
		
		String billFax2 = "(619) 457-4545";
		clientDemographics.setBillingFax2(billFax2);
		
		String billEmail2 = randomCharacter.getRandomAlphaString(6) + "@gmail.com";
		clientDemographics.setBillingEmail2(billEmail2);	
				
		logger.info("*** Step 4 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 4 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);
				
		logger.info("*** Step 5 Actions: - Check Copy Billing Address to Street Address checkbox");
		selectCheckBox(clientDemographics.copyToStreetAddrCheckBox());		
		
		logger.info("*** Step 6 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();		
		clientDemographics.resetBtn().click();
		
		logger.info("*** Step 7 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 7 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
				
		logger.info("*** Step 7 Expected Results: - Verify that Client Street Address Information (Contact 1, Addr1, Addr2, Zip, City, State, Phone1, Fax1 and Email1) display the same as Billing Address Information");
		Assert.assertTrue(clientDemographics.streetContact1Input().getAttribute("value").trim().contains(bilContact1), "        Street Contact1: " + bilContact1 + " should show.");			
		Assert.assertTrue(clientDemographics.streetAddr1Input().getAttribute("value").trim().contains(bilAddr1), "        Street Addr1: " + bilAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.streetAddr2Input().getAttribute("value").trim().contains(bilAddr2), "        Street Addr2: " + bilAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.streetZipInput().getAttribute("value").trim().contains(billZip), "        Street Zip: " + billZip + " should show.");
		Assert.assertTrue(clientDemographics.streetCityInput().getAttribute("value").trim().contains("SAN DIEGO"), "        Street City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.streetStateDropDown(), "CA"), "        Street State: CA should be selected.");		
		Assert.assertTrue(clientDemographics.streetPhone1Input().getAttribute("value").trim().contains(billPhone1), "        Street Phone1: " + billPhone1 + " should show.");
		Assert.assertTrue(clientDemographics.streetFax1Input().getAttribute("value").trim().contains(billFax1), "        Street Fax1: " + billFax1 + " should show.");
		Assert.assertTrue(clientDemographics.streetEmail1Input().getAttribute("value").trim().contains(billEmail1), "        Street Email1: " + billEmail1 + " should show.");
				
		logger.info("*** Step 7 Expected Results: - Verify that Client Street Address Information (Contact 2, Phone2, Fax2 and Email2) display display the same as Billing Address Information");
		Assert.assertTrue(clientDemographics.streetContact2Input().getAttribute("value").trim().contains(bilContact2), "        Street Contact2: " + bilContact2 + " should show.");
		Assert.assertTrue(clientDemographics.streetPhone2Input().getAttribute("value").trim().contains(billPhone2), "        Street Phone2: " + billPhone2 + " should show.");
		Assert.assertTrue(clientDemographics.streetFax2Input().getAttribute("value").trim().contains(billFax2), "        Street Fax2: " + billFax2 + " should show.");
		Assert.assertTrue(clientDemographics.streetEmail2Input().getAttribute("value").trim().contains(billEmail2), "        Street Email1: " + billEmail2 + " should show.");
		
		logger.info("*** Step 8 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}
	
	@Test(priority = 1, description = "Client Processing - Demographics - Copy Billing Address to Correspondence Address")
	@Parameters({"email", "password"})
	public void testRPM_701(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_701 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 3 Actions: - Enter Billing Address Information (Contact 1, Preferred Contact Method, Addr1, Addr2, Zip, Phone1, Fax1 and Email1)");
		String bilContact1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingContact1(bilContact1);
		
		selectItem(clientDemographics.billingContctMethodDropDown(), "E-Mail");		
		
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String bilAddr2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr2(bilAddr2);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		String billFax1 = "(858) 457-4545";
		clientDemographics.setBillingFax1(billFax1);
		
		String billEmail1 = randomCharacter.getRandomAlphaString(6) + "@xifin.com";
		clientDemographics.setBillingEmail1(billEmail1);
		
		logger.info("*** Step 3 Actions: - Enter Billing Address Information (Contact 2, Phone2, Fax2 and Email2)");
		String bilContact2 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingContact2(bilContact2);
		
		String billPhone2 = "(619) 457-4512";
		clientDemographics.setBillingPhone2(billPhone2);			
		
		String billFax2 = "(619) 457-4545";
		clientDemographics.setBillingFax2(billFax2);
		
		String billEmail2 = randomCharacter.getRandomAlphaString(6) + "@gmail.com";
		clientDemographics.setBillingEmail2(billEmail2);		
		
		logger.info("*** Step 4 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);		
		
		logger.info("*** Step 4 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);
		
		logger.info("*** Step 5 Actions: - Check Copy Billing Address to Correspondence Address checkbox");
		selectCheckBox(clientDemographics.copyToCorrespAddrCheckBox());
		
		logger.info("*** Step 6 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();	
		clientDemographics.resetBtn().click();
		
		logger.info("*** Step 7 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 7 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
				
		logger.info("*** Step 7 Expected Results: - Verify that Client Correspondence Address Information (Contact 1, Addr1, Addr2, Zip, City, State, Phone1, Fax1 and Email1) display the same as Billing Address Information");
		Assert.assertTrue(clientDemographics.correspContact1Input().getAttribute("value").trim().contains(bilContact1), "        Correspondence Contact1: " + bilContact1 + " should show.");			
		Assert.assertTrue(clientDemographics.correspAddr1Input().getAttribute("value").trim().contains(bilAddr1), "        Correspondence Addr1: " + bilAddr1 + " should show.");
		Assert.assertTrue(clientDemographics.correspAddr2Input().getAttribute("value").trim().contains(bilAddr2), "        Correspondence Addr2: " + bilAddr2 + " should show.");		
		Assert.assertTrue(clientDemographics.correspZipInput().getAttribute("value").trim().contains(billZip), "        Correspondence Zip: " + billZip + " should show.");
		Assert.assertTrue(clientDemographics.correspCityInput().getAttribute("value").trim().contains("SAN DIEGO"), "        Correspondence City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.correspStateDropDown(), "CA"), "        Correspondence State: CA should be selected.");		
		Assert.assertTrue(clientDemographics.correspPhone1Input().getAttribute("value").trim().contains(billPhone1), "        Correspondence Phone1: " + billPhone1 + " should show.");
		Assert.assertTrue(clientDemographics.correspFax1Input().getAttribute("value").trim().contains(billFax1), "        Correspondence Fax1: " + billFax1 + " should show.");
		Assert.assertTrue(clientDemographics.correspEmail1Input().getAttribute("value").trim().contains(billEmail1), "        Correspondence Email1: " + billEmail1 + " should show.");
				
		logger.info("*** Step 8 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}

	@Test(priority = 1, description = "Client Processing - Demographics - Add Xref to the Client that does not have Xref")
	@Parameters({"email", "password"})
	public void testRPM_722(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_722 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
					
		logger.info("*** Step 2 Actions: - Navigate to Client Tab > Client Demographics JSP");
		switchToDefaultWinFromFrame();			
		headerNavigation.navigateToClientTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that it's on the Client - Demographics page");		
		clientDemographics = new ClientDemographics(driver, wait);
		Assert.assertTrue(clientDemographics.pageTitleText().trim().contains("Client - Demographics"), "        Client - Demographics page title should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Client ID in Client ID field and tab out");
		randomCharacter = new RandomCharacter(driver);
		String clnAbbrev = "AUTOCLN" + randomCharacter.getRandomAlphaNumericString(6);		
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 3 Actions: - Enter Client Name, Account Type, Start Date, Primary Facility");
		clientDemographics.setName(clnAbbrev);
		
		String clnAccountTyp = daoManagerClientWS.getClientAccntType(testDb);
		selectItem(clientDemographics.accountTypeDropDown(), clnAccountTyp);
		
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		clientDemographics.setStartDate(currDt);
		
		String primaryFac = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(clientDemographics.primaryFacilityDropDown(), primaryFac);
		
		logger.info("*** Step 3 Actions: - Enter Billing Addr1, Zip and Phone1");
		String bilAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setBillingAddr1(bilAddr1);
		
		String billZip = "92129";
		clientDemographics.setBillingZip(billZip);
		
		String billPhone1 = "(858) 457-4512";
		clientDemographics.setBillingPhone1(billPhone1);			
		
		logger.info("*** Step 3 Actions: - Enter Street Addr1, Zip and Phone1");
		String streetAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setStreetAddr1(streetAddr1);		
		
		String streetZip = "92121";
		clientDemographics.setStreetZip(streetZip);		
		
		String streetPhone1 = "(858) 789-2145";
		clientDemographics.setStreetPhone1(streetPhone1);
		
		logger.info("*** Step 3 Actions: - Enter Correspondence Addr1, Zip and Phone1");
		String correspAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setCorrespAddr1(correspAddr1);
		
		String correspZip = "92128";
		clientDemographics.setCorrespZip(correspZip);
		
		String correspPhone1 = "(619) 634-7519";
		clientDemographics.setCorrespPhone1(correspPhone1);		
		
		logger.info("*** Step 3 Actions: - Enter Shipping Addr1, Zip and Phone1");
		String shippingAddr1 = randomCharacter.getRandomAlphaString(6);
		clientDemographics.setShippingAddr1(shippingAddr1);
		
		String shippingZip = "92130";
		clientDemographics.setShippingZip(shippingZip);
		
		String shippingPhone1 = "(760) 537-7129";
		clientDemographics.setShippingPhone1(shippingPhone1);	
		
		logger.info("*** Step 3 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 4 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 4 Expected Results: - Verify that Client account info (Client Name, Account Type, Start Date, Primary Facility) display properly");
		Assert.assertTrue(clientDemographics.nameInput().getAttribute("value").trim().contains(clnAbbrev), "        Name: " + clnAbbrev + " should show.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.accountTypeDropDown(), clnAccountTyp), "        Account Type: " + clnAccountTyp + " should be selected.");
		Assert.assertTrue(isDropdownItemSelected(clientDemographics.primaryFacilityDropDown(), primaryFac), "        Primary Facility: " + primaryFac + " should be selected.");
		Assert.assertTrue(clientDemographics.startDateInput().getAttribute("value").trim().contains(currDt), "        Start Date: " + currDt + " should show.");
				
		logger.info("*** Step 5 Actions: - Click Add Xref button");
		clientDemographics.addXrefBtn().click();
		
		logger.info("*** Step 5 Actions: - Enter Eff Date, select Xref Type, Member Abbrev and Member Description");
		clientDemographics.setEffDate(currDt);
		clientDemographics.expDateInput().click();
		clientDemographics.expDateInput().sendKeys(Keys.TAB);
		
		ArrayList<String> xRefTypList = daoManagerXifinRpm.getXrefTypeFromXREFTYPByXrefCat("Client", testDb);
		String xRefTyp = xRefTypList.get(3).trim();				
		clientDemographics.setXref(xRefTyp);
		
		ArrayList<String> xRefList = daoManagerXifinRpm.getXrefFromXREFByXrefTyp(xRefTyp, testDb);
		String xRefAbbrev = xRefList.get(4).trim();
		String xRefDescr = xRefList.get(2).trim();
		String xRef = xRefAbbrev + " - " + xRefDescr;
		clientDemographics.xRefInput().click();
		selectItem(clientDemographics.xRefMemberDropDown(), xRef);	
		clientDemographics.setXref(xRef);
				
		logger.info("*** Step 6 Actions: - Click Submit button");
		clientDemographics.submitBtn().click();			
		
		logger.info("*** Step 7 Actions: - Reload the same Client ID in Client ID field and tab out");
		clientDemographics.setClnId(clnAbbrev);
		
		logger.info("*** Step 7 Expected Results: - Verify that Xref info (Eff Date, Xref Type, Member Abbrev and Member Description) display properly");
		Assert.assertTrue(clientDemographics.effDateInput().getAttribute("value").trim().contains(currDt), "        Eff Date: " + currDt + " should show.");
		Assert.assertTrue(clientDemographics.xRefTypeInput().getAttribute("value").trim().contains(xRefTyp), "        Xref Type: " + xRefTyp + " should show.");
		Assert.assertTrue(clientDemographics.xRefMemberAbbrevInput().getAttribute("value").trim().contains(xRefAbbrev), "        Xref Member Abbrev: " + xRefAbbrev + " should show.");
		Assert.assertTrue(clientDemographics.xRefMemberDescrInput().getAttribute("value").trim().contains(xRefDescr), "        Xref Member Description: " + xRefDescr + " should show.");
				
		logger.info("*** Step 5 Actions: - Click Reset button");
		clientDemographics.resetBtn().click();
	
		driver.close();	
	}
	
}
