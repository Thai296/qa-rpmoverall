package com.overall.client.clientProcessing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PayorExclusions {
	private WebDriverWait wait;
	protected Logger logger;
	public PayorExclusions(RemoteWebDriver driver){
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}

	/* Payor Exclusions Load Page */
	public WebElement payorExclusionsLoadPgTitleTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));
	}

	public WebElement payorExclusionsLoadClientSection(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,'sectionClientPayorExclusion')]")));
	}

	public WebElement payorExclusionsLoadClientIdInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupClientAbbrev")));
	}

	public WebElement payorExclusionsLoadClientIdSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupClientSrchBtn")));
	}

	public WebElement payorExclusionsLoadClientSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payor_exclusions_load_client_id']")));
	}

	/* Payor Exclusions Detail */
	/*** Header
	 * @return
	 * @
	 */
	public WebElement headerHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payor_exclusions_header']")));
	}

	public WebElement headerClientIdTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clnAbbrev")));
	}

	public WebElement headerClientNameTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clnName")));
	}

	public WebElement headerAccountTypTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clnAccntTyp")));
	}

	public WebElement headerViewDocumentLnk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='viewDocumentHeaderInfo']//a[@class='viewDocumentLabel']")));
	}

	public WebElement headerViewClnPortalDocUpDocumentLnk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='viewDocumentHeaderInfo']//a[@class='viewOrganizationDocumentLabel']")));
	}

	public WebElement headerFacAbbrevTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='facilityHeaderInfo']//span[contains(@class,'clientOrderingFacAbbrev')]")));
	}

	public WebElement headerFacNameTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='facilityHeaderInfo']//span[contains(@class,'clientOrderingFacName')]")));
	}

	/**Payor Exclusion Section
	 * @return
	 * @
	 */
	public WebElement payorExclusionSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payor_exclusions_payor_exclusion']")));
	}

	public WebElement payorExclusionTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_clnPyrExclsTable")));
	}

	public WebElement payorExclusionTblEffDateFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_pyrEffDt")));
	}

	public WebElement payorExclusionTblExpDateFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_pyrExpDt")));
	}

	public WebElement payorExclusionTblPayorIdFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_pyrAbbrv")));
	}

	public WebElement payorExclusionTblPayorNameFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_pyrName")));
	}

	public WebElement payorExclusionTblRow(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrExclsTable']//tr["+row+"]")));
	}

	public WebElement payorExclusionTblEffDateTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrExclsTable']//tr["+row+"]/td[@aria-describedby='tbl_clnPyrExclsTable_pyrEffDt']")));
	}

	public WebElement payorExclusionTblExpDateTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrExclsTable']//tr["+row+"]/td[@aria-describedby='tbl_clnPyrExclsTable_pyrExpDt']")));
	}

	public WebElement payorExclusionTblPayorIdTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrExclsTable']//tr["+row+"]/td[@aria-describedby='tbl_clnPyrExclsTable_pyrAbbrv']")));
	}

	public WebElement payorExclusionTblPayorNameTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrExclsTable']//tr["+row+"]/td[@aria-describedby='tbl_clnPyrExclsTable_pyrName']")));
	}

	public WebElement payorExclusionTblDeletedChk(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrExclsTable']//tr["+row+"]/td[@aria-describedby='tbl_clnPyrExclsTable_deleted']/input")));
	}

	public WebElement payorExclusionTblEffDateInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrExclsTable']//tr/td[@aria-describedby='tbl_clnPyrExclsTable_pyrEffDt']/input")));
	}

	public WebElement payorExclusionTblExpDateInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrExclsTable']//tr/td[@aria-describedby='tbl_clnPyrExclsTable_pyrExpDt']/input")));
	}

	public WebElement payorExclusionTblPayorIdInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrExclsTable']//tr/td[@aria-describedby='tbl_clnPyrExclsTable_pyrAbbrv']/input")));
	}

	public WebElement payorExclusionTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_clnPyrExclsTable_iladd")));
	}

	public WebElement payorExclusionTblTotalRecordsTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clnPyrExclsTable_pagernav_right']/div")));
	}

	public WebElement payorExclusionTblNextPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_clnPyrExclsTable_pagernav")));
	}

	public WebElement payorExclusionTblLastPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_tbl_clnPyrExclsTable_pagernav")));
	}

	public WebElement payorExclusionTblFirstPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_tbl_clnPyrExclsTable_pagernav")));
	}

	public WebElement payorExclusionTblPreviousPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_tbl_clnPyrExclsTable_pagernav")));
	}

	public WebElement payorExclusionTblPageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrExclsTable_pagernav_center']//input[@class='ui-pg-input']")));
	}

	public WebElement payorExclusionTblRownumSel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrExclsTable_pagernav_center']//select[@class='ui-pg-selbox']")));
	}

	/**Payor Group Exclusion Section
	 * @return
	 * @
	 */
	public WebElement payorGrpExclusionSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payor_exclusions_payor_group_exclusion']")));
	}

	public WebElement payorGrpExclusionTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_clnPyrGrpExclsTable")));
	}

	public WebElement payorGrpExclusionTblEffDateFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_pyrGrpEffDt")));
	}

	public WebElement payorGrpExclusionTblExpDateFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_pyrGrpExpDt")));
	}

	public WebElement payorGrpExclusionTblGroupNameFilterInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_pyrGrpId")));
	}

	public WebElement payorGrpExclusionTblRow(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrGrpExclsTable']//tr["+row+"]")));
	}

	public WebElement payorGrpExclusionTblEffDateTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrGrpExclsTable']//tr["+row+"]/td[@aria-describedby='tbl_clnPyrGrpExclsTable_pyrGrpEffDt']")));
	}

	public WebElement payorGrpExclusionTblExpDateTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrGrpExclsTable']//tr["+row+"]/td[@aria-describedby='tbl_clnPyrGrpExclsTable_pyrGrpExpDt']")));
	}

	public WebElement payorGrpExclusionTblGroupNameTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrGrpExclsTable']//tr["+row+"]/td[@aria-describedby='tbl_clnPyrGrpExclsTable_pyrGrpId']")));
	}

	public WebElement payorGrpExclusionTblDeletedChk(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrGrpExclsTable']//tr["+row+"]/td[@aria-describedby='tbl_clnPyrGrpExclsTable_deleted']/input")));
	}

	public WebElement payorGrpExclusionTblEffDateInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrGrpExclsTable']//tr/td[@aria-describedby='tbl_clnPyrGrpExclsTable_pyrGrpEffDt']/input")));
	}

	public WebElement payorGrpExclusionTblExpDateInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrGrpExclsTable']//tr/td[@aria-describedby='tbl_clnPyrGrpExclsTable_pyrGrpExpDt']/input")));
	}

	public WebElement payorGrpExclusionTblGroupNameDdl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrGrpExclsTable']//tr/td[@aria-describedby='tbl_clnPyrGrpExclsTable_pyrGrpId']/div[contains(@id,'s2id')]")));
	}

	public WebElement payorGrpExclusionTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_clnPyrGrpExclsTable_iladd")));
	}

	public WebElement payorGrpExclusionTblTotalRecordsTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clnPyrGrpExclsTable_pagernav_right']/div")));
	}

	public WebElement payorGrpExclusionTblNextPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_clnPyrGrpExclsTable_pagernav")));
	}

	public WebElement payorGrpExclusionTblLastPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_tbl_clnPyrGrpExclsTable_pagernav")));
	}

	public WebElement payorGrpExclusionTblFirstPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_tbl_clnPyrGrpExclsTable_pagernav")));
	}

	public WebElement payorGrpExclusionTblPreviousPageIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_tbl_clnPyrGrpExclsTable_pagernav")));
	}

	public WebElement payorGrpExclusionTblPageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrGrpExclsTable_pagernav_center']//input[@class='ui-pg-input']")));
	}

	public WebElement payorGrpExclusionTblRownumSel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrGrpExclsTable_pagernav_center']//select[@class='ui-pg-selbox']")));
	}

	/*** Footer section
	 * @return
	 * @
	 */
	public WebElement footerHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}

	public WebElement footerResetBtn()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}

	public WebElement footerSaveAndClearBtn()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}

	public WebElement warningPopupMessageTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='confirmationDialog']//span")));
	}

	public WebElement warningPopupResetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@aria-describedby='confirmationDialog']//button[contains(@class,'btn_submit')]")));
	}

}
