package com.overall.filemaint.specialPriceTable;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SpecialPriceTable {
	private WebDriverWait wait;
	protected Logger logger;
	public SpecialPriceTable(RemoteWebDriver driver) {
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName()+ "],[" + driver);
	}
	
	//******  Begin Load Special Price Table Page *******/
	public WebElement loadSpecialPriceTablePageTitleTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='line pageTitleContainer']//span[text()='Special Price Table']")));
	}
	
	public WebElement loadSpecialPriceTablePageHeaderHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_special_price_table_header']")));
	}
	
	public WebElement loadSpecialPriceTablePageSpePrcTblIdSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupPrcTblSrchBtn")));
	}
	
	public WebElement loadSpecialPriceTablePageDisplayOptionDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_option")));
	}
	
	public WebElement loadSpecialPriceTablePageDisplayOptionTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_option']//span[contains(@class,'select2-chosen')]")));
	}
	
	public WebElement loadSpecialPriceTablePageSpePrcTblIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupPrcTblId")));
	}
	
	public WebElement specialPriceTblSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-topic='Special Price Table']")));
	}
	//******  End Load Special Price Table Page *******/
	
	//******  Begin Header Of Detail Special Price Table Page*******/
	public WebElement specialPriceTablePageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fileMaintPriceTableForm']//span[text()='Special Price Table']")));
	}
	
	public WebElement specialPriceTablePageHeaderHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_special_price_table_header']")));
	}
	
	public WebElement specialPriceTablePageSpePrcTblIdTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prcTblId")));
	}
	
	public WebElement specialPriceTablePageAccountTypeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//input[@class='accounType keyField dataDisplay']")));
	}
	
	public WebElement specialPriceTablePageDisplayOptionInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//input[@class='keyField dataDisplay']")));
	}
	
	public WebElement specialPriceTablePageHeaderNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
	}
	
	public WebElement specialPriceTablePageFacilityDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_facility")));
	}
	
	public WebElement specialPriceTablePageFacilityTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_facility']//span[@class='select2-chosen']")));
	}
	
	//******  End Header Of Detail Special Price Table Page*******/
	
	//******  Begin Detail Special Price Table Page for a new Special Price Table ID *******/
	
	public WebElement specialPriceTablePageNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nameAddNew")));
	}
	
	public WebElement specialPriceTablePageFacilityNewDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_facilityAddNew")));
	}
	
	public WebElement specialPriceTablePageFacilityNewTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_facilityAddNew']//span[@class='select2-chosen']")));
	}
	
	public WebElement specialPriceTablePageClientRad() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("radioCln")));
	}
	
	public WebElement specialPriceTablePageNonClientRad() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("radioNonClient")));
	}
	
	public WebElement specialPriceTablePageImportCreateRad() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("radioImportCreate")));
	}
	
	public WebElement specialPriceTablePageTableIdRad() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tblIdSearchImport")));
	}
	
	public WebElement specialPriceTablePageChooseFileBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='addNewSection']//input[@value='Choose File']")));
	}
	
	public WebElement specialPriceTablePageFileInfoTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='addNewSection']//span[@class='fileInfo']")));
	}
	
	public WebElement specialPriceTableTableIdSearchIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prcTblSrchBtn")));
	}
	
	public WebElement specialPriceTableTableIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("importPrcTblId")));
	}
	
	public WebElement specialPriceTablePageImportEffDateInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("importEffDate")));
	}
	
	public WebElement specialPriceTablePageImportEffDateIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[@class='unit size30']//img[@title='Choose date']")));
	}
	
	public WebElement specialPriceTablePageAtImportDiscountInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("importDiscount")));
	}
	
	public WebElement specialPriceTablePageCreateWithEffDateInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createWithEffDt")));
	}
	
	public WebElement specialPriceTablePageCreateWithEffDateIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[@class='unit size25']//img[@title='Choose date']")));
	}
	//******  End Detail Special Price Table Page for a new Special Price Table ID *******/
	
	//******  Begin Detail Special Price Table Page for Old Special Price Table ID *******/
	public WebElement specialPriceTablePageErrorsReturnedText(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='sectionServerMessages']//ul[@class='serverErrorsList']/li")));
	}
	
	// Begin of Pricing Section
	
	public WebElement pricingSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_special_price_table_pricing']")));
	}
	
	public WebElement testCodeFilter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_testAbbrev")));
	}
	
	public WebElement nameFilter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_itemName")));
	}
	
	public WebElement effDateFilter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_effDate")));
	}
	
	public WebElement expDateFilter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_expDate")));
	}
	
	public WebElement retail$Filter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_retail")));
	}
	
	public WebElement current$Filter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_currentPrice")));
	}
	
	public WebElement new$Filter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_newPrice")));
	}
	
	public WebElement flatFeeProfileFilter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_flatFee")));
	}
	
	public WebElement discountFilter(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_disc")));
	}
	
	public WebElement pricingTbl(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_pricing")));
	}
	
	public WebElement pricingTblTestCodeColSearchIco(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_testAbbrev']//a")));
	}
	
	public WebElement pricingTblTestCodeColInput(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_testAbbrev']//input")));
	}
	
	public WebElement pricingTblNameColTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_itemName']")));
	}
	
	public WebElement pricingTblEffDateColTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_effDate']")));
	}
	
	public WebElement pricingTblEffDateColInput(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_effDate']//input")));
	} 
	
	public WebElement pricingTblExpDateColTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_expDate']")));
	}
	
	public WebElement pricingTblExpDateColInput(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_expDate']//input")));
	}
	
	public WebElement pricingTblRetailPriceColTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_retail']")));
	}
	
	public WebElement pricingTblCurrentPriceColTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_currentPrice']")));
	}
	
	public WebElement pricingTblNewPriceColTxt(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_newPrice']")));
	}
	
	public WebElement pricingTblNewPriceColInput(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_newPrice']//input")));
	}
	
	public WebElement pricingTblDeleteColChk(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_deleted']/input")));
	}
	
	public WebElement addBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_pricing_iladd")));
	}
	
	public WebElement tblFirstIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_tbl_pricing_pagernav")));
	}
	
	public WebElement tblPrevIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_tbl_pricing_pagernav")));
	}
	
	public WebElement tblNextIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_pricing_pagernav")));
	}
	
	public WebElement tblLastIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_tbl_pricing_pagernav")));
	}
	
	public WebElement totalRecordPricingTblTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_pricing_pagernav_right']/div")));
	}
	
	public WebElement viewClientsBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("viewClientsBtn")));
	}
	
	public WebElement viewPayorsBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("viewpayorsBtn")));
	}
	// End of Pricing Table
	
	// Append File Section
	
	public WebElement appendFileSectionHelpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_special_price_table_append_file']")));
	}
	
	public WebElement chooseFileBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='loadDataSection']//input[@value='Choose File']")));

	}
	
	public WebElement fileInfoTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='loadDataSection']//span[@class='fileInfo']")));
	}
	
	public WebElement appendBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("appendFileBtn")));		
	}
	
	public WebElement appendFileInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("appendFile")));
	}
	//-- End Append File Section
		
	//******  End Detail Special Price Table Page for Old Special Price Table ID *******/
	
	//******  Begin Bottom Detail Special Price Table Page*******/
	public WebElement bottomHelpLinkIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
	
	public WebElement showClipboardIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@ title='Show Clipboard']")));
	}
	
	public WebElement showKeyboardShortcutsIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@ title='Show Keyboard Shortcuts']")));
	}
	
	public WebElement resetBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}
	
	public WebElement saveBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSave")));
	}
	
	public WebElement saveAndClearBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}
	//******  End Bottom Detail Special Price Table Page*******/
	
	/*-- Begin Warming pop up **/
	public WebElement warmingPopupTitleTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='ui-dialog-title'][text()='Warning']")));		
	}
	
	public WebElement warningMessageTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='confirmationDialog']//div[@class='unit lastUnit']//span")));		
	}
	
	public WebElement warmingCancelBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//button[contains(@class,'lockDownOnSubmit')]//span[text()='Cancel']")));		
	}
	
	public WebElement warmingResetBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//button[contains(@class,'lockDownOnSubmit')]//span[text()='Reset']")));		
	}
	/*-- End Warming pop up **/
	public WebElement priceTableIDViewPayorPopup(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clnPyrTblID")));
	}
	public WebElement priceTableNameViewPayorPopup(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clnPyrTblDescr")));
	}
	
}
