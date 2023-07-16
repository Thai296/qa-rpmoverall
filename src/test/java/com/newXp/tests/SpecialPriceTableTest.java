package com.newXp.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import com.mbasys.common.utility.Money;
import com.mbasys.mars.ejb.entity.prc.Prc;
import com.mbasys.mars.ejb.entity.prcTest.PrcTest;
import com.overall.fileMaintenance.pricing.FeeSchedule;
import com.overall.fileMaintenance.pricing.SpecialPriceTable;
import com.overall.menu.MenuNavigation;
import com.overall.utils.SpecialPriceTableUtils;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.utils.RandomCharacter;
import com.xifin.utils.TimeStamp;

import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.utils.SeleniumBaseTest;
import domain.fileMaintenance.specialpricetable.Pricing;
import domain.fileMaintenance.specialpricetable.SpecialPriceTableInformation;
import domain.filemaint.specialPriceTable.ClientPricingTable;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestResult;

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
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.mbasys.mars.ejb.entity.cln.Cln;
import com.mbasys.mars.ejb.entity.clnDt.ClnDt;
import com.mbasys.mars.ejb.entity.qRetro.QRetro;
import com.mbasys.mars.ejb.entity.qRetroAccn.QRetroAccn;
import com.overall.client.clientProcessing.ClientPricingConfiguration;
import com.overall.utils.ClientPricingConfigurationUtils;
import com.xifin.qa.dao.rpm.domain.ClientRetroPriceImpact;

import domain.client.pricingconfiguration.Header;

//import com.xifin.util.Money;


public class SpecialPriceTableTest extends SeleniumBaseTest {
	private RandomCharacter randomCharacter;
	private static final String ACCOUNT_TYPE_CLIENT = "Client";
	private static final String ACCOUNT_TYPE_NON_CLIENT = "Non-Client";
	private static final SimpleDateFormat DATE_FORMAT_MMDDYYYY = new SimpleDateFormat("MM/dd/yyyy");
	private static final String DATE_FORMAT = "MM/dd/yyyy";
	private TimeStamp timeStamp;
	private SpecialPriceTable specialPriceTable;
	private SpecialPriceTableUtils specialPriceTableUtils;
	private XifinAdminUtils xifinAdminUtils;
	private static final int SPECIAL_PRICE_TABLE = 0;
	private static final int UNPROCESSED = 0;
	private static final int PROCESSED = 1;
	private String oldSpecialPriceId;
	private String newSpecialPriceId;
	private static final String LAST_ROW = "last()";
	private static final int FIRST_ROW = 2;
	private PrcTest prcTest;
	private Prc prc;
	private ClnDt clnDt;
	private com.mbasys.mars.ejb.entity.test.Test test;
	private MenuNavigation navigation;
	private ClientPricingConfiguration clientPricingConfiguration;
	private ClientPricingConfigurationUtils clientPricingConfigurationUtils;
	private static final String CLIENT_ABBREV = "200";
	private int oldSpecialPriceIdInClnDt;
	private String filePath1;
	private String filePath2;

