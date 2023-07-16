package com.newXp.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.mbasys.common.utility.Money;
import com.mbasys.mars.ejb.entity.prc.Prc;
import com.mbasys.mars.ejb.entity.prcProc.PrcProc;
import com.mbasys.mars.ejb.entity.prcTest.PrcTest;
import com.overall.fileMaintenance.pricing.FeeSchedule;
import com.overall.menu.MenuNavigation;
import com.overall.utils.FeeScheduleUtils;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;
import com.xifin.xap.utils.XifinAdminUtils;

import domain.fileMaintenance.feeschedule.FeeScheduleInformation;
import domain.fileMaintenance.feeschedule.Pricing;

public class FeeScheduleTest extends SeleniumBaseTest {
	private RandomCharacter randomCharacter;
	private FeeSchedule feeSchedule;
	private FeeScheduleUtils feeScheduleUtils;
	private static final String ACCOUNT_TYPE_CLIENT = "Client";
	private static final String ACCOUNT_TYPE_NON_CLIENT = "Non-Client";
	private static final String BASIS_TYPE_TEST_CODE = "Test Code";
	private static final String BASIS_TYPE_PROCEDURE_CODE = "Procedure Code";
	private static final String PRICE_TYP_EXPECT = "Expect";
	private static final String PRICE_TYP_RETAIL = "Retail";
	private static final String PRICE_TYP_NORMAL = "Normal";
	private static final int FEE_SCHEDULE = 1;
	private String oldFeeScheduleId;
	private String newFeeScheduleId;
	private static final SimpleDateFormat DATE_FORMAT_MMDDYYYY = new SimpleDateFormat("MM/dd/yyyy");
	private TimeStamp timeStamp;

