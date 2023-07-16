package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class PayorSearchResults {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private WebDriverWait wait;

	public PayorSearchResults(RemoteWebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public WebElement newSearchBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Submit")));
	}
	
	public WebElement payorSearchResultTable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorsearchTable")));
	}
	
	public void clickNewSearchBtn() {
		newSearchBtn().click();
		logger.info("        Clicked search button in payor search popup window");
	}
	
	public WebElement pyrSearchResultPageNumberRecordsTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@id,'sp_') and contains(@id,'_pager')]")));
	}
	
	public WebElement address1() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("address1")));
	}
	
	public WebElement pagerBottomRight() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pager_right")));
	}

	public WebElement recordsCount() {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#pager_right > div')[0]");
	}

	public WebElement payorSearchResultsTitleTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,'searchGridHeader')]/span")));
	}
	
	public WebElement pyrAbbrLnk(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//tr[" + row + "]//*[@aria-describedby='payorsearchTable_payorAbbrev']/a")));
	}
	
	public WebElement pyrSearchResultTotalPageTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='sp_1_pager']")));
	}
	
	public WebElement getHeaderOfPayorSearchResults() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_payorsearchTable']/div[1]/span")));
	}
	
	public WebElement totalSearchResultText() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_right']/div")));
	}
	
	public WebElement getNextPagerIconOfPayorSearchResults() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_pager']/span")));
	}
	
	public WebElement getCellInTable(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='payorsearchTable']/tbody/tr[" + row + "]/td[" + col + "]")));
	}
	
	public WebElement getPayorIdCellInTable(int row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='payorsearchTable']/tbody/tr[contains(@class,'ui-widget-content')][" + row + "]/td[2]/a")));
	}
	
	public List<WebElement> pyrSearchResultTblAllRow(){
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='payorsearchTable']//tr[@tabindex]/td[2]")));
    }
	
	public WebElement pyrSearchResultNextPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_pager']/span")));
	}
	
	public WebElement pyrSearchResultFirstPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_pager")));
	}
	
	public WebElement pyrNameTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//tr[" + row + "]//*[@aria-describedby='payorsearchTable_payorName']")));
	}
	
	public WebElement pyrGrpTxt(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//tr[" + row + "]//*[@aria-describedby='payorsearchTable_payorGroupName']")));
	}
	
	public WebElement pyrSearchResultTotalRecordTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_right']/div")));
	}
	
	public WebElement clnPyrPrcsTblTotalRecordTxt() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_clnPyrPrcs_pagernav_right']/div")));
	}
	
	public WebElement clnPyrPrcsTblNextIco() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_tbl_clnPyrPrcs_pagernav']/span")));
	}

	public WebElement address1InPayorSearchResults(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='payorsearchTable']/tbody/tr[" + row + "]/td[@aria-describedby='payorsearchTable_address1']")));
	}
	//added element for refactor
	public WebElement pyrGroupDdl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payorGroupName")));
	}
	
	public void clickPayorSearchResultTablePayorIdCell(int row) {
		getPayorIdCellInTable(row).click();
	}
	
	public WebElement getPayorIdCellInTable(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='payorsearchTable']/tbody/tr[" + row + "]/td[" + col + "]/a")));
	}

	public WebElement getPayorIdInPayorSetUpTable(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tbl_claimStatusConfigDetail']/tbody/tr[" + row + "]/td[" + col + "]")));
	}
	
	public WebElement closeBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Close")));
	}

}
