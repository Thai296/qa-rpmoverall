package com.overall.fileMaintenance.pricing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class SpecialPriceTable {
    private RemoteWebDriver driver;
    protected Logger logger;
    private WebDriverWait wait;

    public SpecialPriceTable(RemoteWebDriver driver, WebDriverWait wait){
        this.driver = driver;
        this.wait=wait;
        logger = Logger.getLogger(this.getClass().getName()+ "],[" + driver);
    }

    //******  Begin Load Special Price Table Page *******/
    public WebElement loadSpecialPriceTablePageTitleTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='line pageTitleContainer']//span[text()='Special Price Table']")));
    }

    public WebElement loadSpecialPriceTablePageHeaderHelpIco() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_special_price_table_header']"));
    }

    public WebElement helpIconBtnInSpecialPriceTableInformation() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_special_price_table_special_price_table_info']"));
    }

    public WebElement helpIconBtnInLoadSpecialPriceTable() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_special_price_table_load_special_price_table']"));
    }

    public WebElement loadSpecialPriceTablePageSpePrcTblIdSearchIco() {
        return driver.findElement(By.id("lookupPrcTblSrchBtn"));
    }

    public WebElement loadSpecialPriceTablePageDisplayOptionDdl() {
        return driver.findElement(By.id("s2id_option"));
    }

    public WebElement loadSpecialPriceTablePageDisplayOptionTxt() {
        return driver.findElement(By.xpath(".//*[@id='s2id_option']//span[contains(@class,'select2-chosen')]"));
    }

    public WebElement loadSpecialPriceTablePageSpePrcTblIdInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupPrcTblId")));
    }

    public WebElement specialPriceTblSectionHelpIco() {
        return driver.findElement(By.xpath(".//*[@data-help-topic='Special Price Table']"));
    }
    //******  End Load Special Price Table Page *******/

    //******  Begin Header Of Detail Special Price Table Page*******/
    public WebElement specialPriceTablePageTitle() {
        return driver.findElement(By.xpath(".//*[@id='fileMaintPriceTableForm']//span[text()='Special Price Table']"));
    }

    public WebElement specialPriceTablePageHeaderHelpIco() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_special_price_table_header']"));
    }

    public WebElement specialPriceTablePageSpePrcTblIdTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prcTblId")));
    }

    public WebElement specialPriceTablePageAccountTypeInput() {
        return driver.findElement(By.xpath(".//input[@class='accounType keyField dataDisplay']"));
    }

    public WebElement accountTypeTxt() {
        return driver.findElement(By.xpath(".//*[@class='fieldsForLoading']//input[contains(@class,'accounType')]"));
    }

    public WebElement facilityChoosenTxt() {
        return driver.findElement(By.xpath(".//*[@id='s2id_facility']//span[contains(@class,'select2-chosen')]"));
    }

    public WebElement displayOptionTxt() {
        return driver.findElement(By.xpath(".//*[@id='optionDisplay']//following-sibling::input"));
    }

    public WebElement specialPriceTablePageDisplayOptionInput() {
        return driver.findElement(By.xpath(".//input[@class='keyField dataDisplay']"));
    }

    public WebElement specialPriceTablePageHeaderNameInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
    }

    public WebElement specialPriceTablePageFacilityDdl() {
        return driver.findElement(By.id("s2id_facility"));
    }

    public WebElement specialPriceTablePageFacilityTxt() {
        return driver.findElement(By.xpath(".//*[@id='s2id_facility']//span[@class='select2-chosen']"));
    }

    //******  End Header Of Detail Special Price Table Page*******/

    //******  Begin Detail Special Price Table Page for a new Special Price Table ID *******/

    public WebElement specialPriceTablePageNameInput() {
        return driver.findElement(By.id("nameAddNew"));
    }

    public WebElement facilityDropdownInSpecialPriceTableInformation(){
        return driver.findElement(By.id("facilityAddNew"));
    }

    public WebElement specialPriceTablePageFacilityNewDdl() {
        return driver.findElement(By.id("s2id_facilityAddNew"));
    }

    public WebElement specialPriceTablePageFacilityNewTxt() {
        return driver.findElement(By.xpath(".//*[@id='s2id_facilityAddNew']//span[@class='select2-chosen']"));
    }

    public WebElement specialPriceTablePageClientRad() {
        return driver.findElement(By.id("radioCln"));
    }

    public WebElement specialPriceTablePageNonClientRad() {
        return driver.findElement(By.id("radioNonClient"));
    }

    public WebElement specialPriceTablePageImportCreateRad() {
        return driver.findElement(By.id("radioImportCreate"));
    }

    public WebElement chooseFileBtnInSpecialPriceTableInformation(){
        return driver.findElement(By.id("importFile"));
    }

    public WebElement specialPriceTablePageTableIdRad() {
        return driver.findElement(By.id("tblIdSearchImport"));
    }

    public WebElement specialPriceTablePageChooseFileBtn() {
        return driver.findElement(By.xpath(".//*[@class='addNewSection']//input[@value='Choose File']"));
    }

    public WebElement specialPriceTablePageFileInfoTxt() {
        return driver.findElement(By.xpath(".//*[@class='addNewSection']//span[@class='fileInfo']"));
    }

    public WebElement specialPriceTableTableIdSearchIco() {
        return driver.findElement(By.id("prcTblSrchBtn"));
    }

    public WebElement specialPriceTableTableIdInput() {
        return driver.findElement(By.id("importPrcTblId"));
    }

    public WebElement specialPriceTablePageImportEffDateInput() {
        return driver.findElement(By.id("importEffDate"));
    }

    public WebElement specialPriceTablePageImportEffDateIco() {
        return driver.findElement(By.xpath(".//div[@class='unit size30']//img[@title='Choose date']"));
    }

    public WebElement specialPriceTablePageAtImportDiscountInput() {
        return driver.findElement(By.id("importDiscount"));
    }

    public WebElement specialPriceTablePageCreateWithEffDateInput() {
        return driver.findElement(By.id("createWithEffDt"));
    }

    public WebElement specialPriceTablePageCreateWithEffDateIco() {
        return driver.findElement(By.xpath(".//div[@class='unit size25']//img[@title='Choose date']"));
    }
    //******  End Detail Special Price Table Page for a new Special Price Table ID *******/

    //******  Begin Detail Special Price Table Page for Old Special Price Table ID *******/
    public WebElement specialPriceTablePageErrorsReturnedText(){
        return driver.findElement(By.xpath(".//*[@id='sectionServerMessages']//ul[@class='serverErrorsList']/li"));
    }

    // Begin of Pricing Section

    public WebElement pricingSectionHelpIcon(){
        return driver.findElement(By.xpath(".//*[@data-help-id='p_special_price_table_pricing']"));
    }

    public WebElement testCodeFilter(){
        return driver.findElement(By.id("gs_testAbbrev"));
    }

    public WebElement nameFilter(){
        return driver.findElement(By.id("gs_itemName"));
    }

    public WebElement effDateFilter(){
        return driver.findElement(By.id("gs_effDate"));
    }

    public WebElement expDateFilter(){
        return driver.findElement(By.id("gs_expDate"));
    }

    public WebElement retail$Filter(){
        return driver.findElement(By.id("gs_retail"));
    }

    public WebElement current$Filter(){
        return driver.findElement(By.id("gs_currentPrice"));
    }

    public WebElement new$Filter(){
        return driver.findElement(By.id("gs_newPrice"));
    }

    public WebElement flatFeeProfileFilter(){
        return driver.findElement(By.id("gs_flatFee"));
    }

    public WebElement discountFilter(){
        return driver.findElement(By.id("gs_disc"));
    }

    public WebElement pricingTbl(){
        return driver.findElement(By.id("tbl_pricing"));
    }

    public WebElement pricingTblTestCodeColSearchIco(String row){
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_testAbbrev']//a"));
    }

    public WebElement testCodeLnkInPricingTbl(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr["+row+"]/td[@aria-describedby='tbl_pricing_testAbbrev']/a"));
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
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_retail']"));
    }

    public WebElement pricingTblCurrentPriceColTxt(String row){
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_currentPrice']"));
    }

    public WebElement pricingTblNewPriceColTxt(String row){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_newPrice']")));
    }

    public WebElement pricingTblNewPriceColInput(String row){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_newPrice']//input")));
    }

    public WebElement pricingTblDeleteColChk(String row){
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']//tr["+ row +"]/td[@aria-describedby='tbl_pricing_deleted']/input"));
    }

    public WebElement addBtn(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_pricing_iladd")));
    }

    public WebElement tblFirstIco(){
        return driver.findElement(By.id("first_tbl_pricing_pagernav"));
    }

    public WebElement tblPrevIco(){
        return driver.findElement(By.id("prev_tbl_pricing_pagernav"));
    }

    public WebElement tblNextIco(){
        return driver.findElement(By.id("next_tbl_pricing_pagernav"));
    }

    public WebElement tblLastIco(){
        return driver.findElement(By.id("last_tbl_pricing_pagernav"));
    }

    public WebElement totalRecordPricingTblTxt(){
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing_pagernav_right']/div"));
    }

    public WebElement viewClientsBtn(){
        return driver.findElement(By.id("viewClientsBtn"));
    }

    public WebElement viewPayorsBtn(){
        return driver.findElement(By.id("viewpayorsBtn"));
    }
    // End of Pricing Table

    // Append File Section

    public WebElement appendFileSectionHelpIcon(){
        return driver.findElement(By.xpath(".//*[@data-help-id='p_special_price_table_append_file']"));
    }

    public WebElement chooseFileBtn(){
        return driver.findElement(By.xpath(".//*[@class='loadDataSection']//input[@value='Choose File']"));

    }

    public WebElement fileInfoTxt(){
        return driver.findElement(By.xpath(".//*[@class='loadDataSection']//span[@class='fileInfo']"));
    }

    public WebElement appendBtn(){
        return driver.findElement(By.id("appendFileBtn"));
    }

    public WebElement chooseFileBtnInAppendFile(){
        return driver.findElement(By.id("appendFile"));
    }
    //-- End Append File Section

    //******  End Detail Special Price Table Page for Old Special Price Table ID *******/

    //******  Begin Bottom Detail Special Price Table Page*******/
    public WebElement bottomHelpLinkIco(){
        return driver.findElement(By.id("pageHelpLink"));
    }

    public WebElement showClipboardIco(){
        return driver.findElement(By.xpath(".//*[@ title='Show Clipboard']"));
    }

    public WebElement showKeyboardShortcutsIco(){
        return driver.findElement(By.xpath(".//*[@ title='Show Keyboard Shortcuts']"));
    }

    public WebElement resetBtn(){
        return driver.findElement(By.id("Reset"));
    }

    public WebElement saveBtn(){
        return driver.findElement(By.id("btnSave"));
    }

    public WebElement saveAndClearBtn(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
    }
    //******  End Bottom Detail Special Price Table Page*******/

    /*-- Begin Warming pop up **/
    public WebElement warmingPopupTitleTxt(){
        return driver.findElement(By.xpath(".//*[@class='ui-dialog-title'][text()='Warning']"));
    }

    public WebElement warningMessageTxt(){
        return driver.findElement(By.xpath(".//*[@id='confirmationDialog']//div[@class='unit lastUnit']//span"));
    }

    public WebElement warmingCancelBtn(){
        return driver.findElement(By.xpath(".//button[contains(@class,'lockDownOnSubmit')]//span[text()='Cancel']"));
    }

    public WebElement warmingResetBtn(){
        return driver.findElement(By.xpath(".//button[contains(@class,'lockDownOnSubmit')]//span[text()='Reset']"));
    }
    /*-- End Warming pop up **/
    public WebElement priceTableIDViewPayorPopup(){
        return driver.findElement(By.id("clnPyrTblID"));
    }
    public WebElement priceTableNameViewPayorPopup(){
        return driver.findElement(By.id("clnPyrTblDescr"));
    }

    //View Client/Payor Search Results
    public WebElement clientPayorPricingTable() {
        return driver.findElement(By.id("tbl_clnPyrPrcs"));
    }

    public WebElement priceTableId() {
        return driver.findElement(By.id("clnPyrTblID"));
    }

    public WebElement helpIconBtnInClientSearchResults(){
        return driver.findElement(By.xpath(".//*[@data-help-id='p_special_price_table_client_pricing']"));
    }

    public WebElement helpIconBtnInPayorSearchResults(){
        return driver.findElement(By.xpath(".//*[@data-help-id='p_fee_schedule_payor_pricing']"));
    }

    public WebElement deleteCheckboxInPricingTbl(String row)  {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr["+row+"]/td[@aria-describedby='tbl_pricing_deleted']/input"));
    }
    
    
    public WebElement changeSpecialPriceTblIDButton() {
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("changePrcTblId")));
    }
    
    public WebElement changeSpecialPriceTblIDPopupTitle() {
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@aria-describedby='changePrcTblIdDialog']//*[@class='ui-dialog-title']")));
    }
    
    public WebElement currentChangeSpecialPriceTblID() {
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prcTblIdDialog")));
    }
    
    public WebElement newChangeSpecialPriceTblID() {
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("newPrcTblId")));
    }
    
    public WebElement validationMessageInChangeSpecialPriceTblIDPopup() {
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div#messagefor_newPrcTblId>div.xf_message_content")));
    }
    
    public WebElement closeValidationMessageInChangeSpecialPriceTblIDPopup() {
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div#messagefor_newPrcTblId a.xf_message_close")));
    }
    
    public WebElement okButtonInChangeSpecialPriceTblIDPopup(){
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='OK']")));
    }
    
    public List<WebElement> clientPayorPricingTableRow() {
    	return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='tbl_clnPyrPrcs']//tr")));
    }
    
    /**
     * Follow up with below attributes
     * abbrev
     * name
     * effDt
     */
    public WebElement componentClientPricingTableByRowAndAttribute(int rowIndex, String component) {
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_clnPyrPrcs']//tr[" + rowIndex + "]/td[@aria-describedby='tbl_clnPyrPrcs_" + component + "']")));
    }
    
    public WebElement closeButton() {
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button.btn_close")));
    }
    
    public WebElement errorsReturnedPopupTitle() {
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.serverErrorsTitle")));
    }
    
    public WebElement retroactivePriceImpactGridTitle() {
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_priceTableRetro']//span[contains(@class,'title')]")));
    }
    
    public List<WebElement> retroactivePriceImpactTblRow() {
    	return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='tbl_priceTableRetro']//tr")));
    }
    
    public WebElement retroactivePriceImpactTblRecordByRowAndAttribute(int row, String attribute) {
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_priceTableRetro']//tr[" + row + "]//td[@aria-describedby='tbl_priceTableRetro_" + attribute + "']")));
    }
    
    public WebElement retroactivePriceImpactTblRecordByRowAndAttribute(String row, String attribute) {
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_priceTableRetro']//tr[" + row + "]//td[@aria-describedby='tbl_priceTableRetro_" + attribute + "']")));
    }
    
    public WebElement totalEstimatedImpact() {
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.totalEstimatedImpact")));
    }
}
