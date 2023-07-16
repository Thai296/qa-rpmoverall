package com.overall.payor.payorDemographics;

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

public class PayorSearch {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public PayorSearch(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	
	
	public WebElement payorIdInput() throws Exception {
		return driver.findElement(By.id("Abbrv"));
	}
	
	public WebElement searchBtn() throws Exception {
		return driver.findElement(By.id("search"));
	}
	
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("blue"));
	}
	
	public void searchPayorId(String search) throws Exception{
		payorIdInput().sendKeys(search);
		searchBtn().click();
		logger.info("Search Payor: " + search);
	}
	
}
