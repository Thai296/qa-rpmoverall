package com.overall.search;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestCodeSearchResults {
	private WebDriverWait wait;
	private RemoteWebDriver driver;	
	protected Logger logger;
	public TestCodeSearchResults(RemoteWebDriver driver){
		this.wait = new WebDriverWait(driver, 10);
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	
	public WebElement testSearchTableResult(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("testsearchTable")));
	}
	
	public WebElement testCodeIdLinkTestCodeSearchResults(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='testsearchTable']/tbody/tr["+ row +"]/td["+ col +"]/a")));
	}

	public WebElement testCdSrchRsltTblTestCodeIdColLnk(String index) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='testsearchTable']/tbody/tr["+ index +"]/td[@aria-describedby='testsearchTable_testAbbrev']/a")));
	}
	
	public List<WebElement> testCdSrchRsltTblAllDataRows(){
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='testsearchTable']//tr[@tabindex='-1']")));
	}

	public WebElement testCdSrchRsltTblTotalPageTxt(){
        return driver.findElement(By.id("sp_1_pager"));
    }

	public WebElement testCodeIdLinkTestCodeSearchResults(String row) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='testsearchTable']//tr["+ row +"]/td[@aria-describedby='testsearchTable_testAbbrev']/a")));
	}

	public WebElement newSearchButton(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Submit")));
	}

	public WebElement firstPageButtonOnSearchResult(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='first_pager']/span")));
	}

	public WebElement prevPageButtonOnSearchResult(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='prev_pager']/span")));
	}

	public WebElement nextPageButtonOnSearchResult(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='next_pager']/span")));
	}

	public WebElement lastPageButtonOnSearchResult(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='last_pager']/span")));
	}
	
	public WebElement entriesPerPageOnSearchResult(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_center']/table/tbody/tr/td[8]/select")));
	}

	public WebElement currentPageOnSearchResultPage(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_center']/table/tbody/tr/td[4]/input")));
	}
	
	public WebElement totalPageOnSearchResultPage(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='sp_1_pager']")));
	}

	public WebElement rightPagerTableResultSearch(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_right']/div")));
	}
	
	public WebElement closeBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Close")));
	}
	
	public void clickCloseBtn() throws InterruptedException {
		closeBtn().click();
		Thread.sleep(1000);
		logger.info("        Click close button in test code search popup window");
	}
	
	public void clickNewSearch() throws Exception {
		newSearchButton().click();
		Thread.sleep(2000);
		logger.info("        Clicked new Search Button");
	}
	
	public void clickLastPageButton(){
		lastPageButtonOnSearchResult().click();
		logger.info("        Clicked last page button");
	}
	
	public void clickFirstPageButton() throws Exception {
		firstPageButtonOnSearchResult().click();
		Thread.sleep(3000);
		logger.info("        Clicked first page button");
	}
	
	public void clickPrevPageButton() throws Exception {
		prevPageButtonOnSearchResult().click();
		Thread.sleep(3000);
		logger.info("        Clicked previous page button");
	}
	
	public void clickNextPageButton() throws Exception {
		nextPageButtonOnSearchResult().click();
		Thread.sleep(3000);
		logger.info("        Clicked next page button");
	}
				
	public List<WebElement> testCodeSearchResultsTblAllRow()  {
        return driver.findElements(By.xpath("//*[@id='testsearchTable']//tr[@tabindex]/td[2]"));
    }
	
	public WebElement testCodeSearchResultTitle() {
		return driver.findElement (By.xpath(".//*[@id='gview_testsearchTable']//span"));
	}
	
	public void clickOnSearchResultsTestCodeIdLink(int row, int col) {	
		String testCodeId = testCodeIdLinkTestCodeSearchResults(row,col).getText();
		testCodeIdLinkTestCodeSearchResults(row,col).click();		
		logger.info("        Click link " + testCodeId + " in row " + row);
	}
		
}
