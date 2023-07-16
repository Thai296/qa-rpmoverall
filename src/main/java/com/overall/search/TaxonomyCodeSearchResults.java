package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TaxonomyCodeSearchResults {
	private WebDriverWait wait;
	protected Logger logger;

	public TaxonomyCodeSearchResults(RemoteWebDriver driver){
		wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public WebElement taxCdSearchResultTbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("taxonomycodesearchresultsTable")));	
	}
	
	public WebElement taxCdSearchResultHelpLink() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));	
	}
	
	public WebElement taxCdSearchResultDataLink(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='taxonomycodesearchresultsTable']//tr["+row+"]//td[@aria-describedby='taxonomycodesearchresultsTable_taxonomyCd']//a")));	
	}
	/**
	 * 
	 * @param row
	 * @param columnName
	 * List of columnNames
	 *  taxonomycodesearchresultsTable_providerTypeDesc
	 * 	taxonomycodesearchresultsTable_classification
	 * 	taxonomycodesearchresultsTable_specialization
	 * 	taxonomycodesearchresultsTable_definition
	 * @return
	 */
	public WebElement taxCdSearchResultDataText(String row, String columnName) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='taxonomycodesearchresultsTable']//tr["+row+"]//td[@aria-describedby='"+ columnName +"']//div")));								  
	}
	
	/**
	 * 
	 * @param row
	 * @param columnName
	 * List of columnNames
	 * taxonomycodesearchresultsTable_classification
	 * taxonomycodesearchresultsTable_specialization
	 * @return
	 */
	public WebElement taxCdSearchResultExpandOrCollapseTextIcon(String row, String columnName) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='taxonomycodesearchresultsTable']//tr["+row+"]//td[@aria-describedby='"+ columnName +"']//span")));
	}
	
	public WebElement taxCdSearchResultReloadGripIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("refresh_taxonomycodesearchresultsTable")));
	}
	
	public WebElement taxCdSearchResultFirstPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_pager")));
	}
	
	public WebElement taxCdSearchResultPrevPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_pager")));
	}
	
	public WebElement taxCdSearchResultNextPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_pager")));
	}
	
	public WebElement taxCdSearchResultLastPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_pager")));
	}
	
	public WebElement taxCdSearchResultTotalRecordsLbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pager_right")));
	}
	
	public WebElement taxCdSearchResultCloseBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@type='button'][@value='Close']")));
	}
	
	public WebElement taxCdSearchResultNewSearchBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@type='button'][@value='Submit']")));
	}
	
	public WebElement keepSearchOpenCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("keepSearchOpen")));
	}

	/**
	 * 
	 * @param row : start from 1
	 * @return
	 */
	public WebElement taxCdSearchResultDataLink(int row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(.//*[@id='taxonomycodesearchresultsTable']/tbody/tr/td/a)["+row+"]")));	
	}
	
	/**
	 * 
	 * @param row: start from 1
	 * @param col: start from 3
	 * @return
	 */
	public WebElement taxCdSearchResultDataText(int row, int col) { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='taxonomycodesearchresultsTable']/tbody/tr["+(1+row)+"]/td["+col+"]/div")));	
	}
	
	/**
	 * 
	 * @param row: start from 1 
	 * @param col: start from 3
	 * @return
	 */
	public WebElement taxCdSearchResultExpandOrCollapseTextIcon(int row, int col) { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='taxonomycodesearchresultsTable']/tbody/tr["+(1+row)+"]/td["+col+"]/span")));	
	}
	
	public WebElement taxCdSearchResultTblHeaderLbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@class,'searchGridHeader')]")));	
	}
}
