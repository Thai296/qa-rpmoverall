package com.overall.fileMaintenance.taxonomyCode;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TaxonomyCode {
	private WebDriverWait wait;
	protected Logger logger;
	
	public TaxonomyCode(RemoteWebDriver driver) {
		this.wait= new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*** Default Screen ***/
	public WebElement taxonomyCodePageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));
	}
	
	public WebElement taxonomyCodeDetailPageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@class,'step2')]//span[contains(@class,'platormPageTitle')]")));
	}
	
	public WebElement loadTaxonomyCodeTblTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//section[@data-section-name='section skipData']//span[contains(@class,'titleTextContainer')]")));
	}
	
	public WebElement taxonomyCodeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupTaxonomyCd")));
	}
	
	public WebElement taxonomyCodeSearchIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("taxonomy_search_btn")));
	}
	
	public WebElement loadTaxonomyCodeHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@class,'step1')]//div[contains(@class,'loadTaxonomyCodeGroup')]//a[contains(@class,'helpIcon')]")));
	}
	
	/*** Taxonomy Code Header Section ***/
	
	public WebElement taxonomyCodeDisplay() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("taxonomyCdDisplay")));
	}
	
	public WebElement effDateDisplay() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("effDtDisplay")));
	}
	
	public WebElement deactivationDateDisplay() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("deactivationDtDisplay")));
	}
	
	public WebElement taxonomyCodeHeaderHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@class,'step2')]//div[contains(@class,'headerDetailGroup ')]//a[contains(@class,'helpIcon')]")));
	}
	
	/*** Taxonomy Code Table ***/
	
	public WebElement taxonomyCodeTblTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']/div[3]/div/section/header/div[1]/span/span")));
	}
	
	public WebElement providerDescLbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("providerDescDisplay")));
	}
	
	public WebElement classificationLbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("classificationDisplay")));
	}
	
	public WebElement specializationLbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("specializationDisplay")));
	}
	
	public WebElement definitionLbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("definitionDisplay")));
	}
	
	public WebElement notesLbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("notesDisplay")));
	}
	
	public WebElement tanoxonomyCodeSectionHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']/div[3]/div/section/div/div[1]/a")));
	}
		
	/*** Footer Section ***/
	
	public WebElement footerHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
	
	public WebElement auditBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("auditBtn")));
	}
	
	public WebElement auditTitleTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_auditlogwait']/div[1]/span")));
	}
	
	public WebElement nextPagerInAuditTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_pager']/span")));
	}
	
	public WebElement totalRecordInAuditTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_right']/div")));
	}
	
	//Payor Group Exclusions
	public WebElement titlePayorGroupExclutionsTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_payorGroupExclusions']/div[1]/span[1]")));
	}
	
	public WebElement helpIconInPayorGroupExclutionsTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']/div[4]/div/div/section/div/div[1]/a")));
	}
	
	public WebElement groupSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_payorGroupExclName")));
	}
	
	public WebElement effDateSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_payorGroupExclEffDate")));
	}
	
	public WebElement expDateSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_payorGroupExclExpDate")));
	}
	
	public WebElement addIconInPayorGroupExclutions() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='add_tbl_payorGroupExclusions']/div")));
	}
	
	public WebElement editIconInPayorGroupExclutions() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='edit_tbl_payorGroupExclusions']/div")));
	}
	
	public WebElement nextIconInPayorGroupExclutions() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_tbl_payorGroupExclusions_pagernav']/span")));
	}
	
	public WebElement totalRecordInPayorGroupExclutions() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorGroupExclusions_pagernav_right']/div")));
	}
	
	public WebElement titleAddOrEditRecordInPayorGroupExclutions() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='edithdtbl_payorGroupExclusions']/span")));
	}
	
	public WebElement groupInputOfRecordInPayorGroupExclutions() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_payorGroupExclName']")));
	}
	
	public WebElement effDateInputOfRecordInPayorGroupExclutions() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorGroupExclEffDate")));
	}
	
	public WebElement expDateInputOfRecordInPayorGroupExclutions() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorGroupExclExpDate")));
	}
	
	public WebElement okBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sData")));
	}
	
	public WebElement cancelBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cData")));
	}
	
	public WebElement deleteCheck() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("deleted")));
	}
	
	public WebElement errorMessageOfRecordInPayorGroupExclutions() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='FormError']/td")));
	}
	
	//payor Exclusions
	public WebElement payorExclutionsTblTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_payorExclusions']/div[1]/span[1]")));
	}
	
	public WebElement payorExclutionsTblHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']/div[5]/div/div/section/div/div[1]/a")));
	}
	
	public WebElement payorIdSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_payorExclID")));
	}
	
	public WebElement nameSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_payorExclName")));
	}
	
	public WebElement payorExclusionsAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='add_tbl_payorExclusions']/div")));
	}
	
	public WebElement payorExclusionsEditBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='edit_tbl_payorExclusions']/div")));
	}
	
	public WebElement payorExclusionsDeleteIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jqgh_tbl_payorExclusions_deleted")));
	}
	
	public WebElement payorExclusionsFirstPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='first_tbl_payorExclusions_pagernav']/span")));
	}
	
	public WebElement payorExclusionsPreviousPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prev_tbl_payorExclusions_pagernav']/span")));
	}
	
	public WebElement payorExclusionsNextPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_tbl_payorExclusions_pagernav']/span")));
	}
	
	public WebElement payorExclusionsLastPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='last_tbl_payorExclusions_pagernav']/span")));
	}
	
	public WebElement payorExclusionsTotalRecords() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusions_pagernav_right']/div")));
	}
	
	public WebElement payorExclusionsPayorIDInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorExclID")));
	}
	
	public WebElement payorExclusionsPayorIdSearchIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_payorExclID']/td[1]/div/a/span")));
	}
	
	public WebElement payorExclusionsPayorNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorExclName")));
	}
	
	public WebElement payorExclusionsPayorEffDateInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorExclEffDate")));
	}
	
	public WebElement payorExclusionsPayorExpDateInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorExclExpDate")));
	}
	
	public WebElement payorExclusionsTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_payorExclusions")));
	}
	
	public WebElement payorExclusionsTblCelData(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusions']/tbody/tr["+row+"]/td["+col+"]")));
	}
	
	public WebElement payorExclusionsTblCelInput(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusions']/tbody/tr["+row+"]/td["+col+"]/input")));
	}
	
	public WebElement saveAndClearBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}
	
	public WebElement getAddRecordTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='edithdtbl_payorExclusions']/span")));
	}
	
	public WebElement payorGrpExclusionsTblCelData(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorGroupExclusions']/tbody/tr["+row+"]/td["+col+"]")));
	}	
	
	public WebElement payorGrpExclusionsTblCelInput(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorGroupExclusions']/tbody/tr["+row+"]/td["+col+"]/input")));
	}
	
	public WebElement errorMsgTextbox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='sectionServerMessages']/div/div[1]/ul")));
	}
	
	public WebElement payorExclusionsTblPayorIdFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_payorExclID")));
	}
	
	public WebElement payorExclusionsTblNameFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_payorExclName")));
	}
	
	public WebElement payorExclusionsTblEffDateFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_payorExclEffDate")));
	}
	
	public WebElement payorExclusionsTblExpDateFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_payorExclExpDate")));
	}
	
	public WebElement payorExclusionsTblPayorIdTxtByRowNum(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusions']/tbody/tr["+row+"]/td[2]")));
	}
	
	public WebElement payorExclusionsTblNameTxtByRowNum(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusions']/tbody/tr["+row+"]/td[3]")));
	}
	
	public WebElement payorExclusionsTblEffDateTxtByRowNum(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusions']/tbody/tr["+row+"]/td[4]")));
	}
	
	public WebElement payorExclusionsTblExpDateTxtByRowNum(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusions']/tbody/tr["+row+"]/td[5]")));
	}
	
	/*** Mehthods ***/ 
	public void enterToTaxonomyCodeInput(String value) {
		taxonomyCodeInput().clear();
		taxonomyCodeInput().sendKeys(value);
		taxonomyCodeInput().sendKeys(Keys.TAB);
		Log.info("        Enter to taxonomyCode: "+value);
	}
	
	public void enterPayorExclID(String value) {
		payorExclusionsPayorIDInput().clear();
		payorExclusionsPayorIDInput().sendKeys(value);
		payorExclusionsPayorIDInput().sendKeys(Keys.TAB);
		logger.info("        Enter Payor Excl Id:" + value);
	}
	
	public void enterPayorExclName(String value) {
		payorExclusionsPayorNameInput().clear();
		payorExclusionsPayorNameInput().sendKeys(value);
		payorExclusionsPayorNameInput().sendKeys(Keys.TAB);
		logger.info("        Enter Payor Excl Name:" + value);
	}
	
	public void enterPayorExclEffDate(String value) {
		payorExclusionsPayorEffDateInput().clear();
		payorExclusionsPayorEffDateInput().sendKeys(value);
		payorExclusionsPayorEffDateInput().sendKeys(Keys.TAB);
		logger.info("        Enter Payor Excl Eff Date: " + value);
	}
	
	public void enterPayorExclExpDate(String value) {
		payorExclusionsPayorExpDateInput().clear();
		payorExclusionsPayorExpDateInput().sendKeys(value);
		payorExclusionsPayorExpDateInput().sendKeys(Keys.TAB);
		logger.info("        Enter Payor Excl Exp Date: " + value);
	}
	public void enterPayorGrpExclEffDate(String value) {
		effDateInputOfRecordInPayorGroupExclutions().clear();
		effDateInputOfRecordInPayorGroupExclutions().sendKeys(value);
		effDateInputOfRecordInPayorGroupExclutions().sendKeys(Keys.TAB);
		logger.info("        Enter Payor Group Excl Eff Date: " + value);
	}
	
	public void enterPayorGrpExclExpDate(String value) {
		expDateInputOfRecordInPayorGroupExclutions().clear();
		expDateInputOfRecordInPayorGroupExclutions().sendKeys(value);
		expDateInputOfRecordInPayorGroupExclutions().sendKeys(Keys.TAB);
		logger.info("        Enter Payor Group Excl Exp Date: " + value);
	}
	
	
	public WebElement taxonomySearchHeader() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='taxonomySearchForm']/header/span")));
	}
	
	public WebElement taxonomyCdSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("taxonomyCd")));
	}
	
	public WebElement providerTypeDescSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("providerTypeDesc")));
	}
	
	public WebElement classificationSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("classification")));
	}
	
	public WebElement specializationSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("specialization")));
	}
	
	public WebElement definitionSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("definition")));
	}
	
	public WebElement notesSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("notes")));
	}
	
	public WebElement searchButtonOnSearchPopup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath((".//*[@id='taxonomyCodeSearch']/div[2]/button[1]"))));
	}
	
	public WebElement frsRowOnSearchResultPopup(int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath((".//*[@id='gview_taxonomycodesearchresultsTable']//tbody/tr[2]/td["+col+"]"))));
	}
	
	public WebElement frsRowOnSearchResultPopupCellHyperlink(int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath((".//*[@id='gview_taxonomycodesearchresultsTable']//tbody/tr[2]/td["+col+"]/a"))));
	}
	
	public WebElement excludedPayorGroupOnSearchPopup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id(("excludedPayorGroup"))));
	}
	
	public WebElement totalRecordTitleFooterOnSearchPopup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath((".//*[@id='pager_right']/div"))));
	}
	
	public WebElement taxonomySearchResultHeader() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_taxonomycodesearchresultsTable']/div[1]/span")));
	}
	
	public WebElement resetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}
	
	public WebElement errorMsgFieldValidation() {
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='messagefor_payorGroupExclEffDate']/div[2]")));
	}
		
	public WebElement getCloseMessageIconOfFieldValidation() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='messagefor_payorGroupExclEffDate']/div[1]/a")));
	}
	
	public WebElement select2DropInput() {
			return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='select2-drop']/div/input")));
	}
	
	public void enterFullDataToAllRequiredFieldsOnSearchPopup(String taxonomyCd,String providerTypeDesc,String classification,String specialization,String definition,String notesSearchInput) throws StaleElementReferenceException, InterruptedException {
		taxonomyCdSearchInput().sendKeys(taxonomyCd);
		taxonomyCdSearchInput().sendKeys(Keys.TAB);
		providerTypeDescSearchInput().sendKeys(providerTypeDesc);
		providerTypeDescSearchInput().sendKeys(Keys.TAB);
		classificationSearchInput().sendKeys(classification);
		classificationSearchInput().sendKeys(Keys.TAB);
		specializationSearchInput().sendKeys(specialization);
		specializationSearchInput().sendKeys(Keys.TAB);
		definitionSearchInput().sendKeys(definition);
		definitionSearchInput().sendKeys(Keys.TAB);
		notesSearchInput().sendKeys(notesSearchInput);
		notesSearchInput().sendKeys(Keys.TAB);
		searchButtonOnSearchPopup().click();
		logger.info("        Enter data to all fields with : "+taxonomyCd+" : "+providerTypeDesc+" : "+classification+" : "+specialization+" : "+definition+" : "+notesSearchInput);
	}
	
	public void enterTaxonomyCodeFieldOnSearchPopup(String value) {
		taxonomyCdSearchInput().clear();
		taxonomyCdSearchInput().sendKeys(value);
		taxonomyCdSearchInput().sendKeys(Keys.TAB);
		logger.info("        Enter "+value+" : to Taxonomy Code field.");
	}
	
	public void enterGroupInputInPayorGrExcl(String group) {
		groupInputOfRecordInPayorGroupExclutions().click();
		select2DropInput().sendKeys(group);
		select2DropInput().sendKeys(Keys.TAB);
		logger.info("        Select Group: " + group);
	} 
	
	public void pressAltR() {
		String selectAll = Keys.chord(Keys.ALT,"R");
		WebElement body = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
		body.sendKeys(selectAll);
		logger.info("        Press Ctr + R");
	}
	
	public void enterDefinitionDataOnSearchPopup(String value) {
		definitionSearchInput().clear();
		definitionSearchInput().sendKeys(value);
		definitionSearchInput().sendKeys(Keys.TAB);
		logger.info("        Enter data to Definition field on Search popup with value: " + value);
	}
	
	public void enterProviderTypDescDataOnSearchPopup(String value) {
		providerTypeDescSearchInput().clear();
		providerTypeDescSearchInput().sendKeys(value);
		providerTypeDescSearchInput().sendKeys(Keys.TAB);
		logger.info("        Enter data to Provider Type Description field on Search popup with value: " + value);
	}
	
	public WebElement payorGroupExclusionsTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_payorGroupExclusions")));
	}
	
	public WebElement sectionSearchFieldInput(String value) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sectionSearchField")));
}
}
