package com.overall.client.physicianAssignment;


import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PhysicianAssignment {
	private WebDriverWait wait;
	private RemoteWebDriver driver;
	protected Logger logger;
	
	public PhysicianAssignment(RemoteWebDriver driver)  {
		this.wait = new WebDriverWait(driver, 10);
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*Physician Assignment Load Page*/
	public WebElement physAssignmentLoadPgTitleTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));
	}
	
	public WebElement clientIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupClientAbbrev")));
	}
	
	public WebElement physicianAssignmentLoadPgClientIdSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupClientSrchBtn")));
	}

	public WebElement loadClientSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_physician_assignment_load_client_id']")));
	}
	
	public WebElement loadClientSection() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='clientPhysAssignmentForm']//*[contains(@class,'layoutMainLookup')]")));
	}
	
	/*Physician Assignment Detail Page*/
	/*** Header ***/
	public WebElement headerHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_physician_assignment_header']")));
	}
	
	public WebElement headerClientIdTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clientAbbrev")));
	}
	
	public WebElement headerClientNmTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clientName")));
	}
	
	public WebElement headerAccountTypTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clientAccountType")));
	}

	public List<WebElement> headerFacilityTxt() {
		return driver.findElements(By.xpath(".//*[@id='facilityHeaderInfo']/span"));
	}
	
	public WebElement headerClientOrderingFacAbbrevTxt()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='facilityHeaderInfo']//span[contains(@class,'clientOrderingFacAbbrev')]")));
	}
	
	public WebElement headerClientOrderingFacNameTxt()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='facilityHeaderInfo']//span[contains(@class,'clientOrderingFacName')]")));  
	}
	
	public WebElement headerViewDocumentLnk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("viewDocumentLabel")));
	}
	
	public WebElement headerViewClnPortalDocUpDocumentLnk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("viewOrganizationDocumentLabel")));
	}
	
	/***  Physician Assignment section ***/
	public WebElement titleSectionNameTitle(String sectionName){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='titleTextContainer']//span[@class='titleText'][.='"+sectionName+"']")));
	}
	
	public WebElement physAssignmentSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_physician_assignment_client_physician_assignment']")));
	}
	
	public WebElement physAssignmentTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_clientPhysAssignments")));
	}
	
	public WebElement physAssignmentTblRowNum(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientPhysAssignments']//tr["+row+"]")));
	}
	
	public WebElement physAssignmentTblNPIFilterInput()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_npiId")));
	}
	
	public WebElement physAssignmentTblUPINFilterInput()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_upinId")));
	}
	
	public WebElement physAssignmentTblLastNameFilterInput()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_lastName")));
	}
	
	public WebElement physAssignmentTblFirstNameFilterInput()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_firstName")));
	}
	
	public WebElement physAssignmentTblPostalCdFilterInput()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_zipId")));
	}
	
	public WebElement physAssignmentTblStateFilterInput()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_stateId")));
	}
	
	public WebElement physAssignmentTblSpecialtyFilterInput()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_specialty")));
	}
	
	public WebElement physAssignmentTblCredentialsFilterInput()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_credentials")));
	}
	
	public WebElement physAssignmentTblNPIInput()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientPhysAssignments']//td[@aria-describedby='tbl_clientPhysAssignments_npiId']//input")));
	}
	
	public WebElement physAssignmentTblNPISearchIco()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientPhysAssignments']//td[@aria-describedby='tbl_clientPhysAssignments_npiId']//a")));
	}
	
	public WebElement physAssignmentTblNPITxt(String row)  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientPhysAssignments']//tr["+row+"]//td[@aria-describedby='tbl_clientPhysAssignments_npiId']")));
	}
	
	public WebElement physAssignmentTblUPINInput()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientPhysAssignments']//td[@aria-describedby='tbl_clientPhysAssignments_upinId']//input")));
	}
	
	public WebElement physAssignmentTblUPINSearchIco()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientPhysAssignments']//td[@aria-describedby='tbl_clientPhysAssignments_upinId']//a")));
	}
	
	public WebElement physAssignmentTblUPINTxt(String row)  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientPhysAssignments']//tr["+row+"]//td[@aria-describedby='tbl_clientPhysAssignments_upinId']")));
	}
	
	public WebElement physAssignmentTblLastNameTxt(String row)  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientPhysAssignments']//tr["+row+"]/td[@aria-describedby='tbl_clientPhysAssignments_lastName']")));
	}
	
	public WebElement physAssignmentTblFirstNameTxt(String row)  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientPhysAssignments']//tr["+row+"]/td[@aria-describedby='tbl_clientPhysAssignments_firstName']")));
	}
	
	public WebElement physAssignmentTblPostalCdTxt(String row)  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientPhysAssignments']//tr["+row+"]/td[@aria-describedby='tbl_clientPhysAssignments_zipId']")));
	}
	
	public WebElement physAssignmentTblStateTxt(String row)  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientPhysAssignments']//tr["+row+"]/td[@aria-describedby='tbl_clientPhysAssignments_stateId']")));
	}
	
	public WebElement physAssignmentTblSpecialtyTxt(String row)  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientPhysAssignments']//tr["+row+"]/td[@aria-describedby='tbl_clientPhysAssignments_specialty']")));
	}
	
	public WebElement physAssignmentTblCredentialsTxt(String row)  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientPhysAssignments']//tr["+row+"]/td[@aria-describedby='tbl_clientPhysAssignments_credentials']")));
	}
	
	public WebElement physAssignmentTblIsDeletedChk(String row)  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientPhysAssignments']//tr["+row+"]/td[@aria-describedby='tbl_clientPhysAssignments_isDelete']/input")));
	}
	
	public WebElement physAssignmentTblAddBtn()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_clientPhysAssignments_iladd")));
	}
	
	public WebElement physAssignmentTblFirstPageIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='first_tbl_clientPhysAssignments_pagernav']/span")));
	}
	
	public WebElement physAssignmentTblPrevPageIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prev_tbl_clientPhysAssignments_pagernav']/span")));
	}
	
	public WebElement physAssignmentTblNextPageIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_tbl_clientPhysAssignments_pagernav']/span")));
	}
	
	public WebElement physAssignmentTblLastPageIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='last_tbl_clientPhysAssignments_pagernav']/span")));
	}
	
	public WebElement physAssignmentTblTotalResultTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clientPhysAssignments_pagernav_right']/div")));
	}
	
	public WebElement physAssignmentSectionCopyClientIdSearchIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cpyClnPhysSrchBtn")));
	}
	
	public WebElement physAssignmentSectionCopyClientIdInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("copyClientPhys")));
	}
	
	/*** footer ***/
	public WebElement footerHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_client_physician_assignment_summary']")));
	}
	
	public WebElement resetBtn()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}
	
	public WebElement saveAndClearBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}
	public WebElement confirmationDialogTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='confirmationDialog']//span")));
	}
	public WebElement confirmationDialogResetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='confirmationDialog']//following-sibling::div//span[contains(text(), 'Reset')]")));
	}
	/*** Field Validation ***/
	public WebElement fieldValidationTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='xf_message_content']")));
	}
	
	
}