	@BeforeMethod(alwaysRun = true)
	@Parameters({"ssoUsername", "ssoPassword"})
	public void beforeMethod(String ssoUsername, String ssoPassword, Method method) throws Exception {
		try {
			logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
			logIntoSso(ssoUsername, ssoPassword);
			navigation = new MenuNavigation(driver, config);
			navigation.navigateToSpecialPriceTablePage();
		} catch (Exception e) {
			logger.error("Error running BeforeMethod", e);
		}
	}
	
	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) throws Exception {
		String methodName = result.getMethod().getMethodName();
		logger.info("*** Starting revert data ***");
		switch (methodName) {
		case "testXPR_1069":
			deleteTestFile(filePath1);
			deleteTestFile(filePath2);
			logger.info("*** Reverting data is successfully ***");
		case "verifyUsersAreAbleToUpdateSpecialPriceID":
			specialPriceTableUtils.updateSpecialPriceTableIdWithOldValue(newSpecialPriceId, oldSpecialPriceId);
			logger.info("*** Reverting data is successfully ***");
			break;
		case "verifyThereIsANewButtonToChangingSpecialPriceTableID":
		case "verifyThereIsAnErrorWhenUsersChangeSpecialPriceTableIDToExistingOne":
			logger.info("*** No need to revert ***");
			break;
		case "verifyRetroPricingEngineIsAppliedForSpecialPriceTable":
			clientPricingConfigurationUtils.updateClnDtByClnAbbrev(clnDt, oldSpecialPriceIdInClnDt);
			specialPriceTableUtils.deletePrcTest(prcTest);
			logger.info("*** Reverting data is successfully ***");
			break;
		default:
			logger.info("*** No need to revert ***");
		}
	}

	@BeforeSuite(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "xapEnv", "disableBrowserPlugins", "specialPriceTableId1", "specialPriceTableId2", "specialPriceTableId3", "specialPriceTableId4"})
	public void BeforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, String xapEnv, @Optional String disableBrowserPlugins, String specialPriceTableId1, String specialPriceTableId2, String specialPriceTableId3, String specialPriceTableId4)
	{
		try
		{
			logger.info("Running BeforeSuite");
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			cleanUpSpecialPriceTableTestData(Arrays.asList(specialPriceTableId1, specialPriceTableId2, specialPriceTableId4));
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
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias",  "ssoUsername", "ssoPassword", "xapEnv", "disableBrowserPlugins", "specialPriceTableId1", "specialPriceTableId2", "specialPriceTableId3", "specialPriceTableId4"})
	public void AfterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias,  String ssoUsername, String ssoPassword, String xapEnv, @Optional String disableBrowserPlugins, String specialPriceTableId1, String specialPriceTableId2, String specialPriceTableId3, String specialPriceTableId4)
	{
		try
		{
			logger.info("Running AfterSuite");
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			cleanUpSpecialPriceTableTestData(Arrays.asList(specialPriceTableId1, specialPriceTableId2, specialPriceTableId4));
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



	@Test(priority = 1, description = "Create new Client Special Price Table from existing table, Individual option")
	@Parameters({"specialPriceTableId1", "copySpecialPriceTableId"})
	public void testXPR_1067(String specialPriceTableId1, String copySpecialPriceTableId) throws Exception {
		logger.info("===== Testing - testXPR_1067 =====");
		randomCharacter = new RandomCharacter(driver);

		specialPriceTable = new SpecialPriceTable(driver, wait);

		logger.info("*** Expected Results: - Verify that the Load Special Price Table page is displayed");
		assertTrue(isOnLoadSpecialPriceTablePage());

		logger.info("*** Action: - Select Individually Display Option");
		selectDropDownByText(specialPriceTable.loadSpecialPriceTablePageDisplayOptionDdl(), "Individually");

		logger.info("*** Action: - Enter a new Special Price Table ID");
		setInputValue(specialPriceTable.loadSpecialPriceTablePageSpePrcTblIdInput(), specialPriceTableId1);

		logger.info("*** Expected Results: - Verify that the Special Price Table page is displayed");
		assertTrue(isElementPresent(specialPriceTable.specialPriceTablePageTitle(),5),"        Special Price Table Page Title is displayed.");
		assertTrue(specialPriceTable.loadSpecialPriceTablePageTitleTxt().getText().equals("Special Price Table"),"        The title of Special Price Table page should be 'Special Price Table'.");

		logger.info("*** Action: - Enter Special Price Table Information");
		SpecialPriceTableInformation expectedSpecialPriceTableInformation = setValuesInSpecialPriceTableInformation(specialPriceTableId1, "Client", "All");

		logger.info("*** Action: - Select Create From Table ID option and enter an existing Table ID");
		clickHiddenPageObject(specialPriceTable.specialPriceTablePageTableIdRad(), 0);
		setInputValue(specialPriceTable.specialPriceTableTableIdInput(), copySpecialPriceTableId);

		logger.info("*** Action: - Enter the date in 'Effective as of' and 'Create with Effective Dates' fields");
		String effectiveDateStr = "06/01/2038";
		setInputValue(specialPriceTable.specialPriceTablePageImportEffDateInput(), effectiveDateStr);
		setInputValue(specialPriceTable.specialPriceTablePageCreateWithEffDateInput(), effectiveDateStr);

		Date effectiveDate = new Date(DATE_FORMAT_MMDDYYYY.parse(effectiveDateStr).getTime());
		Pricing expectedPricing = setTestCodeValuesInPricing(true, "1000", testDao.getTestByTestAbbrev("1000").getName().trim(), effectiveDate, null, new Money(0), new Money(10.00), new Money(10.00), false, "last()");

        List<Pricing> expectedPricingList = new ArrayList<>();
        expectedPricingList.add(expectedPricing);

		logger.info("*** Action: - Click Save button");
		clickHiddenPageObject(specialPriceTable.saveBtn(), 0);

		logger.info("*** Expected Results: - Verify that the Special Price Table page is displayed");
		wait.until(ExpectedConditions.visibilityOf(specialPriceTable.appendBtn()));

		logger.info("*** Expected Results: - Verify that the Test Code created from the existing table is displayed in Pricing table in UI");
		Pricing actualPricing = getTestCodeValuesInPricing("last()");

		List<Pricing> actualPricingList = new ArrayList<>();
		actualPricingList.add(actualPricing);
		assertEquals(actualPricingList, expectedPricingList);

		logger.info("*** Action: - Add a new Test Code in the Pricing table");
		clickHiddenPageObject(specialPriceTable.addBtn(), 0);
		expectedPricing = setTestCodeValuesInPricing(false, "102X", testDao.getTestByTestAbbrev("102X").getName().trim(), effectiveDate, null, new Money(0), new Money(20.00), new Money(20.00), false, "last()");
		expectedPricingList.add(expectedPricing);

		logger.info("*** Action: - Create a Test Code Append TXT file");
		File file = new File(System.getProperty("user.dir")).getParentFile();
		String appendFileName = "SpecialPriceTableAppend_" + randomCharacter.getRandomAlphaString(4) + ".txt";
		String filePath = file +File.separator+"src"+File.separator +"test"+File.separator+"resources"+File.separator+appendFileName;
		createAppendFile(filePath, Arrays.asList("300"), Arrays.asList(effectiveDate), Arrays.asList(effectiveDate), Arrays.asList(new Money(30.00)), Arrays.asList(""));

		logger.info("*** Action: - Click Choose File button and select a Test Code Append file and click Append button");
		clickHiddenPageObject(specialPriceTable.chooseFileBtnInAppendFile(), 0);
		uploadFile(specialPriceTable.chooseFileBtnInAppendFile(), appendFileName);
		clickHiddenPageObject(specialPriceTable.appendBtn(), 0);
		logger.info("*** Expected Results: - Verify that the Test Code in the Append file is added to the Pricing table");
		expectedPricing = setTestCodeValuesInPricing(true, "300", testDao.getTestByTestAbbrev("300").getName().trim(), effectiveDate, effectiveDate, new Money(0), new Money(30.00), new Money(30.00), false, "last()");
		expectedPricingList.add(expectedPricing);

		logger.info("*** Action: - Click Save And Clear button");
		clickHiddenPageObject(specialPriceTable.saveAndClearBtn(), 0);

		logger.info("*** Expected Results: - Verify that it's back to the load Special Price Table page");
		assertTrue(isOnLoadSpecialPriceTablePage());

		logger.info("*** Expected Results: - Verify that the Special Price Table Information data are saved properly in DB");
		List<Prc> prcList = prcDao.getPrcByPrcAbbrev(specialPriceTableId1);
		verifySpecialPriceTableInformationSaved(prcList, expectedSpecialPriceTableInformation);

		logger.info("*** Expected Results: - Verify that the Special Price Table data are saved properly in DB");
		List<PrcTest> prcTestList = rpmDao.getPrcTestByPrcAbbrev(testDb, specialPriceTableId1);
		verifySpecialPriceTableTestCodePricingSaved(prcTestList, expectedPricingList);

		logger.info("*** Action: - Select All Display Option");
		selectDropDownByText(specialPriceTable.loadSpecialPriceTablePageDisplayOptionDdl(), "All");

		logger.info("*** Action: - Enter the newly created Special Price Table ID");
		setInputValue(specialPriceTable.loadSpecialPriceTablePageSpePrcTblIdInput(), specialPriceTableId1);

		logger.info("*** Expected Results: - Verify that the Special Price Table page is displayed");
		wait.until(ExpectedConditions.visibilityOf(specialPriceTable.appendBtn()));

		logger.info("*** Expected Results: - Verify that the data in Special Price Table Information are displayed properly in UI");
		SpecialPriceTableInformation actualSpecialPriceTableInformation = getValuesInSpecialPriceTableInformation();
		assertEquals(actualSpecialPriceTableInformation, expectedSpecialPriceTableInformation);

		logger.info("*** Expected Results: - Verify that the data in Pricing are displayed properly in UI");
		actualPricingList.clear();
		for (int i=0; i<prcTestList.size(); i++) {
			actualPricingList.add(getTestCodeValuesInPricing(String.valueOf(i+2)));
		}
		assertEquals(actualPricingList, expectedPricingList);

		logger.info("*** Action: - Click Reset button");
		clickHiddenPageObject(specialPriceTable.resetBtn(), 0);

		logger.info("*** Actions: - Delete the test TXT file");
		deleteTestFile(filePath);
	}

	@Test(priority = 1, description = "Create new Non-Client Special Price Table by importing file, All option")
	@Parameters({"specialPriceTableId2"})
	public void createNewNonClnSpecialPrcTblByImportingFile(String specialPriceTableId2) throws Exception {
		logger.info("===== Testing - createNewNonClnSpecialPrcTblByImportingFile =====");
		randomCharacter = new RandomCharacter(driver);
		specialPriceTable = new SpecialPriceTable(driver, wait);

		logger.info("*** Expected Results: - Verify that the Load Special Price Table page is displayed");
		assertTrue(isOnLoadSpecialPriceTablePage());

		logger.info("*** Action: - Select All Display Option");
		selectDropDownByText(specialPriceTable.loadSpecialPriceTablePageDisplayOptionDdl(), "All");

		logger.info("*** Action: - Enter a new Special Price Table ID");
		setInputValue(specialPriceTable.loadSpecialPriceTablePageSpePrcTblIdInput(), specialPriceTableId2);

		logger.info("*** Expected Results: - Verify that the Special Price Table page is displayed");
		assertTrue(isElementPresent(specialPriceTable.specialPriceTablePageTitle(),5),"        Special Price Table Page Title is displayed.");
		assertTrue(specialPriceTable.loadSpecialPriceTablePageTitleTxt().getText().equals("Special Price Table"),"        The title of Special Price Table page should be 'Special Price Table'.");

		logger.info("*** Action: - Enter Special Price Table Information");
		SpecialPriceTableInformation expectedSpecialPriceTableInformation = setValuesInSpecialPriceTableInformation(specialPriceTableId2, "Non-Client", "All");

		String effectiveDateStr = "06/01/2038";
		Date effectiveDate = new Date(DATE_FORMAT_MMDDYYYY.parse(effectiveDateStr).getTime());

		logger.info("*** Action: - Create a Test Code Import CSV file");
		File file = new File(System.getProperty("user.dir")).getParentFile();
		String importFileName = "SpecialPriceTableImport_" + randomCharacter.getRandomAlphaString(4) + ".csv";
		filePath1 = file +File.separator+"src"+File.separator +"test"+File.separator+"resources"+File.separator+importFileName;
		createAppendFile(filePath1, Arrays.asList("1000"), Arrays.asList(effectiveDate), Arrays.asList(effectiveDate), Arrays.asList(new Money(40.00)), Arrays.asList(""));

		logger.info("*** Action: - Select Create From Import File option");
		clickHiddenPageObject(specialPriceTable.specialPriceTablePageImportCreateRad(), 0);

		logger.info("*** Action: - Click Choose File button and select the Test Code Import CSV file");
		clickHiddenPageObject(specialPriceTable.chooseFileBtnInSpecialPriceTableInformation(), 0);
		uploadFile(specialPriceTable.chooseFileBtnInSpecialPriceTableInformation(), importFileName);

		logger.info("*** Action: - Click Save button");
		clickHiddenPageObject(specialPriceTable.saveBtn(), 0);

		Pricing expectedPricing = setTestCodeValuesInPricing(true, "1000", testDao.getTestByTestAbbrev("1000").getName().trim(), effectiveDate, effectiveDate, new Money(0), new Money(40.00), new Money(40.00), false, "last()");
		List<Pricing> expectedPricingList = new ArrayList<>();
		expectedPricingList.add(expectedPricing);

		logger.info("*** Expected Results: - Verify that the Special Price Table page is displayed");
		wait.until(ExpectedConditions.visibilityOf(specialPriceTable.appendBtn()));

		logger.info("*** Expected Results: - Verify that the Test Code created from the Import File is displayed in Pricing table in UI");
		Pricing actualPricing = getTestCodeValuesInPricing("last()");

		List<Pricing> actualPricingList = new ArrayList<>();
		actualPricingList.add(actualPricing);
		assertEquals(actualPricingList, expectedPricingList);

		logger.info("*** Action: - Add a new Test Code in the Pricing table");
		clickHiddenPageObject(specialPriceTable.addBtn(), 0);
		expectedPricing = setTestCodeValuesInPricing(false, "102X", testDao.getTestByTestAbbrev("102X").getName().trim(), effectiveDate, null, new Money(0), new Money(20.00), new Money(20.00), false, "last()");
		expectedPricingList.add(expectedPricing);

		actualPricingList.add(actualPricing);

		logger.info("*** Action: - Create a Test Code Append CSV file");
		String appendFileName = "SpecialPriceTableAppend_" + randomCharacter.getRandomAlphaString(4) + ".csv";
		filePath2 = file +File.separator+"src"+File.separator +"test"+File.separator+"resources"+File.separator+appendFileName;
		createAppendFile(filePath2, Arrays.asList("300"), Arrays.asList(effectiveDate), Arrays.asList(effectiveDate), Arrays.asList(new Money(50.00)), Arrays.asList(""));

		logger.info("*** Action: - Click Choose File button and select a Test Code Append file and click Append button");
		clickHiddenPageObject(specialPriceTable.chooseFileBtnInAppendFile(), 0);
		uploadFile(specialPriceTable.chooseFileBtnInAppendFile(), appendFileName);
		clickHiddenPageObject(specialPriceTable.appendBtn(), 0);

		logger.info("*** Expected Results: - Verify that the Test Code in the Append file is added to the Pricing table");
		expectedPricing = setTestCodeValuesInPricing(true, "300", testDao.getTestByTestAbbrev("300").getName().trim(), effectiveDate, effectiveDate, new Money(0), new Money(50.00), new Money(50.00), false, "last()");
		expectedPricingList.add(expectedPricing);

		logger.info("*** Action: - Click Save And Clear button");
		clickHiddenPageObject(specialPriceTable.saveAndClearBtn(), 0);

		logger.info("*** Expected Results: - Verify that it's back to the load Special Price Table page");
		assertTrue(isOnLoadSpecialPriceTablePage());

		logger.info("*** Expected Results: - Verify that the Special Price Table Information data are saved properly in DB");
		List<Prc> prcList = prcDao.getPrcByPrcAbbrev(specialPriceTableId2);
		verifySpecialPriceTableInformationSaved(prcList, expectedSpecialPriceTableInformation);

		logger.info("*** Expected Results: - Verify that the Special Price Table Pricing data are saved properly in DB");
		List<PrcTest> prcTestList = rpmDao.getPrcTestByPrcAbbrev(testDb, specialPriceTableId2);
		verifySpecialPriceTableTestCodePricingSaved(prcTestList, expectedPricingList);

		logger.info("*** Action: - Select All Display Option");
		selectDropDownByText(specialPriceTable.loadSpecialPriceTablePageDisplayOptionDdl(), "All");

		logger.info("*** Action: - Enter the newly created Special Price Table ID");
		setInputValue(specialPriceTable.loadSpecialPriceTablePageSpePrcTblIdInput(), specialPriceTableId2);

		logger.info("*** Expected Results: - Verify that the Special Price Table page is displayed");
		wait.until(ExpectedConditions.visibilityOf(specialPriceTable.appendBtn()));

		logger.info("*** Expected Results: - Verify that the data in Special Price Table Information are displayed properly in UI");
		SpecialPriceTableInformation actualSpecialPriceTableInformation = getValuesInSpecialPriceTableInformation();
		assertEquals(actualSpecialPriceTableInformation, expectedSpecialPriceTableInformation);

		logger.info("*** Expected Results: - Verify that the data in Pricing are displayed properly in UI");
		actualPricingList.clear();
		for (int i=0; i<prcTestList.size(); i++) {
			actualPricingList.add(getTestCodeValuesInPricing(String.valueOf(i+2)));
		}
		assertEquals(actualPricingList, expectedPricingList);

		logger.info("*** Action: - Click Reset button");
		clickHiddenPageObject(specialPriceTable.resetBtn(), 0);
	}


	@Test(priority = 1, description = "Load existing Special Price Table and View Clients")
	@Parameters({"specialPriceTableId3"})
	public void loadExistingSpecialPrcTblAndViewCln(String specialPriceTableId3) throws Exception {
		logger.info("===== Testing - loadExistingSpecialPrcTblAndViewCln =====");
		randomCharacter = new RandomCharacter(driver);
		timeStamp = new TimeStamp(driver);
		specialPriceTable = new SpecialPriceTable(driver, wait);
		specialPriceTableUtils = new SpecialPriceTableUtils(driver, config, wait);
		Prc prc = prcDao.getRandomPrcByFeeScheduleAndHasClient(SPECIAL_PRICE_TABLE);

		logger.info("*** Expected Results: - Verify that the Load Special Price Table page is displayed");
		assertTrue(isOnLoadSpecialPriceTablePage());

		logger.info("*** Action: - Select All Display Option");
		selectDropDownByText(specialPriceTable.loadSpecialPriceTablePageDisplayOptionDdl(), "All");

		logger.info("*** Action: - Enter an existing Special Price Table ID");
		setInputValue(specialPriceTable.loadSpecialPriceTablePageSpePrcTblIdInput(), prc.getPrcAbbrev());

		logger.info("*** Expected Results: - Verify that the Special Price Table page is displayed");
		wait.until(ExpectedConditions.visibilityOf(specialPriceTable.appendBtn()));

		logger.info("*** Action: - Click View Clients button");
		clickHiddenPageObject(specialPriceTable.viewClientsBtn(), 0);

		String parentWindow = switchToPopupWin();

		logger.info("*** Expected Results: - Verify that the Client Search Result - Client Pricing page is displayed");
		List<ClientPricingTable> clnPricingTblAct = specialPriceTableUtils.getClientPricingTable();
		List<ClientPricingTable> clnPricingTblExp = specialPriceTableUtils.getClientPricingTableFromDB(prc);
		
		assertEquals(specialPriceTable.priceTableIDViewPayorPopup().getText(), prc.getPrcAbbrev(), "Special Price Table ID should be displayed correctly in Client Pricing popup");
		assertEquals(specialPriceTable.priceTableNameViewPayorPopup().getText(), prc.getDescr(), "Special Price Table Name should be displayed correctly in Client Pricing popup");
		assertEquals(clnPricingTblAct, clnPricingTblExp, "Client Pricing Table should be displayed correctly");
		
		clickHiddenPageObject(specialPriceTable.closeButton(), 0);
		switchToParentWin(parentWindow);

		logger.info("*** Action: - Click Reset button");
		clickHiddenPageObject(specialPriceTable.resetBtn(), 0);
	}

	@Test(priority = 1, description = "Verify Helps")
	@Parameters({})
	public void verifyHelps() throws Exception {
		logger.info("===== Testing - verifyHelps =====");
		randomCharacter = new RandomCharacter(driver);

		timeStamp = new TimeStamp(driver);
		specialPriceTable = new SpecialPriceTable(driver, wait);

		logger.info("*** Expected Results: - Verify that the Load Special Price Table page is displayed");
		assertTrue(isOnLoadSpecialPriceTablePage());

		logger.info("*** Action: - Click Help icon in Special Price Table header section");
		clickHiddenPageObject(specialPriceTable.loadSpecialPriceTablePageHeaderHelpIco(), 0);

		logger.info("*** Expected Results: - Verify that Special Price Table Header Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/pricing_menu/p_special_price_table_header.htm", "Special Price Table Header"));

		logger.info("*** Action: - Click Help icon in Load Special Price Table section");
		clickHiddenPageObject(specialPriceTable.helpIconBtnInLoadSpecialPriceTable(), 0);

		logger.info("*** Expected Results: - Verify that Load Special Price Table Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/pricing_menu/p_special_price_table_load_special_price_table.htm", "Special Price Table ID Selection"));

		logger.info("*** Action: - Select All Display Option");
		selectDropDownByText(specialPriceTable.loadSpecialPriceTablePageDisplayOptionDdl(), "All");

		logger.info("*** Action: - Enter a new Special Price Table ID");
		String specialPriceTableId = randomCharacter.getRandomAlphaString(8);
		setInputValue(specialPriceTable.loadSpecialPriceTablePageSpePrcTblIdInput(), specialPriceTableId);

		logger.info("*** Expected Results: - Verify that the Special Price Table Information page is displayed");
		assertTrue(isElementPresent(specialPriceTable.specialPriceTablePageTitle(),5),"        Special Price Table Page Title is displayed.");
		assertTrue(specialPriceTable.loadSpecialPriceTablePageTitleTxt().getText().equals("Special Price Table"),"        The title of Special Price Table page should be 'Special Price Table'.");

		logger.info("*** Action: - Click Help icon in Special Price Table Information section");
		clickHiddenPageObject(specialPriceTable.helpIconBtnInSpecialPriceTableInformation(), 0);

		logger.info("*** Expected Results: - Verify that Fee Schedule Information Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/pricing_menu/p_special_price_table_special_price_table_info.htm", "Special Price Table Information"));

		logger.info("*** Action: - Enter Fee Schedule Information");
		setValuesInSpecialPriceTableInformation(specialPriceTableId, "Non-Client", "All");

		logger.info("*** Action: - Click Save button");
		clickHiddenPageObject(specialPriceTable.saveBtn(), 0);

		logger.info("*** Expected Results: - Verify that the Special Price Table page is displayed");
		wait.until(ExpectedConditions.visibilityOf(specialPriceTable.appendBtn()));

		logger.info("*** Action: - Click Help icon in Pricing section");
		clickHiddenPageObject(specialPriceTable.pricingSectionHelpIcon(), 0);

		logger.info("*** Expected Results: - Verify that Fee Schedule Pricing Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/pricing_menu/p_special_price_table_pricing.htm", "Pricing"));

		logger.info("*** Action: - Click Help icon in Append File section");
		clickHiddenPageObject(specialPriceTable.appendFileSectionHelpIcon(), 0);

		logger.info("*** Expected Results: - Verify that Fee Schedule Append File Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/pricing_menu/p_special_price_table_append_file.htm", "Append File"));

		logger.info("*** Action: - Click View Clients button");
		clickHiddenPageObject(specialPriceTable.viewClientsBtn(), 0);

		String parentWindow = switchToPopupWin();

		logger.info("*** Action: - Click Help icon in Client Pricing popup");
		clickHiddenPageObject(specialPriceTable.helpIconBtnInClientSearchResults(), 0);

		logger.info("*** Expected Results: - Verify that Client Pricing Search Results Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/pricing_menu/p_special_price_table_client_pricing.htm", "Client Pricing"));

		driver.close();
		switchToParentWin(parentWindow);
		/*
		logger.info("*** Action: - Click View Payors button");
		clickHiddenPageObject(feeSchedule.viewPayorsBtn());

		parentWindow = switchToPopupWin();

		logger.info("*** Action: - Click Help icon in Payor Pricing popup");
		clickHiddenPageObject(feeSchedule.helpIconBtnInPayorSearchResults());

		logger.info("*** Expected Results: - Verify that Fee Schedule Pricing Search Results Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("p_fee_schedule_payor_pricing.htm", "Payor Pricing"));

		driver.close();
		switchToParentWin(parentWindow);
		*/
		logger.info("*** Action: - Click Reset button");
		clickHiddenPageObject(specialPriceTable.resetBtn(), 0);
	}

	@Test(priority = 1, description = "Delete Test Code")
	@Parameters({"specialPriceTableId4"})
	public void deleteTestCode(String specialPriceTableId4) throws Exception {
		logger.info("===== Testing - deleteTestCode =====");
		randomCharacter = new RandomCharacter(driver);

		specialPriceTable = new SpecialPriceTable(driver, wait);

		logger.info("*** Expected Results: - Verify that the Load Special Price Table page is displayed");
		assertTrue(isOnLoadSpecialPriceTablePage());

		logger.info("*** Action: - Select All Display Option");
		selectDropDownByText(specialPriceTable.loadSpecialPriceTablePageDisplayOptionDdl(), "All");

		logger.info("*** Action: - Enter a new Special Price Table ID");
		setInputValue(specialPriceTable.loadSpecialPriceTablePageSpePrcTblIdInput(), specialPriceTableId4);

		logger.info("*** Expected Results: - Verify that the Special Price Table page is displayed");
		assertTrue(isElementPresent(specialPriceTable.specialPriceTablePageTitle(),5),"        Special Price Table Page Title is displayed.");
		assertTrue(specialPriceTable.loadSpecialPriceTablePageTitleTxt().getText().equals("Special Price Table"),"        The title of Special Price Table page should be 'Special Price Table'.");

		logger.info("*** Action: - Enter Special Price Table Information");
		setValuesInSpecialPriceTableInformation(specialPriceTableId4, "Client", "All");

		String effectiveDateStr = "06/01/2038";
		Date effectiveDate = new Date(DATE_FORMAT_MMDDYYYY.parse(effectiveDateStr).getTime());

		List<Pricing> expectedPricingList = new ArrayList<>();

		logger.info("*** Action: - Click Save button");
		clickHiddenPageObject(specialPriceTable.saveBtn(), 0);

		logger.info("*** Expected Results: - Verify that the Special Price Table page is displayed");
		wait.until(ExpectedConditions.visibilityOf(specialPriceTable.appendBtn()));

		List<Pricing> actualPricingList = new ArrayList<>();

		logger.info("*** Action: - Add a new Test Code in the Pricing table");
		clickHiddenPageObject(specialPriceTable.addBtn(), 0);
		Pricing expectedPricing = setTestCodeValuesInPricing(false, "102X", testDao.getTestByTestAbbrev("102X").getName().trim(), effectiveDate, null, new Money(0), new Money(20.00), new Money(20.00), false, "last()");
		expectedPricingList.add(expectedPricing);

		logger.info("*** Action: - Add another new Test Code in the Pricing table");
		clickHiddenPageObject(specialPriceTable.addBtn(), 0);
		expectedPricing = setTestCodeValuesInPricing(false, "300", testDao.getTestByTestAbbrev("300").getName().trim(), effectiveDate, null, new Money(0), new Money(30.00), new Money(30.00), false, "last()");
		expectedPricingList.add(expectedPricing);

		logger.info("*** Action: - Select a Test Code and select Delete checkbox");
		clickHiddenPageObject(specialPriceTable.deleteCheckboxInPricingTbl("2"), 0);

		logger.info("*** Action: - Click Save And Clear button");
		clickHiddenPageObject(specialPriceTable.saveAndClearBtn(), 0);

		logger.info("*** Expected Results: - Verify that it's back to the load Special Price Table page");
		assertTrue(isOnLoadSpecialPriceTablePage());

		expectedPricingList.remove(0);
		logger.info("*** Expected Results: - Verify that the Pricing data are saved properly in DB");
		List<PrcTest> prcTestList = rpmDao.getPrcTestByPrcAbbrev(testDb, specialPriceTableId4);
		verifySpecialPriceTableTestCodePricingSaved(prcTestList, expectedPricingList);

		logger.info("*** Action: - Select All Display Option");
		selectDropDownByText(specialPriceTable.loadSpecialPriceTablePageDisplayOptionDdl(), "All");

		logger.info("*** Action: - Enter the newly created Special Price Table ID");
		setInputValue(specialPriceTable.loadSpecialPriceTablePageSpePrcTblIdInput(), specialPriceTableId4);

		logger.info("*** Expected Results: - Verify that the Special Price Table page is displayed");
		wait.until(ExpectedConditions.visibilityOf(specialPriceTable.appendBtn()));

		logger.info("*** Expected Results: - Verify that the data in Pricing are displayed properly in UI");
		actualPricingList.clear();
		for (int i=0; i<prcTestList.size(); i++) {
			actualPricingList.add(getTestCodeValuesInPricing(String.valueOf(i+2)));
		}
		assertEquals(actualPricingList, expectedPricingList);

		logger.info("*** Action: - Click Reset button");
		clickHiddenPageObject(specialPriceTable.resetBtn(), 0);
	}

	@Test(priority = 1, description = "Veify that there is a new button to changing Special Price Table ID")
	public void verifyThereIsANewButtonToChangingSpecialPriceTableID(Method method) throws Exception {
		logger.info("*** Implementation test case [" + method.getName() + "] ***");

		randomCharacter = new RandomCharacter();
		specialPriceTable = new SpecialPriceTable(driver, wait);
		specialPriceTableUtils = new SpecialPriceTableUtils(driver, config, wait);
		Prc prc = prcDao.getRandomPrcByFeeSchedule(SPECIAL_PRICE_TABLE);
		String specialPrcIdWithSpecialCharacters = randomCharacter.getRandomAlphaStringwithSpecialCharacters(6) + "@#";
		
		logger.info("*** Step 1 Expected Result: Verify that the Load Special Price Table page is displayed");
		assertTrue(isOnLoadSpecialPriceTablePage());
		
		logger.info("*** Step 2 Actions: Input a valid Special Price Table ID");
		String displayOption = specialPriceTable.loadSpecialPriceTablePageDisplayOptionTxt().getText();
		enterValues(specialPriceTable.loadSpecialPriceTablePageSpePrcTblIdInput(), prc.getPrcAbbrev());
		
		logger.info("*** Step 2 Expected Result: Verify the Special Price Table page is displayed correctly with information of Special Price Table ID");
		SpecialPriceTableInformation expectedSpecialPrcTblInformation = specialPriceTableUtils.getSpecialPriceTblInforInDB(prc, displayOption);
		SpecialPriceTableInformation actualSpecialPrcTblInformation = getValuesInSpecialPriceTableInformation();
		assertEquals(actualSpecialPrcTblInformation, expectedSpecialPrcTblInformation, "SpecialPriceTableInformation should be displayed correctly");
		
		logger.info("*** Step 2 Expected Result: 'Change Special Price Table ID' button is displayed");
		assertTrue(isElementPresent(specialPriceTable.changeSpecialPriceTblIDButton(), 15), "Change Special Price Table ID button should be displayed");
		
		logger.info("*** Step 3 Actions: Click on 'Change Special Price Table ID' button");
		clickHiddenPageObject(specialPriceTable.changeSpecialPriceTblIDButton(), 0);
		
		logger.info("*** Step 3 Expected Result: The 'Change Special Price Table ID' pop up is displayed");
		assertEquals(specialPriceTable.changeSpecialPriceTblIDPopupTitle().getText(), "Change Special Price Table ID", "Change Fee Schedule ID popup should be displayed");
		
		logger.info("*** Step 3 Expected Result: Current Fee Schedule ID field is read only");
		assertTrue(isElementEnabled(specialPriceTable.currentChangeSpecialPriceTblID(), 15, false), "Current Special Price Table Id field should be disabled");
		assertEquals(specialPriceTable.currentChangeSpecialPriceTblID().getAttribute("value"), actualSpecialPrcTblInformation.getSpecialPriceTableId(), "Current Special Price Table ID should be equals " + actualSpecialPrcTblInformation.getSpecialPriceTableId());
		
		logger.info("*** Step 3 Expected Result: New Special Price Table ID field is enabled");
		assertTrue(isElementEnabled(specialPriceTable.newChangeSpecialPriceTblID(), 15, true), "New Special Price Table ID field should be enabled");
		
		logger.info("*** Step 4 Actions: Leave New Special Price Table ID blank" + " | Click on 'OK' button");
		enterValues(specialPriceTable.newChangeSpecialPriceTblID(), "");
		clickHiddenPageObject(specialPriceTable.okButtonInChangeSpecialPriceTblIDPopup(), 0);
		
		logger.info("*** Step 4 Expected Result: Field Validation popup is displayed");
		assertEquals(specialPriceTable.validationMessageInChangeSpecialPriceTblIDPopup().getText().trim(), "New Special Price Table ID is required.", "Field validation should be displayed");
		
		logger.info("*** Step 5 Actions: Close Field Validation popup" + " | Input new Special Price Table ID includes special characters" + " | Click on 'OK' button");
		clickHiddenPageObject(specialPriceTable.closeValidationMessageInChangeSpecialPriceTblIDPopup(), 0);
		enterValues(specialPriceTable.newChangeSpecialPriceTblID(), specialPrcIdWithSpecialCharacters);
		clickHiddenPageObject(specialPriceTable.okButtonInChangeSpecialPriceTblIDPopup(), 0);
		
		logger.info("*** Step 5 Expected Result: Field Validation popup is displayed");
		assertEquals(specialPriceTable.validationMessageInChangeSpecialPriceTblIDPopup().getText().trim(), "Invalid: " + specialPrcIdWithSpecialCharacters + ". Only Number or Letter type values are allowed!", "Field validation should be displayed");
	}
	
	@Test(priority = 1, description = "Verify that users are able to update Special Price ID In Special Price Table screen")
	public void verifyUsersAreAbleToUpdateSpecialPriceID(Method method) throws Exception {
		logger.info("*** Implementation test case [" + method.getName() + "] ***");

		specialPriceTable = new SpecialPriceTable(driver, wait);
		specialPriceTableUtils = new SpecialPriceTableUtils(driver, config, wait);
		Prc prc = prcDao.getRandomPrcByFeeSchedule(SPECIAL_PRICE_TABLE);
		oldSpecialPriceId = prc.getPrcAbbrev();
		newSpecialPriceId = specialPriceTableUtils.getNewPrcAbbrvIsNotExistInSystem();
		
		logger.info("*** Step 1 Expected Result: Verify that the Load Special Price Table page is displayed");
		assertTrue(isOnLoadSpecialPriceTablePage());
		
		logger.info("*** Step 2 Actions: Input a valid Special Price Table ID");
		String displayOption = specialPriceTable.loadSpecialPriceTablePageDisplayOptionTxt().getText();
		enterValues(specialPriceTable.loadSpecialPriceTablePageSpePrcTblIdInput(), oldSpecialPriceId);
		
		logger.info("*** Step 2 Expected Result: Verify the Special Price Table page is displayed correctly with information of Special Price Table ID");
		SpecialPriceTableInformation expectedSpecialPrcTblInformation = specialPriceTableUtils.getSpecialPriceTblInforInDB(prc, displayOption);
		SpecialPriceTableInformation actualSpecialPrcTblInformation = getValuesInSpecialPriceTableInformation();
		assertEquals(actualSpecialPrcTblInformation, expectedSpecialPrcTblInformation, "SpecialPriceTableInformation should be displayed correctly");
		
		logger.info("*** Step 2 Expected Result: 'Change Special Price Table ID' button is displayed");
		assertTrue(isElementPresent(specialPriceTable.changeSpecialPriceTblIDButton(), 15), "Change Special Price Table ID button should be displayed");
		
		logger.info("*** Step 3 Actions: Click on 'Change Special Price Table ID' button");
		clickHiddenPageObject(specialPriceTable.changeSpecialPriceTblIDButton(), 0);
		
		logger.info("*** Step 3 Expected Result: The 'Change Special Price Table ID' pop up is displayed ");
		assertEquals(specialPriceTable.changeSpecialPriceTblIDPopupTitle().getText(), "Change Special Price Table ID", "Change Special Price Table ID popup should be displayed");
		
		logger.info("*** Step 3 Expected Result: Current Special Price Table ID field is read only");
		assertTrue(isElementEnabled(specialPriceTable.currentChangeSpecialPriceTblID(), 15, false), "Current Special Price Table Id field should be disabled");
		assertEquals(specialPriceTable.currentChangeSpecialPriceTblID().getAttribute("value"), actualSpecialPrcTblInformation.getSpecialPriceTableId(), "Current Special Price Table ID should be equals " + actualSpecialPrcTblInformation.getSpecialPriceTableId());
		
		logger.info("*** Step 3 Expected Result: New Special Price Table ID field is enabled");
		assertTrue(isElementEnabled(specialPriceTable.newChangeSpecialPriceTblID(), 15, true), "New Special Price Table ID field should be enabled");
		
		logger.info("*** Step 4 Actions: Input a new Special Price Table ID which isn't existed in the system" + " | Click on 'OK' button");
		enterValues(specialPriceTable.newChangeSpecialPriceTblID(), newSpecialPriceId);
		clickHiddenPageObject(specialPriceTable.okButtonInChangeSpecialPriceTblIDPopup(), 0);
		
		logger.info("*** Step 4 Expected Result: Special Price Table ID field is updated with the new value");
		assertEquals(specialPriceTable.specialPriceTablePageSpePrcTblIdTxt().getAttribute("value"), newSpecialPriceId, "Special Price Table ID should be updated with value " + newSpecialPriceId);
		
		logger.info("*** Step 5 Actions: Click on 'Save And Clear' button");
		clickHiddenPageObject(specialPriceTable.saveAndClearBtn(), 0);
		
		logger.info("*** Step 5 Expected Result: Verify that the Load Special Price Table page is displayed");
		assertTrue(isOnLoadSpecialPriceTablePage());
		
		logger.info("*** Step 5 Expected Result: Special Price Table ID value is updated in Database as new value");
		Prc prcUpdated = prcDao.getPrcByPrcId(prc.getPrcId());
		assertEquals(prcUpdated.getPrcAbbrev(), newSpecialPriceId, "Special Price Table ID should be updated in DB with value " + newSpecialPriceId);
		
		logger.info("*** Step 6 Actions: Input a updated Special Price Table ID");
		enterValues(specialPriceTable.loadSpecialPriceTablePageSpePrcTblIdInput(), newSpecialPriceId);
		
		logger.info("*** Step 6 Expected Result: Verify the Special Price Table page is displayed correctly with information of Special Price Table ID");
		SpecialPriceTableInformation expectedSpecialPrcTblInformationUpdated = specialPriceTableUtils.getSpecialPriceTblInforInDB(prcUpdated, displayOption);
		SpecialPriceTableInformation actualSpecialPrcTblInformationUpdated = getValuesInSpecialPriceTableInformation();
		assertEquals(actualSpecialPrcTblInformationUpdated, expectedSpecialPrcTblInformationUpdated, "SpecialPriceTableInformation should be displayed correctly with the new Special Price Table ID");
	}
	
	@Test(priority = 1, description = "Verify that there is an error when users change Special Price Table ID to existing one in the system")
	public void verifyThereIsAnErrorWhenUsersChangeSpecialPriceTableIDToExistingOne(Method method) throws Exception {
		logger.info("*** Implementation test case [" + method.getName() + "] ***");

		specialPriceTable = new SpecialPriceTable(driver, wait);
		specialPriceTableUtils = new SpecialPriceTableUtils(driver, config, wait);
		Prc prc = prcDao.getRandomPrcByFeeSchedule(SPECIAL_PRICE_TABLE);
		Prc existedPrc = specialPriceTableUtils.getDifferentPrcWithAnotherOne(prc, SPECIAL_PRICE_TABLE);
		
		logger.info("*** Step 1 Expected Result: Verify that the Load Special Price Table page is displayed");
		assertTrue(isOnLoadSpecialPriceTablePage());
		
		logger.info("*** Step 2 Actions: Input a valid Special Price Table ID");
		String displayOption = specialPriceTable.loadSpecialPriceTablePageDisplayOptionTxt().getText();
		enterValues(specialPriceTable.loadSpecialPriceTablePageSpePrcTblIdInput(), prc.getPrcAbbrev());
		
		logger.info("*** Step 2 Expected Result: Verify the Special Price Table page is displayed correctly with information of Special Price Table ID");
		SpecialPriceTableInformation expectedSpecialPrcTblInformation = specialPriceTableUtils.getSpecialPriceTblInforInDB(prc, displayOption);
		SpecialPriceTableInformation actualSpecialPrcTblInformation = getValuesInSpecialPriceTableInformation();
		assertEquals(actualSpecialPrcTblInformation, expectedSpecialPrcTblInformation, "SpecialPriceTableInformation should be displayed correctly");
		
		logger.info("*** Step 2 Expected Result: 'Change Special Price Table ID' button is displayed");
		assertTrue(isElementPresent(specialPriceTable.changeSpecialPriceTblIDButton(), 15), "Change Special Price Table ID button should be displayed");
		
		logger.info("*** Step 3 Actions: Click on 'Change Special Price Table ID' button");
		clickHiddenPageObject(specialPriceTable.changeSpecialPriceTblIDButton(), 0);
		
		logger.info("*** Step 3 Expected Result: The 'Change Special Price Table ID' pop up is displayed ");
		assertEquals(specialPriceTable.changeSpecialPriceTblIDPopupTitle().getText(), "Change Special Price Table ID", "Change Special Price Table ID popup should be displayed");
		
		logger.info("*** Step 3 Expected Result: Current Special Price Table ID field is read only");
		assertTrue(isElementEnabled(specialPriceTable.currentChangeSpecialPriceTblID(), 15, false), "Current Special Price Table Id field should be disabled");
		assertEquals(specialPriceTable.currentChangeSpecialPriceTblID().getAttribute("value"), actualSpecialPrcTblInformation.getSpecialPriceTableId(), "Current Special Price Table ID should be equals " + actualSpecialPrcTblInformation.getSpecialPriceTableId());
		
		logger.info("*** Step 3 Expected Result: New Special Price Table ID field is enabled");
		assertTrue(isElementEnabled(specialPriceTable.newChangeSpecialPriceTblID(), 15, true), "New Special Price Table ID field should be enabled");
		
		logger.info("*** Step 4 Actions: Input a new Special Price Table ID which is existed in the system" +  " | Click on 'OK' button");
		enterValues(specialPriceTable.newChangeSpecialPriceTblID(), existedPrc.getPrcAbbrev());
		clickHiddenPageObject(specialPriceTable.okButtonInChangeSpecialPriceTblIDPopup(), 0);
		
		logger.info("*** Step 4 Expected Result: Field Validation popup is displayed");
		assertEquals(specialPriceTable.validationMessageInChangeSpecialPriceTblIDPopup().getText().trim(), "Special Price Table ID " + existedPrc.getPrcAbbrev() + " is already in use. Please enter a Special Price Table ID that is not already in use.", "Field validation should be displayed");
	}

	@Test(priority = 1, description = "Verify Retro Pricing Engine is applied for Special Price Table")
	@Parameters({"ssoUsername", "ssoPassword", "xapEnv", "eType", "engConfigDB"})
	public void verifyRetroPricingEngineIsAppliedForSpecialPriceTable(String ssoUsername, String ssoPassword, String xapEnv, String eType, String engConfigDB) throws Exception {
		logger.info("*** Implementation test case [verifyRetroPricingEngineIsAppliedForSpecialPriceTable] ***");
		timeStamp = new TimeStamp();
		randomCharacter = new RandomCharacter(driver);
		specialPriceTable = new SpecialPriceTable(driver, wait);
		specialPriceTableUtils = new SpecialPriceTableUtils(driver, config, wait);
		xifinAdminUtils = new XifinAdminUtils(driver, config);
		clientPricingConfiguration = new ClientPricingConfiguration(driver, wait);
		clientPricingConfigurationUtils = new ClientPricingConfigurationUtils(driver, config, wait);
		
		Cln cln = clientDao.getClnByClnAbbrev(CLIENT_ABBREV);
		newSpecialPriceId = specialPriceTableUtils.getNewPrcAbbrvIsNotExistInSystem();
		
		logger.info("*** Expected Results: - Verify that the Load Special Price Table page is displayed");
		assertTrue(isOnLoadSpecialPriceTablePage());

		logger.info("*** Step 1 Actions: Create new Special Price Table - Select Individually Display Option");
		selectDropDownByText(specialPriceTable.loadSpecialPriceTablePageDisplayOptionDdl(), "All");

		logger.info("*** Step 1 Actions: Create new Special Price Table - Enter a new Special Price Table ID");
		enterValues(specialPriceTable.loadSpecialPriceTablePageSpePrcTblIdInput(), newSpecialPriceId);

		logger.info("*** Expected Results: - Verify that the Special Price Table page is displayed");
		assertTrue(isElementPresent(specialPriceTable.specialPriceTablePageTitle(),5),"        Special Price Table Page Title is displayed.");
		assertTrue(specialPriceTable.loadSpecialPriceTablePageTitleTxt().getText().equals("Special Price Table"),"        The title of Special Price Table page should be 'Special Price Table'.");

		logger.info("*** Step 2 Actions: Create new Special Price Table - Enter Special Price Table Information");
		setValuesInSpecialPriceTableInformation(newSpecialPriceId, "Client", "All");
		
		logger.info("*** Step 3 Actions: Create new Special Price Table - Click Save button");
		clickHiddenPageObject(specialPriceTable.saveBtn(), 0);
		
		logger.info("*** Step 4 Actions: Create new Special Price Table - Click Save And Clear button");
		clickHiddenPageObject(specialPriceTable.saveAndClearBtn(), 0);
		
		logger.info("*** Step 5 Actions: Navigate to Client Pricing Configuration Page");
		navigation.navigateToClientPricingConfigurationPage();
		
		logger.info("*** Step 5 Expected Results: Verify that the Pricing Configuration page is displayed");
		clientPricingConfigurationUtils.verifyClientPricingConfigurationPageIsDisplayed(this);
		
		logger.info("*** Step 6 Actions: Load a Client Abbrev in Pricing Configuration page");
		enterValues(clientPricingConfiguration.loadClientSectionClientIdInput(), cln.getClnAbbrev());
		
		logger.info("*** Step 6 Expected Results: - Verify that the details of Pricing Configuration page is correctly");
		waitUntilElementPresent(clientPricingConfiguration.footerSaveAndClearBtn(), 15);
		Header expectedHeader = clientPricingConfigurationUtils.getPricingConfigurationHeaderFromDB(cln.getClnAbbrev());
		Header actualHeader = clientPricingConfigurationUtils.getPricingConfigurationHeaderFromUI();
		assertEquals(actualHeader, expectedHeader, "       Client Pricing Configuration page Header section is correctly.");
		
		logger.info("*** Step 7 Actions: Update Special Price Table Id of Client Pricing in Pricing Detail Grid");
		oldSpecialPriceIdInClnDt = prcDao.getPrcByPrcAbbrev(clientPricingConfiguration.prcDetailSectionClnPrcGrpSpcPrcTblInput().getAttribute("value")).get(0).getPrcId();
		enterValues(clientPricingConfiguration.prcDetailSectionClnPrcGrpSpcPrcTblInput(), newSpecialPriceId);
		
		logger.info("*** Step 8 Actions: - Click Save And Clear button");
		clientPricingConfigurationUtils.saveAndClearAndReturnTotalEstimatedImpact();

		logger.info("*** Step 8 Expected Result: Verify that Special Price Table Id is updated to correspond Cln_Dt");
		prc = prcDao.getPrcByPrcAbbrev(newSpecialPriceId).get(0);
		clnDt = clientDao.getClnDtWithMaxEffDtByClnId(cln.getClnId());
		assertEquals(clnDt.getClnSpcPrcId(), prc.getPrcId(), "       Special Price Table Id should be updated correctly to correspond Cln_Dt");
		
		logger.info("*** Step 9 Actions: Navigate to Special Price Table Page");
		navigation.navigateToSpecialPriceTablePage();
		Map<String, Object> clnSpecialTestRetroImpactMap = specialPriceTableUtils.getClnSpcTestRetroImpactBySpcPrcIdAndClnId(prc.getPrcId(), cln.getClnId());
		test = (com.mbasys.mars.ejb.entity.test.Test) clnSpecialTestRetroImpactMap.get("Test");
		Date dos = (Date) clnSpecialTestRetroImpactMap.get("Dos");
		float originalRetroPrice = (Float) clnSpecialTestRetroImpactMap.get("OriginalRetroPrice");
				
		logger.info("*** Step 9 Expected Result: Verify that the Load Special Price Table page is displayed");
		assertTrue(isOnLoadSpecialPriceTablePage());
		
		logger.info("*** Step 10 Actions: Input a valid Special Price Table ID");
		selectDropDownByText(specialPriceTable.loadSpecialPriceTablePageDisplayOptionDdl(), "All");
		String displayOption = specialPriceTable.loadSpecialPriceTablePageDisplayOptionTxt().getText();
		enterValues(specialPriceTable.loadSpecialPriceTablePageSpePrcTblIdInput(), newSpecialPriceId);
		
		logger.info("*** Step 10 Expected Result: Verify the Special Price Table page is displayed correctly with information of Special Price Table ID");
		SpecialPriceTableInformation expectedSpecialPrcTblInformation = specialPriceTableUtils.getSpecialPriceTblInforInDB(prc, displayOption);
		SpecialPriceTableInformation actualSpecialPrcTblInformation = getValuesInSpecialPriceTableInformation();
		assertEquals(actualSpecialPrcTblInformation, expectedSpecialPrcTblInformation, "SpecialPriceTableInformation should be displayed correctly");
		
		logger.info("*** Step 11 Actions: Click on 'Add' button in Price Grid " + "| Input Test Code Abbrev to Test Code");
		clickHiddenPageObject(specialPriceTable.addBtn(), 0);
		enterValues(specialPriceTable.pricingTblTestCodeColInput(LAST_ROW), test.getTestAbbrev());
		
		logger.info("*** Step 11 Expected Result: Verify Test Name will be populated automatically by Test Code Id");
		assertEquals(specialPriceTable.pricingTblNameColTxt(LAST_ROW).getText(), test.getName(), "Test Name should be equals " + test.getName());
		
		logger.info("*** Step 12 Actions: Change Effective Date " + " | Enter the Expiration Date " + " | Enter the new $ price");
		float newPrice = specialPriceTableUtils.getNewRetroPrice(originalRetroPrice);
		enterValues(specialPriceTable.pricingTblEffDateColInput(LAST_ROW), timeStamp.convertDateToString(dos));
		enterValues(specialPriceTable.pricingTblExpDateColInput(LAST_ROW), timeStamp.getCurrentDate(DATE_FORMAT));
		enterValues(specialPriceTable.pricingTblNewPriceColInput(LAST_ROW), newPrice);
		
		logger.info("*** Step 13 Actions: Click on Save And Clear button");
		specialPriceTableUtils.cleanAllOfRetroBatchesNotCompletedByClnId(clnDt.getClnId());
		clickHiddenPageObject(specialPriceTable.saveAndClearBtn(), 0);
		
		logger.info("*** Step 13 Expected Result: Verify that Retro Price Impact Grid is displayed");
		waitUntilElementPresent(specialPriceTable.retroactivePriceImpactGridTitle(), 15);
		assertTrue(specialPriceTable.retroactivePriceImpactGridTitle().getText().contains("Retroactive Price Impact"), "Retroactive Price Impact grid should be displayed");
		
		logger.info("*** Step 13 Expected Result: Verify that Information of Retroactive Price Impact Grid is displayed correctly");
		List<ClientRetroPriceImpact> clientRetroPriceImpacts = prcDao.getClientRetroPriceImpactByTestIdNewPrcDosAndSpcPrcId(test.getTestId(), (double) newPrice, dos, prc.getPrcId());
		assertEquals(specialPriceTable.retroactivePriceImpactTblRecordByRowAndAttribute(FIRST_ROW, "clnAbbrev").getText(), clnSpecialTestRetroImpactMap.get("ClnAbbrev"), "Client Abbrev should be equals " + clnSpecialTestRetroImpactMap.get("ClnAbbrev"));
		assertEquals(specialPriceTable.retroactivePriceImpactTblRecordByRowAndAttribute(FIRST_ROW, "clnName").getText(), clnSpecialTestRetroImpactMap.get("ClnName"), "Client Name should be equals " + clnSpecialTestRetroImpactMap.get("ClnName"));
		specialPriceTableUtils.verifyRetroPricingDisplayedCorrectly(specialPriceTableUtils.getRetroactivePriceImpactFromUI(), specialPriceTableUtils.getRetroactivePriceImpactFromDB(clientRetroPriceImpacts));
		
		logger.info("*** Step 14 Actions: Click on Accept button");
		clickHiddenPageObject(specialPriceTable.saveAndClearBtn(), 0);
		
		logger.info("*** Step 14 Expected Result: Verify that the Load Special Price Table page is displayed");
		assertTrue(isOnLoadSpecialPriceTablePage());
		
		logger.info("*** Step 14 Expected Result: Verify that the PrcTest is stored in DB with given Prc and Test");
		prcTest = prcDao.getPrcTestByPrcIdTestIdAndEffDt(prc.getPrcId(), test.getTestId(), dos);
		assertNotNull(prcTest, "PrcTest should be stored in Database properly");
		assertEquals(prcTest.getPrice(), newPrice, "The price should be equals " + newPrice);
		
		logger.info("*** Step 14 Expected Result: Verify that q_retro and q_accn_retro has new entry follow by cln Id");
		List<QRetro> qRetros = errorProcessingDao.getQRetroByClnIdProcessedAndInDt(clnDt.getClnId(), UNPROCESSED, TODAYS_DT);
		List<QRetroAccn> qRetroAccns = specialPriceTableUtils.getQRetroAccnByRetroBatchId(qRetros);
		
		assertNotNull(qRetros, "Q_Retro table should be has new entry");
		assertFalse(specialPriceTableUtils.isQRetroProcessed(qRetros), "QRetros should be unprocessing");
		assertNotNull(qRetroAccns, "Q_Retro_Accn table should be has new entry");
		assertFalse(specialPriceTableUtils.isQRetroAccnProcessed(qRetroAccns), "QRetroAccns should be unprocessing");
		
		logger.info("*** Step 15 Actions: Checking SPDISCFLAG field has record in TEST_MSG table");
		specialPriceTableUtils.checkingSpcDiscFlagRecordInTestMsgTable();
		
		logger.info("*** Step 16 Actions: Run Retro Pricing Engine");
		xifinAdminUtils.runPFEngine(this, ssoUsername, ssoPassword, xapEnv, eType, engConfigDB, true);
		
		logger.info("*** Step 16 Expected Result: Verify that the batch in q_retro is processed properly");
		qRetros = errorProcessingDao.getQRetroByClnIdProcessedAndInDt(clnDt.getClnId(), PROCESSED, TODAYS_DT);
		assertTrue(specialPriceTableUtils.isQRetroProcessed(qRetros), "QRetros should be processed properly in Database");
		
		logger.info("*** Step 16 Expected Result: Verify that the Accessions in the batch in q_retro_accn are processed properly");
		qRetroAccns = specialPriceTableUtils.getQRetroAccnByRetroBatchId(qRetros);
		assertTrue(specialPriceTableUtils.isQRetroAccnProcessed(qRetroAccns), "QRetroAccns should be processed properly in Database");
	}
	
	//========================================================================================================================================================
	private boolean isOnLoadSpecialPriceTablePage() throws Exception {
		boolean isOnPage = false;

		assertTrue(isElementPresent(specialPriceTable.loadSpecialPriceTablePageTitleTxt(),5),"        User login successful. Load Special Price Table page is displayed.");
		isOnPage = specialPriceTable.loadSpecialPriceTablePageTitleTxt().getText().equals("Special Price Table");

		return isOnPage;
	}
	private SpecialPriceTableInformation setValuesInSpecialPriceTableInformation(String specialPriceTableId, String accountType, String displayOption) throws Exception {
		SpecialPriceTableInformation specialPriceTableInformation = new SpecialPriceTableInformation();

		//Special Price Table Id
		specialPriceTableInformation.setSpecialPriceTableId(specialPriceTableId);
		//Name
		String name = randomCharacter.getRandomAlphaString(10);
		setInputValue(specialPriceTable.specialPriceTablePageNameInput(), name);
		specialPriceTableInformation.setName(name);
		//Facility
		selectItemByVal(specialPriceTable.facilityDropdownInSpecialPriceTableInformation(), "1"); //1: ACME
		specialPriceTableInformation.setFacility("ACME");
		//Account Type
		if (accountType.equals("Client")){
			clickHiddenPageObject(specialPriceTable.specialPriceTablePageClientRad(), 0);
			specialPriceTableInformation.setAccountType("Client");
		}else if (accountType.equals("Non-Client")){
			clickHiddenPageObject(specialPriceTable.specialPriceTablePageNonClientRad(), 0);
			specialPriceTableInformation.setAccountType("Non-Client");
		}
		//Display Option
		specialPriceTableInformation.setDisplayOption(displayOption);

		return specialPriceTableInformation;
	}

	private Pricing setTestCodeValuesInPricing(boolean isPreLoaded, String testCode, String name, Date effectiveDate, Date expirationDate, Money retail, Money current, Money newPrice, boolean isDeleted, String row) throws Exception {
		Pricing pricing = new Pricing();
		FeeSchedule feeSchedule=new FeeSchedule(driver,wait);

		if (!isPreLoaded){
			setInputValue(specialPriceTable.pricingTblTestCodeColInput(row), testCode);

			specialPriceTable.pricingTblEffDateColInput(row).clear();
			setInputValue(specialPriceTable.pricingTblEffDateColInput(row), DATE_FORMAT_MMDDYYYY.format(effectiveDate));

			specialPriceTable.pricingTblExpDateColInput(row).clear();
			try {
				setInputValue(specialPriceTable.pricingTblExpDateColInput(row), DATE_FORMAT_MMDDYYYY.format(expirationDate));
			}catch(Exception e){
				//do nothing;
			}

			feeSchedule.setInputValue(specialPriceTable.pricingTblNewPriceColInput(row), String.valueOf(newPrice));

			if (isDeleted){
				specialPriceTable.pricingTblDeleteColChk(row).click();
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

		pricing.setTestCode(specialPriceTable.testCodeLnkInPricingTbl(index).getText().trim());
		pricing.setName(specialPriceTable.pricingTblNameColTxt(index).getText().trim());

		Date effectiveDate = new Date(DATE_FORMAT_MMDDYYYY.parse(specialPriceTable.pricingTblEffDateColTxt(index).getText().trim()).getTime());
		pricing.setEffectiveDate(effectiveDate);

		try {
			Date expirationDate = new Date(DATE_FORMAT_MMDDYYYY.parse(specialPriceTable.pricingTblExpDateColTxt(index).getText().trim()).getTime());
			pricing.setExpirationDate(expirationDate);
		}catch(Exception e){
			logger.info("       No Expiration Date.");
			pricing.setExpirationDate(null);
		}

		//pricing.setRetail(new Money(feeSchedule.retailTxtInPricingTbl(index).getText().trim()));
		pricing.setRetail(new Money(0));
		pricing.setCurrent(new Money(specialPriceTable.pricingTblCurrentPriceColTxt(index).getText().trim()));
		pricing.setNewPrice(new Money(specialPriceTable.pricingTblNewPriceColTxt(index).getText().trim()));
		pricing.setDeleted(specialPriceTable.pricingTblDeleteColChk(index).isSelected());

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

	private void cleanUpSpecialPriceTableTestData(List<String> specialPriceTableList) throws XifinDataAccessException {
		for (String specialPriceTable: specialPriceTableList) {
			logger.info("       ========== Deleting Special Price Table " + specialPriceTable + " ==========");
			rpmDao.deletePrcTestByPrcAbbrev(testDb, specialPriceTable);
			rpmDao.deletePrcProcByPrcAbbrev(testDb, specialPriceTable);
			rpmDao.deletePrcByPrcAbbrev(testDb, specialPriceTable);
		}
	}

	private void deleteTestFile(String filePath) throws Exception {
		File testFile = new File(filePath);

		if (isFileExists(testFile, 5)){
			FileUtils.forceDelete(testFile);
		}
	}

	private void verifySpecialPriceTableInformationSaved(List<Prc> prcList, SpecialPriceTableInformation expectedSpecialPriceTableInformation) throws XifinDataAccessException {
		Prc prc = prcList.get(0);

		//Special Price Table ID
		assertEquals(prc.getPrcAbbrev(), expectedSpecialPriceTableInformation.getSpecialPriceTableId());
		//Name
		assertEquals(prc.getDescr(), expectedSpecialPriceTableInformation.getName());
		//Account Type
		assertEquals(getAccountType(prc), expectedSpecialPriceTableInformation.getAccountType());
		//Facility
		String facAbbrevInDB = facilityDao.getFacByFacId(prc.getFacId()).getAbbrv();
		assertEquals(facAbbrevInDB, expectedSpecialPriceTableInformation.getFacility());
	}

	private void verifySpecialPriceTableTestCodePricingSaved(List<PrcTest> prcTestList, List<Pricing> expectedPricingList) throws XifinDataAccessException {
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
			assertTrue(false, "       Pricing data are saved in DB.");
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

	private SpecialPriceTableInformation getValuesInSpecialPriceTableInformation() {
		SpecialPriceTableInformation specialPriceTableInformation = new SpecialPriceTableInformation();

		//Special Price Table ID
		specialPriceTableInformation.setSpecialPriceTableId(specialPriceTable.specialPriceTablePageSpePrcTblIdTxt().getAttribute("value"));
		//Name
		specialPriceTableInformation.setName(specialPriceTable.specialPriceTablePageHeaderNameInput().getAttribute("value"));
		//Account Type
		specialPriceTableInformation.setAccountType(specialPriceTable.accountTypeTxt().getAttribute("value"));
		//Facility
		specialPriceTableInformation.setFacility(specialPriceTable.facilityChoosenTxt().getText());
		//Display Option
		specialPriceTableInformation.setDisplayOption(specialPriceTable.displayOptionTxt().getAttribute("value"));

		return specialPriceTableInformation;
	}

	private void verifySearchResultsDisplayed(List<String> pricingList, String specialPriceTableId) {
		//Price Table ID
		assertEquals(specialPriceTable.priceTableId().getText().trim(), specialPriceTableId);
		//ID
		WebElement id = specialPriceTable.clientPayorPricingTable().findElement(By.xpath("//tbody/tr/td[contains(text(), '" + pricingList.get(0) + "')]"));
		Assert.assertTrue(id.isDisplayed());
		//Name
		WebElement name = specialPriceTable.clientPayorPricingTable().findElement(By.xpath("//tbody/tr/td[contains(text(), '" + pricingList.get(1) + "')]"));
		Assert.assertTrue(name.isDisplayed());
		//Effective Date
		WebElement effectiveDate = specialPriceTable.clientPayorPricingTable().findElement(By.xpath("//tbody/tr/td[contains(text(), '" + pricingList.get(2) + "')]"));
		Assert.assertTrue(effectiveDate.isDisplayed());
	}
}
