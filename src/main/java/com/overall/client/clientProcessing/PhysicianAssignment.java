package com.overall.client.clientProcessing;

import java.util.List;

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

public class PhysicianAssignment {
	
	private RemoteWebDriver driver;	
	protected Logger logger;
	private SeleniumBaseTest b;
	private DaoManagerXifinRpm daoManagerXifinRpm;
	
	public PhysicianAssignment(RemoteWebDriver driver){
		this.driver = driver;
		logger = Logger.getLogger(this.getClass().getName() + "],[" + driver);
	}		
	
	public String pageTitleText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('.blue')[0]).text()"); 
	}	
	
	public String errMsgText() throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return $($('#errorCodeBlockTbl > tbody > tr:nth-child(3) > td:nth-child(2)')[0]).text()"); 
	}
	
	public WebElement clnIdInput() throws Exception {
		return driver.findElement(By.id("clnAbbrev"));	
	}
	
	public WebElement nameInput() throws Exception {
		return driver.findElement(By.id("clnNm"));	
	}
	
	public WebElement acctTypInput() throws Exception {
		return driver.findElement(By.id("acctTyp"));	
	}
	
	public WebElement npiInput() throws Exception {
		return driver.findElement(By.id("npiId"));	
	}
	
	public WebElement newNPIInput() throws Exception {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return $('[class=\"tbbdy1\"] #npiId')[0]"); 
	}	
	
	public WebElement upinInput() throws Exception {
		return driver.findElement(By.id("upinId"));	
	}
	
	public WebElement lNameInput() throws Exception {
		return driver.findElement(By.id("lNm"));	
	}
	
	public WebElement fNameInput() throws Exception {
		return driver.findElement(By.id("fNm"));	
	}
	
	public WebElement zipInput() throws Exception {
		return driver.findElement(By.id("zip"));	
	}
	
	public WebElement stateInput() throws Exception {
		return driver.findElement(By.id("state"));	
	}
	
	public WebElement specialtyInput() throws Exception {
		return driver.findElement(By.id("spec"));	
	}
	
	public WebElement credentialsInput() throws Exception {
		return driver.findElement(By.id("cred"));	
	}
	
	public WebElement copyPhysAssignToClnIdInput() throws Exception {
		return driver.findElement(By.id("cpyClnAbbrev"));	
	}
	
	public WebElement deleteCheckBox() throws Exception {
		return driver.findElement(By.id("isDelete"));	
	}
	
	public WebElement addPhysBtn() throws Exception {
		return driver.findElement(By.id("addPhys"));	
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

	//-------------------------------------------------------------------------------------
	public void setCopyClnId(String str) throws Exception{		
		copyPhysAssignToClnIdInput().sendKeys(str);
		copyPhysAssignToClnIdInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Copy physician assignments to Client ID: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setUPIN(String str) throws Exception{		
		upinInput().sendKeys(str);
		upinInput().sendKeys(Keys.TAB);	
		logger.info("        Entered UPIN: " + str);
	}		
	
	//-------------------------------------------------------------------------------------
	public void setNewNPI(String str) throws Exception{		
		newNPIInput().sendKeys(str);
		newNPIInput().sendKeys(Keys.TAB);	
		logger.info("        Entered NPI: " + str);
	}		
	
	//-------------------------------------------------------------------------------------
	public void setNPI(String str) throws Exception{		
		npiInput().sendKeys(str);
		npiInput().sendKeys(Keys.TAB);	
		logger.info("        Entered NPI: " + str);
	}	
	
	//-------------------------------------------------------------------------------------
	public void setClnID(String str) throws Exception{		
		clnIdInput().sendKeys(str);
		clnIdInput().sendKeys(Keys.TAB);	
		logger.info("        Entered Client ID: " + str);
	}
	
	// ----------------------------------------------------------------------------------------------------------------------------------------
	//This method returns the first 5 digits zip code if the zip in db is not empty
	public String getZip(String zip) throws Exception {		
		String zipStr = null;
		
		if(!(zip.isEmpty())){
			zipStr = zip.substring(0, 4);
		}else{
			zipStr = "";
		}		
		
		return zipStr;
	}
	
}
