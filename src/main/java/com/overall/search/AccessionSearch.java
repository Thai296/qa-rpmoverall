package com.overall.search;

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
	
	//Accn ID Input field
	public WebElement accnIdInput() throws Exception {
		return driver.findElement(By.id("accnId"));
	}	
	
	//Search Button
	public WebElement searchBtn() throws Exception {
		return driver.findElement(By.name("Submit"));
	}	
	
	public void setAccnId(String accnId) throws Exception{
		accnIdInput().sendKeys(accnId);
		accnIdInput().sendKeys(Keys.TAB);
		logger.info("        Entered Accession ID: " + accnId);
	}
	
	public void clickSearchBtn() throws Exception{
		searchBtn().click();
		logger.info("        Clicked Search button.");
	}	
	
}
