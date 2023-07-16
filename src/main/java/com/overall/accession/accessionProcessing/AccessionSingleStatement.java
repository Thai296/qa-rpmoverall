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

public class AccessionSingleStatement {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public AccessionSingleStatement(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	
	public WebElement accnIdInput(String accnId) throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#accnId')).val(\"" + accnId + "\")).trigger('onblur')[0]"); 
	}
	
	//Overload
	public WebElement accnIdInput() throws Exception {
		return driver.findElement(By.id("accnId"));
	}
	
	public WebElement statementTypeDropdown() throws Exception {
		return driver.findElement(By.id("submissionSvcID"));
	}
	
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btn_submit"));
	}
	
	
	
	public void selectStatementType(SeleniumBaseTest b, String value) throws Exception{
		b.selectItem(statementTypeDropdown(), value);
		submitBtn().click();
	}
	
	public void searchAccnId(String accnId) throws Exception{
		accnIdInput(accnId);
		logger.info("Entered AccnID: " + accnId);
	}
	

}
