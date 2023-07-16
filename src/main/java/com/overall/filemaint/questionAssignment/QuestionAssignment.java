package com.overall.filemaint.questionAssignment;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class QuestionAssignment {
	private WebDriverWait wait;
	private RemoteWebDriver driver;
	protected Logger logger;
		
	public QuestionAssignment(RemoteWebDriver driver) {
		this.wait = new WebDriverWait(driver, 10);
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*** Load Question Assignment in default page ***/
	public WebElement questionAssignmentLoadPageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='platormPageTitle']")));
	}
	
	public WebElement loadQuestionAssignmentTblTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='questAssignmentForm']//span[contains(@class,'titleText')]")));
	}
	
	public WebElement questionAssignmentIDInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lookupQuestionAssignment")));
	}
	
	public WebElement clientSearchBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("client_abbrev_search_btn")));
	}
	
	public WebElement testCdSearchBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("test_code_search_btn")));
	}
	
	public WebElement loadQuestionAssignmentTblHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//a[contains(@data-help-id,'p_question_assignment_enter_question_assignment')]")));
	}
	
	public WebElement idTypeAllClientsCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("idTypeAllClients")));
	}
	
	public WebElement idTypeClientCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("idTypeClient")));
	}
	
	public WebElement idTypeTestCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("idTypeTest")));
	}
	
	public WebElement idTypeSpecimenCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("idTypeSpecimen")));
	}
	
	public WebElement spcmTypeIdDropDownList()  {
	    return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("s2id_spcmTypId")));
	}
		
	/*** End Load Question Assignment in default page ***/
	
	/*** Header Question Assignment Detail page***/
	public WebElement runAuditBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("auditBtn")));
	}
	
	public WebElement headerHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//a[contains(@data-help-id,'p_question_assignment_header')]")));
	}
	
	public WebElement questionAssignmentDetailPageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='questAssignmentForm']//span[contains(@class,'platormPageTitle')]")));
	}
	
	public WebElement searchTypeText()  { //ClientId / TestId / Specimen
	    return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("searchTypeDisplay")));
	}
	
	/*** End Header Question Assignment Detail page***/
	
	/*** Load Question Assignment table in Detail page***/
	public WebElement questionAssignmentTblTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//section[@class='groupContainer groupAssignment']//span[contains(@class,'titleTextContainer')]/span")));
	}
	
	public WebElement questionAssignmentTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_questionAssignments")));
	}
	
	public WebElement questionAssignmentTblQuestionIdColFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_qstnId")));
	}
	
	public WebElement questionAssignmentTblTextColFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_qstnText")));
	}
	
	public WebElement questionAssignmentTblRequiredColFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_qstnReqCb")));
	}
	
	public WebElement questionAssignmentTblRow(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionAssignments']//tr["+row+"]")));
	}
	
	public WebElement questionAssignmentTblQuestionIdInput(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionAssignments']//tr["+row+"]/td[contains(@aria-describedby,'tbl_questionAssignments_qstnId')]/input")));
	}
	
	public WebElement questionAssignmentTblQuestionIdColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionAssignments']//tr["+row+"]/td[contains(@aria-describedby,'tbl_questionAssignments_qstnId')]")));
	}
	
	public WebElement questionAssignmentTblTextColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionAssignments']//tr["+row+"]/td[contains(@aria-describedby,'tbl_questionAssignments_qstnText')]/input")));
	}
	
	public WebElement questionAssignmentTblRequireColDropdownList(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionAssignments']//tr["+row+"]/td[contains(@aria-describedby,'tbl_questionAssignments_qstnReqCb')]/div")));
	}
	
	public WebElement questionAssignmentTblRequireColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionAssignments']//tr["+row+"]/td[contains(@aria-describedby,'tbl_questionAssignments_qstnReqCb')]")));
	}
	
	public WebElement questionAssignmentTblRequireColTextOnFocus(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionAssignments']//tr["+row+"]/td[contains(@aria-describedby,'tbl_questionAssignments_qstnReqCb')]//span[1]")));
	}
	
	public WebElement questionAssignmentTblRequireColChosenText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionAssignments']//tr["+row+"]/td[contains(@aria-describedby,'tbl_questionAssignments_qstnReqCb')]//span[1]")));
	}
	
	public WebElement questionAssignmentTblPrintOnClientStatementCheckbox(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionAssignments']//tr["+row+"]/td[contains(@aria-describedby,'tbl_questionAssignments_qstnPrntCb')]/input")));
	}
	
	public WebElement questionAssignmentTblDeleteCheckbox(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionAssignments']//tr["+row+"]/td[contains(@aria-describedby,'tbl_questionAssignments_qstnDeleteCb')]/input")));
	}
	
	public WebElement questionAssignmentTblQuestionIdSearchIcon(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionAssignments']//tr["+row+"]//a[contains(@title,'Question ID Search')]")));
	}
	
	public WebElement questionAssignmentTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_questionAssignments_iladd")));
	}
	
	public WebElement questionAssignmentTblHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_question_assignment']")));
	}
	
	public WebElement questionAssignmentTblFirstPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_tbl_questionAssignments_pagernav")));
	}
	
	public WebElement questionAssignmentTblPrevPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_tbl_questionAssignments_pagernav")));
	}
	
	public WebElement questionAssignmentTblNextPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_questionAssignments_pagernav")));
	}
	
	public WebElement questionAssignmentTblLastPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_tbl_questionAssignments_pagernav")));
	}
	
	public WebElement questionAssignmentTblTotalRecords() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionAssignments_pagernav_right']/div")));
	}	
	
	public WebElement questionAssignmentTblTotalPagesText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(@id,'sp') and contains(@id,'tbl_questionAssignments_pagernav')]")));
	}
	
	public WebElement questionAssignmentTblPageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id,'tbl_questionAssignments_pagernav_center')]//parent::td//following-sibling::input")));
	}	
		
	/*** Question Assignment To All Clients Section***/
	public WebElement qAToAllClnTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_questionAssignmentAllClient")));
	}
	
	public WebElement qAToAllClnTblTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//section[@class='groupContainer groupAssignmentToAllClns']//span[contains(@class,'titleTextContainer')]/span")));
	}
		
	public WebElement qAToAllClnTblHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_question_assignment_all_clients']")));
	}
	
	public WebElement qAToAllClnTblTextFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_qstnTextDef")));
	}
	
	public WebElement qAToAllClnTblTextFilterRequiredFilter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_qstnReqCbDef")));
	}
	
	public WebElement qAToAllClnTbQuestionIdColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionAssignmentAllClient']//tr["+row+"]/td[contains(@aria-describedby,'tbl_questionAssignmentAllClient_qstnIdDef')]")));
	}
	
	public WebElement qAToAllClnTbTextColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionAssignmentAllClient']//tr["+row+"]/td[contains(@aria-describedby,'tbl_questionAssignmentAllClient_qstnText')]")));
	}
	
	public WebElement qAToAllClnTbRequireColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionAssignmentAllClient']//tr["+row+"]/td[contains(@aria-describedby,'tbl_questionAssignmentAllClient_qstnReqCb')]")));
	}
	
	public WebElement qAToAllClnTbPrintOnClientStatementCheckbox(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionAssignmentAllClient']//tr["+row+"]/td[contains(@aria-describedby,'tbl_questionAssignmentAllClient_qstnPrntCb')]/input")));
	}
	
	public WebElement qAToAllClnTblFirstPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_tbl_questionAssignmentAllClient_pagernav")));
	}
	
	public WebElement qAToAllClnTblPrevPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_tbl_questionAssignmentAllClient_pagernav")));
	}
	
	public WebElement qAToAllClnTblNextPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_questionAssignmentAllClient_pagernav")));
	}
	
	public WebElement qAToAllClnTblLastPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_tbl_questionAssignmentAllClient_pagernav")));
	}
	
	public WebElement qAToAllClnTblTotalRecords() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionAssignments_pagernav']//div[@class='ui-paging-info']")));
	}	
	
	/*** End Question Assignment To All Clients Section***/
	
	/*** Footer ***/
	public WebElement footerHelpIcon()  {
	    return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
	
	public WebElement showClipBoardIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[contains(@class,'btnClipboardToolbar')]")));
	}
	
	public WebElement keyBoardShotcutsIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[contains(@class,'btnKeyboardShortcuts')]")));
	}
	
	public WebElement resetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}
	
	public WebElement saveAndClearBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}
	/*** End Footer ***/	
    
	/*** the warning message popup ***/	
	public WebElement warningPopupResetBtn()  {
	    return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@aria-describedby='confirmationDialog']//button/span[text()='Reset']/..")));
	}
	
	/*** Methods ***/	
	public void enterNumberPage(int numberPage) {
		questionAssignmentTblPageInput().clear();
		questionAssignmentTblPageInput().sendKeys(String.valueOf(numberPage));
		questionAssignmentTblPageInput().sendKeys(Keys.ENTER);
		logger.info("        Enter value to Page input :" + numberPage);
	}

	/*** End Methods ***/
	
	public List<WebElement> numSectionInPage() {
		List<WebElement> optionCount = driver.findElements(By.xpath(".//*[@class='layoutComponent layoutMain']//div[@class='blockTag section layoutComponent']//section[contains(@class,'groupContainer')]"));
		return optionCount;
	}
	
	public WebElement sectionContainGroupAssignmentToAllClns() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//section[contains(@class,'groupAssignmentToAllClns')]")));
	}
}