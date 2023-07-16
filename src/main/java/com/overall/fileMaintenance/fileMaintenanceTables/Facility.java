package com.overall.fileMaintenance.fileMaintenanceTables;

//this is only used in testRPM_784 by Production Deployment Smoke Tests

import com.xifin.utils.SeleniumBaseTest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class Facility extends SeleniumBaseTest{
	private static final String ID_MAIN_SECTIONS = "mainSections";

	private final RemoteWebDriver driver;
	protected final Logger logger;
	private final WebDriverWait wait;

	public Facility(RemoteWebDriver driver, WebDriverWait wait){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
		this.wait=wait;
	}

	/*Facility Form*/
	public WebElement facilityTile(){
		return driver.findElement(By.xpath(".//span[@class='platormPageTitle']"));
	}
	
	public WebElement runAuditBtn(){
		return driver.findElement(By.id("auditBtn"));
	}
		
	public WebElement auditDetailTbl(){
		return driver.findElement(By.xpath(".//*[@id='tbl_auditlogwait']"));
	}
	
	public WebElement helpIconAtHead(){
		return driver.findElement(By.xpath(".//div[@class='sectionHelpContainer helpPage']/a"));
	}
	
	public WebElement searchFacIdIcon(){
		return driver.findElement(By.xpath(".//a[@title='Facility Search']/span"));
	}
	
	public WebElement facIdInput(){
		return wait.until(ExpectedConditions.elementToBeClickable(By.id("lookupFacilityInfo")));
	}
	
	public WebElement helpIconInFacSection(){
		return driver.findElement(By.xpath(".//div[@class='groupContent sectionFacility']/div[@class='sectionHelpContainer']/a"));
	}
	
	/*Facility Detail*/
	public WebElement facId(){
		return driver.findElement(By.id("facAbbrevDisplay"));		
	}
	
	public WebElement facAbbrev(){		
		return driver.findElement(By.id("facAbbrev"));
	}
	
	public WebElement primFacIdDropdownList(){//Hard id for selection use select2 method: has s2id_ (It's not dynamic id)
		return driver.findElement(By.id("s2id_primFacId"));
	}
	
	public WebElement billingFacIdDropdownList(){
		return driver.findElement(By.id("s2id_billingFacId"));
	}
	
	public WebElement websiteInput(){
		return driver.findElement(By.id("website"));
	}
	
	public WebElement facNameInput(){
		return driver.findElement(By.id("facName"));
	}

	public String facSpecialtyType(){
		return driver.findElement(By.xpath(".//*[@id='s2id_facSpecialtyType']/a")).getText();
	}
	
	public String facNameText()  {		
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('#facName')[0]).val()");	
	}

	public WebElement facTypeDropdownList(){
		return driver.findElement(By.id("s2id_facType"));
	}
	public WebElement facTypeDropdownOptions(){
		return driver.findElement(By.id("facType"));
	}
	
	public WebElement whollyOwnedSubsidiaryChkBox(){
		return driver.findElement(By.id("subsidChkbx"));
	}
	
	public WebElement DelFacDemographicsRecordChkBox(){
		return driver.findElement(By.id("delFacCB"));
	}
	
	public WebElement titleSectionNameTitle(String sectionName){
		return driver.findElement(By.xpath(".//span[@class='titleTextContainer']/span[@class='titleText'][contains(text(),'"+sectionName+"')]"));
	}	
	
	public WebElement helpIconAtSection(String sectionName){
		return driver.findElement(By.xpath(".//span[@class='titleText'][contains(text(),'"+sectionName+"')]/ancestor::section[@class='groupContainer ']//div[@class='sectionHelpContainer']/a"));
	}
	
	public WebElement postalCodeSearchIconBySection(String sectionName){
		return driver.findElement(By.xpath(".//span[@class='titleText'][contains(text(),'"+sectionName+"')]/ancestor::section[@class='groupContainer ']//a[@title='Zip Code Search']/span"));
	}
	
	//Facility Additional Information 
	public WebElement serviceTypesChkBox(String srvTypName){
		return driver.findElement(By.xpath(".//span[contains(text(),'"+srvTypName+"')]/parent::div/input[@name='svcTyp']"));
	}
	public WebElement facilitySpecialtyTypeDropDown(){
		return driver.findElement(By.id("facSpecialtyType"));
	}
	public boolean isServiceTypeChkBoxChecked(int index){
		//return driver.findElement(By.xpath(".//span[contains(text(),'"+srvTypName+"')]/parent::div/input[@name='svcTyp'][@checked='checked']"));
		//return driver.findElement(By.xpath(".//span[contains(text(),'"+srvTypName+"')]/parent::div/input[@name='svcTyp'][${i}]"));
		return (boolean) ((JavascriptExecutor) driver).executeScript("return $($('[name=\"svcTyp\"]')[" + index + "]).is(':checked')");
	}
		
	public WebElement licenseIdInput(String licenseTypLbl){
		return driver.findElement(By.xpath(".//*[@id='licenseTable']//span[text()='"+licenseTypLbl+"']/ancestor::div[@class='line licenseInfo']//input[@name='facilityLicenseId'][@type='text']"));
	}
	
	public WebElement orgNPISearchIcon(){
		return driver.findElement(By.xpath(".//*[@id='licenseTable']//span[text()='Organizational NPI']/ancestor::div[@class='line licenseInfo']//a[@title='NPI Search']/span"));
	}
	
	public WebElement orgNPIGlobalSearchIcon(){
		return driver.findElement(By.xpath(".//*[@id='licenseTable']//span[text()='Organizational NPI']/ancestor::div[@class='line licenseInfo']//a[@title='NPI Global Search']/span"));
	}
	
	public WebElement orgNPPESSearchIcon(){
		return driver.findElement(By.xpath(".//*[@id='licenseTable']//span[text()='Organizational NPI']/ancestor::div[@class='line licenseInfo']//a[@title='NPPES Search']/span"));
	}
	
	public WebElement facNPISearchIcon(){
		return driver.findElement(By.xpath(".//*[@id='licenseTable']//span[text()='Facility NPI']/ancestor::div[@class='line licenseInfo']//a[@title='NPI Search']"));
	}
	
	public WebElement facNPIGlobalSearchIcon(){
		return driver.findElement(By.xpath(".//*[@id='licenseTable']//span[text()='Facility NPI']/ancestor::div[@class='line licenseInfo']//a[@title='NPI Global Search']/span"));
	}
	
	public WebElement facNPPESSearchIcon(){
		return driver.findElement(By.xpath(".//*[@id='licenseTable']//span[text()='Facility NPI']/ancestor::div[@class='line licenseInfo']//a[@title='NPI Search']/span"));
	}
	
	public WebElement taxonomySearchIcon(){
		return driver.findElement(By.xpath(".//*[@id='licenseTable']//span[text()='Taxonomy Code']/ancestor::div[@class='line licenseInfo']//a[@title='Taxonomy Search']/span"));
	}
	
	public WebElement taxonomyCodeInput(){
		return driver.findElement(By.id("taxonomyLicenseInfo"));
	}
	
	public String taxIdText()  {		
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('[name=\"facilityLicenseId\"]')[0]).val()");	
	}
	
	public String organizationalNpiText()  {		
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('[name=\"facilityLicenseId\"]')[2]).val()");	
	}
	
	public String cliaText()  {		
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('[name=\"facilityLicenseId\"]')[1]).val()");	
	}
	
	public String facilityNpiText()  {		
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('[name=\"facilityLicenseId\"]')[3]).val()");	
	}
	
	public String taxonomyCodeText()  {		
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('[name=\"facilityLicenseId\"]')[4]).val()");	
	}
	
	public String mammoCertText()  {		
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('[name=\"facilityLicenseId\"]')[5]).val()");	
	}

	public WebElement facGroupFilter(){
		return driver.findElement(By.id("gs_facGrpId"));
	}
	
	public WebElement facGrpTbladdIcon(){
		return driver.findElement(By.xpath(".//*[@id='tbl_facilityGroup_iladd']/button"));
	}
	
	public WebElement facilityGroupTblTotalRecord(){
		return driver.findElement(By.xpath(".//*[@id='tbl_facilityGroup_pagernav_right']/div"));
	}
	public WebElement facilityGroupTblNextIcon(){
		return driver.findElement(By.xpath(".//*[@id='next_tbl_facilityGroup_pagernav']/span"));
	}
		
	public WebElement facGrpTblFacGrpColDropdownList(int row){//Row start by 1
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_facilityGroup_facGrpId']/div[contains(@id,'facGrpId')]"));
	}
	
	public WebElement facilityGroupTblFacilityGroupColText(int row){
		int rowNum = row + 2; 
		return driver.findElement(By.xpath("//tr[("+rowNum+")]/td[@aria-describedby='tbl_facilityGroup_facGrpId']"));
	}
	
	public WebElement facGrpTblDeleteChkBox(int row){
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_facilityGroup_deleted']/input"));
	}
	
	//Facility Address and Contact Information
	public WebElement facContact1Input(){
		return driver.findElement(By.id("mailContact1"));
	}
	
	public WebElement facAddr1Input(){
		return driver.findElement(By.id("mailAddr1"));
	}
	
	public WebElement facAddr2Input(){
		return driver.findElement(By.id("mailAddr2"));
	}
	
	public WebElement facCountryDropdown(){
		return driver.findElement(By.id("s2id_mailCountry]"));
	}
	
	public WebElement facCountryTxt(){
		return driver.findElement(By.xpath(".//*[@id='s2id_mailCountry']/a/span[1]"));
	}
	
	public WebElement facZipInput(){
		return driver.findElement(By.id("mailZip"));
	}
	
	public WebElement facPhone1Input(){
		return driver.findElement(By.id("mailPhone1"));
	}
	
	public WebElement facFax1Input(){
		return driver.findElement(By.id("mailFax1"));
	}
	
	public WebElement facContact2Input(){
		return driver.findElement(By.id("mailContact2"));
	}
	
	public WebElement facPhone2Input(){
		return driver.findElement(By.id("mailPhone2"));
	}
	
	public WebElement facFax2Input(){
		return driver.findElement(By.id("mailFax2"));
	}
	
	public WebElement facStateDropdown(){
		return driver.findElement(By.id("s2id_mailState"));
	}
	
	public WebElement facStateTxt(){
		return driver.findElement(By.xpath(".//*[@id='s2id_mailState']/a/span[1]"));
	}
	
	public WebElement facCityInput(){
		return driver.findElement(By.id("mailCity"));
	}
	
	public WebElement facEmail1Input(){
		return driver.findElement(By.id("mailEmail1"));
	}
	
	public WebElement facEmail2Input(){
		return driver.findElement(By.id("mailEmail2"));
	}
	
	//Correspondence Address
	public WebElement correspFacNameInput(){
		return driver.findElement(By.id("correspContact"));
	}
	
	public WebElement correspAddr1Input(){
		return driver.findElement(By.id("correspAddr1"));
	}
	
	public WebElement correspAddr2Input() {
		return driver.findElement(By.id("correspAddr2"));
	}
	
	public WebElement correspCountryDropdown() {
		return driver.findElement(By.id("s2id_correspCountry"));
	}
	
	public WebElement correspCountryTxt(){
		return driver.findElement(By.xpath(".//*[@id='s2id_correspCountry']/a/span[1]"));
	}
	
	public WebElement correspZipInput() {
		return driver.findElement(By.id("correspZip"));
	}
	
	public WebElement correspPhone1Input() {
		return driver.findElement(By.id("correspPhone1"));
	}
	
	public WebElement correspFax1Input() {
		return driver.findElement(By.id("correspFax1"));
	}
	
	public WebElement correspCopyBtn() {
		return driver.findElement(By.id("correspCopy"));
	}
	
	public WebElement correspSateDropdown() {		
		return driver.findElement(By.id("correspState"));
	}
	
	public WebElement correspStateTxt(){
		return driver.findElement(By.xpath(".//*[@id='s2id_correspState']/a/span[1]"));
	}
	
	public WebElement correspCityInput() {
		return driver.findElement(By.id("correspCity"));
	}
	
	public WebElement correspEmail1Input(){
		return driver.findElement(By.id("correspEmail"));
	}
	
	//Default Remit To Address
	public WebElement remitFacilityNameInput(){
		return driver.findElement(By.id("remitName"));
	}
	
	public WebElement remitAddress1Input(){
		return driver.findElement(By.id("remitAddr1"));
	}
	
	public WebElement remitAddress2Input(){
		return driver.findElement(By.id("remitAddr2"));
	}
	
	public WebElement remitCountryTxt(){
		return driver.findElement(By.xpath(".//*[@id='s2id_remitCountry']/a/span[1]"));
	}
	
	public WebElement remitCountryDropdown(){
		return driver.findElement(By.id("s2id_remitCountry"));
	}
	
	public WebElement remitPhone1Input(){
		return driver.findElement(By.id("remitPhone1"));
	}
	
	public WebElement remitFax1Input(){
		return driver.findElement(By.id("remitFax1"));
	}
	
	public WebElement remitPhone2Input(){
		return driver.findElement(By.id("remitPhone2"));
	}
	
	public WebElement remitCopyFromFacilityBtn(){
		return driver.findElement(By.id("remitCopy"));
	}
	
	public WebElement remitStateDropdpwn(){
		return driver.findElement(By.id("remitState"));
	}
	
	public WebElement remitStateTxt(){
		return driver.findElement(By.xpath(".//*[@id='s2id_remitState']/a/span[1]"));
	}
	
	public WebElement remitCityInput(){
		return driver.findElement(By.id("remitCity"));
	}
	
	public WebElement remitEmail1Input(){
		return driver.findElement(By.id("remitEmail"));
	}
	
	public WebElement remitPostalCodeInput(){
		return driver.findElement(By.id("remitZip"));
	}
	
	//Facility Cross Reference
	public WebElement facCrossReferenceTbl(){
		return driver.findElement(By.id("tbl_facCrossRef"));
	}
	
	public WebElement facCrossRefTblEffDateFilter(){
		return driver.findElement(By.id("gs_effectiveDate"));
	}
	
	public WebElement facCrossRefTblExpDateFilter(){
		return driver.findElement(By.id("gs_expirationDate"));
	}
	
	public WebElement facCrossRefTblDesFilter(){
		return driver.findElement(By.id("gs_xrefId"));
	}
	
	public WebElement facCrossRefTblEffDtColInput(int row) { //row start by 1 
		return driver.findElement(By.xpath("//tr[(1+"+row+")]//input[@name='effectiveDate'][@role='textbox']"));
	}
	
	public WebElement facCrossRefTblEffDtColText(int row) { //row start by 1 and get text is get by attribute title/ get Text
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_facCrossRef_effectiveDate']"));
	}
	
	public WebElement facCrossRefTblExpDtColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]//input[@name='expirationDate'][@role='textbox']"));
	}
	
	public WebElement facCrossRefTblExpDtColText(int row) { 
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_facCrossRef_expirationDate']"));
	}
	
	public WebElement facCrossRefTblCrosRefDescriptionColDropdownList(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_facCrossRef_xrefId']/div[contains(@id,'xrefId')]"));
	}

	public WebElement facCrossRefTblCrosRefDescriptionColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_facCrossRef_xrefId']"));
	}
	
	public WebElement facCrossRefTblDeleteColChkBox(int row){
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_facCrossRef_deleted']/input"));
	}
	
	public WebElement facCrossRefTblAddIcon(){
		return driver.findElement(By.id("tbl_facCrossRef_iladd"));
	}
	
	public WebElement facCrossRefTblTotalRecord(){
		return driver.findElement(By.xpath(".//*[@id='tbl_facCrossRef_pagernav_right']/div"));
	}
	
	public WebElement  effDateTitleOnFacilityCrossReferenceSection () {
		return driver.findElement(By.xpath(".//*[@id='tbl_facCrossRef']//td[@aria-describedby='tbl_facCrossRef_effDateStr']"));
	}
	
	public WebElement  expDateTitleOnFacilityCrossReferenceSection () {
		return driver.findElement(By.xpath(".//*[@id='tbl_facCrossRef']//td[@aria-describedby='tbl_facCrossRef_expDateStr']"));
	}
	
	public WebElement  facilityCrossReferenceDescription(String index) {
		return driver.findElement(By.xpath("//*[@id='" + index + "']//td[5]"));
	}
	
	//Jurisdiction Payor 
	public WebElement jurisPayorTbl(){
		return driver.findElement(By.id("tbl_jurisdictionPayor"));
	}
	
	public WebElement jurisPyrTblPayorFilter(){
		return driver.findElement(By.id("gs_pyrAbbrv"));
	}
	
	public WebElement jurisPayorFilter(){
		return driver.findElement(By.id("gs_jurisdictionPyrAbbrv"));
	}
	
	public WebElement jurisPyrTblPayorIdColInput(int row) { //row start by 1 
		return driver.findElement(By.xpath("//tr[(1+"+row+")]//input[@name='pyrAbbrv'][@role='textbox']"));
	}
	
	public WebElement jurisPyrTblPayorIdColText(int row) { //row start by 1 and get text is get by attribute title/ get Text
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_jurisdictionPayor_pyrAbbrv']"));
	}
	
	public WebElement jurisPyrIdColInput(int row) { 
		return driver.findElement(By.xpath("//tr[(1+"+row+")]//input[@name='jurisdictionPyrAbbrv'][@role='textbox']"));
	}
	
	public WebElement jurisPyrIdColText(int row) { 
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_jurisdictionPayor_jurisdictionPyrAbbrv']"));
	}
	
	public WebElement jurisPyrTblDeleteColChkBox(int row){
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_facCrossRef_deleted']/input"));
	}
	
	public WebElement jurisPyrTblAddIcon(){
		return driver.findElement(By.id("tbl_jurisdictionPayor_iladd"));
	}
	
	public WebElement jurisPyrTblTotalRecord(){
		return driver.findElement(By.xpath(".//*[@id='tbl_jurisdictionPayor_pagernav_right']/div"));
	}
	
	public WebElement jurisPyrTblPyrColSearchIcon(String row) {
		return wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[("+row+")]/td[@aria-describedby='tbl_jurisdictionPayor_pyrAbbrv']//a[@title='Payor Search']")));
	}
	
	public WebElement jurisPyrTblJurisPyrColSearchIcon(String row) {
		return driver.findElement(By.xpath("//tr[("+row+")]/td[@aria-describedby='tbl_jurisdictionPayor_jurisdictionPyrAbbrv']//a[@title='Payor Search']"));
	}
	
	public WebElement jurisPyrTblJurisPyrColTxt(String row) {
		return driver.findElement(By.xpath("//tr[("+row+")]/td[@aria-describedby='tbl_jurisdictionPayor_jurisdictionPyrAbbrv']"));
	}
	
	public WebElement jurisPyrTblPyrColTxt(String row) {
		return driver.findElement(By.xpath("//tr[("+row+")]/td[@aria-describedby='tbl_jurisdictionPayor_pyrAbbrv']"));
	}
	
	//Billing Override
	public WebElement billingOverrideTbl(){
		return driver.findElement(By.id("tbl_payorBillingOverride"));
	}
	
	public WebElement overrideIdFilter(){
		return driver.findElement(By.id("gs_overrideDisplay"));
	}
	
	public WebElement npiFilter(){
		return driver.findElement(By.id("gs_npi"));
	}
	
	public WebElement taxIdFilter(){
		return driver.findElement(By.id("gs_taxId"));
	}
	
	public WebElement taxCdFilter(){
		return driver.findElement(By.id("gs_taxonomyCd"));
	}
	
	public WebElement taxNameFilter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorBillingOverride']//*[@id='gs_contactName']"));
	}
	
	public WebElement taxAddressFilter(){
		return driver.findElement(By.id("gs_address"));
	}
	
	public WebElement billOverrTblOverrIdCol(int row) { 
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorBillingOverride_overrideDisplay']"));
	}
	
	public WebElement billOverrTblNpiCol(int row) { 
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorBillingOverride_npi']"));
	}
	
	public WebElement billOverrTblTaxIdCol(int row) { 
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorBillingOverride_taxId']"));
	}
	
	public WebElement billOverrTblTaxonomyCdCol(int row) { 
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorBillingOverride_taxonomyCd']"));
	}
	
	public WebElement billOverrTblNameCol(int row) { 
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorBillingOverride_contactName']"));
	}
	
	public WebElement billOverrTblAddressCol(int row) { 
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorBillingOverride_address']"));
	}
	
	public WebElement billOverrTblDeleteColChkBox(int row){
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorBillingOverride_deleted']/input"));
	}	
	
	public WebElement billOverrTblAddIcon(){
		return driver.findElement(By.id("add_tbl_payorBillingOverride"));
	}
	
	public WebElement billOverrTblEditIcon(){
		return driver.findElement(By.id("edit_tbl_payorBillingOverride"));
	}
	
	public WebElement billOverrTblTotalRecord(){
		return driver.findElement(By.xpath(".//*[@id='tbl_payorBillingOverride_pagernav_right']/div"));
	}
	
	public WebElement billOverrTblPyrGrpInclDdl(){
		return driver.findElement(By.id("s2id_billPyrGrpIncl"));
	}
	
	public WebElement billOverrTblOverrIdCol(String row) { 
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorBillingOverride_overrideDisplay']"));
	}
	
	public WebElement billingOverrideAddRecordNpiInput(){
		return driver.findElement(By.id("npi"));
	}
	
	public WebElement billingOverrideAddRecordTaxIdInput(){
		return driver.findElement(By.id("taxId"));
	}
	
	public WebElement billingOverrideAddRecordTaxonomyCodeInput(){
		return driver.findElement(By.id("taxonomyCd"));
	}
	
	public WebElement billingOverrideAddRecordNameInput(){
		return driver.findElement(By.id("contactName"));
	}
	
	public WebElement billingOverrideAddRecordAddress1Input(){
		return driver.findElement(By.id("addressLine1"));
	}
	
	//Remit Address Override
		//** Payor Group Override Remit Address
	public WebElement pyrGrpOverRemitAddrTbl(){
		return driver.findElement(By.id("tbl_payorGroupOverrideRemitAddress"));
	}
	
	public WebElement payorGrpFilter(){
		return driver.findElement(By.id("gs_pyrGrpId"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblNameFilter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorGroupOverrideRemitAddress']//*[@id='gs_contactName']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblAddress1Filter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorGroupOverrideRemitAddress']//*[@id='gs_addressLine1']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblAddress2Filter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorGroupOverrideRemitAddress']//*[@id='gs_addressLine2']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblPhoneFilter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorGroupOverrideRemitAddress']//*[@id='gs_phoneNumber']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblPhone2Filter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorGroupOverrideRemitAddress']//*[@id='gs_phoneNumber2']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblCityFilter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorGroupOverrideRemitAddress']//*[@id='gs_city']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblFaxFilter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorGroupOverrideRemitAddress']//*[@id='gs_faxNumber']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblEmailFilter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorGroupOverrideRemitAddress']//*[@id='gs_email']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblStateFilter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorGroupOverrideRemitAddress']//*[@id='gs_state']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblCountryFilter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorGroupOverrideRemitAddress']//*[@id='gs_countryCode']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblPostalCodeFilter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorGroupOverrideRemitAddress']//*[@id='gs_zip']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblPyrGrpColDropdownList(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_pyrGrpId']/div[contains(@id,'pyrGrpId')]"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblPyrGrpColText(int row) { 
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_pyrGrpId']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblNameColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_contactName']/input"));
	}

	public WebElement pyrGrpOverRemitAddrTblNameColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_contactName']"));
	}

	public WebElement pyrGrpOverRemitAddrTblAddr1ColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_addressLine1']"));
	}

	public WebElement pyrGrpOverRemitAddrTblAddr1ColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_addressLine1']/input"));
	}

	public WebElement pyrGrpOverRemitAddrTblAddr2ColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_addressLine2']"));
	}

	public WebElement pyrGrpOverRemitAddrTblAddr2ColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_addressLine2']/input"));
	}

	public WebElement pyrGrpOverRemitAddrTblPhoneColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_phoneNumber']"));
	}

	public WebElement pyrGrpOverRemitAddrTblPhoneColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_phoneNumber']/input"));
	}

	public WebElement pyrGrpOverRemitAddrTblPhone2ColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_phoneNumber2']"));
	}

	public WebElement pyrGrpOverRemitAddrTblPhone2ColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_phoneNumber2']/input"));
	}

	public WebElement pyrGrpOverRemitAddrTblCityColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_city']"));
	}

	public WebElement pyrGrpOverRemitAddrTblCityColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_city']/input"));
	}

	public WebElement pyrGrpOverRemitAddrTblFaxColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_faxNumber']"));
	}

	public WebElement pyrGrpOverRemitAddrTblFaxColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_faxNumber']/input"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblEmailColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_email']"));
	}

	public WebElement pyrGrpOverRemitAddrTblEmailColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_email']/input"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblStateColDropdownList(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_state']/div[contains(@id,'state')]"));
	}

	public WebElement pyrGrpOverRemitAddrTblStateColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_state']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblCountryColDropdownList(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_countryCode']/div[contains(@id,'countryCode')]"));
	}

	public WebElement pyrGrpOverRemitAddrTblCountryColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/tpyrOverRemitAddrTblbl_payorGroupOverrideRemitAddress_countryCode']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblPostalCdColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_zip']/input"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblPostalCdColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_zip']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblPostalCdColSearchIcon(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_zip']//a[@title='Zip Code Search']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblDeleteColChkBox(int row){
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_deleted']/input"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblSAddIcon(){
		return driver.findElement(By.id("tbl_payorGroupOverrideRemitAddress_iladd"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblTotalRecordLbl(){
		return driver.findElement(By.xpath(".//*[@id='tbl_payorGroupOverrideRemitAddress_pagernav_right']/div"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblPyrGrpColDropdownList(String row) {
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_pyrGrpId']/div[contains(@id,'pyrGrpId')]"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblNameColInput(String row) {
		return driver.findElement(By.xpath(".//tr["+row+"]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_contactName']/input"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblAddr1ColInput(String row) {
		return driver.findElement(By.xpath(".//tr["+row+"]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_addressLine1']/input"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblCityColInput(String row) {
		return driver.findElement(By.xpath(".//tr["+row+"]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_city']/input"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblEmailColInput(String row) {
		return driver.findElement(By.xpath(".//tr["+row+"]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_email']/input"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblStateColDropdownList(String row) {
		return driver.findElement(By.xpath(".//tr["+row+"]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_state']/div[contains(@id,'state')]"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblCountryColDropdownList(String row) {
		return driver.findElement(By.xpath(".//tr["+row+"]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_countryCode']/div[contains(@id,'countryCode')]"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblPostalCdColInput(String row) {
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_zip']/input"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblPyrGrpColText(String row) {
		return driver.findElement(By.xpath(".//tr["+row+"]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_pyrGrpId']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblCountryColText(String row) {
		return driver.findElement(By.xpath(".//tr["+row+"]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_countryCode']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblStateColText(String row) {
		return driver.findElement(By.xpath(".//tr["+row+"]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_state']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblNameColText(String row) {
		return driver.findElement(By.xpath(".//tr["+row+"]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_contactName']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblAddr1ColText(String row) {
		return driver.findElement(By.xpath(".//tr["+row+"]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_addressLine1']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblPostalCdColText(String row) {
		return driver.findElement(By.xpath(".//tr["+row+"]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_zip']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblCityColText(String row) {
		return driver.findElement(By.xpath(".//tr["+row+"]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_city']"));
	}
	
	public WebElement pyrGrpOverRemitAddrTblEmailColText(String row) {
		return driver.findElement(By.xpath(".//tr["+row+"]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_email']"));
	}
	
		//** Payor Override Remit Address
	public WebElement pyrOverRemitAddrTbl(){
		return driver.findElement(By.id("tbl_payorOverrideRemitAddress"));
	}
	
	public WebElement payorFilter(){
		return driver.findElement(By.id("gs_pyrAbbrev"));
	}
	
	public WebElement pyrOverRemitAddrTbleFilter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorOverrideRemitAddress']//*[@id='gs_contactName']"));
	}
	
	public WebElement pyrOverRemitAddrTblAddress1Filter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorOverrideRemitAddress']//*[@id='gs_addressLine1']"));
	}
	
	public WebElement pyrOverRemitAddrTblAddress2Filter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorOverrideRemitAddress']//*[@id='gs_addressLine2']"));
	}
	
	public WebElement pyrOverRemitAddrTblPhoneFilter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorOverrideRemitAddress']//*[@id='gs_phoneNumber']"));
	}
	
	public WebElement pyrOverRemitAddrTblPhone2Filter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorOverrideRemitAddress']//*[@id='gs_phoneNumber2']"));
	}
	
	public WebElement pyrOverRemitAddrTblCityFilter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorOverrideRemitAddress']//*[@id='gs_city']"));
	}
	
	public WebElement pyrOverRemitAddrTblFaxFilter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorOverrideRemitAddress']//*[@id='gs_faxNumber']"));
	}
	
	public WebElement pyrOverRemitAddrTblEmailFilter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorOverrideRemitAddress']//*[@id='gs_email']"));
	}
	
	public WebElement pyrOverRemitAddrTblStateFilter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorOverrideRemitAddress']//*[@id='gs_state']"));
	}
	
	public WebElement pyrOverRemitAddrTblCountryFilter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorOverrideRemitAddress']//*[@id='gs_countryCode']"));
	}
	
	public WebElement pyrOverRemitAddrTblPostalCodeFilter(){
		return driver.findElement(By.xpath("//*[@id='gbox_tbl_payorOverrideRemitAddress']//*[@id='gs_zip']"));
	}
	
	public WebElement pyrOverRemitAddrTblPyrColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_pyrAbbrev']/input"));
	}
	
	public WebElement pyrOverRemitAddrTblPyrColSearchIcon(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_pyrAbbrev']//a"));
	}
	
	public WebElement pyrOverRemitAddrTblPyrColText(int row) { 
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_pyrAbbrev']"));
	}
	
	public WebElement pyrOverRemitAddrTblNameColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_contactName']/input"));
	}

	public WebElement pyrOverRemitAddrTblNameColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_contactName']"));
	}

	public WebElement pyrOverRemitAddrTblAddr1ColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_addressLine1']"));
	}

	public WebElement pyrOverRemitAddrTblAddr1ColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_addressLine1']/input"));
	}

	public WebElement pyrOverRemitAddrTblAddr2ColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_addressLine2']"));
	}

	public WebElement pyrOverRemitAddrTblAddr2ColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_addressLine2']/input"));
	}

	public WebElement pyrOverRemitAddrTblPhoneColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_phoneNumber']"));
	}

	public WebElement pyrOverRemitAddrTblPhoneColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_phoneNumber']/input"));
	}

	public WebElement pyrOverRemitAddrTblPhone2ColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_phoneNumber2']"));
	}

	public WebElement pyrOverRemitAddrTblPhone2ColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_phoneNumber2']/input"));
	}

	public WebElement pyrOverRemitAddrTblCityColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_city']"));
	}

	public WebElement pyrOverRemitAddrTblCityColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_city']/input"));
	}

	public WebElement pyrOverRemitAddrTblFaxColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_faxNumber']"));
	}

	public WebElement pyrOverRemitAddrTblFaxColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_faxNumber']/input"));
	}
	
	public WebElement pyrOverRemitAddrTblEmailColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_email']"));
	}

	public WebElement pyrOverRemitAddrTblEmailColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_email']/input"));
	}
	
	public WebElement pyrOverRemitAddrTblStateColDropdownList(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_state']/div[contains(@id,'state')]"));
	}

	public WebElement pyrOverRemitAddrTblStateColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_state']"));
	}
	
	public WebElement pyrOverRemitAddrTblCountryColDropdownList(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_countryCode']/div[contains(@id,'countryCode')]"));
	}

	public WebElement pyrOverRemitAddrTblCountryColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_countryCode']"));
	}
	
	public WebElement pyrOverRemitAddrTblPostalCdColInput(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_zip']/input"));
	}
	
	public WebElement pyrOverRemitAddrTblPostalCdColText(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_zip']"));
	}
	
	public WebElement pyrOverRemitAddrTblPostalCdColSearchIcon(int row) {
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorOverrideRemitAddress_zip']//a"));
	}
	
	public WebElement pyrOverRemitAddrTblDeleteColChkBox(int row){
		return driver.findElement(By.xpath("//tr[(1+"+row+")]/td[@aria-describedby='tbl_payorGroupOverrideRemitAddress_deleted']/input"));
	}
	
	public WebElement pyrOverRemitAddrTblAddIcon(){
		return driver.findElement(By.id("tbl_payorOverrideRemitAddress_iladd"));
	}
	
	public WebElement pyrOverRemitAddrTblTotalRecordLbl(){
		return driver.findElement(By.xpath(".//*[@id='tbl_payorOverrideRemitAddress_pagernav_right']/div"));
	}
	
	public WebElement pyrOverRemitAddrTblPyrColSearchIcon(String row) {
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorOverrideRemitAddress_pyrAbbrev']//a"));
	}
	
	public WebElement pyrOverRemitAddrTblNameColInput(String row) {
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorOverrideRemitAddress_contactName']/input"));
	}
	
	public WebElement pyrOverRemitAddrTblAddr1ColInput(String row) {
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorOverrideRemitAddress_addressLine1']/input"));
	}
	
	public WebElement pyrOverRemitAddrTblCityColInput(String row) {
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorOverrideRemitAddress_city']/input"));
	}
	
	public WebElement pyrOverRemitAddrTblEmailColInput(String row) {
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorOverrideRemitAddress_email']/input"));
	}
	
	public WebElement pyrOverRemitAddrTblStateColDropdownList(String row) {
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorOverrideRemitAddress_state']/div[contains(@id,'state')]"));
	}
	
	public WebElement pyrOverRemitAddrTblCountryColDropdownList(String row) {
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorOverrideRemitAddress_countryCode']/div[contains(@id,'countryCode')]"));
	}
	
	public WebElement pyrOverRemitAddrTblPostalCdColInput(String row) {
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorOverrideRemitAddress_zip']/input"));
	}
	
	public WebElement pyrOverRemitAddrTblCountryColText(String row) {
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorOverrideRemitAddress_countryCode']"));
	}
	
	public WebElement pyrOverRemitAddrTblStateColText(String row) {
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorOverrideRemitAddress_state']"));
	}
	
	public WebElement pyrOverRemitAddrTblPyrColText(String row) { 
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorOverrideRemitAddress_pyrAbbrev']"));
	}
	
	public WebElement pyrOverRemitAddrTblNameColText(String row) {
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorOverrideRemitAddress_contactName']"));
	}
	
	public WebElement pyrOverRemitAddrTblAddr1ColText(String row) {
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorOverrideRemitAddress_addressLine1']"));
	}
	
	public WebElement pyrOverRemitAddrTblPostalCdColText(String row) {
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorOverrideRemitAddress_zip']"));
	}
	
	public WebElement pyrOverRemitAddrTblCityColText(String row) {
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorOverrideRemitAddress_city']"));
	}
	
	public WebElement pyrOverRemitAddrTblEmailColText(String row) {
		return driver.findElement(By.xpath("//tr["+row+"]/td[@aria-describedby='tbl_payorOverrideRemitAddress_email']"));
	}

	//Footer
	public WebElement helpLinkIcon(){
		return driver.findElement(By.id("pageHelpLink"));
	}
	
	public WebElement showClipboardIcon(){
		return driver.findElement(By.xpath(".//*[@title='Show Clipboard']"));
	}
	
	public WebElement keyboardShortIcon(){
		return driver.findElement(By.xpath(".//*[@title='Show Keyboard Shortcuts']"));
	}
	
	public WebElement sectionSearchInput(){
		return driver.findElement(By.id("sectionSearchField"));
	}
	
	public WebElement resetBtn(){
		return driver.findElement(By.id("Reset"));
	}
	
	public WebElement saveAndClearBtn(){
		return driver.findElement(By.id("btnSaveAndClear"));
	}
	
	public WebElement primFacIdText(){
		return driver.findElement(By.xpath(".//*[contains(@id,'_primFacId')]/a"));
	}
	
	public WebElement billingFacIdText(){
		return driver.findElement(By.xpath(".//*[contains(@id,'_billingFacId')]/a"));
	}
	
	public WebElement facTypeText(){
		return driver.findElement(By.xpath(".//*[contains(@id,'_facType')]/a"));
	}
	
	public WebElement delFacDemographicsRecordChkBox(){
		return driver.findElement(By.id("delFacCB"));
	}
	
	public WebElement facCountryInput() {
		return driver.findElement(By.xpath(".//*[contains(@id,'_mailCountry')]/input"));
	}
	
	public WebElement facStateInput() {
		return driver.findElement(By.xpath(".//*[contains(@id,'_mailState')]/input"));
	}
	
	public WebElement  corresAddrSectionStateTextbox (){
		return driver.findElement(By.xpath(".//*[contains(@id,'_correspState')]/input"));
	}
	
	public WebElement  corresAddrSectionCountryCombobox (){
		return driver.findElement(By.xpath(".//*[contains(@id,'_correspCountry')]//span[1]"));
	}
	
	public WebElement  defaultRemitToAddressSectionRemitFacilityNameTextbox (){
		return driver.findElement(By.id("remitName"));
	}
	
	public WebElement  defaultRemitToAddressSectionRemitAddressTextbox (int num){
		return driver.findElement(By.id("remitAddr"+num));
	}

	public WebElement  defaultRemitToAddressSectionCountryCombobox (){
		return driver.findElement(By.xpath(".//*[contains(@id,'_remitCountry')]//span[1]"));
	}
	
	public WebElement  defaultRemitToAddressSectionPostalCodeTextBox (){
		return driver.findElement(By.id("remitZip"));
	}
	
	public WebElement  defaultRemitToAddressSectionPhoneTextBox (int num){
		return driver.findElement(By.id("remitPhone"+num));
	}
	
	public WebElement  defaultRemitToAddressSectionFax1TextBox (){
		return driver.findElement(By.id("remitFax1"));
	}
	
	public WebElement  defaultRemitToAddressSectionStateCombobox (){
		return driver.findElement(By.xpath(".//*[contains(@id,'_remitState')]//span[1]"));
	}
	
	public WebElement  defaultRemitToAddressSectionCityTextBox (){
		return driver.findElement(By.id("remitCity"));
	}
	
	public WebElement  defaultRemitToAddressSectionEmailTextBox (){
		return driver.findElement(By.id("remitEmail"));
	}
	
	public WebElement  facilityCrossReferenceTableBottomNav  (){
		return driver.findElement(By.xpath(".//*[@id='tbl_facCrossRef_pagernav_right']/div"));
	}
	public WebElement  jurisdictionPayorTableBottomNav  (){
		return driver.findElement(By.xpath(".//*[@id='tbl_jurisdictionPayor_pagernav_right']/div"));
	}
	
	public WebElement  billingOverrideTableBottomNav  (){
		return driver.findElement(By.xpath(".//*[@id='tbl_payorBillingOverride_pagernav_right']/div"));
	}
	
	public WebElement  remitAddressOverrideTablePayorGroupOverrideRemitAddressBottomNav (){
		return driver.findElement(By.xpath(".//*[@id='tbl_payorGroupOverrideRemitAddress_pagernav_right']/div"));
	}
	
	public WebElement  remitAddressOverrideTablePayorOverrideRemitAddressBottomNav (){
		return driver.findElement(By.xpath(".//*[@id='tbl_payorOverrideRemitAddress_pagernav_right']/div"));
	}
	
	public WebElement  facGrpTblPagerNavRight() {
		return driver.findElement(By.xpath(".//*[@id='tbl_facilityGroup_pagernav_right']/div"));
	}
	
	public WebElement  inputFieldOnDropdownbox (){
		return driver.findElement(By.xpath(".//*[contains(@id,'-drop')]//input"));
	}
	//check data is available for using after choosen a type
	public WebElement  selectedTypeOnDropdownbox (){
		return driver.findElement(By.xpath(".//*[contains(@id,'_facType')]/a/span[1]"));
	}

	public WebElement  svcTypDiagLabCheckbox (){
		return driver.findElement(By.xpath(".//*[@id='svcTypContainer']//input[@name='svcTyp']"));
	}
	
	public WebElement  licenseInformationSectionTaxonomyCodeTextbox (){
		return driver.findElement(By.xpath(".//*[@id='taxonomyLicenseInfo']"));
	}
	
	public WebElement  facGrpDropdown(){
		return driver.findElement(By.xpath(".//*[@aria-describedby='tbl_facilityGroup_facGrpId']"));
	}
	
	public WebElement  facGrpIdDropdown(){
		return driver.findElement(By.id("1_facGrpId"));
	}
	
	public WebElement  facilityGrouptable (){
		return driver.findElement(By.id("tbl_facilityGroup"));
	}
	
	public WebElement  facZipSearchIconBtn() {
		return driver.findElement(By.xpath(".//*[@data-search-type='zipcode']"));
	}
	
	public WebElement  reasonCodeSearchPopup () {
		return driver.findElement(By.id("reasonCodeSearch"));
	}
	
	public WebElement  zipCodeInputOnReasonCodeSearchPopup () {
		return driver.findElement(By.id("zip"));
	}
	
	public WebElement  cityInputOnReasonCodeSearchPopup () {
		return driver.findElement(By.id("city"));
	}
	
	public WebElement  stateDropdownOnReasonCodeSearchPopup () {
		return driver.findElement(By.id("stateDroplist"));
	}
	
	public WebElement  searchButtonOnSearchPopup () {
		return driver.findElement(By.xpath(".//*[@accesskey='s']"));
	}
	
	public WebElement  zipCodeSearchTable (){
		return driver.findElement(By.id("zipCodeSearchTable"));
	}
	
	public WebElement facState(){
		return driver.findElement(By.xpath(".//*[contains(@id,'_mailState')]//a/span"));
	}

	public WebElement  FacilityCrossReferenceEffectiveDateInput(String index){
		return driver.findElement(By.id(index + "_effDateStr"));
	}

	public WebElement  FacilityCrossReferenceExpirationDateInput(String index){
		return driver.findElement(By.id(index + "_expDateStr"));
	}
	
	public WebElement  searchIconOnFacilityCrossReferenceSection (int number){
		// 4 is the fist one , 6 is the second
		return driver.findElement(By.xpath(".//*[@id='tbl_jurisdictionPayor']/tbody/tr[2]/td["+number+"]//a[@title='Payor Search']"));
	}
	
	public WebElement  payorIDInputOnPayorSearchPopup (){
		return driver.findElement(By.id("payorAbbrev"));
	}
	
	public WebElement  payorSearchResultTable (){
		return driver.findElement(By.id("payorsearchTable"));
	}
	
	public WebElement  pyrGrpInclOnAddNewBillingOverridePopup (){
		return driver.findElement(By.xpath(".//*[contains(@id,'_billPyrGrpIncl')]//ul[contains(@class,'-choices')]"));
	}
	
	public WebElement  pyrGrpExclOnAddNewBillingOverridePopup (){
		return driver.findElement(By.xpath(".//*[contains(@id,'_billPyrGrpExcl')]//ul[contains(@class,'-choices')]"));
	}
	
	public WebElement  submitButton (){
		return driver.findElement(By.id("sData"));
	}
	
	public WebElement pyrGrpDropdownOnPyrGrpOverRemitAddrTbl(int row){
		return driver.findElement(By.xpath(".//*[@id='tbl_payorGroupOverrideRemitAddress']/tbody/tr["+row+"]/td[3]//a"));
	}
	
	public WebElement  pyrInputOnPayorOverrideRemitAddrTable (int row){
		return driver.findElement(By.xpath(".//*[@id='tbl_payorOverrideRemitAddress']/tbody/tr["+row+"]/td[4]//input"));
	}
	
	public WebElement  rowOnPayorGroupOverrideRemitAddressTbl (String row, String mark){
		//addressLine1
		//city
		//State
		//Zip
		//Country
		return driver.findElement(By.xpath(".//*[@id='tbl_payorGroupOverrideRemitAddress']//tr["+row+"]//td[contains(@aria-describedby,'"+mark+"')]"));
	}
	
	public WebElement  stateDropdownOnPayorGroupOverrideRemitAddress (String row){
		return driver.findElement(By.xpath(".//*[@id='tbl_payorGroupOverrideRemitAddress']/tbody/tr["+row+"]/td[12]"));
	}
	
	public WebElement  rowOnPayorOverrideRemitAddressTbl (String row, String mark){
		//addressLine1
		//city
		//State
		//Zip
		//Country
		return driver.findElement(By.xpath(".//*[@id='tbl_payorOverrideRemitAddress']//tr["+row+"]//td[contains(@aria-describedby,'"+mark+"')]"));
	}
	
	public WebElement  stateDropdownOnPayorOverrideRemitAddress (String row){
		return driver.findElement(By.xpath(".//*[@id='tbl_payorOverrideRemitAddress']/tbody/tr["+row+"]/td[13]"));
	}
	
	public WebElement  billPyrGrpInclInputOnDropdown (){
		return driver.findElement(By.xpath(".//*[contains(@id,'billPyrGrpIncl')]//ul[contains(@class,'-choices')]//input"));
	}
	
	public WebElement deleteCheckboxOnFacCrossReferenceTbl(){
		return driver.findElement(By.xpath(".//*[@id='tbl_facCrossRef']/tbody/tr[2]/td[6]//input"));
	}
	
	public WebElement deleteCheckboxOnPayorBillingOverrideTbl(){
		return driver.findElement(By.xpath(".//*[@id='tbl_payorBillingOverride']/tbody/tr[2]/td[23]//input"));
	}
	
	public WebElement deleteCheckboxOnJurisdictionPayorTbl(){
		return driver.findElement(By.xpath(".//*[@id='tbl_jurisdictionPayor']/tbody/tr[2]/td[7]//input"));
	}
	
	public WebElement deleteCheckboxOnPyrGrpOverRemitAddrTbl(){
		return driver.findElement(By.xpath(".//*[@id='tbl_payorGroupOverrideRemitAddress']/tbody/tr[2]/td[15]//input"));
	}
	
	public WebElement deleteCheckboxOnPyrOverRemitAddrTbl(){
		return driver.findElement(By.xpath(".//*[@id='tbl_payorOverrideRemitAddress']/tbody/tr[2]/td[16]//input"));
	}

	public WebElement helpIconInFacilitySection(){
		return driver.findElement(By.xpath(".//*[@data-help-id='p_facility_enter_facility']"));
	}
	
	public WebElement helpIconInHeaderSection(){
		return driver.findElement(By.xpath(".//*[@data-help-id='p_facility_header']"));
	}
	
	public WebElement helpIconInFacilityAdditionalInformationSection(){
		return driver.findElement(By.xpath(".//*[@data-help-id='p_facility_facility_additional_information']"));
	}                                                           
	
	public WebElement helpIconInFacilityAddrCntctInfoSection(){
		return driver.findElement(By.xpath(".//*[@data-help-id='p_facility_facility_address_contact_information']"));
	} 
	
	public WebElement helpIconInCorrespondenceAddressSection(){
		return driver.findElement(By.xpath(".//*[@data-help-id='p_facility_correspondence_address']"));
	} 
	
	public WebElement helpIconInDefaultRemitToAddressSection(){
		return driver.findElement(By.xpath(".//*[@data-help-id='p_facility_default_remit_to_address']"));
	} 
	
	public WebElement helpIconInFacilityCrossReferenceSection(){
		return driver.findElement(By.xpath(".//*[@data-help-id='p_facility_facility_cross_reference']"));
	}
	
	public WebElement helpIconInJurisdictionPayorSection(){
		return driver.findElement(By.xpath(".//*[@data-help-id='p_facility_jurisdiction_payor']"));
	}
	
	public WebElement helpIconInBillingOverrideSection(){
		return driver.findElement(By.xpath(".//*[@data-help-id='p_facility_payor_billing_override']"));
	}
	
	public WebElement helpIconInRemitAddressOverrideSection(){
		return driver.findElement(By.xpath(".//*[@data-help-id='p_facility_remit_address_override']"));
	}
	
	public WebElement payorGroupOverrideRemitAddressPagerNavRight(){
		return driver.findElement(By.xpath(".//*[@id='tbl_payorGroupOverrideRemitAddress_pagernav_right']//div"));
	}
	
	public WebElement payorOverrideRemitAddressPagerNavRight(){
		return driver.findElement(By.xpath(".//*[@id='tbl_payorOverrideRemitAddress_pagernav_right']//div"));
	}
	
	public WebElement  inputFieldOnDropdownboxOnAddBillingOvr (){
		return driver.findElement(By.xpath(".//*[contains(@id,'billPyrGrpIncl')]//input"));
	}
	
	public WebElement payorBillingOverridePagerNavRight(){
		return driver.findElement(By.xpath(".//*[@id='tbl_payorBillingOverride_pagernav_right']//div"));
	}
	
	public WebElement  payorSearchResultTableSearchIcon(){
		return driver.findElement(By.xpath(".//*[contains(@class,'searchNav')]/button[@value='Submit']"));
	}
	
	public WebElement payorSearchResultTblIdColTxt(String row) {
		return driver.findElement(By.xpath("//tr[("+row+")]/td[@aria-describedby='payorsearchTable_payorAbbrev']/a"));
	}
	
	public WebElement helpFileTitle()  {		
		return driver.findElement(By.xpath("/html/body/h1"));
	}

	public WebElement  crossReferenceDescriptionDropdownInFacilityCrossReference(int index){
		return driver.findElement(By.id(index + "_xrefId"));
	}
		
    public void setFacilityId(String facId, WebDriverWait wait) 
    {
        WebElement facIdInput = facIdInput();
        wait.until(ExpectedConditions.elementToBeClickable(facIdInput));
        facIdInput().sendKeys(facId);
        facIdInput().sendKeys(Keys.TAB);
        logger.info("       Entered a Facility ID: " + facId);
    }

	public boolean isFacilityLoaded(String facId, WebDriverWait wait) 
	{
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(ID_MAIN_SECTIONS))));
		wait.until(ExpectedConditions.visibilityOf(saveAndClearBtn()));
		return StringUtils.equalsIgnoreCase(StringUtils.trim(facAbbrev().getAttribute("value")), facId);
	}
	public void setFacSpecialtyType(String facSpecialty, WebDriverWait wait)
	{
		WebElement facilitySpecialtyType = facilitySpecialtyTypeDropDown();
		wait.until(ExpectedConditions.elementToBeClickable(facilitySpecialtyType));
		Select select = new Select(facilitySpecialtyType);
		select.selectByVisibleText(facSpecialty);
		logger.info("Selected Facility Specialty Type: " + facSpecialty);
	}
	public List<String> getAllOptionsFromBillingFac()
	{
		WebElement selectElement = driver.findElement(By.id("billingFacId"));
		Select select = new Select(selectElement);
		List<WebElement> allOptions = select.getOptions();
		List<String> optionTexts=new ArrayList<>();
		for (WebElement allOption : allOptions) {
			optionTexts.add(allOption.getText());
		}
		return optionTexts;
	}
	/* Select dropdown JQGrid by text */
	public void selectDropDownByText(WebElement dropDown, String textSelect) throws InterruptedException {
		Actions actions = new Actions(driver);
		dropDown.click();
		WebElement list = driver.findElement(By.xpath(".//*[@id='select2-drop']/ul"));

		List<WebElement> allRows = list.findElements(By.tagName("li"));
		for (WebElement row : allRows) {
			if (row.getText().equalsIgnoreCase(textSelect)) {
				row.click();
//				actions.click().build().perform();
				logger.info("        Selected " + textSelect + " from dropdown.");
				break;
			}
		}
	}
}
