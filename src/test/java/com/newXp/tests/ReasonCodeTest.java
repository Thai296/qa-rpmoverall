package com.newXp.tests;


import com.mbasys.mars.ejb.entity.errCd.ErrCd;
import com.mbasys.mars.ejb.entity.errCdOverride.ErrCdOverride;
import com.mbasys.mars.ejb.entity.errCdXref.ErrCdXref;
import com.mbasys.mars.persistance.ErrorCodeMap;
import com.overall.fileMaintenance.fileMaintenanceTables.Facility;
import com.overall.fileMaintenance.fileMaintenanceTables.ReasonCode;
import com.overall.menu.MenuNavigation;
import com.overall.search.ReasonCodeSearch;
import com.overall.search.ReasonCodeSearchResults;
import com.overall.search.ReasonCodeTableSearch;
import com.overall.search.ReasonCodeTableSearchResults;
import com.xifin.qa.dao.rpm.ErrorDao;
import com.xifin.xap.utils.XifinAdminUtils;
import com.xifin.qa.dao.exception.XifinDataAccessException;
import com.xifin.qa.dao.exception.XifinDataNotFoundException;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.utils.RandomCharacter;
import domain.fileMaintenance.reasoncode.CrossReference;
import domain.fileMaintenance.reasoncode.Detail;
import domain.fileMaintenance.reasoncode.Header;
import domain.fileMaintenance.reasoncode.Overrides;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.sql.Date;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;


public class ReasonCodeTest extends SeleniumBaseTest {
	private RandomCharacter randomCharacter;
	private ReasonCode reasonCode;
	private MenuNavigation navigation;
	private Facility facility;

	@BeforeSuite(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins", "reasonCodeId1", "date1", "reasonCodeId2", "reasonCodeId3", "reasonCodeId4", "reasonCodeId5", "reasonCodeId6", "reasonCodeId7","reasonCodeId8"})
	public void BeforeSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword,
							@Optional String disableBrowserPlugins, String reasonCodeId1, String date1, String reasonCodeId2, String reasonCodeId3, String reasonCodeId4, String reasonCodeId5, String reasonCodeId6, String reasonCodeId7, String reasonCodeId8)
	{
		try
		{
			logger.info("Running BeforeSuite");
			// Disable excess Selenium logging
			java.util.logging.Logger.getLogger("org.openqa.selenium.remote").setLevel(java.util.logging.Level.OFF);
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			cleanUpErrorCode(Arrays.asList(reasonCodeId1, reasonCodeId2, reasonCodeId3, reasonCodeId4, reasonCodeId5, reasonCodeId6, reasonCodeId7, reasonCodeId8), date1);
			new XifinAdminUtils(driver, config).clearDataCache();
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

	@BeforeMethod(alwaysRun = true)
	@Parameters({"ssoUsername", "ssoPassword"})
	public void beforeMethod(String ssoUsername, String ssoPassword, Method method) {
		try {
			logger = Logger.getLogger(getClass().getSimpleName() + "]" + String.format("%-13s", "[" + method.getName()));
			logIntoSso(ssoUsername, ssoPassword);
			navigation = new MenuNavigation(driver, config);
			navigation.navigateToReasonCodePage();
			logger.info("Running BeforeMethod");
		} catch (Exception e) {
			logger.error("Error running BeforeMethod", e);
		}
	}

	@AfterSuite(alwaysRun = true)
	@Parameters({"platform", "browser", "version", "port", "hub", "timeout", "orgAlias", "ssoUsername", "ssoPassword", "disableBrowserPlugins", "reasonCodeId1", "date1", "reasonCodeId2", "reasonCodeId3", "reasonCodeId4", "reasonCodeId5", "reasonCodeId6", "reasonCodeId7", "reasonCodeId8"})
	public void AfterSuite(String platform, String browser, String version, int port, String hub, int timeout, String orgAlias, String ssoUsername, String ssoPassword, @Optional String disableBrowserPlugins, String reasonCodeId1, String date1, String reasonCodeId2, String reasonCodeId3, String reasonCodeId4, String reasonCodeId5, String reasonCodeId6, String reasonCodeId7, String reasonCodeId8)
	{
		try
		{
			logger.info("Running AfterSuite");
			super.setUp(platform, browser, version, port, hub, timeout, orgAlias, disableBrowserPlugins);
			cleanUpErrorCode(Arrays.asList(reasonCodeId1, reasonCodeId2, reasonCodeId3, reasonCodeId4, reasonCodeId5, reasonCodeId6, reasonCodeId7, reasonCodeId8), date1);
			new XifinAdminUtils(driver, config).clearDataCache();
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


	@Test(priority = 1, description = "Update reason code in Claim Status error group and ACK error type and verify Run Audit")
	@Parameters({ "errorGroup", "csErrTypCd", "reasonCodeId"})
	public void testXPR_789(String errorGroup, String csErrTypCd, String reasonCodeId) throws Exception {
		logger.info("===== Testing - testXPR_789 =====");
		reasonCode = new ReasonCode(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		logger.info("*** Expected Results: - Verify that the Load Reason Code page is displayed");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId, "", csErrTypCd, wait, false);

		logger.info("*** Expected Results: - Verify that all fields are disabled besides Short Description input field");
		verifyFieldsDisplayed(errorGroup, "");

		ErrCd errCd = errorDao.getErrCdByErrCd(80000);
		String originalShortDescription = errCd.getShortDescr().trim();

		logger.info("*** Action: - Update Short Description");
		String newShortDescription = "AUTO" + randomCharacter.getRandomAlphaString(10);
		setInputValue(reasonCode.detailSectionShortDescrInput(), newShortDescription);

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(reasonCode.footerSaveAndClearBtn());

		logger.info("*** Action: - Click Reset button");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));
		clickOnElement(reasonCode.footerResetBtn());

		logger.info("*** Expected Results: - Verify that it's back to the Load Reason Code page");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Expected Results: - Verify that the Short Description is updated in DB");
		errCd = errorDao.getErrCdByErrCd(80000);
		String shortDescriptionInDB = errCd.getShortDescr().trim();
		assertEquals(shortDescriptionInDB, newShortDescription);

		logger.info("*** Action: - Reload the same Error Group and Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId, "", csErrTypCd, wait, false);

		logger.info("*** Expected Results: - Verify that the Short Description is updated in UI");
		assertTrue(reasonCode.detailSectionShortDescrInput().isEnabled(), "       Short Description input field should be enabled.");
		assertEquals(reasonCode.detailSectionShortDescrInput().getAttribute("value").trim(), newShortDescription);

		logger.info("*** Action: - Click Run Audit button");
		clickHiddenPageObject(reasonCode.headerRunAuditBtn(), 0);
		String parentWindow = switchToPopupWin();

		logger.info("*** Expected Results: - Verify that the audit data display properly in Audit Detail page");

		List<String> auditDetailList = Arrays.asList("U", "ERR_CD", "SHORT_DESCR", originalShortDescription.toUpperCase(), newShortDescription.toUpperCase());
		verifyAuditDetailDisplayed(auditDetailList);

		driver.close();
		switchToParentWin(parentWindow);

		logger.info("*** Action: - Click Reset button");
		clickOnElement(reasonCode.footerResetBtn());
	}

	@Test(priority = 1, description = "Load Denial error code via Searches and verify Reset button")
	@Parameters({ "errorGroup", "reasonCodeTableId", "reasonCodeId"})
	public void testXPR_790(String errorGroup, String reasonCodeTableId, String reasonCodeId) throws Exception {
		logger.info("===== Testing - testXPR_790 =====");
		reasonCode = new ReasonCode(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		ReasonCodeTableSearch reasonCodeTableSearch = new ReasonCodeTableSearch(driver);
		ReasonCodeTableSearchResults reasonCodeTableSearchResults = new ReasonCodeTableSearchResults(driver);
		ReasonCodeSearch reasonCodeSearch = new ReasonCodeSearch(driver);
		ReasonCodeSearchResults reasonCodeSearchResults = new ReasonCodeSearchResults(driver);

		logger.info("*** Expected Results: - Verify that the Load Reason Code page is displayed");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Select Denial Error Group");
		selectDropDownByText(reasonCode.errorGroupDropDown(), errorGroup);

		logger.info("*** Expected Results: - Verify that Reason Code Table ID input field is displayed");
		wait.until(ExpectedConditions.visibilityOf(reasonCode.reasonCodeTblIdInput()));

		logger.info("*** Action: - Select Reason Code Table Search icon button");
		clickOnElement(reasonCode.reasonCodeTblIdSearchIcon());

		String parentWindow = switchToPopupWin();

		logger.info("*** Expected Results: - Verify that Reason Code Table Search page is displayed");
		wait.until(ExpectedConditions.visibilityOf(reasonCodeTableSearch.reasonCodeTblIdInput()));

		logger.info("*** Action: - Enter value in Reason Code Table ID input field and click Search button");
		setInputValue(reasonCodeTableSearch.reasonCodeTblIdInput(), reasonCodeTableId);
		clickOnElement(reasonCodeTableSearch.searchBtn());

		logger.info("*** Expected Results: - Verify that Reason Code Table Search Result page is displayed");
		wait.until(ExpectedConditions.visibilityOf(reasonCodeTableSearchResults.reasonCodeSearchResultTbl()));

		logger.info("*** Action: - Click the hyperlink from the Search Results table");
		clickOnElement(reasonCodeTableSearchResults.reasonCodeTblIdColLnk("2"));

		switchToParentWin(parentWindow);

		logger.info("*** Expected Results: - Verify that the selected Reason Code Table ID is loaded in the Reason Code screen");
		assertEquals(reasonCode.reasonCodeTblIdInput().getAttribute("value").trim(), reasonCodeTableId, "       Reason Code Table ID " + reasonCodeTableId + " is displayed.");

		logger.info("*** Expected Results: - Verify that Reason Code ID input field is displayed");
		wait.until(ExpectedConditions.visibilityOf(reasonCode.reasonCodeIdInput()));

		logger.info("*** Action: - Select Reason Code ID Search icon button");
		clickOnElement(reasonCode.reasonCodeIdSearchIcon());

		parentWindow = switchToPopupWin();

		logger.info("*** Expected Results: - Verify that Reason Code Search page is displayed");
		wait.until(ExpectedConditions.visibilityOf(reasonCodeSearch.reasonCodeInput()));

		logger.info("*** Expected Results: - Verify that Error Group and Reason Code Table are pre-populated");
		assertEquals(reasonCodeSearch.errorGroupDropdownText().getText().trim(), errorGroup, "       Error Group " + errorGroup + " should be populated.");
		assertEquals(reasonCodeSearch.reasonCodeTableInput().getAttribute("value").trim(), reasonCodeTableId, "       Reason Code Table " + reasonCodeTableId + " should be populated.");

		logger.info("*** Action: - Enter value in Reason Code ID input field and click Search button");
		setInputValue(reasonCodeSearch.reasonCodeInput(), reasonCodeId);
		clickOnElement(reasonCodeSearch.searchBtn());

		logger.info("*** Expected Results: - Verify that Reason Code Search Results page is displayed");
		wait.until(ExpectedConditions.visibilityOf(reasonCodeSearchResults.reasonCodeSearchResultTbl()));

		logger.info("*** Action: - Click the hyperlink from the Search Results table");
		clickOnElement(reasonCodeSearchResults.reasonCodeColLnk("2"));

		switchToParentWin(parentWindow);

		logger.info("*** Expected Results: - Verify that the selected Reason Code ID is loaded in the Reason Code screen");
		assertEquals(reasonCode.headerReasonCodeIdLbl().getAttribute("value").trim(), reasonCodeId, "       Reason Code ID " + reasonCodeId + " is displayed.");

		logger.info("*** Expected Results: - Verify that fields are enabled for Denials Error Code");
		verifyFieldsDisplayed(errorGroup, "");

		logger.info("*** Expected Results: - Verify that the reason code are displayed");
		ErrCd errCd = errorDao.getErrCdByErrCd(57187);
		String shortDescription = errCd.getShortDescr().trim();

		assertEquals(reasonCode.detailSectionShortDescrInput().getAttribute("value").trim(), shortDescription);

		logger.info("*** Action: - Click Reset button");
		clickOnElement(reasonCode.footerResetBtn());

		logger.info("*** Expected Results: - Verify that the Reason Code ID input field is cleared");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));
		assertTrue(reasonCode.reasonCodeIdInput().getAttribute("value").trim().isEmpty(), "       Reason Code ID input field is cleared.");

		logger.info("*** Expected Results: - Verify that the Reason Code Table ID still displayed");
		assertFalse(reasonCode.reasonCodeTblIdInput().getAttribute("value").trim().isEmpty(), "       Reason Code Table ID is displayed.");

		logger.info("*** Action: - Click Reset button again");
		clickOnElement(reasonCode.footerResetBtn());

		logger.info("*** Expected Results: - Verify that it's back to the load page");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("Reset")));
		isOnLoadReasonCodePage();
		assertTrue(reasonCode.errorGroupDropdownTxt().getText().trim().isEmpty(), "       Reason Group dropdown is cleared.");
	}

