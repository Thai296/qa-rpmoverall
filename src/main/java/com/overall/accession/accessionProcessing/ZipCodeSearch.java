package com.overall.accession.accessionProcessing;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class ZipCodeSearch {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public ZipCodeSearch(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	//Zip Code Input field
	public WebElement zipCodeInput() throws Exception {
		return driver.findElement(By.id("zip"));
	}	
	
	//City Input field
	public WebElement cityInput() throws Exception {
		return driver.findElement(By.id("city"));
	}
	
	//State drop down list
	public WebElement stateDropDown() throws Exception {
		return driver.findElement(By.id("stateDroplist"));
	}
		
	//Search Button
	public WebElement searchBtn() throws Exception {
		return driver.findElement(By.name("Submit"));
	}	
	
	//Validation Error message
	public String validationErrText() throws Exception {				
		return (String) ((JavascriptExecutor) driver).executeScript("return $('#messagefor_message0').text()");		
	}
	
	//Close Button in Validation Error message window
	public WebElement validationErrCloseBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('a[title=\"Close\"]')[0].click()"); 
	}	
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	public void setZipCode(String str) throws Exception{
		zipCodeInput().sendKeys(str);
		zipCodeInput().sendKeys(Keys.TAB);
		Thread.sleep(1000);
		logger.info("        Entered Zip Code: " + str);
	}
	
}
