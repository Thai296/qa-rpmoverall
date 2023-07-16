package com.overall.fileMaintenance.fileMaintenanceTables;

import com.xifin.qa.page.BasePage;
import com.xifin.utils.SeleniumBaseTest;
import org.apache.commons.lang3.BooleanUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EligibilityConfiguration extends BasePage
{
	private static final String ID_SAVE_AND_CLEAR_BUTTON ="btnSaveAndClear";
	private static final String ID_ELIG_SVC_ABBREV ="eligSvcAbbrev";
	private static final String ID_LOOKUP_ELIG_SVC_ABBREV="lookupEligSvcAbbrev";
	private static final int TBL_PAYOR_SETUP_DELETE_CHECKBOX_COLUMN = 21;

	public EligibilityConfiguration(RemoteWebDriver driver, WebDriverWait wait, String methodName)
	{
		super(driver, wait, methodName);
	}

	public WebElement contentFrame() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("content")));
	}

	public WebElement platformiFrame() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("platformiframe")));
	}
	/*** Eligibility Configuration Load page***/
	public WebElement eligibilityConfigPageTitle () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"fileMaintEligibilityConfigForm\"]/div[1]/div[1]/div/div/div[2]/span")));
	}

	public WebElement eligSvcIDInputInLoadPage () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id(ID_LOOKUP_ELIG_SVC_ABBREV)));
	}

	public WebElement eligServiceIDSearchIconInLoadPage () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fileMaintEligibilityConfigForm']/div[1]/div[2]/section/div/div[2]/div/a/span")));
	}

	public WebElement eligConfigurationHelpIcon () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fileMaintEligibilityConfigForm']/div[1]/div[2]/section/div/div[1]/a")));
	}

	/*** Elibility Service Search results***/
	public WebElement serviceSearchResultTitlePage () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_eligibilitySearch']/div[1]/span")));
	}

	public WebElement eligibilityTbl () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_eligibilitySearch")));
	}

	public WebElement serviceSearchResultHelpIcon () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}

	public WebElement colRowInServiceSearchResult(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_eligibilitySearch']/tbody/tr["+row+"]/td["+col+"]")));
	}

	public WebElement colRowInServiceSearchResultHyberLink(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_eligibilitySearch']/tbody/tr["+row+"]/td["+col+"]/a")));
	}

	public WebElement loadingIcon () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='refresh_tbl_eligibilitySearch']/div/span")));
	}
	public WebElement nextPagerIconInServiceSearchResult () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_pager']/span")));
	}

	public WebElement lastPagerIconInServiceSearchResult () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='last_pager']/span")));
	}

	public WebElement totalRecordInServiceSearchResult () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_right']/div")));
	}

	public WebElement keepSearchOpenInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("keepSearchOpen")));
	}

	public WebElement closeBtnInServiceSearchResult () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='eligibilitySearchRow']/div[3]/button[2]")));
	}

	/*** Eligibility Configuration display detail page***/
	public WebElement eligibilityConfigPageTitleInDetailPage () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fileMaintEligibilityConfigForm']/div[1]/div[3]/div[1]/div/div[1]/div[2]/span")));
	}

	public WebElement runAuditBtn () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("auditbtn")));
	}

	public WebElement helpIconInHeaderSection() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-help-id='p_eligibility_configuration_header']")));
	}
	public WebElement eligSvcIDInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id(ID_ELIG_SVC_ABBREV)));
	}

	public WebElement searchBtnEligConfig() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='fileMaintEligibilityConfigForm']/div[1]/div[3]/div[1]/div/div[2]/div/div[1]/div[1]/div[1]/div/a")));
	}

	public WebElement nameInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("svcName")));
	}

	public WebElement loginIDInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("loginId")));
	}

	public WebElement changePasswordLink()
	{
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("changePasswdLabel")));
	}

	public WebElement passwordIDInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("passwd")));
	}

	public WebElement classNameInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("classNm")));
	}

	public WebElement exceptionAlertThreadholdInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("alrtThres")));
	}

	public WebElement serverDelayInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("serverDly")));
	}

	public WebElement serverTimeoutInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("serverTimeout")));
	}

	public WebElement pageHelpLinkIcon () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pageHelpLink']/div")));
	}

	public WebElement clipboardIcon () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fileMaintEligibilityConfigForm']/div[1]/div[3]/div[3]/div/div[1]/div[5]/div")));
	}

	public WebElement keyBoardShortcutsIcon () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fileMaintEligibilityConfigForm']/div[1]/div[3]/div[3]/div/div[1]/div[5]/div")));
	}

	public WebElement sectionSearchFieldIcon () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sectionSearchField")));
	}

	public WebElement resetBtn () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}

	public WebElement saveAndClearBtn () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id(ID_SAVE_AND_CLEAR_BUTTON)));
	}

	public WebElement getCellInEligibilityResponseTranslationTbl(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_eligibilityResponseTranslation']/tbody/tr["+row+"]/td["+col+"]")));
	}

	public WebElement getCellCheckInEligibilityResponseTranslationTbl(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_eligibilityResponseTranslation']/tbody/tr["+row+"]/td["+col+"]/input")));
	}

	//Payor Setup
	public WebElement payorSetupTblTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']//span[contains(@class, 'titleText') and contains(text(), 'Payor Setup')]")));
	}

	public WebElement payorSetupTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add_tbl_payorSetup")));
	}

	public WebElement payorSetupTblEditBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit_tbl_payorSetup")));
	}

	public WebElement payorSetupTblPageNumberInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorSetup_pagernav_center']//input")));
	}

	public WebElement payorSetupTblSelectBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorSetup_pagernav_center']//select")));
	}

	public WebElement payorSetupTblFirstPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='first_tbl_payorSetup_pagernav']/span")));
	}

	public WebElement payorSetupTblPrevPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prev_tbl_payorSetup_pagernav']/span")));
	}

	public WebElement payorSetupTblNextPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_tbl_payorSetup_pagernav']/span")));
	}

	public WebElement payorSetupTblLastPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='last_tbl_payorSetup_pagernav']/span")));
	}

	public WebElement payorSetupTblTotalRecord() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorSetup_pagernav_right']/div")));
	}

	public WebElement payorSetupTblDeleteIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jqgh_tbl_payorSetup_deleted")));
	}

	public WebElement payorSetupTblGview() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gview_tbl_payorSetup")));
	}

	public WebElement payorSetupTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_payorSetup")));
	}

	public WebElement payorSetupTblCelData(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorSetup']/tbody/tr["+row+"]/td["+col+"]")));
	}

	public WebElement payorIdFilterInputInPayorSetupTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_payorSetup']//input[@id = 'gs_pyrAbbrv']")));
	}
	public WebElement helpIconInPayorSetup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-help-id='p_eligibility_configuration_payor_setup']")));
	}

	//Eligibility Response Translation table
	public WebElement gviewTblEliResponseTranslationTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gview_tbl_eligibilityResponseTranslation")));
	}

	public WebElement tblEligibilityResponseTranslationTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_eligibilityResponseTranslation")));
	}

	public WebElement eligibilityResponseTranslationTblCelData(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_eligibilityResponseTranslation']/tbody/tr["+row+"]/td["+col+"]")));
	}

	public WebElement titleTxtEliResponseTranslationTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']/div[3]/div/div/section/header/div[1]/span/span")));
	}

	public WebElement addIconEliResponseTranslationTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add_tbl_eligibilityResponseTranslation")));
	}
	public WebElement editIconEliResponseTranslationTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit_tbl_eligibilityResponseTranslation")));
	}
	public WebElement deleteIconEliResponseTranslationTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jqgh_tbl_eligibilityResponseTranslation_deleted")));
	}

	public WebElement currentPageInputEliResponseTranslationTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_eligibilityResponseTranslation_pagernav_center']//input")));
	}

	public WebElement firstPageIconEliResponseTranslationTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='first_tbl_eligibilityResponseTranslation_pagernav']/span")));
	}
	public WebElement prevPageIconEliResponseTranslationTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prev_tbl_eligibilityResponseTranslation_pagernav']/span")));
	}

	public WebElement nextPageIconEliResponseTranslationTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_tbl_eligibilityResponseTranslation_pagernav']/span")));
	}

	public WebElement lastPageIconEliResponseTranslationTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='last_tbl_eligibilityResponseTranslation_pagernav']/span")));
	}

	public WebElement selectBoxEliResponseTranslationTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_eligibilityResponseTranslation_pagernav_center']//select")));
	}

	public WebElement totalRecordsEliResponseTranslationTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_eligibilityResponseTranslation_pagernav_right']/div")));
	}

	public WebElement payorIdSearchBoxEliResponseTranslationTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_eligibilityResponseTranslation']//input[@id = 'gs_pyrAbbrv']")));
	}

	public WebElement helpIconInEliResponseTranslation() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class = 'helpIcon inlineIcon' and contains(@title, 'Eligibility Response')]")));
	}

	public void setEligSvcIDInLoadPage(String value) throws Exception {
		eligSvcIDInputInLoadPage().clear();
		eligSvcIDInputInLoadPage().sendKeys(value);
		eligSvcIDInputInLoadPage().sendKeys(Keys.TAB);
		Thread.sleep(3000);
		logger.info("Entered Eligibility Service ID: " + value);
	}

	// general add-edit records popup
	public WebElement editRecordTitleInEliResponseTranslationTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='edithdtbl_eligibilityResponseTranslation']/span")));
	}

	public WebElement payorIdInputInPopup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pyrAbbrv")));
		//return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#TblGrid_tbl_payorSetup #pyrAbbrv')[0]");

	}


	public WebElement payorIdSearchIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_pyrAbbrv']/td[1]/div/a/span")));
	}

	public WebElement payorNameTxtBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pyrName")));
	}
	public WebElement oKBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sData")));
	}
	public WebElement cancelBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cData")));
	}

	public WebElement deleteCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("deleted")));
	}

	// add-edit record popup on Payor Setup table
	public WebElement outPyrIdTxtBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("outPyrId")));
	}

	public WebElement activeCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("preferred")));
	}

	public WebElement daysToCheckEligInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pyrDaysToChkElig")));
	}
	public WebElement subServiceTxtBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("eligSubSvcAbbrv")));
	}
	public WebElement subServiceSearchIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_eligSubSvcAbbrv']/td[1]/div/a/span")));
	}
	public WebElement translationEnableCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("translationEnabled")));
	}

	public WebElement patientNameCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("updatePtNm")));
	}

	public WebElement patientGenderCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("updatePtGender")));
	}

	public WebElement patientAddressCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("updatePtAddr")));
	}
	public WebElement insuredNameCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("updateInsNm")));
	}
	public WebElement insuredGengerCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("updateInsGender")));
	}
	public WebElement insuredAddressCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("updateInsAddr")));
	}

	public WebElement relshpCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("updateInsRelshp")));
	}
	public WebElement SubscrIDCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("updateInsSubsId")));
	}

	//Add-Edit record popup on Eligibility Response Translation table
	public WebElement newPayorIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("newPyrAbbrv")));
	}
	public WebElement newPayorIdSearchIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_newPyrAbbrv']/td[1]/div/a/span")));
	}

	public WebElement newPayorNameTxtBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("newPyrName")));
	}
	public WebElement responseTxtBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("resp")));
	}
	public WebElement prioDropDown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_prio']/a")));
	}
	public WebElement checkEligCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("checkElig")));
	}


	public void setPayorIdInPopup(String pyrId) {
		payorIdInputInPopup().clear();
		payorIdInputInPopup().sendKeys(pyrId);
		payorIdInputInPopup().sendKeys(Keys.TAB);
		logger.info("        Entered Payor ID: " + pyrId);
	}


	/*** Payor Search ***/
	//Payor Info
	public WebElement payorIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorAbbrev")));
	}
	public WebElement payorNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorName")));
	}
	public WebElement payorGroupNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorGroupName")));
	}
	public WebElement clearingHouseIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clearingHouseId")));
	}
	public WebElement includeSuspendedPayors1Input() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("includeSuspendedPayors1")));
	}
	//Cross-Reference Info
	public WebElement xrefTypeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("xrefType")));
	}
	public WebElement xrefMemberInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_xrefMemberDroplist']/a/span[1]")));
	}
	public WebElement dispXrefData1Input() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dispXrefData1")));
	}
	//Address Info
	public WebElement addressInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("address")));
	}
	public WebElement cityInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("city")));
	}
	public WebElement stateDroplistInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("stateDroplist")));
	}
	public WebElement zipInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("zip")));
	}
	public WebElement countryDroplistInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("countryDroplist")));
	}
	public WebElement phoneInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("phone")));
	}
	public WebElement closeBtnInPayorSearch() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/section/div/div/div/button[3]")));
	}
	public WebElement resetBtnInPayorSearch() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/section/div/div/div/button[2]")));
	}
	public WebElement searchBtnInPayorSearch() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/section/div/div/div/button[1]")));
	}
	/*** Sub Service Search***/
	public WebElement subServiceSearchTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jqgh_tbl_eligibilitysubservicesearch_eligSubSvcName")));
	}
	public WebElement loadingIconInSubSerSearch() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='refresh_tbl_eligibilitysubservicesearch']/div/span")));
	}
	public WebElement totalRecordInSubSerSearch() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='eligibilitySubServiceSearch']/div[2]/div[5]/div[1]/table/tbody/tr/td[3]/div")));
	}

	public void setPayorNameField(String pyrName) {
		payorNameTxtBox().clear();
		payorNameTxtBox().sendKeys(pyrName);
		payorNameTxtBox().sendKeys(Keys.TAB);
		logger.info("Enter Payor Name");
	}

	public void setOutPyrIdField(String outPyrId) {
		outPyrIdTxtBox().clear();
		outPyrIdTxtBox().sendKeys(outPyrId);
		outPyrIdTxtBox().sendKeys(Keys.TAB);
		logger.info("Entered Outgoing Payor Id " + outPyrId);
	}

	public void setDaysToCheckElig(String days) {
		daysToCheckEligInput().clear();
		daysToCheckEligInput().sendKeys(days);
		daysToCheckEligInput().sendKeys(Keys.TAB);
		logger.info("Entered Days To Check Elig: " + days);
	}


	public void setSubServiceField(String subSvc) {
		subServiceTxtBox().clear();
		subServiceTxtBox().sendKeys(subSvc);
		subServiceTxtBox().sendKeys(Keys.TAB);
		logger.info("Enter Sub Service");
	}

	public void setNewPayorId(String newPyrId) {
		newPayorIdInput().clear();
		newPayorIdInput().sendKeys(newPyrId);
		newPayorIdInput().sendKeys(Keys.TAB);
		logger.info("Entered New Payor ID: " + newPyrId);
	}

	public void setNewPayorNameField(String newPyrName) {
		newPayorNameTxtBox().clear();
		newPayorNameTxtBox().sendKeys(newPyrName);
		newPayorNameTxtBox().sendKeys(Keys.TAB);
		logger.info("Entered New Payor Name " + newPyrName);
	}

	public void setResponse(String resp) {
		responseTxtBox().clear();
		responseTxtBox().sendKeys(resp);
		responseTxtBox().sendKeys(Keys.TAB);
		logger.info("Entered Response: " + resp);
	}

	public WebElement prioDropDownList() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prio")));
	}

	public void setPrio(String value) throws Exception {
		SeleniumBaseTest b = new SeleniumBaseTest();
		prioDropDown().click();
		b.selectItemByVal(prioDropDownList(), value);
		logger.info("Selected Prio: " + value + " from Dropdown List.");
	}

	public void setPayorIdFilterInPayorSetupTbl(String value) {
		payorIdFilterInputInPayorSetupTbl().click();
		payorIdFilterInputInPayorSetupTbl().clear();
		payorIdFilterInputInPayorSetupTbl().sendKeys(value);
		payorIdFilterInputInPayorSetupTbl().sendKeys(Keys.TAB);
		logger.info("Entered Payor ID " + value + " in Payor Setup grid Payor ID filter.");
	}

	public void setPayorIdSearchOnEliResponseTranslationTbl(String value) {
		payorIdSearchBoxEliResponseTranslationTbl().click();
		payorIdSearchBoxEliResponseTranslationTbl().clear();
		payorIdSearchBoxEliResponseTranslationTbl().sendKeys(value);
		payorIdSearchBoxEliResponseTranslationTbl().sendKeys(Keys.TAB);
		logger.info("Enter Payor Id to Search Box");
	}

	//Validation Follow-Up Action Code Configuration
	public WebElement titleValidationFollowUpActionCodeConfigTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']/div[4]/div/div/section/header/div[1]/span/span")));
	}

	public WebElement addBtnValidationFollowUpActionCodeConfigTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='add_tbl_eligibilityValidationFollowUp']/button")));
	}

	public WebElement followUpActionCodeDropDown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_eligFollowUpActnTypId']/a")));
	}

	public WebElement actionDropDown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_eligValidationActnTypId']/a")));
	}

	public WebElement curentPageValidationFollowUpActionCodeConfigTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_eligibilityValidationFollowUp_pagernav_center']/table/tbody/tr/td[4]/input")));
	}

	public WebElement firstPageIconValidationFollowUpActionCodeConfigTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='first_tbl_eligibilityValidationFollowUp_pagernav']/span")));
	}

	public WebElement prevPageIconValidationFollowUpActionCodeConfigTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prev_tbl_eligibilityValidationFollowUp_pagernav']/span")));
	}

	public WebElement nextPageIconValidationFollowUpActionCodeConfigTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_tbl_eligibilityValidationFollowUp_pagernav']/span")));
	}

	public WebElement lastPageIconValidationFollowUpActionCodeConfigTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='last_tbl_eligibilityValidationFollowUp_pagernav']/span")));
	}

	public WebElement recordsListboxValidationFollowUpActionCodeConfigTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_eligibilityValidationFollowUp_pagernav_center']/table/tbody/tr/td[8]/select")));
	}

	public WebElement deleteBtnValidationFollowUpActionCodeConfigTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jqgh_tbl_eligibilityValidationFollowUp_deleted")));
	}

	public WebElement totalRecordOnValidationFollowUpActionCodeConfigTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_eligibilityValidationFollowUp_pagernav_right']/div")));
	}

	public WebElement followUpActionCodeSearchBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_eligFollowUpActnTypId")));
	}

	public WebElement payorIdSearchBoxPayorSetupTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_payorSetup']//input[@id = 'gs_pyrAbbrv']")));
	}

	public WebElement select2DropInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='select2-drop']/div/input")));
	}

	public WebElement validationFollowUpActionCodeConfigTblCelData(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_eligibilityValidationFollowUp']/tbody/tr["+row+"]/td["+col+"]")));
	}

	public WebElement getCellInputInValidationFollowUpActionCodeConfigTbl(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_eligibilityValidationFollowUp']/tbody/tr["+row+"]/td["+col+"]/input")));
	}

	public void setFollowUpActionCode(String value) {
		followUpActionCodeDropDown().click();
		select2DropInput().sendKeys(value);
		select2DropInput().sendKeys(Keys.TAB);
		logger.info("Selected Follow-Up Action ID " + value);
	}

	public void setAction(String value) {
		actionDropDown().click();
		select2DropInput().sendKeys(value);
		select2DropInput().sendKeys(Keys.TAB);
		logger.info("Selected Action " + value);
	}

	/*** Validation Reject Reason Code Override Configuration ***/

	public WebElement valRejectResonTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']/div[4]/div/div/section/header/div[1]/span/span")));
	}

	public WebElement rejectReasonInput () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_eligRejectRsnTypId")));
	}
	public WebElement actionInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_eligValidationActnTypId")));
	}

	public WebElement deleteInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_deleted")));
	}

	public WebElement valRejectReasonTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_eligibilityValidationRejectReason")));
	}

	public WebElement addBtnOfValRejectReasonTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add_tbl_eligibilityValidationRejectReason")));
	}

	public WebElement totalRecordOfValRejectReasonTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_eligibilityValidationRejectReason_pagernav_right']/div")));
	}

	public WebElement addRecordTitleOfValRejectReasonTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='edithdtbl_eligibilityValidationRejectReason']/span")));
	}

	public WebElement rejectReasonCodeInputOfValRejectReasonTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_eligRejectRsnTypId']/a/span[1]")));
	}

	public WebElement msgReturnedText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='sectionServerMessages']/div/div[2]/ul")));
	}

	public WebElement helpIconInValidationFollowUpAction() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-help-id='p_eligibility_configuration_validation_follow_up_action_code_configuration']")));
	}

	public WebElement helpIconInValidationRejectReasonCode() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[data-help-id='p_eligibility_configuration_validation_reject_reason_code_override_configuration")));
	}

	public void enterToRejectReasonInput(String value) {
		rejectReasonInput().clear();
		rejectReasonInput().sendKeys(value);
		rejectReasonInput().sendKeys(Keys.TAB);
		logger.info("        Enter reject Reason Input: "+ value);
	}

	public void enterToActionInput(String value) {
		actionInput().clear();
		actionInput().sendKeys(value);
		actionInput().sendKeys(Keys.TAB);
		logger.info("        Enter action Input: "+ value);
	}

	public WebElement auditDetailErrMsg() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("errorMsg")));
	}


	public void enterEligibilityServiceInfos(String loginId, String className, String name, String password, String exceptAlert, String delay, String timeout) throws Exception {
		loginIDInput().clear();
		loginIDInput().sendKeys(loginId);
		loginIDInput().sendKeys(Keys.TAB);

		classNameInput().clear();
		classNameInput().sendKeys(className);
		classNameInput().sendKeys(Keys.TAB);

		nameInput().clear();
		nameInput().sendKeys(name);
		nameInput().sendKeys(Keys.TAB);

		changePasswordLink().click();
		passwordIDInput().clear();
		passwordIDInput().sendKeys(password);
		passwordIDInput().sendKeys(Keys.TAB);

		exceptionAlertThreadholdInput().clear();
		Thread.sleep(2000);
		exceptionAlertThreadholdInput().sendKeys(exceptAlert);
		exceptionAlertThreadholdInput().sendKeys(Keys.TAB);

		serverDelayInput().clear();
		Thread.sleep(2000);
		serverDelayInput().sendKeys(delay);
		serverDelayInput().sendKeys(Keys.TAB);

		serverTimeoutInput().clear();
		Thread.sleep(2000);
		serverTimeoutInput().sendKeys(timeout);
		serverTimeoutInput().sendKeys(Keys.TAB);

		logger.info("Entered Eligibility Configuration Information with: Login ID = " + loginId + ": Classname = " + className + ": Name = " + name + ": Password = " + password + ": Exception Alert Threshold = " + exceptAlert + ": Server Delay: " + delay + ": Server Timout = " + timeout);
	}

	public void enterToPyrIdInPayorSearch(String value) {
		payorIdInput().clear();
		payorIdInput().sendKeys(value);
		payorIdInput().sendKeys(Keys.TAB);
		logger.info("        Enter Payor Id Input: "+ value);
	}

	public void setDataInValidationRejectReasonCodeOverrideConfig(String reJectCd, String action)
	{
		scrollIntoView(addNewEligibilityValidationRejectReasonBtn());
		addNewEligibilityValidationRejectReasonBtn().click();

		rejectReasonSectionrejectReasonCdDropdown().click();
		inputFieldsOnDropdown().sendKeys(reJectCd);
		inputFieldsOnDropdown().sendKeys(Keys.TAB);

		rejectReasonSectionActionDropdown().click();
		inputFieldsOnDropdown().sendKeys(action);
		inputFieldsOnDropdown().sendKeys(Keys.TAB);

		oKBtn().click();
		logger.info("        Selected Reject Reason Code "+ reJectCd +" and Action "+action +" in Validation Reject Reason Code Override grid.");
	}

	public WebElement  rejectReasonSectionrejectReasonCdDropdown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='s2id_eligRejectRsnTypId']/a[1]")));
	}

	public WebElement rejectReasonSectionActionDropdown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='s2id_eligValidationActnTypId']/a[1]/span[1]")));
	}

	public WebElement addNewEligibilityValidationRejectReasonBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='add_tbl_eligibilityValidationRejectReason']/button[1]/div[1]")));
	}

	public WebElement inputFieldsOnDropdown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='select2-drop']/div[1]/input[1]")));
	}

	public WebElement addNewEligibilityResponseTranslationBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='add_tbl_eligibilityResponseTranslation']/button[1]/div[1]")));
	}

	/*** Eligibility Sub Service Search Results ***/

	public WebElement eligibilitySubSvcSearchTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("eligibilitySubSvcSearch")));
	}

	public WebElement eligibilitySubSvcSearchCelData(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_eligibilitySubSvcSearch']/tbody/tr["+row+"]/td["+col+"]/a")));
	}

	public WebElement eligibilitySubSvcSearchTblTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_eligibilitySubSvcSearch']/div[1]/span")));
	}

	public WebElement rowOnPayorSetupTblPyorSetupSection(int row , int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorSetup']/tbody/tr["+row+"]/td["+col+"]")));
	}

	public WebElement editPayorSetupPopup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edithdtbl_payorSetup")));
	}

	public void uncheckAllCheckBokOnEditRecordPyrSetupSection(SeleniumBaseTest b)  throws Exception {
		if (b.isChecked(translationEnableCheckBox()) == true) {
			translationEnableCheckBox().click();
		}
		if (b.isChecked(patientNameCheckBox()) == true) {
			patientNameCheckBox().click();
		}
		if (b.isChecked(patientGenderCheckBox()) == true) {
			patientGenderCheckBox().click();
		}
		if (b.isChecked(insuredAddressCheckBox()) == true) {
			insuredAddressCheckBox().click();
		}
		logger.info("Uncheck all checkbox.");
	}


	public WebElement deleteCheckBoxValidationRejectReasonCodeTbl(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_eligibilityValidationRejectReason']/tbody/tr["+row+"]/td["+col+"]/input")));
	}

	public WebElement helpIconInServiceSearchResult() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}

	public void clickPayorSetupTableDeleteCheckBox(int row)
	{
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorSetup']/tbody/tr["+row+"]/td["+TBL_PAYOR_SETUP_DELETE_CHECKBOX_COLUMN+"]/input"))).click();
	}

	public WebElement titleTextInHelp() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1")));
	}

	public WebElement helpIconInLoadPage() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id=\"fileMaintEligibilityConfigForm\"]/div[1]/div[2]/section/div/div[1]/a")));
	}

	public WebElement helpIconInFooterSection() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}

	public WebElement auditDetailTblRowCol(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_auditlogwait']/tbody/tr["+row+"]/td["+col+"]")));
	}

	public WebElement totalRecordsInAuditDetail () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_right']/div")));
	}
	public WebElement auditDetailTable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gbox_tbl_auditlogwait")));
	}

	public WebElement errMsgInAddRecordPopup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FormError")));
	}

	public WebElement eligibilityResponseTranslationTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_eligibilityResponseTranslation")));
	}

	public WebElement eligibilityValidationFollowUpTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_eligibilityValidationFollowUp")));
	}

	public WebElement newPayorIdFilterInputInEligResponseTranslationTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_newPyrAbbrv")));
	}

	public WebElement followUpActionCodeFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_eligFollowUpActnTypId")));
	}

	public WebElement actionFilterInValidationFollowUp() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_eligValidationActnTypId")));
	}

	public WebElement rejectReasonCodeFilter () {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_eligRejectRsnTypId")));
	}

	private WebElement translationEnabledCheckboxInput()
	{
		return driver.findElement(By.xpath("//td[@data-field-id='translationEnabled']/input"));
	}

	public boolean translationEnabledCheckboxInputValue()
	{
		return BooleanUtils.toBoolean(translationEnabledCheckboxInput().getAttribute("value"));
	}

	public void clickTranslationEnabledCheckboxInput()
	{
		translationEnabledCheckboxInput().click();
	}

	public void addRecordInPayorSetupAllFields(String pyrId, String outPyrId, String days, String subService) {
		payorSetupTblAddBtn().click();
		setPayorIdInPopup(pyrId);
		setOutPyrIdField(outPyrId);
		setDaysToCheckElig(days);
		setSubServiceField(subService);
		oKBtn().click();

		logger.info("Added a new record in Payor Setup grid with Payor ID: " + pyrId + "; Outgoing Payor ID: " +  outPyrId + "; Days to Check Elig: " + days + "; Sub Service: " + subService);
	}

	public void addRecordInPayorSetupRequiredFields(String pyrId, String days) throws Exception {
		payorSetupTblAddBtn().click();
		setPayorIdInPopup(pyrId);
		setDaysToCheckElig(days);
		oKBtn().click();
		Thread.sleep(2000);
		logger.info("Added a new record in Payor Setup grid with Payor ID: " + pyrId + "; Days to Check Elig: " + days);
	}

	public void addRecordInEligResponseTranslationgRequiredFields(String pyrId, String newPyrId) throws Exception {
		addIconEliResponseTranslationTbl().click();
		setPayorIdInPopup(pyrId);
		setNewPayorId(newPyrId);
		oKBtn().click();

		Thread.sleep(2000);

		logger.info("Added a new record in Eligibility Response Tranlsation grid with Payor ID: " + pyrId + "; New Payor ID: " + newPyrId);
	}

	public void addRecordInValidationFollowUpActionCodeConfig(String followUpActionCode,String action) throws Exception {
		scrollIntoView(addBtnValidationFollowUpActionCodeConfigTbl());
		addBtnValidationFollowUpActionCodeConfigTbl().click();
		setFollowUpActionCode(followUpActionCode);
		setAction(action);
		oKBtn().click();
		Thread.sleep(2000);
		logger.info("Selected Follow-up Action Code: "+ followUpActionCode +"; Action: "+action + " in Validation Follow-Up Action Code Configuration grid.");
	}

	public void clearInputValue(WebElement element) {
		element.click();
		element.clear();
		element.sendKeys(Keys.TAB);
		logger.info("Cleared input value of element: " + element);
	}

}