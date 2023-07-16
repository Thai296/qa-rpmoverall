package com.newXp.tests;

import com.mbasys.mars.ejb.entity.accn.Accn;
import com.mbasys.mars.ejb.entity.accnPhys.AccnPhys;
import com.mbasys.mars.ejb.entity.accnTest.AccnTest;
import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.phys.Phys;
import com.mbasys.mars.persistance.AccnStatusMap;
import com.mbasys.mars.persistance.SystemSettingMap;
import com.overall.accession.orderProcessing.AccnTestUpdate;
import com.overall.menu.MenuNavigation;
import com.overall.utils.AccnTestUpdateUtils;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.*;
import com.xifin.xap.utils.XifinAdminUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

public class SmokeAccnTestUpdateTest extends SeleniumBaseTest {

	private TimeStamp timeStamp;
	private AccnTestUpdate accnTestUpdate;	
	private RandomCharacter randomCharacter;
	private MenuNavigation navigation;
	private AccnTestUpdateUtils accnTestUpdateUtils;	
	private XifinAdminUtils xifinAdminUtils;
	private TestDataSetup testDataSetup;
	private ConvertUtil convertUtil;	

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

	@Test(priority = 1, description = "Add a Profile test")
	@Parameters({"project", "testSuite", "testCase", "propLevel", "propName",  "wsUsername", "wsPassword", "testAbbrev"})
	public void testXPR_684(String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword, String testAbbrev) throws Exception {
		logger.info("===== Testing - testXPR_684 =====");
		accnTestUpdateUtils = new AccnTestUpdateUtils(driver, config, wait);
		accnTestUpdate = new AccnTestUpdate(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);
		
		logger.info("*** Step 1 Actions: - Log into Accn Test Update page with SSO username and password");				
		navigation.navigateToAccnTestUpdatePage();

		logger.info("*** Step 1 Expected Results: - Verify that the Load Accession page is displayed with correct page title");	
		Assert.assertTrue(isElementPresent(accnTestUpdate.loadPageTitle(), 10), "        The Accession - Test Update Load Accession page title should show.");
		Assert.assertEquals(accnTestUpdate.loadPageTitle().getText(),"Test Update", "        The Load Accession page title should be 'Accession - Test Update'");

		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession via Accession WS");
		testDataSetup = new TestDataSetup(driver);
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("AccnID: " + accnId);
    	
    	logger.info("*** Step 2 Expected Results: Verify that a new accession was created");
		Assert.assertNotNull(accnId);

		logger.info("*** Step 3 Actions: - Enter the newly created Accession ID in Load Accession screen and tab out");
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 3 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");		
		Assert.assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Accession - Test Update page title should be 'Accession - Test Update'");
		
		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
		Assert.assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, true), "        Save And Clear button should be enabled.");
		
		logger.info("*** Step 4 Actions: - Add a new Profile test and enter final reported date");
		Assert.assertTrue(isElementPresent(accnTestUpdate.addNewRowBtn(), 10), "        Add new row button should be displayed.");
		accnTestUpdate.clickAddNewRowBtn();
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.newTestIdInput(), 10), "        Test ID Input field be displayed.");
		accnTestUpdate.setTestId(testAbbrev);
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.newFinalReportDateInput(), 10), "        Final Report Date Input field be displayed.");
		String currDt = timeStamp.getCurrentDate();
		accnTestUpdate.setProfileFinalReportDate(currDt);

		logger.info("*** Step 5 Actions: - Click Save And Clear button");
		accnTestUpdate.clickSaveAndClearBtn();

		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));
		
		logger.info("*** Step 5 Expected Results: - Verify that the profile test is added to the accession properly in DB");
		List<AccnTest> accnTestList = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev);
		int accnTestSeqId = accnTestList.get(0).getAccnTestSeqId();
		int testId= accnTestList.get(0).getTestId();
		boolean isProf = accnTestList.get(0).getIsProf();
		int units = accnTestList.get(0).getUnits();

		Assert.assertTrue(isProf,  "        Test " + testAbbrev + " is a Profile.");
		Assert.assertEquals(units, 1, "        1 unit of Profile Test " + testAbbrev + " should be added.");
		
		List<AccnTest> accnProfTestInfoList = accessionDao.getAccnTestsByAccnIdAndProfId(accnId, testAbbrev);

		for (AccnTest strings : accnProfTestInfoList) {
			Assert.assertEquals(testId, strings.getProfId(), "        Prof Id for Test Component in Profile Test " + testAbbrev + " should be " + testId);
			Assert.assertEquals(accnTestSeqId, strings.getProfSeqId(), "        Prof Seq Id for Test Component in Profile Test " + testAbbrev + " should be " + accnTestSeqId);
		}
	}
	
	@Test(priority = 1, description = "Add Test with Accn_Sta_id is 21")
	public void testXPR_651() throws Exception {
		logger.info("===== Testing - testXPR_651 =====");     
		accnTestUpdateUtils = new AccnTestUpdateUtils(driver, config, wait);
		accnTestUpdate = new AccnTestUpdate(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);		
		randomCharacter = new RandomCharacter(driver);
		convertUtil = new ConvertUtil();
		
		logger.info("*** Step 1 Actions: - Log into Accn Test Update page with SSO username and password");				
		navigation.navigateToAccnTestUpdatePage();

		logger.info("*** Step 1 Expected Results: - Verify that the Load Accession page is displayed with correct page title");	
		Assert.assertTrue(isElementPresent(accnTestUpdate.loadPageTitle(), 10), "        The Accession - Test Update Load Accession page title should show.");
		Assert.assertEquals(accnTestUpdate.loadPageTitle().getText(),"Test Update", "        The Load Accession page title should be 'Accession - Test Update'");
	    		
		logger.info("*** Step 2 Actions: - Enter a Priced Accession ID in Load Accession screen and tab out");
		Accn accnList = accessionDao.getPricedThirdPartyAccnWOErrorFromAccnAndAccnPyr();
		String accnId = accnList.getAccnId();
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(accnList.getDos().toString());
		String dos = targetFormat.format(date1);
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 2 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");		
		Assert.assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Accession - Test Update page title should be 'Accession - Test Update'");
		
		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
		Assert.assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, true), "        Save And Clear button should be enabled.");
		
		logger.info("*** Step 3 Actions: - Add a single test, enter final reported date and manual price");
		String testAbbrev = testDao.getSingleTestFromTestByEffDt(dos).getTestAbbrev();
		String manualPrc = "89.00";
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.addNewRowBtn(), 10), "        Add new row button should be displayed.");
		accnTestUpdate.clickAddNewRowBtn();
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.newTestIdInput(), 10), "        Test ID Input field be displayed.");
		accnTestUpdate.setTestId(testAbbrev);
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.newFinalReportDateInput(), 10), "        Final Report Date Input field be displayed.");
		String currDt = timeStamp.getCurrentDate();
		accnTestUpdate.setFinalReportDate(currDt);		

		String mainFac = facilityDao.getFacByFacId(1).getAbbrv();
		accnTestUpdate.setValueInDropdown(accnTestUpdate.posDropdown(), mainFac);

		Assert.assertTrue(isElementPresent(accnTestUpdate.newTestInfoInput(19),5),"       Manual Price Input field should be displayed.");
		accnTestUpdate.setNewTestInfo(2,19,manualPrc);

		logger.info("*** Step 4 Actions: - Click Save And Clear button");
		accnTestUpdate.clickSaveAndClearBtn();
		
		logger.info("*** Step 5 Actions: - Reload the same Accession");		
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 5 Expected Result -  Verify that the message 'The Accession is currently posting' is displayed");
		Assert.assertTrue(isElementPresent(accnTestUpdate.returnedErrMsg(), 5),"        Error message is displayed.");
		Assert.assertTrue(accnTestUpdate.returnedErrMsg().getText().contains("The accession is currently posting"), "        Error message - 'The accession is currently posting' is displayed.");
		
		logger.info("*** Step 6 Action: Click Reset Button");
		Assert.assertTrue(isElementPresent(accnTestUpdate.resetBtn(), 5),"        Reset button should show.");
		accnTestUpdate.clickResetBtn();

		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
				
		logger.info("*** Step 7 Actions: - Reload the same Accession");
		navigation.navigateToAccnTestUpdatePage();
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);

		logger.info("*** Step 7 Expected Results: Verify the updated data is saved into DB and display in UI properly");
		List<AccnTest> testInfoFromAccnTest = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev);
		int testTestSeqId = testInfoFromAccnTest.get(0).getAccnTestSeqId();
		Assert.assertEquals(testInfoFromAccnTest.get(0).getManualPrcAsMoney().toString().trim(), manualPrc, "        Manual Price for Accession Test " + testAbbrev + " should be updated to " + manualPrc + " in DB.");
		String formattedManualPrc = convertUtil.formatDollarAmount(Float.valueOf(manualPrc)).replaceAll("[$]", "").trim();
		System.out.println("formattedManualPrc = " + formattedManualPrc);
		Assert.assertTrue(accnTestUpdate.manualPriceText(testTestSeqId, formattedManualPrc).isDisplayed(), "        Manual Price for Accession Test " + testAbbrev + " should be updated to " + manualPrc + " in UI.");
		accnTestUpdate.clickResetBtn();
		
		logger.info("*** Step 8 Actions: - Clear test data");
		accessionDao.deleteAccnTestErrByAccnIdTestAbbrev(accnId, testAbbrev);
		accessionDao.deleteAccnTestByAccnIdTestAbbrev(accnId, testAbbrev);
	}

	@Test(priority = 1, description = "Add Test with Accn_Sta_Id is 41 (Pmt Posting)")
	public void testXPR_653() throws Exception {
		logger.info("===== Testing - testXPR_653 =====");     
		accnTestUpdateUtils = new AccnTestUpdateUtils(driver, config, wait);
		accnTestUpdate = new AccnTestUpdate(driver, wait);
		navigation = new MenuNavigation(driver, config);
		
		logger.info("*** Step 1 Actions: - Log into Accn Test Update page with SSO username and password");				
		navigation.navigateToAccnTestUpdatePage();

		logger.info("*** Step 1 Expected Results: - Verify that the Load Accession page is displayed with correct page title");	
		Assert.assertTrue(isElementPresent(accnTestUpdate.loadPageTitle(), 10), "        The Accession - Test Update Load Accession page title should show.");
		Assert.assertEquals(accnTestUpdate.loadPageTitle().getText(),"Test Update", "        The Load Accession page title should be 'Accession - Test Update'");
	    		
		logger.info("*** Step 2 Actions: - Enter a Pmt Posting Status Accession ID in Load Accession screen and tab out");
		Accn accnList = accessionDao.getAccnByAccnStatus(41);
		String accnId = accnList.getAccnId();
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 2 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");		
		Assert.assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Accession - Test Update page title should be 'Accession - Test Update'");
		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
				
		logger.info("*** Step 2 Expected Results: -  Error message is shown and Save and Clear button is disabled");
		Assert.assertTrue(isElementPresent(accnTestUpdate.returnedErrMsg(), 5),"       Error Message should show.");
		Assert.assertEquals(accnTestUpdate.returnedErrMsg().getText(), "Current accession status prohibits any updating - the accession has already been priced.");
		Assert.assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, false), "        Save And Clear button should be disabled.");
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.resetBtn(), 5), "        Reset button should show.");
		accnTestUpdate.clickResetBtn();	
		
	}	

	@Test(priority = 1, description = "Verify Help files can be opened properly in Load and ATU screen")
	public void testXPR_677() throws Exception {
		logger.info("===== Testing - testXPR_677 =====");     
		accnTestUpdateUtils = new AccnTestUpdateUtils(driver, config, wait);
		accnTestUpdate = new AccnTestUpdate(driver, wait);
		navigation = new MenuNavigation(driver, config);

		logger.info("*** Step 1 Actions: - Log into Accn Test Update page with SSO username and password");				
		navigation.navigateToAccnTestUpdatePage();

		logger.info("*** Step 1 Expected Results: - Verify that the Load Accession page is displayed with correct page title");	
		Assert.assertTrue(isElementPresent(accnTestUpdate.loadPageTitle(), 10), "        The Accession - Test Update Load Accession page title should show.");
		Assert.assertEquals(accnTestUpdate.loadPageTitle().getText(),"Test Update", "        The Load Accession page title should be 'Accession - Test Update'");
	    		
		logger.info("*** Step 2 Actions: Click Help icon button");		
		Assert.assertTrue(isElementPresent(accnTestUpdate.helpIconLink("0"), 5), "        Help Icon On Load Accn Page should show.");
		accnTestUpdate.clickHelpIconLink("0");
		
		logger.info("*** Step 2 Expected Results: - Verify that proper Help file is opened");	
		String parentPopup = switchToPopupWin();
		String currentUrl = driver.getCurrentUrl();
		logger.info("currentUrl="+currentUrl);
		Assert.assertTrue(currentUrl.contains("p_accession_test_update_header.htm"), "        Help window should be opened.");
		driver.close();
		switchToParentWin(parentPopup);
		
		logger.info("*** Step 3 Actions: - Enter a Priced Accession ID in Load Accession screen and tab out");
		Accn accnList = accessionDao.getPricedThirdPartyAccnWOErrorFromAccnAndAccnPyr();
		String accnId = accnList.getAccnId();
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 3 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");		
		Assert.assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Accession - Test Update page title should be 'Accession - Test Update'");
		
		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
		Assert.assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, true), "        Save And Clear button should be enabled.");
		
		logger.info("*** Step 3 Expected Results: Verify that all Help files can be opened properly in Load and ATU screens.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.helpHeaderIconLink(), 5), "        Help Header Icon for Accn Test Update should show.");
		accnTestUpdate.clickHelpIconHeaderLink();
		switchToPopupWin();
		String url = driver.getCurrentUrl();
		Assert.assertTrue(url.contains("p_accession_test_update_header.htm"),"        Help Header window should be opened.");
		driver.close();
		switchToParentWin(parentPopup);
		Assert.assertTrue(isElementPresent(accnTestUpdate.helpSectionIconLink(), 5), "        Help Section Icon for Accn Test Update table should show.");
		accnTestUpdate.clickHelpIconSectionLink();
		switchToPopupWin();
		url = driver.getCurrentUrl();
		Assert.assertTrue(url.contains("p_accession_test_update_section.htm"),"        Help window should be opened.");
		driver.close();
		switchToParentWin(parentPopup);		
		Assert.assertTrue(isElementPresent(accnTestUpdate.pageHelpLink(), 5), "        Page Help Icon for Accn Test Update should show.");
		accnTestUpdate.clickPageHelpLink();
		switchToPopupWin();
		url = driver.getCurrentUrl();
		Assert.assertTrue(url.contains("p_accession_test_update.htm"),"        Help window should be opened.");
		driver.close();
		switchToParentWin(parentPopup);
		Assert.assertTrue(isElementPresent(accnTestUpdate.resetBtn(), 5), "        Reset button should show.");
		accnTestUpdate.clickResetBtn();			
	}

	@Test(priority = 1, description = "Check Client Billing rule for Accn_Sta_id is 11")
	public void testXPR_679() throws Exception {
		logger.info("===== Testing - testXPR_679 =====");     
		accnTestUpdateUtils = new AccnTestUpdateUtils(driver, config, wait);
		accnTestUpdate = new AccnTestUpdate(driver, wait);
		navigation = new MenuNavigation(driver, config);
		xifinAdminUtils = new XifinAdminUtils(driver, config);

		logger.info("*** Step 1 Actions: Turn on system_setting 1149 (Perform Client Billing Rules) and clear system data cache");
		updateSystemSetting(SystemSettingMap.SS_CLIENT_BILLING_RULE_LOGIC_SPLIT, "True", "1");
		xifinAdminUtils.clearDataCache();
		
		logger.info("*** Step 2 Actions: - Log into Accn Test Update page with SSO username and password");				
		navigation.navigateToAccnTestUpdatePage();

		navigation.navigateToAccnTestUpdatePage();
		logger.info("*** Step 2 Expected Results: - Verify that the Load Accession page is displayed with correct page title");	
		Assert.assertTrue(isElementPresent(accnTestUpdate.loadPageTitle(), 10), "        The Accession - Test Update Load Accession page title should show.");
		Assert.assertEquals(accnTestUpdate.loadPageTitle().getText(),"Test Update", "        The Load Accession page title should be 'Accession - Test Update'");
	    		
		logger.info("*** Step 3 Actions: - Enter a Final Reported Status Accession ID in Load Accession screen and tab out");
		Accn accnList = accessionDao.getRandomAccnFromAccnByAccnStatus("11");
		String accnId = accnList.getAccnId();
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 3 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");		
		Assert.assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Accession - Test Update page title should be 'Accession - Test Update'");
		
		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
		Assert.assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, true), "        Save And Clear button should be enabled.");
		
		logger.info("*** Step 4 Actions: Check 'Check Client Billing Rules' checkbox");
		selectCheckBox(accnTestUpdate.clnBillingRulesChkbox());
		Assert.assertTrue(isElementPresent(accnTestUpdate.clnBillingRulesChkbox(), 5), "        Check Client Billing Rules Checkbox should be displayed.");


		logger.info("*** Step 4 Expected Results: Verify that the 'Check Client Billing Rules' checkbox is checked");
		Assert.assertEquals(accnTestUpdate.clnBillingRulesChkbox().getAttribute("checked"), "true", "        'Check Client Billing Rules' checkbox should be checked.");

		logger.info("*** Step 5 Actions: Click Save and Clear button");
		accnTestUpdate.clickSaveAndClearBtn();
		
		logger.info("*** Step 5 Expected Result: Verify that the Accession is in the q_validate_accn with fk_validate_typ_id = 11 (Check Client Billing Rules) in DB");
		Assert.assertEquals(accessionDao.getQValidateAccn(accnId).getValidateTypId(), 11, "        Accession ID " + accnId + " should be in Q_VALIDATE_ACCN with FK_VALIDATE_TYP_ID = 11.");
		
		logger.info("*** Step 6 Actions: Clear test data and clear system data cache");
		accessionDao.deleteAccn(accnId);
		accessionDao.deleteQOeByAccnId(accnId);
		updateSystemSetting(SystemSettingMap.SS_CLIENT_BILLING_RULE_LOGIC_SPLIT, "False", "0");
		xifinAdminUtils.clearDataCache();
	}	
	
	@Test(priority = 1, description = "Update Accession with Travel Fee Calculation")
	@Parameters({"accnId"})
	public void testXPR_686(String accnId) throws Exception {
		logger.info("===== Testing - testXPR_686 =====");     
		accnTestUpdateUtils = new AccnTestUpdateUtils(driver, config, wait);
		accnTestUpdate = new AccnTestUpdate(driver, wait);
		navigation = new MenuNavigation(driver, config);
		randomCharacter = new RandomCharacter(driver);
		
		logger.info("*** Step 1 Actions: - Log into Accn Test Update page with SSO username and password");				
		navigation.navigateToAccnTestUpdatePage();

		logger.info("*** Step 1 Expected Results: - Verify that the Load Accession page is displayed with correct page title");	
		Assert.assertTrue(isElementPresent(accnTestUpdate.loadPageTitle(), 10), "        The Accession - Test Update Load Accession page title should show.");
		Assert.assertEquals(accnTestUpdate.loadPageTitle().getText(),"Test Update", "        The Load Accession page title should be 'Accession - Test Update'");

		logger.info("*** Step 2 Actions: - Enter an Accession in Load Accession screen and tab out");	
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 2 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");		
		Assert.assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Accession - Test Update page title should be 'Accession - Test Update'");
		
		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
		Assert.assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, true), "        Save And Clear button should be enabled.");
				
		logger.info("*** Step 3 Actions: - Update Travel Fee Calculation values and click Save and Clear button");
		String mileage = randomCharacter.getNonZeroRandomNumericString(2);
		String phlebotomyStops = randomCharacter.getNonZeroRandomNumericString(1);
		accnTestUpdate.setMileage(mileage);	
		accnTestUpdate.setPhlebotomyStops(phlebotomyStops) ;
		accnTestUpdate.setTotalPatients(phlebotomyStops);
		
		accnTestUpdate.clickSaveAndClearBtn();
		
		logger.info("*** Step 3 Expected Results: - Verify that the data are saved into DB properly");
		Accn accnInfoList = accessionDao.getAccn(accnId);
		Assert.assertEquals(Integer.toString((int)accnInfoList.getTripMiles()), mileage, "        Mileage should be updated to " + mileage + " for Accession ID " + accnId);
		Assert.assertEquals(accnInfoList.getTripStops(), Integer.parseInt(phlebotomyStops), "        Phlebotomy Stops should be updated to " + phlebotomyStops + " for Accession ID " + accnId);
		Assert.assertEquals(accnInfoList.getTripPtCount(), Integer.parseInt(phlebotomyStops), "        Total Patients should be updated to " + phlebotomyStops + " for Accession ID " + accnId);
		Assert.assertTrue(accnInfoList.getIsRoundTrip(), "        ACCN.B_ROUND_TRIP should be '1' and not being changed.");

		logger.info("*** Step 4 Actions: Clear test data");		
		accessionDao.deleteQOeByAccnId(accnId);
	}	
	
	@Test(priority = 1, description = "Update Test with valid data")
	@Parameters({"project", "testSuite", "testCase"})
	public void testXPR_681(String project, String testSuite, String testCase) throws Exception {
		logger.info("===== Testing - testXPR_681 =====");     
		accnTestUpdateUtils = new AccnTestUpdateUtils(driver, config, wait);
		accnTestUpdate = new AccnTestUpdate(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);		
		randomCharacter = new RandomCharacter(driver);
		convertUtil = new ConvertUtil();
		
		logger.info("*** Step 1 Actions: - Log into Accn Test Update page with SSO username and password");				
		navigation.navigateToAccnTestUpdatePage();

		logger.info("*** Step 1 Expected Results: - Verify that the Load Accession page is displayed with correct page title");	
		Assert.assertTrue(isElementPresent(accnTestUpdate.loadPageTitle(), 10), "        The Accession - Test Update Load Accession page title should show.");
		Assert.assertEquals(accnTestUpdate.loadPageTitle().getText(),"Test Update", "        The Load Accession page title should be 'Accession - Test Update'");

		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession via Accession WS");
		testDataSetup = new TestDataSetup(driver);
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("AccnID: " + accnId);
    	
    	logger.info("*** Step 2 Expected Results: Verify that a new accession was created");
		Assert.assertNotNull(accnId);

    	logger.info("*** Step 3 Actions: - Load the new Accession");
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 3 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");		
		Assert.assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Accession - Test Update page title should be 'Accession - Test Update'");
		
		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
		Assert.assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, true), "        Save And Clear button should be enabled.");
		
		logger.info("*** Step 4 Actions: - Add a new single test, enter final reported date");
		String currDtStr = timeStamp.getCurrentDate();
