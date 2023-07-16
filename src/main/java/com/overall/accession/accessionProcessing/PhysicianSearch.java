package com.overall.accession.accessionProcessing;

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

public class PhysicianSearch {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public PhysicianSearch(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public String pageTitleText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $('[class=\"titleText\"]').text()"); 
	}	
	
	//NPI Input field
	public WebElement npiInput() throws Exception {
		return driver.findElement(By.id("npi"));	
	}	
	
	//UPIN Input field
	public WebElement upinInput() throws Exception {
		return driver.findElement(By.id("upin"));
	}
	
	//fName Input field
	public WebElement firstNameInput() throws Exception {
		return driver.findElement(By.id("firstName"));
	}
	
	//lName Input field
	public WebElement lastNameInput() throws Exception {
		return driver.findElement(By.id("lastName"));
	}
	
	//Credentials drop-down list
	public WebElement credentialsDropdown() throws Exception {
		return driver.findElement(By.id("credential"));
	}	
		
	//Search Button
	public WebElement searchBtn() throws Exception {
		return driver.findElement(By.name("Submit"));
	}

	//Client abbrev field
	public WebElement physicianSearchClnId() {
		return driver.findElement(By.id("clientAbbrev"));
	}

	public void setFirstName(String fName) throws Exception{
		firstNameInput().sendKeys(fName);
		firstNameInput().sendKeys(Keys.TAB);
		logger.info("        Entered First Name: " + fName);
	}
	
	public void setLastName(String lName) throws Exception{
		lastNameInput().sendKeys(lName);
		lastNameInput().sendKeys(Keys.TAB);
		logger.info("        Entered Last Name: " + lName);
	}
	
	public void setNPI(String npi) throws Exception{
		npiInput().sendKeys(npi);
		npiInput().sendKeys(Keys.TAB);
		logger.info("        Entered NPI: " + npi);
	}
	
	public void setUPIN(String upin) throws Exception{
		upinInput().sendKeys(upin);
		upinInput().sendKeys(Keys.TAB);
		Thread.sleep(3000);
		logger.info("        Entered UPIN: " + upin);
	}

	public void setCLNID(String clnid) throws Exception{
		physicianSearchClnId().sendKeys(clnid);
		physicianSearchClnId().sendKeys(Keys.TAB);
		Thread.sleep(3000);
		logger.info("        Entered CLNID: " + clnid);
	}
	
	public void setCredentials(String credential) throws Exception{
		Select oSelection = new Select(credentialsDropdown());
		oSelection.selectByValue(credential);			
		logger.info("        Selected Credential: " + credential);
	}
	
	public void clickSearch() throws Exception{
		searchBtn().click();		
		logger.info("        Clicked the Search button in Physician Search screen.");
	}

}
