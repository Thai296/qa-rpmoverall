package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ReasonCodeConfigSearch {
	private RemoteWebDriver driver;	
	protected Logger logger;

	public ReasonCodeConfigSearch(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	public WebElement reasonCodeInput() {
		return driver.findElement(By.id("reasonCode"));
	}
	
	public WebElement shortDescInput() {
		return driver.findElement(By.id("shortDescription"));
	}
	
	public WebElement fieldDdl() {
		return driver.findElement(By.id("s2id_field"));
	}
	
	public WebElement errorTypDdl() {
		return driver.findElement(By.id("s2id_errorType"));
	}
	
	public WebElement errorErrorGrpLbl() {
		return driver.findElement(By.xpath("//*[@id='']//div[contains(@class,'errorGroupDisplay ')]"));
	}
	
	public WebElement reasonCodeTblInput() {
		return driver.findElement(By.id("reasonCodeTable"));
	}
	
	public WebElement reasonCodeTblSearchIco() {
		return driver.findElement(By.xpath("//*[@data-search-type='reasoncodetable']"));
	}
	
	public WebElement closeBtn() {
		return driver.findElement(By.xpath("//*[@value='Close']"));
	}
	
	public WebElement resetBtn() {
		return driver.findElement(By.xpath("//*[@value='Reset']"));
	}
	
	public WebElement searchBtn() {
		return driver.findElement(By.xpath("//*[@value='Submit']"));
	}
	
}