//		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//		Date currDt = new java.sql.Date(df.parse(currDtStr).getTime());
		String testAbbrev = testDao.getSingleTestFromTestByEffDt(currDtStr).getTestAbbrev();
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.addNewRowBtn(), 10), "        Add new row button should be displayed.");
		accnTestUpdate.clickAddNewRowBtn();
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.newTestIdInput(), 10), "        Test ID Input field be displayed.");
		accnTestUpdate.setTestId(testAbbrev);
		
		String mainFac = facilityDao.getFacByFacId(1).getAbbrv();
		accnTestUpdate.setValueInDropdown(accnTestUpdate.posDropdown(), mainFac);
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.newFinalReportDateInput(), 10), "        Final Report Date Input field be displayed.");
		accnTestUpdate.setFinalReportDate(currDtStr);
		
		logger.info("*** Step 5 Actions: - Click Save And Clear button");
		accnTestUpdate.clickSaveAndClearBtn();
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));

		logger.info("*** Step 6 Actions: - Reload the same Accession");		
		navigation.navigateToAccnTestUpdatePage();
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 6 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");		
		Assert.assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Accession - Test Update page title should be 'Accession - Test Update'");
		
		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
		Assert.assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, true), "        Save And Clear button should be enabled.");
		
		Cln newClnInfList = clientDao.getClnNotInClnDtWithZeroBillingAssgnReq();
		String newClnAbbrev = newClnInfList.getClnAbbrev();
		accnTestUpdate.setClnIdInput(newClnAbbrev);		
		
		logger.info("*** Step 7 Actions: Select the newly added test row");
		List<AccnTest> testInfoFromAccnTest = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev);
		int accnTestSeqId = testInfoFromAccnTest.get(0).getAccnTestSeqId();
		Assert.assertTrue(isElementPresent(accnTestUpdate.testId(accnTestSeqId), 5),"        Test Code ID: " + testAbbrev + " should show.");
		accnTestUpdate.clickTestId(accnTestSeqId);
		
		logger.info("*** Step 8 Actions: Update values for Client ID, Mod1, Mod2, Units, Lab Message, POS, Rendering Physician, Final Reported Date and Renal");
		String mod1 = "0A";
		String mod2 = "SW";
		String newUnits = "2";
		String manualPrice = "89";
		clientDao.getClnFromClnAndFacByClnId(clientDao.getClnByClnAbbrev(newClnAbbrev).getClnId());
		String newPOS = facilityDao.getFacWhereAbbrvNotEmpty().getAbbrv();
		String labMsg = "TNI";
		String labNotes = randomCharacter.getRandomAlphaString(8);
		Phys renderingPhysNPIUPINList = physicianDao.getRenderingPhysFromPhys();
		int renderingPhysSeqId = renderingPhysNPIUPINList.getSeqId();
		Long renderingPhysNPIUPIN;
		if(renderingPhysNPIUPINList.getNpiId()!=null)
			renderingPhysNPIUPIN = renderingPhysNPIUPINList.getNpiId();
		else
			renderingPhysNPIUPIN = Long.valueOf(renderingPhysNPIUPINList.getUpinId());

		accnTestUpdate.clickNewRenderingPhys();
		Assert.assertTrue(isElementPresent(accnTestUpdate.renderingPhysInputBox(), 5),"       Rendering Physician Search Icon button is displayed.");
		accnTestUpdate.setNPIInput(String.valueOf(renderingPhysNPIUPIN));

		String newFinalReportDt = timeStamp.getCurrentDate();
		String renal = "No";
		
		accnTestUpdate.updateTestForAllFields(accnTestSeqId, mod1, mod2, newUnits, manualPrice, labMsg , labNotes, newPOS, renderingPhysSeqId, newFinalReportDt, renal);

		logger.info("*** Step 9 Actions: - Click Save And Clear button");
		accnTestUpdate.clickSaveAndClearBtn();
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));

		logger.info("*** Step 9 Expected Results: - Verify that the updated values are saved into DB properly");
		List<String> testInfosList = daoManagerXifinRpm.getTestInfoFromAccnTestByAccnIdTestAbbrev(accnId, testAbbrev, testDb);
		Assert.assertEquals(testInfosList.get(1), mod1, "        Mod1 " + mod1 + " should be saved into DB.");
		Assert.assertEquals(testInfosList.get(2), mod2, "        Mod2 " + mod2 + " should be saved into DB.");
		Assert.assertEquals(testInfosList.get(3), newUnits, "        UNITS " + newUnits + " should be saved into DB.");
		Assert.assertEquals(testInfosList.get(4), manualPrice, "        PRICE " + manualPrice + " should be saved into DB.");
		Assert.assertEquals(testInfosList.get(5), labMsg,"       Lab Message " + labMsg + " should be saved.");
		Assert.assertEquals(testInfosList.get(7), newPOS, "        POS " + newPOS + " should be saved into DB.");
		Assert.assertEquals(testInfosList.get(9), newFinalReportDt, "        Final report date " + newFinalReportDt + " should be saved into DB.");
		Assert.assertEquals(testInfosList.get(11).toUpperCase(), labNotes, "        Lab Notes " + labNotes + " for Accession " + accnId + " Test " + testAbbrev + " should be saved in DB.");
