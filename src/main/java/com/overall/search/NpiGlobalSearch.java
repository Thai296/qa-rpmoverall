package com.overall.search;


import org.apache.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;


public class NpiGlobalSearch {
	private RemoteWebDriver driver;	
	protected Logger logger;


	public NpiGlobalSearch(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public WebElement npiGlobalTitle() throws Exception{
		return driver.findElement(By.xpath("//*[@id='npiGlobalSearch']//span[@class='titleText']"));
	}

	public WebElement npiInput() throws Exception{
		return driver.findElement(By.id("npi"));
	}
	
	public WebElement npiTypeInput() throws Exception{
		return driver.findElement(By.id("npiType"));
	}	
	
	public WebElement organizationNameInput() throws Exception{
		return driver.findElement(By.id("providerOrgName"));
	}
	
	public WebElement searchIconBtn() throws Exception{
		return driver.findElement(By.name("Submit"));
	}	
	
	public WebElement firstNameInput() throws Exception{
		return driver.findElement(By.id("providerFirstName"));
	}
	
	public WebElement lastNameInput() throws Exception{
		return driver.findElement(By.id("providerLastName"));
	}
	
}
