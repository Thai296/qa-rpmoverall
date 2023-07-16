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

public class ContractSearch {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public ContractSearch(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	
	
	public WebElement adminContractInput() throws Exception {
		return driver.findElement(By.name("adminNm"));
	}
	
	public WebElement searchBtn() throws Exception {
		return driver.findElement(By.name("search"));
	}
	
	
	
	public void searchContract(String search) throws Exception{
		adminContractInput().sendKeys(search);
		searchBtn().click();
		logger.info("Search Contract: " + search);
	}
	
}