//		Assert.assertEquals(testInfosList.get(12), "N", "        Renal flag No for Accession " + accnId + " Test " + testAbbrev + " should be saved in DB.");

		AccnPhys accnPhysInfoList = accessionDao.getAccnPhysByAccnIdAccnTestSeqId(accnId, accnTestSeqId);
		Assert.assertEquals(accnPhysInfoList.getPhysSeqId(), renderingPhysSeqId, "        Rendering Physician Seq Id should be " + renderingPhysSeqId + " for Accession " + accnId);
		Assert.assertEquals(accnPhysInfoList.getAccnPhysTypId(), 4, "        Physician Type should be 4 (Rendering Physician)" + " for Accession " + accnId + " Test " + testAbbrev);

		logger.info("*** Step 10 Actions: - Reload the same Accession");
		navigation.navigateToAccnTestUpdatePage();
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 10 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");
		Assert.assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Accession - Test Update page title should be 'Accession - Test Update'");
		
		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
		Assert.assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, true), "        Save And Clear button should be enabled.");
		
		logger.info("*** Step 10 Expected Results: - Verify that the updated values are showing properly in UI");
		Assert.assertTrue(isElementPresent(accnTestUpdate.testValues(accnTestSeqId, mod1), 5), "        Mod1 " + mod1 + " should show for Test " + testAbbrev);
		Assert.assertTrue(isElementPresent(accnTestUpdate.testValues(accnTestSeqId, mod2), 5), "        Mod2 " + mod2 + " should show for Test " + testAbbrev);
		Assert.assertTrue(isElementPresent(accnTestUpdate.testValues(accnTestSeqId, newUnits), 5), "        Units " + newUnits + " should show for Test " + testAbbrev);
		
		String formattedManualPrc = convertUtil.formatDollarAmount(Float.valueOf(manualPrice)).replaceAll("[$]", "").trim();
		Assert.assertTrue(isElementPresent(accnTestUpdate.testValues(accnTestSeqId, formattedManualPrc), 5), "        Manual Price " + formattedManualPrc + " should show for Test " + testAbbrev);

		Assert.assertTrue(isElementPresent(accnTestUpdate.testValues(accnTestSeqId, labMsg), 5), "        Lab Message " + labMsg + " should show for Test " + testAbbrev);
		Assert.assertTrue(isElementPresent(accnTestUpdate.testValues(accnTestSeqId, labNotes), 5), "        Lab Notes " + labNotes + " should show for Test " + testAbbrev);
		Assert.assertTrue(isElementPresent(accnTestUpdate.testValues(accnTestSeqId, newPOS), 5), "        POS " + newPOS + " should show for Test " + testAbbrev);
		
		Assert.assertTrue(accnTestUpdate.selectedRenderingPhys(accnTestSeqId).getAttribute("title").contains(String.valueOf(renderingPhysNPIUPIN)), "        Rendering Physician NPI/UPIN " + renderingPhysNPIUPIN + " should show for Test " + testAbbrev);
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.testValues(accnTestSeqId, newFinalReportDt), 5), "        Final Report Date " + newFinalReportDt + " should show for Test " + testAbbrev);
//		assertTrue(isElementPresent(accnTestUpdate.testValues(accnTestSeqId, "No"), 5), "        Renal flag No should show for Test " + testAbbrev);
		
		logger.info("*** Step 11 Actions: - Reset the Accession");
		accnTestUpdate.clickResetBtn();
	}
	
	@Test(priority = 1, description = "Delete a newly added valid test")
	public void testXPR_687() throws Exception {
		logger.info("===== Testing - testXPR_687 =====");
		accnTestUpdateUtils = new AccnTestUpdateUtils(driver, config, wait);
		accnTestUpdate = new AccnTestUpdate(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);	
		
		logger.info("*** Step 1 Actions: - Log into Accn Test Update page with SSO username and password");				
		navigation.navigateToAccnTestUpdatePage();

		logger.info("*** Step 1 Expected Results: - Verify that the Load Accession page is displayed with correct page title");	
		Assert.assertTrue(isElementPresent(accnTestUpdate.loadPageTitle(), 10), "        The Accession - Test Update Load Accession page title should show.");
		Assert.assertEquals(accnTestUpdate.loadPageTitle().getText(),"Test Update", "        The Load Accession page title should be 'Accession - Test Update'");
	    		
		logger.info("*** Step 2 Actions: - Enter a Priced Accession ID in Load Accession screen and tab out");
		Accn accnList = accessionDao.getPricedThirdPartyAccnWOErrorFromAccnAndAccnPyr();
		String accnId = accnList.getAccnId();
		DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(accnList.getDos().toString());
		String dos = targetFormat.format(date1);
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 2 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");		
		Assert.assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Accession - Test Update page title should be 'Accession - Test Update'");
		
		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
		Assert.assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, true), "        Save And Clear button should be enabled.");
		
		logger.info("*** Step 3 Actions: - Add a single test and enter final reported date");
		String testAbbrev = testDao.getSingleTestFromTestByEffDt(dos).getTestAbbrev();
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.addNewRowBtn(), 10), "        Add new row button should be displayed.");
		accnTestUpdate.clickAddNewRowBtn();
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.newTestIdInput(), 10), "        Test ID Input field be displayed.");
		accnTestUpdate.setTestId(testAbbrev);
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.newFinalReportDateInput(), 10), "        Final Report Date Input field be displayed.");
		String currDt = timeStamp.getCurrentDate();
		accnTestUpdate.setFinalReportDate(currDt);	
		
		logger.info("*** Step 4 Action: Check Delete checkbox for new Test");
		Assert.assertTrue(isElementPresent(accnTestUpdate.deleteChkbox(), 5),"        Delete box should show.");
		selectCheckBox(accnTestUpdate.deleteChkbox());		
		
		logger.info("*** Step 5 Actions: - Click Save And Clear button");
		accnTestUpdate.clickSaveAndClearBtn();

		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));

		logger.info("*** Step 5 Expected Results: Verify the deleted test is not saved into DB");
		List<AccnTest> testInfoFromAccnTest = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev);		
		Assert.assertTrue(testInfoFromAccnTest.isEmpty(), "        Test " + testAbbrev + " should not be added to the Accession " + accnId);

	}	
	
	@Test(priority = 1, description = "Perform Client Billing Split")
	@Parameters({"project", "testSuite", "testCase", "propLevel", "propName",  "wsUsername", "wsPassword"})
	public void testXPR_688(String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("===== Testing - testXPR_688 =====");     
		accnTestUpdateUtils = new AccnTestUpdateUtils(driver, config, wait);
		accnTestUpdate = new AccnTestUpdate(driver, wait);
		navigation = new MenuNavigation(driver, config);

		logger.info("*** Step 1 Actions: - Log into Accn Test Update page with SSO username and password");				
		navigation.navigateToAccnTestUpdatePage();

		logger.info("*** Step 1 Expected Results: - Verify that the Load Accession page is displayed with correct page title");	
		Assert.assertTrue(isElementPresent(accnTestUpdate.loadPageTitle(), 10), "        The Accession - Test Update Load Accession page title should show.");
		Assert.assertEquals(accnTestUpdate.loadPageTitle().getText(),"Test Update", "        The Load Accession page title should be 'Accession - Test Update'");

		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession via Accession WS");
		testDataSetup = new TestDataSetup(driver);
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("AccnID: " + accnId);
    	
    	logger.info("*** Step 2 Expected Results: Verify that a new accession was created");
		Assert.assertNotNull(accnId);

    	logger.info("*** Step 3 Actions: - Load the new Accession");
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 3 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");		
		Assert.assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Accession - Test Update page title should be 'Accession - Test Update'");
		
		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
		Assert.assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, true), "        Save And Clear button should be enabled.");
		
		logger.info("*** Step 4 Actions: Select a test row");		
		com.mbasys.mars.ejb.entity.test.Test testAbbrevList = testDao.getTestByAccnId(accnId);
		String testAbbrev = testAbbrevList.getTestAbbrev();
		List<AccnTest> testInfoFromAccnTest = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev);
		int accnTestSeqId = testInfoFromAccnTest.get(0).getAccnTestSeqId();
		Assert.assertTrue(isElementPresent(accnTestUpdate.testId(accnTestSeqId), 5),"        Test Code ID: " + testAbbrev + " should show.");
		accnTestUpdate.clickTestId(accnTestSeqId);	

		logger.info("*** Step 5 Actions: Check Cln Bill checkbox");	
		Assert.assertTrue(isElementPresent(accnTestUpdate.clnBillChkbox(accnTestSeqId), 5),"        Cln Bill checkbox should show.");		
		accnTestUpdate.checkClnBillChkbox(accnTestSeqId);

		Assert.assertTrue(isElementPresent(accnTestUpdate.labMsgInput(), 5),"       Lab Message Input is displayed.");
		accnTestUpdate.setLabMsg("TNI");
		
		logger.info("*** Step 6 Actions: - Click Save And Clear button");
		accnTestUpdate.clickSaveAndClearBtn();

		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
		
		logger.info("*** Step 6 Expected Results: - Verify that Accn_Test.B_Bill_Cln = 1 for the selected test");
		List<String> testInfosList = daoManagerXifinRpm.getTestInfoFromAccnTestByAccnIdTestAbbrev(accnId, testAbbrev, testDb);
		Assert.assertEquals(testInfosList.get(13), "1", "        Accn_Test.B_Bill_Cln should be 1 for Accession " + accnId + " Test " + testAbbrev);
	}

	@Test(priority = 1, description = "Add an invalid TestId")
	public void testXPR_690() throws Exception {
		logger.info("===== Testing - testXPR_690 =====");     
		accnTestUpdateUtils = new AccnTestUpdateUtils(driver, config, wait);
		accnTestUpdate = new AccnTestUpdate(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);	
		
		logger.info("*** Step 1 Actions: - Log into Accn Test Update page with SSO username and password");				
		navigation.navigateToAccnTestUpdatePage();

		logger.info("*** Step 1 Expected Results: - Verify that the Load Accession page is displayed with correct page title");	
		Assert.assertTrue(isElementPresent(accnTestUpdate.loadPageTitle(), 10), "        The Accession - Test Update Load Accession page title should show.");
		Assert.assertEquals(accnTestUpdate.loadPageTitle().getText(),"Test Update", "        The Load Accession page title should be 'Accession - Test Update'");
	    		
		logger.info("*** Step 2 Actions: - Enter a Final Reported Accession ID in Load Accession screen and tab out");
		Accn accnList = accessionDao.getRandomAccnFromAccnByAccnStatus("11");
		String accnId = accnList.getAccnId();
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 2 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");		
		Assert.assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Accession - Test Update page title should be 'Accession - Test Update'");
		
		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
		Assert.assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, true), "        Save And Clear button should be enabled.");
		
		logger.info("*** Step 3 Actions: - Add an invalid test");
		String testAbbrev = "INVALIDTEST";						
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.addNewRowBtn(), 10), "        Add new row button should be displayed.");
		accnTestUpdate.clickAddNewRowBtn();
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.newTestIdInput(), 10), "        Test ID Input field be displayed.");
		accnTestUpdate.setTestId(testAbbrev);	
		
		logger.info("*** Step 3 Expected Result - The Error Message is displayed");
		Assert.assertTrue(isElementPresent(accnTestUpdate.returnedErrMsg(),5),"        Error Message is displayed.");
		Assert.assertEquals(accnTestUpdate.returnedErrMsg().getText(), "Invalid Test Code ID " + testAbbrev ,"        The Error Message - Invalid Test Code ID " + testAbbrev + " is displayed.");
		
		logger.info("*** Step 4 Actions: - Click Save And Clear button");
		accnTestUpdate.clickSaveAndClearBtn();

		logger.info("*** Step 5 Expected Results: Verify the invalid test is not saved into DB");
		List<AccnTest> testInfoFromAccnTest = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev);		
		Assert.assertTrue(testInfoFromAccnTest.isEmpty(), "        Test " + testAbbrev + " should not be added to the Accession " + accnId);
	}	
	
	@Test(priority = 1, description = "Update Test rendering physician by search")
	@Parameters({"project", "testSuite", "testCase", "propLevel", "propName",  "wsUsername", "wsPassword"})
	public void testXPR_691(String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("===== Testing - testXPR_691 =====");     
		accnTestUpdateUtils = new AccnTestUpdateUtils(driver, config, wait);
		accnTestUpdate = new AccnTestUpdate(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);		
		randomCharacter = new RandomCharacter(driver);
		convertUtil = new ConvertUtil();

		logger.info("*** Step 1 Actions: - Log into Accn Test Update page with SSO username and password");				
		navigation.navigateToAccnTestUpdatePage();

		logger.info("*** Step 1 Expected Results: - Verify that the Load Accession page is displayed with correct page title");	
		Assert.assertTrue(isElementPresent(accnTestUpdate.loadPageTitle(), 10), "        The Accession - Test Update Load Accession page title should show.");
		Assert.assertEquals(accnTestUpdate.loadPageTitle().getText(),"Test Update", "        The Load Accession page title should be 'Accession - Test Update'");

		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession via Accession WS");
		testDataSetup = new TestDataSetup(driver);
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("AccnID: " + accnId);
    	
    	logger.info("*** Step 2 Expected Results: Verify that a new accession was created");
		Assert.assertNotNull(accnId);

    	logger.info("*** Step 3 Actions: - Load the new Accession");
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 3 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");		
		Assert.assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Accession - Test Update page title should be 'Accession - Test Update'");

		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
		Assert.assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, true), "        Save And Clear button should be enabled.");
		
		logger.info("*** Step 4 Actions: - Add a new single test, enter final reported date");
		String currDtStr = timeStamp.getCurrentDate();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date currDt = new java.sql.Date(df.parse(currDtStr).getTime());
		String testAbbrev = testDao.getSingleTestFromTestByEffDt(currDtStr).getTestAbbrev();
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.addNewRowBtn(), 10), "        Add new row button should be displayed.");
		accnTestUpdate.clickAddNewRowBtn();
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.newTestIdInput(), 10), "        Test ID Input field be displayed.");
		accnTestUpdate.setTestId(testAbbrev);
		
		String mainFac = facilityDao.getFacByFacId(1).getAbbrv();
		accnTestUpdate.setValueInDropdown(accnTestUpdate.posDropdown(), mainFac);
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.newFinalReportDateInput(), 10), "        Final Report Date Input field be displayed.");
		accnTestUpdate.setFinalReportDate(currDtStr);
		
		logger.info("*** Step 5 Actions: - Click Search Rendering Physician icon button");
		Phys renderingPhysNPIUPINList = physicianDao.getRenderingPhysFromPhys();
		int renderingPhysSeqId = renderingPhysNPIUPINList.getSeqId();
		Long renderingPhysNPIUPIN;
		if(renderingPhysNPIUPINList.getNpiId()!=null)
			renderingPhysNPIUPIN = renderingPhysNPIUPINList.getNpiId();
		else
			renderingPhysNPIUPIN = Long.valueOf(renderingPhysNPIUPINList.getUpinId());

		accnTestUpdate.clickNewRenderingPhys();
		Assert.assertTrue(isElementPresent(accnTestUpdate.renderingPhysInputBox(), 5),"       Rendering Physician Search Icon button is displayed.");
		accnTestUpdate.setNPIInput(String.valueOf(renderingPhysNPIUPIN));

		logger.info("*** Step 8 Expected Results: - Verify that the selected Physician is added to the UI for the selected test");
		String renderingPhysStr = accnTestUpdate.newRenderingPhys().getAttribute("title").replaceAll("[^a-zA-Z0-9_-]", "");
		System.out.println("renderingPhysStr = " + renderingPhysStr);
		Assert.assertTrue(renderingPhysStr.contains(String.valueOf(renderingPhysNPIUPIN)), "        Rendering Physician with NPI/UPIN " + renderingPhysNPIUPIN + " should show.");
		
		logger.info("*** Step 9 Actions: - Click Save And Clear button");
		accnTestUpdate.clickSaveAndClearBtn();

		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
				
		logger.info("*** Step 9 Expected Results: - Verify that the updated values are saved into DB properly");
		List<String> testInfosList = daoManagerXifinRpm.getTestInfoFromAccnTestByAccnIdTestAbbrev(accnId, testAbbrev, testDb);		
		Assert.assertEquals(testInfosList.get(9), currDtStr, "        Final report date " + currDt + " should be saved into DB.");
		
		List<AccnTest> testInfoFromAccnTest = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev);
		int accnTestSeqId = testInfoFromAccnTest.get(0).getAccnTestSeqId();
		AccnPhys accnPhysInfoList = accessionDao.getAccnPhysByAccnIdAccnTestSeqId(accnId, accnTestSeqId);
		Assert.assertEquals(accnPhysInfoList.getPhysSeqId(), renderingPhysSeqId, "        Rendering Physician Seq Id should be " + renderingPhysSeqId + " for Accession " + accnId);
		Assert.assertEquals(accnPhysInfoList.getAccnPhysTypId(), 4, "        Physician Type should be 4 (Rendering Physician)" + " for Accession " + accnId + " Test " + testAbbrev);
		
		logger.info("*** Step 10 Actions: - Reload the same Accession");
		navigation.navigateToAccnTestUpdatePage();
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 10 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");
		Assert.assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Accession - Test Update page title should be 'Accession - Test Update'");
		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
		Assert.assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, true), "        Save And Clear button should be enabled.");
		
		logger.info("*** Step 10 Expected Results: - Verify that the updated values are showing properly in UI");
		Assert.assertTrue(accnTestUpdate.selectedRenderingPhys(accnTestSeqId).getAttribute("title").replaceAll("[^a-zA-Z0-9_-]", "").contains(String.valueOf(renderingPhysNPIUPIN)), "        Rendering Physician NPI/UPIN " + renderingPhysNPIUPIN + " should show for Test " + testAbbrev);
		Assert.assertTrue(isElementPresent(accnTestUpdate.testValues(accnTestSeqId, currDtStr), 5), "        Final Report Date " + currDt + " should show for Test " + testAbbrev);
		
		logger.info("*** Step 11 Actions: - Reset the Accession");
		accnTestUpdate.clickResetBtn();
	}

	@Test(priority = 1, description = "Add a duplicate Single Test")
	@Parameters({"project", "testSuite", "testCase", "propLevel", "propName",  "wsUsername", "wsPassword"})
	public void testXPR_692(String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword ) throws Exception {
		logger.info("===== Testing - testXPR_692 =====");     
		accnTestUpdateUtils = new AccnTestUpdateUtils(driver, config, wait);
		accnTestUpdate = new AccnTestUpdate(driver, wait);
		navigation = new MenuNavigation(driver, config);
		timeStamp = new TimeStamp(driver);		
		randomCharacter = new RandomCharacter(driver);
		convertUtil = new ConvertUtil();

		logger.info("*** Step 1 Actions: - Log into Accn Test Update page with SSO username and password");				
		navigation.navigateToAccnTestUpdatePage();

		logger.info("*** Step 1 Expected Results: - Verify that the Load Accession page is displayed with correct page title");	
		Assert.assertTrue(isElementPresent(accnTestUpdate.loadPageTitle(), 10), "        The Accession - Test Update Load Accession page title should show.");
		Assert.assertEquals(accnTestUpdate.loadPageTitle().getText(),"Test Update", "        The Load Accession page title should be 'Accession - Test Update'");
		
		logger.info("*** Step 2 Actions: - Create a new Final Reported Accession via Accession WS");
		testDataSetup = new TestDataSetup(driver);
		Properties testProperties = TestDataSetup.executeWsTestCase(project, testSuite, testCase, config.getProperty(PropertyMap.ACCNWS_URL), config.getProperty(PropertyMap.ACCNWS_USER), config.getProperty(PropertyMap.ACCNWS_PASSWORD), config.getProperties());
		String accnId = testProperties.getProperty("NewAccnID");
		logger.info("AccnID: " + accnId);
    	
    	logger.info("*** Step 2 Expected Results: Verify that a new accession was created");
		Assert.assertNotNull(accnId);

    	logger.info("*** Step 3 Actions: - Load the new Accession");
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 3 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");		
		Assert.assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Accession - Test Update page title should be 'Accession - Test Update'");
		
		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
		Assert.assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, true), "        Save And Clear button should be enabled.");
				
		logger.info("*** Step 4 Actions: - Add a new single test that is not allow duplicate, enter final reported date");
		String currDt = timeStamp.getCurrentDate();
		String testAbbrev = testDao.getNotAllowDupSingleTestFromTestDtByAccnId(accnId).getTestAbbrev();
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.addNewRowBtn(), 10), "        Add new row button should be displayed.");
		accnTestUpdate.clickAddNewRowBtn();
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.newTestIdInput(), 10), "        Test ID Input field be displayed.");
		accnTestUpdate.setTestId(testAbbrev);
		
		String mainFac = facilityDao.getFacByFacId(1).getAbbrv();
		accnTestUpdate.setValueInDropdown(accnTestUpdate.posDropdown(), mainFac);
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.newFinalReportDateInput(), 10), "        Final Report Date Input field be displayed.");
		accnTestUpdate.setFinalReportDate(currDt);	
		
		logger.info("*** Step 5 Actions: - Add the same single test again, enter final reported date");
		accnTestUpdate.clickAddNewRowBtn();
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.newTestIdInput(), 10), "        Test ID Input field be displayed.");
		accnTestUpdate.setTestId(testAbbrev);		
			
		accnTestUpdate.setValueInDropdown(accnTestUpdate.posDropdown(), mainFac);
		
		Assert.assertTrue(isElementPresent(accnTestUpdate.newFinalReportDateInput(), 10), "        Final Report Date Input field be displayed.");
		accnTestUpdate.setFinalReportDate(currDt);	

		logger.info("*** Step 6 Actions: - Click Save And Clear button");
		accnTestUpdate.clickSaveAndClearBtn();

		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_FR_PENDING, QUEUE_WAIT_TIME_MS));
		Assert.assertTrue(accessionDao.waitForAccnToBeOutOfQueue(accnId, AccnStatusMap.Q_PRICE, QUEUE_WAIT_TIME_MS));
		
		logger.info("*** Step 6 Expected Results: - Verify that DUP Lab Message is added to the test in DB");
		List<String> accnTestMsgInfosList = daoManagerPlatform.getAccnTestMsgFromACCNTESTMSGByAccnIdTestAbbrev(accnId, testAbbrev, testDb);		
		String labMsg = accnTestMsgInfosList.get(3);
		String labNotes = accnTestMsgInfosList.get(1).trim();
		int accnTestSeqId = Integer.parseInt(accnTestMsgInfosList.get(4));
		Assert.assertEquals(labMsg, "DUP", "        DUP Lab Message should be added to Test " + testAbbrev);
		Assert.assertEquals(labNotes, "BDUP - Duplicate Test Ordered", "        BDUP - Duplicate Test Ordered Lab Notes should be added to Test " + testAbbrev);
				
		logger.info("*** Step 7 Actions: - Reload the same Accession");
		navigation.navigateToAccnTestUpdatePage();
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 7 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");
		Assert.assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Accession - Test Update page title should be 'Accession - Test Update'");
		
		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
		Assert.assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, true), "        Save And Clear button should be enabled.");
		
		logger.info("*** Step 7 Expected Results: - Verify that the DUP lab message is showing properly in UI");
		Assert.assertTrue(isElementPresent(accnTestUpdate.testValues(accnTestSeqId, labMsg), 5), "        Lab Message " + labMsg + " should show for Test " + testAbbrev);
		Assert.assertTrue(isElementPresent(accnTestUpdate.testValues(accnTestSeqId, labNotes), 5), "        Lab Notes " + labNotes + " should show for Test " + testAbbrev);
	
		logger.info("*** Step 8 Actions: - Reset the Accession");
		accnTestUpdate.clickResetBtn();
	}
	
	@Test(priority = 1, description = "Load an accession with Cln Billing Split")
	@Parameters({"accnId", "testAbbrev"})
	public void testXPR_689(String accnId, String testAbbrev) throws Exception {
		logger.info("===== Testing - testXPR_689 =====");     
		accnTestUpdateUtils = new AccnTestUpdateUtils(driver, config, wait);
		accnTestUpdate = new AccnTestUpdate(driver, wait);
		navigation = new MenuNavigation(driver, config);
		randomCharacter = new RandomCharacter(driver);

		logger.info("*** Step 1 Actions: - Log into Accn Test Update page with SSO username and password");				
		navigation.navigateToAccnTestUpdatePage();
	            

		logger.info("*** Step 1 Expected Results: - Verify that the Load Accession page is displayed with correct page title");	
		Assert.assertTrue(isElementPresent(accnTestUpdate.loadPageTitle(), 10), "        The Accession - Test Update Load Accession page title should show.");
		Assert.assertEquals(accnTestUpdate.loadPageTitle().getText(),"Test Update", "        The Load Accession page title should be 'Accession - Test Update'");

		logger.info("*** Step 2 Actions: - Load a Cln Billing Split main Accession");				
		Assert.assertTrue(isElementPresent(accnTestUpdate.lookUpAccnIdInput(), 10), "        Accession ID Input field should be displayed.");
		accnTestUpdate.setLookUpAccnId(accnId);
		
		logger.info("*** Step 2 Expected Results: - Verify that the accession is loaded properly on the Accession Test Update screen");		
		Assert.assertEquals(accnTestUpdate.accnTestUpdatePageTitle().trim(),"Test Update", "        The Accession - Test Update page title should be 'Accession - Test Update'");
		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(accnId), "        Accession ID " + accnId + " is not loaded properly.");
		Assert.assertTrue(isElementPresent(accnTestUpdate.saveAndClearBtn(), 10), "        Save And Clear button should be displayed.");
		Assert.assertTrue(isElementEnabled(accnTestUpdate.saveAndClearBtn(), 5, true), "        Save And Clear button should be enabled.");
				
		logger.info("*** Step 2 Expected Results: Verify that the hyperlink for the split accns shows");
		Assert.assertTrue(isElementPresent(accnTestUpdate.linkedAccnsLink(), 5), "       LinkedAccmLink should be displayed.");
		
		logger.info("*** Step 2 Expected Results: Verify that the Lab message and Lab note for the split test are displayed correctly. Cln bill checkbox is checked for the split test");
		List<AccnTest> testInfoFromAccnTest = accessionDao.getAccnTestByAccnIdTestAbbrev(accnId, testAbbrev);
		int accnTestSeqId = testInfoFromAccnTest.get(0).getAccnTestSeqId();
		
		accnTestUpdate.clickTestId(accnTestSeqId);
		Assert.assertTrue(isElementPresent(accnTestUpdate.testValues(accnTestSeqId, "SPLIT"), 5), "        Lab Message SPLIT should show for Test " + testAbbrev);
		Assert.assertTrue(isElementPresent(accnTestUpdate.testValues(accnTestSeqId, "AD HOC/Manual Split Accession"), 5), "        Lab Notes AD HOC/Manual Split Accession should show for Test " + testAbbrev);
		Assert.assertTrue(accnTestUpdate.clnBillChkbox(accnTestSeqId).isSelected(), "        Cln Bill checkbox should be checked for Test " + testAbbrev);
	
		logger.info("*** Step 3 Actions: - Click the hyperlink for the split accns");		
		accnTestUpdate.clickLinkedLnk();
		accnTestUpdate.clickLinkedAccnsList(2);

		logger.info("*** Step 3 Expected Results: - Verify that the split Accession is loaded properly");		
		String splitAccnId = accnId + "XM";
		Assert.assertTrue(accnTestUpdateUtils.isAccnLoaded(splitAccnId), "        The Split Accession ID " + splitAccnId + " is not loaded properly.");

		logger.info("*** Step 3 Expected Results: - Verify that the split test is loaded properly and Cln Bill checkbox is checked for the test");
		testInfoFromAccnTest = accessionDao.getAccnTestByAccnIdTestAbbrev(splitAccnId, testAbbrev);
		accnTestSeqId = testInfoFromAccnTest.get(0).getAccnTestSeqId();
		accnTestUpdate.clickTestAbbrev(accnTestSeqId);
		Assert.assertTrue(accnTestUpdate.testValues(accnTestSeqId, testAbbrev).isDisplayed(), "        Test Abbrev " + testAbbrev + " should show.");
		Assert.assertTrue(accnTestUpdate.clnBillChkbox(accnTestSeqId).isSelected(), "        Cln Bill checkbox should be checked for Test " + testAbbrev);	

		Assert.assertTrue(isElementPresent(accnTestUpdate.labMsgInput(), 5),"       Lab Message Input is displayed.");
		accnTestUpdate.setLabMsg("TNI");
		
		logger.info("*** Step 4 Actions: - Reset the Accession");
		accnTestUpdate.clickResetBtn();
	}	
	
}	
