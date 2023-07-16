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

public class AccessionSearchDS {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public AccessionSearchDS(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	
	public WebElement closeBtn() throws Exception {
		return driver.findElement(By.name("Close"));
	}
	
	public WebElement accnIdInput() throws Exception {
		return driver.findElement(By.id("accnId"));
	}
	
	public WebElement searchBtn() throws Exception {
		return driver.findElement(By.name("Submit"));
	}
	
	public void closePopup() throws Exception{
		closeBtn().click();
		logger.info("Clicked Close button");
	}
	
	public WebElement accnSearchGrid(int row, int col) throws Exception {
		return driver.findElement(By.xpath(".//*[@id='accessionsearchTable']/tbody/tr["+row+"]/td["+col+"]/a"));
	}
	//-------------------------------------------------------------------------------------
	public void setAccnId(String str) throws Exception{		
		accnIdInput().sendKeys(str);
		accnIdInput().sendKeys(Keys.TAB);
		logger.info("        Entered Accession ID: " + str);
	}
	
	public void inputAccnID(String text) throws Exception{
		accnIdInput().clear();
		accnIdInput().sendKeys(text);
		logger.info("        Input "+text+" to AccnID");
	}
}
