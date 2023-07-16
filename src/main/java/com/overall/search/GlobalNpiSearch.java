package com.overall.search;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GlobalNpiSearch {
	private WebDriverWait wait;	
	protected Logger logger;

	public GlobalNpiSearch(RemoteWebDriver driver){
		this.wait = new WebDriverWait(driver, 10);
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}
	
	/*Global NPI Search Info*/
	public WebElement globalNpiSearchPageTitle() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='npiGlobalSearch']/section/header/span")));
	}
	
	public WebElement globalNpiInput() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("npi")));
	}
	
	public WebElement globalNpiTypeDropdownList() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("npiType")));
	}	
	
	public WebElement globalNpiOrganizationName() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("providerOrgName")));
	}

	public WebElement globalNpiCloseButton(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@type='button'][@value='Close']")));
	}
	
	public WebElement globalNpiResetButton(){
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@type='button'][@value='Reset']")));
	}
	
	public WebElement globalNpiSearchButton() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@type='button'][@value='Submit']")));
	}
	
	public WebElement globalNpiHelpLink() {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pageHelpLink")));
	}
}
