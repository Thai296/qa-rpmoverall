package com.overall.fileMaintenance.sysMgt;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SubmissionAndRemittance {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	private WebDriverWait wait;

	public SubmissionAndRemittance(RemoteWebDriver driver, WebDriverWait wait){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
		this.wait=wait;
	}
	
	/**** Header ****/
	public WebElement pagetitle() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='fileMaintSubmRemitForm']/div[1]/div[1]/div/div"));
	}

	public WebElement creationFromDateInput() throws Exception {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dateFrom")));
	}

	public WebElement creationToDateInput() throws Exception {
		return driver.findElement(By.id("dateTo"));
	}

	public WebElement printerConfigDropdown() throws Exception {
		return driver.findElement(By.id("printerConfig"));
	}

	public WebElement helpHeaderIcon() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='fileMaintSubmRemitForm']/div/div[2]/div/div[1]/div[1]/a"));
	}

	/**** Submission ****/
	public WebElement submissionTbl() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='tbl_submissions']"));
	}
	public WebElement submissionTblCell(int row, int col) throws Exception {
		return driver.findElement(By.xpath(".//*[@id='tbl_submissions']/tbody/tr[" + row + "]/td[" + col + "]"));
	}
	public WebElement getTextOfColFileInSubmissionTblCell(int row, int col) throws Exception {
		return driver.findElement(By.xpath(".//*[@id='tbl_submissions']/tbody/tr[" + row + "]/td[" + col + "]/a"));
	}
	public WebElement hideSubmissionTblIcon() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='gview_tbl_submissions']/div[1]/a/span"));
	}
	public WebElement nextPageSubmissionTblIcon() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='next_tbl_submissions_pagernav']/span"));
	}
	public WebElement previousPageSubmissionTblIcon() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='prev_tbl_submissions_pagernav']/span"));
	}
	public WebElement lastPageSubmissionTblIcon() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='last_tbl_submissions_pagernav']/span"));
	}
	public WebElement firstPageSubmissionTblIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='first_tbl_submissions_pagernav']/span"));
	}
	public WebElement currentPageSubmissionTblInput() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='tbl_submissions_pagernav_center']/table/tbody/tr/td[4]/input"));
	}
	public WebElement totalReCordsSubmissionTbl() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='tbl_submissions_pagernav_right']/div"));
	}
	public WebElement helpSubmissionTblIcon() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='mainSections']/div[1]/div/div/section/div/div[1]/a"));
	}
	public WebElement verificationIdInput(int row, int col) throws Exception {
		return driver.findElement(By.xpath(".//*[@id='tbl_submissions']/tbody/tr[" + row + "]/td[" + col + "]/input"));
	}

	public void enterVerificationIdInput(int row, int col, String value) throws Exception {
		verificationIdInput(row, col).clear();
		verificationIdInput(row, col).sendKeys(value);
		logger.info("        Enter into VerificationId Input at row = "+row+" col = "+col);
	}
	
	/**** Remittance ****/
	public WebElement remittanceTbl() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='tbl_remittances']"));
	}

	public WebElement hideRemittanceTblIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='gview_tbl_remittances']/div[1]/a/span"));
	}

	public WebElement nextPageRemittanceTblIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='next_tbl_remittances_pagernav']/span"));
	}

	public WebElement previousPageRemittanceTblIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='prev_tbl_remittances_pagernav']/span"));
	}

	public WebElement lastPageRemittanceTblIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='last_tbl_remittances_pagernav']/span"));
	}

	public WebElement firstPageRemittanceTblIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='first_tbl_remittances_pagernav']/span"));
	}

	public WebElement currentPageRemittanceTblInput() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='tbl_remittances_pagernav_center']/table/tbody/tr/td[4]/input"));
	}

	public WebElement totalReCordsRemittanceTbl() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='tbl_remittances_pagernav_right']/div"));
	}

	public WebElement helpRemittanceTblIcon() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='mainSections']/div[2]/div/div/section/div/div[1]/a"));
	}

	/**** RemittanceReport ****/
	public WebElement remittanceReportTbl() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='tbl_remittanceReports']"));
	}

	public WebElement hideRemittanceReportTblIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='gview_tbl_remittanceReports']/div[1]/a/span"));
	}

	public WebElement nextPageRemittanceReportTblIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='next_tbl_remittanceReports_pagernav']/span"));
	}

	public WebElement previousPageRemittanceReportTblIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='prev_tbl_remittanceReports_pagernav']/span"));
	}

	public WebElement lastPageRemittanceReportTblIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='last_tbl_remittanceReports_pagernav']/span"));
	}

	public WebElement firstPageRemittanceReportTblIcon() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='first_tbl_remittanceReports_pagernav']/span"));
	}

	public WebElement currentPageRemittanceReportTblInput() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='tbl_remittanceReports_pagernav_center']/table/tbody/tr/td[4]/input"));
	}

	public WebElement totalReCordsRemittanceReportTbl() throws Exception{
		return driver.findElement(By.xpath(".//*[@id='tbl_remittanceReports_pagernav_right']/div"));
	}

	public WebElement helpRemittanceReportTblIcon() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='mainSections']/div[3]/div/div/section/div/div[1]/a"));
	}

	/**** AcknowledgementReport ****/
	public WebElement acknowledgementReportTbl() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='tbl_acknowledgedmentReports']"));
	}
	
	public WebElement acknowledgementReportTblCell(int row) throws Exception {
		return driver.findElement(By.xpath(".//*[@id='tbl_acknowledgedmentReports']/tbody/tr["+row+"]"));
	}
	
	public WebElement acknowledgementReportTblCellInput(int row, int col)throws Exception  {
		return driver.findElement(By.xpath(".//*[@id='tbl_acknowledgedmentReports']/tbody/tr["+row+"]/td["+col+"]/input"));
	} 
	
	public WebElement hideAcknowledgementReportTblIcon()throws Exception  {
		return driver.findElement(By.xpath(".//*[@id='gview_tbl_acknowledgedmentReports']/div[1]/a/span"));
	}

	public WebElement nextPageAcknowledgementReportTblIcon() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='next_tbl_acknowledgedmentReports_pagernav']/span"));
	}

	public WebElement previousPageAcknowledgementReportTblIcon()throws Exception  {
		return driver.findElement(By.xpath(".//*[@id='prev_tbl_acknowledgedmentReports_pagernav']/span"));
	}

	public WebElement lastPageAcknowledgementReportTblIcon() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='last_tbl_acknowledgedmentReports_pagernav']/span"));
	}

	public WebElement firstPageAcknowledgementReportTblIcon() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='first_tbl_acknowledgedmentReports_pagernav']/span"));
	}

	public WebElement currentPageAcknowledgementReportTblInput() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='tbl_acknowledgedmentReports_pagernav_center']/table/tbody/tr/td[4]/input"));
	}

	public WebElement totalReCordsAcknowledgementReportTbl() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='tbl_acknowledgedmentReports_pagernav_right']/div"));
	}

	public WebElement helpAcknowledgmentReportTblIcon() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='mainSections']/div[4]/div/div/section/div/div[1]/a"));
	}
	
	public void clickToDisplayHiddenSubmissionFile(int row)throws Exception {
		((JavascriptExecutor) driver).executeScript("return $($.find('span[class=\"ui-icon ui-icon-plus\"]')).click();");
	}
	
	public WebElement hideSubmissionFileTbl(int row)throws Exception {
		return driver.findElement(By.id("tbl_acknowledgedmentReports_"+row+"_table"));
	}
	
	/**** LettersAndReports ****/
	public WebElement lettersAndReportsTbl() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='tbl_letterReports']"));
	}

	public WebElement hideLettersAndReportsTblIcon() throws Exception {
		return driver.findElement(By.xpath("//*[@id='gview_tbl_letterReports']/div[1]/a/span"));
	}

	public WebElement nextPageLettersAndReportsTblIcon() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='next_tbl_letterReports_pagernav']/span"));
	}

	public WebElement previousPageLettersAndReportsTblIcon()throws Exception  {
		return driver.findElement(By.xpath(".//*[@id='prev_tbl_letterReports_pagernav']/span"));
	}

	public WebElement lastPageLettersAndReportsTblIcon() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='last_tbl_letterReports_pagernav']/span"));
	}

	public WebElement firstPageLettersAndReportsTblIcon()throws Exception  {
		return driver.findElement(By.xpath("//*[@id='first_tbl_letterReports_pagernav']/span"));
	}

	public WebElement totalReCordsLettersAndReportsTbl() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='tbl_letterReports_pagernav_right']/div"));
	}

	public WebElement lettersAndReportsTblCell(int row, int col) throws Exception {
		return driver.findElement(By.xpath(".//*[@id='tbl_letterReports']/tbody/tr[" + row + "]/td[" + col + "]"));
	}

	public WebElement lettersAndReportsTblCellLink(int row, int col) throws Exception {
		return driver.findElement(By.xpath(".//*[@id='tbl_letterReports']/tbody/tr[" + row + "]/td[" + col + "]/a"));
	}
	
	public List<WebElement> lettersAndReportsTblCellLinks(int row, int col) throws Exception {
		return driver.findElements(By.xpath(".//*[@id='tbl_letterReports']/tbody/tr[" + row + "]/td[" + col + "]/a"));
	}


	public WebElement lettersAndReportsTblCellChb(int row, int col) throws Exception {
		return driver.findElement(By.xpath(".//*[@id='tbl_letterReports']/tbody/tr[" + row + "]/td[" + col + "]/input"));
	}
	
	public WebElement helpLettersReportTblIcon() throws Exception {
		return driver.findElement(By.xpath(".//*[@id='mainSections']/div[5]/div/div/section/div/div[1]/a"));
	}
	
	public WebElement lettersReportSectionLbl() throws Exception {
		return driver.findElement(By.xpath("//*[@id='mainSections']/div[5]/div/div/section/header/div[1]/span"));
	}
	
	public WebElement LettersandReportsFileFilterInput() throws Exception {
		return driver.findElement(By.id("gs_fileNameLink"));
	}
	
	public WebElement LettersandReportsTblCellLink(int row, int col) throws Exception {
		return driver.findElement(By.xpath("//*[@id='tbl_letterReports']/tbody/tr[2]/td[5]/a"));
	}
	
	/**** Footer ****/
	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.xpath("//*[@id='Reset']/span"));
	}

	public WebElement saveAndClearBtn() throws Exception {
		return driver.findElement(By.xpath("//*[@id='btnSaveAndClear']/div[2]"));
	}

	public WebElement sectionSearchField() throws Exception {
		return driver.findElement(By.xpath("//*[@id='sectionSearchField']"));
	}

	public WebElement helpIcon() throws Exception {
		return driver.findElement(By.xpath("//*[@id='pageHelpLink']/div"));
	}

	public WebElement showClipBoardIcon() throws Exception {
		return driver.findElement(By.xpath("//*[@id='fileMaintSubmRemitForm']/div/div[4]/div/div[1]/div[5]/div/div"));
	}

	public WebElement showKeyBoardShortcut() throws Exception {
		return driver.findElement(By.xpath("//*[@id='fileMaintSubmRemitForm']/div/div[4]/div/div[1]/div[6]/div/div"));
	}
	
	public WebElement submissionSectionLbl() throws Exception {
		return driver.findElement(By.xpath("//*[@id='mainSections']/div[1]/div/div/section/header/div[1]/span/span"));
	}
	
	/**** Functions ****/
	public void inputSectionSearchField(String value) throws Exception {
		sectionSearchField().sendKeys(value);
		logger.info("        Input into Section Search : "+value);
	}
	
	public void setCreationFromAndCreationToDate(String fromDt, String toDt) throws Exception {
		creationFromDateInput().clear();
		Thread.sleep(1000);
		creationFromDateInput().sendKeys(fromDt);
		creationToDateInput().clear();
		Thread.sleep(1000);
		creationToDateInput().sendKeys(toDt);
		creationToDateInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		logger.info("        Entered Creation From Date : "+fromDt+" and To Date "+toDt);
	}
	
	public String getTextOfCelInRemittancesTbl(int row, int col) throws Exception{
		return driver.findElement(By.xpath(".//*[@id='tbl_remittances']/tbody/tr["+row+"]/td["+col+"]")).getText();
	}
	
	public void clickOnTransacDt(int row, int col) throws Exception {
		lettersAndReportsTblCellChb(row, col).click();
		lettersAndReportsTblCellChb(row, col).sendKeys(Keys.TAB);
		logger.info("        Click on tbl cell");
	}
	
	public void setLettersandReportsFileFilterInput(String value) throws Exception{
		LettersandReportsFileFilterInput().clear();
		Thread.sleep(1000);
		LettersandReportsFileFilterInput().sendKeys(value);
		LettersandReportsFileFilterInput().sendKeys(Keys.TAB);
		Thread.sleep(2000);
		logger.info("        Entered Letters and Reports File filter: "+value);
	}

	public WebElement filenameFilterInputInSubmissionsTbl()
	{
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gbox_tbl_submissions']//input[@id = 'gs_fileName']")));
	}
}
