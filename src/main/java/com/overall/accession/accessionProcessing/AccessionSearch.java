package com.overall.accession.accessionProcessing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class AccessionSearch {
	
	private RemoteWebDriver driver;	
	protected Logger logger;

	public AccessionSearch(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	
	public WebElement closeBtn() throws Exception {
		return driver.findElement(By.id("cancel"));
	}
	
	public WebElement accnIdInput() throws Exception {
		return driver.findElement(By.id("AccnId"));
	}

	public WebElement accnIdInput1() throws Exception {
		return driver.findElement(By.id("accnId"));
	}

	public WebElement searchBtn() throws Exception {
		return driver.findElement(By.xpath("//*[@id=\"accessionSearch\"]/div[2]/button[1]"));
	}
	
	public void closePopup() throws Exception{
		closeBtn().click();
		logger.info("Clicked Close button");
	}
	
	public WebElement dosRangeFromInput(){
		return driver.findElement(By.id("DOSFrom"));
	}
	
	public WebElement dosRangeToInput(){
		return driver.findElement(By.id("DOSTo"));
	}

	public WebElement accnSearchTitle(){
		return driver.findElement(By.cssSelector(".titleText"));
	}
	
	//-------------------------------------------------------------------------------------
	public void setAccnId(String str) throws Exception{		
		accnIdInput().sendKeys(str);
		accnIdInput().sendKeys(Keys.TAB);
		logger.info("        Entered Accession ID: " + str);
	}
	
	public void setAccnId1(String accnId) throws Exception{
		accnIdInput1().sendKeys(accnId);
		accnIdInput1().sendKeys(Keys.TAB);
		logger.info("Entered AccnID: " + accnId);
	}
	
	public void setDOSRange(String dosFrom, String dosTo) throws Exception{
		dosRangeFromInput().sendKeys(dosFrom);
		dosRangeFromInput().sendKeys(Keys.TAB);
		dosRangeToInput().sendKeys(dosTo);
		dosRangeToInput().sendKeys(Keys.TAB);
		logger.info("        Entered DOS Range From: " + dosFrom );
		logger.info("        Entered DOS Range To: " + dosTo );		
	}
	
	//Search Button
	public WebElement accnSearchBtn() throws Exception {
		return driver.findElement(By.name("Submit"));
	}	
	
	public void clickSearch() throws Exception{
		accnSearchBtn().click();
		logger.info("Clicked the Search button in Accession Search screen.");
	}	
	
	public void clickSearchBtn() throws Exception{		
		searchBtn().click();		
		logger.info("        Clicked Search Button.");
	}

}
