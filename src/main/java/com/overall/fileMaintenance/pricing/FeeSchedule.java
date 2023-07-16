package com.overall.fileMaintenance.pricing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FeeSchedule {
    private final RemoteWebDriver driver;
    protected final Logger logger;
    private final WebDriverWait wait;

    public FeeSchedule(RemoteWebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }

    /*--Load Fee Schedule Page*/
    public WebElement feeSchedulePageTitle() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));
    }

    public WebElement helpIconBtnInHeader() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_fee_schedule_header']"));
    }

    public WebElement displayOptionDropdown() {
        return driver.findElement(By.id("s2id_option"));
    }

    public WebElement loadFeeScheduleDisplayOptionChoosenTxt() {
        return driver.findElement(By.xpath(".//*[@id='s2id_option']//span[contains(@class,'select2-chosen')]"));
    }

    public WebElement loadFeeScheduleFeeScheduleIdSearchIco() {
        return driver.findElement(By.id("lookupPrcTblSrchBtn"));
    }

    public WebElement feeScheduleIdInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupPrcTblId")));
    }

    public WebElement helpIconBtnInLoadFeeSchedule() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_fee_schedule_load_fee_schedule']"));
    }

    public WebElement helpIconBtnInFeeScheduleInformation() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_fee_schedule_fee_schedule_info']"));
    }

    /*--End Load Fee Schedule Page*/
    /*--Detail Fee Schedule Page*/
    //Header Fee Schedule section
    public WebElement feeScheduleIdTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prcTblId")));
    }

    public WebElement accountTypeTxt() {
        return driver.findElement(By.xpath(".//*[@class='fieldsForLoading']//input[contains(@class,'accounType')]"));
    }

    public WebElement basisTypeTxt() {
        return driver.findElement(By.xpath(".//*[@class='fieldsForLoading']//input[contains(@class,'basisType')]"));
    }

    public WebElement displayOptionTxt() {
        return driver.findElement(By.xpath(".//*[@id='optionDisplay']//following-sibling::input"));
    }

    public WebElement nameInput() {
        return driver.findElement(By.id("name"));
    }

    public WebElement facilityDdl() {
        return driver.findElement(By.xpath(".//*[@id='s2id_facility']"));
    }

    public WebElement facilityChoosenTxt() {
        return driver.findElement(By.xpath(".//*[@id='s2id_facility']//span[contains(@class,'select2-chosen')]"));
    }

    public WebElement priceTypeTxt() {
        return driver.findElement(By.xpath(".//*[@class='fieldsForLoading']//input[contains(@class,'priceType')]"));
    }

    public WebElement discountChk() {
        return driver.findElement(By.id("discount"));
    }
    //End Header Fee Schedule section

    //Pricing Section
    public WebElement helpIconBtnInPricingSection() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_fee_schedule_pricing']"));
    }

    public WebElement pricingTblTestCdFilter() {
        return driver.findElement(By.id("gs_testAbbrev"));
    }

    public WebElement pricingTblNameFilter() {
        return driver.findElement(By.id("gs_itemName"));
    }

    public WebElement pricingTblEffDateFilter() {
        return driver.findElement(By.id("gs_effDate"));
    }

    public WebElement pricingTblExpDateFilter() {
        return driver.findElement(By.id("gs_expDate"));
    }

    public WebElement pricingTblRetail$Filter() {
        return driver.findElement(By.id("gs_retail"));
    }

    public WebElement pricingTblCurrent$Filter() {
        return driver.findElement(By.id("gs_currentPrice"));
    }

    public WebElement pricingTblNew$Filter() {
        return driver.findElement(By.id("gs_newPrice"));
    }

    public WebElement pricingTblFlatFeeProfileFilter() {
        return driver.findElement(By.id("gs_flatFee"));
    }

    public WebElement pricingTbl() {
        return driver.findElement(By.id("tbl_pricing"));
    }

    public WebElement pricingTblSearchTestCdIco(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_testAbbrev']//span"));
    }

    public WebElement testCodeInputInPricingTbl(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_testAbbrev']//input"));
    }

    public WebElement procedureCodeInputInPricingTbl(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_procId']/input"));
    }

    public WebElement testCodeLnkInPricingTbl(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_testAbbrev']/a"));
    }

    public WebElement procedureCodeLnkInPricingTbl(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_procId']/a"));
    }

    public WebElement nameTxtInPricingTbl(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_itemName']"));
    }

    public WebElement effectiveDateTxtInPricingTbl(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_effDate']"));
    }

    public WebElement effectiveDateInputInPricingTbl(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_effDate']/input"));
    }

    public WebElement expirationDateTxtInPricingTbl(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_expDate']"));
    }

    public WebElement expirationDateInputInPricingTbl(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_expDate']/input"));
    }

    public WebElement retailTxtInPricingTbl(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_retail']"));
    }

    public WebElement currentTxtInPricingTbl(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_currentPrice']"));
    }

    public WebElement newPriceTxtInPricingTbl(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_newPrice']"));
    }

    public WebElement newPriceInputInPricingTbl(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_newPrice']/input"));
    }

    public WebElement modDropdownInPricingTbl(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_procModifier']/select"));
    }

    public WebElement modDropdownTxtInPricingTbl(String row) {
        //return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr["+row+"]/td[@aria-describedby='tbl_pricing_procModifier']/div/a/span[1]"));
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_procModifier']"));
    }

    public WebElement pricingTblFlatFeeProfileChk(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_flatFee']/input"));
    }

    public WebElement deleteCheckboxInPricingTbl(String row) {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing']/tbody/tr[" + row + "]/td[@aria-describedby='tbl_pricing_deleted']/input"));
    }

    public WebElement pricingTblRow(String row) {
        return driver.findElement(By.xpath("//*[@id='tbl_pricing']/tbody/tr[" + row + "]"));
    }

    public WebElement addBtn() {
        return driver.findElement(By.id("tbl_pricing_iladd"));
    }

    public WebElement tblFirstIco() {
        return driver.findElement(By.id("first_tbl_pricing_pagernav"));
    }

    public WebElement tblPrevIco() {
        return driver.findElement(By.id("prev_tbl_pricing_pagernav"));
    }

    public WebElement tblNextIco() {
        return driver.findElement(By.id("next_tbl_pricing_pagernav"));
    }

    public WebElement tblLastIco() {
        return driver.findElement(By.id("last_tbl_pricing_pagernav"));
    }

    public WebElement totalRecordPricingTblTxt() {
        return driver.findElement(By.xpath(".//*[@id='tbl_pricing_pagernav_right']/div"));
    }

    public WebElement viewClientsBtn() {
        return driver.findElement(By.id("viewClientsBtn"));
    }

    public WebElement viewPayorsBtn() {
        return driver.findElement(By.id("viewpayorsBtn"));
    }
    //--End Pricing Section

    // Append File Section

    public WebElement helpIconBtnInAppendFileSection() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_fee_schedule_append_file']"));
    }

    public WebElement chooseFileBtnInAppendFile() {
        return driver.findElement(By.id("appendFile"));
    }

    public WebElement fileInfoTxt() {
        return driver.findElement(By.xpath(".//*[@data-section-name='appendFileBlockGroup']//div[@class='fakeFileInput']//span"));
    }

    public WebElement appendBtn() {
        return driver.findElement(By.id("appendFileBtn"));
    }

    public WebElement appendFileInput() {
        return driver.findElement(By.id("appendFile"));
    }

    //-- End Append File Section
    //Bottom
    public WebElement bottomHelpLinkIco() {
        return driver.findElement(By.id("pageHelpLink"));

    }

    public WebElement showClipboardIco() {
        return driver.findElement(By.xpath(".//*[@ title='Show Clipboard']"));

    }

    public WebElement showKeyboardShortcutsIco() {
        return driver.findElement(By.xpath(".//*[@ title='Show Keyboard Shortcuts']"));

    }

    public WebElement resetBtn() {
        return driver.findElement(By.id("Reset"));

    }

    public WebElement saveAndClearBtn() {
        return driver.findElement(By.id("btnSaveAndClear"));

    }
    //-- End Append File Section
    //End Bottom
    /*--End Detail Fee Schedule Page*/

    /*-- New Detail Fee Schedule Page */
    public WebElement discountableChk() {
        return driver.findElement(By.id("discount"));
    }

    public WebElement fsInfGrpTitle() {
        return driver.findElement(By.xpath(".//*[@data-section-name='addNewBlockGroup']//span[@class='titleText']"));
    }

    public WebElement fsInfGrpHelpIco() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_price_table_add_new_fee_schedule']"));
    }

    public WebElement nameInputInFeeScheduleInformation() {
        return driver.findElement(By.id("nameAddNew"));
    }

    public WebElement fsInfGrpFacilityDdl() {
        return driver.findElement(By.xpath(".//*[@id='s2id_facilityAddNew']"));
    }

    public WebElement fsInfGrpFacilityChoosenTxt() {
        return driver.findElement(By.xpath(".//*[@id='s2id_facilityAddNew']//span[@class='select2-chosen']"));
    }

    public WebElement clientRadioBtnInFeeScheduleInformation() {
        return driver.findElement(By.id("radioCln"));
    }

    public WebElement nonClientRadioBtnInFeeScheduleInformation() {
        return driver.findElement(By.id("radioNonClient"));
    }

    public WebElement expectRadioBtnInFeeScheduleInformation() {
        return driver.findElement(By.id("radioPrcTypExpect"));
    }

    public WebElement retailRadioBtnInFeeScheduleInformation() {
        return driver.findElement(By.id("radioPrcTypRetail"));
    }

    public WebElement normalRadioBtnInFeeScheduleInformation() {
        return driver.findElement(By.id("radioPrcTypNormal"));
    }

    public WebElement testCodeRadioBtnInFeeScheduleInformation() {
        return driver.findElement(By.id("radioBscTypeTest"));
    }

    public WebElement ProcedureCodeRadioBtnInFeeScheduleInformation() {
        return driver.findElement(By.id("radioBscTypProc"));
    }

    public WebElement importFileRadioBtnInFeeScheduleInformation() {
        return driver.findElement(By.id("radioImportCreate"));
    }

    public WebElement chooseFileBtnInFeeScheduleInformation() {
        return driver.findElement(By.id("importFile"));
    }

    public WebElement tableIdRadioBtnInFeeScheduleInformation() {
        return driver.findElement(By.id("radioTblIdSearch"));
    }

    public WebElement fsInfGrpTableIdSearchBtn() {
        return driver.findElement(By.id("prcTblSrchBtn"));
    }

    public WebElement tableIdInputInFeeScheduleInformation() {
        return driver.findElement(By.id("importPrcTblId"));
    }

    //field : At %
    public WebElement fsInfGrpDiscountInput() {
        return driver.findElement(By.id("importDiscount"));
    }

    public WebElement effectiveInputInFeeScheduleInformation() {
        return driver.findElement(By.id("importEffDate"));
    }

    public WebElement createWithEffectiveDateInputInFeeScheduleInformation() {
        return driver.findElement(By.id("createWithEffDt"));
    }

    public WebElement rvsTableRadioBtn() {
        return driver.findElement(By.id("radioImportRvsTbl"));
    }

    public WebElement rvsTableDropdown() {
        return driver.findElement(By.id("importTableRvs"));
        //return driver.findElement(By.xpath(".//*[@id='s2id_importTableRvs']"));
    }

    public WebElement perUnitInput() {
        return driver.findElement(By.id("importRvsDollar"));
    }

    public WebElement saveBtn() {
        return driver.findElement(By.id("btnSave"));
    }
    /*-- End New Detail Fee Schedule Page */

    /*-- New Detail Schedule Page */

    public WebElement feeScheduleTitle(int index) {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"titleText\"]')[" + index + "]");
    }

    public WebElement facilityDropdownInFeeScheduleInformation() {
        return driver.findElement(By.id("facilityAddNew"));
    }

    public WebElement primaryPayorInput() {
        return driver.findElement(By.id("payorAbbrev"));
    }

    public String primaryPayorInputTxt() {
        return (String) ((JavascriptExecutor) driver).executeScript("return $($('#payorAbbrev')[0]).val()");
    }

    //View Client/Payor Search Results
    public WebElement clientPayorPricingTable() {
        return driver.findElement(By.id("tbl_clnPyrPrcs"));
    }

    public WebElement priceTableId() {
        return driver.findElement(By.id("clnPyrTblID"));
    }

    public WebElement helpIconBtnInClientSearchResults() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_fee_schedule_client_pricing']"));
    }

    public WebElement helpIconBtnInPayorSearchResults() {
        return driver.findElement(By.xpath(".//*[@data-help-id='p_fee_schedule_payor_pricing']"));
    }

    public void setInputValue(WebElement e, String value) throws Exception {
        Thread.sleep(2000);
        e.click();
        e.sendKeys(Keys.CONTROL + "a");
        e.sendKeys(Keys.DELETE);
        e.click();
        e.sendKeys(value);
        e.sendKeys(Keys.TAB);
        Thread.sleep(2000);
        logger.info("        Entered value " + value);
    }
    public WebElement clientIdSearch(){
    	return driver.findElement(By.id("gs_abbrev"));
    }
    
    public WebElement changeFeeScheduleIdButton(){
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("changePrcTblId")));
    }
    
    public WebElement changeFeeScheduleIdPopupTitle(){
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@aria-describedby='changePrcTblIdDialog']//span[@class='ui-dialog-title']")));
    }
    
    public WebElement currentFeeScheduleId(){
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prcTblIdDialog")));
    }
    
    public WebElement newFeeScheduleId(){
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("newPrcTblId")));
    }
    
    public WebElement okButtonInChangeFeeScheduleIdPopup(){
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='OK']")));
    }
    
    public WebElement validationMessageInChangeFeeScheduleIdPopup(){
    	return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div#messagefor_newPrcTblId>div.xf_message_content")));
    }
    
    public WebElement closeValidationMessageInChangeFeeScheduleIdPopup(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div#messagefor_newPrcTblId a.xf_message_close")));
    }
}
