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

public class FeeScheduleSearch {
	private RemoteWebDriver driver;	
	protected Logger logger;
	private String accnId;
	private DaoManagerPlatform daoManagerPlatform;
	private DaoManagerPatientPortal daoManagerPatientPortal;	
	private DaoManagerXifinRpm daoManagerXifinRpm;
	private SeleniumBaseTest b;
	private TestCodeUtils testCodeUtils;
	private RandomCharacter randomCharacter;

	public FeeScheduleSearch(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public WebElement searchFSIcon(){
		return driver.findElement(By.xpath("//*[@id='tr_prcAbbrev']/td[1]/div/a"));
	}
	public WebElement searchBtn(){
		return driver.findElement(By.name("Submit"));
	}
	
	public WebElement resetSearchBtn(){
		return driver.findElement(By.name("Cancel"));
	}
	
	public WebElement closeBtn(){
		return driver.findElement(By.name("Close"));
	}
	
	public WebElement fsIDInput(){
		return driver.findElement(By.id("abbrv"));
	}
	
	public WebElement fsNameInput(){
		return driver.findElement(By.id("descr"));
	}
	
	public WebElement clientInput(){
		return driver.findElement(By.id("cln"));
	}
	
	public WebElement payorInput(){
		return driver.findElement(By.id("pyr"));
	}
	
	public WebElement typeRadio(int typeNumber){
		return driver.findElement(By.id("feescheduleType"+typeNumber));
	}
	
	public WebElement retailRadio(int retialNumber){
		return driver.findElement(By.id("retail"+retialNumber));
	}
	
	public WebElement testBaseRadio(int testBaseNumber){
		return driver.findElement(By.id("testBased"+testBaseNumber));
	}
	
	public WebElement clnBaseRadio(int clnBasedNumber){
		return driver.findElement(By.id("clnBased"+clnBasedNumber));
	}
	
	
	/*METHOD*/
	public void clickSearchFSIcon(){
		searchFSIcon().click();
		logger.info("        Click on Search FS Icon button");
	}
	
	public void clickSearchBtn(){
		searchBtn().click();
		logger.info("        Click on Search button");
	}
	
	public void clickResetSearchBtn(){
		resetSearchBtn().click();
		logger.info("        Click on Reset button");
	}
	
	public void clickCloseBtn(){
		closeBtn().click();
		logger.info("        Click on Close button");
	}
	
	public void inputFSID(String text){
		fsIDInput().clear();
		fsIDInput().sendKeys(text);
		logger.info("        Input "+text+" Fee Schedule ID");
	}
	
	public void inputFSName(String text){
		fsNameInput().clear();
		fsNameInput().sendKeys(text);
		logger.info("        Input "+text+" Fee Schedule Name");
	}
	
	public void inputClient(String text){
		clientInput().clear();
		clientInput().sendKeys(text);
		logger.info("        Input "+text+" Client");
	}
	
	public void inputPayor(String text){
		payorInput().clear();
		payorInput().sendKeys(text);
		logger.info("        Input "+text+" Payor");
	}
	
	public void selectTypeRadio(int typeNumber){
		typeRadio(typeNumber).click();
		logger.info("        Select type radio "+typeNumber);
	}
	
	public void selectRetailRadio(int retialNumber){
		retailRadio(retialNumber).click();
		logger.info("        Select retail radio "+retialNumber);
	}
	
	public void selectTestBaseRadio(int testBaseNumber){
		testBaseRadio(testBaseNumber).click();
		logger.info("        Select test base radio "+testBaseNumber);
	}
	
	public void selectClnBaseRadio(int clnBasedNumber){
		clnBaseRadio(clnBasedNumber).click();
		logger.info("        Select client base radio "+clnBasedNumber);
	}
	
}
