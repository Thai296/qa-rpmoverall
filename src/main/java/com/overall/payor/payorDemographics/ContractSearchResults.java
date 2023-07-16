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

public class ContractSearchResults {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public ContractSearchResults(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement contractIdText() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($('body > form > table:nth-child(3) > tbody > tr:nth-child(2) > td:nth-child(2) > a')).get(0)"); 
	}
	
	
	
	public void selectContract() throws Exception{
		contractIdText().click();
	}
	
	

}
