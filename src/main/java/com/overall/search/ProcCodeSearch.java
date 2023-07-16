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

public class ProcCodeSearch {
	protected Logger logger;
	private String accnId;
	private DaoManagerPlatform daoManagerPlatform;
	private DaoManagerPatientPortal daoManagerPatientPortal;	
	private DaoManagerXifinRpm daoManagerXifinRpm;
	private SeleniumBaseTest b;
	private TestCodeUtils testCodeUtils;
	private RandomCharacter randomCharacter;
	private WebDriverWait wait;

	public ProcCodeSearch(RemoteWebDriver driver){
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public WebElement procedureCodeSearchTitle()throws Exception{
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='procCodeSearch']/section/header/span")));
	}
	
	/*MOVE FROM TESTCODE.JAVA*/
	public WebElement searchProcedureIdInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("prcId")));
	}
	
	public WebElement searchButtonInSearchScreen() throws Exception{
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Submit")));
	}
	
	public WebElement resetSearchButton(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Cancel")));
	}
	
	public WebElement closeBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Close")));
	}
	
	public void searchProcedureCode(String procedureCode) throws Exception{
		searchProcedureIdInput().sendKeys(procedureCode);
		searchButtonInSearchScreen().click();
		logger.info("        Search with id procedure : " + procedureCode);
	}
	
	public void clickResetSearch(){
		resetSearchButton().click();
		logger.info("        Reset search procedure code clicked");
	}
	
	public void clickCloseBtn() {
		closeBtn().click();
		logger.info("        Click close button in procedure code search popup window");
	}
	
	public void clickSearchBtnInSearchScreen() throws Exception{
		searchButtonInSearchScreen().click();
		Thread.sleep(1000);
		logger.info("        Click Test Code Search Button");
	}
	
	/*CREATE NEW FOR PROC - CODE - SEARCH*/
	public WebElement searchProcDescrInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("descr")));
	}
	
	public WebElement searchProcTypeDropdown(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("procTypeId")));
	}
	
	public WebElement searchRevenueCodeDropdown(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("revenueCode")));
	}
	
	public WebElement searchServiceTypeDropdown(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("svcTyp")));
	}
	
	public WebElement searchServiceLevelDropdown(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("svcLvl")));
	}
	
	public WebElement searchIncDelRecordChecbox(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("includeDeletedRecords")));
	}
	
	public WebElement procCodeSearchIcon(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='tr_procId']/td[1]/div/a/span")));
	}
	
	public WebElement procCodeSearchButton(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='procCodeSearch']/div[2]/button[1]")));
	}
	
	public WebElement procCodeSearchTable() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("proccodesearchTable")));
	}
	
	public WebElement keepSearchOpenCheckbox() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("keepSearchOpen")));
	}
	
	public WebElement procCodeResultLink() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='1']/td[2]/a")));
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
	public void clickProcCodeSearchIcon() throws Exception{
		procCodeSearchIcon().click();
		logger.info("        Click on Procedure Code Search Icon");
	}
	
	public void clickProcCodeSearchButton() throws Exception{
		procCodeSearchButton().click();
		Thread.sleep(2000);
		logger.info("        Click on Procedure Code Search Button");
	}
	
	public void inputSearchProcDescr(String descr) throws Exception{
		searchProcDescrInput().sendKeys(descr);
		logger.info("        Input description search : " + descr);
	}
	
	public void inputProcedureCodeID(String procedureCode) throws Exception{
		searchProcedureIdInput().sendKeys(procedureCode);
		logger.info("        Input procedure code ID : " + procedureCode);
	}
	
	public void clickOnProcCodeResultLink() {
		procCodeResultLink().click();
		logger.info("        Click on Result link in Procedure Code Result table");
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
	public void inputProcDescrInput(String procDes) {
		searchProcDescrInput().sendKeys(procDes);
		logger.info("        Input procedure description : " + procDes);
	}
	
	public WebElement helpIcon() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-help-id='p_procedure_code_search']")));
	}
}

