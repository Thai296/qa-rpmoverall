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

public class ReasonCodeSearchResults {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public ReasonCodeSearchResults(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement reasonCodeSearchResultText(int row, int col) throws Exception {							
		return driver.findElement(By.cssSelector("#rsnCdSrchRslts > div > table > tbody > tr:nth-child(" + row + ") > td:nth-child(" + col + ")"));		
	}
	
	public WebElement reasonCodeSearchResultTable(int row, int col, String overrideCd, String overrideCdAbbrev) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$('#rsnCdSrchRslts > div > table > tbody > tr:nth-child(" + row + ") > td:nth-child(" + col + ")').click(setErrForTransDetail(" + overrideCd + ",'" + overrideCdAbbrev + "','bpcTable'))"); 
	}

}
