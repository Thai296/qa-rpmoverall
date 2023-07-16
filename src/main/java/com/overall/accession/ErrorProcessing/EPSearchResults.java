package com.overall.accession.ErrorProcessing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class EPSearchResults {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public EPSearchResults(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement clientIdText() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $($('#oTable > tbody > tr:nth-child(2) > td:nth-child(2)')).get(0)"); 
	}
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("Submit"));
	}
	public WebElement submitBtnPF() {
		return driver.findElement(By.id("btnSubmitAction"));
	}
	public WebElement actionDropdownPF() {
		return driver.findElement(By.id("actions"));
	}
	public WebElement selectCheckBox()   {
		return driver.findElement(By.id("selectChk0"));
	}
	public WebElement selectCheckBoxPF(int rowNum) {
		return driver.findElement(By.xpath(".//*[@id='"+rowNum+"']/td[20]/input"));
	}
}
