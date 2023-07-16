package com.overall.payor.payorContactManager;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PayorContactManager {
	private WebDriverWait wait;
	protected Logger logger;
	
	public PayorContactManager(RemoteWebDriver driver) {
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" +driver );
	}
	
	/*Payor Contact Manager Load Page*/
	public WebElement payorContManagerLoadPgTitleTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));
	}

	public WebElement payorLoadPayorTblTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='payorContactManagerForm']/div[2]/section/header/div/span/span")));
	}

	public WebElement payorContManagerLoadPyrIdInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pyrAbbrv")));
	}
	
	public WebElement payorContManagerLoadSearchPyrIdIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='payor_id_search']")));
	}
	
	public WebElement payorContManagerLoadPyrSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payor_contact_manager_load_payor_id']")));
	}
	
	/*Payor Contact Manager Detail*/
	/** Header **/
	public WebElement headerHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payor_contact_manager_header']")));
	}
	
	public WebElement headerRunAuditIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("auditBtn")));
	}
	
	public WebElement headerPyrIdTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pyrAbbrvHeader")));
	}
	
	public WebElement headerPyrNmTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pyrName")));
	}
	
	public WebElement headerGrpNmTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("grpName")));
	}
	
	public WebElement headerViewDocumentLnk(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("viewDocUrl")));
	}
	
	/** Contact Detail section **/
	public WebElement contactDetailSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payor_contact_manager_contact_detail']")));
	}	

	public WebElement contactDetailTblTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='mainSections']/div/div[2]/div/div/section/header/div[1]/span/span")));
	}

	public WebElement contactDetailTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_contactDetail")));
	}
	
	public WebElement contactDetailTblUserIdFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_userId")));
	}
	
	public WebElement contactDetailTblCntctDateFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_cntctDt")));
	}
	
	public WebElement contactDetailTblCntctInfoFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_cntctInfo")));
	}
	
	public WebElement contactDetailTblFollowUpDtFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_followUpDt")));
	}
	
	public WebElement contactDetailTblFollowUpPersonFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_followUpUserId")));
	}
	
	public WebElement contactDetailTblFollowUpCompleteFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_followUpUserId")));
	}
	
	public WebElement contactDetailTblVoidFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_void")));
	}
	
	public WebElement contactDetailTblCpyNoteToPyrFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_cpyNotePyrs")));
	}
	
	public WebElement contactDetailTblUserIdTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_contactDetail']//tr["+row+"]/td[@aria-describedby='tbl_contactDetail_userId']")));
	}
	
	public WebElement contactDetailTblCntctDateTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_contactDetail']//tr["+row+"]/td[@aria-describedby='tbl_contactDetail_cntctDt']")));
	}
	
	public WebElement contactDetailTblCntctInfoTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_contactDetail']//tr["+row+"]/td[@aria-describedby='tbl_contactDetail_cntctInfo']/div")));
	}
	
	public WebElement contactDetailTblFollowUpDtTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_contactDetail']//tr["+row+"]/td[@aria-describedby='tbl_contactDetail_followUpDt']")));
	}
	
	public WebElement contactDetailTblFollowUpPersonTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_contactDetail']//tr["+row+"]/td[@aria-describedby='tbl_contactDetail_followUpUserId']")));
	}
	
	public WebElement contactDetailTblFollowUpCompleteChk(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_contactDetail']//tr["+row+"]/td[@aria-describedby='tbl_contactDetail_followUpComplete']/input")));
	}
	
	public WebElement contactDetailTblVoidChk(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_contactDetail']//tr["+row+"]/td[@aria-describedby='tbl_contactDetail_void']/input")));
	}
	
	public WebElement contactDetailTblCpyNoteToPyrTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_contactDetail']//tr["+row+"]/td[@aria-describedby='tbl_contactDetail_cpyNotePyrs']/div")));
	}
	
	public WebElement contactDetailTblRownum(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_contactDetail']//tr["+row+"]")));
	}
	
	public WebElement contactDetailTblFisrtPageIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='first_tbl_contactDetail_pagernav']/span")));
	}
	
	public WebElement contactDetailTblPrevPageIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prev_tbl_contactDetail_pagernav']/span")));
	}
	
	public WebElement contactDetailTblNextPageIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_tbl_contactDetail_pagernav']/span")));
	}
	
	public WebElement contactDetailTblLastpageIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='last_tbl_contactDetail_pagernav']/div")));
	}
	
	public WebElement contactDetailTblTotalResultTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_contactDetail_pagernav_right']/div")));
	}
	
	/** Add and Edit Popup **/
	
	public WebElement contactDetailTblAddBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='add_tbl_contactDetail']/button")));
	}
	
	public WebElement contactDetailTblTitlePopupTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='edithdtbl_contactDetail']/span")));
	}
	
	public WebElement contactDetailTblEditBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='edit_tbl_contactDetail']/button")));
	}
	
	public WebElement userIdTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='TblGrid_tbl_contactDetail']//input[@id='userId']")));
	}
	
	public WebElement cntctDtInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='TblGrid_tbl_contactDetail']//input[@id='cntctDt']")));
	}
	
	public WebElement cntctInfoInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='TblGrid_tbl_contactDetail']//textarea[@id='cntctInfo']")));
	}
	
	public WebElement followUpDtInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='TblGrid_tbl_contactDetail']//input[@id='followUpDt']")));
	}
	
	public WebElement followUpPersonDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='TblGrid_tbl_contactDetail']//div[@id='s2id_followUpUserId']")));
	}
	
	public WebElement followUpCompleteChk(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='TblGrid_tbl_contactDetail']//input[@id='followUpComplete']")));
	}
	
	public WebElement voidChk(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='TblGrid_tbl_contactDetail']//input[@id='void']")));
	}
	
	public WebElement cpyNotePyrsInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='TblGrid_tbl_contactDetail']//input[@id='cpyNotePyrs']")));
	}
	
	public WebElement searchcpyNotePyrsIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='TblGrid_tbl_contactDetail']//*[@data-help-id='payor_id_search']")));
	}
	
	public WebElement okIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='Act_Buttons']//*[@id='sData']")));
	}
	
	public WebElement cancelIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='Act_Buttons']//*[@id='cData']")));
	}
	
	public WebElement formErrorMessageTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='TblGrid_tbl_contactDetail']//*[@id='FormError']")));
	}
	
	public WebElement contactDetailTblEditPopup(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("editmodtbl_contactDetail")));
	}
	
	/** Footer **/
	public WebElement footerHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payor_contact_manager_summary']")));
	}
	
	public WebElement resetBtn(){
		return wait.until(ExpectedConditions.elementToBeClickable(By.id("Reset")));
	}
	
	public WebElement saveAndClearBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}
	
	public WebElement confirmationDialogNameBtn(String nmBtn){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='']//span[text()='"+nmBtn+"']")));
	}
	
	public WebElement warningPopupWarningTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='confirmationDialog']//span")));
	}

	//--------------------------------------
	public void clickResetBtn() {
		resetBtn().click();
	}
}
