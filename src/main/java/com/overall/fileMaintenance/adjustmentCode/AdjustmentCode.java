package com.overall.fileMaintenance.adjustmentCode;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdjustmentCode {
    protected Logger logger;
    private WebDriverWait wait;

    public AdjustmentCode(RemoteWebDriver driver) {
        this.wait = new WebDriverWait(driver, 10);
        logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
    }

    public WebElement adjusmentCodePageTitle() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("platormPageTitle")));
    }

    public WebElement adjusmentCodeAdjCdInput() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("adjustmentCode")));
    }
    
    public WebElement adjusmentCodeTypeSel() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("adjustmentCodeType")));
    }
    
    public WebElement headerDetailPageTitleTxt() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sectionServerMessages")));
    }

    /*** General ***/
	public WebElement adjCodePageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='adjustmentCodeForm']//span[@class = 'platormPageTitle']")));
	}
	
	public WebElement adjCodeHeaderHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='adjustmentCodeForm']/div[1]/div/div/div/div[1]/a")));
	}
	
	public WebElement adjCodeFooterHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
	
	public WebElement resetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}
	
	public WebElement saveAndClearBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='btnSaveAndClear']")));
	}
	
	public WebElement runAudiBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("auditBtn")));
	}
	
	/*** Adjustment Code Section ***/
	public WebElement adjCodeTblTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='mainSections']/section/header/div/span/span")));
	}

	public WebElement adjCodeSearchIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']//a[@title = 'Adjustment Code Search']/span")));
	}
	
	public WebElement adjCodeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("adjustmentCode")));
	}
	
	public WebElement select2DropInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='select2-drop']/div/input")));
	}
	
	public WebElement adjCodeTypeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_adjustmentCodeType']/a/span[1]")));
	}
	
	public WebElement adjGLCodeInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_glCode']/a/span[1]")));
	}
	
	public WebElement adjDescriptionInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("adjDescription")));
	}
	
	public WebElement adjNotesInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("notes")));
	}
	
	public WebElement inactiveAdjCodeCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("inactiveAdjCode")));
	}
	
	public WebElement adjCodeSectionHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']/section/div/div[1]/a")));
	}
	
	/***Adjustment Code Cross Reference Table ***/
	public WebElement adjCodeCrossReferenceTblTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']/div[2]/section/header/div[1]/span/span")));
	}
	
	public WebElement adjCodeCrossReferenceTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_adjCodeXref")));
	}
	
	public WebElement adjCodeCrossReferenceTblCelInput(String row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_adjCodeXref']/tbody/tr["+row+"]/td["+col+"]/input")));
	}
		
	public WebElement adjCodeCrossReferenceTblEffDtColInput(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_adjCodeXref']//tr["+ row +"]//td[@aria-describedby='tbl_adjCodeXref_effDate']/input")));
	}
	
	public WebElement adjCodeCrossReferenceTblExpDtColInput(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_adjCodeXref']//tr["+ row +"]//td[@aria-describedby='tbl_adjCodeXref_expDate']/input")));
	}
	
	public WebElement adjCodeCrossReferenceTblDeleteColInput(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_adjCodeXref']//tr["+ row +"]//td[@aria-describedby='tbl_adjCodeXref_deleted']/input")));
	}

	public WebElement adjCodeCrossReferenceAddBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_adjCodeXref_iladd")));
	}
	
	public WebElement adjCodeCrossReferenceDeleteBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jqgh_tbl_adjCodeXref_deleted")));
	}
	
	public WebElement adjCodeCrossReferenceTotalRecords() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_adjCodeXref_pagernav_right']/div")));
	}
	
	public WebElement adjCodeCrossReferenceFirstPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='first_tbl_adjCodeXref_pagernav']/span")));
	}
	
	public WebElement adjCodeCrossReferencePreviousPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prev_tbl_adjCodeXref_pagernav']/span")));
	}
	
	public WebElement adjCodeCrossReferenceNextPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_tbl_adjCodeXref_pagernav']/span")));
	}
	
	public WebElement adjCodeCrossReferenceLastPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='last_tbl_adjCodeXref_pagernav']/span")));
	}
	
	public WebElement adjCodeCrossReferenceHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']/div[2]/section/div/div[1]/a")));
	}
	
	public WebElement adjCodeCrossReferenceEffDateSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_effDate")));
	}
	
	public WebElement adjCodeCrossReferenceExpDateSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_expDate")));
	}
	
	public WebElement adjCodeCrossReferenceXrefSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_xref")));
	}
	
	public WebElement adjCodeCrossReferenceDescriptionDropDownBox(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_adjCodeXref']/tbody/tr["+row+"]/td[@aria-describedby='tbl_adjCodeXref_xref']")));
	}
	
	public WebElement fieldValidationMessageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='messagefor_s2id_jqg2_xref']/div[1]/div")));
	}
	
	public WebElement fieldValidationMessageTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='messagefor_s2id_jqg2_xref']/div[2]")));
	}
	
	public WebElement fieldValidateCloseIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='messagefor_s2id_jqg2_xref']/div[1]/a")));
	}
	
	public WebElement adjCodeTypeDropDownList() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_adjustmentCodeType']/a")));
	}
	
	public WebElement adjGLCodeDropDownList() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_glCode']/a")));
	}
	
	public WebElement fieldValidationForAdjustmentCodeTypeTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='messagefor_s2id_adjustmentCodeType']/div[1]/div")));
	}
	
	public WebElement fieldValidationForAdjustmentCodeTypeText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='messagefor_s2id_adjustmentCodeType']/div[2]")));
	}
	/*** Methods ***/
	public void enterAdjDescription(String adjDescr) throws Exception {
		adjDescriptionInput().clear();
		adjDescriptionInput().sendKeys(adjDescr);
		adjDescriptionInput().sendKeys(Keys.TAB);
		logger.info("        Enter Adj Description: " + adjDescr);
	}
	
	public void enterAdjNotes(String notes) throws Exception {
		adjNotesInput().clear();
		adjNotesInput().sendKeys(notes);
		adjNotesInput().sendKeys(Keys.TAB);
		logger.info("        Enter Adj Notes: " + notes);
	}
	
	public void enterAdjCodeType(String type) throws Exception {
		adjCodeTypeDropDownList().click();
		select2DropInput().sendKeys(type);
		select2DropInput().sendKeys(Keys.TAB);
		logger.info("        Select Adj Code Type: " + type);
	}
	
	public void enterAdjGLCode(String glCode) throws Exception {
		adjGLCodeDropDownList().click();
		select2DropInput().sendKeys(glCode);
		select2DropInput().sendKeys(Keys.TAB);
		logger.info("        Select Adj G/L Code: " + glCode);
	}
	
	public void pressAltR() throws Exception {
		String selectAll = Keys.chord(Keys.ALT, "R");
		WebElement body = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
		body.sendKeys(selectAll);
		logger.info("        press Ctr + R");
	}

	public void clickResetBtn() {
		wait.until(ExpectedConditions.elementToBeClickable(resetBtn()));
		resetBtn().click();
		logger.info("	Clicked Reset button.");
	}
	
	public WebElement adjCodeCrossReferenceTblCel(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_adjCodeXref']/tbody/tr["+row+"]/td["+col+"]")));
	}
	
	public WebElement adjCodeErrorMsg() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='sectionServerMessages']/div/div/ul/li/p")));
	}
	
	public WebElement auditLogWaitTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_auditlogwait")));
	}
}
