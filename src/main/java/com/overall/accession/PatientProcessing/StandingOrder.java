package com.overall.accession.PatientProcessing;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class StandingOrder {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	
	public StandingOrder(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	//Page
	public WebElement pageTitleText() throws Exception {
		return driver.findElement(By.className("blue"));
	}
	
	public WebElement epiInput() throws Exception {
		return driver.findElement(By.id("ptId"));
	}
	
	public WebElement clnIDInput() throws Exception {
		return driver.findElement(By.id("clnAbbrev"));
	}
	
	public WebElement clnNameInput() throws Exception {
		return driver.findElement(By.id("bilAcctNm"));
	}
	
	public WebElement ptSSNInput() throws Exception {
		return driver.findElement(By.id("ptSsn"));
	}
	
	public WebElement ptLNameInput() throws Exception {
		return driver.findElement(By.id("ptLNm"));
	}
	
	public WebElement ptFNameInput() throws Exception {
		return driver.findElement(By.id("ptFNm"));
	}
	
	public WebElement stdOrderIdDropDown() throws Exception {
		return driver.findElement(By.id("stdOrdrId"));
	}
	
	public WebElement authDtInput() throws Exception {
		return driver.findElement(By.id("authrztnDt"));
	}
	
	public WebElement orderingPhysNPIInput() throws Exception {
		return driver.findElement(By.id("npiId"));
	}
	
	public WebElement orderingPhysNameInput() throws Exception {
		return driver.findElement(By.id("ordrPhysNm"));
	}
	
	public WebElement physSignatureOnFileCheckBox() throws Exception {
		return driver.findElement(By.id("isPhysSof"));
	}
	
	public WebElement ptSignatureOnFileCheckBox() throws Exception {
		return driver.findElement(By.id("isPatSof"));
	}
	
	public WebElement deleteStandingOrderCheckBox() throws Exception {
		return driver.findElement(By.id("isDeletedChk"));
	}
	
	public WebElement copySOBtn() throws Exception {
		return driver.findElement(By.id("copyBtn"));
	}
	
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btn_submit"));
	}
	
	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.id("btn_reset"));
	}
	
	public WebElement errMessageText() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('#errorCodeBlockTbl > tbody > tr:nth-child(3) > td:nth-child(2)')[0]"); 
	}
	
	//Standing Order grid
	public WebElement addTestBtn() throws Exception {
		return driver.findElement(By.id("insrtRwBtn"));
	}
	
	public WebElement freqDropDown() throws Exception {
		return driver.findElement(By.id("freqId"));
	}
	
	public WebElement testIDInput() throws Exception {
		return driver.findElement(By.id("testAbbrev"));
	}
	
	public WebElement mod1DropDown() throws Exception {
		return driver.findElement(By.id("mod1"));
	}
	
	public WebElement mod2DropDown() throws Exception {
		return driver.findElement(By.id("mod2"));
	}
	
	public WebElement dxCodeInput() throws Exception {
		return driver.findElement(By.id("diagCdIds"));
	}
	
	public WebElement abnRecdCheckBox() throws Exception {
		return driver.findElement(By.id("abnRecdChk-1"));
	}
	
	//After the record got saved, the ABN Rec'd checkbox id changed to a different id
	public WebElement savedABNRecdCheckBox() throws Exception {
		return driver.findElement(By.id("abnRecdChk0"));
	}
	
	public WebElement abnRsnDropDown() throws Exception {
		return driver.findElement(By.id("abnRsn"));
	}
	
	public WebElement abnComntInput() throws Exception {
		return driver.findElement(By.id("abnCmnt"));
	}

	//-------------------------------------------------------------------------------------
	public void setABNComment(String str) throws Exception{		
		abnComntInput().sendKeys(str);
		abnComntInput().sendKeys(Keys.TAB);			
		logger.info("        Entered ABN Comment: " + str);
	}		
	
	//-------------------------------------------------------------------------------------
	public void setDxCode(String str) throws Exception{		
		dxCodeInput().sendKeys(str);
		dxCodeInput().sendKeys(Keys.TAB);			
		logger.info("        Entered Dx Code: " + str);
	}		
	
	//-------------------------------------------------------------------------------------
	public void setTestID(String str) throws Exception{		
		testIDInput().sendKeys(str);
		testIDInput().sendKeys(Keys.TAB);			
		logger.info("        Entered Test ID: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setOrderingPhysNPI(String str) throws Exception{		
		orderingPhysNPIInput().sendKeys(str);
		orderingPhysNPIInput().sendKeys(Keys.TAB);			
		logger.info("        Entered Ordering Physician NPI: " + str);
	}		
	
	//-------------------------------------------------------------------------------------
	public void setAuthDt(String str) throws Exception{		
		authDtInput().sendKeys(str);
		authDtInput().sendKeys(Keys.TAB);			
		logger.info("        Entered Authorization Date: " + str);
	}		
	
	//-------------------------------------------------------------------------------------
	public void setClnID(String str) throws Exception{		
		clnIDInput().sendKeys(str);
		clnIDInput().sendKeys(Keys.TAB);			
		logger.info("        Entered Client ID: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setEPI(String str) throws Exception{		
		epiInput().sendKeys(str);
		epiInput().sendKeys(Keys.TAB);			
		logger.info("        Entered EPI: " + str);
	}

}
