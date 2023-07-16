package com.overall.client.clientInquiry;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import com.xifin.mars.dao.DaoManagerXifinRpm;
import com.xifin.ssologin.SsoLogin;
import com.xifin.utils.SeleniumBaseTest;
import com.xifin.qa.config.PropertyMap;

public class PriceInquiry {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	private SeleniumBaseTest b;
	private DaoManagerXifinRpm daoManagerXifinRpm;
	
	public PriceInquiry(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}	
	
	//Page
	public String pageTitleText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('.blue')[0]).text()"); 
	}
	
	public WebElement clnIdInput() throws Exception {
		return driver.findElement(By.id("clnAbbrev"));	
	}
	
	public WebElement nameInput() throws Exception {
		return driver.findElement(By.id("clnNm"));	
	}
	
	public WebElement accountTypeInput() throws Exception {
		return driver.findElement(By.id("acnTyp"));	
	}	

	public WebElement resetBtn() throws Exception {
		return driver.findElement(By.id("btn_reset"));	
	}
	
	public WebElement submitBtn() throws Exception {
		return driver.findElement(By.id("btn_submit"));	
	}
	
    public WebElement helpBtnInFrame() throws Exception {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("$($($('#contentarea').contents()).contents().find('#helpbtn')[0]).click()");              
    }
    
    //Current Price
	public WebElement addRowBtn() throws Exception {
		return driver.findElement(By.id("addRowBtn"));	
	}
	
	public WebElement TestIdInput(int row) throws Exception {
		return driver.findElement(By.cssSelector("#prcInfoTbl > tbody > tr:nth-child(" + row + ") > td:nth-child(1) > input"));			
	}	
	
	public WebElement TestDescInput(int row) throws Exception {
		return driver.findElement(By.cssSelector("#prcInfoTbl > tbody > tr:nth-child(" + row + ") > td:nth-child(2)"));					
	}
	
	//Billable Procedure Code Information
	public WebElement procCdInfoClientTableText() throws Exception {		
		return driver.findElement(By.cssSelector("#procCdInfo > table:nth-child(2) > tbody > tr > td:nth-child(1)"));			
	}
	
	public WebElement procCdInfoNonClientTableText() throws Exception {
		return driver.findElement(By.cssSelector("#procCdInfo > table:nth-child(6) > tbody > tr > td:nth-child(1)"));	
	}
	
	public WebElement procCdInfoPyrSpecTableText() throws Exception {
		return driver.findElement(By.cssSelector("#procCdInfo > table:nth-child(10) > tbody > tr > td:nth-child(1)"));	
	}
	
	//-------------------------------------------------------------------------------------
	public void setTestId(String str, int row) throws Exception{			
		TestIdInput(row).sendKeys(str);
		TestIdInput(row).sendKeys(Keys.TAB);	
		logger.info("        Entered Test ID: " + str);
	}
	
	//-------------------------------------------------------------------------------------
	public void setClnId(String str) throws Exception{			
		clnIdInput().sendKeys(str);
		clnIdInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Client ID: " + str);
	}
	
}
