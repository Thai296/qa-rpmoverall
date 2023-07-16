package com.overall.payment.paymentPayments;

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
	
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("blue"));
	}
	
	public WebElement searchResultsTable() throws Exception {
		return driver.findElement(By.id("oTable"));
	}
	
	public WebElement pyrSrchTable() throws Exception {
		return driver.findElement(By.id("pyrSrchTable"));
	}
	
	public WebElement pyrNewSearchBtn() throws Exception {
		return driver.findElement(By.id("btn_newsrch"));
	}
	
	public WebElement searchResultsText(String row, String col) throws Exception {
		//return driver.findElement(By.cssSelector("#oTable > tbody > tr:nth-child(" + row + ") > td:nth-child(" + col + ") > input"));
		return driver.findElement(By.cssSelector("#oTable > tbody > tr:nth-child(" + row + ") > td:nth-child(" + col + ")"));
	}	
	
	public void newSearch() throws Exception{
		pyrNewSearchBtn().click();
		logger.info("        Clicked New Search button");
	}

}
