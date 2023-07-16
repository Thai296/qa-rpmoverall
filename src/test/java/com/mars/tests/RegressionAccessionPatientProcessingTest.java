package com.mars.tests;


//import java.io.File;
//import java.net.URL;
import com.overall.accession.PatientProcessing.PatientDemographicSearch;
import com.overall.accession.PatientProcessing.PatientDemographicSearchResults;
import com.overall.accession.PatientProcessing.PatientDemographics;
import com.overall.accession.PatientProcessing.ProcedureCodeSearch;
import com.overall.accession.PatientProcessing.ProcedureCodeSearchResults;
import com.overall.accession.PatientProcessing.StandingOrder;
import com.overall.accession.accessionNavigation.AccessionNavigation;
import com.overall.accession.accessionProcessing.AccessionDemographics;
import com.overall.accession.accessionProcessing.AccessionSearch;
import com.overall.accession.accessionProcessing.AccessionSingleStatement;
import com.overall.accession.accessionProcessing.AccessionTransactionDetail;
import com.overall.accession.orderProcessing.AccnTestUpdateOld;
import com.overall.fileMaintenance.fileMaintenanceNavigation.FileMaintenanceNavigation;
import com.overall.fileMaintenance.orderProcessingConfig.FileMaintencePatternDefinition;
import com.overall.fileMaintenance.sysMgt.DataCacheConfiguration;
import com.overall.fileMaintenance.sysMgt.TaskScheduler;
import com.overall.fileMaintenance.sysMgt.TaskStatus;
import com.overall.headerNavigation.HeaderNavigation;
import com.overall.payor.payorDemographics.GroupDemographics;
import com.overall.payor.payorDemographics.PricingConfig;
import com.overall.payor.payorNavigation.PayorNavigation;
import com.overall.utils.AccessionUtils;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TestDataSetup;
import com.xifin.utils.TimeStamp;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class RegressionAccessionPatientProcessingTest extends SeleniumBaseTest  {

	private SsoLogin ssoLogin;
	private HeaderNavigation headerNavigation;
//	private Help help;
	private AccessionNavigation accessionNavigation;
//	private SuperSearch superSearch;
//	private SuperSearchResults superSearchResults;
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
	private PatientDemographics patientDemographics;
	private PatientDemographicSearch patientDemographicSearch;
	private PatientDemographicSearchResults patientDemographicSearchResults;
	private RandomCharacter randomCharacter;
	private ProcedureCodeSearch procedureCodeSearch;
	private ProcedureCodeSearchResults procedureCodeSearchResults;
//	private ClientSearch clientSearch;
//	private ClientSearchResults clientSearchResults;
	private AccessionUtils accessionUtils;
	private StandingOrder standingOrder;

	@Test(priority = 1, description = "Load an existing EPI")
	@Parameters({"email", "password"})
	public void testRPM_553(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_553 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		//String winHandler = driver.getWindowHandle();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Load an existing EPI in EPI field and tab out");
		ArrayList<String> ptList = daoManagerXifinRpm.getPtDemoInfoFromPT(testDb);
		String epi = ptList.get(0);
		String fName = ptList.get(1);
		String lName = ptList.get(2);

		patientDemographics.setEPI(epi);

		logger.info("*** Step 3 Expected Results: - Verify that the EPI was loaded properly");
		Assert.assertTrue(patientDemographics.ptFirstNameInput().getAttribute("value").trim().equals(fName), "        Pt First Name: " + fName + " should show.");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(lName), "        Pt Last Name: " + lName + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Load an existing EPI by Search")
	@Parameters({"email", "password"})
	public void testRPM_552(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_552 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();
		String winHandler = driver.getWindowHandle();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Patient Demographic Search button");
		patientDemographics.ptDemoSearchBtn().click();
		switchToPopupWin();

		logger.info("*** Step 3 Expected Results: - Verify that the Patient Demographics Search page displays");
		patientDemographicSearch = new PatientDemographicSearch(driver);
		Assert.assertTrue(patientDemographicSearch.pageTitleText().getText().trim().contains("Patient Demographic - Search"), "        Patient Demographic - Search page title should show.");

		logger.info("*** Step 4 Actions: - Enter EPI in the Patient ID Input field and tab out");
		ArrayList<String> ptList = daoManagerXifinRpm.getPtDemoInfoFromPT(testDb);
		String epi = ptList.get(0);
		String fName = ptList.get(1);
		String lName = ptList.get(2);

		patientDemographicSearch.setPatientId(epi);

		logger.info("*** Step 4 Actions: - Click Search button");
		patientDemographicSearch.searchBtn().click();

		logger.info("*** Step 4 Expected Results: - Verify that it's on Patient Demographic Search Results page");
		patientDemographicSearchResults = new PatientDemographicSearchResults(driver);
		Assert.assertTrue(patientDemographicSearchResults.pageTitleText().getText().trim().contains("Patient Demographic Search Results"), "        Patient Demographic - Search Results page title should show.");

		logger.info("*** Step 5 Actions: - Click EPI hyperlink in the Search Results page");
		patientDemographicSearchResults.epiText(epi).click();;

        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
        Assert.assertTrue(isElementEnabled(patientDemographics.submitBtn(), 5, true), "        Submit button should be enabled.");

		logger.info("*** Step 5 Expected Results: - Verify that the EPI was loaded properly in the Patient Demographics page");
		Assert.assertTrue(patientDemographics.ptFirstNameInput().getAttribute("value").trim().equals(fName), "        Pt First Name: " + fName + " should show.");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(lName), "        Pt Last Name: " + lName + " should show.");

		patientDemographics.resetBtn().click();

		driver.close();
	}

	@Test(priority = 1, description = "Reset")
	@Parameters({"email", "password"})
	public void testRPM_561(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_561 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Load an existing EPI in EPI field and tab out");
		ArrayList<String> ptList = daoManagerXifinRpm.getPtDemoInfoFromPT(testDb);
		String epi = ptList.get(0);
		String fName = ptList.get(1);
		String lName = ptList.get(2);

		patientDemographics.setEPI(epi);

		logger.info("*** Step 3 Expected Results: - Verify that the EPI was loaded properly");
		Assert.assertTrue(patientDemographics.ptFirstNameInput().getAttribute("value").trim().equals(fName), "        Pt First Name: " + fName + " should show.");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(lName), "        Pt Last Name: " + lName + " should show.");

		logger.info("*** Step 4 Actions: - Click Reset button");
		patientDemographics.resetBtn().click();

		logger.info("*** Step 4 Expected Results: - Verify that the EPI field was cleared");
		Assert.assertTrue(patientDemographics.epiInput().getAttribute("value").trim().equals(""), "        EPI field should be cleared.");

		driver.close();
	}

	@Test(priority = 1, description = "Create a new EPI")
	@Parameters({"email", "password"})
	public void testRPM_554(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_554 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor ID and tab out");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 4 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 4 Expected Results: - Verify that the Patient Info was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Add Exp Date")
	@Parameters({"email", "password"})
	public void testRPM_565(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_565 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor ID and tab out");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 4 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 4 Expected Results: - Verify that the Patient Info was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 5 Actions: - Enter 't' (today's date) in Exp Date Input field and tab out");
		patientDemographics.setExpDate("t");

		logger.info("*** Step 5 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the Patient Info and the Exp Date display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		timeStamp = new TimeStamp();
		String currentDate = timeStamp.getCurrentDate();
		Assert.assertTrue(patientDemographics.expDateInput().getAttribute("value").trim().equals(currentDate), "        Exp Date: " + currentDate + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Dialysis Info display")
	@Parameters({"email", "password"})
	public void testRPM_17(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_17 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();
		String winHandler = driver.getWindowHandle();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor ID and tab out");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Enter Dialysis Type, Medication, First Date of Dialysis");
		selectItem(patientDemographics.dialysisTypeDropDown(), "CAPD");
		selectItem(patientDemographics.medicationDropDown(), "Epogen");
		patientDemographics.setFirstDialDate("t");

		logger.info("*** Step 5 Actions: - Click Add Row button in NON-XIFIN Dialysis Patient History");
		patientDemographics.addRowBtn().click();

		logger.info("*** Step 5 Actions: - Enter DOS and Procedure Code in NON-XIFIN Dialysis Patient History");
		patientDemographics.setDialPtHistDOS("t");

		patientDemographics.procCdSearchBtn().click();
		switchToPopupWin();

		procedureCodeSearch = new ProcedureCodeSearch(driver);
		Assert.assertTrue(procedureCodeSearch.pageTitleText().getText().trim().contains("Procedure Code - Search"), "        Procedure Code - Search page title shoudl show");
		procedureCodeSearch.searchBtn().click();

		procedureCodeSearchResults = new ProcedureCodeSearchResults(driver);
		Assert.assertTrue(procedureCodeSearchResults.pageTitleText().getText().trim().contains("Procedure Code Search Results"), "          Procedure Code Search Results page title shoudl show");
		String procCd = procedureCodeSearchResults.procCdText("2").getText().trim();
		procedureCodeSearchResults.procCdText("2").click();

        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		logger.info("*** Step 6 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 7 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 7 Expected Results: - Verify that the Patient Info was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 7 Expected Results: - Verify that Dialysis Information display properly");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.dialysisTypeDropDown(), "CAPD"), "        Dialysis Type: CAPD should be selected");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.medicationDropDown(), "Epogen"), "        Medication: Epogen should be selected");

		timeStamp = new TimeStamp();
		String currentDate = timeStamp.getCurrentDate();
		Assert.assertEquals(patientDemographics.firstDialDtInput().getAttribute("value").trim(), currentDate);

		logger.info("*** Step 7 Expected Results: - Verify that NON-XIFIN Dialysis Patient History display properly");
		Assert.assertEquals(patientDemographics.dialPtHistDOSInput().getAttribute("value").trim(), currentDate);
		Assert.assertEquals(patientDemographics.procCdInput().getAttribute("value").trim(), procCd);

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Add Associated Patient IDs")
	@Parameters({"email", "password"})
	public void testRPM_555(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_555 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor ID and tab out");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 5 Actions: - Click Add Patient button");
		patientDemographics.addPatientBtn().click();

		logger.info("*** Step 5 Expected Results: - Verify that Create Associated Patient ID popup window shows");
		Assert.assertTrue(patientDemographics.asscPtIdPageTitle().getText().trim().contains("Create Associated Patient ID"), "        Create Associated Patient ID page title should show.");

		logger.info("*** Step 6 Actions: - Enter Patient ID");
		randomCharacter =  new RandomCharacter(driver);
		String clnPtId = randomCharacter.getRandomAlphaNumericString(10);
		patientDemographics.setAsscClnPtId(clnPtId);

		logger.info("*** Step 6 Actions: - Select Client Source Type");
		selectItem(patientDemographics.asscSrcTypDropDown(), "Client");

		logger.info("*** Step 6 Actions: - Enter a Client Source ID");
		String clnAbbrev = daoManagerAccnWS.getClnAbbrev(testDb);
		patientDemographics.setAsscClnSrcId(clnAbbrev);

        logger.info("*** Step 6 Actions: - Click Submit button in Create Associated Patient ID popup window");
        patientDemographics.asscSubmitBtn();

        logger.info("*** Step 7 Actions: - Click Submit button in Patient Demographics page");
        patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 8 Actions: - Load the same EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 8 Expected Results: - Verify that the Patient Info display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 8 Expected Results: - Verify that the Associated Patient ID display properly");
		Assert.assertTrue(patientDemographics.asscPtIdText().trim().equals(clnPtId), "        Associated Patient ID: " + clnPtId + " should show.");
		Assert.assertTrue(patientDemographics.asscSrcTypText().trim().equals("Client"), "        Associated Source Type: Client should show.");
		Assert.assertTrue(patientDemographics.asscSrcIdText().trim().equals(clnAbbrev), "        Associated Source ID: " + clnAbbrev + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Remove Associated Patient IDs")
	@Parameters({"email", "password"})
	public void testRPM_562(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_562 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor ID and tab out");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 5 Actions: - Click Add Patient button");
		patientDemographics.addPatientBtn().click();

		logger.info("*** Step 5 Expected Results: - Verify that Create Associated Patient ID popup window shows");
		Assert.assertTrue(patientDemographics.asscPtIdPageTitle().getText().trim().contains("Create Associated Patient ID"), "        Create Associated Patient ID page title should show.");

		logger.info("*** Step 6 Actions: - Enter Patient ID");
		randomCharacter =  new RandomCharacter(driver);
		String clnPtId = randomCharacter.getRandomAlphaNumericString(10);
		patientDemographics.setAsscClnPtId(clnPtId);

		logger.info("*** Step 6 Actions: - Select Client Source Type");
		selectItem(patientDemographics.asscSrcTypDropDown(), "Client");

		logger.info("*** Step 6 Actions: - Enter a Client Source ID");
		String clnAbbrev = daoManagerAccnWS.getClnAbbrev(testDb);
		patientDemographics.setAsscClnSrcId(clnAbbrev);

        logger.info("*** Step 6 Actions: - Click Submit button in Create Associated Patient ID popup window");
        patientDemographics.asscSubmitBtn();

        logger.info("*** Step 7 Actions: - Click Submit button in Patient Demographics page");
        patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 8 Actions: - Load the same EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 8 Expected Results: - Verify that the Patient Info display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 8 Expected Results: - Verify that the Associated Patient ID display properly");
		Assert.assertTrue(patientDemographics.asscPtIdText().trim().equals(clnPtId), "        Associated Patient ID: " + clnPtId + " should show.");
		Assert.assertTrue(patientDemographics.asscSrcTypText().trim().equals("Client"), "        Associated Source Type: Client should show.");
		Assert.assertTrue(patientDemographics.asscSrcIdText().trim().equals(clnAbbrev), "        Associated Source ID: " + clnAbbrev + " should show.");

		logger.info("*** Step 9 Actions: - Check Delete checkbox for the Client Associated Patient ID and Submit");
		patientDemographics.deleteAssocPtIdBtn().click();
        patientDemographics.submitBtn().click();
		Thread.sleep(3000);

		logger.info("*** Step 9 Expected Results: - Verify that the Client Associated Patient ID was deleted");
		Assert.assertTrue(daoManagerXifinRpm.getPtClnInfoFromPTCLNLNK(epi, testDb).isEmpty(), "        Associated Patient ID: " + clnPtId + " should be deleted.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Modify Associated Patient IDs")
	@Parameters({"email", "password"})
	public void testRPM_563(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_563 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor ID and tab out");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 5 Actions: - Click Add Patient button");
		patientDemographics.addPatientBtn().click();

		logger.info("*** Step 5 Expected Results: - Verify that Create Associated Patient ID popup window shows");
		Assert.assertTrue(patientDemographics.asscPtIdPageTitle().getText().trim().contains("Create Associated Patient ID"), "        Create Associated Patient ID page title should show.");

		logger.info("*** Step 6 Actions: - Enter Patient ID");
		randomCharacter =  new RandomCharacter(driver);
		String clnPtId = randomCharacter.getRandomAlphaNumericString(10);
		patientDemographics.setAsscClnPtId(clnPtId);

		logger.info("*** Step 6 Actions: - Select Client Source Type");
		selectItem(patientDemographics.asscSrcTypDropDown(), "Client");

		logger.info("*** Step 6 Actions: - Enter a Client Source ID");
		String clnAbbrev = daoManagerAccnWS.getClnAbbrev(testDb);
		patientDemographics.setAsscClnSrcId(clnAbbrev);

        logger.info("*** Step 6 Actions: - Click Submit button in Create Associated Patient ID popup window");
        patientDemographics.asscSubmitBtn();

        logger.info("*** Step 7 Actions: - Click Submit button in Patient Demographics page");
        patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 8 Actions: - Load the same EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 8 Expected Results: - Verify that the Patient Info display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 8 Expected Results: - Verify that the Associated Patient ID display properly");
		Assert.assertTrue(patientDemographics.asscPtIdText().trim().equals(clnPtId), "        Associated Patient ID: " + clnPtId + " should show.");
		Assert.assertTrue(patientDemographics.asscSrcTypText().trim().equals("Client"), "        Associated Source Type: Client should show.");
		Assert.assertTrue(patientDemographics.asscSrcIdText().trim().equals(clnAbbrev), "        Associated Source ID: " + clnAbbrev + " should show.");

		logger.info("*** Step 9 Actions: - Click the Edit Associated Patient Id button");
		patientDemographics.editAsscPtIdBtn().click();

		logger.info("*** Step 9 Expected Results: - Verify that Edit Associated Patient ID popup window shows");
		Assert.assertTrue(patientDemographics.asscPtIdPageTitle().getText().trim().contains("Edit Associated Patient ID"), "        Edit Associated Patient ID page title should show.");

		logger.info("*** Step 10 Actions: - Select Client Facility Source Type");
		selectItem(patientDemographics.asscSrcTypDropDown(), "Client Primary Facility");

		logger.info("*** Step 10 Actions: - Select Facility Source ID");
		String facAbbrev = daoManagerAccnWS.getFacAbbrevFromFACByFacId("1", testDb);
		selectItem(patientDemographics.asscFacSrcIdDropDown(), facAbbrev);

        logger.info("*** Step 10 Actions: - Click Submit button in Create Associated Patient ID popup window");
        patientDemographics.asscSubmitBtn();

        logger.info("*** Step 10 Actions: - Click Submit button in Patient Demographics page");
        patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 11 Actions: - Load the same EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 11 Expected Results: - Verify that the Patient Info display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 11 Expected Results: - Verify that the updated Associated Patient ID display properly");
		Assert.assertTrue(patientDemographics.asscPtIdText().trim().equals(clnPtId), "        Associated Patient ID: " + clnPtId + " should show.");
		Assert.assertTrue(patientDemographics.asscSrcTypText().trim().equals("Facility"), "        Associated Source Type: Facility should show.");
		Assert.assertTrue(patientDemographics.asscSrcIdText().trim().equals(facAbbrev), "        Associated Source ID: " + facAbbrev + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Patient Demo Info display")
	@Parameters({"email", "password"})
	public void testRPM_556(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_556 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter Patient SSN, Name, DOB, Gender and Marital Status Information");
		patientDemographics.setPtSSN("123456789");

		String ptFName = randomCharacter.getRandomAlphaString(8);
		patientDemographics.setPtFirstName(ptFName);

		patientDemographics.setPtDOB("01/01/2015");
		selectItem(patientDemographics.ptGenderDropDown(), "Female");
		selectItem(patientDemographics.ptMaritalStatusDropDown(), "S");

		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Payor ID");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

        logger.info("*** Step 5 Actions: - Click Submit button in Patient Demographics page");
        patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 6 Actions: - Load the same EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 6 Expected Results: - Verify that the Patient Demo Info display properly");
		Assert.assertTrue(patientDemographics.ptSSNInput().getAttribute("value").trim().equals("123-45-6789"), "        Pt SSN: 123-45-6789 should show.");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptFirstNameInput().getAttribute("value").trim().equals(ptFName), "        Pt First Name: " + ptFName + " should show.");
		Assert.assertTrue(patientDemographics.ptDOBInput().getAttribute("value").trim().equals("01/01/2015"), "        Pt DOB: 01/01/2015 should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.ptGenderDropDown(), "Female"), "        Gender: Female should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.ptMaritalStatusDropDown(), "S"), "        Pt Marital Status: S should show.");

		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Patient Address Info display")
	@Parameters({"email", "password"})
	public void testRPM_579(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_579 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter Patient Address Information");
		String ptAddr1 = "Addr1" + randomCharacter.getRandomAlphaString(6);
		patientDemographics.setPtAddr1(ptAddr1);

		String ptAddr2 = "Addr2" + randomCharacter.getRandomAlphaString(6);
		patientDemographics.setPtAddr2(ptAddr2);

		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Payor ID");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

        logger.info("*** Step 5 Actions: - Click Submit button in Patient Demographics page");
        patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 6 Actions: - Load the same EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 6 Expected Results: - Verify that the Patient Address Info display properly");

		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		Assert.assertTrue(patientDemographics.ptAddr1Input().getAttribute("value").trim().equals(ptAddr1.toUpperCase()), "        Pt Addr1: " + ptAddr1.toUpperCase() + " should show.");
		Assert.assertTrue(patientDemographics.ptAddr2Input().getAttribute("value").trim().equals(ptAddr2.toUpperCase()), "        Pt Addr2: " + ptAddr2.toUpperCase() + " should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.ptCountryDropDown(), "USA"), "        Country: USA should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.ptCityInput().getAttribute("value").trim().equals("SAN DIEGO"), "        Pt City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.ptStateDropDown(), "CA"), "        State: CA should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Patient Contact Info display")
	@Parameters({"email", "password"})
	public void testRPM_557(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_557 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter Patient Phone numbers and Email Information");
		patientDemographics.setPtZip("92129");
		patientDemographics.setPtHmPhone("8587514567");
		patientDemographics.setPtWkPhone("8587514569");
		patientDemographics.setPtEmail("qatester@xifin.com");

		logger.info("*** Step 4 Actions: - Enter Payor ID");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

        logger.info("*** Step 5 Actions: - Click Submit button in Patient Demographics page");
        patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 6 Actions: - Load the same EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 6 Expected Results: - Verify that the Patient Contact Info display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.ptHmPhoneInput().getAttribute("value").trim().equals("(858) 751-4567"), "        Pt Hm Phone: (858) 751-4567 should show.");
		Assert.assertTrue(patientDemographics.ptWkPhoneInput().getAttribute("value").trim().equals("(858) 751-4569"), "        Pt Wk Phone: (858) 751-4569 should show.");
		Assert.assertTrue(patientDemographics.ptEmailInput().getAttribute("value").trim().equals("qatester@xifin.com"), "        Pt Email: qatester@xifin.com should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Patient Comments and MSP DOS Info display")
	@Parameters({"email", "password"})
	public void testRPM_580(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_580 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter Patient Comments and MSP DOS Information");
		patientDemographics.setPtDOB("01/01/2015");
		patientDemographics.setPtZip("92129");

		String ptComments = "Comments " + randomCharacter.getRandomAlphaString(10);
		patientDemographics.setPtComments(ptComments);

		patientDemographics.setPtMSPDOS("01/01/2015");

		logger.info("*** Step 4 Actions: - Enter Payor ID");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

        logger.info("*** Step 5 Actions: - Click Submit button in Patient Demographics page");
        patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 6 Actions: - Load the same EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 6 Expected Results: - Verify that the Patient Comments and MSP DOS Info display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.ptCommentsInput().getAttribute("value").trim().equals(ptComments), "        Comments: " + ptComments + " should show.");
		Assert.assertTrue(patientDemographics.ptMSPDOSInput().getAttribute("value").trim().equals("01/01/2015"), "        DOS of Most Recent MSP Form: 01/01/2015 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Update Patient Demo Info")
	@Parameters({"email", "password"})
	public void testRPM_560(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_560 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter Patient SSN, Name, DOB, Gender and Marital Status Information");
		patientDemographics.setPtSSN("123456789");

		String ptFName = randomCharacter.getRandomAlphaString(8);
		patientDemographics.setPtFirstName(ptFName);

		patientDemographics.setPtDOB("01/01/2015");
		selectItem(patientDemographics.ptGenderDropDown(), "Female");
		selectItem(patientDemographics.ptMaritalStatusDropDown(), "S");

		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Payor ID");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

        logger.info("*** Step 5 Actions: - Click Submit button in Patient Demographics page");
        patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 6 Actions: - Load the same EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 6 Expected Results: - Verify that the Patient Demo Info display properly");
		Assert.assertTrue(patientDemographics.ptSSNInput().getAttribute("value").trim().equals("123-45-6789"), "        Pt SSN: 123-45-6789 should show.");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptFirstNameInput().getAttribute("value").trim().equals(ptFName), "        Pt First Name: " + ptFName + " should show.");
		Assert.assertTrue(patientDemographics.ptDOBInput().getAttribute("value").trim().equals("01/01/2015"), "        Pt DOB: 01/01/2015 should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.ptGenderDropDown(), "Female"), "        Gender: Female should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.ptMaritalStatusDropDown(), "S"), "        Pt Marital Status: S should show.");

		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 7 Actions: - Load the same EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 7 Actions: - Update the Patient Demo Info");
		patientDemographics.setPtSSN("123459999");

		ptFName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setPtFirstName(ptFName);

		patientDemographics.setPtDOB("06/01/2015");
		selectItem(patientDemographics.ptGenderDropDown(), "Male");
		selectItem(patientDemographics.ptMaritalStatusDropDown(), "M");

        logger.info("*** Step 7 Actions: - Click Submit button in Patient Demographics page");
        patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 8 Actions: - Load the same EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 8 Expected Results: - Verify that the updated Patient Demo Info display properly");
		Assert.assertTrue(patientDemographics.ptSSNInput().getAttribute("value").trim().equals("123-45-9999"), "        Pt SSN: 123-45-9999 should show.");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptFirstNameInput().getAttribute("value").trim().equals(ptFName), "        Pt First Name: " + ptFName + " should show.");
		Assert.assertTrue(patientDemographics.ptDOBInput().getAttribute("value").trim().equals("06/01/2015"), "        Pt DOB: 06/01/2015 should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.ptGenderDropDown(), "Male"), "        Gender: Male should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.ptMaritalStatusDropDown(), "M"), "        Pt Marital Status: M should show.");

		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Update Patient Address Info")
	@Parameters({"email", "password"})
	public void testRPM_581(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_581 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter Patient Address Information");
		String ptAddr1 = "Addr1" + randomCharacter.getRandomAlphaString(6);
		patientDemographics.setPtAddr1(ptAddr1);

		String ptAddr2 = "Addr2" + randomCharacter.getRandomAlphaString(6);
		patientDemographics.setPtAddr2(ptAddr2);

		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Payor ID");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

        logger.info("*** Step 5 Actions: - Click Submit button in Patient Demographics page");
        patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 6 Actions: - Load the same EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 6 Expected Results: - Verify that the Patient Address Info display properly");

		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		Assert.assertTrue(patientDemographics.ptAddr1Input().getAttribute("value").trim().equals(ptAddr1.toUpperCase()), "        Pt Addr1: " + ptAddr1.toUpperCase() + " should show.");
		Assert.assertTrue(patientDemographics.ptAddr2Input().getAttribute("value").trim().equals(ptAddr2.toUpperCase()), "        Pt Addr2: " + ptAddr2.toUpperCase() + " should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.ptCountryDropDown(), "USA"), "        Country: USA should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.ptCityInput().getAttribute("value").trim().equals("SAN DIEGO"), "        Pt City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.ptStateDropDown(), "CA"), "        State: CA should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 7 Actions: - Update Patient Address Information");
		ptAddr1 = "Addr1" + randomCharacter.getRandomAlphaString(8);
		patientDemographics.setPtAddr1(ptAddr1);

		ptAddr2 = "Addr2" + randomCharacter.getRandomAlphaString(8);
		patientDemographics.setPtAddr2(ptAddr2);

		patientDemographics.setPtZip("92127");

        logger.info("*** Step 7 Actions: - Click Submit button in Patient Demographics page");
        patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 8 Actions: - Load the same EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 8 Expected Results: - Verify that the updated Patient Address Info display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptAddr1Input().getAttribute("value").trim().equals(ptAddr1.toUpperCase()), "        Pt Addr1: " + ptAddr1.toUpperCase() + " should show.");
		Assert.assertTrue(patientDemographics.ptAddr2Input().getAttribute("value").trim().equals(ptAddr2.toUpperCase()), "        Pt Addr2: " + ptAddr2.toUpperCase() + " should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.ptCountryDropDown(), "USA"), "        Country: USA should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92127"), "        Pt Zip: 92127 should show.");
		Assert.assertTrue(patientDemographics.ptCityInput().getAttribute("value").trim().equals("SAN DIEGO"), "        Pt City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.ptStateDropDown(), "CA"), "        State: CA should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Update Patient Contact Info")
	@Parameters({"email", "password"})
	public void testRPM_582(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_582 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter Patient Phone numbers and Email Information");
		patientDemographics.setPtZip("92129");
		patientDemographics.setPtHmPhone("8587514567");
		patientDemographics.setPtWkPhone("8587514569");
		patientDemographics.setPtEmail("qatester@xifin.com");

		logger.info("*** Step 4 Actions: - Enter Payor ID");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

        logger.info("*** Step 5 Actions: - Click Submit button in Patient Demographics page");
        patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 6 Actions: - Load the same EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 6 Expected Results: - Verify that the Patient Contact Info display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.ptHmPhoneInput().getAttribute("value").trim().equals("(858) 751-4567"), "        Pt Hm Phone: (858) 751-4567 should show.");
		Assert.assertTrue(patientDemographics.ptWkPhoneInput().getAttribute("value").trim().equals("(858) 751-4569"), "        Pt Wk Phone: (858) 751-4569 should show.");
		Assert.assertTrue(patientDemographics.ptEmailInput().getAttribute("value").trim().equals("qatester@xifin.com"), "        Pt Email: qatester@xifin.com should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 7 Actions: - Update Patient Phone numbers and Email Information");
		patientDemographics.setPtZip("92129");
		patientDemographics.setPtHmPhone("8587517654");
		patientDemographics.setPtWkPhone("8587519654");
		patientDemographics.setPtEmail("autotester@xifin.com");

        logger.info("*** Step 7 Actions: - Click Submit button in Patient Demographics page");
        patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 8 Actions: - Load the same EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 8 Expected Results: - Verify that the updated Patient Contact Info display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.ptHmPhoneInput().getAttribute("value").trim().equals("(858) 751-7654"), "        Pt Hm Phone: (858) 751-7654 should show.");
		Assert.assertTrue(patientDemographics.ptWkPhoneInput().getAttribute("value").trim().equals("(858) 751-9654"), "        Pt Wk Phone: (858) 751-9654 should show.");
		Assert.assertTrue(patientDemographics.ptEmailInput().getAttribute("value").trim().equals("autotester@xifin.com"), "        Pt Email: autotester@xifin.com should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Update Patient Comments and MSP DOS Info")
	@Parameters({"email", "password"})
	public void testRPM_583(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_583 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter Patient Comments and MSP DOS Information");
		patientDemographics.setPtDOB("01/01/2015");
		patientDemographics.setPtZip("92129");

		String ptComments = "Comments " + randomCharacter.getRandomAlphaString(10);
		patientDemographics.setPtComments(ptComments);

		patientDemographics.setPtMSPDOS("01/01/2015");

		logger.info("*** Step 4 Actions: - Enter Payor ID");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

        logger.info("*** Step 5 Actions: - Click Submit button in Patient Demographics page");
        patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 6 Actions: - Load the same EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 6 Expected Results: - Verify that the Patient Comments and MSP DOS Info display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.ptCommentsInput().getAttribute("value").trim().equals(ptComments), "        Comments: " + ptComments + " should show.");
		Assert.assertTrue(patientDemographics.ptMSPDOSInput().getAttribute("value").trim().equals("01/01/2015"), "        DOS of Most Recent MSP Form: 01/01/2015 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 7 Actions: - Update Patient Comments and MSP DOS Information");
		ptComments = "Updated Comments " + randomCharacter.getRandomAlphaString(10);
		patientDemographics.setPtComments(ptComments);

		patientDemographics.setPtMSPDOS("02/02/2015");

        logger.info("*** Step 7 Actions: - Click Submit button in Patient Demographics page");
        patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 8 Actions: - Load the same EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 8 Expected Results: - Verify that the updated Patient Comments and MSP DOS Info display properly");
		Assert.assertTrue(patientDemographics.ptCommentsInput().getAttribute("value").trim().equals(ptComments), "        Comments: " + ptComments + " should show.");
		Assert.assertTrue(patientDemographics.ptMSPDOSInput().getAttribute("value").trim().equals("02/02/2015"), "        DOS of Most Recent MSP Form: 02/02/2015 should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Create a new Insurance Eff Date")
	@Parameters({"email", "password"})
	public void testRPM_566(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_566 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor ID and tab out");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the Patient Info was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 6 Actions: - Click Create New Eff. Date button");
		patientDemographics.createNewEffDateBtn();

		logger.info("*** Step 6 Actions: - Enter a new Eff Date and a new Payor ID");
		patientDemographics.setNewEffDate("03/01/2015");

		pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("3", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 6 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 7 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 7 Expected Results: - Verify that the new Eff date and Payor ID show properly");
		selectItem(patientDemographics.effDateDropDown(), "03/01/2015");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Delete Insurance Eff Date")
	@Parameters({"email", "password"})
	public void testRPM_567(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_567 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor ID and tab out");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the Patient Info was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 6 Actions: - Click Create New Eff. Date button");
		patientDemographics.createNewEffDateBtn();

		logger.info("*** Step 6 Actions: - Enter a new Eff Date and a new Payor ID");
		patientDemographics.setNewEffDate("03/01/2015");

		pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("3", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 6 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 7 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 7 Expected Results: - Verify that the new Eff date and Payor ID show properly");
		selectItem(patientDemographics.effDateDropDown(), "03/01/2015");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 8 Actions: - Check Delete Effective Date checkbox");
		patientDemographics.deleteEffDateCheckBox().click();

		logger.info("*** Step 8 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 9 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 9 Expected Results: - Verify that the Eff date got deleted");
		Assert.assertFalse(isDropdownItemSelected(patientDemographics.effDateDropDown(), "03/01/2015"), "        Eff Date: 03/01/2015 should not display in the Eff Date Dropdown.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Update Dialysis Info")
	@Parameters({"email", "password"})
	public void testRPM_584(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_584 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();
		String winHandler = driver.getWindowHandle();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor ID and tab out");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Enter Dialysis Type, Medication, First Date of Dialysis");
		selectItem(patientDemographics.dialysisTypeDropDown(), "CAPD");
		selectItem(patientDemographics.medicationDropDown(), "Epogen");
		patientDemographics.setFirstDialDate("t");

		logger.info("*** Step 5 Actions: - Click Add Row button in NON-XIFIN Dialysis Patient History");
		patientDemographics.addRowBtn().click();

		logger.info("*** Step 5 Actions: - Enter DOS and Procedure Code in NON-XIFIN Dialysis Patient History");
		patientDemographics.setDialPtHistDOS("t");

		patientDemographics.procCdSearchBtn().click();
		switchToPopupWin();

		procedureCodeSearch = new ProcedureCodeSearch(driver);
		Assert.assertTrue(procedureCodeSearch.pageTitleText().getText().trim().contains("Procedure Code - Search"), "        Procedure Code - Search page title shoudl show");
		procedureCodeSearch.searchBtn().click();

		procedureCodeSearchResults = new ProcedureCodeSearchResults(driver);
		Assert.assertTrue(procedureCodeSearchResults.pageTitleText().getText().trim().contains("Procedure Code Search Results"), "          Procedure Code Search Results page title shoudl show");
		String procCd = procedureCodeSearchResults.procCdText("2").getText().trim();
		procedureCodeSearchResults.procCdText("2").click();

        switchToWin(winHandler);
        switchToDefaultWinFromFrame();

		logger.info("*** Step 6 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 7 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 7 Expected Results: - Verify that the Patient Info was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 7 Expected Results: - Verify that Dialysis Information display properly");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.dialysisTypeDropDown(), "CAPD"), "        Dialysis Type: CAPD should be selected");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.medicationDropDown(), "Epogen"), "        Medication: Epogen should be selected");

		timeStamp = new TimeStamp();
		String currentDate = timeStamp.getCurrentDate();
		Assert.assertEquals(patientDemographics.firstDialDtInput().getAttribute("value").trim(), currentDate);

		logger.info("*** Step 7 Expected Results: - Verify that NON-XIFIN Dialysis Patient History display properly");
		Assert.assertEquals(patientDemographics.dialPtHistDOSInput().getAttribute("value").trim(), currentDate);
		Assert.assertEquals(patientDemographics.procCdInput().getAttribute("value").trim(), procCd);

		logger.info("*** Step 8 Actions: - Update Dialysis Information");
		selectItem(patientDemographics.dialysisTypeDropDown(), "ESRD");
		selectItem(patientDemographics.medicationDropDown(), "Sero Negative");
		patientDemographics.setFirstDialDate("04/15/2015");
		patientDemographics.setDialPtHistDOS("04/15/2015");

		logger.info("*** Step 8 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 9 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 9 Expected Results: - Verify that the updated Dialysis Information display properly");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.dialysisTypeDropDown(), "ESRD"), "        Dialysis Type: ESRD should be selected");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.medicationDropDown(), "Sero Negative"), "        Medication: Sero Negative should be selected");

		Assert.assertEquals(patientDemographics.firstDialDtInput().getAttribute("value").trim(), "04/15/2015");
		Assert.assertEquals(patientDemographics.dialPtHistDOSInput().getAttribute("value").trim(), "04/15/2015");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "All Accessions display")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_16(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_16 *****");

		accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));

		logger.info("*** Step 17 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		String winHandler = driver.getWindowHandle();
		String popupHandles = switchToPopupWin("c");
		switchToPopupWin();

		logger.info("*** Step 17 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 18 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);

		logger.info("*** Step 18 Expected Results: - Verify patient First Name is correct when accession data loads");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		Assert.assertEquals(accessionDemographics.ptFNameInput().getAttribute("value"), daoManagerPatientPortal.getFnameByAccnId(accnId, testDb));

		logger.info("*** Step 19 Actions: - Navigate to Create New Patient popup");
		accessionDemographics.naviateToCreateNewPatient();
		//switchToPopupWin();

		logger.info("*** Step 19 Expected Results: - Verify Create New Patient popup displays");
		Assert.assertTrue(isElementPresent(accessionDemographics.createNewPatientPopupOkBtn(),5));

		logger.info("*** Step 20 Actions: - Add the epi");
		accessionDemographics.sysGenerateEpi();
		//accessionDemographics.addEpi();
		//accessionDemographics.createNewPatientPopupCancelBtn();
		//switchToPopupWin(popupHandles);

		logger.info("*** Step 20 Expected Results: - Verify that the EPI gets generated");
		Assert.assertFalse(accessionDemographics.epiText().trim().isEmpty(), "        EPI: " + accessionDemographics.epiText() + " should be generated.");

		logger.info("*** Step 21 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.submit();
		accessionDemographics.reset();
		accessionDemographics.searchAccnId(accnId);

		logger.info("*** Step 21 Expected Results: - Verify EPI is associated to the accession");
		Assert.assertEquals(accessionDemographics.epiText().toString(), daoManagerPatientPortal.getEpiByAccnId(accnId, testDb));

		logger.info("*** Step 22 Actions: - Navigate to Patient Demographics page");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToOriginalPatientDemographicsLink();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 22 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 23 Actions: - Load an existing EPI in EPI field and tab out");
		patientDemographics.setEPI(daoManagerPatientPortal.getEpiByAccnId(accnId, testDb));

		logger.info("*** Step 23 Expected Results: - Verify that the EPI was loaded properly");
		Assert.assertTrue(patientDemographics.ptFirstNameInput().getAttribute("value").trim().equals(daoManagerPatientPortal.getFnameByAccnId(accnId, testDb)), "        Pt First Name: " + daoManagerPatientPortal.getFnameByAccnId(accnId, testDb) + " should show.");

		logger.info("*** Step 24 Actions: - Click the All Accessions tab");
		patientDemographics.allAccnTab();

		logger.info("*** Step 24 Expected Results: - Verify that the Accession ID displays in All Accessions tab");
		Assert.assertTrue(patientDemographics.accnIdLink().getText().trim().equals(accnId), "        Accession ID: " + accnId + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Modify EPI")
	@Parameters({"email", "password", "project", "testSuite", "testCase", "propLevel", "propName", "wsUsername", "wsPassword"})
	public void testRPM_559(String email, String password, String project, String testSuite, String testCase, String propLevel, String propName, String wsUsername, String wsPassword) throws Exception {
		logger.info("***** Testing - testRPM_559 *****");

		accessionUtils = new AccessionUtils(driver, daoManagerXifinRpm, daoManagerPlatform, daoManagerPatientPortal);
		String accnId = accessionUtils.createPricedAccn(this, null, email, password, project, testSuite, testCase, propLevel, propName, wsUsername, wsPassword, testDb, config.getProperty(PropertyMap.ACCNWS_URL));

		logger.info("*** Step 17 Actions: - Navigate to Accession Tab");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToDefaultWinFromFrame();
		headerNavigation.navigateToAccessionTab();
		String winHandler = driver.getWindowHandle();
		String popupHandles = switchToPopupWin("c");
		switchToPopupWin();

		logger.info("*** Step 17 Expected Results: - Verify that the accession demographic page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertEquals(accessionDemographics.pageTitleText().getText().trim(), "Accession - Demographics", "        Accession Demographics page title should show.");

		logger.info("*** Step 18 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.searchAccnId(accnId);

		logger.info("*** Step 18 Expected Results: - Verify patient First Name is correct when accession data loads");
		Assert.assertTrue(isElementPresent(accessionDemographics.ptFNameInput(),5));
		String ptFName = daoManagerPatientPortal.getFnameByAccnId(accnId, testDb);
		Assert.assertEquals(accessionDemographics.ptFNameInput().getAttribute("value"), ptFName);

		logger.info("*** Step 19 Actions: - Navigate to Create New Patient popup");
		accessionDemographics.naviateToCreateNewPatient();
		//switchToPopupWin();

		logger.info("*** Step 19 Expected Results: - Verify Create New Patient popup displays");
		Assert.assertTrue(isElementPresent(accessionDemographics.createNewPatientPopupOkBtn(),5));

		logger.info("*** Step 20 Actions: - Add the epi");
		accessionDemographics.sysGenerateEpi();
		//accessionDemographics.addEpi();
		//accessionDemographics.createNewPatientPopupCancelBtn();
		//switchToPopupWin(popupHandles);

		logger.info("*** Step 20 Expected Results: - Verify that the EPI gets generated");
		Assert.assertFalse(accessionDemographics.epiText().trim().isEmpty(), "        EPI: " + accessionDemographics.epiText() + " should be generated.");

		logger.info("*** Step 21 Actions: - Load the accn data by entering the accnId in the accnId input field and tab");
		accessionDemographics.submit();
		accessionDemographics.reset();
		accessionDemographics.searchAccnId(accnId);

		logger.info("*** Step 21 Expected Results: - Verify EPI is associated to the accession");
		String epi = daoManagerPatientPortal.getEpiByAccnId(accnId, testDb);
		Assert.assertEquals(accessionDemographics.epiText().toString(), epi);

		logger.info("*** Step 22 Actions: - Navigate to Patient Demographics page");
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.navigateToOriginalPatientDemographicsLink();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 22 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 23 Actions: - Load an existing EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 23 Expected Results: - Verify that the EPI was loaded properly");
		Assert.assertTrue(patientDemographics.ptFirstNameInput().getAttribute("value").trim().equals(ptFName), "        Pt First Name: " + ptFName + " should show.");

		logger.info("*** Step 24 Actions: - Click the All Accessions tab");
		patientDemographics.allAccnTab();

		logger.info("*** Step 24 Expected Results: - Verify that the Accession ID displays in All Accessions tab");
		Assert.assertTrue(patientDemographics.accnIdLink().getText().trim().equals(accnId), "        Accession ID: " + accnId + " should show.");

		logger.info("*** Step 25 Actions: - Enter a new EPI in Modify EPI field and tab out");
		String newEPI = "Auto" + epi;
		patientDemographics.setNewEPI(newEPI);

		logger.info("*** Step 25 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToOriginalPatientDemographicsLink();
		switchToPopupWin();
		driver.close();
		switchToWin(winHandler);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 22 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 26 Actions: - Load the new EPI in EPI field and tab out");
		patientDemographics.setEPI(newEPI);

		logger.info("*** Step 26 Expected Results: - Verify that the EPI was loaded properly");
		Assert.assertTrue(patientDemographics.ptFirstNameInput().getAttribute("value").trim().equals(ptFName), "        Pt First Name: " + ptFName + " should show.");

		logger.info("*** Step 27 Actions: - Click the All Accessions tab");
		patientDemographics.allAccnTab();

		logger.info("*** Step 27 Expected Results: - Verify that the Accession ID displays in All Accessions tab");
		Assert.assertTrue(patientDemographics.accnIdLink().getText().trim().equals(accnId), "        Accession ID: " + accnId + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Insurance Payor Info display")
	@Parameters({"email", "password"})
	public void testRPM_15(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_15 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and tab out");
		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Insurance Payor Info (Payor ID, Subs ID, Group ID, Group Name and Plan ID)");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		randomCharacter = new RandomCharacter();
		String subsId = randomCharacter.getRandomNumericString(9);
		String grpId = randomCharacter.getRandomAlphaNumericString(5);
		String grpName = randomCharacter.getRandomAlphaString(6);
		String planId = randomCharacter.getRandomAlphaString(8);

		patientDemographics.setInsSubsId(subsId);
		patientDemographics.setGrpId(grpId);
		patientDemographics.setGrpName(grpName);
		patientDemographics.setPlanId(planId);

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the Insurance Payor Info (Payor ID, Subs ID, Group ID, Group Name and Plan ID) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.subsIdInput().getAttribute("value").trim().equals(subsId), "        Subscriber ID: " + subsId + " should show.");
		Assert.assertTrue(patientDemographics.grpIdInput().getAttribute("value").trim().equals(grpId), "        Group ID: " + grpId + " should show.");
		Assert.assertTrue(patientDemographics.grpNameInput().getAttribute("value").trim().equals(grpName), "        Group Name: " + grpName + " should show.");
		Assert.assertTrue(patientDemographics.planIdInput().getAttribute("value").trim().equals(planId), "        Plan ID: " + planId + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Update Insurance Payor Info")
	@Parameters({"email", "password"})
	public void testRPM_597(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_597 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and tab out");
		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Insurance Payor Info (Payor ID, Subs ID, Group ID, Group Name and Plan ID)");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		randomCharacter = new RandomCharacter();
		String subsId = randomCharacter.getRandomNumericString(9);
		String grpId = randomCharacter.getRandomAlphaNumericString(5);
		String grpName = randomCharacter.getRandomAlphaString(6);
		String planId = randomCharacter.getRandomAlphaString(8);

		patientDemographics.setInsSubsId(subsId);
		patientDemographics.setGrpId(grpId);
		patientDemographics.setGrpName(grpName);
		patientDemographics.setPlanId(planId);

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the Insurance Payor Info (Payor ID, Subs ID, Group ID, Group Name and Plan ID) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.subsIdInput().getAttribute("value").trim().equals(subsId), "        Subscriber ID: " + subsId + " should show.");
		Assert.assertTrue(patientDemographics.grpIdInput().getAttribute("value").trim().equals(grpId), "        Group ID: " + grpId + " should show.");
		Assert.assertTrue(patientDemographics.grpNameInput().getAttribute("value").trim().equals(grpName), "        Group Name: " + grpName + " should show.");
		Assert.assertTrue(patientDemographics.planIdInput().getAttribute("value").trim().equals(planId), "        Plan ID: " + planId + " should show.");


		logger.info("*** Step 6 Actions: - Update Insurance Payor Info (Payor ID, Subs ID, Group ID, Group Name and Plan ID)");
		pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("3", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		subsId = randomCharacter.getRandomNumericString(9);
		grpId = randomCharacter.getRandomAlphaNumericString(5);
		grpName = randomCharacter.getRandomAlphaString(6);
		planId = randomCharacter.getRandomAlphaString(8);

		patientDemographics.setInsSubsId(subsId);
		patientDemographics.setGrpId(grpId);
		patientDemographics.setGrpName(grpName);
		patientDemographics.setPlanId(planId);

		logger.info("*** Step 6 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 7 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 7 Expected Results: - Verify that the updated Insurance Payor Info (Payor ID, Subs ID, Group ID, Group Name and Plan ID) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.subsIdInput().getAttribute("value").trim().equals(subsId), "        Updated Subscriber ID: " + subsId + " should show.");
		Assert.assertTrue(patientDemographics.grpIdInput().getAttribute("value").trim().equals(grpId), "        Updated Group ID: " + grpId + " should show.");
		Assert.assertTrue(patientDemographics.grpNameInput().getAttribute("value").trim().equals(grpName), "        Updated Group Name: " + grpName + " should show.");
		Assert.assertTrue(patientDemographics.planIdInput().getAttribute("value").trim().equals(planId), "        Updated Plan ID: " + planId + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Insurance Insured Demo Info display")
	@Parameters({"email", "password"})
	public void testRPM_585(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_585 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and tab out");
		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Insured Demo Info (Insurd LName, FName, Rlshp, DOB, Sex and SSN)");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		randomCharacter = new RandomCharacter();
		timeStamp = new TimeStamp();
		String insurdFName = randomCharacter.getRandomAlphaString(6);
		String insurdLName = randomCharacter.getRandomAlphaString(6);
		String insurdDOB = timeStamp.getPreviousDate("MM/dd/yyyy", 600);

		patientDemographics.setInsurdFName(insurdFName);
		patientDemographics.setInsurdLName(insurdLName);
		selectItem(patientDemographics.rlshpDropDown(), "self");
		patientDemographics.setInsurdDOB(insurdDOB);
		selectItem(patientDemographics.insuredSexDropDown(), "Female");
		patientDemographics.setInsurdSSN("123456789");

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the Insured Demo Info (Insurd LName, FName, Rlshp, DOB, Sex and SSN) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.insuredLNameInput().getAttribute("value").trim().equals(insurdLName), "        Insured LName: " + insurdLName + " should show.");
		Assert.assertTrue(patientDemographics.insuredFNameInput().getAttribute("value").trim().equals(insurdFName), "        Insured FName: " + insurdFName + " should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.rlshpDropDown(), "self"), "        Relationship: self should be selected.");
		Assert.assertTrue(patientDemographics.insuredDOBInput().getAttribute("value").trim().equals(insurdDOB), "        Insured DOB: " + insurdDOB + " should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.insuredSexDropDown(), "Female"), "        Insured Sex: Female should be selected.");
		Assert.assertTrue(patientDemographics.insuredSSNInput().getAttribute("value").trim().equals("123-45-6789"), "        Insured SSN: 123-45-6789 should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Update Insurance Insured Demo Info")
	@Parameters({"email", "password"})
	public void testRPM_591(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_591 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and tab out");
		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Insured Demo Info (Insurd LName, FName, Rlshp, DOB, Sex and SSN)");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		randomCharacter = new RandomCharacter();
		timeStamp = new TimeStamp();
		String insurdFName = randomCharacter.getRandomAlphaString(6);
		String insurdLName = randomCharacter.getRandomAlphaString(6);
		String insurdDOB = timeStamp.getPreviousDate("MM/dd/yyyy", 600);

		patientDemographics.setInsurdFName(insurdFName);
		patientDemographics.setInsurdLName(insurdLName);
		selectItem(patientDemographics.rlshpDropDown(), "self");
		patientDemographics.setInsurdDOB(insurdDOB);
		selectItem(patientDemographics.insuredSexDropDown(), "Female");
		patientDemographics.setInsurdSSN("123456789");

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the Insured Demo Info (Insurd LName, FName, Rlshp, DOB, Sex and SSN) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.insuredLNameInput().getAttribute("value").trim().equals(insurdLName), "        Insured LName: " + insurdLName + " should show.");
		Assert.assertTrue(patientDemographics.insuredFNameInput().getAttribute("value").trim().equals(insurdFName), "        Insured FName: " + insurdFName + " should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.rlshpDropDown(), "self"), "        Relationship: self should be selected.");
		Assert.assertTrue(patientDemographics.insuredDOBInput().getAttribute("value").trim().equals(insurdDOB), "        Insured DOB: " + insurdDOB + " should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.insuredSexDropDown(), "Female"), "        Insured Sex: Female should be selected.");
		Assert.assertTrue(patientDemographics.insuredSSNInput().getAttribute("value").trim().equals("123-45-6789"), "        Insured SSN: 123-45-6789 should show.");

		logger.info("*** Step 6 Actions: - Update Insured Demo Info (Insurd LName, FName, Rlshp, DOB, Sex and SSN)");
		randomCharacter = new RandomCharacter();
		timeStamp = new TimeStamp();
		insurdFName = randomCharacter.getRandomAlphaString(6);
		insurdLName = randomCharacter.getRandomAlphaString(6);
		insurdDOB = timeStamp.getPreviousDate("MM/dd/yyyy", 400);

		patientDemographics.setInsurdFName(insurdFName);
		patientDemographics.setInsurdLName(insurdLName);
		selectItem(patientDemographics.rlshpDropDown(), "child");
		patientDemographics.setInsurdDOB(insurdDOB);
		selectItem(patientDemographics.insuredSexDropDown(), "Male");
		patientDemographics.setInsurdSSN("123458989");

		logger.info("*** Step 6 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 7 Actions: - Load the EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 7 Expected Results: - Verify that the Updated Insured Demo Info (Insurd LName, FName, Rlshp, DOB, Sex and SSN) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.insuredLNameInput().getAttribute("value").trim().equals(insurdLName), "        Updated Insured LName: " + insurdLName + " should show.");
		Assert.assertTrue(patientDemographics.insuredFNameInput().getAttribute("value").trim().equals(insurdFName), "        Updated Insured FName: " + insurdFName + " should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.rlshpDropDown(), "child"), "        Updated Relationship: child should be selected.");
		Assert.assertTrue(patientDemographics.insuredDOBInput().getAttribute("value").trim().equals(insurdDOB), "        Updated Insured DOB: " + insurdDOB + " should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.insuredSexDropDown(), "Male"), "        Updated Insured Sex: Male should be selected.");
		Assert.assertTrue(patientDemographics.insuredSSNInput().getAttribute("value").trim().equals("123-45-8989"), "        Updated Insured SSN: 123-45-8989 should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Insurance Insured Address Info display")
	@Parameters({"email", "password"})
	public void testRPM_586(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_586 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and tab out");
		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Insured Address Info (Insurd Addr1, Addr2 and Zip)");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		randomCharacter = new RandomCharacter();
		String insurdAddr1 = randomCharacter.getRandomAlphaString(6);
		String insurdAddr2 = randomCharacter.getRandomAlphaString(6);

		patientDemographics.setInsurdAddr1(insurdAddr1);
		patientDemographics.setInsurdAddr2(insurdAddr2);
		patientDemographics.setInsurdZip("92111");

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the Insured Address Info (Insurd Addr1, Addr2, City, State, Zip and Country) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.insuredAddr1Input().getAttribute("value").trim().equals(insurdAddr1), "        Insured Addr1: " + insurdAddr1 + " should show.");
		Assert.assertTrue(patientDemographics.insuredAddr2Input().getAttribute("value").trim().equals(insurdAddr2), "        Insured Addr2: " + insurdAddr2 + " should show.");
		Assert.assertTrue(patientDemographics.insuredCityInput().getAttribute("value").trim().equals("SAN DIEGO"), "        Insured City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.insuredStateDropDown(), "CA"), "        Insured State: CA should be selected.");
		Assert.assertTrue(patientDemographics.insuredZipInput().getAttribute("value").trim().equals("92111"), "        Insured ZIP: 92111 should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.insuredCountryDropDown(), "USA"), "        Insured Country: USA should be selected.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Update Insurance Insured Address Info")
	@Parameters({"email", "password"})
	public void testRPM_592(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_592 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and tab out");
		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Insured Address Info (Insurd Addr1, Addr2 and Zip)");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		randomCharacter = new RandomCharacter();
		String insurdAddr1 = randomCharacter.getRandomAlphaString(6);
		String insurdAddr2 = randomCharacter.getRandomAlphaString(6);

		patientDemographics.setInsurdAddr1(insurdAddr1);
		patientDemographics.setInsurdAddr2(insurdAddr2);
		patientDemographics.setInsurdZip("92111");

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the Insured Address Info (Insurd Addr1, Addr2, City, State, Zip and Country) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.insuredAddr1Input().getAttribute("value").trim().equals(insurdAddr1), "        Insured Addr1: " + insurdAddr1 + " should show.");
		Assert.assertTrue(patientDemographics.insuredAddr2Input().getAttribute("value").trim().equals(insurdAddr2), "        Insured Addr2: " + insurdAddr2 + " should show.");
		Assert.assertTrue(patientDemographics.insuredCityInput().getAttribute("value").trim().equals("SAN DIEGO"), "        Insured City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.insuredStateDropDown(), "CA"), "        Insured State: CA should be selected.");
		Assert.assertTrue(patientDemographics.insuredZipInput().getAttribute("value").trim().equals("92111"), "        Insured ZIP: 92111 should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.insuredCountryDropDown(), "USA"), "        Insured Country: USA should be selected.");

		logger.info("*** Step 6 Actions: - Update Insured Address Info (Insurd Addr1, Addr2 and Zip)");
		randomCharacter = new RandomCharacter();
		insurdAddr1 = randomCharacter.getRandomAlphaString(6);
		insurdAddr2 = randomCharacter.getRandomAlphaString(6);

		patientDemographics.setInsurdAddr1(insurdAddr1);
		patientDemographics.setInsurdAddr2(insurdAddr2);
		patientDemographics.insuredCityInput().clear();
		new Select(patientDemographics.insuredStateDropDown()).selectByValue(" ");
		patientDemographics.setInsurdZip("92591");

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the updated Insured Address Info (Insurd Addr1, Addr2, City, State, Zip and Country) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.insuredAddr1Input().getAttribute("value").trim().equals(insurdAddr1), "        Updated Insured Addr1: " + insurdAddr1 + " should show.");
		Assert.assertTrue(patientDemographics.insuredAddr2Input().getAttribute("value").trim().equals(insurdAddr2), "        Updated Insured Addr2: " + insurdAddr2 + " should show.");
		Assert.assertTrue(patientDemographics.insuredCityInput().getAttribute("value").trim().equals("TEMECULA"), "        Updated Insured City: TEMECULA should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.insuredStateDropDown(), "CA"), "        Updated Insured State: CA should be selected.");
		Assert.assertTrue(patientDemographics.insuredZipInput().getAttribute("value").trim().equals("92591"), "        Updated Insured ZIP: 92591 should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.insuredCountryDropDown(), "USA"), "        Updated Insured Country: USA should be selected.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Insurance Insured Contact Info display")
	@Parameters({"email", "password"})
	public void testRPM_587(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_587 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and tab out");
		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Insured Contact Info (Hm Phn and Wk Phn)");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		String insuredHwPhn = "(619) 791-0679";
		String insuredWkPhn = "(858) 612-7788";
		patientDemographics.setInsurdHmPhn(insuredHwPhn);
		patientDemographics.setInsurdWkPhn(insuredWkPhn);

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the Insured Contact Info (Hm Phn and Wk Phn) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.insuredHmPhnInput().getAttribute("value").trim().equals(insuredHwPhn), "        Insured Hm Phn: " + insuredHwPhn + " should show.");
		Assert.assertTrue(patientDemographics.insuredWkPhnInput().getAttribute("value").trim().equals(insuredWkPhn), "        Insured Wk Phn: " + insuredWkPhn + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Update Insurance Insured Contact Info")
	@Parameters({"email", "password"})
	public void testRPM_593(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_593 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and tab out");
		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Insured Contact Info (Hm Phn and Wk Phn)");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		String insuredHwPhn = "(619) 791-0679";
		String insuredWkPhn = "(858) 612-7788";
		patientDemographics.setInsurdHmPhn(insuredHwPhn);
		patientDemographics.setInsurdWkPhn(insuredWkPhn);

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the Insured Contact Info (Hm Phn and Wk Phn) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.insuredHmPhnInput().getAttribute("value").trim().equals(insuredHwPhn), "        Insured Hm Phn: " + insuredHwPhn + " should show.");
		Assert.assertTrue(patientDemographics.insuredWkPhnInput().getAttribute("value").trim().equals(insuredWkPhn), "        Insured Wk Phn: " + insuredWkPhn + " should show.");

		logger.info("*** Step 6 Actions: - Update Insured Contact Info (Hm Phn and Wk Phn)");
		insuredHwPhn = "(761) 791-5678";
		insuredWkPhn = "(941) 612-9901";
		patientDemographics.setInsurdHmPhn(insuredHwPhn);
		patientDemographics.setInsurdWkPhn(insuredWkPhn);

		logger.info("*** Step 6 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 7 Actions: - Load the EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 7 Expected Results: - Verify that the updated Insured Contact Info (Hm Phn and Wk Phn) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.insuredHmPhnInput().getAttribute("value").trim().equals(insuredHwPhn), "        Updated Insured Hm Phn: " + insuredHwPhn + " should show.");
		Assert.assertTrue(patientDemographics.insuredWkPhnInput().getAttribute("value").trim().equals(insuredWkPhn), "        Updated Insured Wk Phn: " + insuredWkPhn + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Insurance Other Infos display")
	@Parameters({"email", "password"})
	public void testRPM_588(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_588 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and tab out");
		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Insured Payor and other Info (Claim Notes, Internal Notes, Other Info1, Other Info2, Other Info3 and Case ID)");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		String claimNotes = "Claim Notes " + randomCharacter.getRandomAlphaString(6);
		String internalNotes = "Internal Notes " + randomCharacter.getRandomAlphaString(5);
		String otherInfo1 = "Other Info1 " + randomCharacter.getRandomAlphaString(4);
		String otherInfo2 = "Other Info2 " + randomCharacter.getRandomAlphaString(4);
		String otherInfo3 = "Other Info3 " + randomCharacter.getRandomAlphaString(4);
		String caseId = "Case ID " + randomCharacter.getRandomAlphaNumericString(8);

		patientDemographics.setClaimNotes(claimNotes);
		patientDemographics.setInternalNotes(internalNotes);
		patientDemographics.setOtherInfo1(otherInfo1);
		patientDemographics.setOtherInfo2(otherInfo2);
		patientDemographics.setOtherInfo3(otherInfo3);
		patientDemographics.setCaseId(caseId);

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the Insured Payor and other Info (Claim Notes, Internal Notes, Other Info1, Other Info2, Other Info3 and Case ID) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.claimNotesInput().getAttribute("value").trim().equals(claimNotes), "        Claim Notes: " + claimNotes + " should show.");
		Assert.assertTrue(patientDemographics.internalNotesInput().getAttribute("value").trim().equals(internalNotes), "        Internal Notes: " + internalNotes + " should show.");

		Assert.assertTrue(patientDemographics.otherInfo1Input().getAttribute("value").trim().equals(otherInfo1), "        Other Info1: " + otherInfo1 + " should show.");
		Assert.assertTrue(patientDemographics.otherInfo2Input().getAttribute("value").trim().equals(otherInfo2), "        Other Info2: " + otherInfo2 + " should show.");
		Assert.assertTrue(patientDemographics.otherInfo3Input().getAttribute("value").trim().equals(otherInfo3.toUpperCase()), "        Other Info3: " + otherInfo3.toUpperCase() + " should show.");
		Assert.assertTrue(patientDemographics.claimNotesInput().getAttribute("value").trim().equals(claimNotes), "        Claim Notes: " + claimNotes + " should show.");
		Assert.assertTrue(patientDemographics.caseIdInput().getAttribute("value").trim().equals(caseId), "        Case ID: " + caseId + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Update Insurance Other Infos")
	@Parameters({"email", "password"})
	public void testRPM_594(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_594 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and tab out");
		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Insured Payor and other Info (Claim Notes, Internal Notes, Other Info1, Other Info2, Other Info3 and Case ID)");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		String claimNotes = "Claim Notes " + randomCharacter.getRandomAlphaString(6);
		String internalNotes = "Internal Notes " + randomCharacter.getRandomAlphaString(5);
		String otherInfo1 = "Other Info1 " + randomCharacter.getRandomAlphaString(4);
		String otherInfo2 = "Other Info2 " + randomCharacter.getRandomAlphaString(4);
		String otherInfo3 = "Other Info3 " + randomCharacter.getRandomAlphaString(4);
		String caseId = "Case ID " + randomCharacter.getRandomAlphaNumericString(8);

		patientDemographics.setClaimNotes(claimNotes);
		patientDemographics.setInternalNotes(internalNotes);
		patientDemographics.setOtherInfo1(otherInfo1);
		patientDemographics.setOtherInfo2(otherInfo2);
		patientDemographics.setOtherInfo3(otherInfo3);
		patientDemographics.setCaseId(caseId);

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the Insured Payor and other Info (Claim Notes, Internal Notes, Other Info1, Other Info2, Other Info3 and Case ID) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.claimNotesInput().getAttribute("value").trim().equals(claimNotes), "        Claim Notes: " + claimNotes + " should show.");
		Assert.assertTrue(patientDemographics.internalNotesInput().getAttribute("value").trim().equals(internalNotes), "        Internal Notes: " + internalNotes + " should show.");

		Assert.assertTrue(patientDemographics.otherInfo1Input().getAttribute("value").trim().equals(otherInfo1), "        Other Info1: " + otherInfo1 + " should show.");
		Assert.assertTrue(patientDemographics.otherInfo2Input().getAttribute("value").trim().equals(otherInfo2), "        Other Info2: " + otherInfo2 + " should show.");
		Assert.assertTrue(patientDemographics.otherInfo3Input().getAttribute("value").trim().equals(otherInfo3.toUpperCase()), "        Other Info3: " + otherInfo3.toUpperCase() + " should show.");
		Assert.assertTrue(patientDemographics.claimNotesInput().getAttribute("value").trim().equals(claimNotes), "        Claim Notes: " + claimNotes + " should show.");
		Assert.assertTrue(patientDemographics.caseIdInput().getAttribute("value").trim().equals(caseId), "        Case ID: " + caseId + " should show.");

		logger.info("*** Step 6 Actions: - Update other Info (Claim Notes, Internal Notes, Other Info1, Other Info2, Other Info3 and Case ID)");

		claimNotes = "Claim Notes " + randomCharacter.getRandomAlphaString(6);
		internalNotes = "Internal Notes " + randomCharacter.getRandomAlphaString(5);
		otherInfo1 = "Other Info1 " + randomCharacter.getRandomAlphaString(4);
		otherInfo2 = "Other Info2 " + randomCharacter.getRandomAlphaString(4);
		otherInfo3 = "Other Info3 " + randomCharacter.getRandomAlphaString(4);
		caseId = "Case ID " + randomCharacter.getRandomAlphaNumericString(8);

		patientDemographics.setClaimNotes(claimNotes);
		patientDemographics.setInternalNotes(internalNotes);
		patientDemographics.setOtherInfo1(otherInfo1);
		patientDemographics.setOtherInfo2(otherInfo2);
		patientDemographics.setOtherInfo3(otherInfo3);
		patientDemographics.setCaseId(caseId);

		logger.info("*** Step 6 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 7 Actions: - Load the EPI in EPI field");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 7 Expected Results: - Verify that the updated other Info (Claim Notes, Internal Notes, Other Info1, Other Info2, Other Info3 and Case ID) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.claimNotesInput().getAttribute("value").trim().equals(claimNotes), "        Updated Claim Notes: " + claimNotes + " should show.");
		Assert.assertTrue(patientDemographics.internalNotesInput().getAttribute("value").trim().equals(internalNotes), "        Updated Internal Notes: " + internalNotes + " should show.");

		Assert.assertTrue(patientDemographics.otherInfo1Input().getAttribute("value").trim().equals(otherInfo1), "        Updated Other Info1: " + otherInfo1 + " should show.");
		Assert.assertTrue(patientDemographics.otherInfo2Input().getAttribute("value").trim().equals(otherInfo2), "        Updated Other Info2: " + otherInfo2 + " should show.");
		Assert.assertTrue(patientDemographics.otherInfo3Input().getAttribute("value").trim().equals(otherInfo3.toUpperCase()), "        Updated Other Info3: " + otherInfo3.toUpperCase() + " should show.");
		Assert.assertTrue(patientDemographics.claimNotesInput().getAttribute("value").trim().equals(claimNotes), "        Updated Claim Notes: " + claimNotes + " should show.");
		Assert.assertTrue(patientDemographics.caseIdInput().getAttribute("value").trim().equals(caseId), "        Updated Case ID: " + caseId + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Insurance Employer Address Info display")
	@Parameters({"email", "password"})
	public void testRPM_589(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_589 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and tab out");
		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Insured Employer Address Info (Empl Name, Status, Addr1, Addr2, Zip)");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		String emplName = "EmplName " + randomCharacter.getRandomAlphaString(6);
		String emplAddr1 = "EmplAddr1 " + randomCharacter.getRandomAlphaString(5);
		String emplAddr2 = "EmplAddr2 " + randomCharacter.getRandomAlphaString(4);
		String emplZip = "92129";

		patientDemographics.setEmplName(emplName);
		selectItem(patientDemographics.emplStatusDropDown(), "1 Empl Full");
		patientDemographics.setEmplAddr1(emplAddr1);
		patientDemographics.setEmplAddr2(emplAddr2);
		patientDemographics.setEmplZip(emplZip);

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the Insured Employer Address Info (Empl Name, Status, Addr1, Addr2, City, State, Zip and Country) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.emplNameInput().getAttribute("value").trim().equals(emplName), "        Empl Name: " + emplName + " should show.");
		Assert.assertTrue(patientDemographics.emplAddr1Input().getAttribute("value").trim().equals(emplAddr1), "        Empl Addr1: " + emplAddr1 + " should show.");
		Assert.assertTrue(patientDemographics.emplAddr2Input().getAttribute("value").trim().equals(emplAddr2), "        Empl Addr2: " + emplAddr2 + " should show.");
		Assert.assertTrue(patientDemographics.emplCityInput().getAttribute("value").trim().equals("SAN DIEGO"), "        Empl City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.emplStateDropDown(), "CA"), "        Empl State: CA should show.");
		Assert.assertTrue(patientDemographics.emplZipInput().getAttribute("value").trim().equals(emplZip), "        Empl Zip: " + emplZip + " should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.emplCountryDropDown(), "USA"), "        Empl Country: CA should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.emplStatusDropDown(), "1 Empl Full"), "        Empl Status: 1 Empl Full should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Update Insurance Employer Address Info")
	@Parameters({"email", "password"})
	public void testRPM_595(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_595 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and tab out");
		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Insured Employer Address Info (Empl Name, Status, Addr1, Addr2, Zip)");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		String emplName = "EmplName " + randomCharacter.getRandomAlphaString(6);
		String emplAddr1 = "EmplAddr1 " + randomCharacter.getRandomAlphaString(5);
		String emplAddr2 = "EmplAddr2 " + randomCharacter.getRandomAlphaString(4);
		String emplZip = "92129";

		patientDemographics.setEmplName(emplName);
		selectItem(patientDemographics.emplStatusDropDown(), "1 Empl Full");
		patientDemographics.setEmplAddr1(emplAddr1);
		patientDemographics.setEmplAddr2(emplAddr2);
		patientDemographics.setEmplZip(emplZip);

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the Insured Employer Address Info (Empl Name, Status, Addr1, Addr2, City, State, Zip and Country) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.emplNameInput().getAttribute("value").trim().equals(emplName), "        Empl Name: " + emplName + " should show.");
		Assert.assertTrue(patientDemographics.emplAddr1Input().getAttribute("value").trim().equals(emplAddr1), "        Empl Addr1: " + emplAddr1 + " should show.");
		Assert.assertTrue(patientDemographics.emplAddr2Input().getAttribute("value").trim().equals(emplAddr2), "        Empl Addr2: " + emplAddr2 + " should show.");
		Assert.assertTrue(patientDemographics.emplCityInput().getAttribute("value").trim().equals("SAN DIEGO"), "        Empl City: SAN DIEGO should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.emplStateDropDown(), "CA"), "        Empl State: CA should show.");
		Assert.assertTrue(patientDemographics.emplZipInput().getAttribute("value").trim().equals(emplZip), "        Empl Zip: " + emplZip + " should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.emplCountryDropDown(), "USA"), "        Empl Country: USA should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.emplStatusDropDown(), "1 Empl Full"), "        Empl Status: 1 Empl Full should show.");

		logger.info("*** Step 6 Actions: - Enter updated Insured Employer Address Info (Empl Name, Status, Addr1, Addr2, Zip)");

		emplName = "EmplName " + randomCharacter.getRandomAlphaString(3);
		emplAddr1 = "EmplAddr1 " + randomCharacter.getRandomAlphaString(4);
		emplAddr2 = "EmplAddr2 " + randomCharacter.getRandomAlphaString(5);
		emplZip = "92591";

		patientDemographics.setEmplName(emplName);
		selectItem(patientDemographics.emplStatusDropDown(), "2 Empl Part");
		patientDemographics.setEmplAddr1(emplAddr1);
		patientDemographics.setEmplAddr2(emplAddr2);
		patientDemographics.emplCityInput().clear();
		new Select(patientDemographics.emplStateDropDown()).selectByValue(" ");
		patientDemographics.setEmplZip(emplZip);

		logger.info("*** Step 6 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 7 Actions: - Load the EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 7 Expected Results: - Verify that the updated Insured Employer Address Info (Empl Name, Status, Addr1, Addr2, City, State, Zip and Country) display properly");
		Assert.assertTrue(patientDemographics.emplNameInput().getAttribute("value").trim().equals(emplName), "        Updated Empl Name: " + emplName + " should show.");
		Assert.assertTrue(patientDemographics.emplAddr1Input().getAttribute("value").trim().equals(emplAddr1), "        Updated Empl Addr1: " + emplAddr1 + " should show.");
		Assert.assertTrue(patientDemographics.emplAddr2Input().getAttribute("value").trim().equals(emplAddr2), "        Updated Empl Addr2: " + emplAddr2 + " should show.");
		Assert.assertTrue(patientDemographics.emplCityInput().getAttribute("value").trim().equals("TEMECULA"), "        Updated Empl City: TEMECULA should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.emplStateDropDown(), "CA"), "        Updated Empl State: CA should show.");
		Assert.assertTrue(patientDemographics.emplZipInput().getAttribute("value").trim().equals(emplZip), "        Updated Empl Zip: " + emplZip + " should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.emplCountryDropDown(), "USA"), "        Updated Empl Country: USA should show.");
		Assert.assertTrue(isDropdownItemSelected(patientDemographics.emplStatusDropDown(), "2 Empl Part"), "        Updated Empl Status: 2 Empl Part should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Insurance Employer Contact Info display")
	@Parameters({"email", "password"})
	public void testRPM_590(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_590 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and tab out");
		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Insured Employer Contact Info (Empl Phone and Fax)");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		String emplPhone = "(858) 436-4298";
		String emplFax = "(858) 636-5198";

		patientDemographics.setEmplPhone(emplPhone);
		patientDemographics.setEmplFax(emplFax);

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the Insured Employer Contact Info (Empl Phone and Fax) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.emplPhoneInput().getAttribute("value").trim().equals(emplPhone), "        Empl Phone: " + emplPhone + " should show.");
		Assert.assertTrue(patientDemographics.emplFaxInput().getAttribute("value").trim().equals(emplFax), "        Empl Fax: " + emplFax + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Update Insurance Employer Contact Info")
	@Parameters({"email", "password"})
	public void testRPM_596(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_596 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and tab out");
		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Insured Employer Contact Info (Empl Phone and Fax)");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		String emplPhone = "(858) 436-4298";
		String emplFax = "(858) 636-5198";

		patientDemographics.setEmplPhone(emplPhone);
		patientDemographics.setEmplFax(emplFax);

		logger.info("*** Step 4 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 5 Expected Results: - Verify that the Insured Employer Contact Info (Empl Phone and Fax) display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");

		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");
		Assert.assertTrue(patientDemographics.emplPhoneInput().getAttribute("value").trim().equals(emplPhone), "        Empl Phone: " + emplPhone + " should show.");
		Assert.assertTrue(patientDemographics.emplFaxInput().getAttribute("value").trim().equals(emplFax), "        Empl Fax: " + emplFax + " should show.");

		logger.info("*** Step 6 Actions: - Enter Updated Insured Employer Contact Info (Empl Phone and Fax)");

		emplPhone = "(619) 736-4294";
		emplFax = "(619) 936-5193";

		patientDemographics.setEmplPhone(emplPhone);
		patientDemographics.setEmplFax(emplFax);

		logger.info("*** Step 6 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 7 Actions: - Load the EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 7 Expected Results: - Verify that the Updated Insured Employer Contact Info (Empl Phone and Fax) display properly");
		Assert.assertTrue(patientDemographics.emplPhoneInput().getAttribute("value").trim().equals(emplPhone), "        Updated Empl Phone: " + emplPhone + " should show.");
		Assert.assertTrue(patientDemographics.emplFaxInput().getAttribute("value").trim().equals(emplFax), "        Updated Empl Fax: " + emplFax + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Suspend for Bad Address")
	@Parameters({"email", "password"})
	public void testRPM_14(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_14 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and tab out");
		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Insured Employer Contact Info (Empl Phone and Fax)");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 5 Actions: - Check Suspend for Bad Address checkbox");
		selectCheckBox(patientDemographics.suspBadAddrCheckBox());

		logger.info("*** Step 5 Expected Results: - Verify that the Create Suspend Reason popup window shows");
		Assert.assertTrue(patientDemographics.suspPopEditorTitle().getText().trim().contains("Create Suspend Reason"), "        Create Suspend Reason popup window should show.");

		logger.info("*** Step 6 Actions: - Enter Suspend Note");
		String suspNote = "Suspend Note " + randomCharacter.getRandomAlphaString(6);
		patientDemographics.setSuspNote(suspNote);

		logger.info("*** Step 6 Actions: - Click Ok button");
		patientDemographics.suspPopOkBtn().click();

		logger.info("*** Step 6 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 7 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 7 Expected Results: - Verify that the Patient Demographics Info display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 7 Expected Results: - Verify that the Suspend for Bad Address Info display properly");
		Assert.assertTrue(patientDemographics.suspSummaryText().getText().trim().contains("Bad Address"), "        Summary: Bad Address should show.");

		logger.info("*** Step 8 Actions: - Click Show Detail link");
		patientDemographics.suspShowDetailLink().click();

		logger.info("*** Step 8 Actions: - Click Suspend Note link");
		patientDemographics.suspNoteLink().click();

		logger.info("*** Step 8 Expected Results: - Verify that the Edit Suspend Reason popup window shows");
		Assert.assertTrue(patientDemographics.suspPopEditorTitle().getText().trim().contains("Edit Suspend Reason"), "        Edit Suspend Reason popup window should show.");

		logger.info("*** Step 8 Expected Results: - Verify that the Suspend Date, Reason Code and Note show properly");
		timeStamp = new TimeStamp(driver);
		String currDate = timeStamp.getCurrentDate();
		Assert.assertTrue(patientDemographics.suspPopDateText().trim().contains(currDate), "        Suspend Date: " + currDate + " should show.");
		Assert.assertFalse(patientDemographics.suspPopReasonDropDown().isEnabled(),  "        Suspend Reason DropDown should be disabled");
		Assert.assertTrue(patientDemographics.suspPopNoteText().trim().contains(suspNote), "        Suspend Note: " + suspNote + " should show.");

		logger.info("*** Step 9 Actions: - Click Cancel button");
		patientDemographics.suspPopCancelBtn().click();

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Fix Suspend for Bad Address")
	@Parameters({"email", "password"})
	public void testRPM_598(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_598 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and tab out");
		patientDemographics.setPtZip("92129");

		logger.info("*** Step 4 Actions: - Enter Insured Employer Contact Info (Empl Phone and Fax)");
		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 5 Actions: - Check Suspend for Bad Address checkbox");
		selectCheckBox(patientDemographics.suspBadAddrCheckBox());

		logger.info("*** Step 5 Expected Results: - Verify that the Create Suspend Reason popup window shows");
		Assert.assertTrue(patientDemographics.suspPopEditorTitle().getText().trim().contains("Create Suspend Reason"), "        Create Suspend Reason popup window should show.");

		logger.info("*** Step 6 Actions: - Enter Suspend Note");
		String suspNote = "Suspend Note " + randomCharacter.getRandomAlphaString(6);
		patientDemographics.setSuspNote(suspNote);

		logger.info("*** Step 6 Actions: - Click Ok button");
		patientDemographics.suspPopOkBtn().click();

		logger.info("*** Step 6 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 7 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 7 Expected Results: - Verify that the Patient Demographics Info display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 7 Expected Results: - Verify that the Suspend for Bad Address Info display properly");
		Assert.assertTrue(patientDemographics.suspSummaryText().getText().trim().contains("Bad Address"), "        Summary: Bad Address should show.");

		logger.info("*** Step 8 Actions: - Click Show Detail link");
		patientDemographics.suspShowDetailLink().click();

		logger.info("*** Step 8 Actions: - Click Suspend Note link");
		patientDemographics.suspNoteLink().click();

		logger.info("*** Step 8 Expected Results: - Verify that the Edit Suspend Reason popup window shows");
		Assert.assertTrue(patientDemographics.suspPopEditorTitle().getText().trim().contains("Edit Suspend Reason"), "        Edit Suspend Reason popup window should show.");

		logger.info("*** Step 8 Expected Results: - Verify that the Suspend Date, Reason Code and Note show properly");
		timeStamp = new TimeStamp(driver);
		String currDate = timeStamp.getCurrentDate();
		Assert.assertTrue(patientDemographics.suspPopDateText().trim().contains(currDate), "        Suspend Date: " + currDate + " should show.");
		Assert.assertFalse(patientDemographics.suspPopReasonDropDown().isEnabled(),  "        Suspend Reason DropDown should be disabled");
		Assert.assertTrue(patientDemographics.suspPopNoteText().trim().contains(suspNote), "        Suspend Note: " + suspNote + " should show.");

		logger.info("*** Step 9 Actions: - Click Cancel button in Edit Suspend Reason popup window");
		patientDemographics.suspPopCancelBtn().click();

		logger.info("*** Step 9 Actions: - Click Fix checkbox in Suspended Reason Grid");
		selectCheckBox(patientDemographics.suspFixCheckBox());

		logger.info("*** Step 9 Expected Results: - Verify that the Edit Suspend Reason popup window shows");
		Assert.assertTrue(patientDemographics.suspPopEditorTitle().getText().trim().contains("Edit Suspend Reason"), "        Edit Suspend Reason popup window should show.");

		logger.info("*** Step 10 Actions: - Click Fix checkbox in Edit Suspend Reason popup window");
		selectCheckBox(patientDemographics.suspPopFixCheckBox());

		logger.info("*** Step 10 Actions: - Click Ok button in Edit Suspend Reason popup window");
		patientDemographics.suspPopOkBtn().click();

		logger.info("*** Step 10 Actions: - Click Submit button");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 11 Actions: - Load the EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 11 Expected Results: - Verify that the Patient Demographics Info display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 11 Expected Results: - Verify that the Suspend for Bad Address checkbox is unchecked");
		Assert.assertFalse(patientDemographics.suspBadAddrCheckBox().isSelected(), "        The Suspend for Bad Address checkbox should be unchecked.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Suspend Patient Insurance")
	@Parameters({"email", "password"})
	public void testRPM_49(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_49 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 5 Actions: - Check Suspended checkbox in Insurance Information tab");
		selectCheckBox(patientDemographics.suspendedCheckBox());

		logger.info("*** Step 5 Expected Results: - Verify that the Create Suspend Reason popup window shows");
		Assert.assertTrue(patientDemographics.suspPopEditorTitle().getText().trim().contains("Create Suspend Reason"), "        Create Suspend Reason popup window should show.");

		logger.info("*** Step 6 Actions: - Select Suspend Reason Code");
		selectItem(patientDemographics.suspPopReasonDropDown(), "Review New Record");

		logger.info("*** Step 6 Actions: - Enter Suspend Note");
		String suspNote = "Suspend Note " + randomCharacter.getRandomAlphaString(6);
		patientDemographics.setSuspNote(suspNote);

		logger.info("*** Step 6 Actions: - Click Ok button in Create Supspend Reason popup window");
		patientDemographics.suspPopOkBtn().click();

		logger.info("*** Step 6 Actions: - Click Submit button in Patient Demographics screen");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 7 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 7 Expected Results: - Verify that the Patient Demographics Info display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 7 Expected Results: - Verify that the Suspended for Review New Record Info display properly");
		Assert.assertTrue(patientDemographics.suspSummaryText().getText().trim().contains("Review New Record"), "        Summary: Review New Record should show.");

		logger.info("*** Step 8 Actions: - Click Show Detail link");
		patientDemographics.suspShowDetailLink().click();

		logger.info("*** Step 8 Actions: - Click Suspend Note link");
		patientDemographics.suspNoteLink().click();

		logger.info("*** Step 8 Expected Results: - Verify that the Edit Suspend Reason popup window shows");
		Assert.assertTrue(patientDemographics.suspPopEditorTitle().getText().trim().contains("Edit Suspend Reason"), "        Edit Suspend Reason popup window should show.");

		logger.info("*** Step 8 Expected Results: - Verify that the Suspend Date, Reason Code and Note show properly");
		timeStamp = new TimeStamp(driver);
		String currDate = timeStamp.getCurrentDate();
		Assert.assertTrue(patientDemographics.suspPopDateText().trim().contains(currDate), "        Suspend Date: " + currDate + " should show.");
		Assert.assertFalse(patientDemographics.suspPopReasonDropDown().isEnabled(),  "        Suspend Reason DropDown should be disabled");
		Assert.assertTrue(patientDemographics.suspPopNoteText().trim().contains(suspNote), "        Suspend Note: " + suspNote + " should show.");

		logger.info("*** Step 9 Actions: - Click Cancel button in Edit Supspend Reason popup window");
		patientDemographics.suspPopCancelBtn().click();

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Fix Suspend Patient Insurance")
	@Parameters({"email", "password"})
	public void testRPM_599(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_599 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 5 Actions: - Check Suspended checkbox in Insurance Information tab");
		selectCheckBox(patientDemographics.suspendedCheckBox());

		logger.info("*** Step 5 Expected Results: - Verify that the Create Suspend Reason popup window shows");
		Assert.assertTrue(patientDemographics.suspPopEditorTitle().getText().trim().contains("Create Suspend Reason"), "        Create Suspend Reason popup window should show.");

		logger.info("*** Step 6 Actions: - Select Suspend Reason Code");
		selectItem(patientDemographics.suspPopReasonDropDown(), "Review New Record");

		logger.info("*** Step 6 Actions: - Enter Suspend Note");
		String suspNote = "Suspend Note " + randomCharacter.getRandomAlphaString(6);
		patientDemographics.setSuspNote(suspNote);

		logger.info("*** Step 6 Actions: - Click Ok button in Create Supspend Reason popup window");
		patientDemographics.suspPopOkBtn().click();

		logger.info("*** Step 6 Actions: - Click Submit button in Patient Demographics screen");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 7 Actions: - Load the newly created EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 7 Expected Results: - Verify that the Patient Demographics Info display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 7 Expected Results: - Verify that the Suspended for Review New Record Info display properly");
		Assert.assertTrue(patientDemographics.suspSummaryText().getText().trim().contains("Review New Record"), "        Summary: Review New Record should show.");

		logger.info("*** Step 8 Actions: - Click Show Detail link");
		patientDemographics.suspShowDetailLink().click();

		logger.info("*** Step 8 Actions: - Click Suspend Note link");
		patientDemographics.suspNoteLink().click();

		logger.info("*** Step 8 Expected Results: - Verify that the Edit Suspend Reason popup window shows");
		Assert.assertTrue(patientDemographics.suspPopEditorTitle().getText().trim().contains("Edit Suspend Reason"), "        Edit Suspend Reason popup window should show.");

		logger.info("*** Step 8 Expected Results: - Verify that the Suspend Date, Reason Code and Note show properly");
		timeStamp = new TimeStamp(driver);
		String currDate = timeStamp.getCurrentDate();
		Assert.assertTrue(patientDemographics.suspPopDateText().trim().contains(currDate), "        Suspend Date: " + currDate + " should show.");
		Assert.assertFalse(patientDemographics.suspPopReasonDropDown().isEnabled(),  "        Suspend Reason DropDown should be disabled");
		Assert.assertTrue(patientDemographics.suspPopNoteText().trim().contains(suspNote), "        Suspend Note: " + suspNote + " should show.");

		logger.info("*** Step 9 Actions: - Click Cancel button in Edit Supspend Reason popup window");
		patientDemographics.suspPopCancelBtn().click();

		logger.info("*** Step 10 Actions: - Click Fix checkbox in Suspended Reason Grid");
		selectCheckBox(patientDemographics.suspFixCheckBox());

		logger.info("*** Step 10 Expected Results: - Verify that the Edit Suspend Reason popup window shows");
		Assert.assertTrue(patientDemographics.suspPopEditorTitle().getText().trim().contains("Edit Suspend Reason"), "        Edit Suspend Reason popup window should show.");

		logger.info("*** Step 11 Actions: - Click Fix checkbox in Edit Suspend Reason popup window");
		selectCheckBox(patientDemographics.suspPopFixCheckBox());

		logger.info("*** Step 11 Actions: - Click Ok button in Edit Suspend Reason popup window");
		patientDemographics.suspPopOkBtn().click();

		logger.info("*** Step 11 Actions: - Click Submit button in Patient Demographics screen");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 12 Actions: - Load the EPI in EPI field and tab out");
		patientDemographics.setEPI(epi);

		logger.info("*** Step 12 Expected Results: - Verify that the Patient Demographics Info display properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(patientDemographics.ptZipInput().getAttribute("value").trim().equals("92129"), "        Pt Zip: 92129 should show.");
		Assert.assertTrue(patientDemographics.payorIDInput().getAttribute("value").trim().equals(pyrAbbrev), "        Payor ID: " + pyrAbbrev + " should show.");

		logger.info("*** Step 12 Expected Results: - Verify that the Suspended checkbox in Insurance Infomation tab is unchecked");
		Assert.assertFalse(patientDemographics.suspendedCheckBox().isSelected(), "        The Suspended checkbox in Insurance Infomation tab should be unchecked.");

		patientDemographics.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Load Patient Demo from Accession Demo")
	@Parameters({"email", "password"})
	public void testRPM_10(String email, String password) throws Exception {
    	logger.info("***** Testing - testRPM_10 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());
		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");
		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Demographics page");
		switchToDefaultWinFromFrame();
		accessionNavigation = new AccessionNavigation(driver, config);
		accessionNavigation.demographicsLink();

		logger.info("*** Step 2 Expected Results: - The Accession - Demographics page displays");
		accessionDemographics = new AccessionDemographics(driver);
		Assert.assertTrue(accessionDemographics.pageTitleTextInFrame().trim().contains("Accession  - Demographics"), "        Accession Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Load an Accession in the Accn ID field and tab out");

		List<String> accnEPIList = daoManagerXifinRpm.getAccnIdWithEPIFromAccn(testDb);
		String accnId = accnEPIList.get(0);
		String epi = accnEPIList.get(1);
		accessionDemographics.accnIdInputInFrame(accnId);
		Thread.sleep(3000);

		logger.info("*** Step 3 Expected Results: - Verify that the EPI is associated to the accession");
		Assert.assertEquals(accessionDemographics.epiInputInFrameText().trim(), epi);

		logger.info("*** Step 4 Actions: - Navigate to the Patient - Demographics page");
		switchToDefaultWinFromFrame();
		accessionNavigation.patientDemographicsLink();
		Thread.sleep(3000);
		switchToDefaultWinFromFrame();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 4 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 4 Expected Results: - Verify that the EPI that is associated with the accession displays in the Patient Demographics");
		Assert.assertTrue(patientDemographics.epiInput().getAttribute("value").trim().equals(epi), "        EPI: " + epi + " should show.");

		patientDemographics.resetBtn().click();
		driver.close();
    }

	@Test(priority = 1, description = "Create new Standing Order")
	@Parameters({"email", "password"})
	public void testRPM_600(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_600 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Click Submit button in Patient Demographics screen");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Navigate to Standing Order screen");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
		accessionNavigation.navigateToStandingOrderLink();
		switchToPopupWin();

		logger.info("*** Step 5 Expected Results: - Verify that it's on the Standing Order screen");
		standingOrder = new StandingOrder(driver);
		Assert.assertTrue(standingOrder.pageTitleText().getText().trim().contains("Accession - Standing Order"), "        Page Title: Accession - Standing Order should show.");

		logger.info("*** Step 6 Actions: - Load the newly created EPI and tab out");
		standingOrder.setEPI(epi);

		logger.info("*** Step 6 Actions: - Enter Client ID, Authorization Date and Ordering Physician NPI");

		String clnId = daoManagerAccnWS.getClnAbbrev(testDb);
		standingOrder.setClnID(clnId);

		standingOrder.setAuthDt("t");

		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		standingOrder.setOrderingPhysNPI(npi);

		logger.info("*** Step 7 Actions: - Click Add Test button");
		standingOrder.addTestBtn().click();

		logger.info("*** Step 7 Actions: - Enter a Test ID");
		String testID = daoManagerAccnWS.getTestAbbrev(testDb);
		standingOrder.setTestID(testID);

		logger.info("*** Step 7 Actions: - Click Submit button");
		standingOrder.submitBtn().click();

		logger.info("*** Step 8 Actions: - Load the same EPI and Client ID");
		standingOrder.setEPI(epi);
		standingOrder.setClnID(clnId);

		logger.info("*** Step 8 Actions: - Select the newly created Std Order ID");

		ArrayList<String> stdOrderList = daoManagerXifinRpm.getStdOrderInfoFromPTSTDORDERByEPIClnId(epi, clnId, testDb);
		String stdOrderId = stdOrderList.get(0);
		selectItem(standingOrder.stdOrderIdDropDown(), stdOrderId);

		logger.info("*** Step 8 Expected Results: - Verify that Authorization Date, Ordering Physician NPI, Freq and Test ID display properly");
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		Assert.assertTrue(standingOrder.authDtInput().getAttribute("value").contains(currDt), "        Authorization Date: " + currDt + " should show.");

		Assert.assertTrue(standingOrder.orderingPhysNPIInput().getAttribute("value").contains(npi), "        Ordering Physician NPI: " + npi + " should show.");
		Assert.assertTrue(isDropdownItemSelected(standingOrder.freqDropDown(), "daily"), "        Freq: daily should show.");
		Assert.assertTrue(standingOrder.testIDInput().getAttribute("value").contains(testID), "        Test ID: " + testID + " should show.");

		standingOrder.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Delete Standing Order")
	@Parameters({"email", "password"})
	public void testRPM_601(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_601 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Click Submit button in Patient Demographics screen");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Navigate to Standing Order screen");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
		accessionNavigation.navigateToStandingOrderLink();
		switchToPopupWin();

		logger.info("*** Step 5 Expected Results: - Verify that it's on the Standing Order screen");
		standingOrder = new StandingOrder(driver);
		Assert.assertTrue(standingOrder.pageTitleText().getText().trim().contains("Accession - Standing Order"), "        Page Title: Accession - Standing Order should show.");

		logger.info("*** Step 6 Actions: - Load the newly created EPI and tab out");
		standingOrder.setEPI(epi);

		logger.info("*** Step 6 Actions: - Enter Client ID, Authorization Date and Ordering Physician NPI");

		String clnId = daoManagerAccnWS.getClnAbbrev(testDb);
		standingOrder.setClnID(clnId);

		standingOrder.setAuthDt("t");

		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		standingOrder.setOrderingPhysNPI(npi);

		logger.info("*** Step 7 Actions: - Click Add Test button");
		standingOrder.addTestBtn().click();

		logger.info("*** Step 7 Actions: - Enter a Test ID");
		String testID = daoManagerAccnWS.getTestAbbrev(testDb);
		standingOrder.setTestID(testID);

		logger.info("*** Step 7 Actions: - Click Submit button");
		standingOrder.submitBtn().click();

		logger.info("*** Step 8 Actions: - Load the same EPI and Client ID");
		standingOrder.setEPI(epi);
		standingOrder.setClnID(clnId);

		logger.info("*** Step 8 Actions: - Select the newly created Std Order ID");

		ArrayList<String> stdOrderList = daoManagerXifinRpm.getStdOrderInfoFromPTSTDORDERByEPIClnId(epi, clnId, testDb);
		String stdOrderId = stdOrderList.get(0);
		selectItem(standingOrder.stdOrderIdDropDown(), stdOrderId);

		logger.info("*** Step 8 Expected Results: - Verify that Authorization Date, Ordering Physician NPI, Freq and Test ID display properly");
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		Assert.assertTrue(standingOrder.authDtInput().getAttribute("value").contains(currDt), "        Authorization Date: " + currDt + " should show.");

		Assert.assertTrue(standingOrder.orderingPhysNPIInput().getAttribute("value").contains(npi), "        Ordering Physician NPI: " + npi + " should show.");
		Assert.assertTrue(isDropdownItemSelected(standingOrder.freqDropDown(), "daily"), "        Freq: daily should show.");
		Assert.assertTrue(standingOrder.testIDInput().getAttribute("value").contains(testID), "        Test ID: " + testID + " should show.");

		logger.info("*** Step 9 Actions: - Check Delete Standing Order checkbox");
		selectCheckBox(standingOrder.deleteStandingOrderCheckBox());

		logger.info("*** Step 9 Actions: - Click Submit button");
		standingOrder.submitBtn().click();

		logger.info("*** Step 10 Actions: - Load the same EPI and Client ID");
		standingOrder.setEPI(epi);
		standingOrder.setClnID(clnId);

		logger.info("*** Step 10 Expected Results: - Verify that the Standing Order was deleted");
		Assert.assertTrue(isDropdownItemSelected(standingOrder.stdOrderIdDropDown(), "New Std Order"), "        Std Order ID: New Std Order should be selected.");

		standingOrder.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Copy Standing Order")
	@Parameters({"email", "password"})
	public void testRPM_602(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_602 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Click Submit button in Patient Demographics screen");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Navigate to Standing Order screen");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
		accessionNavigation.navigateToStandingOrderLink();
		switchToPopupWin();

		logger.info("*** Step 5 Expected Results: - Verify that it's on the Standing Order screen");
		standingOrder = new StandingOrder(driver);
		Assert.assertTrue(standingOrder.pageTitleText().getText().trim().contains("Accession - Standing Order"), "        Page Title: Accession - Standing Order should show.");

		logger.info("*** Step 6 Actions: - Load the newly created EPI and tab out");
		standingOrder.setEPI(epi);

		logger.info("*** Step 6 Actions: - Enter Client ID, Authorization Date and Ordering Physician NPI");

		String clnId = daoManagerAccnWS.getClnAbbrev(testDb);
		standingOrder.setClnID(clnId);

		standingOrder.setAuthDt("t");

		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		standingOrder.setOrderingPhysNPI(npi);

		logger.info("*** Step 7 Actions: - Click Add Test button");
		standingOrder.addTestBtn().click();

		logger.info("*** Step 7 Actions: - Enter a Test ID");
		String testID = daoManagerAccnWS.getTestAbbrev(testDb);
		standingOrder.setTestID(testID);

		logger.info("*** Step 7 Actions: - Click Submit button");
		standingOrder.submitBtn().click();

		logger.info("*** Step 8 Actions: - Load the same EPI and Client ID");
		standingOrder.setEPI(epi);
		standingOrder.setClnID(clnId);

		logger.info("*** Step 8 Actions: - Select the newly created Std Order ID");

		ArrayList<String> stdOrderList = daoManagerXifinRpm.getStdOrderInfoFromPTSTDORDERByEPIClnId(epi, clnId, testDb);
		String stdOrderId = stdOrderList.get(0);
		selectItem(standingOrder.stdOrderIdDropDown(), stdOrderId);

		logger.info("*** Step 8 Expected Results: - Verify that Authorization Date, Ordering Physician NPI, Freq and Test ID display properly");
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		Assert.assertTrue(standingOrder.authDtInput().getAttribute("value").contains(currDt), "        Authorization Date: " + currDt + " should show.");

		Assert.assertTrue(standingOrder.orderingPhysNPIInput().getAttribute("value").contains(npi), "        Ordering Physician NPI: " + npi + " should show.");
		Assert.assertTrue(isDropdownItemSelected(standingOrder.freqDropDown(), "daily"), "        Freq: daily should show.");
		Assert.assertTrue(standingOrder.testIDInput().getAttribute("value").contains(testID), "        Test ID: " + testID + " should show.");

		logger.info("*** Step 9 Actions: - Click Copy SO button");
		standingOrder.copySOBtn().click();

		logger.info("*** Step 9 Expected Results: - Verify that the same EPI, Client ID, Authorization Date, Ordering Physician NPI, Freq, Test ID display properly");
		Assert.assertTrue(standingOrder.authDtInput().getAttribute("value").contains(currDt), "        Authorization Date: " + currDt + " should show.");
		Assert.assertTrue(standingOrder.orderingPhysNPIInput().getAttribute("value").contains(npi), "        Ordering Physician NPI: " + npi + " should show.");
		Assert.assertTrue(standingOrder.testIDInput().getAttribute("value").contains(testID), "        Test ID: " + testID + " should show.");

		logger.info("*** Step 9 Expected Results: - Verify that Std Order ID - New Std Order is selected");
		Assert.assertTrue(isDropdownItemSelected(standingOrder.stdOrderIdDropDown(), "New Std Order"), "        Std Order ID: New Std Order should be selected.");

		logger.info("*** Step 10 Actions: - Click Submit button");
		standingOrder.submitBtn().click();

		logger.info("*** Step 10 Actions: - Load the same EPI and Client ID");
		standingOrder.setEPI(epi);
		standingOrder.setClnID(clnId);

		logger.info("*** Step 10 Expected Results: - Verify that the new Standing Order was created");
		stdOrderList = daoManagerXifinRpm.getStdOrderInfoFromPTSTDORDERByEPIClnId(epi, clnId, testDb);
		stdOrderId = stdOrderList.get(0);
		selectItem(standingOrder.stdOrderIdDropDown(), stdOrderId);

		logger.info("*** Step 10 Expected Results: - Verify that the same EPI, Client ID, Authorization Date, Ordering Physician NPI, Freq, Test ID for the new Standing Order display properly");
		Assert.assertTrue(standingOrder.authDtInput().getAttribute("value").contains(currDt), "        Authorization Date: " + currDt + " should show.");
		Assert.assertTrue(standingOrder.orderingPhysNPIInput().getAttribute("value").contains(npi), "        Ordering Physician NPI: " + npi + " should show.");
		Assert.assertTrue(standingOrder.testIDInput().getAttribute("value").contains(testID), "        Test ID: " + testID + " should show.");

		standingOrder.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Client Info display")
	@Parameters({"email", "password"})
	public void testRPM_603(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_603 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Click Submit button in Patient Demographics screen");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Navigate to Standing Order screen");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
		accessionNavigation.navigateToStandingOrderLink();
		switchToPopupWin();

		logger.info("*** Step 5 Expected Results: - Verify that it's on the Standing Order screen");
		standingOrder = new StandingOrder(driver);
		Assert.assertTrue(standingOrder.pageTitleText().getText().trim().contains("Accession - Standing Order"), "        Page Title: Accession - Standing Order should show.");

		logger.info("*** Step 6 Actions: - Load the newly created EPI and tab out");
		standingOrder.setEPI(epi);

		logger.info("*** Step 6 Actions: - Enter Client ID, Authorization Date and Ordering Physician NPI");

		String clnId = daoManagerAccnWS.getClnAbbrev(testDb);
		standingOrder.setClnID(clnId);

		standingOrder.setAuthDt("t");

		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		standingOrder.setOrderingPhysNPI(npi);

		logger.info("*** Step 7 Actions: - Click Add Test button");
		standingOrder.addTestBtn().click();

		logger.info("*** Step 7 Actions: - Enter a Test ID");
		String testID = daoManagerAccnWS.getTestAbbrev(testDb);
		standingOrder.setTestID(testID);

		logger.info("*** Step 7 Actions: - Click Submit button");
		standingOrder.submitBtn().click();

		logger.info("*** Step 8 Actions: - Load the same EPI and Client ID");
		standingOrder.setEPI(epi);
		standingOrder.setClnID(clnId);

		logger.info("*** Step 8 Actions: - Select the newly created Std Order ID");

		ArrayList<String> stdOrderList = daoManagerXifinRpm.getStdOrderInfoFromPTSTDORDERByEPIClnId(epi, clnId, testDb);
		String stdOrderId = stdOrderList.get(0);
		selectItem(standingOrder.stdOrderIdDropDown(), stdOrderId);

		logger.info("*** Step 8 Expected Results: - Verify that the Client Name display properly");

		ArrayList<String> clnInfoList = daoManagerClientWS.getClientContactInfo(clnId, testDb);
		String clnName = clnInfoList.get(1);
		Assert.assertTrue(standingOrder.clnNameInput().getAttribute("value").contains(clnName), "        Client Name: " + clnName + " should show.");

		standingOrder.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Patient Info display")
	@Parameters({"email", "password"})
	public void testRPM_604(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_604 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Enter Patient SSN, Name, DOB, Gender and Marital Status Information");
		patientDemographics.setPtSSN("123456789");

		String ptFName = randomCharacter.getRandomAlphaString(8);
		patientDemographics.setPtFirstName(ptFName);

		logger.info("*** Step 4 Actions: - Click Submit button in Patient Demographics screen");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Navigate to Standing Order screen");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
		accessionNavigation.navigateToStandingOrderLink();
		switchToPopupWin();

		logger.info("*** Step 5 Expected Results: - Verify that it's on the Standing Order screen");
		standingOrder = new StandingOrder(driver);
		Assert.assertTrue(standingOrder.pageTitleText().getText().trim().contains("Accession - Standing Order"), "        Page Title: Accession - Standing Order should show.");

		logger.info("*** Step 6 Actions: - Load the newly created EPI and tab out");
		standingOrder.setEPI(epi);

		logger.info("*** Step 6 Actions: - Enter Client ID, Authorization Date and Ordering Physician NPI");

		String clnId = daoManagerAccnWS.getClnAbbrev(testDb);
		standingOrder.setClnID(clnId);

		standingOrder.setAuthDt("t");

		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		standingOrder.setOrderingPhysNPI(npi);

		logger.info("*** Step 7 Actions: - Click Add Test button");
		standingOrder.addTestBtn().click();

		logger.info("*** Step 7 Actions: - Enter a Test ID");
		String testID = daoManagerAccnWS.getTestAbbrev(testDb);
		standingOrder.setTestID(testID);

		logger.info("*** Step 7 Actions: - Click Submit button");
		standingOrder.submitBtn().click();

		logger.info("*** Step 8 Actions: - Load the same EPI and Client ID");
		standingOrder.setEPI(epi);
		standingOrder.setClnID(clnId);

		logger.info("*** Step 8 Actions: - Select the newly created Std Order ID");

		ArrayList<String> stdOrderList = daoManagerXifinRpm.getStdOrderInfoFromPTSTDORDERByEPIClnId(epi, clnId, testDb);
		String stdOrderId = stdOrderList.get(0);
		selectItem(standingOrder.stdOrderIdDropDown(), stdOrderId);

		logger.info("*** Step 8 Expected Results: - Verify that the Pt SSN, Pt Last Name and Pt First Name display properly");
		Assert.assertTrue(standingOrder.ptSSNInput().getAttribute("value").contains("123-45-6789"), "        Pt SSN: 123-45-6789 should show.");
		Assert.assertTrue(standingOrder.ptLNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");
		Assert.assertTrue(standingOrder.ptFNameInput().getAttribute("value").trim().equals(ptFName), "        Pt First Name: " + ptFName + " should show.");

		standingOrder.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Physician Info displays")
	@Parameters({"email", "password"})
	public void testRPM_605(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_605 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Click Submit button in Patient Demographics screen");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Navigate to Standing Order screen");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
		accessionNavigation.navigateToStandingOrderLink();
		switchToPopupWin();

		logger.info("*** Step 5 Expected Results: - Verify that it's on the Standing Order screen");
		standingOrder = new StandingOrder(driver);
		Assert.assertTrue(standingOrder.pageTitleText().getText().trim().contains("Accession - Standing Order"), "        Page Title: Accession - Standing Order should show.");

		logger.info("*** Step 6 Actions: - Load the newly created EPI and tab out");
		standingOrder.setEPI(epi);

		logger.info("*** Step 6 Actions: - Enter Client ID, Authorization Date and Ordering Physician NPI");

		String clnId = daoManagerAccnWS.getClnAbbrev(testDb);
		standingOrder.setClnID(clnId);

		standingOrder.setAuthDt("t");

		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		standingOrder.setOrderingPhysNPI(npi);

		logger.info("*** Step 7 Actions: - Click Add Test button");
		standingOrder.addTestBtn().click();

		logger.info("*** Step 7 Actions: - Enter a Test ID");
		String testID = daoManagerAccnWS.getTestAbbrev(testDb);
		standingOrder.setTestID(testID);

		logger.info("*** Step 7 Actions: - Click Submit button");
		standingOrder.submitBtn().click();

		logger.info("*** Step 8 Actions: - Load the same EPI and Client ID");
		standingOrder.setEPI(epi);
		standingOrder.setClnID(clnId);

		logger.info("*** Step 8 Actions: - Select the newly created Std Order ID");

		ArrayList<String> stdOrderList = daoManagerXifinRpm.getStdOrderInfoFromPTSTDORDERByEPIClnId(epi, clnId, testDb);
		String stdOrderId = stdOrderList.get(0);
		selectItem(standingOrder.stdOrderIdDropDown(), stdOrderId);

		logger.info("*** Step 8 Expected Results: - Verify that the Ordering Physician NPI and Name display properly");
		Assert.assertTrue(standingOrder.orderingPhysNPIInput().getAttribute("value").contains(npi), "        Ordering Physician NPI: " + npi + " should show.");

		ArrayList<String> physInfoList = daoManagerXifinRpm.getPhysInfoFromPHYSByNPI(npi, testDb);
		String physName = physInfoList.get(1).trim().toUpperCase() + ", " + physInfoList.get(2).trim().toUpperCase();
		Assert.assertTrue(standingOrder.orderingPhysNameInput().getAttribute("value").contains(physName), "        Ordering Physician Name: " + physName + " should show.");

		standingOrder.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Signature Info displays")
	@Parameters({"email", "password"})
	public void testRPM_607(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_607 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Click Submit button in Patient Demographics screen");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Navigate to Standing Order screen");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
		accessionNavigation.navigateToStandingOrderLink();
		switchToPopupWin();

		logger.info("*** Step 5 Expected Results: - Verify that it's on the Standing Order screen");
		standingOrder = new StandingOrder(driver);
		Assert.assertTrue(standingOrder.pageTitleText().getText().trim().contains("Accession - Standing Order"), "        Page Title: Accession - Standing Order should show.");

		logger.info("*** Step 6 Actions: - Load the newly created EPI and tab out");
		standingOrder.setEPI(epi);

		logger.info("*** Step 6 Actions: - Enter Client ID, Authorization Date and Ordering Physician NPI");

		String clnId = daoManagerAccnWS.getClnAbbrev(testDb);
		standingOrder.setClnID(clnId);

		standingOrder.setAuthDt("t");

		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		standingOrder.setOrderingPhysNPI(npi);

		logger.info("*** Step 7 Actions: - Click Add Test button");
		standingOrder.addTestBtn().click();

		logger.info("*** Step 7 Actions: - Enter a Test ID");
		String testID = daoManagerAccnWS.getTestAbbrev(testDb);
		standingOrder.setTestID(testID);

		logger.info("*** Step 7 Actions: - Check Physician and Patient Signature on File checkboxes");
		selectCheckBox(standingOrder.physSignatureOnFileCheckBox());
		selectCheckBox(standingOrder.ptSignatureOnFileCheckBox());

		logger.info("*** Step 7 Actions: - Click Submit button");
		standingOrder.submitBtn().click();

		logger.info("*** Step 8 Actions: - Load the same EPI and Client ID");
		standingOrder.setEPI(epi);
		standingOrder.setClnID(clnId);

		logger.info("*** Step 8 Actions: - Select the newly created Std Order ID");

		ArrayList<String> stdOrderList = daoManagerXifinRpm.getStdOrderInfoFromPTSTDORDERByEPIClnId(epi, clnId, testDb);
		String stdOrderId = stdOrderList.get(0);
		selectItem(standingOrder.stdOrderIdDropDown(), stdOrderId);

		logger.info("*** Step 8 Expected Results: - Verify that the Physician and Patient Signature on File checkboxes are checked");
		Assert.assertTrue(isChecked(standingOrder.physSignatureOnFileCheckBox()), "        Physician Signature on File checkbox should be checked.");
		Assert.assertTrue(isChecked(standingOrder.ptSignatureOnFileCheckBox()), "        Patient Signature on File checkbox should be checked.");

		standingOrder.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Reset")
	@Parameters({"email", "password"})
	public void testRPM_611(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_611 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Click Submit button in Patient Demographics screen");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Navigate to Standing Order screen");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
		accessionNavigation.navigateToStandingOrderLink();
		switchToPopupWin();

		logger.info("*** Step 5 Expected Results: - Verify that it's on the Standing Order screen");
		standingOrder = new StandingOrder(driver);
		Assert.assertTrue(standingOrder.pageTitleText().getText().trim().contains("Accession - Standing Order"), "        Page Title: Accession - Standing Order should show.");

		logger.info("*** Step 6 Actions: - Load the newly created EPI and tab out");
		standingOrder.setEPI(epi);

		logger.info("*** Step 6 Actions: - Enter Client ID, Authorization Date and Ordering Physician NPI");

		String clnId = daoManagerAccnWS.getClnAbbrev(testDb);
		standingOrder.setClnID(clnId);

		standingOrder.setAuthDt("t");

		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		standingOrder.setOrderingPhysNPI(npi);

		logger.info("*** Step 7 Actions: - Click Add Test button");
		standingOrder.addTestBtn().click();

		logger.info("*** Step 7 Actions: - Enter a Test ID");
		String testID = daoManagerAccnWS.getTestAbbrev(testDb);
		standingOrder.setTestID(testID);

		logger.info("*** Step 7 Actions: - Click Submit button");
		standingOrder.submitBtn().click();

		logger.info("*** Step 8 Actions: - Load the same EPI and Client ID");
		standingOrder.setEPI(epi);
		standingOrder.setClnID(clnId);

		logger.info("*** Step 8 Actions: - Select the newly created Std Order ID");

		ArrayList<String> stdOrderList = daoManagerXifinRpm.getStdOrderInfoFromPTSTDORDERByEPIClnId(epi, clnId, testDb);
		String stdOrderId = stdOrderList.get(0);
		selectItem(standingOrder.stdOrderIdDropDown(), stdOrderId);

		logger.info("*** Step 8 Expected Results: - Verify that Authorization Date, Ordering Physician NPI, Freq and Test ID display properly");
		timeStamp = new TimeStamp(driver);
		String currDt = timeStamp.getCurrentDate();
		Assert.assertTrue(standingOrder.authDtInput().getAttribute("value").contains(currDt), "        Authorization Date: " + currDt + " should show.");

		Assert.assertTrue(standingOrder.orderingPhysNPIInput().getAttribute("value").contains(npi), "        Ordering Physician NPI: " + npi + " should show.");
		Assert.assertTrue(isDropdownItemSelected(standingOrder.freqDropDown(), "daily"), "        Freq: daily should show.");
		Assert.assertTrue(standingOrder.testIDInput().getAttribute("value").contains(testID), "        Test ID: " + testID + " should show.");

		logger.info("*** Step 9 Actions: - Click Reset button");
		standingOrder.resetBtn().click();

		logger.info("*** Step 9 Expected Results: - Verify that the EPI and Client ID Input fields are cleared and the Submit button is disabled");
		Assert.assertTrue(standingOrder.epiInput().getAttribute("value").isEmpty(), "        EPI Input field should be cleared.");
		Assert.assertTrue(standingOrder.clnIDInput().getAttribute("value").isEmpty(), "        Client ID Input field should be cleared.");
		Assert.assertFalse(standingOrder.submitBtn().isEnabled(), "        The Submit button should be disabled.");

		driver.close();
	}

	@Test(priority = 1, description = "Test Info display")
	@Parameters({"email", "password"})
	public void testRPM_606(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_606 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Click Submit button in Patient Demographics screen");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Navigate to Standing Order screen");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
		accessionNavigation.navigateToStandingOrderLink();
		switchToPopupWin();

		logger.info("*** Step 5 Expected Results: - Verify that it's on the Standing Order screen");
		standingOrder = new StandingOrder(driver);
		Assert.assertTrue(standingOrder.pageTitleText().getText().trim().contains("Accession - Standing Order"), "        Page Title: Accession - Standing Order should show.");

		logger.info("*** Step 6 Actions: - Load the newly created EPI and tab out");
		standingOrder.setEPI(epi);

		logger.info("*** Step 6 Actions: - Enter Client ID, Authorization Date and Ordering Physician NPI");

		String clnId = daoManagerAccnWS.getClnAbbrev(testDb);
		standingOrder.setClnID(clnId);

		standingOrder.setAuthDt("t");

		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		standingOrder.setOrderingPhysNPI(npi);

		logger.info("*** Step 7 Actions: - Click Add Test button");
		standingOrder.addTestBtn().click();

		logger.info("*** Step 7 Actions: - Enter Test ID, Dx Code, ABN Rec'd, ABN Rsn and ABN Comment");
		String testID = daoManagerAccnWS.getTestAbbrev(testDb);
		standingOrder.setTestID(testID);

		String diagCd = daoManagerAccnWS.getDiagnosisCode(testDb);
		standingOrder.setDxCode(diagCd);

		selectCheckBox(standingOrder.abnRecdCheckBox());

		selectItem(standingOrder.abnRsnDropDown(), "Routine");

		String abnComment = "ABN Commnent " + randomCharacter.getRandomAlphaString(8);
		standingOrder.setABNComment(abnComment);

		logger.info("*** Step 7 Actions: - Click Submit button");
		standingOrder.submitBtn().click();

		logger.info("*** Step 8 Actions: - Load the same EPI and Client ID");
		standingOrder.setEPI(epi);
		standingOrder.setClnID(clnId);

		logger.info("*** Step 8 Actions: - Select the newly created Std Order ID");

		ArrayList<String> stdOrderList = daoManagerXifinRpm.getStdOrderInfoFromPTSTDORDERByEPIClnId(epi, clnId, testDb);
		String stdOrderId = stdOrderList.get(0);
		selectItem(standingOrder.stdOrderIdDropDown(), stdOrderId);

		logger.info("*** Step 8 Expected Results: - Verify that Test ID, Dx Code, ABN Rec'd, ABN Rsn and ABN Comment display properly");
		Assert.assertTrue(standingOrder.testIDInput().getAttribute("value").contains(testID), "        Test ID: " + testID + " should show.");
		Assert.assertTrue(standingOrder.dxCodeInput().getAttribute("value").contains(diagCd), "        Dx Code: " + diagCd + " should show.");
		Assert.assertTrue(isChecked(standingOrder.savedABNRecdCheckBox()), "        ABN Rec'd checkbox should be checked.");
		Assert.assertTrue(isDropdownItemSelected(standingOrder.abnRsnDropDown(), "Routine"), "        ABN Rsn: Routine should be selected.");
		Assert.assertTrue(standingOrder.abnComntInput().getAttribute("value").contains(abnComment), "        ABN Comment: " + abnComment + " should show.");

		standingOrder.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Invalid NPI validation error")
	@Parameters({"email", "password"})
	public void testRPM_608(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_608 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Click Submit button in Patient Demographics screen");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Navigate to Standing Order screen");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
		accessionNavigation.navigateToStandingOrderLink();
		switchToPopupWin();

		logger.info("*** Step 5 Expected Results: - Verify that it's on the Standing Order screen");
		standingOrder = new StandingOrder(driver);
		Assert.assertTrue(standingOrder.pageTitleText().getText().trim().contains("Accession - Standing Order"), "        Page Title: Accession - Standing Order should show.");

		logger.info("*** Step 6 Actions: - Load the newly created EPI and tab out");
		standingOrder.setEPI(epi);

		logger.info("*** Step 6 Actions: - Enter Client ID and Authorization Date");

		String clnId = daoManagerAccnWS.getClnAbbrev(testDb);
		standingOrder.setClnID(clnId);

		standingOrder.setAuthDt("t");

		logger.info("*** Step 7 Actions: - Click Add Test button");
		standingOrder.addTestBtn().click();

		logger.info("*** Step 7 Actions: - Enter a Test ID");
		String testID = daoManagerAccnWS.getTestAbbrev(testDb);
		standingOrder.setTestID(testID);

		logger.info("*** Step 7 Actions: - Click Submit button");
		standingOrder.submitBtn().click();

		logger.info("*** Step 7 Expected Results: - Verify that invalid npi error message displays");
		Assert.assertTrue(standingOrder.errMessageText().getText().trim().contains("not a valid npi"), "        Error message: not a valid npi should show.");

		standingOrder.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Authorization date validation error")
	@Parameters({"email", "password"})
	public void testRPM_609(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_609 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Click Submit button in Patient Demographics screen");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Navigate to Standing Order screen");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
		accessionNavigation.navigateToStandingOrderLink();
		switchToPopupWin();

		logger.info("*** Step 5 Expected Results: - Verify that it's on the Standing Order screen");
		standingOrder = new StandingOrder(driver);
		Assert.assertTrue(standingOrder.pageTitleText().getText().trim().contains("Accession - Standing Order"), "        Page Title: Accession - Standing Order should show.");

		logger.info("*** Step 6 Actions: - Load the newly created EPI and tab out");
		standingOrder.setEPI(epi);

		logger.info("*** Step 6 Actions: - Enter Client ID and Ordering Physician NPI");

		String clnId = daoManagerAccnWS.getClnAbbrev(testDb);
		standingOrder.setClnID(clnId);

		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		standingOrder.setOrderingPhysNPI(npi);

		logger.info("*** Step 7 Actions: - Click Add Test button");
		standingOrder.addTestBtn().click();

		logger.info("*** Step 7 Actions: - Enter a Test ID");
		String testID = daoManagerAccnWS.getTestAbbrev(testDb);
		standingOrder.setTestID(testID);

		logger.info("*** Step 7 Actions: - Click Submit button");
		standingOrder.submitBtn().click();

		logger.info("*** Step 7 Expected Results: - Verify that an authorization date is required error message displays");
		Assert.assertTrue(standingOrder.errMessageText().getText().trim().contains("An authorization date is required"), "        Error message: An authorization date is required should show.");

		standingOrder.resetBtn().click();
		driver.close();
	}

	@Test(priority = 1, description = "Test validation error")
	@Parameters({"email", "password"})
	public void testRPM_610(String email, String password) throws Exception {
		logger.info("***** Testing - testRPM_610 *****");

		logger.info("*** Step 1 Actions: - Log into Mars with username and password");
		ssoLogin = new SsoLogin(driver);
		ssoLogin.login(email, password);

		logger.info("*** Step 1 Expected Results: - Verify that user is logged in");
		headerNavigation = new HeaderNavigation(driver, config);
		switchToFrame(headerNavigation.marsHeaderFrame());

		Assert.assertTrue(headerNavigation.marsUsernameText().getText().contains(daoManagerXifinRpm.getUserIdByEmail(email, testDb)), "        " + headerNavigation.marsUsernameText().getText() + " should match " + daoManagerXifinRpm.getUserIdByEmail(email, testDb));

		logger.info("*** Step 1 Expected Results: - Verify that the user has logged into the correct customer test environment");

		Assert.assertEquals(headerNavigation.marsCustomerText().getText(), daoManagerPlatform.getDataValueFromSYSTEMSETTINGBySettingId(9, testDb));

		logger.info("*** Step 2 Actions: - Navigate to the Accession - Patient Demographics page");
		accessionNavigation = new AccessionNavigation(driver, config);
		switchToDefaultWinFromFrame();
		String winHandler = driver.getWindowHandle();
		accessionNavigation.navigateToPatientDemographicsLink();
		switchToPopupWin();

		logger.info("*** Step 2 Expected Results: - Verify that the Accession Patient Demographics page displays");
		patientDemographics = new PatientDemographics(driver, wait);
		Assert.assertTrue(patientDemographics.pageTitleText().getText().trim().contains("Patient Demographics"), "        Accession Patient Demographics page title should show.");

		logger.info("*** Step 3 Actions: - Click Create New EPI button");
		patientDemographics.createNewEPIBtn().click();

		logger.info("*** Step 3 Actions: - Enter Patient Last Name in Create New Patient popup window");
		randomCharacter =  new RandomCharacter(driver);
		String ptLName = randomCharacter.getRandomAlphaString(6);
		patientDemographics.setNewPtLName(ptLName);

		logger.info("*** Step 3 Actions: - Click Ok button in Create New Patient popup window");
		patientDemographics.okBtn().click();

		logger.info("*** Step 3 Actions: - Get the new EPI and tab out in EPI Input field");
		patientDemographics.epiInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String epi = patientDemographics.epiInput().getAttribute("value");

		logger.info("*** Step 3 Expected Results: - Verify that the Pt Last Name was loaded properly");
		Assert.assertTrue(patientDemographics.ptLastNameInput().getAttribute("value").trim().equals(ptLName), "        Pt Last Name: " + ptLName + " should show.");

		logger.info("*** Step 4 Actions: - Enter a Patient Zip and Payor");
		patientDemographics.setPtZip("92129");

		String pyrAbbrev = daoManagerXifinRpm.getPyrAbbrevFromPYRByPyrId("4", testDb);
		patientDemographics.setPayorId(pyrAbbrev);

		logger.info("*** Step 4 Actions: - Click Submit button in Patient Demographics screen");
		patientDemographics.submitBtn().click();
		Thread.sleep(2000);

		logger.info("*** Step 5 Actions: - Navigate to Standing Order screen");
        driver.close();
        switchToWin(winHandler);
        switchToDefaultWinFromFrame();
		accessionNavigation.navigateToStandingOrderLink();
		switchToPopupWin();

		logger.info("*** Step 5 Expected Results: - Verify that it's on the Standing Order screen");
		standingOrder = new StandingOrder(driver);
		Assert.assertTrue(standingOrder.pageTitleText().getText().trim().contains("Accession - Standing Order"), "        Page Title: Accession - Standing Order should show.");

		logger.info("*** Step 6 Actions: - Load the newly created EPI and tab out");
		standingOrder.setEPI(epi);

		logger.info("*** Step 6 Actions: - Enter Client ID, Authorization Date and Ordering Physician NPI");

		String clnId = daoManagerAccnWS.getClnAbbrev(testDb);
		standingOrder.setClnID(clnId);

		standingOrder.setAuthDt("t");

		String npi = daoManagerAccnWS.getPhysicianNPI(testDb);
		standingOrder.setOrderingPhysNPI(npi);

		logger.info("*** Step 7 Actions: - Click Submit button");
		standingOrder.submitBtn().click();

		logger.info("*** Step 7 Expected Results: - Verify that at least one test is required in a standing order error message displays");
		Assert.assertTrue(standingOrder.errMessageText().getText().trim().contains("At least one test is required in a standing order"), "        Error message: At least one test is required in a standing order is required should show.");

		standingOrder.resetBtn().click();
		driver.close();
	}

}
