package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PhysicianSearch {
	private WebDriverWait wait;
	protected Logger logger;
	public PhysicianSearch(RemoteWebDriver driver){
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public WebElement physcianSearchTitle(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@class,'sectionHeader')]//span[@class='titleText']")));
	}
	
	public WebElement physSrchPageFirstNameInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
	}
	
	public WebElement physSrchPageLastNameInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lastName")));
	}
	
	public WebElement upinInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("upin")));
	}
	
	public WebElement npiInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("npi")));
	}
	
	public WebElement searchBtn(){		
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Submit")));
	}	
	
	//Actions:
	public void setNPIInput(String value)throws Exception {		
		npiInput().sendKeys(value);
		npiInput().sendKeys(Keys.TAB);		
		logger.info("        Entered NPI: " + value);
	}
	
	public void clickSearchBtn() throws Exception {		
		searchBtn().click();				
		logger.info("        Clicked Search button.");
	}
	
	public void enterPhysSrchPageFirstName(String value) {
		physSrchPageFirstNameInput().sendKeys(value);
		logger.info("       Enter " + value + " to First Name ");
	}
	
	public void enterPhysSrchPageLastName(String value) {
		physSrchPageLastNameInput().sendKeys(value);
	}
	
	public void enterPhysSrchPageUpin(String value) {
		upinInput().sendKeys(value);
		logger.info("       Enter "+value+" to Upin Name ");
	}
	
}