	@BeforeMethod(alwaysRun = true)
	@Parameters({"ssoUsername", "ssoPassword"})
	public void beforeMethod(String ssoUsername, String ssoPassword, Method method) {
		try {
			logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
			logIntoSso(ssoUsername, ssoPassword);
			MenuNavigation navigation = new MenuNavigation(driver, config);
			navigation.navigateToFeeSchedulePage();
		} catch (Exception e) {
			logger.error("Error running BeforeMethod", e);
		}
	}
	
	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) throws Exception {
		String methodName = result.getMethod().getMethodName();
		logger.info("*** Starting revert data ***");
		switch (methodName) {
		case "verifyUsersAreAbleToUpdateFeeScheduleID":
			feeScheduleUtils.updateFeeScheduleIdWithOldValue(newFeeScheduleId, oldFeeScheduleId);
			logger.info("*** Reverting data is successfully ***");
			break;
		case "verifyThereIsANewButtonToChangingFeeScheduleID":
		case "verifyThereIsAnErrorWhenUsersChangeFeeScheduleIDToTheExistingOne":
			logger.info("*** No need to revert ***");
			break;
		default:
			logger.info("*** Invalid method ***");
		}
	}

	@BeforeSuite(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "disableBrowserPlugins", "feeScheduleId1", "feeScheduleId2", "feeScheduleId3", "feeScheduleId5"})
	public void BeforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, @Optional String disableBrowserPlugins, String feeScheduleId1, String feeScheduleId2, String feeScheduleId3, String feeScheduleId5)
	{
		try
		{
			logger.info("Running BeforeSuite");
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			cleanUpFeeScheduleTestData(Arrays.asList(feeScheduleId1, feeScheduleId2, feeScheduleId3, feeScheduleId5));
		}
		catch (Exception e)
		{
			Assert.fail("Error running BeforeSuite", e);
		}
		finally
		{
			quitWebDriver();
		}
	}

	@AfterSuite(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "disableBrowserPlugins", "feeScheduleId1", "feeScheduleId2", "feeScheduleId3", "feeScheduleId5"})
	public void AfterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, @Optional String disableBrowserPlugins, String feeScheduleId1, String feeScheduleId2, String feeScheduleId3, String feeScheduleId5)
	{
		try
		{
			logger.info("Running AfterSuite");
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			cleanUpFeeScheduleTestData(Arrays.asList(feeScheduleId1, feeScheduleId2, feeScheduleId3, feeScheduleId5));
		}
		catch (Exception e)
		{
			Assert.fail("Error running AfterSuite", e);
		}
		finally
		{
			quitWebDriver();
		}
	}


	@Test(priority = 1, description = "Create new Cln Retail Test Fee Schedule from existing FS table, Individual option")
	@Parameters({"feeScheduleId1", "copyFeeScheduleId"})
	public void testXPR_1056(String feeScheduleId1, String copyFeeScheduleId) throws Exception {
		logger.info("===== Testing - testXPR_1056 =====");
		feeSchedule = new FeeSchedule(driver, wait);
		randomCharacter = new RandomCharacter(driver);


		logger.info("*** Expected Results: - Verify that the Load Fee Schedule page is displayed");
		assertTrue(isOnLoadFeeSchedulePage());

		logger.info("*** Action: - Select Individually Display Option");
		selectDropDownByText(feeSchedule.displayOptionDropdown(), "Individually");

		logger.info("*** Action: - Enter a new Fee Schedule ID");
		setInputValue(feeSchedule.feeScheduleIdInput(), feeScheduleId1);

		logger.info("*** Expected Results: - Verify that the Fee Schedule Information page is displayed");
		assertEquals(feeSchedule.feeScheduleTitle(1).getText(), "Fee Schedule Information");

		logger.info("*** Action: - Enter Fee Schedule Information");
		FeeScheduleInformation expectedFeeScheduleInformation = setValuesInFeeScheduleInformation(feeScheduleId1, true, "Client", "Retail", "Test Code", "All");

		logger.info("*** Action: - Select Create From Table ID option and enter an existing Table ID");
		clickOnElement(feeSchedule.tableIdRadioBtnInFeeScheduleInformation());
		setInputValue(feeSchedule.tableIdInputInFeeScheduleInformation(), copyFeeScheduleId);

		logger.info("*** Action: - Enter the Effective and Create with Effective Dates");
		String effectiveDateStr = "06/01/2038";
		setInputValue(feeSchedule.effectiveInputInFeeScheduleInformation(), effectiveDateStr);
		setInputValue(feeSchedule.createWithEffectiveDateInputInFeeScheduleInformation(), effectiveDateStr);

		Date effectiveDate = new Date(DATE_FORMAT_MMDDYYYY.parse(effectiveDateStr).getTime());
		Pricing expectedPricing = setTestCodeValuesInPricing(true, "1000", testDao.getTestByTestAbbrev("1000").getName().trim(), effectiveDate, null, new Money(0), new Money(10.00), new Money(10.00), false, "last()");

        List<Pricing> expectedPricingList = new ArrayList<>();
        expectedPricingList.add(expectedPricing);

		logger.info("*** Action: - Click Save button");
		clickOnElement(feeSchedule.saveBtn());

		logger.info("*** Expected Results: - Verify that the Fee Schedule page is displayed");
		wait.until(ExpectedConditions.visibilityOf(feeSchedule.appendBtn()));

		logger.info("*** Expected Results: - Verify that the Test Code created from the existing FS table is displayed in Pricing table in UI");
		Pricing actualPricing = getTestCodeValuesInPricing("last()");

		List<Pricing> actualPricingList = new ArrayList<>();
		actualPricingList.add(actualPricing);
		assertEquals(actualPricingList, expectedPricingList);

		logger.info("*** Action: - Add a new Test Code in the Pricing table");
		clickOnElement(feeSchedule.addBtn());
		expectedPricing = setTestCodeValuesInPricing(false, "102X", testDao.getTestByTestAbbrev("102X").getName().trim(), effectiveDate, null, new Money(0), new Money(20.00), new Money(20.00), false, "last()");
		expectedPricingList.add(expectedPricing);

		logger.info("*** Action: - Create a Test Code Append TXT file");
		File file = new File(System.getProperty("user.dir")).getParentFile();
		String appendFileName = "FeeScheduleAppend_" + randomCharacter.getRandomAlphaString(4) + ".txt";
		String filePath = file +File.separator+"src"+File.separator +"test"+File.separator+"resources"+File.separator+appendFileName;
		createAppendFile(filePath, Collections.singletonList("300"), Collections.singletonList(effectiveDate), Collections.singletonList(effectiveDate), Collections.singletonList(new Money(30.00)), Collections.singletonList(""));

		logger.info("*** Action: - Click Choose File button and select a Test Code Append file and click Append button");
		clickHiddenPageObject(feeSchedule.chooseFileBtnInAppendFile(), 0);
		uploadFile(feeSchedule.chooseFileBtnInAppendFile(), appendFileName);
		clickOnElement(feeSchedule.appendBtn());

		logger.info("*** Expected Results: - Verify that the Test Code in the Append file is added to the Pricing table");
		expectedPricing = setTestCodeValuesInPricing(true, "300", testDao.getTestByTestAbbrev("300").getName().trim(), effectiveDate, effectiveDate, new Money(0), new Money(30.00), new Money(30.00), false, "last()");
		expectedPricingList.add(expectedPricing);

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(feeSchedule.saveAndClearBtn());

		logger.info("*** Expected Results: - Verify that it's back to the load Fee Schedule page");
		assertTrue(isOnLoadFeeSchedulePage());

		logger.info("*** Expected Results: - Verify that the Fee Schedule Information data are saved properly in DB");
		List<Prc> prcList = prcDao.getPrcByPrcAbbrev(feeScheduleId1);
		verifyFeeScheduleInformationSaved(prcList, expectedFeeScheduleInformation);

		logger.info("*** Expected Results: - Verify that the Fee Schedule Pricing data are saved properly in DB");
		List<PrcTest> prcTestList = prcDao.getPrcTestByPrcAbbrev(feeScheduleId1);
		verifyFeeScheduleTestCodePricingSaved(prcTestList, expectedPricingList);

		logger.info("*** Action: - Select All Display Option");
		selectDropDownByText(feeSchedule.displayOptionDropdown(), "All");

		logger.info("*** Action: - Enter the newly created Fee Schedule ID");
		setInputValue(feeSchedule.feeScheduleIdInput(), feeScheduleId1);

		logger.info("*** Expected Results: - Verify that the Fee Schedule page is displayed");
		wait.until(ExpectedConditions.visibilityOf(feeSchedule.appendBtn()));

		logger.info("*** Expected Results: - Verify that the data in Fee Schedule Information are displayed properly in UI");
		FeeScheduleInformation actualFeeScheduleInformation = getValuesInFeeScheduleInformation();
		assertEquals(actualFeeScheduleInformation, expectedFeeScheduleInformation);

		logger.info("*** Expected Results: - Verify that the data in Pricing are displayed properly in UI");
		actualPricingList.clear();
		for (int i=0; i<prcTestList.size(); i++) {
			actualPricingList.add(getTestCodeValuesInPricing(String.valueOf(i+2)));
		}
		assertEquals(actualPricingList, expectedPricingList);

		logger.info("*** Action: - Click Reset button");
		clickOnElement(feeSchedule.resetBtn());

		logger.info("*** Actions: - Delete the test TXT file");
		deleteTestFile(filePath);
	}

	@Test(priority = 1, description = "Create new NonCln Expect Proc Cd Fee Schedule from Import file, All option")
	@Parameters({"feeScheduleId2"})
	public void testXPR_1058(String feeScheduleId2) throws Exception {
		logger.info("===== Testing - testXPR_1058 =====");
		feeSchedule = new FeeSchedule(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		logger.info("*** Expected Results: - Verify that the Load Fee Schedule page is displayed");
		assertTrue(isOnLoadFeeSchedulePage());

		logger.info("*** Action: - Select All Display Option");
		selectDropDownByText(feeSchedule.displayOptionDropdown(), "All");

		logger.info("*** Action: - Enter a new Fee Schedule ID");
		setInputValue(feeSchedule.feeScheduleIdInput(), feeScheduleId2);

		logger.info("*** Expected Results: - Verify that the Fee Schedule Information page is displayed");
		assertEquals(feeSchedule.feeScheduleTitle(1).getText(), "Fee Schedule Information");

		logger.info("*** Action: - Enter Fee Schedule Information");
		FeeScheduleInformation expectedFeeScheduleInformation = setValuesInFeeScheduleInformation(feeScheduleId2, true, "Non-Client", "Expect", "Procedure Code", "All");

		String effectiveDateStr = "06/01/2038";
		Date effectiveDate = new Date(DATE_FORMAT_MMDDYYYY.parse(effectiveDateStr).getTime());

		logger.info("*** Action: - Create a Procedure Code Import CSV file");
		File file = new File(System.getProperty("user.dir")).getParentFile();
		String importFileName = "FeeScheduleImport_" + randomCharacter.getRandomAlphaString(4) + ".csv";
		String filePath = file +File.separator+"src"+File.separator +"test"+File.separator+"resources"+File.separator+importFileName;
		createAppendFile(filePath, Collections.singletonList("G0102"), Collections.singletonList(effectiveDate), Collections.singletonList(effectiveDate), Collections.singletonList(new Money(40.00)), Collections.singletonList("9A"));

		logger.info("*** Action: - Select Create From Import File option");
		clickOnElement(feeSchedule.importFileRadioBtnInFeeScheduleInformation());

		logger.info("*** Action: - Click Choose File button and select a Procedure Code Import file");
		clickHiddenPageObject(feeSchedule.chooseFileBtnInFeeScheduleInformation(), 0);
		uploadFile(feeSchedule.chooseFileBtnInFeeScheduleInformation(), importFileName);

		logger.info("*** Action: - Click Save button");
		clickOnElement(feeSchedule.saveBtn());

		Pricing expectedPricing = setProcedureCodeValuesInPricing(true, "G0102", "9A", procedureCodeDao.getProcCdByProcIdWhereSSIs58("G0102").getDescr().trim(), effectiveDate, effectiveDate, new Money(0), new Money(40.00), new Money(40.00), false, "last()");
		List<Pricing> expectedPricingList = new ArrayList<>();
		expectedPricingList.add(expectedPricing);

		logger.info("*** Expected Results: - Verify that the Fee Schedule page is displayed");
		wait.until(ExpectedConditions.visibilityOf(feeSchedule.appendBtn()));

		logger.info("*** Expected Results: - Verify that the Procedure Code created from the Import File is displayed in Pricing table in UI");
		Pricing actualPricing = getProcedureCodeValuesInPricing("last()");

		List<Pricing> actualPricingList = new ArrayList<>();
		actualPricingList.add(actualPricing);
		assertEquals(actualPricingList, expectedPricingList);

		logger.info("*** Action: - Add a new Procedure Code in the Pricing table");
		clickOnElement(feeSchedule.addBtn());
		expectedPricing = setProcedureCodeValuesInPricing(false, "R0075", null, procedureCodeDao.getProcCdByProcIdWhereSSIs58("R0075").getDescr().trim(), effectiveDate, null, new Money(0), new Money(30.00), new Money(30.00), false, "last()");
		expectedPricingList.add(expectedPricing);

		actualPricing = getProcedureCodeValuesInPricing("last()");
		actualPricingList.add(actualPricing);

		logger.info("*** Action: - Create a Procedure Code Append CSV file");
		String appendFileName = "FeeScheduleAppend_" + randomCharacter.getRandomAlphaString(4) + ".csv";
		filePath = file +File.separator+"src"+File.separator +"test"+File.separator+"resources"+File.separator+appendFileName;
		createAppendFile(filePath, Collections.singletonList("V5299"), Collections.singletonList(effectiveDate), Collections.singletonList(effectiveDate), Collections.singletonList(new Money(20.00)), Collections.singletonList("22"));

		logger.info("*** Action: - Click Choose File button and select a Procedure Code Append file and click Append button");
		clickHiddenPageObject(feeSchedule.chooseFileBtnInAppendFile(), 0);
		uploadFile(feeSchedule.chooseFileBtnInAppendFile(), appendFileName);
		clickHiddenPageObject(feeSchedule.appendBtn(), 0);

		logger.info("*** Expected Results: - Verify that the Procedure Code in the Append file is added to the Pricing table");
		expectedPricing = setProcedureCodeValuesInPricing(true, "V5299", "22", procedureCodeDao.getProcCdByProcIdWhereSSIs58("V5299").getDescr().trim(), effectiveDate, effectiveDate, new Money(0), new Money(20.00), new Money(20.00), false, "last()");
		expectedPricingList.add(expectedPricing);

		logger.info("*** Action: - Click Save And Clear button");
		clickHiddenPageObject(feeSchedule.saveAndClearBtn(), 0);

		logger.info("*** Expected Results: - Verify that it's back to the load Fee Schedule page");
		assertTrue(isOnLoadFeeSchedulePage());

		logger.info("*** Expected Results: - Verify that the Fee Schedule Information data are saved properly in DB");
		List<Prc> prcList = prcDao.getPrcByPrcAbbrev(feeScheduleId2);
		verifyFeeScheduleInformationSaved(prcList, expectedFeeScheduleInformation);

		logger.info("*** Expected Results: - Verify that the Fee Schedule Pricing data are saved properly in DB");
		List<PrcProc> prcProcList = prcDao.getPrcProcByPrcAbbrev(feeScheduleId2);
		verifyFeeScheduleProcedureCodePricingSaved(prcProcList, expectedPricingList);

		logger.info("*** Action: - Select All Display Option");
		selectDropDownByText(feeSchedule.displayOptionDropdown(), "All");

		logger.info("*** Action: - Enter the newly created Fee Schedule ID");
		setInputValue(feeSchedule.feeScheduleIdInput(), feeScheduleId2);

		logger.info("*** Expected Results: - Verify that the Fee Schedule page is displayed");
		wait.until(ExpectedConditions.visibilityOf(feeSchedule.appendBtn()));

		logger.info("*** Expected Results: - Verify that the data in Fee Schedule Information are displayed properly in UI");
		FeeScheduleInformation actualFeeScheduleInformation = getValuesInFeeScheduleInformation();
		assertEquals(actualFeeScheduleInformation, expectedFeeScheduleInformation);

		logger.info("*** Expected Results: - Verify that the data in Pricing are displayed properly in UI");
		actualPricingList.clear();
		for (int i=0; i<prcProcList.size(); i++) {
			actualPricingList.add(getProcedureCodeValuesInPricing(String.valueOf(i+2)));
		}
		assertEquals(actualPricingList, expectedPricingList);

		logger.info("*** Action: - Click Reset button");
		clickOnElement(feeSchedule.resetBtn());

		logger.info("*** Actions: - Delete the test TXT file");
		deleteTestFile(filePath);
	}

	@Test(priority = 1, description = "Create new NonCln Normal Proc Cd Fee Schedule from RVS table")
	@Parameters({"feeScheduleId3"})
	public void testXPR_1226(String feeScheduleId3) throws Exception {
		logger.info("===== Testing - testXPR_1226 =====");
		feeSchedule = new FeeSchedule(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		timeStamp = new TimeStamp(driver);

		logger.info("*** Expected Results: - Verify that the Load Fee Schedule page is displayed");
		assertTrue(isOnLoadFeeSchedulePage());

		logger.info("*** Action: - Select All Display Option");
		selectDropDownByText(feeSchedule.displayOptionDropdown(), "All");

		logger.info("*** Action: - Enter a new Fee Schedule ID");
		setInputValue(feeSchedule.feeScheduleIdInput(), feeScheduleId3);

		logger.info("*** Expected Results: - Verify that the Fee Schedule Information page is displayed");
		assertEquals(feeSchedule.feeScheduleTitle(1).getText(), "Fee Schedule Information");

		logger.info("*** Action: - Enter Fee Schedule Information");
		FeeScheduleInformation expectedFeeScheduleInformation = setValuesInFeeScheduleInformation(feeScheduleId3, true, "Non-Client", "Normal", "Procedure Code", "All");

		Date currentDate = new Date(DATE_FORMAT_MMDDYYYY.parse(timeStamp.getCurrentDate()).getTime());

		logger.info("*** Action: - Select Create From RVS Table option");
		clickOnElement(feeSchedule.rvsTableRadioBtn());

		logger.info("*** Action: - Select McGraw Hill (RVS Type 1) option from the Dropdown");
		selectItemByVal(feeSchedule.rvsTableDropdown(), "1");

		logger.info("*** Action: - Enter per Unit amount");
		setInputValue(feeSchedule.perUnitInput(), "20.00");

		logger.info("*** Action: - Click Save button");
		clickOnElement(feeSchedule.saveBtn());

		Pricing expectedPricing = setProcedureCodeValuesInPricing(true, "82943", null, procedureCodeDao.getProcCdByProcIdWhereSSIs58("82943").getDescr().trim(), currentDate, null, new Money(0), new Money(20.00), new Money(20.00), false, "last()");
		List<Pricing> expectedPricingList = new ArrayList<>();
		expectedPricingList.add(expectedPricing);

		logger.info("*** Expected Results: - Verify that the Fee Schedule page is displayed");
		wait.until(ExpectedConditions.visibilityOf(feeSchedule.appendBtn()));

		logger.info("*** Expected Results: - Verify that the Procedure Code created from the RVS table is displayed in Pricing table in UI");
		Pricing actualPricing = getProcedureCodeValuesInPricing("last()");

		List<Pricing> actualPricingList = new ArrayList<>();
		actualPricingList.add(actualPricing);
		assertEquals(actualPricingList, expectedPricingList);

		logger.info("*** Action: - Enter a Payor in Primary Payor input field");
		setInputValue(feeSchedule.primaryPayorInput(), "P");

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(feeSchedule.saveAndClearBtn());

		logger.info("*** Expected Results: - Verify that it's back to the load Fee Schedule page");
		assertTrue(isOnLoadFeeSchedulePage());

		logger.info("*** Expected Results: - Verify that the Fee Schedule Information data are saved properly in DB");
		List<Prc> prcList = prcDao.getPrcByPrcAbbrev(feeScheduleId3);
		verifyFeeScheduleInformationSaved(prcList, expectedFeeScheduleInformation);

		//Primary Payor Id saved in DB
		assertEquals(prcList.get(0).getPrimaryPyrId(), 4);

		logger.info("*** Expected Results: - Verify that the Fee Schedule Pricing data are saved properly in DB");
		List<PrcProc> prcProcList = prcDao.getPrcProcByPrcAbbrev(feeScheduleId3);
		verifyFeeScheduleProcedureCodePricingSaved(prcProcList, expectedPricingList);

		logger.info("*** Action: - Select All Display Option");
		selectDropDownByText(feeSchedule.displayOptionDropdown(), "All");

		logger.info("*** Action: - Enter the newly created Fee Schedule ID");
		setInputValue(feeSchedule.feeScheduleIdInput(), feeScheduleId3);

		logger.info("*** Expected Results: - Verify that the Fee Schedule page is displayed");
		wait.until(ExpectedConditions.visibilityOf(feeSchedule.appendBtn()));

		logger.info("*** Expected Results: - Verify that the data in Fee Schedule Information are displayed properly in UI");
		FeeScheduleInformation actualFeeScheduleInformation = getValuesInFeeScheduleInformation();
		assertEquals(actualFeeScheduleInformation, expectedFeeScheduleInformation);

		logger.info("*** Expected Results: - Verify that the data in Pricing are displayed properly in UI");
		actualPricingList.clear();
		for (int i=0; i<prcProcList.size(); i++) {
			actualPricingList.add(getProcedureCodeValuesInPricing(String.valueOf(i+2)));
		}
		assertEquals(actualPricingList, expectedPricingList);

		//Primary Payor Abbrev displayed
		assertEquals(feeSchedule.primaryPayorInputTxt().trim(), "P");

		logger.info("*** Action: - Click Reset button");
		clickOnElement(feeSchedule.resetBtn());
	}

	@Test(priority = 1, description = "Load existing FS and View Clients and Payors")
	@Parameters({"feeScheduleId4"})
	public void testXPR_1063(String feeScheduleId4) throws Exception {
		logger.info("===== Testing - testXPR_1063 =====");
		feeSchedule = new FeeSchedule(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		timeStamp = new TimeStamp(driver);

		logger.info("*** Expected Results: - Verify that the Load Fee Schedule page is displayed");
		assertTrue(isOnLoadFeeSchedulePage());

		logger.info("*** Action: - Select All Display Option");
		selectDropDownByText(feeSchedule.displayOptionDropdown(), "All");

		logger.info("*** Action: - Enter an existing Fee Schedule ID");
		setInputValue(feeSchedule.feeScheduleIdInput(), feeScheduleId4);

		logger.info("*** Expected Results: - Verify that the Fee Schedule page is displayed");
		wait.until(ExpectedConditions.visibilityOf(feeSchedule.appendBtn()));

		logger.info("*** Action: - Click View Clients button");
		clickHiddenPageObject(feeSchedule.viewClientsBtn(), 0);

		String parentWindow = switchToPopupWin();

		logger.info("*** Expected Results: - Verify that the Client Search Result - Client Pricing page is displayed");
		List<String> clientPricingList = Arrays.asList("TESTAT435944", "792497", "07/01/2020");
		verifySearchResultsDisplayed(clientPricingList, feeScheduleId4);

		driver.close();
		switchToParentWin(parentWindow);

		logger.info("*** Action: - Click View Payors button");
		clickHiddenPageObject(feeSchedule.viewPayorsBtn(), 0);

		parentWindow = switchToPopupWin();

		logger.info("*** Expected Results: - Verify that the Payor Search Result - Payor Pricing page is displayed");
		List<String> payorPricingList = Arrays.asList("TESTPYRTDGSP", "TESTNAMEDVKCK", "11/04/2015");
		verifySearchResultsDisplayed(payorPricingList, feeScheduleId4);

		driver.close();
		switchToParentWin(parentWindow);

		logger.info("*** Action: - Click Reset button");
		clickOnElement(feeSchedule.resetBtn());
	}

	@Test(priority = 1, description = "Verify Helps")
	public void testXPR_1066() throws Exception {
		logger.info("===== Testing - testXPR_1066 =====");
		feeSchedule = new FeeSchedule(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		timeStamp = new TimeStamp(driver);

		logger.info("*** Expected Results: - Verify that the Load Fee Schedule page is displayed");
		assertTrue(isOnLoadFeeSchedulePage());

		logger.info("*** Action: - Click Help icon in Fee Schedule header section");
		clickOnElement(feeSchedule.helpIconBtnInHeader());

		logger.info("*** Expected Results: - Verify that Fee Schedule Header Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/pricing_menu/p_fee_schedule_header.htm", "Fee Schedule Header"));

		logger.info("*** Action: - Click Help icon in Load Fee Schedule section");
		clickOnElement(feeSchedule.helpIconBtnInLoadFeeSchedule());

		logger.info("*** Expected Results: - Verify that Load Fee Schedule Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/pricing_menu/p_fee_schedule_load_fee_schedule.htm", "Fee Schedule Selection"));

		logger.info("*** Action: - Select All Display Option");
		selectDropDownByText(feeSchedule.displayOptionDropdown(), "All");

		logger.info("*** Action: - Enter a new Fee Schedule ID");
		String feeScheduleId = randomCharacter.getRandomAlphaString(8);
		setInputValue(feeSchedule.feeScheduleIdInput(), feeScheduleId);

		logger.info("*** Expected Results: - Verify that the Fee Schedule Information page is displayed");
		assertEquals(feeSchedule.feeScheduleTitle(1).getText(), "Fee Schedule Information");

		logger.info("*** Action: - Click Help icon in Fee Schedule Information section");
		clickOnElement(feeSchedule.helpIconBtnInFeeScheduleInformation());

		logger.info("*** Expected Results: - Verify that Fee Schedule Information Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/pricing_menu/p_fee_schedule_fee_schedule_info.htm", "Fee Schedule Information"));

		logger.info("*** Action: - Enter Fee Schedule Information");
		setValuesInFeeScheduleInformation(feeScheduleId, true, "Non-Client", "Expect", "Procedure Code", "All");

		logger.info("*** Action: - Click Save button");
		clickOnElement(feeSchedule.saveBtn());

		logger.info("*** Expected Results: - Verify that the Fee Schedule page is displayed");
		wait.until(ExpectedConditions.visibilityOf(feeSchedule.appendBtn()));

		logger.info("*** Action: - Click Help icon in Fee Schedule Pricing section");
		clickOnElement(feeSchedule.helpIconBtnInPricingSection());

		logger.info("*** Expected Results: - Verify that Fee Schedule Pricing Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/pricing_menu/p_fee_schedule_pricing.htm", "Pricing"));

		logger.info("*** Action: - Click Help icon in Fee Schedule Append File section");
		clickOnElement(feeSchedule.helpIconBtnInAppendFileSection());

		logger.info("*** Expected Results: - Verify that Fee Schedule Append File Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/pricing_menu/p_fee_schedule_append_file.htm", "Append File"));

		logger.info("*** Action: - Click View Clients button");
		clickOnElement(feeSchedule.viewClientsBtn());

		String parentWindow = switchToPopupWin();

		logger.info("*** Action: - Click Help icon in Client Pricing popup");
		clickOnElement(feeSchedule.helpIconBtnInClientSearchResults());

		logger.info("*** Expected Results: - Verify that Fee Schedule Pricing Search Results Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/pricing_menu/p_fee_schedule_client_pricing.htm", "Client Pricing"));

		driver.close();
		switchToParentWin(parentWindow);

		logger.info("*** Action: - Click View Payors button");
		clickOnElement(feeSchedule.viewPayorsBtn());

		parentWindow = switchToPopupWin();

		logger.info("*** Action: - Click Help icon in Payor Pricing popup");
		clickOnElement(feeSchedule.helpIconBtnInPayorSearchResults());

		logger.info("*** Expected Results: - Verify that Fee Schedule Pricing Search Results Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("p_fee_schedule_payor_pricing.htm", "Payor Pricing"));

		driver.close();
		switchToParentWin(parentWindow);

		logger.info("*** Action: - Click Reset button");
		clickOnElement(feeSchedule.resetBtn());
	}

	@Test(priority = 1, description = "Delete Test Code")
	@Parameters({"feeScheduleId5"})
	public void testXPR_1062(String feeScheduleId5) throws Exception {
		logger.info("===== Testing - testXPR_1062 =====");
		feeSchedule = new FeeSchedule(driver, wait);
		randomCharacter = new RandomCharacter(driver);


		logger.info("*** Expected Results: - Verify that the Load Fee Schedule page is displayed");
		assertTrue(isOnLoadFeeSchedulePage());

		logger.info("*** Action: - Select All Display Option");
		selectDropDownByText(feeSchedule.displayOptionDropdown(), "All");

		logger.info("*** Action: - Enter a new Fee Schedule ID");
		setInputValue(feeSchedule.feeScheduleIdInput(), feeScheduleId5);

		logger.info("*** Expected Results: - Verify that the Fee Schedule Information page is displayed");
		assertEquals(feeSchedule.feeScheduleTitle(1).getText(), "Fee Schedule Information");

		logger.info("*** Action: - Enter Fee Schedule Information");
		setValuesInFeeScheduleInformation(feeScheduleId5, true, "Client", "Retail", "Test Code", "All");

		String effectiveDateStr = "06/01/2038";
		Date effectiveDate = new Date(DATE_FORMAT_MMDDYYYY.parse(effectiveDateStr).getTime());

		List<Pricing> expectedPricingList = new ArrayList<>();

		logger.info("*** Action: - Click Save button");
		clickOnElement(feeSchedule.saveBtn());

		logger.info("*** Expected Results: - Verify that the Fee Schedule page is displayed");
		wait.until(ExpectedConditions.visibilityOf(feeSchedule.appendBtn()));

		List<Pricing> actualPricingList = new ArrayList<>();

		logger.info("*** Action: - Add a new Test Code in the Pricing table");
		clickOnElement(feeSchedule.addBtn());
		Pricing expectedPricing = setTestCodeValuesInPricing(false, "102X", testDao.getTestByTestAbbrev("102X").getName().trim(), effectiveDate, null, new Money(0), new Money(20.00), new Money(20.00), false, "last()");
		expectedPricingList.add(expectedPricing);

		logger.info("*** Action: - Add another new Test Code in the Pricing table");
		clickOnElement(feeSchedule.addBtn());
		expectedPricing = setTestCodeValuesInPricing(false, "300", testDao.getTestByTestAbbrev("300").getName().trim(), effectiveDate, null, new Money(0), new Money(30.00), new Money(30.00), false, "last()");
		expectedPricingList.add(expectedPricing);

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(feeSchedule.saveAndClearBtn());

		logger.info("*** Expected Results: - Verify that it's back to the load Fee Schedule page");
		assertTrue(isOnLoadFeeSchedulePage());

		logger.info("*** Expected Results: - Verify that the Fee Schedule Pricing data are saved properly in DB");
		List<PrcTest> prcTestList = prcDao.getPrcTestByPrcAbbrev(feeScheduleId5);
		verifyFeeScheduleTestCodePricingSaved(prcTestList, expectedPricingList);

		logger.info("*** Action: - Select All Display Option");
		selectDropDownByText(feeSchedule.displayOptionDropdown(), "All");

		logger.info("*** Action: - Enter the newly created Fee Schedule ID");
		setInputValue(feeSchedule.feeScheduleIdInput(), feeScheduleId5);

		logger.info("*** Expected Results: - Verify that the Fee Schedule page is displayed");
		wait.until(ExpectedConditions.visibilityOf(feeSchedule.appendBtn()));

		logger.info("*** Expected Results: - Verify that the data in Pricing are displayed properly in UI");
		actualPricingList.clear();
		for (int i=0; i<prcTestList.size(); i++) {
			actualPricingList.add(getTestCodeValuesInPricing(String.valueOf(i+2)));
		}
		assertEquals(actualPricingList, expectedPricingList);

		logger.info("*** Action: - Select a Test Code and select Delete checkbox");
		clickOnElement(feeSchedule.deleteCheckboxInPricingTbl("2"));

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(feeSchedule.saveAndClearBtn());

		logger.info("*** Expected Results: - Verify that it's back to the load Fee Schedule page");
		assertTrue(isOnLoadFeeSchedulePage());

		expectedPricingList.remove(0);
		logger.info("*** Expected Results: - Verify that the Fee Schedule Pricing data are saved properly in DB");
		prcTestList = prcDao.getPrcTestByPrcAbbrev(feeScheduleId5);
		verifyFeeScheduleTestCodePricingSaved(prcTestList, expectedPricingList);

		logger.info("*** Action: - Select All Display Option");
		selectDropDownByText(feeSchedule.displayOptionDropdown(), "All");

		logger.info("*** Action: - Enter the newly created Fee Schedule ID");
		setInputValue(feeSchedule.feeScheduleIdInput(), feeScheduleId5);

		logger.info("*** Expected Results: - Verify that the Fee Schedule page is displayed");
		wait.until(ExpectedConditions.visibilityOf(feeSchedule.appendBtn()));

		logger.info("*** Expected Results: - Verify that the data in Pricing are displayed properly in UI");
		actualPricingList.clear();
		for (int i=0; i<prcTestList.size(); i++) {
			actualPricingList.add(getTestCodeValuesInPricing(String.valueOf(i+2)));
		}
		assertEquals(actualPricingList, expectedPricingList);

		logger.info("*** Action: - Click Reset button");
		clickOnElement(feeSchedule.resetBtn());
	}
	
	@Test(priority = 1, description = "Verify that there is a new button to changing Fee Schedule ID")
	public void verifyThereIsANewButtonToChangingFeeScheduleID(Method method) throws Exception {
		logger.info("*** Implementation test case [" + method.getName() + "] ***");
		feeSchedule = new FeeSchedule(driver, wait);
		feeScheduleUtils = new FeeScheduleUtils(driver, config);
		randomCharacter = new RandomCharacter();
		Prc prc = prcDao.getRandomPrcByFeeSchedule(FEE_SCHEDULE);
		String feeScheduleIdWithSpecialCharacters = randomCharacter.getRandomAlphaStringwithSpecialCharacters(6) + "@#";
		
		logger.info("*** Step 1 Expected Result: Verify that the Load Fee Schedule page is displayed");
		assertTrue(isOnLoadFeeSchedulePage());
		
		logger.info("*** Step 2 Actions: Input a valid Fee Schedule ID");
		String displayOption = feeSchedule.loadFeeScheduleDisplayOptionChoosenTxt().getText();
		enterValues(feeSchedule.feeScheduleIdInput(), prc.getPrcAbbrev());
		
		logger.info("*** Step 2 Expected Result: Verify the Fee Schedule page is displayed correctly with information of Fee Schedule ID");
		FeeScheduleInformation expectedFeeScheduleInformation = feeScheduleUtils.getFeeScheduleInformationInDB(prc, displayOption);
		FeeScheduleInformation actualFeeScheduleInformation = getValuesInFeeScheduleInformation();
		assertEquals(actualFeeScheduleInformation, expectedFeeScheduleInformation, "FeeScheduleInformation should be displayed correctly");
		
		logger.info("*** Step 2 Expected Result: 'Change Fee Schedule ID' button is displayed");
		assertTrue(isElementPresent(feeSchedule.changeFeeScheduleIdButton(), 15), "Change Fee Schedule ID button should be displayed");
		
		logger.info("*** Step 3 Actions: Click on 'Change Fee Schedule ID' button");
		clickOnElement(feeSchedule.changeFeeScheduleIdButton());
		
		logger.info("*** Step 3 Expected Result: The 'Change Fee Schedule ID' pop up is displayed ");
		assertEquals(feeSchedule.changeFeeScheduleIdPopupTitle().getText(), "Change Fee Schedule ID", "Change Fee Schedule ID popup should be displayed");
		
		logger.info("*** Step 3 Expected Result: Current Fee Schedule ID field is read only");
		assertTrue(isElementEnabled(feeSchedule.currentFeeScheduleId(), 15, false), "Current Fee Schedule Id field should be disabled");
		assertEquals(feeSchedule.currentFeeScheduleId().getAttribute("value"), actualFeeScheduleInformation.getFeeScheduleId(), "Current Fee Schedule ID should be equals " + actualFeeScheduleInformation.getFeeScheduleId());
		
		logger.info("*** Step 3 Expected Result: New Fee Schedule ID field is enabled");
		assertTrue(isElementEnabled(feeSchedule.newFeeScheduleId(), 15, true), "New Fee Schedule ID field should be enabled");
		
		logger.info("*** Step 4 Actions: Leave New Fee Schedule ID blank" + " | Click on 'OK' button");
		enterValues(feeSchedule.newFeeScheduleId(), "");
		clickOnElement(feeSchedule.okButtonInChangeFeeScheduleIdPopup());
		
		logger.info("*** Step 4 Expected Result: Field Validation popup is displayed");
		assertEquals(feeSchedule.validationMessageInChangeFeeScheduleIdPopup().getText().trim(), "New Fee Schedule ID is required.", "Field validation popup should be displayed");
		
		logger.info("*** Step 5 Actions: Close Field Validation popup" + " | Input new Fee Schedule ID includes special characters" + " | Click on 'OK' button");
		clickOnElement(feeSchedule.closeValidationMessageInChangeFeeScheduleIdPopup());
		enterValues(feeSchedule.newFeeScheduleId(), feeScheduleIdWithSpecialCharacters);
		clickOnElement(feeSchedule.okButtonInChangeFeeScheduleIdPopup());
		
		logger.info("*** Step 5 Expected Result: Field Validation popup is displayed");
		assertEquals(feeSchedule.validationMessageInChangeFeeScheduleIdPopup().getText().trim(), "Invalid: " + feeScheduleIdWithSpecialCharacters + ". Only Number or Letter type values are allowed!", "Field validation popup should be displayed");
	}
	
	@Test(priority = 1, description = "Verify that users are able to update Fee Schedule ID")
	public void verifyUsersAreAbleToUpdateFeeScheduleID(Method method) throws Exception {
		logger.info("*** Implementation test case [" + method.getName() + "] ***");
		feeSchedule = new FeeSchedule(driver, wait);
		feeScheduleUtils = new FeeScheduleUtils(driver, config);
		Prc prc = prcDao.getRandomPrcByFeeSchedule(FEE_SCHEDULE);
		oldFeeScheduleId = prc.getPrcAbbrev();
		newFeeScheduleId = feeScheduleUtils.getNewPrcAbbrvIsNotExistInSystem(FEE_SCHEDULE);
		
		logger.info("*** Step 1 Expected Result: Verify that the Load Fee Schedule page is displayed");
		assertTrue(isOnLoadFeeSchedulePage());
		
		logger.info("*** Step 2 Actions: Input a valid Fee Schedule ID");
		String displayOption = feeSchedule.loadFeeScheduleDisplayOptionChoosenTxt().getText();
		enterValues(feeSchedule.feeScheduleIdInput(), oldFeeScheduleId);
		
		logger.info("*** Step 2 Expected Result: Verify the Fee Schedule page is displayed correctly with information of Fee Schedule ID");
		FeeScheduleInformation expectedFeeScheduleInformation = feeScheduleUtils.getFeeScheduleInformationInDB(prc, displayOption);
		FeeScheduleInformation actualFeeScheduleInformation = getValuesInFeeScheduleInformation();
		assertEquals(actualFeeScheduleInformation, expectedFeeScheduleInformation, "FeeScheduleInformation should be displayed correctly");
		
		logger.info("*** Step 2 Expected Result: 'Change Fee Schedule ID' button is displayed");
		assertTrue(isElementPresent(feeSchedule.changeFeeScheduleIdButton(), 15), "Change Fee Schedule ID button should be displayed");
		
		logger.info("*** Step 3 Actions: Click on 'Change Fee Schedule ID' button");
		clickOnElement(feeSchedule.changeFeeScheduleIdButton());
		
		logger.info("*** Step 3 Expected Result: The 'Change Fee Schedule ID' pop up is displayed ");
		assertEquals(feeSchedule.changeFeeScheduleIdPopupTitle().getText(), "Change Fee Schedule ID", "Change Fee Schedule ID popup should be displayed");
		
		logger.info("*** Step 3 Expected Result: Current Fee Schedule ID field is read only");
		assertTrue(isElementEnabled(feeSchedule.currentFeeScheduleId(), 15, false), "Current Fee Schedule Id field should be disabled");
		assertEquals(feeSchedule.currentFeeScheduleId().getAttribute("value"), actualFeeScheduleInformation.getFeeScheduleId(), "Current Fee Schedule ID should be equals " + actualFeeScheduleInformation.getFeeScheduleId());
		
		logger.info("*** Step 3 Expected Result: New Fee Schedule ID field is enabled");
		assertTrue(isElementEnabled(feeSchedule.newFeeScheduleId(), 15, true), "New Fee Schedule ID field should be enabled");
		
		logger.info("*** Step 4 Actions: Input a new Fee Schedule ID which isn't existed in the system" + " | Click on 'OK' button");
		enterValues(feeSchedule.newFeeScheduleId(), newFeeScheduleId);
		clickOnElement(feeSchedule.okButtonInChangeFeeScheduleIdPopup());
		
		logger.info("*** Step 4 Expected Result: Fee Schedule ID field is updated with the new value");
		assertEquals(feeSchedule.feeScheduleIdTxt().getAttribute("value"), newFeeScheduleId, "Fee Schedule ID should be updated with value " + newFeeScheduleId);
		
		logger.info("*** Step 5 Actions: Click on 'Save And Clear' button");
		clickOnElement(feeSchedule.saveAndClearBtn());
		
		logger.info("*** Step 5 Expected Result: Verify that the Load Fee Schedule page is displayed");
		assertTrue(isOnLoadFeeSchedulePage());
		
		logger.info("*** Step 5 Expected Result: Fee Schedule ID value is updated in Database as new value");
		Prc prcUpdated = prcDao.getPrcByPrcId(prc.getPrcId());
		assertEquals(prcUpdated.getPrcAbbrev(), newFeeScheduleId, "Fee Schedule ID should be updated in DB with value " + newFeeScheduleId);
		
		logger.info("*** Step 6 Actions: Input a updated Fee Schedule ID");
		enterValues(feeSchedule.feeScheduleIdInput(), newFeeScheduleId);
		
		logger.info("*** Step 6 Expected Result: Verify the Fee Schedule page is displayed correctly with information of Fee Schedule ID");
		FeeScheduleInformation expectedFeeScheduleInformationUpdated = feeScheduleUtils.getFeeScheduleInformationInDB(prcUpdated, displayOption);
		FeeScheduleInformation actualFeeScheduleInformationUpdated = getValuesInFeeScheduleInformation();
		assertEquals(expectedFeeScheduleInformationUpdated, actualFeeScheduleInformationUpdated, "FeeScheduleInformation should be displayed correctly with the new Fee Schedule ID");
	}
	
	@Test(priority = 1, description = "Verify that there is an error when users change Fee Schedule ID to the existing one in the system")
	public void verifyThereIsAnErrorWhenUsersChangeFeeScheduleIDToTheExistingOne(Method method) throws Exception {
		logger.info("*** Implementation test case [" + method.getName() + "] ***");
		feeSchedule = new FeeSchedule(driver, wait);
		feeScheduleUtils = new FeeScheduleUtils(driver, config);
		Prc prc = prcDao.getRandomPrcByFeeSchedule(FEE_SCHEDULE);
		Prc existedPrc = feeScheduleUtils.getDifferentPrcWithAnotherOne(prc, FEE_SCHEDULE);
		
		logger.info("*** Step 1 Expected Result: Verify that the Load Fee Schedule page is displayed");
		assertTrue(isOnLoadFeeSchedulePage());
		
		logger.info("*** Step 2 Actions: Input a valid Fee Schedule ID");
		String displayOption = feeSchedule.loadFeeScheduleDisplayOptionChoosenTxt().getText();
		enterValues(feeSchedule.feeScheduleIdInput(), prc.getPrcAbbrev());
		
		logger.info("*** Step 2 Expected Result: Verify the Fee Schedule page is displayed correctly with information of Fee Schedule ID");
		FeeScheduleInformation expectedFeeScheduleInformation = feeScheduleUtils.getFeeScheduleInformationInDB(prc, displayOption);
		FeeScheduleInformation actualFeeScheduleInformation = getValuesInFeeScheduleInformation();
		assertEquals(actualFeeScheduleInformation, expectedFeeScheduleInformation, "FeeScheduleInformation should be displayed correctly");
		
		logger.info("*** Step 2 Expected Result: 'Change Fee Schedule ID' button is displayed");
		assertTrue(isElementPresent(feeSchedule.changeFeeScheduleIdButton(), 15), "Change Fee Schedule ID button should be displayed");
		
		logger.info("*** Step 3 Actions: Click on 'Change Fee Schedule ID' button");
		clickOnElement(feeSchedule.changeFeeScheduleIdButton());
		
		logger.info("*** Step 3 Expected Result: The 'Change Fee Schedule ID' pop up is displayed ");
		assertEquals(feeSchedule.changeFeeScheduleIdPopupTitle().getText(), "Change Fee Schedule ID", "Change Fee Schedule ID popup should be displayed");
		
		logger.info("*** Step 3 Expected Result: Current Fee Schedule ID field is read only");
		assertTrue(isElementEnabled(feeSchedule.currentFeeScheduleId(), 15, false), "Current Fee Schedule Id field should be disabled");
		assertEquals(feeSchedule.currentFeeScheduleId().getAttribute("value"), actualFeeScheduleInformation.getFeeScheduleId(), "Current Fee Schedule ID should be equals " + actualFeeScheduleInformation.getFeeScheduleId());
		
		logger.info("*** Step 3 Expected Result: New Fee Schedule ID field is enabled");
		assertTrue(isElementEnabled(feeSchedule.newFeeScheduleId(), 15, true), "New Fee Schedule ID field should be enabled");
		
		logger.info("*** Step 4 Actions: Input a new Fee Schedule ID which is existed in the system" +  " | Click on 'OK' button");
		enterValues(feeSchedule.newFeeScheduleId(), existedPrc.getPrcAbbrev());
		clickOnElement(feeSchedule.okButtonInChangeFeeScheduleIdPopup());
		
		logger.info("*** Step 4 Expected Result: Field Validation popup is displayed");
		assertEquals(feeSchedule.validationMessageInChangeFeeScheduleIdPopup().getText().trim(), "Fee Schedule ID " + existedPrc.getPrcAbbrev() + " is already in use. Please enter a Fee Schedule ID that is not already in use.", "Field validation should be displayed");
	}
	
	//========================================================================================================================================================
	private boolean isOnLoadFeeSchedulePage() throws Exception {
		boolean isOnPage = false;

		assertTrue(isElementPresent(feeSchedule.displayOptionDropdown(), 5), "        User login successful. The Fee Schedule page is displayed.");
		isOnPage = feeSchedule.feeSchedulePageTitle().getText().equals("Fee Schedule");

		return isOnPage;
	}
	private FeeScheduleInformation setValuesInFeeScheduleInformation(String feeScheduleId, boolean isDiscountable, String accountType, String priceType, String basisType, String displayOption) throws Exception {
		FeeScheduleInformation feeScheduleInformation = new FeeScheduleInformation();

		feeScheduleInformation.setFeeScheduleId(feeScheduleId);
		feeScheduleInformation.setDiscountable(isDiscountable);

		//Name
		String name = randomCharacter.getRandomAlphaString(10);
		setInputValue(feeSchedule.nameInputInFeeScheduleInformation(), name);
		feeScheduleInformation.setName(name);
		//Facility
		selectItemByVal(feeSchedule.facilityDropdownInFeeScheduleInformation(), "1"); //1: ACME
		feeScheduleInformation.setFacility("ACME");
		//Account Type
		if (accountType.equals("Client")){
			clickOnElement(feeSchedule.clientRadioBtnInFeeScheduleInformation());
			feeScheduleInformation.setAccountType("Client");
		}else if (accountType.equals("Non-Client")){
			clickOnElement(feeSchedule.nonClientRadioBtnInFeeScheduleInformation());
			feeScheduleInformation.setAccountType("Non-Client");
		}
		//Price Type
        switch (priceType) {
            case "Expect":
                clickOnElement(feeSchedule.expectRadioBtnInFeeScheduleInformation());
                feeScheduleInformation.setPriceType("Expect");
                break;
            case "Retail":
                clickOnElement(feeSchedule.retailRadioBtnInFeeScheduleInformation());
                feeScheduleInformation.setPriceType("Retail");
                break;
            case "Normal":
                clickOnElement(feeSchedule.normalRadioBtnInFeeScheduleInformation());
                feeScheduleInformation.setPriceType("Normal");
                break;
        }
		//Basis Type
		if (basisType.equals("Test Code")){
			clickOnElement(feeSchedule.testCodeRadioBtnInFeeScheduleInformation());
			feeScheduleInformation.setBasisType("Test Code");
		}else if (basisType.equals("Procedure Code")){
			clickOnElement(feeSchedule.ProcedureCodeRadioBtnInFeeScheduleInformation());
			feeScheduleInformation.setBasisType("Procedure Code");
		}
		//Display Option
		feeScheduleInformation.setDisplayOption(displayOption);

		return feeScheduleInformation;
	}

	private Pricing setTestCodeValuesInPricing(boolean isPreLoaded, String testCode, String name, Date effectiveDate, Date expirationDate, Money retail, Money current, Money newPrice, boolean isDeleted, String row) throws Exception {
		Pricing pricing = new Pricing();

		if (!isPreLoaded){
			setInputValue(feeSchedule.testCodeInputInPricingTbl(row), testCode);

			feeSchedule.effectiveDateInputInPricingTbl(row).clear();
			setInputValue(feeSchedule.effectiveDateInputInPricingTbl(row), DATE_FORMAT_MMDDYYYY.format(effectiveDate));

			feeSchedule.expirationDateInputInPricingTbl(row).clear();
			try {
				setInputValue(feeSchedule.expirationDateInputInPricingTbl(row), DATE_FORMAT_MMDDYYYY.format(expirationDate));
			}catch(Exception e){
				//do nothing;
			}

			feeSchedule.setInputValue(feeSchedule.newPriceInputInPricingTbl(row), String.valueOf(newPrice));

			if (isDeleted){
				feeSchedule.deleteCheckboxInPricingTbl(row).click();
			}
		}

		pricing.setTestCode(testCode);
		pricing.setName(name);
		pricing.setEffectiveDate(effectiveDate);
		pricing.setExpirationDate(expirationDate);
		pricing.setRetail(retail);
		pricing.setCurrent(current);
		pricing.setNewPrice(newPrice);
		pricing.setDeleted(isDeleted);

		return pricing;
	}

	private Pricing getTestCodeValuesInPricing(String index) throws ParseException {
		Pricing pricing = new Pricing();

		pricing.setTestCode(feeSchedule.testCodeLnkInPricingTbl(index).getText().trim());
		pricing.setName(feeSchedule.nameTxtInPricingTbl(index).getText().trim());

		Date effectiveDate = new java.sql.Date(DATE_FORMAT_MMDDYYYY.parse(feeSchedule.effectiveDateTxtInPricingTbl(index).getText().trim()).getTime());
		pricing.setEffectiveDate(effectiveDate);

		try {
			Date expirationDate = new java.sql.Date(DATE_FORMAT_MMDDYYYY.parse(feeSchedule.expirationDateTxtInPricingTbl(index).getText().trim()).getTime());
			pricing.setExpirationDate(expirationDate);
		}catch(Exception e){
			logger.info("       No Expiration Date.");
			pricing.setExpirationDate(null);
		}

		pricing.setRetail(new Money(0));
		pricing.setCurrent(new Money(feeSchedule.currentTxtInPricingTbl(index).getText().trim()));
		pricing.setNewPrice(new Money(feeSchedule.newPriceTxtInPricingTbl(index).getText().trim()));
		pricing.setDeleted(feeSchedule.deleteCheckboxInPricingTbl(index).isSelected());

		return pricing;
	}

	private void createAppendFile(String filePath, List<String> codeList, List<Date> effectiveDateList, List<Date> expirationDateList, List<Money> newPriceList, List<String> modList) throws IOException {
		List<String> lines = new ArrayList<>();
		int i=0;

		for (String code:codeList){
			String line = code + "," + DATE_FORMAT_MMDDYYYY.format(effectiveDateList.get(i)) + "," + DATE_FORMAT_MMDDYYYY.format(expirationDateList.get(i)) + "," + newPriceList.get(i) + "," + modList.get(i) + ",";
			lines.add(line);
			i++;
		}

		Path file = Paths.get(filePath);
		Files.write(file, lines, Charset.forName("UTF-8"));
	}

	private void cleanUpFeeScheduleTestData(List<String> feeScheduleList) throws XifinDataAccessException {
		for (String feeSchedule: feeScheduleList) {
			logger.info("       ========== Deleting Fee Schedule " + feeSchedule + " ==========");
			rpmDao.deletePrcTestByPrcAbbrev(testDb, feeSchedule);
			rpmDao.deletePrcProcByPrcAbbrev(testDb, feeSchedule);
			rpmDao.deletePrcByPrcAbbrev(testDb, feeSchedule);
		}
	}

	private void deleteTestFile(String filePath) throws Exception {
		File testFile = new File(filePath);

		if (isFileExists(testFile, 5)){
			FileUtils.forceDelete(testFile);
		}
	}

	private void verifyFeeScheduleInformationSaved(List<Prc> prcList, FeeScheduleInformation expectedFeeScheduleInformation) throws XifinDataAccessException {
		Prc prc = prcList.get(0);

		//Fee Schedule ID
		assertEquals(prc.getPrcAbbrev(), expectedFeeScheduleInformation.getFeeScheduleId());
		//Name
		assertEquals(prc.getDescr(), expectedFeeScheduleInformation.getName());
		//Account Type
		assertEquals(getAccountType(prc), expectedFeeScheduleInformation.getAccountType());
		//Facility
		String facAbbrevInDB = facilityDao.getFacByFacId(prc.getFacId()).getAbbrv();
		assertEquals(facAbbrevInDB, expectedFeeScheduleInformation.getFacility());
		//Basis Type
		assertEquals(getBasisType(prc), expectedFeeScheduleInformation.getBasisType());
		//Price Type
		assertEquals(getPriceType(prc), expectedFeeScheduleInformation.getPriceType());
		//Display Option:  Not saved to DB, no need to check
		//Discountable
		assertEquals(prc.isNoDisc, !expectedFeeScheduleInformation.isDiscountable());
	}

	private void verifyFeeScheduleTestCodePricingSaved(List<PrcTest> prcTestList, List<Pricing> expectedPricingList) throws XifinDataAccessException {
		if (prcTestList.size() == expectedPricingList.size()){
			for (int i=0; i<prcTestList.size(); i++){
				//Test Abbrev
				String testAbbrevInDB = testDao.getTestByTestId(prcTestList.get(i).getTestId()).getTestAbbrev();
				assertEquals(testAbbrevInDB, expectedPricingList.get(i).getTestCode());
				//Effective Date
				assertEquals(prcTestList.get(i).getEffDt(), expectedPricingList.get(i).getEffectiveDate());
				//Expiration Date
				assertEquals(prcTestList.get(i).getExpDt(), expectedPricingList.get(i).getExpirationDate());
				//Price
				assertEquals(prcTestList.get(i).getPriceAsMoney(), expectedPricingList.get(i).getNewPrice());
			}
		}
		else{
            fail("       Pricing data are saved in DB.");
		}
	}

	private String getAccountType(Prc prc) {
		String value;

		if (prc.isClientBased) {
			value = ACCOUNT_TYPE_CLIENT;
		} else {
			value = ACCOUNT_TYPE_NON_CLIENT;
		}

		return value;
	}

	private String getBasisType(Prc prc) {
		String value;

		if (prc.isTestBased) {
			value = BASIS_TYPE_TEST_CODE;
		} else {
			value = BASIS_TYPE_PROCEDURE_CODE;
		}

		return value;
	}

	private String getPriceType(Prc prc){
		String value = null;

		if ( !prc.isRetail && !prc.isExpect) {
			value = PRICE_TYP_NORMAL;
		} else if(!prc.isRetail && prc.isExpect) {
			value = PRICE_TYP_EXPECT;
		} else if(prc.isRetail) {
			value = PRICE_TYP_RETAIL;
		}

		return value;
	}

	private FeeScheduleInformation getValuesInFeeScheduleInformation() {
		FeeScheduleInformation feeScheduleInformation = new FeeScheduleInformation();

		//Fee Schedule ID
		feeScheduleInformation.setFeeScheduleId(feeSchedule.feeScheduleIdTxt().getAttribute("value"));
		//Name
		feeScheduleInformation.setName(feeSchedule.nameInput().getAttribute("value"));
		//Account Type
		feeScheduleInformation.setAccountType(feeSchedule.accountTypeTxt().getAttribute("value"));
		//Facility
		feeScheduleInformation.setFacility(feeSchedule.facilityChoosenTxt().getText());
		//Basis Type
		feeScheduleInformation.setBasisType(feeSchedule.basisTypeTxt().getAttribute("value"));
		//Price Type
		if (feeSchedule.priceTypeTxt().getAttribute("value").length()<2){
			feeScheduleInformation.setPriceType(PRICE_TYP_NORMAL);
		}else {
			feeScheduleInformation.setPriceType(feeSchedule.priceTypeTxt().getAttribute("value"));
		}
		//Display Option
		feeScheduleInformation.setDisplayOption(feeSchedule.displayOptionTxt().getAttribute("value"));
		//Discountable
		feeScheduleInformation.setDiscountable(feeSchedule.discountableChk().isSelected());

		return feeScheduleInformation;
	}

	private Pricing setProcedureCodeValuesInPricing(boolean isPreLoaded, String procedureCode, String mod, String name, Date effectiveDate, Date expirationDate, Money mcExp, Money current, Money newPrice, boolean isDeleted, String row) throws Exception {
		Pricing pricing = new Pricing();

		if (!isPreLoaded){
			//Procedure Code
			setInputValue(feeSchedule.procedureCodeInputInPricingTbl(row), procedureCode);
			//Mod
			try {
				selectItemByVal(feeSchedule.modDropdownInPricingTbl(row), mod);
			}catch (Exception e){
				//do nothing;
			}
			//Effective Date
			feeSchedule.effectiveDateInputInPricingTbl(row).clear();
			setInputValue(feeSchedule.effectiveDateInputInPricingTbl(row), DATE_FORMAT_MMDDYYYY.format(effectiveDate));
			//Expiration Date
			feeSchedule.expirationDateInputInPricingTbl(row).clear();
			try {
				setInputValue(feeSchedule.expirationDateInputInPricingTbl(row), DATE_FORMAT_MMDDYYYY.format(expirationDate));
			}catch(Exception e){
				//do nothing;
			}
			//New Price
			feeSchedule.setInputValue(feeSchedule.newPriceInputInPricingTbl(row), String.valueOf(newPrice));
			//Delete
			if (isDeleted){
				feeSchedule.deleteCheckboxInPricingTbl(row).click();
			}
		}

		pricing.setProcedureCode(procedureCode);
		pricing.setMod(mod);
		pricing.setName(name);
		pricing.setEffectiveDate(effectiveDate);
		pricing.setExpirationDate(expirationDate);
		pricing.setMcExp(mcExp);
		pricing.setCurrent(current);
		pricing.setNewPrice(newPrice);
		pricing.setDeleted(isDeleted);

		return pricing;
	}

	private Pricing getProcedureCodeValuesInPricing(String index) throws ParseException {
		Pricing pricing = new Pricing();
		//Procedure Code
		pricing.setProcedureCode(feeSchedule.procedureCodeLnkInPricingTbl(index).getText().trim());
		//Mod
        if (feeSchedule.modDropdownTxtInPricingTbl(index).getAttribute("title").trim().length() >= 2) {
            pricing.setMod(feeSchedule.modDropdownTxtInPricingTbl(index).getAttribute("title").trim());
        }
        //Name
		pricing.setName(feeSchedule.nameTxtInPricingTbl(index).getText().trim());
		//Effective Date
		Date effectiveDate = new java.sql.Date(DATE_FORMAT_MMDDYYYY.parse(feeSchedule.effectiveDateTxtInPricingTbl(index).getAttribute("title").trim()).getTime());
		pricing.setEffectiveDate(effectiveDate);
		//Expiration Date
		try {
			Date expirationDate = new java.sql.Date(DATE_FORMAT_MMDDYYYY.parse(feeSchedule.expirationDateTxtInPricingTbl(index).getAttribute("title").trim()).getTime());
			pricing.setExpirationDate(expirationDate);
		}catch(Exception e){
			logger.info("       No Expiration Date.");
			pricing.setExpirationDate(null);
		}
		//MC Exp
		pricing.setMcExp(new Money(0));
		//Current
		pricing.setCurrent(new Money(feeSchedule.currentTxtInPricingTbl(index).getAttribute("title").trim()));
		//New Priced
		pricing.setNewPrice(new Money(feeSchedule.newPriceTxtInPricingTbl(index).getAttribute("title").trim()));
		//Delete
		pricing.setDeleted(feeSchedule.deleteCheckboxInPricingTbl(index).isSelected());

		return pricing;
	}

	private void verifyFeeScheduleProcedureCodePricingSaved(List<PrcProc> prcProcList, List<Pricing> expectedPricingList) {
		if (prcProcList.size() == expectedPricingList.size()){
			for (int i=0; i<prcProcList.size(); i++){
				//Procedure Code
				assertEquals(prcProcList.get(i).getProcId(), expectedPricingList.get(i).getProcedureCode());
				//Mod
				assertEquals(prcProcList.get(i).getModId(), expectedPricingList.get(i).getMod());
				//Effective Date
				assertEquals(prcProcList.get(i).getEffDt(), expectedPricingList.get(i).getEffectiveDate());
				//Expiration Date
				assertEquals(prcProcList.get(i).getExpDt(), expectedPricingList.get(i).getExpirationDate());
				//Price
				assertEquals(prcProcList.get(i).getPriceAsMoney(), expectedPricingList.get(i).getNewPrice());
			}
		}
		else{
            fail("       Pricing data are saved in DB.");
		}
	}

	private void verifySearchResultsDisplayed(List<String> pricingList, String feeScheduleId) {
		//Price Table ID
		assertEquals(feeSchedule.priceTableId().getText().trim(), feeScheduleId);
		//ID
		feeSchedule.clientIdSearch().sendKeys(pricingList.get(0));
		WebElement id = feeSchedule.clientPayorPricingTable().findElement(By.xpath("//tbody/tr/td[contains(text(), '" + pricingList.get(0) + "')]"));
		Assert.assertTrue(id.isDisplayed());
		//Name
		WebElement name = feeSchedule.clientPayorPricingTable().findElement(By.xpath("//tbody/tr/td[contains(text(), '" + pricingList.get(1) + "')]"));
		Assert.assertTrue(name.isDisplayed());
		//Effective Date
		WebElement effectiveDate = feeSchedule.clientPayorPricingTable().findElement(By.xpath("//tbody/tr/td[contains(text(), '" + pricingList.get(2) + "')]"));
		Assert.assertTrue(effectiveDate.isDisplayed());
	}


}
