package com.overall.fileMaintenance.procedureCode;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProcedureCode {
	private WebDriverWait wait;
	protected Logger logger;

	public ProcedureCode(RemoteWebDriver driver)  {
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}

	/*** Procedure Code Details Header ***/
	public WebElement proCdDetailTitle()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='platormPageTitle']")));
	}

	public WebElement runAuditBtn()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("auditBtn")));
	}

	public WebElement proCdDetailHelpIcon()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='fileMaintProcedureCodeForm']/div[1]/div[3]/div[1]/div/div[1]/div[1]/a")));
	}

	public WebElement procCodeTypIdTextbox()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("procCodeTypId")));
	}

	public WebElement procCodeIdTextbox()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("procCodeId")));
	}

	public WebElement ServiceTypeDropdownList()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("svcTypId")));
	}

	public WebElement shortDescriptionInput()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("shortDesc")));
	}

	public WebElement descriptionInput()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("procDesc")));
	}

	public WebElement revenueCdTypIdDropdownList()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("revCodeTypId")));
	}

	public WebElement helpIconOnHeader()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_procedure_code_header']")));
	}
	
	/***  Create New Procedure Code Type Popup ***/
	public WebElement createNewProcPopupTitle()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='ui-id-1']")));
	}
	
	public WebElement procCodeTypInCreateNewProcPopupInput()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("procCodeTypAbbrev")));
	}
	
	public WebElement descriptionInCreateNewProcPopupInput()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("procCodeTypDescr")));
	}
	
	public WebElement cancleBtnInCreateNewProcPopup()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/div[3]/div[3]/div/button[1]")));
	}
	
	public WebElement okBtnInCreateNewProcPopup()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/div[3]/div[3]/div/button[2]")));
	}
	
	/*** Service Level Section ***/

	public WebElement ServiceLevelTypIdDropdownList()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("svcLvlTypId")));
	}

	public WebElement zeroBillCheckbox()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("zeroBillCheck")));
	}

	public WebElement unlistCheckbox()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("unlistCheck")));
	}

	public WebElement autochemCheckbox()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("autochemCheck")));
	}

	public WebElement zeroExpectCheckbox()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("zeroExpectCheck")));
	}

	public WebElement panelCodeCheckbox()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("panelCodeCheck")));
	}

	public WebElement expectPriceCheckbox()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("expectPriceCheck")));
	}

	public WebElement suppressModCheckbox()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("suppressModCheck")));
	}

	public WebElement travelFeeCheckbox()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("travelFeeCheck")));
	}

	public WebElement serviceLevelSectionHelpIcon()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("travelFeeCheck")));
	}

	public WebElement helpIconOnServiceLevel()  {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_procedure_code_service_level']")));
	}
	/*** Procedure Code Page Load ***/
	public WebElement procedureCodePageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='platormPageTitle']")));
	}
	
	public WebElement procedureCodeTblTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='platormPageTitle']")));
	}
	
	public WebElement procedureCodeTypeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("procTypId")));
	}
	
	public WebElement procedureCodeIDInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupProcCodeId")));
	}
	
	public WebElement procedureCodeIDSearchIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("procedure_code_search_btn")));
	}
	
	public WebElement createNewProcedureCodeTypeLink() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fileMaintProcedureCodeForm']/div[1]/div[2]/section/div/div[2]/div/div[1]/div[2]/a")));
	}
	
	public WebElement procedureCodeSectionHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_procedure_code_load_procedure_code']")));
	}
	
	/*** Procedure Cross Reference table ***/
	
	public WebElement procedureCrossReferenceTblTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']/div[4]/div/section/header/div[1]/span/span")));
	}
	
	public WebElement procedureCrossReferenceEffDateSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_effDate")));
	}
	
	public WebElement produreCrossReferenceCrossRefDescSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_xRefId")));
	}
	
	public WebElement procedureCrossReferenceAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_procedureCrossReference_iladd")));
	}
	
	public WebElement procedureCrossReferenceEditBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit_tbl_procedureCrossReference")));
	}
	
	public WebElement procedureCrossReferenceDeletedIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jqgh_tbl_procedureCrossReference_deleted")));
	}
	
	public WebElement procedureCrossReferenceTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_procedureCrossReference")));
	}
	
	public WebElement procedureCrossReferenceCelData(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_procedureCrossReference']/tbody/tr["+row+"]/td["+col+"]")));
	}
	
	public WebElement procedureCrossReferenceCelInput(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_procedureCrossReference']/tbody/tr["+row+"]/td["+col+"]/input")));
	}
	
	public WebElement procedureCrossReferenceTotalRecords() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_procedureCrossReference_pagernav_right']/div")));
	}
	
	public WebElement procedureCrossReferenceFirstPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='first_tbl_procedureCrossReference_pagernav']/span")));
	}
	
	public WebElement procedureCrossReferencePrevPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prev_tbl_procedureCrossReference_pagernav']/span")));
	}
	
	public WebElement procedureCrossReferenceNextPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_tbl_procedureCrossReference_pagernav']/span")));
	}
	
	public WebElement procedureCrossReferenceLastPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='last_tbl_procedureCrossReference_pagernav']/span")));
	}
	
	public WebElement procedureCrossReferenceHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_procedure_code_procedure_cross_reference']")));
	}
	
	public WebElement procedureCrossReferenceAddRecordPopupTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='edithdtbl_procedureCrossReference']/span")));
	}
	
	public WebElement procedureCrossReferenceEffDateInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("effDate")));
	}
	
	public WebElement procedureCrossReferenceCrossReferenceDescriptionDropdown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_xRefId']")));
	}
	
	public WebElement crossReferenceDescrInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='select2-drop']/div/input")));
	}
	
	public WebElement oKBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sData")));
	}
	
	public WebElement cancelBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cData")));
	}
	
	public WebElement helpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
	
	public WebElement procedureCrossReferenceDeleteCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("deleted")));
	}

	public WebElement saveAndClearBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}
	
	public WebElement resetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}
	
	/*** Audit Page***/
	
	public WebElement auditPageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_auditlogwait']/div[1]/span")));
	}
	
	public WebElement nextPagerInAuditPageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_pager']/span")));
	}
	
	public WebElement totalRecordInAuditPageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_right']/div")));
	}
	
	/*** Methods ***/
	public void enterToShortDescriptionInput(String value) throws Exception {
		shortDescriptionInput().clear();
		shortDescriptionInput().sendKeys(value);
		shortDescriptionInput().sendKeys(Keys.TAB);
		Log.info("        Enter to shortDescriptionInput: " + value);
	}
	
	public void enterToDescriptionInput(String value) throws Exception {
		descriptionInput().clear();
		descriptionInput().sendKeys(value);
		descriptionInput().sendKeys(Keys.TAB);
		Log.info("        Enter to DescriptionInput: " + value);
	}
	
	public void enterProcedureCrossReferenceEffDateInput(String value) {
		procedureCrossReferenceEffDateInput().clear();
		procedureCrossReferenceEffDateInput().sendKeys(value);
		procedureCrossReferenceEffDateInput().sendKeys(Keys.TAB);
		Log.info("        Enter Procedure Cross Reference Eff Date: " + value);
	}
	
	public void enterpPocedureCrossReferenceEffDateSearch(String value) {
		procedureCrossReferenceEffDateSearchInput().clear();
		procedureCrossReferenceEffDateSearchInput().sendKeys(value);
		procedureCrossReferenceEffDateSearchInput().sendKeys(Keys.TAB);
		Log.info("        Enter Procedure Cross Reference Eff Date Search: " + value);
	}
	
	public WebElement fileMaintProcedureCodeTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fileMaintProcedureCodeForm']/div[1]/div[1]/div[1]/span[1]")));
	}
	
	public WebElement fileMaintProcedureCodeMainPageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='fileMaintProcedureCodeForm']/div[1]/div[3]/div[1]/div[1]/div[1]/div[2]/span[1]")));
	}	
	
	public WebElement effDtErrMsgTextLbl() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@id,'messagefor_jqg') and contains(@id,'_effDate')]//*[@class='xf_message_content']")));
    }                                            
	
	public WebElement effDtErrMsgCancleBtn() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@id,'messagefor_jqg') and contains(@id,'_effDate')]//*[@class='xf_message_close']")));
    } 
	
	public void selectProcedureCodeType(String value) {
		procedureCodeTypeInput().click();
		procedureCodeTypeInput().sendKeys(value);
		procedureCodeTypeInput().sendKeys(Keys.TAB);
		logger.info("        Select Procedure Code Type: " + value);
	}
	
	public void enterCrossReferenceDescription(String value) {
		procedureCrossReferenceCrossReferenceDescriptionDropdown().click();
		crossReferenceDescrInput().clear();
		crossReferenceDescrInput().sendKeys(value);
		crossReferenceDescrInput().sendKeys(Keys.TAB);
		logger.info("        Enter Cross Reference Description: " + value);
	}
	
	public WebElement CrossReferenceDescriptionErrorMessagesTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("xf_message_content")));
	}
	
	public WebElement sectionServerMessages() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='sectionServerMessages']//li[1]/p[1]")));
	}
	public void enterpPocedureCrossReferenceCrossRefDescrSearch(String value) {
		produreCrossReferenceCrossRefDescSearchInput().clear();
		produreCrossReferenceCrossRefDescSearchInput().sendKeys(value);
		produreCrossReferenceCrossRefDescSearchInput().sendKeys(Keys.TAB);
		Log.info("        Enter Cross Reference Description Search: " + value);
	}
	
	public void enterProcCodeTypInCreateNewProcPopup(String value) {
		procCodeTypInCreateNewProcPopupInput().clear();
		procCodeTypInCreateNewProcPopupInput().sendKeys(value);
		procCodeTypInCreateNewProcPopupInput().sendKeys(Keys.TAB);
	}
	
	public void enterDescriptionInCreateNewProcPopup(String  value) {
		descriptionInCreateNewProcPopupInput().clear();
		descriptionInCreateNewProcPopupInput().sendKeys(value);
		descriptionInCreateNewProcPopupInput().sendKeys(Keys.TAB);
	}

	public void pressAltR() {
        String selectAll = Keys.chord(Keys.ALT, "R");
        WebElement body = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        body.sendKeys(selectAll);
        logger.info("        press Ctr + R");
    }
	
	public WebElement selectedProcCodeTypIdTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_procTypId']//span[@class='select2-chosen']")));
	}
	
	// Procedure Cross Reference Tbl
	public WebElement procXRefTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_procedureCrossReference")));
	}

	public WebElement procXRefTblEffDtCol(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_procedureCrossReference']//tr[" + row + "]/*[@aria-describedby='tbl_procedureCrossReference_effDate']")));
	}
	
	public WebElement procXRefTblEffDtColInput(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_procedureCrossReference']//tr[" + row + "]/*[@aria-describedby='tbl_procedureCrossReference_effDate']/input")));
	}

	public WebElement procXRefTblExpDtColInput(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_procedureCrossReference']//tr[" + row + "]/*[@aria-describedby='tbl_procedureCrossReference_expDate']/input")));
	}

	public WebElement procXRefTblXRefDescColDdl(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_procedureCrossReference']//tr[" + row + "]/*[@aria-describedby='tbl_procedureCrossReference_xRefId']/*[contains(@id, 'xRefId') and contains(@id, 's2id')]")));
	}
	
	public WebElement procXRefTblIsDeletedColChk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_procedureCrossReference']//tr[" + row + "]/*[@aria-describedby='tbl_procedureCrossReference_deleted']/input")));
	}
	
	public WebElement alertMessageTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='confirmationDialog']//span")));
	}
	
	public WebElement alertMessageCloseBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='confirmationDialog']//button[@title='close']")));
	}
	
	public WebElement alertMessageResetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='confirmationDialog']//*[@class='ui-dialog-buttonset']//*[text()[contains(., 'Reset')]]")));
	}
	
	public WebElement alertMessageCancelBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='confirmationDialog']//*[@class='ui-dialog-buttonset']//*[text()[contains(., 'Cancel')]]")));
	}
	
	public WebElement invalidDateMessageCloseBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='xf_message_close']")));
	}
}
