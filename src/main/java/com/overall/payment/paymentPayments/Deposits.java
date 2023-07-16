package com.overall.payment.paymentPayments;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class Deposits {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	private WebDriverWait wait;

	public Deposits(RemoteWebDriver driver, WebDriverWait wait){
		this.driver = driver;
		this.wait=wait;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}		

	public WebElement depositLedgerBtn() {
		return driver.findElement(By.id("btnLedger"));
	}
	public WebElement reconciledAmtInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_bankReconciliation']//tr//*[@aria-describedby='tbl_bankReconciliation_reconcileAmt']//*[@name='reconcileAmt']")));
	}
	public WebElement reconciledAmtInputTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_bankReconciliation']//tr//*[@aria-describedby='tbl_bankReconciliation_reconcileAmt']")));
	}

	public WebElement depositIDInput() {
		return wait.until(ExpectedConditions.elementToBeClickable(By.id("depositId")));
	}

	public WebElement paymentDepositsPageTitleTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[class='platormPageTitle']")));
	}

	public WebElement depositsSection() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("section[class*='lookupPaymentDepositsSection']")));
	}

	public WebElement depositsSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payment_deposits_load_deposit_id']")));
	}

	public WebElement depositsSectionDepositIdBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("depositIdSrchBtn")));
	}

	public WebElement depositsSectionDepositIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("depositId")));
	}

	public WebElement depositsSectionCreateDepositIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='btnCreateNewDepContainer']//span")));
	}

	public WebElement headerDepositIdTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("depId")));
	}

	public WebElement headerDepositDateInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("depRemitDt")));
	}

	public WebElement headerAccountingDateInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("depAccntDt")));
	}

	public WebElement headerDepositAmtInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("depAmt")));
	}

	public WebElement headerBankSel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("bank")));
	}

	public WebElement headerEraIdTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("eraId")));
	}

	public WebElement headerEffIdTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("eftId")));
	}

	public WebElement headerUserTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user")));
	}

	public WebElement headerDeleteDepositChk() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("delDeposit")));
	}

	public WebElement headerHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payment_deposits_header']")));
	}

	public WebElement loadDepositsPageErrorMessageTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='sectionTextBottom lookupMessageContainer']")));
	}

	public WebElement errorSection() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sectionServerMessages")));
	}

	public WebElement errorSectionMessageTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='sectionServerMessages']//p")));
	}

	public WebElement bnkReconciliationTblBnkTransIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_bankReconciliation']//tr//*[@aria-describedby='tbl_bankReconciliation_bankTransId']//*[@name='bankTransId']")));
	}

	public WebElement updateBnkReconciliationTblBnkTransIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_bankReconciliation']//tr//*[@aria-describedby='tbl_bankReconciliation_bankTransId']")));
	}

	public WebElement bnkReconciliationTblBnkTransIdTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_bankReconciliation']//*[@aria-describedby='tbl_bankReconciliation_bankTransId']")));
	}

	public WebElement bnkReconciliationTblBnkDateTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_bankReconciliation']//*[@aria-describedby='tbl_bankReconciliation_bankDate']")));
	}

	public WebElement bnkReconciliationTblBnkAmountTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_bankReconciliation']//*[@aria-describedby='tbl_bankReconciliation_bankAmt']")));
	}

	public WebElement bnkReconciliationTblBnkTransIdFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_bankTransId")));
	}

	public WebElement bnkReconciliationTblBnkTransDtInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_bankDate")));
	}

	public WebElement bnkReconciliationTblBnkTransAmtInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_bankAmt")));
	}

	public WebElement bnkReconliSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("section[data-help-id='p_payment_deposits_bank_reconciliation']")));
	}

	public WebElement bnkReconciliationTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#tbl_bankReconciliation_iladd > button")));
	}

	public WebElement bnkReconciliationTblTotalRecordsTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_bankReconciliation_pagernav_right']/div")));
	}

	public WebElement batchesSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payment_deposits_batches']")));
	}

	public WebElement batchesTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_batches")));
	}

	public List<WebElement> batchesTblDataRows() {
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(".//*[@id='tbl_batches']//tr[@tabindex]")));
	}

	public WebElement batchesTblRowNum(int row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_batches']//tr[" + row + "]")));
	}

	public WebElement batchesTblBatchIdColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr["+row+"]/td[@aria-describedby='tbl_batches_batchId']")));
	}

	public WebElement batchesTblBatchIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr/td[@aria-describedby='tbl_batches_batchId']/input")));
	}

	public WebElement batchesTblPayorIdColBtn(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr["+row+"]/td[@aria-describedby='tbl_batches_payorAbbrev']//a")));
	}

	public WebElement batchesTblPayorIdColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr["+row+"]/td[@aria-describedby='tbl_batches_payorAbbrev']")));
	}

	public WebElement batchesTblPayorIdColInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr/td[@aria-describedby='tbl_batches_payorAbbrev']//input")));
	}

	public WebElement batchesTblBatchAmtColInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr/td[@aria-describedby='tbl_batches_batchAmt']//input")));
	}

	public WebElement batchesTblBatchAmtColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr["+row+"]/td[@aria-describedby='tbl_batches_batchAmt']")));
	}

	public WebElement batchesTblUnappliedAmtColInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr/td[@aria-describedby='tbl_batches_unappliedAmt']/input")));
	}

	public WebElement batchesTblUnappliedAmtColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr["+row+"]/td[@aria-describedby='tbl_batches_unappliedAmt']")));
	}

	public WebElement batchesTblAppliedAmtColInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr/td[@aria-describedby='tbl_batches_appliedAmt']/input")));
	}

	public WebElement batchesTblAppliedAmtColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr["+row+"]/td[@aria-describedby='tbl_batches_appliedAmt']")));
	}

	public WebElement batchesTblPostedAmtColInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr/td[@aria-describedby='tbl_batches_postedAmt']/input")));
	}

	public WebElement batchesTblPostedAmtColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr["+row+"]/td[@aria-describedby='tbl_batches_postedAmt']")));
	}

	public WebElement batchesTblLastActivityColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr["+row+"]/td[@aria-describedby='tbl_batches_lastActivityDt']")));
	}

	public WebElement batchesTblStatusColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr["+row+"]/td[@aria-describedby='tbl_batches_status']")));
	}

	public WebElement batchesTblAssignedUserColInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr/td[@aria-describedby='tbl_batches_assignUser']//input")));
	}

	public WebElement batchesTblAssignedUserColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr["+row+"]/td[@aria-describedby='tbl_batches_assignUser']")));
	}

	public WebElement batchesTblNonARAmtColInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr/td[@aria-describedby='tbl_batches_nonArAmt']//input")));
	}

	public WebElement batchesTblNonARAmtColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr["+row+"]/td[@aria-describedby='tbl_batches_nonArAmt']")));
	}

	public WebElement batchesTblGLAccountColSel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr/td[@aria-describedby='tbl_batches_glCode']/select")));
	}

	public WebElement batchesTblGLAccountColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr["+row+"]/td[@aria-describedby='tbl_batches_glCode']")));
	}

	public WebElement batchesTblNoteColInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr/td[@aria-describedby='tbl_batches_notes']//textarea")));
	}

	public WebElement batchesTblNoteColTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr["+row+"]/td[@aria-describedby='tbl_batches_notes']")));
	}

	public WebElement batchesTblPostNonARAmtColChk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr["+row+"]/td[@aria-describedby='tbl_batches_postNonArAmt']/input")));
	}

	public WebElement batchesTblDeleteColChk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches']//tr["+row+"]/td[@aria-describedby='tbl_batches_deleted']/input")));
	}

	public WebElement batchesTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#tbl_batches_iladd > button ")));
	}

	public WebElement batchesTblTotalRecordTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_batches_pagernav_right']/div")));
	}

	public WebElement batchesTblTotalBatchAmtColTxt() {
		return wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='ui-jqgrid-ftable']//td[@aria-describedby='tbl_batches_batchAmt']/span")));
	}

	public WebElement batchesTblTotalUnappliedAmtColTxt() {
		return wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='ui-jqgrid-ftable']//td[@aria-describedby='tbl_batches_unappliedAmt']/span")));
	}

	public WebElement batchesTblTotalAppliedAmtColTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='ui-jqgrid-ftable']//td[@aria-describedby='tbl_batches_appliedAmt']/span")));
	}

	public WebElement batchesTblTotalPostedAmtColTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='ui-jqgrid-ftable']//td[@aria-describedby='tbl_batches_postedAmt']/span")));
	}

	public WebElement batchesTblTotalNonARAmtColTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='ui-jqgrid-ftable']//td[@aria-describedby='tbl_batches_nonArAmt']/span")));
	}

	public WebElement depositNotesSectionHelpIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_payment_deposits_deposit_notes']")));
	}

	public WebElement depositNotesSectionNoteInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("note")));
	}

	public WebElement warningPopupTitleTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='confirmationDialog']//span[@class='ui-dialog-title']")));
	}

	public WebElement warningPopupCloseIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='confirmationDialog']//button[contains(@class,'titlebar-close')]")));
	}

	public WebElement warningPopupMessageTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='confirmationDialog']//div[@class='contentBlock']//span")));
	}

	public WebElement warningPopupResetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='confirmationDialog']//button[contains(@class,'btn_submit')]")));
	}

	public WebElement dateValidationPopupCloseIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='messagefor_depRemitDt']//a[@class='xf_message_close']")));
	}

	public WebElement dateValidationPopupMessageTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='messagefor_depRemitDt']//div[@class='xf_message_content']")));
	}

	public WebElement bankValidationPopupCloseIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='messagefor_bank']//a[@class='xf_message_close']")));
	}

	public WebElement bankValidationPopupMessageTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='messagefor_bank']//div[@class='xf_message_content']")));
	}

	public WebElement footerResetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}

	public WebElement footerSaveAndClearBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}

	public WebElement footerSectionSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sectionSearchField")));
	}

	public void clickOnResetBtnAtWarningPopup(){
		warningPopupResetBtn().click();
	}

	public void clickOnCreateDepositIco(){
		depositsSectionCreateDepositIco().click();
	}

	public void clickOnBnkReconciliationAddBtn(){
		bnkReconciliationTblAddBtn().click();
	}

	public void clickOnSaveAndClearBtn(){
		footerSaveAndClearBtn().click();
	}

	public void clickOnBnkTransactionIdInput() {
		updateBnkReconciliationTblBnkTransIdInput().click();
	}
	public void clickOnResetBtn() {
		footerResetBtn().click();
	}
}
