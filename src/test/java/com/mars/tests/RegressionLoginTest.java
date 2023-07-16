package com.mars.tests;

/*
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
*/

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
/*
import com.overall.accession.accessionNavigation.AccessionNavigation;
import com.overall.accession.accessionProcessing.AccessionDemographics;
import com.overall.accession.accessionProcessing.AccessionSearch;
import com.overall.accession.accessionProcessing.AccessionSingleStatement;
import com.overall.accession.accessionProcessing.AccessionTransactionDetail;
import com.overall.client.clientInquiry.PriceInquiry;
import com.overall.client.clientProcessing.AuditLog;
import com.overall.client.clientProcessing.ClientDemographics;
import com.overall.client.clientProcessing.EligCensusConfig;
import com.overall.client.clientProcessing.PayorExclusions;
import com.overall.client.clientProcessing.PhysicianAssignment;
import com.overall.client.clientNavigation.ClientNavigation;
import com.overall.fileMaintenance.orderProcessingConfig.FileMaintencePatternDefinition;
import com.overall.fileMaintenance.sysMgt.DataCacheConfiguration;
import com.overall.fileMaintenance.sysMgt.TaskScheduler;
import com.overall.fileMaintenance.sysMgt.TaskStatus;
import com.overall.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
*/
import com.overall.headerNavigation.HeaderNavigation;
/*
import com.overall.help.Help;
import com.overall.accession.orderProcessing.AccnTestUpdateOld;
import com.overall.payor.payorDemographics.DialysisFrequencyControl;
import com.overall.payor.payorDemographics.GroupDemographics;
import com.overall.payor.payorDemographics.PricingConfig;
import com.overall.payor.payorNavigation.PayorNavigation;
import com.overall.accession.accessionProcessing.superSearch.SuperSearch;
import com.overall.accession.accessionProcessing.superSearch.SuperSearchResults;
*/
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
/*
import com.xifin.utils.Parser;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
*/

public class RegressionLoginTest extends SeleniumBaseTest  {
//	private Parser parser;
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
	private TimeStamp timeStamp;
	private ClientNavigation clientNavigation;
	private PayorExclusions payorExclusions;
	private AuditLog auditLog;
	private PhysicianAssignment physicianAssignment;
	private EligCensusConfig eligCensusConfig;
	private RandomCharacter randomCharacter;
	private ClientDemographics clientDemographics;
	private PriceInquiry priceInquiry;
	*/
	
	@Test(priority = 1, description = "RPM:Login - SSO Services widget display")
	@Parameters({"email", "password"})
	public void testRPM_729(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_729 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that SSO Services Widget displays");
		headerNavigation = new HeaderNavigation(driver, config);
		Assert.assertTrue(isElementPresent(headerNavigation.ssoServiceWidget(), 5), "        SSO Services Widget should display.");
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");		
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		driver.close();	
	}		

	@Test(priority = 1, description = "RPM:Login - SSO Services widget can be opened")
	@Parameters({"email", "password"})
	public void testRPM_750(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_750 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that SSO Services Widget displays");
		headerNavigation = new HeaderNavigation(driver, config);
		Assert.assertTrue(isElementPresent(headerNavigation.ssoServiceWidget(), 5), "        SSO Services Widget should display.");
		
		logger.info("*** Step 2 Actions: - Click SSO Services Link");
		headerNavigation.ssoServiceWidget().click();
		
		logger.info("*** Step 2 Expected Results: - Verify that SSO Services Widget can be opened");
		Assert.assertTrue(headerNavigation.ssoServicesPopupText().getText().trim().contains("XIFIN Services"), "        SSO Services popup window should show.");
		
		logger.info("*** Step 3 Actions: - Click SSO Services Widget Close Button");
		headerNavigation.ssoServiceWidgetCloseBtn().click();
		
		logger.info("*** Step 3 Expected Results: - Verify that user is logged in");		
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 3 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		driver.close();	
	}	
	
}
