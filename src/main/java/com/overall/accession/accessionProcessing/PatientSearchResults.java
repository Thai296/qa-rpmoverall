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

public class PatientSearchResults {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public PatientSearchResults(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	

	public WebElement patientSearchResultTable() throws Exception {
		return driver.findElement(By.id("patientsearchTable"));
	}	
	
	public WebElement newSearchBtn() throws Exception {
		return driver.findElement(By.name("Submit"));
	}
	
	public void clickNewSearch() throws Exception{
		newSearchBtn().click();		
		Thread.sleep(1000);
		logger.info("        Clicked the New Search button in Patient Search Results screen.");
	}
	
	/*
	public WebElement physicianSearchResultNPI(int row, int col) throws Exception {
		return driver.findElement(By.xpath("//*[@id=" + row + "]/td[" + col + "]/a"));
	}
	*/

}
