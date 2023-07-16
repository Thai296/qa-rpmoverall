package com.mars.tests;


import com.overall.headerNavigation.HeaderNavigation;
import com.overall.payor.payorDemographics.AuditDetail;
import com.overall.payor.payorDemographics.ContractConfig;
import com.overall.payor.payorDemographics.ContractSearch;
import com.overall.payor.payorDemographics.ContractSearchResults;
import com.overall.payor.payorDemographics.DialysisFrequencyControl;
import com.overall.payor.payorDemographics.GroupDemographics;
import com.overall.payor.payorDemographics.PayorDemographics;
import com.overall.payor.payorDemographics.PricingConfig;
import com.overall.payor.payorNavigation.PayorNavigation;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

//import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
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
/*
import com.overall.help.Help;
import com.overall.accession.orderProcessing.AccnTestUpdateOld;
import com.overall.payment.paymentNavigation.PaymentNavigation;
import com.overall.payment.paymentPayments.DepositSearch;
import com.overall.payment.paymentPayments.DepositSearchResults;
import com.overall.payment.paymentPayments.Deposits;
*/
//import com.overall.accession.accessionProcessing.superSearch.SuperSearch;
//import com.overall.accession.accessionProcessing.superSearch.SuperSearchResults;
//import com.xifin.utils.TestDataSetup;



public class RegressionPayorDemographicsTest extends SeleniumBaseTest  {
	
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
	*/
	private PayorNavigation payorNavigation;
	private GroupDemographics groupDemographics;
	private PricingConfig pricingConfig;
	/*
	private DataCacheConfiguration dataCacheConfiguration;
	private TaskScheduler taskScheduler;
	private TaskStatus taskStatus;
	private FileMaintencePatternDefinition fileMaintencePatternDefinition;
	*/
	private TimeStamp timeStamp;
	private PayorDemographics payorDemographics;
	private RandomCharacter randomCharacter;
	private DialysisFrequencyControl dialysisFrequencyControl;
	private AuditDetail auditDetail; 
	private ContractConfig contractConfig;
	private ContractSearch contractSearch;
	private ContractSearchResults contractSearchResults;
	
	
	
