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

public class PatientSearch {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public PatientSearch(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement patientIdInput() throws Exception {
		return driver.findElement(By.id("patientId"));
	}	
	
	public WebElement firstNameInput() throws Exception {
		return driver.findElement(By.id("firstName"));
	}
	
	public WebElement lastNameInput() throws Exception {
		return driver.findElement(By.id("lastName"));
	}
	
	public WebElement dobInput() throws Exception {
		return driver.findElement(By.id("dob"));
	}
	
	public WebElement showAllClnFacPtIdCheckbox() throws Exception {
		return driver.findElement(By.id("showAllAssociatedIds"));
	}
	
	//Search Button
	public WebElement searchBtn() throws Exception {
		return driver.findElement(By.name("Submit"));
	}
	
	//Close Button
	public WebElement closeBtn() throws Exception {
		return driver.findElement(By.name("Close"));
	}
	
	public void clickSearch() throws Exception{
		searchBtn().click();	
		Thread.sleep(1000);
		logger.info("        Clicked the Search button in Patient Search screen.");
	}	
	
	public void clickClose() throws Exception{
		closeBtn().click();		
		Thread.sleep(1000);
		logger.info("        Clicked the Close button in Patient Search screen.");
	}
	
	public void setPatientId(String ptId) throws Exception{
		patientIdInput().sendKeys(ptId);
		patientIdInput().sendKeys(Keys.TAB);
		logger.info("        Entered Patient ID: " + ptId);
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
	
	public void setDOB(String dob) throws Exception{
		dobInput().sendKeys(dob);
		dobInput().sendKeys(Keys.TAB);
		logger.info("        Entered Date of Birth: " + dob);
	}
	
}
