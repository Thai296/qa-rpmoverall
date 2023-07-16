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

public class ZipCodeSearchResults {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public ZipCodeSearchResults(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	

	//Zip Code search table
	public WebElement zipCodeSearchTable() throws Exception {
		return driver.findElement(By.id("gview_zipCodeSearchTable"));  
	}	
	
	//Zip Code search results table
	public WebElement zipCodeSearchResultsTable() throws Exception {
		return driver.findElement(By.id("zipCodeSearchTable"));  
	}

	//Close Button
	public WebElement closeBtn() throws Exception {
		return driver.findElement(By.name("Close"));
	}	
	
	//New Search Button
	public WebElement newSearchBtn() throws Exception {
		return driver.findElement(By.name("Submit"));
	}
	
	//Page Info for total number of records in the search results
	public String pageInfoText() throws Exception {				
		return (String) ((JavascriptExecutor) driver).executeScript("return $('#pg_pager [class=\"ui-paging-info\"]').text()");		
	}

}
