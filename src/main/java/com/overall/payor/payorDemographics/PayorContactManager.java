package com.overall.payor.payorDemographics;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class PayorContactManager {
	
	private RemoteWebDriver driver;	
	protected Logger logger;

	public PayorContactManager(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	/*----------WEB ELEMENT GENERAL----------*/
	public WebElement tableContactManger() throws Exception {
		return driver.findElement(By.id("cntctHdrTable"));			
	}
	public WebElement clientLink() throws Exception {
		return driver.findElementByLinkText("Client");
	}	
	public WebElement submitBtn() throws Exception{
		return driver.findElement(By.id("btn_submit"));
	}
	public WebElement resetBtn() throws Exception{
		return driver.findElement(By.id("btn_reset"));				
	}
	
	/*---------WEB ELEMENT CREATE NEW CLIENT----------*/
	public WebElement payorIdInput(){
		return driver.findElement(By.id("pyrAbbrv"));
	}
	public WebElement payorNameInput(){
		return driver.findElement(By.id("pyrName"));
	}
	public WebElement groupNameInput(){
		return driver.findElement(By.id("grpName"));
	}
		
	/*----------METHOD CREATE NEW CLIENT----------*/
	
	public void clickResetBtn() throws Exception{
		resetBtn().click();
		logger.info("         Click on Reset Button");
	}
	public void inputPayorId(String payorId) throws Exception{
		payorIdInput().sendKeys(payorId);
		payorIdInput().sendKeys(Keys.TAB);
		
		logger.info("        enter payor id: " + payorId);
	}
}
