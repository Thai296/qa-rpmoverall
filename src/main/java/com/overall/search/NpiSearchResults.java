package com.overall.search;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NpiSearchResults {
	private WebDriverWait wait;
	protected Logger logger;

	public NpiSearchResults(RemoteWebDriver driver){
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*NPI Search Results*/	
	public WebElement npiSearchResultTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_npisearchTable']/div[1]/span")));
	} 
	
	public WebElement npiTable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("npisearchTable")));
	}
	
	public WebElement npiTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='gview_npisearchTable']/div[1]/span")));
	}
	
	public WebElement npiSearchResultsTblNPINumber(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='npisearchTable']/tbody/tr["+ row +"]/td["+ col +"]/a")));
	}
	
	public WebElement npiSearchResultsTblReloadGrid() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='refresh_npisearchTable']/div/span")));
	}
	
	public WebElement npiSearchResultsTblFirstIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='first_pager']/span")));
	}
	
	public WebElement npiSearchResultsTblNpiColLink(String row) {		
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='npisearchTable']//tr["+ row +"]/td[@aria-describedby='npisearchTable_npi']/a")));
	}
	
	public WebElement npiSearchResultsTblPreviousIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prev_pager']/span")));
	}
	
	public WebElement npiSearchResultsTblNumberOfPageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_center']/table/tbody/tr/td[4]/input")));
	}
	
	public WebElement npiSearchResultsTblNumberOfEntirePageLabel() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='sp_1_pager']")));
	}
	
	public WebElement npiSearchResultsTblNextIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_pager']/span")));
	}
	
	public WebElement npiSearchResultsTblLastestIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='last_pager']/span")));
	}
	
	public WebElement npiSearchResultsTblShowNumberOfResultDropdown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_center']/table/tbody/tr/td[8]/select")));
	}
	
	public WebElement  npiSearchResultsTblViewResultsLable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_right']/div")));
	}
	
	public WebElement npiSearchResultsKeepSearchOpenCheckbox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='keepSearchOpen']")));
	}
	
	public WebElement npiSearchResultsHelpLink() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pageHelpLink']")));
	}
	
	public WebElement npiSearchResultsCloseButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='npiSearch']/div[3]/button[2]")));
	}
	
	public WebElement npiSearchResultsNewSearchButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='npiSearch']/div[3]/button[1]")));
	}
	
	public void clickNPISearchResultsTblNPINumber(int row, int col) throws Exception {	
		String NIP =npiSearchResultsTblNPINumber(row,col).getText();
		npiSearchResultsTblNPINumber(row,col).click();		
		logger.info("        Click link " + NIP + " in row " + row);
	}
	
	public void clickNPISearchResultsTblReloadGrid() throws Exception {
		npiSearchResultsTblReloadGrid().click();
		logger.info("        Click on Reload Icon on NPI search results table");
	}
	
	public void clickNPISearchResultsTblFirstIcon() throws Exception {
		npiSearchResultsTblFirstIcon().click();
		logger.info("        Click on First Icon on NPI search results table");		
	}
	
	public void clickNPISearchResultsTblPreviuosicon() throws Exception {
		npiSearchResultsTblPreviousIcon().click();
		logger.info("        Click on Previous Icon on NPI search results table");
	}
	
	public void inputNPISearchResultsTblNumberOfPage(String value) throws Exception {
		npiSearchResultsTblNumberOfPageInput().sendKeys(value);
		logger.info("        Input "+value+"to Number Of Page on NPI search results table");
	}
	
	public void clickNPISearchResultsTblNextIcon() throws Exception {
		npiSearchResultsTblNextIcon().click();
		logger.info("        Click on Next Icon on NPI search results table");
	}
	
	public void clickNPISearchResultsTblLastestIcon() throws Exception {
		npiSearchResultsTblLastestIcon().click();
		logger.info("        Click on Lastest Icon on NPI search results table");
	}
	
	public void selectNPISearchResultsTblShowNumberOfResultsDropdown(String value) throws Exception {
		Select dropdown = new Select(npiSearchResultsTblShowNumberOfResultDropdown());
		dropdown.selectByVisibleText(value);
		logger.info("        Select "+value+" in The Number of Results dropdownn on NPI search results table");
	}
	
	public void clickNPISearchResultsKeepSearchOpenCheckbox() throws Exception {
		npiSearchResultsKeepSearchOpenCheckbox().click();
		logger.info("        Click on Keep Search Open checkbox on NPI search results");
	}
	
	public void clickNPISearchResultsHelplink() throws Exception {
		npiSearchResultsHelpLink().click();
		logger.info("        Click on Help link on NPI search results");
	}
	
	public void clickNPISearchResultsCloseButton() throws Exception {
		npiSearchResultsCloseButton().click();
		logger.info("        Click on Close button on NPI search results");
	}
	
	public void clickNPISearchResultsNewSearchButton() throws Exception {
		npiSearchResultsNewSearchButton().click();
		logger.info("        Click on New Search button on NPI search results");
	}
	
	public WebElement getLinkValueFromWebTable(String tableId, int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='" + tableId + "']/tbody/tr[" + row + "]/td[" + col + "]/a")));		
	}
	
	public WebElement getValueFromWebTable(String tableId, int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='" + tableId + "']/tbody/tr[" + row + "]/td[" + col + "]")));
	}
	
	public WebElement npiSearchResultsTblNpiTypeColTxt(String row) {		
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='npisearchTable']//tr["+ row +"]/td[@aria-describedby='npisearchTable_abbrev']")));
	}
	
	public WebElement npiSearchResultsTblNameColTxt(String row) {		
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='npisearchTable']//tr["+ row +"]/td[@aria-describedby='npisearchTable_name']")));
	}
	
	public WebElement npiSearchResultsTblTotalPagesTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='pager_center']//span[contains(@id,'sp') and contains(@id,'pager')]")));
	}
	
	public List<WebElement> npiSearchResultsTblAllDataRows() {
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='npisearchTable']//tr[@tabindex='-1']")));
	}
	
}