	@Test(priority = 1, description = "Verify all Helps")
	@Parameters({ "errorGroup", "csErrTypCd", "reasonCodeId"})
	public void testXPR_791(String errorGroup, String csErrTypCd, String reasonCodeId) throws Exception {
		logger.info("===== Testing - testXPR_791 =====");
		reasonCode = new ReasonCode(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		logger.info("*** Expected Results: - Verify that the Load Reason Code page is displayed");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Click Help icon in Reason Code header section");
		clickOnElement(reasonCode.headerHelpIcon());

		logger.info("*** Expected Results: - Verify that Reason Code Header Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/codes_menu/p_reason_code_header.htm", "Reason Code Header"));

		logger.info("*** Action: - Click Help icon in Reason Code section");
		clickOnElement(reasonCode.reasonCodeSectionLoadPageHelpIcon());

		logger.info("*** Expected Results: - Verify that Load Reason Code Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/codes_menu/p_reason_code_enter_reason_code.htm", "Reason Code Selection"));

		logger.info("*** Action: - Select Claim Status Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId, "", csErrTypCd, wait, false);

		logger.info("*** Expected Results: - Verify that all fields are disabled besides Short Description input field");
		verifyFieldsDisplayed(errorGroup, "");

		logger.info("*** Action: - Click Help icon in Detail section");
		clickOnElement(reasonCode.detailSectionHelpIcon());

		logger.info("*** Expected Results: - Verify that Reason Code Detail Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/codes_menu/p_reason_code_detail.htm", "Detail"));

		logger.info("*** Action: - Click Help icon in Overrides section");
		clickHiddenPageObject(reasonCode.overridesSectionHelpIcon(), 0);