	@Test(priority = 1, description = "RPM - Payor - Group Demographics - Group ID - Primary Service Id display")
	@Parameters({"email", "password", "groupId"})
	public void testRPM_50(String email, String password, String groupId) throws Exception {
    	logger.info("*** Testing - testRPM_50 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Payor page
		logger.info("*** Step 2 - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Group Demographics page
		logger.info("*** Step 3 - Navigate to Group Demographics");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToGroupDemographicsLink();
		switchToPopupWin();
		
		//Load the accn data
		logger.info("*** Step 4 - Load the groupId by entering the groupId in the groupId dropdown");
		groupDemographics = new GroupDemographics(driver);
		Assert.assertTrue(isElementPresent(groupDemographics.nameInput(),5));
		groupDemographics.setGropuId(groupId);
		
		//Compare the Patient Payor Group Id Primary Service Id dbList to the dropdown list of the Primary Service ID
		int cycleId = groupDemographics.setCycle();
		ArrayList<String> svcIdList = daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(3, 1, testDb);
		Assert.assertTrue(compareDropdownItems(svcIdList, groupDemographics.prmySrvcId(cycleId,4), false));
		//Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(3, 1, testDb), groupDemographics.prmySrvcId(groupDemographics.setCycle(),4), false));
    }
	
	@Test(priority = 1, description = "RPM - Payor - Group Demographics - Group ID - Non Primary Service Id display")
	@Parameters({"email", "password", "groupId"})
	public void testRPM_51(String email, String password, String groupId) throws Exception {
    	logger.info("*** Testing - testRPM_51 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Payor page
		logger.info("*** Step 2 - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Group Demographics page
		logger.info("*** Step 3 - Navigate to Group Demographics");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToGroupDemographicsLink();
		switchToPopupWin();
		
		//Load the accn data
		logger.info("*** Step 4 - Load the groupId by entering the groupId in the groupId dropdown");
		groupDemographics = new GroupDemographics(driver);
		Assert.assertTrue(isElementPresent(groupDemographics.nameInput(),5));
		groupDemographics.setGropuId(groupId);
		
		//Compare the Patient Payor Group Id Non Primary Service Id dbList to the dropdown list of the Primary Service ID
		int cycleId = groupDemographics.setCycle();
		ArrayList<String> svcIdList = daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(3, 1, testDb);
		Assert.assertTrue(compareDropdownItems(svcIdList, groupDemographics.prmySrvcId(cycleId,5), false));		
		//Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(3, 1, testDb), groupDemographics.prmySrvcId(groupDemographics.setCycle(),5), false));
    }
	
	@Test(priority = 1, description = "RPM - Payor - Group Demographics - Group ID Non Patient - Primary Service Id display")
	@Parameters({"email", "password", "groupId"})
	public void testRPM_52(String email, String password, String groupId) throws Exception {
    	logger.info("*** Testing - testRPM_52 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Payor page
		logger.info("*** Step 2 - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Group Demographics page
		logger.info("*** Step 3 - Navigate to Group Demographics");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToGroupDemographicsLink();
		switchToPopupWin();
		
		//Load the accn data
		logger.info("*** Step 4 - Load the groupId by entering the groupId in the groupId dropdown");
		groupDemographics = new GroupDemographics(driver);
		Assert.assertTrue(isElementPresent(groupDemographics.nameInput(),5));
		groupDemographics.setGropuId(groupId);
		
		//Compare the Non Patient Payor Group Id Primary Service Id dbList to the dropdown list of the Primary Service ID
		int cycleId = groupDemographics.setCycle();
		ArrayList<String> svcIdList = daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(2, 1, testDb);
		Assert.assertTrue(compareDropdownItems(svcIdList, groupDemographics.prmySrvcId(cycleId,4), false));
		//Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(2, 1, testDb), groupDemographics.prmySrvcId(groupDemographics.setCycle(),4), false));
    }
	
	@Test(priority = 1, description = "RPM - Payor - Group Demographics - Group ID Non Patient - Non Primary Service Id display")
	@Parameters({"email", "password", "groupId"})
	public void testRPM_53(String email, String password, String groupId) throws Exception {
    	logger.info("*** Testing - testRPM_53 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Payor page
		logger.info("*** Step 2 - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Group Demographics page
		logger.info("*** Step 3 - Navigate to Group Demographics");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToGroupDemographicsLink();
		switchToPopupWin();
		
		//Load the accn data
		logger.info("*** Step 4 - Load the groupId by entering the groupId in the groupId dropdown");
		groupDemographics = new GroupDemographics(driver);
		Assert.assertTrue(isElementPresent(groupDemographics.nameInput(),5));
		groupDemographics.setGropuId(groupId);
		
		//Compare the Non Patient Payor Group Id Non Primary Service Id dbList to the dropdown list of the Primary Service ID
		int cycleId = groupDemographics.setCycle();
		ArrayList<String> svcIdList = daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(2, 1, testDb);
		Assert.assertTrue(compareDropdownItems(svcIdList, groupDemographics.prmySrvcId(cycleId,5), false));
		//Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(2, 1, testDb), groupDemographics.prmySrvcId(groupDemographics.setCycle(),5), false));
    }
	
	@Test(priority = 1, description = "RPM - Payor - Group Demographics - Group ID Non Patient - Add Procedure Code Override")
	@Parameters({"email", "password", "groupId"})
	public void testRPM_54(String email, String password, String groupId) throws Exception {
    	logger.info("*** Testing - testRPM_54 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Payor page
		logger.info("*** Step 2 - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Group Demographics page
		logger.info("*** Step 3 - Navigate to Group Demographics");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToGroupDemographicsLink();
		switchToPopupWin();
		
		//Load the accn data
		logger.info("*** Step 4 - Load the groupId by entering the groupId in the groupId dropdown");
		groupDemographics = new GroupDemographics(driver);
		Assert.assertTrue(isElementPresent(groupDemographics.nameInput(),5));
		groupDemographics.setGropuId(groupId);
		
		//Compare the Submission Service Id dbList for Non Patient Payor Group Id to the dropdown list of the Submission Service ID
		int rowId = groupDemographics.setProcCodeOverride();
		ArrayList<String> svcIdList = daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(2, 1, testDb);
		Assert.assertTrue(compareDropdownItems(svcIdList, groupDemographics.submissionSrvcIdDropdown(rowId,3), false));
		//Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(2, 1, testDb), groupDemographics.submissionSrvcIdDropdown(groupDemographics.setProcCodeOverride(),3), false));
    }
	
	@Test(priority = 1, description = "RPM - Payor - Group Demographics - Group ID Non Patient - Add Override Disabled")
	@Parameters({"email", "password", "groupId"})
	public void testRPM_55(String email, String password, String groupId) throws Exception {
    	logger.info("*** Testing - testRPM_55 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Payor page
		logger.info("*** Step 2 - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Group Demographics page
		logger.info("*** Step 3 - Navigate to Group Demographics");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToGroupDemographicsLink();
		switchToPopupWin();
		
		//Load the group Id data
		logger.info("*** Step 4 - Load the groupId by entering the groupId in the groupId dropdown");
		groupDemographics = new GroupDemographics(driver);
		Assert.assertTrue(isElementPresent(groupDemographics.nameInput(),5));
		groupDemographics.setGropuId(groupId);
		
		//Ensure the Add Override button is disabled
		Assert.assertFalse(groupDemographics.addOverrideBtn().isEnabled());
    }
	
	@Test(priority = 1, description = "RPM - Payor - Group Demographics - Group ID Patient - Manual Demand Format Services display")
	@Parameters({"email", "password", "groupId"})
	public void testRPM_56(String email, String password, String groupId) throws Exception {
    	logger.info("*** Testing - testRPM_56 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Payor page
		logger.info("*** Step 2 - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Group Demographics page
		logger.info("*** Step 3 - Navigate to Group Demographics");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToGroupDemographicsLink();
		switchToPopupWin();
		
		//Load the group Id data
		logger.info("*** Step 4 - Load the groupId by entering the groupId in the groupId dropdown");
		groupDemographics = new GroupDemographics(driver);
		Assert.assertTrue(isElementPresent(groupDemographics.nameInput(),5));
		groupDemographics.setGropuId(groupId);
		
		//Compare the Patient Payor Service Id dbList to the dropdown list of the Manual Demand Format Services
		Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getPayorAbbrvFromDataFmtTypByBPaperBSupported(1, 1, testDb), groupDemographics.manualDemandFormatDropdown(), true));
    }
	
	@Test(priority = 1, description = "RPM - Payor - Group Demographics - Group ID Non Patient - Manual Demand Format Services display")
	@Parameters({"email", "password", "groupId"})
	public void testRPM_57(String email, String password, String groupId) throws Exception {
    	logger.info("*** Testing - testRPM_57 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Payor page
		logger.info("*** Step 2 - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Group Demographics page
		logger.info("*** Step 3 - Navigate to Group Demographics");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToGroupDemographicsLink();
		switchToPopupWin();
		
		//Load the group Id data
		logger.info("*** Step 4 - Load the groupId by entering the groupId in the groupId dropdown");
		groupDemographics = new GroupDemographics(driver);
		Assert.assertTrue(isElementPresent(groupDemographics.nameInput(),5));
		groupDemographics.setGropuId(groupId);
		
		//Compare the Non Patient Payor Service Id dbList to the dropdown list of the Manual Demand Format Services
		Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getPayorAbbrvFromDataFmtTypByBPaperBSupported(1, 1, testDb), groupDemographics.manualDemandFormatDropdown(), true));
    }
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Id P - Submission Service Proc code Override")
	@Parameters({"email", "password", "payorId"})
	public void testRPM_58(String email, String password, String payorId) throws Exception {
    	logger.info("*** Testing - testRPM_58 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Payor page
		logger.info("*** Step 2 - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Pricing Config page
		logger.info("*** Step 3 - Navigate to Pricing Configuration");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		//Load the payor Id
		logger.info("*** Step 4 - Load the Payor Id by entering the payor Id in the Payor Id Input");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(isElementPresent(pricingConfig.payorIdInput(),5));
		pricingConfig.setPayorId(payorId);
		
		//Add Override and compare the Payor Service Id dbList to the dropdown list of the Submission Service Procedure Code Override Services
		logger.info("*** Step 5 - Click Add Override button");
		int count = pricingConfig.setSubSrvProcCodeOverride();
		ArrayList<String> svcIdList = daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(3, 1, testDb);
		Assert.assertTrue(compareDropdownItems(svcIdList, pricingConfig.submissionServiceDropdown(count), false));
		//Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(3, 1, testDb), pricingConfig.submissionServiceDropdown(pricingConfig.setSubSrvProcCodeOverride()), false));																										  
    }
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Id P - Dunning Cycle Primary Service")
	@Parameters({"email", "password", "payorId"})
	public void testRPM_59(String email, String password, String payorId) throws Exception {
    	logger.info("*** Testing - testRPM_59 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Payor page
		logger.info("*** Step 2 - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Pricing Config page
		logger.info("*** Step 3 - Navigate to Pricing Configuration");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		//Load the payor Id
		logger.info("*** Step 4 - Load the Payor Id by entering the payor Id in the Payor Id Input");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(isElementPresent(pricingConfig.payorIdInput(),5));
		pricingConfig.setPayorId(payorId);
		
		//Add Cycle and compare the Payor Primary Service Id dbList to the dropdown list of the Primary Service Id
		logger.info("*** Step 5 - Click Add Cycle button");
		int cycleId = pricingConfig.setCycle();
		ArrayList<String> svcIdList = daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(3, 1, testDb);
		Assert.assertTrue(compareDropdownItems(svcIdList, pricingConfig.prmySrvcIdDropdown(cycleId, 4), false));
		//Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(3, 1, testDb), pricingConfig.prmySrvcIdDropdown(pricingConfig.setCycle(), 4), false));																										  
    }
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Id P - Dunning Cycle Non Primary Service")
	@Parameters({"email", "password", "payorId"})
	public void testRPM_60(String email, String password, String payorId) throws Exception {
    	logger.info("*** Testing - testRPM_60 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Payor page
		logger.info("*** Step 2 - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Pricing Config page
		logger.info("*** Step 3 - Navigate to Pricing Configuration");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		//Load the payor Id
		logger.info("*** Step 4 - Load the Payor Id by entering the payor Id in the Payor Id Input");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(isElementPresent(pricingConfig.payorIdInput(),5));
		pricingConfig.setPayorId(payorId);
		
		//Add Cycle and compare the Payor Non Primary Service Id dbList to the dropdown list of the Non Primary Service Id
		logger.info("*** Step 5 - Click Add Cycle button");		
		int cycleId = pricingConfig.setCycle();
		ArrayList<String> svcIdList = daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(3, 1, testDb);
		Assert.assertTrue(compareDropdownItems(svcIdList, pricingConfig.prmySrvcIdDropdown(cycleId, 5), false));
		//Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(3, 1, testDb), pricingConfig.prmySrvcIdDropdown(pricingConfig.setCycle(), 5), false));																										  
    }
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Id P - Submission Provider Service")
	@Parameters({"email", "password", "payorId"})
	public void testRPM_61(String email, String password, String payorId) throws Exception {
    	logger.info("*** Testing - testRPM_61 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Payor page
		logger.info("*** Step 2 - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Pricing Config page
		logger.info("*** Step 3 - Navigate to Pricing Configuration");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		//Load the payor Id
		logger.info("*** Step 4 - Load the Payor Id by entering the payor Id in the Payor Id Input");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(isElementPresent(pricingConfig.payorIdInput(),5));
		pricingConfig.setPayorId(payorId);
		
		//Add Provider and compare the Provider Service Id dbList to the dropdown list of the Submission Service Id
		logger.info("*** Step 5 - Click Add Provider button");		
		int count = pricingConfig.setProvider();
		ArrayList<String> svcIdList = daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(3, 1, testDb);
		Assert.assertTrue(compareDropdownItems(svcIdList, pricingConfig.submSrvcProviderDropdown(count), false));
		//Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(3, 1, testDb), pricingConfig.submSrvcProviderDropdown(pricingConfig.setProvider()), false));																										  
    }
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Id P - Manual Demand Submission Service")
	@Parameters({"email", "password", "payorId"})
	public void testRPM_62(String email, String password, String payorId) throws Exception {
    	logger.info("*** Testing - testRPM_62 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Payor page
		logger.info("*** Step 2 - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Pricing Config page
		logger.info("*** Step 3 - Navigate to Pricing Configuration");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		//Load the payor Id
		logger.info("*** Step 4 - Load the Payor Id by entering the payor Id in the Payor Id Input");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(isElementPresent(pricingConfig.payorIdInput(),5));
		pricingConfig.setPayorId(payorId);
		
		//Compare the Submission Service Id dbList to the dropdown list of the Manual Demand Submission Service Id
		ArrayList<String> svcIdList = daoManagerXifinRpm.getPayorAbbrvFromSubmSvcByTypTypIdBSupportedBPaperBSupported(3, 1, 1, testDb);
		Assert.assertTrue(compareDropdownItems(svcIdList, pricingConfig.manualSubmSrvcDropdown(), false));	
		//Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getPayorAbbrvFromSubmSvcByTypTypIdBSupportedBPaperBSupported(3, 1, 1, testDb), pricingConfig.manualSubmSrvcDropdown(), false));																										  
    }
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Id Non Patient Client - Manual Demand Submission Service")
	@Parameters({"email", "password", "payorId"})
	public void testRPM_63(String email, String password, String payorId) throws Exception {
    	logger.info("*** Testing - testRPM_63 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Payor page
		logger.info("*** Step 2 - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Pricing Config page
		logger.info("*** Step 3 - Navigate to Pricing Configuration");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		//Load the payor Id
		logger.info("*** Step 4 - Load the Payor Id by entering the payor Id in the Payor Id Input");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(isElementPresent(pricingConfig.payorIdInput(),5));
		
		payorId = daoManagerAccnWS.getNonPatientNonclientPayorAbbrevFromPyr(testDb);
		pricingConfig.setPayorId(payorId);
		
		//Compare the Submission Service Id dbList to the dropdown list of the Manual Demand Submission Service Non Patient Non Client Id
		ArrayList<String> svcIdList = daoManagerXifinRpm.getPayorAbbrvFromSubmSvcByTypTypIdBSupportedBPaperBSupported(2, 1, 1, testDb);
		Assert.assertTrue(compareDropdownItems(svcIdList, pricingConfig.manualSubmSrvcDropdown(), false));
		//Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getPayorAbbrvFromSubmSvcByTypTypIdBSupportedBPaperBSupported(2, 1, 1, testDb), pricingConfig.manualSubmSrvcDropdown(), false));																										  
    }
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Id Non Patient Client - Submission Provider Service")
	@Parameters({"email", "password", "payorId"})
	public void testRPM_64(String email, String password, String payorId) throws Exception {
    	logger.info("*** Testing - testRPM_64 ***");
    	
		//login
		logger.info("*** Step 1 - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		//verify that user is logged in
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)));
		
		//Verify that customer is correct
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
		
		//Go to Payor page
		logger.info("*** Step 2 - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Pricing Config page
		logger.info("*** Step 3 - Navigate to Pricing Configuration");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		//Load the payor Id
		logger.info("*** Step 4 - Load the Payor Id by entering the payor Id in the Payor Id Input");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(isElementPresent(pricingConfig.payorIdInput(),5));
		
		payorId = daoManagerAccnWS.getNonPatientNonclientPayorAbbrevFromPyr(testDb);
		pricingConfig.setPayorId(payorId);
		
		//Add Provider and compare the Provider Service Id dbList to the dropdown list of the Submission Service Id
		logger.info("*** Step 5 - Click Add Provider button");
		int count = pricingConfig.setProvider();
		ArrayList<String> svcIdList = daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(2, 1, testDb);
		Assert.assertTrue(compareDropdownItems(svcIdList, pricingConfig.submSrvcProviderDropdown(count), false));
		//Assert.assertTrue(compareDropdownItems(daoManagerXifinRpm.getPayorAbbrvFromSubmSvcDocTypByDocTypTypIdBSupported(2, 1, testDb), pricingConfig.submSrvcProviderDropdown(pricingConfig.setProvider()), false));																										  
    }
	
	@Test(priority = 1, description = "RPM - Payor - Payor Demographics JSP - Add1 and Zip not required for Patient group")
	@Parameters({"email", "password"})
	public void testRPM_418(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_418 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Payor Demographics page		
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPyrDemoLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Demographics page shows");
		payorDemographics = new PayorDemographics(driver);
		Assert.assertTrue(payorDemographics.pyrIDInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load a Patient Payor ID in the Payor Id field and tab out");
		//"4" = Patient payor group
		String ptPyrAbbrev = daoManagerPlatform.getPyrAbbrevFromPYRByPyrGrpId(4, testDb);
		payorDemographics.setPayrID(ptPyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor info loaded properly");				
		Assert.assertEquals(payorDemographics.pyrNameInput().getAttribute("value"), daoManagerPayorWS.getPayorNameByPayorId(ptPyrAbbrev, testDb));
		
		logger.info("*** Step 4 Actions: - Enter a random text in Contact 1 field and tab out");
		randomCharacter = new RandomCharacter();	
		String pyrContact1 = randomCharacter.getRandomAlphaString(6);
		payorDemographics.setPayrContact1(pyrContact1);
		
		logger.info("*** Step 4 Actions: - Clear the value in Payor Addr1 field");
		payorDemographics.pyrAddr1Input().clear();
		
		logger.info("*** Step 4 Actions: - Clear the value in Payor Zip field");
		payorDemographics.pyrZipInput().clear();
		
		logger.info("*** Step 4 Actions: - Click on the Submit button");
		payorDemographics.submitBtn().click();
		Thread.sleep(5000);
		
		logger.info("*** Step 4 Expected Results: - Verify that no error was returned and the new Contact value was saved into DB");
		ArrayList<String> pyrInfoList = daoManagerXifinRpm.getPyrContactInfoFromPyrByPyrAbbrev(ptPyrAbbrev, testDb);
		//Ensure that the pyrContact1 value was saved to DB
		Assert.assertEquals(pyrInfoList.get(3), pyrContact1) ;
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();	
	}
	
	@Test(priority = 1, description = "RPM - Payor - Payor Demographics - Remove Add1 and Zip for existing Patient group record")
	@Parameters({"email", "password"})
	public void testRPM_423(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_423 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Payor Demographics page		
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPyrDemoLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Demographics page shows");
		payorDemographics = new PayorDemographics(driver);
		Assert.assertTrue(payorDemographics.pyrIDInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load a Patient Payor ID in the Payor Id field and tab out");
		//"4" = Patient payor group
		String ptPyrAbbrev = daoManagerPlatform.getPyrAbbrevFromPYRByPyrGrpId(4, testDb);
		payorDemographics.setPayrID(ptPyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor info loaded properly");				
		Assert.assertEquals(payorDemographics.pyrNameInput().getAttribute("value"), daoManagerPayorWS.getPayorNameByPayorId(ptPyrAbbrev, testDb));
		
		logger.info("*** Step 4 Actions: - Enter a random value in Payor Addr1 field and tab out");
		randomCharacter = new RandomCharacter();	
		String pyrAddr1 = "Payor Addr1 " + randomCharacter.getRandomAlphaString(4);
		payorDemographics.setPayrAddr1(pyrAddr1);
		
		logger.info("*** Step 4 Actions: - Enter '92121' in Payor Zip field and tab out");
		payorDemographics.setPayrZip("92121");
		
		logger.info("*** Step 4 Actions: - Click on the Submit button");
		payorDemographics.submitBtn().click();
		Thread.sleep(5000);
		
		logger.info("*** Step 4 Expected Results: - Verify that the values were saved into DB");
		ArrayList<String> pyrInfoList = daoManagerXifinRpm.getPyrContactInfoFromPyrByPyrAbbrev(ptPyrAbbrev, testDb);
		//Ensure that the pyrAddr1 value was saved to DB
		Assert.assertEquals(pyrInfoList.get(5), pyrAddr1.toUpperCase()) ;
		//Ensure that the Zip 92121 was saved to DB
		Assert.assertEquals(pyrInfoList.get(10), "92121") ;
		
		logger.info("*** Step 5 Actions: - Reload the same payor and tab out");
		payorDemographics.setPayrID(ptPyrAbbrev);
		
		logger.info("*** Step 5 Expected Results: - Verify that the payor info loaded properly");				
		Assert.assertEquals(payorDemographics.pyrNameInput().getAttribute("value"), daoManagerPayorWS.getPayorNameByPayorId(ptPyrAbbrev, testDb));
		
		logger.info("*** Step 6 Actions: - Clear the value in Payor Addr1 field");
		payorDemographics.pyrAddr1Input().clear();
		
		logger.info("*** Step 6 Actions: - Clear the value in Payor Zip field");
		payorDemographics.pyrZipInput().clear();
		
		logger.info("*** Step 6 Actions: - Click on the Submit button");
		payorDemographics.submitBtn().click();
		Thread.sleep(5000);
		
		logger.info("*** Step 6 Expected Results: - Verify that the updates were saved in DB");
		pyrInfoList = daoManagerXifinRpm.getPyrContactInfoFromPyrByPyrAbbrev(ptPyrAbbrev, testDb);
		//Ensure that the pyrAddr1 value was saved to DB
		Assert.assertTrue(pyrInfoList.get(5).equals(""), "        Payor Addr1 should be cleared.");
		//Ensure that the Zip 92121 was saved to DB
		Assert.assertTrue(pyrInfoList.get(10).equals(""), "        Payor Zip should be cleared.");		
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();	
	}
	
	@Test(priority = 1, description = "RPM - Payor - Payor Demographics - Add1 and Zip not required for Client group")
	@Parameters({"email", "password"})
	public void testRPM_425(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_425 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Payor Demographics page		
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPyrDemoLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Demographics page shows");
		payorDemographics = new PayorDemographics(driver);
		Assert.assertTrue(payorDemographics.pyrIDInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load a Client Payor ID in the Payor Id field and tab out");
		//"3" = Client payor group
		String ptPyrAbbrev = daoManagerPlatform.getPyrAbbrevFromPYRByPyrGrpId(3, testDb);
		payorDemographics.setPayrID(ptPyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor info loaded properly");				
		Assert.assertEquals(payorDemographics.pyrNameInput().getAttribute("value"), daoManagerPayorWS.getPayorNameByPayorId(ptPyrAbbrev, testDb));
		
		logger.info("*** Step 4 Actions: - Enter a random text in Contact 1 field and tab out");
		randomCharacter = new RandomCharacter();	
		String pyrContact1 = randomCharacter.getRandomAlphaString(6);
		payorDemographics.setPayrContact1(pyrContact1);
		
		logger.info("*** Step 4 Actions: - Clear the value in Payor Addr1 field");
		payorDemographics.pyrAddr1Input().clear();
		
		logger.info("*** Step 4 Actions: - Clear the value in Payor Zip field");
		payorDemographics.pyrZipInput().clear();
		
		logger.info("*** Step 4 Actions: - Click on the Submit button");
		payorDemographics.submitBtn().click();
		Thread.sleep(5000);
		
		logger.info("*** Step 4 Expected Results: - Verify that no error was returned and the new Contact value was saved into DB");
		ArrayList<String> pyrInfoList = daoManagerXifinRpm.getPyrContactInfoFromPyrByPyrAbbrev(ptPyrAbbrev, testDb);
		//Ensure that the pyrContact1 value was saved to DB
		Assert.assertEquals(pyrInfoList.get(3), pyrContact1) ;
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();	
	}
	
	@Test(priority = 1, description = "RPM - Payor - Payor Demographics - Remove Add1 and Zip for existing Client group record")
	@Parameters({"email", "password"})
	public void testRPM_426(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_426 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Payor Demographics page		
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPyrDemoLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Demographics page shows");
		payorDemographics = new PayorDemographics(driver);
		Assert.assertTrue(payorDemographics.pyrIDInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load a Client Payor ID in the Payor Id field and tab out");
		//"3" = Client payor group
		String ptPyrAbbrev = daoManagerPlatform.getPyrAbbrevFromPYRByPyrGrpId(3, testDb);
		payorDemographics.setPayrID(ptPyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor info loaded properly");				
		Assert.assertEquals(payorDemographics.pyrNameInput().getAttribute("value"), daoManagerPayorWS.getPayorNameByPayorId(ptPyrAbbrev, testDb));
		
		logger.info("*** Step 4 Actions: - Enter a random value in Payor Addr1 field and tab out");
		randomCharacter = new RandomCharacter();	
		String pyrAddr1 = "Payor Addr1 " + randomCharacter.getRandomAlphaString(4);
		payorDemographics.setPayrAddr1(pyrAddr1);
		
		logger.info("*** Step 4 Actions: - Enter '92121' in Payor Zip field and tab out");
		payorDemographics.setPayrZip("92121");
		
		logger.info("*** Step 4 Actions: - Click on the Submit button");
		payorDemographics.submitBtn().click();
		Thread.sleep(5000);
		
		logger.info("*** Step 4 Expected Results: - Verify that the values were saved into DB");
		ArrayList<String> pyrInfoList = daoManagerXifinRpm.getPyrContactInfoFromPyrByPyrAbbrev(ptPyrAbbrev, testDb);
		//Ensure that the pyrAddr1 value was saved to DB
		Assert.assertEquals(pyrInfoList.get(5), pyrAddr1.toUpperCase()) ;
		//Ensure that the Zip 92121 was saved to DB
		Assert.assertEquals(pyrInfoList.get(10), "92121") ;
		
		logger.info("*** Step 5 Actions: - Reload the same payor and tab out");
		payorDemographics.setPayrID(ptPyrAbbrev);
		
		logger.info("*** Step 5 Expected Results: - Verify that the payor info loaded properly");				
		Assert.assertEquals(payorDemographics.pyrNameInput().getAttribute("value"), daoManagerPayorWS.getPayorNameByPayorId(ptPyrAbbrev, testDb));
		
		logger.info("*** Step 6 Actions: - Clear the value in Payor Addr1 field");
		payorDemographics.pyrAddr1Input().clear();
		
		logger.info("*** Step 6 Actions: - Clear the value in Payor Zip field");
		payorDemographics.pyrZipInput().clear();
		
		logger.info("*** Step 6 Actions: - Click on the Submit button");
		payorDemographics.submitBtn().click();
		Thread.sleep(5000);
		
		logger.info("*** Step 6 Expected Results: - Verify that the updates were saved in DB");
		pyrInfoList = daoManagerXifinRpm.getPyrContactInfoFromPyrByPyrAbbrev(ptPyrAbbrev, testDb);
		//Ensure that the pyrAddr1 value was saved to DB
		Assert.assertTrue(pyrInfoList.get(5).equals(""), "        Payor Addr1 should be cleared.");
		//Ensure that the Zip 92121 was saved to DB
		Assert.assertTrue(pyrInfoList.get(10).equals(""), "        Payor Zip should be cleared.");		
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();	
	}
	
	@Test(priority = 1, description = "RPM - Payor - Payor Demographics - Add1 and Zip required for non-Patient and non-Client group(s)")
	@Parameters({"email", "password"})
	public void testRPM_427(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_427 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Payor Demographics page		
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPyrDemoLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Demographics page shows");
		payorDemographics = new PayorDemographics(driver);
		Assert.assertTrue(payorDemographics.pyrIDInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load a 3rd party Payor ID (non-Client and non-Patient Payors) and tab out");
		//"1" = Medicare payor group
		String ptPyrAbbrev = daoManagerPlatform.getPyrAbbrevFromPYRByPyrGrpId(1, testDb);
		payorDemographics.setPayrID(ptPyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor info loaded properly");				
		Assert.assertEquals(payorDemographics.pyrNameInput().getAttribute("value"), daoManagerPayorWS.getPayorNameByPayorId(ptPyrAbbrev, testDb));
		
		logger.info("*** Step 4 Actions: - Enter a random text in Contact 1 field and tab out");
		randomCharacter = new RandomCharacter();	
		String pyrContact1 = randomCharacter.getRandomAlphaString(6);
		payorDemographics.setPayrContact1(pyrContact1);
		
		logger.info("*** Step 4 Actions: - Clear the value in Payor Addr1 field");
		payorDemographics.pyrAddr1Input().clear();
		
		logger.info("*** Step 4 Actions: - Clear the value in Payor Zip field");
		payorDemographics.pyrZipInput().clear();
		
		logger.info("*** Step 4 Actions: - Click on the Submit button");
		payorDemographics.submitBtn().click();
		Thread.sleep(5000);
		
		logger.info("*** Step 4 Expected Results: - Verify that error messages for requiring Addr1 and Zip show");
		Assert.assertTrue(payorDemographics.pyrDemoErrText(3, 2).getText().trim().contains("Payor Address 1 Required"), "        Payor Address 1 Required error should show.");
		Assert.assertTrue(payorDemographics.pyrDemoErrText(4, 2).getText().trim().contains("ZIP Code Required"), "        ZIP Code Required error should show.");
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();	
	}
	
	@Test(priority = 1, description = "RPM - Payor - Payor Demographics - New Record for Patient payor group does not require Addr1 and Zip")
	@Parameters({"email", "password"})
	public void testRPM_430(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_430 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Payor Demographics page		
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPyrDemoLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Demographics page shows");
		payorDemographics = new PayorDemographics(driver);
		Assert.assertTrue(payorDemographics.pyrIDInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Payor ID in the Payor ID field and tab out");
		randomCharacter = new RandomCharacter();	
		String newPyrabbrev = "TESTPYR" + randomCharacter.getRandomAlphaString(5);
		payorDemographics.setPayrID(newPyrabbrev);
		
		logger.info("*** Step 3 Actions: - Enter a Name in the Name field and tab out");
		String newPyrName = "TESTNAME" + randomCharacter.getRandomAlphaString(5);
		payorDemographics.setPayrName(newPyrName);
		
		logger.info("*** Step 3 Actions: - Select Patient from Group dropdown list");
		//"4" = "Patient"
		payorDemographics.setGroupName("4");
		
		logger.info("*** Step 3 Actions: - Enter a Eff Date (anyday before today) in the Eff Date field and tab out");
		//t-1 = yesterday
		payorDemographics.setEffDate("t-1");
		
		logger.info("*** Step 3 Actions: - Click on the Submit button");
		payorDemographics.submitBtn().click();
		Thread.sleep(5000);
		
		logger.info("*** Step 3 Expected Results: - Verify that the new record was saved into DB");
		ArrayList<String> pyrInfoList = daoManagerXifinRpm.getPyrContactInfoFromPyrByPyrAbbrev(newPyrabbrev, testDb);		
		Assert.assertTrue(pyrInfoList.get(0).equals(newPyrName), "        Payor Name should be saved.");		
		Assert.assertTrue(pyrInfoList.get(1).equals("Patient"), "        Payor Group Name should be saved.");	
		timeStamp = new TimeStamp();		
		Assert.assertTrue(pyrInfoList.get(2).equals(timeStamp.getPreviousDate("MM/dd/yyyy", 1)), "       Eff Date should be saved.");		
				
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();	
	}
	
	@Test(priority = 1, description = "RPM - Payor - Payor Demographics - New Record for Client payor group does not require Addr1 and Zip")
	@Parameters({"email", "password"})
	public void testRPM_433(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_433 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Payor Demographics page		
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPyrDemoLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Demographics page shows");
		payorDemographics = new PayorDemographics(driver);
		Assert.assertTrue(payorDemographics.pyrIDInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Enter a new Payor ID in the Payor ID field and tab out");
		randomCharacter = new RandomCharacter();	
		String newPyrabbrev = "TESTPYR" + randomCharacter.getRandomAlphaString(5);
		payorDemographics.setPayrID(newPyrabbrev);
		
		logger.info("*** Step 3 Actions: - Enter a Name in the Name field and tab out");
		String newPyrName = "TESTNAME" + randomCharacter.getRandomAlphaString(5);
		payorDemographics.setPayrName(newPyrName);
		
		logger.info("*** Step 3 Actions: - Select Client from Group dropdown list");
		//"3" = "Client"
		payorDemographics.setGroupName("3");
		
		logger.info("*** Step 3 Actions: - Enter a Eff Date (anyday before today) in the Eff Date field and tab out");
		//t-1 = yesterday
		payorDemographics.setEffDate("t-1");
		
		logger.info("*** Step 3 Actions: - Click on the Submit button");
		payorDemographics.submitBtn().click();
		Thread.sleep(5000);
		
		logger.info("*** Step 3 Expected Results: - Verify that the new record was saved into DB");
		ArrayList<String> pyrInfoList = daoManagerXifinRpm.getPyrContactInfoFromPyrByPyrAbbrev(newPyrabbrev, testDb);		
		Assert.assertTrue(pyrInfoList.get(0).equals(newPyrName), "        Payor Name should be saved.");		
		Assert.assertTrue(pyrInfoList.get(1).equals("Client"), "        Payor Group Name should be saved.");			                                            
		timeStamp = new TimeStamp();		
		Assert.assertTrue(pyrInfoList.get(2).equals(timeStamp.getPreviousDate("MM/dd/yyyy", 1)), "       Eff Date should be saved.");		
				
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();	
	}
	
	@Test(priority = 1, description = "RPM - Payor - Dialysis Frequency Control-Payors Groups grid scrollbar does not hide Delete column")
	@Parameters({"email", "password"})
	public void testRPM_437(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_437 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Go to Payor tab > Dialysis Frequency Control  jsp");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Dialysis Frequency Control page		
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToDialysisFreqControlLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Dialysis Frequency Control page shows");
		dialysisFrequencyControl = new DialysisFrequencyControl(driver);
		Assert.assertTrue(dialysisFrequencyControl.dialysisFreqControlIDInput().isDisplayed(), "       Dialysis Frequency Control ID Input field should show.");
		
		logger.info("*** Step 3 Actions: - Enter in a Dialysis Frequency Control ID and Tab Off");
		ArrayList<String> dialList = daoManagerXifinRpm.getAbbrevDescrFromDIALFREQCONTROL(testDb);
		//dialList.get(0) = Dial Freq Ctrl ID
		dialysisFrequencyControl.setDialysisFreqControlID(dialList.get(0));
	
		logger.info("*** Step 3 Expected Results: - Verify that the page populates with data");		
		//dialList.get(0) = Dial Freq Ctrl Description
		Assert.assertEquals(dialysisFrequencyControl.dialysisFreqControlDescInput().getAttribute("value"), dialList.get(1));	
		
		logger.info("*** Step 4 Actions: - Click on the 'Add Payor Group' button to add a row to the grid");
		dialysisFrequencyControl.clickAddPyrGrp();
		
		logger.info("*** Step 4 Expected Results: - Verify that the Delete column still visible");
		Assert.assertTrue(dialysisFrequencyControl.isPyrGrpDeleteColVisible(1, 5), "        The Payor Group grid Delete column should be visible.");
			
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();	
	}
	
	@Test(priority = 1, description = "RPM - Payor - Dialysis Frequency Control-Payors grid scrollbar does not hide Delete column")
	@Parameters({"email", "password"})
	public void testRPM_438(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_438 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Go to Payor tab > Dialysis Frequency Control  jsp");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Dialysis Frequency Control page		
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToDialysisFreqControlLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that the Dialysis Frequency Control page shows");
		dialysisFrequencyControl = new DialysisFrequencyControl(driver);
		Assert.assertTrue(dialysisFrequencyControl.dialysisFreqControlIDInput().isDisplayed(), "       Dialysis Frequency Control ID Input field should show.");
		
		logger.info("*** Step 3 Actions: - Enter in a Dialysis Frequency Control ID and Tab Off");
		ArrayList<String> dialList = daoManagerXifinRpm.getAbbrevDescrFromDIALFREQCONTROL(testDb);
		//dialList.get(0) = Dial Freq Ctrl ID
		dialysisFrequencyControl.setDialysisFreqControlID(dialList.get(0));
	
		logger.info("*** Step 3 Expected Results: - Verify that the page populates with data");		
		//dialList.get(0) = Dial Freq Ctrl Description
		Assert.assertEquals(dialysisFrequencyControl.dialysisFreqControlDescInput().getAttribute("value"), dialList.get(1));	
		
		logger.info("*** Step 4 Actions: - Click on the 'Add Payor' button to add a row to the grid");
		dialysisFrequencyControl.clickAddPyr();
		
		logger.info("*** Step 4 Expected Results: - Verify that the Delete column still visible");
		Assert.assertTrue(dialysisFrequencyControl.isPyrDeleteColVisible(1, 5), "        The Payor grid Delete column should be visible.");
			
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();	
	}
	
	@Test(priority = 1, description = "RPM - Payor - Payor Demographics - Do not allow double-click on Submit button")
	@Parameters({"email", "password"})
	public void testRPM_470(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_470 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Payor Demographics page		
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPyrDemoLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Demographics page shows");
		payorDemographics = new PayorDemographics(driver);
		Assert.assertTrue(payorDemographics.pyrIDInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 2 Expected Results: - Verify that the Submit button is disabled");
		Assert.assertFalse(payorDemographics.submitBtn().isEnabled(), "        The Submit button should be disabled.");
		
		logger.info("*** Step 2 Expected Results: - Verify that the Reset button is enabled");
		Assert.assertTrue(payorDemographics.resetBtn().isEnabled(), "        The Reset button should be enabled.");
				
		logger.info("*** Step 3 Actions: - Load an existing Payor in the Payor ID field and tab out");
		//"3" = Client payor group
		String pyrAbbrev = daoManagerPlatform.getPyrAbbrevFromPYRByPyrGrpId(3, testDb);
		payorDemographics.setPayrID(pyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor was loaded properly");				
		Assert.assertEquals(payorDemographics.pyrNameInput().getAttribute("value"), daoManagerPayorWS.getPayorNameByPayorId(pyrAbbrev, testDb));
		
		logger.info("*** Step 3 Expected Results: - Verify that the Submit button is enabled");
		Assert.assertTrue(payorDemographics.submitBtn().isEnabled(), "        The Submit button should be enabled.");
		
		logger.info("*** Step 3 Expected Results: - Verify that the Reset button is enabled");
		Assert.assertTrue(payorDemographics.resetBtn().isEnabled(), "        The Reset button should be enabled.");
		
		logger.info("*** Step 4 Actions: - Clear the Payor Contact1 and enter a new value and tab out and click the Submit button");
		randomCharacter = new RandomCharacter();	
		String pyrContact1 = randomCharacter.getRandomAlphaString(6);
		payorDemographics.pyrContact1Input().clear();
		payorDemographics.setPayrContact1(pyrContact1);
		payorDemographics.submitBtn().click();
		
		logger.info("*** Step 4 Actions: - Click the Submit button a second time");
		payorDemographics.submitBtn().click();
		
		logger.info("*** Step 4 Expected Results: - Verify that the screen is rendered and the fileds were empty");
		Assert.assertTrue(payorDemographics.pyrIDInput().getText().isEmpty(), "        Payor ID Input field should be cleared.");
		
		logger.info("*** Step 5 Actions: - Reload the same payor and tab out");
		payorDemographics.setPayrID(pyrAbbrev);
		
		logger.info("*** Step 5 Expected Results: - Verify that the payor was loaded properly");				
		Assert.assertEquals(payorDemographics.pyrNameInput().getAttribute("value"), daoManagerPayorWS.getPayorNameByPayorId(pyrAbbrev, testDb));
		
		logger.info("*** Step 5 Expected Results: - Verify that the updated Contact1 value was saved to DB");
		ArrayList<String> pyrInfoList = daoManagerXifinRpm.getPyrContactInfoFromPyrByPyrAbbrev(pyrAbbrev, testDb);
		//Ensure that the pyrContact1 value was saved to DB
		Assert.assertEquals(pyrInfoList.get(3), pyrContact1);
		
		logger.info("*** Step 5 Expected Results: - Verify that the updated Contact1 value displays in UI");
		Assert.assertEquals(payorDemographics.pyrContact1Input().getAttribute("value"), pyrContact1);
				
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();	
	}

	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Insurance Name")
	@Parameters({"email", "password", "pyrAbbrev"})
	public void testRPM_486(String email, String password, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_486 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
			
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load an existing Payor in the Payor ID field and tab out");
		pricingConfig.setPayorId(pyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor was loaded properly");	
		ArrayList<String> list = daoManagerPayorWS.getPyrPricingConfigInfoFromPyrByPyrAbbrv(pyrAbbrev, testDb);
		String payorAbbrv = list.get(0);
		String insName = list.get(1);
		Assert.assertEquals(pricingConfig.payorIdInput().getAttribute("value"), payorAbbrv);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Insurance Name");
		Assert.assertEquals(pricingConfig.insNameInput().getAttribute("value"), insName);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Group Name")
	@Parameters({"email", "password", "pyrAbbrev"})
	public void testRPM_487(String email, String password, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_487 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
			
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load an existing Payor in the Payor ID field and tab out");
		pricingConfig.setPayorId(pyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor was loaded properly");	
		ArrayList<String> list = daoManagerPayorWS.getPyrPricingConfigInfoFromPyrByPyrAbbrv(pyrAbbrev, testDb);
		String payorAbbrv = list.get(0);
		Assert.assertEquals(pricingConfig.payorIdInput().getAttribute("value"), payorAbbrv);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Group Name");
		String pyrGrpId = list.get(2);
		String grpName = daoManagerPayorWS.getPyrGrpNameFromPyrGrpByPyrGrpId(pyrGrpId, testDb);
		Assert.assertEquals(pricingConfig.grpNameInput().getAttribute("value"), grpName);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Reason Code")
	@Parameters({"email", "password", "pyrAbbrev"})
	public void testRPM_488(String email, String password, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_488 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
			
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load an existing Payor in the Payor ID field and tab out");
		pricingConfig.setPayorId(pyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor was loaded properly");	
		ArrayList<String> list = daoManagerPayorWS.getPyrPricingConfigInfoFromPyrByPyrAbbrv(pyrAbbrev, testDb);
		String payorAbbrv = list.get(0);
		Assert.assertEquals(pricingConfig.payorIdInput().getAttribute("value"), payorAbbrv);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Reason Code is correct");
		String dnlTblId = list.get(3);
		String reasonCd = daoManagerPayorWS.getReasonCdNameFromDnlByDnlTblId(dnlTblId, testDb);
		Assert.assertEquals(pricingConfig.reasonCodeInput().getAttribute("value"), reasonCd);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Referring Phys ID")
	@Parameters({"email", "password", "pyrAbbrev"})
	public void testRPM_489(String email, String password, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_489 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
			
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load an existing Payor in the Payor ID field and tab out");
		pricingConfig.setPayorId(pyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor was loaded properly");	
		ArrayList<String> list = daoManagerPayorWS.getPyrPricingConfigInfoFromPyrByPyrAbbrv(pyrAbbrev, testDb);
		String payorAbbrv = list.get(0);
		Assert.assertEquals(pricingConfig.payorIdInput().getAttribute("value"), payorAbbrv);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Referring Phys Id is correct");
		String physIdTypId = list.get(4);
		String refPhysId = daoManagerPayorWS.getPhysIdTypFromPhysIdTypByPhysIdTypId(physIdTypId, testDb);
		Assert.assertEquals(selectItem(pricingConfig.refPhysIdDropdown(), refPhysId), refPhysId);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor W/O Basis")
	@Parameters({"email", "password", "pyrAbbrev"})
	public void testRPM_490(String email, String password, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_490 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
			
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load an existing Payor in the Payor ID field and tab out");
		pricingConfig.setPayorId(pyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor was loaded properly");	
		ArrayList<String> list = daoManagerPayorWS.getPyrPricingConfigInfoFromPyrByPyrAbbrv(pyrAbbrev, testDb);
		String payorAbbrv = list.get(0);
		Assert.assertEquals(pricingConfig.payorIdInput().getAttribute("value"), payorAbbrv);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor W/O Basis is correct");
		String autoWoBasisTypId = list.get(5);
		String woBasisId = daoManagerPayorWS.getWoBasisFromAutoWoBasisTypByAutoWoBasisTypId(autoWoBasisTypId, testDb);
		Assert.assertEquals(selectItem(pricingConfig.woBasisDropdown(), woBasisId), woBasisId);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Min W/O Balance")
	@Parameters({"email", "password", "pyrAbbrev"})
	public void testRPM_491(String email, String password, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_491 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
			
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load an existing Payor in the Payor ID field and tab out");
		pricingConfig.setPayorId(pyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor was loaded properly");	
		ArrayList<String> list = daoManagerPayorWS.getPyrPricingConfigInfoFromPyrByPyrAbbrv(pyrAbbrev, testDb);
		String payorAbbrv = list.get(0);
		Assert.assertEquals(pricingConfig.payorIdInput().getAttribute("value"), payorAbbrv);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Min W/O Balance is correct");
		String minWoBal = list.get(6);
		double doubleMinWoBal = Double.parseDouble(minWoBal);
		Assert.assertEquals(pricingConfig.minWoBalInput().getAttribute("value"), "$" + String.format("%.2f", new BigDecimal(doubleMinWoBal)));
	}
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor W/O Age")
	@Parameters({"email", "password", "pyrAbbrev"})
	public void testRPM_492(String email, String password, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_492 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
			
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load an existing Payor in the Payor ID field and tab out");
		pricingConfig.setPayorId(pyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor was loaded properly");	
		ArrayList<String> list = daoManagerPayorWS.getPyrPricingConfigInfoFromPyrByPyrAbbrv(pyrAbbrev, testDb);
		String payorAbbrv = list.get(0);
		Assert.assertEquals(pricingConfig.payorIdInput().getAttribute("value"), payorAbbrv);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor W/O Age is correct");
		String woAge = list.get(7);
		Assert.assertEquals(pricingConfig.woAgeInput().getAttribute("value"), woAge);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Filing Time Limit")
	@Parameters({"email", "password", "pyrAbbrev"})
	public void testRPM_493(String email, String password, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_493 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
			
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load an existing Payor in the Payor ID field and tab out");
		pricingConfig.setPayorId(pyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor was loaded properly");	
		ArrayList<String> list = daoManagerPayorWS.getPyrPricingConfigInfoFromPyrByPyrAbbrv(pyrAbbrev, testDb);
		String payorAbbrv = list.get(0);
		Assert.assertEquals(pricingConfig.payorIdInput().getAttribute("value"), payorAbbrv);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Filing Time Limit is correct");
		String flTmLmt = list.get(8);
		Assert.assertEquals(pricingConfig.flTmLmtInput().getAttribute("value"), flTmLmt);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Refiling Time Limit")
	@Parameters({"email", "password", "pyrAbbrev"})
	public void testRPM_494(String email, String password, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_494 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
			
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load an existing Payor in the Payor ID field and tab out");
		pricingConfig.setPayorId(pyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor was loaded properly");	
		ArrayList<String> list = daoManagerPayorWS.getPyrPricingConfigInfoFromPyrByPyrAbbrv(pyrAbbrev, testDb);
		String payorAbbrv = list.get(0);
		Assert.assertEquals(pricingConfig.payorIdInput().getAttribute("value"), payorAbbrv);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Refiling Time Limit is correct");
		String reFlTmLmt = list.get(9);
		Assert.assertEquals(pricingConfig.reFlTmLmtInput().getAttribute("value"), reFlTmLmt);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Appeal Time Limit")
	@Parameters({"email", "password", "pyrAbbrev"})
	public void testRPM_495(String email, String password, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_495 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
			
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load an existing Payor in the Payor ID field and tab out");
		pricingConfig.setPayorId(pyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor was loaded properly");	
		ArrayList<String> list = daoManagerPayorWS.getPyrPricingConfigInfoFromPyrByPyrAbbrv(pyrAbbrev, testDb);
		String payorAbbrv = list.get(0);
		Assert.assertEquals(pricingConfig.payorIdInput().getAttribute("value"), payorAbbrv);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Appeal Time Limit is correct");
		String applTmLmt = list.get(10);
		Assert.assertEquals(pricingConfig.applTmLmtInput().getAttribute("value"), applTmLmt);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Dx Selection Method")
	@Parameters({"email", "password", "pyrAbbrev"})
	public void testRPM_496(String email, String password, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_496 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
			
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load an existing Payor in the Payor ID field and tab out");
		pricingConfig.setPayorId(pyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor was loaded properly");	
		ArrayList<String> list = daoManagerPayorWS.getPyrPricingConfigInfoFromPyrByPyrAbbrv(pyrAbbrev, testDb);
		String payorAbbrv = list.get(0);
		Assert.assertEquals(pricingConfig.payorIdInput().getAttribute("value"), payorAbbrv);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Dx Selection Method is correct");
		String diagSelMthdTypId = list.get(11);
		diagSelMthdTypId = daoManagerPayorWS.getDxSelMthdFromDiagSelMthdTypByDiagSelMthdTypId(diagSelMthdTypId, testDb);
		Assert.assertEquals(selectItem(pricingConfig.diagMethodDropdown(), diagSelMthdTypId), diagSelMthdTypId);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Dx Required Checkbox")
	@Parameters({"email", "password", "pyrAbbrev"})
	public void testRPM_497(String email, String password, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_497 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
			
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load an existing Payor in the Payor ID field and tab out");
		pricingConfig.setPayorId(pyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor was loaded properly");	
		ArrayList<String> list = daoManagerPayorWS.getPyrPricingConfigInfoFromPyrByPyrAbbrv(pyrAbbrev, testDb);
		String payorAbbrv = list.get(0);
		Assert.assertEquals(pricingConfig.payorIdInput().getAttribute("value"), payorAbbrv);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Dx Required Checkbox is checked");
		String diagReq = list.get(12);
		Assert.assertEquals(isChecked(pricingConfig.diagReqCheckbox()), pricingConfig.reqCheck(diagReq));
	}
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Payor Print Test Name U Listed Procs")
	@Parameters({"email", "password", "pyrAbbrev"})
	public void testRPM_498(String email, String password, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_498 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
			
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load an existing Payor in the Payor ID field and tab out");
		pricingConfig.setPayorId(pyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor was loaded properly");	
		ArrayList<String> list = daoManagerPayorWS.getPyrPricingConfigInfoFromPyrByPyrAbbrv(pyrAbbrev, testDb);
		String payorAbbrv = list.get(0);
		Assert.assertEquals(pricingConfig.payorIdInput().getAttribute("value"), payorAbbrv);
		
		logger.info("*** Step 3 Expected Results: - Verify that the Payor Print Test Name U Listed Procs is correct");
		String prntTestNmUnlstProcs = list.get(13);
		Assert.assertEquals(isChecked(pricingConfig.printTestNameOnUnlistedProcsCheckbox()), pricingConfig.reqCheck(prntTestNmUnlstProcs));
	}
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Reset")
	@Parameters({"email", "password", "pyrAbbrev"})
	public void testRPM_499(String email, String password, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_499 *****");    	
		
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
			
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load an existing Payor in the Payor ID field and tab out");
		pricingConfig.setPayorId(pyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor was loaded properly");	
		ArrayList<String> list = daoManagerPayorWS.getPyrPricingConfigInfoFromPyrByPyrAbbrv(pyrAbbrev, testDb);
		String payorAbbrv = list.get(0);
		Assert.assertEquals(pricingConfig.payorIdInput().getAttribute("value"), payorAbbrv);
		
		logger.info("*** Step 4 Actions: - Click Reset button");
		pricingConfig.resetPayor();
		
		logger.info("*** Step 4 Expected Results: - Verify that the payor data gets cleared");
		Assert.assertEquals(pricingConfig.payorIdInput().getAttribute("value"), "");
		
	}
	
	@Test(priority = 1, description = "RPM - Payor - Pricing Configuration - Run Audit")
	@Parameters({"email", "password", "pyrAbbrev"})
	public void testRPM_500(String email, String password, String pyrAbbrev) throws Exception {
    	logger.info("***** Testing - testRPM_500 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab > Payor Demographics");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
			
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToPricingConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		logger.info("*** Step 3 Actions: - Load an existing Payor in the Payor ID field and tab out");
		pricingConfig.setPayorId(pyrAbbrev);
		
		logger.info("*** Step 3 Expected Results: - Verify that the payor was loaded properly");	
		ArrayList<String> list = daoManagerPayorWS.getPyrPricingConfigInfoFromPyrByPyrAbbrv(pyrAbbrev, testDb);
		String payorAbbrv = list.get(0);
		Assert.assertEquals(pricingConfig.payorIdInput().getAttribute("value"), payorAbbrv);
		
		logger.info("*** Step 4 Actions: - Click Run Audit button");
		pricingConfig.runAudit();
		
		logger.info("*** Step 4 Expected Results: - Verify that the payor data gets cleared");
		auditDetail = new AuditDetail(driver);
		switchToPopupWin();
		Assert.assertEquals(auditDetail.titleText().getText(), "Audit Detail");
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Search Contract ID")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_508(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_508 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Click on the the Contract Search button");
		contractConfig.searchContract();
		contractSearch = new ContractSearch(driver);
		switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Search popup displays");
		Assert.assertTrue(contractSearch.adminContractInput().isDisplayed(), "        Contract Admin field should show");
		
		logger.info("*** Step 5 Actions: - Enter * in the Contract Admin input and click Search");
		contractSearch.searchContract(contractId);
		
		logger.info("*** Step 5 Expected Results: - Verify that the Contract Search Results page displays with records of contract IDs");
		contractSearchResults = new ContractSearchResults(driver);
		Assert.assertTrue(isElementPresent(contractSearchResults.contractIdText(), 5));
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Load Contract Id via Contract Search window")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_509(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_509 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Click on the the Contract Search button");
		contractConfig.searchContract();
		contractSearch = new ContractSearch(driver);
		switchToPopupWin();
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Search popup displays");
		Assert.assertTrue(contractSearch.adminContractInput().isDisplayed(), "        Contract Admin field should show");
		
		logger.info("*** Step 5 Actions: - Enter * in the Contract Admin input and click Search");
		contractSearch.searchContract(contractId);
		
		logger.info("*** Step 5 Expected Results: - Verify that the Contract Search Results page displays with records of contract IDs");
		contractSearchResults = new ContractSearchResults(driver);
		Assert.assertTrue(isElementPresent(contractSearchResults.contractIdText(), 5));
		
		logger.info("*** Step 6 Actions: - Select the first record Contract Id record");
		contractId = contractSearchResults.contractIdText().getText();
		contractSearchResults.selectContract();
		switchToWin(winHandler);

		logger.info("*** Step 6 Expected Results: - Verify that the contract Id gets input in the Contract Id configuration page");
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		contractId = contractConfig.contractIdInput().getAttribute("value");
		contractConfig.contractIdInput(contractId);
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Name")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_510(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_510 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Name is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String contractName = list.get(0);
		Assert.assertEquals(contractConfig.contractNameInput().getAttribute("value"), contractName);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract ID")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_511(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_511 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Coverage")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_512(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_512 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Coverage is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String contractCoverageId = list.get(1);
		contractCoverageId = daoManagerPayorWS.getCtrCovrgTypFromCtrCovrgTypByCtrCovrgId(contractCoverageId, testDb);
		Assert.assertTrue(isDropdownItemSelected(contractConfig.contractCoverageDropdown(), contractCoverageId));
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Eff Date")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_513(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_513 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Eff Date is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String effDt = list.get(2);
		Assert.assertEquals(contractConfig.effDtInput().getAttribute("value"), effDt);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Exp Date")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_514(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_514 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Exp Date is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String endDt = list.get(3);
		Assert.assertEquals(contractConfig.endDtInput().getAttribute("value"), endDt);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Review Date")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_515(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_515 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Review Date is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String reviewDt = list.get(4);
		Assert.assertEquals(contractConfig.reviewDtInput().getAttribute("value"), reviewDt);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Continue Pricing Exp Checkbox")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_516(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_516 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Continue Pricing Checkbox state is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String priceExpCheckboxSt = list.get(5);
		Assert.assertEquals(contractConfig.priceExpContractCheckbox().isSelected(), isValueTrue(priceExpCheckboxSt));
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Report Form")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_517(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_517 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that the Contract Report Form is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String rptForm = list.get(6);
		Assert.assertTrue(isDropdownItemSelected(contractConfig.reportFormDropdown(), rptForm));
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Admin Name")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_518(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_518 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Admin Name is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String adminName = list.get(7);
		Assert.assertEquals(contractConfig.adminNameInput().getAttribute("value"), adminName);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Admin Country")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_519(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_519 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Admin Country is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String adminCountry = list.get(8);
		adminCountry = daoManagerPayorWS.getCtCntryFromCntryBycntryId(adminCountry, testDb);
		Assert.assertTrue(isDropdownItemSelected(contractConfig.adminCountryInput(), adminCountry));
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Admin Addr1")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_520(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_520 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Admin Addr1 is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String adminAddr1 = list.get(10);
		Assert.assertEquals(contractConfig.adminAddr1Input().getAttribute("value"), adminAddr1);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Admin Addr2")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_521(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_521 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Admin Addr2 is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String adminAddr2 = list.get(11);
		Assert.assertEquals(contractConfig.adminAddr2Input().getAttribute("value"), adminAddr2);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Admin Zip")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_522(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_522 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Admin zip is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String adminZip = list.get(12);
		Assert.assertEquals(contractConfig.adminZipInput().getAttribute("value"), adminZip);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Admin City")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_523(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_523 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Admin city is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String adminCity = list.get(13);
		Assert.assertEquals(contractConfig.adminCityInput().getAttribute("value"), adminCity);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Admin State")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_524(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_524 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Admin city is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String adminState = list.get(14);
		Assert.assertTrue(isDropdownItemSelected(contractConfig.adminStDropdown(), adminState));
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Admin Contact")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_525(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_525 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Admin contact is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String adminContact = list.get(15);
		Assert.assertEquals(contractConfig.adminContactInput().getAttribute("value"), adminContact);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Admin Phone")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_526(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_526 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Admin contact is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String adminPhone = list.get(16);
		
		String adminPhoneUI = contractConfig.adminPhoneInput().getAttribute("value").replaceAll("[()-]", "");
		Assert.assertEquals(adminPhoneUI.replaceAll(" ", ""), adminPhone);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Admin Fax")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_527(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_527 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Admin fax is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String adminFax = list.get(17);
		String adminFaxUI = contractConfig.adminFaxInput().getAttribute("value").replaceAll("[()-]", "");
		Assert.assertEquals(adminFaxUI.replaceAll(" ", ""), adminFax);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Admin Email")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_528(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_528 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Admin fax is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String adminEmail = list.get(18);
		Assert.assertEquals(contractConfig.adminEmailInput().getAttribute("value"), adminEmail);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contract Admin Preferred Contact Method")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_529(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_529 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contract Preferred Contact Method is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String adminContactMthd = list.get(19);
		adminContactMthd = daoManagerPayorWS.getCtMthdFromCtMthdByCtMthdId(adminContactMthd, testDb);
		Assert.assertTrue(isDropdownItemSelected(contractConfig.adminContactMethodDropdown(), adminContactMthd));
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Payor Assignment Eff Date")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_530(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_530 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Payor Assignment Eff Date is correct");
		ArrayList<String> list = daoManagerPayorWS.getPayorContractInfoByContrctId(contractId, testDb);
		String pyrEffDt = list.get(2);
		Assert.assertEquals(contractConfig.payorAssigEffDtInput().getAttribute("value"), pyrEffDt);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Payor Assignment Payor ID")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_531(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_531 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Payor Assignment Payor ID is correct");
		String pyrId = daoManagerPayorWS.getPyrIdFromPyrDtByContrctId(contractId, testDb);
		pyrId = daoManagerPayorWS.getPyrIdFromPyrByPyrId(pyrId, testDb);
		Assert.assertEquals(contractConfig.payorAssigPayorIdInput().getAttribute("value"), pyrId);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Payor Assignment Name")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_532(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_532 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Payor Name is correct");
		String pyrId = daoManagerPayorWS.getPyrIdFromPyrDtByContrctId(contractId, testDb);
		String pyrName = daoManagerPayorWS.getPyrNameFromPyrByPyrId(pyrId, testDb);
		Assert.assertEquals(contractConfig.payorAssigPayorNameInput().getAttribute("value"), pyrName);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contact Detail UserID")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_533(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_533 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contact Detail userId is correct");
		ArrayList<String> list = daoManagerPayorWS.getPyrcontrctCntctInfoFromPyrContrctCntctBycontrctId(contractId, testDb);
		String userId = list.get(0);
		Assert.assertEquals(contractConfig.userIdInput().getAttribute("value"), userId);
		//Assert.assertTrue(isDropdownItemSelected(contractConfig.userIdInput(), userId));
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contact Detail Date")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_534(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_534 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contact Detail Date is correct");
		ArrayList<String> list = daoManagerPayorWS.getPyrcontrctCntctInfoFromPyrContrctCntctBycontrctId(contractId, testDb);
		String detailDt = list.get(1);
		Assert.assertEquals(contractConfig.contactDtInput().getAttribute("value"), detailDt);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contact Detail Info")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_535(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_535 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contact Detail Info is correct");
		ArrayList<String> list = daoManagerPayorWS.getPyrcontrctCntctInfoFromPyrContrctCntctBycontrctId(contractId, testDb);
		String detailInfo = list.get(2);
		Assert.assertEquals(contractConfig.contactInfoInput().getAttribute("value"), detailInfo);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contact Detail Follow Up Date")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_536(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_536 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contact Detail Follow Up Date is correct");
		ArrayList<String> list = daoManagerPayorWS.getPyrcontrctCntctInfoFromPyrContrctCntctBycontrctId(contractId, testDb);
		String detailFollowUpDt = list.get(3);
		Assert.assertEquals(contractConfig.followUpDtInput().getAttribute("value"), detailFollowUpDt);
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Contact Detail Follow Up Person")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_537(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_537 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 4 Expected Results: - Verify that Contact Detail Follow Up Person is correct");
		ArrayList<String> list = daoManagerPayorWS.getPyrcontrctCntctInfoFromPyrContrctCntctBycontrctId(contractId, testDb);
		String detailFollowUpPerson = list.get(4);
		Assert.assertTrue(isDropdownItemSelected(contractConfig.followUpPersonDropdown(), detailFollowUpPerson));
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Payor Reset")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_538(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_538 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 5 Actions: - Click the Reset Button");
		contractConfig.resetPayor();
		
		logger.info("*** Step 5 Expected Results: - Verify that the contract payor data gets cleared");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), "");
	}
	
	@Test(priority = 1, description = "RPM - Payor - Contract Config - Payor Run Audit")
	@Parameters({"email", "password", "contractId"})
	public void testRPM_539(String email, String password, String contractId) throws Exception {
    	logger.info("***** Testing - testRPM_539 *****");    	
		
    	logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
			
		logger.info("*** Step 2 Actions: - Navigate to Payor Tab");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		logger.info("*** Step 2 Expected Results: - Verify that Payor Pricing Configuration page shows");
		pricingConfig = new PricingConfig(driver, wait);
		Assert.assertTrue(pricingConfig.payorIdInput().isDisplayed(), "        Payor ID field should show.");
		
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		logger.info("*** Step 3 Actions: - Navigate to Contract Config page");
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToContractConfigLink();
		switchToPopupWin();
		
		logger.info("*** Step 3 Expected Results: - Verify that Contract Configuration page shows");
		contractConfig = new ContractConfig(driver);
		winHandler = driver.getWindowHandle();
		Assert.assertTrue(isElementPresent(contractConfig.contractIdInput(), 5), "        Contract ID field should show.");
		
		logger.info("*** Step 4 Actions: - Enter a contract Id in the Contract Id input and tab out");
		contractConfig.contractIdInput(contractId);
		
		logger.info("*** Step 4 Expected Results: - Verify that the Contract Id info gets loaded on the page");
		Assert.assertEquals(contractConfig.contractIdInput().getAttribute("value"), contractId);

		logger.info("*** Step 5 Actions: - Click Run Audit button");
		contractConfig.runAudit();
		
		logger.info("*** Step 5 Expected Results: - Verify the Audit Detail page displays");
		auditDetail = new AuditDetail(driver);
		switchToPopupWin();
		Assert.assertEquals(auditDetail.titleText().getText(), "Audit Detail");
	}
	
	@Test(priority = 1, description = "RPM - Payor - Group Demographics - Patient Group Add Dunning Cycle")
	@Parameters({"email", "password"})
	public void testRPM_505(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_505 *****");
    	
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
						
		logger.info("*** Step 2 Actions:- Navigate to Payor Tab > Group Demographics page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Group Demographics page	
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToGroupDemographicsLink();
		switchToPopupWin();	
		
		logger.info("*** Step 2 Expected Results:- Verify that it's on Group Demographics page");
		groupDemographics = new GroupDemographics(driver);
		Assert.assertTrue(isElementPresent(groupDemographics.nameInput(),5), "        Group ID drop-down list should show.");
		
		logger.info("*** Step 3 Actions: - Select Patient Group ID from the dropdown");
		//"4" = Patient group
		ArrayList<String> pyrGrpInfoList = daoManagerXifinRpm.getPyrGrpInfoFromPYRGRPByPyrGrpId("4", testDb);
		String pyrGrpName = pyrGrpInfoList.get(0);
		groupDemographics.setGropuId(pyrGrpName);
		
		logger.info("*** Step 3 Expected Results:- Verify that the Patient group was loaded properly");
		Assert.assertTrue(groupDemographics.nameInput().getAttribute("value").trim().contains(pyrGrpName), "        " + pyrGrpName + " Group Name should show.");
		
		logger.info("*** Step 4 Actions: - Add new Dunning Cycle and Click Submit Button");
		groupDemographics.addCycleBtn().click();
		
		String updatedCount = groupDemographics.cycleCountText().getText();
		int index = Integer.parseInt(updatedCount) - 1;
		groupDemographics.setCycleText(updatedCount, index);
		groupDemographics.setDaysText("88", index);			
		
		groupDemographics.clickSubmit();
		Assert.assertTrue(isElemAttrMatchFound(groupDemographics.groupIdDropDown(), "class", "help-bound", 40), "        The Submission should be done after 40 seconds.");
		
		logger.info("*** Step 4 Expected Results:- Verify that the newly added Dunning Cycle was saved in DB");
		//"4" = Patient group
		ArrayList<String> pyrGrpDunList = daoManagerXifinRpm.getPyrGrpDunFromPYRGRPDUNByPyrGrpIdSubmCnt("4", updatedCount, testDb);
		Assert.assertEquals(pyrGrpDunList.get(0), updatedCount);
		
		logger.info("*** Step 5 Actions: - Select Patient Group ID from the dropdown");		
		groupDemographics.setGropuId(pyrGrpName);
		
		logger.info("*** Step 5 Expected Results:- Verify that the Patient group was loaded properly");
		Assert.assertTrue(groupDemographics.nameInput().getAttribute("value").trim().contains(pyrGrpName), "        " + pyrGrpName + " Group Name should show.");
		
		logger.info("*** Step 5 Expected Results:- Verify that the newly added Dunning Cycle displays properly");
		Assert.assertEquals(groupDemographics.cycleIndexText(updatedCount).getText().trim(), updatedCount);
				
		groupDemographics.resetBtn().click();
		
		logger.info("*** Step 6 Actions: - Remove the last added Payor Group Dunning Cycle in DB");
		daoManagerXifinRpm.deletePyrGrpDunFromPYRGRPDUNByPyrGrpId("4", testDb);
		
		driver.close();
    }
	
	@Test(priority = 1, description = "RPM - Payor - Group Demographics - Delete Dunning Cycle")
	@Parameters({"email", "password"})
	public void testRPM_506(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_506 *****");
    	
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
						
		logger.info("*** Step 2 Actions:- Navigate to Payor Tab > Group Demographics page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Group Demographics page	
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToGroupDemographicsLink();
		switchToPopupWin();	
		
		logger.info("*** Step 2 Expected Results:- Verify that it's on Group Demographics page");
		groupDemographics = new GroupDemographics(driver);
		Assert.assertTrue(isElementPresent(groupDemographics.nameInput(),5), "        Group ID drop-down list should show.");
		
		logger.info("*** Step 3 Actions: - Select Patient Group ID from the dropdown");
		//"4" = Patient group
		ArrayList<String> pyrGrpInfoList = daoManagerXifinRpm.getPyrGrpInfoFromPYRGRPByPyrGrpId("4", testDb);
		String pyrGrpName = pyrGrpInfoList.get(0);
		groupDemographics.setGropuId(pyrGrpName);
		
		logger.info("*** Step 3 Expected Results:- Verify that the Patient group was loaded properly");
		Assert.assertTrue(groupDemographics.nameInput().getAttribute("value").trim().contains(pyrGrpName), "        " + pyrGrpName + " Group Name should show.");
		
		logger.info("*** Step 4 Actions: - Add new Dunning Cycle and Click Submit Button");
		groupDemographics.addCycleBtn().click();
		
		String updatedCount = groupDemographics.cycleCountText().getText();
		int index = Integer.parseInt(updatedCount) - 1;
		groupDemographics.setCycleText(updatedCount, index);
		groupDemographics.setDaysText("88", index);			
		
		groupDemographics.clickSubmit();
		
		Assert.assertTrue(isElemAttrMatchFound(groupDemographics.groupIdDropDown(), "class", "help-bound", 40), "        The Submission should be done after 40 seconds.");
		
		logger.info("*** Step 4 Expected Results:- Verify that the newly added Dunning Cycle was saved in DB");
		//"4" = Patient group
		ArrayList<String> pyrGrpDunList = daoManagerXifinRpm.getPyrGrpDunFromPYRGRPDUNByPyrGrpIdSubmCnt("4", updatedCount, testDb);
		Assert.assertEquals(pyrGrpDunList.get(0), updatedCount);
		
		logger.info("*** Step 5 Actions: - Select Patient Group ID from the dropdown");		
		groupDemographics.setGropuId(pyrGrpName);
		
		logger.info("*** Step 5 Expected Results:- Verify that the Patient group was loaded properly");
		Assert.assertTrue(groupDemographics.nameInput().getAttribute("value").trim().contains(pyrGrpName), "        " + pyrGrpName + " Group Name should show.");
		
		logger.info("*** Step 5 Expected Results:- Verify that the newly added Dunning Cycle displays properly");
		Assert.assertEquals(groupDemographics.cycleIndexText(updatedCount).getText().trim(), updatedCount);
						
		logger.info("*** Step 6 Actions: - Select the last Dunning Cycle and check Delete checkbox; Click Submit button");			
		groupDemographics.dunningCycleDeleteChkBox(String.valueOf(index));		
		groupDemographics.clickSubmit();
		
		Assert.assertTrue(isElemAttrMatchFound(groupDemographics.groupIdDropDown(), "class", "help-bound", 20), "        The Submission should be done after 20 seconds.");
		
		logger.info("*** Step 6 Expected Results:- Verify that the Dunning Cycle was deleted in DB");
		pyrGrpDunList = daoManagerXifinRpm.getPyrGrpDunFromPYRGRPDUNByPyrGrpIdSubmCnt("4", updatedCount, testDb);
		Assert.assertTrue(pyrGrpDunList.isEmpty(), "        The Dunning Cycle " + updatedCount + " should be deleted.");
		
		logger.info("*** Step 7 Actions: - Select Patient Group ID from the dropdown");		
		groupDemographics.setGropuId(pyrGrpName);
		
		logger.info("*** Step 7 Expected Results:- Verify that the Patient group was loaded properly");
		Assert.assertTrue(groupDemographics.nameInput().getAttribute("value").trim().contains(pyrGrpName), "        " + pyrGrpName + " Group Name should show.");
		
		logger.info("*** Step 7 Expected Results:- Verify that the total number of Dunning Cycle was updated in UI");
		updatedCount = groupDemographics.cycleCountText().getText();
		Assert.assertEquals(updatedCount, String.valueOf(index));		
		
		groupDemographics.resetBtn().click();
		driver.close();
    }
	
	@Test(priority = 1, description = "RPM - Payor - Group Demographics - Patient Payor Group Demographic Info display")
	@Parameters({"email", "password"})
	public void testRPM_507(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_507 *****");
    	
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
						
		logger.info("*** Step 2 Actions:- Navigate to Payor Tab > Group Demographics page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Group Demographics page	
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToGroupDemographicsLink();
		switchToPopupWin();	
		
		logger.info("*** Step 2 Expected Results:- Verify that it's on Group Demographics page");
		groupDemographics = new GroupDemographics(driver);
		Assert.assertTrue(isElementPresent(groupDemographics.nameInput(),5), "        Group ID drop-down list should show.");
		
		logger.info("*** Step 3 Actions: - Select Patient Group ID from the dropdown");
		//"4" = Patient group
		ArrayList<String> pyrGrpInfoList = daoManagerXifinRpm.getPyrGrpInfoFromPYRGRPByPyrGrpId("4", testDb);
		String pyrGrpName = pyrGrpInfoList.get(0);
		groupDemographics.setGropuId(pyrGrpName);
		
		logger.info("*** Step 3 Expected Results:- Verify that the Patient Group Demographic Info display properly");
		Assert.assertTrue(groupDemographics.nameInput().getAttribute("value").trim().contains(pyrGrpName), "        " + pyrGrpName + " Group Name should show.");
		
		String minWOBal = pyrGrpInfoList.get(1);
		String woAge = pyrGrpInfoList.get(3);
		String woBasis = pyrGrpInfoList.get(10);
		String bContracted = pyrGrpInfoList.get(11);
		String bOIG = pyrGrpInfoList.get(12);
		String bADL = pyrGrpInfoList.get(14);
		String note = pyrGrpInfoList.get(16);
		
		//Contracted
		Assert.assertTrue(groupDemographics.contractedCheckBoxText(), "        The Contracted checkbox should be disabled.");			
		Assert.assertEquals(groupDemographics.contractedCheckBox().isSelected(), isValueTrue(bContracted));
		//W/O Basis
		Assert.assertEquals(groupDemographics.woBasisText(), woBasis.trim());		
		//Min W/O Balance
		Assert.assertEquals(groupDemographics.minWOBalInput().getAttribute("value").trim(), minWOBal.trim());		
		//W/O Age
		Assert.assertEquals(groupDemographics.woAgeInput().getAttribute("value").trim(), woAge.trim());
		//Use OIG		
		Assert.assertEquals(groupDemographics.useOIGCheckBox().isSelected(), isValueTrue(bOIG));
		//Annual Disclosure Letter		
		Assert.assertEquals(groupDemographics.printADLCheckBox().isSelected(), isValueTrue(bADL));
		//Indigent Discount Note
		Assert.assertEquals(groupDemographics.noteInput().getText().trim(), note.trim());
		
		groupDemographics.resetBtn().click();
		driver.close();
    }
	
	@Test(priority = 1, description = "RPM - Payor - Group Demographics - Client Group Client Dunning Option")
	@Parameters({"email", "password"})
	public void testRPM_540(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_540 *****");
    	
		logger.info("*** Step 1 Actions: - Log into RPM with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);		
		
		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));
		
		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");		
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));
						
		logger.info("*** Step 2 Actions:- Navigate to Payor Tab > Group Demographics page");
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		headerNavigation.navigateToPayorTab();
		switchToPopupWin();
		
		//Go to main page
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		
		//Go to Group Demographics page	
		payorNavigation = new PayorNavigation(driver, config);
		payorNavigation.navigateToGroupDemographicsLink();
		switchToPopupWin();	
		
		logger.info("*** Step 2 Expected Results:- Verify that it's on Group Demographics page");
		groupDemographics = new GroupDemographics(driver);
		Assert.assertTrue(isElementPresent(groupDemographics.nameInput(),5), "        Group ID drop-down list should show.");
		
		logger.info("*** Step 3 Actions: - Select Client Group ID from the dropdown");
		//"3" = Client group
		ArrayList<String> pyrGrpInfoList = daoManagerXifinRpm.getPyrGrpInfoFromPYRGRPByPyrGrpId("3", testDb);
		String pyrGrpName = pyrGrpInfoList.get(0);
		groupDemographics.setGropuId(pyrGrpName);
		
		logger.info("*** Step 3 Expected Results:- Verify that the Client group was loaded properly");
		Assert.assertTrue(groupDemographics.nameInput().getAttribute("value").trim().contains(pyrGrpName), "        " + pyrGrpName + " Group Name should show.");
		
		logger.info("*** Step 4 Actions: - Enter a new Monthly Interest Rate in 90-119 Aging Bucket; Click Submit button");
		String mir = "1.23";
		groupDemographics.setMIR90(this, mir);
		groupDemographics.clickSubmit();
		
		logger.info("*** Step 4 Expected Results:- Verify that the Monthly Interest Rate was saved in DB");
		ArrayList<String> clnDunInfoList = daoManagerXifinRpm.getClnDunInfoFromCLNDUNByClnId("0", testDb);
		Assert.assertTrue(clnDunInfoList.get(8).trim().equals(mir), "        Monthly Interest Rate " + mir + " was not saved." );
		
		logger.info("*** Step 5 Actions: - Select Client Group ID from the dropdown");		
		groupDemographics.setGropuId(pyrGrpName);
		
		logger.info("*** Step 5 Expected Results:- Verify that the Client group was loaded properly");
		Assert.assertTrue(groupDemographics.nameInput().getAttribute("value").trim().contains(pyrGrpName), "        " + pyrGrpName + " Group Name should show.");
		
		logger.info("*** Step 5 Expected Results:- Verify that the Monthly Interest Rate displays properly");
		Assert.assertTrue(groupDemographics.mir90Input().getAttribute("value").trim().equals(mir), "        Monthly Interest Rate " + mir + " should diaplay.");
		
		logger.info("*** Step 6 Actions: - Reset Monthly Interest Rate in 90-119 Aging Bucket; Click Submit button");
		groupDemographics.setMIR90(this, "0");
		groupDemographics.clickSubmit();		
		
		driver.close();	
		
    }
	
}	
