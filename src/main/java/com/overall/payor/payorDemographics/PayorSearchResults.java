package com.overall.payor.payorDemographics;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class PayorSearchResults {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public PayorSearchResults(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement payorIdText() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($('#pyrSrchTable > tbody > tr:nth-child(2) > td:nth-child(1) > input')).get(0)"); 
	}
	
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("blue"));
	}
	
	public WebElement payorIdText(String rowNum) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#pyrSrchTable > tbody > tr:nth-child(" + rowNum + ") > td:nth-child(1) > input')[0]"); 
	}

}
