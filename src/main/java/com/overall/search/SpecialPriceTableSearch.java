package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SpecialPriceTableSearch {
	private RemoteWebDriver driver;	
	private WebDriverWait wait;
	protected Logger logger;
	
	public SpecialPriceTableSearch(RemoteWebDriver driver){
		this.driver = driver;
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
		
	public WebElement helpIco(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
	
	public WebElement titleTxt(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='feeScheduleSearch']//span[text()='Special Price Table Search']")));
	}
	
	public WebElement specialPriceTableIdInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("abbrv")));
	}
	
	public WebElement nameInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("descr")));
	}
	
	public WebElement clientInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cln")));
	}
	
	public WebElement payorInput(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pyr")));
	}
	
	public WebElement clientBasedRad(int clnBasedNumber){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("clnBased"+clnBasedNumber)));
	}
	
	public WebElement searchBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Submit")));
	}
	
	public WebElement resetSearchBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Cancel")));
	}
	
	public WebElement closeBtn(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Close")));
	}
}
