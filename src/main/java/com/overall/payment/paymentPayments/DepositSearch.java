package com.overall.payment.paymentPayments;

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

public class DepositSearch {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public DepositSearch(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	public WebElement depositDateRangeCheckBox() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#depositsLedger :input[name=depPeriodChkBx][value=1]')[0]"); 
	}
	
	public WebElement depositDateRangeFromInput() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#depositsLedger #dtFrom')[0]"); 
	}	
	
	public WebElement depositDateRangeToInput() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#depositsLedger #dtTo')[0]"); 
	}	
	
	public WebElement searchBtn() throws Exception {
		return driver.findElement(By.id("display"));
	}
	
	public WebElement depositAmountRangeFromInput() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#depositsLedger #amtFrom')[0]"); 
	}	
	
	public WebElement depositAmountRangeToInput() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#depositsLedger #amtTo')[0]"); 
	}	
	
	public WebElement depositPeriodDropDown() throws Exception {
		return driver.findElement(By.id("depositPeriod"));
	}
	
	public WebElement fileNameInput() throws Exception {
		return driver.findElement(By.id("fileName"));
	}
	
	public WebElement bankDropDown() throws Exception {
		return driver.findElement(By.id("bankFilter"));
	}
	
	public WebElement payorIDInput() throws Exception {
		return driver.findElement(By.id("pyrAbbrev"));
	}
	
	public WebElement eftIDInput() throws Exception {
		return driver.findElement(By.id("eftID"));
	}
	
	//-------------------------------------------------------------------------------------
	public void setEFTID(String str) throws Exception{		
		eftIDInput().sendKeys(str);
		eftIDInput().sendKeys(Keys.TAB);
		logger.info("        Entered EFT ID: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setPayorID(String str) throws Exception{		
		payorIDInput().sendKeys(str);
		payorIDInput().sendKeys(Keys.TAB);
		logger.info("        Entered Payor ID: " + str);
	}
		
	//------------------------------------------------------------------------------------------------------------------------------
	public void setBank(String value) throws Exception{
        Select oSelection = new Select(bankDropDown());
        oSelection.selectByValue(value);      
        logger.info("        Selected Bank: " + value);       
	}
	
	//-------------------------------------------------------------------------------------
	public void setFileName(String str) throws Exception{		
		fileNameInput().sendKeys(str);
		fileNameInput().sendKeys(Keys.TAB);
		logger.info("        Entered Remittance File Name: " + str);
	}

	//------------------------------------------------------------------------------------------------------------------------------
	public void setDepositPeriod(String value) throws Exception{
        Select oSelection = new Select(depositPeriodDropDown());
        oSelection.selectByValue(value);      
        logger.info("        Selected Deposite Period: " + value);       
	}
	
	//-------------------------------------------------------------------------------------
	public void setDepositDateRangeFrom(String str) throws Exception{		
		depositDateRangeFromInput().sendKeys(str);
		depositDateRangeFromInput().sendKeys(Keys.TAB);
		logger.info("        Entered Deposite Date Range From: " + str);
	}

	//-------------------------------------------------------------------------------------
	public void setDepositDateRangeTo(String str) throws Exception{		
		depositDateRangeToInput().sendKeys(str);
		depositDateRangeToInput().sendKeys(Keys.TAB);
		logger.info("        Entered Deposite Date Range To: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setDepositAmountRangeFrom(String str) throws Exception{		
		depositAmountRangeFromInput().sendKeys(str);
		depositAmountRangeFromInput().sendKeys(Keys.TAB);
		logger.info("        Entered Deposite Amount Range From: " + str);
	}

	//-------------------------------------------------------------------------------------
	public void setDepositAmountRangeTo(String str) throws Exception{		
		depositAmountRangeToInput().sendKeys(str);
		depositAmountRangeToInput().sendKeys(Keys.TAB);
		logger.info("        Entered Deposite Amount Range To: " + str);
	}
	

}
