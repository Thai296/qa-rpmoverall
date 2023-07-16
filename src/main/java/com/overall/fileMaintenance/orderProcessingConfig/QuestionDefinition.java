package com.overall.fileMaintenance.orderProcessingConfig;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class QuestionDefinition {
	private WebDriverWait wait;
	protected Logger logger;
		
	public QuestionDefinition(RemoteWebDriver driver)  {
		this.wait= new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public WebElement questionDefPageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='platormPageTitle']")));
	}
	
	public WebElement runAuditBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("auditBtn")));
	}
	
	public WebElement helpIconOnHeader() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//a[@data-help-id='p_question_definition_header']")));
	}
	
	public WebElement questionDefinitionTblHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//a[@data-help-id='p_question_definition']")));
	}
	
	public WebElement helpIconOnFooter() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
	
	public WebElement showClipBoardIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[contains(@class,'btnClipboardToolbar') and @title='Show Clipboard']")));
	}
	
	public WebElement keyBoardShotcutsIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[contains(@class,'btnKeyboardShortcuts') and @title='Show Keyboard Shortcuts']")));
	}
	
	public WebElement resetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}
	
	public WebElement saveAndClearBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}
	
	/*** Question Definition Table***/
	public WebElement questionDefinitionTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_questionDefinitionDetail")));
	}
	
	public WebElement questionDefinitionTblQuestionIdColInput(String row) { //row start from 2 - last()
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_questionDefinitionDetail']//tr["+row+"]/td[@aria-describedby='tbl_questionDefinitionDetail_questnId']/input")));
	}
	
	public WebElement questionDefinitionTblQuestionIdColText(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_questionDefinitionDetail']//tr["+row+"]/td[@aria-describedby='tbl_questionDefinitionDetail_questnId']")));
	}
	
	public WebElement questionDefinitionTblPatternIdColText(String row) { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_questionDefinitionDetail']//tr["+row+"]/td[@aria-describedby='tbl_questionDefinitionDetail_patternTypId']")));
	}
	
	public WebElement questionDefinitionTblPatternIdColDropdownList(String row) { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_questionDefinitionDetail']//tr["+row+"]/td[@aria-describedby='tbl_questionDefinitionDetail_patternTypId']//div[contains(@id,'s2id') and contains(@id,'patternTypId')]")));
	}

	public WebElement questionDefinitionTblTextColText(String row) { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_questionDefinitionDetail']//tr["+row+"]/td[@aria-describedby='tbl_questionDefinitionDetail_questn']/div")));
	}
	
	public WebElement questionDefinitionTblTextColtextarea(String row) { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_questionDefinitionDetail']//tr["+row+"]/td[@aria-describedby='tbl_questionDefinitionDetail_questn']/textarea")));
	}

	public WebElement questionDefinitionTblPatternOverrColText(String row) { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_questionDefinitionDetail']//tr["+row+"]/td[@aria-describedby='tbl_questionDefinitionDetail_patternOvrride']/div")));
	}
	
	public WebElement questionDefinitionTblPatternOverrColtextarea(String row) { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_questionDefinitionDetail']//tr["+row+"]/td[@aria-describedby='tbl_questionDefinitionDetail_patternOvrride']/textarea")));
	}
	
	public WebElement questionDefinitionTblErrorMgsColText(String row) { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_questionDefinitionDetail']//tr["+row+"]/td[@aria-describedby='tbl_questionDefinitionDetail_errMsg']/div")));
	}
	
	public WebElement questionDefinitionTblErrorMgsColtextarea(String row) { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_questionDefinitionDetail']//tr["+row+"]/td[@aria-describedby='tbl_questionDefinitionDetail_errMsg']/textarea")));
	}
	
	public WebElement questionDefinitionTblAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_questionDefinitionDetail_iladd")));
	}
	
	public WebElement questionDefinitionTblEditBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit_tbl_questionDefinitionDetail")));
	}
	
	public WebElement questionDefinitionTblFirstPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='first_tbl_questionDefinitionDetail_pagernav']/span")));
	}
	
	public WebElement questionDefinitionTblPrevPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prev_tbl_questionDefinitionDetail_pagernav']/span")));
	}
	
	public WebElement questionDefinitionTblNextPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_tbl_questionDefinitionDetail_pagernav']/span")));
	}
	
	public WebElement questionDefinitionTblLastPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='last_tbl_questionDefinitionDetail_pagernav']/span")));
	}
	
	public WebElement questionDefinitionTblTotalRecords() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_questionDefinitionDetail_pagernav_right']/div")));
	}
	
	public WebElement qstnTextInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("questn")));
	}
	
	public WebElement patternOverrideInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("patternOvrride")));
	}
	
	public WebElement errorMessageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("errMsg")));
	}
	
	public WebElement oKBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sData")));
	}
	
	public WebElement canCelBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cData")));
	}
	
	public WebElement questionIdFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_questnId")));
	}
	
	public WebElement patternIdFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_patternTypId")));
	}
	
	public WebElement textFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_questn")));
	}
	
	public WebElement patternOverrideFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_patternOvrride")));
	}
	
	public WebElement errorMessageFilterInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_errMsg")));
	}
	
	public WebElement questionDefinitionTblTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gbox_tbl_questionDefinitionDetail']//span[contains(@class,'title')]")));
	}
	
	public WebElement questionDefinitionTblPageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(@id,'sp') and contains(@id,'tbl_questionDefinitionDetail_pagernav')]//parent::td//following-sibling::input")));
	}
	
	public WebElement questionDefinitionTblTotalPagesText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(@id,'sp') and contains(@id,'tbl_questionDefinitionDetail_pagernav')]")));
	}
	
	public void enterNumberPage(int numberPage) throws Exception {
		questionDefinitionTblPageInput().clear();
		questionDefinitionTblPageInput().sendKeys(String.valueOf(numberPage));
		questionDefinitionTblPageInput().sendKeys(Keys.ENTER);
		logger.info("        Enter value to Page input :" + numberPage);
	}

	//audit page
	public WebElement auditTblActionColText(String row) { //row start from 2
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_auditlogwait']//tr["+row+"]/td[@aria-describedby='tbl_auditlogwait_action']")));
	}

	public WebElement messageOfWarningPopup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[contains(@class,'contentBlock')]//div[contains(@class,'unit lastUnit')]/span")));

	}
	
	public WebElement resetBtnOfWarningPopup() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[contains(@class,'ui-dialog-buttonset')]//button[contains(@class,'btn_submit')]")));
	}	
    
    // Question Definition elements
    public WebElement questionDefinitionPageTitle() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));
    }
    
    public WebElement questionDefinitionTblQuestionIdTxt(String rowIndex) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_questionDefinitionDetail']//tr["+rowIndex+"]//*[@aria-describedby='tbl_questionDefinitionDetail_questnId']")));
    }
    
    public WebElement questionDefinitionTblPatternIdTxt(String rowIndex) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_questionDefinitionDetail']//tr["+rowIndex+"]//*[@aria-describedby='tbl_questionDefinitionDetail_patternTypId']")));
    }
    
    public WebElement questionDefinitionTblPatternOverrideTxt(String rowIndex) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_questionDefinitionDetail']//tr["+rowIndex+"]//*[@aria-describedby='tbl_questionDefinitionDetail_patternOvrride']/div")));
    }
    
    public WebElement questionDefinitionTblErrorMsgTxt(String rowIndex) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_questionDefinitionDetail']//tr["+rowIndex+"]//*[@aria-describedby='tbl_questionDefinitionDetail_errMsg']/div")));
    }
    
    public WebElement questionDefinitionTblTotalRecord() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tbl_questionDefinitionDetail_pagernav_right']//div")));
    }
    
    public WebElement questionDefinitionTblNextPageIco() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_tbl_questionDefinitionDetail_pagernav")));
    }
    
    public WebElement warningMsgTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='sectionServerMessages']//p")));
    }
}
