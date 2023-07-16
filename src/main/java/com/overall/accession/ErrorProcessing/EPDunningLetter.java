package com.overall.accession.ErrorProcessing;

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

public class EPDunningLetter {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public EPDunningLetter(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	//Page
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("blue"));
	}
	
	public WebElement ltrDestinationDropdown() throws Exception {
		return driver.findElement(By.id("ltrDest"));
	}	
	
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btn_submit"));
	}
	
	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.id("btn_reset"));
	}
	
	public WebElement helpBtn() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#contentarea').contents()).contents().find('#helpbtn')[0]).click()"); 
	}
	
	//Dunning Cycle grid
	public WebElement addCycleBtn() throws Exception {
		return driver.findElement(By.id("btnAddCycl"));
	}
	
	public WebElement daysToNextLtr1Input() throws Exception {
		return driver.findElement(By.id("days1"));
	}	
	
	public WebElement ltrCountText1() throws Exception {
		return driver.findElement(By.id("cycle1"));
	}
	
	public WebElement docTypeDropdown1() throws Exception {
		return driver.findElement(By.id("doc1"));
	}
	
	public WebElement daysToNextLtr2Input() throws Exception {
		return driver.findElement(By.id("days2"));
	}	
	
	public WebElement ltrCountText2() throws Exception {
		return driver.findElement(By.id("cycle2"));
	}
	
	public WebElement docTypeDropdown2() throws Exception {
		return driver.findElement(By.id("doc2"));
	}
	
	public WebElement deleteCheckBox(String row) throws Exception {
		return driver.findElement(By.cssSelector("#oTable > tbody > tr:nth-child(" + row + ") > td:nth-child(4)"));
	}
	
	//Error message
	public String fldInputReqErrMsgText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $('[style*=\"padding: 10px;\"]').text()"); 
	}
	
	public WebElement pageValidationErrMsgText() throws Exception {
		return driver.findElement(By.cssSelector("#errorCodeBlockTbl > tbody > tr:nth-child(3) > td:nth-child(2)"));
	}
	
	//-------------------------------------------------------------------------------------
	public void setDaysToNextLtr2(String str) throws Exception{	
		daysToNextLtr2Input().clear();
		daysToNextLtr2Input().sendKeys(str);	
		daysToNextLtr2Input().sendKeys(Keys.TAB);
		logger.info("        Entered Days to Next Letter for the 2nd Dunning Cycle: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setDaysToNextLtr1(String str) throws Exception{	
		daysToNextLtr1Input().clear();
		daysToNextLtr1Input().sendKeys(str);	
		daysToNextLtr1Input().sendKeys(Keys.TAB);
		logger.info("        Entered Days to Next Letter for the 1st Dunning Cycle: " + str);
	}	
	
}
