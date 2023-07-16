package com.overall.filemaint.physicianLicense;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PhysicianLicense {
	protected Logger logger;
	private WebDriverWait wait;
	
	public PhysicianLicense(RemoteWebDriver driver) {
		wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" +driver );
	}
	
	/* Physician License Load page : (NPI/UPIN input ) */
	public WebElement lookupPhyLicIdSection() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fileMaintPhysicianLicenseForm']//div[contains(@class,'lookupPhyLicIdSection')]")));
	}

	public WebElement fileMaintPhyLicHeaderContent() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fileMaintPhysicianLicenseForm']//div[contains(@class,'fileMaintPhyLicHeaderContent')]")));
	}

	public WebElement physicianTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fileMaintPhysicianLicenseForm']//span[@class='platormPageTitle']")));
	}
	
	public WebElement auditBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("auditBtn")));
	}
		
	public WebElement headerHelpIcon(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_physician_license_header']")));
	}
	
	public WebElement physSearchIcon(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//form[@id='fileMaintPhysicianLicenseForm']//section//*[@data-help-id='document_code_id_search']/span")));
	}
	
	public WebElement npiIdLookupInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("npiIdLookup")));
	}
	
	public WebElement upinIdLookupInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("upinIdLookup")));
	}
	
	public WebElement physSectionHelpIcon(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_physician_license_load_npi_upin']")));
	}
	/* End Physician License Load page : (NPI/UPIN input ) */
	
	/* Header */	
	public WebElement headerPhysicianSearchIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fileMaintPhysicianLicenseForm']//span[@data-search-type='physician']")));
	}
	
	public WebElement headerNpiIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("npiIdHeader")));
	}
	
	public WebElement headerUpinIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("upinIdHeader")));
	}
	
	public WebElement headerDeleteLicienseConfigCheckbox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("phyIdDelChk")));
	}
	/* End Header */	
	
	/* Physician License Section */
	public WebElement phyLicSectionLastNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lastName")));
	}
	
	public WebElement phyLicSectionFirstNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
	}
	
	public WebElement phyLicSectionAddressOneInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("addressOne")));
	}
	
	public WebElement phyLicCountryDropDown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@class,'countryField')]")));
	}
		
	public WebElement phyLicSectionAddressTwoInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("addressTwo")));
	}
	
	public WebElement phyLicSectionCityInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("city")));
	}
	
	public WebElement phyLicSectionPostalCodeSearchIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@title,'Postal Code Search')]")));
	}
	
	public WebElement phyLicSectionPostalCodeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("zip")));
	}
	
	public WebElement phyLicSectionStateInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("state")));
	}
	
	public WebElement phyLicSectionOverideServiceFacilityLocationDropDown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_overrideSvcFacilityLocation' and contains(@class,'formfield')]")));
	}
	
	public WebElement phyLicSectionAddressText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("relevantFacAddrr")));
	}
	
	public WebElement phyLicSectionNpiText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("relevantFacNpi")));
	}
	
	public WebElement phyLicSectionTaxIdText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("relevantFacTaxID")));
	}
	
	public WebElement phyLicSectionSpecialtyDropDown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_specialty']")));
	}
	
	public WebElement phyLicSectionCredentialsDropDown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_credentials")));
	}
	
	public WebElement taxId() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("taxID")));
	}
	
	public WebElement phyLicSectionCredentialsText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_credentials']/a/span[@class='select2-chosen']")));
	}
	/* End Physician License Section */
	
	/* Taxonomy Group */
	public WebElement taxonomyGrpTaxonomyCodeSearchIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@title,'Taxonomy Search')]/span")));
	}
	
	public WebElement taxonomyGrpTaxonomyCodeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("taxonomyCode")));
	}
	
	public WebElement taxonomyGrpGoToTaxonomyCodeButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnGotoTaxonomyCode")));
	}
	
	public WebElement taxonomyGrpProviderTypeDescriptionText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prvTypDescr")));
	}
	
	public WebElement taxonomyGrpClassificationText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("classification")));
	}
	
	public WebElement taxonomyGrpSpecializationText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("specialization")));
	}
	/* End Taxonomy Group */
	
	/* State Licenses Section */
	public WebElement staLicSectionStaLicTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_stateLicenses")));
	}
	
	public WebElement staLicSectionStaLicTblStateColFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_stateLicenses']//input[@name='state']")));
	}
	
	public WebElement staLicSectionStaLicTblStateColDropDown(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_stateLicenses']//tr["+ row +"]/td[@aria-describedby='tbl_stateLicenses_state']")));
	}
	
	public WebElement staLicSectionStaLicTblStateColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_stateLicenses']//tr["+ row +"]/td[@aria-describedby='tbl_stateLicenses_state']")));
	}
	
	public WebElement staLicSectionStaLicTblLicenseColFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_stateLicenses']//input[@name='licenseId']")));
	}
	
	public WebElement staLicSectionStaLicTblLicenseColInput(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_stateLicenses']//tr["+ row +"]/td[@aria-describedby='tbl_stateLicenses_licenseId']/input")));
	}
	
	public WebElement staLicSectionStaLicTblLicenseColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_stateLicenses']//tr["+ row +"]/td[@aria-describedby='tbl_stateLicenses_licenseId']")));
	}
	
	public WebElement staLicSectionStaLicTblDeleteColCheckbox(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_stateLicenses']//tr["+ row +"]/td[@aria-describedby='tbl_stateLicenses_deleted']/input")));
	}
	
	public WebElement staLicSectionStaLicTblAddButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_stateLicenses_iladd']/button")));
	}
	
	public WebElement staLicSectionStaLicTblFirstIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='first_tbl_stateLicenses_pagernav']/span")));
	}
	
	public WebElement staLicSectionStaLicTblPrevIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prev_tbl_stateLicenses_pagernav']/span")));
	}
	
	public WebElement staLicSectionStaLicTblPageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_stateLicenses_pagernav_center']//input")));
	}
	
	public WebElement staLicSectionStaLicTblNextIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_tbl_stateLicenses_pagernav']/span")));
	}
	
	public WebElement staLicSectionStaLicTblLastIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='last_tbl_stateLicenses_pagernav']/span")));
	}
	
	public WebElement staLicSectionStaLicTblPageDropDown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_stateLicenses_pagernav_center']//select")));
	}
	
	public WebElement staLicSectionStaLicTblTotalRecord() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_stateLicenses_pagernav_right']/div")));
	}
	/* End State Licenses Section */
	

	/* Affiliated Licenses */
	public WebElement affLicSectionHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id ='p_physician_license_affiliated_licenses']")));
	}
	
	public WebElement affLicTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_affiliatedLicenses")));
	}
	
	public WebElement affLicTblPyrIdFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_affiliatedLicenses']//input[@id='gs_payorAbbrev']")));
	}
	
	public WebElement affLicTblPyrNameFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_affiliatedLicenses']//input[@id='gs_payorName']")));
	}
	
	public WebElement affLicTblLicenseTypIdFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_affiliatedLicenses']//input[@id='gs_licenseTypeId']")));
	}
	
	public WebElement affLicTblLicenseIdFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_affiliatedLicenses']//input[@id='gs_licenseId']")));
	}
	
	public WebElement affLicTblPyrIdColInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='tbl_affiliatedLicenses_payorAbbrev']/input")));
	}
	
	public WebElement affLicTblPyrIdColSearchBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_affiliatedLicenses']//td[@aria-describedby='tbl_affiliatedLicenses_payorAbbrev']/child::a")));
	}

	public WebElement affLicTblRowNumColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_affiliatedLicenses']//tr["+ row +"]/td[@aria-describedby='tbl_affiliatedLicenses_rn']")));
	}

	public WebElement affLicTblPyrIdColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_affiliatedLicenses']//tr["+ row +"]/td[@aria-describedby='tbl_affiliatedLicenses_payorAbbrev']")));
	}
	
	public WebElement affLicTblPyrNameColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_affiliatedLicenses']//tr["+ row +"]/td[@aria-describedby='tbl_affiliatedLicenses_payorName']")));
	}
	
	public WebElement affLicTblLicenseTypIdColDropdownList() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='tbl_affiliatedLicenses_licenseTypeId']/div")));
	}
	
	public WebElement affLicTblLicenseTypIdColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_affiliatedLicenses']//tr["+ row +"]/td[@aria-describedby='tbl_affiliatedLicenses_licenseTypeId']")));
	}
	
	public WebElement affLicTblLicenseIdColInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='tbl_affiliatedLicenses_licenseId']/input")));
	}
	
	public WebElement affLicTblLicenseIdColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_affiliatedLicenses']//tr["+ row +"]/td[@aria-describedby='tbl_affiliatedLicenses_licenseId']")));
	}
	
	public WebElement affLicTblDeletedColCheckbox(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_affiliatedLicenses']//tr[("+ row +")]/td[@aria-describedby='tbl_affiliatedLicenses_deleted']/input")));
	}
	
	public WebElement affLicTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//td[@id='tbl_affiliatedLicenses_iladd']/button")));
	}
	
	public WebElement affLicTblFirstPageBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//td[@id='first_tbl_affiliatedLicenses_pagernav']/span")));
	}
	
	public WebElement affLicTblPreviousPageBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//td[@id='prev_tbl_affiliatedLicenses_pagernav']/span")));
	}
	
	public WebElement affLicTblNextPageBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//td[@id='next_tbl_affiliatedLicenses_pagernav']/span")));
	}
	
	public WebElement affLicTblLastPageBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//td[@id='last_tbl_affiliatedLicenses_pagernav']/span")));
	}
	
	public WebElement affLicTblPageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_affiliatedLicenses_pagernav_center']//td[@dir='ltr']/input")));
	}
	
	public WebElement affLicTblTotalPagesDropdownList() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_affiliatedLicenses_pagernav_center']//td[@dir='ltr']/select")));
	}
	
	public WebElement affLicTblTotalResultText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_affiliatedLicenses_pagernav_right']/div")));
	}
	/* End Affiliated Licenses */
	
	/* Client Specific Affiliated Licenses */
	public WebElement clnSpeAffLicSectionHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id ='p_physician_license_client_specific_affiliated_licenses']")));
	}
	
	public WebElement clnSpeAffLicTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_clientSpecificAffiliatedLicenses")));
	}
	
	public WebElement clnSpeAffLicTblPyrIdFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gbox_tbl_clientSpecificAffiliatedLicenses']//input[@id='gs_payorAbbrev']")));
	}
	
	public WebElement clnSpeAffLicTblPyrNameFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gbox_tbl_clientSpecificAffiliatedLicenses']//input[@id='gs_payorName']")));
	}
	
	public WebElement clnSpeAffLicTblClnIdFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gbox_tbl_clientSpecificAffiliatedLicenses']//input[@id='gs_clientAbbrev']")));
	}
	
	public WebElement clnSpeAffLicTblLicenseIdFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gbox_tbl_clientSpecificAffiliatedLicenses']//input[@id='gs_licenseId']")));
	}
	
	public WebElement clnSpeAffLicTblPyrIdColInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='tbl_clientSpecificAffiliatedLicenses_payorAbbrev']/input")));
	}
	
	public WebElement clnSpeAffLicTblPyrIdColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientSpecificAffiliatedLicenses']//tr[("+ row +")]/td[@aria-describedby='tbl_clientSpecificAffiliatedLicenses_payorAbbrev']")));
	}
	
	public WebElement clnSpeAffLicTblPyrNameColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientSpecificAffiliatedLicenses']//tr[("+ row +")]/td[@aria-describedby='tbl_clientSpecificAffiliatedLicenses_payorName']")));
	}
	
	public WebElement clnSpeAffLicTblClnIdColInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='tbl_clientSpecificAffiliatedLicenses_clientAbbrev']/input")));
	}
	
	public WebElement clnSpeAffLicTblClnIdColSearchBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientSpecificAffiliatedLicenses']//td[@aria-describedby='tbl_clientSpecificAffiliatedLicenses_clientAbbrev']/child::a")));
	}
	
	public WebElement clnSpeAffLicTblClnIdColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientSpecificAffiliatedLicenses']//tr[("+ row +")]/td[@aria-describedby='tbl_clientSpecificAffiliatedLicenses_clientAbbrev']")));
	}
	
	public WebElement clnSpeAffLicTblLicenseIdColInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='tbl_clientSpecificAffiliatedLicenses_licenseId']/input")));
	}
	
	public WebElement clnSpeAffLicTblLicenseIdColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientSpecificAffiliatedLicenses']//tr[("+ row +")]/td[@aria-describedby='tbl_clientSpecificAffiliatedLicenses_licenseId']")));
	}
	
	public WebElement clnSpeAffLicTblDeletedColCheckbox(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientSpecificAffiliatedLicenses']//tr[("+ row +")]/td[@aria-describedby='tbl_clientSpecificAffiliatedLicenses_deleted']/input")));
	}
	
	public WebElement clnSpeAffLicTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//td[@id='tbl_clientSpecificAffiliatedLicenses_iladd']/button")));
	}
	
	public WebElement clnSpeAffLicTblFirstPageBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//td[@id='first_tbl_clientSpecificAffiliatedLicenses_pagernav']/span")));
	}
	
	public WebElement clnSpeAffLicTblPreviousPageBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//td[@id='prev_tbl_clientSpecificAffiliatedLicenses_pagernav']/span")));
	}
	
	public WebElement clnSpeAffLicTblNextPageBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//td[@id='next_tbl_clientSpecificAffiliatedLicenses_pagernav']/span")));
	}
	
	public WebElement clnSpeAffLicTblLastPageBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//td[@id='last_tbl_clientSpecificAffiliatedLicenses_pagernav']/span")));
	}
	
	public WebElement clnSpeAffLicTblPageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientSpecificAffiliatedLicenses_pagernav_center']//td[@dir='ltr']/input")));
	}
	
	public WebElement clnSpeAffLicTblTotalPagesDropdownList() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientSpecificAffiliatedLicenses_pagernav_center']//td[@dir='ltr']/select")));
	}
	
	public WebElement clnSpeAffLicTblTotalResultText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientSpecificAffiliatedLicenses_pagernav_right']/div")));
	}
	/* End Client Specific Affiliated Licenses*/
	
	/* OIG Exclusions */
	public WebElement oigExclusSectionHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id ='p_physician_license_oig_exclusions']")));
	}
	
	public WebElement oigExclusTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_oIGExclusions")));
	}
	
	public WebElement oigExclusTblEffectiveDtFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_oIGExclusions']//input[@id='gs_effDateStr']")));
	}
	
	public WebElement oigExclusTblExpirationDtFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_oIGExclusions']//input[@id='gs_expDateStr']")));
	}

	public WebElement oigExclusTblEffectiveDtColInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='tbl_oIGExclusions_effDateStr']/input")));
	}
	
	public WebElement oigExclusTblEffectiveDtColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_oIGExclusions']//tr[("+ row +")]/td[@aria-describedby='tbl_oIGExclusions_effDateStr']")));
	}
	
	public WebElement oigExclusTblExpirationDtColInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='tbl_oIGExclusions_expDateStr']/input")));
	}
	
	public WebElement oigExclusTblExpirationDtColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_oIGExclusions']//tr[("+ row +")]/td[@aria-describedby='tbl_oIGExclusions_expDateStr']")));
	}
	
	public WebElement oigExclusTblDeletedColCheckbox(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_oIGExclusions']//tr[("+ row +")]/td[@aria-describedby='tbl_oIGExclusions_deleted']/input")));
	}
	
	public WebElement oigExclusTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//td[@id='tbl_oIGExclusions_iladd']/button")));
	}
	
	public WebElement oigExclusTblFirstPageBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//td[@id='first_tbl_oIGExclusions_pagernav']/span")));
	}
	
	public WebElement oigExclusTblPreviousPageBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//td[@id='prev_tbl_oIGExclusions_pagernav']/span")));
	}
	
	public WebElement oigExclusTblNextPageBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//td[@id='next_tbl_oIGExclusions_pagernav']/span")));
	}
	
	public WebElement oigExclusTblLastPageBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//td[@id='last_tbl_oIGExclusions_pagernav']/span")));
	}
	
	public WebElement oigExclusTblPageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_oIGExclusions_pagernav_center']//td[@dir='ltr']/input")));
	}
	
	public WebElement oigExclusTblTotalPagesDropdownList() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_oIGExclusions_pagernav_center']//td[@dir='ltr']/select")));
	}
	
	public WebElement oigExclusTblTotalResultText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_oIGExclusions_pagernav_right']/div")));
	}
	/* End OIG Exclusions*/
	
	/* Footer */
	public WebElement footerHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
	
	public WebElement showClipboardIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@title,'Show Clipboard')]")));
	}
	
	public WebElement showKeyboardShortcutsIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@title,'Show Keyboard Shortcuts')]")));
	}
	
	public WebElement sectionSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sectionSearchField")));
	}
	
	public WebElement resetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}
	
	public WebElement saveAndClearBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}
	/* End footer*/
	
	
	/* Payor Group Exclusion*/
	public WebElement payorGrpExclusionTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_payorGroupExclusion")));
	}

	public WebElement payorGrpExclusionTblEffDtFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_payorGroupExclusion']//input[@id='gs_effDateStr']")));
	}
	
	public WebElement payorGrpExclusionTblExpDtFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_payorGroupExclusion']//input[@id='gs_expDateStr']")));
	}

	public WebElement payorGrpExclusionTblGroupFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_payorGroupExclusion']//input[@id='gs_payorGrpId']")));
	}

	public WebElement addNewPayorGroupExclusionbtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorGroupExclusion_iladd']/button")));
	}
	
	public WebElement effDateOnPayorGroupExclTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorGroupExclusion']//tr["+row+"]/td[@aria-describedby='tbl_payorGroupExclusion_effDateStr']")));
	}
	
	public WebElement effDateOnPayorGroupExclTblInput(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorGroupExclusion']//tr["+row+"]/td[@aria-describedby='tbl_payorGroupExclusion_effDateStr']/input")));
	}
		
	public WebElement expDateOnPayorGroupExclTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorGroupExclusion']//tr["+row+"]/td[@aria-describedby='tbl_payorGroupExclusion_expDateStr']")));
	}
		
	public WebElement expDateOnPayorGroupExclTblInput(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorGroupExclusion']//tr["+row+"]/td[@aria-describedby='tbl_payorGroupExclusion_expDateStr']/input")));
	}
	
	public WebElement pyrGrpIdOnPayorGroupExclTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorGroupExclusion']//tr["+row+"]/td[@aria-describedby='tbl_payorGroupExclusion_payorGrpId']//div")));
	}
	
	public WebElement pyrGrpIdOnPayorGroupExclTblText(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorGroupExclusion']//tr["+row+"]/td[@aria-describedby='tbl_payorGroupExclusion_payorGrpId']")));
	}
	
	public WebElement deleteChkboxOnPayorGroupExclTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorGroupExclusion']//tr["+row+"]/td[@aria-describedby='tbl_payorGroupExclusion_deleted']//input")));
	}
	
	public WebElement pyrGrpExclNavBar(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorGroupExclusion_pagernav_right']/div")));
	}
	
	public WebElement pyrGroupExclNavBtn(String btn){
		//first   ,   prev    , next   ,last
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='"+btn+"_tbl_payorGroupExclusion_pagernav']/span")));
	}
	
	public WebElement rowPerPayorGroupExclusionPageInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorGroupExclusion_pagernav_center']//input")));
	}
	
	public WebElement helpIconOnPayorGroupExclusionTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_physician_license_payor_group_exclusion']")));
	}
	
	/* Payor Exclusion*/
	public WebElement payorExclusionTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_payorExclusion")));
	}
	
	public WebElement payorExclusionTblEffDtFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_payorExclusion']//input[@id='gs_effDateStr']")));
	}

	public WebElement payorExclusionTblExpDtFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_payorExclusion']//input[@id='gs_expDateStr']")));
	}

	public WebElement payorExclusionTblPayorIdFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_payorExclusion']//input[@id='gs_payorAbbrev']")));
	}

	public WebElement payorExclusionTblPayorNameFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_payorExclusion']//input[@id='gs_payorName']")));
	}

	public WebElement addNewPayorExclusionbtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusion_iladd']/button")));
	}
	
	public WebElement effDateOnPayorExclTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusion']//tr["+row+"]/td[@aria-describedby='tbl_payorExclusion_effDateStr']")));
	}
		
	public WebElement effDateOnPayorExclTblInput(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusion']//tr["+row+"]/td[@aria-describedby='tbl_payorExclusion_effDateStr']/input")));
	}
	
	public WebElement expDateOnPayorExclTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusion']//tr["+row+"]/td[@aria-describedby='tbl_payorExclusion_expDateStr']")));
	}
		
	public WebElement expDateOnPayorExclTblInput(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusion']//tr["+row+"]/td[@aria-describedby='tbl_payorExclusion_expDateStr']/input")));
	}
	
	public WebElement pyrIdCellOnPayorExclTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusion']//tr["+row+"]/td[@aria-describedby='tbl_payorExclusion_payorAbbrev']")));
	}
	
	public WebElement pyrIdInputOnPayorExclTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusion']//tr["+row+"]/td[@aria-describedby='tbl_payorExclusion_payorAbbrev']//input")));
	}
	public WebElement pyrSearchIconOnPayorExclTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusion']//tr["+row+"]//a[@data-help-id='payor_id_search']")));
	}
	
	public WebElement deletePyrExclCkboxOnPayorExclTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusion']//tr["+row+"]//td[@aria-describedby='tbl_payorExclusion_deleted']//input")));
	}

	
	public WebElement pyrExclNavBar(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusion_pagernav_right']/div")));
	}
	
	public WebElement pyrExclNavBtn(String btn){
		//first   ,   prev    , next   ,last
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='"+btn+"_tbl_payorExclusion_pagernav']/span")));
	}
	
	public WebElement rowPerPayorExclusionPageInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_payorExclusion_pagernav_center']//input")));
	}
	
	public WebElement helpIconOnPayorExclusionTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_physician_license_payor_exclusion']")));
	}
	
	/*Physician Cross References*/
	public WebElement physicianCrossReferencesTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_physicianCrossReferences")));
	}
	
	public WebElement physicianCrossReferencesTblEffDtFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_physicianCrossReferences']//input[@id='gs_effDateStr']")));
	}

	public WebElement physicianCrossReferencesTblExpDtFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_physicianCrossReferences']//input[@id='gs_expDateStr']")));
	}

	public WebElement physicianCrossReferencesTblXrefDescFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-labelledby='gbox_tbl_physicianCrossReferences']//input[@id='gs_xrefDescr']")));
	}

	public WebElement addPhysicianCrossReferencesbtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_physicianCrossReferences_iladd']/button")));
	}
	
	public WebElement effDateOnCrossReferencesTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_physicianCrossReferences']//tr["+row+"]/td[@aria-describedby='tbl_physicianCrossReferences_effDateStr']")));
	}
	
	public WebElement effDateOnCrossReferencesTblInput(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_physicianCrossReferences']//tr["+row+"]/td[@aria-describedby='tbl_physicianCrossReferences_effDateStr']/input")));
	}
	
	public WebElement expDateOnCrossReferencesTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_physicianCrossReferences']//tr["+row+"]/td[@aria-describedby='tbl_physicianCrossReferences_expDateStr']")));
	}
	
	public WebElement expDateOnCrossReferencesTblInput(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_physicianCrossReferences']//tr["+row+"]/td[@aria-describedby='tbl_physicianCrossReferences_expDateStr']/input")));
	}
	
	public WebElement xrefOnCrossReferencesTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_physicianCrossReferences']//tr["+row+"]/td[@aria-describedby='tbl_physicianCrossReferences_xrefDescr']//input")));
	}
	
	public WebElement xrefTextOnCrossReferencesTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_physicianCrossReferences']//tr["+row+"]/td[@aria-describedby='tbl_physicianCrossReferences_xrefDescr']")));
	}
	
	public WebElement xrefSearchIconOnCrossReferencesTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_physicianCrossReferences']//tr["+row+"]/td[@aria-describedby='tbl_physicianCrossReferences_xrefDescr']//a[@data-help-id='xref_id_search']")));
	}
	
	public WebElement deleteChkboxOnCrossReferencesTbl(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_physicianCrossReferences']//tr["+row+"]/td[@aria-describedby='tbl_physicianCrossReferences_deleted']//input")));
	}
	
	public WebElement physicianCrossReferencesNavBar(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_physicianCrossReferences_pagernav_right']/div")));
	}
	
	public WebElement physicianCrossReferencesNavBtn(String btn){
		//first   ,   prev    , next   ,last
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='"+btn+"_tbl_physicianCrossReferences_pagernav']/span")));
	}
	
	public WebElement rowPerPhysicianCrossReferencesPageInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_physicianCrossReferences_pagernav_center']//input")));
	}
	
	public WebElement helpIconOnCrossReferencesTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_physician_license_physician_cross_references']")));
	}
	
	/* Internal Note*/
	public WebElement internalNote(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id	("internalNote")));
	}
	
	public WebElement helpIconOnInternalNoteTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_physician_license_internal_note']")));
	}
	
	public WebElement helpIconOnPhysLicSection(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_physician_license_physician_license']")));
	}
	
	public WebElement helpIconOnStateLicSection(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_physician_license_state_licenses']")));
	}
	
	public WebElement confirmDialog(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='confirmationDialog']//span")));
	}
	
	public WebElement resetButtonOnConfimDialog(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='ui-dialog-buttonset']//*[text()='Reset']")));
	}
	
	
	public WebElement warningMessage(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='confirmationDialog']//div[@class='unit lastUnit']/span")));
	}
	
}
