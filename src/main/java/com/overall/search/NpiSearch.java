package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NpiSearch {
	private WebDriverWait wait;
	protected Logger logger;

	public NpiSearch(RemoteWebDriver driver){
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*NPI search*/
	public WebElement npiTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='npiSearch']/section/header/span")));
	}

	public WebElement npiInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='npi']")));
	}
	
	public WebElement npiSearchIconBtn() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//a[@title='NPI Search']/span")));
	}	
	
	public WebElement clientNameInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clientName")));
	}
	

	public void setClientName(String value) throws Exception {
		clientNameInput().sendKeys(value);
		clientNameInput().sendKeys(Keys.TAB);
		
		logger.info("        Entered Client Name "+value);
	}
	
	public void inputNPI(String value) throws Exception {
		npiInput().sendKeys(value);
		logger.info("        Input "+value+" NPI");
	}
	
	public void clickNPISearchIconBtn() throws Exception {
		npiSearchIconBtn().click();
		logger.info("        Click on NPI Search Icon button");
	}
	
	/*NPI Info*/	
	public WebElement npiEntityDropdown() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='npiEntity']")));
	}	
	
	public WebElement facilityInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("facilityName")));
	}
	
	public WebElement firstNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
	}
	
	public WebElement lastNameInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lastName")));
	}
	
	public WebElement npiCloseButton(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/section/div/div/div/button[3]")));
	}
	
	public WebElement npiResetButton(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/section/div/div/div/button[2]")));
	}
	
	public WebElement npiSearchButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/section/div/div/div/button[1]")));
	}
	
	public WebElement npiHelpLink() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pageHelpLink']")));
	}
	
	public void inputNPIFacility(String value) throws Exception {
		facilityInput().sendKeys(value);
		facilityInput().sendKeys(Keys.TAB);
		
		logger.info("        Entered Facility "+value);
	}
	
	public void setFisrtNamet(String value) throws Exception {
		firstNameInput().sendKeys(value);
		firstNameInput().sendKeys(Keys.TAB);
		
		logger.info("        Entered First Name "+value);
	}
	
	public void setLastName(String value) throws Exception {
		lastNameInput().sendKeys(value);
		lastNameInput().sendKeys(Keys.TAB);
		
		logger.info("        Entered Last Name "+value);
	}
	
	public void clickNPICloseButton() throws Exception {
		npiCloseButton().click();
		logger.info("        Click on NPI Close Button");
	}
	
	public void clickNPIResetButton() throws Exception {
		npiResetButton().click();
		logger.info("        Click on NPI Reset Button");
	}
	
	public void clickNPISearchButton() throws Exception {
		npiSearchButton().click();
		Thread.sleep(1000);
		logger.info("        Click on NPI Search Button");
	}
	
	public void clickNPIHelpLink() throws Exception {
		npiHelpLink().click();
		logger.info("        Click on NPI Help Link");
	}
}
