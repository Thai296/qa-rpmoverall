package com.overall.fileMaintenance.crossReferenceConfig;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CrossReferenceConfig {
	private WebDriverWait wait;
	protected Logger logger;
	private RemoteWebDriver driver;

	
	public CrossReferenceConfig(RemoteWebDriver driver) {
		this.driver = driver;
		this.wait= new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public WebElement resetBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Reset")));
	}
	
	public WebElement saveAndClearBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSaveAndClear")));
	}
	
	public WebElement runAuditBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("auditBtn")));
	}
	
	public WebElement crossReferenceConfigTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@class='platormPageTitle']")));
	}
	
	public WebElement categoryDropDownBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_xrefCatIdSel']/a")));
	}
	
	public WebElement typeDropDownBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='s2id_xrefTypIdSel']/a")));
	}
	
	public WebElement select2DropInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='select2-drop']/div/input")));
	}
	
	public WebElement headerHelpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_cross_reference_configuration_header']")));
	}
	
	//Element of Cross Reference Members Table
	public WebElement titleMainSections() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='mainSections']/div[2]/section/header/div[1]/span/span")));
	}
	
	public WebElement typRequiredCheck() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("xrefTypReqChkd")));
	}
	
	public WebElement allowDupTypeCheck() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("xrefTypDupChkd")));
	}
	
	public WebElement singleUseXrefsCheck() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("xrefTypeSingleChkd")));
	}
	
	public WebElement headerCrossReferenceMembersTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_tbl_crossReferenceMember']/div[1]/span[1]")));
	}
	
	public WebElement totalRecordInTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_crossReferenceMember_pagernav_right']/div")));
	}
	
	public WebElement crossReferenceMembersTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbl_crossReferenceMember")));
	}

	public WebElement abbrevSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_abbrev")));
	}
	
	public WebElement descrSearchInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gs_descr")));
	}
	
	public WebElement descrInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("descr")));
	}
	
	public WebElement addTblIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='add_tbl_crossReferenceMember']/div")));
	}
	
	public WebElement editTblIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='edit_tbl_crossReferenceMember']/div")));
	}
	
	public WebElement firstTblIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='first_tbl_crossReferenceMember_pagernav']/span")));
	}
	
	public WebElement prevTblIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prev_tbl_crossReferenceMember_pagernav']/span")));
	}
	
	public WebElement lastTblIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='last_tbl_crossReferenceMember_pagernav']/span")));
	}

	public WebElement cellRowInTbl(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_crossReferenceMember']/tbody/tr["+row+"]/td["+col+"]")));
	}
	
	public WebElement cellRowCheckDelInTbl(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_crossReferenceMember']/tbody/tr["+row+"]/td["+col+"]/input")));
	}
	
	public WebElement helpIconOfCrossReferenceMember() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_cross_reference_configuration_load_category_type']")));
	}
	public void selectCategoryDropdown(String value) {
		categoryDropDownBox().click();
		select2DropInput().sendKeys(value);
		select2DropInput().sendKeys(Keys.ENTER);
		logger.info("        Select Category dropdown list: " + value);
	}
	
	public void selectTypeDropdown(String value) throws Exception {
		select2DropInput().click();
		select2DropInput().sendKeys(value);
		select2DropInput().sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		logger.info("        Select Type dropdown list: " + value);
	}
	
	public void sendTabIntoElement(WebElement webElement) {
		typeDropDownBox().sendKeys(Keys.TAB);
	}
	
	public WebElement nextPagerIconInAuditDetailTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_pager']/span")));
	}
	
	public WebElement totalPageNumbers() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sp_1_tbl_crossReferenceMember_pagernav")));
	}
	
	public WebElement abbrevInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("abbrev")));
	}
	
	public WebElement addRecordPopupTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='edithdtbl_crossReferenceMember']/span")));
	}
	
	public void sendAbbrevInput(String abbrev) {
		abbrevInput().clear();
		abbrevInput().sendKeys(abbrev);
		abbrevInput().sendKeys(Keys.TAB);
		logger.info("        Input Abbrev information:" + abbrev);
	}
	
	public void sendDescInput(String desc) {
		descrInput().clear();
		descrInput().sendKeys(desc);
		descrInput().sendKeys(Keys.TAB);
		logger.info("        Input Description information:" + desc);
	}


	public void sendAbbrevSearchInput(String abbrev) throws Exception {
		abbrevSearchInput().clear();
		abbrevSearchInput().sendKeys(abbrev);
		abbrevSearchInput().sendKeys(Keys.TAB);
		Thread.sleep(5000);
		logger.info("        Input Abbrev information:" + abbrev);
	}

	public WebElement okBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sData")));
	}
	
	public WebElement canCelBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cData")));
	}

	public WebElement errMessSection() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='sectionServerMessages']/div/div[1]/ul/li/p")));
	}

	
	public WebElement maxRecordOnpage() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_crossReferenceMember_pagernav_center']/table/tbody/tr/td[8]/select")));
	}
	
	public WebElement nextIconInCrossReferenMem() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_tbl_crossReferenceMember_pagernav']/span")));
	}
	
	public WebElement warningMessage(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ui-id-11")));
	}
	
	public WebElement warningOkBtn() {
		return (WebElement) ((JavascriptExecutor) wait).executeScript("return $($.find('div[aria-describedby=\"effDateDataHasChangedDialog\"] .ui-dialog-buttonset > button:nth-child(1)')).get(0)");
	}

	public WebElement lastCellRowCheckDelInTbl(int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_crossReferenceMember']/tbody/tr[last()]/td["+col+"]/input")));
	}

	// Create New Type Elements

	public WebElement createNewTypeLnk() {return driver.findElement(By.linkText("Create New Type"));


	}

	public WebElement createNewTypePopUpHeader() {
		return driver.findElementById("ui-id-1");
	}

	public WebElement abbrevTypeInput() {
		return driver.findElementById("crossRefTypAbbrev");

	}

	public WebElement abbrevDescrInput() {
		return driver.findElementById("crossRefTypDescr");

	}



	public void sendAbbrevTypeInput(String abbrev) throws Exception {
		abbrevTypeInput().clear();
		abbrevTypeInput().sendKeys(abbrev);
		abbrevTypeInput().sendKeys(Keys.TAB);
		logger.info("        Input Abbrev  Type information:" + abbrev);
	}

	public void sendAbbrevDescrInput(String abbrev) throws Exception {
		abbrevDescrInput().clear();
		abbrevDescrInput().sendKeys(abbrev);
		abbrevDescrInput().sendKeys(Keys.TAB);

	}

	public WebElement newTypOkBtn() {
		return driver.findElement(By.xpath("/html/body/div[4]/div[3]/div/button[2]"));

		}

}


