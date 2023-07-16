package com.overall.accession.dailyReceipt;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DailyReceipt {
	private WebDriverWait wait;
	private RemoteWebDriver driver;
	protected Logger logger;
	
	public DailyReceipt(RemoteWebDriver driver){
		this.wait = new WebDriverWait(driver, 10);
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	

	// Accession Daily Receipt load page
	
	public WebElement pageTitle(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));
    }
	
	public WebElement dailyReceiptLoadPageDailyReceiptSection(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='dlyRcptAbbrv']//ancestor::div[contains(@class,'layoutMainLookup')]")));
    }
	
	public WebElement dailyReceiptLoadPageDailyReceiptSectionHelpIco(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-help-id='p_daily_receipt_load_daily_receipt_id']")));
    }
	
	public WebElement loadPageDailyReceiptIDSearchIco(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupDlyRcptSrchBtn")));
    }
	
	public WebElement loadPageDailyReceiptIDInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("dlyRcptAbbrv")));
    }
	
	public WebElement loadPageCreateNewDailyReceiptLnk(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='btnCreateNewContainer']/a")));
    }
	
	public WebElement loadPagePaymentFacilityIdSearchIco(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnFacilitySearch")));
    }
	
	public WebElement loadPagePaymentFacilityIdInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("paymentFacilityAbbrev")));
    }
	
	public WebElement loadPagePaymentFacilityNameInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("paymentFacilityName")));
    }
    
    public WebElement loadPagePaymentUserIDSearchIco(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnUserSearch")));
    }
    
    public WebElement loadPagePaymentUserIdInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("paymentUserId")));
    }
    
    public WebElement loadPagePaymentUserNameInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("paymentUserFullName")));
    }
    
    public WebElement loadPageGetPaymentsBtn(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnGetPayments")));
    }
    
    public WebElement loadPageMessageTxt(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='accessionDailyReceiptForm']//*[contains(@class,'lookupMessageContainer')]")));
    }
    
    public WebElement loadPageResetBtn(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnGetPayments")));
    }

	public WebElement loadPageDailyErrorMessageTxt(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='sectionTextBottom lookupMessageContainer']")));
    }
    // ACCESSION DAILY RECEIPT DETAIL PAGE
    
    // header
    
    public WebElement headerHelpIco(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-help-id='p_daily_receipt_header']")));
    }
    
    public WebElement headerDailyReceiptIdTxt(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='dlyRcptId']//preceding-sibling::span[contains(@class,'abbrev')]")));
    }

    public WebElement headerPaymentFacilityIdTxt(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,'accnDailyReceiptHeaderContent')]//*[contains(@class, 'paymentFacilityAbbrev')]")));
    }
    
    public WebElement headerPaymentFacilityNameTxt(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,'accnDailyReceiptHeaderContent')]//*[contains(@class, 'paymentFacilityName')]")));
    }
    
    public WebElement headerPaymentUserIdTxt(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,'accnDailyReceiptHeaderContent')]//*[contains(@class, 'paymentUserId')]")));
    }
    
    public WebElement headerPaymentUserNameTxt(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,'accnDailyReceiptHeaderContent')]//*[contains(@class, 'paymentUserFullName')]")));
    }
    
    public WebElement headerDepositIDTxt(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,'accnDailyReceiptHeaderContent')]//*[contains(@class, 'depositId')]")));
    }
    
    public WebElement headerBatchIdTxt(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,'accnDailyReceiptHeaderContent')]//*[contains(@class, 'depositBatchId')]")));
    }
    
    public WebElement headerDateOfDepositTxt(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,'accnDailyReceiptHeaderContent')]//*[contains(@class, 'dateOfDeposit')]")));
    }
    
    public WebElement headerCreatedByTxt(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,'accnDailyReceiptHeaderContent')]//*[contains(@class, 'createdBy')]")));
    }
    
    // begin Daily Receipt Summary section
    public WebElement dailyReceiptSummarySectionHelpIco(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-help-id='p_daily_receipt_daily_receipt_summary']")));
    }
    
    public WebElement dailyReceiptSummarySectionBagIdInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("bagId")));
    }

    public WebElement dailyReceiptSummarySectionDrawerIdInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("drawerId")));
    }
    
    public WebElement dailyReceiptSummarySectionCashAtDrawerOpeningInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cashAtDrawerOpening")));
    }
    
    public WebElement dailyReceiptSummarySectionCashAtDrawerClosingInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cashAtDrawerClosing")));
    }
    
    public WebElement dailyReceiptSummarySectionCashCountedAtDrawerOpeningByInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cashDrawerOpenedBy")));
    }
    
    public WebElement dailyReceiptSummarySectionCashCountedAtDrawerClosingByInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cashDrawerClosedBy")));
    }
    
    public WebElement dailyReceiptSummarySectionVerifiedInAccountingByInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("verifiedByUser")));
    }

    public WebElement dailyReceiptSummarySectionTotalsCashInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("totalCash")));
    }
    
    public WebElement dailyReceiptSummarySectionTotalsCashOverUnderInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cashOverUnder")));
    }
    
    public WebElement dailyReceiptSummarySectionTotalAcctUseOnlyInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cashAccountingUse")));
    }
    
    public WebElement dailyReceiptSummarySectionTotalCheckInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("totalCheck")));
    }
    
    public WebElement dailyReceiptSummarySectionCheckAcctUseOnlyInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("checkAccountingUse")));
    }
    
    public WebElement dailyReceiptSummarySectionPlsExplainAnyOveragesShortagesInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("overUnderComment")));
    }
	
    // begin Daily Receipt Details section
    public WebElement dailyReceiptDetailsSectionHelpIco(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-help-id='p_daily_receipt_daily_receipt_details']")));
    }
    
    public WebElement outpatientCenterReceiptLogTbl(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_outpatientCenterReceiptLog")));
    }
    
    public WebElement outpatientCenterReceiptLogTblAccesionIdFilterInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_accnId")));
    }
    
    public WebElement outpatientCenterReceiptLogTblEpiFilterInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_epi")));
    }
    
    public WebElement outpatientCenterReceiptLogTblPatientNameFilterInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_patientFullName")));
    }
    
    public WebElement outpatientCenterReceiptLogTblCashFilterInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_cashAmount")));
    }
    
    public WebElement outpatientCenterReceiptLogTblCheckAmountFilterInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_checkAmount")));
    }
    
    public WebElement outpatientCenterReceiptLogTblCheckNumberFilterInput(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_checkNumber")));
    }
    
    public List<WebElement> outpatientCenterReceiptLogTblAllRow(){
        return driver.findElements(By.xpath("//*[@id='tbl_outpatientCenterReceiptLog']//tr[@tabindex]"));
    }
    
    public WebElement outpatientCenterReceiptLogTblPmtSeqTxt(String row){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_outpatientCenterReceiptLog']//tr["+row+"]/td[@aria-describedby='tbl_outpatientCenterReceiptLog_pmtSeq']")));
    }
    
    public WebElement outpatientCenterReceiptLogTblAccesionIdTxt(String row){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_outpatientCenterReceiptLog']//tr["+row+"]/td[@aria-describedby='tbl_outpatientCenterReceiptLog_accnId']")));
    }
    
    public WebElement outpatientCenterReceiptLogTblEpiTxt(String row){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_outpatientCenterReceiptLog']//tr["+row+"]/td[@aria-describedby='tbl_outpatientCenterReceiptLog_epi']")));
    }
    
    public WebElement outpatientCenterReceiptLogTblPatientNameTxt(String row){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_outpatientCenterReceiptLog']//tr["+row+"]/td[@aria-describedby='tbl_outpatientCenterReceiptLog_patientFullName']")));
    }
    
    public WebElement outpatientCenterReceiptLogTblCashTxt(String row){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_outpatientCenterReceiptLog']//tr["+row+"]/td[@aria-describedby='tbl_outpatientCenterReceiptLog_cashAmount']")));
    }
    
    public WebElement outpatientCenterReceiptLogTblCheckAmountTxt(String row){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_outpatientCenterReceiptLog']//tr["+row+"]/td[@aria-describedby='tbl_outpatientCenterReceiptLog_checkAmount']")));
    }
    
    public WebElement outpatientCenterReceiptLogTblCheckNumberTxt(String row){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_outpatientCenterReceiptLog']//tr["+row+"]/td[@aria-describedby='tbl_outpatientCenterReceiptLog_checkNumber']")));
    }
    
    public WebElement outpatientCenterReceiptLogTblTotalCashTxt(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_outpatientCenterReceiptLog']/div[last()]//td[@aria-describedby='tbl_outpatientCenterReceiptLog_cashAmount']")));
    }
    
    public WebElement outpatientCenterReceiptLogTblTotalsCheckTxt(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='gview_tbl_outpatientCenterReceiptLog']/div[last()]//td[@aria-describedby='tbl_outpatientCenterReceiptLog_checkAmount']")));
    }
    
    public WebElement outpatientCenterReceiptLogTblFirstPageIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_tbl_outpatientCenterReceiptLog_pagernav")));
    }

    public WebElement outpatientCenterReceiptLogTblPrevPageIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_tbl_outpatientCenterReceiptLog_pagernav")));
    }

    public WebElement outpatientCenterReceiptLogTblPageInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_outpatientCenterReceiptLog_pagernav_center']//input[@class='ui-pg-input']")));
    }

    public WebElement outpatientCenterReceiptLogTotalPageLbl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sp_1_tbl_outpatientCenterReceiptLog_pagernav")));
    }
    
    public WebElement outpatientCenterReceiptLogTblNextPageIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_outpatientCenterReceiptLog_pagernav")));
    }

    public WebElement outpatientCenterReceiptLogTblLastPageIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_tbl_outpatientCenterReceiptLog_pagernav")));
    }

    public WebElement outpatientCenterReceiptLogTblRowNumSel() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_outpatientCenterReceiptLog_pagernav_center']//select[@class='ui-pg-selbox']")));
    }

    public WebElement outpatientCenterReceiptLogTblTotalRecordTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_outpatientCenterReceiptLog_pagernav_right']/div")));
    }
    
    // footer
    
    public WebElement footerHelpIco(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_daily_receipt_summary']")));
    }
    
    public WebElement footerResetBtn(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
    }
    
    public WebElement footerSaveAndClearBtn(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
    }
    
    public WebElement warningPopupWarningTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='confirmationDialog']//span")));
    }
    
    public WebElement warningPopupResetBtn(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='confirmationDialog']//span[text()='Reset']/parent::button")));
    }
    
    public WebElement warningPopupCancelBtn(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='confirmationDialog']//span[text()='Cancel']/parent::button")));
    }
    
    public List<WebElement> errorsReturnedMessageTxt(){
        return driver.findElements(By.xpath("//*[@id='sectionServerMessages']//ul[@class='serverErrorsList']//p"));
    }
    
    //Created New Daily Receipt
    public WebElement createdNewDailyReceiptPopupTitleTxt(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@aria-describedby='newDailyReceiptDialog']//span[@class='ui-dialog-title']")));
    }
    
    public WebElement createdNewDailyReceiptPopupCloseIco(){
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@aria-describedby='newDailyReceiptDialog']//button/span[contains(@class,'ui-icon-closethick')]")));
    }
}
