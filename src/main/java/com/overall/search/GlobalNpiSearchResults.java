package com.overall.search;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GlobalNpiSearchResults {
	private WebDriverWait wait;	
	protected Logger logger;

	public GlobalNpiSearchResults(RemoteWebDriver driver){
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*Global NPI Search Results*/	
	public WebElement globalNpiSearchResultTable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("npisearchTable")));
	}
	
	public WebElement globalNpiSearchResultTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_npisearchTable']/div[1]/span")));
	}
	
	public WebElement globalNpiSearchResultDataLink(String row) { //row start from 1
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='npisearchTable']/tbody/tr/td/a["+row+"]")));	
	}
	
	public WebElement globalNpiSearchResultResultDataText(int row, int col) { //row start from 1 - Col start from 3
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='npisearchTable']/tbody/tr["+(1+row)+"]/td["+col+"]")));	
	}
	
	public WebElement globalNpiSearchResultReloadGripIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("refresh_npisearchTable")));
	}
	
	public WebElement globalNpiSearchResultFirstPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_pager")));
	}
	
	public WebElement globalNpiSearchResultPrevPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_pager")));
	}
	
	public WebElement globalNpiSearchResultNextPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_pager")));
	}
	
	public WebElement globalNpiSearchResultLastPageIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_pager")));
	}
	
	public WebElement globalNpiSearchResultTotalRecordsLbl() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pager_right")));
	}
	
	public WebElement globalNpiSearchResultCloseBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@type='button'][@value='Close']")));
	}
	
	public WebElement globalNpiSearchResultNewSearchBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@type='button'][@value='Submit']")));
	}
	
	public WebElement keepSearchOpenCheckBox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("keepSearchOpen")));
	}
	
	public WebElement globalNpiSearchResultsTblTotalPagesTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='pager_center']//span[contains(@id,'sp') and contains(@id,'pager')]")));
	}
	
	public List<WebElement> globalNpiSearchResultsTblAllDataRows() {
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='npisearchTable']//tr[@tabindex='-1']")));
	}
	
	public WebElement globalNpiSearchResultsTblNpiColLnk(String row) {		
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='npisearchTable']//tr["+ row +"]/td[@aria-describedby='npisearchTable_npi']/a")));
	}
	
	public WebElement globalNpiSearchResultsTblNpiTypeColTxt(String row) {		
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='npisearchTable']//tr["+ row +"]/td[@aria-describedby='npisearchTable_abbrev']")));
	}
	
	public WebElement globalNpiSearchResultsTblPrvderOrgNameColTxt(String row) {		
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='npisearchTable']//tr["+ row +"]/td[@aria-describedby='npisearchTable_organizationName']")));
	}
	
	public WebElement globalNpiSearchResultsTblProviderNameColTxt(String row) {		
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='npisearchTable']//tr["+ row +"]/td[@aria-describedby='npisearchTable_fullName']")));
	}
	
	public WebElement globalNpiSearchResultsTblViewResultsLable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_right']/div")));
	}
}