		logger.info("*** Expected Results: - Verify that Reason Code Overrides Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/codes_menu/p_reason_code_overrides.htm", "Overrides"));

		logger.info("*** Action: - Click Help icon in Cross Reference section");
		clickHiddenPageObject(reasonCode.crossReferenceTblHelpIcon(), 0);

		logger.info("*** Expected Results: - Verify that Reason Code Cross Reference Help file is displayed properly");
		assertTrue(verifyHelpPageDisplayed("help/file_maintenance_tab/codes_menu/p_reason_code_cross_reference.htm", "Reason Code Cross-Reference"));

		logger.info("*** Action: - Click Reset button");
		clickOnElement(reasonCode.footerResetBtn());
		clickOnElement(reasonCode.footerResetBtn());
	}

	@Test(priority = 1, description = "Add new Effective date for error code in Claim Status error group and DEN error type")
	@Parameters({ "errorGroup", "reasonCodeId1", "csErrTypCd", "date1"})
	public void testXPR_1020(String errorGroup, String reasonCodeId1, String csErrTypCd, String date1) throws Exception {
		logger.info("===== Testing - testXPR_1020 =====");
		reasonCode = new ReasonCode(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date effectiveDate = new java.sql.Date(df.parse(date1).getTime());

		logger.info("*** Expected Results: - Verify that the Load Reason Code page is displayed");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId1, "", csErrTypCd, wait, false);

		logger.info("*** Expected Results: - Verify that fields are enabled/disabled properly");
		verifyFieldsDisplayed(errorGroup, " DEN");

		logger.info("*** Action: - Click Create New Effective Date icon button");
		clickHiddenPageObject(reasonCode.headerNewEffectiveDateBtn(), 0);

		logger.info("*** Action: - Enter values in Header section");
		Header expectedHeader = setValuesInReasonCodeHeader(errorGroup, reasonCodeId1, effectiveDate);

		logger.info("*** Action: - Enter values in Detail section");
		Detail expectedDetail = setValuesInReasonCodeDetail(errorGroup, "DEN");

		logger.info("*** Action: - Add a new record in Overrides section");
		scrollToSection("Overrides");
		assertTrue(isElementPresent(reasonCode.overridesSectionOverridesTblAddBtn(), 5), "        Add new row button should show in Overrides table.");
		clickOnElement(reasonCode.overridesSectionOverridesTblAddBtn());
		Overrides expectedOverrides = setValueInReasonCodeOverrides();
		clickOnElement(reasonCode.overridesSectionAddRecordPopupOkBtn());

		logger.info("*** Action: - Add new record in Cross Reference section");
		scrollToElement(reasonCode.crossReferenceTblAddBtn());
		assertTrue(isElementPresent(reasonCode.crossReferenceTblAddBtn(), 5), "        Add new row button should show in Reason Code Cross Reference table.");
		clickOnElement(reasonCode.crossReferenceTblAddBtn());
		CrossReference expectedCrossReference = setValuesInReasonCodeCrossReference(effectiveDate);

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(reasonCode.footerSaveAndClearBtn());

		logger.info("*** Action: - Click Reset button");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));
		clickOnElement(reasonCode.footerResetBtn());

		logger.info("*** Expected Results: - Verify that it's back to the Load Reason Code page");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Verify the values in Header section are saved properly in DB");
		ErrCd errCd = errorDao.getErrCdByAbbrevEffDt(reasonCodeId1, effectiveDate);
		verifyHeaderDataSaved(expectedHeader, errCd, errorGroup, effectiveDate);

		logger.info("*** Action: - Verify the values in Detail section are saved properly in DB");
		verifyDetailDataSaved(expectedDetail, errCd, errorGroup);

		logger.info("*** Action: - Verify the values in Overrides section are saved properly in DB");
		ErrCdOverride errCdOverride = errorDao.getErrCdOverrideByAbbrevEffDtLtrTxt(reasonCodeId1, effectiveDate, expectedOverrides.getCorrespondenceText());
		verifyOverridesDataSaved(expectedOverrides, errCdOverride);

		logger.info("*** Action: - Verify the values in Cross Reference section are saved properly in DB");
		ErrCdXref errCdXref = errorDao.getErrCdXrefByErrCdAbbrevErrCdEffDtXrefDescr(reasonCodeId1, effectiveDate, expectedCrossReference.getCrossReferenceDescription().split("-")[1]);
		verifyCrossReferenceDataSaved(expectedCrossReference, errCdXref);

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId1, "", csErrTypCd, wait, false);

		logger.info("*** Action: - Verify the values in Header section are displayed properly in UI");
		Header actualHeader = getValuesInReasonCodeHeader(errorGroup);
		assertEquals(actualHeader, expectedHeader);

		logger.info("*** Action: - Verify the values in Detail section are displayed properly in UI");
		Detail actualDetail = getValuesInReasonCodeDetail(errorGroup);
		assertEquals(actualDetail, expectedDetail);

		logger.info("*** Action: - Verify the values in Overrides section are displayed properly in UI");
		List<Overrides> actualOverridesList = getValuesInReasonCodeOverrides(1);
		for (Overrides overrides:actualOverridesList){
			assertEquals(overrides, expectedOverrides);
		}

		logger.info("*** Action: - Verify the values in Cross Reference section are displayed properly in UI");
		List<CrossReference> actualCrossReferenceList = getValuesInReasonCodeCrossReference(1);
		for(CrossReference crossReference:actualCrossReferenceList){
			assertEquals(crossReference, expectedCrossReference);
		}

		logger.info("*** Action: - Click Reset button");
		clickOnElement(reasonCode.footerResetBtn());
	}

	@Test(priority = 1, description = "Update the error code in Denial error group")
	@Parameters({ "errorGroup", "reasonCodeId2", "date1", "reasonCodeTableId"})
	public void testXPR_1024(String errorGroup, String reasonCodeId2, String date1, String reasonCodeTableId) throws Exception {
		logger.info("===== Testing - testXPR_1024 =====");
		reasonCode = new ReasonCode(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date effectiveDate = new java.sql.Date(df.parse(date1).getTime());

		logger.info("*** Expected Results: - Verify that the Load Reason Code page is displayed");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId2, reasonCodeTableId, "", wait, false);

		logger.info("*** Expected Results: - Verify that fields are enabled/disabled properly");
		verifyFieldsDisplayed(errorGroup, "");

		logger.info("*** Action: - Enter values in Detail section");
		Detail expectedDetail = setValuesInReasonCodeDetail(errorGroup, "");

		logger.info("*** Action: - Add a new record in Overrides section");
		scrollToSection("Overrides");
		assertTrue(isElementPresent(reasonCode.overridesSectionOverridesTblAddBtn(), 5), "        Add new row button should show in Overrides table.");
		clickOnElement(reasonCode.overridesSectionOverridesTblAddBtn());
		Overrides expectedOverrides = setValueInReasonCodeOverrides();
		clickOnElement(reasonCode.overridesSectionAddRecordPopupOkBtn());

		logger.info("*** Action: - Add new record in Cross Reference section");
		scrollToElement(reasonCode.crossReferenceTblAddBtn());
		assertTrue(isElementPresent(reasonCode.crossReferenceTblAddBtn(), 5), "        Add new row button should show in Reason Code Cross Reference table.");
		clickOnElement(reasonCode.crossReferenceTblAddBtn());
		CrossReference expectedCrossReference = setValuesInReasonCodeCrossReference(effectiveDate);

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(reasonCode.footerSaveAndClearBtn());

		logger.info("*** Action: - Click Reset button");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));
		clickOnElement(reasonCode.footerResetBtn());

		logger.info("*** Expected Results: - Verify that it's back to the Load Reason Code page");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Verify the values in Detail section are saved properly in DB");
		ErrCd errCd = errorDao.getErrCdByAbbrevEffDt(reasonCodeId2, effectiveDate);
		verifyDetailDataSaved(expectedDetail, errCd, errorGroup);

		logger.info("*** Action: - Verify the values in Overrides section are saved properly in DB");
		ErrCdOverride errCdOverride = errorDao.getErrCdOverrideByAbbrevEffDtLtrTxt(reasonCodeId2, effectiveDate, expectedOverrides.getCorrespondenceText());
		verifyOverridesDataSaved(expectedOverrides, errCdOverride);

		logger.info("*** Action: - Verify the values in Cross Reference section are saved properly in DB");
		ErrCdXref errCdXref = errorDao.getErrCdXrefByErrCdAbbrevErrCdEffDtXrefDescr(reasonCodeId2, effectiveDate, expectedCrossReference.getCrossReferenceDescription().split("-")[1]);
		verifyCrossReferenceDataSaved(expectedCrossReference, errCdXref);

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId2, reasonCodeTableId, "", wait, false);

		logger.info("*** Action: - Verify the values in Detail section are displayed properly in UI");
		Detail actualDetail = getValuesInReasonCodeDetail(errorGroup);
		assertEquals(actualDetail, expectedDetail);

		logger.info("*** Action: - Verify the values in Overrides section are displayed properly in UI");
		List<Overrides> actualOverridesList = getValuesInReasonCodeOverrides(1);
		for (Overrides overrides:actualOverridesList){
			assertEquals(overrides, expectedOverrides);
		}

		logger.info("*** Action: - Verify the values in Cross Reference section are displayed properly in UI");
		List<CrossReference> actualCrossReferenceList = getValuesInReasonCodeCrossReference(1);
		for(CrossReference crossReference:actualCrossReferenceList){
			assertEquals(crossReference, expectedCrossReference);
		}

		logger.info("*** Action: - Click Reset button");
		clickOnElement(reasonCode.footerResetBtn());
	}

	@Test(priority = 1, description = "Add new error code in Over-ride error group")
	@Parameters({ "errorGroup", "reasonCodeId3", "date1"})
	public void testXPR_1026(String errorGroup, String reasonCodeId3, String date1) throws Exception {
		logger.info("===== Testing - testXPR_1026 =====");
		reasonCode = new ReasonCode(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date effectiveDate = new java.sql.Date(df.parse(date1).getTime());

		logger.info("*** Expected Results: - Verify that the Load Reason Code page is displayed");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId3, "", "", wait, false);

		logger.info("*** Expected Results: - Verify that the fields are enabled/disabled properly for Over-ride Reason Code");
		verifyFieldsDisplayed(errorGroup, "");

		logger.info("*** Action: - Enter values in Header section");
		Header expectedHeader = setValuesInReasonCodeHeader(errorGroup, reasonCodeId3, effectiveDate);

		logger.info("*** Action: - Enter values in Detail section");
		Detail expectedDetail = setValuesInReasonCodeDetail(errorGroup, "");

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(reasonCode.footerSaveAndClearBtn());

		logger.info("*** Action: - Click Reset button");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));
		clickOnElement(reasonCode.footerResetBtn());

		logger.info("*** Expected Results: - Verify that it's back to the Load Reason Code page");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Verify the values in Header section are saved properly in DB");
		ErrCd errCd = errorDao.getErrCdByAbbrevEffDt(reasonCodeId3, effectiveDate);
		verifyHeaderDataSaved(expectedHeader, errCd, errorGroup, effectiveDate);

		logger.info("*** Action: - Verify the values in Detail section are saved properly in DB");
		verifyDetailDataSaved(expectedDetail, errCd, errorGroup);

		logger.info("*** Action: - Reload the same Error Group and Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId3, "", "", wait, false);

		logger.info("*** Action: - Verify the values in Header section are displayed properly in UI");
		Header actualHeader = getValuesInReasonCodeHeader(errorGroup);
		assertEquals(actualHeader, expectedHeader);

		logger.info("*** Action: - Verify the values in Detail section are displayed properly in UI");
		Detail actualDetail = getValuesInReasonCodeDetail(errorGroup);
		assertEquals(actualDetail, expectedDetail);

		logger.info("*** Action: - Click Reset button");
		clickOnElement(reasonCode.footerResetBtn());
	}

	@Test(priority = 1, description = "Delete record in Overrides and Cross Reference for Unbillable Error Code")
	@Parameters({ "errorGroup", "reasonCodeId4", "date1"})
	public void testXPR_1029(String errorGroup, String reasonCodeId4, String date1) throws Exception {
		logger.info("===== Testing - testXPR_1029 =====");
		reasonCode = new ReasonCode(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date effectiveDate = new java.sql.Date(df.parse(date1).getTime());

		logger.info("*** Expected Results: - Verify that the Load Reason Code page is displayed");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId4, "", "", wait, false);

		logger.info("*** Expected Results: - Verify that fields are enabled/disabled properly");
		verifyFieldsDisplayed(errorGroup, "");

		logger.info("*** Action: - Add a new record in Overrides section");
		scrollToSection("Overrides");
		assertTrue(isElementPresent(reasonCode.overridesSectionOverridesTblAddBtn(), 5), "        Add new row button should show in Overrides table.");
		clickOnElement(reasonCode.overridesSectionOverridesTblAddBtn());
		Overrides expectedOverrides = setValueInReasonCodeOverrides();
		clickOnElement(reasonCode.overridesSectionAddRecordPopupOkBtn());

		logger.info("*** Action: - Add new record in Cross Reference section");
		scrollToElement(reasonCode.crossReferenceTblAddBtn());
		assertTrue(isElementPresent(reasonCode.crossReferenceTblAddBtn(), 5), "        Add new row button should show in Reason Code Cross Reference table.");
		clickOnElement(reasonCode.crossReferenceTblAddBtn());
		CrossReference expectedCrossReference = setValuesInReasonCodeCrossReference(effectiveDate);

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(reasonCode.footerSaveAndClearBtn());

		logger.info("*** Action: - Click Reset button");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));
		clickOnElement(reasonCode.footerResetBtn());

		logger.info("*** Expected Results: - Verify that it's back to the Load Reason Code page");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Verify the values in Overrides section are saved properly in DB");
		ErrCdOverride errCdOverride = errorDao.getErrCdOverrideByAbbrevEffDtLtrTxt(reasonCodeId4, effectiveDate, expectedOverrides.getCorrespondenceText());
		verifyOverridesDataSaved(expectedOverrides, errCdOverride);

		logger.info("*** Action: - Verify the values in Cross Reference section are saved properly in DB");
		ErrCdXref errCdXref = errorDao.getErrCdXrefByErrCdAbbrevErrCdEffDtXrefDescr(reasonCodeId4, effectiveDate, expectedCrossReference.getCrossReferenceDescription().split("-")[1]);
		verifyCrossReferenceDataSaved(expectedCrossReference, errCdXref);

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId4, "", "", wait, false);

		scrollToSection("Overrides");

		logger.info("*** Action: - Verify the values in Overrides section are displayed properly in UI");
		List<Overrides> actualOverridesList = getValuesInReasonCodeOverrides(1);
		for (Overrides overrides:actualOverridesList){
			assertEquals(overrides, expectedOverrides);
		}

		logger.info("*** Action: - Verify the values in Cross Reference section are displayed properly in UI");
		List<CrossReference> actualCrossReferenceList = getValuesInReasonCodeCrossReference(1);
		for(CrossReference crossReference:actualCrossReferenceList){
			assertEquals(crossReference, expectedCrossReference);
		}

		logger.info("*** Action: - Select a record in Overrides table and check Delete checkbox");
		clickOnElement(reasonCode.overridesSectionOverridesTblDeleteChkbox(2)); //1st record

		logger.info("*** Action: - Select a record in Cross Reference table and check Delete checkbox");
		clickHiddenPageObject(reasonCode.deleteChkboxInCrossReferenceTable(2),0); //1st record

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(reasonCode.footerSaveAndClearBtn());

		logger.info("*** Action: - Click Reset button");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));
		clickOnElement(reasonCode.footerResetBtn());

		logger.info("*** Expected Results: - Verify that it's back to the Load Reason Code page");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Verify the record is deleted from ERR_CD_OVERRIDE properly in DB");
		assertNull(errorDao.getErrCdOverrideByAbbrevEffDtLtrTxt(reasonCodeId4, effectiveDate, expectedOverrides.getCorrespondenceText()));

		logger.info("*** Action: - Verify the record is deleted from ERR_CD_XREF properly in DB");
		assertNull(errorDao.getErrCdXrefByErrCdAbbrevErrCdEffDtXrefDescr(reasonCodeId4, effectiveDate, expectedCrossReference.getCrossReferenceDescription()));

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId4, "", "", wait, false);

		scrollToSection("Overrides");

		logger.info("*** Action: - Verify the deleted record does not show in Overrides table in UI");
		actualOverridesList = getValuesInReasonCodeOverrides(1); //1: one record;
		assertTrue(actualOverridesList.isEmpty());

		logger.info("*** Action: - Verify the deleted record does not show in Cross Reference table in UI");
		actualCrossReferenceList = getValuesInReasonCodeCrossReference(1); //1: one record;
		assertTrue(actualCrossReferenceList.isEmpty());

		logger.info("*** Action: - Click Reset button");
		clickOnElement(reasonCode.footerResetBtn());
	}

	@Test(priority = 1, description = "Delete Reason Code for Unpriceable error code")
	@Parameters({"errorGroup", "reasonCodeId5", "date1"})
	public void testXPR_1030(String errorGroup, String reasonCodeId5, String date1) throws Exception {
		logger.info("===== Testing - testXPR_1030 =====");
		reasonCode = new ReasonCode(driver, wait);
		randomCharacter = new RandomCharacter(driver);
		facility=new Facility(driver,wait);

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date effectiveDate = new java.sql.Date(df.parse(date1).getTime());

		logger.info("*** Expected Results: - Verify that the Load Reason Code page is displayed");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId5, "", "", wait, false);

		logger.info("*** Expected Results: - Verify that fields are enabled/disabled properly");
		verifyFieldsDisplayed(errorGroup, "");

		logger.info("*** Action: - Click Delete Reason Code checkbox");
		clickOnElement(reasonCode.headerDeleteReasonCodeChkBox());

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(reasonCode.footerSaveAndClearBtn());

		logger.info("*** Action: - Click Reset button");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));
		clickOnElement(reasonCode.footerResetBtn());

		logger.info("*** Expected Results: - Verify the Reason Code is marked as B_DELETED = 1 in DB");
		ErrCd errCd =errorDao.getErrCdByAbbrevEffDt(reasonCodeId5, effectiveDate);
		assertTrue(errCd.getIsDeleted());

		logger.info("*** Expected Results: - Verify that it's back to the Load Reason Code page");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Select Error Group and enter the deleted Reason Code ID");
		wait.until(ExpectedConditions.elementToBeClickable(reasonCode.errorGroupDropDown()));
		facility.selectDropDownByText(reasonCode.errorGroupDropDown(), errorGroup);
		wait.until(ExpectedConditions.visibilityOf(reasonCode.reasonCodeIdInput()));
		setInputValue(reasonCode.reasonCodeIdInput(), reasonCodeId5);

		logger.info("*** Expected Results: - Verify that '1333 - Can't Create New Error Code.' error shows");
		assertTrue(reasonCode.reasonCoderErrorMessageInLoadPage().getText().contains("Can't Create New Error Code"));

		logger.info("*** Action: - Click Reset button");
		clickOnElement(reasonCode.footerResetBtn());
	}
	@Test(priority = 1, description = "Add record in the overrides section for Unbillable Error code")
	@Parameters({"errorGroup", "reasonCodeId6", "date1"})
	public void testXPR_2063(String errorGroup, String reasonCodeId6, String date1) throws Exception {
		logger.info("===== Testing - testXPR_2063 =====");
		reasonCode = new ReasonCode(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date effectiveDate = new java.sql.Date(df.parse(date1).getTime());

		logger.info("*** Expected Results: - Verify that the Load Reason Code page is displayed");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId6, "", "", wait, false);

		logger.info("*** Expected Results: - Verify that fields are enabled/disabled properly");
		verifyFieldsDisplayed(errorGroup, "");

		logger.info("*** Action: - Add a new record in Overrides section");
		scrollToSection("Overrides");
		assertTrue(isElementPresent(reasonCode.overridesSectionOverridesTblAddBtn(), 5), "        Add new row button should show in Overrides table.");
		clickOnElement(reasonCode.overridesSectionOverridesTblAddBtn());
		Overrides expectedOverrides = setValueInReasonCodeOverridesWithSpecialCharacters();
		clickOnElement(reasonCode.overridesSectionAddRecordPopupOkBtn());

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(reasonCode.footerSaveAndClearBtn());

		logger.info("*** Action: - Click Reset button");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));
		clickOnElement(reasonCode.footerResetBtn());

		logger.info("*** Expected Results: - Verify that it's back to the Load Reason Code page");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Verify the values in Overrides section are saved properly in DB");
		ErrCdOverride errCdOverride = errorDao.getErrCdOverrideByAbbrevEffDtLtrTxt(reasonCodeId6, effectiveDate, expectedOverrides.getCorrespondenceText());
		verifyOverridesDataSaved(expectedOverrides, errCdOverride);
		verifyOverridesDataSavedWithSpecial(expectedOverrides,errCdOverride);

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId6, "", "", wait, false);

		scrollToSection("Overrides");

		logger.info("*** Action: - Verify the values in Overrides section are displayed properly in UI");
		assertTrue(isElementPresent(reasonCode.overridesTableInOverridesSection(),5));
		reasonCode.overrideTableEditFirstRow();

		//If the correspondence text contains < sign then the lists won't match due to an existing issue.
		List<Overrides> actualOverridesList = getValuesInReasonCodeOverridesForSpecialCharacters(1);
		for (Overrides overrides:actualOverridesList){
			assertEquals(overrides, expectedOverrides);
		}
		clickOnElement(reasonCode.overridesSectionAddRecordPopupOkBtn());

		logger.info("*** Action: - Select a record in Overrides table and check Delete checkbox");
		clickOnElement(reasonCode.overridesSectionOverridesTblDeleteChkbox(2)); //1st record

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(reasonCode.footerSaveAndClearBtn());

		logger.info("*** Action: - Click Reset button");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));
		clickOnElement(reasonCode.footerResetBtn());

		logger.info("*** Expected Results: - Verify that it's back to the Load Reason Code page");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Verify the record is deleted from ERR_CD_OVERRIDE properly in DB");
		assertNull(errorDao.getErrCdOverrideByAbbrevEffDtLtrTxt(reasonCodeId6, effectiveDate, expectedOverrides.getCorrespondenceText()));

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId6, "", "", wait, false);

		scrollToSection("Overrides");

		logger.info("*** Action: - Verify the deleted record does not show in Overrides table in UI");
		actualOverridesList = getValuesInReasonCodeOverrides(1); //1: one record;
		assertTrue(actualOverridesList.isEmpty());

		logger.info("*** Action: - Click Reset button");
		clickOnElement(reasonCode.footerResetBtn());
	}

	@Test(priority = 1, description = "add record in the overrides section for denial Error code")
	@Parameters({"errorGroup", "reasonCodeId7", "date1", "reasonCodeTableId"})
	public void testXPR_2064(String errorGroup, String reasonCodeId7, String date1, String reasonCodeTableId) throws Exception {
		logger.info("===== Testing - testXPR_2064 =====");
		reasonCode = new ReasonCode(driver, wait);
		randomCharacter = new RandomCharacter(driver);
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date effectiveDate = new java.sql.Date(df.parse(date1).getTime());

		logger.info("*** Expected Results: - Verify that the Load Reason Code page is displayed");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId7, reasonCodeTableId, "", wait, false);

		logger.info("*** Expected Results: - Verify that fields are enabled/disabled properly");
		verifyFieldsDisplayed(errorGroup, "");

		logger.info("*** Action: - Enter values in Detail section");
		Detail expectedDetail = setValuesInReasonCodeDetail(errorGroup, "");

		logger.info("*** Action: - Add a new record in Overrides section");
		scrollToSection("Overrides");
		assertTrue(isElementPresent(reasonCode.overridesSectionOverridesTblAddBtn(), 5), "        Add new row button should show in Overrides table.");
		clickOnElement(reasonCode.overridesSectionOverridesTblAddBtn());
		Overrides expectedOverrides = setValueInReasonCodeOverridesWithSpecialCharacters();
		clickOnElement(reasonCode.overridesSectionAddRecordPopupOkBtn());

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(reasonCode.footerSaveAndClearBtn());

		logger.info("*** Action: - Click Reset button");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));
		clickOnElement(reasonCode.footerResetBtn());

		logger.info("*** Expected Results: - Verify that it's back to the Load Reason Code page");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Verify the values in Detail section are saved properly in DB");
		ErrCd errCd = errorDao.getErrCdByAbbrevEffDt(reasonCodeId7, effectiveDate);
		verifyDetailDataSaved(expectedDetail, errCd, errorGroup);

		logger.info("*** Action: - Verify the values in Overrides section are saved properly in DB");
		ErrCdOverride errCdOverride =errorDao.getErrCdOverrideByAbbrevEffDtLtrTxt(reasonCodeId7, effectiveDate, expectedOverrides.getCorrespondenceText());
		verifyOverridesDataSaved(expectedOverrides, errCdOverride);
		verifyOverridesDataSavedWithSpecial(expectedOverrides,errCdOverride);

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId7, reasonCodeTableId, "", wait, false);
		scrollToSection("Overrides");

		logger.info("*** Action: - Verify the values in Overrides section are displayed properly in UI");
		assertTrue(isElementPresent(reasonCode.overridesTableInOverridesSection(),5));
		reasonCode.overrideTableEditFirstRow();

		//If the correspondence text contains < sign then the lists won't match due to an existing issue.
		List<Overrides> actualOverridesList = getValuesInReasonCodeOverridesForSpecialCharacters(1);
		for (Overrides overrides:actualOverridesList){
			assertEquals(overrides, expectedOverrides);
		}
		clickOnElement(reasonCode.overridesSectionAddRecordPopupOkBtn());

		logger.info("*** Action: - Select a record in Overrides table and check Delete checkbox");
		clickOnElement(reasonCode.overridesSectionOverridesTblDeleteChkbox(2)); //1st record

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(reasonCode.footerSaveAndClearBtn());

		logger.info("*** Action: - Click Reset button");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));
		clickOnElement(reasonCode.footerResetBtn());

		logger.info("*** Expected Results: - Verify that it's back to the Load Reason Code page");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Verify the record is deleted from ERR_CD_OVERRIDE properly in DB");
		assertNull(errorDao.getErrCdXrefByErrCdAbbrevErrCdEffDtXrefDescr(reasonCodeId7, effectiveDate, expectedOverrides.getCorrespondenceText()));

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId7, reasonCodeTableId, "", wait, false);

		scrollToSection("Overrides");

		logger.info("*** Action: - Verify the deleted record does not show in Overrides table in UI");
		actualOverridesList = getValuesInReasonCodeOverrides(1); //1: one record;
		assertTrue(actualOverridesList.isEmpty());

		logger.info("*** Action: - Click Reset button");
		clickOnElement(reasonCode.footerResetBtn());
	}
	@Test(priority = 1, description = "Add record in the overrides section for Claim Status Error code")
	@Parameters({ "errorGroup", "reasonCodeId8", "csErrTypCd", "date1"})
	public void testXPR_2065(String errorGroup, String reasonCodeId8, String csErrTypCd, String date1) throws Exception {
		logger.info("===== Testing - testXPR_2065 =====");
		reasonCode = new ReasonCode(driver, wait);
		randomCharacter = new RandomCharacter(driver);

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date effectiveDate = new java.sql.Date(df.parse(date1).getTime());

		logger.info("*** Expected Results: - Verify that the Load Reason Code page is displayed");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId8, "", csErrTypCd, wait, false);

		logger.info("*** Expected Results: - Verify that fields are enabled/disabled properly");
		verifyFieldsDisplayed(errorGroup, " DEN");

		logger.info("*** Action: - Add a new record in Overrides section");
		scrollToSection("Overrides");
		assertTrue(isElementPresent(reasonCode.overridesSectionOverridesTblAddBtn(), 5), "        Add new row button should show in Overrides table.");
		clickOnElement(reasonCode.overridesSectionOverridesTblAddBtn());
		Overrides expectedOverrides = setValueInReasonCodeOverridesWithSpecialCharacters();
		clickOnElement(reasonCode.overridesSectionAddRecordPopupOkBtn());

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(reasonCode.footerSaveAndClearBtn());

		logger.info("*** Action: - Click Reset button");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));
		clickOnElement(reasonCode.footerResetBtn());

		logger.info("*** Expected Results: - Verify that it's back to the Load Reason Code page");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Verify the values in Overrides section are saved properly in DB");
		ErrCdOverride errCdOverride =errorDao.getErrCdOverrideByAbbrevEffDtLtrTxt(reasonCodeId8, effectiveDate, expectedOverrides.getCorrespondenceText());
		verifyOverridesDataSaved(expectedOverrides, errCdOverride);
		verifyOverridesDataSavedWithSpecial(expectedOverrides,errCdOverride);

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId8, "", csErrTypCd, wait, false);
		scrollToSection("Overrides");

		logger.info("*** Action: - Verify the values in Overrides section are displayed properly in UI");
		assertTrue(isElementPresent(reasonCode.overridesTableInOverridesSection(),5));
		reasonCode.overrideTableEditFirstRow();

		//If the correspondence text contains < sign then the lists won't match due to an existing issue.
		List<Overrides> actualOverridesList = getValuesInReasonCodeOverridesForSpecialCharacters(1);
		for (Overrides overrides:actualOverridesList){
			assertEquals(overrides, expectedOverrides);
		}
		clickOnElement(reasonCode.overridesSectionAddRecordPopupOkBtn());

		logger.info("*** Action: - Select a record in Overrides table and check Delete checkbox");
		clickOnElement(reasonCode.overridesSectionOverridesTblDeleteChkbox(2)); //1st record

		logger.info("*** Action: - Click Save And Clear button");
		clickOnElement(reasonCode.footerSaveAndClearBtn());

		logger.info("*** Action: - Click Reset button");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btnSaveAndClear")));
		clickOnElement(reasonCode.footerResetBtn());

		logger.info("*** Expected Results: - Verify that it's back to the Load Reason Code page");
		assertTrue(isOnLoadReasonCodePage());

		logger.info("*** Action: - Verify the record is deleted from ERR_CD_OVERRIDE properly in DB");
		assertNull(errorDao.getErrCdXrefByErrCdAbbrevErrCdEffDtXrefDescr(reasonCodeId8, effectiveDate, expectedOverrides.getCorrespondenceText()));

		logger.info("*** Action: - Select Error Group and enter Reason Code ID");
		setReasonCode(errorGroup, reasonCodeId8, "", csErrTypCd, wait, false);

		scrollToSection("Overrides");

		logger.info("*** Action: - Verify the deleted record does not show in Overrides table in UI");
		actualOverridesList = getValuesInReasonCodeOverrides(1); //1: one record;
		assertTrue(actualOverridesList.isEmpty());

		logger.info("*** Action: - Click Reset button");
		clickOnElement(reasonCode.footerResetBtn());


	}

	//========================================================================================================================================================
	private boolean isOnLoadReasonCodePage() throws Exception {
		boolean isOnPage = false;

		assertTrue(isElementPresent(reasonCode.reasonCodeTitle(), 5), "        User login successful. The Reason Code page is displayed.");
		isOnPage = reasonCode.reasonCodeTitle().getText().equals("Reason Code");
		return isOnPage;
	}

	private void setReasonCode(String errorGroup, String reasonCodeId, String reasonCodeTableId, String csErrTypCd, WebDriverWait wait, boolean isReload) throws Exception {
		if (!isReload) {
			reasonCode.selectDropDownByErrorGroup(errorGroup, wait);
		}
		switch (errorGroup) {
			case "Over-ride":
			case "Unbillable":
			case "Unpriceable":
				wait.until(ExpectedConditions.elementToBeClickable(reasonCode.reasonCodeIdInput()));
				setInputValue(reasonCode.reasonCodeIdInput(), reasonCodeId);
				break;

			case "Claim Status":
				reasonCode.selectDropDownByErrorType(csErrTypCd, wait);
				wait.until(ExpectedConditions.elementToBeClickable(reasonCode.reasonCodeIdInput()));
				setInputValue(reasonCode.reasonCodeIdInput(), reasonCodeId);
				break;

			case "Denial":
				wait.until(ExpectedConditions.visibilityOf(reasonCode.reasonCodeTblIdInput()));
				setInputValue(reasonCode.reasonCodeTblIdInput(), reasonCodeTableId);
				wait.until(ExpectedConditions.elementToBeClickable(reasonCode.reasonCodeIdInput()));
				setInputValue(reasonCode.reasonCodeIdInput(), reasonCodeId);
				break;
		}

		wait.until(ExpectedConditions.visibilityOf(reasonCode.detailSectionShortDescrInput()));
		wait.until(ExpectedConditions.elementToBeClickable(reasonCode.footerSaveAndClearBtn()));

		logger.info("       Entered a Reason Code ID: " + reasonCodeId + " in Error Group " + errorGroup + "; Reason Code Table ID: " + reasonCodeTableId);
	}

	private void verifyFieldsDisplayed(String errorGroup, String claimStatusErrType) {
		String errorCode = (errorGroup + claimStatusErrType).trim();

		switch (errorCode) {
			case "Claim Status":
				assertTrue(reasonCode.detailSectionShortDescrInput().isEnabled(), "       Short Description input field should be enabled.");

				assertTrue(reasonCode.detailSectionClmStaErrTypInput().isDisplayed(), "       ClaimStatusErrorType field should be displayed.");

				assertFalse(reasonCode.headerEffectiveDateInput().isEnabled(), "       Effective Date input field is enabled.");
				assertFalse(reasonCode.headerDosEffectiveDateInput().isEnabled(), "       DOS Effective Date input field is enabled.");
				assertFalse(reasonCode.headerDeleteReasonCodeChkBox().isEnabled(), "       Delete Reason Code checkbox is enabled.");

				assertFalse(reasonCode.detailSectionDescrInput().isEnabled(), "       Description text area is enabled.");
				assertFalse(reasonCode.fieldDropdown().isEnabled(), "       Field dropdown is enabled.");
				assertFalse(reasonCode.detailSectionUseManualDmdFmForReSubmChkBox().isEnabled(), "       Use Manual Demand Format for Re-Submit checkbox is enabled.");
				assertFalse(reasonCode.abnRelatedReasonDropdown().isEnabled(), "       ABN Related Reason dropdown is enabled.");

				assertFalse(reasonCode.noActionCheckbox().isEnabled(), "       No Action checkbox is enabled.");
				assertFalse(reasonCode.autoMatchCheckbox().isEnabled(), "       Auto Match checkbox is enabled.");
				assertFalse(reasonCode.matchCompareCheckbox().isEnabled(), "       Match Compare checkbox is enabled.");
				assertFalse(reasonCode.manualCheckbox().isEnabled(), "       Manual checkbox is enabled.");
				assertFalse(reasonCode.priorActnGrpCorrespondenceDropdown().isEnabled(), "      Correspondence dropdown is enabled.");
				assertFalse(reasonCode.priorActnGrpCorrespondenceTextInput().isEnabled(), "       Correspondence Text input field is enabled.");
				assertFalse(reasonCode.priorActnGrpOutsideAgencyDropdown().isEnabled(), "       Outside Agency dropdown is enabled.");

				for (int i = 1; i <= 10; i++) {
					assertFalse(reasonCode.finalActionRadioBtn(i).isEnabled(), "       Final Action radio buttons is enabled.");
				}

				assertTrue(reasonCode.addBtnInOverrides().getAttribute("class").contains("disabled"), "       Add button in Overrides table is disabled.");
				assertTrue(reasonCode.addBtnInCrossReference().getAttribute("class").contains("disabled"), "       Add button in Reason Code Cross Reference table is disabled.");
				break;

			case "Claim Status DEN":
				assertTrue(reasonCode.headerEffectiveDateInput().isEnabled(), "       Effective Date input field is enabled.");
				assertTrue(reasonCode.headerDosEffectiveDateInput().isEnabled(), "       DOS Effective Date input field is enabled.");
				assertTrue(reasonCode.headerDeleteReasonCodeChkBox().isEnabled(), "       Delete Reason Code checkbox is enabled.");

				assertTrue(reasonCode.detailSectionShortDescrInput().isEnabled(), "       Short Description input field should be enabled.");
				assertTrue(reasonCode.detailSectionDescrInput().isEnabled(), "       Description text area is enabled.");
				assertTrue(reasonCode.fieldDropdown().isEnabled(), "       Field dropdown is enabled.");
				assertTrue(reasonCode.detailSectionUseManualDmdFmForReSubmChkBox().isEnabled(), "       Use Manual Demand Format for Re-Submit checkbox is enabled.");
				assertTrue(reasonCode.detailSectionClmStaErrTypInput().isDisplayed(), "       ClaimStatusErrorType field should be displayed.");

				assertTrue(reasonCode.noActionCheckbox().isEnabled(), "       No Action checkbox is enabled.");
				assertTrue(reasonCode.autoMatchCheckbox().isEnabled(), "       Auto Match checkbox is enabled.");
				assertTrue(reasonCode.matchCompareCheckbox().isEnabled(), "       Match Compare checkbox is enabled.");
				assertTrue(reasonCode.manualCheckbox().isEnabled(), "       Manual checkbox is enabled.");
				assertTrue(reasonCode.priorActnGrpCorrespondenceDropdown().isEnabled(), "      Correspondence dropdown is enabled.");
				assertTrue(reasonCode.priorActnGrpCorrespondenceTextInput().isEnabled(), "       Correspondence Text input field is enabled.");
				assertTrue(reasonCode.priorActnGrpOutsideAgencyDropdown().isEnabled(), "       Outside Agency dropdown is enabled.");

				for (int i = 1; i <= 9; i++) {
					assertTrue(reasonCode.finalActionRadioBtn(i).isEnabled(), "       Final Action radio buttons are enabled.");
				}
				//assertFalse(reasonCode.finalActionRadioBtn(10).isEnabled(), "       Final Action Convert radio buttons is enabled.");

				assertFalse(reasonCode.addBtnInOverrides().getAttribute("class").contains("disabled"), "       Add button in Overrides table is disabled.");
				assertFalse(reasonCode.addBtnInCrossReference().getAttribute("class").contains("disabled"), "       Add button in Reason Code Cross Reference table is disabled.");
				break;
			case "Denial":
				assertTrue(reasonCode.detailSectionShortDescrInput().isEnabled(), "       Short Description input field should be enabled.");

				assertTrue(reasonCode.headerEffectiveDateInput().isEnabled(), "       Effective Date input field is enabled.");
				assertTrue(reasonCode.headerDosEffectiveDateInput().isEnabled(), "       DOS Effective Date input field is enabled.");
				assertTrue(reasonCode.headerDeleteReasonCodeChkBox().isEnabled(), "       Delete Reason Code checkbox is enabled.");

				assertTrue(reasonCode.detailSectionDescrInput().isEnabled(), "       Description text area is enabled.");
				assertTrue(reasonCode.fieldDropdown().isEnabled(), "       Field dropdown is enabled.");
				assertTrue(reasonCode.detailSectionUseManualDmdFmForReSubmChkBox().isEnabled(), "       Use Manual Demand Format for Re-Submit checkbox is enabled.");

				assertTrue(reasonCode.noActionCheckbox().isEnabled(), "       No Action checkbox is enabled.");
				assertTrue(reasonCode.autoMatchCheckbox().isEnabled(), "       Auto Match checkbox is enabled.");
				assertTrue(reasonCode.matchCompareCheckbox().isEnabled(), "       Match Compare checkbox is enabled.");
				assertTrue(reasonCode.manualCheckbox().isEnabled(), "       Manual checkbox is enabled.");
				assertTrue(reasonCode.priorActnGrpCorrespondenceDropdown().isEnabled(), "      Correspondence dropdown is enabled.");
				assertTrue(reasonCode.priorActnGrpCorrespondenceTextInput().isEnabled(), "       Correspondence Text input field is enabled.");
				assertTrue(reasonCode.priorActnGrpOutsideAgencyDropdown().isEnabled(), "       Outside Agency dropdown is enabled.");

				for (int i = 1; i <= 9; i++) {
					assertTrue(reasonCode.finalActionRadioBtn(i).isEnabled(), "       Final Action radio buttons are enabled.");
				}
				//assertFalse(reasonCode.finalActionRadioBtn(10).isEnabled(), "       Final Action Convert radio buttons is enabled.");

				assertFalse(reasonCode.addBtnInOverrides().getAttribute("class").contains("disabled"), "       Add button in Overrides table is disabled.");
				assertFalse(reasonCode.addBtnInCrossReference().getAttribute("class").contains("disabled"), "       Add button in Reason Code Cross Reference table is disabled.");
				break;
			case "Over-ride":
				assertFalse(reasonCode.detailSectionClmStaErrTypInput().isDisplayed(), "       ClaimStatusErrorType field should not be displayed.");

				assertTrue(reasonCode.headerEffectiveDateInput().isEnabled(), "       Effective Date input field is enabled.");
				assertTrue(reasonCode.headerDosEffectiveDateInput().isEnabled(), "       DOS Effective Date input field is enabled.");
				assertTrue(reasonCode.headerDeleteReasonCodeChkBox().isEnabled(), "       Delete Reason Code checkbox is enabled.");

				assertTrue(reasonCode.detailSectionShortDescrInput().isEnabled(), "       Short Description input field should be enabled.");
				assertTrue(reasonCode.detailSectionDescrInput().isEnabled(), "       Description text area is enabled.");
				assertFalse(reasonCode.fieldDropdown().isEnabled(), "       Field dropdown is enabled.");
				assertFalse(reasonCode.detailSectionUseManualDmdFmForReSubmChkBox().isEnabled(), "       Use Manual Demand Format for Re-Submit checkbox is enabled.");
				assertFalse(reasonCode.abnRelatedReasonDropdown().isEnabled(), "       ABN Related Reason dropdown is enabled.");

				assertTrue(reasonCode.autoMatchCheckbox().isEnabled(), "       Auto Match checkbox is enabled.");
				assertTrue(reasonCode.matchCompareCheckbox().isEnabled(), "       Match Compare checkbox is enabled.");
				assertTrue(reasonCode.manualCheckbox().isEnabled(), "       Manual checkbox is enabled.");
				assertTrue(reasonCode.priorActnGrpCorrespondenceDropdown().isEnabled(), "      Correspondence dropdown is enabled.");
				assertTrue(reasonCode.priorActnGrpCorrespondenceTextInput().isEnabled(), "       Correspondence Text input field is enabled.");
				assertTrue(reasonCode.priorActnGrpOutsideAgencyDropdown().isEnabled(), "       Outside Agency dropdown is enabled.");

//				assertTrue(reasonCode.finalActionRadioBtn(1).isEnabled(), "       Final Action Hold radio buttons is enabled.");
				for (int i = 1; i <= 9; i++) {
					assertTrue(reasonCode.finalActionRadioBtn(i).isEnabled(), "       Final Action radio buttons is enabled.");
				}
				assertFalse(reasonCode.finalActionRadioBtn(10).isEnabled(), "       Final Action Convert radio buttons is disabled.");
				assertFalse(reasonCode.addBtnInOverrides().getAttribute("class").contains("disabled"), "       Add button in Overrides table is disabled.");
				assertTrue(reasonCode.addBtnInCrossReference().getAttribute("class").contains("disabled"), "       Add button in Reason Code Cross Reference table is disabled.");
				break;
			case "Unbillable":
				//Header section
				assertTrue(reasonCode.headerEffectiveDateInput().isEnabled(), "       Effective Date input field is enabled.");
				assertTrue(reasonCode.headerDosEffectiveDateInput().isEnabled(), "       DOS Effective Date input field is enabled.");
				assertTrue(reasonCode.headerDeleteReasonCodeChkBox().isEnabled(), "       Delete Reason Code checkbox is enabled.");
				//Detail section
				assertTrue(reasonCode.detailSectionShortDescrInput().isEnabled(), "       Short Description input field should be enabled.");
				assertTrue(reasonCode.detailSectionDescrInput().isEnabled(), "       Description text area is enabled.");
				assertTrue(reasonCode.fieldDropdown().isEnabled(), "       Field dropdown is enabled.");
				assertFalse(reasonCode.detailSectionUseManualDmdFmForReSubmChkBox().isEnabled(), "       Use Manual Demand Format for Re-Submit checkbox is enabled.");
				assertFalse(reasonCode.abnRelatedReasonDropdown().isEnabled(), "       ABN Related Reason dropdown is enabled.");
				assertTrue(reasonCode.unbillableErrorTypeDropdown().isEnabled(), "       Unbillable Error Type Dropdown is enabled.");

				assertFalse(reasonCode.noActionCheckbox().isEnabled(), "       No Action checkbox is enabled.");
				assertTrue(reasonCode.autoMatchCheckbox().isEnabled(), "       Auto Match checkbox is enabled.");
				assertTrue(reasonCode.matchCompareCheckbox().isEnabled(), "       Match Compare checkbox is enabled.");
				assertTrue(reasonCode.manualCheckbox().isEnabled(), "       Manual checkbox is enabled.");
				assertTrue(reasonCode.priorActnGrpCorrespondenceDropdown().isEnabled(), "      Correspondence dropdown is enabled.");
				assertTrue(reasonCode.priorActnGrpCorrespondenceTextInput().isEnabled(), "       Correspondence Text input field is enabled.");
				assertTrue(reasonCode.priorActnGrpOutsideAgencyDropdown().isEnabled(), "       Outside Agency dropdown is enabled.");

				for (int i = 1; i <= 10; i++) {
					assertTrue(reasonCode.finalActionRadioBtn(i).isEnabled(), "       Final Action radio buttons is enabled.");
				}
				//Overrides section
				assertFalse(reasonCode.addBtnInOverrides().getAttribute("class").contains("disabled"), "       Add button in Overrides table is disabled.");
				//Cross Reference section
				assertFalse(reasonCode.addBtnInCrossReference().getAttribute("class").contains("disabled"), "       Add button in Reason Code Cross Reference table is disabled.");
				break;
			case "Unpriceable":
				//Header section
				assertTrue(reasonCode.headerEffectiveDateInput().isEnabled(), "       Effective Date input field is enabled.");
				assertTrue(reasonCode.headerDosEffectiveDateInput().isEnabled(), "       DOS Effective Date input field is enabled.");
				assertTrue(reasonCode.headerDeleteReasonCodeChkBox().isEnabled(), "       Delete Reason Code checkbox is enabled.");
				//Detail section
				assertTrue(reasonCode.detailSectionShortDescrInput().isEnabled(), "       Short Description input field should be enabled.");
				assertTrue(reasonCode.detailSectionDescrInput().isEnabled(), "       Description text area is enabled.");
				assertFalse(reasonCode.fieldDropdown().isEnabled(), "       Field dropdown is enabled.");
				assertFalse(reasonCode.detailSectionUseManualDmdFmForReSubmChkBox().isEnabled(), "       Use Manual Demand Format for Re-Submit checkbox is enabled.");
				assertFalse(reasonCode.abnRelatedReasonDropdown().isEnabled(), "       ABN Related Reason dropdown is enabled.");

				assertFalse(reasonCode.noActionCheckbox().isEnabled(), "       No Action checkbox is enabled.");
				assertTrue(reasonCode.autoMatchCheckbox().isEnabled(), "       Auto Match checkbox is enabled.");
				assertTrue(reasonCode.matchCompareCheckbox().isEnabled(), "       Match Compare checkbox is enabled.");
				assertTrue(reasonCode.manualCheckbox().isEnabled(), "       Manual checkbox is enabled.");
				assertTrue(reasonCode.priorActnGrpCorrespondenceDropdown().isEnabled(), "      Correspondence dropdown is enabled.");
				assertTrue(reasonCode.priorActnGrpCorrespondenceTextInput().isEnabled(), "       Correspondence Text input field is enabled.");
				assertTrue(reasonCode.priorActnGrpOutsideAgencyDropdown().isEnabled(), "       Outside Agency dropdown is enabled.");

				assertTrue(reasonCode.finalActionRadioBtn(1).isEnabled(), "       Final Action Hold radio buttons is enabled.");
				assertTrue(reasonCode.finalActionRadioBtn(4).isEnabled(), "       Final Action Write-off radio buttons is enabled.");
				assertTrue(reasonCode.finalActionRadioBtn(5).isEnabled(), "       Final Action Collections radio buttons is enabled.");
				assertTrue(reasonCode.finalActionRadioBtn(8).isEnabled(), "       Final Action Reprice to Specific Payor radio buttons is enabled.");
				assertTrue(reasonCode.finalActionRadioBtn(9).isEnabled(), "       Final Action Reprice to Next Payor radio buttons is enabled.");
				assertFalse(reasonCode.finalActionRadioBtn(2).isEnabled(), "       Final Action  Move to Specific Payor radio buttons is disabled.");
				assertFalse(reasonCode.finalActionRadioBtn(3).isEnabled(), "       Final Action Next Payor radio buttons is disabled.");
				assertFalse(reasonCode.finalActionRadioBtn(6).isEnabled(), "       Final Action Client radio buttons is disabled.");
				assertFalse(reasonCode.finalActionRadioBtn(7).isEnabled(), "       Final Action Patient radio buttons is disabled.");
				assertFalse(reasonCode.finalActionRadioBtn(10).isEnabled(), "       Final Action Convert radio buttons is disabled.");
				//Overrides section
				assertTrue(reasonCode.addBtnInOverrides().getAttribute("class").contains("disabled"), "       Add button in Overrides table is disabled.");
				//Cross Reference section
				assertTrue(reasonCode.addBtnInCrossReference().getAttribute("class").contains("disabled"), "       Add button in Reason Code Cross Reference table is disabled.");
				break;
		}
	}

	private void verifyAuditDetailDisplayed(List<String> auditDetailList){

		WebElement action = reasonCode.auditDetailTable().findElement(By.xpath("//tbody/tr/td[contains(text(), '" + auditDetailList.get(0) + "')]"));
		wait.until(ExpectedConditions.visibilityOf(action));
		WebElement table = reasonCode.auditDetailTable().findElement(By.xpath("//tbody/tr/td[contains(text(), '" + auditDetailList.get(1) + "')]"));
		Assert.assertTrue(table.isDisplayed());
		WebElement field = reasonCode.auditDetailTable().findElement(By.xpath("//tbody/tr/td[contains(text(), '" + auditDetailList.get(2) + "')]"));
		Assert.assertTrue(field.isDisplayed());
		WebElement oldValue = reasonCode.auditDetailTable().findElement(By.xpath("//tbody/tr/td[contains(text(), '" + auditDetailList.get(3) + "')]"));
		Assert.assertTrue(oldValue.isDisplayed());
		WebElement newValue = reasonCode.auditDetailTable().findElement(By.xpath("//tbody/tr/td[contains(text(), '" + auditDetailList.get(4) + "')]"));
		Assert.assertTrue(newValue.isDisplayed());
	}

	private Header getValuesInReasonCodeHeader(String errorGroup) throws ParseException {
		Header header = new Header();

		if (errorGroup.equals("Denial")) {
			header.setReasonCodeTableId(reasonCode.headerReasonCodeTableId().getAttribute("value").trim());
			header.setName(reasonCode.headerName().getAttribute("value").trim());
		}

		header.setErrorGroup(errorGroup);
		header.setReasonCodeId(reasonCode.headerReasonCodeIdLbl().getAttribute("value").trim());

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date effectiveDate = new java.sql.Date(df.parse(reasonCode.effectiveDateDropDown().getText().trim()).getTime());
		header.setEffectiveDate(effectiveDate);

		Date dosEffectiveDate = new java.sql.Date(df.parse(reasonCode.headerDosEffectiveDateInput().getAttribute("value").trim()).getTime());
		header.setDosEffectiveDate(dosEffectiveDate);

		header.setDeleteReasonCode(false);
		return header;
	}

	private Header setValuesInReasonCodeHeader(String errorGroup, String reasonCodeId, Date date) throws Exception {
		Header header = new Header();

		header.setErrorGroup(errorGroup);
		header.setReasonCodeId(reasonCodeId);

		Format formatter = new SimpleDateFormat("MM/dd/yyyy");
		setInputValue(reasonCode.headerEffectiveDateInput(), formatter.format(date));
		header.setEffectiveDate(date);

		header.setDosEffectiveDate(date);

		return header;
	}

	private Detail setValuesInReasonCodeDetail(String errorGroup, String claimStatusErrorType) throws Exception {
		Detail detail = new Detail();
		//Short Description
		String shortDescription = randomCharacter.getRandomAlphaString(15);
		setInputValue(reasonCode.detailSectionShortDescrInput(), shortDescription);
		detail.setShortDescription(shortDescription);
		//Description
		String description = randomCharacter.getRandomAlphaString(25);
		setInputValue(reasonCode.detailSectionDescrInput(), description);
		detail.setDescription(description);
		if (!errorGroup.equals("Over-ride")) {
			//Field
			selectItem(reasonCode.fieldDropdown(), "Authorization");
			detail.setField("Authorization");
			//Use Manual Demand Format for Re-Submit
			selectCheckBox(reasonCode.detailSectionUseManualDmdFmForReSubmChkBox());
			detail.setUseManualDemandFormatForReSubmit(true);
			//Prioritized Action
			clickHiddenPageObject(reasonCode.manualCheckbox(), 0);
			//selectCheckBox(reasonCode.manualCheckbox());
			detail.setManual(true);
			//Correspondence
			scrollToElement(reasonCode.detailSectionUseManualDmdFmForReSubmChkBox());
			selectItemByVal(reasonCode.priorActnGrpCorrespondenceDropdown(), "2"); //2: Client;
			detail.setCorrespondence("Client");
			//Correspondence Text
			String correspondenceText = randomCharacter.getRandomAlphaString(20);
			setInputValue(reasonCode.priorActnGrpCorrespondenceTextInput(), correspondenceText);
			detail.setCorrespondenceText(correspondenceText);
			//Outside Agency
			selectItemByVal(reasonCode.priorActnGrpOutsideAgencyDropdown(), "3"); //3: CODERYTE;
			detail.setOutsideAgency("CODERYTE");
			//Outside Agency Pre Corresp
			selectItemByVal(reasonCode.outsideAgencyPreCorrespDropdown(), "6"); //6: FR-ADD-PRE;
			detail.setOutsideAgencyPreCorresp("FR-ADD-PRE");
			//Final Action
			clickOnElement(reasonCode.finalActionRadioBtn(6));
			setInputValue(reasonCode.finalActGrpMoveToPyrInput(), "P");
			detail.setFinalAction("Move to Specific Payor");
			detail.setPyrId("P");
		}

		switch (claimStatusErrorType) {
			case "DEN":
				detail.setClaimStatusErrorType("Denial");
				break;
			case "ACK":
				detail.setClaimStatusErrorType("Acknowledgement");
				break;
		}

		if (errorGroup.equals("Denial")){
			//ABN Related Reason
			selectItemByVal(reasonCode.abnRelatedReasonDropdown(), "1"); //1: Diagnosis
			detail.setAbnRelatedReason("Diagnosis");
			//ABN Signed - Final Action
			scrollToElement(reasonCode.detailSectionShortDescrInput());
			clickOnElement(reasonCode.abnSignedFinalActionRadioBtn(2)); //id=2 and value=9(Write-Off)
			detail.setAbnSignedFinalAction(getAbnSignedFinalAction(9));
			wait.until(ExpectedConditions.elementToBeClickable(By.id("adjCdIdAbnSignedAbbrev")));
			//Adjustment Code
			setInputValue(reasonCode.abnSignedFinalActionAdjustmentCodeInput(), "INK");
			detail.setAdjustmentCode("INK");
		}else if (errorGroup.equals("Over-ride")){
			//Error Level
			selectItemByVal(reasonCode.priorActnGrpCorrespondenceDropdown(), "2"); //2: Client;
			detail.setCorrespondence("Client");
			//Correspondence Text
			String correspondenceText = randomCharacter.getRandomAlphaString(20);
			setInputValue(reasonCode.priorActnGrpCorrespondenceTextInput(), correspondenceText);
			detail.setCorrespondenceText(correspondenceText);
			selectItemByVal(reasonCode.priorActnGrpOutsideAgencyDropdown(), "3"); //3: CODERYTE;
			detail.setOutsideAgency("CODERYTE");
			//Outside Agency Pre Corresp
			selectItemByVal(reasonCode.outsideAgencyPreCorrespDropdown(), "6"); //6: FR-ADD-PRE;
			detail.setOutsideAgencyPreCorresp("FR-ADD-PRE");
			scrollToElement(reasonCode.detailSectionUseManualDmdFmForReSubmChkBox());
			clickOnElement(reasonCode.errorLevelRadioBtn(4));//Procedure
			detail.setErrorLevel("Procedure");
			//Prioritized Action
			selectCheckBox(reasonCode.manualCheckbox());
			detail.setManual(true);
			//Final Action
			//scrollToElement(reasonCode.finalActionRadioBtn(1));
			clickOnElement(reasonCode.finalActionRadioBtn(1));//Hold
			detail.setFinalAction("Hold");
		}

		return detail;
	}

	private void scrollToSection(String sectionTitle) {
		reasonCode.footerSectionSearchInput().clear();
		reasonCode.footerSectionSearchInput().sendKeys(sectionTitle);
	}

	private Overrides setValueInReasonCodeOverrides() throws Exception {
		Overrides overrides = new Overrides();
		//Payor ID
		assertTrue(isElementPresent(reasonCode.overridesSectionAddRecordPopupPayorIdInput(), 5), "        Payor ID input field should show in Add Record popup.");
		setInputValue(reasonCode.overridesSectionAddRecordPopupPayorIdInput(), "P");
		overrides.setPayorId("P");
		overrides.setPayorName(payorDao.getPyrByPyrAbbrv("P").getName().trim());
		//Client ID
		setInputValue(reasonCode.overridesSectionAddRecordPopupClientIdInput(), "100");
		overrides.setClientId("100");
		overrides.setClientName(clientDao.getClnByClnAbbrev("100").getBilAcctNm().trim());
		//Proc Code
		setInputValue(reasonCode.overridesSectionAddRecordPopupProcCodeInput(), "80020");
		overrides.setProcCode("80020");
		//Man Fmt for Re-Submit
		selectCheckBox(reasonCode.overridesSectionAddRecordPopupManFmtForReSubmitChk());
		overrides.setManFmtForReSubmit(true);
		//Manual
		selectCheckBox(reasonCode.overridesSectionAddRecordPopupManualChk());
		overrides.setManual(true);
		//Correspondence
		selectItemByVal(reasonCode.overridesSectionAddRecordPopupCorrespondenceDropdown(), "2"); //2: Client;
		overrides.setCorrespondence("Client");
		//Correspondence Text
		String correspondenceText = randomCharacter.getRandomAlphaString(20);
		setInputValue(reasonCode.overridesSectionAddRecordPopupCorrespondenceTxt(), correspondenceText);
		overrides.setCorrespondenceText(correspondenceText);
		//Outside Agency
		selectItemByVal(reasonCode.overridesSectionAddRecordPopupOutsideAgencyDropdown(), "3"); //3: CODERYTE;
		overrides.setOutsideAgency("CODERYTE");
		//Outside Agency Pre Corresp
		selectItemByVal(reasonCode.overridesSectionAddRecordPopupOutsideAgencyPreCorrespDropdown(), "7"); //7: CODERYTE Pre Corresp;
		overrides.setOutsideAgencyPreCorresp("CODERYTE-PRE");
		//Final Action
		selectItem(reasonCode.finalActionDropdownInAddRecord(), "Next Payor"); //11: Next Payor;
		overrides.setFinalAction("NextPayor");
		//Authorized By
		String user = reasonCode.overridesSectionAddRecordPopupAuthorizedByTxt().getAttribute("value").trim();
		overrides.setAuthorizedBy(user);

		return overrides;
	}

	private CrossReference setValuesInReasonCodeCrossReference(Date date) throws Exception {
		CrossReference crossReference = new CrossReference();
		Format formatter = new SimpleDateFormat("MM/dd/yyyy");
		String effectiveDate = formatter.format(date);

		assertTrue(isElementPresent(reasonCode.crossReferenceTblEffDtInput(), 5), "        Effective Date input field should show.");
		setInputValue(reasonCode.crossReferenceTblEffDtInput(), effectiveDate);
		crossReference.setEffectiveDate(date);

		setInputValue(reasonCode.crossReferenceTblExpDtInput(), effectiveDate);
		crossReference.setExpirationDate(date);

		selectItemByVal(reasonCode.crossReferenceDescriptionDropdown(), "1101"); //1101: AUTOTEST-AUTOMATION TEST;
		crossReference.setCrossReferenceDescription("AUTOTEST-AUTOMATION TEST");

		return crossReference;
	}

	private Overrides setValueInReasonCodeOverridesWithSpecialCharacters() throws Exception {
		Overrides overrides = new Overrides();
		//Payor ID
		assertTrue(isElementPresent(reasonCode.overridesSectionAddRecordPopupPayorIdInput(), 5), "        Payor ID input field should show in Add Record popup.");
		setInputValue(reasonCode.overridesSectionAddRecordPopupPayorIdInput(), "P");
		overrides.setPayorId("P");
		overrides.setPayorName(payorDao.getPyrByPyrAbbrv("P").getName().trim());
		//Client ID
		setInputValue(reasonCode.overridesSectionAddRecordPopupClientIdInput(), "100");
		overrides.setClientId("100");
		overrides.setClientName(clientDao.getClnByClnAbbrev("100").getBilAcctNm().trim());
		//Proc Code
		setInputValue(reasonCode.overridesSectionAddRecordPopupProcCodeInput(), "80020");
		overrides.setProcCode("80020");
		//Man Fmt for Re-Submit
		selectCheckBox(reasonCode.overridesSectionAddRecordPopupManFmtForReSubmitChk());
		overrides.setManFmtForReSubmit(true);
		//Manual
		selectCheckBox(reasonCode.overridesSectionAddRecordPopupManualChk());
		overrides.setManual(true);
		//Correspondence
		selectItemByVal(reasonCode.overridesSectionAddRecordPopupCorrespondenceDropdown(), "2"); //2: Client;
		overrides.setCorrespondence("Client");
		//Correspondence Text
		String correspondenceText = randomCharacter.getRandomAlphaStringwithSpecialCharacters(20);
		setInputValue(reasonCode.overridesSectionAddRecordPopupCorrespondenceTxt(), correspondenceText);
		overrides.setCorrespondenceText(correspondenceText);
		//Outside Agency
		selectItemByVal(reasonCode.overridesSectionAddRecordPopupOutsideAgencyDropdown(), "3"); //3: CODERYTE;
		overrides.setOutsideAgency("CODERYTE");
		//Outside Agency Pre Corresp
		selectItemByVal(reasonCode.overridesSectionAddRecordPopupOutsideAgencyPreCorrespDropdown(), "7"); //7: CODERYTE Pre Corresp;
		overrides.setOutsideAgencyPreCorresp("CODERYTE-PRE");
		//Final Action
		selectItem(reasonCode.finalActionDropdownInAddRecord(), "Next Payor"); //11: Next Payor;
		overrides.setFinalAction("NextPayor");
		//Authorized By
		String user = reasonCode.overridesSectionAddRecordPopupAuthorizedByTxt().getAttribute("value").trim();
		overrides.setAuthorizedBy(user);

		return overrides;
	}
	private void verifyHeaderDataSaved(Header header, ErrCd errCd, String errorGroup, Date date) throws XifinDataAccessException {
		int errorGroupId = errorDao.getErrGrpByDescr(header.getErrorGroup()).getErrGrpId();

		switch (errorGroup) {
			case "Claim Status":
				assertEquals(errCd.getErrGrpId(), errorGroupId);
				break;
			case "Denial":
				int reasonCodeTableId = payorDao.getDnlByAbbrv(header.getReasonCodeTableId()).getDnlTblId();
				assertEquals(errCd.getDnlTblId(), reasonCodeTableId);
				break;
		}

		assertEquals(errCd.getEffDt(), date);
		assertEquals(errCd.getDosEffDt(), date);
	}

	private void verifyDetailDataSaved(Detail detail, ErrCd errCd, String errorGroup) throws XifinDataAccessException, XifinDataNotFoundException {

		switch (errorGroup) {
			case "Claim Status":
				int claimStatusErrTypId = errorProcessingDao.getClaimStatusErrTypByDescr(detail.getClaimStatusErrorType()).getClaimStatusErrTypId();
				assertEquals(errCd.getClaimStatusErrTypId(), claimStatusErrTypId);
				//Field
				int fldId = payorDao.getFldByDescr(detail.getField()).getFldId();
				assertEquals(errCd.getFldId(), fldId);
				//Use Manual Demand Format for Re-Submit
				assertEquals(errCd.getIsResubUsingManFmt(), detail.isUseManualDemandFormatForReSubmit());
				//Prioritized Action
				assertEquals(errCd.getIsManActn(), detail.isManual());
				int correspTypId = errorProcessingDao.getEpCorrespTypByDescr(detail.getCorrespondence()).getCorrespTypId();
				assertEquals(errCd.getCorrespTypId(), correspTypId);
				assertEquals(errCd.getLtrTxt(), detail.getCorrespondenceText());
				int outAgncyId = agencyDao.getAgncyByAbbrev( detail.getOutsideAgency()).getAgncyId();
				assertEquals(errCd.getOutAgncyId(), outAgncyId);
				int outAgncyPreCorrespId = agencyDao.getAgncyByAbbrev( detail.getOutsideAgencyPreCorresp()).getAgncyId();
				assertEquals(errCd.getOutAgncyIdPreCorresp(), outAgncyPreCorrespId);
				//Final Action
				int finalActionId = finalDao.getFinalActnTypByDescr(detail.getFinalAction()).getFinal_actn_id();
				assertEquals(errCd.getFinalActnId(), finalActionId);
				int specificPyrId = payorDao.getPyrByPyrAbbrv(detail.getPyrId()).getPyrId();
				assertEquals(errCd.getSpecificPyrId(), Integer.valueOf(specificPyrId));
				break;
			case "Denial":
				//Field
				fldId = payorDao.getFldByDescr(detail.getField()).getFldId();
				assertEquals(errCd.getFldId(), fldId);
				//Use Manual Demand Format for Re-Submit
				assertEquals(errCd.getIsResubUsingManFmt(), detail.isUseManualDemandFormatForReSubmit());
				//ABN Related Reason
				String abnRelatedReasonInDB = getAbnRelatedReason(errCd.getProcCdSpcfc());
				assertEquals(abnRelatedReasonInDB, detail.getAbnRelatedReason());
				//ABN Signed - Final Action
				String abnSignedFinalActionInDB = getAbnSignedFinalAction(errCd.getFinalActnIdAbnSigned());
				assertEquals(abnSignedFinalActionInDB, detail.getAbnSignedFinalAction());
				//Adjustment Code
				String adjustmentCodeInDB = adjustmentCodeDao.getAdjCdByAdjCdId(errCd.getAdjCdIdAbnSigned()).getAdjAbbrv();
				assertEquals(adjustmentCodeInDB, detail.getAdjustmentCode());
				//Prioritized Action
				assertEquals(errCd.getIsManActn(), detail.isManual());
				correspTypId = errorProcessingDao.getEpCorrespTypByDescr(detail.getCorrespondence()).getCorrespTypId();
				assertEquals(errCd.getCorrespTypId(), correspTypId);
				assertEquals(errCd.getLtrTxt(), detail.getCorrespondenceText());
				outAgncyId = agencyDao.getAgncyByAbbrev( detail.getOutsideAgency()).getAgncyId();
				assertEquals(errCd.getOutAgncyId(), outAgncyId);
				outAgncyPreCorrespId = agencyDao.getAgncyByAbbrev( detail.getOutsideAgencyPreCorresp()).getAgncyId();
				assertEquals(errCd.getOutAgncyIdPreCorresp(), outAgncyPreCorrespId);
				//Final Action
				finalActionId = finalDao.getFinalActnTypByDescr(detail.getFinalAction()).getFinal_actn_id();
				assertEquals(errCd.getFinalActnId(), finalActionId);
				specificPyrId = payorDao.getPyrByPyrAbbrv(detail.getPyrId()).getPyrId();
				assertEquals(errCd.getSpecificPyrId(), Integer.valueOf(specificPyrId));
				break;
			case "Over-ride":
				//Error Level
				String errorLevelTypDescrInDB = errorDao.getErrLvlTypByErrLvlTypId(errCd.getErrLvlTypId()).getDescr().split(" ")[0].trim();
				assertEquals(errorLevelTypDescrInDB, detail.getErrorLevel());
				//Prioritized Action
				assertEquals(errCd.getIsManActn(), detail.isManual());
				//Final Action
				finalActionId = finalDao.getFinalActnTypByDescr(detail.getFinalAction()).getFinal_actn_id();
				assertEquals(errCd.getFinalActnId(), finalActionId);
				break;
		}
		//Short Description
		assertEquals(errCd.getShortDescr(), detail.getShortDescription());
		//Description
		assertEquals(errCd.getDetlDescr(), detail.getDescription());
	}

	private void verifyOverridesDataSaved(Overrides expectedOverrides, ErrCdOverride errCdOverride) throws XifinDataAccessException, XifinDataNotFoundException {
		//Override ID
		if (!(expectedOverrides.getPayorId() == null)) {
			int pyrId = payorDao.getPyrByPyrAbbrv(expectedOverrides.getPayorId()).getPyrId();
			assertEquals(errCdOverride.getPyrId(), pyrId);
		}

		if (!(expectedOverrides.getPayorGroup() == null)) {
			int pyrGrpId = payorDao.getPyrGrpByPyrGrpNm(expectedOverrides.getPayorName()).getPyrGrpId();
			assertEquals(errCdOverride.getPyrGrpId(), pyrGrpId);
		}

		if (!(expectedOverrides.getClientId() == null)) {
			int clnId = clientDao.getClnByClnAbbrev(expectedOverrides.getClientId()).getClnId();
			assertEquals(errCdOverride.getClnId(), clnId);
		}

		if (!(expectedOverrides.getClientAccountType() == null)) {
			int clnAccntTypId = clientDao.getClnAccntTypByDescr(expectedOverrides.getClientAccountType()).getAccntTypId();
			assertEquals(errCdOverride.getClnId(), clnAccntTypId);
		}

		//Proc Code
		assertEquals(errCdOverride.getProcId(), expectedOverrides.getProcCode());
		//Man Fmt for Re-Submit
		assertEquals(errCdOverride.getIsResubUsingManFmt(), expectedOverrides.isManFmtForReSubmit());
		//Auto Match
		assertEquals(errCdOverride.getIsAutoMatch(), expectedOverrides.isAutoMatch());
		// Match Compare
		assertEquals(errCdOverride.getIsMatchCompare(), expectedOverrides.isMatchCompare());
		//Manual
		assertEquals(errCdOverride.getIsForceToMan(), expectedOverrides.isManual());
		//Correspondence
		int correspTypId = errorProcessingDao.getEpCorrespTypByDescr(expectedOverrides.getCorrespondence()).getCorrespTypId();
		assertEquals(errCdOverride.getCorrespTypId(), correspTypId);
		//Outside Agency
		int outAgncyId = agencyDao.getAgncyByAbbrev( expectedOverrides.getOutsideAgency()).getAgncyId();
		assertEquals(errCdOverride.getOutAgncyId(), outAgncyId);
		//Outside Agency Pre Corresp
		int outAgncyPreCorrespId = agencyDao.getAgncyByAbbrev( expectedOverrides.getOutsideAgencyPreCorresp()).getAgncyId();
		assertEquals(errCdOverride.getOutAgncyIdPreCorresp(), outAgncyPreCorrespId);
		//Final Action
		int finalActionId = finalDao.getFinalActnTypByDescr(expectedOverrides.getFinalAction()).getFinal_actn_id();
		assertEquals(errCdOverride.getFinalActnTyp(), finalActionId);
		//Authorized By
		assertEquals(errCdOverride.getUserId(), expectedOverrides.getAuthorizedBy());
	}

	private void verifyCrossReferenceDataSaved(CrossReference expectedCrossReference, ErrCdXref errCdXref) throws XifinDataNotFoundException, XifinDataAccessException {
		//Effective Date
		assertEquals(errCdXref.getEffDt(), expectedCrossReference.getEffectiveDate());
		//Expiration Date
		assertEquals(errCdXref.getExpDt(), expectedCrossReference.getExpirationDate());
		//Cross Reference Description
		String xRefDescr = expectedCrossReference.getCrossReferenceDescription().split("-")[1];
		int xRefId = crossReferenceDao.getXrefByDescr(xRefDescr).getXrefId();
		assertEquals(errCdXref.getXrefId(), xRefId);
	}
	private void verifyOverridesDataSavedWithSpecial(Overrides expectedOverrides, ErrCdOverride errCdOverride) {

		if(!(expectedOverrides.getCorrespondenceText()==null)){
			assertEquals(errCdOverride.getLtrTxt(),expectedOverrides.getCorrespondenceText());
		}

	}

	private Detail getValuesInReasonCodeDetail(String errorGroup) throws XifinDataAccessException {
		Detail detail = new Detail();

		//Short Description
		detail.setShortDescription(reasonCode.detailSectionShortDescrInput().getAttribute("value").trim());
		//Description
		detail.setDescription(reasonCode.detailSectionDescrInput().getText().trim());
		//Field
		if (reasonCode.fieldDropdown().isEnabled()) {
			detail.setField(reasonCode.fieldDropdownText().getText());
		} else{
			detail.setField(null);
		}
		//detail.setClaimStatusErrorType(reasonCode.detailSectionClmStaErrTypInput().getAttribute("value"));
		//Use Manual Demand Format for Re-Submit
		detail.setUseManualDemandFormatForReSubmit(reasonCode.detailSectionUseManualDmdFmForReSubmChkBox().isSelected());
		//Manual
		detail.setManual(reasonCode.manualCheckbox().isSelected());
		//Correspondence
		if (reasonCode.priorActnGrpCorrespondenceDropdown().isEnabled()) {
			int correspTypId = Integer.valueOf(reasonCode.priorActnGrpCorrespondenceDropdown().getAttribute("value"));
			detail.setCorrespondence(errorProcessingDao.getEpCorrespTypByCorrespTypId(correspTypId).getDescr().trim());
		} else {
			detail.setCorrespondence(null);
		}
		//Correspondence Text
		if (reasonCode.priorActnGrpCorrespondenceTextInput().isEnabled()) {
			detail.setCorrespondenceText(reasonCode.priorActnGrpCorrespondenceTextInput().getText());
		} else {
			detail.setCorrespondenceText(null);
		}
		//OutsideAgency
		if (reasonCode.priorActnGrpOutsideAgencyDropdown().isEnabled()) {
			int agncyTypId = Integer.valueOf(reasonCode.priorActnGrpOutsideAgencyDropdown().getAttribute("value"));
			detail.setOutsideAgency(agencyDao.getAgncyByAgncyId(agncyTypId).getAbbrev().trim());
		}else {
			detail.setOutsideAgency(null);
		}
		//OutsideAgencyPreCorresp
		if (reasonCode.outsideAgencyPreCorrespDropdown().isEnabled()) {
			int agncyTypId = Integer.valueOf(reasonCode.outsideAgencyPreCorrespDropdown().getAttribute("value"));
			detail.setOutsideAgencyPreCorresp(agencyDao.getAgncyByAgncyId(agncyTypId).getAbbrev().trim());
		}else {
			detail.setOutsideAgencyPreCorresp(null);
		}
		//Final Action
		for (int i = 1; i<=10; i++){
			if (reasonCode.finalActionRadioBtn(i).isSelected()){
				detail.setFinalAction(reasonCode.finalActionLabelInDetail(i).getText());
				if (i==6 || i==4){
					detail.setPyrId(reasonCode.finalActGrpMoveToPyrInput().getAttribute("value"));
				}
			}
		}

		switch (errorGroup) {
			case "Claim Status":
				//Claim Status Error Type
				detail.setClaimStatusErrorType(reasonCode.detailSectionClmStaErrTypInput().getAttribute("value"));
				break;
			case "Denial":
				//ABN Related Reason
				int abnRelatedReasonId = Integer.valueOf(reasonCode.abnRelatedReasonDropdown().getAttribute("value"));
				detail.setAbnRelatedReason(getAbnRelatedReason(abnRelatedReasonId));
				//Abn Signed - Final Action
				int abnSignedFinalActionId = Integer.valueOf(reasonCode.abnSignedFinalActionRadioBtn(2).getAttribute("value"));
				detail.setAbnSignedFinalAction(getAbnSignedFinalAction(abnSignedFinalActionId));
				//Adjustment Code
				String adjustmentCode = reasonCode.abnSignedFinalActionAdjustmentCodeInput().getAttribute("value");
				detail.setAdjustmentCode(adjustmentCode);
				break;
			case "Over-ride":
				//Error Level
				for (int i = 1; i<=4; i++){
					if (reasonCode.errorLevelRadioBtn(i).isSelected()){
						detail.setErrorLevel(reasonCode.errorLevelLabel(i).getText());
					}
				}
				break;
		}

		return detail;
	}

	private List<Overrides> getValuesInReasonCodeOverrides(int size){

		List<Overrides> overridesList  = new ArrayList<>();
		try {
			for (int i = 1; i <= size; i++) {
				Overrides overrides = new Overrides();
				//Override ID
				overrides.setPayorId(reasonCode.overridesSectionOverridesTblOverrideIdTxt(String.valueOf(i + 1)).getAttribute("title").split(" ")[1].trim());
				overrides.setPayorName(StringUtils.removeEnd(reasonCode.overridesSectionOverridesTblOverrideIdTxt(String.valueOf(i + 1)).getAttribute("title").split(" ")[3].trim(), "Client:"));
				overrides.setClientId(reasonCode.overridesSectionOverridesTblOverrideIdTxt(String.valueOf(i + 1)).getAttribute("title").split(" ")[4].trim());
				overrides.setClientName(reasonCode.overridesSectionOverridesTblOverrideIdTxt(String.valueOf(i + 1)).getAttribute("title").split("[|]")[2].trim());
				//Proc Code
				overrides.setProcCode(reasonCode.overridesSectionOverridesTblProcCodeTxt(String.valueOf(i + 1)).getAttribute("title"));
				//Man Fmt for Re-Submit
				overrides.setManFmtForReSubmit(reasonCode.overridesSectionOverridesTblManFmtForReSubmitChk(String.valueOf(i + 1)).isSelected());
				//Manual
				overrides.setManual(reasonCode.overridesSectionOverridesTblManualChk(String.valueOf(i + 1)).isSelected());
				//Correspondence
				overrides.setCorrespondence(reasonCode.correspondenceInOverridesTable(String.valueOf(i + 1)).getText());
				//Correspondence Text
				overrides.setCorrespondenceText(reasonCode.overridesSectionOverridesTblCorrespondenceTxt(String.valueOf(i + 1)).getAttribute("data-corresp-txt"));
				//Outside Agency
				overrides.setOutsideAgency(reasonCode.overridesSectionOverridesTblOutsiteAgencyTxt(String.valueOf(i + 1)).getText().toUpperCase());
				//Outside Agency Pre Corresp
				overrides.setOutsideAgencyPreCorresp(reasonCode.overridesSectionOverridesTblOutsiteAgencyPreCorrespTxt(String.valueOf(i + 1)).getText().toUpperCase() + "-PRE");
				//Final Action
				if (reasonCode.overridesSectionOverridesTblFinalActionTxt(String.valueOf(i + 1)).getText().equals("Next Payor")) {
					overrides.setFinalAction(reasonCode.overridesSectionOverridesTblFinalActionTxt(String.valueOf(i + 1)).getText().replaceAll(" ", "").trim());
				} else {
					overrides.setFinalAction(reasonCode.overridesSectionOverridesTblFinalActionTxt(String.valueOf(i + 1)).getText());
				}
				//Authorized By
				overrides.setAuthorizedBy(reasonCode.overridesSectionOverridesTblAuthorizedByTxt(String.valueOf(i + 1)).getText());

				overridesList.add(overrides);
			}
		}catch (Exception e){
			logger.info("       No record found in Overrides table.");
		}

		return overridesList;
	}
	private List<Overrides> getValuesInReasonCodeOverridesForSpecialCharacters(int size){

		List<Overrides> overridesList  = new ArrayList<>();
		try {
			for (int i = 1; i <= size; i++) {
				Overrides overrides = new Overrides();
				//Override ID
				overrides.setPayorId(reasonCode.overridesSectionAddRecordPopupPayorIdInput().getAttribute("title").split(" ")[1].trim());
				overrides.setPayorName(StringUtils.removeEnd(reasonCode.overridesSectionAddRecordPopupPayorNameInput().getAttribute("title").split(" ")[3].trim(), "Client:"));
				overrides.setClientId(reasonCode.overridesSectionAddRecordPopupClientIdInput().getAttribute("title").split(" ")[4].trim());
				overrides.setClientName(reasonCode.overridesSectionAddRecordPopupClientNameInput().getAttribute("title").split("[|]")[2].trim());
				//Proc Code
				overrides.setProcCode(reasonCode.overridesSectionAddRecordPopupProcCodeInput().getAttribute("title"));
				//Man Fmt for Re-Submit
				overrides.setManFmtForReSubmit(reasonCode.overridesSectionAddRecordPopupManFmtForReSubmitChk().isSelected());
				//Manual
				overrides.setManual(reasonCode.overridesSectionAddRecordPopupManualChk().isSelected());
				//Correspondence
				overrides.setCorrespondence(reasonCode.overridesSectionAddRecordPopupCorrespondenceDropdown().getText());
				//Correspondence Text
				overrides.setCorrespondenceText(reasonCode.overridesSectionAddRecordPopupCorrespondenceTxt().getAttribute("data-corresp-txt"));
				//Outside Agency
				overrides.setOutsideAgency(reasonCode.overridesSectionAddRecordPopupOutsideAgencyDropdown().getText().toUpperCase());
				//Outside Agency Pre Corresp
				overrides.setOutsideAgencyPreCorresp(reasonCode.overridesSectionAddRecordPopupOutsideAgencyPreCorrespDropdown().getText().toUpperCase() + "-PRE");
				//Final Action
				if (reasonCode.overridesSectionAddRecordPopupFinalActionDdl().getText().equals("Next Payor")) {
					overrides.setFinalAction(reasonCode.overridesSectionAddRecordPopupFinalActionDdl().getText().replaceAll(" ", "").trim());
				} else {
					overrides.setFinalAction(reasonCode.overridesSectionAddRecordPopupFinalActionDdl().getText());
				}
				//Authorized By
				overrides.setAuthorizedBy(reasonCode.overridesSectionAddRecordPopupAuthorizedByTxt().getText());

				overridesList.add(overrides);
						}
		}catch (Exception e){
			logger.info("       No record found in Overrides table.");
		}

		return overridesList;
	}
	private List<CrossReference> getValuesInReasonCodeCrossReference(int size) {
		List<CrossReference> crossReferenceList = new ArrayList<>();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		try {
			for (int i = 1; i <= size; i++) {
				CrossReference crossReference = new CrossReference();

				//Effective Date
				String effectiveDateStr = reasonCode.crossReferenceTblEffDtCol(String.valueOf(i + 1)).getAttribute("title");
				crossReference.setEffectiveDate(new java.sql.Date(df.parse(effectiveDateStr).getTime()));
				//Expiration Date
				String expirationDateStr = reasonCode.crossReferenceTblExpDtCol(String.valueOf(i + 1)).getAttribute("title");
				crossReference.setExpirationDate(new java.sql.Date(df.parse(expirationDateStr).getTime()));
				//Cross Reference Description
				crossReference.setCrossReferenceDescription(reasonCode.crossReferenceTblCrossRefDescrCol(String.valueOf(i + 1)).getAttribute("title"));

				crossReferenceList.add(crossReference);
			}
		}catch (Exception e){
			logger.info("       No record found in Cross Reference table.");
		}

		return crossReferenceList;
	}

	private void cleanUpErrorCode(List<String> errorCodeList, String dateStr) throws ParseException, XifinDataAccessException {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date effDt = new java.sql.Date(df.parse(dateStr).getTime());

		for (String errorCode: errorCodeList) {
			logger.info("       ========== Looking for Reason Code " + errorCode + " ==========");
			ErrCd errCd = null;
			try {
				 errCd = errorDao.getErrCdByAbbrevEffDt(errorCode, effDt);
			}catch (XifinDataAccessException e){
				logger.info("       No Error Code " + errorCode + " with Effective date " + dateStr + " found. No need to delete.");
				continue;
			}

			try {
				errorDao.deleteErrCdXrefByErrCd(errCd.getErrCd());
			}
			catch (Exception e){
				logger.info("       No Error Code " + errorCode + " with Effective date " + dateStr + " found. No need to delete.");
				continue;
			}

			try {
				errorDao.deleteErrCdOverrideByErrCd(errCd.getErrCd());
			}
			catch (Exception e){
				logger.info("       No Error Code " + errorCode + " with Effective date " + dateStr + " found. No need to delete.");
				continue;
			}

			switch (errCd.getAbbrev())
			{
				case "AUTOTESTXPR1024":
				case "AUTOTESTXPR1032":
				case "AUTOTESTXPR1020":
					errCd.setOutAgncyId(0);
					errCd.setCorrespTypId(0);
					errCd.setFldId(0);
					errCd.setFinalActnId(0);
					errCd.setFinalActnIdAbnSigned(0);
					errCd.setAdjCdIdAbnSigned(0);
					errCd.setIsManActn(false);
					errCd.setLtrTxt("");
					errCd.setProcCdSpcfc(0);
					errCd.setIsResubUsingManFmt(false);
					errCd.setSpecificPyrId(0);
					break;
				case "AUTOTESTXPR1030":
					errCd.setIsDeleted(false);
					break;
				case "AUTOTESTXPR1029":
				case "AUTOTESTXPR1031":
				case "AUTOTESTXPR1034":
					// DO nothing
					break;
				default:
					errCd.setResultCode(ErrorCodeMap.DELETED_RECORD);
			}
			errorDao.setErrCd(errCd);
		}
	}

	private String getAbnRelatedReason(int index){
		String abnRelatedReasonStr = "";

		if (index == 1){
			abnRelatedReasonStr = "Diagnosis";
		}else if (index == 2){
			abnRelatedReasonStr = "Frequency";
		}

		return abnRelatedReasonStr;
	}

	private String getAbnSignedFinalAction(int index){
		String abnSignedFinalActionStr = "";

		if (index == 7){
			abnSignedFinalActionStr = "Hold";
		}else if (index == 9){
			abnSignedFinalActionStr = "Write-Off";
		}else if (index == 11){
			abnSignedFinalActionStr = "Next Payor";
		}

		return abnSignedFinalActionStr;
	}
}
