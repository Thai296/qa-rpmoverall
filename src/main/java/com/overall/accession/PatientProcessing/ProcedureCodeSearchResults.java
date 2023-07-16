package com.overall.accession.PatientProcessing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class ProcedureCodeSearchResults {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public ProcedureCodeSearchResults(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("blue"));
	}
	
	public WebElement procCdText(String row) throws Exception {		
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#rsltTable > tbody > tr:nth-child(" + row + ") > td:nth-child(1) > span > a')[0]");
	}
	
}
