package com.overall.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;

import com.eviware.soapui.support.StringUtils;
import com.xifin.mars.dao.DaoManagerXifinRpm;
import com.xifin.patientportal.dao.DaoManagerPatientPortal;
import com.xifin.platform.dao.DaoManagerPlatform;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;
import com.xifin.utils.RandomCharacter;
import com.overall.utils.TestCodeUtils;

public class ProcCodeSearchResults {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private String accnId;
	private DaoManagerPlatform daoManagerPlatform;
	private DaoManagerPatientPortal daoManagerPatientPortal;	
	private DaoManagerXifinRpm daoManagerXifinRpm;
	private SeleniumBaseTest b;
	private TestCodeUtils testCodeUtils;
	private RandomCharacter randomCharacter;
	private WebDriverWait wait;

	public ProcCodeSearchResults(RemoteWebDriver driver){
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*Element*/
	public WebElement procCodeSearchResultsTableHeader(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gview_proccodesearchTable")));
	}
	
	public WebElement pageHelpLink(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
	
	public WebElement keepSearchOpenCheckbox(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("keepSearchOpen")));
	}
	
	public WebElement newSearchBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='procCodeSearch']/div[3]/button[1]")));
	}
	
	public WebElement closeSearchBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='procCodeSearch']/div[3]/button[2]")));
	}
	
	public WebElement pagerBottomRight(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pager_right")));
	}
	
	public WebElement selectEntryPage(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_center']/table/tbody/tr/td[8]/select")));
	}
	
	public WebElement jumpPageInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_center']/table/tbody/tr/td[4]/input")));
	}
	public WebElement procedureIdLinkTestCodeSearchResults(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='proccodesearchTable']/tbody/tr["+ row +"]/td["+ col +"]/a")));
	}
	
	public WebElement procedureNameLinkTestCodeSearchResults(int row, int col) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='proccodesearchTable']/tbody/tr["+ row +"]/td["+ col +"]")));
	}
	
	public WebElement rightPagerTableResultSearch(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pager_right']/div")));
	}
	
	public WebElement procCodeSearchPage(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("procCodeSearch")));
	}
	
	public WebElement procTypeDropdown(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("procTypeId")));
	}
	
	public WebElement procCodeSearchResultTable(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("proccodesearchTable")));
	}
	
	public WebElement lastPagerDisabledBtn() {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#last_pager[class*=\"disabled\"]')[0]");
	}
	
	public WebElement recordsCount() throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#pager_right > div')[0]");
	}

	public WebElement recordsCountRight() throws Exception {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"pager_right\"]/div")));
	}
	public WebElement procCodeSearchTable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("proccodesearchTable")));
	}
	
	public WebElement procCodeResultLink() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='1']/td[2]/a")));
	}
	
	public WebElement closeBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Close")));
	}
	
	public WebElement currentPageInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='pager_center']/table/tbody/tr/td[4]/input")));
	}
	
	public WebElement nextPageButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("next_pager")));
	}
	
	public WebElement prevPageButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prev_pager")));
	}
	
	public WebElement firstPageButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first_pager")));
	}
	
	public WebElement lastPageButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("last_pager")));
	}
	
	public WebElement totalPageSpan() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sp_1_pager")));
	}
	
	public WebElement numberOfEntriesDropdown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='pager_center']/table/tbody/tr/td[8]/select")));
	}	
	
	/*Method*/
	public void clickOnProcCodeResultLink() {
		procCodeResultLink().click();
		logger.info("        Click on Result link in Procedure Code Result table");
	}
		
	public void clickCloseBtn() {
		closeBtn().click();
		logger.info("        Click close button in procedure code search popup window");
	}
	
	public void clickOnSearchResultsProcedureIdLink(int row, int col) throws Exception {	
		String procId = procedureIdLinkTestCodeSearchResults(row,col).getText();
		procedureIdLinkTestCodeSearchResults(row,col).click();	
		Thread.sleep(1000);
		logger.info("        Click link " + procId + " in row " + row);		
	}
	
	public void inputJumpPageInput(String value) throws InterruptedException{
		jumpPageInput().sendKeys(value);
		jumpPageInput().sendKeys(Keys.ENTER);
		Thread.sleep(1000);
		logger.info("        Input: " + value);
	}
	
	public void clickPageHelpLink(){
		pageHelpLink().click();
		logger.info("        Click Page Help Link ");
	}
	
	public void clickCloseSearchBtn(){
		closeSearchBtn().click();
		logger.info("        Click Close Search Button ");
	}
	
	public void clickNewSearchBtn(){
		newSearchBtn().click();
		logger.info("        Click New Search Button ");
	}
	
	public void clickOnNextPageButton() throws Exception {
		nextPageButton().click();
		Thread.sleep(3000);
		logger.info("        Clicked on Next page button at the bottom menu");
	}
	
	public void clickOnPrevPageButton() throws Exception {
		prevPageButton().click();
		Thread.sleep(3000);
		logger.info("        Clicked on Prev page button at the bottom menu");
	}
	
	public void clickOnFirstPageButton() throws Exception {
		firstPageButton().click();
		Thread.sleep(1000);
		logger.info("        Clicked on First page button at the bottom menu");
	}
	
	public void clickOnLastPageButton() throws Exception {
		lastPageButton().click();
		Thread.sleep(1000);
		logger.info("        Clicked on Last page button at the bottom menu");
	}
	
	public WebElement procCodeSearchResultTblProcCdColLnk(String row){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='proccodesearchTable']//tr[" + row + "]/*[@aria-describedby='proccodesearchTable_procId']/a")));
	}
}

