package com.overall.fileMaintenance.fileMaintenanceTables;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;


public class TaxonomyCode {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public TaxonomyCode(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("heading1"));
	}
	

	
	
	//Taxonomy Code
	public WebElement taxonomyDefinition() throws Exception {
		return driver.findElement(By.cssSelector("#tblTaxCode > tbody > tr:nth-child(5) > td.dataCell > div"));
	}
	
	public WebElement taxonomyNotesText() throws Exception {
		return driver.findElement(By.cssSelector("#tblTaxCode > tbody > tr:nth-child(6) > td.dataCell > div"));
	}
	
	public WebElement taxonomyeffectiveDateText() throws Exception {
		return driver.findElement(By.id("taxFromDate"));
	}
	

	

}
