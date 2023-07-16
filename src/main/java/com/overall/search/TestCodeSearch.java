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
import org.openqa.selenium.support.ui.Select;
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

public class TestCodeSearch {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private String accnId;
	private DaoManagerPlatform daoManagerPlatform;
	private DaoManagerPatientPortal daoManagerPatientPortal;	
	private DaoManagerXifinRpm daoManagerXifinRpm;
	private SeleniumBaseTest b;
	private TestCodeUtils testCodeUtils;
	private RandomCharacter randomCharacter;

	public TestCodeSearch(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public WebElement testCodeSearchIconBtn() throws Exception{
		return driver.findElement(By.id("test_code_search_btn"));
	}

	public WebElement testCodeIdSearchPage(){
		return driver.findElement(By.name("testAbbrev"));
	}
	
	public WebElement searchButtonInSearchScreen() throws Exception{
		return driver.findElement(By.name("Submit"));
	}
	
	public WebElement labTestCodeIdInputSearchPage(){
		return driver.findElement(By.id("labTestCode"));
	}

	public WebElement facilityDropdownOnSearchPage(){
		return driver.findElement(By.id("facility"));
	}

	public WebElement procCodeInputSearchPage(){
		return driver.findElement(By.id("procCode"));
	}
	public WebElement procCodeDescriptionInputSearchPage(){
		return driver.findElement(By.id("procCodeDescription"));
	}

	public WebElement procCodeTypeOnSearchPage(){
		return driver.findElement(By.id("procCodeTypeUserSelected"));
	}

	public WebElement resetSearchButton(){
		return driver.findElement(By.name("Cancel"));
	}

	public WebElement closeSearchButton(){
		return driver.findElement(By.name("Close"));
	}

	public WebElement testNameInput() throws Exception{
		return driver.findElement(By.id("testName"));
	}

	public WebElement procedureIdLinkTestCodeSearchResults(int row, int col) {
		return driver.findElement (By.xpath("//*[@id='proccodesearchTable']/tbody/tr["+ row +"]/td["+ col +"]/a"));
	}

	public WebElement procedureNameLinkTestCodeSearchResults(int row, int col) {
		return driver.findElement (By.xpath("//*[@id='proccodesearchTable']/tbody/tr["+ row +"]/td["+ col +"]"));
	}

	public WebElement searchOptionProfile(int index){
		return driver.findElement(By.id("profileFlag"+index));
	}

	public WebElement sortByDropDown(){
		return driver.findElement(By.id("sortBy"));
	}

	public WebElement saveInProgressInfoText() throws Exception {
		return driver.findElement(By.id("messagefor_message0")); 
	}	

	public void setTestName(String testName) throws Exception{
		testNameInput().sendKeys(testName);
		logger.info("        Enterred TestName: "  + testName);
	}
	
	public void clickCloseSearch(){
		closeSearchButton().click();
		logger.info("        Close search clicked");
	}

	public void clickResetSearch(){
		resetSearchButton().click();
		logger.info("        Reset search clicked");
	}

	public void setprocCodeOnSearchPage(String procCode){
		procCodeInputSearchPage().sendKeys(procCode);
		logger.info("        Entered "+procCode+" to Procedure Code Info");
	}
	public void setProcCodeDescriptionOnSearchPage(String procCodeDescription){
		procCodeDescriptionInputSearchPage().sendKeys(procCodeDescription);
		logger.info("        Entered "+procCodeDescription+" Procedure Code Info");
	}

	public void setLabTestCodeId(String labTestCode){
		labTestCodeIdInputSearchPage().sendKeys(labTestCode);
		logger.info("        Entered "+labTestCode+" to Lab Test Code ID");
	}

	public void setTestCodeIdSearchPage(String testCodeId){
		testCodeIdSearchPage().sendKeys(testCodeId);
		logger.info("        Entered Test Code ID : " + testCodeId);
	}
	
	public void clickOnSearchResultsProcedureIdLink(int row, int col) throws Exception {	
		String procId = procedureIdLinkTestCodeSearchResults(row,col).getText();
		procedureIdLinkTestCodeSearchResults(row,col).click();		
		logger.info("        Click link " + procId + " in row " + row);

	}

	public void clickSearchBtnInSearchScreen() throws Exception{
		searchButtonInSearchScreen().click();
		Thread.sleep(3000);
		logger.info("        Click Test Code Search Button");
	}

	public void clickTestCodeSearchIconBtn() throws Exception{
		testCodeSearchIconBtn().click();
		logger.info("        Click Test Code Search icon Button");
	}
	
}

